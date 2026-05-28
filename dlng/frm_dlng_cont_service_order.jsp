<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh(opration)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	
		
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_cont_service_order.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+"&u="+u+
			"&contract_type="+contract_type;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function fcc(fcc_flg)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	
	var msg="";
	var flag=true;
	
	if(flag)
	{
		var a;
		
		if(fcc_flg=="Y")
		{
			a = confirm("Do you want to Approve Service Contract?");
		}
		else
		{
			a = confirm("Do you want to Disapprove Service Contract?");
		}
		if(a)
		{
			alert("Submit to update Deal Checks & Control!!");
			document.forms[0].fcc_flg.value=fcc_flg
		}
		else
		{
			if(document.forms[0].temp_fcc_flg.value=="Y")
			{
				document.forms[0].ff[0].checked=true;
				document.forms[0].ff[1].checked=false;
			}
			else if(document.forms[0].temp_fcc_flg.value=="N")
			{
				document.forms[0].ff[0].checked=false;
				document.forms[0].ff[1].checked=true;
			}
			else
			{
				document.forms[0].ff[0].checked=false;
				document.forms[0].ff[1].checked=false;
			}
		}
	}
	else
	{
		alert(msg);
		if(document.forms[0].temp_fcc_flg.value=="Y")
		{
			document.forms[0].ff[0].checked=true;
			document.forms[0].ff[1].checked=false;
		}
		else if(document.forms[0].temp_fcc_flg.value=="N")
		{
			document.forms[0].ff[0].checked=false;
			document.forms[0].ff[1].checked=true;
		}
		else
		{
			document.forms[0].ff[0].checked=false;
			document.forms[0].ff[1].checked=false;
		}
	}
}

function enabled_TransPlantDtl(obj,id1,id2)
{
	if(obj.checked)
	{
		document.getElementById(id1).disabled=false;
		document.getElementById(id2).disabled=false;
	}
	else
	{
		document.getElementById(id1).disabled=true;
		document.getElementById(id2).disabled=true;
	}
}

