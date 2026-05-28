<%@page import="java.util.Vector"%>
<%@page import="java.text.*"%>
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
	
	var mapping_id = document.forms[0].mapping_id.value
	var truck_trans_cd = document.forms[0].truck_trans_cd.value
	var truck_cd = document.forms[0].truck_cd.value
	
	var accroid = document.forms[0].accroid.value;
	
	var qtyMmbtu = document.forms[0].qtyMmbtu.value;
	var stateMapping = document.forms[0].stateMapping.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_generate_dlng_ltcora_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
		"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&cargo_no="+cargo_no+
		"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&inv_dt="+invoice_dt+
		"&user_defined_dt="+user_defined_dt+"&operation="+operation+"&sel_exchng_cd="+exchng_cd+
		"&exist_financial_year="+exist_financial_year+"&bu_state_tin="+bu_state_tin+"&temp_period_start_dt="+temp_period_start_dt+"&temp_period_end_dt="+temp_period_end_dt+
		"&exchng_rate_mapp="+exchng_rate_mapp+"&accroid="+accroid+"&inv_flag="+inv_flag+
		"&truck_cd="+truck_cd+"&truck_trans_cd="+truck_trans_cd+
		"&mapping_id="+mapping_id+
		"&qtyMmbtu="+qtyMmbtu+"&stateMapping="+stateMapping+
		"&u="+u;
	
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

