package org.extras;
import java.text.*;
import java.util.*;


public class DateA {
	
	private Calendar aDate =  Calendar.getInstance();
	
	public DateA() {
		this.aDate =  Calendar.getInstance();
	}
	
	public Calendar getaDate() {
		return aDate;
	}

	public void setaDate(Calendar aDate) {
		this.aDate = aDate;
	}

	public DateA(String dateStr) {
		this.aDate =  Calendar.getInstance();
		//parse the string into MM DD YYYY
		int MM = 0;
		int DD = 0;
		int YYYY = 0;
		
		//test for - or / separators
		int dashLocation = dateStr.indexOf('-');
		int slashLocation = dateStr.indexOf('/');
		if (dashLocation < 0 && slashLocation < 0){
			//no dash and no slash = invalid date
			System.out.println("\nERROR : Invalid date format (MM-DD-YYYY  or MM/DD/YYYY). ");
		}else if (dashLocation>=0) {
			String[] splitArray = dateStr.split("-");
			MM = Integer.parseInt(splitArray[0]);
			DD = Integer.parseInt(splitArray[1]);
			YYYY = Integer.parseInt(splitArray[2]);
		}else { //(slashLocation>=0)
			String[] splitArray = dateStr.split("/");
			MM = Integer.parseInt(splitArray[0]);
			DD = Integer.parseInt(splitArray[1]);
			YYYY = Integer.parseInt(splitArray[2]);
		}
		
		this.aDate = DateA.getDate(MM, DD, YYYY);
		 
	}
		
	public void setTodaysDate() {		
		this.aDate =  Calendar.getInstance(); 
	}

	public static Calendar getTodaysDate() {		
		Calendar today = Calendar.getInstance();
		return today;
	}
	
	public void displayDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		System.out.println("Date : " + dateFormat.format(this.aDate.getTime()));		
	}
	
	
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		return  dateFormat.format(this.aDate.getTime());		
	}
	
	public static void displayDate(Calendar date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		System.out.println("Date : " + dateFormat.format(date.getTime()));
	}
	 
	public void displayFullDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		System.out.println("Date Time : " + dateFormat.format(this.aDate.getTime()));
	}
	
	public static void displayFullDate(Calendar date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		System.out.println("Date Time : " + dateFormat.format(date.getTime()));
	}
	
	
	public void setDate(int MM, int DD, int YYYY) {
		
		
		this.aDate.clear();
		//this.aDate.set(YYYY, MM, DD); 
		this.aDate.set(YYYY, MM-1, DD); 
		
//		date.set(Calendar.MONTH, MM);
//		date.set(Calendar.DAY_OF_YEAR, DD);
//		date.set(Calendar.YEAR, YYYY);
		
	}
	
	
	public static Calendar getDate(int MM, int DD, int YYYY) {
		
		Calendar date = Calendar.getInstance();
		date.clear();
		date.set(YYYY, MM-1, DD);
		return date;
		
//		date.set(Calendar.MONTH, MM);
//		date.set(Calendar.DAY_OF_YEAR, DD);
//		date.set(Calendar.YEAR, YYYY);
		
	}
	
	
	public static DateA getDateA(int MM, int DD, int YYYY) {
		
		DateA date = new DateA(); 
		date.aDate.set(YYYY, MM-1, DD);
		return date;
		
//		date.set(Calendar.MONTH, MM);
//		date.set(Calendar.DAY_OF_YEAR, DD);
//		date.set(Calendar.YEAR, YYYY);
		
	}
	public void setTruncateDate() {
		//date.clear();
		this.aDate.set(Calendar.HOUR_OF_DAY, 0);
		this.aDate.set(Calendar.MINUTE, 0);
		this.aDate.set(Calendar.SECOND, 0);			 
		this.aDate.set(Calendar.MILLISECOND,0); 
		//date.set(date.YEAR, date.MONTH, date.DAY_OF_MONTH, 0, 0, 0);
	}
	
	public static Calendar getTruncateDate(Calendar date) {
		//date.clear();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);			 
		date.set(Calendar.MILLISECOND,0);
		return date;
		//date.set(date.YEAR, date.MONTH, date.DAY_OF_MONTH, 0, 0, 0);
	}
	
	public void addDays(int x) {

		this.aDate.add(Calendar.DAY_OF_YEAR, x);  //add x to the field DAY 
//		System.out.print("\n");
//		System.out.print(this.toString());
//		System.out.print("\n");	
		//automatically handles year conversions etc
	}
	
	
	public static DateA returnAddedDays(DateA date, int x) {
		//date.clear();
		date.getaDate().add(Calendar.DAY_OF_YEAR, x);   //add x to the field DAY 
		return date;
		//date.set(date.YEAR, date.MONTH, date.DAY_OF_MONTH, 0, 0, 0);
	}
	
