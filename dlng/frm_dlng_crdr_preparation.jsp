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
	/*if(document.forms[0].ff_pdf_type!=null && document.forms[0].ff_pdf_type!=undefined)
	{
		ff_pdf_type = document.forms[0].ff_pdf_type.value;
	}*/
	
	var msg="";
	var flag = true;
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "../dlng/frm_dlng_crdr_preparation.jsp?&u="+u+"&pdf_type="+pdf_type+
				"&ff_pdf_type="+ff_pdf_type+"&month="+month+"&year="+year+"&accroid="+accroid;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg)
	}
}

function refershPar(sub_msg,msg_type,accroid)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var pdf_type="All";
	var ff_pdf_type="All";
	if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	}
	/*if(document.forms[0].ff_pdf_type!=null && document.forms[0].ff_pdf_type!=undefined)
	{
		ff_pdf_type = document.forms[0].ff_pdf_type.value;
	}*/
	
	var msg="";
	var flag = true;
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "../dlng/frm_dlng_crdr_preparation.jsp?&u="+u+"&pdf_type="+pdf_type+
			"&ff_pdf_type="+ff_pdf_type+"&month="+month+"&year="+year+"&accroid="+accroid+"&msg="+sub_msg+"&msg_type="+msg_type;
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg)
	}
}

var newWindow;
function openCreditDebitInv(accroid)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_generate_dlng_crdr.jsp?u="+u+"&month="+month+"&year="+year+"&operation=PREPARE&accroid="+accroid;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Sales Credit/Debit","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Sales Credit/Debit","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function openActivity1(counterparty_cd,inv_seq,fin_yr,bu_st_cd,operation,accroid,inv_flag,ref_no)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_generate_dlng_crdr.jsp?counterparty_cd="+counterparty_cd+"&invoice_type="+inv_flag+"&invoice_seq="+inv_seq+"&operation="+operation+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&accroid="+accroid+"&inv_flag="+inv_flag+"&sel_inv_no="+ref_no+"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare Credit/Debit","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare Credit/Debit","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function openActivity3(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,cargo_no,activityFlag,accroid,inv_flag,ref_no)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_view_chk_aprv_crdr_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+"&activityFlag="+activityFlag+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&cargo_no="+cargo_no+"&accroid="+accroid+
			"&inv_flag="+inv_flag+"&sel_inv_no="+ref_no+"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare Credit/Debit","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare Credit/Debit","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function printPDF(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,
		period_st_dt,period_end_dt,bu,fin_yr,bu_st_cd,inv_seq,index,accroid,inv_flag)
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
		var url = "pdf_dlng_crdr_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&accroid="+accroid+"&inv_flag="+inv_flag+
			"&print_pdf_type="+pdf_type+"&u="+u;
	
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"PDF CRDR Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"PDF CRDR Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
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
	
	var url = "../dlng/rpt_view_crdr_sales_inv_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
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

function openPdfFile(url)
{
	window.open(url);
}

function openAllPdfFile(fin_yr,bu_st_cd,inv_seq)
{
	var url = "rpt_view_all_pdf.jsp?financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"PDF DLNG Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"PDF DLNG Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
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
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="sales_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
int currentYear = 0;
int currentMonth = 0;
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	currentMonth=Integer.parseInt(temp[1]);
	currentYear=Integer.parseInt(temp[2]);
}
int filter_start_year = CommonVariable.filter_start_year;

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
String pdf_type=request.getParameter("pdf_type")==null?"All":request.getParameter("pdf_type");

if(month.length() == 1)
{
	month="0"+month; 
}

