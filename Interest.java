package org.extras;

//import 
//import java.math.BigDecimal;
//import java.util.Date; 
//import java.util.Arrays;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.*;
import java.util.*; 
import org.extras.DateA;
import org.extras.InterestRateTable;

//mimics the Calculation class

public class Interest {
 
	private String operation;
    private BigDecimal amount;
    private DateA startDate;
    private DateA endDate;
    private Integer compoundDays;  //# times to compount per year
    private InterestRateTable tbl;
    private BigDecimal output;
    
     
    
    
    public Interest() {
    	amount = BigDecimal.valueOf(0);
    	startDate = new DateA("1-1-1900");
    	endDate = new DateA("1-1-1900");
    	compoundDays = 0;
    	output = BigDecimal.valueOf(0);

    }
    
    public Interest(String operation, BigDecimal amount, DateA startDate, DateA endDate, Integer compoundDays, InterestRateTable tbl) {
        super();
        this.operation = operation;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.compoundDays = compoundDays;
        this.tbl = tbl;        
    }

    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public DateA getStartDate() {
        return startDate;
    }
    
    public void setStartDate(DateA startDate) {
        this.startDate = startDate;
    }
    
    public DateA getEndDate() {
        return endDate;
    }
    
    public void setEndDate(DateA endDate) {
        this.endDate = endDate;
    }
    
    public Integer getCompoundDays() {
        return compoundDays;
    }
    
    public void setCompoundDays(Integer compoundDays) {
        this.compoundDays = compoundDays;
    }
    
    public InterestRateTable getTbl() {
        return tbl;
    }
    
    public void setTbl(InterestRateTable tbl) {
        this.tbl = tbl;
    }
    
    public BigDecimal getOutput() {
        return output;
    }
    
    public void setOutput(BigDecimal output) {
        this.output = output;
    }  
    
    public String toString() {
    	String strInterest = "\n----Interest Problem ----";
    	//System.out.println(strInterest);

    	strInterest = strInterest + "\n________________________________________________________________";
    	//System.out.println(strInterest);
    	strInterest = strInterest + "\nOperation :              " + operation;

    	strInterest = strInterest + "\nAmount :                 " + amount.toString() ;
    	//System.out.println(strInterest);

    	strInterest = strInterest + "\nStart Date :             " + startDate.toString() ;
    	//System.out.println(strInterest);

    	strInterest = strInterest + "\nEnd Date :               " + endDate.toString() ;
    	//System.out.println(strInterest);

    	strInterest = strInterest + "\nCompound Days per Year : " + compoundDays.toString() ;
    	//System.out.println(strInterest);

    	strInterest = strInterest + "\n\t\t--Interest Table -- ";
    	//System.out.println(strInterest);

    	strInterest = strInterest + "\n________________________________________________________________\n";
    	//System.out.println(strInterest);

    	strInterest = strInterest + tbl.toString() ;
    	//System.out.println(strInterest);

    	strInterest = strInterest + "\n________________________________________________________________";
    	//System.out.println(strInterest);

    	strInterest = strInterest + "\nOutput :\t\t" + output.toString() ;       	
    	//System.out.println(strInterest);
    	strInterest = strInterest + "\n\n";
    	return strInterest;
    }
    
