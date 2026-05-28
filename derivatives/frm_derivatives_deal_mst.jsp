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
	
	var url = "frm_derivatives_deal_mst.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	
	var cont_ref_no = document.forms[0].cont_ref_no.value;
	var trade_dt = document.forms[0].trade_dt.value;
	var trade_time = document.forms[0].trade_time.value;
	var ent_dt = document.forms[0].ent_dt.value;
	var ent_time = document.forms[0].ent_time.value;
	//var start_dt = document.forms[0].start_dt.value;
	//var end_dt = document.forms[0].end_dt.value;
	var dda_dt = document.forms[0].dda_dt.value;
	var signing_dt = document.forms[0].signing_dt.value;
	var signing_time = document.forms[0].signing_time.value;
	var cont_status_flg = document.forms[0].cont_status_flg.value;
	
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	var no_of_security_dtl = document.forms[0].no_of_security_dtl.value;
	var no_of_instrument_dtl = document.forms[0].no_of_instrument_dtl.value;

	//var agmt_base = document.forms[0].agmt_base.value;

	var chk_plant = document.forms[0].chk_plant;
	var chk_oth_plant = document.forms[0].chk_oth_plant;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var agmt_no = document.forms[0].agmt_no.value;
	
	var msg="";
	var flag=true;
	
	if(counterparty_cd == "" || counterparty_cd == "0")
	{
		msg+="Select Counterparty!\n";
		flag=false;
	}
	
	if(trim(cont_ref_no) == "")
	{
		msg+="Enter Trade Ref#!\n";
		flag=false;
	}

	if(opration=="INSERT")
	{
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
	if(trim(trade_dt) == "")
	{
		msg+="Enter Trade Date!\n";
		flag=false;
	}	
	if(trim(signing_dt) == "")
	{
		msg+="Enter Trade Confirmation Date!\n";
		flag=false;
	}
	if(trim(signing_time) == "")
	{
		msg+="Enter Trade Confirmation Time!\n";
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
			a = confirm("Do you want to Modify Derivative (Hedge) Deal?");
		}
		else
		{
			a = confirm("Do you want to Create New Derivative (Hedge) Deal?");
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
			//if(clearance=="KYC")
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
			if(trim(no_of_instrument_dtl) != "")
			{
				if(parseInt(no_of_instrument_dtl) <= 0)
				{
					temp_msg += "Please fill-in the Instrument Detail after Submitting Contract Detail!\n";
				}
			}
			else
			{
				temp_msg += "Please fill-in the Instrument Detail after Submitting Contract Detail!!\n";
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

function textCounter(field, countfield, maxlimit)
{
	if(field.value.length > maxlimit)
	{
		field.value = field.value.substring(0, maxlimit);
	}
	else
	{
		countfield.value = maxlimit - field.value.length;
	}
}

var newWindow;
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_derivatives_deal_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type,"CN Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_derivatives_deal_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type,"CN Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
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
	var trade_dt= document.forms[0].trade_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_derivatives_cont_billing_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
			"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+trade_dt+
			"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Derivative (Hedge) Deal Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Derivative (Hedge) Deal Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no)
{
	var opration = document.forms[0].opration.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_derivatives_deal_mst.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
			"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setAgmtDetail(countpty_cd,agmt_no,agmt_rev_no,agreement_type,agmt_nm)
{
	/* document.forms[0].agmt_no.value=agmt_no;
	document.forms[0].agmt_rev_no.value=agmt_rev_no;
	document.forms[0].agmt_nm.value=agmt_nm */
	var opration = document.forms[0].opration.value;
	var contract_type = document.forms[0].contract_type.value;
	
	var u = document.forms[0].u.value;
	
	//document.forms[0].agreement_type.value=agmt_type;

	var url = "frm_derivatives_deal_mst.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+
		"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&contract_type="+contract_type+"&agmt_nm="+agmt_nm+
		"&u="+u;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);

}


function setValues(strPlant,strBuPlant)
{
	var chk_plant = document.forms[0].chk_plant;
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

function checkRateFormate(obj) //WHEN CHANGE RATE UNIT
{ 
	var a="0"
	var b="0"
	
	var rate = document.forms[0].cargo_price;
	
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

var securityWindow;
function securityPreReceipt(cont_type,agmt,agmt_rev,cont,cont_rev)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var sec_category = document.forms[0].sec_category.value;
	
	var signing_dt = document.forms[0].signing_dt.value;
	var temp_end_dt = document.forms[0].temp_end_dt.value;
	
	var u = document.forms[0].u.value;
	
	if(trim(cont_type)!="" && (trim(cont)!="" && cont!="0") 
			&& (trim(counterparty_cd)!="" && counterparty_cd!="0")
			&& trim(sec_category)!="")
	{
		var url = "../credit_risk/frm_pre_receipt_security.jsp?counterparty_cd="+counterparty_cd+"&cont_no="+cont+"&cont_rev_no="+cont_rev+
			"&agmt_no="+agmt+"&agmt_rev_no="+agmt_rev+"&contract_type="+cont_type+"&sec_category="+sec_category+"&start_dt="+signing_dt+"&end_dt="+temp_end_dt+"&u="+u;
		
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


var priceWindow;
function variablePriceConfig(cont_type,agmt,agmt_rev,cont,cont_rev,cont_ref,cont_status,start_dt,end_dt,cargo_no,cn_contract_status)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	//var temp_start_dt = document.forms[0].temp_start_dt.value;
	//var temp_end_dt = document.forms[0].temp_end_dt.value;
	
	var u = document.forms[0].u.value;
	
	if(trim(cont_type)!="" && (trim(cont)!="" && cont!="0") && (trim(counterparty_cd)!="" && counterparty_cd!="0"))
	{
		var url = "../market_risk/frm_config_price_mst.jsp?counterparty_cd="+counterparty_cd+"&cont_no="+cont+"&cont_rev_no="+cont_rev+"&cont_ref="+cont_ref+
			"&agmt_no="+agmt+"&agmt_rev_no="+agmt_rev+"&contract_type="+cont_type+"&start_dt="+start_dt+"&end_dt="+end_dt+"&cont_status="+cont_status+"&cargo_no="+cargo_no+"&cn_contract_status="+cn_contract_status+
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

function doSubmitInstru()
{
	
	document.forms[0].option.value = "HEDGE_INSTRUMENT_DTL";
	document.forms[0].opration.value = "MODIFY";
	
	var instrument_type = document.forms[0].instrument_type.value;
	var buy_sell = document.forms[0].buy_sell.value;
	var product_nm = document.forms[0].product_nm.value;
	var qty = document.forms[0].qty.value;
	var rate = document.forms[0].rate.value;
	var rate_unit = document.forms[0].rate_unit.value;
	var curve_nm = document.forms[0].curve_nm.value;
	var proj_method = document.forms[0].proj_method.value;
	var cont_month = document.forms[0].cont_month.value;
	var cont_year = document.forms[0].cont_year.value;
	var price_start_dt = document.forms[0].price_start_dt.value;
	var price_end_dt = document.forms[0].price_end_dt.value;
	var conv_factor = document.forms[0].conv_factor.value;
	var conv_factor_unit = document.forms[0].conv_factor_unit.value;
	var inv_instrument_no = document.forms[0].inv_instrument_no.value;
	var inv_cr_dr_gen = document.forms[0].inv_cr_dr_gen.value;
	var instrument_no = document.forms[0].instrument_no.value;
	
	
	var msg="";
	var flag=true;
	
	if(inv_instrument_no!="" && instrument_no==inv_instrument_no && inv_cr_dr_gen=="")
	{
		msg+="Can not Modify Instrument details as Invoice Generated for This Instrument!\n";
		flag=false;
	}
	else
	{
		if(trim(instrument_type)=="" || instrument_type==0)
		{
			msg+="Please Select Instrument Type!\n";
			flag=false;
		}
		
		if(trim(buy_sell)=="" || buy_sell==0)
		{
			msg+="Please Select Buy/Sell!\n";
			flag=false;
		}
		if(trim(product_nm)=="" || product_nm==0)
		{
			msg+="Please Select Product Name!\n";
			flag=false;
		}
		if(trim(qty)=="")
		{
			msg+="Please Enter Quantity!\n";
			flag=false;
		}
		if(trim(conv_factor) == "")
		{
			msg+="Enter Conversion Factor!\n";
			flag=false;
		}
		if(trim(conv_factor_unit) == "")
		{
			msg+="Select Conversion Factor Unit!\n";
			flag=false;
		}
		if(trim(rate)=="")
		{
			msg+="Please Enter Fixed Price!\n";
			flag=false;
		}
		if(trim(curve_nm)=="" || curve_nm==0)
		{
			msg+="Please Select Curve Name!\n";
			flag=false;
		}
		if(trim(proj_method)=="" || proj_method==0)
		{
			msg+="Please Select Projection Method!\n";
			flag=false;
		}
		if(trim(cont_month)=="" || cont_month==0)
		{
			msg+="Please Select Contract Month!\n";
			flag=false;
		}
		if(trim(cont_year)=="" || cont_year==0)
		{
			msg+="Please Select Contract Year!\n";
			flag=false;
		}
		if(trim(price_start_dt)=="" || price_start_dt==0)
		{
			msg+="Please Enter Pricing Start Date!\n";
			flag=false;
		}
		else
		{
			var start_dt = document.forms[0].price_start_dt.value;
			var temp_price_start_dt = document.forms[0].temp_price_start_dt.value;
			var temp_price_end_dt = document.forms[0].temp_price_end_dt.value;
			
			var splitSt_dt = start_dt.split("/");
			var splitPeriod_St_dt = temp_price_start_dt.split("/");
			var splitPeriod_end_dt = temp_price_end_dt.split("/");
			
			var temp_st_dt = splitSt_dt[2]+splitSt_dt[1]+splitSt_dt[0];
			var temp_period_st_dt = splitPeriod_St_dt[2]+splitPeriod_St_dt[1]+splitPeriod_St_dt[0];
			var temp_period_end_dt = splitPeriod_end_dt[2]+splitPeriod_end_dt[1]+splitPeriod_end_dt[0];
			
			if(start_dt!="")
			{
				if(temp_st_dt < temp_period_st_dt)
				{
					alert("Pricing Start date ("+start_dt+") should not be Less than "+temp_price_start_dt+" !");
					document.forms[0].price_start_dt.value=temp_price_start_dt;
					$("input[name='price_start_dt']").datepicker("update", temp_price_start_dt);
					flag=false;
				}
				if(temp_period_end_dt < temp_st_dt)
				{
					alert("Pricing Start date ("+start_dt+") should not be greater than "+temp_price_end_dt+" !");
					document.forms[0].price_start_dt.value=temp_price_start_dt;
					$("input[name='price_start_dt']").datepicker("update", temp_price_start_dt);
					flag=false;
				}
			}
		}
		if(trim(price_end_dt)=="" || price_end_dt==0)
		{
			msg+="Please Enter Pricing End Date!\n";
			flag=false;
		}
		else
		{
			var end_dt = document.forms[0].price_end_dt.value;
			var temp_price_start_dt = document.forms[0].temp_price_start_dt.value;
			var temp_price_end_dt = document.forms[0].temp_price_end_dt.value;
			
			var splitEnd_dt = end_dt.split("/");
			var splitPeriod_St_dt = temp_price_start_dt.split("/");
			var splitPeriod_end_dt = temp_price_end_dt.split("/");
			
			var temp_end_dt = splitEnd_dt[2]+splitEnd_dt[1]+splitEnd_dt[0];
			var temp_period_st_dt = splitPeriod_St_dt[2]+splitPeriod_St_dt[1]+splitPeriod_St_dt[0];
			var temp_period_end_dt = splitPeriod_end_dt[2]+splitPeriod_end_dt[1]+splitPeriod_end_dt[0];
			
			if(end_dt!="")
			{
				if(temp_end_dt > temp_period_end_dt)
				{
					alert("Pricing End date ("+end_dt+") should not be greater than "+temp_price_end_dt+" !");
					document.forms[0].price_end_dt.value=temp_price_end_dt;
					$("input[name='price_end_dt']").datepicker("update", temp_price_end_dt);
					flag=false;
				}
				
				if(temp_end_dt < temp_period_st_dt)
				{
					alert("Pricing End date ("+end_dt+") should not be Less than "+temp_price_start_dt+" !");
					document.forms[0].price_end_dt.value=temp_price_end_dt;
					$("input[name='price_end_dt']").datepicker("update", temp_price_end_dt);
					flag=false;
				}
			}
		}
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Insert the Instrument Details?");
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

function openAgreementList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agreement_type = "U";
	if(counterparty_cd=="0" || counterparty_cd=="")
	{
		alert("Select Counterparty!");
	}
	else
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open("frm_derivatives_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_derivatives_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function doModifyInstrument(VINSTRUMENT_NO, VDISP_INSTRUMENT_NO, VINSTRUMENT_TYPE, VINSTRUMENT_BUY_SELL, 
		VINSTRUMENT_STATUS_FLG, VINSTRUMENT_STATUS, VINSTRUMENT_QTY, VINSTRUMENT_QTY_UNIT, VINSTRUMENT_RATE,
		VINSTRUMENT_RATE_UNIT, VINSTRUMENT_RATE_UNIT_FLG, VPRICE_START_DT, VPRICE_END_DT, VPRODUCT_NM, VCURVE_NM,
		VPROJ_METHOD, VCONT_DD_MM_YR,VCONVERSION_FACTOR,VCONVERSION_FACTOR_UNIT,VGEN_INV_INSTRUMENT_NO,VGEN_PDF_INV_INSTRUMENT_NO,VCR_DR_CRITERIA)
{
	document.forms[0].show_instrument_number.value = VDISP_INSTRUMENT_NO;
	document.forms[0].instrument_no.value = VINSTRUMENT_NO;
	document.forms[0].instrument_type.value = VINSTRUMENT_TYPE;
	document.forms[0].buy_sell.value=VINSTRUMENT_BUY_SELL;
	document.forms[0].inst_status_flag.value=VINSTRUMENT_STATUS_FLG;
	document.forms[0].product_nm.value=VPRODUCT_NM;
	document.forms[0].qty.value=VINSTRUMENT_QTY;
	
	document.forms[0].conv_factor.value=VCONVERSION_FACTOR;
	if(VINSTRUMENT_QTY_UNIT=="MMBTU")
	{
		document.forms[0].conv_factor.readOnly = true;
	}
	else
	{
		document.forms[0].conv_factor.readOnly = false;
	}
	
	document.forms[0].conv_factor_unit.value=VINSTRUMENT_QTY_UNIT+" to MMBTU";;
	document.forms[0].conv_factor_unit.readOnly = true;
	
	/* document.forms[0].conv_factor.value=VCONVERSION_FACTOR;
	document.forms[0].conv_factor_unit.value=VCONVERSION_FACTOR_UNIT; */

	lbl_qty.innerHTML="Quantity ("+VINSTRUMENT_QTY_UNIT+")<span class='s-red'>*</span>";
	document.forms[0].qty_unit.value=VINSTRUMENT_QTY_UNIT;
	
	document.forms[0].rate.value=VINSTRUMENT_RATE;
	
	// Create a new option
	const newOption1 = document.createElement("option");
	newOption1.value = "$/"+VINSTRUMENT_QTY_UNIT;
	newOption1.text = "$/"+VINSTRUMENT_QTY_UNIT;
	// Add the new option to the select element
	document.forms[0].disp_rate_unit.appendChild(newOption1);
	
	document.forms[0].rate_unit.value=VINSTRUMENT_RATE_UNIT;
	document.forms[0].curve_nm.value=VCURVE_NM;
	
	// Create a new option
	const newOption = document.createElement("option");
	newOption.value = VPROJ_METHOD;
	newOption.text = VPROJ_METHOD;

	// Add the new option to the select element
	document.forms[0].proj_method.appendChild(newOption);
	
	
	document.forms[0].proj_method.value=VPROJ_METHOD;
	
	var cont_dd_mm_yr = VCONT_DD_MM_YR;
	
	var splitContDt = cont_dd_mm_yr.split("/");
	
	document.forms[0].cont_month.value=splitContDt[1];
	document.forms[0].cont_year.value=splitContDt[2];
	document.forms[0].price_start_dt.value=VPRICE_START_DT;
	document.forms[0].temp_price_start_dt.value=VPRICE_START_DT;
	document.forms[0].price_end_dt.value=VPRICE_END_DT;
	document.forms[0].temp_price_end_dt.value=VPRICE_END_DT;
	document.forms[0].inv_instrument_no.value=VGEN_INV_INSTRUMENT_NO;
	document.forms[0].inv_cr_dr_gen.value=VCR_DR_CRITERIA;
	$("input[name='price_start_dt']").datepicker("update", VPRICE_START_DT);
	$("input[name='price_end_dt']").datepicker("update", VPRICE_END_DT);
	
	if(VGEN_PDF_INV_INSTRUMENT_NO!="")
	{
		document.forms[0].submit_btn.style.display="none";
		document.forms[0].buy_sell.style.pointerEvents="none";
		document.forms[0].buy_sell.style.backgroundColor="#eee";
		document.forms[0].instrument_type.style.pointerEvents="none";
		document.forms[0].instrument_type.style.backgroundColor="#eee";
		document.forms[0].product_nm.style.pointerEvents="none";
		document.forms[0].product_nm.style.backgroundColor="#eee";
		document.forms[0].inst_status_flag.style.pointerEvents="none";
		document.forms[0].inst_status_flag.style.backgroundColor="#eee";
		document.forms[0].cont_month.style.pointerEvents="none";
		document.forms[0].cont_month.style.backgroundColor="#eee";
		document.forms[0].cont_year.style.pointerEvents="none";
		document.forms[0].cont_year.style.backgroundColor="#eee";
		document.forms[0].price_start_dt.style.pointerEvents="none";
		document.forms[0].price_start_dt.style.backgroundColor="#eee";
		document.forms[0].price_end_dt.style.pointerEvents="none";
		document.forms[0].price_end_dt.style.backgroundColor="#eee";
		document.forms[0].disp_rate_unit.style.backgroundColor="#eee";
		document.forms[0].qty.readOnly=true;
		document.forms[0].conv_factor.readOnly=true;
		document.forms[0].rate.readOnly=true;
		
		var cnt=0;
		if(VCR_DR_CRITERIA.includes("QTY"))
		{
			document.forms[0].qty.readOnly=false;
			cnt++;
		}
		else
		{
			document.forms[0].qty.readOnly=true;
		}
		if(VCR_DR_CRITERIA.includes("FIXEDPRICE"))
		{
			document.forms[0].rate.readOnly=false;
			cnt++;
		}
		else
		{
			document.forms[0].rate.readOnly=true;
		}
		if(cnt>0)
		{
			document.forms[0].submit_btn.style.display="block";
		}
		else
		{
			document.forms[0].submit_btn.style.display="none";
		}
		
	}
	else
	{
		document.forms[0].submit_btn.style.display="block";
		document.forms[0].buy_sell.style.pointerEvents="";
		document.forms[0].buy_sell.style.backgroundColor="";
		document.forms[0].instrument_type.style.pointerEvents="";
		document.forms[0].instrument_type.style.backgroundColor="";
		document.forms[0].product_nm.style.pointerEvents="";
		document.forms[0].product_nm.style.backgroundColor="";
		document.forms[0].inst_status_flag.style.pointerEvents="";
		document.forms[0].inst_status_flag.style.backgroundColor="";
		document.forms[0].cont_month.style.pointerEvents="";
		document.forms[0].cont_month.style.backgroundColor="";
		document.forms[0].cont_year.style.pointerEvents="";
		document.forms[0].cont_year.style.backgroundColor="";
		document.forms[0].price_start_dt.style.pointerEvents="";
		document.forms[0].price_start_dt.style.backgroundColor="";
		document.forms[0].price_end_dt.style.pointerEvents="";
		document.forms[0].price_end_dt.style.backgroundColor="";
		document.forms[0].disp_rate_unit.style.backgroundColor="";
		document.forms[0].qty.readOnly=false;
		document.forms[0].conv_factor.readOnly=false;
		document.forms[0].rate.readOnly=false;
	}
}

function clearInstument(show_instrument_number,instrument_no,year)
{
	document.forms[0].show_instrument_number.value = show_instrument_number;
	document.forms[0].instrument_no.value = instrument_no;
	document.forms[0].instrument_type.value = "Fixed/Float Swap";
	document.forms[0].buy_sell.value="BUY";
	document.forms[0].inst_status_flag.value="Y";
	document.forms[0].product_nm.value="";
	document.forms[0].qty.value="";
	document.forms[0].conv_factor.value="";
	document.forms[0].conv_factor_unit.value="";
	//document.forms[0].qty_unit.value=VINSTRUMENT_QTY_UNIT;
	document.forms[0].rate.value="";
	document.forms[0].rate_unit.value="2";
	document.forms[0].curve_nm.value="";
	
	document.forms[0].proj_method.value="";

	document.forms[0].cont_month.value="00";
	document.forms[0].cont_year.value=year;
	document.forms[0].price_start_dt.value="";
	document.forms[0].temp_price_start_dt.value="";
	document.forms[0].price_end_dt.value="";
	document.forms[0].temp_price_end_dt.value="";
	document.forms[0].inv_instrument_no.value="";
	document.forms[0].inv_cr_dr_gen.value="";
	document.forms[0].submit_btn.style.display="block";
	
	document.forms[0].buy_sell.style.pointerEvents="";
	document.forms[0].buy_sell.style.backgroundColor="";
	document.forms[0].instrument_type.style.pointerEvents="";
	document.forms[0].instrument_type.style.backgroundColor="";
	document.forms[0].product_nm.style.pointerEvents="";
	document.forms[0].product_nm.style.backgroundColor="";
	document.forms[0].inst_status_flag.style.pointerEvents="";
	document.forms[0].inst_status_flag.style.backgroundColor="";
	document.forms[0].cont_month.style.pointerEvents="";
	document.forms[0].cont_month.style.backgroundColor="";
	document.forms[0].cont_year.style.pointerEvents="";
	document.forms[0].cont_year.style.backgroundColor="";
	document.forms[0].price_start_dt.style.pointerEvents="";
	document.forms[0].price_start_dt.style.backgroundColor="";
	document.forms[0].price_end_dt.style.pointerEvents="";
	document.forms[0].price_end_dt.style.backgroundColor="";
	document.forms[0].disp_rate_unit.style.backgroundColor="";
	document.forms[0].qty.readOnly=false;
	document.forms[0].conv_factor.readOnly=false;
	document.forms[0].rate.readOnly=false;
}

function revisionEffDateShows()
{
	const checkbox = document.getElementById("rev_chk");
	const div_std = document.getElementById("rev_chk_div");
	const cont_status_flg = document.forms[0].cont_status_flg.value;

	if(cont_status_flg!="F")
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

function checkPeriodStartdate()
{
	var start_dt = document.forms[0].price_start_dt.value;
	var temp_price_start_dt = document.forms[0].temp_price_start_dt.value;
	var temp_price_end_dt = document.forms[0].temp_price_end_dt.value;
	
	var splitSt_dt = start_dt.split("/");
	var splitPeriod_St_dt = temp_price_start_dt.split("/");
	var splitPeriod_end_dt = temp_price_end_dt.split("/");
	
	var temp_st_dt = splitSt_dt[2]+splitSt_dt[1]+splitSt_dt[0];
	var temp_period_st_dt = splitPeriod_St_dt[2]+splitPeriod_St_dt[1]+splitPeriod_St_dt[0];
	var temp_period_end_dt = splitPeriod_end_dt[2]+splitPeriod_end_dt[1]+splitPeriod_end_dt[0];
	
	if(start_dt!="")
	{
		if(temp_st_dt < temp_period_st_dt)
		{
			alert("Pricing Start date ("+start_dt+") should not be Less than "+temp_price_start_dt+" !");
			document.forms[0].price_start_dt.value=temp_price_start_dt;
			$("input[name='price_start_dt']").datepicker("update", temp_price_start_dt);
			return false;
		}
		if(temp_period_end_dt < temp_st_dt)
		{
			alert("Pricing Start date ("+start_dt+") should not be greater than "+temp_price_end_dt+" !");
			document.forms[0].price_start_dt.value=temp_price_start_dt;
			$("input[name='price_start_dt']").datepicker("update", temp_price_start_dt);
			return false;
		}
	}
}

function checkPeriodEnddate()
{
	var end_dt = document.forms[0].price_end_dt.value;
	var temp_price_start_dt = document.forms[0].temp_price_start_dt.value;
	var temp_price_end_dt = document.forms[0].temp_price_end_dt.value;
	
	var splitEnd_dt = end_dt.split("/");
	var splitPeriod_St_dt = temp_price_start_dt.split("/");
	var splitPeriod_end_dt = temp_price_end_dt.split("/");
	
	var temp_end_dt = splitEnd_dt[2]+splitEnd_dt[1]+splitEnd_dt[0];
	var temp_period_st_dt = splitPeriod_St_dt[2]+splitPeriod_St_dt[1]+splitPeriod_St_dt[0];
	var temp_period_end_dt = splitPeriod_end_dt[2]+splitPeriod_end_dt[1]+splitPeriod_end_dt[0];
	
	if(end_dt!="")
	{
		if(temp_end_dt > temp_period_end_dt)
		{
			alert("Pricing End date ("+end_dt+") should not be greater than "+temp_price_end_dt+" !");
			document.forms[0].price_end_dt.value=temp_price_end_dt;
			$("input[name='price_end_dt']").datepicker("update", temp_price_end_dt);
			
			return false;
		}
		
		if(temp_end_dt < temp_period_st_dt)
		{
			alert("Pricing End date ("+end_dt+") should not be Less than "+temp_price_start_dt+" !");
			document.forms[0].price_end_dt.value=temp_price_end_dt;
			$("input[name='price_end_dt']").datepicker("update", temp_price_end_dt);
			return false;
		}
	}
}

function checkSigningDdaDate(obj)
{
	var eff_dt = obj.value;
	var agmt_signing_dt = document.forms[0].agmt_signing_dt.value;
	var agmt_end_dt = document.forms[0].agmt_end_dt.value;
	
	var spliteff_dt = eff_dt.split("/");
	var splitAgmtSign_dt = agmt_signing_dt.split("/");
	var splitAgmt_end_dt = agmt_end_dt.split("/");
	
	var tmp_eff_dt = spliteff_dt[2]+spliteff_dt[1]+spliteff_dt[0];
	var tmp_agmt_sig_dt = splitAgmtSign_dt[2]+splitAgmtSign_dt[1]+splitAgmtSign_dt[0];
	var temp_agmt_end_dt = splitAgmt_end_dt[2]+splitAgmt_end_dt[1]+splitAgmt_end_dt[0];
	
	if (agmt_signing_dt == "")
	{
		alert("First Select the Agreement and then Proceed !");
	}
	else 
	{		
		if(eff_dt!="")
		{
			if(tmp_eff_dt < tmp_agmt_sig_dt)
			{
				alert("Entered date ("+eff_dt+") should be Greater than or equals to Agreement date ("+agmt_signing_dt+") !");
				obj.value="";
				
				return false;
			}
			
			if(temp_agmt_end_dt < tmp_eff_dt)
			{
				alert("Entered date ("+eff_dt+") should be Less than Agreement End date ("+agmt_end_dt+") !");
				obj.value="";
				
				return false;
			}
		}
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

function checkInrUsd()
{
	var rate_unit=document.forms[0].cargo_rate_unit.value;
	var rate = document.forms[0].cargo_price.value;
	
	if(rate_unit!="" && rate!="")
	{
		if(rate_unit=="1")
		{
			checkValuePrecision(rate,"2","4","cargo_price");
		}
		else if(rate_unit=="1")
		{
			checkValuePrecision(rate,"4","2","cargo_price");
		}
	}
}

function changePayRecv()
{
	var buy_sell = document.forms[0].buy_sell.value;
	var fixed_lable = document.getElementById('fixed_lable');
	var float_lable = document.getElementById('float_lable');
	
	if(buy_sell=="BUY")
	{
		fixed_lable.innerHTML="Fixed Side (Pay)";
		float_lable.innerHTML="Float Side (Receive)";
	}
	else if(buy_sell=="SELL")
	{
		fixed_lable.innerHTML="Fixed Side (Receive)";
		float_lable.innerHTML="Float Side (Pay)";
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
</script>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DataBean_Derivarives_mst" id="db_derivative" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String dateTime = utildate.getSysdateWithTime24hr();
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String clearance = request.getParameter("clearance")==null?"KYC":request.getParameter("clearance");

String agmt_nm = request.getParameter("agmt_nm")==null?"":request.getParameter("agmt_nm");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String instrument_no = request.getParameter("instrument_no")==null?"":request.getParameter("instrument_no");
String agreement_base=request.getParameter("agmt_base")==null?"":request.getParameter("agmt_base");
String contract_type=request.getParameter("contract_type")==null?"V":request.getParameter("contract_type");
String agreement_type = request.getParameter("agreement_type")==null?"U":request.getParameter("agreement_type"); 


db_derivative.setCallFlag("HEDGE_CONT_MST");
db_derivative.setClearance(clearance);
db_derivative.setCounterparty_cd(counterparty_cd);
db_derivative.setComp_cd(owner_cd);
db_derivative.setOpration(opration);
db_derivative.setAgmt_no(agmt_no);
db_derivative.setAgmt_rev_no(agmt_rev_no);
db_derivative.setCont_no(cont_no);
db_derivative.setCont_rev_no(cont_rev_no);
db_derivative.setContract_type(contract_type);
db_derivative.setInstrument_no(instrument_no);
db_derivative.setAgreement_type(agreement_type);
db_derivative.init();

Vector VCOUNTERPARTY_CD = db_derivative.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = db_derivative.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = db_derivative.getVCOUNTERPARTY_ABBR();

Vector VPLANT_NM = db_derivative.getVPLANT_NM();
Vector VPLANT_ABBR = db_derivative.getVPLANT_ABBR();
Vector VPLANT_SEQ_NO = db_derivative.getVPLANT_SEQ_NO();
Vector VBU_CD = db_derivative.getVBU_CD();
Vector VBU_PLANT_NM = db_derivative.getVBU_PLANT_NM();
Vector VBU_PLANT_ABBR = db_derivative.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = db_derivative.getVBU_PLANT_SEQ_NO();

Vector VSEL_PLANT_SEQ_NO = db_derivative.getVSEL_PLANT_SEQ_NO();
Vector VSEL_BU_CD = db_derivative.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = db_derivative.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_TRAD_CD = db_derivative.getVSEL_TRAD_CD();
Vector VSEL_PLANT_ABBR = db_derivative.getVSEL_PLANT_ABBR();
Vector VSEL_BU_PLANT_ABBR = db_derivative.getVSEL_BU_PLANT_ABBR();

Vector VPROD_TYPE = db_derivative.getVPROD_TYPE();
Vector VCURVE_TYPE = db_derivative.getVCURVE_TYPE();

Vector VINSTRUMENT_NO = db_derivative.getVINSTRUMENT_NO();
Vector VDISP_INSTRUMENT_NO = db_derivative.getVDISP_INSTRUMENT_NO();
Vector VINSTRUMENT_TYPE = db_derivative.getVINSTRUMENT_TYPE();
Vector VINSTRUMENT_BUY_SELL = db_derivative.getVINSTRUMENT_BUY_SELL();
Vector VINSTRUMENT_STATUS_FLG = db_derivative.getVINSTRUMENT_STATUS_FLG();
Vector VINSTRUMENT_STATUS = db_derivative.getVINSTRUMENT_STATUS();
Vector VINSTRUMENT_QTY = db_derivative.getVINSTRUMENT_QTY();
Vector VINSTRUMENT_QTY_UNIT = db_derivative.getVINSTRUMENT_QTY_UNIT();
Vector VINSTRUMENT_RATE = db_derivative.getVINSTRUMENT_RATE();
Vector VINSTRUMENT_RATE_UNIT = db_derivative.getVINSTRUMENT_RATE_UNIT();
Vector VINSTRUMENT_RATE_UNIT_FLG = db_derivative.getVINSTRUMENT_RATE_UNIT_FLG();
Vector VPRICE_START_DT = db_derivative.getVPRICE_START_DT();
Vector VPRICE_END_DT = db_derivative.getVPRICE_END_DT();
Vector VPRODUCT_NM = db_derivative.getVPRODUCT_NM();
Vector VCURVE_NM = db_derivative.getVCURVE_NM();
Vector VPROJ_METHOD = db_derivative.getVPROJ_METHOD();
Vector VCONT_DD_MM_YR = db_derivative.getVCONT_DD_MM_YR();
Vector VCONVERSION_FACTOR = db_derivative.getVCONVERSION_FACTOR();
Vector VCONVERSION_FACTOR_UNIT = db_derivative.getVCONVERSION_FACTOR_UNIT();
Vector VGEN_INV_INSTRUMENT_NO = db_derivative.getVGEN_INV_INSTRUMENT_NO();
Vector VGEN_PDF_INV_INSTRUMENT_NO = db_derivative.getVGEN_PDF_INV_INSTRUMENT_NO();
Vector VCR_DR_CRITERIA = db_derivative.getVCR_DR_CRITERIA();

//Vector VINDEX = db_derivative.getVINDEX();
String min_counterparty_eff_dt = db_derivative.getMin_counterparty_eff_dt();
String trade_dt = db_derivative.getTrade_dt();
String trade_time = db_derivative.getTrade_time();
String agmt_signing_dt = db_derivative.getAgmt_signing_dt();
String ent_dt = db_derivative.getEnt_dt();
String ent_time = db_derivative.getEnt_time();
String start_dt = db_derivative.getStart_dt();
String end_dt = db_derivative.getEnd_dt();
String agmt_start_dt = db_derivative.getAgmt_start_dt();
String agmt_end_dt = db_derivative.getAgmt_end_dt();
String agmt_type = db_derivative.getAgmt_type();
String remark = db_derivative.getRemark();
String contpty_abbr = db_derivative.getContpty_abbr();
String agmt_name = db_derivative.getAgmt_name();
String agmt_ref_no =db_derivative.getAgmt_ref_no();
String cont_name=db_derivative.getCont_name();
String cont_disp_name=db_derivative.getCont_disp_name();
String cont_ref_no=db_derivative.getCont_ref_no();
String dda_dt=db_derivative.getDda_dt();
String dda_time=db_derivative.getDda_time();
String cont_status_flg = db_derivative.getCont_status_flg();
String cont_status = db_derivative.getCont_status();
String bill_flag = db_derivative.getBilling_flag();
String billing_clause = db_derivative.getBilling_clause();
String rev_dt = db_derivative.getRev_dt();
//String conversion_factor= db_derivative.getConversion_factor();
//String conversion_factor_unit = db_derivative.getConversion_factor_unit();
String price_change_history="";
String cargo_status_flag = "";
if(cargo_status_flag.equals(""))
{
	cargo_status_flag="N";
}

String fcc_flg=db_derivative.getFcc_flg();
String no_of_instrument_dtl = db_derivative.getNo_of_instrument_dtl();
String no_of_security_dtl=db_derivative.getNo_of_security_dtl();
String no_of_billing_dtl=db_derivative.getNo_of_billing_dtl();
String split_flag = db_derivative.getSplit_flag();
String split_type = db_derivative.getSplit_type();
String instrument_number = db_derivative.getInstrument_number();
String show_instrument_number = db_derivative.getShow_instru_number();
String day_def_clause = db_derivative.getDay_def_clause();
String demurrage_clause = db_derivative.getDemurrage_clause();
String measurement_clause = db_derivative.getMeasurement_clause();
String spec_gas_clause = db_derivative.getSpec_gas_clause();
String agmt_agmt_base=db_derivative.getAgmt_agmt_base();
String signing_dt=db_derivative.getSigning_dt();
String signing_time=db_derivative.getSigning_time();
String is_inv_gen=db_derivative.getIs_inv_gen();
String closure_note=db_derivative.getClosure_note();

if(cont_status_flg.equals("")){
	cont_status_flg="F";
	cont_status="New";
}

if(trade_time.equals("")){
	trade_time="00:00";
}
if(dda_time.equals("")){
	dda_time="00:00";
}
if(signing_time.equals("")){
	signing_time="00:00";
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

String sec_category_value="";

if((no_of_security_dtl.equals("0") || no_of_security_dtl.equals("")))
{
	sec_category_value = "R";
}
else
{
	sec_category_value = db_derivative.getSec_category_value();
}

int currentYear = utildate.getCurrentYear();

//<!--Harsh Maheta 20230903 : Added for old values to show in Deal audit history-->//
String cp_name = db_derivative.getContpty_name();
String cp_abbr = db_derivative.getContpty_abbr();

String mapped_cont_no = db_derivative.getMapped_cont_no();
String old_value="CP="+counterparty_cd+"#NAME="+cp_name+"#ABBR="+cp_abbr+"#CONTNAME="+cont_name+"#CONTNO="+mapped_cont_no+"#CONTREFNO="+cont_ref_no+
"#CONTTYPE="+contract_type+"#DDADT="+dda_dt+"#DDATIME="+dda_time+"#TRADEDT="+trade_dt+"#TRADEIME="+trade_time+
"#ENTDT="+ent_dt+"#ENTTIME="+ent_time+"#AGMTTYPE="+agreement_type+"#STARTDT="+start_dt+"#ENDDT="+end_dt+"#CONT_STATUS="+cont_status_flg;
%>
<body onload="billingShows();setValues('<%=strPlant%>','<%=strBuPlant%>');<%if(opration.equals("MODIFY")){ %>revisionEffDateShows();<%} %>"> 
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_DerivativesMaster">

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
					    	Derivatives Deal
					    </div>
					    <div class="btn-group">
						  <label class="btn btn-outline-secondary btngrp <%if(opration.equals("INSERT")){%>btnactive<%}%>" onclick="refresh('INSERT','<%=clearance%>');"><i class="fa fa-plus-circle"></i>&nbsp;New</label>
						  <label class="btn btn-outline-secondary btngrp <%if(opration.equals("MODIFY")){%>btnactive<%}%>" onclick="refresh('MODIFY','<%=clearance%>');"><i class="fa fa-edit"></i>&nbsp;Modify</label>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
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
				    			<label class="form-label"><b>Contract Type</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<%if(opration.equals("INSERT")){ %>
									<div class="col">
					    		<%}else{ %>
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    		<%} %>
				    				<select class="form-select form-select-sm" name="contract_type"  style="pointer-events: none;">
				    					<option value="V">Derivatives</option>
				    				</select>
				    				<script>document.forms[0].contract_type.value="<%=contract_type%>"</script>
				      			</div>
				      			<%if(opration.equals("INSERT")){ %>
								<div class="col-auto">
				    				<input type="button" class="btn btn-info btn-sm select_btn" value="Select Agreement" style="font-weight: bold;" onclick="openAgreementList();" <%if(opration.equals("MODIFY")){ %>disabled<%} %>>
				    			</div>
				    			<div class="col">
				    				<input type="text" class="form-control form-control-sm" name="agmt_nm" Value="<%=agmt_nm %>" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_no" value="<%=cont_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_rev_no" value="<%=cont_rev_no%>" maxLength="2" readOnly>
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
								<div class="col-sm-4 col-xs-4 col-md-4">
				    				<input type="text" class="form-control form-control-sm" name="" 
				    				<%if(!cont_no.equals("")){ %> value="<%=cont_disp_name%>" <%} %>
				    				maxLength="50" readOnly>
				    			</div>
				    			<div class="col-sm-8 col-xs-8 col-md-8">
				    				<input type="text" class="form-control form-control-sm" name="cont_disp_name" value="<%=cont_name%>" maxLength="50" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="cont_name" value="<%=cont_disp_name%>" maxLength="50" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_no" value="<%=cont_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_rev_no" value="<%=cont_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_base" value="<%=agreement_base%>" maxLength="2" readOnly>
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
				    				<%if(cont_status_flg.equals("X") || cont_status_flg.equals("C")){%>
				    					<i name="disp_cont_status_remark" id="disp_cont_status_remark" class="fa fa-info-circle fa-lg" aria-hidden="true" style="pointer-events: auto; color:#800040" title="<%=closure_note%>"></i>
				    				<%} %>
				    				
				    				<input type="hidden" name="cont_status_remark" value="<%=closure_note%>">
				    				<input type="hidden" name="cont_status" value="<%=cont_status%>">
				      			</div>
				  			</div>
						</div>
					</div>
					<%} %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Trade Ref#<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="cont_ref_no" value="<%=cont_ref_no%>" maxLength="25" oninput="handleComma(this,'Trade Ref#')">
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
				      					<input type="hidden" name="agmt_signing_dt" value="<%=agmt_signing_dt%>">
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="dda_dt" value="<%=dda_dt%>" maxLength="10" 
			      						onblur="validateDate(this);checkSigningDdaDate(this);" 
			      						onchange="validateDate(this);checkSigningDdaDate(this);"
			      						autocomplete="off">
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
							<label class="form-label"><b>Trade Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="trade_dt" value="<%=trade_dt%>" maxLength="10" 
			      						onblur="validateDate(this);checkSigningDdaDate(this);" 
			      						onchange="validateDate(this);checkSigningDdaDate(this);"
			      						autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="trade_time" value="<%=trade_time%>" maxLength="5" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Trade Confirmation Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="signing_dt" value="<%=signing_dt%>" maxLength="10" 
			      						onblur="validateDate(this);checkSigningDdaDate(this);" 
			      						onchange="validateDate(this);checkSigningDdaDate(this);"
			      						autocomplete="off">
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
					</div>
					<div style="display:none">
					<div class="row m-b-5">
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
						<%if(!cont_status_flg.equals("F")){ %>
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
					<%} %>
					</div>
					</div>
					<div class="row m-b-5" <%if(!opration.equals("MODIFY")){ %>style="display:none;"<%} %>>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Security<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<select class="form-select form-select-sm" name="sec_category">
										<!-- <option value="">--Select--</option> -->
										<option value="R">Incoming</option>
										<option value="I">Outgoing</option>
									</select>
									<script>document.forms[0].sec_category.value="<%=sec_category_value%>"</script>
								</div>
				    			<div class="col">
				    				<input type="button" name="security_btn" class="btn btn-sm config_btn" value="Security Config" onclick="securityPreReceipt('<%=contract_type %>','<%=agmt_no%>','<%=agmt_rev_no%>','<%=cont_no%>','<%=cont_rev_no%>');" <%if(cont_status_flg.equals("X")){ %>disabled<%} %>>
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
					    					<input type="radio" class="form-check-input" name="chk_bu_plant" value="<%=VBU_PLANT_SEQ_NO.elementAt(i)%>" <%if(is_inv_gen.equals("Y")){ %>style="pointer-events:none;"<%} %>>&nbsp;<%=VBU_PLANT_ABBR.elementAt(i)%>&nbsp;&nbsp;
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
					    					<input type="radio" class="form-check-input" name="chk_plant" value="<%=VPLANT_SEQ_NO.elementAt(i)%>" <%if(is_inv_gen.equals("Y")){ %>style="pointer-events:none;"<%} %>>&nbsp;<%=VPLANT_ABBR.elementAt(i)%>
					    				<%}%>
					    				<input type="hidden" name="tmp_chk_plant" value="<%=strPlant%>">
				    				<%}else{ %>
				    					<%= utilmsg.warningMessage("Please configure Plants for Selected Trader!")%>
				    				<%} %>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Remark<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<font size="1" face="arial, helvetica, sans-serif">&nbsp;( You may enter up to 500 characters. )&nbsp;
										<input readonly type=text name="remLen" size="3" maxlength="3" class=""> characters left
									</font><br>
				      				<textarea class="form-control" name="remark" id="remark" cols="75" rows="1" onKeyDown="textCounter(this.form.remark,this.form.remLen,499);" onKeyUp="textCounter(this.form.remark,this.form.remLen,499);" oninput="handleComma(this,'Remark')"><%=remark%></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody" 
				<%if(opration.equals("MODIFY") && (!cont_no.equals("0") && !cont_no.equals(""))){ %><%}else{ %>style="display:none;"<%} %>>
				    <div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Trade Details</label>
					</div>					
				    <div class="row m-b-5">
				    	<div class="col-sm-6 col-xs-6 col-md-6">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>No. Of Instrument :</b></label>
								</div>
				    			<div class="col-auto">
			    					<input name="instrument_number" id="instrument_number" type="text" class="form-control form-control-sm" Readonly value="<%=no_of_instrument_dtl %>">
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-6 col-xs-6 col-md-6">
				    		<div class="d-flex justify-content-between"> 
				    			<div></div>
				    			<%if(!no_of_instrument_dtl.equals("")){ %>
				    			<%if(Integer.parseInt(no_of_instrument_dtl)<99){ %>
								<div class="btn-group" align="right">
									<label class="btn btn-outline-secondary subbtngrp1" data-bs-toggle="modal" data-bs-target="#AddNewInstrument" 
									onclick="changePayRecv();clearInstument('<%=show_instrument_number%>','<%=instrument_number%>','<%=currentYear%>');" 
									>Add New Instrument</label>
								</div>
								<%} %>
								<%} %>
							</div>
				    	</div>
				    </div>
				    <div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="cargo_tb">
								<thead>
									<tr>
										<th></th>
										<th>Instrument type</th>
										<th>Instrument#</th>
										<th>Status</th>
										<th>Buy/Sell</th>
										<th>Quantity</th>
										<th>Fixed Price</th>
										<th>Product</th>
										<th>Curve Name</th>
										<th>Projection Method</th>
										<th>Contract Month/Year</th>
										<th>Settlement Status</th>
									</tr>
								</thead>
								<tbody id="itemTab">
								<%if(VINSTRUMENT_NO.size()>0)
								{
									for (int i=0;i<VINSTRUMENT_NO.size();i++)
									{
										String criteria_value = (String) VCR_DR_CRITERIA.elementAt(i);
									%>
										<tr id="row">
											<td align="center">
												<font title="Click to Edit" <%if(VGEN_PDF_INV_INSTRUMENT_NO.elementAt(i).equals("") || (criteria_value.contains("QTY") || criteria_value.contains("FIXEDPRICE"))){ %>style="color:var(--header_color)"<%} %>>
												<i class="fa <%if(VGEN_PDF_INV_INSTRUMENT_NO.elementAt(i).equals("") || (criteria_value.contains("QTY") || criteria_value.contains("FIXEDPRICE"))){ %>fa-edit<%}else{ %>fa-eye<%} %> fa-lg" data-bs-toggle="modal" data-bs-target="#AddNewInstrument" 
												onclick="doModifyInstrument('<%=VINSTRUMENT_NO.elementAt(i)%>','<%= VDISP_INSTRUMENT_NO.elementAt(i)%>','<%= VINSTRUMENT_TYPE.elementAt(i)%>','<%= VINSTRUMENT_BUY_SELL.elementAt(i)%>',
													'<%=VINSTRUMENT_STATUS_FLG.elementAt(i)%>','<%= VINSTRUMENT_STATUS.elementAt(i)%>','<%= VINSTRUMENT_QTY.elementAt(i)%>','<%= VINSTRUMENT_QTY_UNIT.elementAt(i)%>','<%= VINSTRUMENT_RATE.elementAt(i)%>',
													'<%=VINSTRUMENT_RATE_UNIT.elementAt(i)%>','','<%= VPRICE_START_DT.elementAt(i)%>','<%= VPRICE_END_DT.elementAt(i)%>','<%= VPRODUCT_NM.elementAt(i)%>','<%= VCURVE_NM.elementAt(i)%>',
													'<%=VPROJ_METHOD.elementAt(i)%>','<%= VCONT_DD_MM_YR.elementAt(i)%>','<%= VCONVERSION_FACTOR.elementAt(i)%>','<%= VCONVERSION_FACTOR_UNIT.elementAt(i)%>',<%if(is_inv_gen.equals("Y")){ %>'<%=VGEN_INV_INSTRUMENT_NO.elementAt(i) %>'<%}else{ %>''<%} %>,
													'<%=VGEN_PDF_INV_INSTRUMENT_NO.elementAt(i) %>','<%=criteria_value %>');changePayRecv();">
												</i> 
												</font>
											</td>
											<td align="center"><%=VINSTRUMENT_TYPE.elementAt(i)%></td>
											<td align="center"><%=VDISP_INSTRUMENT_NO.elementAt(i)%>
												<input type="hidden" id="instru_no_<%=i%>" name="instru_no" value="<%=VINSTRUMENT_NO.elementAt(i) %>">
											</td>
											<td align="center">
											<font style="color:<%if(VINSTRUMENT_STATUS_FLG.elementAt(i).equals("Y")){%>#a6ff4d<%}else if(VINSTRUMENT_STATUS_FLG.elementAt(i).equals("N")){%>gold<%}else{%>red<%}%>">
												<i class="fa fa-circle fa-lg" ></i>
												&nbsp;
											</font>
											<%=VINSTRUMENT_STATUS.elementAt(i)%>
											</td>
											<td align="center"><%=VINSTRUMENT_BUY_SELL.elementAt(i)%></td>
											<td align="right"><%=VINSTRUMENT_QTY.elementAt(i)%></td>
											<td align="right"><%=VINSTRUMENT_RATE.elementAt(i)%> $/<%=VINSTRUMENT_QTY_UNIT.elementAt(i) %></td>
											<td align="center"><%=VPRODUCT_NM.elementAt(i)%></td>
											<td align="center"><%=VCURVE_NM.elementAt(i)%></td>
											<td align="center"><%=VPROJ_METHOD.elementAt(i)%></td>
											<td align="center"><%=VCONT_DD_MM_YR.elementAt(i).toString().substring(3)%></td>
											<td align="center"></td>
										</tr>
									<%} %>
								<%}else{ %>
								<tr>
									<td colspan="12" align="center"><%=utilmsg.infoMessage("<b>No Instrument Data Available!</b>")%></td>
								</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Governing Clauses</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="billing_flag" id="billing_flag" value="Y" onchange="billingShows()" <%if(bill_flag.equals("Y")){ %>checked<%}else{ %>checked<%} %>>&nbsp;Billing</b></label>
				  				</div>
				    			<div id="billing_flag_div" class="col">
				    				<input type="text" class="form-control form-control-sm" name="billing_clause" value="<%=billing_clause%>" placeholder="Clause#" maxLength="10" >
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div id="billing_flag_div1" class="col-sm-12 col-xs-12 col-md-12">
						      		<input type="button" class="btn btn-sm config_btn" value="Configure Billing" <%if(opration.equals("MODIFY")){ %>onclick="openBillingDtl();"<%} %> <%if(cont_status_flg.equals("X")){ %>disabled<%} %>>
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y") && !cont_status_flg.equals("X")){ %>
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
					    	Derivative - Change Request(s)
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
								<%-- <div class="col-auto">
									<input type="button" class="btn btn-sm request_btn" value="Price Change Request" onclick="doPriceChangeRequest();"
									<%//if(!fcc_flg.equals("Y") || cont_status_flg.equals("X")||cont_status_flg.equals("C")){%>disabled<%//}else if(!is_allocated.equals("Y")){%>disabled<%//}%>>
								</div> --%>				    			
				    			<%-- <div class="col-auto">
				    				<input type="button" class="btn btn-sm request_btn" value="Closure Request" onclick="doClosureRequest();" 
				    				<%//if((opration.equals("INSERT") || (cont_no.equals("0") || cont_no.equals(""))) || !is_allocated.equals("Y")|| closure_request_flag.equals("Y")||cont_status_flg.equals("C")){%>
				    				style="background:#e6e6e6;color:black;"  <%//} %>>
				    			</div> --%>
				    			<div class="col-auto" title="<%if(is_inv_gen.equals("Y") && !cont_status_flg.equals("X")){%>Can't cancel the contract if Invoice Generated for any Instrument!<%}%>">
				    				<%if(write_access.equals("Y")){ %>
									<input type="button" class="btn btn-sm request_btn" value="Cancel Contract" data-bs-toggle="modal" data-bs-target="#CancelModal" <%if(opration.equals("MODIFY") && !cont_status_flg.equals("X")){if((is_inv_gen.equals("Y") || cont_no.equals("0") || cont_no.equals(""))){ %>style="background:#e6e6e6;color:black;" disabled<%}}else{%>style="background:#e6e6e6;color:black;" disabled<%} %>>
									<%}else{ %>
									<input type="button" class="btn btn-sm request_btn" value="Cancel Contract" disabled>
									<%} %>
				    			</div>
				    			<%-- <div class="col-auto">
				    				<input type="button" class="btn btn-sm request_btn" value="Re-Open Request" onclick = "doReopenRequest();"  <%if((opration.equals("INSERT") || !cont_status_flg.equals("C"))||closure_request_flag.equals("O")){%> disabled <%} %>>
				    			</div> --%>
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
									<%=utilmsg.errorMessage("<b>Billing Detail is not Configured!</b>")%>
								</div>
							</div>
						<%} %>
						<%if((no_of_security_dtl.equals("0") || no_of_security_dtl.equals(""))){ %>
							<div class="row m-b-5">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.errorMessage("<b>Security Detail is not Configured!</b>")%>
								</div>
							</div>
						<%} %>
						<%if((no_of_instrument_dtl.equals("0") || no_of_instrument_dtl.equals(""))){ %>
							<div class="row m-b-5">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.errorMessage("<b>Instrument Detail is not Configured!</b>")%>
								</div>
							</div>
						<%} %>
					<%} %>
					<%if(cont_status_flg.equals("X")){%>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<%=utilmsg.infoMessage("<b>This Contract has been Canceled!</b>")%>
							</div>
						</div>		
					<%}%>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="AddNewInstrument" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
        			Add New Instrument
        		</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" onclick="">
        	</div>
        	<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Instrument#</b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12" >
				    				<input type=text class="form-control form-control-sm" name="show_instrument_number" id="show_instrument_number" value="<%=show_instrument_number %>" readonly>
		      						<input type="hidden" class="form-control form-control-sm" name="instrument_no" id="instrument_no" value="<%=instrument_number %>" readonly>
		      						<input type="hidden" class="form-control form-control-sm" name="inv_instrument_no" id="inv_instrument_no" value="" readonly>
		      						<input type="hidden" class="form-control form-control-sm" name="inv_cr_dr_gen" id="inv_cr_dr_gen" value="" readonly>
	      						</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Instrument type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12" >
									<select class="form-select form-select-sm" name="instrument_type">
								   	 	 <option id="fixed_float_swap" value="Fixed/Float Swap" selected="selected">Fixed/Float Swap</option>
								   	</select>
							   	</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Buy/Sell<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12" >
									<select class="form-select form-select-sm" name="buy_sell" onchange="changePayRecv();">
								   	 	 <option value="BUY" selected="selected">Buy</option>
								   	 	 <option value="SELL">Sell</option>
								   	</select>
							   	</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Product<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12" >
									<select class="form-select form-select-sm" name="product_nm" onchange="fetchCurveAndProjMethod();">
								   	 	 <option value="">--Select--</option>
								   	 	 <%for(int i=0;i<VPROD_TYPE.size();i++){ %>
								   	 	 <option value="<%=VPROD_TYPE.elementAt(i)%>"><%=VPROD_TYPE.elementAt(i)%></option>
								   	 	 <%} %>
								   	</select>
							   	</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b id="lbl_qty">Quantity()<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm" name="qty" id="qty" value="" onblur="checkValuePrecision(this.value,'2','10','qty');negNumber(this);">
		      						<input type="hidden" name="qty_unit" id="qty_unit" value="">
	      						</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Status<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12" >
									<select class="form-select form-select-sm" name="inst_status_flag">
								   	 	 <option value="Y">Confirmed</option>
								   	 	 <option value="X">Cancel</option>
								   	</select>
							   	</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Conversion Factor<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-6 col-xs-6 col-md-6">
				    				<input name="conv_factor" id="conv_factor" type="text" class="form-control form-control-sm" value="">
				  				</div>
								<div class="col-sm-6 col-xs-6 col-md-6">
									<input name="conv_factor_unit" id="conv_factor_unit" type="text" class="form-control form-control-sm" value="" readonly>
									<!-- <select class="form-select form-select-sm" name="conv_factor_unit">
										<option value="">--Select--</option>
										<option value="MTM" selected="selected">MMBTU to MMBTU</option>
										<option value="BTM">BBL to MMBTU</option>
									</select> -->
								</div>
				  			</div>
						</div>
					</div>
					&nbsp;
					<div class="row m-b-5">
						<div class="col-sm-6 col-xs-6 col-md-6">
							<div class="row" >
								<label  id="fixed_lable" class="form-label subheader"><i class="fa fa-snowflake-o"></i> Fixed Side</label>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-4 col-xs-4 col-md-4">
									<div class="form-group row">
						    			<label class="form-label"><b>Fixed Price<span class="s-red">*</span></b></label>
						  			</div>
								</div>
								<div class="col-sm-8 col-xs-8 col-md-8">
									<div class="form-group row">
						    			<div class="col-sm-6 col-xs-6 col-md-6">
						      				<input type="text" class="form-control form-control-sm"  name="rate" id="rate"  value="" onblur="checkValuePrecision(this.value,'4','2','rate');negNumber(this);">
						    			</div>
						    			<div class="col-sm-6 col-xs-6 col-md-6">
						      				<select class="form-select form-select-sm" name="disp_rate_unit" id="disp_rate_unit" onchange="checkRateFormate(this);checkInrUsd();" style="pointer-events: none;">							   	 	 
										   	</select>
										 	<input name="rate_unit" id="rate_unit" type="hidden" value="">
										</div>
									</div>
						  		</div>
						  	</div>
						</div>
						<div class="col-sm-6 col-xs-6 col-md-6">
							<div class="row" >
								<label id="float_lable" class="form-label subheader"><i class="fa fa-snowflake-o"></i> Float Side</label>
							</div>
							<div class="row m-b-5">
						  		<div class="col-sm-4 col-xs-4 col-md-4"> 
									<div class="form-group row">
						    			<label class="form-label"><b>Curve Name<span class="s-red">*</span></b></label>
						  			</div>
								</div>
								<div class="col-sm-8 col-xs-8 col-md-8">
									<div class="form-group row">
										<div class="col-sm-12 col-xs-12 col-md-12" >
											<select class="form-select form-select-sm" name="curve_nm" style="pointer-events: none; background-color: #eee;">
										   	 	 <option  value="">--Select--</option>
										   	 	 <%for(int i=0;i<VCURVE_TYPE.size();i++){ %>
										   	 	 <option value="<%=VCURVE_TYPE.elementAt(i)%>"><%=VCURVE_TYPE.elementAt(i)%></option>
										   	 	 <%} %>
										   	</select>
									   	</div>
						  			</div>
								</div>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-4 col-xs-4 col-md-4">  
									<div class="form-group row">
						    			<label class="form-label"><b>Projection Method<span class="s-red">*</span></b></label>
						  			</div>
								</div>
								<div class="col-sm-8 col-xs-8 col-md-8">
									<div class="form-group row">
										<div class="col-sm-12 col-xs-12 col-md-12" >
											<select class="form-select form-select-sm" name="proj_method" style="pointer-events: none; background-color: #eee;">
										   	 	 <option value="">--Select--</option>
										   	</select>
									   	</div>
						  			</div>
								</div>
		      				</div>
		      				<div class="row m-b-5">
								<div class="col-sm-4 col-xs-4 col-md-4">  
									<div class="form-group row">
						    			<label class="form-label"><b>Contract Month/Year<span class="s-red">*</span></b></label>
						  			</div>
								</div>
								<div class="col-sm-3 col-xs-3 col-md-3">
									<div class="form-group row">
							  			<div class="col">
							  				<select class="form-select form-select-sm" name="cont_month" onchange="fetchCurveAndProjMethod();">
												<option value="00" label="--Select--">--Select--</option>
												<option value="01" label="January">January</option>
												<option value="02" label="February">February</option>
												<option value="03" label="March">March</option>
												<option value="04" label="April">April</option>
												<option value="05" label="May">May</option>
												<option value="06" label="June">June</option>
												<option value="07" label="July">July</option>
												<option value="08" label="August">August</option>
												<option value="09" label="September">September</option>
												<option value="10" label="October">October</option>
												<option value="11" label="November">November</option>
												<option value="12" label="December">December</option>
											</select>
											<script>document.forms[0].cont_month.value="01"</script>
										</div>
									</div>
								</div>
								<div class="col-auto">
									<div class="form-group row">
										<label class="form-label"><b>Year</b></label>
							  		</div>
								</div>
								<div class="col-sm-4 col-xs-4 col-md-4">
									<div class="form-group row">
										<div class="col">
							  				<select class="form-select form-select-sm" name="cont_year" onchange="fetchCurveAndProjMethod();">
							  					<%for(int i=(currentYear+1); i > (currentYear-10);i--){ %>
													<option value="<%=i%>"><%=i%></option>
												<%} %>
											</select>
											<script>document.forms[0].cont_year.value="<%=currentYear%>"</script>
										</div>
									</div>
								</div>
		      				</div>
		      				<div class="row m-b-5">
								<div class="col-sm-4 col-xs-4 col-md-4"> 
									<div class="form-group row">
						    			<label class="form-label"><b>Pricing Start Date<span class="s-red">*</span></b></label>
						  			</div>
								</div>
								<div class="col-sm-8 col-xs-8 col-md-8">
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						      				<div class="input-group input-group-sm" >
					      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="price_start_dt" value="" maxLength="10"
					      						onchange="validateDate(this);checkPeriodStartdate();"   autocomplete="off">
					      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      						</div>
						    			</div>
						    			<input type="hidden" name="temp_start_dt" value="">
						    			<input type="hidden" name="agmt_start_dt" value="<%=agmt_start_dt%>">
						    			<input type="hidden" name="temp_price_start_dt" value="">
						  			</div>
								</div>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-4 col-xs-4 col-md-4">
									<label class="form-label"><b>Pricing End Date<span class="s-red">*</span></b></label>
								</div>
								<div class="col-sm-8 col-xs-8 col-md-8">
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						      				<div class="input-group input-group-sm" >
					      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="price_end_dt" value="<%//=price_end_dt%>" maxLength="10" 
					      						onchange="validateDate(this);checkPeriodEnddate();" autocomplete="off">
					      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      						</div>
						    			</div>
						    			<input type="hidden" name="temp_end_dt" value="<%//=price_end_dt%>">
						    			<input type="hidden" name="agmt_end_dt" value="<%=agmt_end_dt%>">
						    			<input type="hidden" name="temp_price_end_dt" value="">
						  			</div>
								</div>
							</div>
						</div>
      				</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div align="right">
					<input type="button" class="btn btn-warning com-btn" name="submit_btn" value="Submit" onclick="doSubmitInstru();" <%if(cont_status_flg.equals("X")){ %>disabled<%} %>>
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
					Contract [<%=cont_disp_name%>] Cancellation
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
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="cancelContract();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
				</div>
	      	</div>
		</div>
	</div>
</div>
<!-- /Cancel Modal -->

<input type="hidden" name="option" value="HEDGE_CONT_MST">
<input type="hidden" name="old_value" value="<%=old_value%>">

<input type="hidden"  name="no_of_cargo" value="">

<input type="hidden" name="prev_clearance" value="<%=clearance%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="existing_instrument_no" value="">
<input type="hidden" name="cont_status_flg" value="<%=cont_status_flg%>">
<input type="hidden" name="agreement_type" value="<%=agreement_type%>" >
<input type="hidden" name="change_request" value="">

<input type="hidden" name="no_of_billing_dtl" value="<%=no_of_billing_dtl%>">
<input type="hidden" name="no_of_security_dtl" value="<%=no_of_security_dtl%>">
<input type="hidden" name="no_of_instrument_dtl" value="<%=no_of_instrument_dtl%>">
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
<input type="hidden" name="sysdate" value="<%=sysdate%>">

<script >
function fetchCurveAndProjMethod()
{
	var product_nm = document.forms[0].product_nm.value;
	var cont_month = document.forms[0].cont_month.value;
	var cont_year = document.forms[0].cont_year.value;
	
	if((product_nm !="" && product_nm!="0"))
	{
		$.post("../servlet/DB_Derivative_Ajax"+ "?product_nm="+product_nm+"&cont_month="+cont_month+"&cont_year="+cont_year+"&setCallType=fetchCurveAndProjMethod", function(responseJson) {
			
			//console.log(responseJson);
			
			$.each(responseJson, function(index, json) {
				$.each(json.CURVEANDPROJM_DTL, function(index_1, json_1) {
					if(parseInt(json.CURVEANDPROJM_DTL.length) >0 )
					{
						document.forms[0].curve_nm.value=json_1.CURVE_TYPE;
						
						// Create a new option
						const newOption = document.createElement("option");
						
						newOption.value = json_1.PROJMETHOD;
						newOption.text = json_1.PROJMETHOD;

						// Add the new option to the select element
						document.forms[0].proj_method.appendChild(newOption);
						
						document.forms[0].proj_method.value=json_1.PROJMETHOD;
						
						document.forms[0].price_start_dt.value=json_1.PRICE_START_DT;
						document.forms[0].temp_price_start_dt.value=json_1.PRICE_START_DT;
						document.forms[0].price_end_dt.value=json_1.PRICE_END_DT;
						document.forms[0].temp_price_end_dt.value=json_1.PRICE_END_DT;
						
						$("input[name='price_start_dt']").datepicker("update", json_1.PRICE_START_DT);
						$("input[name='price_end_dt']").datepicker("update", json_1.PRICE_END_DT);
						
						var lbl_qty = document.getElementById('lbl_qty');
						
						var splitCurveUnit = json_1.CURVE_UNIT.split("/");
						
						var qtyUnit = splitCurveUnit[1];
						
						if(qtyUnit!=null)
						{
							lbl_qty.innerHTML="Quantity ("+qtyUnit+")<span class='s-red'>*</span>";
							document.forms[0].qty_unit.value=qtyUnit;
							
							if(qtyUnit == "MMBTU")
							{
								document.forms[0].conv_factor.value="1";
								document.forms[0].conv_factor.readOnly = true;
							}
							else
							{
								if(cont_month == "00")
								{
									document.forms[0].conv_factor.value="";
									document.forms[0].conv_factor.readOnly = false;
								}
							}
							
							document.forms[0].conv_factor_unit.value=qtyUnit+" to MMBTU";
							//document.forms[0].conv_factor_unit.style.backgroundColor  = "#eee";
							document.forms[0].conv_factor_unit.readOnly = true;
	
							document.forms[0].rate_unit.value="2";//By Default 2 is taken as rate will be in $ only.
							
							var disp_rate_unit = document.forms[0].disp_rate_unit;
							
							const selectElement = document.getElementById('disp_rate_unit');
	
							// Remove all options by iterating over them
							while (selectElement.options.length > 0)
							{
							    selectElement.remove(0);
							}
							
							const newOption1 = document.createElement("option");
	
							newOption1.text = "$/"+qtyUnit;
							newOption1.value = "$/"+qtyUnit;
	
							document.getElementById("disp_rate_unit").appendChild(newOption1);
						}
						else
						{
							alert("Product Quantity unit is Not Valid !");
							
							lbl_qty.innerHTML="Quantity ()<span class='s-red'>*</span>";
							document.forms[0].qty_unit.value="";
							
							document.forms[0].conv_factor.value="";
							document.forms[0].conv_factor.readOnly = true;
							
							document.forms[0].conv_factor_unit.value="";
							//document.forms[0].conv_factor_unit.style.backgroundColor  = "#eee";
							document.forms[0].conv_factor_unit.readOnly = true;
	
							document.forms[0].rate_unit.value="";//By Default 2 is taken as rate will be in $ only.
							
							var disp_rate_unit = document.forms[0].disp_rate_unit;
							
							const selectElement = document.getElementById('disp_rate_unit');
	
							// Remove all options by iterating over them
							while (selectElement.options.length > 0)
							{
							    selectElement.remove(0);
							}
							
							const newOption1 = document.createElement("option");
	
							newOption1.text = "$/"+qtyUnit;
							newOption1.value = "$/"+qtyUnit;
	
						}
					}
				});
			});
		});
	}
}
</script>
</form>
</body>
</html>