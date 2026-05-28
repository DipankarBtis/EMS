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
	var pay_status = document.forms[0].pay_status.value;
	
	var flag=checkMonthYearRange(document.forms[0].month,document.forms[0].year,document.forms[0].month_to,document.forms[0].year_to);
	var u = document.forms[0].u.value;
	
	if(flag==true)
	{
		var url = "frm_purchase_payable_tracking.jsp?u="+u+"&pay_status="+pay_status+
				"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to;
	
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
	var pay_status = document.forms[0].pay_status.value;
	
	var url = "xls_payable_tracking.jsp?fileName=Payable Tracking From "+month+"/"+year+" To "+month_to+"/"+year_to+".xls&pay_status="+pay_status+
	"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to;

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
	var invoice_type = document.getElementById("invoice_type"+index);
	var type_flag = document.getElementById("type_flag"+index);
	var invoice_flag = document.getElementById("invoice_flag"+index);
	var bu_unit = document.getElementById("bu_unit"+index);
	var net_payable_amt = document.getElementById("net_payable_amt"+index);
	var net_payable_amt_usd = document.getElementById("net_payable_amt_usd"+index);
	var pay_received_amt = document.getElementById("pay_received_amt"+index);
	var total_received_amt = document.getElementById("total_received_amt"+index);
	var short_received = document.getElementById("short_received"+index);
	var pay_received_dt = document.getElementById("pay_received_dt"+index);
	var remark = document.getElementById("remark"+index);
	var remittance_no = document.getElementById("remittance_no"+index);
	
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
		invoice_type.disabled=false;
		type_flag.disabled=false;
		invoice_flag.disabled=false;
		bu_unit.disabled=false;
		net_payable_amt.disabled=false;
		net_payable_amt_usd.disabled=false;
		pay_received_amt.disabled=false;
		total_received_amt.disabled=false;
		short_received.disabled=false;
		pay_received_dt.disabled=false;
		remark.disabled=false;
		remittance_no.disabled=false;
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
		invoice_type.disabled=true;
		type_flag.disabled=true;
		invoice_flag.disabled=true;
		bu_unit.disabled=true;
		net_payable_amt.disabled=true;
		net_payable_amt_usd.disabled=true;
		pay_received_amt.disabled=true;
		total_received_amt.disabled=true;
		short_received.disabled=true;
		pay_received_dt.disabled=true;
		remark.disabled=true;
		remittance_no.disabled=true;
	}
}

