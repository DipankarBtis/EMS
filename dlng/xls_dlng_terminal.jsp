<%@page import="java.text.SimpleDateFormat"%>
<%@page import= "java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title></title>
<script type="text/javascript">
</script>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="page"/>
<%
String sysdate=utildate.getSysdate();
String firstDate="01/"+sysdate.substring(3, sysdate.length());

String fill_station=request.getParameter("fill_station")==null?"0":request.getParameter("fill_station");
String gas_dt=request.getParameter("gas_dt")==null?sysdate:request.getParameter("gas_dt");
//String from_dt=request.getParameter("from_dt")==null?firstDate:request.getParameter("from_dt");
//String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
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
dlng.setCallFlag("DLNG_TERMINAL_REPORT");
dlng.setComp_cd(owner_cd);
dlng.setFill_station(fill_station);
dlng.setGas_dt(gas_dt);
dlng.init();

Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();

Vector VGAS_DT = dlng.getVGAS_DT();
Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_PLANT_SEQ = dlng.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_NM = dlng.getVCOUNTERPARTY_PLANT_NM();
Vector VCOUNTERPARTY_PLANT_ABBR = dlng.getVCOUNTERPARTY_PLANT_ABBR();
Vector VNOM_REV_NO = dlng.getVNOM_REV_NO();
Vector VQTY_MMBTU = dlng.getVQTY_MMBTU();
Vector VQTY_MT = dlng.getVQTY_MT();
Vector VTRUCK_TRANS_CD = dlng.getVTRUCK_TRANS_CD();
Vector VTRUCK_CD = dlng.getVTRUCK_CD();
Vector VSLOT_START_TIME = dlng.getVSLOT_START_TIME();
Vector VARRIVAL_DT = dlng.getVARRIVAL_DT();
Vector VREMARK = dlng.getVREMARK();
Vector VDRIVER_NM = dlng.getVDRIVER_NM();
Vector VCHECKPOST_NM = dlng.getVCHECKPOST_NM();
Vector VTRANSPORTER_ABBR = dlng.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_NM = dlng.getVTRANSPORTER_NM();
Vector VTRUCK_REG_NUM = dlng.getVTRUCK_REG_NUM();
Vector VINDEX = dlng.getVINDEX();
Vector VBU_PLANT_SEQ = dlng.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = dlng.getVBU_PLANT_ABBR();
Vector VFILLING_ST_CD = dlng.getVFILLING_ST_CD();
Vector VFILLING_ST_ABBR = dlng.getVFILLING_ST_ABBR();
Vector VSLOT_DLT = dlng.getVSLOT_DLT();
Vector VBAY_CD = dlng.getVBAY_CD();
Vector VBAY_NM = dlng.getVBAY_NM();

