
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh(clearance)
{
	var prev_clearance = document.forms[0].prev_clearance.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	if(prev_clearance != clearance)
	{
		counterparty_cd="0";
	}
	
	var u = document.forms[0].u.value;
		
	var url = "frm_advance_booking.jsp?counterparty_cd="+counterparty_cd+"&clearance="+clearance+"&u="+u;
			
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doRestate()
{
	var sec_type = document.forms[0].sec_type.value;
	
	if(sec_type="ADV")
	{
		document.getElementById("AdvdealConfig").disabled=false;
		document.forms[0].adv_sec_category.disabled=false;
		document.forms[0].adv_deal_type.disabled=false;
		document.forms[0].adv_currency.disabled=false;
		document.forms[0].adv_value.disabled=false;
		document.forms[0].adv_received_dt.disabled=false;
		document.forms[0].adv_pg_ref.disabled=false;
		document.forms[0].adv_remark.disabled=false;
		document.forms[0].adv_status.disabled=false;
	} 
	var a =  confirm("Do you want to Restate the Advance Booking Details?");
	if(a)
	{
		document.forms[0].opration.value="RESTATE";
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].submit();
	}
	else
	{
		document.getElementById("AdvdealConfig").disabled=true;
		document.forms[0].adv_sec_category.disabled=true;
		document.forms[0].adv_deal_type.disabled=true;
		document.forms[0].adv_currency.disabled=true;
		document.forms[0].adv_value.disabled=true;
		document.forms[0].adv_received_dt.disabled=true;
		document.forms[0].adv_pg_ref.disabled=true;
		document.forms[0].adv_status.disabled=true;
	}
}

function doModifyAdv(VSEC_CATEGORY ,VCURRENCY ,VVALUE ,VRECEIVED_DATE ,VDEAL_NO ,VCOUNTERPARTY_CD ,VSEC_REF_NO,VREMARK,VDEAL_TYPE,
		VCOUNTERPARTY_NAME,VSEQ_NO,VDEAL_DTL,VSEC_TYPE,VSTATUS,VCOUNTERPARTY_ABBR,VSEQ_REV_NO,VEXP_VAL,VADV_PG_REF)
{
	document.forms[0].adv_status_flg.value = VSTATUS;
	document.forms[0].adv_counterparty_abbr.value = VCOUNTERPARTY_ABBR;
	document.forms[0].adv_counterparty_name.value = VCOUNTERPARTY_NAME;
	document.forms[0].adv_sec_category.value = VSEC_CATEGORY;
	if(VDEAL_NO!="")
	{
		document.forms[0].adv_deal_no.value = VDEAL_NO;
	}
	document.forms[0].adv_currency.value = VCURRENCY;
	document.forms[0].adv_value.value = VVALUE;
	document.forms[0].adv_received_dt.value = VRECEIVED_DATE;
	document.forms[0].adv_pg_ref.value = VADV_PG_REF;
	document.forms[0].adv_remark.value = VREMARK;
	document.forms[0].adv_counterpty_cd.value = VCOUNTERPARTY_CD;
	document.forms[0].adv_seq_no.value = VSEQ_NO;
	document.forms[0].adv_ref_no.value = VSEC_REF_NO;
	document.forms[0].sec_type.value = VSEC_TYPE;
	document.forms[0].adv_seq_rev_no.value=VSEQ_REV_NO;
	
	if(VSTATUS!="P")
	{
		document.forms[0].adv_status.value = VSTATUS;
	}
	else
	{
		document.forms[0].adv_status.value = "";
	}
	
	if(VSTATUS=="R")
	{
		document.forms[0].adv_status.style.display = "none";
		document.forms[0].adv_status1.style.display = "block";
		document.forms[0].adv_status1.value = "Restated";
	}
	
	if(VSTATUS=="O" || VSTATUS=="R" || VSTATUS=="C" || VEXP_VAL=="Y")
	{
		document.getElementById("adv_reset_btn").style.display="block";
		document.getElementById("AdvdealConfig").disabled=true;
		document.forms[0].adv_sec_category.disabled=true;
		document.forms[0].adv_deal_type.disabled=true;
		document.forms[0].adv_currency.disabled=true;
		document.forms[0].adv_value.disabled=true;
		document.forms[0].adv_received_dt.disabled=true;
		document.forms[0].adv_pg_ref.disabled=true;
		document.forms[0].adv_status.disabled=true;
		document.forms[0].adv_remark.disabled=true;
		document.forms[0].adv_status2.disabled=true;
	}
	else
	{
		document.getElementById("adv_reset_btn").style.display="none";
		document.getElementById("adv_sub_btn").style.display="block";
		document.getElementById("AdvdealConfig").disabled=false;
	}
	
	var adv_cancel_flg = "";
	if(VSTATUS=="O")
	{
		document.forms[0].adv_status.style.display="none";
		document.forms[0].adv_status2.value=VSTATUS;
		adv_cancel_flg = "Y";
	}
	else
	{
		document.forms[0].adv_status.value=VSTATUS;
		document.forms[0].adv_status2.style.display="none";
		adv_cancel_flg="";
	}
	document.forms[0].adv_cancel_flg.value=adv_cancel_flg;
	
	if(VSTATUS=="R" || VSTATUS=="C" || VEXP_VAL=="Y" || VSTATUS=="O")
	{
		document.forms[0].adv_remark.disabled=true;
		document.forms[0].adv_status.disabled=true;
		document.forms[0].adv_status2.disabled=true;
		document.getElementById("adv_reset_btn").style.display="none";
		document.getElementById("adv_sub_btn").style.display="none";
	}
		
	document.forms[0].adv_deal_type.value=VDEAL_TYPE;
	var security_type = document.forms[0].sec_type.value;
	var secure_type="";
	
	if(security_type=="ADV")
	{
		secure_type="Advance Payment";
	}
	var security_ref_no = document.forms[0].adv_ref_no.value;
	var abbr = document.forms[0].adv_counterparty_abbr.value;
	
	document.getElementById("adv_hdr").innerHTML = "Modify "+secure_type+"("+security_ref_no+")"; 
	document.getElementById("adv_deal_hdr").innerHTML = abbr+" Deal/s";
	
	document.forms[0].opration.value="MODIFY";
	
	if(VSTATUS=="O")
	{
		VSTATUS="In Order";
	}
	else if(VSTATUS=="A")
	{
		VSTATUS="Pending for Amendment";
	}
	else if(VSTATUS=="P")
	{
		VSTATUS="Pending";
	}
	else if(VSTATUS=="C")
	{
		VSTATUS="Cancelled";
	}
	else if(VSTATUS=="R")
	{
		VSTATUS="Restated";
	}
	
	if(VSEC_CATEGORY=="R")
	{
		VSEC_CATEGORY="Incoming";
	}
	else if(VSEC_CATEGORY=="I")
	{
		VSEC_CATEGORY="Outgoing";
	}
	
	if(VCURRENCY=="1")
	{
		VCURRENCY="INR";
	}
	else if(VCURRENCY=="2")
	{
		VCURRENCY="USD";
	}
	
	var clearance = document.forms[0].clearance.value;
	
	var old_value = "CP="+VCOUNTERPARTY_CD+"#NAME="+VCOUNTERPARTY_NAME+"#ABBR="+VCOUNTERPARTY_ABBR+"#SEC_TYPE="+VSEC_TYPE+"#SEC_CATEGORY="+VSEC_CATEGORY+"#DEAL_TYPE="+VDEAL_TYPE+"#DEAL_NO="+VDEAL_NO+"#VALUE="+VVALUE+"#CURRENCY="+VCURRENCY+"#FLUCTUATION="+""+"#VARIATION="+""+
	    			"#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+
					"#CONF_BANK_REF="+""+"#RECIEVED_DT="+VRECEIVED_DATE+"#REVIEW_DT="+""+"#ISSUANCE_DT="+""+"#EXPIRY_DT="+""+"#STATUS="+VSTATUS+"#TENOR="+""+"#REMARK="+VREMARK+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+VSEC_REF_NO+"#CANCEL_DT="+""+"#GX="+clearance;

	document.forms[0].adv_old_value.value=old_value;
	
}

