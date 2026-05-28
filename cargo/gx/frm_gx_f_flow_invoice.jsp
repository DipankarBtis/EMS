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
	var gx_counterparty_cd = document.forms[0].gx_counterparty_cd.value;
	var gx_bu_unit = document.forms[0].gx_bu_unit.value;
	
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var mapping_id = document.forms[0].mapping_id.value;
	var bu_unit = document.forms[0].bu_unit.value;
	var invoice_type= document.forms[0].invoice_type.value;
	var contact_person = document.forms[0].contact_person.value; //gx contact person
	var bu_contact_person = document.forms[0].bu_contact_person.value;
	
	var activity_type = document.forms[0].activity_type.value;
	var tcs_tds = document.forms[0].tcs_tds.value;
	
	var msg="";
	var flag = true;
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "frm_gx_f_flow_invoice.jsp?u="+u+"&counterparty_cd="+counterparty_cd+"&month="+month+"&year="+year+
				"&mapping_id="+mapping_id+"&activity_type="+activity_type+
				"&gx_counterparty_cd="+gx_counterparty_cd+"&gx_bu_unit="+gx_bu_unit+"&bu_unit="+bu_unit+
				"&invoice_type="+invoice_type+"&opration="+opration+"&tcs_tds="+tcs_tds+
				"&contact_person="+contact_person+"&bu_contact_person="+bu_contact_person;

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
		input01.readOnly = true;
		input01.value = new_seq_no;
		input01.style.width='50px';
		
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

