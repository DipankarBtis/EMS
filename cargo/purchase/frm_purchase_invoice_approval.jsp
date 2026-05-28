<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var segment = "";//document.forms[0].segment.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_purchase_invoice_approval.jsp?u="+u+"&segment="+segment+"&from_dt="+from_dt+"&to_dt="+to_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var newWindow;
function openSapView(financial_year,invoice_seq,contract_type)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_view_purchase_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+"&contract_type="+contract_type+"&u="+u;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Purchase SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Purchase SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function doExchngSubmit(index,financial_year,invoice_seq,contract_type,exchng_flag,purchase_type_flag,invoice_type,inv_flag)
{
	var sap_exchng_dt=document.getElementById("sap_exchang_dt"+index).value
	var sap_exchng_rate=document.getElementById("sap_exchang_rate"+index).value
	
	/* var tds_factor=document.getElementById("tds_factor"+index).value
	var tds_amt=document.getElementById("tds_amt"+index).value
	var tds_struct_cd=document.getElementById("tds_struct_cd"+index).value
	var tds_eff_dt=document.getElementById("tds_eff_dt"+index).value */
	
	if(trim(sap_exchng_rate)!="")
	{
		var a=confirm("Do you want to apply this Exchange rate?")
		if(a)
		{
			document.forms[0].financial_year.value=financial_year;
			document.forms[0].invoice_seq.value=invoice_seq;
			document.forms[0].contract_type.value=contract_type;
			document.forms[0].exchng_dt.value=sap_exchng_dt;
			document.forms[0].exchng_rate.value=sap_exchng_rate;
			document.forms[0].sap_exchng_flag.value=exchng_flag;
			document.forms[0].purchase_type_flag.value=purchase_type_flag;
			document.forms[0].invoice_type.value=invoice_type;
			
			document.forms[0].inv_flag.value=inv_flag;
			
			/* document.forms[0].tdsFactor.value=tds_factor;
			document.forms[0].tdsAmount.value=tds_amt;
			document.forms[0].tdsStructCd.value=tds_struct_cd;
			document.forms[0].tdsEffDt.value=tds_eff_dt */
			
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert("Enter Exchange Rate for ROW - "+(parseInt(index)+1))
	}
}

function doGenXML(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,purchase_type_flag,invoice_type,sap_approval_flag, bu_seq, cont_no, inv_flag)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_view_purchase_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
			"&contract_type="+contract_type+"&purchase_type_flag="+purchase_type_flag+"&invoice_type="+invoice_type+
			"&counterparty_cd="+couterpty_cd+"&invoice_no="+invoice_no+"&sap_approval_flag="+sap_approval_flag+
			"&bu_seq="+bu_seq+"&cont_no="+cont_no+"&inv_flag="+inv_flag+"&u="+u;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Purchase SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Purchase SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var segment = "";//document.forms[0].segment.value;
	
	var url = "xls_purchase_invoice_approval.jsp?segment="+segment+"&from_dt="+from_dt+"&to_dt="+to_dt;
	
	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");

purchase.setCallFlag("PURCHASE_INVOICE_APPROVAL");
purchase.setComp_cd(owner_cd);
purchase.setSegment(segment);
purchase.setFrom_dt(from_dt);
purchase.setTo_dt(to_dt);
purchase.init();

Vector VSEGMENT = purchase.getVSEGMENT();
Vector VSEGMENT_TYPE = purchase.getVSEGMENT_TYPE();

Vector VCONT_NO = purchase.getVCONT_NO();
Vector VINVOICE_DT = purchase.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = purchase.getVINVOICE_DUE_DT();
Vector VINVOICE_NO = purchase.getVINVOICE_NO();
Vector VINVOICE_SEQ = purchase.getVINVOICE_SEQ();
Vector VPERIOD_START_DT = purchase.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = purchase.getVPERIOD_END_DT();
Vector VALLOC_QTY = purchase.getVALLOC_QTY();
Vector VSALES_PRICE = purchase.getVSALES_PRICE();
Vector VSALES_PRICE_UNIT = purchase.getVSALES_PRICE_UNIT();
Vector VGROSS_AMT = purchase.getVGROSS_AMT();
Vector VTAX_AMT = purchase.getVTAX_AMT();
Vector VINVOICE_AMT = purchase.getVINVOICE_AMT();
Vector VEXCHNAGE_RATE = purchase.getVEXCHNAGE_RATE();
Vector VEXCHNAGE_RATE_DATE = purchase.getVEXCHNAGE_RATE_DATE();
Vector VBU_NM = purchase.getVBU_NM();
Vector VBU_SEQ = purchase.getVBU_SEQ();
Vector VDEAL_NO = purchase.getVDEAL_NO();
Vector VSALE_AMT = purchase.getVSALE_AMT();
Vector VADJ_SIGN = purchase.getVADJ_SIGN();
Vector VADJ_AMT = purchase.getVADJ_AMT();
Vector VNET_PAYABLE = purchase.getVNET_PAYABLE();
Vector VTCS_TDS = purchase.getVTCS_TDS();
Vector VTCS_TDS_AMT = purchase.getVTCS_TDS_AMT();
Vector VTCS_TDS_FACTOR = purchase.getVTCS_TDS_FACTOR();
Vector VINVOICE_RAISED_IN = purchase.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = purchase.getVPAYMENT_DONE_IN();
Vector VFINANCIAL_YEAR = purchase.getVFINANCIAL_YEAR();
Vector VCONTRACT_TYPE = purchase.getVCONTRACT_TYPE();
Vector VSAP_EXCHANG_FLAG = purchase.getVSAP_EXCHANG_FLAG();
Vector VSAP_APPROVAL_FLAG = purchase.getVSAP_APPROVAL_FLAG();
Vector VINV_FLAG = purchase.getVINV_FLAG();

Vector VINVOICE_TYPE =purchase.getVINVOICE_TYPE();
Vector VPAYMENT_TYPE_FLAG = purchase.getVPAYMENT_TYPE_FLAG();
Vector VPAYMENT_TYPE_NM= purchase.getVPAYMENT_TYPE_NM();

Vector VGROSS_AMT_USD = purchase.getVGROSS_AMT_USD();
Vector VTAX_AMT_USD = purchase.getVTAX_AMT_USD();
Vector VINVOICE_AMT_USD = purchase.getVINVOICE_AMT_USD();
Vector VADJ_AMT_USD = purchase.getVADJ_AMT_USD();
Vector VNET_PAYABLE_USD = purchase.getVNET_PAYABLE_USD();
Vector VTCS_TDS_AMT_USD = purchase.getVTCS_TDS_AMT_USD();
Vector VTCS_TDS_STRUCT_CD = purchase.getVTCS_TDS_STRUCT_CD();
Vector VTCS_TDS_EFF_DT = purchase.getVTCS_TDS_EFF_DT();
Vector VTCS_TDS_DONE = purchase.getVTCS_TDS_DONE();

Vector VCOUNTERPTY_CD = purchase.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = purchase.getVCOUNTERPTY_ABBR();
Vector VINDEX = purchase.getVINDEX();
Vector VCASH_FLOW = purchase.getVCASH_FLOW();
Vector VDIS_REMITTANCE_NO = purchase.getVDIS_REMITTANCE_NO();
Vector VDISP_INVOICE_NO = purchase.getVDISP_INVOICE_NO();
Vector VQTY_UNIT = purchase.getVQTY_UNIT();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_PurchaseReports">

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
					    	Purchase Actual Report
					    </div>
					    <%-- <div class="btn-group">
							<select class="btn btn-outline-secondary btngrp btnactive" name="segment" onchange="refresh();">
								<option value="">All</option>
								<%for(int i=0;i<VSEGMENT.size();i++){ %>
								<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT.elementAt(i)%></option>
								<%} %>
							</select>
						</div> 						
						<script>document.forms[0].segment.value="<%=segment%>"</script>--%>
						<div align="right" onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>From</b></label>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
											<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
											onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
											<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
										</div>
									</div>
									<div class="col-auto">
										<label class="form-label"><b>To</b></label>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
											<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
											onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
											<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%int i=0;int k=0;
					for(int j=0; j<VSEGMENT_TYPE.size(); j++){ 
						int index = Integer.parseInt(""+VINDEX.elementAt(j));
					if(j!=0)
					{%>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div> 
					<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VSEGMENT.elementAt(j) %></label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover serchtbl" id="example<%=j%>">
								<thead id="tbsearch<%=j%>">
									<tr >
										<th rowspan="2" class="tbser<%=j%>">Type</th>
										<th rowspan="2" class="tbser<%=j%>">Trader</th>
										<th rowspan="2" class="tbser<%=j%>"><%=owner_abbr%> BU</th>
										<th rowspan="2" class="tbser<%=j%>"><%if(VSEGMENT_TYPE.elementAt(j).equals("N") || VSEGMENT_TYPE.elementAt(j).equals("CDF") || VSEGMENT_TYPE.elementAt(j).equals("CDP")){ %>Cargo#<%}else{ %>Contract#<%} %></th>
										<th rowspan="2" class="tbser<%=j%>">Billing Period</th>
										<th rowspan="2" class="tbser<%=j%>">Cash Flow</th> 
										<th rowspan="2" class="tbser<%=j%>">Invoice#</th>
										<th rowspan="2" class="tbser<%=j%>">Remittance#</th>
										<th rowspan="2" class="tbser<%=j%>">Invoice Date</th>
										<th rowspan="2">Invoice Due Date</th>
										<th rowspan="2">Invoiced Quantity</th>
										<th rowspan="2">Rate</th>
										<th rowspan="2" class="tbser<%=j%>" style="background: #000066; color: white;">Rate Unit</th>
										<th rowspan="2">Sales Amount</th>
										<th rowspan="2" class="tbser<%=j%>" style="background: #000066; color: white;">Invoice Raised In</th>
										<th rowspan="2">Exchange Rate Date</th>
										<th rowspan="2">Exchange Rate</th>
										<th rowspan="2" style="background: #000066; color: white;">Invoice Paid In</th>
										<th colspan="2">Gross Amount</th>
										<th colspan="2">Tax</th>
										<th colspan="2">Invoice Amount</th>
										<th rowspan="2" class="tbser<%=j%>">TCS/TDS</th>
										<th rowspan="2">TCS/TDS %</th>
										<th colspan="2">+TCS/-TDS Amount</th>
										<th colspan="2">Adjust Amount</th>
										<th colspan="2">Net Payable</th>
										<th rowspan="2">SAP XML</th>
									</tr>
									<tr>
										<th>USD</th>
										<th>INR</th>
										<th>USD</th>
										<th>INR</th>
										<th>USD</th>
										<th>INR</th>
										<th>USD</th>
										<th>INR</th>
										<th>USD</th>
										<th>INR</th>
										<th>USD</th>
										<th>INR</th>										
									</tr>
								</thead>
								<tbody>
								<%k=0;
								if(index > 0){ %>
									<%for(i=i; i<VINVOICE_SEQ.size(); i++){ 
										k+=1;
										
										boolean isEditable=false;
										if((VINVOICE_RAISED_IN.elementAt(i).equals("USD") && !VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")) && !VCONTRACT_TYPE.elementAt(i).toString().equals("N"))
										{
											isEditable=true; 
										}
									%>
									<tr>
										<td align="center"><%=VPAYMENT_TYPE_NM.elementAt(i) %></td>
										<td align="center"><%=VCOUNTERPTY_ABBR.elementAt(i) %></td>
										<td align="center"><%=VBU_NM.elementAt(i) %></td>
										<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
										<td align="center"><%=VPERIOD_START_DT.elementAt(i) %> - <%=VPERIOD_END_DT.elementAt(i) %></td>
										<td align="center"><%=VCASH_FLOW.elementAt(i) %></td>
										<td align="left"><%=VDISP_INVOICE_NO.elementAt(i) %></td>
										<td align="center"><%=VDIS_REMITTANCE_NO.elementAt(i) %></td>
										<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
										<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
										<td align="right"><%=VALLOC_QTY.elementAt(i) %> <%=VQTY_UNIT.elementAt(i) %></td>
										<td align="right"><%=VSALES_PRICE.elementAt(i) %></td>
										<td align="center" style="background: #b3f0ff;"><%=VSALES_PRICE_UNIT.elementAt(i) %></td>
										<td align="right"><%=VSALE_AMT.elementAt(i) %></td>	
										<td align="center" style="background: #b3f0ff;"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>																													
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm <%if(isEditable){ %>date fmsdtpick<%} %>" 
						      						name="sap_exchang_dt" id="sap_exchang_dt<%=i%>" value="<%=VEXCHNAGE_RATE_DATE.elementAt(i) %>" maxLength="10" autocomplete="off" 
						      						<%if(!isEditable){ %>readOnly<%} %>>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div>
				      						</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="row">
													<div class="col">
														<input type="text" class="form-control form-control-sm" name="sap_exchang_rate" id="sap_exchang_rate<%=i%>" 
														value="<%=VEXCHNAGE_RATE.elementAt(i) %>" style="text-align: right;" 
														<%if(!isEditable){ %>readOnly<%} %>>
													</div>
													<div class="col-auto">
														<%if(VINVOICE_RAISED_IN.elementAt(i).equals("USD") && VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){%>
															<i class="fa fa-magic fa-lg fa-rotate-270" style="color:gray;"></i>
														<%}else if(VINVOICE_RAISED_IN.elementAt(i).equals("USD") && !VCONTRACT_TYPE.elementAt(i).toString().equals("N")){ %>
															<i class="fa fa-magic fa-lg fa-rotate-270" style="color:#730099;" 
															onclick="doExchngSubmit('<%=i%>','<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>',
															'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VSAP_EXCHANG_FLAG.elementAt(i)%>','<%=VPAYMENT_TYPE_FLAG.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','<%=VINV_FLAG.elementAt(i)%>')"></i>
														<%}else {%>
															<i class="fa fa-magic fa-lg fa-rotate-270" style="color:transparent;"></i>
														<%}%>	
													</div>
												</div>
											</div>
										</td>
										<td align="center" style="background: #b3f0ff;"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
										<td align="right"><%=VGROSS_AMT_USD.elementAt(i) %></td>
										<td align="right"><%=VGROSS_AMT.elementAt(i) %></td>
										<td align="right"><%=VTAX_AMT_USD.elementAt(i) %></td>
										<td align="right"><%=VTAX_AMT.elementAt(i) %></td>
										<td align="right"><%=VINVOICE_AMT_USD.elementAt(i) %></td>
										<td align="right"><%=VINVOICE_AMT.elementAt(i) %></td>
										<td align="center"><%=VTCS_TDS.elementAt(i) %></td>
										<td align="center"><%=VTCS_TDS_FACTOR.elementAt(i)%></td>
										<td align="right"><%=VTCS_TDS_AMT_USD.elementAt(i) %></td>										
										<td align="right"><%=VTCS_TDS_AMT.elementAt(i) %></td>
										<td align="right"><%=VADJ_SIGN.elementAt(i)%><%=VADJ_AMT_USD.elementAt(i)%></td>										
										<td align="right"><%=VADJ_SIGN.elementAt(i)%><%=VADJ_AMT.elementAt(i)%></td>
										<td align="right"><%=VNET_PAYABLE_USD.elementAt(i)%></td>
										<td align="right"><%=VNET_PAYABLE.elementAt(i)%></td>
										<td align="center">
										<%if(approve_access.equals("Y")){ %>																						
											<i class="fa <%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 	
											 	onclick="doGenXML('<%=VCOUNTERPTY_CD.elementAt(i)%>',
											 	'<%=VINVOICE_NO.elementAt(i)%>',
												 '<%=VFINANCIAL_YEAR.elementAt(i)%>',
												 '<%=VINVOICE_SEQ.elementAt(i)%>',
												 '<%=VCONTRACT_TYPE.elementAt(i)%>',
												 '<%=VPAYMENT_TYPE_FLAG.elementAt(i)%>',
												 '<%=VINVOICE_TYPE.elementAt(i)%>',
												 '<%=VSAP_APPROVAL_FLAG.elementAt(i)%>',
												 '<%=VBU_SEQ.elementAt(i)%>',
												 '<%=VCONT_NO.elementAt(i)%>',
												 '<%=VINV_FLAG.elementAt(i)%>');"
												style="<%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
														color: orange;
													<%} else{%>
														color: brown;
												<%}%>">	
											</i>
										<%}else{ %>	
											<i class="fa <%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" style="color:grey;"	></i>								
										<%} %> 												
										</td>
									</tr>
									<%
										if(k==index)
										{
											i=i+1;
											break;
										}
									} %>
								<%}else{ %>
									<tr>
										<td colspan="33" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="financial_year" value="">
<input type="hidden" name="invoice_seq" value="">
<input type="hidden" name="contract_type" value="">
<input type="hidden" name="exchng_dt" value="">
<input type="hidden" name="exchng_rate" value="">
<input type="hidden" name="sap_exchng_flag" value="">
<input type="hidden" name="tdsFactor" value="">
<input type="hidden" name="tdsAmount" value="">
<input type="hidden" name="tdsStructCd" value="">
<input type="hidden" name="tdsEffDt" value="">
<input type="hidden" name="purchase_type_flag" value="">
<input type="hidden" name="invoice_type" value="">
<input type="hidden" name="inv_flag" value="">

<input type="hidden" name="option" value="SAP_EXCHANGE_RATE_UPDATE">

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

</form>
</body>
<script>
$(document).ready(function() {	
	$('.serchtbl').each(function(k){
		$('#tbsearch'+k).each(function(j){						
			$('#tbsearch'+k+' th').each(function(i){
				var title = $(this).text();
				if($(this).hasClass('tbser'+k))
				{
					if(i==21){i=i+3;}
					if(title == "Sr#")
					{
						$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+''+k+'" onkeyup="Search(this,'+i+','+k+');" placeholder="Search '+title+'" style="width:40px"/></div>');
					}
					else
					{
						$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+''+k+'" onkeyup="Search(this,'+i+','+k+');" placeholder="Search '+title+'" style="width:100px"/></div>');
					}
				}
			});		
		});
	});
});
	
function Search(obj, indx, tblid) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+tblid);
  	
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