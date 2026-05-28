<%@page import="javax.management.modelmbean.RequiredModelMBean"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh(accroid)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	var segment = document.forms[0].segment.value;
	
	if(document.getElementById("inv_title_"+accroid)!=null && document.getElementById("inv_title_"+accroid)!=undefined)
	{
		inv_title = document.getElementById("inv_title_"+accroid).value;
	}
	
	var url = "frm_set_debit_credit_note.jsp?counterparty_cd="+counterparty_cd+"&month="+month+"&year="+year+"&billing_cycle="+billing_cycle+"&segment="+segment+"&accroid="+accroid;
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setCriteria(index)
{
    var chk = document.forms[0].chk;
    if (chk.length !== undefined) {
		var select = document.forms[0].criteria_nm[index];
    }
    else {
		var select = document.forms[0].criteria_nm;
    }
    var selectedValues = [];

    for (var i = 0; i < select.options.length; i++) {
        if (select.options[i].selected) {
            selectedValues.push(select.options[i].value);
        }
    }

    var criteriaString = selectedValues.join("@");
    
   /*  example :selectedValues = ["1", "2", "3"];
    criteriaString = "1@2@3"; */
    if (chk.length !== undefined) {
    	document.forms[0].criteriaDesc[index].value = criteriaString;
    }
    else {
    	document.forms[0].criteriaDesc.value = criteriaString;
    }
    
}
function Search(obj, indx, j) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+j);
  	
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

function doSubmit(){
	
	var dr_cr_flag = document.forms[0].dr_cr_flag;
	var criteriaDesc = document.forms[0].criteriaDesc;
	var chk = document.forms[0].chk;
	var remark=document.forms[0].remark;
	var msg="";
	var flag=true;
	
	var index = 0;
	if (chk.length !== undefined) {
		
		for (var i = 0; i < chk.length; i++) {
			if (chk[i].checked) {
				index = i;
				flag = true;
				break;
			}
			else {
				flag = false;
			}
		}
		
		if (!flag) {
	   	 	msg += "Select any one radio button.\n";
	        msg += "Select Debit/Credit Flag.\n";
	        msg += "Select atleast one Criteria.\n";
	        msg += "Enter Reason.\n";
		}
	
		if (index != -1 && (dr_cr_flag[index].value == "" || dr_cr_flag[index].value == "0")) {
	        msg += "Select Debit/Credit Flag.\n";
	        flag = false;
	    }
		if (index != -1 && criteriaDesc[index].value == "") {
	        msg += "Select atleast one Criteria.\n";
	        flag = false;
	    }
		if (index != -1 && criteriaDesc[index].value == "0") {
	        msg += "Select atleast one Criteria.\n";
	        flag = false;
	    }
		if (index != -1 && remark[index].value == "") {
	        msg += "Please Enter Reason!\n";
	        flag = false;
	    }
	}
	else {
		if (!chk.checked) {
	   	 	msg += "Select any one radio button.\n";
	   	 	flag = false;
		}
	
		if (dr_cr_flag.value == "" || dr_cr_flag.value == "0") {
	        msg += "Select Debit/Credit Flag.\n";
	        flag = false;
	    }
		if (criteriaDesc.value == "") {
	        msg += "Select atleast one Criteria.\n";
	        flag = false;
	    }
		if (criteriaDesc.value == "0") {
	        msg += "Select atleast one Criteria.\n";
	        flag = false;
	    }
		if (remark.value == "") {
	        msg += "Please Enter Reason!\n";
	        flag = false;
	    }
	}
	
	if(flag)
	{
		document.forms[0].index.value = index;
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].submit();
	}
	else
	{
		alert(msg);
	}
	
}

function setEnableDisable(index) {

	var dr_cr_flag = document.forms[0].dr_cr_flag;
	var criteria_nm = document.forms[0].criteria_nm;
	var chk = document.forms[0].chk;
	
	if (chk.length !== undefined) {
		for (var i = 0; i < dr_cr_flag.length; i++) {
			if (i == index) {
				dr_cr_flag[i].disabled = false;
				criteria_nm[i].disabled = false;
			}
			else {
				dr_cr_flag[i].disabled = true;
				criteria_nm[i].disabled = true;
			}
		}
	}
	else {
		 dr_cr_flag.disabled = false;
		 criteria_nm.disabled = false;
	}
}

