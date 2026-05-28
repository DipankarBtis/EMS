<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh(comp_cd)
{
	var url = "profile_selection.jsp?comp_profile="+comp_cd;
	location.replace(url);
}
function logout()
{
	var url = "../home/logout.jsp";
	location.replace(url);
	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.util.DataBean_LoginAlter" id="LoginAlter" scope="page"></jsp:useBean>
<%
String comp_profile = request.getParameter("comp_profile")==null?"":request.getParameter("comp_profile");
String expin = request.getParameter("expin")==null?"":request.getParameter("expin");
if(!comp_profile.equals(""))
{
	LoginAlter.setCompany_cd(comp_profile);
	LoginAlter.setCallFlag("ENTITY_DETAIL");
	LoginAlter.init();
	
	String comp_cd = LoginAlter.getComp_cd();
	String comp_abbr = LoginAlter.getComp_abbr();
	String comp_nm = LoginAlter.getComp_nm();
	String comp_logo = LoginAlter.getComp_logo();
	
	session.setAttribute("comp_cd", comp_cd);
	session.setAttribute("comp_abbr", comp_abbr);
	session.setAttribute("comp_nm", comp_nm);
	session.setAttribute("comp_logo", comp_logo);
	
	try
	{
		new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,""+session.getAttribute("emp_nm"),""+session.getAttribute("ip"), "0", "Profile","0","Profile", "", "", "Entered in "+comp_abbr+" Profile!");  	
	}
	catch(Exception infoLogger)
	{
		infoLogger.printStackTrace();
	}
	
	String url = "fms.jsp";
	if(!expin.equals(""))
	{
		url+="?expin="+expin;
	}
	response.sendRedirect(url);
}
else
{
	LoginAlter.setCallFlag("LOGIN_PAGE");
	LoginAlter.init();	
}

Vector VCOMPANY_CD = LoginAlter.getVCOMPANY_CD();
Vector VCOMPANY_NM = LoginAlter.getVCOMPANY_NM();
Vector VCOMPANY_ABBR = LoginAlter.getVCOMPANY_ABBR();

%>
<nav class="navbar navbar-expand-lg navbar-dark" style="background-color: var(--header_color);">
	<div class="container-fluid">
    	<a class="navbar-brand" href="#" style="padding-top: 2px;padding-left: 4px;padding-right: 2px;" onclick="homepage();">
			<img src="../<%=CommonVariable.company_logo_path %>/Shell_logo.png" height="30px">&nbsp;<font style="font-size:24px; color:black"><%=CommonVariable.app_name%></font>
    	</a>
    	<button class="navbar-toggler" style="border-color:var(--header_font_color);" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      		<!-- <span class="navbar-toggler-icon"></span> -->
      		<font><i class="fa fa-bars" style="color:var(--header_font_color);"></i></font>
    	</button>
    	<div class="collapse navbar-collapse" id="navbarSupportedContent">
      		<ul class="navbar-nav me-auto mb-2 mb-lg-0">
      		</ul>
      		<div class="d-flex">
      			<label class="btn" onclick="" style="padding-left:0px;"><b><span id = "min"></span>:<span id = "sec"></span></b></label>
      			<label class="btn" onclick="" style="padding-left:0px;">
      				<b>
      					<i class="fa fa-user-circle fa-lg"></i>&nbsp;
      					<%=session.getAttribute("emp_nm")==null?"":session.getAttribute("emp_nm")%>,
      					<%=session.getAttribute("comp_abbr")==null?"":session.getAttribute("comp_abbr")%>
      				</b>
      			</label>
      			<label class="btn" onclick="logout();" style="padding-left:0px;"><b><i title="Sign out" class="fa fa-sign-out fa-lg"></i>&nbsp;</b></label>
		  	</div>
    	</div>
  	</div>
</nav>

<body>
	<br><br><br><br><br><br>
	<div class="row m-b-5">
		<div class="col-sm-2 col-xs-2 col-md-2"></div>
		<%for(int i=0; i<VCOMPANY_CD.size(); i++){ %>
		<div class="col-sm-4 col-xs-4 col-md-4">	
		    <div class="col" align="center">	    	
		     	<div  align="center" class="p-3 border bg-light" style="height: 120px;width: 120px;font-size: 26px;"> 
		     		<i class="fa fa-building-o fa-2x" onclick="refresh('<%=VCOMPANY_CD.elementAt(i)%>');"></i>
		     		<br><%=VCOMPANY_ABBR.elementAt(i)%>
		     	</div>
    		</div>  
    	</div>	
		<%} %>
		<div class="col-sm-2 col-xs-2 col-md-2"></div>
	</div>				
</body>
</html>