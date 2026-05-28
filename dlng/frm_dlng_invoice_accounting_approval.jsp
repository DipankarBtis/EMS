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
	var chk = document.forms[0].chk;	// SagarB20251006 Added this for passing the value of checkbox
	if (chk.checked == true) {
		chk = "T";
	}
	else {
		chk = "F";
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_invoice_accounting_approval.jsp?u="+u+"&segment="+segment+
			"&from_dt="+from_dt+"&to_dt="+to_dt+"&chk="+chk;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function enable_disable(index)
{
	var chk = document.getElementById("chk"+index);
	
	var counterparty_cd = document.getElementById("counterparty_cd"+index);
	var agmt_no = document.getElementById("agmt_no"+index);
	var agmt_rev_no = document.getElementById("agmt_rev_no"+index);
	var cont_no = document.getElementById("cont_no"+index);
	var cont_rev_no = document.getElementById("cont_rev_no"+index);
	var contract_type = document.getElementById("contract_type"+index);
	var financial_year = document.getElementById("financial_year"+index);
	var invoice_seq = document.getElementById("invoice_seq"+index);
	var bu_state_tin = document.getElementById("bu_state_tin"+index);
	var tds_on_gross = document.getElementById("tds_on_gross"+index);
	var tds_on_gross_amt = document.getElementById("tds_on_gross_amt"+index);
	var tds_on_tax = document.getElementById("tds_on_tax"+index);
	var tds_on_tax_amt = document.getElementById("tds_on_tax_amt"+index);
	var net_payable_amt = document.getElementById("net_payable_amt"+index);
	var pay_received_amt = document.getElementById("pay_received_amt"+index);
	var short_received = document.getElementById("short_received"+index);
	var pay_received_dt = document.getElementById("pay_received_dt"+index);
	var remark = document.getElementById("remark"+index);
	
	if(chk.checked)
	{
		counterparty_cd.disabled=false;
		agmt_no.disabled=false;
		agmt_rev_no.disabled=false;
		cont_no.disabled=false;
		cont_rev_no.disabled=false;
		contract_type.disabled=false;
		financial_year.disabled=false;
		invoice_seq.disabled=false;
		bu_state_tin.disabled=false;
		tds_on_gross.disabled=false;
		tds_on_gross_amt.disabled=false;
		tds_on_tax.disabled=false;
		tds_on_tax_amt.disabled=false;
		net_payable_amt.disabled=false;
		pay_received_amt.disabled=false;
		short_received.disabled=false;
		pay_received_dt.disabled=false;
		remark.disabled=false;
	}
	else
	{
		counterparty_cd.disabled=true;
		agmt_no.disabled=true;
		agmt_rev_no.disabled=true;
		cont_no.disabled=true;
		cont_rev_no.disabled=true;
		contract_type.disabled=true;
		financial_year.disabled=true;
		invoice_seq.disabled=true;
		bu_state_tin.disabled=true;
		tds_on_gross.disabled=true;
		tds_on_gross_amt.disabled=true;
		tds_on_tax.disabled=true;
		tds_on_tax_amt.disabled=true;
		net_payable_amt.disabled=true;
		pay_received_amt.disabled=true;
		short_received.disabled=true;
		pay_received_dt.disabled=true;
		remark.disabled=true;
	}
}

function calcPayableAmt(index)
{
	var gross_amt = document.getElementById("gross_amt"+index);
	var tds_on_gross = document.getElementById("tds_on_gross"+index);
	var tds_on_gross_amt = document.getElementById("tds_on_gross_amt"+index);
	
	var tax_amt = document.getElementById("tax_amt"+index);
	var tds_on_tax = document.getElementById("tds_on_tax"+index);
	var tds_on_tax_amt = document.getElementById("tds_on_tax_amt"+index);
	
	var invoice_amt = document.getElementById("invoice_amt"+index);
	var net_payable_amt = document.getElementById("net_payable_amt"+index);
	var pay_received_amt = document.getElementById("pay_received_amt"+index);
	var short_received = document.getElementById("short_received"+index);

	if(trim(gross_amt.value)!="" && trim(tds_on_gross.value)!="")
	{
		var tds_amt = parseFloat("0");
		
		tds_amt=parseFloat(gross_amt.value) * parseFloat(tds_on_gross.value) / 100;
		
		if(!isNaN(tds_amt))
		{
			tds_on_gross_amt.value=round(parseFloat(tds_amt),2)
		}
		else
		{
			tds_on_gross_amt.value="";
		}
	}
	else
	{
		tds_on_gross_amt.value="";
	}
	
	//calc tds on tax
	if(trim(tax_amt.value)!="" && trim(tds_on_tax.value)!="")
	{
		var tds_amt = parseFloat("0");
		
		tds_amt=parseFloat(tax_amt.value) * parseFloat(tds_on_tax.value) / 100;
		
		if(!isNaN(tds_amt))
		{
			tds_on_tax_amt.value=round(parseFloat(tds_amt),2)
		}
		else
		{
			tds_on_tax_amt.value="";
		}
	}
	else
	{
		tds_on_tax_amt.value="";
	}
	
	//calc net receivable
	if(trim(invoice_amt.value)!="")
	{
		var net_recv_amt = parseFloat(invoice_amt.value);
		
		if(trim(tds_on_gross.value)!="")
		{
			net_recv_amt=parseFloat(net_recv_amt) - parseFloat(tds_on_gross_amt.value);
		}
		
		if(trim(tds_on_tax.value)!="")
		{
			net_recv_amt=parseFloat(net_recv_amt) - parseFloat(tds_on_tax_amt.value);
		}
		
		if(!isNaN(net_recv_amt))
		{
			net_payable_amt.value=round(parseFloat(net_recv_amt),2)
		}
		else
		{
			net_payable_amt.value="";
		}
	}
	
	//calc short receive
	if(trim(pay_received_amt.value)!="")
	{
		var short_amt = round(parseFloat(net_payable_amt.value)-parseFloat(pay_received_amt.value),2);
		
		if(!isNaN(short_amt))
		{
			short_received.value=round(parseFloat(short_amt),2)
		}
		else
		{
			short_received.value="";
		}
	}
	else
	{
		short_received.value=net_payable_amt.value;
	}
}

function doSubmit()
{
	var chk = document.forms[0].chk;
	
	var pay_received_amt = document.forms[0].pay_received_amt;
	var pay_received_dt = document.forms[0].pay_received_dt;
	
	var msg="";
	var flag=true;
	
	var countChk=parseInt("0");
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					countChk=parseInt(countChk) + 1;
					
					if(trim(pay_received_amt[i].value) == "")
					{
						msg+="Enter Actual Received for ROW - "+(praseInt(i)+1)+"!\n";
						flag=false;
					}
					if(trim(pay_received_dt[i].value) == "")
					{
						msg+="Enter Received Date for ROW - "+(praseInt(i)+1)+"!\n";
						flag=false;
					}
				}
			}
		}
		else
		{
			if(chk.checked)
			{
				countChk=parseInt(countChk) + 1;
				
				if(trim(pay_received_amt.value) == "")
				{
					msg+="Enter Actual Received for ROW - 1!\n";
					flag=false;
				}
				if(trim(pay_received_dt.value) == "")
				{
					msg+="Enter Received Date for ROW - 1!\n";
					flag=false;
				}
			}
		}
	}
	
	if(parseInt(countChk)<=0)
	{
		alert("Please Select atleast one(1) ROW for Submit!");
	}
	else
	{
		if(flag)
		{
			var a = confirm("Do you want to Submit?")
			if(a)
			{
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].submit();
			}
		}
		else
		{
			alert(msg);
		}
	}
}

