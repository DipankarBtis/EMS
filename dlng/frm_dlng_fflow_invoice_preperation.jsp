<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	//var gas_dt = document.forms[0].gas_dt.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var pdf_type = document.forms[0].pdf_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_fflow_invoice_preperation.jsp?&month="+month+"&year="+year+"&pdf_type="+pdf_type+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function nextDate(day_no)
{
	var dt = document.forms[0].gas_dt.value;
	if(dt!="")
	{
	   	var split = dt.split("/");
		var d_dt = split[0];
		var m_dt = split[1];
		var y_dt = split[2];
		
		var dt1 = new Date(y_dt+"-"+m_dt+"-"+d_dt);
		if(day_no == "-1")
		{
			dt1.setDate(dt1.getDate()-1);
		}
		else
		{
			dt1.setDate(dt1.getDate()+1);
		}
		var day = dt1.getDate();
		if(parseInt(day) < 10)
		{
			day="0"+day;
		}
		var month = dt1.getMonth()+1;
		var year = dt1.getFullYear();
		if(parseInt(month) < 10)
		{
			month="0"+month;
		}
		var to_dt= day+"/"+month+"/"+year;
		
		document.forms[0].gas_dt.value=to_dt;
		
		refresh();
	}
}

function openPdfFile(url)
{
	window.open(url);
}

var newWindow;

function doGenXML(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,type_flag,invoice_type,
		bu_state_tin,sap_approval_flag,agmt_no,cont_no, inv_pdf_flag,cargo_no)
{
	if(inv_pdf_flag=="")
	{
		inv_pdf_flag="N";
	}
	else
	{
		inv_pdf_flag="Y";
	}
	
	var u = document.forms[0].u.value;
	
	var url = "../dlng/rpt_view_sales_invoice_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
			"&contract_type="+contract_type+"&type_flag="+type_flag+"&invoice_type="+invoice_type+
			"&counterparty_cd="+couterpty_cd+"&invoice_no="+invoice_no+"&bu_state_tin="+bu_state_tin+
			"&sap_approval_flag="+sap_approval_flag+"&agmt_no="+agmt_no+"&cont_no="+cont_no+"&inv_pdf_flag="+inv_pdf_flag+
			"&cargo_no="+cargo_no+
			"&u="+u;

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

function openMailBody(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,
		period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,index,inv_flag)
{
	var pdf_type="";
	if(document.getElementById("pdf_type")!=null && document.getElementById("pdf_type")!=undefined)
	{
		pdf_type = document.getElementById("pdf_type").value;
	}
	
	var all_mail_pdf_type = document.getElementById("all_mail_pdf_type"+index).value;
	
	if(pdf_type == "All")
	{
		pdf_type=all_mail_pdf_type;
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_sign_pdf_mail.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&mail_pdf_type="+pdf_type+
			"&mail_inv_type=S&inv_flag="+inv_flag+
			"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow= window.open(url,"Mail Body","top=10,left=70,width=900,height=700,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow= window.open(url,"Mail Body","top=10,left=70,width=900,height=700,scrollbars=1");
	}
}

function openMailBodyFor402(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,
		period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,index,inv_flag)
{
	var pdf_type="402";
	
	var u = document.forms[0].u.value;
	
	var url = "frm_sign_pdf_mail.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&mail_pdf_type="+pdf_type+
			"&mail_inv_type=S&inv_flag="+inv_flag+
			"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow= window.open(url,"Mail Body","top=10,left=70,width=900,height=700,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow= window.open(url,"Mail Body","top=10,left=70,width=900,height=700,scrollbars=1");
	}
}

