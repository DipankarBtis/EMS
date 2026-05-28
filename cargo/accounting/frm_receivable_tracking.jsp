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
	
	var u = document.forms[0].u.value;
	
	var url = "frm_receivable_tracking.jsp?u="+u+"&segment="+segment+
			"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to;

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
	var invoice_type = document.getElementById("invoice_type"+index);
	var type_flag = document.getElementById("type_flag"+index);
	var bu_state_tin = document.getElementById("bu_state_tin"+index);
	var net_payable_amt = document.getElementById("net_payable_amt"+index);
	var pay_received_amt = document.getElementById("pay_received_amt"+index);
	var total_received_amt = document.getElementById("total_received_amt"+index);
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
		invoice_type.disabled=false;
		type_flag.disabled=false;
		bu_state_tin.disabled=false;
		net_payable_amt.disabled=false;
		pay_received_amt.disabled=false;
		total_received_amt.disabled=false;
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
		invoice_type.disabled=true;
		type_flag.disabled=true;
		bu_state_tin.disabled=true;
		net_payable_amt.disabled=true;
		pay_received_amt.disabled=true;
		total_received_amt.disabled=true;
		short_received.disabled=true;
		pay_received_dt.disabled=true;
		remark.disabled=true;
	}
}

function calcPayableAmt(index)
{
	var gross_amt = document.getElementById("gross_amt"+index);

	var invoice_amt = document.getElementById("invoice_amt"+index);
	var net_payable_amt = document.getElementById("net_payable_amt"+index);
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
			alert("Payment entered is more than Receivable for ROW - "+(parseInt(index)+1));
			
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
						msg+="Enter Actual Received for ROW - "+(parseInt(i)+1)+"!\n";
						flag=false;
					}
					if(trim(pay_received_dt[i].value) == "")
					{
						msg+="Enter Received Date for ROW - "+(parseInt(i)+1)+"!\n";
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

function checkPayReceviDt(obj,index)
{
	var sysdate=document.forms[0].sysdate.value;
	var count = compareDate(obj.value,sysdate);
	if(parseInt(count) == 1)
	{
		alert("Pay Received Date should not be grater then Sysdate!")
		obj.value="";
		return false;
	}
}
function exportToXls()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var month_to = document.forms[0].month_to.value;
	var year_to = document.forms[0].year_to.value;
	var segment = document.forms[0].segment.value;
	
	var url = "xls_receivable_tracking.jsp?fileName=Receivable Tracking Report.xls&segment="+segment+"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to;

	location.replace(url);
}
</script>

</head>
<jsp:useBean class="com.etrm.fms.accounting.DataBean_Accounting" id="accounting" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
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

accounting.setCallFlag("RECEIVABLE_TRACKING");
accounting.setComp_cd(owner_cd);
accounting.setMonth(month);
accounting.setYear(year);
accounting.setMonth_to(month_to);
accounting.setYear_to(year_to);
accounting.init();

Vector VSEGMENT = accounting.getVSEGMENT();
Vector VSEGMENT_TYPE = accounting.getVSEGMENT_TYPE();