var newWindow;

function doGenXML(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,type_flag,invoice_type,bu_state_tin,sap_approval_flag,agmt_no,cont_no)
{
	/*var a=confirm("Do you want to Generate XML?")
	if(a)
	{
		document.forms[0].counterparty_cd.value=couterpty_cd;
		document.forms[0].invoice_no.value=invoice_no;
		document.forms[0].financial_year.value=financial_year;
		document.forms[0].invoice_seq.value=invoice_seq;
		document.forms[0].contract_type.value=contract_type;
		document.forms[0].type_flag.value=type_flag;
		document.forms[0].invoice_type.value=invoice_type;
		
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].option.value="GENERATE_INVOICE_XML"
		document.forms[0].submit();
	}*/
	var u = document.forms[0].u.value;
	
	var url = "rpt_view_sales_invoice_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
			"&contract_type="+contract_type+"&type_flag="+type_flag+"&invoice_type="+invoice_type+
			"&counterparty_cd="+couterpty_cd+"&invoice_no="+invoice_no+"&bu_state_tin="+bu_state_tin+
			"&sap_approval_flag="+sap_approval_flag+"&agmt_no="+agmt_no+"&cont_no="+cont_no;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Sales SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Sales SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var segment = document.forms[0].segment.value;
	var chk = document.forms[0].chk;	// SagarB20251006 Added this for passing the value of checkbox
	if (chk.checked == true) {
		chk = "T";
	}
	else {
		chk = "F";
	}
	
	var url = "xls_dlng_invoice_accounting_approval.jsp?fileName=DLNG Sales Actual Report.xls&segment="+segment+"&from_dt="+from_dt+"&to_dt="+to_dt+"&chk="+chk;

	location.replace(url);
}
</script>

