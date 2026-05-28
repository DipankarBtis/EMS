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

if(month.length() == 1)
{
	month="0"+month; 
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

govt_rpt.setCallFlag("SUPPLY_DEMAND_SECTOR_MASTER_RPT");
govt_rpt.setComp_cd(owner_cd);
govt_rpt.setMonth(month);
govt_rpt.setYear(year);
govt_rpt.init();

Vector VCOUNTERPARTY_CD = govt_rpt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = govt_rpt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = govt_rpt.getVCOUNTERPARTY_NM();
Vector VPLANT_NM = govt_rpt.getVPLANT_NM();
Vector VPLANT_SECTOR_NM = govt_rpt.getVPLANT_SECTOR_NM();
Vector VDISPLAY_DEAL_MAP = govt_rpt.getVDISPLAY_DEAL_MAP();
Vector VDEMAND_SECTOR_CD = govt_rpt.getVDEMAND_SECTOR_CD();
Vector VSUPPLY_SECTOR_CD = govt_rpt.getVSUPPLY_SECTOR_CD();
Vector VSTATE_CD = govt_rpt.getVSTATE_CD();
Vector VQTY_MMBTU = govt_rpt.getVQTY_MMBTU();
Vector VQTY_MMSCM = govt_rpt.getVQTY_MMSCM();
Vector VQTY_MT = govt_rpt.getVQTY_MT();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="11" rowspan="" align="center"><b>Master Report (Demand and Supply)</b></th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>Sr#</th>
				<th>Customer Name</th>
				<th>Plant Name</th>
				<th>Sector from <br> Plant Mapping</th>
				<th>Contract</th>
				<th>SectorID-DEMAND</th>
				<th>BUSectorId-SUPPLY</th>
				<th>State Code</th>
				<th>Qty MMBTU</th>
				<th>Qty MSCM</th>
				<th>Qty MT</th>
			</tr>
		</thead>
		<tbody>
			<%if(VCOUNTERPARTY_CD.size()>0){%>
				<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){%>
					<tr>
						<td align="center"><%=i+1%></td>
						<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i) %></td>
						<td align="center"><%=VPLANT_NM.elementAt(i) %></td>
						<td align="center"><%=VPLANT_SECTOR_NM.elementAt(i) %></td>
						<td>
							<div style="width:400px; word-wrap: break-word; white-space: normal;">
								<%=VDISPLAY_DEAL_MAP.elementAt(i) %>
							</div>
						</td>
						<td align="center"><%=VDEMAND_SECTOR_CD.elementAt(i) %></td>
						<td align="center"><%=VSUPPLY_SECTOR_CD.elementAt(i) %></td>
						<td align="center"><%=VSTATE_CD.elementAt(i) %></td>
						<td align="right"><%=VQTY_MMBTU.elementAt(i) %></td>
						<td align="right"><%=VQTY_MMSCM.elementAt(i) %></td>
						<td align="right"><%=VQTY_MT.elementAt(i) %></td>
					</tr>
				<%}%>
			<%}else{%>
				<tr>
					<td colspan="11" align="center"><%=utilmsg.infoMessage("<b>No Data Found!</b>") %></td>
				</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>