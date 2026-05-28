<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>

<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_Govt_Reports" id="govt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String current_fin_year=utildate.getFinancialYear(sysdate);

String fin_year = request.getParameter("fin_year")==null?""+current_fin_year:request.getParameter("fin_year");
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

govt_rpt.setCallFlag("TERMINAL_CAPACITY_UTILIZATION");
govt_rpt.setComp_cd(owner_cd);
govt_rpt.setFinancial_year(fin_year);
govt_rpt.init();

Vector VMST_FINANCIAL_YEAR = govt_rpt.getVMST_FINANCIAL_YEAR();
Vector VLOCATION = govt_rpt.getVLOCATION();
Vector VPROMOTERS = govt_rpt.getVPROMOTERS();
Vector VMONTH_END_DT = govt_rpt.getVMONTH_END_DT();
Vector VNAMEPLATE_CAP = govt_rpt.getVNAMEPLATE_CAP();
Vector VAVG_CAP = govt_rpt.getVAVG_CAP();
Vector VAVAILABLE_CAP = govt_rpt.getVAVAILABLE_CAP();
Vector VCAP_UTILIZE_PER = govt_rpt.getVCAP_UTILIZE_PER();
Vector VCUM_CAP_UTILIZE_PER = govt_rpt.getVCUM_CAP_UTILIZE_PER();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="9" rowspan="" align="center"><b>Terminal Capacity Utilization</b></th>
		</tr>
	</table>
	<table class="table table-bordered" id="example" border="1">
		<thead id="tbsearch">
			<tr>
				<th align="center">Sr#</th>
				<th align="center">Location</th>
				<th align="center">Promoters</th>
				<th align="center">As on</th>
				<th align="center">Total Yearly Capacity (in MMTPA)</th>
				<th align="center">Average Capacity for the month<br>(Total Capacity/12 in MMTPA)</th>
				<th align="center">Available Capacity for the month<br>(in MMTPA)</th>
				<th align="center">Capacity utilization in % for the month</th>
				<th align="center">Cumulative Capacity utilization in % (Year to Month)</th>
			</tr>
		</thead>
		<tbody>
			<%if(VMONTH_END_DT.size()>0){%>
				<%for(int i=0;i<VMONTH_END_DT.size();i++){%>
					<tr>
						<td align="center"><%=(i+1) %></td>
						<td align="center"><%=VLOCATION.elementAt(i) %></td>
						<td align="center"><%=VPROMOTERS.elementAt(i) %></td>
						<td align="center"><%=VMONTH_END_DT.elementAt(i) %></td>
						<td align="right"><%=VNAMEPLATE_CAP.elementAt(i) %></td>
						<td align="right"><%=VAVG_CAP.elementAt(i) %></td>
						<td align="right"><%=VAVAILABLE_CAP.elementAt(i) %></td>
						<td align="right"><%=VCAP_UTILIZE_PER.elementAt(i) %></td>
						<td align="right"><%=VCUM_CAP_UTILIZE_PER.elementAt(i) %></td>
					</tr>
				<%} %>
			<%}else{%>
				<tr>
					<td colspan="9" align="center"><%=utilmsg.infoMessage("<b>No Terminal Utilization Data Found!</b>") %></td>
				</tr>
			<%}%>
		</tbody>
	</table>
</body>
</html>
