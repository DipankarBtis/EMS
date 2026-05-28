<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh(accroid)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var pdf_type="O";
	/*if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	}*/
	
	pdf_type = document.getElementById("pdf_type").value;
	
	var u = document.forms[0].u.value;
	
	var url = "../sales_invoice/frm_ltcora_storage_invoice_prep.jsp?&u="+u+"&month="+month+"&year="+year+
			"&accroid="+accroid+"&pdf_type="+pdf_type;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var newWindow;
function openActivity1(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,
		plant_seq,period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,bu_st_cd,
		temp_period_st_dt,temp_period_end_dt,cargo_no,operation,accroid,inv_flag)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_generate_ltcora_storage_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+"&operation="+operation+
			"&exist_financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&temp_period_start_dt="+temp_period_st_dt+"&temp_period_end_dt="+temp_period_end_dt+
			"&cargo_no="+cargo_no+"&accroid="+accroid+"&inv_flag="+inv_flag+"&u="+u;
	
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

function openActivity3(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,bu_st_cd,inv_seq,cargo_no,activityFlag,accroid,inv_flag)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_view_chk_aprv_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+"&activityFlag="+activityFlag+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&cargo_no="+cargo_no+"&accroid="+accroid+
			"&inv_flag="+inv_flag+"&u="+u;
	
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
		period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,bu_st_cd,inv_seq,index,cargo_no,accroid,inv_flag)
{
	var print_access = document.forms[0].print_access.value;
	
	var pdf_type="";
	/*if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	}*/
	pdf_type = document.getElementById("pdf_type").value;
	
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
		var url = "pdf_sell_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&cargo_no="+cargo_no+"&accroid="+accroid+"&inv_flag="+inv_flag+
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

function refershPar(sub_msg,msg_type,accroid)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var pdf_type="O";
	/*if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	}*/
	pdf_type = document.getElementById("pdf_type").value;
	
	var msg="";
	var flag = true;
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "frm_ltcora_storage_invoice_prep.jsp?&u="+u+"&pdf_type="+pdf_type+
			"&month="+month+"&year="+year+"&accroid="+accroid+"&msg="+sub_msg+"&msg_type="+msg_type;
	
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

function openMailBody(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,
		period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,bu_st_cd,inv_seq,index,accroid,inv_flag)
{
	var pdf_type="";
	/*if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	}*/
	
	pdf_type = document.getElementById("pdf_type").value;
	
	var all_mail_pdf_type = document.getElementById("all_mail_pdf_type"+index).value;
	
	if(pdf_type == "All")
	{
		pdf_type=all_mail_pdf_type;
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_sign_pdf_mail.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+
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
int filter_start_year = CommonVariable.filter_start_year;

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String pdf_type=request.getParameter("pdf_type")==null?"O":request.getParameter("pdf_type");

if(month.length() == 1)
{
	month="0"+month; 
}

sales_inv.setCallFlag("LTCORA_STORAGE_INV_PREPARATION_LIST");
sales_inv.setComp_cd(owner_cd);
sales_inv.setMonth(month);
sales_inv.setYear(year);
sales_inv.setPrint_pdf_type(pdf_type);
sales_inv.setView_pdf_type(pdf_type);
sales_inv.setMail_pdf_type(pdf_type);
sales_inv.init();

Vector VINVOICE_LIST_ABBR = sales_inv.getVINVOICE_LIST_ABBR();
Vector VINVOICE_LIST_NAME = sales_inv.getVINVOICE_LIST_NAME();
Vector VINDEX = sales_inv.getVINDEX();

Vector VCOUNTERPTY_CD = sales_inv.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = sales_inv.getVCOUNTERPTY_ABBR();
Vector VCONT_NO = sales_inv.getVCONT_NO();
Vector VCONT_REV_NO = sales_inv.getVCONT_REV_NO();
Vector VCARGO_NO = sales_inv.getVCARGO_NO();
Vector VAGMT_NO = sales_inv.getVAGMT_NO();
Vector VAGMT_REV_NO = sales_inv.getVAGMT_REV_NO();
//Vector VSTART_DT = sales_inv.getVSTART_DT();
//Vector VEND_DT = sales_inv.getVEND_DT();
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
Vector VINV_FLAG = sales_inv.getVINV_FLAG();
Vector VINVOICE_DT = sales_inv.getVINVOICE_DT();

Vector VTEMP_PERIOD_START_DT = sales_inv.getVTEMP_PERIOD_START_DT();
Vector VTEMP_PERIOD_END_DT = sales_inv.getVTEMP_PERIOD_END_DT();
Vector VALLOC_QTY = sales_inv.getVALLOC_QTY();
Vector VIS_IRN_GENERATED = sales_inv.getVIS_IRN_GENERATED();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//";
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;

%>
<body>
<%@ include file="../home/header.jsp"%>
<form>
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
					    	LTCORA(Sell) Storage Invoice Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-0 col-md-4"></div>
						<div class="col-sm-6 col-xs-12 col-md-4">
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
					  					<%for(int i=(currentYear); i > filter_start_year;i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
					  		</div>
						</div>
					  	<div class="col-sm-3 col-xs-0 col-md-4"></div>
					</div>
				</div>
				<div class="card-body cdbody">
	      			<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover serchtbl" id="example0">
								<thead id="tbsearch0">
									<tr>
										<th class="tbser0" rowspan="2">Customer</th>
										<th class="tbser0" rowspan="2">Contract No<br>[Contract/Trade Ref#]</th>
										<th class="tbser0" rowspan="2">Plant</th>
										<th class="tbser0" rowspan="2">Business Unit</th>
										<th class="tbser0" rowspan="2">Invoice#</th>
										<th class="tbser0" rowspan="2">Invoice Date</th>
										<th rowspan="2">Generate/<br>View IRP</th>
										<th rowspan="2">Modify IRP</th>
										<th rowspan="2">IRP Check</th>					
										<th rowspan="2">Fin Ops<br>Finalization</th>
										<th rowspan="2">SAP XML</th>
										<th rowspan="2">IRN Status</th>
										<th colspan="3" align="center">
										<div align="center">
											<select class="form-select form-select-sm" name="pdf_type" id="pdf_type" style="width:80px;" onchange="refresh('<%//=heading%>');">
												<option value="O">Original</option>
												<option value="D">Duplicate</option>
												<option value="T">Triplicate</option>
												<option value="All">All</option>
											</select>
											<script>document.getElementById("pdf_type").value="<%=pdf_type%>"</script>
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
								<%
								if(VCOUNTERPTY_CD.size()>0)
								{
									for(int i=0; i<VCOUNTERPTY_CD.size(); i++)
									{
									%>
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
										<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
										<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
											<td align="center">
												<i class="fa <%if(VINVOICE_SEQ.elementAt(i).equals("")){ %>fa-cogs<%}else{%>fa-eye<%} %> fa-2x" 
												onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																	'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																	'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																	'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																	'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																	'<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VTEMP_PERIOD_START_DT.elementAt(i)%>',
																	'<%=VTEMP_PERIOD_END_DT.elementAt(i)%>','<%=VCARGO_NO.elementAt(i)%>','INSERT','<%//=heading%>','<%=VINV_FLAG.elementAt(i) %>');"
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
																	'<%=VTEMP_PERIOD_END_DT.elementAt(i)%>','<%=VCARGO_NO.elementAt(i)%>','MODIFY','<%//=heading%>','<%=VINV_FLAG.elementAt(i) %>');"
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
																	'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCARGO_NO.elementAt(i)%>','CHECK','<%//=heading%>','<%=VINV_FLAG.elementAt(i) %>');"
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
																	'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCARGO_NO.elementAt(i)%>','APPROVE','<%//=heading%>','<%=VINV_FLAG.elementAt(i) %>');"
												style="<%if(!VPDF_INV_FLAG.elementAt(i).equals("") || !VINV_CHECKED_FLAG.elementAt(i).equals("Y") || VIS_IRN_GENERATED.elementAt(i).equals("Y")){ %>
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
												<%}else{%>
												<i class="fa <%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
												onclick="doGenXML('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
												 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
												 'SG','','<%=VBU_STATE_TIN.elementAt(i)%>','<%=VSAP_APPROVAL_FLAG.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
												 '<%=VCONT_NO.elementAt(i)%>','<%=VPDF_INV_FLAG.elementAt(i)%>','<%//=heading%>','<%=VINV_FLAG.elementAt(i) %>');"
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
																	'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																	'<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=i%>','<%=VCARGO_NO.elementAt(i)%>','<%//=heading%>','<%=VINV_FLAG.elementAt(i) %>');"
													style="<%if(!VINV_APPROVED_FLAG.elementAt(i).equals("Y") || !VIS_IRN_GENERATED.elementAt(i).equals("Y") || VPDF_TYPE.elementAt(i).equals(pdf_type)){ %>
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
														'<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
														'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=i%>','<%//=heading%>','<%=VINV_FLAG.elementAt(i) %>');"
													style="<%if(VSIGN_PDF_TYPE.elementAt(i).equals("")){ %>
													pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
													color:blue;
													<%}%>">
												</i>
												<input type="hidden" name="all_mail_pdf_type" id="all_mail_pdf_type<%=i%>" value="<%=VSIGN_PDF_TYPE.elementAt(i)%>">
											</td>
										<%} %>	
									<%}else{ %>
										<tr>
											<td align="center" colspan="15"><%=utilmsg.infoMessage("<b>No Storage Invoice is Ready for Generate!</b>") %></td>
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