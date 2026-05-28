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
	var contract_type = document.forms[0].contract_type.value;
	
	if(prev_clearance != clearance)
	{
		counterparty_cd="0";
		if(clearance=="IGX")
		{
			contract_type="I";
		}
	}
		
	var u = document.forms[0].u.value;
	
	var url = "frm_trader_contract_mst.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+"&u="+u+
			"&clearance="+clearance+"&contract_type="+contract_type;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function splitEnabled(obj)
{
	var chk_plant = document.forms[0].chk_plant;
	var count=parseInt("0");
	if(chk_plant!=null && chk_plant.length!=undefined)
 	{
  		for(var i=0;i<chk_plant.length;i++)
  		{
   			if(chk_plant[i].checked)
   			{
   				count++;
   			} 
  		} 
 	}
 	else if(chk_plant!=null)
 	{
   		if(chk_plant.checked)
     	{
   			count++;
   		} 
 	}
	
	if(count>1)
	{
		if(obj.checked)
		{
			alert("Split Trader cann't be selected with multiple Trader Plant Configured!");
			obj.checked=false;
			document.forms[0].split_enabled.value="N";
		}
	}
	else
	{
		if(obj.checked)
		{
			document.forms[0].split_enabled.value="Y";
			
			document.forms[0].split_type_flag.value="P";
			
			document.getElementById('other_trd').style.display="flex";
			
			document.getElementById('othTrdPlantListDis').style.display="";
			
			setPlantWiseSplitVal(true)
		}
		else
		{
			document.forms[0].split_enabled.value="N";
			
			document.forms[0].split_type_flag.value="";
			
			document.getElementById('other_trd').style.display="none";
			
			document.getElementById('othTrdPlantListDis').style.display="none";
			
			setPlantWiseSplitVal(false)
		}
	}
}

function setPlantWiseSplitVal(flag)
{
	var chk_plant = document.forms[0].chk_plant;
	
	var chk_oth_plant = document.forms[0].chk_oth_plant;
	var oth_plant_seq_no = document.forms[0].oth_plant_seq_no;
	var oth_trd_cd = document.forms[0].oth_trd_cd;
	
	if(chk_plant!=null && chk_plant.length!=undefined)
 	{
  		for(var i=0;i<chk_plant.length;i++)
  		{
  			var id='split_value_'+chk_plant[i].value;
   			if(chk_plant[i].checked)
   			{
   				document.getElementById(id).disabled=false;
   				if(!flag)
   				{
   					document.getElementById(id).value="";
   				}
   			} 
   			else
   			{
   				document.getElementById(id).disabled=true;
   				document.getElementById(id).value="";
   			}
   			
   			var id1='hid_col_split_'+chk_plant[i].value;
   			if(flag)
			{
   				document.getElementById(id1).style.display="";
			}
   			else
   			{
   				document.getElementById(id1).style.display="none";
   			}
  		} 
 	}
 	else if(chk_plant!=null)
 	{
 		var id='split_value_'+chk_plant.value;
		if(chk_plant.checked)
     	{
   			document.getElementById(id).disabled=false;
   			if(!flag)
			{
				document.getElementById(id).value="";
			}
		}
		else
		{
			document.getElementById(id).disabled=true;
			document.getElementById(id).value="";
		}
		
		var id1='hid_col_split_'+chk_plant.value;
		if(flag)
		{
			document.getElementById(id1).style.display="";
		}
		else
		{
			document.getElementById(id1).style.display="none";
		}
 	}
	
	//FOR OTHER TRADER
	if(chk_oth_plant!=null && chk_oth_plant.length!=undefined)
 	{
  		for(var i=0;i<chk_oth_plant.length;i++)
  		{
  			var id='oth_split_value_'+oth_trd_cd[i].value+"_"+oth_plant_seq_no[i].value;
			if(chk_oth_plant[i].checked)
   			{
				document.getElementById(id).disabled=false;	
				
				oth_plant_seq_no[i].disabled=false;
				oth_trd_cd[i].disabled=false;
				
				if(!flag)
   				{
					chk_oth_plant[i].checked=false;
   					document.getElementById(id).value="";
   				}
			}
			else
			{
				document.getElementById(id).disabled=true;
				
				oth_plant_seq_no[i].disabled=true;
				oth_trd_cd[i].disabled=true;
				document.getElementById(id).value="";
			}
			
			var id1='hid_col_oth_split_'+oth_trd_cd[i].value+"_"+oth_plant_seq_no[i].value;
   			if(flag)
			{
   				document.getElementById(id1).style.display="";
			}
   			else
   			{
   				document.getElementById(id1).style.display="none";
   			}
  		} 
 	}
 	else if(chk_oth_plant!=null)
 	{
 		var id='oth_split_value_'+oth_trd_cd.value+"_"+oth_plant_seq_no.value;
		if(chk_oth_plant.checked)
     	{
			document.getElementById(id).disabled=false;	
			
			oth_plant_seq_no.disabled=false;
			oth_trd_cd.disabled=false;
			
			if(!flag)
			{
				chk_oth_plant.checked=false;
				document.getElementById(id).value="";
			}
		}
		else
		{
			document.getElementById(id).disabled=true;
			
			oth_plant_seq_no.disabled=true;
			oth_trd_cd.disabled=true;
			document.getElementById(id).value="";
		}
   		
   		var id1='hid_col_oth_split_'+oth_trd_cd.value+"_"+oth_plant_seq_no.value;
		if(flag)
		{
			document.getElementById(id1).style.display="";
		}
		else
		{
			document.getElementById(id1).style.display="none";
		}
 	}
}

function setSplitTypeFlag(obj)
{
	if(obj.checked)
	{
		document.forms[0].split_type_flag.value=obj.value;
	}
}

