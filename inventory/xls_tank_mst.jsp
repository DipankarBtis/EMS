<%@page import= "java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Harsh Maheta 20230807 : Form for Bank Master-->
<html>
</head>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_TankTerminal" id="dbterminal" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
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

String sysDt=utildate.getSysdate();

dbterminal.setCallFlag("TANK_MST");
dbterminal.setComp_cd(owner_cd);
dbterminal.init();

Vector VTANK_CD = dbterminal.getVTANK_CD();
Vector VTANK_NAME = dbterminal.getVTANK_NAME();
Vector VEFF_DT = dbterminal.getVEFF_DT();
Vector VSTATUS = dbterminal.getVSTATUS();
Vector VTANK_T1_VOLUME = dbterminal.getVTANK_T1_VOLUME();
Vector VTANK_T1_HEIGHT = dbterminal.getVTANK_T1_HEIGHT();
Vector VTANK_T2_VOLUME = dbterminal.getVTANK_T2_VOLUME();
Vector VTANK_T2_HEIGHT = dbterminal.getVTANK_T2_HEIGHT();
Vector VTANK_D1_VOLUME = dbterminal.getVTANK_D1_VOLUME();
Vector VTANK_D1_HEIGHT = dbterminal.getVTANK_D1_HEIGHT();
Vector VTANK_D2_VOLUME = dbterminal.getVTANK_D2_VOLUME();
Vector VTANK_D2_HEIGHT = dbterminal.getVTANK_D2_HEIGHT();
Vector VTANK_DIAMETER = dbterminal.getVTANK_DIAMETER();
Vector VTANK_PI_TAG = dbterminal.getVTANK_PI_TAG();

Vector VICD_EFF_DT = dbterminal.getVICD_EFF_DT();
Vector VICD_PERCENTAGE = dbterminal.getVICD_PERCENTAGE();
Vector VICD_REMARK = dbterminal.getVICD_REMARK();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="13" rowspan="2" align="left">Tank Master</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th rowspan="2">Sr.</th>
				<th rowspan="2">Tank Name</th>
				<th rowspan="2">Pi Tag</th>
				<th rowspan="2">Eff. Date</th>
				<th rowspan="2">Diameter</th>
				<th rowspan="2">Status</th>
		    	<th colspan="2">Tank Top Level-1 (T1)</th>
		    	<th colspan="2">Tank Top Level-2 (T2)</th>
		    	<th colspan="2">Dead Stock Level-1 (D1)</th>
		    	<th colspan="2">Dead Stock Level-2 (D2)</th>
			</tr>
			<tr>
				<th>Height (Millimetre)</th>
				<th>Volume (M3 of LNG)</th>
				<!-- <th>Diameter (Millimetre)</th> -->
				<th>Height (Millimetre)</th>
				<th>Volume (M3 of LNG)</th>
				<!-- <th>Diameter (Millimetre)</th> -->
				<th>Height (Millimetre)</th>
				<th>Volume (M3 of LNG)</th>
				<!-- <th>Diameter (Millimetre)</th> -->
				<th>Height (Millimetre)</th>
				<th>Volume (M3 of LNG)</th>
				<!-- <th>Diameter (Millimetre)</th> -->
			</tr>
		</thead>
		<tbody id="mainTbody">
		<%int j=0;int k=0;
		if(VTANK_CD.size()>0){%>
			<%for(int i=0; i<VTANK_CD.size(); i++){ 
			%>
				<tr>
					<td align="center"><%=i+1%></td>
					<td align="center"><%=VTANK_NAME.elementAt(i)%></td>
					<td align="center"><%=VTANK_PI_TAG.elementAt(i)%></td>
					<td align="center"><%=VEFF_DT.elementAt(i)%></td>
					<td align="center"><%=VTANK_DIAMETER.elementAt(i)%></td>
					<td align="center">
						<div align="center">
							<%if(VSTATUS.elementAt(i).equals("Y")){%>
							Active
							<%}else{ %>
							Cancelled
							<%} %>
						</div>
					</td>
					<td align="center"><%=VTANK_T1_HEIGHT.elementAt(i)%></td>
					<td align="center"><%=VTANK_T1_VOLUME.elementAt(i)%></td>
					<td align="center"><%=VTANK_T2_HEIGHT.elementAt(i)%></td>
					<td align="center"><%=VTANK_T2_VOLUME.elementAt(i)%></td>
					<td align="center"><%=VTANK_D1_HEIGHT.elementAt(i)%></td>
					<td align="center"><%=VTANK_D1_VOLUME.elementAt(i)%></td>
					<td align="center"><%=VTANK_D2_HEIGHT.elementAt(i)%></td>
					<td align="center"><%=VTANK_D2_VOLUME.elementAt(i)%></td>
				</tr>
			<%} %>
		<%}else{ %>
			<tr>
				<td colspan="13" align="center"><%=utilmsg.infoMessage("<b>No Tank Data Available!</b>") %></td>
			</tr>
		<%} %>
		</tbody>
	</table>
	<table  width="100%" border="1">
		<tr>
			<th colspan="13" rowspan="2" align=""></th>
		</tr>
	</table>
	<table  width="100%" border="1">
		<tr>
			<th colspan=3 rowspan="2" align="left">Internal Consumption Details</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<tbody id="mainTbody">
			<tr>
				<td align="center">Effective Date</td>
				<td align="center">Internal Consumption Percentage(%)</td>
				<td align="center">Remark</td>
			</tr>
			<%//int j=0;int k=0;
				if(VICD_EFF_DT.size()>0){%>
					<%for(int i=0; i<VICD_EFF_DT.size(); i++){ 
					%>
						<tr>
							<td align="center"><%=VICD_EFF_DT.elementAt(i)%></td>
							<td align="center"><%=VICD_PERCENTAGE.elementAt(i)%></td>
							<td align="center"><%=VICD_REMARK.elementAt(i)%></td>
						</tr>
					<%} %>
				<%}else{ %>
					<tr>
						<td colspan="3" align="center"><b>No Internal Consumption details Data Available!</b></td>
					</tr>
				<%} %>
		</tbody>
	</table>
</body>
</html>