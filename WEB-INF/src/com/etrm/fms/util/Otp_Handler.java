package com.etrm.fms.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.mail.MailDelivery;

public class Otp_Handler
{
	public static Connection dbcon;
	String db_src_file_name="Otp_Handler.java";
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	MailDelivery mailDelv = new MailDelivery();
	
	private Preferences preferences =  Preferences.userRoot().node("/otpVerif");
	
	public boolean manageOTP(String empCd, String email, int start, int end)
	{
		String key= empCd;
		
		//otp = (new EncryptTest()).generateOTP(start,end);	//Code for generating otp
		otp="1234";
		preferences.put(key, otp);	//Storing the otp
		emp_cd=empCd;
		
		boolean gen_otp = mailOTP(email,otp);
		
		return gen_otp;
	}
	
	public boolean mailOTP(String mail_recipent, String otp)
	{
		String function_nm="mailOTP";
		boolean gen_otp = false;
		
		try 
		{
			Context Context = new InitialContext();
			
			Context envContext  = (Context)Context.lookup("java:/comp/env");
			DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
			if(ds != null)
			{
				dbcon = ds.getConnection();				
			}
			else
			{
				System.out.println("Data Source Not Found");
			}
			if(dbcon != null)
			{
				String env_context = utilBean.getAutomationKeyDetail(dbcon,"ENV_CONTEXT");
				String emp_nm = utilBean.getEmpName(dbcon,emp_cd);
				String subject=CommonVariable.app_name_sub+" "+env_context+": OTP ";
				String mailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"
						+ "The OTP for your "+CommonVariable.app_name+" account is : <font color='black'><b>"+otp+"</b></font>"
						+ "</span><br>";			
				mailBody+=CommonVariable.mail_signature;
				mailBody+=CommonVariable.mail_disclaimer;
				mailBody+= "</html>";
				
				//gen_otp = mailDelv.sendMail("",mail_recipent, subject, mailBody, "", "", "");
				dbcon.close();
			}
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally
		{
			if(dbcon != null){try {dbcon.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		}
		return gen_otp;
	}
	
	public void verifyOTP()
	{
		String key = emp_cd;
		String s =  preferences.get(key, "NO OTP FOUND");
		if(s.equals(otp_val))
		{
			preferences.remove(key);	//for removing the data from preferences 
			result = true;
		}
		
	}
	
	public void removeData(String emp_cd)
	{
		String key = emp_cd;
		preferences.remove(key);
	}
	
	String comp_cd = "";
	String otp_val = "";
	private String otp = "";
	String emp_cd = "";
	boolean result = false;
	
	public void setOtp_val(String otp_val) {this.otp_val = otp_val; }
	public void setEmp_Cd(String empCd) {this.emp_cd = empCd; }
	public void setComp_Cd(String compCd) {this.comp_cd = compCd;} 
	public boolean getResult() {return result;}
}
