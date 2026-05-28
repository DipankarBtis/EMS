<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh(operation)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var sel_inv_no = document.forms[0].sel_inv_no.value;
	var invoice_type = "";
	if(document.getElementsByName("invoice_type").length > 0)
	{
		invoice_type = document.getElementsByName("invoice_type")[0].value;
	}
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var prev_month = document.forms[0].prev_month.value;
	var prev_year = document.forms[0].prev_year.value;
	
	var crdr_gen_type = document.forms[0].crdr_gen_type.value;
	var accroid = document.forms[0].accroid.value;
	var u = document.forms[0].u.value;
	
	if(prev_month!=month || prev_year!=year)
	{
		counterparty_cd="";
		sel_inv_no="";
	}
	
	var msg="";
	var flag = true;
	if(flag)
	{
		var url = "frm_generate_derv_crdr.jsp?u="+u+"&counterparty_cd="+counterparty_cd+
				"&month="+month+"&year="+year+"&operation="+operation+"&accroid="+accroid+"&sel_inv_no="+sel_inv_no+"&crdr_gen_type="+crdr_gen_type;//+
				//"&invoice_type="+invoice_type;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}

function changeTblhed(obj)
{
	var typ="";
	if(obj.value=="CR")
	{
		typ="Credit Note";
	}
	else if(obj.value=="DR")
	{
		typ="Debit Note";
	}
	document.getElementById("th_invType").innerHTML=typ
}


async function calc(index)
{
	
	var new_buy_rate=document.getElementById("new_buy_rate"+index).value;
	var new_sell_rate=document.getElementById("new_sell_rate"+index).value;
	var new_qty=document.getElementById("new_qty"+index).value;
	
	if(new_sell_rate!="")
	{
		document.getElementById("new_sell_amt"+index).value = (parseFloat(new_sell_rate)*parseFloat(new_qty)).toFixed(2);
	}
	else
	{
		document.getElementById("new_sell_amt"+index).value = document.getElementById("inv_sell_amt"+index).value;
		document.getElementById("new_sell_rate"+index).value = document.getElementById("inv_sell_rate"+index).value;
	}
	
	if(new_sell_rate!="")
	{
		document.getElementById("new_buy_amt"+index).value = (parseFloat(new_buy_rate)*parseFloat(new_qty)).toFixed(2);
	}
	else
	{
		document.getElementById("new_buy_amt"+index).value = document.getElementById("inv_buy_amt"+index).value;
		document.getElementById("new_buy_rate"+index).value = document.getElementById("inv_buy_rate"+index).value;
	}
	document.getElementById("new_total_amt"+index).value = (parseFloat(document.getElementById("new_sell_amt"+index).value)-parseFloat(document.getElementById("new_buy_amt"+index).value)).toFixed(2);
	
	window.setTimeout(function() 
	{
		difference(index);
	}, 400);
	
	window.setTimeout(function() 
 	{
    	calculateGrandTotal();
 	}, 400);
}

function difference(index)
{
	const fields = [
        "qty", "buy_rate", "buy_amt", "sell_rate", "sell_amt","total_amt"
    ];
	
	fields.forEach(field => {
		
        var mainValue = document.getElementById("inv_"+field+index).value;
        var newValue = document.getElementById("new_"+field+index).value;
        var buySell = document.getElementById('mainBuySell'+index).value;
		//alert(mainValue+" == "+newValue)
        if (mainValue !== "" && newValue !== "") 
        {
        	const diff = parseFloat(newValue) - parseFloat(mainValue);
        	document.getElementById(field+index).value = diff.toFixed(2);
        	if(buySell=="BUY")
       		{
       			if(field=="buy_rate")
     			{
       				document.getElementById(field+index).value = diff.toFixed(4);
     			}
       			if(field=="sell_rate")
     			{
       				document.getElementById(field+index).value = diff.toFixed(3);
     			}
       		}
        	else if(buySell=="SELL")
        	{
        		if(field=="buy_rate")
     			{
       				document.getElementById(field+index).value = diff.toFixed(3);
     			}
       			if(field=="sell_rate")
     			{
       				document.getElementById(field+index).value = diff.toFixed(4);
     			}
        	}
        }
    });
}

function selectCriteria(formula)
{
	var criteria = document.getElementsByName("criteria");
	var split_formula = formula.split("#");
	
	for(var i=0; i<split_formula.length; i++)
	{
		var criteriaVal=split_formula[i];
		
		if(criteria.length > 0)
	    {
	    	for(var j=0; j<criteria.length; j++)
	    	{
	    		if(criteria[j].value == criteriaVal)
	    		{
	    			criteria[j].checked=true;
	    			enableNewValues(criteria[j]);
	    		}
	    	}
	    }
	}
}

function Do_close(msg,msg_type,accroid)
{
	window.opener.refershPar(msg,msg_type,accroid);
	window.close();
}

function doDeleteAnnexure(annex_filenm,annex_folder)
{
	var a=confirm("Annexure File Name : "+annex_filenm+"\n\nDo you want to Delete?")
	if(a)
	{
		document.forms[0].att_file_name.value=annex_filenm;
		document.forms[0].annexure_folder.value=annex_folder;
		document.forms[0].option.value="DELETE_CRDR_ANNEXURE";
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].submit();
	}
}

/* function toggleAllRows(headerCb) 
{
    const allInputs = document.querySelectorAll("input[type='checkbox']");
    allInputs.forEach(cb => {
        if (cb.id === headerCb.id) return;
        if (cb.id && cb.id.indexOf("chk") === 0) 
        {
            if (cb.checked !== headerCb.checked) 
            {
                cb.checked = headerCb.checked;

                cb.onclick();
            }
        }
    });
} */

function changeRmk(bank_formula)
{
	var crdr_type=document.forms[0].crdr_type.value;
	var remark = document.forms[0].remark.value;
	var invoice_type = document.forms[0].invoice_type.value;
	
	if(crdr_type=="DR" && invoice_type=="I")
	{
		document.forms[0].remark.value="Please pay the invoiced amount by wire transfer at Bank Name: "+bank_formula;
	}
	else
	{
		document.forms[0].remark.value="";
	}
	
}

