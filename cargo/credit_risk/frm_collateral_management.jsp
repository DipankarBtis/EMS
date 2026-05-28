<%@page import="org.apache.poi.util.SystemOutLogger"%>
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
	
		var url = "frm_collateral_management.jsp?counterparty_cd="+counterparty_cd+"&clearance="+clearance+"&u="+u;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
}

function doModifyLc(VSEC_CATEGORY,VSTATUS,VCURRENCY,VVALUE,VRECEIVED_DATE,VDEAL_NO,VCOUNTERPARTY_CD,VREF_NO,VREMARK,VDEAL_TYPE,VVALUE_VARIATION,VVALUE_FLUCTUATION,VISS_BANK_REF,VADV_BANK_REF,
		VCONFIRM_BANK_REF,VISSUE_DT,VEXPIRE_DT,VTENOR,VREVIEW_DT,VSEC_TYPE,VCOUNTERPARTY_NM,VISS_BANK,VADV_BANK,VCONFIRM_BANK,VDEAL_DTL,VSEQ_NO,VCOUNTERPARTY_ABBR,VSEQ_REV_NO,VEXP_VAL,VISS_BANK_NM,VADV_BANK_NM,VCONFIRM_BANK_NM,VINORDER_HIST)
{
	document.forms[0].status_flg.value = VSTATUS;
	document.forms[0].counterparty_abbr.value=VCOUNTERPARTY_ABBR;
	document.forms[0].value_variation.value=VVALUE_VARIATION;
	document.forms[0].issuing_bankRef.value=VISS_BANK_REF;
	document.forms[0].advising_bankRef.value=VADV_BANK_REF;
	document.forms[0].confirming_bankRef.value=VCONFIRM_BANK_REF;
	document.forms[0].issuance_dt.value=VISSUE_DT;
	document.forms[0].expire_dt.value=VEXPIRE_DT;
	document.forms[0].tenor.value=VTENOR;
	document.forms[0].review_dt.value=VREVIEW_DT;
	document.forms[0].remark.value=VREMARK;
	document.forms[0].sec_category.value=VSEC_CATEGORY;
	document.forms[0].currency.value=VCURRENCY;
	document.forms[0].value.value=VVALUE;
	document.forms[0].received_dt.value=VRECEIVED_DATE;
	if(VDEAL_NO!="")
	{
		document.forms[0].deal_no.value = VDEAL_NO;
	}
	document.forms[0].counterpty_cd.value=VCOUNTERPARTY_CD;
	document.forms[0].ref_no.value=VREF_NO;
	document.forms[0].sec_type.value=VSEC_TYPE;
	document.forms[0].counterparty_name.value=VCOUNTERPARTY_NM;
	document.forms[0].value_fluctuation.value=VVALUE_FLUCTUATION;
	document.forms[0].issuing_bankName.value=VISS_BANK;
	document.forms[0].advising_bankName.value=VADV_BANK;
	document.forms[0].confirming_bankName.value=VCONFIRM_BANK;
	document.forms[0].seq_no.value=VSEQ_NO;
	document.forms[0].seq_rev_no.value=VSEQ_REV_NO;
	document.forms[0].lc_inorder_hist.value=VINORDER_HIST;
	
	if(VSTATUS!="P")
	{
		document.forms[0].status.value = VSTATUS;
	}
	else
	{
		document.forms[0].status.value = "";
	}
	
	if(VSTATUS=="R")
	{
		document.forms[0].status.style.display = "none";
		document.forms[0].status1.style.display = "block";
		document.forms[0].status1.value = "Restated";
	}
	
	if(VSTATUS=="O" || VSTATUS=="R" || VSTATUS=="C" || VEXP_VAL=="Y")
	{
		document.getElementById("reset_btn").style.display="block";
		document.getElementById("dealConfig").disabled=true;
		document.forms[0].sec_category.disabled=true;
		document.forms[0].deal_type.disabled=true;
		document.forms[0].value.disabled=true;
		document.forms[0].currency.disabled=true;
		document.forms[0].value_fluctuation.disabled=true;
		document.forms[0].value_variation.disabled=true;
		document.forms[0].issuing_bankName.disabled=true;
		document.forms[0].advising_bankName.disabled=true;
		document.forms[0].confirming_bankName.disabled=true;
		document.forms[0].issuing_bankRef.disabled=true;
		document.forms[0].advising_bankRef.disabled=true;
		document.forms[0].confirming_bankRef.disabled=true;
		document.forms[0].received_dt.disabled=true;
		document.forms[0].review_dt.disabled=true;
		document.forms[0].issuance_dt.disabled=true;
		document.forms[0].expire_dt.disabled=true;
	}
	else
	{
		document.getElementById("reset_btn").style.display="none";
		document.getElementById("sub_btn").style.display="block";
	}
	
	var cancel_flg = "";
	if(VSTATUS=="O")
	{
		document.forms[0].status.style.display="none";
		document.forms[0].status2.value=VSTATUS;
		cancel_flg = "Y";
	}
	else
	{
		document.forms[0].status.value=VSTATUS;
		document.forms[0].status2.style.display="none";
		cancel_flg="";
	}
	document.forms[0].cancel_flg.value=cancel_flg;
	
	if(VSTATUS=="R" || VSTATUS=="C" || VEXP_VAL=="Y")
	{
		document.forms[0].remark.disabled=true;
		document.forms[0].status.disabled=true;
		document.getElementById("reset_btn").style.display="none";
		document.getElementById("sub_btn").style.display="none";
	}
	document.forms[0].deal_type.value=VDEAL_TYPE;
	
	if(VVALUE_VARIATION == 'Fixed')
	{
		document.getElementById('value_fluctuation').readOnly=true;
	}
	else if(VVALUE_VARIATION == 'Fully Fluctuating')
	{
		document.getElementById('value_fluctuation').readOnly=true;
	}
	else
	{
		document.getElementById('value_fluctuation').readOnly=false;
	}
	
	var security_type = document.forms[0].sec_type.value;
	
	var secure_type="";
	if(security_type=="LC")
	{
		secure_type="Letter Of Credit";
	}
	else if(security_type=="BG")
	{
		secure_type="Bank guarantee";
	}
	else if(security_type=="PCG")
	{
		secure_type="Parent Corporate Guarantee";
	}
	else if(security_type=="OA")
	{
		secure_type="Open Account";
	}
	else if(security_type=="ADV")
	{
		secure_type="Advance Payment";
	}
	var security_ref_no = document.forms[0].ref_no.value;
	var abbr = document.forms[0].counterparty_abbr.value;
	
	document.getElementById("hdr").innerHTML = "Modify "+secure_type+"("+security_ref_no+")"; 
	document.getElementById("deal_hdr").innerHTML = abbr+" Deal/s"; 
	
	document.forms[0].status.style.pointerEvents = "auto";
	
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
	else if(VSTATUS=="D")
	{
		VSTATUS="Dummy";
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
	
	var old_value = "CP="+VCOUNTERPARTY_CD+"#NAME="+VCOUNTERPARTY_NM+"#ABBR="+VCOUNTERPARTY_ABBR+"#SEC_TYPE="+VSEC_TYPE+"#SEC_CATEGORY="+VSEC_CATEGORY+"#DEAL_TYPE="+VDEAL_TYPE+"#DEAL_NO="+VDEAL_NO+"#VALUE="+VVALUE+"#CURRENCY="+VCURRENCY+"#FLUCTUATION="+VVALUE_FLUCTUATION+"#VARIATION="+VVALUE_VARIATION+
			   	    "#ISS_BANK_CD="+VISS_BANK+"#ISS_BANK_NAME="+VISS_BANK_NM+"#ISS_BANK_REF="+VISS_BANK_REF+"#ADV_BANK_CD="+VADV_BANK+"#ADV_BANK_NAME="+VADV_BANK_NM+"#ADV_BANK_REF="+VADV_BANK_REF+"#CONF_BANK_CD="+VCONFIRM_BANK+"#CONF_BANK_NAME="+VCONFIRM_BANK_NM+
					"#CONF_BANK_REF="+VCONFIRM_BANK_REF+"#RECIEVED_DT="+VRECEIVED_DATE+"#REVIEW_DT="+VREVIEW_DT+"#ISSUANCE_DT="+VISSUE_DT+"#EXPIRY_DT="+VEXPIRE_DT+"#STATUS="+VSTATUS+"#TENOR="+VTENOR+"#REMARK="+VREMARK+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+VREF_NO+"#CANCEL_DT="+""+"#GX="+clearance+"#REVERSAL="+""+"#SAP_APPROVAL="+"";
	
	document.forms[0].lc_old_value.value=old_value;
}

function doSubmit()
{
	document.getElementById("dealConfig").disabled=false;
	document.forms[0].sec_category.disabled=false;
	document.forms[0].deal_type.disabled=false;
	document.forms[0].value.disabled=false;
	document.forms[0].currency.disabled=false;
	document.forms[0].value_fluctuation.disabled=false;
	document.forms[0].value_variation.disabled=false;
	document.forms[0].issuing_bankName.disabled=false;
	document.forms[0].advising_bankName.disabled=false;
	document.forms[0].confirming_bankName.disabled=false;
	document.forms[0].issuing_bankRef.disabled=false;
	document.forms[0].advising_bankRef.disabled=false;
	document.forms[0].confirming_bankRef.disabled=false;
	document.forms[0].received_dt.disabled=false;
	document.forms[0].review_dt.disabled=false;
	document.forms[0].issuance_dt.disabled=false;
	document.forms[0].expire_dt.disabled=false;
	document.forms[0].remark.disabled=false;
	document.forms[0].status.disabled=false;
	
	var status_flg = document.forms[0].status_flg.value;
	var ref_no = document.forms[0].ref_no.value;
	var sec_category = document.forms[0].sec_category.value;
	var deal_type = document.forms[0].deal_type.value;
	//var deal_no = document.forms[0].deal_no.value;
	var currency = document.forms[0].currency.value; 
	var variation = document.forms[0].value_variation.value;variation
	var value = document.forms[0].value.value;
	var fluctuation = document.forms[0].value_fluctuation.value;
	var issuing_bank_nm = document.forms[0].issuing_bankName.value;
	var issuing_bank_ref = document.forms[0].issuing_bankRef.value;
	var advising_bank_nm = document.forms[0].advising_bankName.value;
	var advising_bank_ref  = document.forms[0].advising_bankRef.value;
	var confirming_bank_nm = document.forms[0].confirming_bankName.value;
	var confirming_bank_ref = document.forms[0].confirming_bankRef.value;
	var received_dt = document.forms[0].received_dt.value;
	var issuance_dt = document.forms[0].issuance_dt.value;
	var expire_dt = document.forms[0].expire_dt.value;
	var tenor = document.forms[0].tenor.value;
	var status = document.forms[0].status.value;
	var review_dt = document.forms[0].review_dt.value;
	var remark = document.forms[0].remark.value;
	var sec_type = document.forms[0].sec_type.value;
	
	var opration = document.forms[0].opration.value;
	var msg="";
	var flag=true;
	if(status!="C")
	{
		if(trim(deal_type)=="" || deal_type==0)
		{
			msg+="Please Select Deal Type!\n";
			flag=false;
		}
		if(trim(currency)=="" || currency==0)
		{
			msg+="Please Select currency Type!\n";
			flag=false;
		}
		if(trim(variation)=="" || variation==0)
		{
			msg+="Please Select Variation Value!\n";
			flag=false;
		}
		if(trim(value)=="")
		{
			msg+="Please Enter Value!\n";
			flag=false;
		}
		if(trim(fluctuation)=="")
		{
			msg+="Please Enter Fluctuation Value!\n";
			flag=false;
		}
		if(trim(issuing_bank_nm)=="" || issuing_bank_nm==0)
		{
			msg+="Please Select Issuing Bank Name!\n";
			flag=false;
		}
		if(trim(issuing_bank_ref)=="")
		{
			msg+="Please Enter Issuing Bank's Reference!\n";
			flag=false;
		}
		if(trim(received_dt)=="")
		{
			msg+="Please Enter Received Date!\n";
			flag=false;
		}
		if(trim(issuance_dt)=="")
		{
			msg+="Please Enter Issuance Date!\n";
			flag=false;
		}
		if(trim(expire_dt)=="")
		{
			msg+="Please Enter Expire Date!\n";
			flag=false;
		}
		if(trim(tenor)=="")
		{
			msg+="Please Enter Tenor!\n";
			flag=false;
		}
		if(trim(status)=="" || status==0)
		{
			msg+="Please Select Status!\n";
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
			if(status_flg=="O")
			{
				document.getElementById("dealConfig").disabled=true;
				document.forms[0].sec_category.disabled=true;
				document.forms[0].deal_type.disabled=true;
				document.forms[0].value.disabled=true;
				document.forms[0].currency.disabled=true;
				document.forms[0].value_fluctuation.disabled=true;
				document.forms[0].value_variation.disabled=true;
				document.forms[0].issuing_bankName.disabled=true;
				document.forms[0].advising_bankName.disabled=true;
				document.forms[0].confirming_bankName.disabled=true;
				document.forms[0].issuing_bankRef.disabled=true;
				document.forms[0].advising_bankRef.disabled=true;
				document.forms[0].confirming_bankRef.disabled=true;
				document.forms[0].received_dt.disabled=true;
				document.forms[0].review_dt.disabled=true;
				document.forms[0].issuance_dt.disabled=true;
				document.forms[0].expire_dt.disabled=true;
			}
		}
	}
	else
	{
		alert(msg);
	}
}

function doRestate()
{
	var sec_type = document.forms[0].sec_type.value;
	if(sec_type=="LC" || sec_type=="BG")
	{
		document.getElementById("dealConfig").disabled=false;
		document.forms[0].sec_category.disabled=false;
		document.forms[0].deal_type.disabled=false;
		document.forms[0].value.disabled=false;
		document.forms[0].currency.disabled=false;
		document.forms[0].value_fluctuation.disabled=false;
		document.forms[0].value_variation.disabled=false;
		document.forms[0].issuing_bankName.disabled=false;
		document.forms[0].advising_bankName.disabled=false;
		document.forms[0].confirming_bankName.disabled=false;
		document.forms[0].issuing_bankRef.disabled=false;
		document.forms[0].advising_bankRef.disabled=false;
		document.forms[0].confirming_bankRef.disabled=false;
		document.forms[0].received_dt.disabled=false;
		document.forms[0].review_dt.disabled=false;
		document.forms[0].issuance_dt.disabled=false;
		document.forms[0].expire_dt.disabled=false;
		document.forms[0].remark.disabled=false;
		document.forms[0].status.disabled=false;
	}
	else if(sec_type="PCG")
	{
		document.getElementById("PcgdealConfig").disabled=false;
		document.forms[0].pcg_sec_category.disabled=false;
		document.forms[0].pcg_deal_type.disabled=false;
		document.forms[0].pcg_currency.disabled=false;
		document.forms[0].pcg_value.disabled=false;
		document.forms[0].pcg_guarantor_name.disabled=false;
		document.forms[0].pcg_received_dt.disabled=false;
		document.forms[0].pcg_review_dt.disabled=false;
		document.forms[0].pcg_issuance_dt.disabled=false;
		document.forms[0].pcg_expire_dt.disabled=false;
		document.forms[0].pcg_status.disabled=false;
		document.forms[0].pcg_remark.disabled=false;
	}
	/* else if(sec_type="ADV")
	{
		document.getElementById("AdvdealConfig").disabled=false;
		document.forms[0].adv_sec_category.disabled=false;
		document.forms[0].adv_deal_type.disabled=false;
		document.forms[0].adv_currency.disabled=false;
		document.forms[0].adv_value.disabled=false;
		document.forms[0].adv_received_dt.disabled=false;
		document.forms[0].adv_remark.disabled=false;
		document.forms[0].adv_status.disabled=false;
	} */
	var a =  confirm("Do you want to Restate the Security Details?");
	if(a)
	{
		document.forms[0].opration.value="RESTATE";
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].submit();
	}
	else
	{
		if(sec_type=="LC" || sec_type=="BG")
		{
			document.getElementById("dealConfig").disabled=true;
			document.forms[0].sec_category.disabled=true;
			document.forms[0].deal_type.disabled=true;
			document.forms[0].value.disabled=true;
			document.forms[0].currency.disabled=true;
			document.forms[0].value_fluctuation.disabled=true;
			document.forms[0].value_variation.disabled=true;
			document.forms[0].issuing_bankName.disabled=true;
			document.forms[0].advising_bankName.disabled=true;
			document.forms[0].confirming_bankName.disabled=true;
			document.forms[0].issuing_bankRef.disabled=true;
			document.forms[0].advising_bankRef.disabled=true;
			document.forms[0].confirming_bankRef.disabled=true;
			document.forms[0].received_dt.disabled=true;
			document.forms[0].review_dt.disabled=true;
			document.forms[0].issuance_dt.disabled=true;
			document.forms[0].expire_dt.disabled=true;
			
		}
		else if(sec_type="PCG")
		{
			document.getElementById("PcgdealConfig").disabled=true;
			document.forms[0].pcg_sec_category.disabled=true;
			document.forms[0].pcg_deal_type.disabled=true;
			document.forms[0].pcg_currency.disabled=true;
			document.forms[0].pcg_value.disabled=true;
			document.forms[0].pcg_guarantor_name.disabled=true;
			document.forms[0].pcg_received_dt.disabled=true;
			document.forms[0].pcg_review_dt.disabled=true;
			document.forms[0].pcg_issuance_dt.disabled=true;
			document.forms[0].pcg_expire_dt.disabled=true;
			
		}
	}
}