function doSubmit()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	var contract_type = document.forms[0].contract_type.value;
	
	var cont_ref_no = document.forms[0].cont_ref_no.value;
	var dda_dt = document.forms[0].dda_dt.value;
	var dda_time = document.forms[0].dda_time.value;
	var signing_dt = document.forms[0].signing_dt.value;
	var signing_time = document.forms[0].signing_time.value;
	var ent_dt = document.forms[0].ent_dt.value;
	var ent_time = document.forms[0].ent_time.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var transport_qty = document.forms[0].transport_qty.value;
	var cont_status_flg = document.forms[0].cont_status_flg.value;
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	var no_of_security_dtl = document.forms[0].no_of_security_dtl.value;
	var contdt_change_request_flag = document.forms[0].contdt_change_request_flag.value;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var entry_point = document.forms[0].entry_point.value;
	var exit_point = document.forms[0].exit_point.value;
	var allowed_lay_time_hrs = document.forms[0].allowed_lay_time_hrs.value;
	var allowed_lay_time_min = document.forms[0].allowed_lay_time_min.value;
	var transport_management_charge = document.forms[0].transport_management_charge.value;
	var transport_management_charge_unit = document.forms[0].transport_management_charge_unit.value;
	var transport_management_charge_eff_dt = document.forms[0].transport_management_charge_eff_dt.value;
	var qty_opt = document.forms[0].qty_opt;
	var qty_opt_frim = document.forms[0].qty_opt_frim.value;
	var qty_opt_re = document.forms[0].qty_opt_re.value;
	
	var msg="";
	var flag=true;
	
	if(counterparty_cd == "" || counterparty_cd == "0")
	{
		msg+="Select Counterparty!\n";
		flag=false;
	}
	if(trim(contract_type) == "")
	{
		msg+="Select Contract Type!\n";
		flag=false;
	}
	if(contract_type=="B")
	{
		var agmt_no = document.forms[0].agmt_no.value;
		if(agmt_no == "" || agmt_no == "0")
		{
			msg+="Select Agreement!\n";
			flag=false;
		}
	}
	if(opration=="MODIFY")
	{
		var cont_no = document.forms[0].cont_no.value;
		if(cont_no == "" || cont_no == "0")
		{
			msg+="Select Contact!\n";
			flag=false;
		}
	}
	
	if(trim(dda_dt) == "")
	{
		msg+="Enter DDA Date!\n";
		flag=false;
	}
	if(trim(dda_time) == "")
	{
		msg+="Enter DDA Time!\n";
		flag=false;
	}
	if(trim(signing_dt) == "")
	{
		msg+="Enter Contract/Trade Date!\n";
		flag=false;
	}
	if(trim(signing_time) == "")
	{
		msg+="Enter Signing Time!\n";
		flag=false;
	}
	if(trim(ent_dt) == "")
	{
		msg+="Enter Deal Enter Date!\n";
		flag=false;
	}
	if(trim(ent_time) == "")
	{
		msg+="Enter Deal Enter Time!\n";
		flag=false;
	}
	if(trim(start_dt) == "")
	{
		msg+="Enter Start Date!\n";
		flag=false;
	}
		
	if(trim(end_dt) == "")
	{
		msg+="Enter End Date!\n";
		flag=false;
	}
	
	if(trim(transport_qty) == "")
	{
		msg+="Enter Transport Quantity!\n";
		flag=false;
	}
	if(trim(entry_point) == "")
	{
		msg+="Select Entry Point!\n";
		flag=false;
	}
	if(trim(exit_point) == "")
	{
		msg+="Enter Exit Point!\n";
		flag=false;
	}
	if(trim(allowed_lay_time_hrs) == "" && trim(allowed_lay_time_min) == "")
	{
		msg+="Enter Allowed lay time!\n";
		flag=false;
	}
	if(trim(transport_management_charge) == "")
	{
		msg+="Enter Transport Management Charge!\n";
		flag=false;
	}
	if(trim(transport_management_charge_unit) == "")
	{
		msg+="Select Transport Management Charge Unit!\n";
		flag=false;
	}
	if(trim(transport_management_charge_eff_dt) == "")
	{
		msg+="Enter Transport Management Charge Effective date!\n";
		flag=false;
	}
	if(qty_opt[0].checked == false && qty_opt[1].checked == false)
	{
		msg+="Select Select Quantity Option!\n";
		flag=false;
	}
	if(trim(qty_opt_frim) == "")
	{
		msg+="Enter Quntity Firm!\n";
		flag=false;
	}
	if(trim(transport_management_charge_eff_dt) == "")
	{
		msg+="Enter Quantity RE!\n";
		flag=false;
	}
	
	//FOR BUSINESS UNIT
	chkFlg = false;
	if(chk_bu_plant!=null && chk_bu_plant!=undefined)
	{
		if(chk_bu_plant.length!=undefined)
		{
			for(var i=0;i<chk_bu_plant.length;i++)
			{
				if(chk_bu_plant[i].checked)
				{
					chkFlg = true;	
				}
			}
		}
		else
		{
			if(chk_bu_plant.checked)
			{
				chkFlg = true;	
			}
		}
	}
	if(chkFlg==false)
	{
		msg += "Please Select Atleast One Business Unit/Buyer Plant!\n";
		flag=false;
	}
	
	
	if(flag)
	{
		var a;
		
		var temp_confirm_msg="";
		if(contdt_change_request_flag == "Y")
		{
			temp_confirm_msg="On your Submission Contract Duration (Start Date and End Date) Chages will be Applied and associated fields will go read-Only!\n\n";	
		}
		if(opration=="MODIFY")
		{
				temp_confirm_msg+="Do you want to Modify Contract?";
				a = confirm(temp_confirm_msg);
			//}
		}
		else
		{
			a = confirm("Do you want to Create New Contract?");
		}
		if(a)
		{
			var temp_msg="";
			if(trim(no_of_billing_dtl) != "")
			{
				if(parseInt(no_of_billing_dtl) <= 0)
				{
					temp_msg += "Please fill in Billing Detail after Submitting Contract Detail!\n";
				}					
			}
			else
			{
				temp_msg += "Please fill in Billing Detail after Submitting Contract Detail!!\n";
			}
			if(trim(no_of_security_dtl) != "")
			{
				if(parseInt(no_of_security_dtl) <= 0)
				{
					temp_msg += "Please Enter Security Detail after Submitting Contract Detail!\n";
				}
			}
			else
			{
				temp_msg += "Please Enter Security Detail after Submitting Contract Detail!\n";
			}
			if(trim(temp_msg) != "")
			{
				alert(temp_msg)
			}
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
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_svc_cont_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type,"Service Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_svc_cont_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type,"Service Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function openAgreementList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	if(counterparty_cd=="0" || counterparty_cd=="")
	{
		alert("Select Counterparty!");
	}
	else
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open("frm_dlng_cont_agmt_svc_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_dlng_cont_agmt_svc_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function openBillingDtl()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var rate_unit = document.forms[0].transport_management_charge_unit.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_so_billing_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
			"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+
			"&u="+u+"&end_dt="+end_dt+"&rate_unit="+rate_unit;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Contract Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Contract Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,cont_type)
{
	var opration = document.forms[0].opration.value;
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_cont_service_order.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&contract_type="+cont_type+
			"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setAgmtDetail(countpty_cd,agmt_no,agmt_rev_no,agmt_type,agmt_nm)
{
	var opration = document.forms[0].opration.value;
	var contract_type = document.forms[0].contract_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_cont_service_order.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+
		"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&contract_type="+contract_type+"&agmt_nm="+agmt_nm+
		"&u="+u;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);

}

function setValues(strBuPlant)
{
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	
	var sepBuPlant = strBuPlant.split("@");
	
	//FOR BU PLANT
	if(chk_bu_plant!=null && chk_bu_plant.length!=undefined)
 	{
  		for(var i=0;i<chk_bu_plant.length;i++)
  		{
   			for(var j=0;j<sepBuPlant.length;j++)
   			{
     			if(chk_bu_plant[i].value == sepBuPlant[j])
     			{
     				chk_bu_plant[i].checked = true;
     			}
   			} 
  		} 
 	}
 	else if(chk_bu_plant!=null)
 	{
   		for(var j=0;j<sepBuPlant.length;j++)
   		{
   			if(chk_bu_plant.value == sepBuPlant[j])
     		{
   				chk_bu_plant.checked = true;
     		}
   		} 
 	}
}

function checkMmcq()
{
	var mmcq=document.forms[0].mmcq.value;
	if(mmcq!=null && trim(mmcq)!='' )
	{
	    var mmcq_per = parseFloat(mmcq);
		if(mmcq_per < 100)
		{
			alert("MMCQ percentage cannot be less than 100 percent !!! ");
			document.forms[0].mmcq.value='';
		}
	}
}

var securityWindow;
function securityPreReceipt(cont_type,agmt,agmt_rev,cont,cont_rev)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var sec_category = document.forms[0].sec_category.value;
	
	var temp_start_dt = document.forms[0].temp_start_dt.value;
	var temp_end_dt = document.forms[0].temp_end_dt.value;
	
	var u = document.forms[0].u.value;
	
	if(trim(cont_type)!="" && (trim(cont)!="" && cont!="0") && (trim(counterparty_cd)!="" && counterparty_cd!="0"))
	{
		var url = "../credit_risk/frm_pre_receipt_security.jsp?counterparty_cd="+counterparty_cd+"&cont_no="+cont+"&cont_rev_no="+cont_rev+
			"&agmt_no="+agmt+"&agmt_rev_no="+agmt_rev+"&contract_type="+cont_type+"&sec_category="+sec_category+"&start_dt="+temp_start_dt+"&end_dt="+temp_end_dt+
			"&u="+u;
		
		if(!securityWindow || securityWindow.closed)
		{
			securityWindow = window.open(url,"Pre-Receipt Security","top=10,left=100,width=1200,height=600,scrollbars=1");
		}
		else
		{
			securityWindow.close();
			securityWindow = window.open(url,"Pre-Receipt Security","top=10,left=100,width=1200,height=600,scrollbars=1");
		}
	}
	else
	{
		alert("Select Contract Detail!")
	}
}

/* var dcqWindow;
function variableDCQ(cont_type,agmt,agmt_rev,cont,cont_rev)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var temp_start_dt = document.forms[0].temp_start_dt.value;
	var temp_end_dt = document.forms[0].temp_end_dt.value;
	
	var u = document.forms[0].u.value;
	
	if(trim(cont_type)!="" && (trim(cont)!="" && cont!="0") && (trim(counterparty_cd)!="" && counterparty_cd!="0"))
	{
		var url = "../contract_master/frm_contract_dcq_dtl.jsp?counterparty_cd="+counterparty_cd+"&cont_no="+cont+"&cont_rev_no="+cont_rev+
			"&agmt_no="+agmt+"&agmt_rev_no="+agmt_rev+"&contract_type="+cont_type+"&start_dt="+temp_start_dt+"&end_dt="+temp_end_dt+
			"&u="+u;
		
		if(!dcqWindow || dcqWindow.closed)
		{
			dcqWindow = window.open(url,"Variable DCQ","top=10,left=100,width=1200,height=600,scrollbars=1");
		}
		else
		{
			dcqWindow.close();
			dcqWindow = window.open(url,"Variable DCQ","top=10,left=100,width=1200,height=600,scrollbars=1");
		}
	}
	else
	{
		alert("Select Contract Detail!")
	}
}*/
function securityRefresh(msg,msg_type)
{
	alert(msg);
	if(msg_type=="S")
	{
		document.forms[0].securityFlag.value="Y"
	}
	else
	{
		document.forms[0].securityFlag.value="N"
	}
}

function doContractDurationChangeRequest()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	var contract_type = document.forms[0].contract_type.value;
	
	var cont_ref_no = document.forms[0].cont_ref_no.value;
	var dda_dt = document.forms[0].dda_dt.value;
	var dda_time = document.forms[0].dda_time.value;
	var signing_dt = document.forms[0].signing_dt.value;
	var signing_time = document.forms[0].signing_time.value;
	var ent_dt = document.forms[0].ent_dt.value;
	var ent_time = document.forms[0].ent_time.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var transport_qty = document.forms[0].transport_qty.value;
	var cont_status_flg = document.forms[0].cont_status_flg.value;
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	var no_of_security_dtl = document.forms[0].no_of_security_dtl.value;
	var contdt_change_request_flag = document.forms[0].contdt_change_request_flag.value;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var entry_point = document.forms[0].entry_point.value;
	var exit_point = document.forms[0].exit_point.value;
	var allowed_lay_time_hrs = document.forms[0].allowed_lay_time_hrs.value;
	var allowed_lay_time_min = document.forms[0].allowed_lay_time_min.value;
	var transport_management_charge = document.forms[0].transport_management_charge.value;
	var transport_management_charge_unit = document.forms[0].transport_management_charge_unit.value;
	var transport_management_charge_eff_dt = document.forms[0].transport_management_charge_eff_dt.value;
	var qty_opt = document.forms[0].qty_opt;
	var qty_opt_frim = document.forms[0].qty_opt_frim.value;
	var qty_opt_re = document.forms[0].qty_opt_re.value;
	
	var msg="";
	var flag=true;
	
	if(counterparty_cd == "" || counterparty_cd == "0")
	{
		msg+="Select Counterparty!\n";
		flag=false;
	}
	if(trim(contract_type) == "")
	{
		msg+="Select Contract Type!\n";
		flag=false;
	}
	if(contract_type=="B")
	{
		var agmt_no = document.forms[0].agmt_no.value;
		if(agmt_no == "" || agmt_no == "0")
		{
			msg+="Select Agreement!\n";
			flag=false;
		}
	}
	if(opration=="MODIFY")
	{
		var cont_no = document.forms[0].cont_no.value;
		if(cont_no == "" || cont_no == "0")
		{
			msg+="Select Contact!\n";
			flag=false;
		}
	}
	
	if(trim(dda_dt) == "")
	{
		msg+="Enter DDA Date!\n";
		flag=false;
	}
	if(trim(dda_time) == "")
	{
		msg+="Enter DDA Time!\n";
		flag=false;
	}
	if(trim(signing_dt) == "")
	{
		msg+="Enter Contract/Trade Date!\n";
		flag=false;
	}
	if(trim(signing_time) == "")
	{
		msg+="Enter Signing Time!\n";
		flag=false;
	}
	if(trim(ent_dt) == "")
	{
		msg+="Enter Deal Enter Date!\n";
		flag=false;
	}
	if(trim(ent_time) == "")
	{
		msg+="Enter Deal Enter Time!\n";
		flag=false;
	}
	if(trim(start_dt) == "")
	{
		msg+="Enter Start Date!\n";
		flag=false;
	}
		
	if(trim(end_dt) == "")
	{
		msg+="Enter End Date!\n";
		flag=false;
	}
	
	if(trim(transport_qty) == "")
	{
		msg+="Enter Transport Quantity!\n";
		flag=false;
	}
	if(trim(entry_point) == "")
	{
		msg+="Select Entry Point!\n";
		flag=false;
	}
	if(trim(exit_point) == "")
	{
		msg+="Enter Exit Point!\n";
		flag=false;
	}
	if(trim(allowed_lay_time_hrs) == "" && trim(allowed_lay_time_min) == "")
	{
		msg+="Enter Allowed lay time!\n";
		flag=false;
	}
	if(trim(transport_management_charge) == "")
	{
		msg+="Enter Transport Management Charge!\n";
		flag=false;
	}
	if(trim(transport_management_charge_unit) == "")
	{
		msg+="Select Transport Management Charge Unit!\n";
		flag=false;
	}
	if(trim(transport_management_charge_eff_dt) == "")
	{
		msg+="Enter Transport Management Charge Effective date!\n";
		flag=false;
	}
	if(qty_opt[0].checked == false && qty_opt[1].checked == false)
	{
		msg+="Select Select Quantity Option!\n";
		flag=false;
	}
	if(trim(qty_opt_frim) == "")
	{
		msg+="Enter Quntity Firm!\n";
		flag=false;
	}
	if(trim(transport_management_charge_eff_dt) == "")
	{
		msg+="Enter Quantity RE!\n";
		flag=false;
	}
	
	//FOR BUSINESS UNIT
	chkFlg = false;
	if(chk_bu_plant!=null && chk_bu_plant!=undefined)
	{
		if(chk_bu_plant.length!=undefined)
		{
			for(var i=0;i<chk_bu_plant.length;i++)
			{
				if(chk_bu_plant[i].checked)
				{
					chkFlg = true;	
				}
			}
		}
		else
		{
			if(chk_bu_plant.checked)
			{
				chkFlg = true;	
			}
		}
	}
	if(chkFlg==false)
	{
		msg += "Please Select Atleast One Business Unit/Buyer Plant!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Contract Revision will be Submitted Along With Contract Duration Modification Request!\n\nDo You Want To Revise The Contract Details, With Contract Duration Modification Request?");
		if(a)
		{
			document.forms[0].change_request.value="CONTRACT_DATE";
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function billingShows()
{
	const checkbox = document.getElementById("billing_flag");
	const div_std1 = document.getElementById("billing_flag_div");
	const div_std2 = document.getElementById("billing_flag_div1");

	if (checkbox.checked) 
	{
		div_std1.style.display = "block";
		div_std2.style.display = "block";
	} 
	else 
	{
		div_std1.style.display = "none";
		div_std2.style.display = "none";
	}
}


function checkValuePrecision(value,precision,maxVal,id)
{
	try
	{
		if(value == "" || value == null)
		{
			
		}
		else
		{
			var number = parseFloat(value);
	        if (isNaN(number))
	        {
	            alert("Value is not a valid number!!");
	            
	            document.getElementById(id).value = "";
	        }
	        else
	        {
	        	if(value.includes('.'))
				{
					var parts = value.split('.');
					var integerPart = parts[0];
					var decimalPart = parts[1] || '';
					 
					
					if (integerPart.length <= maxVal)
					{
						//return true;
					}
					else
					{
						alert("Provided Value should be in ("+maxVal+", "+precision+") format!!");
						document.getElementById(id).value = "";
						//return false;
					}
					
					if (decimalPart.length <= precision)
					{
						//return true;
					}
					else
					{
						alert("Provided Value should be in ("+maxVal+", "+precision+") format!!");
						document.getElementById(id).value = "";
					}
				}
				else
				{
					if (value.length <= maxVal)
					 {
						 //return true;
					 }
					 else
					 {
						 alert("Provided Value should be in ("+maxVal+", "+precision+") format!!");
						 document.getElementById(id).value = "";
					 }
				}
	        }
		}
    }
	catch (error)
	{
        //alert(error);
        return false;
    }
}

function checkAgmtStartdate()
{
	var start_dt = document.forms[0].start_dt.value;
	var agmt_start_dt = document.forms[0].agmt_start_dt.value;
	var agmt_end_dt = document.forms[0].agmt_end_dt.value;
	
	var splitSt_dt = start_dt.split("/");
	var splitAgmtSt_dt = agmt_start_dt.split("/");
	var splitAgmt_end_dt = agmt_end_dt.split("/");
	
	var temp_st_dt = splitSt_dt[2]+splitSt_dt[1]+splitSt_dt[0];
	var temp_agmt_st_dt = splitAgmtSt_dt[2]+splitAgmtSt_dt[1]+splitAgmtSt_dt[0];
	var temp_agmt_end_dt = splitAgmt_end_dt[2]+splitAgmt_end_dt[1]+splitAgmt_end_dt[0];
	
	if(start_dt!="")
	{
		if(temp_st_dt < temp_agmt_st_dt)
		{
			alert("Contract Start date ("+start_dt+") should be gretar than or equals to Agreement Start date ("+agmt_start_dt+") !");
			document.forms[0].start_dt.value="";
			return false;
		}
		if(temp_agmt_end_dt < temp_st_dt)
		{
			alert("Contract Start date ("+start_dt+") should be less than Agreement End date ("+agmt_end_dt+") !");
			document.forms[0].start_dt.value="";
			return false;
		}
	}
}

function checkAgmtEnddate()
{
	var end_dt = document.forms[0].end_dt.value;
	var agmt_end_dt = document.forms[0].agmt_end_dt.value;
	var agmt_start_dt = document.forms[0].agmt_start_dt.value;
	
	var splitEnd_dt = end_dt.split("/");
	var splitAgmt_end_dt = agmt_end_dt.split("/");
	var splitAgmtSt_dt = agmt_start_dt.split("/");
	
	var temp_end_dt = splitEnd_dt[2]+splitEnd_dt[1]+splitEnd_dt[0];
	var temp_agmt_end_dt = splitAgmt_end_dt[2]+splitAgmt_end_dt[1]+splitAgmt_end_dt[0];
	var temp_agmt_st_dt = splitAgmtSt_dt[2]+splitAgmtSt_dt[1]+splitAgmtSt_dt[0];
	
	if(end_dt!="")
	{
		if(temp_end_dt > temp_agmt_end_dt)
		{
			alert("Contract End date ("+end_dt+") should be less than or equals to Agreement End date ("+agmt_end_dt+") !");
			document.forms[0].end_dt.value="";
			return false;
		}
		
		if(temp_end_dt < temp_agmt_st_dt)
		{
			alert("Contract End date ("+end_dt+") should be grater than Agreement Start date ("+agmt_start_dt+") !");
			document.forms[0].end_dt.value="";
			return false;
		}
	}
}

function checkSigningDdaDate()
{
	var signing_dt = document.forms[0].signing_dt.value;
	var agmt_signing_dt = document.forms[0].agmt_signing_dt.value;
	var dda_dt = document.forms[0].dda_dt.value;
	var agmt_end_dt = document.forms[0].agmt_end_dt.value;
	
	var splitSign_dt = signing_dt.split("/");
	var splitAgmtSign_dt = agmt_signing_dt.split("/");
	var splitDda_dt = dda_dt.split("/");
	var splitAgmt_end_dt = agmt_end_dt.split("/");
	
	var tmp_sig_dt = splitSign_dt[2]+splitSign_dt[1]+splitSign_dt[0];
	var tmp_agmt_sig_dt = splitAgmtSign_dt[2]+splitAgmtSign_dt[1]+splitAgmtSign_dt[0];
	var tmp_dda_dt = splitDda_dt[2]+splitDda_dt[1]+splitDda_dt[0];
	var temp_agmt_end_dt = splitAgmt_end_dt[2]+splitAgmt_end_dt[1]+splitAgmt_end_dt[0];
	
	if (agmt_signing_dt == "")
	{
		alert("First Select the Agreement and then Proceed !");
	}
	else 
	{		
		if(signing_dt!="")
		{
			if(tmp_sig_dt < tmp_agmt_sig_dt)
			{
				alert("Contract Date ("+signing_dt+") should be Greater than or equals to Agreement Contract/Trade Date ("+agmt_signing_dt+") !");
				document.forms[0].signing_dt.value="";
				
				return false;
			}
			
			if(temp_agmt_end_dt < tmp_sig_dt)
			{
				alert("Contract Date ("+signing_dt+") should be Less than Agreement End date ("+agmt_end_dt+") !");
				document.forms[0].signing_dt.value="";
				
				return false;
			}
		}
		
		if(dda_dt!="")
		{
			if(tmp_dda_dt < tmp_agmt_sig_dt)
			{
				alert("Contract DDA date ("+dda_dt+") should be Greater than or equals to Agreement Contract/Trade Date ("+agmt_signing_dt+") !");
				document.forms[0].dda_dt.value="";
				
				return false;
			}
			
			if(temp_agmt_end_dt < tmp_dda_dt)
			{
				alert("Contract DDA date ("+dda_dt+") should be Less than Agreement End date ("+agmt_end_dt+") !");
				document.forms[0].dda_dt.value="";
				
				return false;
			}
		}
	}	
}

function adjustTimeHour()
{
	var allowedLayTimeHrs = document.getElementById("allowed_lay_time_hrs").value;
	
	if (isNaN(allowedLayTimeHrs) )
	{
        alert("Provided Value for Hours is not a number!!");
        document.getElementById("allowed_lay_time_hrs").value = "";
    }
}

function adjustTimeMin()
{
	var allowedLayTimeHrs = document.getElementById("allowed_lay_time_hrs").value;
	var allowedLayTimeMin = document.getElementById("allowed_lay_time_min").value;
	
	if (isNaN(allowedLayTimeMin) )
	{
        alert("Provided Value for Minutes is not a number!!");
        document.getElementById("allowed_lay_time_min").value = "";
    }
	
	if (allowedLayTimeMin <= -1)
	{
		alert("Please enter non-negative values for allowed lay time (hours and minutes).");
	    return;
	}
	
	if (allowedLayTimeMin >= 60)
	{
		alert("Minute Range must be in the Range of 0 to 59");
		document.getElementById("allowed_lay_time_min").value = "";
	}
}

function openSalesContList()
{
	var customer_cd=document.forms[0].counterparty_cd.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var entry_point = document.forms[0].entry_point.value;
	var exit_point = document.forms[0].exit_point.value;
	
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	
	var msg="";
	var flag=true;
	
	var bu_unit="";
	var chkFlg=false;
	
	if(trim(start_dt) == "")
	{
		msg+="Enter Start Date!\n";
		flag=false;
	}
	if(trim(end_dt) == "")
	{
		msg+="Enter End Date!\n";
		flag=false;
	}
	if(trim(entry_point) == "")
	{
		msg+="Select Entry Point!\n";
		flag=false;
	}
	if(trim(exit_point) == "")
	{
		msg+="Select Exit Point!\n";
		flag=false;
	}
	
	if(chk_bu_plant!=null && chk_bu_plant!=undefined)
	{
		if(chk_bu_plant.length!=undefined)
		{
			for(var i=0;i<chk_bu_plant.length;i++)
			{
				if(chk_bu_plant[i].checked)
				{
					bu_unit=chk_bu_plant[i].value;	
					chkFlg=true;
				}
			}
		}
		else
		{
			if(chk_bu_plant.checked)
			{
				bu_unit=chk_bu_plant.value;	
				chkFlg=true;
			}
		}
	}
	
	if(chkFlg==false)
	{
		msg += "Please Select Atleast One Business Unit!\n";
		flag=false;
	}	
	
	var url ="frm_sales_contract_list.jsp?customer_cd="+customer_cd+"&start_dt="+start_dt+"&end_dt="+end_dt+
			"&entry_point="+entry_point+"&exit_point="+exit_point+"&bu_unit="+bu_unit;
	
	if(flag)
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Sales Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Sales Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
	else
	{
		alert(msg);
	}
}

function setSalesContDetail(customer_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,cont_type,dealNo,contRef)
{
	var sales_cont_nm=dealNo+" ("+contRef+")";
	var sales_cont_map=cont_type+"-"+agmt_no+"-"+agmt_rev_no+"-"+cont_no+"-"+cont_rev_no;
		
	document.forms[0].customer_cd.value=customer_cd;
	document.forms[0].sales_cont_nm.value=sales_cont_nm;
	
	document.forms[0].sales_cont_map.value=sales_cont_map;
}

function handleComma(input,field_nm) 
{
	if (input.value.includes(',')) 
    {
      alert("Comma(,) not allowed for "+field_nm);
      input.value = input.value.replace(/,/g, '');
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_svc_cont_master" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String dateTime = utildate.getSysdateWithTime24hr();
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String agmt_nm = request.getParameter("agmt_nm")==null?"":request.getParameter("agmt_nm");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");

dlng.setCallFlag("SO_SVC_CONT");
dlng.setCounterparty_cd(counterparty_cd);
dlng.setComp_cd(owner_cd);
dlng.setOpration(opration);
dlng.setAgmt_no(agmt_no);
dlng.setAgreement_type("T");
dlng.setAgmt_rev_no(agmt_rev_no);
dlng.setCont_no(cont_no);
dlng.setCont_rev_no(cont_rev_no);
dlng.setContract_type(contract_type);
dlng.init();

Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();

Vector VPLANT_NM = dlng.getVPLANT_NM();
Vector VPLANT_ABBR = dlng.getVPLANT_ABBR();
Vector VPLANT_SEQ_NO = dlng.getVPLANT_SEQ_NO();
Vector VBU_CD = dlng.getVBU_CD();
Vector VBU_PLANT_NM = dlng.getVBU_PLANT_NM();
Vector VBU_PLANT_ABBR = dlng.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = dlng.getVBU_PLANT_SEQ_NO();

Vector VSEL_PLANT_SEQ_NO = dlng.getVSEL_PLANT_SEQ_NO();
Vector VSEL_BU_CD = dlng.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = dlng.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_PLANT_ABBR = dlng.getVSEL_PLANT_ABBR();
Vector VSEL_BU_PLANT_ABBR = dlng.getVSEL_BU_PLANT_ABBR();

Vector VFILL_STATION_CD = dlng.getVFILL_STATION_CD();
Vector VFILL_STATION_NM = dlng.getVFILL_STATION_NM();
Vector VFILL_STATION_ABBR = dlng.getVFILL_STATION_ABBR();

String min_counterparty_eff_dt = dlng.getMin_counterparty_eff_dt();

//String cont_no = dlng.getCont_no();
//String cont_rev_no = dlng.getCont_rev_no();
String cont_ref_no = dlng.getCont_ref_no();
String signing_dt = dlng.getSigning_dt();
String signing_time = dlng.getSigning_time();
String dda_dt = dlng.getDda_dt();
String dda_time = dlng.getDda_time();
String ent_dt = dlng.getEnt_dt();
String ent_time = dlng.getEnt_time();
String start_dt = dlng.getStart_dt();
String end_dt = dlng.getEnd_dt();
String day_def_flag = dlng.getDay_def_flag();
String day_start_time = dlng.getDay_start_time();
String day_end_time = dlng.getDay_end_time();
String mmcq_flag = dlng.getMmcq_flag();
String mmcq_percentage = dlng.getMmcq_percentage();
String cont_name = dlng.getCont_name();
String dealMapping = dlng.getDealMapping();
String contpty_abbr = dlng.getContpty_abbr();
String fcc_flg = dlng.getFcc_flg();
String cont_status_flg = dlng.getCont_status_flg();
String cont_status = dlng.getCont_status();
String is_allocated = dlng.getIs_allocated();
String no_of_billing_dtl=dlng.getNo_of_billing_dtl();
String no_of_security_dtl=dlng.getNo_of_security_dtl();
String contdt_change_request_flag = dlng.getContdt_change_request_flag();
String agmt_signing_dt = dlng.getAgmt_signing_dt();
String agmt_start_dt = dlng.getAgmt_start_dt();
String agmt_end_dt = dlng.getAgmt_end_dt();

String day_def_clause = dlng.getDay_def_clause();
String mmcq_clause = dlng.getMmcq_clause();
String dcq = dlng.getDcq();
String fill_station_cd = dlng.getFill_station_cd();
String plant_seq_no = dlng.getPlant_seq_no();
String alw_laytime_hrs = dlng.getAlw_laytime_hrs();
String alw_laytime_min = dlng.getAlw_laytime_min();
String layover_charge_inr = dlng.getLayover_charge_inr();
String layover_hrs = dlng.getLayover_hrs();
String transport_mgmt_charge = dlng.getTransport_mgmt_charge();
String transport_mgmt_unit = dlng.getTransport_mgmt_unit();
String transport_mgmt_eff_dt = dlng.getTransport_mgmt_eff_dt();
String qty_opt = dlng.getQty_opt();
String qty_opt_firm = dlng.getQty_opt_firm();
String qty_opt_re = dlng.getQty_opt_re();

String customer_cd=dlng.getCustomer_cd();
String sales_cont_nm=dlng.getSales_cont_nm();
String sales_cont_map=dlng.getSales_cont_map();

if(customer_cd.equals(""))
{
	customer_cd="0";
}
if(cont_status_flg.equals("")){
	cont_status_flg="F";
	cont_status="New";
}

if(signing_time.equals("")){
	signing_time="00:00";
}
if(dda_time.equals("")){
	dda_time="00:00";
}
if(day_start_time.equals("")){
	day_start_time="06:00";
}
if(day_end_time.equals("")){
	day_end_time="06:00";
}
if(mmcq_percentage.equals(""))
{
	mmcq_percentage="100";
}

if(ent_dt.equals(""))
{
	String split[] = dateTime.split(" ");
	ent_dt = split[0];
	ent_time = split[1];
}

String bill_flag = dlng.getBill_flag();
String billing_clause = dlng.getBilling_clause();

String strPlant="";
String strBuPlant="";

for(int i=0;i<VSEL_PLANT_SEQ_NO.size();i++)
{
	strPlant = strPlant + VSEL_PLANT_SEQ_NO.elementAt(i)+"@";
}
for(int i=0;i<VSEL_BU_PLANT_SEQ_NO.size();i++)
{
	strBuPlant = strBuPlant + VSEL_BU_PLANT_SEQ_NO.elementAt(i)+"@";
}

//<!--Harsh Maheta 20230901 : Added for old values to show in Deal audit history-->//
String cp_name = dlng.getContpty_nm();
String cp_abbr = dlng.getContpty_abbr();
String mapped_cont_no = dlng.getMapped_cont_no();

String old_value="CP="+counterparty_cd+"#NAME="+cp_name+"#ABBR="+cp_abbr+"#CONTNAME="+cont_name+"#CONTNO="+mapped_cont_no+"#CONTREFNO="+cont_ref_no+"#CONTTYPE="+contract_type+"#DDADT="+dda_dt+"#DDATIME="+dda_time+"#SIGNDT="+signing_dt+"#SIGNTIME="+signing_time+
"#ENTDT="+ent_dt+"#ENTTIME="+ent_time+"#STARTDT="+start_dt+"#ENDDT="+end_dt+"#MDCQ="+mmcq_percentage+"#CONT_STATUS="+cont_status;

%>
<%-- <body onload="setValues('<%=strTransCd%>','<%=strTrans%>','<%=strPlant%>','<%=strBuPlant%>','<%=strTransChrg%>','<%=strGxBuPlant%>','<%=strMktMrgin%>','<%=strOthChrg%>');liabilityShows();billingShows();messurmentShows();off_spec_gasShows();terminatorShows();"> --%>
<body onload="billingShows();setValues('<%=strBuPlant%>');">
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_DLNG_svc_cont_master">

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
					    	Service Order / Term Sheet
					    </div>
					    <div class="btn-group">
						  <label class="btn btn-outline-secondary btngrp <%if(opration.equals("INSERT")){%>btnactive<%}%>" onclick="refresh('INSERT');"><i class="fa fa-plus-circle"></i>&nbsp;New</label>
						  <label class="btn btn-outline-secondary btngrp <%if(opration.equals("MODIFY")){%>btnactive<%}%>" onclick="refresh('MODIFY');"><i class="fa fa-edit"></i>&nbsp;Modify</label>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					&nbsp;
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Customer<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=opration%>');">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
									<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_no" value="<%=cont_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_rev_no" value="<%=cont_rev_no%>" maxLength="2" readOnly>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Contract Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">
							<%if(contract_type.equals("B") && opration.equals("INSERT")){ %>
								<div class="col">
				    		<%}else{ %>
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    		<%} %>
				    				<select class="form-select form-select-sm" name="contract_type" onchange="refresh('<%=opration%>');" <%if(opration.equals("MODIFY") && (!cont_no.equals("0") && !cont_no.equals(""))){ %>style="pointer-events: none;"<%} %>>
				    					<option value="">--Select--</option>
				    					<option value="B">Service Order</option>
				    					<option value="M">Term Sheet</option>
				    				</select>
				    				<script>document.forms[0].contract_type.value="<%=contract_type%>"</script>
				      			</div>
				      			<%if(contract_type.equals("B") && opration.equals("INSERT")){ %>
								<div class="col-auto">
				    				<input type="button" class="btn btn-info btn-sm select_btn" value="Select Agreement" style="font-weight: bold;" onclick="openAgreementList();" <%if(opration.equals("MODIFY")){ %>disabled<%} %>>
				    			</div>
				    			<div class="col">
				    				<input type="text" class="form-control form-control-sm" name="agmt_nm" value="<%=agmt_nm%>" readOnly>
				    			</div>
								<%} %>
							</div>
						</div>
					</div>
					<%if(opration.equalsIgnoreCase("MODIFY")){ %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    			<!-- <label class="form-label"><b>Contract No<span class="s-red">*</span></b></label> -->
				    			<input type="button" class="btn btn-info btn-sm select_btn" value="Select Contract" onclick="openContList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-4 col-xs-4 col-md-4"">
				    				<input type="text" class="form-control form-control-sm" name="" value="<%=dealMapping%>" maxLength="50" readOnly>
				    			</div>
				    			<div class="col-sm-8 col-xs-8 col-md-8">
				    				<input type="text" class="form-control form-control-sm" name="cont_name" value="<%=cont_name %>" maxLength="50" readOnly>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Status</b></label>

				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label"><b><%=cont_status%></b></label>
				    				<input type="hidden" name="cont_status" value="<%=cont_status%>">
				      			</div>
				  			</div>
						</div>
					</div>
					<%} %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Contract Ref#<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="cont_ref_no" value="<%=cont_ref_no%>" maxLength="25" oninput="handleComma(this,'Contract Ref#')">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Deal Enter Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date" name="ent_dt" value="<%=ent_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" readOnly>
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="ent_time" value="<%=ent_time%>" maxLength="5" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off" readOnly>
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>DDA Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="dda_dt" value="<%=dda_dt%>" maxLength="10" 
			      						onblur="validateDate(this);<%if(contract_type.equals("B")){ %>checkSigningDdaDate();<%} %>" onchange="validateDate(this);<%if(contract_type.equals("B")){ %>checkSigningDdaDate();<%} %>" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="dda_time" value="<%=dda_time%>" maxLength="5" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Contract Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="signing_dt" value="<%=signing_dt%>" maxLength="10" 
			      						onblur="validateDate(this);checkSigningStartDt(document.forms[0].start_dt, this.value);<%if(contract_type.equals("B")){ %>checkSigningDdaDate();<%} %>" 
			      						onchange="validateDate(this);checkSigningStartDt(document.forms[0].start_dt,this.value);<%if(contract_type.equals("B")){ %>checkSigningDdaDate();<%} %>" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<input type="hidden" name="agmt_signing_dt" value="<%=agmt_signing_dt%>">
				    			</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="signing_time" value="<%=signing_time%>" maxLength="5" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Start Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
										<input type="text"
											class="form-control form-control-sm date fmsdtpick"
											name="start_dt" value="<%=start_dt%>" maxLength="10"
											onblur="validateDate(this);checkEffDtWithContStDt(this,'<%=min_counterparty_eff_dt%>');checkStartEndDate(this,document.forms[0].end_dt,'F');checkSigningStartDt(this,document.forms[0].signing_dt.value);"
											onchange="validateDate(this);checkEffDtWithContStDt(this,'<%=min_counterparty_eff_dt%>');checkStartEndDate(this,document.forms[0].end_dt,'F');checkSigningStartDt(this,document.forms[0].signing_dt.value);<%if(contract_type.equals("B")){ %>checkAgmtStartdate();<%} %>"
											autocomplete="off"
											<%if((!cont_status_flg.equals("F")&& !contdt_change_request_flag.equals("Y"))) {%>
											readOnly style="pointer-events: none;" <%} %>> <span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<input type="hidden" name="agmt_start_dt" value="<%=agmt_start_dt%>">
				    			</div>
				    			<div class="col">
				    				<input type="button" class="btn btn-sm modify_btn" value="Modify Contract Duration" onclick="doContractDurationChangeRequest();" <%if(!fcc_flg.equals("Y")){%>disabled<%}%>>
				    			</div>
				    			<input type="hidden" name="temp_start_dt" value="<%=start_dt%>">
				  			</div>
				  			
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>End Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="end_dt" value="<%=end_dt%>" maxLength="10" 
			      						onblur="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');" 
			      						onchange="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');<%if(contract_type.equals("B")){ %>checkAgmtEnddate();<%} %>" autocomplete="off"
			      						<%if(!cont_status_flg.equals("F") && !contdt_change_request_flag.equals("Y")) { %>readOnly style="pointer-events: none;"<%} %>>
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<input type="hidden" name="agmt_end_dt" value="<%=agmt_end_dt%>">
				    			</div>
				    			<input type="hidden" name="temp_end_dt" value="<%=end_dt%>">
				  			</div>
						</div>
					</div>
					<div class="row m-b-5" <%if(!opration.equals("MODIFY")){ %>style="display:none;"<%} %>>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Security<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<select class="form-select form-select-sm" name="sec_category" id="sec_category">
										<option value="R">Incoming</option>
										<option value="I">Outgoing</option>
									</select>
								</div>
				    			<div class="col">
				    				<input type="button" name="security_btn" class="btn btn-sm config_btn" value="Security Config" onclick="securityPreReceipt('<%=contract_type%>','<%=agmt_no%>','<%=agmt_rev_no%>','<%=cont_no%>','<%=cont_rev_no%>');">
				  					<input type="hidden" name="securityFlag" value="N">
				  				</div>
				  				<!-- <script>document.forms[0].sec_category.style.pointerEvents = "none";</script>
				  				<script>document.forms[0].security_btn.style.pointerEvents = "none";</script> -->
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Transport Qty<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<input type="text" class="form-control form-control-sm" name="transport_qty" value="<%=dcq %>" onblur="checkNumber1(this,12,2);">
				    			</div>
				    			<div class="col-auto">
									<select class="form-select form-select-sm" name="transport_qty_unit" style="pointer-events: none;">
				    					<option value="1">MMBTU</option>
				    				</select>
				    			</div>	
				    			<%-- <%if(opration.equals("MODIFY")){ %>
				    			<div class="col-auto">
				    				<input type="button" class="btn btn-sm config_btn" value="Variable Transport Qty Config">
				    			</div>			    		
				    			<%} %> --%>	
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Business Unit<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<%if(VBU_CD.size() > 0) {%>
					    				<%for(int i=0; i<VBU_CD.size(); i++){ %>
					    					<input type="checkbox" class="form-check-input" name="chk_bu_plant" value="<%=VBU_PLANT_SEQ_NO.elementAt(i)%>">&nbsp;<%=VBU_PLANT_ABBR.elementAt(i)%>&nbsp;&nbsp;
					    				<%}%>
				    				<%}else{ %>
				    					<%= utilmsg.warningMessage("Please configure Business Plants!")%>
				    				<%} %>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Filling Station / Entry Point<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="entry_point" >
				    					<option value="">--Select--</option>
										<%for(int i=0;i<VFILL_STATION_CD.size();i++){ %>
										<option value="<%=VFILL_STATION_CD.elementAt(i)%>"><%=VFILL_STATION_NM.elementAt(i)%></option>
										<%} %>
				    				</select>
				    				<script>document.forms[0].entry_point.value="<%=fill_station_cd%>"</script>
				    			</div>
				    		</div>
				    	</div>
				    </div>
				    <div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Customer Plant / Exit Point<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="exit_point" >
				    					<option value="">--Select--</option>
										<%for(int i=0;i<VPLANT_SEQ_NO.size();i++){ %>
										<option value="<%=VPLANT_SEQ_NO.elementAt(i)%>"><%=VPLANT_ABBR.elementAt(i)%></option>
										<%} %>
				    				</select>
				    				<script>document.forms[0].exit_point.value="<%=plant_seq_no%>"</script>
				    			</div>
				    		</div>
				    	</div>
					</div>
					
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Governing Clauses</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Allowed Lay Time<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
				  			<div class="form-group row">
			    				<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="allowed_lay_time_hrs" id="allowed_lay_time_hrs"  value="<%=alw_laytime_hrs %>" placeholder="Hours" maxLength="5"  autocomplete="off" onchange="adjustTimeHour()">
			      						<span class="input-group-text">Hour</span>
		      						</div>
				    			</div>
				    			<div class="col">
						      		<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="allowed_lay_time_min" id="allowed_lay_time_min"  value="<%=alw_laytime_min %>" placeholder="Minutes" maxLength="5"  autocomplete="off" onchange="adjustTimeMin()">
			      						<span class="input-group-text">Min</span>
		      						</div>
		      					</div>
		      				</div>
		    			</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Layover Charges</b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
				  			<div class="form-group row">
			    				<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="layover_charge" value="<%=layover_charge_inr %>" onkeyup="checkForNumber(this);" onblur="checkNumber1(this,7,2);">
			      						<span class="input-group-text">INR</span>
		      						</div>
				    			</div>
				    			<div class="col-1" align="center">
				    				<label class="form-label"><b>Per</b></label>
				    			</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="layover_hours" id="layover_hours"  value="<%=layover_hrs %>" placeholder="Hours" maxLength="5"  autocomplete="off" onchange="adjustTimeHour()">
			      						<span class="input-group-text">Hour</span>
		      						</div>
				    			</div>
				    		</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="day_def" value="Y" <%if(day_def_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Day Definition</b></label>
				    			</div>
				    			<div class="col">
				    				<input type="text" class="form-control form-control-sm" name="day_def_clause_no" value="<%=day_def_clause %>" placeholder="Clause#">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
				  			<div class="form-group row">
			    				<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="day_time_from" value="<%=day_start_time%>" maxLength="10" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col-1" align="center">
				    				<label class="form-label"><b>To</b></label>
				    			</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="day_time_to" value="<%=day_end_time%>" maxLength="10" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				    		</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="mmcq_flag" value="Y" <%if(mmcq_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;MMCQ</b></label>
				  				</div>
				    			<div class="col">
				    				<input type="text" class="form-control form-control-sm" name="mmcq_clause_no" value="<%=mmcq_clause %>" placeholder="Clause#">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-auto">
				    				<input type="text" class="form-control form-control-sm" name="mmcq" value="<%=mmcq_percentage%>" onkeyup="checkForNumber(this);" onblur="checkNumber1(this,5,2);checkMmcq();">
				      			</div>
				  			</div>
						</div>
					</div>
					<%-- <div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="liability_checkbox" id="liability_checkbox" onchange="liabilityShows()" value="Y" <%if(liability_flag.equals("Y")){ %>checked<%} %>>&nbsp;Liability</b></label>
				  				</div>
				    			<div id="liability_div1" class="col">
				    				<input type="text" class="form-control form-control-sm" name="liability_clause" value="<%=liability_clause %>" placeholder="Clause#" maxLength="10">
				      			</div>
			      			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div id="liability_div2" class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="button" class="btn btn-sm config_btn" value="Configure Liability" <%if(opration.equals("MODIFY")){ %>onclick="openLiabilityClause();"<%} else {%> disabled <%} %>>&nbsp;
				    				<%if(opration.equals("MODIFY")){ %>
				    				<input type="checkbox" class="form-check-input" name="liab_lq_damg" value="Y" <%if(liability_lq_dmg.equals("Y")){ %>checked<%} %> style="pointer-events: none;">&nbsp;Liquidated Damages&nbsp;&nbsp;  
				    				<input type="checkbox" class="form-check-input" name="liab_take_pay" value="Y" <%if(liability_take_pay.equals("Y")){ %>checked<%} %> style="pointer-events: none;">&nbsp;Take or Pay&nbsp;&nbsp;
				    				<input type="checkbox" class="form-check-input" name="liab_makeup" value="Y" <%if(liability_makeup.equals("Y")){ %>checked<%} %> style="pointer-events: none;">&nbsp;Make Up Gas   
				    				<%} %>
				    			</div>
				  			</div>
						</div>
					</div> --%>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="billing_flag" id="billing_flag" value="Y" onchange="billingShows()" <%if(bill_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Billing</b></label>
				  				</div>
				  				<div id="billing_flag_div" class="col">
				    				<input type="text" class="form-control form-control-sm" name="billing_clause_no" value="<%=billing_clause%>" placeholder="Clause#" maxLength="10">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div id="billing_flag_div1" class="col-sm-12 col-xs-12 col-md-12">
						      		<input type="button" class="btn btn-sm config_btn" value="Configure Billing" <%if(opration.equals("MODIFY")){ %>onclick="openBillingDtl();"<%} else {%> disabled <%} %>>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Rate Components</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Transport Management Charge<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
								    <input id="prov_svc_rate" type="text" class="form-control form-control-sm" name="transport_management_charge" value="<%=transport_mgmt_charge %>" onblur="negNumber(this);" maxlength="6">
								</div>
								<div class="col">
								    <select id="prov_svc_rate_unit1" class="form-select form-select-sm" name="transport_management_charge_unit" onchange="checkProvRateFormat()">
								        <option value="1">INR/MMBTU</option>
								        <!-- <option value="2">USD/MMBTU</option>
								        <option value="4">INR/MT</option>
								        <option value="5">INR/KM</option> -->
								        <option value="3">INR/Truck</option><!-- LumpSum -->
								    </select>
								    <script>document.forms[0].transport_management_charge_unit.value = "<%=transport_mgmt_unit%>";</script>
								</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Effective date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="transport_management_charge_eff_dt" value="<%=transport_mgmt_eff_dt %>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Sales Contract Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
			    					<input type="button" name="sel_sales_cont" id="sel_sales_cont" class="btn btn-sm config_btn" onclick="openSalesContList();" value="Link Sales Contract" style="font-weight: bold;">
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<textarea class="form-control form-control-sm" name="sales_cont_nm" readOnly <%if(!sales_cont_nm.equals("")){ %>style="background:#99ffcc;"<%} %> rows="<%=sales_cont_nm.split(", ").length<=0?1:sales_cont_nm.split(", ").length%>"><%=sales_cont_nm.replace(", ","\n")%></textarea>
				      				<%-- <input type="text" class="form-control form-control-sm" readOnly name="sales_cont_nm" value="<%=sales_cont_nm %>"> --%>
				    				<input type="hidden" name="sales_cont_map" value="<%=sales_cont_map%>">
				    				<input type="hidden" name="customer_cd" value="<%=customer_cd%>">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Quantity Option<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
								    <input type="radio" name="qty_opt" value="1" <%if(qty_opt.equals("1")){%>checked<%}%>>&nbsp;No. Of Trucks&nbsp;&nbsp;
				      				<input type="radio" name="qty_opt" value="2" <%if(qty_opt.equals("2")){%>checked<%}%>>&nbsp;Total Quantity (MMBTU)&nbsp;&nbsp;
								</div>
								<div class="col">
								    <div class="form-group row">
					    				<div class="col">
						      				<div class="input-group input-group-sm" >
						      					<span class="input-group-text">Firm</span>
					      						<input type="text" class="form-control form-control-sm" name="qty_opt_frim" placeholder="Firm" value="<%=qty_opt_firm %>" onkeyup="checkForNumber(this);" onblur="checkNumber1(this,5,2);">
				      						</div>
						    			</div>
						    			<div class="col">
						      				<div class="input-group input-group-sm" >
						      					<span class="input-group-text">RE</span>
					      						<input type="text" class="form-control form-control-sm" name="qty_opt_re" placeholder="RE" value="<%=qty_opt_re %>" onkeyup="checkForNumber(this);" onblur="checkNumber1(this,5,2);">
				      						</div>
						    			</div>
						    		</div>
								</div>
				  			</div>
						</div>
						
					</div>
					
					<div <%if(opration.equals("MODIFY") && (!cont_no.equals("0") && !cont_no.equals(""))){ %><%}else{ %>style="display:none;"<%} %>>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<%if((!no_of_security_dtl.equals("0") && !no_of_security_dtl.equals("")) && (!no_of_billing_dtl.equals("0") && !no_of_billing_dtl.equals(""))){ %>
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Deal Checks & Control</label>
						</div>
						<div class="row m-b-5"  style="pointer-events: auto;">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Deal Checks & Control</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
									<div class="col-auto">
									<%if(approve_access.equals("Y")){ %>
										<input type="radio" name="ff" onclick="fcc('Y');" <%if(fcc_flg.equals("Y")){ %>checked<%} %>>&nbsp;Yes&nbsp;&nbsp;
										<input type="radio" name="ff" onclick="fcc('N');" <%if(fcc_flg.equals("N")){ %>checked<%} %>>&nbsp;No
									<%}else{ %>
										<input type="radio" name="ff" onclick="fcc('Y');" <%if(fcc_flg.equals("Y")){ %>checked<%} %> disabled>&nbsp;Yes&nbsp;&nbsp;
										<input type="radio" name="ff" onclick="fcc('N');" <%if(fcc_flg.equals("N")){ %>checked<%} %> disabled>&nbsp;No
									<%} %>
										<input type="hidden" name="fcc_flg" value="<%=fcc_flg%>">
										<input type="hidden" name="temp_fcc_flg" value="<%=fcc_flg%>">
									</div>
					    			<div class="col">
					    				<%if(fcc_flg.equals("Y")){ %>
					    					<label class="form-label" style="color:green"><b>Approved</b></label>
					    				<%}else if(fcc_flg.equals("N")){ %>
					    				 	<label class="form-label" style="color:red"><b>Disapproved</b></label>
					    				<%}else{ %>
					    					<label class="form-label" style="color:blue"><b>Pending</b></label>
					    				<%} %>
					  				</div>
					  			</div>
							</div>
						</div>
						<%} %>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
				</div>
			</div>	
		</div>
	</div>
	&nbsp;
	<%if(!cont_no.equals("0") && !cont_no.equals("")){ %>
		<%if((no_of_billing_dtl.equals("0") || no_of_billing_dtl.equals("")) || (no_of_security_dtl.equals("0") || no_of_security_dtl.equals("")) || contdt_change_request_flag.equals("Y")){ %>
			<div class="row">
				<div class="col-md-12 col-sm-12 col-xs-12">
					<div class="card cardmain">
						<div class="card-body cdbody">
							<%if(!cont_no.equals("0") && !cont_no.equals("")){ %>
								<%if(!no_of_billing_dtl.equals("0") && Integer.parseInt(no_of_billing_dtl)<Integer.parseInt(""+VSEL_PLANT_SEQ_NO.size())) {%>
									<div class="row m-b-5">
										<div class="col-sm-12 col-xs-12 col-md-12">
											<%=utilmsg.errorMessage("<b>Billing Details Not configure for all Plants. Invoice will not appear for Plant/s missing billing detail!</b>")%>
										</div>
									</div>
								<%} %>
								<%if(no_of_billing_dtl.equals("0") || no_of_billing_dtl.equals("")){ %>
									<div class="row m-b-5">
										<div class="col-sm-12 col-xs-12 col-md-12">
											<%=utilmsg.errorMessage("<b>Billing Detail is not Configured. The Contract will not appear for FCC!</b>")%>
										</div>
									</div>
								<%} %>
								<%if((no_of_security_dtl.equals("0") || no_of_security_dtl.equals(""))){ %>
									<div class="row m-b-5">
										<div class="col-sm-12 col-xs-12 col-md-12">
											<%=utilmsg.errorMessage("<b>Security Detail is not Configured. The Contract will not appear for FCC!</b>")%>
										</div>
									</div>
								<%} %>
								<%if(contdt_change_request_flag.equals("Y")){ %>
									<div class="row m-b-5">
										<div class="col-sm-12 col-xs-12 col-md-12">
											<%=utilmsg.errorMessage("<b>Contract Duration Modification pending for the Contract!. The Contract will not appear for FCC!</b>")%>
										</div>
									</div>
								<%} %>
							<%} %>
						</div>
					</div>
				</div>
			</div>
		<%}%>
	<%} %>
</div>

<input type="hidden" name="option" value="SO_SVC_CONT">
<input type="hidden" name="old_value" value="<%=old_value%>">

<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="cont_status_flg" value="<%=cont_status_flg%>">
<input type="hidden" name="is_allocated" value="<%=is_allocated%>">

<input type="hidden" name="no_of_billing_dtl" value="<%=no_of_billing_dtl%>">
<input type="hidden" name="no_of_security_dtl" value="<%=no_of_security_dtl%>">

<input type="hidden" name="change_request" value="">

<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="contdt_change_request_flag" value="<%=contdt_change_request_flag%>">


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