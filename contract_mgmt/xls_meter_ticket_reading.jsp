<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import= "java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String plant_seq=request.getParameter("plant_seq")==null?"0":request.getParameter("plant_seq");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String meter_seq=request.getParameter("meter_seq")==null?"0":request.getParameter("meter_seq");
String meter_nm=request.getParameter("meter_nm")==null?"":request.getParameter("meter_nm");
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

cont_mgmt.setCallFlag("REPORT_METER_TICKET_READING");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setFrom_dt(from_dt);
cont_mgmt.setTo_dt(to_dt);
cont_mgmt.setCounterparty_cd(counterparty_cd);
cont_mgmt.setPlant_seq(plant_seq);
cont_mgmt.setMeter_seq(meter_seq);
cont_mgmt.init();

Vector VMST_COUNTERPARTY_CD = cont_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = cont_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = cont_mgmt.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_PLANT_SEQ = cont_mgmt.getVMST_COUNTERPARTY_PLANT_SEQ();
Vector VMST_COUNTERPARTY_PLANT_ABBR = cont_mgmt.getVMST_COUNTERPARTY_PLANT_ABBR();
Vector VSUB_METER_SEQ = cont_mgmt.getVSUB_METER_SEQ();
Vector VSUB_METER_NM = cont_mgmt.getVSUB_METER_NM();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = cont_mgmt.getVCOUNTERPARTY_PLANT_ABBR();

Vector VINDEX = cont_mgmt.getVINDEX();
Vector VSUB_INDEX = cont_mgmt.getVSUB_INDEX();

Vector VGAS_DT = cont_mgmt.getVGAS_DT();
Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();
Vector VQTY_SCM = cont_mgmt.getVQTY_SCM();
Vector VQTY_BTU = cont_mgmt.getVQTY_BTU();
Vector VRECONCIL_QTY_MMBTU = cont_mgmt.getVRECONCIL_QTY_MMBTU();
Vector VRECONCIL_QTY_SCM = cont_mgmt.getVRECONCIL_QTY_SCM();
Vector VRECONCIL_QTY_BTU = cont_mgmt.getVRECONCIL_QTY_BTU();
Vector VTOTAL_QTY_MMBTU = cont_mgmt.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_SCM = cont_mgmt.getVTOTAL_QTY_SCM();
Vector VCALC_GCV = cont_mgmt.getVCALC_GCV();
Vector VCALC_NCV = cont_mgmt.getVCALC_NCV();
Vector VDEFINE_GCV = cont_mgmt.getVDEFINE_GCV();
Vector VDEFINE_NCV = cont_mgmt.getVDEFINE_NCV();

Vector VCOLOR = cont_mgmt.getVCOLOR();

Vector VTOT_QTY_MMBTU = cont_mgmt.getVTOT_QTY_MMBTU();
Vector VTOT_QTY_SCM = cont_mgmt.getVTOT_QTY_SCM();
Vector VTOT_QTY_BTU = cont_mgmt.getVTOT_QTY_BTU();
Vector VTOT_RECONCIL_QTY_MMBTU = cont_mgmt.getVTOT_RECONCIL_QTY_MMBTU();
Vector VTOT_RECONCIL_QTY_SCM = cont_mgmt.getVTOT_RECONCIL_QTY_SCM();
Vector VTOT_RECONCIL_QTY_BTU = cont_mgmt.getVTOT_RECONCIL_QTY_BTU();
Vector VTOT_TOTAL_QTY_MMBTU = cont_mgmt.getVTOT_TOTAL_QTY_MMBTU();
Vector VTOT_TOTAL_QTY_SCM = cont_mgmt.getVTOT_TOTAL_QTY_SCM();
Vector VTOT_CALC_GCV = cont_mgmt.getVTOT_CALC_GCV();
Vector VTOT_CALC_NCV = cont_mgmt.getVTOT_CALC_NCV();
Vector VTOT_DEFINE_GCV = cont_mgmt.getVTOT_DEFINE_GCV();
Vector VTOT_DEFINE_NCV = cont_mgmt.getVTOT_DEFINE_NCV();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition", "inline; filename="+fileName);
%>

<table  width="100%" border="1">
	<tr>
		<th nowrap="nowrap" style="font-size: 21" colspan="11" rowspan="" align="left">Meter Ticket Reading (Generated For <%=from_dt%> To <%=to_dt%>)</th>
	</tr>
