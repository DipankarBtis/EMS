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
	
	var url = "frm_confirm_notice_mst_fcc.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+"&u="+u;

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
	
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	var no_of_security_dtl = document.forms[0].no_of_security_dtl.value;
	var no_of_cargo_dtl = document.forms[0].no_of_cargo_dtl.value;
	var no_of_liability_dtl = document.forms[0].no_of_liability_dtl.value;
	
	//var agmt_base = document.forms[0].agmt_base.value;
	var agreement_type = document.forms[0].agreement_type;
	var agreement_base = document.forms[0].agreement_base;
	var transportation_charges = document.forms[0].transportation_charges.value
	
	
	var chk_plant = document.forms[0].chk_plant;
	var chk_oth_plant = document.forms[0].chk_oth_plant;
	var chk_trans = document.forms[0].chk_trans;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var agmt_no = document.forms[0].agmt_no.value;
	
	var msg="";
	var flag=true;
	
	if(counterparty_cd == "" || counterparty_cd == "0")
	{
		msg+="Select Counterparty!\n";
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
	
	/* if(agreement_base[1].checked == true)
	{
		if(trim(transportation_charges) == "")
		{
			msg+="Enter Transportation Charges!\n";
			flag=false;
		}
	} */
	
	
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
		
		if(fcc_flg=="Y")
		{
			a = confirm("Do you want to Approve Confirmation Notice?");
		}
		else
		{
			a = confirm("Do you want to Disapprove Confirmation Notice?");
		}
		if(a)
		{
			document.forms[0].fcc_flg.value=fcc_flg
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].option.value="CN_CONT_FCC"
			document.forms[0].submit();
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

var newWindow;
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_cn_contract_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&fcc_flag=Y","CN Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_cn_contract_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&fcc_flag=Y","CN Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
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
	var rate_unit = document.forms[0].demmurage_rate_unit.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_cn_billing_dtl_fcc.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
			"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+
			"&u="+u+"&rate_unit="+rate_unit;
	
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

function setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,agmt_base)
{
	var opration = document.forms[0].opration.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_confirm_notice_mst_fcc.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&agmt_base="+agmt_base+
			"&u="+u;

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

function showHide(flg)
{
	/* if(flg == "D")
	{
		document.getElementById("dlvTranChg").style.display="flex";
		document.getElementById("linkTrans").style.display="flex";
	}
	else
	{
		document.getElementById("dlvTranChg").style.display="none";
		document.getElementById("linkTrans").style.display="none";
		document.forms[0].transportation_charges.value="";
	} */
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
		var url = "../credit_risk/frm_pre_receipt_security_fcc.jsp?counterparty_cd="+counterparty_cd+"&cont_no="+cont+"&cont_rev_no="+cont_rev+
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
function variablePriceConfig(cont_type,agmt,agmt_rev,cont,cont_rev,cont_ref,cont_status,start_dt,end_dt,cargo_no)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	//var temp_start_dt = document.forms[0].temp_start_dt.value;
	//var temp_end_dt = document.forms[0].temp_end_dt.value;
	
	var u = document.forms[0].u.value;
	
	if(trim(cont_type)!="" && (trim(cont)!="" && cont!="0") && (trim(counterparty_cd)!="" && counterparty_cd!="0"))
	{
		var url = "../market_risk/frm_config_price_mst_fcc.jsp?counterparty_cd="+counterparty_cd+"&cont_no="+cont+"&cont_rev_no="+cont_rev+"&cont_ref="+cont_ref+
			"&agmt_no="+agmt+"&agmt_rev_no="+agmt_rev+"&contract_type="+cont_type+"&start_dt="+start_dt+"&end_dt="+end_dt+"&cont_status="+cont_status+"&cargo_no="+cargo_no+
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

function demurrageShows()
{
	const checkbox = document.getElementById("demmurageCheckbox");
	const div_std = document.getElementById("demurrage_div_std");
	const div_std1 = document.getElementById("demurrage_clause_div");

	if (checkbox.checked) 
	{
		div_std.style.display = "block";
		div_std1.style.display = "block";
		document.getElementById("demmurageCheckbox").value="Y";
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

function doSubmitcargo()
{
	
	document.forms[0].option.value = "CN_CARGO_DTL";
	document.forms[0].opration.value = "MODIFY";
	var cargo_ref = document.forms[0].cargo_ref.value;
	var agreement_base = document.forms[0].agreement_base;
	var cargo_agmt_base = document.forms[0].cargo_agmt_base;
	var from_dt = document.forms[0].cargo_from_dt.value;
	var to_dt = document.forms[0].cargo_to_dt.value;
	var volume = document.forms[0].cargo_volume.value;
	var price = document.forms[0].cargo_price.value;
	var rate_unit = document.forms[0].cargo_rate_unit.value;
	var msg="";
	var flag=true;
	
	if(trim(cargo_ref)=="" || cargo_ref==0)
	{
		msg+="Please Enter Cargo Ref!\n";
		flag=false;
	}
	
	if(trim(from_dt)=="" || from_dt==0)
	{
		msg+="Please Enter From Date!\n";
		flag=false;
	}
	if(trim(to_dt)=="" || to_dt==0)
	{
		msg+="Please Enter To Date!\n";
		flag=false;
	}
	if(trim(volume)=="" || volume==0)
	{
		msg+="Please Enter Volume(MMBTU)!\n";
		flag=false;
	}
	if(trim(price)=="" || price==0)
	{
		msg+="Please Enter Price!\n";
		flag=false;
	}
	if(trim(rate_unit)=="" || rate_unit==0)
	{
		msg+="Please Select Rate Unit!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Insert the Cargo Details?");
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
	var agreement_type = "M";
	if(counterparty_cd=="0" || counterparty_cd=="")
	{
		alert("Select Counterparty!");
	}
	else
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open("frm_cn_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_cn_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function doModifyCargo(VDISP_CARGO_NO,VCARGO_NO,VCARGO_REF,VCARGO_STATUS_FLG,VCARGO_START_DT,VCARGO_END_DT,VCARGO_QTY,VCARGO_PRICE,VCARGO_PRICE_UNIT_FLG,VCARGO_AGMT_BASE)
{
	document.forms[0].show_cargo_number.value = VDISP_CARGO_NO;
	document.forms[0].cargo_no.value = VCARGO_NO;
	document.forms[0].cargo_ref.value = VCARGO_REF;
	document.forms[0].cargo_agmt_base.value = VCARGO_AGMT_BASE;
	document.forms[0].cargo_from_dt.value = VCARGO_START_DT;
	document.forms[0].cargo_to_dt.value = VCARGO_END_DT;
	document.forms[0].cargo_volume.value = VCARGO_QTY;
	document.forms[0].cargo_price.value = VCARGO_PRICE;
	document.forms[0].cargo_rate_unit.value = VCARGO_PRICE_UNIT_FLG;
	document.forms[0].cargo_status_flag.value = VCARGO_STATUS_FLG;
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
	var agmt_type = document.forms[0].agmt_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_cn_liability_clause_fcc.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agmt_type+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+"&contract_type="+contract_type+
			"&end_dt="+end_dt+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&u="+u;
	
	
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
	var splitSt_dt = start_dt.split("/");
	var splitAgmtSt_dt = agmt_start_dt.split("/");
	
	var temp_st_dt = splitSt_dt[2]+splitSt_dt[1]+splitSt_dt[0];
	var temp_agmt_st_dt = splitAgmtSt_dt[2]+splitAgmtSt_dt[1]+splitAgmtSt_dt[0];
	
	if(temp_st_dt!="")
	{
		if(temp_st_dt < temp_agmt_st_dt)
		{
			alert("Contract Start date should be gretar than or equals to Agreement Start date !");
			document.forms[0].start_dt.value="";
		}
	}
}

function checkAgmtEnddate()
{
	var end_dt = document.forms[0].end_dt.value;
	var agmt_end_dt = document.forms[0].agmt_end_dt.value;
	
	var splitEnd_dt = end_dt.split("/");
	var splitAgmt_end_dt = agmt_end_dt.split("/");
	
	var temp_end_dt = splitEnd_dt[2]+splitEnd_dt[1]+splitEnd_dt[0];
	var temp_agmt_end_dt = splitAgmt_end_dt[2]+splitAgmt_end_dt[1]+splitAgmt_end_dt[0];
	
	if(temp_end_dt!="")
	{
		if(temp_end_dt > temp_agmt_end_dt)
		{
			alert("Contract End date should be less than or equals to Agreement End date !");
			document.forms[0].end_dt.value="";
		}
	}
}

function checkSigningDdaDate()
{
	var signing_dt = document.forms[0].signing_dt.value;
	var agmt_signing_dt = document.forms[0].agmt_signing_dt.value;
	var dda_dt = document.forms[0].dda_dt.value;
	
	var splitSign_dt = signing_dt.split("/");
	var splitAgmtSign_dt = agmt_signing_dt.split("/");
	var splitDda_dt = dda_dt.split("/");
	
	var tmp_sig_dt = splitSign_dt[2]+splitSign_dt[1]+splitSign_dt[0];
	var tmp_agmt_sig_dt = splitAgmtSign_dt[2]+splitAgmtSign_dt[1]+splitAgmtSign_dt[0];
	var tmp_dda_dt = splitDda_dt[2]+splitDda_dt[1]+splitDda_dt[0];
	
	if(tmp_sig_dt!="")
	{
		if(tmp_sig_dt < tmp_agmt_sig_dt)
		{
			alert("Contract Signing date should be Greater than or equals to Agreement Signing date !");
			document.forms[0].signing_dt.value="";
		}
	}
	
	if(tmp_dda_dt!="")
	{
		if(tmp_dda_dt < tmp_agmt_sig_dt)
		{
			alert("Contract DDA date should be Greater than or equals to Agreement Signing date !");
			document.forms[0].dda_dt.value="";
		}
	}
}

function checkCargoFromDt()
{
	var start_dt = document.forms[0].start_dt.value;
	var cargo_from_dt = document.forms[0].cargo_from_dt.value;
	
	//alert(cargo_from_dt+"....."+start_dt);
	//alert(cargo_to_dt+"....."+end_dt);
	var splitSt_dt = start_dt.split("/");
	var splitCargoFrom_dt = cargo_from_dt.split("/");
	var temp_st_dt = splitSt_dt[2]+splitSt_dt[1]+splitSt_dt[0];
	var temp_cargo_from_dt = splitCargoFrom_dt[2]+splitCargoFrom_dt[1]+splitCargoFrom_dt[0];
	
	if(trim(temp_cargo_from_dt)=="" || temp_cargo_from_dt==null)
	{
	}
	else
	{
		if(temp_cargo_from_dt < temp_st_dt)
		{
			alert("Cargo From date should be Greater than or equals to CN Start date !");
			document.forms[0].cargo_from_dt.value="";
		}
	}
}

function checkCargoEndDt()
{
	var end_dt = document.forms[0].end_dt.value;
	var cargo_to_dt = document.forms[0].cargo_to_dt.value;
	
	if(trim(cargo_to_dt)!="")
	{	
		var splitEnd_dt = end_dt.split("/");
		var splitCargoTo_dt = cargo_to_dt.split("/");
		var temp_end_dt = splitEnd_dt[2]+splitEnd_dt[1]+splitEnd_dt[0];
		var temp_cargo_to_dt = splitCargoTo_dt[2]+splitCargoTo_dt[1]+splitCargoTo_dt[0];
		
		if(trim(temp_cargo_to_dt) == "" || temp_cargo_to_dt == null)
		{
		}
		else
		{
			if(temp_cargo_to_dt > temp_end_dt)
			{
				alert("Cargo To date should be Less than or equals to CN End date !");
				document.forms[0].cargo_to_dt.value="";
			}
		}
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
</script>
</head>
<jsp:useBean class="com.etrm.fms.cargo.DataBean_Cargo_mst" id="cargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String dateTime = utildate.getSysdateWithTime24hr();
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"MODIFY":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String clearance = request.getParameter("clearance")==null?"KYC":request.getParameter("clearance");

String agmt_nm = request.getParameter("agmt_nm")==null?"":request.getParameter("agmt_nm");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
String agreement_base=request.getParameter("agmt_base")==null?"":request.getParameter("agmt_base");
String contract_type=request.getParameter("contract_type")==null?"N":request.getParameter("contract_type");
String agreement_type = request.getParameter("agmt_type")==null?"M":request.getParameter("agmt_type"); 


cargo.setCallFlag("CONFIRM_NOTICE_MST");
cargo.setClearance(clearance);
cargo.setCounterparty_cd(counterparty_cd);
cargo.setComp_cd(owner_cd);
cargo.setOpration(opration);
cargo.setAgmt_no(agmt_no);
cargo.setAgmt_rev_no(agmt_rev_no);
cargo.setCont_no(cont_no);
cargo.setCont_rev_no(cont_rev_no);
cargo.setContract_type(contract_type);
cargo.setCargo_no(cargo_no);
cargo.setAgreement_type(agreement_type);
cargo.init();

Vector VCOUNTERPARTY_CD = cargo.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = cargo.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = cargo.getVCOUNTERPARTY_ABBR();
Vector VOTH_COUNTERPARTY_CD = cargo.getVOTH_COUNTERPARTY_CD();
Vector VOTH_COUNTERPARTY_NM = cargo.getVOTH_COUNTERPARTY_NM();
Vector VOTH_COUNTERPARTY_ABBR = cargo.getVOTH_COUNTERPARTY_ABBR();

Vector VPLANT_NM = cargo.getVPLANT_NM();
Vector VPLANT_ABBR = cargo.getVPLANT_ABBR();
Vector VPLANT_SEQ_NO = cargo.getVPLANT_SEQ_NO();
Vector VOTH_PLANT_NM = cargo.getVOTH_PLANT_NM();
Vector VOTH_PLANT_ABBR = cargo.getVOTH_PLANT_ABBR();
Vector VOTH_PLANT_SEQ_NO = cargo.getVOTH_PLANT_SEQ_NO();
Vector VTRANS_CD = cargo.getVTRANS_CD();
Vector VTRANS_PLANT_NM = cargo.getVTRANS_PLANT_NM();
Vector VTRANS_PLANT_ABBR = cargo.getVTRANS_PLANT_ABBR();
Vector VTRANS_PLANT_SEQ_NO = cargo.getVTRANS_PLANT_SEQ_NO();
Vector VBU_CD = cargo.getVBU_CD();
Vector VBU_PLANT_NM = cargo.getVBU_PLANT_NM();
Vector VBU_PLANT_ABBR = cargo.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = cargo.getVBU_PLANT_SEQ_NO();

Vector VGX_BU_CD = cargo.getVGX_BU_CD();
Vector VGX_BU_PLANT_NM = cargo.getVGX_BU_PLANT_NM();
Vector VGX_BU_PLANT_ABBR = cargo.getVGX_BU_PLANT_ABBR();
Vector VGX_BU_PLANT_SEQ_NO = cargo.getVGX_BU_PLANT_SEQ_NO();

Vector VSEL_GX_BU_CD = cargo.getVSEL_GX_BU_CD();
Vector VSEL_GX_BU_PLANT_SEQ_NO = cargo.getVSEL_GX_BU_PLANT_SEQ_NO();

Vector VSEL_PLANT_SEQ_NO = cargo.getVSEL_PLANT_SEQ_NO();
Vector VSEL_TRANS_CD = cargo.getVSEL_TRANS_CD();
Vector VSEL_TRANS_PLANT_SEQ_NO = cargo.getVSEL_TRANS_PLANT_SEQ_NO();
Vector VSEL_SPLIT_VALUE = cargo.getVSEL_SPLIT_VALUE();
Vector VSEL_BU_CD = cargo.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = cargo.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_TRAD_CD = cargo.getVSEL_TRAD_CD();
Vector VSEL_PLANT_ABBR = cargo.getVSEL_PLANT_ABBR();
Vector VSEL_TRANS_PLANT_ABBR = cargo.getVSEL_TRANS_PLANT_ABBR();
Vector VSEL_BU_PLANT_ABBR = cargo.getVSEL_BU_PLANT_ABBR();

Vector VOTH_SEL_TRAD_CD = cargo.getVOTH_SEL_TRAD_CD();
Vector VOTH_SEL_PLANT_SEQ_NO = cargo.getVOTH_SEL_PLANT_SEQ_NO();
Vector VOTH_SEL_PLANT_ABBR = cargo.getVOTH_SEL_PLANT_ABBR();
Vector VOTH_SEL_SPLIT_VALUE = cargo.getVOTH_SEL_SPLIT_VALUE();

Vector VTEMP_TRANS_CD = cargo.getVTEMP_TRANS_CD();
Vector VTEMP_TRANS_ABBR = cargo.getVTEMP_TRANS_ABBR();

Vector VFORMULA_ID = cargo.getVFORMULA_ID();
Vector VFORMULA_NM = cargo.getVFORMULA_NM();

Vector VCARGO_NO = cargo.getVCARGO_NO();
Vector VCARGO_REF = cargo.getVCARGO_REF();
Vector VCARGO_STATUS = cargo.getVCARGO_STATUS();
Vector VCARGO_STATUS_FLG = cargo.getVCARGO_STATUS_FLG();
Vector VCARGO_QTY = cargo.getVCARGO_QTY();
Vector VCARGO_PRICE = cargo.getVCARGO_PRICE();
Vector VCARGO_PRICE_UNIT = cargo.getVCARGO_PRICE_UNIT();
Vector VCARGO_PRICE_UNIT_FLG = cargo.getVCARGO_PRICE_UNIT_FLG();
Vector VCARGO_START_DT = cargo.getVCARGO_START_DT();
Vector VCARGO_END_DT = cargo.getVCARGO_END_DT();
Vector VCARGO_AGMT_BASE = cargo.getVCARGO_AGMT_BASE();
Vector VDISP_CARGO_NO = cargo.getVDISP_CARGO_NO();
Vector VCARGO_TOLERANCE_PER = cargo.getVCARGO_TOLERANCE_PER();

Vector VINDEX = cargo.getVINDEX();
String min_counterparty_eff_dt = cargo.getMin_counterparty_eff_dt();
String signing_dt = cargo.getSigning_dt();
String signing_time = cargo.getSigning_time();
String agmt_signing_dt = cargo.getAgmt_signing_dt();
String ent_dt = cargo.getEnt_dt();
String ent_time = cargo.getEnt_time();
String start_dt = cargo.getStart_dt();
String end_dt = cargo.getEnd_dt();
String agmt_start_dt = cargo.getAgmt_start_dt();
String agmt_end_dt = cargo.getAgmt_end_dt();
String agmt_base = cargo.getAgmt_base();
String agmt_type = cargo.getAgmt_type();
String agmt_typ = cargo.getAgmt_typ();
String buy_nom_flag = cargo.getBuy_nom_flag();
String buy_month_nom = cargo.getBuy_month_nom();
String buy_fortnightly_nom = cargo.getBuy_fortnightly_nom();
String buy_week_nom = cargo.getBuy_week_nom();
String buy_daily_nom = cargo.getBuy_daily_nom();
String sell_nom_flag = cargo.getSell_nom_flag();
String sell_month_nom = cargo.getSell_month_nom();
String sell_fortnightly_nom = cargo.getSell_fortnightly_nom();
String sell_week_nom = cargo.getSell_week_nom();
String sell_daily_nom = cargo.getSell_daily_nom();
String day_def_flag = cargo.getDay_def_flag();
String day_start_time = cargo.getDay_start_time();
String day_end_time = cargo.getDay_end_time();
String mdcq_flag = cargo.getMdcq_flag();
String mdcq_percentage = cargo.getMdcq_percentage();
String remark = cargo.getRemark();
String contpty_abbr = cargo.getContpty_abbr();
String agmt_name = cargo.getAgmt_name();
String agmt_ref_no =cargo.getAgmt_ref_no();
String cont_name=cargo.getCont_name();
String cont_disp_name=cargo.getCont_disp_name();
String cont_ref_no=cargo.getCont_ref_no();
String dda_dt=cargo.getDda_dt();
String dda_time=cargo.getDda_time();
String cont_status_flg = cargo.getCont_status_flg();
String cont_status = cargo.getCont_status();
String bill_flag = cargo.getBilling_flag();
String billing_clause = cargo.getBilling_clause();
String rev_dt = cargo.getRev_dt();
String allowed_laytime_hrs = cargo.getAlw_laytime_hrs();
String allowed_laytime_msn = cargo.getAlw_laytime_mns();
String demurrage = cargo.getDemurrage();
String demurrage_rate = cargo.getDemurrage_rate();
String demurrage_rate_unit = cargo.getDemurrage_rate_unit();
String messurment_flag = cargo.getMessurment_flag();
String meas_std = cargo.getMeas_std();
String meas_temp = cargo.getMeas_temp();
String pressure_max_bar = cargo.getPressure_max_bar();
String pressure_min_bar = cargo.getPressure_min_bar();
String off_spec_gas_flag = cargo.getOff_spec_gas_flag();
String spec_gas_eng_base = cargo.getSpec_gas_eng_base();
String spec_max_eng = cargo.getSpec_max_eng();
String spec_min_eng = cargo.getSpec_min_eng();
String liability_flag = cargo.getLiability_flag();
String liability_clause = cargo.getLiability_clause();
String liability_lq_dmg = cargo.getLiability_lq_dmg();
String liability_take_pay = cargo.getLiability_take_pay();
String liability_makeup = cargo.getLiability_makeup();
String terminaton_flag = cargo.getTermination_flag();
String termination_clause = cargo.getTermination_clause();
String termination_planned = cargo.getTermination_planned();
String termination_forced = cargo.getTermination_forced();
String price_change_history="";
String cargo_status_flag = "";
if(cargo_status_flag.equals(""))
{
	cargo_status_flag="N";
}
String fcc_flg=cargo.getFcc_flg();
String no_of_cargo_dtl = cargo.getNo_of_cargo_dtl();
String no_of_security_dtl=cargo.getNo_of_security_dtl();
String no_of_billing_dtl=cargo.getNo_of_billing_dtl();
String no_of_liability_dtl=cargo.getNo_of_liability_dtl();
String split_flag = cargo.getSplit_flag();
String split_type = cargo.getSplit_type();
String cargo_number = cargo.getCargo_number();
String show_cargo_number = cargo.getShow_cargo_number();
String day_def_clause = cargo.getDay_def_clause();
String demurrage_clause = cargo.getDemurrage_clause();
String measurement_clause = cargo.getMeasurement_clause();
String spec_gas_clause = cargo.getSpec_gas_clause();
String agmt_agmt_base=cargo.getAgmt_agmt_base();

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
if(agmt_base.equals("")){
	agmt_base="X";
}
if(agmt_typ.equals("")){
	agmt_typ="0";
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

String sec_category_value="";
if((no_of_security_dtl.equals("0") || no_of_security_dtl.equals("")))
{
	sec_category_value = "";
}
else
{
	sec_category_value="I";
}


//<!--Harsh Maheta 20230903 : Added for old values to show in Deal audit history-->//
String cp_name = cargo.getContpty_name();
String cp_abbr = cargo.getContpty_abbr();

String old_value="CP="+counterparty_cd+"#NAME="+cp_name+"#ABBR="+cp_abbr+"#CONTNAME="+cont_name+"#CONTNO="+cont_no+"#CONTREFNO="+cont_ref_no+"#CONTTYPE="+contract_type+"#DDADT="+dda_dt+"#DDATIME="+dda_time+"#SIGNDT="+signing_dt+"#SIGNTIME="+signing_time+
"#ENTDT="+ent_dt+"#ENTTIME="+ent_time+"#AGMTTYPE="+agmt_type+"#AGMTBASE="+agmt_base+"#STARTDT="+start_dt+"#ENDDT="+end_dt+"#CONT_STATUS="+cont_status_flg;
%>
<body  style="pointer-events: none;" onload="demurrageShows();messurmentShows();terminatorShows();off_spec_gasShows();billingShows();liabilityShows();setValues('<%=strPlant%>','<%=strBuPlant%>');"> <%--  onload="setValues('<%=strTransCd%>','<%=strTrans%>','<%=strPlant%>','<%=strBuPlant%>',
'<%=strGxBuPlant%>','<%=strSplitVal%>','<%=split_flag%>','<%=strOthTrdCd%>','<%=strOthPlant%>','<%=strOthSplitVal%>');" --%>
<div style="pointer-events: auto;"><%@ include file="../home/header.jsp"%></div>

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
					    	Confirmation Notice (Finance Checks & Control)
					    </div>
					   <%--  <div class="btn-group">
						  <label class="btn btn-outline-secondary btngrp <%if(opration.equals("INSERT")){%>btnactive<%}%>" onclick="refresh('INSERT','<%=clearance%>');"><i class="fa fa-plus-circle"></i>&nbsp;New</label>
						  <label class="btn btn-outline-secondary btngrp <%if(opration.equals("MODIFY")){%>btnactive<%}%>" onclick="refresh('MODIFY','<%=clearance%>');"><i class="fa fa-edit"></i>&nbsp;Modify</label>
						</div> --%>
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
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=opration%>');"  style="pointer-events: auto;">
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
				    					<option value="N">LNG</option>
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
				    			<input type="button" class="btn btn-info btn-sm select_btn" value="Select Contract" onclick="openContList();" style="font-weight: bold;pointer-events: auto;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
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
				    				<input type="hidden" name="cont_status" value="<%=cont_status%>">
				      			</div>
				  			</div>
						</div>
					</div>
					<%} %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Contract Ref#</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="cont_ref_no" value="<%=cont_ref_no%>" maxLength="25">
				    			</div>
				  			</div>
						</div>
						<!-- <div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Trade Ref#</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="trade_ref_no" value="" maxLength="25">
				    			</div>
				  			</div>
						</div> -->
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
			      						onblur="validateDate(this);checkSigningDdaDate();" onchange="validateDate(this);checkSigningDdaDate();" autocomplete="off">
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
			      						onblur="validateDate(this);checkSigningStartDt(document.forms[0].start_dt, this.value);checkSigningDdaDate();" 
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
					      				<input type="radio" name="agreement_type" value="0" <%if(agmt_typ.equals("0")){%>checked<%}%>>&nbsp;Term&nbsp;&nbsp;
				      					<input type="radio" name="agreement_type" value="1" <%if(agmt_typ.equals("1")){%>checked<%}%>>&nbsp;Spot&nbsp;&nbsp;
					    			<!--  </div>-->
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
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Agreement Base<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="radio" name="agreement_base" value="X" <%if(agmt_base.equals("X")){%>checked<%}%> onclick="showHide('X');">&nbsp;Ex-Ship&nbsp;&nbsp;
				      				<input type="radio" name="agreement_base" value="D" <%if(agmt_base.equals("D")){%>checked<%}%> onclick="showHide('D');">&nbsp;DES&nbsp;&nbsp;
				      				<%if(agmt_agmt_base.equals("X")){ %>
			    					<script>document.forms[0].agreement_base[1].style.pointerEvents = "none";</script>
			    					<%}else if(agmt_agmt_base.equals("D")){ %>
			    					<script>document.forms[0].agreement_base[0].style.pointerEvents = "none";</script>
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
					<div class="row m-b-5" id="dlvTranChg" style="display:none;"> 
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Transportation Charges<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="transportation_charges" value="" onkeyup="checkForNumber(this);" onblur="checkNumber1(this,7,4);">
				    			</div>
				  			</div>
						</div>
					</div>
					
					<!-- &nbsp; -->
					<div class="row m-b-5" <%if(!opration.equals("MODIFY")){ %>style="display:none;"<%} %>>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Security<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<select class="form-select form-select-sm" name="sec_category" id="sec_category" style="pointer-events: auto;">
										<option value="">--Select--</option>
										<option value="R">Incoming</option>
										<option value="I">Outgoing</option>
									</select>
									<script>document.forms[0].sec_category.value="<%=sec_category_value%>"</script>									
								</div>
				    			<div class="col">
				    				<input type="button" name="security_btn" class="btn btn-sm config_btn" value="Security Config" onclick="securityPreReceipt('<%=contract_type %>','<%=agmt_no%>','<%=agmt_rev_no%>','<%=cont_no%>','<%=cont_rev_no%>');" style="pointer-events: auto;">
				  					<input type="hidden" name="securityFlag" value="N">
				  				</div>
				  			</div>
						</div>
					</div>
					<!-- <div class="row m-b-5">
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
					</div> -->
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
				</div>
				<div class="card-body cdbody" <%if(opration.equals("MODIFY") && (!cont_no.equals("0") && !cont_no.equals(""))){ %><%}else{ %>style="display:none;"<%} %>>
				    <div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Cargo Details</label>
					</div>
					<!-- <div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-between"> 
								<div></div>
								<div class="btn-group" align="right">
									<label class="btn btn-outline-secondary subbtngrp1" data-bs-toggle="modal" data-bs-target="#AddNewCargo">Add New Cargo</label>
								</div>
							</div>
						</div>
					</div> -->
				    <div class="row m-b-5">
				    	<div class="col-sm-6 col-xs-6 col-md-6">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>No. Of Cargo :</b></label>
								</div>
				    			<div class="col-auto">
			    					<input name="cargo_number" id="cargo_number" type="text" class="form-control form-control-sm" Readonly value="<%=no_of_cargo_dtl %>">
				    			</div>
				    		</div>
				    	</div>
				    </div>
				    <div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="cargo_tb">
								<thead>
									<tr>
										<th></th>
										<th>Cargo#</th>
										<th>Cargo Ref#</th>
										<th>Status</th>
										<th>Cargo Arrival Window <br> (From - To)</th>
										<th>Volume (MMBTU)</th>
										<th>Volume Tolerance (%)</th>
										<th>Rate Unit</th>										
										<th>Rate</th>
									</tr>
								</thead>
								<tbody id="itemTab">
								<%if(VCARGO_NO.size()>0)
								{
									for (int i=0;i<VCARGO_NO.size();i++)
									{%>
										<tr id="row">
											<td align="center"><%=i+1%></td>
											<td align="center"><%=VDISP_CARGO_NO.elementAt(i) %></td>
											<td align="center"><%=VCARGO_REF.elementAt(i) %></td>
											<td align="center">
											<font style="color:<%if(VCARGO_STATUS_FLG.elementAt(i).equals("Y")){%>#a6ff4d<%}else if(VCARGO_STATUS_FLG.elementAt(i).equals("N")){%>gold<%}else{%>red<%}%>">
												<i class="fa fa-circle fa-lg" ></i>
												&nbsp;
											</font>
											<%=VCARGO_STATUS.elementAt(i) %></td>
											<td align="center"><%=VCARGO_START_DT.elementAt(i) %> - <%=VCARGO_END_DT.elementAt(i) %></td>
											<td align="right"><%=VCARGO_QTY.elementAt(i) %></td>
											<td align="right"><%= VCARGO_TOLERANCE_PER.elementAt(i)%></td>
											<td align="center"><%=VCARGO_PRICE_UNIT.elementAt(i) %></td>
											<td align="center">
												<div class="d-flex justify-content-between"> 
													<div class="form-group row">
														<div style="width:100px;">
															<input type="text" class="form-control form-control-sm" value="<%=VCARGO_PRICE.elementAt(i) %>" style="text-align:right" readonly>	
														</div>
														<%-- <div style="width:100px;">
															<input type="text" class="form-control form-control-sm" value="<%=VCARGO_PRICE_UNIT.elementAt(i) %>" readonly>	
														</div> --%>
														<div style="width:100px;" >
															<input type="button" class="btn btn-sm config_btn" value="Variable Price Config" 
															onclick="variablePriceConfig('<%=contract_type%>','<%=agmt_no%>','<%=agmt_rev_no%>','<%=cont_no%>',
																						'<%=cont_rev_no%>','<%=cont_ref_no%>','<%=VCARGO_STATUS_FLG.elementAt(i)%>',
																						'<%=VCARGO_START_DT.elementAt(i) %>','<%=VCARGO_END_DT.elementAt(i) %>','<%=VCARGO_NO.elementAt(i)%>');" style="pointer-events: auto;">
														</div>
													</div>
												</div>
											</td>
										</tr>
									<%} %>
								<%}else{ %>
								<tr>
									<td colspan="8" align="center"><%=utilmsg.infoMessage("<b>No Cargo Available!</b>")%></td>
								</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%if(opration.equals("MODIFY")){ %>
					<%} %>
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
			      						<input type="text" class="form-control form-control-sm" name="allowed_lay_time_hrs" id="allowed_lay_time_hrs"  value="<%=allowed_laytime_hrs %>" placeholder="Hours" maxLength="5"  autocomplete="off" onchange="adjustTimeHour()">
			      						<span class="input-group-text">Hour</span>
		      						</div>
				    			</div>
				    			<div class="col">
						      		<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="allowed_lay_time_min" id="allowed_lay_time_min"  value="<%=allowed_laytime_msn %>" placeholder="Minutes" maxLength="5"  autocomplete="off" onchange="adjustTimeMin()">
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
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="demmurageCheckbox" id="demmurageCheckbox" value="Y" onchange="demurrageShows()" <%if(demurrage.equals("Y")){ %>checked<%} %>> Demurrage Rate</b>
			      					</label>
				  				</div>
				    			<div class="col" id="demurrage_clause_div">
				    				<input type="text" class="form-control form-control-sm" name="demurrage_clause_no" value="<%=demurrage_clause %>" placeholder="Clause#" maxLength="10">
				      			</div>
				  			</div>
						</div>
						<div id="demurrage_div_std" class="col-sm-4 col-xs-4 col-md-4">
		      		 		<div class="form-group row">
		      					<div class="col-sm-6 col-xs-6 col-md-6">
				      				<input type="text" class="form-control form-control-sm" name="demmurage_rate" id="demmurage_rate" value="<%=demurrage_rate%>" onblur="checkValuePrecision(this.value,'4','2','demmurage_rate')">
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
								<div class="col-auto">
									<label class="form-label">&nbsp;Energy Base</label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="spec_gas_energy_base" >
		      							<option value="">--Select--</option>
		      							<option value="GCV">GCV</option>
		      							<option value="NCV">NCV</option>
		      						</select>
	 		      					<script>document.forms[0].spec_gas_energy_base.value="<%=spec_gas_eng_base.trim()%>"</script>
								</div>
								<div class="col-auto">
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
					<div class="row m-b-5"  style="pointer-events: auto;">
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
					<div class="row m-b-5"  style="pointer-events: auto;">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b><input type="checkbox" class="form-check-input" name="billing_flag" id="billing_flag" value="Y" onchange="billingShows()" <%if(bill_flag.equals("Y")){ %>checked<%} %>>&nbsp;Billing</b></label>
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
						<div class="row m-b-5"  style="pointer-events: auto;">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Finance Checks & Control</b></label>
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
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>

<div class="modal fade" id="AddNewCargo" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
        			Add New Cargo Details
        		</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" >
        	</div>
        	<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Cargo#</b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" >
				    				<input type=text class="form-control form-control-sm" name="show_cargo_number" id="show_cargo_number" value="<%=show_cargo_number %>" readonly>
		      						<input type="hidden" class="form-control form-control-sm" name="cargo_no" id="cargo_no" value="<%=cargo_number %>" readonly>
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
								<select class="form-select form-select-sm" name="cargo_status_flag"  style="text-align:left; flex: 2; width: 100px;">
							   	 	 <option value="N" selected="selected">Not-Confirmed</option>
							   	 	 <option value="Y">Confirmed</option>
							   	 	 <option value="C">Cancelled</option>
							   	</select>
							   	<script>document.forms[0].cargo_status_flag="<%=cargo_status_flag%>"</script>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Cargo Ref#<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm" name="cargo_ref" id="cargo_ref"  value="">
	      						</div>
							</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Agreement Base<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    			<%if(agmt_base.equals("X")){%>
				    				<input type="radio" name="cargo_agmt_base" value="X" <%if(agmt_base.equals("X")){%>checked<%}%> onclick="showHide('X');">&nbsp;Ex-Ship&nbsp;&nbsp;
				    			<%}else if(agmt_base.equals("D")){ %>
				    				<input type="radio" name="cargo_agmt_base" value="D" <%if(agmt_base.equals("D")){%>checked<%}%> onclick="showHide('D');">&nbsp;DES&nbsp;&nbsp;
				    			<%}else if(agmt_base.equals("B")){ %>
				      				<input type="radio" name="cargo_agmt_base" value="X" <%if(agmt_base.equals("X")){%>checked<%}%> onclick="showHide('X');">&nbsp;Ex-Ship&nbsp;&nbsp;
				      				<input type="radio" name="cargo_agmt_base" value="D" <%if(agmt_base.equals("D")){%>checked<%}%> onclick="showHide('D');">&nbsp;DES&nbsp;&nbsp;
				      			<%} %>
				    			</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>From Date<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="cargo_from_dt" value="" maxLength="10" 
		      						onblur="validateDate(this);checkCargoFromDt();checkStartEndDate(document.forms[0].cargo_to_dt,this,'F');" onchange="validateDate(this);checkCargoFromDt();checkStartEndDate(this,document.forms[0].cargo_to_dt,'F')" autocomplete="off">
		      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
	      						</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> To Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="cargo_to_dt" value="" maxLength="10" 
		      						onblur="validateDate(this);checkCargoEndDt();checkStartEndDate(document.forms[0].cargo_from_dt,this,'T');" onchange="validateDate(this);checkCargoEndDt();checkStartEndDate(document.forms[0].cargo_from_dt,this,'T');" autocomplete="off">
		      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
	      						</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Volume(MMBTU)<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm" name="cargo_volume" id="cargo_volume" value="" onblur="checkValuePrecision(this.value,'2','10','cargo_volume')">
	      						</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Cargo Price<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" style="flex: 2;" name="cargo_price" id="cargo_price" style="text-align:right;" value="" 
				      				onblur="checkValuePrecision(this.value,'2','4','cargo_price')">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
			      				<select class="form-select form-select-sm" name="cargo_rate_unit" onchange="checkRateFormate(this);checkInrUsd();" style="text-align:left; flex: 2; width: 100px;"  <%if(opration.equals("MODIFY")) {%>style="pointer-events: none;"<%} %>>
							   	 	 <option value="" selected="selected">--Select--</option>
							   	 	 <option value="1">INR/MMBTU</option>
							   	 	 <option value="2">USD/MMBTU</option>
							   	</select>
				  			</div>
				  		</div>
      				</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div align="right">
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitcargo();">
				</div>
      		</div>
        </div>
	</div>
</div>



<input type="hidden" name="option" value="CN_MST">
<input type="hidden" name="old_value" value="<%=old_value%>">

<input type="hidden"  name="no_of_cargo" value="">

<input type="hidden" name="prev_clearance" value="<%=clearance%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="existing_cargo_no" value="">
<input type="hidden" name="cont_status_flg" value="<%=cont_status_flg%>">
<input type="hidden" name="agmt_type" value="<%=agmt_type%>" >

<input type="hidden" name="linkedTransSize" value="<%=VFORMULA_ID.size()%>">

<input type="hidden" name="no_of_billing_dtl" value="<%=no_of_billing_dtl%>">
<input type="hidden" name="no_of_security_dtl" value="<%=no_of_security_dtl%>">
<input type="hidden" name="no_of_cargo_dtl" value="<%=no_of_cargo_dtl%>">
<input type="hidden" name="no_of_liability_dtl" value="<%=no_of_liability_dtl%>">
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



</form>
</body>
</html>