</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="dlngInv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");
String chk=request.getParameter("chk")==null?"T":request.getParameter("chk");		// SagarB20251006 Added for check variable

dlngInv.setCallFlag("DLNG_INVOICE_FO_APPROVAL");
dlngInv.setComp_cd(owner_cd);
dlngInv.setFrom_dt(from_dt);
dlngInv.setTo_dt(to_dt);
dlngInv.setSegment(segment);
dlngInv.setChk(chk);	// SagarB20251006 Added for Checkbox
dlngInv.init();

Vector VSEGMENT = dlngInv.getVSEGMENT();
Vector VSEGMENT_TYPE = dlngInv.getVSEGMENT_TYPE();
Vector VPLANT_ABBR = dlngInv.getVPLANT_ABBR();
Vector VPLANT_SEQ = dlngInv.getVPLANT_SEQ();
Vector VBU_NM = dlngInv.getVBU_NM();
Vector VTRUCK_CD = dlngInv.getVTRUCK_CD();
Vector VTRUCK_NO = dlngInv.getVTRUCK_NO();
Vector VCOUNTERPARTY_CD = dlngInv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dlngInv.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dlngInv.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = dlngInv.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlngInv.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlngInv.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = dlngInv.getVCONT_NO();
Vector VCONT_REV_NO = dlngInv.getVCONT_REV_NO();
Vector VAGMT_NO = dlngInv.getVAGMT_NO();
Vector VAGMT_REV_NO = dlngInv.getVAGMT_REV_NO();
Vector VDIS_CONT_MAPPING = dlngInv.getVDIS_CONT_MAPPING();
Vector VCONTRACT_TYPE = dlngInv.getVCONTRACT_TYPE();
Vector VPERIOD_START_DT = dlngInv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = dlngInv.getVPERIOD_END_DT();
Vector VFINANCIAL_YEAR = dlngInv.getVFINANCIAL_YEAR();
Vector VINVOICE_NO = dlngInv.getVINVOICE_NO();
Vector VINVOICE_SEQ = dlngInv.getVINVOICE_SEQ();
Vector VBU_STATE_TIN = dlngInv.getVBU_STATE_TIN();
Vector VINVOICE_DT = dlngInv.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = dlngInv.getVINVOICE_DUE_DT();
Vector VSALES_PRICE = dlngInv.getVSALES_PRICE();
Vector VSALES_PRICE_CD = dlngInv.getVSALES_PRICE_CD();
Vector VSALES_PRICE_NM = dlngInv.getVSALES_PRICE_NM();
Vector VGROSS_AMT = dlngInv.getVGROSS_AMT();
Vector VTAX_AMT = dlngInv.getVTAX_AMT();
Vector VINVOICE_AMT = dlngInv.getVINVOICE_AMT();
Vector VNET_PAYABLE_AMT = dlngInv.getVNET_PAYABLE_AMT();
Vector VTCS_AMT = dlngInv.getVTCS_AMT();
Vector VTDS_GROSS_PERCENT = dlngInv.getVTDS_GROSS_PERCENT();
Vector VTDS_GROSS_AMT = dlngInv.getVTDS_GROSS_AMT();
Vector VTDS_TAX_PERCENT = dlngInv.getVTDS_TAX_PERCENT();
Vector VTDS_TAX_AMT = dlngInv.getVTDS_TAX_AMT();
Vector VPAY_RECV_AMT = dlngInv.getVPAY_RECV_AMT();
Vector VPAY_RECV_DT = dlngInv.getVPAY_RECV_DT();
Vector VTAX_STRUCT_DTL = dlngInv.getVTAX_STRUCT_DTL();
Vector VTDS_TCS_FLAG = dlngInv.getVTDS_TCS_FLAG();
Vector VSHORT_RECEIVED = dlngInv.getVSHORT_RECEIVED();
Vector VSAP_APPROVAL_FLAG = dlngInv.getVSAP_APPROVAL_FLAG();
Vector VTYPE_FLAG = dlngInv.getVTYPE_FLAG();

