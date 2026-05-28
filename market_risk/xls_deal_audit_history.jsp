<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Harsh Maheta 20230901 : Developed export to excel for Deal audit history-->

<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_MarketRisk" id="dbmarket" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateutil" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%

String firstDtOfMth = ""+dateutil.getFirstDateOfMonth();
String sysdate = ""+dateutil.getSysdate();
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String status_flag =request.getParameter("status_flag")==null?sysdate:request.getParameter("status_flag");

String from_dt=request.getParameter("from_dt")==null?firstDtOfMth:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
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
String company_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");

dbmarket.setCallFlag("DEAL_AUDIT_HISTORY");
dbmarket.setFrom_dt(from_dt);
dbmarket.setStatus_flag(status_flag);
dbmarket.setCounterparty_cd(counterparty_cd);
dbmarket.setTo_dt(to_dt);
dbmarket.setComp_cd(company_cd);
dbmarket.init();

Vector VCOUNTERPARTY_CD = dbmarket.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbmarket.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbmarket.getVCOUNTERPARTY_ABBR();
Vector VDEAL_DETAILS = dbmarket.getVDEAL_DETAILS();
Vector VDEAL_NAME = dbmarket.getVDEAL_NAME();
Vector VCONT_NO = dbmarket.getVCONT_NO();
Vector VLAST_UPDATE = dbmarket.getVLAST_UPDATE();
Vector VLAST_UPDATE_BY = dbmarket.getVLAST_UPDATE_BY();
Vector VDEAL_STATUS = dbmarket.getVDEAL_STATUS();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="8" rowspan="2" align="center">Deal Audit History <%=from_dt%> - <%=to_dt%></th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>Sr#</th>
				<th>Updated Date</th>
				<th>Updated By</th>
				<th>Counterparty</th>
				<th>Contract#</th>
				<th>Contract/Trade Ref#</th>
				<th>Change/Activity</th>
				<th>Status</th>
			</tr>
		</thead>
		<tbody>
		<%if(VLAST_UPDATE.size()>0) {%>
			<%for(int i=0;i<VDEAL_DETAILS.size(); i++){ %>
			<tr>
				<td><div><%=i+1%> </div></td>
				<td><div><%=VLAST_UPDATE.elementAt(i) %> </div></td>
				<td><div><%=VLAST_UPDATE_BY.elementAt(i) %> </div></td>
				<td><div><%=VCOUNTERPARTY_ABBR.elementAt(i)%></div></td>
				<td><div><%=VCONT_NO.elementAt(i)%></div></td>
				<td><div><%=VDEAL_NAME.elementAt(i) %> </div></td>
				<td><div><%=VDEAL_DETAILS.elementAt(i)%> </div></td>
				<td><div><%=VDEAL_STATUS.elementAt(i)%> </div></td>
			</tr>
			<%} %>
		<%}else{ %>
			<tr>
				<td colspan="8" align="center">No Changes in Deal is Available!</td>
			</tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>