function doSubmitAdv()
{
	document.getElementById("AdvdealConfig").disabled=false;
	document.forms[0].adv_sec_category.disabled=false;
	document.forms[0].adv_deal_type.disabled=false;
	document.forms[0].adv_currency.disabled=false;
	document.forms[0].adv_value.disabled=false;
	document.forms[0].adv_received_dt.disabled=false;
	document.forms[0].adv_pg_ref.disabled=false;
	document.forms[0].adv_remark.disabled=false;
	document.forms[0].adv_status.disabled=false;
    document.forms[0].adv_chk_deal.disabled=false;
	
    var adv_status_flg = document.forms[0].adv_status_flg.value;
	var adv_sec_category = document.forms[0].adv_sec_category.value;
	var adv_deal_type = document.forms[0].adv_deal_type.value;
	var adv_currency = document.forms[0].adv_currency.value; 
	var adv_value = document.forms[0].adv_value.value;
	var adv_received_dt = document.forms[0].adv_received_dt.value;
	var adv_pg_ref = document.forms[0].adv_pg_ref.value;
	var adv_status = document.forms[0].adv_status.value;
    var adv_chk_deal = document.forms[0].adv_chk_deal.value;
	
	var opration = document.forms[0].opration.value;
	var msg="";
	var flag=true;
	
	if(adv_status!="C")
	{
		if(trim(adv_deal_type)=="" || adv_deal_type==0)
		{
			msg+="Please Select Deal Type!\n";
			flag=false;
		}
		if(trim(adv_currency)=="" || adv_currency==0)
		{
			msg+="Please Select currency Type!\n";
			flag=false;
		}
		if(trim(adv_value)=="")
		{
			msg+="Please Enter Value!\n";
			flag=false;
		}
	    if(trim(adv_chk_deal)=="")
		{
			msg+="Please Select Contract#!\n";
			flag=false;
		}	
		if(trim(adv_received_dt)=="")
		{
			msg+="Please Enter Received Date!\n";
			flag=false;
		}
		if(trim(adv_status)=="")
		{
			msg+="Please Select Status!\n";
			flag=false;
		}
		if(trim(adv_pg_ref)=="") // Party Generated Ref# made mandatory for SAP XML generation as suggected by Vijay & Divya 
		{
			msg+="Please Enter Party Generated Ref#!\n";
			flag=false;
		}
	}
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" the Security Details?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
		else
		{
			if(adv_status_flg=="O")
			{
				document.getElementById("AdvdealConfig").disabled=true;
				document.forms[0].adv_sec_category.disabled=true;
				document.forms[0].adv_deal_type.disabled=true;
				document.forms[0].adv_currency.disabled=true;
				document.forms[0].adv_value.disabled=true;
				document.forms[0].adv_received_dt.disabled=true;
				document.forms[0].adv_pg_ref.disabled=true;
				document.forms[0].adv_chk_deal.disabled=true;
			}
		}
	}
	else
	{
		alert(msg);
	}
} 

