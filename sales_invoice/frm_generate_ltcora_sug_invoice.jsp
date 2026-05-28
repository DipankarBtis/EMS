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
	
	var exchng_cd = document.forms[0].exchng_cd.value;
	var exchng_rate_mapp = document.forms[0].exchng_rate_mapp.value
	
	var alloc_qty = document.forms[0].alloc_qty.value;
	var price = document.forms[0].price.value
	var inv_flag = document.forms[0].inv_flag.value;
	
	var bu_state_tin = document.forms[0].bu_state_tin.value;
	var exist_financial_year = document.forms[0].exist_financial_year.value;
	
	var accroid = document.forms[0].accroid.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_generate_ltcora_sug_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
		"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&cargo_no="+cargo_no+
		"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&inv_dt="+invoice_dt+
		"&operation="+operation+"&sel_exchng_cd="+exchng_cd+
		"&exist_financial_year="+exist_financial_year+"&bu_state_tin="+bu_state_tin+"&temp_period_start_dt="+temp_period_start_dt+"&temp_period_end_dt="+temp_period_end_dt+
		"&allocQty="+alloc_qty+"&price="+price+"&inv_flag="+inv_flag+"&exchng_rate_mapp="+exchng_rate_mapp+"&accroid="+accroid+"&u="+u;
	
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
	//var gross_amt = document.forms[0].gross_amt.value
	//var exchng_rate = document.forms[0].exchng_rate.value
	var gross_amt1 = document.forms[0].gross_amt1.value
	var tax_amt = document.forms[0].tax_amt.value
	var invoice_amt = document.forms[0].invoice_amt.value
	var net_payable = document.forms[0].net_payable.value
	
	var agmt_base = document.forms[0].agmt_base.value;
	
	var tcs_tds = document.forms[0].tcs_tds.value;
	var tcs_struct_cd = document.forms[0].tcs_struct_cd.value;
	var tds_struct_cd = document.forms[0].tds_struct_cd.value;
	
	var plant_gstin_no = document.forms[0].plant_gstin_no.value;
	var bu_gstin_no = document.forms[0].bu_gstin_no.value;
	
	var sug_percent = document.forms[0].sug_percent.value
	var sug_qty = document.forms[0].sug_qty.value
	
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
			msg+="Invoice Due Date should be grater or equal Invoice Date!";
			flag=false;
		}
	}
	
	if(trim(alloc_qty)!="")
	{
		if(parseFloat(alloc_qty) <= 0)
		{
			msg+="Unloaded Qty should be > ZERO(0)!\n";
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
	
	if(trim(alloc_qty)=="")
	{
		msg+="Enter Unloaded Qty!\n";
		flag=false;
	}
	if(trim(price)=="")
	{
		msg+="Enter Confirmed Price!\n";
		flag=false;
	}
	if(trim(sug_percent)=="")
	{
		msg+="SUG Percentage Missing!\n";
		flag=false;
	}
	if(trim(sug_qty)=="")
	{
		msg+="SUG Qty Missing!\n";
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
	var sub_tax_amt = document.forms[0].sub_tax_amt
	var tax_comp_count=parseFloat(0);
	if(sub_tax_amt!=null && sub_tax_amt!=undefined)
	{
		if(sub_tax_amt.length!=undefined)
		{
			for(var i=0;i<sub_tax_amt.length;i++)
			{
				if(trim(sub_tax_amt[i].value)=="")
				{
					tax_comp_count++;
				}
			}
		}
		else
		{
			if(trim(sub_tax_amt.value)=="")
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
	
	if(flag)
	{
		var a=confirm("Do you want to Submit LTCORA SUG Invoice?");
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

function taxCalc(input)
{
	var tax_amt = document.forms[0].tax_amt
	var invoice_amt = document.forms[0].invoice_amt
	var net_payable = document.forms[0].net_payable
	
	var sub_tax_amt = document.forms[0].sub_tax_amt
	
	var total_tax=parseFloat(0);
	
	if(sub_tax_amt!=null && sub_tax_amt!=undefined)
	{
		if(sub_tax_amt.length!=undefined)
		{
			for(var i=0;i<sub_tax_amt.length;i++)
			{
				if(trim(sub_tax_amt[i].value)!="")
				{
					var temp = parseFloat(sub_tax_amt[i].value)
					if(!isNaN(temp))
					{
						total_tax+=parseFloat(temp);
					}
				}
			}
		}
		else
		{

			if(input=="tax" && trim(tax_amt.value)!="")
			{
				var temp = parseFloat(tax_amt.value)
				if(!isNaN(temp))
				{
					total_tax=parseFloat(temp);
					sub_tax_amt.value=round(parseFloat(total_tax),2);
				}
			}
		}
	}
	
	if(input=="")
	{
		tax_amt.value=round(parseFloat(total_tax),2);
	}
	invoice_amt.value=tax_amt.value
	net_payable.value=invoice_amt.value
	
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
String allocQty=request.getParameter("allocQty")==null?"":request.getParameter("allocQty");
String temp_price=request.getParameter("price")==null?"":request.getParameter("price");

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
sales_inv.setAllocQty(allocQty);
sales_inv.setTemp_price(temp_price);
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
String sug_percentage=sales_inv.getSug_percentage();
String sug_qty=sales_inv.getSug_qty();

String plant_gstin_no = sales_inv.getPlant_gstin_no();
String bu_gstin_no = sales_inv.getBu_gstin_no();

String remark_1 =sales_inv.getRemark_1();
String remark_2 =sales_inv.getRemark_2();

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
<body onload="<%if(!msg.equals("")){ %>Do_close('<%=msg%>','<%=msg_type%>','<%=accroid%>');<%} %> <%if(price_cd.equals("2")){ %>checkExchngRate();<%}%>"
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
					    	LTCORA(Sell) SUG Invoice Detail
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
									<tbody>
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
											<td><b>Unloaded Qty<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="alloc_qty" value="<%=qty_mmbtu%>" 
														onblur="negNumber(this);checkNumber1(this,12,2);refresh();">
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
														<input type="text" class="form-control form-control-sm" name="price" value="<%=price%>" 
														onblur="negNumber(this);checkNumber1(this,6,2);refresh();">
														<span class="input-group-text"><%=price_cd_nm%>/MMBTU</span>
													</div>
													<input type="hidden" class="form-control form-control-sm" name="price_cd" value="<%=price_cd%>">
												</div>
											</td>
										</tr>
										<tr>
											<td><b>SUG Percentage<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sug_percent" value="<%=sug_percentage%>" readOnly>
														<span class="input-group-text">%</span>
													</div>
												</div>
											</td>
										</tr>
										<tr>
											<td><b>SUG Quantity<span class="s-red">*</span></b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="sug_qty" value="<%=sug_qty%>" readOnly>
														<span class="input-group-text">MMBTU</span>
													</div>
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
										<tr>
											<td><b>Tax (<%=tax_struct_dtl%>)</b></td>
											<td align="center">
												<div style="width:200px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" name="tax_amt" value="<%=tax_amt%>" title="<%=tax_info%>" 
														onblur="negNumber(this);checkNumber1(this,11,2);taxCalc('tax');">
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
																	<input type="text" class="form-control form-control-sm" name="sub_tax_amt" value="<%=((Vector) temp.elementAt(2)).elementAt(j)%>" 
																	onblur="negNumber(this);checkNumber1(this,11,2);taxCalc('');">
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
<input type="hidden" name="billing_cycle" value="<%=billing_cycle%>">
<input type="hidden" name="bu_unit" value="<%=bu_unit%>">
<input type="hidden" name="bu_state_tin" value="<%=bu_state_tin%>">
<%-- <input type="hidden" name="financial_year" value="<%=financial_year%>"> --%>
<input type="hidden" name="inv_flag" value="<%=inv_flag%>">

<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="exist_financial_year" value="<%=exist_financial_year%>">

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