function calcPayableAmt(index)
{
	
	var contract_type = document.getElementById("contract_type"+index);
	var invoice_flag = document.getElementById("invoice_flag"+index);
	var gross_amt = "";
	var invoice_amt = "";
	var net_payable_amt = "";
	
	if(contract_type.value=="N" && (invoice_flag.value=="P" || invoice_flag.value=="F"))
	{
		gross_amt = document.getElementById("gross_amt_usd"+index);
		invoice_amt = document.getElementById("invoice_amt_usd"+index);
		net_payable_amt = document.getElementById("net_payable_amt_usd"+index);
	}
	else
	{
		gross_amt = document.getElementById("gross_amt"+index);
		invoice_amt = document.getElementById("invoice_amt"+index);
		net_payable_amt = document.getElementById("net_payable_amt"+index);
	}
	
	var pay_received_amt = document.getElementById("pay_received_amt"+index);
	var short_received = document.getElementById("short_received"+index);
	var temp_short_received = document.getElementById("temp_short_received"+index);
	var total_received_amt = document.getElementById("total_received_amt"+index);
	var temp_total_received_amt = document.getElementById("temp_total_received_amt"+index);
	
	//calc short receive
	if(trim(pay_received_amt.value)!="")
	{
		var total_recv;
		var short_amt;
		
		if(trim(temp_total_received_amt.value)!="")
		{
			total_recv=round(parseFloat(temp_total_received_amt.value) + parseFloat(pay_received_amt.value),2);
		}
		else
		{
			total_recv=round(parseFloat(pay_received_amt.value),2);
		}
		
		if(!isNaN(total_recv))
		{
			total_received_amt.value=round(parseFloat(total_recv),2)
		}
		else
		{
			total_received_amt.value="";
		}
		
		short_amt = round(parseFloat(net_payable_amt.value)-parseFloat(total_recv),2);
		
		if(!isNaN(short_amt))
		{
			short_received.value=round(parseFloat(short_amt),2)
		}
		else
		{
			short_received.value="";
		}
		
		if(parseFloat(pay_received_amt.value) > parseFloat(temp_short_received.value))
		{
			alert("Payment entered is more than Paied for ROW - "+(parseInt(index)+1));
			
			short_received.value=temp_short_received.value;
			pay_received_amt.value="";
			total_received_amt.value=temp_total_received_amt.value;
		}
	}
	else
	{
		total_received_amt.value=temp_total_received_amt.value;
		
		if(trim(total_received_amt.value)!="")
		{
			var short_amt=round(parseFloat(net_payable_amt.value)-parseFloat(total_received_amt.value),2);
			
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
						msg+="Enter Actual Paid for ROW - "+(parseInt(i)+1)+"!\n";
						flag=false;
					}
					if(trim(pay_received_dt[i].value) == "")
					{
						msg+="Enter Paid Date for ROW - "+(parseInt(i)+1)+"!\n";
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
					msg+="Enter Actual Paid for ROW - 1!\n";
					flag=false;
				}
				if(trim(pay_received_dt.value) == "")
				{
					msg+="Enter Paid Date for ROW - 1!\n";
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

function checkPayReceviDt(obj,index)
{
	var sysdate=document.forms[0].sysdate.value;
	var count = compareDate(obj.value,sysdate);
	if(parseInt(count) == 1)
	{
		alert("Payment Date should not be grater then Sysdate!")
		obj.value="";
		return false;
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
int filter_start_year = CommonVariable.filter_start_year;

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String month_to=request.getParameter("month_to")==null?""+currentMonth:request.getParameter("month_to");
String year_to=request.getParameter("year_to")==null?""+currentYear:request.getParameter("year_to");
String pay_status=request.getParameter("pay_status")==null?"unpaid":request.getParameter("pay_status");

if(month.length() == 1)
{
	month="0"+month; 
}
if(month_to.length() == 1)
{
	month_to="0"+month_to; 
}

purchase.setCallFlag("PAYABLE_TRACKING");
purchase.setComp_cd(owner_cd);
purchase.setMonth(month);
purchase.setYear(year);
purchase.setMonth_to(month_to);
purchase.setYear_to(year_to);
purchase.setPay_status(pay_status);
purchase.init();

Vector VSEGMENT = purchase.getVSEGMENT();
Vector VSEGMENT_TYPE = purchase.getVSEGMENT_TYPE();

Vector VCOUNTERPTY_CD = purchase.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = purchase.getVCOUNTERPTY_ABBR();
Vector VMST_COUNTERPARTY_CD = purchase.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = purchase.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = purchase.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = purchase.getVCONT_NO();
Vector VCONT_REV_NO = purchase.getVCONT_REV_NO();
Vector VAGMT_NO = purchase.getVAGMT_NO();
Vector VAGMT_REV_NO = purchase.getVAGMT_REV_NO();
Vector VDIS_CONT_MAPPING = purchase.getVDIS_CONT_MAPPING();
Vector VCONTRACT_TYPE = purchase.getVCONTRACT_TYPE();
Vector VPERIOD_START_DT = purchase.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = purchase.getVPERIOD_END_DT();
Vector VFINANCIAL_YEAR = purchase.getVFINANCIAL_YEAR();
Vector VINVOICE_NO = purchase.getVINVOICE_NO();
Vector VINVOICE_SEQ = purchase.getVINVOICE_SEQ();
Vector VBU_UNIT = purchase.getVBU_UNIT();
Vector VINVOICE_DT = purchase.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = purchase.getVINVOICE_DUE_DT();
Vector VSALES_PRICE = purchase.getVSALES_PRICE();
Vector VSALES_PRICE_CD = purchase.getVSALES_PRICE_CD();
Vector VSALES_PRICE_NM = purchase.getVSALES_PRICE_NM();
Vector VGROSS_AMT = purchase.getVGROSS_AMT();
Vector VTAX_AMT = purchase.getVTAX_AMT();
Vector VINVOICE_AMT = purchase.getVINVOICE_AMT();
Vector VNET_PAYABLE = purchase.getVNET_PAYABLE();
Vector VTCS_AMT = purchase.getVTCS_AMT();
Vector VTCS_FACTOR = purchase.getVTCS_FACTOR();
Vector VTDS_AMT = purchase.getVTDS_AMT();
Vector VTDS_FACTOR = purchase.getVTDS_FACTOR();
Vector VPAY_RECV_AMT = purchase.getVPAY_RECV_AMT();
Vector VPAY_RECV_DT = purchase.getVPAY_RECV_DT();
Vector VTAX_STRUCT_DTL = purchase.getVTAX_STRUCT_DTL();
Vector VSHORT_RECEIVED = purchase.getVSHORT_RECEIVED();
Vector VPAY_RECV_HISTORY = purchase.getVPAY_RECV_HISTORY();
Vector VINVOICE_RAISED_IN = purchase.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = purchase.getVPAYMENT_DONE_IN();
Vector VINVOICE_TYPE = purchase.getVINVOICE_TYPE();
Vector VINDEX = purchase.getVINDEX();
Vector VINV_FLG = purchase.getVINV_FLG();
Vector VRATE_NM = purchase.getVRATE_NM();
Vector VINV_RAISED_IN = purchase.getVINV_RAISED_IN();
Vector VREMITTANCE_NO = purchase.getVREMITTANCE_NO();
Vector VDIS_REMITTANCE_NO = purchase.getVDIS_REMITTANCE_NO();
Vector VPAY_COLOR = purchase.getVPAY_COLOR();

Vector VGROSS_AMT_USD = purchase.getVGROSS_AMT_USD();
Vector VTAX_AMT_USD = purchase.getVTAX_AMT_USD();					
Vector VINVOICE_AMT_USD = purchase.getVINVOICE_AMT_USD();
Vector VNET_PAYABLE_USD = purchase.getVNET_PAYABLE_USD();
Vector VTCS_AMT_USD = purchase.getVTCS_AMT_USD();
Vector VTDS_AMT_USD = purchase.getVTDS_AMT_USD();
Vector VINVOICE_FLAG = purchase.getVINVOICE_FLAG();
Vector VSPLIT_VALUE = purchase.getVSPLIT_VALUE();
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
					    	Payable Tracking
					    </div>
					    <div class="row justify-content-end">
							<div class="col-auto" onclick="exportToXls();" style="color:green;">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
							</div>					
							<div class="col-auto">
							 	<div class="btn-group">
									<select class="btn btn-outline-secondary btngrp btnactive" name="pay_status" onchange="refresh();">
										<option value="all" label=All>All</option>
										<option value="paid" label="Paid">Paid</option>
										<option value="unpaid" label="Unpaid" selected="selected">Unpaid</option>
										<option value="partially_paid" label="Partially Paid">Partially Paid</option>
									</select>
								</div>
								<script>document.forms[0].pay_status.value="<%=pay_status%>"</script>
							</div>
						</div>
					    
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
					  				<select class="form-select form-select-sm" name="month" onchange="">
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
					  				<select class="form-select form-select-sm" name="year" onchange="">
					  					<%for(int i=(currentYear+1); i > filter_start_year;i--){ %>
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
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col">
					  				<select class="form-select form-select-sm" name="month_to" onchange="">
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
					  				<select class="form-select form-select-sm" name="year_to" onchange="">
					  					<%for(int i=(currentYear+1); i > filter_start_year;i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year_to.value="<%=year_to%>"</script>
								</div>
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
								</div>
							</div>
				  		</div>
						<div class="col-sm-2 col-xs-2 col-md-2"></div>
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
							<table class="table table-bordered table-hover" id="payable<%=j%>">
								<thead>
									<tr>
										<th rowspan="2"></th>
										<th rowspan="2">
											Trader
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Customer<%=j %>" onkeyup="Search(this,'1','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">
											Contract#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract<%=j %>" onkeyup="Search(this,'2','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">
											Billing Period
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_billing<%=j %>" onkeyup="Search(this,'3','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Remittance#<br>[Invoice Type]</th>
										<th rowspan="2">
											Invoice#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_InvoiceNo<%=j %>" onkeyup="Search(this,'5','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Invoice Date</th>
										<th rowspan="2">Purchase Rate</th>
										<th rowspan="2" style="background: #000066; color: white;">Rate Unit/ MMBTU</th>
										<th rowspan="2" style="background: #000066; color: white;">Invoice Raised In</th>	
										<th colspan="2">Gross Amount</th>
										<th rowspan="2">Tax Structure</th>
										<th colspan="2">Tax Amount</th>
										<th colspan="2">Invoice Amount</th>
										<th colspan="3">TCS</th>
										<th colspan="3">TDS</th>
										<th colspan="2">Net Payable</th>
										<th rowspan="2">Invoice Due Date</th>
										<th rowspan="2" style="background: #000066; color: white;">Invoice Paid In</th>
										<th rowspan="2">Total Paid</th>										
										<th rowspan="2">Short Paid</th>
										<th rowspan="2">Actual Paid</th>
										<th rowspan="2">Payment Date</th>
										<th rowspan="2">Remark</th>
									</tr>
									<tr>
										<th>USD</th>
										<th>INR</th>
										<th>USD</th>
										<th>INR</th>
										<th>USD</th>
										<th>INR</th>
										<th>TCS(%)</th>
										<th>TCS(USD)</th>
										<th>TCS(INR)</th>
										<th>TDS(%)</th>
										<th>TDS(USD)</th>
										<th>TDS(INR)</th>
										<th>USD</th>
										<th>INR</th>
									</tr>
								</thead>
								<tbody>
								<%k=0;
								if(index > 0){ %>
									<%for(i=i; i<VCOUNTERPTY_CD.size(); i++){ 
										k+=1; %>
									<tr>
										<td align="center">
											<input type="checkbox" class="form-check-input" name="chk" id="chk<%=i%>" onclick="enable_disable('<%=i%>');">
										</td>
										<td align="center"><%=VCOUNTERPTY_ABBR.elementAt(i)%>
											<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=i%>" value="<%=VCOUNTERPTY_CD.elementAt(i)%>" disabled>
											<input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=i%>" value="<%=VAGMT_REV_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=i%>" value="<%=VCONT_REV_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="contract_type" id="contract_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled>
											<input type="hidden" name="financial_year" id="financial_year<%=i%>" value="<%=VFINANCIAL_YEAR.elementAt(i)%>" disabled>
											<input type="hidden" name="invoice_seq" id="invoice_seq<%=i%>" value="<%=VINVOICE_SEQ.elementAt(i)%>" disabled>
											<input type="hidden" name="bu_unit" id="bu_unit<%=i%>" value="<%=VBU_UNIT.elementAt(i)%>" disabled>
											<input type="hidden" name="invoice_type" id="invoice_type<%=i%>" value="<%=VINVOICE_TYPE.elementAt(i)%>" disabled>
										</td>
										<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i) %>
											<%if(!VSPLIT_VALUE.elementAt(i).equals("")){ %>
													<font style="background:#ff99ff;">[Split <%=VSPLIT_VALUE.elementAt(i)%>%]</font>
											<%} %>	
										</td>
										<td align="center"><%=VPERIOD_START_DT.elementAt(i) %>-<%=VPERIOD_END_DT.elementAt(i) %></td>
										<td align="center"><%=VDIS_REMITTANCE_NO.elementAt(i)%>
											<input type="hidden" name="remittance_no" id="remittance_no<%=i%>" value="<%=VREMITTANCE_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="type_flag" id="type_flag<%=i%>" value="<%=VINV_FLG.elementAt(i)%>" disabled>
											<input type="hidden" name="invoice_flag" id="invoice_flag<%=i%>" value="<%=VINVOICE_FLAG.elementAt(i)%>" disabled>
										</td>
										<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
										<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
										<td align="right"><%=VSALES_PRICE.elementAt(i) %></td>
										<td align="center" style="background: #b3f0ff;"><%=VSALES_PRICE_NM.elementAt(i) %></td>
										<td align="center" style="background: #b3f0ff;"><%=VINV_RAISED_IN.elementAt(i) %></td>
										<td align="right"><%=VGROSS_AMT_USD.elementAt(i) %>
											<input type="hidden" name="gross_amt_usd" id="gross_amt_usd<%=i%>" value="<%=VGROSS_AMT_USD.elementAt(i)%>">
										</td>
										<td align="right"><%=VGROSS_AMT.elementAt(i) %>
											<input type="hidden" name="gross_amt" id="gross_amt<%=i%>" value="<%=VGROSS_AMT.elementAt(i)%>">
										</td>
										<td align="center"><%=VTAX_STRUCT_DTL.elementAt(i) %></td>
										<td align="right"><%=VTAX_AMT_USD.elementAt(i) %>
											<input type="hidden" name="tax_amt_usd" id="tax_amt_usd<%=i%>" value="<%=VTAX_AMT_USD.elementAt(i)%>">
										</td>
										<td align="right"><%=VTAX_AMT.elementAt(i) %>
											<input type="hidden" name="tax_amt" id="tax_amt<%=i%>" value="<%=VTAX_AMT.elementAt(i)%>">
										</td>
										<td align="right"><%=VINVOICE_AMT_USD.elementAt(i) %>
											<input type="hidden" name="invoice_amt_usd" id="invoice_amt_usd<%=i%>" value="<%=VINVOICE_AMT_USD.elementAt(i)%>">
										</td>
										<td align="right"><%=VINVOICE_AMT.elementAt(i) %>
											<input type="hidden" name="invoice_amt" id="invoice_amt<%=i%>" value="<%=VINVOICE_AMT.elementAt(i)%>">
										</td>
										<td align="right"><%=VTCS_FACTOR.elementAt(i) %></td>
										<td align="right"><%=VTCS_AMT_USD.elementAt(i) %></td>
										<td align="right"><%=VTCS_AMT.elementAt(i) %></td>
										<td align="right"><%=VTDS_FACTOR.elementAt(i) %></td>
										<td align="right"><%=VTDS_AMT_USD.elementAt(i) %></td>
										<td align="right"><%=VTDS_AMT.elementAt(i) %></td>
										<td align="right"><%=VNET_PAYABLE_USD.elementAt(i) %>
											<input type="hidden" class="form-control form-control-sm" name="net_payable_amt_usd" id="net_payable_amt_usd<%=i%>" value="<%=VNET_PAYABLE_USD.elementAt(i)%>" style="text-align:right;" disabled readOnly>
										</td>
										<td align="right"><%=VNET_PAYABLE.elementAt(i) %>
											<input type="hidden" class="form-control form-control-sm" name="net_payable_amt" id="net_payable_amt<%=i%>" value="<%=VNET_PAYABLE.elementAt(i)%>" style="text-align:right;" disabled readOnly>
										</td>
										<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
										<td align="center" style="background: <%if(VPAYMENT_DONE_IN.elementAt(i).equals("USD")){ %>#b3ff99<%}else{ %>#b3f0ff<%}%>;"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
										<td align="center" title="<%=VPAY_RECV_HISTORY.elementAt(i) %>">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="total_received_amt" id="total_received_amt<%=i%>" 
												 value="<%=VPAY_RECV_AMT.elementAt(i)%>" style="text-align:right;background:<%=VPAY_COLOR.elementAt(i)%>"
												onkeyup="calcPayableAmt('<%=i%>');" onblur="checkNumber1(this,12,2);" disabled readOnly>
											</div>
											<input type="hidden" class="form-control form-control-sm" name="temp_total_received_amt" id="temp_total_received_amt<%=i%>" 
												value="<%=VPAY_RECV_AMT.elementAt(i)%>" style="text-align:right;" disabled>
										</td>		
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="short_received" id="short_received<%=i%>" 
												value="<%=VSHORT_RECEIVED.elementAt(i)%>" style="text-align:right;" disabled readOnly>
											</div>
											<input type="hidden" class="form-control form-control-sm" name="temp_short_receiverd" id="temp_short_received<%=i%>" 
											value="<%=VSHORT_RECEIVED.elementAt(i)%>" style="text-align:right;" disabled readOnly>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="pay_received_amt" id="pay_received_amt<%=i%>" 
												value="" style="text-align:right;" 
												onkeyup="calcPayableAmt('<%=i%>');" onblur="checkNumber1(this,12,2);" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="pay_received_dt" id="pay_received_dt<%=i%>" 
						      						value="" maxLength="10" onchange="validateDate(this);checkPayReceviDt(this,'<%=i%>');" autocomplete="off" disabled>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div> 
					      					</div>
										</td>
										<td align="center">
											<div style="width:150px;">
												<textarea class="form-control form-control-sm" rows="1" cols="75" name="remark" id="remark<%=i%>" disabled><%%></textarea>
											</div>
										</td>
									</tr>
									<%
										if(k==index)
										{
											i=i+1;
											break;
										}
									} %>
								<%} else {%>
									<tr>
										<td colspan="32">
											<div align="center"><%=utilmsg.infoMessage("<b>No Remittance generated for Report Period!</b>")%></div>
										</td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<%} %>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="PAYABLE_TRACKING">

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
<input type="hidden" name="sysdate" value="<%=sysdate%>">

</form>
</body>
<script>
function Search(obj, indx, j) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("payable"+j);
  	
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