function NewSubmit()
{
	var counterparty_cd = document.forms[0].counterparty.value;
	var new_sec_type = document.forms[0].new_sec_type.value;
	var new_reciept_dt = document.forms[0].new_reciept_dt.value;
	var new_currency = document.forms[0].new_currency.value;
	var new_value = document.forms[0].new_value.value;
	var new_remark = document.forms[0].new_remark.value;
	var new_category = document.forms[0].new_category.value;
	var new_deal_no = document.forms[0].new_deal_no.value;
	var new_chk_deal = document.forms[0].new_chk_deal.value;
	
	
	var opration = document.forms[0].opration.value;
	var msg="";
	var flag=true;
	
	if(trim(counterparty_cd)=="" || counterparty_cd==0)
	{
		msg+="Please Select Counterparty!\n";
		flag=false;
	}
	if(trim(new_category)=="" || new_category==0)
	{
		msg+="Please Select Category!\n";
		flag=false;
	}
	if(trim(new_sec_type)=="" || new_sec_type==0)
	{
		msg+="Please Select Security Type!\n";
		flag=false;
	}
	if(trim(new_chk_deal)=="" || new_chk_deal==0)
	{
		msg+="Please Select Contract#!\n";
		flag=false;
	}	
	if(trim(new_reciept_dt)=="" || new_reciept_dt==0)
	{
		msg+="Please Enter Reciept Date!\n";
		flag=false;
	}
	if(trim(new_currency)=="" || new_currency==0)
	{
		msg+="Please Select Currency!\n";
		flag=false;
	}
	if(trim(new_value)=="" || new_value==0)
	{
		msg+="Please Enter Amount!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" the Security Details?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

var newWindow;
function doGenXML(couterpty_cd,clearance,seq_no, seq_rev_no, sap_approval_flag,sec_ref_no,isReversal, isPrintPdf)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_view_advance_sap_approval.jsp?&counterparty_cd="+couterpty_cd+
			"&clearance="+clearance+"&seq_no="+seq_no+"&seq_rev_no="+seq_rev_no+"&sap_approval_flag="+sap_approval_flag+
			"&sec_ref_no="+sec_ref_no+"&isReversal="+isReversal+"&isPrintPdf="+isPrintPdf+"&u="+u;
			

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Advance SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Advance SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}


function printPDF(counterparty_cd,seq_no, seq_rev_no, clearance, isReversal)
{
	
	var url = "view_advance_remittance.jsp?counterparty_cd="+counterparty_cd+"&seq_no="+seq_no+
		"&seq_rev_no="+seq_rev_no+"&gx="+clearance+"&isReversal="+isReversal;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Generate Advance Remittance PDF","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Generate Advance Remittance PDF","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
}

function openPdfFile(url)
{
	window.open(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DataBean_CreditRisk" id="credit_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String sec_category = request.getParameter("sec_category")==null?"":request.getParameter("sec_category");
String clearance = request.getParameter("clearance")==null?"K":request.getParameter("clearance");
String status = request.getParameter("status")==null?"":request.getParameter("status");
String adv_flag = "ADV";

credit_risk.setCallFlag("SECURITY_COLLATERAL_MANAGEMENT");
credit_risk.setCounterparty_cd(counterparty_cd);
credit_risk.setClearance(clearance);
credit_risk.setComp_cd(owner_cd);
credit_risk.setAdv_flag(adv_flag);
credit_risk.init();

Vector VMST_COUNTERPARTY_CD = credit_risk.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = credit_risk.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = credit_risk.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_CD = credit_risk.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NAME = credit_risk.getVCOUNTERPARTY_NAME();
Vector VSEC_CATEGORY = credit_risk.getVSEC_CATEGORY();
Vector VSEC_TYPE = credit_risk.getVSEC_TYPE();
Vector VSEC_REF_NO = credit_risk.getVSEC_REF_NO();
Vector VVALUE = credit_risk.getVVALUE();
Vector VRECEIVED_DATE = credit_risk.getVRECEIVED_DATE();
Vector VCURRENCY = credit_risk.getVCURRENCY();
Vector VSTATUS = credit_risk.getVSTATUS();
Vector VMST_BANK_CD = credit_risk.getVMST_BANK_CD();
Vector VMST_BANK_NM = credit_risk.getVMST_BANK_NM();
Vector VMST_BANK_ABBR = credit_risk.getVMST_BANK_ABBR();
Vector VMST_BRANCH_NAME = credit_risk.getVMST_BRANCH_NAME();
Vector VDEAL_NO = credit_risk.getVDEAL_NO();
Vector VISS_BANK_NM = credit_risk.getVISS_BANK_NM();
Vector VVALUE_FLUCTUATION = credit_risk.getVVALUE_FLUCTUATION();
Vector VDEAL_TYPE = credit_risk.getVDEAL_TYPE();
Vector VISS_BANK_REF = credit_risk.getVISS_BANK_REF();
Vector VADV_BANK_NM = credit_risk.getVADV_BANK_NM();
Vector VADV_BANK_REF = credit_risk.getVADV_BANK_REF();
Vector VCONFIRM_BANK_NM = credit_risk.getVCONFIRM_BANK_NM();
Vector VCONFIRM_BANK_REF = credit_risk.getVCONFIRM_BANK_REF();
Vector VREVIEW_DT = credit_risk.getVREVIEW_DT();
Vector VTENOR = credit_risk.getVTENOR();
Vector VISSUE_DT = credit_risk.getVISSUE_DT();
Vector VEXPIRE_DT = credit_risk.getVEXPIRE_DT();
Vector VREMARK = credit_risk.getVREMARK();
Vector VVALUE_VARIATION = credit_risk.getVVALUE_VARIATION();
Vector VGUARANTOR_NM = credit_risk.getVGUARANTOR_NM();
Vector VGUARANTOR_CD = credit_risk.getVGUARANTOR_CD();
Vector VISS_BANK_CD = credit_risk.getVISS_BANK_CD();
Vector VADV_BANK_CD = credit_risk.getVADV_BANK_CD();
Vector VCONFIRM_BANK_CD = credit_risk.getVCONFIRM_BANK_CD();
Vector VCANCEL_DT = credit_risk.getVCANCEL_DT();
Vector VRENEW_DT = credit_risk.getVRENEW_DT();
Vector V_DEAL_NO = credit_risk.getV_DEAL_NO();
Vector V_DURATION = credit_risk.getV_DURATION();
Vector VAGMT_NO = credit_risk.getVAGMT_NO();
Vector VAGMT_REV_NO = credit_risk.getVAGMT_REV_NO();
Vector VCONT_NO = credit_risk.getVCONT_NO();
Vector VCONT_REV_NO = credit_risk.getVCONT_REV_NO();
Vector VCONTRACT_TYPE = credit_risk.getVCONTRACT_TYPE();
Vector VCONTRACT_DTL =credit_risk.getVCONTRACT_DTL();
Vector VDEAL_DTL =credit_risk.getVDEAL_DTL();
Vector VSEQ_NO = credit_risk.getVSEQ_NO();
Vector VSEL_DEAL_DTL = credit_risk.getVSEL_DEAL_DTL();
Vector VSEQ_REV_NO = credit_risk.getVSEQ_REV_NO();
Vector VCOUNTERPARTY_ABBR = credit_risk.getVCOUNTERPARTY_ABBR();
Vector VALL_DEAL_DTL = credit_risk.getVALL_DEAL_DTL();
Vector VALL_DEAL_NO = credit_risk.getVALL_DEAL_NO();
Vector VEXP_VAL = credit_risk.getVEXP_VAL();
Vector VNEW_COUNTPTY_ABBR = credit_risk.getVNEW_COUNTPTY_ABBR();
Vector VALL_CP_ABBR = credit_risk.getVALL_CP_ABBR();//HP20230914
Vector VSAP_APPROVAL_FLAG = credit_risk.getVSAP_APPROVAL_FLAG();
Vector VSAP_REVERSAL_FLAG = credit_risk.getVSAP_REVERSAL_FLAG();
Vector VDURATION = credit_risk.getVDURATION();
Vector VADV_PG_REF = credit_risk.getVADV_PG_REF();
Vector VPDF_REV_GENERATED = credit_risk.getVPDF_REV_GENERATED();
Vector VPDF_GENERATED = credit_risk.getVPDF_GENERATED();
Vector VPDF_FILE_NAME = credit_risk.getVPDF_FILE_NAME();
Vector VPDF_REV_FILE_NAME = credit_risk.getVPDF_REV_FILE_NAME();



String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//";
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd+"//security//adv//";
//System.out.println("VPDF_GENERATED...."+VPDF_GENERATED+"...VPDF_REV_GENERATED..."+VPDF_REV_GENERATED);
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_CreditRisk">
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
				    		Advance Booking  
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row" >
						<div class="col-sm-3 col-xs-3 col-md-12">
							<div align="center">
								<div class="btn-group" >
									<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("K")){%>btnactive<%}%>" onclick="refresh('K')">KYC</label>
									<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("I")){%>btnactive<%}%>" onclick="refresh('I')">IGX</label>
									<!-- HP20230914 ENABLED IGX SELECTION -->
								</div>
							</div>
						</div>
					</div>					
					<div class="row">						
						<div class="col-sm-3 col-xs-3 col-md-12">
							<div class="d-flex justify-content-between"> 
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>Counterparty :</b></label>
									</div>
									<div class="col-auto">
										<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=clearance%>')">
											<option value="0" lable="0" selected="selected">--Select--</option>
											<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
												<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
											<%} %> 
										</select>
										<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
									</div> 
								</div>
								<div class="btn-group" align="right">
									<label class="btn btn-outline-secondary subbtngrp1" data-bs-toggle="modal" data-bs-target="#AddNewSecurity">Add New Advance</label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						<%if(!counterparty_cd.equals("0")){ %>
							<div class="table-responsive">
								<table class="table table-bordered" id="filterbysearch">
									<thead>
										<tr>
											<th>SR#</th>
											<th>Counterparty<div align="center"><input class="form-control form-control-sm" type="text" id="counterparty_nm" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Incoming/ Outgoing<div align="center"><input class="form-control form-control-sm" type="text" id="category" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Security Type<div align="center"><input class="form-control form-control-sm" type="text" id="security_type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Security Ref.<div align="center"><input class="form-control form-control-sm" type="text" id="security_reference_no" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Status<div align="center"><input class="form-control form-control-sm" type="text" id="stats" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Deal Type<div align="center"><input class="form-control form-control-sm" type="text" id="dealType" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Deal No.<div align="center"><input class="form-control form-control-sm" type="text" id="dealNo" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Currency</th>
											<th>value</th>
											<th>Received Date</th>
											<th>Party Generated Ref.<div align="center"><input class="form-control form-control-sm" type="text" id="pg_ref" onkeyup="Search(this,'11');" placeholder="Search.." style="width:100px"/></div></th>
											<!-- <th>Issuance Date</th> -->
											<!-- <th>Expire Date</th> -->
											<!-- <th>Cancellation Date</th> -->
											<!-- <th>Renew Date</th> -->
											<!-- <th>Remarks</th> -->
											<%if(clearance.equals("I")){ %>	
											<th>View / Print PDF</th>
											<th>SAP Approval</th>
											<th>View / Print <br>Reversal PDF</th>
											<th>SAP Reversal</th>
											<%} %>
										</tr>
									</thead>
									<tbody>
									<%if(VSEC_REF_NO.size()>0) {%>
									<%int k=0;
									for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){
										k+=1;
										String security_type = ""+VSEC_TYPE.elementAt(i);
										String security_category = ""+VSEC_CATEGORY.elementAt(i);
										%>
										<tr>
											<td align="right">
											<%if(write_access.equals("Y")){ %>
												<font title="Click to Edit" <%if(VEXP_VAL.elementAt(i).equals("Y") || VSTATUS.elementAt(i).equals("R") || VSTATUS.elementAt(i).equals("C") || VSTATUS.elementAt(i).equals("O")){%><%}else{ %>style="color:var(--header_color)"<%} %>>
													<%if(security_type.equals("ADV")){ %>
													<i <%if(VEXP_VAL.elementAt(i).equals("Y") || VSTATUS.elementAt(i).equals("R") || VSTATUS.elementAt(i).equals("C") || VSTATUS.elementAt(i).equals("O")){%>class="fa fa-eye fa-lg" <%}else{ %>class="fa fa-edit fa-lg"<%} %>
														 data-bs-toggle="modal" 
														data-bs-target="#SecurityAdv" onclick="doModifyAdv('<%=VSEC_CATEGORY.elementAt(i)%>','<%=VCURRENCY.elementAt(i)%>','<%=VVALUE.elementAt(i)%>','<%=VRECEIVED_DATE.elementAt(i)%>',
														'<%=VDEAL_NO.elementAt(i)%>','<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VSEC_REF_NO.elementAt(i) %>','<%=VREMARK.elementAt(i)%>','<%=VDEAL_TYPE.elementAt(i)%>',
														'<%=VCOUNTERPARTY_NAME.elementAt(i) %>','<%=VSEQ_NO.elementAt(i) %>','<%=VDEAL_DTL.elementAt(i) %>','<%=VSEC_TYPE.elementAt(i) %>',
														'<%=VSTATUS.elementAt(i)%>','<%=VCOUNTERPARTY_ABBR.elementAt(i) %>','<%=VSEQ_REV_NO.elementAt(i) %>','<%=VEXP_VAL.elementAt(i)%>','<%=VADV_PG_REF.elementAt(i) %>');
														setDealNoDtl('<%=VSEL_DEAL_DTL.elementAt(i) %>'); showDealNo();checkDealType()">
													</i>
													<%} %>
												</font>&nbsp;
												<%} %>
												<%=k%>
											</td>
											<td align="center"><%=VCOUNTERPARTY_NAME.elementAt(i) %></td>
											<td align="center">
												<%if(security_category.equals("R")){ %>
													Incoming
												<%}else if(security_category.equals("I")){ %>
													Outgoing
												<%} %>
											</td>
											<td align="center">
											<span 
											<%if(VSEC_TYPE.elementAt(i).equals("LC")){ %>
	    										class="alert alert-info"
	    									<%}else if(VSEC_TYPE.elementAt(i).equals("BG")){ %>
	    										class="alert alert-warning"
	    									<%}else if(VSEC_TYPE.elementAt(i).equals("PCG")){ %>
	    										class="alert alert-primary"
	    									<%}else if(VSEC_TYPE.elementAt(i).equals("ADV")){ %>
	    										class="alert alert-dark"
    										<%}else if(VSEC_TYPE.elementAt(i).equals("OA")){ %>
	    										class="alert alert-danger"
	    									<%}%>><%=VSEC_TYPE.elementAt(i) %></span></td>
											<td align="center"><%=VSEC_REF_NO.elementAt(i) %></td>
											<td align="center">
												<%if(VSTATUS.elementAt(i).equals("P")){ %>
													<span class="alert alert-primary">Pending</span>
												<%}else if(VSTATUS.elementAt(i).equals("O")){  %>
													<span class="alert alert-success">In Order</span>
												<%}else if(VSTATUS.elementAt(i).equals("C")){  %>
													<span class="alert alert-danger">Cancelled</span>
												<%}else if(VSTATUS.elementAt(i).equals("A")){  %>
													<span class="alert alert-secondary">Pending For Amendment</span>
												<%}else if(VSTATUS.elementAt(i).equals("R")){  %>
													<span class="alert alert-warning">Restated</span>
												<%} %>
											</td>
											<td align="center"><%=VDEAL_TYPE.elementAt(i) %></td>
											<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
											<td align="center">
											<%if(VCURRENCY.elementAt(i).equals("1")){ %>
												INR
											<%}else if(VCURRENCY.elementAt(i).equals("2")){ %>
												USD
											<%}else{} %>
											</td>
											<td align="right"><%=VVALUE.elementAt(i) %></td>
											<td align="center"><%=VRECEIVED_DATE.elementAt(i) %></td>
											<td align="center"><%=VADV_PG_REF.elementAt(i) %></td>
											<%-- <td align="center"><%=VISSUE_DT.elementAt(i) %></td> --%>
											<%-- <td align="center"><%=VEXPIRE_DT.elementAt(i) %></td> --%>
											<%-- <td align="center"><%=VCANCEL_DT.elementAt(i) %></td> --%>
											<%-- <td align="center"><%=VRENEW_DT.elementAt(i) %></td> --%>
											<%-- <td align="center"><%=VREMARK.elementAt(i) %></td> --%>
											<%if(clearance.equals("I")){ %>
											<td align="center">
											<%if(!VSTATUS.elementAt(i).equals("C")){ %>
											<%if(!VPDF_GENERATED.elementAt(i).equals("Y")){ %>
												<i class="fa fa-print fa-2x"
													onclick="printPDF('<%=VCOUNTERPARTY_CD.elementAt(i)%>', '<%=VSEQ_NO.elementAt(i) %>', 
																		'<%=VSEQ_REV_NO.elementAt(i) %>', '<%=clearance%>','N');" 
													style="color:#800000;">												
												</i>		
											<%} else {%>
												<i class="fa fa-file-pdf-o fa-2x"
													onclick="openPdfFile('<%=file_url%><%=VPDF_FILE_NAME.elementAt(i)%>');" 
													style="color:red;">												
												</i>
											<%} %>	
											<%} %>	
											</td> 
											<td align="center">		
											<%if(VSTATUS.elementAt(i).equals("O")){ %>
												<i class="fa <%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
														onclick="doGenXML('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=clearance%>',
													 '<%=VSEQ_NO.elementAt(i)%>',<%=VSEQ_REV_NO.elementAt(i) %>,'<%=VSAP_APPROVAL_FLAG.elementAt(i)%>',
													 '<%=VSEC_REF_NO.elementAt(i)%>','N', '<%=VPDF_GENERATED.elementAt(i)%>');"
													style="<%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>color: orange;<%}%>">
												</i>
											<%}%>
											</td>	
											<td align="center">
											<%if(!VSTATUS.elementAt(i).equals("C")){ %>
											<%if(!VPDF_REV_GENERATED.elementAt(i).equals("Y")){ %>
												<i class="fa fa-print fa-2x"
													onclick="printPDF('<%=VCOUNTERPARTY_CD.elementAt(i)%>', '<%=VSEQ_NO.elementAt(i) %>', 
																		'<%=VSEQ_REV_NO.elementAt(i) %>', '<%=clearance%>','Y');" 
													style="<%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
														pointer-events: none; opacity: .65; color: gray;
													<%} else{%>
													color:#800000;
													<%}%>">												
												</i>
											<%} else {%>
												<i class="fa fa-file-pdf-o fa-2x"
													onclick="openPdfFile('<%=file_url%><%=VPDF_REV_FILE_NAME.elementAt(i)%>');" 
													style="color:red;">												
												</i>
											<%} %>
											<%} %>				
											</td>
											<td align="center">			
											<%if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
												<i class="fa <%if(!VSAP_REVERSAL_FLAG.elementAt(i).equals("Y")){ %>fa-file-code-o<%}else{%>fa-eye<%} %> fa-2x" 
														onclick="doGenXML('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=clearance%>',
													 '<%=VSEQ_NO.elementAt(i)%>',<%=VSEQ_REV_NO.elementAt(i) %>,'<%=VSAP_REVERSAL_FLAG.elementAt(i)%>',
													 '<%=VSEC_REF_NO.elementAt(i)%>','Y', '<%=VPDF_REV_GENERATED.elementAt(i)%>');"
													style="<%if(!VSAP_REVERSAL_FLAG.elementAt(i).equals("Y")){ %>color: orange;<%}%>">
												</i>
											<%}%>
											<%} %>													
										</tr>
										<%} %>
										<%}else{ %>
											<tr><td colspan="22" align="center"><%=utilmsg.infoMessage("<b>Advance Booking Is Not Available!</b>") %></td></tr>
										<%} %>
									</tbody>
								</table>
							</div>
							<%}else{ %>
								<div colspan="22" align="center"><%=utilmsg.infoMessage("<b>Please Select any Counterparty!</b>") %></div>
							<%} %>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="SecurityAdv" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader" id="adv_hdr">
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" onclick="refresh('<%=clearance%>')"> 
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Counterparty</b></label>
				  			</div>
						</div>
	   					<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="adv_counterparty_name" readonly>
									<input type="hidden" class="form-control form-control-sm" name="adv_counterpty_cd" readonly>
									<input type="hidden" class="form-control form-control-sm" name="adv_counterparty_abbr" readonly>
						      	</div>
						    </div>
						</div>
	      				<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Category<span class="s-red">*</span></b></label>
				  			</div>
						</div>
	   					<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="adv_sec_category">
				      					<option value="" selected="selected" >--Select--</option>
				      					<option value="R">Incoming</option>
				      					<option value="I">Outgoing</option>
						      		</select>
						      	</div>
						    </div>
						</div>
      				</div>
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Deal Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="adv_deal_type" id="adv_deal_typ" onchange="checkDealType()">
				      					<option value="" selected="selected" >--Select--</option>
				      					<option value="GAS" >GAS</option>
				      					<option value="LTCORA" >LTCORA</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="button" class="btn btn-sm config_btn" id="AdvdealConfig" value="Attach Contract#" 
					    			data-bs-toggle="modal" data-bs-target="#AdvDealNoModel" onclick="showDealNo()">
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label" id="AdvDealNoDisplay" style="display:none;"></label>
				      			</div>
				  			</div>
						</div>
					</div>
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Security Value<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="adv_value">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="adv_currency">
				      					<option value="1" selected="selected">INR</option>
				      					<option value="2">USD</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Consumed Value</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="" readonly>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Recv./Due. Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="adv_received_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Party Generated Ref#<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
		      						<input type="text" class="form-control form-control-sm" name="adv_pg_ref" maxlength="12">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Status<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="adv_status">
				      					<option value="" selected="selected">--Select--</option>
				      					<option value="O">In order</option>
				      					<option value="A">Pending for Amendment</option>
				      					<option value="C">Cancelled</option>
				      				</select>
				      				<select class="form-select form-select-sm" name="adv_status2" >
				      					<option value="O">In order</option>
				      					<option value="C">Cancelled</option>
				      				</select>
				      				<input type="text" class="form-control form-control-sm" name="adv_status1" readonly style="display: none;">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Remark</b></label>
				  			</div>
						</div>
						<div class="col-sm-12 col-xs-12 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="adv_remark">
				    			</div>
				  			</div>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
	      			<div align="left" >
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" id="adv_reset_btn" value="Restate" onclick="doRestate();" style="display: none;">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" id="adv_reset_btn" value="Restate" style="display: none;">
						<%} %>
					</div>
	        		<div align="right" >
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" id="adv_sub_btn" value="Submit" onclick="doSubmitAdv();" style="display: button;">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" id="adv_sub_btn" value="Submit" disabled style="display: button;">
						<%} %>
					</div>
				</div>
      		</div>
      	</div>
	</div>
