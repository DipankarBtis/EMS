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
	
	var pdf_type="O";
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
		var url = "../derivatives/frm_derivatives_CRDR_preparation.jsp?&u="+u+"&print_pdf_type="+pdf_type+
				"&month="+month+"&year="+year+"&accroid="+accroid;
	
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
	
	var pdf_type="O";
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
		var url = "../derivatives/frm_derivatives_CRDR_preparation.jsp?&u="+u+"&print_pdf_type="+pdf_type+
			"&month="+month+"&year="+year+"&accroid="+accroid+"&msg="+sub_msg+"&msg_type="+msg_type;
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
	
	var url = "frm_generate_derv_crdr.jsp?u="+u+"&month="+month+"&year="+year+"&operation=INSERT&accroid="+accroid+"&crdr_gen_type="+genType;
	
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

function openActivity1(counterparty_cd,bu_plant_seq,plant_seq,bu_st_cd,invoice_seq,ref_no,fin_yr,crdr_type,operation,accroid)
{
	var u = document.forms[0].u.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var url = "frm_generate_derv_crdr.jsp?counterparty_cd="+counterparty_cd+"&crdr_type="+crdr_type+"&invoice_seq="+invoice_seq+"&operation="+operation+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&accroid="+accroid+"&sel_inv_no="+ref_no+"&u="+u+"&month="+month+"&year="+year;
	
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

function openActivity3(counterparty_cd,bu_plant_seq,plant_seq,bu_st_cd,invoice_seq,ref_no,fin_yr,crdr_type,operation,accroid)
{
	var u = document.forms[0].u.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var url = "rpt_derv_view_chk_aprv_crdr_dtl.jsp?counterparty_cd="+counterparty_cd+"&crdr_type="+crdr_type+"&invoice_seq="+invoice_seq+"&operation="+operation+"&plant_seq="+plant_seq+
			"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&accroid="+accroid+"&sel_inv_no="+ref_no+"&u="+u+"&month="+month+"&year="+year+"&bu_plant_seq="+bu_plant_seq;
	
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

function doGenXML(counterparty_cd,bu_plant_seq,plant_seq,bu_st_cd,invoice_seq,ref_no,fin_yr,crdr_type,accroid,crdr_no,invoice_type,index,inv_pdf_flag,sap_approval_flag)
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
	
	var url = "../derivatives/rpt_view_derv_crdr_invoice_sap_approval.jsp?counterparty_cd="+counterparty_cd+"&crdr_type="+crdr_type+"&invoice_seq="+invoice_seq+"&plant_seq="+plant_seq+
	"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&accroid="+accroid+"&sel_inv_no="+ref_no+"&u="+u+"&sap_approval_flag="+sap_approval_flag+
	"&bu_plant_seq="+bu_plant_seq+"&crdr_inv_no="+crdr_no+"&invoice_type="+invoice_type+"&inv_pdf_flag="+inv_pdf_flag;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Derivatives CRDR SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Derivatives CRDR SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function printPDF(counterparty_cd,bu_plant_seq,plant_seq,bu_st_cd,invoice_seq,ref_no,fin_yr,crdr_type,accroid,crdr_no,invoice_type,index)
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
		var url = "pdf_derv_crdr_invoice.jsp?counterparty_cd="+counterparty_cd+"&crdr_type="+crdr_type+"&invoice_seq="+invoice_seq+"&plant_seq="+plant_seq+
		"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&accroid="+accroid+"&sel_inv_no="+ref_no+"&u="+u+"&print_pdf_type="+pdf_type+
		"&bu_plant_seq="+bu_plant_seq+"&crdr_inv_no="+crdr_no+"&invoice_type="+invoice_type;
	
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

function openAllPdfFile(fin_yr,bu_st_cd,inv_seq,inv_type)
{
	var url = "rpt_derv_crdr_view_all_pdf.jsp?financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&invoice_seq="+inv_seq+"&invoice_type="+inv_type;
	
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

function openMailBody(counterparty_cd,bu_plant_seq,plant_seq,bu_st_cd,invoice_seq,ref_no,fin_yr,crdr_type,accroid,crdr_no,invoice_type,index)
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
	
	var url = "frm_derivatives_crdr_sign_pdf_mail.jsp?counterparty_cd="+counterparty_cd+"&crdr_type="+crdr_type+"&invoice_seq="+invoice_seq+"&plant_seq="+plant_seq+
	"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&accroid="+accroid+"&sel_inv_no="+ref_no+"&u="+u+"&print_pdf_type="+pdf_type+
	"&bu_plant_seq="+bu_plant_seq+"&crdr_inv_no="+crdr_no+"&invoice_type="+invoice_type+"&mail_pdf_type="+pdf_type;
	
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

function setUploadParam(inv_seq,fin_yr,bu_state_tin,accroid,invoice_type)
{
	document.forms[0].file_invoice_seq.value=inv_seq;
	document.forms[0].file_financial_year.value=fin_yr;
	document.forms[0].file_bu_state_tin.value=bu_state_tin;
	document.forms[0].file_invoice_type.value=invoice_type;
	document.forms[0].invoice_title.value="P";
	//document.forms[0].file_invoice_type.value="";
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
	var file_bu_state_tin = document.forms[0].file_bu_state_tin.value;
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
	if(trim(file_bu_state_tin)=="")
	{
		msg+='Bu State missing!\n';
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
			document.forms[0].option.value="CRDR_PDF_UPLOAD";
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
<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Invoice" id="derv_inv" scope="request"></jsp:useBean>
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
String pdf_type=request.getParameter("print_pdf_type")==null?"O":request.getParameter("print_pdf_type");

if(month.length() == 1)
{
	month="0"+month; 
}

derv_inv.setCallFlag("EXISTING_CRDR_LIST");
derv_inv.setComp_cd(owner_cd);
derv_inv.setMonth(month);
derv_inv.setYear(year);
derv_inv.setPrint_pdf_type(pdf_type);
derv_inv.setView_pdf_type(pdf_type);
derv_inv.setMail_pdf_type(pdf_type);
derv_inv.init();

Vector VMST_INV_TYPE_FLG = derv_inv.getVMST_INV_TYPE_FLG();
Vector VMST_INV_TYPE = derv_inv.getVMST_INV_TYPE();
Vector VINDEX = derv_inv.getVINDEX();

Vector VCOUNTERPARTY_NM = derv_inv.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_CD = derv_inv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = derv_inv.getVCOUNTERPARTY_ABBR();
Vector VDEAL_MAPPING = derv_inv.getVDEAL_MAPPING();
Vector VPLANT_ABBR = derv_inv.getVPLANT_ABBR();
Vector VPLANT_SEQ = derv_inv.getVPLANT_SEQ();
Vector VBU_PLANT_SEQ = derv_inv.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = derv_inv.getVBU_PLANT_ABBR();
Vector VINVOICE_NO = derv_inv.getVINVOICE_NO();
Vector VINVOICE_SEQ = derv_inv.getVINVOICE_SEQ();
Vector VINV_CHECKED_FLG = derv_inv.getVINV_CHECKED_FLG();
Vector VINV_AUTHORIZED_FLG = derv_inv.getVINV_AUTHORIZED_FLG();
Vector VINV_APPROVED_FLG = derv_inv.getVINV_APPROVED_FLG();
Vector VINV_PDF_FLG = derv_inv.getVINV_PDF_FLG();
Vector VINV_PDF_TYPE = derv_inv.getVINV_PDF_TYPE();
Vector VINV_SAP_APPROVAL_FLAG = derv_inv.getVINV_SAP_APPROVAL_FLAG();
Vector VBU_STATE_TIN = derv_inv.getVBU_STATE_TIN();
Vector VINVOICE_EXIST = derv_inv.getVINVOICE_EXIST();
Vector VINVOICE_DT = derv_inv.getVINVOICE_DT();
Vector VINVOICE_TYPE = derv_inv.getVINVOICE_TYPE();
Vector VCONT_REF = derv_inv.getVCONT_REF();
Vector VCRDR_CRITERIA = derv_inv.getVCRDR_CRITERIA();
Vector VINV_FLAG = derv_inv.getVINV_FLAG();
Vector VREF_NO = derv_inv.getVREF_NO();
Vector VCREDIT_DEBIT_NO = derv_inv.getVCREDIT_DEBIT_NO();
Vector VINV_FLAG_NM = derv_inv.getVINV_FLAG_NM();
Vector VFIN_YEAR = derv_inv.getVFIN_YEAR();
Vector VDISP_CRDR_CRITERIA = derv_inv.getVDISP_CRDR_CRITERIA();

Vector VPDF_FILE_NAME = derv_inv.getVPDF_FILE_NAME();
Vector VPDF_FILE_PATH = derv_inv.getVPDF_FILE_PATH();
Vector VEMAIL_SENT = derv_inv.getVEMAIL_SENT();
Vector VEMAIL_SENT_INFO = derv_inv.getVEMAIL_SENT_INFO();
Vector VSIGN_PDF_TYPE = derv_inv.getVSIGN_PDF_TYPE();
Vector VFILE_UPLOAD_COUNT = derv_inv.getVFILE_UPLOAD_COUNT();

Vector VRE_PRINT_PDF = derv_inv.getVRE_PRINT_PDF();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//";
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;
String temp_pdf_type="";
%>
<body>
<%@ include file="../home/header.jsp"%>
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
					    	Derivatives Credit/Debit Generation
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
								<div class="col-md-4 col-sm-6 col-xs-6">
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
						  		<div class="col-md-4 col-sm-6 col-xs-6">
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
					</div>
				</div>
				<div class="card-body cdbody">
				<%int i=0,k=0,l=0,m=0;
				for(int j=0; j<VMST_INV_TYPE_FLG.size(); j++)
				{ 
					int index=Integer.parseInt(""+VINDEX.elementAt(j));;
					String heading = ""+VMST_INV_TYPE_FLG.elementAt(j);
					%>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="<%=heading%>">
   										<button name="sub_module_cd" class="accordion-button <%if(!accroid.equals(heading)){%>collapsed<%}%> accor-btn" type="button" 
   											data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="<%if(!accroid.equals(heading)){%>true<%}else{%>false<%} %>" aria-controls="collapse<%=j%>">
							    			Derivatives <%=VMST_INV_TYPE.elementAt(j)%>&nbsp;<font color="blue" style="background: white;padding: 2px 5px 4px 5px;border-radius: 30px;">(<%=index%> Items)</font>
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
																	<th class="tbser<%=j%>" rowspan="2">Counterparty</th>
																	<th class="tbser<%=j%>" rowspan="2">Contract No<br>[Contract/Trade Ref#]</th>
																	<!-- <th rowspan="2">Contract Period</th> -->
																	<th class="tbser<%=j%>" rowspan="2">Plant</th>
																	<th class="tbser<%=j%>" rowspan="2">Business Unit</th>
																	<!-- <th rowspan="2">Billing Cycle</th> -->
																	<th class="tbser<%=j%>" rowspan="2">Invoice Type</th>
																	<th class="tbser<%=j%>" rowspan="2">Credit/Debit Note Date</th>
																	<th class="tbser<%=j%>" rowspan="2">Criteria</th>
																	<th class="tbser<%=j%>" rowspan="2">Credit/Debit#<br>[Ref#]</th>
																	<th rowspan="2">View IRP</th>
																	<th rowspan="2">Modify IRP</th>
																	<th rowspan="2">IRP Check</th>					
																	<th rowspan="2">Fin Ops<br>Finalization</th>
																	<th rowspan="2">SAP XML</th>
																	<th>Print PDF</th>
																	<%if(heading.equals("R")){ %>
																		<th>Upload Received<br>Invoice</th>
																	<%} %>
																	<th>View PDF
																		<br>
																		<select class="form-select form-select-sm" name="pdf_type" id="pdf_type_<%=heading%>" style="width: 80px;" onchange="refresh('<%=heading%>');">
																    		<option value="O">SG</option>
																    		<%if(heading.equals("R")){ %>
																    		<option value="P">PG(RECV)</option>
																    		<%} %>
																    	</select>	
																    	<%if(heading.equals("I")){ 
																    		temp_pdf_type="O";
																    	}else if(heading.equals("R")){
																    		temp_pdf_type=pdf_type;
																    	} %>
																    	<script>document.getElementById("pdf_type_<%=heading%>").value="<%=temp_pdf_type%>"</script>
																    	<%-- <input type="hidden" name="pdf_type" id="pdf_type_<%=heading%>" value="O"> --%>
																	</th>
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
																		<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(i)%>">
																			<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
																		</td>
																		<td align="center">
																			<font color="blue"><%=VDEAL_MAPPING.elementAt(i)%></font>
																			
																			<br>[<%=VCONT_REF.elementAt(i)%>]
																		</td>
																		<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
																		<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
																		<td align="center"><%=VINV_FLAG_NM.elementAt(i)%></td>
																		<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
																		<td align="center"><%=VDISP_CRDR_CRITERIA.elementAt(i)%></td>
																		<td align="center">
																			<%=VCREDIT_DEBIT_NO.elementAt(i)%>
																			<br>
																			<font color="blue">[Ref# <%=VREF_NO.elementAt(i)%>]</font>
																		</td>
																		<td align="center">
																			<i class="fa fa-eye fa-2x" 
																			onclick="openActivity1('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VBU_PLANT_SEQ.elementAt(i) %>','<%=VPLANT_SEQ.elementAt(i) %>','<%=VBU_STATE_TIN.elementAt(i) %>'
																				,'<%=VINVOICE_SEQ.elementAt(i) %>','<%=VREF_NO.elementAt(i) %>','<%=VFIN_YEAR.elementAt(i) %>','<%=VINV_FLAG.elementAt(i) %>','VIEW','<%=heading %>');"
																			style="color:black;">																		
																			</i>					
																		</td>
																		<td align="center">
																			<i class="fa fa-pencil fa-2x" 
																			onclick="openActivity1('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VBU_PLANT_SEQ.elementAt(i) %>','<%=VPLANT_SEQ.elementAt(i) %>','<%=VBU_STATE_TIN.elementAt(i) %>'
																				,'<%=VINVOICE_SEQ.elementAt(i) %>','<%=VREF_NO.elementAt(i) %>','<%=VFIN_YEAR.elementAt(i) %>','<%=VINV_FLAG.elementAt(i) %>','MODIFY','<%=heading %>');"
																			style="<%if(VINV_CHECKED_FLG.elementAt(i).equals("Y")){ %>
																					pointer-events: none; opacity: .65;color: gray;
																					<%} else {%>
																						color:#ff9900;
																					<%}%>">
																			</i>
																		</td>
																		<td align="center">												
																			<i class="fa fa-stethoscope fa-2x"												
																			onclick="openActivity3('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VBU_PLANT_SEQ.elementAt(i) %>','<%=VPLANT_SEQ.elementAt(i) %>','<%=VBU_STATE_TIN.elementAt(i) %>'
																				,'<%=VINVOICE_SEQ.elementAt(i) %>','<%=VREF_NO.elementAt(i) %>','<%=VFIN_YEAR.elementAt(i) %>','<%=VINV_FLAG.elementAt(i) %>','CHECK','<%=heading %>');"
																			style="<%if(VINV_APPROVED_FLG.elementAt(i).equals("Y")){ %>
																					pointer-events: none; opacity: .65; color: gray;
																					<%} else {%>
																						color:#ff3399;
																					<%}%>">
																			</i>
																		</td>
																		<td align="center">	
																		<%if(VRE_PRINT_PDF.elementAt(i).equals("Y")){%>
																			<i class="fa fa-eye fa-2x"
																			onclick="openActivity3('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VBU_PLANT_SEQ.elementAt(i) %>','<%=VPLANT_SEQ.elementAt(i) %>','<%=VBU_STATE_TIN.elementAt(i) %>'
																				,'<%=VINVOICE_SEQ.elementAt(i) %>','<%=VREF_NO.elementAt(i) %>','<%=VFIN_YEAR.elementAt(i) %>','<%=VINV_FLAG.elementAt(i) %>','','<%=heading %>');"
																			style="color:black;">
																			</i>
																			<%}else{ %>	
																			<i class="fa fa-flag fa-2x"
																			onclick="openActivity3('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VBU_PLANT_SEQ.elementAt(i) %>','<%=VPLANT_SEQ.elementAt(i) %>','<%=VBU_STATE_TIN.elementAt(i) %>'
																				,'<%=VINVOICE_SEQ.elementAt(i) %>','<%=VREF_NO.elementAt(i) %>','<%=VFIN_YEAR.elementAt(i) %>','<%=VINV_FLAG.elementAt(i) %>','APPROVE','<%=heading %>');"
																			style="<%if(!VINV_PDF_FLG.elementAt(i).equals("") || !VINV_CHECKED_FLG.elementAt(i).equals("Y")){ %>
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
																			<i class="fa <%if(!VINV_SAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
																			onclick="doGenXML('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VBU_PLANT_SEQ.elementAt(i) %>','<%=VPLANT_SEQ.elementAt(i) %>','<%=VBU_STATE_TIN.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i) %>'
																				,'<%=VREF_NO.elementAt(i) %>','<%=VFIN_YEAR.elementAt(i) %>','<%=VINV_FLAG.elementAt(i) %>','<%=heading %>','<%=VCREDIT_DEBIT_NO.elementAt(i) %>','<%=VINVOICE_TYPE.elementAt(i) %>','<%=i %>','<%=VINV_PDF_FLG.elementAt(i) %>','<%=VINV_SAP_APPROVAL_FLAG.elementAt(i) %>');"
																			style="<%if(!VINV_SAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
																						color: orange;
																					<%} else{%>
																						color: brown;
																					<%}%>		
																				">													
																			</i>
																		<%} %>
																		</td>
																		<td align="center">
																			<i class="fa fa-print fa-2x" title="<%=VINV_PDF_TYPE.elementAt(i)%>"
																				onclick="printPDF('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VBU_PLANT_SEQ.elementAt(i) %>','<%=VPLANT_SEQ.elementAt(i) %>','<%=VBU_STATE_TIN.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i) %>'
																					,'<%=VREF_NO.elementAt(i) %>','<%=VFIN_YEAR.elementAt(i) %>','<%=VINV_FLAG.elementAt(i) %>','<%=heading %>','<%=VCREDIT_DEBIT_NO.elementAt(i) %>','<%=VINVOICE_TYPE.elementAt(i) %>','<%=i %>');"
																				style="<%if(VRE_PRINT_PDF.elementAt(i).equals("Y")){%>
																				color:#800000;
																				<%}else if(!VINV_APPROVED_FLG.elementAt(i).equals("Y") 
																							|| VINV_PDF_TYPE.elementAt(i).equals(pdf_type)){ %>
																				pointer-events: none; opacity: .65; color: gray;
																				<%} else{%>
																				color:#800000;
																				<%}%>">
																			</i>
																			<input type="hidden" name="all_pdf_type" id="all_pdf_type<%=i%>" value="<%=VINV_PDF_TYPE.elementAt(i)%>">
																		</td>
																		<%if(heading.equals("R")){ %>
																		<td align="center">	
																			<span class="position-relative">
																				<i class="fa fa-upload fa-2x" data-bs-toggle="modal" data-bs-target="#FileModal" 
																					onclick="setUploadParam('<%=VINVOICE_SEQ.elementAt(i)%>','<%=VFIN_YEAR.elementAt(i)%>','<%=VBU_STATE_TIN.elementAt(i)%>','<%=heading%>','<%=VINVOICE_TYPE.elementAt(i)%>')"
																					style="color:#00bfff;">	
																				</i>
																				<%if(!VFILE_UPLOAD_COUNT.elementAt(i).toString().equals("0")){ %>
																				<span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-primary">
																					<%=VFILE_UPLOAD_COUNT.elementAt(i)%>
																				</span>
																				<%} %>
																			</span>	
																		</td>
																		<%} %>
																		<td align="center">
																			<i class="fa fa-file-pdf-o fa-2x"
																				<%if(pdf_type.equals("All")){ %>
																				onclick="openAllPdfFile('<%=VFIN_YEAR.elementAt(i) %>',
																					'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i) %>')"
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
																		<%if(heading.equals("I")){ %>
																			<td align="center">
																				<i class="fa fa-envelope-o fa-2x" 
																					title="<%=(!VSIGN_PDF_TYPE.elementAt(i).toString().equals(""))?VSIGN_PDF_TYPE.elementAt(i).toString()+" Signed":""%>&#10;<%=VEMAIL_SENT_INFO.elementAt(i) %>"
																					onclick="openMailBody('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VBU_PLANT_SEQ.elementAt(i) %>','<%=VPLANT_SEQ.elementAt(i) %>','<%=VBU_STATE_TIN.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i) %>'
																						,'<%=VREF_NO.elementAt(i) %>','<%=VFIN_YEAR.elementAt(i) %>','<%=VINV_FLAG.elementAt(i) %>','<%=heading %>','<%=VCREDIT_DEBIT_NO.elementAt(i) %>','<%=VINVOICE_TYPE.elementAt(i) %>','<%=i %>');"
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
																		<%}else{ %>
																			<td align="center">
																			<i class="fa fa-envelope-o fa-2x" 
																				title="<%=VSIGN_PDF_TYPE.elementAt(i)%>"
																				onclick="openMailBody('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VBU_PLANT_SEQ.elementAt(i) %>','<%=VPLANT_SEQ.elementAt(i) %>','<%=VBU_STATE_TIN.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i) %>'
																					,'<%=VREF_NO.elementAt(i) %>','<%=VFIN_YEAR.elementAt(i) %>','<%=VINV_FLAG.elementAt(i) %>','<%=heading %>','<%=VCREDIT_DEBIT_NO.elementAt(i) %>','<%=VINVOICE_TYPE.elementAt(i) %>','<%=i %>');"
																				style="<%if(VFILE_UPLOAD_COUNT.elementAt(i).toString().equals("0")){ %>
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
																		<%} %>
																	</tr>
																	<%if(k==index){
																			i=i+1;
																			break;
																		} %>
																	<%} %>	
																<%}else{ %>
																	<tr>
																		<td align="center" colspan="<%if(heading.equals("R")){%>17<%}else{%>16<%}%>"><%=utilmsg.infoMessage("<b>No Credit/Debit Note is Generated!</b>") %></td>
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
					    			<input type="hidden" name="file_bu_state_tin">
					    			<input type="hidden" name="file_invoice_seq">
					    			<input type="hidden" name="file_financial_year">
					    			<input type="hidden" name="file_inv_type">
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