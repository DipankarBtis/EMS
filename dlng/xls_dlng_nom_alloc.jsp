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

// Get value after Page Refresh()
String from_dt = request.getParameter("from_dt")==null?nextdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?nextdate:request.getParameter("to_dt");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String transporter_cd=request.getParameter("transporter_cd")==null?"0":request.getParameter("transporter_cd");
String transporter_truck=request.getParameter("transporter_truck")==null?"0":request.getParameter("transporter_truck");
String chk_diff = request.getParameter("chk_diff")==null?"":request.getParameter("chk_diff");
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

//Send Value to Java
dlng.setCallFlag("DLNG_NOM_ALLOC");
dlng.setComp_cd(owner_cd);
dlng.setFrom_dt(from_dt);
dlng.setTo_dt(to_dt);
dlng.setCounterparty_cd(counterparty_cd);
dlng.setTransporter_cd(transporter_cd);
dlng.setTransporter_truck(transporter_truck);
dlng.setChk_diff(chk_diff);
dlng.setBu_seq(bu_seq);		// SagarB20250922 Added this variable for showing BU list
dlng.init();

// Get Vectors from Java
Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();

Vector VMST_TRANSPORTER_CD = dlng.getVMST_TRANSPORTER_CD();
Vector VMST_TRANSPORTER_NM = dlng.getVMST_TRANSPORTER_NM();
Vector VMST_TRANSPORTER_ABBR = dlng.getVMST_TRANSPORTER_ABBR();
Vector VMST_TRANSPORTER_TRUCK = dlng.getVMST_TRANSPORTER_TRUCK();
Vector VMST_TRANSPORTER_TRUCK_NO = dlng.getVMST_TRANSPORTER_TRUCK_NO();


Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VTRANSPORTER_CD = dlng.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = dlng.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_PLANT_SEQ = dlng.getVTRANSPORTER_TRUCK();
Vector VTRANSPORTER_PLANT_ABBR = dlng.getVTRANSPORTER_TRUCK_NO();
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
Vector VQTY_MT = dlng.getVQTY_MT();
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
Vector VGAS_DT = dlng.getVGAS_DT();

Vector VQTY_SELLER_MMBTU = dlng.getVQTY_SELLER_MMBTU();
Vector VQTY_SELLER_MT = dlng.getVQTY_SELLER_MT();
Vector VQTY_ALLOC_MMBTU = dlng.getVQTY_ALLOC_MMBTU();
Vector VQTY_ALLOC_MT = dlng.getVQTY_ALLOC_MT();
Vector VCOLOR = dlng.getVCOLOR();
Vector VCOLOR_ALLOC = dlng.getVCOLOR_ALLOC();
Vector VAGMT_BASE = dlng.getVAGMT_BASE();
Vector VCONTRACT_TYPE_NM = dlng.getVCONTRACT_TYPE_NM();

String total_buyer_mmbtu = dlng.getTotal_buyer_mmbtu();
String total_buyer_mt = dlng.getTotal_buyer_mt();
String total_seller_mmbtu = dlng.getTotal_seller_mmbtu();
String total_seller_mt = dlng.getTotal_seller_mt();
String total_alloc_mmbtu = dlng.getTotal_alloc_mmbtu();
String total_alloc_mt = dlng.getTotal_alloc_mt();

String file_nm="Nomination_vs_truck_loading_"+to_dt.replaceAll("/", "")+".xls";
%>
<body>
	<%
	response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "inline; filename="+file_nm);  
	%>	
	<font size="4"><b>DLNG Nomination vs Truck Loading Report From <%=from_dt%> To <%=to_dt%></b></font>
	<br>
	<!-- SagarB20250929 Added this variable for showing BU Abbr in exported excel -->
	<%if(!bu_seq.equals("0") && (!bu_seq.equals(""))){ %>
	<font size="3"><b>Business Unit : <%=bu_abbr%></b></font>
	<%} %>					  				
	<br>					  				
	<%if(VCOUNTERPARTY_PLANT_SEQ.size()>0)
	{ %>	
 		<table border="1">
			<thead>
				<tr>
					<th rowspan="2">Gas Day</th>																	
					<th rowspan="2">Customer</th>
					<th rowspan="2">Customer Plant</th>
					<th rowspan="2">BU</th>
					<th rowspan="2">Contract#</th>
					<th rowspan="2">Contract/Trade Ref#</th>
					<th rowspan="2">DCQ</th>
					<th colspan="2">Buyer Nomination</th>	
					<th colspan="2">Seller Nomination</th>
					<th colspan="2">Truck Loading</th>																
				</tr>
				<tr>
					<th>MMBTU</th>
					<th>MT</th>
					<th>MMBTU</th>
					<th>MT</th>
					<th>MMBTU</th>
					<th>MT</th>
				</tr>
			</thead>
			<tbody>
				<%for(int l=0; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
				{ 
				%>
					<tr>
						<td align="center"><%=VGAS_DT.elementAt(l) %></td>
						<td align="center"><%=VCOUNTERPARTY_NM.elementAt(l)%></td>
						<td align="center"><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%></td>
						<td align="center"><%=VBU_PLANT_ABBR.elementAt(l)%></td>
						<td align="center"><%=VDIS_CONT_MAPPING.elementAt(l)%></td>
						<td align="center">&nbsp;<%=VCONT_REF.elementAt(l)%></td>
						<td align="right"><%=VDCQ.elementAt(l) %></td>																			
						<td align="right"><%=VQTY_MMBTU.elementAt(l) %></td>
						<td align="right"><%=VQTY_MT.elementAt(l) %></td>
						<td align="right" style="color:<%=VCOLOR.elementAt(l)%>"><%=VQTY_SELLER_MMBTU.elementAt(l) %></td>
						<td align="right"><%=VQTY_SELLER_MT.elementAt(l) %></td>
						<td align="right" style="color:<%=VCOLOR_ALLOC.elementAt(l)%>"><%=VQTY_ALLOC_MMBTU.elementAt(l) %></td>
						<td align="right"><%=VQTY_ALLOC_MT.elementAt(l) %></td>
					</tr>
				<%} %>
				<tr>
					<td colspan="7"><div align="right"><b>Total:</b></div></td>
					<td align="right"><b><%=total_buyer_mmbtu %></b></td>
					<td align="right"><b><%=total_buyer_mt %></b></td>
					<td align="right"><b><%=total_seller_mmbtu %></b></td>
					<td align="right"><b><%=total_seller_mt %></b></td>
					<td align="right"><b><%=total_alloc_mmbtu %></b></td>
					<td align="right"><b><%=total_alloc_mt %></b></td>
				</tr>
			</tbody>
		</table>
	<%}else{ %>
		<%=utilmsg.infoMessage("<b>No Seller Nomination Done for Selected Gas Day!</b>") %>
	<%} %>				

</body>
</html>