//	public static int daysBetweenDates(DateA a, DateA b) {
//		if(a.aDate.after(b.aDate)) {				
//			return a2bDays(b,a);
//		}else if(a.aDate.before(b.aDate)){
//			return a2bDays(a,b);
//		}else if(a.aDate.equals(b.aDate)){
//			return 0;
//		}else {
//			return -1;
//		} 
//		
//	}
	
	public static int daysBetweenDates(DateA a, DateA b) {
//		 System.out.println("daysBetweenDates: a = "+ a.toString() + "\n");
//		 System.out.println("daysBetweenDates: b = "+ b.toString() + "\n");
		if(a.aDate.after(b.aDate)) {				
			return a2bDays(b,a);
		}else if(a.aDate.before(b.aDate)){
			return a2bDays(a,b);
		}else if( a.equalTo(b)){
			return 0;
		}else {
			return -1;
		} 		
	}
	
	public static int a2bDays(DateA a, DateA b) {
		int days = 0;
		DateA z = new DateA(); 
		//System.out.println("z initial" + z.toString());
		z.setDate(a.getMM(), a.getDD(), a.getYYYY()  );
		//System.out.println("   a MM" + a.getMM() +   "   a DD"  + a.getDD()+ "   a YYYY" + a.getYYYY()  );
		//System.out.println("z set" + z.toString());
		
		while(!z.equalTo(b)){
			//z = addDays(z, 1);
			z.addDays(1);			
			days++;
			if(days%50 == 0) {
//				System.out.println("a : " + a.toString());
//				System.out.println("z : " + z.toString());
//				System.out.println("b :" + b.toString());
//				System.out.println("days" + days);
				
			}
			if (days>10000000) {
				break;
			}
		}
		return days;
	}
	

	public boolean greaterThan( DateA b) {
		boolean g = false;
		if (this.aDate.after(b.aDate)) {
			g = true;
			//System.out.println(this.toString() + "greater than - true" +  b.toString());
		}else {
			g = false;
			//System.out.println(this.toString() + "greater than - false" +  b.toString()); 
		}		 
		return g;
	}
	
	public boolean lessThan( DateA b) {
		boolean g = false;
		if (this.aDate.before(b.aDate)) {
			g = true;
			//System.out.println(this.toString() + "less than - true" +  b.toString());
		}else {
			g = false;
			//System.out.println(this.toString() + "less than - false" +  b.toString());	
		}		 
		
		return g;
	}
	
	
	public boolean equalTo( DateA b) {
		boolean g = false;	 
		
		if (this.getMM() == b.getMM() && this.getDD() == b.getDD() && this.getYYYY() == b.getYYYY()) {
			g = true;
			//System.out.println(this.toString() + "equal - " + b.toString());
		}else {
			g = false;
			//System.out.println(this.toString() + "not equal - " + b.toString()); 
		}		 
		return g;
	}
	
	public int getYYYY() {
	    int year = this.aDate.get(Calendar.YEAR);
	    return year;
	}
	
	public int getMM() {
	    int month = this.aDate.get(Calendar.MONTH)+1;
	    return month;
	}
	
	public int getDD() {
	    int day = this.aDate.get(Calendar.DAY_OF_MONTH);
	    return day;
	}
    
	
}//end class

