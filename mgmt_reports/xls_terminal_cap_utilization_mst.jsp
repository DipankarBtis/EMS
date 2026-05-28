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

govt_rpt.setCallFlag("TERMINAL_CAPACITY_UTILIZATION_MST");
govt_rpt.setComp_cd(owner_cd);
govt_rpt.setFinancial_year(fin_year);
govt_rpt.init();

Vector VMONTH_YEAR = govt_rpt.getVMONTH_YEAR();
Vector VRLNG_SEND_OUT_MMBTU = govt_rpt.getVRLNG_SEND_OUT_MMBTU();
Vector VDLNG_SEND_OUT_MMBTU = govt_rpt.getVDLNG_SEND_OUT_MMBTU();
Vector VTOTAL_SEND_OUT_MMBTU = govt_rpt.getVTOTAL_SEND_OUT_MMBTU();
Vector VMONTH_DAYS = govt_rpt.getVMONTH_DAYS();
Vector VTOTAL_SEND_OUT_MMSCM = govt_rpt.getVTOTAL_SEND_OUT_MMSCM();
Vector VTOTAL_SEND_OUT_MMSCMD = govt_rpt.getVTOTAL_SEND_OUT_MMSCMD();
Vector VNAMEPLATE_CAP = govt_rpt.getVNAMEPLATE_CAP();
Vector VMMSCMD_BY_NP = govt_rpt.getVMMSCMD_BY_NP();
Vector VMMTPA_CAP = govt_rpt.getVMMTPA_CAP();
Vector VNAMEPLATE_CAP_PER_MTH = govt_rpt.getVNAMEPLATE_CAP_PER_MTH();
Vector VMMTPA_CAP_PER_MTH = govt_rpt.getVMMTPA_CAP_PER_MTH();
Vector VCAP_UTILIZE_PER = govt_rpt.getVCAP_UTILIZE_PER();
Vector VCUM_CAP_UTILIZE_PER = govt_rpt.getVCUM_CAP_UTILIZE_PER();
Vector VMST_FINANCIAL_YEAR = govt_rpt.getVMST_FINANCIAL_YEAR();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="15" rowspan="" align="center"><b>Terminal Capacity Utilization Master Report</b></th>
		</tr>
	</table>
	<table class="table table-bordered" id="example" border="1">
		<thead id="tbsearch">
			<tr>
				<th align="center">Sr#</th>
				<th align="center">Month-Year</th>
				<th align="center">RLNG Send Out in GAIL-GSPL-PIL HAZ (MMBTU)</th>
				<th align="center">DLNG Volume (Own+TP) (MMBTU)</th>
				<th align="center">Total Send out from<br>Terminal in MMBTU</th>
				<th align="center">No. of Days in month</th>
				<th align="center">Terminal Send Out (MMSCM)</th>
				<th align="center">Terminal Send Out (MMSCMD)</th>
				<th align="center">Nameplate Capacity (MMTPA)</th>
				<th align="center">MMSCMD/Nameplate Capacity (MMTPA)</th>
				<th align="center">MMTPA Capacity</th>
				<th align="center">Nameplate Capacity per month (MMTPA)</th>
				<th align="center">MMTPA Capacity per month</th>
				<th align="center">Capacity Utilization in % for the month</th>
				<th align="center">Cumulative Capacity utilization in %<br>Year to Month</th>
			</tr>
		</thead>
		<tbody>
			<%if(VMONTH_YEAR.size()>0){%>
				<%for(int i=0;i<VMONTH_YEAR.size();i++){%>
					<tr>
						<td align="center"><%=i+1 %></td>
						<td align="center"><%=VMONTH_YEAR.elementAt(i) %></td>
						<td align="right"><%=VRLNG_SEND_OUT_MMBTU.elementAt(i) %></td>
						<td align="right"><%=VDLNG_SEND_OUT_MMBTU.elementAt(i) %></td>
						<td align="right"><%=VTOTAL_SEND_OUT_MMBTU.elementAt(i) %></td>
						<td align="center"><%=VMONTH_DAYS.elementAt(i)%></td>
						<td align="right"><%=VTOTAL_SEND_OUT_MMSCM.elementAt(i) %></td>
						<td align="right"><%=VTOTAL_SEND_OUT_MMSCMD.elementAt(i) %></td>
						<td align="right"><%=VNAMEPLATE_CAP.elementAt(i) %></td>
						<td align="right"><%=VMMSCMD_BY_NP.elementAt(i) %></td>
						<td align="right"><%=VMMTPA_CAP.elementAt(i) %></td>
						<td align="right"><%=VNAMEPLATE_CAP_PER_MTH.elementAt(i) %></td>
						<td align="right"><%=VMMTPA_CAP_PER_MTH.elementAt(i) %></td>
						<td align="right"><%=VCAP_UTILIZE_PER.elementAt(i) %></td>
						<td align="right"><%=VCUM_CAP_UTILIZE_PER.elementAt(i) %></td>
					</tr>
				<%} %>
			<%} else{%>
				<tr>
					<td colspan="15" align="center"><%=utilmsg.infoMessage("<b>No Terminal Utilization Data Found!</b>") %></td>
				</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>
