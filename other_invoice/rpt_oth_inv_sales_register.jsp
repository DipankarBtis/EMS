<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var month_to = document.forms[0].month_to.value;
	var year_to = document.forms[0].year_to.value;
	var tmp_month = document.forms[0].tmp_from_month.value;
	var tmp_year = document.forms[0].tmp_from_year.value;
	var tmp_month_to = document.forms[0].tmp_to_month.value;
	var tmp_year_to = document.forms[0].tmp_to_year.value;
	
	var u = document.forms[0].u.value;
	
	var invoice_type=document.forms[0].invoice_type.value;
	
	var flag=checkMonthYearRange(document.forms[0].month,document.forms[0].year,document.forms[0].month_to,document.forms[0].year_to);
	if(flag==true)
	{
		var url="rpt_oth_inv_sales_register.jsp?invoice_type="+invoice_type+"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to+"&u="+u;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		document.forms[0].month.value=tmp_month;
		document.forms[0].year.value=tmp_year;
		document.forms[0].month_to.value=tmp_month_to;
		document.forms[0].year_to.value=tmp_year_to;
	}
}

function exportToXls()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var month_to = document.forms[0].month_to.value;
	var year_to = document.forms[0].year_to.value;
	var invoice_type=document.forms[0].invoice_type.value;
	
	var fileName="Other_Invoice_GST_Register.xls"
	
	var url="xls_oth_inv_sales_register.jsp?invoice_type="+invoice_type+"&fileName="+fileName+"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to;
	
	location.replace(url);
}
</script>
</head>

