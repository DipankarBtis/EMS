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
	var segment = document.forms[0].segment.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_remittance_sun_accounting approval.jsp?u="+u+"&segment="+segment+"&from_dt="+from_dt+"&to_dt="+to_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function refresh_exchng_rate(remittance_no,i)
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var segment = document.forms[0].segment.value;
	
	var u = document.forms[0].u.value;
	var exchng_dt = document.getElementById("sap_exchang_dt"+i).value;
	
	var url = "frm_remittance_sun_accounting approval.jsp?u="+u+"&segment="+segment+"&from_dt="+from_dt+"&to_dt="+to_dt+"&remittance_no="+remittance_no+"&exchng_rate_dt="+exchng_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doApprove(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,purchase_type_flag,invoice_type,sap_approval_flag, bu_seq, cont_no, inv_flag,bu_tin,isEdit,exchng_rate_dt,exchng_rate,exchng_rate_config,act_arrv_dt)
{
	var flag=true;
	if(isEdit=='True'||isEdit=='true')
	{
		if(inv_flag=='F' && (purchase_type_flag=='S'|| purchase_type_flag=='P'||purchase_type_flag=='FF') && contract_type!='N')
		{
			if(exchng_rate=='')
			{
				flag=false;
				alert("Configure Exchange Rate for the given date ("+exchng_rate_dt+")!");
			}
		}
	}
	if(exchng_rate_config=='N')
	{
		flag=false;
		alert("Exchange Rate is not configured for the given invoice date("+act_arrv_dt+")!");
	}
	if(flag==true)
	{
		var a=confirm("Do you want to approve invoice("+invoice_no+")?");
		if(a)
		{
			document.forms[0].counterparty_cd.value=couterpty_cd;
			document.forms[0].invoice_no.value=invoice_no;
			document.forms[0].financial_year.value=financial_year;
			document.forms[0].invoice_seq.value=invoice_seq;
			document.forms[0].contract_type.value=contract_type;
			document.forms[0].purchase_type_flag.value=purchase_type_flag;
			document.forms[0].invoice_type.value=invoice_type;
			document.forms[0].inv_flag.value=inv_flag;
			document.forms[0].bu_state_tin.value=bu_tin;
			document.forms[0].exchng_rate.value=exchng_rate;
			document.forms[0].exchng_dt.value=exchng_rate_dt;
			document.forms[0].option.value="APPROVE_PURCHASE_INVOICE_SUN_XML";
			document.forms[0].submit();
		}
	}
}

function doReApprove(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,purchase_type_flag,invoice_type,sap_approval_flag, bu_seq, cont_no, inv_flag,bu_tin,isEdit,exchng_rate_dt,exchng_rate,exchng_rate_config,act_arrv_dt)
{
	var flag=true;
	if(isEdit=='True'||isEdit=='true')
	{
		if(inv_flag=='F' && (purchase_type_flag=='S'|| purchase_type_flag=='P'||purchase_type_flag=='FF') && contract_type!='N')
		{
			if(exchng_rate=='')
			{
				flag=false;
				alert("Configure Exchange Rate for the given date ("+exchng_rate_dt+")!");
			}
		}
	}
	if(exchng_rate_config=='N')
	{
		flag=false;
		alert("Exchange Rate is not configured for the given invoice date("+act_arrv_dt+")!");
	}
	if(flag==true)
	{
		var a=confirm("Do you want to re-approve invoice("+invoice_no+")?");
		if(a)
		{
			document.forms[0].counterparty_cd.value=couterpty_cd;
			document.forms[0].invoice_no.value=invoice_no;
			document.forms[0].financial_year.value=financial_year;
			document.forms[0].invoice_seq.value=invoice_seq;
			document.forms[0].contract_type.value=contract_type;
			document.forms[0].purchase_type_flag.value=purchase_type_flag;
			document.forms[0].invoice_type.value=invoice_type;
			document.forms[0].inv_flag.value=inv_flag;
			document.forms[0].bu_state_tin.value=bu_tin;
			document.forms[0].exchng_rate.value=exchng_rate;
			document.forms[0].exchng_dt.value=exchng_rate_dt;
			document.forms[0].option.value="REAPPROVE_PURCHASE_INVOICE_SUN_XML";
			document.forms[0].submit();
		}
	}
}
</script>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sun_interface" id="accounting" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");
String exchng_rate_dt=request.getParameter("exchng_rate_dt")==null?"":request.getParameter("exchng_rate_dt");
String remittance_no=request.getParameter("remittance_no")==null?"":request.getParameter("remittance_no");

