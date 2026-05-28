<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
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
	
	var period_start_dt="";
	var period_end_dt="";
	
	var tc_inv_title="";
	var ic_inv_title="";
	var fflow_inv_title="";
	
	if(document.getElementById("tc_inv_title")!=null && document.getElementById("tc_inv_title")!=undefined)
	{
		tc_inv_title = document.getElementById("tc_inv_title").value;
	}
	if(document.getElementById("ic_inv_title")!=null && document.getElementById("ic_inv_title")!=undefined)
	{
		ic_inv_title = document.getElementById("ic_inv_title").value;
	}
	if(document.getElementById("fflow_inv_title")!=null && document.getElementById("fflow_inv_title")!=undefined)
	{
		fflow_inv_title = document.getElementById("fflow_inv_title").value;//document.forms[0].fflow_inv_title.value;
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gta_invoice.jsp?&u="+u+"&month="+month+"&year="+year+"&billing_cycle="+billing_cycle+
			"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+
			"&tc_inv_title="+tc_inv_title+"&ic_inv_title="+ic_inv_title+
			"&fflow_inv_title="+fflow_inv_title;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var newWindow;
function openActivity1(invoice_type,counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,trans_bu_seq,period_st_dt,period_end_dt,bu,billing_cycle,qty_mmbtu,activity_type,
		inv_comp,financial_year,invoice_seq,inv_grp_index,isSubmitted)
{
	var u = document.forms[0].u.value;
	var MergeFlag=false;
	var inv_type_nm="";
	var tempInvCompNm="";
	
	if(isSubmitted!="Y")
	{
		var tempInvComp="";
		var count=parseInt("0");
		
		if(invoice_type=="IC")
		{
			inv_type_nm="Imbalance Charge Remittance";
			for(var i=0;i<3;i++)
	  		{
				var temp=document.getElementById("inv_chk_"+inv_grp_index+"_"+i);
				var temp1=document.getElementById("inv_compo_abbr_"+inv_grp_index+"_"+i);
				var temp2=document.getElementById("inv_compo_nm_"+inv_grp_index+"_"+i);
				if(temp!=null)
				{
					if(temp.checked)
					{
						count++;	
						if(tempInvComp=="")
						{
							tempInvComp=temp1.value;
							tempInvCompNm=temp2.value;
						}
						else
						{
							tempInvComp+="-"+temp1.value;
							tempInvCompNm+="\n"+temp2.value;
						}
					}
				}
			}
		}
		else if(invoice_type=="TC")
		{
			inv_type_nm="Transmission Charge Remittance";
			for(var i=0;i<2;i++)
	  		{
				var temp=document.getElementById("inv_chk_"+inv_grp_index+"_"+i);
				var temp1=document.getElementById("inv_compo_abbr_"+inv_grp_index+"_"+i);
				var temp2=document.getElementById("inv_compo_nm_"+inv_grp_index+"_"+i);
				if(temp!=null)
				{
					if(temp.checked)
					{
						count++;
						if(tempInvComp=="")
						{
							tempInvComp=temp1.value;
							tempInvCompNm=temp2.value;
						}
						else
						{
							tempInvComp+="-"+temp1.value;
							tempInvCompNm+="\n"+temp2.value;
						}
					}
				}
			}	
		}
		
		if(parseInt(count)>1)
		{
			inv_comp=tempInvComp;
			MergeFlag=true;
		}
	}
	
	var a=true;
	if(MergeFlag)
	{
		a=confirm("Do you want to Merge following Components of "+inv_type_nm+"?\n\n"+tempInvCompNm+
				"\n\nNote: Once Merged and Submitted, generated Remittance can't be splitted again!");
	}
	
	if(a)
	{
		var file="frm_gta_prepare_payment.jsp";
		
		var url = file+"?invoice_type="+invoice_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
				"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&trans_bu_seq="+trans_bu_seq+"&period_start_dt="+period_st_dt+
				"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+
				"&activity_type="+activity_type+"&qty_mmbtu="+qty_mmbtu+"&inv_comp="+inv_comp+
				"&financial_year="+financial_year+"&invoice_seq="+invoice_seq+"&u="+u;
		
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Prepare Transporter Payment","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Prepare Transporter Payment","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
	}
}

function printPDF(invoice_type,counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,trans_bu_seq,period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,inv_seq,inv_flag)
{
	var u = document.forms[0].u.value;
	
	var url = "pdf_gta_invoice_remittance.jsp?invoice_type="+invoice_type+"&inv_type="+inv_flag+"&counterparty_cd="+counterparty_cd+
		"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&contract_type="+cont_type+
		"&trans_bu_seq="+trans_bu_seq+"&bu_unit="+bu+"&period_start_dt="+period_st_dt+"&period_end_dt="+period_end_dt+
		"&billing_cycle="+billing_cycle+"&financial_year="+fin_yr+"&invoice_seq="+inv_seq;
	
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

function printFFLOW_PDF(invoice_type,counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,trans_bu_seq,period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,inv_seq,inv_flag,mapping_id,address_type,invoice_no,invoice_seq,trans_counterparty_cd)
{
	var u = document.forms[0].u.value;
	var is_print="1";
	
	var url = "../gta/pdf_gta_f_flow_invoice.jsp?counterparty_cd="+counterparty_cd+"&is_print="+is_print+"&contract_type="+cont_type+
	"&invoice_type="+invoice_type+"&mapping_id="+mapping_id+"&address_type="+address_type+"&bu_unit="+bu+"&billing_cycle="+billing_cycle+
	"&inv_no="+invoice_no+"&inv_seq="+invoice_seq+"&financial="+fin_yr+"&trans_counterparty_cd="+trans_counterparty_cd;
	
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

function setUploadParam(invoice_type,inv_seq,fin_yr,cont_type)
{
	document.forms[0].file_invoice_seq.value=inv_seq;
	document.forms[0].file_financial_year.value=fin_yr;
	document.forms[0].file_contract_type.value=cont_type;
	document.forms[0].invoice_title.value="PG_RECV";
	document.forms[0].file_invoice_type.value=invoice_type;
	
	document.forms[0].upload_inv_type.value="";
	
	document.getElementById('file_upload').value="";
}

function setFFlowUploadParam(inv_seq,fin_yr,cont_type,inv_type,gta_counterparty_cd)
{
	document.forms[0].file_invoice_seq.value=inv_seq;
	document.forms[0].file_financial_year.value=fin_yr;
	document.forms[0].file_contract_type.value=cont_type;
	document.forms[0].invoice_title.value="PG_RECV";
	document.forms[0].file_invoice_type.value=inv_type;
	document.forms[0].file_gta_counterparty_cd.value=gta_counterparty_cd;
	
	document.forms[0].upload_inv_type.value="FFLOW";
	
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

function openPdfFile(url)
{
	window.open(url);
}

function openMailBody(invoice_type,counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,trans_bu_seq,
		period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,inv_seq,mail_inv_type)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_gta_invoice_mail.jsp?invoice_type="+invoice_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&trans_bu_seq="+trans_bu_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+
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
		sap_approval_flag, bu_seq, agmt_no, cont_no, inv_pdf_flag)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_view_gta_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
			"&contract_type="+contract_type+"&type_flag="+type_flag+"&invoice_type="+invoice_type+
			"&counterparty_cd="+couterpty_cd+"&invoice_no="+invoice_no+"&sap_approval_flag="+sap_approval_flag+
			"&bu_seq="+bu_seq+"&agmt_no="+agmt_no+"&cont_no="+cont_no+"&inv_pdf_flag="+inv_pdf_flag+
			"&u="+u;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"GTA SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"GTA SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
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
	
	var url = "frm_gta_f_flow_invoice.jsp?u="+u+
			"&month="+month+"&year="+year+"&opration=INSERT&activity_type="+activity_type;//"&billing_cycle="+billing_cycle+
	
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

function openFreeFlowInv_Modify(counterparty_cd,trans_bu_seq,period_st_dt,period_end_dt,bu,billing_cycle,mapping_id,inv_seq,inv_no,inv_type,finan_yr,activity_type,address_type)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var split=period_end_dt.split("/");
	month=split[1];
	year=split[2];
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gta_f_flow_invoice.jsp?u="+u+
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

</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GTA_Remittance" id="remittance" scope="request"></jsp:useBean>
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
String tc_inv_title=request.getParameter("tc_inv_title")==null?"":request.getParameter("tc_inv_title");
String ic_inv_title=request.getParameter("ic_inv_title")==null?"":request.getParameter("ic_inv_title");
String fflow_inv_title=request.getParameter("fflow_inv_title")==null?"":request.getParameter("fflow_inv_title");

if(month.length() == 1)
{
	month="0"+month; 
}

remittance.setCallFlag("GTA_REMITTANCE_PREPARATION_LIST");
remittance.setComp_cd(owner_cd);
remittance.setMonth(month);
remittance.setYear(year);
remittance.setBilling_cycle(billing_cycle);
remittance.setTc_inv_title(tc_inv_title);
remittance.setIc_inv_title(ic_inv_title);
remittance.setFflow_inv_title(fflow_inv_title);
remittance.init();

Vector VINVOICE_TITLE=remittance.getVINVOICE_TITLE();
Vector VINVOICE_TYPE=remittance.getVINVOICE_TYPE();

Vector VINV_INDEX=remittance.getVINV_INDEX();

Vector VCOUNTERPTY_CD = remittance.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = remittance.getVCOUNTERPTY_ABBR();
Vector VCONT_NO = remittance.getVCONT_NO();
Vector VCONT_REV_NO = remittance.getVCONT_REV_NO();
Vector VAGMT_NO = remittance.getVAGMT_NO();
Vector VAGMT_REV_NO = remittance.getVAGMT_REV_NO();
Vector VSTART_DT = remittance.getVSTART_DT();
Vector VEND_DT = remittance.getVEND_DT();
Vector VCONT_NAME = remittance.getVCONT_NAME();
Vector VCONT_REF_NO = remittance.getVCONT_REF_NO();
Vector VCONTRACT_TYPE = remittance.getVCONTRACT_TYPE();
Vector VTRANS_BU_SEQ = remittance.getVTRANS_BU_SEQ();
Vector VTRANS_BU_ABBR = remittance.getVTRANS_BU_ABBR();
Vector VBU_PLANT_SEQ = remittance.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = remittance.getVBU_PLANT_ABBR();
Vector VDEAL_NO = remittance.getVDEAL_NO();
Vector VPERIOD_START_DT = remittance.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = remittance.getVPERIOD_END_DT();
Vector VINVOICE_NO = remittance.getVINVOICE_NO();
Vector VREMITTANCE_NO = remittance.getVREMITTANCE_NO();
Vector VSTATUS = remittance.getVSTATUS();
Vector VBILLING_FREQ_FLAG = remittance.getVBILLING_FREQ_FLAG();
Vector VBILLING_FREQ_NM = remittance.getVBILLING_FREQ_NM();
Vector VAPPROVE_FLAG_CHECK = remittance.getVAPPROVE_FLAG_CHECK();
Vector VCHECK_FLAG_CHECK = remittance.getVCHECK_FLAG_CHECK();
Vector VAUTHORIZ_FLAG_CHECK = remittance.getVAUTHORIZ_FLAG_CHECK();
Vector VIS_SUBMITTED = remittance.getVIS_SUBMITTED();
Vector VINVOICE_SEQ = remittance.getVINVOICE_SEQ();
Vector VFINANCIAL_YEAR = remittance.getVFINANCIAL_YEAR();
Vector VFILE_UPLOAD_COUNT = remittance.getVFILE_UPLOAD_COUNT();
Vector VUPLOADED_FILE_NAME = remittance.getVUPLOADED_FILE_NAME();
Vector VAPPROVE_INVOICE_FLAG = remittance.getVAPPROVE_INVOICE_FLAG();
Vector VPDF_INV_FLAG = remittance.getVPDF_INV_FLAG();
Vector VQTY_MMBTU=remittance.getVQTY_MMBTU();
Vector VTEMP_QTY_MMBTU=remittance.getVTEMP_QTY_MMBTU();
Vector VSAP_APPROVAL_FLAG = remittance.getVSAP_APPROVAL_FLAG();
Vector VTYPE_FLAG = remittance.getVTYPE_FLAG();
Vector VMAPPING_ID = remittance.getVMAPPING_ID();
Vector VINV_COMPONENTS = remittance.getVINV_COMPONENTS();
Vector VINV_COMPONENTS_ABBR = remittance.getVINV_COMPONENTS_ABBR();

Vector VINV_GRP_INDEX = remittance.getVINV_GRP_INDEX();
Vector VINV_GRP_INDEX_ROW_ID = remittance.getVINV_GRP_INDEX_ROW_ID();
Vector VINV_GRP_INDEX_COLOR = remittance.getVINV_GRP_INDEX_COLOR();

Vector VOTH_COUNTERPTY_CD = remittance.getVOTH_COUNTERPTY_CD();
Vector VOTH_GTA_COUNTERPTY_CD = remittance.getVOTH_GTA_COUNTERPTY_CD();
Vector VOTH_COUNTERPTY_ABBR = remittance.getVOTH_COUNTERPTY_ABBR();
Vector VOTH_CONT_NO = remittance.getVOTH_CONT_NO();
Vector VOTH_CONT_REV_NO = remittance.getVOTH_CONT_REV_NO();
Vector VOTH_AGMT_NO = remittance.getVOTH_AGMT_NO();
Vector VOTH_AGMT_REV_NO = remittance.getVOTH_AGMT_REV_NO();
Vector VOTH_START_DT = remittance.getVOTH_START_DT();
Vector VOTH_END_DT = remittance.getVOTH_END_DT();
Vector VOTH_CONT_NAME = remittance.getVOTH_CONT_NAME();
Vector VOTH_CONT_REF_NO = remittance.getVOTH_CONT_REF_NO();
Vector VOTH_CONTRACT_TYPE = remittance.getVOTH_CONTRACT_TYPE();
Vector VOTH_TRANS_BU_SEQ = remittance.getVOTH_TRANS_BU_SEQ();
Vector VOTH_TRANS_BU_ABBR = remittance.getVOTH_TRANS_BU_ABBR();
Vector VOTH_BU_PLANT_SEQ = remittance.getVOTH_BU_PLANT_SEQ();
Vector VOTH_BU_PLANT_ABBR = remittance.getVOTH_BU_PLANT_ABBR();
Vector VOTH_DEAL_NO = remittance.getVOTH_DEAL_NO();
Vector VOTH_PERIOD_START_DT = remittance.getVOTH_PERIOD_START_DT();
Vector VOTH_PERIOD_END_DT = remittance.getVOTH_PERIOD_END_DT();
Vector VOTH_INVOICE_NO = remittance.getVOTH_INVOICE_NO();
Vector VOTH_REMITTANCE_NO = remittance.getVOTH_REMITTANCE_NO();
Vector VOTH_STATUS = remittance.getVOTH_STATUS();
Vector VOTH_BILLING_FREQ_FLAG = remittance.getVOTH_BILLING_FREQ_FLAG();
Vector VOTH_BILLING_FREQ_NM = remittance.getVOTH_BILLING_FREQ_NM();
Vector VOTH_APPROVE_FLAG_CHECK = remittance.getVOTH_APPROVE_FLAG_CHECK();
Vector VOTH_CHECK_FLAG_CHECK = remittance.getVOTH_CHECK_FLAG_CHECK();
Vector VOTH_AUTHORIZ_FLAG_CHECK = remittance.getVOTH_AUTHORIZ_FLAG_CHECK();
Vector VOTH_IS_SUBMITTED = remittance.getVOTH_IS_SUBMITTED();
Vector VOTH_INVOICE_SEQ = remittance.getVOTH_INVOICE_SEQ();
Vector VOTH_FINANCIAL_YEAR = remittance.getVOTH_FINANCIAL_YEAR();
Vector VOTH_FILE_UPLOAD_COUNT = remittance.getVOTH_FILE_UPLOAD_COUNT();
Vector VOTH_UPLOADED_FILE_NAME = remittance.getVOTH_UPLOADED_FILE_NAME();
Vector VOTH_APPROVE_INVOICE_FLAG = remittance.getVOTH_APPROVE_INVOICE_FLAG();
Vector VOTH_PDF_INV_FLAG = remittance.getVOTH_PDF_INV_FLAG();
Vector VOTH_QTY_MMBTU=remittance.getVOTH_QTY_MMBTU();
Vector VOTH_TEMP_QTY_MMBTU=remittance.getVOTH_TEMP_QTY_MMBTU();
Vector VOTH_SAP_APPROVAL_FLAG = remittance.getVOTH_SAP_APPROVAL_FLAG();
Vector VOTH_TYPE_FLAG = remittance.getVOTH_TYPE_FLAG();
Vector VOTH_INVOICE_TYPE = remittance.getVOTH_INVOICE_TYPE();
Vector VOTH_ADDR_FLAG = remittance.getVOTH_ADDR_FLAG();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String file_url = "";
String fflow_file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd+"//gta//f_flow_invoice//";
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_GTA_Remittance" enctype="multipart/form-data">

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
					    	GTA Remittance Generation
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
										<!-- <option value="3">1st-Weekly</option>
										<option value="4">2nd-Weekly</option>
										<option value="5">3rd-Weekly</option>
										<option value="6">4th-Weekly</option>
										<option value="9">5th-Weekly</option>
										<option value="7">Monthly</option>-->
										<option value="8">Other</option> 
										<option value="0">All</option>
					  				</select>
					  				<script>document.forms[0].billing_cycle.value="<%=billing_cycle%>"</script>
					  			</div>
					  		</div>
					  	</div>
					  	<div class="col-sm-3 col-xs-3 col-md-3"></div>
					</div>
				</div>
				<div class="card-body cdbody">
				<div class="row m-b-5">
					<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;GTA Remittance</label>
				</div>
				<%int k=0,i=0; %>
				<%for(int j=0;j<VINVOICE_TYPE.size();j++)
				{ 
					int index=Integer.parseInt(""+VINV_INDEX.elementAt(j));
					String invoice_type=""+VINVOICE_TYPE.elementAt(j);
					String sub_folder2="invoice";
			        if(invoice_type.equals("TC"))
			        {
			        	sub_folder2="transmission_invoice";
			        }
			        else if(invoice_type.equals("IC"))
			        {
			        	sub_folder2="imbalance_invoice";
			        }
					file_url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd+"//gta//"+sub_folder2+"//";
				%>
				<% if(j!=0)
				{%>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">&nbsp;
						</div>
					</div> 
					<%} %>
				   <h2 class="accordion-header" id="heading<%=j%>">
						<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="false" aria-controls="collapse<%=j%>">
			    			<%=VINVOICE_TITLE.elementAt(j)%> &nbsp;<span style="color:blue;"> (<%=VINV_INDEX.elementAt(j)%> Items)</span>
			      		</button>
			    	</h2>
					<div id="collapse<%=j%>" class="accordion-collapse collapse" aria-labelledby="heading<%=j%>">
			      		<div class="accordion-body accor-body">						
							<div class="row">
								<div class="col-md-12 col-sm-12 col-xs-12">
									<div class="table-responsive">
										<table class="table table-bordered table-hover">
											<thead>
												<tr>
													<th></th>
													<th>Transporter</th>
													<th>Contract No<br>[Contract Ref#]</th>
													<th>Contract Period</th>
													<th>Transporter<br>Business Unit</th>
													<th>Business Unit</th>
													<th>Billing Cycle</th>
													<th>Billing Period</th>
													<th>Invoice Components</th>
													<th>Invoice#<br>[Remittance#]</th>
													<th>Remittance Status</th>
													<th>Prepare<br>Remittance</th>
													<th>Check<br>Remittance</th>
													<th>Authorize<br>Remittance</th>
													<th>Approve<br>Remittance</th>
													<th>SAP XML</th>
													<th>Print PDF
														<!-- <br>
														<select class="form-select form-select-sm" >
												    		<option value="0">All</option>
												    		<option value="1">SG</option>
												    		<option value="2">PG</option>
												    	</select>	 -->
													</th>
													<th>Upload Received<br>Invoice</th>											
													<th>View PDF
														<br>
														<select class="form-select form-select-sm" id="<%=invoice_type.toLowerCase()%>_inv_title" 
														onchange="refresh();" style="width:80px;">
												    		<option value="">--Select--</option>
												    		<option value="SG">SG</option>
												    		<option value="PG">PG</option>
												    		<option value="PG_RECV">PG(RECV)</option>
												    	</select>
												    	<%if(invoice_type.equals("TC")){ %>	
												    	<script>document.getElementById("<%=invoice_type.toLowerCase()%>_inv_title").value="<%=tc_inv_title%>"</script>
												    	<%} else if(invoice_type.equals("IC")){ %>	
												    	<script>document.getElementById("<%=invoice_type.toLowerCase()%>_inv_title").value="<%=ic_inv_title%>"</script>
												    	<%} %>
													</th>
													<th>Send Mail</th>
												</tr>
											</thead>
											<tbody>
											<%k=0;
											if(index > 0){ %>
												<%for(i=i; i<VCOUNTERPTY_CD.size(); i++)
												{ 
													k+=1;
												%>
													<tr>
														<td align="center" style="background: <%=VINV_GRP_INDEX_COLOR.elementAt(i)%>">
															<input type="checkbox" class="form-check-input" name="inv_chk_<%=VINV_GRP_INDEX.elementAt(i)%>" 
																id="inv_chk_<%=VINV_GRP_INDEX.elementAt(i)%>_<%=VINV_GRP_INDEX_ROW_ID.elementAt(i)%>" 
																<%if(VIS_SUBMITTED.elementAt(i).equals("Y")){ %>disabled<%} %>>
															<input type="hidden" name="inv_compo_abbr_<%=VINV_GRP_INDEX.elementAt(i)%>" 
																id="inv_compo_abbr_<%=VINV_GRP_INDEX.elementAt(i)%>_<%=VINV_GRP_INDEX_ROW_ID.elementAt(i)%>" 
																value="<%=VINV_COMPONENTS_ABBR.elementAt(i)%>">
															<input type="hidden" name="inv_compo_nm_<%=VINV_GRP_INDEX.elementAt(i)%>" 
																id="inv_compo_nm_<%=VINV_GRP_INDEX.elementAt(i)%>_<%=VINV_GRP_INDEX_ROW_ID.elementAt(i)%>" 
																value="<%=VINV_COMPONENTS.elementAt(i)%>">	
														</td>
														<td align="center">
															<%=VCOUNTERPTY_ABBR.elementAt(i)%>
														</td>
														<td align="center" title="<%if(invoice_type.equals("TC")){%>Transmission MMBTU: <%=VTEMP_QTY_MMBTU.elementAt(i)%><%} %>">
															<font color="blue"><%=VDEAL_NO.elementAt(i)%></font><br>[<%=VCONT_REF_NO.elementAt(i)%>]
														</td>
														<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
														<td align="center"><%=VTRANS_BU_ABBR.elementAt(i)%></td>
														<td align="center"><%=VBU_PLANT_ABBR.elementAt(i) %></td>
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
														<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
														<td align="center"><%=VINV_COMPONENTS.elementAt(i)%></td>
														<td align="center">
															<%=VINVOICE_NO.elementAt(i)%>
															<%if(!VREMITTANCE_NO.elementAt(i).toString().equals("")){%>
																<br><font color="blue" >[<%=VREMITTANCE_NO.elementAt(i)%>]
															<%} %>
														</td>
														<td align="center"><%=VSTATUS.elementAt(i) %></td>
														<td align="center">
															<%if(VPDF_INV_FLAG.elementAt(i).equals("Y")){ %>
															<i class="fa fa-eye fa-2x"
															onclick="openActivity1('<%=invoice_type%>','<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																	'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VTRANS_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>',
																	'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VQTY_MMBTU.elementAt(i)%>','','<%=VINV_COMPONENTS_ABBR.elementAt(i) %>',
																	'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINV_GRP_INDEX.elementAt(i) %>','<%=VIS_SUBMITTED.elementAt(i)%>')"
															style="color:black;">
															</i>
															<%}else{ %>
															<i class="fa fa-cogs fa-2x"
															onclick="openActivity1('<%=invoice_type%>','<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																	'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VTRANS_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>',
																	'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VQTY_MMBTU.elementAt(i)%>','PREPARE','<%=VINV_COMPONENTS_ABBR.elementAt(i) %>',
																	'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINV_GRP_INDEX.elementAt(i) %>','<%=VIS_SUBMITTED.elementAt(i)%>')"
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
															onclick="openActivity1('<%=invoice_type%>','<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																	'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VTRANS_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>',
																	'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VQTY_MMBTU.elementAt(i)%>','CHECK','<%=VINV_COMPONENTS_ABBR.elementAt(i) %>',
																	'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINV_GRP_INDEX.elementAt(i) %>','<%=VIS_SUBMITTED.elementAt(i)%>')"
															style="<%if(VIS_SUBMITTED.elementAt(i).equals("N") || VAPPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
																pointer-events: none; opacity: .65; color: gray;
																	<%} else{%>
																	color:#ff3399;
																	<%}%>">												
															</i>
														</td>
														<td align="center">
															<i class="fa fa-thumbs-o-up fa-2x"
															onclick="openActivity1('<%=invoice_type%>','<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																		'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VTRANS_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>',
																		'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VQTY_MMBTU.elementAt(i)%>','AUTHORIZE','<%=VINV_COMPONENTS_ABBR.elementAt(i) %>',
																		'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINV_GRP_INDEX.elementAt(i) %>','<%=VIS_SUBMITTED.elementAt(i)%>')"
															style="<%if(VCHECK_FLAG_CHECK.elementAt(i).equals("N") || VAPPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
																		pointer-events: none; opacity: .65; color: gray;
																	<%} else{%>
																	color:blue;
																	<%}%>">		
															</i>
														</td>
														<td align="center">
															<i class="fa fa-flag fa-2x"
															onclick="openActivity1('<%=invoice_type%>','<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																		'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VTRANS_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>',
																		'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VQTY_MMBTU.elementAt(i)%>','APPROVE','<%=VINV_COMPONENTS_ABBR.elementAt(i) %>',
																		'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINV_GRP_INDEX.elementAt(i) %>','<%=VIS_SUBMITTED.elementAt(i)%>')"
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
															 '<%=VTYPE_FLAG.elementAt(i)%>','<%=invoice_type%>','<%=VSAP_APPROVAL_FLAG.elementAt(i)%>',
															 '<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VPDF_INV_FLAG.elementAt(i)%>');"
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
															onclick="printPDF('<%=invoice_type%>','<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VTRANS_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																'<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VAPPROVE_INVOICE_FLAG.elementAt(i)%>')" 
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
																	onclick="setUploadParam('<%=invoice_type%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>')"
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
																onclick="openMailBody('<%=invoice_type%>','<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																	'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																	'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																	'<%=VTRANS_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																	'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																	'<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VINVOICE_SEQ.elementAt(i)%>','S');"
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
													<td align="center" colspan="20"><%=utilmsg.infoMessage("<b>No Remittance is Ready for Generate!</b>") %></td>
												</tr>
											<%} %>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				<%} %>&nbsp;
				<div class="row m-b-5">
					<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;GTA Free Flow Remittance</label>
				</div>
				<div class="row m-b-5">
					<div class="col-sm-12 col-xs-12 col-md-12" align="right">
						<div class="btn-group">
							<label class="btn btn-outline-secondary subbtngrp" onclick="openFreeFlowInv();"><i class="fa fa-plus-circle"></i>&nbsp;Create New</label>
						</div>
					</div>					
				</div>
				
					<h2 class="accordion-header" id="heading11">
						<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse11" aria-expanded="false" aria-controls="collapse11">
			    			 GTA Free Flow Remittance Generation &nbsp;<span style="color:blue;">(<%=VOTH_COUNTERPTY_ABBR.size()%> Items)</span>
			      		</button>
			    	</h2>
					<div id="collapse11" class="accordion-collapse collapse" aria-labelledby="heading11">
			      		<div class="accordion-body accor-body">	
							<div class="row">
								<div class="col-md-12 col-sm-12 col-xs-12">
									<div class="table-responsive">
										<table class="table table-bordered table-hover">
											<thead>
												<tr>
													<th>Transporter</th>
													<th>Contract No<br>[Contract Ref#]</th>
													<th>Contract Period</th>
													<th>Transporter<br>Business Unit</th>
													<th>Business Unit</th>
													<!-- <th>Billing Cycle</th> -->
													<th>Billing Period</th>
													<th>Invoice# <br>[Remittance#]</th>
													<th>Remittance Status</th>
													<th>Prepare<br>Remittance</th>
													<th>Check<br>Remittance</th>
													<th>Authorize<br>Remittance</th>
													<th>Approve<br>Remittance</th>
													<th>SAP XML</th>
													<th>Print PDF
														<!-- <br>
														<select class="form-select form-select-sm" >
												    		<option value="0">All</option>
												    		<option value="1">SG</option>
												    		<option value="2">PG</option>
												    	</select>	 -->
													</th>
													<th>Upload Received<br>Invoice</th>											
													<th>View PDF
														<br>
														<select class="form-select form-select-sm" name="fflow_inv_title" id="fflow_inv_title" onchange="refresh();">
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
											<%k=0;
											if(VOTH_COUNTERPTY_CD.size() > 0){ %>
												<%for(i=0; i<VOTH_COUNTERPTY_CD.size(); i++)
												{
													k+=1;
												%>
													<tr>
														<td align="center">
															<%=VOTH_COUNTERPTY_ABBR.elementAt(i)%>
														</td>
														<td align="center" title="">
															<font color="blue"><%=VOTH_DEAL_NO.elementAt(i)%></font><br>[<%=VOTH_CONT_REF_NO.elementAt(i)%>]
														</td>
														<td align="center"><%=VOTH_START_DT.elementAt(i)%> - <%=VOTH_END_DT.elementAt(i)%></td>
														<td align="center"><%=VOTH_TRANS_BU_ABBR.elementAt(i)%></td>
														<td align="center"><%=VOTH_BU_PLANT_ABBR.elementAt(i) %></td>
														<%-- <td align="center">
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
														</td> --%>
														<td align="center"><%=VOTH_PERIOD_START_DT.elementAt(i)%> - <%=VOTH_PERIOD_END_DT.elementAt(i)%></td>
														<td align="center"><%=VOTH_INVOICE_NO.elementAt(i)%><br><font color="blue" >[<%=VOTH_REMITTANCE_NO.elementAt(i) %>]</font></td>
														<td align="center"><%=VOTH_STATUS.elementAt(i) %></td>
														<td align="center">
															<%if(VOTH_PDF_INV_FLAG.elementAt(i).equals("Y")){ %>
																<i class="fa fa-eye fa-2x"
																onclick="openFreeFlowInv_Modify('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_TRANS_BU_SEQ.elementAt(i)%>','<%=VOTH_PERIOD_START_DT.elementAt(i)%>','<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i) %>','<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>',
																		'<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_REMITTANCE_NO.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>',
																		'<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','','<%=VOTH_ADDR_FLAG.elementAt(i) %>')"
																style="color:black;">
																</i>
															<%}else{ %>
																<i class="fa fa-pencil fa-2x"
																onclick="openFreeFlowInv_Modify('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_TRANS_BU_SEQ.elementAt(i)%>','<%=VOTH_PERIOD_START_DT.elementAt(i)%>','<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i) %>','<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>',
																		'<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_REMITTANCE_NO.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>',
																		'<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','PREPARE','<%=VOTH_ADDR_FLAG.elementAt(i) %>')"
																style="<%if(VOTH_APPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
																	pointer-events: none; opacity: .65; color: gray;
																		<%} else{%>
																		color:orange;
																		<%}%>">
																</i>
															<%} %>
														</td>
														<td align="center">
															<i class="fa fa-stethoscope fa-2x"
															onclick="openFreeFlowInv_Modify('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_TRANS_BU_SEQ.elementAt(i)%>','<%=VOTH_PERIOD_START_DT.elementAt(i)%>','<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i) %>','<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>',
																		'<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_REMITTANCE_NO.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>',
																		'<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','CHECK','<%=VOTH_ADDR_FLAG.elementAt(i) %>')"
															style="<%if(VOTH_IS_SUBMITTED.elementAt(i).equals("N") || VOTH_APPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
																pointer-events: none; opacity: .65; color: gray;
																	<%} else{%>
																	color:#ff3399;
																	<%}%>">												
															</i>
														</td>
														<td align="center">
															<i class="fa fa-thumbs-o-up fa-2x"
															onclick="openFreeFlowInv_Modify('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_TRANS_BU_SEQ.elementAt(i)%>','<%=VOTH_PERIOD_START_DT.elementAt(i)%>','<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i) %>','<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>',
																		'<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_REMITTANCE_NO.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>',
																		'<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','AUTHORIZE','<%=VOTH_ADDR_FLAG.elementAt(i) %>')"
															style="<%if(VOTH_CHECK_FLAG_CHECK.elementAt(i).equals("N") || VOTH_APPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
																		pointer-events: none; opacity: .65; color: gray;
																	<%} else{%>
																	color:blue;
																	<%}%>">		
															</i>
														</td>
														<td align="center">
															<i class="fa fa-flag fa-2x"
															onclick="openFreeFlowInv_Modify('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>','<%=VOTH_PERIOD_START_DT.elementAt(i)%>','<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i) %>','<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>',
																		'<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_REMITTANCE_NO.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>',
																		'<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','APPROVE','<%=VOTH_ADDR_FLAG.elementAt(i) %>')"
															style="<%if(VOTH_AUTHORIZ_FLAG_CHECK.elementAt(i).equals("N") || VOTH_PDF_INV_FLAG.elementAt(i).equals("Y")){ %>
																	pointer-events: none; opacity: .65; color: gray;
																<%} else{%>
																color:#00e600;
																<%}%>">
															</i>
														</td>
														<td align="center">
															<%-- <i class="fa <%if(!VOTH_SAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
															onclick="doGenXML('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_REMITTANCE_NO.elementAt(i)%>',
															 '<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_CONTRACT_TYPE.elementAt(i)%>',
															 '<%=VOTH_TYPE_FLAG.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_SAP_APPROVAL_FLAG.elementAt(i)%>',
															 '<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>','<%=VOTH_AGMT_NO.elementAt(i)%>','<%=VOTH_CONT_NO.elementAt(i)%>','<%=VOTH_PDF_INV_FLAG.elementAt(i)%>');"
															style="<%if(!VOTH_APPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
																pointer-events: none; opacity: .65; color: gray;
																<%} else{%>
																	<%if(!VOTH_SAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
																		color: orange;
																	<%} else{%>
																		color: brown;
																	<%}%>		
																<%}%>">													
															</i> --%>
															<i class="fa <%if(VOTH_APPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
															onclick="doGenXML('<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_REMITTANCE_NO.elementAt(i)%>',
															 '<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_CONTRACT_TYPE.elementAt(i)%>',
															 '<%=VOTH_TYPE_FLAG.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_SAP_APPROVAL_FLAG.elementAt(i)%>',
															 '<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>','<%=VOTH_AGMT_NO.elementAt(i)%>','<%=VOTH_CONT_NO.elementAt(i)%>','<%=VOTH_PDF_INV_FLAG.elementAt(i)%>');"
															style="<%if(!VOTH_SAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
																		color: orange;
																	<%} else{%>
																		color: brown;
																	<%}%>">													
															</i>
														</td>
														<td align="center">																			
															<i class="fa fa-print fa-2x"
															onclick="printFFLOW_PDF('<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_AGMT_NO.elementAt(i)%>','<%=VOTH_AGMT_REV_NO.elementAt(i)%>','<%=VOTH_CONT_NO.elementAt(i)%>','<%=VOTH_CONT_REV_NO.elementAt(i)%>',
																'<%=VOTH_CONTRACT_TYPE.elementAt(i)%>','<%=VOTH_TRANS_BU_SEQ.elementAt(i)%>','<%=VOTH_PERIOD_START_DT.elementAt(i)%>','<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
																'<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_APPROVE_INVOICE_FLAG.elementAt(i)%>',
																'<%=VMAPPING_ID.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>','<%=VOTH_REMITTANCE_NO.elementAt(i) %>','<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_GTA_COUNTERPTY_CD.elementAt(i)%>')" 
															style="<%if(VOTH_APPROVE_FLAG_CHECK.elementAt(i).equals("N") || VOTH_APPROVE_INVOICE_FLAG.elementAt(i).equals("") || VOTH_PDF_INV_FLAG.elementAt(i).equals("Y")){ %>
																pointer-events: none; opacity: .65; color: gray;
															<%} else{%>
															color:#800000;
															<%}%>">												
															</i>													
														</td>
														<td align="center">	
															<span class="position-relative">
																<i class="fa fa-upload fa-2x" data-bs-toggle="modal" data-bs-target="#FileModal" 
																	onclick="setFFlowUploadParam('<%=VOTH_INVOICE_SEQ.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i)%>','<%=VOTH_CONTRACT_TYPE.elementAt(i)%>','<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_GTA_COUNTERPTY_CD.elementAt(i) %>')"
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
																onclick="openMailBody('<%=VOTH_INVOICE_TYPE.elementAt(i)%>','<%=VOTH_COUNTERPTY_CD.elementAt(i)%>','<%=VOTH_AGMT_NO.elementAt(i)%>',
																	'<%=VOTH_AGMT_REV_NO.elementAt(i)%>','<%=VOTH_CONT_NO.elementAt(i)%>',
																	'<%=VOTH_CONT_REV_NO.elementAt(i)%>','<%=VOTH_CONTRACT_TYPE.elementAt(i)%>',
																	'<%=VOTH_TRANS_BU_SEQ.elementAt(i)%>','<%=VOTH_PERIOD_START_DT.elementAt(i)%>',
																	'<%=VOTH_PERIOD_END_DT.elementAt(i)%>','<%=VOTH_BU_PLANT_SEQ.elementAt(i)%>',
																	'<%=VOTH_BILLING_FREQ_FLAG.elementAt(i)%>','<%=VOTH_FINANCIAL_YEAR.elementAt(i) %>',
																	'<%=VOTH_INVOICE_SEQ.elementAt(i)%>','F');"
																style="<%if(!VOTH_FILE_UPLOAD_COUNT.elementAt(i).toString().equals("0") && VOTH_PDF_INV_FLAG.elementAt(i).equals("Y")){ %>
																color:blue;
																<%}else{%>
																pointer-events: none; opacity: .65; color: gray;
																<%}%>">
															</i>
														</td>
													</tr>
												<%} %>
											<%}else{ %>
												<tr>
													<td align="center" colspan="18"><%=utilmsg.infoMessage("<b>No Free Flow Remittance Generated!</b>") %></td>
												</tr>
											<%} %>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>&nbsp;
				</div>
				<div class="modal-footer cdfooter">
					<div align="center"><%=utilmsg.infoMessage("<b>Remittance list displayed based on Billing Cycle Selection , Free flow Remittance list displayed based on Invoice Date.</b>")%></div>					
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
					    			<input type="hidden" name="invoice_title">
					    			<input type="hidden" name="file_invoice_type">
					    			<input type="hidden" name="upload_inv_type">
					    			<input type="hidden" name="file_gta_counterparty_cd">
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