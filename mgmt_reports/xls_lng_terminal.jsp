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
govt_rpt.setCallFlag("LNG_TERMINAL_RPT");
govt_rpt.setComp_cd(owner_cd);
govt_rpt.setMonth(month);
govt_rpt.setYear(year);
govt_rpt.setMonth_to(month_to);
govt_rpt.setYear_to(year_to);
govt_rpt.init();

Vector VMONTH_YEAR = govt_rpt.getVMONTH_YEAR();
Vector VTERMINAL_CD = govt_rpt.getVTERMINAL_CD();
Vector VTERMINAL_CAP = govt_rpt.getVTERMINAL_CAP();
Vector VCUM_CAP_UTILIZE_PER = govt_rpt.getVCUM_CAP_UTILIZE_PER();
Vector VNO_TANKS = govt_rpt.getVNO_TANKS();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%">
		<tr>
			<th colspan="5" rowspan="" align="center" style="background-color: #70AD47;"><b>Table-4A LNG Terminal</b></th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th align="center" valign="middle" style="background-color: #70AD47;"><b>1</b></th>
				<th align="center" valign="middle" style="background-color: #70AD47;"><b>2</b></th>
				<th align="center" valign="middle" style="background-color: #70AD47;"><b>3</b></th>
				<th align="center" valign="middle" style="background-color: #70AD47;"><b>4</b></th>
				<th align="center" valign="middle" style="background-color: #70AD47;"><b>5</b></th>
			</tr>
			<tr>
				<th align="center" style="background-color: #FFFF00;">Month/Year</th>
				<th align="center" style="background-color: #FFFF00;">Terminal Code</th>
				<th align="center" style="background-color: #FFFF00;">Terminal Capacity (MMTPA)</th>
				<th align="center" style="background-color: #FFFF00;">Capacity Utilization* (%)</th>
				<th align="center" style="background-color: #FFFF00;">No. of Tanks</th>
			</tr>
		</thead>
		<tbody>
			<%if(VMONTH_YEAR.size()>0){%>
				<%for(int i=0;i<VMONTH_YEAR.size();i++){%>
					<tr>
						<td align="center"><%=VMONTH_YEAR.elementAt(i) %></td>
						<td align="center"><%=VTERMINAL_CD.elementAt(i) %></td>
						<td align="right"><%=VTERMINAL_CAP.elementAt(i) %></td>
						<td align="right"><%=VCUM_CAP_UTILIZE_PER.elementAt(i) %></td>
						<td align="center"><%=VNO_TANKS.elementAt(i) %></td>
					</tr>
				<%} %>
			<%}else{ %>
				<tr>
					<td colspan="6" align="center"><%=utilmsg.infoMessage("<b>No LNG Terminal Data Found!</b>") %></td>
				</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>