function disableFormForViewMode() {
    const form = document.forms[0];
    if (!form) return;

    const elements = form.querySelectorAll(
        "input, select, textarea, button"
    );

    elements.forEach(el => {
        // allow scrolling & text selection
        if (el.type !== "hidden") {
            el.disabled = true;
        }
    });
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Invoice" id="derv_inv" scope="request"></jsp:useBean>
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

String operation=request.getParameter("operation")==null?"INSERT":request.getParameter("operation");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String sel_inv_no = request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String crdr_gen_type = request.getParameter("crdr_gen_type")==null?"":request.getParameter("crdr_gen_type");
String crdr_type = request.getParameter("crdr_type")==null?"":request.getParameter("crdr_type");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
if(month.length() == 1)
{
	month="0"+month; 
}

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

derv_inv.setCallFlag("DERV_CRDR_PREPARATION_LIST");
derv_inv.setComp_cd(owner_cd);
derv_inv.setMonth(month);
derv_inv.setYear(year);
derv_inv.setCounterparty_cd(counterparty_cd);
derv_inv.setSel_inv_no(sel_inv_no);
derv_inv.setOperation(operation);
derv_inv.setInvoice_seq(invoice_seq);
derv_inv.setCrdr_type(crdr_type);
derv_inv.init();

Vector VMST_COUNTERPARTY_CD = derv_inv.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = derv_inv.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = derv_inv.getVMST_COUNTERPARTY_ABBR();

Vector VINVOICE_NO_LIST = derv_inv.getVINVOICE_NO_LIST();
Vector VCRITERIA_FLAG = derv_inv.getVCRITERIA_FLAG();
Vector VCRITERIA_NAME = derv_inv.getVCRITERIA_NAME();
Vector VCRITERIA_HIDE = derv_inv.getVCRITERIA_HIDE();
Vector VALL_INSTRUMENT_NO = derv_inv.getVALL_INSTRUMENT_NO();
Vector VINSTRUMENT_TYPE = derv_inv.getVINSTRUMENT_TYPE();
Vector VCURVE_NM = derv_inv.getVCURVE_NM();
Vector VINSTRUMENT_DURATION = derv_inv.getVINSTRUMENT_DURATION();
Vector VQTY_UNIT = derv_inv.getVQTY_UNIT();
Vector VBOOKED_MMBTU = derv_inv.getVBOOKED_MMBTU();
Vector VBUY_RATE = derv_inv.getVBUY_RATE();
Vector VBUY_SELL = derv_inv.getVBUY_SELL();
Vector VBUY_AMT = derv_inv.getVBUY_AMT();
Vector VSELL_RATE = derv_inv.getVSELL_RATE();
Vector VSELL_AMT = derv_inv.getVSELL_AMT();
Vector VAGMT_NO = derv_inv.getVAGMT_NO();
Vector VAGMT_REV = derv_inv.getVAGMT_REV();
Vector VAGMT_TYPE = derv_inv.getVAGMT_TYPE();
Vector VCONT_NO = derv_inv.getVCONT_NO();
Vector VCONT_REV = derv_inv.getVCONT_REV();
Vector VCONT_TYPE = derv_inv.getVCONT_TYPE();
Vector VINSTRUMENT_NO = derv_inv.getVINSTRUMENT_NO();
Vector VTRADE_DT = derv_inv.getVTRADE_DT();
Vector VPERIOD_START_DT = derv_inv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = derv_inv.getVPERIOD_END_DT();

Vector VMAIN_BOOKED_MMBTU = derv_inv.getVMAIN_BOOKED_MMBTU();
Vector VMAIN_BUY_SELL = derv_inv.getVMAIN_BUY_SELL();
Vector VMAIN_SELL_RATE = derv_inv.getVMAIN_SELL_RATE();
Vector VMAIN_SELL_AMT = derv_inv.getVMAIN_SELL_AMT();
Vector VMAIN_BUY_RATE = derv_inv.getVMAIN_BUY_RATE();
Vector VMAIN_BUY_AMT = derv_inv.getVMAIN_BUY_AMT();
Vector VMAIN_TOTAL_AMT = derv_inv.getVMAIN_TOTAL_AMT();

Vector VNEW_QTY = derv_inv.getVNEW_QTY();
Vector VNEW_QTY_UNIT = derv_inv.getVNEW_QTY_UNIT();
Vector VNEW_SELL_RATE = derv_inv.getVNEW_SELL_RATE();
Vector VNEW_SELL_AMT = derv_inv.getVNEW_SELL_AMT();
Vector VNEW_BUY_RATE = derv_inv.getVNEW_BUY_RATE();
Vector VNEW_BUY_AMT = derv_inv.getVNEW_BUY_AMT();
Vector VNEW_TOTAL_AMT = derv_inv.getVNEW_TOTAL_AMT();
Vector IS_CRDR_SUBMITTED = derv_inv.getIS_CRDR_SUBMITTED();


String bu_plant_abbr = derv_inv.getBu_plant_abbr();
String counterparty_abbr = derv_inv.getCounterparty_abbr();
String plant_abbr = derv_inv.getPlant_abbr();
String bu_plant_seq = derv_inv.getBu_plant_seq();
String plant_seq = derv_inv.getPlant_seq();
String period_start_dt = derv_inv.getPeriod_start_dt();
String period_end_dt = derv_inv.getPeriod_end_dt();
String bu_state_tin = derv_inv.getBu_state_tin();
String fy_year = derv_inv.getFy_year();
String invoice_type = derv_inv.getInvoice_type();
String crdr_dt = derv_inv.getCrdr_dt();
String crdr_due_dt = derv_inv.getCrdr_due_dt();
String crdr_remark = derv_inv.getCrdr_remark();
String bank_formula = derv_inv.getBank_formula();
String inv_ref = derv_inv.getInv_ref();

String criteri_formula="";
if(!operation.equals("INSERT"))
{
	crdr_type = derv_inv.getCrdr_type();
	sel_inv_no = derv_inv.getSel_inv_no();
	criteri_formula = derv_inv.getCriteri_formula();
}
if(invoice_type.equals("I") && crdr_type.equals("DR") && (crdr_remark.equals("") || crdr_remark.equals("Please pay the invoiced amount by wire transfer at Bank Name: ")))
{
	crdr_remark="Please pay the invoiced amount by wire transfer at Bank Name: "+bank_formula;
}

String annexure_path="../"+CommonVariable.work_dir+owner_cd+""+CommonVariable.crdr_annexure_path+"";
%>
<body onload="<%if(!msg.equals("")){ %>Do_close('<%=msg%>','<%=msg_type%>','<%=accroid%>');<%} %> 
<%if(!criteri_formula.equals("")) {%>selectCriteria('<%=criteri_formula%>');<%}%> 
<%-- <%if(criteri_formula.contains("QTY")) {%>calc();<%}%>
<%if(operation.equals("MODIFY")) {%>getBankDetail(document.forms[0].invoice_dt);<%} %> --%>
initCheckedRows(); <%if(operation.equals("VIEW")) {%>disableFormForViewMode();<%} %>"
>
<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_Derivatives_Invoice" enctype="multipart/form-data">
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
					    	Derivatives Credit/Debit Detail
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row" <%if(operation.equals("MODIFY")){%>style="pointer-events: none;"<%}%>>
						<div class="col-sm-6 col-xs-2 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Month/Year</b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="month" onchange="refresh('<%=operation%>');">
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
					  				<select class="form-select form-select-sm" name="year" onchange="refresh('<%=operation%>');">
					  					<%for(int i=(currentYear+1); i > filter_start_year;i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
					  		</div>
						</div>
						<div class="col-sm-6 col-xs-2 col-md-4">
							<div class="form-group row">
								<div class="col-auto">  	  	
					    			<label class="form-label"><b>Select Customer</b></label>
					  			</div>
								<div class="col">  
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=operation%>');">
										<option value="">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-xs-2 col-md-4">	
							<div class="form-group row">
								<div class="col-auto">
						    		<label class="form-label"><b>Invoice No</b></label>
						  		</div>				  	
								<div class="col"> 
									<select class="form-select form-select-sm" name="sel_inv_no" onchange="refresh('<%=operation%>');">
										<option value="">--Select--</option>
										<%for (int i=0;i<VINVOICE_NO_LIST.size();i++){ %>
										<option value="<%=VINVOICE_NO_LIST.elementAt(i)%>"><%=VINVOICE_NO_LIST.elementAt(i)%></option>										
										<%} %>
									</select>
									<script>document.forms[0].sel_inv_no.value="<%=sel_inv_no%>"</script>								
								</div>
							</div>	
						</div>	
					</div>
				</div>
				<%if(!sel_inv_no.equals("")) {%>
				<div class="card-body cdbody">
					<div class="row m-b-5"> <%-- <%if(operation.equals("MODIFY")){%>style="pointer-events: none;"<%}%>> --%>
						<div class="col-sm-6 col-xs-2 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>CR/DR Type</b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="crdr_type" onchange="changeRmk('<%=bank_formula%>')"> <%-- onchange="refresh('<%=operation%>');"> --%>
					  					<option value="">--Select--</option>
										<option value="CR">Credit Note</option>
										<option value="DR">Debit Note</option>
									</select>
									<script>document.forms[0].crdr_type.value="<%=crdr_type%>"</script>
								</div>
					  		</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>CR/DR Criteria</b></label>
					  			</div>
					  			<%for(int i=0;i<VCRITERIA_FLAG.size();i++){ %>
					  			<div class="col-auto" <%if(VCRITERIA_HIDE.elementAt(i).equals("Y")) {%>style="display:none;"<%} %>>
 				  					<label class="form-label">
 				  						<input type="checkbox" class="form-check-input" name="criteria" id="criteria_<%=VCRITERIA_FLAG.elementAt(i)%>" 
 				  							value="<%=VCRITERIA_FLAG.elementAt(i)%>" 
 				  							onclick="criteriFormula(); <%if(VCRITERIA_FLAG.elementAt(i).equals("FLOATPRICE")){%>toggleAllRows(this);<%}%>setTimeout(()=>enableNewValues(this),150);">
 				  						<span style="background:#e6e6ff;">&nbsp;<%=VCRITERIA_NAME.elementAt(i) %></span>
 				  					</label>
				  				</div>
				  				<%} %>
					  		</div>
						</div>
					</div>
				</div>
				<%-- <%if(!invoice_type.equals("")) {%> --%>
					<div class="card-body cdbody">
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="table-responsive">
									<table class="table table-bordered table-hover">
										<thead>
										<tr>
											<th colspan="1"><%=owner_abbr%> Detail</th>
											<th colspan="2">Customer Detail</th>
										</tr>
										<tr>
											<th>Business Unit<span class="s-red">*</span></th>
											<th>Customer</th>
											<th>Plant</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td align="center">
												<%=bu_plant_abbr %>
												<input type="hidden" name="bu_plant_seq" value="<%=bu_plant_seq%>">
												<input type="hidden" name="bu_state_tin" value="<%=bu_state_tin%>">
											</td>
											<td align="center">
												<%=counterparty_abbr %>
											</td>
											<td align="center">
												<%=plant_abbr%>
												<input type="hidden" name="plant_seq" value="<%=plant_seq%>">
											</td>
										</tr>
									</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Invoice Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="invoice_dt" value="<%=crdr_dt %>" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
							<%if(invoice_type.equals("R")){ %>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Vendor Invoice No<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="inv_ref" value="<%=inv_ref %>" maxlength="25">
					    			</div>
					  			</div>
							</div>
							<%} %>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Due Date</b></label>
					  			</div>
							</div> 
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="invoice_due_dt" value="<%=crdr_due_dt %>" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
					</div>
					<div class="card-body cdbody">
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="table-responsive">
									<table class="table table-bordered table-hover">
										<thead>
											<tr>
												<th><input type="checkbox" class="form-check-input" name="chk_all" id="chk_all" onclick="toggleAllRows(this)"></th>
												<th>Instrument No</th>
												<th>Item</th>
												<th>Index</th>
												<th>Trade Term</th>
												<th>Quantity Unit</th>
												<th>Quantity</th>
												<th>Rate Unit</th>
												<th>Buy Rate</th>
												<th>Buy Amount</th>
												<th>Sell Rate</th>
												<th>Sell Amount</th>
												<th>Total Amount</th>
											</tr>
										</thead>
										<tbody>
											<%for(int i=0; i<VALL_INSTRUMENT_NO.size(); i++){ %>
												<tr>
													<td align="center"><input type="checkbox"  class="form-check-input" name="chk" id="chk<%=i%>" onclick="toggleExpandRow(this,<%=i%>)" <%if(IS_CRDR_SUBMITTED.elementAt(i).equals("Y")) {%>checked<%} %>></td>
													<td><%=VALL_INSTRUMENT_NO.elementAt(i) %>
														<input type="hidden" class="form-check-input" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>" disabled>
														<input type="hidden" class="form-check-input" name="agmt_rev" id="agmt_rev<%=i%>" value="<%=VAGMT_REV.elementAt(i)%>" disabled>
														<input type="hidden" class="form-check-input" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>" disabled>
														<input type="hidden" class="form-check-input" name="cont_rev" id="cont_rev<%=i%>" value="<%=VCONT_REV.elementAt(i)%>" disabled>
														<input type="hidden" class="form-check-input" name="cont_type" id="cont_type<%=i%>" value="<%=VCONT_TYPE.elementAt(i)%>" disabled>
														<input type="hidden" class="form-check-input" name="instrument_no" id="instrument_no<%=i%>" value="<%=VINSTRUMENT_NO.elementAt(i)%>" disabled>
														<input type="hidden" class="form-check-input" name="period_st_dt" id="period_st_dt<%=i%>" value="<%=VPERIOD_START_DT.elementAt(i)%>" disabled>
														<input type="hidden" class="form-check-input" name="period_end_dt" id="period_end_dt<%=i%>" value="<%=VPERIOD_END_DT.elementAt(i)%>" disabled >
														<input type="hidden" class="form-check-input" name="instruDealNo" id="instruDealNo<%=i%>" value="<%=VALL_INSTRUMENT_NO.elementAt(i)%>">
														<input type="hidden" class="form-check-input" name="buySell" id="buySell<%=i %>" value="<%=VBUY_SELL.elementAt(i)%>">
														<input type="hidden" class="form-check-input" name="mainBuySell" id="mainBuySell<%=i %>" value="<%=VMAIN_BUY_SELL.elementAt(i)%>">
													</td>
													<td align="center"><%=VINSTRUMENT_TYPE.elementAt(i) %></td>
													<td align="center"><%=VCURVE_NM.elementAt(i) %></td>
													<td align="center">
														<%=VINSTRUMENT_DURATION.elementAt(i) %>
														<input type="hidden" class="form-check-input" name="instruDuration" id="instruDuration<%=i%>" value="<%=VINSTRUMENT_DURATION.elementAt(i)%>" disabled>
													</td>
													<td align="center"><%=VQTY_UNIT.elementAt(i) %></td>
													<td align="center">
														<div style="width:100px;">
															<input type="text" class="form-control form-control-sm" name="qty" id="qty<%=i %>" value="<%=VBOOKED_MMBTU.elementAt(i)%>" style="text-align: right;" onchange="" readonly disabled>
														</div>
													</td>
													<td align="center">USD</td>
													<td align="center">
														<div style="width:100px;">
															<input type="text" class="form-control form-control-sm" name="buy_rate" id="buy_rate<%=i %>" value="<%=VBUY_RATE.elementAt(i)%>" style="text-align: right;" onchange="" readonly disabled>
														</div>
													</td>
													<td align="center">
														<div style="width:100px;">
															<input type="text" class="form-control form-control-sm" name="buy_amt" id="buy_amt<%=i %>" value="<%=VBUY_AMT.elementAt(i) %>" style="text-align: right;" readonly disabled>
														</div>
													</td>
													<td align="center">
														<div style="width:100px;">
															<input type="text" class="form-control form-control-sm" name="sell_rate" id="sell_rate<%=i %>" value="<%=VSELL_RATE.elementAt(i)%>" style="text-align: right;" onchange="" readonly disabled>
														</div>
													</td>
													<td align="center">
														<div style="width:100px;">
															<input type="text" class="form-control form-control-sm" name="sell_amt" id="sell_amt<%=i %>" value="<%=VSELL_AMT.elementAt(i) %>" style="text-align: right;" readonly disabled>
														</div>
													</td>
													<td align="center">
														<div style="width:100px;">
															<input type="text" class="form-control form-control-sm" name="total_amt" id="total_amt<%=i %>" value="" style="text-align: right;" readonly disabled>
														</div>
													</td>
												</tr>
											<%} %>
											<tr>
												<td colspan="12" align="right"><b>Total:</b></td>
												<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" name="grand_total_amt" id="grand_total_amt" value="" style="text-align: right;" readonly>
													</div>
												</td>
											</tr>
										</tbody>
									</table>
								</div>&nbsp;
								<div class="row m-b-5">
									<div class="col-sm-2 col-xs-2 col-md-2">
										<label class="form-label"><b>Remark</b></label>
									</div>
									<div class="col-sm-10 col-xs-10 col-md-10">
										<div class="form-group row">
							    			<div class="col-sm-12 col-xs-12 col-md-12">
							      				<textarea class="form-control form-control-sm" rows="3" cols="75" name="remark"><%=crdr_remark%></textarea>
							    			</div>
							  			</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<%if(!operation.equals("VIEW")){ %>
					<div class="card-footer cdfooter text-center">
						<div class="d-flex justify-content-between">
							<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
							<%if(write_access.equals("Y")){ %>
							<div class="row justify-content-end">
								<!-- <div class="col-auto">
									<i class="fa fa-eye fa-2x" onclick="viewBeforeSubmit();"></i>
								</div> -->
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
								</div>
							</div>
							<%}else{ %>
							<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
							<%} %>
						</div>
					</div>
					<%} %>
					<%} %>
				<%-- <%} %> --%>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="GEN_DERV_CR_DR">
<input type="hidden" name="opration" value="<%=operation%>">
<input type="hidden" name="operation" value="<%=operation%>">

<input type="hidden" name="prev_month" value="<%=month%>">
<input type="hidden" name="prev_year" value="<%=year%>">
<input type="hidden" name="accroid" value="<%=accroid%>">
<input type="hidden" name="crdr_gen_type" value="<%=crdr_gen_type%>">
<input type="hidden" name="financial_year" value="<%=fy_year%>">
<input type="hidden" name="invoice_type" value="<%=invoice_type%>">
<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">

<input type="hidden" name="criteri_formula" value="<%=criteri_formula%>">
<input type="hidden" name="temp_criteri_formula" value="<%=criteri_formula%>">

<input type="hidden" name="annexure_folder" value="">
<input type="hidden" name="att_file_name" value="">

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

<script>
var invoiceVec = [];
</script>

<script>
var VMAIN_BOOKED_MMBTU_JS = [];
var VMAIN_BUY_SELL_JS    = [];
var VMAIN_SELL_RATE_JS   = [];
var VMAIN_SELL_AMT_JS    = [];
var VMAIN_BUY_RATE_JS    = [];
var VMAIN_BUY_AMT_JS     = [];
var VMAIN_TOT_AMT_JS     = [];
var VNEW_BOOKED_MMBTU_JS = [];
var VNEW_SELL_RATE_JS   = [];
var VNEW_SELL_AMT_JS    = [];
var VNEW_BUY_RATE_JS    = [];
var VNEW_BUY_AMT_JS     = [];
var VNEW_TOT_AMT_JS     = [];
</script>

<%
for(int i=0; i<VMAIN_BOOKED_MMBTU.size(); i++){
%>
<script>
VMAIN_BOOKED_MMBTU_JS[<%=i%>] = "<%=VMAIN_BOOKED_MMBTU.get(i)%>";
VMAIN_BUY_SELL_JS[<%=i%>]    = "<%=VMAIN_BUY_SELL.get(i)%>";
VMAIN_SELL_RATE_JS[<%=i%>]   = "<%=VMAIN_SELL_RATE.get(i)%>";
VMAIN_SELL_AMT_JS[<%=i%>]    = "<%=VMAIN_SELL_AMT.get(i)%>";
VMAIN_BUY_RATE_JS[<%=i%>]    = "<%=VMAIN_BUY_RATE.get(i)%>";
VMAIN_BUY_AMT_JS[<%=i%>]     = "<%=VMAIN_BUY_AMT.get(i)%>";
VMAIN_TOT_AMT_JS[<%=i%>]     = "<%=VMAIN_TOTAL_AMT.get(i)%>";

VNEW_BOOKED_MMBTU_JS[<%=i%>] = "<%=VNEW_QTY.get(i)%>";
VNEW_SELL_RATE_JS[<%=i%>]   = "<%=VNEW_SELL_RATE.get(i)%>";
VNEW_SELL_AMT_JS[<%=i%>]    = "<%=VNEW_SELL_AMT.get(i)%>";
VNEW_BUY_RATE_JS[<%=i%>]    = "<%=VNEW_BUY_RATE.get(i)%>";
VNEW_BUY_AMT_JS[<%=i%>]     = "<%=VNEW_BUY_AMT.get(i)%>";
VNEW_TOT_AMT_JS[<%=i%>]     = "<%=VNEW_TOTAL_AMT.get(i)%>";
</script>
<%
}
%>

<script>
var IS_CRDR_SUBMITTED_JS = [];
</script>

<%
for(int i=0; i<IS_CRDR_SUBMITTED.size(); i++){
%>
<script>
IS_CRDR_SUBMITTED_JS[<%=i%>] = "<%=IS_CRDR_SUBMITTED.get(i)%>";
</script>
<%
}
%>


<script>
function toggleAllRows(headerCb) 
{
    var operation = document.forms[0].operation.value;
    var chk = document.getElementsByName("chk");

    for (var i = 0; i < chk.length; i++) 
    {
        // MODIFY mode protection
        if (operation === "MODIFY" && !headerCb.checked) 
        {
            // If already submitted → DO NOT uncheck
            if (IS_CRDR_SUBMITTED_JS[i] === "Y") 
            {
                chk[i].checked = true;
                continue;
            }
        }

        // Normal behavior
        if (chk[i].checked !== headerCb.checked) 
        {
            chk[i].checked = headerCb.checked;
            chk[i].onclick();
        }
    }
}


function toggleExpandRow(cb, idx) 
{
	if (cb.checked) 
	{
		var cont_no = document.getElementById("cont_no"+idx).disabled=false;
		var agmt_no = document.getElementById("agmt_no"+idx).disabled=false;
		var cont_rev = document.getElementById("cont_rev"+idx).disabled=false;
		var agmt_rev = document.getElementById("agmt_rev"+idx).disabled=false;
		var cont_type = document.getElementById("cont_type"+idx).disabled=false;
		var instrument_no = document.getElementById("instrument_no"+idx).disabled=false;
		var period_st_dt = document.getElementById("period_st_dt"+idx).disabled=false;
		var period_end_dt = document.getElementById("period_end_dt"+idx).disabled=false;
		var total_amt = document.getElementById("total_amt"+idx).disabled=false;
		var sell_amt = document.getElementById("sell_amt"+idx).disabled=false;
		var sell_rate = document.getElementById("sell_rate"+idx).disabled=false;
		var buy_amt = document.getElementById("buy_amt"+idx).disabled=false;
		var buy_rate = document.getElementById("buy_rate"+idx).disabled=false;
		var qty = document.getElementById("qty"+idx).disabled=false;
		var instruDuration = document.getElementById("instruDuration"+idx).disabled=false;
	}
	else
	{
		var cont_no = document.getElementById("cont_no"+idx).disabled=true;
		var agmt_no = document.getElementById("agmt_no"+idx).disabled=true;
		var cont_rev = document.getElementById("cont_rev"+idx).disabled=true;
		var agmt_rev = document.getElementById("agmt_rev"+idx).disabled=true;
		var cont_type = document.getElementById("cont_type"+idx).disabled=true;
		var instrument_no = document.getElementById("instrument_no"+idx).disabled=true;
		var period_st_dt = document.getElementById("period_st_dt"+idx).disabled=true;
		var period_end_dt = document.getElementById("period_end_dt"+idx).disabled=true;
		var total_amt = document.getElementById("total_amt"+idx).disabled=true;
		var sell_amt = document.getElementById("sell_amt"+idx).disabled=true;
		var sell_rate = document.getElementById("sell_rate"+idx).disabled=true;
		var buy_amt = document.getElementById("buy_amt"+idx).disabled=true;
		var buy_rate = document.getElementById("buy_rate"+idx).disabled=true;
		var qty = document.getElementById("qty"+idx).disabled=true;
		var instruDuration = document.getElementById("instruDuration"+idx).disabled=true;
	}
	
    const mainRow = cb.closest("tr");
    const tbody = mainRow.parentNode;

    const key = mainRow.cells[1].innerText.trim();
    const rowClass = "exp_" + key;

    // Collapse
    if (!cb.checked) {
        document.querySelectorAll("." + rowClass)
            .forEach(r => r.remove());
        return;
    }

    // Prevent duplicate
    if (document.querySelector("." + rowClass)) return;

    const td = (html = "") => {
        const c = document.createElement("td");
        c.innerHTML = html;
        return c;
    };

 // ---------- Invoice Value (A) ----------
    const rowA = document.createElement("tr");
    rowA.className = rowClass + " sub-row";
    rowA.id = "invRow" + idx;

    rowA.append(
        td(),
        td(),
        td(),
        td(),
        td(),
        td('<div align="right"><b>Invoiced Value (A)</b></div>'),
        td('<div align="center"><input id="inv_qty'+idx+'" name="inv_qty" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
        td('<div align="center">USD</div>'),
        td('<div align="center"><input id="inv_buy_rate'+idx+'" name="inv_buy_rate" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
        td('<div align="center"><input id="inv_buy_amt'+idx+'" name="inv_buy_amt" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
        td('<div align="center"><input id="inv_sell_rate'+idx+'" name="inv_sell_rate" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
        td('<div align="center"><input id="inv_sell_amt'+idx+'" name="inv_sell_amt" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
        td('<div align="center"><input id="inv_total_amt'+idx+'" name="inv_total_amt" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>')
    );

    // ---------- New Value (B) ----------
    const rowB = document.createElement("tr");
    rowB.className = rowClass + " sub-row";
    rowB.id = "newRow" + idx;

    rowB.append(
        td(),
        td(),
        td(),
        td(),
        td(),
        td('<div align="right"><b>New Value (B)</b></div>'),
        td('<div align="center"><input id="new_qty'+idx+'" name="new_qty" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
        td('<div align="center">USD</div>'),
        td('<div align="center"><input id="new_buy_rate'+idx+'" name="new_buy_rate" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly onchange="calc('+idx+');"></div>'),
        td('<div align="center"><input id="new_buy_amt'+idx+'" name="new_buy_amt" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
        td('<div align="center"><input id="new_sell_rate'+idx+'" name="new_sell_rate" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly onchange="calc('+idx+')"></div>'),
        td('<div align="center"><input id="new_sell_amt'+idx+'" name="new_sell_amt" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
        td('<div align="center"><input id="new_total_amt'+idx+'" name="new_total_amt" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>')
    );

    tbody.insertBefore(rowA, mainRow.nextSibling);
    tbody.insertBefore(rowB, rowA.nextSibling);

    document.getElementById("inv_qty"+idx).value       = VMAIN_BOOKED_MMBTU_JS[idx];
    document.getElementById("inv_buy_rate"+idx).value = VMAIN_BUY_RATE_JS[idx];
    document.getElementById("inv_buy_amt"+idx).value  = VMAIN_BUY_AMT_JS[idx];
    document.getElementById("inv_sell_rate"+idx).value= VMAIN_SELL_RATE_JS[idx];
    document.getElementById("inv_sell_amt"+idx).value = VMAIN_SELL_AMT_JS[idx];
    document.getElementById("inv_total_amt"+idx).value  = VMAIN_TOT_AMT_JS[idx];

    document.getElementById("new_qty"+idx).value       = VNEW_BOOKED_MMBTU_JS[idx];
    document.getElementById("new_buy_rate"+idx).value = VNEW_BUY_RATE_JS[idx];
    document.getElementById("new_buy_amt"+idx).value  = VNEW_BUY_AMT_JS[idx];
    document.getElementById("new_sell_rate"+idx).value= VNEW_SELL_RATE_JS[idx];
    document.getElementById("new_sell_amt"+idx).value = VNEW_SELL_AMT_JS[idx];
    document.getElementById("new_total_amt"+idx).value  = VNEW_TOT_AMT_JS[idx];
}

async function getBankDetail(obj)
{
	var invoice_type = document.forms[0].invoice_type.value;
	var contract_type = document.forms[0].contract_type.value;
	if(invoice_type=="DR" && contract_type !="X")
	{
		var inv_dt=	obj.value;
		if(inv_dt.trim()!="")
		{
			$.post("../servlet/DB_Invoice_Ajax?setCallType=BANK_DTL_FOR_CR_DR&inv_dt="+inv_dt, function(responseJson) {
				console.log(responseJson);
				$.each(responseJson, function(index, json) 
				{
					$.each(json.BANK_DTL, function(index_1, json_1) 
					{
						document.forms[0].remark1.value=json_1.REMARK;
					});
				});
			});
		}
		else
		{
			document.forms[0].remark1.value="";
		}
	}
	else
	{
		document.forms[0].remark1.value="";
	}
}

enableButton=true;
function doSubmit()
{
	var rowCount = <%=VALL_INSTRUMENT_NO.size()%>;
    var counterparty_cd = document.forms[0].counterparty_cd.value;
    var agmt_no = document.forms[0].agmt_no;
    var agmt_rev = document.forms[0].agmt_rev;
    var cont_no = document.forms[0].cont_no;
    var cont_rev = document.forms[0].cont_rev;
    var instrument_no = document.forms[0].instrument_no;
    var contract_type = document.forms[0].cont_type;
    
    var invoice_dt = document.forms[0].invoice_dt.value;
	var invoice_due_dt = document.forms[0].invoice_due_dt.value;
	var inv_ref = "";
	
	var criteria = document.getElementsByName("criteria");
	var chk = document.getElementsByName("chk");
	var invoice_type = document.forms[0].invoice_type.value;
	var operation = document.forms[0].operation.value;
	
	if(invoice_type=="R")
	{
		inv_ref=document.forms[0].inv_ref.value;
	}
	var remark = document.forms[0].remark.value;
	var temp_criteri_formula = document.forms[0].temp_criteri_formula.value;
	
	var crdr_gen_type = document.forms[0].crdr_gen_type.value;
	var crdr_type = document.forms[0].crdr_type.value;
	var grand_total_amt = document.forms[0].grand_total_amt.value;
	
    var msg="";
	var flag=true;
	var isChangeInQty=false;
	var isChangeInFixedPrice=false;
	var subQtyInstruList="";
	var modQtyInstruList="";
	var subPriceInstruList="";
	var modPriceInstruList="";
	
	
	if(crdr_type.trim() === "") 
    {
        msg += "Select CR/DR Type!\n";
        flag = false;
    }
    if(counterparty_cd.trim() === "") 
    {
        msg += "Missing Counterparty Code!\n";
        flag = false;
    }
    
	for (var i = 0; i < rowCount; i++) 
    {
		if (chk[i] && chk[i].checked)
		{
			if(rowCount==1)
			{
				if(agmt_no.value.trim() === "") 
			    {
			        msg += "Missing Agreement Number!\n";
			        flag = false;
			    }
			    if(agmt_rev.value.trim() === "") 
			    {
			        msg += "Missing Agreement Revision!\n";
			        flag = false;
			    }
			    if(cont_no.value.trim() === "") 
			    {
			        msg += "Missing Contract Number!\n";
			        flag = false;
			    }
			    if(cont_rev.value.trim() === "") 
			    {
			        msg += "Missing Contract Revision!\n";
			        flag = false;
			    }
			    if(contract_type.value.trim() === "") 
			    {
			        msg += "Missing Contract Type!\n";
			        flag = false;
			    }
			}
			else
			{
				if(!agmt_no[i] || agmt_no[i].value.trim() === "") 
			    {
			        msg += "Missing Agreement Number!\n";
			        flag = false;
			    }
			    if(!agmt_no[i] || agmt_rev[i].value.trim() === "") 
			    {
			        msg += "Missing Agreement Revision!\n";
			        flag = false;
			    }
			    if(!cont_no[i] || cont_no[i].value.trim() === "") 
			    {
			        msg += "Missing Contract Number!\n";
			        flag = false;
			    }
			    if(!cont_rev[i] || cont_rev[i].value.trim() === "") 
			    {
			        msg += "Missing Contract Revision!\n";
			        flag = false;
			    }
			    
			    if(!contract_type[i] || contract_type[i].value.trim() === "") 
			    {
			        msg += "Missing Contract Type!\n";
			        flag = false;
			    }
			}
	   
		    var instruDealNo = document.getElementById('instruDealNo'+i).value;
		    var instruDuration = document.getElementById('instruDuration'+i).value;
		    var buySell = document.getElementById('buySell'+i).value;
		    var mainBuySell = document.getElementById('mainBuySell'+i).value;
		    var criChkCnt=parseInt("0");
		    if(criteria.length > 0)
		    {
		    	for(var j=0; j<criteria.length; j++)
		    	{
		    		var chngin=criteria[j].value;
		    		
		    		if(criteria[j].checked)
		    		{
		    			criChkCnt++;
		    		}
		    		
		    		if(chngin=="QTY" && criteria[j].checked)
		    		{
		    			isChangeInQty=true;
		    			var inv_alloc_qty = document.getElementById("inv_qty"+i).value;
		    			var new_alloc_qty = document.getElementById("new_qty"+i).value;
		    			subQtyInstruList+= instruDealNo+" ("+instruDuration+") \n";
		    			if(operation=="MODIFY" && (temp_criteri_formula.includes("QTY") || chngin=="QTY"))
		        		{
		    				isChangeInQty=false;
			    			if(new_alloc_qty.trim()=="" || new_alloc_qty.trim()==inv_alloc_qty.trim())
			    			{
			    				msg += "Enter New Quantity for the Row "+parseFloat(i+1)+"!\n";
			    		        flag = true;
			    		        isChangeInQty=true;
			    		        modQtyInstruList+= instruDealNo+" ("+instruDuration+") \n";
			    			}
			    			else if(operation=="MODIFY")
			    			{
			    				if(new_alloc_qty.trim()==inv_alloc_qty.trim() && temp_criteri_formula.includes("QTY"))
			    				{
			    					msg += "New Quantity and Invoice Quantity shouldn't be same for the Row "+parseFloat(i+1)+"!\n";
			        		        flag = true;
			    				}
			    			}
		        		}
		    		}
		    		else if(chngin=="FIXEDPRICE" && criteria[j].checked)
		    		{
		    			isChangeInFixedPrice=true;
		    			var inv_price = "";
		    			var new_price = "";
		    			subPriceInstruList+= instruDealNo+" ("+instruDuration+") \n";
		    			if(buySell=="SELL")
	    				{
	    					inv_price = document.getElementById('inv_sell_rate'+i).value;
	    					new_price = document.getElementById('new_sell_rate'+i).value;
	    				}
		    			else if(buySell=="BUY")
	    				{
		    				inv_price = document.getElementById('inv_buy_rate'+i).value;
	    					new_price = document.getElementById('new_buy_rate'+i).value;
	    				}
		    			if(operation=="MODIFY" && (temp_criteri_formula.includes("FIXEDPRICE") || chngin=="FIXEDPRICE"))
		        		{
		    				isChangeInFixedPrice=false;
		    				if(new_price.trim()=="" || new_price.trim()==inv_price.trim())
			    			{
		    					msg += "Enter New Fixed Price for the Row "+parseFloat(i+1)+"!\n";
			    		        flag = true;
			    		        isChangeInFixedPrice=true;
			    		        modPriceInstruList+= instruDealNo+" ("+instruDuration+") \n";
			    			}
			    			else if(operation=="MODIFY")
			    			{
			    				if(new_price.trim()==inv_price.trim() && temp_criteri_formula.includes("FIXEDPRICE"))
			    				{
			    					msg += "New Fixed Price and Invoice Fixed Price shouldn't be same for the Row "+parseFloat(i+1)+"!\n";
			        		        flag = true;
			    				}
			    			}
		        		}
		    		}
		    		else if(chngin=="FLOATPRICE" && criteria[j].checked)
		    		{
		    			var inv_price = "";
		    			var new_price = "";
		    			if(mainBuySell=="BUY")
	    				{
	    					inv_price = document.getElementById('inv_sell_rate'+i).value;
	    					new_price = document.getElementById('new_sell_rate'+i).value;
	    				}
		    			else if(mainBuySell=="SELL")
	    				{
		    				inv_price = document.getElementById('inv_buy_rate'+i).value;
	    					new_price = document.getElementById('new_buy_rate'+i).value;
	    				}
		    			
		    			if(new_price.trim()=="")
		    			{
	    					msg += "Enter New Float Price for the Row "+parseFloat(i+1)+"!\n";
		    		        flag = false;
		    			}
	    				if(new_price.trim()==inv_price.trim() && (temp_criteri_formula.includes("FLOATPRICE") || chngin=="FLOATPRICE"))
	    				{
	    					msg += "New Float Price and Invoice Float Price shouldn't be same for the Row "+parseFloat(i+1)+"!\n";
	        		        flag = false;
	    				}
		    		}
		    	}
		    }
	    }
	}
    
    
    if(criChkCnt==0)
    {
    	msg += "Please select atleast ONE(1) CR/DR Criteria!\n";
        flag = false;
    }
	
	if(trim(invoice_dt)=="")
	{
		msg+="Enter Invoice Date!\n";
		flag=false;
	}
	
	if(invoice_type=="R" && trim(inv_ref)=="")
	{
		msg+="Enter Vendor Invoice No!\n";
		flag=false;
	}
		
	if(operation=="MODIFY")
	{
		if(trim(invoice_type)!="CR")
		{
			if(trim(invoice_due_dt)=="")
			{
				msg+="Enter Invoice Due Date!\n";
				flag=false;
			}	
		}
		
		if(trim(invoice_dt)!="" && trim(invoice_due_dt)!="")
		{
			var count = compareDate(invoice_dt,invoice_due_dt);
			if(parseInt(count) == 1)
			{
				msg+="Invoice Due Date should be grater or equal Invoice Date!";
				flag=false;
			}
		}
	}
	
	if(invoice_type=="CR")
	{
		document.forms[0].remark.value="";
	}
	else if(invoice_type=="DR" && contract_type !="X")
	{
		if(remark.trim()=="")
		{
			msg+="Bank Detail Missing in Remark!\n";
			flag=false;
		}
	}
	
	var chkCnt=parseInt("0");
	if(chk.length > 0)
    {
    	for(var i=0; i<chk.length; i++)
    	{
    		if(chk[i].checked)
    		{
    			chkCnt++;
    		}
    	}
    }
	if(chkCnt==0)
    {
    	msg += "Please select atleast ONE(1) Instrument To generate CR/DR!\n";
        flag = false;
    }
	
	if(operation=="MODIFY")
	{
		if(trim(grand_total_amt)=="")
		{
			msg+="Grand Total Amount missing!\n";
			flag=false;
		}
	}
	
	if(parseFloat(grand_total_amt) > 0 && crdr_type=="CR")
	{
		msg+="You are supose to generate Debit Note but you have selected Credit Note..Please change the Invoice Type!\n";
		flag=false;
	}
	else if(parseFloat(grand_total_amt) < 0 && crdr_type=="DR")
	{
		msg+="You are supose to generate Credit Note but you have selected Debit Note..Please change the Invoice Type!\n";
		flag=false;
	}
			
	var qtyInstruList;
	var priceInstruList;
	if(operation=="MODIFY")
	{
		qtyInstruList = modQtyInstruList;
		priceInstruList = modPriceInstruList;
	}
	else
	{
		qtyInstruList = subQtyInstruList;
		priceInstruList = subPriceInstruList;
	}
	
	if(flag)
	{
		if(isChangeInQty)
		{
			alert("⚠️ CR/DR Criteria - Change in Quantity - is Selected!\n\n" +
		              "Important: On Submit, Quantity modification will be allowed for following Instrument/s :\n\n"+qtyInstruList+"\n" +
		              "Once Quantity updated, use Modify option to Submit CR/DR Note.\n\n" +
		              "Note: System will NOT allow Check or Approve, if NO Quantity Change detected!\n\n");	
		}
		if(isChangeInFixedPrice)
		{
			alert("⚠️ CR/DR Criteria - Change in Fixed Price - is Selected!\n\n" +
		              "Important: On Submit, Fixed Price modification will be allowed for following Instrument/s:.\n\n"+priceInstruList+"\n" +
		              "Once Fixed Price updated, use Modify option to Submit CR/DR Note.\n\n" +
		              "Note: System will NOT allow Check or Approve, if NO Fixed Price Change detected!");	
		}
		var a=confirm("Do you want to Submit Credit/Debit Note?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			editAllowedOnCpStatus = true;
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

async function initCheckedRows() 
{
    var i = 0;

    while (true) 
    {
        var cb = document.getElementById("chk" + i);
       
        if (!cb) break;

        if (cb.checked) 
        {
            toggleExpandRow(cb, i);
            calc(i)
        }
        i++;
    }
    window.setTimeout(function() 
 	{
    	calculateGrandTotal();
 	}, 450);
}

function criteriFormula()
{
	var rowCount = <%=VALL_INSTRUMENT_NO.size()%>;
	var criteria = document.getElementsByName("criteria");
	var formula="";
	if(criteria.length > 0)
    {
		
    	for(var i=0; i<criteria.length; i++)
    	{
    		var operation = document.forms[0].operation.value;
    		var chk = document.getElementsByName("chk");
    		
    		if(criteria[i].value=="QTY")
    		{
    			var cnt=0;
    			if(operation=="MODIFY" && !criteria[i].checked)
    			{
    				for (var j = 0; j < rowCount; j++) 
    			    {
    					if (chk[j].checked)
    				    {
    						var new_qty = document.getElementById("new_qty"+j).value;
        					var main_qty = document.getElementById("inv_qty"+j).value;
    						if(new_qty != main_qty && new_qty!="")
    						{
    							cnt++;
    						}
    				    }
    			    }
    			}
    			if(cnt>0)
        		{
        			alert("Quantity Change detected! CR/DR Criteria - Change in Quantity - can't be de-Selected!");
        			criteria[i].checked=true;
        		}
    		}
    		
    		if(criteria[i].value=="FIXEDPRICE")
    		{
    			var cnt=0;
    			if(operation=="MODIFY" && !criteria[i].checked)
    			{
    				for (var j = 0; j < rowCount; j++) 
    			    {
    					if (chk[j].checked)
    				    {
    						var buySell = document.getElementById('buySell'+j).value;
    						var inv_price = "";//document.forms[0].main_price.value;
    			    		var new_price = "";//document.forms[0].new_price.value;
    			    			
   			    			if(buySell=="SELL")
   		    				{
   		    					inv_price = document.getElementById('inv_sell_rate'+j).value;
   		    					new_price = document.getElementById('new_sell_rate'+j).value;
   		    				}
   			    			else if(buySell=="BUY")
   		    				{
   			    				inv_price = document.getElementById('inv_buy_rate'+j).value;
   		    					new_price = document.getElementById('new_buy_rate'+j).value;
   		    				}
    						 
    						if(new_price != inv_price && new_price!="")
    						{
    							cnt++;
    						}
    				    }
    			    }
    			}
    			if(cnt>0)
        		{
        			alert("Fixed Price Change detected! CR/DR Criteria - Change in Fixed Price - can't be de-Selected!");
        			criteria[i].checked=true;
        		}
    		}
    		
    		if(criteria[i].value=="FLOATPRICE")
    		{
    			var cnt=0;
    			if(operation=="MODIFY" && !criteria[i].checked)
    			{
    				for (var j = 0; j < rowCount; j++) 
    			    {
    					if (chk[j].checked)
    				    {
    						var buySell = document.getElementById('buySell'+j).value;
    						var inv_price = "";//document.forms[0].main_price.value;
    			    		var new_price = "";//document.forms[0].new_price.value;
    			    			
   			    			if(buySell=="BUY")
   		    				{
   		    					inv_price = document.getElementById('inv_sell_rate'+j).value;
   		    					new_price = document.getElementById('new_sell_rate'+j).value;
   		    				}
   			    			else if(buySell=="SELL")
   		    				{
   			    				inv_price = document.getElementById('inv_buy_rate'+j).value;
   		    					new_price = document.getElementById('new_buy_rate'+j).value;
   		    				}
    						 
    						if(new_price != inv_price && new_price!="")
    						{
    							cnt++;
    						}
    				    }
    			    }
    			}
    			if(cnt>0)
        		{
        			alert("Float Price Change detected! CR/DR Criteria - Change in Float Price - can't be de-Selected!");
        			criteria[i].checked=true;
        		}
    		}
    		
    		if(criteria[i].checked)
    		{
    			if(formula=="")
    			{
    				formula=criteria[i].value;
    			}
    			else
    			{
    				formula=formula+"#"+criteria[i].value;
    			}
    		}
    	}
    }
	
	document.forms[0].criteri_formula.value=formula;
}

function calculateGrandTotal()
{
	var rowCount = <%=VALL_INSTRUMENT_NO.size()%>;
    var chk = document.getElementsByName('chk');
    var grandTotal = 0.0;

    for (var i = 0; i < rowCount; i++)
    {
        if (chk[i] && chk[i].checked)
        {
            var amtFld = document.getElementById('total_amt'+i).value;
            if (amtFld && amtFld.trim() !== "")
            {
                var val = parseFloat(amtFld);
                if (!isNaN(val))
                {
                    grandTotal += val;
                }
            }
        }
    }
    
    document.forms[0].grand_total_amt.value = grandTotal.toFixed(2);
}

function enableNewValues(obj)
{
	setTimeout(function () 
	{
		var rowCount = <%=VALL_INSTRUMENT_NO.size()%>;
		var operation = document.forms[0].operation.value;
		var chk = document.getElementsByName("chk");
		
		if(obj.value=="FLOATPRICE")
		{
			for (var j = 0; j < rowCount; j++) 
		    {
				if (chk[j].checked)
			    {
					var buySell = document.getElementById('mainBuySell'+j).value;
					var inv_price = "";
		    		var new_price = "";
		    		
		    		if(obj.checked)
		    		{
		    			if(VNEW_BOOKED_MMBTU_JS[j]=="")
		    			{
		    				document.getElementById("new_qty"+j).value       = VMAIN_BOOKED_MMBTU_JS[j];
		    			    document.getElementById("new_buy_rate"+j).value = VMAIN_BUY_RATE_JS[j];
		    			    document.getElementById("new_buy_amt"+j).value  = VMAIN_BUY_AMT_JS[j];
		    			    document.getElementById("new_sell_rate"+j).value= VMAIN_SELL_RATE_JS[j];
		    			    document.getElementById("new_sell_amt"+j).value = VMAIN_SELL_AMT_JS[j];
		    			    document.getElementById("new_total_amt"+j).value  = VMAIN_TOT_AMT_JS[j];
		    			}
		    			
			    		if(buySell=="BUY")
		   				{
		   					document.getElementById('new_sell_rate'+j).readOnly=false;
		   				}
			    		else if(buySell=="SELL")
		   				{
		   					document.getElementById('new_buy_rate'+j).readOnly=false;
		   				}
			    		
			    		calc(j);
		    		}
		    		else
	    			{
		    			if(buySell=="BUY")
		   				{
		   					document.getElementById('new_sell_rate'+j).readOnly=true;
		   				}
			    		else if(buySell=="SELL")
		   				{
		   					document.getElementById('new_buy_rate'+j).readOnly=true;
		   				}
	    			}
			    }
				else
				{
					if(buySell=="BUY")
	   				{
	   					document.getElementById('new_sell_rate'+j).readOnly=true;
	   				}
		    		else if(buySell=="SELL")
	   				{
	   					document.getElementById('new_buy_rate'+j).readOnly=true;
	   				}
				}
		    }
			window.setTimeout(function() 
			{
				calculateGrandTotal();
			}, 450);
		}
	}, 200);
}

</script>

</form>
</body>
</html>