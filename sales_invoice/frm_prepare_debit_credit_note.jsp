<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh(accroid)
{
	/* var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	var prev_billing_cycle = document.forms[0].prev_billing_cycle.value; */
	
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	
	var pdf_type="O";
	var ff_pdf_type="O";
	if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	}
	
	
	var msg="";
	var flag = true;
	
	var count = compareDate(from_dt,to_dt);
	if(parseInt(count) == 1)
	{
		msg+="From Date should be less or equal To Date!";
		flag=false;
	}
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "../sales_invoice/frm_prepare_debit_credit_note.jsp?&u="+u+"&pdf_type="+pdf_type+"&from_dt="+from_dt+"&to_dt="+to_dt+"&accroid="+accroid;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
		document.forms[0].from_dt.value="";
	}
}

function checkStartEndDate(flag)
{
	var period_start_dt=document.forms[0].period_start_dt;
	var period_end_dt=document.forms[0].period_end_dt;
	
	var obj = period_start_dt;
	var obj1 = period_end_dt;
	
	if((obj.value!="" && trim(obj.value) != "" && obj.value != null) && (obj1.value!="" && trim(obj1.value) != "" && obj1.value != null))
	{
		if(flag=="F")
		{
			var count = compareDate(obj.value,obj1.value);
			if(parseInt(count) == 1)
			{
				alert("Billing Period Start Date should be less or equal Period End Date!")
				obj.value="";
				return false;
			}
		}
		else if(flag=="T")
		{
			var count = compareDate(obj.value,obj1.value);
			if(parseInt(count) == 1)
			{
				alert("Billing Period End Date should be grater or equal Period Start Date!")
				obj1.value="";
				return false;
			}
		}
	}
}


var newWindow;
function openActivity1(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,
		plant_seq,period_st_dt,period_end_dt,bu,billing_cycle,drcr_fin_yr,bu_st_cd,
		temp_period_st_dt,temp_period_end_dt,cargo_no,operation,accroid,inv_flag,criteria,drcr_flag,drcr_seq,remark,fin_yr)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_generate_debit_credit_note.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+"&operation="+operation+
			"&drcr_fin_yr="+drcr_fin_yr+"&bu_state_tin="+bu_st_cd+"&temp_period_start_dt="+temp_period_st_dt+"&temp_period_end_dt="+temp_period_end_dt+
			"&cargo_no="+cargo_no+"&accroid="+accroid+"&inv_flag="+inv_flag+"&criteria="+criteria+"&drcr_flag="+drcr_flag+"&drcr_seq="+drcr_seq+
			"&remark="+remark+"&exist_financial_year="+fin_yr+"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare Sales Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare Sales Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function openActivity3(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,period_st_dt,period_end_dt,bu,
		billing_cycle,drcr_fin_yr,bu_st_cd,inv_seq,cargo_no,activityFlag,accroid,inv_flag,drcr_flag,drcr_seq,fin_yr)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_drcr_view_chk_aprv_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+"&activityFlag="+activityFlag+
			"&drcr_fin_yr="+drcr_fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&cargo_no="+cargo_no+"&accroid="+accroid+
			"&inv_flag="+inv_flag+"&drcr_flag="+drcr_flag+"&drcr_seq="+drcr_seq+"&financial_year="+fin_yr+"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare Sales Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare Sales Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function PrintPDF(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,
		period_st_dt,period_end_dt,bu,billing_cycle,drcr_fin_yr,bu_st_cd,inv_seq,index,cargo_no,accroid,inv_flag,drcr_flag,drcr_seq,criteria,fin_yr)
{
	var print_access = document.forms[0].print_access.value;

	
	var drcr_pdf_type="";
	if(document.getElementById("drcr_pdf_type_"+accroid)!=null && document.getElementById("drcr_pdf_type_"+accroid)!=undefined)
	{
		drcr_pdf_type = document.getElementById("drcr_pdf_type_"+accroid).value;
	}
	
	var all_pdf_type = document.getElementById("all_pdf_type"+index).value;
	
	if(drcr_pdf_type == "All")
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
		var url = "pdf_drcr_note.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+
			"&drcr_fin_yr="+drcr_fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&cargo_no="+cargo_no+"&accroid="+accroid+"&inv_flag="+inv_flag+
			"&print_pdf_type="+drcr_pdf_type+"&drcr_flag="+drcr_flag+"&drcr_seq="+drcr_seq+"&criteria="+criteria+"&financial_year="+fin_yr+"&u="+u;
	
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"PDF Debit/Credit Note","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"PDF Debit/Credit Note","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
	}
}