</script>
</head>

<jsp:useBean class="com.etrm.fms.sales_invoice.DataBean_Sales_Drcr_note" id="Drcr_note" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<% 
String sysdate = utildate.getSysdate();
String sys_date_num = "0"; 
int currentYear = 0;
int currentMonth = 0;
String temp_billing_cycle="";
if(!sysdate.equals(""))
{
	String[] sys_temp = sysdate.split("/");
	sys_date_num=sys_temp[0];
}

String prvMonthDate="";
if(Integer.parseInt(sys_date_num) <= 15)
{
	temp_billing_cycle="2";
	prvMonthDate=utildate.getFirstDateOfPreviousMonth(sysdate);
}
else
{
	temp_billing_cycle="1";
	prvMonthDate=sysdate;
}

String date_num = "0"; 
if(!prvMonthDate.equals(""))
{
	String[] temp = prvMonthDate.split("/");
	date_num=temp[0];
	currentMonth=Integer.parseInt(temp[1]);
	currentYear=Integer.parseInt(temp[2]);
}

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String period_start_dt=request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
String dr_cr_flag=request.getParameter("dr_cr_flag")==null?"0":request.getParameter("dr_cr_flag");
String chk=request.getParameter("chk")==null?"":request.getParameter("chk");
String criteriaDesc=request.getParameter("criteriaDesc")==null?"":request.getParameter("criteriaDesc");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
String drcr_no="";

Drcr_note.setCallFlag("SET_DR_CR_NOTE");
Drcr_note.setMonth(month);
Drcr_note.setYear(year);
Drcr_note.setComp_cd(owner_cd);
Drcr_note.setCounterparty_cd(counterparty_cd);
/* Drcr_note.setAgmt_no(agmt_no);
Drcr_note.setAgmt_rev_no(agmt_rev);
Drcr_note.setCont_no(cont_no);
Drcr_note.setCont_rev_no(cont_rev); */
Drcr_note.setContract_type(contract_type);
/* Drcr_note.setPlant_seq(plant_seq); */
Drcr_note.setBilling_cycle(billing_cycle);
Drcr_note.setPeriod_start_dt(period_start_dt);
Drcr_note.setPeriod_end_dt(period_end_dt);
Drcr_note.setSegment(segment);
Drcr_note.init();

if(billing_cycle.equals(""))
{
	billing_cycle=temp_billing_cycle;
}

if(month.length() == 1)
{
	month="0"+month; 
}

Vector VCOUNTERPARTY_CD = Drcr_note.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = Drcr_note.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = Drcr_note.getVCOUNTERPARTY_NM();
Vector VDRCR_INVOICE_DT = Drcr_note.getVDRCR_INVOICE_DT();
Vector VDRCR_DUE_DT = Drcr_note.getVDRCR_DUE_DT();
Vector VDRCR_INVOICE_NO = Drcr_note.getVDRCR_INVOICE_NO();
Vector VDRCR_INVOICE_SEQ = Drcr_note.getVDRCR_INVOICE_SEQ();
Vector VDRCR_PERIOD_START_DT = Drcr_note.getVDRCR_PERIOD_START_DT();
Vector VDRCR_PERIOD_END_DT = Drcr_note.getVDRCR_PERIOD_END_DT();
Vector VDRCR_START_DT = Drcr_note.getVDRCR_START_DT();
Vector VDRCR_END_DT = Drcr_note.getVDRCR_END_DT();
Vector VDRCR_CRITERIA = Drcr_note.getVDRCR_CRITERIA();

