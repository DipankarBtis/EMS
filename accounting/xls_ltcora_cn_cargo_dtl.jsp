<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.accounting.DataBean_Accounting" id="accounting" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String str_month =""+currentMonth;
if(str_month.length()==1)
{
	str_month = "0"+str_month;
}

String month=request.getParameter("month")==null?""+str_month:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
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
accounting.setCallFlag("LTCORA_CN_CARGO_RPT");
accounting.setComp_cd(owner_cd);
accounting.setMonth(month);
accounting.setYear(year);
accounting.init();

Vector VCOUNTERPARTY_CD = accounting.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = accounting.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = accounting.getVCOUNTERPARTY_NM();

Vector VSHIP_CD = accounting.getVSHIP_CD();
Vector VSUPP_CD = accounting.getVSUPP_CD();
Vector VCARGO_REF_NO = accounting.getVCARGO_REF_NO();
Vector VSHIP_NM = accounting.getVSHIP_NM();
Vector VSUPP_NM = accounting.getVSUPP_NM();
Vector VACTUAL_RECPT_DT = accounting.getVACTUAL_RECPT_DT();
Vector VTOTAL_ADQ_QTY = accounting.getVTOTAL_ADQ_QTY();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="7" rowspan="2" align="left">LTCORA CN Cargo Details (<%=month%>/<%=year%>)</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead id="tbsearch">
			<tr>
				<th>Sr#</th>
				<th>Customer Name</th>
				<th>Cargo Ref#</th>
				<th>Vessel Name</th>
				<th>Supplier Name</th>
				<th>Actual Receipt Date</th>
				<th>Actual Unloaded Qty <br>(MMBTU)</th>
			</tr>
		</thead>
		<tbody>
		<%int i=0,k=0;
		if(VCOUNTERPARTY_CD.size() > 0){ %>
			<%for(i=0; i<VCOUNTERPARTY_CD.size(); i++){ 
			k+=1;
			%>
			<tr>
				<td align="center"><%=k%></td>
				<td align="center" title="<%=VCOUNTERPARTY_ABBR.elementAt(i) %>"><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
				<td align="center"><%=VCARGO_REF_NO.elementAt(i) %></td>
				<td align="center"><%=VSHIP_NM.elementAt(i) %></td>
				<td align="center"><%=VSUPP_NM.elementAt(i) %></td>
				<td align="center"><%=VACTUAL_RECPT_DT.elementAt(i) %></td>
				<td align="center"><%=VTOTAL_ADQ_QTY.elementAt(i) %></td>
			</tr>
			<%} %>
		<%}else{%>
			<tr>
				<td colspan="7" align="center"><%=utilmsg.infoMessage("<b>No Cargo Data Available!</b>") %></td>
			</tr>
		<%}%>
		</tbody>
	</table>
</body>
</html>