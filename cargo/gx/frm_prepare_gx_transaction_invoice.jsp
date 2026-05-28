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
	var gx_counterparty_cd = document.forms[0].gx_counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev = document.forms[0].cont_rev.value;
	var contract_type = document.forms[0].contract_type.value;
	var gx_bu_unit = document.forms[0].gx_bu_unit.value;
	var invoice_type = document.forms[0].invoice_type.value;
	
	var activity_type = document.forms[0].activity_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_prepare_gx_transaction_invoice.jsp?counterparty_cd="+counterparty_cd+"&gx_counterparty_cd="+gx_counterparty_cd+
			"&contract_type="+contract_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&gx_bu_unit="+gx_bu_unit+
			"&bu_unit="+bu_unit+"&activity_type="+activity_type+"&invoice_type="+invoice_type+
			"&u="+u;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function refresh1()
{
	var bu_unit = document.forms[0].bu_unit.value;
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var gx_counterparty_cd = document.forms[0].gx_counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev = document.forms[0].cont_rev.value;
	var contract_type = document.forms[0].contract_type.value;
	var gx_bu_unit = document.forms[0].gx_bu_unit.value;
	var invoice_type = document.forms[0].invoice_type.value;
	
	var activity_type = document.forms[0].activity_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_prepare_gx_transaction_invoice.jsp?counterparty_cd="+counterparty_cd+"&gx_counterparty_cd="+gx_counterparty_cd+
		"&contract_type="+contract_type+"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+
		"&gx_bu_unit="+gx_bu_unit+"&bu_unit="+bu_unit+"&refresh_flg=Y&activity_type="+activity_type+"&invoice_type="+invoice_type+
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
			if(party_gross_amt!=null && party_gross_amt!=undefined)
			{
				//party_gross_amt.readOnly=true;
				party_exchng_rate.readOnly=true;
			}
			//party_gross_amt1.readOnly=true;
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
	var gx_bu_contact_person = document.forms[0].gx_bu_contact_person.value
	var bu_contact_person = document.forms[0].bu_contact_person.value
	
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
	
	var applicable_abbr = document.forms[0].applicable_abbr.value
	
	var msg="";
	var flag=true;
	
	if(gx_bu_contact_person=="0" || trim(gx_bu_contact_person) == "")
	{
		msg+="Select Gx Business Contact Person!\n";
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
	var plant_seq = document.forms[0].plant_seq.value;
	var period_start_dt = document.forms[0].period_start_dt.value;
	var period_end_dt = document.forms[0].period_end_dt.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	
	var sys_financial_year = document.forms[0].sys_financial_year.value;
	
	var u = document.forms[0].u.value;
	
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

function viewBeforeSubmit(inv_flag)
{
	var bu_unit = document.forms[0].bu_unit.value;
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var gx_counterparty_cd = document.forms[0].gx_counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev = document.forms[0].cont_rev.value;
	var contract_type = document.forms[0].contract_type.value;
	var gx_bu_unit = document.forms[0].gx_bu_unit.value;
	var invoice_type = document.forms[0].invoice_type.value;
	
	var sys_financial_year = document.forms[0].sys_financial_year.value;
	var sys_price_cd = document.forms[0].sys_price_cd.value;
	var sys_invoice_raised_in = document.forms[0].sys_invoice_raised_in.value;
	
	var gx_bu_contact_person = document.forms[0].gx_bu_contact_person.value
	var bu_contact_person = document.forms[0].bu_contact_person.value
	
	var adj_plusmin="";
	if(inv_flag=="P")
	{
		adj_plusmin= document.forms[0].party_adj_plusmin.value
	}
	else
	{
		adj_plusmin= document.forms[0].sys_adj_plusmin.value
	}
	
	if((bu_contact_person=="0" || trim(bu_contact_person)=="") && (gx_bu_contact_person=="0" || trim(gx_bu_contact_person)==""))
	{
		alert("Select Business Contact Person!\nSelect Customer GX Contact Person!");
	}
	else if(bu_contact_person=="0" || trim(bu_contact_person)=="")
	{
		alert("Select Business Contact Person!");
	}
	else if(gx_bu_contact_person=="0" || trim(gx_bu_contact_person)=="")
	{
		alert("Select Customer GX Contact Person!");
	}
	else
	{
		var url = "../gx/rpt_view_before_submit.jsp?inv_type="+inv_flag+"&counterparty_cd="+counterparty_cd+"&gx_counterparty_cd="+gx_counterparty_cd+
			"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&contract_type="+contract_type+
			"&bu_unit="+bu_unit+"&gx_bu_unit="+gx_bu_unit+"&invoice_type="+invoice_type+
			"&financial_year="+sys_financial_year+"&price_cd="+sys_price_cd+"&invoice_raised_in="+sys_invoice_raised_in+
			"&gx_bu_contact_person="+gx_bu_contact_person+"&bu_contact_person="+bu_contact_person+
			"&adj_plusmin="+adj_plusmin;
		
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
	var party_sub_tax_amt = document.forms[0].party_sub_tax_amt
	
	var diiff_qty=parseFloat("0");
	var diiff_price=parseFloat("0");
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
	
	var applicable_abbr = document.forms[0].applicable_abbr.value;
	
	var party_invoice_raised_in = document.forms[0].party_invoice_raised_in;
	var party_price_cd = document.forms[0].party_price_cd;
	
	var sys_tax_factor = document.forms[0].sys_tax_factor;
	
	var party_tds_factor = document.forms[0].party_tds_factor;
	var party_tds_amt = document.forms[0].party_tds_amt;
	
	var party_sub_tax_amt = document.forms[0].party_sub_tax_amt;
	var len_multi_tax = document.forms[0].len_multi_tax;
	
	if(trim(party_price.value) != "" && trim(party_alloc_qty.value)!="")
	{
		var gross = parseFloat("0");
		/*
		var gross = parseFloat(party_price.value) * parseFloat(party_alloc_qty.value)
		
		if(!isNaN(gross))
		{
			party_gross_amt.value=round(parseFloat(gross),2)
		}
		else
		{
			party_gross_amt.value="";
		}
		*/
		
		if(party_gross_amt.value != "")
		{
			gross=round(parseFloat(party_gross_amt.value),2)
		}
		
		var gross1="";
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
		}
		*/
		
		if(!isNaN(gross1))
		{
			//party_gross_amt1.value=round(parseFloat(gross1),2)
			
			if(party_gross_amt1.value != "")
			{
				gross1=round(parseFloat(party_gross_amt1.value),2)
				
				if(applicable_abbr=="TDS" && party_tds_factor.value!="")
				{
					var tds_amt =parseFloat(gross1) * parseFloat(party_tds_factor.value)/100;
					if(!isNaN(tds_amt))
					{
						party_tds_amt.value=round(parseFloat(tds_amt),2)
					}
				}
			}
			
			//if(party_gross_amt.value == "" && gross1 !="")
			if(party_price_cd.value=="1" && gross1 !="")
			{
				if(!isNaN(gross1))
				{
					party_gross_amt.value=round(parseFloat(gross1),2);
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
<jsp:useBean class="com.etrm.fms.gx.DataBean_Gx_Invoice" id="gx_remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String gx_counterparty_cd=request.getParameter("gx_counterparty_cd")==null?"":request.getParameter("gx_counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String gx_bu_unit=request.getParameter("gx_bu_unit")==null?"":request.getParameter("gx_bu_unit");

String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String refresh_flg =request.getParameter("refresh_flg")==null?"":request.getParameter("refresh_flg");
String activity_type =request.getParameter("activity_type")==null?"":request.getParameter("activity_type");
String invoice_type =request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");

gx_remittance.setCallFlag("GX_TXN_INVOICE_DETAIL");
gx_remittance.setComp_cd(owner_cd);
gx_remittance.setCounterparty_cd(counterparty_cd);
gx_remittance.setGx_counterparty_cd(gx_counterparty_cd);
gx_remittance.setAgmt_no(agmt_no);
gx_remittance.setAgmt_rev_no(agmt_rev);
gx_remittance.setCont_no(cont_no);
gx_remittance.setCont_rev_no(cont_rev);
gx_remittance.setContract_type(contract_type);
gx_remittance.setGx_bu_unit(gx_bu_unit);
gx_remittance.setBu_unit(bu_unit);
gx_remittance.setRefresh_flg(refresh_flg);
gx_remittance.setInvoice_type(invoice_type);
gx_remittance.init();

String couterpty_abbr=gx_remittance.getCouterpty_abbr();
String gx_couterpty_abbr=gx_remittance.getGx_couterpty_abbr();
String deal_no=gx_remittance.getDeal_no();
String gx_bu_plant_abbr=gx_remittance.getGx_bu_plant_abbr();
String bu_plant_abbr=gx_remittance.getBu_plant_abbr();
String qty_mmbtu=gx_remittance.getQty_mmbtu();
String price=gx_remittance.getPrice();
String price_cd=gx_remittance.getPrice_cd();
String price_cd_nm=gx_remittance.getPrice_cd_nm();
String invoice_raised_in = gx_remittance.getInvoice_raised_in();
String invoice_raised_in_nm = gx_remittance.getInvoice_raised_in_nm();
String payment_done_in = gx_remittance.getPayment_done_in();
String payment_done_in_nm = gx_remittance.getPayment_done_in_nm();
String due_days = gx_remittance.getDue_days();
String gross_amt=gx_remittance.getGross_amt();
String gross_amt1=gx_remittance.getGross_amt1();
String exchng_rate_cd=gx_remittance.getExchng_rate_cd();
String exchang_rate = gx_remittance.getExchang_rate();
String exchang_rate_dt = gx_remittance.getExchang_rate_dt();
String tax_amt = gx_remittance.getTax_amt();
String tax_struct_cd=gx_remittance.getTax_struct_cd();
String tax_struct_dt=gx_remittance.getTax_struct_dt();
String tax_struct_dtl=gx_remittance.getTax_struct_dtl();
String tax_info = gx_remittance.getTax_info();
String tax_factor = gx_remittance.getTax_factor();
String invoice_seq=gx_remittance.getInvoice_seq();
String invoice_no=gx_remittance.getInvoice_no();
String sys_invoice_no=gx_remittance.getSys_invoice_no();
String invoice_amt = gx_remittance.getInvoice_amt();
String net_payable = gx_remittance.getNet_payable();
String bu_contact_person_cd = gx_remittance.getBu_contact_person_cd();
String gx_bu_contact_person_cd = gx_remittance.getGx_bu_contact_person_cd();
String invoice_dt = gx_remittance.getInvoice_dt();
String invoice_due_dt = gx_remittance.getInvoice_due_dt();
String invoice_check_flag = gx_remittance.getInvoice_check_flag();
String invoice_check_dt = gx_remittance.getInvoice_check_dt();
String invoice_check_by = gx_remittance.getInvoice_check_by();
String invoice_check_nm = gx_remittance.getInvoice_check_nm();
String invoice_auth_flag = gx_remittance.getInvoice_auth_flag();
String invoice_auth_dt = gx_remittance.getInvoice_auth_dt();
String invoice_auth_by = gx_remittance.getInvoice_auth_by();
String invoice_auth_nm = gx_remittance.getInvoice_auth_nm();
String invoice_aprv_flag = gx_remittance.getInvoice_aprv_flag();
String invoice_aprv_dt = gx_remittance.getInvoice_aprv_dt();
String invoice_aprv_by = gx_remittance.getInvoice_aprv_by();
String invoice_aprv_nm = gx_remittance.getInvoice_aprv_nm();
String invoice_adj_sign = gx_remittance.getInvoice_adj_sign();
String invoice_adj_amt = gx_remittance.getInvoice_adj_amt();
String financial_year = gx_remittance.getFinancial_year();
String applicable_abbr = gx_remittance.getApplicable_abbr();
String tds_factor = gx_remittance.getTds_factor();
String tds_amount = gx_remittance.getTds_amount();
String tds_struct_cd=gx_remittance.getTds_struct_cd();
String tds_struct_dt=gx_remittance.getTds_struct_dt();

String pg_qty_mmbtu=gx_remittance.getPg_qty_mmbtu();
String pg_price=gx_remittance.getPg_price();
String pg_price_cd=gx_remittance.getPg_price_cd();
String pg_invoice_raised_in=gx_remittance.getPg_invoice_raised_in();
String pg_gross_amt=gx_remittance.getPg_gross_amt();
String pg_gross_amt1=gx_remittance.getPg_gross_amt1();
String pg_exchng_rate_cd=gx_remittance.getPg_exchng_rate_cd();
String pg_exchang_rate = gx_remittance.getPg_exchang_rate();
String pg_exchang_rate_dt = gx_remittance.getPg_exchang_rate_dt();
String pg_tax_amt = gx_remittance.getPg_tax_amt();
String pg_tax_struct_cd=gx_remittance.getPg_tax_struct_cd();
String pg_tax_struct_dt=gx_remittance.getPg_tax_struct_dt();
String pg_tax_info = gx_remittance.getPg_tax_info();
String pg_invoice_seq=gx_remittance.getPg_invoice_seq();
String pg_invoice_no=gx_remittance.getPg_invoice_no();
String pg_sys_invoice_no=gx_remittance.getPg_sys_invoice_no();
String pg_invoice_amt = gx_remittance.getPg_invoice_amt();
String pg_net_payable = gx_remittance.getPg_net_payable();
String pg_invoice_dt = gx_remittance.getPg_invoice_dt();
String pg_invoice_due_dt = gx_remittance.getPg_invoice_due_dt();
String pg_invoice_check_flag = gx_remittance.getPg_invoice_check_flag();
String pg_invoice_check_dt = gx_remittance.getPg_invoice_check_dt();
String pg_invoice_check_by = gx_remittance.getPg_invoice_check_by();
String pg_invoice_check_nm = gx_remittance.getPg_invoice_check_nm();
String pg_invoice_auth_flag = gx_remittance.getPg_invoice_auth_flag();
String pg_invoice_auth_dt = gx_remittance.getPg_invoice_auth_dt();
String pg_invoice_auth_by = gx_remittance.getPg_invoice_auth_by();
String pg_invoice_auth_nm = gx_remittance.getPg_invoice_auth_nm();
String pg_invoice_aprv_flag = gx_remittance.getPg_invoice_aprv_flag();
String pg_invoice_aprv_dt = gx_remittance.getPg_invoice_aprv_dt();
String pg_invoice_aprv_by = gx_remittance.getPg_invoice_aprv_by();
String pg_invoice_aprv_nm = gx_remittance.getPg_invoice_aprv_nm();
String pg_invoice_adj_sign = gx_remittance.getPg_invoice_adj_sign();
String pg_invoice_adj_amt = gx_remittance.getPg_invoice_adj_amt();
String pg_financial_year = gx_remittance.getPg_financial_year();
String pg_tds_factor = gx_remittance.getPg_tds_factor();
String pg_tds_amount = gx_remittance.getPg_tds_amount();
String pg_tds_struct_cd=gx_remittance.getPg_tds_struct_cd();
String pg_tds_struct_dt=gx_remittance.getPg_tds_struct_dt();

Vector VGX_BU_CONTACT_PERSON = gx_remittance.getVGX_BU_CONTACT_PERSON();
Vector VGX_BU_CONTACT_PERSON_CD = gx_remittance.getVGX_BU_CONTACT_PERSON_CD();
Vector VBU_CONTACT_PERSON = gx_remittance.getVBU_CONTACT_PERSON();
Vector VBU_CONTACT_PERSON_CD = gx_remittance.getVBU_CONTACT_PERSON_CD();

Vector VSG_MULTI_TAX_STRUCT = gx_remittance.getVSG_MULTI_TAX_STRUCT();
Vector VPG_MULTI_TAX_STRUCT = gx_remittance.getVPG_MULTI_TAX_STRUCT();

int len_multi_tax=0;

boolean sg_submission_chk = gx_remittance.getSg_submission_chk();
boolean pg_submission_chk = gx_remittance.getPg_submission_chk();
%>
<body onload="enableSystemGen(true);enablePartyGen(false);difference();copyInvoiceNo();<%if(!msg.equals("")){%>refreshParent();<%}%>"
	<%if(!activity_type.equals("PREPARE")) {%>style="pointer-events: none;"<%} %>
>

<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_Gx_Invoice">

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
					    	Transaction Charge Remittance Detail
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
											<th colspan="3">GX Detail</th>
											<%if(contract_type.equals("X")){ %>
											<th rowspan="2">Customer</th>
											<%}else if(contract_type.equals("I")) { %>
											<th rowspan="2">Trader</th>
											<%} %>
											<th rowspan="2">Contract No<br>[Contract/Trade Ref#]</th>
											<th colspan="2"><%=owner_abbr%> Detail</th>
										</tr>
										<tr>
											<th>GX</th>
											<th>GX Business Unit</th>
											<th>Contact Person<span class="s-red">*</span></th>
											<th>Business Unit</th>
											<th>Contact Person<span class="s-red">*</span></th>
											
										</tr>
									</thead>
									<tbody>
										<tr>
											<td align="center">
												<%=gx_couterpty_abbr%>
											</td>
											<td align="center"><%=gx_bu_plant_abbr%></td>
											<td align="center">
												<div style="width:150px;">
													<select class="form-select form-select-sm" name="gx_bu_contact_person">
														<option value="0">--Select--</option>
														<%for(int i=0;i<VGX_BU_CONTACT_PERSON_CD.size();i++){ %>
														<option value="<%=VGX_BU_CONTACT_PERSON_CD.elementAt(i)%>"><%=VGX_BU_CONTACT_PERSON.elementAt(i)%></option>
														<%} %>
													</select>
													<script>document.forms[0].gx_bu_contact_person.value="<%=gx_bu_contact_person_cd%>"</script>
												</div>
											</td>
											<td align="center"><%=couterpty_abbr%></td>
											<td align="center"><%=deal_no%></td>
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
													<input type="radio" name="gx_invoice_type" value="S" checked onclick="doEnabled(this);">&nbsp;System Generated
												</span>
											</th>
											<th>
												<span class="badge rounded-pill" style="background:skyblue;color:black;font-size:12px;">
													<input type="radio" name="gx_invoice_type" value="P" onclick="doEnabled(this);" <%if(!invoice_check_flag.equals("Y")){ %>disabled<%} %>>&nbsp;Party Generated
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
										<tr>
											<td><b>Contractual Quantity</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_alloc_qty" value="<%=qty_mmbtu%>" readOnly>
														<span class="input-group-text">MMBTU</span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="party_alloc_qty" value="<%=pg_qty_mmbtu%>"
														onkeyup=" partyGenCalc();difference();checkNumber1(this,12,2);">
														<span class="input-group-text">MMBTU</span>
													</div>
												</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><b>Transaction Fee<span class="s-red">*</span></b></td>
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
														<input type="text" class="form-control form-control-sm" name="party_gross_amt" value="<%=pg_gross_amt%>" readOnly 
														onkeyup=" partyGenCalc();difference();checkNumber1(this,11,2);">
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
														<input type="text" class="form-control form-control-sm" name="party_gross_amt1" value="<%=pg_gross_amt1%>" readOnly 
														onkeyup=" partyGenCalc();difference();checkNumber1(this,11,2);">
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
														onkeyup="partyGenCalc();difference();checkNumber1(this,14,2);">
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
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
														onkeyup="partyGenCalc();difference();checkNumber1(this,14,2);">
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

<input type="hidden" name="option" value="GX_TXN_INVOICE">
<input type="hidden" name="opration" value="INSERT">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="gx_counterparty_cd" value="<%=gx_counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" value="<%=agmt_rev%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev" value="<%=cont_rev%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="gx_bu_unit" value="<%=gx_bu_unit%>">
<input type="hidden" name="bu_unit" value="<%=bu_unit%>">
<input type="hidden" name="invoice_type" value="<%=invoice_type%>">

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

</form>
</body>
</html>