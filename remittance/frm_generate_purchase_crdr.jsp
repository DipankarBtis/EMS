<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh(operation)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var sel_inv_no = document.forms[0].sel_inv_no.value;
	var invoice_type = "";
	if(document.getElementsByName("invoice_type").length > 0)
	{
		invoice_type = document.getElementsByName("invoice_type")[0].value;
	}
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var prev_month = document.forms[0].prev_month.value;
	var prev_year = document.forms[0].prev_year.value;
	
	var crdr_gen_type = document.forms[0].crdr_gen_type.value;
	var accroid = document.forms[0].accroid.value;
	var u = document.forms[0].u.value;
	
	if(prev_month!=month || prev_year!=year)
	{
		counterparty_cd="";
		sel_inv_no="";
	}
	
	var msg="";
	var flag = true;
	if(flag)
	{
		var url = "frm_generate_purchase_crdr.jsp?u="+u+"&counterparty_cd="+counterparty_cd+
				"&month="+month+"&year="+year+"&operation="+operation+"&accroid="+accroid+"&sel_inv_no="+sel_inv_no+"&crdr_gen_type="+crdr_gen_type;//+
				//"&invoice_type="+invoice_type;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}

function changeTblhed(obj)
{
	var typ="";
	if(obj.value=="CR")
	{
		typ="Credit Note";
	}
	else if(obj.value=="DR")
	{
		typ="Debit Note";
	}
	typ=typ+" [B-A]";
	document.getElementById("th_invType").innerHTML=typ
}

function enableNewValues(obj)
{
	
	var operation = document.forms[0].operation.value;
	
	if(obj.value=="QTY")
	{
		var changed_qty_mmbtu = document.forms[0].new_alloc_qty.value;
		var main_alloc_qty = document.forms[0].main_alloc_qty.value;
		
		if(operation=="MODIFY" && !obj.checked)
		{
			if(parseFloat(changed_qty_mmbtu).toFixed(2) != parseFloat(main_alloc_qty).toFixed(2))
			{
				alert("Quantity Change detected! CR/DR Criteria - Change in Quantity - can't be de-Selected!");
				obj.checked=true;
			}
		}
	}
	
	if(obj.value=="PRICE")
	{
		if(obj.checked)
		{
			//document.getElementById("div_price").style.display="";
			document.forms[0].new_price.readOnly=false;
		}
		else
		{
			//document.getElementById("div_price").style.display="none";
			document.forms[0].new_price.readOnly=true;
		}
	}
	
	if(obj.value=="EXCHG")
	{
		if(obj.checked)
		{
			//document.getElementById("div_exchange").style.display="";
			document.forms[0].new_exchng_rate.readOnly=false;
		}
		else
		{
			//document.getElementById("div_exchange").style.display="none";
			document.forms[0].new_exchng_rate.readOnly=true;
		}
	}
	
	if(obj.value=="TC")
	{
		if(obj.checked)
		{
			//document.getElementById("div_transportation_tariff").style.display="";
			document.forms[0].new_transportation_tariff.readOnly=false;
		}
		else
		{
			//document.getElementById("div_transportation_tariff").style.display="none";
			document.forms[0].new_transportation_tariff.readOnly=true;
		}
	}
	
	if(obj.value=="MM")
	{
		if(obj.checked)
		{
			//document.getElementById("div_marketing_margin").style.display="";
			document.forms[0].new_marketing_margin.readOnly=false;
		}
		else
		{
			//document.getElementById("div_marketing_margin").style.display="none";
			document.forms[0].new_marketing_margin.readOnly=true;
		}
	}
	
	if(obj.value=="OC")
	{
		if(obj.checked)
		{
			//document.getElementById("div_other_charges").style.display="";
			document.forms[0].new_other_charges.readOnly=false;
		}
		else
		{
			//document.getElementById("div_other_charges").style.display="none";
			document.forms[0].new_other_charges.readOnly=true;
		}
	}
	
	if(obj.value=="TAXP")
	{
		if(obj.checked)
		{
			//document.getElementById("td_price").style.display="";
			document.forms[0].tax_config_btn.disabled=false;
		}
		else
		{
			//document.getElementById("td_price").style.display="none";
			document.forms[0].tax_config_btn.disabled=true;
		}
	}
}

async function calc()
{
	var alloc_qty = document.forms[0].main_alloc_qty.value;
	
	var price = document.forms[0].new_price.value;
	var price_cd = document.forms[0].price_cd.value;
	var exchng_rate = document.forms[0].new_exchng_rate.value;
	var main_invoice_raised_in = document.forms[0].main_invoice_raised_in.value;
	
	var transportation_tariff = document.forms[0].new_transportation_tariff.value;
	var marketing_margin = document.forms[0].new_marketing_margin.value;
	var other_charges = document.forms[0].new_other_charges.value;
	var split_flag = document.forms[0].split_flag.value;
	
	var tcs_tds = document.forms[0].tcs_tds.value;
	
	var operation = document.forms[0].operation.value;
	
	if(trim(alloc_qty) != "")
	{
		if(split_flag=="Y")
		{
			alloc_qty = document.forms[0].changed_qty_mmbtu.value;
			document.forms[0].new_alloc_qty.value=parseFloat(alloc_qty).toFixed(2);
			
			if(parseFloat(document.forms[0].changed_qty_mmbtu.value).toFixed(2)!=parseFloat(document.forms[0].main_alloc_qty.value).toFixed(2) && !document.getElementById("criteria_QTY").checked)
			{
				document.getElementById("new_alloc_qty").style.color = "red";
			}
			else
			{
				document.getElementById("new_alloc_qty").style.color = "black";
			}
		}
		else
		{
			if(document.getElementById("criteria_QTY").checked && (operation=="MODIFY" || operation=="INSERT" || operation=="VIEW"))
			{
				alloc_qty=document.forms[0].new_alloc_qty.value;
			}
			else
			{
				alloc_qty=document.forms[0].main_alloc_qty.value;
				document.forms[0].new_alloc_qty.value=alloc_qty;
			}
		}
		
		if(document.getElementById("criteria_PRICE").checked)
		{
			price = document.forms[0].new_price.value;
		}
		else
		{
			price = document.forms[0].main_price.value;
			document.forms[0].new_price.value=price;
		}
		var gross="";
		
		if(!document.getElementById("criteria_PRICE").checked && !document.getElementById("criteria_QTY").checked)
		{
			gross = document.forms[0].main_gross_amt.value;
			document.forms[0].new_gross_amt.value=gross;
		}
		else
		{
			if(trim(price) != "" && trim(alloc_qty) != "")
			{
				gross=parseFloat(parseFloat(alloc_qty)*parseFloat(price)).toFixed(2);
			}
			else
			{
				gross="";
			}
			document.forms[0].new_gross_amt.value=gross;
		}
		
		var gross1="";
		if(price_cd=="2" && main_invoice_raised_in=="1") 
		{
			if(document.getElementById("criteria_EXCHG").checked)
			{
				exchng_rate = document.forms[0].new_exchng_rate.value;
			}
			else
			{
				exchng_rate = document.forms[0].main_exchng_rate.value;
				document.forms[0].new_exchng_rate.value=exchng_rate;
			} 
			
			if(!document.getElementById("criteria_EXCHG").checked && !document.getElementById("criteria_PRICE").checked && !document.getElementById("criteria_QTY").checked)
			{
				gross1 = document.forms[0].main_gross_amt1.value;
				document.forms[0].new_gross_amt1.value=gross1;
			}
			else
			{
				if(trim(exchng_rate)!="" && trim(gross) != "")
				{
					gross1=parseFloat(parseFloat(exchng_rate)*parseFloat(gross)).toFixed(2);
				}
				document.forms[0].new_gross_amt1.value=gross1;
			}
			
		}
		else
		{
			document.forms[0].new_gross_amt1.value=gross;
			gross1=gross;
			
			exchng_rate = document.forms[0].main_exchng_rate.value;
			document.forms[0].new_exchng_rate.value=exchng_rate;
		}
		
		var tc="";
		var mm="";
		var oc="";
		
		if(document.getElementById("criteria_TC").checked)
		{
			transportation_tariff = document.forms[0].new_transportation_tariff.value;
		}
		else
		{
			transportation_tariff = document.forms[0].main_transportation_tariff.value;
			document.forms[0].new_transportation_tariff.value=transportation_tariff;
		}
		
		if(trim(transportation_tariff)!="" && trim(alloc_qty) != "")
		{
			tc=parseFloat(parseFloat(transportation_tariff)*parseFloat(alloc_qty)).toFixed(2);
			document.forms[0].new_transportation_amount.value=tc;
		}
		else
		{
			document.forms[0].new_transportation_amount.value="";
		}
		
		if(document.getElementById("criteria_MM").checked)
		{
			marketing_margin = document.forms[0].new_marketing_margin.value;
		}
		else
		{
			marketing_margin = document.forms[0].main_marketing_margin.value;
			document.forms[0].new_marketing_margin.value=marketing_margin;
		}
		
		if(trim(marketing_margin)!="" && trim(alloc_qty) != "")
		{
			mm=parseFloat(parseFloat(marketing_margin)*parseFloat(alloc_qty)).toFixed(2);
			document.forms[0].new_marketing_margin_amount.value=mm;
		}
		else
		{
			document.forms[0].new_marketing_margin_amount.value="";
		}
	
		if(document.getElementById("criteria_OC").checked)
		{
			other_charges = document.forms[0].new_other_charges.value;
		}
		else
		{
			other_charges = document.forms[0].main_other_charges.value;
			document.forms[0].new_other_charges.value=other_charges;
		}
		
		if(trim(other_charges)!="" && trim(alloc_qty) != "")
		{
			oc=parseFloat(parseFloat(other_charges)*parseFloat(alloc_qty)).toFixed(2);
			document.forms[0].new_other_charges_amount.value=oc;
		}
		else
		{
			document.forms[0].new_other_charges_amount.value="";
		}
		
		if(!document.getElementById("criteria_TC").checked && !document.getElementById("criteria_MM").checked && !document.getElementById("criteria_OC").checked && !document.getElementById("criteria_EXCHG").checked && !document.getElementById("criteria_PRICE").checked && !document.getElementById("criteria_QTY").checked)
		{
			gross1=document.forms[0].main_gross_amt1.value;
			document.forms[0].new_gross_amt1.value=gross1;
		}
		else
		{
			if(tc!="" && gross1!="")
			{
				gross1=parseFloat(parseFloat(tc)+parseFloat(gross1)).toFixed(2);
			}
			
			if(mm!="" && gross1!="")
			{
				gross1=parseFloat(parseFloat(mm)+parseFloat(gross1)).toFixed(2);
			}
			
			if(oc!="" && gross1!="")
			{
				gross1=parseFloat(parseFloat(oc)+parseFloat(gross1)).toFixed(2);
			}
			
			document.forms[0].new_gross_include_transport_tariff.value=gross1;
		}
		if(gross1!="")
		{
			if(document.getElementById("criteria_TAXP").checked)
			{
				await calcExsitingTaxStructure(document.forms[0].new_tax_cd.value,gross1);
			}
			else
			{
				await calcExsitingTaxStructure(document.forms[0].main_tax_cd.value,gross1)
			}
			
			window.setTimeout(function() 
			{
				var tax = document.forms[0].new_tax_amt.value
				var invAmt="";
				if(tax!="")
				{
					invAmt=parseFloat(parseFloat(gross1)+parseFloat(tax)).toFixed(2);
				}
				
				document.forms[0].new_invoice_amt.value=invAmt;
				
				
				if(tcs_tds=="TDS" && gross1!="")
				{
					var main_gross_include_transport_tariff = document.forms[0].main_gross_include_transport_tariff.value;
					var main_tds_amt = document.forms[0].main_tds_amt.value;
					var tds_factor=document.forms[0].main_tds_factor.value;
					
					const diffGross = (parseFloat(gross1) - parseFloat(main_gross_include_transport_tariff)).toFixed(2);
					
					var tdsAmt="";
					var temp_tdsAmt="";
					if(tds_factor != "")
					{
						temp_tdsAmt=((parseFloat(diffGross)*parseFloat(tds_factor))/100).toFixed(2);
						
						tdsAmt = (parseFloat(temp_tdsAmt)+parseFloat(main_tds_amt)).toFixed(2);
					}
					
					document.forms[0].new_tds_amt.value=tdsAmt;
					document.forms[0].new_net_payable.value=invAmt;
				}
				else if(tcs_tds=="TCS")
				{
					var main_inv_amt = document.forms[0].main_invoice_amt.value;
					var main_tcs_amt = document.forms[0].main_tcs_amt.value;
					var tcs_factor=document.forms[0].main_tcs_factor.value;
					
					const diffInv = (parseFloat(invAmt) - parseFloat(main_inv_amt)).toFixed(2);

					var tcsAmt="";
					var diff_tcsAmt="";
					if(trim(main_tcs_amt) != "")
					{
						diff_tcsAmt=((parseFloat(diffInv)*parseFloat(tcs_factor))/100).toFixed(2);
						tcsAmt = (parseFloat(diff_tcsAmt)+parseFloat(main_tcs_amt)).toFixed(2);
					}
					document.forms[0].new_tcs_amt.value=tcsAmt;
					
					document.forms[0].new_net_payable.value=(parseFloat(invAmt)+parseFloat(tcsAmt)).toFixed(2);
				}
				else
				{
					document.forms[0].new_net_payable.value=invAmt;
				}
			}, 800);
		}
		
		window.setTimeout(function() 
		{
			difference('');
		}, 800);
	}
}

function difference(changed_flg)
{
	
	var main_price_cd = document.forms[0].main_price_cd.value;
	var is_changed = "";
	const fields = [
        "alloc_qty", "price", "gross_amt", "exchng_rate", "gross_amt1",
        "transportation_tariff", "transportation_amount",
        "marketing_margin", "marketing_margin_amount",
        "other_charges", "other_charges_amount", "gross_include_transport_tariff", "tax_amt", "invoice_amt","tcs_amt", "net_payable","tds_amt"
    ];
	
	fields.forEach(field => {
        var mainValue = document.getElementsByName("main_"+field)[0].value;
        var newValue = document.getElementsByName("new_"+field)[0].value;
        
        var gross_amt1 = document.forms[0].gross_amt1.value;
		var gross_include_transport_tariff = document.forms[0].gross_include_transport_tariff.value;
		//alert(mainValue+" == "+newValue)
        if (mainValue !== "" && newValue !== "") 
        {
        	const diff = parseFloat(newValue) - parseFloat(mainValue);
        	if(main_price_cd=="2" && field == "price")
        	{
        		document.getElementsByName(field)[0].value = diff.toFixed(4);
        	}
        	else if(field == "transportation_tariff" || field == "marketing_margin" || field == "other_charges")
        	{
        		document.getElementsByName(field)[0].value = diff.toFixed(4);
        	}
        	else if(field == "exchng_rate")
        	{
        		if(document.getElementById("criteria_EXCHG").checked)
       			{
        			document.getElementsByName(field)[0].value = diff.toFixed(4);
       			}
        		else
       			{
        			document.getElementsByName(field)[0].value = mainValue;
       			}
        	}
        	else
        	{
        		document.getElementsByName(field)[0].value = diff.toFixed(2);
        	}  	
        
		
			if(field=="gross_amt1" && gross_amt1!=diff.toFixed(2) && gross_amt1!="" && gross_amt1!="0.00" && changed_flg!="")
			{
				is_changed="Y";
				document.forms[0].gross_amt1.value=gross_amt1;
				
				if(changed_flg=="G")
				{
					calcExsitingTaxStructureforChange(document.forms[0].main_tax_cd.value,gross_amt1)
				}
				
			}
			if(field=="gross_include_transport_tariff" && gross_include_transport_tariff!=diff.toFixed(2) && gross_include_transport_tariff!="" && changed_flg!="")
			{
				is_changed="Y";
				document.forms[0].gross_include_transport_tariff.value=gross_include_transport_tariff;
				
				if(changed_flg=="G")
				{
					calcExsitingTaxStructureforChange(document.forms[0].main_tax_cd.value,gross_include_transport_tariff)
				}
			}
        }
    });
	
	
	var sub_tax_amt=document.getElementsByName("sub_tax_amt");
	if(sub_tax_amt.length > 0)
	{
		var tmp_tax_amt = 0;
		for(var i=0; i<sub_tax_amt.length; i++)
    	{
			var mainValue = document.getElementsByName("main_sub_tax_amt")[i].value;
	        var newValue = document.getElementsByName("new_sub_tax_amt")[i].value;
			//alert(mainValue+" == "+newValue)
	        if (mainValue !== "" && newValue !== "") 
	        {
	        	const diff = parseFloat(newValue) - parseFloat(mainValue);
	        	
	        	if(sub_tax_amt[i].value!=diff && sub_tax_amt[i].value!="" && sub_tax_amt[i].value!="0.00" && changed_flg=="T")
        		{
	        		sub_tax_amt[i].value = sub_tax_amt[i].value;
	        		tmp_tax_amt+=parseFloat(sub_tax_amt[i].value);
	        		is_changed="Y";
        		}
	        	else
        		{
	        		sub_tax_amt[i].value = diff.toFixed(2);
	        		tmp_tax_amt+=diff;
        		}
	        }
    	}
		if(is_changed=="Y")
		{
			document.forms[0].tax_amt.value=tmp_tax_amt;
		}
	}
	
	
	if(is_changed=="Y")
	{
		var tax_amt = document.forms[0].tax_amt.value;
		var gross_amt1 = document.forms[0].gross_amt1.value;
		var gross_include_transport_tariff = document.forms[0].gross_include_transport_tariff.value;
		
		var transportation_tariff = document.forms[0].new_transportation_tariff.value;
		var marketing_margin = document.forms[0].new_marketing_margin.value;
		var other_charges = document.forms[0].new_other_charges.value;
		if(transportation_tariff!="" || marketing_margin!="" || other_charges!="")
		{
			if(gross_include_transport_tariff!="")
			{
				gross_amt1=gross_include_transport_tariff;
			}
		}
		
		document.forms[0].invoice_amt.value=parseFloat(gross_amt1)+parseFloat(tax_amt);
		var tmp_inv_amt=document.forms[0].invoice_amt.value;
		
		var tcs_tds = document.forms[0].tcs_tds.value;
		
		
		if(tcs_tds=="TCS")
		{
			var main_tcs_amt = document.forms[0].main_tcs_amt.value;
			if(trim(main_tcs_amt)=="")
			{
				main_tcs_amt="0.00";
			}
			document.forms[0].net_payable.value=(parseFloat(tmp_inv_amt)+parseFloat(main_tcs_amt)).toFixed(2);
		}
		else
		{
			document.forms[0].net_payable.value=tmp_inv_amt;
		}
		
		if(tcs_tds=="TDS")
		{
			var main_tds_factor = document.forms[0].main_tds_factor.value;
			
			document.forms[0].tds_amt.value = ((parseFloat(gross_amt1)*parseFloat(main_tds_factor))/100).toFixed(2);
		}
	}
}
enableButton=true;
function doSubmit()
{
	var bu_unit = document.forms[0].bu_unit.value;
	var plant_seq = document.forms[0].plant_seq.value;
	var contact_person = document.forms[0].contact_person.value;
	var bu_contact_person = document.forms[0].bu_contact_person.value;
	
    var counterparty_cd = document.forms[0].counterparty_cd.value;
    var agmt_no = document.forms[0].agmt_no.value;
    var agmt_rev = document.forms[0].agmt_rev.value;
    var cont_no = document.forms[0].cont_no.value;
    var cont_rev = document.forms[0].cont_rev.value;
    var cargo_no = document.forms[0].cargo_no.value;
    var contract_type = document.forms[0].contract_type.value;
    var period_start_dt = document.forms[0].period_start_dt.value;
    var period_end_dt = document.forms[0].period_end_dt.value;
    var couterpty_abbr = document.forms[0].couterpty_abbr.value;
    var sel_inv_no = document.forms[0].sel_inv_no.value;
    var deal_no = document.forms[0].deal_no.value;
    
    var invoice_dt = document.forms[0].invoice_dt.value;
	var invoice_due_dt = document.forms[0].invoice_due_dt.value;
	var alloc_qty = document.forms[0].alloc_qty.value;
	var price = document.forms[0].price.value;
	var price_cd = document.forms[0].price_cd.value;
	var gross_amt = document.forms[0].gross_amt.value;
	var exchng_rate = document.forms[0].exchng_rate.value;
	var gross_amt1 = document.forms[0].gross_amt1.value;
	var tax_amt = document.forms[0].tax_amt.value;
	var invoice_amt = document.forms[0].invoice_amt.value;
	var net_payable = document.forms[0].net_payable.value;
	var gross_include_transport_tariff = document.forms[0].gross_include_transport_tariff.value
	
	var criteria = document.getElementsByName("criteria");
	var invoice_type = document.forms[0].invoice_type.value;
	var operation = document.forms[0].operation.value;
	var invoice_ref = document.forms[0].invoice_ref.value;
	
	var remark1 = document.forms[0].remark1.value;
	var temp_criteri_formula = document.forms[0].temp_criteri_formula.value;
	
	var crdr_gen_type = document.forms[0].crdr_gen_type.value;
	var split_invoice_list = document.forms[0].split_invoice_list.value;
	var split_flag = document.forms[0].split_flag.value;
	
    var msg="";
	var flag=true;
	var isChangeInQty=false;
	
	if(invoice_type.trim() === "") 
    {
        msg += "Select CR/DR Type!\n";
        flag = false;
    }
    if(counterparty_cd.trim() === "") 
    {
        msg += "Missing Counterparty Code!\n";
        flag = false;
    }
    if(agmt_no.trim() === "") 
    {
        msg += "Missing Agreement Number!\n";
        flag = false;
    }
    if(agmt_rev.trim() === "") 
    {
        msg += "Missing Agreement Revision!\n";
        flag = false;
    }
    if(cont_no.trim() === "") 
    {
        msg += "Missing Contract Number!\n";
        flag = false;
    }
    if(cont_rev.trim() === "") 
    {
        msg += "Missing Contract Revision!\n";
        flag = false;
    }
    
    if(contract_type=="O" || contract_type=="Q")
    {
	    if(cargo_no.trim() === "") 
	    {
	        msg += "Missing Cargo No!\n";
	        flag = false;
	    }
    }
    if(contract_type.trim() === "") 
    {
        msg += "Missing Contract Type!\n";
        flag = false;
    }
    if(bu_unit.trim() === "") 
    {
        msg += "Missing Bussiness Unit!\n";
        flag = false;
    }
    if(plant_seq.trim() === "") 
    {
        msg += "Missing Customer Plant!\n";
        flag = false;
    }
    
    if(trim(invoice_ref)=="")
	{
		msg+="Enter Invoice#!\n";
		flag=false;
	}
    
    if(trim(invoice_dt)=="")
	{
		msg+="Enter Invoice Date!\n";
		flag=false;
	}
	
	if(trim(invoice_type)!="CR")
	{
		if(trim(invoice_due_dt)=="")
		{
			msg+="Enter Invoice Due Date!\n";
			flag=false;
		}	
	}
	
	if(trim(invoice_dt)!="" && trim(invoice_due_dt)!="")
	{
		var count = compareDate(invoice_dt,invoice_due_dt);
		if(parseInt(count) == 1)
		{
			msg+="Invoice Due Date should be grater or equal Invoice Date!";
			flag=false;
		}
	}
	
    var criChkCnt=parseInt("0");
    if(criteria.length > 0)
    {
    	for(var i=0; i<criteria.length; i++)
    	{
    		var chngin=criteria[i].value;
    		
    		if(criteria[i].checked)
    		{
    			criChkCnt++;
    		}
    		
    		if(chngin=="QTY" && criteria[i].checked)
    		{
    			isChangeInQty=true;
    			var main_alloc_qty = document.forms[0].main_alloc_qty.value;
    			var new_alloc_qty = document.forms[0].new_alloc_qty.value;
    			
    			if(new_alloc_qty.trim()=="")
    			{
    				msg += "Enter New Quantity!\n";
    		        flag = false;
    			}
    			else if(operation=="MODIFY")
    			{
    				if(new_alloc_qty.trim()==main_alloc_qty.trim() && temp_criteri_formula.includes("QTY"))
    				{
    					msg += "New Quantity and Invoice Quantity shouldn't be same!\n";
        		        flag = false;
    				}
    			}
    		}
    		else if(chngin=="PRICE" && criteria[i].checked)
    		{
    			var main_price = document.forms[0].main_price.value;
    			var new_price = document.forms[0].new_price.value;
    			
    			if(new_price.trim()=="")
    			{
    				msg += "Enter New Price!\n";
    		        flag = false;
    			}
    			else if(new_price.trim()==main_price.trim())
    			{
    				msg += "New Price and Invoice Price shouldn't be same!\n";
    		        flag = false;
    			}
    		}
    		else if(chngin=="EXCHG" && criteria[i].checked)
    		{
    			var main_exchng_rate = document.forms[0].main_exchng_rate.value;
    			var new_exchng_rate = document.forms[0].new_exchng_rate.value;
    			
    			if(new_exchng_rate.trim()=="")
    			{
    				msg += "Enter New Exchange Rate!\n";
    		        flag = false;
    			}
    			else if(new_exchng_rate.trim()==main_exchng_rate.trim())
    			{
    				msg += "New Exchange Rate and Invoice Exchange Rate shouldn't be same!\n";
    		        flag = false;
    			}
    		}
    		else if(chngin=="TC" && criteria[i].checked)
    		{
    			var main_transportation_tariff = document.forms[0].main_transportation_tariff.value;
    			var new_transportation_tariff = document.forms[0].new_transportation_tariff.value;
    			
    			if(new_transportation_tariff.trim()=="")
    			{
    				msg += "Enter New Transportation Tariff!\n";
    		        flag = false;
    			}
    			else if(new_transportation_tariff.trim()==main_transportation_tariff.trim())
    			{
    				msg += "New Transportation Tariff and Invoice Transportation Tariff shouldn't be same!\n";
    		        flag = false;
    			}
    		}
    		else if(chngin=="MM" && criteria[i].checked)
    		{
    			var main_marketing_margin = document.forms[0].main_marketing_margin.value;
    			var new_marketing_margin = document.forms[0].new_marketing_margin.value;
    			
    			if(new_marketing_margin.trim()=="")
    			{
    				msg += "Enter New Marketing Margin!\n";
    		        flag = false;
    			}
    			else if(new_marketing_margin.trim()==main_marketing_margin.trim())
    			{
    				msg += "New Marketing Margin and Invoice Marketing Margin shouldn't be same!\n";
    		        flag = false;
    			}
    		}
    		else if(chngin=="OC" && criteria[i].checked)
    		{
    			var main_other_charges = document.forms[0].main_other_charges.value;
    			var new_other_charges = document.forms[0].new_other_charges.value;
    			
    			if(new_other_charges.trim()=="")
    			{
    				msg += "Enter New Other Charges!\n";
    		        flag = false;
    			}
    			else if(new_other_charges.trim()==main_other_charges.trim())
    			{
    				msg += "New Other Charges and Invoice Other Charges shouldn't be same!\n";
    		        flag = false;
    			}
    		}
    		else if(chngin=="TAXP" && criteria[i].checked)
    		{
    			var main_tax_struct_dtl = document.forms[0].main_tax_struct_dtl.value;
    			var new_tax_struct_dtl = document.forms[0].new_tax_struct_dtl.value;
    			
    			if(main_tax_struct_dtl.trim()==new_tax_struct_dtl.trim())
    			{
    				msg += "New Tax Structure and Invoice Tax Structure shouldn't be same!\n";
    		        flag = false;
    			}
    		}
    		else if(chngin=="IMB" && criteria[i].checked)
    		{
    			var imb_qty = document.forms[0].imb_qty.value;
    			var imb_amt = document.forms[0].imb_amt.value;
    			
    			if(imb_qty.trim()=="")
    			{
    				msg += "Enter Imbalance Qty!\n";
    		        flag = false;
    			}
    			
    			if(imb_amt.trim()=="")
    			{
    				msg += "Enter Imbalance Amount!\n";
    		        flag = false;
    			}
    		}
    		else if(chngin=="SHIP" && criteria[i].checked)
    		{
    			var ship_or_pay_qty = document.forms[0].ship_or_pay_qty.value;
    			var ship_or_pay_amt = document.forms[0].ship_or_pay_amt.value;
    			
    			if(ship_or_pay_qty.trim()=="")
    			{
    				msg += "Enter Ship or Pay Qty!\n";
    		        flag = false;
    			}
    			
    			if(ship_or_pay_amt.trim()=="")
    			{
    				msg += "Enter Ship or Pay Amount!\n";
    		        flag = false;
    			}
    		}
    		else if(chngin=="UNAUTH" && criteria[i].checked)
    		{
    			var ovrun_qty = document.forms[0].ovrun_qty.value;
        		var ovrun_amt = document.forms[0].ovrun_amt.value;
    			
    			if(ovrun_qty.trim()=="")
    			{
    				msg += "Enter Unauthorized Overrun Qty!\n";
    		        flag = false;
    			}
    			
    			if(ovrun_amt.trim()=="")
    			{
    				msg += "Enter Unauthorized Overrun Amount!\n";
    		        flag = false;
    			}
    		}
    	}
    }
    
    if(criChkCnt==0)
    {
    	msg += "Please select atleast ONE(1) CR/DR Criteria!\n";
        flag = false;
    }
    
    if(contact_person=="0" || trim(contact_person) == "")
	{
		msg+="Select Customer Contact Person!\n";
		flag=false;
	}
	if(bu_contact_person=="0" || trim(bu_contact_person) == "")
	{
		msg+="Select Business Contact Person!\n";
		flag=false;
	}
	
	
	if(trim(price)=="")
	{
		msg+="Confirmed Price missing!\n";
		flag=false;
	}
	if(trim(gross_amt1)=="")
	{
		msg+="Gross Amount missing!\n";
		flag=false;
	}
	
	if(trim(tax_amt)=="")
	{
		msg+="Tax Amount missing!\n";
		flag=false;
	}
	if(trim(invoice_amt)=="")
	{
		msg+="Invoice Amount missing!\n";
		flag=false;
	}
	
	if(trim(net_payable)=="")
	{
		msg+="Net Payable missing!\n";
		flag=false;
	}
	else
	{
		if(crdr_gen_type!="CRDR_IMB")
		{
			if(parseFloat(net_payable) > 0 && invoice_type=="CR")
			{
				msg+="You are supose to generate Debit Note but you have selected Credit Note..Please change the CR/DR Type!\n";
				flag=false;
			}
			else if(parseFloat(net_payable) < 0 && invoice_type=="DR")
			{
				msg+="You are supose to generate Credit Note but you have selected Debit Note..Please change the CR/DR Type!\n";
				flag=false;
			}
		}
	}
	
	if(trim(tax_amt)!="" && trim(gross_include_transport_tariff) != "")
	{
		if(parseFloat(gross_amt1) >= 0 && parseFloat(tax_amt) < 0)
		{
			msg+="Sign(+/-) for Gross Amount and Tax Amount should be same!\n";
			flag=false;
		}
		
		if(parseFloat(gross_amt1) < 0 && parseFloat(tax_amt) >= 0)
		{
			msg+="Sign(+/-) for Gross Amount and Tax Amount should be same!\n";
			flag=false;
		}
	}
	
	if(invoice_type=="CR")
	{
		document.forms[0].remark1.value="";
	}
	
	
	if(crdr_gen_type=="CRDR_IMB")
	{
		var fileInput = document.getElementById('annexure_att');
		if (fileInput.files.length > 0) 
		{
			const fileName = fileInput.files[0].name; // Get the file name
			const maxLength = 50; // Set your desired max length
			if (fileName.length > maxLength) 
			{
				msg+="File name is too long! ("+fileName.length+" characters). Max allowed is "+maxLength+".";
				flag=false;
		    } 
		} 
		/* else 
		{
			alert('No file selected.');
		} */
	}
	
	if(operation=="CHECK")
	{
		var chk=document.forms[0].chk;
		if(!chk[0].checked && !chk[1].checked)
		{
			msg+="Plese Select either YES or NO !";
			flag=false;
		}
	}
	if(operation=="AUTHORIZE")
	{
		var auth=document.forms[0].auth;
		if(!auth[0].checked && !auth[1].checked)
		{
			msg+="Plese Select either YES or NO !";
			flag=false;
		}
	}
	if(operation=="APPROVE")
	{
		var aprv=document.forms[0].aprv;
		if(!aprv[0].checked && !aprv[1].checked)
		{
			msg+="Plese Select either YES or NO !";
			flag=false;
		}
	}
	
	if(split_flag=="Y" && parseFloat(document.forms[0].main_alloc_qty.value).toFixed(2)!=parseFloat(document.forms[0].changed_qty_mmbtu.value).toFixed(2) && !isChangeInQty)
	{
		msg+="⚠️ Quantity Changed Detected! Please Select Change In Quantity Criteria!";
		flag=false;
	}
	
	if(flag)
	{
		if(isChangeInQty)
		{
			var main_alloc_qty = document.forms[0].main_alloc_qty.value;
			var new_alloc_qty = document.forms[0].new_alloc_qty.value;
			
			if(new_alloc_qty.trim()==main_alloc_qty.trim() || new_alloc_qty.trim()=="" || new_alloc_qty.trim()=="0.00")
			{
			
				alert("⚠️ CR/DR Criteria - Change in Quantity - is Selected!\n\n" +
			              "Important: On Submit, Contract Allocation data modification will be allowed for Gas Date range: "+period_start_dt+" to " +period_end_dt +".\n\n" +
			              "Once Allocation data updated, use Modify option to Submit this CR/DR Note.\n\n" +
			              "Note: System will NOT allow Check or Approve this CR/DR Note, if NO Quantity Change detected!");	
			}
			
			if(split_flag=="Y")
			{
				alert("⚠️ "+couterpty_abbr+" Remittance "+sel_inv_no+" associeted with Split Deal "+deal_no+"\n\n"+
						"Note: Current CR/DR will Consider Change in Quantity as per Split Percentage in "+deal_no+" for "+couterpty_abbr+":\n\n"+split_invoice_list);
			}
		}
		
		if(operation=="MODIFY")
		{
			a=confirm("Do you want to Modify Credit/Debit Note?");
		}
		else if(operation=="CHECK")
		{
			a=confirm("Do you want to Check Credit/Debit Note?");
		}
		else if(operation=="AUTHORIZE")
		{
			a=confirm("Do you want to Authorize Credit/Debit Note?");
		}
		else if(operation=="APPROVE")
		{
			a=confirm("Do you want to Approve Credit/Debit Note?");
		}
		else
		{
			var a=confirm("Do you want to Submit Credit/Debit Note?");
		}
		
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			editAllowedOnCpStatus = true;
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}

}

function criteriFormula()
{
	var criteria = document.getElementsByName("criteria");
	var formula="";
	if(criteria.length > 0)
    {
    	for(var i=0; i<criteria.length; i++)
    	{
    		if(criteria[i].checked)
    		{
    			if(formula=="")
    			{
    				formula=criteria[i].value;
    			}
    			else
    			{
    				formula=formula+"#"+criteria[i].value;
    			}
    		}
    	}
    }
	
	document.forms[0].criteri_formula.value=formula;
}

function selectCriteria(formula)
{
	var criteria = document.getElementsByName("criteria");
	var split_formula = formula.split("#");
	
	for(var i=0; i<split_formula.length; i++)
	{
		var criteriaVal=split_formula[i];
		
		if(criteria.length > 0)
	    {
	    	for(var j=0; j<criteria.length; j++)
	    	{
	    		if(criteria[j].value == criteriaVal)
	    		{
	    			criteria[j].checked=true;
	    			enableNewValues(criteria[j]);
	    		}
	    	}
	    }
	}
}

function Do_close(msg,msg_type,accroid)
{
	window.opener.refershPar(msg,msg_type,accroid);
	window.close();
}

function openTaxStructMst()
{
	var type="P";
	var contract_type = document.forms[0].contract_type.value;
	if(contract_type=="O" || contract_type=="Q")
	{
		type="S";
	}
	var taxStructCd=document.forms[0].main_tax_cd.value;
	
	if(type=="")
	{
		alert("Select Invoice Category!")	
	}
	else
	{
		var newWindow;
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open("frm_purchase_crdr_tax_structure_list.jsp?type="+type+"&taxStructCd="+taxStructCd,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_purchase_crdr_tax_structure_list.jsp?type="+type+"&taxStructCd="+taxStructCd,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function setTaxStructDetail(tax_struct_cd,tax_struct_nm,tax_struct_eff_dt)
{
	var info = "Tax Config ("+tax_struct_nm+")";
	document.forms[0].tax_config_btn.value=info;
	
	document.forms[0].new_tax_cd.value=tax_struct_cd;
	//document.forms[0].new_tax_dt.value=tax_struct_eff_dt;
	calc();

}

function doDeleteAnnexure(annex_filenm,annex_folder)
{
	var a=confirm("Annexure File Name : "+annex_filenm+"\n\nDo you want to Delete?")
	if(a)
	{
		document.forms[0].att_file_name.value=annex_filenm;
		document.forms[0].annexure_folder.value=annex_folder;
		document.forms[0].option.value="DELETE_CRDR_ANNEXURE";
		document.getElementById("loading").style.visibility = "visible";
		editAllowedOnCpStatus = true;
		document.forms[0].submit();
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.remittance.DataBean_Remittance" id="pur_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();
int filter_start_year = CommonVariable.filter_start_year;

String operation=request.getParameter("operation")==null?"INSERT":request.getParameter("operation");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String sel_inv_no = request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
String invoice_type = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String crdr_gen_type = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
String sgpg_type = request.getParameter("sgpg_type")==null?"":request.getParameter("sgpg_type");
String crdr_no = request.getParameter("crdr_no")==null?"":request.getParameter("crdr_no");
String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
if(month.length() == 1)
{
	month="0"+month; 
}

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

pur_inv.setCallFlag("CRDR_PREPARATION_LIST");
pur_inv.setComp_cd(owner_cd);
pur_inv.setMonth(month);
pur_inv.setYear(year);
pur_inv.setCounterparty_cd(counterparty_cd);
pur_inv.setSel_inv_no(sel_inv_no);
pur_inv.setOperation(operation);
pur_inv.setSgpg_type(sgpg_type);
pur_inv.setFinancial_year(financial_year);
pur_inv.setInvoice_seq(invoice_seq);
pur_inv.setCrdr_gen_type(crdr_gen_type);
pur_inv.init();

Vector VCOUNTERPARTY_CD = pur_inv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = pur_inv.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = pur_inv.getVCOUNTERPARTY_ABBR();

Vector VINVOICE_NO_LIST = pur_inv.getVINVOICE_NO_LIST();
Vector VINVOICE_REF = pur_inv.getVINVOICE_REF();
Vector VCRITERIA_FLAG = pur_inv.getVCRITERIA_FLAG();
Vector VCRITERIA_NAME = pur_inv.getVCRITERIA_NAME();
Vector VCRITERIA_HIDE = pur_inv.getVCRITERIA_HIDE();

Vector VCONTACT_PERSON = pur_inv.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = pur_inv.getVCONTACT_PERSON_CD();
Vector VBU_CONTACT_PERSON = pur_inv.getVBU_CONTACT_PERSON();
Vector VBU_CONTACT_PERSON_CD = pur_inv.getVBU_CONTACT_PERSON_CD();

String criteri_formula = pur_inv.getCriteri_formula();

String couterpty_abbr=pur_inv.getCouterpty_abbr();
String couterpty_nm=pur_inv.getCouterpty_nm();
String cont_no = pur_inv.getCont_no();
String cont_rev_no = pur_inv.getCont_rev_no();
String agmt_no = pur_inv.getAgmt_no();
String agmt_rev_no = pur_inv.getAgmt_rev_no();
String cargo_no = pur_inv.getCargo_no();
String contract_type = pur_inv.getContract_type();
String deal_no=pur_inv.getDeal_no();
String contract_ref="";//pur_inv.getContract_ref();
plant_seq=pur_inv.getPlant_seq();
String plant_abbr=pur_inv.getPlant_abbr();
bu_unit=pur_inv.getBu_unit();
String bu_plant_abbr=pur_inv.getBu_plant_abbr();
String period_start_dt=pur_inv.getPeriod_start_dt();
String period_end_dt=pur_inv.getPeriod_end_dt();
String bu_contact_person_cd = pur_inv.getBu_contact_person_cd();
String contact_person_cd = pur_inv.getContact_person_cd();

String invoice_raised_in = pur_inv.getInvoice_raised_in();
String invoice_raised_in_nm = pur_inv.getInvoice_raised_in_nm();
String price_cd=pur_inv.getPrice_cd();
String price_cd_nm=pur_inv.getPrice_cd_nm();
String exchng_rate_cd=pur_inv.getExchng_rate_cd();
String exchang_rate_dt=pur_inv.getExchang_rate_dt();

String billing_freq = pur_inv.getBilling_freq();
String invoice_dt = pur_inv.getInvoice_dt();
String invoice_due_dt = pur_inv.getInvoice_due_dt();
String qty_mmbtu=pur_inv.getQty_mmbtu();
String price=pur_inv.getPrice();
String exchang_rate=pur_inv.getExchang_rate();
String gross_amt=pur_inv.getGross_amt();
String gross_amt1=pur_inv.getGross_amt1();
String transportation_charges = pur_inv.getTransportation_charges();
String transportation_amount = pur_inv.getTransportation_amount();
String marketing_margin = pur_inv.getMarketing_margin();
String marketing_margin_amount = pur_inv.getMarketing_margin_amount();
String other_charges = pur_inv.getOther_charges();
String other_charges_amount = pur_inv.getOther_charges_amount();
String gross_include_transport_tariff = pur_inv.getGross_include_transport_tariff();
String tax_amt = pur_inv.getTax_amt();
String tax_struct_cd=pur_inv.getTax_struct_cd();
String tax_struct_dt=pur_inv.getTax_struct_dt();
String tax_struct_dtl=pur_inv.getTax_struct_dtl();
String tax_info = pur_inv.getTax_info();
String tax_factor = pur_inv.getTax_factor();
String invoice_amt = pur_inv.getInvoice_amt();
String net_payable = pur_inv.getNet_payable();
String invoice_ref = pur_inv.getInvoice_ref();

String applicable_amt = "";//pur_inv.getApplicable_amt();

String applicable_flag = pur_inv.getApplicable_flag();
String applicable_abbr = pur_inv.getApplicable_abbr();
String tcs_factor = pur_inv.getTcs_factor();
String tcs_amount = pur_inv.getTcs_amount();
String tcs_struct_cd=pur_inv.getTcs_struct_cd();
String tcs_struct_dt=pur_inv.getTcs_struct_dt();
String tds_factor = pur_inv.getTds_factor();
String tds_amount = pur_inv.getTds_amount();
String tds_struct_cd=pur_inv.getTds_struct_cd();
String tds_struct_dt=pur_inv.getTds_struct_dt();

String invoice_check_flag = pur_inv.getInvoice_check_flag();
String invoice_check_dt = pur_inv.getInvoice_check_dt();
String invoice_check_by = pur_inv.getInvoice_check_by();
String invoice_check_nm = pur_inv.getInvoice_check_nm();
String invoice_auth_flag = pur_inv.getInvoice_auth_flag();
String invoice_auth_dt = pur_inv.getInvoice_auth_dt();
String invoice_auth_by = pur_inv.getInvoice_auth_by();
String invoice_auth_nm = pur_inv.getInvoice_auth_nm();
String invoice_aprv_flag = pur_inv.getInvoice_aprv_flag();
String invoice_aprv_dt = pur_inv.getInvoice_aprv_dt();
String invoice_aprv_by = pur_inv.getInvoice_aprv_by();
String invoice_aprv_nm = pur_inv.getInvoice_aprv_nm();

String imb_qty="";//pur_inv.getImb_qty();
String imb_amt="";//pur_inv.getImb_amt();
String ship_or_pay_qty="";//pur_inv.getShip_or_pay_qty();
String ship_or_pay_amt="";//pur_inv.getShip_or_pay_amt();
String ovrun_qty="";//pur_inv.getOvrun_qty();
String ovrun_amt="";//pur_inv.getOvrun_amt();
String att_file_name="";//pur_inv.getAtt_file_name();

String main_invoice_dt =pur_inv.getMain_invoice_dt();
String main_invoice_due_dt =pur_inv.getMain_invoice_due_dt();
String main_qty_mmbtu=pur_inv.getMain_qty_mmbtu();
String main_price=pur_inv.getMain_price();
String main_exchang_rate=pur_inv.getMain_exchang_rate();
String main_gross_amt=pur_inv.getMain_gross_amt();
String main_gross_amt1=pur_inv.getMain_gross_amt1();
String main_transportation_charges =pur_inv.getMain_transportation_charges();
String main_transportation_amount =pur_inv.getMain_transportation_amount();
String main_marketing_margin =pur_inv.getMain_marketing_margin();
String main_marketing_margin_amount =pur_inv.getMain_marketing_margin_amount();
String main_other_charges =pur_inv.getMain_other_charges();
String main_other_charges_amount =pur_inv.getMain_other_charges_amount();
String main_gross_include_transport_tariff =pur_inv.getMain_gross_include_transport_tariff();
String main_tax_amt =pur_inv.getMain_tax_amt();
String main_tax_struct_cd=pur_inv.getMain_tax_struct_cd();
String main_tax_struct_dt=pur_inv.getMain_tax_struct_dt();
String main_tax_struct_dtl=pur_inv.getMain_tax_struct_dtl();
String main_tax_info ="";//pur_inv.getMain_tax_info();
String main_tax_factor ="";//pur_inv.getMain_tax_factor();
String main_invoice_amt =pur_inv.getMain_invoice_amt();
String main_net_payable =pur_inv.getMain_net_payable();
String main_tds_amount =pur_inv.getMain_tds_amount();
String main_tds_factor =pur_inv.getMain_tds_factor();
String main_tds_struct_cd =pur_inv.getMain_tds_struct_cd();
String main_tds_struct_dt =pur_inv.getMain_tds_struct_dt();
String main_tcs_amount =pur_inv.getMain_tcs_amount();
String main_tcs_factor =pur_inv.getMain_tcs_factor();
String main_tcs_struct_cd =pur_inv.getMain_tcs_struct_cd();
String main_tcs_struct_dt =pur_inv.getMain_tcs_struct_dt();
String main_invoice_ref =pur_inv.getMain_invoice_ref();

String new_qty_mmbtu=pur_inv.getNew_qty_mmbtu();
String new_price=pur_inv.getNew_price();
String new_exchang_rate=pur_inv.getNew_exchang_rate();
String new_gross_amt=pur_inv.getNew_gross_amt();
String new_gross_amt1=pur_inv.getNew_gross_amt1();
String new_transportation_charges =pur_inv.getNew_transportation_charges();
String new_transportation_amount =pur_inv.getNew_transportation_amount();
String new_marketing_margin =pur_inv.getNew_marketing_margin();
String new_marketing_margin_amount =pur_inv.getNew_marketing_margin_amount();
String new_other_charges =pur_inv.getNew_other_charges();
String new_other_charges_amount =pur_inv.getNew_other_charges_amount();
String new_gross_include_transport_tariff =pur_inv.getNew_gross_include_transport_tariff();
String new_tax_amt =pur_inv.getNew_tax_amt();
String new_tax_struct_cd=pur_inv.getNew_tax_struct_cd();
String new_tax_struct_dt=pur_inv.getNew_tax_struct_dt();
String new_tax_struct_dtl=pur_inv.getNew_tax_struct_dtl();
String new_tax_info = pur_inv.getNew_tax_info();
String new_tax_factor = pur_inv.getNew_tax_factor();
String new_invoice_amt =pur_inv.getNew_invoice_amt();
String new_net_payable =pur_inv.getNew_net_payable();
String new_tds_amount =pur_inv.getNew_tds_amount();
String new_tds_factor =pur_inv.getNew_tds_factor();
String new_tds_struct_cd =pur_inv.getNew_tds_struct_cd();
String new_tds_struct_dt =pur_inv.getNew_tds_struct_dt();
String new_tcs_amount =pur_inv.getNew_tcs_amount();
String new_tcs_factor =pur_inv.getNew_tcs_factor();
String new_tcs_struct_cd =pur_inv.getNew_tcs_struct_cd();
String new_tcs_struct_dt =pur_inv.getNew_tcs_struct_dt();

String split_flag =pur_inv.getSplit_flag();
String split_value =pur_inv.getSplit_value();
String split_invoice_list =pur_inv.getSplit_invoice_list();

String changed_qty_mmbtu = pur_inv.getChanged_qty_mmbtu();
String remark_1=pur_inv.getRemark_1();
sgpg_type=pur_inv.getSgpg_type();

String invTypeNm="";
if(invoice_type.equals("CR"))
{
	invTypeNm="Credit Note";
}
else if(invoice_type.equals("DR"))
{
	invTypeNm="Debit Note";
}

boolean isMainGrossIncTriff = pur_inv.getIsMainGrossIncTriff();
boolean isGrossIncTriff = pur_inv.getIsGrossIncTriff();
boolean isNewGrossIncTriff = pur_inv.getIsNewGrossIncTriff();
Vector VMULTI_TAX_STRUCT = pur_inv.getVMULTI_TAX_STRUCT();
Vector VMAIN_MULTI_TAX_STRUCT = pur_inv.getVMAIN_MULTI_TAX_STRUCT();
Vector VNEW_MULTI_TAX_STRUCT = pur_inv.getVNEW_MULTI_TAX_STRUCT();

String annexure_path="../"+CommonVariable.work_dir+owner_cd+""+CommonVariable.crdr_annexure_path+"";

%>
<body onload="<%if(!msg.equals("")){ %>Do_close('<%=msg%>','<%=msg_type%>','<%=accroid%>');<%} %> 
<%if(!criteri_formula.equals("")) {%>selectCriteria('<%=criteri_formula%>');<%}%> 
<%if(criteri_formula.contains("QTY")) {%>calc();<%}%>
<%-- <%if(operation.equals("MODIFY")) {%>getBankDetail(document.forms[0].invoice_dt);<%} %> --%>"
<%if(operation.equals("VIEW") || operation.equals("CHECK") || operation.equals("AUTHORIZE") || operation.equals("APPROVE")) {%>style="pointer-events: none;"<%} %>>

<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_Remittance">
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
					    	Purchase Credit/Debit Detail
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row" <%if(operation.equals("MODIFY")){%>style="pointer-events: none;"<%}%>>
						<div class="col-sm-6 col-xs-2 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Month/Year</b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="month" onchange="refresh('<%=operation%>');">
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
									<script>document.forms[0].month.value="<%=month%>"</script>
								</div>
								<div class="col">
					  				<select class="form-select form-select-sm" name="year" onchange="refresh('<%=operation%>');">
					  					<%for(int i=(currentYear+1); i > filter_start_year;i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
					  		</div>
						</div>
						<div class="col-sm-6 col-xs-2 col-md-4">
							<div class="form-group row">
								<div class="col-auto">  	  	
					    			<label class="form-label"><b>Select Trader</b></label>
					  			</div>
								<div class="col">  
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=operation%>');">
										<option value="">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-xs-2 col-md-4">	
							<div class="form-group row">
								<div class="col-auto">
						    		<label class="form-label"><b>Invoice No</b></label>
						  		</div>				  	
								<div class="col"> 
									<select class="form-select form-select-sm searchable-select" name="sel_inv_no" onchange="refresh('<%=operation%>');">
										<option value="">--Select--</option>
										<%for (int i=0;i<VINVOICE_NO_LIST.size();i++){ %>
										<option value="<%=VINVOICE_NO_LIST.elementAt(i)%>"><%=VINVOICE_NO_LIST.elementAt(i)%> (<%=VINVOICE_REF.elementAt(i) %>)</option>										
										<%} %> 
									</select>
									<script>document.forms[0].sel_inv_no.value="<%=sel_inv_no%>"</script>								
								</div>
							</div>	
						</div>	
					  	<div class="col-sm-1 col-xs-1 col-md-1"></div>
					</div>
				</div>
				<%if(!sel_inv_no.equals("")) {%>
				<div class="card-body cdbody">
					<div class="row m-b-5"> <%-- <%if(operation.equals("MODIFY")){%>style="pointer-events: none;"<%}%>> --%>
						<div class="col-sm-6 col-xs-2 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>CR/DR Type</b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="invoice_type" onchange="changeTblhed(this);"> <%-- onchange="refresh('<%=operation%>');"> --%>
					  					<option value="">--Select--</option>
										<option value="CR">Credit Note</option>
										<option value="DR">Debit Note</option>
									</select>
									<script>document.forms[0].invoice_type.value="<%=invoice_type%>"</script>
								</div>
					  		</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>CR/DR Criteria</b></label>
					  			</div>
					  			<%for(int i=0;i<VCRITERIA_FLAG.size();i++){ %>
					  			<div class="col-auto" <%if(VCRITERIA_HIDE.elementAt(i).equals("Y")) {%>style="display:none;"<%} %>>
 				  					<label class="form-label">
 				  						<input type="checkbox" class="form-check-input" name="criteria" id="criteria_<%=VCRITERIA_FLAG.elementAt(i)%>" 
 				  							value="<%=VCRITERIA_FLAG.elementAt(i)%>" 
 				  							<%if(crdr_gen_type.equals("CRDR_IMB")) {%>
 				  							onclick="enableNewValues(this);criteriFormula();imbShipCalc();"
 				  							<%}else{ %>
 				  							onclick="enableNewValues(this);criteriFormula();calc();"
 				  							<%} %>>
 				  						<span style="background:#e6e6ff;">&nbsp;<%=VCRITERIA_NAME.elementAt(i) %></span>
 				  					</label>
				  				</div>
				  				<%} %>
					  		</div>
						</div>
					</div>
				</div>
				<%-- <%if(!invoice_type.equals("")) {%> --%>
					<div class="card-body cdbody">
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="table-responsive">
									<table class="table table-bordered table-hover">
										<thead>
											<tr>
												<th colspan="3">Trader Detail</th>
												<%if(contract_type.equals("N") && !cargo_no.equals("")){ %>
												<th rowspan="2">Cargo No<br>[Cargo Ref#]</th>
												<%}else{%>
												<th rowspan="2">Contract No<br>[Contract/Trade Ref#]</th>
												<%} %>
												<%if(contract_type.equals("N") && !cargo_no.equals("")){ %>
												<th rowspan="2">BOE#</th>
												<%}%>
												<th rowspan="2">Billing Period</th>
												<th colspan="2"><%=owner_abbr%> Detail</th>
											</tr>
											<tr>
												<th>Trader</th>
												<th>Plant</th>
												<th>Contact Person<span class="s-red">*</span></th>
												<th>Business Unit</th>
												<th>Contact Person<span class="s-red">*</span></th>
												
											</tr>
										</thead>
										<tbody>
											<tr>
												<td align="center">
													<%=couterpty_abbr%>
												</td>
												<td align="center"><%=plant_abbr%></td>
												<td align="center">
													<div style="width:150px;">
														<select class="form-select form-select-sm" name="contact_person">
															<option value="0">--Select--</option>
															<%for(int i=0;i<VCONTACT_PERSON_CD.size();i++){ %>
															<option value="<%=VCONTACT_PERSON_CD.elementAt(i)%>"><%=VCONTACT_PERSON.elementAt(i)%></option>
															<%} %>
														</select>
														<script>document.forms[0].contact_person.value="<%=contact_person_cd%>"</script>
													</div>
												</td>
												<td align="center"><%=deal_no%><%if(split_flag.equals("Y")){ %><font style="background:#ff99ff;">[Split <%=split_value %>%]</font><%} %></td>
												<%if(contract_type.equals("N") && !cargo_no.equals("")){ %>
												<td align="center"><%//=boe_nm%></td>
												<%} %>
												<td align="center"><%=period_start_dt%> - <%=period_end_dt%></td>
												<td align="center">
													<%=bu_plant_abbr%>
												</td>
												<td align="center">
													<div style="width:150px;">
														<select class="form-select form-select-sm" name="bu_contact_person">
															<option value="0">--Select--</option>
															<%for(int i=0;i<VBU_CONTACT_PERSON_CD.size();i++){ %>
															<option value="<%=VBU_CONTACT_PERSON_CD.elementAt(i)%>"><%=VBU_CONTACT_PERSON.elementAt(i)%></option>
															<%} %>
														</select>
														<script>document.forms[0].bu_contact_person.value="<%=bu_contact_person_cd%>"</script>
													</div>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					
					<div class="card-body cdbody">
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="table-responsive">
									<table class="table table-bordered table-hover">
										<thead>
											<tr>
												<th></th>
												<th><%=sel_inv_no%> [A]</th>
												<th>Revised Invoice [B]</th>
												<th id="th_invType"><%if(invoice_type.equals("CR")) {%>Credit Note<%}else if(invoice_type.equals("DR")){ %>Debit Note<%} %> [B-A]</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td><b>Invoice#<span class="s-red">*</span></b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm" name="main_invoice_ref" value="<%=main_invoice_ref%>" maxLength="50" autocomplete="off" disabled readonly>
							      						</div>
						      						</div>
												</td>
												<td align="center"></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm" name="invoice_ref" value="<%=invoice_ref%>" maxLength="50" autocomplete="off">
							      						</div>
						      						</div>
												</td>
											</tr>
											<tr>
												<td><b>Invoice Date<span class="s-red">*</span></b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="main_invoice_dt" value="<%=main_invoice_dt%>" maxLength="10" autocomplete="off" disabled readonly>
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      						</div>
						      						</div>
												</td>
												<td align="center"></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="invoice_dt" value="<%=invoice_dt%>" maxLength="10" 
								      						onblur="validateDate(this);" 
								      						onchange="validateDate(this);" autocomplete="off"><!-- getBankDetail(this) -->
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      						</div>
							      						<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
							      						<input type="hidden" name="exist_invoice_seq" value="<%=invoice_seq%>">
							      						<input type="hidden" name="financial_year" value="<%=financial_year%>">
						      						</div>
												</td>
											</tr>
											<tr>
												<td><b>Invoice Due Date<span class="s-red">*</span></b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="main_invoice_due_dt" value="<%=main_invoice_due_dt%>" maxLength="10" autocomplete="off" disabled readonly>
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      						</div>
							      					</div>
												</td>
												<td align="center"></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="invoice_due_dt" value="<%=invoice_due_dt%>" maxLength="10" 
								      						onblur="validateDate(this);" 
								      						onchange="validateDate(this);" autocomplete="off">
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      						</div>
							      					</div>
												</td>
											</tr>
											<tr>
												<td><b>Allocated Qty</b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_alloc_qty" value="<%=main_qty_mmbtu%>" readOnly>
															<span class="input-group-text">MMBTU</span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_alloc_qty" id="new_alloc_qty" value="<%=new_qty_mmbtu %>" onblur="negNumber(this);calc();" readOnly>
															<span class="input-group-text">MMBTU</span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="alloc_qty" value="<%=qty_mmbtu%>" readOnly>
															<span class="input-group-text">MMBTU</span>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td><b>Confirmed Price<span class="s-red">*</span></b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_price" value="<%=main_price%>" readOnly>
															<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
														</div>
														<input type="hidden" class="form-control form-control-sm" name="main_price_cd" value="<%=price_cd%>">
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_price" value="<%=new_price%>" 
															onblur="negNumber(this);<%if(price_cd.equals("2")){ %>checkNumber1(this,6,4);<%}else{%>checkNumber1(this,6,2);<%}%>calc();" readOnly>
															<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="price" value="<%=price%>" readOnly>
															<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
														</div>
														<input type="hidden" class="form-control form-control-sm" name="price_cd" value="<%=price_cd%>">
													</div>
												</td>
											</tr>
											<tr <%if(!price_cd.equals(invoice_raised_in)){ %><%}else{ %>style="display:none;"<%} %>>
												<td><b>Gross Amount</b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_gross_amt" value="<%=main_gross_amt%>" readOnly>
															<span class="input-group-text"><%=price_cd_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_gross_amt" value="<%=new_gross_amt%>" readOnly>
															<span class="input-group-text"><%=price_cd_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="gross_amt" value="<%=gross_amt%>" readOnly>
															<span class="input-group-text"><%=price_cd_nm%></span>
														</div>
													</div>
												</td>
											</tr>
											<tr <%if(!price_cd.equals(invoice_raised_in)){ %><%}else{ %>style="display:none;"<%} %>>
												<td><b><span>Exchange Rate</span></b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_exchng_rate" value="<%=main_exchang_rate%>" readOnly>
															<span class="input-group-text">INR/USD</span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_exchng_rate" value="<%=new_exchang_rate %>" 
															onblur="negNumber(this);checkNumber1(this,7,4);calc();" readOnly>
															<span class="input-group-text">INR/USD</span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="exchng_rate" value="<%=exchang_rate %>" readOnly>
															<span class="input-group-text">INR/USD</span>
														</div>
														<input type="hidden" name="exchng_cd" value="<%=exchng_rate_cd%>">
														<input type="hidden" name="exchng_dt" value="<%=exchang_rate_dt%>"> 
													</div>
												</td>
											</tr>
											<tr>
												<td><b>Gross Amount<span class="s-red">*</span></b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_gross_amt1" value="<%=main_gross_amt1%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
														<input type="hidden" class="form-control form-control-sm" name="main_invoice_raised_in" value="<%=invoice_raised_in%>">
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_gross_amt1" value="<%=new_gross_amt1%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="gross_amt1" value="<%=gross_amt1%>" onchange="difference('G')">
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
														<input type="hidden" class="form-control form-control-sm" name="invoice_raised_in" value="<%=invoice_raised_in%>">
													</div>
												</td>
											</tr>
											<tr <%if(main_transportation_charges.equals("")){ %>style="display:none;"<%} %>>
												<td><b>Transportation Tariff (<%=main_transportation_charges%> INR/MMBTU)<span class="s-red">*</span></b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_transportation_amount" value="<%=main_transportation_amount%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
														<input type="hidden" class="form-control form-control-sm" name="main_transportation_tariff" value="<%=main_transportation_charges%>">
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_transportation_tariff" value="<%=new_transportation_charges%>" 
															onblur="negNumber(this);checkNumber1(this,7,4);calc();" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%>/MMBTU</span>
														</div>
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_transportation_amount" value="<%=new_transportation_amount %>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="transportation_tariff" value="<%=transportation_charges%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%>/MMBTU</span>
														</div>
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="transportation_amount" value="<%=transportation_amount %>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
											</tr>
											<tr <%if(main_marketing_margin.equals("")){ %>style="display:none;"<%} %>>
												<td><b>Marketing Margin (<%=main_marketing_margin%> INR/MMBTU)<span class="s-red">*</span></b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_marketing_margin_amount" value="<%=main_marketing_margin_amount%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
														<input type="hidden" class="form-control form-control-sm" name="main_marketing_margin" value="<%=main_marketing_margin%>">
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_marketing_margin" value="<%=new_marketing_margin%>" 
															onblur="negNumber(this);checkNumber1(this,7,4);calc();" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%>/MMBTU</span>
														</div>
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_marketing_margin_amount" value="<%=new_marketing_margin_amount %>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="marketing_margin" value="<%=marketing_margin%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%>/MMBTU</span>
														</div>
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="marketing_margin_amount" value="<%=marketing_margin_amount%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
											</tr>
											<tr <%if(main_other_charges.equals("")){ %>style="display:none;"<%} %>>
												<td><b>Other Charges (<%=main_other_charges%> INR/MMBTU)<span class="s-red">*</span></b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_other_charges_amount" value="<%=main_other_charges_amount%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
														<input type="hidden" class="form-control form-control-sm" name="main_other_charges" value="<%=main_other_charges%>">
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_other_charges" value="<%=new_other_charges%>" 
															onblur="negNumber(this);checkNumber1(this,7,4);calc();" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%>/MMBTU</span>
														</div>
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_other_charges_amount" value="<%=new_other_charges_amount%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="other_charges" value="<%=other_charges%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%>/MMBTU</span>
														</div>
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="other_charges_amount" value="<%=other_charges_amount%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
											</tr>
											<tr <%if(!isMainGrossIncTriff){ %>style="display:none;"<%} %>>
												<td><b>Total Gross Amount<span class="s-red">*</span></b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_gross_include_transport_tariff" value="<%=main_gross_include_transport_tariff%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_gross_include_transport_tariff" value="<%=new_gross_include_transport_tariff%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="gross_include_transport_tariff" value="<%=gross_include_transport_tariff %>" onchange="difference('G')">
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td><b>Tax (<%=main_tax_struct_dtl%>)</b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_tax_amt" value="<%=main_tax_amt%>" title="<%=main_tax_info%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
														<input type="hidden" name="main_tax_cd" value="<%=main_tax_struct_cd%>">
														<input type="hidden" name="main_tax_dt" value="<%=main_tax_struct_dt%>">
														<input type="hidden" name="main_tax_factor" value="<%=main_tax_factor%>">
														<input type="hidden" name="main_tax_struct_dtl" value="<%=main_tax_struct_dtl%>">
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div>
															<input type="button" class="btn btn-sm config_btn" name="tax_config_btn" value="Tax Config (<%=new_tax_struct_dtl%>)" onclick="openTaxStructMst();" disabled>
														</div>
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_tax_amt" value="<%=new_tax_amt%>" title="" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
														<input type="hidden" name="new_tax_cd" value="<%=new_tax_struct_cd%>">
														<input type="hidden" name="new_tax_dt" value="<%=new_tax_struct_dt%>">
														<input type="hidden" name="new_tax_factor" value="<%=new_tax_factor%>">
														<input type="hidden" name="new_tax_struct_dtl" value="<%=new_tax_struct_dtl%>">
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="tax_amt" value="<%=tax_amt%>" title="" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
														<input type="hidden" name="tax_cd" value="<%=tax_struct_cd%>">
														<input type="hidden" name="tax_dt" value="<%=tax_struct_dt%>">
														<input type="hidden" name="tax_factor" value="<%=tax_factor%>">
														<input type="hidden" name="tax_struct_dtl" value="<%=tax_struct_dtl%>">
													</div>
												</td>
											</tr>
											<%if(VMAIN_MULTI_TAX_STRUCT.size()>0){
												for(int i=0;i<VMAIN_MULTI_TAX_STRUCT.size();i++)
												{
													Vector main_temp =(Vector)((Vector)((Vector)VMAIN_MULTI_TAX_STRUCT.elementAt(i)));
													
													Vector new_temp =(Vector)((Vector)((Vector)VNEW_MULTI_TAX_STRUCT.elementAt(i)));
													
													Vector temp =(Vector)((Vector)((Vector)VMULTI_TAX_STRUCT.elementAt(i)));
													
													for(int j=0;j<((Vector) main_temp.elementAt(0)).size(); j++)
													{%>
														<tr style="background: #cff4fc;"><%-- <%if(((Vector) temp.elementAt(0)).size()==1){%>display:none;<%} %>" > --%>
															<td align="right"><b><%=((Vector) main_temp.elementAt(1)).elementAt(j)%></b></td>
															<td align="center">
																<div style="width:200px;">
																	<div class="input-group input-group-sm" >
																		<input type="text" class="form-control form-control-sm" name="main_sub_tax_amt" value="<%=((Vector) main_temp.elementAt(2)).elementAt(j)%>" readOnly>
																		<span class="input-group-text"><%=invoice_raised_in_nm%></span>
																	</div>
																	<%-- <input type="hidden" name="sub_tax_code" value="<%=((Vector) main_temp.elementAt(0)).elementAt(j)%>">
																	<input type="hidden" name="sub_tax_struct" value="<%=((Vector) main_temp.elementAt(1)).elementAt(j)%>">
																	<input type="hidden" name="sub_tax_base_amt" value="<%=((Vector) main_temp.elementAt(3)).elementAt(j)%>"> --%>
																</div>
															</td>
															<td align="center">
																<div style="width:200px;">
																	<div class="input-group input-group-sm" >
																		<input type="text" class="form-control form-control-sm" name="new_sub_tax_amt" value="<%=((Vector) new_temp.elementAt(2)).elementAt(j)%>" readOnly>
																		<span class="input-group-text"><%=invoice_raised_in_nm%></span>
																	</div>
																	<input type="hidden" name="new_sub_tax_code" value="<%=((Vector) new_temp.elementAt(0)).elementAt(j)%>">
																	<input type="hidden" name="new_sub_tax_struct" value="<%=((Vector) new_temp.elementAt(1)).elementAt(j)%>">
																	<input type="hidden" name="new_sub_tax_base_amt" value="<%=((Vector) new_temp.elementAt(3)).elementAt(j)%>">
																</div>
															</td>
															<td align="center">
																<div style="width:200px;">
																	<div class="input-group input-group-sm" >
																		<input type="text" class="form-control form-control-sm" name="sub_tax_amt" value="<%=((Vector) temp.elementAt(2)).elementAt(j)%>" onchange="difference('T')">
																		<span class="input-group-text"><%=invoice_raised_in_nm%></span>
																	</div>
																	<input type="hidden" name="sub_tax_code" value="<%=((Vector) temp.elementAt(0)).elementAt(j)%>">
																	<input type="hidden" name="sub_tax_struct" value="<%=((Vector) temp.elementAt(1)).elementAt(j)%>">
																	<input type="hidden" name="sub_tax_base_amt" value="<%=((Vector) temp.elementAt(3)).elementAt(j)%>">
																</div>
															</td>
														</tr>
													<%}
												}
											} %>
											<tr>
												<td><b>Invoice Amount</b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_invoice_amt" value="<%=main_invoice_amt%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_invoice_amt" value="<%=new_invoice_amt%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="invoice_amt" value="<%=invoice_amt%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
											</tr>
											
											<tr <%if(!applicable_abbr.equals("TCS")){ %>style="display:none;"<%} %>>
												<td><b>TCS (<%=main_tcs_factor%>%)</b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_tcs_amt" value="<%=main_tcs_amount%>" readOnly>
															<input type="hidden" class="form-control form-control-sm" name="main_tcs_factor" value="<%=main_tcs_factor%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_tcs_amt" value="<%=new_tcs_amount%>" readOnly>
															<input type="hidden" name="new_tcs_factor" value="<%=main_tcs_factor%>" readOnly>
															<input type="hidden" name="new_tcs_struct_cd" value="<%=main_tcs_struct_cd%>">
															<input type="hidden" name="new_tcs_struct_dt" value="<%=main_tcs_struct_dt%>">
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="tcs_amt" value="<%=tcs_amount%>" readOnly>
															<input type="hidden" class="form-control form-control-sm" name="tcs_factor" value="<%=main_tcs_factor%>" readOnly>
															<input type="hidden" name="tcs_struct_cd" value="<%=main_tcs_struct_cd%>">
															<input type="hidden" name="tcs_struct_dt" value="<%=main_tcs_struct_dt%>">
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
											</tr>
											
											<tr>
												<td><b>Net Payable</b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_net_payable" value="<%=main_net_payable%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_net_payable" value="<%=new_net_payable%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="net_payable" value="<%=net_payable%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td colspan="4">&nbsp;</td>
											</tr>
											<tr <%if(!applicable_abbr.equals("TDS")){ %>style="display:none;"<%} %>>
												<td><b>TDS (<%=main_tds_factor%>%)</b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_tds_amt" value="<%=main_tds_amount%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
															<input type="hidden" name="main_tds_factor" value="<%=main_tds_factor%>" readOnly>
															<input type="hidden" name="main_tds_struct_cd" value="<%=main_tds_struct_cd%>">
															<input type="hidden" name="main_tds_struct_dt" value="<%=main_tds_struct_dt%>">
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_tds_amt" value="<%=new_tds_amount%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
														<%-- <input type="hidden" name="new_tds_factor" value="<%=new_tds_factor%>" readOnly>
														<input type="hidden" name="new_tds_struct_cd" value="<%=new_tds_struct_cd%>">
														<input type="hidden" name="new_tds_struct_dt" value="<%=new_tds_struct_dt%>"> --%>
														<input type="hidden" name="new_tds_factor" value="<%=main_tds_factor%>" readOnly>
														<input type="hidden" name="new_tds_struct_cd" value="<%=main_tds_struct_cd%>">
														<input type="hidden" name="new_tds_struct_dt" value="<%=main_tds_struct_dt%>">
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="tds_amt" value="<%=tds_amount%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
															<%-- <input type="hidden" name="tds_factor" value="<%=tds_factor%>" readOnly>
															<input type="hidden" name="tds_struct_cd" value="<%=tds_struct_cd%>">
															<input type="hidden" name="tds_struct_dt" value="<%=tds_struct_dt%>"> --%>
															<input type="hidden" name="tds_factor" value="<%=main_tds_factor%>" readOnly>
															<input type="hidden" name="tds_struct_cd" value="<%=main_tds_struct_cd%>">
															<input type="hidden" name="tds_struct_dt" value="<%=main_tds_struct_dt%>">
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td><b>Remark-1</b></td>
												<td align="center" colspan="3">
													<!-- <div style="width:400px;"> -->
														<textarea class="form-control form-control-sm" rows="3" cols="75" name="remark1"><%=remark_1%></textarea>
													<!-- </div> -->
												</td>
											</tr>
											<%if(operation.equals("CHECK")){ %>
											<tr><td colspan="4">&nbsp;</td></tr>
											<tr style="<%if(!operation.equals("CHECK")){%>display:none;<%}else{ %>pointer-events: auto;<%}%>">
												<td><b>Check <%=invTypeNm%></b></td>
												<td align="center" colspan="3">
													<div style="width:250px;">
														<div class="form-group row">
															<div class="col-auto"> 
																<span class="pillbox yesRdBtn">	
																	<input type="radio" name="chk" value="Y" <%if(invoice_check_flag.equals("Y")){%>checked<%} %>>&nbsp;Yes
																</span>
															</div>
															<div class="col-auto">
																<span class="pillbox noRdBtn">	
																	<input type="radio" name="chk" value="N" <%if(invoice_check_flag.equals("N")){%>checked<%} %>>&nbsp;No
																</span>	
															</div>
															
														</div>
													</div>
													<%if(!invoice_check_flag.equals("")){ %>
														<font color="<%if(invoice_check_flag.equals("Y")){ %>green<%}else{%>red<%}%>"><%=invoice_check_dt%> <%=invoice_check_nm%> by <%=invoice_check_by%></font>
													<%} %>
												</td>
											</tr>
											<%}else if(operation.equals("AUTHORIZE")){ %>
											<tr><td colspan="4">&nbsp;</td></tr>
											<tr style="<%if(!operation.equals("AUTHORIZE")){%>display:none;<%}else{ %>pointer-events: auto;<%}%>">
												<td><b>Authorize <%=invTypeNm%></b></td>
												<td align="center" colspan="3">
													<div style="width:250px;">
														<div class="form-group row">
															<div class="col-auto"> 
																<span class="pillbox yesRdBtn">	
																	<input type="radio" name="auth" value="Y" <%if(invoice_auth_flag.equals("Y")){%>checked<%} %>>&nbsp;Yes
																</span>
															</div>
															<div class="col-auto">
																<span class="pillbox noRdBtn">	
																	<input type="radio" name="auth" value="N" <%if(invoice_auth_flag.equals("N")){%>checked<%} %>>&nbsp;No
																</span>	
															</div>
															
														</div>
													</div>
													<%if(!invoice_auth_flag.equals("")){ %>
														<font color="<%if(invoice_auth_flag.equals("Y")){ %>green<%}else{%>red<%}%>"><%=invoice_auth_dt%> <%=invoice_auth_nm%> by <%=invoice_auth_by%></font>
													<%} %>
												</td>
											</tr>
											<%} else if(operation.equals("APPROVE")){ %>
											<tr><td colspan="4">&nbsp;</td></tr>
											<tr style="<%if(!operation.equals("APPROVE")){%>display:none;<%}else{ %>pointer-events: auto;<%}%>">
												<td><b>Approve <%=invTypeNm%></b></td>
												<td align="center" colspan="3">
													<div style="width:250px;">
														<div class="form-group row">
															<div class="col-auto"> 
																<span class="pillbox yesRdBtn">	
																	<input type="radio" name="aprv" value="A" <%if(invoice_aprv_flag.equals("A")){%>checked<%} %>>&nbsp;Yes
																</span>
															</div>
															<div class="col-auto">
																<span class="pillbox noRdBtn">	
																	<input type="radio" name="aprv" value="R" <%if(invoice_aprv_flag.equals("R")){%>checked<%} %>>&nbsp;No
																</span>	
															</div>
															
														</div>
													</div>
													<%if(!invoice_aprv_flag.equals("")){ %>
														<font color="<%if(invoice_aprv_flag.equals("A")){ %>green<%}else{%>red<%}%>"><%=invoice_aprv_dt%> <%=invoice_aprv_nm%> by <%=invoice_aprv_by%></font>
													<%} %>
												</td>
											</tr>
											<%} %>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					<%if(!operation.equals("INSERT") && !operation.equals("VIEW")){ %>
					<div class="card-footer cdfooter text-center">
						<div class="d-flex justify-content-between">
							<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
							<%if(write_access.equals("Y")){ %>
							<div class="row justify-content-end">
								<!-- <div class="col-auto">
									<i class="fa fa-eye fa-2x" onclick="viewBeforeSubmit();"></i>
								</div> -->
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();" style="pointer-events: auto;">
								</div>
							</div>
							<%}else{ %>
							<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
							<%} %>
						</div>
					</div>
					<%} %>
					<%} %>
				<%-- <%} %> --%>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="GENERATE_CR_DR">
<input type="hidden" name="opration" value="<%=operation%>">
<input type="hidden" name="operation" value="<%=operation%>">

<input type="hidden" name="prev_month" value="<%=month%>">
<input type="hidden" name="prev_year" value="<%=year%>">
<input type="hidden" name="accroid" value="<%=accroid%>">
<input type="hidden" name="crdr_gen_type" value="<%=crdr_gen_type%>">
<input type="hidden" name="sgpg_type" value="<%=sgpg_type%>">
<input type="hidden" name="billing_freq" value="<%=billing_freq%>">
<input type="hidden" name="crdr_no" value="<%=crdr_no%>">
<input type="hidden" name="split_flag" value="<%=split_flag%>">
<input type="hidden" name="split_value" value="<%=split_value%>">
<input type="hidden" name="split_invoice_list" value="<%=split_invoice_list%>">
<input type="hidden" name="couterpty_abbr" value="<%=couterpty_abbr%>">
<input type="hidden" name="deal_no" value="<%=deal_no%>">

<%-- <input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>"> --%>
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" value="<%=agmt_rev_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev" value="<%=cont_rev_no%>">
<input type="hidden" name="cargo_no" value="<%=cargo_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="plant_seq" value="<%=plant_seq%>">
<input type="hidden" name="bu_unit" value="<%=bu_unit%>">
<input type="hidden" name="period_start_dt" value="<%=period_start_dt%>">
<input type="hidden" name="period_end_dt" value="<%=period_end_dt%>">
<input type="hidden" name="contract_ref" value="<%=contract_ref%>">

<input type="hidden" name="tcs_tds" value="<%=applicable_abbr%>">
<input type="hidden" name="applicable_flag" value="<%=applicable_flag%>">
<input type="hidden" name="criteri_formula" value="<%=criteri_formula%>">
<input type="hidden" name="changed_qty_mmbtu" value="<%=changed_qty_mmbtu%>">
<input type="hidden" name="temp_criteri_formula" value="<%=criteri_formula%>">

<input type="hidden" name="annexure_folder" value="">
<input type="hidden" name="att_file_name" value="">

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

<script>
async function calcExsitingTaxStructure(tax_struct_cd,gross_amt)
{
	if(tax_struct_cd!="" && gross_amt!="")
	{
		$.post("../servlet/DB_Remittance_Ajax?setCallType=CALC_EXISTING_TAX_DTL_FOR_CR_DR&tax_struct_cd="+tax_struct_cd+"&gross_amt="+gross_amt, function(responseJson) {
			console.log(responseJson);
			$.each(responseJson, function(index, json) 
			{
				$.each(json.TOTAL_TAX_DTL, function(index_1, json_1) {
					
					document.forms[0].new_tax_amt.value=json_1.TAX_AMT;
					document.forms[0].new_tax_cd.value=json_1.TAX_STRUCT_CD;
					document.forms[0].new_tax_struct_dtl.value=json_1.TAX_STRUCT_DTL;
					
					document.forms[0].tax_cd.value=json_1.TAX_STRUCT_CD;
					document.forms[0].tax_struct_dtl.value=json_1.TAX_STRUCT_DTL;
					
					document.forms[0].tax_config_btn.value="Tax Config \n("+json_1.TAX_STRUCT_DTL+")";
				});
				
				var idx = parseInt("0");
				$.each(json.SUB_TAX_DTL, function(index_1, subArray) {
					$.each(subArray, function(index_inner, json_1) {
						document.getElementsByName("new_sub_tax_amt")[idx].value=json_1.SUB_TAX_AMT;
						document.getElementsByName("new_sub_tax_struct")[idx].value=json_1.SUB_TAX_STRUCT_DTL;
						document.getElementsByName("new_sub_tax_code")[idx].value=json_1.SUB_TAX_CODE;
						document.getElementsByName("new_sub_tax_base_amt")[idx].value=json_1.SUB_TAX_BASE_AMT;
						
						document.getElementsByName("sub_tax_struct")[idx].value=json_1.SUB_TAX_STRUCT_DTL;
						document.getElementsByName("sub_tax_code")[idx].value=json_1.SUB_TAX_CODE;
						
						idx++;
						
						/*document.forms[0].new_sub_tax_amt.value=json_1.SUB_TAX_AMT;
						//document.forms[0].new_sub_tax_struct_cd.value=json_1.SUB_TAX_STRUCT_CD;
						document.forms[0].new_sub_tax_struct.value=json_1.SUB_TAX_STRUCT_DTL;
						document.forms[0].new_sub_tax_code.value=json_1.SUB_TAX_CODE;
						document.forms[0].new_sub_tax_base_amt.value=json_1.SUB_TAX_BASE_AMT;
						
						document.forms[0].sub_tax_struct.value=json_1.SUB_TAX_STRUCT_DTL;
						document.forms[0].sub_tax_code.value=json_1.SUB_TAX_CODE;
						//document.forms[0].new_sub_tax_base_amt.value=json_1.SUB_TAX_BASE_AMT;
						*/
						
						/* tax_row+="<div class='row m-b-5' "
						if(json.SUB_TAX_DTL.length == 1)
						{
							tax_row+="style='display:none;'"
						}
						tax_row+=">"
							+"<div class='col-sm-6 col-xs-6 col-md-6'>" 
							+"</div>"
							+"<div class='col-sm-2 col-xs-2 col-md-2'>" 
								+"<div class='form-group row' align='right'>"
									+"<label class='form-label'><b>"+json_1.SUB_TAX_STRUCT_DTL+"</b></label>"
								+"</div>"
							+"</div>"
							+"<div class='col-sm-4 col-xs-4 col-md-4'> " 
								+"<div class='form-group row'>"
									+"<div class='col-sm-12 col-xs-12 col-md-12'>"
										+"<input type='text' class='form-control form-control-sm' name='sub_tax_amt' value='"+json_1.SUB_TAX_AMT+"' onblur='negNumber(this);checkNumber1(this,11,2);taxCalc('');' "+readFlag+">"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_struct_cd' value='"+json_1.TAX_STRUCT_CD+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_struct_dtl' value='"+json_1.SUB_TAX_STRUCT_DTL+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_code' value='"+json_1.SUB_TAX_CODE+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_base_amt' value='"+json_1.SUB_TAX_BASE_AMT+"' readOnly>"
									+"</div>"
								+"</div>"
							+"</div>"
						+"</div>"; */
				    });
				});
				
				
			});
			
			document.getElementById("loading").style.visibility = "hidden";
		});
	}
}

async function calcExsitingTaxStructureforChange(tax_struct_cd,gross_amt)
{
	if(tax_struct_cd!="" && gross_amt!="")
	{
		$.post("../servlet/DB_Remittance_Ajax?setCallType=CALC_EXISTING_TAX_DTL_FOR_CR_DR&tax_struct_cd="+tax_struct_cd+"&gross_amt="+gross_amt, function(responseJson) {
			console.log(responseJson);
			$.each(responseJson, function(index, json) 
			{
				$.each(json.TOTAL_TAX_DTL, function(index_1, json_1) {
					
					document.forms[0].tax_amt.value=json_1.TAX_AMT;
					document.forms[0].tax_cd.value=json_1.TAX_STRUCT_CD;
					document.forms[0].tax_struct_dtl.value=json_1.TAX_STRUCT_DTL;
					
					document.forms[0].tax_cd.value=json_1.TAX_STRUCT_CD;
					document.forms[0].tax_struct_dtl.value=json_1.TAX_STRUCT_DTL;
					
					document.forms[0].tax_config_btn.value="Tax Config";
				});
				
				var idx = parseInt("0");
				$.each(json.SUB_TAX_DTL, function(index_1, subArray) {
					$.each(subArray, function(index_inner, json_1) {
						document.getElementsByName("sub_tax_amt")[idx].value=json_1.SUB_TAX_AMT;
						document.getElementsByName("sub_tax_struct")[idx].value=json_1.SUB_TAX_STRUCT_DTL;
						document.getElementsByName("sub_tax_code")[idx].value=json_1.SUB_TAX_CODE;
						document.getElementsByName("sub_tax_base_amt")[idx].value=json_1.SUB_TAX_BASE_AMT;
						
						document.getElementsByName("sub_tax_struct")[idx].value=json_1.SUB_TAX_STRUCT_DTL;
						document.getElementsByName("sub_tax_code")[idx].value=json_1.SUB_TAX_CODE;
						
						idx++;
						
						/*document.forms[0].new_sub_tax_amt.value=json_1.SUB_TAX_AMT;
						//document.forms[0].new_sub_tax_struct_cd.value=json_1.SUB_TAX_STRUCT_CD;
						document.forms[0].new_sub_tax_struct.value=json_1.SUB_TAX_STRUCT_DTL;
						document.forms[0].new_sub_tax_code.value=json_1.SUB_TAX_CODE;
						document.forms[0].new_sub_tax_base_amt.value=json_1.SUB_TAX_BASE_AMT;
						
						document.forms[0].sub_tax_struct.value=json_1.SUB_TAX_STRUCT_DTL;
						document.forms[0].sub_tax_code.value=json_1.SUB_TAX_CODE;
						//document.forms[0].new_sub_tax_base_amt.value=json_1.SUB_TAX_BASE_AMT;
						*/
						
						/* tax_row+="<div class='row m-b-5' "
						if(json.SUB_TAX_DTL.length == 1)
						{
							tax_row+="style='display:none;'"
						}
						tax_row+=">"
							+"<div class='col-sm-6 col-xs-6 col-md-6'>" 
							+"</div>"
							+"<div class='col-sm-2 col-xs-2 col-md-2'>" 
								+"<div class='form-group row' align='right'>"
									+"<label class='form-label'><b>"+json_1.SUB_TAX_STRUCT_DTL+"</b></label>"
								+"</div>"
							+"</div>"
							+"<div class='col-sm-4 col-xs-4 col-md-4'> " 
								+"<div class='form-group row'>"
									+"<div class='col-sm-12 col-xs-12 col-md-12'>"
										+"<input type='text' class='form-control form-control-sm' name='sub_tax_amt' value='"+json_1.SUB_TAX_AMT+"' onblur='negNumber(this);checkNumber1(this,11,2);taxCalc('');' "+readFlag+">"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_struct_cd' value='"+json_1.TAX_STRUCT_CD+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_struct_dtl' value='"+json_1.SUB_TAX_STRUCT_DTL+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_code' value='"+json_1.SUB_TAX_CODE+"' readOnly>"
										+"<input type='hidden' class='form-control form-control-sm' name='sub_tax_base_amt' value='"+json_1.SUB_TAX_BASE_AMT+"' readOnly>"
									+"</div>"
								+"</div>"
							+"</div>"
						+"</div>"; */
				    });
				});
				
				
			});
			
			document.getElementById("loading").style.visibility = "hidden";
		});
	}
}
</script>

</form>
</body>
</html>