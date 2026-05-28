package com.etrm.fms.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CurrencyUtil
{

	private static final String[] tensNames = 
	{
		"",
	    " Ten",
	    " Twenty",
	    " Thirty",
	    " Forty",
	    " Fifty",
	    " Sixty",
	    " Seventy",
	    " Eighty",
	    " Ninety"
	};

	private static final String[] numNames = 
	{
	    "",
	    " One",
	    " Two",
	    " Three",
	    " Four",
	    " Five",
	    " Six",
	    " Seven",
	    " Eight",
	    " Nine",
	    " Ten",
	    " Eleven",
	    " Twelve",
	    " Thirteen",
	    " Fourteen",
	    " Fifteen",
	    " Sixteen",
	    " Seventeen",
	    " Eighteen",
	    " Nineteen"
	};

	private static String convertLessThanOneThousand(int number) 
	{
	    String soFar;
	
	    if (number % 100 < 20)
	    {
	      soFar = numNames[number % 100];
	      number /= 100;
	    }
	    else 
	    {
	      soFar = numNames[number % 10];
	      number /= 10;
	
	      soFar = tensNames[number % 10] + soFar;
	      number /= 10;
	    }
	    if (number == 0) return soFar;
	    return numNames[number] + " Hundred" + soFar;
	}
	
	 static String result = "";
	 public static String convert(double number) 
	 {
		 if (Double.doubleToRawLongBits(number)==Double.doubleToRawLongBits(0)) { return "zero"; }
	
		 String snumber = Double.toString(number);
	
		// pad with "0"
		String mask = "000000000000.00";
		DecimalFormat df = new DecimalFormat(mask);
		snumber = df.format(number);
		
		// XXXnnnnnnnnn 
		int billions = Integer.parseInt(snumber.substring(0,3));
		// nnnXXXnnnnnn
		int millions  = Integer.parseInt(snumber.substring(3,6)); 
		// nnnnnnXXXnnn
		int hundredThousands = Integer.parseInt(snumber.substring(6,9)); 
		// nnnnnnnnnXXX
		int thousands = Integer.parseInt(snumber.substring(9,12));
		    
	
	    String tradBillions;
	    switch (billions) 
	    {
		    case 0:
		    	tradBillions = "";
		    break;
		    case 1 :
		    	tradBillions = convertLessThanOneThousand(billions) 
		    	+ " Billion ";
		    break;
		    default :
			  tradBillions = convertLessThanOneThousand(billions) 
			  + " Billion ";
	    }
	    result =  tradBillions;
	
	    String tradMillions;
	    switch (millions) 
	    {
	    	case 0:
	    		tradMillions = "";
	    	break;
	    	case 1 :
	    		tradMillions = convertLessThanOneThousand(millions) 
	    		+ " Million ";
	    	break;
	    	default :
	    		tradMillions = convertLessThanOneThousand(millions) 
	    		+ " Million ";
    	}
    	result =  result + tradMillions;
	
	String tradHundredThousands;
	switch (hundredThousands) 
	{
		case 0:
			tradHundredThousands = "";
		break;
		case 1 :
			tradHundredThousands = "One Thousand ";
		break;
		default :
			tradHundredThousands = convertLessThanOneThousand(hundredThousands) 
			+ " Thousand ";
	}
	result =  result + tradHundredThousands;
	
	String tradThousand;
	tradThousand = convertLessThanOneThousand(thousands);
	result =  result + tradThousand;
	//		    System.out.println("---ehdf--"+result);
	// remove extra spaces!
	return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
	}

	 
	 public static String formatAmount(String amt) 
	 {
        try 
        {
            double value = Double.parseDouble(amt);

            // US-style grouping (150,000.00)
            NumberFormat nf = new DecimalFormat("###,###,###,##0.00");

            return nf.format(value);
        } 
        catch (NumberFormatException e) 
        {
            // Handle invalid input gracefully
            return "0.00";
        }
    }
	 
	 public static String formatWithCommaNoDecimal(String amt) 
	 {
        try 
        {
            double value = Double.parseDouble(amt);

            // US-style grouping (150,000.00)
            NumberFormat nf = new DecimalFormat("###,###,###,##0");

            return nf.format(value);
        } 
        catch (NumberFormatException e) 
        {
            // Handle invalid input gracefully
            return "0.00";
        }
    }
}
