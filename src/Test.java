import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Test {
	
	public static int ProfitGreedy(double investment[][],double money[],double fee)
	{
		double percentage = 1-fee; //QUICK USAGE OF TAX
		int company[] = new int [money.length]; //STORING LAST COMPANY INDEX.
		double total[] = new double[money.length];  // TOTAL MONEY THAT BE WİTHDRAWN FROM COMPANY.
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
					//NEW MONEY AMOUNT, IF WE CHANGE THE COMPANY WİTH PAYING TAX.
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
		
		return (int)total[total.length- 1];
	}
	
	
	public static int ProfitDynamic(double investment[][],double money[],double fee)
	{
		double percentage = 1-fee; //QUICK USAGE OF TAX
		double total[][] = new double[investment.length][investment[0].length];  // TOTAL MONEY THAT BE WİTHDRAWN FROM COMPANY.
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
							different =((total[i-1][k]*percentage)+money[i]/2+money[i-1]/2)*(investment[i][j]/100)+((total[i-1][k]*percentage)+money[i]/2+money[i-1]/2);
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
		max += money[money.length-1]/2;
		
		return (int) max;
	}
	
	
	public static int CostDynamic(int p,int d,int[] demands,int[] garageCost)
	{
		int[][] minCost = new int[demands.length][garageCost.length]; // STORING MIN COST FOR EACH MONTH.
		int missing = 0;  // MISSING CAR AMOUNT FOR EACH MONTH.
		int extra = 0;    // CAPACITY - DEMAND.
		int min = Integer.MAX_VALUE; // TEMP VALUE.
		int intern = 0;  // INTERN COUNTER.
		
		for (int i = 0; i < demands.length; i++)  // MONTH LOOP.
		{
			for (int j = 0; j < garageCost.length; j++) //GARAGE COST LOOP.
			{
				if(i==0)
					minCost[i][j] = garageCost[j];   //IF WE START TO FIRST MONTH WITH CARS, FIRST WE MUST PAY THE GARAGE COST.
				else
				{
					if(demands[i]>=p)   //IF DEMAND BIGGER THAN CAPACITY.
					{
						missing = demands[i] - p;  //DETERMINE MISSING VALUE AND EXTRA.
						extra = 0;
					}
					else  //IF CAPACITY BIGGER THAN DEMAND.
					{
						missing = 0;
						extra = p - demands[i];
					}
					for (int k = 0; k < garageCost.length; k++) //SECOND GARAGE LOOP TO DETERMINE BEST WAY.
					{
						
						if(missing !=0) //IF THERE IS A MISSING.
						{
							intern =  missing + j - k; //DETERMINING INTERN (MISSING + NEEDED CAR AFTER DEMAND - STORED CAR)
							if(intern>=0)
							{
								min = Math.min(min, minCost[i-1][k]+garageCost[j]+d*intern); // CALCULATE NEW COST FOR THIS MONTH 
							}
						}
						else if(extra !=0) // IF THERE ARE EXTRA CAPACITY.
						{
							intern = j - k -extra; // DETERMINING INTERN (NEEDED CAR AFTER DEMAND  - STORED CAR - EXTRA CARS. )
							if(intern>=0)
							{
								min = Math.min(min, minCost[i-1][k]+garageCost[j]+d*intern); // CALCULATE NEW COST FOR THIS MONTH 
							}
							else
								min = Math.min(min, minCost[i-1][j]+garageCost[j]); // CALCULATE NEW COST FOR THIS MONTH 
						}
						else
						{
							min = Math.min(min, minCost[i-1][j]+garageCost[j]);
						}
						
					}
					minCost[i][j] = min;
					min = Integer.MAX_VALUE;
				}
			}
		}
			min =  Integer.MAX_VALUE;
			for (int j = 0; j < minCost[0].length; j++) {
				
				min = Math.min(min, minCost[minCost.length-1][j]);
			}
		
			return min;
		
	}
	
	
	public static int CostGreedy(int p,int d,int[] demands)
	{
		// JUST USING INTERNS TO PROVIDE DAMENDED CARS.
		// THERE CAN BE ONE MORE SOLUTION :
		// 						JUST FOR FIRST MONTH WE CAN USE GARAGE. IT MIGHT MAKE DIFFERENCE A BIT.
		// 						IN THIS CODE I DO NOT USE GARAGE COST.
		int cost = 0;
		for (int i = 0; i < demands.length; i++) 
		{
			int missing = demands[i] - p;
			if(missing > 0)
				cost += missing * d; 
		}
		return cost;
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		
		int p = 5; 
		int d = 5; 
		int x = 3; 
		double t = 2; 
		int B = 100;                         	// vehicle cost
		int c = 3;
		
		int[] costDemand = new int[x+1];						   //	vehicle demands of each month. (Part 1)
		double demands [] = new double[x];						  // vehicle demands of each month.  (Part 2)
		double investment[][] = new double[x][c];				  // each company offers.
		int sum = 0;  											 // sum of total car demand.
		
		
		// READING DEMAND.
		BufferedReader  br = new BufferedReader(new FileReader("month_demand.txt"));
		String st = br.readLine(); //to pass first line.
		int count = 0;
	    String splitted[];
		costDemand[0] = 0;
		while ((st = br.readLine()) != null && count < x) {
	            splitted = st.split("\t");
	            demands[count] = Integer.parseInt(splitted[1]);
	            costDemand[count+1] = Integer.parseInt(splitted[1]);
	            count++;
	    }
	    br.close();
	    
	    for (int i = 0; i < costDemand.length; i++) {
	    	sum += costDemand[i];

	    }
	    
	    // GARAGE COST.
	    int garageCost[] = new int[sum+1];
	    garageCost[0] = 0;
	    br = new BufferedReader(new FileReader("garage_cost.txt"));
	    st = br.readLine(); //to pass first line.
	    count = 1;
	    while ((st = br.readLine()) != null && count <= sum) {
	    	splitted = st.split("\t");
	    	garageCost[count] = Integer.parseInt(splitted[1]);
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
		double money[] = new double [demands.length];			// money for each month.
		
		for (int i = 0; i < money.length; i++) 				  // for loop (arranging money array)
		{
			money[i] = demands[i]*B;
		}
															  // for loop end..
		
		System.out.println("DP Cost = " + CostDynamic(p,d,costDemand,garageCost));
		System.out.println("DP Profit = " + ProfitDynamic(investment,money,fee));
		System.out.println("--------------------------");
		System.out.println("Greedy Cost = " + CostGreedy(p,d,costDemand));
	    System.out.println("Greedy Profit = " + ProfitGreedy(investment,money,fee));
	}

}
