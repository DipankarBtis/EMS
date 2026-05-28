<%@page import="com.etrm.fms.util.CommonVariable"%>
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
	var billing_cycle = document.forms[0].billing_cycle.value;
	var prev_billing_cycle = document.forms[0].prev_billing_cycle.value;
	
	var print_pdf_type = document.forms[0].print_pdf_type.value;
	var view_pdf_type = document.forms[0].view_pdf_type.value;
	var mail_pdf_type = document.forms[0].mail_pdf_type.value;
	
	var ff_print_pdf_type = document.forms[0].ff_print_pdf_type.value;
	var ff_view_pdf_type = document.forms[0].ff_view_pdf_type.value;
	var ff_mail_pdf_type = document.forms[0].ff_mail_pdf_type.value;
	
	var msg="";
	var flag = true;
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "../sales_invoice/frm_sales_invoice_preperation.jsp?&u="+u+"&print_pdf_type="+print_pdf_type+
				"&view_pdf_type="+view_pdf_type+"&mail_pdf_type="+mail_pdf_type+
				"&ff_print_pdf_type="+ff_print_pdf_type+"&ff_view_pdf_type="+ff_view_pdf_type+"&ff_mail_pdf_type="+ff_mail_pdf_type+
				"&month="+month+"&year="+year+"&billing_cycle="+billing_cycle;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg)
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
		plant_seq,period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,bu_st_cd,
		temp_period_st_dt,temp_period_end_dt,operation)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_generate_sales_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+"&operation="+operation+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&temp_period_start_dt="+temp_period_st_dt+"&temp_period_end_dt="+temp_period_end_dt+
			"&u="+u;
	
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

function openActivity3(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,bu_st_cd,inv_seq,activityFlag)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_view_chk_aprv_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+"&activityFlag="+activityFlag+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+
			"&u="+u;
	
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

