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
	
	var url = "frm_generate_ltcora_storage_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
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
	
	var advance_adj_flag = document.forms[0].advance_adj_flag.value;
	
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
	
	if(price_cd=="2")
	{
		if(exchng_rate == "0.0000" || trim(exchng_rate) == "")
		{
			msg+="Please Select The Exchange Rate Which Is Available !!!\nOR\nEnter The Exchange Rate From Interest & Exchange Rate Entry Form For The Selected Date!\n";
			flag=false;
		}
	}
	
	/*if(trim(alloc_qty)!="")
	{
		if(parseFloat(alloc_qty) <= 0)
		{
			msg+="Allocated Qty should be > ZERO(0)!\n";
			flag=false;	
		}
		else
		{*/
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
	/*	}
	}
	*/
	
	/*if(trim(price)=="")
	{
		msg+="Confirmed Price missing!\n";
		flag=false;
	}*/
	
	if(trim(gross_amt1)=="")
	{
		msg+="Gross Amount missing!\n";
		flag=false;
	}
	if(agmt_base=="D")
	{
		if(trim(transportation_amount)=="")
		{
			msg+="Transporatation Tariff missing!\n";
			flag=false;
		}
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
		
		if(advance_adj_flag=="Y")
		{
			var receipt_voucher = document.getElementsByName("receipt_voucher")
			var gross_adjust = document.getElementsByName("gross_adjust")
			
			if(receipt_voucher!=null && receipt_voucher!=undefined)
			{
				if(receipt_voucher.length!=undefined)
				{
					for(var i=0;i<receipt_voucher.length;i++)
					{
						if(receipt_voucher[i].value == "")
						{
							msg+="Select Receipt Voucher for ROW - "+(i+1)+"!\n";
							flag=false;
						}
						
						if(trim(gross_adjust[i].value) == "")
						{
							msg+="Enter Gross Adjust for ROW - "+(i+1)+"!\n";
							flag=false;
						}
					}
				}
			}
			
			var sub_tax_amt = document.forms[0].sub_tax_amt;
			var sub_tax_struct = document.forms[0].sub_tax_struct;
			
			if(sub_tax_amt!=null && sub_tax_amt!=undefined)
			{
				if(sub_tax_amt.length!=undefined)
				{
					for(var i=0;i<sub_tax_amt.length;i++)
					{
						var tax_struct = sub_tax_struct[i].value;
						var tax_amt=sub_tax_amt[i].value;
						
						let nameParts = tax_struct.split(' ');
			    		taxAbbr= nameParts[0];
			    		
			    		var tax_balance = document.getElementsByName(taxAbbr+"_balance");
			    		var tax_adjust = document.getElementsByName(taxAbbr+"_adjust");
			    		
			    		for(var j=0;j<tax_balance.length;j++)
						{
							if(tax_balance[j].value != "")
							{
								if(trim(tax_adjust[j].value) == "")
								{
									msg+="Enter "+taxAbbr+" Adjust for ROW - "+(j+1)+"!\n";
									flag=false;
								}
							}
						}
			    		
			    		var adjusted_tax = document.getElementsByName("adjusted_"+taxAbbr+"_amt")[0].value;
			    		if(adjusted_tax == "")
						{
							msg+=taxAbbr+" Amount after Adjustment shouldn't be blank!\n";
							flag=false;
						}
						else
						{
							if(parseFloat(adjusted_tax) < 0)
							{
								msg+=taxAbbr+" Amount after Adjustment should not be < 0!\n";
								flag=false;
							}
						}
					}
				}
				else
				{
					var tax_struct = sub_tax_struct.value;
					var tax_amt=sub_tax_amt.value;
					
					let nameParts = tax_struct.split(' ');
		    		taxAbbr= nameParts[0];
		    		
		    		var tax_balance = document.getElementsByName(taxAbbr+"_balance");
		    		var tax_adjust = document.getElementsByName(taxAbbr+"_adjust");
		    		
		    		for(var j=0;j<tax_balance.length;j++)
					{
						if(tax_balance[j].value != "")
						{
							if(trim(tax_adjust[j].value) == "")
							{
								msg+="Enter "+taxAbbr+" Adjust for ROW - "+(j+1)+"!\n";
								flag=false;
							}
						}
					}
		    		
		    		var adjusted_tax = document.getElementsByName("adjusted_"+taxAbbr+"_amt")[0].value;
		    		if(adjusted_tax == "")
					{
						msg+=taxAbbr+" Amount after Adjustment shouldn't be blank!\n";
						flag=false;
					}
					else
					{
						if(parseFloat(adjusted_tax) < 0)
						{
							msg+=taxAbbr+" Amount after Adjustment should not be < 0!\n";
							flag=false;
						}
					}
				}
			}
			
			var adjusted_gross = document.forms[0].adjusted_gross_amt.value;
			var adjusted_tax = document.forms[0].adjusted_tax_amt.value;
			var adjusted_net = document.forms[0].adjusted_net_payable.value;
			
			if(adjusted_gross == "")
			{
				msg+="Gross Amount after Adjustment shouldn't be blank!\n";
				flag=false;
			}
			else
			{
				if(parseFloat(adjusted_gross) < 0)
				{
					msg+="Gross Amount after Adjustment should not be < 0!\n";
					flag=false;
				}
			}
			
			if(adjusted_tax == "")
			{
				msg+="Tax Amount after Adjustment shouldn't be blank!\n";
				flag=false;
			}
			else
			{
				if(parseFloat(adjusted_tax) < 0)
				{
					msg+="Tax Amount after Adjustment should not be < 0!\n";
					flag=false;
				}
			}
			
			if(adjusted_net == "")
			{
				msg+="Net Amount after Adjustment shouldn't be blank!\n";
				flag=false;
			}
			else
			{
				if(parseFloat(adjusted_net) < 0)
				{
					msg+="Net Amount after Adjustment should not be < 0!\n";
					flag=false;
				}
			}
		}
	}
	
	if(flag)
	{
		var a=confirm("Do you want to Submit Sales Invoice?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].period_end_dt.value=document.forms[0].skip_period_end_dt.value;
			document.forms[0].temp_period_end_dt.value=document.forms[0].skip_period_end_dt.value;
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
	var isGrossIncTriff = document.forms[0].isGrossIncTriff.value;
	
	var tcs_tds = document.forms[0].tcs_tds.value;
	
	var adv_adj_flg="";
	var advance_adj_flag = document.forms[0].advance_adj_flag.value;
	if(advance_adj_flag=="Y")
	{
		if(document.forms[0].adj_adv_flag.checked)
		{
			adv_adj_flg="Y";
		}
	}
	
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
			"&bu_state_tin="+bu_state_tin+"&invoice_dt="+invoice_dt+"&agmt_base="+agmt_base+"&tcs_tds="+tcs_tds+"&cargo_no="+cargo_no+"&inv_flag="+inv_flag+"&adv_adj_flg="+adv_adj_flg;
	
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

function enableUserDefine(obj)
{
	var user_define=document.forms[0].user_define;
	
	if(user_define!=null && user_define!=undefined)
	{
		if(user_define.length!=undefined)
		{
			for(var i=0;i<user_define.length;i++)
			{
				var skip_chk = document.getElementById("skip_chk_"+i);
				if(obj.value=="U" && !skip_chk.checked)
				{
					user_define[i].disabled=false;
					calculateStorageAmt(i)
				}
				else
				{
					user_define[i].disabled=true;
					calculateStorageAmt(i)
				}
			}
		}
		else
		{
			var skip_chk = document.getElementById("skip_chk_0");
			if(obj.value=="U" && !skip_chk.checked)
			{
				user_define.disabled=false;
				calculateStorageAmt("0")
			}
			else
			{
				user_define.disabled=true;
				calculateStorageAmt("0")
			}
		}
	}
}

function enableSkipRow(obj,index,total_size)
{
	var chk = document.getElementById("chk_"+index);
	
	var disc_day_flag = document.getElementById("disc_day_flag_"+index);
	
	var disc_day_flag = document.getElementById("disc_day_flag_"+index);
	var storage_dt = document.getElementById("storage_dt_"+index);
	var storage_inventory = document.getElementById("storage_inventory_"+index);
	var storage_charge = document.getElementById("storage_charge_"+index);
	var storage_amt = document.getElementById("storage_amt_"+index);
	var user_define = document.getElementById("user_define_"+index);
	var offtake_qty = document.getElementById("offtake_qty_"+index);
	
	var rate_type = document.getElementById("rate_type_u");
	
	var discount_days = document.forms[0].discount_days.value;
	
	if(obj.checked)
	{
		chk.checked=false;
		chk.disabled=true;
		
		if(rate_type.checked)
		{
			user_define.disabled=true;
		}
		
		if((parseInt(index)-1) >= 0)
		{
			document.forms[0].skip_period_end_dt.value=document.getElementById("storage_dt_"+(parseInt(index)-1)).value;
		}
		
		if((parseInt(index)-1) > 0)
		{
			document.getElementById("skip_chk_"+(parseInt(index)-1)).disabled=false;
		}
		
		if((parseInt(index)+1) <= parseInt(total_size))
		{
			document.getElementById("skip_chk_"+(parseInt(index)+1)).disabled=true;
		}
		
		disc_day_flag.disabled=true;
		storage_dt.disabled=true;
		storage_inventory.disabled=true;
		storage_charge.disabled=true;
		storage_amt.disabled=true;
		user_define.disabled=true;
		offtake_qty.disabled=true;
	}
	else
	{
		if(discount_days=="Y")
		{
			chk.disabled=false;
		}
		
		if(rate_type.checked)
		{
			user_define.disabled=false;
		}
		
		if((parseInt(index)-1) >= 0)
		{
			document.getElementById("skip_chk_"+(parseInt(index)-1)).disabled=true;
		}
		
		if((parseInt(index)+1) <= parseInt(total_size))
		{
			document.getElementById("skip_chk_"+(parseInt(index)+1)).disabled=false;
		}
		
		document.forms[0].skip_period_end_dt.value=document.getElementById("storage_dt_"+index).value;
		
		disc_day_flag.disabled=false;
		storage_dt.disabled=false;
		storage_inventory.disabled=false;
		storage_charge.disabled=false;
		storage_amt.disabled=false;
		user_define.disabled=false;
		offtake_qty.disabled=false;
	}
}

function calculateStorageAmt(index)
{
	var skip_chk = document.getElementById("skip_chk_"+index);
	
	var chk = document.getElementById("chk_"+index);
	
	var rate_type = document.forms[0].rate_type;
	
	var temp_chk = document.forms[0].chk;
	
	var disc_day_flag = document.getElementById("disc_day_flag_"+index);
	
	var storage_inventory = document.getElementById("storage_inventory_"+index);
	var storage_charge = document.getElementById("storage_charge_"+index);
	var storage_amt = document.getElementById("storage_amt_"+index);
	var user_define = document.getElementById("user_define_"+index);
	
	if(chk.checked)
	{
		storage_amt.value="";
		user_define.value="";
		//storage_charge.value="";
		
		disc_day_flag.value="Y";
	}
	else
	{
		disc_day_flag.value="N";
		
		if(skip_chk.checked)
		{
			storage_amt.value="";
			user_define.value="";
		}
		else if(rate_type.value=="U")
		{
			if(storage_inventory.value!="" && user_define.value!="")
			{
				var amt = round(parseFloat(storage_inventory.value) * parseFloat(user_define.value),2);
				storage_amt.value=amt;
			}
			else
			{
				storage_amt.value="";
			}
		}
		else
		{
			if(storage_inventory.value!="" && storage_charge.value!="")
			{
				var amt = round(parseFloat(storage_inventory.value) * parseFloat(storage_charge.value),2);
				storage_amt.value=amt;
			}
			else
			{
				storage_amt.value="";
			}
		}
	}
	
	var total=parseFloat("0");
	
	if(temp_chk!=null && temp_chk!=undefined)
	{
		if(temp_chk.length!=undefined)
		{
			for(var i=0;i<temp_chk.length;i++)
			{
				var temp_storage_amt = document.forms[0].storage_amt;
				if(temp_storage_amt[i].value!="")
				{
					total=total+parseFloat(temp_storage_amt[i].value);
				}
			}
		}
		else
		{
			var temp_storage_amt = document.forms[0].storage_amt;
			if(temp_storage_amt.value!="")
			{
				total=total+parseFloat(temp_storage_amt.value);
			}
		}
	}
	
	document.forms[0].total_storage_amt.value=round(parseFloat(total),2);
	
	//INVOICE PARAMETER CALCULATION
	var price_cd = document.forms[0].price_cd.value;
	var exchng_rate = document.forms[0].exchng_rate.value;
	var gross_amt = document.forms[0].gross_amt;
	var gross_amt1 = document.forms[0].gross_amt1;
	var tax_amt = document.forms[0].tax_amt;
	var invoice_amt = document.forms[0].invoice_amt;
	var net_payable = document.forms[0].net_payable;
	
	if(price_cd=="2")
	{
		gross_amt.value=round(parseFloat(total),2);
		
		if(exchng_rate!="")
		{
			gross_amt1.value=round((parseFloat(total) * parseFloat(exchng_rate)),2);
		}
		else
		{
			gross_amt1.value="";
		}
	}
	else
	{
		gross_amt.value=round(parseFloat(total),2);
		gross_amt1.value=round(parseFloat(total),2);
	}
	
	//TAX CALCULATION
	var sub_tax_amt = document.forms[0].sub_tax_amt;
	var sub_tax_factor = document.forms[0].sub_tax_factor;
	var sub_tax_base_amt = document.forms[0].sub_tax_base_amt;
	var total_tax=parseFloat(0);
	
	if(sub_tax_amt!=null && sub_tax_amt!=undefined)
	{
		if(sub_tax_amt.length!=undefined)
		{
			for(var i=0;i<sub_tax_amt.length;i++)
			{
				if(trim(sub_tax_factor[i].value)!="" && trim(gross_amt1.value)!="")
				{
					sub_tax_amt[i].value=round((parseFloat(gross_amt1.value)*parseFloat(sub_tax_factor[i].value))/100,2)
					sub_tax_base_amt[i].value=gross_amt1.value;
					
					total_tax=total_tax+parseFloat(sub_tax_amt[i].value)
				}
				else
				{
					sub_tax_amt[i].value="";
					sub_tax_base_amt[i].value="";
				}
			}
		}
		else
		{

			if(trim(sub_tax_factor.value)!="" && trim(gross_amt1.value)!="")
			{
				sub_tax_amt.value=round((parseFloat(gross_amt1.value)*parseFloat(sub_tax_factor.value))/100,2)	
				sub_tax_base_amt.value=gross_amt1.value;
				total_tax=total_tax+parseFloat(sub_tax_amt.value)
			}
			else
			{
				sub_tax_amt.value="";
				sub_tax_base_amt.value="";
			}
		}
	}
	
	tax_amt.value=round(parseFloat(total_tax),2)
	
	var inv_amt=parseFloat("0");
	if(trim(gross_amt1.value)!="" && trim(gross_amt1.value)!="")
	{
		inv_amt=round(parseFloat(gross_amt1.value)+parseFloat(tax_amt.value),2)
	}
	
	invoice_amt.value=round(parseFloat(inv_amt),2);
	net_payable.value=invoice_amt.value;
	
	//TDS CALCULATION
	var tds_amt = document.forms[0].tds_amt;
	var tds_factor = document.forms[0].tds_factor;
	var display_tds_amt = document.getElementById("display_tds_amt");
	
	if(trim(gross_amt1.value)!="" && trim(tds_factor.value)!="")
	{
		tds_amt.value=round((parseFloat(gross_amt1.value)*parseFloat(tds_factor.value))/100,2)
		display_tds_amt.innerHTML=tds_amt.value;
	}
	else
	{
		tds_amt.value="";
		display_tds_amt.innerHTML="";
	}
}

function showAdjTbl(obj)
{
	var tax_amt = document.forms[0].tax_amt.value
	
	var sub_tax_amt = document.forms[0].sub_tax_amt;
	var sub_tax_struct = document.forms[0].sub_tax_struct;
	
	var price_cd = document.forms[0].price_cd.value;
	
	if((document.forms[0].exchng_rate.value == "0.0000" || trim(document.forms[0].exchng_rate.value) == "") && price_cd=="2")
	{
		obj.checked=false
		alert("Please Select The Exchange Rate Which Is Available !!!\nOR\nEnter The Exchange Rate From Interest & Exchange Rate Entry Form For The Selected Date !!!")
	}
	else if(trim(tax_amt)=="")
	{
		obj.checked=false
		alert("Tax Amount missing!");
	}
	else if(obj.checked)
	{
		document.getElementById("advAdjTbl").style.display="";
		document.getElementById("dis_adjted_gross_amt").style.display="";
		document.getElementById("dis_adjted_tax_amt").style.display="";
		document.getElementById("dis_adjted_net_amt").style.display="";
		
		if(sub_tax_amt!=null && sub_tax_amt!=undefined)
		{
			if(sub_tax_amt.length!=undefined)
			{
				for(var i=0;i<sub_tax_amt.length;i++)
				{
					var tax_struct = sub_tax_struct[i].value;
					var tax_amt=sub_tax_amt[i].value;
					
					let nameParts = tax_struct.split(' ');
		    		taxAbbr= nameParts[0];
		    		
		    		document.getElementById("dis_adjted_"+taxAbbr+"_amt").style.display="";
				}
			}
			else
			{
				var tax_struct = sub_tax_struct.value;
				var tax_amt=sub_tax_amt.value;
				
				let nameParts = tax_struct.split(' ');
	    		taxAbbr= nameParts[0];
	    		
	    		document.getElementById("dis_adjted_"+taxAbbr+"_amt").style.display="";
			}
		}
	}
	else
	{
		document.getElementById("advAdjTbl").style.display="none";
		document.getElementById("dis_adjted_gross_amt").style.display="none";
		document.getElementById("dis_adjted_tax_amt").style.display="none";
		document.getElementById("dis_adjted_net_amt").style.display="none";
		
		if(sub_tax_amt!=null && sub_tax_amt!=undefined)
		{
			if(sub_tax_amt.length!=undefined)
			{
				for(var i=0;i<sub_tax_amt.length;i++)
				{
					var tax_struct = sub_tax_struct[i].value;
					var tax_amt=sub_tax_amt[i].value;
					
					let nameParts = tax_struct.split(' ');
		    		taxAbbr= nameParts[0];
		    		
		    		document.getElementById("dis_adjted_"+taxAbbr+"_amt").style.display="none";
				}
			}
			else
			{
				var tax_struct = sub_tax_struct.value;
				var tax_amt=sub_tax_amt.value;
				
				let nameParts = tax_struct.split(' ');
	    		taxAbbr= nameParts[0];
	    		
	    		document.getElementById("dis_adjted_"+taxAbbr+"_amt").style.display="none";
			}
		}
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.sales_invoice.DataBean_Sales_Invoice" id="sales_inv" scope="request"></jsp:useBean>
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

String inv_dt=request.getParameter("inv_dt")==null?"":request.getParameter("inv_dt");
String user_defined_dt=request.getParameter("user_defined_dt")==null?"":request.getParameter("user_defined_dt");
String sel_exchng_cd=request.getParameter("sel_exchng_cd")==null?"":request.getParameter("sel_exchng_cd");

String exchng_rate_mapp=request.getParameter("exchng_rate_mapp")==null?"":request.getParameter("exchng_rate_mapp");


sales_inv.setCallFlag("SALES_INVOICE_GENERATION");
sales_inv.setComp_cd(owner_cd);
sales_inv.setCounterparty_cd(counterparty_cd);
sales_inv.setAgmt_no(agmt_no);
sales_inv.setAgmt_rev_no(agmt_rev);
sales_inv.setCont_no(cont_no);
sales_inv.setCont_rev_no(cont_rev);
sales_inv.setContract_type(contract_type);
sales_inv.setCargo_no(cargo_no);
sales_inv.setPlant_seq(plant_seq);
sales_inv.setBilling_cycle(billing_cycle);
sales_inv.setPeriod_start_dt(period_start_dt);
sales_inv.setPeriod_end_dt(period_end_dt);
sales_inv.setTemp_period_start_dt(temp_period_start_dt);
sales_inv.setTemp_period_end_dt(temp_period_end_dt);
sales_inv.setBu_unit(bu_unit);
//sales_inv.setFinancial_year(financial_year);
sales_inv.setExist_financial_year(exist_financial_year);
sales_inv.setBu_state_tin(bu_state_tin);
sales_inv.setOperation(operation);
sales_inv.setInv_dt(inv_dt);
sales_inv.setUser_defined_dt(user_defined_dt);
sales_inv.setSel_exchng_cd(sel_exchng_cd);
sales_inv.setExchange_rate_mapping(exchng_rate_mapp);
sales_inv.setInv_flag(inv_flag);
sales_inv.init();

if(user_defined_dt.equals(""))
{
	user_defined_dt=sales_inv.getUser_defined_dt();
}

//String temp_financial_year=financial_year;
//if(financial_year.equals(""))
//{
String financial_year=sales_inv.getFinancial_year();

//}

String couterpty_abbr=sales_inv.getCouterpty_abbr();
String couterpty_nm=sales_inv.getCouterpty_nm();
String deal_no=sales_inv.getDeal_no();
String contract_ref=sales_inv.getContract_ref();
String plant_abbr=sales_inv.getPlant_abbr();
String bu_plant_abbr=sales_inv.getBu_plant_abbr();
String qty_mmbtu=sales_inv.getQty_mmbtu();
String price=sales_inv.getPrice();
String price_cd=sales_inv.getPrice_cd();
String price_cd_nm=sales_inv.getPrice_cd_nm();
String bu_contact_person_cd = sales_inv.getBu_contact_person_cd();
String contact_person_cd = sales_inv.getContact_person_cd();
String invoice_raised_in = sales_inv.getInvoice_raised_in();
String invoice_raised_in_nm = sales_inv.getInvoice_raised_in_nm();
String invoice_seq=sales_inv.getInvoice_seq();
String invoice_no=sales_inv.getInvoice_no();
String invoice_dt = sales_inv.getInvoice_dt();
String invoice_due_dt = sales_inv.getInvoice_due_dt();
String gross_amt=sales_inv.getGross_amt();
String gross_amt1=sales_inv.getGross_amt1();
String exchng_rate_cd=sales_inv.getExchng_rate_cd();
String exchng_rate_cal=sales_inv.getExchng_rate_cal();
String exchang_rate=sales_inv.getExchang_rate();
String exchang_rate_dt=sales_inv.getExchang_rate_dt();
String exchang_criteria=sales_inv.getExchang_criteria();
String last_avlb_exchng_dt = sales_inv.getLast_avlb_exchng_dt();
String lable_inv_criteria = sales_inv.getLable_inv_criteria();
String lable_inv_date = sales_inv.getLable_inv_date();
String invoice_id_seq=sales_inv.getInvoice_id_seq();

String correction_msg = sales_inv.getCorrection_msg();
String daily_tot_amt_inr=sales_inv.getDaily_tot_amt_inr();
String daily_tot_amt_usd=sales_inv.getDaily_tot_amt_usd();
String daily_tot_qty=sales_inv.getDaily_tot_qty();
String tax_amt = sales_inv.getTax_amt();
String tax_struct_cd=sales_inv.getTax_struct_cd();
String tax_struct_dt=sales_inv.getTax_struct_dt();
String tax_struct_dtl=sales_inv.getTax_struct_dtl();
String tax_info = sales_inv.getTax_info();
String tax_factor = sales_inv.getTax_factor();
String invoice_amt = sales_inv.getInvoice_amt();
String net_payable = sales_inv.getNet_payable();
String agmt_base = sales_inv.getAgmt_base();
String transportation_charges = sales_inv.getTransportation_charges();
String transportation_amount = sales_inv.getTransportation_amount();
String gross_include_transport_tariff = sales_inv.getGross_include_transport_tariff();
String marketing_margin = sales_inv.getMarketing_margin();
String marketing_margin_amount = sales_inv.getMarketing_margin_amount();
String other_charges = sales_inv.getOther_charges();
String other_charges_amount = sales_inv.getOther_charges_amount();

String plant_gstin_no = sales_inv.getPlant_gstin_no();
String bu_gstin_no = sales_inv.getBu_gstin_no();

String remark_1 =sales_inv.getRemark_1();
String remark_2 =sales_inv.getRemark_2();

String discount_days = sales_inv.getDiscount_days();

boolean submission_chk = sales_inv.getSubmission_chk();
boolean correction_needed = sales_inv.getCorrection_needed();

String previousFinancialYear = sales_inv.getPreviousFinancialYear();

String sellerTurnoverFlag=sales_inv.getSellerTurnoverFlag();
String buyerTurnoverFlag=sales_inv.getBuyerTurnoverFlag();

String applicable_flag = sales_inv.getApplicable_flag();
String applicable_abbr = sales_inv.getApplicable_abbr();
String applicable_amt = sales_inv.getApplicable_amt();

String TCS_factor = sales_inv.getTCS_factor();
String tcs_struct_cd = sales_inv.getTcs_struct_cd();
String tcs_struct_dt = sales_inv.getTcs_struct_dt();

String tds_amt = sales_inv.getTds_amt();
String tds_factor = sales_inv.getTds_factor();
String tds_struct_cd = sales_inv.getTds_struct_cd();
String tds_struct_dt = sales_inv.getTds_struct_dt();

String inv_entered_on = sales_inv.getInv_entered_on();
String inv_approved_on = sales_inv.getInv_approved_on();
String advance_adj_flag = sales_inv.getAdvance_adj_flag();

Vector VCONTACT_PERSON = sales_inv.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = sales_inv.getVCONTACT_PERSON_CD();
Vector VBU_CONTACT_PERSON = sales_inv.getVBU_CONTACT_PERSON();
Vector VBU_CONTACT_PERSON_CD = sales_inv.getVBU_CONTACT_PERSON_CD();

Vector VP_EXCHNG_RATE_CD = sales_inv.getVP_EXCHNG_RATE_CD();
Vector VP_EXCHNG_RATE_VALUE = sales_inv.getVP_EXCHNG_RATE_VALUE();
Vector VP_BG_COLOR = sales_inv.getVP_BG_COLOR();
Vector VB_EXCHNG_RATE_CD = sales_inv.getVB_EXCHNG_RATE_CD();
Vector VB_EXCHNG_RATE_VALUE = sales_inv.getVB_EXCHNG_RATE_VALUE();
Vector VB_BG_COLOR = sales_inv.getVB_BG_COLOR();
Vector VU_EXCHNG_RATE_CD = sales_inv.getVU_EXCHNG_RATE_CD();
Vector VU_EXCHNG_RATE_VALUE = sales_inv.getVU_EXCHNG_RATE_VALUE();
Vector VU_BG_COLOR = sales_inv.getVU_BG_COLOR();
Vector VEXCHNG_RATE_CD = sales_inv.getVEXCHNG_RATE_CD();
Vector VEXCHNG_RATE_NM = sales_inv.getVEXCHNG_RATE_NM();
Vector VEXCHNG_RATE_FLAG = sales_inv.getVEXCHNG_RATE_FLAG();

Vector VEXCHNG_RATE_CAL_CD = sales_inv.getVEXCHNG_RATE_CAL_CD();
Vector VEXCHNG_RATE_CAL_VAL = sales_inv.getVEXCHNG_RATE_CAL_VAL();
Vector VEXCHNG_RATE_CAL_COLOR = sales_inv.getVEXCHNG_RATE_CAL_COLOR();

Vector VALLOCATION_DT = sales_inv.getVALLOCATION_DT();
Vector VPRICE = sales_inv.getVPRICE();
Vector VALLOCATION_QTY = sales_inv.getVALLOCATION_QTY();
Vector VAMOUNT_USD = sales_inv.getVAMOUNT_USD();
Vector VAMOUNT_INR = sales_inv.getVAMOUNT_INR();

Vector VDEAL_NO = sales_inv.getVDEAL_NO();
Vector VCONT_REF_NO = sales_inv.getVCONT_REF_NO();
Vector VINVOICE_AMT = sales_inv.getVINVOICE_AMT();
Vector VINVOICE_DT = sales_inv.getVINVOICE_DT();

Vector VINVOICE_NO = sales_inv.getVINVOICE_NO();
Vector VMULTI_TAX_STRUCT = sales_inv.getVMULTI_TAX_STRUCT();

Vector VSTORAGE_DATE = sales_inv.getVSTORAGE_DATE();
Vector VSTORAGE_INVENTORY = sales_inv.getVSTORAGE_INVENTORY();
Vector VOFFTAKE_QTY = sales_inv.getVOFFTAKE_QTY();
Vector VSTORAGE_CHARGE = sales_inv.getVSTORAGE_CHARGE();
Vector VSTORAGE_AMT = sales_inv.getVSTORAGE_AMT();
Vector VUSER_DEFINE = sales_inv.getVUSER_DEFINE();
Vector VRATE_TYPE = sales_inv.getVRATE_TYPE();
Vector VDISCOUNT_FLAG = sales_inv.getVDISCOUNT_FLAG();

Vector VRECEIPT_VOUCHER_MST = sales_inv.getVRECEIPT_VOUCHER_MST();
Vector VEXISTING_RECEIPT_VOUCHER = sales_inv.getVEXISTING_RECEIPT_VOUCHER();


String storage_start_dt = sales_inv.getStorage_start_dt();
String storage_end_dt = sales_inv.getStorage_end_dt();

String total_InvoiceAmt = sales_inv.getTotal_InvoiceAmt();

boolean isGrossIncTriff = sales_inv.getIsGrossIncTriff();

String exchange_rate_mapping=sales_inv.getExchange_rate_mapping();
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
<body onload="<%if(!msg.equals("")){ %>Do_close('<%=msg%>','<%=msg_type%>','<%=accroid%>');<%} %>"
<%if(submission_chk && operation.equals("INSERT")) {%>style="pointer-events: none;"<%} %>>

<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_Sales_Invoice">
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
					    	LTCORA(Sell) Storage Invoice Detail
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
											<th rowspan="2">Extended Storage Period</th>
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
											<td align="center"><%=storage_start_dt%> - <%=storage_end_dt%></td>
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
								<!-- &nbsp;&nbsp;<i class="fa fa-info-circle fa-lg" title="Click to View Invoice" style="color:#0000cc;" data-bs-toggle="modal" data-bs-target="#TcsInvDtl"></i> -->
								<%if(applicable_abbr.equals("TDS")) {%>&nbsp;&nbsp;<span class="badge bg-info text-dark"><font id="display_tds_amt"><%=tds_amt%></font> INR</span><%} %>
							</h5>
							<div class="col-1">
							</div>
						</div>
					</div>
				</div>
				<%if(price_cd.equals("2")){ %>
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
											<td align="center" style="background:<%=VP_BG_COLOR.elementAt(i)%>">
												<%if(!VP_EXCHNG_RATE_VALUE.elementAt(i).toString().trim().equals("")) {%>
													<input type="radio" name="exchgRate" id='P-<%=VP_EXCHNG_RATE_CD.elementAt(i)%>' value="<%=VP_EXCHNG_RATE_VALUE.elementAt(i)%>" 
													<%-- onclick="setExchnageRate(this,'<%=last_avlb_exchng_dt%>','<%=VP_EXCHNG_RATE_CD.elementAt(i)%>')" HP20230929 DUE TO TDS NOT CALCULATED IF EXCHNG RATE NOT CONFIGURED --%>
													onclick="setExchngRateCd1(this);"
													<%-- <%if(VEXCHNG_RATE_CD.elementAt(i).equals(exchng_rate_cd) && exchang_rate_dt.equals(last_avlb_exchng_dt)){%>checked<%} %> --%>
													>&nbsp;
												<%}%>
												<%=VP_EXCHNG_RATE_VALUE.elementAt(i)%>
											</td>
											<%} %>
										</tr>
										<tr>
											<td><b><%=lable_inv_criteria %></b></td>
											<td align="center"><b><%=lable_inv_date%></b></td>
											<%for(int i=0;i<VB_EXCHNG_RATE_CD.size();i++){ %>
											<td align="center" style="background:<%=VB_BG_COLOR.elementAt(i)%>">
												<%if(!VB_EXCHNG_RATE_VALUE.elementAt(i).toString().trim().equals("")) {%>
													<input type="radio" name="exchgRate" id='B-<%=VB_EXCHNG_RATE_CD.elementAt(i)%>' value="<%=VB_EXCHNG_RATE_VALUE.elementAt(i)%>" 
													<%-- onclick="setExchnageRate(this,'<%=invoice_dt%>','<%=VB_EXCHNG_RATE_CD.elementAt(i)%>')" HP20230929 DUE TO TDS NOT CALCULATED IF EXCHNG RATE NOT CONFIGURED --%>
													onclick="setExchngRateCd1(this);"
													<%-- <%if(VEXCHNG_RATE_CD.elementAt(i).equals(exchng_rate_cd) && exchang_rate_dt.equals(lable_inv_date)){%>checked<%} %> --%>
													>&nbsp;
												<%}%>
												<%=VB_EXCHNG_RATE_VALUE.elementAt(i) %>
											</td>
											<%} %>
										</tr>
										<tr>
											<td><b>User Defined</b></td>
											<td align="center">
												<div style="width:100px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="user_defined_dt" value="<%=user_defined_dt%>" maxLength="10" 
							      						onblur="validateDate(this);" 
							      						onchange="validateDate(this);refresh();" autocomplete="off">
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
					      						</div>
											</td>
											<%for(int i=0;i<VU_EXCHNG_RATE_CD.size();i++){ %>
											<td align="center" style="background:<%=VU_BG_COLOR.elementAt(i)%>">
												<%if(!VU_EXCHNG_RATE_VALUE.elementAt(i).toString().trim().equals("")) {%>
													<input type="radio" name="exchgRate" id='U-<%=VU_EXCHNG_RATE_CD.elementAt(i)%>' value="<%=VU_EXCHNG_RATE_VALUE.elementAt(i)%>" 
													<%-- onclick="setExchnageRate(this,'<%=user_defined_dt%>','<%=VU_EXCHNG_RATE_CD.elementAt(i)%>')"  HP20230929 DUE TO TDS NOT CALCULATED IF EXCHNG RATE NOT CONFIGURED--%>
													onclick="setExchngRateCd1(this);"
													<%-- <%if(VEXCHNG_RATE_CD.elementAt(i).equals(exchng_rate_cd) && exchang_rate_dt.equals(user_defined_dt)){%>checked<%} %> --%>
													>&nbsp;
												<%}%>
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
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Storage Charge Details</label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th rowspan="2">Skip Date</th>
											<th rowspan="2">Select Discount Days</th>
											<th rowspan="2">Date</th>
											<th rowspan="2">Storage Inventory<br>(MMBTU)</th>
											<th rowspan="2">Offtake Qty<br>(MMBTU)</th>
											<th colspan="2">Storage Charge (<%=price_cd_nm%>/MMBTU)</th> <!-- JD -->
											<th rowspan="2">Amount</th>
										</tr>
										<tr>
											<th><input type="radio" class="form-check-input" name="rate_type" id="rate_type_u" value="U" <%if(VRATE_TYPE.contains("U")) {%>checked<%} %> onclick="enableUserDefine(this);">&nbsp;User Defined</th>
											<th><input type="radio" class="form-check-input" name="rate_type" id="rate_type_c" value="C" <%if(VRATE_TYPE.contains("C")) {%>checked<%}else if(!VRATE_TYPE.contains("U")) { %>checked<%} %> onclick="enableUserDefine(this);">&nbsp;Contactual</th> <!-- JD --> 
										</tr>
									</thead>
									<tbody>
									<%for(int i=0; i<VSTORAGE_DATE.size(); i++){ %>
										<tr>
											<td align="center">
												<input type="checkbox" class="form-check-input" name="skip_chk" id="skip_chk_<%=i%>" 
												onclick="enableSkipRow(this,<%=i%>,<%=VSTORAGE_DATE.size()-1%>);calculateStorageAmt(<%=i%>);" 
												<%if((VSTORAGE_DATE.size()-1)!=i) {%> disabled<%}else if(i==0 && (VSTORAGE_DATE.size()-1)==0){ %>disabled<%} %>>
											</td>
											<td align="center">
												<input type="checkbox" class="form-check-input" name="chk" id="chk_<%=i%>" 
												onclick="calculateStorageAmt(<%=i%>);" <%if(VDISCOUNT_FLAG.elementAt(i).equals("Y")) {%>checked<%} %> 
												<%if(!discount_days.equals("Y")) {%>disabled<%} %>>
												<input type="hidden" name="disc_day_flag" id="disc_day_flag_<%=i%>" value="<%=VDISCOUNT_FLAG.elementAt(i)%>">
											</td>
											<td align="center">
												<%=VSTORAGE_DATE.elementAt(i) %>
												<input type="hidden" name="storage_dt" id="storage_dt_<%=i%>" value="<%=VSTORAGE_DATE.elementAt(i) %>">
											</td>
											<td align="right">
												<%=VSTORAGE_INVENTORY.elementAt(i) %>
												<input type="hidden" name="storage_inventory" id="storage_inventory_<%=i%>" value="<%=VSTORAGE_INVENTORY.elementAt(i) %>">
											</td>
											<td align="right">
												<%=VOFFTAKE_QTY.elementAt(i) %>
												<input type="hidden" name="offtake_qty" id="offtake_qty_<%=i%>" value="<%=VOFFTAKE_QTY.elementAt(i) %>">
											</td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="user_define" id="user_define_<%=i%>" value="<%=VUSER_DEFINE.elementAt(i)%>" 
													style="text-align:right;" 
													onblur="negNumber(this);<%if(price_cd.equals("1")){ %>checkNumber1(this,6,2);<%}else{ %>checkNumber1(this,6,4);<%}%>calculateStorageAmt(<%=i%>);" disabled>
												</div>
											</td>
											<td align="right">
												<%=VSTORAGE_CHARGE.elementAt(i) %>
												<input type="hidden" name="storage_charge" id="storage_charge_<%=i%>" value="<%=VSTORAGE_CHARGE.elementAt(i) %>">
											</td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="storage_amt" id="storage_amt_<%=i%>" value="<%=VSTORAGE_AMT.elementAt(i)%>" 
													style="text-align:right;" 
													onblur="negNumber(this);checkNumber1(this,9,2);" disabled>
												</div>
											</td>
										</tr>
									<%} %>
										<tr style="font-weight: bold;">
											<td colspan="7" align="right">Total Amount in <%=price_cd_nm%> : </td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="total_storage_amt" value="<%=gross_amt%>" 
													style="text-align:right;font-weight: bold;" disabled>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<%if(advance_adj_flag.equals("Y")) {%>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="form-group row">
								<div class="col-auto">
 				  					<label class="form-label"><b><input type="checkbox" class="form-check-input" name="adj_adv_flag" id="adj_adv_flag" onclick="showAdjTbl(this);">&nbsp;<span style="background:#e6e6ff;">Adjust Advance</span></b></label>
				  				</div>
				  			</div>
						</div>
					</div>
					<div class="row" style="display: none;" id="advAdjTbl">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th rowspan="2">Sr#</th>
											<th rowspan="2">Receipt Voucher</th>
											<th colspan="2">Gross Adjustment (INR)</th>
											<%if(VMULTI_TAX_STRUCT.size()>0)
											{%>
												<th colspan="<%=((Vector)((Vector)((Vector)((Vector)VMULTI_TAX_STRUCT.elementAt(0)))).elementAt(0)).size()*2%>">Tax Adjustment (INR)</th>
											<%} %>
											<th rowspan="2">Add</th>
										</tr>
										<tr>
											<th>Balance</th>
											<th>Adjustment</th>
											<%if(VMULTI_TAX_STRUCT.size()>0)
											{
												for(int i=0;i<VMULTI_TAX_STRUCT.size();i++)
												{
													Vector temp =(Vector)((Vector)((Vector)VMULTI_TAX_STRUCT.elementAt(i)));
													
													for(int j=0;j<((Vector) temp.elementAt(0)).size(); j++)
													{%>
														<th>Balance <%=((Vector) temp.elementAt(1)).elementAt(j).toString().split(" ")[0]%></th>
														<th>Adjustment <%=((Vector) temp.elementAt(1)).elementAt(j).toString().split(" ")[0]%></th>			
													<%}
												}
											} %>
										</tr>
									</thead>
									<tbody id="adv_tbody">
										<tr id="adv_row_0">
											<td align="center" id="adv_srno_0">1</td>
											<td align="center">
												<div style="width:150px;">
													<select class="form-select form-select-sm" name="receipt_voucher" id="receipt_voucher_0" onchange="fetchReceiptVoucherBalance(this,'0');">
														<option value="">--Select--</option>
														<%for(int i=0;i<VRECEIPT_VOUCHER_MST.size();i++){ %>
														<option value="<%=VRECEIPT_VOUCHER_MST.elementAt(i)%>"><%=VRECEIPT_VOUCHER_MST.elementAt(i)%></option>
														<%} %>
													</select>
												</div>
											</td>
											<td align="center">
												<div style="width:100px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="gross_balance" id="gross_balance_0" readOnly>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:100px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="gross_adjust" id="gross_adjust_0" 
														onblur="negNumber(this);checkNumber1(this,12,2);validateAdjGross(this,'0')" readOnly>
													</div>
												</div>
											</td>
											<%if(VMULTI_TAX_STRUCT.size()>0)
											{
												for(int i=0;i<VMULTI_TAX_STRUCT.size();i++)
												{
													Vector temp =(Vector)((Vector)((Vector)VMULTI_TAX_STRUCT.elementAt(i)));
													
													for(int j=0;j<((Vector) temp.elementAt(0)).size(); j++)
													{
														String taxAbbr=((Vector) temp.elementAt(1)).elementAt(j).toString().split(" ")[0];
													%>
															<td align="center">
																<div style="width:100px;">
																	<div class="input-group input-group-sm" >
																		<input type="text" class="form-control form-control-sm" name="<%=taxAbbr%>_balance" id="<%=taxAbbr%>_balance_0" readOnly>
																	</div>
																</div>
															</td>
															<td align="center">
																<div style="width:100px;">
																	<div class="input-group input-group-sm" >
																		<input type="text" class="form-control form-control-sm" name="<%=taxAbbr%>_adjust" id="<%=taxAbbr%>_adjust_0"
																		onblur="negNumber(this);checkNumber1(this,11,2);validateAdjTax(this,'0','<%=taxAbbr%>')" readOnly>
																	</div>
																</div>
															</td>		
													<%}
												}
											} %>
											<!-- <td align="center">
												<a onclick="addrow();" id="minus">
													<i class="fa fa-minus-circle fa-2x"></i>
												</a>&nbsp;
												<a onclick="addrow();" id="plus>">
													<i class="fa fa-plus-circle fa-2x"></i>
												</a>
											</td> -->
											<td align="center">
										        <a onclick="addRow('0');" id="plus_0">
										            <i class="fa fa-plus-circle fa-2x"></i>
										        </a>&nbsp;
										        <a onclick="removeRow('0');" id="minus_0">
										            <i class="fa fa-minus-circle fa-2x"></i>
										        </a>
										    </td>
										</tr>										
									</tbody>
									<tbody>
										<tr style="font-weight: bold;">
											<td align="right" colspan="3">Total : </td>
											<td align="center">
												<div style="width:100px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="gross_total" id="gross_total" style="font-weight: bold;" readOnly>
													</div>
												</div>
											</td>
											<%if(VMULTI_TAX_STRUCT.size()>0)
											{
												for(int i=0;i<VMULTI_TAX_STRUCT.size();i++)
												{
													Vector temp =(Vector)((Vector)((Vector)VMULTI_TAX_STRUCT.elementAt(i)));
													
													for(int j=0;j<((Vector) temp.elementAt(0)).size(); j++)
													{
														String taxAbbr=((Vector) temp.elementAt(1)).elementAt(j).toString().split(" ")[0];
													%>
															<td align="center">
															</td>
															<td align="center">
																<div style="width:100px;">
																	<div class="input-group input-group-sm" >
																		<input type="text" class="form-control form-control-sm" name="<%=taxAbbr%>_total" id="<%=taxAbbr%>_total" style="font-weight: bold;" readOnly>
																	</div>
																</div>
															</td>		
													<%}
												}
											} %>
											<td></td>
										</tr>
									</tbody>
								</table>
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
										<tr style="display: none;"> <!-- not applicable for storage invoice but passing ZERO(0) as default -->
											<td><b>Allocated Qty</b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<%-- <input type="text" class="form-control form-control-sm" name="alloc_qty" value="<%=qty_mmbtu%>" readOnly> --%>
														<input type="text" class="form-control form-control-sm" name="alloc_qty" value="0" readOnly>
														<span class="input-group-text">MMBTU</span>
													</div>
												</div>
											</td>
										</tr>
										<tr style="display: none;"> <!-- not applicable for storage invoice but passing ZERO(0) as default -->
											<td><b>Confirmed Price<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<%-- <input type="text" class="form-control form-control-sm" name="price" value="<%=price%>" readOnly> --%>
														<input type="text" class="form-control form-control-sm" name="price" value="0" readOnly>
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
														<input type="text" class="form-control form-control-sm" name="gross_amt" value="<%=gross_amt%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%></span>
													</div>
												</div>
											</td>
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
										<tr <%if(!isGrossIncTriff){ %>style="display:none;"<%} %>>
											<td><b>Total Gross Amount<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="gross_include_transport_tariff" value="<%=gross_include_transport_tariff%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
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
																<input type="hidden" name="sub_tax_factor" value="<%=((Vector) temp.elementAt(4)).elementAt(j)%>">
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
										<tr style="display: none;" id="dis_adjted_gross_amt">
											<td><b>Gross Amount <span style="background:#e6e6ff;">(After Adjustment)</b><span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="adjusted_gross_amt" value="" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
										</tr>
										<tr style="display: none;" id="dis_adjted_tax_amt">
											<td><b>Tax (<%=tax_struct_dtl%>) <span style="background:#e6e6ff;">(After Adjustment)</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="adjusted_tax_amt" value="" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
										</tr>
										<%if(VMULTI_TAX_STRUCT.size()>0){
											for(int i=0;i<VMULTI_TAX_STRUCT.size();i++)
											{
												Vector temp =(Vector)((Vector)((Vector)VMULTI_TAX_STRUCT.elementAt(i)));
												
												for(int j=0;j<((Vector) temp.elementAt(0)).size(); j++)
												{
													String taxAbbr=((Vector) temp.elementAt(1)).elementAt(j).toString().split(" ")[0];
												%>
													<tr style="background: #cff4fc;display:none;" id="dis_adjted_<%=taxAbbr%>_amt">
														<td align="right"><b><%=taxAbbr%> <span style="background:#e6e6ff;">(After Adjustment)</span></b></td>
														<td align="center">
															<div style="width:200px;">
																<div class="input-group input-group-sm" >
																	<input type="text" class="form-control form-control-sm" name="adjusted_<%=taxAbbr%>_amt" readOnly>
																	<span class="input-group-text"><%=invoice_raised_in_nm%></span>
																</div>
															</div>
														</td>
													</tr>
												<%}
											}
										} %>
										<tr style="display: none;" id="dis_adjted_net_amt">
											<td><b>Net Payable <span style="background:#e6e6ff;">(After Adjustment)</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="adjusted_net_payable" value="" readOnly>
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
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<div class="row justify-content-end">
							<div class="col-auto">
								<i class="fa fa-eye fa-2x" onclick="viewBeforeSubmit();"></i>
							</div>
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

<input type="hidden" name="option" value="SALES_INVOICE">
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
<input type="hidden" name="skip_period_start_dt" value="<%=period_start_dt%>">
<input type="hidden" name="skip_period_end_dt" value="<%=period_end_dt%>">
<input type="hidden" name="billing_cycle" value="<%=billing_cycle%>">
<input type="hidden" name="bu_unit" value="<%=bu_unit%>">
<input type="hidden" name="bu_state_tin" value="<%=bu_state_tin%>">
<%-- <input type="hidden" name="financial_year" value="<%=financial_year%>"> --%>

<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="exist_financial_year" value="<%=exist_financial_year%>">
<input type="hidden" name="inv_flag" value="<%=inv_flag%>">

<input type="hidden" name="discount_days" value="<%=discount_days%>">

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

<input type="hidden" name="isGrossIncTriff" value="<%=isGrossIncTriff%>">

<input type="hidden" name="exchng_rate_mapp" value="<%=exchng_rate_mapp%>">

<input type="hidden" name="accroid" value="<%=accroid%>">

<input type="hidden" name="plant_gstin_no" value="<%=plant_gstin_no%>">
<input type="hidden" name="bu_gstin_no" value="<%=bu_gstin_no%>">

<input type="hidden" name="advance_adj_flag" value="<%=advance_adj_flag%>">

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
function fetchReceiptVoucherBalance(obj,row_id)
{
	var receiptVoucher=document.getElementsByName("receipt_voucher");
	var receipt_voucher=obj.value;
	
	var invoice_seq = document.forms[0].invoice_seq.value;
	var financial_year = document.forms[0].exist_financial_year.value;
	var bu_state_tin = document.forms[0].bu_state_tin.value;
	var operation = document.forms[0].operation.value;
	
	if(receipt_voucher!="")
	{
		if(receiptVoucher!=null && receiptVoucher!=undefined)
		{
			if(receiptVoucher.length!=undefined)
			{
				for(var i=0;i<receiptVoucher.length;i++)
				{
					if(row_id!=i)
					{
						if(receipt_voucher == receiptVoucher[i].value)
						{
							alert(receipt_voucher+" already Selected!");
							obj.value="";
							receipt_voucher=obj.value;
						}
					}
				}
			}
		}
	}
	
	if(receipt_voucher!="")
	{
		$.post("../servlet/DB_Invoice_Ajax?setCallType=FETCH_RECE_VOUC_BALANCE&receipt_voucher="+receipt_voucher+"&invoice_seq="+invoice_seq+"&financial_year="+financial_year+"&bu_state_tin="+bu_state_tin+"&operation="+operation, function(responseJson) {
			console.log(responseJson);
			var tax_row="";
			$.each(responseJson, function(index, json) 
			{
				$.each(json.ADV_DTL, function(index_1, json_1) 
				{
					if(json_1.INV_COMPONENT == "GROSS")
					{
						document.getElementById("gross_balance_"+row_id).value=json_1.BALANCE_AMT;
						document.getElementById("gross_adjust_"+row_id).readOnly=false;
					}
					else
					{
						let element = document.getElementById(json_1.INV_COMPONENT+"_balance_"+ row_id);
						if(element)
						{
							document.getElementById(json_1.INV_COMPONENT+"_balance_"+ row_id).value=json_1.BALANCE_AMT;
							document.getElementById(json_1.INV_COMPONENT+"_adjust_"+row_id).readOnly=false;
						}
					}
				});
			});
			
			document.getElementById("loading").style.visibility = "hidden";
		});
	}
	else
	{
		document.getElementById("loading").style.visibility = "hidden";
		
		document.getElementById("gross_balance_"+row_id).value="";
		document.getElementById("gross_adjust_"+row_id).value="";
	}
}

function validateAdjGross(obj, row_id)
{
	var adjust_amt=obj.value;
	var gross_amt1 = document.forms[0].gross_amt1.value;
	var gross_balance=document.getElementById("gross_balance_"+row_id).value;
	
	if(gross_amt1!="" && gross_balance!="" && trim(adjust_amt)!="")
	{
		if(parseFloat(adjust_amt) > parseFloat(gross_balance))
		{
			alert("Gross Adjustment("+round(parseFloat(adjust_amt),2)+") should not be > Gross Balance("+round(parseFloat(gross_balance),2)+")!");
			obj.value="";
		}
		else if(parseFloat(adjust_amt) > parseFloat(gross_amt1))
		{
			alert("Gross Adjustment("+round(parseFloat(adjust_amt),2)+") should not be > Invoice Gross Amount("+round(parseFloat(gross_amt1),2)+")!");
			obj.value="";
		}
	}
	
	totalAdjAdvGrossAmt();
}

function validateAdjTax(obj, row_id,taxAbbr)
{
	var adjust_amt=obj.value;
	var tax_balance=document.getElementById(taxAbbr+"_balance_"+row_id).value;
	
	var sub_tax_amt = document.forms[0].sub_tax_amt;
	var sub_tax_struct = document.forms[0].sub_tax_struct;
	
	
	if(sub_tax_amt!=null && sub_tax_amt!=undefined)
	{
		if(sub_tax_amt.length!=undefined)
		{
			for(var i=0;i<sub_tax_amt.length;i++)
			{
				var tax_struct = sub_tax_struct[i].value;
				var tax_amt=sub_tax_amt[i].value;
				
				if(tax_struct.startsWith(taxAbbr))
				{
					if(tax_amt!="" && tax_balance!="" && trim(adjust_amt)!="")
					{
						if(parseFloat(adjust_amt) > parseFloat(tax_balance))
						{
							alert(taxAbbr+" Adjustment("+round(parseFloat(adjust_amt),2)+") should not be > "+taxAbbr+" Balance("+round(parseFloat(tax_balance),2)+")!");
							obj.value="";
						}
						else if(parseFloat(adjust_amt) > parseFloat(tax_amt))
						{
							alert(taxAbbr+" Adjustment("+round(parseFloat(adjust_amt),2)+") should not be > Invoice "+taxAbbr+" Amount("+round(parseFloat(tax_amt),2)+")!");
							obj.value="";
						}
					}
				}
			}
		}
		else
		{
			var tax_struct = sub_tax_struct.value;
			var tax_amt=sub_tax_amt.value;
			
			if(tax_struct.startsWith(taxAbbr))
			{
				if(tax_amt!="" && tax_balance!="" && trim(adjust_amt)!="")
				{
					if(parseFloat(adjust_amt) > parseFloat(tax_balance))
					{
						alert(taxAbbr+" Adjustment("+round(parseFloat(adjust_amt),2)+") should not be > "+taxAbbr+" Balance("+round(parseFloat(tax_balance),2)+")!");
						obj.value="";
					}
					else if(parseFloat(adjust_amt) > parseFloat(tax_amt))
					{
						alert(taxAbbr+" Adjustment("+round(parseFloat(adjust_amt),2)+") should not be > Invoice "+taxAbbr+" Amount("+round(parseFloat(tax_amt),2)+")!");
						obj.value="";
					}
				}
			}
		}
	}
	
	totalAdjAdvTaxAmt(taxAbbr);
}

function totalAdjAdvGrossAmt()
{
	var gross_amt1 = document.forms[0].gross_amt1.value;
	var gross_adjust=document.getElementsByName("gross_adjust");
	
	var total_gross=parseFloat("0");
	var count=parseInt("0");
	
	if(gross_adjust!=null && gross_adjust!=undefined)
	{
		if(gross_adjust.length!=undefined)
		{
			for(var i=0;i<gross_adjust.length;i++)
			{
				if(trim(gross_adjust[i].value) != "")
				{
					count++;
					total_gross= parseFloat(total_gross) + parseFloat(gross_adjust[i].value);
				}
			}
		}
		else
		{
			if(trim(gross_adjust[0].value) != "")
			{
				count++;
				total_gross= parseFloat(total_gross) + parseFloat(gross_adjust[0].value);
			}
		}
	}
	
	document.forms[0].gross_total.value=round(parseFloat(total_gross),2)
	
	if(parseInt(count)==0)
	{
		document.forms[0].adjusted_gross_amt.value=gross_amt1;
	}
	else
	{
		document.forms[0].adjusted_gross_amt.value=round(parseFloat(gross_amt1)-parseFloat(total_gross),2);
	}
	totalNetAmt();
}

function totalAdjAdvTaxAmt(taxAbbr)
{
	var tax_amt = document.forms[0].tax_amt.value
	var sub_tax_amt = document.forms[0].sub_tax_amt;
	var sub_tax_struct = document.forms[0].sub_tax_struct;
	
	var tax_adjust=document.getElementsByName(taxAbbr+"_adjust");
	
	var total_tax=parseFloat("0");
	
	if(tax_adjust!=null && tax_adjust!=undefined)
	{
		if(tax_adjust.length!=undefined)
		{
			for(var i=0;i<tax_adjust.length;i++)
			{
				if(trim(tax_adjust[i].value) != "")
				{
					total_tax= total_tax + parseFloat(tax_adjust[i].value);
				}
			}
		}
		else
		{
			total_tax= total_tax + parseFloat(tax_adjust[0].value);
		}
	}
	
	var ori_tax="";
	if(sub_tax_amt!=null && sub_tax_amt!=undefined)
	{
		if(sub_tax_amt.length!=undefined)
		{
			for(var i=0;i<sub_tax_amt.length;i++)
			{
				var tax_struct = sub_tax_struct[i].value;
				var tax_amt=sub_tax_amt[i].value;
				
				if(tax_struct.startsWith(taxAbbr))
				{
					ori_tax=tax_amt;
				}
			}
		}
		else
		{
			var tax_struct = sub_tax_struct.value;
			var tax_amt=sub_tax_amt.value;
			
			if(tax_struct.startsWith(taxAbbr))
			{
				ori_tax=tax_amt;
			}
		}
	}
	
	document.getElementsByName(taxAbbr+"_total")[0].value=round(parseFloat(total_tax),2)
	document.getElementsByName("adjusted_"+taxAbbr+"_amt")[0].value=round(parseFloat(ori_tax)-parseFloat(total_tax),2);
	
	var main_tax=parseFloat("0");
	var count=parseInt("0");
	if(sub_tax_amt!=null && sub_tax_amt!=undefined)
	{
		if(sub_tax_amt.length!=undefined)
		{
			for(var i=0;i<sub_tax_amt.length;i++)
			{
				var tax_struct = sub_tax_struct[i].value;
				var tax_amt=sub_tax_amt[i].value;
				
				let nameParts = tax_struct.split(' ');
	    		var taxAbbr1= nameParts[0];
	    		
	    		if(document.getElementsByName("adjusted_"+taxAbbr1+"_amt")[0].value != "")
	    		{
	    			count++;
	    			main_tax=parseFloat(main_tax) + parseFloat(document.getElementsByName("adjusted_"+taxAbbr1+"_amt")[0].value);
	    		}
			}
		}
		else
		{
			var tax_struct = sub_tax_struct.value;
			var tax_amt=sub_tax_amt.value;
			
			let nameParts = tax_struct.split(' ');
    		var taxAbbr1= nameParts[0];
    		
    		if(document.getElementsByName("adjusted_"+taxAbbr1+"_amt")[0].value != "")
    		{
    			count++;
    			main_tax=parseFloat(main_tax) + parseFloat(document.getElementsByName("adjusted_"+taxAbbr1+"_amt")[0].value);
    		}
		}
	}
	
	if(parseInt(count)==0)
	{
		document.forms[0].adjusted_tax_amt.value=tax_amt;
	}
	else
	{
		document.forms[0].adjusted_tax_amt.value=round(parseFloat(main_tax),2);
	}
	
	totalNetAmt();
}

function totalNetAmt()
{
	var gross = document.forms[0].adjusted_gross_amt.value
	var tax = document.forms[0].adjusted_tax_amt.value
	
	var gross_amt1 = document.forms[0].gross_amt1.value;
	var tax_amt = document.forms[0].tax_amt.value
	
	var net=parseFloat("0");
	if(gross!="")
	{
		net+=parseFloat(gross);
	}
	else
	{
		net+=parseFloat(gross_amt1);
	}
	
	if(tax!="")
	{
		net+=parseFloat(tax);
	}
	else
	{
		net+=parseFloat(tax_amt);
	}
	
	document.forms[0].adjusted_net_payable.value=round(parseFloat(net),2);
}

//Function to add a new row
function addRow(rowIndex) 
{	
	var index=parseInt(rowIndex) + 1;
	
	//let table = document.querySelector("tbody");
	let table = document.getElementById('adv_tbody');
 	let row = document.getElementById("adv_row_0");
 	let newRow = row.cloneNode(true);
 	newRow.id = "adv_row_" + index;
 	
 	var taxAbbr;
 	newRow.querySelectorAll("input, select,a,td").forEach(input => {
 		
     	if (input.id && input.id.includes("_0")) 
     	{
    		input.id = input.id.replace(/_0$/, "_" + index);
    		input.value = "";
		}
		if (input.name && input.name.includes("_0")) 
		{
    		input.name = input.name.replace(/_0$/, "_" + index);
 		}
		
		if (input.name) 
		{
    		let nameParts = input.name.split('_');
    		taxAbbr= nameParts[0];
		}
     	
		if (input.id && input.id.startsWith("adv_srno_")) 
		{
		    input.innerHTML = parseInt(index) + 1;
		}
   
     	if(input.tagName === "A") 
     	{
     		if(input.id.startsWith("plus_"))
			{
     			input.setAttribute("onclick", "addRow('"+index+"')");
			}
     		else if(input.id.startsWith("minus_"))
     		{
     			input.setAttribute("onclick", "removeRow('"+index+"')");
     		}
     	}
     	else if(input.tagName === "SELECT") 
     	{
            input.setAttribute("onchange", "fetchReceiptVoucherBalance(this,'"+index+"')");
        } 
     	else if (input.tagName === "INPUT") 
        {
        	if(input.name.startsWith("gross_adjust"))
			{
        		input.setAttribute("onblur", "negNumber(this);checkNumber1(this,12,2);validateAdjGross(this,'"+index+"')");
			}
        	else if(input.name.startsWith(taxAbbr+"_adjust"))
        	{
        		input.setAttribute("onblur", "negNumber(this);checkNumber1(this,11,2);validateAdjTax(this,'"+index+"','"+taxAbbr+"')");	
        	}
        }
 	});

 	table.appendChild(newRow);
 	document.getElementById('plus_'+rowIndex).style.display="none";
	document.getElementById('minus_'+rowIndex).style.display="none";
	
	document.getElementById('plus_'+index).style.display="";
	document.getElementById('minus_'+index).style.display="";
}

function removeRow(rowIndex) 
{
	//let table = document.querySelector("tbody");
	let table = document.getElementById('adv_tbody');
 	let rows = table.querySelectorAll("tr");

 	if (rows.length > 1) 
 	{
     	table.removeChild(rows[rows.length - 1]);
     	
     	var temp_rowIndex=parseInt(rowIndex)-1;
     	document.getElementById('plus_'+temp_rowIndex).style.display="";
    	document.getElementById('minus_'+temp_rowIndex).style.display="";
 	}
}

function fetchExistingReceiptVoucherDtl(obj,row_id)
{
	var receipt_voucher=obj.value;
	var invoice_seq = document.forms[0].invoice_seq.value;
	var financial_year = document.forms[0].exist_financial_year.value;
	var bu_state_tin = document.forms[0].bu_state_tin.value;
	
	if(receipt_voucher!="" && invoice_seq!="" && financial_year!="" && bu_state_tin!="")
	{
		$.post("../servlet/DB_Invoice_Ajax?setCallType=FETCH_EXISTING_RECE_VOUC_DTL&receipt_voucher="+receipt_voucher+"&invoice_seq="+invoice_seq+"&financial_year="+financial_year+"&bu_state_tin="+bu_state_tin, function(responseJson) {
			console.log(responseJson);
			var tax_row="";
			$.each(responseJson, function(index, json) 
			{
				$.each(json.ADV_DTL, function(index_1, json_1) 
				{
					if(json_1.INV_COMPONENT == "GROSS")
					{
						document.getElementById("gross_adjust_"+row_id).value=json_1.ADJUSTED_AMT;
						
						totalAdjAdvGrossAmt();
					}
					else
					{
						let element = document.getElementById(json_1.INV_COMPONENT+"_balance_"+ row_id);
						if(element)
						{
							document.getElementById(json_1.INV_COMPONENT+"_adjust_"+ row_id).value=json_1.ADJUSTED_AMT;
							
							totalAdjAdvTaxAmt(json_1.INV_COMPONENT)
						}
					}
				});
			});
			
			document.getElementById("loading").style.visibility = "hidden";
		});
	}
	else
	{
		document.getElementById("loading").style.visibility = "hidden";
	}
}
</script>
<%if(operation.equals("INSERT") && submission_chk && VEXISTING_RECEIPT_VOUCHER.size() > 0) {%>
	<%for(int i=0; i<VEXISTING_RECEIPT_VOUCHER.size(); i++){ %>
	<script>
		var exitRowId="<%=i%>";
		var sizeOfRowId="<%=VEXISTING_RECEIPT_VOUCHER.size()%>";
		
		if(parseInt(parseInt(exitRowId)) == 0)
		{
			document.getElementById("adj_adv_flag").checked=true;
			showAdjTbl(document.getElementById("adj_adv_flag"));
		}
		document.getElementById("receipt_voucher_"+exitRowId).value="<%=VEXISTING_RECEIPT_VOUCHER.elementAt(i)%>";
		fetchExistingReceiptVoucherDtl(document.getElementById("receipt_voucher_"+exitRowId),exitRowId);
		if(parseInt(parseInt(exitRowId)+1) < parseInt(sizeOfRowId))
		{
			addRow(exitRowId);
		}
	</script>
	<%} %>
<%} %>
</form>
</body>
</html>