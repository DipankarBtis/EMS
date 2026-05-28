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
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var pdf_type="All";
	var ff_pdf_type="All";
	if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	}
	if(document.forms[0].ff_pdf_type!=null && document.forms[0].ff_pdf_type!=undefined)
	{
		ff_pdf_type = document.forms[0].ff_pdf_type.value;
	}
	
	var msg="";
	var flag = true;
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "../dlng/frm_dlng_lp_preparation.jsp?&u="+u+"&pdf_type="+pdf_type+
				"&ff_pdf_type="+ff_pdf_type+"&month="+month+"&year="+year+"&accroid="+accroid;
	
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
		plant_seq,period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,operation,accroid,inv_flag,
		ori_inv_seq,ori_fin_yr,lp_inv_flag,ori_invoice_no,cont_start_dt,cont_end_dt,truck_cd,truck_trans_cd,truck_no,mapping_id,sell_cont_map)
{
	var u = document.forms[0].u.value;

	var url = "frm_dlng_lp_generate.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+"&operation="+operation+
			"&exist_financial_year="+ori_fin_yr+"&lp_financial_year="+fin_yr+
			"&bu_state_tin="+bu_st_cd+"&sell_cont_map="+sell_cont_map+
			"&ori_invoice_seq="+ori_inv_seq+
			"&ori_invoice_no="+ori_invoice_no+
			"&truck_cd="+truck_cd+"&truck_trans_cd="+truck_trans_cd+"&truck_no="+truck_no+"&mapping_id="+mapping_id+
			"&accroid="+accroid+"&inv_flag="+inv_flag+"&lp_inv_flag="+lp_inv_flag+"&cont_start_dt="+cont_start_dt+"&cont_end_dt="+cont_end_dt+"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare DLNG Late Payment Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare DLNG Late Payment Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}
function openActivity3(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,
		activityFlag,accroid,inv_flag,ori_inv_flag,exist_fin_yr,ori_inv_seq,cont_start_dt,cont_end_dt,ori_inv_no,truck_cd,truck_trans_cd,truck_no,mapping_id)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_view_lp_chk_aprv_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+"&activityFlag="+activityFlag+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&accroid="+accroid+
			"&inv_flag="+inv_flag+"&ori_inv_flag="+ori_inv_flag+"&exist_fin_yr="+exist_fin_yr+"&ori_inv_seq="+ori_inv_seq+
			"&cont_start_dt="+cont_start_dt+"&cont_end_dt="+cont_end_dt+"&ori_inv_no="+ori_inv_no+
			"&truck_cd="+truck_cd+"&truck_trans_cd="+truck_trans_cd+"&truck_no="+truck_no+"&mapping_id="+mapping_id+"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare DLNG Late Payment Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare DLNG Late Payment Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function printPDF(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,index,accroid,inv_flag,ori_inv_flag,
		exist_fin_yr,ori_inv_seq,cont_start_dt,cont_end_dt,ori_inv_no,truck_cd,truck_trans_cd,truck_no,mapping_id)
{
	var print_access = document.forms[0].print_access.value;
	
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
		var url = "pdf_dlng_lp_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&accroid="+accroid+
			"&inv_flag="+inv_flag+"&ori_inv_flag="+ori_inv_flag+"&exist_fin_yr="+exist_fin_yr+"&ori_inv_seq="+ori_inv_seq+
			"&cont_start_dt="+cont_start_dt+"&cont_end_dt="+cont_end_dt+"&ori_inv_no="+ori_inv_no+
			"&truck_cd="+truck_cd+"&truck_trans_cd="+truck_trans_cd+"&truck_no="+truck_no+"&mapping_id="+mapping_id+
			"&print_pdf_type="+pdf_type+"&u="+u;
		
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"PDF DLNG Late Payment Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"PDF DLNG Late Payment Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
	}
}

