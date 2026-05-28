<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.util.Vector"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refreshParent(accroid,msg,msg_type)
{
	window.opener.refershPar(accroid,msg,msg_type);
	window.close();
}

function refresh()
{
	var supp_cd = document.forms[0].supp_cd.value;
	var vend_cd = document.forms[0].vend_cd.value;
	var inv_dt = document.forms[0].inv_dt.value;
	var curr = document.forms[0].currency.value;
	var sac_cd=document.forms[0].sac_cd.value;
	var due_dt=document.forms[0].due_dt.value;
	var desc_item = document.forms[0].desc_item.value;
	var amount = document.forms[0].amount.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var inv_type = document.forms[0].inv_type.value;
	var operation = document.forms[0].operation.value;
	var accord = document.forms[0].accord.value;
	var u = document.forms[0].u.value;

	
	var url = "frm_oth_inv_hppl_seipl.jsp?supp_cd="+supp_cd+"&vend_cd="+vend_cd+"&inv_dt="+inv_dt+"&currency="+curr
			+"&desc_item="+desc_item+"&amount="+amount+"&sac_cd="+sac_cd+
			"&due_dt="+due_dt+"&accord="+accord+"&month="+month+"&year="+year+"&inv_type="+inv_type+"&operation="+operation+"&u="+u;

	location.replace(url);
}

function textCounter(field, countfield, maxlimit)
{
	if(field.value.length > maxlimit)
	{
		field.value = field.value.substring(0, maxlimit);
	}
	else
	{
		countfield.value = maxlimit - field.value.length;
	}
}
 
