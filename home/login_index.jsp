<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Vector"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><%=CommonVariable.app_name%></title>

</head>
<jsp:useBean class="com.etrm.fms.util.DataBean_LoginAlter" id="LoginAlter" scope="page"></jsp:useBean>
<%
String username= request.getParameter("username")==null?"":request.getParameter("username");
String password= request.getParameter("password")==null?"":request.getParameter("password");
String inp_user_nm = request.getParameter("inputUserID_nm")==null?"":request.getParameter("inputUserID_nm");
String inp_user_email = request.getParameter("inputEmail_Id")==null?"":request.getParameter("inputEmail_Id");
boolean rel_lock_flag = Boolean.parseBoolean(request.getParameter("rel_lock_flag")==null?"false":request.getParameter("rel_lock_flag"));
boolean reset_pswd_flag = Boolean.parseBoolean(request.getParameter("reset_pswd_flag")==null?"false":request.getParameter("reset_pswd_flag"));
//System.out.println(username+"----"+password);
if(rel_lock_flag)
{
	LoginAlter.setUsername(inp_user_nm);
	LoginAlter.setInp_email(inp_user_email);
	LoginAlter.setCallFlag("RELEASE_LOCK");
	LoginAlter.init();
}
else if(reset_pswd_flag)
{
	LoginAlter.setUsername(inp_user_nm);
	LoginAlter.setInp_email(inp_user_email);
	LoginAlter.setCallFlag("FORGET_PASSWORD");
	LoginAlter.init();
	
}
else
{
	LoginAlter.setUsername(username);
	LoginAlter.setPassword(password);
	LoginAlter.setCallFlag("LOGIN_CHECK");
	LoginAlter.init();
}

boolean login = LoginAlter.getLogin();
boolean locked = LoginAlter.getLocked();
boolean disabled = LoginAlter.getDisabled();
boolean dormant = LoginAlter.getDormant();
boolean removed = LoginAlter.getRemoved();
boolean passChangeReq = LoginAlter.getPassChangeReq();
boolean incorrUserNm = LoginAlter.getIncorrUserNm();
boolean genOtp = LoginAlter.getGenOtp();
genOtp = true;
boolean release_lock = LoginAlter.getRelease_Lock();
boolean reset_pswd = LoginAlter.getReset_Pswd();

Integer passChangeDays = LoginAlter.getPassChangeDays();

String emp_cd = LoginAlter.getEmp_cd();
String emp_nm = LoginAlter.getEmp_nm();
//String comp_cd = LoginAlter.getComp_cd();
//String comp_abbr = LoginAlter.getComp_abbr();
//String comp_nm = LoginAlter.getComp_nm();
String email = LoginAlter.getEmail();
//String comp_logo = LoginAlter.getComp_logo();
String default_profile = LoginAlter.getDefault_profile();

String user_id=LoginAlter.getUserId();
//username = username.equals("")?user_id:username;
if(reset_pswd_flag || rel_lock_flag)
{
	username = user_id;
}

String exp_days = LoginAlter.getExp_days();
String rem_days = LoginAlter.getRem_days();
String out_msg = LoginAlter.getMsg();
boolean lock_flag=false;

boolean temp=false;
%>

<body>
<form method="post">
				<input class="input100" type="hidden" name="url" value=''>
               	<input class="input100" type="hidden" name="emp_cd" value='<%=emp_cd%>'>
               	<input class="input100" type="hidden" name="emp_uid" value='<%=username%>'>
               	<input class="input100" type="hidden" name="emp_nm" value='<%=emp_nm%>'>
               	<input class="input100" type="hidden" name="email" value='<%=email%>'>
               	<input class="input100" type="hidden" name="otp_flag" value=''>
               	<input class="input100" type="hidden" name="msg_type" value=''>
               	<input class="input100" type="hidden" name="msg" value=''>
               	<input class="input100" type="hidden" name="rel_lock_flag" value='<%=rel_lock_flag%>'>
               	<input class="input100" type="hidden" name="reset_pswd_flag" value='<%=reset_pswd_flag%>'>
