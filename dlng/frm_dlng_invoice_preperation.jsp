<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var gas_dt = document.forms[0].gas_dt.value;
	var pdf_type = document.forms[0].pdf_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_invoice_preperation.jsp?gas_dt="+gas_dt+"&pdf_type="+pdf_type+"&u="+u;

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
		plant_seq,period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,operation,inv_flag,truck_cd,truck_trans_cd,mapping_id)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_generate_dlng_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+"&operation="+operation+
			"&exist_financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&inv_flag="+inv_flag+"&u="+u+
			"&truck_cd="+truck_cd+"&truck_trans_cd="+truck_trans_cd+"&mapping_id="+mapping_id;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare DLNG Sales Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare DLNG Sales Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function openActivity3(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,activityFlag,inv_flag,truck_cd,truck_trans_cd,mapping_id)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_view_chk_aprv_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+"&activityFlag="+activityFlag+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+
			"&inv_flag="+inv_flag+"&u="+u+
			"&truck_cd="+truck_cd+"&truck_trans_cd="+truck_trans_cd+"&mapping_id="+mapping_id;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare DLNG Sales Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare DLNG Sales Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function printPDF(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,
		period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,index,accroid,inv_flag,truck_cd,truck_trans_cd,mapping_id,reprint_flag)
{
	var print_access = document.forms[0].print_access.value;
	
	var pdf_type="";
	if(document.getElementById("pdf_type")!=null && document.getElementById("pdf_type")!=undefined)
	{
		pdf_type = document.getElementById("pdf_type").value;
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
		var url = "pdf_dlng_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&accroid="+accroid+"&inv_flag="+inv_flag+
			"&truck_cd="+truck_cd+"&truck_trans_cd="+truck_trans_cd+"&mapping_id="+mapping_id+"&reprint_flag="+reprint_flag+
			"&print_pdf_type="+pdf_type+"&u="+u;
	
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

function openPdfFile(url)
{
	window.open(url);
}

function refershPar(sub_msg,msg_type,accroid)
{
	var gas_dt = document.forms[0].gas_dt.value;
	
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
		var url = "frm_dlng_invoice_preperation.jsp?&u="+u+"&pdf_type="+pdf_type+
			"&gas_dt="+gas_dt+"&accroid="+accroid+"&msg="+sub_msg+"&msg_type="+msg_type;
	
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
		period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,index,accroid,inv_flag)
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
		period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,index,accroid,inv_flag)
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
String gas_dt = request.getParameter("gas_dt")==null?sysdate:request.getParameter("gas_dt");
String pdf_type=request.getParameter("pdf_type")==null?"All":request.getParameter("pdf_type");

dlng_inv.setCallFlag("DLNG_SALES_INVOICE_PREPARATION_LIST");
dlng_inv.setComp_cd(owner_cd);
dlng_inv.setGas_dt(gas_dt);
dlng_inv.setPrint_pdf_type(pdf_type);
dlng_inv.setView_pdf_type(pdf_type);
dlng_inv.setMail_pdf_type(pdf_type);
dlng_inv.init();

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
Vector VTRUCK_CD = dlng_inv.getVTRUCK_CD();
Vector VTRUCK_NO = dlng_inv.getVTRUCK_NO();
Vector VBU_STATE_TIN = dlng_inv.getVBU_STATE_TIN();
Vector VFINANCIAL_YEAR = dlng_inv.getVFINANCIAL_YEAR();
Vector VINVOICE_SEQ = dlng_inv.getVINVOICE_SEQ();
Vector VINV_CHECKED_FLAG = dlng_inv.getVINV_CHECKED_FLAG();
Vector VINV_APPROVED_FLAG = dlng_inv.getVINV_APPROVED_FLAG();
Vector VINVOICE_EXIST = dlng_inv.getVINVOICE_EXIST();
Vector VINV_FLAG = dlng_inv.getVINV_FLAG();
Vector VPERIOD_START_DT = dlng_inv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = dlng_inv.getVPERIOD_END_DT();
Vector VTRUCK_TRANS_CD = dlng_inv.getVTRUCK_TRANS_CD();
Vector VMAPPING_ID = dlng_inv.getVMAPPING_ID();
Vector VPDF_INV_FLAG = dlng_inv.getVPDF_INV_FLAG();
Vector VINVOICE_NO = dlng_inv.getVINVOICE_NO();
Vector VSAP_APPROVAL_FLAG=dlng_inv.getVSAP_APPROVAL_FLAG();
Vector VPDF_TYPE = dlng_inv.getVPDF_TYPE();
Vector VPDF_FILE_PATH = dlng_inv.getVPDF_FILE_PATH();
Vector VPDF_FILE_NAME = dlng_inv.getVPDF_FILE_NAME();
Vector VSIGN_PDF_TYPE = dlng_inv.getVSIGN_PDF_TYPE();
Vector VGENERATED_FORM_402_PATH = dlng_inv.getVGENERATED_FORM_402_PATH();
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
					    	DLNG Sales Invoice Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
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
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover serchtbl" id="example0">
									<thead id="tbsearch0">
										<tr>
											<th class="tbser0" rowspan="2">Truck No</th>
											<th class="tbser0" rowspan="2">Customer</th>
											<th class="tbser0" rowspan="2">Contract No<br>[Contract/Trade Ref#]</th>
											<th class="tbser0" rowspan="2">Contract Period</th>
											<th class="tbser0" rowspan="2">Plant</th>
											<th class="tbser0" rowspan="2">Business Unit</th>
											<th class="tbser0" rowspan="2">Invoice#</th>
											<th rowspan="2">Generate/<br>View IRP</th>
											<th rowspan="2">Modify IRP</th>
											<th rowspan="2">Invoice Check</th>	
											<th rowspan="2">SAP XML</th>
											<th colspan="3" align="center">
											<div align="center">
												<select class="form-select form-select-sm" name="pdf_type" id="pdf_type" style="width:100px;" onchange="refresh();">
													<option value="O">Original</option>
													<option value="D">Duplicate</option>
													<option value="T">Triplicate</option>
													<option value="All">All</option>
												</select>
												<script>document.getElementById("pdf_type").value="<%=pdf_type%>"</script>
											</div>
											</th>
											<th colspan="2" align="center">Form 402</th>
										</tr>
										<tr>
											<th>Print PDF</th>
											<th>View PDF</th>
											<th>Send Mail</th>
											<th>Download</th>
											<th>Send Mail</th>
										</tr>
									</thead>
									<tbody>
									<%if(VCOUNTERPTY_CD.size()>0){ %>
										<%for(int i=0; i<VCOUNTERPTY_CD.size(); i++){ %>
										<tr>
											<td align="center"><%=VTRUCK_NO.elementAt(i)%></td>
											<td align="center" title="<%=VCOUNTERPTY_NM.elementAt(i)%>"><%=VCOUNTERPTY_ABBR.elementAt(i) %></td>
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
											<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
											<td align="center">	
												<i class="fa <%if(VINVOICE_SEQ.elementAt(i).equals("")){ %>fa-cogs<%}else{%>fa-eye<%} %> fa-2x" 
												onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																	'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																	'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																	'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																	'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VBU_STATE_TIN.elementAt(i)%>','INSERT','<%=VINV_FLAG.elementAt(i) %>',
																	'<%=VTRUCK_CD.elementAt(i) %>','<%=VTRUCK_TRANS_CD.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i)%>');"
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
																	'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VBU_STATE_TIN.elementAt(i)%>','MODIFY','<%=VINV_FLAG.elementAt(i) %>',
																	'<%=VTRUCK_CD.elementAt(i) %>','<%=VTRUCK_TRANS_CD.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i)%>');"
												style="<%if(VINV_APPROVED_FLAG.elementAt(i).equals("Y") || VINVOICE_EXIST.elementAt(i).equals("N")){ %>
														pointer-events: none; opacity: .65;color: gray;
														<%} else {%>
															color:#ff9900;
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
																	'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','','<%=VINV_FLAG.elementAt(i) %>',
																	'<%=VTRUCK_CD.elementAt(i) %>','<%=VTRUCK_TRANS_CD.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i)%>');"
												style="color:black;">
												</i>
											<%}else{ %>											
												<i class="fa fa-flag fa-2x"
												onclick="openActivity3('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																	'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																	'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																	'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																	'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','APPROVE','<%=VINV_FLAG.elementAt(i) %>',
																	'<%=VTRUCK_CD.elementAt(i) %>','<%=VTRUCK_TRANS_CD.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i)%>');"
												style="<%if(!VPDF_INV_FLAG.elementAt(i).equals("") ||  VINVOICE_EXIST.elementAt(i).equals("N")){ %>
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
													<i class="fa <%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
														onclick="doGenXML('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
														 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
														 'SG','','<%=VBU_STATE_TIN.elementAt(i)%>','<%=VSAP_APPROVAL_FLAG.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
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
											<td align="center">
												<i class="fa fa-print fa-2x" title="<%=VPDF_TYPE.elementAt(i)%>"
													onclick="printPDF('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																	'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																	'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																	'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																	'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=i%>','<%//=heading%>','<%=VINV_FLAG.elementAt(i) %>',
																	'<%=VTRUCK_CD.elementAt(i) %>','<%=VTRUCK_TRANS_CD.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i)%>','<%=VRE_PRINT_PDF.elementAt(i)%>');"
													style="<%if(VRE_PRINT_PDF.elementAt(i).equals("Y") && !VPDF_TYPE.elementAt(i).equals(pdf_type)) {%>
													color:#800000;
													<%}else if(!VINV_APPROVED_FLAG.elementAt(i).equals("Y") || VPDF_TYPE.elementAt(i).equals(pdf_type)){ %>
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
														'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
														'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=i%>','<%//=heading%>','<%=VINV_FLAG.elementAt(i) %>');"
													style="<%if(VSIGN_PDF_TYPE.elementAt(i).equals("")){ %>
													pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
													color:blue;
													<%}%>">
												</i>
												<input type="hidden" name="all_mail_pdf_type" id="all_mail_pdf_type<%=i%>" value="<%=VSIGN_PDF_TYPE.elementAt(i)%>">
											</td>
											<td align="center">
												<div class="icon-wrapper">
												 <a href = "../<%=VGENERATED_FORM_402_PATH.elementAt(i)%>" 
												  <%if(!VGENERATED_FORM_402_PATH.elementAt(i).equals("")) {%>title="Click to Download Form 402"<%} %>>
												  <i class="fa fa-file fa-2x" 
												  <%if(VGENERATED_FORM_402_PATH.elementAt(i).equals("")) {%>
												  style="pointer-events: none; opacity: .65; color: gray;"
												  <%}else{%>
												  <%}%>></i></a>
												  <span class="icon-text">402</span>
												</div>
											</td>
											<td align="center">
												<i class="fa fa-envelope-o fa-2x" title="<%=VSIGN_PDF_TYPE.elementAt(i)%>"
													onclick="openMailBodyFor402('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
														'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
														'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
														'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
														'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
														'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=i%>','<%//=heading%>','<%=VINV_FLAG.elementAt(i) %>');"
													style="<%if(VGENERATED_FORM_402_PATH.elementAt(i).equals("")){ %>
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