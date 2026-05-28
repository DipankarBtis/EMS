<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import= "java.util.*"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
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

dbcounterpty.setCallFlag("COUNTERPARTY_AUDIT_REPORT");
dbcounterpty.setCounterparty_cd(counterparty_cd);
dbcounterpty.setFrom_dt(from_dt);
dbcounterpty.setTo_dt(to_dt);
dbcounterpty.init();

Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_DETAIL = dbcounterpty.getVCOUNTERPARTY_DETAIL();
Vector VCOUNTERPARTY_REGISTER_ADDRESS_DETAIL = dbcounterpty.getVCOUNTERPARTY_REGISTER_ADDRESS_DETAIL();
Vector VSTATUS = dbcounterpty.getVSTATUS();
Vector VLAST_UPDATE = dbcounterpty.getVLAST_UPDATE();
Vector VLAST_UPDATE_BY = dbcounterpty.getVLAST_UPDATE_BY();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="7" rowspan="2" align="left">Counterparty Modification - Audit Report (<%=from_dt %> - <%=to_dt %>)</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>Sr#</th>
				<th>Last Update</th>
				<th>
					Last Update By
				</th>
				<th>
					Counterparty
				</th>
				<th>Counterparty Detail</th>
				<th>Counterparty Register Address & Contact Detail</th>
				<th>Status</th>
			</tr>
		</thead>
		<tbody>
		<%if(VCOUNTERPARTY_DETAIL.size()>0){ %>
			<%for(int i=0;i<VCOUNTERPARTY_DETAIL.size(); i++){ %>
			<tr>
				<td align="center"><%=i+1%></td>
				<td align="center"><%=VLAST_UPDATE.elementAt(i)%></td>
				<td><%=VLAST_UPDATE_BY.elementAt(i)%></td>
				<td><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
				<td><%=VCOUNTERPARTY_DETAIL.elementAt(i)%></td>
				<td><%=VCOUNTERPARTY_REGISTER_ADDRESS_DETAIL.elementAt(i)%></td>
				<td><%=VSTATUS.elementAt(i) %></td>
			</tr>
			<%} %>
		<%} %>
		</tbody>
	</table>
</body>
</html>