enableButton=true;
function doSubmit()
{
	var bu_unit = document.forms[0].bu_unit.value;
	var contact_person = document.forms[0].contact_person.value
	var bu_contact_person = document.forms[0].bu_contact_person.value
	
	var contract_type = document.forms[0].contract_type.value
	
	//var invoice_no = document.forms[0].invoice_no.value
	var invoice_dt = document.forms[0].invoice_dt.value
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
	var transportation_amount = document.forms[0].transportation_amount.value;
	
	var tcs_tds = document.forms[0].tcs_tds.value;
	var tcs_struct_cd = document.forms[0].tcs_struct_cd.value;
	var tds_struct_cd = document.forms[0].tds_struct_cd.value;
	
	var plant_gstin_no = document.forms[0].plant_gstin_no.value;
	var bu_gstin_no = document.forms[0].bu_gstin_no.value;
	var sys_dt = document.forms[0].sys_dt.value;
	
	var msg="";
	var flag=true;
	
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
	if(trim(invoice_dt)=="")
	{
		msg+="Enter Invoice Date!\n";
		flag=false;
	}
	if(trim(invoice_due_dt)=="")
	{
		msg+="Enter Invoice Due Date!\n";
		flag=false;
	}
	
	if(trim(invoice_dt)!="" && trim(invoice_due_dt)!="")
	{
		var count = compareDate(invoice_dt,invoice_due_dt);
		if(parseInt(count) == 1)
		{
			msg+="Invoice Due Date should be grater or equal Invoice Date!\n";
			flag=false;
		}
	}

	if(trim(invoice_dt)!="" && trim(sys_dt)!="")
	{
		var count = compareDate(invoice_dt,sys_dt);
		if(parseInt(count) == 1)
		{
			msg+="Invoice Date should not be grater than "+sys_dt+"";
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
			if(tcs_tds=="TCS")
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
			}
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
	
	if(flag)
	{
		var a=confirm("Do you want to Submit LTCORA DLNG Invoice?");
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

var newWindow;
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
		var url = "../dlng/rpt_view_before_submit.jsp?counterparty_cd="+counterparty_cd+
			"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&contract_type="+contract_type+
			"&plant_seq="+plant_seq+"&bu_unit="+bu_unit+"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+
			"&billing_cycle="+billing_cycle+"&financial_year="+financial_year+"&price_cd="+price_cd+
			"&mkt_mrgin="+mkt_mrgin+"&oth_chrg="+oth_chrg+"&trans_tariff="+trans_tariff+
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
}

function viewBeforeForm402()
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
	
	var mapping_id = document.forms[0].mapping_id.value
	var truck_trans_cd = document.forms[0].truck_trans_cd.value
	var truck_cd = document.forms[0].truck_cd.value
	var driver_cd = document.forms[0].driver_cd.value
	var check_post_cd = document.forms[0].check_post_cd.value
	
	if(trim(driver_cd) =="" && trim(check_post_cd) =="")
	{
		alert("Select Driver Name!\nSelect Check Post!");
	}
	else if(trim(driver_cd) =="")
	{
		alert("Select Driver Name!");
	}
	else if(trim(check_post_cd) =="")
	{
		alert("Select Check Post!");
	}
	else
	{
		var url = "../dlng/rpt_view_before_form_402.jsp?counterparty_cd="+counterparty_cd+
			"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&contract_type="+contract_type+
			"&plant_seq="+plant_seq+"&bu_unit="+bu_unit+"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+
			"&financial_year="+financial_year+
			"&bu_state_tin="+bu_state_tin+"&inv_flag="+inv_flag+"&truck_cd="+truck_cd+"&truck_trans_cd="+truck_trans_cd+"&mapping_id="+mapping_id+
			"&driver_cd="+driver_cd+"&check_post_cd="+check_post_cd;

		if(!newWindow || newWindow.closed)
		{
			newWindow= window.open(url,"Invoice Remittance","top=10,left=70,width=1000,height=700,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow= window.open(url,"Invoice Remittance","top=10,left=70,width=1000,height=700,scrollbars=1");
		}	
	}
}

function proceedTruckInv()
{
	var alloc_qty = document.forms[0].alloc_qty;
	var transport_mgmt_unit = document.forms[0].transport_mgmt_unit.value;
	
	var sales_inv_qty = document.forms[0].sales_inv_qty;
	var sel_checkbox = document.forms[0].sel_checkbox;
	
	var totalAllocMmbtu = parseFloat("0");
	var noOfSelTruckInvoice = parseInt("0");

	var stateMapping = "";
	
	if(sel_checkbox.length != undefined)
	{
		for(var i=0; i<sel_checkbox.length;i++)
		{
			if(sel_checkbox[i].checked)
			{
				noOfSelTruckInvoice++;
				totalAllocMmbtu = totalAllocMmbtu +  parseFloat(sales_inv_qty[i].value);
			}
				
			stateMapping += i + (sel_checkbox[i].checked ? 'true' : 'false');
            if (i < sel_checkbox.length - 1) {
                stateMapping += '-'; // Add separator if not last item
            }
		}
	}
	else
	{
		if(sel_checkbox.checked)
		{
			noOfSelTruckInvoice++;
			totalAllocMmbtu = totalAllocMmbtu +  parseFloat(sales_inv_qty.value);
		}
		
		stateMapping = '0' + (sel_checkbox.checked ? 'true' : 'false');
	}
	
	if(transport_mgmt_unit == "3") //INR/Truck
	{
		alloc_qty.value = noOfSelTruckInvoice;
	}
	else if(transport_mgmt_unit == "4")	//INR/MT
	{
		alloc_qty.value = parseFloat(parseFloat(totalAllocMmbtu)/51.5).toFixed(2);
	}
	else
	{
		alloc_qty.value = parseFloat(totalAllocMmbtu).toFixed(2);
	}
	
	
	document.forms[0].qtyMmbtu.value = alloc_qty.value ;
	document.forms[0].stateMapping.value = stateMapping ;
	
	refresh();
}

function enableDisableTruckInv(obj,i_index)
{
	var sel_checkbox_flag = document.getElementById("sel_checkbox_flag_"+i_index);
	var truck_trans_cd = document.getElementById("truck_trans_cd_"+i_index);
	var truck_cd = document.getElementById("truck_cd_"+i_index);
	var sales_inv_qty = document.getElementById("sales_inv_qty_"+i_index);
	
	if(obj.checked)
	{
		sel_checkbox_flag.disabled=false;
		truck_trans_cd.disabled=false;
		truck_cd.disabled=false;
		sales_inv_qty.disabled=false;
	}
	else
	{
		sel_checkbox_flag.disabled=true;
		truck_trans_cd.disabled=true;
		truck_cd.disabled=true;
		sales_inv_qty.disabled=true;
	}
}

function totalInvQty()
{
	var truck_alloc_qty = document.forms[0].truck_alloc_qty;
	var truck_alloc_qty_mt = document.forms[0].truck_alloc_qty_mt;
	var total_sales_inv_qty = document.getElementById("total_sales_inv_qty");
	var disp_sales_inv_qty = document.getElementById("disp_sales_inv_qty");
	var total_sales_inv_qty_mt = document.getElementById("total_sales_inv_qty_mt");
	var disp_sales_inv_qty_mt = document.getElementById("disp_sales_inv_qty_mt");
	
	var totalQty = parseFloat("0");
	var totalQtyMt = parseFloat("0");
	
	if(truck_alloc_qty.length != undefined)
	{
		for(var i=0; i<truck_alloc_qty.length;i++)
		{
			totalQty = totalQty + parseFloat(truck_alloc_qty[i].value);
			totalQtyMt = totalQtyMt + parseFloat(truck_alloc_qty_mt[i].value);
		}
	}
	else
	{
		totalQty = parseFloat(truck_alloc_qty.value);
		totalQtyMt = parseFloat(truck_alloc_qty_mt.value);
	}
	
	total_sales_inv_qty.value = parseFloat(totalQty).toFixed(2);
	disp_sales_inv_qty.innerHTML = parseFloat(totalQty).toFixed(2);
	total_sales_inv_qty_mt.value = parseFloat(totalQtyMt).toFixed(2);
	disp_sales_inv_qty_mt.innerHTML = parseFloat(totalQtyMt).toFixed(2);
}

function setCheckboxesState()
{
    var stateMapping =document.forms[0].stateMapping.value
    var submission_chk =document.forms[0].submission_chk.value
    var operation =document.forms[0].operation.value
    var sales_inv_linked =document.forms[0].sales_inv_linked;
    
    if (stateMapping != "") 
    {
        var sel_checkbox = document.forms[0].sel_checkbox;
        
        // Split the stateMapping string into individual parts: "1true", "2false", etc.
        var states = stateMapping.split('-');
        
        // Loop through the states and restore checkbox states
        for (var i = 0; i < states.length; i++) {
            var state = states[i];
            var checkboxIndex = parseInt(state.charAt(0));  // The first character is the index (1, 2, 3, etc.)
            var isChecked = state.substring(1) === 'true';  // The rest is either 'true' or 'false'
            
            if (sel_checkbox.length !== undefined) 
            {
            	if(sales_inv_linked[checkboxIndex].value != "EL")
            	{
            		sel_checkbox[checkboxIndex].checked = isChecked;  // Restore the checkbox state
            	}
            	else
            	{
            		sel_checkbox[checkboxIndex].checked = false;
            	}
            	
            	enableDisableTruckInv(sel_checkbox[checkboxIndex],checkboxIndex)
            } 
            else 
            {
                if (checkboxIndex === 0)
                {
                	if(sales_inv_linked.value != 'EL')
                	{
                		sel_checkbox.checked = isChecked;
                	}
                	else
                	{
                		sel_checkbox.checked = false;
                	}
                	
                	enableDisableTruckInv(sel_checkbox,checkboxIndex)
                }
            }
        }
    }
    else if(operation == "INSERT")
    {
    	if(submission_chk != 'true')
    	{
    		var sel_checkbox = document.forms[0].sel_checkbox;

            if (sel_checkbox.length != undefined) 
            {
                for (var i = 0; i < sel_checkbox.length; i++) 
                {
                	if(sales_inv_linked[i].value != 'EL')
                	{
    	            	sel_checkbox[i].checked = true;
                	}
                	else
                	{
                		sel_checkbox[i].checked = false;
                	}
                	
                	enableDisableTruckInv(sel_checkbox[i],i);
                }
            } 
            else
            { 
            	if(sales_inv_linked.value != 'EL')
            	{
                	sel_checkbox.checked = true;
            	}
            	else
            	{
            		sel_checkbox.checked = false;
            	}
            	
            	enableDisableTruckInv(sel_checkbox,0);
            }
    	}
    	else
    	{
    		var sel_checkbox = document.forms[0].sel_checkbox;

            if (sel_checkbox.length != undefined) 
            {
                for (var i = 0; i < sel_checkbox.length; i++) 
                {
                	if(sales_inv_linked[i].value != 'EL' && sales_inv_linked[i].value == 'Y' )
                	{
    	            	sel_checkbox[i].checked = true;
                	}
                	else
                	{
                		sel_checkbox[i].checked = false;
                	}
                	
                	enableDisableTruckInv(sel_checkbox[i],i);
                }
            } 
            else
            { 
            	if(sales_inv_linked.value != 'EL' && sales_inv_linked.value == 'Y')
            	{
                	sel_checkbox.checked = true;
            	}
            	else
            	{
            		sel_checkbox.checked = false;
            	}
            	
            	enableDisableTruckInv(sel_checkbox,0);
            }
    	}
    }
};
</script>

<style>
.icon-wrapper {
  position: relative;
  display: inline-block;
  width: 2.5em;
  height: 2.5em;
  text-align: center;
}

.icon-wrapper i {
  color: black; /* Excel green */
}

.icon-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -45%);
  font-size: 0.7em;
  color: white;
  font-weight: bold;
  pointer-events: none;
}

