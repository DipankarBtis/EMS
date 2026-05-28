<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>

<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_Govt_Reports" id="govt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String month_to=request.getParameter("month_to")==null?""+currentMonth:request.getParameter("month_to");
String year_to=request.getParameter("year_to")==null?""+currentYear:request.getParameter("year_to");

if(month.length() == 1)
{
	month="0"+month; 
}
if(month_to.length() == 1)
{
	month_to="0"+month_to; 
}

String owner_cd="";

if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

govt_rpt.setCallFlag("SECTORAL_CONSUMPTION_RPT");
govt_rpt.setComp_cd(owner_cd);
govt_rpt.setMonth(month);
govt_rpt.setYear(year);
govt_rpt.setMonth_to(month_to);
govt_rpt.setYear_to(year_to);
govt_rpt.init();

Vector VMONTH_YEAR = govt_rpt.getVMONTH_YEAR();
Vector VSUPP_COMP_CD = govt_rpt.getVSUPP_COMP_CD();
Vector VPRODUCT_CD = govt_rpt.getVPRODUCT_CD();
Vector VSECTOR_CD = govt_rpt.getVSECTOR_CD();
Vector VRECEV_COMP_CD = govt_rpt.getVRECEV_COMP_CD();
Vector VSTATE_CD = govt_rpt.getVSTATE_CD();
Vector VQTY_MMSCM = govt_rpt.getVQTY_MMSCM();
Vector VQTY_MMBTU = govt_rpt.getVQTY_MMBTU();
Vector VCALORIFIC_VAL = govt_rpt.getVCALORIFIC_VAL();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%">
		<tr>
			<th colspan="8" rowspan="" align="center" style="background-color: #92D050;"><b>Table 3A: Sectoral Consumption</b></th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th align="center" valign="middle" style="background-color: #92D050;"><b>1</b></th>
				<th align="center" valign="middle" style="background-color: #92D050;"><b>2</b></th>
				<th align="center" valign="middle" style="background-color: #92D050;"><b>3</b></th>
				<th align="center" valign="middle" style="background-color: #92D050;"><b>4</b></th>
				<th align="center" valign="middle" style="background-color: #92D050;"><b>5</b></th>
				<th align="center" valign="middle" style="background-color: #92D050;"><b>6</b></th>
				<th align="center" valign="middle" style="background-color: #92D050;"><b>7</b></th>
				<th align="center" valign="middle" style="background-color: #92D050;"><b>8</b></th>
			</tr>
			<tr>
				<th align="center" valign="middle" style="background-color: yellow;">Month & Year</th>
				<th align="center" valign="middle" style="background-color: yellow;">Supplying Company Code</th>
				<th align="center" valign="middle" style="background-color: yellow;">Product Code</th>
				<th align="center" valign="middle" style="background-color: yellow;">Sector Code</th>
				<th align="center" valign="middle" style="background-color: yellow;">Receiving Company Code</th>
				<th align="center" valign="middle" style="background-color: yellow;">State Code</th>
				<th align="center" valign="middle" style="background-color: yellow;">Quantity Supplied<br>(MMSCM)</th>
				<th align="center" valign="middle" style="background-color: yellow;">Average Calorific Value<br>(Kcal/SCM)</th>
			</tr>
		</thead>
		<tbody>
			<%if(VMONTH_YEAR.size()>0){%>
				<%for(int i=0;i<VMONTH_YEAR.size();i++){%>
					<tr>
						<td align="center"><%=VMONTH_YEAR.elementAt(i)%></td>
						<td align="center"><%=VSUPP_COMP_CD.elementAt(i)%></td>
						<td align="center"><%=VPRODUCT_CD.elementAt(i)%></td>
						<td align="center"><%=VSECTOR_CD.elementAt(i)%></td>
						<td align="center"><%=VRECEV_COMP_CD.elementAt(i)%></td>
						<td align="center"><%=VSTATE_CD.elementAt(i)%></td>
						<td align="right"><%=VQTY_MMSCM.elementAt(i)%></td>
						<td align="right"><%=VCALORIFIC_VAL.elementAt(i)%></td>
					</tr>
				<%} %>
			<%}else{%>
				<tr>
					<td colspan="9" align="center"><%=utilmsg.infoMessage("<b>No PPAC Demand Data Found!</b>") %></td>
				</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>