accounting.setCallFlag("REMITTANCE_SUN_APPROVAL");
accounting.setComp_cd(owner_cd);
accounting.setFrom_dt(from_dt);
accounting.setTo_dt(to_dt);
accounting.setSegment(segment);
accounting.setRemittance_no(remittance_no);
accounting.setExchng_rate_dt(exchng_rate_dt);
accounting.init();

Vector VSEGMENT = accounting.getVSEGMENT();
Vector VSEGMENT_TYPE = accounting.getVSEGMENT_TYPE();
Vector VTEMP_SEGMENT = accounting.getVTEMP_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = accounting.getVTEMP_SEGMENT_TYPE();
Vector VCONT_NO = accounting.getVCONT_NO();
Vector VINVOICE_DT = accounting.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = accounting.getVINVOICE_DUE_DT();
Vector VINVOICE_NO = accounting.getVINVOICE_NO();
Vector VINVOICE_SEQ = accounting.getVINVOICE_SEQ();
Vector VPERIOD_START_DT = accounting.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = accounting.getVPERIOD_END_DT();
Vector VALLOC_QTY = accounting.getVALLOC_QTY();
Vector VSALES_PRICE = accounting.getVSALES_PRICE();
Vector VSALES_PRICE_UNIT = accounting.getVSALES_PRICE_UNIT();
Vector VGROSS_AMT = accounting.getVGROSS_AMT();
Vector VTAX_AMT = accounting.getVTAX_AMT();
Vector VINVOICE_AMT = accounting.getVINVOICE_AMT();
Vector VEXCHNAGE_RATE = accounting.getVEXCHNAGE_RATE();
Vector VEXCHNAGE_RATE_DATE = accounting.getVEXCHNAGE_RATE_DATE();
Vector VBU_NM = accounting.getVBU_NM();
Vector VBU_SEQ = accounting.getVBU_SEQ();
//Vector VDEAL_NO = accounting.getVDEAL_NO();
Vector VSALE_AMT = accounting.getVSALE_AMT();
Vector VADJ_SIGN = accounting.getVADJ_SIGN();
Vector VADJ_AMT = accounting.getVADJ_AMT();
Vector VNET_PAYABLE = accounting.getVNET_PAYABLE();
Vector VTCS_TDS = accounting.getVTCS_TDS();
Vector VTCS_TDS_AMT = accounting.getVTCS_TDS_AMT();
Vector VTCS_TDS_FACTOR = accounting.getVTCS_TDS_FACTOR();
Vector VINVOICE_RAISED_IN = accounting.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = accounting.getVPAYMENT_DONE_IN();
Vector VFINANCIAL_YEAR = accounting.getVFINANCIAL_YEAR();
Vector VCONTRACT_TYPE = accounting.getVCONTRACT_TYPE();
Vector VSAP_EXCHANG_FLAG = accounting.getVSAP_EXCHANG_FLAG();
Vector VSAP_APPROVAL_FLAG = accounting.getVSAP_APPROVAL_FLAG();
Vector VINV_FLAG = accounting.getVINV_FLAG();

Vector VINVOICE_TYPE =accounting.getVINVOICE_TYPE();
Vector VPAYMENT_TYPE_FLAG = accounting.getVPAYMENT_TYPE_FLAG();
Vector VPAYMENT_TYPE_NM= accounting.getVPAYMENT_TYPE_NM();

