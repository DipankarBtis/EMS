<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	
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
	
	var url = "frm_purchase_cr_dr_note_gen.jsp?form_cd="+formCd+"&form_nm="+formNm+"&counterparty_cd="+counterparty_cd+
			"&mod_cd="+mod_cd+"&mod_nm="+mod_nm+"&month="+month+"&year="+year+"&billing_cycle="+billing_cycle+
			"&read_access="+read_access+"&write_access="+write_access+"&check_access="+check_access+
			"&print_access="+print_access+"&delete_access="+delete_access+"&audit_access="+audit_access+
			"&authorize_access="+authorize_access+"&approve_access="+approve_access+"&execute_access="+execute_access;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.remittance.DataBean_Remittance" id="remittance" scope="request"></jsp:useBean>
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

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String billing_cycle=request.getParameter("billing_cycle")==null?"0":request.getParameter("billing_cycle");

if(month.length() == 1)
{
	month="0"+month; 
}

remittance.setCallFlag("INVOICE_DR_CR_NOTE_GEN");
remittance.setComp_cd(owner_cd);
remittance.setCounterparty_cd(counterparty_cd);
remittance.setMonth(month);
remittance.setYear(year);
remittance.setBilling_cycle(billing_cycle);
remittance.init();

Vector VCOUNTERPARTY_CD = remittance.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = remittance.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = remittance.getVCOUNTERPARTY_ABBR();

