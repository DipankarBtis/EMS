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
	var u = document.forms[0].u.value;
	
	var url = "frm_ltcora_agmt_sell_mst.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function enabledEffDt(obj)
{
	var rev_eff_dt = document.forms[0].rev_eff_dt;
	var sysdate = document.forms[0].sysdate.value;
	var prev_rev_eff_dt = document.forms[0].prev_rev_eff_dt.value;
	
	if(obj.checked)
	{
		rev_eff_dt.readOnly=true;
		rev_eff_dt.style.pointerEvents = "none";
		rev_eff_dt.value=sysdate;
	}
	else
	{
		rev_eff_dt.readOnly=true;
		rev_eff_dt.style.pointerEvents = "none";
		rev_eff_dt.value=prev_rev_eff_dt;
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

function liabilityShows()
{
	const checkbox = document.getElementById("liability_checkbox");
	const div_clause = document.getElementById("liability_clause_div");
	const div_std = document.getElementById("liability_div");

	if (checkbox.checked) 
	{
		div_clause.style.display = "block";
		div_std.style.display = "block";
		
		document.getElementById("liability_checkbox").value="Y";
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
	const div_clause = document.getElementById("teminator_clause_div");
	const div_std = document.getElementById("teminator_div");

	if (checkbox.checked) 
	{
		div_clause.style.display = "block";
		div_std.style.display = "block";
		
		document.getElementById("terminator_checkbox").value="Y";
	} 
	else 
	{
		div_clause.style.display = "none";
		div_std.style.display = "none";
	}
}
function billingShows()
{
	const checkbox = document.getElementById("billing_flag");
	const div_clause = document.getElementById("billing_clause_div");
	const div_std = document.getElementById("billing_div");

	if (checkbox.checked) 
	{
		div_clause.style.display = "block";
		div_std.style.display = "block";

		document.getElementById("billing_flag").value="Y";
	} 
	else 
	{
		div_clause.style.display = "none";
		div_std.style.display = "none";
	}
}

function revisionEffDateShows()
{
	const checkbox = document.getElementById("rev_chk");
	const div_std = document.getElementById("rev_chk_div");
	var opration = document.forms[0].opration.value;

	if(opration=="MODIFY")
	{
		if (checkbox.checked) 
		{
			div_std.style.display = "block";
			document.getElementById("rev_chk").value="Y";
		} 
		else 
		{
			div_std.style.display = "none";
		}
	}
}

var newWindow;
function openAgreementList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_type = document.forms[0].agmt_type.value;
	var buy_sale = document.forms[0].buy_sale.value;

	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_ltcora_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&buy_sale="+buy_sale+"&agreement_type="+agmt_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_ltcora_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&buy_sale="+buy_sale+"&agreement_type="+agmt_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function openBillingDtl()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var buy_sale = document.forms[0].buy_sale.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var agmt_type = document.forms[0].agmt_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_ltcora_agmt_billing_dtl.jsp?counterparty_cd="+counterparty_cd+"&buy_sale="+buy_sale+
			"&agreement_type="+agmt_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
			"&start_dt="+start_dt+"&end_dt="+end_dt+"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"LTCORA Agreement Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Agreement Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setAgmtDetail(countpty_cd,buy_sale,agmt_type,agmt_no,agmt_rev_no)
{
	var opration = document.forms[0].opration.value;
	//var clearance = document.forms[0].clearance.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_ltcora_agmt_sell_mst.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+
			"&buy_sale="+buy_sale+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&agreement_type="+agmt_type+
			"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
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

function setValues(strTransCd,strTrans,strPlant,strBuPlant)
{
	var chk_plant = document.forms[0].chk_plant;
	var chk_bu_plant = document.forms[0].chk_bu_plant;	
	var sepPlant = strPlant.split("@");
	var sepBuPlant = strBuPlant.split("@");
	
	var chk_trans = document.forms[0].chk_trans;
	var trans_cd = document.forms[0].trans_cd;
	var trans_plant_seq_no = document.forms[0].trans_plant_seq_no;
	var trans_plant_abbr = document.forms[0].trans_plant_abbr;
	
	var sepTransCd = strTransCd.split("@");
	var sepTrans = strTrans.split("@");
	
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

function doSubmit()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	
	var signing_dt = document.forms[0].signing_dt.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var agmt_ref_no = document.forms[0].agmt_ref_no.value;
	
	var agreement_type = document.forms[0].agreement_type;
	var agreement_base = document.forms[0].agreement_base;
	
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	
	var chk_plant = document.forms[0].chk_plant;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var chk_trans = document.forms[0].chk_trans;
	
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
	if(trim(agmt_ref_no) == "")
	{
		msg+="Enter Agreement Ref#!\n";
		flag=false;
	}
	if(trim(signing_dt) == "")
	{
		msg+="Enter Contract Date!\n";
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
	/* if(agreement_base[0].checked == false && agreement_base[1].checked == false && agreement_base[2].checked == false)
	{
		msg+="Select Agreement Base!\n";
		flag=false;
	} */
	
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
				a = confirm("On saving your modification/s, this Agreement will be Revised!\n\nDo you want to Proceed?");
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
			var temp_msg="";
			if(trim(no_of_billing_dtl) != "")
			{
				if(parseInt(no_of_billing_dtl) <= 0)
				{
					temp_msg += "Please filling the Billing Detail after Submitting Agreement Detail!\n";
				}
			}
			else
			{
				temp_msg += "Please filling the Billing Detail after Submitting Agreement Detail!!\n";
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
	var buy_sale = document.forms[0].buy_sale.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_ltcora_agmt_liability_clause.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agmt_type+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+"&buy_sale="+buy_sale+
			"&u="+u+"&end_dt="+end_dt;
	
	
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

<jsp:useBean class="com.etrm.fms.ltcora.DataBean_LtcoraMaster" id="ltcora" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String dateTime = utildate.getSysdateWithTime24hr();
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String buy_sale=request.getParameter("buy_sale")==null?"C":request.getParameter("buy_sale");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String agreement_type = request.getParameter("agreement_type")==null?"A":request.getParameter("agreement_type");

ltcora.setCallFlag("LTCORA_AGREEMENT_MST");
ltcora.setComp_cd(owner_cd);
ltcora.setCounterparty_cd(counterparty_cd);
ltcora.setAgreement_type(agreement_type);
ltcora.setAgmt_no(agmt_no);
ltcora.setAgmt_rev_no(agmt_rev_no);
ltcora.setBuy_sale(buy_sale);
ltcora.setOpration(opration);
ltcora.init();

Vector VCOUNTERPARTY_CD = ltcora.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = ltcora.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = ltcora.getVCOUNTERPARTY_ABBR();

Vector VPLANT_NM = ltcora.getVPLANT_NM();
Vector VPLANT_ABBR = ltcora.getVPLANT_ABBR();
Vector VPLANT_SEQ_NO = ltcora.getVPLANT_SEQ_NO();
Vector VTRANS_CD = ltcora.getVTRANS_CD();
Vector VTRANS_PLANT_NM = ltcora.getVTRANS_PLANT_NM();
Vector VTRANS_PLANT_ABBR = ltcora.getVTRANS_PLANT_ABBR();
Vector VTRANS_PLANT_SEQ_NO = ltcora.getVTRANS_PLANT_SEQ_NO();
Vector VBU_CD = ltcora.getVBU_CD();
Vector VBU_PLANT_NM = ltcora.getVBU_PLANT_NM();
Vector VBU_PLANT_ABBR = ltcora.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = ltcora.getVBU_PLANT_SEQ_NO();

Vector VSEL_PLANT_SEQ_NO = ltcora.getVSEL_PLANT_SEQ_NO();
Vector VSEL_BU_CD = ltcora.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = ltcora.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_TRAD_CD = ltcora.getVSEL_TRAD_CD();
Vector VSEL_PLANT_ABBR = ltcora.getVSEL_PLANT_ABBR();
Vector VSEL_BU_PLANT_ABBR = ltcora.getVSEL_BU_PLANT_ABBR();

Vector VTEMP_TRANS_CD = ltcora.getVTEMP_TRANS_CD();
Vector VTEMP_TRANS_ABBR = ltcora.getVTEMP_TRANS_ABBR();

Vector VSEL_TRANS_CD = ltcora.getVSEL_TRANS_CD();
Vector VSEL_TRANS_PLANT_SEQ_NO = ltcora.getVSEL_TRANS_PLANT_SEQ_NO();
Vector VSEL_TRANS_PLANT_ABBR = ltcora.getVSEL_TRANS_PLANT_ABBR();

String min_counterparty_eff_dt = ltcora.getMin_counterparty_eff_dt();

String cont_name = ltcora.getCont_name();
String rev_dt = ltcora.getRev_dt();
String signing_dt = ltcora.getSigning_dt();
String ent_dt = ltcora.getEnt_dt();
String ent_time = ltcora.getEnt_time();
String start_dt = ltcora.getStart_dt();
String end_dt = ltcora.getEnd_dt();
String cont_ref_no = ltcora.getCont_ref_no();
String contpty_abbr = ltcora.getContpty_abbr();
String status_nm = ltcora.getStatus_nm();
String agmt_base = ltcora.getAgmt_base();
String agmt_type = ltcora.getAgmt_type();

String buy_nom_flag = ltcora.getBuy_nom_flag();
String buy_nom_clause = ltcora.getBuy_nom_clause();
String buy_month_nom = ltcora.getBuy_month_nom();
String buy_fortnightly_nom = ltcora.getBuy_fortnightly_nom();
String buy_week_nom = ltcora.getBuy_week_nom();
String buy_daily_nom = ltcora.getBuy_daily_nom();
String buy_nom_cutoff_time = ltcora.getBuy_nom_cutoff_time();
String sell_nom_flag = ltcora.getSell_nom_flag();
String sell_nom_clause = ltcora.getSell_nom_clause();
String sell_month_nom = ltcora.getSell_month_nom();
String sell_fortnightly_nom = ltcora.getSell_fortnightly_nom();
String sell_week_nom = ltcora.getSell_week_nom();
String sell_daily_nom = ltcora.getSell_daily_nom();
String day_def_flag = ltcora.getDay_def_flag();
String day_def_clause = ltcora.getDay_def_clause();
String day_start_time = ltcora.getDay_start_time();
String day_end_time = ltcora.getDay_end_time();
String mdcq_flag = ltcora.getMdcq_flag();
String mdcq_clause = ltcora.getMdcq_clause();
String mdcq_percentage = ltcora.getMdcq_percentage();
String off_spec_gas_flag = ltcora.getOff_spec_gas_flag();
String spec_cause = ltcora.getSpec_clause();
String spec_gas_eng_base = ltcora.getSpec_gas_eng_base();
String spec_max_eng = ltcora.getSpec_max_eng();
String spec_min_eng = ltcora.getSpec_min_eng();
String messurment_flag = ltcora.getMessurment_flag();
String meas_clause = ltcora.getMeas_clause();
String meas_std = ltcora.getMeas_std();
String meas_temp = ltcora.getMeas_temp();
String pressure_max_bar = ltcora.getPressure_max_bar();
String pressure_min_bar = ltcora.getPressure_min_bar();
String liability_flag = ltcora.getLiability_flag();
String liability_clause = ltcora.getLiability_clause();
String liability_lq_dmg = ltcora.getLiability_lq_dmg();
String liability_take_pay = ltcora.getLiability_take_pay();
String liability_makeup = ltcora.getLiability_makeup();
String bill_flag = ltcora.getBill_flag();
String billing_clause = ltcora.getBilling_clause();
String no_of_billing_dtl=ltcora.getNo_of_billing_dtl();
String termination_flag = ltcora.getTerminaton_flag();
String termination_clause = ltcora.getTermination_clause();
String termination_planned = ltcora.getTermination_planned();
String termination_forced = ltcora.getTermination_forced();

String max_end_dt=ltcora.getMax_end_dt();
String display_msg=ltcora.getDisplay_msg();

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

String strPlant="";
String strBuPlant="";
String strTransCd="";
String strTrans="";

for(int i=0;i<VSEL_PLANT_SEQ_NO.size();i++)
{
	strPlant = strPlant + VSEL_PLANT_SEQ_NO.elementAt(i)+"@";
}
for(int i=0;i<VSEL_BU_PLANT_SEQ_NO.size();i++)
{
	strBuPlant = strBuPlant + VSEL_BU_PLANT_SEQ_NO.elementAt(i)+"@";
}
for(int i=0;i<VSEL_TRANS_CD.size();i++)
{
	strTransCd = strTransCd + VSEL_TRANS_CD.elementAt(i)+"@";
	strTrans = strTrans + VSEL_TRANS_PLANT_SEQ_NO.elementAt(i)+"@";
}

%>
<body onload="setValues('<%=strTransCd%>','<%=strTrans%>','<%=strPlant%>','<%=strBuPlant%>'); revisionEffDateShows();messurmentShows();off_spec_gasShows();liabilityShows();billingShows();terminatorShows();">
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_LtcoraMaster">

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
					    	LTCORA (Sell) Agreement
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
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Agreement</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="agmt_type" style="pointer-events: none;">
				    					<option value="A">LTCORA (Sell)</option>
				    				</select>
				    				<script>document.forms[0].agmt_type.value="<%=agreement_type%>"</script>
				      			</div>
				  			</div>
						</div>
					</div>
					<%if(opration.equals("MODIFY")){ %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    			<input type="button" class="btn btn-info btn-sm select_btn" value="Select Agreement" onclick="openAgreementList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="cont_name" value="<%=cont_name%>" maxLength="50" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
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
				    				<label class="form-label"><b><%=status_nm%></b></label>
				      			</div>
				  			</div>
						</div>
					</div>						
					<%} %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Agreement Ref#<span class="s-red">*</span></b></label>
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
							<label class="form-label"><b>Agreement Enter Date<span class="s-red">*</span></b></label>
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
							<label class="form-label"><b>Contract Date<span class="s-red">*</span></b></label>
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
						<%if(opration.equals("MODIFY")){ %>						
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Last Revised On</b></label>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">
							<div class="form-group row">								
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="rev_dt" value="<%=rev_dt%>" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off" readOnly style="pointer-events: none;">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<input type="hidden" name="prev_rev_eff_dt" value="<%=rev_dt%>">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">  
							<div class="form-group row">
				    			<label class="form-label"><b><input type="checkbox" class="form-check-input" name="rev_chk" id="rev_chk" value="Y" onchange="revisionEffDateShows();" onclick="enabledEffDt(this);">&nbsp;Apply Revision</b></label>
				  			</div>
						</div>
						<div id="rev_chk_div" class="col-sm-2 col-xs-2 col-md-2">
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
		      						<input type="hidden" name="prev_rev_eff_dt" value="<%=rev_dt%>">
				    			</div>
				  			</div>
						</div>
						<%} %>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Agreement Type<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="radio" name="agreement_type" value="O" <%if(agmt_type.equals("O")){%>checked<%}%>>&nbsp;CN&nbsp;&nbsp;
				      				<input type="radio" name="agreement_type" value="Q" <%if(agmt_type.equals("Q")){%>checked<%}%>>&nbsp;Period&nbsp;&nbsp;
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
				      				<%-- <input type="radio" name="agreement_base" value="D" <%if(agmt_base.equals("D")){%>checked<%}%>>&nbsp;Delivery&nbsp;&nbsp;
				      				<input type="radio" name="agreement_base" value="B" <%if(agmt_base.equals("B")){%>checked<%}%>>&nbsp;Ex-Terminal/ Delivery&nbsp;&nbsp; --%>
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
				    				<input type="button" class="btn btn-sm config_btn" value="Configure Delivery Point" title="Transporter Exit Point / Customer Delivery Point" 
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
				    				<input type="text" class="form-control form-control-sm" name="buy_clause_no" value="<%=buy_nom_clause%>" placeholder="Clause#" maxlength="10">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="checkbox" class="form-check-input" name="chk_buyer_nom" value="M" <%if(buy_month_nom.equals("Y")){ %>checked<%} %>>&nbsp;Monthly&nbsp;&nbsp;
				    				<input type="checkbox" class="form-check-input" name="chk_buyer_nom" value="F" <%if(buy_fortnightly_nom.equals("Y")){ %>checked<%} %>>&nbsp;Fortnightly&nbsp;&nbsp;
				    				<input type="checkbox" class="form-check-input" name="chk_buyer_nom" value="W" <%if(buy_week_nom.equals("Y")){ %>checked<%} %>>&nbsp;Weekly&nbsp;&nbsp;
				    				<input type="checkbox" class="form-check-input" name="chk_buyer_nom" value="D" <%if(buy_daily_nom.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Daily&nbsp;&nbsp;	
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
				    				<input type="text" class="form-control form-control-sm" name="sell_clause_no" value="<%=sell_nom_clause%>" placeholder="Clause#" maxlength="10">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="checkbox" name="chk_seller_nom" class="form-check-input" value="M" <%if(sell_month_nom.equals("Y")){ %>checked<%} %>>&nbsp;Monthly&nbsp;&nbsp;
				    				<input type="checkbox" name="chk_seller_nom" class="form-check-input" value="F" <%if(sell_fortnightly_nom.equals("Y")){ %>checked<%} %>>&nbsp;Fortnightly&nbsp;&nbsp;
				    				<input type="checkbox" name="chk_seller_nom" class="form-check-input" value="W" <%if(sell_week_nom.equals("Y")){ %>checked<%} %>>&nbsp;Weekly&nbsp;&nbsp;
				    				<input type="checkbox" name="chk_seller_nom" class="form-check-input" value="D" <%if(sell_daily_nom.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Daily&nbsp;&nbsp;
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
				    				<input type="text" class="form-control form-control-sm" name="day_clause_no" value="<%=day_def_clause%>" placeholder="Clause#" maxlength="10">
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
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="mdcq_flag" value="Y" <%if(mdcq_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;MCSOC(%)</b></label>
				  				</div>
				    			<div class="col">
				    				<input type="text" class="form-control form-control-sm" name="mdcq_clause_no" value="<%=mdcq_clause%>" placeholder="Clause#" maxlength="10">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-auto">
				    				<input type="text" class="form-control form-control-sm" name="mdcq_percent" value="<%=mdcq_percentage%>" onkeyup="negNumber(this);" onblur="appPercentage(this);">
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
				    				<input type="text" class="form-control form-control-sm" name="measure_clause_no" value="<%=meas_clause%>" placeholder="Clause#" maxlength="10">
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
											<input type="text" class="form-control form-control-sm" name="meas_standard" value="<%=meas_std%>">								
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
												<input type="text" class="form-control form-control-sm" name="meas_temperature" id="meas_temperature" value="<%=meas_temp%>"  >
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
												<input type="text" placeholder="min" class="form-control form-control-sm" name="pressure_min_bar" id="pressure_min_bar" value="<%=pressure_min_bar%>" >
												<span class="input-group-text"><b>-</b></span>
												<input type="text" placeholder="max" class="form-control form-control-sm" name="pressure_max_bar" id="pressure_max_bar"  value="<%=pressure_max_bar%>">
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
				    			<div class="col" id="off_spec_clause_div">
				    				<input type="text" class="form-control form-control-sm" name="spec_clause_no" value="<%=spec_cause%>" placeholder="Clause#" maxlength="10">
				      			</div>
				  			</div>
						</div>
						<div id="off_spec_gas_div" class="col-sm-10 col-xs-10 col-md-10">
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
										<input type="text" placeholder="min" class="form-control form-control-sm" name="spec_gas_min_energy" id="spec_gas_min_energy" value="<%=spec_min_eng%>" size="10">
										<span class="input-group-text"><b>-</b></span>
										<input type="text" placeholder="max" class="form-control form-control-sm" name="spec_gas_max_energy" id="spec_gas_max_energy" value="<%=spec_max_eng%>" size="10">										
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
				    			<div id="liability_clause_div" class="col">
				    				<input type="text" class="form-control form-control-sm" name="liability_clause" value="<%=liability_clause %>" placeholder="Clause#" maxLength="10">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-8 col-xs-8 col-md-8">  
							<div class="form-group row">
				    			<div id="liability_div" class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="button" class="btn btn-sm config_btn" value="Configure Liability" <%if(opration.equals("MODIFY")){ %>onclick="openLiabilityClause();"<%} %>>&nbsp;
				    				<%if(opration.equals("MODIFY")){ %>
				    				<input type="checkbox" class="form-check-input" name="liab_lq_damg" value="Y" <%if(liability_lq_dmg.equals("Y")){ %>checked<%} %>>&nbsp;Liquidated Damages&nbsp;&nbsp;  
				    				<input type="checkbox" class="form-check-input" name="liab_take_pay" value="Y" <%if(liability_take_pay.equals("Y")){ %>checked<%} %>>&nbsp;Take or Pay&nbsp;&nbsp;
				    				<input type="checkbox" class="form-check-input" name="liab_makeup" value="Y" <%if(liability_makeup.equals("Y")){ %>checked<%} %>>&nbsp;Make Up Gas   
				    				<%} %>
				    			</div>				    			
 					  		</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="billing_flag" id="billing_flag" onchange="billingShows();" value="Y" <%if(bill_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Billing</b></label>
				  				</div>
				    			<div id="billing_clause_div" class="col">
				    				<input type="text" class="form-control form-control-sm" name="billing_clause_no" value="<%=billing_clause%>" placeholder="Clause#">
				      			</div>
				  			</div>
						</div>
						<div id="billing_div" class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="button" class="btn btn-sm config_btn" value="Configure Billing" onclick="openBillingDtl();">
					    		</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="terminator_checkbox" id="terminator_checkbox" onchange="terminatorShows();" value="Y" <%if(termination_flag.equals("Y")){ %>checked<%} %>>&nbsp;Termination/Suspention</b></label>
				  				</div>
				    			<div id="teminator_clause_div" class="col">
				    				<input type="text" class="form-control form-control-sm" name="terminate_clause_no" value="<%=termination_clause%>" placeholder="Clause#">
				      			</div>
				  			</div>
						</div>
						<div id="teminator_div" class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="checkbox" class="form-check-input" name="terminate_planed" value="Y" <%if(termination_planned.equals("Y")){ %>checked<%} %>>&nbsp;Planned Maintenance&nbsp;&nbsp;
			      					<input type="checkbox" class="form-check-input" name="terminate_force" value="Y" <%if(termination_forced.equals("Y")){ %>checked<%} %>>&nbsp;Force Majeure
								</div>
				  			</div>
						</div>
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
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	LTCORA (Sell) Agreement - Change Request(s)
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-auto">
							<input type="button" class="btn btn-sm request_btn" value="Deactivation Request" style="background:#e6e6e6;color:black;">
						</div>
						<div class="col-auto">
							<input type="button" class="btn btn-sm request_btn" value="Apply Activation" style="background:#e6e6e6;color:black">
						</div>
						<div class="col-auto">
							<input type="button" class="btn btn-sm request_btn" value="Closure Request" style="background:#e6e6e6;color:black">
						</div>
						<div class="col-auto">
							<input type="button" class="btn btn-sm request_btn" value="Apply Renewal" style="background:#e6e6e6;color:black">
						</div>
					</div>
					<br>					
					<%if(!agmt_no.equals("0") && !agmt_no.equals("")){ %>
						<%if(no_of_billing_dtl.equals("0") || no_of_billing_dtl.equals("")){ %>
							<div class="row m-b-5">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.errorMessage("<b>Billing Detail is checked but not Configured.</b>")%>
								</div>
							</div>
						<%} %>						
					<%} %>					
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
        		<%if(write_access.equals("Y")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Proceed" onclick="doSubmitTransSel();">
				<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Proceed" onclick="doSubmitTransSel();" disabled>
				<%} %>
				</div>
      		</div>
      	</div>
	</div>
</div>

<input type="hidden" name="option" value="LTCORA_AGREEMENT_MST">
<input type="hidden" name="buy_sale" value="<%=buy_sale%>">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="no_of_billing_dtl" value="<%=no_of_billing_dtl%>">

<input type="hidden" name="u" value="<%=u%>">
<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">

<input type="hidden" name="max_end_dt" value="<%=max_end_dt%>">
<input type="hidden" name="display_msg" value="<%=display_msg%>">

</form>
</body>
</html>