<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function doSubmit()
{
	var eff_dt = document.forms[0].eff_dt.value;
	var freq = document.forms[0].freq.value;
	var billing_flag = document.forms[0].billing_flag.value;
	var due_date = document.forms[0].due_date.value;
	var due_dt_in = document.forms[0].due_dt_in.value
	var inv_currency = document.forms[0].inv_currency.value;
	var payment_currency = document.forms[0].payment_currency.value;
	var inv_criteria = document.forms[0].inv_criteria.value;
	var exchng_rate = document.forms[0].exchng_rate.value;
	var exchg_val = document.forms[0].exchg_val.value;
	var exch_calc_base = document.forms[0].exch_calc_base.value;
	var rate = document.forms[0].rate.value;
	
	var billing_days = document.forms[0].billing_days.value;
	
	var rate_unit = document.forms[0].rate_unit.value;
	
	var msg="";
	var flag=true;
	
	if(trim(eff_dt) == "")
	{
		msg+="Select Eff Date!\n";
		flag=false;
	}
	if(trim(freq) == "")
	{
		msg+="Select Billing/Payment Freq!\n";
		flag=false;
	}
	if(trim(freq) == "O")
	{
		if(trim(billing_days)=="")
		{
			msg+="Enter Number of Days to define 'Other' Billing Freq!\n";
			flag=false;
		}
	}
	if(trim(billing_flag) == "")
	{
		msg+="Select Billing Period!\n";
		flag=false;
	}
	if(trim(due_date) == "")
	{
		msg+="Enter Payment Due Period!\n";
		flag=false;
	}
	if(trim(due_dt_in) == "")
	{
		msg+="Select Consider Due Date in!\n";
		flag=false;
	}
	if(trim(inv_currency) == "" || inv_currency == "0")
	{
		msg+="Select Invoice Raised In!\n";
		flag=false;
	}
	if(trim(payment_currency) == "" || payment_currency == "0")
	{
		msg+="Select Payment Done In!\n";
		flag=false;
	}
	if(trim(rate) == "")
	{
		msg+="Select Interest Rate!\n";
		flag=false;
	}
	
	if(rate_unit == "2")
	{
		if(trim(exchng_rate) == "")
		{
			msg+="Select Exchange Rate Reference!\n";
			flag=false;
		}
		if(exchng_rate=="0")
		{
			if(trim(exchg_val) == "")
			{
				msg+="Enter Exchange Value!\n";
				flag=false;
			}
		}
		else
		{
			if(exch_calc_base=="D")
			{
				if(trim(inv_criteria) == "")
				{
					msg+="Select Exchange Rate Calculation Criteria!\n";
					flag=false;
				}
			}
		}
	}
	else
	{
		document.forms[0].exchng_rate.value="";
		document.forms[0].exch_calc_base.value="";
		document.forms[0].exchg_rate_note.value="";
		document.forms[0].exchng_rate.value="";
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Submit?");
		if(a)
		{
			//document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg)
	}
}

function showExchngCriteria(obj)
{
	if(obj.value=="D")
	{
		document.getElementById("exchngCri").style.display="flex";
	}
	else
	{
		
		document.getElementById("exchngCri").style.display="none";
		document.forms[0].inv_criteria.value="";
	}
}

function setBillingDays(obj)
{
	if(obj.value=="O")
	{
		document.forms[0].billing_days.readOnly=false;
		document.getElementById("id_days").style.display="flex";
	}
	else
	{
		document.forms[0].billing_days.value="";
		document.forms[0].billing_days.readOnly=true;
		document.getElementById("id_days").style.display="none";
	}
}

function setBillingPeriod(obj)
{
	if(obj.value=="T")
	{
		document.forms[0].freq.value="O";
		//document.forms[0].freq.style.pointerEvents = "none";
		
		document.forms[0].billing_days.readOnly=false;
		document.getElementById("id_days").style.display="flex";
	}
	else
	{
		document.forms[0].freq.value=document.forms[0].hid_billing_freq.value;
		//document.forms[0].freq.style.pointerEvents = "auto";
		
		document.forms[0].billing_days.readOnly=true;
		document.getElementById("id_days").style.display="none";
	}
}

function setExchgVal(obj)
{
	if(obj.value=="0")
	{
		document.getElementById("row_exchg_val").style.display="";
		document.getElementById("row2_exchg").style.display="none";
		document.forms[0].inv_criteria.value="A";
	}
	else if(obj.value=="")
	{
		document.getElementById("row_exchg_val").style.display="none";
		document.getElementById("row2_exchg").style.display="none";
		document.forms[0].exchg_val.value="";
	}
	else
	{
		document.getElementById("row_exchg_val").style.display="none";
		document.getElementById("row2_exchg").style.display="";
		document.forms[0].exchg_val.value="";
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_master.DataBean_ContractMaster" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
<%
String clearance=request.getParameter("clearance")==null?"":request.getParameter("clearance");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String end_dt=request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String rate_unit=request.getParameter("rate_unit")==null?"2":request.getParameter("rate_unit");
String display_map_id=utilBean.NewDealMappingId(owner_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");

contract.setCallFlag("CONTRACT_BILLING_DTL");
contract.setComp_cd(owner_cd);
contract.setCont_start_dt(start_dt);
contract.setCont_end_dt(end_dt);
contract.setCounterparty_cd(counterparty_cd);
contract.setAgmt_no(agmt_no);
contract.setAgmt_rev_no(agmt_rev_no);
contract.setCont_no(cont_no);
contract.setCont_rev_no(cont_rev_no);
contract.setContract_type(contract_type);
contract.init();

Vector VEXCHNG_RATE_CD = contract.getVEXCHNG_RATE_CD();
Vector VEXCHNG_RATE_NM = contract.getVEXCHNG_RATE_NM();
Vector VINT_RATE_CD = contract.getVINT_RATE_CD();
Vector VINT_RATE_NM = contract.getVINT_RATE_NM();

Vector VPLANT_NAME = contract.getVPLANT_NAME();
Vector VTAX_STRUCT_CD = contract.getVTAX_STRUCT_CD();
Vector VTAX_STRUCT_NM = contract.getVTAX_STRUCT_NM();
Vector VBU_PLANT_NM = contract.getVBU_PLANT_NM();
Vector VTAX_SAP_CODE = contract.getVTAX_SAP_CODE();

Vector VINVOICE_TYPE = contract.getVINVOICE_TYPE();			//Pratham Bhatt 20240822: for invoice type 
Vector VINVOICE_CATEGORY = contract.getVINVOICE_CATEGORY();	//Pratham Bhatt 20240822: for invoice category 

String billing_freq=contract.getBilling_freq();
String billing_flag=contract.getBilling_flag();
String due_date=contract.getDue_date();
String sec_due_date=contract.getSec_due_date();
String inv_currency=contract.getInv_currency();
String payment_currency=contract.getPayment_currency();
String interest_rate_cd=contract.getInterest_rate_cd();
String interest_cal_sign=contract.getInterest_cal_sign();
String interest_cal_per=contract.getInterest_cal_per();
String exchng_rate_cd=contract.getExchng_rate_cd();
String exchng_cal=contract.getExchng_cal();
String exchng_criteria=contract.getExchng_criteria();
String exchng_note=contract.getExchng_note();
String due_dt_in=contract.getDue_dt_in();
String exclude_sat=contract.getExclude_sat();
String eff_dt=contract.getEff_dt();
String billing_days=contract.getBilling_days();
String exchg_val=contract.getExchg_val();
String old_eff_dt = contract.getOld_eff_dt();


if(due_dt_in.equals(""))
{
	due_dt_in="C";
}
%>
<body style="pointer-events: none;" onload="setBillingPeriod(document.forms[0].billing_flag);setBillingDays(document.forms[0].freq);setExchgVal(document.forms[0].exchng_rate);">

<form method="post" action="../servlet/Frm_ContracMaster">

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
					    <%=utilBean.getCounterpartyABBR(counterparty_cd)%> [<%=display_map_id%>] 
					     Billing Details
					     <%if(!old_eff_dt.equals("")) {%>
					     (Effective since <%=old_eff_dt%>)
					     <%} %>
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-6 col-xs-6 col-md-6"> </div>					
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label" style="background:#FFF4A3;"><b>New Eff Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div style="width:100px;" class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="eff_dt" value="<%=eff_dt%>" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" readOnly style="background:#FFF4A3;">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    		</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Billing on<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="billing_flag" onchange="setBillingPeriod(this);">
							    		<option value="">--Select--</option>
							     		<option value="T"> [T] TCQ Completion </option>
							     		<option value="B"> [B] Billing Period </option>
							     	</select>
							     	<script>document.forms[0].billing_flag.value="<%=billing_flag%>"</script>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Billing Period<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-6">
				    				<select class="form-select form-select-sm" name="freq" onchange="setBillingDays(this);">
				    					<option value="W">Weekly</option>
				    					<option value="F">Fortnightly</option>
				    					<option value="M">Monthly</option>
				    					<!-- <option value="Q">Quarterly</option> -->
				    					<option value="O">Other</option>
				    				</select>
				    				<script>document.forms[0].freq.value="<%=billing_freq%>"</script>
				    				<input type="hidden" name="hid_billing_freq" value="<%=billing_freq%>">
				    			</div>
				    			<div class="col-6">
				    				<div class="input-group input-group-sm" id="id_days">
					    				<input type="text" class="form-control form-control-sm" name="billing_days" 
					    				onblur="negNumber(this),checkNumber1(this,2,0);" size="5" value="<%=billing_days%>" style="background:#ffffb3;">
					    				<span class="input-group-text">Days</span>
					    			</div>
				    			</div>
				    		</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Payment Due Date Consideration</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Payment Due Period<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="due_date" onblur="negNumber(this),checkNumber1(this,3,0);" value="<%=due_date%>">
			      						<span class="input-group-text">Days</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">
		    				<font color="blue"><b>Secondary Payment Due Period</b></font>
		    			</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="sec_due_date" onblur="negNumber(this),checkNumber1(this,3,0);" value="<%=sec_due_date%>">
			      						<span class="input-group-text">Days</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
					</div>
					<div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">
		    				<font color=""><b>Consider Due Date in<span class="s-red">*</span></b></font>
		    			</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-auto">
				    				<select class="form-select form-select-sm" name="due_dt_in">
				    					<option value="C">Calendar Days</option>
				    					<option value="B">Business Days</option>
				    				</select>
				    				<script>document.forms[0].due_dt_in.value="<%=due_dt_in%>"</script>
				    			</div>
				    			<div class="col-auto">
				    				<input type="checkbox" class="form-check-input" name="exclude_sat" value="Y" <%if(exclude_sat.equals("Y")){%>checked<%}%>>&nbsp;<b>Exclude Saturday</b>
				    			</div>
				    		</div>
				    	</div>
				    </div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Invoice Currency & Exchange Rate</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Invoice Raised In<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="inv_currency">
							    		<option value="0">--Select--</option>
							     		<option value="1"> INR </option>
							     		<%-- <%if(rate_unit.equals("2")){ %>
							     		<option value="2"> USD </option>
							     		<%} %> --%>
							     	</select>
							     	<script>document.forms[0].inv_currency.value="<%=inv_currency%>"</script>
				    			</div>
				    		</div>
				    	</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Payment Done In<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="payment_currency">
							    		<option value="0">--Select--</option>
							     		<option value="1"> INR </option>
							     		<%-- <%if(rate_unit.equals("2")){ %>
							     		<option value="2"> USD </option>
							     		<%} %> --%>
							     	</select>
							     	<script>document.forms[0].payment_currency.value="<%=payment_currency%>"</script>
				    			</div>
				    		</div>
				    	</div>
					</div>
					<div <%if(rate_unit.equals("1")){%>style="display:none"<%}%>>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">
								<label class="form-label"><b>Exchange Rate<span class="s-red">*</span></b></label>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<select class="form-select form-select-sm" name="exchng_rate" onchange="setExchgVal(this);">
								    		<option value="">--Select--</option>
								    		<option value="0">FIXED</option>
								    		<%for(int i=0;i<VEXCHNG_RATE_CD.size();i++){%>	
								    		<option value="<%=VEXCHNG_RATE_CD.elementAt(i)%>"><%=VEXCHNG_RATE_NM.elementAt(i)%></option>
									     	<%}%>
								    	</select>
								    	<script>document.forms[0].exchng_rate.value="<%=exchng_rate_cd%>"</script>
								    </div>
								</div>
							</div>
							<div class="col-sm-6 col-xs-6 col-md-6" id="row_exchg_val" style="display:none;">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>Exchange Value<span class="s-red">*</span></b></label>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
						    				<input type="text" class="form-control form-control-sm" name="exchg_val" 
						    				value="<%=exchg_val%>" onblur="checkNumber1(this,6,4);">
						    				<span class="input-group-text">INR/USD</span>
						    			</div>
					    			</div>
								</div>
							</div>
						</div>
						<div id="row2_exchg">
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">
									<label class="form-label"><b>Exchange Rate Calculation<span class="s-red">*</span></b></label>
								</div>
								<div class="col-sm-10 col-xs-10 col-md-10">
									<div class="form-group row">
						    			<div class="col">
						    				<select class="form-select form-select-sm" name="exch_calc_base" onchange="showExchngCriteria(this);">
									    		<option value="D">On Particular Day Base</option>
									     		<option value="A">On Daily Basis of Billing Period</option>
									     	</select>
									     	<script>document.forms[0].exch_calc_base.value="<%=exchng_cal%>"</script>
						    			</div>
										<div class="col">
						    				<select class="form-select form-select-sm" name="inv_criteria" id="exchngCri" <%if(!exchng_cal.equals("D")){ %>style="display:none;"<%} %>>
									    		<option value="">--Select--</option>
									     		<option value="INV">Date of Invoice</option>
									     		<option value="LST">Last Day of Billing Cycle</option>
									     		<option value="PRE">Previous Day of Billing Cycle</option>
									     	</select> 
									     	<script>document.forms[0].inv_criteria.value="<%=exchng_criteria%>"</script>
						    			</div>
						    		</div>
						    	</div>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">
									<label class="form-label"><b>Exchange Rate Consideration On</b></label>
								</div>
								<div class="col-sm-10 col-xs-10 col-md-10">
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						    				<input type="text" class="form-control form-control-sm" name="exchg_rate_note" value="<%=exchng_note%>" size="80" maxlength="200">
						    			</div>
						    		</div>
						    	</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Interest Rate Calculation</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Interest Rate<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">
							<div class="form-group row">
				    			<div class="col">
				    				<select class="form-select form-select-sm" name="rate">
							    		<option value="">--Select--</option>
							    		<%for(int i=0;i<VINT_RATE_CD.size();i++){%>	
							    		<option value="<%=VINT_RATE_CD.elementAt(i)%>"><%=VINT_RATE_NM.elementAt(i)%></option>
								     	<%}%>
							    	</select>
							    	<script>document.forms[0].rate.value="<%=interest_rate_cd%>"</script>
				    			</div>
				    			<div class="col-auto">
				    				<select class="form-select form-select-sm" name="plusmin">
							     		<option value=""></option>
							     		<option value="+"> + </option>
							     		<option value="-"> - </option>
							     	</select>
							     	<script>document.forms[0].plusmin.value="<%=interest_cal_sign%>"</script>
				    			</div>
				    			<div class="col">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="modeper" value="<%=interest_cal_per%>" style='text-align:right;' onblur="negNumber(this),checkNumber1(this,4,2);">
			      						<span class="input-group-text">(%)</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	&nbsp;
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Applicable Taxes
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Business Unit</th>
										<th>Plant</th>
										<th>Invoice Category</th>
										<th>Invoice Type</th>
										<th>Tax Structure Details</th>
										<th>Tax SAP Code</th>
									</tr>
								</thead>
								<tbody>
								<%if(VPLANT_NAME.size()>0){ %>
									<%for(int i=0; i<VPLANT_NAME.size(); i++){ %>
										<tr style="background:#99ffcc;">
											<td align="center"><%=VBU_PLANT_NM.elementAt(i)%></td>
											<td align="center"><%=VPLANT_NAME.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_CATEGORY.elementAt(i) %></td>
											<td align="center"><%=VINVOICE_TYPE.elementAt(i) %></td>
											<td>[<%=VTAX_STRUCT_CD.elementAt(i)%>]- <%=VTAX_STRUCT_NM.elementAt(i)%></td>
											<td align="center"><%=VTAX_SAP_CODE.elementAt(i)%></td>
										</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="6" align="center"><%=utilmsg.infoMessage("<b>No Taxes Configured!</b>") %></td>
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

<input type="hidden" name="option" value="CONTRACT_BILLING_DTL">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>">  	
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="start_dt" value="<%=start_dt%>">
<input type="hidden" name="rate_unit" value="<%=rate_unit%>">

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
</html>