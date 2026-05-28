<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--Harsh Maheta 20250708 : DLNG C-Form Entry/Mst-->
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh()
{
	var financial_year = document.forms[0].financial_year.value;
	var counterparty = document.forms[0].counterparty.value;
	var state = document.forms[0].state.value;
	var period_start_dt = document.forms[0].period_frm.value;
	var period_end_dt = document.forms[0].period_to.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_cfrom_dtl.jsp?financial_year="+financial_year+"&counterparty="+counterparty+
			"&state="+state+"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+
			"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var flag = true;
	var msg = "";
	
	var financial_year = document.forms[0].financial_year.value
	var counterparty = document.forms[0].counterparty.value
	var state = document.forms[0].state.value

	var cformNo = document.forms[0].cformNo.value
	var cformDt = document.forms[0].cformDt.value
	var period_frm = document.forms[0].period_frm.value
	var period_to = document.forms[0].period_to.value
	var cform_total_amt = document.forms[0].cform_total_amt.value

	var td = document.getElementById('total_inv_amt_td');
	var total = parseFloat(td.textContent.trim()) || 0;
	var fileInput = document.getElementById('file_upload');
	
	var inv_chk = document.forms[0].inv_chk;
	
	var countChk=parseInt("0");
	
	if(inv_chk!=null && inv_chk!=undefined)
	{
		if(inv_chk.length!=undefined)
		{
			for(var i=0;i<inv_chk.length;i++)
			{
				if(inv_chk[i].checked)
				{
					countChk=parseInt(countChk) + 1;
				}
			}
		}
		else
		{
			if(inv_chk.checked)
			{
				countChk=parseInt(countChk) + 1;
			}
		}
	}
	
	if(financial_year == "0" || financial_year == "" || financial_year == null)
	{
		msg="Please Select Financial Year!!\n";
		flag=false;
	}
	if(counterparty == "0" || counterparty == " " || counterparty == null)
	{
		msg+="Please Select Counterparty!!\n";
		flag=false;
	}
	if(state == "0" || state == "00" || state == null)
	{
		msg+="Please Select State!!\n";
		flag=false;
	}
	
	if(cformNo == "" || cformNo == " " || cformNo == null)
	{
		msg+="Please Enter C-Form Serial# !!\n";
		flag=false;
	}
	if(cformDt == "" || cformDt == " " || cformDt == null)
	{
		msg+="Please Enter Date of Issue!!\n";
		flag=false;
	}
	if(period_frm == "" || period_frm == " " || period_frm == null)
	{
		msg+="Please Enter Period Start Date!!\n";
		flag=false;
	}
	if(period_to == "" || period_to == " " || period_to == null)
	{
		msg+="Please Enter Period End Date!!\n";
		flag=false;
	}
	if(cform_total_amt == "" || cform_total_amt == " " || cform_total_amt == null)
	{
		msg+="Please Enter Total Amount!!\n";
		flag=false;
	}
	
	if(parseInt(countChk)<=0)
	{
		msg+="Please Select atleast one(1) Invoice for Submit!\n";
		flag=false;
	}
	
	if (parseFloat(cform_total_amt) < parseFloat(total)) 
	{
		//Removed this condition after Meeting with Vijay : 20250715 
	    msg += "Total Amount should be Grater or equal to Total Selected Invoice Amount!!\n";
	    flag = false;
	}
	
	if(msg!="")
	{
		alert(msg);
	}
	else
	{
		if (!fileInput || fileInput.files.length === 0) 
		{
			var conf = confirm("No file has been uploaded for C-Form Entry!!\nDo you want to continue?");
			if(conf)
			{
				var a = confirm("Do you want Submit C-Form Entry?");
				if(a)
				{
					var conf1 = confirm("Once C-Form Entry is submitted for the Invoice/s, cannot be reverted!!\nAre you sure want to continue?");
					
					if(conf1)
					{
						document.getElementById("loading").style.visibility = "visible";
						document.forms[0].submit();
					}
				}
			}
		}
		else
		{
			var a = confirm("Do you want Submit C-Form Entry?");
			if(a)
			{
				var conf1 = confirm("Once C-Form Entry is submitted for the Invoice/s, cannot be reverted!!\nAre you sure want to continue?");
				
				if(conf1)
				{
					document.getElementById("loading").style.visibility = "visible";
					document.forms[0].submit();
				}
			}
		}
	}
}

