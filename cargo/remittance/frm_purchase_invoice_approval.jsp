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
	var segment = document.forms[0].segment.value;
	
	var read_access = document.forms[0].read_access.value;
	var write_access = document.forms[0].write_access.value;
	var check_access = document.forms[0].check_access.value;
	var print_access = document.forms[0].print_access.value;
	var delete_access = document.forms[0].delete_access.value;
	var audit_access = document.forms[0].audit_access.value;
	var authorize_access = document.forms[0].authorize_access.value;
	var approve_access = document.forms[0].approve_access.value;
	var execute_access = document.forms[0].execute_access.value;
	var formCd = document.forms[0].form_cd.value;
	var formNm = document.forms[0].form_nm.value;
	var mod_cd = document.forms[0].mod_cd.value;
	var mod_nm = document.forms[0].mod_nm.value;
	
	var url = "frm_purchase_invoice_approval.jsp?form_cd="+formCd+"&form_nm="+formNm+"&segment="+segment+
			"&mod_cd="+mod_cd+"&mod_nm="+mod_nm+"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to+
			"&read_access="+read_access+"&write_access="+write_access+"&check_access="+check_access+
			"&print_access="+print_access+"&delete_access="+delete_access+"&audit_access="+audit_access+
			"&authorize_access="+authorize_access+"&approve_access="+approve_access+"&execute_access="+execute_access;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var newWindow;
