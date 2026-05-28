<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh(accroid)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	var prev_billing_cycle = document.forms[0].prev_billing_cycle.value;
	
	//var pdf_type = document.forms[0].pdf_type.value;
	
	var pdf_type="All";
	if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	}
	
	var msg="";
	var flag = true;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_service_invoice_preperation.jsp?&u="+u+"&pdf_type="+pdf_type+"&month="+month+"&year="+year+"&billing_cycle="+billing_cycle;

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

var newWindow;
function openActivity1(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,
		plant_seq,period_st_dt,period_end_dt,bu,operation,
		sell_agmt_no,sell_agmt_rev,sell_cont_no,sell_cont_rev,sell_cont_type,bu_st_cd,fin_yr,
		qtyMmbtu,sell_cont_map,inv_flag,billing_cycle,heading,cargo_no,accroid)
{
	
	var u = document.forms[0].u.value;
	var url="";
	
	if(heading == "TMS_INV")
	{
		url = "frm_generate_dlng_svc_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
		"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
		"&period_end_dt="+period_end_dt+"&bu_unit="+bu+"&operation="+operation+
		"&u="+u+"&sell_agmt_no="+sell_agmt_no+"&sell_agmt_rev_no="+sell_agmt_rev+
		"&sell_cont_no="+sell_cont_no+"&sell_cont_rev_no="+sell_cont_rev+"&sell_contract_type="+sell_cont_type+
		"&exist_financial_year="+fin_yr+
		"&sell_cont_map="+sell_cont_map+
		"&qtyMmbtu="+qtyMmbtu+"&billing_cycle="+billing_cycle+
		"&bu_state_tin="+bu_st_cd+"&inv_flag="+inv_flag;
	}
	else
	{
		url = "frm_generate_dlng_ltcora_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
		"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
		"&period_end_dt="+period_end_dt+"&bu_unit="+bu+"&operation="+operation+"&u="+u+
		"&exist_financial_year="+fin_yr+"&qtyMmbtu="+qtyMmbtu+"&billing_cycle="+billing_cycle+
		"&bu_state_tin="+bu_st_cd+"&cargo_no="+cargo_no+
		"&accroid="+accroid+
		"&inv_flag="+inv_flag;
	}
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare LTCORA DLNG Service Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare DLNG Service Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function openActivity3(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,activityFlag,inv_flag,sell_cont_map,heading,cargo_no)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_svc_view_chk_aprv_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+"&activityFlag="+activityFlag+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+
			"&inv_flag="+inv_flag+"&cargo_no="+cargo_no+"&u="+u+
			"&sell_cont_map="+sell_cont_map;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare DLNG Service Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare DLNG Service Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function printPDF(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,
		period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,index,accroid,inv_flag,sell_cont_map,cargo_no)
{
	var print_access = document.forms[0].print_access.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	
	var pdf_type="";
	if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	}
	
	var all_pdf_type = document.getElementById("all_pdf_type"+index).value;
	
	if(pdf_type == "All")
	{
		pdf_type=all_pdf_type;
	}
	
	var u = document.forms[0].u.value;
	
	if(print_access=="N")
	{
		alert("You don't have Print Rights!");	
	}
	else
	{
		var url = "pdf_dlng_svc_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&accroid="+accroid+"&inv_flag="+inv_flag+
			"&sell_cont_map="+sell_cont_map+"&billing_cycle="+billing_cycle+
			"&print_pdf_type="+pdf_type+
			"&cargo_no="+cargo_no+
			"&u="+u;
	
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"PDF DLNG Service Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"PDF DLNG Service Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
	}
}

function openPdfFile(url)
{
	window.open(url);
}

function refershPar(sub_msg,msg_type,accroid)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	
	var pdf_type="O";
	if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	}
	
	var msg="";
	var flag = true;
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "frm_dlng_service_invoice_preperation.jsp?&u="+u+"&pdf_type="+pdf_type+
			"&month="+month+
			"&year="+year+
			"&billing_cycle="+billing_cycle+
			"&accroid="+accroid+
			"&msg="+sub_msg+"&msg_type="+msg_type;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg)
	}
}