Vector VDRCR_REMARK = Drcr_note.getVDRCR_REMARK(); 
Vector DRCR_DUE_DT = Drcr_note.getDRCR_DUE_DT();
Vector DRCR_DT = Drcr_note.getDRCR_DT();
Vector DRCR_PERIOD_START_DT = Drcr_note.getDRCR_PERIOD_START_DT();
Vector DRCR_PERIOD_END_DT = Drcr_note.getDRCR_PERIOD_END_DT();
Vector DRCR_INVOICE_NO = Drcr_note.getDRCR_INVOICE_NO();

Vector VDEAL_NO = Drcr_note.getVDEAL_NO();
Vector VDEAL_CONT_REF_NO = Drcr_note.getVDEAL_CONT_REF_NO();


Vector VDRCR_LIST_ABBR = Drcr_note.getVDRCR_LIST_ABBR();
Vector VDRCR_LIST_NM = Drcr_note.getVDRCR_LIST_NM();
Vector VINDEX = Drcr_note.getVINDEX();


VDRCR_CRITERIA.add("CHANGE IN EXCHNG RATE");
VDRCR_CRITERIA.add("CHANGE IN PRICE");
/* VDRCR_CRITERIA.add("CHANGE IN QTY");
VDRCR_CRITERIA.add("CHANGE IN TRANSPORTATION TARIFF");
VDRCR_CRITERIA.add("CHANGE IN MARKET MARGIN");
VDRCR_CRITERIA.add("CHANGE IN OTHER CHARGES");
VDRCR_CRITERIA.add("CHANGE IN TAX %");
VDRCR_CRITERIA.add("CHANGE IN TAX STRUCTURE");
VDRCR_CRITERIA.add("CHANGE IN BU"); */

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Sales_Drcr_note">

