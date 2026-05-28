
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

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt = request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
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

cr_report.setCallFlag("COLLATERAL_AUDIT_REPORT");
cr_report.setComp_cd(owner_cd);
cr_report.setCounterparty_cd(counterparty_cd);
cr_report.setFrom_dt(from_dt);
cr_report.setTo_dt(to_dt);
cr_report.init();

Vector VSECURITYDETAILS = cr_report.getVSECURITYDETAILS();
Vector VCOUNTERPARTY_CD = cr_report.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = cr_report.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_NAME = cr_report.getVCOUNTERPARTY_NAME();
Vector VCOUNTERPARTY_ABBR = cr_report.getVCOUNTERPARTY_ABBR();
Vector VLAST_UPDATE_DATE = cr_report.getVLAST_UPDATE_DATE();
Vector VLAST_UPDATE_BY = cr_report.getVLAST_UPDATE_BY();
Vector VDEAL_NO = cr_report.getVDEAL_NO();
Vector VREF_NO = cr_report.getVREF_NO();
Vector VSEC_TYPE = cr_report.getVSEC_TYPE();
Vector VBGCOLOR = cr_report.getVBGCOLOR();
Vector VCONT_REF = cr_report.getVCONT_REF();
Vector VAUDIT_TYPE = cr_report.getVAUDIT_TYPE();
Vector VCO_ABBR = cr_report.getVCO_ABBR();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="10" rowspan="2" align="left">Collateral Audit report (<%=from_dt%> - <%=to_dt%>)</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>SR#</th>
				<th align="center">Legal Entity</th>
				<th align="center">Last Updated On</th>
				<th align="center">Last Updated By</th>
				<th align="center">Counterparty Name</th>
				<th align="center">Counterparty ABBR</th>
				<th align="center">Security Type</th>
				<th align="center">Security Ref#</th>
				<th align="center">Contract#</th>
				<th>Change Type</th>
			</tr>
		</thead>
		<tbody>
		<%if(VSECURITYDETAILS.size()>0){
			int j=0;%>
			<%for(int i=0;i<VSECURITYDETAILS.size(); i++){ 
			 if(!VREF_NO.elementAt(i).equals("") && VAUDIT_TYPE.elementAt(i).equals("")){
				j++;%>
				<tr>
					<td align="center"><%=j%></td>
					<td align="center"><%=VCO_ABBR.elementAt(i)%></td>
					<td align="center"><%=VLAST_UPDATE_DATE.elementAt(i)%></td>
					<td align="center"><%=VLAST_UPDATE_BY.elementAt(i)%></td>
					<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
					<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
					<td align="center"><%=VSEC_TYPE.elementAt(i) %></td>
					<td align="center"><font style="background: <%=VBGCOLOR.elementAt(i)%>"><%=VREF_NO.elementAt(i) %></font></td>
					<td align="center"><%=VDEAL_NO.elementAt(i)%><br>
					</td>
					<td><%=VSECURITYDETAILS.elementAt(i) %></td>
				</tr>
				<%} %>
			<%} %>
		<%}else{ %>
			<tr><td colspan="10" align="center"><b>Audit History Not Available for Selected Date!</b></td></tr>
		<%} %>
		</tbody>
	</table>
	<br>
	<table width="100%" border="1">
		<tr>
			<th colspan="8" rowspan="2" align="left">Limit Audit report (<%=from_dt%> - <%=to_dt%>)</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>SR#</th>
				<th align="center">Legal Entity</th>
				<th align="center">Last Updated On</th>
				<th align="center">Last Updated By</th>
				<th align="center">Counterparty Name</th>
				<th align="center">Counterparty ABBR</th>
				<!-- <th align="center">Security Type</th> -->
				<th align="center">Credit Rating/Limit Ref#</th>
				<!-- <th align="center">Contract#</th> -->
				<th>Change Type</th>
			</tr>
		</thead>
		<tbody>
		<%if(VSECURITYDETAILS.size()>0){
			int j=0;%>
			<%for(int i=0;i<VSECURITYDETAILS.size(); i++){ 
			 if(!VREF_NO.elementAt(i).equals("") && !VAUDIT_TYPE.elementAt(i).equals("")){
				j++;%>
				<tr>
					<td align="center"><%=j%></td>
					<td align="center"><%=VCO_ABBR.elementAt(i)%></td>
					<td align="center"><%=VLAST_UPDATE_DATE.elementAt(i)%></td>
					<td align="center"><%=VLAST_UPDATE_BY.elementAt(i)%></td>
					<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
					<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
					<%-- <td align="center"><%=VSEC_TYPE.elementAt(i) %></td> --%>
					<td align="center"><font style="background: <%=VBGCOLOR.elementAt(i)%>"><%=VREF_NO.elementAt(i) %></font></td>
					<%-- <td align="center"><%=VDEAL_NO.elementAt(i)%><br> --%>
					</td>
					<td><%=VSECURITYDETAILS.elementAt(i) %></td>
				</tr>
				<%} %>
			<%} %>
		<%}else{ %>
			<tr><td colspan="8" align="center"><b>Audit History Not Available for Selected Date!</b></td></tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>