function doModifyPcg(VSEC_CATEGORY ,VCURRENCY ,VVALUE ,VRECEIVED_DATE ,VDEAL_NO ,VCOUNTERPARTY_CD ,VSEC_REF_NO,VREMARK,VDEAL_TYPE,VCOUNTERPARTY_NAME,VSEQ_NO,
		VISSUE_DT,VEXPIRE_DT,VTENOR,VREVIEW_DT,VRENEW_DT,VDEAL_DTL,VSEC_TYPE,VGUARANTOR_NM,VGUARANTOR_CD,VSTATUS,VCOUNTERPARTY_ABBR,VSEQ_REV_NO,VEXP_VAL)
{
	document.forms[0].pcg_status_flg.value = VSTATUS;
	document.forms[0].pcg_counterparty_abbr.value = VCOUNTERPARTY_ABBR;
	document.forms[0].pcg_counterparty_name.value = VCOUNTERPARTY_NAME;
	document.forms[0].pcg_sec_category.value = VSEC_CATEGORY;
	if(VDEAL_NO!="")
	{
		document.forms[0].pcg_deal_no.value = VDEAL_NO;
	}
	document.forms[0].pcg_currency.value = VCURRENCY;
	document.forms[0].pcg_value.value = VVALUE;
	document.forms[0].pcg_received_dt.value = VRECEIVED_DATE;
	//document.forms[0].pcg_renew_dt.value = VRENEW_DT;
	document.forms[0].pcg_issuance_dt.value = VISSUE_DT;
	document.forms[0].pcg_expire_dt.value = VEXPIRE_DT;
	document.forms[0].pcg_tenor.value = VTENOR;
	document.forms[0].pcg_review_dt.value = VREVIEW_DT;
	document.forms[0].pcg_remark.value = VREMARK;
	document.forms[0].pcg_counterpty_cd.value = VCOUNTERPARTY_CD;
	document.forms[0].pcg_seq_no.value = VSEQ_NO;
	document.forms[0].pcg_ref_no.value = VSEC_REF_NO;
	document.forms[0].sec_type.value = VSEC_TYPE;
	document.forms[0].pcg_remark.value = VREMARK;
	document.forms[0].pcg_guarantor_name.value = VGUARANTOR_CD;
	document.forms[0].pcg_seq_rev_no.value=VSEQ_REV_NO;
	
	if(VSTATUS!="P")
	{
		document.forms[0].pcg_status.value = VSTATUS;
	}
	else
	{
		document.forms[0].pcg_status.value = "";
	}
	
	if(VSTATUS=="R")
	{
		document.forms[0].pcg_status.style.display = "none";
		document.forms[0].pcg_status1.style.display = "block";
		document.forms[0].pcg_status1.value = "Restated";
	}
	
	if(VSTATUS=="O" || VSTATUS=="R" || VSTATUS=="C" || VEXP_VAL=="Y")
	{
		document.getElementById("pcg_reset_btn").style.display="block";
		document.getElementById("PcgdealConfig").disabled=true;
		document.forms[0].pcg_sec_category.disabled=true;
		document.forms[0].pcg_deal_type.disabled=true;
		document.forms[0].pcg_currency.disabled=true;
		document.forms[0].pcg_value.disabled=true;
		document.forms[0].pcg_guarantor_name.disabled=true;
		document.forms[0].pcg_received_dt.disabled=true;
		document.forms[0].pcg_review_dt.disabled=true;
		document.forms[0].pcg_issuance_dt.disabled=true;
		document.forms[0].pcg_expire_dt.disabled=true;
		
	}
	else
	{
		document.getElementById("pcg_reset_btn").style.display="none";
		document.getElementById("pcg_sub_btn").style.display="block";
		document.getElementById("PcgdealConfig").disabled=false;
	}
	
	var pcg_cancel_flg = "";
	if(VSTATUS=="O")
	{
		document.forms[0].pcg_status.style.display="none";
		document.forms[0].pcg_status2.value=VSTATUS;
		pcg_cancel_flg = "Y";
	}
	else
	{
		document.forms[0].pcg_status.value=VSTATUS;
		document.forms[0].pcg_status2.style.display="none";
		pcg_cancel_flg="";
	}
	document.forms[0].pcg_cancel_flg.value=pcg_cancel_flg;
	
	if(VSTATUS=="R" || VSTATUS=="C" || VEXP_VAL=="Y")
	{
		document.forms[0].pcg_status.disabled=true;
		document.forms[0].pcg_remark.disabled=true;
		document.getElementById("pcg_reset_btn").style.display="none";
		document.getElementById("pcg_sub_btn").style.display="none";
	}
	document.forms[0].pcg_deal_type.value=VDEAL_TYPE;
	var security_type = document.forms[0].sec_type.value;
	var secure_type="";
	if(security_type=="LC")
	{
		secure_type="Letter Of Credit";
	}
	else if(security_type=="BG")
	{
		secure_type="Bank guarantee";
	}
	else if(security_type=="PCG")
	{
		secure_type="Parent Corporate Guarantee";
	}
	else if(security_type=="OA")
	{
		secure_type="Open Account";
	}
	else if(security_type=="ADV")
	{
		secure_type="Advance Payment";
	}
	var security_ref_no = document.forms[0].pcg_ref_no.value;
	var abbr = document.forms[0].pcg_counterparty_abbr.value;
	document.getElementById("pcg_hdr").innerHTML = "Modify "+secure_type+"("+security_ref_no+")"; 
	document.getElementById("pcg_deal_hdr").innerHTML = abbr+" Deal/s"; 
	
	document.forms[0].status.style.pointerEvents = "auto";
	
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
	    		    "#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+"#CONF_BANK_REF="+""+"#RECIEVED_DT="+VRECEIVED_DATE+
					"#REVIEW_DT="+VREVIEW_DT+"#ISSUANCE_DT="+VISSUE_DT+"#EXPIRY_DT="+VEXPIRE_DT+"#STATUS="+VSTATUS+"#TENOR="+VTENOR+"#REMARK="+VREMARK+"#GUARANTOR_CD="+VGUARANTOR_CD+"#GUARANTOR_NM="+VGUARANTOR_NM+"#SEC_REF_NO="+VSEC_REF_NO+"#CANCEL_DT="+""+"#GX="+clearance+"#REVERSAL="+""+"#SAP_APPROVAL="+"";


	document.forms[0].pcg_old_value.value=old_value;
}


