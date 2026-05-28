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
	
	var sys_alloc_qty = document.forms[0].sys_alloc_qty.value;
	var sys_price = document.forms[0].sys_price.value;
	
	var contact_person = document.forms[0].contact_person.value;
	var bu_contact_person = document.forms[0].bu_contact_person.value;
	var sys_invoice_no = document.forms[0].sys_invoice_no.value;
	var sys_invoice_dt = document.forms[0].sys_invoice_dt.value;
	var sys_invoice_due_dt = document.forms[0].sys_invoice_due_dt.value;
	
	var activity_type = document.forms[0].activity_type.value;
	
	var accroid = document.forms[0].accroid.value;
	var u = document.forms[0].u.value;
	
	var url = "frm_prepare_sug_payment.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cargo_no="+cargo_no+"&boe_no="+boe_no+"&cont_no="+cont_no+
		"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_start_dt+
		"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&activity_type="+activity_type+
		"&qty_mmbtu="+qty_mmbtu+"&invoice_dt="+sys_invoice_dt+"&invoice_due_dt="+sys_invoice_due_dt+"&inv_flag="+inv_flag+
		"&allocQty="+sys_alloc_qty+"&price="+sys_price+"&temp_bu_cont_person="+bu_contact_person+"&temp_cont_person="+contact_person+"&temp_inv_no="+sys_invoice_no+
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
	
	var contact_person = document.forms[0].contact_person.value;
	var bu_contact_person = document.forms[0].bu_contact_person.value;
	var sys_invoice_no = document.forms[0].sys_invoice_no.value;
	var sys_invoice_dt = document.forms[0].sys_invoice_dt.value;
	var sys_invoice_due_dt = document.forms[0].sys_invoice_due_dt.value;
	
	var sys_alloc_qty = document.forms[0].sys_alloc_qty.value;
	var sys_price = document.forms[0].sys_price.value;
	
	var activity_type = document.forms[0].activity_type.value;
	
	var accroid = document.forms[0].accroid.value;
	var u = document.forms[0].u.value;
	
	var url = "frm_prepare_sug_payment.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cargo_no="+cargo_no+"&boe_no="+boe_no+"&cont_no="+cont_no+
		"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_start_dt+
		"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&refresh_flg=Y&activity_type="+activity_type+
		"&qty_mmbtu="+qty_mmbtu+"&invoice_dt="+sys_invoice_dt+"&invoice_due_dt="+sys_invoice_due_dt+"&inv_flag="+inv_flag+
		"&allocQty="+sys_alloc_qty+"&price="+sys_price+"&temp_bu_cont_person="+bu_contact_person+"&temp_cont_person="+contact_person+"&temp_inv_no="+sys_invoice_no+
		"&accroid="+accroid+"&u="+u;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

