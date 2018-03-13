package org.anvard.webmvc.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import org.eclipse.jetty.util.log.Log;
import org.apache.commons.io.FileUtils;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import org.anvard.webmvc.api.Calculation;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.extras.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


@Controller
public class Calculator {

		//GET
    @RequestMapping(value = "/calc/{op}/{left}/{right}", method = RequestMethod.GET)
    @ResponseBody
    public Calculation calculate(@PathVariable("op") String op, @PathVariable("left") Integer left,
            @PathVariable("right") Integer right ) {    	
    	//System.out.println("date" + (date));   
        Assert.notNull(op);
        Assert.notNull(left);
        Assert.notNull(right);
        Calculation result = new Calculation();
        result.setOperation(op);
        result.setLeft(left);
        result.setRight(right);
        return doCalc(result);        
    }
    
    
//    GET + DATE
//    public Calculation calculate(@PathVariable("op") String op, @PathVariable("left") Integer left,
//            @PathVariable("right") Integer right, @RequestParam("date") String date) {
//    	    System.out.println("date" + (date));
    

    //POST
    @RequestMapping(value = "/calc2", method = RequestMethod.POST)
    @ResponseBody
    public Calculation calculate(@RequestBody Calculation calc) {    	
        Assert.notNull(calc);
        Assert.notNull(calc.getOperation());
        Assert.notNull(calc.getLeft());
        Assert.notNull(calc.getRight());
        return doCalc(calc);        
    }
    
    //POST
//    @RequestMapping(value = "/interest", method = RequestMethod.POST)
//    @ResponseBody
//    public Interest calcInterest(@RequestBody Interest calc) {
//        Assert.notNull(calc);
//        Assert.notNull(calc.getOperation());
//        Assert.notNull(calc.getAmount());
//        Assert.notNull(calc.getStartDate());
//        Assert.notNull(calc.getEndDate());
//        Assert.notNull(calc.getCompoundDays());
//        Assert.notNull(calc.getTbl());
//        return doInterest(calc);
//    }

    //POST TESTING
    @RequestMapping(value = "/interest", method = RequestMethod.POST)
    @ResponseBody
    
    public String calcInterest(@RequestBody String body) {
    	
    //PRINT input to server
    	System.out.print("\nBody = " + body);
    	
    //CONVERT body to object    	
    	//object shell for json object  & test object
    	Interest interestProblem = new Interest();   	
    	
    	//json object mapper
    	ObjectMapper mapper = new ObjectMapper();    	
    	
    	//json string --> mapper --> object shell
    	try {    		
    		interestProblem = mapper.readValue(body, Interest.class); 
    	}catch(Exception e){ 
    		e.printStackTrace();
    		System.out.print(e.toString());
    		System.out.print("\nFAILED TO MAP OBJECT");
    	}
    	
    	
    	
    	
    	//prove you captured the object
    	String strIP = interestProblem.toString();
    	System.out.print(strIP);
    	
    	//System.out.print(interestProblem.getOperation());
    	
    	//if 3rd party
    	if(interestProblem.getOperation().equals("TotalInterestForIRS")) {
    		System.out.println("\n\nTotalInterestForIRS started");
    		
    		
    		int intOfEle = 0;   //container for int val   // years
    		double doubleOfEle = 0.0; //container for double val  // rates
    		String strOfEle = "";
    		 
    		String YYYY = "";
    		String firstQ = "1-1-";
    		String secondQ = "4-1-";
    		String thirdQ = "7-1-";
    		String fourthQ = "10-1-";
    		int dataCount = 0;
    		int arrayIndex = 0;
    		InterestRateTable tblIRS = new InterestRateTable();
    		
		    		Document doc;
					try {
						//grab irs data
						doc = Jsoup.connect("https://proconnect.intuit.com/proseries/articles/federal-irs-underpayment-interest-rates/").get();
									//System.out.println(doc.title());
						Element table = doc.select("table").get(1).select("tbody").get(0); //select the first or second table.
						Elements rows = table.select("tr");
						
									//System.out.println("row size (data only 35)" + rows.size());
						
						//build IRS objects
						int sizeA = (rows.size()-2)*4;
			    		DateA[] irsDates = new DateA[sizeA]; //add num rows*col
			    		double[] irsRates = new double[sizeA]; //add num rows*col
			    		arrayIndex = 0;  // first spot
					    		//System.out.println("size * 4 = " + rows.size()*4);
					    		//InterestRateTable tblIRS = new InterestRateTable( new DateA[]{new DateA(), new DateA(), new DateA()}, new int[]{0.01,0.02,0.03} ) ;
					    		//InterestRateTable tblIRS = new InterestRateTable( irsDates, irsRates) ;
			    		
			    		System.out.println("new table");
			    		
			    		//search data and populate objects
						for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
						    Element row = rows.get(i);
						    Elements cols = row.select("td");
			
						    for (Element col : cols) {
						    	
						    	strOfEle = col.text().toString()  ;
						    			
									    	//intOfEle = Integer.valueOf(  col.text().toString() );		
									    	//System.out.println(col.text());									    	
									    		//System.out.println("mod5 = " + (dataCount%5));
						    		if(dataCount%5 == 1) {
						    			irsDates[arrayIndex] = new DateA(firstQ+YYYY);	
						    			try {
							    			irsRates[arrayIndex] = tblValToDouble(strOfEle);
						    			}catch(Exception e) {
						    				irsRates[arrayIndex] = 0.0;
						    			}
						    			arrayIndex++;
						    		}else if(dataCount%5 == 2) {
						    			irsDates[arrayIndex] = new DateA(secondQ+YYYY);
						    			try {
						    				irsRates[arrayIndex] = tblValToDouble(strOfEle);
							    		}catch(Exception e) {
						    				irsRates[arrayIndex] = 0.0;
						    			}
						    			arrayIndex++;
						    		}else if(dataCount%5 == 3) {
						    			irsDates[arrayIndex] = new DateA(thirdQ+YYYY);
						    			try {
						    				irsRates[arrayIndex] = tblValToDouble(strOfEle);
							    		}catch(Exception e) {
						    				irsRates[arrayIndex] = 0.0;
						    			}
						    			arrayIndex++;
						    		}else if(dataCount%5 == 4) {
						    			irsDates[arrayIndex] = new DateA(fourthQ+YYYY);
						    			try {
						    				irsRates[arrayIndex] = tblValToDouble(strOfEle);
							    		}catch(Exception e) {
						    				irsRates[arrayIndex] = 0.0;
						    			}
						    			arrayIndex++;
						    		}else {
						    			YYYY = strOfEle;	
						    			
						    		}		
						    		
						    		
//						    		try {
//						    			System.out.println("DC " + dataCount + " AI "+ arrayIndex + " YYYY = " + YYYY + " SD = " + irsDates[dataCount].toString() + " Rate = " + irsRates[dataCount] );
//						    		}catch(Exception e) {
//						    			System.out.println("failed to print stats");
//						    		}
						    		
						    		dataCount++;
						    		
						    }//end for col
						    
						    
						}//end for row
						
					DateA[] irsDates2 = new DateA[sizeA]; //add num rows*col
			    	double[] irsRates2 = new double[sizeA]; //add num rows*col	
					for(int ii = 0; ii<sizeA; ii++) {
						irsDates2[ii] = irsDates[sizeA - 1 - ii];
						irsRates2[ii] = irsRates[sizeA - 1 - ii];
					}//fix order
					
					DateA tempDateA = new DateA();
					double tempDouble = 0.0;
					for(int ii = 0; ii<sizeA; ii++) {
						if(ii%4==0) {
							tempDateA = irsDates2[ii] ;
							tempDouble = irsRates2[ii];
							irsDates2[ii] = irsDates2[ii+3];
							irsRates2[ii] = irsRates2[ii+3];
							irsDates2[ii+3] = tempDateA;
							irsRates2[ii+3] = tempDouble;
						}else if(ii%4==1) {
							tempDateA = irsDates2[ii] ;
							tempDouble = irsRates2[ii];
							irsDates2[ii] = irsDates2[ii+1];
							irsRates2[ii] = irsRates2[ii+1];
							irsDates2[ii+1] = tempDateA;
							irsRates2[ii+1] = tempDouble;	
						}else {
							//mod 2 and mod 3 already handled
						}
						
					}//fix quarters
			    	
			    	
					tblIRS = new InterestRateTable( irsDates2, irsRates2) ;
				    System.out.println("Printing IRS table");
				    System.out.println(tblIRS.toString());
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}   //end of try catch
					
		interestProblem.setTbl(tblIRS);			
				    
    	}    	//end 3rd party
    	
    	
    	
