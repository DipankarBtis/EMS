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
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String nextdate = utildate.getNextDate();

String from_dt = request.getParameter("from_dt")==null?nextdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?nextdate:request.getParameter("to_dt");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String bu_seq=request.getParameter("bu_seq")==null?"0":request.getParameter("bu_seq");	// SagarB20250922 Added this variable for showing BU
String bu_abbr=request.getParameter("bu_abbr")==null?"":request.getParameter("bu_abbr");	// SagarB20250922 Added this variable for showing BU
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

dlng.setCallFlag("DLNG_ALLOC_TO_CUST");
dlng.setComp_cd(owner_cd);
dlng.setFrom_dt(from_dt);
dlng.setTo_dt(to_dt);
dlng.setCounterparty_cd(counterparty_cd);
dlng.setBu_seq(bu_seq);		// SagarB20250922 Added this variable for showing BU list
dlng.init();

Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
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
Vector VCONT_NAME = dlng.getVCONT_NAME();
Vector VCONT_REF = dlng.getVCONT_REF();

Vector VCONT_NO = dlng.getVCONT_NO();
Vector VCONT_REV_NO = dlng.getVCONT_REV_NO();
Vector VAGMT_NO = dlng.getVAGMT_NO();
Vector VAGMT_REV_NO = dlng.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = dlng.getVCONTRACT_TYPE();
Vector VDIS_CONT_MAPPING = dlng.getVDIS_CONT_MAPPING();
Vector VSTART_DT = dlng.getVSTART_DT();
Vector VEND_DT = dlng.getVEND_DT();

Vector VINDEX = dlng.getVINDEX();
Vector VSUB_INDEX = dlng.getVSUB_INDEX();
Vector VGAS_DT = dlng.getVGAS_DT();

Vector VQTY_ALLOC_MMBTU = dlng.getVQTY_ALLOC_MMBTU();
Vector VQTY_ALLOC_MT = dlng.getVQTY_ALLOC_MT();
Vector VAGMT_BASE = dlng.getVAGMT_BASE();
Vector VCONTRACT_TYPE_NM = dlng.getVCONTRACT_TYPE_NM();

String total_alloc_mt = dlng.getTotal_alloc_mt();
String total_alloc_mmbtu = dlng.getTotal_alloc_mmbtu();

// SagarB20250929 Changed below file name since it contradicted with DLNG
// String file_nm="Allocation_to_customer_"+to_dt.replaceAll("/", "")+".xls";
String file_nm="Dlng_Allocation_to_customer_"+to_dt.replaceAll("/", "")+".xls";
%>
<body>
	<%
	response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "inline; filename="+file_nm);  
	%>	
	<font size="4"><b>DLNG Allocation To Customer From <%=from_dt%> To <%=to_dt%></b></font>
	<br>	
	
	<!-- SagarB20250929 Added this variable for showing BU Abbr in exported excel -->
	<%if(!bu_seq.equals("0") && (!bu_seq.equals(""))){ %>
	<font size="3"><b>Business Unit : <%=bu_abbr%></b></font>
	<%} %>
	
	<br>					  				
	<%if(VCOUNTERPARTY_CD.size()>0)
	{ %>	
 		<table border="1">
			<thead>
				<tr>
					<th rowspan="2">Gas Day</th>																	
					<th rowspan="2">Customer</th>
					<th rowspan="2">Customer Plant</th>
					<th rowspan="2">BU</th>
					<th rowspan="2">Contract Type</th>
					<th rowspan="2">Contract#</th>
					<th rowspan="2">Contract/Trade Ref#</th>
					<th rowspan="2">Contract Period</th>
					<th colspan="2">Truck Loading</th>																
				</tr>
				<tr>
					<th>MMBTU</th>
					<th>MT</th>
				</tr>
			</thead>
			<tbody>
				<%for(int l=0; l<VCOUNTERPARTY_CD.size(); l++)
				{ 
				%>
					<tr>
						<td align="center"><%=VGAS_DT.elementAt(l) %></td>
						<td align="center"><%=VCOUNTERPARTY_NM.elementAt(l)%></td>
						<td align="center"><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%></td>
						<td align="center"><%=VBU_PLANT_ABBR.elementAt(l)%></td>
						<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(l)%></td>
						<td><%=VDIS_CONT_MAPPING.elementAt(l)%></td>
						<td><%=VCONT_REF.elementAt(l)%></td>
						<td align="center"><%=VSTART_DT.elementAt(l) %>-<%=VEND_DT.elementAt(l) %></td>																			
						<td align="right"><%=VQTY_ALLOC_MMBTU.elementAt(l) %></td>
						<td align="right"><%=VQTY_ALLOC_MT.elementAt(l) %></td>
					</tr>
				<%} %>
				<tr>
					<td colspan="8"><div align="right"><b>Total:</b></div></td>
					<td align="right"><b><%=total_alloc_mmbtu %></b></td>
					<td align="right"><b><%=total_alloc_mt %></b></td>
				</tr>
			</tbody>
		</table>
	<%}else{ %>
		<%=utilmsg.infoMessage("<b>No Allocation Done for Selected Gas Day!</b>") %>
	<%} %>				

</body>
</html>