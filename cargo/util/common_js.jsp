<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@ include file="../sess/Expire.jsp" %>
<%String COMMlogo="";
if(session.getAttribute("comp_logo")==null||session.getAttribute("comp_logo")==""||session.getAttribute("comp_logo").toString().equals("null"))
{
	COMMlogo="";
}  
else
{
	COMMlogo=""+session.getAttribute("comp_logo");
}

String sel_comp_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	sel_comp_cd="";
}  
else
{
	sel_comp_cd=""+session.getAttribute("comp_cd");
}
%>
<link rel="SHORTCUT ICON" href="../images/<%=COMMlogo%>">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="refresh" content="<%=session.getMaxInactiveInterval()%>;url=../sess/Expire.jsp?expire_msg=Timeout"/>
<title>FMSng</title>

<link rel="stylesheet" type="text/css" href="../bootstrap/bootstrap5/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="../font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="../css/datepicker/bootstrap-datepicker3.css">
<link rel="stylesheet" type="text/css" href="../css/util.css">
<link rel="stylesheet" type="text/css" href="../css/main.css">
<link rel="stylesheet" type="text/css" href="../css/navbar.css">
<link rel="stylesheet" type="text/css" href="../css/responsive.css">
<link rel="stylesheet" type="text/css" href="../css/common.css">

<% if ("2".equals(sel_comp_cd)) { %>
    <link rel="stylesheet" type="text/css" href="../css/common_2.css">  <!-- CSS for comp_cd = "1" -->
<% } else { %>
    <link rel="stylesheet" type="text/css" href="../css/common.css">  <!-- CSS for comp_cd = "2" -->
<% } %>

<script type="text/javascript" src="../bootstrap/bootstrap5/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/datepicker/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="../js/datepicker/bootstrap-datepicker.es.min.js"></script>

<script type="text/javascript" src="../js/util.js"></script>
<%--Added by Pratham Bhatt 20240617 for session expire  --%>
<script type="text/javascript" src="../js/session.js"></script>

<%@ include file="common_val.jsp" %>

<script type="text/javascript">
document.title = "<%=CommonVariable.app_name%> | <%=owner_abbr%> | <%=formNm%>";
</script>
