<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var inv_title_S="";
	var inv_title_B="";
	var fflow_inv_title="";
	
	if(document.getElementById("inv_title_S")!=null && document.getElementById("inv_title_S")!=undefined)
	{
		inv_title_S = document.getElementById("inv_title_S").value;
	}
	if(document.getElementById("inv_title_B")!=null && document.getElementById("inv_title_B")!=undefined)
	{
		inv_title_B = document.getElementById("inv_title_B").value;
	}
	
	if(document.getElementById("fflow_inv_title")!=null && document.getElementById("fflow_inv_title")!=undefined)
	{
		fflow_inv_title = document.getElementById("fflow_inv_title").value;
	}
	var flag=true;
	var msg="";
	
	var count = compareDate(from_dt,to_dt);
	if(parseInt(count) == 1)
	{
		msg+="From Date should be less or equal To Date!";
		flag=false;
	}
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "frm_gx_transaction_invoice_preparation.jsp?counterparty_cd="+counterparty_cd+"&from_dt="+from_dt+"&to_dt="+to_dt+
				"&inv_title_S="+inv_title_S+"&inv_title_B="+inv_title_B+"&fflow_inv_title="+fflow_inv_title+
				"&u="+u;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}

var newWindow;
function openActivity1(counterparty_cd,gx_counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,gx_bu,bu,invoice_type,activity_type)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_prepare_gx_transaction_invoice.jsp?counterparty_cd="+counterparty_cd+"&gx_counterparty_cd="+gx_counterparty_cd+
			"&contract_type="+cont_type+"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+
			"&gx_bu_unit="+gx_bu+"&bu_unit="+bu+"&activity_type="+activity_type+"&invoice_type="+invoice_type+
			"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare Seller Payment","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare Seller Payment","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
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
	var file_gx_counterparty_cd = document.forms[0].file_gx_counterparty_cd.value;
	var invoice_title = document.forms[0].invoice_title.value;
	var file_invoice_type = document.forms[0].file_invoice_type.value;
    
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
	if(trim(file_invoice_type)=="")
	{
		msg+='Invoice Type missing!\n';
       	flag = false;
	}
	if(trim(file_financial_year)=="")
	{
		msg+='Financial Year missing!\n';
       	flag = false;
	}
	if(trim(file_gx_counterparty_cd)=="")
	{
		msg+='GX Counterparty Cd missing!\n';
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

function setUploadParam(inv_seq,fin_yr,gx_counterparty_cd,invoice_type,cont_type)
{
	document.forms[0].file_invoice_seq.value=inv_seq;
	document.forms[0].file_financial_year.value=fin_yr;
	document.forms[0].file_gx_counterparty_cd.value=gx_counterparty_cd;
	document.forms[0].invoice_title.value="PG_RECV";
	document.forms[0].file_invoice_type.value=invoice_type;
	document.forms[0].file_contract_type.value=cont_type;
	
	document.forms[0].upload_inv_type.value="";
	
	document.getElementById('file_upload').value="";
}

function openPdfFile(url)
{
	window.open(url);
}

function printPDF(counterparty_cd,gx_counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,gx_bu_unit,bu_unit,fin_yr,inv_seq,inv_flag,invoice_type)
{
	var u = document.forms[0].u.value;
	
	var url = "pdf_gx_invoice_remittance.jsp?inv_type="+inv_flag+"&counterparty_cd="+counterparty_cd+"&gx_counterparty_cd="+gx_counterparty_cd+
		"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&contract_type="+cont_type+
		"&gx_bu_unit="+gx_bu_unit+"&bu_unit="+bu_unit+"&financial_year="+fin_yr+"&invoice_seq="+inv_seq+"&invoice_type="+invoice_type;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare Seller Payment","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare Seller Payment","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
}

function openMailBody(counterparty_cd,gx_counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,gx_bu,bu,fin_yr,inv_seq,invoice_type,mail_inv_type)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_gx_invoice_remittance_mail.jsp?counterparty_cd="+counterparty_cd+"&gx_counterparty_cd="+gx_counterparty_cd+
			"&contract_type="+cont_type+"&cont_no="+cont_no+"&invoice_type="+invoice_type+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&gx_bu_unit="+gx_bu+"&bu_unit="+bu+
			"&financial_year="+fin_yr+"&invoice_seq="+inv_seq+"&mail_inv_type="+mail_inv_type+
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
		sap_approval_flag, gx_counterparty_cd,bu_plant_seq, agmt_no, cont_no, inv_pdf_flag)
{
	var url = "rpt_view_gx_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
	"&contract_type="+contract_type+"&type_flag="+type_flag+"&invoice_type="+invoice_type+"&gx_counterparty_cd="+gx_counterparty_cd+
	"&counterparty_cd="+couterpty_cd+"&invoice_no="+invoice_no+"&sap_approval_flag="+sap_approval_flag+
	"&bu_plant_seq="+bu_plant_seq+"&agmt_no="+agmt_no+"&cont_no="+cont_no+"&inv_pdf_flag="+inv_pdf_flag;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Gas Exchange SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Gas Exchange SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}