function doSubmitPcg()
{
	document.getElementById("PcgdealConfig").disabled=false;
	document.forms[0].pcg_sec_category.disabled=false;
	document.forms[0].pcg_deal_type.disabled=false;
	document.forms[0].pcg_currency.disabled=false;
	document.forms[0].pcg_value.disabled=false;
	document.forms[0].pcg_guarantor_name.disabled=false;
	document.forms[0].pcg_received_dt.disabled=false;
	document.forms[0].pcg_review_dt.disabled=false;
	document.forms[0].pcg_issuance_dt.disabled=false;
	document.forms[0].pcg_expire_dt.disabled=false;
	document.forms[0].pcg_status.disabled=false;
	document.forms[0].pcg_remark.disabled=false;
	
	var pcg_status_flg = document.forms[0].pcg_status_flg.value;
	var pcg_sec_category = document.forms[0].pcg_sec_category.value;
	var pcg_deal_type = document.forms[0].pcg_deal_type.value;
	var pcg_currency = document.forms[0].pcg_currency.value; 
	var pcg_value = document.forms[0].pcg_value.value;
	var pcg_received_dt = document.forms[0].pcg_received_dt.value;
	//var pcg_renew_dt = document.forms[0].pcg_renew_dt.value;
	var pcg_issuance_dt = document.forms[0].pcg_issuance_dt.value;
	var pcg_expire_dt = document.forms[0].pcg_expire_dt.value;
	var pcg_guarantor_name = document.forms[0].pcg_guarantor_name.value;
	var pcg_status = document.forms[0].pcg_status.value;
	
	var opration = document.forms[0].opration.value;
	var msg="";
	var flag=true;
	
	if(pcg_status!="C")
	{
		if(trim(pcg_deal_type)=="" || pcg_deal_type==0)
		{
			msg+="Please Select Deal Type!\n";
			flag=false;
		}
		if(trim(pcg_currency)=="" || pcg_currency==0)
		{
			msg+="Please Select currency Type!\n";
			flag=false;
		}
		if(trim(pcg_value)=="")
		{
			msg+="Please Enter Value!\n";
			flag=false;
		}
		if(trim(pcg_guarantor_name)=="" || pcg_guarantor_name==0)
		{
			msg+="Please Select Guarantor Name!\n";
			flag=false;
		}
		if(trim(pcg_received_dt)=="")
		{
			msg+="Please Enter Received Date!\n";
			flag=false;
		}
		if(trim(pcg_issuance_dt)=="")
		{
			msg+="Please Enter Issuance Date!\n";
			flag=false;
		}
		if(trim(pcg_expire_dt)=="")
		{
			msg+="Please Enter Expire Date!\n";
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
			if(pcg_status_flg=="O")
			{
				document.getElementById("PcgdealConfig").disabled=true;
				document.forms[0].pcg_sec_category.disabled=true;
				document.forms[0].pcg_deal_type.disabled=true;
				document.forms[0].pcg_currency.disabled=true;
				document.forms[0].pcg_value.disabled=true;
				document.forms[0].pcg_guarantor_name.disabled=true;
				document.forms[0].pcg_received_dt.disabled=true;
				document.forms[0].pcg_review_dt.disabled=true;
				document.forms[0].pcg_issuance_dt.disabled=true;
				document.forms[0].pcg_expire_dt.disabled=true;
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
	//var new_deal_no = document.forms[0].new_deal_no.value;
	
	
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

function valueFluctuation()
{
	var value_variation = document.forms[0].value_variation.value;
	if(value_variation == 'Fixed')
	{
		document.forms[0].value_fluctuation.value='0';
		document.getElementById('value_fluctuation').readOnly=true;
	}
	else if(value_variation == 'Fully Fluctuating')
	{
		document.forms[0].value_fluctuation.value='10';
		document.getElementById('value_fluctuation').readOnly=true;
	}
	else
	{
		document.forms[0].value_fluctuation.value='';
		document.getElementById('value_fluctuation').readOnly=false;
	}
}

function countTenorLc()
{
 	var d1 = document.forms[0].issuance_dt.value;
 	var d2 = document.forms[0].expire_dt.value;
 	if(d1 != "" && d2 != "")
 	{
 		var split = d1.split("/");
		var split1 = d2.split("/");
		var date1 = new Date(split[2]+"-"+split[1]+"-"+split[0]);
		var date2 = new Date(split1[2]+"-"+split1[1]+"-"+split1[0]);
 		if(date1 > date2)
 		{
 			alert("Please select Issuance Date Greater than Expire  Date!!");
 			flag=false;
 		}
 		else
 	 	{
 			var res = Math.abs(date1 - date2) / 1000;
 	    	var days = Math.floor(res / 86400)+1;
 	    	document.forms[0].tenor.value=days;
 	 	}
		
 	}
} 
function countTenorPcg()
{
 	var d1 = document.forms[0].pcg_issuance_dt.value;
 	var d2 = document.forms[0].pcg_expire_dt.value;
 	if(d1 != "" && d2 != "")
 	{
 		var split = d1.split("/");
		var split1 = d2.split("/");
		var date1 = new Date(split[2]+"-"+split[1]+"-"+split[0]);
		var date2 = new Date(split1[2]+"-"+split1[1]+"-"+split1[0]);
 		if(date1 > date2)
 		{
 			alert("Please select Issuance Date Greater than Expire  Date!!");
 			flag=false;
 		}
 		else
 	 	{
 			var res = Math.abs(date1 - date2) / 1000;
 	    	var days = Math.floor(res / 86400)+1;
 	    	document.forms[0].pcg_tenor.value=days;
 	 	}
		
 	}
} 

function checkNumber1(split_val,a,b)
{ 
	var sec_type = document.forms[0].sec_type.value;
	/* var split_val="";
	if(sec_type=="LC" || sec_type=="BG")
	{
		split_val=document.forms[0].split_value;
	}
	else if(sec_type=="PCG")
	{
		split_val=document.forms[0].pcg_split_value;
	} */
	
	var c = parseInt(a)-parseInt(b);
	var flag=true;
	
	var fieldValue=split_val.value;
    
    var len = 0;
    
    var str = fieldValue.substring(0,fieldValue.indexOf('.')).length;
	
	if(str == 0)
	{
		len = fieldValue.length;
	}
	else
	{
		len = str;
	}
    
    if(split_val.value!="" && split_val.value!=null && split_val.value!=' ')
    {
		if((parseInt(len) > parseInt(c)))
		{
    		alert("Please, Enter In the Required Format.."+'('+c+' ,'+b+' )');
    		split_val.value= "";
    		split_val.select();
			flag = false;
		}
		else
		{
			var decallowed = b;  // how many decimals are allowed?
        
        	if(isNaN(fieldValue) || fieldValue == "")
        	{
        		alert("Please, Enter In the Required  Format.."+'('+c+' ,'+b+' )');
        		split_val.value="";
        		split_val.select();	 
				flag=false;
        	}
      		else
      		{
         		if(fieldValue.indexOf('.') == -1) 
		    	{
		    		fieldValue += ".";
         		}
         	
         		dectext = fieldValue.substring(fieldValue.indexOf('.')+1, fieldValue.length);
         	
         		if(parseInt(dectext.length) > parseInt(decallowed))
            	{
		    		alert("Please, Enter In the Required  Format.."+'('+c+' ,'+b+') !!!');
		    		split_val.value="";
		    		split_val.select();		
			 		flag=false;
            	}
         		else
         		{
              		flag=true;
            	}
        	}
		}
   	}
    
    return flag;
}

function isNumeric_value_lc()
{
	var value = document.forms[0].value.value;
	var number = /^-?\d*\.?\d+$/;
	let rateInput = document.getElementById('value').value.trim();
    let unit = document.getElementById('currency').value;
	
    if(trim(value)=="" || value==0)
    {
    }
    else
    {
		if(value.match(number))
		{
		    let formatRegex;
		    let alertMessage;
		    
		    if(rateInput == "")
		    {
		    	
		    }
		    else
		    {
		    	if (unit === '1')
		        {
	                formatRegex = /^\d{1,12}(\.\d{1,2})?$/;
	                alertMessage = ("Please, Enter In the Required Format..(12 ,2)");
		        } 
		        else if (unit === '2')
		        {
	                formatRegex = /^\d{1,10}(\.\d{1,4})?$/;
	                alertMessage = ("Please, Enter In the Required Format..(10 ,4)");
		        } 
		        else
		        {
		        	return;
		        }
	
		        if (!formatRegex.test(rateInput))
		        {
		            alert(alertMessage);
		            document.getElementById('value').value = "";
		        }
		    }
		}
		else
		{
			alert("Enter Only Numeric Value !!");
			document.forms[0].value.value="";
			return false;
		}
    }
}

function isNumeric_value_pcg()
{
	var value = document.forms[0].pcg_value.value;
	var number = /^-?\d*\.?\d+$/;
	let rateInput = document.getElementById('pcg_value').value.trim();
    let unit = document.getElementById('pcg_currency').value;
	
    if(trim(value)=="" || value==0)
    {
    }
    else
    {
		if(value.match(number))
		{
		    let formatRegex;
		    let alertMessage;
		    
		    if(rateInput == "")
		    {
		    	
		    }
		    else
		    {
		    	if (unit === '1')
		        {
	                formatRegex = /^\d{1,12}(\.\d{1,2})?$/;
	                alertMessage = ("Please, Enter In the Required Format..(12 ,2)");
		        } 
		        else if (unit === '2')
		        {
	                formatRegex = /^\d{1,10}(\.\d{1,4})?$/;
	                alertMessage = ("Please, Enter In the Required Format..(10 ,4)");
		        } 
		        else
		        {
		        	return;
		        }
	
		        if (!formatRegex.test(rateInput))
		        {
		            alert(alertMessage);
		            document.getElementById('pcg_value').value = "";
		        }
		    }
		}
		else
		{
			alert("Enter Only Numeric Value !!");
			document.forms[0].pcg_value.value="";
			return false;
		}
    }
}

function isNumeric_value_new()
{
	var value = document.forms[0].new_value.value;
	var number = /^-?\d*\.?\d+$/;
	let rateInput = document.getElementById('new_value').value.trim();
    let unit = document.getElementById('new_currency').value;
	
    if(trim(value)=="" || value==0)
    {
    }
    else
    {
		if(value.match(number))
		{
		    let formatRegex;
		    let alertMessage;
		    
		    if(rateInput == "")
		    {
		    	
		    }
		    else
		    {
		    	if (unit === '1')
		        {
	                formatRegex = /^\d{1,12}(\.\d{1,2})?$/;
	                alertMessage = ("Please, Enter In the Required Format..(12 ,2)");
		        } 
		        else if (unit === '2')
		        {
	                formatRegex = /^\d{1,10}(\.\d{1,4})?$/;
	                alertMessage = ("Please, Enter In the Required Format..(10 ,4)");
		        } 
		        else
		        {
		        	return;
		        }
	
		        if (!formatRegex.test(rateInput))
		        {
		            alert(alertMessage);
		            document.getElementById('new_value').value = "";
		        }
		    }
		}
		else
		{
			alert("Enter Only Numeric Value !!");
			document.forms[0].new_value.value="";
			return false;
		}
    }
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

credit_risk.setCallFlag("SECURITY_COLLATERAL_MANAGEMENT");
credit_risk.setCounterparty_cd(counterparty_cd);
credit_risk.setClearance(clearance);
credit_risk.setComp_cd(owner_cd);
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
Vector VDURATION = credit_risk.getVDURATION();
Vector VSHARE_PERCENT = credit_risk.getVSHARE_PERCENT();

Vector VPO_GUARANTOR_CD = credit_risk.getVPO_GUARANTOR_CD();
Vector VPO_GUARANTOR_NAME = credit_risk.getVPO_GUARANTOR_NAME();
Vector VINORDER_HIST = credit_risk.getVINORDER_HIST();

%>
<body onload="">
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
				    		Collateral Management 
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-12">
							<div align="center">
								<div class="btn-group" align="center">
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
									<label class="btn btn-outline-secondary subbtngrp1" data-bs-toggle="modal" data-bs-target="#AddNewSecurity">Add New Security</label>
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
											<th>Security Ref.No<div align="center"><input class="form-control form-control-sm" type="text" id="security_reference_no" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Status<div align="center"><input class="form-control form-control-sm" type="text" id="stats" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Deal Type<div align="center"><input class="form-control form-control-sm" type="text" id="dealType" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Deal No.<div align="center"><input class="form-control form-control-sm" type="text" id="dealNo" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Currency</th>
											<th>value</th>
											<th>Value Variation</th>
											<th>Value Fluctuation (%)</th>
											<th>Received / Authenticated Date</th>
											<th>Issuance Date</th>
											<th>Expire Date</th>
											<th>Cancellation / Restate Date</th>
											<th>Renew Date</th>
											<th>Tenor (Day)</th>
											<th>Issuing Bank name</th>
											<th>Issuing Bank's Reference</th>
											<th>Advising Bank Name</th>
											<th>Advising Bank's Reference</th>
											<th>Confirming Bank Name</th>
											<th>Confirming Bank's Reference</th>
											<th>Guarantor Name</th>
											<th>Remarks</th>
											<th>Review date</th>
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
											<%if(write_access.equals("Y") && !security_type.equals("OA") && !security_type.equals("ADV")){ %>
												<font title="Click to Edit" <%if(VEXP_VAL.elementAt(i).equals("Y") || VSTATUS.elementAt(i).equals("R") || VSTATUS.elementAt(i).equals("C")){%><%}else{ %>style="color:var(--header_color)"<%} %>>
													<%if(security_type.equals("LC") || security_type.equals("BG")){ %>
													<i <%if(VEXP_VAL.elementAt(i).equals("Y")|| VSTATUS.elementAt(i).equals("R") || VSTATUS.elementAt(i).equals("C")){%>class="fa fa-eye fa-lg" <%}else{ %>	class="fa fa-edit fa-lg"<%} %>
														data-bs-toggle="modal" 
														data-bs-target="#SecurityLc" onclick=" doModifyLc('<%=VSEC_CATEGORY.elementAt(i)%>',
														'<%=VSTATUS.elementAt(i)%>','<%=VCURRENCY.elementAt(i)%>',
														'<%=VVALUE.elementAt(i)%>','<%=VRECEIVED_DATE.elementAt(i)%>','<%=VDEAL_NO.elementAt(i)%>',
														'<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VSEC_REF_NO.elementAt(i) %>','<%=VREMARK.elementAt(i)%>',
														'<%=VDEAL_TYPE.elementAt(i)%>','<%=VVALUE_VARIATION.elementAt(i)%>','<%=VVALUE_FLUCTUATION.elementAt(i)%>',
														'<%=VISS_BANK_REF.elementAt(i)%>','<%=VADV_BANK_REF.elementAt(i)%>','<%=VCONFIRM_BANK_REF.elementAt(i)%>',
														'<%=VISSUE_DT.elementAt(i)%>','<%=VEXPIRE_DT.elementAt(i)%>','<%=VTENOR.elementAt(i)%>','<%=VREVIEW_DT.elementAt(i)%>',
														'<%=VSEC_TYPE.elementAt(i) %>','<%=VCOUNTERPARTY_NAME.elementAt(i) %>','<%=VISS_BANK_CD.elementAt(i)%>',
														'<%=VADV_BANK_CD.elementAt(i)%>','<%=VCONFIRM_BANK_CD.elementAt(i)%>','<%=VDEAL_DTL.elementAt(i) %>','<%=VSEQ_NO.elementAt(i) %>',
														'<%=VCOUNTERPARTY_ABBR.elementAt(i) %>','<%=VSEQ_REV_NO.elementAt(i) %>','<%=VEXP_VAL.elementAt(i)%>','<%=VISS_BANK_NM.elementAt(i) %>',
														'<%=VADV_BANK_NM.elementAt(i) %>','<%=VCONFIRM_BANK_NM.elementAt(i) %>','<%=VINORDER_HIST.elementAt(i) %>'); 
														setDealNoDtl('<%=VSEL_DEAL_DTL.elementAt(i) %>','<%=VSHARE_PERCENT.elementAt(i) %>'); showDealNo();checkDealType();">
													</i>
													<%}else if(security_type.equals("PCG")){  %>
													<i <%if(VEXP_VAL.elementAt(i).equals("Y") || VSTATUS.elementAt(i).equals("R") || VSTATUS.elementAt(i).equals("C")){%>class="fa fa-eye fa-lg" <%}else{ %>class="fa fa-edit fa-lg"<%} %>
														 data-bs-toggle="modal" 
														data-bs-target="#SecurityPcg" onclick="doModifyPcg('<%=VSEC_CATEGORY.elementAt(i)%>','<%=VCURRENCY.elementAt(i)%>','<%=VVALUE.elementAt(i)%>','<%=VRECEIVED_DATE.elementAt(i)%>',
														'<%=VDEAL_NO.elementAt(i)%>','<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VSEC_REF_NO.elementAt(i) %>','<%=VREMARK.elementAt(i)%>','<%=VDEAL_TYPE.elementAt(i)%>',
														'<%=VCOUNTERPARTY_NAME.elementAt(i) %>','<%=VSEQ_NO.elementAt(i) %>','<%=VISSUE_DT.elementAt(i)%>','<%=VEXPIRE_DT.elementAt(i)%>','<%=VTENOR.elementAt(i)%>','<%=VREVIEW_DT.elementAt(i)%>',
														'<%=VRENEW_DT.elementAt(i)%>','<%=VDEAL_DTL.elementAt(i) %>','<%=VSEC_TYPE.elementAt(i) %>','<%=VGUARANTOR_NM.elementAt(i)%>','<%=VGUARANTOR_CD.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>',
														'<%=VCOUNTERPARTY_ABBR.elementAt(i) %>','<%=VSEQ_REV_NO.elementAt(i) %>','<%=VEXP_VAL.elementAt(i)%>'); 
														setDealNoDtl('<%=VSEL_DEAL_DTL.elementAt(i) %>','<%=VSHARE_PERCENT.elementAt(i) %>'); showDealNo();checkDealType();">
													</i>
													<%}%>
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
												<%}else if(VSTATUS.elementAt(i).equals("D")){  %>
													<span class="alert alert-info">Dummy</span>
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
											<td align="center"><%=VVALUE_VARIATION.elementAt(i) %></td>
											<td align="center"><%=VVALUE_FLUCTUATION.elementAt(i) %></td>
											<td align="center"><%=VRECEIVED_DATE.elementAt(i) %></td>
											<td align="center"><%=VISSUE_DT.elementAt(i) %></td>
											<td align="center"><%=VEXPIRE_DT.elementAt(i) %></td>
											<td align="center"><%=VCANCEL_DT.elementAt(i) %></td>
											<td align="center"><%=VRENEW_DT.elementAt(i) %></td>
											<td align="center"><%=VTENOR.elementAt(i) %></td>
											<td align="center"><%=VISS_BANK_NM.elementAt(i) %></td>
											<td align="center"><%=VISS_BANK_REF.elementAt(i) %></td>
											<td align="center"><%=VADV_BANK_NM.elementAt(i) %></td>
											<td align="center"><%=VADV_BANK_REF.elementAt(i) %></td>
											<td align="center"><%=VCONFIRM_BANK_NM.elementAt(i) %></td>
											<td align="center"><%=VCONFIRM_BANK_REF.elementAt(i) %></td>
											<td align="center"><%=VGUARANTOR_NM.elementAt(i) %></td>
											<td align="center"><%=VREMARK.elementAt(i) %></td>
											<td align="center"><%=VREVIEW_DT.elementAt(i) %></td>
										</tr>
										<%} %>
										<%}else{ %>
											<tr><td colspan="27" align="center"><%=utilmsg.infoMessage("<b>Security Is Not Available!</b>") %></td></tr>
										<%} %>
									</tbody>
								</table>
							</div>
							<%}else{ %>
								<div colspan="27" align="center"><%=utilmsg.infoMessage("<b>Please Select any Counterparty!</b>") %></div>
							<%} %>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="SecurityLc" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader" id="hdr">
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
									<input type="text" class="form-control form-control-sm" name="counterparty_name" readonly>
									<input type="hidden" class="form-control form-control-sm" name="counterpty_cd" readonly>
									<input type="hidden" class="form-control form-control-sm" name="counterparty_abbr" readonly>
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
									<select class="form-select form-select-sm" name="sec_category">
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
				      				<select class="form-select form-select-sm" name="deal_type"  id="deal_type_bg_lc" onchange="checkDealType()">
				      					<option value="" selected="selected">--Select--</option>
				      					<option value="GAS">GAS</option>
				      					<option value="LTCORA" >LTCORA</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="button" class="btn btn-sm config_btn" id="dealConfig" value="Attach Contract#" 
					    			<%if(clearance.equals("K")){ %>data-bs-toggle="modal" data-bs-target="#DealNoModel" onclick="showDealNo()"<%} %>>
					    			<!--HP20230914 NOT REQUIRED DEAL CONFIG FOR IGX  -->
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label" id="DealNoDisplay" style="display:none;"></label>
				      			</div>
				  			</div>
						</div>
					</div>
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Security value<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="value" id="value" onchange="isNumeric_value_lc()"> 
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="currency" id="currency" onchange="isNumeric_value_lc()">
				      					<option value="1">INR</option>
				      					<option value="2">USD</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Value Fluctuation %<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="value_variation" onchange="valueFluctuation();">
				      					<option value="">--Select--</option>
				      					<option value="Fixed">Fixed</option>
				      					<option value="Fixed + x% Fluctuating">Fixed + x% Fluctuating</option>
				      					<option value="Fully Fluctuating">Fully Fluctuating</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" id="value_fluctuation" name="value_fluctuation" >
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Issuing Bank<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="issuing_bankName">
				      					<option value="">--Select--</option>
				      					<%for(int i=0; i<VMST_BANK_CD.size(); i++){ %>
				      						<option value="<%=VMST_BANK_CD.elementAt(i)%>"><%=VMST_BANK_NM.elementAt(i)%> (<%=VMST_BRANCH_NAME.elementAt(i)%>)</option>
				      					<%} %>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Issuing Bank's Reference<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="issuing_bankRef">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Advising Bank</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="advising_bankName">
				      					<option value="">--Select--</option>
				      					<%for(int i=0; i<VMST_BANK_CD.size(); i++){ %>
				      						<option value="<%=VMST_BANK_CD.elementAt(i)%>"><%=VMST_BANK_NM.elementAt(i)%> (<%=VMST_BRANCH_NAME.elementAt(i)%>)</option>
				      					<%} %>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Advising Bank's Reference</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="advising_bankRef">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Confirming Bank</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="confirming_bankName">
				      					<option value="">--Select--</option>
				      					<%for(int i=0; i<VMST_BANK_CD.size(); i++){ %>
				      						<option value="<%=VMST_BANK_CD.elementAt(i)%>"><%=VMST_BANK_NM.elementAt(i)%> (<%=VMST_BRANCH_NAME.elementAt(i)%>)</option>
				      					<%} %>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Confirming Bank's Reference</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="confirming_bankRef">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Recv./Auth. Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="received_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Review Date</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="review_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Issuance Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="issuance_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);countTenorLc();" onchange="validateDate(this);countTenorLc();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Expire Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="expire_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);countTenorLc();" onchange="validateDate(this);countTenorLc();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
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
				      				<select class="form-select form-select-sm" name="status">
				      					<option value="">--Select--</option>
				      					<option value="O">In order</option>
				      					<option value="A">Pending for Amendment</option>
				      					<option value="C">Cancelled</option>
				      					<option value="D">Dummy</option>
				      				</select>
				      				<select class="form-select form-select-sm" name="status2" >
				      					<option value="O">In order</option>
				      					<option value="C">Cancelled</option>
				      				</select>
				      				<input type="text" class="form-control form-control-sm" name="status1" readonly style="display: none;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
			    			<label class="form-label" style="background:#FFF4A3;"><b> Tenor</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="tenor" readOnly style="background:#FFF4A3;">
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
				    				<input type="text" class="form-control form-control-sm" name="remark">
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
						<input type="button" class="btn btn-warning com-btn" id="reset_btn" value="Restate" onclick="doRestate();" style="display: none;">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" id="reset_btn" value="Restate" style="display: none;">
						<%} %>
					</div>
	        		<div align="right" >
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" id="sub_btn" value="Submit" onclick="doSubmit();" style="display: button;">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" id="sub_btn" value="Submit" disabled style="display: button;">
						<%} %>
					</div>
				</div>
      		</div>
      	</div>
	</div>