</div>

<div class="modal fade" id="AddNewSecurity" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
        			Add New Advance
        		</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" onclick="refresh('<%=clearance%>')">
        	</div>
        	<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Counterparty<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="counterparty" >
										<option value="0" lable="Select">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
											<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %> 
									</select>
									<script>document.forms[0].counterparty.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Category<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<select class="form-select form-select-sm" name="new_category">
			      							<option value="" selected="selected" >--Select--</option>
					      					<option value="R">Incoming</option>
					      					<option value="I">Outgoing</option>
				      					</select>
		      						</div>
				    			</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Security Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="new_sec_type">
				      					<option value="ADV" selected="selected">ADV Advance Payment</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="button" class="btn btn-sm config_btn" value="Attach Contract#" 
					    			data-bs-toggle="modal" data-bs-target="#NewDealNoModel" onclick="showNewDealNo()">
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label" id="NewDealNoDisplay" style="display:none;"></label>
				      			</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Expected Receipt(Due) Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="new_reciept_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Amount<span class="s-red">*</span></b></label>
				  			</div>
						</div>
				  		<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="new_value">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">  
							<div class="form-group row">
				    			<div class="col-sm-10 col-xs-10 col-md-10">
				      				<select class="form-select form-select-sm" name="new_currency">
				      					<option value="1" selected="selected">INR</option>
				      					<option value="2">USD</option>
				      				</select>
				    			</div>
				  			</div>
				  		</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Remark</b></label>
				  			</div>
						</div>
						<div class="col-sm-12 col-xs-12 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="new_remark">
				    			</div>
				  			</div>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div align="right">
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="NewSubmit();">
				</div>
      		</div>
        </div>
	</div>