Vector VDTL_NOM_REV_NO = dlng.getVDTL_NOM_REV_NO();
Vector VDTL_QTY_MMBTU = dlng.getVDTL_QTY_MMBTU();
Vector VDTL_QTY_MT = dlng.getVDTL_QTY_MT();
Vector VDTL_TRUCK_TRANS_CD = dlng.getVDTL_TRUCK_TRANS_CD();
Vector VDTL_TRUCK_CD = dlng.getVDTL_TRUCK_CD();
Vector VDTL_SLOT_START_TIME = dlng.getVDTL_SLOT_START_TIME();
Vector VDTL_ARRIVAL_DT = dlng.getVDTL_ARRIVAL_DT();
Vector VDTL_REMARK = dlng.getVDTL_REMARK();
Vector VDTL_DRIVER_NM = dlng.getVDTL_DRIVER_NM();
Vector VDTL_CHECKPOST_NM = dlng.getVDTL_CHECKPOST_NM();
Vector VDTL_TRANSPORTER_ABBR =dlng.getVDTL_TRANSPORTER_ABBR();
Vector VDTL_TRANSPORTER_NM =dlng.getVDTL_TRANSPORTER_NM();
Vector VDTL_TRUCK_REG_NUM =dlng.getVDTL_TRUCK_REG_NUM();
Vector VDTL_FILLING_ST_ABBR =dlng.getVDTL_FILLING_ST_ABBR();
Vector VDTL_BAY_NM =dlng.getVDTL_BAY_NM();
Vector VDTL_SLOT_DLT =dlng.getVDTL_SLOT_DLT();
%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition", "inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="16" rowspan="2" align="left">Terminal Report (<%=gas_dt%>)</th>
		</tr>
	</table>
	<table  width="100%" border="1">
		<thead>
			<tr>
				<th rowspan="2">Sr No</th>
				<th rowspan="2">Gas Date</th>
				<th rowspan="2">Customer Name</th>
				<th rowspan="2">Business Unit</th>
				<th rowspan="2">Customer Plant</th>
				<th rowspan="2" title="Seller Nomination">Rev No</th>
				<th colspan="2">Nomination Qty</th>
				<th rowspan="2">Transporter Name</th>
				<th rowspan="2">Truck Reg No.</th>
				<th rowspan="2">Driver Name</th>
				<th rowspan="2">Filling Station Association</th>
				<th colspan="2">Scheduled Slot</th>
				<th rowspan="2">Check post</th>
				<th rowspan="2">Remarks</th>
			</tr>
			<tr>
				<th>MMBTU</th>
				<th>MT</th>
				<th>Date<br>(DD/MM/YYYY)</th>
				<th>Time<br>(HH:MM)</th>
			</tr>
		</thead>
		<tbody id="mainTbody">
		<%int j=0;int k=0;
			if(VGAS_DT.size()>0)
			{
				for (int i=0;i<VGAS_DT.size();i++)
				{
						int size = Integer.parseInt(""+VINDEX.elementAt(i));
					%>
					<tr class = "">
						<td align="center"><%=i+1%></td>
						<td align="center"><%=VGAS_DT.elementAt(i)%></td>
						<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
						<td align="center"><%=VBU_PLANT_ABBR.elementAt(i) %></td>
						<td align="center"><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(i) %></td>
						<td align="center"><%=VNOM_REV_NO.elementAt(i)%></td>
						<td align="right"><%=VQTY_MMBTU.elementAt(i)%></td>
						<td align="right"><%=VQTY_MT.elementAt(i)%></td>
						<td align="center" title=""><%=VTRANSPORTER_NM.elementAt(i)%></td>
						<td align="center"><%=VTRUCK_REG_NUM.elementAt(i)%></td>
						<td align="center"><%=VDRIVER_NM.elementAt(i)%></td>
						<td align="center">
							<%=VFILLING_ST_ABBR.elementAt(i)%> [<%=VBAY_NM.elementAt(i)%>]<br><%=VSLOT_DLT.elementAt(i)%>
						</td>
						<td align="center"><%=VARRIVAL_DT.elementAt(i)%></td>
						<td align="center"><%=VSLOT_START_TIME.elementAt(i)%></td>
						<td align="left"><%=VCHECKPOST_NM.elementAt(i)%></td>
						<td align="center"><%=VREMARK.elementAt(i)%></td>
					</tr>
					<%-- <%if(size>0)
					{k=0;%>
						<tbody id="tbody<%=i%>">
							<tr style="text-align:center;font-weight:bold;background:#bce6ff;color:#0c63e4;">
								<td colspan="4" rowspan="<%=size+1%>" style="background:white;"></td>
							</tr>
							<%for(j=j; j<= VDTL_NOM_REV_NO.size(); j++)
							{
								k+=1;
							%>
							<tr>
								<td colspan="5" style="background:white;"></td>
								<td style="text-align:center"><%=VDTL_NOM_REV_NO.elementAt(j) %></td>
								<td align="right"><%=VDTL_QTY_MMBTU.elementAt(j)%></td>
								<td align="right"><%=VDTL_QTY_MT.elementAt(j)%></td>
								<td  align="center" title="<%=VDTL_TRANSPORTER_NM.elementAt(j)%>"><%=VDTL_TRANSPORTER_ABBR.elementAt(j)%></td>
								<td align="center"><%=VDTL_TRUCK_REG_NUM.elementAt(j)%></td>
								<td align="center"><%=VDTL_DRIVER_NM.elementAt(j)%></td>
								<td align="center"><%=VDTL_FILLING_ST_ABBR.elementAt(j)%> [<%=VDTL_BAY_NM.elementAt(j)%>]<br><%=VDTL_SLOT_DLT.elementAt(j)%></td>
								<td align="center"><%=VDTL_ARRIVAL_DT.elementAt(j)%></td>
								<td align="center"><%=VDTL_SLOT_START_TIME.elementAt(j)%></td>
								<td align="left"><%=VDTL_CHECKPOST_NM.elementAt(j)%></td>
								<td align="center"><%=VDTL_REMARK.elementAt(j)%></td>
							</tr>	
							<%if(k==size)
								{
									j=j+1;
									break;
								}
							}%>
						</tbody>
					<%} %> --%>
				<%} %>
			<%}else{ %>
			<tr>
				<td colspan="16" align="center"><%=utilmsg.infoMessage("<b>No Data Available!</b>")%></td>
			</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>