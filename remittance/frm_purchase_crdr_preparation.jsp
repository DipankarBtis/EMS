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
	
	var period_start_dt="";
	var period_end_dt="";
	
	var inv_title="SG";
	
	if(document.getElementById("inv_title_"+accroid)!=null && document.getElementById("inv_title_"+accroid)!=undefined)
	{
		inv_title = document.getElementById("inv_title_"+accroid).value;
	}
	
	var u = document.forms[0].u.value;
	
	var url = "../remittance/frm_purchase_crdr_preparation.jsp?&u="+u+"&month="+month+"&year="+year+
			"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+"&inv_title="+inv_title+
			"&accroid="+accroid;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
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
		var url = "../remittance/frm_purchase_crdr_preparation.jsp?&u="+u+"&pdf_type="+pdf_type+
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
function openCreditDebitInv(accroid,genType)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_generate_purchase_crdr.jsp?u="+u+"&month="+month+"&year="+year+"&operation=PREPARE&accroid="+accroid+"&crdr_gen_type="+genType;
	
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

var newWindow;
function openActivity1(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cargo_no,cont_type,plant_seq,bu,qty_mmbtu,activity_type,accroid,inv_flag,ref_no,crdr_no,sgpg_type,invoice_seq)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_generate_purchase_crdr.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cargo_no="+cargo_no+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+
			"&bu_unit="+bu+"&operation="+activity_type+"&invoice_seq="+invoice_seq+
			"&qty_mmbtu="+qty_mmbtu+"&accroid="+accroid+"&u="+u+"&inv_flag="+inv_flag+"&sel_inv_no="+ref_no+"&crdr_no="+crdr_no+"&sgpg_type="+sgpg_type;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"CR/DR Generation","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"CR/DR Generation","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function openActivity3(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cargo_no,cont_type,plant_seq,bu,qty_mmbtu,activity_type,accroid,inv_flag,ref_no,crdr_no,sgpg_type,invoice_seq)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_view_purchase_crdr_chk_aprv_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cargo_no="+cargo_no+"&cont_no="+cont_no+
	"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+
	"&bu_unit="+bu+"&operation="+activity_type+"&invoice_seq="+invoice_seq+"&activityFlag="+activity_type+
	"&qty_mmbtu="+qty_mmbtu+"&accroid="+accroid+"&u="+u+"&inv_flag="+inv_flag+"&sel_inv_no="+ref_no+"&crdr_no="+crdr_no+"&sgpg_type="+sgpg_type;
	
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

