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
<jsp:useBean class="com.etrm.fms.util.DataBean_LoginAlter" id="LoginAlter" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.Otp_Handler" id="otpAuth" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateutil" scope="page"></jsp:useBean>
<%
String ip=request.getRemoteAddr();
String otpval= request.getParameter("otpval")==null?"":request.getParameter("otpval");
String valid_otp_time = request.getParameter("valid_otp_time")==null?"":request.getParameter("valid_otp_time");
String resend_flag = request.getParameter("resend_flag")==null?"":request.getParameter("resend_flag");
String count_trials = request.getParameter("count_trials")==null?"0":request.getParameter("count_trials");
String emp_uid = request.getParameter("emp_uid")==null?"":request.getParameter("emp_uid");
boolean rel_lock_flag = Boolean.parseBoolean(request.getParameter("rel_lock_flag")==null?"false":request.getParameter("rel_lock_flag"));
boolean reset_pswd_flag = Boolean.parseBoolean(request.getParameter("reset_pswd_flag")==null?"false":request.getParameter("reset_pswd_flag"));

LoginAlter.setUsername(emp_uid);
LoginAlter.setCallFlag("USER DETAILS");
LoginAlter.init();
boolean passChangeReq = LoginAlter.getPassChangeReq();
Integer passChangeDays = LoginAlter.getPassChangeDays();

String emp_cd = LoginAlter.getEmp_cd();
String emp_nm = LoginAlter.getEmp_nm();
String email = LoginAlter.getEmail();
String default_profile = LoginAlter.getDefault_profile();

String exp_days = LoginAlter.getExp_days();
String rem_days = LoginAlter.getRem_days();

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
boolean otp_status=Boolean.parseBoolean(valid_otp_time);  
otpAuth.setOtp_val(otpval);
otpAuth.setEmp_Cd(emp_cd);
//otpAuth.setComp_Cd(comp_cd);
otpAuth.verifyOTP();

boolean result=otpAuth.getResult();
String url="";
%>
<body>
<!-- <form method="post" action = "../servlet/Frm_reset_password"> -->
<form method="post" action = "../servlet/Frm_reset_password">
               	<input class="input100" type="hidden" name="emp_uid" value='<%=emp_uid%>'>
               	<input class="input100" type="hidden" name="msg_type" value=''>
               	<input class="input100" type="hidden" name="msg" value=''>
               	<input class="input100" type="hidden" name="count_trials" value='<%=count_trials%>'>
               	<input class="input100" type="hidden" name="otp_flag" value=''>
               	<input class="input100" type="hidden" name="rel_lock_flag" value='<%=rel_lock_flag%>'>
               	<input class="input100" type="hidden" name="reset_pswd_flag" value='<%=reset_pswd_flag%>'>
               	<input class="input100" type="hidden" name="frm_submit_flag" value=''>
               	<input class="input100" type="hidden" name="option" value=''>