function openFreeFlowInv()
{
	var activity_type="PREPARE";
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gx_f_flow_invoice.jsp?u="+u+"&opration=INSERT&activity_type="+activity_type;

	//"&month="+month+"&year="+year+

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

function openFreeFlowInv_Modify(counterparty_cd,bu_unit,gx_counterparty_cd,gx_bu_unit,mapping_id,inv_seq,inv_no,inv_type,finan_yr,activity_type)
{
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gx_f_flow_invoice.jsp?u="+u+"&counterparty_cd="+counterparty_cd+"&bu_unit="+bu_unit+"&gx_counterparty_cd="+gx_counterparty_cd+
			"&gx_bu_unit="+gx_bu_unit+"&mapping_id="+mapping_id+"&inv_no="+inv_no+"&inv_seq="+inv_seq+
			"&financial="+finan_yr+"&invoice_type="+inv_type+
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

function fflowPrintPDF(counterparty_cd, bu_unit, gx_counterparty_cd, gx_bu_unit, mapping_id,cont_type, invoice_type,invoice_no,invoice_seq,financial_year)
{
	var is_print="1";
	
	var url = "pdf_gx_f_flow_invoice.jsp?counterparty_cd="+counterparty_cd+"&gx_counterparty_cd="+gx_counterparty_cd+"&bu_unit="+bu_unit+"&gx_bu_unit="+gx_bu_unit+
		"&is_print="+is_print+"&contract_type="+cont_type+"&mapping_id="+mapping_id+
		"&invoice_type="+invoice_type+"&inv_no="+invoice_no+"&inv_seq="+invoice_seq+"&financial="+financial_year;
	
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

function setFFlowUploadParam(inv_seq,fin_yr,cont_type,inv_type, gx_counterparty_cd)
{
	document.forms[0].file_invoice_seq.value=inv_seq;
	document.forms[0].file_financial_year.value=fin_yr;
	document.forms[0].file_contract_type.value=cont_type;
	document.forms[0].invoice_title.value="PG_RECV";
	document.forms[0].file_invoice_type.value=inv_type;
	document.forms[0].file_gx_counterparty_cd.value=gx_counterparty_cd;

	document.forms[0].upload_inv_type.value="FFLOW";
	
	document.getElementById('file_upload').value="";
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gx.DataBean_Gx_Invoice" id="gx_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String firstDate="01/"+sysdate.substring(3, sysdate.length());

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?firstDate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

String inv_title_S=request.getParameter("inv_title_S")==null?"":request.getParameter("inv_title_S");
String inv_title_B=request.getParameter("inv_title_B")==null?"":request.getParameter("inv_title_B");
String fflow_inv_title=request.getParameter("fflow_inv_title")==null?"":request.getParameter("fflow_inv_title");

gx_inv.setCallFlag("GX_INVOICE_PREPARATION");
gx_inv.setComp_cd(owner_cd);
gx_inv.setCounterparty_cd(counterparty_cd);
gx_inv.setFrom_dt(from_dt);
gx_inv.setTo_dt(to_dt);
gx_inv.setInv_title_S(inv_title_S);
gx_inv.setInv_title_B(inv_title_B);
gx_inv.setFflow_inv_title(fflow_inv_title);

gx_inv.init();

Vector VMST_COUNTERPARTY_CD = gx_inv.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = gx_inv.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = gx_inv.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPTY_CD = gx_inv.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = gx_inv.getVCOUNTERPTY_ABBR();
Vector VGX_COUNTERPTY_CD = gx_inv.getVGX_COUNTERPTY_CD();
Vector VGX_COUNTERPTY_ABBR = gx_inv.getVGX_COUNTERPTY_ABBR();
Vector VCONT_NO = gx_inv.getVCONT_NO();
Vector VCONT_REV_NO = gx_inv.getVCONT_REV_NO();
Vector VAGMT_NO = gx_inv.getVAGMT_NO();
Vector VAGMT_REV_NO = gx_inv.getVAGMT_REV_NO();
Vector VSTART_DT = gx_inv.getVSTART_DT();
Vector VEND_DT = gx_inv.getVEND_DT();
Vector VCONT_NAME = gx_inv.getVCONT_NAME();
Vector VCONT_REF_NO = gx_inv.getVCONT_REF_NO();
Vector VCONTRACT_TYPE = gx_inv.getVCONTRACT_TYPE();
Vector VGX_BU_PLANT_SEQ = gx_inv.getVGX_BU_PLANT_SEQ();
Vector VGX_BU_PLANT_ABBR = gx_inv.getVGX_BU_PLANT_ABBR();
Vector VBU_PLANT_SEQ = gx_inv.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = gx_inv.getVBU_PLANT_ABBR();
Vector VDEAL_NO = gx_inv.getVDEAL_NO();
Vector VAGMT_BASE = gx_inv.getVAGMT_BASE();

Vector VINVOICE_NO = gx_inv.getVINVOICE_NO();
Vector VINVOICE_SEQ = gx_inv.getVINVOICE_SEQ();
Vector VINVOICE_TYPE = gx_inv.getVINVOICE_TYPE();
Vector VFINANCIAL_YEAR = gx_inv.getVFINANCIAL_YEAR();
Vector VSTATUS = gx_inv.getVSTATUS();
Vector VAPPROVE_INVOICE_FLAG = gx_inv.getVAPPROVE_INVOICE_FLAG();
Vector VPDF_INV_FLAG = gx_inv.getVPDF_INV_FLAG();
Vector VAPPROVE_FLAG_CHECK = gx_inv.getVAPPROVE_FLAG_CHECK();
Vector VCHECK_FLAG_CHECK = gx_inv.getVCHECK_FLAG_CHECK();
Vector VAUTHORIZ_FLAG_CHECK = gx_inv.getVAUTHORIZ_FLAG_CHECK();
Vector VIS_SUBMITTED = gx_inv.getVIS_SUBMITTED();
Vector VFILE_UPLOAD_COUNT = gx_inv.getVFILE_UPLOAD_COUNT();
Vector VUPLOADED_FILE_NAME = gx_inv.getVUPLOADED_FILE_NAME();
Vector VSAP_APPROVAL_FLAG = gx_inv.getVSAP_APPROVAL_FLAG();
Vector VTYPE_FLAG = gx_inv.getVTYPE_FLAG();


Vector VSELL_BUY_FLAG=gx_inv.getVSELL_BUY_FLAG();
Vector VSELL_BUY_NM=gx_inv.getVSELL_BUY_NM();
Vector VINDEX=gx_inv.getVINDEX();

Vector VOTH_COUNTERPTY_CD = gx_inv.getVOTH_COUNTERPTY_CD();
Vector VOTH_COUNTERPTY_ABBR = gx_inv.getVOTH_COUNTERPTY_ABBR();
Vector VOTH_GX_COUNTERPTY_ABBR = gx_inv.getVOTH_GX_COUNTERPTY_ABBR();
Vector VOTH_GX_COUNTERPTY_CD = gx_inv.getVOTH_GX_COUNTERPTY_CD();

Vector VOTH_DEAL_NO = gx_inv.getVOTH_DEAL_NO();
Vector VOTH_CONT_REF_NO = gx_inv.getVOTH_CONT_REF_NO();
Vector VOTH_CONTRACT_TYPE = gx_inv.getVOTH_CONTRACT_TYPE();
Vector VOTH_START_DT = gx_inv.getVOTH_START_DT();
Vector VOTH_END_DT = gx_inv.getVOTH_END_DT();
Vector VOTH_AGMT_NO = gx_inv.getVOTH_AGMT_NO();
Vector VOTH_CONT_NO = gx_inv.getVOTH_CONT_NO();
Vector VOTH_CONT_REV_NO = gx_inv.getVOTH_CONT_REV_NO();
Vector VOTH_AGMT_REV_NO = gx_inv.getVOTH_AGMT_REV_NO();
Vector VOTH_GX_BU_PLANT_ABBR = gx_inv.getVOTH_GX_BU_PLANT_ABBR();
Vector VOTH_GX_BU_PLANT_SEQ = gx_inv.getVOTH_GX_BU_PLANT_SEQ();
Vector VOTH_BU_PLANT_ABBR = gx_inv.getVOTH_BU_PLANT_ABBR();
Vector VOTH_BU_PLANT_SEQ = gx_inv.getVOTH_BU_PLANT_SEQ();
Vector VOTH_DIS_INVOICE_NO = gx_inv.getVOTH_DIS_INVOICE_NO();
Vector VOTH_STATUS = gx_inv.getVOTH_STATUS();
Vector VMAPPING_ID = gx_inv.getVMAPPING_ID();
Vector VOTH_INVOICE_NO = gx_inv.getVOTH_INVOICE_NO();
Vector VOTH_INVOICE_SEQ = gx_inv.getVOTH_INVOICE_SEQ();
Vector VOTH_INVOICE_TYPE = gx_inv.getVOTH_INVOICE_TYPE();
Vector VOTH_FINANCIAL_YEAR = gx_inv.getVOTH_FINANCIAL_YEAR();

Vector VOTH_PDF_INV_FLAG = gx_inv.getVOTH_PDF_INV_FLAG();
Vector VOTH_CHECK_FLAG_CHECK = gx_inv.getVOTH_CHECK_FLAG_CHECK();
Vector VOTH_IS_SUBMITTED = gx_inv.getVOTH_IS_SUBMITTED();
Vector VOTH_AUTHORIZ_FLAG_CHECK = gx_inv.getVOTH_AUTHORIZ_FLAG_CHECK();
Vector VOTH_APPROVE_FLAG_CHECK = gx_inv.getVOTH_APPROVE_FLAG_CHECK();
Vector VOTH_SAP_APPROVAL_FLAG = gx_inv.getVOTH_SAP_APPROVAL_FLAG();
Vector VOTH_TYPE_FLAG = gx_inv.getVOTH_TYPE_FLAG();

Vector VOTH_FILE_UPLOAD_COUNT = gx_inv.getVOTH_FILE_UPLOAD_COUNT();
Vector VOTH_UPLOADED_FILE_NAME = gx_inv.getVOTH_UPLOADED_FILE_NAME();

/* Vector VOTH_PLANT_SEQ = gx_inv.getVOTH_PLANT_SEQ();
Vector VOTH_PLANT_ABBR = gx_inv.getVOTH_PLANT_ABBR();
Vector VOTH_BU_PLANT_SEQ = gx_inv.getVOTH_BU_PLANT_SEQ();
Vector VOTH_BU_PLANT_ABBR = gx_inv.getVOTH_BU_PLANT_ABBR();
 */

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd+""+CommonVariable.gx_inv_path;
String fflow_file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd+""+CommonVariable.gx_freeflow_inv_path;
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Gx_Invoice" enctype="multipart/form-data">

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
					    	Transaction Charge Remittance Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0">All</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
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
		      						</div>
				    			</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2"></div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%int k=0,i=0; %>
				<%for(int j=0;j<VSELL_BUY_NM.size();j++)
				{ 
					int index=Integer.parseInt(""+VINDEX.elementAt(j));
					String sellBuy_flag=""+VSELL_BUY_FLAG.elementAt(j);
					%>
					<%if(j!=0){ %>&nbsp;<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> GX Transaction Charge Remittance [<%=VSELL_BUY_NM.elementAt(j)%>]&nbsp;<span style="color:blue;">(<%=VINDEX.elementAt(j)%> Items)</span></label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th>Customer</th>
											<th>Gas Exchange</th>
											<th>Contract No<br>[Contract/Trade Ref#]</th>
											<th>Contract Period</th>
											<th>Gx Business Unit</th>
											<th>Business Unit</th>
											<th>Invoice#</th>
											<th>Remittance Status</th>
											<th>Prepare<br>Remittance</th>
											<th>Check<br>Remittance</th>
											<th>Authorize<br>Remittance</th>
											<th>Approve<br>Remittance</th>
											<th>SAP<br>XML</th>
											<th>Print PDF</th>
											<th>Upload Received<br>Invoice</th>											
											<th>View PDF
												<br>
												<select class="form-select form-select-sm" name="inv_title" id="inv_title_<%=sellBuy_flag%>" onchange="refresh();" style="width:80px;">
										    		<option value="">--Select--</option>
										    		<option value="SG">SG</option>
										    		<option value="PG">PG</option>
										    		<option value="PG_RECV">PG(RECV)</option>
										    	</select>
										    	<%if(sellBuy_flag.equals("S")){ %>	
										    	<script>document.getElementById("inv_title_<%=sellBuy_flag%>").value="<%=inv_title_S%>"</script>
										    	<%}else{ %>
										    	<script>document.getElementById("inv_title_<%=sellBuy_flag%>").value="<%=inv_title_B%>"</script>
										    	<%} %>
										    </th>
											<th>Send Mail</th>
										</tr>
									</thead>
									<tbody>
									<%k=0;
									if(index>0){ %>
										<%for(i=i;i<VCONT_NO.size();i++){
											k+=1;
										%>
											<tr>
												<td align="center">
													<%=VCOUNTERPTY_ABBR.elementAt(i)%>
												</td>
												<td align="center">
													<%=VGX_COUNTERPTY_ABBR.elementAt(i)%>
												</td>
												<td align="center" title="Agmt Base : <%=VAGMT_BASE.elementAt(i)%>">
													<font color="blue"><%=VDEAL_NO.elementAt(i)%></font>
													<%if(VAGMT_BASE.elementAt(i).equals("D")){ %>
													<font style="background:#a6ff4d;">[DLV]</font>
													<%} %>
													<br>[<%=VCONT_REF_NO.elementAt(i)%>]
												</td>
												<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
												<td align="center"><%=VGX_BU_PLANT_ABBR.elementAt(i)%></td>
												<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
												<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
												<td align="center"><%=VSTATUS.elementAt(i) %></td>
												<td align="center">
													<%if(VPDF_INV_FLAG.elementAt(i).equals("Y")){ %>
													<i class="fa fa-eye fa-2x"
													onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VGX_COUNTERPTY_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>',
															'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
															'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VGX_BU_PLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','')"
													style="color:black;">
													</i>
													<%}else{ %>
													<i class="fa fa-cogs fa-2x"
													onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VGX_COUNTERPTY_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>',
															'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
															'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VGX_BU_PLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','PREPARE')"
													style="<%if(VAPPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
														pointer-events: none; opacity: .65; color: gray;
															<%} else{%>
															color:#008080;
															<%}%>">
													</i>
													<%} %>
												</td>
												<td align="center">
													<i class="fa fa-stethoscope fa-2x"
													onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VGX_COUNTERPTY_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>',
														'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
														'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VGX_BU_PLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','CHECK')"
													style="<%if(VIS_SUBMITTED.elementAt(i).equals("N") || VAPPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
														pointer-events: none; opacity: .65; color: gray;
															<%} else{%>
															color:#ff3399;
															<%}%>">												
													</i>
												</td>
												<td align="center">
													<i class="fa fa-thumbs-o-up fa-2x"
													onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VGX_COUNTERPTY_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>',
														'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
														'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VGX_BU_PLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','AUTHORIZE')"
													style="<%if(VCHECK_FLAG_CHECK.elementAt(i).equals("N") || VAPPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
																pointer-events: none; opacity: .65; color: gray;
															<%} else{%>
															color:blue;
															<%}%>">		
													</i>
												</td>
												<td align="center">
													<i class="fa fa-flag fa-2x"
													onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VGX_COUNTERPTY_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>',
														'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
														'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VGX_BU_PLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','APPROVE')"
													style="<%if(VAUTHORIZ_FLAG_CHECK.elementAt(i).equals("N") || VPDF_INV_FLAG.elementAt(i).equals("Y")){ %>
															pointer-events: none; opacity: .65; color: gray;
														<%} else{%>
														color:#00e600;
														<%}%>">
													</i>
												</td>
												<td align="center">
													<i class="fa <%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
														onclick="doGenXML('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
													 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
													 '<%=VTYPE_FLAG.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','<%=VSAP_APPROVAL_FLAG.elementAt(i)%>',
													 '<%=VGX_COUNTERPTY_CD.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
													 '<%=VCONT_NO.elementAt(i)%>','<%=VPDF_INV_FLAG.elementAt(i)%>');"
													style="<%if(!VAPPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
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
													<i class="fa fa-print fa-2x"
													onclick="printPDF('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VGX_COUNTERPTY_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>',
															'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
															'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
															'<%=VGX_BU_PLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
															'<%=VFINANCIAL_YEAR.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i)%>',
															'<%=VAPPROVE_INVOICE_FLAG.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>');" 
													style="<%if(VAPPROVE_FLAG_CHECK.elementAt(i).equals("N") || VAPPROVE_INVOICE_FLAG.elementAt(i).equals("") || VPDF_INV_FLAG.elementAt(i).equals("Y")){ %>
														pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
													color:#800000;
													<%}%>">												
													</i>																										
												</td>
												<td align="center">	
													<span class="position-relative">
														<i class="fa fa-upload fa-2x" data-bs-toggle="modal" data-bs-target="#FileModal" 
															onclick="setUploadParam('<%=VINVOICE_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i)%>',
																'<%=VGX_COUNTERPTY_CD.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>')"
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
													<i class="fa fa-envelope-o fa-2x" 
														onclick="openMailBody('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VGX_COUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
															'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
															'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
															'<%=VGX_BU_PLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
															'<%=VFINANCIAL_YEAR.elementAt(i) %>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','S');"
														style="<%if(!VFILE_UPLOAD_COUNT.elementAt(i).toString().equals("0") && VPDF_INV_FLAG.elementAt(i).equals("Y")){ %>
														color:blue;
														<%} else{%>
														pointer-events: none; opacity: .65; color: gray;
														<%}%>">
													</i>
												</td>
											</tr>
											<%if(k==index){
												i+=1;
												break;
											} %>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="16" align="center"><%=utilmsg.infoMessage("<b>No Contract is Ready for Generate!</b>")%></td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				<%} %>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> GX Free Flow Remittance Generation</label>
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
											<th>Gas Exchange</th>
											<th>Contract No<br>[Contract/Trade Ref#]</th>
											<th>Contract Period</th>
											<th>Gx Business Unit</th>
											<th>Business Unit</th>
											<th>Invoice#</th>
											<th>Remittance Status</th>
											<th>Prepare<br>Remittance</th>
											<th>Check<br>Remittance</th>
											<th>Authorize<br>Remittance</th>
											<th>Approve<br>Remittance</th>
											<th>SAP<br>XML</th>
											<th>Print PDF</th>
											<th>Upload Received<br>Invoice</th>											
											<th>View PDF
												<br>
												<select class="form-select form-select-sm" name="fflow_inv_title" id="fflow_inv_title" onchange="refresh();" style="width:80px;">
										    		<option value="">--Select--</option>
										    		<option value="PG">PG</option>
										    		<option value="PG_RECV">PG(RECV)</option>
										    	</select>
										    	<script>document.getElementById("fflow_inv_title").value="<%=fflow_inv_title%>"</script>
										   	</th>
											<th>Send Mail</th>
										</tr>
									</thead>
									<tbody>
									<%if(VOTH_COUNTERPTY_CD.size() > 0){ %>
										<%for(i=0; i<VOTH_COUNTERPTY_CD.size(); i++){ %>
										<tr>
											<td align="center">	<%=VOTH_COUNTERPTY_ABBR.elementAt(i)%></td>						
											<td align="center">	<%=VOTH_GX_COUNTERPTY_ABBR.elementAt(i)%></td>	
											<td align="center">
												<font color="blue"><%=VOTH_DEAL_NO.elementAt(i)%></font><br>[<%=VOTH_CONT_REF_NO.elementAt(i)%>]
											</td>
											<td align="center"><%=VOTH_START_DT.elementAt(i)%> - <%=VOTH_END_DT.elementAt(i)%></td>
											<td align="center"><%=VOTH_GX_BU_PLANT_ABBR.elementAt(i)%></td>
											<td align="center"><%=VOTH_BU_PLANT_ABBR.elementAt(i)%></td>
											<td align="center"><%=VOTH_DIS_INVOICE_NO.elementAt(i)%></td>
											<td align="center"><%=VOTH_STATUS.elementAt(i) %></td>
											<td align="center">
												<%if(VOTH_PDF_INV_FLAG.elementAt(i).equals("Y")){ %>
												<i class="fa fa-eye fa-2x"
												onclick="openFreeFlowInv_Modify('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_GX_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_GX_BU_PLANT_SEQ.elementAt(i)%>','<%=VMAPPING_ID.elementAt(i)%>',
													'<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_INVOICE_NO.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','');"
												style="color:black;">
												</i>
												<%}else{ %>
												<i class="fa fa-pencil fa-2x"  
												onclick="openFreeFlowInv_Modify('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_GX_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_GX_BU_PLANT_SEQ.elementAt(i)%>','<%=VMAPPING_ID.elementAt(i)%>',
													'<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_INVOICE_NO.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','PREPARE');"
												style="<%if(VOTH_CHECK_FLAG_CHECK.elementAt(i).equals("Y")){ %>
													pointer-events: none; opacity: .65; color: gray;
														<%} else{%>
														color:orange;
														<%}%>">
												</i>
												<%} %>
											</td>
											<td align="center">
												<i class="fa fa-stethoscope fa-2x"
												onclick="openFreeFlowInv_Modify('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_GX_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_GX_BU_PLANT_SEQ.elementAt(i)%>','<%=VMAPPING_ID.elementAt(i)%>',
													'<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_INVOICE_NO.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','CHECK');"
												style="<%if(VOTH_IS_SUBMITTED.elementAt(i).equals("N") || VOTH_AUTHORIZ_FLAG_CHECK.elementAt(i).equals("Y")){ %>
													pointer-events: none; opacity: .65; color: gray;
														<%} else{%>
														color:#ff3399;
														<%}%>">
																									
												</i>
											</td>
											<td align="center">
												<i class="fa fa-thumbs-o-up fa-2x"
												onclick="openFreeFlowInv_Modify('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_GX_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_GX_BU_PLANT_SEQ.elementAt(i)%>','<%=VMAPPING_ID.elementAt(i)%>',
													'<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_INVOICE_NO.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','AUTHORIZE');"
												style="<%if(VOTH_CHECK_FLAG_CHECK.elementAt(i).equals("N") || VOTH_APPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
															pointer-events: none; opacity: .65; color: gray;
														<%} else{%>
														color:blue;
														<%}%>">		
												</i>
											</td>
											<td align="center">
												<i class="fa fa-flag fa-2x"
												onclick="openFreeFlowInv_Modify('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
													'<%=VOTH_GX_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_GX_BU_PLANT_SEQ.elementAt(i)%>','<%=VMAPPING_ID.elementAt(i)%>',
													'<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_INVOICE_NO.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','APPROVE');"
												style="<%if(VOTH_AUTHORIZ_FLAG_CHECK.elementAt(i).equals("N") || VOTH_PDF_INV_FLAG.elementAt(i).equals("Y")){ %>
														pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
													color:#00e600;
													<%}%>">
												</i>
											</td>
											<td align="center">												
												<i class="fa <%if(!VOTH_SAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
												onclick="doGenXML('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_INVOICE_NO.elementAt(i)%>',
												 	 '<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_CONTRACT_TYPE.elementAt(i)%>',
													 '<%=VOTH_TYPE_FLAG.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_SAP_APPROVAL_FLAG.elementAt(i)%>',
												 	'<%=VOTH_GX_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>','<%=VOTH_AGMT_NO.elementAt(i)%>',
													 '<%=VOTH_CONT_NO.elementAt(i)%>','<%=VOTH_PDF_INV_FLAG.elementAt(i)%>');"
												style="<%if(!VOTH_APPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
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
												<i class="fa fa-print fa-2x"
												onclick="fflowPrintPDF('<%=VOTH_COUNTERPTY_CD.elementAt(i) %>','<%=VOTH_BU_PLANT_SEQ.elementAt(i) %>',
													'<%=VOTH_GX_COUNTERPTY_CD.elementAt(i) %>','<%=VOTH_GX_BU_PLANT_SEQ.elementAt(i) %>',
													'<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_CONTRACT_TYPE.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i) %>',
													'<%=VOTH_INVOICE_NO.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i) %>');"
 												style="<%if(VOTH_APPROVE_FLAG_CHECK.elementAt(i).equals("N") || VOTH_PDF_INV_FLAG.elementAt(i).equals("Y")){ %>
													pointer-events: none; opacity: .65; color: gray;
												<%} else{%>
												color:#800000;
												<%}%>">												
												</i>													
											</td>
											<td align="center">	
												<span class="position-relative">
													<i class="fa fa-upload fa-2x" data-bs-toggle="modal" data-bs-target="#FileModal"
 														onclick="setFFlowUploadParam('<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>',
 														'<%=VOTH_CONTRACT_TYPE.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_GX_COUNTERPTY_CD.elementAt(i) %>')" 
														style="<%if(VOTH_IS_SUBMITTED.elementAt(i).equals("N")){ %>
															pointer-events: none; opacity: .65; color: gray;
														<%} else{%>
														color:#00bfff;
														<%}%>">	
													</i>
													<%if(!VOTH_FILE_UPLOAD_COUNT.elementAt(i).toString().equals("0")){ %>
													<span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-primary">
														<%=VOTH_FILE_UPLOAD_COUNT.elementAt(i)%>
													</span>
													<%} %>
												</span>												
											</td>
											<td align="center">																						
												<i class="fa fa-file-pdf-o fa-2x" title="<%=VOTH_UPLOADED_FILE_NAME.elementAt(i)%>"
 												onclick="openPdfFile('<%=fflow_file_url%><%=VOTH_UPLOADED_FILE_NAME.elementAt(i)%>')"
												style="<%if(VOTH_UPLOADED_FILE_NAME.elementAt(i).equals("")){ %>
													pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
													color:red;
													<%}%>">
												</i>								
											</td>
											<td align="center">
												<i class="fa fa-envelope-o fa-2x" 
													onclick="openMailBody('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_GX_COUNTERPTY_CD.elementAt(i)%>',
														'<%=VOTH_AGMT_NO.elementAt(i)%>','<%=VOTH_AGMT_REV_NO.elementAt(i)%>','<%=VOTH_CONT_NO.elementAt(i)%>',
														'<%=VOTH_CONT_REV_NO.elementAt(i)%>','<%=VOTH_CONTRACT_TYPE.elementAt(i)%>',
														'<%=VOTH_GX_BU_PLANT_SEQ.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
														'<%=VOTH_FINANCIAL_YEAR.elementAt(i) %>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','F');"
													style="<%if(!VOTH_FILE_UPLOAD_COUNT.elementAt(i).toString().equals("0") && VOTH_PDF_INV_FLAG.elementAt(i).equals("Y")){ %>
													color:blue;
													<%} else{%>
													pointer-events: none; opacity: .65; color: gray;
													<%}%>">
												</i>
											</td>
										</tr>
									<%} %>
									<%}else{ %>
										<tr>
											<td align="center" colspan="17"><%=utilmsg.infoMessage("<b>No Invoice is Generated!</b>") %></td>
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
					    			<input type="hidden" name="file_gx_counterparty_cd">
					    			<input type="hidden" name="file_invoice_seq">
					    			<input type="hidden" name="file_financial_year">
					    			<input type="hidden" name="invoice_title">
					    			<input type="hidden" name="file_invoice_type">
					    			<input type="hidden" name="file_contract_type">
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

</form>
</body>
</html>