function refershPar(sub_msg,msg_type,accroid)
{
	/* var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	var prev_billing_cycle = document.forms[0].prev_billing_cycle.value; */
	
	
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
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
		var url = "frm_prepare_debit_credit_note.jsp?&u="+u+"&pdf_type="+pdf_type+"&from_dt="+from_dt+"&to_dt="+to_dt+"&accroid="+accroid+"&msg="+sub_msg+"&msg_type="+msg_type;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg)
	}
}

function openPdfFile(url)
{
	window.open(url);
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

function openAllFfPdfFile(fin_yr,inv_type,bu_st_cd,inv_seq,flag)
{
	var url = "rpt_view_all_pdf.jsp?financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&invoice_type="+inv_type+"&flag="+flag;
	
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


function openMailBody(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,
		period_st_dt,period_end_dt,bu,billing_cycle,drcr_fin_yr,bu_st_cd,inv_seq,index,accroid,inv_flag,drcr_flag,drcr_seq,criteria,fin_yr)
{
	var drcr_pdf_type="";
	if(document.getElementById("drcr_pdf_type_"+accroid)!=null && document.getElementById("drcr_pdf_type_"+accroid)!=undefined)
	{
		drcr_pdf_type = document.getElementById("drcr_pdf_type_"+accroid).value;
	}

	
	var drcr_all_mail_pdf_type = document.getElementById("drcr_all_mail_pdf_type"+index).value;
	
	if(drcr_pdf_type == "All")
	{
		drcr_pdf_type=drcr_all_mail_pdf_type;
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_drcr_sign_pdf_mail.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+
			"&drcr_fin_yr="+drcr_fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&mail_pdf_type="+drcr_pdf_type+
			"&mail_inv_type=S&inv_flag="+inv_flag+"&drcr_flag="+drcr_flag+"&drcr_seq="+drcr_seq+"&criteria="+criteria+"&financial_year="+fin_yr;
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


function doGenXML(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,type_flag,invoice_type,
		bu_state_tin,sap_approval_flag,agmt_no,cont_no, inv_pdf_flag,accroid)
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
	
	var url = "../accounting/rpt_view_sales_invoice_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
			"&contract_type="+contract_type+"&type_flag="+type_flag+"&invoice_type="+invoice_type+
			"&counterparty_cd="+couterpty_cd+"&invoice_no="+invoice_no+"&bu_state_tin="+bu_state_tin+
			"&sap_approval_flag="+sap_approval_flag+"&agmt_no="+agmt_no+"&cont_no="+cont_no+"&inv_pdf_flag="+inv_pdf_flag+"&accroid="+accroid+
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
//int currentYear = utildate.getCurrentYear();
//int currentMonth = utildate.getCurrentMonth();

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

/* String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle"); */
int curr_year=utildate.getCurrentYear();
String from_dt = request.getParameter("from_dt")==null?"01/01/"+curr_year:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?"31/12/"+curr_year:request.getParameter("to_dt");
String drcr_pdf_type=request.getParameter("drcr_pdf_type")==null?"O":request.getParameter("drcr_pdf_type"); //DhvaniT20250818 for Dr/Cr Note

/* if(billing_cycle.equals(""))
{
	billing_cycle=temp_billing_cycle;
}

if(month.length() == 1)
{
	month="0"+month; 
} */

Drcr_note.setCallFlag("DEBIT_CREDIT_NOTE_LIST");
Drcr_note.setComp_cd(owner_cd);
/* Drcr_note.setMonth(month);
Drcr_note.setYear(year);
Drcr_note.setBilling_cycle(billing_cycle); */
Drcr_note.setFrom_dt(from_dt);
Drcr_note.setTo_dt(to_dt);
Drcr_note.setPrint_pdf_type(drcr_pdf_type);
Drcr_note.setView_pdf_type(drcr_pdf_type);
Drcr_note.setMail_pdf_type(drcr_pdf_type);
Drcr_note.init();

//Deep22082025 
Vector VINVOICE_LIST_ABBR = Drcr_note.getVINVOICE_LIST_ABBR();
Vector VINVOICE_LIST_NAME = Drcr_note.getVINVOICE_LIST_NAME();
Vector VINDEX = Drcr_note.getVINDEX();

//DhvaniT25050818 for Dr/Cr Note
Vector VCOUNTERPARTY_CD = Drcr_note.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = Drcr_note.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = Drcr_note.getVCOUNTERPARTY_NM();
Vector VDRCR_CONT_NO = Drcr_note.getVDRCR_CONT_NO();
Vector VDRCR_CONT_REV_NO = Drcr_note.getVDRCR_CONT_REV_NO();
Vector VDRCR_AGMT_NO = Drcr_note.getVDRCR_AGMT_NO();
Vector VDRCR_AGMT_REV_NO = Drcr_note.getVDRCR_AGMT_REV_NO();
Vector VDRCR_START_DT = Drcr_note.getVDRCR_START_DT();
Vector VDRCR_END_DT = Drcr_note.getVDRCR_END_DT();
Vector VDRCR_CONT_NAME = Drcr_note.getVDRCR_CONT_NAME();
Vector VDRCR_CONT_REF_NO = Drcr_note.getVDRCR_CONT_REF_NO();
Vector VDRCR_CONTRACT_TYPE = Drcr_note.getVDRCR_CONTRACT_TYPE();
Vector VDRCR_PLANT_SEQ = Drcr_note.getVDRCR_PLANT_SEQ();
Vector VDRCR_PLANT_ABBR = Drcr_note.getVDRCR_PLANT_ABBR();
Vector VDRCR_BU_PLANT_SEQ = Drcr_note.getVDRCR_BU_PLANT_SEQ();
Vector VDRCR_BU_PLANT_ABBR = Drcr_note.getVDRCR_BU_PLANT_ABBR();
Vector VDRCR_DEAL_NO = Drcr_note.getVDRCR_DEAL_NO();
Vector VDRCR_PERIOD_START_DT = Drcr_note.getVDRCR_PERIOD_START_DT();
Vector VDRCR_PERIOD_END_DT = Drcr_note.getVDRCR_PERIOD_END_DT();
Vector VDRCR_INVOICE_NO = Drcr_note.getVDRCR_INVOICE_NO();
Vector VDRCR_STATUS = Drcr_note.getVDRCR_STATUS();
Vector VDRCR_BILLING_FREQ_FLAG = Drcr_note.getVDRCR_BILLING_FREQ_FLAG();
Vector VDRCR_BILLING_FREQ_NM = Drcr_note.getVDRCR_BILLING_FREQ_NM();
Vector VDRCR_INV_CHECKED_FLAG = Drcr_note.getVDRCR_INV_CHECKED_FLAG();
Vector VDRCR_INV_APPROVED_FLAG = Drcr_note.getVDRCR_INV_APPROVED_FLAG();
Vector VDRCR_BU_STATE_TIN = Drcr_note.getVDRCR_BU_STATE_TIN();
Vector VDRCR_FINANCIAL_YEAR = Drcr_note.getVDRCR_FINANCIAL_YEAR();
Vector VDRCR_INVOICE_EXIST = Drcr_note.getVDRCR_INVOICE_EXIST();
Vector VDRCR_INVOICE_SEQ=Drcr_note.getVDRCR_INVOICE_SEQ();
Vector VDRCR_NO=Drcr_note.getVDRCR_NO();
Vector VDRCR_PDF_INV_FLAG=Drcr_note.getVDRCR_PDF_INV_FLAG();
Vector VDRCR_PDF_TYPE=Drcr_note.getVDRCR_PDF_TYPE();
Vector VDRCR_PDF_FILE_NAME=Drcr_note.getVDRCR_PDF_FILE_NAME();
Vector VDRCR_PDF_FILE_PATH=Drcr_note.getVDRCR_PDF_FILE_PATH();
Vector VDRCR_PDF_SIGNED_FLAG=Drcr_note.getVDRCR_PDF_SIGNED_FLAG();
Vector VDRCR_SIGN_PDF_TYPE=Drcr_note.getVDRCR_SIGN_PDF_TYPE();
/* Vector VDRCR_INVOICE_TYPE=Drcr_note.getVDRCR_INVOICE_TYPE();
Vector VDRCR_MAPPING_ID=Drcr_note.getVDRCR_MAPPING_ID(); */
Vector VDRCR_SAP_APPROVAL_FLAG = Drcr_note.getVDRCR_SAP_APPROVAL_FLAG();
Vector VDRCR_IS_IRN_GENERATED = Drcr_note.getVDRCR_IS_IRN_GENERATED();
Vector VDRCR_ITEM_DESC = Drcr_note.getVDRCR_ITEM_DESC();
Vector VDEAL_NO = Drcr_note.getVDEAL_NO();
Vector VDEAL_CONT_REF_NO = Drcr_note.getVDEAL_CONT_REF_NO();
Vector VDRCR_CARGO_NO = Drcr_note.getVDRCR_CARGO_NO();
Vector VDRCR_INV_FLAG = Drcr_note.getVDRCR_INV_FLAG();
Vector VDRCR_SEQ = Drcr_note.getVDRCR_SEQ();
Vector VDRCR_REF = Drcr_note.getVDRCR_REF();
Vector VDRCR_FLAG = Drcr_note.getVDRCR_FLAG();
Vector VDRCR_CRITERIA = Drcr_note.getVDRCR_CRITERIA();
Vector VDRCR1_SEQ = Drcr_note.getVDRCR1_SEQ();
Vector VDRCR_REMARK = Drcr_note.getVDRCR_REMARK();
Vector VFINANCIAL_YEAR = Drcr_note.getVFINANCIAL_YEAR();
String criteria=Drcr_note.getcriteria();


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
					    	Sales Debit/Credit Note Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
						<%-- <div class="col-sm-3 col-xs-3 col-md-3">
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
					  					<option value="1">1st-Fortnight</option>
										<option value="2">2nd-Fortnight</option>
										<!-- <option value="3">1st-Weekly</option>
										<option value="4">2nd-Weekly</option>
										<option value="5">3rd-Weekly</option>
										<option value="6">4th-Weekly</option>
										<option value="9">5th-Weekly</option>
										<option value="7">Monthly</option>
										<option value="8">Other</option>
										<option value="11">TCQ Completion</option> -->
										<option value="0">All</option>
					  				</select>
					  				<script>document.forms[0].billing_cycle.value="<%=billing_cycle%>"</script>
					  			</div>
					  		</div>
					  	</div> --%>
					  	<div class="col-sm-6 col-xs-6 col-md-6">
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>From</b></label>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
											<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
											onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
											<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
											<script>document.forms[0].from_dt.value="<%=from_dt%>"</script>
										</div>
									</div>
									<div class="col-auto">
										<label class="form-label"><b>To</b></label>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
											<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
											onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
											<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
											<script>document.forms[0].to_dt.value="<%=to_dt%>"</script>
										</div>
									</div>
								</div>
							</div>
						</div>
					  	<%-- <div class="col-sm-3 col-xs-3 col-md-3"> 	
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
					  	</div> --%>
					</div>
				</div>
				<div class="card-body cdbody">
					<!-- Deep23082025 for Dr/Cr Note  -->
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
   											data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="<%if(accroid.equals(heading)){%>true<%}else{%>false<%} %>" aria-controls="collapse<%=j%>">
							    			<%=VINVOICE_LIST_NAME.elementAt(j)%> &nbsp;<font color="blue">(<%=index%> Items)</font>
							      		</button>	
							    	</h2>
									<div id="collapse<%=j%>" class="accordion-collapse collapse <%if(accroid.equals(heading)){%>show<%}%>" aria-labelledby="<%=heading%>">
							      		<div class="accordion-body accor-body">
											<%-- <div class="row m-b-5">
												<div class="col-sm-12 col-xs-12 col-md-12" align="right">
													<div class="btn-group">
														<label class="btn btn-outline-secondary subbtngrp" onclick="openDrCrNote('<%=accro1%>');"><i class="fa fa-plus-circle"></i>&nbsp;Create New</label>
													</div>
												</div>
											</div> --%>
											<div class="row">
												<div class="col-md-12 col-sm-12 col-xs-12">
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr>
																	<th rowspan="2">Customer</th>
																	<th rowspan="2">Contract No<br>[Contract/Trade Ref#]</th>
																	<th rowspan="2">Contract Period</th>
																	<th rowspan="2">Plant</th>
																	<th rowspan="2">Business Unit</th>
																	<th rowspan="2">Billing Cycle</th>
																	<th rowspan="2">Billing Period</th>
																	<th rowspan="2">Item Description</th>
																	<th rowspan="2">Invoice#</th>
																	<th rowspan="2">DR/CR#</th>
																	<th rowspan="2">Generate/View IRP</th>
																	<th rowspan="2">Modify IRP</th>
																	<th rowspan="2">IRP Check</th>	
																	<th rowspan="2">Fin Ops<br>Finalization</th>				
																	<th rowspan="2">SAP Xml</th>
																	<%if(heading.equals("LTCORA_INV_HEAD")) {%><th rowspan="2">IRN Status</th><%} %>
																	<th colspan="3">
																		<div align="center">
																		<%-- 	<select class="form-select form-select-sm" name="drcr_pdf_type" style="width:80px;" onchange="refresh('<%=heading%>');">
																				<option value="O">Original</option>
																				<option value="D">Duplicate</option>
																				<option value="T">Triplicate</option>
																				<option value="All">All</option>
																			</select>
																			<script>document.forms[0].drcr_pdf_type.value="<%=drcr_pdf_type%>"</script>	 --%>	
																			
																				<select class="form-select form-select-sm" name="drcr_pdf_type" id="drcr_pdf_type_<%=heading%>" style="width:80px;" onchange="refresh('<%=heading%>');">
																				<option value="O">Original</option>
																				<option value="D">Duplicate</option>
																				<option value="T">Triplicate</option>
																				<option value="All">All</option>
																			</select>
																			<script>document.getElementById("drcr_pdf_type_<%=heading%>").value="<%=drcr_pdf_type%>"</script>
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
																for(i=i; i<VCOUNTERPARTY_CD.size(); i++)
																{ 
																	k+=1;
																%>
																<tr>
																	<td align="center">
																		<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
																	</td>
																	<td align="center">
																		<font color="blue"><%=VDEAL_NO.elementAt(i)%></font><br>[<%=VDEAL_CONT_REF_NO.elementAt(i)%>]
																	</td>
																	<td align="center"><%=VDRCR_START_DT.elementAt(i)%> - <%=VDRCR_END_DT.elementAt(i)%></td>
																	<td align="center"><%=VDRCR_PLANT_ABBR.elementAt(i)%></td>
																	<td align="center"><%=VDRCR_BU_PLANT_ABBR.elementAt(i)%></td>
																	<td align="center">
																		<span 
								    									<%if(VDRCR_BILLING_FREQ_NM.elementAt(i).equals("1st-Fortnight")){ %>
								    										class="alert alert-info"
								    									<%}else if(VDRCR_BILLING_FREQ_NM.elementAt(i).equals("2nd-Fortnight")){ %>
								    										class="alert alert-warning"
								    									<%}else if(VDRCR_BILLING_FREQ_NM.elementAt(i).equals("1st-Weekly")){ %>
								    										class="alert" style="background:#eeccff;color: #660099;"
								    									<%}else if(VDRCR_BILLING_FREQ_NM.elementAt(i).equals("2nd-Weekly")){ %>
								    										class="alert alert-dark"
								    									<%}else if(VDRCR_BILLING_FREQ_NM.elementAt(i).equals("3rd-Weekly")){ %>
								    										class="alert alert-success"
								    									<%}else if(VDRCR_BILLING_FREQ_NM.elementAt(i).equals("4th-Weekly")){ %>
								    										class="alert alert-danger"
								    									<%}else if(VDRCR_BILLING_FREQ_NM.elementAt(i).equals("5th-Weekly")){ %>
								    										class="alert" style="background:#e6ccff;color:#330066;"
								    									<%}else if(VDRCR_BILLING_FREQ_NM.elementAt(i).equals("Monthly")){ %>
								    										class="alert alert-primary"
								    									<%}else if(VDRCR_BILLING_FREQ_NM.elementAt(i).equals("Other")){ %>
								    										class="alert" style="background:#b3ffb3;color: #008000;"
								    									<%} %>
								    									><b><%=VDRCR_BILLING_FREQ_NM.elementAt(i)%></b></span>												
																	</td>
																	<td align="center">
																	<%=VDRCR_PERIOD_START_DT.elementAt(i)%> - <%=VDRCR_PERIOD_END_DT.elementAt(i)%>
																	</td>
																	<td align ="center"><%=VDRCR_FLAG.elementAt(i)%> : <%=VDRCR_ITEM_DESC.elementAt(i)%></td>
																	<td align="center"><%=VDRCR_INVOICE_NO.elementAt(i)%></td>
																	<td align="center"><%=VDRCR_NO.elementAt(i)%></td>
																	<td align="center">											
																		<i class="fa <%if(VDRCR_SEQ.elementAt(i).equals("")){ %>fa-cogs<%}else{%>fa-eye<%} %> fa-2x" 
																			onclick="openActivity1('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VDRCR_AGMT_NO.elementAt(i)%>',
																								'<%=VDRCR_AGMT_REV_NO.elementAt(i)%>','<%=VDRCR_CONT_NO.elementAt(i)%>',
																								'<%=VDRCR_CONT_REV_NO.elementAt(i)%>','<%=VDRCR_CONTRACT_TYPE.elementAt(i)%>',
																								'<%=VDRCR_PLANT_SEQ.elementAt(i)%>','<%=VDRCR_PERIOD_START_DT.elementAt(i)%>',
																								'<%=VDRCR_PERIOD_END_DT.elementAt(i)%>','<%=VDRCR_BU_PLANT_SEQ.elementAt(i)%>',
																								'<%=VDRCR_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VDRCR_FINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VDRCR_BU_STATE_TIN.elementAt(i)%>','<%=VDRCR_START_DT.elementAt(i)%>',
																								'<%=VDRCR_END_DT.elementAt(i)%>','<%=VDRCR_CARGO_NO.elementAt(i)%>','INSERT','<%=heading%>',
																								'<%=VDRCR_INV_FLAG.elementAt(i)%>','<%=VDRCR_CRITERIA.elementAt(i)%>','<%=VDRCR_FLAG.elementAt(i)%>',
																								'<%=VDRCR1_SEQ.elementAt(i)%>','<%=VDRCR_REMARK.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i)%>');"
																			style="<%if(!VDRCR_SEQ.elementAt(i).equals("")){ %>
																				color:black;
																				<%}else{%>
																				color: #008080; 
																				<%} %>"
																				>																		
																			</i>		
																	</td>
																	<td align="center">
																		 <i class="fa fa-pencil fa-2x" 
																			onclick="openActivity1('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VDRCR_AGMT_NO.elementAt(i)%>',
																								'<%=VDRCR_AGMT_REV_NO.elementAt(i)%>','<%=VDRCR_CONT_NO.elementAt(i)%>',
																								'<%=VDRCR_CONT_REV_NO.elementAt(i)%>','<%=VDRCR_CONTRACT_TYPE.elementAt(i)%>',
																								'<%=VDRCR_PLANT_SEQ.elementAt(i)%>','<%=VDRCR_PERIOD_START_DT.elementAt(i)%>',
																								'<%=VDRCR_PERIOD_END_DT.elementAt(i)%>','<%=VDRCR_BU_PLANT_SEQ.elementAt(i)%>',
																								'<%=VDRCR_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VDRCR_FINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VDRCR_BU_STATE_TIN.elementAt(i)%>','<%=VDRCR_START_DT.elementAt(i)%>',
																								'<%=VDRCR_END_DT.elementAt(i)%>','<%=VDRCR_CARGO_NO.elementAt(i)%>','MODIFY','<%=heading%>','<%=VDRCR_INV_FLAG.elementAt(i) %>');"
																			style="<%if(!VCOUNTERPARTY_CD.elementAt(i).equals("")){ %>
																					pointer-events: none; opacity: .65;color: gray;
																					<%} else {%>
																						color:#ff9900;
																					<%}%>">
																			</i>
																	</td>
																	<td align="center">
																	<i class="fa fa-stethoscope fa-2x"												
																			onclick="openActivity3('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VDRCR_AGMT_NO.elementAt(i)%>',
																								'<%=VDRCR_AGMT_REV_NO.elementAt(i)%>','<%=VDRCR_CONT_NO.elementAt(i)%>',
																								'<%=VDRCR_CONT_REV_NO.elementAt(i)%>','<%=VDRCR_CONTRACT_TYPE.elementAt(i)%>',
																								'<%=VDRCR_PLANT_SEQ.elementAt(i)%>','<%=VDRCR_PERIOD_START_DT.elementAt(i)%>',
																								'<%=VDRCR_PERIOD_END_DT.elementAt(i)%>','<%=VDRCR_BU_PLANT_SEQ.elementAt(i)%>',
																								'<%=VDRCR_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VDRCR_FINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VDRCR_BU_STATE_TIN.elementAt(i)%>','<%=VDRCR_INVOICE_SEQ.elementAt(i)%>','<%=VDRCR_CARGO_NO.elementAt(i)%>',
																								'CHECK','<%=heading%>','<%=VDRCR_INV_FLAG.elementAt(i) %>','<%=VDRCR_FLAG.elementAt(i)%>','<%=VDRCR_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i)%>');"
																			style="<%if(VDRCR_INV_APPROVED_FLAG.elementAt(i).equals("Y") || VDRCR_INVOICE_EXIST.elementAt(i).equals("N")){ %>
																					pointer-events: none; opacity: .65; color: gray;
																					<%} else {%>
																						color:#ff3399;
																					<%}%>">
																	</i>
																	</td>
																	<td align="center">												
																		<i class="fa fa-flag fa-2x"
																			onclick="openActivity3('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VDRCR_AGMT_NO.elementAt(i)%>',
																								'<%=VDRCR_AGMT_REV_NO.elementAt(i)%>','<%=VDRCR_CONT_NO.elementAt(i)%>',
																								'<%=VDRCR_CONT_REV_NO.elementAt(i)%>','<%=VDRCR_CONTRACT_TYPE.elementAt(i)%>',
																								'<%=VDRCR_PLANT_SEQ.elementAt(i)%>','<%=VDRCR_PERIOD_START_DT.elementAt(i)%>',
																								'<%=VDRCR_PERIOD_END_DT.elementAt(i)%>','<%=VDRCR_BU_PLANT_SEQ.elementAt(i)%>',
																								'<%=VDRCR_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VDRCR_FINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VDRCR_BU_STATE_TIN.elementAt(i)%>','<%=VDRCR_INVOICE_SEQ.elementAt(i)%>','<%=VDRCR_CARGO_NO.elementAt(i)%>',
																								'APPROVE','<%=heading%>','<%=VDRCR_INV_FLAG.elementAt(i) %>','<%=VDRCR_FLAG.elementAt(i)%>','<%=VDRCR_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i)%>');"

																				<%if(heading.equals("LTCORA_INV_HEAD")) {%>
																			style="<%if(!VDRCR_PDF_INV_FLAG.elementAt(i).equals("") || !VDRCR_INV_CHECKED_FLAG.elementAt(i).equals("Y") || VDRCR_IS_IRN_GENERATED.elementAt(i).equals("Y")){ %>
																					pointer-events: none; opacity: .65; color: gray;
																					<%} else{%>
																					color:#00cc00;
																					<%}%>">
																			<%}else{ %>
																			style="<%if(!VDRCR_PDF_INV_FLAG.elementAt(i).equals("") || !VDRCR_INV_CHECKED_FLAG.elementAt(i).equals("Y")){ %>
																					pointer-events: none; opacity: .65; color: gray;
																					<%} else{%>
																					color:#00cc00;
																					<%}%>">
																			<%} %>
																		</i>				 										
																	</td>
																	<td align="center">
																		 <i class="fa <%if(!VDRCR_SAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
																		onclick="doGenXML('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VDRCR_INVOICE_NO.elementAt(i)%>',
																		 		'<%=VDRCR_FINANCIAL_YEAR.elementAt(i)%>','<%=VDRCR_INVOICE_SEQ.elementAt(i)%>',
																		 		'<%=VDRCR_CONTRACT_TYPE.elementAt(i)%>','DRCR',
																		 		'<%-- <%=VDRCR_INVOICE_TYPE.elementAt(i)%>',' --%><%=VDRCR_BU_STATE_TIN.elementAt(i)%>',
																		 		'<%=VDRCR_SAP_APPROVAL_FLAG.elementAt(i)%>','<%=VDRCR_AGMT_NO.elementAt(i)%>',
																		 		'<%=VDRCR_CONT_NO.elementAt(i)%>','<%=VDRCR_PDF_INV_FLAG.elementAt(i)%>','<%=heading%>');"
																		<%-- style="<%if(!VDRCR_INV_APPROVED_FLAG.elementAt(i).equals("Y")){ %> --%>
																		style="<%if(!VDRCR_SAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
																			pointer-events: none; opacity: .65; color: gray;
																			<%} else{%>
																				<%if(!VDRCR_SAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
																					color: orange;
																				<%} else{%>
																					color: brown;
																				<%}%>	
																			<%}%>">													
																		</i>
																	</td>
																	</td>
																		<%if(heading.equals("LTCORA_INV_HEAD")) {%>
																		<td align="center">
																			<i class="fa fa-qrcode fa-2x"
																				<%if(VDRCR_IS_IRN_GENERATED.elementAt(i).equals("Y")) {%>
																				title="IRN Generated!"
																				style="color: #0099cc;"
																				<%}else{ %>
																				title="Generation of IRN is Pending!"
																				style="opacity: .65; color: gray;"
																				<%} %>	
																			></i>
																		</td>
																		<%} %>
																	<td align="center">
																			<i class="fa fa-print fa-2x" title="<%=VDRCR_PDF_TYPE.elementAt(i)%>"
																				onclick="PrintPDF('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VDRCR_AGMT_NO.elementAt(i)%>',
																								'<%=VDRCR_AGMT_REV_NO.elementAt(i)%>','<%=VDRCR_CONT_NO.elementAt(i)%>',
																								'<%=VDRCR_CONT_REV_NO.elementAt(i)%>','<%=VDRCR_CONTRACT_TYPE.elementAt(i)%>',
																								'<%=VDRCR_PLANT_SEQ.elementAt(i)%>','<%=VDRCR_PERIOD_START_DT.elementAt(i)%>',
																								'<%=VDRCR_PERIOD_END_DT.elementAt(i)%>','<%=VDRCR_BU_PLANT_SEQ.elementAt(i)%>',
																								'<%=VDRCR_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VDRCR_FINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VDRCR_BU_STATE_TIN.elementAt(i)%>','<%=VDRCR_INVOICE_SEQ.elementAt(i)%>','<%=i%>',
																								'<%=VDRCR_CARGO_NO.elementAt(i)%>','<%=heading%>','<%=VDRCR_INV_FLAG.elementAt(i) %>',
																								'<%=VDRCR_FLAG.elementAt(i)%>','<%=VDRCR_SEQ.elementAt(i)%>','<%=VDRCR_CRITERIA.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i)%>');"
																				style="<%if(!VDRCR_INV_APPROVED_FLAG.elementAt(i).equals("Y") || !VDRCR_IS_IRN_GENERATED.elementAt(i).equals("Y") || VDRCR_PDF_TYPE.elementAt(i).equals(drcr_pdf_type)){ %>
																				pointer-events: none; opacity: .65; color: gray;
																				<%} else{%>
																				color:#800000;
																				<%}%>">
																			</i>
																			<input type="hidden" name="all_pdf_type" id="all_pdf_type<%=i%>" value="<%=VDRCR_PDF_TYPE.elementAt(i)%>">
																		</td>
																	<td align="center">
																		<i class="fa fa-file-pdf-o fa-2x"
																			onclick="openPdfFile('<%=file_url%><%=VDRCR_PDF_FILE_PATH.elementAt(i)%><%=VDRCR_PDF_FILE_NAME.elementAt(i)%>')"
																			style="<%if(VDRCR_PDF_FILE_NAME.elementAt(i).equals("")){ %>
																			pointer-events: none; opacity: .65; color: gray;
																			<%} else{%>
																			color:red;
																			<%}%>">
																		</i>
																	</td>
																	<td align="center">
																		<i class="fa fa-envelope-o fa-2x" title="<%=VDRCR_SIGN_PDF_TYPE.elementAt(i)%>"
																			onclick="openMailBody('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VDRCR_AGMT_NO.elementAt(i)%>',
																				'<%=VDRCR_AGMT_REV_NO.elementAt(i)%>','<%=VDRCR_CONT_NO.elementAt(i)%>',
																				'<%=VDRCR_CONT_REV_NO.elementAt(i)%>','<%=VDRCR_CONTRACT_TYPE.elementAt(i)%>',
																				'<%=VDRCR_PLANT_SEQ.elementAt(i)%>','<%=VDRCR_PERIOD_START_DT.elementAt(i)%>',
																				'<%=VDRCR_PERIOD_END_DT.elementAt(i)%>','<%=VDRCR_BU_PLANT_SEQ.elementAt(i)%>',
																				'<%=VDRCR_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VDRCR_FINANCIAL_YEAR.elementAt(i) %>',
																				'<%=VDRCR_BU_STATE_TIN.elementAt(i)%>','<%=VDRCR_INVOICE_SEQ.elementAt(i)%>',
																				'<%=i%>','<%=heading%>','<%=VDRCR_INV_FLAG.elementAt(i) %>','<%=VDRCR_FLAG.elementAt(i)%>',
																				'<%=VDRCR_SEQ.elementAt(i)%>','<%=VDRCR_CRITERIA.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i)%>');"
																			style="<%if(VDRCR_SIGN_PDF_TYPE.elementAt(i).equals("")){ %>
																			pointer-events: none; opacity: .65; color: gray;
																			<%} else{%>
																			color:blue;
																			<%}%>">
																		</i>
																		<input type="hidden" name="drcr_all_mail_pdf_type" id="drcr_all_mail_pdf_type<%=i%>" value="<%=VDRCR_SIGN_PDF_TYPE.elementAt(i)%>">
																	</td>
																</tr>
																		<%if(k==index){
																			i=i+1;
																			break;
																		} %>
																	<%} %>	
																<%}else{ %>
																<tr>
																	<td align="center" colspan="16"><%=utilmsg.infoMessage("<b>No Debit/Credit Note is Ready for Generate!</b>") %></td>
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

<%-- <input type="hidden" name="prev_billing_cycle" value="<%=billing_cycle%>"> --%>
<input type="hidden" name="sysdate" value="<%=sysdate%>">

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
</body>
</html>