</div>

<div class="modal fade" id="NewDealNoModel" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
        			Deal No
        		</div>
        	</div>
        	<div class="modal-body mdbody">
        		<div class="cdbody">
        			<div>
	        			<table>
        					<%for(int i=0; i<VCONTRACT_DTL.size(); i++){%>
        					<tr>
	        					<td style="display:none;" id="new_column_deal_<%=VCONTRACT_DTL.elementAt(i)%>">
	        						<input type="radio" class="form-check-input" name="new_chk_deal" value="<%=VCONTRACT_DTL.elementAt(i)%>">&nbsp;<%=VNEW_COUNTPTY_ABBR.elementAt(i)%>-<%=V_DEAL_NO.elementAt(i)%> <label style="color:blue;"> (<%=V_DURATION.elementAt(i) %>)</label>&nbsp;&nbsp;&nbsp;&nbsp;
				    				<input type="hidden"  name="new_deal_dtl" value="<%=VCONTRACT_DTL.elementAt(i)%>">
				    				<input type="hidden"  name="new_deal_no" value="<%=V_DEAL_NO.elementAt(i)%>">
				    				<input type="hidden"  name="new_abbr" value="<%=VNEW_COUNTPTY_ABBR.elementAt(i)%>">
	        					</td>
	        				</tr>
        					<%} %>
	        			</table>
        			</div>
        			<div align="center" id="new_msg" style="display: none;"></div>
      			</div>
        	</div>
        	<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Back" data-bs-target="#AddNewSecurity" data-bs-toggle="modal" data-bs-dismiss="modal">
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitNewDealNo()" 
					data-bs-target="#AddNewSecurity" data-bs-toggle="modal" data-bs-dismiss="modal">
				</div>
      		</div>
        </div>
    </div>
