<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"MODIFY":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
String financial_year = request.getParameter("financial_year")==null?"0":request.getParameter("financial_year");
String turnover_cd = request.getParameter("turnover_cd")==null?"0":request.getParameter("turnover_cd");
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

dbcounterpty.setCallFlag("ENTITY_TURNOVER_ENTRY");
dbcounterpty.setEntity_role(entity_role);
dbcounterpty.setComp_cd(owner_cd);
dbcounterpty.setFinancial_year(financial_year);
dbcounterpty.setTurnover_cd(turnover_cd);
dbcounterpty.init();

Vector VFINANCIAL_YEAR = dbcounterpty.getVFINANCIAL_YEAR();

Vector VCOUNTERPARTY_CD = dbcounterpty.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcounterpty.getVCOUNTERPARTY_ABBR();
Vector VTURNOVER_FLAG = dbcounterpty.getVTURNOVER_FLAG();

String entity_nm="";

if(entity_role.equals("C"))
{
	entity_nm="Customer";
}
if(entity_role.equals("T"))
{
	entity_nm="Trader";
}
if(entity_role.equals("B"))
{
	entity_nm="Business Owner";
}
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="3" rowspan="2" align="left"><%=entity_nm %> Turnover Entry for <%=financial_year %></th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>Sr#</th>
				<th>Counterparty Name</th>
				<th>Turnover</th>
			</tr>
		</thead>
		<tbody>
		<%if(entity_role.equals("0") || financial_year.equals("0") || turnover_cd.equals("0")){ %>
			<tr>
				<td align="center" colspan="4"><%=utilmsg.infoMessage("<b>Select Entity and Finanicial Year/Turnover!</b>") %></td>
			</tr>
		<%}else{ %>
			<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
			<tr>
				<td align="center"><%=i+1%></td>
				<td><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i) %></td>
				<td align="center">
				<%if(VTURNOVER_FLAG.elementAt(i).equals("Y")){ %>Yes<%}else if(VTURNOVER_FLAG.elementAt(i).equals("N")){ %>No<%}else{ %><%} %>
				</td>
			</tr>
			<%} %>
		<%} %>
		</tbody>
	</table>
	
</body>
</html>