function calculateTotalInvAmt()
{
	var no_invoices = document.forms[0].no_invoices.value;
	
	if(parseInt(no_invoices)>0)
	{
		var inv_total = 0;
	    var hold_total = 0;
	    var checkboxes = document.forms[0].elements['inv_chk'];

	    // Convert single checkbox to array if only one checkbox exists
	    if (!checkboxes.length) {
	        checkboxes = [checkboxes];
	    }

	    for (var i = 0; i < checkboxes.length; i++) {
	        if (checkboxes[i].checked) {
	            var amtInput = document.getElementById('inv_amt_' + i);
	            if (amtInput) {
	                var amt = parseFloat(amtInput.value) || 0;
	                inv_total += amt;
	            }
	        }
	    }

	    var totalTd = document.getElementById('total_inv_amt_td');
	    if (totalTd) 
	    {
	    	totalTd.textContent = inv_total.toFixed(2);
	    }

	    for (var i = 0; i < checkboxes.length; i++) {
	        if (checkboxes[i].checked) {
	            var amtInput = document.getElementById('hold_amt_' + i);
	            if (amtInput) {
	                var amt = parseFloat(amtInput.value) || 0;
	                hold_total += amt;
	            }
	        }
	    }

	    var totalTd1 = document.getElementById('total_hold_amt_td');
	    if (totalTd1) 
	    {
	    	totalTd1.textContent = hold_total.toFixed(2);
	    }
	}
}

function EnableDiableInv(obj,index)
{
	var chk = obj;
	
	var invoice_no = document.getElementById("invoice_no_"+index);
	var invoice_dt = document.getElementById("invoice_dt_"+index);
	var invoice_amt = document.getElementById("invoice_amt_"+index);
	var invoice_commodity = document.getElementById("invoice_commodity_"+index);
	var invoice_hold_amt = document.getElementById("invoice_hold_amt_"+index);
	var invoice_bu_state = document.getElementById("invoice_bu_state_"+index);
	var invoice_seq = document.getElementById("invoice_seq_"+index);
	
	if(chk.checked)
	{
		invoice_no.disabled=false;
		invoice_dt.disabled=false;
		invoice_amt.disabled=false;
		invoice_commodity.disabled=false;
		invoice_hold_amt.disabled=false;
		invoice_bu_state.disabled=false;
		invoice_seq.disabled=false;
	}
	else
	{
		invoice_no.disabled=true;
		invoice_dt.disabled=true;
		invoice_amt.disabled=true;
		invoice_hold_amt.disabled=true;
		invoice_bu_state.disabled=true;
		invoice_seq.disabled=true;
		invoice_commodity.disabled=true;
	}
}

function openPdfFile(url)
{
	window.open(url);
}

function doModify(counterparty,financial_year,state,cformNo,cformDt,cform_total_amt,period_frm,period_to,cformCd)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_cfrom_dtl.jsp?financial_year="+financial_year+"&counterparty="+counterparty+"&state="+state+
			"&cform_no="+cformNo+"&period_start_dt="+period_frm+"&period_end_dt="+period_to+"&cformCd="+cformCd+
			"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="dbdlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysDt = utildate.getSysdate();
String currentFinYear = utildate.getFinancialYear(sysDt);

String operation = request.getParameter("operation")==null?"":request.getParameter("operation");
String counterparty  = request.getParameter("counterparty")==null?"0":request.getParameter("counterparty");
String state  = request.getParameter("state")==null?"00":request.getParameter("state");
String financial_year  = request.getParameter("financial_year")==null?"0":request.getParameter("financial_year");
String period_from = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_to = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
String cform_no = request.getParameter("cform_no")==null?"":request.getParameter("cform_no");
String cformCd = request.getParameter("cformCd")==null?"":request.getParameter("cformCd");

String currfinancial_year = utildate.getFinancialYear(sysDt);

//String period_from="";
//String period_to="";

if(!financial_year.equals("0") && cformCd.equals("") && period_from.equals("") && period_to.equals("") )
{
	String[] temp_financial_year=financial_year.split("-");
	
	period_from="01/04/"+temp_financial_year[0];
	
	if(financial_year.equals(currfinancial_year))
	{
		 period_to=sysDt;
	}
	else
	{
		 period_to="31/03/"+temp_financial_year[1];
	}
}

dbdlng.setCallFlag("CFORM_ENTRY_MST");
dbdlng.setComp_cd(owner_cd);
dbdlng.setCounterparty_cd(counterparty);
dbdlng.setState(state);

dbdlng.setPeriod_start_dt(period_from);
dbdlng.setPeriod_end_dt(period_to);
dbdlng.setFinancial_year(financial_year);
dbdlng.setCform_cd(cformCd);
dbdlng.init();

Vector VMST_COUNTERPARTY_CD = dbdlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dbdlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dbdlng.getVMST_COUNTERPARTY_ABBR();