function printPDF(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,
		period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,bu_st_cd,inv_seq,index)
{
	var print_access = document.forms[0].print_access.value;
	var print_pdf_type = document.forms[0].print_pdf_type.value;
	
	var all_pdf_type = document.getElementById("all_pdf_type"+index).value;
	
	if(print_pdf_type == "All")
	{
		print_pdf_type=all_pdf_type;
	}
	
	var u = document.forms[0].u.value;
	
	if(print_access=="N")
	{
		alert("You don't have Print Rights!");	
	}
	else
	{
		var url = "pdf_sell_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&print_pdf_type="+print_pdf_type;
	
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
}

function refershPar(sub_msg,msg_type)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	var prev_billing_cycle = document.forms[0].prev_billing_cycle.value;
	
	var print_pdf_type = document.forms[0].print_pdf_type.value;
	var view_pdf_type = document.forms[0].view_pdf_type.value;
	
	var msg="";
	var flag = true;
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "frm_sales_invoice_preperation.jsp?&u="+u+"&print_pdf_type="+print_pdf_type+
			"&view_pdf_type="+view_pdf_type+"&month="+month+"&year="+year+"&billing_cycle="+billing_cycle;
	
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

function openFreeFlowInv()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	if(billing_cycle=="0")
	{
		billing_cycle="1";
	}
	
	var activity_type="PREPARE";
	
	var u = document.forms[0].u.value;
	
	var url = "frm_f_flow_invoice.jsp?u="+u+
			"&billing_cycle="+billing_cycle+"&month="+month+"&year="+year+"&opration=INSERT&activity_type="+activity_type;
	
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

function openFreeFlowInv_Modify(counterparty_cd,address_type,period_st_dt,period_end_dt,bu,billing_cycle,
		mapping_id,inv_seq,inv_no,inv_type,finan_yr,bu_state_tin,activity_type)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var split=period_end_dt.split("/");
	month=split[1];
	year=split[2];
	
	var u = document.forms[0].u.value;
	
	var url = "frm_f_flow_invoice.jsp?u="+u+"&bu_state_tin="+bu_state_tin+
			"&counterparty_cd="+counterparty_cd+"&period_start_dt="+period_st_dt+"&period_end_dt="+period_end_dt+"&mapping_id="+mapping_id+
			"&address_type="+address_type+"&bu_unit="+bu+"&inv_no="+inv_no+"&inv_seq="+inv_seq+"&financial="+finan_yr+"&invoice_type="+inv_type+
			"&billing_cycle="+billing_cycle+"&month="+month+"&year="+year+"&opration=MODIFY&activity_type="+activity_type;
	
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
		mapping_id,inv_seq,inv_type,finan_yr,bu_state_tin,activityFlag)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_f_flow_view_chk_aprv_dtl.jsp?u="+u+"&bu_state_tin="+bu_state_tin+
			"&counterparty_cd="+counterparty_cd+"&period_start_dt="+period_st_dt+"&period_end_dt="+period_end_dt+"&mapping_id="+mapping_id+
			"&address_type="+address_type+"&bu_unit="+bu+"&invoice_seq="+inv_seq+"&financial_year="+finan_yr+"&invoice_type="+inv_type+
			"&billing_cycle="+billing_cycle+"&activityFlag="+activityFlag;
	
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
		invoice_no,invoice_seq,financial_year,bu_state_tin,cont_type,index)
{
	var print_access = document.forms[0].print_access.value;
	var ff_print_pdf_type = document.forms[0].ff_print_pdf_type.value;
	
	var all_pdf_type = document.getElementById("oth_all_pdf_type"+index).value;
	
	if(ff_print_pdf_type == "All")
	{
		ff_print_pdf_type=all_pdf_type;
	}
	
	var is_print="1";
	
	if(print_access=="N")
	{
		alert("You don't have Print Rights!");
	}
	else
	{
		var url = "pdf_f_flow_invoice.jsp?counterparty_cd="+counterparty_cd+"&is_print="+is_print+"&bu_state_tin="+bu_state_tin+
			"&invoice_type="+invoice_type+"&mapping_id="+mapping_id+"&address_type="+address_type+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+
			"&inv_no="+invoice_no+"&inv_seq="+invoice_seq+"&financial="+financial_year+"&ff_print_pdf_type="+ff_print_pdf_type+"&contract_type="+cont_type;
		
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

function openMailBody(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,
		period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,bu_st_cd,inv_seq,index)
{
	var mail_pdf_type = document.forms[0].mail_pdf_type.value;
	
	var all_mail_pdf_type = document.getElementById("all_mail_pdf_type"+index).value;
	
	if(mail_pdf_type == "All")
	{
		mail_pdf_type=all_mail_pdf_type;
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_sign_pdf_mail.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&mail_pdf_type="+mail_pdf_type+
			"&mail_inv_type=S"+
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

function openffMailBody(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,
		period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,bu_st_cd,inv_seq,index,inv_type)
{
	var ff_mail_pdf_type = document.forms[0].ff_mail_pdf_type.value;
	
	var oth_all_mail_pdf_type = document.getElementById("oth_all_mail_pdf_type"+index).value;
	
	if(ff_mail_pdf_type == "All")
	{
		ff_mail_pdf_type=oth_all_mail_pdf_type;
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_sign_pdf_mail.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&mail_pdf_type="+ff_mail_pdf_type+
			"&mail_inv_type=F&invoice_type="+inv_type+
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
	
	var url = "../accounting/rpt_view_sales_invoice_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
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
</script>
</head>
<jsp:useBean class="com.etrm.fms.sales_invoice.DataBean_Sales_Invoice" id="sales_inv" scope="request"></jsp:useBean>
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

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String print_pdf_type=request.getParameter("print_pdf_type")==null?"O":request.getParameter("print_pdf_type");
String view_pdf_type=request.getParameter("view_pdf_type")==null?"O":request.getParameter("view_pdf_type");
String mail_pdf_type=request.getParameter("mail_pdf_type")==null?"O":request.getParameter("mail_pdf_type");
String ff_print_pdf_type=request.getParameter("ff_print_pdf_type")==null?"O":request.getParameter("ff_print_pdf_type");
String ff_view_pdf_type=request.getParameter("ff_view_pdf_type")==null?"O":request.getParameter("ff_view_pdf_type");
String ff_mail_pdf_type=request.getParameter("ff_mail_pdf_type")==null?"O":request.getParameter("ff_mail_pdf_type");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
if(billing_cycle.equals(""))
{
	if(Integer.parseInt(date_num) > 15)
	{
		billing_cycle="2";
	}
	else
	{
		billing_cycle="1";
	}
}

if(month.length() == 1)
{
	month="0"+month; 
}

sales_inv.setCallFlag("SALES_INVOICE_PREPARATION_LIST");
sales_inv.setComp_cd(owner_cd);
sales_inv.setMonth(month);
sales_inv.setYear(year);
sales_inv.setBilling_cycle(billing_cycle);
sales_inv.setPrint_pdf_type(print_pdf_type);
sales_inv.setView_pdf_type(view_pdf_type);
sales_inv.setMail_pdf_type(mail_pdf_type);
sales_inv.setFf_print_pdf_type(ff_print_pdf_type);
sales_inv.setFf_view_pdf_type(ff_view_pdf_type);
sales_inv.setFf_mail_pdf_type(ff_mail_pdf_type);
sales_inv.init();

Vector VCOUNTERPTY_CD = sales_inv.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = sales_inv.getVCOUNTERPTY_ABBR();
Vector VCONT_NO = sales_inv.getVCONT_NO();
Vector VCONT_REV_NO = sales_inv.getVCONT_REV_NO();
Vector VAGMT_NO = sales_inv.getVAGMT_NO();
Vector VAGMT_REV_NO = sales_inv.getVAGMT_REV_NO();
Vector VSTART_DT = sales_inv.getVSTART_DT();
Vector VEND_DT = sales_inv.getVEND_DT();
Vector VCONT_NAME = sales_inv.getVCONT_NAME();
Vector VCONT_REF_NO = sales_inv.getVCONT_REF_NO();
Vector VCONTRACT_TYPE = sales_inv.getVCONTRACT_TYPE();
Vector VPLANT_SEQ = sales_inv.getVPLANT_SEQ();
Vector VPLANT_ABBR = sales_inv.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = sales_inv.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = sales_inv.getVBU_PLANT_ABBR();
Vector VDEAL_NO = sales_inv.getVDEAL_NO();
Vector VPERIOD_START_DT = sales_inv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = sales_inv.getVPERIOD_END_DT();
Vector VINVOICE_NO = sales_inv.getVINVOICE_NO();
Vector VSTATUS = sales_inv.getVSTATUS();
Vector VBILLING_FREQ_FLAG = sales_inv.getVBILLING_FREQ_FLAG();
Vector VBILLING_FREQ_NM = sales_inv.getVBILLING_FREQ_NM();
Vector VINV_CHECKED_FLAG = sales_inv.getVINV_CHECKED_FLAG();
Vector VINV_APPROVED_FLAG = sales_inv.getVINV_APPROVED_FLAG();
Vector VBU_STATE_TIN = sales_inv.getVBU_STATE_TIN();
Vector VFINANCIAL_YEAR = sales_inv.getVFINANCIAL_YEAR();
Vector VINVOICE_EXIST = sales_inv.getVINVOICE_EXIST();
Vector VINVOICE_SEQ=sales_inv.getVINVOICE_SEQ();
Vector VDIFF_COLOR=sales_inv.getVDIFF_COLOR();
Vector VPDF_INV_FLAG=sales_inv.getVPDF_INV_FLAG();
Vector VPDF_TYPE=sales_inv.getVPDF_TYPE();
Vector VPDF_FILE_NAME=sales_inv.getVPDF_FILE_NAME();
Vector VPDF_FILE_PATH=sales_inv.getVPDF_FILE_PATH();
Vector VPDF_SIGNED_FLAG=sales_inv.getVPDF_SIGNED_FLAG();
Vector VSIGN_PDF_TYPE=sales_inv.getVSIGN_PDF_TYPE();
Vector VAGMT_BASE = sales_inv.getVAGMT_BASE();
Vector VSAP_APPROVAL_FLAG = sales_inv.getVSAP_APPROVAL_FLAG();

Vector VTEMP_PERIOD_START_DT = sales_inv.getVTEMP_PERIOD_START_DT();
Vector VTEMP_PERIOD_END_DT = sales_inv.getVTEMP_PERIOD_END_DT();
Vector VALLOC_QTY = sales_inv.getVALLOC_QTY();

//FOR F FLOW
Vector VOTH_COUNTERPTY_CD = sales_inv.getVOTH_COUNTERPTY_CD();
Vector VOTH_COUNTERPTY_ABBR = sales_inv.getVOTH_COUNTERPTY_ABBR();
Vector VOTH_CONT_NO = sales_inv.getVOTH_CONT_NO();
Vector VOTH_CONT_REV_NO = sales_inv.getVOTH_CONT_REV_NO();
Vector VOTH_AGMT_NO = sales_inv.getVOTH_AGMT_NO();
Vector VOTH_AGMT_REV_NO = sales_inv.getVOTH_AGMT_REV_NO();
Vector VOTH_START_DT = sales_inv.getVOTH_START_DT();
Vector VOTH_END_DT = sales_inv.getVOTH_END_DT();
Vector VOTH_CONT_NAME = sales_inv.getVOTH_CONT_NAME();
Vector VOTH_CONT_REF_NO = sales_inv.getVOTH_CONT_REF_NO();
Vector VOTH_CONTRACT_TYPE = sales_inv.getVOTH_CONTRACT_TYPE();
Vector VOTH_PLANT_SEQ = sales_inv.getVOTH_PLANT_SEQ();
Vector VOTH_PLANT_ABBR = sales_inv.getVOTH_PLANT_ABBR();
Vector VOTH_BU_PLANT_SEQ = sales_inv.getVOTH_BU_PLANT_SEQ();
Vector VOTH_BU_PLANT_ABBR = sales_inv.getVOTH_BU_PLANT_ABBR();
Vector VOTH_DEAL_NO = sales_inv.getVOTH_DEAL_NO();
Vector VOTH_PERIOD_START_DT = sales_inv.getVOTH_PERIOD_START_DT();
Vector VOTH_PERIOD_END_DT = sales_inv.getVOTH_PERIOD_END_DT();
Vector VOTH_INVOICE_NO = sales_inv.getVOTH_INVOICE_NO();
Vector VOTH_STATUS = sales_inv.getVOTH_STATUS();
Vector VOTH_BILLING_FREQ_FLAG = sales_inv.getVOTH_BILLING_FREQ_FLAG();
Vector VOTH_BILLING_FREQ_NM = sales_inv.getVOTH_BILLING_FREQ_NM();
Vector VOTH_INV_CHECKED_FLAG = sales_inv.getVOTH_INV_CHECKED_FLAG();
Vector VOTH_INV_APPROVED_FLAG = sales_inv.getVOTH_INV_APPROVED_FLAG();
Vector VOTH_BU_STATE_TIN = sales_inv.getVOTH_BU_STATE_TIN();
Vector VOTH_FINANCIAL_YEAR = sales_inv.getVOTH_FINANCIAL_YEAR();
Vector VOTH_INVOICE_EXIST = sales_inv.getVOTH_INVOICE_EXIST();
Vector VOTH_INVOICE_SEQ=sales_inv.getVOTH_INVOICE_SEQ();
Vector VOTH_PDF_INV_FLAG=sales_inv.getVOTH_PDF_INV_FLAG();
Vector VOTH_PDF_TYPE=sales_inv.getVOTH_PDF_TYPE();
Vector VOTH_PDF_FILE_NAME=sales_inv.getVOTH_PDF_FILE_NAME();
Vector VOTH_PDF_FILE_PATH=sales_inv.getVOTH_PDF_FILE_PATH();
Vector VOTH_PDF_SIGNED_FLAG=sales_inv.getVOTH_PDF_SIGNED_FLAG();
Vector VOTH_SIGN_PDF_TYPE=sales_inv.getVOTH_SIGN_PDF_TYPE();
Vector VOTH_INVOICE_TYPE=sales_inv.getVOTH_INVOICE_TYPE();
Vector VMAPPING_ID=sales_inv.getVMAPPING_ID();
Vector VOTH_SAP_APPROVAL_FLAG = sales_inv.getVOTH_SAP_APPROVAL_FLAG();

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
					    	Sales Invoice Generation
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
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Sales Invoice Generation</label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th>Customer</th>
											<th>Contract No<br>[Contract/Trade Ref#]</th>
											<th>Contract Period</th>
											<th>Plant</th>
											<th>Business Unit</th>
											<th>Billing Cycle</th>
											<th>Billing Period</th>
											<th>Invoice#</th>
											<th>Prepare/<br>View Invoice</th>
											<th>Modify<br>Invoice</th>
											<th>Check<br>Invoice</th>
											<th>Approve<br>Invoice</th>
											<th>SAP<br>XML</th>
											<th>Print PDF
												<br>
												<select class="form-select form-select-sm" name="print_pdf_type" style="width:80px;" onchange="refresh();">
													<option value="O">Original</option>
													<option value="D">Duplicate</option>
													<option value="T">Triplicate</option>
													<option value="All">All</option>
												</select>
												<script>document.forms[0].print_pdf_type.value="<%=print_pdf_type%>"</script>
											</th>
											<th>View PDF
												<br>
												<select class="form-select form-select-sm" name="view_pdf_type" style="width:80px;" onchange="refresh();">
													<option value="O">Original</option>
													<option value="D">Duplicate</option>
													<option value="T">Triplicate</option>
													<option value="All">All</option>
												</select>
												<script>document.forms[0].view_pdf_type.value="<%=view_pdf_type%>"</script>
											</th>
											<th>Send Mail
												<br>
												<select class="form-select form-select-sm" name="mail_pdf_type" style="width:80px;" onchange="refresh();">
													<option value="O">Original</option>
													<option value="D">Duplicate</option>
													<option value="T">Triplicate</option>
													<option value="All">All</option>
												</select>
												<script>document.forms[0].mail_pdf_type.value="<%=mail_pdf_type%>"</script>
											</th>
										</tr>
									</thead>
									<tbody>
									<%if(VCOUNTERPTY_CD.size() > 0){ %>
										<%for(int i=0; i<VCOUNTERPTY_CD.size(); i++){ %>
										<tr>
											<td align="center">
												<%=VCOUNTERPTY_ABBR.elementAt(i)%>
											</td>
											<td align="center" title="Agmt Base : <%=VAGMT_BASE.elementAt(i)%>">
												<font color="blue"><%=VDEAL_NO.elementAt(i)%></font>
												<%if(VAGMT_BASE.elementAt(i).equals("D")){ %>
												<font style="background:#a6ff4d;">[DLV]</font>
												<%} %>
												<br>[<%=VCONT_REF_NO.elementAt(i)%>]
											</td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
											<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
											<td align="center">
												<span 
		    									<%if(VBILLING_FREQ_NM.elementAt(i).equals("1st-Fortnight")){ %>
		    										class="alert alert-info"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("2nd-Fortnight")){ %>
		    										class="alert alert-warning"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("1st-Weekly")){ %>
		    										class="alert" style="background:#eeccff;color: #660099;"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("2nd-Weekly")){ %>
		    										class="alert alert-dark"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("3rd-Weekly")){ %>
		    										class="alert alert-success"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("4th-Weekly")){ %>
		    										class="alert alert-danger"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("5th-Weekly")){ %>
		    										class="alert" style="background:#e6ccff;color:#330066;"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("Monthly")){ %>
		    										class="alert alert-primary"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("Other")){ %>
		    										class="alert" style="background:#b3ffb3;color: #008000;"
		    									<%} %>
		    									><b><%=VBILLING_FREQ_NM.elementAt(i)%></b></span>																								
											</td>
											<td align="center" title="Alloc MMBTU : <%=VALLOC_QTY.elementAt(i)%>">
											<font <%if(!VDIFF_COLOR.elementAt(i).equals("")){%>color="blue"<%} %>><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></font>
											<%-- <%if(!VDIFF_COLOR.elementAt(i).equals("")){%>
											<br><font color="<%=VDIFF_COLOR.elementAt(i)%>">[<%=VTEMP_PERIOD_START_DT.elementAt(i)%> - <%=VTEMP_PERIOD_END_DT.elementAt(i)%>]</font>
											<%} %> --%>
											</td>
											<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
											<td align="center">
												<i class="fa <%if(VINVOICE_SEQ.elementAt(i).equals("")){ %>fa-cogs<%}else{%>fa-eye<%} %> fa-2x" 
												onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																	'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																	'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																	'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																	'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																	'<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VTEMP_PERIOD_START_DT.elementAt(i)%>',
																	'<%=VTEMP_PERIOD_END_DT.elementAt(i)%>','INSERT');"
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
																	'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																	'<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VTEMP_PERIOD_START_DT.elementAt(i)%>',
																	'<%=VTEMP_PERIOD_END_DT.elementAt(i)%>','MODIFY');"
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
																	'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																	'<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','CHECK');"
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
																	'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																	'<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','APPROVE');"
												style="<%if(!VPDF_INV_FLAG.elementAt(i).equals("") || !VINV_CHECKED_FLAG.elementAt(i).equals("Y")){ %>
														pointer-events: none; opacity: .65; color: gray;
														<%} else{%>
														color:#00cc00;
														<%}%>">
												</i>											
											</td>
											<td align="center">
												<i class="fa <%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
												onclick="doGenXML('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
												 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
												 'SG','','<%=VBU_STATE_TIN.elementAt(i)%>','<%=VSAP_APPROVAL_FLAG.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
												 '<%=VCONT_NO.elementAt(i)%>','<%=VPDF_INV_FLAG.elementAt(i)%>');"
												style="<%if(!VINV_APPROVED_FLAG.elementAt(i).equals("Y")){ %>
													pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
														<%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
															color: orange;
														<%} else{%>
															color: brown;
														<%}%>		
													<%}%>">													
												</i>
											</td>
											<td align="center">
												<i class="fa fa-print fa-2x" title="<%=VPDF_TYPE.elementAt(i)%>"
													onclick="printPDF('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																	'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																	'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																	'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																	'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																	'<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=i%>');"
													style="<%if(!VINV_APPROVED_FLAG.elementAt(i).equals("Y") || VPDF_TYPE.elementAt(i).equals(print_pdf_type)){ %>
													pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
													color:#800000;
													<%}%>">
												</i>
												<input type="hidden" name="all_pdf_type" id="all_pdf_type<%=i%>" value="<%=VPDF_TYPE.elementAt(i)%>">
											</td>
											<td align="center">
												<i class="fa fa-file-pdf-o fa-2x"
													<%if(view_pdf_type.equals("All")){ %>
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
												<i class="fa fa-envelope-o fa-2x" title="<%=VSIGN_PDF_TYPE.elementAt(i)%>"
													onclick="openMailBody('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
														'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
														'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
														'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
														'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
														'<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
														'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=i%>');"
													style="<%if(VSIGN_PDF_TYPE.elementAt(i).equals("")){ %>
													pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
													color:blue;
													<%}%>">
												</i>
												<input type="hidden" name="all_mail_pdf_type" id="all_mail_pdf_type<%=i%>" value="<%=VSIGN_PDF_TYPE.elementAt(i)%>">
											</td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td align="center" colspan="16"><%=utilmsg.infoMessage("<b>No Invoice is Ready for Generate!</b>") %></td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>	
					</div>	
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Sales Free Flow Invoice Generation</label>
					</div>	
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
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th>Customer</th>
											<th>Contract No<br>[Contract/Trade Ref#]</th>
											<th>Contract Period</th>
											<th>Plant</th>
											<th>Business Unit</th>
											<th>Billing Cycle</th>
											<th>Billing Period</th>
											<th>Invoice#</th>
											<th>View<br>Invoice</th>
											<th>Modify<br>Invoice</th>
											<th>Check<br>Invoice</th>
											<th>Approve<br>Invoice</th>
											<th>SAP<br>Xml</th>
											<th>Print PDF
												<br>
												<select class="form-select form-select-sm" name="ff_print_pdf_type" style="width:80px;" onchange="refresh();">
													<option value="O">Original</option>
													<option value="D">Duplicate</option>
													<option value="T">Triplicate</option>
													<option value="All">All</option>
												</select>
												<script>document.forms[0].ff_print_pdf_type.value="<%=ff_print_pdf_type%>"</script>
											</th>
											<th>View PDF
												<br>
												<select class="form-select form-select-sm" name="ff_view_pdf_type" style="width:80px;" onchange="refresh();">
													<option value="O">Original</option>
													<option value="D">Duplicate</option>
													<option value="T">Triplicate</option>
													<option value="All">All</option>
												</select>
												<script>document.forms[0].ff_view_pdf_type.value="<%=ff_view_pdf_type%>"</script>
											</th>
											<th>Send Mail
												<br>
												<select class="form-select form-select-sm" name="ff_mail_pdf_type" style="width:80px;" onchange="refresh();">
													<option value="O">Original</option>
													<option value="D">Duplicate</option>
													<option value="T">Triplicate</option>
													<option value="All">All</option>
												</select>
												<script>document.forms[0].ff_mail_pdf_type.value="<%=ff_mail_pdf_type%>"</script>
											</th>
										</tr>
									</thead>
									<tbody>
									<%if(VOTH_COUNTERPTY_CD.size() > 0){ %>
										<%for(int i=0; i<VOTH_COUNTERPTY_CD.size(); i++){ %>
										<tr>
											<td align="center">
												<%=VOTH_COUNTERPTY_ABBR.elementAt(i)%>
											</td>
											<td align="center">
												<font color="blue"><%=VOTH_DEAL_NO.elementAt(i)%></font><br>[<%=VOTH_CONT_REF_NO.elementAt(i)%>]
											</td>
											<td align="center"><%=VOTH_START_DT.elementAt(i)%> - <%=VOTH_END_DT.elementAt(i)%></td>
											<td align="center"><%=VOTH_PLANT_ABBR.elementAt(i)%></td>
											<td align="center"><%=VOTH_BU_PLANT_ABBR.elementAt(i)%></td>
											<td align="center">
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
											</td>
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
													'<%=VOTH_BU_STATE_TIN.elementAt(i)%>','');"																							
												style="color:black;">
												</i>
											</td>
											<td align="center">
												<i class="fa fa-pencil fa-2x"  
												onclick="openFreeFlowInv_Modify('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_PERIOD_START_DT.elementAt(i)%>','<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>',
													'<%=VOTH_INVOICE_NO.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>',
													'<%=VOTH_BU_STATE_TIN.elementAt(i)%>','PREPARE');"
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
													'<%=VOTH_BU_STATE_TIN.elementAt(i)%>','CHECK');"												
												style="<%if(VOTH_INV_APPROVED_FLAG.elementAt(i).equals("Y") || VOTH_INVOICE_EXIST.elementAt(i).equals("N")){ %>
														pointer-events: none; opacity: .65; color: gray;
														<%} else {%>
															color:#ff3399;
														<%}%>">
												</i>
											</td>
											<td align="center">												
												<i class="fa fa-flag fa-2x"
												onclick="openFreeFlowInv_Approval('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_PERIOD_START_DT.elementAt(i)%>','<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>',
													'<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>',
													'<%=VOTH_BU_STATE_TIN.elementAt(i)%>','APPROVE');"
												style="<%if(!VOTH_PDF_INV_FLAG.elementAt(i).equals("") || !VOTH_INV_CHECKED_FLAG.elementAt(i).equals("Y")){ %>
														pointer-events: none; opacity: .65; color: gray;
														<%} else{%>
														color:#00cc00;
														<%}%>">
												</i>											
											</td>
											<td align="center">
												<i class="fa <%if(!VOTH_SAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
												onclick="doGenXML('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_INVOICE_NO.elementAt(i)%>',
												 		'<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>',
												 		'<%=VOTH_CONTRACT_TYPE.elementAt(i)%>','FFLOW',
												 		'<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_BU_STATE_TIN.elementAt(i)%>',
												 		'<%=VOTH_SAP_APPROVAL_FLAG.elementAt(i)%>','<%=VOTH_AGMT_NO.elementAt(i)%>',
												 		'<%=VOTH_CONT_NO.elementAt(i)%>','<%=VOTH_PDF_INV_FLAG.elementAt(i)%>');"
												style="<%if(!VOTH_INV_APPROVED_FLAG.elementAt(i).equals("Y")){ %>
													pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
														<%if(!VOTH_SAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
															color: orange;
														<%} else{%>
															color: brown;
														<%}%>	
													<%}%>">													
												</i>
											</td>
											<td align="center">
												<i class="fa fa-print fa-2x" title="<%=VOTH_PDF_TYPE.elementAt(i)%>" 
													onclick="fflowPrintPDF('<%=VOTH_BU_PLANT_SEQ.elementAt(i) %>','<%=VOTH_COUNTERPTY_CD.elementAt(i) %>','<%=VOTH_BILLING_FREQ_FLAG.elementAt(i) %>',
														'<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_PLANT_SEQ.elementAt(i) %>','<%=VOTH_INVOICE_TYPE.elementAt(i) %>',
														'<%=VOTH_INVOICE_NO.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i) %>',
														'<%=VOTH_BU_STATE_TIN.elementAt(i)%>','<%=VOTH_CONTRACT_TYPE.elementAt(i)%>','<%=i%>');"
												
													style="<%if(!VOTH_INV_APPROVED_FLAG.elementAt(i).equals("Y") || VOTH_PDF_TYPE.elementAt(i).equals(ff_print_pdf_type)){ %>
													pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
													color:#800000;
													<%}%>">
												</i>
												<input type="hidden" name="oth_all_pdf_type" id="oth_all_pdf_type<%=i%>" value="<%=VOTH_PDF_TYPE.elementAt(i)%>">
											</td>
											<td align="center">
												<i class="fa fa-file-pdf-o fa-2x"
													<%if(ff_view_pdf_type.equals("All")){ %>
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
														'<%=i%>','<%=VOTH_INVOICE_TYPE.elementAt(i) %>');"
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
											<td align="center" colspan="16"><%=utilmsg.infoMessage("<b>No Invoice is Ready for Generate!</b>") %></td>
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

<input type="hidden" name="prev_billing_cycle" value="<%=billing_cycle%>">
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

</form>
</body>
</html>