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

//System.out.println(username+"----"+password);

LoginAlter.setUsername(username);
LoginAlter.setPassword(password);
LoginAlter.setCallFlag("LOGIN_CHECK");
LoginAlter.init();

boolean login = LoginAlter.getLogin();
boolean locked = LoginAlter.getLocked();
boolean disabled = LoginAlter.getDisabled();
boolean dormant = LoginAlter.getDormant();
boolean removed = LoginAlter.getRemoved();
boolean passChangeReq = LoginAlter.getPassChangeReq();
boolean incorrUserNm = LoginAlter.getIncorrUserNm();

Integer passChangeDays = LoginAlter.getPassChangeDays();

String emp_cd = LoginAlter.getEmp_cd();
String emp_nm = LoginAlter.getEmp_nm();
//String comp_cd = LoginAlter.getComp_cd();
//String comp_abbr = LoginAlter.getComp_abbr();
//String comp_nm = LoginAlter.getComp_nm();
String email = LoginAlter.getEmail();
//String comp_logo = LoginAlter.getComp_logo();

String exp_days = LoginAlter.getExp_days();
String rem_days = LoginAlter.getRem_days();

boolean lock_flag=false;

boolean temp=false;
%>

<body>
<%if(login)
{
	String ip=request.getRemoteAddr();
	session.setAttribute("emp_uid", username);
	//session.setAttribute("comp_cd", comp_cd);
	//session.setAttribute("comp_abbr", comp_abbr);
	//session.setAttribute("comp_nm", comp_nm);
	//session.setAttribute("comp_logo", comp_logo);
	session.setAttribute("emp_cd", emp_cd);
	session.setAttribute("emp_nm", emp_nm);
	session.setAttribute("email", email);
	session.setAttribute("ip",ip);
	session.setMaxInactiveInterval(-1); //handled in common_js.jsp file.
	
	try
	{
		new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, "",emp_nm, ip, "0", "Login Page","0","Login Page", "", "", "Login");  	
	}
	catch(Exception infoLogger)
	{
		infoLogger.printStackTrace();
	}
	
	if(!passChangeReq) 
	{
		String url ="";
		
		if(passChangeDays<=Integer.parseInt(exp_days))
		{
			//url = "fms.jsp";
			url = "profile_selection.jsp";
		}
		if(Integer.parseInt(rem_days)>=(Integer.parseInt(exp_days)-passChangeDays)){
			
			url = "../home/fms.jsp?expin="+(Integer.parseInt(exp_days)-passChangeDays);
		}
		response.sendRedirect(url);
	}
	else
	{ 	
		if(passChangeDays>Integer.parseInt(exp_days))
		{
			String url = "../admin/frm_requried_password_change.jsp?exped="+passChangeDays;
			response.sendRedirect(url);
		}
		else
		{
			String url = "../admin/frm_requried_password_change.jsp";
			response.sendRedirect(url);	
		}
	}
	
}
else
{
	if(locked)
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

				String rem_attampts = String.valueOf(3-Integer.parseInt(""+VLOGIN_ATTAMPT.elementAt(0)));
				
				if(rem_attampts.equals("0"))
				{
					msg="Your account has been locked,<br> please contact System Admin!!";
				}
				else
				{
					msg="Invalid Username or Password!! <br> (Invalid Attempt "+VLOGIN_ATTAMPT.elementAt(0)+" of 3)";
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
</body>
</html>