<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh(opration)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var gas_dt = document.forms[0].gas_dt.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var prev_month = document.forms[0].prev_month.value;
	var prev_year = document.forms[0].prev_year.value;
	
	var billing_cycle = document.forms[0].billing_cycle.value;
	var prev_billing_cycle = document.forms[0].prev_billing_cycle.value;
	
	var period_start_dt=document.forms[0].period_start_dt.value;
	var period_end_dt=document.forms[0].period_end_dt.value;
	
	var mapping_id = document.forms[0].mapping_id.value;
	var filter_cont_type= document.forms[0].filter_cont_type.value;
	var address_type = document.forms[0].address_type.value;
	var bu_unit = document.forms[0].bu_unit.value;
	var contact_person = document.forms[0].contact_person.value;
	var bu_contact_person = document.forms[0].bu_contact_person.value;
	var financial_year = document.forms[0].financial_year.value;
	var bu_state_tin = document.forms[0].bu_state_tin.value;
	
	var accroid = document.forms[0].accroid.value;
	
	var invoice_category="";
	if (document.forms[0].invoice_category[0].checked)
	{
		invoice_category= document.forms[0].invoice_category[0].value;
	}
	else if (document.forms[0].invoice_category[1].checked)
	{
		invoice_category= document.forms[0].invoice_category[1].value;
	}
	
	var invoice_type= document.forms[0].invoice_type.value;
	
	var activity_type = document.forms[0].activity_type.value;
	var tcs_tds = document.forms[0].tcs_tds.value;
	
	var msg="";
	var flag = true;
	if(prev_billing_cycle == billing_cycle)
	{
		if(billing_cycle == "8")
		{
			if(trim(period_start_dt)=="" || trim(period_end_dt)=="")
			{
				msg="Enter Billing Period!";
				flag=false;
			}
		}
	}
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "frm_dlng_f_flow_invoice.jsp?u="+u+"&counterparty_cd="+counterparty_cd+"&gas_dt="+gas_dt+
				"&month="+month+"&year="+year+"&billing_cycle="+billing_cycle;
		if(prev_month!=month || prev_year!=year)
		{
		}
		else
		{
			url+="&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt;
		}
		
		url+="&mapping_id="+mapping_id+
				"&filter_cont_type="+filter_cont_type+"&activity_type="+activity_type+
				"&address_type="+address_type+"&bu_unit="+bu_unit+
				"&invoice_category="+invoice_category+"&invoice_type="+invoice_type+"&opration="+opration+
				"&tcs_tds="+tcs_tds+
				"&contact_person="+contact_person+"&bu_contact_person="+bu_contact_person+"&accroid="+accroid
				+"&financial="+financial_year+"&bu_state_tin="+bu_state_tin;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}

function checkStartEndDate(flag)
{
	var period_start_dt=document.forms[0].period_start_dt;
	var period_end_dt=document.forms[0].period_end_dt;
	
	var obj = period_start_dt;
	var obj1 = period_end_dt;
	
	if((obj.value!="" && trim(obj.value) != "" && obj.value != null) && (obj1.value!="" && trim(obj1.value) != "" && obj1.value != null))
	{
		if(flag=="F")
		{
			var count = compareDate(obj.value,obj1.value);
			if(parseInt(count) == 1)
			{
				alert("Billing Period Start Date should be less or equal Period End Date!")
				obj.value="";
				return false;
			}
		}
		else if(flag=="T")
		{
			var count = compareDate(obj.value,obj1.value);
			if(parseInt(count) == 1)
			{
				alert("Billing Period End Date should be grater or equal Period Start Date!")
				obj1.value="";
				return false;
			}
		}
	}
}

function addrow()
{
	//var max_seq=document.forms[0].no_of_line.value;
	//var tab_name = document.getElementById("itemTab");
	
	var max_seq = document.getElementById("item_size").value;
	var prev_minus= document.getElementById("minus"+max_seq);
	var prev_plus= document.getElementById("plus"+max_seq);
	var new_seq_no = parseInt(max_seq)+1;
	
	if(parseInt(new_seq_no) > 99)
	{
		alert("Maximum number of lines added for the Invoice!")	
	}
	else
	{
		var tab_name = document.getElementById("itemTab");
		var row_new = document.createElement("tr"); 
		row_new.id = 'row'+new_seq_no;
		
		var td01 = document.createElement("td");
		var div01 = document.createElement("DIV");
		div01.align='center';
		var input01 = document.createElement("input")
		input01.name = "item_seq";
		input01.id = "item_seq"+new_seq_no;
		input01.type = "text";
		input01.className="form-control form-control-sm";
		input01.maxlength = "2";
		//input01.readOnly = true;
		input01.value = new_seq_no;
		input01.style.width='50px';
		input01.setAttribute("onblur","checkNumber1(this,2,0);");

		div01.appendChild(input01);
		
		var td02 = document.createElement("td");
		var div02 = document.createElement("DIV");
		div02.align='center';
		var input02 = document.createElement("textarea")
		input02.name = "item_note";
		input02.id = "item_note"+new_seq_no;
		input02.type = "text";
		input02.className="form-control form-control-sm";
		input02.maxlength = "50";
		input02.value = "";
		input02.cols = "75";
		input02.rows = "1";
		input02.style.width='250px';
		
		div02.appendChild(input02);
		
		var td03 = document.createElement("td");
		var div03 = document.createElement("DIV");
		div03.align='center';
		var input03 = document.createElement("input");
		input03.name = "unit";
		input03.id = "unit"+new_seq_no;
		input03.type = "text";
		input03.className="form-control form-control-sm";
		input03.style.width='100px';
		input03.maxlength = "30";
	
		
		div03.appendChild(input03);
		
		var td04 = document.createElement("td");
		var div04 = document.createElement("DIV");
		div04.align='center';
		var input04 = document.createElement("input")
		input04.name = "qty";
		input04.id = "qty"+new_seq_no;
		input04.type = "text";
		input04.className="form-control form-control-sm";
		input04.style.width='100px';
		//input04.style.text-align='right';
		input04.maxlength = "10";
		
		div04.appendChild(input04);
		
		var td05 = document.createElement("td");
		var div05 = document.createElement("DIV");
		div05.align='center';
		var input05 = document.createElement("input")
		input05.name = "rate";
		input05.id = "rate"+new_seq_no;
		input05.type = "text";
		input05.className="form-control form-control-sm";
		input05.style.width='100px';
		//input05.style.text-align='right';
		input05.maxlength = "10";
		
		div05.appendChild(input05);
		
		var td06 = document.createElement("td");
		var div06 = document.createElement("DIV");
		div06.align='center';
		var input06 = document.createElement("input")
		input06.name = "amount";
		input06.id = "amount"+new_seq_no;
		input06.type = "text";
		input06.className="form-control form-control-sm";
		input06.style.width='100px';
		//input06.style.text-align='right';
		input06.maxlength = "20";
		
		div06.appendChild(input06);
		
		var td07 = document.createElement("td");
		td07.className="text-center"
		var input07 = document.createElement("a")
		input07.setAttribute("onclick","removeRow('"+row_new.id+"','"+new_seq_no+"');");
		input07.id = "minus"+new_seq_no;
		var i=document.createElement("i");
		i.className="fa fa-minus-circle fa-2x";
		
		var input07_1 = document.createElement("a")
		input07_1.setAttribute("onclick","addrow();");
		input07_1.id = "plus"+new_seq_no;
		var i_1=document.createElement("i");
		i_1.className="fa fa-plus-circle fa-2x";
		
		input07.appendChild(i);
		input07_1.appendChild(i_1);
		
		td01.appendChild(div01);
		td02.appendChild(div02);
		td03.appendChild(div03);
		td04.appendChild(div04);
		td05.appendChild(div05);
		td06.appendChild(div06);
		td07.appendChild(input07);
		td07.appendChild(document.createTextNode("      "))
		td07.appendChild(input07_1);
		
		row_new.appendChild(td01);
		row_new.appendChild(td02);
		row_new.appendChild(td03);
		row_new.appendChild(td04);
		row_new.appendChild(td05);
		row_new.appendChild(td06);
		row_new.appendChild(td07);
		
		tab_name.appendChild(row_new);
		
		document.forms[0].no_of_line.value=new_seq_no;
		document.getElementById("item_size").value=new_seq_no;
		
		if(parseInt(new_seq_no) > 1)
		{
			prev_minus.style.display="none";
			prev_plus.style.display="none";
		}
		else
		{
			prev_plus.style.display="none";
		}
	}
}