function doGenXML(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,type_flag,invoice_type,
		bu_state_tin,sap_approval_flag,agmt_no,cont_no, inv_pdf_flag)
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
		period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,index,accroid,inv_flag,all_mail_pdf_type,cargo_no)
{
	var pdf_type="";
	if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	}
	
	//var all_mail_pdf_type = document.getElementById("all_mail_pdf_type"+index).value;
	
	if(pdf_type == "All")
	{
		pdf_type=all_mail_pdf_type;
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_sign_pdf_mail.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&mail_pdf_type="+pdf_type+
			"&mail_inv_type=SVC&inv_flag="+inv_flag+
			"&cargo_no="+cargo_no
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
		period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,index,accroid,inv_flag)
{
	var pdf_type="402";
	
	var u = document.forms[0].u.value;
	
	var url = "frm_sign_pdf_mail.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&mail_pdf_type="+pdf_type+
			"&mail_inv_type=SVC&inv_flag="+inv_flag+
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
	var url = "rpt_view_all_pdf.jsp?financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&flag=SVC";
	
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
//int currentYear = utildate.getCurrentYear();
//int currentMonth = utildate.getCurrentMonth();
int filter_start_year = CommonVariable.filter_start_year;

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
String gas_dt = request.getParameter("gas_dt")==null?sysdate:request.getParameter("gas_dt");
String pdf_type=request.getParameter("pdf_type")==null?"All":request.getParameter("pdf_type");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String ff_pdf_type=request.getParameter("ff_pdf_type")==null?"O":request.getParameter("ff_pdf_type");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
if(billing_cycle.equals(""))
{
	billing_cycle=temp_billing_cycle;
}

if(month.length() == 1)
{
	month="0"+month; 
}

dlng_inv.setCallFlag("DLNG_SERVICE_INVOICE_PREPARATION_LIST");
dlng_inv.setComp_cd(owner_cd);
dlng_inv.setMonth(month);
dlng_inv.setYear(year);
dlng_inv.setBilling_cycle(billing_cycle);
dlng_inv.setPrint_pdf_type(pdf_type);
dlng_inv.setView_pdf_type(pdf_type);
dlng_inv.setMail_pdf_type(pdf_type);
dlng_inv.init();

Vector VINVOICE_LIST_ABBR = dlng_inv.getVINVOICE_LIST_ABBR();
Vector VINVOICE_LIST_NAME = dlng_inv.getVINVOICE_LIST_NAME();
Vector VINDEX = dlng_inv.getVINDEX();

Vector VCOUNTERPTY_CD = dlng_inv.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = dlng_inv.getVCOUNTERPTY_ABBR();
Vector VCOUNTERPTY_NM = dlng_inv.getVCOUNTERPTY_NM();
Vector VCONT_NO = dlng_inv.getVCONT_NO();
Vector VCONT_REV_NO = dlng_inv.getVCONT_REV_NO();
Vector VCARGO_NO = dlng_inv.getVCARGO_NO();
Vector VAGMT_NO = dlng_inv.getVAGMT_NO();
Vector VAGMT_REV_NO = dlng_inv.getVAGMT_REV_NO();
Vector VSTART_DT = dlng_inv.getVSTART_DT();
Vector VEND_DT = dlng_inv.getVEND_DT();
Vector VCONT_REF_NO = dlng_inv.getVCONT_REF_NO();
Vector VCONTRACT_TYPE = dlng_inv.getVCONTRACT_TYPE();
Vector VPLANT_SEQ = dlng_inv.getVPLANT_SEQ();
Vector VPLANT_ABBR = dlng_inv.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = dlng_inv.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = dlng_inv.getVBU_PLANT_ABBR();
Vector VDEAL_NO = dlng_inv.getVDEAL_NO();
Vector VAGMT_BASE = dlng_inv.getVAGMT_BASE();
Vector VBU_STATE_TIN = dlng_inv.getVBU_STATE_TIN();
Vector VFINANCIAL_YEAR = dlng_inv.getVFINANCIAL_YEAR();
Vector VINVOICE_SEQ = dlng_inv.getVINVOICE_SEQ();
Vector VINVOICE_DT = dlng_inv.getVINVOICE_DT();
Vector VINV_CHECKED_FLAG = dlng_inv.getVINV_CHECKED_FLAG();
Vector VINV_APPROVED_FLAG = dlng_inv.getVINV_APPROVED_FLAG();
Vector VINVOICE_EXIST = dlng_inv.getVINVOICE_EXIST();
Vector VINV_FLAG = dlng_inv.getVINV_FLAG();
Vector VPERIOD_START_DT = dlng_inv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = dlng_inv.getVPERIOD_END_DT();
Vector VPDF_INV_FLAG = dlng_inv.getVPDF_INV_FLAG();
Vector VINVOICE_NO = dlng_inv.getVINVOICE_NO();
Vector VSAP_APPROVAL_FLAG=dlng_inv.getVSAP_APPROVAL_FLAG();
Vector VPDF_TYPE = dlng_inv.getVPDF_TYPE();
Vector VPDF_FILE_PATH = dlng_inv.getVPDF_FILE_PATH();
Vector VPDF_FILE_NAME = dlng_inv.getVPDF_FILE_NAME();
Vector VSIGN_PDF_TYPE = dlng_inv.getVSIGN_PDF_TYPE();
Vector VGENERATED_FORM_402_PATH = dlng_inv.getVGENERATED_FORM_402_PATH();
Vector VFIN_SYS = dlng_inv.getVFIN_SYS();
Vector VIS_IRN_GENERATED = dlng_inv.getVIS_IRN_GENERATED();

Vector VSELL_AGMT_NO = dlng_inv.getVSELL_AGMT_NO();
Vector VSELL_AGMT_REV_NO = dlng_inv.getVSELL_AGMT_REV_NO();
Vector VSELL_CONT_NO = dlng_inv.getVSELL_CONT_NO();
Vector VSELL_CONT_REV_NO = dlng_inv.getVSELL_CONT_REV_NO();
Vector VSELL_CONTRACT_TYPE = dlng_inv.getVSELL_CONTRACT_TYPE();
Vector VTOTAL_TRUCK_INV_QTY = dlng_inv.getVTOTAL_TRUCK_INV_QTY();
Vector VSELL_CONT_MAP = dlng_inv.getVSELL_CONT_MAP();
Vector VSELL_DISP_CONT = dlng_inv.getVSELL_DISP_CONT();
Vector VSELL_CONT_REF = dlng_inv.getVSELL_CONT_REF();
Vector VEMAIL_SENT = dlng_inv.getVEMAIL_SENT();
Vector VEMAIL_SENT_INFO = dlng_inv.getVEMAIL_SENT_INFO();

//System.out.println("VINDEX---"+VINDEX);
//System.out.println("VCARGO_NO---"+VCARGO_NO);

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
					    	DLNG Service Invoice Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Month/Year</b></label>
					  			</div>
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
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Billing Cycle</b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="billing_cycle" onchange="">
					  					<option value="1">1st-Fortnight</option>
										<option value="2">2nd-Fortnight</option>
										<option value="3">1st-Weekly</option>
										<option value="4">2nd-Weekly</option>
										<option value="5">3rd-Weekly</option>
										<option value="6">4th-Weekly</option>
										<option value="9">5th-Weekly</option>
										<option value="7">Monthly</option>
										<option value="8">Other</option>
										<option value="0">All</option>
					  				</select>
					  				<script>document.forms[0].billing_cycle.value="<%=billing_cycle%>"</script>
					  			</div>
					  			<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
								</div>
					  		</div>
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
					<%int i=0,k=0,l=0,m=0;
					for(int j=0; j<VINVOICE_LIST_NAME.size(); j++)
					{ 
						int index=Integer.parseInt(""+VINDEX.elementAt(j));
						String heading = ""+VINVOICE_LIST_ABBR.elementAt(j);
						%>
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="<%=heading%>">
	   										<button name="sub_module_cd" class="accordion-button <%if(!accroid.equals(heading)){%>collapsed<%}%> accor-btn" type="button" 
	   											data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="<%if(!accroid.equals(heading)){%>true<%}else{%>false<%} %>" aria-controls="collapse<%=j%>">
								    			<%=VINVOICE_LIST_NAME.elementAt(j)%>&nbsp;<font color="blue">(<%=index%> Items)</font>
								      		</button>	
								    	</h2>
										<div id="collapse<%=j%>" class="accordion-collapse collapse <%if(accroid.equals(heading)){%>show<%}%>" aria-labelledby="<%=heading%>">
								      		<div class="accordion-body accor-body">
												<div class="row">
													<div class="col-md-12 col-sm-12 col-xs-12">
														<div class="table-responsive">
															<table class="table table-bordered table-hover serchtbl" id="example0">
																<thead id="tbsearch0">
																	<tr>
																		<th rowspan="2" class="tbser0">Customer</th>
																		<th rowspan="2" class="tbser0">Contract No<br>[Contract/Trade Ref#]</th>
																		<%if(heading.equals("TMS_INV")) {%><th rowspan="2" class="tbser0">Linked Contract No<br>[Contract/Trade Ref#]</th><%} %>
																		<th rowspan="2" class="tbser0">Contract Period</th>
																		<th rowspan="2" class="tbser0">Plant</th>
																		<th rowspan="2" class="tbser0">Business Unit</th>
																		<th rowspan="2" class="tbser0">Invoice#<br>[Invoice Date]</th>
																		<th rowspan="2">Generate/<br>View IRP</th>
																		<th rowspan="2">Modify IRP</th>
																		<th rowspan="2">IRP Check</th>					
																		<th rowspan="2">Fin Ops<br>Finalization</th>
																		<th rowspan="2">SAP XML</th>
																		<th rowspan="2">IRN Status</th>
																		<th colspan="3" align="center">
																		<div align="center">
																			<select class="form-select form-select-sm" name="pdf_type" id="pdf_type_<%=heading%>" style="width:100px;" onchange="refresh('<%=heading%>');">
																				<option value="O">Original</option>
																				<option value="D">Duplicate</option>
																				<option value="T">Triplicate</option>
																				<option value="All">All</option>
																			</select>
																			<script>document.getElementById("pdf_type_<%=heading%>").value="<%=pdf_type%>"</script>
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
																<%k=0;
																if(index>0)
																{
																	for(i=i; i<VCOUNTERPTY_CD.size(); i++)
																	{
																		k+=1;
																	%>
																	<tr>
																		<td align="center" title="<%=VCOUNTERPTY_NM.elementAt(i)%>"><%=VCOUNTERPTY_ABBR.elementAt(i) %></td>
																		<td align="center" >
																			<font color="blue"><%=VDEAL_NO.elementAt(i)%></font>
																			<br>[<%=VCONT_REF_NO.elementAt(i)%>]
																		</td>
																		<%if(heading.equals("TMS_INV")) {%>
																		<td align="center" >
																			<font><%=VSELL_DISP_CONT.elementAt(i)%></font>
																			<br>[<%=VSELL_CONT_REF.elementAt(i)%>]
																		</td>
																		<%} %>
																		<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
																		<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
																		<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
																		<td align="center"><%=VINVOICE_NO.elementAt(i)%><br><%=VINVOICE_DT.elementAt(i) %></td>
																		<td align="center">	
																			<i class="fa <%if(VINVOICE_SEQ.elementAt(i).equals("")){ %>fa-cogs<%}else{%>fa-eye<%} %> fa-2x" 
																			onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																								'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																								'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																								'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																								'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','INSERT',
																								'<%=VSELL_AGMT_NO.elementAt(i) %>','<%=VSELL_AGMT_REV_NO.elementAt(i) %>',
																								'<%=VSELL_CONT_NO.elementAt(i) %>','<%=VSELL_CONT_REV_NO.elementAt(i)%>',
																								'<%=VSELL_CONTRACT_TYPE.elementAt(i)%>','<%=VBU_STATE_TIN.elementAt(i)%>',
																								'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VTOTAL_TRUCK_INV_QTY.elementAt(i)%>',
																								'<%=VSELL_CONT_MAP.elementAt(i)%>','<%=VINV_FLAG.elementAt(i)%>',
																								'<%=billing_cycle%>','<%=heading%>','<%=VCARGO_NO.elementAt(i)%>','<%=heading%>');"
																								
																			style="<%if(!VINVOICE_SEQ.elementAt(i).equals("")){ %>
																				color:black;
																				<%}else{%>
																				color: #008080; 
																				<%} %>"
																				>
																			</i>				
																		</td>
																		<td align="center">
																			<i class="fa fa-pencil fa-2x" 
																			onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																								'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																								'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																								'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																								'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','MODIFY',
																								'<%=VSELL_AGMT_NO.elementAt(i) %>','<%=VSELL_AGMT_REV_NO.elementAt(i) %>',
																								'<%=VSELL_CONT_NO.elementAt(i) %>','<%=VSELL_CONT_REV_NO.elementAt(i)%>',
																								'<%=VSELL_CONTRACT_TYPE.elementAt(i)%>','<%=VBU_STATE_TIN.elementAt(i)%>',
																								'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VTOTAL_TRUCK_INV_QTY.elementAt(i)%>',
																								'<%=VSELL_CONT_MAP.elementAt(i)%>','<%=VINV_FLAG.elementAt(i)%>',
																								'<%=billing_cycle%>','<%=heading%>','<%=VCARGO_NO.elementAt(i)%>','<%=heading%>');"
																			style="<%if(VINV_CHECKED_FLAG.elementAt(i).equals("Y") || VINVOICE_EXIST.elementAt(i).equals("N")){ %>
																					pointer-events: none; opacity: .65;color: gray;
																					<%} else {%>
																						color:#ff9900;
																					<%}%>">
																			</i>
																		</td>
																		<td align="center">												
																			<i class="fa fa-stethoscope fa-2x"
																			onclick="openActivity3('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																								'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																								'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																								'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																								'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','CHECK','<%=VINV_FLAG.elementAt(i) %>',
																								'<%=VSELL_CONT_MAP.elementAt(i) %>','<%=heading%>','<%=VCARGO_NO.elementAt(i)%>');"
																			style="<%if(VINV_APPROVED_FLAG.elementAt(i).equals("Y") || VINVOICE_EXIST.elementAt(i).equals("N")){ %>
																					pointer-events: none; opacity: .65; color: gray;
																					<%} else {%>
																						color:#ff3399;
																					<%}%>">
																			</i>											
																		</td>
																		<td align="center">												
																			<i class="fa fa-flag fa-2x"
																			onclick="openActivity3('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																								'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																								'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																								'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																								'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','APPROVE','<%=VINV_FLAG.elementAt(i) %>',
																								'<%=VSELL_CONT_MAP.elementAt(i) %>','<%=heading%>','<%=VCARGO_NO.elementAt(i)%>');"
																			style="<%if(!VPDF_INV_FLAG.elementAt(i).equals("") || !VINV_CHECKED_FLAG.elementAt(i).equals("Y") ||  VINVOICE_EXIST.elementAt(i).equals("N")){ %>
																					pointer-events: none; opacity: .65; color: gray;
																					<%} else{%>
																					color:#00cc00;
																					<%}%>">
																			</i>											
																		</td>
																		<td align="center">
																			<%if(!owner_cd.equals("1") && utildate.getDays(""+VINVOICE_DT.elementAt(i),"01/04/2026")<=0){ %>
																				<span class="fa-stack fa-lg">
																				  <i class="fa fa-eye fa-stack-1x"></i>
																				  <i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
																				</span>
																			<%}else{ %>
																				<i class="fa <%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
																					onclick="doGenXML('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
																					 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																					 'SVC','','<%=VBU_STATE_TIN.elementAt(i)%>','<%=VSAP_APPROVAL_FLAG.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																					 '<%=VCONT_NO.elementAt(i)%>','<%=VPDF_INV_FLAG.elementAt(i)%>');"
																					style="<%if(!VINVOICE_EXIST.elementAt(i).equals("Y")){ %>
																						pointer-events: none; opacity: .65; color: gray;
																						<%} else{%>
																							<%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
																								color: orange;
																							<%} else{%>
																								color: brown;
																							<%}%>		
																						<%}%>">													
																					</i>
																			<%} %>
																		</td>
																	<!-- IRN STATUS -->
																		<td align="center">
																			<i class="fa fa-qrcode fa-2x"
																				<%if(VIS_IRN_GENERATED.elementAt(i).equals("Y")) {%>
																				title="IRN Generated!"
																				style="color: #0099cc;"
																				<%}else{ %>
																				title="Generation of IRN is Pending!"
																				style="opacity: .65; color: gray;"
																				<%} %>	
																			></i>
																		</td>
																		<td align="center">
																			<i class="fa fa-print fa-2x" title="<%=VPDF_TYPE.elementAt(i)%>"
																				onclick="printPDF('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																								'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																								'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																								'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																								'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=i%>','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>',
																								'<%=VSELL_CONT_MAP.elementAt(i) %>','<%=VCARGO_NO.elementAt(i)%>');"
																				style="<%if(!VINV_APPROVED_FLAG.elementAt(i).equals("Y") || VPDF_TYPE.elementAt(i).equals(pdf_type) || !VIS_IRN_GENERATED.elementAt(i).equals("Y")){ %>
																				pointer-events: none; opacity: .65; color: gray;
																				<%} else{%>
																				color:#800000;
																				<%}%>">
																			</i>
																			<input type="hidden" name="all_pdf_type" id="all_pdf_type<%=i%>" value="<%=VPDF_TYPE.elementAt(i)%>">
																		</td>
																		<td align="center">
																			<i class="fa fa-file-pdf-o fa-2x"
																				<%if(pdf_type.equals("All")){ %>
																					onclick="openAllPdfFile('<%=VFINANCIAL_YEAR.elementAt(i) %>',
								 														'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>')"
																				<%}else{ %>
								 													onclick="openPdfFile('<%=file_url%><%=VPDF_FILE_PATH.elementAt(i)%><%=VPDF_FILE_NAME.elementAt(i)%>')"
																				<%} %>
																				style="<%if(VPDF_FILE_NAME.elementAt(i).equals("")){ %>
																				pointer-events: none; opacity: .65; color: gray;
																				<%} else{%>
																				color:red;
																				<%}%>">
																			</i>
																		</td>
																		<td align="center">
																			<i class="<%if(VEMAIL_SENT.elementAt(i).equals("Y")) {%>fa fa-envelope fa-2x<%}else{ %>fa fa-envelope-o fa-2x <%}%>" 
																			 title="<%=(!VSIGN_PDF_TYPE.elementAt(i).toString().equals(""))?VSIGN_PDF_TYPE.elementAt(i).toString()+" Signed":""%>&#10;<%=VEMAIL_SENT_INFO.elementAt(i) %>"
																				onclick="openMailBody('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																					'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																					'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																					'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																					'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																					'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=i%>','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>',
																					'<%=VSIGN_PDF_TYPE.elementAt(i)%>','<%=VCARGO_NO.elementAt(i)%>');"
																				style="<%if(VSIGN_PDF_TYPE.elementAt(i).equals("")){ %>
																				pointer-events: none; opacity: .65; color: gray;
																				<%} else { 
																					if(!VEMAIL_SENT.elementAt(i).equals("Y")){%>
																					color:blue;
																					<%}else{%>
																					color:green;"
																				<%}} %>
																				>
																			</i>
																			<input type="hidden" name="all_mail_pdf_type" id="all_mail_pdf_type<%=i%>" value="<%=VSIGN_PDF_TYPE.elementAt(i)%>">
																		</td>
																	</tr>
																	<%if(k==index){
																			i=i+1;
																			break;
																		} %>
																	<%} %>	
																<%}else{ %>
																	<tr>
																		<td align="center" colspan="<%if(heading.equals("LTCORA_INV_HEAD")) {%>20<%}else{%>19<%}%>"><%=utilmsg.infoMessage("<b>No Invoice is Ready for Generate!</b>") %></td>
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
						</div>
					<%} %>	
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="prev_billing_cycle" value="<%=billing_cycle%>">

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