</div>
<div class="modal fade" id="AdvDealNoModel" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader" id="adv_deal_hdr">
				</div>
        	</div>
        	<div class="modal-body mdbody">
        		<div class="cdbody">
        			<table>
        			<%for(int i=0; i<VALL_DEAL_DTL.size(); i++){
        				String temp_duration = ""+VDURATION.elementAt(i);
        				String deal_no = ""+VALL_DEAL_NO.elementAt(i);
	        			String deal_dtl = ""+VALL_DEAL_DTL.elementAt(i);
	        			String cp_abbr = ""+VALL_CP_ABBR.elementAt(i); //HP20230914
	        			String[] dealNo = deal_no.split(", ");
	        			String[] dealDtl = deal_dtl.split(", ");
	        			String[] cpAbbr = cp_abbr.split(", ");//HP20230914
	        			String[] duration = temp_duration.split(", ");
	        			
	        			for(int j=0; j<dealDtl.length; j++)
	        			{%>
        				<tr>
        					<td style="display:none;" id="adv_column_deal_<%=dealDtl[j]%>">
        						<input type="radio" class="form-check-input" name="adv_chk_deal" value="<%=dealDtl[j]%>">
        						<%--HP20230914 &nbsp;<%=VCOUNTERPARTY_ABBR.elementAt(j)%>-<%=dealNo[j]%>&nbsp;&nbsp;&nbsp;&nbsp; --%>
        						&nbsp;<%=cpAbbr[j]%>-<%=dealNo[j]%> <label style="color:blue;">(<%=duration[j] %>)</label>&nbsp;&nbsp;&nbsp;&nbsp;
			    				<input type="hidden"  name="adv_deal_dtl" value="<%=dealDtl[j]%>" >
			    				<input type="hidden"  name="adv_deal_no" value="<%=dealNo[j]%>" >
			    				<%--HP20230914 <input type="hidden"  name="adv_abbr" value="<%=VCOUNTERPARTY_ABBR.elementAt(j)%>" > --%>
			    				<input type="hidden"  name="adv_abbr" value="<%=cpAbbr[j]%>" >
        					</td>
        				</tr>
        				<%} %>
        			<%} %>
        			</table>
        			<div align="center" id="adv_msg" style="display: none;"></div>
      			</div>
        	</div>
        	<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Back" data-bs-target="#SecurityAdv" data-bs-toggle="modal" data-bs-dismiss="modal">
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitDealNo()" 
					data-bs-target="#SecurityAdv" data-bs-toggle="modal" data-bs-dismiss="modal">
				</div>
      		</div>
        </div>
    </div>
