package com.etrm.fms.util;
public class escapeSingleQuotes
{
	 public String replaceSingleQuotes(String sInput)
	 {
	       StringBuffer sbOutput = new StringBuffer();
	       for(int i = 0; sInput!= null && i < sInput.length(); i++)
	       {
	           sbOutput.append(sInput.charAt(i));
	           if(sInput.charAt(i) == '\''){
	               sbOutput.append('\'');
	           }
	           
	       }
	       return sbOutput.toString();
	}
}