Vector VTCS_TDS = dlngInv.getVTCS_TDS();
Vector VALLOC_QTY = dlngInv.getVALLOC_QTY();
Vector VINVOICE_RAISED_IN = dlngInv.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = dlngInv.getVPAYMENT_DONE_IN();
Vector VSALES_AMT = dlngInv.getVSALES_AMT();
Vector VINVOICE_TYPE=dlngInv.getVINVOICE_TYPE();
Vector VCONT_REF_NO= dlngInv.getVCONT_REF_NO();
Vector VCASH_FLOW= dlngInv.getVCASH_FLOW();

Vector VFIN_SYS = dlngInv.getVFIN_SYS();//PB 20250627 for Sun
Vector VALLOC_MT = dlngInv.getVALLOC_MT();// SagarB20251006 for MT
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_DLNG_invoice">

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
					<div class="d-flex flex-column flex-md-row justify-content-between align-items-center">
					    <div class="topheader">
					    	DLNG Sales Actual Report
					    </div>
						<div class="form-group row">
							<div class="d-flex justify-content-between">
								<div class="btn-group">
									<select class="btn btn-outline-secondary btngrp btnactive" name="segment" onchange="refresh();">
										<option value="">All</option>
										<%for(int i=0;i<VSEGMENT.size();i++){ %>
										<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT.elementAt(i)%></option>
										<%} %>
									</select>
								</div>&nbsp;
								<div class="col-auto">
									<div align="right" onclick="exportToXls();" style="color:green;">
										<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
									</div>
								</div>
							</div>
						</div>
						<script>document.forms[0].segment.value="<%=segment%>"</script>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<!-- HM20251208 : Responsive view fix -->
						<div class="col-md-2 col-sm-0 col-xs-0"></div>
						<div class="col-sm-5 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col">
									<div class="input-group input-group-sm" >
										<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
										onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">		<!-- SagarB20251006 Removed refresh(); function -->
										<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
									</div>
								</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col">
									<div class="input-group input-group-sm" >
										<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
										onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">		<!-- SagarB20251006 Removed refresh(); function -->
										<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
									</div>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<input type="checkbox" class="form-check-input" name="chk" id="chk"> <b> All Invoices </b>
									<script> <% if(chk.equals("T")) { %> document.getElementById("chk").checked = true; <% } else { %> document.getElementById("chk").checked = false; <% } %> </script>
					  			</div>
					  		</div>
					  	</div>
						
						<!-- SagarB20251006 Added below button for page refresh -->
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filters" onclick="refresh();">
					  			</div>
					  		</div>
					  	</div>
					  	<div class="col-md-2 col-sm-0 col-xs-0"></div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th rowspan="2">SR#
										</th>
										<th rowspan="2">Customer
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Customer" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Plant
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Customer_plant" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2"><%=owner_abbr%> BU
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_bu" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Contract#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Contract/Trade Ref#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_ContractRef" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Cash Flow
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_cash_flow" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Invoice#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_InvoiceNo" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Invoice Date</th>
										<th rowspan="2">Invoiced MMBTU</th>
										<th rowspan="2">Invoiced MT</th>		<!-- SagarB20251006 Added MT Column	 -->
										<th rowspan="2">Rate</th>
										<th rowspan="2" style="background: #000066; color: white;">Rate Unit / MMBTU</th>
										<th rowspan="2">Sales Amount</th>
										<th rowspan="2" style="background: #000066; color: white;">Invoice Raised In</th>
										<th rowspan="2" style="background: #000066; color: white;">Invoice Paid In</th>
										<th rowspan="2">Gross Amount</th>
										<th rowspan="2">Tax </th>
										<th rowspan="2">Invoice Amount</th>
										<th rowspan="2">TCS/TDS
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_tcstds" onkeyup="Search(this,'19');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">TCS/TDS%</th>
										<th rowspan="2">+TCS/-TDS Amount</th>
										<th rowspan="2">Net Receivable</th>
										<th rowspan="2">Billing Period
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_billinng period" onkeyup="Search(this,'23');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Invoice Due Date</th>
										<th rowspan="2">SAP View</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPARTY_CD.size() > 0){ %>
									<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
									<tr>
										<td align="center"><%=i+1%></td>
										<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%>">
											<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
											<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>" disabled>
											<input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=i%>" value="<%=VAGMT_REV_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=i%>" value="<%=VCONT_REV_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="contract_type" id="contract_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled>
											<input type="hidden" name="financial_year" id="financial_year<%=i%>" value="<%=VFINANCIAL_YEAR.elementAt(i)%>" disabled>
											<input type="hidden" name="invoice_seq" id="invoice_seq<%=i%>" value="<%=VINVOICE_SEQ.elementAt(i)%>" disabled>
											<input type="hidden" name="bu_state_tin" id="bu_state_tin<%=i%>" value="<%=VBU_STATE_TIN.elementAt(i)%>" disabled>
										</td>
										<td align="center"><%=VPLANT_ABBR.elementAt(i) %></td>
										<td align="center"><%=VBU_NM.elementAt(i) %></td>
										<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i) %></td>
										<td align="center"><%=VCONT_REF_NO.elementAt(i) %></td>
										<td align="center"><%=VCASH_FLOW.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
										<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
										<td align="right"><%=VALLOC_MT.elementAt(i) %></td>		<!-- SagarB20251006 Added MT data -->
										<td align="right"><%=VSALES_PRICE.elementAt(i)%></td>
										<td align="center" style="background: #b3f0ff;"><%=VSALES_PRICE_NM.elementAt(i) %></td>
										<td align="right"><%=VSALES_AMT.elementAt(i)%></td>
										<td style="background: #b3f0ff;"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>
										<td style="background: #b3f0ff;"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
										<td> <%=VGROSS_AMT.elementAt(i)%>
											<input type="hidden" name="gross_amt" id="gross_amt<%=i%>" value="<%=VGROSS_AMT.elementAt(i)%>">
										</td>
										<td align="right">
											<%=VTAX_AMT.elementAt(i)%>
											<input type="hidden" name="tax_amt" id="tax_amt<%=i%>" value="<%=VTAX_AMT.elementAt(i)%>">
										</td>
										<td align="right">
											<%=VINVOICE_AMT.elementAt(i) %>
											<input type="hidden" name="invoice_amt" id="invoice_amt<%=i%>" value="<%=VINVOICE_AMT.elementAt(i)%>">
										</td>
										<td><%=VTCS_TDS.elementAt(i) %></td>
										<td><%=VTDS_GROSS_PERCENT.elementAt(i) %></td>
										<td><%=VTDS_GROSS_AMT.elementAt(i) %></td>										
										<td align="right">
											<%=VNET_PAYABLE_AMT.elementAt(i)%>
										</td>
										<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>	
										<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
										<td align="center">
										<%if(!owner_cd.equals("1") && utildate.getDays(""+VINVOICE_DT.elementAt(i),"01/04/2026")<=0){ %>
											<%if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")) {%>
												<%if(VFIN_SYS.elementAt(i).equals("S")){%>
													<span>
														 <i class="fa fa-sun-o fa-2x" aria-hidden="true" style="color:orange;"></i>
													</span>
												<%}else{%>
													<span class="fa-stack fa-lg">
													  <i class="fa fa-eye fa-stack-1x"></i>
													  <i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
													</span>
												<%} %>
											<%} %>	
										<%}else{%>								
											<i class="fa <%if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")) {%>fa-eye<%} %> fa-2x"
											onclick="doGenXML('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
											 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
											 '<%=VTYPE_FLAG.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','<%=VBU_STATE_TIN.elementAt(i)%>',
											 '<%=VSAP_APPROVAL_FLAG.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>');"
											 style="color: brown;"
											></i>
										<%} %> 	 											
										</td>			 											
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="29" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
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
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example");
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
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