function enabled_OthTrdPlantDtl(obj,id1,id2)
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
	var clearance = document.forms[0].clearance.value;
	
	var cont_ref_no = document.forms[0].cont_ref_no.value;
	var trade_ref_no = document.forms[0].trade_ref_no.value;
	
	var dda_dt = document.forms[0].dda_dt.value;
	var signing_dt = document.forms[0].signing_dt.value;
	var signing_time = document.forms[0].signing_time.value;
	var ent_dt = document.forms[0].ent_dt.value;
	var ent_time = document.forms[0].ent_time.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var rate = document.forms[0].rate.value;
	var rate_unit = document.forms[0].rate_unit.value;
	var tcq = document.forms[0].tcq.value;
	var quantity_unit = document.forms[0].quantity_unit.value;
	var dcq = document.forms[0].dcq.value;
	var cont_status_flg = document.forms[0].cont_status_flg.value;
	
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	var no_of_security_dtl = document.forms[0].no_of_security_dtl.value;
	var no_of_liability_dtl = document.forms[0].no_of_liability_dtl.value;
	
	var agreement_type = document.forms[0].agreement_type;
	var agreement_base = document.forms[0].agreement_base;
	var transportation_charges = document.forms[0].transportation_charges.value
	
	var split_enabled=document.forms[0].split_enabled.value
	
	var chk_plant = document.forms[0].chk_plant;
	var chk_oth_plant = document.forms[0].chk_oth_plant;
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
		var cont_no = document.forms[0].cont_no.value;
		if(cont_no == "" || cont_no == "0")
		{
			msg+="Select Contact!\n";
			flag=false;
		}
	}
	if (clearance=="KYC")
	{	
		if(trim(cont_ref_no) == "")
		{
			msg+="Enter Contract Ref#!\n";
			flag=false;
		}
	}	
	if (clearance=="IGX")
	{	
		if(trim(trade_ref_no) == "")
		{
			msg+="Enter Trade Ref#!\n";
			flag=false;
		}
	}	
	if(trim(dda_dt) == "")
	{
		msg+="Enter DDA Date!\n";
		flag=false;
	}
	if(trim(signing_dt) == "")
	{
		msg+="Enter Signing Date!\n";
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
	if(trim(rate) == "")
	{
		msg+="Enter Gas Price!\n";
		flag=false;
	}
	if(trim(rate_unit) == "")
	{
		msg+="Select Rate Unit!\n";
		flag=false;
	}
	if(trim(tcq) == "")
	{
		msg+="Enter TCQ!\n";
		flag=false;
	}
	if(trim(quantity_unit) == "")
	{
		msg+="Select Quantity Unit!\n";
		flag=false;
	}
	if(trim(dcq) == "")
	{
		msg+="Enter DCQ!\n";
		flag=false;
	}
	
	
	//FOR TRADER PLANT
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
		msg += "Please Select Atleast One Trader-Plant!\n";
		flag=false;
	}
	
	//FOR OTHER TRADER
	if(split_enabled=="Y")
	{
		chkFlg = false;
		if(chk_oth_plant!=null && chk_oth_plant!=undefined)
		{
			if(chk_oth_plant.length!=undefined)
			{
				for(var i=0;i<chk_oth_plant.length;i++)
				{
					if(chk_oth_plant[i].checked)
					{
						chkFlg = true;	
					}
				}
			}
			else
			{
				if(chk_oth_plant.checked)
				{
					chkFlg = true;	
				}
			}
		}
		if(chkFlg==false)
		{
			msg += "Please Select Atleast One Split Trader-Plant!\n";
			flag=false;
		}	
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
			a = confirm("Do you want to Modify Trader Contract?");
		}
		else
		{
			a = confirm("Do you want to Create New Trader Contract?");
		}
		if(a)
		{
			var temp_msg="";
			if(trim(no_of_billing_dtl) != "")
			{
				if(parseInt(no_of_billing_dtl) <= 0)
				{
					temp_msg += "Please fill-in the Billing Detail after Submitting Contract Detail!\n";
				}
			}
			else
			{
				temp_msg += "Please fill-in the Billing Detail after Submitting Contract Detail!!\n";
			}
			if(clearance=="KYC")
			{
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
			}
			if(trim(no_of_liability_dtl) != "")
			{
				if(parseInt(no_of_liability_dtl) <= 0)
				{
					temp_msg += "Please fill-in the Liability Detail after Submitting Contract Detail!\n";
				}
			}
			else
			{
				temp_msg += "Please fill-in the Liability Detail after Submitting Contract Detail!\n";
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
		newWindow = window.open("frm_trader_contract_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type,"Trader Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_trader_contract_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type,"Trader Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
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
	var clearance = document.forms[0].clearance.value;
	var start_dt = document.forms[0].start_dt.value;
	var rate_unit = document.forms[0].rate_unit.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_trader_billing_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
			"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+"&start_dt="+start_dt+
			"&u="+u+"&rate_unit="+rate_unit;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Trader Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Trader Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,contract_type)
{
	var opration = document.forms[0].opration.value;
	var clearance = document.forms[0].clearance.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_trader_contract_mst.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+
			"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
			"&u="+u+"&clearance="+clearance+"&contract_type="+contract_type;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setValues(strTransCd,strTrans,strPlant,strPlantChrg,strBuPlant,strGxBuPlant,strSplitVal,splitFlag,strOthTrdCd,strOthPlant,strOthPlantChrg,strOthSplitVal)
{
	var chk_plant = document.forms[0].chk_plant;
	var split_value = document.forms[0].split_value;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var chk_gx_bu_plant = document.forms[0].chk_gx_bu_plant;
	var trans_cd = document.forms[0].trans_cd;
	var trans_plant_seq_no = document.forms[0].trans_plant_seq_no;
	var trans_plant_abbr = document.forms[0].trans_plant_abbr;
	
	var chk_oth_plant = document.forms[0].chk_oth_plant;
	var oth_plant_seq_no = document.forms[0].oth_plant_seq_no;
	var oth_trd_cd = document.forms[0].oth_trd_cd;
	var oth_split_value = document.forms[0].oth_split_value;
	
	var chk_split=document.forms[0].chk_split;
	
	var charge_abbr = document.forms[0].charge_abbr;
	
	var sepTransCd = strTransCd.split("@");
	var sepTrans = strTrans.split("@");
	var sepPlant = strPlant.split("@");
	var sepPlantChrg = strPlantChrg.split("@");
	var sepBuPlant = strBuPlant.split("@");
	var sepGxBuPlant = strGxBuPlant.split("@");
	var sepSplitVal = strSplitVal.split("@");
	
	var sepOthTrdCd = strOthTrdCd.split("@");
	var sepOthPlant = strOthPlant.split("@");
	var sepOthPlantChrg = strOthPlantChrg.split("@");
	var sepOthSplitVal = strOthSplitVal.split("@");
	
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
     				
     				var temp_id='split_value_'+chk_plant[i].value;
     				document.getElementById(temp_id).disabled=false;
     				
     				split_value[i].value=sepSplitVal[j]; 
     				
     				var sepPlantChrgVal = sepPlantChrg[j].split("$$");
     				if(charge_abbr!=null && charge_abbr.length!=undefined)
     			 	{
	     				for(var k=0;k<charge_abbr.length;k++)
	     		  		{
	     					var tempPlantChrgVal = sepPlantChrgVal[k].split("#");
	     		  			var temp = document.getElementById("charge_abbr_"+k).value;
	     		  			//alert(temp)
	     		  			document.getElementById(temp+"_"+i+"_"+k).disabled=false;
	     		  			document.getElementById("eff_dt_"+temp+"_"+i+"_"+k).disabled=false;
	     		  			
	     		  			document.getElementById(temp+"_"+i+"_"+k).value=tempPlantChrgVal[0];
	     		  			document.getElementById("eff_dt_"+temp+"_"+i+"_"+k).value=tempPlantChrgVal[1];
	     		  		}
     			 	}
     				else if(charge_abbr!=null)
     			 	{
     					var tempPlantChrgVal = sepPlantChrgVal[0].split("#");
     					var temp = document.getElementById("charge_abbr_0").value;
     		  			//alert(temp)
     		  			document.getElementById(temp+"_"+i+"_0").disabled=false;
     		  			document.getElementById("eff_dt_"+temp+"_"+i+"_0").disabled=false;
     		  			
     		  			document.getElementById(temp+"_"+i+"_0").value=tempPlantChrgVal[0];
     		  			document.getElementById("eff_dt_"+temp+"_"+i+"_0").value=tempPlantChrgVal[1];
     			 	}
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
   				
   				var temp_id='split_value_'+chk_plant.value;
 				document.getElementById(temp_id).disabled=false;
 				
   				split_value.value=sepSplitVal[j];
   				
   				var sepPlantChrgVal = sepPlantChrg[j].split("$$");
   				if(charge_abbr!=null && charge_abbr.length!=undefined)
 			 	{
     				for(var k=0;k<charge_abbr.length;k++)
     		  		{
     					var tempPlantChrgVal = sepPlantChrgVal[k].split("#");
     		  			var temp = document.getElementById("charge_abbr_"+k).value;
     		  			//alert(temp)
     		  			document.getElementById(temp+"_0_"+k).disabled=false;
     		  			document.getElementById("eff_dt_"+temp+"_0_"+k).disabled=false;
     		  			
     		  			document.getElementById(temp+"_0_"+k).value=tempPlantChrgVal[0];
     		  			document.getElementById("eff_dt_"+temp+"_0_"+k).value=tempPlantChrgVal[1];
     		  		}
 			 	}
 				else if(charge_abbr!=null)
 			 	{
 					var tempPlantChrgVal = sepPlantChrgVal[0].split("#");
 					var temp = document.getElementById("charge_abbr_0").value;
 		  			//alert(temp)
 		  			document.getElementById(temp+"_0_0").disabled=false;
 		  			document.getElementById("eff_dt_"+temp+"_0_0").disabled=false;
 		  			
 		  			document.getElementById(temp+"_0_0").value=tempPlantChrgVal[0];
 		  			document.getElementById("eff_dt_"+temp+"_0_0").value=tempPlantChrgVal[1];
 			 	}
     		}
   		} 
 	}
	
	//FOR OTHER PLANT
	if(chk_oth_plant!=null && chk_oth_plant.length!=undefined)
 	{
  		for(var i=0;i<chk_oth_plant.length;i++)
  		{
   			for(var j=0;j<sepOthTrdCd.length;j++)
   			{
     			if(oth_plant_seq_no[i].value == sepOthPlant[j] && oth_trd_cd[i].value == sepOthTrdCd[j])
     			{
     				chk_oth_plant[i].checked = true;
     				
     				oth_split_value[i].value=sepOthSplitVal[j];
     				
     				var sepOthPlantChrgVal = sepOthPlantChrg[j].split("$$");
     				if(charge_abbr!=null && charge_abbr.length!=undefined)
     			 	{
	     				for(var k=0;k<charge_abbr.length;k++)
	     		  		{
	     					var tempOthPlantChrgVal = sepOthPlantChrgVal[k].split("#");
	     		  			var temp = document.getElementById("charge_abbr_"+k).value;
	     		  			//alert(temp+"==="+k)
	     		  			document.getElementById("oth_"+temp+"_"+i+"_"+k).disabled=false;
	     		  			document.getElementById("oth_eff_dt_"+temp+"_"+i+"_"+k).disabled=false;
	     		  			
	     		  			document.getElementById("oth_"+temp+"_"+i+"_"+k).value=tempOthPlantChrgVal[0];
	     		  			document.getElementById("oth_eff_dt_"+temp+"_"+i+"_"+k).value=tempOthPlantChrgVal[1];
	     		  		}
     			 	}
     				else if(charge_abbr!=null)
     			 	{
     					var tempOthPlantChrgVal = sepOthPlantChrgVal[0].split("#");
     					var temp = document.getElementById("charge_abbr_0").value;
     		  			//alert(temp+"==="+k)
     		  			document.getElementById("oth_"+temp+"_"+i+"_0").disabled=false;
     		  			document.getElementById("oth_eff_dt_"+temp+"_"+i+"_0").disabled=false;
     		  			
     		  			document.getElementById("oth_"+temp+"_"+i+"_0").value=tempOthPlantChrgVal[0];
     		  			document.getElementById("oth_eff_dt_"+temp+"_"+i+"_0").value=tempOthPlantChrgVal[1];
     			 	}
     			}
   			} 
  		} 
 	}
 	else if(chk_oth_plant!=null)
 	{
   		for(var j=0;j<sepOthTrdCd.length;j++)
   		{
   			if(oth_plant_seq_no.value == sepOthPlant[j] && oth_trd_cd.value == sepOthTrdCd[j])
     		{
   				chk_oth_plant.checked = true;
       			
       			oth_split_value.value=sepOthSplitVal[j];
       			
       			var sepOthPlantChrgVal = sepOthPlantChrg[j].split("$$");
       			if(charge_abbr!=null && charge_abbr.length!=undefined)
 			 	{
     				for(var k=0;k<charge_abbr.length;k++)
     		  		{
     					var tempOthPlantChrgVal = sepOthPlantChrgVal[k].split("#");
     		  			var temp = document.getElementById("charge_abbr_"+k).value;
     		  			//alert(temp+"==="+k)
     		  			document.getElementById("oth_"+temp+"_0_"+k).disabled=false;
     		  			document.getElementById("oth_eff_dt_"+temp+"_0_"+k).disabled=false;
     		  			
     		  			document.getElementById("oth_"+temp+"_0_"+k).value=tempOthPlantChrgVal[0];
     		  			document.getElementById("oth_eff_dt_"+temp+"_0_"+k).value=tempOthPlantChrgVal[1];
     		  		}
 			 	}
 				else if(charge_abbr!=null)
 			 	{
 					var tempOthPlantChrgVal = sepOthPlantChrgVal[0].split("#");
 					var temp = document.getElementById("charge_abbr_0").value;
 		  			//alert(temp+"==="+k)
 		  			document.getElementById("oth_"+temp+"_0_0").disabled=false;
 		  			document.getElementById("oth_eff_dt_"+temp+"_0_0").disabled=false;
 		  			
 		  			document.getElementById("oth_"+temp+"_0_0").value=tempOthPlantChrgVal[0];
 		  			document.getElementById("oth_eff_dt_"+temp+"_0_0").value=tempOthPlantChrgVal[1];
 			 	}
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
	
	//FOR GX BU PLANT
	if(chk_gx_bu_plant!=null && chk_gx_bu_plant.length!=undefined)
 	{
  		for(var i=0;i<chk_gx_bu_plant.length;i++)
  		{
   			for(var j=0;j<sepGxBuPlant.length;j++)
   			{
     			if(chk_gx_bu_plant[i].value == sepGxBuPlant[j])
     			{
     				chk_gx_bu_plant[i].checked = true;
     			}
   			} 
  		} 
  		if(strGxBuPlant == "")
   		{
   			chk_gx_bu_plant[0].checked = true;
   		}
 	}
 	else if(chk_gx_bu_plant!=null)
 	{
   		for(var j=0;j<sepGxBuPlant.length;j++)
   		{
   			if(chk_gx_bu_plant.value == sepGxBuPlant[j])
     		{
   				chk_gx_bu_plant.checked = true;
     		}
   		}
   		
   		if(strGxBuPlant == "")
   		{
   			chk_gx_bu_plant.checked = true;
   		}
 	}
	
	if(splitFlag=="Y")
	{
		chk_split.checked=true;
	}
	else
	{
		chk_split.checked=false;
	}
	splitEnabled(chk_split);
	doSubmitTradConfig('');
}

function showHide(flg)
{
	if(flg == "D")
	{
		document.getElementById("dlvTranChg").style.display="flex";
		document.getElementById("linkTrans").style.display="flex";
	}
	else
	{
		document.getElementById("dlvTranChg").style.display="none";
		document.getElementById("linkTrans").style.display="none";
		document.forms[0].transportation_charges.value="";
	}
}

function checkRateFormate(obj) //WHEN CHANGE RATE UNIT
{ 
	var a="0"
	var b="0"
	
	var rate = document.forms[0].rate;
	
	if(obj.value == "1")
	{
		a="6";
		b="2";
	}
	else
	{
		a="6";
		b="4";
	}
	
	rate.setAttribute("onblur","checkNumber1(this,"+a+","+b+");");
	
	var c = parseInt(a)-parseInt(b);
	var flag=true;
	
	var fieldValue=rate.value;
    
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
    
    if(rate.value!="" && rate.value!=null && rate.value!=' ')
    {
		if((parseInt(len) > parseInt(c)))
		{
    		alert("Please, Enter In the Required  Format.."+'('+c+' ,'+b+' )');
    		rate.value= "";
    		rate.select();
			flag = false;
		}
		else
		{
			var decallowed = b;  // how many decimals are allowed?
        
        	if(isNaN(fieldValue) || fieldValue == "")
        	{
        		alert("Please, Enter In the Required  Format.."+'('+c+' ,'+b+' )');
        		rate.value="";
        		rate.select();	 
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
		    		rate.value="";
		    		rate.select();		
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

function checkTxnRateFormate(obj)
{ 
	var a="0"
	var b="0"
	
	var rate = document.forms[0].txn_charges;
	
	if(obj.value == "1")
	{
		a="6";
		b="2";
	}
	else
	{
		a="6";
		b="4";
	}
	
	rate.setAttribute("onblur","checkNumber1(this,"+a+","+b+");");
	
	var c = parseInt(a)-parseInt(b);
	var flag=true;
	
	var fieldValue=rate.value;
    
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
    
    if(rate.value!="" && rate.value!=null && rate.value!=' ')
    {
		if((parseInt(len) > parseInt(c)))
		{
    		alert("Please, Enter In the Required  Format.."+'('+c+' ,'+b+' )');
    		rate.value= "";
    		rate.select();
			flag = false;
		}
		else
		{
			var decallowed = b;  // how many decimals are allowed?
        
        	if(isNaN(fieldValue) || fieldValue == "")
        	{
        		alert("Please, Enter In the Required  Format.."+'('+c+' ,'+b+' )');
        		rate.value="";
        		rate.select();	 
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
		    		rate.value="";
		    		rate.select();		
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

function addLinkTransporter()
{
	var LinkedTransTab = document.getElementById("LinkedTransTab").innerHTML;
	
	var dp_trd_plant = document.forms[0].dp_trd_plant.value
	var dp_trans_plant = document.forms[0].dp_trans_plant.value
	var dp_bu_plant = document.forms[0].dp_bu_plant.value
	//alert(LinkedTransTab);
	
	var msg="";
	var flag = true;
	if(dp_trd_plant == "0" || trim(dp_trd_plant) == "")
	{
		msg+="Select Trader Plant!\n";
		flag=false;
	}
	if(dp_trans_plant == "0" || trim(dp_trans_plant) == "") 
	{
		msg+="Select Transporter Delivery Point!\n";
		flag=false;
	}
	if(dp_bu_plant == "0" || trim(dp_bu_plant) == "")
	{
		msg+="Select Business Unit/Buyer Plant!\n";
		flag=false;
	}
	
	if(flag)
	{
		var formulaId = document.forms[0].formula_id;
		
		var formula=document.forms[0].dp_bu_plant.options[document.forms[0].dp_bu_plant.selectedIndex].text+" : "
			+document.forms[0].dp_trd_plant.options[document.forms[0].dp_trd_plant.selectedIndex].text+" - "
			+document.forms[0].dp_trans_plant.options[document.forms[0].dp_trans_plant.selectedIndex].text;
	
		var formula_id=document.forms[0].dp_bu_plant.value+"-"
			+document.forms[0].dp_trd_plant.value+"-"
			+document.forms[0].dp_trans_plant.value;
		
		var count = parseInt("0");
		
		if(formulaId!=null && formulaId!=undefined)
		{
			if(formulaId.length!=undefined)
			{
				for(var i=0; i<formulaId.length; i++)
				{
					if(formula_id == formula_id[i].value)
					{
						count++;
					}
				}
			}
			else
			{
				if(formula_id == formulaId.value)
				{
					count++;
				}
			}
		}
		
		if(parseInt(count)>0)
		{
			msg+="Formula already Exist Select another Formula!\n";
			flag=false;
		}
		
		if(flag)
		{
			var max_seq = document.forms[0].linkedTransSize.value;
			var new_seq_no = parseInt(max_seq)+1;
			var tab_name = document.getElementById("LinkedTransTab");
			var row_new = document.createElement("tr"); 
			row_new.id = 'row'+new_seq_no;
			
			var td01 = document.createElement("td");
			var div01 = document.createElement("DIV");
			div01.align='center';
			div01.appendChild(document.createTextNode(new_seq_no));
			td01.appendChild(div01);
			
			var td02 = document.createElement("td");
			var input02 = document.createElement("input")
			input02.name = "formula_id";
			input02.id = "formula_id"+new_seq_no;
			input02.type = "hidden";
			input02.value = formula_id;
			td02.appendChild(document.createTextNode(formula));
			td02.appendChild(input02);
			
			var td03 = document.createElement("td");
			td03.appendChild(document.createTextNode(""));
			
			row_new.appendChild(td01);
			row_new.appendChild(td02);
			row_new.appendChild(td03);
			
			tab_name.appendChild(row_new);
			document.forms[0].linkedTransSize.value=new_seq_no;
			
			document.forms[0].dp_trd_plant.value="0"
			document.forms[0].dp_trans_plant.value="0"
			document.forms[0].dp_bu_plant.value="0"
		}
		else
		{
			alert(msg);
		}
	}
	else
	{
		alert(msg);
	}
	
}

function doSubmitLinkTransporter()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	
	var formula_id = document.forms[0].formula_id;
	
	var flag = true;
	var msg = "";
	
	if(counterparty_cd == "" || counterparty_cd == "0")
	{
		msg+="Select Counterparty!\n";
		flag=false;
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
	
	if(formula_id!=null && formula_id!=undefined)
	{
		if(formula_id.length!=undefined)
		{
			for(var i=0; i<formula_id.length; i++)
			{
				if(formula_id[i].value == "" || trim(formula_id[i].value) == "")
				{
					flag=false;
					msg+="Please Select Formula for ROW-"+(parseInt(i)+1)+"\n";
				}
			}
		}
		else
		{
			if(formula_id.value == "" || trim(formula_id.value) == "")
			{
				flag=false;
				msg+="Please Select Formula\n";
			}
		}
	}
	else
	{
		flag=false;
		msg+="Please Select atleast ONE(1) Formula!\n";
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Add Link Transporter?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].option.value="TRADER_CONT_LINK_TRANS"
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
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
	
	if(trim(cont_type)!="" && (trim(cont)!="" && cont!="0") 
			&& (trim(counterparty_cd)!="" && counterparty_cd!="0")
			&& trim(sec_category)!="")
	{
		var url = "../credit_risk/frm_pre_receipt_security.jsp?counterparty_cd="+counterparty_cd+"&cont_no="+cont+"&cont_rev_no="+cont_rev+
			"&agmt_no="+agmt+"&agmt_rev_no="+agmt_rev+"&contract_type="+cont_type+"&sec_category="+sec_category+"&start_dt="+temp_start_dt+"&end_dt="+temp_end_dt+"&u="+u;
		
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
		if (trim(sec_category)=="")
		{
			alert("Select Security Category !")
		}	
		else
		{	
			alert("Select Contract Detail!")
		}	
	}
}

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

function checkTCQQty()
{
	var tcq = document.forms[0].tcq.value;
	var temp_tcq = document.forms[0].temp_tcq.value;
	var received_qty = document.forms[0].received_qty.value;
	
	if(trim(received_qty) != "" && trim(tcq) != "")
	{
		if(parseFloat(tcq) > 0)
		{
			if(parseFloat(tcq) < parseFloat(received_qty))
			{
				alert("TCQ MMBTU should be greater or equal to Received MMBTU!");
				document.forms[0].tcq.value=temp_tcq;
			}
		}
	}
}

function doSubmitTradConfig(submit_flag)
{
	var chk_plant = document.forms[0].chk_plant;
	var split_value = document.forms[0].split_value;
	var plant_abbr = document.forms[0].plant_abbr;
	
	var chk_oth_plant = document.forms[0].chk_oth_plant;
	var oth_plant_seq_no = document.forms[0].oth_plant_seq_no;
	var oth_trd_cd = document.forms[0].oth_trd_cd;
	var oth_plant_abbr = document.forms[0].oth_plant_abbr;
	var oth_split_value = document.forms[0].oth_split_value;
	
	var split_enabled = document.forms[0].split_enabled.value;
	
	var charge_abbr = document.forms[0].charge_abbr;
	var charge_nm = document.forms[0].charge_nm;
	
	var count_percent=parseFloat("0");
	
	var msg="";
	var flag=true;
	
	var display ="";
	
	var countTrdPlant=parseInt("0");
	
	if(chk_plant!=null && chk_plant.length!=undefined)
 	{
  		for(var i=0;i<chk_plant.length;i++)
  		{
   			if(chk_plant[i].checked)
   			{
   				countTrdPlant+=1;
   				
   				if(display!="")
   				{
   					display+="<br>"+plant_abbr[i].value;
   				}
   				else
   				{
   					display+=plant_abbr[i].value;
   				}
   				
   				if(split_value[i].value!="")
   				{
   					display+=" <font style='background:#ff99ff'>("+split_value[i].value+"%)</font>";
   					
   					count_percent=parseFloat(count_percent) + parseFloat(split_value[i].value);
   				}
   				else
   				{
   					msg+="Enter Percentage for "+plant_abbr[i].value+"!\n";
   					flag=false;
   				}
   				
   				if(charge_abbr!=null && charge_abbr.length!=undefined)
   			 	{
   			  		for(var j=0;j<charge_abbr.length;j++)
   			  		{
   			  			var temp = document.getElementById("charge_abbr_"+j).value;
   			  			var temp_nm = document.getElementById("charge_nm_"+j).value;
   			  			//alert(temp)
   			  			var rate = document.getElementById(temp+"_"+i+"_"+j).value;
   			  			var eff = document.getElementById("eff_dt_"+temp+"_"+i+"_"+j).value;
   			  			
   			  			if(rate != "" && eff != "")
   			  			{
							display+=" <font style='background:#99ffcc'>"+temp_nm+" "+rate+"(eff. "+eff+")</font>";
   			  			}
   			  		} 
   			 	}
   			 	else if(charge_abbr!=null)
   			 	{
   			 		var temp = document.getElementById("charge_abbr_0").value;
   			 		var temp_nm = document.getElementById("charge_nm_0").value;
		  			//alert(temp)
		  			var rate = document.getElementById(temp+"_"+i+"_0").value;
		  			var eff = document.getElementById("eff_dt_"+temp+"_"+i+"_0").value;
		  			
		  			if(rate != "" && eff != "")
			  		{
		  				display+=" <font style='background:#99ffcc'>"+temp_nm+" "+rate+"(eff. "+eff+")</font>";
			  		}
   			 	}
   			} 
  		} 
 	}
 	else if(chk_plant!=null)
 	{
   		if(chk_plant.checked)
     	{
   			countTrdPlant+=1;
   			
   			if(display!="")
			{
				display+="<br>"+plant_abbr.value;
			}
			else
			{
				display+=plant_abbr.value;
			}
   			
   			if(split_value.value!="")
			{
				display+=" <font style='background:#ff99ff'>("+split_value.value+"%)</font>";
				
				count_percent=parseFloat(count_percent) + parseFloat(split_value.value);
			}
   			else
			{
				msg+="Enter Percentage for "+plant_abbr.value+"!\n";
				flag=false;
			}
   			
   			if(charge_abbr!=null && charge_abbr.length!=undefined)
		 	{
		  		for(var j=0;j<charge_abbr.length;j++)
		  		{
		  			var temp = document.getElementById("charge_abbr_"+j).value;
		  			var temp_nm = document.getElementById("charge_nm_"+j).value;
		  			//alert(temp)
		  			var rate = document.getElementById(temp+"_0_"+j).value;
		  			var eff = document.getElementById("eff_dt_"+temp+"_0_"+j).value;
		  			
		  			if(rate != "" && eff != "")
			  		{
		  				display+=" <font style='background:#99ffcc'>"+temp_nm+" "+rate+"(eff. "+eff+")</font>";
			  		}
		  		} 
		 	}
		 	else if(charge_abbr!=null)
		 	{
		 		var temp = document.getElementById("charge_nm_0").value;
		 		var temp_nm = document.getElementById("charge_nm_0").value;
	  			//alert(temp)
	  			var rate = document.getElementById(temp+"_0_0").value;
	  			var eff = document.getElementById("eff_dt_"+temp+"_0_0").value;
	  			
	  			if(rate != "" && eff != "")
		  		{
	  				display+=" <font style='background:#99ffcc'>"+temp_nm+" "+rate+"(eff. "+eff+")</font>";
		  		}
	  		}
   		} 
 	}
	
	if(countTrdPlant<=0)
	{
		msg += "Please Select Atleast One Trader-Plant!\n";
		flag=false;
	}
	
	//FOR OTHER TRADER
	var display1="";
	var countOthTrdPlant=parseInt("0");
	
	if(chk_oth_plant!=null && chk_oth_plant.length!=undefined)
 	{
  		for(var i=0;i<chk_oth_plant.length;i++)
  		{
  			if(chk_oth_plant[i].checked)
   			{
  				countOthTrdPlant+=1;
  				
  				if(display1!="")
   				{
  					display1+="<br>"+oth_plant_abbr[i].value;
   				}
   				else
   				{
   					display1+=oth_plant_abbr[i].value;
   				}
   				
   				if(oth_split_value[i].value!="")
   				{
   					display1+=" <font style='background:#ff99ff'>("+oth_split_value[i].value+"%)</font>";
   					
   					count_percent=parseFloat(count_percent) + parseFloat(oth_split_value[i].value);
   				}
   				else
   				{
   					msg+="Enter Percentage for "+oth_plant_abbr[i].value+"!\n";
   					flag=false;
   				}
   				
   				if(charge_abbr!=null && charge_abbr.length!=undefined)
   			 	{
   			  		for(var j=0;j<charge_abbr.length;j++)
   			  		{
   			  			var temp = document.getElementById("charge_abbr_"+j).value;
   			  			var temp_nm = document.getElementById("charge_nm_"+j).value;
   			  			//alert(temp)
   			  			var rate = document.getElementById("oth_"+temp+"_"+i+"_"+j).value;
   			  			var eff = document.getElementById("oth_eff_dt_"+temp+"_"+i+"_"+j).value;
   			  			
   			  			if(rate != "" && eff != "")
   			  			{
							display1+=" <font style='background:#99ffcc'>"+temp_nm+" "+rate+"(eff. "+eff+")</font>";
   			  			}
   			  		} 
   			 	}
   			 	else if(charge_abbr!=null)
   			 	{
   			 		var temp = document.getElementById("charge_abbr_0").value;
   			 		var temp_nm = document.getElementById("charge_nm_0").value;
		  			//alert(temp)
		  			var rate = document.getElementById("oth_"+temp+"_"+i+"_0").value;
		  			var eff = document.getElementById("oth_eff_dt_"+temp+"_"+i+"_0").value;
		  			
		  			if(rate != "" && eff != "")
			  		{
		  				display1+=" <font style='background:#99ffcc'>"+temp_nm+" "+rate+"(eff. "+eff+")</font>";
			  		}
   			 	}
   			}
  		} 
 	}
 	else if(chk_oth_plant!=null)
 	{
 		if(chk_oth_plant.checked)
     	{
 			countOthTrdPlant+=1;
 			
 			if(display1!="")
			{
				display1+="<br>"+oth_plant_abbr.value;
			}
			else
			{
				display1+=oth_plant_abbr.value;
			}
			
			if(oth_split_value.value!="")
			{
				display1+=" <font style='background:#ff99ff'>("+oth_split_value.value+"%)</font>";
				
				count_percent=parseFloat(count_percent) + parseFloat(oth_split_value.value);
			}
			else
			{
				msg+="Enter Percentage for "+oth_plant_abbr[i].value+"!\n";
				flag=false;
			}
			
			if(charge_abbr!=null && charge_abbr.length!=undefined)
		 	{
		  		for(var j=0;j<charge_abbr.length;j++)
		  		{
		  			var temp = document.getElementById("charge_abbr_"+j).value;
		  			var temp_nm = document.getElementById("charge_nm_"+j).value;
		  			//alert(temp)
		  			var rate = document.getElementById("oth_"+temp+"_0_"+j).value;
		  			var eff = document.getElementById("oth_eff_dt_"+temp+"_0_"+j).value;
		  			
		  			if(rate != "" && eff != "")
		  			{
					display1+=" <font style='background:#99ffcc'>"+temp_nm+" "+rate+"(eff. "+eff+")</font>";
		  			}
		  		} 
		 	}
		 	else if(charge_abbr!=null)
		 	{
		 		var temp = document.getElementById("charge_abbr_0").value;
		 		var temp_nm = document.getElementById("charge_nm_0").value;
  				//alert(temp)
  				var rate = document.getElementById("oth_"+temp+"_0_0").value;
  				var eff = document.getElementById("oth_eff_dt_"+temp+"_0_0").value;
  			
  				if(rate != "" && eff != "")
	  			{
  					display1+=" <font style='background:#99ffcc'>"+temp_nm+" "+rate+"(eff. "+eff+")</font>";
	  			}
		 	}
   		}
 	}
	
	if(countOthTrdPlant<=0)
	{
		msg += "Please Select Atleast One Split Trader-Plant!\n";
		flag=false;
	}
	
	if(countTrdPlant<=0 && submit_flag=="P")
	{
		alert("Please Select Atleast One Trader-Plant!");
	}
	else if(!flag && split_enabled=="Y" && submit_flag=="P")
	{
		alert(msg);	
	}
	else
	{
		if(split_enabled=="Y" && count_percent!=100)
		{
			alert("Total Percentage : "+count_percent+"\n\nThe Total Percentage should be equal to 100%!");	
		}
		else
		{
			document.getElementById("tradDisplay").innerHTML=display;
			
			if(display != "")
			{
				document.getElementById("tradDisplay").style.display="inline";
			//	document.getElementById("tansAlert").style.display="none";
			}
			
			document.getElementById("OthtradPlantDisplay").innerHTML=display1;
			
			if(display1 != "")
			{
				document.getElementById("OthtradPlantDisplay").style.display="inline";
			//	document.getElementById("tansAlert").style.display="none";
			}
			
			if(submit_flag=="P")
			{
				alert("Proceed and Submit Contract!");	
			}
			$("#TradConfigModal").modal("hide");
		}
	}
}

function enableSplit(obj,id,index,flag)
{
	var charge_abbr = document.forms[0].charge_abbr;
	if(obj.checked)
	{
		document.getElementById(id).disabled=false;
		
		if(charge_abbr!=null && charge_abbr.length!=undefined)
	 	{
	  		for(var i=0;i<charge_abbr.length;i++)
	  		{
	  			var temp = document.getElementById("charge_abbr_"+i).value;
	  			//alert(temp)
	  			if(flag=="oth")
	  			{
	  				document.getElementById("oth_"+temp+"_"+index+"_"+i).disabled=false;
	  				document.getElementById("oth_eff_dt_"+temp+"_"+index+"_"+i).disabled=false;
				}
	  			else
	  			{
	  				document.getElementById(temp+"_"+index+"_"+i).disabled=false;
	  				document.getElementById("eff_dt_"+temp+"_"+index+"_"+i).disabled=false;
	  			}
	  		} 
	 	}
	 	else if(charge_abbr!=null)
	 	{
	 		var temp = document.getElementById("charge_abbr_0").value;
  			//alert(temp)
  			if(flag=="oth")
  			{
  				document.getElementById("oth_"+temp+"_"+index+"_0").disabled=false;
  				document.getElementById("oth_eff_dt_"+temp+"_"+index+"_0").disabled=false;
			}
  			else
  			{
				document.getElementById(temp+"_"+index+"_0").disabled=false;
  				document.getElementById("eff_dt_"+temp+"_"+index+"_0").disabled=false;
  			}
	 	}
	}
	else
	{
		document.getElementById(id).disabled=true;
		
		if(charge_abbr!=null && charge_abbr.length!=undefined)
	 	{
	  		for(var i=0;i<charge_abbr.length;i++)
	  		{
	  			var temp = document.getElementById("charge_abbr_"+i).value;
	  			//alert(temp)
	  			if(flag=="oth")
	  			{
	  				document.getElementById("oth_"+temp+"_"+index+"_"+i).disabled=true;
	  				document.getElementById("oth_eff_dt_"+temp+"_"+index+"_"+i).disabled=true;
				}
	  			else
	  			{
	  				document.getElementById(temp+"_"+index+"_"+i).disabled=true;
	  				document.getElementById("eff_dt_"+temp+"_"+index+"_"+i).disabled=true;
	  			}
	  		} 
	 	}
	 	else if(charge_abbr!=null)
	 	{
	 		var temp = document.getElementById("charge_abbr_0").value;
  			//alert(temp)
  			if(flag=="oth")
  			{
  				document.getElementById("oth_"+temp+"_"+index+"_0").disabled=true;
  				document.getElementById("oth_eff_dt_"+temp+"_"+index+"_0").disabled=true;
			}
  			else
  			{
				document.getElementById(temp+"_"+index+"_0").disabled=true;
  				document.getElementById("eff_dt_"+temp+"_"+index+"_0").disabled=true;
  			}
	 	}
	}
}


var priceWindow;
function variablePriceConfig(cont_type,agmt,agmt_rev,cont,cont_rev,cont_ref,cont_status)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var temp_start_dt = document.forms[0].temp_start_dt.value;
	var temp_end_dt = document.forms[0].temp_end_dt.value;
	
	var u = document.forms[0].u.value;
	
	if(trim(cont_type)!="" && (trim(cont)!="" && cont!="0") && (trim(counterparty_cd)!="" && counterparty_cd!="0"))
	{
		var url = "../market_risk/frm_config_price_mst.jsp?counterparty_cd="+counterparty_cd+"&cont_no="+cont+"&cont_rev_no="+cont_rev+"&cont_ref="+cont_ref+
			"&agmt_no="+agmt+"&agmt_rev_no="+agmt_rev+"&contract_type="+cont_type+"&start_dt="+temp_start_dt+"&end_dt="+temp_end_dt+"&cont_status="+cont_status+
			"&u="+u;
		
		if(!priceWindow || priceWindow.closed)
		{
			priceWindow = window.open(url,"Variable Price Config","top=10,left=100,width=1200,height=600,scrollbars=1");
		}
		else
		{
			priceWindow.close();
			priceWindow = window.open(url,"Variable Price Config","top=10,left=100,width=1200,height=600,scrollbars=1");
		}
	}
	else
	{
		alert("Select Contract Detail!")
	}
}

function openLiabilityDtl()
{
	var opration = document.forms[0].opration.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	var contract_type = document.forms[0].contract_type.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var rate = document.forms[0].rate.value;
	var rate_unit = document.forms[0].rate_unit.value;
	var tcq = document.forms[0].tcq.value;
	var dcq = document.forms[0].dcq.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_trader_cont_liability_clause.jsp?counterparty_cd="+counterparty_cd+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+"&contract_type="+contract_type+"&tcq="+tcq+"&dcq="+dcq+
			"&end_dt="+end_dt+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&rate="+rate+"&rate_unit="+rate_unit+"&u="+u;
	
	
	if(cont_no == "" || cont_no == "0")
	{
		alert("Please Create or Select Contract!");
	}
	else
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Trader Cont Liability Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Trader Cont Liability Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function messurmentShows()
{
	const checkbox = document.getElementById("measurementCheckbox");
	const div_std = document.getElementById("mesurement_div_std");
	const div_std1 = document.getElementById("measurement_clause_div");

	if (checkbox.checked) 
	{
		div_std.style.display = "block";
		div_std1.style.display = "block";
		document.getElementById("measurementCheckbox").value="Y";
	} 
	else 
	{
		div_std.style.display = "none";
		div_std1.style.display = "none";
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
	const div_std = document.getElementById("off_spec_gas_div");
	const div_std1 = document.getElementById("off_spec_clause_div");
	
	if (checkbox.checked) 
	{
		div_std.style.display = "block";
		div_std1.style.display = "block";
		
		document.getElementById("off_spec_gas_checkbox").value="Y";
	} 
	else 
	{
		div_std.style.display = "none";
		div_std1.style.display = "none";
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
						alert("Provided value should be in ("+maxVal+", "+precision+") format!!");
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
</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DataBean_Trader_Contract_Mst" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
<%
String dateTime = utildate.getSysdateWithTime24hr();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String clearance = request.getParameter("clearance")==null?"KYC":request.getParameter("clearance");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");

String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
/* if(contract_type.equals("")){
	if(clearance.equals("IGX")){
		contract_type="I";
	}else if(clearance.equals("KYC")){
		contract_type="D";
	}
} */

purchase.setCallFlag("TRADER_CONT_MST");
purchase.setClearance(clearance);
purchase.setCounterparty_cd(counterparty_cd);
purchase.setComp_cd(owner_cd);
purchase.setOpration(opration);
purchase.setAgmt_no(agmt_no);
purchase.setAgmt_rev_no(agmt_rev_no);
purchase.setCont_no(cont_no);
purchase.setCont_rev_no(cont_rev_no);
purchase.setContract_type(contract_type);
purchase.init();

Vector VCOUNTERPARTY_CD = purchase.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = purchase.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = purchase.getVCOUNTERPARTY_ABBR();
Vector VOTH_COUNTERPARTY_CD = purchase.getVOTH_COUNTERPARTY_CD();
Vector VOTH_COUNTERPARTY_NM = purchase.getVOTH_COUNTERPARTY_NM();
Vector VOTH_COUNTERPARTY_ABBR = purchase.getVOTH_COUNTERPARTY_ABBR();

Vector VPLANT_NM = purchase.getVPLANT_NM();
Vector VPLANT_ABBR = purchase.getVPLANT_ABBR();
Vector VPLANT_SEQ_NO = purchase.getVPLANT_SEQ_NO();
Vector VOTH_PLANT_NM = purchase.getVOTH_PLANT_NM();
Vector VOTH_PLANT_ABBR = purchase.getVOTH_PLANT_ABBR();
Vector VOTH_PLANT_SEQ_NO = purchase.getVOTH_PLANT_SEQ_NO();
Vector VTRANS_CD = purchase.getVTRANS_CD();
Vector VTRANS_PLANT_NM = purchase.getVTRANS_PLANT_NM();
Vector VTRANS_PLANT_ABBR = purchase.getVTRANS_PLANT_ABBR();
Vector VTRANS_PLANT_SEQ_NO = purchase.getVTRANS_PLANT_SEQ_NO();
Vector VBU_CD = purchase.getVBU_CD();
Vector VBU_PLANT_NM = purchase.getVBU_PLANT_NM();
Vector VBU_PLANT_ABBR = purchase.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = purchase.getVBU_PLANT_SEQ_NO();

Vector VGX_BU_CD = purchase.getVGX_BU_CD();
Vector VGX_BU_PLANT_NM = purchase.getVGX_BU_PLANT_NM();
Vector VGX_BU_PLANT_ABBR = purchase.getVGX_BU_PLANT_ABBR();
Vector VGX_BU_PLANT_SEQ_NO = purchase.getVGX_BU_PLANT_SEQ_NO();

Vector VSEL_GX_BU_CD = purchase.getVSEL_GX_BU_CD();
Vector VSEL_GX_BU_PLANT_SEQ_NO = purchase.getVSEL_GX_BU_PLANT_SEQ_NO();

Vector VSEL_PLANT_SEQ_NO = purchase.getVSEL_PLANT_SEQ_NO();
Vector VSEL_TRANS_CD = purchase.getVSEL_TRANS_CD();
Vector VSEL_TRANS_PLANT_SEQ_NO = purchase.getVSEL_TRANS_PLANT_SEQ_NO();
Vector VSEL_SPLIT_VALUE = purchase.getVSEL_SPLIT_VALUE();
Vector VSEL_CHARGE_VALUE = purchase.getVSEL_CHARGE_VALUE();
Vector VSEL_BU_CD = purchase.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = purchase.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_TRAD_CD = purchase.getVSEL_TRAD_CD();
Vector VSEL_PLANT_ABBR = purchase.getVSEL_PLANT_ABBR();
Vector VSEL_TRANS_PLANT_ABBR = purchase.getVSEL_TRANS_PLANT_ABBR();
Vector VSEL_BU_PLANT_ABBR = purchase.getVSEL_BU_PLANT_ABBR();

Vector VOTH_SEL_TRAD_CD = purchase.getVOTH_SEL_TRAD_CD();
Vector VOTH_SEL_PLANT_SEQ_NO = purchase.getVOTH_SEL_PLANT_SEQ_NO();
Vector VOTH_SEL_PLANT_ABBR = purchase.getVOTH_SEL_PLANT_ABBR();
Vector VOTH_SEL_SPLIT_VALUE = purchase.getVOTH_SEL_SPLIT_VALUE();
Vector VOTH_SEL_CHARGE_VALUE = purchase.getVOTH_SEL_CHARGE_VALUE();

Vector VTEMP_TRANS_CD = purchase.getVTEMP_TRANS_CD();
Vector VTEMP_TRANS_ABBR = purchase.getVTEMP_TRANS_ABBR();

Vector VFORMULA_ID = purchase.getVFORMULA_ID();
Vector VFORMULA_NM = purchase.getVFORMULA_NM();

Vector VINDEX = purchase.getVINDEX();

Vector VCHARGE_ABBR = purchase.getVCHARGE_ABBR();
Vector VCHARGE_NAME = purchase.getVCHARGE_NAME();

String min_counterparty_eff_dt = purchase.getMin_counterparty_eff_dt();

//String cont_no = purchase.getCont_no();
//String cont_rev_no = purchase.getCont_rev_no();
String cont_ref_no = purchase.getCont_ref_no();
String trade_ref_no = purchase.getTrade_ref_no();
String signing_dt = purchase.getSigning_dt();
String signing_time = purchase.getSigning_time();
String dda_dt = purchase.getDda_dt();
String dda_time = purchase.getDda_time();
String ent_dt = purchase.getEnt_dt();
String ent_time = purchase.getEnt_time();
String start_dt = purchase.getStart_dt();
String end_dt = purchase.getEnd_dt();
String agmt_base = purchase.getAgmt_base();
String agmt_type = purchase.getAgmt_type();
String tcq = purchase.getTcq();
String dcq = purchase.getDcq();
String quantity_unit = purchase.getQuantity_unit();
String rate = purchase.getRate();
String rate_unit = purchase.getRate_unit();
String post_margin = purchase.getPost_margin();
String transportation_charges = purchase.getTransportation_charges();
String split_flag = purchase.getSplit_flag();
String split_type = purchase.getSplit_type();
String buy_nom_flag = purchase.getBuy_nom_flag();
String buy_month_nom = purchase.getBuy_month_nom();
String buy_fortnightly_nom = purchase.getBuy_fortnightly_nom();
String buy_week_nom = purchase.getBuy_week_nom();
String buy_daily_nom = purchase.getBuy_daily_nom();
String sell_nom_flag = purchase.getSell_nom_flag();
String sell_month_nom = purchase.getSell_month_nom();
String sell_fortnightly_nom = purchase.getSell_fortnightly_nom();
String sell_week_nom = purchase.getSell_week_nom();
String sell_daily_nom = purchase.getSell_daily_nom();
String day_def_flag = purchase.getDay_def_flag();
String day_start_time = purchase.getDay_start_time();
String day_end_time = purchase.getDay_end_time();
String mdcq_flag = purchase.getMdcq_flag();
String mdcq_percentage = purchase.getMdcq_percentage();
String remark = purchase.getRemark();
String cont_name = purchase.getCont_name();
String dealMapping = purchase.getDealMapping();
String contpty_abbr = purchase.getContpty_abbr();
String fcc_flg = purchase.getFcc_flg();
String cont_status_flg = purchase.getCont_status_flg();
String cont_status = purchase.getCont_status();
String txn_charges = purchase.getTxn_charges();
String txn_unit = purchase.getTxn_unit();
String no_of_billing_dtl=purchase.getNo_of_billing_dtl();
String no_of_security_dtl=purchase.getNo_of_security_dtl();
String received_qty=purchase.getReceived_qty();
String price_change_history=purchase.getPrice_change_history();
String no_of_liability_dtl=purchase.getNo_of_liability_dtl();
String gx_counterparty_cd = purchase.getGx_counterparty_cd();
String liability_lq_dmg = purchase.getLiability_lq_dmg();
String liability_take_pay = purchase.getLiability_take_pay();
String liability_makeup = purchase.getLiability_makeup();

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
if(post_margin.equals("")){
	post_margin="20";
}
if(txn_charges.equals("")){
	txn_charges="4";
}
if(agmt_base.equals("")){
	agmt_base="X";
}
if(agmt_type.equals("")){
	agmt_type="0";
}
if(split_flag.equals("")){
	split_flag="N";
}
if(day_start_time.equals("")){
	day_start_time="06:00";
}
if(day_end_time.equals("")){
	day_end_time="06:00";
}
/* if(rate_unit.equals("")){
	rate_unit="1";
} */
if(txn_unit.equals("")){
	txn_unit="1";
}
if(quantity_unit.equals("")){
	quantity_unit="1";
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
String strPlantChrg="";
String strSplitVal="";
String strBuPlant="";
String strGxBuPlant="";

String strOthTrdCd="";
String strOthPlant="";
String strOthPlantChrg="";
String strOthSplitVal="";
for(int i=0;i<VSEL_TRANS_CD.size();i++)
{
	strTransCd = strTransCd + VSEL_TRANS_CD.elementAt(i)+"@";
	strTrans = strTrans + VSEL_TRANS_PLANT_SEQ_NO.elementAt(i)+"@";
}
for(int i=0;i<VSEL_PLANT_SEQ_NO.size();i++)
{
	strPlant = strPlant + VSEL_PLANT_SEQ_NO.elementAt(i)+"@";
	strPlantChrg = strPlantChrg + VSEL_CHARGE_VALUE.elementAt(i)+"@";
	strSplitVal = strSplitVal + VSEL_SPLIT_VALUE.elementAt(i)+"@";
}
/* for(int i=0;i<VSEL_SPLIT_VALUE.size();i++)
{
	strSplitVal = strSplitVal + VSEL_SPLIT_VALUE.elementAt(i)+"@";
} */
for(int i=0;i<VSEL_BU_PLANT_SEQ_NO.size();i++)
{
	strBuPlant = strBuPlant + VSEL_BU_PLANT_SEQ_NO.elementAt(i)+"@";
}

/* for(int i=0;i<VOTH_SEL_TRAD_CD.size();i++)
{
	strOthTrdCd = strOthTrdCd + VOTH_SEL_TRAD_CD.elementAt(i)+"@";
} */
for(int i=0;i<VOTH_SEL_PLANT_SEQ_NO.size();i++)
{
	strOthTrdCd = strOthTrdCd + VOTH_SEL_TRAD_CD.elementAt(i)+"@";
	strOthPlant = strOthPlant + VOTH_SEL_PLANT_SEQ_NO.elementAt(i)+"@";
	strOthPlantChrg = strOthPlantChrg + VOTH_SEL_CHARGE_VALUE.elementAt(i)+"@";
	strOthSplitVal = strOthSplitVal + VOTH_SEL_SPLIT_VALUE.elementAt(i)+"@";
}
/* for(int i=0;i<VOTH_SEL_SPLIT_VALUE.size();i++)
{
	strOthSplitVal = strOthSplitVal + VOTH_SEL_SPLIT_VALUE.elementAt(i)+"@";
} */

String disFormulaNm="";
for(int i=0;i<VFORMULA_NM.size();i++)
{
	if(disFormulaNm.equals(""))
	{
		disFormulaNm+="<span style='background: #ff99ff;'>"+VFORMULA_NM.elementAt(i)+"</span>";
	}
	else
	{
		disFormulaNm+=", <span style='background: #ff99ff;'>"+VFORMULA_NM.elementAt(i)+"</span>";
	}
}
for(int i=0;i<VSEL_GX_BU_PLANT_SEQ_NO.size();i++)
{
	strGxBuPlant = strGxBuPlant + VSEL_GX_BU_PLANT_SEQ_NO.elementAt(i)+"@";
}

String sec_category_value="";
if((no_of_security_dtl.equals("0") || no_of_security_dtl.equals("")))
{
	sec_category_value = "";
}
else
{
	sec_category_value="I";
}

String messurment_flag = purchase.getMessurment_flag();
String meas_std = purchase.getMeas_std();
String meas_temp = purchase.getMeas_temp();
String pressure_max_bar = purchase.getPressure_max_bar();
String pressure_min_bar = purchase.getPressure_min_bar();
String off_spec_gas_flag = purchase.getOff_spec_gas_flag();
String spec_gas_eng_base = purchase.getSpec_gas_eng_base();
String spec_max_eng = purchase.getSpec_max_eng();
String spec_min_eng = purchase.getSpec_min_eng();
String liability_flag = purchase.getLiability_flag();
String liability_clause = purchase.getLiability_clause();
String terminaton_flag = purchase.getTermination_flag();
String termination_clause = purchase.getTermination_clause();
String termination_planned = purchase.getTermination_planned();
String termination_forced = purchase.getTermination_forced();
String day_def_clause = purchase.getDay_def_clause();
String measurement_clause = purchase.getMeasurement_clause();
String spec_gas_clause = purchase.getSpec_gas_clause();
String bill_flag = purchase.getBilling_flag();
String billing_clause = purchase.getBilling_clause();

//<!--Harsh Maheta 20230903 : Added for old values to show in Deal audit history-->//
String cp_name = ""+utilBean.getCounterpartyName(counterparty_cd);
String cp_abbr = ""+utilBean.getCounterpartyABBR(counterparty_cd);

String old_value="CP="+counterparty_cd+"#NAME="+cp_name+"#ABBR="+cp_abbr+"#CONTNAME="+contract_type+cont_name+"#CONTNO="+cont_no+"#CONTREFNO="+cont_ref_no+"#CONTTYPE="+contract_type+"#TRADE_REFNO="+trade_ref_no+"#DDADT="+dda_dt+"#DDATIME="+dda_time+"#SIGNDT="+signing_dt+"#SIGNTIME="+signing_time+
"#ENTDT="+ent_dt+"#ENTTIME="+ent_time+"#STARTDT="+start_dt+"#ENDDT="+end_dt+"#RATE="+rate+"#RATEUNIT="+rate_unit+"#TCQ="+tcq+"#DCQ="+dcq+"#QUNIT="+quantity_unit+"#MDCQ="+mdcq_percentage+"#GXFEE="+txn_charges+"#GXFEEUNIT="+txn_unit+"#POSTMARG="+post_margin+"#CONT_STATUS="+cont_status;
%>
<body onload="setValues('<%=strTransCd%>','<%=strTrans%>','<%=strPlant%>','<%=strPlantChrg%>','<%=strBuPlant%>',
'<%=strGxBuPlant%>','<%=strSplitVal%>','<%=split_flag%>','<%=strOthTrdCd%>','<%=strOthPlant%>','<%=strOthPlantChrg%>','<%=strOthSplitVal%>');messurmentShows();terminatorShows();off_spec_gasShows();billingShows();liabilityShows();">
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_Trader_Contract_Mst">

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
					    	Trader Contract Master
					    </div>
					    <div class="btn-group">
						  <label class="btn btn-outline-secondary btngrp <%if(opration.equals("INSERT")){%>btnactive<%}%>" onclick="refresh('INSERT','<%=clearance%>');"><i class="fa fa-plus-circle"></i>&nbsp;New</label>
						  <label class="btn btn-outline-secondary btngrp <%if(opration.equals("MODIFY")){%>btnactive<%}%>" onclick="refresh('MODIFY','<%=clearance%>');"><i class="fa fa-edit"></i>&nbsp;Modify</label>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12" align="center">
				    				<div class="btn-group">
										<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("KYC")){%>btnactive<%}%>" onclick="refresh('<%=opration%>','KYC');">KYC</label>
										<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("IGX")){%>btnactive<%}%>" onclick="refresh('<%=opration%>','IGX');">IGX</label>
									</div>
				    			</div>
				  			</div>
						</div>
					</div>
					&nbsp;
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
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Contract Type</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="contract_type" onchange="refresh('<%=opration%>','<%=clearance%>');" <%if(opration.equals("MODIFY") && (!cont_no.equals("0") && !cont_no.equals(""))){ %>style="pointer-events: none;"<%} %>>
				    					<%if(clearance.equals("IGX")){ %>
				    					<option value="I">IGX</option>
				    					<%}else{ %>
				    					<option value="">--Select--</option>
				    					<option value="D">Domestic NG</option>
				    					<option value="T">In Tank LNG/RLNG</option>
				    					<%} %>
				    				</select>
				    				<script>document.forms[0].contract_type.value="<%=contract_type%>"</script>
				      			</div>
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
								<div class="col-sm-4 col-xs-4 col-md-4"> 
				    				<input type="text" class="form-control form-control-sm" name="" value="<%=dealMapping%>" maxLength="50" readOnly>				    				
				    			</div>
				    			<div class="col-sm-8 col-xs-8 col-md-8">
				    				<input type="text" class="form-control form-control-sm" name="cont_name" value="<%=cont_name%>" maxLength="50" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_no" value="<%=cont_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_rev_no" value="<%=cont_rev_no%>" maxLength="2" readOnly>
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
				    			<label class="form-label"><b>Contract Ref#<%if(clearance.equals("KYC")){%><span class="s-red">*</span><%}%></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="cont_ref_no" value="<%=cont_ref_no%>" maxLength="25">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Trade Ref#<%if(clearance.equals("IGX")){%><span class="s-red">*</span><%}%></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="trade_ref_no" value="<%=trade_ref_no%>" maxLength="25">
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
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
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
							<label class="form-label"><b>Contract/Trade Date<span class="s-red">*</span></b></label>
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
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="signing_time" value="<%=signing_time%>" maxLength="5" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
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
					<div class="row m-b-5" style="display:none;">
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
					<div class="row m-b-5" style="display:none;">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Agreement Base<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="radio" name="agreement_base" value="X" <%if(agmt_base.equals("X")){%>checked<%}%> onclick="showHide('X');">&nbsp;Ex-Terminal&nbsp;&nbsp;
				      				<input type="radio" name="agreement_base" value="D" <%if(agmt_base.equals("D")){%>checked<%}%> onclick="showHide('D');">&nbsp;Delivery&nbsp;&nbsp;
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
			      						onchange="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
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
										<option value="">--Select--</option>									
										<%if(!clearance.equals("IGX")){ %>
										<option value="R">Incoming</option>
										<%} %>
										<option value="I">Outgoing</option>
									</select>
									<script>document.forms[0].sec_category.value="<%=sec_category_value%>"</script>
								</div>
				    			<div class="col">
				    				<input type="button" name="security_btn" class="btn btn-sm config_btn" value="Security Config" onclick="securityPreReceipt('<%=contract_type %>','<%=agmt_no%>','<%=agmt_rev_no%>','<%=cont_no%>','<%=cont_rev_no%>');">
				  					<input type="hidden" name="securityFlag" value="N">
				  				</div>
				  				<%if(clearance.equals("IGX")){ %>
				  				<script>document.forms[0].sec_category.style.pointerEvents = "none";</script>
				  				<script>document.forms[0].security_btn.style.pointerEvents = "none";</script>
				  				<%} %>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Gas Price<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">
				    			<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="rate" value="<%=rate%>" 
				      				<%if(rate_unit.equals("1")){ %>onblur="checkNumber1(this,6,2);"<%}else{ %>onblur="checkNumber1(this,6,4);"<%}%> 
				      				<%if(opration.equals("MODIFY")) {%>readOnly<%} %>>
		      					</div>
				    			<div class="col">
		      						<select class="form-select form-select-sm" name="rate_unit" onchange="checkRateFormate(this);" <%if(opration.equals("MODIFY")) {%>style="pointer-events: none;"<%} %>>
		      							<option value="">--Select--</option>
		      							<option value="2">USD/MMBTU</option>
		      							<option value="1">INR/MMBTU</option>
		      						</select>
		      						<script>document.forms[0].rate_unit.value="<%=rate_unit%>"</script>
				    			</div>
				    			<%if(opration.equals("MODIFY")){ %>
				    			<div class="col">
				    				<input type="button" class="btn btn-sm config_btn" value="Variable Price Config" onclick="variablePriceConfig('<%=contract_type%>','<%=agmt_no%>','<%=agmt_rev_no%>','<%=cont_no%>','<%=cont_rev_no%>','<%=cont_ref_no%>','<%=cont_status%>');">
				  				</div>
				  				<%} %>
				  			</div> 
						</div>
					</div>
					<div class="row m-b-5">	
						<div class="col-sm-2 col-xs-2 col-md-2">
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">
								<div class="col">
									<%=price_change_history%>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5" id="dlvTranChg">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Transportation Charges<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="transportation_charges" value="<%=transportation_charges%>" onkeyup="checkForNumber(this);" onblur="checkNumber1(this,7,4);">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>TCQ<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="tcq" value="<%=tcq%>" onblur="checkNumber1(this,10,2);checkTCQQty();">
				      				<input type="hidden" class="form-control form-control-sm" name="temp_tcq" value="<%=tcq%>">
				    			</div>
				    			<div class="col">
		      						<select class="form-select form-select-sm" name="quantity_unit">
		      							<option value="1">MMBTU</option>
		      							<!-- <option value="2">TBTU</option> -->
		      						</select>
		      						<script>document.forms[0].quantity_unit.value="<%=quantity_unit%>"</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>TCQ Variation</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-2 col-xs-2 col-md-2">
				      				<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="" value="TBD" maxLength="100" readonly>
					    			</div>
				    			</div>
				    			<div class="col-sm-10 col-xs-10 col-md-10">
				      				<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="" value="TBD" maxLength="100" readonly>
					    			</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label" style="background:#FFF4A3;"><b>Received MMBTU</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="received_qty" value="<%=received_qty%>" readOnly style="background:#FFF4A3;">
					    			</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>DCQ (MMBTU)<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="dcq" value="<%=dcq%>" onblur="checkNumber1(this,10,2);">
				    			</div>
				    			<div class="col">
				    				<input type="button" class="btn btn-sm config_btn" value="Variable DCQ Config" style="display:none;">
				  				</div>				    			
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>MDCQ(%)</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="mdcq" value="<%=mdcq_percentage%>" onkeyup="checkForNumber(this);" onblur="checkNumber1(this,5,2);checkMdcq();">
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5" <%if(!clearance.equals("IGX")){ %>style="display:none;"<%}%>>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>GX Transaction Fee</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-auto">
				      				<input type="text" class="form-control form-control-sm" name="txn_charges" value="<%=txn_charges%>" 
				      				<%if(rate_unit.equals("1")){ %>onblur="checkNumber1(this,6,2);"<%}else{ %>onblur="checkNumber1(this,6,4);"<%}%>>
				    			</div>
				    			<div class="col-auto">
		      						<select class="form-select form-select-sm" name="txn_unit" onchange="checkTxnRateFormate(this);" style="pointer-events: none;">
		      							<option value="2">USD/MMBTU</option>
		      							<option value="1">INR/MMBTU</option>
		      						</select>
		      						<script>document.forms[0].txn_unit.value="<%=txn_unit%>"</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Post Trade Margin(%)</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="post_margin" value="<%=post_margin%>" onblur="checkNumber1(this,5,2);">
					    			</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<%if(contract_type.equals("I")){ %>
					<div class="row m-b-5" >
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>GX Business Unit<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<%if(VGX_BU_CD.size() > 0) {%>
					    				<%for(int i=0; i<VGX_BU_CD.size(); i++){ %>
					    					<input type="radio" name="chk_gx_bu_plant" 
					    					value="<%=VGX_BU_PLANT_SEQ_NO.elementAt(i)%>">&nbsp;<%=VGX_BU_PLANT_ABBR.elementAt(i)%>&nbsp;&nbsp;
					    				<%}%>
				    				<%}else{ %>
				    					<%= utilmsg.warningMessage("Please configure GX Business Plants!")%>
				    				<%} %>
				      			</div>
				  			</div>
						</div>
					</div>
					<%} %>
					&nbsp;
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
			    					<input type="button" class="btn btn-sm config_btn" value="Trader Plant Config"  
			    					data-bs-toggle="modal" data-bs-target="#TradConfigModal">
				    			</div>
				  			</div>
						</div>
						
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				
				    				<label class="form-label" id="tradDisplay" style="display:none;"></label>
				      			</div>
				  			</div>
						</div>
					</div>
					
					<div class="row m-b-5" id="other_trd" style="display:none;">
						
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Split Trader-Plant(s)<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label" id="OthtradPlantDisplay" style="display:none;"></label>
				      			</div>
				  			</div>
						</div>
					</div>
					
					<div class="row m-b-5" id="linkTrans">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="button" class="btn btn-sm config_btn" value="Link GTA" data-bs-toggle="modal" data-bs-target="">
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      			</div>
				  			</div>
						</div>
					</div>
					&nbsp;
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
					    					<input <%if(clearance.equals("IGX")){ %>type="radio"<%}else{ %>type="checkbox"<%} %> class="form-check-input" name="chk_bu_plant" value="<%=VBU_PLANT_SEQ_NO.elementAt(i)%>">&nbsp;<%=VBU_PLANT_ABBR.elementAt(i)%>&nbsp;&nbsp;
					    				<%}%>
				    				<%}else{ %>
				    					<%= utilmsg.warningMessage("Please configure Business Plants!")%>
				    				<%} %>
				      			</div>
				  			</div>
						</div>
					</div>
					<%if(opration.equals("MODIFY")){ %>
					<%} %>
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
							<div class="form-group row">
				    			<label class="form-label"><b><input type="checkbox" class="form-check-input" name="buyer_nom" value="Y" <%if(buy_nom_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Buyer Nomination</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="checkbox" class="form-check-input" name="chk_buyer_nom" value="M" <%if(buy_month_nom.equals("Y")){ %>checked<%} %>>&nbsp;Monthly&nbsp;&nbsp;
				    				<input type="checkbox" class="form-check-input" name="chk_buyer_nom" value="F" <%if(buy_fortnightly_nom.equals("Y")){ %>checked<%} %>>&nbsp;Fortnightly&nbsp;&nbsp;
				    				<input type="checkbox" class="form-check-input" name="chk_buyer_nom" value="W" <%if(buy_week_nom.equals("Y")){ %>checked<%} %>>&nbsp;Weekly&nbsp;&nbsp;
				    				<input type="checkbox" class="form-check-input" name="chk_buyer_nom" value="D" <%if(buy_daily_nom.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Daily&nbsp;&nbsp;	
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b><input type="checkbox" class="form-check-input" name="seller_nom" value="Y" <%if(sell_nom_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Seller Nomination</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
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
				    				<input type="text" class="form-control form-control-sm" name="day_def_clause_no" value="<%=day_def_clause %>" placeholder="Clause#" maxLength="10">
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
									<label class="form-label"><b><input type="checkbox" class="form-check-input" name="measurementCheckbox" id="measurementCheckbox" value="Y" onchange="messurmentShows()" <%if(messurment_flag.equals("Y")){ %>checked<%} %>>&nbsp;Measurement</b></label>
				  				</div>
				    			<div id="measurement_clause_div" class="col">
				    				<input type="text" class="form-control form-control-sm" name="measure_clause_no" value="<%=measurement_clause %>" placeholder="Clause#" maxLength="10">
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
												<input type="text" class="form-control form-control-sm" name="meas_temperature" id="meas_temperature" value="<%=meas_temp%>"  onblur="checkValuePrecision(this.value,'2','3','meas_temperature')">
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
												<input type="text" placeholder="min" class="form-control form-control-sm" name="pressure_min_bar" id="pressure_min_bar" value="<%=pressure_min_bar%>" onblur="checkValuePrecision(this.value,'2','3','pressure_min_bar')">
												<span class="input-group-text"><b>-</b></span>
												<input type="text" placeholder="max" class="form-control form-control-sm" name="pressure_max_bar" id="pressure_max_bar"  value="<%=pressure_max_bar%>" onblur="checkValuePrecision(this.value,'2','3','pressure_max_bar')">
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
				    				<input type="text" class="form-control form-control-sm" name="spec_clause_no" value="<%=spec_gas_clause %>" placeholder="Clause#" maxLength="10">
				      			</div>
				  			</div>
						</div>
						<div id="off_spec_gas_div" class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-3 col-xs-3 col-md-3">
									<label class="form-label">&nbsp;Energy Base</label>
								</div>
								<div class="col-sm-3 col-xs-3 col-md-3">
									<select class="form-select form-select-sm" name="spec_gas_energy_base" >
		      							<option value="">--Select--</option>
		      							<option value="GCV">GCV</option>
		      							<option value="NCV">NCV</option>
		      						</select>
	 		      					<script>document.forms[0].spec_gas_energy_base.value="<%=spec_gas_eng_base.trim()%>"</script>
								</div>
								<div class="col-sm-6 col-xs-6 col-md-6">
									<div class="input-group input-group-sm" >
										<input type="text" placeholder="min" class="form-control form-control-sm" name="spec_gas_min_energy" id="spec_gas_min_energy" value="<%=spec_min_eng%>" onblur="checkValuePrecision(this.value,'2','5','spec_gas_min_energy')" size="10">
										<span class="input-group-text"><b>-</b></span>
										<input type="text" placeholder="max" class="form-control form-control-sm" name="spec_gas_max_energy" id="spec_gas_max_energy" value="<%=spec_max_eng%>" onblur="checkValuePrecision(this.value,'2','5','spec_gas_max_energy')" size="10">										
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
				    				<input type="button" class="btn btn-sm config_btn" value="Configure Liability" <%if(opration.equals("MODIFY")){ %>onclick="openLiabilityDtl();"<%} %>>&nbsp;
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
				    				<input type="text" class="form-control form-control-sm" name="buy_clause_no" value="<%=billing_clause%>" placeholder="Clause#" maxLength="10">
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
					<div <%if(opration.equals("MODIFY") && (!cont_no.equals("0") && !cont_no.equals(""))){ %><%}else{ %>style="display:none;"<%} %>>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Finance Checks & Control</label>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Finance Checks & Control</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
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
					    	Trader Contract - Change Request(s)
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
								<div class="col-auto">
				    				<input type="button" class="btn btn-sm request_btn" value="Modify Contract Duration" style="background:#e6e6e6;color:black;">
				    			</div>
				    			<div class="col-auto">
				    				<input type="button" class="btn btn-sm request_btn" value="Closure Request" style="background:#e6e6e6;color:black;">
				    			</div>
				  			</div>
						</div>
					</div>
					<br>
					<%if(!cont_no.equals("0") && !cont_no.equals("")){ %>
						<%if(no_of_billing_dtl.equals("0") || no_of_billing_dtl.equals("")){ %>
							<div class="row m-b-5">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.errorMessage("<b>Billing Detail is not Configured. The Contract will not appear for FCC!</b>")%>
								</div>
							</div>
						<%} %>
						<%if((no_of_security_dtl.equals("0") || no_of_security_dtl.equals("")) && clearance.equals("KYC")){ %>
							<div class="row m-b-5">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.errorMessage("<b>Security Detail is not Configured. The Contract will not appear for FCC!</b>")%>
								</div>
							</div>
						<%} %>
						<%if(no_of_liability_dtl.equals("0") || no_of_liability_dtl.equals("")){ %>
							<div class="row m-b-5">
								<div align="center" class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.infoMessage("<b>Liability details configuration is pending for Contract.</b>")%>
								</div>
							</div>
						<%} %>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- NO NEED TRASPORTER MODEL 09/11/2022  -->
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
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitTransSel();">
				</div>
      		</div>
      	</div>
	</div>
</div>

<!-- NO NEED LINK TRASPORTER MODEL 09/11/2022  -->    
<!-- LINK TRASPORTER -->
<div class="modal fade" id="LinkTrans" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Link Transporter
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-12 col-xs-12 col-md-12">
      						<div class="form-group row">
      							<div class="col-sm-3 col-xs-3 col-md-3">
				    				<label class="form-label"><b>Business Unit<span class="s-red">*</span></b></label>
				    				<select class="form-select form-select-sm" name="dp_bu_plant">
										<option value="0">Select Business Unit</option>
										<%for(int i=0;i<VSEL_BU_PLANT_SEQ_NO.size();i++){ %>
										<option value="<%=VSEL_BU_PLANT_SEQ_NO.elementAt(i)%>"><%=VSEL_BU_PLANT_ABBR.elementAt(i)%></option>
										<%} %>
									</select>
				    			</div>
				    			<div class="col-sm-3 col-xs-3 col-md-3">
				    				<label class="form-label"><b>Trader Plant(s)<span class="s-red">*</span></b></label>
				    				<select class="form-select form-select-sm" name="dp_trd_plant">
										<option value="0">Select Trader Plant</option>
										<%for(int i=0;i<VSEL_PLANT_SEQ_NO.size();i++){ %>
										<option value="<%=VSEL_TRAD_CD.elementAt(i)%>-<%=VSEL_PLANT_SEQ_NO.elementAt(i)%>"><%=VSEL_PLANT_ABBR.elementAt(i)%></option>
										<%} %>
									</select>
				    			</div>
				    			<div class="col-sm-3 col-xs-3 col-md-3">
				    				<label class="form-label"><b>Transporter Entry Point<span class="s-red">*</span></b></label>
				    				<select class="form-select form-select-sm" name="dp_trans_plant">
										<option value="0">Select Transporter Delivery Point</option>
										<%for(int i=0;i<VSEL_TRANS_CD.size();i++){ %>
										<option value="<%=VSEL_TRANS_CD.elementAt(i) %>-<%=VSEL_TRANS_PLANT_SEQ_NO.elementAt(i)%>"><%=VSEL_TRANS_PLANT_ABBR.elementAt(i)%></option>
										<%} %>
									</select>
				    			</div>
				    			<div class="col-sm-3 col-xs-3 col-md-3" align="center">
				    				<input type="button" class="btn btn-warning com-btn" value="Add" onclick = "addLinkTransporter();">
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
						<label class="form-label subheader">Linked Transporter List</label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Linked Transporter</th>
										<th></th>
									</tr>
								</thead>
								<tbody id="LinkedTransTab">
									<%for(int i=0; i<VFORMULA_ID.size(); i++){ %>
									<tr>
										<td align="center"><%=i+1%></td>
										<td><%=VFORMULA_NM.elementAt(i)%>
										<input type="hidden" name="formula_id" id="formula_id<%=i+1%>" value="<%=VFORMULA_ID.elementAt(i)%>">	
										</td>
										<td></td>
									</tr>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="" align="right">
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitLinkTransporter();">
				</div>
      		</div>
      	</div>
	</div>
</div>

<div class="modal fade" id="TradConfigModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Trader Plant Configuration
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
								<div class="d-flex justify-content-end">
					    			<label class="form-label">
					    				<b>
					    					<input type="checkbox" class="form-check-input" name="chk_split" onclick="splitEnabled(this);">&nbsp;Split Trader(% wise)
					    					<input type="hidden" name="split_enabled" value="<%=split_flag%>">
					    					<input type="hidden" name="split_type_flag" value="<%=split_type%>">
					    				</b>
					    			</label>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=contpty_abbr%></label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th rowspan="2"></th>
										<th rowspan="2">Plant</th>
										<th rowspan="2">Split%</th>
										<% for(int j=0; j<VCHARGE_ABBR.size(); j++){ %>
										<th colspan="2"><%=VCHARGE_NAME.elementAt(j)%></th>
										<%}%>
									</tr>
									<tr>
										<% for(int j=0; j<VCHARGE_ABBR.size(); j++){ %>
										<th>INR/MMBTU</th>
										<th>Eff Date</th>
										<%}%>																	
									</tr>
								</thead>
								<tbody>							
								<%if(VPLANT_SEQ_NO.size() > 0) {%>
							    	<%for(int i=0; i<VPLANT_SEQ_NO.size(); i++){ %>	
							    		<tr>
											<td align="center">	
				      							<input type="checkbox" class="form-check-input" name="chk_plant" value="<%=VPLANT_SEQ_NO.elementAt(i)%>" 
				      							onclick="enableSplit(this,'split_value_<%=VPLANT_SEQ_NO.elementAt(i)%>','<%=i%>','');" >
				      						</td>
				      						<td><%=VPLANT_ABBR.elementAt(i)%></td>
								    		<td align="center">
					    						<div class="col-auto" id="hid_col_split_<%=VPLANT_SEQ_NO.elementAt(i)%>" style="display:none;width:100px;">
						    						<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm" name="split_value" id="split_value_<%=VPLANT_SEQ_NO.elementAt(i)%>" 
							      						value="" maxLength="6" size="5" onblur="checkNumber1(this,5,2);" disabled>	
							      						<span class="input-group-text"><i class="fa fa-percent fa-lg"></i></span>
						      						</div>
						      					</div>
					    						<input type="hidden" name="plant_abbr" value="<%=VPLANT_ABBR.elementAt(i)%>">
								    		</td>								    		
								    		<% for(int j=0; j<VCHARGE_ABBR.size(); j++){ %>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="<%=VCHARGE_ABBR.elementAt(j)%>" id="<%=VCHARGE_ABBR.elementAt(j)%>_<%=i%>_<%=j%>" size="10" value="" style="text-align: right;" onblur="checkNumber1(this,7,4);" disabled>
					    						</div>
					    					</td>
					    					<td align="center">
					    						<div style="width:100px;">
						    						<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt_<%=VCHARGE_ABBR.elementAt(j)%>" id="eff_dt_<%=VCHARGE_ABBR.elementAt(j)%>_<%=i%>_<%=j%>" maxLength="10" 
							      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" disabled>
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
						      					</div>
											</td>
											<%}%>
								    	</tr>	
				    				<%}%>
			    				<%}else{ %>
				    				<tr>
			    						<td colspan="<%=(VCHARGE_ABBR.size()*2)+3%>" align="center"><%= utilmsg.warningMessage("Please configure Plants for Trader!")%></td>
			    					</tr>
			    				<%} %>	
			    				</tbody>
			    			</table>
			    		</div>
			    	</div>				  			
      			</div>
	      		<div class="cdbody" id="othTrdPlantListDis" style="display:none;">
		     		<%int j=0,k=0;%>
					<%for(int i=0;i<VOTH_COUNTERPARTY_CD.size();i++){ 
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
						String oth_countpty_cd=""+VOTH_COUNTERPARTY_CD.elementAt(i);
					%>
						<%if(i!=0){ %>&nbsp;<%}%>
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VOTH_COUNTERPARTY_ABBR.elementAt(i)%></label>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">  
								<div class="form-group row">
									<div class="table-responsive">
										<table class="table table-bordered table-hover">
											<thead>
												<tr>
													<th rowspan="2"></th>
													<th rowspan="2">Plant</th>
													<th rowspan="2">Split%</th>
													<% for(int m=0; m<VCHARGE_ABBR.size(); m++){ %>
													<th colspan="2"><%=VCHARGE_NAME.elementAt(m)%></th>
													<%}%>
												</tr>
												<tr>
													<% for(int m=0; m<VCHARGE_ABBR.size(); m++){ %>
													<th>INR/MMBTU</th>
													<th>Eff Date</th>
													<%}%>																	
												</tr>
											</thead>
											<tbody>
											<%k=0;
						    				if(index > 0) {%>
							    				<%for(j=j; j<VOTH_PLANT_SEQ_NO.size(); j++){ 
							    					k+=1;
							    				%>
							    				<tr>
													<td align="center">	
						      							<input type="checkbox" class="form-check-input" name="chk_oth_plant" 
									    					onclick="enableSplit(this,'oth_split_value_<%=oth_countpty_cd%>_<%=VOTH_PLANT_SEQ_NO.elementAt(j)%>','<%=j%>','oth');
									    					enabled_TransPlantDtl(this,'oth_trd_cd_<%=oth_countpty_cd%>_<%=VOTH_PLANT_SEQ_NO.elementAt(j)%>','oth_plant_seq_no_<%=oth_countpty_cd%>_<%=VOTH_PLANT_SEQ_NO.elementAt(j)%>')">
									    				<input type="hidden" name="oth_trd_cd" id="oth_trd_cd_<%=oth_countpty_cd%>_<%=VOTH_PLANT_SEQ_NO.elementAt(j)%>" value="<%=oth_countpty_cd%>" disabled>
							    						<input type="hidden" name="oth_plant_seq_no" id="oth_plant_seq_no_<%=oth_countpty_cd%>_<%=VOTH_PLANT_SEQ_NO.elementAt(j)%>" value="<%=VOTH_PLANT_SEQ_NO.elementAt(j)%>" disabled>
							    						<input type="hidden" name="oth_plant_abbr" id="oth_plant_abbr<%=oth_countpty_cd%>_<%=VOTH_PLANT_SEQ_NO.elementAt(j)%>" value="<%=VOTH_PLANT_ABBR.elementAt(j)%>" disabled>
						      						</td>
						      						<td><%=VOTH_PLANT_ABBR.elementAt(j)%></td>
						      						<td>
						      							<div class="col-auto" id="hid_col_oth_split_<%=oth_countpty_cd%>_<%=VOTH_PLANT_SEQ_NO.elementAt(j)%>" style="display:none;width:100px;">
								    						<div class="input-group input-group-sm" >
									      						<input type="text" class="form-control form-control-sm" name="oth_split_value" 
									      						id="oth_split_value_<%=oth_countpty_cd%>_<%=VOTH_PLANT_SEQ_NO.elementAt(j)%>"
									      						value="" maxLength="6" size="5" onblur="checkNumber1(this,5,2);" disabled>	
									      						<span class="input-group-text"><i class="fa fa-percent fa-lg"></i></span>
								      						</div>
								      					</div>
						      						</td>
						      						<% for(int m=0; m<VCHARGE_ABBR.size(); m++){ %>
													<td align="center">
														<div style="width:100px;">
															<input type="text" class="form-control form-control-sm" name="oth_<%=VCHARGE_ABBR.elementAt(m)%>" id="oth_<%=VCHARGE_ABBR.elementAt(m)%>_<%=j%>_<%=m%>" size="10" value="" style="text-align: right;" onblur="checkNumber1(this,7,4);" disabled>
							    						</div>
							    					</td>
							    					<td align="center">
							    						<div style="width:100px;">
								    						<div class="input-group input-group-sm" >
									      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="oth_eff_dt_<%=VCHARGE_ABBR.elementAt(m)%>" id="oth_eff_dt_<%=VCHARGE_ABBR.elementAt(m)%>_<%=j%>_<%=m%>" maxLength="10" 
									      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" disabled>
									      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
								      						</div>
								      					</div>
													</td>
													<%}%>
						      					</tr>
							    					<%if(k==index){ 
							    						j++;
							    						break;
							    					}%>
							    				<%}%>
						    				<%}else{ %>
						    					<tr>
						    						<td colspan="<%=(VCHARGE_ABBR.size()*2)+3%>" align="center"><%= utilmsg.warningMessage("Please configure Plants for Trader!")%></td>
						    					</tr>
						    				<%} %>
						    				</tbody>
						    			</table>
						    		</div>
								</div>
							</div>
						</div>
					<%} %>	
	     		</div>
	     	</div>
      		<div class="modal-footer cdfooter">
        		<div class="" align="right">
					<input type="button" class="btn btn-warning com-btn" value="Proceed" onclick="doSubmitTradConfig('P');">
				</div>
      		</div>
      	</div>
	</div>
</div>

<input type="hidden" name="option" value="TRADER_CONT_MST">
<input type="hidden" name="old_value" value="<%=old_value%>">

<input type="hidden" name="prev_clearance" value="<%=clearance%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="opration" value="<%=opration%>">
<%-- <input type="hidden" name="contract_type" value="<%=contract_type%>"> --%>
<input type="hidden" name="cont_status_flg" value="<%=cont_status_flg%>">

<input type="hidden" name="linkedTransSize" value="<%=VFORMULA_ID.size()%>">

<input type="hidden" name="no_of_billing_dtl" value="<%=no_of_billing_dtl%>">
<input type="hidden" name="no_of_security_dtl" value="<%=no_of_security_dtl%>">
<input type="hidden" name="no_of_liability_dtl" value="<%=no_of_liability_dtl%>">
<input type="hidden" name="gx_counterparty_cd" value="<%=gx_counterparty_cd%>">

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

<% for(int m=0; m<VCHARGE_ABBR.size(); m++){ %>
<input type="hidden" name="charge_abbr" id="charge_abbr_<%=m%>" value="<%=VCHARGE_ABBR.elementAt(m)%>">
<input type="hidden" name="charge_nm" id="charge_nm_<%=m%>" value="<%=VCHARGE_NAME.elementAt(m)%>">
<%}%>

<%if(agmt_base.equals("D"))
{%>
	<script>
	document.getElementById("dlvTranChg").style.display="flex";
	document.getElementById("linkTrans").style.display="flex";
	</script>
<%}
else
{%>
	<script>
		document.getElementById("dlvTranChg").style.display="none";
		document.getElementById("linkTrans").style.display="none";
		document.forms[0].transportation_charges.value="";
	</script>
<%} %>

<script>
</script>

</form>
</body>
</html>