enableButton=true;
function doSubmit()
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
	var sys_tds_amt = document.forms[0].sys_tds_amt.value
	var sys_tds_factor = document.forms[0].sys_tds_factor.value
	
	var sys_sug_percent = document.forms[0].sys_sug_percent.value
	var sys_sug_qty = document.forms[0].sys_sug_qty.value
	
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
	if(trim(sys_alloc_qty)=="")
	{
		msg+="Enter Unloaded Qty!\n";
		flag=false;
	}
	if(trim(sys_price)=="")
	{
		msg+="Enter Confirmed Price!\n";
		flag=false;
	}
	
	if(trim(sys_sug_percent)=="")
	{
		msg+="SUG Percentage Missing!\n";
		flag=false;
	}
	if(trim(sys_sug_qty)=="")
	{
		msg+="SUG Qty Missing!\n";
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
	
	var sys_sub_tax_amt = document.forms[0].sys_sub_tax_amt
	var tax_comp_count=parseFloat(0);
	if(sys_sub_tax_amt!=null && sys_sub_tax_amt!=undefined)
	{
		if(sys_sub_tax_amt.length!=undefined)
		{
			for(var i=0;i<sys_sub_tax_amt.length;i++)
			{
				if(trim(sys_sub_tax_amt[i].value)=="")
				{
					tax_comp_count++;
				}
			}
		}
		else
		{
			if(trim(sys_sub_tax_amt.value)=="")
			{
				tax_comp_count++;
			}
		}
	}
	
	if(tax_comp_count > 0)
	{
		msg+="Tax Componet Should not be Blank!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a=confirm("Do you want to Submit System Generated Invoice?");
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
	if(inv_type=="P")
	{
		adj_plusmin= document.forms[0].party_adj_plusmin.value
	}
	else
	{
		adj_plusmin= document.forms[0].sys_adj_plusmin.value
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
		
		var msg_flag=true;
		
		if(flag=="S")
		{
			if(!sys_chk[0].checked && !sys_chk[1].checked)
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

function InvApproval(aprv_flag)
{
	var approve_access = document.forms[0].approve_access.value;
	
	var final_aprv = document.forms[0].final_aprv;
	
	if(approve_access=="Y")
	{	
		if(!final_aprv.checked)
		{
			alert("Check System Generated!")
		}
		else
		{
			var inv_nm="";
			if(final_aprv.checked)
			{
				inv_nm="System Generated";
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

function refreshParent(accroid)
{
	window.opener.refresh(accroid);
}

function taxCalc(input)
{
	var sys_tax_amt = document.forms[0].sys_tax_amt
	var sys_invoice_amt = document.forms[0].sys_invoice_amt
	var sys_net_payable = document.forms[0].sys_net_payable
	
	var sys_sub_tax_amt = document.forms[0].sys_sub_tax_amt
	
	var total_tax=parseFloat(0);
	
	if(sys_sub_tax_amt!=null && sys_sub_tax_amt!=undefined)
	{
		if(sys_sub_tax_amt.length!=undefined)
		{
			for(var i=0;i<sys_sub_tax_amt.length;i++)
			{
				if(trim(sys_sub_tax_amt[i].value)!="")
				{
					var temp = parseFloat(sys_sub_tax_amt[i].value)
					if(!isNaN(temp))
					{
						total_tax+=parseFloat(temp);
					}
				}
			}
		}
		else
		{

			if(input=="tax" && trim(sys_tax_amt.value)!="")
			{
				var temp = parseFloat(sys_tax_amt.value)
				if(!isNaN(temp))
				{
					total_tax=parseFloat(temp);
					sys_sub_tax_amt.value=round(parseFloat(total_tax),2);
				}
			}
		}
	}
	
	if(input=="")
	{
		sys_tax_amt.value=round(parseFloat(total_tax),2);
	}
	sys_invoice_amt.value=sys_tax_amt.value
	sys_net_payable.value=sys_invoice_amt.value
	
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

String allocQty=request.getParameter("allocQty")==null?"":request.getParameter("allocQty");
String temp_price=request.getParameter("price")==null?"":request.getParameter("price");

String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String refresh_flg =request.getParameter("refresh_flg")==null?"":request.getParameter("refresh_flg");
String activity_type =request.getParameter("activity_type")==null?"":request.getParameter("activity_type");

String temp_bu_cont_person_cd = request.getParameter("temp_bu_cont_person")==null?"":request.getParameter("temp_bu_cont_person");
String temp_cont_person_cd = request.getParameter("temp_cont_person")==null?"":request.getParameter("temp_cont_person");
String temp_inv_no = request.getParameter("temp_inv_no")==null?"":request.getParameter("temp_inv_no");

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
remittance.setAllocQty(allocQty);
remittance.setTemp_price(temp_price);
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
String sug_percentage=remittance.getSug_percentage();
String sug_qty=remittance.getSug_qty();

String qty_mmbtu = remittance.getQty_mmbtu();

boolean sg_submission_chk = remittance.getSg_submission_chk();

Vector VSEL_BU_CD = remittance.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = remittance.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_BU_PLANT_ABBR = remittance.getVSEL_BU_PLANT_ABBR();
Vector VCONTACT_PERSON = remittance.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = remittance.getVCONTACT_PERSON_CD();
Vector VBU_CONTACT_PERSON = remittance.getVBU_CONTACT_PERSON();
Vector VBU_CONTACT_PERSON_CD = remittance.getVBU_CONTACT_PERSON_CD();

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

String submitted_fiscal_yr = remittance.getSubmitted_fiscal_yr();

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

if(temp_bu_cont_person_cd.equals("")){
	temp_bu_cont_person_cd=bu_contact_person_cd;
}
if(temp_cont_person_cd.equals("")){
	temp_cont_person_cd=contact_person_cd;
}
if(temp_inv_no.equals("")){
	temp_inv_no=invoice_no;
}
%>
<body onload="<%if(!msg.equals("")){%>refreshParent('<%=accroid%>');<%}%>"
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
					    Purchase LTCORA SUG Remittance Detail
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
											<th rowspan="2">Contract No<br>[Contract/Trade Ref#]</th>
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
													<script>document.forms[0].contact_person.value="<%=temp_cont_person_cd%>"</script>
												</div>
											</td>
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
													<script>document.forms[0].bu_contact_person.value="<%=temp_bu_cont_person_cd%>"</script>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<%if(!bu_unit.equals("0")){ %>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover" id="inv_difference">
									<tbody>
										<tr>
											<td><b><%=inv_lineitem%>#<span class="s-red">*</span><font color="blue">(Party Generated)</font></b></td>
											<td align="center">
												<div style="width:250px;">
													<input type="text" class="form-control form-control-sm" name="sys_invoice_no" value="<%=temp_inv_no%>" onblur="copyInvoiceNo();" style="color:blue;">
													<input type="hidden" name="sys_invoice_seq" value="<%=invoice_seq%>">
													<input type="hidden" name="sys_financial_year" value="<%=financial_year%>">
													<input type="hidden" name="system_invoice_no" value="<%=sys_invoice_no%>">
												</div>
											</td>
										</tr>
										<tr>
											<td><b><%=inv_lineitem%> Date<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="sys_invoice_dt" value="<%=invoice_dt%>" maxLength="10" 
							      						onchange="validateDate(this);refresh1();" autocomplete="off"><!-- refresh(); -->
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
					      						</div>
											</td>
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
										</tr>
										<tr>
											<td><b>Unloaded Quantity<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_alloc_qty" value="<%=qty_mmbtu%>" 
														onblur="negNumber(this);checkNumber1(this,12,2);refresh();">
														<span class="input-group-text"><%=energy_unit_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="sys_qty_unit" value="<%=energy_unit%>" readOnly>
												</div>
											</td>
										</tr>
										<tr>
											<td><b>Confirmed Price<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_price" value="<%=price%>" 
														onblur="negNumber(this);checkNumber1(this,6,2);refresh();">
														<span class="input-group-text"><%=price_cd_nm%>/<%=energy_unit_nm%></span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="sys_price_cd" value="<%=price_cd%>">
												</div>
											</td>
										</tr>
										<tr>
											<td><b>SUG Percentage<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_sug_percent" value="<%=sug_percentage%>" readOnly>
														<span class="input-group-text">%</span>
													</div>
												</div>
											</td>
										</tr>
										<tr>
											<td><b>SUG Quantity<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_sug_qty" value="<%=sug_qty%>" readOnly>
														<span class="input-group-text"><%=energy_unit_nm%></span>
													</div>
												</div>
											</td>
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
										</tr>
										<tr <%if(!price_cd.equals(invoice_raised_in)){ %><%}else{ %>style="display:none;"<%} %>>
											<td><b><%=exchg_lineitem%> <font color="blue">as on <%=exchang_rate_dt%></font></b></td>
											<td align="center">
												<div style="width:250px;">
													<input type="text" class="form-control form-control-sm" name="sys_exchng_rate" value="<%=exchang_rate%>" readOnly>
													<input type="hidden" name="sys_exchng_cd" value="<%=exchng_rate_cd%>">
													<input type="hidden" name="sys_exchng_dt" value="<%=exchang_rate_dt%>"> 
												</div>
											</td>
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
										</tr>
										<tr>
											<td><b><%=tax_lineitem%> (<%=tax_struct_dtl%>)</b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sys_tax_amt" value="<%=tax_amt%>" title="<%=tax_info%>" 
														onblur="negNumber(this);checkNumber1(this,14,2);taxCalc('tax');">
														<span class="input-group-text"><%=invoice_raised_in_nm%></span>
													</div>
													<input type="hidden" name="sys_tax_cd" value="<%=tax_struct_cd%>">
													<input type="hidden" name="sys_tax_dt" value="<%=tax_struct_dt%>">
													<input type="hidden" name="sys_tax_factor" value="<%=tax_factor%>">
													<input type="hidden" name="sys_tax_struct_dtl" value="<%=tax_struct_dtl%>">
												</div>
											</td>
										</tr>
										<%if(VSG_MULTI_TAX_STRUCT.size()>0){
											for(int i=0;i<VSG_MULTI_TAX_STRUCT.size();i++)
											{
												Vector temp =(Vector)((Vector)((Vector)VSG_MULTI_TAX_STRUCT.elementAt(i)));
												
												len_multi_tax=((Vector) temp.elementAt(0)).size();
												for(int j=0;j<((Vector) temp.elementAt(0)).size(); j++)
												{%>
													<tr style="background: #cff4fc;<%if(((Vector) temp.elementAt(0)).size()==1){%>display:none;<%} %>" >
														<td align="right"><b><%=((Vector) temp.elementAt(1)).elementAt(j)%></b></td>
														<td align="center">
															<div style="width:250px;">
																<div class="input-group input-group-sm" >
																	<input type="text" class="form-control form-control-sm" name="sys_sub_tax_amt" 
																		value="<%=((Vector) temp.elementAt(2)).elementAt(j)%>" onblur="negNumber(this);checkNumber1(this,14,2);taxCalc('');">
																	<span class="input-group-text"><%=invoice_raised_in_nm%></span>
																</div>
																<input type="hidden" name="sys_sub_tax_code" value="<%=((Vector) temp.elementAt(0)).elementAt(j)%>">
																<input type="hidden" name="sys_sub_tax_struct" value="<%=((Vector) temp.elementAt(1)).elementAt(j)%>">
																<input type="hidden" name="sys_sub_tax_base_amt" value="<%=((Vector) temp.elementAt(3)).elementAt(j)%>">
															</div>
														</td>
													</tr>
												<%}
											}
										} %>
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
										</tr>
										
										<tr style="<%if(!activity_type.equals("PREPARE")){%>display:none;<%} %>">
											<td><b>View/Submit <%=inv_lineitem%></b></td>
											<td align="center">
												<div style="width:250px;">
													<div class="d-flex justify-content-between">
														<i class="fa fa-refresh fa-2x" style="color:blue;" onclick="refresh1();" id="sys_ref_icon"></i>
														<i class="fa fa-eye fa-2x" id="sys_view" onclick="viewBeforeSubmit('S');"></i>
														<%if(write_access.equals("Y")){ %>
														<input type="button" class="btn btn-warning com-btn" id="sys_submit" value="Submit" onclick="doSubmit('S');">
														<%}else{ %>
														<input type="button" class="btn btn-warning com-btn" id="sys_submit" value="Submit" disabled>
														<%} %>
														<!-- <input type="button" class="btn btn-warning com-btn" id="sys_view" value="View" onclick="printPDF('S');"> -->
													</div>
												</div>
											</td>
										</tr>
										<tr style="<%if(activity_type.equals("PREPARE")){%>display:none;<%}else{ %>pointer-events: auto;<%}%>">
											<td><b>View <%=inv_lineitem%></b></td>
											<td align="center">
												<i class="fa fa-eye fa-2x" onclick="viewBeforeSubmit('S');"></i>
											</td>
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
															<input type="button" class="btn btn-warning com-btn" id="sys_auth_sub" value="Submit" onclick="authorizeInvoice('S');" <%if(invoice_aprv_flag.equals("A")){ %>disabled<%} %>>
														</div>
													</div>
												</div>
												<%if(!invoice_auth_flag.equals("")){ %>
													<font color="<%if(invoice_auth_flag.equals("Y")){ %>green<%}else{%>red<%}%>"><%=invoice_auth_dt%> <%=invoice_auth_nm%> by <%=invoice_auth_by%></font>
												<%} %>
											</td>
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
				    				<br>
				      				<%if(!invoice_aprv_flag.equals("")){ %>
										<font color="<%if(invoice_aprv_flag.equals("A")){ %>green<%}else{%>red<%}%>"><%=invoice_aprv_dt%> <%=invoice_aprv_nm%> by <%=invoice_aprv_by%></font>
									<%} %>
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-6 col-xs-6 col-md-6">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12"> 
				    				<input type="button" class="btn btn-warning com-btn" value="Approve" onclick="InvApproval('A');" <%if(!invoice_auth_flag.equals("Y")){%>disabled<%}%>>
				    				<input type="button" class="btn btn-warning com-btn" value="Reject" onclick="InvApproval('R');" <%if(!invoice_auth_flag.equals("Y")){%>disabled<%}%>>
				    			</div>
				    		</div>
				    	</div>
					</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div align="center">
						<%=utilmsg.infoMessage("<b>Auto roundoff ensured for Price lines</b>") %>
					</div>
				</div>
				<%} %>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="TRADER_INVOICE">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="invoice_type" value="S">

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

<input type="hidden" name="inv_aprv_flag" value="">

<input type="hidden" name="sg_submission_chk" value="<%=sg_submission_chk%>">

<input type="hidden" name="activity_type" value="<%=activity_type%>">

<input type="hidden" name="invoice_aprv_flag" value="<%=invoice_aprv_flag%>">
<input type="hidden" name="qty_mmbtu" value="<%=qty_mmbtu%>">

<input type="hidden" name="applicable_abbr" value="<%=applicable_abbr%>">
<input type="hidden" name="applicable_flag" value="<%=applicable_flag%>">

<input type="hidden" name="sys_tds_amt" value="<%=tds_amount%>">
<input type="hidden" name="sys_tds_factor" value="<%=tds_factor%>">
<input type="hidden" name="sys_tds_cd" value="<%=tds_struct_cd%>">
<input type="hidden" name="sys_tds_dt" value="<%=tds_struct_dt%>">

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