</div>

<div class="modal fade" id="SecurityPcg" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader" id="pcg_hdr">
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
									<input type="text" class="form-control form-control-sm" name="pcg_counterparty_name" readonly>
									<input type="hidden" class="form-control form-control-sm" name="pcg_counterpty_cd" readonly>
									<input type="hidden" class="form-control form-control-sm" name="pcg_counterparty_abbr" readonly>
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
									<select class="form-select form-select-sm" name="pcg_sec_category">
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
				      				<select class="form-select form-select-sm" name="pcg_deal_type" id="deal_type_pcg" onchange="checkDealType()">
				      					<option value="" >--Select--</option>
				      					<option value="GAS" >GAS</option>
				      					<option value="LTCORA" >LTCORA</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="button" class="btn btn-sm config_btn" id="PcgdealConfig" value="Attach Contract#" 
					    			<%if(clearance.equals("K")){ %>data-bs-toggle="modal" data-bs-target="#PcgDealNoModel" onclick="showDealNo()" disabled<%} %>>
					    			<!--HP20230914 NOT REQUIRED DEAL CONFIG FOR IGX  -->
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label" id="PcgDealNoDisplay" style="display:none;"></label>
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
				    				<input type="text" class="form-control form-control-sm" name="pcg_value" id="pcg_value" onchange="isNumeric_value_pcg()">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="pcg_currency" id="pcg_currency" onchange="isNumeric_value_pcg()">
				      					<option value="1" selected="selected">INR</option>
				      					<option value="2">USD</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Guarantor Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="pcg_guarantor_name">
				      					<option value="0">--Select--</option>
				      					<%for(int i=0; i<VPO_GUARANTOR_CD.size(); i++){ %>
				      						<option value="<%=VPO_GUARANTOR_CD.elementAt(i)%>"><%=VPO_GUARANTOR_NAME.elementAt(i)%></option>
				      					<%} %>
				      				</select>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Recv./Auth.  Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="pcg_received_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Review Date</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="pcg_review_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Issuance Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="pcg_issuance_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);countTenorPcg();" onchange="validateDate(this);countTenorPcg();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Expire Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="pcg_expire_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);countTenorPcg();" onchange="validateDate(this);countTenorPcg();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
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
				      				<select class="form-select form-select-sm" name="pcg_status">
				      					<option value="">--Select--</option>
				      					<option value="O">In order</option>
				      					<option value="A">Pending for Amendment</option>
				      					<option value="C">Cancelled</option>
				      				</select>
				      				<select class="form-select form-select-sm" name="pcg_status2" >
				      					<option value="O">In order</option>
				      					<option value="C">Cancelled</option>
				      				</select>
				      				<input type="text" class="form-control form-control-sm" name="pcg_status1" readonly style="display: none;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
			    			<label class="form-label" style="background:#FFF4A3;"><b>Tenor</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="pcg_tenor" readOnly style="background:#FFF4A3;">
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
				    				<input type="text" class="form-control form-control-sm" name="pcg_remark">
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
						<input type="button" class="btn btn-warning com-btn" id="pcg_reset_btn" value="Restate" onclick="doRestate();" style="display: none;">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" id="pcg_reset_btn" value="Restate" style="display: none;">
						<%} %>
					</div>
	        		<div align="right" >
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" id="pcg_sub_btn" value="Submit" onclick="doSubmitPcg();" style="display: button;">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" id="pcg_sub_btn" value="Submit" disabled style="display: button;">
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
        			Add New Security
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
				      					<option value="">--Select--</option>
				      					<option value="LC">LC Letter Of Credit</option>
				      					<option value="BG">BG Bank Guarantee</option>
				      					<option value="PCG">PCG Parent Corporate Guarantee</option>
				      					<!-- <option value="ADV">ADV Advance Payment</option> -->
				      				</select>
				    			</div>
				  			</div>
						</div>
						
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
								
									<input type="button" class="btn btn-sm config_btn" value="Attach Contract#" 
					    			<%if(clearance.equals("K")){ %>data-bs-toggle="modal" data-bs-target="#NewDealNoModel" onclick="showNewDealNo()"<%} %>>
					    			<!--HP20230914 NOT REQUIRED DEAL CONFIG FOR IGX  -->
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
				      				<input type="text" class="form-control form-control-sm" name="new_value" id="new_value" onchange="isNumeric_value_new()">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">  
							<div class="form-group row">
				    			<div class="col-sm-10 col-xs-10 col-md-10">
				      				<select class="form-select form-select-sm" name="new_currency" id="new_currency" onchange="isNumeric_value_new()">
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
	        					<td valign="top" style="display:none;" id="new_column_deal_<%=VCONTRACT_DTL.elementAt(i)%>">
	        						<input type="checkbox" class="form-check-input" name="new_chk_deal" value="<%=VCONTRACT_DTL.elementAt(i)%>" onclick="enableSplit(this,'new_split_value_<%=VCONTRACT_DTL.elementAt(i)%>');newDealChecksValidate()">&nbsp;<%=VNEW_COUNTPTY_ABBR.elementAt(i)%>-<%=V_DEAL_NO.elementAt(i)%> <label style="color:blue;"> (<%=V_DURATION.elementAt(i) %>)</label>&nbsp;&nbsp;&nbsp;&nbsp;
				    				<input type="hidden"  name="new_deal_dtl" value="<%=VCONTRACT_DTL.elementAt(i)%>" >
				    				<input type="hidden"  name="new_deal_no" value="<%=V_DEAL_NO.elementAt(i)%>" >
				    				<input type="hidden"  name="new_abbr" value="<%=VNEW_COUNTPTY_ABBR.elementAt(i)%>" >
	        					</td>
	        					<td>
	        						<div class="col-auto" id="new_hid_col_split_<%=VCONTRACT_DTL.elementAt(i)%>" style="display:none;">
			    						<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm" name="new_split_value" id="new_split_value_<%=VCONTRACT_DTL.elementAt(i)%>" 
				      						value="" maxLength="6" size="5" onblur="checkNumber1(this,5,2);" disabled style="text-align:right;">	
				      						<span class="input-group-text"><i class="fa fa-percent fa-lg"></i></span>
			      						</div>&nbsp;
			      					</div>
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
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitNewPercent()" >
				</div>
      		</div>
        </div>
    </div>
