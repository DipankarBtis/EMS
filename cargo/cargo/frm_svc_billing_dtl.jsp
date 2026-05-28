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
	var freq = document.forms[0].freq.value;
	var billing_flag = document.forms[0].billing_flag.value;
	var due_date = document.forms[0].due_date.value;
	var due_dt_in = document.forms[0].due_dt_in.value
	var inv_currency = document.forms[0].inv_currency.value;
	var payment_currency = document.forms[0].payment_currency.value;
	var rate = document.forms[0].rate.value;
	
	var final_rate_unit = document.forms[0].final_rate_unit.value;
	var provisional_rate_unit = document.forms[0].provisional_rate_unit.value;
	var u = document.forms[0].u.value;
	
	var msg="";
	var flag=true;
	
	if(trim(freq) == "")
	{
		msg+="Select Billing/Payment Freq!\n";
		flag=false;
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


</script>
</head>
<jsp:useBean class="com.etrm.fms.cargo.DB_cargo_service_cont_master" id="svc" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
<%
String clearance=request.getParameter("clearance")==null?"":request.getParameter("clearance");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String entity_type=request.getParameter("entity_type")==null?"":request.getParameter("entity_type");
String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String final_rate_unit=request.getParameter("final_rate_unit")==null?"":request.getParameter("final_rate_unit");
String provisional_rate_unit=request.getParameter("provisional_rate_unit")==null?"":request.getParameter("provisional_rate_unit");
String display_map_id=utilBean.NewDealMappingId(owner_cd, counterparty_cd, "", "", cont_no, "", contract_type, "");

svc.setCallFlag("SVC_BILLING_DTL");
svc.setComp_cd(owner_cd);
svc.setCont_start_dt(start_dt);
svc.setCounterparty_cd(counterparty_cd);
svc.setCont_no(cont_no);
svc.setEntity_role(entity_type);
svc.setContract_type(contract_type);
svc.init();

Vector VEXCHNG_RATE_CD = svc.getVEXCHNG_RATE_CD();
Vector VEXCHNG_RATE_NM = svc.getVEXCHNG_RATE_NM();
Vector VINT_RATE_CD = svc.getVINT_RATE_CD();
Vector VINT_RATE_NM = svc.getVINT_RATE_NM();

Vector VPLANT_NAME = svc.getVPLANT_NAME();
Vector VTAX_STRUCT_CD = svc.getVTAX_STRUCT_CD();
Vector VTAX_STRUCT_NM = svc.getVTAX_STRUCT_NM();
Vector VTAX_SAP_CODE = svc.getVTAX_SAP_CODE();
Vector VBU_PLANT_NM = svc.getVBU_PLANT_NM();

Vector VINVOICE_TYPE = svc.getVINVOICE_TYPE(); 			//Pratham Bhatt 20240821: for invoice type 
Vector VINVOICE_CATEGORY = svc.getVINVOICE_CATEGORY();	//Pratham Bhatt 20240822: for invoice category

String billing_freq=svc.getBilling_freq();
String billing_flag=svc.getBilling_flag();
String due_date=svc.getDue_date();
String sec_due_date=svc.getSec_due_date();
String inv_currency=svc.getInv_currency();
String payment_currency=svc.getPayment_currency();
String interest_rate_cd=svc.getInterest_rate_cd();
String interest_cal_sign=svc.getInterest_cal_sign();
String interest_cal_per=svc.getInterest_cal_per();
String exchng_rate_cd=svc.getExchng_rate_cd();
String exchng_cal=svc.getExchng_cal();
String exchng_criteria=svc.getExchng_criteria();
String exchng_note=svc.getExchng_note();

String due_dt_in=svc.getDue_dt_in();
String exclude_sat=svc.getExclude_sat();
if(due_dt_in.equals(""))
{
	due_dt_in="C";
}
if(payment_currency.equals(""))
{
	payment_currency="2";
}
if(inv_currency.equals(""))
{
	inv_currency="2";
}
if(billing_freq.equals(""))
{
	billing_freq="D";
}
%>
<body>
<form method="post" action="../servlet/Frm_cargo_service_cont_master">
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
					    	<%=utilBean.getCounterpartyABBR(counterparty_cd, owner_cd)%> [<%=display_map_id%>] Billing Details
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Billing on<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="billing_flag" style="pointer-events: none;">
							     		<option value="D"> [D] Delivery Period </option>
							     	</select>
							     	<%-- <script>document.forms[0].billing_flag.value="<%=billing_flag%>"</script> --%>
				    			</div>
				    		</div>
				    	</div>
					
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Billing Period<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
			    				<select class="form-select form-select-sm" name="freq" >
			    					<option value="D" selected="selected">Delivery Period</option>
			    				</select>
			    				<script>document.forms[0].freq.value="<%=billing_freq%>"</script>
				    		</div>
						</div>
					</div>
					<div class="row m-b-5">
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
					<div>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Invoice Currency</label>
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
								     		<option value="1">INR</option>
								     		<%if(final_rate_unit.equals("2") || provisional_rate_unit.equals("2")){ %>
								     		<option value="2">USD</option>
								     		<%} %>
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
								     		<option value="1">INR</option>
								     		<!-- <option value="2">USD</option> -->
								     	</select>
								     	<script>document.forms[0].payment_currency.value="<%=payment_currency%>"</script>
					    			</div>
					    		</div>
					    	</div>
						</div>
					</div>
					<%if(final_rate_unit.equals("2") || provisional_rate_unit.equals("2")){ %>
					<div>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Exchange Rate Calculation</label>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">
								<label class="form-label"><b>Exchange Rate<span class="s-red">*</span></b></label>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<select class="form-select form-select-sm" name="exchng_rate">
								    		<option value="">--Select--</option>
								    		<%for(int i=0;i<VEXCHNG_RATE_CD.size();i++){%>	
								    		<option value="<%=VEXCHNG_RATE_CD.elementAt(i)%>"><%=VEXCHNG_RATE_NM.elementAt(i)%></option>
									     	<%}%>
								    	</select>
								    	<script>document.forms[0].exchng_rate.value="<%=exchng_rate_cd%>"</script>
								    </div>
								</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">
								<label class="form-label"><b>Exchange Rate Calculation<span class="s-red">*</span></b></label>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">
								<div class="form-group row">
					    			<div class="col">
					    				<select class="form-select form-select-sm" name="exch_calc_base" onchange="showExchngCriteria(this);">
								    		<option value="D">On Particular Day Base</option>
								     		<!-- <option value="A">On Daily Basis of Billing Period</option> -->
								     	</select>
								     	<script>document.forms[0].exch_calc_base.value="<%=exchng_cal%>"</script>
					    			</div>
									<div class="col">
					    				<select class="form-select form-select-sm" name="inv_criteria" id="exchngCri" <%if(!exchng_cal.equals("D")){ %>style="display:none;"<%} %>>
								    		<option value="">--Select--</option>
								     		<option value="INV">Date of Invoice</option>
								     		<option value="LST">Last Day of Billing Cycle</option>
								     		<option value="PRE">Previous Day of Billing Cycle</option>
								     		<option value="DUE">Date of Payment</option>
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
					<%} %>
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
										<th>Entity BU</th>
										<th>Invoice Category</th><!-- Pratham Bhatt: for invoice Category  -->
										<th>Invoice Type</th><!-- Pratham Bhatt: for invoice Type  -->
										<th>Tax Structure Details</th> 
									</tr>
								</thead>
								<tbody>
								<%if(VPLANT_NAME.size()>0){ %>
									<%for(int i=0; i<VPLANT_NAME.size(); i++){ %>
										<tr style="background:#99ffcc;">
											<td align="center"><%=VBU_PLANT_NM.elementAt(i)%></td>
											<td align="center"><%=VPLANT_NAME.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_CATEGORY.elementAt(i) %></td> <!-- Pratham Bhatt: for invoice Category  -->
											<td align="center"><%=VINVOICE_TYPE.elementAt(i)%></td> <!-- Pratham Bhatt: for invoice Type  -->
											<td>[<%=VTAX_STRUCT_CD.elementAt(i)%>]- <%=VTAX_STRUCT_NM.elementAt(i)%></td>
										</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="5" align="center"><%=utilmsg.infoMessage("<b>No Taxes Configured!</b>") %></td>
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

<input type="hidden" name="option" value="SVC_BILLING_DTL">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="entity_type" value="<%=entity_type%>">  	
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="start_dt" value="<%=start_dt%>">
<input type="hidden" name="provisional_rate_unit" value="<%=provisional_rate_unit%>">
<input type="hidden" name="final_rate_unit" value="<%=final_rate_unit%>">

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