</style>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="dlng_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utilDate" scope="request"></jsp:useBean>
<%
String sysdt = utilDate.getSysdate();

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
String mapping_id=request.getParameter("mapping_id")==null?"":request.getParameter("mapping_id");
String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"":request.getParameter("truck_trans_cd");
String truck_cd=request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");

String inv_dt=request.getParameter("inv_dt")==null?"":request.getParameter("inv_dt");
String user_defined_dt=request.getParameter("user_defined_dt")==null?"":request.getParameter("user_defined_dt");
String sel_exchng_cd=request.getParameter("sel_exchng_cd")==null?"":request.getParameter("sel_exchng_cd");

String exchng_rate_mapp=request.getParameter("exchng_rate_mapp")==null?"":request.getParameter("exchng_rate_mapp");

String qtyMmbtu=request.getParameter("qtyMmbtu")==null?"0":request.getParameter("qtyMmbtu");
String stateMapping=request.getParameter("stateMapping")==null?"":request.getParameter("stateMapping");

dlng_inv.setCallFlag("DLNG_LTCORA_INVOICE_GENERATION");
dlng_inv.setComp_cd(owner_cd);
dlng_inv.setCounterparty_cd(counterparty_cd);
dlng_inv.setAgmt_no(agmt_no);
dlng_inv.setAgmt_rev_no(agmt_rev);
dlng_inv.setCont_no(cont_no);
dlng_inv.setCont_rev_no(cont_rev);
dlng_inv.setContract_type(contract_type);
dlng_inv.setCargo_no(cargo_no);
dlng_inv.setPlant_seq(plant_seq);
dlng_inv.setBilling_cycle(billing_cycle);
dlng_inv.setPeriod_start_dt(period_start_dt);
dlng_inv.setPeriod_end_dt(period_end_dt);
dlng_inv.setTemp_period_start_dt(temp_period_start_dt);
dlng_inv.setTemp_period_end_dt(temp_period_end_dt);
dlng_inv.setBu_unit(bu_unit);
//dlng_inv.setFinancial_year(financial_year);
dlng_inv.setExist_financial_year(exist_financial_year);
dlng_inv.setBu_state_tin(bu_state_tin);
dlng_inv.setOperation(operation);
dlng_inv.setInv_dt(inv_dt);
dlng_inv.setUser_defined_dt(user_defined_dt);
dlng_inv.setSel_exchng_cd(sel_exchng_cd);
dlng_inv.setExchange_rate_mapping(exchng_rate_mapp);
dlng_inv.setInv_flag(inv_flag);
dlng_inv.setMapping_id(mapping_id);
dlng_inv.setTruck_trans_cd(truck_trans_cd);
dlng_inv.setTruck_cd(truck_cd);
dlng_inv.setQtyMmbtu(qtyMmbtu);
dlng_inv.init();

final NumberFormat nf = new DecimalFormat("###########0.00");

if(user_defined_dt.equals(""))
{
	user_defined_dt=dlng_inv.getUser_defined_dt();
}

//String temp_financial_year=financial_year;
//if(financial_year.equals(""))
//{
String financial_year=dlng_inv.getFinancial_year();

//}
String couterpty_abbr=dlng_inv.getCouterpty_abbr();
String couterpty_nm=dlng_inv.getCouterpty_nm();
String deal_no=dlng_inv.getDeal_no();
String contract_ref=dlng_inv.getContract_ref();
String plant_abbr=dlng_inv.getPlant_abbr();
String bu_plant_abbr=dlng_inv.getBu_plant_abbr();
String qty_mmbtu=dlng_inv.getQty_mmbtu();
String qty_unit=dlng_inv.getQty_unit();
String qty_unit_nm=dlng_inv.getQty_unit_nm();
String dist_unit=dlng_inv.getDist_unit();
String price=dlng_inv.getPrice();
String price_cd=dlng_inv.getPrice_cd();
String price_cd_nm=dlng_inv.getPrice_cd_nm();
String bu_contact_person_cd = dlng_inv.getBu_contact_person_cd();
String contact_person_cd = dlng_inv.getContact_person_cd();
String invoice_raised_in = dlng_inv.getInvoice_raised_in();
String invoice_raised_in_nm = dlng_inv.getInvoice_raised_in_nm();
String invoice_seq=dlng_inv.getInvoice_seq();
String invoice_no=dlng_inv.getInvoice_no();
String invoice_dt = dlng_inv.getInvoice_dt();
String invoice_due_dt = dlng_inv.getInvoice_due_dt();
String gross_amt=dlng_inv.getGross_amt();
String gross_amt1=dlng_inv.getGross_amt1();
String exchng_rate_cd=dlng_inv.getExchng_rate_cd();
String exchng_rate_cal=dlng_inv.getExchng_rate_cal();
String exchang_rate=dlng_inv.getExchang_rate();
String exchang_rate_dt=dlng_inv.getExchang_rate_dt();
String exchang_criteria=dlng_inv.getExchang_criteria();
String last_avlb_exchng_dt = dlng_inv.getLast_avlb_exchng_dt();
String lable_inv_criteria = dlng_inv.getLable_inv_criteria();
String lable_inv_date = dlng_inv.getLable_inv_date();
String invoice_id_seq=dlng_inv.getInvoice_id_seq();