Vector VFINANCIAL_YEAR = dbdlng.getVFINANCIAL_YEAR();
Vector VCOUNTERPARTY_CD = dbdlng.getVCOUNTERPARTY_CD();
Vector VCFORM_CD = dbdlng.getVCFORM_CD();
Vector VCFORM_NO = dbdlng.getVCFORM_NO();
Vector VCFORM_DT = dbdlng.getVCFORM_DT();
Vector VCFORM_FINANCIAL_YEAR = dbdlng.getVCFORM_FINANCIAL_YEAR();
Vector VISSUING_STATE = dbdlng.getVISSUING_STATE();
Vector VPERIOD_FROM = dbdlng.getVPERIOD_FROM();
Vector VPERIOD_TO =dbdlng.getVPERIOD_TO();
Vector VCFORM_AMOUNT =dbdlng.getVCFORM_AMOUNT();
Vector VCFORM_FILE = dbdlng.getVCFORM_FILE();
Vector VNO_OF_INVOICES = dbdlng.getVNO_OF_INVOICES();
Vector VINVOICE_NO = dbdlng.getVINVOICE_NO();
Vector VINVOICE_DT = dbdlng.getVINVOICE_DT();
Vector VGROSS_AMT =dbdlng.getVGROSS_AMT();
Vector VTAX_AMT = dbdlng.getVTAX_AMT();
Vector VINVOICE_AMT = dbdlng.getVINVOICE_AMT();
Vector VINVOICE_HOLD_AMT = dbdlng.getVINVOICE_HOLD_AMT();
Vector VCFORM_ENTRY_EXIST = dbdlng.getVCFORM_ENTRY_EXIST();
Vector VINVOICE_COMMODITY = dbdlng.getVINVOICE_COMMODITY();
Vector VINVOICE_SEQ = dbdlng.getVINVOICE_SEQ();
Vector VBU_STATE_TIN = dbdlng.getVBU_STATE_TIN();

Vector VSTATE_CD =dbdlng.getVSTATE_CD();
Vector VSTATE_NM =dbdlng.getVSTATE_NM();

String noOfInvoice = dbdlng.getNoOfInvoice();
String cform_serial_no=dbdlng.getCform_serial_no();
String cform_dt=dbdlng.getCform_dt();
if(!cform_no.equals(""))
{
	period_from=dbdlng.getPeriod_from();
	period_to=dbdlng.getPeriod_to();
}
String cform_amount=dbdlng.getCform_amount();
String cform_file=dbdlng.getCform_file();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd+"//C-FORM//";