Vector VGROSS_AMT_USD = accounting.getVGROSS_AMT_USD();
Vector VTAX_AMT_USD = accounting.getVTAX_AMT_USD();
Vector VINVOICE_AMT_USD = accounting.getVINVOICE_AMT_USD();
Vector VADJ_AMT_USD = accounting.getVADJ_AMT_USD();
Vector VNET_PAYABLE_USD = accounting.getVNET_PAYABLE_USD();
Vector VTCS_TDS_AMT_USD = accounting.getVTCS_TDS_AMT_USD();
Vector VTCS_TDS_STRUCT_CD = accounting.getVTCS_TDS_STRUCT_CD();
Vector VTCS_TDS_EFF_DT = accounting.getVTCS_TDS_EFF_DT();
Vector VTCS_TDS_DONE = accounting.getVTCS_TDS_DONE();

Vector VCOUNTERPARTY_CD = accounting.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = accounting.getVCOUNTERPARTY_ABBR();
Vector VINDEX = accounting.getVINDEX();
Vector VCASH_FLOW = accounting.getVCASH_FLOW();
Vector VDIS_REMITTANCE_NO = accounting.getVDIS_REMITTANCE_NO();
Vector VDISP_INVOICE_NO = accounting.getVINVOICE_NO();
Vector VQTY_UNIT = accounting.getVQTY_UNIT();
Vector VSPLIT_VALUE = accounting.getVSPLIT_VALUE();
Vector VDIS_CONT_MAPPING = accounting.getVDIS_CONT_MAPPING();
Vector VBU_STATE_TIN = accounting.getVBU_STATE_TIN();
Vector VEXCHNG_RATE_CONFIG = accounting.getVEXCHNG_RATE_CONFIG();
Vector VACT_ARRIVAL_DT = accounting.getVACT_ARRIVAL_DT();
Vector VTRANS_DT = accounting.getVTRANS_DT();

