<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CreditRisk_Report" id="cr_report" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String clearance = request.getParameter("clearance")==null?"K":request.getParameter("clearance");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String owner_cd="";



if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

cr_report.setCallFlag("LIMIT_SUMMARY");
cr_report.setClearance(clearance);
cr_report.setComp_cd(owner_cd);
cr_report.init();

Vector VCOUNTERPARTY_NAME = cr_report.getVCOUNTERPARTY_NAME();
Vector VCREDIT_RATING = cr_report.getVCREDIT_RATING();
Vector VAVAILABLE = cr_report.getVAVAILABLE();
Vector VTOTAL_LIMIT = cr_report.getVTOTAL_LIMIT();
Vector VUNSECURED = cr_report.getVUNSECURED();
Vector VTEMPORARY = cr_report.getVTEMPORARY();
Vector VADJUST_USAGE = cr_report.getVADJUST_USAGE();
Vector VUSAGE = cr_report.getVUSAGE();
Vector VNET_USAGE = cr_report.getVNET_USAGE();
Vector VUSED = cr_report.getVUSED();
Vector VCOUNTERPARTY_ABBR = cr_report.getVCOUNTERPARTY_ABBR();

if(clearance.equals("K"))
{
	clearance = "KYC";
}
else if(clearance.equals("I"))
{
	clearance = "IGX";
}
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="12" rowspan="2" align="left">Limit Summary Report for <%=clearance %></th>
		</tr>
	</table>&nbsp;
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>SR#</th>
				<th>Counterparty/<br> Bank Name</th>
				<th>Counterparty/<br> Bank ABBR</th>
				<th>Credit rating</th>
				<th>Available limit (INR)</th>
				<th>Net Usage (INR)</th>
				<th>Usage (INR)</th>
				<th>Total limit (INR)</th>
				<th>Unsecured limit (INR)</th>
				<th>Temporary limit (INR)</th>
				<th>Usage Adjustment (INR)</th>
				<th>% Used</th>
			</tr>
		</thead>
		<tbody>
		<%int k=0;
		if(VCOUNTERPARTY_NAME.size()!=0)
		{ 
			for(int i=0; i<VCOUNTERPARTY_NAME.size(); i++){
			k+=1;
			%>
				<tr>
					<td align="center"><%=k %></td>
					<td align="center"><%=VCOUNTERPARTY_NAME.elementAt(i) %></td>
					<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i) %></td>
					<td align="center"><%=VCREDIT_RATING.elementAt(i) %></td>
					<td align="right"><%=VAVAILABLE.elementAt(i) %></td>
					<td align="right"><%=VNET_USAGE.elementAt(i) %></td>
					<td align="right"><%=VUSAGE.elementAt(i) %></td>
					<td align="right"><%=VTOTAL_LIMIT.elementAt(i) %></td>
					<td align="right"><%=VUNSECURED.elementAt(i) %></td>
					<td align="right"><%=VTEMPORARY.elementAt(i) %></td>
					<td align="right"><%=VADJUST_USAGE.elementAt(i) %></td>
					<td align="right"><%=VUSED.elementAt(i) %></td>
				</tr>
			<%} %>
		<%}else{ %>
			<tr><td colspan="11" align="center"><%=utilmsg.infoMessage("<b>Limit Summary Data Is Not Available!</b>") %></td></tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>