    public void calcTotalInterest() {
    	
    	//initialize
    	DateA intThroughDate = new DateA();
    	intThroughDate.setDate(this.startDate.getMM(), this.startDate.getDD(), this.startDate.getYYYY());
    	int compoundIntervalDays  = 0;
    	int intRateIntervalDays  = 0;
    	int endPeriodIntervalDays  = 0;
    	int appliedDays  = 0;
    	int totalDays = 0;
    	BigDecimal simpleInt = new BigDecimal(0.0);
    	BigDecimal compInt = new BigDecimal(0.0);
    	
    	int annualDays = 0;
    	double yearlyIntRate = 0.0;
    	double factor = 0.0;       //dailyIntRate * applied days
    	BigDecimal dailyIntRate = new BigDecimal(0.0);
    	BigDecimal factorBD = new BigDecimal(0.0);
    	BigDecimal priorInterest = new BigDecimal(0.0);
    	BigDecimal currentInterest = new BigDecimal(0.0);
    	BigDecimal totalSimple = new BigDecimal(0.0);
    	BigDecimal totalComp = new BigDecimal(0.0);
    	BigDecimal dailyInterest = new BigDecimal(0.0);
    	BigDecimal totalInterest = new BigDecimal(0.0);
    	
    	boolean ccontinue = true;
    	int loopNum = 0;
    	String errorMsg = "MSG:";
    	
    	//error checking
    	if (this.endDate.greaterThan(this.tbl.lastDate()  )  ){
    		errorMsg += " Warning - end date exceeds rate table.";
    	}
    	if (this.startDate.lessThan(this.tbl.firstDate()  )  ){
    		errorMsg += " Warning - start date preceeds rate table.";
    	}
    	
//    	 __        ______     ______   .______   
//    	 |  |      /  __  \   /  __  \  |   _  \  
//    	 |  |     |  |  |  | |  |  |  | |  |_)  | 
//    	 |  |     |  |  |  | |  |  |  | |   ___/  
//    	 |  `----.|  `--'  | |  `--'  | |  |      
//    	 |_______| \______/   \______/  | _|   
    	
    	
    	
    	while (intThroughDate.lessThan(endDate) && ccontinue == true) {
    		//RATE
    		yearlyIntRate = setIntRate(intThroughDate);
    		dailyIntRate = new BigDecimal(Double.toString(yearlyIntRate)); 
    			//System.out.print("\nyearlyRate: " + yearlyIntRate + "  daily rate: " + dailyIntRate.toString() + " annual days: " + annualDays);
    		annualDays = getAnnualDays(intThroughDate.getYYYY());
				//System.out.print("\nyearlyRate: " + yearlyIntRate + "  daily rate: " + dailyIntRate.toString() + " annual days: " + annualDays);
    		dailyIntRate = dailyIntRate.divide(new BigDecimal(annualDays), 16, RoundingMode.HALF_UP );
    		//a.divide(b, 2, RoundingMode.HALF_UP)
    		//dailyIntRate = dailyIntRate.divide(new BigDecimal(annualDays), RoundingMode.HALF_EVEN)  ));
    			System.out.print("\nyearlyRate: " + yearlyIntRate + "  daily rate: " + dailyIntRate.toString() + " annual days: " + annualDays);
    		
    		//TESTING MSG
    		System.out.print("\nRATE");
    		//System.out.print("\nyearly rate: " + yearlyIntRate);
    		System.out.print("\nTotalDays: "+ totalDays);
    		System.out.print("\nCurrent Interest: "+ currentInterest.toString());
    		System.out.print("\nPlacemarker: " + intThroughDate.toString());
    		System.out.print("\nError Msg: " + errorMsg);
    		System.out.print("\nStart Date: " + this.startDate.toString() + " End Date: " + this.endDate.toString());
    		
    		
    		//DAY INTERVALS
    		compoundIntervalDays = checkDaysCompound(intThroughDate);
    		intRateIntervalDays = checkDaysIntRate(intThroughDate);
    		endPeriodIntervalDays = checkDaysEnd(intThroughDate);
    		
    		System.out.print("\n DAY INTERVALS");
    		System.out.print("\nTotalDays: "+ totalDays);
    		System.out.print("\nCurrent Interest: "+ currentInterest.toString());
    		System.out.print("\nPlacemarker: " + intThroughDate.toString());
    		System.out.print("\nError Msg: " + errorMsg);
    		System.out.print("\nStart Date:" + this.startDate.toString() + " End Date: " + this.endDate.toString());
    		
    		
    		//APPLY INTEREST TO HOW MANY DAYS?
    		appliedDays = min3(compoundIntervalDays, intRateIntervalDays, endPeriodIntervalDays );
    		System.out.print("\n\nApplied Days = " + appliedDays + "\n");
    		
    		//CALC
    		
    		//simpleInt = (appliedDays * dailyIntRate * amount);
    		//compInt = (appliedDays * dailyIntRate * priorInterest);
    		//currentInterest = currentInterest + simpleInt + compInt;
    		
    			System.out.println("daily int rate "	+ dailyIntRate);
    	//	factor = 
    	//		System.out.println("factor "	+ factor);
    		factorBD = dailyIntRate.multiply( new BigDecimal(  Integer.toString(appliedDays)  )  )   ;
    		//factorBD = new BigDecimal(Double.toString(factor)) ;
    			System.out.println("factorBD "	+ factorBD.toString());
    			System.out.println("amount "	+ amount.toString());
    		simpleInt = amount.multiply(factorBD) ;
    			System.out.println("simpleInt = "	+ simpleInt + " = amount * factorBD ");
    		compInt = priorInterest.multiply(factorBD) ;    	
    			System.out.println("priorInterest = "	+ priorInterest.toString()  );
    			System.out.println("compInt = "	+ compInt + " = priorInt * factorBD");
    		currentInterest = currentInterest.add(simpleInt) ;
    			System.out.println("currentInterest 1 = "	+ currentInterest + " = currentInterest + simpleInt");
    		currentInterest = currentInterest.add(compInt) ;
    			System.out.println("currentInterest 2 "	+ currentInterest + "= currentInterest + compInt ");
    		totalSimple = totalSimple.add(simpleInt) ;
    			System.out.println("totalSimple : "	+ totalSimple + " = totalSpimple + simpleInt");
    		totalComp = totalComp.add(compInt) ;
    			System.out.println("totalComp "	+ totalComp + " = totalComp + compInt");
    		
    		
    		System.out.print("\nCALC");
    		System.out.print("\nTotalDays: "+ totalDays);
    		System.out.print("\nCurrent Interest: "+ currentInterest.toString());
    		System.out.print("\nPlacemarker: " + intThroughDate.toString());
    		System.out.print("\nError Msg: " + errorMsg);
    		System.out.print("\nStart Date:" + this.startDate.toString() + " End Date: " + this.endDate.toString());
    		//no problem with start date (after this point problem occurs)
    		
    		//COMPOUNDING
    		if (appliedDays == compoundIntervalDays ) {
    			priorInterest = priorInterest.add(currentInterest);  //yearly accumulation added to compounding pile of interest
    			currentInterest = new BigDecimal("0.0");   //yearly accumulation resets to 0
    		}
    		
    		//ERROR CHECKING
    		if(appliedDays < 1) {
    			ccontinue = false;
    			errorMsg += " Error - appliedDays is less than 1.";
    		}
    		loopNum = loopNum +1;
    		if(loopNum > 10000) {
    			ccontinue = false;
    			errorMsg += " Error - Infinite loop.";
    		}
    		
    		//INCREMENT PLACEMARKER IN LOOP
    		totalDays = totalDays + appliedDays;
    		intThroughDate.addDays(appliedDays);   //verify   		
    		
    		//TESTING MSG
    		System.out.print("\nPrep LOOP");
    		System.out.print("\nTotalDays (accum applied days): "+ totalDays);
    		System.out.print("\nAmount : " + this.amount.toString());
    		System.out.print("\nCurrent Interest: "+ currentInterest.toString());
    		System.out.print("\nPrior Interest: "+ priorInterest.toString());    		
    		System.out.print("\nPlacemarker: " + intThroughDate.toString());
    		System.out.print("\nError Msg: " + errorMsg);
    		//problem start date + 1
    		System.out.print("\nStart Date:" + this.startDate.toString() + " End Date: " + this.endDate.toString());
    		System.out.print("\nLooping\n\n");
    		
    	}//end LOOP
    	
    	//OUTPUT
    	this.output = totalSimple.add(totalComp);
    	//dailyInterest = dailyIntRate * (amount + priorInterest);
    	dailyInterest = amount.add(priorInterest);
    	dailyInterest = dailyInterest.multiply(dailyIntRate)  ;
    	
    	//Error Check
//    	if (abs(totalInterest - (priorInterest + currentInterest)   ) > 0.1 ) {
//		errorMsg = "Error - Totals do not match";
//	    }
    	
    	BigDecimal errorBD = new BigDecimal(0.0);
    	errorBD = priorInterest.add(currentInterest);
    	errorBD = totalInterest.subtract(priorInterest);
    	double errorD = errorBD.doubleValue();
    	if (absD(errorD) > 0.1) {
    		errorMsg += " ERROR - Totals do not match.";
    	}
    	
    	//rounding function ?
    	
    }//end algorithm method
    
    
    public int getAnnualDays(int yyyy) {
    	if(yyyy%4 == 0 ){
    		return 366;
    	}else {
    		return 365;
    	} 
    }
    