function doSubmit()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var gx_counterparty_cd = document.forms[0].gx_counterparty_cd.value;
	var gx_bu_unit = document.forms[0].gx_bu_unit.value;
	
	var mapping_id = document.forms[0].mapping_id.value;
	var bu_unit = document.forms[0].bu_unit.value;
	var invoice_type= document.forms[0].invoice_type.value;
	
	var contact_person = document.forms[0].contact_person.value;
	var bu_contact_person = document.forms[0].bu_contact_person.value;
	
	var invoice_no = document.forms[0].invoice_no.value;
	var invoice_ref = document.forms[0].invoice_ref.value;
	var invoice_category = document.forms[0].invoice_category;
	var invoice_dt = document.forms[0].invoice_dt.value;
	var invoice_due_dt = document.forms[0].invoice_due_dt.value;
	
	var invoice_raised_in = document.forms[0].invoice_raised_in.value
	
	var tax_cd = document.forms[0].tax_cd.value
	var tax_amt = document.forms[0].tax_amt.value
	
	var invoice_amt = document.forms[0].invoice_amt.value;
	var net_amt = document.forms[0].net_amt.value;
	
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
	if(trim(gx_bu_unit)=="" || gx_bu_unit == "0")
	{
		msg+="Select Gas Exchange Business Unit!\n";
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
	if(invoice_type == "OR")
	{
		var subject_line = document.forms[0].subject_line.value;
		if(trim(subject_line) == "")
		{
			msg+="Enter Other Invoice String!\n";
			flag=false;
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
	if(trim(invoice_ref)=="")
	{
		msg+="Select Invoice No!\n";
		flag=false;
	}
	if(trim(invoice_dt)=="")
	{
		msg+="Select Invoice Date!\n";
		flag=false;
	}
	if(trim(invoice_due_dt)=="")
	{
		msg+="Select Invoice Due Date!\n";
		flag=false;
	}
	if(trim(invoice_raised_in)=="" || invoice_raised_in=="0")
	{
		msg+="Select Invoice Raised In!\n";
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
	
	var url = "frm_purchase_f_flow_invoice.jsp?u="+u+"&counterparty_cd="+counterparty_cd+"&month="+month+"&year="+year+"&invoice_type="+inv_typ+"&opration="+opration+
			"&mapping_id="+mapping_id+"&address_type="+add_type+"&bu_unit="+bu+"&billing_cycle="+freq+
			"&period_start_dt="+p_start_dt+"&period_end_dt="+p_end_dt+"&inv_no="+inv_no+"&inv_seq="+inv_seq+"&financial="+financial;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function printPDF()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var bu_unit = document.forms[0].bu_unit.value;
	var gx_counterparty_cd = document.forms[0].gx_counterparty_cd.value;
	var gx_bu_unit = document.forms[0].gx_bu_unit.value;
	//var billing_cycle = document.forms[0].billing_cycle.value;
	var mapping_id = document.forms[0].mapping_id.value;
	//var address_type = document.forms[0].address_type.value;
	var invoice_type = document.forms[0].invoice_type.value;
	var invoice_no = document.forms[0].invoice_no.value;
	var invoice_seq = document.forms[0].invoice_seq.value;
	var financial_year = document.forms[0].financial_year.value;
	
	var contract_type = document.forms[0].contract_type.value;
	
	var is_print="0";
	
	var url = "../gx/pdf_gx_f_flow_invoice.jsp?counterparty_cd="+counterparty_cd+"&bu_unit="+bu_unit+
		"&gx_counterparty_cd="+gx_counterparty_cd+"&gx_bu_unit="+gx_bu_unit+
		"&is_print="+is_print+"&contract_type="+contract_type+
		"&invoice_type="+invoice_type+"&mapping_id="+mapping_id+
		"&inv_no="+invoice_no+"&inv_seq="+invoice_seq+"&financial="+financial_year;
	
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
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].opration_type.value="CHECK";
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
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].opration_type.value="AUTHORIZE";
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
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].opration_type.value="APPROVE";
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
	document.forms[0].tcs_amt_unit.value=obj.value
	document.forms[0].tds_amt_unit.value=obj.value
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
			newWindow = window.open("frm_tax_structure_list.jsp?type="+type,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_tax_structure_list.jsp?type="+type,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
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
			newWindow = window.open("frm_tcs_tds_tax_structure_list.jsp?type="+type+"&tax_app="+tax_app,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_tcs_tds_tax_structure_list.jsp?type="+type+"&tax_app="+tax_app,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
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

function refreshParent()
{
	window.opener.refresh();
}

function copyInvoiceNo()
{
	var invoice_ref = document.forms[0].invoice_ref;
	
	if(invoice_ref.value.length>16)
	{
		alert("Invoice# 16 digit allowed!");
		invoice_ref.value="";
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
</script>

</head>
<jsp:useBean class="com.etrm.fms.gx.DataBean_Gx_Invoice" id="remittance" scope="request"></jsp:useBean>
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

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");

if(month.length() == 1)
{
	month="0"+month; 
}

String mapping_id=request.getParameter("mapping_id")==null?"":request.getParameter("mapping_id");
String gx_bu_unit=request.getParameter("gx_bu_unit")==null?"":request.getParameter("gx_bu_unit");
String bu_unit=request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");

String inv_no=request.getParameter("inv_no")==null?"":request.getParameter("inv_no");
String inv_seq=request.getParameter("inv_seq")==null?"":request.getParameter("inv_seq");
String financial=request.getParameter("financial")==null?"":request.getParameter("financial");

String activity_type =request.getParameter("activity_type")==null?"":request.getParameter("activity_type");
String contact_person=request.getParameter("contact_person")==null?"0":request.getParameter("contact_person");
String bu_contact_person=request.getParameter("bu_contact_person")==null?"0":request.getParameter("bu_contact_person");
String gx_counterparty_cd=request.getParameter("gx_counterparty_cd")==null?"":request.getParameter("gx_counterparty_cd");

String tcs_tds=request.getParameter("tcs_tds")==null?"TDS":request.getParameter("tcs_tds");

String contract_type="";
String cont_no="";
String cont_rev="";
String agmt_no="";
String agmt_rev="";
if(!mapping_id.equals("") && !mapping_id.equals("0"))
{
	String[] temp = mapping_id.split("-");
	contract_type=temp[0];
	agmt_no=temp[1];
	agmt_rev=temp[2];
	cont_no=temp[3];
	cont_rev=temp[4];
}

remittance.setCallFlag("F_FLOW_INVOICE");
remittance.setComp_cd(owner_cd);
remittance.setCounterparty_cd(counterparty_cd);
remittance.setAgmt_no(agmt_no);
remittance.setAgmt_rev_no(agmt_rev);
remittance.setCont_no(cont_no);
remittance.setCont_rev_no(cont_rev);
remittance.setContract_type(contract_type);
remittance.setMonth(month);
remittance.setYear(year);
remittance.setBu_unit(bu_unit);
remittance.setGx_bu_unit(gx_bu_unit);
remittance.setInvoice_type(invoice_type);
remittance.setOpration(opration);
remittance.setInv_no(inv_no);
remittance.setInv_seq(inv_seq);
remittance.setFinancial(financial);
remittance.setGx_counterparty_cd(gx_counterparty_cd);
remittance.init();

String tempGxCountCd = remittance.getGx_counterparty_cd();
if(gx_counterparty_cd.equals(""))
{
	gx_counterparty_cd=tempGxCountCd;
}

Vector VCOUNTERPARTY_CD = remittance.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = remittance.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = remittance.getVCOUNTERPARTY_ABBR();

Vector VDEAL_NO = remittance.getVDEAL_NO();
Vector VMAPPING_ID = remittance.getVMAPPING_ID();
Vector VPLANT_SEQ = remittance.getVPLANT_SEQ();
Vector VPLANT_ABBR = remittance.getVPLANT_ABBR();
Vector VGX_BU_CONTACT_PERSON = remittance.getVGX_BU_CONTACT_PERSON();
Vector VGX_BU_CONTACT_PERSON_CD = remittance.getVGX_BU_CONTACT_PERSON_CD();
Vector VSEL_BU_CD = remittance.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = remittance.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_BU_PLANT_ABBR = remittance.getVSEL_BU_PLANT_ABBR();
Vector VBU_CONTACT_PERSON = remittance.getVBU_CONTACT_PERSON();
Vector VBU_CONTACT_PERSON_CD = remittance.getVBU_CONTACT_PERSON_CD();
Vector VLINK_INVOICE_NO = remittance.getVLINK_INVOICE_NO();

Vector VMULTI_TAX_STRUCT = remittance.getVMULTI_TAX_STRUCT();

int len_multi_tax=0;

String invoice_no= remittance.getInvoice_no();
String invoice_seq= remittance.getInvoice_seq();
String financial_year = remittance.getFinancial_year();
String invoice_ref = remittance.getInvoice_ref();
String invoice_dt = remittance.getInvoice_dt();
String invoice_due_dt = remittance.getInvoice_due_dt();
String invoice_category = remittance.getInvoice_category();
String num_line = remittance.getNum_line();
String linked_invoice = remittance.getLinked_invoice();
String note = remittance.getNote();
String gx_counterpty_abbr = remittance.getGx_couterpty_abbr();
String gx_bu_contact_person_cd = remittance.getGx_bu_contact_person_cd();
String bu_contact_person_cd = remittance.getBu_contact_person_cd();
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
String other_inv_str = remittance.getOther_inv_str();
String qty_mmbtu = remittance.getQty_mmbtu();
String gross_amt = remittance.getGross_amt();
String exchang_rate = remittance.getExchang_rate();
String gross_amt1 = remittance.getGross_amt1();
String tax_amt = remittance.getTax_amt();
String invoice_amt = remittance.getInvoice_amt();
String invoice_adj_amt = remittance.getInvoice_adj_amt();
String net_payable = remittance.getNet_payable();
String invoice_raised_in = remittance.getInvoice_raised_in();
String amount_in_word = remittance.getAmount_in_word();
String tax_struct_cd=remittance.getTax_struct_cd();
String tax_struct_dt=remittance.getTax_struct_dt();
String tax_struct_info=remittance.getTax_struct_info();

String tds_amount=remittance.getTds_amount();
String tds_factor=remittance.getTds_factor();
String tds_struct_cd=remittance.getTds_struct_cd();
String tds_struct_dt=remittance.getTds_struct_dt();
String tds_struct_info=remittance.getTds_struct_info();

String tcs_amount=remittance.getTcs_amount();
String tcs_factor=remittance.getTcs_factor();
String tcs_struct_cd=remittance.getTcs_struct_cd();
String tcs_struct_dt=remittance.getTcs_struct_dt();
String tcs_struct_info=remittance.getTcs_struct_info();

String applicable_abbr = remittance.getApplicable_abbr();
if(applicable_abbr.equals(""))
{
	applicable_abbr=tcs_tds;
}

if(invoice_raised_in.equals("0") || invoice_raised_in.equals(""))
{
	invoice_raised_in="1";
}

Vector VLINE_NO = remittance.getVLINE_NO();
Vector VLINE_DESC = remittance.getVLINE_DESC();
Vector VUNIT = remittance.getVUNIT();
Vector VQTY = remittance.getVQTY();
Vector VRATE = remittance.getVRATE();
Vector VAMOUNT = remittance.getVAMOUNT();

boolean submission_chk = remittance.getSubmission_chk();

if(!contact_person.equals("0") && !contact_person.equals(""))
{
	gx_bu_contact_person_cd=contact_person;
}
if(!bu_contact_person.equals("0") && !bu_contact_person.equals(""))
{
	bu_contact_person_cd=bu_contact_person;
}

if(invoice_category.equals(""))
{
	invoice_category="S";
}
%>
<body onload="<%if(!msg.equals("")){%>refreshParent();<%}%>" <%if(!activity_type.equals("PREPARE")) {%>style="pointer-events: none;"<%} %>>
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
					    	GX Free Flow Remittance Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5" <%if(opration.equals("MODIFY")){%>style="pointer-events: none;"<%}%>>
						<div class="col-sm-1 col-xs-1 col-md-1"></div>						
						<div class="col-sm-3 col-xs-3 col-md-3">
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
					  					<%for(int i=(currentYear+1); i > (currentYear-10);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
					  		</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">  	
					    			<label class="form-label"><b>Counterparty</b></label>
					  			</div>
								<div class="col">  
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=opration%>');">
										<option value="">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
					  	<div class="col-sm-1 col-xs-1 col-md-1"></div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th rowspan="2">Contract No</th>
											<th colspan="3">GX Detail</th>
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
												<div style="width:150px;">
													<select class="form-select form-select-sm" name="mapping_id" onchange="refresh('<%=opration%>');">
														<option value="">--Select--</option>
														<%for(int i=0;i<VMAPPING_ID.size();i++){ %>
														<option value="<%=VMAPPING_ID.elementAt(i)%>"><%=VDEAL_NO.elementAt(i)%></option>
														<%} %>
													</select>
													<script>document.forms[0].mapping_id.value="<%=mapping_id%>"</script>
												</div>
											</td>
											<td align="center">
												<%=gx_counterpty_abbr%>
											</td>
											<td align="center">
												<div style="width:150px;">
													<select class="form-select form-select-sm" name="gx_bu_unit" onchange="refresh('<%=opration%>');">
														<option value="">--Select--</option>
														<%for(int i=0;i<VPLANT_SEQ.size();i++){ %>
														<option value="<%=VPLANT_SEQ.elementAt(i)%>"><%=VPLANT_ABBR.elementAt(i)%></option>
														<%} %>
													</select>
													<script>document.forms[0].gx_bu_unit.value="<%=gx_bu_unit%>"</script>
												</div>											
											</td>
											<td align="center">
												<div style="width:150px;">
													<select class="form-select form-select-sm" name="contact_person">
														<option value="0">--Select--</option>
														<%for(int i=0;i<VGX_BU_CONTACT_PERSON_CD.size();i++){ %>
														<option value="<%=VGX_BU_CONTACT_PERSON_CD.elementAt(i)%>"><%=VGX_BU_CONTACT_PERSON.elementAt(i)%></option>
														<%} %>
													</select>
													<script>document.forms[0].contact_person.value="<%=gx_bu_contact_person_cd%>"</script>
												</div>
											</td>
											<td align="center">
												<div style="width:150px;">
													<select class="form-select form-select-sm" name="bu_unit" onchange="refresh('<%=opration%>');">
														<option value="">--Select--</option>
														<%for(int i=0;i<VSEL_BU_PLANT_SEQ_NO.size();i++){ %>
														<option value="<%=VSEL_BU_PLANT_SEQ_NO.elementAt(i)%>"><%=VSEL_BU_PLANT_ABBR.elementAt(i)%></option>
														<%} %>
													</select>
													<script>document.forms[0].bu_unit.value="<%=bu_unit%>"</script>
												</div>
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
					<div class="row m-b-5">
						<div class="col-sm-6 col-xs-6 col-md-6">  
							<div class="form-group row">
								<div class="col-auto">
									<h5><span class="badge bg-info text-dark">TCS/TDS Applicable :</span></h5>
								</div> 
								<div class="col-auto">
									<select class="form-select form-select-sm" name="tcs_tds" onchange="enableTCSTDS(this);">
										<option value="NA">NA</option>
										<option value="TDS">TDS</option>
										<option value="TCS">TCS</option>
									</select>
									<script>
									document.forms[0].tcs_tds.value="<%=applicable_abbr%>"
									</script>
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
				    			<label class="form-label"><b>Invoice Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
							<%if(invoice_type.equals("OR")){ %>
								<div class="col-auto">
							<%}else { %>
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    		<%} %>
				    				<select class="form-select form-select-sm" name="invoice_type" onchange="refresh('<%=opration%>');">
										<option value="">--Select--</option>
										<option value="CR">Credit Note</option>
										<option value="DR">Debit Note</option>
										<option value="LP">Late Payment Invoice</option>
										<option value="OR">Other</option>
									</select>
									<script>document.forms[0].invoice_type.value="<%=invoice_type%>"</script>				
				    			</div>
				    		<%if(invoice_type.equals("OR")){ %>
				    			<div class="col">
				    				<input type="text" class="form-control form-control-sm" name="subject_line" value="<%=other_inv_str%>">
				    			</div>
				    		<%} %>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Invoice Category<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="radio" name="invoice_category" value="P" <%if(invoice_category.equals("P")){ %>checked<%}%>>&nbsp;Product&nbsp;&nbsp;
				    				<input type="radio" name="invoice_category" value="S" <%if(invoice_category.equals("S")){ %>checked<%}%>>&nbsp;Service				
				    			</div>
				    		</div>
				    	</div>
				    </div>
				    <div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Invoice#<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<!-- <div class="col"> -->
				    				<input type="hidden" class="form-control form-control-sm" name="invoice_no" value="<%=invoice_no%>" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="invoice_seq" value="<%=invoice_seq%>" readOnly>
				    			<!-- </div> -->
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="invoice_ref" onblur="copyInvoiceNo();" value="<%=invoice_ref%>">
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Link Invoice Ref</b></label>
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
								<label class="form-label"><b>Invoice Due Date<span class="s-red">*</span></b></label>
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
										<option value="2">USD</option>
									</select>
									<script>document.forms[0].invoice_raised_in.value="<%=invoice_raised_in%>"</script>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Invoiced MMBTU</b></label>
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
				    					<input type="text" class="form-control form-control-sm" name="tds_amt" value="<%=tds_amount%>" maxlength='20' style="text-align:right;">
				    					<select class="form-select form-select-sm" name="tds_amt_unit" disabled>
											<option value="0">--Select--</option>
						  					<option value="1">INR</option>
											<option value="2">USD</option>
										</select>
										<script>document.forms[0].tds_amt_unit.value="<%=invoice_raised_in%>"</script>
				      				</div>
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
													<input type="text" class="form-control form-control-sm" name="item_seq" value="<%=VLINE_NO.elementAt(i)%>" style="width:50px;" readOnly>
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
													<a onclick="addrow();" id="minus<%=i+1%>" <%if((i+1) == 1){ %>style="display:none"<%} %><%else if((VLINE_NO.size() - 1) == i){ %><%}else{%>style="display:none"<%} %>>
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
														<option value="0">--Select--</option>
									  					<option value="1">INR</option>
														<option value="2">USD</option>
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
														<option value="0">--Select--</option>
									  					<option value="1">INR</option>
														<option value="2">USD</option>
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
														<option value="0">--Select--</option>
									  					<option value="1">INR</option>
														<option value="2">USD</option>
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
													<input type="text" class="form-control form-control-sm" name="tcs_amt" value="<%=tcs_amount%>" maxlength='20' style="width:100px;text-align:right;">
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
														<option value="0">--Select--</option>
									  					<option value="1">INR</option>
														<option value="2">USD</option>
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
				    <div class="row m-b-5">
				    	<div class="col-sm-6 col-xs-6 col-md-6" align="right"> 
				    		<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();" <%if(invoice_check_flag.equals("Y")){ %>disabled<%} %> <%if(!activity_type.equals("PREPARE")){ %>style="display:none;"<%} %>>
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

<input type="hidden" name="gx_counterparty_cd" value="<%=gx_counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" value="<%=agmt_rev%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev" value="<%=cont_rev%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="financial_year" value="<%=financial_year%>">

<input type="hidden" name="activity_type" value="<%=activity_type%>">

<input type="hidden" name="item_size" id="item_size" value="<%=VLINE_NO.size()%>">

<input type="hidden" class="form-control form-control-sm" name="no_of_line" value="<%=num_line%>" maxlength="2">

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