sales_inv.setCallFlag("EXISTING_CRDR_LIST");
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
Vector VCOUNTERPTY_NM = sales_inv.getVCOUNTERPTY_NM();
Vector VCONT_NO = sales_inv.getVCONT_NO();
Vector VCONT_REV_NO = sales_inv.getVCONT_REV_NO();
Vector VCARGO_NO = sales_inv.getVCARGO_NO();
Vector VAGMT_NO = sales_inv.getVAGMT_NO();
Vector VAGMT_REV_NO = sales_inv.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = sales_inv.getVCONTRACT_TYPE();
Vector VDEAL_NO = sales_inv.getVDEAL_NO();
Vector VCONT_REF_NO = sales_inv.getVCONT_REF_NO();
Vector VAGMT_BASE = sales_inv.getVAGMT_BASE();
Vector VPLANT_SEQ = sales_inv.getVPLANT_SEQ();
Vector VPLANT_ABBR = sales_inv.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = sales_inv.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = sales_inv.getVBU_PLANT_ABBR();
Vector VPERIOD_START_DT = sales_inv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = sales_inv.getVPERIOD_END_DT();
Vector VCREDIT_DEBIT_NO = sales_inv.getVCREDIT_DEBIT_NO();
Vector VCRDR_CRITERIA = sales_inv.getVCRDR_CRITERIA();
Vector VREF_NO = sales_inv.getVREF_NO();
Vector VINV_FLAG = sales_inv.getVINV_FLAG();
Vector VINVOICE_TYPE = sales_inv.getVINVOICE_TYPE();
Vector VINVOICE_TYPE_NM = sales_inv.getVINVOICE_TYPE_NM();
Vector VBU_STATE_TIN = sales_inv.getVBU_STATE_TIN();
Vector VFINANCIAL_YEAR = sales_inv.getVFINANCIAL_YEAR();
Vector VINVOICE_SEQ = sales_inv.getVINVOICE_SEQ();
Vector VINVOICE_DT = sales_inv.getVINVOICE_DT();
Vector VINV_CHECKED_FLAG = sales_inv.getVINV_CHECKED_FLAG();
Vector VINV_APPROVED_FLAG = sales_inv.getVINV_APPROVED_FLAG();
Vector VPDF_INV_FLAG = sales_inv.getVPDF_INV_FLAG();
Vector VIS_IRN_GENERATED = sales_inv.getVIS_IRN_GENERATED();
Vector VSAP_APPROVAL_FLAG = sales_inv.getVSAP_APPROVAL_FLAG();
Vector VPDF_TYPE = sales_inv.getVPDF_TYPE();
Vector VPDF_FILE_PATH = sales_inv.getVPDF_FILE_PATH();
Vector VPDF_FILE_NAME = sales_inv.getVPDF_FILE_NAME();
Vector VSIGN_PDF_TYPE = sales_inv.getVSIGN_PDF_TYPE();
Vector VEMAIL_SENT = sales_inv.getVEMAIL_SENT();
Vector VEMAIL_SENT_INFO = sales_inv.getVEMAIL_SENT_INFO();

Vector VTRUCK_CD = sales_inv.getVTRUCK_CD();
Vector VTRUCK_NO = sales_inv.getVTRUCK_NO();

