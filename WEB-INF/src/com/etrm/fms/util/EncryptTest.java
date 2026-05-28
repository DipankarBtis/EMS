package com.etrm.fms.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptTest
{
    public StringBuffer encrypt(String s)
    {
        String s1 = s;
        StringBuffer stringbuffer = new StringBuffer();

        try   
    	{	
        	stringbuffer.append(generatePassword("10"));
        	
    		MessageDigest md = MessageDigest.getInstance("SHA-256");
    		byte[] messageDigest = md.digest(s.getBytes());  
    		BigInteger no = new BigInteger(1, messageDigest);  
    		String hashtext = no.toString(16);  
    		while (hashtext.length() < 32)   
    		{  
    			hashtext = "0" + hashtext;  
    		}  
    		
    		stringbuffer.append(hashtext);
    	}  
    	catch (NoSuchAlgorithmException e)   
    	{  
    		throw new RuntimeException(e);  
    	}

        return stringbuffer;
    }
    
    public StringBuffer decrypt(String s)
    {
        String s1 = s;
        s1 = s1.substring(10);
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(s1);
        return stringbuffer;
    }
    
    public String generatePassword(String pwd_lengh) 
    {
    	String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    String LOWER = "abcdefghijklmnopqrstuvwxyz";
	    String DIGITS = "0123456789";
	    
    	int pwd_lengh_val =Integer.parseInt(pwd_lengh);
    	
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(pwd_lengh_val);

        String allCharacters = UPPER + DIGITS + LOWER;

        // Make sure at least one character is chosen from each character set
        password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        
        
        // Fill the remaining characters randomly
        for (int i = 3; i < pwd_lengh_val; i++) 
        {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }
       
        return password.toString();
    }
    
    //for OTP 
    public String generateOTP(int start,int end)
    {
    	String DIGITS ="0123456789";
    	SecureRandom random = new SecureRandom();
    	int len = random.nextInt((end-start)+1)+start;
    	StringBuilder otp = new StringBuilder(len);
    	
    	for (int i = 0; i < len; i++) 
        {
            otp.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
    	return otp.toString();
    }
    
    //URL Param Encrypt
    public StringBuffer URLencrypt(String s)
    {
        String s1 = s;
        char c = '0';
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(generatePassword("5"));
       
        for(int i = 0; i < s1.length(); i++)
        {
        	char ch = s1.charAt(i);
        	switch (ch)  
            {  
                case '0':
                	c='R';
                    break;  
                case '1': 
                	c='s';
                    break;  
                case '2':
                	c='K';
                    break;  
                case '3':
                	c='h';
                    break;  
                case '4':
                	c='J';
                    break;  
                case '5':
                	c='a';
                    break;  
                case '6':
                	c='M';
                    break;  
                case '7':
                	c='p';
                    break;  
                case '8':
                	c='W';
                    break;  
                case '9':
                	c='z';
                    break;  
                case '-':
                	c='B';
                    break;  
                default:  
                    break;  
            }
            stringbuffer.append(c);
            stringbuffer.append(generatePassword("2"));
        }
        stringbuffer.append(generatePassword("5"));
        return stringbuffer;
    }
    
    //URL Param Decrypt
    public StringBuffer URLdecrypt(String s)
    {
        String s1 = s;
        s1 = s1.substring(5);
        char c = '0';
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < s1.length()-7; i++)
        {
        	char ch = s1.charAt(i);
        	switch (ch)  
            {  
                case 'R':
                	c='0';
                    break;  
                case 's': 
                	c='1';
                    break;  
                case 'K':
                	c='2';
                    break;  
                case 'h':
                	c='3';
                    break;  
                case 'J':
                	c='4';
                    break;  
                case 'a':
                	c='5';
                    break;  
                case 'M':
                	c='6';
                    break;  
                case 'p':
                	c='7';
                    break;  
                case 'W':
                	c='8';
                    break;  
                case 'z':
                	c='9';
                    break;  
                case 'B':
                	c='-';
                    break;  
                default:  
                    break;  
            }
            stringbuffer.append(c);
            ++i;
            ++i;
            ++i;
        }
        return stringbuffer;
    }
}