    public double absD(double in) {
    	double out;
    	if (in < 0) {
    		out = in * -1;
    	}else {
    		out = in;
    	}    		
    	return out;
    }
    
    
    public double setIntRate(DateA placemarker) {
    	double rate = 0.0;
    	//go through this.tbl;
    	rate = this.tbl.getRate(placemarker);
    	//System.out.println("rate:  " + rate);
    	return rate;    	
    }
    
    public int checkDaysCompound(DateA placemarker) {
    	int days = 0;
    	DateA nextCD = new DateA();
    	nextCD = nextCompoundDate(placemarker);
    	//System.out.println("nextCompDate" + nextCD.toString() + "\n");
    	//System.out.println("placemarker" + placemarker.toString() + "\n");
    	days = DateA.daysBetweenDates(nextCD, placemarker);    
    	//System.out.println("days compound = " + days);
    	return days;
    }
    
    public DateA nextCompoundDate(DateA placemarker) {
    	//System.out.println("\ncheckDaysCompound - placemarker start: "+ placemarker.toString());
    	DateA nextCD = new DateA();
    	int cd = this.compoundDays;
    	//  if cd = 1, annual, every 12/31/xxxx
    	// if cd = 12, monthly, every 
    	// if cd = 365, daily, days +1
    	int mm = 12;
    	int dd = 31;
    	int yyyy = 2099;
    	
    	if(cd == 1) {
    		mm = 1;
    		dd = 1;
    		yyyy = placemarker.getYYYY() +1;
    	}else if (cd == 12){
    		mm = placemarker.getMM() +1;
    		dd = 1;
    		yyyy = placemarker.getYYYY();
    		if(mm > 12) {
    			mm = 1;
    			yyyy = yyyy + 1;
    		}
    	}else if (cd == 365) {
    		mm = placemarker.getMM();
    		dd = placemarker.getDD() +1;
    		yyyy = placemarker.getYYYY();
    		if(mm == 1 || mm == 3 || mm == 5 || mm == 7 || mm == 8 || mm == 10 || mm == 12) {
    			if (dd > 31){
    				mm ++;
    				dd = 1;
    				if(mm > 12) {
    	    			mm = 1;
    	    			yyyy = yyyy + 1;
    	    		}
    			}
    			
    		}else if ( mm == 2) {
    			if(dd>28) {
    				mm++;
    				dd = 1;
    			}
    		}else {
    			if(dd>30) {
    				mm++;
    				dd = 1;
    			}
    		}
    	}//end build  mm/dd/yyyy
    	nextCD.setDate(mm, dd, yyyy);   
    	//System.out.println("checkDaysCompound - placemarker end: "+ placemarker.toString());
    	//System.out.println("checkDaysCompound - next comp dt: "+ nextCD.toString());
    	return nextCD;    	
    }
    
    
    public int checkDaysIntRate(DateA placemarker) {
    	//System.out.println("checkDaysIntRate - placemarker start: "+ placemarker.toString());
    	int days = 0;
    	DateA b = this.tbl.getNextRateDate(placemarker);
    	//System.out.println("checkDaysIntRate - placemarker end: "+ placemarker.toString() + "   Days = "+ days);

    	//System.out.println("checkDaysIntRate - next rate date end: "+ b.toString() + "   Days = "+ days);
    	days = DateA.daysBetweenDates(placemarker, b);


    	return days;
    }
    