<%
if(!rel_lock_flag && !reset_pswd_flag)
{
	if(!resend_status)
	{
		if(otp_status)
		{
			if(result == true)
			{
				session.setAttribute("emp_uid", emp_uid);
				session.setAttribute("emp_cd", emp_cd);
				session.setAttribute("emp_nm", emp_nm);
				session.setAttribute("email", email);
				session.setAttribute("ip",ip);
				session.setMaxInactiveInterval(-1); //handled in common_js.jsp file.
				/*try
				{
					new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, "",emp_nm, ip, "0", "Login Page","0","Login Page", "", "", "Login");  	
				}
				catch(Exception infoLogger)
				{
					infoLogger.printStackTrace();
				} */
				
				if(!passChangeReq) 
				{	
					url = "profile_selection.jsp";
					if(passChangeDays<=Integer.parseInt(exp_days))
					{
						//url = "fms.jsp";
						if(!default_profile.equals(""))
						{
							url+="?comp_profile="+default_profile;
						}
					}
					if(Integer.parseInt(rem_days)>=(Integer.parseInt(exp_days)-passChangeDays))
					{
						if(!default_profile.equals(""))
						{
							url+="&expin="+(Integer.parseInt(exp_days)-passChangeDays);
						}
						else
						{
							url+="?expin="+(Integer.parseInt(exp_days)-passChangeDays);
						}
					}
				}
				else
				{ 	
					if(passChangeDays>Integer.parseInt(exp_days))
					{
// 						url = "../admin/frm_requried_password_change.jsp?exped="+passChangeDays;
						url = "../home/frm_requried_password_change.jsp?exped="+passChangeDays;
					}
					else
					{
// 						url = "../admin/frm_requried_password_change.jsp";
						url = "../home/frm_requried_password_change.jsp";
					}
				}
				response.sendRedirect(url);
			}
			
			else
			{
				String msg="Incorrect OTP!!";%>
			<script>
				window.onload = function()
				{
					document.forms[0].emp_uid.value= '<%=emp_uid%>';
					document.forms[0].msg.value='<%=msg%>';
					document.forms[0].count_trials.value='<%=count%>';
					document.forms[0].otp_flag.value=true;
					document.forms[0].action = "login.jsp";
					document.forms[0].submit();
				}
			</script>			
			<%}
		}
		else
		{
			otpAuth.removeData(emp_cd);
			String msg = "Your Otp has expired, <br>Please click Resend OTP!";
		%>
			<script>
				window.onload = function()
				{
					document.forms[0].emp_uid.value= '<%=emp_uid%>';
					document.forms[0].msg.value='<%=msg%>';
					document.forms[0].count_trials.value='<%=count%>';
					document.forms[0].otp_flag.value=true;
					document.forms[0].action = "login.jsp";
					document.forms[0].submit();
				}
			</script>
		<%}
	}
	else
	{
		count++;
		otpAuth.removeData(emp_cd);
		if(count<Integer.parseInt(max_otp))
		{
			otpAuth.manageOTP(emp_cd, email,Integer.parseInt(otp_min_length),Integer.parseInt(otp_max_length));
			
			String msg = "OTP is resend to your Registered Email address";
			String msg_type="S";
		%>
			<script>
				window.onload = function()
				{
					document.forms[0].emp_uid.value= '<%=emp_uid%>';
					document.forms[0].msg.value='<%=msg%>';
					document.forms[0].msg_type.value='<%=msg_type%>';
					document.forms[0].count_trials.value='<%=count%>';
					document.forms[0].otp_flag.value=true;
					document.forms[0].action = "login.jsp";
					document.forms[0].submit();
				}
			</script>	
		<%}
		else
		{
			String msg = "You have exceed the maximum number of OTP trials!";
			response.sendRedirect("login.jsp?msg="+msg);
		}
	}
}
else if(rel_lock_flag)
{
	if(!resend_status)
	{
		if(otp_status)
		{
			if(result == true)
			{
				String option = "RELEASE_LOCK";
			%>
				<script>
					window.onload = function()
					{
						document.forms[0].emp_uid.value= '<%=emp_uid%>';
						document.forms[0].count_trials.value='<%=count%>';
						document.forms[0].rel_lock_flag.value='<%=rel_lock_flag%>';
						document.forms[0].option.value='<%=option%>';
						document.forms[0].frm_submit_flag.value=true;
						document.forms[0].otp_flag.value=true;
						//document.forms[0].action = "../servlet/Frm_reset_password";
						document.forms[0].action = "login.jsp";
						document.forms[0].submit();
					}
				</script>
			<%}
			else
			{
				String msg = "Incorrect OTP!!";
			%>
				<script>
				window.onload = function()
				{
					document.forms[0].emp_uid.value= '<%=emp_uid%>';
					document.forms[0].msg.value='<%=msg%>';
					document.forms[0].count_trials.value='<%=count%>';
					document.forms[0].rel_lock_flag.value='<%=rel_lock_flag%>';
					document.forms[0].otp_flag.value=true;
					document.forms[0].action = "login.jsp";
					document.forms[0].submit();
				}
			</script>
			<%}
		}
		else
		{
			otpAuth.removeData(emp_cd);
			String msg = "Your Otp has expired, <br>Please click Resend OTP!";
		%>
			<script>
				window.onload = function()
				{
					document.forms[0].emp_uid.value= '<%=emp_uid%>';
					document.forms[0].msg.value='<%=msg%>';
					document.forms[0].count_trials.value='<%=count%>';
					document.forms[0].rel_lock_flag.value='<%=rel_lock_flag%>';
					document.forms[0].otp_flag.value=true;
					document.forms[0].action = "login.jsp";
					document.forms[0].submit();
				}
			</script>
		<%}
	}
	else
	{
		count++;
		otpAuth.removeData(emp_cd);
		if(count<Integer.parseInt(max_otp))
		{
			otpAuth.manageOTP(emp_cd, email,Integer.parseInt(otp_min_length),Integer.parseInt(otp_max_length));
			
			String msg = "OTP for self unlock is resend to your Registered Email address";
			String msg_type="S";
		%>
			<script>
				window.onload = function()
				{
					document.forms[0].emp_uid.value= '<%=emp_uid%>';
					document.forms[0].msg.value='<%=msg%>';
					document.forms[0].msg_type.value='<%=msg_type%>';
					document.forms[0].count_trials.value='<%=count%>';
					document.forms[0].rel_lock_flag.value='<%=rel_lock_flag%>';
					document.forms[0].otp_flag.value=true;
					document.forms[0].action = "login.jsp";
					document.forms[0].submit();
				}
			</script>	
		<%}
		else
		{
			String msg = "You have exceed the maximum number of OTP trials!";
			response.sendRedirect("login.jsp?msg="+msg);
		}
	}
}
else if(reset_pswd_flag)
{
	if(!resend_status)
	{
		if(otp_status)
		{
			if(result == true)
			{
				String option = "FORGET_PASSWORD";
				%>
				<script>
					window.onload = function()
					{
						document.forms[0].emp_uid.value= '<%=emp_uid%>';
						document.forms[0].count_trials.value='<%=count%>';
						document.forms[0].reset_pswd_flag.value='<%=reset_pswd_flag%>';
						document.forms[0].option.value='<%=option%>';
						document.forms[0].frm_submit_flag.value=true;
						document.forms[0].otp_flag.value=true;
// 						document.forms[0].action = "../servlet/Frm_reset_password";
						document.forms[0].action = "login.jsp";
						document.forms[0].submit();
					}
				</script>	
			<%}
			else
			{
				String msg = "Incorrect OTP";%>
			
				<script>
				window.onload = function()
				{
					document.forms[0].emp_uid.value= '<%=emp_uid%>';
					document.forms[0].msg.value='<%=msg%>';
					document.forms[0].count_trials.value='<%=count%>';
					document.forms[0].reset_pswd_flag.value='<%=reset_pswd_flag%>';
					document.forms[0].otp_flag.value=true;
					document.forms[0].action = "login.jsp";
					document.forms[0].submit();
				}
			</script>
			<%}
		}
		else
		{
			otpAuth.removeData(emp_cd);
			String msg = "Your Otp has expired, <br>Please click Resend OTP!";
		%>
			<script>
				window.onload = function()
				{
					document.forms[0].emp_uid.value= '<%=emp_uid%>';
					document.forms[0].msg.value='<%=msg%>';
					document.forms[0].count_trials.value='<%=count%>';
					document.forms[0].reset_pswd_flag.value='<%=reset_pswd_flag%>';
					document.forms[0].otp_flag.value=true;
					document.forms[0].action = "login.jsp";
					document.forms[0].submit();
				}
			</script>
		<%}
	}
	else
	{
		count++;
		otpAuth.removeData(emp_cd);
		//otpAuth.removeData(comp_cd, emp_cd);
		if(count<Integer.parseInt(max_otp))
		{
			otpAuth.manageOTP(emp_cd, email,Integer.parseInt(otp_min_length),Integer.parseInt(otp_max_length));
			
			String msg = "OTP for forgot password is resend to your Registered Email address";
			String msg_type="S";
		%>
			<script>
				window.onload = function()
				{
					document.forms[0].emp_uid.value= '<%=emp_uid%>';
					document.forms[0].msg.value='<%=msg%>';
					document.forms[0].msg_type.value='<%=msg_type%>';
					document.forms[0].count_trials.value='<%=count%>';
					document.forms[0].reset_pswd_flag.value='<%=reset_pswd_flag%>';
					document.forms[0].otp_flag.value=true;
					document.forms[0].action = "login.jsp";
					document.forms[0].submit();
				}
			</script>	
		<%}
		else
		{
			String msg = "You have exceed the maximum number of OTP trials!";
			response.sendRedirect("login.jsp?msg="+msg);
		}
	}
}
%>

</form>
</body>
</html>