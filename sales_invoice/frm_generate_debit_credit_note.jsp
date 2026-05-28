<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var operation = document.forms[0].operation.value;
	var bu_unit = document.forms[0].bu_unit.value;
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev = document.forms[0].cont_rev.value;
	var cargo_no = document.forms[0].cargo_no.value;
	var contract_type = document.forms[0].contract_type.value;
	var plant_seq = document.forms[0].plant_seq.value;
	var period_start_dt = document.forms[0].period_start_dt.value;
	var period_end_dt = document.forms[0].period_end_dt.value;
	var temp_period_start_dt = document.forms[0].temp_period_start_dt.value;
	var temp_period_end_dt = document.forms[0].temp_period_end_dt.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	var invoice_dt = document.forms[0].invoice_dt.value;
	var exchg_rate_type = document.forms[0].exchg_rate_type.value;
	var user_defined_dt="";
	if(exchg_rate_type=="D")
	{
		user_defined_dt = document.forms[0].user_defined_dt.value;
	}
	
	var exchng_cd = document.forms[0].exchng_cd.value;
	var exchng_rate_mapp = document.forms[0].exchng_rate_mapp.value
	
	var bu_state_tin = document.forms[0].bu_state_tin.value;
	var exist_financial_year = document.forms[0].exist_financial_year.value;
	var inv_flag = document.forms[0].inv_flag.value
	
	var accroid = document.forms[0].accroid.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_generate_debit_credit_note.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
		"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&cargo_no="+cargo_no+
		"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&inv_dt="+invoice_dt+
		"&user_defined_dt="+user_defined_dt+"&operation="+operation+"&sel_exchng_cd="+exchng_cd+
		"&exist_financial_year="+exist_financial_year+"&bu_state_tin="+bu_state_tin+"&temp_period_start_dt="+temp_period_start_dt+"&temp_period_end_dt="+temp_period_end_dt+
		"&exchng_rate_mapp="+exchng_rate_mapp+"&accroid="+accroid+"&inv_flag="+inv_flag+"&u="+u;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setExchngRateCd(obj)
{
	var exchng_cd = document.forms[0].exchng_cd
	if(obj.checked)
	{
		exchng_cd.value=obj.value;
		refresh();
	}
}

function setExchngRateCd1(obj)
{
	var exchng_rate_mapp = document.forms[0].exchng_rate_mapp
	if(obj.checked)
	{
		exchng_rate_mapp.value=obj.id;
		refresh();
	}
}

