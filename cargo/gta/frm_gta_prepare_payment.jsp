<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh() //NOT IN USE
{
	var bu_unit = document.forms[0].bu_unit.value;
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev = document.forms[0].cont_rev.value;
	var contract_type = document.forms[0].contract_type.value;
	var trans_bu_seq = document.forms[0].trans_bu_seq.value;
	var period_start_dt = document.forms[0].period_start_dt.value;
	var period_end_dt = document.forms[0].period_end_dt.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	
	var activity_type = document.forms[0].activity_type.value;
	var financial_year = document.forms[0].financial_year.value;
	var invoice_seq = document.forms[0].invoice_seq.value;
	var inv_component = document.forms[0].inv_component.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gta_prepare_payment.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
		"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&trans_bu_seq="+trans_bu_seq+"&period_start_dt="+period_start_dt+
		"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&activity_type="+activity_type+
		"&financial_year="+financial_year+"&invoice_seq="+invoice_seq+"&inv_comp="+inv_component+
		"&u="+u;
	
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
	var contract_type = document.forms[0].contract_type.value;
	var trans_bu_seq = document.forms[0].trans_bu_seq.value;
	var period_start_dt = document.forms[0].period_start_dt.value;
	var period_end_dt = document.forms[0].period_end_dt.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	var invoice_type = document.forms[0].invoice_type.value;
	var qty = document.forms[0].qty.value;
	
	var activity_type = document.forms[0].activity_type.value;
	var financial_year = document.forms[0].financial_year.value;
	var invoice_seq = document.forms[0].invoice_seq.value;
	var inv_component = document.forms[0].inv_component.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gta_prepare_payment.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
		"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&trans_bu_seq="+trans_bu_seq+"&period_start_dt="+period_start_dt+
		"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&refresh_flg=Y&activity_type="+activity_type+
		"&invoice_type="+invoice_type+"&qty_mmbtu="+qty+"&financial_year="+financial_year+"&invoice_seq="+invoice_seq+"&inv_comp="+inv_component+
		"&u="+u;
	
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
	
	var sys_ref_icon = document.getElementById("sys_ref_icon")
	var sys_view = document.getElementById("sys_view")
	var sys_submit = document.getElementById("sys_submit")
	var sys_chk_sub = document.getElementById("sys_chk_sub")
	var sys_auth_sub = document.getElementById("sys_auth_sub")
	
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
			sys_submit.disabled=false;
			sys_ref_icon.style.pointerEvents = "auto";
			//sys_auth_sub.disabled=false;
			
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
	
	var party_sub_tax_amt = document.forms[0].party_sub_tax_amt
	
	var party_deficiency_qty = document.forms[0].party_deficiency_qty
	var party_ship_pay_rate = document.forms[0].party_ship_pay_rate
	var party_neg_imb_qty = document.forms[0].party_neg_imb_qty
	var party_neg_imb_rate = document.forms[0].party_neg_imb_rate
	var party_pos_imb_qty = document.forms[0].party_pos_imb_qty
	var party_pos_imb_rate = document.forms[0].party_pos_imb_rate
	var party_unauth_overrun_qty = document.forms[0].party_unauth_overrun_qty
	var party_unauth_overrun_rate = document.forms[0].party_unauth_overrun_rate
	
	var party_view = document.getElementById("party_view")
	var party_submit = document.getElementById("party_submit")
	var party_chk_sub = document.getElementById("party_chk_sub")
	var party_auth_sub = document.getElementById("party_auth_sub")
	
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
			
			party_deficiency_qty.readOnly=false;
			party_ship_pay_rate.readOnly=false;
			party_neg_imb_qty.readOnly=false;
			party_neg_imb_rate.readOnly=false;
			party_pos_imb_qty.readOnly=false;
			party_pos_imb_rate.readOnly=false;
			party_unauth_overrun_qty.readOnly=false;
			party_unauth_overrun_rate.readOnly=false;
			
			if(activity_type=="PREPARE")
			{
				party_invoice_dt.style.pointerEvents = "auto";
				party_invoice_due_dt.style.pointerEvents = "auto";
			}
			if(party_gross_amt!=null && party_gross_amt!=undefined)
			{
				//party_gross_amt.readOnly=false;
				party_exchng_rate.readOnly=false;
			}
			party_gross_amt1.readOnly=false;
			party_tax_amt.readOnly=false;
				party_invoice_amt.readOnly=false;
			party_adj_plusmin.style.pointerEvents = "auto";
			party_adj_amt.readOnly=false;
				party_net_payable.readOnly=false;
			
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
			party_submit.disabled=false;
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
			
			party_deficiency_qty.readOnly=true;
			party_ship_pay_rate.readOnly=true;
			party_neg_imb_qty.readOnly=true;
			party_neg_imb_rate.readOnly=true;
			party_pos_imb_qty.readOnly=true;
			party_pos_imb_rate.readOnly=true;
			party_unauth_overrun_qty.readOnly=true;
			party_unauth_overrun_rate.readOnly=true;
			if(party_gross_amt!=null && party_gross_amt!=undefined)
			{
				//party_gross_amt.readOnly=true;
				party_exchng_rate.readOnly=true;
			}
			party_gross_amt1.readOnly=true;
			party_tax_amt.readOnly=true;
			party_invoice_amt.readOnly=true;
			party_adj_plusmin.style.pointerEvents = "none";
			party_adj_amt.readOnly=true;
			party_net_payable.readOnly=true;
			
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

function doSubmit(inv_flg)
{
	//var contact_person = document.forms[0].contact_person.value
	var bu_contact_person = document.forms[0].bu_contact_person.value
	var invoice_type = document.forms[0].invoice_type.value
	var inv_component = document.forms[0].inv_component.value;
	
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
	var sys_neg_imb_qty = document.forms[0].sys_neg_imb_qty.value
	var sys_neg_imb_rate = document.forms[0].sys_neg_imb_rate.value
	var sys_pos_imb_qty = document.forms[0].sys_pos_imb_qty.value
	var sys_pos_imb_rate = document.forms[0].sys_pos_imb_rate.value
	var sys_unauth_overrun_qty = document.forms[0].sys_unauth_overrun_qty.value
	var sys_unauth_overrun_rate = document.forms[0].sys_unauth_overrun_rate.value
	var sys_tds_amt = document.forms[0].sys_tds_amt.value
	var sys_tds_factor = document.forms[0].sys_tds_factor.value
	
	var party_invoice_no = document.forms[0].party_invoice_no.value
	var party_invoice_dt = document.forms[0].party_invoice_dt.value
	var party_invoice_due_dt = document.forms[0].party_invoice_due_dt.value
	var party_alloc_qty = document.forms[0].party_alloc_qty.value
	var party_price = document.forms[0].party_price.value
	//var party_gross_amt = document.forms[0].party_gross_amt.value
	//var party_exchng_rate = document.forms[0].party_exchng_rate.value
	var party_gross_amt1 = document.forms[0].party_gross_amt1.value
	var party_tax_amt = document.forms[0].party_tax_amt.value
	var party_invoice_amt = document.forms[0].party_invoice_amt.value
	var party_adj_plusmin = document.forms[0].party_adj_plusmin.value
	var party_adj_amt = document.forms[0].party_adj_amt.value
	var party_net_payable = document.forms[0].party_net_payable.value
	var party_neg_imb_qty = document.forms[0].party_neg_imb_qty.value
	var party_neg_imb_rate = document.forms[0].party_neg_imb_rate.value
	var party_pos_imb_qty = document.forms[0].party_pos_imb_qty.value
	var party_pos_imb_rate = document.forms[0].party_pos_imb_rate.value
	var party_unauth_overrun_qty = document.forms[0].party_unauth_overrun_qty.value
	var party_unauth_overrun_rate = document.forms[0].party_unauth_overrun_rate.value
	
	var applicable_abbr = document.forms[0].applicable_abbr.value
	
	var msg="";
	var flag=true;
	
	/* if(contact_person=="0" || trim(contact_person) == "")
	{
		msg+="Select Trader Contact Person!\n";
		flag=false;
	} */
	if(bu_contact_person=="0" || trim(bu_contact_person) == "")
	{
		msg+="Select Business Contact Person!\n";
		flag=false;
	}
	
	if(trim(invoice_type) == "")
	{
		msg+="Invoice Type is missing!\n";
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
		
		if(invoice_type=="IC")
		{
			if(trim(sys_neg_imb_rate)=="" && inv_component.includes("NI"))
			{
				msg+="Negative Imbalance Rate missing!\n";
				flag=false;
			}
			if(trim(sys_pos_imb_rate)=="" && inv_component.includes("PI"))
			{
				msg+="Positive Imbalance Rate missing!\n";
				flag=false;
			}
			if(trim(sys_unauth_overrun_rate)=="" && inv_component.includes("UR"))
			{
				msg+="Unauthorised Overrun Rate missing!\n";
				flag=false;
			}
		}
		else if(invoice_type=="IC")
		{
			if(trim(sys_price)=="" && inv_component.includes("TP"))
			{
				msg+="Confirmed Price missing!\n";
				flag=false;
			}	
		}
		
		if(trim(sys_gross_amt1)=="")
		{
			msg+="Gross Amount missing!\n";
			flag=false;
		}
		if(trim(sys_tax_amt)=="")
		{
			msg+="Tax Amount missing!\n";
			flag=false;
		}
		if(trim(sys_invoice_amt)=="")
		{
			msg+="Invoice Amount missing!\n";
			flag=false;
		}
		if(applicable_abbr=="TDS")
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
		if(invoice_type=="IC")
		{
			/* if(trim(party_neg_imb_rate)=="" && inv_component.includes("NI"))
			{
				msg+="Negative Imbalance Rate missing!\n";
				flag=false;
			}
			if(trim(party_pos_imb_rate)=="" && inv_component.includes("PI"))
			{
				msg+="Positive Imbalance Rate missing!\n";
				flag=false;
			}
			if(trim(party_unauth_overrun_rate)=="" && inv_component.includes("UR"))
			{
				msg+="Unauthorised Overrun Rate missing!\n";
				flag=false;
			} */
		}
		else if(invoice_type=="IC")
		{
			if(trim(party_price)=="" && inv_component.includes("TP"))
			{
				msg+="Confirmed Price missing!\n";
				flag=false;
			}	
		}
		if(trim(party_gross_amt1)=="")
		{
			msg+="Gross Amount missing!\n";
			flag=false;
		}
		if(trim(party_tax_amt)=="")
		{
			msg+="Tax Amount missing!\n";
			flag=false;
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
	var trans_bu_seq = document.forms[0].trans_bu_seq.value;
	var period_start_dt = document.forms[0].period_start_dt.value;
	var period_end_dt = document.forms[0].period_end_dt.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	
	var sys_financial_year = document.forms[0].sys_financial_year.value;
	
	var u = document.forms[0].u.value;
	
	var url = "../pdf_buy_invoice_remittance.jsp?inv_type="+inv_flag+"&counterparty_cd="+counterparty_cd+
			"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&contract_type="+contract_type+
			"&trans_bu_seq="+trans_bu_seq+"&bu_unit="+bu_unit+"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+
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

function viewBeforeSubmit(inv_flag)
{
	var bu_unit = document.forms[0].bu_unit.value;
	var invoice_type = document.forms[0].invoice_type.value
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev = document.forms[0].cont_rev.value;
	var contract_type = document.forms[0].contract_type.value;
	var trans_bu_seq = document.forms[0].trans_bu_seq.value;
	var period_start_dt = document.forms[0].period_start_dt.value;
	var period_end_dt = document.forms[0].period_end_dt.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	
	var sys_financial_year = document.forms[0].sys_financial_year.value;
	var sys_price_cd = document.forms[0].sys_price_cd.value;
	var sys_invoice_raised_in = document.forms[0].sys_invoice_raised_in.value;
	
	var sys_alloc_qty = document.forms[0].sys_alloc_qty.value
	var sys_deficiency_qty = document.forms[0].sys_deficiency_qty.value
	var sys_neg_imb_qty = document.forms[0].sys_neg_imb_qty.value
	var sys_pos_imb_qty = document.forms[0].sys_pos_imb_qty.value
	var sys_unauth_overrun_qty = document.forms[0].sys_unauth_overrun_qty.value
	
	var party_alloc_qty = document.forms[0].party_alloc_qty.value
	var party_deficiency_qty = document.forms[0].party_deficiency_qty.value
	var party_neg_imb_qty = document.forms[0].party_neg_imb_qty.value
	var party_pos_imb_qty = document.forms[0].party_pos_imb_qty.value
	var party_unauth_overrun_qty = document.forms[0].party_unauth_overrun_qty.value
	
	//var contact_person = document.forms[0].contact_person.value
	var bu_contact_person = document.forms[0].bu_contact_person.value
	
	var adj_plusmin="";
	var allo_qty="";
	var neg_qty="";
	var pos_qty="";
	var unauth_qty="";
	var def_qty="";
	if(inv_flag=="P")
	{
		adj_plusmin= document.forms[0].party_adj_plusmin.value
		
		neg_qty=party_neg_imb_qty;
		pos_qty=party_pos_imb_qty;
		unauth_qty=party_unauth_overrun_qty;
		
		def_qty=party_deficiency_qty;
		allo_qty=party_alloc_qty;
	}
	else
	{
		adj_plusmin= document.forms[0].sys_adj_plusmin.value
		
		neg_qty=sys_neg_imb_qty;
		pos_qty=sys_pos_imb_qty;
		unauth_qty=sys_unauth_overrun_qty;
		
		def_qty=sys_deficiency_qty;
		allo_qty=sys_alloc_qty;
	}
	
	if(bu_contact_person=="0" || trim(bu_contact_person)=="")
	{
		alert("Select Business Contact Person!");
	}
	else
	{
		var url = "../gta/rpt_gta_view_before_submit.jsp?inv_type="+inv_flag+"&counterparty_cd="+counterparty_cd+
			"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&contract_type="+contract_type+
			"&trans_bu_seq="+trans_bu_seq+"&bu_unit="+bu_unit+"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+
			"&billing_cycle="+billing_cycle+"&financial_year="+sys_financial_year+"&price_cd="+sys_price_cd+
			"&invoice_raised_in="+sys_invoice_raised_in+"&bu_contact_person="+bu_contact_person+
			"&adj_plusmin="+adj_plusmin+"&invoice_type="+invoice_type+
			"&neg_qty="+neg_qty+"&pos_qty="+pos_qty+"&unauth_qty="+unauth_qty+"&def_qty="+def_qty+"&allo_qty="+allo_qty;
		
		if(!newWindow || newWindow.closed)
		{
			newWindow= window.open(url,"Invoice Remittance","top=10,left=70,width=1100,height=900,scrollbars=1");
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
				a=confirm("Do you want to Checked System Generated Invoice?");
			}
			else if(flag == "P")
			{
				a=confirm("Do you want to Checked Party Generated Invoice?");
			}
			
			if(a)
			{
				document.forms[0].opration.value="CHECK";
				document.getElementById("loading").style.visibility = "visible";
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
	var sys_deficiency_qty = document.forms[0].sys_deficiency_qty;
	var sys_ship_pay_rate = document.forms[0].sys_ship_pay_rate;
	var sys_neg_imb_qty = document.forms[0].sys_neg_imb_qty
	var sys_neg_imb_rate = document.forms[0].sys_neg_imb_rate
	var sys_pos_imb_qty = document.forms[0].sys_pos_imb_qty
	var sys_pos_imb_rate = document.forms[0].sys_pos_imb_rate
	var sys_unauth_overrun_qty = document.forms[0].sys_unauth_overrun_qty
	var sys_unauth_overrun_rate = document.forms[0].sys_unauth_overrun_rate
	var sys_sub_tax_amt = document.forms[0].sys_sub_tax_amt
	
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
	var party_deficiency_qty = document.forms[0].party_deficiency_qty;
	var party_ship_pay_rate = document.forms[0].party_ship_pay_rate;
	var party_neg_imb_qty = document.forms[0].party_neg_imb_qty;
	var party_neg_imb_rate = document.forms[0].party_neg_imb_rate;
	var party_pos_imb_qty = document.forms[0].party_pos_imb_qty;
	var party_pos_imb_rate = document.forms[0].party_pos_imb_rate;
	var party_unauth_overrun_qty = document.forms[0].party_unauth_overrun_qty;
	var party_unauth_overrun_rate = document.forms[0].party_unauth_overrun_rate;
	var party_sub_tax_amt = document.forms[0].party_sub_tax_amt
	
	var diiff_qty=parseFloat("0");
	var diiff_price=parseFloat("0");
	
	var diiff_deficiency_qty=parseFloat("0");
	var diiff_ship_pay_rate=parseFloat("0");
	var diiff_neg_imb_qty=parseFloat("0");
	var diiff_neg_imb_rate=parseFloat("0");
	var diiff_pos_imb_qty=parseFloat("0");
	var diiff_pos_imb_rate=parseFloat("0");
	var diiff_unauth_qty=parseFloat("0");
	var diiff_unauth_rate=parseFloat("0");
	
	var diiff_gross=parseFloat("0");
	var diiff_exchg=parseFloat("0");
	var diiff_gross1=parseFloat("0");
	var diiff_txt=parseFloat("0");
	var diiff_totamt=parseFloat("0");
	var diiff_netpay=parseFloat("0");
	
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
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_qty),3);
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
	  	trCount +=1;
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
		
		trCount +=1;
		if(trim(sys_deficiency_qty.value)!="" && trim(party_deficiency_qty.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_deficiency_qty=parseFloat(party_deficiency_qty.value)-parseFloat(sys_deficiency_qty.value)
			if(!isNaN(diiff_deficiency_qty))
			{
				if(parseFloat(diiff_deficiency_qty) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_deficiency_qty),3);
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
		trCount +=1;
		if(trim(sys_ship_pay_rate.value)!="" && trim(party_ship_pay_rate.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_ship_pay_rate=parseFloat(party_ship_pay_rate.value)-parseFloat(sys_ship_pay_rate.value)
			if(!isNaN(diiff_ship_pay_rate))
			{
				if(parseFloat(diiff_ship_pay_rate) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_ship_pay_rate),2);
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
		trCount +=1;
		if(trim(sys_neg_imb_qty.value)!="" && trim(party_neg_imb_qty.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_neg_imb_qty=parseFloat(party_neg_imb_qty.value)-parseFloat(sys_neg_imb_qty.value)
			if(!isNaN(diiff_neg_imb_qty))
			{
				if(parseFloat(diiff_neg_imb_qty) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_neg_imb_qty),3);
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

		trCount +=1;
		if(trim(sys_neg_imb_rate.value)!="" && trim(party_neg_imb_rate.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_neg_imb_rate=parseFloat(party_neg_imb_rate.value)-parseFloat(sys_neg_imb_rate.value)
			if(!isNaN(diiff_neg_imb_rate))
			{
				if(parseFloat(diiff_neg_imb_rate) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_neg_imb_rate),2);
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
		trCount +=1;
		if(trim(sys_pos_imb_qty.value)!="" && trim(party_pos_imb_qty.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_pos_imb_qty=parseFloat(party_pos_imb_qty.value)-parseFloat(sys_pos_imb_qty.value)
			if(!isNaN(diiff_pos_imb_qty))
			{
				if(parseFloat(diiff_pos_imb_qty) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_pos_imb_qty),3);
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

		trCount +=1;
		if(trim(sys_pos_imb_rate.value)!="" && trim(party_pos_imb_rate.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_pos_imb_rate=parseFloat(party_pos_imb_rate.value)-parseFloat(sys_pos_imb_rate.value)
			if(!isNaN(diiff_pos_imb_rate))
			{
				if(parseFloat(diiff_pos_imb_rate) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_pos_imb_rate),2);
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
		
		trCount +=1;
		if(trim(sys_unauth_overrun_qty.value)!="" && trim(party_unauth_overrun_qty.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_unauth_qty=parseFloat(party_unauth_overrun_qty.value)-parseFloat(sys_unauth_overrun_qty.value)
			if(!isNaN(diiff_unauth_qty))
			{
				if(parseFloat(diiff_unauth_qty) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_unauth_qty),3);
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

		trCount +=1;
		if(trim(sys_unauth_overrun_rate.value)!="" && trim(party_unauth_overrun_rate.value)!="")
		{
			var td = tr[trCount].getElementsByTagName("td")
			diiff_unauth_rate=parseFloat(party_unauth_overrun_rate.value)-parseFloat(sys_unauth_overrun_rate.value)
			if(!isNaN(diiff_unauth_rate))
			{
				if(parseFloat(diiff_unauth_rate) != 0)
				{
					td = td[(td.length) - 1].innerText="Difference "+round(parseFloat(diiff_unauth_rate),2);
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
		
		trCount +=1;
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
		trCount +=1;
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
		trCount +=1;
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
		
		trCount +=1;
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
		
		trCount +=1; //Skip
		trCount +=1;
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
	var billing_cycle = document.forms[0].billing_cycle.value;
	var ship_pay_freq = document.forms[0].ship_pay_freq.value;
	var days = document.forms[0].days.value;
	
	var sys_neg_imb_qty = document.forms[0].sys_neg_imb_qty
	var sys_pos_imb_qty = document.forms[0].sys_pos_imb_qty
	var sys_unauth_overrun_qty = document.forms[0].sys_unauth_overrun_qty
	
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
	
	var party_deficiency_qty = document.forms[0].party_deficiency_qty
	var party_ship_pay_rate = document.forms[0].party_ship_pay_rate
	var party_neg_imb_qty = document.forms[0].party_neg_imb_qty
	var party_neg_imb_rate = document.forms[0].party_neg_imb_rate
	var party_pos_imb_qty = document.forms[0].party_pos_imb_qty
	var party_pos_imb_rate = document.forms[0].party_pos_imb_rate
	var party_unauth_overrun_qty = document.forms[0].party_unauth_overrun_qty
	var party_unauth_overrun_rate = document.forms[0].party_unauth_overrun_rate
	
	var party_transmission_amt = document.forms[0].party_transmission_amt
	var party_deficiency_amt = document.forms[0].party_deficiency_amt
	var party_neg_imb_amt = document.forms[0].party_neg_imb_amt
	var party_pos_imb_amt = document.forms[0].party_pos_imb_amt
	var party_unauth_overrun_amt = document.forms[0].party_unauth_overrun_amt
	
	var party_invoice_raised_in = document.forms[0].party_invoice_raised_in;
	var party_price_cd = document.forms[0].party_price_cd;
	
	var sys_tax_factor = document.forms[0].sys_tax_factor;
	
	var invoice_type = document.forms[0].invoice_type.value;
	
	var applicable_abbr = document.forms[0].applicable_abbr.value;
	var party_tds_factor = document.forms[0].party_tds_factor;
	var party_tds_amt = document.forms[0].party_tds_amt;
	
	var party_sub_tax_amt=document.forms[0].party_sub_tax_amt;
	var len_multi_tax=document.forms[0].len_multi_tax;
	
	if((invoice_type=="TC") || (invoice_type=="IC" ))
	{
		var gross = "";
		
		if(invoice_type=="IC")
		{
			//gross=parseFloat("0");
			
			if(trim(party_neg_imb_qty.value) != "" && trim(party_neg_imb_rate.value)!="")
			{
				//gross = parseFloat(gross) + (parseFloat(party_neg_imb_qty.value) * parseFloat(party_neg_imb_rate.value))
				
				party_neg_imb_amt.value=round((parseFloat(party_neg_imb_qty.value) * parseFloat(party_neg_imb_rate.value)),2)
			}
			else
			{
				party_neg_imb_amt.value=round(0,2)
			}
			if(trim(party_pos_imb_qty.value) != "" && trim(party_pos_imb_rate.value)!="")
			{
				//gross = parseFloat(gross) + (parseFloat(party_pos_imb_qty.value) * parseFloat(party_pos_imb_rate.value))
				
				party_pos_imb_amt.value=round((parseFloat(party_pos_imb_qty.value) * parseFloat(party_pos_imb_rate.value)),2)
			}
			else
			{
				party_pos_imb_amt.value=round(0,2)
			}
			if(trim(party_unauth_overrun_qty.value) != "" && trim(party_unauth_overrun_rate.value)!="")
			{
				//gross = parseFloat(gross) + (parseFloat(party_unauth_overrun_qty.value) * parseFloat(party_unauth_overrun_rate.value))
				
				party_unauth_overrun_amt.value=round((parseFloat(party_unauth_overrun_qty.value) * parseFloat(party_unauth_overrun_rate.value)),2)
			}
			else
			{
				party_unauth_overrun_amt.value=round(0,2)
			}	  
		}
		else if(invoice_type=="TC")
		{
			if(ship_pay_freq=="M")
			{
				if(billing_cycle=="2" || parseInt(days)<1)
				{
					if(trim(party_deficiency_qty.value) != "" && trim(party_ship_pay_rate.value)!="")
					{
						//gross=parseFloat("0");
						
						//gross = parseFloat(gross) + (parseFloat(party_deficiency_qty.value) * parseFloat(party_ship_pay_rate.value))
						
						party_deficiency_amt.value=round((parseFloat(party_deficiency_qty.value) * parseFloat(party_ship_pay_rate.value)),2)
					}
					else
					{
						party_deficiency_amt.value=round(0,2)
					}
				}
			}
			
			if(trim(party_alloc_qty.value) != "" && trim(party_price.value)!="")
			{
				party_transmission_amt.value=round((parseFloat(party_price.value) * parseFloat(party_alloc_qty.value)),2)
			}
			else
			{
				party_transmission_amt.value=round(0,2)
			}
		}
		
		if(party_gross_amt1.value!="")
		{
			gross=party_gross_amt1.value;
		}
		if(!isNaN(gross))
		{
			party_gross_amt.value=round(parseFloat(gross),2)
		}
		else
		{
			party_gross_amt.value="";
		}
		
		
		
		var gross1="";
		if(party_price_cd.value != party_invoice_raised_in.value)
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
		}
		
		if(!isNaN(gross1))
		{
			party_gross_amt1.value=round(parseFloat(gross1),2)
			
			if(gross1 !="")
			{
				if(applicable_abbr=="TDS" && party_tds_factor.value!="")
				{
					var tds_amt =parseFloat(gross1) * parseFloat(party_tds_factor.value)/100;
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
			
			if(trim(party_tax_amt.value) != "")
			{
				if(parseInt(len_multi_tax.value)==1) //FOR SINGLE TAX COMPONENT
				{
					party_sub_tax_amt.value=party_tax_amt.value;
				}
				var tot_inv=parseFloat(gross1) + parseFloat(party_tax_amt.value)
				if(!isNaN(tot_inv))
				{
					party_invoice_amt.value=round(parseFloat(tot_inv),2)
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
		else
		{
			party_gross_amt1.value="";
			party_invoice_amt.value="";
			party_net_payable.value="";
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

function refreshParent()
{
	window.opener.refresh();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GTA_Remittance" id="remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String trans_bu_seq=request.getParameter("trans_bu_seq")==null?"":request.getParameter("trans_bu_seq");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String period_start_dt=request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
String qty=request.getParameter("qty_mmbtu")==null?"":request.getParameter("qty_mmbtu");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
String inv_comp=request.getParameter("inv_comp")==null?"":request.getParameter("inv_comp");
String fiscal_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String inv_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");

String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String refresh_flg =request.getParameter("refresh_flg")==null?"":request.getParameter("refresh_flg");
String activity_type =request.getParameter("activity_type")==null?"":request.getParameter("activity_type");

remittance.setCallFlag("GTA_SELLER_PAYMENT_DETAIL");
remittance.setComp_cd(owner_cd);
remittance.setCounterparty_cd(counterparty_cd);
remittance.setAgmt_no(agmt_no);
remittance.setAgmt_rev_no(agmt_rev);
remittance.setCont_no(cont_no);
remittance.setCont_rev_no(cont_rev);
remittance.setContract_type(contract_type);
remittance.setTrans_bu_seq(trans_bu_seq);
remittance.setBilling_cycle(billing_cycle);
remittance.setPeriod_start_dt(period_start_dt);
remittance.setPeriod_end_dt(period_end_dt);
remittance.setBu_unit(bu_unit);
remittance.setRefresh_flg(refresh_flg);
//remittance.setQty_mmbtu(qty);
remittance.setInvoice_type(invoice_type);
remittance.setInv_component(inv_comp);
remittance.setFiscal_year(fiscal_year);
remittance.setInv_seq(inv_seq);
remittance.init();

String cont_end_dt=remittance.getCont_end_dt();
String couterpty_abbr=remittance.getCouterpty_abbr();
String deal_no=remittance.getDeal_no();
String trans_bu_abbr=remittance.getTrans_bu_abbr();
String bu_plant_abbr=remittance.getBu_plant_abbr();
String qty_mmbtu=remittance.getQty_mmbtu();
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
String invoice_dt = remittance.getInvoice_dt();
String invoice_due_dt = remittance.getInvoice_due_dt();
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
String negative_imbalance_qty = remittance.getNegative_imbalance_qty();
String positive_imbalance_qty = remittance.getPositive_imbalance_qty();
String unauthorized_overrun_qty = remittance.getUnauthorized_overrun_qty();
String negative_imbalance_rate = remittance.getNegative_imbalance_rate();
String positive_imbalance_rate = remittance.getPositive_imbalance_rate();
String unauthorized_overrun_rate = remittance.getUnauthorized_overrun_rate();

String positive_imbalance_amount = remittance.getPositive_imbalance_amount();
String negative_imbalance_amount = remittance.getNegative_imbalance_amount();
String unauthorized_imbalance_amount = remittance.getUnauthorized_imbalance_amount();
String ship_pay_rate= remittance.getShip_pay_rate();
String deficiency_qty=remittance.getDeficiency_qty();
String deficiency_amt=remittance.getDeficiency_amt();
String transmission_amt=remittance.getTransmission_amt();

String applicable_abbr = remittance.getApplicable_abbr();
String tds_factor = remittance.getTds_factor();
String tds_amount = remittance.getTds_amount();
String tds_struct_cd=remittance.getTds_struct_cd();
String tds_struct_dt=remittance.getTds_struct_dt();

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
String pg_negative_imbalance_qty = remittance.getPg_negative_imbalance_qty();
String pg_positive_imbalance_qty = remittance.getPg_positive_imbalance_qty();
String pg_unauthorized_overrun_qty = remittance.getPg_unauthorized_overrun_qty();
String pg_negative_imbalance_rate = remittance.getPg_negative_imbalance_rate();
String pg_positive_imbalance_rate = remittance.getPg_positive_imbalance_rate();
String pg_unauthorized_overrun_rate = remittance.getPg_unauthorized_overrun_rate();

String pg_positive_imbalance_amount = remittance.getPg_positive_imbalance_amount();
String pg_negative_imbalance_amount = remittance.getPg_negative_imbalance_amount();
String pg_unauthorized_imbalance_amount = remittance.getPg_unauthorized_imbalance_amount();
String pg_ship_pay_rate= remittance.getPg_ship_pay_rate();
String pg_deficiency_qty=remittance.getPg_deficiency_qty();
String pg_deficiency_amt=remittance.getPg_deficiency_amt();
String pg_transmission_amt=remittance.getPg_transmission_amt();

String pg_tds_factor = remittance.getPg_tds_factor();
String pg_tds_amount = remittance.getPg_tds_amount();
String pg_tds_struct_cd=remittance.getPg_tds_struct_cd();
String pg_tds_struct_dt=remittance.getPg_tds_struct_dt();

String ship_pay_freq = remittance.getShip_pay_freq();

Vector VBU_CONTACT_PERSON = remittance.getVBU_CONTACT_PERSON();
Vector VBU_CONTACT_PERSON_CD = remittance.getVBU_CONTACT_PERSON_CD();

Vector VSG_MULTI_TAX_STRUCT = remittance.getVSG_MULTI_TAX_STRUCT();
Vector VPG_MULTI_TAX_STRUCT = remittance.getVPG_MULTI_TAX_STRUCT();

int len_multi_tax=0;


boolean sg_submission_chk = remittance.getSg_submission_chk();
boolean pg_submission_chk = remittance.getPg_submission_chk();

int days =utildate.getDays(cont_end_dt, period_end_dt);
%>
<body onload="enableSystemGen(true);enablePartyGen(false);difference();copyInvoiceNo();<%if(!msg.equals("")){%>refreshParent();<%}%>"
	<%if(!activity_type.equals("PREPARE")) {%>style="pointer-events: none;"<%} %>
>

<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_GTA_Remittance">

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
					    	<%if(invoice_type.equals("IC")){ %>
					    	Imbalance Charge Remittance Detail
					    	<%}else{ %>
					    	Transmission Charge Remittance Detail
					    	<%} %>
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
											<th colspan="2">Transporter Detail</th>
											<th rowspan="2">GTC <br>[Contract Ref#]</th>
											<th rowspan="2">Billing Period</th>
											<th colspan="2"><%=owner_abbr%> Detail</th>
										</tr>
										<tr>
											<th>Transporter</th>
											<th>Transporter BU</th>
											<th>Business Unit</th>
											<th>Contact Person<span class="s-red">*</span></th>											
										</tr>
									</thead>
									<tbody>
										<tr>
											<td align="center">
												<%=couterpty_abbr%>
											</td>
											<td align="center"><%=trans_bu_abbr%></td>
											<td align="center"><%=deal_no%></td>
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
							<h5>
								<span class="badge bg-info text-dark">TCS/TDS Applicable : <%=applicable_abbr%></span>
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
													<input type="radio" name="gta_invoice_type" value="S" checked onclick="doEnabled(this);">&nbsp;System Generated
												</span>
											</th>
											<th>
												<span class="badge rounded-pill" style="background:skyblue;color:black;font-size:12px;">
													<input type="radio" name="gta_invoice_type" value="P" onclick="doEnabled(this);" <%if(!invoice_check_flag.equals("Y")){ %>disabled<%} %>>&nbsp;Party Generated
												</span>
											</th>
											<th>
												Difference
											</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><b>Invoice#<span class="s-red">*</span><font color="blue">(Party Generated)</font></b></td>
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
													<input type="hidden" name="party_invoice_seq" value="<%=pg_invoice_seq%>">
													<input type="hidden" name="party_financial_year" value="<%=pg_financial_year%>">
													<input type="hidden" name="party_system_invoice_no" value="<%=pg_sys_invoice_no%>">
												</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b>Invoice Date<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="sys_invoice_dt" value="<%=invoice_dt%>" maxLength="10" 
							      						onblur="validateDate(this);" 
							      						onchange="validateDate(this);" autocomplete="off">
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
											<td><b>Invoice Due Date<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="sys_invoice_due_dt" value="<%=invoice_due_dt%>" maxLength="10" 
							      						onblur="validateDate(this);" 
							      						onchange="validateDate(this);" autocomplete="off">
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
										<tr id="tab_trans_qty">
											<td><b>Transmitted Quantity</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_alloc_qty" value="<%=qty_mmbtu%>" readOnly>
														<span class="input-group-text">MMBTU</span>
													</div>
													<input type="hidden" name="sys_transmission_amt" value="<%=transmission_amt%>" readOnly>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_alloc_qty" value="<%=pg_qty_mmbtu%>"
														onkeyup=" partyGenCalc();difference();checkNumber1(this,13,3);">
														<span class="input-group-text">MMBTU</span>
													</div>
													<input type="hidden" name="party_transmission_amt" value="<%=pg_transmission_amt%>" readOnly>
												</div>
											</td>
											<td></td>
										</tr>
										<tr id="tab_trans_chrg">
											<td><b>Transportation Rate<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_price" value="<%=price%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="sys_price_cd" value="<%=price_cd%>">
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_price" value="<%=pg_price%>" 
														onkeyup=" partyGenCalc();difference();<%if(price_cd.equals("1")){ %>checkNumber1(this,6,2);<%}else{ %>checkNumber1(this,6,4);<%}%>">
														<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="party_price_cd" value="<%=pg_price_cd%>">
												</div>
											</td>
											<td></td>
										</tr>
										<tr id="tab_def_qty">
											<td><b>Ship-or-Pay Chargeable Quantity</b></td> <!-- DEFICIENCY QUANTITY -->
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_deficiency_qty" value="<%=deficiency_qty%>" readOnly>
														<span class="input-group-text">MMBTU</span>
													</div>
													<input type="hidden" name="sys_deficiency_amt" value="<%=deficiency_amt%>" readOnly>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_deficiency_qty" value="<%=pg_deficiency_qty%>"
														onkeyup=" partyGenCalc();difference();checkNumber1(this,13,3);">
														<span class="input-group-text">MMBTU</span>
													</div>
													<input type="hidden" name="party_deficiency_amt" value="<%=pg_deficiency_amt%>" readOnly>
												</div>
											</td>
											<td></td>
										</tr>
										<tr id="tab_ship_chrg">
											<td><b>Ship-or-Pay Rate<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_ship_pay_rate" value="<%=ship_pay_rate%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_ship_pay_rate" value="<%=pg_ship_pay_rate%>" 
														onkeyup=" partyGenCalc();difference();<%if(price_cd.equals("1")){ %>checkNumber1(this,6,2);<%}else{ %>checkNumber1(this,6,4);<%}%>">
														<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr id="tab_neg_imb_qty">
											<td><b>Negative Imbalance > 5%</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_neg_imb_qty" value="<%=negative_imbalance_qty%>" readOnly>
														<span class="input-group-text">MMBTU</span>
													</div>
													<input type="hidden" name="sys_neg_imb_amt" value="<%=negative_imbalance_amount%>" readOnly>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_neg_imb_qty" value="<%=pg_negative_imbalance_qty%>"
														onkeyup=" partyGenCalc();difference();checkNumber1(this,13,3);">
														<span class="input-group-text">MMBTU</span>
													</div>
													<input type="hidden" name="party_neg_imb_amt" value="<%=pg_negative_imbalance_amount%>" readOnly>
												</div>
											</td>
											<td></td>
										</tr>
										<tr id="tab_neg_imb_chrg">
											<td><b>Negative Imbalance Rate<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_neg_imb_rate" value="<%=negative_imbalance_rate%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_neg_imb_rate" value="<%=pg_negative_imbalance_rate%>" 
														onkeyup=" partyGenCalc();difference();<%if(price_cd.equals("1")){ %>checkNumber1(this,6,2);<%}else{ %>checkNumber1(this,6,4);<%}%>">
														<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr id="tab_pos_imb_qty">
											<td><b>Positive Imbalance > 10%</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_pos_imb_qty" value="<%=positive_imbalance_qty%>" readOnly>
														<span class="input-group-text">MMBTU</span>
													</div>
													<input type="hidden" name="sys_pos_imb_amt" value="<%=positive_imbalance_amount%>" readOnly>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_pos_imb_qty" value="<%=pg_positive_imbalance_qty%>"
														onkeyup=" partyGenCalc();difference();checkNumber1(this,13,3);">
														<span class="input-group-text">MMBTU</span>
													</div>
													<input type="hidden" name="party_pos_imb_amt" value="<%=pg_positive_imbalance_amount%>" readOnly>
												</div>
											</td>
											<td></td>
										</tr>
										<tr id="tab_pos_imb_chrg">
											<td><b>Positive Imbalance Rate<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_pos_imb_rate" value="<%=positive_imbalance_rate%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_pos_imb_rate" value="<%=pg_positive_imbalance_rate%>" 
														onkeyup=" partyGenCalc();difference();<%if(price_cd.equals("1")){ %>checkNumber1(this,6,2);<%}else{ %>checkNumber1(this,6,4);<%}%>">
														<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr id="tab_unauth_qty">
											<td><b>Unauthorized Overrun</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_unauth_overrun_qty" value="<%=unauthorized_overrun_qty%>" readOnly>
														<span class="input-group-text">MMBTU</span>
													</div>
													<input type="hidden" name="sys_unauth_overrun_amt" value="<%=unauthorized_imbalance_amount%>" readOnly>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_unauth_overrun_qty" value="<%=pg_unauthorized_overrun_qty%>"
														onkeyup=" partyGenCalc();difference();checkNumber1(this,13,3);">
														<span class="input-group-text">MMBTU</span>
													</div>
													<input type="hidden" name="party_unauth_overrun_amt" value="<%=pg_unauthorized_imbalance_amount%>" readOnly>
												</div>
											</td>
											<td></td>
										</tr>
										<tr id="tab_unauth_chrg">
											<td><b>Unauthorized Overrun Charge<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_unauth_overrun_rate" value="<%=unauthorized_overrun_rate%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_unauth_overrun_rate" value="<%=pg_unauthorized_overrun_rate%>" 
														onkeyup=" partyGenCalc();difference();<%if(price_cd.equals("1")){ %>checkNumber1(this,6,2);<%}else{ %>checkNumber1(this,6,4);<%}%>">
														<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
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
														<input type="text" class="form-control form-control-sm" name="party_gross_amt" value="<%=pg_gross_amt%>" readOnly>
														<span class="input-group-text"><%=price_cd_nm%></span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr <%if(!price_cd.equals(invoice_raised_in)){ %><%}else{ %>style="display:none;"<%} %>>
											<td><b>Exchange Rate</b></td>
											<td align="center">
												<div style="width:250px;">
													<input type="text" class="form-control form-control-sm" name="sys_exchng_rate" value="<%=exchang_rate%>" readOnly>
													<input type="hidden" name="sys_exchng_cd" value="<%=exchng_rate_cd%>">
													<input type="hidden" name="sys_exchng_dt" value="<%=exchang_rate_dt%>"> 
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<input type="text" class="form-control form-control-sm" name="party_exchng_rate" value="<%=pg_exchang_rate%>" onkeyup=" partyGenCalc();difference();">
													<input type="hidden" name="party_exchng_cd" value="<%=pg_exchng_rate_cd%>">
													<input type="hidden" name="party_exchng_dt" value="<%=pg_exchang_rate_dt%>">
												</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b>Gross Amount<span class="s-red">*</span></b></td>
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
														onkeyup="partyGenCalc();difference();" readOnly>
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="party_invoice_raised_in" value="<%=pg_invoice_raised_in%>">
												</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b>Tax (<%=tax_struct_dtl%>)</b></td>
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
														onkeyup=" partyGenCalc();difference();checkNumber1(this,11,2);">
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
										<tr>
											<td title="Gross Amount + Tax Amount"><b>Invoice Amount<span class="s-red">*</span></b></td>
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
														onkeyup="difference();checkNumber1(this,11,2);">
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr style="display:none;">
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
														onkeyup="difference();checkNumber1(this,11,2);">
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr style="<%if(!activity_type.equals("PREPARE")){%>display:none;<%} %>">
											<td><b>View/Submit Invoice</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="d-flex justify-content-between">
														<i class="fa fa-refresh fa-2x" style="color:blue;" onclick="refresh1();" id="sys_ref_icon"></i>
														<i class="fa fa-eye fa-2x" id="sys_view" onclick="viewBeforeSubmit('S');"></i>
														<input type="button" class="btn btn-warning com-btn" id="sys_submit" value="Submit" onclick="doSubmit('S');">
														<!-- <input type="button" class="btn btn-warning com-btn" id="sys_view" value="View" onclick="printPDF('S');"> -->
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="d-flex justify-content-between">
														<i class="fa fa-eye fa-2x" id="party_view" onclick="viewBeforeSubmit('P');"></i>
														<input type="button" class="btn btn-warning com-btn" id="party_submit" value="Submit" onclick="doSubmit('P');">
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr style="<%if(activity_type.equals("PREPARE")){%>display:none;<%}else{ %>pointer-events: auto;<%}%>">
											<td><b>View Invoice</b></td>
											<td align="center">
												<i class="fa fa-eye fa-2x" onclick="viewBeforeSubmit('S');"></i>
											</td>
											<td align="center">
												<i class="fa fa-eye fa-2x" onclick="viewBeforeSubmit('P');"></i>
											</td>
											<td></td>
										</tr>
										<tr style="<%if(!activity_type.equals("CHECK")){%>display:none;<%}else{ %>pointer-events: auto;<%}%>">
											<td><b>Check Invoice</b></td>
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
											<td><b>Authorize Invoice</b></td>
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

<input type="hidden" name="option" value="GTA_INVOICE">
<input type="hidden" name="opration" value="INSERT">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" value="<%=agmt_rev%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev" value="<%=cont_rev%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="trans_bu_seq" value="<%=trans_bu_seq%>">
<input type="hidden" name="period_start_dt" value="<%=period_start_dt%>">
<input type="hidden" name="period_end_dt" value="<%=period_end_dt%>">
<input type="hidden" name="billing_cycle" value="<%=billing_cycle%>">
<input type="hidden" name="bu_unit" value="<%=bu_unit%>">
<input type="hidden" name="financial_year" value="<%=fiscal_year%>">
<input type="hidden" name="invoice_seq" value="<%=inv_seq%>">
<input type="hidden" name="inv_component" value="<%=inv_comp%>">

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

<input type="hidden" name="invoice_type" value="<%=invoice_type%>">
<input type="hidden" name="qty" value="<%=qty%>">

<input type="hidden" name="ship_pay_freq" value="<%=ship_pay_freq%>">

<input type="hidden" name="applicable_abbr" value="<%=applicable_abbr%>">

<input type="hidden" name="sys_tds_amt" value="<%=tds_amount%>">
<input type="hidden" name="sys_tds_factor" value="<%=tds_factor%>">
<input type="hidden" name="sys_tds_cd" value="<%=tds_struct_cd%>">
<input type="hidden" name="sys_tds_dt" value="<%=tds_struct_dt%>">

<input type="hidden" name="party_tds_amt" value="<%=pg_tds_amount%>">
<input type="hidden" name="party_tds_factor" value="<%=pg_tds_factor%>">
<input type="hidden" name="party_tds_cd" value="<%=pg_tds_struct_cd%>">
<input type="hidden" name="party_tds_dt" value="<%=pg_tds_struct_dt%>">

<input type="hidden" name="len_multi_tax" value="<%=len_multi_tax%>">

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

<input type="hidden" name="days" value="<%=days%>">

<%if(invoice_type.equals("TC")){ %>
	<%if(ship_pay_freq.equals("M")){ %>
		<%if(billing_cycle.equals("2") || days<1){ %>
			<script>
			document.getElementById("tab_trans_qty").style.display="table-row";
			document.getElementById("tab_trans_chrg").style.display="table-row";
			document.getElementById("tab_def_qty").style.display="table-row";
			document.getElementById("tab_ship_chrg").style.display="table-row";
			document.getElementById("tab_neg_imb_qty").style.display="none";
			document.getElementById("tab_neg_imb_chrg").style.display="none";
			document.getElementById("tab_pos_imb_qty").style.display="none";
			document.getElementById("tab_pos_imb_chrg").style.display="none";
			document.getElementById("tab_unauth_qty").style.display="none";
			document.getElementById("tab_unauth_chrg").style.display="none";
			</script>
		<%}else{ %>
			<script>
			document.getElementById("tab_trans_qty").style.display="table-row";
			document.getElementById("tab_trans_chrg").style.display="table-row";
			document.getElementById("tab_def_qty").style.display="none";
			document.getElementById("tab_ship_chrg").style.display="none";
			document.getElementById("tab_neg_imb_qty").style.display="none";
			document.getElementById("tab_neg_imb_chrg").style.display="none";
			document.getElementById("tab_pos_imb_qty").style.display="none";
			document.getElementById("tab_pos_imb_chrg").style.display="none";
			document.getElementById("tab_unauth_qty").style.display="none";
			document.getElementById("tab_unauth_chrg").style.display="none";
			</script>
		<%} %>
	<%}else{ %>
		<script>
		document.getElementById("tab_trans_qty").style.display="table-row";
		document.getElementById("tab_trans_chrg").style.display="table-row";
		document.getElementById("tab_def_qty").style.display="none";
		document.getElementById("tab_ship_chrg").style.display="none";
		document.getElementById("tab_neg_imb_qty").style.display="none";
		document.getElementById("tab_neg_imb_chrg").style.display="none";
		document.getElementById("tab_pos_imb_qty").style.display="none";
		document.getElementById("tab_pos_imb_chrg").style.display="none";
		document.getElementById("tab_unauth_qty").style.display="none";
		document.getElementById("tab_unauth_chrg").style.display="none";
		</script>
	<%} %>
<%}else if(invoice_type.equals("IC")){ %>
<script>
document.getElementById("tab_trans_qty").style.display="none";
document.getElementById("tab_trans_chrg").style.display="none";
document.getElementById("tab_def_qty").style.display="none";
document.getElementById("tab_ship_chrg").style.display="none";
document.getElementById("tab_neg_imb_qty").style.display="table-row";
document.getElementById("tab_neg_imb_chrg").style.display="table-row";
document.getElementById("tab_pos_imb_qty").style.display="table-row";
document.getElementById("tab_pos_imb_chrg").style.display="table-row";
document.getElementById("tab_unauth_qty").style.display="table-row";
document.getElementById("tab_unauth_chrg").style.display="table-row";
</script>
<%} %>

<script>
document.getElementById("tab_trans_qty").style.display="none";
document.getElementById("tab_trans_chrg").style.display="none";
document.getElementById("tab_def_qty").style.display="none";
document.getElementById("tab_ship_chrg").style.display="none";
document.getElementById("tab_neg_imb_qty").style.display="none";
document.getElementById("tab_neg_imb_chrg").style.display="none";
document.getElementById("tab_pos_imb_qty").style.display="none";
document.getElementById("tab_pos_imb_chrg").style.display="none";
document.getElementById("tab_unauth_qty").style.display="none";
document.getElementById("tab_unauth_chrg").style.display="none";
</script>

<%if(invoice_type.equals("TC")){ %>
	<%if(inv_comp.contains("TP")) {%>
	<script>
	document.getElementById("tab_trans_qty").style.display="table-row";
	document.getElementById("tab_trans_chrg").style.display="table-row";
	</script>
	<%}
	if(inv_comp.contains("SP")) { %>
	<script>
	document.getElementById("tab_def_qty").style.display="table-row";
	document.getElementById("tab_ship_chrg").style.display="table-row";
	</script>
	<%}%>
<%}else if(invoice_type.equals("IC")) {%>
	<%if(inv_comp.contains("NI")) { %>
	<script>
	document.getElementById("tab_neg_imb_qty").style.display="table-row";
	document.getElementById("tab_neg_imb_chrg").style.display="table-row";
	</script>
	<%}
	if(inv_comp.contains("PI")) { %>
	<script>
	document.getElementById("tab_pos_imb_qty").style.display="table-row";
	document.getElementById("tab_pos_imb_chrg").style.display="table-row";
	</script>
	<%}
	if(inv_comp.contains("UR")) { %>
	<script>
	document.getElementById("tab_unauth_qty").style.display="table-row";
	document.getElementById("tab_unauth_chrg").style.display="table-row";
	</script>
	<%} %>
<%} %>
</form>
</body>
</html>