</div>

<div class="modal fade" id="DealNoModel" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
       			<div class="topheader" id="deal_hdr">
				</div>
        	</div>
        	<div class="modal-body mdbody">
        		<div class="cdbody">
        			<table>
        			<%for(int i=0; i<VALL_DEAL_DTL.size(); i++)
        			{ 
        				String temp_duration = ""+VDURATION.elementAt(i);
	        			String deal_no = ""+VALL_DEAL_NO.elementAt(i);
	        			String deal_dtl = ""+VALL_DEAL_DTL.elementAt(i);
	        			String cp_abbr = ""+VALL_CP_ABBR.elementAt(i); //HP20230914
	        			String[] dealNo = deal_no.split(", ");
	        			String[] dealDtl = deal_dtl.split(", ");
	        			String[] cpAbbr = cp_abbr.split(", ");//HP20230914
	        			String[] duration = temp_duration.split(", ");
	        			
	        		 	for(int j=0; j<dealDtl.length; j++)
	     				{
	     				%>
        				<tr>
        					<td style="display:none;" id="column_deal_<%=dealDtl[j]%>">
       							<input type="checkbox" class="form-check-input"  name="chk_deal" value="<%=dealDtl[j]%>" onclick="enableSplit(this,'split_value_<%=dealDtl[j]%>');validateChecks('chk_deal')">
       							<%--HP20230914 &nbsp;<%=VCOUNTERPARTY_ABBR.elementAt(j)%>-<%=dealNo[j]%>&nbsp; --%>
        						&nbsp;<%=cpAbbr[j]%>-<%=dealNo[j]%> <label style="color:blue;"> (<%=duration[j] %>)</label>&nbsp;
			    				<input type="hidden"  name="deal_dtl"  value="<%=dealDtl[j]%>" >
			    				<input type="hidden"  name="deal_no"  value="<%=dealNo[j]%>" >
			    				<%--HP20230914 <input type="hidden"  name="abbr" value="<%=VCOUNTERPARTY_ABBR.elementAt(j)%>" disabled> --%>
			    				<input type="hidden"  name="abbr" value="<%=cpAbbr[j]%>" >
        					</td>
        					<td>
        						<div class="col-auto" id="hid_col_split_<%=dealDtl[j]%>" style="display:none;">
		    						<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="split_value" id="split_value_<%=dealDtl[j]%>" 
			      						value="" maxLength="6" size="5" onblur="checkNumber1(this,5,2);" disabled style="text-align:right;">	
			      						<span class="input-group-text"><i class="fa fa-percent fa-lg"></i></span>
		      						</div>
		      					</div>
	    						<input type="hidden" name="sec_split" value="<%=dealDtl[j]%>">
        					</td>
        				</tr>
        				<%} %>
        			<%} %>
        			</table>
        			<div align="center" id="lc_msg" style="display: none;"></div>
      			</div>
        	</div>
        	<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Back" data-bs-target="#SecurityLc" data-bs-toggle="modal" data-bs-dismiss="modal">
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitPercent()">
				</div>
      		</div>
        </div>
    </div>