function doGenXML(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cargo_no,cont_type,plant_seq,bu,qty_mmbtu,accroid,inv_flag,ref_no,crdr_no,sgpg_type,invoice_seq,invoice_dt,sap_approval_flag,inv_pdf_flag,inv_raised_in)
{
	
	var u = document.forms[0].u.value;
	var invPg="";
	var purchase_type_flag="";
	if(inv_raised_in!="1")
	{
		invPg="Y";
	}
	if(sgpg_type=="SG")
	{
		purchase_type_flag="S";
	}
	else if(sgpg_type=="PG")
	{
		purchase_type_flag="P";
	}
	var url = "../purchase/rpt_view_crdr_purchase_sap_approval.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cargo_no="+cargo_no+"&cont_no="+cont_no+
	"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&invPg=Y"+"&purchase_type_flag="+purchase_type_flag+
	"&bu_unit="+bu+"&invoice_seq="+invoice_seq+"&invoice_dt="+invoice_dt+"&sap_approval_flag="+sap_approval_flag+"&inv_pdf_flag="+inv_pdf_flag+
	"&qty_mmbtu="+qty_mmbtu+"&accroid="+accroid+"&u="+u+"&inv_flag="+inv_flag+"&sel_inv_no="+ref_no+"&crdr_no="+crdr_no+"&sgpg_type="+sgpg_type;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"CRDR SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"CRDR SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function printPDF(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cargo_no,cont_type,plant_seq,bu,qty_mmbtu,accroid,inv_flag,ref_no,crdr_no,sgpg_type,invoice_seq,invoice_dt)
{
	var print_access = document.forms[0].print_access.value;
	
	var u = document.forms[0].u.value;
	
	if(print_access=="N")
	{
		alert("You don't have Print Rights!");	
	}
	else
	{
		var url = "pdf_purchase_crdr_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cargo_no="+cargo_no+"&cont_no="+cont_no+
		"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+
		"&bu_unit="+bu+"&invoice_seq="+invoice_seq+"&invoice_dt="+invoice_dt+
		"&qty_mmbtu="+qty_mmbtu+"&accroid="+accroid+"&u="+u+"&inv_flag="+inv_flag+"&sel_inv_no="+ref_no+"&crdr_no="+crdr_no+"&sgpg_type="+sgpg_type;
	
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

function openMailBody(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cargo_no,cont_type,plant_seq,bu,qty_mmbtu,accroid,inv_flag,ref_no,crdr_no,sgpg_type,invoice_seq,invoice_dt)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_purchase_crdr_invoice_mail.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cargo_no="+cargo_no+"&cont_no="+cont_no+
	"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+
	"&bu_unit="+bu+"&invoice_seq="+invoice_seq+"&invoice_dt="+invoice_dt+
	"&qty_mmbtu="+qty_mmbtu+"&accroid="+accroid+"&u="+u+"&inv_flag="+inv_flag+"&sel_inv_no="+ref_no+"&crdr_no="+crdr_no+"&sgpg_type="+sgpg_type;
	
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

function setUploadParam(inv_seq,fin_yr,cont_type,accroid,inv_flag)
{
	document.forms[0].file_invoice_seq.value=inv_seq;
	document.forms[0].file_financial_year.value=fin_yr;
	document.forms[0].file_contract_type.value=cont_type;
	document.forms[0].file_inv_flag.value=inv_flag;
	document.forms[0].invoice_title.value="PG_RECV";
	document.forms[0].file_invoice_type.value="";
	document.forms[0].accroid.value=accroid;
	
	document.forms[0].upload_inv_type.value="";
	
	document.getElementById('file_upload').value="";
}

function doUpload()
{
	var allowedExtensions =/(\.pdf)$/i;
	 
	var msg = "";
	var flag = true;
	 
	var fileInput =document.getElementById('file_upload');
	var filePath = fileInput.value;
	
	var file_invoice_seq = document.forms[0].file_invoice_seq.value;
	var file_financial_year = document.forms[0].file_financial_year.value;
	var file_contract_type = document.forms[0].file_contract_type.value;
	var invoice_title = document.forms[0].invoice_title.value;
    
	if(fileInput.value=="")
    {
		msg+='Please Select any File!\n';
        fileInput.value = '';
        flag = false;
    }
	else if(!allowedExtensions.exec(filePath)) 
    {
		msg+='Upload file in .pdf format only!\n';
        fileInput.value = '';
        flag = false;
    }
	
	if(trim(file_invoice_seq)=="")
	{
		msg+='Invoice Seq# missing!\n';
       	flag = false;
	}
	if(trim(file_financial_year)=="")
	{
		msg+='Financial Year missing!\n';
       	flag = false;
	}
	if(trim(file_contract_type)=="")
	{
		msg+='Contract Type missing!\n';
       	flag = false;
	}
	if(trim(invoice_title)=="")
	{
		msg+='Invoice Title missing!\n';
       	flag = false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to upload file?");
		if(a)
	    {
			document.forms[0].option.value="INVOICE_PDF_UPLOAD";
			document.getElementById("loading").style.visibility = "visible";
		  	document.forms[0].submit();	
		}
	}
 	else
 	{
 		alert(msg);
 	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.remittance.DataBean_Remittance" id="pur_inv" scope="request"></jsp:useBean>
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
String inv_title=request.getParameter("inv_title")==null?"SG":request.getParameter("inv_title");

if(month.length() == 1)
{
	month="0"+month; 
}

pur_inv.setCallFlag("EXISTING_CRDR_LIST");
pur_inv.setComp_cd(owner_cd);
pur_inv.setMonth(month);
pur_inv.setYear(year);
pur_inv.setInv_title(inv_title);
pur_inv.init();

Vector VINVOICE_LIST_ABBR = new Vector();//pur_inv.getVINVOICE_LIST_ABBR();
Vector VREMITTANCE_LIST_NAME = pur_inv.getVREMITTANCE_LIST_NAME();
Vector VINDEX = pur_inv.getVINDEX();

Vector VCOUNTERPTY_CD = pur_inv.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = pur_inv.getVCOUNTERPTY_ABBR();
Vector VCONT_NO = pur_inv.getVCONT_NO();
Vector VCONT_REV_NO = pur_inv.getVCONT_REV_NO();
Vector VAGMT_NO = pur_inv.getVAGMT_NO();
Vector VAGMT_REV_NO = pur_inv.getVAGMT_REV_NO();
Vector VSTART_DT = pur_inv.getVSTART_DT();
Vector VEND_DT = pur_inv.getVEND_DT();
Vector VCONT_NAME = pur_inv.getVCONT_NAME();
Vector VCONT_REF_NO = pur_inv.getVCONT_REF_NO();
Vector VCONTRACT_TYPE = pur_inv.getVCONTRACT_TYPE();
Vector VPLANT_SEQ = pur_inv.getVPLANT_SEQ();
Vector VPLANT_ABBR = pur_inv.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = pur_inv.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = pur_inv.getVBU_PLANT_ABBR();
Vector VDEAL_NO = pur_inv.getVDEAL_NO();
Vector VPERIOD_START_DT = pur_inv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = pur_inv.getVPERIOD_END_DT();
Vector VINVOICE_NO = pur_inv.getVINVOICE_NO();
Vector VSTATUS = pur_inv.getVSTATUS();
Vector VBILLING_FREQ_FLAG = pur_inv.getVBILLING_FREQ_FLAG();
Vector VBILLING_FREQ_NM = pur_inv.getVBILLING_FREQ_NM();
Vector VAPPROVE_FLAG_CHECK = pur_inv.getVAPPROVE_FLAG_CHECK();
Vector VCHECK_FLAG_CHECK = pur_inv.getVCHECK_FLAG_CHECK();
Vector VAUTHORIZ_FLAG_CHECK = pur_inv.getVAUTHORIZ_FLAG_CHECK();
Vector VIS_SUBMITTED = pur_inv.getVIS_SUBMITTED();
Vector VINVOICE_SEQ = pur_inv.getVINVOICE_SEQ();
Vector VFINANCIAL_YEAR = pur_inv.getVFINANCIAL_YEAR();
Vector VFILE_UPLOAD_COUNT = pur_inv.getVFILE_UPLOAD_COUNT();
Vector VUPLOADED_FILE_NAME = pur_inv.getVUPLOADED_FILE_NAME();
Vector VAPPROVE_INVOICE_FLAG = pur_inv.getVAPPROVE_INVOICE_FLAG();
Vector VPDF_INV_FLAG = pur_inv.getVPDF_INV_FLAG();
Vector VSAP_APPROVAL_FLAG = pur_inv.getVSAP_APPROVAL_FLAG();
Vector VPAYMENT_TYPE_FLAG = pur_inv.getVPAYMENT_TYPE_FLAG();
Vector VREMITTANCE_NO = pur_inv.getVREMITTANCE_NO();

Vector VSPLIT_FLAG = pur_inv.getVSPLIT_FLAG();
Vector VSPLIT_VALUE = pur_inv.getVSPLIT_VALUE();
Vector VALLOC_QTY = pur_inv.getVALLOC_QTY();
Vector VCARGO_NO = pur_inv.getVCARGO_NO();
Vector VBOE_NO = pur_inv.getVBOE_NO();
Vector VBOE_NM = pur_inv.getVBOE_NM();
Vector VINV_FLAG = pur_inv.getVINV_FLAG();
Vector VSHIP_NAME = pur_inv.getVSHIP_NAME();

Vector VCRDR_CRITERIA = pur_inv.getVCRDR_CRITERIA();
Vector VCREDIT_DEBIT_NO = pur_inv.getVCREDIT_DEBIT_NO();
Vector VREF_NO = pur_inv.getVREF_NO();
Vector VINVOICE_TYPE = pur_inv.getVINVOICE_TYPE();
Vector VINVOICE_TYPE_NM = pur_inv.getVINVOICE_TYPE_NM();
Vector VINVOICE_DT = pur_inv.getVINVOICE_DT();
Vector VSGPG_TYPE = pur_inv.getVSGPG_TYPE();
Vector VEMAIL_SENT = pur_inv.getVEMAIL_SENT();
Vector VINV_RAISED_IN = pur_inv.getVINV_RAISED_IN();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//";
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd+"//purchase//invoice//";
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Remittance" enctype="multipart/form-data">
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
					    	Purchase Credit/Debit Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="form-group row">
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
								<div class="col-md-2 col-sm-6 col-xs-6">
									<!-- <div class="d-flex justify-content-center"> -->
										<div class="form-group row">
											<div class="col">
												<!-- <label class="form-label"><b>Invoice Type</b></label> -->
												<div class="btn-group">
													<label class="btn btn-outline-secondary subbtngrp" onclick="openCreditDebitInv('<%//=accro2%>','CRDR');"><i class="fa fa-plus-circle"></i>&nbsp;Create New Credit/Debit</label>
												</div>
								  			</div>
										</div>
									<!-- </div> -->
						  		</div>
						  	</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%int i=0,k=0,l=0,m=0;
				for(int j=0; j<VREMITTANCE_LIST_NAME.size(); j++)
				{ 
					int index=Integer.parseInt(""+VINDEX.elementAt(j));
					String heading = ""+VREMITTANCE_LIST_NAME.elementAt(j);
					%>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="<%=heading%>">
   										<button name="sub_module_cd" class="accordion-button <%if(!accroid.equals(heading)){%>collapsed<%}%> accor-btn" type="button" 
   											data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="<%if(!accroid.equals(heading)){%>true<%}else{%>false<%} %>" aria-controls="collapse<%=j%>">
							    			<%=VREMITTANCE_LIST_NAME.elementAt(j)%>&nbsp;<font color="blue" style="background: white;padding: 2px 5px 4px 5px;border-radius: 30px;">(<%=index %> Items)</font>
							      		</button>	
							    	</h2>
									<div id="collapse<%=j%>" class="accordion-collapse collapse <%if(accroid.equals(heading)){%>show<%}%>" aria-labelledby="<%=heading%>">
							      		<div class="accordion-body accor-body">
											<div class="row">
												<div class="col-md-12 col-sm-12 col-xs-12">
							    					<div align="right">
							    						<input class="form-control form-control-sm" type="text" id="globalSearch" onkeyup="globalSearchTable(this,'<%=j %>')" placeholder="Search.." style="width:200px"/>
							    					</div>
						    					</div>
												<div class="col-md-12 col-sm-12 col-xs-12">
												
													<div class="table-responsive">
														<table class="table table-bordered table-hover ems_sorttbl" id="ems_table<%=j%>">
															<thead id="ems_tbsort<%=j%>">
																<tr>
																	<th class="ems_thsort<%=j%>">Trader</th>
																	<th class="ems_thsort<%=j%>">Contract No<br>[Contract/Trade Ref#]</th>
																	<th class="ems_thsort<%=j%>">Billing Period</th>
																	<th class="ems_thsort<%=j%>">Plant</th>
																	<th class="ems_thsort<%=j%>">Business Unit</th>
																	<th class="ems_thsort<%=j%>">Invoice#</th>
																	<th class="ems_thsort<%=j%>">Invoice Type</th>
																	<th class="ems_thsort<%=j%>">Credit/Debit Note Date</th>
																	<th class="ems_thsort<%=j%>">Criteria</th>
																	<th class="ems_thsort<%=j%>">Credit/Debit#[Ref#]</th>
																	<th class="tbser<%=j%>">Generate IRP</th>
																	<th class="tbser<%=j%>">IRP Check</th>
																	<th class="tbser<%=j%>">IRP Approval</th>
																	<th class="tbser<%=j%>">Fin Ops Finalization</th>
																	<th class="tbser<%=j%>">SAP<br>XML</th>
																	<th class="tbser<%=j%>">Print PDF</th>
																	<th class="tbser<%=j%>">Upload Received<br>Invoice</th>											
																	<th >View PDF
																		<br>
																		<select class="form-select form-select-sm" name="inv_title" id="inv_title_<%=heading%>" style="width: 80px;" onchange="refresh('<%=heading%>');">
																    		<option value="">--Select--</option>
																    		<option value="SG">SG</option>
																    		<option value="PG">PG</option>
																    		<option value="PG_RECV">PG(RECV)</option>
																    	</select>	
																    	<script>document.getElementById("inv_title_<%=heading%>").value="<%=inv_title%>"</script>
																	</th>
																	<th class="tbser<%=j%>">Send Mail</th>
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
																	<td align="center">
																		<%=VCOUNTERPTY_ABBR.elementAt(i)%>
																	</td>
																	<td align="center" title="Billing Period Alloc MMBTU : <%=VALLOC_QTY.elementAt(i)%>">
																		<font color="blue"><%=VDEAL_NO.elementAt(i)%></font> <%if(VSPLIT_FLAG.elementAt(i).equals("Y")){ %><font style="background:#ff99ff;">[Split <%=VSPLIT_VALUE.elementAt(i) %>%]</font><%} %>
																		
																		<br>[<%=VCONT_REF_NO.elementAt(i)%>]
																	</td>
																	<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
																	<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
																	<td align="center"><%=VBU_PLANT_ABBR.elementAt(i) %></td>
																	<td align="center"><%=VINVOICE_TYPE_NM.elementAt(i)%></td>
																	<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
																	<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
																	<td align="center"><%=VCRDR_CRITERIA.elementAt(i)%></td>
																	<td align="center">
																		<%=VCREDIT_DEBIT_NO.elementAt(i)%>
																		<br>
																		<font color="blue">[Ref# <%=VREF_NO.elementAt(i)%>]</font>
																	</td>
																	<td align="center">
																		<%if(VPDF_INV_FLAG.elementAt(i).equals("Y") || VCHECK_FLAG_CHECK.elementAt(i).equals("Y")){ %>
																		<i class="fa fa-eye fa-2x"
																		onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																				'<%=VCARGO_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>',
																				'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VALLOC_QTY.elementAt(i)%>','VIEW','<%=heading%>','<%=VINVOICE_TYPE.elementAt(i) %>','<%=VREF_NO.elementAt(i)%>','<%=VCREDIT_DEBIT_NO.elementAt(i) %>',
																				'<%=VSGPG_TYPE.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i) %>')"
																		style="color:black;">
																		</i>
																		<%}else{ %>
																		<i class="fa fa-pencil fa-2x"
																		onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																				'<%=VCARGO_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>',
																				'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VALLOC_QTY.elementAt(i)%>','MODIFY','<%=heading%>','<%=VINVOICE_TYPE.elementAt(i) %>','<%=VREF_NO.elementAt(i)%>','<%=VCREDIT_DEBIT_NO.elementAt(i) %>',
																				'<%=VSGPG_TYPE.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i) %>')"
																		style="<%if(VAPPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
																			pointer-events: none; opacity: .65; color: gray;
																				<%} else{%>
																				color:#ff9900;
																				<%}%>">
																		</i>
																		<%} %>
																	</td>
																	<td align="center">
																	<%if(VCONTRACT_TYPE.elementAt(i).equals("N") && VINV_FLAG.elementAt(i).equals("PF") && VCHECK_FLAG_CHECK.elementAt(i).equals("Y")) { %>
																		<i class="fa fa-stethoscope fa-2x" style="pointer-events: none; opacity: .65; color: gray;"></i>
																	<%}else{ %>
																		<i class="fa fa-stethoscope fa-2x"
																		onclick="openActivity3('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																				'<%=VCARGO_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>',
																				'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VALLOC_QTY.elementAt(i)%>','CHECK','<%=heading%>','<%=VINVOICE_TYPE.elementAt(i) %>','<%=VREF_NO.elementAt(i)%>','<%=VCREDIT_DEBIT_NO.elementAt(i) %>',
																				'<%=VSGPG_TYPE.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i) %>')"
																		style="<%if(VIS_SUBMITTED.elementAt(i).equals("N") || VAPPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
																			pointer-events: none; opacity: .65; color: gray;
																				<%} else{%>
																				color:#ff3399;
																				<%}%>">	
																		</i>
																	<%} %>
																	</td>
																	<td align="center">
																	<%if(VCONTRACT_TYPE.elementAt(i).equals("N") && VINV_FLAG.elementAt(i).equals("PF") && VCHECK_FLAG_CHECK.elementAt(i).equals("Y")) { %>
																		<i class="fa fa-thumbs-o-up fa-2x" style="pointer-events: none; opacity: .65; color: gray;"></i>
																	<%}else{ %>
																		<i class="fa fa-thumbs-o-up fa-2x"
																		onclick="openActivity3('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																				'<%=VCARGO_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>',
																				'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VALLOC_QTY.elementAt(i)%>','AUTHORIZE','<%=heading%>','<%=VINVOICE_TYPE.elementAt(i) %>','<%=VREF_NO.elementAt(i)%>','<%=VCREDIT_DEBIT_NO.elementAt(i) %>',
																				'<%=VSGPG_TYPE.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i) %>')"
																		style="<%if(VCHECK_FLAG_CHECK.elementAt(i).equals("N") || VAPPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
																					pointer-events: none; opacity: .65; color: gray;
																				<%} else{%>
																				color:blue;
																				<%}%>">		
																		</i>
																	<%} %>
																	</td>
																	<td align="center">
																		<i class="fa fa-flag fa-2x"
																		onclick="openActivity3('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																				'<%=VCARGO_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>',
																				'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VALLOC_QTY.elementAt(i)%>','APPROVE','<%=heading%>','<%=VINVOICE_TYPE.elementAt(i) %>','<%=VREF_NO.elementAt(i)%>','<%=VCREDIT_DEBIT_NO.elementAt(i) %>',
																				'<%=VSGPG_TYPE.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i) %>')"
																		style="<%if(VAUTHORIZ_FLAG_CHECK.elementAt(i).equals("N") || VPDF_INV_FLAG.elementAt(i).equals("Y")){ %>
																				pointer-events: none; opacity: .65; color: gray;
																			<%} else{%>
																			color:#00e600;
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
																		<%if(heading.equals("PROFRM_HEAD")){ %>
																			<span class="fa-stack fa-lg">
																			  <i class="fa fa-eye fa-stack-1x"></i>
																			  <i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
																			</span>
																		<%}else{%>
																			<i class="fa fa-eye fa-2x" 
																			onclick="doGenXML('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																				'<%=VCARGO_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>',
																				'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VALLOC_QTY.elementAt(i)%>','<%=heading%>','<%=VINVOICE_TYPE.elementAt(i) %>','<%=VREF_NO.elementAt(i)%>','<%=VCREDIT_DEBIT_NO.elementAt(i) %>',
																				'<%=VSGPG_TYPE.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i) %>','<%=VINVOICE_DT.elementAt(i) %>','<%=VSAP_APPROVAL_FLAG.elementAt(i) %>','<%=VPDF_INV_FLAG.elementAt(i) %>','<%=VINV_RAISED_IN.elementAt(i) %>');"
																			style="<%if(VIS_SUBMITTED.elementAt(i).equals("N")){ %>
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
																	<%}%>
																	</td>
																	<td align="center">																			
																		<i class="fa fa-print fa-2x"
																		onclick="printPDF('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																				'<%=VCARGO_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>',
																				'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VALLOC_QTY.elementAt(i)%>','<%=heading%>','<%=VINVOICE_TYPE.elementAt(i) %>','<%=VREF_NO.elementAt(i)%>','<%=VCREDIT_DEBIT_NO.elementAt(i) %>',
																				'<%=VSGPG_TYPE.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i) %>','<%=VINVOICE_DT.elementAt(i) %>')" 
																		style="<%if(VAPPROVE_FLAG_CHECK.elementAt(i).equals("N") || VAPPROVE_INVOICE_FLAG.elementAt(i).equals("") || VPDF_INV_FLAG.elementAt(i).equals("Y")){ %>
																			pointer-events: none; opacity: .65; color: gray;
																		<%} else{%>
																		color:#800000;
																		<%}%>">												
																		</i>												
																	</td>
																	<td align="center">	
																	<%if(VCONTRACT_TYPE.elementAt(i).equals("N") && VINV_FLAG.elementAt(i).equals("PF")){ %>
																		<i class="fa fa-upload fa-2x" style="pointer-events: none; opacity: .65; color: gray;"></i>
																	<%}else{ %>
																		<span class="position-relative">
																			<i class="fa fa-upload fa-2x" data-bs-toggle="modal" data-bs-target="#FileModal" 
																				onclick="setUploadParam('<%=VINVOICE_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=heading%>','<%=VINVOICE_TYPE.elementAt(i)%>')"
																				style="<%if(VIS_SUBMITTED.elementAt(i).equals("N")){ %>
																					pointer-events: none; opacity: .65; color: gray;
																				<%} else{%>
																				color:#00bfff;
																				<%}%>">	
																			</i>
																			<%if(!VFILE_UPLOAD_COUNT.elementAt(i).toString().equals("0")){ %>
																			<span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-primary">
																				<%=VFILE_UPLOAD_COUNT.elementAt(i)%>
																			</span>
																			<%} %>
																		</span>	
																	<%} %>											
																	</td>
																	<td align="center">																						
																		<i class="fa fa-file-pdf-o fa-2x" title="<%=VUPLOADED_FILE_NAME.elementAt(i)%>"
																		onclick="openPdfFile('<%=file_url%><%=VUPLOADED_FILE_NAME.elementAt(i)%>')"
																		style="<%if(VUPLOADED_FILE_NAME.elementAt(i).equals("")){ %>
																				pointer-events: none; opacity: .65; color: gray;
																			<%} else{%>
																			color:red;
																			<%}%>">
																		</i>								
																	</td>
																	<td align="center">
																		<i class="<%if(VEMAIL_SENT.elementAt(i).equals("Y")) {%>fa fa-envelope fa-2x<%}else{ %>fa fa-envelope-o fa-2x <%}%>" 
																				title="<%if(VEMAIL_SENT.elementAt(i).equals("Y")){%>Email Sent!<%}%>"
																			onclick="openMailBody('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																				'<%=VCARGO_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>',
																				'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VALLOC_QTY.elementAt(i)%>','<%=heading%>','<%=VINVOICE_TYPE.elementAt(i) %>','<%=VREF_NO.elementAt(i)%>','<%=VCREDIT_DEBIT_NO.elementAt(i) %>',
																				'<%=VSGPG_TYPE.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i) %>','<%=VINVOICE_DT.elementAt(i) %>');"
																			style="<%if(!VFILE_UPLOAD_COUNT.elementAt(i).toString().equals("0") && VPDF_INV_FLAG.elementAt(i).equals("Y")){ %>
																			<%if(VEMAIL_SENT.elementAt(i).equals("Y")) {%>
																				color:green;	
																				<%}else{ %>
																				color:blue;
																				<%} %>
																			<%} else{%>
																			pointer-events: none; opacity: .65; color: gray;
																			<%}%>">
																		</i>
																	</td>																						
																</tr>
																	<%if(k==index){
																			i=i+1;
																			break;
																		} %>
																	<%} %>	
																<%}else{ %>
																	<tr>
																		<td align="center" colspan="<%if(heading.equals("LTCORA_INV_HEAD")) {%>18<%}else{%>19<%}%>"><%=utilmsg.infoMessage("<b>No Credit/Debit Note is Generated!</b>") %></td>
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

<div class="modal fade" id="FileModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Upload Invoice PDF
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Select File</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<div class="input-group input-group-sm" >
					    				<input type="file" class="form-control form-control-sm" name="file_upload" id="file_upload">
					    				<span class="input-group-text"><i class="fa fa-upload fa-lg"></i></span>
				      				</div>
					    			<input type="hidden" name="file_contract_type">
					    			<input type="hidden" name="file_invoice_seq">
					    			<input type="hidden" name="file_financial_year">
					    			<input type="hidden" name="file_inv_flag">
					    			<input type="hidden" name="invoice_title">
					    			<input type="hidden" name="file_invoice_type">
					    			<input type="hidden" name="upload_inv_type">
					    		</div>
				  			</div>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
					<%if(write_access.equals("Y")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doUpload();">
					<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
					<%} %>
				</div>
      		</div>
      	</div>
    </div>
</div>

<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="option" value="">

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