Vector VINVOICE_DT = remittance.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = remittance.getVINVOICE_DUE_DT();
Vector VINVOICE_NO = remittance.getVINVOICE_NO();
Vector VINVOICE_SEQ = remittance.getVINVOICE_SEQ();
Vector VPERIOD_START_DT = remittance.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = remittance.getVPERIOD_END_DT();
Vector VFREQ = remittance.getVFREQ();
Vector VFREQ_NM = remittance.getVFREQ_NM();
Vector VALLOC_QTY = remittance.getVALLOC_QTY();
Vector VSALES_PRICE = remittance.getVSALES_PRICE();
Vector VSALES_PRICE_UNIT = remittance.getVSALES_PRICE_UNIT();
Vector VRATE_NM = remittance.getVRATE_NM();
Vector VGROSS_AMT = remittance.getVGROSS_AMT();
Vector VTAX_AMT = remittance.getVTAX_AMT();
Vector VINVOICE_AMT = remittance.getVINVOICE_AMT();
Vector VADJ_SIGN = remittance.getVADJ_SIGN();
Vector VADJ_AMT = remittance.getVADJ_AMT();
Vector VNET_PAYABLE = remittance.getVNET_PAYABLE();
Vector VEXCHNAGE_RATE = remittance.getVEXCHNAGE_RATE();
Vector VSALES_PRICE_USD = remittance.getVSALES_PRICE_USD();
Vector VSALES_PRICE_UNIT_USD = remittance.getVSALES_PRICE_UNIT_USD();
Vector VRATE_NM_USD = remittance.getVRATE_NM_USD();
Vector VGROSS_AMT_USD = remittance.getVGROSS_AMT_USD();
Vector VTAX_AMT_USD = remittance.getVTAX_AMT_USD();
Vector VINVOICE_AMT_USD = remittance.getVINVOICE_AMT_USD();
Vector VADJ_SIGN_USD = remittance.getVADJ_SIGN_USD();
Vector VADJ_AMT_USD = remittance.getVADJ_AMT_USD();
Vector VNET_PAYABLE_USD = remittance.getVNET_PAYABLE_USD();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form action="">

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
					    	Trader CR/DR Note Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-1 col-xs-1 col-md-1"></div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">  	
					    			<label class="form-label"><b>Select Trader</b></label>
					  			</div>
								<div class="col">  
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh();">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Month/Year</b></label>
					  			</div>
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
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Billing Cycle</b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="billing_cycle" onchange="refresh();">
					  					<option value="0">All</option>
					  					<option value="1">1st-Fortnight</option>
										<option value="2">2nd-Fortnight</option>
										<option value="3">1st-Weekly</option>
										<option value="4">2nd-Weekly</option>
										<option value="5">3rd-Weekly</option>
										<option value="6">4th-Weekly</option>
										<option value="9">5th-Weekly</option>
										<option value="7">Monthly</option>
										<option value="8">Other</option>
					  				</select>
					  				<script>document.forms[0].billing_cycle.value="<%=billing_cycle%>"</script>
					  			</div>
					  		</div>
					  	</div>
					  	<div class="col-sm-1 col-xs-1 col-md-1"></div>
					</div>
					&nbsp;
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th rowspan="2"></th>
											<th rowspan="2">Seq#</th>
											<th rowspan="2">Invoice#</th>
											<th rowspan="2">Billing Cycle</th>
											<th rowspan="2">Invoice Period</th>
											<th rowspan="2">Invoice Date</th>
											<th rowspan="2">Invoice Due Date</th>
											<th rowspan="2">Invoiced<br>MMBTU</th>
											<th rowspan="2">Exchange Rate</th>
											<th colspan="6">USD Details</th>
											<th colspan="6">INR Details</th>
											<th rowspan="2">Payment Status</th>
											<th rowspan="2">Payment Received</th>
											<th rowspan="2">CR/DR</th>
										</tr>
										<tr>
											<th>Price per MMBTU</th>
											<th>Gross Amount</th>
											<th>Tax</th>
											<th>Invoice Amount</th>
											<th>Adjustment Amount</th>
											<th>Net Payable</th>
											<th>Price per MMBTU</th>
											<th>Gross Amount</th>
											<th>Tax</th>
											<th>Invoice Amount</th>
											<th>Adjustment Amount</th>
											<th>Net Payable</th>
										</tr>
									</thead>
									<tbody>
									<%if(VINVOICE_SEQ.size() > 0){ %>
										<%for(int i=0; i<VINVOICE_SEQ.size(); i++){ %>
										<tr>
											<td align="center">
												
											</td>
											<td align="center">
												<%=VINVOICE_SEQ.elementAt(i)%>
											</td>
											<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
											<td align="center"><%=VFREQ_NM.elementAt(i)%></td>
											<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
											<td align="right"><%=VALLOC_QTY.elementAt(i)%></td>
											<td align="right"><%=VEXCHNAGE_RATE.elementAt(i)%></td>
											<td align="right"><%=VSALES_PRICE_USD.elementAt(i)%><!-- &nbsp; --><%//=VRATE_NM_USD.elementAt(i)%></td>
											<td align="right"><%=VGROSS_AMT_USD.elementAt(i)%></td>
											<td align="right"><%=VTAX_AMT_USD.elementAt(i) %></td>
											<td align="right"><%=VINVOICE_AMT_USD.elementAt(i)%></td>
											<td align="right"><%=VADJ_SIGN_USD.elementAt(i)%><%=VADJ_AMT_USD.elementAt(i)%></td>
											<td align="right"><%=VNET_PAYABLE_USD.elementAt(i)%></td>
											<td align="right"><%=VSALES_PRICE.elementAt(i)%><!-- &nbsp; --><%//=VRATE_NM.elementAt(i)%></td>
											<td align="right"><%=VGROSS_AMT.elementAt(i)%></td>
											<td align="right"><%=VTAX_AMT.elementAt(i) %></td>
											<td align="right"><%=VINVOICE_AMT.elementAt(i)%></td>
											<td align="right"><%=VADJ_SIGN.elementAt(i)%><%=VADJ_AMT.elementAt(i)%></td>
											<td align="right"><%=VNET_PAYABLE.elementAt(i)%></td>
											<td align="center"></td>
											<td align="right"></td>
											<td align="center">
												<input type="button" class="btn btn-warning com-btn" value="Add">
											</td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="24" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Month!</b>") %></td>
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