Vector VCOUNTERPARTY_CD = accounting.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = accounting.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = accounting.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = accounting.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = accounting.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = accounting.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = accounting.getVCONT_NO();
Vector VCONT_REV_NO = accounting.getVCONT_REV_NO();
Vector VAGMT_NO = accounting.getVAGMT_NO();
Vector VAGMT_REV_NO = accounting.getVAGMT_REV_NO();
Vector VDIS_CONT_MAPPING = accounting.getVDIS_CONT_MAPPING();
Vector VCONTRACT_TYPE = accounting.getVCONTRACT_TYPE();
Vector VPERIOD_START_DT = accounting.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = accounting.getVPERIOD_END_DT();
Vector VFINANCIAL_YEAR = accounting.getVFINANCIAL_YEAR();
Vector VINVOICE_NO = accounting.getVINVOICE_NO();
Vector VINVOICE_SEQ = accounting.getVINVOICE_SEQ();
Vector VBU_STATE_TIN = accounting.getVBU_STATE_TIN();
Vector VINVOICE_DT = accounting.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = accounting.getVINVOICE_DUE_DT();
Vector VSALES_PRICE = accounting.getVSALES_PRICE();
Vector VSALES_PRICE_CD = accounting.getVSALES_PRICE_CD();
Vector VSALES_PRICE_NM = accounting.getVSALES_PRICE_NM();
Vector VGROSS_AMT = accounting.getVGROSS_AMT();
Vector VTAX_AMT = accounting.getVTAX_AMT();
Vector VINVOICE_AMT = accounting.getVINVOICE_AMT();
Vector VNET_PAYABLE_AMT = accounting.getVNET_PAYABLE_AMT();
Vector VTCS_AMT = accounting.getVTCS_AMT();
Vector VTCS_FACTOR = accounting.getVTCS_FACTOR();
Vector VTDS_GROSS_PERCENT = accounting.getVTDS_GROSS_PERCENT();
Vector VTDS_GROSS_AMT = accounting.getVTDS_GROSS_AMT();
Vector VTDS_TAX_PERCENT = accounting.getVTDS_TAX_PERCENT();
Vector VTDS_TAX_AMT = accounting.getVTDS_TAX_AMT();
Vector VPAY_RECV_AMT = accounting.getVPAY_RECV_AMT();
Vector VPAY_RECV_DT = accounting.getVPAY_RECV_DT();
Vector VTAX_STRUCT_DTL = accounting.getVTAX_STRUCT_DTL();
Vector VTDS_TCS_FLAG = accounting.getVTDS_TCS_FLAG();
Vector VSHORT_RECEIVED = accounting.getVSHORT_RECEIVED();
Vector VPAY_RECV_HISTORY = accounting.getVPAY_RECV_HISTORY();
Vector VINVOICE_RAISED_IN = accounting.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = accounting.getVPAYMENT_DONE_IN();
Vector VINVOICE_TYPE = accounting.getVINVOICE_TYPE();
Vector VTYPE_FLAG = accounting.getVTYPE_FLAG();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Accounting">

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
					    	Receivable Tracking
					    </div>
					    <div class="d-flex justify-content-between">
					   		 <div onclick="exportToXls();" style="color:green;">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
							</div>&nbsp;
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
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th rowspan="2"></th>
										<th rowspan="2">
											Customer
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Customer" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">
											Contract#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Contract Type<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract_type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></th>
										<th rowspan="2">Billing Period</th>
										<th rowspan="2">
											Invoice#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_InvoiceNo" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Invoice Date</th>
										<th rowspan="2">Sales Rate</th>
										<th rowspan="2" style="background: #000066; color: white;">Rate Unit/ MMBTU</th>
										<th rowspan="2" style="background: #000066; color: white;">Invoice Raised In</th>										
										<th rowspan="2">Gross Amount (INR)</th>
										<th rowspan="2">Tax Structure</th>
										<th rowspan="2">Tax Amount (INR)</th>
										<th rowspan="2">Invoice Amount (INR)</th>
										<th colspan="2">TCS (INR)</th>
										<th colspan="2">TDS (INR)</th>
										<th rowspan="2">Net Receivable</th>
										<th rowspan="2">Invoice Due Date</th>
										<th rowspan="2" style="background: #000066; color: white;">Invoice Paid In</th>
										<th rowspan="2">Total Received (INR)</th>										
										<th rowspan="2">Short Received (INR)</th>
										<th rowspan="2">Actual Received (INR)</th>
										<th rowspan="2">Received Date</th>
										<th rowspan="2">Remark</th>
									</tr>
									<tr>
										<th>TCS(%)</th>
										<th>TCS Values</th>
										<th>TDS(%)</th>
										<th>TDS Values</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPARTY_CD.size() > 0){ %>
									<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ 
										String cont_type_nm =  utilBean.getContractTypeName(""+VCONTRACT_TYPE.elementAt(i));
									%>
									<tr>
										<td>
											<input type="checkbox" class="form-check-input" name="chk" id="chk<%=i%>" onclick="enable_disable('<%=i%>');">
										</td>
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
											<input type="hidden" name="invoice_type" id="invoice_type<%=i%>" value="<%=VINVOICE_TYPE.elementAt(i)%>" disabled>
											<input type="hidden" name="type_flag" id="type_flag<%=i%>" value="<%=VTYPE_FLAG.elementAt(i)%>" disabled>
										</td>
										<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i) %></td>
										<td align="center"><%=cont_type_nm %></td>
										<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
										<td align="right"><%=VSALES_PRICE.elementAt(i)%></td>
										<td align="center" style="background: #b3f0ff;"><%=VSALES_PRICE_NM.elementAt(i) %></td>
										<td align="center" style="background: #b3f0ff;"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>
										<td align="right">
											<%=VGROSS_AMT.elementAt(i)%>
											<input type="hidden" name="gross_amt" id="gross_amt<%=i%>" value="<%=VGROSS_AMT.elementAt(i)%>">
										</td>
										<td align="center"><%=VTAX_STRUCT_DTL.elementAt(i)%></td>
										<td align="right">
											<%=VTAX_AMT.elementAt(i)%>
											<input type="hidden" name="tax_amt" id="tax_amt<%=i%>" value="<%=VTAX_AMT.elementAt(i)%>">
										</td>
										<td align="right">
											<%=VINVOICE_AMT.elementAt(i) %>
											<input type="hidden" name="invoice_amt" id="invoice_amt<%=i%>" value="<%=VINVOICE_AMT.elementAt(i)%>">
										</td>
										<td align="right"><%=VTCS_FACTOR.elementAt(i) %></td>
										<td align="right"><%=VTCS_AMT.elementAt(i) %></td>										
										<td align="center"><%=VTDS_GROSS_PERCENT.elementAt(i)%></td>
										<td align="center"><%=VTDS_GROSS_AMT.elementAt(i)%></td>										
										<td align="right"><input type="hidden" class="form-control form-control-sm" name="net_payable_amt" id="net_payable_amt<%=i%>" value="<%=VNET_PAYABLE_AMT.elementAt(i)%>" style="text-align:right;" disabled readOnly><%=VNET_PAYABLE_AMT.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
										<td align="center" style="background: #b3f0ff;"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
										<td align="center" title="<%=VPAY_RECV_HISTORY.elementAt(i)%>">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="total_received_amt" id="total_received_amt<%=i%>" 
												value="<%=VPAY_RECV_AMT.elementAt(i)%>" style="text-align:right;" 
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
											<input type="hidden" class="form-control form-control-sm" name="temp_short_received" id="temp_short_received<%=i%>" 
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
									<%} %>
								<%} else {%>
									<tr>
										<td colspan="26">
											<div align="center"><%=utilmsg.infoMessage("<b>No Invoice generated for Report Period!</b>")%></div>
										</td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
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

<input type="hidden" name="option" value="RECEIVABLE_TRACKING">

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