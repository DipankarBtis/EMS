<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
</head>
<script>
function refresh(opration)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var u = document.forms[0].u.value;
	
	var url = "frm_ltcora_cont_sell_mst.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&opration="+opration+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function enabledEffDt(obj)
{
	var opration = document.forms[0].opration.value;
	
	if(opration=="MODIFY")
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


function checkRateFormate(obj,terif_type) //WHEN CHANGE RATE UNIT
{ 
	var a="0"
	var b="0"
	
	var rate 
	
	if(terif_type=="lt_terif")
	{
		rate = document.forms[0].ltcora_tariff;
	}
	else if(terif_type=="stor_terif")
	{
		rate = document.forms[0].storage_tariff;
	}
	
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

function doSubmit()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	var contract_type = document.forms[0].contract_type.value;
	var buy_sale=document.forms[0].buy_sale.value;
	
	var dda_dt = document.forms[0].dda_dt.value;
	var dda_time = document.forms[0].dda_time.value;
	var signing_dt = document.forms[0].signing_dt.value;
	var signing_time = document.forms[0].signing_time.value;
	var ent_dt = document.forms[0].ent_dt.value;
	var ent_time = document.forms[0].ent_time.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var cont_ref_no = document.forms[0].cont_ref_no.value;
	
	//var cont_status_flg = document.forms[0].cont_status_flg.value;
	
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	var no_of_security_dtl = document.forms[0].no_of_security_dtl.value;
	
	var agmt_no = document.forms[0].agmt_no.value;
	var agreement_type = document.forms[0].agreement_type;
	var agreement_base = document.forms[0].agreement_base;
	
	var chk_plant = document.forms[0].chk_plant;
	var chk_plant_abbr = document.forms[0].chk_plant_abbr;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	
	var no_of_cargo = document.forms[0].no_of_cargo.value;
	var ltcora_tariff = document.forms[0].ltcora_tariff.value;
	var ltcora_tariff_unit = document.forms[0].ltcora_tariff_unit.value;
	var sug = document.forms[0].sug.value;

	var storage_tariff = document.forms[0].storage_tariff.value;
	var storage_tariff_unit = document.forms[0].storage_tariff_unit.value;
	
	var tlu_flag = document.forms[0].tlu_flag.value;
	
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
	
	if(agmt_no == "" || agmt_no == "0")
	{
		msg+="Select Agreement!\n";
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
	if(trim(cont_ref_no) == "")
	{
		msg+="Enter Contract Ref#!\n";
		flag=false;
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
		msg+="Enter Contract Date!\n";
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
	
	if(trim(storage_tariff) != "" && trim(storage_tariff) != null)
	{
		if(trim(storage_tariff_unit) == "")
		{
			msg+="Select Storage Tariff Unit\n";
			flag=false;
		}
	}
	
		
	
	/* if(agreement_type[0].checked == false && agreement_type[1].checked == false)
	{
		msg+="Select Agreement Type!\n";
		flag=false;
	} */
	/* if(agreement_base[0].checked == false && agreement_base[1].checked == false)
	{
		msg+="Select Agreement Base!\n";
		flag=false;
	} */
	
	if(agreement_base.checked == false)
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
		msg += "Please Select Atleast One Trader Plant!\n";
		flag=false;
	}	
	
	if(tlu_flag == "Y")
	{
		var chk_plant = document.forms[0].dlng_chk_plant;
		var chk_plant_abbr = document.forms[0].dlng_chk_plant_abbr;
		
		var chk_truck_trans = document.forms[0].chk_truck_trans;
		var chk_fill_station = document.forms[0].chk_fill_station;
		
		var charge_abbr = document.forms[0].charge_abbr;
		
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
						if(charge_abbr!=null && charge_abbr.length!=undefined)
					 	{
					  		for(var j=0;j<charge_abbr.length;j++)
					  		{
					  			var temp = document.getElementById("charge_abbr_"+j).value;
					  			//alert(temp)
					  			if(temp=="TLU")
					  			{
					  				var chrRate=document.getElementById(temp+"_"+i+"_"+j).value;
						  			var chrEff=document.getElementById("eff_dt_"+temp+"_"+i+"_"+j).value;
						  			
						  			if(trim(chrRate)=="")
									{
										msg+="Enter TLU Charges for "+chk_plant_abbr[i].value+"!\n";
										flag=false;
									}
						  			
						  			if(trim(chrEff)=="")
									{
										msg+="Select TLU Charges Eff.Dt for "+chk_plant_abbr[i].value+"!\n";
										flag=false;
									}
								}
					  		} 
					 	}
					 	else if(charge_abbr!=null)
					 	{
					 		var temp = document.getElementById("charge_abbr_0").value;
				  			//alert(temp)
				  			if(temp=="TLU")
				  			{
				  				var chrRate=document.getElementById(temp+"_"+i+"_0").value;
					  			var chrEff=document.getElementById("eff_dt_"+temp+"_"+i+"_0").value;
					  			
					  			if(trim(chrRate)=="")
								{
									msg+="Enter TLU Charges for "+chk_plant_abbr[i].value+"!\n";
									flag=false;
								}
					  			
					  			if(trim(chrEff)=="")
								{
									msg+="Select TLU Charges Eff.Dt for "+chk_plant_abbr[i].value+"!\n";
									flag=false;
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
					if(charge_abbr!=null && charge_abbr.length!=undefined)
				 	{
				  		for(var j=0;j<charge_abbr.length;j++)
				  		{
				  			var temp = document.getElementById("charge_abbr_"+j).value;
				  			//alert(temp)
				  			if(temp=="TLU")
				  			{
				  				var chrRate=document.getElementById(temp+"_0_"+j).value;
					  			var chrEff=document.getElementById("eff_dt_"+temp+"_0_"+j).value;
					  			
					  			if(trim(chrRate)=="")
								{
									msg+="Enter TLU Charges for "+chk_plant_abbr.value+"!\n";
									flag=false;
								}
					  			
					  			if(trim(chrEff)=="")
								{
									msg+="Select TLU Charges Eff.Dt for "+chk_plant_abbr.value+"!\n";
									flag=false;
								}
							}
				  		} 
				 	}
				 	else if(charge_abbr!=null)
				 	{
				 		var temp = document.getElementById("charge_abbr_0").value;
			  			//alert(temp)
			  			if(temp=="TLU")
			  			{
			  				var chrRate=document.getElementById(temp+"_0_0").value;
				  			var chrEff=document.getElementById("eff_dt_"+temp+"_0_0").value;
				  			
				  			if(trim(chrRate)=="")
							{
								msg+="Enter TLU Charges for "+chk_plant_abbr.value+"!\n";
								flag=false;
							}
				  			
				  			if(trim(chrEff)=="")
							{
								msg+="Select TLU Charges Eff.Dt for "+chk_plant_abbr.value+"!\n";
								flag=false;
							}
						}
				 	}
				}
			}
		}
		if(chkFlg==false)
		{
			msg += "Please Select Atleast One Customer-Plant For DLNG Service!\n";
			flag=false;
		}
		
		//FOR TRANSPORTER
		chkFlg = false;
		if(chk_truck_trans!=null && chk_truck_trans!=undefined)
		{
			if(chk_truck_trans.length!=undefined)
			{
				for(var i=0;i<chk_truck_trans.length;i++)
				{
					if(chk_truck_trans[i].checked)
					{
						chkFlg = true;		
					}
				}
			}
			else
			{
				if(chk_truck_trans.checked)
				{
					chkFlg = true;	
				}
			}
		}
		if(chkFlg==false)
		{
			msg += "Please Select Atleast One Truck Transporter!\n";
			flag=false;
		}
		
		//FOR FILLING STATION
		chkFlg = false;
		if(chk_fill_station!=null && chk_fill_station!=undefined)
		{
			if(chk_fill_station.length!=undefined)
			{
				for(var i=0;i<chk_fill_station.length;i++)
				{
					if(chk_fill_station[i].checked)
					{
						chkFlg = true;		
					}
				}
			}
			else
			{
				if(chk_fill_station.checked)
				{
					chkFlg = true;	
				}
			}
		}
		if(chkFlg==false)
		{
			msg += "Please Select Atleast One Filling Station!\n";
			flag=false;
		}
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
	
	if(trim(no_of_cargo) == "")
	{
		msg+="Enter Number of Cargo!\n";
		flag=false;
	}
	
	if(trim(ltcora_tariff) == "")
	{
		msg+="Enter LTCORA Tariff!\n";
		flag=false;
	}
	
	if(trim(ltcora_tariff_unit) == "")
	{
		msg+="Select LTCORA Tariff Unit!\n";
		flag=false;
	}

	if(trim(sug) == "")
	{
		msg+="Enter SUG%!\n";
		flag=false;
	}
	else
	{
		if(parseFloat(sug) > 100)
		{
			msg+="SUG% cant exceed 100%!\n";
			flag=false;
		}
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
		if(opration=="MODIFY")
		{
			/* if(cont_status_flg == "Y")
			{
				a = confirm("On saving your modification/s, this Contract will be Revised!\n\nDo you want to Proceed?");
			}
			else
			{ */
				a = confirm("Do you want to Modify Contract?");
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
					temp_msg += "Please filling the Billing Detail after Submitting Contract Detail!\n";
				}
			}
			else
			{
				temp_msg += "Please filling the Billing Detail after Submitting Contract Detail!!\n";
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


function setValues(strTransCd,strTrans,strPlant,strBuPlant,strFillStaCd,strFillStaAbbr,strTruckTransCd,strTruckTrans,strPlantChrg,strDlngPlant,strChargehistdesc)
{
	var chk_plant = document.forms[0].chk_plant;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var trans_cd = document.forms[0].trans_cd;
	var trans_plant_seq_no = document.forms[0].trans_plant_seq_no;
	var trans_plant_abbr = document.forms[0].trans_plant_abbr;
	var dlng_chk_plant = document.forms[0].dlng_chk_plant;
	
	var chk_truck_trans = document.forms[0].chk_truck_trans;
	var chk_fill_station = document.forms[0].chk_fill_station;
	
	var charge_abbr = document.forms[0].charge_abbr;
	
	strChargehistdesc = strChargehistdesc.replace(/~/g, "\n");
	
	var sepTransCd = strTransCd.split("@");
	var sepTrans = strTrans.split("@");
	var sepPlant = strPlant.split("@");
	var sepDlngPlant = strDlngPlant.split("@");
	var sepBuPlant = strBuPlant.split("@");

	var sepTruckTransCd = strTruckTransCd.split("@");
	var sepTruckTrans = strTruckTrans.split("@");
	var sepFillStaCd = strFillStaCd.split("@");
	var sepFillStaAbbr = strFillStaAbbr.split("@");
	var sepPlantChrg = strPlantChrg.split("@");
	var sepstrChargehistdesc = strChargehistdesc.split("@");
	
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
	
	//FOR TRUCK-TRANSPORTER
	if(chk_truck_trans!=null && chk_truck_trans.length!=undefined)
 	{
  		for(var i=0;i<chk_truck_trans.length;i++)
  		{
   			for(var j=0;j<sepTruckTransCd.length;j++)
   			{
     			if(chk_truck_trans[i].value == sepTruckTransCd[j])
     			{
     				chk_truck_trans[i].checked = true;
     			}
   			} 
  		} 
 	}
 	else if(chk_truck_trans!=null)
 	{
   		for(var j=0;j<sepTruckTransCd.length;j++)
   		{
   			if(chk_truck_trans.value == sepTruckTransCd[j])
     		{
   				chk_truck_trans.checked = true;
     		}
   		} 
 	}
	
	//FOR FILLING STATION
	if(chk_fill_station!=null && chk_fill_station.length!=undefined)
 	{
  		for(var i=0;i<chk_fill_station.length;i++)
  		{
   			for(var j=0;j<sepFillStaCd.length;j++)
   			{
     			if(chk_fill_station[i].value == sepFillStaCd[j])
     			{
     				chk_fill_station[i].checked = true;
     			}
   			} 
  		} 
 	}
 	else if(chk_fill_station!=null)
 	{
   		for(var j=0;j<sepFillStaCd.length;j++)
   		{
   			if(chk_fill_station.value == sepFillStaCd[j])
     		{
   				chk_fill_station.checked = true;
     		}
   		} 
 	}
	
	//FOR TLU PLANT
	if(dlng_chk_plant!=null && dlng_chk_plant.length!=undefined)
 	{
  		for(var i=0;i<dlng_chk_plant.length;i++)
  		{
   			for(var j=0;j<sepDlngPlant.length;j++)
   			{
     			if(dlng_chk_plant[i].value == sepDlngPlant[j])
     			{
     				document.getElementById("history_btn_"+sepDlngPlant[j]).setAttribute("title", sepstrChargehistdesc[j]);

     				dlng_chk_plant[i].checked = true;
     				
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
	     		  			document.getElementById("max_eff_dt_"+temp+"_"+i+"_"+k).value=tempPlantChrgVal[1];
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
     		  			document.getElementById("max_eff_dt_"+temp+"_"+i+"_0").value=tempPlantChrgVal[1];
     			 	}
     			}
   			} 
  		} 
 	}
 	else if(dlng_chk_plant!=null)
 	{
   		for(var j=0;j<sepDlngPlant.length;j++)
   		{
   			if(dlng_chk_plant.value == sepDlngPlant[j])
     		{
   				document.getElementById("history_btn_"+sepDlngPlant[j]).setAttribute("title", sepstrChargehistdesc[j]);
   				
   				dlng_chk_plant.checked = true;
   				
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
     		  			document.getElementById("max_eff_dt_"+temp+"_0_"+k).value=tempPlantChrgVal[1];
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
 		  			document.getElementById("max_eff_dt_"+temp+"_0_0").value=tempPlantChrgVal[1];
 			 	}
     		}
   		} 
 	}
	
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
	
	doSubmitCustConfig('');
}


var newWindow;
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agreement_type = document.forms[0].agreement_type.value;
	var contract_type = document.forms[0].contract_type.value;
	var buy_sale = document.forms[0].buy_sale.value;
	
	/* if(contract_type=="0" || contract_type=="") //HM20250228:Condition removed as per vijay's feedback
	{
		alert("Select Contract Type!");
	}
	else */
	{
		var url="frm_ltcora_cont_list.jsp?counterparty_cd="+counterparty_cd+"&buy_sale="+buy_sale+"&agreement_type="+agreement_type+"&contract_type="+contract_type
		
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"LTCORA Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"LTCORA Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,cont_type)
{
	var opration = document.forms[0].opration.value;
	var agreement_type = document.forms[0].agreement_type.value;
	var buy_sale = document.forms[0].buy_sale.value;
	var u = document.forms[0].u.value;
	
	//alert(countpty_cd+"-"+agmt_no+"-"+agmt_rev_no+"-"+cont_no+"-"+cont_rev_no+"-"+cont_type);
	
	var url = "frm_ltcora_cont_sell_mst.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&contract_type="+cont_type+"&buy_sale="+buy_sale+"&agreement_type="+agreement_type+
			"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function openAgreementList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agreement_type = document.forms[0].agreement_type.value;
	var contract_type = document.forms[0].contract_type.value;
	var buy_sale = document.forms[0].buy_sale.value;
	
	if(counterparty_cd=="0" || counterparty_cd=="")
	{
		alert("Select Counterparty!");
	}
	else if(contract_type=="0" || contract_type=="")
	{
		alert("Select Contract Type!");
	}
	else
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open("frm_ltcora_cont_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&buy_sale="+buy_sale+"&agreement_type="+agreement_type+"&contract_type="+contract_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_ltcors_cont_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&buy_sale="+buy_sale+"&agreement_type="+agreement_type+"&contract_type="+contract_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}	
}

function setAgmtDetail(countpty_cd,buy_sale,agmt_type,agmt_no,agmt_rev_no,agmt_nm)
{
	var opration = document.forms[0].opration.value;
	var contract_type = document.forms[0].contract_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_ltcora_cont_sell_mst.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+
			"&buy_sale="+buy_sale+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
			"&agreement_type="+agmt_type+"&agmt_nm="+agmt_nm+
			"&contract_type="+contract_type+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doEnbStorageClause()
{
	var extend_storage = document.forms[0].extend_storage;
	var discount_days = document.forms[0].discount_days;
	var storage_tariff = document.forms[0].storage_tariff;
	var storage_tariff_unit = document.forms[0].storage_tariff_unit;
	
	var prev_discount_days = document.forms[0].prev_discount_days;
	var prev_storage_tariff = document.forms[0].prev_storage_tariff;
	var prev_storage_tariff_unit = document.forms[0].prev_storage_tariff_unit;
	
	if(extend_storage.value=="Y")
	{
		//discount_days.value=prev_discount_days.value;
		//storage_tariff.value=prev_storage_tariff.value;
		//storage_tariff_unit.value=prev_storage_tariff_unit.value;
		
		discount_days.style.pointerEvents = "auto";
		discount_days.style.background="";
		storage_tariff.readOnly=false;
		storage_tariff_unit.style.pointerEvents = "auto";
		storage_tariff_unit.style.background="";
	}
	else
	{
		//discount_days.value="N";
		//storage_tariff.value="";
		//storage_tariff_unit.value="";
		
		discount_days.style.pointerEvents = "none";
		discount_days.style.background="#ededed";
		storage_tariff.readOnly=true;
		storage_tariff_unit.style.pointerEvents = "none";
		storage_tariff_unit.style.background="#ededed";
	}
	
	
}
var securityWindow;
function securityPreReceipt(cont_type,agmt,agmt_rev,cont,cont_rev,cont_status_flg)
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
		var url="";
		if(cont_status_flg=='X'||cont_status_flg=='C'||cont_status_flg=='T')
		{
			url = "../credit_risk/frm_pre_receipt_security_fcc.jsp?counterparty_cd="+counterparty_cd+"&cont_no="+cont+"&cont_rev_no="+cont_rev+
				"&agmt_no="+agmt+"&agmt_rev_no="+agmt_rev+"&contract_type="+cont_type+"&sec_category="+sec_category+"&start_dt="+temp_start_dt+"&end_dt="+temp_end_dt+"&u="+u;
		}
		else
		{
			url = "../credit_risk/frm_pre_receipt_security.jsp?counterparty_cd="+counterparty_cd+"&cont_no="+cont+"&cont_rev_no="+cont_rev+
				"&agmt_no="+agmt+"&agmt_rev_no="+agmt_rev+"&contract_type="+cont_type+"&sec_category="+sec_category+"&start_dt="+temp_start_dt+"&end_dt="+temp_end_dt+"&u="+u;
		}
		
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
			alert("Select CN Detail!")
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
	var buy_sale = document.forms[0].buy_sale.value;
	var agreement_type = document.forms[0].agreement_type.value;
	var ltcora_tariff_unit = document.forms[0].ltcora_tariff_unit.value;
	var storage_tariff_unit = document.forms[0].storage_tariff_unit.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_ltcora_cont_billing_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&buy_sale="+buy_sale+"&cont_no="+cont_no+
			"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+"&agreement_type="+agreement_type+"&rate_unit="+ltcora_tariff_unit+"&storage_tariff_unit="+storage_tariff_unit+
			"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Confirmation Notice Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Confirmation Notice Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function fcc(fcc_flg)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	
	var signing_dt = document.forms[0].signing_dt.value;
	var signing_time = document.forms[0].signing_time.value;
	var ent_dt = document.forms[0].ent_dt.value;
	var ent_time = document.forms[0].ent_time.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var dda_dt = document.forms[0].dda_dt.value;
	var cont_status_flg = document.forms[0].cont_status_flg.value;
	
	var agreement_type = document.forms[0].agreement_type;
	var agreement_base = document.forms[0].agreement_base;
	
	var chk_plant = document.forms[0].chk_plant;
	var chk_oth_plant = document.forms[0].chk_oth_plant;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var agmt_no = document.forms[0].agmt_no.value;
	
	var cont_status_flg = document.forms[0].cont_status_flg;
	
	var msg="";
	var flag=true;
	
	if(flag)
	{
		var a;
		
		if(fcc_flg=="Y")
		{
			a = confirm("Do you want to Approve LTCORA (Buy) CN/Period?");
		}
		else
		{
			a = confirm("Do you want to Disapprove LTCORA (Buy) CN/Period?");
		}
		if(a)
		{
			alert("Submit to update Deal Checks & Control!!");
			document.forms[0].fcc_flg.value=fcc_flg;
			cont_status_flg.value="Y";
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

function openLiabilityClause(cont_status_flg)
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
	var agreement_type = document.forms[0].agreement_type.value;
	var buy_sale = document.forms[0].buy_sale.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_ltcora_cont_liability_clause.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+"&contract_type="+contract_type+
			"&end_dt="+end_dt+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&buy_sale="+buy_sale;
	if(cont_status_flg!='X' && cont_status_flg!='C' && cont_status_flg!='T')
	{
		url+="&u="+u;
	}		
	
	
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
				alert("Contract Contract Date ("+signing_dt+") should be Greater than or equals to Agreement Contract Date ("+agmt_signing_dt+") !");
				document.forms[0].signing_dt.value="";
				
				return false;
			}
			
			if(temp_agmt_end_dt < tmp_sig_dt)
			{
				alert("Contract Contract Date ("+signing_dt+") should be Less than Agreement End date ("+agmt_end_dt+") !");
				document.forms[0].signing_dt.value="";
				
				return false;
			}
		}
		
		if(dda_dt!="")
		{
			if(tmp_dda_dt < tmp_agmt_sig_dt)
			{
				alert("Contract DDA date ("+dda_dt+") should be Greater than or equals to Agreement Contract Date ("+agmt_signing_dt+") !");
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

function checkMCSOC(mcos)
{
	if(mcos.value != "")
	{
		var floatMCOS = parseFloat(mcos.value);
		
		if(floatMCOS<100)
		{
			alert("MCSOC(%) should be grater or equal to 100!!");
			mcos.value="100";
		}
	}
	else
	{
		mcos.value="100";
	}
}

function openVariableTariff(cont_status_flg)
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
	var agreement_type = document.forms[0].agreement_type.value;
	var buy_sale = document.forms[0].buy_sale.value;
	var storage_tariff = document.forms[0].storage_tariff.value;
	var storage_tariff_unit = document.forms[0].storage_tariff_unit.value;
	var extend_storage = document.forms[0].extend_storage.value;

	var u = document.forms[0].u.value;
	
	var url = "frm_ltcora_variable_tariff_config.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+"&storage_tariff="+storage_tariff+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+"&contract_type="+contract_type+"&storage_tariff_unit="+storage_tariff_unit+
			"&end_dt="+end_dt+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&buy_sale="+buy_sale;
	if(cont_status_flg!='X' && cont_status_flg!='C' && cont_status_flg!='T')
	{
		url+="&u="+u;
	}
	
	if(trim(extend_storage) == "" || extend_storage == "N")
	{
		alert("Extended Storage Must Be Selected 'Yes' for 'Variable Tariff Config' !");
		return false;
	}
	if(trim(storage_tariff) == "")
	{
		alert("Enter Storage Teriff Value!");
		return false;
	}
	if(trim(storage_tariff_unit) == "" || storage_tariff_unit == "0")
	{
		alert("Select Storage Teriff Unit!");
		return false;
	}
	else
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"LTCORA Variable Teriff","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"LTCORA Variable Teriff","top=10,left=10,width=1100,height=600,scrollbars=1");
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

function cancelContract()
{
	var cancel_note = document.forms[0].cancel_note.value;
	if(cancel_note=='')
	{
		alert("Enter the cancel note!");
	}
	else
	{
		var a=confirm("Once cancelled the contract can not be Re-Opened!\n\nAre you sure, you want to cancel the contract?");
		if(a)
		{
			document.forms[0].change_request.value="CANCEL";
			document.forms[0].cont_status_flg.value="X";
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
}

function doClosureRequest(clsr_type)
{
	var conf_msg='';
	if(clsr_type == 'C')
	{
		conf_msg = "New Contract Revision will be Submitted Along With Contract Closure Request!\n\nDo You Want To Revise The Contract Details, With Contract Closure Request?";
	}
	else if(clsr_type=='T')
	{
		conf_msg = "New Contract Revision will be Submitted Along With Contract Tremination Request!\n\nDo You Want To Revise The Contract Details, With Contract Termination Request?";
	}
	var a = confirm(conf_msg);
	if(a)
	{
		if(clsr_type=='C')
		{
			document.forms[0].change_request.value="CLOSE";
		}
		else if(clsr_type=='T')
		{
			document.forms[0].change_request.value="TERMINATE";
		}
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].submit();
	}
}

function doReopenRequest()
{
	var a = confirm("Are you sure you want to re-open contract? ");
	if(a)
	{
		document.forms[0].change_request.value="REOPEN";
		document.getElementById("loading").style.visibility = "visible";
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

function enableDlngServices(tlu_flag)
{
	var dlngSvcDiv = document.getElementById('dlngSvcDiv');

	if(tlu_flag == "Y")
	{
		dlngSvcDiv.style.display ="block";
	}
	else
	{
		dlngSvcDiv.style.display ="none";
	}
}

function doSubmitCustConfig(submit_flag)
{
	var chk_plant = document.forms[0].dlng_chk_plant;
	var chk_plant_abbr = document.forms[0].dlng_chk_plant_abbr;
	
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
		 		var temp = document.getElementById("charge_abbr_0").value;
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

function setTransChrg(obj,index,cust_pant_nom)
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
		if(cust_pant_nom != 'Y')
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
		else
		{
			obj.checked=true;
			alert("Can't un-checked, nomination is done for this customer plant!");
		}
	}
}
</script>
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
String agmt_nm = request.getParameter("agmt_nm")==null?"":request.getParameter("agmt_nm");

String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_nm = request.getParameter("cont_nm")==null?"":request.getParameter("cont_nm");


ltcora.setCallFlag("LTCORA_CONTRACT_MST");
ltcora.setComp_cd(owner_cd);
ltcora.setCounterparty_cd(counterparty_cd);
ltcora.setAgreement_type(agreement_type);
ltcora.setAgmt_no(agmt_no);
ltcora.setAgmt_rev_no(agmt_rev_no);
ltcora.setCont_no(cont_no);
ltcora.setCont_rev_no(cont_rev_no);
ltcora.setContract_type(contract_type);
ltcora.setBuy_sale(buy_sale);
ltcora.setOpration(opration);
ltcora.init();


Vector VCOUNTERPARTY_CD = ltcora.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = ltcora.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = ltcora.getVCOUNTERPARTY_ABBR();

Vector VPLANT_NM = ltcora.getVPLANT_NM();
Vector VPLANT_ABBR = ltcora.getVPLANT_ABBR();
Vector VPLANT_SEQ_NO = ltcora.getVPLANT_SEQ_NO();
Vector VBU_CD = ltcora.getVBU_CD();
Vector VBU_PLANT_NM = ltcora.getVBU_PLANT_NM();
Vector VBU_PLANT_ABBR = ltcora.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = ltcora.getVBU_PLANT_SEQ_NO();

Vector VTRANS_CD = ltcora.getVTRANS_CD();
Vector VTRANS_PLANT_NM = ltcora.getVTRANS_PLANT_NM();
Vector VTRANS_PLANT_ABBR = ltcora.getVTRANS_PLANT_ABBR();
Vector VTRANS_PLANT_SEQ_NO = ltcora.getVTRANS_PLANT_SEQ_NO();

Vector VTEMP_TRANS_CD = ltcora.getVTEMP_TRANS_CD();
Vector VTEMP_TRANS_ABBR = ltcora.getVTEMP_TRANS_ABBR();

Vector VSEL_PLANT_SEQ_NO = ltcora.getVSEL_PLANT_SEQ_NO();
Vector VSEL_BU_CD = ltcora.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = ltcora.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_TRAD_CD = ltcora.getVSEL_TRAD_CD();
Vector VSEL_PLANT_ABBR = ltcora.getVSEL_PLANT_ABBR();
Vector VSEL_BU_PLANT_ABBR = ltcora.getVSEL_BU_PLANT_ABBR();
Vector VSEL_TRANS_CD = ltcora.getVSEL_TRANS_CD();
Vector VSEL_TRANS_PLANT_SEQ_NO = ltcora.getVSEL_TRANS_PLANT_SEQ_NO();

Vector VTRUCK_TRANS_ABBR = ltcora.getVTRUCK_TRANS_ABBR();
Vector VTRUCK_TRANS_CD = ltcora.getVTRUCK_TRANS_CD();
Vector VTRUCK_TRANS_NM = ltcora.getVTRUCK_TRANS_NM();

Vector VFILL_STATION_ABBR = ltcora.getVFILL_STATION_ABBR();
Vector VFILL_STATION_CD = ltcora.getVFILL_STATION_CD();

Vector VNOM_SEL_TRUCK_TRANS_CHK = ltcora.getVNOM_SEL_TRUCK_TRANS_CHK();
Vector VNOM_SEL_CUST_CHK = ltcora.getVNOM_SEL_CUST_CHK();
Vector VNOM_SEL_FILL_CHK = ltcora.getVNOM_SEL_FILL_CHK();

Vector VDLNG_PLANT_NM = ltcora.getVDLNG_PLANT_NM();
Vector VDLNG_PLANT_ABBR = ltcora.getVDLNG_PLANT_ABBR();
Vector VDLNG_PLANT_SEQ_NO = ltcora.getVDLNG_PLANT_SEQ_NO();
Vector VDLNG_NOM_SEL_CUST_CHK = ltcora.getVDLNG_NOM_SEL_CUST_CHK();

Vector VSEL_TRUCK_TRANS_CD = ltcora.getVSEL_TRUCK_TRANS_CD();
Vector VSEL_TRUCK_TRANS_ABBR = ltcora.getVSEL_TRUCK_TRANS_ABBR();
Vector VSEL_FILL_STATION_ABBR = ltcora.getVSEL_FILL_STATION_ABBR();
Vector VSEL_FILL_STATION_CD = ltcora.getVSEL_FILL_STATION_CD();
Vector VSEL_CHARGE_VALUE = ltcora.getVSEL_CHARGE_VALUE();
Vector VSEL_CHARGE_DESC = ltcora.getVSEL_CHARGE_DESC();
Vector VSEL_DLNG_PLANT_SEQ_NO = ltcora.getVSEL_DLNG_PLANT_SEQ_NO();

Vector VCHARGE_ABBR = ltcora.getVCHARGE_ABBR();
Vector VCHARGE_NAME = ltcora.getVCHARGE_NAME();

String min_counterparty_eff_dt = ltcora.getMin_counterparty_eff_dt();

String cont_status = ltcora.getCont_status();
String cont_status_flg = ltcora.getCont_status_flg();

cont_rev_no=ltcora.getCont_rev_no();
String cont_name = ltcora.getCont_name();
String rev_dt = ltcora.getRev_dt();
String dda_dt = ltcora.getDda_dt();
String dda_time = ltcora.getDda_time();
String signing_dt = ltcora.getSigning_dt();
String signing_time = ltcora.getSigning_time();
String ent_dt = ltcora.getEnt_dt();
String ent_time = ltcora.getEnt_time();
String start_dt = ltcora.getStart_dt();
String end_dt = ltcora.getEnd_dt();
String cont_ref_no = ltcora.getCont_ref_no();
String contpty_abbr = ltcora.getContpty_abbr();
String status_nm = ltcora.getStatus_nm();
String agmt_base = ltcora.getAgmt_base();
String agmt_type = ltcora.getAgmt_type();

String no_of_security_dtl=ltcora.getNo_of_security_dtl();

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
String liability_lq_dmg = ltcora.getLiability_lq_dmg();//JD
String liability_take_pay = ltcora.getLiability_take_pay();//JD
String liability_makeup = ltcora.getLiability_makeup();//JD
String bill_flag = ltcora.getBill_flag();
String billing_clause = ltcora.getBilling_clause();
String no_of_billing_dtl=ltcora.getNo_of_billing_dtl();
String termination_flag = ltcora.getTerminaton_flag();
String termination_clause = ltcora.getTermination_clause();
String termination_planned = ltcora.getTermination_planned();
String termination_forced = ltcora.getTermination_forced();
String agmt_start_dt = ltcora.getAgmt_start_dt();
String agmt_end_dt = ltcora.getAgmt_end_dt();
String agmt_signing_dt = ltcora.getAgmt_signing_dt();

String agmt_agmt_base=ltcora.getAgmt_agmt_base();

String fcc_flg =ltcora.getFcc_flg();
String closure_request_flag = ""; //ltcora.getClosure_request_flag();


String no_of_cargo=ltcora.getNo_of_cargo();
String ltcora_tariff=ltcora.getLtcora_tariff();
String ltcora_tariff_unit=ltcora.getLtcora_tariff_unit();
String sug=ltcora.getSug();
String tariff_discount=ltcora.getTariff_discount();
String vol_discount=ltcora.getVol_discount();
String vol_discount_unit=ltcora.getVol_discount_unit();
String tlu_flag=ltcora.getTlu_flag();
String adv_adjust=ltcora.getAdv_adjust();
String adv_adjust_type="";
String adv_adjust_amount="";
String adv_adjust_unit="";
String extend_storage=ltcora.getExtend_storage();
String discount_days=ltcora.getDiscount_days();
String storage_tariff_mode=ltcora.getStorage_tariff_mode(); //to be removed
String storage_tariff=ltcora.getStorage_tariff();
String storage_tariff_unit=ltcora.getStorage_tariff_unit();

String is_cargo_alloc = ltcora.getIs_cargo_alloc();
String display_map_id = ltcora.getDisplay_map_id();
String close_note = ltcora.getClosure_note();
String clsr_flag = ltcora.getClosure_flag();
String is_expired = ltcora.getIs_expired();
/*
if(cont_status_flg.equals(""))
{
	cont_status_flg="F";
	cont_status="New";
}
else if(cont_status_flg.equals("Y"))
{
	cont_status="Approved";
}
else if(cont_status_flg.equals("N"))
{
	cont_status="Disapproved";
}
else if(cont_status_flg.equals("F"))
{
	cont_status="New";
}
else if(cont_status_flg.equals("P"))
{
	cont_status="Pending Approval";
}*/

if(dda_time.equals("")){
	dda_time="00:00";
}
if(signing_time.equals("")){
	signing_time="00:00";
}
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

if(tariff_discount.equals("")){
	tariff_discount="N";
}
if(vol_discount.equals("")){
	vol_discount="N";
}
if(extend_storage.equals("")){
	extend_storage="N";
}
if(discount_days.equals("")){
	discount_days="N";
}
if(tlu_flag.equals(""))
{
	tlu_flag="N";
}

String strPlant="";
String strBuPlant="";
String strTransCd="";
String strTrans="";

String strFillStaCd="";
String strFillStaAbbr="";
String strTruckTransCd="";
String strTruckTrans="";
String strPlantChrg="";
String strChargehistdesc="";
String strDlngPlant="";

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
for(int i=0;i<VSEL_TRUCK_TRANS_CD.size();i++)
{
	strTruckTransCd = strTruckTransCd + VSEL_TRUCK_TRANS_CD.elementAt(i)+"@";
	strTruckTrans = strTruckTrans + VSEL_TRUCK_TRANS_ABBR.elementAt(i)+"@";
}
for(int i=0;i<VSEL_DLNG_PLANT_SEQ_NO.size();i++)
{
	strDlngPlant = strDlngPlant + VSEL_DLNG_PLANT_SEQ_NO.elementAt(i)+"@";
	strPlantChrg = strPlantChrg + VSEL_CHARGE_VALUE.elementAt(i)+"@";
	strChargehistdesc = strChargehistdesc +VSEL_CHARGE_DESC.elementAt(i)+"@";
}
for(int i=0;i<VSEL_FILL_STATION_CD.size();i++)
{
	strFillStaCd = strFillStaCd + VSEL_FILL_STATION_CD.elementAt(i)+"@";
	strFillStaAbbr = strFillStaAbbr + VSEL_FILL_STATION_ABBR.elementAt(i)+"@";
}
// NOTE :: To be removed latter when advance flow is clear
if(adv_adjust.equals(""))
{
	adv_adjust="N";
}

%>
<body onload="setValues('<%=strTransCd%>','<%=strTrans%>','<%=strPlant%>','<%=strBuPlant%>','<%=strFillStaCd%>','<%=strFillStaAbbr%>','<%=strTruckTransCd%>','<%=strTruckTrans%>','<%=strPlantChrg%>','<%=strDlngPlant%>','<%=strChargehistdesc %>');
liabilityShows();billingShows();messurmentShows();off_spec_gasShows();terminatorShows();doEnbStorageClause();enableDlngServices('<%=tlu_flag%>');">
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
					    	LTCORA (Sell) CN/Period
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
							<%if(opration.equals("INSERT")){ %>
								<div class="col">
				    		<%}else{ %>
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    		<%} %>
				    				<select class="form-select form-select-sm" name="contract_type" onchange="refresh('<%=opration%>');"> <%--HM20250308 :Removed as per Vijay's UAT Feedback <%if(opration.equals("MODIFY") && (!cont_no.equals("0") && !cont_no.equals(""))){ %>style="pointer-events: none;"<%} %> --%>
				    					<option value="">--Select--</option>
				    					<option value="O">CN</option>
				    					<option value="Q">Period</option>
				    				</select>
				    				<script>document.forms[0].contract_type.value="<%=contract_type%>"</script>
				      			</div>
				      			<%if(opration.equals("INSERT")){ %>
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
				    			<div class="col-sm-12 col-xs-12 col-md-12">
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
				    				<%if(cont_status_flg.equals("X") || cont_status_flg.equals("T") || cont_status_flg.equals("C")){%>
				    					<i name="disp_cont_status_remark" id="disp_cont_status_remark" class="fa fa-info-circle fa-lg" aria-hidden="true" style="pointer-events: auto; color:#800040" title="<%=close_note%>"></i>
				    				<%} %>
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
			      						onblur="validateDate(this);" onchange="validateDate(this);checkSigningDdaDate();" autocomplete="off">
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
						<%-- <div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>DDA Note</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="dda_note" value="<%//=dda_note%>" maxLength="100">
				    			</div>
				  			</div>
						</div> --%>
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
			      						onchange="validateDate(this);checkSigningStartDt(document.forms[0].start_dt,this.value);checkSigningDdaDate();" autocomplete="off">
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
							<div class="form-group row">
				    			<label class="form-label" title="Agreement AGMT BASE : <%=agmt_agmt_base%>"><b>Agreement Base<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="radio" name="agreement_base" value="X" <%if(agmt_base.equals("X")){%>checked<%}%> onclick="showHide('X');">&nbsp;Ex-Terminal&nbsp;&nbsp;
				    				<script>document.forms[0].agreement_base.style.pointerEvents = "none";</script>
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
			      						onchange="validateDate(this);checkEffDtWithContStDt(this,'<%=min_counterparty_eff_dt%>');checkStartEndDate(this,document.forms[0].end_dt,'F');checkSigningStartDt(this,document.forms[0].signing_dt.value);checkAgmtStartdate();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<input type="hidden" name="temp_start_dt" value="<%=start_dt%>">
				    			<input type="hidden" name="agmt_start_dt" value="<%=agmt_start_dt%>">
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
			      						onchange="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');checkAgmtEnddate();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<input type="hidden" name="temp_end_dt" value="<%=end_dt%>">
				    			<input type="hidden" name="agmt_end_dt" value="<%=agmt_end_dt%>">
				  			</div>
						</div>
					</div>
					<div class="row m-b-5" <%if(!opration.equals("MODIFY")){ %>style="display:none;"<%} %>>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Security<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col">
									<select class="form-select form-select-sm" name="sec_category" id="sec_category">
										<option value="R">Incoming</option>
										<option value="I">Outgoing</option>
									</select>
								</div>
				    			<div class="col">
				    				<input type="button" name="security_btn" class="btn btn-sm config_btn" value="Security Config" onclick="securityPreReceipt('<%=contract_type%>','<%=agmt_no%>','<%=agmt_rev_no%>','<%=cont_no%>','<%=cont_rev_no%>','<%=cont_status_flg%>');">
				  					<input type="hidden" name="securityFlag" value="N">
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
				    			<label class="form-label"><b>Customer(<%=contpty_abbr%>)-Plant/s<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<%if(VPLANT_SEQ_NO.size() > 0) {%>
					    				<%for(int i=0; i<VPLANT_SEQ_NO.size(); i++){ %>
					    					<input type="checkbox" class="form-check-input" name="chk_plant" value="<%=VPLANT_SEQ_NO.elementAt(i)%>">&nbsp;<%=VPLANT_ABBR.elementAt(i)%>
					    					<input type="hidden" name="tmp_chk_plant" value="<%=strPlant%>">
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
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> DLNG Service Terms</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b>DLNG Service</b></label>
				  				</div>				    			
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				    				<select class="form-select form-select-sm" name="tlu_flag" onchange="enableDlngServices(this.value);">
				    					<option value="N">No</option>
				    					<option value="Y">Yes</option>
				    				</select>
				    				<script>document.forms[0].tlu_flag.value="<%=tlu_flag%>"</script>				    								    								    				
				      			</div>
				  			</div>
						</div>
					</div>
					<div id="dlngSvcDiv" style="display:none">
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
				    					<input type="button" class="btn btn-sm config_btn" value="DLNG Charge Config"  
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
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Truck Transporter <span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<%if(VTRUCK_TRANS_CD.size() > 0) {%>
						    				<%for(int i=0; i<VTRUCK_TRANS_CD.size(); i++){ %>
						    					<input type="checkbox" class="form-check-input" name="chk_truck_trans" value="<%=VTRUCK_TRANS_CD.elementAt(i)%>" title="<%=VTRUCK_TRANS_NM.elementAt(i)%>">&nbsp;<%=VTRUCK_TRANS_ABBR.elementAt(i)%>&nbsp;&nbsp;
						    					<input type="hidden" name="truck_trans_nom" value="<%=VNOM_SEL_TRUCK_TRANS_CHK.elementAt(i)%>" >
						    				<%}%>
					    				<%}else{ %>
					    					<%= utilmsg.warningMessage("Please configure Truck Transporter!")%>
					    				<%} %>
					      			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Filling Station<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<%if(VFILL_STATION_CD.size() > 0) {%>
						    				<%for(int i=0; i<VFILL_STATION_CD.size(); i++){ %>
						    					<input type="checkbox" class="form-check-input" name="chk_fill_station" value="<%=VFILL_STATION_CD.elementAt(i)%>">&nbsp;<%=VFILL_STATION_ABBR.elementAt(i)%>&nbsp;&nbsp;
						    					<input type="hidden" name="fill_nom" value="<%=VNOM_SEL_FILL_CHK.elementAt(i)%>">
						    				<%}%>
					    				<%}else{ %>
					    					<%= utilmsg.warningMessage("Please configure Filling Station!")%>
					    				<%} %>
					      			</div>
					  			</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Commercial Terms</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>No. of Cargo<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="no_of_cargo" onblur="negNumber(this);checkNumber1(this,2,0);" value="<%=no_of_cargo%>" maxLength="2">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>LTCORA Tariff<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="ltcora_tariff" value="<%=ltcora_tariff%>" 
				      				<%if(ltcora_tariff_unit.equals("1")){ %>onblur="checkNumber1(this,6,2);negNumber(this);"<%}else{ %>onblur="checkNumber1(this,6,4);negNumber(this);"<%}%>>
		      					</div>				    				
				    			<div class="col">
				    				<select class="form-select form-select-sm" name="ltcora_tariff_unit" onchange="checkRateFormate(this,'lt_terif');">
				    					<option value="">--Select--</option>
				    					<option value="2">USD/MMBTU</option>
				    					<option value="1">INR/MMBTU</option>
				    				</select>
				    				<script>document.forms[0].ltcora_tariff_unit.value="<%=ltcora_tariff_unit%>"</script>
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>SUG%<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				    				<div class="input-group input-group-sm" >
				      					<input type="text" class="form-control form-control-sm" name="sug" onblur="appPercentage(this);" value="<%=sug%>" maxLength="25">
				    					<span class="input-group-text">%</span>
				    				</div>
				    			</div>
				  			</div>
						</div>
					</div>		
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b>Tariff Discount</b></label>
				  				</div>				    			
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				    				<select class="form-select form-select-sm" name="tariff_discount"> 
				    					<option value="N">No</option>
				    					<option value="Y">Yes</option>
				    				</select>
				    				<script>document.forms[0].tariff_discount.value="<%=tariff_discount%>"</script>				    				
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b>Volume Discount</b></label>
				  				</div>				    			
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				    				<select class="form-select form-select-sm" name="vol_discount">
				    					<option value="N">No</option>
				    					<option value="Y">Yes</option>
				    				</select>
				    				<script>document.forms[0].vol_discount.value="<%=vol_discount%>"</script>				    								    				
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b>Advance Adjustment</b></label>
				  				</div>				    			
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				    				<select class="form-select form-select-sm" name="adv_adjust">
				    					<option value="N">No</option>
				    					<option value="Y">Yes</option>
				    				</select>
				    				<script>document.forms[0].adv_adjust.value="<%=adv_adjust%>"</script>				    								    								    				
				      			</div>
				  			</div>
						</div>
						<%-- <div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b>Advance Adjustment Type</b></label>
				  				</div>				    			
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				    				<select class="form-select form-select-sm" name="adv_adjust_type" value="<%=adv_adjust_type%>" maxLength="25">
				    					<option value="A">Amount Based</option>
				    				</select>
				      			</div>
				  			</div>
						</div> --%>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Storage Clauses</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label"><b>Extended Storage Applicable</b></label>
				  				</div>				    			
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="extend_storage" onchange="doEnbStorageClause();">
				    					<option value="Y">Yes</option>
				    					<option value="N">No</option>
				    				</select>
				    				<script>document.forms[0].extend_storage.value="<%=extend_storage%>"</script>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label"><b>Day's Discount</b></label>
				  				</div>				    			
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="discount_days">
				    					<option value="Y">Yes</option>
				    					<option value="N">No</option>
				    				</select>
				    				<input type="hidden" name="prev_discount_days" value="<%=discount_days%>">
				    				<script>document.forms[0].discount_days.value="<%=discount_days%>"</script>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label"><b>Storage Tariff</b></label>
				  				</div>				    			
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="storage_tariff" value="<%=storage_tariff%>" 
				      				<%if(storage_tariff_unit.equals("1")){ %>onblur="checkNumber1(this,6,2);"<%}else{ %>onblur="checkNumber1(this,6,4);"<%}%>>
		      						<input type="hidden" name="prev_storage_tariff" value="<%=storage_tariff%>">
		      					</div>				    				
				    			<div class="col">
				    				<select class="form-select form-select-sm" name="storage_tariff_unit" onchange="checkRateFormate(this,'stor_terif');">
				    					<option value="">--Select--</option>
				    					<option value="2">USD/MMBTU</option>
				    					<option value="1">INR/MMBTU</option>
				    				</select>
				    				<input type="hidden" name="prev_storage_tariff_unit" value="<%=storage_tariff_unit%>">
				    				<script>document.forms[0].storage_tariff_unit.value="<%=storage_tariff_unit%>"</script>
				      			</div>
				      			<%if(opration.equals("MODIFY")){ %>
				    			<div class="col">
				    				<input type="button" class="btn btn-sm config_btn" value="Variable Tariff Config" onclick="openVariableTariff('<%=cont_status_flg%>')">
				  				</div>
				  				<%} %>
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
								<div class="col-auto">
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="buyer_nom" value="Y" <%if(buy_nom_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Buyer Nomination</b></label>
				  				</div>
				    			<div class="col">
				    				<input type="text" class="form-control form-control-sm" name="buy_clause_no" value="<%=buy_nom_clause%>" placeholder="Clause#">
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
				    				<input type="text" class="form-control form-control-sm" name="sell_clause_no" value="<%=sell_nom_clause%>" placeholder="Clause#">
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
				    				<input type="text" class="form-control form-control-sm" name="day_clause_no" value="<%=day_def_clause%>" placeholder="Clause#">
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
				    				<input type="text" class="form-control form-control-sm" name="mdcq_clause_no" value="<%=mdcq_clause%>" placeholder="Clause#">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				    				<div class="input-group input-group-sm" >
				    					<input type="text" class="form-control form-control-sm" name="mdcq_percent" value="<%=mdcq_percentage%>" onkeyup="negNumber(this);" maxlength="3" onblur="checkMCSOC(this);">
				      					<span class="input-group-text">%</span>				      					
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
				    				<input type="text" class="form-control form-control-sm" name="measure_clause_no" value="<%=meas_clause%>" placeholder="Clause#">
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
				    			<div id="off_spec_clause_div" class="col">
				    				<input type="text" class="form-control form-control-sm" name="spec_clause_no" value="<%=spec_cause%>" placeholder="Clause#">
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
				    				<input type="button" class="btn btn-sm config_btn" value="Configure Liability" <%if(opration.equals("MODIFY")){ %>onclick="openLiabilityClause('<%=cont_status_flg%>');"<%} %>>&nbsp;
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
					    			<input type="button" class="btn btn-sm config_btn" value="Configure Billing" <%if(opration.equals("MODIFY")){ %>onclick="openBillingDtl();"<%} %>>
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
									<%if(approve_access.equals("Y") && !cont_status_flg.equals("X")&& !cont_status_flg.equals("C")&& !cont_status_flg.equals("T")&&!clsr_flag.equals("Y")&&!clsr_flag.equals("R")){ %>
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
						<%if(write_access.equals("Y") && !closure_request_flag.equals("Y") && !cont_status_flg.equals("X")&& !cont_status_flg.equals("T")&& !cont_status_flg.equals("C") && !clsr_flag.equals("Y")&& !clsr_flag.equals("R")){ %>
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
					    	LTCORA (Sell)CN/Period - Change Request(s)
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
				    			<div class="col-auto">
				    				<input type="button" class="btn btn-sm request_btn" value="Modify Contract Duration" style="background:#e6e6e6;color:black;" >
				    			</div>
				    			<div class="col-auto" title="<%if(!cont_status_flg.equals("X")&& !cont_status_flg.equals("T")&& !cont_status_flg.equals("C")&& !clsr_flag.equals("Y")&& !clsr_flag.equals("R")){if(!is_cargo_alloc.equals("Y")){%>Cargo is not configured, cancle it!<%}else if(!is_expired.equals("Y")){%>Contract is Active, Terminate it!<%}}%>">
				    				<input type="button" class="btn btn-sm request_btn" value="Closure Request" <%if(is_cargo_alloc.equals("Y")&&is_expired.equals("Y")&&opration.equals("MODIFY")&&!cont_no.equals("")&& !cont_status_flg.equals("X")&& !cont_status_flg.equals("C")&& !cont_status_flg.equals("T")&& !clsr_flag.equals("Y")&& !clsr_flag.equals("R")){%>onclick="doClosureRequest('C');"<%}else{%>style="background:#e6e6e6;color:black;" disabled<%} %>>
				    			</div>
				    			<div class="col-auto" title="<%if(!cont_status_flg.equals("X")&& !cont_status_flg.equals("T")&& !cont_status_flg.equals("C")&& !clsr_flag.equals("Y")&& !clsr_flag.equals("R")){if(!is_cargo_alloc.equals("Y")){%>Cargo is not configured, cancle it!<%}else if(is_expired.equals("Y")){%>Contract is Expired, Close it!<%}else if(!termination_forced.equals("Y")){%>Configure Termination/Suspension (Force Majeure)!<%}}%>">
				    				<input type="button" class="btn btn-sm request_btn" value="Terminate Request" <%if(is_cargo_alloc.equals("Y")&&!is_expired.equals("Y")&&opration.equals("MODIFY")&&!cont_no.equals("")&& !cont_status_flg.equals("X")&& !cont_status_flg.equals("C")&& !cont_status_flg.equals("T")&&termination_forced.equals("Y")){%>onclick="doClosureRequest('T');"<%}else{%>style="background:#e6e6e6;color:black;" disabled<%} %>>
				    			</div>
				    			<div class="col-auto" title="<% if(!closure_request_flag.equals("O")&&!cont_status_flg.equals("X")&& !cont_status_flg.equals("T")&& !cont_status_flg.equals("C")){%>Activates when contract is closed or terminated!<%}else if(closure_request_flag.equals("O")){%>Contract Reopen Request generated!<%}%>">
				    				<input type="button" class="btn btn-sm request_btn" value="ReOpen Contract" <%if((cont_status_flg.equals("C")||cont_status_flg.equals("T"))&&!clsr_flag.equals("O")){%>onclick = "doReopenRequest();" <%}else{%>style="background:#e6e6e6;color:black;" disabled<%} %>>
				    			</div>
				    			<div class="col-auto" title="<%if(is_cargo_alloc.equals("Y")){%>Cargo is allocated for this contract, close|terminate it!<%}%>">
				    				<input type="button" class="btn btn-sm request_btn" value="Cancel Contract" <%if(!is_cargo_alloc.equals("Y")&&opration.equals("MODIFY")&&!cont_no.equals("")&& !cont_status_flg.equals("X")&& !cont_status_flg.equals("C")&& !cont_status_flg.equals("T")){%>data-bs-toggle="modal" data-bs-target="#CancelModal" <%}else{%>style="background:#e6e6e6;color:black;" disabled<%} %>>
				    			</div>
				  			</div>
						</div>
					</div>
					<br>
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
					<%} %>					
					<%if(closure_request_flag.equals("Y")){ %>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<%=utilmsg.infoMessage("<b>Contract Termination Request Generated!</b>")%>
							</div>
						</div>	
					<%} %>
					<%if(cont_status_flg.equals("X")) {%>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<%=utilmsg.infoMessage("<b>This Contract has been Cancled!</b>")%>
							</div>
						</div>	
					<%}else if(cont_status_flg.equals("C")){%>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<%=utilmsg.infoMessage("<b>This Contract has been Closed!</b>")%>
							</div>
						</div>	
					<%}else if(cont_status_flg.equals("T")){%>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<%=utilmsg.infoMessage("<b>This Contract has been Terminated!</b>")%>
							</div>
						</div>	
					<%} %>
					
					<%if(clsr_flag.equals("Y")){%>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<%=utilmsg.infoMessage("<b>Closure Request has been generated!</b>")%>
							</div>
						</div>	
					<%}else if(clsr_flag.equals("R")){%>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<%=utilmsg.infoMessage("<b>Termination Request has been generated!</b>")%>
							</div>
						</div>	
					<%}else if(clsr_flag.equals("O")){ %>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<%=utilmsg.infoMessage("<b>ReOpen Request has been generated!</b>")%>
							</div>
						</div>	
					<%} %>
				</div>
			</div>
		</div>
	</div>	
</div>

<!-- Cancel Modal -->
<div class="modal fade" id="CancelModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
		<div class="modal-content">
			<div class="modal-header cdheader">
        		<div class="topheader">
					Contract [<%=display_map_id%>] Cancellation
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" onclick="(function() {document.forms[0].cancel_note.value='Cancel due to incorrect entry.'})();">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Cancel Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">
				    			<div class="col">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date" name="cancel_dt" value="<%=sysdate %>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" readOnly>
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      					</div>
				  			</div> 
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Cancel Note:<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">
				    			<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="cancel_note" value="Cancel due to incorrect entry." maxLength="40">
				    			</div>
				    		</div>
				    	</div>
					</div>
      			</div>
      		</div>
			<div class="modal-footer cdfooter">
	       		<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Refresh" onclick="(function() {document.forms[0].cancel_note.value=''})();">
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="cancelContract();">
					</div>
				</div>
	      	</div>
		</div>
	</div>
</div>

<!-- /Cancel Modal -->

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
					<%if(cont_status_flg.equals("X")||cont_status_flg.equals("C")||cont_status_flg.equals("T")){%>
						<input type="button" class="btn btn-warning com-btn" value="Proceed" onclick="" disabled>
					<%}else{%>
						<input type="button" class="btn btn-warning com-btn" value="Proceed" onclick="doSubmitTransSel();">
					<%}%>
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
					DLNG Charge Configuration
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<%-- <div class="cdbody">
      				<div class="row" >
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="form-group row">
					    		<div class="d-flex justify-content-center">
					    			<span><%=utilmsg.errorMessage("<b>Same Eff. Date will overwrite the Charges details, Different Eff. Date will create new entry!</b>")%></span>
					    		</div>
					    	</div>
					    </div>
					</div>
				</div> --%>
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
				      							<input type="checkbox" class="form-check-input" name="dlng_chk_plant" value="<%=VPLANT_SEQ_NO.elementAt(i)%>" 
				      							onclick="setTransChrg(this,'<%=i%>','<%=VDLNG_NOM_SEL_CUST_CHK.elementAt(i)%>');" 
				      							>
				      						</td>
				      						<td>
				      							<%=VPLANT_ABBR.elementAt(i)%>&nbsp;
				      							<i name="history_btn_<%=VPLANT_SEQ_NO.elementAt(i)%>" id="history_btn_<%=VPLANT_SEQ_NO.elementAt(i)%>" class="fa fa-info-circle fa-lg" aria-hidden="true" style="pointer-events: auto; color:#800040"></i>
				      							<input type="hidden" name="dlng_chk_plant_abbr" value="<%=VPLANT_ABBR.elementAt(i)%>">
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
							      						onblur="validateDate(this);checkChargesEffDate(this,'<%=VCHARGE_NAME.elementAt(j)%>','max_eff_dt_<%=VCHARGE_ABBR.elementAt(j)%>_<%=i%>_<%=j%>');"
							      						onchange="validateDate(this);checkChargesEffDate(this,'<%=VCHARGE_NAME.elementAt(j)%>','max_eff_dt_<%=VCHARGE_ABBR.elementAt(j)%>_<%=i%>_<%=j%>');" 
							      						autocomplete="off" disabled>
							      						<input type="hidden" name="max_eff_dt_<%=VCHARGE_ABBR.elementAt(j)%>" id="max_eff_dt_<%=VCHARGE_ABBR.elementAt(j)%>_<%=i%>_<%=j%>" >
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
        		<%if(!cont_status_flg.equals("X")&&!cont_status_flg.equals("C")&&!cont_status_flg.equals("T")){%>
        		<div class="" align="right">
					<input type="button" class="btn btn-warning com-btn" value="Proceed" onclick="doSubmitCustConfig('P');">
				</div>
				<%} %>
      		</div>
      	</div>
	</div>
</div>

<input type="hidden" name="option" value="LTCORA_CONTRACT_MST">
<input type="hidden" name="buy_sale" value="<%=buy_sale%>">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="agreement_type" value="<%=agreement_type%>">
<input type="hidden" name="no_of_billing_dtl" value="<%=no_of_billing_dtl%>">
<input type="hidden" name="no_of_security_dtl" value="<%=no_of_security_dtl%>">
<input type="hidden" name="cont_status_flg" value="<%=cont_status_flg%>">
<input type="hidden" name="change_request" value="">

<input type="hidden" name="u" value="<%=u%>">
<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<% for(int m=0; m<VCHARGE_ABBR.size(); m++){ %>
<input type="hidden" name="charge_abbr" id="charge_abbr_<%=m%>" value="<%=VCHARGE_ABBR.elementAt(m)%>">
<input type="hidden" name="charge_nm" id="charge_nm_<%=m%>" value="<%=VCHARGE_NAME.elementAt(m)%>">
<%}%>

<script>
function checkChargesEffDate(obj,chargeNm,maxId)
{
	var cont_start_dt = document.forms[0].start_dt.value;
	var cont_end_dt = document.forms[0].end_dt.value;
	
	var max_dt = document.getElementById(maxId).value;
	
	if((obj.value!="" && trim(obj.value) != "" && obj.value != null))
	{
		var count = compareDate(cont_start_dt,obj.value);
		var count1 = compareDate(obj.value,cont_end_dt);
		
		if(parseInt(count) == 1 || parseInt(count1) == 1)
		{
			alert(chargeNm+" Eff date should be in range of contract duration("+cont_start_dt+" - "+cont_end_dt+")!")
			obj.value="";
			return false;
		}
		
		count2 = compareDate(max_dt,obj.value);
		
		if(parseInt(count2) == 1 )
		{
			alert(chargeNm+" Eff date should be less then last "+max_dt+" effective date!")
			obj.value="";
			return false;
		}
	}
}
</script>
</form>
</body>
<%-- <script type="text/javascript">
$( document ).ready(function(){
	setValues('<%=strPlant%>','<%=strBuPlant%>'); 
	revisionEffDateShows();
	messurmentShows();
	off_spec_gasShows();
	liabilityShows();
	billingShows();
	terminatorShows();
	doEnbStorageClause();
});
</script> --%>
</html>