<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Vector"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=CommonVariable.app_name%></title>
</head>
<jsp:useBean class="com.etrm.fms.admin.DataBean_Admin" id="dbadmin" scope="request"></jsp:useBean> 
<jsp:useBean class="com.etrm.fms.util.Otp_Handler" id="otpAuth" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateutil" scope="page"></jsp:useBean>
<%
String ip=request.getRemoteAddr();
String url= request.getParameter("url")==null?"":request.getParameter("url");
String otpval= request.getParameter("otpval")==null?"":request.getParameter("otpval");
String emp_cd= request.getParameter("emp_cd")==null?"":request.getParameter("emp_cd");
//String comp_cd= request.getParameter("comp_cd")==null?"":request.getParameter("comp_cd");
String otp_flag = request.getParameter("otp_flag")==null?"":request.getParameter("otp_flag");
String email = request.getParameter("email")==null?"":request.getParameter("email");
String resend_flag = request.getParameter("resend_flag")==null?"":request.getParameter("resend_flag");
String count_trials = request.getParameter("count_trials")==null?"0":request.getParameter("count_trials");
String emp_uid = request.getParameter("emp_uid")==null?"":request.getParameter("emp_uid");
String emp_nm = request.getParameter("emp_nm")==null?"":request.getParameter("emp_nm");

String sysdate = ""+dateutil.getSysdate();
String eff_dt=request.getParameter("eff_dt")==null?sysdate:request.getParameter("eff_dt");

dbadmin.setCallFlag("PASSWORD_POLICY");
dbadmin.setEff_dt(eff_dt);
//dbadmin.setComp_cd(comp_cd);
dbadmin.init();
String max_otp = dbadmin.getMax_otp();
String otp_max_length = dbadmin.getOtp_max_length();
String otp_min_length = dbadmin.getOtp_min_length();

int count = Integer.parseInt(count_trials);

boolean resend_status = Boolean.parseBoolean(resend_flag);
boolean otp_status=Boolean.parseBoolean(otp_flag);  
otpAuth.setOtp_val(otpval);
otpAuth.setEmp_Cd(emp_cd);
//otpAuth.setComp_Cd(comp_cd);
otpAuth.verifyOTP();

boolean result=otpAuth.getResult();
if(!resend_status)
{
	if(otp_status)
	{
		if(result == true)
		{
			session.setAttribute("emp_uid", emp_uid);
			//session.setAttribute("comp_cd", comp_cd);
			//session.setAttribute("comp_abbr", comp_abbr);
			//session.setAttribute("comp_nm", comp_nm);
			//session.setAttribute("comp_logo", comp_logo);
			session.setAttribute("emp_cd", emp_cd);
			session.setAttribute("emp_nm", emp_nm);
			session.setAttribute("email", email);
			session.setAttribute("ip",ip);
			//session.setMaxInactiveInterval(1800); //handled in common_js.jsp file.
			/*try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, "",emp_nm, ip, "0", "Login Page","0","Login Page", "", "", "Login");  	
			}
			catch(Exception infoLogger)
			{
				infoLogger.printStackTrace();
			} */
			response.sendRedirect(url);
		}
		
		else
		{
			String msg="Incorrect OTP!!";
			response.sendRedirect("otpauth.jsp?msg="+msg+"&url="+url+"&count_trials="+count+"&emp_uid="+emp_uid+"&emp_cd="+emp_cd+"&emp_nm="+emp_nm+"&email="+email);
		}
	}
	else
	{
		//otpAuth.removeData(comp_cd, emp_cd);
		otpAuth.removeData(emp_cd);
		String msg = "Your Otp has expired, <br>Please click Resend OTP!";
		response.sendRedirect("otpauth.jsp?msg="+msg+"&url="+url+"&count_trials="+count+"&emp_uid="+emp_uid+"&emp_cd="+emp_cd+"&emp_nm="+emp_nm+"&email="+email);
	}
}
else
{
	count++;
	otpAuth.removeData(emp_cd);
	//otpAuth.removeData(comp_cd, emp_cd);
	if(count<Integer.parseInt(max_otp))
	{
		otpAuth.manageOTP(emp_cd, email,Integer.parseInt(otp_min_length),Integer.parseInt(otp_max_length));
		
		//otpAuth.manageOTP(comp_cd, emp_cd, email,Integer.parseInt(otp_min_length),Integer.parseInt(otp_max_length));
		String msg = "OTP is resend to your Registered Email address";
		String msg_type="S";
		response.sendRedirect("otpauth.jsp?msg="+msg+"&msg_type="+msg_type+"&url="+url+"&count_trials="+count+"&emp_uid="+emp_uid+"&emp_cd="+emp_cd+"&emp_nm="+emp_nm+"&email="+email);
	}
	else
	{
		String msg = "You have exceed the maximum number of OTP trials!";
		response.sendRedirect("login.jsp?msg="+msg);
	}
}
%>
 