function openAllPdfFile(fin_yr,bu_st_cd,inv_seq)
{
	var url = "rpt_view_all_pdf.jsp?financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"PDF Sales Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"PDF Sales Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function openFreeFlowInv()
{
	//var gas_dt = document.forms[0].gas_dt.value;
	
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var activity_type="PREPARE";
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_f_flow_invoice.jsp?u="+u+
			"&month="+month+"&year="+year+"&opration=INSERT&activity_type="+activity_type;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"DLNG Free Flow Invoice","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"DLNG Free Flow Invoice","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function openFreeFlowInv_Modify(counterparty_cd,address_type,period_st_dt,period_end_dt,bu,billing_cycle,
		mapping_id,inv_seq,inv_no,inv_type,finan_yr,bu_state_tin,activity_type,cargo_no)
{
	//var gas_dt = document.forms[0].gas_dt.value;
	
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var split=period_end_dt.split("/");
	month=split[1];
	year=split[2];
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_f_flow_invoice.jsp?u="+u+"&bu_state_tin="+bu_state_tin+
			"&counterparty_cd="+counterparty_cd+"&period_start_dt="+period_st_dt+"&period_end_dt="+period_end_dt+"&mapping_id="+mapping_id+
			"&address_type="+address_type+"&bu_unit="+bu+"&inv_no="+inv_no+"&inv_seq="+inv_seq+"&financial="+finan_yr+"&invoice_type="+inv_type+
			"&billing_cycle="+billing_cycle+
			"&month="+month+"&year="+year+
			"&cargo_no="+cargo_no+
			"&opration=MODIFY&activity_type="+activity_type;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Free Flow Invoice","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Free Flow Invoice","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function openFreeFlowInv_Approval(counterparty_cd,address_type,period_st_dt,period_end_dt,bu,billing_cycle,
		mapping_id,inv_seq,inv_type,finan_yr,bu_state_tin,activityFlag,cargo_no)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_dlng_f_flow_view_chk_aprv_dtl.jsp?u="+u+"&bu_state_tin="+bu_state_tin+
			"&counterparty_cd="+counterparty_cd+"&period_start_dt="+period_st_dt+"&period_end_dt="+period_end_dt+"&mapping_id="+mapping_id+
			"&address_type="+address_type+"&bu_unit="+bu+"&invoice_seq="+inv_seq+"&financial_year="+finan_yr+"&invoice_type="+inv_type+
			"&billing_cycle="+billing_cycle+"&activityFlag="+activityFlag+"&cargo_no="+cargo_no;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Free Flow Invoice","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Free Flow Invoice","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function fflowPrintPDF(bu_unit,counterparty_cd,billing_cycle,mapping_id,address_type,invoice_type,
		invoice_no,invoice_seq,financial_year,bu_state_tin,cont_type,index,cargo_no)
{
	var u = document.forms[0].u.value;
	
	var print_access = document.forms[0].print_access.value;
	var pdf_type="";
	if(document.forms[0].pdf_type!=null && document.forms[0].pdf_type!=undefined)
	{
		pdf_type = document.forms[0].pdf_type.value;
	}
	
	var all_pdf_type = document.getElementById("oth_all_pdf_type"+index).value;
	
	if(pdf_type == "All")
	{
		pdf_type=all_pdf_type;
	}
	
	var is_print="1";
	
	if(print_access=="N")
	{
		alert("You don't have Print Rights!");
	}
	else
	{
		var url = "pdf_dlng_f_flow_invoice.jsp?counterparty_cd="+counterparty_cd+"&is_print="+is_print+"&bu_state_tin="+bu_state_tin+
			"&invoice_type="+invoice_type+"&mapping_id="+mapping_id+"&address_type="+address_type+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+
			"&inv_no="+invoice_no+"&inv_seq="+invoice_seq+"&financial="+financial_year+"&ff_print_pdf_type="+pdf_type+
			"&contract_type="+cont_type+
			"&cargo_no="+cargo_no+
			"&u="+u;
		
		if(!newWindow || newWindow.closed)
		{
			newWindow= window.open(url,"F-Flow Invoice","top=10,left=70,width=900,height=700,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow= window.open(url,"F-Flow Invoice","top=10,left=70,width=900,height=700,scrollbars=1");
		}
	}
}


function openffMailBody(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,
		period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,bu_st_cd,inv_seq,index,inv_type,cargo_no)
{
	var pdf_type="";
	if(document.forms[0].pdf_type!=null && document.forms[0].pdf_type!=undefined)
	{
		pdf_type = document.forms[0].pdf_type.value;
	}
	
	var oth_all_mail_pdf_type = document.getElementById("oth_all_mail_pdf_type"+index).value;
	
	if(pdf_type == "All")
	{
		pdf_type=oth_all_mail_pdf_type;
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_sign_pdf_mail.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&mail_pdf_type="+pdf_type+
			"&mail_inv_type=F&invoice_type="+inv_type+
			"&cargo_no="+cargo_no+
			"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow= window.open(url,"Mail Body","top=10,left=70,width=900,height=700,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow= window.open(url,"Mail Body","top=10,left=70,width=900,height=700,scrollbars=1");
	}
}

function openAllFfPdfFile(fin_yr,inv_type,bu_st_cd,inv_seq,flag)
{
	var url = "rpt_view_all_pdf.jsp?financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&invoice_type="+inv_type+"&flag="+flag;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"PDF DLNG Sales Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"PDF DLNG Sales Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function refershPar(sub_msg,msg_type,accroid)
{
	//var gas_dt = document.forms[0].gas_dt.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var pdf_type="O";
	if(document.getElementById("pdf_type")!=null && document.getElementById("pdf_type")!=undefined)
	{
		pdf_type = document.getElementById("pdf_type").value;
	}
	
	var msg="";
	var flag = true;
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "frm_dlng_fflow_invoice_preperation.jsp?&u="+u+"&pdf_type="+pdf_type+
				"&month="+month+"&year="+year+"&accroid="+accroid+"&msg="+sub_msg+"&msg_type="+msg_type;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg)
	}
}

</script>

<style>
.icon-wrapper {
  position: relative;
  display: inline-block;
  width: 2.5em;
  height: 2.5em;
  text-align: center;
}

.icon-wrapper i {
  color: black; /* Excel green */
}

.icon-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -45%);
  font-size: 0.7em;
  color: white;
  font-weight: bold;
  pointer-events: none;
}

</style>

</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="dlng_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();
int filter_start_year = CommonVariable.filter_start_year;

//String gas_dt = request.getParameter("gas_dt")==null?sysdate:request.getParameter("gas_dt");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String pdf_type=request.getParameter("pdf_type")==null?"All":request.getParameter("pdf_type");

if(month.length() == 1)
{
	month="0"+month; 
}

dlng_inv.setCallFlag("DLNG_FFLOW_INVOICE_PREPARATION_LIST");
dlng_inv.setComp_cd(owner_cd);
dlng_inv.setMonth(month);
dlng_inv.setYear(year);
//dlng_inv.setGas_dt(gas_dt);
dlng_inv.setFf_print_pdf_type(pdf_type);
dlng_inv.setFf_view_pdf_type(pdf_type);
dlng_inv.setFf_mail_pdf_type(pdf_type);
dlng_inv.init();

//FOR F FLOW
Vector VOTH_TRUCK_REG_NO = dlng_inv.getVOTH_TRUCK_REG_NO();
Vector VOTH_TRUCK_CD = dlng_inv.getVOTH_TRUCK_CD();
Vector VOTH_COUNTERPTY_CD = dlng_inv.getVOTH_COUNTERPTY_CD();
Vector VOTH_COUNTERPTY_NM = dlng_inv.getVOTH_COUNTERPTY_NM();
Vector VOTH_COUNTERPTY_ABBR = dlng_inv.getVOTH_COUNTERPTY_ABBR();
Vector VOTH_CONT_NO = dlng_inv.getVOTH_CONT_NO();
Vector VOTH_CONT_REV_NO = dlng_inv.getVOTH_CONT_REV_NO();
Vector VOTH_AGMT_NO = dlng_inv.getVOTH_AGMT_NO();
Vector VOTH_AGMT_REV_NO = dlng_inv.getVOTH_AGMT_REV_NO();
Vector VOTH_START_DT = dlng_inv.getVOTH_START_DT();
Vector VOTH_END_DT = dlng_inv.getVOTH_END_DT();
Vector VOTH_CONT_NAME = dlng_inv.getVOTH_CONT_NAME();
Vector VOTH_CONT_REF_NO = dlng_inv.getVOTH_CONT_REF_NO();
Vector VOTH_CONTRACT_TYPE = dlng_inv.getVOTH_CONTRACT_TYPE();
Vector VOTH_PLANT_SEQ = dlng_inv.getVOTH_PLANT_SEQ();
Vector VOTH_PLANT_ABBR = dlng_inv.getVOTH_PLANT_ABBR();
Vector VOTH_BU_PLANT_SEQ = dlng_inv.getVOTH_BU_PLANT_SEQ();
Vector VOTH_BU_PLANT_ABBR = dlng_inv.getVOTH_BU_PLANT_ABBR();
Vector VOTH_DEAL_NO = dlng_inv.getVOTH_DEAL_NO();
Vector VOTH_PERIOD_START_DT = dlng_inv.getVOTH_PERIOD_START_DT();
Vector VOTH_PERIOD_END_DT = dlng_inv.getVOTH_PERIOD_END_DT();
Vector VOTH_INVOICE_NO = dlng_inv.getVOTH_INVOICE_NO();
Vector VOTH_STATUS = dlng_inv.getVOTH_STATUS();
Vector VOTH_BILLING_FREQ_FLAG = dlng_inv.getVOTH_BILLING_FREQ_FLAG();
Vector VOTH_BILLING_FREQ_NM = dlng_inv.getVOTH_BILLING_FREQ_NM();
Vector VOTH_INV_CHECKED_FLAG = dlng_inv.getVOTH_INV_CHECKED_FLAG();
Vector VOTH_INV_APPROVED_FLAG = dlng_inv.getVOTH_INV_APPROVED_FLAG();
Vector VOTH_BU_STATE_TIN = dlng_inv.getVOTH_BU_STATE_TIN();
Vector VOTH_FINANCIAL_YEAR = dlng_inv.getVOTH_FINANCIAL_YEAR();
Vector VOTH_INVOICE_EXIST = dlng_inv.getVOTH_INVOICE_EXIST();
Vector VOTH_INVOICE_SEQ=dlng_inv.getVOTH_INVOICE_SEQ();
Vector VOTH_PDF_INV_FLAG=dlng_inv.getVOTH_PDF_INV_FLAG();
Vector VOTH_PDF_TYPE=dlng_inv.getVOTH_PDF_TYPE();
Vector VOTH_PDF_FILE_NAME=dlng_inv.getVOTH_PDF_FILE_NAME();
Vector VOTH_PDF_FILE_PATH=dlng_inv.getVOTH_PDF_FILE_PATH();
Vector VOTH_PDF_SIGNED_FLAG=dlng_inv.getVOTH_PDF_SIGNED_FLAG();
Vector VOTH_SIGN_PDF_TYPE=dlng_inv.getVOTH_SIGN_PDF_TYPE();
Vector VOTH_INVOICE_TYPE=dlng_inv.getVOTH_INVOICE_TYPE();
Vector VOTH_SAP_APPROVAL_FLAG = dlng_inv.getVOTH_SAP_APPROVAL_FLAG();
Vector VOTH_IS_IRN_GENERATED = dlng_inv.getVOTH_IS_IRN_GENERATED();
Vector VMAPPING_ID = dlng_inv.getVMAPPING_ID();
Vector VCARGO_NO = dlng_inv.getVCARGO_NO();
Vector VFIN_SYS = dlng_inv.getVFIN_SYS();
Vector VINVOICE_DT = dlng_inv.getVINVOICE_DT();

Vector VRE_PRINT_PDF = dlng_inv.getVRE_PRINT_PDF();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//";
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;
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
					    	DLNG Sales FFLOW Invoice Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%-- <div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Gas Day<span class="s-red">*</span></b></label>
								</div>
			    				<div class="col">
				      				<div class="input-group input-group-sm" >
				      					<span class="input-group-text" onclick="nextDate('-1');" title="click for Back Date"><i class="fa fa-backward fa-lg"></i></span>
					      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="gas_dt" id="gas_dt" value="<%=gas_dt%>" maxLength="10" 
					      				onchange="validateDate(this);refresh();">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      					<span class="input-group-text" onclick="nextDate('1');" title="click for Next Date"><i class="fa fa-forward fa-lg"></i></span>
				      				</div>
				    			</div>
				    		</div>
						</div>
						<div class="col-sm-9 col-xs-9 col-md-9">
					  		<div class="d-flex justify-content-end">
					  			<div class="form-group row">
									<div class="col-auto">
										<div class="btn-group">
											<a href="<%=url%>pdf_signer//PDFSigner.jar" download>
												<label class="btn btn-outline-secondary subbtngrp">
													<i class="fa fa-pencil-square-o"></i>&nbsp;Sign PDF
												</label>
											</a>
										</div>
									</div>
									<div class="col-auto">
										<div class="btn-group">
											<label style="color:blue; font-size: 20px;" title="Click to Refresh" onclick="location.reload();"><i class="fa fa-refresh"></i></label>
										</div>
									</div>
								</div>
							</div>
					  	</div>
					</div> --%>
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
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
					  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
					  		</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
					  	</div>
					  	<div class="col-sm-3 col-xs-3 col-md-3">
					  		<div class="d-flex justify-content-end">
					  			<div class="form-group row">
									<div class="col-auto">
										<div class="btn-group">
											<a href="<%=url%>pdf_signer//PDFSigner.jar" download>
												<label class="btn btn-outline-secondary subbtngrp" onclick="downloadPdfSigner('<%=url%>');">
													<i class="fa fa-pencil-square-o"></i>&nbsp;Sign PDF
												</label>
											</a>
										</div>
									</div>
									<div class="col-auto">
										<div class="btn-group">
											<label style="color:blue; font-size: 20px;" title="Click to Refresh" onclick="location.reload();"><i class="fa fa-refresh"></i></label>
										</div>
									</div>
								</div>
							</div>
					  	</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" onclick="openFreeFlowInv();"><i class="fa fa-plus-circle"></i>&nbsp;Create New</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover serchtbl" id="example0">
									<thead id="tbsearch0">
										<tr>
											<!-- <th rowspan="2">Truck No.</th> -->
											<th rowspan="2">Customer</th>
											<th rowspan="2">Contract No<br>[Contract/Trade Ref#]</th>
											<th rowspan="2">Contract Period</th>
											<th rowspan="2">Plant</th>
											<th rowspan="2">Business Unit</th>
											<!-- <th rowspan="2">Billing Cycle</th> -->
											<th rowspan="2">Billing Period</th>
											<th rowspan="2">Invoice#</th>
											<th rowspan="2">View IRP</th>
											<th rowspan="2">Modify IRP</th>
											<th rowspan="2">IRP Check</th>					
											<th rowspan="2">Fin Ops<br>Finalization</th>
											<th rowspan="2">SAP Xml</th>
											<th rowspan="2">IRN Status</th>
											<th colspan="3">
												<div align="center">
													<select class="form-select form-select-sm" name="pdf_type" style="width:80px;" onchange="refresh();">
														<option value="O">Original</option>
														<option value="D">Duplicate</option>
														<option value="T">Triplicate</option>
														<option value="All">All</option>
													</select>
													<script>document.forms[0].pdf_type.value="<%=pdf_type%>"</script>		
												</div>
											</th>
										</tr>
										<tr>
											<th>Print PDF</th>
											<th>View PDF</th>
											<th>Send Mail</th>
										</tr>
									</thead>
									<tbody>
									<%if(VOTH_COUNTERPTY_CD.size() > 0){ %>
										<%for(int i=0; i<VOTH_COUNTERPTY_CD.size(); i++){ %>
										<tr>
											<%-- <td align="center">
												<%=VOTH_TRUCK_REG_NO.elementAt(i)%>
											</td> --%>
											<td align="center" title="<%=VOTH_COUNTERPTY_NM.elementAt(i)%>">
												<%=VOTH_COUNTERPTY_ABBR.elementAt(i)%>
											</td>
											<td align="center">
												<font color="blue"><%=VOTH_DEAL_NO.elementAt(i)%></font><br>[<%=VOTH_CONT_REF_NO.elementAt(i)%>]
											</td>
											<td align="center"><%=VOTH_START_DT.elementAt(i)%> - <%=VOTH_END_DT.elementAt(i)%></td>
											<td align="center"><%=VOTH_PLANT_ABBR.elementAt(i)%></td>
											<td align="center"><%=VOTH_BU_PLANT_ABBR.elementAt(i)%></td>
											<%-- <td align="center">
												<span 
		    									<%if(VOTH_BILLING_FREQ_NM.elementAt(i).equals("1st-Fortnight")){ %>
		    										class="alert alert-info"
		    									<%}else if(VOTH_BILLING_FREQ_NM.elementAt(i).equals("2nd-Fortnight")){ %>
		    										class="alert alert-warning"
		    									<%}else if(VOTH_BILLING_FREQ_NM.elementAt(i).equals("1st-Weekly")){ %>
		    										class="alert" style="background:#eeccff;color: #660099;"
		    									<%}else if(VOTH_BILLING_FREQ_NM.elementAt(i).equals("2nd-Weekly")){ %>
		    										class="alert alert-dark"
		    									<%}else if(VOTH_BILLING_FREQ_NM.elementAt(i).equals("3rd-Weekly")){ %>
		    										class="alert alert-success"
		    									<%}else if(VOTH_BILLING_FREQ_NM.elementAt(i).equals("4th-Weekly")){ %>
		    										class="alert alert-danger"
		    									<%}else if(VOTH_BILLING_FREQ_NM.elementAt(i).equals("5th-Weekly")){ %>
		    										class="alert" style="background:#e6ccff;color:#330066;"
		    									<%}else if(VOTH_BILLING_FREQ_NM.elementAt(i).equals("Monthly")){ %>
		    										class="alert alert-primary"
		    									<%}else if(VOTH_BILLING_FREQ_NM.elementAt(i).equals("Other")){ %>
		    										class="alert" style="background:#b3ffb3;color: #008000;"
		    									<%} %>
		    									><b><%=VOTH_BILLING_FREQ_NM.elementAt(i)%></b></span>												
											</td> --%>
											<td align="center">
											<%=VOTH_PERIOD_START_DT.elementAt(i)%> - <%=VOTH_PERIOD_END_DT.elementAt(i)%>
											</td>
											<td align="center"><%=VOTH_INVOICE_NO.elementAt(i)%></td>
											<td align="center">												
												<i class="fa fa-eye fa-2x"	
												onclick="openFreeFlowInv_Modify('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_PERIOD_START_DT.elementAt(i)%>','<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>',
													'<%=VOTH_INVOICE_NO.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>',
													'<%=VOTH_BU_STATE_TIN.elementAt(i)%>','','<%=VCARGO_NO.elementAt(i)%>');"																							
												style="color:black;">
												</i>
											</td>
											<td align="center">
												<i class="fa fa-pencil fa-2x"  
												onclick="openFreeFlowInv_Modify('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_PERIOD_START_DT.elementAt(i)%>','<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>',
													'<%=VOTH_INVOICE_NO.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>',
													'<%=VOTH_BU_STATE_TIN.elementAt(i)%>','PREPARE','<%=VCARGO_NO.elementAt(i)%>');"
												style="<%if(VOTH_INV_CHECKED_FLAG.elementAt(i).equals("Y")){ %>
													pointer-events: none; opacity: .65; color: gray;
														<%} else{%>
															color: orange;
														<%}%>">
												</i>
											</td>
											<td align="center">												
												<i class="fa fa-stethoscope fa-2x"
												onclick="openFreeFlowInv_Approval('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_PERIOD_START_DT.elementAt(i)%>','<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>',
													'<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>',
													'<%=VOTH_BU_STATE_TIN.elementAt(i)%>','CHECK','<%=VCARGO_NO.elementAt(i)%>');"												
												style="<%if(VOTH_INV_APPROVED_FLAG.elementAt(i).equals("Y") || VOTH_INVOICE_EXIST.elementAt(i).equals("N")){ %>
														pointer-events: none; opacity: .65; color: gray;
														<%} else {%>
															color:#ff3399;
														<%}%>">
												</i>
											</td>
											<td align="center">	
											<%if(VRE_PRINT_PDF.elementAt(i).equals("Y")){%>	
													<i class="fa fa-eye fa-2x"
													onclick="openFreeFlowInv_Approval('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_PLANT_SEQ.elementAt(i)%>',
														'<%=VOTH_PERIOD_START_DT.elementAt(i)%>','<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
														'<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>',
														'<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>',
														'<%=VOTH_BU_STATE_TIN.elementAt(i)%>','','<%=VCARGO_NO.elementAt(i)%>');"
													style="color:black;">
													</i>
											<%}else{ %>								
													<i class="fa fa-flag fa-2x"
													onclick="openFreeFlowInv_Approval('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_PLANT_SEQ.elementAt(i)%>',
														'<%=VOTH_PERIOD_START_DT.elementAt(i)%>','<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
														'<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>',
														'<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>',
														'<%=VOTH_BU_STATE_TIN.elementAt(i)%>','APPROVE','<%=VCARGO_NO.elementAt(i)%>');"
													style="<%if(!VOTH_PDF_INV_FLAG.elementAt(i).equals("") || !VOTH_INV_CHECKED_FLAG.elementAt(i).equals("Y") || VOTH_IS_IRN_GENERATED.elementAt(i).equals("Y")){ %>
															pointer-events: none; opacity: .65; color: gray;
															<%} else{%>
															color:#00cc00;
															<%}%>">
													</i>
												<%} %>											
											</td>
											<td align="center">
												<%if(!owner_cd.equals("1") && utildate.getDays(""+VINVOICE_DT.elementAt(i),"01/04/2026")<=0){ %>
													<span class="fa-stack fa-lg">
													  <i class="fa fa-eye fa-stack-1x"></i>
													  <i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
													</span>
												<%}else{%>
													<i class="fa <%if(!VOTH_SAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
													onclick="doGenXML('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_INVOICE_NO.elementAt(i)%>',
													 		'<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>',
													 		'<%=VOTH_CONTRACT_TYPE.elementAt(i)%>','FFLOW',
													 		'<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_BU_STATE_TIN.elementAt(i)%>',
													 		'<%=VOTH_SAP_APPROVAL_FLAG.elementAt(i)%>','<%=VOTH_AGMT_NO.elementAt(i)%>',
													 		'<%=VOTH_CONT_NO.elementAt(i)%>','<%=VOTH_PDF_INV_FLAG.elementAt(i)%>','<%=VCARGO_NO.elementAt(i)%>');"
													<%-- style="<%if(!VOTH_INV_APPROVED_FLAG.elementAt(i).equals("Y")){ %> --%>
													style="<%if(!VOTH_INVOICE_EXIST.elementAt(i).equals("Y")){ %>
														pointer-events: none; opacity: .65; color: gray;
														<%} else{%>
															<%if(!VOTH_SAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
																color: orange;
															<%} else{%>
																color: brown;
															<%}%>	
														<%}%>">													
													</i>
												<%} %>
											</td>
											<td align="center">
											<%if(VOTH_IS_IRN_GENERATED.elementAt(i).equals("NA")) {%>
												<span class="fa-stack fa-lg">
												  <i class="fa fa-qrcode fa-stack-1x"></i>
												  <i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
												</span>
											<%}else{ %>
												<i class="fa fa-qrcode fa-2x"
													<%if(VOTH_IS_IRN_GENERATED.elementAt(i).equals("Y")) {%>
													title="IRN Generated!"
													style="color: #0099cc;"
													<%}else{ %>
													title="Generation of IRN is Pending!"
													style="opacity: .65; color: gray;"
													<%} %>	
												></i>
											<%} %>
											</td>
											<td align="center">
												<i class="fa fa-print fa-2x" title="<%=VOTH_PDF_TYPE.elementAt(i)%>" 
													onclick="fflowPrintPDF('<%=VOTH_BU_PLANT_SEQ.elementAt(i) %>','<%=VOTH_COUNTERPTY_CD.elementAt(i) %>','<%=VOTH_BILLING_FREQ_FLAG.elementAt(i) %>',
														'<%=VMAPPING_ID.elementAt(i)%>',
														'<%=VOTH_PLANT_SEQ.elementAt(i) %>','<%=VOTH_INVOICE_TYPE.elementAt(i) %>',
														'<%=VOTH_INVOICE_NO.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i) %>',
														'<%=VOTH_BU_STATE_TIN.elementAt(i)%>','<%=VOTH_CONTRACT_TYPE.elementAt(i)%>','<%=i%>','<%=VCARGO_NO.elementAt(i)%>');"
												
													style="<%if(VRE_PRINT_PDF.elementAt(i).equals("Y") && !VOTH_PDF_TYPE.elementAt(i).equals(pdf_type)){%>
													color:#800000;
													<%}else if(!VOTH_INV_APPROVED_FLAG.elementAt(i).equals("Y") || VOTH_PDF_TYPE.elementAt(i).equals(pdf_type) || VOTH_IS_IRN_GENERATED.elementAt(i).equals("N")){ %>
													pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
													color:#800000;
													<%}%>">
												</i>
												<input type="hidden" name="oth_all_pdf_type" id="oth_all_pdf_type<%=i%>" value="<%=VOTH_PDF_TYPE.elementAt(i)%>">
											</td>
											<td align="center">
												<i class="fa fa-file-pdf-o fa-2x"
													<%if(pdf_type.equals("All")){ %>
													onclick="openAllFfPdfFile('<%=VOTH_FINANCIAL_YEAR.elementAt(i) %>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>',
														'<%=VOTH_BU_STATE_TIN.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','FF')"
													<%}else{ %>
													onclick="openPdfFile('<%=file_url%><%=VOTH_PDF_FILE_PATH.elementAt(i)%><%=VOTH_PDF_FILE_NAME.elementAt(i)%>')"
													<%} %>
													style="<%if(VOTH_PDF_FILE_NAME.elementAt(i).equals("")){ %>
													pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
													color:red;
													<%}%>">
												</i>
											</td>
											<td align="center">
												<i class="fa fa-envelope-o fa-2x" title="<%=VOTH_SIGN_PDF_TYPE.elementAt(i)%>"
													onclick="openffMailBody('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_AGMT_NO.elementAt(i)%>',
														'<%=VOTH_AGMT_REV_NO.elementAt(i)%>','<%=VOTH_CONT_NO.elementAt(i)%>',
														'<%=VOTH_CONT_REV_NO.elementAt(i)%>','<%=VOTH_CONTRACT_TYPE.elementAt(i)%>',
														'<%=VOTH_PLANT_SEQ.elementAt(i)%>','<%=VOTH_PERIOD_START_DT.elementAt(i)%>',
														'<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
														'<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i) %>',
														'<%=VOTH_BU_STATE_TIN.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>',
														'<%=i%>','<%=VOTH_INVOICE_TYPE.elementAt(i) %>','<%=VCARGO_NO.elementAt(i)%>');"
													style="<%if(VOTH_SIGN_PDF_TYPE.elementAt(i).equals("")){ %>
													pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
													color:blue;
													<%}%>">
												</i>
												<input type="hidden" name="oth_all_mail_pdf_type" id="oth_all_mail_pdf_type<%=i%>" value="<%=VOTH_SIGN_PDF_TYPE.elementAt(i)%>">
											</td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td align="center" colspan="18"><%=utilmsg.infoMessage("<b>No Invoice Data Availble!</b>") %></td>
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
<input type="hidden" name="u" value="<%=u%>">
</form>

<script>
$(document).ready(function() {	
	$('.serchtbl').each(function(k){
		$('#tbsearch'+k).each(function(j){						
			$('#tbsearch'+k+' th').each(function(i){
				var title = $(this).text();
				if($(this).hasClass('tbser'+k))
				{
					$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+''+k+'" onkeyup="Search(this,'+i+','+k+');" placeholder="Search '+title+'" style="width:100px"/></div>');
				}
			});		
		});
	});
});
	
function Search(obj, indx, tblid) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+tblid);
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	//tbody=tr[i].getElementsByTagName("tbody");alert(tbody)
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
</body>
</html>