function doSubmit()
{
	var bu_unit = document.forms[0].bu_unit.value;
	var contact_person = document.forms[0].contact_person.value
	var bu_contact_person = document.forms[0].bu_contact_person.value
	
	var contract_type = document.forms[0].contract_type.value
	
	//var invoice_no = document.forms[0].invoice_no.value
	var drcr_invoice_dt = document.forms[0].drcr_invoice_dt.value
	var invoice_due_dt = document.forms[0].invoice_due_dt.value
	var alloc_qty = document.forms[0].alloc_qty.value
	var price = document.forms[0].price.value
	var price_cd = document.forms[0].price_cd.value
	//var gross_amt = document.forms[0].gross_amt.value
	var exchng_rate = document.forms[0].exchng_rate.value
	var gross_amt1 = document.forms[0].gross_amt1.value
	var tax_amt = document.forms[0].tax_amt.value
	var invoice_amt = document.forms[0].invoice_amt.value
	var net_payable = document.forms[0].net_payable.value
	
	var agmt_base = document.forms[0].agmt_base.value;
	//var transportation_amount = document.forms[0].transportation_amount.value;
	
	/* var tcs_tds = document.forms[0].tcs_tds.value;
	var tcs_struct_cd = document.forms[0].tcs_struct_cd.value;
	var tds_struct_cd = document.forms[0].tds_struct_cd.value; */
	
	var plant_gstin_no = document.forms[0].plant_gstin_no.value;
	var bu_gstin_no = document.forms[0].bu_gstin_no.value;
	
	
	var drcr_price = document.forms[0].drcr_price.value;
	var drcr_exchng_rate = document.forms[0].drcr_exchng_rate.value;
	var invoice_dt = document.forms[0].invoice_dt.value;
	
	var remark=document.forms[0].remark.value;
	
	var msg="";
	var flag=true;
	
	
	if(trim(remark) == "") {
		msg+="Please Enter Reason!\n";
		flag=false;
	}
	
	if(trim(drcr_price) == "") {
		msg+="Confirmed Price missing!\n";
		flag=false;
	}
	if(trim(drcr_exchng_rate) == "") {
		msg+="Exchange rate Missing!\n";
		flag=false;
	}
	
	if(contact_person=="0" || trim(contact_person) == "")
	{
		msg+="Select Customer Contact Person!\n";
		flag=false;
	}
	if(bu_unit=="0" || trim(bu_unit) == "")
	{
		msg+="Select Business Unit!\n";
		flag=false;
	}	
	if(bu_contact_person=="0" || trim(bu_contact_person) == "")
	{
		msg+="Select Business Contact Person!\n";
		flag=false;
	}
	
	
	/* if(trim(invoice_no)=="")
	{
		msg+="Invoice No missing!\n";
		flag=false;
	} */
	if(trim(drcr_invoice_dt)=="")
	{
		msg+="Enter Debit/Credit Note Date!\n";
		flag=false;
	}
	if(trim(invoice_due_dt)=="")
	{
		msg+="Enter Invoice Due Date!\n";
		flag=false;
	}
	
	if(trim(drcr_invoice_dt)!="" && trim(invoice_dt)!="")
	{
		var count = compareDate(invoice_dt,drcr_invoice_dt);
		if(parseInt(count) == 1)
		{
			msg+="Debit/Credit Note	 Date should be grater or equal Invoice Date!";
			flag=false;
		}
	}
	
	
	if(price_cd=="2")
	{
		if(exchng_rate == "0.0000" || trim(exchng_rate) == "")
		{
			msg+="Please Select The Exchange Rate Which Is Available !!!\nOR\nEnter The Exchange Rate From Interest & Exchange Rate Entry Form For The Selected Date!\n";
			flag=false;
		}
	}
	
	if(trim(alloc_qty)!="")
	{
		if(parseFloat(alloc_qty) <= 0)
		{
			msg+="Allocated Qty should be > ZERO(0)!\n";
			flag=false;	
		}
		else
		{
			/* if(tcs_tds=="TCS")
			{
				if(trim(tcs_struct_cd)=="")
				{
					var tcs_factor = document.forms[0].tcs_factor.value;
					var tcs_amt = document.forms[0].tcs_amt.value;
					
					var tcsCnt=parseInt("0");
					if(trim(tcs_factor)=="" || tcs_factor=="0.00" || tcs_factor=="0.000" || tcs_factor=="0")
					{
						tcsCnt++;
					}
					if(trim(tcs_amt)=="" || tcs_amt=="0.00" || tcs_amt=="0.000" || tcs_amt=="0")
					{
						tcsCnt++;
					}
					
					if(parseInt(tcsCnt) > 0)
					{
						msg+="TCS is applicable for this Invoice.\nPlease configure TCS factor and re-try invoice submission!\n";
						flag=false;
					}
				}
			}
			else if(tcs_tds=="TDS")
			{
				if(trim(tds_struct_cd)=="")
				{
					var tds_factor = document.forms[0].tds_factor.value;
					var tds_amt = document.forms[0].tds_amt.value;
					
					var tdsCnt=parseInt("0");
					if(trim(tds_factor)=="" || tds_factor=="0.00" || tds_factor=="0.000" || tds_factor=="0")
					{
						tdsCnt++;
					}
					if(trim(tds_amt)=="" || tds_amt=="0.00" || tds_amt=="0.000" || tds_amt=="0")
					{
						tdsCnt++;
					}
					
					if(parseInt(tdsCnt) > 0)
					{
						msg+="TDS is applicable for this Invoice.\nPlease configure TDS factor and re-try invoice submission!\n";
						flag=false;
					}
				}
			} */
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
	/* if(agmt_base=="D")
	{
		if(trim(transportation_amount)=="")
		{
			msg+="Transporatation Tariff missing!\n";
			flag=false;
		}
	} */
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
	
	if(contract_type=="O" || contract_type=="Q")
	{
		if(trim(plant_gstin_no)=="")
		{
			msg+="Please configure GSTIN No for Selected Customer Plant before Invoice Submission!\n";
			flag=false;
		}
		
		if(trim(bu_gstin_no)=="")
		{
			msg+="Please configure GSTIN No for Selected Business Unit before Invoice Submission!\n";
			flag=false;
		}
	}
	
	
	
	if(flag){
		var a=confirm("Do you want to Submit Debit/Credit Note ?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else {
		alert(msg);
	}
}

function Do_close(msg,msg_type,accroid)
{
	window.opener.refershPar(msg,msg_type,accroid);
	window.close();
}

function setExchnageRate(obj,date,cd)
{
	var alloc_qty = document.forms[0].alloc_qty.value
	var price = document.forms[0].price.value
	var price_cd = document.forms[0].price_cd.value
	var invoice_raised_in = document.forms[0].invoice_raised_in.value
	var gross_amt = document.forms[0].gross_amt.value
	
	var exchng_rate = document.forms[0].exchng_rate.value
	var exchng_cd = document.forms[0].exchng_cd.value
	var exchng_dt = document.forms[0].exchng_dt.value
	
	var gross_amt1 = document.forms[0].gross_amt1.value
	var tax_amt = document.forms[0].tax_amt.value
	var tax_factor = document.forms[0].tax_factor.value
	
	var invoice_amt = document.forms[0].invoice_amt.value
	var net_payable = document.forms[0].net_payable.value
	
	var grossAmount=0;
	var grossAmount1=0;
	var taxAmount=0;
	var invoiceAmount=0;
	var netPayAmount=0;
	if(obj.checked)
	{
		document.forms[0].exchng_rate.value=obj.value;
		document.forms[0].exchng_cd.value=cd;
		document.forms[0].exchng_dt.value=date;
		
		if(trim(alloc_qty) != "")
		{
			if(parseFloat(alloc_qty) > 0)
			{
				grossAmount = parseFloat(alloc_qty) *  parseFloat(price)
				
				if(price_cd == "2")
				{
					grossAmount1 = parseFloat(grossAmount) *  parseFloat(obj.value)
				}
				else
				{
					grossAmount1 = parseFloat(grossAmount)
				}
				
				taxAmount = (parseFloat(grossAmount1) * parseFloat(tax_factor)) / 100;
				
				invoiceAmount = parseFloat(grossAmount1) + parseFloat(taxAmount); 
				netPayAmount=parseFloat(invoiceAmount)
				
				document.forms[0].gross_amt.value=round(parseFloat(grossAmount),2);
				document.forms[0].gross_amt1.value=round(parseFloat(grossAmount1),2);
				document.forms[0].tax_amt.value=round(parseFloat(taxAmount),2);
				document.forms[0].invoice_amt.value=round(parseFloat(invoiceAmount),2);
				document.forms[0].net_payable.value=round(parseFloat(netPayAmount),2);
			}
			else
			{
				document.forms[0].gross_amt.value="";
				document.forms[0].gross_amt1.value="";
				document.forms[0].tax_amt.value="";
				document.forms[0].invoice_amt.value="";
				document.forms[0].net_payable.value="";
			}
		}
		else
		{
			document.forms[0].gross_amt.value="";
			document.forms[0].gross_amt1.value="";
			document.forms[0].tax_amt.value="";
			document.forms[0].invoice_amt.value="";
			document.forms[0].net_payable.value="";
		}
	}
}



function changeAmount() {
	
	var criteria_desc = document.forms[0].criteria_desc.value;
	var criteria = document.forms[0].criteria.value;
	var drcr_flag = document.forms[0].drcr_flag.value;
	var diff="Difference ";
	var diff_qty="";
	var diff_price="";
	var diff_exchng="";
	
	var cont_type = document.forms[0].contract_type.value;

	var alloc_qty = parseFloat(document.forms[0].alloc_qty.value) || 0;
	var price = parseFloat(document.forms[0].price.value) || 0;
	var exchng_rate = parseFloat(document.forms[0].exchng_rate.value) || 0;
	var gross_amt = parseFloat(document.forms[0].gross_amt.value) || 0;
	var gross_amt1 = document.forms[0].gross_amt1.value;
	var invoice_amt = document.forms[0].invoice_amt.value;
 	var net_payable = document.forms[0].net_payable.value;
	
	var drcr_alloc_qty = parseFloat(document.forms[0].drcr_alloc_qty.value) || 0;
	var drcr_price = parseFloat(document.forms[0].drcr_price.value);
	
	var drcr_exchng_rate = parseFloat(document.forms[0].drcr_exchng_rate.value);
	var drcr_price_cd = document.forms[0].drcr_price_cd.value;
	var invoice_raised_in = document.forms[0].invoice_raised_in.value;
	var drcr_gross_amt = parseFloat(document.forms[0].drcr_gross_amt.value) || 0;
	var drcr_gross_amt1 = parseFloat(document.forms[0].drcr_gross_amt1.value) || 0;
	var drcr_invoice_amt = parseFloat(document.forms[0].drcr_invoice_amt.value) || 0;
 	var drcr_net_payable = parseFloat(document.forms[0].drcr_net_payable.value) || 0;
 	
 	
 	
 	
	var drcr_tax_factor = document.forms[0].drcr_tax_factor.value;
	var drcr_tax_amt = parseFloat(document.forms[0].drcr_tax_amt	.value);
	var drcr_tax_struct_dtl = document.forms[0].drcr_tax_struct_dtl.value;
	
	var drcr_sub_tax_struct = document.getElementsByName("drcr_sub_tax_struct");	
	var drcr_sub_tax_amt = document.getElementsByName("drcr_sub_tax_amt");
	
	
		if(!Number.isNaN(drcr_exchng_rate) && !Number.isNaN(drcr_price))
		{
			if(criteria_desc.includes("1") && drcr_price_cd != 1)
			{
		 		if(drcr_flag == "DR"){
		 			if(criteria == "1"){ 
		 				diff_exchng = drcr_exchng_rate - exchng_rate; 
		 				diff_exchng = parseFloat(diff_exchng).toFixed(4);
		 				document.getElementById("diff_exchng").innerText = diff+diff_exchng;
		 				drcr_gross_amt = drcr_alloc_qty * drcr_price ;
						drcr_gross_amt1 = drcr_alloc_qty * drcr_price * diff_exchng ;
		 			}
		 			else if(criteria == "2"){
		 				diff_price += drcr_price - price;
		 				diff_price = parseFloat(diff_price).toFixed(4);
		 				document.getElementById("diff_price").innerText = diff+diff_price;
		 				drcr_gross_amt = drcr_alloc_qty * diff_price ;
						drcr_gross_amt1 = drcr_alloc_qty * diff_price * drcr_exchng_rate ;
		 			}
		 			else if(criteria == "3") {
		 				diff_qty += drcr_alloc_qty - alloc_qty;
		 				drcr_gross_amt = diff_qty * drcr_price ;
						drcr_gross_amt1 = diff_qty * drcr_price * drcr_exchng_rate ;
		 			}
		 		}
		 		else{
		 			if(criteria == "1"){ 
		 				diff_exchng = exchng_rate - drcr_exchng_rate; 
		 				diff_exchng = parseFloat(diff_exchng).toFixed(4);
		 				document.getElementById("diff_exchng").innerText = diff+diff_exchng;
		 				drcr_gross_amt = drcr_alloc_qty * drcr_price ;
						drcr_gross_amt1 = drcr_alloc_qty * drcr_price * diff_exchng ;
		 			}
		 			else if(criteria == "2"){
		 				diff_price += price - drcr_price;
		 				diff_price = parseFloat(diff_price).toFixed(4);
		 				document.getElementById("diff_price").innerText = diff+diff_price;
		 				drcr_gross_amt = drcr_alloc_qty * diff_price ;
						drcr_gross_amt1 = drcr_alloc_qty * diff_price * drcr_exchng_rate ;
		 			}
		 			else if(criteria == "3") {
		 				diff_qty += alloc_qty - drcr_alloc_qty;
		 				drcr_gross_amt = diff_qty * drcr_price ;
						drcr_gross_amt1 = diff_qty * drcr_price * drcr_exchng_rate ;
		 			}
		 			
		 		}
			}
			else if(drcr_price_cd != 1){
				if(drcr_flag == "DR"){
		 			if(criteria == "1"){ 
		 				diff_exchng = drcr_exchng_rate - exchng_rate; 
		 				diff_exchng = parseFloat(diff_exchng).toFixed(4);
		 				document.getElementById("diff_exchng").innerText = diff+diff_exchng;
		 				drcr_gross_amt = drcr_alloc_qty * drcr_price ;
						drcr_gross_amt1 = drcr_alloc_qty * drcr_price * diff_exchng ;
		 			}
		 			else if(criteria == "2"){
		 				diff_price += drcr_price - price;
		 				diff_price = parseFloat(diff_price).toFixed(4);
		 				document.getElementById("diff_price").innerText = diff+diff_price;
		 				drcr_gross_amt = drcr_alloc_qty * diff_price ;
						drcr_gross_amt1 = drcr_alloc_qty * diff_price * drcr_exchng_rate ;
		 			}
		 			else if(criteria == "3") {
		 				diff_qty += drcr_alloc_qty - alloc_qty;
		 				drcr_gross_amt = diff_qty * drcr_price ;
						drcr_gross_amt1 = diff_qty * drcr_price * drcr_exchng_rate ;
		 			}
		 		}
		 		else{
		 			if(criteria == "1"){ 
		 				diff_exchng = exchng_rate - drcr_exchng_rate; 
		 				diff_exchng = parseFloat(diff_exchng).toFixed(4);
		 				document.getElementById("diff_exchng").innerText = diff+diff_exchng;
		 				drcr_gross_amt = drcr_alloc_qty * drcr_price ;
						drcr_gross_amt1 = drcr_alloc_qty * drcr_price * diff_exchng ;
		 			}
		 			else if(criteria == "2"){
		 				diff_price += price - drcr_price;
		 				diff_price = parseFloat(diff_price).toFixed(4);
		 				document.getElementById("diff_price").innerText = diff+diff_price;
		 				drcr_gross_amt = drcr_alloc_qty * diff_price ;
						drcr_gross_amt1 = drcr_alloc_qty * diff_price * drcr_exchng_rate ;
		 			}
		 			else if(criteria == "3") {
		 				diff_qty += alloc_qty - drcr_alloc_qty;
		 				drcr_gross_amt = diff_qty * drcr_price ;
						drcr_gross_amt1 = diff_qty * drcr_price * drcr_exchng_rate ;
		 			}		 			
		 		}	
			}
			else{
				if(drcr_flag == "DR"){
		 			if(criteria == "2"){
		 				diff_price += drcr_price - price;
		 				diff_price = parseFloat(diff_price).toFixed(4);
		 				document.getElementById("diff_price").innerText = diff+diff_price;
		 			}
		 			else if(criteria == "3") {
		 				diff_qty += drcr_alloc_qty - alloc_qty;
		 				drcr_gross_amt1 = diff_qty * drcr_price ;
		 			}
		 		}
		 		else{
		 			if(criteria == "2"){
		 				diff_price += price - drcr_price;
		 				diff_price = parseFloat(diff_price).toFixed(4);
		 				document.getElementById("diff_price").innerText = diff+diff_price;
		 			}
		 			else if(criteria == "3") {
		 				diff_qty += alloc_qty - drcr_alloc_qty;
		 				drcr_gross_amt1 = diff_qty * drcr_price ;
		 			}	
		 		}
			}
			
			if(!drcr_tax_struct_dtl.includes(",")){
				if(!drcr_tax_struct_dtl || drcr_tax_struct_dtl.trim() === ""){
					drcr_tax_factor = 0;
					drcr_tax_amt = drcr_gross_amt1 * drcr_tax_factor / 100;
				}
				else{
					drcr_tax_factor = drcr_tax_struct_dtl.split(" ")[1];
					drcr_tax_factor = drcr_tax_factor.substring(0,1);
					drcr_tax_amt = drcr_gross_amt1 * drcr_tax_factor / 100;
				}
			}
		 	else {
		 		for(i=0;i<drcr_sub_tax_struct.length;i++) {
					drcr_tax_factor = drcr_sub_tax_struct[0].value.split(" ")[1];
					drcr_tax_factor = drcr_tax_factor.substring(0,1);
					
					
					drcr_sub_tax_amt = drcr_gross_amt1 * drcr_tax_factor / 100;
					document.forms[0].drcr_sub_tax_amt[i].value = parseFloat(drcr_sub_tax_amt).toFixed(2);
					drcr_tax_amt = drcr_sub_tax_amt * 2;
		 		}
		 	}
			
			drcr_invoice_amt = drcr_gross_amt1 + drcr_tax_amt;
		 	drcr_net_payable = drcr_invoice_amt;

		 	
		 	if(cont_type == "O") {
		 		drcr_gross_amt = parseFloat(drcr_gross_amt).toFixed(2);
		 		
		 		drcr_gross_amt1 = drcr_gross_amt1.toFixed(0);
		 		drcr_gross_amt1 = parseFloat(drcr_gross_amt1).toFixed(2);
		 		drcr_tax_amt = drcr_tax_amt.toFixed(0);
			 	drcr_tax_amt = parseFloat(drcr_tax_amt).toFixed(2);
			 	var subTaxFields = document.forms[0].elements["drcr_sub_tax_amt"];
	 			for (let i = 0; i < subTaxFields.length; i++) {
	 			    subTaxFields[i].value = drcr_tax_amt/2;
	 			}
			 	drcr_invoice_amt = drcr_invoice_amt.toFixed(0);
			 	drcr_invoice_amt = parseFloat(drcr_invoice_amt).toFixed(2);
			 	drcr_net_payable = drcr_net_payable.toFixed(0);
			 	drcr_net_payable = parseFloat(drcr_net_payable).toFixed(2);
			 	
			 	
		 		}else {
		 			drcr_gross_amt = drcr_gross_amt.toFixed(2);
		 			drcr_gross_amt1 = drcr_gross_amt1.toFixed(2);
				 	drcr_tax_amt = drcr_tax_amt.toFixed(2);
				 	drcr_invoice_amt = drcr_invoice_amt.toFixed(2);
				 	drcr_net_payable = drcr_net_payable.toFixed(2);
		 		}

		 	
		 	
			document.forms[0].drcr_gross_amt.value = drcr_gross_amt;
			//document.forms[0].drcr_exchng_rate.value = drcr_exchng_rate;
			document.forms[0].drcr_gross_amt1.value = drcr_gross_amt1;
			document.forms[0].drcr_tax_amt.value = drcr_tax_amt;
			//document.forms[0].drcr_sub_tax_amt.value = drcr_sub_tax_amt;
			document.forms[0].drcr_invoice_amt.value = drcr_invoice_amt;
			document.forms[0].drcr_net_payable.value = drcr_net_payable;
		 }
		
		/* else 
		 {
			if(criteria == "1"){ 
				document.getElementById("diff_exchng").innerText = "";
	 			document.forms[0].drcr_exchng_rate.value = "";
	 			document.forms[0].drcr_gross_amt1.value = "";
	 			document.forms[0].drcr_tax_amt.value = "";
	 			var subTaxFields = document.forms[0].elements["drcr_sub_tax_amt"];
	 			for (let i = 0; i < subTaxFields.length; i++) {
	 			    subTaxFields[i].value = "";
	 			}
	 			document.forms[0].drcr_invoice_amt.value = "";
	 			document.forms[0].drcr_net_payable.value = "";
 			}
 			else if(criteria == "2"){
 				document.getElementById("diff_price").innerText = "";
	 			document.forms[0].drcr_price.value = "";
	 			document.forms[0].drcr_gross_amt.value = "";
	 			document.forms[0].drcr_exchng_rate.value = "";
	 			document.forms[0].drcr_gross_amt1.value = "";
	 			document.forms[0].drcr_tax_amt.value = "";
	 			var subTaxFields = document.forms[0].elements["drcr_sub_tax_amt"];
	 			for (let i = 0; i < subTaxFields.length; i++) {
	 			    subTaxFields[i].value = "";
	 			}
	 			document.forms[0].drcr_invoice_amt.value = "";
	 			document.forms[0].drcr_net_payable.value = "";
 			}
			 	
			 	
		 } */
		
		

		 	
 	
 	
	
/* 	diff_price = parseFloat(diff_price).toFixed(4);
	
	document.getElementById("diff_qty").innerText = diff+diff_qty;
	document.getElementById("diff_price").innerText = diff+diff_price;
	document.getElementById("diff_qty").innerText = diff_qty; */
	
}

/* function changeCharges()
{
	var drcr_alloc_qty = parseFloat(document.forms[0].drcr_alloc_qty.value);
	var drcr_transportation_tariff = parseFloat(document.forms[0].drcr_transportation_tariff.value);
	var drcr_gross_include_transport_tariff = "";
	var criteria = document.forms[0].criteria.value;
	
	var drcr_tax_factor = document.forms[0].drcr_tax_factor;
	var drcr_tax_struct_dtl = document.forms[0].drcr_tax_struct_dtl.value;
	var drcr_tax_amt = document.forms[0].drcr_tax_amt.value;
	
	var drcr_sub_tax_struct = document.getElementsByName("drcr_sub_tax_struct");	
	var drcr_sub_tax_amt = document.getElementsByName("drcr_sub_tax_amt");
	
	
	
	var drcr_transportation_amount="";
	
	

	if(criteria.includes("4")){
		drcr_transportation_amount = drcr_alloc_qty * drcr_transportation_tariff;
		
		drcr_gross_include_transport_tariff = drcr_transportation_amount;
		
// 		document.forms[0].drcr_transportation_tariff.value = parseFloat(drcr_transportation_tariff);
		document.forms[0].drcr_transportation_amount.value = parseFloat(drcr_transportation_amount).toFixed(2);
		document.forms[0].drcr_gross_include_transport_tariff.value = parseFloat(drcr_gross_include_transport_tariff).toFixed(2);
	}
	
	if(!drcr_tax_struct_dtl.includes(","))
	{
		if(!drcr_tax_struct_dtl || drcr_tax_struct_dtl.trim() === "")
		{
			drcr_tax_factor = 0;
			drcr_tax_amt = drcr_gross_include_transport_tariff * drcr_tax_factor / 100;
		}
		else
		{
			drcr_tax_factor = drcr_tax_struct_dtl.split(" ")[1];
			drcr_tax_factor = drcr_tax_factor.substring(0,1);
			drcr_tax_amt = drcr_gross_include_transport_tariff * drcr_tax_factor / 100;
		}
	}
	else
	{
		for(i=0;i<drcr_sub_tax_struct.length;i++) {
			drcr_tax_factor = drcr_sub_tax_struct[0].value.split(" ")[1];
			drcr_tax_factor = drcr_tax_factor.substring(0,1);
			
			
			drcr_sub_tax_amt = drcr_gross_include_transport_tariff * drcr_tax_factor / 100;
			document.forms[0].drcr_sub_tax_amt[i].value = drcr_sub_tax_amt;
			drcr_tax_amt = drcr_sub_tax_amt * 2;
		}
		
	}
	
	var drcr_invoice_amt = drcr_gross_include_transport_tariff + drcr_tax_amt;
	var drcr_net_payable = drcr_invoice_amt;
	
	document.forms[0].drcr_gross_include_transport_tariff.value = parseFloat(drcr_gross_include_transport_tariff).toFixed(2);
	document.forms[0].drcr_tax_amt.value = parseFloat(drcr_tax_amt).toFixed(2);
	document.forms[0].drcr_invoice_amt.value = parseFloat(drcr_invoice_amt).toFixed(2);
	document.forms[0].drcr_net_payable.value = parseFloat(drcr_net_payable).toFixed(2);
	
} */

function validation() {
	
	var criteria = document.forms[0].criteria.value;
	var drcr_flag = document.forms[0].drcr_flag.value;
	
	var exchng_rate = parseFloat(document.forms[0].exchng_rate.value) || 0;
	var drcr_exchng_rate = parseFloat(document.forms[0].drcr_exchng_rate.value);
	
	var price = parseFloat(document.forms[0].price.value) || 0;
	var drcr_price = parseFloat(document.forms[0].drcr_price.value) || 0;
	var drcr_sub_tax_amt = document.getElementsByName("drcr_sub_tax_amt");
	
	var alloc_qty = parseFloat(document.forms[0].alloc_qty.value) || 0;	
	var drcr_alloc_qty = parseFloat(document.forms[0].drcr_alloc_qty.value) || 0;
	var drcr_invoice_dt = document.forms[0].drcr_invoice_dt.value;
	var drcr_invoice_due_dt = document.forms[0].drcr_invoice_due_dt.value;
	var invoice_dt = document.forms[0].invoice_dt.value;
	var invoice_due_dt = document.forms[0].invoice_due_dt.value;
	
	var flag = true;

	
 	if(drcr_flag == "DR"){ 
 			if(criteria == "1" && exchng_rate>=drcr_exchng_rate) {
 				
 				flag =  false;
	 			alert("Exchange Rate should be Greater than Invoice Exchange Rate");
	 			document.getElementById("diff_exchng").innerText = "";
	 			document.forms[0].drcr_exchng_rate.value = "";
	 			document.forms[0].drcr_gross_amt1.value = "";
	 			document.forms[0].drcr_tax_amt.value = "";
	 			var subTaxFields = document.forms[0].elements["drcr_sub_tax_amt"];
	 			for (let i = 0; i < subTaxFields.length; i++) {
	 			    subTaxFields[i].value = "";
	 			}
	 			document.forms[0].drcr_invoice_amt.value = "";
	 			document.forms[0].drcr_net_payable.value = "";
	 			return false;
 			}
 			else if(criteria == "2" && price>=drcr_price) {
	 			flag =  false;
	 			alert("Price should be Greater than Invoice Price");
	 			document.getElementById("diff_price").innerText = "";
	 			document.forms[0].drcr_price.value = "";
	 			document.forms[0].drcr_gross_amt.value = "";
	 			document.forms[0].drcr_gross_amt1.value = "";
	 			document.forms[0].drcr_tax_amt.value = "";
	 			var subTaxFields = document.forms[0].elements["drcr_sub_tax_amt"];
	 			for (let i = 0; i < subTaxFields.length; i++) {
	 			    subTaxFields[i].value = "";
	 			}
	 			document.forms[0].drcr_invoice_amt.value = "";
	 			document.forms[0].drcr_net_payable.value = "";
	 			return false;
	 		} 
 			else if(criteria == "3" && alloc_qty>=drcr_alloc_qty) {
 				flag =  false;
	 			alert("Quantity be Greater than Allocated Quantity");
	 			document.getElementById("diff_qty").innerText = "";
	 			changeAmount();
	 			return false;
	 		}
 	}
 	else if(drcr_flag == "CR") {
 		if(criteria == "1" && exchng_rate<=drcr_exchng_rate) {
				flag =  false;
	 			alert("Exchange Rate should be Less than Invoice Exchange Rate");
	 			document.getElementById("diff_exchng").innerText = "";
	 			document.getElementById("diff_exchng").innerText = "";
	 			document.forms[0].drcr_exchng_rate.value = "";
	 			document.forms[0].drcr_gross_amt1.value = "";
	 			document.forms[0].drcr_tax_amt.value = "";
	 				var subTaxFields = document.forms[0].elements["drcr_sub_tax_amt"];
		 			for (let i = 0; i < subTaxFields.length; i++) {
		 			    subTaxFields[i].value = "";
		 			}
	 			document.forms[0].drcr_invoice_amt.value = "";
	 			document.forms[0].drcr_net_payable.value = "";
	 			return false;
			}
			else if(criteria == "2" && price<=drcr_price) {
 			flag =  false;
 			alert("Price should be Less than Invoice Price");
 			document.getElementById("diff_price").innerText = "";
 			document.forms[0].drcr_price.value = "";
 			document.forms[0].drcr_gross_amt.value = "";
 			document.forms[0].drcr_exchng_rate.value = "";
 			document.forms[0].drcr_gross_amt1.value = "";
 			document.forms[0].drcr_tax_amt.value = "";
 			var subTaxFields = document.forms[0].elements["drcr_sub_tax_amt"];
 			for (let i = 0; i < subTaxFields.length; i++) {
 			    subTaxFields[i].value = "";
 			}
 			document.forms[0].drcr_invoice_amt.value = "";
 			document.forms[0].drcr_net_payable.value = "";
 			return false;
 		} 
			else if(criteria == "3" && alloc_qty<=drcr_alloc_qty) {
				flag =  false;
 			alert("Quantity be Less than Allocated Quantity");
 			document.getElementById("diff_qty").innerText = "";
 	
 			return false;
 		}
 	}
}

function handleField()
{
	var drcr_invoice_dt = document.forms[0].drcr_invoice_dt.value;
	var drcr_invoice_due_dt = document.forms[0].drcr_invoice_due_dt.value;
	var invoice_dt = document.forms[0].invoice_dt.value;
	var invoice_due_dt = document.forms[0].invoice_due_dt.value;
	
	var flag = true;
	
	if(trim(drcr_invoice_dt)!="" && trim(invoice_dt)!="")
	{
		var count = compareDate(invoice_dt,drcr_invoice_dt);
		if(parseInt(count) == 1)
		{
			msg="Debit/Credit Note Date should be greater or equal Invoice Date!";
			alert(msg);
			flag=false;
			document.forms[0].drcr_invoice_dt.value = "";
			return false;
		}
	}
	if(trim(drcr_invoice_dt)!="" && trim(drcr_invoice_due_dt)!="")
	{
		var count = compareDate(drcr_invoice_dt,drcr_invoice_due_dt);
		if(parseInt(count) == 1)
		{
			msg="Debit/Credit Due Date should be greater or equal to Debit/Credit Note Date!";
			alert(msg);
			flag=false;
			document.forms[0].drcr_invoice_due_dt.value = "";
			return false;
		}
	}
	if(trim(drcr_invoice_due_dt)!="" && trim(invoice_due_dt)!="")
	{
		var count = compareDate(invoice_dt,drcr_invoice_due_dt);
		if(parseInt(count) == 1)
		{
			msg= "Debit/Credit Due Date should be greater or equal to Invoice Date!";
			alert(msg);
			flag=false;
			document.forms[0].drcr_invoice_due_dt.value = "";
			return false;
		}
	}
	
}

function checkExchngRate()
{
	if(document.forms[0].exchng_rate.value == "0.0000" || trim(document.forms[0].exchng_rate.value) == "")
	{
		alert("First Check The Following Fields :\n\nPlease Select The Exchange Rate Which Is Available !!!\nOR\nEnter The Exchange Rate From Interest & Exchange Rate Entry Form For The Selected Date !!!")
	}
}

/* function setInvoiceNo()
{
	var e = document.forms[0].invoice_id_seq;
	var invoiceNo = e.options[e.selectedIndex].text;
	if(document.forms[0].invoice_id_seq.value=="")
	{
		document.forms[0].invoice_no.value="";
	}
	else
	{
		document.forms[0].invoice_no.value=invoiceNo;
	}
} */

/* var newWindow;
function viewBeforeSubmit()
{
	var bu_unit = document.forms[0].bu_unit.value;
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev = document.forms[0].cont_rev.value;
	var cargo_no = document.forms[0].cargo_no.value;
	var contract_type = document.forms[0].contract_type.value;
	var plant_seq = document.forms[0].plant_seq.value;
	var period_start_dt = document.forms[0].period_start_dt.value;
	var period_end_dt = document.forms[0].period_end_dt.value;
	var billing_cycle = document.forms[0].billing_cycle.value;	
	var inv_flag = document.forms[0].inv_flag.value;
	
	var financial_year = document.forms[0].financial_year.value;
	var bu_state_tin = document.forms[0].bu_state_tin.value;
	var price_cd = document.forms[0].price_cd.value;
	var invoice_raised_in = document.forms[0].invoice_raised_in.value;
	
	var contact_person = document.forms[0].contact_person.value;
	var bu_contact_person = document.forms[0].bu_contact_person.value;
	
	var invoice_dt = document.forms[0].invoice_dt.value;
	var agmt_base = document.forms[0].agmt_base.value;
	
	var mkt_mrgin = document.forms[0].marketing_margin.value;
	var oth_chrg = document.forms[0].other_charges.value;
	var trans_tariff = document.forms[0].transportation_tariff.value;
	var mkt_mrgin_amt = document.forms[0].marketing_margin_amount.value;
	var oth_chrg_amt = document.forms[0].other_charges_amount.value;
	var trans_tariff_amt = document.forms[0].transportation_amount.value;
	var isGrossIncTriff = document.forms[0].isGrossIncTriff.value;
	
	var tcs_tds = document.forms[0].tcs_tds.value;
	
	if((bu_contact_person=="0" || trim(bu_contact_person)=="") && (contact_person=="0" || trim(contact_person)==""))
	{
		alert("Select Business Contact Person!\nSelect Customer Contact Person!");
	}
	else if(bu_contact_person=="0" || trim(bu_contact_person)=="")
	{
		alert("Select Business Contact Person!");
	}
	else if(contact_person=="0" || trim(contact_person)=="")
	{
		alert("Select Customer Contact Person!");
	}
	else
	{
		var url = "../sales_invoice/rpt_view_before_submit.jsp?counterparty_cd="+counterparty_cd+
			"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&contract_type="+contract_type+
			"&plant_seq="+plant_seq+"&bu_unit="+bu_unit+"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+
			"&billing_cycle="+billing_cycle+"&financial_year="+financial_year+"&price_cd="+price_cd+
			"&mkt_mrgin="+mkt_mrgin+"&oth_chrg="+oth_chrg+"&trans_tariff="+trans_tariff+"&isGrossIncTriff="+isGrossIncTriff+
			"&mkt_mrgin_amt="+mkt_mrgin_amt+"&oth_chrg_amt="+oth_chrg_amt+"&trans_tariff_amt="+trans_tariff_amt+
			"&invoice_raised_in="+invoice_raised_in+"&contact_person="+contact_person+"&bu_contact_person="+bu_contact_person+
			"&bu_state_tin="+bu_state_tin+"&invoice_dt="+invoice_dt+"&agmt_base="+agmt_base+"&tcs_tds="+tcs_tds+"&cargo_no="+cargo_no+"&inv_flag="+inv_flag;
	
		if(!newWindow || newWindow.closed)
		{
			newWindow= window.open(url,"Invoice Remittance","top=10,left=70,width=900,height=700,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow= window.open(url,"Invoice Remittance","top=10,left=70,width=900,height=700,scrollbars=1");
		}
	}
} */
</script>
</head>
<jsp:useBean class="com.etrm.fms.sales_invoice.DataBean_Sales_Drcr_note" id="Drcr_note" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String plant_seq=request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String period_start_dt=request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
String temp_period_start_dt=request.getParameter("temp_period_start_dt")==null?"":request.getParameter("temp_period_start_dt");
String temp_period_end_dt=request.getParameter("temp_period_end_dt")==null?"":request.getParameter("temp_period_end_dt");
String bu_unit=request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String exist_financial_year=request.getParameter("exist_financial_year")==null?"":request.getParameter("exist_financial_year");
String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
String operation=request.getParameter("operation")==null?"":request.getParameter("operation");
String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
String inv_flag=request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
String criteria=request.getParameter("criteria")==null?"":request.getParameter("criteria");
String drcr_flag=request.getParameter("drcr_flag")==null?"":request.getParameter("drcr_flag");
String drcr_seq=request.getParameter("drcr_seq")==null?"":request.getParameter("drcr_seq");
String remark=request.getParameter("remark")==null?"":request.getParameter("remark");
String drcr_fin_yr=request.getParameter("drcr_fin_yr")==null?"":request.getParameter("drcr_fin_yr");


String inv_dt=request.getParameter("inv_dt")==null?"":request.getParameter("inv_dt");
String user_defined_dt=request.getParameter("user_defined_dt")==null?"":request.getParameter("user_defined_dt");
String sel_exchng_cd=request.getParameter("sel_exchng_cd")==null?"":request.getParameter("sel_exchng_cd");

String exchng_rate_mapp=request.getParameter("exchng_rate_mapp")==null?"":request.getParameter("exchng_rate_mapp");


Drcr_note.setCallFlag("DEBIT_CREDIT_NOTE_GENERATION");
Drcr_note.setComp_cd(owner_cd);
Drcr_note.setCounterparty_cd(counterparty_cd);
Drcr_note.setAgmt_no(agmt_no);
Drcr_note.setAgmt_rev_no(agmt_rev);
Drcr_note.setCont_no(cont_no);
Drcr_note.setCont_rev_no(cont_rev);
Drcr_note.setContract_type(contract_type);
Drcr_note.setCargo_no(cargo_no);
Drcr_note.setPlant_seq(plant_seq);
Drcr_note.setBilling_cycle(billing_cycle);
Drcr_note.setPeriod_start_dt(period_start_dt);
Drcr_note.setPeriod_end_dt(period_end_dt);
Drcr_note.setTemp_period_start_dt(temp_period_start_dt);
Drcr_note.setTemp_period_end_dt(temp_period_end_dt);
Drcr_note.setBu_unit(bu_unit);
//Drcr_note.setFinancial_year(financial_year);
Drcr_note.setExist_financial_year(exist_financial_year);
Drcr_note.setBu_state_tin(bu_state_tin);
Drcr_note.setOperation(operation);
Drcr_note.setInv_dt(inv_dt);
Drcr_note.setUser_defined_dt(user_defined_dt);
Drcr_note.setSel_exchng_cd(sel_exchng_cd);
Drcr_note.setExchange_rate_mapping(exchng_rate_mapp);
Drcr_note.setInv_flag(inv_flag);
Drcr_note.setdrcr_seq(drcr_seq);
Drcr_note.setdrcr_flag(drcr_flag);
Drcr_note.setcriteria(criteria);
Drcr_note.setdrcr_fin_yr(drcr_fin_yr);
Drcr_note.init();

if(user_defined_dt.equals(""))
{
	user_defined_dt=Drcr_note.getUser_defined_dt();
}

//String temp_financial_year=financial_year;
//if(financial_year.equals(""))
//{
String financial_year=Drcr_note.getFinancial_year();

//}

String couterpty_abbr=Drcr_note.getCouterpty_abbr();
String couterpty_nm=Drcr_note.getCouterpty_nm();
String deal_no=Drcr_note.getDeal_no();
String contract_ref=Drcr_note.getContract_ref();
String plant_abbr=Drcr_note.getPlant_abbr();
String bu_plant_abbr=Drcr_note.getBu_plant_abbr();
String qty_mmbtu=Drcr_note.getQty_mmbtu();
String price=Drcr_note.getPrice();
String price_cd=Drcr_note.getPrice_cd();
String price_cd_nm=Drcr_note.getPrice_cd_nm();
String bu_contact_person_cd = Drcr_note.getBu_contact_person_cd();
String contact_person_cd = Drcr_note.getContact_person_cd();
String invoice_raised_in = Drcr_note.getInvoice_raised_in();
String invoice_raised_in_nm = Drcr_note.getInvoice_raised_in_nm();
String invoice_seq=Drcr_note.getInvoice_seq();
String invoice_no=Drcr_note.getInvoice_no();
String drcr_invoice_no=Drcr_note.getdrcr_invoice_no();
String drcr_ref=Drcr_note.getdrcr_ref();
String invoice_dt = Drcr_note.getInvoice_dt();
String invoice_due_dt = Drcr_note.getInvoice_due_dt();
String gross_amt=Drcr_note.getGross_amt();
String gross_amt1=Drcr_note.getGross_amt1();
String exchng_rate_cd=Drcr_note.getExchng_rate_cd();
String exchng_rate_cal=Drcr_note.getExchng_rate_cal();
String exchang_rate=Drcr_note.getExchang_rate();
String exchang_rate_dt=Drcr_note.getExchang_rate_dt();
String exchang_criteria=Drcr_note.getExchang_criteria();
String last_avlb_exchng_dt = Drcr_note.getLast_avlb_exchng_dt();
String lable_inv_criteria = Drcr_note.getLable_inv_criteria();
String lable_inv_date = Drcr_note.getLable_inv_date();
String invoice_id_seq=Drcr_note.getInvoice_id_seq();
String criteria_desc=Drcr_note.getCriteria_desc();
String drcr_dt=Drcr_note.getdrcr_dt();
String drcr_due_dt=Drcr_note.getdrcr_due_dt();
String drcr_alloc_qty=Drcr_note.getdrcr_alloc_qty();
String drcr_price=Drcr_note.getdrcr_price();
String drcr_exchng=Drcr_note.getdrcr_exchng();

String correction_msg = Drcr_note.getCorrection_msg();
String daily_tot_amt_inr=Drcr_note.getDaily_tot_amt_inr();
String daily_tot_amt_usd=Drcr_note.getDaily_tot_amt_usd();
String daily_tot_qty=Drcr_note.getDaily_tot_qty();
String tax_amt = Drcr_note.getTax_amt();
String tax_struct_cd=Drcr_note.getTax_struct_cd();
String tax_struct_dt=Drcr_note.getTax_struct_dt();
String tax_struct_dtl=Drcr_note.getTax_struct_dtl();
String tax_info = Drcr_note.getTax_info();
String tax_factor = Drcr_note.getTax_factor();
String invoice_amt = Drcr_note.getInvoice_amt();
String net_payable = Drcr_note.getNet_payable();
String agmt_base = Drcr_note.getAgmt_base();
String transportation_charges = Drcr_note.getTransportation_charges();
String transportation_amount = Drcr_note.getTransportation_amount();
String gross_include_transport_tariff = Drcr_note.getGross_include_transport_tariff();
String marketing_margin = Drcr_note.getMarketing_margin();
String marketing_margin_amount = Drcr_note.getMarketing_margin_amount();
String other_charges = Drcr_note.getOther_charges();
String other_charges_amount = Drcr_note.getOther_charges_amount();

String plant_gstin_no = Drcr_note.getPlant_gstin_no();
String bu_gstin_no = Drcr_note.getBu_gstin_no();

String remark_1 =Drcr_note.getRemark_1();
String remark_2 =Drcr_note.getRemark_2();

boolean submission_chk = Drcr_note.getSubmission_chk();
boolean correction_needed = Drcr_note.getCorrection_needed();

String previousFinancialYear = Drcr_note.getPreviousFinancialYear();

String sellerTurnoverFlag=Drcr_note.getSellerTurnoverFlag();
String buyerTurnoverFlag=Drcr_note.getBuyerTurnoverFlag();

String applicable_flag = Drcr_note.getApplicable_flag();
String applicable_abbr = Drcr_note.getApplicable_abbr();
String applicable_amt = Drcr_note.getApplicable_amt();

String TCS_factor = Drcr_note.getTCS_factor();
String tcs_struct_cd = Drcr_note.getTcs_struct_cd();
String tcs_struct_dt = Drcr_note.getTcs_struct_dt();

String tds_amt = Drcr_note.getTds_amt();
String tds_factor = Drcr_note.getTds_factor();
String tds_struct_cd = Drcr_note.getTds_struct_cd();
String tds_struct_dt = Drcr_note.getTds_struct_dt();

String inv_entered_on = Drcr_note.getDrcr_Inv_entered_on();
String inv_approved_on = Drcr_note.getdrcr_inv_approved_on();
String drcr_inv_info_msg = Drcr_note.getDrcr_Inv_entered_on();

Vector VCONTACT_PERSON = Drcr_note.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = Drcr_note.getVCONTACT_PERSON_CD();
Vector VBU_CONTACT_PERSON = Drcr_note.getVBU_CONTACT_PERSON();
Vector VBU_CONTACT_PERSON_CD = Drcr_note.getVBU_CONTACT_PERSON_CD();

Vector VP_EXCHNG_RATE_CD = Drcr_note.getVP_EXCHNG_RATE_CD();
Vector VP_EXCHNG_RATE_VALUE = Drcr_note.getVP_EXCHNG_RATE_VALUE();
Vector VP_BG_COLOR = Drcr_note.getVP_BG_COLOR();
Vector VB_EXCHNG_RATE_CD = Drcr_note.getVB_EXCHNG_RATE_CD();
Vector VB_EXCHNG_RATE_VALUE = Drcr_note.getVB_EXCHNG_RATE_VALUE();
Vector VB_BG_COLOR = Drcr_note.getVB_BG_COLOR();
Vector VU_EXCHNG_RATE_CD = Drcr_note.getVU_EXCHNG_RATE_CD();
Vector VU_EXCHNG_RATE_VALUE = Drcr_note.getVU_EXCHNG_RATE_VALUE();
Vector VU_BG_COLOR = Drcr_note.getVU_BG_COLOR();
Vector VEXCHNG_RATE_CD = Drcr_note.getVEXCHNG_RATE_CD();
Vector VEXCHNG_RATE_NM = Drcr_note.getVEXCHNG_RATE_NM();
Vector VEXCHNG_RATE_FLAG = Drcr_note.getVEXCHNG_RATE_FLAG();

Vector VEXCHNG_RATE_CAL_CD = Drcr_note.getVEXCHNG_RATE_CAL_CD();
Vector VEXCHNG_RATE_CAL_VAL = Drcr_note.getVEXCHNG_RATE_CAL_VAL();
Vector VEXCHNG_RATE_CAL_COLOR = Drcr_note.getVEXCHNG_RATE_CAL_COLOR();

Vector VALLOCATION_DT = Drcr_note.getVALLOCATION_DT();
Vector VPRICE = Drcr_note.getVPRICE();
Vector VALLOCATION_QTY = Drcr_note.getVALLOCATION_QTY();
Vector VAMOUNT_USD = Drcr_note.getVAMOUNT_USD();
Vector VAMOUNT_INR = Drcr_note.getVAMOUNT_INR();

Vector VDEAL_NO = Drcr_note.getVDEAL_NO();
Vector VCONT_REF_NO = Drcr_note.getVCONT_REF_NO();
Vector VINVOICE_AMT = Drcr_note.getVINVOICE_AMT();
Vector VINVOICE_DT = Drcr_note.getVINVOICE_DT();

Vector VINVOICE_NO = Drcr_note.getVINVOICE_NO();
Vector VMULTI_TAX_STRUCT = Drcr_note.getVMULTI_TAX_STRUCT();

String total_InvoiceAmt = Drcr_note.getTotal_InvoiceAmt();

boolean isGrossIncTriff = Drcr_note.getIsGrossIncTriff();

String exchange_rate_mapping=Drcr_note.getExchange_rate_mapping();
if(exchng_rate_mapp.equals(""))
{
	exchng_rate_mapp=exchange_rate_mapping;
}

String type = "";
if(drcr_flag.equals("DR")) {
	type = "Debit Note";
}
else {
	type = "Credit Note";
}

String inv_info_msg="";
if(!inv_entered_on.equals(""))
{
	inv_info_msg= type+" Entered On : <b>"+inv_entered_on+"</b>";
}
if(!inv_approved_on.equals(""))
{
	inv_info_msg+=" "+type+" Approved On : <b>"+inv_approved_on+"</b>";
}

%>
<body onload="<%if(!msg.equals("")){ %>Do_close('<%=msg%>','<%=msg_type%>','<%=accroid%>');<%} %> <%if(!inv_entered_on.equals("")){ %> changeAmount();<%} %>"
<%if(submission_chk && operation.equals("INSERT")) {%>style="pointer-events: none;"<%} %>>

<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_Sales_Drcr_note">
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
					    	Debit/Credit Note Detail
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
						<div class="topheader">
					    	<%if(criteria.equals("1")) {%>
								CHANGE IN EXCHANGE RATE
							<%} else if(criteria.equals("2")){%>
								CHANGE IN PRICE
							<%} else if(criteria.equals("3")){%>
								CHANGE IN QUANTITY
							<%} %>
					    </div>
							<%-- <h5>
								<span class="badge bg-info text-dark">TCS/TDS Applicable : <%=applicable_abbr%></span>
								<%if(!contract_type.equals("Q") && !contract_type.equals("O")) {%>
								&nbsp;&nbsp;<i class="fa fa-info-circle fa-lg" title="Click to View Invoice" style="color:#0000cc;" data-bs-toggle="modal" data-bs-target="#TcsInvDtl"></i>
								<%} %>
								<%if(applicable_abbr.equals("TDS")) {%>&nbsp;&nbsp;<span class="badge bg-info text-dark"><%=tds_amt%> INR</span><%} %>
							</h5> --%>
							<div class="col-1">
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
											<th>
												<!-- <span class="badge rounded-pill" style="background:orange;color:black;font-size:12px;">
													<input type="radio" name="gta_invoice_type" value="S" checked onclick="doEnabled(this);">&nbsp;System Generated
												</span> -->
												Invoice Details
											</th>
											<th>
												<%-- <span class="badge rounded-pill" style="background:skyblue;color:black;font-size:12px;">
													<input type="radio" name="gta_invoice_type" value="P" onclick="doEnabled(this);" <%if(!invoice_check_flag.equals("Y")){ %>disabled<%} %>>&nbsp;Party Generated
												</span> --%>
												<%if(drcr_flag.equals("DR")) {%>
													Debit Note Details 
												<%} else {%>
													Credit Note Details
												<%} %>
											</th>
											<th>
												Difference
											</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<%-- <td><b>Invoice #<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<select class="form-select form-select-sm" name="invoice_id_seq" style="background:yellow;font-weight:bold;" onchange="setInvoiceNo();">
														<option value="">--Select--</option>
														<%for(int i=0; i<VINVOICE_ID_SEQ.size();i++){ %>
														<option value="<%=VINVOICE_ID_SEQ.elementAt(i)%>"><%=VINVOICE_NO.elementAt(i)%></option>
														<%} %>
													</select>
													<script>document.forms[0].invoice_id_seq.value="<%=invoice_id_seq%>"</script>
													<input type="hidden" class="form-control form-control-sm" name="invoice_no" value="<%=invoice_no%>" readOnly>
												</div>
											</td> --%>
										</tr>
										<tr>
										<td><b>Invoice No<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<input type="text" class="form-control form-control-sm" name="invoice_no" value="<%=invoice_no%>" style="color:blue;" readOnly>
													<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>" readOnly>
													<input type="hidden" name="financial_year" value="<%=financial_year%>" readOnly >
													<input type="hidden" name="invoice_no" value="<%=invoice_no%>" readOnly>
												</div>
											</td>
										<!-- <td><b>Debit/Credit Note No<span class="s-red">*</span></b></td> -->
											<td align="center">
												<div style="width:200px;">
												<%-- <%System.out.println(">>>>"+invoice_no); %> --%>
													<input type="text" class="form-control form-control-sm" name="drcr_invoice_no" value="<%=drcr_invoice_no%>" style="color:blue;" readOnly>
													<input type="hidden" name="drcr_seq" value="<%=drcr_seq%>">
													<input type="hidden" name="drcr_financial_year" value="<%=financial_year%>">
													<input type="hidden" name="drcr_invoice_no" value="<%=drcr_invoice_no%>">
													<input type="hidden" name="drcr_ref" value="<%=drcr_ref%>">
												</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b>Invoice Date<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="invoice_dt" value="<%=invoice_dt%>" maxLength="10" 
							      						onblur="validateDate(this);" 
							      						onchange="validateDate(this);refresh();" autocomplete="off" style="pointer-events: none;" readOnly>
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
						      						<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
						      						<input type="hidden" name="exist_invoice_seq" value="<%=invoice_seq%>">
					      						</div>
											</td>
											<!-- <td><b>Debit/Credit Note Date<span class="s-red">*</span></b></td> -->
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="drcr_invoice_dt" value="<%=drcr_dt%>" maxLength="10" 
							      						onblur="validateDate(this);" 
							      						onchange="handleField();validateDate(this); " autocomplete="off"
							      						<%if(!drcr_invoice_no.equals("") && drcr_invoice_no != null) {%>
							      						readOnly
							      						<%} %>
							      						>
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
						      						<input type="hidden" name="invoice_seq" value="<%=drcr_seq%>">
						      						<input type="hidden" name="exist_invoice_seq" value="<%=drcr_seq%>">
					      						</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b>Invoice Due Date</b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="invoice_due_dt" value="<%=invoice_due_dt%>" maxLength="10" 
							      						onblur="validateDate(this);" 
							      						onchange="validateDate(this);" autocomplete="off" style="pointer-events: none;" readOnly>
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
						      					</div>
											</td>
											<!-- <td><b>Payment Due Date<span class="s-red">*</span></b></td> -->
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="drcr_invoice_due_dt" value="<%=drcr_due_dt%>" maxLength="10" 
							      						onblur="validateDate(this);" 
							      						onchange="handleField();validateDate(this); " autocomplete="off"
							      						<%if(!drcr_invoice_no.equals("") && drcr_invoice_no != null) {%>
							      						readOnly
							      						<%} %>
							      						>
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
						      					</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b>Allocated Qty</b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="alloc_qty" value="<%=qty_mmbtu%>" readOnly>
														<span class="input-group-text">MMBTU</span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
													<% if(criteria.contains("3")) {%>
														<input type="text" class="form-control form-control-sm" name="drcr_alloc_qty" value="<%=drcr_alloc_qty%>" onkeyup="changeAmount();"
															<%if(!drcr_invoice_no.equals("") && drcr_invoice_no != null) {%>
								      						readOnly
								      						<%} %>
								      						>
													<%} else {%>
														<input type="text" class="form-control form-control-sm" name="drcr_alloc_qty" value="<%=qty_mmbtu%>" readOnly>
													<%}%>
														<span class="input-group-text">MMBTU</span>
													</div>
												</div>
											</td>
											<td id="diff_qty"></td>
										</tr>
										<tr>
											<td><b>Confirmed Price<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="price" value="<%=price%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="price_cd" value="<%=price_cd%>">
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
													<% if(criteria.contains("2")) {%>
														<input type="text" class="form-control form-control-sm" name="drcr_price" value="<%=drcr_price%>" onkeyup="checkNumber1(this,8,4);changeAmount();" onChange=" validation();changeAmount(); "
														<%if(!drcr_invoice_no.equals("") && drcr_invoice_no != null) {%>
								      						readOnly
								      						<%} %>
								      						>
													<%} else {%>
														<input type="text" class="form-control form-control-sm" name="drcr_price" value="<%=price%>" readOnly>
													<%}%>
														<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="drcr_price_cd" value="<%=price_cd%>">
												</div>
											</td>
											<td id="diff_price"></td>
										</tr>
										<tr <%if(!price_cd.equals(invoice_raised_in)){ %><%}else{ %>style="display:none;"<%} %>>
											<td><b>Gross Amount</b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="gross_amt" value="<%=gross_amt%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
													<% if(criteria.contains("2") || criteria.contains("3")) {%>
														<input type="text" class="form-control form-control-sm" name="drcr_gross_amt" value="" readOnly>
													<%} else {%>
														<input type="text" class="form-control form-control-sm" name="drcr_gross_amt" value="<%=gross_amt%>" readOnly>
													<%}%>
														
														<span class="input-group-text"><%=price_cd_nm%></span>
													</div>
												</div>
											</td>
											<td id="diff_gross"></td>
										</tr>
										<tr <%if(!price_cd.equals(invoice_raised_in)){ %><%}else{ %>style="display:none;"<%} %>>
											<td><b><span style="background:yellow;<%if(exchang_rate.equals("0.0000") || exchang_rate.equals("")){%>color:red;<%}%>">Exchange Rate</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="exchng_rate" value="<%=exchang_rate%>" readOnly <%if(exchang_rate.equals("0.00") || exchang_rate.equals("")){%>style="color:red;"<%}%>>
														<span class="input-group-text">INR/USD</span>
													</div>
													<input type="hidden" name="exchng_cd" value="<%=exchng_rate_cd%>">
													<input type="hidden" name="exchng_dt" value="<%=exchang_rate_dt%>"> 
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
													<% if(criteria.contains("1")) {%>
														<input type="text" class="form-control form-control-sm" name="drcr_exchng_rate" value="<%=drcr_exchng%>" <%if(exchang_rate.equals("0.00") || exchang_rate.equals("")){%>style="color:red;"<%}%> onkeyup="checkNumber1(this,7,4);changeAmount();" onChange=" validation();"
														<%if(!drcr_invoice_no.equals("") && drcr_invoice_no != null) {%>
								      						readOnly
								      						<%} %>
								      						>
													<%} else {%>
														<input type="text" class="form-control form-control-sm" name="drcr_exchng_rate" value="<%=exchang_rate%>" readOnly  <%if(exchang_rate.equals("0.00") || exchang_rate.equals("")){%>style="color:red;"<%}%>>
													<%}%>
														
														<span class="input-group-text">INR/USD</span>
													</div>
													<input type="hidden" name="exchng_cd" value="<%=exchng_rate_cd%>">
													<input type="hidden" name="exchng_dt" value="<%=exchang_rate_dt%>"> 
												</div>
											</td>
											<td id="diff_exchng" ></td>
										</tr>
										<tr>
											<td><b>Gross Amount<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="gross_amt1" value="<%=gross_amt1%>" <%if(correction_needed){%>style="color:red;"<%} %> readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="invoice_raised_in" value="<%=invoice_raised_in%>">
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
													<% if(criteria.contains("1") || criteria.contains("2") || criteria.contains("3")){%>
														<input type="text" class="form-control form-control-sm" name="drcr_gross_amt1" value="" readOnly>
													<%}else{ %>
														<input type="text" class="form-control form-control-sm" name="drcr_gross_amt1" value="<%=gross_amt1%>" <%if(correction_needed){%>style="color:red;"<%} %> readOnly>
													<%} %>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="invoice_raised_in" value="<%=invoice_raised_in%>">
												</div>
											</td>
											<td id="diff_gross1"></td>
										</tr>
										<%-- <tr <%if(!agmt_base.equals("D")){ %>style="display:none;"<%} %>>
										<tr <%if(transportation_charges.equals("") && !criteria.contains("4")){ %>style="display:none;"<%} %>>
											<td><b>Transportation Tariff (<%=transportation_charges%> INR/MMBTU)<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="transportation_amount" value="<%=transportation_amount%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="transportation_tariff" value="<%=transportation_charges%>">
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
													<% if(criteria.contains("4")) {%>
															<input type="text" class="form-control form-control-sm" name="drcr_transportation_tariff" value="" onkeyup="changeCharges();"
															<%if(!drcr_invoice_no.equals("") && drcr_invoice_no != null) {%>
								      						readOnly
								      						<%} %>>
															<span class="input-group-text"><%=invoice_raised_in_nm%>/MMBTU</span>
															&nbsp;&nbsp; 
															<input type="text" class="form-control form-control-sm" name="drcr_transportation_amount" value="" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														<%} else {%>
															<input type="text" class="form-control form-control-sm" name="drcr_transportation_amount" value="<%=transportation_amount%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
															<input type="hidden" class="form-control form-control-sm" name="drcr_transportation_tariff" value="<%=transportation_charges%>">
														<%} %>
													</div>
												</div>
											</td>
											<td id="diff_trans"></td>
										</tr> --%>
										<%--<tr <%if(marketing_margin.equals("")){ %>style="display:none;"<%} %>>
											<td><b>Marketing Margin (<%=marketing_margin%> INR/MMBTU)<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="marketing_margin_amount" value="<%=marketing_margin_amount%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="marketing_margin" value="<%=marketing_margin%>">
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="marketing_margin_amount" value="<%=marketing_margin_amount%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="drcr_marketing_margin" value="<%=marketing_margin%>">
												</div>
											</td>
											<td id="diff_market"></td>
										</tr>
										<tr <%if(other_charges.equals("")){ %>style="display:none;"<%} %>>
											<td><b>Other Charges (<%=other_charges%> INR/MMBTU)<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="other_charges_amount" value="<%=other_charges_amount%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="other_charges" value="<%=other_charges%>">
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="other_charges_amount" value="<%=other_charges_amount%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="drcr_other_charges" value="<%=other_charges%>">
												</div>
											</td>
											<td id="diff_other"></td>
										</tr>--%>
										<%-- <tr <%if(!isGrossIncTriff){ %>style="display:none;"<%} %>>
											<td><b>Total Gross Amount<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="gross_include_transport_tariff" value="<%=gross_include_transport_tariff%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="drcr_gross_include_transport_tariff" value="" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td id="diff_grosstrans"></td>
										</tr>  --%>
										<tr>
											<td><b>Tax (<%=tax_struct_dtl%>)</b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="tax_amt" value="<%=tax_amt%>" title="<%=tax_info%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" name="tax_cd" value="<%=tax_struct_cd%>">
													<input type="hidden" name="tax_dt" value="<%=tax_struct_dt%>">
													<input type="hidden" name="tax_factor" value="<%=tax_factor%>">
													<input type="hidden" name="tax_struct_dtl" value="<%=tax_struct_dtl%>">
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
													<% if(criteria.contains("1") || criteria.contains("2") || criteria.contains("3") ) {%>
														<input type="text" class="form-control form-control-sm" name="drcr_tax_amt" value="" title="<%=tax_info%>" readOnly>
													<%} else {%>
														<input type="text" class="form-control form-control-sm" name="drcr_tax_amt" value="<%=tax_amt%>" title="<%=tax_info%>" readOnly>
													<% }%>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" name="drcr_tax_cd" value="<%=tax_struct_cd%>">
													<input type="hidden" name="drcr_tax_dt" value="<%=tax_struct_dt%>">
													<input type="hidden" name="drcr_tax_factor" value="<%=tax_factor%>">
													<input type="hidden" name="drcr_tax_struct_dtl" value="<%=tax_struct_dtl%>">
												</div>
											</td>
											<td id="diff_tax"></td>
										</tr>
										<%if(VMULTI_TAX_STRUCT.size()>0){
											for(int i=0;i<VMULTI_TAX_STRUCT.size();i++)
											{
												Vector temp =(Vector)((Vector)((Vector)VMULTI_TAX_STRUCT.elementAt(i)));
												
												for(int j=0;j<((Vector) temp.elementAt(0)).size(); j++)
												{%>
													<tr style="background: #cff4fc;<%if(((Vector) temp.elementAt(0)).size()==1){%>display:none;<%} %>" >
														<td align="right"><b><%=((Vector) temp.elementAt(1)).elementAt(j)%></b></td>
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
														<td align="center">
															<div style="width:200px;">
																<div class="input-group input-group-sm" >
																<% if(criteria.contains("1") || criteria.contains("2") || criteria.contains("3")){%>
																	<input type="text" class="form-control form-control-sm" name="drcr_sub_tax_amt" value="" readOnly>
																<%} else {%>
																	<input type="text" class="form-control form-control-sm" name="drcr_sub_tax_amt" value="<%=((Vector) temp.elementAt(2)).elementAt(j)%>" readOnly>
																<% }%>
																	<span class="input-group-text"><%=invoice_raised_in_nm%></span>
																</div>
																<input type="hidden" name="drcr_sub_tax_code" value="<%=((Vector) temp.elementAt(0)).elementAt(j)%>">
																<input type="hidden" name="drcr_sub_tax_struct" value="<%=((Vector) temp.elementAt(1)).elementAt(j)%>">
																<input type="hidden" name="drcr_sub_tax_base_amt" value="<%=((Vector) temp.elementAt(3)).elementAt(j)%>">
															</div>
														</td>
														<td id="diff_subtax"></td>
													</tr>
												<%}
											}
										} %>
										<tr>
											<td><b>Invoice Amount</b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="invoice_amt" value="<%=invoice_amt%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="drcr_invoice_amt" value="" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td id="diff_invamt"></td>
										</tr>
										<%-- <%if(applicable_abbr.equals("TCS")){ %>
										<tr>
											<td><b>TCS (<%=TCS_factor%>%)</b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="tcs_amt" value="<%=applicable_amt%>" readOnly>
														<input type="hidden" class="form-control form-control-sm" name="tcs_factor" value="<%=TCS_factor%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="drcr_tcs_amt" value="<%=applicable_amt%>" readOnly>
														<input type="hidden" class="form-control form-control-sm" name="drcr_tcs_factor" value="<%=TCS_factor%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<%} %> --%>
										<tr>
											<td><b>Net Payable</b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="net_payable" value="<%=net_payable%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="drcr_net_payable" value="" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td id="diff_netamt"></td>
										</tr>
										<tr>
											<td><b>Reason<span class="s-red">*</span></b></td>
											<td align="center" colspan="3">
												<!-- <div style="width:400px;"> -->
													<textarea class="form-control form-control-sm" rows="3" cols="75" name="remark" value="<%=remark%>"><%=remark%></textarea>
												<!-- </div> -->
											</td>
										</tr>
										<tr>
											<td><b>Remark</b></td>
											<td align="center" colspan="3">
												<!-- <div style="width:400px;"> -->
													<textarea class="form-control form-control-sm" rows="3" cols="75" name="drcr_remark"></textarea>
												<!-- </div> -->
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<%if(operation.equals("INSERT") && !drcr_inv_info_msg.equals("")){ %>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div align="center"><%=utilmsg.infoMessage(inv_info_msg) %></div>
						</div>
					</div>
				</div>
				<%}%>
				<%if((submission_chk==false && operation.equals("INSERT")) || operation.equals("MODIFY")){ %>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<div class="row justify-content-end">
							<!-- <div class="col-auto">
								<i class="fa fa-eye fa-2x" onclick="viewBeforeSubmit();"></i>
							</div> -->
							<div class="col-auto">
								<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();" <%if(correction_needed){%>disabled<%} %>>
							</div>
						</div>
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
				</div>
				<%} %>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="TcsInvDtl" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Invoice Detail
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row">
      					<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th colspan="3">Customer Name : <%=couterpty_nm%></th>
											<%-- <th colspan="3">Financial Year : <%=financial_year%></th> --%>
											<th colspan="3">Financial Year : <%=financial_year%></th>
										</tr>
										<tr>
											<th>Sr#</th>
											<th>Contract#</th>
											<th>Contract Ref#</th>
											<th>Invoice#</th>
											<th>Invoice Date</th>
											<th>Invoice Amount</th>
										</tr>
									</thead>
									<tbody>
									<%if(VDEAL_NO.size()>0){ %>
										<%for(int i=0;i<VDEAL_NO.size(); i++){ %>
										<tr>
											<td align="center"><%=i+1%></td>
											<td align="center"><%=VDEAL_NO.elementAt(i)%></td>
											<td align="center"><%=VCONT_REF_NO.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
											<td align="right"><%=VINVOICE_AMT.elementAt(i)%></td>
										</tr>
										<%} %>
										<tr>
											<td align="right" colspan="5"><b>Total :</b></td>
											<td align="right"><b><%=total_InvoiceAmt %></b></td>
										</tr>
									<%}else{ %>
										<tr>
											<td align="center" colspan="6"><%=utilmsg.infoMessage("<b>No Invoice!</b>")%></td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<%String turnFlgNm=""; %>
							<%if(sellerTurnoverFlag.equals("Y")){ 
								turnFlgNm="Yes";
							}else if(sellerTurnoverFlag.equals("N")){
								turnFlgNm="No";
							}else{
								turnFlgNm="?";
							} %>
							<%String buyTurnFlgNm=""; %>
							<%if(buyerTurnoverFlag.equals("Y")){ 
								buyTurnFlgNm="Yes";
							}else if(buyerTurnoverFlag.equals("N")){
								buyTurnFlgNm="No";
							}else{
								buyTurnFlgNm="?";
							} %>
							<%=utilmsg.infoMessage("Prev Financial Year : <b>"+previousFinancialYear+"</b> Seller Turnover > 10 cr : <b>"+turnFlgNm+"</b>  Buyer Turnover > 10 cr : <b>"+buyTurnFlgNm+"</b>") %>
						</div>
					</div>
      			</div>
      		</div>
      	</div>
	</div>
</div>

<input type="hidden" name="option" value="DRCR_NOTE">
<!-- <input type="hidden" name="opration" value="INSERT"> -->
<input type="hidden" name="opration" value="<%=operation%>">
<input type="hidden" name="operation" value="<%=operation%>">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" value="<%=agmt_rev%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev" value="<%=cont_rev%>">
<input type="hidden" name="cargo_no" value="<%=cargo_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="plant_seq" value="<%=plant_seq%>">
<input type="hidden" name="period_start_dt" value="<%=period_start_dt%>">
<input type="hidden" name="period_end_dt" value="<%=period_end_dt%>">
<input type="hidden" name="temp_period_start_dt" value="<%=temp_period_start_dt%>">
<input type="hidden" name="temp_period_end_dt" value="<%=temp_period_end_dt%>">
<input type="hidden" name="billing_cycle" value="<%=billing_cycle%>">
<input type="hidden" name="bu_unit" value="<%=bu_unit%>">
<input type="hidden" name="bu_state_tin" value="<%=bu_state_tin%>">
<%-- <input type="hidden" name="financial_year" value="<%=financial_year%>"> --%>

<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="exist_financial_year" value="<%=exist_financial_year%>">
<input type="hidden" name="inv_flag" value="<%=inv_flag%>">

<input type="hidden" name="exchg_rate_type" value="<%=exchng_rate_cal%>">
<input type="hidden" name="tcs_tds" value="<%=applicable_abbr%>">
<input type="hidden" name="tcs_struct_cd" value="<%=tcs_struct_cd%>">
<input type="hidden" name="tcs_struct_dt" value="<%=tcs_struct_dt%>">
<input type="hidden" name="agmt_base" value="<%=agmt_base%>">
<input type="hidden" name="contract_ref" value="<%=contract_ref%>">

<input type="hidden" name="tds_amt" value="<%=tds_amt%>">
<input type="hidden" name="tds_factor" value="<%=tds_factor%>">
<input type="hidden" name="tds_struct_cd" value="<%=tds_struct_cd%>">
<input type="hidden" name="tds_struct_dt" value="<%=tds_struct_dt%>">

<input type="hidden" name="criteria_desc" value="<%=criteria_desc%>">
<input type="hidden" name="criteria" value="<%=criteria%>">
<input type="hidden" name="drcr_flag" value="<%=drcr_flag%>">

<input type="hidden" name="isGrossIncTriff" value="<%=isGrossIncTriff%>">

<input type="hidden" name="exchng_rate_mapp" value="<%=exchng_rate_mapp%>">

<input type="hidden" name="accroid" value="<%=accroid%>">

<input type="hidden" name="plant_gstin_no" value="<%=plant_gstin_no%>">
<input type="hidden" name="bu_gstin_no" value="<%=bu_gstin_no%>">

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
</form>
</body>
</html>