<jsp:useBean class="com.etrm.fms.other_invoice.DB_Other_Invoice_Report" id="oth_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();
int filter_start_year = CommonVariable.filter_start_year;

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
<%@ include file="../home/header.jsp"%>
<%if(!owner_cd.equals("2")) {%>
<div class="box-body">
	<div class="row">
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
		<div class="col-md-8 col-sm-8 col-xs-8">
			<div class="card cardmain">
				<div class="card-header cdheader ">
				</div>
				<div class="card-body cdbody">
					<div class="alert alert-info">
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="form-group row" align="center">
									<label class="form-label"  style="font-size:40px;font-weight: 700;"><i class='fa fa-exclamation-circle fa-lg'></i> Feature Not Supported</label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
				</div>
			</div>   
		</div>
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
	</div>
</div>
<%}else{ %>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<%if(!msg.equals("")){
				if(msg_type.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
				<%}else if(msg_type.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
				<%}
			} %>
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Other Invoice GST Register
					    </div>
					    <div class="d-flex justify-content-between">
					   		 <div onclick="exportToXls();" style="color:green;">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
							</div>
						   	<%-- 
						   		 &nbsp;
								<div class="btn-group">
									<select class="btn btn-outline-secondary btngrp btnactive" name="segment" onchange="refresh();">
										<option value="">All</option>
										<option value="S">RLNG(SN,LOA)</option>
										<option value="E">DLNG(SN,LOA,SO,TS)</option>
										<option value="X">IGX(RLNG,DLNG)</option>
										<option value="A">LTCORA</option>
										<option value="V">Derivatives</option>
									</select>
								</div>
								<script>document.forms[0].segment.value="<%=segment%>"</script>
						   	<--%>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<!-- <div class="col-sm-3 col-xs-3 col-md-3"></div> -->
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>From</b></label>
					  		</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="month">
										<option value="01" label="January">January</option>
										<option value="02" label="February">February</option>
										<option value="03" label="March">March</option>
										<option value="04" label="April">April</option>
										<option value="05" label="May">May</option>
										<option value="06" label="June">June</option>
										<option value="07" label="July">July</option>
										<option value="08" label="August">August</option>
										<option value="09" label="September">September</option>
										<option value="10" label="October">October</option>
										<option value="11" label="November">November</option>
										<option value="12" label="December">December</option>
									</select>
									<script>document.forms[0].month.value="<%=month%>"</script>
								</div>
								<div class="col">
					  				<select class="form-select form-select-sm" name="year">
					  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
							</div>
						</div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>To</b></label>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col">
					  				<select class="form-select form-select-sm" name="month_to">
										<option value="01" label="January">January</option>
										<option value="02" label="February">February</option>
										<option value="03" label="March">March</option>
										<option value="04" label="April">April</option>
										<option value="05" label="May">May</option>
										<option value="06" label="June">June</option>
										<option value="07" label="July">July</option>
										<option value="08" label="August">August</option>
										<option value="09" label="September">September</option>
										<option value="10" label="October">October</option>
										<option value="11" label="November">November</option>
										<option value="12" label="December">December</option>
									</select>
									<script>document.forms[0].month_to.value="<%=month_to%>"</script>
								</div>
								<div class="col">
					  				<select class="form-select form-select-sm" name="year_to">
					  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year_to.value="<%=year_to%>"</script>
								</div>
							</div>
				  		</div>
				  		<!-- <div class="col-sm-2 col-xs-2 col-md-2"></div> -->
				  		<%-- <div class="col-sm-2 col-xs-2 col-md-2">  	
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Vendor</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="vendor_cd" >		
										<option value="0">--Select--</option>
										<%for(int i=0;i<VSEGMENT_TYPE.size();i++){ %>
										<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>	
						</div> --%>
				  		<div class="col-sm-3 col-xs-3 col-md-3">  	
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Invoice Type</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="invoice_type">		
										<option value="0">--All--</option>
										<%for(int i=0;i<VSEGMENT_TYPE.size();i++){ %>
										<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].invoice_type.value="<%=invoice_type%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">
						
							<div class="col">
								<input type="button" class="btn btn-warning com-btn" value="Apply Filters" onclick="refresh();">
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%
						int i=0,j=0,ctn=0,l=0,k=0;
						for(l=0;l<VDISP_SEGMENT_TYPE.size();l++){
							int index = Integer.parseInt(""+VINDEX.elementAt(l));
					%>
							<div class="row">
								<div class="col-md-12 col-sm-12 col-xs-12">
									<div class="accordion">
										<div class="accordion-item accor_item">
											<h2 class="accordion-header" id="heading1">
												<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l %>" aria-expanded="false" aria-controls="collapse<%=l%>">
												<%=VDISP_SEGMENT_NM.elementAt(l) %>&nbsp;&nbsp;<font color="blue">(<%=index%> Items)</font>
												</button>	
									    	</h2>
									    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading1">
									    		<div class="accordion-body accor-body">
									    			<div class="row">
									    				<div class="table-responsive">
															<table class="table table-bordered table-hover" id="example<%=l%>">
																<thead id="tbsearch<%=l%>">
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
														</div>
									    			</div>
									    		</div>
									    	</div>
										</div>
									</div>
								</div>
							</div>					
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="tmp_from_month" value="<%=month%>">
<input type="hidden" name="tmp_from_year" value="<%=year%>">
<input type="hidden" name="tmp_to_month" value="<%=month_to%>">
<input type="hidden" name="tmp_to_year" value="<%=year_to%>">

<input type="hidden" name="read_access" value="<%=read_access%>">
<input type="hidden" name="write_access" value="<%=write_access%>">
<input type="hidden" name="check_access" value="<%=check_access%>">
<input type="hidden" name="print_access" value="<%=print_access%>">
<input type="hidden" name="delete_access" value="<%=delete_access%>">  	
<input type="hidden" name="audit_access" value="<%=audit_access%>">
<input type="hidden" name="authorize_access" value="<%=authorize_access%>">
<input type="hidden" name="approve_access" value="<%=approve_access%>">
<input type="hidden" name="execute_access" value="<%=execute_access%>">
<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<input type="hidden" name="u" value="<%=u%>">
<input type="hidden" name="acc_size" value="<%=VINDEX.size()%>">
</form>
<%} %>
</body>
<script>

$(document).ready(function() {
	var acc_size=document.forms[0].acc_size.value; 
	for(k=0;k<acc_size;k++)
	{
		$('#tbsearch'+k+' th').each(function(i){
			//alert(i)
			var title = $(this).text();
			if(title == "Sr#" || title == "USD Details" || title == "INR Details")
			{
				//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
			}
			
			else
			{
				$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'_'+k+'" onkeyup="Search(this,'+i+','+k+');" placeholder="Search '+title+'" style="width:100px"/></div>');
			}
		});
	}
	
});

function Search(obj, indx,k) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+k);
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	//tbody=tr[i].getElementsByTagName("tbody");alert(tbody)
    	if (td) 
    	{
      		txtValue = td.textContent || td.innerText;
      		if (txtValue.toLocaleLowerCase().indexOf(filter) > -1) {
        		tr[i].style.display = "";
        		count++;
      		} else {
      			tr[i].style.display = "none";
      		}
    	}       
  	}
}
</script>
</html>