package com.etrm.fms.util;

import java.util.prefs.Preferences;

import com.etrm.fms.mail.MailDelivery;

public class Otp_Handler
{
	String db_src_file_name="Otp_Handler.java";
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	MailDelivery mailDelv = new MailDelivery();
	
	private Preferences preferences =  Preferences.userRoot().node("/otpVerif");
	
	//public void manageOTP(String compCd, String empCd, String email, int start, int end)
	public boolean manageOTP(String empCd, String email, int start, int end)
	{
		// String key= compCd+"-"+empCd;
		String key= empCd;
		
		otp = (new EncryptTest()).generateOTP(start,end);	//Code for generating otp
		otp="1234";
		preferences.put(key, otp);	//Storing the otp
		emp_cd=empCd;
		
		System.out.println("empCd: "+empCd+"\notp:"+otp+"\nemail id:"+email);
		
		//mailOTP(compCd,email,otp);
		boolean gen_otp = mailOTP(email,otp);
		
		return gen_otp;
	}
	
	//public void mailOTP(String comp_cd,String mail_recipent, String otp)
	public boolean mailOTP(String mail_recipent, String otp)
	{
		String function_nm="mailOTP";
		boolean gen_otp = false;
		
		try 
		{
			String env_context = utilBean.getAutomationKeyDetail("ENV_CONTEXT");
			String emp_nm = utilBean.getEmpName(emp_cd);
			System.out.println(env_context+"------------"+emp_nm);
			String subject=CommonVariable.app_name_sub+" "+env_context+": OTP ";
			String mailBody="<html>"
				+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"
				+ "The OTP for your "+CommonVariable.app_name+" account is : <font style='background:#00cc00' color='white'>"+otp+"</font>"
				+ "</span><br>";			
			mailBody+=CommonVariable.mail_signature;
			mailBody+=CommonVariable.mail_disclaimer;
			mailBody+= "</html>";
			
			gen_otp = mailDelv.sendMail("",mail_recipent, subject, mailBody, "", "", "");
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return gen_otp;
	}
	
	public void verifyOTP()
	{
//		String key = comp_cd+"-"+emp_cd;
		String key = emp_cd;
		String s =  preferences.get(key, "NO OTP FOUND");
		System.out.println("The otp is :"+s);
		if(s.equals(otp_val))
		{
			preferences.remove(key);	//for removing the data from preferences 
			result = true;
		}
		
	}
	
	//for removing the data after specific amount of time
	//public void removeData(String comp_cd, String emp_cd)
	public void removeData(String emp_cd)
	{
		String key = emp_cd;
		//String key = comp_cd+"-"+emp_cd;
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