    	//PROCESS object
    	
    	interestProblem.calcTotalInterest();
    	strIP = interestProblem.toString();
    	
    //RETURN RESULT Total interest message    	
        
        return "SUCCESS.\n\n" + strIP;  //return as response to postman
        
    }
    
    private void SendGetRequestByUrl(String url) throws IOException {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
    	
    }
    

    private double tblValToDouble(String input) {
	    String subS = "0";
	    double out = 0.0;
	    try {
	    	int iend = input.indexOf("%");
	    	if (iend != -1) {
	        	subS= input.substring(0 , iend);
	    	}
	    	int in = Integer.valueOf(subS);
	    	out = in / 100.0;
	    }catch(Exception e) {
	    	out = -1.0;
	    }
    	return out;
    	
//    	String filename = "abc.def.ghi";     // full file name
//    	int iend = filename.indexOf("."); //this finds the first occurrence of "." 
//    	//in string thus giving you the index of where it is in the string
//
//    	// Now iend can be -1, if lets say the string had no "." at all in it i.e. no "." is not found. 
//    	//So check and account for it.
//
//    	if (iend != -1) 
//    	String subString= filename.substring(0 , iend)
    	
    }
    
    private Calculation doCalc(Calculation c) {
        String op = c.getOperation();
        int left = c.getLeft();
        int right = c.getRight();
        if (op.equalsIgnoreCase("subtract")) {
            c.setResult(left - right);
        } else if (op.equalsIgnoreCase("multiply")) {
            c.setResult(left * right);
        } else if (op.equalsIgnoreCase("divide")) {
            c.setResult(left / right);
        } else {
            c.setResult(left + right);
        }
        return c;
    }
    
    
    //ALGORITHM
    /*
    private Interest doInterest(Interest c) {
        String op = c.getOperation();
        BigDecimal amount = c.getAmount();
        DateA startDate = c.getStartDate();
        DateA endDate = c.getEndDate();
        Integer compoundDays = c.getCompoundDays();
        InterestRateTable tbl = c.getTbl();
        
        		
        
        if (op.equalsIgnoreCase("totalInterest")) {
            c.setOutput(amount.add(new BigDecimal(1)   )   );
        } 
//        else if (op.equalsIgnoreCase("multiply")) {
//            c.setResult(left * right);
//        } 
//        else if (op.equalsIgnoreCase("divide")) {
//            c.setResult(left / right);
//        } 
        else {
            c.setOutput(new BigDecimal(0));
        }
        return c;
    }*/
    
    
}//end class