    public int checkDaysEnd(DateA placemarker) {

    	//System.out.println("checkDaysEnd - placemarker start: "+ placemarker.toString());
    	int days = 0;
    	days = DateA.daysBetweenDates(placemarker, this.endDate);
    	
    	//System.out.println("checkDaysEnd - placemarker end: "+ placemarker.toString() + "days till end = " + days);
    	return days;
    }
    
    
    public int min3(int a, int b, int c) {
    	int min = Integer.MAX_VALUE;
    	if (a < min) {
    		min = a;
    	}
    	if (b < min) {
    		min = b;
    	}
    	if (c < min) {
    		min = c;
    	}
    	return min;
    }
    
    
}//end class


/*
 * Principle (Currency), 
 * Start Date (Date), 
 * End Date (Date), 
 * Compounding Rate in Days (Integer), 
 * Interest rate table ([date],[double] matrix)


public class Interest {
	//C represents class variable
	//BigDecimal dollar = new BigDecimal("0.0");
	BigDecimal cdollar;
	Calendar cstartDate;
	Calendar cendDate;
	int ccompoundingDays; 
	InterestRateTable tbl;
	
	
	public Interest(BigDecimal dollar, Calendar startDate, Calendar endDate, int compoundingDays){
		this.cdollar = dollar;
		this.cstartDate = startDate;
		this.cendDate = endDate;
		this.ccompoundingDays = compoundingDays;
		
		
	}

	public Interest(){
		this.cdollar = new BigDecimal("0.0");
		this.cstartDate = Calendar.getInstance();
		this.cstartDate.clear();
		this.cendDate =  Calendar.getInstance();
		this.cendDate.clear();
		this.ccompoundingDays = 0;
	}
	
}
  */

/*
Inputs
Principle (Currency), Start Date (Date), End Date (Date), Compounding Rate in Days (Integer), Interest rate table ([date],[double] matrix) 
Outputs
Total Interest, Simple Interest, Compound Interest, Prior Year Interest, Current Year Interest, Average Interest Rate, Total Days
Process
Set Date Marker to Start Date
WHILE Date Marker is less than the End Date
Current Interest Rate = Grab interest rate based on Date Marker
Current Interest Days = Calculate the minimum of 3 intervals
Days until the end of the period
Days until a new interest rate occurs
Days until a compounding event occurs
Simple Interest = Principle * Current Interest Rate * Current Interest Days
Compound Interest = Accrued Compounded Interest * Current Interest Rate * Current Interest Days
Accrued Simple Interest += Simple Interest 
Accrued Compounded Interest += Compound Interest
If Compounding event occurs, then
Accrued Compounded Interest += Accrued Simple Interest
Increment Date Marker by Current Interest Days

*/