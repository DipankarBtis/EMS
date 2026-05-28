<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script type="text/javascript">
function refresh(accroid)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var inv_title="SG";
	
	if(document.getElementById("inv_title_"+accroid)!=null && document.getElementById("inv_title_"+accroid)!=undefined)
	{
		inv_title = document.getElementById("inv_title_"+accroid).value;
	}
	
	var flag=true;
	var msg="";
	
	var u = document.forms[0].u.value;
	
	if(trim(from_dt)!="" && trim(to_dt)!="")
	{
		if(flag)
		{
			var url = "frm_purchase_service_invoice.jsp?counterparty_cd="+counterparty_cd+"&from_dt="+from_dt+"&to_dt="+to_dt+
				"&inv_title="+inv_title+"&accroid="+accroid+"&u="+u;
		
			document.getElementById("loading").style.visibility = "visible";
			location.replace(url);
		}
	}
}

var newWindow;
function openActivity1(counterparty_cd,entity,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,period_st_dt,period_end_dt,bu,billing_cycle,activity_type,accroid,inv_flag,mapping_id,financial_year,invoice_seq)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_prepare_service_payment.jsp?counterparty_cd="+counterparty_cd+"&entity="+entity+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+"&activity_type="+activity_type+
			"&accroid="+accroid+"&u="+u+"&inv_flag="+inv_flag+"&mapping_id="+mapping_id+"&financial_year="+financial_year+"&invoice_seq="+invoice_seq;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare Service Payment","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare Seller Payment","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function doGenXML(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,purchase_type_flag,invoice_type,
		sap_approval_flag, bu_seq, cont_no, inv_pdf_flag,accroid,inv_flag,rem_no)
{
	var url = "../purchase/rpt_view_purchase_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
			"&contract_type="+contract_type+"&purchase_type_flag="+purchase_type_flag+"&invoice_type="+invoice_type+
			"&counterparty_cd="+couterpty_cd+"&invoice_no="+invoice_no+"&sap_approval_flag="+sap_approval_flag+
			"&bu_seq="+bu_seq+"&cont_no="+cont_no+"&inv_pdf_flag="+inv_pdf_flag+"&inv_flag="+inv_flag+
			"&accroid="+accroid+"&invPg=Y"+"&remittance_no="+rem_no;;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Purchase SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Purchase SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function printPDF(counterparty_cd,entity,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,period_st_dt,period_end_dt,bu,
		billing_cycle,fin_yr,inv_seq,inv_type,accroid,inv_flag,mapping_id)
{
	var u = document.forms[0].u.value;
	
	var url = "pdf_buy_invoice_remittance.jsp?inv_type="+inv_type+"&counterparty_cd="+counterparty_cd+"&entity="+entity+
		"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&contract_type="+cont_type+
		"&plant_seq="+plant_seq+"&bu_unit="+bu+"&period_start_dt="+period_st_dt+"&period_end_dt="+period_end_dt+
		"&billing_cycle="+billing_cycle+"&financial_year="+fin_yr+"&invoice_seq="+inv_seq+"&accroid="+accroid+
		"&mapping_id="+mapping_id+"&inv_flag="+inv_flag+"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare Service Payment","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare Service Payment","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
}

function openMailBody(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,
		period_st_dt,period_end_dt,bu,billing_cycle,fin_yr,inv_seq, inv_type,mail_inv_type,inv_flag)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_purchase_invoice_mail.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+"&bu_unit="+bu+"&invoice_type="+inv_type+
			"&financial_year="+fin_yr+"&invoice_seq="+inv_seq+"&mail_inv_type="+mail_inv_type+"&inv_flag="+inv_flag+
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

function openPdfFile(url)
{
	window.open(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.remittance.DataBean_ServiceRemittance" id="remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String firstDate="01/"+sysdate.substring(3, sysdate.length());

String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?firstDate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
String inv_title=request.getParameter("inv_title")==null?"SG":request.getParameter("inv_title");

remittance.setCallFlag("REMITTANCE_PREPARATION_LIST");
remittance.setComp_cd(owner_cd);
remittance.setCounterparty_cd(counterparty_cd);
remittance.setFrom_dt(from_dt);
remittance.setTo_dt(to_dt);
remittance.setInv_title(inv_title);
remittance.init();

Vector VMST_COUNTERPARTY_CD = remittance.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = remittance.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = remittance.getVMST_COUNTERPARTY_ABBR();

Vector VREMITTANCE_LIST_ABBR = remittance.getVREMITTANCE_LIST_ABBR();
Vector VREMITTANCE_LIST_NAME = remittance.getVREMITTANCE_LIST_NAME();
Vector VINDEX = remittance.getVINDEX();

Vector VCOUNTERPTY_CD = remittance.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = remittance.getVCOUNTERPTY_ABBR();
Vector VENTITY=remittance.getVENTITY();
Vector VCONT_NO = remittance.getVCONT_NO();
Vector VCONT_REV_NO = remittance.getVCONT_REV_NO();
Vector VAGMT_NO = remittance.getVAGMT_NO();
Vector VAGMT_REV_NO = remittance.getVAGMT_REV_NO();
Vector VSTART_DT = remittance.getVSTART_DT();
Vector VEND_DT = remittance.getVEND_DT();
Vector VCONT_REF_NO = remittance.getVCONT_REF_NO();
Vector VCONTRACT_TYPE = remittance.getVCONTRACT_TYPE();
Vector VPLANT_BU_SEQ = remittance.getVPLANT_BU_SEQ();
Vector VPLANT_BU_ABBR = remittance.getVPLANT_BU_ABBR();
Vector VBU_PLANT_SEQ = remittance.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = remittance.getVBU_PLANT_ABBR();
Vector VDEAL_NO = remittance.getVDEAL_NO();
Vector VPERIOD_START_DT = remittance.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = remittance.getVPERIOD_END_DT();
Vector VINVOICE_NO = remittance.getVINVOICE_NO();
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
Vector VSAP_APPROVAL_FLAG = remittance.getVSAP_APPROVAL_FLAG();
Vector VPAYMENT_TYPE_FLAG = remittance.getVPAYMENT_TYPE_FLAG();
Vector VREMITTANCE_NO = remittance.getVREMITTANCE_NO();
Vector VCARGO_DEAL_NO = remittance.getVCARGO_DEAL_NO();
Vector VCARGO_REF_NO = remittance.getVCARGO_REF_NO();
Vector VMAPPING_ID =remittance.getVMAPPING_ID();
Vector VINV_FLAG =remittance.getVINV_FLAG();
Vector VINVOICE_DT =remittance.getVINVOICE_DT();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
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
					    	Cargo Support Service Remittance Preparation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
						<%-- <div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="">All</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div> --%>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);checkDateRange(this,document.forms[0].to_dt);" onchange="validateDate(this);checkDateRange(this,document.forms[0].to_dt);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);checkDateRange(document.forms[0].from_dt,this);" onchange="validateDate(this);checkDateRange(document.forms[0].from_dt,this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%int i=0,k=0,l=0,m=0;
					for(int j=0; j<VREMITTANCE_LIST_ABBR.size(); j++)
					{ 
						int index=Integer.parseInt(""+VINDEX.elementAt(j));
						String heading = ""+VREMITTANCE_LIST_ABBR.elementAt(j);
						%>
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="<%=heading%>">
	   										<button name="sub_module_cd" class="accordion-button <%if(!accroid.equals(heading)){%>collapsed<%}%> accor-btn" type="button" 
	   											data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="<%if(!accroid.equals(heading)){%>true<%}else{%>false<%} %>" aria-controls="collapse<%=j%>">
								    			<%=VREMITTANCE_LIST_NAME.elementAt(j)%>&nbsp;<font color="blue">(<%=index%> Items)</font>
								      		</button>	
								    	</h2>
										<div id="collapse<%=j%>" class="accordion-collapse collapse <%if(accroid.equals(heading)){%>show<%}%>" aria-labelledby="<%=heading%>">
								      		<div class="accordion-body accor-body">
								      			<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover serchtbl" id="example<%=j%>">
															<thead id="tbsearch<%=j%>">
																<tr>
																	<th class="tbser<%=j%>">Counterparty</th>
																	<th class="tbser<%=j%>">Contract No<br>[Contract Ref#]</th>
																	<th>Contract Period</th>
																	<th class="tbser<%=j%>">Bu Plant</th>
																	<th class="tbser<%=j%>">Business Unit</th>
																	<th class="tbser<%=j%>">Cargo No<br>[Cargo Ref#]</th>
																	<th>Cargo Arrival</th>
																	<th>Billing Cycle</th>
																	<!-- <th>Billing Period</th> -->
																	<th>Invoice#</th>
																	<th>Remittance#</th>
																	<th>Remittance Status</th>
																	<th>Generate IRP</th>
																	<th>IRP Check</th>
																	<th>IRP Approval</th>
																	<th>Fin Ops Finalization</th>
																	<th>SAP<br>XML</th>
																	<th>Print PDF</th>
																	<th>Upload Received<br>Invoice</th>											
																	<th>View PDF
																		<br>
																		<select class="form-select form-select-sm" name="inv_title" id="inv_title_<%=heading%>" style="width: 80px;" onchange="refresh('<%=heading%>');">
																    		<option value="">--Select--</option>
																    		<option value="SG">SG</option>
																    		<option value="PG">PG</option>
																    		<option value="PG_RECV">PG(RECV)</option>
																    	</select>	
																    	<script>document.getElementById("inv_title_<%=heading%>").value="<%=inv_title%>"</script>
																	</th>
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
																		<td align="center">
																			<%=VCOUNTERPTY_ABBR.elementAt(i)%>
																		</td>
																		<td align="center">
																			<font color="blue"><%=VDEAL_NO.elementAt(i)%></font>
																			<br>[<%=VCONT_REF_NO.elementAt(i)%>]
																		</td>
																		<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
																		<td align="center"><%=VPLANT_BU_ABBR.elementAt(i)%></td>
																		<td align="center"><%=VBU_PLANT_ABBR.elementAt(i) %></td>
																		<td align="center">
																			<font color="blue"><%=VCARGO_DEAL_NO.elementAt(i)%></font>
																			<br>[<%=VCARGO_REF_NO.elementAt(i)%>]
																		</td>
																		<td align="center"><%=VPERIOD_END_DT.elementAt(i)%></td>
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
									    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("Delivery Period")){ %>
									    										class="alert" style="background:#ff80bf;color:#33001a;"
									    									<%} %>
									    									><b><%=VBILLING_FREQ_NM.elementAt(i)%></b></span>																							
																		</td>
																		<%-- <td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td> --%>
																		<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
																		<td align="center"><%=VREMITTANCE_NO.elementAt(i)%></td>
																		<td align="center"><%=VSTATUS.elementAt(i) %></td>
																		<td align="center">
																			<%if(VPDF_INV_FLAG.elementAt(i).equals("Y")){ %>
																			<i class="fa fa-eye fa-2x"
																			onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VENTITY.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																					'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>',
																					'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VBILLING_FREQ_FLAG.elementAt(i)%>','','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i)%>',
																					'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>')"
																			style="color:black;">
																			</i>
																			<%}else{ %>
																			<i class="fa fa-cogs fa-2x"
																			onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VENTITY.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																					'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>',
																					'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VBILLING_FREQ_FLAG.elementAt(i)%>','PREPARE','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i)%>',
																					'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>')"
																			style="<%if(VAPPROVE_FLAG_CHECK.elementAt(i).equals("Y")){ %>
																				pointer-events: none; opacity: .65; color: gray;
																					<%} else{%>
																					color:#008080;
																					<%}%>">
																			</i>
																			<%} %>
																		</td>
																		<td align="center">
																		<%if(VCONTRACT_TYPE.elementAt(i).equals("N") && VINV_FLAG.elementAt(i).equals("PF") && VCHECK_FLAG_CHECK.elementAt(i).equals("Y")) { %>
																			<i class="fa fa-stethoscope fa-2x" style="pointer-events: none; opacity: .65; color: gray;"></i>
																		<%}else{ %>
																			<i class="fa fa-stethoscope fa-2x"
																			onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VENTITY.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																					'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>',
																					'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VBILLING_FREQ_FLAG.elementAt(i)%>','CHECK','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i)%>',
																					'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>')"
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
																			<i class="fa fa-stethoscope fa-2x" style="pointer-events: none; opacity: .65; color: gray;"></i>
																		<%}else{ %>
																			<i class="fa fa-thumbs-o-up fa-2x"
																			onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VENTITY.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																						'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>',
																						'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VBILLING_FREQ_FLAG.elementAt(i)%>','AUTHORIZE','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i)%>',
																						'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>')"
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
																			onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VENTITY.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																						'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>',
																						'<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VBILLING_FREQ_FLAG.elementAt(i)%>','APPROVE','<%=heading%>','<%=VINV_FLAG.elementAt(i) %>','<%=VMAPPING_ID.elementAt(i)%>',
																						'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>')"
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
																				<i class="fa fa-eye fa-2x" 
																				onclick="doGenXML('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
																				 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																				 '<%=VPAYMENT_TYPE_FLAG.elementAt(i)%>','','<%=VSAP_APPROVAL_FLAG.elementAt(i)%>',
																				 '<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VPDF_INV_FLAG.elementAt(i)%>','<%=heading%>',
																				 '<%=VINV_FLAG.elementAt(i)%>','<%=VREMITTANCE_NO.elementAt(i)%>');"
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
																			<%} %>
																		</td>
																		<td align="center">																			
																			<i class="fa fa-print fa-2x"
																			onclick="printPDF('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VENTITY.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
																				'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																				'<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VAPPROVE_INVOICE_FLAG.elementAt(i)%>','<%=heading%>',
																				'<%=VINV_FLAG.elementAt(i)%>','<%=VMAPPING_ID.elementAt(i)%>')" 
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
																					onclick="setUploadParam('<%=VINVOICE_SEQ.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=heading%>','<%=VINV_FLAG.elementAt(i)%>')"
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
																				onclick="openMailBody('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
																					'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
																					'<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																					'<%=VPLANT_BU_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>',
																					'<%=VPERIOD_END_DT.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>',
																					'<%=VBILLING_FREQ_FLAG.elementAt(i)%>','<%=VFINANCIAL_YEAR.elementAt(i) %>',
																					'<%=VINVOICE_SEQ.elementAt(i)%>','','<%=VAPPROVE_INVOICE_FLAG.elementAt(i)%>','<%=VINV_FLAG.elementAt(i)%>');"
																				style="<%if(!VFILE_UPLOAD_COUNT.elementAt(i).toString().equals("0") && VPDF_INV_FLAG.elementAt(i).equals("Y")){ %>
																				color:blue;
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
																<%} 
															}else{%>
																<tr>
																	<td align="center" colspan="20"><%=utilmsg.infoMessage("<b>No "+VREMITTANCE_LIST_NAME.elementAt(j)+" is Ready for Generate!</b>") %></td>
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


<input type="hidden" name="option" value="">
<input type="hidden" name="prev_billing_cycle" value="<%//=billing_cycle%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="submission_from" value="SERVICE">

<input type="hidden" name="counterparty_cd" value=""> <!-- for time being added this input filed as removed counterparty filter -->

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
</html>