import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Test {
	
	public static double Part2Greedy(double investment[][],double money[],double fee)
	{
		double percentage = 1-fee; //QUICK USAGE OF TAX
		int company[] = new int [money.length]; //STORING LAST COMPANY INDEX.
		double total[] = new double[money.length];  // TOTAL MONEY THAT BE WÝTHDRAWN FROM COMPANY.
		double max = 0;   //STORE MAX PROFIT.
		double temp = 0;  //TEMP.
		int colTemp = -1; // STORING CURRENT COMPANY INDEX.
		
		// MONTH LOOP.
		for (int i = 0; i < investment.length; i++) 
		{
			colTemp = -1; // INTIALIZE IN EVERY TURN.
			
			//COMPANY LOOP.
			for (int j = 0; j < investment[0].length; j++) 
			{
					temp = max; 
					max = Math.max(investment[i][j],max); // COMPARE EACH MONTH CORRESPONDING TO EACH MONTHS INVESTMENT RATE.
					if(temp != max)
						colTemp = j; // CHANGE INDEX IF FIND NEW MAX PROFIT.
			}
			// FIRST MONTH. 
			if(i == 0)
			{
				total[i] = (max/100)*(money[i]/2) + (money[i]/2); //INCREMENT OF FIRST MONTH. (HALF OF FIRST MONTH * FEE) + HALF OF FIRST MONTH. 
				company[i] = colTemp; //SAVE COMPNAY INDEX.
			}
			// OTHER MONTHS.
			else 
			{	
				// IF MAX PROFIT COMPANY INDEX IS SAME AS PREVIOUS MONTH.
				if(company[i-1]==colTemp)
				{
					// (HALF MONTH OF PREVIOUS MONTH + HALF OF CURRENT MONTH) * FEE + (HALF MONTH OF PREVIOUS MONTH + HALF OF CURRENT MONTH)
					total[i]=(max/100)*(money[i]/2+total[i-1]+money[i-1]/2)+(money[i]/2+total[i-1]+money[i-1]/2);
					company[i] = colTemp; //SAVE SAME COMPANY INDEX.
				}
				// IF MAX PROFIT COMPANY INDEX IS NOT SAME AS PREVIOUS MONTH.
				else 
				{
					//NEW MONEY AMOUNT, IF WE CHANGE THE COMPANY WÝTH PAYING TAX.
					//(HALF MONTH OF PREVIOUS MONTH * TAX + HALF OF CURRENT MONTH) * FEE + (HALF MONTH OF PREVIOUS MONTH * TAX + HALF OF CURRENT MONTH)
					double different =(total[i-1]*percentage+money[i]/2+money[i-1]/2)*(max/100)+(total[i-1]*percentage+money[i]/2+money[i-1]/2);
					//NEW MONEY AMOUNT, IF WE DONT CHANGE THE COMPANY.
					//(HALF MONTH OF PREVIOUS MONTH + HALF OF CURRENT MONTH) * FEE + (HALF MONTH OF PREVIOUS MONTH + HALF OF CURRENT MONTH)
					double same	=(total[i-1]+money[i]/2+money[i-1]/2)*(investment[i][company[i-1]]/100)+(total[i-1]+money[i]/2+money[i-1]/2);
					// CONTROLLING THESE MONEY TO CHOOSE COMPANY.
					// AND SAVING INDEX VS.
					if(different > same)
					{
						total[i]=different;
						company[i] = colTemp;
					}
					else 
					{
						total[i]=same;
						company[i] = company[i-1];
					}//ELSE END.
				}//ELSE END.
			}//ELSE END.
			max = 0; //REFRESH THE MAX VALUE FOR NEW TURN.
		}//FOR END.
		total[total.length-1]+=money[money.length-1]/2; // ADDING HALF OF LAST MONTH.
			
		
		for (int i = 0; i < total.length; i++) {
			System.out.print(company[i] + " ");
		}
		System.out.println();
		
		return total[total.length- 1];
	}
	
	
	public static double Part2Dynamic(double investment[][],double money[],double fee)
	{
		double percentage = 1-fee; //QUICK USAGE OF TAX
		double total[][] = new double[investment.length][investment[0].length];  // TOTAL MONEY THAT BE WÝTHDRAWN FROM COMPANY.
		double max = 0;   //STORE MAX PROFIT.
		double same = 0;
		double different = 0;
		
		// MONTH LOOP.
		for (int i = 0; i < investment.length; i++) 
		{
			//COMPANY LOOP.
			for (int j = 0; j < investment[0].length; j++) 
			{
				if(i == 0)
				{
					total[i][j] = (investment[i][j]/100)*(money[i]/2) + (money[i]/2); //INCREMENT OF FIRST MONTH. (HALF OF FIRST MONTH * FEE) + HALF OF FIRST MONTH. 
				}
				// OTHER MONTHS.
				else 
				{	
					for (int k = 0; k < investment[0].length; k++) 
					{
						if(k==j)
							//(HALF MONTH OF PREVIOUS MONTH + HALF OF CURRENT MONTH) * FEE + (HALF MONTH OF PREVIOUS MONTH + HALF OF CURRENT MONTH)
							same = (total[i-1][j]+money[i]/2+money[i-1]/2)*(investment[i][j]/100)+(total[i-1][j]+money[i]/2+money[i-1]/2);
						else
						{
							//(HALF MONTH OF PREVIOUS MONTH * TAX + HALF OF CURRENT MONTH) * FEE + (HALF MONTH OF PREVIOUS MONTH * TAX + HALF OF CURRENT MONTH)
							different =(total[i-1][k]*percentage+money[i]/2+money[i-1]/2)*(investment[i][j]/100)+(total[i-1][k]*percentage+money[i]/2+money[i-1]/2);
							max = Math.max(max, different);
						}
					}
							
					total[i][j] = Math.max(same, max);
				}//ELSE END.
							
				max = 0; //REFRESH THE MAX VALUE FOR NEW TURN.
			}//LOOP END.
					// FIRST MONTH. 
		}//FOR END.
		
		max  = 0;
		for (int i = 0; i < total[0].length; i++) {
				max = Math.max(max, total[total.length-1][i]);	
		}
		return max + money[money.length-1]/2;
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		
		//int p = 6; 
		//int d=6; 
		int x= 25; 
		double t = 2; 
		int B=100;                         	// vehicle cost
		int c = 5;
		
		
		double demands [] = new double[x];						  // vehicle demands of each month.
		double investment[][] = new double[x][c];				  // each company offers.
		
		
		// READING DEMAND.
		BufferedReader br = new BufferedReader(new FileReader("month_demand.txt"));
		String st = br.readLine(); //to pass first line.
		int count = 0;
		String splitted[];
		while ((st = br.readLine()) != null && count < x) {
	            splitted = st.split("\t");
	            demands[count] = Integer.parseInt(splitted[1]);
	            count++;
	    }
	    br.close();
	    
	    //READING INVESTMENT
	    br = new BufferedReader(new FileReader("investment.txt"));
	    st = br.readLine(); //to pass first line.
		count = 0;
		st = null;
		String temp[] = new String[c];
		while ((st = br.readLine()) != null && count < x) {
				temp = st.split("\t");
	            for (int i = 1; i <= c; i++) {
	            	investment[count][i-1] = Integer.parseInt(temp[i]);
				}
	            count++;
	    }
	    br.close();
	    //
	    //
	    //
		
		double fee = t/100;									  // tax rate.
		double money[] = new double [demands.length];					  // money for each month.
		
		for (int i = 0; i < money.length; i++) 				  // for loop (arranging money array)
		{
			money[i] = demands[i]*B;
		}
															  // for loop end..
		
		System.out.println(Part2Greedy(investment,money,fee));
		System.out.println(Part2Dynamic(investment,money,fee));
	}

}