String correction_msg = dlng_inv.getCorrection_msg();
String daily_tot_amt_inr=dlng_inv.getDaily_tot_amt_inr();
String daily_tot_amt_usd=dlng_inv.getDaily_tot_amt_usd();
String daily_tot_qty=dlng_inv.getDaily_tot_qty();
String tax_amt = dlng_inv.getTax_amt();
String tax_struct_cd=dlng_inv.getTax_struct_cd();
String tax_struct_dt=dlng_inv.getTax_struct_dt();
String tax_struct_dtl=dlng_inv.getTax_struct_dtl();
String tax_info = dlng_inv.getTax_info();
String tax_factor = dlng_inv.getTax_factor();
String invoice_amt = dlng_inv.getInvoice_amt();
String net_payable = dlng_inv.getNet_payable();
String agmt_base = dlng_inv.getAgmt_base();
String transportation_charges = dlng_inv.getTransportation_charges();
String transportation_amount = dlng_inv.getTransportation_amount();
String gross_include_transport_tariff = dlng_inv.getGross_include_transport_tariff();
String marketing_margin = dlng_inv.getMarketing_margin();
String marketing_margin_amount = dlng_inv.getMarketing_margin_amount();
String other_charges = dlng_inv.getOther_charges();
String other_charges_amount = dlng_inv.getOther_charges_amount();

String plant_gstin_no = dlng_inv.getPlant_gstin_no();
String bu_gstin_no = dlng_inv.getBu_gstin_no();

String remark_1 =dlng_inv.getRemark_1();
String remark_2 =dlng_inv.getRemark_2();

boolean submission_chk = dlng_inv.getSubmission_chk();
boolean correction_needed = dlng_inv.getCorrection_needed();

String previousFinancialYear = dlng_inv.getPreviousFinancialYear();

String sellerTurnoverFlag=dlng_inv.getSellerTurnoverFlag();
String buyerTurnoverFlag=dlng_inv.getBuyerTurnoverFlag();

String applicable_flag = dlng_inv.getApplicable_flag();
String applicable_abbr = dlng_inv.getApplicable_abbr();
String applicable_amt = dlng_inv.getApplicable_amt();

String TCS_factor = dlng_inv.getTCS_factor();
String tcs_struct_cd = dlng_inv.getTcs_struct_cd();
String tcs_struct_dt = dlng_inv.getTcs_struct_dt();

String tds_amt = dlng_inv.getTds_amt();
String tds_factor = dlng_inv.getTds_factor();
String tds_struct_cd = dlng_inv.getTds_struct_cd();
String tds_struct_dt = dlng_inv.getTds_struct_dt();

String inv_entered_on = dlng_inv.getInv_entered_on();
String inv_approved_on = dlng_inv.getInv_approved_on();

Vector VCONTACT_PERSON = dlng_inv.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = dlng_inv.getVCONTACT_PERSON_CD();
Vector VBU_CONTACT_PERSON = dlng_inv.getVBU_CONTACT_PERSON();
Vector VBU_CONTACT_PERSON_CD = dlng_inv.getVBU_CONTACT_PERSON_CD();

Vector VP_EXCHNG_RATE_CD = dlng_inv.getVP_EXCHNG_RATE_CD();
Vector VP_EXCHNG_RATE_VALUE = dlng_inv.getVP_EXCHNG_RATE_VALUE();
Vector VP_BG_COLOR = dlng_inv.getVP_BG_COLOR();
Vector VB_EXCHNG_RATE_CD = dlng_inv.getVB_EXCHNG_RATE_CD();
Vector VB_EXCHNG_RATE_VALUE = dlng_inv.getVB_EXCHNG_RATE_VALUE();
Vector VB_BG_COLOR = dlng_inv.getVB_BG_COLOR();
Vector VU_EXCHNG_RATE_CD = dlng_inv.getVU_EXCHNG_RATE_CD();
Vector VU_EXCHNG_RATE_VALUE = dlng_inv.getVU_EXCHNG_RATE_VALUE();
Vector VU_BG_COLOR = dlng_inv.getVU_BG_COLOR();
Vector VEXCHNG_RATE_CD = dlng_inv.getVEXCHNG_RATE_CD();
Vector VEXCHNG_RATE_NM = dlng_inv.getVEXCHNG_RATE_NM();
Vector VEXCHNG_RATE_FLAG = dlng_inv.getVEXCHNG_RATE_FLAG();

Vector VEXCHNG_RATE_CAL_CD = dlng_inv.getVEXCHNG_RATE_CAL_CD();
Vector VEXCHNG_RATE_CAL_VAL = dlng_inv.getVEXCHNG_RATE_CAL_VAL();
Vector VEXCHNG_RATE_CAL_COLOR = dlng_inv.getVEXCHNG_RATE_CAL_COLOR();

Vector VALLOCATION_DT = dlng_inv.getVALLOCATION_DT();
Vector VPRICE = dlng_inv.getVPRICE();
Vector VALLOCATION_QTY = dlng_inv.getVALLOCATION_QTY();
Vector VAMOUNT_USD = dlng_inv.getVAMOUNT_USD();
Vector VAMOUNT_INR = dlng_inv.getVAMOUNT_INR();

Vector VDEAL_NO = dlng_inv.getVDEAL_NO();
Vector VCONT_REF_NO = dlng_inv.getVCONT_REF_NO();
Vector VINVOICE_AMT = dlng_inv.getVINVOICE_AMT();
Vector VINVOICE_DT = dlng_inv.getVINVOICE_DT();

Vector VINVOICE_NO = dlng_inv.getVINVOICE_NO();
Vector VMULTI_TAX_STRUCT = dlng_inv.getVMULTI_TAX_STRUCT();

