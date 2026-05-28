<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh(opration,clearance)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var prev_clearance = document.forms[0].prev_clearance.value;
	
	if(prev_clearance != clearance)
	{
		counterparty_cd="0";
	}
		
	var u = document.forms[0].u.value;
	
	var url = "frm_agmt_mst.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+"&u="+u+"&clearance="+clearance;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
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
	
	var signing_dt = document.forms[0].signing_dt.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	
	var agreement_type = document.forms[0].agreement_type;
	var agreement_base = document.forms[0].agreement_base;
	
	var chk_plant = document.forms[0].chk_plant;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	
	var msg="";
	var flag=true;
	
	if(counterparty_cd == "" || counterparty_cd == "0")
	{
		msg+="Select Counterparty!\n";
		flag=false;
	}
	if(opration=="MODIFY")
	{
		var agmt_no = document.forms[0].agmt_no.value;
		if(agmt_no == "" || agmt_no == "0")
		{
			msg+="Select Agreement!\n";
			flag=false;
		}
	}
	if(trim(signing_dt) == "")
	{
		msg+="Enter Contract/Trade Date!\n";
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
	
	if(agreement_type[0].checked == false && agreement_type[1].checked == false)
	{
		msg+="Select Agreement Type!\n";
		flag=false;
	}
	if(agreement_base[0].checked == false && agreement_base[1].checked == false && agreement_base[2].checked == false)
	{
		msg+="Select Agreement Base!\n";
		flag=false;
	}
	
	if(opration=="MODIFY")
	{
		if(document.forms[0].rev_chk.checked)
		{
			if(trim(document.forms[0].rev_eff_dt.value)=="")
			{
				msg+="Enter Effective Date for New Revision!\n";
				flag=false;
			}
		}
	}
	
	//FOR CUSTOMER PLANT
	var chkFlg = false;
	if(chk_plant!=null && chk_plant!=undefined)
	{
		if(chk_plant.length!=undefined)
		{
			for(var i=0;i<chk_plant.length;i++)
			{
				if(chk_plant[i].checked)
				{
					chkFlg = true;	
				}
			}
		}
		else
		{
			if(chk_plant.checked)
			{
				chkFlg = true;	
			}
		}
	}
	if(chkFlg==false)
	{
		msg += "Please Select Atleast One Customer-Plant!\n";
		flag=false;
	}
	
	//FOR TRANSPORTER
	chkFlg = false;
	if(chk_trans!=null && chk_trans!=undefined)
	{
		if(chk_trans.length!=undefined)
		{
			for(var i=0;i<chk_trans.length;i++)
			{
				if(chk_trans[i].checked)
				{
					chkFlg = true;	
				}
			}
		}
		else
		{
			if(chk_trans.checked)
			{
				chkFlg = true;	
			}
		}
	}
	if(chkFlg==false)
	{
		msg += "Please Select Atleast One Transporter!\n";
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
		if(opration=="MODIFY")
		{
			if(document.forms[0].rev_chk.checked)
			{
				a = confirm("On saving your modification/s, this Contract will be Revised!\n\nDo you want to Proceed?");
			}
			else
			{ 
				a = confirm("Do you want to Modify Agreement?");
			}
		}
		else
		{
			a = confirm("Do you want to Create New Agreement?");
		}
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
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_type = document.forms[0].agmt_type.value;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agmt_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agmt_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function openBillingDtl()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var clearance = document.forms[0].clearance.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var agmt_type = document.forms[0].agmt_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_agmt_billing_dtl.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agmt_type+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+"&start_dt="+start_dt+
			"&u="+u+"&end_dt="+end_dt;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Agreement Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Agreement Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setContDetail(countpty_cd,agmt_no,agmt_rev_no,agmt_type)
{
	var opration = document.forms[0].opration.value;
	var clearance = document.forms[0].clearance.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_agmt_mst.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&agreement_type="+agmt_type+
			"&u="+u+"&clearance="+clearance;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setValues(strTransCd,strTrans,strPlant,strBuPlant)
{
	var chk_plant = document.forms[0].chk_plant;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var trans_cd = document.forms[0].trans_cd;
	var trans_plant_seq_no = document.forms[0].trans_plant_seq_no;
	var trans_plant_abbr = document.forms[0].trans_plant_abbr;
	
	var sepTransCd = strTransCd.split("@");
	var sepTrans = strTrans.split("@");
	var sepPlant = strPlant.split("@");
	var sepBuPlant = strBuPlant.split("@");
	
	
	//FOR TRANSPORTER
	var display="";
	if(chk_trans!=null && chk_trans.length!=undefined)
 	{
  		for(var i=0;i<chk_trans.length;i++)
  		{
   			for(var j=0;j<sepTrans.length;j++)
   			{
     			if(trans_plant_seq_no[i].value == sepTrans[j] && trans_cd[i].value == sepTransCd[j])
     			{
       				chk_trans[i].checked = true;
       				trans_cd[i].disabled =false;
       				trans_plant_seq_no[i].disabled =false;
       				if(display!="")
       				{
       					display+=", "+trans_plant_abbr[i].value;
       				}
       				else
       				{
       					display+=trans_plant_abbr[i].value;
       				}
     			}
   			} 
  		} 
 	}
 	else if(chk_trans!=null)
 	{
   		for(var j=0;j<sepTrans.length;j++)
   		{
   			if(trans_plant_seq_no.value == sepTrans[j] && trans_cd.value == sepTransCd[j])
     		{
       			chk_trans.checked = true;
       			trans_cd.disabled =false;
   				trans_plant_seq_no.disabled =false;
       			if(display!="")
   				{
   					display+=", "+trans_plant_abbr.value;
   				}
   				else
   				{
   					display+=trans_plant_abbr.value;
   				}
     		}
   		} 
 	}
	document.getElementById("tansDisplay").innerHTML=display;
	if(display != "")
	{
		document.getElementById("tansDisplay").style.display="inline";
		document.getElementById("tansAlert").style.display="none";
	}
	
	
	//FOR PLANT
	if(chk_plant!=null && chk_plant.length!=undefined)
 	{
  		for(var i=0;i<chk_plant.length;i++)
  		{
   			for(var j=0;j<sepPlant.length;j++)
   			{
     			if(chk_plant[i].value == sepPlant[j])
     			{
     				chk_plant[i].checked = true;
     			}
   			} 
  		} 
 	}
 	else if(chk_plant!=null)
 	{
   		for(var j=0;j<sepPlant.length;j++)
   		{
   			if(chk_plant.value == sepPlant[j])
     		{
   				chk_plant.checked = true;
     		}
   		} 
 	}
	
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

function doSubmitTransSel()
{
	var chk_trans = document.forms[0].chk_trans;
	var trans_cd = document.forms[0].trans_cd;
	var trans_plant_seq_no = document.forms[0].trans_plant_seq_no;
	var trans_plant_abbr = document.forms[0].trans_plant_abbr;
	
	var display ="";
	if(chk_trans!=null && chk_trans.length!=undefined)
 	{
  		for(var i=0;i<chk_trans.length;i++)
  		{
   			if(chk_trans[i].checked)
   			{
   				if(display!="")
   				{
   					display+=", "+trans_plant_abbr[i].value;
   				}
   				else
   				{
   					display+=trans_plant_abbr[i].value;
   				}
   			} 
  		} 
 	}
 	else if(chk_trans!=null)
 	{
   		if(chk_trans.checked)
     	{
   			if(display!="")
			{
				display+=", "+trans_plant_abbr.value;
			}
			else
			{
				display+=trans_plant_abbr.value;
			}
   		} 
 	}
	
	document.getElementById("tansDisplay").innerHTML=display;
	if(display != "")
	{
		document.getElementById("tansDisplay").style.display="inline";
		document.getElementById("tansAlert").style.display="none";
	}
	$("#TransModal").modal("hide");
}

function checkMdcq()
{
	var mdcq=document.forms[0].mdcq.value;
	if(mdcq!=null && trim(mdcq)!='' )
	{
	    var mdcq_per = parseFloat(mdcq);
		if(mdcq_per < 100)
		{
			alert("MDCQ percentage cannot be less than 100 percent !!! ");
			document.forms[0].mdcq.value='';
		}
	}
}

function enabledEffDt(obj)
{
	var rev_eff_dt = document.forms[0].rev_eff_dt;
	if(obj.checked)
	{
		rev_eff_dt.readOnly=false;
		rev_eff_dt.style.pointerEvents = "auto";
	}
	else
	{
		rev_eff_dt.readOnly=true;
		rev_eff_dt.style.pointerEvents = "none";
	}
}

function liabilityShows()
{
	const checkbox = document.getElementById("liability_checkbox");
	const div_std1 = document.getElementById("liability_div1");
	const div_std2 = document.getElementById("liability_div2");

	if (checkbox.checked) 
	{
		div_std1.style.display = "block";
		div_std2.style.display = "block";
		
		document.getElementById("liability_checkbox").value="Y";
	} 
	else 
	{
		div_std1.style.display = "none";
		div_std2.style.display = "none";
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

function openLiabilityClause()
{
	var opration = document.forms[0].opration.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_no="";
	if(opration=="MODIFY")
	{
		var agmt_no = document.forms[0].agmt_no.value;
		var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	}
	
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var agmt_type = document.forms[0].agmt_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_agmt_liability_dtl.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agmt_type+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+
			"&end_dt="+end_dt+"&u="+u;
	
	
	if(agmt_no == "" || agmt_no == "0")
	{
		alert("Please Create or Select Agreement!");
	}
	else
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Agreement Liability Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Agreement Liability Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function messurmentShows()
{
	const checkbox = document.getElementById("measurementCheckbox");
	const div_clause = document.getElementById("measurement_clause_div");
	const div_std = document.getElementById("mesurement_div_std");
	
	if (checkbox.checked) 
	{
		div_clause.style.display = "block";
		div_std.style.display = "block";		
		document.getElementById("measurementCheckbox").value="Y";
	} 
	else 
	{
		div_clause.style.display = "none";
		div_std.style.display = "none";
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

function off_spec_gasShows()
{
	const checkbox = document.getElementById("off_spec_gas_checkbox");
	const div_clause = document.getElementById("off_spec_clause_div");
	const div_std = document.getElementById("off_spec_gas_div");
	
	if (checkbox.checked) 
	{
		div_clause.style.display = "block";
		div_std.style.display = "block";
		
		document.getElementById("off_spec_gas_checkbox").value="Y";
	} 
	else 
	{
		div_clause.style.display = "none";
		div_std.style.display = "none";
	}
}

function terminatorShows()
{
	const checkbox = document.getElementById("terminator_checkbox");
	const div_std1 = document.getElementById("teminator_div");
	const div_std2 = document.getElementById("teminator_div1");

	if (checkbox.checked) 
	{
		div_std1.style.display = "block";
		div_std2.style.display = "block";
		
		document.getElementById("terminator_checkbox").value="Y";
	} 
	else 
	{
		div_std1.style.display = "none";
		div_std2.style.display = "none";
	}
}

function checkAllContractDuration(end_dt)
{
	var display_msg = document.forms[0].display_msg.value;
	var max_end_dt = document.forms[0].max_end_dt.value;
	
	var count = compareDate(max_end_dt,end_dt);
	
	if(end_dt != '' && end_dt != null)
	{
		if(parseInt(count) == 1)
		{
			document.forms[0].end_dt.value = "";
			alert(display_msg)
		}
	}
}

function reactivateAgmt(deal_map)
{
	var a=confirm("Do you want to Reactivate the agreement#("+deal_map+")?");
	if(a)
	{
		document.forms[0].reopen_request_flag.value="Y";
		document.forms[0].submit();
	}
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
<jsp:useBean class="com.etrm.fms.contract_master.DataBean_ContractMaster" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String dateTime = utildate.getSysdateWithTime24hr();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String clearance = request.getParameter("clearance")==null?"KYC":request.getParameter("clearance");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String agreement_type = request.getParameter("agreement_type")==null?"F":request.getParameter("agreement_type");

contract.setCallFlag("AGREEMENT_MST");
contract.setClearance(clearance);
contract.setCounterparty_cd(counterparty_cd);
contract.setComp_cd(owner_cd);
contract.setOpration(opration);
contract.setAgmt_no(agmt_no);
contract.setAgmt_rev_no(agmt_rev_no);
contract.setAgreement_type(agreement_type);
contract.init();

Vector VCOUNTERPARTY_CD = contract.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = contract.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = contract.getVCOUNTERPARTY_ABBR();

Vector VPLANT_NM = contract.getVPLANT_NM();
Vector VPLANT_ABBR = contract.getVPLANT_ABBR();
Vector VPLANT_SEQ_NO = contract.getVPLANT_SEQ_NO();
Vector VTRANS_CD = contract.getVTRANS_CD();
Vector VTRANS_PLANT_NM = contract.getVTRANS_PLANT_NM();
Vector VTRANS_PLANT_ABBR = contract.getVTRANS_PLANT_ABBR();
Vector VTRANS_PLANT_SEQ_NO = contract.getVTRANS_PLANT_SEQ_NO();
Vector VBU_CD = contract.getVBU_CD();
Vector VBU_PLANT_NM = contract.getVBU_PLANT_NM();
Vector VBU_PLANT_ABBR = contract.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = contract.getVBU_PLANT_SEQ_NO();

Vector VSEL_PLANT_SEQ_NO = contract.getVSEL_PLANT_SEQ_NO();
Vector VSEL_TRANS_CD = contract.getVSEL_TRANS_CD();
Vector VSEL_TRANS_PLANT_SEQ_NO = contract.getVSEL_TRANS_PLANT_SEQ_NO();
Vector VSEL_BU_CD = contract.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = contract.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_TRAD_CD = contract.getVSEL_TRAD_CD();
Vector VSEL_PLANT_ABBR = contract.getVSEL_PLANT_ABBR();
Vector VSEL_TRANS_PLANT_ABBR = contract.getVSEL_TRANS_PLANT_ABBR();
Vector VSEL_BU_PLANT_ABBR = contract.getVSEL_BU_PLANT_ABBR();

Vector VTEMP_TRANS_CD = contract.getVTEMP_TRANS_CD();
Vector VTEMP_TRANS_ABBR = contract.getVTEMP_TRANS_ABBR();

Vector VFORMULA_ID = contract.getVFORMULA_ID();
Vector VFORMULA_NM = contract.getVFORMULA_NM();

String min_counterparty_eff_dt = contract.getMin_counterparty_eff_dt();

String signing_dt = contract.getSigning_dt();
String ent_dt = contract.getEnt_dt();
String ent_time = contract.getEnt_time();
String start_dt = contract.getStart_dt();
String end_dt = contract.getEnd_dt();
String agmt_base = contract.getAgmt_base();
String agmt_type = contract.getAgmt_type();
String buy_nom_flag = contract.getBuy_nom_flag();
String buy_month_nom = contract.getBuy_month_nom();
String buy_fortnightly_nom = contract.getBuy_fortnightly_nom();
String buy_week_nom = contract.getBuy_week_nom();
String buy_daily_nom = contract.getBuy_daily_nom();
String sell_nom_flag = contract.getSell_nom_flag();
String sell_month_nom = contract.getSell_month_nom();
String sell_fortnightly_nom = contract.getSell_fortnightly_nom();
String sell_week_nom = contract.getSell_week_nom();
String sell_daily_nom = contract.getSell_daily_nom();
String day_def_flag = contract.getDay_def_flag();
String day_start_time = contract.getDay_start_time();
String day_end_time = contract.getDay_end_time();
String mdcq_flag = contract.getMdcq_flag();
String mdcq_percentage = contract.getMdcq_percentage();
String remark = contract.getRemark();
String contpty_abbr = contract.getContpty_abbr();
String cont_name = contract.getCont_name();
String dealMapping = contract.getDealMapping();
String cont_ref_no = contract.getCont_ref_no();
String status = contract.getStatus();
String status_nm = contract.getStatus_nm();
String bill_flag = contract.getBill_flag();
String rev_dt = contract.getRev_dt();
String buy_nom_cutoff_time = contract.getBuy_nom_cutoff_time();

String max_end_dt=contract.getMax_end_dt();
String display_msg=contract.getDisplay_msg();

if(agmt_base.equals("")){
	agmt_base="X";
}
if(agmt_type.equals("")){
	agmt_type="0";
}
if(buy_nom_cutoff_time.equals("")){
	buy_nom_cutoff_time="00:00";
}
if(day_start_time.equals("")){
	day_start_time="06:00";
}
if(day_end_time.equals("")){
	day_end_time="06:00";
}
if(mdcq_percentage.equals(""))
{
	mdcq_percentage="100";
}

if(ent_dt.equals(""))
{
	String split[] = dateTime.split(" ");
	ent_dt = split[0];
	ent_time = split[1];
}

String strTransCd="";
String strTrans="";
String strPlant="";
String strBuPlant="";
for(int i=0;i<VSEL_TRANS_CD.size();i++)
{
	strTransCd = strTransCd + VSEL_TRANS_CD.elementAt(i)+"@";
	strTrans = strTrans + VSEL_TRANS_PLANT_SEQ_NO.elementAt(i)+"@";
}
for(int i=0;i<VSEL_PLANT_SEQ_NO.size();i++)
{
	strPlant = strPlant + VSEL_PLANT_SEQ_NO.elementAt(i)+"@";
}
for(int i=0;i<VSEL_BU_PLANT_SEQ_NO.size();i++)
{
	strBuPlant = strBuPlant + VSEL_BU_PLANT_SEQ_NO.elementAt(i)+"@";
}

String billing_clause = contract.getBilling_clause();
String messurment_flag = contract.getMessurment_flag();
String meas_clause = contract.getMeas_clause();
String meas_std = contract.getMeas_std();
String meas_temp = contract.getMeas_temp();
String pressure_max_bar = contract.getPressure_max_bar();
String pressure_min_bar = contract.getPressure_min_bar();
String off_spec_gas_flag = contract.getOff_spec_gas_flag();
String spec_gas_eng_base = contract.getSpec_gas_eng_base();
String spec_cause = contract.getSpec_clause();
String spec_max_eng = contract.getSpec_max_eng();
String spec_min_eng = contract.getSpec_min_eng();
String liability_flag = contract.getLiability_flag();
String liability_clause = contract.getLiability_clause();
String liability_lq_dmg = contract.getLiability_lq_dmg();
String liability_take_pay = contract.getLiability_take_pay();
String liability_makeup = contract.getLiability_makeup();
String terminaton_flag = contract.getTermination_flag();
String termination_clause = contract.getTermination_clause();
String termination_planned = contract.getTermination_planned();
String termination_forced = contract.getTermination_forced();
String no_of_billing_dtl=contract.getNo_of_billing_dtl();
String no_of_liability_dtl=contract.getNo_of_liability_dtl();

String is_expired=contract.getIs_expired();
String reopen_request_flg=contract.getReopen_request_flg();
String reopen_approval_flag=contract.getReopen_approval_flag();
%>
<body onload="setValues('<%=strTransCd%>','<%=strTrans%>','<%=strPlant%>','<%=strBuPlant%>');liabilityShows();billingShows();messurmentShows();off_spec_gasShows();terminatorShows();">
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_ContracMaster">

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
					    	Gas Sales Agreement (FGSA)
					    </div>
					    <div class="btn-group">
						  <lable class="btn btn-outline-secondary btngrp <%if(opration.equals("INSERT")){%>btnactive<%}%>" onclick="refresh('INSERT','<%=clearance%>');"><i class="fa fa-plus-circle"></i>&nbsp;New</lable>
						  <lable class="btn btn-outline-secondary btngrp <%if(opration.equals("MODIFY")){%>btnactive<%}%>" onclick="refresh('MODIFY','<%=clearance%>');"><i class="fa fa-edit"></i>&nbsp;Modify</lable>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12" align="center">
				    				<div class="btn-group">
										<lable class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("KYC")){%>btnactive<%}%>" onclick="refresh('<%=opration%>','KYC');">KYC</lable>
										<lable class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("IGX")){%>btnactive<%}%>" onclick="refresh('<%=opration%>','IGX');" style="pointer-events: none;">IGX</lable>
									</div>
				    			</div>
				  			</div>
						</div>
					</div>
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
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=opration%>','<%=clearance%>');">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Agreement Type</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="agmt_type" style="pointer-events: none;">
				    					<option value="F">FGSA</option>
				    					<option value="T">Tender</option>
				    				</select>
				    				<script>document.forms[0].agmt_type.value="<%=agreement_type%>"</script>
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
				    				<label class="form-label">
				    					<%if(is_expired.equals("Y")){%>
					    					<b><font color="red">Archived</font></b>
				    					<%}else{%>
				    						<b><%=status_nm%></b>
				    					<%} %>
				    				</label>
				      			</div>
				  			</div>
						</div>
					</div>
					<%if(opration.equals("MODIFY")){ %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    			<!-- <label class="form-label"><b>Contract No<span class="s-red">*</span></b></label> -->
				    			<input type="button" class="btn btn-info btn-sm select_btn" value="Select Agreement" onclick="openContList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-4 col-xs-4 col-md-4">
									<input type="text" class="form-control form-control-sm" name="dealMapping" value="<%=cont_name%>" maxLength="50" readOnly>
								</div>
				    			<div class="col-sm-8 col-xs-8 col-md-8">
				    				<input type="text" class="form-control form-control-sm" name="cont_name" value="<%=cont_name%>" maxLength="50" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b><input type="checkbox" class="form-check-input" name="rev_chk" value="Y" onclick="enabledEffDt(this);">&nbsp;Apply Revision</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Effective Date</b></label>
								</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="rev_eff_dt" value="<%=rev_dt%>" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off" readOnly style="pointer-events: none;">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<%} %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Agreement Ref#</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="agmt_ref_no" value="<%=cont_ref_no%>" maxLength="25" oninput="handleComma(this,'Agreement Ref#')">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Agreement Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="signing_dt" value="<%=signing_dt%>" maxLength="10" 
			      						onblur="validateDate(this);checkSigningStartDt(document.forms[0].start_dt, this.value);" 
			      						onchange="validateDate(this);checkSigningStartDt(document.forms[0].start_dt,this.value);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Agreement Type<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="radio" name="agreement_type" value="0" <%if(agmt_type.equals("0")){%>checked<%}%>>&nbsp;Term&nbsp;&nbsp;
				      				<input type="radio" name="agreement_type" value="1" <%if(agmt_type.equals("1")){%>checked<%}%>>&nbsp;Spot&nbsp;&nbsp;
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Agreement Base<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="radio" name="agreement_base" value="X" <%if(agmt_base.equals("X")){%>checked<%}%>>&nbsp;Ex-Terminal&nbsp;&nbsp;
				      				<input type="radio" name="agreement_base" value="D" <%if(agmt_base.equals("D")){%>checked<%}%>>&nbsp;Delivery&nbsp;&nbsp;
				      				<input type="radio" name="agreement_base" value="B" <%if(agmt_base.equals("B")){%>checked<%}%>>&nbsp;Ex-Terminal/ Delivery&nbsp;&nbsp;
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
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="start_dt" value="<%=start_dt%>" maxLength="10" 
			      						onblur="validateDate(this);checkEffDtWithContStDt(this,'<%=min_counterparty_eff_dt%>');checkStartEndDate(this,document.forms[0].end_dt,'F');checkSigningStartDt(this,document.forms[0].signing_dt.value);" 
			      						onchange="validateDate(this);checkEffDtWithContStDt(this,'<%=min_counterparty_eff_dt%>');checkStartEndDate(this,document.forms[0].end_dt,'F');checkSigningStartDt(this,document.forms[0].signing_dt.value);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
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
			      						onblur="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');checkAllContractDuration(this.value);" 
			      						onchange="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');checkAllContractDuration(this.value);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
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
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="button" class="btn btn-sm config_btn" value="Configure Delivery Point" title="Transporter Exit Point / Trader Delivery Point" 
				    				data-bs-toggle="modal" data-bs-target="#TransModal">
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label" id="tansDisplay" style="display:none;"></label>
				    				<div id="tansAlert" align="left"><%=utilmsg.warningMessage("Please configure Transporter Plants!")%></div>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Customer(<%=contpty_abbr%>)-Plant/s<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<%if(VPLANT_SEQ_NO.size() > 0) {%>
					    				<%for(int i=0; i<VPLANT_SEQ_NO.size(); i++){ %>
					    					<input type="checkbox" class="form-check-input" name="chk_plant" value="<%=VPLANT_SEQ_NO.elementAt(i)%>">&nbsp;<%=VPLANT_ABBR.elementAt(i)%>
					    				<%}%>
				    				<%}else{ %>
				    					<%= utilmsg.warningMessage("Please configure Plants for Selected Customer!")%>
				    				<%} %>
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
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Agreement Clauses</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="buyer_nom" value="Y" <%if(buy_nom_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Buyer Nomination</b></label>
				  				</div>
				    			<div class="col">
				    				<input type="text" class="form-control form-control-sm" name="buy_clause_no" value="" placeholder="Clause#">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="checkbox" class="form-check-input" name="chk_buyer_nom" value="M" <%if(buy_month_nom.equals("Y")||buy_month_nom.equals("")){ %>checked<%} %>>&nbsp;Monthly&nbsp;&nbsp;
				    				<input type="checkbox" class="form-check-input" name="chk_buyer_nom" value="F" <%if(buy_fortnightly_nom.equals("Y")||buy_fortnightly_nom.equals("")){ %>checked<%} %>>&nbsp;Fortnightly&nbsp;&nbsp;
				    				<input type="checkbox" class="form-check-input" name="chk_buyer_nom" value="W" <%if(buy_week_nom.equals("Y")||buy_week_nom.equals("")){ %>checked<%} %>>&nbsp;Weekly&nbsp;&nbsp;
				    				<input type="checkbox" class="form-check-input" name="chk_buyer_nom" value="D" <%if(buy_daily_nom.equals("Y")||buy_daily_nom.equals("")){ %>checked<%}else{ %>checked<%} %> style="pointer-events: none;">&nbsp;Daily&nbsp;&nbsp;	
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Cut-off Time</b></label>
								</div>
				    			<div class="col-auto">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="buy_nom_cutoff" value="<%=buy_nom_cutoff_time%>" maxLength="5" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
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
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="seller_nom" value="Y" <%if(sell_nom_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Seller Nomination</b></label>
				  				</div>
				    			<div class="col">
				    				<input type="text" class="form-control form-control-sm" name="buy_clause_no" value="" placeholder="Clause#">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="checkbox" name="chk_seller_nom" class="form-check-input" value="M" <%if(sell_month_nom.equals("Y")||sell_fortnightly_nom.equals("")){ %>checked<%} %>>&nbsp;Monthly&nbsp;&nbsp;
				    				<input type="checkbox" name="chk_seller_nom" class="form-check-input" value="F" <%if(sell_fortnightly_nom.equals("Y")||sell_fortnightly_nom.equals("")){ %>checked<%} %>>&nbsp;Fortnightly&nbsp;&nbsp;
				    				<input type="checkbox" name="chk_seller_nom" class="form-check-input" value="W" <%if(sell_week_nom.equals("Y")||sell_week_nom.equals("")){ %>checked<%} %>>&nbsp;Weekly&nbsp;&nbsp;
				    				<input type="checkbox" name="chk_seller_nom" class="form-check-input" value="D" <%if(sell_daily_nom.equals("Y")||sell_daily_nom.equals("")){ %>checked<%}else{ %>checked<%} %> style="pointer-events: none;">&nbsp;Daily&nbsp;&nbsp;
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
				    				<input type="text" class="form-control form-control-sm" name="buy_clause_no" value="" placeholder="Clause#">
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
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="mdcq_flag" value="Y" <%if(day_def_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;MDCQ(%)</b></label>
				  				</div>
				    			<div class="col">
				    				<input type="text" class="form-control form-control-sm" name="buy_clause_no" value="" placeholder="Clause#">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-auto">
				    				<input type="text" class="form-control form-control-sm" name="mdcq" value="<%=mdcq_percentage%>" onkeyup="checkForNumber(this);" onblur="checkNumber1(this,5,2);checkMdcq();">
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b><input type="checkbox" class="form-check-input" name="measurementCheckbox" id="measurementCheckbox" value="Y" onchange="messurmentShows()" <%if(messurment_flag.equals("Y")){ %>checked<%} %>>&nbsp;Measurement</b></label>
				  				</div>
				    			<div id="measurement_clause_div" class="col">
				    				<input type="text" class="form-control form-control-sm" name="measure_clause_no" value="<%=meas_clause%>" placeholder="Clause#" maxLength="10">
				      			</div>
				  			</div>
						</div>
						<div id="mesurement_div_std" class="col-sm-10 col-xs-10 col-md-10">
							<div class="form-group row">  
								<div class="col-auto">
									<div class="form-group row">
										<div class="col-auto">
											<label class="form-label">&nbsp;Standard</label>
										</div>
										<div class="col-auto">
											<input type="text" class="form-control form-control-sm" name="meas_standard" value="<%=meas_std%>" maxLength="20">								
										</div>	
									</div>
								</div>
								<div class="col-auto">
									<div class="form-group row">				    			
										<div class="col-auto">
											<label class="form-label">&nbsp;Temperature</label>
										</div>
										<div class="col-auto">
											<div class="input-group input-group-sm" >
												<input type="text" class="form-control form-control-sm" name="meas_temperature" id="meas_temperature" value="<%=meas_temp%>"  onblur="checkValuePrecision(this.value,'2','3','meas_temperature')" maxLength="6">
												<span class="input-group-text"><sup>o</sup>C</span>	
											</div>														
										</div>
									</div>
								</div>
								<div class="col-auto">
									<div class="form-group row">				    			
						  				<div class="col-auto">
											<label class="form-label">&nbsp;Delivery Pressure</label>
										</div>
										<div class="col-auto">
											<div class="input-group input-group-sm" >
												<input type="text" placeholder="min" class="form-control form-control-sm" name="pressure_min_bar" id="pressure_min_bar" value="<%=pressure_min_bar%>" onblur="checkValuePrecision(this.value,'2','3','pressure_min_bar')" maxLength="6">
												<span class="input-group-text"><b>-</b></span>
												<input type="text" placeholder="max" class="form-control form-control-sm" name="pressure_max_bar" id="pressure_max_bar"  value="<%=pressure_max_bar%>" onblur="checkValuePrecision(this.value,'2','3','pressure_max_bar')" maxLength="6">
												<span class="input-group-text">Bar</span>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>	
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
 				  					<label class="form-label"><b><input type="checkbox" class="form-check-input" name="off_spec_gas_checkbox" id="off_spec_gas_checkbox" onchange="off_spec_gasShows()" value="Y" <%if(off_spec_gas_flag.equals("Y")){ %>checked<%} %>>&nbsp;Off Spec Gas</b></label>
				  				</div>
				    			<div id="off_spec_clause_div" class="col">
				    				<input type="text" class="form-control form-control-sm" name="spec_clause_no" value="<%=spec_cause%>" placeholder="Clause#" maxLength="10">
				      			</div>
				  			</div>
						</div>
						<div id="off_spec_gas_div" class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label">&nbsp;Energy Base</label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="spec_gas_energy_base" >
		      							<option value="0">--Select--</option>
		      							<option value="GCV">GCV</option>
		      							<option value="NCV">NCV</option>
		      						</select>
	 		      					<script>document.forms[0].spec_gas_energy_base.value="<%=spec_gas_eng_base.trim()%>"</script>
								</div>
								<div class="col-auto">
									<div class="input-group input-group-sm" >
										<input type="text" placeholder="min" class="form-control form-control-sm" name="spec_gas_min_energy" id="spec_gas_min_energy" value="<%=spec_min_eng%>" size="10" onblur="checkValuePrecision(this.value,'2','5','spec_gas_min_energy')" maxLength="8">
										<span class="input-group-text"><b>-</b></span>
										<input type="text" placeholder="max" class="form-control form-control-sm" name="spec_gas_max_energy" id="spec_gas_max_energy" value="<%=spec_max_eng%>" size="10" onblur="checkValuePrecision(this.value,'2','5','spec_gas_max_energy')" maxLength="8">										
										<span class="input-group-text">Kcal/SCM</span>
									</div>
								</div>								
							</div>
						</div>							
					</div>
					
					<div class="row m-b-5">
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
				    				<input type="button" class="btn btn-sm config_btn" value="Configure Liability" <%if(opration.equals("MODIFY")){ %>onclick="openLiabilityClause();"<%} %>>&nbsp;
				    				<%if(opration.equals("MODIFY")){ %>
				    				<input type="checkbox" class="form-check-input" name="liab_lq_damg" value="Y" <%if(liability_lq_dmg.equals("Y")){ %>checked<%} %> style="pointer-events: none;">&nbsp;Liquidated Damages&nbsp;&nbsp;  
				    				<input type="checkbox" class="form-check-input" name="liab_take_pay" value="Y" <%if(liability_take_pay.equals("Y")){ %>checked<%} %> style="pointer-events: none;">&nbsp;Take or Pay&nbsp;&nbsp;
				    				<input type="checkbox" class="form-check-input" name="liab_makeup" value="Y" <%if(liability_makeup.equals("Y")){ %>checked<%} %> style="pointer-events: none;">&nbsp;Make Up Gas   
				    				<%} %>
				    			</div>
				  			</div>
						</div>
					</div>
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
						      		<input type="button" class="btn btn-sm config_btn" value="Configure Billing" <%if(opration.equals("MODIFY")){ %>onclick="openBillingDtl();"<%} %>>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="terminator_checkbox" id="terminator_checkbox" onchange="terminatorShows();" value="Y" <%if(terminaton_flag.equals("Y")){ %>checked<%} %>>&nbsp;Termination/Suspention</b></label>
				  				</div>
				  				<div id="teminator_div" class="col">
				    				<input type="text" class="form-control form-control-sm" name="terminate_clause" value="<%=termination_clause %>" placeholder="Clause#" maxLength="10">
				      			</div>
				  			</div>
						</div>
						<div id="teminator_div1" class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="checkbox" class="form-check-input" name="terminate_planed" value="Y" <%if(termination_planned.equals("Y")){ %>checked<%} %>>&nbsp;Planned Maintenance&nbsp;&nbsp;
			      					<input type="checkbox" class="form-check-input" name="terminate_force" value="Y" <%if(termination_forced.equals("Y")){ %>checked<%} %>>&nbsp;Force Majeure
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
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Agreement Deactivate/ Activate/ Closure/ Renewal</label>
					</div>
					<div class="row m-b-5">
						<!-- <div class="col-auto">
							<input type="button" class="btn btn-sm request_btn" value="Deactivation Request" style="background:#e6e6e6;color:black;">
						</div> -->
						<div class="col-auto" title="<%if(reopen_request_flg.equals("Y")){%>Activation/Re-Open request generated!<%}%>">
							<input type="button" class="btn btn-sm request_btn" value="Re-Open Request" <%if(is_expired.equals("Y") && !reopen_request_flg.equals("Y") && !reopen_approval_flag.equals("Y")){%>onclick="reactivateAgmt('<%=dealMapping %>')";<%}else{%>style="background:#e6e6e6;color:black" disabled<%}%>>
						</div>
						<div class="col-auto">
							<input type="button" class="btn btn-sm request_btn" value="Closure Request" style="background:#e6e6e6;color:black">
						</div>
						<!-- <div class="col-auto">
							<input type="button" class="btn btn-sm request_btn" value="Apply Renewal" style="background:#e6e6e6;color:black">
						</div> -->
					</div>
					<br>
					<%if(!agmt_no.equals("")){%>
						<%if(is_expired.equals("Y") && !reopen_approval_flag.equals("Y") && !reopen_approval_flag.equals("N")){%>
							<div class="row m-b-5">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.errorMessage("<b>This deal has been Archived!</b>")%>
								</div>
							</div>
						<%}%>
						<%if(reopen_request_flg.equals("Y") ){%>
							<div class="row m-b-5">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.infoMessage("<b>Activation/Re-Open request has been generated!</b>")%>
								</div>
							</div>
						<%}%>
						<%if(reopen_approval_flag.equals("Y")){%>
							<div class="row m-b-5">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.infoMessage("<b>Agreement is activated!</b>")%>
								</div>
							</div>
						<%}else if(reopen_approval_flag.equals("N")){%>
							<div class="row m-b-5">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.infoMessage("<b>Agreement is activation/re-open request has been rejected!</b>")%>
								</div>
							</div>
						<%} %>
					<%}%>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y") && (!is_expired.equals("Y") || reopen_approval_flag.equals("Y"))){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- TRASPORTER MODEL -->
<div class="modal fade" id="TransModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Transporter Entry Point
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
	      			<%for(int i=0; i<VTEMP_TRANS_CD.size(); i++){ %>
	      				<%if(i==0){ %>
	      				<div class="row m-b-5">
							<label class="form-label subheader"><%=VTEMP_TRANS_ABBR.elementAt(i)%></label>
						</div>
	      				<%}else{ %>
	      				<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<div class="row m-b-5">
							<label class="form-label subheader"><%=VTEMP_TRANS_ABBR.elementAt(i)%></label>
						</div>
	      				<%} %>
	      				<%int rwcount=0;
	      				boolean ck = false;
	      				if(VTRANS_CD.contains(VTEMP_TRANS_CD.elementAt(i)))
	      				{
		      				for(int j=0; j<VTRANS_CD.size(); j++){ 
		      				%>
		      					<%if(VTRANS_CD.elementAt(j).equals(VTEMP_TRANS_CD.elementAt(i))){ 
			      					rwcount+=2;
			      					if(rwcount==2){ck=false;
			      					%>
			      					<div class="row m-b-5">
			      					<%} %>
										<div class="col-sm-2 col-xs-2 col-md-2">  
											<div class="form-group row">
												<div class="col-sm-12 col-xs-12 col-md-12">
								    				<input type="checkbox" class="form-check-input" name="chk_trans" onclick="enabled_TransPlantDtl(this,'trans_cd<%=VTRANS_CD.elementAt(j)%><%=VTRANS_PLANT_SEQ_NO.elementAt(j)%>','trans_plant_seq_no<%=VTRANS_CD.elementAt(j)%><%=VTRANS_PLANT_SEQ_NO.elementAt(j)%>')">&nbsp;<%=VTRANS_PLANT_ABBR.elementAt(j)%>&nbsp;&nbsp;
					    							<input type="hidden" name="trans_cd" id="trans_cd<%=VTRANS_CD.elementAt(j)%><%=VTRANS_PLANT_SEQ_NO.elementAt(j)%>" value="<%=VTRANS_CD.elementAt(j)%>" disabled>
					    							<input type="hidden" name="trans_plant_seq_no" id="trans_plant_seq_no<%=VTRANS_CD.elementAt(j)%><%=VTRANS_PLANT_SEQ_NO.elementAt(j)%>" value="<%=VTRANS_PLANT_SEQ_NO.elementAt(j)%>" disabled>
					    							<input type="hidden" name="trans_plant_abbr" id="trans_plant_abbr<%=VTRANS_CD.elementAt(j)%><%=VTRANS_PLANT_SEQ_NO.elementAt(j)%>" value="<%=VTRANS_PLANT_ABBR.elementAt(j)%>" disabled>
								  				</div>
								  			</div>
										</div>
									<%if(rwcount==12){rwcount=0;ck=true;%>
									</div>
									<%} %>
		      					<%} %>
		      				<%} %>
		      				<%if(!ck){ %>
	      					</div>
	      					<%} %>
	      				<%}else{ %>
	      					<%= utilmsg.warningMessage("Please configure Transporter Plants!")%>
	      				<%} %>
	      			<%} %>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="" align="right">
        		<%if(write_access.equals("Y") && !is_expired.equals("Y")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitTransSel();">
				<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitTransSel();" disabled>
				<%} %>
				</div>
      		</div>
      	</div>
	</div>
</div>

<input type="hidden" name="option" value="AGREEMENT_MST">
<input type="hidden" name="prev_clearance" value="<%=clearance%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="status" value="<%=status%>">
<input type="hidden" name="reopen_request_flag" value="">
<input type="hidden" name="reopen_approval_flag" value="<%=reopen_approval_flag%>">


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

<input type="hidden" name="max_end_dt" value="<%=max_end_dt%>">
<input type="hidden" name="display_msg" value="<%=display_msg%>">

<script>
</script>

</form>
</body>
</html>