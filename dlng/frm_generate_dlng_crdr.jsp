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
		var url = "frm_generate_dlng_crdr.jsp?u="+u+"&counterparty_cd="+counterparty_cd+
				"&month="+month+"&year="+year+"&operation="+operation+"&accroid="+accroid+"&sel_inv_no="+sel_inv_no;//+
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
		var changed_qty_mmbtu = document.forms[0].changed_qty_mmbtu.value;
		var main_alloc_qty = document.forms[0].main_alloc_qty.value;
		
		if(operation=="MODIFY" && !obj.checked)
		{
			if(changed_qty_mmbtu != main_alloc_qty && changed_qty_mmbtu.trim()!="")
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

/*function diffValue(flag)
{
	if(flag=="PRICE")
	{
		var new_price = document.forms[0].new_price.value;
		var main_price = document.forms[0].main_price.value;
		var price_cd = document.forms[0].price_cd.value;
		
		if(trim(new_price)!="" && trim(main_price)!="")
		{
			var decimalDigit="2";
			if(price_cd=="2")
			{
				decimalDigit="4";
			}
			var diff = parseFloat(parseFloat(main_price)-parseFloat(new_price)).toFixed(decimalDigit);
			document.forms[0].price.value=diff;
		}
		else
		{
			document.forms[0].price.value="";
		}
	}
	
	if(flag=="EXCHG")
	{
		var new_exchng_rate = document.forms[0].new_exchng_rate.value;
		var main_exchng_rate = document.forms[0].main_exchng_rate.value;
		
		if(trim(new_exchng_rate)!="" && trim(main_exchng_rate)!="")
		{
			var decimalDigit="4";
			var diff = parseFloat(parseFloat(main_exchng_rate)-parseFloat(new_exchng_rate)).toFixed(decimalDigit);
			document.forms[0].exchng_rate.value=diff;
		}
		else
		{
			document.forms[0].exchng_rate.value="";
		}
	}
	
	if(flag=="TC")
	{
		var new_transportation_tariff = document.forms[0].new_transportation_tariff.value;
		var main_transportation_tariff = document.forms[0].main_transportation_tariff.value;
		
		if(trim(new_transportation_tariff)!="" && trim(main_transportation_tariff)!="")
		{
			var decimalDigit="4";
			var diff = parseFloat(parseFloat(main_transportation_tariff)-parseFloat(new_transportation_tariff)).toFixed(decimalDigit);
			document.forms[0].transportation_tariff.value=diff;
		}
		else
		{
			document.forms[0].transportation_tariff.value="";
		}
	}
	
	if(flag=="MM")
	{
		var new_marketing_margin = document.forms[0].new_marketing_margin.value;
		var main_marketing_margin = document.forms[0].main_marketing_margin.value;
		
		if(trim(new_marketing_margin)!="" && trim(main_marketing_margin)!="")
		{
			var decimalDigit="2";
			var diff = parseFloat(parseFloat(main_marketing_margin)-parseFloat(new_marketing_margin)).toFixed(decimalDigit);
			document.forms[0].marketing_margin.value=diff;
		}
		else
		{
			document.forms[0].marketing_margin.value="";
		}
	}
	
	if(flag=="OC")
	{
		var new_other_charges = document.forms[0].new_other_charges.value;
		var main_other_charges = document.forms[0].main_other_charges.value;
		
		if(trim(new_other_charges)!="" && trim(main_other_charges)!="")
		{
			var decimalDigit="2";
			var diff = parseFloat(parseFloat(main_other_charges)-parseFloat(new_other_charges)).toFixed(decimalDigit);
			document.forms[0].other_charges.value=diff;
		}
		else
		{
			document.forms[0].other_charges.value="";
		}
	}
}*/

async function calc()
{
	var alloc_qty = document.forms[0].main_alloc_qty.value;
	
	var price = document.forms[0].new_price.value;
	var price_cd = document.forms[0].price_cd.value;
	var exchng_rate = document.forms[0].new_exchng_rate.value;
	
	var transportation_tariff = document.forms[0].new_transportation_tariff.value;
	var marketing_margin = document.forms[0].new_marketing_margin.value;
	var other_charges = document.forms[0].new_other_charges.value;
	
	var tcs_tds = document.forms[0].tcs_tds.value;
	
	var operation = document.forms[0].operation.value;
	
	if(trim(alloc_qty) != "")
	{
		if(document.getElementById("criteria_QTY").checked && (operation=="MODIFY" || operation=="INSERT"))
		{
			alloc_qty=document.forms[0].new_alloc_qty.value;
		}
		else
		{
			alloc_qty=document.forms[0].main_alloc_qty.value;
			document.forms[0].new_alloc_qty.value=alloc_qty;
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
		if(trim(price) != "")
		{
			gross=parseFloat(parseFloat(alloc_qty)*parseFloat(price)).toFixed(2);
		}
		else
		{
			gross="";
		}
		
		document.forms[0].new_gross_amt.value=gross;
		
		var gross1="";
		if(price_cd=="2")
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
			
			if(trim(exchng_rate)!="" && trim(gross) != "")
			{
				gross1=parseFloat(parseFloat(exchng_rate)*parseFloat(gross)).toFixed(2);
			}
			
			document.forms[0].new_gross_amt1.value=gross1;
		}
		else
		{
			document.forms[0].new_gross_amt1.value=gross;
			gross1=gross;
		}
		
		var tc="";
		var mm="";
		var oc="";
		
		/*if(document.getElementById("criteria_TC").checked)
		{
			transportation_tariff = document.forms[0].new_transportation_tariff.value;
		}
		else
		{
			transportation_tariff = document.forms[0].main_transportation_tariff.value;
			document.forms[0].new_transportation_tariff.value=transportation_tariff;
		}
		
		if(trim(transportation_tariff)!="")
		{
			tc=parseFloat(parseFloat(transportation_tariff)*parseFloat(alloc_qty)).toFixed(2);
			document.forms[0].new_transportation_amount.value=tc;
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
		
		if(trim(marketing_margin)!="")
		{
			mm=parseFloat(parseFloat(marketing_margin)*parseFloat(alloc_qty)).toFixed(2);
			document.forms[0].new_marketing_margin_amount.value=mm;
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
		
		if(trim(other_charges)!="")
		{
			oc=parseFloat(parseFloat(other_charges)*parseFloat(alloc_qty)).toFixed(2);
			document.forms[0].new_other_charges_amount.value=oc;
		}
		
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
		}*/
		
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
				document.forms[0].new_net_payable.value=invAmt;
				
				if(tcs_tds=="TDS" && gross1!="")
				{	
					var main_gross_amt1 = document.forms[0].main_gross_amt1.value;
					var main_tds_amt = document.forms[0].main_tds_amt.value;
					var tds_factor=document.forms[0].main_tds_factor.value;
					
					const diffGross = (parseFloat(gross1) - parseFloat(main_gross_amt1)).toFixed(2);
					
					var tdsAmt="";
					var temp_tdsAmt="";
					if(tds_factor != "")
					{
						temp_tdsAmt=((parseFloat(diffGross)*parseFloat(tds_factor))/100).toFixed(2);
						
						tdsAmt = (parseFloat(temp_tdsAmt)+parseFloat(main_tds_amt)).toFixed(2);
					}
					
					document.forms[0].new_tds_amt.value=tdsAmt;
				}
			}, 800);
		}
		
		window.setTimeout(function() 
		{
			difference();
		}, 800);
	}
}