Vector VSALES_TRUCK_CD = dlng_inv.getVSALES_TRUCK_CD();
Vector VSALES_TRUCK_REG_NO = dlng_inv.getVSALES_TRUCK_REG_NO();
Vector VSALES_INV_QTY = dlng_inv.getVSALES_INV_QTY();
Vector VSALES_INV_QTY_MT = dlng_inv.getVSALES_INV_QTY_MT();
Vector VSALES_MAPPING_ID = dlng_inv.getVSALES_MAPPING_ID();
Vector VSALES_TRUCK_TRANS_CD = dlng_inv.getVSALES_TRUCK_TRANS_CD();
Vector VSELL_PERIOD_START_DT = dlng_inv.getVSELL_PERIOD_START_DT();
Vector VSELL_PERIOD_END_DT = dlng_inv.getVSELL_PERIOD_END_DT();
Vector VFILL_STATION_CD = dlng_inv.getVFILL_STATION_CD();
Vector VFILL_STATION_ABBR = dlng_inv.getVFILL_STATION_ABBR();
Vector VDISP_SLOT_DTL = dlng_inv.getVDISP_SLOT_DTL();
Vector VBAY_CD = dlng_inv.getVBAY_CD();
Vector VBAY_NM = dlng_inv.getVBAY_NM();

Vector VGAS_DT = dlng_inv.getVGAS_DT();

String linked_driver_cd = dlng_inv.getLinked_driver_cd();
String linked_checkpost_cd = dlng_inv.getLinked_checkpost_cd();

String total_InvoiceAmt = dlng_inv.getTotal_InvoiceAmt();
String total_truck_invoice = dlng_inv.getTotal_truck_invoice();
String transport_mgmt_unit = dlng_inv.getTransport_mgmt_unit();
String transport_mgmt_unit_nm = dlng_inv.getTransport_mgmt_unit_nm();
String transport_mgmt_charge = dlng_inv.getTransport_mgmt_charge();

String exchange_rate_mapping=dlng_inv.getExchange_rate_mapping();
if(exchng_rate_mapp.equals(""))
{
	exchng_rate_mapp=exchange_rate_mapping;
}

