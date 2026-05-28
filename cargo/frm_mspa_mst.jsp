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
	
	var url = "frm_mspa_mst.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+"&u="+u+"&clearance="+clearance;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
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
	
	var chk_plant = document.forms[0].chk_plant;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var demmurage_rate = document.forms[0].demmurage_rate.value;
	
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	var no_of_liability_dtl = document.forms[0].no_of_liability_dtl.value;
	
	var alw_laytime_hrs = document.forms[0].alw_laytime_hrs.value;
	var alw_laytime_mns = document.forms[0].alw_laytime_mns.value;
	
	var msg="";
	var flag=true;
	
	if(counterparty_cd == "" || counterparty_cd == "0")
	{
		msg+="Select Counterparty!\n";
		flag=false;
	}
	if(trim(agmt_ref_no) == "")
	{
		msg+="Enter Agreement Ref#!\n";
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
		msg+="Enter Signing Date!\n";
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
	if(demmurage_rate == "" || demmurage_rate == null)
	{
		msg+="Enter Demurrage Rate!\n";
		flag=false;
	}
	if(alw_laytime_hrs == "" || alw_laytime_hrs == null)
	{
		msg+="Enter Allowed Lay Time Hour!\n";
		flag=false;
	}
	if(alw_laytime_mns == "" || alw_laytime_mns == null)
	{
		msg+="Enter Allowed Lay Time Minutes!\n";
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
					temp_msg += "Please fill-in the Billing Detail after Submitting Agreement Detail!\n";
				}
			}
			else
			{
				temp_msg += "Please fill-in the Billing Detail after Submitting Agreement Detail!!\n";
			}
			
			if(trim(no_of_liability_dtl) != "")
			{
				if(parseInt(no_of_liability_dtl) <= 0)
				{
					temp_msg += "Please fill-in the Liability Detail after Submitting Agreement Detail!\n";
				}
			}
			else
			{
				temp_msg += "Please fill-in the Liability Detail after Submitting Agreement Detail!\n";
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

var newWindow;
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_type = document.forms[0].agmt_type.value;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_msp_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agmt_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_msp_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agmt_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
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
	
	var url = "frm_mspa_liability_clause.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agmt_type+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+
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
	
	var url = "frm_mspa_agmt_billing_dtl.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agmt_type+
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

function setAgmtDetail(countpty_cd,agmt_no,agmt_rev_no,agmt_type)
{
	var opration = document.forms[0].opration.value;
	var clearance = document.forms[0].clearance.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_mspa_mst.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&agreement_type="+agmt_type+
			"&u="+u+"&clearance="+clearance;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setValues(strPlant,strBuPlant)
{
	var chk_plant = document.forms[0].chk_plant;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	
	var sepPlant = strPlant.split("@");
	var sepBuPlant = strBuPlant.split("@");

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

function revisionEffDateShows()
{
	const checkbox = document.getElementById("rev_chk");
	const div_std = document.getElementById("rev_chk_div");

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

function demurrageShows()
{
	const checkbox = document.getElementById("demmurageCheckbox");
	const div_std = document.getElementById("demurrage_div_std");
	const div_clause = document.getElementById("demurrage_clause_div");

	if (checkbox.checked) 
	{
		div_std.style.display = "block";
		div_clause.style.display = "block";
		
		document.getElementById("demmurageCheckbox").value="Y";
	} 
	else 
	{
		div_std.style.display = "none";
		div_clause.style.display = "none";
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
<jsp:useBean class="com.etrm.fms.cargo.DataBean_Cargo_mst" id="db_cargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String dateTime = utildate.getSysdateWithTime24hr();
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String clearance = request.getParameter("clearance")==null?"KYC":request.getParameter("clearance");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String agreement_type = request.getParameter("agreement_type")==null?"M":request.getParameter("agreement_type");

db_cargo.setCallFlag("MSPA_AGREEMENT_MST");
db_cargo.setClearance(clearance);
db_cargo.setCounterparty_cd(counterparty_cd);
db_cargo.setComp_cd(owner_cd);
db_cargo.setOpration(opration);
db_cargo.setAgmt_no(agmt_no);
db_cargo.setAgmt_rev_no(agmt_rev_no);
db_cargo.setAgreement_type(agreement_type);
db_cargo.init();

Vector VCOUNTERPARTY_CD = db_cargo.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = db_cargo.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = db_cargo.getVCOUNTERPARTY_ABBR();

Vector VPLANT_NM = db_cargo.getVPLANT_NM();
Vector VPLANT_ABBR = db_cargo.getVPLANT_ABBR();
Vector VPLANT_SEQ_NO = db_cargo.getVPLANT_SEQ_NO();
Vector VTRANS_CD = db_cargo.getVTRANS_CD();
Vector VTRANS_PLANT_NM = db_cargo.getVTRANS_PLANT_NM();
Vector VTRANS_PLANT_ABBR = db_cargo.getVTRANS_PLANT_ABBR();
Vector VTRANS_PLANT_SEQ_NO = db_cargo.getVTRANS_PLANT_SEQ_NO();
Vector VBU_CD = db_cargo.getVBU_CD();
Vector VBU_PLANT_NM = db_cargo.getVBU_PLANT_NM();
Vector VBU_PLANT_ABBR = db_cargo.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = db_cargo.getVBU_PLANT_SEQ_NO();

Vector VSEL_PLANT_SEQ_NO = db_cargo.getVSEL_PLANT_SEQ_NO();
Vector VSEL_TRANS_CD = db_cargo.getVSEL_TRANS_CD();
Vector VSEL_TRANS_PLANT_SEQ_NO = db_cargo.getVSEL_TRANS_PLANT_SEQ_NO();
Vector VSEL_SPLIT_VALUE = db_cargo.getVSEL_SPLIT_VALUE();
Vector VSEL_BU_CD = db_cargo.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = db_cargo.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_TRAD_CD = db_cargo.getVSEL_TRAD_CD();
Vector VSEL_PLANT_ABBR = db_cargo.getVSEL_PLANT_ABBR();
Vector VSEL_TRANS_PLANT_ABBR = db_cargo.getVSEL_TRANS_PLANT_ABBR();
Vector VSEL_BU_PLANT_ABBR = db_cargo.getVSEL_BU_PLANT_ABBR();

Vector VTEMP_TRANS_CD = db_cargo.getVTEMP_TRANS_CD();
Vector VTEMP_TRANS_ABBR = db_cargo.getVTEMP_TRANS_ABBR();

Vector VFORMULA_ID = db_cargo.getVFORMULA_ID();
Vector VFORMULA_NM = db_cargo.getVFORMULA_NM();

String min_counterparty_eff_dt = db_cargo.getMin_counterparty_eff_dt();

String signing_dt = db_cargo.getSigning_dt();
String signing_time = db_cargo.getSigning_time();
String ent_dt = db_cargo.getEnt_dt();
String ent_time = db_cargo.getEnt_time();
String start_dt = db_cargo.getStart_dt();
String end_dt = db_cargo.getEnd_dt();
String agmt_base = db_cargo.getAgmt_base();
String agmt_typ = db_cargo.getAgmt_typ();
String day_def_flag = db_cargo.getDay_def_flag();
String day_start_time = db_cargo.getDay_start_time();
String day_end_time = db_cargo.getDay_end_time();
String day_def_clause = db_cargo.getDay_def_clause();
String remark = db_cargo.getRemark();
String contpty_abbr = db_cargo.getContpty_abbr();
String agmt_name = db_cargo.getAgmt_name();
String agmt_ref_no =db_cargo.getAgmt_ref_no();

String status = db_cargo.getStatus();
String status_nm = db_cargo.getStatus_nm();
String bill_flag = db_cargo.getBilling_flag();
String billing_clause = db_cargo.getBilling_clause();
String rev_dt = db_cargo.getRev_dt();
String allowed_laytime_hrs = db_cargo.getAlw_laytime_hrs();
String allowed_laytime_msn = db_cargo.getAlw_laytime_mns();
String demurrage = db_cargo.getDemurrage();
String demurrage_rate = db_cargo.getDemurrage_rate();
String demurrage_clause = db_cargo.getDemurrage_clause();
String demurrage_rate_unit = db_cargo.getDemurrage_rate_unit();
String messurment_flag = db_cargo.getMessurment_flag();
String meas_clause = db_cargo.getMeas_clause();
String meas_std = db_cargo.getMeas_std();
String meas_temp = db_cargo.getMeas_temp();
String pressure_max_bar = db_cargo.getPressure_max_bar();
String pressure_min_bar = db_cargo.getPressure_min_bar();
String off_spec_gas_flag = db_cargo.getOff_spec_gas_flag();
String spec_gas_eng_base = db_cargo.getSpec_gas_eng_base();
String spec_cause = db_cargo.getSpec_clause();
String spec_max_eng = db_cargo.getSpec_max_eng();
String spec_min_eng = db_cargo.getSpec_min_eng();
String liability_flag = db_cargo.getLiability_flag();
String liability_clause = db_cargo.getLiability_clause();
String liability_lq_dmg = db_cargo.getLiability_lq_dmg();
String liability_take_pay = db_cargo.getLiability_take_pay();
String liability_makeup = db_cargo.getLiability_makeup();
String terminaton_flag = db_cargo.getTermination_flag();
String termination_clause = db_cargo.getTermination_clause();
String termination_planned = db_cargo.getTermination_planned();
String termination_forced = db_cargo.getTermination_forced();
String no_of_billing_dtl=db_cargo.getNo_of_billing_dtl();
String no_of_liability_dtl=db_cargo.getNo_of_liability_dtl();
String max_end_dt=db_cargo.getMax_end_dt();
String display_msg=db_cargo.getDisplay_msg();

if(agmt_base.equals("")){
	agmt_base="";
}
if(agmt_typ.equals("")){
	agmt_typ="";
}
if(day_start_time.equals("")){
	day_start_time="06:00";
}
if(day_end_time.equals("")){
	day_end_time="06:00";
}

if(ent_dt.equals(""))
{
	String split[] = dateTime.split(" ");
	ent_dt = split[0];
	ent_time = split[1];
}

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
String cp_name = db_cargo.getContpty_name();
String cp_abbr = db_cargo.getContpty_abbr();

String old_value="CP="+counterparty_cd+"#NAME="+cp_name+"#ABBR="+cp_abbr+"#AGMTNAME="+agmt_name+"#AGMTNO="+agmt_no+"#AGMTREFNO="+agmt_ref_no+"#AGMTTYPE="+agreement_type+"#SIGNDT="+signing_dt+"#SIGNTIME="+signing_time+
"#ENTDT="+ent_dt+"#ENTTIME="+ent_time+"#AGMTTYP="+agmt_typ+"#AGMTBASE="+agmt_base+"#STARTDT="+start_dt+"#ENDDT="+end_dt;

%>
<body onload="setValues('<%=strPlant%>','<%=strBuPlant%>');demurrageShows();messurmentShows();terminatorShows();off_spec_gasShows();billingShows();liabilityShows();<%if(opration.equals("MODIFY")){ %>revisionEffDateShows();<%} %>" >
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_CargoMaster">
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
					    	MSPA
					    </div>
					    <div class="btn-group">
						  <lable class="btn btn-outline-secondary btngrp <%if(opration.equals("INSERT")){%>btnactive<%}%>" onclick="refresh('INSERT','<%=clearance%>');"><i class="fa fa-plus-circle"></i>&nbsp;New</lable>
						  <lable class="btn btn-outline-secondary btngrp <%if(opration.equals("MODIFY")){%>btnactive<%}%>" onclick="refresh('MODIFY','<%=clearance%>');"><i class="fa fa-edit"></i>&nbsp;Modify</lable>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Trader<span class="s-red">*</span></b></label>
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
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Agreement</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="agmt_type" style="pointer-events: none;">
				    					<option value="M">MSPA</option>
				    				</select>
				    				<script>document.forms[0].agmt_type.value="<%=agreement_type%>"</script>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						
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
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="cont_name" value="<%=agmt_name%>" maxLength="50" readOnly>
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
				      				<input type="text" class="form-control form-control-sm" name="agmt_ref_no" value="<%=agmt_ref_no%>" maxLength="25" oninput="handleComma(this,'Agreement Ref#')">
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
				      				<input type="radio" name="agreement_type" value="0" <%if(agmt_typ.equals("0")){%>checked<%}%>>&nbsp;Term&nbsp;&nbsp;
				      				<input type="radio" name="agreement_type" value="1" <%if(agmt_typ.equals("1")){%>checked<%}%>>&nbsp;Spot&nbsp;&nbsp;
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
				      				<input type="radio" name="agreement_base" value="X" <%if(agmt_base.equals("X")){%>checked<%}%>>&nbsp;Ex-Ship&nbsp;&nbsp;
				      				<input type="radio" name="agreement_base" value="D" <%if(agmt_base.equals("D")){%>checked<%}%>>&nbsp;DES&nbsp;&nbsp;
				    				<input type="radio" name="agreement_base" value="B" <%if(agmt_base.equals("B")){%>checked<%}%>>&nbsp;Ex-Ship/DES&nbsp;&nbsp;
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
				    			<label class="form-label"><b>Trader(<%=contpty_abbr%>)-Plant/s<span class="s-red">*</span></b></label>
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
				    					<%= utilmsg.warningMessage("Please configure Plants for Selected Trader!")%>
				    				<%} %>
				      			</div>
				  			</div>
						</div>
					</div>
					<br>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Agreement Clauses</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<label class="form-label"><b>Allowed Lay Time<span class="s-red">*</span></b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-6 col-xs-6 col-md-6">
				    				<div class="input-group input-group-sm">
			      						<input type="text" class="form-control form-control-sm" name="alw_laytime_hrs" id="allowed_lay_time_hrs" value="<%=allowed_laytime_hrs %>" maxLength="3" autocomplete="off" onchange="adjustTimeHour()">
			      						<span class="input-group-text">Hour</span>
		      						</div>
				    			</div>
				    			<div class="col-sm-6 col-xs-6 col-md-6">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="alw_laytime_mns" id="allowed_lay_time_min"  value="<%=allowed_laytime_msn %>" maxLength="2" autocomplete="off" onchange="adjustTimeMin()">
			      						<span class="input-group-text">Min</span>
		      						</div>
				    			</div>
							</div>
			    		</div>
				    </div>
				   	<div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
				    		<div class="form-group row">
								<div class="col-auto">
			      					<label class="form-label"><b><input type="checkbox" class="form-check-input" name="demmurageCheckbox" id="demmurageCheckbox" value="Y" onchange="demurrageShows()" checked>&nbsp;Demurrage Rate<span class="s-red">*</span></b></label>
				  				</div>
				    			<div id="demurrage_clause_div" class="col">
				    				<input type="text" class="form-control form-control-sm" name="demurrage_clause_no" value="<%=demurrage_clause%>" placeholder="Clause#" maxLength="10">
				      			</div>
				  			</div>
				  		</div>
				  		<div id="demurrage_div_std" class="col-sm-4 col-xs-4 col-md-4">
				    		<div class="form-group row">
		      					<div class="col-sm-6 col-xs-6 col-md-6">
				      				<input type="text" class="form-control form-control-sm" name="demmurage_rate" id="demmurage_rate" value="<%=demurrage_rate%>" onblur="checkValuePrecision(this.value,'4','6','demmurage_rate');negNumber(this);">
		      					</div>
				    			<div class="col-sm-6 col-xs-6 col-md-6">
		      						<select class="form-select form-select-sm" name="demmurage_rate_unit" onchange="checkRateFormate(this);" <%if(opration.equals("MODIFY")) {%>style="pointer-events: none;"<%} %>>
		      							<!-- <option value="1">INR/Day</option> -->
		      							<option value="2">USD/Day</option>
		      						</select>
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
				    				<input type="text" class="form-control form-control-sm" name="day_def_clause_no" value="<%=day_def_clause %>" placeholder="Clause#" maxLength="10">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
				  			<div class="form-group row">
			    				<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="day_time_from" value="<%=day_start_time%>" maxLength="5" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col-1" align="center">
				    				<label class="form-label"><b>To</b></label>
				    			</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="day_time_to" value="<%=day_end_time%>" maxLength="5" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
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
								<div align="center" class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.infoMessage("<b>Billing details configuration is pending for Agreement.</b>")%>
								</div>
							</div>
						<%} %>
						<%if(no_of_liability_dtl.equals("0") || no_of_liability_dtl.equals("")){ %>
							<div class="row m-b-5">
								<div align="center" class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.infoMessage("<b>Liability details configuration is pending for Agreement.</b>")%>
								</div>
							</div>
						<%} %>
					<%} %>
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
</div>

<input type="hidden" name="option" value="MSP_AGREEMENT_MST">
<input type="hidden" name="old_value" value="<%=old_value%>">
<input type="hidden" name="prev_clearance" value="<%=clearance%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="status" value="<%=status%>">
<input type="hidden" name="no_of_billing_dtl" value="<%=no_of_billing_dtl%>">
<input type="hidden" name="no_of_liability_dtl" value="<%=no_of_liability_dtl%>">
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
<input type="hidden" name="max_end_dt" value="<%=max_end_dt%>">
<input type="hidden" name="display_msg" value="<%=display_msg%>">

<script>
</script>

</form>
</body>
</html>