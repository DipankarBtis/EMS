<%
String emp_cd=session.getAttribute("emp_cd")==null?"":""+session.getAttribute("emp_cd");
String emp_nm=session.getAttribute("emp_nm")==null?"":""+session.getAttribute("emp_nm");
String comp_cd=session.getAttribute("comp_cd")==null?"":""+session.getAttribute("comp_cd");
String ip=session.getAttribute("ip")==null?"":""+session.getAttribute("ip");
try
{
	new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, "0", "Logout Page","0","Logout Page", "", "", "Logout");  	
}
catch(Exception infoLogger)
{
	infoLogger.printStackTrace();
}

session.invalidate();
String url="../home/login.jsp";
response.sendRedirect(url);
%>