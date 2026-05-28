<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.other_invoice.DB_Other_Invoice_Report" id="oth_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

String sysdate = utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String invoice_type=request.getParameter("invoice_type")==null?"0":request.getParameter("invoice_type");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
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

oth_rpt.setCallFlag("OTH_INV_GST_REGISTER");
oth_rpt.setComp_cd(owner_cd);
oth_rpt.setInvoice_type(invoice_type);
oth_rpt.setMonth(month);
oth_rpt.setMonth_to(month_to);
oth_rpt.setYear(year);
oth_rpt.setYear_to(year_to);
oth_rpt.init();

Vector VSEGMENT_NM = oth_rpt.getVSEGMENT_NM();
Vector VSEGMENT_TYPE = oth_rpt.getVSEGMENT_TYPE();
Vector VDISP_SEGMENT_NM = oth_rpt.getVDISP_SEGMENT_NM();
Vector VDISP_SEGMENT_TYPE = oth_rpt.getVDISP_SEGMENT_TYPE();

Vector VSUPPLIER_CD = oth_rpt.getVSUPPLIER_CD();
Vector VSUPPLIER_NM = oth_rpt.getVSUPPLIER_NM();
Vector VVENDOR_CD = oth_rpt.getVVENDOR_CD();
Vector VVENDOR_NM = oth_rpt.getVVENDOR_NM();
Vector VGST_TIN_NO = oth_rpt.getVGST_TIN_NO();
Vector VINVOICE_NO = oth_rpt.getVINVOICE_NO();
Vector VINVOICE_DT = oth_rpt.getVINVOICE_DT();
Vector VVENDOR_SUPP_INV_REF_NO = oth_rpt.getVVENDOR_SUPP_INV_REF_NO();
Vector VPACER_NO = oth_rpt.getVPACER_NO();
Vector VGROSS_AMT_USD = oth_rpt.getVGROSS_AMT_USD();
Vector VIGST_AMT_USD = oth_rpt.getVIGST_AMT_USD();
Vector VCGST_AMT_USD = oth_rpt.getVCGST_AMT_USD();
Vector VSGST_AMT_USD = oth_rpt.getVSGST_AMT_USD();
Vector VGROSS_AMT_INR = oth_rpt.getVGROSS_AMT_INR();
Vector VIGST_AMT_INR = oth_rpt.getVIGST_AMT_INR();
Vector VCGST_AMT_INR = oth_rpt.getVCGST_AMT_INR();
Vector VSGST_AMT_INR = oth_rpt.getVSGST_AMT_INR();
Vector VIGST_FACTOR_INR = oth_rpt.getVIGST_FACTOR_INR();
Vector VIGST_FACTOR_USD = oth_rpt.getVIGST_FACTOR_USD();
Vector VCGST_FACTOR_INR = oth_rpt.getVCGST_FACTOR_INR();
Vector VCGST_FACTOR_USD = oth_rpt.getVCGST_FACTOR_USD();
Vector VSGST_FACTOR_INR = oth_rpt.getVSGST_FACTOR_INR();
Vector VSGST_FACTOR_USD = oth_rpt.getVSGST_FACTOR_USD();
Vector VSAC_CD = oth_rpt.getVSAC_CD();
Vector VVESSEL_NM = oth_rpt.getVVESSEL_NM();
Vector VVESSEL_FLG = oth_rpt.getVVESSEL_FLG();
Vector VQTY = oth_rpt.getVQTY();
Vector VGRT= oth_rpt.getVGRT();
Vector VIMPORTER= oth_rpt.getVIMPORTER();
Vector VBERTH_TIME_SLOT= oth_rpt.getVBERTH_TIME_SLOT();
Vector VSALES_PRICE = oth_rpt.getVSALES_PRICE();
Vector VINVOICE_DUE_DT = oth_rpt.getVINVOICE_DUE_DT();
Vector VCHRG_AMT = oth_rpt.getVCHRG_AMT();
Vector VSALE_NO = oth_rpt.getVSALE_NO();
Vector VHSN_CD = oth_rpt.getVHSN_CD();
Vector VGATE_PASS_NO = oth_rpt.getVGATE_PASS_NO();
Vector VPUR_NO = oth_rpt.getVPUR_NO();
Vector VREFERENCE = oth_rpt.getVREFERENCE();
Vector VUAM = oth_rpt.getVUAM();
Vector VINDEX = oth_rpt.getVINDEX();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table  width="100%" border="1">
		<tr>
			<th colspan="30" rowspan="2" align="left">Other Invoice GST Register From <%=month %>/<%=year %> To <%=month_to %>/<%=year_to %></th>
		</tr>
	</table >
	<%
		int i=0,j=0,ctn=0,l=0,k=0;
		for(l=0;l<VDISP_SEGMENT_TYPE.size();l++){
			int index = Integer.parseInt(""+VINDEX.elementAt(l));
	%>
		<div class="row">
			<div class="col-sm-12 col-xs-12 col-md-12">
				&nbsp;
			</div>
		</div> 
		<table  width="100%" border="1">
			<tr>
				<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("COSTR") || VDISP_SEGMENT_TYPE.elementAt(l).equals("NPR")){%>
					<td colspan="23" align="left"><b><%=VDISP_SEGMENT_NM.elementAt(l)%></b></td>
				<%}else if(VDISP_SEGMENT_TYPE.elementAt(l).equals("HSA")){%>
					<td colspan="28" align="left"><b><%=VDISP_SEGMENT_NM.elementAt(l)%></b></td>
				<%}else if(VDISP_SEGMENT_TYPE.elementAt(l).equals("HS")){%>
					<td colspan="23" align="left"><b><%=VDISP_SEGMENT_NM.elementAt(l)%></b></td>
				<%}else if(VDISP_SEGMENT_TYPE.elementAt(l).equals("COSTRH")){%>
					<td colspan="22" align="left"><b><%=VDISP_SEGMENT_NM.elementAt(l)%></b></td>
				<%}else{%>
					<td colspan="25" align="left"><b><%=VDISP_SEGMENT_NM.elementAt(l)%></b></td>
				<%} %>
			</tr>
		</table>
		<table width="100%" border="1">
			<thead>
				<tr>
					<th rowspan="2">Sr#</th>
					<%if(!VDISP_SEGMENT_TYPE.elementAt(l).equals("HSA") && !VDISP_SEGMENT_TYPE.elementAt(l).equals("HS") && !VDISP_SEGMENT_TYPE.elementAt(l).equals("COSTRH")){%>
						<th rowspan="2">Supplier Name</th>
					<%}%>
					<th rowspan="2">Vendor Name</th>
					<th rowspan="2">GSTIN/UIN</th>
					<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("HSA")){%>
						<th rowspan="2">Vessel Name</th>
						<th rowspan="2">FLAG</th>
						<th rowspan="2">Quantity M<sup>3</sup></th>
						<th rowspan="2">Importer</th>
					<%} %>
					<th rowspan="2">Invoice No</th>
					<th rowspan="2">Invoice Date</th>
					<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("HSA")){%>
						<th rowspan="2">Due Date</th>
						<th rowspan="2">Sale Price</th>
						<th rowspan="2">No. Of Time Slots Berthing</th>
						<th rowspan="2">GRT</th>
					<%} %>
					<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("HS")){%>
						<th rowspan="2">Due Date</th>
						<th rowspan="2">Sale Price</th>
						<th rowspan="2">Total Charge(USD)</th>
					<%} %>
					<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("COSTRH")){%>
						<th rowspan="2">Contract/Purchase Order Ref</th>
						<th rowspan="2">Reference</th>
					<%} %>
					<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("SFA")){%>
						<th rowspan="2">Sale No</th>
						<th rowspan="2">Gate Pass No</th>
					<%} %>
					<%if(!VDISP_SEGMENT_TYPE.elementAt(l).equals("HSA") && !VDISP_SEGMENT_TYPE.elementAt(l).equals("HS") && !VDISP_SEGMENT_TYPE.elementAt(l).equals("COSTRH")&&!VDISP_SEGMENT_TYPE.elementAt(l).equals("SFA")){%>
						<th rowspan="2">Vendor/Supplier Invoice Ref#</th>
						<th rowspan="2">Pacer WO/PO No</th>
					<%} %>
					<th colspan="7">USD Details</th>
					<th colspan="7">INR Details</th>
					<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("SFA")){%>
						<th rowspan="2">HSN Code</th>
					<%} %>
					<%if(!VDISP_SEGMENT_TYPE.elementAt(l).equals("SFA")){%>
						<th rowspan="2">SAC Code</th>
					<%} %>
					<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("SFA")){%>
						<th rowspan="2">QTY</th>
						<th rowspan="2">Unit of Measure</th>
					<%} %>
				</tr>
				<tr>
					<th>Gross Amount</th>
						<th>IGST %</th>
					<th>IGST Amount</th>
					<th>CGST %</th>
					<th>CGST Amount</th>
					<th>SGST %</th>
					<th>SGST Amount</th>									
					<th>Gross Amount</th>
						<th>IGST %</th>
					<th>IGST Amount</th>
					<th>CGST %</th>
					<th>CGST Amount</th>
					<th>SGST %</th>
					<th>SGST Amount</th>									
				</tr>
			</thead>
			<tbody>
				<%k=0;
				if(index > 0){ %>
					<%for(i=i;i<VVENDOR_CD.size();i++){
						k+=1;
					%>
						<tr>
							<td align="center"><%=k%></td>
							<%if(!VDISP_SEGMENT_TYPE.elementAt(l).equals("HSA") && !VDISP_SEGMENT_TYPE.elementAt(l).equals("HS") && !VDISP_SEGMENT_TYPE.elementAt(l).equals("COSTRH")){%>
								<td align="center"><%=VSUPPLIER_NM.elementAt(i)%></td>
							<%} %>
							<td align="center"><%=VVENDOR_NM.elementAt(i)%></td>
							<td align="center"><%=VGST_TIN_NO.elementAt(i)%></td>
							<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("HSA")){%>
								<td align="center"><%=VVESSEL_NM.elementAt(i)%></td>
								<td align="center"><%=VVESSEL_FLG.elementAt(i)%></td>
								<td align="right"><%=VQTY.elementAt(i)%></td>
								<td align="center"><%=VIMPORTER.elementAt(i)%></td>
							<%} %>
							<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
							<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
							<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("HSA")){%>
								<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
								<td align="right"><%=VSALES_PRICE.elementAt(i) %></td>
								<td align="center"><%=VBERTH_TIME_SLOT.elementAt(i)%></td>
								<td align="center"><%=VGRT.elementAt(i)%></td>
							<%} %>
							<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("HS")){%>
								<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
								<td align="right"><%=VSALES_PRICE.elementAt(i) %></td>
								<td align="right"><%=VCHRG_AMT.elementAt(i) %></td>
							<%} %>
							<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("COSTRH")){%>
								<td align="center"><%=VPUR_NO.elementAt(i)%></td>
								<td align="center"><%=VREFERENCE.elementAt(i)%></td>
							<%} %>
							<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("SFA")){%>
								<td align="center"><%=VSALE_NO.elementAt(i)%></td>
								<td align="center"><%=VGATE_PASS_NO.elementAt(i)%></td>
							<%} %>
							<%if(!VDISP_SEGMENT_TYPE.elementAt(l).equals("HSA") && !VDISP_SEGMENT_TYPE.elementAt(l).equals("HS") && !VDISP_SEGMENT_TYPE.elementAt(l).equals("COSTRH") && !VDISP_SEGMENT_TYPE.elementAt(l).equals("SFA")){ %>
							    <td align="center"><%=VVENDOR_SUPP_INV_REF_NO.elementAt(i)%></td>
							    <td align="center"><%=VPACER_NO.elementAt(i)%></td>
							<%} %>
							<td align="right"><%=VGROSS_AMT_USD.elementAt(i)%></td>
							<td align="right"><%=VIGST_FACTOR_USD.elementAt(i)%></td>
							<td align="right"><%=VIGST_AMT_USD.elementAt(i)%></td>
							<td align="right"><%=VCGST_FACTOR_USD.elementAt(i)%></td>
							<td align="right"><%=VCGST_AMT_USD.elementAt(i)%></td>
							<td align="right"><%=VSGST_FACTOR_USD.elementAt(i)%></td>
							<td align="right"><%=VSGST_AMT_USD.elementAt(i)%></td>
							<td align="right"><%=VGROSS_AMT_INR.elementAt(i)%></td>
							<td align="right"><%=VIGST_FACTOR_INR.elementAt(i)%></td>
							<td align="right"><%=VIGST_AMT_INR.elementAt(i)%></td>
							<td align="right"><%=VCGST_FACTOR_INR.elementAt(i)%></td>
							<td align="right"><%=VCGST_AMT_INR.elementAt(i)%></td>
							<td align="right"><%=VSGST_FACTOR_INR.elementAt(i)%></td>
							<td align="right"><%=VSGST_AMT_INR.elementAt(i)%></td>
							<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("SFA")){%>
								<td align="center"><%=VHSN_CD.elementAt(i) %></td>	<!-- HSN -->
							<%} %>
							<%if(!VDISP_SEGMENT_TYPE.elementAt(l).equals("SFA")){%>
								<td align="center"><%=VSAC_CD.elementAt(i) %></td>	<!-- SAC -->
							<%} %>
							<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("SFA")){%>
								<td align="right"><%=VQTY.elementAt(i)%></td>
								<td align="center"><%=VUAM.elementAt(i) %></td>	
							<%} %>
						</tr>
						<%if(k==index)
						{
							i+=1;
							break;
						}
						%>
					<%} %>
				<%}else{%>
					<tr>
						<%if(VDISP_SEGMENT_TYPE.elementAt(l).equals("COSTR") || VDISP_SEGMENT_TYPE.elementAt(l).equals("NPR")){%>
							<td colspan="23" align="center"><%=utilmsg.infoMessage("<b>No "+ VDISP_SEGMENT_NM.elementAt(l)+" Invoice is Generated for Selected Period!</b>") %></td>
						<%}else if(VDISP_SEGMENT_TYPE.elementAt(l).equals("HSA")){%>
							<td colspan="28" align="center"><%=utilmsg.infoMessage("<b>No "+ VDISP_SEGMENT_NM.elementAt(l)+" Invoice is Generated for Selected Period!</b>") %></td>
						<%}else if(VDISP_SEGMENT_TYPE.elementAt(l).equals("HS")){%>
							<td colspan="23" align="center"><%=utilmsg.infoMessage("<b>No "+ VDISP_SEGMENT_NM.elementAt(l)+" Invoice is Generated for Selected Period!</b>") %></td>
						<%}else if(VDISP_SEGMENT_TYPE.elementAt(l).equals("COSTRH")){%>
							<td colspan="22" align="center"><%=utilmsg.infoMessage("<b>No "+ VDISP_SEGMENT_NM.elementAt(l)+" Invoice is Generated for Selected Period!</b>") %></td>
						<%}else{%>
							<td colspan="25" align="center"><%=utilmsg.infoMessage("<b>No "+VDISP_SEGMENT_NM.elementAt(l)+"Invoice is Generated for Selected Period!</b>") %></td>
						<%} %>
					</tr>
				<%} %>
			</tbody>
		</table>
	<%}%>
</body>
</html>