<input type="hidden" name="accroid" value="">
</form>

<script>
$(document).ready(function() {	
	$('#example0').each(function(k){
// 		$('#tbsearch'+k+' th').each(function(i){
// 		    if($(this).hasClass('tbser'+k))
// 		    {
// 		        let originalTitle = $(this).html();

// 		        $(this).html(
// 		            '<div class="head-title">' + originalTitle + '</div>' +
// 		            '<div align="center">' +
// 		            '<input type="text" class="form-control form-control-sm" ' +
// 		            'onkeyup="Search(this,'+i+','+k+');" ' +
// 		            'placeholder="Search '+$(this).text()+'" style="width:100px"/>' +
// 		            '</div>'
// 		        );
// 		    }
// 		});
		$('#tbsearch'+k+' th').each(function(i){

		    if($(this).hasClass('tbser'+k))
		    {
		        let originalTitle = $(this).html();   
		        let colIndex = i;                     

		        $(this).html(
		            '<div class="head-title">' + originalTitle + '</div>' +
		            '<div align="center">' +
		                '<input type="text" id="search_'+k+'_'+i+'" class="form-control form-control-sm" ' +
		                'onkeyup="Search(this,'+colIndex+','+k+');" ' +
		                'placeholder="Search '+$(this).text()+'"/>' +
		            '</div>'
		        );
		    }
		});
	});
});
	
function Search(obj, indx, tblid) 
{
    var input = obj;
    var filter = input.value.toLowerCase();
    var table = document.getElementById("example"+tblid);

    var tr = table.getElementsByTagName("tr");

    for (var i = 1; i < tr.length; i++) 
    {
        var td = tr[i].getElementsByTagName("td")[indx];

        if (td) 
        {
            var txtValue = td.textContent || td.innerText;

            tr[i].style.display =
                txtValue.toLowerCase().indexOf(filter) > -1 ? "" : "none";
        }
    }
}

</script>
</body>
</html>