String inv_info_msg="";
if(!inv_entered_on.equals(""))
{
	inv_info_msg="Invoice Entered On : <b>"+inv_entered_on+"</b>";
}
if(!inv_approved_on.equals(""))
{
	inv_info_msg+=" Invoice Approved On : <b>"+inv_approved_on+"</b>";
}
%>
<body onload="totalInvQty();<%if(!msg.equals("")){ %>Do_close('<%=msg%>','<%=msg_type%>','<%=accroid%>');<%} %>"
<%if(submission_chk && operation.equals("INSERT")) {%>style="pointer-events: none;"<%} %>>

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
					    	LTCORA DLNG Service Invoice Detail
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
					<div class="accordion">
						<div class="accordion-item accor_item">
							<h2 class="accordion-header" id="heading">
 								<button class="accordion-button <%if(stateMapping.equals("") || (submission_chk && operation.equals("INSERT"))){%>collapsed<%}%> accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse" aria-expanded="false" aria-controls="collapse">
					    			<font>LTCORA Truck Loading&nbsp;<font color="blue">(<%=total_truck_invoice%>)</font></font>
					      		</button>
					    	</h2>
					    	<div id="collapse" class="accordion-collapse collapse <%if(!stateMapping.equals("") || (submission_chk && operation.equals("INSERT"))){%>show<%}%>" aria-labelledby="heading">
					      		<div class="accordion-body accor-body">
					        		<div class="row">
										<div class="table-responsive">
											<table class="table table-bordered table-hover" id="filterbysearch">
												<thead id="tbsearch0">
													<tr>
														<!-- <th>Un-check<br>Truck/s to Discount</th> -->
														<th rowspan="2">Sr#</th>
														<th rowspan="2">Truck#</th>
														<th rowspan="2">Gas Date</th>
														<!-- <th colspan="2">Arrival</th> -->
														<th colspan="2">Truck Loaded Qty</th>
													</tr>
													<tr>
														<!-- <th>Loading Start</th>
														<th>Loading End</th> -->
														<th>MMBTU</th>
														<th>MT</th>
													</tr>
												</thead>
												<tbody>
												<%if(VSALES_TRUCK_CD.size()>0){ %>
													<%for(int i=0; i<VSALES_TRUCK_CD.size(); i++){ %>
													<tr>
														<%-- <td align="center"
														<%if(VSALES_INV_LINKED.elementAt(i).equals("EL")){ %>title="Truck Selected in another Service Invoice!" style="background: #df9fbf;"<%}%>
														>
															<div align="center">
																<input type="checkbox" class="form-check-input" name="sel_checkbox" id="sel_checkbox<%=i%>" 
																onclick="enableDisableTruckInv(this,'<%=i%>');totalInvQty();" 
																<%
																if(stateMapping.equals("") && VSALES_INV_LINKED.elementAt(i).equals("Y") && operation.equals("MODIFY")){ %>checked<%}%>
																<%if(VSALES_INV_LINKED.elementAt(i).equals("EL")){ %>style="pointer-events:none"<%}%>
																>
																<input type="hidden" name="sel_checkbox_flag" id="sel_checkbox_flag_<%=i%>" value="">
																<input type="hidden" name="truck_trans_cd" id="truck_trans_cd_<%=i%>" value="<%=VSALES_TRUCK_TRANS_CD.elementAt(i)%>">
																<input type="hidden" name="truck_cd" id="truck_cd_<%=i%>" value="<%=VSALES_TRUCK_CD.elementAt(i)%>">
																<input type="hidden" name="sales_inv_qty" id="sales_inv_qty_<%=i %>" value="<%=VSALES_INV_QTY.elementAt(i)%>">
																<input type="hidden" name="sales_inv_linked" id="sales_inv_linked_<%=i %>" value="<%=VSALES_INV_LINKED.elementAt(i)%>">
															</div>
														</td> --%>
														<td align="center">
															<%=i+1%>.
															<input type="hidden" name="truck_trans_cd" id="truck_trans_cd_<%=i%>" value="<%=VSALES_TRUCK_TRANS_CD.elementAt(i)%>">
																<input type="hidden" name="truck_cd" id="truck_cd_<%=i%>" value="<%=VSALES_TRUCK_CD.elementAt(i)%>">
																<input type="hidden" name="truck_alloc_qty" id="truck_alloc_qty_<%=i %>" value="<%=VSALES_INV_QTY.elementAt(i)%>">
																<input type="hidden" name="truck_alloc_qty_mt" id="truck_alloc_qty_mt_<%=i %>" value="<%=VSALES_INV_QTY_MT.elementAt(i)%>">
																<input type="hidden" name="alloc_mapping" id="alloc_mapping_<%=i %>" value="<%=VSALES_MAPPING_ID.elementAt(i)%>">
														</td>
														<td align="center">
															<%=VSALES_TRUCK_REG_NO.elementAt(i)%>
														</td>
														<td align="center">
															<%=VGAS_DT.elementAt(i)%>
														</td>
														<%-- <td align="center"><%=VSELL_PERIOD_START_DT.elementAt(i)%></td>
														<td align="center"><%=VSELL_PERIOD_END_DT.elementAt(i) %></td> --%>
														<td align="center" align="right"><%=VSALES_INV_QTY.elementAt(i)%>
														<td align="center" align="right"><%=VSALES_INV_QTY_MT.elementAt(i)%>
														</td>
													</tr>
													<%} %>
													<tr>
														<td colspan="3" align="right"><b>Total:</b></td>
														<td id="disp_sales_inv_qty" align="center">
														</td>
														<td id="disp_sales_inv_qty_mt" align="center">
														</td>
														<input type="hidden" id="total_sales_inv_qty" name="total_sales_inv_qty" value="">
														<input type="hidden" id="total_sales_inv_qty_mt" name="total_sales_inv_qty_mt" value="">
													</tr>
												<%}else{ %>
													<tr>
														<td align="center" colspan="8"><%=utilmsg.infoMessage("<b>No Allocation done for Contract during Billing Period!</b>") %></td>
													</tr>
												<%} %>
												<input type="hidden" id="checkboxState" name="checkboxState">
												</tbody>
											</table>
										</div>
									</div>
									<%-- <div class="row m-b-5">
										<div align="center"><%=utilmsg.infoMessage("<b>Discounted Truck/s will not appear for service invoicing for this service contract!</b>") %></div>
				   					</div> --%>
					      		</div>
					    	</div>
					    </div>
					</div>
					<!-- <div class="col" align="right">
						<input type="button" class="btn btn-warning com-btn" value="Apply" onclick="proceedTruckInv();">
					</div> -->
				</div>
				<%if(!contract_type.equals("Q") && !contract_type.equals("O")) {%>
					<%if((buyerTurnoverFlag.equals("") || sellerTurnoverFlag.equals("")) || (buyerTurnoverFlag.equals("") && sellerTurnoverFlag.equals(""))) {%>
					<div class="card-body cdbody">
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<%String turnOverMsg="";
								if(buyerTurnoverFlag.equals("")){ 
									turnOverMsg="Buyer";
								} 
								if(sellerTurnoverFlag.equals("")){ 
									if(!turnOverMsg.equals(""))
									{
										turnOverMsg+="/Seller";
									}
									else
									{
										turnOverMsg+="Seller";
									}
								} %>
								<%=utilmsg.errorMessage("Please configure "+turnOverMsg+" Turnover entry for TCS/TDS calculation!")%>
							</div>
						</div>
					</div>
					<%} %>
				<%} %>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<h5>
								<span class="badge bg-info text-dark">TCS/TDS Applicable : <%=applicable_abbr%></span>
								<%if(!contract_type.equals("Q") && !contract_type.equals("O")) {%>
								&nbsp;&nbsp;<i class="fa fa-info-circle fa-lg" title="Click to View Invoice" style="color:#0000cc;" data-bs-toggle="modal" data-bs-target="#TcsInvDtl"></i>
								<%} %>
								<%if(applicable_abbr.equals("TDS")) {%>&nbsp;&nbsp;<span class="badge bg-info text-dark"><%=tds_amt%> INR</span><%} %>
							</h5>
							<div class="col-1">
							</div>
						</div>
					</div>
				</div>
				<%if(!price_cd.equals(invoice_raised_in)){ %>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label">
							<b>
								<span style="background:yellow;">Exchange Rate</span> 
								Consideration : <%if(exchng_rate_cd.equals("0")){%>
												Fixed
												<%}else{%>
													<%if(exchng_rate_cal.equals("A")){%>
													On Daily Basis of Billing Period
													<%} else {%>
													On Particular Day Base<%} %>
												<%} %>
							</b>
						</label>
					</div>
					<div class="row" <%if(exchng_rate_cd.equals("0")){%>style="display:none;"<%} %>>
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
								<%if(exchng_rate_cal.equals("A"))
								{%>
									<thead>
										<tr>
											<th>Date</th>
											<%for(int i=0;i<VEXCHNG_RATE_CD.size();i++){ %>
											<th>
												<input type="radio" name="sel_exchng_cd" <%if(VEXCHNG_RATE_CD.elementAt(i).equals(exchng_rate_cd)){%>checked<%} %> value="<%=VEXCHNG_RATE_CD.elementAt(i)%>" onclick="setExchngRateCd(this);">&nbsp;
												<span style="background:<%if(VEXCHNG_RATE_CD.elementAt(i).equals(exchng_rate_cd)){%>yellow<%}%>;"><%=VEXCHNG_RATE_NM.elementAt(i) %></span>
											</th>
											<%} %>
											<th>Price (USD)</th>
											<th>Daily Qty (MMBTU)</th>
											<th>Daily Gross (USD)</th>
											<th>Daily Gross (INR)</th>
										</tr>
									</thead>
									<tbody>
										<%int z=0, y=0,x=VEXCHNG_RATE_CD.size();
										for(int i=0; i<VALLOCATION_DT.size(); i++){ %>
										<tr>
											<td align="center">
												<div style="width:100px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="allocation_dt" id="allocation_dt<%=i%>" value="<%=VALLOCATION_DT.elementAt(i)%>" readOnly maxLength="10" 
							      						onblur="validateDate(this);" 
							      						onchange="validateDate(this);" autocomplete="off">
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
												</div>
											</td>
											<%y=0;											
											for(z=z; z<VEXCHNG_RATE_CAL_CD.size(); z++){ 
												y=y+1;
											%>
											<td align="center" style="background:<%=VEXCHNG_RATE_CAL_COLOR.elementAt(z)%>;">
											<%=VEXCHNG_RATE_CAL_VAL.elementAt(z) %>
											<%if(VEXCHNG_RATE_CAL_CD.elementAt(z).equals(exchng_rate_cd)){ %>
											<input type="hidden" class="form-control form-control-sm" name="dailyExchngRate" id="dailyExchngRate<%=i%>" value="<%=VEXCHNG_RATE_CAL_VAL.elementAt(z) %>">
											<%} %>
											</td>
											<%
												if(y==x)
												{
													z=z+1;
													break;
												}
											} %>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="salesPrice" id="salesPrice<%=i%>" value="<%=VPRICE.elementAt(i)%>" readOnly style="text-align:right">
													<input type="hidden" class="form-control form-control-sm" name="salesPriceCd" id="salesPriceCd<%=i%>" value="<%=price_cd%>" readOnly style="text-align:right">
												</div>
											</td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="allocation_qty" id="allocation_qty<%=i%>" value="<%=VALLOCATION_QTY.elementAt(i)%>" readOnly style="text-align:right">
												</div>
											</td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="amount_usd" id="amount_usd<%=i%>" value="<%=VAMOUNT_USD.elementAt(i)%>" readOnly style="text-align:right">
												</div>
											</td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="amount_inr" id="amount_inr<%=i%>" value="<%=VAMOUNT_INR.elementAt(i)%>" readOnly style="text-align:right">
												</div>
											</td>
										</tr>
										<%} %>
										
										<tr>
											<td align="right" colspan="<%=x+2%>"><b>Total :</b></td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="" value="<%=daily_tot_qty%>" readOnly style="text-align:right">
												</div>
											</td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="" value="<%=daily_tot_amt_usd%>" readOnly style="text-align:right">
												</div>
											</td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="" value="<%=daily_tot_amt_inr%>" readOnly style="text-align:right">
												</div>
											</td>
										</tr>
									</tbody>
								<%}
								else
								{%>
									<thead>
										<tr>
											<th>Exchange Rate Day</th>
											<th>Date</th>
											<%for(int i=0;i<VEXCHNG_RATE_CD.size();i++){ %>
											<th><span style="background:<%if(VEXCHNG_RATE_CD.elementAt(i).equals(exchng_rate_cd)){%>yellow<%}%>;"><%=VEXCHNG_RATE_NM.elementAt(i) %></font></th>
											<%} %>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><b>Previous Available Day</b></td>
											<td align="center"><b><%=last_avlb_exchng_dt%></b></td>
											<%for(int i=0;i<VP_EXCHNG_RATE_CD.size();i++){ %>
											<td align="center" style="background:<%=VP_BG_COLOR.elementAt(i)%>;">
												<%if(!VP_EXCHNG_RATE_VALUE.elementAt(i).toString().trim().equals("")) {%>
													<input type="radio" name="exchgRate" id='P-<%=VP_EXCHNG_RATE_CD.elementAt(i)%>' value="<%=VP_EXCHNG_RATE_VALUE.elementAt(i)%>" 
													<%-- onclick="setExchnageRate(this,'<%=last_avlb_exchng_dt%>','<%=VP_EXCHNG_RATE_CD.elementAt(i)%>')" HP20230929 DUE TO TDS NOT CALCULATED IF EXCHNG RATE NOT CONFIGURED --%>
													onclick="setExchngRateCd1(this);"
													<%if(VEXCHNG_RATE_CD.elementAt(i).equals(exchng_rate_cd) && exchang_rate_dt.equals(last_avlb_exchng_dt)){%>checked<%} %>
													>&nbsp;
												<%}%>
												<%=VP_EXCHNG_RATE_VALUE.elementAt(i)%>
											</td>
											<%} %>
										</tr>
										<%-- <tr>
											<td><b><%=lable_inv_criteria %></b></td>
											<td align="center"><b><%=lable_inv_date%></b></td>
											<%for(int i=0;i<VB_EXCHNG_RATE_CD.size();i++){ %>
											<td align="center" style="background:<%=VB_BG_COLOR.elementAt(i)%>;">
												<%if(!VB_EXCHNG_RATE_VALUE.elementAt(i).toString().trim().equals("")) {%>
													<input type="radio" name="exchgRate" id='B-<%=VB_EXCHNG_RATE_CD.elementAt(i)%>' value="<%=VB_EXCHNG_RATE_VALUE.elementAt(i)%>" 
													onclick="setExchnageRate(this,'<%=invoice_dt%>','<%=VB_EXCHNG_RATE_CD.elementAt(i)%>')" HP20230929 DUE TO TDS NOT CALCULATED IF EXCHNG RATE NOT CONFIGURED
													onclick="setExchngRateCd1(this);"
													<%if(VEXCHNG_RATE_CD.elementAt(i).equals(exchng_rate_cd) && exchang_rate_dt.equals(lable_inv_date)){%>checked<%} %>
													>&nbsp;
												<%}%>
												<%=VB_EXCHNG_RATE_VALUE.elementAt(i) %>
											</td>
											<%} %>
										</tr> --%>
										<tr style="pointer-events: none;">
											<!-- <td><b>User Defined</b></td> -->
											<td><b>Applicable Exchange Rate</b></td>
											<td align="center">
												<div style="width:100px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="user_defined_dt" value="<%=user_defined_dt%>" maxLength="10" 
							      						onblur="validateDate(this);" 
							      						onchange="validateDate(this);refresh();" autocomplete="off" >
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
					      						</div>
											</td>
											<%for(int i=0;i<VU_EXCHNG_RATE_CD.size();i++){ %>
											<td align="center" style="background:<%=VU_BG_COLOR.elementAt(i)%>;">
												<%-- <%if(!VU_EXCHNG_RATE_VALUE.elementAt(i).toString().trim().equals("")) {%>
													<input type="radio" name="exchgRate" id='U-<%=VU_EXCHNG_RATE_CD.elementAt(i)%>' value="<%=VU_EXCHNG_RATE_VALUE.elementAt(i)%>" 
													onclick="setExchnageRate(this,'<%=user_defined_dt%>','<%=VU_EXCHNG_RATE_CD.elementAt(i)%>')"  HP20230929 DUE TO TDS NOT CALCULATED IF EXCHNG RATE NOT CONFIGURED
													onclick="setExchngRateCd1(this);"
													<%if(VEXCHNG_RATE_CD.elementAt(i).equals(exchng_rate_cd) && exchang_rate_dt.equals(user_defined_dt)){%>checked<%} %>
													>&nbsp;
												<%}%> --%>
												<%=VU_EXCHNG_RATE_VALUE.elementAt(i) %>
											</td>
											<%} %>
											<%if(!exchng_rate_mapp.equals("")){ %>
											<script>
											if(document.getElementById('<%=exchng_rate_mapp%>') != null)
											{
												document.getElementById('<%=exchng_rate_mapp%>').checked=true;
											}
											</script>
											<%} %>
										</tr>
									</tbody>
								<%}%>
								</table>
								<%if(correction_needed){ %>
								<label class="form-label" style="color:red;"><%=utilmsg.errorMessage(correction_msg)%></label>
								<%} %>
								<label class="form-label">Effective Exchange Rate = Gross Amount (INR) / Gross Amount (USD) = <%=exchang_rate%></label>
							</div>
						</div>
					</div>
				</div>
				<%} %>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
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
											<td><b>Invoice Date<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="invoice_dt" value="<%=invoice_dt%>" maxLength="10" 
							      						onblur="validateDate(this);" 
							      						onchange="validateDate(this);refresh();" autocomplete="off">
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
						      						<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
						      						<input type="hidden" name="exist_invoice_seq" value="<%=invoice_seq%>">
					      						</div>
											</td>
										</tr>
										<tr>
											<td><b>Invoice Due Date<span class="s-red">*</span></b></td>
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
											<td><b>Allocated Quantity</b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="alloc_qty" value="<%=qty_mmbtu%>" readOnly>
														<span class="input-group-text">MMBTU</span>
														<input type="hidden" name="qty_unit" value="<%=qty_unit%>">
														<input type="hidden" name="dist_unit" value="<%=dist_unit%>">
													</div>
												</div>
											</td>
										</tr>
										<tr>
											<td><b>Contract Price<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="price" value="<%=price%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%></span>
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
														<input type="text" class="form-control form-control-sm" name="gross_amt" value="<%=gross_amt%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%></span>
													</div>
												</div>
											</td>
										</tr>
										<tr <%
										if(!price_cd.equals(invoice_raised_in)){ %><%}else{ %>style="display:none;"<%} %>>
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
										</tr>
										<%-- <tr <%if(!agmt_base.equals("D")){ %>style="display:none;"<%} %>> --%>
										<tr <%if(transportation_charges.equals("")){ %>style="display:none;"<%} %>>
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
										</tr>
										<tr <%if(marketing_margin.equals("")){ %>style="display:none;"<%} %>>
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
										</tr>
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
										</tr>
										<%if(applicable_abbr.equals("TCS")){ %>
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
										</tr>
										<%} %>
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
										</tr>
										<tr>
											<td><b>Remark-1</b></td>
											<td align="center">
												<!-- <div style="width:400px;"> -->
													<textarea class="form-control form-control-sm" rows="3" cols="75" name="remark1"><%=remark_1%></textarea>
												<!-- </div> -->
											</td>
										</tr>
										<tr>
											<td><b>Remark-2</b></td>
											<td align="center">
												<!-- <div style="width:400px;"> -->
													<textarea class="form-control form-control-sm" rows="3" cols="75" name="remark2"><%=remark_2%></textarea>
												<!-- </div> -->
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<%if(operation.equals("INSERT") && !inv_info_msg.equals("")){ %>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div align="center"><%=utilmsg.infoMessage(inv_info_msg) %></div>
						</div>
					</div>
				</div>
				<%} %>
				<%if((submission_chk==false && operation.equals("INSERT")) || operation.equals("MODIFY")){ %>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<div class="row justify-content-start">
							<div class="col-auto">
								<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
							</div>
						</div>
						<%if(write_access.equals("Y")){ %>
						<div class="row justify-content-end">
							<div class="col-auto">
								<i class="fa fa-eye fa-2x" onclick="viewBeforeSubmit();"></i>
							</div>
							<!-- <div class="col-auto">
								<div class="icon-wrapper">
								  <i class="fa fa-file fa-2x" onclick="viewBeforeForm402();"></i>
								  <span class="icon-text">402</span>
								</div>
								<input type="button" class="btn btn-warning com-btn" value="View Form 402" onclick="viewBeforeForm402();">
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

<input type="hidden" name="option" value="DLNG_LTCORA_INVOICE">
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
<%-- <input type="hidden" name="truck_cd" value="<%=truck_cd%>">
<input type="hidden" name="truck_trans_cd" value="<%=truck_trans_cd%>"> --%>
<input type="hidden" name="mapping_id" value="<%=mapping_id%>">
<input type="hidden" name="qtyMmbtu" value="<%=qtyMmbtu%>">

<input type="hidden" name="transport_mgmt_unit" value="<%=transport_mgmt_unit%>">

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

<input type="hidden" name="exchng_rate_mapp" value="<%=exchng_rate_mapp%>">

<input type="hidden" name="accroid" value="<%=accroid%>">

<input type="hidden" name="plant_gstin_no" value="<%=plant_gstin_no%>">
<input type="hidden" name="bu_gstin_no" value="<%=bu_gstin_no%>">
<input type="hidden" name="sys_dt" value="<%=sysdt%>">

<input type="hidden" name="stateMapping" value="<%=stateMapping%>">
<input type="hidden" name="submission_chk" value="<%=submission_chk%>">

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