</div>

<div class="modal fade" id="PcgDealNoModel" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader" id="pcg_deal_hdr">
				</div>
        	</div>
        	<div class="modal-body mdbody">
        		<div class="cdbody">
        			<table>
        			<%for(int i=0; i<VALL_DEAL_DTL.size(); i++)
        			{ 
        				String temp_duration = ""+VDURATION.elementAt(i);
        				String deal_no = ""+VALL_DEAL_NO.elementAt(i);
	        			String deal_dtl = ""+VALL_DEAL_DTL.elementAt(i);
	        			String cp_abbr = ""+VALL_CP_ABBR.elementAt(i); //HP20230914
	        			String[] dealNo = deal_no.split(", ");
	        			String[] dealDtl = deal_dtl.split(", ");
	        			String[] cpAbbr = cp_abbr.split(", ");//HP20230914
	        			String[] duration = temp_duration.split(", ");
	        			%>
        				<% for(int j=0; j<dealDtl.length; j++)
	        			{%>
        				<tr>
        					<td style="display:none;" id="pcg_column_deal_<%=dealDtl[j]%>">
        						<input type="checkbox" class="form-check-input" name="pcg_chk_deal" value="<%=dealDtl[j]%>" onclick="enableSplit(this,'pcg_split_value_<%=dealDtl[j]%>');validateChecks('pcg_chk_deal')">
        						<%--HP20230914 &nbsp;<%=VCOUNTERPARTY_ABBR.elementAt(j)%>-<%=dealNo[j]%>&nbsp;&nbsp;&nbsp;&nbsp; --%>
        						&nbsp;<%=cpAbbr[j]%>-<%=dealNo[j]%> <label style="color:blue;"> (<%=duration[j] %>)</label>&nbsp;&nbsp;&nbsp;&nbsp;
			    				<input type="hidden"  name="pcg_deal_dtl" value="<%=dealDtl[j]%>" >
			    				<input type="hidden"  name="pcg_deal_no" value="<%=dealNo[j]%>" >
			    				<%--HP20230914 <input type="hidden"  name="pcg_abbr" value="<%=VCOUNTERPARTY_ABBR.elementAt(j)%>" disabled> --%>
			    				<input type="hidden"  name="pcg_abbr" value="<%=cpAbbr[j]%>" >
        					</td>
        					<td>
        						<div class="col-auto" id="pcg_hid_col_split_<%=dealDtl[j]%>" style="display:none;">
		    						<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="pcg_split_value" id="pcg_split_value_<%=dealDtl[j]%>" 
			      						value="" maxLength="6" size="5" onblur="checkNumber1(this,5,2);" disabled style="text-align:right;">	
			      						<span class="input-group-text"><i class="fa fa-percent fa-lg"></i></span>
		      						</div>
		      					</div>
	    						<input type="hidden" name="pcg_sec_split" value="<%=dealDtl[j]%>">
        					</td>
        				</tr>
        				<%} %>
        			<%} %>
        			</table>
        			<div align="center" id="pcg_msg" style="display: none;"></div>
      			</div>
        	</div>
        	<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Back" data-bs-target="#SecurityPcg" data-bs-toggle="modal" data-bs-dismiss="modal">
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitPercent()">
				</div>
      		</div>
        </div>
    </div>