function openSapView(financial_year,invoice_seq,contract_type)
{
	var url = "rpt_view_purchase_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+"&contract_type="+contract_type;

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

function doExchngSubmit(index,financial_year,invoice_seq,contract_type,exchng_flag)
{
	var sap_exchng_dt=document.getElementById("sap_exchang_dt"+index).value
	var sap_exchng_rate=document.getElementById("sap_exchang_rate"+index).value
	
	var tds_factor=document.getElementById("tds_factor"+index).value
	var tds_amt=document.getElementById("tds_amt"+index).value
	var tds_struct_cd=document.getElementById("tds_struct_cd"+index).value
	var tds_eff_dt=document.getElementById("tds_eff_dt"+index).value
	
	if(trim(sap_exchng_rate)!="")
	{
		var a=confirm("Do you want to submit?")
		if(a)
		{
			document.forms[0].financial_year.value=financial_year;
			document.forms[0].invoice_seq.value=invoice_seq;
			document.forms[0].contract_type.value=contract_type;
			document.forms[0].exchng_dt.value=sap_exchng_dt;
			document.forms[0].exchng_rate.value=sap_exchng_rate;
			document.forms[0].sap_exchng_flag.value=exchng_flag;
			
			document.forms[0].tdsFactor.value=tds_factor;
			document.forms[0].tdsAmount.value=tds_amt;
			document.forms[0].tdsStructCd.value=tds_struct_cd;
			document.forms[0].tdsEffDt.value=tds_eff_dt
			
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert("Enter Exchange Rate for ROW - "+(parseInt(index)+1))
	}
}

function doApprove(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,purchase_type_flag,invoice_type)
{
	var a=confirm("Your Approval will set the seal on SAP P80 posting!\n\nDo you want to Approve?")
	if(a)
	{
		document.forms[0].counterparty_cd.value=couterpty_cd;
		document.forms[0].invoice_no.value=invoice_no;
		document.forms[0].financial_year.value=financial_year;
		document.forms[0].invoice_seq.value=invoice_seq;
		document.forms[0].contract_type.value=contract_type;
		document.forms[0].purchase_type_flag.value=purchase_type_flag;
		document.forms[0].invoice_type.value=invoice_type;
		
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].option.value="INVOICE_SAP_APPROVE"
		document.forms[0].submit();
	}
}

function doGenXML(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,purchase_type_flag,invoice_type)
{
	/*var a=confirm("Do you want to Generate XML?")
	if(a)
	{
		document.forms[0].counterparty_cd.value=couterpty_cd;
		document.forms[0].invoice_no.value=invoice_no;
		document.forms[0].financial_year.value=financial_year;
		document.forms[0].invoice_seq.value=invoice_seq;
		document.forms[0].contract_type.value=contract_type;
		document.forms[0].purchase_type_flag.value=purchase_type_flag;
		document.forms[0].invoice_type.value=invoice_type;
		
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].option.value="GENERATE_INVOICE_XML"
		document.forms[0].submit();
	}*/
	
	var url = "rpt_view_purchase_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
			"&contract_type="+contract_type+"&purchase_type_flag="+purchase_type_flag+"&invoice_type="+invoice_type+
			"&counterparty_cd="+couterpty_cd+"&invoice_no="+invoice_no;

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
</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String month_to=request.getParameter("month_to")==null?""+currentMonth:request.getParameter("month_to");
String year_to=request.getParameter("year_to")==null?""+currentYear:request.getParameter("year_to");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");

if(month.length() == 1)
{
	month="0"+month; 
}
if(month_to.length() == 1)
{
	month_to="0"+month_to; 
}

purchase.setCallFlag("PURCHASE_INVOICE_APPROVAL");
purchase.setComp_cd(owner_cd);
purchase.setMonth(month);
purchase.setYear(year);
purchase.setMonth_to(month_to);
purchase.setYear_to(year_to);
purchase.setSegment(segment);
purchase.init();

Vector VSEGMENT = purchase.getVSEGMENT();
Vector VSEGMENT_TYPE = purchase.getVSEGMENT_TYPE();

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
Vector VPAYMENT_TYPE_FLAG = purchase.getVPAYMENT_TYPE_FLAG();
Vector VGROSS_AMT_USD = purchase.getVGROSS_AMT_USD();
Vector VTAX_AMT_USD = purchase.getVTAX_AMT_USD();
Vector VINVOICE_AMT_USD = purchase.getVINVOICE_AMT_USD();
Vector VADJ_AMT_USD = purchase.getVADJ_AMT_USD();
Vector VNET_PAYABLE_USD = purchase.getVNET_PAYABLE_USD();
Vector VTCS_TDS_AMT_USD = purchase.getVTCS_TDS_AMT_USD();
Vector VTCS_TDS_STRUCT_CD = purchase.getVTCS_TDS_STRUCT_CD();
Vector VTCS_TDS_EFF_DT = purchase.getVTCS_TDS_EFF_DT();
Vector VTCS_TDS_DONE = purchase.getVTCS_TDS_DONE();
Vector VINVOICE_TYPE =purchase.getVINVOICE_TYPE();

Vector VCOUNTERPTY_CD = purchase.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = purchase.getVCOUNTERPTY_ABBR();
Vector VINDEX = purchase.getVINDEX();
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
					    	Purchase Invoice Approval
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
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>Month/Year</b></label>
					  		</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="month" onchange="refresh();">
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
					  				<select class="form-select form-select-sm" name="year" onchange="refresh();">
					  					<%for(int i=(currentYear+1); i > (currentYear-10);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
							</div>
						</div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>to</b></label>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col">
					  				<select class="form-select form-select-sm" name="month_to" onchange="refresh();">
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
					  				<select class="form-select form-select-sm" name="year_to" onchange="refresh();">
					  					<%for(int i=(currentYear+1); i > (currentYear-10);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year_to.value="<%=year_to%>"</script>
								</div>
							</div>
				  		</div>
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
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
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Trader</th>
										<th><%=owner_abbr%> BU</th>
										<th>Contract#</th>
										<th>Billing Period</th>
										<th>Invoice#</th>
										<th>Invoice Date</th>
										<th>Invoice MMBTU</th>
										<th>Rate</th>
										<th>Rate Unit</th>
										<th>Invoice Raised In</th>
										<th>Exchange Rate Date</th>
										<th>Exchange Rate</th>
										<th style="background: #000066; color: white;">Invoice Paid In</th>
										<th>Gross Amount</th>
										<th>Tax</th>
										<th>Invoice Amount</th>
										<th>TCS/TDS</th>
										<th>TCS/TDS %</th>
										<th>+TCS/-TDS Amount</th>
										<th>Adjust Amount</th>
										<th>Net Payable</th>
										<!-- <th>Prepare</th> -->
										<th>FO Approval</th>
									</tr>
								</thead>
								<tbody>
								<%k=0;
								if(index > 0){ %>
									<%for(i=i; i<VINVOICE_SEQ.size(); i++){ 
										k+=1;
										
										boolean isEditable=false;
										if((!VEXCHNAGE_RATE.elementAt(i).equals("") && !VSAP_EXCHANG_FLAG.elementAt(i).equals("Y")) || VSAP_APPROVAL_FLAG.elementAt(i).equals("Y"))
										{
											isEditable=true;
										}
									%>
									<tr>
										<td align="center"><%=VCOUNTERPTY_ABBR.elementAt(i) %></td>
										<td align="center"><%=VBU_NM.elementAt(i) %></td>
										<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
										<td align="center"><%=VPERIOD_START_DT.elementAt(i) %> - <%=VPERIOD_END_DT.elementAt(i) %></td>
										<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
										<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
										<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
										<td align="right"><%=VSALES_PRICE.elementAt(i) %></td>
										<td align="center"><%=VSALES_PRICE_UNIT.elementAt(i) %></td>
										<td align="center"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>
										<%-- <td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm <%if(isEditable){ %>date fmsdtpick<%} %>" 
						      						name="sap_exchang_dt" id="sap_exchang_dt<%=i%>" value="<%=VEXCHNAGE_RATE_DATE.elementAt(i) %>" maxLength="10" autocomplete="off" 
						      						<%if(isEditable){ %>readOnly<%} %>>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div>
				      						</div>
										</td>
										<td align="center">
											<div style="width:75px;">
												<input type="text" class="form-control form-control-sm" name="sap_exchang_rate" id="sap_exchang_rate<%=i%>" 
												value="<%=VEXCHNAGE_RATE.elementAt(i) %>" style="text-align: right;" 
												<%if(isEditable){ %>readOnly<%} %>>
											</div>
										</td> --%>
										<td align="center"><%=VEXCHNAGE_RATE_DATE.elementAt(i) %></td>
										<td align="right"><%=VEXCHNAGE_RATE.elementAt(i) %></td>
										<td align="center" style="background: #b3f0ff;"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
										<td align="right"><%=VGROSS_AMT.elementAt(i) %></td>
										<td align="right"><%=VTAX_AMT.elementAt(i) %></td>
										<td align="right"><%=VINVOICE_AMT.elementAt(i) %></td>
										<td align="center"><%=VTCS_TDS.elementAt(i) %></td>
										<%-- <%if(VTCS_TDS.elementAt(i).equals("TDS")){ %>
										<td align="center">
											<div style="width:50px;">
												<input type="text" class="form-control form-control-sm" name="tds_factor" id="tds_factor<%=i%>" 
												value="<%=VTCS_TDS_FACTOR.elementAt(i) %>" style="text-align: right;" readOnly>
											</div>
										</td>
										<td align="center">
											<div style="width:80px;">
												<input type="text" class="form-control form-control-sm" name="tds_amt" id="tds_amt<%=i%>" 
												value="<%=VTCS_TDS_AMT.elementAt(i) %>" style="text-align: right;" readOnly>
												
												<input type="hidden" name="tds_struct_cd" id="tds_struct_cd<%=i%>" value="<%=VTCS_TDS_STRUCT_CD.elementAt(i) %>">
												<input type="hidden" name="tds_eff_dt" id="tds_eff_dt<%=i%>" value="<%=VTCS_TDS_EFF_DT.elementAt(i) %>">
											</div>
										</td>
										<%}else{ %> --%>
										<td align="right"><%=VTCS_TDS_FACTOR.elementAt(i) %></td>
										<td align="right">
											<%=VTCS_TDS_AMT.elementAt(i) %>
											<input type="hidden" name="tds_factor" id="tds_factor<%=i%>" value="">
											<input type="hidden" name="tds_amt" id="tds_amt<%=i%>" value="">
											<input type="hidden" name="tds_struct_cd" id="tds_struct_cd<%=i%>" value="">
											<input type="hidden" name="tds_eff_dt" id="tds_eff_dt<%=i%>" value="">
										</td>
										<%-- <%} %> --%>
										<td align="right"><%=VADJ_SIGN.elementAt(i)%><%=VADJ_AMT.elementAt(i)%></td>
										<td align="right"><%=VNET_PAYABLE.elementAt(i)%></td>
										<%-- <td align="center">
											<%if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y") && VSAP_EXCHANG_FLAG.elementAt(i).equals("Y")){%>
												<i class="fa fa-cogs fa-2x" style="color:gray;"></i>
											<%}else if(VSAP_EXCHANG_FLAG.elementAt(i).equals("Y") || VTCS_TDS.elementAt(i).equals("TDS")){ %>
												<i class="fa fa-cogs fa-2x" style="color:#008080;" 
												onclick="doExchngSubmit('<%=i%>','<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>',
												'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VSAP_EXCHANG_FLAG.elementAt(i)%>')"></i>
											<%} %>
										</td> --%>
										<td align="center">
											<input type="button"  
											<%if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
												 class="btn btn-success" value="Approved"
											<%}else{%>
												 class="btn btn-warning com-btn" value="Approve" onclick="doGenXML('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
												 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
												 '<%=VPAYMENT_TYPE_FLAG.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>');"
											<%} %>
											>
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
										<td colspan="23" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Month!</b>") %></td>
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

<input type="hidden" name="counterparty_cd" value="">
<input type="hidden" name="invoice_no" value="">
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

</form>
</body>
</html>