<%if(login && genOtp)
{
	String ip=request.getRemoteAddr();
	//Setting of session attribute is handled in iindex.jsp
	
	/*
	session.setAttribute("emp_uid", username);
	//session.setAttribute("comp_cd", comp_cd);
	//session.setAttribute("comp_abbr", comp_abbr);
	//session.setAttribute("comp_nm", comp_nm);
	//session.setAttribute("comp_logo", comp_logo);
	session.setAttribute("emp_cd", emp_cd);
	session.setAttribute("emp_nm", emp_nm);
	session.setAttribute("email", email);
	session.setAttribute("ip",ip);
	//session.setMaxInactiveInterval(1800); //handled in common_js.jsp file.
	*/
	
	try
	{
		new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, "",emp_nm, ip, "0", "Login Page","0","Login Page", "", "", "Login");  	
	}
	catch(Exception infoLogger)
	{
		infoLogger.printStackTrace();
	}
	
	String url ="";
	String otp_url="";
	String msg = "OTP is mailed to registered email address!";
	String msg_type = "S";
	
	//Handled in iindex.jsp file 
	/*if(!passChangeReq) 
	{	
		if(passChangeDays<=Integer.parseInt(exp_days))
		{
			//url = "fms.jsp";
			url = "profile_selection.jsp";
			if(!default_profile.equals(""))
			{
				url+="?comp_profile="+default_profile;
			}
			//otp_url = "otpauth.jsp?url="+url+"&msg_type="+msg_type+"&msg="+msg+"&emp_uid="+username+"&emp_cd="+emp_cd+"&emp_nm="+emp_nm+"&email="+email;	// for OTP functionality
			otp_url = "otpauth.jsp?msg_type="+msg_type+"&msg="+msg+"&emp_uid="+username+"&emp_cd="+emp_cd+"&emp_nm="+emp_nm+"&email="+email;//+"&comp_profile="+default_profile;	// for OTP functionalit
			//otp_url = "otpauth.jsp?msg_type="+msg_type+"&msg="+msg+"&emp_uid="+username;
		}
		if(Integer.parseInt(rem_days)>=(Integer.parseInt(exp_days)-passChangeDays)){
			
			url = "../home/fms.jsp?expin="+(Integer.parseInt(exp_days)-passChangeDays);
			otp_url = "otpauth.jsp?url="+url+"&msg_type="+msg_type+"&msg="+msg+"&emp_uid="+username+"&emp_cd="+emp_cd+"&emp_nm="+emp_nm+"&email="+email; //for OTP functionality
		}
		//response.sendRedirect(url);
	}
	else
	{ 	
		if(passChangeDays>Integer.parseInt(exp_days))
		{
			//String url = "../admin/frm_requried_password_change.jsp?exped="+passChangeDays;
			url = "../admin/frm_requried_password_change.jsp?exped="+passChangeDays;
			otp_url = "otpauth.jsp?url="+url+"&msg_type="+msg_type+"&msg="+msg+"&emp_uid="+username+"&emp_cd="+emp_cd+"&emp_nm="+emp_nm+"&email="+email;
			//response.sendRedirect(url);
		}
		else
		{
			url = "../admin/frm_requried_password_change.jsp";
			otp_url = "otpauth.jsp?url="+url+"&msg_type="+msg_type+"&msg="+msg+"&emp_uid="+username+"&emp_cd="+emp_cd+"&emp_nm="+emp_nm+"&email="+email;
			//response.sendRedirect(url);	
		}
		//response.sendRedirect(otp_url);
	}*/
	%>
	<script>
		window.onload = function()
		{
			document.forms[0].emp_uid.value= '<%=username%>';
			document.forms[0].msg_type.value='<%=msg_type%>';
			document.forms[0].msg.value='<%=msg%>';
			document.forms[0].otp_flag.value=true;
			document.forms[0].reset_pswd_flag.value=false;
			document.forms[0].rel_lock_flag.value=false;
			document.forms[0].action = "login.jsp";
			document.forms[0].submit();
		}
		
	</script>
