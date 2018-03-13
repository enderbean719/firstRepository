package org.extras;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date; 
import org.extras.*;

public class InterestRateTable {
	DateA[] startDates;
	double[] rates;
	
	
	public DateA[] getStartDates() {
		return startDates;
	}

	public void setStartDates(DateA[] startDates) {
		this.startDates = startDates;
	}

	public double[] getRates() {
		return rates;
	}

	public void setRates(double[] rates) {
		this.rates = rates;
	}

	public InterestRateTable() {
		
	}
	
	public InterestRateTable(DateA[] d, double[] r) {
		//https://stackoverflow.com/questions/28232866/read-json-array-into-java-array-lists-map
		startDates = d;
		rates = r;
		
		
		//Category[] categories = {new Category(max), new Category(max), new Category(max), new Category(max)};
		//InterestRateTable IRStbl = new InterestRateTable({new DateA(), new DateA()}, {0.01,0.02} ) ;
		//InterestRateTable IRStbl = new InterestRateTable( new DateA[]{new DateA(), new DateA(), new DateA()}, new int[]{0.01,0.02,0.03} ) ;
		//data = new int[]{0, 0, 0};
		
	}
	
	
	public InterestRateTable(int option) {
		if (option == 1){
			//hardcode dates for IRS
			
		}else if (option == 2) {
			//hardcode dates for ...
		}else {
			//default
			//InterestRateTable();
			
		}
		
	}
	
	public double getRate(DateA rateDate) {
		double rate = 0.0; 
		int l =  this.length(); 
		l = l - 1;  //last index
		for(int i = 0; i<l; i++) {
			if ((this.startDates[i].lessThan(rateDate)||this.startDates[i].equalTo(rateDate)) && this.startDates[i+1].greaterThan(rateDate)) {
				rate = this.rates[i];
			}
		}
		if(rateDate.greaterThan(this.startDates[l])  ||  rateDate.equalTo(this.startDates[l])   ) {
			rate = this.rates[l];
		}		
		return rate;
	}
	
	public DateA getNextRateDate(DateA placemarker) {
		DateA nextRateDate = new DateA();	
		boolean set = false;
		int l =  this.length(); 
		l = l - 1;  //last index
		for(int i = 0; i<l; i++) {
			if ((this.startDates[i].lessThan(placemarker) || this.startDates[i].equalTo(placemarker) ) && this.startDates[i+1].greaterThan(placemarker)) {
				nextRateDate = this.startDates[i+1];
				set = true;
			}
		}
		if(placemarker.greaterThan(this.startDates[l])) {
			//nextRateDate = DateA.getDateA(12, 31, 2199);
			nextRateDate = new DateA("12-31-2199");
			set = true;
		}
		if (set == false) {
			nextRateDate = new DateA("12-31-2200");
		}
		return nextRateDate;
	}
	
	
	public String toString() {
		String strTbl = ""; 
		int l =  this.length(); 
		l = l - 1;  //l = last index
		for(int i = 0; i<l; i++) {
			strTbl = strTbl + "\n\tStartDate :\t" + this.startDates[i] + "\tRate :\t" +this.rates[i];
		}						
		return strTbl;		
	}
	
	
	public DateA firstDate() {
		return this.startDates[0];
	}
	
	public DateA lastDate() {
		return this.startDates[this.length()-1];
	}
	

	
	public int length() {
		int l = 0;
		l = this.startDates.length;
		if (this.rates.length !=  this.startDates.length) {
			
			System.out.print("InterestRateTable data missing");	
			if(this.rates.length <  this.startDates.length) {
					l = this.rates.length;
			}else {
					l = this.startDates.length;
			}
		}
		return l;
	}
	
}//end class