</div>	

<input type="hidden" name="adv_status_flg">
<input type="hidden" name="adv_cancel_flg">
<input type="hidden" name="prev_clearance" value="<%=clearance%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="adv_ref_no">
<input type="hidden" name="adv_seq_no">
<input type="hidden" name="adv_seq_rev_no">
<input type="hidden" name="option" value="SECURITY_MST">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="adv_old_value" value="">
<input type="hidden" name="sec_type">

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
<script>
function checkDealType()
{
	var sec_type = document.forms[0].sec_type.value;
	var button;
	var deal_type;
	if(sec_type=="ADV")
	{
	deal_type = document.forms[0].adv_deal_type.value;
	button = document.getElementById("AdvdealConfig");
	}
	else
	{
		deal_type="";
	}
	
	if(trim(deal_type) == "" || deal_type==undefined || deal_type==null)
	{
		button.disabled=true;
	}
	else
	{
		button.disabled=false;
	}
}
function setDealNoDtl(DealNo)
{
	var sel_deal_no=DealNo.split("@@");
	var sec_type = document.forms[0].sec_type.value;
	var chk_deal="";
	
	if(sec_type=="ADV")
	{
		chk_deal = document.forms[0].adv_chk_deal;
	}
	for(var j=0;j<sel_deal_no.length;j++)
	{
		if(chk_deal!=null && chk_deal.length!=undefined)
		{
	 		for(var i=0;i<chk_deal.length;i++)
	 		{
	  			if(chk_deal[i].value == sel_deal_no[j])
	  			{
	  				chk_deal[i].checked=true;
	  			} 
	 		} 
		}
		else if(chk_deal!=null)
		{
			if(chk_deal.value == sel_deal_no[j])
			{
				chk_deal.checked=true;
			} 
		}
	}
	
	doSubmitDealNo()
}

function showNewDealNo()
{
	var counterparty_cd = document.forms[0].counterparty.value;
	var clearance = document.forms[0].clearance.value; //HP20230914
	var chk_deal = document.forms[0].new_chk_deal;
	var disp_msg = "<div colspan='22' align='center'><div class='container'><div class='alert alert-info'><i class='fa fa-info-circle fa-lg'></i>&nbsp; Deal No is Not Available </div></div></div>"
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
		var flag = false;
  		for(var i=0;i<chk_deal.length;i++)
  		{
  			var chkDeal=chk_deal[i].value;
  			var split_val=chkDeal.split("-");
  			var count_cd=""
  			var contType=""; //HP20230914
  			if(chkDeal!="")
  			{
  				count_cd=split_val[0];
  				contType=split_val[1]; //HP20230914
  			}
  			
  			if(clearance=="I") //HP20230914 NO NEED CHECK COUNTERPARTY_CD FOR IGX
  			{
  				if(contType!="" && (contType=="X" || contType=="I"))
	  			{ 
	  				flag=true;
	  				document.getElementById("new_column_deal_"+chkDeal).style.display="table-cell";
	  			}
	  			else
	  			{
	  				document.getElementById("new_column_deal_"+chkDeal).style.display="none";
	  				chk_deal[i].checked=false;
	  			}
  			}
  			else
  			{
	  			if(count_cd!="" && count_cd==counterparty_cd)
	  			{ 
	  				flag=true;
	  				document.getElementById("new_column_deal_"+chkDeal).style.display="table-cell";
	  			}
	  			else
	  			{
	  				document.getElementById("new_column_deal_"+chkDeal).style.display="none";
	  				chk_deal[i].checked=false;
	  			}
  			}
  		}
  		if(flag == false)
		{
			document.getElementById("new_msg").style.display="block";
			document.getElementById("new_msg").innerHTML=disp_msg;
		}
  		else
  		{
  			document.getElementById("new_msg").style.display="none";
  		}
 	}
	else
 	{//HP20230914 ELSE PART WAS NOT CORRECTED BY ARTH, NOW CORRECTED
		var flag = false;
		var chkDeal=chk_deal.value;
		
		var split_val=chkDeal.split("-");
		
		var count_cd=""
		var contType=""; //HP20230914
		if(chkDeal!="")
		{
			count_cd=split_val[0];
			contType=split_val[1]; //HP20230914
		}
		
		if(clearance=="I") //HP20230914 NO NEED CHECK COUNTERPARTY_CD FOR IGX
		{
			if(contType!="" && (contType=="X" || contType=="I"))
 			{ 
 				flag=true;
 				document.getElementById("new_column_deal_"+chkDeal).style.display="table-cell";
 			}
 			else
 			{
 				document.getElementById("new_column_deal_"+chkDeal).style.display="none";
 				chk_deal[i].checked=false;
 			}
		}
		else
		{
 			if(count_cd!="" && count_cd==counterparty_cd)
 			{ 
 				flag=true;
 				document.getElementById("new_column_deal_"+chkDeal).style.display="table-cell";
 			}
 			else
 			{
 				document.getElementById("new_column_deal_"+chkDeal).style.display="none";
 				chk_deal[i].checked=false;
 			}
		}
		
		if(flag == false)
		{
			document.getElementById("new_msg").style.display="block";
			document.getElementById("new_msg").innerHTML=disp_msg;
		}
  		else
  		{
  			document.getElementById("new_msg").style.display="none";
  		}
 	}
}

