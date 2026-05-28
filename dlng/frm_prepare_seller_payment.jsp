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
	var bu_unit = document.forms[0].bu_unit.value;
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev = document.forms[0].cont_rev.value;
	var cargo_no = document.forms[0].cargo_no.value;
	var boe_no = document.forms[0].boe_no.value;
	var contract_type = document.forms[0].contract_type.value;
	var plant_seq = document.forms[0].plant_seq.value;
	var period_start_dt = document.forms[0].period_start_dt.value;
	var period_end_dt = document.forms[0].period_end_dt.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	var qty_mmbtu = document.forms[0].qty_mmbtu.value;
	var inv_flag = document.forms[0].inv_flag.value;
	
	var sys_invoice_dt = document.forms[0].sys_invoice_dt.value;
	var sys_invoice_due_dt = document.forms[0].sys_invoice_due_dt.value;
	
	var activity_type = document.forms[0].activity_type.value;
	
	var accroid = document.forms[0].accroid.value;
	var u = document.forms[0].u.value;
	
	var url = "frm_prepare_seller_payment.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cargo_no="+cargo_no+"&boe_no="+boe_no+"&cont_no="+cont_no+
		"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_start_dt+
		"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&activity_type="+activity_type+
		"&qty_mmbtu="+qty_mmbtu+"&invoice_dt="+sys_invoice_dt+"&invoice_due_dt="+sys_invoice_due_dt+"&inv_flag="+inv_flag+
		"&accroid="+accroid+"&u="+u;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function refresh1()
{
	var bu_unit = document.forms[0].bu_unit.value;
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev = document.forms[0].cont_rev.value;
	var cargo_no = document.forms[0].cargo_no.value;
	var boe_no = document.forms[0].boe_no.value;
	var contract_type = document.forms[0].contract_type.value;
	var plant_seq = document.forms[0].plant_seq.value;
	var period_start_dt = document.forms[0].period_start_dt.value;
	var period_end_dt = document.forms[0].period_end_dt.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	var qty_mmbtu = document.forms[0].qty_mmbtu.value;
	var inv_flag = document.forms[0].inv_flag.value;
	
	var sys_invoice_dt = document.forms[0].sys_invoice_dt.value;
	var sys_invoice_due_dt = document.forms[0].sys_invoice_due_dt.value;
	
	var activity_type = document.forms[0].activity_type.value;
	
	var accroid = document.forms[0].accroid.value;
	var u = document.forms[0].u.value;
	
	var url = "frm_prepare_seller_payment.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cargo_no="+cargo_no+"&boe_no="+boe_no+"&cont_no="+cont_no+
		"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_start_dt+
		"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&refresh_flg=Y&activity_type="+activity_type+
		"&qty_mmbtu="+qty_mmbtu+"&invoice_dt="+sys_invoice_dt+"&invoice_due_dt="+sys_invoice_due_dt+"&inv_flag="+inv_flag+
		"&accroid="+accroid+"&u="+u;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function enableSystemGen(flag)
{
	var sys_invoice_no = document.forms[0].sys_invoice_no
	var sys_invoice_dt = document.forms[0].sys_invoice_dt
	var sys_invoice_due_dt = document.forms[0].sys_invoice_due_dt
	var sys_alloc_qty = document.forms[0].sys_alloc_qty
	var sys_price = document.forms[0].sys_price
	var sys_gross_amt = document.forms[0].sys_gross_amt
	var sys_exchng_rate = document.forms[0].sys_exchng_rate
	var sys_gross_amt1 = document.forms[0].sys_gross_amt1
	var sys_tax_amt = document.forms[0].sys_tax_amt
	var sys_invoice_amt = document.forms[0].sys_invoice_amt
	var sys_adj_plusmin = document.forms[0].sys_adj_plusmin
	var sys_adj_amt = document.forms[0].sys_adj_amt
	var sys_net_payable = document.forms[0].sys_net_payable
	var sys_tcs_amt = document.forms[0].sys_tcs_amt
	var sys_remark = document.forms[0].sys_remark
	
	var sys_ref_icon = document.getElementById("sys_ref_icon")
	var sys_view = document.getElementById("sys_view")
	var sys_submit = document.getElementById("sys_submit")
	var sys_chk_sub = document.getElementById("sys_chk_sub")
	var sys_auth_sub = document.getElementById("sys_auth_sub")
	
	var write_access = document.forms[0].write_access.value;
	
	var sg_submission_chk = document.forms[0].sg_submission_chk.value;
	
	var activity_type = document.forms[0].activity_type.value;
		
	if(sys_invoice_no!=null && sys_invoice_no!=undefined)
	{
		var system_chk_flag = document.forms[0].system_chk_flag
		var system_auth_flag = document.forms[0].system_auth_flag
		
		/* if(system_auth_flag.value=="Y")
		{
			flag=false;
		} */
		if(flag)
		{
			sys_invoice_no.readOnly=false;
			sys_invoice_dt.readOnly=false;
			
			sys_invoice_due_dt.readOnly=false;
			
			if(activity_type=="PREPARE")
			{
				sys_invoice_dt.style.pointerEvents = "auto";
				sys_invoice_due_dt.style.pointerEvents = "auto";
			}
			
			
			//sys_view.style.pointerEvents = "auto";
			//sys_submit.style.pointerEvents = "auto";
			//sys_chk_sub.style.pointerEvents = "auto";
			//sys_auth_sub.style.pointerEvents = "auto";
			sys_view.disabled=false;
			if(write_access=="Y")
			{
				sys_submit.disabled=false;
			}
			else
			{
				sys_submit.disabled=true;
			}
			sys_ref_icon.style.pointerEvents = "auto";
			//sys_auth_sub.disabled=false;
			
			sys_remark.readOnly=false;
			
			if(system_chk_flag.value=="Y")
			{
				sys_submit.disabled=true;
				sys_ref_icon.style.pointerEvents = "none";
				
				if(document.forms[0].invoice_aprv_flag.value=="A" || document.forms[0].pg_invoice_aprv_flag.value=="A")
				{
					sys_auth_sub.disabled=true;
				}
				else
				{
					sys_auth_sub.disabled=false;
				}
			}
			else
			{
				sys_auth_sub.disabled=true;
			}
			
			if(system_auth_flag.value=="Y" || sg_submission_chk == "false")
			{
				sys_chk_sub.disabled=true;
			}
			else
			{
				sys_chk_sub.disabled=false;
			}
		}
		else
		{
			sys_invoice_no.readOnly=true;
			sys_invoice_dt.readOnly=true;
			sys_invoice_dt.style.pointerEvents = "none";
			sys_invoice_due_dt.readOnly=true;	
			sys_invoice_due_dt.style.pointerEvents = "none";
			
			//sys_view.style.pointerEvents = "none";
			//sys_submit.style.pointerEvents = "none";
			//sys_chk_sub.style.pointerEvents = "none";
			//sys_auth_sub.style.pointerEvents = "none";
			sys_view.disabled=true;
			sys_submit.disabled=true;
			sys_ref_icon.style.pointerEvents = "none";
			sys_chk_sub.disabled=true;
			sys_remark.readOnly=true;
			
			/* if(system_auth_flag.value=="Y")
			{
				sys_auth_sub.disabled=false;
			}
			else
			{ */
				sys_auth_sub.disabled=true;
			//}
		}
		sys_adj_plusmin.style.pointerEvents = "none";
	}
}

function enablePartyGen(flag)
{
	var party_invoice_no = document.forms[0].party_invoice_no;
	var party_invoice_dt = document.forms[0].party_invoice_dt;
	var party_invoice_due_dt = document.forms[0].party_invoice_due_dt;
	var party_alloc_qty = document.forms[0].party_alloc_qty;
	var party_price = document.forms[0].party_price;
	var party_gross_amt = document.forms[0].party_gross_amt;
	var party_exchng_rate = document.forms[0].party_exchng_rate;
	var party_gross_amt1 = document.forms[0].party_gross_amt1;
	var party_tax_amt = document.forms[0].party_tax_amt;
	var party_invoice_amt = document.forms[0].party_invoice_amt;
	var party_adj_plusmin = document.forms[0].party_adj_plusmin;
	var party_adj_amt = document.forms[0].party_adj_amt;
	var party_net_payable = document.forms[0].party_net_payable;
	var party_tcs_amt = document.forms[0].party_tcs_amt
	var party_cif_amt = document.forms[0].party_cif_amt
	var party_assessable_amt = document.forms[0].party_assessable_amt
	var party_remark = document.forms[0].party_remark
	var party_diff_price = document.forms[0].party_diff_price;
	var party_cd_paid_amt = document.forms[0].party_cd_paid_amt;
	var party_marketing_margin_amount = document.forms[0].party_marketing_margin_amount;
	var party_other_charges_amount = document.forms[0].party_other_charges_amount;
	var party_transportation_amount = document.forms[0].party_transportation_amount;
	//var party_gross_include_transport_tariff = document.forms[0].party_gross_include_transport_tariff;
	
	var party_sub_tax_amt = document.forms[0].party_sub_tax_amt
	
	var party_view = document.getElementById("party_view")
	var party_submit = document.getElementById("party_submit")
	var party_chk_sub = document.getElementById("party_chk_sub")
	var party_auth_sub = document.getElementById("party_auth_sub")
	
	var write_access = document.forms[0].write_access.value;
	
	var pg_submission_chk = document.forms[0].pg_submission_chk.value;
	
	var activity_type = document.forms[0].activity_type.value;
	
	if(party_invoice_no!=null && party_invoice_no!=undefined)
	{
		var party_chk_flag = document.forms[0].party_chk_flag
		var party_auth_flag = document.forms[0].party_auth_flag
		
		/* if(party_auth_flag.value == "Y")
		{
			flag=false;
		} */
		if(flag)
		{
			//party_invoice_no.readOnly=false;
			party_invoice_dt.readOnly=false;
			
			party_invoice_due_dt.readOnly=false;
			
			party_alloc_qty.readOnly=false;
			party_price.readOnly=false;
			
			if(activity_type=="PREPARE")
			{
				party_invoice_dt.style.pointerEvents = "auto";
				party_invoice_due_dt.style.pointerEvents = "auto";
			}
			if(party_gross_amt!=null && party_gross_amt!=undefined)
			{
				party_gross_amt.readOnly=false;
				party_exchng_rate.readOnly=false;
			}
			party_gross_amt1.readOnly=false;
			party_tax_amt.readOnly=false;
			party_invoice_amt.readOnly=false;
			party_adj_plusmin.style.pointerEvents = "auto";
			party_adj_amt.readOnly=false;
			party_net_payable.readOnly=false;
			party_tcs_amt.readOnly=false;
			
			party_cif_amt.readOnly=false;
			party_assessable_amt.readOnly=false;
			party_remark.readOnly=false;
			party_diff_price.readOnly=false;
			party_cd_paid_amt.readOnly=false;
			
			party_marketing_margin_amount.readOnly=false;
			party_other_charges_amount.readOnly=false;
			party_transportation_amount.readOnly=false;
			//party_gross_include_transport_tariff.readOnly=false;
			
			//party_view.style.pointerEvents = "auto";
			//party_submit.style.pointerEvents = "auto";
			//party_chk_sub.style.pointerEvents = "auto";
			//party_auth_sub.style.pointerEvents = "auto";
			
			if(party_sub_tax_amt!=null && party_sub_tax_amt!=undefined)
			{
				if(party_sub_tax_amt.length!=undefined)
				{
					for(var i=0;i<party_sub_tax_amt.length;i++)
					{
						party_sub_tax_amt[i].readOnly=false;
					}
				}
				else
				{
					party_sub_tax_amt.readOnly=false;
				}
			}
			
			party_view.disabled=false;
			//party_submit.disabled=false;
			
			if(write_access=="Y")
			{
				party_submit.disabled=false;
			}
			else
			{
				party_submit.disabled=true;
			}
			
			party_auth_sub.disabled=false;
			
			if(party_chk_flag.value=="Y")
			{
				party_submit.disabled=true;
				
				if(document.forms[0].invoice_aprv_flag.value=="A" || document.forms[0].pg_invoice_aprv_flag.value=="A")
				{
					party_auth_sub.disabled=true;
				}
				else
				{
					party_auth_sub.disabled=false;
				}
			}
			else
			{
				party_auth_sub.disabled=true;
			}
			
			if(party_auth_flag.value=="Y" || pg_submission_chk == "false")
			{
				party_chk_sub.disabled=true;
			}
			else
			{
				party_chk_sub.disabled=false;
			}
		}
		else
		{
			//party_invoice_no.readOnly=true;
			party_invoice_dt.readOnly=true;
			party_invoice_dt.style.pointerEvents = "none";
			party_invoice_due_dt.readOnly=true;
			party_invoice_due_dt.style.pointerEvents = "none";
			party_alloc_qty.readOnly=true;
			party_price.readOnly=true;
			if(party_gross_amt!=null && party_gross_amt!=undefined)
			{
				party_gross_amt.readOnly=true;
				party_exchng_rate.readOnly=true;
			}
			party_gross_amt1.readOnly=true;
			party_tax_amt.readOnly=true;
			party_invoice_amt.readOnly=true;
			party_adj_plusmin.style.pointerEvents = "none";
			party_adj_amt.readOnly=true;
			party_net_payable.readOnly=true;
			party_tcs_amt.readOnly=true;
			
			party_cif_amt.readOnly=true;
			party_assessable_amt.readOnly=true;
			party_remark.readOnly=true;
			party_diff_price.readOnly=true;
			party_cd_paid_amt.readOnly=true;
			
			party_marketing_margin_amount.readOnly=true;
			party_other_charges_amount.readOnly=true;
			party_transportation_amount.readOnly=true;
			//party_gross_include_transport_tariff.readOnly=true;
			
			//party_view.style.pointerEvents = "none";
			//party_submit.style.pointerEvents = "none";
			//party_chk_sub.style.pointerEvents = "none";
			//party_auth_sub.style.pointerEvents = "none";
			
			party_view.disabled=true;
			party_submit.disabled=true;
			party_chk_sub.disabled=true;
			/* if(party_auth_flag.value=="Y")
			{
				party_auth_sub.disabled=false;
			}
			else
			{ */
				party_auth_sub.disabled=true;
			//}
			
			if(party_sub_tax_amt!=null && party_sub_tax_amt!=undefined)
			{
				if(party_sub_tax_amt.length!=undefined)
				{
					for(var i=0;i<party_sub_tax_amt.length;i++)
					{
						party_sub_tax_amt[i].readOnly=true;
					}
				}
				else
				{
					party_sub_tax_amt.readOnly=true;
				}
			}
		}
	}
}

function doEnabled(obj)
{
	if(obj.value == "P")
	{
		enableSystemGen(false);
		enablePartyGen(true);
	}
	else if(obj.value == "S")
	{
		enableSystemGen(true);
		enablePartyGen(false);
	}
}

enableButton=true;
function doSubmit(inv_flg)
{
	var contact_person = document.forms[0].contact_person.value
	var bu_contact_person = document.forms[0].bu_contact_person.value
	
	var contract_type = document.forms[0].contract_type.value;
	var cargo_no = document.forms[0].cargo_no.value;
	var inv_flag = document.forms[0].inv_flag.value;
	
	var sys_invoice_no = document.forms[0].sys_invoice_no.value
	var sys_invoice_dt = document.forms[0].sys_invoice_dt.value
	var sys_invoice_due_dt = document.forms[0].sys_invoice_due_dt.value
	var sys_alloc_qty = document.forms[0].sys_alloc_qty.value
	var sys_price = document.forms[0].sys_price.value
	//var sys_gross_amt = document.forms[0].sys_gross_amt.value
	//var sys_exchng_rate = document.forms[0].sys_exchng_rate.value
	var sys_gross_amt1 = document.forms[0].sys_gross_amt1.value
	var sys_tax_amt = document.forms[0].sys_tax_amt.value
	var sys_invoice_amt = document.forms[0].sys_invoice_amt.value
	var sys_adj_plusmin = document.forms[0].sys_adj_plusmin.value
	var sys_adj_amt = document.forms[0].sys_adj_amt.value
	var sys_net_payable = document.forms[0].sys_net_payable.value
	var sys_tds_cd = document.forms[0].sys_tds_cd.value
	var sys_tds_amt = document.forms[0].sys_tds_amt.value
	var sys_tds_factor = document.forms[0].sys_tds_factor.value
	var sys_diff_price = document.forms[0].sys_diff_price.value
	var sys_marketing_margin_amount = document.forms[0].sys_marketing_margin_amount.value;
	var sys_other_charges_amount = document.forms[0].sys_other_charges_amount.value;
	var sys_transportation_amount = document.forms[0].sys_transportation_amount.value;
	var sys_gross_include_transport_tariff = document.forms[0].sys_gross_include_transport_tariff.value;
	
	var party_invoice_no = document.forms[0].party_invoice_no.value
	var party_invoice_dt = document.forms[0].party_invoice_dt.value
	var party_invoice_due_dt = document.forms[0].party_invoice_due_dt.value
	var party_alloc_qty = document.forms[0].party_alloc_qty.value
	var party_price = document.forms[0].party_price.value
	//var party_gross_amt = document.forms[0].party_gross_amt.value
	//var party_exchng_rate = document.forms[0].party_exchng_rate.value
	var party_gross_amt1 = document.forms[0].party_gross_amt1.value
	var party_tds_cd = document.forms[0].party_tds_cd.value
	var party_tax_amt = document.forms[0].party_tax_amt.value
	var party_invoice_amt = document.forms[0].party_invoice_amt.value
	var party_adj_plusmin = document.forms[0].party_adj_plusmin.value
	var party_adj_amt = document.forms[0].party_adj_amt.value
	var party_net_payable = document.forms[0].party_net_payable.value
	var party_diff_price = document.forms[0].party_diff_price.value
	var party_marketing_margin_amount = document.forms[0].party_marketing_margin_amount.value;
	var party_other_charges_amount = document.forms[0].party_other_charges_amount.value;
	var party_transportation_amount = document.forms[0].party_transportation_amount.value;
	var party_gross_include_transport_tariff = document.forms[0].party_gross_include_transport_tariff.value;
	
	var applicable_abbr = document.forms[0].applicable_abbr.value
	
	var msg="";
	var flag=true;
	
	if(contact_person=="0" || trim(contact_person) == "")
	{
		msg+="Select Trader Contact Person!\n";
		flag=false;
	}
	if(bu_contact_person=="0" || trim(bu_contact_person) == "")
	{
		msg+="Select Business Contact Person!\n";
		flag=false;
	}
	
	if(inv_flg == "S")
	{
		if(trim(sys_invoice_no)=="")
		{
			msg+="Invoice No missing!\n";
			flag=false;
		}
		if(trim(sys_invoice_dt)=="")
		{
			msg+="Enter Invoice Date!\n";
			flag=false;
		}
		if(trim(sys_invoice_due_dt)=="")
		{
			msg+="Enter Invoice Due Date!\n";
			flag=false;
		}
		if(trim(sys_invoice_dt)!="" && trim(sys_invoice_due_dt)!="")
		{
			var count = compareDate(sys_invoice_dt,sys_invoice_due_dt);
			if(parseInt(count) == 1)
			{
				msg+="Invoice Due Date should be grater or equal Invoice Date!";
				flag=false;
			}
		}
		if(trim(sys_price)=="")
		{
			msg+="Confirmed Price missing!\n";
			flag=false;
		}
		if(trim(sys_gross_amt1)=="")
		{
			msg+="Gross Amount missing!\n";
			flag=false;
		}
		if((contract_type!="N" && inv_flag!="P") || (inv_flag=="CP" || inv_flag=="CF"))
		{
			if(trim(sys_tax_amt)=="")
			{
				msg+="Tax Amount missing!\n";
				flag=false;
			}
		}
		if(trim(sys_invoice_amt)=="")
		{
			msg+="Invoice Amount missing!\n";
			flag=false;
		}
		if(applicable_abbr=="TDS")
		{
			if(trim(sys_tds_cd)=="")
			{
				var tdsCnt=parseInt("0");
				if(trim(sys_tds_factor)=="" || sys_tds_factor=="0.00" || sys_tds_factor=="0.000" || sys_tds_factor=="0")
				{
					tdsCnt++;
				}
				if(trim(sys_tds_amt)=="" || sys_tds_amt=="0.00" || sys_tds_amt=="0.000" || sys_tds_amt=="0")
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
	else if(inv_flg=="P")
	{
		if(trim(party_invoice_no)=="")
		{
			msg+="Invoice No missing!\n";
			flag=false;
		}
		if(trim(party_invoice_dt)=="")
		{
			msg+="Enter Invoice Date!\n";
			flag=false;
		}
		if(trim(party_invoice_due_dt)=="")
		{
			msg+="Enter Invoice Due Date!\n";
			flag=false;
		}
		if(trim(party_invoice_dt)!="" && trim(party_invoice_due_dt)!="")
		{
			var count = compareDate(party_invoice_dt,party_invoice_due_dt);
			if(parseInt(count) == 1)
			{
				msg+="Invoice Due Date should be grater or equal Invoice Date!";
				flag=false;
			}
		}
		if(trim(party_price)=="")
		{
			msg+="Confirmed Price missing!\n";
			flag=false;
		}
		if(trim(party_gross_amt1)=="")
		{
			msg+="Gross Amount missing!\n";
			flag=false;
		}
		if((contract_type!="N" && inv_flag!="P") || (inv_flag=="CP" || inv_flag=="CF"))
		{
			if(trim(party_tax_amt)=="")
			{
				msg+="Tax Amount missing!\n";
				flag=false;
			}
		}
		if(trim(party_invoice_amt)=="")
		{
			msg+="Invoice Amount missing!\n";
			flag=false;
		}
	}
	
	if(flag)
	{
		var a;
		if(inv_flg == "S")
		{
			a=confirm("Do you want to Submit System Generated Invoice?");
		}
		else if(inv_flg == "P")
		{
			a=confirm("Do you want to Submit Party Generated Invoice?");
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

var newWindow="";
function printPDF(inv_flag)
{
	var bu_unit = document.forms[0].bu_unit.value;
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev = document.forms[0].cont_rev.value;
	var contract_type = document.forms[0].contract_type.value;
	var plant_seq = document.forms[0].plant_seq.value;
	var period_start_dt = document.forms[0].period_start_dt.value;
	var period_end_dt = document.forms[0].period_end_dt.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	
	var sys_financial_year = document.forms[0].sys_financial_year.value;
	
	var url = "../pdf_buy_invoice_remittance.jsp?inv_type="+inv_flag+"&counterparty_cd="+counterparty_cd+
			"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&contract_type="+contract_type+
			"&plant_seq="+plant_seq+"&bu_unit="+bu_unit+"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+
			"&billing_cycle="+billing_cycle+"&financial_year="+sys_financial_year;
	
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

function viewBeforeSubmit(inv_type)
{
	var bu_unit = document.forms[0].bu_unit.value;
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev = document.forms[0].cont_rev.value;
	var contract_type = document.forms[0].contract_type.value;
	var cargo_no = document.forms[0].cargo_no.value;
	var boe_no = document.forms[0].boe_no.value;
	var inv_flag = document.forms[0].inv_flag.value;
	var plant_seq = document.forms[0].plant_seq.value;
	var period_start_dt = document.forms[0].period_start_dt.value;
	var period_end_dt = document.forms[0].period_end_dt.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	
	var sys_financial_year = document.forms[0].sys_financial_year.value;
	var sys_price_cd = document.forms[0].sys_price_cd.value;
	var sys_invoice_raised_in = document.forms[0].sys_invoice_raised_in.value;
	
	var contact_person = document.forms[0].contact_person.value
	var bu_contact_person = document.forms[0].bu_contact_person.value
	var applicable_flag = document.forms[0].applicable_flag.value
	
	var adj_plusmin="";
	var mkt_mrgin = "";
	var oth_chrg = "";
	var trans_tariff = "";
	var isGrossIncTriff = document.forms[0].isGrossIncTriff.value;
	if(inv_type=="P")
	{
		adj_plusmin= document.forms[0].party_adj_plusmin.value
		mkt_mrgin = document.forms[0].party_marketing_margin.value;
		oth_chrg = document.forms[0].party_other_charges.value;
		trans_tariff = document.forms[0].party_transportation_tariff.value;
	}
	else
	{
		adj_plusmin= document.forms[0].sys_adj_plusmin.value
		mkt_mrgin = document.forms[0].sys_marketing_margin.value;
		oth_chrg = document.forms[0].sys_other_charges.value;
		trans_tariff = document.forms[0].sys_transportation_tariff.value;
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
		var url = "../remittance/rpt_view_before_submit.jsp?inv_type="+inv_type+"&counterparty_cd="+counterparty_cd+
			"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&contract_type="+contract_type+
			"&plant_seq="+plant_seq+"&bu_unit="+bu_unit+"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+
			"&billing_cycle="+billing_cycle+"&financial_year="+sys_financial_year+"&price_cd="+sys_price_cd+
			"&mkt_mrgin="+mkt_mrgin+"&oth_chrg="+oth_chrg+"&trans_tariff="+trans_tariff+"&isGrossIncTriff="+isGrossIncTriff+
			"&invoice_raised_in="+sys_invoice_raised_in+"&contact_person="+contact_person+"&bu_contact_person="+bu_contact_person+
			"&adj_plusmin="+adj_plusmin+"&applicable_flag="+applicable_flag+"&cargo_no="+cargo_no+"&boe_no="+boe_no+"&inv_flag="+inv_flag;
		
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

function checkInvoice(flag)
{
	var check_access = document.forms[0].check_access.value;
	var contract_type = document.forms[0].contract_type.value;
	var inv_flag = document.forms[0].inv_flag.value;
	
	if(check_access=="Y")
	{
		var sys_chk = document.forms[0].sys_chk;
		var party_chk = document.forms[0].party_chk;
		
		var msg_flag=true;
		
		if(flag=="S")
		{
			if(!sys_chk[0].checked && !sys_chk[1].checked)
			{
				alert("Check either YES or NO")
				msg_flag=false;
			}
		}
		else if(flag=="P")
		{
			if(!party_chk[0].checked && !party_chk[1].checked)
			{
				alert("Check either YES or NO")
				msg_flag=false;
			}
		}
		else
		{
			msg_flag=false;
		}
		
		if(msg_flag)
		{
			var a;
			if(flag == "S")
			{
				if(contract_type=="N" && inv_flag=="PF" && document.forms[0].sys_chk[0].checked)
				{
					a=confirm("On your Check Approval, Cargo Proforma Remittance will be blocked for Modification.\n\nDo you want to Checked System Generated Invoice?");
				}
				else
				{
					a=confirm("Do you want to Checked System Generated Invoice?");
				}
			}
			else if(flag == "P")
			{
				a=confirm("Do you want to Checked Party Generated Invoice?");
			}
			
			if(a)
			{
				document.forms[0].opration.value="CHECK";
				document.getElementById("loading").style.visibility = "visible";
				editAllowedOnCpStatus = true;
				document.forms[0].submit();
			}
		}
	}
	else
	{
		alert("You do not have access for Check Invoice!")
	}
}

function authorizeInvoice(flag)
{
	var authorize_access = document.forms[0].authorize_access.value;
	
	var sys_auth = document.forms[0].sys_auth;
	var party_auth = document.forms[0].party_auth;
	
	if(authorize_access=="Y")
	{
		var msg_flag=true;
		
		if(flag=="S")
		{
			if(!sys_auth[0].checked && !sys_auth[1].checked)
			{
				alert("Check either YES or NO")
				msg_flag=false;
			}
		}
		else if(flag=="P")
		{
			if(!party_auth[0].checked && !party_auth[1].checked)
			{
				alert("Check either YES or NO")
				msg_flag=false;
			}
		}
		else
		{
			msg_flag=false;
		}
		
		if(msg_flag)
		{
			var a;
			if(flag == "S")
			{
				a=confirm("Do you want to Authorize System Generated Invoice?");
			}
			else if(flag == "P")
			{
				a=confirm("Do you want to Authorize Party Generated Invoice?");
			}
			
			if(a)
			{
				document.forms[0].opration.value="AUTHORIZE";
				document.getElementById("loading").style.visibility = "visible";
				editAllowedOnCpStatus = true;
				document.forms[0].submit();
			}
		}
	}
	else
	{
		alert("You do not have access for Authorize Invoice!")
	}
}

function difference()
{
	var sys_invoice_no = document.forms[0].sys_invoice_no
	var sys_invoice_dt = document.forms[0].sys_invoice_dt
	var sys_invoice_due_dt = document.forms[0].sys_invoice_due_dt
	var sys_alloc_qty = document.forms[0].sys_alloc_qty
	var sys_price = document.forms[0].sys_price
	var sys_gross_amt = document.forms[0].sys_gross_amt
	var sys_exchng_rate = document.forms[0].sys_exchng_rate
	var sys_gross_amt1 = document.forms[0].sys_gross_amt1
	var sys_tax_amt = document.forms[0].sys_tax_amt
	var sys_invoice_amt = document.forms[0].sys_invoice_amt
	var sys_adj_plusmin = document.forms[0].sys_adj_plusmin
	var sys_adj_amt = document.forms[0].sys_adj_amt
	var sys_net_payable = document.forms[0].sys_net_payable
	var sys_tcs_amt = document.forms[0].sys_tcs_amt
	var sys_sub_tax_amt = document.forms[0].sys_sub_tax_amt
	var sys_cif_amt = document.forms[0].sys_cif_amt
	var sys_assessable_amt = document.forms[0].sys_assessable_amt
	var sys_diff_price = document.forms[0].sys_diff_price
	var sys_cd_paid_amt = document.forms[0].sys_cd_paid_amt
	var sys_marketing_margin_amount = document.forms[0].sys_marketing_margin_amount;
	var sys_other_charges_amount = document.forms[0].sys_other_charges_amount;
	var sys_transportation_amount = document.forms[0].sys_transportation_amount;
	var sys_gross_include_transport_tariff = document.forms[0].sys_gross_include_transport_tariff;
	
	var party_invoice_no = document.forms[0].party_invoice_no;
	var party_invoice_dt = document.forms[0].party_invoice_dt;
	var party_invoice_due_dt = document.forms[0].party_invoice_due_dt;
	var party_alloc_qty = document.forms[0].party_alloc_qty;
	var party_price = document.forms[0].party_price;
	var party_gross_amt = document.forms[0].party_gross_amt;
	var party_exchng_rate = document.forms[0].party_exchng_rate;
	var party_gross_amt1 = document.forms[0].party_gross_amt1;
	var party_tax_amt = document.forms[0].party_tax_amt;
	var party_invoice_amt = document.forms[0].party_invoice_amt;
	var party_adj_plusmin = document.forms[0].party_adj_plusmin;
	var party_adj_amt = document.forms[0].party_adj_amt;
	var party_net_payable = document.forms[0].party_net_payable;
	var party_tcs_amt = document.forms[0].party_tcs_amt
	var party_sub_tax_amt = document.forms[0].party_sub_tax_amt
	var party_cif_amt = document.forms[0].party_cif_amt
	var party_assessable_amt = document.forms[0].party_assessable_amt
	var party_diff_price = document.forms[0].party_diff_price
	var party_cd_paid_amt = document.forms[0].party_cd_paid_amt
	var party_marketing_margin_amount = document.forms[0].party_marketing_margin_amount;
	var party_other_charges_amount = document.forms[0].party_other_charges_amount;
	var party_transportation_amount = document.forms[0].party_transportation_amount;
	var party_gross_include_transport_tariff = document.forms[0].party_gross_include_transport_tariff;
	
	var diiff_qty=parseFloat("0");
	var diiff_price=parseFloat("0");
	var diiff_gross=parseFloat("0");
	var diiff_exchg=parseFloat("0");
	var diiff_gross1=parseFloat("0");
	var diiff_txt=parseFloat("0");
	var diiff_totamt=parseFloat("0");
	var diiff_netpay=parseFloat("0");
	var diiff_tcsamt=parseFloat("0");
	var diiff_cif=parseFloat("0");
	var diiff_assessable=parseFloat("0");
	var diiff_diff_price=parseFloat("0");
	var diiff_cd_paid_amt=parseFloat("0");
	var diiff_marketing_margin_amount = parseFloat("0");
	var diiff_other_charges_amount = parseFloat("0");
	var diiff_transportation_amount = parseFloat("0");
	
	var table = document.getElementById("inv_difference");
  	var tr = table.getElementsByTagName("tr");
  	
  	var flag = false;
  	
  	var trCount=parseInt("4"); //first row index;
  	
  	if(sys_invoice_no!=null && sys_invoice_no!=undefined)
	{
	  	if(trim(sys_alloc_qty.value)!="" && trim(party_alloc_qty.value)!="")
		{
	  		var td = tr[trCount].getElementsByTagName("td")
			diiff_qty=parseFloat(party_alloc_qty.value)-parseFloat(sys_alloc_qty.value)
			if(!isNaN(diiff_qty))
			{
				if(parseFloat(diiff_qty) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_qty),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
	  	trCount+=1;
		if(trim(sys_price.value)!="" && trim(party_price.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_price=parseFloat(party_price.value)-parseFloat(sys_price.value)
			if(!isNaN(diiff_price))
			{
				if(parseFloat(diiff_price) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_price),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		trCount+=1;
		if(trim(sys_diff_price.value)!="" && trim(party_diff_price.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_diff_price=parseFloat(party_diff_price.value)-parseFloat(sys_diff_price.value)
			if(!isNaN(diiff_diff_price))
			{
				if(parseFloat(diiff_diff_price) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_diff_price),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		trCount+=1;
		if(trim(sys_gross_amt.value)!="" && trim(party_gross_amt.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_gross=parseFloat(party_gross_amt.value)-parseFloat(sys_gross_amt.value)
			if(!isNaN(diiff_gross))
			{
				if(parseFloat(diiff_gross) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_gross),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		trCount+=1;
		if(trim(sys_exchng_rate.value)!="" && trim(party_exchng_rate.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_exchg=parseFloat(party_exchng_rate.value)-parseFloat(sys_exchng_rate.value)
			if(!isNaN(diiff_exchg))
			{
				if(parseFloat(diiff_exchg) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_exchg),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		trCount+=1;
		if(trim(sys_gross_amt1.value)!="" && trim(party_gross_amt1.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_gross1=parseFloat(party_gross_amt1.value)-parseFloat(sys_gross_amt1.value)
			if(!isNaN(diiff_gross1))
			{
				if(parseFloat(diiff_gross1) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_gross1),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		//Transportation charge
		trCount+=1;
		if(trim(sys_transportation_amount.value)!="" && trim(party_transportation_amount.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_transportation_amount=parseFloat(party_transportation_amount.value)-parseFloat(sys_transportation_amount.value)
			if(!isNaN(diiff_transportation_amount))
			{
				if(parseFloat(diiff_transportation_amount) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_transportation_amount),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		//Market Margin
		trCount+=1;
		if(trim(sys_marketing_margin_amount.value)!="" && trim(party_marketing_margin_amount.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_marketing_margin_amount=parseFloat(party_marketing_margin_amount.value)-parseFloat(sys_marketing_margin_amount.value)
			if(!isNaN(diiff_marketing_margin_amount))
			{
				if(parseFloat(diiff_marketing_margin_amount) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_marketing_margin_amount),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		//Other Charges
		trCount+=1;
		if(trim(sys_other_charges_amount.value)!="" && trim(party_other_charges_amount.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_other_charges_amount=parseFloat(party_other_charges_amount.value)-parseFloat(sys_other_charges_amount.value)
			if(!isNaN(diiff_other_charges_amount))
			{
				if(parseFloat(diiff_other_charges_amount) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_other_charges_amount),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		//Total Gross including charges
		trCount+=1;
		if(trim(sys_gross_include_transport_tariff.value)!="" && trim(party_gross_include_transport_tariff.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_gross_include_transport_tariff=parseFloat(party_gross_include_transport_tariff.value)-parseFloat(sys_gross_include_transport_tariff.value)
			if(!isNaN(diiff_gross_include_transport_tariff))
			{
				if(parseFloat(diiff_gross_include_transport_tariff) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_gross_include_transport_tariff),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		//CIF
		trCount+=1;
		if(trim(sys_cif_amt.value)!="" && trim(party_cif_amt.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_cif=parseFloat(party_cif_amt.value)-parseFloat(sys_cif_amt.value)
			if(!isNaN(diiff_cif))
			{
				if(parseFloat(diiff_cif) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_cif),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		//assessable
		trCount+=1;
		if(trim(sys_assessable_amt.value)!="" && trim(party_assessable_amt.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_assessable=parseFloat(party_assessable_amt.value)-parseFloat(sys_assessable_amt.value)
			if(!isNaN(diiff_assessable))
			{
				if(parseFloat(diiff_assessable) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_assessable),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		trCount+=1;
		if(trim(sys_tax_amt.value)!="" && trim(party_tax_amt.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_txt=parseFloat(party_tax_amt.value)-parseFloat(sys_tax_amt.value)
			if(!isNaN(diiff_txt))
			{
				if(parseFloat(diiff_txt) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_txt),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		if(sys_sub_tax_amt!=null && sys_sub_tax_amt!=undefined)
		{
			if(sys_sub_tax_amt.length!=undefined)
			{
				for(var i=0;i<sys_sub_tax_amt.length;i++)
				{
					trCount+=1;
					if(trim(sys_sub_tax_amt[i].value)!="" && trim(party_sub_tax_amt[i].value)!="")
					{
						var td = tr[trCount].getElementsByTagName("td")
						diiff_txt=parseFloat(party_sub_tax_amt[i].value)-parseFloat(sys_sub_tax_amt[i].value)
						if(!isNaN(diiff_txt))
						{
							if(parseFloat(diiff_txt) != 0)
							{
								td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_txt),2);
								flag = true;
							}
							else
							{
								td = td[(td.length) - 1].innerText="";
							}
						}
						else
						{
							td = td[(td.length) - 1].innerText="";
						}
					}
					else
					{
						var td = tr[trCount].getElementsByTagName("td")
						td = td[(td.length) - 1].innerText="";
					}
				}
			}
			else
			{
				trCount+=1;
				if(trim(sys_sub_tax_amt.value)!="" && trim(party_sub_tax_amt.value)!="")
				{
					var td = tr[trCount].getElementsByTagName("td")
					diiff_txt=parseFloat(party_sub_tax_amt.value)-parseFloat(sys_sub_tax_amt.value)
					if(!isNaN(diiff_txt))
					{
						if(parseFloat(diiff_txt) != 0)
						{
							td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_txt),2);
							flag = true;
						}
						else
						{
							td = td[(td.length) - 1].innerText="";
						}
					}
					else
					{
						td = td[(td.length) - 1].innerText="";
					}
				}
				else
				{
					var td = tr[trCount].getElementsByTagName("td")
					td = td[(td.length) - 1].innerText="";
				}
			}
		}
		
		trCount+=1;
		if(trim(sys_cd_paid_amt.value)!="" && trim(party_cd_paid_amt.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_cd_paid_amt=parseFloat(party_cd_paid_amt.value)-parseFloat(sys_cd_paid_amt.value)
			if(!isNaN(diiff_cd_paid_amt))
			{
				if(parseFloat(diiff_cd_paid_amt) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_cd_paid_amt),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		trCount+=1;
		if(trim(sys_invoice_amt.value)!="" && trim(party_invoice_amt.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_totamt=parseFloat(party_invoice_amt.value)-parseFloat(sys_invoice_amt.value)
			if(!isNaN(diiff_totamt))
			{
				if(parseFloat(diiff_totamt) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_totamt),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}

		trCount+=1;
		if(trim(sys_tcs_amt.value)!="" && trim(party_tcs_amt.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_tcsamt=parseFloat(party_tcs_amt.value)-parseFloat(sys_tcs_amt.value)
			if(!isNaN(diiff_tcsamt))
			{
				if(parseFloat(diiff_tcsamt) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_tcsamt),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		trCount+=1;//skip
		trCount+=1;
		if(trim(sys_net_payable.value)!="" && trim(party_net_payable.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_netpay=parseFloat(party_net_payable.value)-parseFloat(sys_net_payable.value)
			if(!isNaN(diiff_netpay))
			{
				if(parseFloat(diiff_netpay) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_netpay),2);
					flag = true;
				}
				else
				{
					td = td[(td.length) - 1].innerText="";
				}
			}
			else
			{
				td = td[(td.length) - 1].innerText="";
			}
		}
		else
		{
			var td = tr[trCount].getElementsByTagName("td")
			td = td[(td.length) - 1].innerText="";
		}
		
		if(flag)
		{
			var th = tr[0].getElementsByTagName("th")
			th = th[(th.length) - 1].style.display="";
			
			for(var i=1; i<tr.length; i++)
			{
				var td = tr[i].getElementsByTagName("td")
				td = td[(td.length) - 1].style.display="";
			}
		}
		else
		{
			for(var i=1; i<tr.length; i++)
			{
				var td = tr[i].getElementsByTagName("td")
				td = td[(td.length) - 1].style.display="none";
			}
			var th = tr[0].getElementsByTagName("th")
			th = th[(th.length) - 1].style.display="none";
		}
	}
}

function partyGenCalc()
{
	var inv_flag = document.forms[0].inv_flag.value;
	
	var party_alloc_qty = document.forms[0].party_alloc_qty;
	var party_price = document.forms[0].party_price;
	var party_gross_amt = document.forms[0].party_gross_amt;
	var party_exchng_rate = document.forms[0].party_exchng_rate;
	var party_gross_amt1 = document.forms[0].party_gross_amt1;
	var party_tax_amt = document.forms[0].party_tax_amt;
	var party_invoice_amt = document.forms[0].party_invoice_amt;
	var party_adj_plusmin = document.forms[0].party_adj_plusmin;
	var party_adj_amt = document.forms[0].party_adj_amt;
	var party_net_payable = document.forms[0].party_net_payable;
	var party_tcs_amt = document.forms[0].party_tcs_amt;
	var party_marketing_margin = document.forms[0].party_marketing_margin;
	var party_other_charges = document.forms[0].party_other_charges;
	var party_transportation_tariff = document.forms[0].party_transportation_tariff;
	var party_marketing_margin_amount = document.forms[0].party_marketing_margin_amount;
	var party_other_charges_amount = document.forms[0].party_other_charges_amount;
	var party_transportation_amount = document.forms[0].party_transportation_amount;
	var party_gross_include_transport_tariff = document.forms[0].party_gross_include_transport_tariff;
	
	var applicable_flag = document.forms[0].applicable_flag.value;
	var applicable_abbr = document.forms[0].applicable_abbr.value;
	
	var party_invoice_raised_in = document.forms[0].party_invoice_raised_in;
	var party_price_cd = document.forms[0].party_price_cd;
	
	var sys_tax_factor = document.forms[0].sys_tax_factor;
	
	var party_tds_factor = document.forms[0].party_tds_factor;
	var party_tds_amt = document.forms[0].party_tds_amt;
	
	var party_sub_tax_amt=document.forms[0].party_sub_tax_amt;
	var len_multi_tax=document.forms[0].len_multi_tax;
	
	var contract_type=document.forms[0].contract_type;
	
	if(inv_flag=="CP" || inv_flag=="CF")
	{
		if(trim(party_tax_amt.value) != "")
		{
			if(parseInt(len_multi_tax.value)==1) //FOR SINGLE TAX COMPONENT
			{
				party_sub_tax_amt.value=party_tax_amt.value;
			}
			/*COMMENTED BY JD
			var tot_inv=parseFloat(party_tax_amt.value)
			if(!isNaN(tot_inv))
			{
				party_invoice_amt.value=round(parseFloat(tot_inv),2)
				party_net_payable.value=round(parseFloat(tot_inv),2)
			}
			else
			{
				party_invoice_amt.value="";
				party_net_payable.value="";
			}*/
		}
		else
		{
			party_invoice_amt.value="";
			party_net_payable.value="";
		}
		
		if(trim(party_adj_plusmin.value)!="")
		{
			var netPay = parseFloat("0");
			if(trim(party_adj_plusmin.value)=="+")
			{
				netPay = parseFloat(party_invoice_amt.value) + parseFloat(party_adj_amt.value)
			}
			else if(trim(party_adj_plusmin.value)=="-")
			{
				netPay = parseFloat(party_invoice_amt.value) - parseFloat(party_adj_amt.value)
			}
			
			if(!isNaN(netPay))
			{
				party_net_payable.value=round(parseFloat(netPay),2)
			}
			else
			{
				party_net_payable.value="";
			}
		}
	}
	else if(trim(party_price.value) != "" && trim(party_alloc_qty.value)!="")
	{
		/* var gross = parseFloat(party_price.value) * parseFloat(party_alloc_qty.value)
		
		if(!isNaN(gross))
		{
			party_gross_amt.value=round(parseFloat(gross),2)
		}
		else
		{
			party_gross_amt.value="";
		} */
		
		var gross="";
		if(party_gross_amt.value!="")
		{
			gross=parseFloat(party_gross_amt.value)
		}
		
		var gross1="";
		var gross_incl_chrg=parseFloat("0");
		/*if(party_price_cd.value != party_invoice_raised_in.value)
		{
			if(trim(party_exchng_rate.value) != "")
			{
				gross1 = parseFloat(party_exchng_rate.value) * parseFloat(gross);
			}
			else
			{
				gross1 = round(parseFloat("0"),2)
			}
		}
		else
		{
			gross1 = round(parseFloat(gross),2)
		} */
		
		if(!isNaN(gross1))
		{
			//party_gross_amt1.value=round(parseFloat(gross1),2)
			
			if(party_gross_amt1.value!="")
			{
				gross1=parseFloat(party_gross_amt1.value)
				
				gross_incl_chrg=parseFloat(gross1);
			}
			
			//if(party_gross_amt.value == "" && gross1 !="")
			if(party_price_cd.value=="1" && gross1 !="")
			{
				if(!isNaN(gross1))
				{
					party_gross_amt.value=round(parseFloat(gross1),2);
				}
			}
			else if(party_price_cd.value==party_invoice_raised_in.value && gross1 !="")
			{
				if(!isNaN(gross1))
				{
					party_gross_amt.value=round(parseFloat(gross1),2);
				}
			}
			
			if(party_transportation_amount.value!="" && party_transportation_tariff.value!="")
			{
				gross_incl_chrg+=parseFloat(party_transportation_amount.value);
			}
			if(party_marketing_margin_amount.value!="" && party_marketing_margin.value!="")
			{
				gross_incl_chrg+=parseFloat(party_marketing_margin_amount.value);
			}
			if(party_other_charges_amount.value!="" && party_other_charges.value!="")
			{
				gross_incl_chrg+=parseFloat(party_other_charges_amount.value);
			}
			
			party_gross_include_transport_tariff.value=round(parseFloat(gross_incl_chrg),2);
			if(gross_incl_chrg > 0)
			{
				if(applicable_abbr=="TDS" && party_tds_factor.value!="")
				{
					var tds_amt =parseFloat(gross_incl_chrg) * parseFloat(party_tds_factor.value)/100;
					if(!isNaN(tds_amt))
					{
						party_tds_amt.value=round(parseFloat(tds_amt),2)
					}
				}
			}
			
			/* var tax=parseFloat(gross1) * parseFloat(sys_tax_factor.value)/100
			if(!isNaN(tax))
			{
				party_tax_amt.value=round(parseFloat(tax),2)
			}
			else
			{
				party_tax_amt.value="";
			}
			
			var tot_inv=parseFloat(gross1) + parseFloat(tax)
			if(!isNaN(tot_inv))
			{
				party_invoice_amt.value=round(parseFloat(tot_inv),2)
				party_net_payable.value=round(parseFloat(tot_inv),2)
			}
			else
			{
				party_invoice_amt.value="";
				party_net_payable.value="";
			} */
			
			if(contract_type.value=="N")
			{
				if(!isNaN(gross1) && gross1 != "")
				{
					party_invoice_amt.value=round(parseFloat(gross1),2);
					party_net_payable.value=round(parseFloat(gross1),2);
				}
				else
				{
					party_invoice_amt.value="";
					party_net_payable.value="";
				}
			}
			else
			{
				if(trim(party_tax_amt.value) != "")
				{
					if(parseInt(len_multi_tax.value)==1) //FOR SINGLE TAX COMPONENT
					{
						party_sub_tax_amt.value=party_tax_amt.value;
					}
					//var tot_inv=parseFloat(gross1) + parseFloat(party_tax_amt.value)
					var tot_inv=parseFloat(gross_incl_chrg) + parseFloat(party_tax_amt.value)
					if(!isNaN(tot_inv))
					{
						party_invoice_amt.value=round(parseFloat(tot_inv),2)
						
						if(applicable_flag=="Y" && party_tcs_amt.value!="")
						{
							tot_inv =parseFloat(tot_inv) + parseFloat(party_tcs_amt.value)
						}
						
						party_net_payable.value=round(parseFloat(tot_inv),2)
					}
					else
					{
						party_invoice_amt.value="";
						party_net_payable.value="";
					}
				}
				else
				{
					party_invoice_amt.value="";
					party_net_payable.value="";
				}
			}
			
			if(trim(party_adj_plusmin.value)!="")
			{
				var netPay = parseFloat("0");
				if(trim(party_adj_plusmin.value)=="+")
				{
					netPay = parseFloat(party_invoice_amt.value) + parseFloat(party_adj_amt.value)
				}
				else if(trim(party_adj_plusmin.value)=="-")
				{
					netPay = parseFloat(party_invoice_amt.value) - parseFloat(party_adj_amt.value)
				}
				
				if(applicable_flag=="Y" && party_tcs_amt.value!="")
				{
					netPay =parseFloat(netPay) + parseFloat(party_tcs_amt.value)
				}
				
				if(!isNaN(netPay))
				{
					party_net_payable.value=round(parseFloat(netPay),2)
				}
				else
				{
					party_net_payable.value="";
				}
			}
		}
		else
		{
			party_gross_amt1.value="";
			party_invoice_amt.value="";
			party_net_payable.value="";
			party_tcs_amt.value="";
			party_gross_include_transport_tariff.value="";
		}
	}
}

function InvApproval(aprv_flag)
{
	var approve_access = document.forms[0].approve_access.value;
	
	var final_aprv = document.forms[0].final_aprv;
	
	if(approve_access=="Y")
	{	
		if(!final_aprv[0].checked && !final_aprv[1].checked)
		{
			alert("Check either System Generated or Party Generated")
		}
		else
		{
			var inv_nm="";
			if(final_aprv[0].checked)
			{
				inv_nm="System Generated";
			}
			else if(final_aprv[1].checked)
			{
				inv_nm="Party Generated";
			}
			
			var a;
			if(aprv_flag == "A")
			{
				a=confirm("Do you want to Approve "+inv_nm+" Invoice?");
			}
			else if(aprv_flag == "R")
			{
				a=confirm("Do you want to Reject "+inv_nm+" Invoice?");
			}
			
			if(a)
			{
				document.forms[0].inv_aprv_flag.value=aprv_flag;
				document.forms[0].opration.value="APPROVE";
				document.getElementById("loading").style.visibility = "visible";
				editAllowedOnCpStatus = true;
				document.forms[0].submit();
			}
		}
	} 
	else 
	{
		alert("You do not have access for Approve Invoice!")
	}		
}

function copyInvoiceNo()
{
	var sys_invoice_no = document.forms[0].sys_invoice_no;
	var party_invoice_no = document.forms[0].party_invoice_no;
	
	if(sys_invoice_no.value.length>16)
	{
		alert("Invoice# 16 digit allowed!");
		sys_invoice_no.value="";
	}
	party_invoice_no.value=sys_invoice_no.value;
}

function refreshParent(accroid)
{
	window.opener.refresh(accroid);
}

function doGenXML(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,purchase_type_flag,invoice_type,
		sap_approval_flag, bu_seq, cont_no, inv_pdf_flag,accroid,inv_flag,cargo_no,boe_no,view_before)
{
	var url = "../purchase/rpt_view_purchase_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
			"&contract_type="+contract_type+"&purchase_type_flag="+purchase_type_flag+"&invoice_type="+invoice_type+
			"&counterparty_cd="+couterpty_cd+"&invoice_no="+invoice_no+"&sap_approval_flag="+sap_approval_flag+
			"&bu_seq="+bu_seq+"&cont_no="+cont_no+"&inv_pdf_flag="+inv_pdf_flag+"&inv_flag="+inv_flag+"&cargo_no="+cargo_no+"&boe_no="+boe_no+
			"&accroid="+accroid+"&invPg=Y&view_before="+view_before;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Purchase SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Purchase SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function enableTDS(obj)
{
	var sys_tds_amt = document.forms[0].sys_tds_amt.value;
	var new_tds_amt = document.forms[0].new_tds_amt.value;
	
	if(obj.value=="Y")
	{
		document.getElementById("tds_input_div").style.display="";
		
		document.forms[0].isTDSalrted.value="Y";
	}
	else
	{
		document.getElementById("tds_input_div").style.display="none";
		
		//document.forms[0].isTDSalrted.value="N";
	}
	
	document.getElementById('tds_gross_amt').value="";
	document.getElementById('recalc_tds_amt').value="";
	document.getElementById('upt_val_id').innerHTML=document.forms[0].new_tds_amt.value;
	
	document.forms[0].final_tds_amt.value=document.forms[0].new_tds_amt.value;
}

function calcTDS()
{
	var gross1 = document.getElementById('tds_gross_amt').value;
	if(gross1!="")
	{
		var tds_factor=document.forms[0].new_tds_factor.value;
		var tdsAmt="";
		if(tds_factor != "")
		{
			//tdsAmt=((parseFloat(gross1)*parseFloat(tds_factor))/100).toFixed(2)
			
			tdsAmt = (Math.round(gross1 * tds_factor) / 100).toFixed(2);
		}
		
		document.forms[0].recalc_tds_amt.value=tdsAmt;
		
		document.getElementById('upt_val_id').innerHTML=tdsAmt;
		
		document.forms[0].final_tds_amt.value=tdsAmt;
	}
	else
	{
		document.getElementById('upt_val_id').innerHTML=document.forms[0].new_tds_amt.value;
		
		document.forms[0].final_tds_amt.value=document.forms[0].new_tds_amt.value;
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.remittance.DataBean_Remittance" id="remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
String boe_no=request.getParameter("boe_no")==null?"":request.getParameter("boe_no");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String plant_seq=request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String period_start_dt=request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
//String qty_mmbtu=request.getParameter("qty_mmbtu")==null?"":request.getParameter("qty_mmbtu");
String invoice_dt=request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
String invoice_due_dt=request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
String inv_flag=request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");

String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String refresh_flg =request.getParameter("refresh_flg")==null?"":request.getParameter("refresh_flg");
String activity_type =request.getParameter("activity_type")==null?"":request.getParameter("activity_type");

remittance.setCallFlag("SELLER_PAYMENT_DETAIL");
remittance.setComp_cd(owner_cd);
remittance.setCounterparty_cd(counterparty_cd);
remittance.setAgmt_no(agmt_no);
remittance.setAgmt_rev_no(agmt_rev);
remittance.setCont_no(cont_no);
remittance.setCont_rev_no(cont_rev);
remittance.setCargo_no(cargo_no);
remittance.setBoe_no(boe_no);
remittance.setContract_type(contract_type);
remittance.setPlant_seq(plant_seq);
remittance.setBilling_cycle(billing_cycle);
remittance.setPeriod_start_dt(period_start_dt);
remittance.setPeriod_end_dt(period_end_dt);
remittance.setBu_unit(bu_unit);
remittance.setRefresh_flg(refresh_flg);
//remittance.setQty_mmbtu(qty_mmbtu);
remittance.setInvoice_dt(invoice_dt);
remittance.setInvoice_due_dt(invoice_due_dt);
remittance.setInv_flag(inv_flag);
remittance.init();

String boe_nm=remittance.getBoe_nm();

String couterpty_nm=remittance.getCouterpty_nm();
String couterpty_abbr=remittance.getCouterpty_abbr();
String deal_no=remittance.getDeal_no();
String contRef=remittance.getContRef();
String plant_abbr=remittance.getPlant_abbr();
String bu_plant_abbr=remittance.getBu_plant_abbr();
//String qty_mmbtu=remittance.getQty_mmbtu();
String price=remittance.getPrice();
String price_cd=remittance.getPrice_cd();
String price_cd_nm=remittance.getPrice_cd_nm();
String invoice_raised_in = remittance.getInvoice_raised_in();
String invoice_raised_in_nm = remittance.getInvoice_raised_in_nm();
String payment_done_in = remittance.getPayment_done_in();
String payment_done_in_nm = remittance.getPayment_done_in_nm();
String due_days = remittance.getDue_days();
String gross_amt=remittance.getGross_amt();
String gross_amt1=remittance.getGross_amt1();
String exchng_rate_cd=remittance.getExchng_rate_cd();
String exchang_rate = remittance.getExchang_rate();
String exchang_rate_dt = remittance.getExchang_rate_dt();
String energy_unit=remittance.getEnergy_unit();
String energy_unit_nm=remittance.getEnergy_unit_nm();
String tax_amt = remittance.getTax_amt();
String tax_struct_cd=remittance.getTax_struct_cd();
String tax_struct_dt=remittance.getTax_struct_dt();
String tax_struct_dtl=remittance.getTax_struct_dtl();
String tax_info = remittance.getTax_info();
String tax_factor = remittance.getTax_factor();
String invoice_seq=remittance.getInvoice_seq();
String invoice_no=remittance.getInvoice_no();
String sys_invoice_no=remittance.getSys_invoice_no();
String invoice_amt = remittance.getInvoice_amt();
String net_payable = remittance.getNet_payable();
String bu_contact_person_cd = remittance.getBu_contact_person_cd();
String contact_person_cd = remittance.getContact_person_cd();
if(invoice_dt.equals("")){
	invoice_dt = remittance.getInvoice_dt();
}
if(invoice_due_dt.equals("")){
	invoice_due_dt = remittance.getInvoice_due_dt();
}
String invoice_check_flag = remittance.getInvoice_check_flag();
String invoice_check_dt = remittance.getInvoice_check_dt();
String invoice_check_by = remittance.getInvoice_check_by();
String invoice_check_nm = remittance.getInvoice_check_nm();
String invoice_auth_flag = remittance.getInvoice_auth_flag();
String invoice_auth_dt = remittance.getInvoice_auth_dt();
String invoice_auth_by = remittance.getInvoice_auth_by();
String invoice_auth_nm = remittance.getInvoice_auth_nm();
String invoice_aprv_flag = remittance.getInvoice_aprv_flag();
String invoice_aprv_dt = remittance.getInvoice_aprv_dt();
String invoice_aprv_by = remittance.getInvoice_aprv_by();
String invoice_aprv_nm = remittance.getInvoice_aprv_nm();
String invoice_adj_sign = remittance.getInvoice_adj_sign();
String invoice_adj_amt = remittance.getInvoice_adj_amt();
String financial_year = remittance.getFinancial_year();
String applicable_flag = remittance.getApplicable_flag();
String applicable_abbr = remittance.getApplicable_abbr();
String tcs_factor = remittance.getTcs_factor();
String tcs_amount = remittance.getTcs_amount();
String tcs_struct_cd=remittance.getTcs_struct_cd();
String tcs_struct_dt=remittance.getTcs_struct_dt();
String tds_factor = remittance.getTds_factor();
String tds_amount = remittance.getTds_amount();
String tds_struct_cd=remittance.getTds_struct_cd();
String tds_struct_dt=remittance.getTds_struct_dt();
String cif_value=remittance.getCif_value();
String assessable_vlaue=remittance.getAssessable_vlaue();
String remark=remittance.getRemark();
String transportation_charges=remittance.getTransportation_charges();
String transportation_amount=remittance.getTransportation_amount();
String gross_include_transport_tariff=remittance.getGross_include_transport_tariff();
String marketing_margin=remittance.getMarketing_margin();
String marketing_margin_amount=remittance.getMarketing_margin_amount();
String other_charges=remittance.getOther_charges();
String other_charges_amount=remittance.getOther_charges_amount();

String qty_mmbtu = remittance.getQty_mmbtu();

String pg_qty_mmbtu=remittance.getPg_qty_mmbtu();
String pg_price=remittance.getPg_price();
String pg_price_cd=remittance.getPg_price_cd();
String pg_invoice_raised_in=remittance.getPg_invoice_raised_in();
String pg_gross_amt=remittance.getPg_gross_amt();
String pg_gross_amt1=remittance.getPg_gross_amt1();
String pg_exchng_rate_cd=remittance.getPg_exchng_rate_cd();
String pg_exchang_rate = remittance.getPg_exchang_rate();
String pg_exchang_rate_dt = remittance.getPg_exchang_rate_dt();
String pg_tax_amt = remittance.getPg_tax_amt();
String pg_tax_struct_cd=remittance.getPg_tax_struct_cd();
String pg_tax_struct_dt=remittance.getPg_tax_struct_dt();
String pg_tax_info = remittance.getPg_tax_info();
String pg_invoice_seq=remittance.getPg_invoice_seq();
String pg_invoice_no=remittance.getPg_invoice_no();
String pg_sys_invoice_no=remittance.getPg_sys_invoice_no();
String pg_invoice_amt = remittance.getPg_invoice_amt();
String pg_net_payable = remittance.getPg_net_payable();
String pg_invoice_dt = remittance.getPg_invoice_dt();
String pg_invoice_due_dt = remittance.getPg_invoice_due_dt();
String pg_invoice_check_flag = remittance.getPg_invoice_check_flag();
String pg_invoice_check_dt = remittance.getPg_invoice_check_dt();
String pg_invoice_check_by = remittance.getPg_invoice_check_by();
String pg_invoice_check_nm = remittance.getPg_invoice_check_nm();
String pg_invoice_auth_flag = remittance.getPg_invoice_auth_flag();
String pg_invoice_auth_dt = remittance.getPg_invoice_auth_dt();
String pg_invoice_auth_by = remittance.getPg_invoice_auth_by();
String pg_invoice_auth_nm = remittance.getPg_invoice_auth_nm();
String pg_invoice_aprv_flag = remittance.getPg_invoice_aprv_flag();
String pg_invoice_aprv_dt = remittance.getPg_invoice_aprv_dt();
String pg_invoice_aprv_by = remittance.getPg_invoice_aprv_by();
String pg_invoice_aprv_nm = remittance.getPg_invoice_aprv_nm();
String pg_invoice_adj_sign = remittance.getPg_invoice_adj_sign();
String pg_invoice_adj_amt = remittance.getPg_invoice_adj_amt();
String pg_financial_year = remittance.getPg_financial_year();
String pg_tcs_factor = remittance.getPg_tcs_factor();
String pg_tcs_amount = remittance.getPg_tcs_amount();
String pg_tcs_struct_cd=remittance.getPg_tcs_struct_cd();
String pg_tcs_struct_dt=remittance.getPg_tcs_struct_dt();
String pg_tds_factor = remittance.getPg_tds_factor();
String pg_tds_amount = remittance.getPg_tds_amount();
String pg_tds_struct_cd=remittance.getPg_tds_struct_cd();
String pg_tds_struct_dt=remittance.getPg_tds_struct_dt();
String pg_cif_value=remittance.getPg_cif_value();
String pg_assessable_vlaue=remittance.getPg_assessable_vlaue();
String pg_remark=remittance.getPg_remark();
String pg_gross_include_transport_tariff=remittance.getPg_gross_include_transport_tariff();
String pg_transportation_amount=remittance.getPg_transportation_amount();
String pg_marketing_margin_amount=remittance.getPg_marketing_margin_amount();
String pg_other_charges_amount=remittance.getPg_other_charges_amount();

Vector VSEL_BU_CD = remittance.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = remittance.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_BU_PLANT_ABBR = remittance.getVSEL_BU_PLANT_ABBR();
Vector VCONTACT_PERSON = remittance.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = remittance.getVCONTACT_PERSON_CD();
Vector VBU_CONTACT_PERSON = remittance.getVBU_CONTACT_PERSON();
Vector VBU_CONTACT_PERSON_CD = remittance.getVBU_CONTACT_PERSON_CD();

boolean sg_submission_chk = remittance.getSg_submission_chk();
boolean pg_submission_chk = remittance.getPg_submission_chk();

Vector VDEAL_NO = remittance.getVDEAL_NO();
Vector VCONT_REF_NO = remittance.getVCONT_REF_NO();
Vector VINVOICE_AMT = remittance.getVINVOICE_AMT();
Vector VINVOICE_AMT_USD = remittance.getVINVOICE_AMT_USD();
Vector VINVOICE_DT = remittance.getVINVOICE_DT();
Vector VGROSS_AMT = remittance.getVGROSS_AMT();
Vector VGROSS_AMT_USD = remittance.getVGROSS_AMT_USD();

Vector VINVOICE_NO = remittance.getVINVOICE_NO();
String total_InvoiceAmt = remittance.getTotal_InvoiceAmt();
String total_GrossAmt = remittance.getTotal_GrossAmt();

Vector VSG_MULTI_TAX_STRUCT = remittance.getVSG_MULTI_TAX_STRUCT();
Vector VPG_MULTI_TAX_STRUCT = remittance.getVPG_MULTI_TAX_STRUCT();

String final_qty=remittance.getFinal_qty();
String final_price=remittance.getFinal_price();
String final_inv_amt=remittance.getFinal_inv_amt();
String profm_qty=remittance.getProfm_qty();
String profm_price=remittance.getProfm_price();
String profm_inv_amt=remittance.getProfm_inv_amt();
String diff_qty=remittance.getDiff_qty();
String diff_price=remittance.getDiff_price();
String diff_inv_amt=remittance.getDiff_inv_amt();

String cd_paid_amt=remittance.getCd_paid_amt();

String pg_diff_price=remittance.getPg_diff_price();
String pg_cd_paid_amt=remittance.getPg_cd_paid_amt();

String submitted_fiscal_yr = remittance.getSubmitted_fiscal_yr();

boolean isGrossIncTriff = remittance.getIsGrossIncTriff();
int len_multi_tax=0;

String inv_lineitem="Invoice";
String tax_lineitem="Tax";
String gross_lineitem="Gross";
String exchg_lineitem="Exchange Rate";
if(inv_flag.equals("CP") || inv_flag.equals("CF"))
{
	inv_lineitem="Custom Duty";	
	tax_lineitem="Custom Duty";
	gross_lineitem="Invoice";
	exchg_lineitem="Custom Exchange Rate";
}

String new_tds_amt = remittance.getNew_tds_amt();
String new_tds_factor =remittance.getNew_tds_factor();
String new_tds_struct_cd =remittance.getNew_tds_struct_cd();
String new_tds_struct_dt=remittance.getNew_tds_struct_dt();
String new_applicable_abbr = remittance.getNew_applicable_abbr();
%>
<body onload="enableSystemGen(true);enablePartyGen(false);difference();copyInvoiceNo();<%if(!msg.equals("")){%>refreshParent('<%=accroid%>');<%}%>"
	<%if(!activity_type.equals("PREPARE")) {%>style="pointer-events: none;"<%} %>
>

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
					    <%if(inv_flag.equals("PF") && contract_type.equals("N")){ %>
					    	Cargo Proforma
					    <%}else if((inv_flag.equals("P") || inv_flag.equals("CP")) && contract_type.equals("N")){ %>
					    	Cargo Provisional
					    <%}else if((inv_flag.equals("F") || inv_flag.equals("CF")) && contract_type.equals("N")){ %>
					    	Cargo Final	
					    <%}else if(contract_type.equals("G") || contract_type.equals("P")){ %>
					    	LTCORA Purchase	
					    <%}else{ %>
					    	Purchase 
					    <%} %>
					    
					    <%if(inv_flag.equals("CP") || inv_flag.equals("CF")){ %>
					    	Custom Duty
					    <%}%>
					     	Remittance Detail
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
											<td align="center"><%=deal_no%></td>
											<%if(contract_type.equals("N") && !cargo_no.equals("")){ %>
											<td align="center"><%=boe_nm%></td>
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
				<%if(inv_flag.equals("F") && contract_type.equals("N")) {%>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th></th>
											<th>Provisional Invoice</th>
											<th>Final Invoice (this)</th>
											<th>Difference</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><b>Quantity</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="" value="<%=profm_qty%>" readOnly>
														<span class="input-group-text"><%=energy_unit_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="" value="<%=final_qty%>" readOnly>
														<span class="input-group-text"><%=energy_unit_nm%></span>
													</div>
												</div>
											</td>
											<td><%=diff_qty%></td>
										</tr>
										<tr>
											<td><b>Price</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="" value="<%=profm_price%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%>/<%=energy_unit_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="" value="<%=price_cd%>">
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="" value="<%=final_price%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%>/<%=energy_unit_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="" value="<%=price_cd%>">
												</div>
											</td>
											<td><%=diff_price%></td>
										</tr>	
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>						
				<%}else if(inv_flag.equals("CF")){ %>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th></th>
											<th>Provisional Custom Duty</th>
											<th>Final/Provisional Invoice</th>
											<th>Difference</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><b>Quantity</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="" value="<%=profm_qty%>" readOnly>
														<span class="input-group-text"><%=energy_unit_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="" value="<%=final_qty%>" readOnly>
														<span class="input-group-text"><%=energy_unit_nm%></span>
													</div>
												</div>
											</td>
											<td><%=diff_qty%></td>
										</tr>
										<tr>
											<td><b>Price</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="" value="<%=profm_price%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%>/<%=energy_unit_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="" value="<%=price_cd%>">
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="" value="<%=final_price%>" readOnly >
														<span class="input-group-text"><%=price_cd_nm%>/<%=energy_unit_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="" value="<%=price_cd%>">
												</div>
											</td>
											<td><%=diff_price%></td>
										</tr>
										<tr>
											<td><b>Custom Duty</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="" value="<%=profm_inv_amt%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="" value="<%=price_cd%>">
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="" value="<%=final_inv_amt%>" readOnly >
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="" value="<%=price_cd%>">
												</div>
											</td>
											<td><%=diff_inv_amt%></td>
										</tr>	
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<%} %>
				<div class="card-body cdbody" <%if(contract_type.equals("N")) {%>style="display: none;"<%} %>>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<h5>
								<span class="badge bg-info text-dark">TCS/TDS Applicable : <%=applicable_abbr%></span>
								<%if(!contract_type.equals("G") && !contract_type.equals("P")) {%>
								&nbsp;&nbsp;<i class="fa fa-info-circle fa-lg" title="Click to View Invoice" style="color:#0000cc;" data-bs-toggle="modal" data-bs-target="#TcsInvDtl"></i>
								<%} %>
								<%if(applicable_abbr.equals("TDS")) {%>
									&nbsp;&nbsp;<span class="badge text-dark" style="background:orange;">SG : <%=tds_amount%> <%=invoice_raised_in_nm %></span>
									&nbsp;&nbsp;<span class="badge text-dark" style="background:skyblue;">PG : <%=pg_tds_amount%> <%=invoice_raised_in_nm %></span>
								<%} %>
							</h5>
						</div>
					</div>
				</div>
				<%if(!bu_unit.equals("0")){ %>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover" id="inv_difference">
									<thead>
										<tr <%if(!activity_type.equals("PREPARE") && !activity_type.equals("APPROVE")) {%>style="pointer-events: auto;"<%} %>>
											<th></th>
											<th>
												<span class="badge rounded-pill" style="background:orange;color:black;font-size:12px;">
													<input type="radio" name="invoice_type" value="S" checked onclick="doEnabled(this);">&nbsp;System Generated
												</span>
											</th>
											<th>
												<span class="badge rounded-pill" style="background:skyblue;color:black;font-size:12px;">
													<input type="radio" name="invoice_type" value="P" onclick="doEnabled(this);" <%if(!sg_submission_chk){ %>disabled<%} %>>&nbsp;Party Generated
												</span>
											</th>
											<th>
												Difference
											</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><b><%=inv_lineitem%>#<span class="s-red">*</span><font color="blue">(Party Generated)</font></b></td>
											<td align="center">
												<div style="width:250px;">
													<input type="text" class="form-control form-control-sm" name="sys_invoice_no" value="<%=invoice_no%>" onblur="copyInvoiceNo();" style="color:blue;">
													<input type="hidden" name="sys_invoice_seq" value="<%=invoice_seq%>">
													<input type="hidden" name="sys_financial_year" value="<%=financial_year%>">
													<input type="hidden" name="system_invoice_no" value="<%=sys_invoice_no%>">
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<input type="text" class="form-control form-control-sm" name="party_invoice_no" value="<%=pg_invoice_no%>" style="color:blue;" readOnly>
													<input type="hidden" name="temp_party_invoice_no" value="<%=pg_invoice_no%>">
													<input type="hidden" name="party_invoice_seq" value="<%=pg_invoice_seq%>">
													<input type="hidden" name="party_financial_year" value="<%=pg_financial_year%>">
													<input type="hidden" name="party_system_invoice_no" value="<%=pg_sys_invoice_no%>">
												</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b><%=inv_lineitem%> Date<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="sys_invoice_dt" value="<%=invoice_dt%>" maxLength="10" 
							      						onchange="validateDate(this);refresh1();" autocomplete="off"><!-- ; -->
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
					      						</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="party_invoice_dt" value="<%=pg_invoice_dt%>" maxLength="10" 
							      						onblur="validateDate(this);" 
							      						onchange="validateDate(this);" autocomplete="off">
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
						      					</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b><%=inv_lineitem%> Due Date<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="sys_invoice_due_dt" value="<%=invoice_due_dt%>" maxLength="10" 
							      						onchange="validateDate(this);" autocomplete="off"><!-- refresh(); -->
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
						      					</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="party_invoice_due_dt" value="<%=pg_invoice_due_dt%>" maxLength="10" 
							      						onblur="validateDate(this);" 
							      						onchange="validateDate(this);" autocomplete="off">
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
						      					</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b><%if(inv_flag.equals("PF") || inv_flag.equals("CP")) {%>Estimated
											<%}else if(contract_type.equals("N") && (inv_flag.equals("F") || inv_flag.equals("CF") || inv_flag.equals("P"))) {%>Unloaded
											<%}else{%>Allocation<%}%> Quantity</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_alloc_qty" value="<%=qty_mmbtu%>" readOnly>
														<span class="input-group-text"><%=energy_unit_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="sys_qty_unit" value="<%=energy_unit%>" readOnly>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_alloc_qty" value="<%=pg_qty_mmbtu%>"
														onkeyup=" partyGenCalc();difference();checkNumber1(this,12,2);">
														<span class="input-group-text"><%=energy_unit_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="party_qty_unit" value="<%=energy_unit%>" readOnly>
												</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b>Confirmed Price<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_price" value="<%=price%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%>/<%=energy_unit_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="sys_price_cd" value="<%=price_cd%>">
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_price" value="<%=pg_price%>" 
														onkeyup=" partyGenCalc();difference();<%if(price_cd.equals("1")){ %>checkNumber1(this,6,2);<%}else{ %>checkNumber1(this,6,4);<%}%>">
														<span class="input-group-text"><%=price_cd_nm%>/<%=energy_unit_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="party_price_cd" value="<%=pg_price_cd%>">
												</div>
											</td>
											<td></td>
										</tr>
										<tr <%if(contract_type.equals("N") && inv_flag.equals("F")) {%><%}else{ %>style="display:none;"<%} %>>
											<td><b>Diffrence in Price<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_diff_price" value="<%=diff_price%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%>/<%=energy_unit_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_diff_price" value="<%=pg_diff_price%>" 
														onkeyup=" partyGenCalc();difference();<%if(price_cd.equals("1")){ %>checkNumber1(this,6,2);<%}else{ %>checkNumber1(this,6,4);<%}%>">
														<span class="input-group-text"><%=price_cd_nm%>/<%=energy_unit_nm%></span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr <%if(!price_cd.equals(invoice_raised_in)){ %><%}else{ %>style="display:none;"<%} %>>
											<td><b>Gross Amount</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_gross_amt" value="<%=gross_amt%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_gross_amt" value="<%=pg_gross_amt%>" 
														onkeyup=" partyGenCalc();difference();" readOnly>
														<span class="input-group-text"><%=price_cd_nm%></span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr <%if(!price_cd.equals(invoice_raised_in)){ %><%}else{ %>style="display:none;"<%} %>>
											<td><b><%=exchg_lineitem%> <font color="blue">as on <%if(inv_flag.equals("CF")){%>Provisional<%}else{%><%=exchang_rate_dt%><%}%></font></b></td>
											<td align="center">
												<div style="width:250px;">
													<input type="text" class="form-control form-control-sm" name="sys_exchng_rate" value="<%=exchang_rate%>" readOnly>
													<input type="hidden" name="sys_exchng_cd" value="<%=exchng_rate_cd%>">
													<input type="hidden" name="sys_exchng_dt" value="<%=exchang_rate_dt%>"> 
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<input type="text" class="form-control form-control-sm" name="party_exchng_rate" value="<%=pg_exchang_rate%>" 
													onkeyup=" partyGenCalc();difference();">
													<input type="hidden" name="party_exchng_cd" value="<%=pg_exchng_rate_cd%>">
													<input type="hidden" name="party_exchng_dt" value="<%=pg_exchang_rate_dt%>">
												</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b><%=gross_lineitem%> Amount<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_gross_amt1" value="<%=gross_amt1%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="sys_invoice_raised_in" value="<%=invoice_raised_in%>">
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_gross_amt1" value="<%=pg_gross_amt1%>" 
														onkeyup=" partyGenCalc();difference();" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="party_invoice_raised_in" value="<%=pg_invoice_raised_in%>">
												</div>
											</td>
											<td></td>
										</tr>
										
										<tr <%if(transportation_charges.equals("")){ %>style="display:none;"<%} %>>
											<td><b>Transportation Tariff (<%=transportation_charges%> INR/MMBTU)<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_transportation_amount" value="<%=transportation_amount%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="sys_transportation_tariff" value="<%=transportation_charges%>">
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_transportation_amount" value="<%=pg_transportation_amount%>" 
														onkeyup=" partyGenCalc();difference();" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="party_transportation_tariff" value="<%=transportation_charges%>">
												</div>
											</td>
											<td></td>
										</tr>
										<tr <%if(marketing_margin.equals("")){ %>style="display:none;"<%} %>>
											<td><b>Marketing Margin (<%=marketing_margin%> INR/MMBTU)<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_marketing_margin_amount" value="<%=marketing_margin_amount%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="sys_marketing_margin" value="<%=marketing_margin%>">
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_marketing_margin_amount" value="<%=pg_marketing_margin_amount%>" 
														onkeyup=" partyGenCalc();difference();" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="party_marketing_margin" value="<%=marketing_margin%>">
												</div>
											</td>
											<td></td>
										</tr>
										<tr <%if(other_charges.equals("")){ %>style="display:none;"<%} %>>
											<td><b>Other Charges (<%=other_charges%> INR/MMBTU)<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_other_charges_amount" value="<%=other_charges_amount%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="sys_other_charges" value="<%=other_charges%>">
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_other_charges_amount" value="<%=pg_other_charges_amount%>" 
														onkeyup=" partyGenCalc();difference();" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="party_other_charges" value="<%=other_charges%>">
												</div>
											</td>
											<td></td>
										</tr>
										<tr <%if(!isGrossIncTriff){ %>style="display:none;"<%} %>>
											<td><b>Total Gross Amount<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_gross_include_transport_tariff" value="<%=gross_include_transport_tariff%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_gross_include_transport_tariff" value="<%=pg_gross_include_transport_tariff%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										
										<tr <%if(!inv_flag.equals("CP") && !inv_flag.equals("CF")) {%> style="display:none;"<%} %>>
											<td><b>CIF Value of Cargo</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_cif_amt" value="<%=cif_value%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_cif_amt" value="<%=pg_cif_value%>" 
														onkeyup=" partyGenCalc();difference();" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr <%if(!inv_flag.equals("CP") && !inv_flag.equals("CF")) {%> style="display:none;"<%} %>>
											<td><b>Assessable Value</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_assessable_amt" value="<%=assessable_vlaue%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_assessable_amt" value="<%=pg_assessable_vlaue%>" 
														onkeyup=" partyGenCalc();difference();" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr <%if(contract_type.equals("N") && (inv_flag.equals("PF") || inv_flag.equals("P") || inv_flag.equals("F"))){ %>style="display: none;"<%} %>>
											<td><b><%=tax_lineitem%> (<%=tax_struct_dtl%>)</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_tax_amt" value="<%=tax_amt%>" title="<%=tax_info%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" name="sys_tax_cd" value="<%=tax_struct_cd%>">
													<input type="hidden" name="sys_tax_dt" value="<%=tax_struct_dt%>">
													<input type="hidden" name="sys_tax_factor" value="<%=tax_factor%>">
													<input type="hidden" name="sys_tax_struct_dtl" value="<%=tax_struct_dtl%>">
												</div>
											</td>
											<td align="center">	
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_tax_amt" value="<%=pg_tax_amt%>" readOnly
														onkeyup=" partyGenCalc();difference();checkNumber1(this,14,2);">
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" name="party_tax_cd" value="<%=pg_tax_struct_cd%>">
													<input type="hidden" name="party_tax_dt" value="<%=pg_tax_struct_dt%>">
													<input type="hidden" name="party_tax_struct_dtl" value="<%=tax_struct_dtl%>">
												</div>
											</td>
											<td></td>
										</tr>
										<%if(VSG_MULTI_TAX_STRUCT.size()>0){
											for(int i=0;i<VSG_MULTI_TAX_STRUCT.size();i++)
											{
												Vector temp =(Vector)((Vector)((Vector)VSG_MULTI_TAX_STRUCT.elementAt(i)));
												Vector temp_pg =(Vector)((Vector)((Vector)VPG_MULTI_TAX_STRUCT.elementAt(i)));
												
												len_multi_tax=((Vector) temp.elementAt(0)).size();
												for(int j=0;j<((Vector) temp.elementAt(0)).size(); j++)
												{%>
													<tr style="background: #cff4fc;<%if(((Vector) temp.elementAt(0)).size()==1){%>display:none;<%} %>" >
														<td align="right"><b><%=((Vector) temp.elementAt(1)).elementAt(j)%></b></td>
														<td align="center">
															<div style="width:250px;">
																<div class="input-group input-group-sm" >
																	<input type="text" class="form-control form-control-sm" name="sys_sub_tax_amt" 
																		value="<%=((Vector) temp.elementAt(2)).elementAt(j)%>" readOnly>
																	<span class="input-group-text"><%=invoice_raised_in_nm%></span>
																</div>
																<input type="hidden" name="sys_sub_tax_code" value="<%=((Vector) temp.elementAt(0)).elementAt(j)%>">
																<input type="hidden" name="sys_sub_tax_struct" value="<%=((Vector) temp.elementAt(1)).elementAt(j)%>">
																<input type="hidden" name="sys_sub_tax_base_amt" value="<%=((Vector) temp.elementAt(3)).elementAt(j)%>">
															</div>
														</td>
														<td align="center">
															<div style="width:250px;">
																<div class="input-group input-group-sm" >
																	<input type="text" class="form-control form-control-sm" name="party_sub_tax_amt" 
																		value="<%=((Vector) temp_pg.elementAt(2)).elementAt(j)%>" onkeyup="partyGenCalc();difference();checkNumber1(this,14,2);" readOnly>
																	<span class="input-group-text"><%=invoice_raised_in_nm%></span>
																</div>
																<input type="hidden" name="party_sub_tax_code" value="<%=((Vector) temp_pg.elementAt(0)).elementAt(j)%>">
																<input type="hidden" name="party_sub_tax_struct" value="<%=((Vector) temp_pg.elementAt(1)).elementAt(j)%>">
																<input type="hidden" name="party_sub_tax_base_amt" value="<%=((Vector) temp_pg.elementAt(3)).elementAt(j)%>">
															</div>
														</td>
														<td></td>
													</tr>
												<%}
											}
										} %>
										<tr <%if(!inv_flag.equals("CF")) {%> style="display:none;"<%} %>>
											<td><b>Custom Duty Paid</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_cd_paid_amt" value="<%=cd_paid_amt%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_cd_paid_amt" value="<%=pg_cd_paid_amt%>" 
														onkeyup=" partyGenCalc();difference();" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td title="Gross Amount + Tax Amount"><b><%=inv_lineitem%> Amount<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_invoice_amt" value="<%=invoice_amt%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_invoice_amt" value="<%=pg_invoice_amt%>" readOnly 
														onkeyup=" partyGenCalc();difference();checkNumber1(this,14,2);">
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr <%if(!applicable_flag.equals("Y")){ %>style="display:none;"<%} %>>
											<td><b>TCS(<%=tcs_factor%>%)</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_tcs_amt" value="<%=tcs_amount%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" name="sys_tcs_factor" value="<%=tcs_factor%>" readOnly>
													<input type="hidden" name="sys_tcs_cd" value="<%=tcs_struct_cd%>">
													<input type="hidden" name="sys_tcs_dt" value="<%=tcs_struct_dt%>">
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_tcs_amt" value="<%=pg_tcs_amount%>" readOnly 
														onkeyup="partyGenCalc();difference();checkNumber1(this,10,2);">
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" name="party_tcs_factor" value="<%=pg_tcs_factor%>" readOnly>
													<input type="hidden" name="party_tcs_cd" value="<%=pg_tcs_struct_cd%>">
													<input type="hidden" name="party_tcs_dt" value="<%=pg_tcs_struct_dt%>">
												</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b>Adjustment</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="row">
														<div class="col-auto">
															<select class="form-select form-select-sm" name="sys_adj_plusmin">
																<option value="">-Select-</option>
																<option value="+">+</option>
																<option value="-">-</option>
															</select>
															<script>document.forms[0].sys_adj_plusmin.value="<%=invoice_adj_sign%>"</script>
														</div>
														<div class="col">
															<div class="input-group input-group-sm" >
																<input type="text" class="form-control form-control-sm" name="sys_adj_amt" value="<%=invoice_adj_amt%>" readOnly>
																<span class="input-group-text"><%=invoice_raised_in_nm%></span>
															</div>
														</div>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="row">
														<div class="col-auto">
															<select class="form-select form-select-sm" name="party_adj_plusmin">
																<option value="">-Select-</option>
																<option value="+">+</option>
																<option value="-">-</option>
															</select>
															<script>document.forms[0].party_adj_plusmin.value="<%=pg_invoice_adj_sign%>"</script>
														</div>
														<div class="col">
															<div class="input-group input-group-sm" >
																<input type="text" class="form-control form-control-sm" name="party_adj_amt" value="<%=pg_invoice_adj_amt%>" readOnly 
																onkeyup=" partyGenCalc();difference();checkNumber1(this,12,2);">
																<span class="input-group-text"><%=invoice_raised_in_nm%></span>
															</div>
														</div>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b>Net Payable</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_net_payable" value="<%=net_payable%>" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_net_payable" value="<%=pg_net_payable%>" readOnly 
														onkeyup=" partyGenCalc();difference();checkNumber1(this,14,2);">
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										
										<tr>
											<td><b>Remark</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<textarea class="form-control form-control-sm" rows="2" cols="125" name="sys_remark"><%=remark%></textarea>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<textarea class="form-control form-control-sm" rows="2" cols="125" name="party_remark" ><%=pg_remark%></textarea>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										
										<tr style="<%if(!activity_type.equals("PREPARE")){%>display:none;<%} %>">
											<td><b>View/Submit <%=inv_lineitem%></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="d-flex justify-content-between">
														<i class="fa fa-refresh fa-2x" style="color:blue;" onclick="refresh1();" id="sys_ref_icon"></i>
														<i class="fa fa-eye fa-2x" id="sys_view" onclick="viewBeforeSubmit('S');"></i>
														<%if(!invoice_no.equals("") && !accroid.equals("PROFRM_HEAD")) {%>
														<i class="fa fa-file-code-o fa-2x" 
														onclick="doGenXML('<%=counterparty_cd%>','<%=invoice_no%>','<%=financial_year%>','<%=invoice_seq%>','<%=contract_type%>',
														 'S','','N','<%=bu_unit%>','<%=cont_no%>','N','<%=accroid%>','<%=inv_flag%>','<%=cargo_no%>','<%=boe_no%>','VIEW');"
														style="color: orange;">													
														</i>
														<%} %>
														<%if(write_access.equals("Y")){ %>
														<input type="button" class="btn btn-warning com-btn" id="sys_submit" value="Submit" onclick="doSubmit('S');">
														<%}else{ %>
														<input type="button" class="btn btn-warning com-btn" id="sys_submit" value="Submit" disabled>
														<%} %>
														<!-- <input type="button" class="btn btn-warning com-btn" id="sys_view" value="View" onclick="printPDF('S');"> -->
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="d-flex justify-content-between">
														<i class="fa fa-eye fa-2x" id="party_view" onclick="viewBeforeSubmit('P');"></i>
														<%if(!pg_invoice_no.equals("") && !accroid.equals("PROFRM_HEAD")) {%>
														<i class="fa fa-file-code-o fa-2x" 
														onclick="doGenXML('<%=counterparty_cd%>','<%=pg_invoice_no%>','<%=financial_year%>','<%=invoice_seq%>','<%=contract_type%>',
														 'P','','N','<%=bu_unit%>','<%=cont_no%>','N','<%=accroid%>','<%=inv_flag%>','<%=cargo_no%>','<%=boe_no%>','VIEW');"
														style="color: orange;">													
														</i>
														<%} %>
														<%if(write_access.equals("Y")){ %>
														<input type="button" class="btn btn-warning com-btn" id="party_submit" value="Submit" onclick="doSubmit('P');">
														<%}else{ %>
														<input type="button" class="btn btn-warning com-btn" id="party_submit" value="Submit" disabled>
														<%} %>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr style="<%if(activity_type.equals("PREPARE")){%>display:none;<%}else{ %>pointer-events: auto;<%}%>">
											<td><b>View <%=inv_lineitem%></b></td>
											<td align="center">
												<div style="width:125px;">
													<div class="d-flex justify-content-between">
														<i class="fa fa-eye fa-2x" onclick="viewBeforeSubmit('S');"></i>
														<%if(!invoice_no.equals("") && !accroid.equals("PROFRM_HEAD")) {%>
														<i class="fa fa-file-code-o fa-2x" 
														onclick="doGenXML('<%=counterparty_cd%>','<%=invoice_no%>','<%=financial_year%>','<%=invoice_seq%>','<%=contract_type%>',
														 'S','','N','<%=bu_unit%>','<%=cont_no%>','N','<%=accroid%>','<%=inv_flag%>','<%=cargo_no%>','<%=boe_no%>','VIEW');"
														style="color: orange;">													
														</i>
														<%} %>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:125px;">
													<div class="d-flex justify-content-between">
														<i class="fa fa-eye fa-2x" onclick="viewBeforeSubmit('P');"></i>
														<%if(!pg_invoice_no.equals("") && !accroid.equals("PROFRM_HEAD")) {%>
														<i class="fa fa-file-code-o fa-2x" 
														onclick="doGenXML('<%=counterparty_cd%>','<%=pg_invoice_no%>','<%=financial_year%>','<%=invoice_seq%>','<%=contract_type%>',
														 'P','','N','<%=bu_unit%>','<%=cont_no%>','N','<%=accroid%>','<%=inv_flag%>','<%=cargo_no%>','<%=boe_no%>','VIEW');"
														style="color: orange;">													
														</i>
														<%} %>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr style="<%if(!activity_type.equals("CHECK")){%>display:none;<%}else{ %>pointer-events: auto;<%}%>">
											<td><b>Check <%=inv_lineitem%></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="form-group row">
														<div class="col-auto"> 
															<span class="pillbox yesRdBtn">	
																<input type="radio" name="sys_chk" value="Y" <%if(invoice_check_flag.equals("Y")){%>checked<%} %>>&nbsp;Yes
															</span>
														</div>
														<div class="col-auto">
															<span class="pillbox noRdBtn">	
																<input type="radio" name="sys_chk" value="N" <%if(invoice_check_flag.equals("N")){%>checked<%} %>>&nbsp;No
															</span>	
														</div>
														<div class="col-auto">
															<input type="button" class="btn btn-warning com-btn" id="sys_chk_sub" value="Submit" onclick="checkInvoice('S');">
														</div>
													</div>
												</div>
												<%if(!invoice_check_flag.equals("")){ %>
													<font color="<%if(invoice_check_flag.equals("Y")){ %>green<%}else{%>red<%}%>"><%=invoice_check_dt%> <%=invoice_check_nm%> by <%=invoice_check_by%></font>
												<%} %>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="form-group row">
														<div class="col-auto"> 
															<span class="pillbox yesRdBtn">	
																<input type="radio" name="party_chk" value="Y" <%if(pg_invoice_check_flag.equals("Y")){%>checked<%} %>>&nbsp;Yes
															</span>
														</div>
														<div class="col-auto">
															<span class="pillbox noRdBtn">	
																<input type="radio" name="party_chk" value="N" <%if(pg_invoice_check_flag.equals("N")){%>checked<%} %>>&nbsp;No
															</span>	
														</div>
														<div class="col-auto">
															<input type="button" class="btn btn-warning com-btn" id="party_chk_sub" value="Submit" onclick="checkInvoice('P');">
														</div>
													</div>
												</div>
												<%if(!pg_invoice_check_flag.equals("")){ %>
													<font color="<%if(pg_invoice_check_flag.equals("Y")){ %>green<%}else{%>red<%}%>"><%=pg_invoice_check_dt%> <%=pg_invoice_check_nm%> by <%=pg_invoice_check_by%></font>
												<%} %>
											</td>
											<td></td>
										</tr>
										<tr style="<%if(!activity_type.equals("AUTHORIZE")){%>display:none;<%}else{ %>pointer-events: auto;<%}%>">
											<td><b>Authorize <%=inv_lineitem%></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="form-group row">
														<div class="col-auto"> 
															<span class="pillbox yesRdBtn">	
																<input type="radio" name="sys_auth" value="Y" <%if(invoice_auth_flag.equals("Y")){%>checked<%} %>>&nbsp;Yes
															</span>
														</div>
														<div class="col-auto">
															<span class="pillbox noRdBtn">	
																<input type="radio" name="sys_auth" value="N" <%if(invoice_auth_flag.equals("N")){%>checked<%} %>>&nbsp;No
															</span>	
														</div>
														<div class="col-auto">
															<input type="button" class="btn btn-warning com-btn" id="sys_auth_sub" value="Submit" onclick="authorizeInvoice('S');" <%if(invoice_aprv_flag.equals("A") || pg_invoice_aprv_flag.equals("A")){ %>disabled<%} %>>
														</div>
													</div>
												</div>
												<%if(!invoice_auth_flag.equals("")){ %>
													<font color="<%if(invoice_auth_flag.equals("Y")){ %>green<%}else{%>red<%}%>"><%=invoice_auth_dt%> <%=invoice_auth_nm%> by <%=invoice_auth_by%></font>
												<%} %>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="form-group row">
														<div class="col-auto"> 
															<span class="pillbox yesRdBtn">
																<input type="radio" name="party_auth" value="Y" <%if(pg_invoice_auth_flag.equals("Y")){%>checked<%} %>>&nbsp;Yes
															</span>
														</div>
														<div class="col-auto">
															<span class="pillbox noRdBtn">
																<input type="radio" name="party_auth" value="N" <%if(pg_invoice_auth_flag.equals("N")){%>checked<%} %>>&nbsp;No
															</span>
														</div>
														<div class="col-auto">
															<input type="button" class="btn btn-warning com-btn" id="party_auth_sub" value="Submit" onclick="authorizeInvoice('P');" <%if(invoice_aprv_flag.equals("A") || pg_invoice_aprv_flag.equals("A")){ %>disabled<%} %>>
														</div>
													</div>
												</div>
												<%if(!pg_invoice_auth_flag.equals("")){ %>
													<font color="<%if(pg_invoice_auth_flag.equals("Y")){ %>green<%}else{%>red<%}%>"><%=pg_invoice_auth_dt%> <%=pg_invoice_auth_nm%> by <%=pg_invoice_auth_by%></font>
												<%} %>
											</td>
											<td></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div style="<%if(!activity_type.equals("APPROVE")){%>display:none<%}else{ %>pointer-events: auto;<%}%>">
					&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Final Approval</label>
					</div>
					<%
					String tds_amt="";
					if(pg_tds_amount!="")
					{
						tds_amt=pg_tds_amount;
					}
					else
					{
						tds_amt=tds_amount;
					}
					
					String activity_value="";
					if(pg_invoice_aprv_flag=="Y")
					{
						activity_value=pg_invoice_aprv_flag;
					}
					else
					{
						activity_value=invoice_aprv_flag;
					}
					
					%>
					<div align="center">
					<%if(new_applicable_abbr.equals("TDS")) {%>
						<table border="1" width="75%" align="center" cellpadding="2" cellspacing="0">
							<tr valign="middle" <%if(activity_value.equals("A")) {%>style="display: none;"<%} %>>
								<td>
									<div align="center">
										<b>TDS/TCS Applicable : <%=new_applicable_abbr%><%if(new_tds_struct_cd.equals("")) {%><br/><br/><font color="red">(TDS is applicable for this Invoice. Please configure TDS factor and re-try Approval process)</font><%} %></b><br/><br/>
										TDS Recalculated : <b><%=new_tds_amt%> INR</b> (Submitted TDS Value : <b><%=tds_amt%> INR</b>)
										<br/><br/>
										Refine TDS : <input type="radio" name="tds_rd" value="Y" onClick="enableTDS(this);">&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="radio" name="tds_rd" value="N" onClick="enableTDS(this);" checked>&nbsp;<b>No</b>
										<br/><br/>
										<div style="display: none;" id="tds_input_div">
											Enter Gross Amount : <input type="text" name="tds_gross_amt" id="tds_gross_amt" onblur="calcTDS();" maxlength="13">&nbsp;&nbsp;&nbsp;
											Calculated TDS :<input type="text" name="recalc_tds_amt" id="recalc_tds_amt" style="background: #e9ecef;" readOnly>
											<br/><br/>
										</div>
										On Approval TDS value will be updated as <font id="upt_val_id"><%=new_tds_amt%></font> INR
									</div>
								</td>
							</tr>
							<tr valign="middle" <%if(!activity_value.equals("A")) {%>style="display: none;"<%} %>>
								<td>
									<div align="center">
										<b>TDS/TCS Applicable : <%=applicable_abbr%></b><br/><br/>
										Submitted TDS Value : <b><%=tds_amt%> INR</b>
									</div>
								</td>
							</tr>
						</table>
					<%}else{ %>
						<table border="1" width="75%" align="center" cellpadding="2" cellspacing="0">
							<tr valign="middle">
								<td>
									<div align="center"><b>TDS/TCS Applicable : <%=new_applicable_abbr%></b></div>
								</td>
							</tr>
						</table>
					<%} %>
					</div>&nbsp;
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Final Approve For</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="radio" name="final_aprv" value="S" <%if(!invoice_auth_flag.equals("Y")){ %>disabled<%} %> <%if(!invoice_aprv_flag.equals("")){ %>checked<%} %>>&nbsp;System Generated&nbsp;&nbsp;&nbsp;
				    				<input type="radio" name="final_aprv" value="P" <%if(!pg_invoice_auth_flag.equals("Y")){ %>disabled<%} %> <%if(!pg_invoice_aprv_flag.equals("")){ %>checked<%} %>>&nbsp;Party Generated
				      				<br>
				      				<%if(!pg_invoice_aprv_flag.equals("")){ %>
										<font color="<%if(pg_invoice_aprv_flag.equals("A")){ %>green<%}else{%>red<%}%>"><%=pg_invoice_aprv_dt%> <%=pg_invoice_aprv_nm%> by <%=pg_invoice_aprv_by%></font>
									<%} %>
									<%if(!invoice_aprv_flag.equals("")){ %>
										<font color="<%if(invoice_aprv_flag.equals("A")){ %>green<%}else{%>red<%}%>"><%=invoice_aprv_dt%> <%=invoice_aprv_nm%> by <%=invoice_aprv_by%></font>
									<%} %>
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-6 col-xs-6 col-md-6">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12"> 
				    				<input type="button" class="btn btn-warning com-btn" value="Approve" onclick="InvApproval('A');" <%if(!invoice_auth_flag.equals("Y") && !pg_invoice_auth_flag.equals("Y")){%>disabled<%}%>>
				    				<input type="button" class="btn btn-warning com-btn" value="Reject" onclick="InvApproval('R');" <%if(!invoice_auth_flag.equals("Y") && !pg_invoice_auth_flag.equals("Y")){%>disabled<%}%>>
				    			</div>
				    		</div>
				    	</div>
					</div>
					</div>
				</div>
				<div class="modal-footer cdfooter">
	        		<!-- <div class="" align="right">
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="">
					</div> -->
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
											<th colspan="5">Customer Name : <%=couterpty_nm%></th>
											<th colspan="4">Financial Year : <%=financial_year%></th>
										</tr>
										<tr>
											<th rowspan="2">Sr#</th>
											<th rowspan="2">Contract#</th>
											<th rowspan="2">Contract Ref#</th>
											<th rowspan="2">Invoice#</th>
											<th rowspan="2">Invoice Date</th>
											<th colspan="2">Gross Amount</th>
											<th colspan="2">Invoice Amount</th>
										</tr>
										<tr>
											<th>INR</th>
											<th>USD</th>
											<th>INR</th>
											<th>USD</th>
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
											<td align="right"><%=VGROSS_AMT.elementAt(i)%></td>
											<td align="right"><%=VGROSS_AMT_USD.elementAt(i)%></td>
											<td align="right"><%=VINVOICE_AMT.elementAt(i)%></td>
											<td align="right"><%=VINVOICE_AMT_USD.elementAt(i) %></td>
										</tr>
										<%} %>
										<tr>
											<td align="right" colspan="5"><b>Total :</b></td>
											<td align="right"><b><%=total_GrossAmt%></b></td>
											<td align="right"></td>
											<td align="right"><b><%=total_InvoiceAmt%></b></td>
											<td align="right"></td>
										</tr>
									<%}else{ %>
										<tr>
											<td align="center" colspan="9"><%=utilmsg.infoMessage("<b>No Invoice!</b>")%></td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<%String tcsYesNo="No";
							if(applicable_flag.equals("Y"))
							{
								tcsYesNo="Yes";
							}%>
							<%=utilmsg.infoMessage("Financial Year: <b>"+financial_year+"</b>: TCS u/s 206C(1M) of IT ACT: <b>"+tcsYesNo+"</b>") %>
						</div>
					</div>
      			</div>
      		</div>
      	</div>
	</div>
</div>

<input type="hidden" name="option" value="TRADER_INVOICE">
<input type="hidden" name="opration" value="INSERT">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" value="<%=agmt_rev%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev" value="<%=cont_rev%>">
<input type="hidden" name="cargo_no" value="<%=cargo_no%>">
<input type="hidden" name="boe_no" value="<%=boe_no%>">
<input type="hidden" name="boe_nm" value="<%=boe_nm%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="plant_seq" value="<%=plant_seq%>">
<input type="hidden" name="period_start_dt" value="<%=period_start_dt%>">
<input type="hidden" name="period_end_dt" value="<%=period_end_dt%>">
<input type="hidden" name="billing_cycle" value="<%=billing_cycle%>">
<input type="hidden" name="bu_unit" value="<%=bu_unit%>">
<input type="hidden" name="inv_flag" value="<%=inv_flag%>">
<input type="hidden" name="contRef" value="<%=contRef%>">

<input type="hidden" name="system_chk_flag" value="<%=invoice_check_flag%>">
<input type="hidden" name="system_auth_flag" value="<%=invoice_auth_flag%>">

<input type="hidden" name="party_chk_flag" value="<%=pg_invoice_check_flag%>">
<input type="hidden" name="party_auth_flag" value="<%=pg_invoice_auth_flag%>">

<input type="hidden" name="inv_aprv_flag" value="">

<input type="hidden" name="sg_submission_chk" value="<%=sg_submission_chk%>">
<input type="hidden" name="pg_submission_chk" value="<%=pg_submission_chk%>">

<input type="hidden" name="activity_type" value="<%=activity_type%>">

<input type="hidden" name="invoice_aprv_flag" value="<%=invoice_aprv_flag%>">
<input type="hidden" name="pg_invoice_aprv_flag" value="<%=pg_invoice_aprv_flag%>">

<input type="hidden" name="qty_mmbtu" value="<%=qty_mmbtu%>">

<input type="hidden" name="applicable_abbr" value="<%=applicable_abbr%>">
<input type="hidden" name="applicable_flag" value="<%=applicable_flag%>">

<input type="hidden" name="sys_tds_amt" value="<%=tds_amount%>">
<input type="hidden" name="sys_tds_factor" value="<%=tds_factor%>">
<input type="hidden" name="sys_tds_cd" value="<%=tds_struct_cd%>">
<input type="hidden" name="sys_tds_dt" value="<%=tds_struct_dt%>">

<input type="hidden" name="party_tds_amt" value="<%=pg_tds_amount%>">
<input type="hidden" name="party_tds_factor" value="<%=pg_tds_factor%>">
<input type="hidden" name="party_tds_cd" value="<%=pg_tds_struct_cd%>">
<input type="hidden" name="party_tds_dt" value="<%=pg_tds_struct_dt%>">

<input type="hidden" name="new_tds_amt" value="<%=new_tds_amt%>">
<input type="hidden" name="new_tds_factor" value="<%=new_tds_factor%>">
<input type="hidden" name="new_tds_struct_cd" value="<%=new_tds_struct_cd%>">
<input type="hidden" name="new_applicable_abbr" value="<%=new_applicable_abbr%>">

<input type="hidden" name="final_tds_amt" value="<%=new_tds_amt%>">
<input type="hidden" name="isTDSalrted" value="<%if(!new_tds_amt.equals(tds_amount) && !new_tds_amt.equals("")){%>Y<%}%>">

<input type="hidden" name="isGrossIncTriff" value="<%=isGrossIncTriff%>">
<input type="hidden" name="len_multi_tax" value="<%=len_multi_tax%>">

<input type="hidden" name="submitted_fiscal_yr" value="<%=submitted_fiscal_yr%>">
<input type="hidden" name="accroid" value="<%=accroid%>">

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