</div>

<input type="hidden" name="status_flg">
<input type="hidden" name="pcg_status_flg">
<input type="hidden" name="cancel_flg">
<input type="hidden" name="pcg_cancel_flg">
<input type="hidden" name="adv_cancel_flg">
<input type="hidden" name="prev_clearance" value="<%=clearance%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="ref_no">
<input type="hidden" name="seq_no">
<input type="hidden" name="seq_rev_no">
<input type="hidden" name="pcg_ref_no">
<input type="hidden" name="pcg_seq_no">
<input type="hidden" name="pcg_seq_rev_no">
<input type="hidden" name="option" value="SECURITY_MST">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="lc_old_value" value="">
<input type="hidden" name="pcg_old_value" value="">
<input type="hidden" name="lc_inorder_hist" value="">
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
	
	var status;
	var rest_status;
	var button;
	var deal_type;
	
	if(sec_type=="LC" || sec_type=="BG")
	{
		deal_type = document.forms[0].deal_type.value;
		button = document.getElementById("dealConfig");
		status = document.forms[0].status.value;
		rest_status = document.forms[0].status1.value;
	}
	else if(sec_type=="PCG")
	{
		deal_type = document.forms[0].pcg_deal_type.value;
		button = document.getElementById("PcgdealConfig");
		status = document.forms[0].pcg_status.value;
		rest_status = document.forms[0].pcg_status1.value;
	}
	else
	{
		deal_type="";
	}
	
	if(status == "O" || rest_status == "Restated" || status == "C")
	{
		button.disabled=true;
	}
	else
	{
		if(trim(deal_type) == "" || deal_type==undefined || deal_type==null)
		{
			button.disabled=true;
		}
		else
		{
			button.disabled=false;
		}
	}
}

function enableSplit(obj,id)
{
	if(obj.checked)
	{
		document.getElementById(id).disabled=false;
		
	}
	else
	{
		document.getElementById(id).disabled=true;
	}
}

//Added By pratham Bhatt 20240810 for validating checks
function newDealChecksValidate()
{
	var checkbox = document.getElementsByName("new_chk_deal");
	var lt_flag = 0;
	var gs_flag = 0;
	var split_val=document.forms[0].new_split_value;
	
	  for (var i = 0; i < checkbox.length; i++)
	  {
		  var flag = false;
          if (checkbox[i].checked) 
          {
        	  let deal = checkbox[i].value.split("-");
        	  deal_typ = deal[1];
        	  if(deal_typ==="G"||deal_typ==="P")
			  {
        		  lt_flag=1;
			  }
        	  else 
        	  {
        		  gs_flag=1;
        	  } 
          }
          if(lt_flag+gs_flag>1)
          {
        	alert("Selected Contract must be from same deal type!");
        	checkbox.forEach(checkbox=>{checkbox.checked=false;});
        	split_val.forEach(split_val=>{split_val.disabled=true;});
        	return false;  
          }
	  }    
}
function validateChecks(name)
{
	var checkbox = document.getElementsByName(""+name);
	var split_val
	if(name=="chk_deal")
	{
		split_val = document.forms[0].split_value;
	}
	else if(name=="pcg_chk_deal")
	{
		split_val = document.forms[0].pcg_split_value;
	}
	var lt_flag = 0;
	var gs_flag = 0;
	  for (var i = 0; i < checkbox.length; i++)
	  {
          if (checkbox[i].checked) 
          {
        	  let deal = checkbox[i].value.split("/");
        	  deal_typ = deal[1];
        	  if(deal_typ==="G"||deal_typ==="P")
			  {
        		  lt_flag=1;
			  }
        	  else 
        	  {
        		  gs_flag=1;
        	  } 
          }
       	  if(lt_flag+gs_flag>1)
       	  {
       		  alert("Selected Contract must be from same deal type!");
       		  checkbox.forEach(checkbox=>{checkbox.checked=false;});
       		  split_val.forEach(split_val=>{split_val.disabled=true;});
       		  
       		  //checkbox[i].checked=false;
       		  return false;
       	  }
	  }    
}