</table>
<%int j=0,k=0,l=0,m=0;
	for(int i=0; i<VCOUNTERPARTY_CD.size(); i++)
	{ 
		String countpty_cd=""+VCOUNTERPARTY_CD.elementAt(i);
		int index=Integer.parseInt(""+VINDEX.elementAt(i));
	%>
		<%k=0;
		if(index > 0){ %>
			<%for(j=j;j<VCOUNTERPARTY_PLANT_SEQ.size(); j++) 
			{
				String countpty_plant_seq=""+VCOUNTERPARTY_PLANT_SEQ.elementAt(j);
				int sub_index = Integer.parseInt(""+VSUB_INDEX.elementAt(j));
				k+=1;
			%>
&nbsp;
<table width="100%" border="1">
	<tr>
	  	<td colspan="11" rowspan=""><b><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(j)%>&nbsp;&nbsp;<%if(!meter_nm.equals("")){%><font color="blue">[Meter&nbsp;:&nbsp;<%=meter_nm%>]</font></b></td><%} %>				
	</tr>
</table>
<table width="100%" border="1">		
		<thead>
			<tr>
				<th rowspan="2">Gas Day</th>
				<th colspan="2">Meter Qty</th>
				<th colspan="2">Reconciliation Qty</th>
				<th colspan="2">Total Qty</th>
				<th colspan="2">Calculated Heat Value (KCal/SCM)</th>
				<th colspan="2">Define Heat Value (KCal/SCM)</th>
			</tr>
			<tr>
				<th>MMBTU</th>
				<th>SCM</th>
				<th>MMBTU</th>
				<th>SCM</th>
				<th>MMBTU</th>
				<th>SCM</th>
				<th>GCV</th>
				<th>NCV</th>
				<th>GCV</th>
				<th>NCV</th>
			</tr>
		</thead>
		<tbody>
			<%m=0;
		if(sub_index>0){ %>
			<%for(l=l; l<VQTY_MMBTU.size(); l++)
			{ 
				m+=1;
			%>
				<tr>
					<td align="center"><%=VGAS_DT.elementAt(l)%></td>
					<td align="right"><%=VQTY_MMBTU.elementAt(l)%></td>
					<td align="right"><%=VQTY_SCM.elementAt(l)%></td>
					<td align="right"><%=VRECONCIL_QTY_MMBTU.elementAt(l)%></td>
					<td align="right"><%=VRECONCIL_QTY_SCM.elementAt(l)%></td>
					<td align="right"><%=VTOTAL_QTY_MMBTU.elementAt(l)%></td>
					<td align="right"><%=VTOTAL_QTY_SCM.elementAt(l)%></td>
					<td align="right"><%=VCALC_GCV.elementAt(l)%></td>
					<td align="right"><%=VCALC_NCV.elementAt(l)%></td>
					<td align="right"><%=VDEFINE_GCV.elementAt(l)%></td>
					<td align="right"><%=VDEFINE_NCV.elementAt(l)%></td>
				</tr>
				<%if(m==sub_index)
				{%>
					<tr>
						<td align="right"><b>Total (<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(j)%>)</b></td>
						<td align="right"><b><%=VTOT_QTY_MMBTU.elementAt(j)%></b></td>
						<td align="right"><b><%=VTOT_QTY_SCM.elementAt(j)%></b></td>
						<td align="right"><b><%=VTOT_RECONCIL_QTY_MMBTU.elementAt(j)%></b></td>
						<td align="right"><b><%=VTOT_RECONCIL_QTY_SCM.elementAt(j)%></b></td>
						<td align="right"><b><%=VTOT_TOTAL_QTY_MMBTU.elementAt(j)%></b></td>
						<td align="right"><b><%=VTOT_TOTAL_QTY_SCM.elementAt(j)%></b></td>
						<td align="right"><b><%=VTOT_CALC_GCV.elementAt(j)%></b></td>
						<td align="right"><b><%=VTOT_CALC_NCV.elementAt(j)%></b></td>
						<td align="right"><b><%=VTOT_DEFINE_GCV.elementAt(j)%></b></td>
						<td align="right"><b><%=VTOT_DEFINE_NCV.elementAt(j)%></b></td>
					</tr>
					<%l=l+1;
					break;
				}%>
			<%} %> 
		<%} %>
	</tbody>
</table>
	<%if(k==index)
	{
	j=j+1;
		break;
	}%>
		<%} %>
	<%} %>
<%} %>
</body>
</html>