function doSubmit()
{
	var supp_cd = document.forms[0].supp_cd.value;
	var vend_cd = document.forms[0].vend_cd.value;
	var sac_cd = document.forms[0].sac_cd.value;
	var inv_dt = document.forms[0].inv_dt.value;
	var tax_struct = document.forms[0].tax_struct_nm.value;
	var desc_item = document.forms[0].desc_item.value;
	var amount = document.forms[0].amount.value;
	var supp_gstin_no = document.forms[0].supp_gstin_no.value;
	var vend_gstin_no = document.forms[0].vend_gstin_no.value;
	var total_gross = document.forms[0].total_gross.value;
	var total_less = document.forms[0].total_less.value;
	var gross_revenue = document.forms[0].gross_revenue.value;
	var gross_less = document.forms[0].gross_less.value;
	var item_size = parseInt(document.getElementById("item_size").value) || 1;
	var item_size1 = parseInt(document.getElementById("item_size1").value) || 1;
	var u = document.forms[0].u.value;
	
	var msg="";
	var flag=true;

	if(parseFloat(total_gross)<parseFloat(total_less))
	{
		msg+="Gross Revenue should be higher than Water Front Royalty (WFR)!";
		flag=false
	}
	
	if(supp_cd=="0" || trim(supp_cd)=="")
	{
		msg+="Select Supplier Name!\n";
		flag=false
	}
	if(vend_cd=="0" || trim(vend_cd)=="")
	{
		msg+="Select Vendor Name!\n";
		flag=false
	}
	if(sac_cd=="0" || trim(sac_cd)=="")
	{
		msg+="Select SAC Name!\n";
		flag=false
	}
	if(inv_dt=="0" || trim(inv_dt)=="")
	{
		msg+="Enter Invoice Date!\n";
		flag=false
	}
	if(tax_struct=="0" || trim(tax_struct)=="" || trim(tax_struct)=="IGST")
	{
		msg+="Select Tax Structure!\n";
		flag=false
	}
	if(desc_item==" " || trim(desc_item)=="")
	{
		msg+="Enter Description of Line Item!\n";
		flag=false
	}
	
	if(gross_revenue==" " || trim(gross_revenue)=="")
	{
		msg+="Enter Gross Revenue!\n";
		flag=false
	}
	if(gross_less==" " || trim(gross_less)=="")
	{
		msg+="Enter Less: Water Front Royalty!\n";
		flag=false
	}
	
	if(amount=="" || amount=="0" || trim(amount)=="")
	{
		msg+="Enter Line Item Amount!\n";
		flag=false
	}

	if(supp_gstin_no==" " || trim(supp_gstin_no)=="")
	{
		msg+="Please configure GSTIN No for Selected Supplier before Invoice Submission!\n";
		flag=false
	}

	if(vend_gstin_no==" " || trim(vend_gstin_no)=="")
	{
		msg+="Please configure GSTIN No for Selected Vendor before Invoice Submission!\n";
		flag=false
	}
 
	var item_size = document.forms[0].gross_amount;
	var item_size1 = document.forms[0].less_amount;
	
	if(item_size!='' && item_size!=null && item_size!=undefined)
	{
		if(item_size.length>0)
		{
			var cnt=0,cnt1=0;
			for (var i = 0; i < item_size.length; i++) 
			{
				if(document.forms[0].gross_amount[i].value=='' || document.forms[0].des[i].value=='')
				{
					if(document.forms[0].gross_amount[i].value=='')
	 	 	 		{
	 	 				cnt++;
	 	 	 	 		flag = false;
	 	 	 	 	}
	 	 			if(document.forms[0].des[i].value=='')
	 	 	 		{
	 	 				cnt1++;
	 	 	 	 		flag = false;
	 	 	 	 	}
				}
			}
			if(cnt>0)
			{
				msg += "Amount Cannot Be Blank For any line Item of Gross Revenue\n";
	 		}
			if(cnt1>0)
	 		{
	 			msg += "Description Cannot Be Blank For any line Item of Gross Revenue!\n";
	 		}
		}
		else 
		{
			if(document.forms[0].gross_amount.value=='' || document.forms[0].des.value=='')
			{
				if(document.forms[0].gross_amount.value=='')
 	 	 		{
					msg += "Amount Cannot Be Blank For any line Item of Gross Revenue\n";
 	 	 	 		flag = false;
 	 	 	 	}
 	 			if(document.forms[0].des.value=='')
 	 	 		{
 	 				msg += "Description Cannot Be Blank For any line Item of Gross Revenue!\n";
 	 	 	 		flag = false;
 	 	 	 	}
			}
		}
	}
	
	if(item_size1!='' && item_size1!=null && item_size1!=undefined)
	{
		if(item_size1.length>0)
		{
			var cnt=0,cnt1=0;
			for (var i = 0; i < item_size1.length; i++) 
			{
				if(document.forms[0].less_amount[i].value=='' || document.forms[0].less_des[i].value=='')
				{
					if(document.forms[0].less_amount[i].value=='')
	 	 	 		{
	 	 				cnt++;
	 	 	 	 		flag = false;
	 	 	 	 	}
	 	 			if(document.forms[0].less_des[i].value=='')
	 	 	 		{
	 	 				cnt1++;
	 	 	 	 		flag = false;
	 	 	 	 	}
				}
			}
			if(cnt>0)
			{
				msg += "Amount Cannot Be Blank For any line Item of WFR\n";
	 		}
			if(cnt1>0)
	 		{
	 			msg += "Description Cannot Be Blank For any line Item of WFR!\n";
	 		}
		}
		else 
		{
			if(document.forms[0].less_amount.value=='' || document.forms[0].less_des.value=='')
			{
				if(document.forms[0].less_amount.value=='')
 	 	 		{
					msg += "Amount Cannot Be Blank For any line Item of WFR\n";
 	 	 	 		flag = false;
 	 	 	 	}
 	 			if(document.forms[0].less_des.value=='')
 	 	 		{
 	 				msg += "Description Cannot Be Blank For any line Item of WFR!\n";
 	 	 	 		flag = false;
 	 	 	 	}
			}
		}
	}
	
	
	if(flag)
	{
		//document.getElementById("loading").style.visibility = "visible";
		if(confirm("Do You want to submit this Invoice???"))
		{
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}


var newWindow;

function refreshChild(counterparty_cd)
{
	var operation = document.forms[0].operation.value;
	
	document.forms[0].counterparty_cd.value=counterparty_cd;
	
	refresh(operation);
}

function checkEffectiveDate(input, sysDateStr) 
{
    var userDate = new Date(input.value);
    var sysDate = new Date(sysDateStr);
    
    if (userDate > sysDate) {
        alert("Date cannot be after system date.");
        input.value = "";
        input.focus();
        return false;
    }

    return true;
}



var total_gross = 0;
var total_less = 0;

function grossamtcalc(index)
{
	var calamt = 0;
	var gross_amounts = document.forms[0].gross_amount;
	var gross_revenue = parseFloat(document.forms[0].gross_revenue.value) || 0;
	
	
	if(gross_amounts!='' && gross_amounts!=null && gross_amounts!=undefined)
	{
		calamt = gross_revenue;
		if(gross_amounts.length>0)
		{
			if (gross_revenue != '') 
			{
				for (var i = 0; i < gross_amounts.length; i++) 
				{
					var gross_amount = (gross_amounts[i].value) || 0;
					var sign = document.forms[0].gross_sign[i].value;
					
					if (sign == '+') 
					{
						calamt = parseFloat(calamt)+parseFloat(gross_amount);
					}
					else if (sign == '-') 
					{
						calamt = parseFloat(calamt)-parseFloat(gross_amount);
					}	
				}
			}
			else 
			{
				alert("Please Enter Gross Revenue!");
				return;
			}
		}
		else 
		{
			if (gross_revenue != '') 
			{ 
				var gross_amount = parseFloat(document.forms[0].gross_amount.value) || 0;
				var gross_sign = document.forms[0].gross_sign.value;
				if (gross_sign == '+') 
				{
					calamt = parseFloat(calamt)+parseFloat(gross_amount);
				}
				else if (gross_sign == '-') 
				{
					calamt = parseFloat(calamt)-parseFloat(gross_amount);
				}
			}
			else 
			{
				alert("Please Enter Gross Revenue!");
				return;
			}
		}
	}
	else 
	{
		calamt = gross_revenue;
	}
	
	return calamt;
}

function lessamtcalc(index)
{
	var calamt1 = 0;
	var less_amounts = document.forms[0].less_amount;
	var gross_less = parseFloat(document.forms[0].gross_less.value) || 0;
	
	if(less_amounts!='' && less_amounts!=null && less_amounts!=undefined)
	{
		calamt1 = gross_less;
		if(less_amounts.length>0)
		{
			if (gross_less != '') 
			{
				for (var i = 0; i < less_amounts.length; i++) 
				{
					var less_amount = parseFloat(less_amounts[i].value) || 0;
					var sign = document.forms[0].less_sign[i].value;
					
					if (sign == '+') 
					{
						calamt1 = parseFloat(calamt1)+parseFloat(less_amount);
					}
					else if (sign == '-') 
					{
						calamt1 = parseFloat(calamt1)-parseFloat(less_amount);
					}	
				}
			}
			else 
			{
				alert("Please Enter Less: Water Front Royalty (WFR)!");
				return;
			}
		}
		else 
		{
			if (gross_less != '') 
			{
					var less_amount = parseFloat(document.forms[0].less_amount.value) || 0;
					var less_sign = document.forms[0].less_sign.value;
					
					if (less_sign == '+') 
					{
						calamt1 = parseFloat(calamt1)+parseFloat(less_amount);
					}
					else if (less_sign == '-') 
					{
						calamt1 = parseFloat(calamt1)-parseFloat(less_amount);
					}
			}
			else 
			{
				alert("Please Enter Less: Water Front Royalty (WFR)!");
				return;
			}
			
		}
	}
	else 
	{
		calamt1 = gross_less;
	}
	return calamt1;
}
function calcAmount(operation, index1, index2) 
{
	var amount = '';
	
	if(index1!='')
	{
		total_gross = grossamtcalc(index1);
	}
	
	if(index2!='')
	{
		total_less = lessamtcalc(index2);
	}
	
	amount = parseFloat(total_gross) - parseFloat(total_less);
	
	document.forms[0].total_gross.value = parseFloat(total_gross).toFixed(2);
	document.forms[0].total_less.value = parseFloat(total_less).toFixed(2);

	if (amount < 0) 
	{
		alert("Gross Revenue should be higher than Water Front Royalty (WFR)");
		amount = 0;
		return;
	}

	document.forms[0].amount.value = parseFloat(amount).toFixed(2);
	document.forms[0].total_amount.value = parseFloat(Math.round((amount * 3) / 100)).toFixed(2);
	
	var tot_amt = Number(document.forms[0].tax_amt.value) + Number(document.forms[0].total_amount.value);
	document.forms[0].total_amt_inr.value = parseFloat(tot_amt).toFixed(2);
	
	taxCalcAmt();
}

function taxCalcAmt()
{
	var tax_name = document.forms[0].tax_struct_nm.value;
	var total_amount = document.forms[0].total_amount.value;
	
	if (total_amount == "" || total_amount == " ") 
	{
		total_amount = "0";
	}
	
	
	var tax1="";
	var tax2="";
	var tax_amount="";
	var sub_tax_amt="";
	var sub_tax_name=tax_name.split(", ");
	
	if(!tax_name.includes(","))
	{
		if(!tax_name || tax_name.trim() === "")
		{
			tax1 = 0;
			tax_amount = document.forms[0].total_amount.value * tax1 / 100;
		}
		else
		{
			tax1 = (tax_name.split(",")[0].split(" ")[1].split("%")[0] * document.forms[0].total_amount.value) / 100;
			tax_amount = tax1;
		}
		document.forms[0].tax_amt.value=parseFloat(Math.round(tax_amount)).toFixed(2);
	}
 	else 
 	{
 		for(i=0;i<sub_tax_name.length;i++) 
 		{
 			tax2 = Math.round((sub_tax_name[0].split(",")[0].split(" ")[1].split("%")[0] * document.forms[0].total_amount.value) / 100);
			sub_tax_amt = tax2 * 2;
 		}
 		document.forms[0].tax_amt.value=parseFloat(Math.round(sub_tax_amt)).toFixed(2);
 	}
	
	var tot_amt=Number(document.forms[0].tax_amt.value)  + Number(document.forms[0].total_amount.value);
	document.forms[0].total_amt_inr.value = parseFloat(tot_amt).toFixed(2); 
	
}
function openTaxStructMst(supp_state,vend_state)
{
	var type="";
	if(document.forms[0].invoice_category.checked)
	{
		type=document.forms[0].invoice_category.value;
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
			newWindow = window.open("frm_other_inv_tax_structure_list.jsp?type="+type+"&supp_state="+supp_state+"&vend_state="+vend_state,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_other_inv_tax_structure_list.jsp?type="+type+"&supp_state="+supp_state+"&vend_state="+vend_state,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function setTaxStructDetail(tax_struct_cd,tax_struct_nm,tax_struct_eff_dt)
{
	var info = "Tax ("+tax_struct_nm+")";
	document.forms[0].tax_struct_nm.value=tax_struct_nm
	document.forms[0].tax_struct_info.value=info
	document.forms[0].tax_cd.value=tax_struct_cd
	document.forms[0].tax_dt.value=tax_struct_eff_dt
	taxCalcAmt();
	
}

function checkInvoiceDueDate(obj,obj1,flag)
{
	if((obj.value!="" && trim(obj.value) != "" && obj.value != null) && (obj1.value!="" && trim(obj1.value) != "" && obj1.value != null))
	{
		if(flag=="F")
		{
			var count = compareDate(obj.value,obj1.value);
			if(parseInt(count) == 1)
			{
				alert("Invoice Date should be less or equal Invoice Due Date!")
				obj.value="";
				return false;
			}
		}
		else if(flag=="T")
		{
			var count = compareDate(obj.value,obj1.value);
			if(parseInt(count) == 1)
			{
				alert("Invoice Due Date should be grater or equal Invoice Date!")
				obj1.value="";
				return false;
			}
		}
	}
}

function addrow()
{
	
	var max_seq = document.getElementById("item_size").value;
	var prev_minus= document.getElementById("minus"+max_seq);
	var prev_plus= document.getElementById("plus"+max_seq);
	var new_seq_no = parseInt(max_seq)+1;
	var operation = document.forms[0].operation.value
	
	if(parseInt(new_seq_no) > 99)
	{
		alert("Maximum number of lines added for the Invoice!")	
	}
	else
	{
		var tab_name = document.getElementById("itemgross");
		var row_new = document.createElement("tr");
		row_new.id = "row_1" + new_seq_no;

		var td00 = document.createElement("td");
		var div00 = document.createElement("div");
		td00.appendChild(div00);

		var td01 = document.createElement("td");
		var div01 = document.createElement("div");
		div01.align = "center";
		
		var input03 = document.createElement("input");
		input03.name = "des";
		input03.id = "des" + new_seq_no;
		input03.type = "text";
		input03.className = "form-control form-control-sm";
		input03.style.width = "320px";
		input03.maxlength = "40";
		input03.placeholder = "Description";

		div01.appendChild(input03);
		td01.appendChild(div01);
		
		var td04 = document.createElement("td");
		var div04 = document.createElement("div");
		td04.appendChild(div04);
		
		var td02 = document.createElement("td");
		td02.style.textAlign = "center";

		var div02 = document.createElement("div");
		div02.style.display = "flex";
		div02.style.justifyContent = "center";
		div02.style.alignItems = "center";
		div02.style.gap = "6px";
		
		var input01 = document.createElement("select");
		input01.name = "gross_sign";
		input01.id = "gross_sign" + new_seq_no;
		input01.className = "form-select form-select-sm";
		input01.style.width = "45px";
		input01.setAttribute("onchange","calcAmount('"+operation+"','"+new_seq_no+"','"+max_seq+"');");

		var option1 = new Option("+", "+");
		var option2 = new Option("-", "-");
		input01.appendChild(option1);
		input01.appendChild(option2);

		input01.appendChild(option1);
		input01.appendChild(option2);
		
		var td02 = document.createElement("td");
		var input02 = document.createElement("input");
		input02.name = "gross_amount";
		input02.id = "gross_amount"+new_seq_no;
		input02.type = "text";
		input02.className="form-control form-control-sm";
		input02.style.width='100px';
		input02.style.textalign='left';
		input02.maxlength = "30";
		input02.autcomplete = "off"; 
		input02.setAttribute("onchange","calcAmount('"+operation+"','"+new_seq_no+"','"+max_seq+"');");
		
		div02.appendChild(input01);
		div02.appendChild(input02);
		td02.appendChild(div02);
		
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

		td07.appendChild(input07);
		td07.appendChild(document.createTextNode("      "))
		td07.appendChild(input07_1);
		
		row_new.appendChild(td00);
		row_new.appendChild(td01);
		row_new.appendChild(td04);
		row_new.appendChild(td02);
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

function addrow1()
{
	
	var max_seq = document.getElementById("item_size1").value;
	var prev_minus= document.getElementById("minus1"+max_seq);
	var prev_plus= document.getElementById("plus1"+max_seq);
	var new_seq_no = parseInt(max_seq)+1;
	var operation = document.forms[0].operation.value
	
	if(parseInt(new_seq_no) > 99)
	{
		alert("Maximum number of lines added for the Invoice!")	
	}
	else
	{
		var tab_name = document.getElementById("itemless");
		var row_new = document.createElement("tr");
		row_new.id = "row_2" + new_seq_no;

		var td00 = document.createElement("td");
		var div00 = document.createElement("div");
		td00.appendChild(div00);

		var td01 = document.createElement("td");
		var div01 = document.createElement("div");
		div01.align = "center";

		var input03 = document.createElement("input");
		input03.name = "less_des";
		input03.id = "less_des" + new_seq_no;
		input03.type = "text";
		input03.className = "form-control form-control-sm";
		input03.style.width = "320px";
		input03.maxlength = "40";
		input03.placeholder = "Description";

		div01.appendChild(input03);
		td01.appendChild(div01);

		var td04 = document.createElement("td");
		var div04 = document.createElement("div");
		td04.appendChild(div04);

		var td02 = document.createElement("td");
		td02.style.textAlign = "center";

		var div02 = document.createElement("div");
		div02.style.display = "flex";
		div02.style.justifyContent = "center";
		div02.style.alignItems = "center";
		div02.style.gap = "6px";

		var input01 = document.createElement("select");
		input01.name = "less_sign";
		input01.id = "less_sign" + new_seq_no;
		input01.className = "form-select form-select-sm";
		input01.style.width = "45px";
		input01.setAttribute("onchange", "calcAmount('" + operation + "','"+max_seq+"','" + new_seq_no + "');");

		var option1 = new Option("+", "+");
		var option2 = new Option("-", "-");
		input01.appendChild(option1);
		input01.appendChild(option2);

		var input02 = document.createElement("input");
		input02.name = "less_amount";
		input02.id = "less_amount" + new_seq_no;
		input02.type = "text";
		input02.className = "form-control form-control-sm";
		input02.style.width = "100px";
		input02.maxlength = "30";
		input02.style.textAlign = "left";
		input02.autcomplete = "off"; 
		input02.setAttribute("onchange", "calcAmount('" + operation + "','"+max_seq+"','" + new_seq_no + "');");

		div02.appendChild(input01);
		div02.appendChild(input02);
		td02.appendChild(div02);

		var td07 = document.createElement("td");
		td07.className="text-center"
		var input07 = document.createElement("a")
		input07.setAttribute("onclick","removeRow1('"+row_new.id+"','"+new_seq_no+"');");
		input07.id = "minus1"+new_seq_no;
		var i=document.createElement("i");
		i.className="fa fa-minus-circle fa-2x";
		
		var input07_1 = document.createElement("a")
		input07_1.setAttribute("onclick","addrow1();");
		input07_1.id = "plus1"+new_seq_no;
		var i_1=document.createElement("i");
		i_1.className="fa fa-plus-circle fa-2x";
		
		input07.appendChild(i);
		input07_1.appendChild(i_1);

		td07.appendChild(input07);
		td07.appendChild(document.createTextNode("      "))
		td07.appendChild(input07_1);

		row_new.appendChild(td00);
		row_new.appendChild(td01);
		row_new.appendChild(td04);
		row_new.appendChild(td02);
		row_new.appendChild(td07);

		tab_name.appendChild(row_new);

		document.forms[0].no_of_line1.value = new_seq_no;
		document.getElementById("item_size1").value = new_seq_no;

		
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
		var total_amt = document.forms[0].amount.value;
		var total_gross = document.forms[0].total_gross.value;
		
		if(total_amt!='' && total_amt!=null && total_amt!=undefined)
		{
			var amt = document.getElementById("gross_amount"+seq_no).value;
			var sign = document.getElementById("gross_sign"+seq_no).value;
			var des = document.getElementById("des"+seq_no).value;
			
			if(amt!='')
			{
				if(sign=='-')
				{	
					total_amt = parseFloat(total_amt)+parseFloat(amt);
					total_gross = parseFloat(total_gross)+parseFloat(amt);
				}
				else
				{
					total_amt = parseFloat(total_amt)-parseFloat(amt);
					total_gross = parseFloat(total_gross)-parseFloat(amt);
				} 
			}
			
			document.forms[0].amount.value = parseFloat(total_amt).toFixed(2);
			document.forms[0].total_amount.value = parseFloat(Math.round((total_amt * 3) / 100)).toFixed(2);
			taxCalcAmt();
		}
		
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

function removeRow1(row_id, seq_no)
{
	var row_cnt = document.forms[0].item_size1.value;
	
	if(parseInt(row_cnt) == parseInt(seq_no))
	{
		var total_amt = document.forms[0].amount.value;
		var total_less = document.forms[0].total_less.value;
		
		if(total_amt!='' && total_amt!=null && total_amt!=undefined)
		{
			var amt = document.getElementById("less_amount"+seq_no).value;
			var sign = document.getElementById("less_sign"+seq_no).value;
			var des = document.getElementById("less_des"+seq_no).value;
			
			if(amt!='')
			{
				if(sign=='-')
				{
					total_amt = parseFloat(total_amt)-parseFloat(amt);
					total_less = parseFloat(total_less)+parseFloat(amt);
				}
				else
				{
					total_amt = parseFloat(total_amt)+parseFloat(amt);
					total_less = parseFloat(total_less)-parseFloat(amt);
				} 
			}
			document.forms[0].total_less.value = parseFloat(total_less).toFixed(2);
			document.forms[0].amount.value = parseFloat(total_amt).toFixed(2);
			document.forms[0].total_amount.value = parseFloat((total_amt*3)/100).toFixed(2);
			taxCalcAmt();
		}
		
		if((parseInt(seq_no)-1) > 1)
		{
			document.getElementById("minus1"+(parseInt(seq_no)-1)).style.display="";
			document.getElementById("plus1"+(parseInt(seq_no)-1)).style.display="";
		}
		else
		{
			document.getElementById("minus1"+(parseInt(seq_no)-1)).style.display="none";
			document.getElementById("plus1"+(parseInt(seq_no)-1)).style.display="";
		}
		
		if(parseFloat(row_cnt) > 0)
		{
			document.forms[0].item_size1.value = parseFloat(row_cnt)-1;
			document.forms[0].no_of_line1.value = parseFloat(row_cnt)-1;
		}
		var row = document.getElementById(row_id);
		row.parentNode.removeChild(row);
	}
	else
	{
		alert("Please First Remove Last Row!!");
	}
}
function resetDueDate(obj)
{
	var inv_dt = obj.value;
	var due_dt = '';
	var maxDay=31;
	if(inv_dt!=null && inv_dt!='' && inv_dt!=' ')
	{			
		var dd=getDay(inv_dt);			
		var mm=getMon(inv_dt);
		var yy=getYear(inv_dt);
				
		if(mm == 1 || mm == 3 || mm == 5 || mm == 7 || mm == 8 || mm == 10 || mm == 12)
			maxDay = 31;
		if(mm == 4 || mm == 6 || mm == 9 || mm == 11)
			maxDay = 30;
		if(mm == 2)
        { 			
			yearStr = yy / 4 +"";
			if(yearStr.indexOf('.') == -1)
				maxDay = 29;
			else
				maxDay = 28;
		}
		
		var day=dd+12;
		if(day > maxDay)
		{
				day%=maxDay;
				mm+=1;
				if(mm > 12)
				{
					mm%=12;
					yy+=1;
				}
				else if(mm < 10)
				{
					mm="0"+ mm;
				}
				if(day < 10){
					day="0"+day;
				}
		}
		else if(day < 10)
		{
			day="0"+day;
			if(mm < 10)
			{
				mm="0"+ mm;
			}
		}	else{
			if(mm < 10)
			{
				mm="0"+ mm;
			}
			if(day < 10){
				day="0"+day;
			}
		}	
		due_dt=day+ "/" + mm + "/" + yy;	
	}	
	document.forms[0].due_dt.value = due_dt;
}
function printPDF(comp_cd,fin_year,inv_seq)
{
	var supp_cd = document.forms[0].supp_cd.value;
	var is_print="0";
	var inv_type="AHPL";
	
	var url = "../other_invoice/pdf_other_invoice.jsp?comp_cd="+comp_cd+"&is_print="+is_print+"&fin_year="+fin_year+
		"&inv_type="+inv_type+"&supp_cd="+supp_cd+"&inv_seq="+inv_seq;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow= window.open(url,"PDF Other Invoice","top=10,left=70,width=900,height=700,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow= window.open(url,"PDF Other Invoice","top=10,left=70,width=900,height=700,scrollbars=1");
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.other_invoice.DataBean_Other_Invoice" id="other_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%

String sysdate = utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String supp_cd=request.getParameter("supp_cd")==null?"0":request.getParameter("supp_cd");
String vend_cd=request.getParameter("vend_cd")==null?"0":request.getParameter("vend_cd");
String sac_cd=request.getParameter("sac_cd")==null?"0":request.getParameter("sac_cd");
String operation=request.getParameter("operation")==null?"INSERT":request.getParameter("operation");
String inv_type=request.getParameter("inv_type")==null?"":request.getParameter("inv_type");
String inv_dt = request.getParameter("inv_dt")==null?"":request.getParameter("inv_dt");
String due_dt = request.getParameter("due_dt")==null?"":request.getParameter("due_dt");
String currency = request.getParameter("currency")==null?"1":request.getParameter("currency");
String tax_structure=request.getParameter("tax_structure")==null?"0":request.getParameter("tax_structure");
String desc_item = request.getParameter("desc_item")==null?"":request.getParameter("desc_item");
String accord = request.getParameter("accord")==null?"":request.getParameter("accord");
String type = request.getParameter("type")==null?"":request.getParameter("type");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String inv_no = request.getParameter("inv_no")==null?"":request.getParameter("inv_no");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String p_start_dt = utildate.getFirstDateOfMonth(month, year);
String p_end_dt = utildate.getLastDateOfMonth(month, year);
String finYr = request.getParameter("fin_year")==null?"":request.getParameter("fin_year");

other_inv.setCallFlag("OTH_INV_AHPL_SHARE");
other_inv.setSupplier_Cd(supp_cd);
other_inv.setVendor_cd(vend_cd);
other_inv.setMonth(month);
other_inv.setYear(year);
other_inv.setInv_type(inv_type);
other_inv.setOpration(operation);
other_inv.setInv_No(inv_no);
other_inv.setInv_seq(invoice_seq);
other_inv.setComp_cd(owner_cd);
other_inv.setFin_yr(finYr);
other_inv.init();

Vector VSUPPLIER_CD = other_inv.getVSUPPLIER_CD();
Vector VSUPPLIER_NM = other_inv.getVSUPPLIER_NM();
Vector VSUPPLIER_ABBR = other_inv.getVSUPPLIER_ABBR();
Vector VENDOR_CD=other_inv.getVVENDOR_CD();
Vector VENDOR_NM = other_inv.getVENDOR_NM();
Vector VENDOR_ABBR = other_inv.getVENDOR_ABBR();
Vector VSAC_CD=other_inv.getVSAC_CD();
Vector VSAC_DESC=other_inv.getVSAC_DESC();
Vector VSAC_CODE=other_inv.getVSAC_CODE();
Vector VTAX_STRUCTURE_CD=other_inv.getVTAX_STRUCTURE_CD();
Vector VTAX_STRUCTURE_DESC=other_inv.getVTAX_STRUCTURE_DESC();
Vector VMULTI_TAX_STRUCT=other_inv.getVMULTI_TAX_STRUCT();
Vector VLINE_NO=other_inv.getVLINE_NO();
Vector VLINE_NO1=other_inv.getVLINE_NO1();
Vector VGSIGN = other_inv.getVGSIGN();
Vector VGAMT_DES = other_inv.getVGAMT_DES();
Vector VGITEM_AMT = other_inv.getVGITEM_AMT();
Vector VLSIGN = other_inv.getVLSIGN();
Vector VLAMT_DES = other_inv.getVLAMT_DES();
Vector VLITEM_AMT = other_inv.getVLITEM_AMT();


supp_cd = other_inv.getSupp_cd();
vend_cd = other_inv.getVendor_cd();
String supp_address=other_inv.getSupplier_Address();
String supp_state=other_inv.getSupplier_State();
String supp_city=other_inv.getSupplier_City();
String supp_gstin_no=other_inv.getSupp_gstin_no();
String supp_pan_no=other_inv.getSupplier_Pan_No();
String vend_abbr=other_inv.getAbbr();
String supp_abbr=other_inv.getSupp_abbr();
String vend_address=other_inv.getVendor_Address();
String vend_state=other_inv.getVendor_State();
String vend_city=other_inv.getVendor_City();
String vend_gstin_no=other_inv.getVendor_GstTin_No();
String vend_pan_no=other_inv.getVendor_Pan_No();
String country=other_inv.getVendor_Country();
String pin=other_inv.getVendor_Pin_No();
String eff_dt=other_inv.getEff_dt();
String invoice_category = other_inv.getInvoice_category();
String invoice_raised_in = other_inv.getInvoice_raised_in();
		
if(invoice_category.equals(""))
{
	invoice_category="S";
}
if(invoice_raised_in.equals(""))
{
	invoice_raised_in="1";
}
boolean submission_chk = other_inv.getSubmission_chk();

if (type.equals("VIEW") && operation.equals("MODIFY"))  
{
	inv_no = other_inv.getInv_No();
}


String tax_amt = "";
String sale_amt = "";
String total_gst = "";
String gross_amount = "";
String net_amt = "";
String remark1 = "";
String remark2 = "";
String remark3 = "";
String fin_yr = "";
String inv_seq = "";
String tax_struct_dt="";
String tax_struct_cd="";
String tax_struct_info="";
String gross_revenue = ""; 
String gross_less = ""; 

if(operation.equals("MODIFY"))
{
	gross_revenue = other_inv.getGross_revenue();
	gross_less = other_inv.getGross_less();
	sac_cd =  other_inv.getSac_cd();
	inv_dt = other_inv.getInv_dt();
	due_dt = other_inv.getDue_dt();

	currency = other_inv.getCurrency();
	tax_structure = other_inv.getTax_name();
	tax_amt = other_inv.getTax_amt();
	total_gst = other_inv.getTotal_gst();
	desc_item = other_inv.getDesc_item();
	sale_amt = other_inv.getSale_amt();
	gross_amount = other_inv.getGross_amt();
	net_amt = other_inv.getNet_amt();
	fin_yr = other_inv.getFinanical_yr();
	inv_seq = other_inv.getInvoice_seq();
	tax_struct_dt = other_inv.getTax_struct_dt();
	tax_struct_cd = other_inv.getTax_struct_cd();
	tax_struct_info = other_inv.getTax_struct_info();
	remark1 = other_inv.getremark1();
	remark2 = other_inv.getremark2();
	remark3 = other_inv.getRemark3();
}
int len_multi_tax=0;

if(desc_item.equals(""))
{
	String date = "01/"+month+"/"+year;
	String previous_dt = utildate.getFirstDateOfPreviousMonth(date);
	String Mon = utildate.getShortMonthName(previous_dt);
	
	desc_item = "3% of net revenue share of AHPL for the month of "+Mon+" - "+year;
}
if(remark1.equals(""))
{
	remark1="Payment to be made in the name of \"Hazira Port Private Limited\" by wire transfer.";
}
if(remark2.equals(""))
{
	remark2="The Bank Details, for Wire Transfer through RTGS, are as follows:  \nHazira Port Private Limited   \nBank Name: CITIBANK N.A. \nAccount Number: 522615056 \nIFSC CODE: CITI0100000   \nBank Address: Bombay Mutual Building, 293, Dr. D N Road, Fort, Mumbai-400001" ;
}
%>
<body onload="<%if(!msg.equals("")){%>refreshParent('<%=accord%>','<%=msg%>','<%=msg_type%>');<%}%> " 
<%if(type.equals("VIEW") && operation.equals("MODIFY")) {%>style="pointer-events: none;"<%} %>>
<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_other_invoice">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
							AHPL Invoice (AHPL Revenue Share)
						</div>
					</div>
				</div>
				<div class="row m-b-5">
				<!-- Supplier details -->
					<div class="col-sm-6 col-xs-6 col-md-6">
						<div class="card-body cdbody">
							<div class="row m-b-5">
								<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Supplier Details</label>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>Supplier Name <span class="s-red">*</span></b></label>
						  			</div>
								</div>
								<div class="col-sm-10 col-xs-10 col-md-10">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
							      			<select class="form-select form-select-sm" name="supp_cd" onchange="refresh();" style="pointer-events:none; background-color: #e9ecef">
												<option value="0">--Select--</option>
											<%for(int i=0;i<VSUPPLIER_CD.size();i++){ %>
												<option value="<%=VSUPPLIER_CD.elementAt(i)%>"><%=VSUPPLIER_ABBR.elementAt(i)%> - <%=VSUPPLIER_NM.elementAt(i)%></option>
											<%} %>
											</select>
											<script>document.forms[0].supp_cd.value="<%=supp_cd%>"</script>
						    			</div>
						  			</div>
								</div>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>Supplier Address</b></label>
						  			</div>
								</div>
								<div class="col-sm-10 col-xs-10 col-md-10">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						      				<input type="text" class="form-control form-control-sm" name="supp_address" value="<%=supp_address%>" maxLength="40" readonly >
						    			</div>
						  			</div>
								</div>
							</div>
							<!--city & state  -->
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>City </b></label>
						  			</div>
								</div>
								<div class="col-sm-4 col-xs-4 col-md-4">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
											<input type="text" class="form-control form-control-sm" name="supp_city" value="<%=supp_city%>" maxLength="40" readonly>
						    			</div>
						  			</div>
								</div>
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>State </b></label>
						  			</div>
								</div>
								<div class="col-sm-4 col-xs-4 col-md-4">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
											<input type="text" class="form-control form-control-sm" name="supp_state" value="<%=supp_state%>" maxLength="40" readonly>
						    			</div>
						  			</div>
								</div>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>PAN No. </b></label>
						  			</div>
								</div>
								<div class="col-sm-4 col-xs-4 col-md-4">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						      				<input type="text" class="form-control form-control-sm" name="supp_pan_no" value="<%=supp_pan_no%>" maxLength="6" onkeyup="checkForNumber(this);" readonly>
						    			</div>
						  			</div>
								</div>
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>GSTIN No </b></label>
						  			</div>
								</div>
								<div class="col-sm-4 col-xs-4 col-md-4">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						      				<input type="text" class="form-control form-control-sm" name="supp_gstin_no" value="<%=supp_gstin_no%>" maxLength="6" onkeyup="checkForNumber(this);" readonly>
						    			</div>
						  			</div>
								</div>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>SAC <span class="s-red">*</span></b></label>
						  			</div>
								</div>
								<div class="col-sm-10 col-xs-10 col-md-10">  
									<div class="form-group row">
							    		<div class="col-sm-12 col-xs-12 col-md-12">
							    			<select class="form-select form-select-sm" name="sac_cd">
													<option value="0">--Select--</option>
														<%for(int i=0;i<VSAC_CD.size();i++){ %>
													<option value="<%=VSAC_CD.elementAt(i)%>"><%=VSAC_CODE.elementAt(i)%>-<%=VSAC_DESC.elementAt(i)%></option>
														<%} %>
											</select>
											<script>document.forms[0].sac_cd.value="<%=sac_cd%>"</script>
							    		</div>
							  		</div>
								</div>
							</div>
						</div>
					</div>
					<!--Vendor details  -->
					<div class="col-sm-6 col-xs-6 col-md-6">
						<div class="card-body cdbody">
							<div class="row m-b-5">
								<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Vendor Details</label>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>Vendor Name <span class="s-red">*</span></b></label>
						  			</div>
								</div>
								<div class="col-sm-10 col-xs-10 col-md-10">  
									<div class="form-group row">
					    				<div class="col-sm-12 col-xs-12 col-md-12">
					    			
							      			<select class="form-select form-select-sm" name="vend_cd" onchange="refresh();" style="pointer-events:none; background-color: #e9ecef">
												<option value="0">--Select--</option>
												<%for(int i=0;i<VENDOR_CD.size();i++){ %>
												<option value="<%=VENDOR_CD.elementAt(i)%>"><%=VENDOR_ABBR.elementAt(i)%> - <%=VENDOR_NM.elementAt(i)%></option>
													<%} %>
											</select>
										
											<script>document.forms[0].vend_cd.value="<%=vend_cd%>"</script>
					    				</div>
					  				</div>
								</div>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>Vendor Address</b></label>
						  			</div>
								</div>
								<div class="col-sm-10 col-xs-10 col-md-10">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
											<input type="text" class="form-control form-control-sm" name="vend_address" value="<%=vend_address%>" maxLength="40" readonly>
						    			</div>
						  			</div>
								</div>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>City</b></label>
						  			</div>
								</div>
								<div class="col-sm-4 col-xs-4 col-md-4">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
											<input type="text" class="form-control form-control-sm" name="vend_city" value="<%=vend_city%>" maxLength="40" readonly>
						    			</div>
						  			</div>
								</div>
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>State</b></label>
						  			</div>
								</div>
								<div class="col-sm-4 col-xs-4 col-md-4">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
											<input type="text" class="form-control form-control-sm" name="vend_state" value="<%=vend_state%>" maxLength="40" readonly>
						    			</div>
						  			</div>
								</div>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>PAN No.</b></label>
						  			</div>
								</div>
								<div class="col-sm-4 col-xs-4 col-md-4">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						      				<input type="text" class="form-control form-control-sm" name="vend_pan_no" value="<%=vend_pan_no%>" maxLength="6" onkeyup="checkForNumber(this);" readonly>
						    			</div>
						  			</div>
								</div>
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>GSTIN No</b></label>
						  			</div>
								</div>
								<div class="col-sm-4 col-xs-4 col-md-4">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						      				<input type="text" class="form-control form-control-sm" name="vend_gstin_no" value="<%=vend_gstin_no%>" maxLength="6" onkeyup="checkForNumber(this);" readonly>
						    			</div>
						  			</div>
								</div>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>PIN Code</b></label>
						  			</div>
								</div>
								<div class="col-sm-4 col-xs-4 col-md-4">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
											<input type="text" class="form-control form-control-sm" name="pin" value="<%=pin%>" maxLength="40" readonly>
						    			</div>
						  			</div>
								</div>
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>Country</b></label>
						  			</div>
								</div>
								<div class="col-sm-4 col-xs-4 col-md-4">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
											<input type="text" class="form-control form-control-sm" name="country" value="<%=country%>" maxLength="40" readonly>
						    			</div>
						  			</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- Invoice details -->
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
				    				<input type="radio" name="invoice_category" value="S" onchange="refresh();" <%if(invoice_category.equals("S")){ %>checked<%}%>>&nbsp;Service				
				    			</div>
				    		</div>
				    	</div>
					</div>
					<%if (type.equals("VIEW") && operation.equals("MODIFY")){  %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Invoice No. <span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="inv_no" value="<%=inv_no%>" maxLength="150" readonly>
				      				<input type="hidden" name="address_type" value="R">
				    			</div>
				  			</div>
						</div>
					</div>
					<%} %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Invoice Date <span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
		      				<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="inv_dt" value="<%=inv_dt%>" maxLength="10" onblur="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');" onchange="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');resetDueDate(this);" autocomplete="off">
	      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
      						</div>
				    	</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Invoice Due Date <span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
		      				<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="due_dt" value="<%=due_dt%>" maxLength="10" onblur="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');" onchange="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');checkInvoiceDueDate(document.forms[0].inv_dt,this,'T')" autocomplete="off">
	      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
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
				    				<select class="form-select form-select-sm" name="invoice_raised_in">
										<option value="0">--Select--</option> 
					  					<option value="1">INR</option>
									</select>
									<script>document.forms[0].invoice_raised_in.value="<%=invoice_raised_in%>"</script>
				    			</div>
				    		</div>
				    	</div>
				    </div>
				 <%--    <div id="Revenue_item">
				    	&nbsp;
					    <div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="table-responsive">
									<table class="table table-bordered table-hover">
										<tbody id="itemgross">
											<%for(int i=0;i<VLINE_NO.size(); i++){ %>
													<tr>
														<td align="center" width="550px"> 
															<%if(VLINE_NO.elementAt(i).equals("1") ) {%>
																<b style="color: #203d78;">Gross Revenue</b>
															<%} else {%>
																<input type="text" class="form-control form-control-sm" name="des" id="des<%=i%>" value="<%=VGAMT_DES.elementAt(i) %>" maxlength='30' style="width:500px;">
															<%} %>
														</td>
														<td align="right" width="300px">
															<div align="right" style='display:flex; alignItems:center; gap:5px;'>
																<%if(VLINE_NO.elementAt(i).equals("1") ) {%>
																	<input type="text" class="form-control form-control-sm" name="gross_revenue" value="<%=gross_revenue %>" maxlength='20' style="width:100px;text-align:right;" onchange="calcAmount('<%=operation%>','1','<%=VLINE_NO1.size()%>')">
																<%} else {%>
																	<select class="form-select form-select-sm" name="gross_sign" id="gross_sign<%=i%>" style="width: 50px;" onchange="calcAmount('<%=operation%>','<%=i%>','<%=VLINE_NO1.size()%>')">
								  										<option value="+">+</option>
								  										<option value="-">-</option>
																	</select>
																	<script>document.getElementById('gross_sign<%=i%>').value="<%=VGSIGN.elementAt(i)%>"</script>
																	
																	<input type="text" class="form-control form-control-sm" name="gross_amount" id="gross_amount<%=i%>" value="<%=VGITEM_AMT.elementAt(i) %>" maxlength='30' style="width:150px;text-align:right;" onchange="calcAmount('<%=operation%>','<%=i%>','<%=VLINE_NO1.size()%>')">
																	
																<%} %>
															</div>
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
										<tbody id="itemless">
											<%for(int i=0;i<VLINE_NO1.size(); i++){ %>
												<tr>
													<td align="center" width="550px">
													<%if(VLINE_NO1.elementAt(i).equals("1") ) {%>
															<b style="color: #203d78;">Less: Water Front Royalty (WFR)</b>
														<%} else {%>
															<input type="text" class="form-control form-control-sm" name="less_des" id="des<%=i%>" value="<%=VLAMT_DES.elementAt(i) %>" maxlength='30' style="width:500px;">
														<%} %>
													</td>
													<td align="right" width="300px">
														<div align="right" style='display:flex; alignItems:right; gap:5px;'>
															<%if(VLINE_NO1.elementAt(i).equals("1") ) {%>
																<input type="text" class="form-control form-control-sm" name="gross_less" value="<%=gross_less%>" maxlength='20' style="width:100px;text-align:right;" onchange="calcAmount('<%=operation%>','<%=VLINE_NO.size()%>','1')">
															<%} else {%>	
																<select class="form-select form-select-sm" name="less_sign" id="less_sign<%=i%>" style="width: 50px;" onchange="calcAmount('<%=operation%>','<%=VLINE_NO.size()%>','<%=i%>')">
							  										<option value="+">+</option>
							  										<option value="-">-</option>
																</select>
																<script>document.getElementById('less_sign<%=i%>').value="<%=VLSIGN.elementAt(i)%>"</script>
																
																<input type="text" class="form-control form-control-sm" name="less_amount" id="less_amount<%=i%>" value="<%=VLITEM_AMT.elementAt(i) %>" maxlength='30' style="width:150px;text-align:right;" onchange="calcAmount('<%=operation%>','<%=VLINE_NO.size()%>','<%=i%>')">
																
															<%} %>													
														</div>		
													</td>
													<td align="center">
														<a <%if(VLINE_NO1.size()>1){ %><%}else{ %>onclick="addrow1();"<%} %> id="minus1<%=i+1%>" <%if((i+1) == 1){ %>style="display:none"<%} %><%else if((VLINE_NO1.size() - 1) == i){ %><%}else{%>style="display:none"<%} %>>
															<i class="fa fa-minus-circle fa-2x"></i>
														</a>&nbsp;
														<a onclick="addrow1();" id="plus1<%=i+1%>" <%if((VLINE_NO1.size() - 1) == i){ %><%}else{%>style="display:none"<%} %>>
															<i class="fa fa-plus-circle fa-2x"></i>
														</a>
													</td>
												</tr>
											<%} %>
										</tbody>
									</table>
								</div>
							</div>
					    </div>
				    </div> --%>
				     <div id="lineItem">
				    	&nbsp;
					    <div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="table-responsive">
									<table class="table table-bordered table-hover">
										<thead>
											<tr>
												<th></th>
												<th>Line Item</th>
												<th>Unit</th>
												<th>Amount</th>
												<th>Add/Del Line</th>
											</tr>
										</thead>
										<tbody id="itemgross">
											<%for(int i=0;i<VLINE_NO.size(); i++){ %>
													<tr>
													<td></td>
														<td align="center" width="450px"> 
															<%if(VLINE_NO.elementAt(i).equals("1") ) {%>
																<input type="text" class="form-control form-control-sm" name="" maxlength="80" style="width:320px;font-weight: bold;" value="Gross Revenue" readOnly>
															<%} else {%>
																<input type="text" class="form-control form-control-sm" name="des" id="des<%=i%>" value="<%=VGAMT_DES.elementAt(i) %>" maxlength='30' style="width:320px;" autocomplete="off">
															<%} %>
														</td>
														<td align="center">
																<select class="form-select form-select-sm" style='width:100px;' name="currency" onchange="" >
																	<option value="1">INR</option>
																</select>
																<script>document.forms[0].currency.value="<%=currency%>"</script>
														</td>
														<td align="center">
															<%if(VLINE_NO.elementAt(i).equals("1") ) {%>
																<input type="text" class="form-control form-control-sm" name="gross_revenue" value="<%=gross_revenue %>" maxlength='20' style="width:150px;text-align:right;" onchange="calcAmount('<%=operation%>','1','<%=VLINE_NO1.size()%>')" autocomplete="off">
															<%} else {%>
															<div style="display:flex; justify-content:center; align-items:center; gap:6px; width:100%; margin:auto;">
																<select class="form-select form-select-sm" name="gross_sign" id="gross_sign<%=i%>" style="width: 45px;" onchange="calcAmount('<%=operation%>','<%=i%>','<%=VLINE_NO1.size()%>')">
							  										<option value="+">+</option>
							  										<option value="-">-</option>
																</select>
																
																<input type="text" class="form-control form-control-sm" name="gross_amount" id="gross_amount<%=i%>" value="<%=VGITEM_AMT.elementAt(i) %>" maxlength='30' style="width:100px;text-align:right;" onchange="calcAmount('<%=operation%>','<%=i%>','<%=VLINE_NO1.size()%>')">
																
																<script>document.getElementById('gross_sign<%=i%>').value="<%=VGSIGN.elementAt(i)%>"</script>
															</div>
															<%} %>	
														
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
										<tbody id="itemless">
											<%for(int i=0;i<VLINE_NO1.size(); i++){ %>
												<tr>
													<td></td>
													<td align="center" width="450px">
														<%if(VLINE_NO1.elementAt(i).equals("1") ) {%>
															<input type="text" class="form-control form-control-sm" name="" maxlength="80" style="width:320px;font-weight: bold;" value="Less: Water Front Royalty (WFR)" readOnly>
														<%} else {%>
															<input type="text" class="form-control form-control-sm" name="less_des" id="des<%=i%>" value="<%=VLAMT_DES.elementAt(i) %>" maxlength='30' style="width:320px;" autocomplete="off">
														<%} %>
													</td>
													<td align="center">
														<select class="form-select form-select-sm" style='width:100px;' name="currency" onchange="" >
															<option value="1">INR</option>
														</select>
														<script>document.forms[0].currency.value="<%=currency%>"</script>
													</td>
													<td  align="center" >
														<%if(VLINE_NO1.elementAt(i).equals("1") ) {%>
															<input type="text" class="form-control form-control-sm" name="gross_less" value="<%=gross_less%>" maxlength='20' style="width:150px;text-align:right;" onchange="calcAmount('<%=operation%>','<%=VLINE_NO.size()%>','1')" autocomplete="off">
														<%} else {%>
															<div style="display:flex; justify-content:center; align-items:center; gap:6px; width:100%; margin:auto;">
																<select class="form-select form-select-sm" name="less_sign" id="less_sign<%=i%>" style="width: 45px;" onchange="calcAmount('<%=operation%>','<%=VLINE_NO.size()%>','<%=i%>')">
									  								<option value="+">+</option>
									  								<option value="-">-</option>
																</select>
																
																<input type="text" class="form-control form-control-sm" name="less_amount" id="less_amount<%=i%>" value="<%=VLITEM_AMT.elementAt(i) %>" maxlength='30' style="width:100px;text-align:right;" onchange="calcAmount('<%=operation%>','<%=VLINE_NO.size()%>','<%=i%>')" autocomplete="off">
																
																<script>document.getElementById('less_sign<%=i%>').value="<%=VLSIGN.elementAt(i)%>"</script>
															</div>
														<%} %>
													</td>
													<td align="center">
														<a <%if(VLINE_NO1.size()>1){ %><%}else{ %>onclick="addrow1();"<%} %> id="minus1<%=i+1%>" <%if((i+1) == 1){ %>style="display:none"<%} %><%else if((VLINE_NO1.size() - 1) == i){ %><%}else{%>style="display:none"<%} %>>
															<i class="fa fa-minus-circle fa-2x"></i>
														</a>&nbsp;
														<a onclick="addrow1();" id="plus1<%=i+1%>" <%if((VLINE_NO1.size() - 1) == i){ %><%}else{%>style="display:none"<%} %>>
															<i class="fa fa-plus-circle fa-2x"></i>
														</a>
													</td>
												</tr>
											<%} %>
										</tbody>
										<tbody id="item">
											<tr>
												<td align="center">
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="" maxlength="80" style="width:320px;font-weight: bold;" value="Net Revenue excluding WFR" readOnly>
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;' name="currency" onchange="" >
														<option value="1">INR</option>
													</select>
													<script>document.forms[0].currency.value="<%=currency%>"</script>
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="amount" value="<%=sale_amt%>" maxlength='20' style="width:150px;text-align:right;" readOnly>
												</td>
												<td></td>
											</tr>
										</tbody>
										<tbody>
											<tr>
												<td align="center">
													<!-- <input type="checkbox" class="form-check-input"> -->
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="desc_item" value="<%=desc_item%>" maxlength="80" style="width:320px;"> 
													<%-- <textarea class="form-control form-control-sm" name="desc_item"   maxLength="80" cols="75" rows="1" style="width:320px;"><%=desc_item%></textarea> --%>
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;'>
														<option value="1">INR</option>
													</select>
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="total_amount" value="<%=gross_amount%>" maxlength='20' style="width:150px;text-align:right;" readOnly>
												</td>
												<td></td>
											</tr>
											<tr>
												<td align="center">
													<input type="button" class="btn btn-sm config_btn" value="Tax Config" onclick="openTaxStructMst('<%=supp_state %>','<%=vend_state %>');">
													<input type="hidden" name="tax_cd" value="<%=tax_struct_cd%>">
													<input type="hidden" name="tax_dt" value="<%=tax_struct_dt%>">
													<input type="hidden" name="temp_tax_cd" value="<%=tax_struct_cd%>">
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="tax_struct_info" maxlength="80" style="width:320px;font-weight: bold;" value="Tax (<%=tax_struct_info%>)" readOnly>	
													<input type="hidden" name="tax_struct_nm" value="<%=tax_struct_info%>">									
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;' name="tax_amt_unit">
														<!-- <option value="0">--Select--</option> -->
									  					<option value="1">INR</option>
														<!-- <option value="2">USD</option> -->
													</select>
													<script>document.forms[0].tax_amt_unit.value="<%=invoice_raised_in%>"</script>
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="tax_amt" value="<%=total_gst%>" maxlength='20' style="width:150px;text-align:right;" readOnly>
												</td>
												<td></td>
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
														</td>
														<td align="center">
															<input class="form-control form-control-sm" name="sub_tax_struct_info" cols="75" rows="1" 
															style="width:320px;font-weight: bold; text-align: right;" readOnly value="<%=((Vector) temp.elementAt(1)).elementAt(j)%>">								
														</td>
														<td align="center">
															<select class="form-select form-select-sm" style='width:100px;' name="sub_tax_amt_unit" id="sub_tax_amt_unit_<%=j%>">
											  					<option value="1">INR</option>
															</select>
															<script>document.getElementById("sub_tax_amt_unit_<%=j%>").value="<%=invoice_raised_in%>"</script>
														</td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="sub_tax_amt" value="<%=((Vector) temp.elementAt(2)).elementAt(j)%>" maxlength='20' style="width:150px;text-align:right;" readOnly>
														</td>
														<td></td>
													</tr>
													<%}
												}
											} %>
											<tr>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="" maxlength="80" style="width:320px;font-weight: bold;" value="Invoice Amount" readOnly>
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;' name="inv_amt_unit">
														<!-- <option value="0">--Select--</option> -->
									  					<option value="1">INR</option>
														<!-- <option value="2">USD</option> -->
													</select>
													<script>document.forms[0].inv_amt_unit.value="<%=invoice_raised_in%>"</script>
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="total_amt_inr" value="<%=net_amt%>" maxlength='20' style="width:150px;text-align:right;" readOnly>
												</td>
												<td></td>
											</tr>
										</tbody>	
									</table>
								</div>
							</div>
					    </div>
				    </div>&nbsp;
				    <div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Remark 1</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<font size="1" face="arial, helvetica, sans-serif">&nbsp;( You may enter up to 1000 characters. )&nbsp;
										<input readonly type=text name="remLen1" size="3" maxlength="3" class=""> characters left
									</font><br>
				      				<textarea class="form-control" name="remark1" cols="75" rows="2" onKeyDown="textCounter(this.form.remark1,this.form.remLen1,999);" onKeyUp="textCounter(this.form.remark1,this.form.remLen1,999);"><%=remark1%></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Remark 2</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<font size="1" face="arial, helvetica, sans-serif">&nbsp;( You may enter up to 1000 characters. )&nbsp;
										<input readonly type=text name="remLen2" size="3" maxlength="3" class=""> characters left
									</font><br>
				      				<textarea class="form-control" name="remark2" cols="75" rows="6" onKeyDown="textCounter(this.form.remark2,this.form.remLen2,999);" onKeyUp="textCounter(this.form.remark2,this.form.remLen2,999);"><%=remark2%></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Remark 3</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<font size="1" face="arial, helvetica, sans-serif">&nbsp;( You may enter up to 1000 characters. )&nbsp;
										<input readonly type=text name="remLen3" size="3" maxlength="3" class=""> characters left
									</font><br>
				      				<textarea class="form-control" name="remark3" cols="75" rows="3" onKeyDown="textCounter(this.form.remark3,this.form.remLen3,999);" onKeyUp="textCounter(this.form.remark3,this.form.remLen3,999);"><%=remark3%></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter d-flex justify-content-between align-items-center">
				 	 <div>
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();" <%if(type.equals("VIEW") && operation.equals("MODIFY")){ %> disabled<%} %>>
					 </div>	
					 <div>
						 <div class="d-flex justify-content-end">
							<div class="email-icon-wrapper">
						        <span class="fa-stack fa-lg" title="Print Temp PDF" onclick="printPDF('<%=owner_cd%>','<%=finYr%>','<%=inv_seq%>');" style="position: relative;pointer-events: auto;<%if(operation.equals("INSERT")){ %>display:none;<%} %>">
						            <i class="fa fa-file-pdf-o fa-stack-2x" style="position:absolute; left:-1.3em; top:-0.2em; color:red;"></i>
									<i class="fa fa-eye fa-stack-1x" style="position:absolute; left:-1.8em; top:0.2em; color:#000; padding:2px;"></i>
						        </span>
						    </div>
							&nbsp;
						<%if(write_access.equals("Y")){ %>
							<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();" <%if(type.equals("VIEW") && operation.equals("MODIFY")){ %> disabled<%} %>>
						<%}else{ %>
							<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
						 </div>
					 </div>
				</div>
			</div>			
		</div>
	</div>
</div>

<input type="hidden" name="option" value="AHPL_SHARE">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="operation" value="<%=operation%>">
<input type="hidden" name="temp_pan_no" value="<%=vend_pan_no%>">
<input type="hidden" name="inv_type" value="<%=inv_type%>">
<input type="hidden" name="month" value="<%=month%>">
<input type="hidden" name="year" value="<%=year%>">
<input type="hidden" name="period_start" value="<%=p_start_dt%>">
<input type="hidden" name="period_end" value="<%=p_end_dt%>">
<input type="hidden" name="financial_yr" value="<%=fin_yr%>">
<input type="hidden" name="inv_seq" value="<%=inv_seq%>">
<input type="hidden" name="vend_abbr" value="<%=vend_abbr%>">
<input type="hidden" name="supp_abbr" value="<%=supp_abbr%>">
<input type="hidden" name="item_size" id="item_size" value="<%=VLINE_NO.size()%>">
<input type="hidden" name="item_size1" id="item_size1" value="<%=VLINE_NO1.size()%>">
<input type="hidden" class="form-control form-control-sm" name="no_of_line" value="" maxlength="2">
<input type="hidden" class="form-control form-control-sm" name="no_of_line1" value="" maxlength="2">
<input type="hidden" name="total_gross" value="" >
<input type="hidden" name="total_less" value="">

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
<input type="hidden" name="accord" value="<%=accord%>">
<input type="hidden" name="u" value="<%=u%>">

</form>
</body>
</html>