function setDealNoDtl(DealNo,share_percent)
{
	var clearance = document.forms[0].clearance.value;
	var sel_deal_no=DealNo.split("@@");
	var sec_type = document.forms[0].sec_type.value;
	
	var sel_share_percent = share_percent.split("@@");
	
	var chk_deal="";
	var share_percent="";
	
	if(sec_type=="LC" || sec_type=="BG")
	{
		chk_deal = document.forms[0].chk_deal;
		share_percent = document.forms[0].split_value;
	}
	else if(sec_type=="PCG")
	{
		chk_deal = document.forms[0].pcg_chk_deal;
		share_percent = document.forms[0].pcg_split_value;
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
	  				share_percent[i].disabled=false;
	  				share_percent[i].value = sel_share_percent[j];
	  			} 
	 		} 
		}
		else if(chk_deal!=null)
		{
			if(chk_deal.value == sel_deal_no[j])
			{
				chk_deal.checked=true;
				share_percent.disabled=false;
				share_percent.value = sel_share_percent[j];
  				
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
	  				document.getElementById("new_hid_col_split_"+chkDeal).style.display="table-cell";
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
	  				document.getElementById("new_hid_col_split_"+chkDeal).style.display="table-cell";
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
 				document.getElementById("new_hid_col_split_"+chkDeal).style.display="table-cell";
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
 				document.getElementById("new_hid_col_split_"+chkDeal).style.display="table-cell";
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
	var deal_type;
	var sec_type = document.forms[0].sec_type.value;
	var counterparty_cd="";
	var clearance = document.forms[0].clearance.value; //HP20230914
	var ref_no="";
	var chk_deal="";
	var column = "";
	var disp_msg = "<div colspan='22' align='center'><div class='container'><div class='alert alert-info'><i class='fa fa-info-circle fa-lg'></i>&nbsp; Deal No is Not Available </div></div></div>"
	var msg = "";
	var split = "";
	if(sec_type=="LC" || sec_type=="BG")
	{
		deal_type = document.forms[0].deal_type.value;
		ref_no = document.forms[0].ref_no.value;
		counterparty_cd = document.forms[0].counterpty_cd.value;
		chk_deal = document.forms[0].chk_deal;
	}
	else if(sec_type=="PCG")
	{
		deal_type = document.forms[0].pcg_deal_type.value;
		ref_no = document.forms[0].pcg_ref_no.value;
		counterparty_cd = document.forms[0].pcg_counterpty_cd.value;
		chk_deal = document.forms[0].pcg_chk_deal;
	}
		
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
  		for(var i=0;i<chk_deal.length;i++)
  		{
  			
  			var chkDeal=chk_deal[i].value;
  			var split_val=chkDeal.split("/");
  			var count_cd=""
  			var contType=""; //HP20230914
  			if(chkDeal!="")
  			{
  				count_cd=split_val[0];
  				contType=split_val[1]; //HP20230914
  			}
  			
  			if(sec_type=="LC" || sec_type=="BG")
  			{
  				column = document.getElementById("column_deal_"+chkDeal);
  				msg = document.getElementById("lc_msg");
  				split =  document.getElementById("hid_col_split_"+chkDeal);
  			}
  			else if(sec_type=="PCG")
  			{
  				column = document.getElementById("pcg_column_deal_"+chkDeal);
  				msg = document.getElementById("pcg_msg")
  				split =  document.getElementById("pcg_hid_col_split_"+chkDeal);
  			}
			
  			
  			if(clearance=="I") //HP20230914 NO NEED CHECK COUNTERPARTY_CD FOR IGX
  			{
  				if(contType!="" && ((contType=="X" || contType=="I") && count_cd==ref_no))
  	 			{
  					column.style.display="table-cell";
  					split.style.display="table-cell";
	  				//msg.style.display="none";
  	 			}
  				else
	  			{
	  				column.style.display="none";
	  				//msg.style.display="block";
	  				//msg.innerHTML=disp_msg;
	  				split.style.display="none";
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
	  						split.style.display="table-cell";
	  					}
	  					else
	  					{
	  						column.style.display="none";
	  						split.style.display="none";
	  		 				chk_deal[i].checked=false;
	  					}
	  				}
	  				else if( deal_type=="GAS")
	  				{
	  					if( (contType=="G" || contType=="P"))
	  					{
	  						column.style.display="none";
	  						split.style.display="none";
	  		 				chk_deal[i].checked=false;
	  						
	  					}
	  					else
	  					{
	  						column.style.display="table-cell";
	  						split.style.display="table-cell";
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
		
		if(sec_type=="LC" || sec_type=="BG")
		{
			column = document.getElementById("column_deal_"+chkDeal);
			msg = document.getElementById("lc_msg"); 
		}
		else if(sec_type=="PCG")
		{
			column = document.getElementById("pcg_column_deal_"+chkDeal);
			msg = document.getElementById("pcg_msg");
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
	var new_split_value = document.forms[0].new_split_value;
	var msg="";
	var flag=true;
	var count_percent=parseFloat("0");
	var display ="";
	
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
  		for(var i=0;i<chk_deal.length;i++)
  		{
   			if(chk_deal[i].checked)
   			{
   				if(display!="")
   				{
   					display+=" , "+count_abbr[i].value+"-"+new_deal_no[i].value;
   				}
   				else
   				{
   					display+=count_abbr[i].value+"-"+new_deal_no[i].value;
   				}
   				if(new_split_value[i].value!="")
   				{
   					display+=" <font style='background:#ff99ff'>("+new_split_value[i].value+"%)</font>";
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
				display+=" , "+count_abbr[i].value+"-"+new_deal_no.value;
			}
			else
			{
				display+=count_abbr[i].value+"-"+new_deal_no.value;
			}
   			if(new_split_value.value!="")
			{
				display+=" <font style='background:#ff99ff'>("+new_split_value.value+"%)</font>";
			}
   		} 
 	}
	
	document.getElementById("NewDealNoDisplay").innerHTML=display;
	if(display != "")
	{
		document.getElementById("NewDealNoDisplay").style.display="inline";
	}
	$("#NewDealNoModel").modal("hide");
	$("#AddNewSecurity").modal("show");
}

function doSubmitDealNo()
{
	var sec_type = document.forms[0].sec_type.value;
	var chk_deal = "";
	var deal_no = "";
	var deal_dtl = "";
	var count_abbr = "";
	var split_value = "";
	
	var msg="";
	var flag=true;
	var count_percent=parseFloat("0");
	
	if(sec_type=="LC" || sec_type=="BG")
	{
		chk_deal = document.forms[0].chk_deal;
		deal_no = document.forms[0].deal_no;
		deal_dtl = document.forms[0].deal_dtl;
		count_abbr = document.forms[0].abbr;
		split_value = document.forms[0].split_value;
	}
	else if(sec_type=="PCG")
	{
		chk_deal = document.forms[0].pcg_chk_deal;
		deal_no = document.forms[0].pcg_deal_no;
		deal_dtl = document.forms[0].pcg_deal_dtl;
		count_abbr = document.forms[0].pcg_abbr;
		split_value = document.forms[0].pcg_split_value;
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
	   					display+=" , "+count_abbr[i].value+"-"+deal_no[i].value;
	   				}
	   				else
	   				{
	   					display+=count_abbr[i].value+"-"+deal_no[i].value;
	   				}
	   				if(split_value[i].value!="")
	   				{
	   					display+=" <font style='background:#ff99ff'>("+split_value[i].value+"%)</font>";
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
					display+=" , "+count_abbr.value+"-"+deal_no.value;
				}
				else
				{
					display+=count_abbr.value+"-"+deal_no.value;
				}
	   			if(split_value.value!="")
   				{
   					display+=" <font style='background:#ff99ff'>("+split_value.value+"%)</font>";
   				}
   			}
   			else
   			{
   				display="";
   			}

   		} 
 	}
		
	if(sec_type=="LC" || sec_type=="BG")
	{
		if(display != "")
		{
			document.getElementById("DealNoDisplay").innerHTML=display;
			document.getElementById("DealNoDisplay").style.display="inline";
			//PrathamBHatt 20240809
			var deal_val = document.getElementById("deal_type_bg_lc").value;
			
			display1 = display.split("-");
			
			//if(display1[1].startsWith("G") || display1[1].startsWith("P"))
			if(display1.includes("G") || display1.includes("P"))
			{
				document.getElementById("deal_type_bg_lc").value = "LTCORA";
				document.forms[0].deal_type.value = "LTCORA";
			}
			else
			{
				document.getElementById("deal_type_bg_lc").value = "GAS";
				document.forms[0].deal_type.value = "GAS";
			}
				document.forms[0].deal_type.style.pointerEvents="none";
		}
		$("#DealNoModel").modal("hide");
		$("#SecurityLc").modal("show");
	}
	else if(sec_type=="PCG")
	{
		if(display != "")
		{
			document.getElementById("PcgDealNoDisplay").innerHTML=display;
			document.getElementById("PcgDealNoDisplay").style.display="inline";
			//PrathamBHatt 20240809
			display1 = display.split("-");
			
//			if(display1[1].startsWith("G") || display1[1].startsWith("P"))
			if(display1.includes("G") || display1.includes("P"))
			{
				document.getElementById("deal_type_pcg").value = "LTCORA";
				document.forms[0].pcg_deal_type.value = "LTCORA";
			}
			else
			{
				document.getElementById("deal_type_pcg").value = "GAS";
				document.forms[0].pcg_deal_type.value = "GAS";
			}
			document.forms[0].pcg_deal_type.style.pointerEvents="none";
		}
		$("#PcgDealNoModel").modal("hide");
		$("#SecurityPcg").modal("show");
	}
}


function doSubmitNewPercent()
{
	var chk_deal = document.forms[0].new_chk_deal;
	var new_deal_no = document.forms[0].new_deal_no;
	var new_deal_dtl = document.forms[0].new_deal_dtl;
	var count_abbr = document.forms[0].new_abbr;
	var new_split_value = document.forms[0].new_split_value;
	var msg="";
	var flag=true;
	var count_percent=parseFloat("0");
	var display ="";
	
	var count=parseInt("0");
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
		var temp_msg="";
		var temp_flag=true;  		
		for(var i=0;i<chk_deal.length;i++)
  		{
   			if(chk_deal[i].checked)
   			{
				count+=1;
   				if(display!="")
   				{
   					display+=", "+count_abbr[i].value+"-"+new_deal_no[i].value;
   				}
   				else
   				{
   					display+=count_abbr[i].value+"-"+new_deal_no[i].value;
   				}
   				
   				if(new_split_value[i].value!="")
   				{
   					count_percent=parseFloat(count_percent) + parseFloat(new_split_value[i].value);
   				}
   				else
   				{
   					temp_msg+="Enter Percentage for "+count_abbr[i].value+"-"+new_deal_no[i].value+" !\n";
   					temp_flag=false;
   				}
   			} 
  		} 
  		if(count>1)
		{
			msg=temp_msg;
			flag=temp_flag;	
		}
 	}
 	else if(chk_deal!=null)
 	{
   		if(chk_deal.checked)
     	{
   			if(display!="")
			{
				display+=", "+count_abbr[i].value+"-"+new_deal_no.value;
			}
			else
			{
				display+=count_abbr[i].value+"-"+new_deal_no.value;
			}
   			if(new_split_value.value!="")
			{
   				count_percent=parseFloat(count_percent) + parseFloat(new_split_value.value);
			}
			else
			{
				//NOT REQUIRED FOR SINGLE DEAL
				//msg+="Enter Percentage for "+count_abbr.value+"-"+new_deal_no.value+" !\n";
				//flag=false;			
			}
   		} 
 	}
 	if(!flag)
 	{
 		alert(msg);
 	}
 	else
	{
 		if(count_percent!=100 && count_percent>0)
 		{
 			alert("total percentage should be equals to 100% !");
 		}
 		else
 		{
 			doSubmitNewDealNo()
 		}
	}
}

function doSubmitPercent()
{
	var sec_type = document.forms[0].sec_type.value;
	var chk_deal = "";
	var deal_no = "";
	var deal_dtl = "";
	var count_abbr = "";
	var split_value = "";
	
	var msg="";
	var flag=true;
	var count_percent=parseFloat("0");
	
	if(sec_type=="LC" || sec_type=="BG")
	{
		chk_deal = document.forms[0].chk_deal;
		deal_no = document.forms[0].deal_no;
		deal_dtl = document.forms[0].deal_dtl;
		count_abbr = document.forms[0].abbr;
		split_value = document.forms[0].split_value;
	}
	else if(sec_type=="PCG")
	{
		chk_deal = document.forms[0].pcg_chk_deal;
		deal_no = document.forms[0].pcg_deal_no;
		deal_dtl = document.forms[0].pcg_deal_dtl;
		count_abbr = document.forms[0].pcg_abbr;
		split_value = document.forms[0].pcg_split_value;
	}
	
	var display ="";
	var count=parseInt("0");
	if(chk_deal!=null && chk_deal.length!=undefined)
 	{
		var temp_msg="";
		var temp_flag=true;  		
		for(var i=0;i<chk_deal.length;i++)
  		{
   			if(chk_deal[i].checked)
   			{
				count+=1;
   				if(deal_no[i].value!="")
   				{
	   				if(split_value[i].value!="")
	   				{
	   					count_percent=parseFloat(count_percent) + parseFloat(split_value[i].value);
	   				}
	   				else
	   				{
	   					temp_msg+="Enter Percentage for "+count_abbr[i].value+"-"+deal_no[i].value+" !\n";
	   					temp_flag=false;
	   				}
   				}
   				else
				{
   					display="";
				}
   			} 
  		} 
		if(count>1)
		{
			msg=temp_msg;
			flag=temp_flag;	
		} 	
	}
 	else if(chk_deal!=null)
 	{
   		if(chk_deal.checked)
     	{
   			if(deal_no!="")
   			{
	   			if(split_value.value!="")
   				{
   					count_percent=parseFloat(count_percent) + parseFloat(split_value.value);
   				}
   				else
   				{
   					//NOT REQUIRED FOR SINGLE DEAL
   					//msg+="Enter Percentage for "+count_abbr.value+"-"+deal_no.value+" !\n";
   					//flag=false;   				
   				}
   			}
   			else
   			{
   				display="";
   			}
   		} 
 	}
		
	if(!flag)
 	{
 		alert(msg);
 	}
 	else
	{
 		if(count_percent!=100 && count_percent>0)
 		{
 			alert("total percentage should be equals to 100% !");
 		}
 		else
 		{	
 			doSubmitDealNo()
 		}
	}
}

</script>
</html>