function difference()
{
	/*var main_alloc_qty = document.forms[0].main_alloc_qty.value;
	var main_price = document.forms[0].main_price.value;
	var main_gross_amt=document.forms[0].main_gross_amt.value;
	var main_exchng_rate = document.forms[0].main_exchng_rate.value;
	var main_gross_amt1 = document.forms[0].main_gross_amt1.value;
	var main_transportation_tariff = document.forms[0].main_transportation_tariff.value;
	var main_transportation_amount = document.forms[0].main_transportation_amount.value;
	var main_marketing_margin = document.forms[0].main_marketing_margin.value;
	var main_marketing_margin_amount = document.forms[0].main_marketing_margin_amount.value;
	var main_other_charges = document.forms[0].main_other_charges.value;
	var main_other_charges_amount = document.forms[0].main_other_charges_amount.value;
	
	var new_alloc_qty = document.forms[0].new_alloc_qty.value;
	var new_price = document.forms[0].new_price.value;
	var new_gross_amt=document.forms[0].new_gross_amt.value;
	var new_exchng_rate = document.forms[0].new_exchng_rate.value;
	var new_gross_amt1 = document.forms[0].new_gross_amt1.value;
	var new_transportation_tariff = document.forms[0].new_transportation_tariff.value;
	var new_transportation_amount = document.forms[0].new_transportation_amount.value;
	var new_marketing_margin = document.forms[0].new_marketing_margin.value;
	var new_marketing_margin_amount = document.forms[0].new_marketing_margin_amount.value;
	var new_other_charges = document.forms[0].new_other_charges.value;
	var new_other_charges_amount = document.forms[0].new_other_charges_amount.value;
	*/
	/*if (main_alloc_qty !== "" && new_alloc_qty !== "") 
	{
	    const totalQty = parseFloat(main_alloc_qty) + parseFloat(new_alloc_qty);
	    document.forms[0].alloc_qty.value = totalQty.toFixed(2);
	}*/
	
	var main_price_cd = document.forms[0].main_price_cd.value;
	
	const fields = [
        "alloc_qty", "price", "gross_amt", "exchng_rate", "gross_amt1",
        "transportation_tariff", "transportation_amount",
        "marketing_margin", "marketing_margin_amount",
        "other_charges", "other_charges_amount", "tax_amt", "invoice_amt", "net_payable","tds_amt"
    ];
	
	fields.forEach(field => {
        var mainValue = document.getElementsByName("main_"+field)[0].value;
        var newValue = document.getElementsByName("new_"+field)[0].value;
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
        		document.getElementsByName(field)[0].value = diff.toFixed(4);
        	}
        	else
        	{
        		document.getElementsByName(field)[0].value = diff.toFixed(2);
        	}  	
        }
    });
	
	var sub_tax_amt=document.getElementsByName("sub_tax_amt");
	if(sub_tax_amt.length > 0)
	{
		for(var i=0; i<sub_tax_amt.length; i++)
    	{
			var mainValue = document.getElementsByName("main_sub_tax_amt")[i].value;
	        var newValue = document.getElementsByName("new_sub_tax_amt")[i].value;
			//alert(mainValue+" == "+newValue)
	        if (mainValue !== "" && newValue !== "") 
	        {
	        	const diff = parseFloat(newValue) - parseFloat(mainValue);
	        	sub_tax_amt[i].value = diff.toFixed(2);
	        }
    	}
	}
}
enableButton=true;
function doSubmit()
{
	var bu_unit = document.forms[0].bu_unit.value;
	var bu_state_tin = document.forms[0].bu_state_tin.value;
	var plant_seq = document.forms[0].plant_seq.value;
	var contact_person = document.forms[0].contact_person.value;
	var bu_contact_person = document.forms[0].bu_contact_person.value;
	
    var counterparty_cd = document.forms[0].counterparty_cd.value;
    var agmt_no = document.forms[0].agmt_no.value;
    var agmt_rev = document.forms[0].agmt_rev.value;
    var cont_no = document.forms[0].cont_no.value;
    var cont_rev = document.forms[0].cont_rev.value;
    var contract_type = document.forms[0].contract_type.value;
    var period_start_dt = document.forms[0].period_start_dt.value;
    var period_end_dt = document.forms[0].period_end_dt.value;
    
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
	
	var criteria = document.getElementsByName("criteria");
	var invoice_type = document.forms[0].invoice_type.value;
	var operation = document.forms[0].operation.value;
	
	var remark1 = document.forms[0].remark1.value;
	var temp_criteri_formula = document.forms[0].temp_criteri_formula.value;
    
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
    if(bu_state_tin.trim() === "") 
    {
        msg += "Missing Business State TIN!\n";
        flag = false;
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
	
	if(trim(invoice_dt)=="")
	{
		msg+="Enter Invoice Date!\n";
		flag=false;
	}
	
	if(invoice_type=="CR")
	{
		document.forms[0].remark1.value="";
	}
	else if(trim(invoice_type)!="CR")
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
	
	if(trim(tax_amt)!="" && trim(gross_amt1) != "")
	{
		if(parseFloat(gross_amt1) > 0 && parseFloat(tax_amt) < 0)
		{
			msg+="Sign(+/-) for Gross Amount and Tax Amount should be same!\n";
			flag=false;
		}
		
		if(parseFloat(gross_amt1) < 0 && parseFloat(tax_amt) > 0)
		{
			msg+="Sign(+/-) for Gross Amount and Tax Amount should be same!\n";
			flag=false;
		}
	}
	
	if(invoice_type=="DR" && contract_type !="W")
	{
		if(remark1.trim()=="")
		{
			msg+="Bank Detail Missing in Remark-1!\n";
			flag=false;
		}
	}
	
	if(flag)
	{
		if(isChangeInQty)
		{
			alert("⚠️ CR/DR Criteria - Change in Quantity - is Selected!\n\n" +
		              "Important: On Submit, Contract Allocation data modification will be allowed for Gas Date range: "+period_start_dt+" to " +period_end_dt +".\n\n" +
		              "Once Allocation data updated, use Modify option to Submit this CR/DR Note.\n\n" +
		              "Note: System will NOT allow Check or Approve this CR/DR Note, if NO Quantity Change detected!");	
		}
		var a=confirm("Do you want to Submit Credit/Debit Note?");
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
			newWindow = window.open("frm_crdr_tax_structure_list.jsp?type="+type+"&taxStructCd="+taxStructCd,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_crdr_tax_structure_list.jsp?type="+type+"&taxStructCd="+taxStructCd,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
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
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="sales_inv" scope="request"></jsp:useBean>
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
String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
if(month.length() == 1)
{
	month="0"+month; 
}

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

sales_inv.setCallFlag("CRDR_PREPARATION_LIST");
sales_inv.setComp_cd(owner_cd);
sales_inv.setMonth(month);
sales_inv.setYear(year);
sales_inv.setCounterparty_cd(counterparty_cd);
sales_inv.setSel_inv_no(sel_inv_no);
sales_inv.setOperation(operation);
sales_inv.setBu_state_tin(bu_state_tin);
sales_inv.setFinancial_year(financial_year);
sales_inv.setInvoice_seq(invoice_seq);
sales_inv.init();

Vector VCOUNTERPARTY_CD = sales_inv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = sales_inv.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = sales_inv.getVCOUNTERPARTY_ABBR();

Vector VINVOICE_NO_LIST = sales_inv.getVINVOICE_NO_LIST();
Vector VCRITERIA_FLAG = sales_inv.getVCRITERIA_FLAG();
Vector VCRITERIA_NAME = sales_inv.getVCRITERIA_NAME();
Vector VCRITERIA_HIDE = sales_inv.getVCRITERIA_HIDE();

Vector VCONTACT_PERSON = sales_inv.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = sales_inv.getVCONTACT_PERSON_CD();
Vector VBU_CONTACT_PERSON = sales_inv.getVBU_CONTACT_PERSON();
Vector VBU_CONTACT_PERSON_CD = sales_inv.getVBU_CONTACT_PERSON_CD();

String criteri_formula = sales_inv.getCriteri_formula();

String couterpty_abbr=sales_inv.getCouterpty_abbr();
String couterpty_nm=sales_inv.getCouterpty_nm();
String cont_no = sales_inv.getCont_no();
String cont_rev_no = sales_inv.getCont_rev_no();
String agmt_no = sales_inv.getAgmt_no();
String agmt_rev_no = sales_inv.getAgmt_rev_no();
String contract_type = sales_inv.getContract_type();
String deal_no=sales_inv.getDeal_no();
String contract_ref=sales_inv.getContract_ref();
String plant_seq=sales_inv.getPlant_seq();
String plant_abbr=sales_inv.getPlant_abbr();
String bu_unit=sales_inv.getBu_unit();
String bu_plant_abbr=sales_inv.getBu_plant_abbr();
//String bu_state_tin=sales_inv.getBu_state_tin();
if(bu_state_tin.equals(""))
{
	bu_state_tin=sales_inv.getBu_state_tin();
}
String period_start_dt=sales_inv.getPeriod_start_dt();
String period_end_dt=sales_inv.getPeriod_end_dt();
String bu_contact_person_cd = sales_inv.getBu_contact_person_cd();
String contact_person_cd = sales_inv.getContact_person_cd();
String truck_cd = sales_inv.getTruck_cd();
String truck_trans_cd = sales_inv.getTruck_trans_cd();
String mapping_id = sales_inv.getMapping_id();

String invoice_raised_in = sales_inv.getInvoice_raised_in();
String invoice_raised_in_nm = sales_inv.getInvoice_raised_in_nm();
String price_cd=sales_inv.getPrice_cd();
String price_cd_nm=sales_inv.getPrice_cd_nm();
String exchng_rate_cd=sales_inv.getExchng_rate_cd();
String exchang_rate_dt=sales_inv.getExchang_rate_dt();

String invoice_dt = sales_inv.getInvoice_dt();
String invoice_due_dt = sales_inv.getInvoice_due_dt();
String qty_mmbtu=sales_inv.getQty_mmbtu();
String price=sales_inv.getPrice();
String exchang_rate=sales_inv.getExchang_rate();
String gross_amt=sales_inv.getGross_amt();
String gross_amt1=sales_inv.getGross_amt1();
String transportation_charges = sales_inv.getTransportation_charges();
String transportation_amount = sales_inv.getTransportation_amount();
String marketing_margin = sales_inv.getMarketing_margin();
String marketing_margin_amount = sales_inv.getMarketing_margin_amount();
String other_charges = sales_inv.getOther_charges();
String other_charges_amount = sales_inv.getOther_charges_amount();
String gross_include_transport_tariff = sales_inv.getGross_include_transport_tariff();
String tax_amt = sales_inv.getTax_amt();
String tax_struct_cd=sales_inv.getTax_struct_cd();
String tax_struct_dt=sales_inv.getTax_struct_dt();
String tax_struct_dtl=sales_inv.getTax_struct_dtl();
String tax_info = sales_inv.getTax_info();
String tax_factor = sales_inv.getTax_factor();
String invoice_amt = sales_inv.getInvoice_amt();
String net_payable = sales_inv.getNet_payable();
String tds_amt = sales_inv.getTds_amt();
String tds_factor = sales_inv.getTds_factor();
String tds_struct_cd = sales_inv.getTds_struct_cd();
String tds_struct_dt = sales_inv.getTds_struct_dt();

String applicable_flag = sales_inv.getApplicable_flag();
String applicable_abbr = sales_inv.getApplicable_abbr();

String applicable_amt = sales_inv.getApplicable_amt();
String TCS_factor = sales_inv.getTCS_factor();
String tcs_struct_cd = sales_inv.getTcs_struct_cd();
String tcs_struct_dt = sales_inv.getTcs_struct_dt();

String main_invoice_dt = sales_inv.getMain_invoice_dt();
String main_invoice_due_dt = sales_inv.getMain_invoice_due_dt();
String main_qty_mmbtu=sales_inv.getMain_qty_mmbtu();
String main_price=sales_inv.getMain_price();
String main_exchang_rate=sales_inv.getMain_exchang_rate();
String main_gross_amt=sales_inv.getMain_gross_amt();
String main_gross_amt1=sales_inv.getMain_gross_amt1();
String main_transportation_charges = sales_inv.getMain_transportation_charges();
String main_transportation_amount = sales_inv.getMain_transportation_amount();
String main_marketing_margin = sales_inv.getMain_marketing_margin();
String main_marketing_margin_amount = sales_inv.getMain_marketing_margin_amount();
String main_other_charges = sales_inv.getMain_other_charges();
String main_other_charges_amount = sales_inv.getMain_other_charges_amount();
String main_gross_include_transport_tariff = sales_inv.getMain_gross_include_transport_tariff();
String main_tax_amt = sales_inv.getMain_tax_amt();
String main_tax_struct_cd=sales_inv.getMain_tax_struct_cd();
String main_tax_struct_dt=sales_inv.getMain_tax_struct_dt();
String main_tax_struct_dtl=sales_inv.getMain_tax_struct_dtl();
String main_tax_info = sales_inv.getMain_tax_info();
String main_tax_factor = sales_inv.getMain_tax_factor();
String main_invoice_amt = sales_inv.getMain_invoice_amt();
String main_net_payable = sales_inv.getMain_net_payable();
String main_tds_amt = sales_inv.getMain_tds_amt();
String main_tds_factor = sales_inv.getMain_tds_factor();
String main_tds_struct_cd = sales_inv.getMain_tds_struct_cd();
String main_tds_struct_dt = sales_inv.getMain_tds_struct_dt();
String main_applicable_amt = sales_inv.getMain_applicable_amt();
String main_TCS_factor = sales_inv.getMain_TCS_factor();
String main_tcs_struct_cd = sales_inv.getMain_tcs_struct_cd();
String main_tcs_struct_dt = sales_inv.getMain_tcs_struct_dt();

String new_invoice_dt = sales_inv.getNew_invoice_dt();
String new_invoice_due_dt = sales_inv.getNew_invoice_due_dt();
String new_qty_mmbtu=sales_inv.getNew_qty_mmbtu();
String new_price=sales_inv.getNew_price();
String new_exchang_rate=sales_inv.getNew_exchang_rate();
String new_gross_amt=sales_inv.getNew_gross_amt();
String new_gross_amt1=sales_inv.getNew_gross_amt1();
String new_transportation_charges = sales_inv.getNew_transportation_charges();
String new_transportation_amount = sales_inv.getNew_transportation_amount();
String new_marketing_margin = sales_inv.getNew_marketing_margin();
String new_marketing_margin_amount = sales_inv.getNew_marketing_margin_amount();
String new_other_charges = sales_inv.getNew_other_charges();
String new_other_charges_amount = sales_inv.getNew_other_charges_amount();
String new_gross_include_transport_tariff = sales_inv.getNew_gross_include_transport_tariff();
String new_tax_amt = sales_inv.getNew_tax_amt();
String new_tax_struct_cd=sales_inv.getNew_tax_struct_cd();
String new_tax_struct_dt=sales_inv.getNew_tax_struct_dt();
String new_tax_struct_dtl=sales_inv.getNew_tax_struct_dtl();
String new_tax_info = sales_inv.getNew_tax_info();
String new_tax_factor = sales_inv.getNew_tax_factor();
String new_invoice_amt = sales_inv.getNew_invoice_amt();
String new_net_payable = sales_inv.getNew_net_payable();
String new_tds_amt = sales_inv.getNew_tds_amt();
String new_tds_factor = sales_inv.getNew_tds_factor();
String new_tds_struct_cd = sales_inv.getNew_tds_struct_cd();
String new_tds_struct_dt = sales_inv.getNew_tds_struct_dt();
String new_applicable_amt = sales_inv.getNew_applicable_amt();
String new_TCS_factor = sales_inv.getNew_TCS_factor();
String new_tcs_struct_cd = sales_inv.getNew_tcs_struct_cd();
String new_tcs_struct_dt = sales_inv.getNew_tcs_struct_dt();

String changed_qty_mmbtu = sales_inv.getChanged_qty_mmbtu();

/*

//String invoice_seq=sales_inv.getInvoice_seq();
String invoice_no=sales_inv.getInvoice_no();



String exchng_rate_cal=sales_inv.getExchng_rate_cal();


String exchang_criteria=sales_inv.getExchang_criteria();
String last_avlb_exchng_dt = sales_inv.getLast_avlb_exchng_dt();
String lable_inv_criteria = sales_inv.getLable_inv_criteria();
String lable_inv_date = sales_inv.getLable_inv_date();
String invoice_id_seq=sales_inv.getInvoice_id_seq();

String correction_msg = sales_inv.getCorrection_msg();
String daily_tot_amt_inr=sales_inv.getDaily_tot_amt_inr();
String daily_tot_amt_usd=sales_inv.getDaily_tot_amt_usd();
String daily_tot_qty=sales_inv.getDaily_tot_qty();
String agmt_base = sales_inv.getAgmt_base();




String tds_amt = sales_inv.getTds_amt();
String tds_factor = sales_inv.getTds_factor();
String tds_struct_cd = sales_inv.getTds_struct_cd();
String tds_struct_dt = sales_inv.getTds_struct_dt();
*/

String remark_1 =sales_inv.getRemark_1();
String remark_2 =sales_inv.getRemark_2();

Vector VMULTI_TAX_STRUCT = sales_inv.getVMULTI_TAX_STRUCT();
Vector VMAIN_MULTI_TAX_STRUCT = sales_inv.getVMAIN_MULTI_TAX_STRUCT();
Vector VNEW_MULTI_TAX_STRUCT = sales_inv.getVNEW_MULTI_TAX_STRUCT();

%>
<body onload="<%if(!msg.equals("")){ %>Do_close('<%=msg%>','<%=msg_type%>','<%=accroid%>');<%} %> 
<%if(!criteri_formula.equals("")) {%>selectCriteria('<%=criteri_formula%>');<%}%> 
<%if(criteri_formula.contains("QTY")) {%>calc();<%}%>
<%if(operation.equals("MODIFY")) {%>getBankDetail(document.forms[0].invoice_dt);<%} %>"
<%if(operation.equals("INSERT")) {%>style="pointer-events: none;"<%} %>>

<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_DLNG_Invoice">
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
					    	DLNG Sales Credit/Debit Detail
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
					    			<label class="form-label"><b>Select Customer</b></label>
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
										<option value="<%=VINVOICE_NO_LIST.elementAt(i)%>"><%=VINVOICE_NO_LIST.elementAt(i)%></option>										
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
 				  							value="<%=VCRITERIA_FLAG.elementAt(i)%>" onclick="enableNewValues(this);criteriFormula();calc();">
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
												<th colspan="2"><%=owner_abbr%> Detail</th>
												<th rowspan="2">Contract No<br>[Contract/Trader Ref#]</th>
												<th rowspan="2">Billing Period</th>
												<th colspan="3">Customer Detail</th>
											</tr>
											<tr>
												<th>Business Unit<span class="s-red">*</span></th>
												<th>Contact Person<span class="s-red">*</span></th>
												<th>Customer</th>
												<th>Plant</th>
												<th>Contact Person<span class="s-red">*</span></th>
											</tr>
										</thead>
										<tbody>
											<tr>
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
												<td align="center"><%=deal_no%></td>
												<td align="center"><%=period_start_dt%> - <%=period_end_dt%></td>
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
								      						onchange="validateDate(this);getBankDetail(this)" autocomplete="off">
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
															<input type="text" class="form-control form-control-sm" name="new_alloc_qty" value="<%=new_qty_mmbtu %>" readOnly>
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
															<input type="text" class="form-control form-control-sm" name="gross_amt1" value="<%=gross_amt1%>" readOnly>
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
																		<input type="text" class="form-control form-control-sm" name="sub_tax_amt" value="<%=((Vector) temp.elementAt(2)).elementAt(j)%>" readOnly>
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
											<%if(applicable_abbr.equals("TCS")){ %>
											<tr>
												<td><b>TCS (<%=main_TCS_factor%>%)</b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_tcs_amt" value="<%=applicable_amt%>" readOnly>
															<%-- <input type="hidden" class="form-control form-control-sm" name="tcs_factor" value="<%=TCS_factor%>" readOnly> --%>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_tcs_amt" value="<%=applicable_amt%>" readOnly>
															<input type="hidden" class="form-control form-control-sm" name="new_tcs_factor" value="<%=new_TCS_factor%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="tcs_amt" value="<%=applicable_amt%>" readOnly>
															<input type="hidden" class="form-control form-control-sm" name="tcs_factor" value="<%=TCS_factor%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
											</tr>
											<%} %>
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
															<input type="text" class="form-control form-control-sm" name="main_tds_amt" value="<%=main_tds_amt%>" readOnly>
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
															<input type="text" class="form-control form-control-sm" name="new_tds_amt" value="<%=new_tds_amt%>" readOnly>
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
															<input type="text" class="form-control form-control-sm" name="tds_amt" value="<%=tds_amt%>" readOnly>
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
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					<%if(!operation.equals("INSERT")){ %>
					<div class="card-footer cdfooter text-center">
						<div class="d-flex justify-content-between">
							<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
							<%if(write_access.equals("Y")){ %>
							<div class="row justify-content-end">
								<!-- <div class="col-auto">
									<i class="fa fa-eye fa-2x" onclick="viewBeforeSubmit();"></i>
								</div> -->
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
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

<%-- <input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>"> --%>
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" value="<%=agmt_rev_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev" value="<%=cont_rev_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="plant_seq" value="<%=plant_seq%>">
<input type="hidden" name="bu_unit" value="<%=bu_unit%>">
<input type="hidden" name="bu_state_tin" value="<%=bu_state_tin%>">
<input type="hidden" name="period_start_dt" value="<%=period_start_dt%>">
<input type="hidden" name="period_end_dt" value="<%=period_end_dt%>">
<input type="hidden" name="truck_cd" value="<%=truck_cd%>">
<input type="hidden" name="truck_trans_cd" value="<%=truck_trans_cd%>">
<input type="hidden" name="mapping_id" value="<%=mapping_id%>">

<input type="hidden" name="tcs_tds" value="<%=applicable_abbr%>">
<input type="hidden" name="criteri_formula" value="<%=criteri_formula%>">
<input type="hidden" name="changed_qty_mmbtu" value="<%=changed_qty_mmbtu%>">
<input type="hidden" name="temp_criteri_formula" value="<%=criteri_formula%>">

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
		$.post("../servlet/DB_DLNG_Invoice_Ajax?setCallType=CALC_EXISTING_TAX_DTL_FOR_CR_DR&tax_struct_cd="+tax_struct_cd+"&gross_amt="+gross_amt, function(responseJson) {
			console.log(responseJson);
			$.each(responseJson, function(index, json) 
			{
				$.each(json.TOTAL_TAX_DTL, function(index_1, json_1) {
					
					document.forms[0].new_tax_amt.value=json_1.TAX_AMT;
					document.forms[0].new_tax_cd.value=json_1.TAX_STRUCT_CD;
					document.forms[0].new_tax_struct_dtl.value=json_1.TAX_STRUCT_DTL;
					
					document.forms[0].tax_cd.value=json_1.TAX_STRUCT_CD;
					document.forms[0].tax_struct_dtl.value=json_1.TAX_STRUCT_DTL;
					
					document.forms[0].tax_config_btn.value="Tax Config ("+json_1.TAX_STRUCT_DTL+")";
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
						
						/*tax_row+="<div class='row m-b-5' "
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
						+"</div>";*/
				    });
				});
				
				
			});
			
			document.getElementById("loading").style.visibility = "hidden";
		});
	}
}

async function getBankDetail(obj)
{
	var invoice_type = document.forms[0].invoice_type.value;
	var contract_type = document.forms[0].contract_type.value;
	if(invoice_type=="DR" && contract_type !="X")
	{
		var inv_dt=	obj.value;
		if(inv_dt.trim()!="")
		{
			$.post("../servlet/DB_DLNG_Invoice_Ajax?setCallType=BANK_DTL_FOR_CR_DR&inv_dt="+inv_dt, function(responseJson) {
				console.log(responseJson);
				$.each(responseJson, function(index, json) 
				{
					$.each(json.BANK_DTL, function(index_1, json_1) 
					{
						document.forms[0].remark1.value=json_1.REMARK;
					});
				});
			});
		}
		else
		{
			document.forms[0].remark1.value="";
		}
	}
	else
	{
		document.forms[0].remark1.value="";
	}
}
</script>

</form>
</body>
</html>