%>
<body onload="calculateTotalInvAmt();">
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_DLNG_Invoice" enctype="multipart/form-data">

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
							C-Form Entry
						</div>	
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<!-- <div class="col-sm-2 col-xs-2 col-md-2"></div> -->
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Financial Year<span class="s-red">*</span></b></label>
					  			</div>
								<div class="col">
					  				<select class="btn btn-outline-secondary subbtngrp <%if(!financial_year.equals("0")){%>btnactive<%}%>" name="financial_year" onchange="refresh()">
											<option value="0">--Select--</option>
											<%for(int i=0;i<VFINANCIAL_YEAR.size();i++){ %>
											<option value="<%=VFINANCIAL_YEAR.elementAt(i)%>"><%=VFINANCIAL_YEAR.elementAt(i)%></option>
											<%} %>
										</select>
									<script>document.forms[0].financial_year.value="<%=financial_year%>"</script>
								</div>
					  		</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer<span class="s-red">*</span></b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="counterparty" onchange="refresh();">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
					  				</select>
					  				<script>document.forms[0].counterparty.value="<%=counterparty%>"</script>
					  			</div>
					  		</div>
					  	</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label" title="Customer Plant State"><b>Issuing State<span class="s-red">*</span></b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="state" onchange="refresh();">
										<option value="00" selected="selected" >--Select--</option>
					    				<%for(int i=0;i<VSTATE_CD.size(); i++){ %>
										<option value="<%=VSTATE_CD.elementAt(i)%>"><%=VSTATE_NM.elementAt(i)%></option>
										<%} %>
					  				</select>
					  				<script>document.forms[0].state.value="<%=state%>"</script>
					  			</div>
					  		</div>
					  	</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>C-Form Serial#<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="cformNo" value="<%=cform_serial_no %>" maxLength="14" 
				      				<%if(!cform_no.equals("")){ %>readonly<%} %>><%-- onchange="checkSerialNoExist(this,'<%=owner_cd%>');" --%>
				      				<input type="hidden" name="cform_cd" id="cform_cd" value="<%=cformCd%>" >
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Date of Issue<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="cformDt" value="<%=cform_dt%>" 
			      						maxLength="10" onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Period<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="period_frm" value="<%=period_from%>" maxLength="10" 
			      						onblur="validateDate(this);checkStartEndDate(this,document.forms[0].period_to,'F');" 
			      						onchange="refresh();validateDate(this);checkStartEndDate(this,document.forms[0].period_to,'F');" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="period_to" value="<%=period_to%>" maxLength="10" 
			      						onblur="validateDate(this);checkStartEndDate(document.forms[0].period_frm,this,'T');" 
			      						onchange="refresh();validateDate(this);checkStartEndDate(document.forms[0].period_frm,this,'T');" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Total Amount<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="cform_total_amt" value="<%=cform_amount %>" onblur="checkNumber1(this,13,2);">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Upload C-Form</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="input-group input-group-sm" >
			      					<input type="file" class="form-control form-control-sm" name="file_upload" id="file_upload" value="<%=cform_file%>">
				      				<span class="input-group-text"><i class="fa fa-upload fa-lg"></i></span>
				      			</div>
				      		</div>
						</div>
					</div>
					&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Invoice Details</label>
					</div>
					<div class="accordion">
						<div class="accordion-item accor_item">
							<h2 class="accordion-header" id="heading1">
 								<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse1" aria-expanded="false" aria-controls="collapse1">
 								Invoice&nbsp;<font color="blue">(<%=noOfInvoice%>)</font></button>	
					    	</h2>
							<div id="collapse1" class="accordion-collapse collapse" aria-labelledby="heading1">
					      		<div class="accordion-body accor-body">
					      			<div class="row">
										<div class="table-responsive">
											<table class="table table-bordered table-hover" id="example1">
												<thead>
													<tr>
														<th>Select</th>
														<th>Invoice#
															<br><div align="center"><input class="form-control form-control-sm" type="text" id="inv_no" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>	
														</th>
														<th>
															Invoice Date
															<br><div align="center"><input class="form-control form-control-sm" type="text" id="inv_dt" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>	
														</th>
														<th>Commodity
															<br><div align="center"><input class="form-control form-control-sm" type="text" id="inv_cmdt" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>	
														</th>
														<th>Taxable Amount
															<br><div align="center"><input class="form-control form-control-sm" type="text" id="inv_amt" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div>	
														</th>
														<th>Tax
															<br><div align="center"><input class="form-control form-control-sm" type="text" id="inv_tax" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div>	
														</th>
														<th>
															Total
															<br><div align="center"><input class="form-control form-control-sm" type="text" id="inv_total" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div>	
														</th>
														<th>
															Hold Amount
															<br><div align="center"><input class="form-control form-control-sm" type="text" id="inv_hold_amt" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div>	
														</th>
													</tr>
												</thead>
												<tbody>
												<%if(VINVOICE_NO.size() > 0){ %>
													<%for(int i=0; i<VINVOICE_NO.size(); i++){ %>
													<tr id="r<%=i%>">
														<td width="5%" align="center">
															<input type="checkbox" 
															<%if(VCFORM_ENTRY_EXIST.elementAt(i).equals("Y")){%>style="pointer-events:none" checked <%} %> 
															name="inv_chk" id="inv_chk_<%=i%>"
															onchange="calculateTotalInvAmt();EnableDiableInv(this,'<%=i%>');">
														</td>
														<td align="center"><%=VINVOICE_NO.elementAt(i) %>
															<input type="hidden" name="invoice_no" id="invoice_no_<%=i%>" value="<%=VINVOICE_NO.elementAt(i) %>" disabled="disabled">
															<input type="hidden" name="invoice_bu_state" id="invoice_bu_state_<%=i%>" value="<%=VBU_STATE_TIN.elementAt(i) %>" disabled="disabled">
															<input type="hidden" name="invoice_seq" id="invoice_seq_<%=i%>" value="<%=VINVOICE_SEQ.elementAt(i) %>" disabled="disabled">
														</td>
														<td align="center"><%=VINVOICE_DT.elementAt(i) %>
															<input type="hidden" name="invoice_dt" id="invoice_dt_<%=i%>" value="<%=VINVOICE_DT.elementAt(i) %>" disabled="disabled">
														</td>
														<td align="center"><%=VINVOICE_COMMODITY.elementAt(i) %>
															<input type="hidden" name="invoice_commodity" id="invoice_commodity_<%=i%>" value="<%=VINVOICE_COMMODITY.elementAt(i) %>" disabled="disabled">
														</td>
														<td align="right"><%=VGROSS_AMT.elementAt(i) %>
														</td>
														<td align="right"><%=VTAX_AMT.elementAt(i)%></td>
														<td align="right" ><%=VINVOICE_AMT.elementAt(i) %>
															<input type="hidden" name="inv_amt" id="inv_amt_<%=i%>" value="<%=VINVOICE_AMT.elementAt(i) %>">
															<input type="hidden" name="invoice_amt" id="invoice_amt_<%=i%>" value="<%=VINVOICE_AMT.elementAt(i) %>" disabled="disabled">
														</td>
														<td align="right"><%=VINVOICE_HOLD_AMT.elementAt(i) %>
														<input type="hidden" name="hold_amt" id="hold_amt_<%=i%>" value="<%=VINVOICE_HOLD_AMT.elementAt(i) %>">
														<input type="hidden" name="invoice_hold_amt" id="invoice_hold_amt_<%=i%>" value="<%=VINVOICE_HOLD_AMT.elementAt(i) %>" disabled="disabled">
														</td>
													</tr>
													<%} %>
													<tr>
														<td colspan="6" align="right"><b>Total:</b></td>
														<td id="total_inv_amt_td" align="right" style="background-color: #cff4fc; color: #055160;">
														</td>
														<td id="total_hold_amt_td" align="right" style="background-color: #cff4fc; color: #055160;">
														</td>
													</tr>
												<%}else{ %>
													<tr>
														<td colspan="8" align="center">
															<%=utilmsg.infoMessage("<b>Invoice List is not available for Selected Options!</b>")%>
														</td>
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
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="refresh();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
				</div>
			</div>
			&nbsp;
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
							C-Form Details
						</div>	
					</div>
				</div>
				<div class="cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Select</th>
										<th>C-Form#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="cform_no" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>	
										</th>
										<th>
											C-Form Date
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="cform_dt" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>	
										</th>
										<th>Amount
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="cform_amt" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>	
										</th>
										<th>Period
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="cform_period" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div>	
										</th>
										<th>
											View PDF
										</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCFORM_NO.size() > 0){ %>
									<%for(int i=0; i<VCFORM_NO.size(); i++){ %>
									<tr id="r<%=i%>">
										<td width="5%" align="center">
											<div align="center">
												<input type="radio" name="rd" onclick="doModify('<%=counterparty%>','<%=financial_year%>','<%=state %>',
												'<%=VCFORM_NO.elementAt(i)%>',
												'<%=VCFORM_DT.elementAt(i)%>','<%=VCFORM_AMOUNT.elementAt(i)%>',
												'<%=VPERIOD_FROM.elementAt(i)%>','<%=VPERIOD_TO.elementAt(i)%>',
												'<%=VCFORM_CD.elementAt(i) %>');">&nbsp;&nbsp;
												<%=i+1%>
											</div>
										</td>
										<td align="center"><%=VCFORM_NO.elementAt(i) %>
										</td>
										<td align="center"><%=VCFORM_DT.elementAt(i) %>
										</td>
										<td align="center"><%=VCFORM_AMOUNT.elementAt(i) %></td>
										<td align="center"><%=VPERIOD_FROM.elementAt(i)%> - <%=VPERIOD_TO.elementAt(i)%></td>
										<td align="center">
											<%if(!VCFORM_FILE.elementAt(i).equals("")){ %>																					
												<i class="fa fa-file-pdf-o fa-2x" style="color: red" title="<%=VCFORM_FILE.elementAt(i)%>"
												onclick="openPdfFile('<%=file_url%><%=VCFORM_FILE.elementAt(i)%>')"
												>
												</i>
											<%} %>
										</td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="6" align="center">
											<%=utilmsg.infoMessage("<b>C-Form List is not available for Selected Finanicial year!</b>")%>
										</td>
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

<input type="hidden" name="option" id="option" value="CFORM_ENTRY_MST">
<input type="hidden" name="opration" value="INSERT" id="opration">
<input type="hidden" name="no_invoices" id="no_invoices" value="<%=VINVOICE_NO.size()%>">
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

function checkSerialNoExist(obj,comp_cd)
{
	var info="";
	
	var cform_no= obj.value;
	
	$.post("../servlet/DB_Dlng_Ajax?setCallType=IsCFormNoExist&comp_cd="+comp_cd+"&cform_no="+cform_no, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.CFOMNO_DTL, function(index_1, json_1) {
				if(parseInt(json_1.CFOMNO) > 0)
				{
					info+="C-Form Serial# already Exist!\n";
				}
				if(info!="")
				{
					alert(info)
					obj.value="";
				}
			});
		});
	});
	
	return info;
}

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