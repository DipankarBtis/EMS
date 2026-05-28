<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String nextdate = utildate.getNextDate();

String gas_dt = request.getParameter("gas_dt")==null?nextdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "-1");

String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

dlng.setCallFlag("DLNG_BUYER_NOM_REV_WISE");
dlng.setComp_cd(owner_cd);
dlng.setGas_dt(gas_dt);
dlng.init();

Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VTRANSPORTER_CD = dlng.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = dlng.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_TRUCK = dlng.getVTRANSPORTER_TRUCK();
Vector VTRANSPORTER_TRUCK_NO = dlng.getVTRANSPORTER_TRUCK_NO();
Vector VCOUNTERPARTY_PLANT_SEQ = dlng.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = dlng.getVCOUNTERPARTY_PLANT_ABBR();
Vector VBU_CD = dlng.getVBU_CD();
Vector VBU_PLANT_SEQ = dlng.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = dlng.getVBU_PLANT_ABBR();

Vector VNOM_REV_NO = dlng.getVNOM_REV_NO();
Vector VGEN_TIME = dlng.getVGEN_TIME();
Vector VGEN_DT = dlng.getVGEN_DT();
Vector VBASE = dlng.getVBASE();
Vector VGCV = dlng.getVGCV();
Vector VNCV = dlng.getVNCV();
Vector VQTY_MMBTU = dlng.getVQTY_MMBTU();
Vector VQTY_SCM = dlng.getVQTY_SCM();
Vector VDCQ = dlng.getVDCQ();
Vector VMDCQ_QTY = dlng.getVMDCQ_QTY();
Vector VCONT_NAME = dlng.getVCONT_NAME();
Vector VCONT_REF = dlng.getVCONT_REF();

Vector VCONT_NO = dlng.getVCONT_NO();
Vector VCONT_REV_NO = dlng.getVCONT_REV_NO();
Vector VAGMT_NO = dlng.getVAGMT_NO();
Vector VAGMT_REV_NO = dlng.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = dlng.getVCONTRACT_TYPE();
Vector VDIS_CONT_MAPPING = dlng.getVDIS_CONT_MAPPING();

Vector VINDEX = dlng.getVINDEX();
Vector VSUB_INDEX = dlng.getVSUB_INDEX();
Vector VCONTRACT_TYPE_NM = dlng.getVCONTRACT_TYPE_NM();

String file_nm="Dlng_BuyerNomination_RevisionWise_"+gas_dt.replaceAll("/", "")+".xls";
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "inline; filename="+file_nm);
    
%>
<table border="1">
<tr>
	<td align="left" colspan="13">
		<font size="4"><b>DLNG Buyer Nomination Revision Wise - Gas Day <%=gas_dt%></b></font>
	</td>
</tr>
<%if(VCOUNTERPARTY_CD.size()>0)
{ %>	
	<tr>
		<th rowspan="2">Gen Day</th>
		<th rowspan="2">Gen Time</th>
		<th rowspan="2">Rev#</th>
		<th rowspan="2">Customer</th>
		<th rowspan="2">Customer Plant</th>
		<th rowspan="2">Business Unit</th>
		<th rowspan="2">Contract#</th>
		<th rowspan="2">Contract/Trade Ref#</th>
		<th rowspan="2">DCQ</th>
		<th rowspan="2">Energy (MMBTU)</th>
		<th rowspan="2">Energy (MT)</th>
		<th colspan="2">Calorific Value Base(KCal/SCM)</th>																
	</tr>
	<tr>
		<th>GCV</th>
		<th>NCV</th>
	</tr>
	<%for(int l=0; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
	{ 
	%>
		<tr>
			<td align="center">&nbsp;<%=VGEN_DT.elementAt(l) %>&nbsp;</td>
			<td align="center"><%=VGEN_TIME.elementAt(l) %></td>
			<td align="center"><b><%=VNOM_REV_NO.elementAt(l) %></b></td>
			<td align="center"><%=VCOUNTERPARTY_NM.elementAt(l)%></td>
			<td align="center"><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%></td>
			<td align="center"><%=VBU_PLANT_ABBR.elementAt(l)%></td>
			<td align="center"><%=VDIS_CONT_MAPPING.elementAt(l)%></td>
			<td align="center">&nbsp;<%=VCONT_REF.elementAt(l)%></td>
			<td align="right"><%=VDCQ.elementAt(l) %></td>																			
			<td align="right"><%=VQTY_MMBTU.elementAt(l) %></td>
			<td align="right"><%=VQTY_SCM.elementAt(l) %></td>
			<td align="right"><%=VGCV.elementAt(l)%></td>
			<td align="right"><%=VNCV.elementAt(l)%></td>
		</tr>
	<%} %>		
<%}else{ %>
	<tr>
		<td colspan="12">
			<div align="center">
			<b>No Buyer Nomination Done for Selected Gas Day!</b>
			</div>
		</td>
	</tr>
<%} %>
</table>

</form>
</body>
</html>