<%}
else if(rel_lock_flag)
{
	if(release_lock  && genOtp)
	{
		
		String msg = "OTP for self unlock is mailed to registered email address!";
		String msg_type = "S";
		%>
		<script>
		window.onload = function()
		{
			document.forms[0].emp_uid.value= '<%=username%>';
			document.forms[0].msg_type.value='<%=msg_type%>';
			document.forms[0].msg.value='<%=msg%>';
			document.forms[0].rel_lock_flag.value=true;
			document.forms[0].otp_flag.value=true;
			document.forms[0].action = "login.jsp";
			document.forms[0].submit();
		}
		
	</script>
	<%}
	else
	{
		String url="";
		if(release_lock && !genOtp)
		{
			String msg = "Failed to Generte OTP Email, Please contact System Administrator!";
			url = "../home/login.jsp?msg="+msg;
		}
		else
		{
			String msg ="The User Account Self UnLocked Falied!";
			url = "../home/login.jsp?msg="+msg;
		}
		response.sendRedirect(url);
	}
}
else if(reset_pswd_flag)
{
	String msg = "";
	String msg_type="";
	if(reset_pswd && genOtp)
	{
		msg="OTP for forgot password is mailed to registered email address!";
		msg_type = "S";%>
		<script>
		window.onload = function()
		{
			document.forms[0].emp_uid.value= '<%=username%>';
			document.forms[0].msg_type.value='<%=msg_type%>';
			document.forms[0].msg.value='<%=msg%>';
			document.forms[0].reset_pswd_flag.value=true;
			document.forms[0].otp_flag.value=true;
			document.forms[0].action = "login.jsp";
			document.forms[0].submit();
		}
	</script>
	<%}
	else
	{
		String url="";
		if(reset_pswd && !genOtp)
		{
			msg = "Failed to Generte OTP Email, Please contact System Administrator!";
			url = "../home/login.jsp?msg="+msg;
		}
		else
		{
			msg = out_msg;
			url = "../home/login.jsp?msg="+msg;
		}
		response.sendRedirect(url);
	}
	
}
else
{
	if(login && !genOtp)
	{
		String msg = "Failed to Generte OTP Email, Please contact System Administrator!";
		String url = "../home/login.jsp?msg="+msg;
		response.sendRedirect(url);
	}
	else if(locked)
	{%>
		<script>
			var url = "login.jsp?msg=Your account has been <b>locked</b>, please contact system Admin!!";
			location.replace(url);
		</script>	
	<%}
	else
	{
		if(disabled)
		{
			String msg="Your account has been <b>Disabled</b>, please contact system Admin!!";
			
			String url = "../home/login.jsp?msg="+msg;
			response.sendRedirect(url);
		}
		else if(dormant)
		{
			String msg="Your account is <b>Dormant</b>, please contact system Admin!!";
			
			String url = "../home/login.jsp?msg="+msg;
			response.sendRedirect(url);
		}
		else if(removed)
		{
			String msg="Your account is <b>Removed</b>, please contact system Admin!!";
			
			String url = "../home/login.jsp?msg="+msg;
			response.sendRedirect(url);
		}
		else
		{
			String msg="";
			
			if(!incorrUserNm)
			{
				String ip=request.getRemoteAddr();
				
				LoginAlter.setEmplo_nm(emp_nm);
				LoginAlter.setEmplo_cd(emp_cd);
				LoginAlter.setIp(ip);
				LoginAlter.setUsername(username);
				LoginAlter.setPassword(password);
				//LoginAlter.setCompany_cd(company_cd);
				LoginAlter.setCallFlag("LOCK_USER");
				LoginAlter.init();
				
				Vector VLOGIN_ATTAMPT = LoginAlter.getVLOGIN_ATTAMPT();
				
				//Check Size of array VLOGIN_ATTEMPT
				if(VLOGIN_ATTAMPT.size()==0)
				{
					msg = "Login in to application failed! Please connect system Admin!";
				}
				else
				{
					String rem_attampts = String.valueOf(3-Integer.parseInt(""+VLOGIN_ATTAMPT.elementAt(0)));
					if(rem_attampts.equals("0"))
					{
						msg="Your account has been locked,<br> please contact System Admin!!";
					}
					else
					{
						msg="Invalid Username or Password!! <br> (Invalid Attempt "+VLOGIN_ATTAMPT.elementAt(0)+" of 3)";
					}
				}
				
				try
				{
					new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, "",emp_nm, ip, "0", "Login Page","0","Login Page", "", "", msg.replaceAll("<br>", ""));  	
				}
				catch(Exception infoLogger)
				{
					infoLogger.printStackTrace();
				}
				
				String url = "../home/login.jsp?msg="+msg;
				response.sendRedirect(url);
			}
			else
			{
				msg="Invalid Username!!!";
			
				String url = "../home/login.jsp?msg="+msg;
				response.sendRedirect(url);
			}
		}
	%>
	<%}%>
<%}%>
				
</form>
</body>
</html>