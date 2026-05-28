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
			contract_type="X";
		}
	}
		
	var u = document.forms[0].u.value;
	
	var url = "frm_contract_mst.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+"&u="+u+
			"&clearance="+clearance+"&contract_type="+contract_type;

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
	var contract_type = document.forms[0].contract_type.value;
	var clearance=document.forms[0].clearance.value;
	
	var cont_ref_no = document.forms[0].cont_ref_no.value;
	var trade_ref_no = document.forms[0].trade_ref_no.value;
	
	var dda_dt = document.forms[0].dda_dt.value;
	var dda_time = document.forms[0].dda_time.value;
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
	var txn_charges = document.forms[0].txn_charges.value;
	
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	var no_of_security_dtl = document.forms[0].no_of_security_dtl.value;
	
	var contdt_change_request_flag = document.forms[0].contdt_change_request_flag.value;
	var max_date = document.forms[0].max_date.value;
	var min_nom_date = document.forms[0].min_nom_date.value;

	var agreement_type = document.forms[0].agreement_type;
	var agreement_base = document.forms[0].agreement_base;
	
	var chk_plant = document.forms[0].chk_plant;
	var chk_plant_abbr = document.forms[0].chk_plant_abbr;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	
	var charge_abbr = document.forms[0].charge_abbr;
	
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
	if(contract_type=="S")
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
	if(trim(contdt_change_request_flag) == "Y" && trim(min_nom_date) !="")
	{
		var value_0 = compareDate(start_dt,min_nom_date);
		if(value_0 == "1")
		{
			msg+="Contract Start Date "+start_dt+" > First Nomination Date "+min_nom_date+" not Allowed!\n";
			flag=false
		}
	}	
	if(trim(end_dt) == "")
	{
		msg+="Enter End Date!\n";
		flag=false;
	}
	if(trim(contdt_change_request_flag) == "Y" && trim(max_date) !="")
	{
		var value_1 = compareDate(max_date,end_dt);
		if(value_1 == "1") 
		{
			msg+="Contract End Date "+end_dt+" < Last Allocation/Invoice Date "+max_date+" not Allowed!\n";
			flag=false
		}
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
	
	if(trim(txn_charges) == "" && contract_type=="X")
	{
		msg+="Enter TXN Charges!\n";
		flag=false;
	}
	
	if(agreement_type[0].checked == false && agreement_type[1].checked == false)
	{
		msg+="Select Agreement Type!\n";
		flag=false;
	}
	if(agreement_base[0].checked == false && agreement_base[1].checked == false)
	{
		msg+="Select Agreement Base!\n";
		flag=false;
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
					if(agreement_base[1].checked == true)
					{
						if(charge_abbr!=null && charge_abbr.length!=undefined)
					 	{
					  		for(var j=0;j<charge_abbr.length;j++)
					  		{
					  			var temp = document.getElementById("charge_abbr_"+j).value;
					  			//alert(temp)
					  			if(temp=="TC")
					  			{
					  				var chrRate=document.getElementById(temp+"_"+i+"_"+j).value;
						  			var chrEff=document.getElementById("eff_dt_"+temp+"_"+i+"_"+j).value;
						  			
						  			if(trim(chrRate)=="")
									{
										msg+="Enter Transportation Charges for "+chk_plant_abbr[i].value+"!\n";
										flag=false;
									}
						  			
						  			if(trim(chrEff)=="")
									{
										msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr[i].value+"!\n";
										flag=false;
									}
								}
					  		} 
					 	}
					 	else if(charge_abbr!=null)
					 	{
					 		var temp = document.getElementById("charge_abbr_0").value;
				  			//alert(temp)
				  			if(temp=="TC")
				  			{
				  				var chrRate=document.getElementById(temp+"_"+i+"_0").value;
					  			var chrEff=document.getElementById("eff_dt_"+temp+"_"+i+"_0").value;
					  			
					  			if(trim(chrRate)=="")
								{
									msg+="Enter Transportation Charges for "+chk_plant_abbr[i].value+"!\n";
									flag=false;
								}
					  			
					  			if(trim(chrEff)=="")
								{
									msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr[i].value+"!\n";
									flag=false;
								}
							}
					 	}
					}		
				}
			}
		}
		else
		{
			if(chk_plant.checked)
			{
				chkFlg = true;
				if(agreement_base[1].checked == true)
				{
					if(charge_abbr!=null && charge_abbr.length!=undefined)
				 	{
				  		for(var j=0;j<charge_abbr.length;j++)
				  		{
				  			var temp = document.getElementById("charge_abbr_"+j).value;
				  			//alert(temp)
				  			if(temp=="TC")
				  			{
				  				var chrRate=document.getElementById(temp+"_0_"+j).value;
					  			var chrEff=document.getElementById("eff_dt_"+temp+"_0_"+j).value;
					  			
					  			if(trim(chrRate)=="")
								{
									msg+="Enter Transportation Charges for "+chk_plant_abbr.value+"!\n";
									flag=false;
								}
					  			
					  			if(trim(chrEff)=="")
								{
									msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr.value+"!\n";
									flag=false;
								}
							}
				  		} 
				 	}
				 	else if(charge_abbr!=null)
				 	{
				 		var temp = document.getElementById("charge_abbr_0").value;
			  			//alert(temp)
			  			if(temp=="TC")
			  			{
			  				var chrRate=document.getElementById(temp+"_0_0").value;
				  			var chrEff=document.getElementById("eff_dt_"+temp+"_0_0").value;
				  			
				  			if(trim(chrRate)=="")
							{
								msg+="Enter Transportation Charges for "+chk_plant_abbr.value+"!\n";
								flag=false;
							}
				  			
				  			if(trim(chrEff)=="")
							{
								msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr.value+"!\n";
								flag=false;
							}
						}
				 	}
				}
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
	
	/* if(opration=="INSERT" && clearance=="KYC")
	{
		if(document.forms[0].securityFlag.value=='N')
		{
			msg += "Please Enter Security!! \n";
			flag=false;
		}
	} */
	
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
			/* if(cont_status_flg == "Y")
			{
				a = confirm("On saving your modification/s, this Contract will be Revised!\n\nDo you want to Proceed?");
			}
			else
			{ */
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
	var clearance = document.forms[0].clearance.value;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_contract_list.jsp?counterparty_cd="+counterparty_cd+"&clearance="+clearance+"&contract_type="+contract_type,"Supply Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_contract_list.jsp?counterparty_cd="+counterparty_cd+"&clearance="+clearance+"&contract_type="+contract_type,"Supply Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
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
			newWindow = window.open("frm_cont_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_cont_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
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
	var clearance = document.forms[0].clearance.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var rate_unit = document.forms[0].rate_unit.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_contract_billing_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
			"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+"&start_dt="+start_dt+
			"&u="+u+"&rate_unit="+rate_unit+"&end_dt="+end_dt;
	
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
	var clearance = document.forms[0].clearance.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_contract_mst.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&contract_type="+cont_type+
			"&u="+u+"&clearance="+clearance;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setAgmtDetail(countpty_cd,agmt_no,agmt_rev_no,agmt_type,agmt_nm)
{
	/* document.forms[0].agmt_no.value=agmt_no;
	document.forms[0].agmt_rev_no.value=agmt_rev_no;
	document.forms[0].agmt_nm.value=agmt_nm */
	var opration = document.forms[0].opration.value;
	var clearance = document.forms[0].clearance.value;
	var contract_type = document.forms[0].contract_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_contract_mst.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+
		"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&contract_type="+contract_type+"&agmt_nm="+agmt_nm+
		"&u="+u+"&clearance="+clearance;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);

}

function setValues(strTransCd,strTrans,strPlant,strBuPlant,strPlantChrg,strGxBuPlant)
{
	var chk_plant = document.forms[0].chk_plant;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var chk_gx_bu_plant = document.forms[0].chk_gx_bu_plant;
	var trans_cd = document.forms[0].trans_cd;
	var trans_plant_seq_no = document.forms[0].trans_plant_seq_no;
	var trans_plant_abbr = document.forms[0].trans_plant_abbr;
	
	var charge_abbr = document.forms[0].charge_abbr;
	
	var sepTransCd = strTransCd.split("@");
	var sepTrans = strTrans.split("@");
	var sepPlant = strPlant.split("@");
	var sepBuPlant = strBuPlant.split("@");
	var sepPlantChrg = strPlantChrg.split("@");
	var sepGxBuPlant = strGxBuPlant.split("@");
	
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
	
	doSubmitCustConfig('');
}

function doSubmitCustConfig(submit_flag)
{
	var chk_plant = document.forms[0].chk_plant;
	var chk_plant_abbr = document.forms[0].chk_plant_abbr;
	
	var charge_abbr = document.forms[0].charge_abbr;
	var charge_nm = document.forms[0].charge_nm;
	
	var msg="";
	var flag=true;
	
	var display ="";
	
	var countCustPlant=parseInt("0");
	
	if(chk_plant!=null && chk_plant.length!=undefined)
 	{
  		for(var i=0;i<chk_plant.length;i++)
  		{
   			if(chk_plant[i].checked)
   			{
   				countCustPlant+=1;
   				
   				if(display!="")
   				{
   					display+="<br>"+chk_plant_abbr[i].value;
   				}
   				else
   				{
   					display+=chk_plant_abbr[i].value;
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
   			countCustPlant+=1;
   			
   			if(display!="")
			{
				display+="<br>"+chk_plant_abbr.value;
			}
			else
			{
				display+=chk_plant_abbr.value;
			}
   			
   			if(charge_abbr!=null && charge_abbr.length!=undefined)
		 	{
		  		for(var j=0;j<charge_abbr.length;j++)
		  		{
		  			var temp = document.getElementById("charge_abbr_"+j).value;
		  			var temp_nm = document.getElementById("charge_nm_"+j).value;
		  			
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
	  			
		 		var rate = document.getElementById(temp+"_0_0").value;
	  			var eff = document.getElementById("eff_dt_"+temp+"_0_0").value;
	  			
	  			if(rate != "" && eff != "")
		  		{
	  				display+=" <font style='background:#99ffcc'>"+temp_nm+" "+rate+"(eff. "+eff+")</font>";
		  		}
	  		}
   		} 
 	}
	
	if(countCustPlant<=0)
	{
		msg += "Please Select Atleast One Customer-Plant!\n";
		flag=false;
	}
	
	if(countCustPlant<=0 && submit_flag=="P")
	{
		alert("Please Select Atleast One Trader-Plant!");
	}
	else if(!flag && submit_flag=="P")
	{
		alert(msg);	
	}
	else
	{
		document.getElementById("custDisplay").innerHTML=display;
			
		if(display != "")
		{
			document.getElementById("custDisplay").style.display="inline";
		//	document.getElementById("tansAlert").style.display="none";
		}
		
		if(submit_flag=="P")
		{
			alert("Proceed and Submit Contract!");	
		}
		$("#CustConfigModal").modal("hide");
	}
}

function showHide(flg)
{
	var transportation_charges = document.forms[0].transportation_charges;
	if(flg == "D")
	{
		document.getElementById("linkTrans").style.display="flex";
		/* FOLLOWING NOT REQUIRED AFTER SUPPORTING EX-TERMINAL CONTRACT
		if(transportation_charges!=null && transportation_charges!=undefined)
		{
			if(transportation_charges.length!=undefined)
			{
				for(var i=0;i<transportation_charges.length;i++)
				{
					document.getElementById("transChrg"+i).style.display="";
				}
			}
			else
			{
				document.getElementById("transChrg0").style.display="";
			}
		}*/
	}
	else
	{
		document.getElementById("linkTrans").style.display="none";
		/*if(transportation_charges!=null && transportation_charges!=undefined)
		{
			if(transportation_charges.length!=undefined)
			{
				for(var i=0;i<transportation_charges.length;i++)
				{
					document.getElementById("transChrg"+i).style.display="none";
				}
			}
			else
			{
				document.getElementById("transChrg0").style.display="none";
			}
		}*/
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

var creditExceedWindow;
function creditExceedDaysConfig(cont_type,agmt,agmt_rev,cont,cont_rev)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var temp_start_dt = document.forms[0].temp_start_dt.value;
	var temp_end_dt = document.forms[0].temp_end_dt.value;
	
	var isDealFm="Y";
	
	var u = document.forms[0].u.value;
	
	if(trim(cont_type)!="" && (trim(cont)!="" && cont!="0") && (trim(counterparty_cd)!="" && counterparty_cd!="0"))
	{
		var url = "../credit_risk/frm_credit_exceed_days.jsp?counterparty_cd="+counterparty_cd+"&cont_no="+cont+"&cont_rev_no="+cont_rev+
			"&agmt_no="+agmt+"&agmt_rev_no="+agmt_rev+"&contract_type="+cont_type+
			"&start_dt="+temp_start_dt+"&end_dt="+temp_end_dt+"&isDealFm="+isDealFm+
			"&u="+u;;
		
		if(!creditExceedWindow || creditExceedWindow.closed)
		{
			creditExceedWindow = window.open(url,"Credit Exceed Days Configuration","top=10,left=100,width=1200,height=600,scrollbars=1");
		}
		else
		{
			creditExceedWindow.close();
			creditExceedWindow = window.open(url,"Credit Exceed Days Configuration","top=10,left=100,width=1200,height=600,scrollbars=1");
		}
	}
	else
	{
		alert("Select Contract Detail!")
	}
}

var dcqWindow;
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

function setTransChrg(obj,index)
{
	var charge_abbr = document.forms[0].charge_abbr;
	if(obj.checked)
	{
		if(charge_abbr!=null && charge_abbr.length!=undefined)
	 	{
	  		for(var i=0;i<charge_abbr.length;i++)
	  		{
	  			var temp = document.getElementById("charge_abbr_"+i).value;
	  			//alert(temp)
	  			
	  			document.getElementById(temp+"_"+index+"_"+i).disabled=false;
	  			document.getElementById("eff_dt_"+temp+"_"+index+"_"+i).disabled=false;
	  		} 
	 	}
	 	else if(charge_abbr!=null)
	 	{
	 		var temp = document.getElementById("charge_abbr_0").value;
  			//alert(temp)
  			document.getElementById(temp+"_"+index+"_0").disabled=false;
  			document.getElementById("eff_dt_"+temp+"_"+index+"_0").disabled=false;
	 	}
	}
	else
	{
		if(charge_abbr!=null && charge_abbr.length!=undefined)
	 	{
	  		for(var i=0;i<charge_abbr.length;i++)
	  		{
	  			var temp = document.getElementById("charge_abbr_"+i).value;
	  			//alert(temp)
	  			document.getElementById(temp+"_"+index+"_"+i).disabled=true;
	  			document.getElementById("eff_dt_"+temp+"_"+index+"_"+i).disabled=true;
	  		} 
	 	}
	 	else if(charge_abbr!=null)
	 	{
	 		var temp = document.getElementById("charge_abbr_0").value;
  			//alert(temp)
  			document.getElementById(temp+"_"+index+"_0").disabled=true;
  			document.getElementById("eff_dt_"+temp+"_"+index+"_0").disabled=true;
	 	}
	}
}


function doTcqRequest()
{
	var tcq_sign = document.forms[0].tcq_sign.value
	var var_tcq = document.forms[0].var_tcq.value
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	var contract_type = document.forms[0].contract_type.value;
	var clearance=document.forms[0].clearance.value;
	
	var dda_dt = document.forms[0].dda_dt.value;
	var dda_time = document.forms[0].dda_time.value;
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
	var supplied_qty = document.forms[0].supplied_qty.value;
	
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	var no_of_security_dtl = document.forms[0].no_of_security_dtl.value;
	
	var agreement_type = document.forms[0].agreement_type;
	var agreement_base = document.forms[0].agreement_base;
	
	var chk_plant = document.forms[0].chk_plant;
	var chk_plant_abbr = document.forms[0].chk_plant_abbr;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	
	var charge_abbr = document.forms[0].charge_abbr;
	
	var temp_start_dt = document.forms[0].temp_start_dt.value;
	var temp_end_dt = document.forms[0].temp_end_dt.value;
	var sysdate=document.forms[0].sysdate.value;
	var val_dt = compareDate(sysdate,temp_end_dt);
	
	var msg="";
	var flag=true;
	
	if(temp_start_dt != "" & temp_end_dt != "")
	{
		if(parseInt(val_dt) == 1)
		{
			msg+="Contract is expired! - TCQ Change Request Can't be Generated!";
			flag=false;
			
		}
	}
	else
	{
		msg+="Contract Start - End Date Missing!";
		flag=false;
	}
	
	if (flag)
	{	
		if(trim(tcq_sign)=="")
		{
			msg+="Select TCQ Sign!\n";
			flag=false;	
		}
		if(trim(var_tcq)=="")
		{
			msg+="Enter TCQ Qty for Modification!\n";
			flag=false;	
		}
		
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
		if(contract_type=="S")
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
		
		if(agreement_type[0].checked == false && agreement_type[1].checked == false)
		{
			msg+="Select Agreement Type!\n";
			flag=false;
		}
		if(agreement_base[0].checked == false && agreement_base[1].checked == false)
		{
			msg+="Select Agreement Base!\n";
			flag=false;
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
						if(agreement_base[1].checked == true)
						{
							if(charge_abbr!=null && charge_abbr.length!=undefined)
						 	{
						  		for(var j=0;j<charge_abbr.length;j++)
						  		{
						  			var temp = document.getElementById("charge_abbr_"+j).value;
						  			//alert(temp)
						  			if(temp=="TC")
						  			{
						  				var chrRate=document.getElementById(temp+"_"+i+"_"+j).value;
							  			var chrEff=document.getElementById("eff_dt_"+temp+"_"+i+"_"+j).value;
							  			
							  			if(trim(chrRate)=="")
										{
											msg+="Enter Transportation Charges for "+chk_plant_abbr[i].value+"!\n";
											flag=false;
										}
							  			
							  			if(trim(chrEff)=="")
										{
											msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr[i].value+"!\n";
											flag=false;
										}
									}
						  		} 
						 	}
						 	else if(charge_abbr!=null)
						 	{
						 		var temp = document.getElementById("charge_abbr_0").value;
					  			//alert(temp)
					  			if(temp=="TC")
					  			{
					  				var chrRate=document.getElementById(temp+"_"+i+"_0").value;
						  			var chrEff=document.getElementById("eff_dt_"+temp+"_"+i+"_0").value;
						  			
						  			if(trim(chrRate)=="")
									{
										msg+="Enter Transportation Charges for "+chk_plant_abbr[i].value+"!\n";
										flag=false;
									}
						  			
						  			if(trim(chrEff)=="")
									{
										msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr[i].value+"!\n";
										flag=false;
									}
								}
						 	}
						}		
					}
				}
			}
			else
			{
				if(chk_plant.checked)
				{
					chkFlg = true;
					if(agreement_base[1].checked == true)
					{
						if(charge_abbr!=null && charge_abbr.length!=undefined)
					 	{
					  		for(var j=0;j<charge_abbr.length;j++)
					  		{
					  			var temp = document.getElementById("charge_abbr_"+j).value;
					  			//alert(temp)
					  			if(temp=="TC")
					  			{
					  				var chrRate=document.getElementById(temp+"_0_"+j).value;
						  			var chrEff=document.getElementById("eff_dt_"+temp+"_0_"+j).value;
						  			
						  			if(trim(chrRate)=="")
									{
										msg+="Enter Transportation Charges for "+chk_plant_abbr.value+"!\n";
										flag=false;
									}
						  			
						  			if(trim(chrEff)=="")
									{
										msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr.value+"!\n";
										flag=false;
									}
								}
					  		} 
					 	}
					 	else if(charge_abbr!=null)
					 	{
					 		var temp = document.getElementById("charge_abbr_0").value;
				  			//alert(temp)
				  			if(temp=="TC")
				  			{
				  				var chrRate=document.getElementById(temp+"_0_0").value;
					  			var chrEff=document.getElementById("eff_dt_"+temp+"_0_0").value;
					  			
					  			if(trim(chrRate)=="")
								{
									msg+="Enter Transportation Charges for "+chk_plant_abbr.value+"!\n";
									flag=false;
								}
					  			
					  			if(trim(chrEff)=="")
								{
									msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr.value+"!\n";
									flag=false;
								}
							}
					 	}
					}
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
		
		if(trim(supplied_qty) != "")
		{
			var rem_qty=parseFloat(tcq)-parseFloat(supplied_qty);
			if(parseFloat(rem_qty)<0)
			{
				rem_qty=parseFloat("0.00");
			}
			if(tcq_sign=="-")
			{
				if(parseFloat(var_tcq) > parseFloat(rem_qty))
				{
					msg += "TCQ : "+tcq+"\nSupplied MMBTU : "+supplied_qty+"\nTransferable MMBTU : "+round(parseFloat(rem_qty),2)+"\n";
					flag=false;
				}
			}
		}
	}
	
	if(flag)
	{
		var a = confirm("New Contract Revision will be Submitted Along With TCQ Modification Request!\n\nDo You Want To Revise The Contract Details, With TCQ Modification Request?");
		if(a)
		{
			document.forms[0].change_request.value="TCQ";
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function doContractDurationChangeRequest()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	var contract_type = document.forms[0].contract_type.value;
	var clearance=document.forms[0].clearance.value;
	
	var dda_dt = document.forms[0].dda_dt.value;
	var dda_time = document.forms[0].dda_time.value;
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
	var supplied_qty = document.forms[0].supplied_qty.value;
	
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	var no_of_security_dtl = document.forms[0].no_of_security_dtl.value;
	
	var agreement_type = document.forms[0].agreement_type;
	var agreement_base = document.forms[0].agreement_base;
	
	var chk_plant = document.forms[0].chk_plant;
	var chk_plant_abbr = document.forms[0].chk_plant_abbr;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	
	var charge_abbr = document.forms[0].charge_abbr;
	
	var temp_start_dt = document.forms[0].temp_start_dt.value;
	var temp_end_dt = document.forms[0].temp_end_dt.value;
	var sysdate=document.forms[0].sysdate.value;
	var val_dt = compareDate(sysdate,temp_end_dt);
	
	var msg="";
	var flag=true;
	
	/* if(temp_start_dt != "" & temp_end_dt != "")
	{
		if(parseInt(val_dt) == 1)
		{
			msg+="Contract Expired! - Price change request cann't be Generated!";
			flag=false;
		}
	}
	else */
	if(temp_start_dt == "" & temp_end_dt == "")
	{
		msg+="Contract Start - End Date Missing!";
		flag=false;
	} 
	
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
	if(contract_type=="S")
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
	
	if(agreement_type[0].checked == false && agreement_type[1].checked == false)
	{
		msg+="Select Agreement Type!\n";
		flag=false;
	}
	if(agreement_base[0].checked == false && agreement_base[1].checked == false)
	{
		msg+="Select Agreement Base!\n";
		flag=false;
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
					if(agreement_base[1].checked == true)
					{
						if(charge_abbr!=null && charge_abbr.length!=undefined)
					 	{
					  		for(var j=0;j<charge_abbr.length;j++)
					  		{
					  			var temp = document.getElementById("charge_abbr_"+j).value;
					  			//alert(temp)
					  			if(temp=="TC")
					  			{
					  				var chrRate=document.getElementById(temp+"_"+i+"_"+j).value;
						  			var chrEff=document.getElementById("eff_dt_"+temp+"_"+i+"_"+j).value;
						  			
						  			if(trim(chrRate)=="")
									{
										msg+="Enter Transportation Charges for "+chk_plant_abbr[i].value+"!\n";
										flag=false;
									}
						  			
						  			if(trim(chrEff)=="")
									{
										msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr[i].value+"!\n";
										flag=false;
									}
								}
					  		} 
					 	}
					 	else if(charge_abbr!=null)
					 	{
					 		var temp = document.getElementById("charge_abbr_0").value;
				  			//alert(temp)
				  			if(temp=="TC")
				  			{
				  				var chrRate=document.getElementById(temp+"_"+i+"_0").value;
					  			var chrEff=document.getElementById("eff_dt_"+temp+"_"+i+"_0").value;
					  			
					  			if(trim(chrRate)=="")
								{
									msg+="Enter Transportation Charges for "+chk_plant_abbr[i].value+"!\n";
									flag=false;
								}
					  			
					  			if(trim(chrEff)=="")
								{
									msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr[i].value+"!\n";
									flag=false;
								}
							}
					 	}
					}		
				}
			}
		}
		else
		{
			if(chk_plant.checked)
			{
				chkFlg = true;
				if(agreement_base[1].checked == true)
				{
					if(charge_abbr!=null && charge_abbr.length!=undefined)
				 	{
				  		for(var j=0;j<charge_abbr.length;j++)
				  		{
				  			var temp = document.getElementById("charge_abbr_"+j).value;
				  			//alert(temp)
				  			if(temp=="TC")
				  			{
				  				var chrRate=document.getElementById(temp+"_0_"+j).value;
					  			var chrEff=document.getElementById("eff_dt_"+temp+"_0_"+j).value;
					  			
					  			if(trim(chrRate)=="")
								{
									msg+="Enter Transportation Charges for "+chk_plant_abbr.value+"!\n";
									flag=false;
								}
					  			
					  			if(trim(chrEff)=="")
								{
									msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr.value+"!\n";
									flag=false;
								}
							}
				  		} 
				 	}
				 	else if(charge_abbr!=null)
				 	{
				 		var temp = document.getElementById("charge_abbr_0").value;
			  			//alert(temp)
			  			if(temp=="TC")
			  			{
			  				var chrRate=document.getElementById(temp+"_0_0").value;
				  			var chrEff=document.getElementById("eff_dt_"+temp+"_0_0").value;
				  			
				  			if(trim(chrRate)=="")
							{
								msg+="Enter Transportation Charges for "+chk_plant_abbr.value+"!\n";
								flag=false;
							}
				  			
				  			if(trim(chrEff)=="")
							{
								msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr.value+"!\n";
								flag=false;
							}
						}
				 	}
				}
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


function doPriceChangeRequest()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	var contract_type = document.forms[0].contract_type.value;
	var clearance=document.forms[0].clearance.value;
	
	var dda_dt = document.forms[0].dda_dt.value;
	var dda_time = document.forms[0].dda_time.value;
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
	var supplied_qty = document.forms[0].supplied_qty.value;
	
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	var no_of_security_dtl = document.forms[0].no_of_security_dtl.value;
	
	var agreement_type = document.forms[0].agreement_type;
	var agreement_base = document.forms[0].agreement_base;
	
	var chk_plant = document.forms[0].chk_plant;
	var chk_plant_abbr = document.forms[0].chk_plant_abbr;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	
	var charge_abbr = document.forms[0].charge_abbr;
	
	var temp_start_dt = document.forms[0].temp_start_dt.value;
	var temp_end_dt = document.forms[0].temp_end_dt.value;
	var sysdate=document.forms[0].sysdate.value;
	var val_dt = compareDate(sysdate,temp_end_dt);
	
	var msg="";
	var flag=true;
	
	if(temp_start_dt != "" & temp_end_dt != "")
	{
		if(parseInt(val_dt) == 1)
		{
			msg+="Contract Expired! - Price change request cann't be Generated!";
			flag=false;
		}
	}
	else
	{
		msg+="Contract Start - End Date Missing!";
		flag=false;
	}
	
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
	if(contract_type=="S")
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
	
	if(agreement_type[0].checked == false && agreement_type[1].checked == false)
	{
		msg+="Select Agreement Type!\n";
		flag=false;
	}
	if(agreement_base[0].checked == false && agreement_base[1].checked == false)
	{
		msg+="Select Agreement Base!\n";
		flag=false;
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
					if(agreement_base[1].checked == true)
					{
						if(charge_abbr!=null && charge_abbr.length!=undefined)
					 	{
					  		for(var j=0;j<charge_abbr.length;j++)
					  		{
					  			var temp = document.getElementById("charge_abbr_"+j).value;
					  			//alert(temp)
					  			if(temp=="TC")
					  			{
					  				var chrRate=document.getElementById(temp+"_"+i+"_"+j).value;
						  			var chrEff=document.getElementById("eff_dt_"+temp+"_"+i+"_"+j).value;
						  			
						  			if(trim(chrRate)=="")
									{
										msg+="Enter Transportation Charges for "+chk_plant_abbr[i].value+"!\n";
										flag=false;
									}
						  			
						  			if(trim(chrEff)=="")
									{
										msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr[i].value+"!\n";
										flag=false;
									}
								}
					  		} 
					 	}
					 	else if(charge_abbr!=null)
					 	{
					 		var temp = document.getElementById("charge_abbr_0").value;
				  			//alert(temp)
				  			if(temp=="TC")
				  			{
				  				var chrRate=document.getElementById(temp+"_"+i+"_0").value;
					  			var chrEff=document.getElementById("eff_dt_"+temp+"_"+i+"_0").value;
					  			
					  			if(trim(chrRate)=="")
								{
									msg+="Enter Transportation Charges for "+chk_plant_abbr[i].value+"!\n";
									flag=false;
								}
					  			
					  			if(trim(chrEff)=="")
								{
									msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr[i].value+"!\n";
									flag=false;
								}
							}
					 	}
					}		
				}
			}
		}
		else
		{
			if(chk_plant.checked)
			{
				chkFlg = true;
				if(agreement_base[1].checked == true)
				{
					if(charge_abbr!=null && charge_abbr.length!=undefined)
				 	{
				  		for(var j=0;j<charge_abbr.length;j++)
				  		{
				  			var temp = document.getElementById("charge_abbr_"+j).value;
				  			//alert(temp)
				  			if(temp=="TC")
				  			{
				  				var chrRate=document.getElementById(temp+"_0_"+j).value;
					  			var chrEff=document.getElementById("eff_dt_"+temp+"_0_"+j).value;
					  			
					  			if(trim(chrRate)=="")
								{
									msg+="Enter Transportation Charges for "+chk_plant_abbr.value+"!\n";
									flag=false;
								}
					  			
					  			if(trim(chrEff)=="")
								{
									msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr.value+"!\n";
									flag=false;
								}
							}
				  		} 
				 	}
				 	else if(charge_abbr!=null)
				 	{
				 		var temp = document.getElementById("charge_abbr_0").value;
			  			//alert(temp)
			  			if(temp=="TC")
			  			{
			  				var chrRate=document.getElementById(temp+"_0_0").value;
				  			var chrEff=document.getElementById("eff_dt_"+temp+"_0_0").value;
				  			
				  			if(trim(chrRate)=="")
							{
								msg+="Enter Transportation Charges for "+chk_plant_abbr.value+"!\n";
								flag=false;
							}
				  			
				  			if(trim(chrEff)=="")
							{
								msg+="Select Transportation Charges Eff.Dt for "+chk_plant_abbr.value+"!\n";
								flag=false;
							}
						}
				 	}
				}
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
		var a = confirm("New Contract Revision will be Submitted Along With Price Change Request!\n\nDo You Want To Revise The Contract Details, With Price Change Request?");
		if(a)
		{
			document.forms[0].change_request.value="PRICE";
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
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
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	var contract_type = document.forms[0].contract_type.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var agmt_type = "F";
	var rate = document.forms[0].rate.value;
	var rate_unit = document.forms[0].rate_unit.value;
	var tcq = document.forms[0].tcq.value;
	var dcq = document.forms[0].dcq.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_cont_liability_dtl.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agmt_type+
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
			newWindow = window.open(url,"Confirmation Notice Liability Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Confirmation Notice Liability Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
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
				alert("Contract Contract/Trade Date ("+signing_dt+") should be Greater than or equals to Agreement Contract/Trade Date ("+agmt_signing_dt+") !");
				document.forms[0].signing_dt.value="";
				
				return false;
			}
			
			if(temp_agmt_end_dt < tmp_sig_dt)
			{
				alert("Contract Contract/Trade Date ("+signing_dt+") should be Less than Agreement End date ("+agmt_end_dt+") !");
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
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_master.DataBean_ContractMaster" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
<%
String dateTime = utildate.getSysdateWithTime24hr();
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String clearance = request.getParameter("clearance")==null?"KYC":request.getParameter("clearance");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String agmt_nm = request.getParameter("agmt_nm")==null?"":request.getParameter("agmt_nm");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");

contract.setCallFlag("SUPPLY_CONTRACT_MST");
contract.setClearance(clearance);
contract.setCounterparty_cd(counterparty_cd);
contract.setComp_cd(owner_cd);
contract.setOpration(opration);
contract.setAgmt_no(agmt_no);
contract.setAgreement_type("F");
contract.setAgmt_rev_no(agmt_rev_no);
contract.setCont_no(cont_no);
contract.setCont_rev_no(cont_rev_no);
contract.setContract_type(contract_type);
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

Vector VGX_BU_CD = contract.getVGX_BU_CD();
Vector VGX_BU_PLANT_NM = contract.getVGX_BU_PLANT_NM();
Vector VGX_BU_PLANT_ABBR = contract.getVGX_BU_PLANT_ABBR();
Vector VGX_BU_PLANT_SEQ_NO = contract.getVGX_BU_PLANT_SEQ_NO();

Vector VSEL_GX_BU_CD = contract.getVSEL_GX_BU_CD();
Vector VSEL_GX_BU_PLANT_SEQ_NO = contract.getVSEL_GX_BU_PLANT_SEQ_NO();
Vector VSEL_PLANT_SEQ_NO = contract.getVSEL_PLANT_SEQ_NO();
Vector VSEL_CHARGE_VALUE = contract.getVSEL_CHARGE_VALUE();
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

Vector VCHARGE_ABBR = contract.getVCHARGE_ABBR();
Vector VCHARGE_NAME = contract.getVCHARGE_NAME();

String min_counterparty_eff_dt = contract.getMin_counterparty_eff_dt();

//String cont_no = contract.getCont_no();
//String cont_rev_no = contract.getCont_rev_no();
String cont_ref_no = contract.getCont_ref_no();
String trade_ref_no = contract.getTrade_ref_no();
String signing_dt = contract.getSigning_dt();
String signing_time = contract.getSigning_time();
String dda_dt = contract.getDda_dt();
String dda_time = contract.getDda_time();
String ent_dt = contract.getEnt_dt();
String ent_time = contract.getEnt_time();
String start_dt = contract.getStart_dt();
String end_dt = contract.getEnd_dt();
String agmt_base = contract.getAgmt_base();
String agmt_type = contract.getAgmt_type();
String tcq = contract.getTcq();
String dcq = contract.getDcq();
String quantity_unit = contract.getQuantity_unit();
String rate = contract.getRate();
String rate_unit = contract.getRate_unit();
String post_margin = contract.getPost_margin();
String transportation_charges = contract.getTransportation_charges();
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
String cont_name = contract.getCont_name();
String dealMapping = contract.getDealMapping();
String contpty_abbr = contract.getContpty_abbr();
String fcc_flg = contract.getFcc_flg();
String cont_status_flg = contract.getCont_status_flg();
String cont_status = contract.getCont_status();
String is_allocated = contract.getIs_allocated();
String txn_charges = contract.getTxn_charges();
String txn_unit = contract.getTxn_unit();
String buy_nom_cutoff_time = contract.getBuy_nom_cutoff_time();
String no_of_billing_dtl=contract.getNo_of_billing_dtl();
String no_of_security_dtl=contract.getNo_of_security_dtl();
String supplied_qty=contract.getSupplied_qty();
String contract_offset_qty=contract.getContract_offset_qty();

String agmt_agmt_base=contract.getAgmt_agmt_base();
String gt_contract_dtl=contract.getGt_contract_dtl();
String tcq_request_flag=contract.getTcq_request_flag();
String contdt_change_request_flag = contract.getContdt_change_request_flag();
String var_tcq=contract.getVar_tcq();
String tcq_sign=contract.getTcq_sign();
String price_change_request_flag = contract.getPrice_change_request_flag();
String price_change_history=contract.getPrice_change_history();

String gx_counterparty_cd = contract.getGx_counterparty_cd();
String is_inv_submitted = contract.getIs_inv_submitted();
String max_date = contract.getMax_date();
String min_nom_date = contract.getMin_nom_date();

String agmt_signing_dt = contract.getAgmt_signing_dt();
String agmt_start_dt = contract.getAgmt_start_dt();
String agmt_end_dt = contract.getAgmt_end_dt();

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
if(buy_nom_cutoff_time.equals("")){
	buy_nom_cutoff_time="00:00";
}
if(post_margin.equals("")){
	post_margin="15";
}
if(txn_charges.equals("")){
	txn_charges="4";
}
if(agmt_base.equals("") || agmt_base.equals("B")){ //B means both agreement base
	agmt_base="X";
}
if(agmt_type.equals("")){
	agmt_type="0";
}
if(day_start_time.equals("")){
	day_start_time="06:00";
}
if(day_end_time.equals("")){
	day_end_time="06:00";
}
/*if(rate_unit.equals("")){
	rate_unit="1";
}*/
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

String bill_flag = contract.getBill_flag();
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
String no_of_liability_dtl=contract.getNo_of_liability_dtl();

String strTransCd="";
String strTrans="";
String strPlant="";
String strBuPlant="";
String strPlantChrg="";
String strGxBuPlant="";

for(int i=0;i<VSEL_TRANS_CD.size();i++)
{
	strTransCd = strTransCd + VSEL_TRANS_CD.elementAt(i)+"@";
	strTrans = strTrans + VSEL_TRANS_PLANT_SEQ_NO.elementAt(i)+"@";
}
for(int i=0;i<VSEL_PLANT_SEQ_NO.size();i++)
{
	strPlant = strPlant + VSEL_PLANT_SEQ_NO.elementAt(i)+"@";
	strPlantChrg = strPlantChrg + VSEL_CHARGE_VALUE.elementAt(i)+"@";
}
for(int i=0;i<VSEL_BU_PLANT_SEQ_NO.size();i++)
{
	strBuPlant = strBuPlant + VSEL_BU_PLANT_SEQ_NO.elementAt(i)+"@";
}

for(int i=0;i<VSEL_GX_BU_PLANT_SEQ_NO.size();i++)
{
	strGxBuPlant = strGxBuPlant + VSEL_GX_BU_PLANT_SEQ_NO.elementAt(i)+"@";
}

//<!--Harsh Maheta 20230901 : Added for old values to show in Deal audit history-->//
String cp_name = ""+utilBean.getCounterpartyName(counterparty_cd);
String cp_abbr = ""+utilBean.getCounterpartyABBR(counterparty_cd);
String mapped_cont_no = utilBean.getDisplayDealMapping(agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type);

String old_value="CP="+counterparty_cd+"#NAME="+cp_name+"#ABBR="+cp_abbr+"#CONTNAME="+cont_name+"#CONTNO="+mapped_cont_no+"#CONTREFNO="+cont_ref_no+"#CONTTYPE="+contract_type+"#TRADE_REFNO="+trade_ref_no+"#DDADT="+dda_dt+"#DDATIME="+dda_time+"#SIGNDT="+signing_dt+"#SIGNTIME="+signing_time+
"#ENTDT="+ent_dt+"#ENTTIME="+ent_time+"#AGMTTYPE="+agmt_type+"#AGMTBASE="+agmt_base+"#STARTDT="+start_dt+"#ENDDT="+end_dt+"#RATE="+rate+"#RATEUNIT="+rate_unit+"#TCQ="+tcq+"#DCQ="+dcq+"#QTYMOD="+var_tcq+"#QUNIT="+quantity_unit+"#MDCQ="+mdcq_percentage+"#GXFEE="+txn_charges+"#GXFEEUNIT="+txn_unit+"#POSTMARG="+post_margin+"#CONT_STATUS="+cont_status;

%>
<%-- <body onload="setValues('<%=strTransCd%>','<%=strTrans%>','<%=strPlant%>','<%=strBuPlant%>','<%=strTransChrg%>','<%=strGxBuPlant%>','<%=strMktMrgin%>','<%=strOthChrg%>');liabilityShows();billingShows();messurmentShows();off_spec_gasShows();terminatorShows();"> --%>
<body onload="setValues('<%=strTransCd%>','<%=strTrans%>','<%=strPlant%>','<%=strBuPlant%>','<%=strPlantChrg%>','<%=strGxBuPlant%>');liabilityShows();billingShows();messurmentShows();off_spec_gasShows();terminatorShows();">
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
					    	Gas Supply Contract
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
				    				<!-- <input type="radio" name="rd_clear">&nbsp; KYC &nbsp;&nbsp;
				    				<input type="radio" name="rd_clear">&nbsp; IGX -->
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
							<%if(contract_type.equals("S") && opration.equals("INSERT")){ %>
								<div class="col">
				    		<%}else{ %>
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    		<%} %>
				    				<select class="form-select form-select-sm" name="contract_type" onchange="refresh('<%=opration%>','<%=clearance%>');" <%if(opration.equals("MODIFY") && (!cont_no.equals("0") && !cont_no.equals(""))){ %>style="pointer-events: none;"<%} %>>
				    					<%if(clearance.equals("IGX")){ %>
				    					<option value="X">IGX</option>
				    					<%}else{ %>
				    					<option value="">--Select--</option>
				    					<option value="L">LOA</option>
				    					<option value="S">SN</option>
				    					<%} %>
				    				</select>
				    				<script>document.forms[0].contract_type.value="<%=contract_type%>"</script>
				      			</div>
				      			<%if(contract_type.equals("S") && opration.equals("INSERT")){ %>
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
				    				<input type="text" class="form-control form-control-sm" name="cont_name" value="<%=cont_name%>" maxLength="50" readOnly>
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
			      						onblur="validateDate(this);<%if(contract_type.equals("S")){ %>checkSigningDdaDate();<%} %>" onchange="validateDate(this);<%if(contract_type.equals("S")){ %>checkSigningDdaDate();<%} %>" autocomplete="off">
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
			      						onblur="validateDate(this);checkSigningStartDt(document.forms[0].start_dt, this.value);<%if(contract_type.equals("S")){ %>checkSigningDdaDate();<%} %>" 
			      						onchange="validateDate(this);checkSigningStartDt(document.forms[0].start_dt,this.value);<%if(contract_type.equals("S")){ %>checkSigningDdaDate();<%} %>" autocomplete="off">
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
							<label class="form-label"><b>Agreement Type<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<!-- JD <div class="col-sm-12 col-xs-12 col-md-12"> -->
					      				<input type="radio" name="agreement_type" value="0" <%if(agmt_type.equals("0")){%>checked<%}%>>&nbsp;Term&nbsp;&nbsp;
				      					<input type="radio" name="agreement_type" value="1" <%if(agmt_type.equals("1")){%>checked<%}%>>&nbsp;Spot&nbsp;&nbsp;
					    			<!--  </div>-->
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label" title="Agreement AGMT BASE : <%=agmt_agmt_base%>"><b>Agreement Base<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="radio" name="agreement_base" value="X" <%if(agmt_base.equals("X")){%>checked<%}%> onclick="showHide('X');">&nbsp;Ex-Terminal&nbsp;&nbsp;
				      				<input type="radio" name="agreement_base" value="D" <%if(agmt_base.equals("D")){%>checked<%}%> onclick="showHide('D');">&nbsp;Delivery&nbsp;&nbsp; 
				    				<%if(contract_type.equals("S")){ %>
				    					<%if(agmt_agmt_base.equals("X")){ %>
				    					<script>document.forms[0].agreement_base[1].style.pointerEvents = "none";</script>
				    					<%}else if(agmt_agmt_base.equals("D")){ %>
				    					<script>document.forms[0].agreement_base[0].style.pointerEvents = "none";</script>
				    					<%} %>
				    				<%} %>
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
											onchange="validateDate(this);checkEffDtWithContStDt(this,'<%=min_counterparty_eff_dt%>');checkStartEndDate(this,document.forms[0].end_dt,'F');checkSigningStartDt(this,document.forms[0].signing_dt.value);<%if(contract_type.equals("S")){ %>checkAgmtStartdate();<%} %>"
											autocomplete="off"
											<%if((!cont_status_flg.equals("F")&& !contdt_change_request_flag.equals("Y")) || !is_inv_submitted.equals("0")) {%>
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
			      						onchange="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');<%if(contract_type.equals("S")){ %>checkAgmtEnddate();<%} %>" autocomplete="off"
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
										<%if(!clearance.equals("IGX")){ %>
										<option value="R">Incoming</option>
										<%} %>
										<option value="I">Outgoing</option>
									</select>
								</div>
				    			<div class="col">
				    				<input type="button" name="security_btn" class="btn btn-sm config_btn" value="Security Config" onclick="securityPreReceipt('<%=contract_type%>','<%=agmt_no%>','<%=agmt_rev_no%>','<%=cont_no%>','<%=cont_rev_no%>');">
				  					<input type="hidden" name="securityFlag" value="N">
				  				</div>
				  				<%if(clearance.equals("IGX")){ %>
				  				<script>document.forms[0].sec_category.style.pointerEvents = "none";</script>
				  				<script>document.forms[0].security_btn.style.pointerEvents = "none";</script>
				  				<%} %>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Credit Exceed Days</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">								
				    			<div class="col">
				    				<input type="button" name="credit_exceed_btn" class="btn btn-sm config_btn" value="Credit Exceed Config" onclick="creditExceedDaysConfig('<%=contract_type%>','<%=agmt_no%>','<%=agmt_rev_no%>','<%=cont_no%>','<%=cont_rev_no%>');">
				  				</div>				  				
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
				      				<%if(is_allocated.equals("Y")){ %>readOnly<%} %> >
		      					</div>
				    			<div class="col">
		      						<select class="form-select form-select-sm" name="rate_unit" onchange="checkRateFormate(this);">
		      							<option value="">--Select--</option>
		      							<option value="2">USD / MMBTU</option>
		      							<option value="1">INR / MMBTU</option>
		      						</select>
		      						<script>document.forms[0].rate_unit.value="<%=rate_unit%>"</script>
		      						<%if(is_allocated.equals("Y")){ %>
		      						<script>document.forms[0].rate_unit.style.pointerEvents = "none";</script>
		      						<%} %>
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
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>TCQ<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="tcq" value="<%=tcq%>" onblur="negNumber(this);checkNumber1(this,10,2);" <%if(is_allocated.equals("Y")){ %>readOnly<%} %>>
				    			</div>
				    			<div class="col">
		      						<select class="form-select form-select-sm" name="quantity_unit">
		      							<option value="1">MMBTU</option>
		      						</select>
		      						<script>document.forms[0].quantity_unit.value="<%=quantity_unit%>"</script>
		      						<%if(is_allocated.equals("Y")){ %>
		      						<script>document.forms[0].quantity_unit.style.pointerEvents = "none";</script>
		      						<%} %>
				    			</div>
				    			<div class="col">
				    			<%if(is_allocated.equals("Y")){ %>
				    			<i class="fa fa-check-circle fa-2x" style="color:green;" title="Purchased MMBTU mapped"></i>
				    			<%}else{ %>
				    			<i class="fa fa-times-circle fa-2x" style="color:red;" title="Purchased MMBTU not mapped"></i>
				    			<%} %>
				    			</div>
				    			
				    			<!-- <div class="col">
				    				<input type="button" class="btn btn-sm request_btn" value="TCQ Change Request">
				  				</div> -->
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>TCQ Variation</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="" value="TBD" maxLength="100" readonly>
					    			</div>
				    			</div>
				    			<div class="col">
				      				<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="" value="TBD" maxLength="100" readonly>
					    			</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5" <%if((opration.equals("INSERT") || (cont_no.equals("0") || cont_no.equals(""))) || !is_allocated.equals("Y")){%>style="display:none;"<%} %>>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Quantity Modification</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-auto">
		      						<select class="form-select form-select-sm" name="tcq_sign">
		      							<option value="+">+</option>
		      							<option value="-">-</option>
		      						</select>
		      						<%if(!tcq_sign.equals("")) {%>
		      						<script>document.forms[0].tcq_sign.value="<%=tcq_sign%>"</script>
		      						<%} %>
		      					</div>
				    			<div class="col-auto">
				      				<input type="text" class="form-control form-control-sm" name="var_tcq" value="<%=var_tcq%>" onblur="negNumber(this);checkNumber1(this,10,2);">
				    			</div>
				    			<div class="col-auto">
				    				<input type="button" class="btn btn-sm request_btn" value="TCQ Change Request" onclick="doTcqRequest();" <%if(!fcc_flg.equals("Y")){%>disabled<%} %>>
				    			</div>
				  			</div>
						</div>						
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Supplied MMBTU</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="supplied_qty" value="<%=supplied_qty%>" readOnly style="background:#99ffcc;">
				    			</div>
				  			</div>
						</div>
						<% if (!contract_offset_qty.equals("0.00") && !contract_offset_qty.equals("")) {%>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label" style="color: blue;"><b>Migration Offset MMBTU</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="offset_qty" value="<%=contract_offset_qty%>" readOnly style="background:#99ffcc;">
				    			</div>
				  			</div>
						</div>
						<%} %>
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
				    			<%if(opration.equals("MODIFY")){ %>
				    			<div class="col">
				    				<input type="button" class="btn btn-sm config_btn" value="Variable DCQ Config" onclick="variableDCQ('<%=contract_type%>','<%=agmt_no%>','<%=agmt_rev_no%>','<%=cont_no%>','<%=cont_rev_no%>');">
				    			</div>			    		
				    			<%} %>	
				  			</div>
						</div>
						<%-- <div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="button" class="btn btn-sm config_btn" value="Variable DCQ Config" data-bs-toggle="modal" data-bs-target="">
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<%= utilmsg.infoMessage("TBD")%>
				    			</div>
				  			</div>
						</div> --%>
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
							<label class="form-label"><b>GX Transaction Fee<span class="s-red">*</span></b></label>
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
					      				<input type="text" class="form-control form-control-sm" name="post_margin" value="<%=post_margin%>"  onkeyup="checkForNumber(this);" onblur="checkNumber1(this,5,2);">
					    			</div>
				    			</div>
				  			</div>
						</div>
					</div>
					&nbsp;
					<%if(contract_type.equals("X")){ %>
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
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="button" class="btn btn-sm config_btn" value="Delivery Pt Config" title="Transporter Entry Point / Delivery Point-<%=owner_abbr%>" 
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
				    			<div class="col-sm-12 col-xs-12 col-md-12">
			    					<input type="button" class="btn btn-sm config_btn" value="Customer Plant Config"  
			    					data-bs-toggle="modal" data-bs-target="#CustConfigModal">
				    			</div>
				  			</div>
						</div>
						
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				
				    				<label class="form-label" id="custDisplay" style="display:none;"></label>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5" id="linkTrans">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Linked GTC</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" value="<%=gt_contract_dtl%>" readOnly style="background:#99ffcc;">
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
							<div class="form-group row">
				    			<label class="form-label"><b><input type="checkbox" class="form-check-input" name="buyer_nom" value="Y" <%if(buy_nom_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Buyer Nomination</b></label>
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
				    			<label class="form-label"><b><input type="checkbox" class="form-check-input" name="day_def" value="Y" <%if(day_def_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Day Definition</b></label>
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
					<%-- <div class="row m-b-5">
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
					</div> --%>
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
				    				<input type="button" class="btn btn-sm config_btn" value="Configure Liability" <%if(opration.equals("MODIFY")){ %>onclick="openLiabilityClause();"<%} else {%> disabled <%} %>>&nbsp;
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
						      		<input type="button" class="btn btn-sm config_btn" value="Configure Billing" <%if(opration.equals("MODIFY")){ %>onclick="openBillingDtl();"<%} else {%> disabled <%} %>>
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
						<%if(write_access.equals("Y") && !tcq_request_flag.equals("Y") 
								&& !price_change_request_flag.equals("Y")){ %>
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
					    	Gas Supply Contract - Change Request(s)
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
								<div class="col-auto">
									<input type="button" class="btn btn-sm request_btn" value="Price Change Request" onclick="doPriceChangeRequest();" <%if(!fcc_flg.equals("Y")){%>disabled<%}else if(!is_allocated.equals("Y")){%>disabled<%}%>>
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
						<%if(contdt_change_request_flag.equals("Y")){ %>
							<div class="row m-b-5">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.errorMessage("<b>Contract Duration Modification pending for the Contract!. The Contract will not appear for FCC!</b>")%>
								</div>
							</div>
						<%} %>
					<%} %>
					<%if(price_change_request_flag.equals("Y")){ %>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<%=utilmsg.infoMessage("<b>Price Change Request has been Generated for the Contract!</b>")%>
							</div>
						</div>
					<%}else if(tcq_request_flag.equals("Y")){ %>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<%=utilmsg.infoMessage("<b>TCQ Modification Request has been Generated for the Contract!</b>")%>
							</div>
						</div>					
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
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitTransSel();">
				</div>
      		</div>
      	</div>
	</div>
</div>

<!-- CUSTOMER MODEL -->
<div class="modal fade" id="CustConfigModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Customer Plant Configuration
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
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
				      							onclick="setTransChrg(this,'<%=i%>');" >
				      						</td>
				      						<td>
				      							<%=VPLANT_ABBR.elementAt(i)%>
				      							<input type="hidden" name="chk_plant_abbr" value="<%=VPLANT_ABBR.elementAt(i)%>">
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
			    						<td colspan="<%=(VCHARGE_ABBR.size()*2)+2%>" align="center"><%= utilmsg.warningMessage("Please configure Plants for Customer!")%></td>
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
					<input type="button" class="btn btn-warning com-btn" value="Proceed" onclick="doSubmitCustConfig('P');">
				</div>
      		</div>
      	</div>
	</div>
</div> 

<input type="hidden" name="option" value="SUPPLY_CONT_MST">
<input type="hidden" name="old_value" value="<%=old_value%>">

<input type="hidden" name="prev_clearance" value="<%=clearance%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="cont_status_flg" value="<%=cont_status_flg%>">
<input type="hidden" name="is_allocated" value="<%=is_allocated%>">

<input type="hidden" name="no_of_billing_dtl" value="<%=no_of_billing_dtl%>">
<input type="hidden" name="no_of_security_dtl" value="<%=no_of_security_dtl%>">

<input type="hidden" name="gx_counterparty_cd" value="<%=gx_counterparty_cd%>">

<input type="hidden" name="change_request" value="">

<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="is_inv_submitted" value="<%=is_inv_submitted%>">
<input type="hidden" name="contdt_change_request_flag" value="<%=contdt_change_request_flag%>">
<input type="hidden" name="max_date" value="<%=max_date%>">
<input type="hidden" name="min_nom_date" value="<%=min_nom_date%>">


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
	document.getElementById("linkTrans").style.display="flex";
	</script>
<%}else{%>
	<script>
	document.getElementById("linkTrans").style.display="none";
	</script>
<%} %>


</form>
</body>
</html>