function refershPar(sub_msg,msg_type,accroid)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var pdf_type="O";
	var ff_pdf_type="O";
	if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	}
	if(document.forms[0].ff_pdf_type!=null && document.forms[0].ff_pdf_type!=undefined)
	{
		ff_pdf_type = document.forms[0].ff_pdf_type.value;
	}
	
	var msg="";
	var flag = true;
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "frm_dlng_lp_preparation.jsp?&u="+u+"&pdf_type="+pdf_type+
			"&ff_pdf_type="+ff_pdf_type+"&month="+month+"&year="+year+"&accroid="+accroid+"&msg="+sub_msg+"&msg_type="+msg_type;
	
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
	var url = "rpt_view_all_pdf.jsp?financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&flag=LP";
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"PDF DLNG LP Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"PDF DLNG LP Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
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
		period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,index,accroid,inv_flag)
{
	var pdf_type="";
	if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
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
			"&mail_inv_type=LP&inv_flag="+inv_flag+
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
	
	var url = "../dlng/rpt_view_sales_invoice_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
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
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="dlng_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String sys_date_num = "0"; 
/* int currentYear = 0;
int currentMonth = 0;
if(!sysdate.equals(""))
{
	String[] sys_temp = sysdate.split("/");
	sys_date_num=sys_temp[0];
}

String prvMonthDate="";
if(Integer.parseInt(sys_date_num) <= 15)
{
	prvMonthDate=utildate.getFirstDateOfPreviousMonth(sysdate);
}
else
{
	prvMonthDate=sysdate;
}

String date_num = "0"; 
if(!prvMonthDate.equals(""))
{
	String[] temp = prvMonthDate.split("/");
	date_num=temp[0];
	currentMonth=Integer.parseInt(temp[1]);
	currentYear=Integer.parseInt(temp[2]);
} */
int currentYear = utildate.getCurrentYear();
String currentMonth = ""+utildate.getCurrentMonth();
int filter_start_year = CommonVariable.filter_start_year;
if((currentMonth).length()<2)
{
	currentMonth = "0"+currentMonth;
}
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String pdf_type=request.getParameter("pdf_type")==null?"All":request.getParameter("pdf_type");
String ff_pdf_type=request.getParameter("ff_pdf_type")==null?"All":request.getParameter("ff_pdf_type");

dlng_inv.setCallFlag("LP_INVOICE_LIST");
dlng_inv.setComp_cd(owner_cd);
dlng_inv.setMonth(month);
dlng_inv.setYear(year);
dlng_inv.setPrint_pdf_type(pdf_type);
dlng_inv.setView_pdf_type(pdf_type);
dlng_inv.setMail_pdf_type(pdf_type);
dlng_inv.setFf_print_pdf_type(ff_pdf_type);
dlng_inv.setFf_view_pdf_type(ff_pdf_type);
dlng_inv.setFf_mail_pdf_type(ff_pdf_type);
dlng_inv.init();

Vector VINVOICE_LIST_ABBR = dlng_inv.getVINVOICE_LIST_ABBR();
Vector VINVOICE_LIST_NAME = dlng_inv.getVINVOICE_LIST_NAME();
Vector VINDEX = dlng_inv.getVINDEX();

Vector VCOUNTERPTY_CD = dlng_inv.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = dlng_inv.getVCOUNTERPTY_ABBR();
Vector VCONT_NO = dlng_inv.getVCONT_NO();
Vector VCONT_REV_NO = dlng_inv.getVCONT_REV_NO();
Vector VCARGO_NO = dlng_inv.getVCARGO_NO();
Vector VAGMT_NO = dlng_inv.getVAGMT_NO();
Vector VAGMT_REV_NO = dlng_inv.getVAGMT_REV_NO();
Vector VSTART_DT = dlng_inv.getVSTART_DT();
Vector VEND_DT = dlng_inv.getVEND_DT();
// Vector VCONT_NAME = dlng_inv.getVCONT_NAME();
Vector VCONT_REF_NO = dlng_inv.getVCONT_REF_NO();
Vector VCONTRACT_TYPE = dlng_inv.getVCONTRACT_TYPE();
Vector VPLANT_SEQ = dlng_inv.getVPLANT_SEQ();
Vector VPLANT_ABBR = dlng_inv.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = dlng_inv.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = dlng_inv.getVBU_PLANT_ABBR();
Vector VDEAL_NO = dlng_inv.getVDEAL_NO();
Vector VPERIOD_START_DT = dlng_inv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = dlng_inv.getVPERIOD_END_DT();
Vector VINVOICE_NO = dlng_inv.getVINVOICE_NO();
Vector VINVOICE_DT = dlng_inv.getVINVOICE_DT();
Vector VSTATUS = dlng_inv.getVSTATUS();
Vector VBILLING_FREQ_FLAG = dlng_inv.getVBILLING_FREQ_FLAG();
Vector VBILLING_FREQ_NM = dlng_inv.getVBILLING_FREQ_NM();
Vector VINV_CHECKED_FLAG = dlng_inv.getVINV_CHECKED_FLAG();
Vector VINV_APPROVED_FLAG = dlng_inv.getVINV_APPROVED_FLAG();
Vector VBU_STATE_TIN = dlng_inv.getVBU_STATE_TIN();
Vector VFINANCIAL_YEAR = dlng_inv.getVFINANCIAL_YEAR();
Vector VORI_FINANCIAL_YEAR = dlng_inv.getVORI_FINANCIAL_YEAR();
Vector VINVOICE_EXIST = dlng_inv.getVINVOICE_EXIST();
Vector VINVOICE_SEQ=dlng_inv.getVINVOICE_SEQ();
Vector VDIFF_COLOR=dlng_inv.getVDIFF_COLOR();
Vector VPDF_INV_FLAG=dlng_inv.getVPDF_INV_FLAG();
Vector VPDF_TYPE=dlng_inv.getVPDF_TYPE();
Vector VPDF_FILE_NAME=dlng_inv.getVPDF_FILE_NAME();
Vector VPDF_FILE_PATH=dlng_inv.getVPDF_FILE_PATH();
Vector VPDF_SIGNED_FLAG=dlng_inv.getVPDF_SIGNED_FLAG();
Vector VSIGN_PDF_TYPE=dlng_inv.getVSIGN_PDF_TYPE();
Vector VAGMT_BASE = dlng_inv.getVAGMT_BASE();
Vector VSAP_APPROVAL_FLAG = dlng_inv.getVSAP_APPROVAL_FLAG();
Vector VINV_FLAG = dlng_inv.getVINV_FLAG();
Vector VTRUCK_CD = dlng_inv.getVTRUCK_CD();
Vector VTRUCK_NO = dlng_inv.getVTRUCK_NO();
Vector VTRUCK_TRANS_CD = dlng_inv.getVTRUCK_TRANS_CD();
Vector VMAPPING_ID = dlng_inv.getVMAPPING_ID();
Vector VSELL_CONT_MAP = dlng_inv.getVSELL_CONT_MAP();

Vector VORI_INV_FLAG = dlng_inv.getVORI_INV_FLAG();

// System.out.println("VORI_FINANCIAL_YEAR======"+VORI_FINANCIAL_YEAR);
// System.out.println("VFINANCIAL_YEAR======"+VFINANCIAL_YEAR);
// System.out.println("VORI_INV_FLAG======"+VORI_INV_FLAG); 
// System.out.println("VINV_FLAG======"+VINV_FLAG); 

Vector VALLOC_QTY = dlng_inv.getVALLOC_QTY();
Vector VIS_IRN_GENERATED = dlng_inv.getVIS_IRN_GENERATED();

Vector VORI_INV_SEQ = dlng_inv.getVORI_INV_SEQ();
Vector VORI_INVOICE_NO = dlng_inv.getVORI_INVOICE_NO();
Vector VORI_INVOICE_PAYRECV_DT = dlng_inv.getVORI_INVOICE_PAYRECV_DT();
Vector VORI_INVOICE_DUE_DT = dlng_inv.getVORI_INVOICE_DUE_DT();
Vector VORI_INVOICE_DT = dlng_inv.getVORI_INVOICE_DT();

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
					    	DLNG Late Payment Invoice Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
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
					  					<%for(int i=(currentYear+1); i > filter_start_year;i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
					  		</div>
						</div>
					  	<div class="col-sm-5 col-xs-5 col-md-5">
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
															<table class="table table-bordered table-hover" id="example<%=j%>">
																<thead id="tbsearch<%=j%>">
																	<tr>
																		<th rowspan="2">Truck No.</th>
																		<th rowspan="2">Customer</th>
																		<th rowspan="2">Contract No<br>[Contract/Trade Ref#]</th>
																		<th rowspan="2">Plant</th>
																		<th rowspan="2">Business Unit</th>
																		<th rowspan="2">Invoice#</th>
																		<th rowspan="2">Invoice Date</th>
																		<th rowspan="2">Invoice Due Date</th>
																		<th rowspan="2">Payment Received Date</th>
																		<th rowspan="2">Latepay Invoice#<br>[Invoice Date]</th>
																		<th rowspan="2">Generate/<br>View IRP</th>
																		<th rowspan="2">Modify IRP</th>
																		<th rowspan="2">IRP Approve</th>					
																		<th rowspan="2">Fin Ops<br>Finalization</th>
																		<th rowspan="2">SAP XML</th>
																		<%if(heading.equals("SVC_INV_HEAD")) {%><th rowspan="2">IRN Status</th><%} %>
																		<th colspan="3" align="center">
																		<div align="center">
																			<select class="form-select form-select-sm" name="pdf_type" id="pdf_type_<%=heading%>" style="width:80px;" onchange="refresh('<%=heading%>');">
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
																		<td align="center"><%=VTRUCK_NO.elementAt(i)%></td>
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
																		<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
																		<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
																		<td align="center"><%=VORI_INVOICE_NO.elementAt(i)%></td>
																		<td align="center"><%=VORI_INVOICE_DT.elementAt(i)%></td>
																		<td align="center"><%=VORI_INVOICE_DUE_DT.elementAt(i)%></td>
																		<td align="center"><%=VORI_INVOICE_PAYRECV_DT.elementAt(i)%></td>
																		<td align="center"><%=VINVOICE_NO.elementAt(i)%><br><%=VINVOICE_DT.elementAt(i)%></td>
																		<td align="center">
																			<i class="fa <%if(VINVOICE_SEQ.elementAt(i).equals("")){ %>fa-cogs<%}else{%>fa-eye<%} %> fa-2x" 
																			onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																								'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																								'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																								'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																								'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																								'<%=VFINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VBU_STATE_TIN.elementAt(i)%>',
																								'INSERT','<%=heading%>','<%=VORI_INV_FLAG.elementAt(i) %>',
																								'<%=VORI_INV_SEQ.elementAt(i) %>','<%=VORI_FINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VINV_FLAG.elementAt(i) %>','<%=VORI_INVOICE_NO.elementAt(i) %>',
																								'<%=VSTART_DT.elementAt(i) %>','<%=VEND_DT.elementAt(i) %>',
																								'<%=VTRUCK_CD.elementAt(i) %>','<%=VTRUCK_TRANS_CD.elementAt(i) %>',
																								'<%=VTRUCK_NO.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i) %>',
																								'<%=VSELL_CONT_MAP.elementAt(i)%>');"
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
																								'<%=VFINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VBU_STATE_TIN.elementAt(i)%>',
																								'MODIFY','<%=heading%>','<%=VORI_INV_FLAG.elementAt(i) %>',
																								'<%=VORI_INV_SEQ.elementAt(i) %>','<%=VORI_FINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VINV_FLAG.elementAt(i) %>','<%=VORI_INVOICE_NO.elementAt(i) %>',
																								'<%=VSTART_DT.elementAt(i) %>','<%=VEND_DT.elementAt(i) %>',
																								'<%=VTRUCK_CD.elementAt(i) %>','<%=VTRUCK_TRANS_CD.elementAt(i) %>',
																								'<%=VTRUCK_NO.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i) %>',
																								'<%=VSELL_CONT_MAP.elementAt(i)%>');"
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
																								'<%=VFINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','CHECK','<%=heading%>',
																								'<%=VINV_FLAG.elementAt(i) %>','<%=VORI_INV_FLAG.elementAt(i) %>','<%=VORI_FINANCIAL_YEAR.elementAt(i) %>','<%=VORI_INV_SEQ.elementAt(i) %>',
																								'<%=VSTART_DT.elementAt(i) %>','<%=VEND_DT.elementAt(i) %>','<%=VORI_INVOICE_NO.elementAt(i) %>',
																								'<%=VTRUCK_CD.elementAt(i) %>','<%=VTRUCK_TRANS_CD.elementAt(i) %>',
																								'<%=VTRUCK_NO.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i) %>');"
																			style="<%if(VINV_APPROVED_FLAG.elementAt(i).equals("Y") || VINVOICE_EXIST.elementAt(i).equals("N")){ %> 
 																					 pointer-events: none; opacity: .65; color: gray;  
 																					<%} else {%> 
																						color:#ff3399;
 																					<%}%>"> 
																			</i>
																		</td>
																		<td align="center">
																		<%if(VRE_PRINT_PDF.elementAt(i).equals("Y")){%>
																		<i class="fa fa-eye fa-2x"
																			onclick="openActivity3('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																								'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																								'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																								'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																								'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																								'<%=VFINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','','<%=heading%>',
																								'<%=VINV_FLAG.elementAt(i) %>','<%=VORI_INV_FLAG.elementAt(i) %>','<%=VORI_FINANCIAL_YEAR.elementAt(i) %>','<%=VORI_INV_SEQ.elementAt(i) %>',
																								'<%=VSTART_DT.elementAt(i) %>','<%=VEND_DT.elementAt(i) %>','<%=VORI_INVOICE_NO.elementAt(i) %>',
																								'<%=VTRUCK_CD.elementAt(i) %>','<%=VTRUCK_TRANS_CD.elementAt(i) %>',
																								'<%=VTRUCK_NO.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i) %>');"
																			style="color:black;">
																			</i>
																		<%}else{ %>
																			<i class="fa fa-flag fa-2x"
																			onclick="openActivity3('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																								'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																								'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																								'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																								'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																								'<%=VFINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','APPROVE','<%=heading%>',
																								'<%=VINV_FLAG.elementAt(i) %>','<%=VORI_INV_FLAG.elementAt(i) %>','<%=VORI_FINANCIAL_YEAR.elementAt(i) %>','<%=VORI_INV_SEQ.elementAt(i) %>',
																								'<%=VSTART_DT.elementAt(i) %>','<%=VEND_DT.elementAt(i) %>','<%=VORI_INVOICE_NO.elementAt(i) %>',
																								'<%=VTRUCK_CD.elementAt(i) %>','<%=VTRUCK_TRANS_CD.elementAt(i) %>',
																								'<%=VTRUCK_NO.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i) %>');"
																			<%if(heading.equals("SVC_INV_HEAD")) {%>
																			style="<%if(!VPDF_INV_FLAG.elementAt(i).equals("") || !VINV_CHECKED_FLAG.elementAt(i).equals("Y") || VIS_IRN_GENERATED.elementAt(i).equals("Y")){ %>
																					pointer-events: none; opacity: .65; color: gray;
																					<%} else{%>
																					color:#00cc00;
																					<%}%>">
																			<%}else{ %>
																			style="<%if(!VPDF_INV_FLAG.elementAt(i).equals("") || !VINV_CHECKED_FLAG.elementAt(i).equals("Y")){ %> 
 																					pointer-events: none; opacity: .65; color: gray; 
 																					<%} else{%> 
																					color:#00cc00;
																					<%}%>"> 
																			<%} %>
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
																			<i class="fa <%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
																			onclick="doGenXML('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
																			 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																			 'SG','','<%=VBU_STATE_TIN.elementAt(i)%>','<%=VSAP_APPROVAL_FLAG.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																			 '<%=VCONT_NO.elementAt(i)%>','<%=VPDF_INV_FLAG.elementAt(i)%>','<%=heading%>');"
																			<%-- style="<%if(!VINV_APPROVED_FLAG.elementAt(i).equals("Y")){ %> --%>
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
																		<%if(heading.equals("SVC_INV_HEAD")) {%>
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
																		<%} %>
																		<td align="center">
																			<i class="fa fa-print fa-2x" title="<%=VPDF_TYPE.elementAt(i)%>"
																				onclick="printPDF('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																								'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																								'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																								'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																								'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																								'<%=VFINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=i%>','<%=heading%>',
																								'<%=VINV_FLAG.elementAt(i) %>','<%=VORI_INV_FLAG.elementAt(i) %>',
																								'<%=VORI_FINANCIAL_YEAR.elementAt(i) %>','<%=VORI_INV_SEQ.elementAt(i) %>',
																								'<%=VSTART_DT.elementAt(i) %>','<%=VEND_DT.elementAt(i) %>',
																								'<%=VORI_INVOICE_NO.elementAt(i) %>','<%=VTRUCK_CD.elementAt(i) %>',
																								'<%=VTRUCK_TRANS_CD.elementAt(i) %>',
																								'<%=VTRUCK_NO.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i) %>');"
																				style="<%if(VRE_PRINT_PDF.elementAt(i).equals("Y") && !VPDF_TYPE.elementAt(i).equals(pdf_type)){%>
																				color:#800000;
																				<%}else if(!VINV_APPROVED_FLAG.elementAt(i).equals("Y") || !VIS_IRN_GENERATED.elementAt(i).equals("Y") || VPDF_TYPE.elementAt(i).equals(pdf_type)){ %>
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
																			<i class="fa fa-envelope-o fa-2x" title="<%=VSIGN_PDF_TYPE.elementAt(i)%>"
																				onclick="openMailBody('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																					'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																					'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																					'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																					'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																					'<%=VFINANCIAL_YEAR.elementAt(i) %>',
																					'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>',
																					'<%=i%>','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>');"
																				style="<%if(VSIGN_PDF_TYPE.elementAt(i).equals("")){ %>
																				pointer-events: none; opacity: .65; color: gray;
																				<%} else{%>
																				color:blue;
																				<%}%>">
																			</i>
																			<input type="hidden" name="all_mail_pdf_type" id="all_mail_pdf_type<%=i%>" value="<%=VSIGN_PDF_TYPE.elementAt(i)%>">
																		</td>
																	</tr>
																		<%if(k==index){
																			i=i+1;
																			break;
																		} %>
																<%}}else{ %>
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

<input type="hidden" name="sysdate" value="<%=sysdate%>">

<input type="hidden" name="invoice_list_name" value="<%=VINVOICE_LIST_NAME.size()%>">

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

<script>
$(document).ready(function() {

    var invoice_list_name = parseInt(document.forms[0].invoice_list_name.value, 10);

    for (var j = 0; j < invoice_list_name; j++)
    {
        var tbsearch = document.getElementById("tbsearch" + j);

        $(tbsearch).find('th').each(function(i) {

            if ($(this).find('select').length > 0) {
                return;
            }

            var title = $(this).text().trim();

            if (title !== "Select" && 
                title !== "Generate/View IRP" && 
                title !== "Modify IRP" && 
                title !== "IRP Approve" && 
                title !== "Fin OpsFinalization" && 
                title !== "SAP XML" && 
                title !== "IRN Status" && 
                title !== "Print PDF" && 
                title !== "View PDF" && 
                title !== "Send Mail" &&
                !title.startsWith("Original Dupl")
            ) {
            	let originalTitle = $(this).html();
            	
                $(this).html(
               		'<div class="head-title">' + originalTitle + '</div>' +
                    '<div align="center">' +
                        '<input type="text" class="form-control form-control-sm" ' +
                        'id="table_' + title + '_' + j + '" ' +
                        'onkeyup="Search(this,' + i + ',' + j + ');" ' +
                        'placeholder="Search ' + title + '" ' +
                        'style="width:100px"/>' +
                    '</div>'
                );
            }
        });
    }
});

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
</script>

</form>
</body>
</html>