function removeRow(row_id, seq_no)
{
	var row_cnt = document.forms[0].item_size.value;
	
	if(parseInt(row_cnt) == parseInt(seq_no))
	{
		if((parseInt(seq_no)-1) > 1)
		{
			document.getElementById("minus"+(parseInt(seq_no)-1)).style.display="";
			document.getElementById("plus"+(parseInt(seq_no)-1)).style.display="";
		}
		else
		{
			document.getElementById("minus"+(parseInt(seq_no)-1)).style.display="none";
			document.getElementById("plus"+(parseInt(seq_no)-1)).style.display="";
		}
		
		if(parseFloat(row_cnt) > 0)
		{
			document.forms[0].item_size.value = parseFloat(row_cnt)-1;
			document.forms[0].no_of_line.value = parseFloat(row_cnt)-1;
		}
		var row = document.getElementById(row_id);
		row.parentNode.removeChild(row);
	}
	else
	{
		alert("Please First Remove Last Row!!");
	}
}

enableButton=true;
function doSubmit()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	
	var period_start_dt=document.forms[0].period_start_dt.value;
	var period_end_dt=document.forms[0].period_end_dt.value;
	
	var mapping_id = document.forms[0].mapping_id.value;
	var address_type = document.forms[0].address_type.value;
	var bu_unit = document.forms[0].bu_unit.value;
	var invoice_type= document.forms[0].invoice_type.value;
	var sub_invoice_type= document.forms[0].sub_invoice_type;

	
	var contact_person = document.forms[0].contact_person.value;
	var bu_contact_person = document.forms[0].bu_contact_person.value;
	
	var invoice_no = document.forms[0].invoice_no.value;
	var invoice_category = document.forms[0].invoice_category;
	
	var invoice_category_value="";
	var sac_no ="";
	
	if (document.forms[0].invoice_category[0].checked)
	{
		invoice_category_value= document.forms[0].invoice_category[0].value;
	}
	else if (document.forms[0].invoice_category[1].checked)
	{
		invoice_category_value= document.forms[0].invoice_category[1].value;
		sac_no = document.forms[0].sac_no.value;
	}

	var invoice_dt = document.forms[0].invoice_dt.value;
	var invoice_due_dt = document.forms[0].invoice_due_dt.value;
	
	var invoice_raised_in = document.forms[0].invoice_raised_in.value
	
	var tax_cd = document.forms[0].tax_cd.value
	var tax_amt = document.forms[0].tax_amt.value
	
	var gross_amt_inr = document.forms[0].gross_amt_inr.value;
	
	var invoice_amt = document.forms[0].invoice_amt.value;
	var net_amt = document.forms[0].net_amt.value;
	
	var item_seq = document.forms[0].item_seq;
	
	var tcs_tds = document.forms[0].tcs_tds.value;
	var alloc_qty = document.forms[0].alloc_qty.value;
	
	var msg="";
	var flag = true;
	
	if(trim(counterparty_cd)=="" || counterparty_cd == "0")
	{
		msg+="Select Counterparty!\n";
		flag=false;
	}
	if(trim(month)=="" || month == "0")
	{
		msg+="Select Month!\n";
		flag=false;
	}
	if(trim(year)=="" || year == "0")
	{
		msg+="Select Year!\n";
		flag=false;
	}
	if(trim(mapping_id)=="" || mapping_id == "0")
	{
		msg+="Select Contract!\n";
		flag=false;
	}
	if(trim(billing_cycle)=="" || billing_cycle == "0")
	{
		msg+="Select Billing Cycle!\n";
		flag=false;
	}
	if(trim(period_start_dt)=="")
	{
		msg+="Enter Period Start Date!\n";
		flag=false;
	}
	if(trim(period_end_dt)=="")
	{
		msg+="Enter Period End Date!\n";
		flag=false;
	}
	if(trim(address_type)=="" || address_type == "0")
	{
		msg+="Select Address Type!\n";
		flag=false;
	}
	if(trim(contact_person)=="" || contact_person == "0")
	{
		msg+="Select Contact Person!\n";
		flag=false;
	}
	if(trim(bu_unit)=="" || bu_unit == "0")
	{
		msg+="Select Business Unit!\n";
		flag=false;
	}
	if(trim(bu_contact_person)=="" || bu_contact_person == "0")
	{
		msg+="Select Business Contact Person!\n";
		flag=false;
	}
	if(!invoice_category[0].checked && !invoice_category[1].checked)
	{
		msg+="Select Invoice Category!\n";
		flag=false;
	}
	if(trim(invoice_type)=="" || invoice_type == "0")
	{
		msg+="Select Invoice Type!\n";
		flag=false;
	}
	//if(invoice_type == "OR")
	{
		var subject_line = document.forms[0].subject_line.value;
		if(trim(subject_line) == "")
		{
			//msg+="Enter Other Invoice String!\n";
			msg+="Enter Invoice Heading!\n";
			flag=false;
		}
	}
	/* if(trim(invoice_no)=="")
	{
		msg+="Select Invoice No!\n";
		flag=false;
	} */
	if(invoice_type == "CR" || invoice_type == "DR" || invoice_type == "CCR" || invoice_type == "CDR")
	{
		if(trim(sub_invoice_type.value)=="")
		{
			msg+="Select Sub Invoice Type!\n";
			flag=false;
		}
	}
	if(trim(invoice_dt)=="")
	{
		msg+="Select Invoice Date!\n";
		flag=false;
	}
	if(invoice_type != "CR" && invoice_type != "CCR")
	{
		if(trim(invoice_due_dt)=="")
		{
			msg+="Select Invoice Due Date!\n";
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
	}
	
	if(invoice_type != "LP")
	{
		if(trim(alloc_qty)=="")
		{
			msg+="Enter Invoiced MMBTU!\n";
			flag=false;
		}
	}
	
	if(trim(invoice_raised_in)=="" || invoice_raised_in=="0")
	{
		msg+="Select Invoice Raised In!\n";
		flag=false;
	}
	
	if (trim(gross_amt_inr)=="")
	{
		msg+="Enter Gross Amount (INR)!\n";
		flag=false;
	}		 
	
	if (trim(invoice_category_value)=="S" && (trim(sac_no)=="0" || trim(sac_no)==""))
	{
		msg+="Select SAC No!\n";
		flag=false;
	}		 
		
	
	if(trim(tax_cd)!="")
	{
		if(trim(tax_amt)=="")
		{
			msg+="Enter Tax Amount!\n";
			flag=false;
		}
		
		var sub_tax_amt = document.forms[0].sub_tax_amt;
		var sub_tax_struct = document.forms[0].sub_tax_struct;
		if(sub_tax_amt!=null && sub_tax_amt!=undefined)
		{
			if(sub_tax_amt.length!=undefined)
			{
				for(var i=0;i<sub_tax_amt.length;i++)
				{
					if(trim(sub_tax_amt[i].value)=="")
					{
						msg+="Enter Component Tax Amount for "+sub_tax_struct[i].value+"!\n";
						flag=false;
					}
				}
			}
			else
			{
				sub_tax_amt.value=tax_amt;
			}
		}
	}
	
	if(tcs_tds=="TDS")
	{
		var tds_cd = document.forms[0].tds_cd.value;
		var tds_dt = document.forms[0].tds_dt.value;
		var tds_amt = document.forms[0].tds_amt.value;
		
		if(trim(tds_cd)=="")
		{
			msg+="TDS tax structure is missing!\n";
			flag=false;
		}
		
		if(trim(tds_amt)=="")
		{
			msg+="TDS amount is missing!\n";
			flag=false;
		}
	}
	
	if(tcs_tds=="TCS")
	{
		var tcs_cd = document.forms[0].tcs_cd.value;
		var tcs_dt = document.forms[0].tcs_dt.value;
		var tcs_amt = document.forms[0].tcs_amt.value;
		
		if(trim(tcs_cd)=="")
		{
			msg+="TCS tax structure is missing!\n";
			flag=false;
		}
		
		if(trim(tcs_amt)=="")
		{
			msg+="TCS amount is missing!\n";
			flag=false;
		}
	}
	
	if(trim(invoice_amt)=="")
	{
		msg+="Enter Invoice Amount!\n";
		flag=false;
	}
	if(trim(net_amt)=="")
	{
		msg+="Enter Net Amount!\n";
		flag=false;
	}
	
	var chkFlag=false;
	
	if(item_seq!=null && item_seq!=undefined)
	{
		if(item_seq.length!=undefined)
		{
			for(var i=0;i<item_seq.length;i++)
			{
				if(item_seq[i].value=="")
				{
					msg+="Enter Item Seq# for ROW - "+(parseInt(i)+1)+"!\n";
					flag=false;		
				}
			}
		}
		else
		{
			if(item_seq.value=="")
			{
				msg+="Enter Item Seq# for ROW - 1!\n";
				flag=false;
			}
		}
		
		if(item_seq.length!=undefined)
		{
			for(var i=0;i<item_seq.length;i++)
			{
				for(var j=0;j<item_seq.length;j++)
				{
					if(i!=j)
					{
						if(item_seq[i].value==item_seq[j].value)
						{
							chkFlag=true;
							break;
						}
					}
				}
			}
		}
	}
	if(chkFlag)
	{
		msg+="Item Seq# should not be same, Re-check!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Submit?");
		if(a)
		{
			if(tcs_tds=="TDS")
			{
			}
			else
			{
				document.forms[0].tds_cd.value="";
				document.forms[0].tds_dt.value="";
				document.forms[0].tds_amt.value="";
			}
			
			if(tcs_tds=="TCS")
			{
			}
			else
			{
				document.forms[0].tcs_cd.value="";
				document.forms[0].tcs_dt.value="";
				document.forms[0].tcs_amt.value="";
			}
			
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

var newWindow;
function openInvoiceList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var url="rpt_pur_f_flow_invoice_list.jsp?counterparty_cd="+counterparty_cd+"&month="+month+"&year="+year;
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Invoice List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Invoice List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setInvoiceDetail(financial,inv_typ,inv_seq,inv_no,mapping_id,add_type,bu,freq,p_start_dt,p_end_dt)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var opration = document.forms[0].opration.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_f_flow_invoice.jsp?u="+u+"&counterparty_cd="+counterparty_cd+
			"&month="+month+"&year="+year+"&invoice_type="+inv_typ+"&opration="+opration+
			"&mapping_id="+mapping_id+"&address_type="+add_type+"&bu_unit="+bu+"&billing_cycle="+freq+
			"&period_start_dt="+p_start_dt+"&period_end_dt="+p_end_dt+"&inv_no="+inv_no+"&inv_seq="+inv_seq+"&financial="+financial;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function printPDF()
{
	var bu_unit = document.forms[0].bu_unit.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	var mapping_id = document.forms[0].mapping_id.value;
	var address_type = document.forms[0].address_type.value;
	var invoice_type = document.forms[0].invoice_type.value;
	var invoice_no = document.forms[0].invoice_no.value;
	var invoice_seq = document.forms[0].invoice_seq.value;
	var financial_year = document.forms[0].financial_year.value;
	
	var contract_type = document.forms[0].contract_type.value;
	var bu_state_tin = document.forms[0].bu_state_tin.value;
	
	var is_print="0";
	
	var url = "../dlng/pdf_dlng_f_flow_invoice.jsp?counterparty_cd="+counterparty_cd+"&is_print="+is_print+"&contract_type="+contract_type+
		"&invoice_type="+invoice_type+"&mapping_id="+mapping_id+"&address_type="+address_type+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+
		"&inv_no="+invoice_no+"&inv_seq="+invoice_seq+"&financial="+financial_year+"&bu_state_tin="+bu_state_tin;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow= window.open(url,"F-Flow Invoice","top=10,left=70,width=900,height=700,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow= window.open(url,"F-Flow Invoice","top=10,left=70,width=900,height=700,scrollbars=1");
	}
}

function checkInvoice()
{
	var check_access = document.forms[0].check_access.value;
	
	var chk = document.forms[0].chk;
	
	if(check_access=="Y")
	{
		var msg_flag=true;
		
		if(!chk[0].checked && !chk[1].checked)
		{
			alert("Check either YES or NO")
			msg_flag=false;
		}
		
		if(msg_flag)
		{
			var a=confirm("Do you want to Checked Invoice?");
			if(a)
			{
				document.forms[0].opration_type.value="CHECK";
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

function authorizeInvoice()
{
	var authorize_access = document.forms[0].authorize_access.value;
	
	var auth = document.forms[0].auth;
	
	if(authorize_access=="Y")
	{
		var msg_flag=true;
		
		if(!auth[0].checked && !auth[1].checked)
		{
			alert("Check either YES or NO")
			msg_flag=false;
		}
		
		if(msg_flag)
		{
			var a=confirm("Do you want to Authorize Invoice?");
			if(a)
			{
				document.forms[0].opration_type.value="AUTHORIZE";
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

function approveInvoice()
{
	var approve_access = document.forms[0].approve_access.value;
	
	var aprv = document.forms[0].aprv;
	
	if(approve_access=="Y")
	{
		var msg_flag=true;
		
		if(!aprv[0].checked && !aprv[1].checked)
		{
			alert("Check either YES or NO")
			msg_flag=false;
		}
		
		if(msg_flag)
		{
			var a=confirm("Do you want to Approve Invoice?");
			if(a)
			{
				document.forms[0].opration_type.value="APPROVE";
				document.getElementById("loading").style.visibility = "visible";
				editAllowedOnCpStatus = true;
				document.forms[0].submit();
			}
		}
	}
	else
	{
		alert("You do not have access for Approval Invoice!")
	}
}

function setUnitValue(obj)
{
	document.forms[0].tax_amt_unit.value=obj.value
	document.forms[0].inv_amt_unit.value=obj.value
	document.forms[0].adj_amt_unit.value=obj.value
	document.forms[0].net_amt_unit.value=obj.value
}

function openTcsTdsStructMst(tax_app)
{
	var type="";
	if(document.forms[0].invoice_category[0].checked)
	{
		type=document.forms[0].invoice_category[0].value;
	}
	else if(document.forms[0].invoice_category[1].checked)
	{
		type=document.forms[0].invoice_category[1].value;
	}
	
	if(type=="")
	{
		alert("Select Invoice Category!")	
	}
	else
	{
		var newWindow;
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open("frm_dlng_tcs_tds_tax_structure_list.jsp?type="+type+"&tax_app="+tax_app,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_dlng_tcs_tds_tax_structure_list.jsp?type="+type+"&tax_app="+tax_app,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function openTaxStructMst()
{
	var type="";
	if(document.forms[0].invoice_category[0].checked)
	{
		type=document.forms[0].invoice_category[0].value;
	}
	else if(document.forms[0].invoice_category[1].checked)
	{
		type=document.forms[0].invoice_category[1].value;
	}
	
	if(type=="")
	{
		alert("Select Invoice Category!")	
	}
	else
	{
		var newWindow;
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open("frm_dlng_tax_structure_list.jsp?type="+type,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_dlng_tax_structure_list.jsp?type="+type,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function setTaxStructDetail(tax_struct_cd,tax_struct_nm,tax_struct_eff_dt)
{
	var info = "Tax ("+tax_struct_nm+")";
	document.forms[0].tax_struct_info.value=info
	document.forms[0].tax_cd.value=tax_struct_cd
	document.forms[0].tax_dt.value=tax_struct_eff_dt
}

function setTcsTdsStructDetail(tax_struct_cd,tax_struct_nm,tax_struct_eff_dt,tax_app)
{
	if(tax_app=="TCS")
	{
		var info = "TCS ("+tax_struct_nm+")";
		document.forms[0].tcs_struct_info.value=info
		document.forms[0].tcs_cd.value=tax_struct_cd
		document.forms[0].tcs_dt.value=tax_struct_eff_dt
	}
	else
	{
		var info = "TDS ("+tax_struct_nm+")";
		document.forms[0].tds_struct_info.value=info
		document.forms[0].tds_cd.value=tax_struct_cd
		document.forms[0].tds_dt.value=tax_struct_eff_dt
	}
}

function refreshParent(accroid)
{
	window.opener.refresh(accroid);
}

function doDeleteAnnexure(annex_filenm,annex_seq,annex_folder)
{
	var a=confirm("Annexure File Name : "+annex_filenm+"\n\nDo you want to Delete?")
	if(a)
	{
		document.forms[0].annexure_seq.value=annex_seq
		document.forms[0].annexure_folder.value=annex_folder
		document.forms[0].option.value="DELETE_F_FLOW_ANNEXURE";
		document.getElementById("loading").style.visibility = "visible";
		editAllowedOnCpStatus = true;
		document.forms[0].submit();
	}
}

function enableTCSTDS(obj)
{
	if(obj.value=="TDS")
	{
		document.getElementById("row_tds").style.display="";
		document.getElementById("row_tds1").style.display="";
		
		document.getElementById("row_tcs").style.display="none";
	}
	else if(obj.value=="TCS")
	{
		document.getElementById("row_tds").style.display="none";
		document.getElementById("row_tds1").style.display="none";
		
		document.getElementById("row_tcs").style.display="table-row";
	}
	else
	{
		document.getElementById("row_tds").style.display="none";
		document.getElementById("row_tds1").style.display="none";
		document.getElementById("row_tcs").style.display="none";
	}
}

function setPeriodEndDate(period_start_dt)
{
	document.forms[0].period_end_dt.value = period_start_dt;
}
</script>

</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="dlng_inv" scope="request"></jsp:useBean>
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

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String gas_dt=request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
if(billing_cycle.equals(""))
{
	billing_cycle="8";
	/* if(Integer.parseInt(date_num) > 15)
	{
		billing_cycle="2";
	}
	else
	{
		billing_cycle="1";
	} */
}

if(month.equals("") || year.equals(""))
{
	month = gas_dt.split("/")[1];
	year = gas_dt.split("/")[2];
}

if(month.length() == 1)
{
	month="0"+month; 
}
String start_mth_dt=""+utildate.getFirstDateOfMonth(month, year);
String end_mth_dt=""+utildate.getLastDateOfMonth(month, year);

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

String mapping_id=request.getParameter("mapping_id")==null?"0":request.getParameter("mapping_id");
String filter_cont_type=request.getParameter("filter_cont_type")==null?"":request.getParameter("filter_cont_type");
String address_type=request.getParameter("address_type")==null?"0":request.getParameter("address_type");
String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String inv_category=request.getParameter("invoice_category")==null?"":request.getParameter("invoice_category");
String invoice_type=request.getParameter("invoice_type")==null?"0":request.getParameter("invoice_type");

String period_start_dt=request.getParameter("period_start_dt")==null?start_mth_dt:request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?end_mth_dt:request.getParameter("period_end_dt");

String inv_no=request.getParameter("inv_no")==null?"":request.getParameter("inv_no");
String inv_seq=request.getParameter("inv_seq")==null?"":request.getParameter("inv_seq");
String financial=request.getParameter("financial")==null?"":request.getParameter("financial");
String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");

String contact_person=request.getParameter("contact_person")==null?"0":request.getParameter("contact_person");
String bu_contact_person=request.getParameter("bu_contact_person")==null?"0":request.getParameter("bu_contact_person");

String activity_type =request.getParameter("activity_type")==null?"":request.getParameter("activity_type");

String tcs_tds=request.getParameter("tcs_tds")==null?"NA":request.getParameter("tcs_tds");

String contract_type="";
String cont_no="";
String cont_rev="";
String agmt_no="";
String agmt_rev="";
String cargo_no="";
if(!mapping_id.equals("") && !mapping_id.equals("0"))
{
	String[] temp = mapping_id.split("-");
	contract_type=temp[0];
	agmt_no=temp[1];
	agmt_rev=temp[2];
	cont_no=temp[3];
	cont_rev=temp[4];
	if(temp.length==6)
	{
		cargo_no=temp[5];
	}
}

dlng_inv.setCallFlag("DLNG_F_FLOW_INVOICE");
dlng_inv.setComp_cd(owner_cd);
dlng_inv.setCounterparty_cd(counterparty_cd);
dlng_inv.setAgmt_no(agmt_no);
dlng_inv.setAgmt_rev_no(agmt_rev);
dlng_inv.setCont_no(cont_no);
dlng_inv.setCont_rev_no(cont_rev);
dlng_inv.setContract_type(contract_type);
dlng_inv.setFilter_cont_type(filter_cont_type);
dlng_inv.setCargo_no(cargo_no);
dlng_inv.setMonth(month);
dlng_inv.setYear(year);
dlng_inv.setBilling_cycle(billing_cycle);
dlng_inv.setBu_unit(bu_unit);
dlng_inv.setBu_state_tin(bu_state_tin);
dlng_inv.setAddress_type(address_type);
if(billing_cycle.equals("8"))
{
	dlng_inv.setPeriod_start_dt(period_start_dt);
	dlng_inv.setPeriod_end_dt(period_end_dt);
}
dlng_inv.setInvoice_type(invoice_type);
dlng_inv.setOpration(opration);
dlng_inv.setInv_no(inv_no);
dlng_inv.setInv_seq(inv_seq);
dlng_inv.setFinancial(financial);
dlng_inv.init();

Vector VFILTER_CONT_TYPE = dlng_inv.getVFILTER_CONT_TYPE();
Vector VFILTER_CONT_NAME = dlng_inv.getVFILTER_CONT_NAME();

Vector VCOUNTERPARTY_CD = dlng_inv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dlng_inv.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dlng_inv.getVCOUNTERPARTY_ABBR();

Vector VDEAL_NO = dlng_inv.getVDEAL_NO();
Vector VMAPPING_ID = dlng_inv.getVMAPPING_ID();
Vector VADDRESS_TYPE = dlng_inv.getVADDRESS_TYPE();
Vector VADDRESS_NAME = dlng_inv.getVADDRESS_NAME();
Vector VCONTACT_PERSON = dlng_inv.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = dlng_inv.getVCONTACT_PERSON_CD();
Vector VSEL_BU_CD = dlng_inv.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = dlng_inv.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_BU_PLANT_ABBR = dlng_inv.getVSEL_BU_PLANT_ABBR();
Vector VBU_CONTACT_PERSON = dlng_inv.getVBU_CONTACT_PERSON();
Vector VBU_CONTACT_PERSON_CD = dlng_inv.getVBU_CONTACT_PERSON_CD();
Vector VLINK_INVOICE_NO = dlng_inv.getVLINK_INVOICE_NO();

Vector VMULTI_TAX_STRUCT = dlng_inv.getVMULTI_TAX_STRUCT();

int len_multi_tax=0;


Vector VANNEXURE_SEQ = dlng_inv.getVANNEXURE_SEQ();
Vector VANNEXURE_FILE_NM = dlng_inv.getVANNEXURE_FILE_NM();
Vector VANNEXURE_FOLDER = dlng_inv.getVANNEXURE_FOLDER();

Vector VMST_SAC_DESC = dlng_inv.getVMST_SAC_DESC();
Vector VMST_SAC_CODE = dlng_inv.getVMST_SAC_CODE();
Vector VMST_SAC_CD = dlng_inv.getVMST_SAC_CD();

String period_stdt=dlng_inv.getPeriod_start_dt();
String period_enddt=dlng_inv.getPeriod_end_dt();
if(period_stdt.equals("") && period_enddt.equals(""))
{
	period_stdt=period_start_dt;
	period_enddt=period_end_dt;
}
String couterpty_abbr=dlng_inv.getCouterpty_abbr();
String invoice_no= dlng_inv.getInvoice_no();
String invoice_seq= dlng_inv.getInvoice_seq();
String financial_year = dlng_inv.getFinancial_year();
String invoice_dt = dlng_inv.getInvoice_dt();
String invoice_due_dt = dlng_inv.getInvoice_due_dt();
String invoice_category = dlng_inv.getInvoice_category();
String num_line = dlng_inv.getNum_line();
String linked_invoice = dlng_inv.getLinked_invoice();
String note = dlng_inv.getNote();
String contact_person_cd = dlng_inv.getContact_person_cd();
String bu_contact_person_cd = dlng_inv.getBu_contact_person_cd();
String invoice_check_flag = dlng_inv.getInvoice_check_flag();
String invoice_check_dt = dlng_inv.getInvoice_check_dt();
String invoice_check_by = dlng_inv.getInvoice_check_by();
String invoice_check_nm = dlng_inv.getInvoice_check_nm();
String invoice_auth_flag = dlng_inv.getInvoice_auth_flag();
String invoice_auth_dt = dlng_inv.getInvoice_auth_dt();
String invoice_auth_by = dlng_inv.getInvoice_auth_by();
String invoice_auth_nm = dlng_inv.getInvoice_auth_nm();
String invoice_aprv_flag = dlng_inv.getInvoice_aprv_flag();
String invoice_aprv_dt = dlng_inv.getInvoice_aprv_dt();
String invoice_aprv_by = dlng_inv.getInvoice_aprv_by();
String invoice_aprv_nm = dlng_inv.getInvoice_aprv_nm();
String other_inv_str = dlng_inv.getOther_inv_str();
String bu_state_code = dlng_inv.getBu_state_tin();
if(bu_state_code.equals(""))
{
	bu_state_code=bu_state_tin;
}

String sub_invoice_type = dlng_inv.getSub_inv_type();

String gross_amt = dlng_inv.getGross_amt();
String exchang_rate = dlng_inv.getExchang_rate();
String gross_amt1 = dlng_inv.getGross_amt1();
String tax_amt = dlng_inv.getTax_amt();
String invoice_amt = dlng_inv.getInvoice_amt();
String invoice_adj_amt = dlng_inv.getInvoice_adj_amt();
String net_payable = dlng_inv.getNet_payable();
String invoice_raised_in = dlng_inv.getInvoice_raised_in();
String amount_in_word = dlng_inv.getAmount_in_word();
String tax_struct_cd=dlng_inv.getTax_struct_cd();
String tax_struct_dt=dlng_inv.getTax_struct_dt();
String tax_struct_info=dlng_inv.getTax_struct_info();
String qty_mmbtu = dlng_inv.getQty_mmbtu();
String sac_no = dlng_inv.getSac_no();

String tds_amt=dlng_inv.getTds_amt();
//String tds_factor=dlng_inv.getTds_factor();
String tds_struct_cd=dlng_inv.getTds_struct_cd();
String tds_struct_dt=dlng_inv.getTds_struct_dt();
String tds_struct_info=dlng_inv.getTds_struct_info();

String applicable_amt=dlng_inv.getApplicable_amt();
//String tcs_factor=dlng_inv.getTcs_factor();
String tcs_struct_cd=dlng_inv.getTcs_struct_cd();
String tcs_struct_dt=dlng_inv.getTcs_struct_dt();
String tcs_struct_info=dlng_inv.getTcs_struct_info();

if(financial_year.equals(""))
{
	financial_year=financial;
}

String applicable_abbr = dlng_inv.getApplicable_abbr();
if(applicable_abbr.equals(""))
{
	applicable_abbr=tcs_tds;
}
if(invoice_raised_in.equals("0") || invoice_raised_in.equals(""))
{
	invoice_raised_in="1";
}

String contRef = dlng_inv.getContRef();

Vector VLINE_NO = dlng_inv.getVLINE_NO();
Vector VLINE_DESC = dlng_inv.getVLINE_DESC();
Vector VUNIT = dlng_inv.getVUNIT();
Vector VQTY = dlng_inv.getVQTY();
Vector VRATE = dlng_inv.getVRATE();
Vector VAMOUNT = dlng_inv.getVAMOUNT();

boolean submission_chk = dlng_inv.getSubmission_chk();

if(!contact_person.equals("0") && !contact_person.equals(""))
{
	contact_person_cd=contact_person;
}
if(!bu_contact_person.equals("0") && !bu_contact_person.equals(""))
{
	bu_contact_person_cd=bu_contact_person;
}

if(invoice_category.equals(""))
{
	if (contract_type.equals("O") || contract_type.equals("Q"))
	{
		invoice_category="S";
	}	
	else
	{
		invoice_category="P";
	}
}


String annexure_path="../"+CommonVariable.work_dir+owner_cd+""+CommonVariable.freeflow_annexure_path+"";
%>
<body onload="<%if(!msg.equals("")){%>refreshParent('<%=accroid%>');<%}%>" <%if(!activity_type.equals("PREPARE")) {%>style="pointer-events: none;"<%} %>>

<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_DLNG_Invoice" enctype="multipart/form-data">

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
					    	DLNG Sales Free Flow Invoice Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5" <%if(opration.equals("MODIFY")){%>style="pointer-events: none;"<%}%>>
						<div class="col-sm-6 col-xs-2 col-md-4">
							<div class="form-group row">
								<div class="col-auto">  	  	
					    			<label class="form-label"><b>Select Customer</b></label>
					  			</div>
								<div class="col">  
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=opration%>');">
										<option value="0">--Select--</option>
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
									<label class="form-label"><b>Month/Year</b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="month" onchange="refresh('<%=opration%>');">
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
					  				<select class="form-select form-select-sm" name="year" onchange="refresh('<%=opration%>');">
					  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
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
						    		<label class="form-label"><b>Contract Type</b></label>
						  		</div>				  	
								<div class="col"> 
									<select class="form-select form-select-sm" name="filter_cont_type" onchange="refresh('<%=opration%>');">
										<option value="">--All--</option>
										<%for (int i=0;i<VFILTER_CONT_TYPE.size();i++){ %>
										<option value="<%=VFILTER_CONT_TYPE.elementAt(i)%>"><%=VFILTER_CONT_NAME.elementAt(i)%></option>										
										<%} %>
									</select>
									<script>document.forms[0].filter_cont_type.value="<%=filter_cont_type%>"</script>								
								</div>
							</div>	
						</div>	
						<%-- <%if(opration.equals("MODIFY")){ %>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="button" class="btn btn-info btn-sm select_btn" value="Select Invoice" onclick=" openInvoiceList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<%} %> --%>
					  	<div class="col-sm-1 col-xs-1 col-md-1"></div>
					</div>
				</div>
				<div class="card-body cdbody" <%if(opration.equals("MODIFY")){%>style="pointer-events: none;"<%}%>>
					<div class="row m-b-5">				
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Billing Period<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="row m-b-5">
								<div class="col">
									<div class="input-group input-group-sm">
				      					<input type="text" class="form-control form-control-sm date <%if(billing_cycle.equals("8")){ %>fmsdtpick<%}%>" name="period_start_dt" value="<%=period_stdt %>" maxLength="10" 
				      					onchange="validateDate(this);refresh('<%=opration%>');setPeriodEndDate(this.value);" <%if(!billing_cycle.equals("8")){ %>readonly<%} %>><!-- checkStartEndDate('F'); -->
				      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      				</div>
			      				</div>
			      				<div class="col">
				      				<div class="input-group input-group-sm">
				      					<input type="text" class="form-control form-control-sm date <%if(billing_cycle.equals("8")){ %>fmsdtpick<%}%>" name="period_end_dt" value="<%=period_end_dt %>" maxLength="10" 
				      					onchange="validateDate(this);checkStartEndDate('T');refresh('<%=opration%>');" 
				      					><%-- <%if(!billing_cycle.equals("8")){ %>readonly<%} %> --%>
				      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      				</div>
	      						</div>
								<select class="form-select form-select-sm" name="billing_cycle" onchange="refresh('<%=opration%>');" style="display: none;">
				  					<option value="1">1st-Fortnight</option>
									<option value="2">2nd-Fortnight</option>
									<option value="3">1st-Weekly</option>
									<option value="4">2nd-Weekly</option>
									<option value="5">3rd-Weekly</option>
									<option value="6">4th-Weekly</option>
									<option value="9">5th-Weekly</option>
									<option value="7">Monthly</option>
									<option value="8">Other</option>
				  				</select>
				  				<script>document.forms[0].billing_cycle.value="<%=billing_cycle%>"</script>
	      					</div>
	      				</div>
						<div class="col-sm-2 col-xs-2 col-md-2">	
							<div class="form-group row">
					    		<label class="form-label"><b>Contract#<span class="s-red">*</span></b></label>
					  		</div>
					  	</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">	
								<div class="col"> 
									<div class="input-group input-group-sm">
										<select class="form-select form-select-sm" name="mapping_id" onchange="refresh('<%=opration%>');">
											<option value="0">--Select--</option>
											<%for(int i=0;i<VMAPPING_ID.size();i++){ %>
											<option value="<%=VMAPPING_ID.elementAt(i)%>"><%=VDEAL_NO.elementAt(i)%></option>
											<%} %>
										</select>
										<script>document.forms[0].mapping_id.value="<%=mapping_id%>"</script>
									</div>
								</div>		
							</div>
						</div>	      											      				
					</div>
					<div class="row m-b-5">							
						<div class="col-sm-2 col-xs-2 col-md-2">	
							<div class="form-group row">
					    		<label class="form-label"><b><%=couterpty_abbr%> Address<span class="s-red">*</span></b></label>
					  		</div>
					  	</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">	
								<div class="col"> 
									<div class="input-group input-group-sm">
										<select class="form-select form-select-sm" name="address_type" onchange="refresh('<%=opration%>');">
											<option value="0">--Select--</option>
											<%for(int i=0;i<VADDRESS_TYPE.size();i++){ %>
											<option value="<%=VADDRESS_TYPE.elementAt(i)%>"><%=VADDRESS_NAME.elementAt(i)%></option>
											<%} %>
										</select>
										<script>document.forms[0].address_type.value="<%=address_type%>"</script>							
									</div>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">	
							<div class="form-group row">
					    		<label class="form-label"><b><%=couterpty_abbr%> Contact Person<span class="s-red">*</span></b></label>
					  		</div>
					  	</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">	
								<div class="col"> 
									<div class="input-group input-group-sm">
										<select class="form-select form-select-sm" name="contact_person" style="pointer-events: auto;">
											<option value="0">--Select--</option>
											<%for(int i=0;i<VCONTACT_PERSON_CD.size();i++){ %>
											<option value="<%=VCONTACT_PERSON_CD.elementAt(i)%>"><%=VCONTACT_PERSON.elementAt(i)%></option>
											<%} %>
										</select>
										<script>document.forms[0].contact_person.value="<%=contact_person_cd%>"</script>
									</div>
								</div>
							</div>		
						</div>
					</div>	
					<div class="row m-b-5">							
						<div class="col-sm-2 col-xs-2 col-md-2">	
							<div class="form-group row">
					    		<label class="form-label"><b><%=owner_abbr%> Business Unit<span class="s-red">*</span></b></label>
					  		</div>
					  	</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">	
								<div class="col"> 
									<div class="input-group input-group-sm">
										<select class="form-select form-select-sm" name="bu_unit" onchange="refresh('<%=opration%>');">
											<option value="0">--Select--</option>
											<%for(int i=0;i<VSEL_BU_PLANT_SEQ_NO.size();i++){ %>
											<option value="<%=VSEL_BU_PLANT_SEQ_NO.elementAt(i)%>"><%=VSEL_BU_PLANT_ABBR.elementAt(i)%></option>
											<%} %>
										</select>
										<script>document.forms[0].bu_unit.value="<%=bu_unit%>"</script>
									</div>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">	
							<div class="form-group row">
					    		<label class="form-label"><b><%=owner_abbr%> Contact Person<span class="s-red">*</span></b></label>
					  		</div>
					  	</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">	
								<div class="col"> 
									<div class="input-group input-group-sm">
										<select class="form-select form-select-sm" name="bu_contact_person" style="pointer-events: auto;">
											<option value="0">--Select--</option>
											<%for(int i=0;i<VBU_CONTACT_PERSON_CD.size();i++){ %>
											<option value="<%=VBU_CONTACT_PERSON_CD.elementAt(i)%>"><%=VBU_CONTACT_PERSON.elementAt(i)%></option>
											<%} %>
										</select>
										<script>document.forms[0].bu_contact_person.value="<%=bu_contact_person_cd%>"</script>									
									</div>
								</div>
							</div>
						</div>												
					</div>																		
				</div>						
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
								<div class="col-auto">
									<h5><span class="badge bg-info text-dark">TCS/TDS Applicable :</span></h5>
								</div> 
								<div class="col-auto">
									<select class="form-select form-select-sm" name="tcs_tds" onchange="enableTCSTDS(this);">
										<option value="NA">NA</option>
										<option value="TCS">TCS</option>
										<option value="TDS">TDS</option>
									</select>
									<script>document.forms[0].tcs_tds.value="<%=applicable_abbr%>"</script>
								</div>
							</div>
						</div>	
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Invoice Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Invoice Category<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="radio" name="invoice_category" value="P" onchange="refresh('<%=opration%>');" <%if(invoice_category.equals("P")){ %>checked<%}%>>&nbsp;Product&nbsp;&nbsp;
				    				<input type="radio" name="invoice_category" value="S" onchange="refresh('<%=opration%>');" <%if(invoice_category.equals("S")){ %>checked<%}%>>&nbsp;Service				
				    			</div>
				    		</div>
				    	</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Invoice Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-auto">									
									<select class="form-select form-select-sm" name="invoice_type" onchange="refresh('<%=opration%>');">									
										<option value="0">--Select--</option>
										<%if(invoice_category.equals("P")){ %>
										<option value="CR">Credit Note (CR)</option>
										<option value="DR">Debit Note (DR)</option>
										<option value="CCR">Commercial Credit Note (CCR)</option>
										<option value="CDR">Commercial Debit Note (CDR)</option>
										<option value="LP">Late Payment (LP)</option>
										<option value="OR">Other (OR)</option>
										<%}else if(invoice_category.equals("S") 
												){%>										
										<option value="TLU">DLNG Service Charge (TLU)</option><!-- && (contract_type.equals("O") || contract_type.equals("Q")) -->
										<%} %>
									</select>										
									<script>document.forms[0].invoice_type.value="<%=invoice_type%>"</script>				
				    			</div>
				    			<div class="col">
				    				<input type="text" class="form-control form-control-sm" name="subject_line" 
				    				value="<%=other_inv_str%>" placeholder="Invoice Heading">
				    			</div>
				    		</div>
				    	</div>	
				    	<%if (invoice_type.equals("CR") || invoice_type.equals("DR") || invoice_type.equals("CCR") || invoice_type.equals("CDR")) {%>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Sub Invoice Head<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="sub_invoice_type">
										<option value="">--Select--</option>
										<%if(invoice_category.equals("P") ){%>	
										<option value="NA">Not Applicable</option>
										<option value="LP">Late Payment (LP)</option>
										<option value="IMB">Imbalance Charge (IMB)</option>	
										<% } else if(invoice_category.equals("S")){%>	
										<option value="LP">Late Payment (LP)</option>										
										<%} %>									
									</select>
									<script>document.forms[0].sub_invoice_type.value="<%=sub_invoice_type%>"</script>																	
				    			</div>
				    		</div>
				    	</div>
				    	<%} %>
				    </div>
				    <div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Invoice#</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="invoice_no" value="<%=invoice_no%>" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="invoice_seq" value="<%=invoice_seq%>" readOnly>
				    			</div>
				    			
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b><!-- <input type="checkbox" class="form-check-input" checked>&nbsp; -->Link Invoice Ref</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="linked_invoice">
										<option value="">--Select--</option>
										<%for(int i=0;i<VLINK_INVOICE_NO.size();i++){ %>
										<option value="<%=VLINK_INVOICE_NO.elementAt(i)%>"><%=VLINK_INVOICE_NO.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].linked_invoice.value="<%=linked_invoice%>"</script>
				    			</div>
				    		</div>
				    	</div>
				    </div>
				    <div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Invoice Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm">
				      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="invoice_dt" value="<%=invoice_dt%>" maxLength="10" 
				      					onchange="validateDate(this);">
				      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      				</div>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Invoice Due Date
								<%if(!invoice_type.equals("CR") && !invoice_type.equals("CCR")) {%><span class="s-red">*</span><%} %>
								</b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm">
				      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="invoice_due_dt" value="<%=invoice_due_dt%>" maxLength="10" 
				      					onchange="validateDate(this);">
				      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      				</div>
				    			</div>
				    		</div>
				    	</div>
				    </div>
				   <div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Invoice Raised In<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="invoice_raised_in" onchange="setUnitValue(this);">
										<option value="0">--Select--</option> 
					  					<option value="1">INR</option>
										<!-- <option value="2">USD</option> -->
									</select>
									<script>document.forms[0].invoice_raised_in.value="<%=invoice_raised_in%>"</script>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Invoiced MMBTU<%if(!invoice_type.equals("LP")) {%><span class="s-red">*</span><%} %></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="alloc_qty" value="<%=qty_mmbtu%>">
				    			</div>
				    		</div>
				    	</div>
				    </div>
				    <%if(invoice_category.equals("S") ){%>	
				    <div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>SAC<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">	
								<div class="col"> 
									<div class="input-group input-group-sm">
										<select class="form-select form-select-sm" name="sac_no">
											<option value="0">--Select--</option>
											<%for(int i=0;i<VMST_SAC_CD.size();i++){ %>
											<option value="<%=VMST_SAC_CD.elementAt(i)%>"><%=VMST_SAC_CODE.elementAt(i)%> - <%=VMST_SAC_DESC.elementAt(i)%></option>
											<%} %>
										</select>
										<script>document.forms[0].sac_no.value="<%=sac_no%>"</script>
									</div>
								</div>		
							</div>
				    	</div>
				    </div>
				    <%} %>
				    <div class="row m-b-5">				    
				    	<div class="col-sm-2 col-xs-2 col-md-2" id="row_tds" style="display: none">  
			    			<input type="button" class="btn btn-sm config_btn" value="TDS Config" onclick="openTcsTdsStructMst('TDS');">
							<input type="hidden" name="tds_cd" value="<%=tds_struct_cd%>">
							<input type="hidden" name="tds_dt" value="<%=tds_struct_dt%>">
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" id="row_tds1" style="display: none">  
							<div class="form-group row">
								<div class="col">
									<textarea class="form-control form-control-sm" name="tds_struct_info" cols="75" rows="1" style="font-weight: bold;" readOnly>TDS(<%=tds_struct_info%>)</textarea>
				    			</div>
				    			<div class="col">
				    				<div class="input-group input-group-sm">
					    				<input type="text" class="form-control form-control-sm" name="tds_amt" value="<%=tds_amt%>" maxlength='20' style="text-align:right;">
					    				<select class="form-select form-select-sm" name="tds_amt_unit" disabled>
											<!-- <option value="0">--Select--</option> -->
						  					<option value="1">INR</option>
											<!-- <option value="2">USD</option> -->
										</select>
									</div>
									<script>document.forms[0].tds_amt_unit.value="<%=invoice_raised_in%>"</script>
				    			</div>
				    		</div>
				    	</div>
				    </div>
				    <div id="lineItem">
				    	&nbsp;
					    <div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="table-responsive">
									<table class="table table-bordered table-hover">
										<thead>
											<tr>
												<th>Sr#</th>
												<th>Line Item</th>
												<th>Unit</th>
												<th>Qty</th>
												<th>Rate</th>
												<th>Amount</th>
												<th>Add/Del line</th>
											</tr>
										</thead>
										<tbody id="itemTab">
											<%for(int i=0;i<VLINE_NO.size(); i++){ %>
											<tr>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="item_seq" value="<%=VLINE_NO.elementAt(i)%>" style="width:50px;" onblur="checkNumber1(this,2,0);">
												</td>
												<td align="center">
													<textarea class="form-control form-control-sm" name="item_note" cols="75" rows="1" style="width:250px;"><%=VLINE_DESC.elementAt(i)%></textarea>
												</td>
												<td align="center">
													<input type='text' class='form-control form-control-sm' name='unit' value="<%=VUNIT.elementAt(i)%>" maxlength='30' style='width:100px;'>
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="qty" value="<%=VQTY.elementAt(i)%>" maxlength='10' style="width:100px;text-align:right;">
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="rate" value="<%=VRATE.elementAt(i)%>" maxlength='10' style="width:100px;text-align:right;">
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="amount" value="<%=VAMOUNT.elementAt(i)%>" maxlength='20' style="width:100px;text-align:right;">
												</td>
												<td align="center">
													<a <%if(VLINE_NO.size()>1){ %><%}else{ %>onclick="addrow();"<%} %> id="minus<%=i+1%>" <%if((i+1) == 1){ %>style="display:none"<%} %><%else if((VLINE_NO.size() - 1) == i){ %><%}else{%>style="display:none"<%} %>>
														<i class="fa fa-minus-circle fa-2x"></i>
													</a>&nbsp;
													<a onclick="addrow();" id="plus<%=i+1%>" <%if((VLINE_NO.size() - 1) == i){ %><%}else{%>style="display:none"<%} %>>
														<i class="fa fa-plus-circle fa-2x"></i>
													</a>
												</td>
											</tr>
											<%} %>
										</tbody>
										<tbody>
											<tr>
												<td align="center">
													<!-- <input type="checkbox" class="form-check-input"> -->
												</td>
												<td align="center">
													<textarea class="form-control form-control-sm" name="" cols="75" rows="1" style="width:250px;font-weight: bold;" readOnly>Gross Amount (USD)</textarea>
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;'>
														<option value="2">USD</option>
													</select>
												</td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="gross_amt_usd" value="<%=gross_amt%>" maxlength='20' style="width:100px;text-align:right;">
												</td>
												<td align="center"></td>
											</tr>
											<tr>
												<td align="center">
													<!-- <input type="checkbox" class="form-check-input"> -->
												</td>
												<td align="center">
													<textarea class="form-control form-control-sm" name="" cols="75" rows="1" style="width:250px;font-weight: bold;" readOnly>Exchange Rate (INR/USD)</textarea>
												</td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="exchange_rate" value="<%=exchang_rate%>" maxlength='20' style="width:100px;text-align:right;">
												</td>
												<td align="center"></td>
												<td align="center"></td>
											</tr>
											<tr>
												<td align="center">
													<!-- <input type="checkbox" class="form-check-input"> -->
												</td>
												<td align="center">
													<textarea class="form-control form-control-sm" name="" cols="75" rows="1" style="width:250px;font-weight: bold;" readOnly>Gross Amount (INR)</textarea>
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;'>
														<option value="1">INR</option>
													</select>
												</td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="gross_amt_inr" value="<%=gross_amt1%>" maxlength='20' style="width:100px;text-align:right;">
												</td>
												<td align="center"></td>
											</tr>
											<tr>
												<td align="center">
													<input type="button" class="btn btn-sm config_btn" value="Tax Config" onclick="openTaxStructMst();">
													<input type="hidden" name="tax_cd" value="<%=tax_struct_cd%>">
													<input type="hidden" name="tax_dt" value="<%=tax_struct_dt%>">
													<input type="hidden" name="temp_tax_cd" value="<%=tax_struct_cd%>">
												</td>
												<td align="center">
													<textarea class="form-control form-control-sm" name="tax_struct_info" cols="75" rows="1" style="width:250px;font-weight: bold;" readOnly>Tax (<%=tax_struct_info%>)</textarea>								
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;' name="tax_amt_unit">
														<!-- <option value="0">--Select--</option> -->
									  					<option value="1">INR</option>
														<!-- <option value="2">USD</option> -->
													</select>
													<script>document.forms[0].tax_amt_unit.value="<%=invoice_raised_in%>"</script>
												</td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="tax_amt" value="<%=tax_amt%>" maxlength='20' style="width:100px;text-align:right;">
												</td>
												<td align="center"></td>
											</tr>
											<%if(VMULTI_TAX_STRUCT.size()>0){
												for(int i=0;i<VMULTI_TAX_STRUCT.size();i++)
												{
													Vector temp =(Vector)((Vector)((Vector)VMULTI_TAX_STRUCT.elementAt(i)));
													
													len_multi_tax=((Vector) temp.elementAt(0)).size();
													for(int j=0;j<((Vector) temp.elementAt(0)).size(); j++)
													{%>
													<tr style="background: #cff4fc;<%if(((Vector) temp.elementAt(0)).size()==1){%>display:none;<%} %>">
														<td align="center">
															<input type="hidden" name="sub_tax_code" value="<%=((Vector) temp.elementAt(0)).elementAt(j)%>">
															<input type="hidden" name="sub_tax_struct" value="<%=((Vector) temp.elementAt(1)).elementAt(j)%>">
															<input type="hidden" name="sub_tax_base_amt" value="<%=((Vector) temp.elementAt(3)).elementAt(j)%>">
														</td>
														<td align="center">
															<input class="form-control form-control-sm" name="sub_tax_struct_info" cols="75" rows="1" 
															style="width:250px;font-weight: bold; text-align: right;" readOnly value="<%=((Vector) temp.elementAt(1)).elementAt(j)%>">								
														</td>
														<td align="center">
															<select class="form-select form-select-sm" style='width:100px;' name="sub_tax_amt_unit" id="sub_tax_amt_unit_<%=j%>">
																<option value="0">--Select--</option>
											  					<option value="1">INR</option>
																<option value="2">USD</option>
															</select>
															<script>document.getElementById("sub_tax_amt_unit_<%=j%>").value="<%=invoice_raised_in%>"</script>
														</td>
														<td align="center"></td>
														<td align="center"></td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="sub_tax_amt" value="<%=((Vector) temp.elementAt(2)).elementAt(j)%>" maxlength='20' style="width:100px;text-align:right;">
														</td>
														<td align="center"></td>
													</tr>
													<%}
												}
											} %>
											<tr>
												<td align="center"></td>
												<td align="center">
													<textarea class="form-control form-control-sm" name="" cols="75" rows="1" style="width:250px;font-weight: bold;" readOnly>Invoice Amount</textarea>
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;' name="inv_amt_unit">
														<!-- <option value="0">--Select--</option> -->
									  					<option value="1">INR</option>
														<!-- <option value="2">USD</option> -->
													</select>
													<script>document.forms[0].inv_amt_unit.value="<%=invoice_raised_in%>"</script>
												</td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="invoice_amt" value="<%=invoice_amt%>" maxlength='20' style="width:100px;text-align:right;">
												</td>
												<td align="center"></td>
											</tr>
											<tr>
												<td align="center">
													<!-- <input type="checkbox" class="form-check-input"> -->
												</td>
												<td align="center">
													<textarea class="form-control form-control-sm" name="" cols="75" rows="1" style="width:250px;font-weight: bold;" readOnly>Adjustments</textarea>
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;' name="adj_amt_unit">
														<!-- <option value="0">--Select--</option> -->
									  					<option value="1">INR</option>
														<!-- <option value="2">USD</option> -->
													</select>
													<script>document.forms[0].adj_amt_unit.value="<%=invoice_raised_in%>"</script>
												</td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="adjust_amt" value="<%=invoice_adj_amt%>" maxlength='20' style="width:100px;text-align:right;">
												</td>
												<td align="center"></td>
											</tr>
											<tr id="row_tcs" style="display:none;">
												<td align="center">
													<input type="button" class="btn btn-sm config_btn" value="TCS Config" onclick="openTcsTdsStructMst('TCS');">
													<input type="hidden" name="tcs_cd" value="<%=tcs_struct_cd%>">
													<input type="hidden" name="tcs_dt" value="<%=tcs_struct_dt%>">
												</td>
												<td align="center">
													<textarea class="form-control form-control-sm" name="tcs_struct_info" cols="75" rows="1" style="width:250px;font-weight: bold;" readOnly>TCS(<%=tcs_struct_info%>)</textarea>
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;' name="tcs_amt_unit">
														<option value="0">--Select--</option>
									  					<option value="1">INR</option>
														<option value="2">USD</option>
													</select>
													<script>document.forms[0].tcs_amt_unit.value="<%=invoice_raised_in%>"</script>
												</td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="tcs_amt" value="<%=applicable_amt%>" maxlength='20' style="width:100px;text-align:right;">
												</td>
												<td align="center"></td>
											</tr>
											<tr>
												<td align="center"></td>
												<td align="center">
													<textarea class="form-control form-control-sm" name="" cols="75" rows="1" style="width:250px;font-weight: bold;" readOnly>Net Payable</textarea>
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;' name="net_amt_unit">
														<!-- <option value="0">--Select--</option> -->
									  					<option value="1">INR</option>
														<!-- <option value="2">USD</option> -->
													</select>
													<script>document.forms[0].net_amt_unit.value="<%=invoice_raised_in%>"</script>
												</td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="net_amt" value="<%=net_payable%>" maxlength='20' style="width:100px;text-align:right;">
												</td>
												<td align="center"></td>
											</tr>
										</tbody>	
									</table>
								</div>
							</div>
					    </div>
				    </div>
				    <%-- <div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Amount in Word</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="amt_in_word" value="<%=amount_in_word%>">
				    			</div>
				    		</div>
				    	</div>
				    </div> --%>
				    &nbsp;
				    <div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Note</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<textarea class="form-control form-control-sm" name="note" cols="75" rows="3"><%=note%></textarea>
				    			</div>
				    		</div>
				    	</div>
				    </div>
				    <div class="row m-b-5" style="display: none">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Attach Annexure</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
				      					<input type="file" class="form-control form-control-sm" name="annexure_att" id="annexure_att">
					      				<span class="input-group-text"><i class="fa fa-upload fa-lg"></i></span>
					      			</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5" style="display: none">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Attached Annexure</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
							<%for(int i=0;i<VANNEXURE_SEQ.size();i++){ %>
				    			<div class="col-12">
				    				<div class="input-group input-group-sm" >
				      					<input class="form-control form-control-sm" style="background:#99ffcc;" value="<%=VANNEXURE_FILE_NM.elementAt(i)%>">
					      				<span class="input-group-text" <%if(!activity_type.equals("PREPARE")) {%>style="pointer-events: auto;"<%} %>>
											<a href="<%=annexure_path+"/"+VANNEXURE_FOLDER.elementAt(i)+"/"+VANNEXURE_FILE_NM.elementAt(i)%>" download>
												<font color="black"><i class="fa fa-download fa-lg"></i></font>
											</a>
										</span>
										<span class="input-group-text"><i class="fa fa-trash-o fa-lg" style="color:red;" onclick="doDeleteAnnexure('<%=VANNEXURE_FILE_NM.elementAt(i)%>','<%=VANNEXURE_SEQ.elementAt(i)%>','<%=VANNEXURE_FOLDER.elementAt(i)%>');"></i></span>
					      			</div>
					      		</div>
				    		<%} %>
				  			</div>
						</div>
					</div>
					&nbsp;
				    <div class="row m-b-5">
				    	<div class="col-sm-6 col-xs-6 col-md-6" align="right"> 
				    		<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    			<%if(write_access.equals("Y")){ %>
				    				<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();" <%if(invoice_check_flag.equals("Y")){ %>disabled<%} %> <%if(!activity_type.equals("PREPARE")){ %>style="display:none;"<%} %>>
				    			<%}else{ %>
				    				<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();" disabled>
				    			<%} %>
				    				<input type="button" class="btn btn-warning com-btn" value="View" onclick="printPDF();" style="pointer-events: auto;">
				    			</div>
				    		</div>
				    	</div>
				    </div>
				</div>
				<%if(opration.equals("MODIFY")) {%>
				<div class="card-body cdbody" <%if(activity_type.equals("PREPARE")){ %>style="display:none;"<%} %>>
					<!-- <div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Check/ Authorize/ Approve</label>
					</div> -->
					<div class="row m-b-5" style="<%if(!activity_type.equals("CHECK")){ %>display:none;<%} %>pointer-events: auto;">
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-3"> 
				    				<label class="form-label"><b>Check</b></label>
				    			</div>
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
				    			<div class="col-auto">
				    				<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="checkInvoice();" 
				    				<%if(invoice_auth_flag.equals("Y") || !submission_chk){ %>disabled<%}%>>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<%if(!invoice_check_flag.equals("")){ %>
									<font color="<%if(invoice_check_flag.equals("Y")){ %>green<%}else{%>red<%}%>"><%=invoice_check_dt%> <%=invoice_check_nm%> by <%=invoice_check_by%></font>
								<%} %>
							</div>
						</div>
					</div>
					<div class="row m-b-5" style="<%if(!activity_type.equals("AUTHORIZE")){ %>display:none;<%} %>pointer-events: auto;">
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-3"> 
				    				<label class="form-label"><b>Authorize</b></label>
				    			</div>
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
				    			<div class="col-auto">
				    				<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="authorizeInvoice();" 
				    				<%if(invoice_aprv_flag.equals("A") || !invoice_check_flag.equals("Y")){ %>disabled<%} %>>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<%if(!invoice_auth_flag.equals("")){ %>
									<font color="<%if(invoice_auth_flag.equals("Y")){ %>green<%}else{%>red<%}%>"><%=invoice_auth_dt%> <%=invoice_auth_nm%> by <%=invoice_auth_by%></font>
								<%} %>
							</div>
						</div>
					</div>
					<div class="row m-b-5" style="<%if(!activity_type.equals("APPROVE")){ %>display:none;<%} %>pointer-events: auto;">
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">	
								<div class="col-3"> 
				    				<label class="form-label"><b>Approve</b></label>
				    			</div>
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
				    			<div class="col-auto"> 
				    				<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="approveInvoice();" 
				    				<%if(!invoice_auth_flag.equals("Y")) {%>disabled<%} %>>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<%if(!invoice_aprv_flag.equals("")){ %>
									<font color="<%if(invoice_aprv_flag.equals("A")){ %>green<%}else{%>red<%}%>"><%=invoice_aprv_dt%> <%=invoice_aprv_nm%> by <%=invoice_aprv_by%></font>
								<%} %>
							</div>
						</div>
					</div>
				</div>
				<%} %>	
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="F_FLOW_INVOICE">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="opration_type" value="">
<input type="hidden" name="prev_billing_cycle" value="<%=billing_cycle%>">

<input type="hidden" name="prev_month" value="<%=month%>">
<input type="hidden" name="prev_year" value="<%=year%>">

<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" value="<%=agmt_rev%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev" value="<%=cont_rev%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="bu_state_tin" value="<%=bu_state_code%>">
<input type="hidden" name="contract_ref" value="<%=contRef%>">
<input type="hidden" name="cargo_no" value="<%=cargo_no%>">
<input type="hidden" name="accroid" value="<%=accroid%>">
<input type="hidden" name="activity_type" value="<%=activity_type%>">

<input type="hidden" name="item_size" id="item_size" value="<%=VLINE_NO.size()%>">

<input type="hidden" class="form-control form-control-sm" name="no_of_line" value="<%=num_line%>" maxlength="2">

<input type="hidden" name="annexure_seq" value="">
<input type="hidden" name="annexure_folder" value="">
<input type="hidden" name="gas_dt" value="<%=gas_dt%>">

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
enableTCSTDS(document.forms[0].tcs_tds);
</script>

</form>
</body>
</html>