Vector VRE_PRINT_PDF = sales_inv.getVRE_PRINT_PDF();

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
					    	DLNG Sales Credit/Debit Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="form-group row">
								<div class="col-md-1 col-sm-0 col-xs-0"></div>
								<div class="col-md-4 col-sm-6 col-xs-6">
									<!-- <div class="d-flex justify-content-center"> -->
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
									<!-- </div> -->
								</div>
								<div class="col-md-4 col-sm-3 col-xs-3">
									<!-- <div class="d-flex justify-content-center"> -->
										<div class="form-group row">
											<div class="col-auto">
												<!-- <label class="form-label"><b>Invoice Type</b></label> -->
												<div class="btn-group">
													<label class="btn btn-outline-secondary subbtngrp" onclick="openCreditDebitInv('<%//=accro2%>');"><i class="fa fa-plus-circle"></i>&nbsp;Create New Credit/Debit</label>
												</div>
								  			</div>
										</div>
									<!-- </div> -->
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
						  		<div class="col-md-1 col-sm-0 col-xs-0"></div>
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
							    			<%=VINVOICE_LIST_NAME.elementAt(j)%>&nbsp;<font color="blue" style="background: white;padding: 2px 5px 4px 5px;border-radius: 30px;">(<%=index%> Items)</font>
							      		</button>	
							    	</h2>
									<div id="collapse<%=j%>" class="accordion-collapse collapse <%if(accroid.equals(heading)){%>show<%}%>" aria-labelledby="<%=heading%>">
							      		<div class="accordion-body accor-body">
											<div class="row">
												<div class="col-md-12 col-sm-12 col-xs-12">
													<div class="table-responsive">
														<table class="table table-bordered table-hover serchtbl" id="example<%=j%>">
															<thead id="tbsearch<%=j%>">
																<tr>
																	<th class="tbser<%=j%>" rowspan="2">Truck No</th>
																	<th class="tbser<%=j%>" rowspan="2">Customer</th>
																	<th class="tbser<%=j%>" rowspan="2">Contract No<br>[Contract/Trade Ref#]</th>
																	<!-- <th rowspan="2">Contract Period</th> -->
																	<th class="tbser<%=j%>" rowspan="2">Plant</th>
																	<th class="tbser<%=j%>" rowspan="2">Business Unit</th>
																	<!-- <th rowspan="2">Billing Cycle</th> -->
																	<th class="tbser<%=j%>" rowspan="2">Gas Day</th>
																	<th class="tbser<%=j%>" rowspan="2">Invoice Type</th>
																	<th class="tbser<%=j%>" rowspan="2">Credit/Debit Note Date</th>
																	<th class="tbser<%=j%>" rowspan="2">Criteria</th>
																	<th class="tbser<%=j%>" rowspan="2">Credit/Debit#<br>[Ref#]</th>
																	<th rowspan="2">View IRP</th>
																	<th rowspan="2">Modify IRP</th>				
																	<th rowspan="2">IRP Check</th>					
																	<th rowspan="2">Fin Ops<br>Finalization</th>
																	<th rowspan="2">SAP XML</th>
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
																		<td align="center" title="<%=VCOUNTERPTY_NM.elementAt(i)%>">
																			<%=VCOUNTERPTY_ABBR.elementAt(i)%>
																		</td>
																		<td align="center">
																			<font color="blue"><%=VDEAL_NO.elementAt(i)%></font>
																			<%if(VAGMT_BASE.elementAt(i).equals("D")){ %>
																			<font style="background:#a6ff4d;">[DLV]</font>
																			<%} %>
																			<br>[<%=VCONT_REF_NO.elementAt(i)%>]
																		</td>
																		<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
																		<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
																		<td align="center">
																			<font><%=VPERIOD_END_DT.elementAt(i)%></font>
																		</td>
																		<td align="center"><%=VINVOICE_TYPE_NM.elementAt(i)%></td>
																		<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
																		<td align="center"><%=VCRDR_CRITERIA.elementAt(i)%></td>
																		<td align="center">
																			<%=VCREDIT_DEBIT_NO.elementAt(i)%>
																			<br>
																			<font color="blue">[Ref# <%=VREF_NO.elementAt(i)%>]</font>
																		</td>
																		<td align="center">
																			<i class="fa fa-eye fa-2x" 
																			onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VBU_STATE_TIN.elementAt(i)%>','INSERT','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>','<%=VREF_NO.elementAt(i)%>');"
																			style="color:black;">																		
																			</i>					
																		</td>
																		<td align="center">
																			<i class="fa fa-pencil fa-2x" 
																			onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																								'<%=VBU_STATE_TIN.elementAt(i)%>','MODIFY','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>','<%=VREF_NO.elementAt(i)%>');"
																			style="<%if(VINV_CHECKED_FLAG.elementAt(i).equals("Y")){ %>
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
																								'<%=VFINANCIAL_YEAR.elementAt(i) %>','<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>',
																								'<%=VCARGO_NO.elementAt(i)%>','CHECK','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>','<%=VREF_NO.elementAt(i)%>');"
																			style="<%if(VINV_APPROVED_FLAG.elementAt(i).equals("Y")){ %>
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
																								'<%=VFINANCIAL_YEAR.elementAt(i) %>','<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>',
																								'<%=VCARGO_NO.elementAt(i)%>','','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>','<%=VREF_NO.elementAt(i)%>');"
																			style="color:black;">
																			</i>
																		<%}else{%>										
																			<i class="fa fa-flag fa-2x"
																			onclick="openActivity3('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																								'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																								'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																								'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																								'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																								'<%=VFINANCIAL_YEAR.elementAt(i) %>','<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>',
																								'<%=VCARGO_NO.elementAt(i)%>','APPROVE','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>','<%=VREF_NO.elementAt(i)%>');"
																			style="<%if(!VPDF_INV_FLAG.elementAt(i).equals("") || !VINV_CHECKED_FLAG.elementAt(i).equals("Y")){ %>
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
																			onclick="doGenXML('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VCREDIT_DEBIT_NO.elementAt(i)%>',
																			 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																			 'SG','','<%=VBU_STATE_TIN.elementAt(i)%>','<%=VSAP_APPROVAL_FLAG.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																			 '<%=VCONT_NO.elementAt(i)%>','<%=VPDF_INV_FLAG.elementAt(i)%>','<%=heading%>');"
																			style="<%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
																						color: orange;
																					<%} else{%>
																						color: brown;
																					<%}%>		
																				">													
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
																								'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=i%>','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>');" 
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
																			<i class="<%if(VEMAIL_SENT.elementAt(i).equals("Y")) {%>fa fa-envelope fa-2x<%}else{ %>fa fa-envelope-o fa-2x <%}%>" 
																			title="<%=(!VSIGN_PDF_TYPE.elementAt(i).toString().equals(""))?VSIGN_PDF_TYPE.elementAt(i).toString()+" Signed":""%>&#10;<%=VEMAIL_SENT_INFO.elementAt(i) %>"
																				onclick="openMailBody('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																					'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																					'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																					'<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																					'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																					'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=i%>','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>');"
																				style="<%if(VSIGN_PDF_TYPE.elementAt(i).equals("")){ %>
																				pointer-events: none; opacity: .65; color: gray;
																				<%} else{%>
																					<%if(VEMAIL_SENT.elementAt(i).equals("Y")) {%>
																					color:green;	
																					<%}else{ %>
																					color:blue;
																					<%} %>
																				<%}%>">
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
																		<td align="center" colspan="18"><%=utilmsg.infoMessage("<b>No Credit/Debit Note is Generated!</b>") %></td>
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