function showDealNo()
{
	var sec_type = document.forms[0].sec_type.value;
	var counterparty_cd="";
	var clearance = document.forms[0].clearance.value; //HP20230914
	var ref_no="";
	var chk_deal="";
	var column = "";
	var disp_msg = "<div colspan='22' align='center'><div class='container'><div class='alert alert-info'><i class='fa fa-info-circle fa-lg'></i>&nbsp; Deal No is Not Available </div></div></div>"
	var msg = "";
	
	if(sec_type=="ADV")
	{
		ref_no = document.forms[0].adv_ref_no.value;
		counterparty_cd = document.forms[0].adv_counterpty_cd.value;
		chk_deal = document.forms[0].adv_chk_deal;
	}
	
	
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
  		for(var i=0;i<chk_deal.length;i++)
  		{
  			var deal_type;
  			var chkDeal=chk_deal[i].value;
  			var split_val=chkDeal.split("/");
  			var count_cd=""
  			var contType=""; //HP20230914
  			if(chkDeal!="")
  			{
  				count_cd=split_val[0];
  				contType=split_val[1]; //HP20230914
  			}
  			
  			if(sec_type=="ADV")
  			{
  				deal_type = document.forms[0].adv_deal_type.value;
  				column = document.getElementById("adv_column_deal_"+chkDeal);
  				msg = document.getElementById("adv_msg")
  			}

  			if(clearance=="I") //HP20230914 NO NEED CHECK COUNTERPARTY_CD FOR IGX
  			{
  				if(contType!="" && ((contType=="X" || contType=="I") && count_cd==ref_no))
  	 			{
  					column.style.display="table-cell";
  	 			}
  				else
	  			{
	  				column.style.display="none";
	  				chk_deal[i].checked=false;
	  			}
  			}
  			else
  			{
  				if(count_cd!="" && (count_cd==counterparty_cd || count_cd==ref_no))
	  			{
	  				if(deal_type=="LTCORA")
	  				{
	  					if((contType=="G" || contType=="P"))
	  					{
	  						column.style.display="table-cell";
	  					}
	  					else
	  					{
	  						column.style.display="none";
	  		 				chk_deal[i].checked=false;
	  					}
	  				}
	  				else if( deal_type=="GAS")
	  				{
	  					if( (contType=="G" || contType=="P"))
	  					{
	  						column.style.display="none";
	  		 				chk_deal[i].checked=false;
	  						
	  					}
	  					else
	  					{
	  						column.style.display="table-cell";
	  					}
	  				}
	  			}
	  			else
	  			{
	  				column.style.display="none";
	  				chk_deal[i].checked=false;
	  			} 
  			}
  		}
 	}
	else if(chk_deal!=null)
 	{
 		var chkDeal=chk_deal.value;
		
		var split_val=chkDeal.split("/");
		
		var count_cd=""
		var contType=""; //HP20230914
		if(chkDeal!="")
		{
			count_cd=split_val[0];
			contType=split_val[1]; //HP20230914
		}
		
		if(sec_type=="ADV")
		{
			column = document.getElementById("adv_column_deal_"+chkDeal);
			msg = document.getElementById("adv_msg");
		}
		
		if(clearance=="I") //HP20230914 NO NEED CHECK COUNTERPARTY_CD FOR IGX
		{
			if(contType!="" && ((contType=="X" || contType=="I") && count_cd==ref_no))
 			{
				column.style.display="table-cell";
 				//msg.style.display="none";
 			}
			else
 			{
 				column.style.display="none";
 				//msg.style.display="block";
 				//msg.innerHTML=disp_msg;
 				chk_deal[i].checked=false;
 			}
		}
		else
		{
			if(count_cd!="" && (count_cd==counterparty_cd || count_cd==ref_no))
  			{
  				if(deal_type=="LTCORA")
  				{
  					if((contType=="G" || contType=="P"))
  					{
  						column.style.display="table-cell";
  					}
  					else
  					{
  						column.style.display="none";
  		 				chk_deal[i].checked=false;
  					}
  				}
  				else if( deal_type=="GAS")
  				{
  					if( (contType=="G" || contType=="P"))
  					{
  						column.style.display="none";
  		 				chk_deal[i].checked=false;
  						
  					}
  					else
  					{
  						column.style.display="table-cell";
  					}
  				}
  			}
 			else
 			{
 				column.style.display="none";
 				chk_deal[i].checked=false;
 			}
		}
 	}
}

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch");
  	
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

function doSubmitNewDealNo()
{
	var chk_deal = document.forms[0].new_chk_deal;
	var new_deal_no = document.forms[0].new_deal_no;
	var new_deal_dtl = document.forms[0].new_deal_dtl;
	var count_abbr = document.forms[0].new_abbr;
	
	var display ="";
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
  		for(var i=0;i<chk_deal.length;i++)
  		{
   			if(chk_deal[i].checked)
   			{
   				if(display!="")
   				{
   					display+=", "+count_abbr[i].value+"-"+new_deal_no[i].value;
   				}
   				else
   				{
   					display+=count_abbr[i].value+"-"+new_deal_no[i].value;
   				}
   			} 
  		} 
 	}
 	else if(chk_deal!=null)
 	{
   		if(chk_deal.checked)
     	{
   			if(display!="")
			{
				display+=", "+count_abbr[i].value+"-"+new_deal_no[i].value;
			}
			else
			{
				display+=count_abbr[i].value+"-"+new_deal_no[i].value;
			}
   		} 
 	}
	
	document.getElementById("NewDealNoDisplay").innerHTML=display;
	if(display != "")
	{
		document.getElementById("NewDealNoDisplay").style.display="inline";
	}
	$("#NewDealNoModel").modal("hide");
}

function doSubmitDealNo()
{
	var deal_type = document.forms[0].adv_deal_type.value;
	var sec_type = document.forms[0].sec_type.value;
	var chk_deal = "";
	var deal_no = "";
	var deal_dtl = "";
	var count_abbr = "";
	
	if(sec_type=="ADV")
	{
		chk_deal = document.forms[0].adv_chk_deal;
		deal_no = document.forms[0].adv_deal_no;
		deal_dtl = document.forms[0].adv_deal_dtl;
		count_abbr = document.forms[0].adv_abbr;
	} 
	
	var display ="";
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
  		for(var i=0;i<chk_deal.length;i++)
  		{
   			if(chk_deal[i].checked)
   			{
   				if(deal_no[i].value!="")
   				{
	   				if(display!="")
	   				{
	   					display+=", "+count_abbr[i].value+"-"+deal_no[i].value;
	   				}
	   				else
	   				{
	   					display+=count_abbr[i].value+"-"+deal_no[i].value;
	   				}
   				}
   				else
				{
   					display="";
				}
   			} 
  		} 
 	}
 	else if(chk_deal!=null)
 	{
   		if(chk_deal.checked)
     	{
   			if(deal_no!="")
   			{
	   			if(display!="")
				{
					display+=", "+count_abbr.value+"-"+deal_no.value;
				}
				else
				{
					display+=count_abbr.value+"-"+deal_no.value;
				}
   			}
   			else
   			{
   				display="";
   			}
   		} 
 	}
	
	if(sec_type=="ADV")
	{
		if(display != "")
		{
			document.getElementById("AdvDealNoDisplay").innerHTML=display;
			document.getElementById("AdvDealNoDisplay").style.display="inline";
			//PrathamBhatt 20240809
			var deal_val = document.getElementById("adv_deal_typ").value;
			display1 = display.split("-");
//			if(display1[1].startsWith("G") || display1[1].startsWith("P"))
			if(display1[2].includes("G") || display1[2].includes("P"))
			{
				document.getElementById("adv_deal_typ").value = "LTCORA";
				document.forms[0].adv_deal_type.value = "LTCORA";
			}
			else
			{
				document.getElementById("adv_deal_typ").value = "GAS";
				document.forms[0].adv_deal_type.value = "GAS";
			}
			document.forms[0].adv_deal_type.style.pointerEvents="none";
		}
		$("#AdvDealNoModel").modal("hide");
	} 
	
}
</script>
</html>