Vector VAPPROVE_HIST=accounting.getVAPPROVE_HIST();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_sun_interface">
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
					    	Purchase Actual SUN Approval
					    </div>
					    <div class="btn-group">
							<select class="btn btn-outline-secondary btngrp btnactive" name="segment" onchange="refresh();">
								<option value="">All</option>
								<%for(int i=0;i<VSEGMENT.size();i++){ %>
								<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT.elementAt(i)%></option>
								<%} %>
							</select>
						</div>
						<script>document.forms[0].segment.value="<%=segment%>"</script>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-10 col-xs-10 col-md-10">
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
						<!-- <div class="col-sm-2 col-xs-2 col-md-2">
							<div class="d-flex justify-content-end">
								<div class="form-group row">
									<div class="col-auto">
										<div align="right" onclick="exportToXls();" style="color:green;">
											<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
										</div>
									</div>
								</div>
							</div>
						</div> -->
					</div>
				</div>
				<div class="card-body cdbody">
				<%
					int k=0;
					int i=0;
					int l=0;			
					for(l=0;l<VTEMP_SEGMENT_TYPE.size();l++){
					int index = Integer.parseInt(""+VINDEX.elementAt(l));%>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading1">
										<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l %>" aria-expanded="false" aria-controls="collapse<%=l%>"><%=VTEMP_SEGMENT.elementAt(l) %>&nbsp;<font color="blue">(<%=index%> Items)</font></button>	
							    	</h2>
							    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading1">
							    		<div class="accordion-body accor-body">
											<div class="row">
												<div class="table-responsive">
													<table class="table table-bordered table-hover" id="example<%=l%>">
														<thead id="tbsearch<%=l%>">
															<tr>
																<th rowspan="2">SR#</th>
																<th rowspan="2">Trader</th>
																<th rowspan="2"><%=owner_abbr%> BU</th>
																<th rowspan="2">Contract#</th>
																<!-- <th rowspan="2">Contract/Trade Ref#</th> -->
																<th rowspan="2">Billing Period</th>
																<th rowspan="2">Cash Flow</th>
																<th rowspan="2">Invoice#</th>
																<th rowspan="2">Remittance#</th>
																<th rowspan="2">Invoice Date</th>
																<th rowspan="2">Invoice Due Date</th>
																<th rowspan="2">Invoiced Quantity</th>
																<th rowspan="2">Rate</th>
																<th rowspan="2" style="background: #000066; color: white;">Rate Unit / MMBTU</th>
																<th rowspan="2">Sales Amount</th>
																<th rowspan="2" style="background: #000066; color: white;">Invoice Raised In</th>
																<th rowspan="2" <%if(VTEMP_SEGMENT_TYPE.elementAt(l).equals("V")){%>style="display:none;"<%}%>>Exchange Rate Date</th>
																<th rowspan="2" <%if(VTEMP_SEGMENT_TYPE.elementAt(l).equals("V")){%>style="display:none;"<%}%>>Exchange Rate</th>
																<th rowspan="2" <%if(!VTEMP_SEGMENT_TYPE.elementAt(l).equals("F")){%>style="display:none;"<%}%>>Transaction Date</th>
																<th rowspan="2" style="background: #000066; color: white;">Invoice Paid In</th>
																<th colspan="2">Gross Amount</th>
																<th colspan="2">Tax </th>
																<th colspan="2">Invoice Amount</th>
																<th rowspan="2">TCS/TDS</th>
																<th rowspan="2">TCS/TDS%</th>
																<th colspan="2">+TCS/-TDS Amount</th>
																<th colspan="2">Adjust Amount</th>
																<th colspan="2">Net Payable</th>
																<th rowspan="2">SUN Approval</th>
																<th rowspan="2">SUN Re-Approval</th>
																<th rowspan="2">SUN Approved By/On</th>
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
														<% k=0;
															if(index > 0){ %>
															<%for(i=i; i<VCOUNTERPARTY_CD.size(); i++){
																boolean isEditable=false;
																if((VINVOICE_RAISED_IN.elementAt(i).equals("USD") && !VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")) && !VCONTRACT_TYPE.elementAt(i).toString().equals("N"))
																{
																	isEditable=true; 
																}
																k+=1;
																%>
															<tr>
																<td align="center"><%=k%></td>
																<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%>">
																	<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
																</td>
																<td align="center"><%=VBU_NM.elementAt(i) %></td>
																<td align="center"><%if(VTEMP_SEGMENT_TYPE.elementAt(l).equals("V")){%><div style="width:400px; word-wrap: break-word; white-space: normal;"><%}%><%=VDIS_CONT_MAPPING.elementAt(i) %><%if(VTEMP_SEGMENT_TYPE.elementAt(l).equals("V")){%></div><%} %></td>
																<%-- <td align="center"><%=VCONT_REF_NO.elementAt(i) %></td> --%>
																<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
																<td align="center"><%=VCASH_FLOW.elementAt(i)%></td>
																<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
																<td align="center"><%=VDIS_REMITTANCE_NO.elementAt(i)%></td>
																<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
																<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
																<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
																<td align="right"><%=VSALES_PRICE.elementAt(i)%></td>
																<td align="center" style="background: #b3f0ff;"><%=VSALES_PRICE_UNIT.elementAt(i) %></td>
																<td align="right"><%=VSALE_AMT.elementAt(i) %></td>
																<td style="background: #b3f0ff;"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>
																<td align="center" <%if(VTEMP_SEGMENT_TYPE.elementAt(l).equals("V")){%>style="display:none;"<%}%>>
																	<div style="width:100px;">
																		<div class="input-group input-group-sm" >
												      						<input type="text" class="form-control form-control-sm <%if(isEditable){ %>date fmsdtpick<%} %>" 
												      						name="sap_exchang_dt" id="sap_exchang_dt<%=i%>" value="<%=VEXCHNAGE_RATE_DATE.elementAt(i) %>" maxLength="10" autocomplete="off" 
												      						<%if(!isEditable){ %>readOnly<%} %> onchange="refresh_exchng_rate('<%=VDIS_REMITTANCE_NO.elementAt(i)%>','<%=i%>')">
												      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
											      						</div>
										      						</div>
																</td>
																<td align="center" <%if(VTEMP_SEGMENT_TYPE.elementAt(l).equals("V")){%>style="display:none;"<%}%>>
																	<div style="width:100px;">
																		<div class="row">
																			<div class="col">
																				<input type="text" class="form-control form-control-sm" name="sap_exchang_rate" id="sap_exchang_rate<%=i%>" 
																				value="<%=VEXCHNAGE_RATE.elementAt(i) %>" style="text-align: right;" 
																				<%if(!isEditable){ %>readOnly<%} %>>
																			</div>
																			<%-- <div class="col-auto">
																				<%if(VINVOICE_RAISED_IN.elementAt(i).equals("USD") && VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){%>
																					<i class="fa fa-magic fa-lg fa-rotate-270" style="color:gray;"></i>
																				<%}else if(VINVOICE_RAISED_IN.elementAt(i).equals("USD") && !VCONTRACT_TYPE.elementAt(i).toString().equals("N")){ %>
																					<i class="fa fa-magic fa-lg fa-rotate-270" style="color:#730099;" 
																					onclick="doExchngSubmit('<%=i%>','<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>',
																					'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VSAP_EXCHANG_FLAG.elementAt(i)%>','<%=VPAYMENT_TYPE_FLAG.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','<%=VINV_FLAG.elementAt(i)%>')"></i>
																				<%}else {%>
																					<i class="fa fa-magic fa-lg fa-rotate-270" style="color:transparent;"></i>
																				<%}%>	
																			</div> --%>
																		</div>
																	</div>
																</td>
																<td align="center" <%if(!VTEMP_SEGMENT_TYPE.elementAt(l).equals("F")){%>style="display:none;"<%}%>>
																	<div style="width:100px;">
																		<div class="input-group input-group-sm" >
																			<input type="text" class="form-control form-control-sm " 
														      						 value="<%=VTRANS_DT.elementAt(i)%>" maxLength="10" autocomplete="off" 
														      						readOnly>
														      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
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
																<%if(owner_cd.equals("1")){ %>
																			<span class="fa-stack fa-lg">
																			  <i class="fa fa-flag fa-stack-1x"></i>
																			  <i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
																			</span>
																<%}else{%>										 
																	<%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y") && approve_access.equals("Y")){ %>
																		<!-- <i class="fa fa-sun-o fa-2x " aria-hidden="true" value="Approve" -->
																		<i class="fa fa-flag fa-2x" aria-hidden="true" value="Approve"
																	 	onclick="doApprove('<%=VCOUNTERPARTY_CD.elementAt(i)%>',
																	 		'<%=VINVOICE_NO.elementAt(i)%>',
																		 	'<%=VFINANCIAL_YEAR.elementAt(i)%>',
																		 	'<%=VINVOICE_SEQ.elementAt(i)%>',
																		 	'<%=VCONTRACT_TYPE.elementAt(i)%>',
																			'<%=VPAYMENT_TYPE_FLAG.elementAt(i)%>',
																			'<%=VINVOICE_TYPE.elementAt(i)%>',
																			'<%=VSAP_APPROVAL_FLAG.elementAt(i)%>',
																			'<%=VBU_SEQ.elementAt(i)%>',
																			'<%=VCONT_NO.elementAt(i)%>',
																			'<%=VINV_FLAG.elementAt(i)%>',
																			'<%=VBU_STATE_TIN.elementAt(i)%>',
																			'<%=isEditable%>',
																			'<%=VEXCHNAGE_RATE_DATE.elementAt(i) %>',
																			'<%=VEXCHNAGE_RATE.elementAt(i) %>',
																			'<%=VEXCHNG_RATE_CONFIG.elementAt(i) %>',
																			'<%=VACT_ARRIVAL_DT.elementAt(i) %>'
																		 	);"
																		 style="color:#00cc00;"> </i>
																	<%}else{ %> 
																		<i class="fa fa-flag fa-2x" style="pointer-events: none; opacity: .65; color: gray;"></i>
																		<!--  style="color:#ff6600;"></i> -->
																	<%} %>
																<%} %>	
																</td>	
																<td align="center">
																	<%if(owner_cd.equals("1")){ %>
																			<span class="fa-stack fa-lg">
																			  <i class="fa fa-flag fa-stack-1x"></i>
																			  <i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
																			</span>
																	<%}else{%>
																		<%if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y") && approve_access.equals("Y")){ %>
																			<i class="fa fa-thumbs-o-up fa-2x" aria-hidden="true" value="Approve"
																	 		onclick="doReApprove('<%=VCOUNTERPARTY_CD.elementAt(i)%>',
																	 		'<%=VINVOICE_NO.elementAt(i)%>',
																		 	'<%=VFINANCIAL_YEAR.elementAt(i)%>',
																		 	'<%=VINVOICE_SEQ.elementAt(i)%>',
																		 	'<%=VCONTRACT_TYPE.elementAt(i)%>',
																			'<%=VPAYMENT_TYPE_FLAG.elementAt(i)%>',
																			'<%=VINVOICE_TYPE.elementAt(i)%>',
																			'<%=VSAP_APPROVAL_FLAG.elementAt(i)%>',
																			'<%=VBU_SEQ.elementAt(i)%>',
																			'<%=VCONT_NO.elementAt(i)%>',
																			'<%=VINV_FLAG.elementAt(i)%>',
																			'<%=VBU_STATE_TIN.elementAt(i)%>',
																			'<%=isEditable%>',
																			'<%=VEXCHNAGE_RATE_DATE.elementAt(i) %>',
																			'<%=VEXCHNAGE_RATE.elementAt(i) %>',
																			'<%=VEXCHNG_RATE_CONFIG.elementAt(i) %>',
																			'<%=VACT_ARRIVAL_DT.elementAt(i) %>'
																		 	);"
																		 	style="color:#00cc00;"> </i>
																		<%}else{%> 
																			<i class="fa fa-thumbs-o-up fa-2x" style="pointer-events: none; opacity: .65; color: gray;"></i>
																			<!--  style="color:#ff6600;"></i> -->
																		<%} %>
																	<%} %>
																</td>
																<td align="center">
																<%if(owner_cd.equals("1")){ %>
																	Not Applicable		
																<%}else{%>
																	<%=VAPPROVE_HIST.elementAt(i) %>
																<%} %>
																</td>		 											
															</tr>
																<%if(k==index)
																{i=i+1;
																	break;
																} %>
															<%}%>
														<%}else{ %>
															<tr>
																<td colspan="35" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
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
				<%}%>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="counterparty_cd" value="">
<input type="hidden" name="bu_state_tin" value="">
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
<input type="hidden" name="type_flag" value="">
<input type="hidden" name="invoice_type" value="">
<input type="hidden" name="invoice_no" value="">
<input type="hidden" name="option" value="">
<input type="hidden" name="purchase_type_flag" value="">
<input type="hidden" name="cont_no" value="">
<input type="hidden" name="inv_flag" value="">
<input type="hidden" name="acc_size" value="<%=VINDEX.size()%>">

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
	var acc_size=document.forms[0].acc_size.value; 
	for(k=0;k<acc_size;k++)
	{
		$('#tbsearch'+k+' th').each(function(i){
			//alert(i)
			var title = $(this).text();
			if(title == "Sr#")
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