<div class="card-body cdbody">
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
					    	Sales Debit/Credit Note
					    </div>
					    <div class="row justify-content-end">
							<div class="col-auto">
							 	<div class="btn-group">
							 	<!-- SagarB20250906 -->
									<%-- <select class="btn btn-outline-secondary btngrp btnactive" name="segment" onchange="refresh();">
										<option value="">All</option>
										<%for(int i=0;i<VDRCR_LIST_ABBR.size();i++){ %>
										<option value="<%=VDRCR_LIST_ABBR.elementAt(i)%>"><%=VDRCR_LIST_ABBR.elementAt(i)%></option>
										<%} %>
									</select> --%>
									<select class="btn btn-outline-secondary btngrp btnactive" name="segment" onchange="refresh();">
										<option value="">All</option>
										<option value="RLNG">RLNG</option>
										<option value="LTCORA">LTCORA</option>
									</select>
								</div>
								 <script>document.forms[0].segment.value="<%=segment%>"</script> 
							</div>
						</div>
					</div>
				</div>
				
				<!-- Counterparty / Month / Year / Billing Cycle  -->
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-1 col-xs-1 col-md-1"></div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">  	
					    			<label class="form-label"><b>Select Customer</b></label>
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
					  					<%for(int i=(currentYear); i > (currentYear-10);i--){ %>
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
										<!-- <option value="3">1st-Weekly</option>
										<option value="4">2nd-Weekly</option>
										<option value="5">3rd-Weekly</option>
										<option value="6">4th-Weekly</option>
										<option value="9">5th-Weekly</option>
										<option value="7">Monthly</option>
										<option value="8">Other</option> -->
					  				</select>
					  				<script>document.forms[0].billing_cycle.value="<%=billing_cycle%>"</script>
					  			</div>
					  		</div>
					  	</div>
					  	<div class="col-sm-1 col-xs-1 col-md-1"></div>
					</div>
				</div>
				
				<!-- INVOICE LIST -->
				<div class="card-body cdbody">
				<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Invoice List </label>
				</div>
					<%int i=0,k=0,l=0,m=0;
					for(int j=0; j<VDRCR_LIST_ABBR.size(); j++)
					{ 
						int index=Integer.parseInt(""+VINDEX.elementAt(j));
						String heading = ""+VDRCR_LIST_ABBR.elementAt(j);
					%>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
						<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="<%=heading%>">
	   										<button name="sub_module_cd" class="accordion-button <%if(!accroid.equals(heading)){%>collapsed<%}%> accor-btn" type="button" 
	   											data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="<%if(!accroid.equals(heading)){%>true<%}else{%>false<%} %>" aria-controls="collapse<%=j%>">
								    			<%=VDRCR_LIST_NM.elementAt(j)%>&nbsp;
								    			<font color="blue">(<%=index%> Items)</font> 
								      		</button>	
								    	</h2>
									<div id="collapse<%=j%>" class="accordion-collapse collapse <%if(accroid.equals(heading)){%>show<%}%>" aria-labelledby="<%=heading%>">
								<div class="accordion-body accor-body">
							<div class="row">
							<div class="table-responsive">
								<table class="table table-bordered table-hover" id="example1">
									<thead>
										<tr>
											<th rowspan="2">Select</th>
											<th rowspan="2">Contract No<br>[Contract/Trade Ref#]
											<br><div align="center"><input class="form-control form-control-sm" type="text" placeholder="Search.." id = "cont_no" onkeyup="Search(this,'1','1')" style="width:100px"/></div></th>
											<th rowspan="2">Invoice# 
											<br><div align="center"><input class="form-control form-control-sm" type="text" placeholder="Search.." id = "invoice_no" onkeyup="Search(this,'2','1')" style="width:100px"/></div></th>
											<th rowspan="2">Invoice Period</th>
											<th rowspan="2">Invoice Date
											<br><div align="center"><input class="form-control form-control-sm" type="text" placeholder="Search.." id = "dt" onkeyup="Search(this,'3','1')" style="width:100px"/></div></th>
											<th rowspan="2">Invoice Due Date</th>
											<th rowspan="2">Debit/Credit<span class="s-red">*</span></th>
											<th rowspan="2">Criteria<span class="s-red">*</span></th>
											<th rowspan="2">Reason<span class="s-red">*</span></th>
										</tr>
									</thead>
									<tbody>
									<%k=0;
									// SagarB20250906
									if(index>0)
									{ 
									for(i=i; i<VDRCR_INVOICE_SEQ.size(); i++){
										k+=1;
										
									%>
										<tr>
											<td align="center">
												<input type="radio" name="chk" onclick="setEnableDisable('<%=i%>')">
											    <%=i+1%>
											    <script>document.forms[0].chk.value="<%=chk%>"</script>
											</td>
											<td align="center">
												<font color="blue"><%=VDEAL_NO.elementAt(i)%></font><br>[<%=VDEAL_CONT_REF_NO.elementAt(i)%>]
												<input type="hidden" name="deal_no" id="deal_no" value="<%=VDEAL_NO.elementAt(i)%>">
												<input type="hidden" name="deal_ref" id="deal_ref" value="<%=VDEAL_CONT_REF_NO.elementAt(i)%>">
											</td>
											<td align="center"><%=VDRCR_INVOICE_NO.elementAt(i)%>
											 <input type="hidden" name="inv_no" id="inv_no" value="<%= VDRCR_INVOICE_NO.elementAt(i)%>">
											 <input type="hidden" name="drcr_ref" value="<%=VDRCR_INVOICE_NO.elementAt(i)%>">
											 <input type="hidden" name="invoice_seq" value="<%=VDRCR_INVOICE_SEQ.elementAt(i)%>">
											</td>
											<td align="center"><%=VDRCR_START_DT.elementAt(i)%> - <%=VDRCR_END_DT.elementAt(i)%>
											<input type="hidden" name="start_dt" id="start_dt" value="<%=VDRCR_START_DT.elementAt(i)%>">
											<input type="hidden" name="end_dt" id="end_dt" value="<%=VDRCR_END_DT.elementAt(i)%>">
											</td>
											<td align="center">
												<%=VDRCR_INVOICE_DT.elementAt(i)%>
												<input type="hidden" name="invoice_dt" id="invoice_dt" value="<%=VDRCR_INVOICE_DT.elementAt(i)%>">
											</td>
											<td align="center">
												<%=VDRCR_DUE_DT.elementAt(i)%>
												<input type="hidden" name="inv_due_dt" id="inv_due_dt" value="<%=VDRCR_DUE_DT.elementAt(i)%>">
											</td>
											<td align="center">
												<select class="col-md-6 col-sm-6 col-xs-6" name="dr_cr_flag" disabled>
													<option value="0" >--Select--</option>
													<option value="DR">Debit</option>
													<option value="CR">Credit</option>
												</select>
												<script>document.forms[0].dr_cr_flag.value="<%=dr_cr_flag%>"</script> 
											</td>
											<td align="center" class="col-auto" title="Hold Ctrl/ Shift to select multiple options">
											    <select class="form-select form-select-sm" name="criteria_nm" id="criteria_nm" multiple="multiple" onchange="setCriteria(<%=i%>)" disabled>
											        <option value="0" >--Select--</option>
											        <% for(l=0; l < VDRCR_CRITERIA.size(); l++) { %>	<!-- SagarB20250905 -->
											            <option value="<%=l+1%>"><%= VDRCR_CRITERIA.elementAt(l) %></option>
											        <% } %>
											    </select>
											    <input type="hidden" name="criteriaDesc" id="criteriaDesc" value="">
											    <script>document.forms[0].criteriaDesc.value="<%=criteriaDesc%>"</script>
											</td>
											<td align="center">
												<div class="col-md-12 col-sm-12 col-xs-12">  
													<!-- <div class="form-group row">  -->
										    			 <!-- <div class="col-sm-10 col-xs-10 col-md-10"> -->
										    				<textarea class="form-control form-control-sm"  id="remark<%=i%>"  name="remark" rows="3" maxlength="150" value=""><%=VDRCR_REMARK.elementAt(i)%></textarea>
										    				<%=VDRCR_REMARK.elementAt(i)%>
										      			<!-- </div> -->
										  			</div>
												<!-- </div> -->
											</td>
										</tr>
									
										<%if(k==index){
													i=i+1;
													break;
											} %>   
											
										<%}%>
										 <%}else{ %>
											<tr>
												<td colspan="40" align="center"><%=utilmsg.infoMessage("<b>No Debit/Credit is Generated for Selected Month!</b>") %></td>
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
		
		<%-- <!-- DRCR  -->
					<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Debit/Credit List </label>
					</div>
					<%
					for(int j=0; j<VDRCR_LIST_ABBR.size(); j++)
					{ 
						/* int index=Integer.parseInt(""+VINDEX.elementAt(j)); */
						String heading = ""+VDRCR_LIST_ABBR.elementAt(j);
						%>
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="<%=heading%>">
	   										<button name="sub_module_cd" class="accordion-button <%if(!accroid.equals(heading)){%>collapsed<%}%> accor-btn" type="button" 
	   											data-bs-toggle="collapse" data-bs-target="#collapse1<%=j%>" aria-expanded="<%if(!accroid.equals(heading)){%>true<%}else{%>false<%} %>" aria-controls="collapse1<%=j%>">
								    			<%=VDRCR_LIST_NM.elementAt(j)%>&nbsp;
								    			<font color="blue">(<%=index%> Items)</font>
								      		</button>	
								    	</h2>
										<div id="collapse1<%=j%>" class="accordion-collapse collapse <%if(accroid.equals(heading)){%>show<%}%>" aria-labelledby="<%=heading%>">
								      		<div class="accordion-body accor-body">
								      			<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
														<thead>
															<tr>
																<th rowspan="2">Select</th>
																<th rowspan="2">Contract No<br>[Contract/Trade Ref#]
																<br><div align="center"><input class="form-control form-control-sm" type="text" placeholder="Search.." id = "cont_no" onkeyup="Search(this,'1','2')" style="width:100px"/></div>
																</th>
																<th rowspan="2">Invoice#
																<br><div align="center"><input class="form-control form-control-sm" type="text" placeholder="Search.." id = "invoice_no" onkeyup="Search(this,'2','2')" style="width:100px"/></div>
																</th>
																<th rowspan="2">DR/CR#</th>
																<th rowspan="2">Invoice Period</th>
																<th rowspan="2">Invoice Date
																<br><div align="center"><input class="form-control form-control-sm" type="text" placeholder="Search.." id = "dt" onkeyup="Search(this,'3','2')" style="width:100px"/></div>
																</th>
																<th rowspan="2">Invoice Due Date</th>
																<th rowspan="2">Credit/ Debit</th>
																<th rowspan="2">Criteria</th>
																<th rowspan="2">Reason<span>*</span></th>
															</tr>
														</thead>
														<tbody>
														<%k=0;
														/* if(index>0)
														{ */%>
														
														<%for(i=0; i<DRCR_INVOICE_NO.size(); i++){ %>
															<tr>
																<td align="center">
																	<input type="radio" name="chk" onclick="setEnableDisable('<%=i%>')">
																    <%=i+1%>
																    <script>document.forms[0].chk.value="<%=chk%>"</script>
																</td>
																<td align="center">
																	<%=VDEAL_NO.elementAt(i)%>
																	<input type="hidden" name="drcr_no" value="<%=VDEAL_NO.elementAt(i)%>">
																	<input type="hidden" name="drcr_ref" value="<%=VDEAL_CONT_REF_NO.elementAt(i)%>">
																</td>
																<td align="center">
																	<%=DRCR_PERIOD_START_DT.elementAt(i)%> - <%=DRCR_PERIOD_END_DT.elementAt(i)%>
																	<input type="hidden" name="drcr_start_dt" id="drcr_start_dt" value="<%=DRCR_PERIOD_START_DT.elementAt(i)%>">
																	<input type="hidden" name="drcr_end_dt" id="drcr_end_dt" value="<%=DRCR_PERIOD_END_DT.elementAt(i)%>">
																</td>
																<td align="center">
																	<%=DRCR_DT.elementAt(i)%>
																	<input type="hidden" name="drcr_dt" id="drcr_dt" value="<%=DRCR_DT.elementAt(i)%>">
																</td>
																<td align="center">
																	<%=DRCR_DUE_DT.elementAt(i)%>
																	<input type="hidden" name="drcr_due_dt" id="drcr_due_dt" value="<%=DRCR_DUE_DT.elementAt(i)%>">
																</td>
																<td align="center">
																	<select class="col-md-6 col-sm-6 col-xs-6" name="dr_cr_flag">
													  					<option value="0">--Select--</option>
													  					<option value="DR">Debit</option>
																		<option value="CR">Credit</option>
													  				</select>
													  				<script>document.forms[0].dr_cr_flag.value="<%=dr_cr_flag%>"</script>
																</td>
																<td align="center">
											 						  <select class="form-select form-select-sm" name="criteria_nm" id="criteria_nm" multiple="multiple" onchange="setCriteria(<%=i%>)" disabled>
																        <option value="0" >--Select--</option>
																        <% for(l=0; l < VDRCR_CRITERIA.size(); l++) { %>
																            <option value="<%=j+1%>"><%= VDRCR_CRITERIA.elementAt(l) %></option>
																        <% } %>
																    </select>
																    <input type="hidden" name="criteriaDesc2"  value="">
																    <script>document.forms[0].criteriaDesc2.value="<%=criteriaDesc%>"</script>
																</td>
																<td align="center">
																	<div class="col-sm-12 col-xs-12 col-md-12">  
																		<div class="form-group row">
															    			<div class="col-sm-10 col-xs-10 col-md-10">
															    				<textarea class="form-control form-control-sm"  name="remark1" rows="3" maxlength="160"></textarea>
															      			</div>
															  			</div>
																	</div>
																</td>
															</tr>
															<%} %>
															<%}else{ %>
																<tr>
																	<td colspan="40" align="center"><%=utilmsg.infoMessage("<b>No Debit/Credit is Generated for Selected Month!</b>") %></td>
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
				</div> --%>
		
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="SET_DRCR_NOTE">
<input type="hidden" name="index" value="">
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