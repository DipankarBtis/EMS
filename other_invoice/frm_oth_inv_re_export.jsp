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
	var invoice_category = document.forms[0].invoice_category.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var inv_type = document.forms[0].inv_type.value;
	var operation = document.forms[0].operation.value;
	var accord = document.forms[0].accord.value;
	var u = document.forms[0].u.value;
	
	var url = "frm_oth_inv_re_export.jsp?supp_cd="+supp_cd+"&vend_cd="+vend_cd+"&inv_dt="+inv_dt+"&invoice_category="+invoice_category+"&accord="+accord
				+"&month="+month+"&year="+year+"&inv_type="+inv_type+"&operation="+operation+"&u="+u;
		location.replace(url);
	
}

 
function doSubmit()
{
	var supp_cd = document.forms[0].supp_cd.value;
	var vend_cd = document.forms[0].vend_cd.value;
	var vend_gstin_no = document.forms[0].vend_gstin_no.value;
	var supp_gstin_no = document.forms[0].supp_gstin_no.value;
	var inv_dt = document.forms[0].inv_dt.value;
	
	var invoice_category = document.forms[0].invoice_category.value;
	var u = document.forms[0].u.value;
	var inv_raised = document.forms[0].invoice_raised_in.value;
	var gross_curr = document.forms[0].currency.value;
	var net_curr = document.forms[0].inv_amt_unit.value;
	
	var vend_ship_abbr = document.forms[0].vend_ship_abbr.value;
	var vend_bill_abbr = document.forms[0].vend_bill_abbr.value;
	
	
	var msg="";
	var flag=true;

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
	if(inv_dt=="0" || trim(inv_dt)=="")
	{
		msg+="Enter Invoice Date!\n";
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
	if(invoice_category==" " || trim(invoice_category)=="")
	{
		msg+="Please Select Invoice Category and Enter Necessary Information!\n";
		flag=false
	}
	
	if(vend_ship_abbr==" " || trim(vend_ship_abbr)=="")
	{
		msg+="Please Configure Correspondence Address for Selected Vendor!\n";
		flag=false
	}
	if(vend_bill_abbr==" " || trim(vend_bill_abbr)=="")
	{
		msg+="Please Configure Billing Address for Selected Vendor!\n";
		flag=false
	}
	
	
		var len=document.forms[0].qty.length;	
		if(len==undefined)
		{
			if(document.forms[0].qty.value=='' || document.forms[0].rate.value=='' || document.forms[0].desc_item.value=='' || document.forms[0].sac_code.value==''
				|| document.forms[0].uom.value==''	|| document.forms[0].dimensions.value=='' || document.forms[0].net_wt.value=='' || document.forms[0].gross_wt.value==''
				|| document.forms[0].pack_dtls.value=='')
			{
				if(document.forms[0].desc_item.value=='')
				{
					msg += "Please Enter Line Item Description\n";
		  	 	 	flag = false;
				}
				if(document.forms[0].rate.value=='')
				{
					msg += "Please Enter Rate\n";
		  	 	 	flag = false;
				}
				if(document.forms[0].qty.value=='')
				{
					msg += "Please Enter Quantity\n";
		  	 	 	flag = false;
				}
				if(document.forms[0].sac_code.value=='')
				{
					msg += "Please Enter SAC/HSN Code\n";
		  	 	 	flag = false;
				}
				if(document.forms[0].uom.value=='')
				{
					msg += "Please Enter UOM\n";
		  	 	 	flag = false;
				}
				if(document.forms[0].dimensions.value=='')
				{
					msg += "Please Enter Dimension\n";
		  	 	 	flag = false;
				}
				if(document.forms[0].net_wt.value=='')
				{
					msg += "Please Enter Net_Wt\n";
		  	 	 	flag = false;
				}
				if(document.forms[0].gross_wt.value=='')
				{
					msg += "Please Enter Gross_Wt\n";
		  	 	 	flag = false;
				}
				if(document.forms[0].pack_dtls.value=='')
				{
					msg += "Please Enter Packing Details\n";
		  	 	 	flag = false;
				}
				
			}
		}
		else 
		{
			var cnt=0,cnt1=0,cnt2=0,cnt3=0,cnt4=0,cnt5=0,cnt6=0,cnt7=0,cnt8=0;
			for(var i=0;i<len;i++)
			{
				if(document.forms[0].qty.value=='' || document.forms[0].rate.value=='' || document.forms[0].desc_item.value=='' || document.forms[0].sac_code.value==''
					|| document.forms[0].uom.value==''	|| document.forms[0].dimensions.value=='' || document.forms[0].net_wt.value=='' || document.forms[0].gross_wt.value==''
					|| document.forms[0].pack_dtls.value=='')
				{
					if(document.forms[0].qty[i].value=='')
	 	 	 		{
	 	 				cnt++;
	 	 	 	 		flag = false;
	 	 	 	 	}
	 	 			if(document.forms[0].rate[i].value=='')
	 	 	 		{
	 	 				cnt1++;
	 	 	 	 		flag = false;
	 	 	 	 	}
	 	 			
	 	 			if(document.forms[0].desc_item[i].value=='')
	 	 	 		{
	 	 				cnt2++;
	 	 	 	 		flag = false;
	 	 	 	 	}
	 	 			if(document.forms[0].sac_code[i].value=='')
	 				{
	 				
	 	 				cnt3++;
	 	 	 	 		flag = false;
	 				}
	 	 			if(document.forms[0].uom[i].value=='')
					{
	 	 				cnt4++;
	 	 	 	 		flag = false;
					}
					if(document.forms[0].dimensions[i].value=='')
					{
						cnt5++;
	 	 	 	 		flag = false;
					}
					if(document.forms[0].net_wt[i].value=='')
					{
						cnt6++;
	 	 	 	 		flag = false;
					}
					if(document.forms[0].gross_wt[i].value=='')
					{
						cnt7++;
	 	 	 	 		flag = false;
					}
					if(document.forms[0].pack_dtls[i].value=='')
					{
						cnt8++;
	 	 	 	 		flag = false;
					}
				}
			}
			
			if(cnt>0)
			{
				msg += "Quantity Cannot Be Blank For any line Item\n";
	 		}
			if(cnt1>0)
	 		{
	 			msg += "Rate Cannot Be Blank For any line Item\n";
	 		}
			if(cnt2>0)
	 		{
				msg += "Item Description Cannot Be Blank For any line Item\n";
	 		}
	 		if(cnt3>0)
	 		{
	 			msg += "SAC/HSN Code Cannot Be Blank For any line Item\n";
	 		}
	 		if(cnt4>0)
	 		{
	 			msg += "UOM Cannot Be Blank For any line Item\n";
	 		}
	 		if(cnt5>0)
	 		{
	 			msg += "Dimensions Cannot Be Blank For any line Item\n";
	 		}
	 		if(cnt6>0)
	 		{
	 			msg += "Net Wt Cannot Be Blank For any line Item\n";
	 		}
	 		if(cnt7>0)
	 		{
	 			msg += "Gross Wt Code Cannot Be Blank For any line Item\n";
	 		}
	 		if(cnt8>0)
	 		{
	 			msg += "Packing Details Cannot Be Blank For any line Item\n";
	 		}
	 		
			msg+="Please Enter Enter Necessary Information!\n";
		}
	
	if((inv_raised !== gross_curr) && (inv_raised !== net_curr))
	{
		msg+="Select Same Currecny as Invoice Raised For Gross amt And Invoice Amount \n";
		flag=false
	}
	
	if(flag)
	{
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

function addrow() 
{

    var max_seq = document.getElementById("item_size").value;
    var prev_minus = document.getElementById("minus" + max_seq);
    var prev_plus = document.getElementById("plus" + max_seq);
    var new_seq_no = parseInt(max_seq)+1;
    var inv_cat = document.forms[0].invoice_category.value
    var supp_state = document.forms[0].supp_state.value
    var vend_state = document.forms[0].vend_state.value
    
    if (new_seq_no > 99) 
    {
        alert("Maximum number of lines added for the Invoice!");
        return;
    }
    else 
    {
			    var tab = document.getElementById("itemTab");
			    var tr = document.createElement("tr");
			    tr.id = "row" + new_seq_no;
			
			    var td1 = document.createElement("td");
			    td1.align = "center";
			    var sac = document.createElement("input");
			    sac.type = "text";
			    sac.name = "sac_code";
			    sac.id = "sac_code"+new_seq_no;
			    sac.className = "form-control form-control-sm";
			    sac.maxLength = "20";
			    sac.style = "width:100px;text-align:right;";
			    td1.appendChild(sac);
			
			    var td2 = document.createElement("td");
			    td2.align = "center";
			    var desc = document.createElement("textarea");
			    desc.name = "desc_item";
			    desc.id = "desc_item"+new_seq_no;
			    desc.className = "form-control form-control-sm";
			    desc.maxLength = "125";
			    desc.cols = "75";
			    desc.rows = "3";
			    desc.style = "width:250px;";
			    td2.appendChild(desc);
			
			
			    var td3 = document.createElement("td");
			    td3.align = "center";
			    var uom = document.createElement("input");
			    uom.type = "text";
			    uom.name = "uom";
			    uom.id = "uom"+new_seq_no;
			    uom.placeholder = "UOM";
			    uom.className = "form-control form-control-sm";
			    uom.maxLength = "20";
			    uom.style = "width:100px;text-align:right;";
			    td3.appendChild(uom);
			    
			    var td4 = document.createElement("td");
			    td4.align = "center";
			    var dimensions = document.createElement("textarea");
			    dimensions.name = "dimensions";
			    dimensions.id = "dimensions"+new_seq_no;
			    dimensions.className = "form-control form-control-sm";
			    dimensions.maxLength = "100";
			    dimensions.cols = "75";
			    dimensions.rows = "3";
			    dimensions.style = "width:250px;";
			    td4.appendChild(dimensions);
			    
			    var td5 = document.createElement("td");
			    td5.align = "center";
			    var net_wt = document.createElement("input");
			    net_wt.type = "text";
			    net_wt.name = "net_wt";
			    net_wt.id = "net_wt"+new_seq_no;
			    net_wt.className = "form-control form-control-sm";
			    net_wt.maxLength = "80";
			    net_wt.style = "width:100px;text-align:right;";
			    td5.appendChild(net_wt);
			    
			    var td6 = document.createElement("td");
			    td6.align = "center";
			    var gross_wt = document.createElement("input");
			    gross_wt.type = "text";
			    gross_wt.name = "gross_wt";
			    gross_wt.id = "gross_wt"+new_seq_no;
			    gross_wt.className = "form-control form-control-sm";
			    gross_wt.maxLength = "80";
			    gross_wt.style = "width:100px;text-align:right;";
			    td6.appendChild(gross_wt);
			
			
			    var td7 = document.createElement("td");
			    td7.align = "center";
			    var qty = document.createElement("input");
			    qty.type = "text";
			    qty.name = "qty";
			    qty.id = "qty"+new_seq_no;
			    qty.className = "form-control form-control-sm";
			    qty.maxLength = "20";
			    qty.style = "width:100px;text-align:right;";
			    qty.setAttribute("onchange", "calcAmount('"+new_seq_no+"');");
			    td7.appendChild(qty);
			    
			    var td8 = document.createElement("td");
			    td8.align = "center";
			    var pack_dtls = document.createElement("input");
			    pack_dtls.type = "text";
			    pack_dtls.name = "pack_dtls";
			    pack_dtls.id = "pack_dtls"+new_seq_no;
			    pack_dtls.className = "form-control form-control-sm";
			    pack_dtls.maxLength = "80";
			    pack_dtls.style = "width:100px;text-align:left;";
			    td8.appendChild(pack_dtls);
			
			    var td9 = document.createElement("td");
			    td9.align = "center";
			    var rate = document.createElement("input");
			    rate.type = "text";
			    rate.name = "rate";
			    rate.id = "rate"+new_seq_no;
			    rate.className = "form-control form-control-sm";
			    rate.maxLength = "20";
			    rate.style = "width:100px;text-align:right;";
			    rate.setAttribute("onchange", "calcAmount('"+new_seq_no+"');");
			    td9.appendChild(rate);
			
			    var td11 = document.createElement("td");
			    td11.align = "center";
			    var amt = document.createElement("input");
			    amt.type = "text";
			    amt.name = "amount";
			    amt.id = "amount"+new_seq_no;
			    amt.className = "form-control form-control-sm";
			    amt.maxLength = "20";
			    amt.style = "width:100px;text-align:right;";
			    amt.readOnly = true;
			    td11.appendChild(amt);
			
			    var td12 = document.createElement("td");
			    td12.align = "center";
			
			    var minus = document.createElement("a");
			    minus.id = "minus" + new_seq_no;
			    minus.setAttribute("onclick", "removeRow('row" + new_seq_no + "','" + new_seq_no + "');");
			    var mIcon = document.createElement("i");
			    mIcon.className = "fa fa-minus-circle fa-2x";
			    minus.appendChild(mIcon);
			
			    var plus = document.createElement("a");
			    plus.id = "plus" + new_seq_no;
			    plus.setAttribute("onclick", "addrow();");
			    var pIcon = document.createElement("i");
			    pIcon.className = "fa fa-plus-circle fa-2x";
			    plus.appendChild(pIcon);
			
			    td12.appendChild(minus);
			    td12.appendChild(document.createTextNode(" "));
			    td12.appendChild(plus);
			
			    tr.appendChild(td1);
			    tr.appendChild(td2);
			    tr.appendChild(td3);
			    tr.appendChild(td4);
			    tr.appendChild(td5);
			    tr.appendChild(td6);
			    tr.appendChild(td7);
			    tr.appendChild(td8);
			    tr.appendChild(td9);
			    tr.appendChild(td11);
			    tr.appendChild(td12);
			
			    tab.appendChild(tr);
    	
	    document.getElementById("item_size").value = new_seq_no;
	    document.forms[0].no_of_line.value = new_seq_no;
	
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
		var total_amt = document.forms[0].total_amount.value;
		var type = document.forms[0].invoice_category.value;
		
			if(total_amt!='' && total_amt!=null && total_amt!=undefined)
			{
				var amt = document.getElementById("amount"+seq_no).value;
				total_amt -= amt;
				document.forms[0].total_amount.value = parseFloat(Math.round(total_amt)).toFixed(2);
				document.forms[0].total_amt_inr.value = parseFloat(Math.round(total_amt)).toFixed(2);
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

function calcAmount(index) 
{
    var amount = 0;
	
	var qty  = parseFloat(document.getElementById("qty" + index)?.value)  || 0;
    var rate = parseFloat(document.getElementById("rate" + index)?.value) || 0;

    if(qty!='' && qty!=null && qty!=undefined)
    {
	    var amount = qty * rate;
	    document.getElementById("amount" + index).value = parseFloat(Math.round(amount)).toFixed(2);
	
	    var total = 0;
	    var totalRows = parseInt(document.getElementById("item_size").value) || 1;
	
	    for (var i = 1; i <= totalRows; i++) 
	    {
	        if (!document.getElementById("amount" + i)) continue;
	
	        var rowAmt = parseFloat(document.getElementById("amount" + i).value) || 0;
	        total += rowAmt;
	    }
	    document.forms[0].total_amount.value = parseFloat(Math.round(total)).toFixed(2);
	    document.forms[0].total_amt_inr.value = parseFloat(Math.round(total)).toFixed(2);
    }
}


function calcAmount1(index) 
{
    var amount = 0;
	
	var qty  = parseFloat(document.getElementById("qty" + index)?.value)  || 0;
    var rate = parseFloat(document.getElementById("rate" + index)?.value) || 0;

    if(qty!='' && qty!=null && qty!=undefined)
    {
	    var amount = qty * rate;
	    document.getElementById("amount" + index).value = parseFloat(Math.round(amount)).toFixed(2);
	
	    var total = 0;
	    var totalRows = parseInt(document.getElementById("item_size").value) || 1;
	
	    for (var i = 1; i <= totalRows; i++) 
	    {
	        if (!document.getElementById("amount" + i)) continue;
	        var rowAmt = parseFloat(document.getElementById("amount" + i).value) || 0;
	        total += rowAmt;
	        
	        if (!document.getElementById("cess_per" + i)) continue;
	        var cessper = parseFloat(document.getElementById("cess_per" + i).value) || 0;
	        document.getElementById("cess_amt" + i).value = parseFloat(Math.round((cessper * rowAmt) / 100)).toFixed(2);
	    }
	    document.forms[0].total_amount.value = parseFloat(Math.round(total)).toFixed(2);
	    document.forms[0].total_amt_inr.value = parseFloat(Math.round(total)).toFixed(2);
	   
    }
    
}

function printPDF(comp_cd,fin_year,inv_seq)
{
	var supp_cd = document.forms[0].supp_cd.value;
	var is_print="0";
	var inv_type="RXP";
	
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

//Function for autoselect of Gross and Invoice Currency
document.addEventListener("DOMContentLoaded", function () {
    const invoiceCurrency = document.getElementById("invoice_raised_in");
    const grossCurrency = document.forms[0].currency;
    const invoiceAmtCurrency = document.forms[0].inv_amt_unit;

    function syncCurrency() {
        const selectedValue = invoiceCurrency.value;
        grossCurrency.value = selectedValue;
        invoiceAmtCurrency.value = selectedValue;
    }
    syncCurrency();
    invoiceCurrency.addEventListener("change", syncCurrency);
});
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
String operation=request.getParameter("operation")==null?"INSERT":request.getParameter("operation");
String inv_type=request.getParameter("inv_type")==null?"":request.getParameter("inv_type");
String inv_dt = request.getParameter("inv_dt")==null?"":request.getParameter("inv_dt");
String currency = request.getParameter("currency")==null?"1":request.getParameter("currency");
String desc_item = request.getParameter("desc_item")==null?"":request.getParameter("desc_item");
String amount = request.getParameter("amount")==null?"":request.getParameter("amount");
String accord = request.getParameter("accord")==null?"":request.getParameter("accord");
String type = request.getParameter("type")==null?"":request.getParameter("type");
String invoice_category = request.getParameter("invoice_category")==null?"":request.getParameter("invoice_category");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String inv_no = request.getParameter("inv_no")==null?"":request.getParameter("inv_no");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String p_start_dt = utildate.getFirstDateOfMonth(month, year);
String p_end_dt = utildate.getLastDateOfMonth(month, year);
String finYr = request.getParameter("fin_year")==null?"":request.getParameter("fin_year");

other_inv.setCallFlag("OTH_INV_RE_EXPORT");
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
Vector VLINE_NO=other_inv.getVLINE_NO();
Vector VGITEM_AMT=other_inv.getVGITEM_AMT();
Vector VPRICE=other_inv.getVPRICE();
Vector VQTY=other_inv.getVQTY();
Vector VUOM_NO=other_inv.getVUOM_NO();
Vector VGAMT_DES=other_inv.getVGAMT_DES();
Vector VSAC_CODE=other_inv.getVSAC_CODE();

Vector VDIMENSION=other_inv.getVDIMENSION();
Vector VNET_WT=other_inv.getVNET_WT();
Vector VGROSS_WT=other_inv.getVGROSS_WT();
Vector VPACK_DTLS=other_inv.getVPACK_DTLS();

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

String reference_no=other_inv.getReference_no();
String per_carriage=other_inv.getPer_carriage();
String final_destination=other_inv.getFinal_destination();
String country_of_origin=other_inv.getCountry_of_origin();
String port_of_discharge=other_inv.getPort_of_discharge();
String port_of_loading=other_inv.getPort_of_loading();
String pay_terms=other_inv.getPay_terms();
//System.out.println("frontend_per_carriage: "+per_carriage);


String vend_ship_abbr=other_inv.getVend_Ship_Abbr();
String vend_ship_city=other_inv.getVend_Ship_City();
String vend_ship_pin_no=other_inv.getVend_Ship_Pin_No();
String vend_ship_country=other_inv.getVend_Ship_Country();
String vend_ship_state=other_inv.getVend_Ship_State();

String vend_bill_abbr=other_inv.getVend_Bill_Abbr();
String vend_bill_city=other_inv.getVend_Bill_City();
String vend_bill_pin_no=other_inv.getVend_Bill_Pin_No();
String vend_bill_country=other_inv.getVend_Bill_Country();
String vend_bill_state=other_inv.getVend_Bill_State();


String invoice_raised_in = other_inv.getInvoice_raised_in();
if(invoice_raised_in.equals(""))
{
	invoice_raised_in="1";
}
boolean submission_chk = other_inv.getSubmission_chk();

if (type.equals("VIEW") && operation.equals("MODIFY"))  
{
	inv_no = other_inv.getInv_No();
}
String sale_amt = "";
String total_gst = "";
String gross_amount = "";
String net_amt = "";
String remark1 = "";
String remark2 = "";
String fin_yr = "";
String inv_seq = "";


if(operation.equals("MODIFY"))
{
	invoice_category = other_inv.getInvoice_category();
	supp_cd = other_inv.getSupp_cd();
	vend_cd = other_inv.getVendor_cd();
	inv_dt = other_inv.getInv_dt();
	currency = other_inv.getCurrency();
	total_gst = other_inv.getTotal_gst();
	desc_item = other_inv.getDesc_item();
	sale_amt = other_inv.getSale_amt();
	gross_amount = other_inv.getGross_amt();
	net_amt = other_inv.getNet_amt();
	remark1 = other_inv.getremark1();
	remark2 = other_inv.getremark2();
	fin_yr = other_inv.getFinanical_yr();
	inv_seq = other_inv.getInvoice_seq();
	remark1 = other_inv.getremark1();
}
if(remark1.equals(""))
{
	remark1="I.E.Code No.: 0801003075  \nDeclaration: \nWe declare that this Invoice shows the actual price of the goods described and that all \nparticulars are true and correct";
}

%>
<body onload="<%if(!msg.equals("")){%>refreshParent('<%=accord%>','<%=msg%>','<%=msg_type%>');<%}%> " 
<%if(type.equals("VIEW") && operation.equals("MODIFY")) {%>style="pointer-events: none;"<%} %> >
<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_other_invoice">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
							ReExport Invoice
						</div>
					</div>
				</div>
				<div class="row m-b-5">
				<!-- Supplier details -->
					<div class="col-sm-6 col-xs-6 col-md-6">
						<div class="card-body cdbody">
							<div class="row m-b-5">
								<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Exporter Details</label>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>Exporter Name <span class="s-red">*</span></b></label>
						  			</div>
								</div>
								<div class="col-sm-10 col-xs-10 col-md-10">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
							      			<select class="form-select form-select-sm" name="supp_cd" <%if(operation.equals("MODIFY")) {%>style="pointer-events:none; background-color: #e9ecef"<%} else {%>onchange="refresh();"<%} %>>
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
						    			<label class="form-label"><b>Exporter Address</b></label>
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
								<div></div>
								<div></div>
								<div></div>
								<div></div>
								<div></div>
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
					    			
							      			<select class="form-select form-select-sm" name="vend_cd" <%if(operation.equals("MODIFY")) {%>style="pointer-events:none; background-color: #e9ecef"<%} else {%>onchange="refresh();"<%} %>>
												<option value="0">--Select--</option>
												<%for(int i=0;i<VENDOR_CD.size();i++){ %>
												<option value="<%=VENDOR_CD.elementAt(i)%>"><%=VENDOR_ABBR.elementAt(i)%> - <%=VENDOR_NM.elementAt(i)%></option>
													<%} %>
											</select>
										
											<script>document.forms[0].vend_cd.value="<%=vend_cd%>"</script><!-- // here we pass vend_cd string -->
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
				<!-- Vendor Shipping Address -->
				<div class="row m-b-5">
				<!-- Supplier details -->
					<div class="col-sm-6 col-xs-6 col-md-6">
						<div class="card-body cdbody">
							<div class="row m-b-5">
								<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Vendor Shipping Address</label>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b> Address</b></label>
						  			</div>
								</div>
								<div class="col-sm-10 col-xs-10 col-md-10">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						      				<input type="text" class="form-control form-control-sm" name="vend_ship_abbr" value="<%=vend_ship_abbr%>" maxLength="40" readonly >
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
											<input type="text" class="form-control form-control-sm" name="vend_ship_city" value="<%=vend_ship_city%>" maxLength="40" readonly>
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
											<input type="text" class="form-control form-control-sm" name="vend_ship_state" value="<%=vend_ship_state%>" maxLength="40" readonly>
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
											<input type="text" class="form-control form-control-sm" name="vend_ship_pin_no" value="<%=vend_ship_pin_no%>" maxLength="40" readonly>
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
											<input type="text" class="form-control form-control-sm" name="vend_ship_country" value="<%=vend_ship_country%>" maxLength="40" readonly>
						    			</div>
						  			</div>
								</div>
							</div>
						</div>
					</div>
					<!--Vendor Billing Address -->
					<div class="col-sm-6 col-xs-6 col-md-6">
						<div class="card-body cdbody">
							<div class="row m-b-5">
								<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Vendor Billing Address</label>
							</div>
							<div class="row m-b-5">
								<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b> Address</b></label>
						  			</div>
								</div>
								<div class="col-sm-10 col-xs-10 col-md-10">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
											<input type="text" class="form-control form-control-sm" name="vend_bill_abbr" value="<%=vend_bill_abbr%>" maxLength="40" readonly>
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
											<input type="text" class="form-control form-control-sm" name="vend_bill_city" value="<%=vend_bill_city%>" maxLength="40" readonly>
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
											<input type="text" class="form-control form-control-sm" name="vend_bill_state" value="<%=vend_bill_state%>" maxLength="40" readonly>
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
											<input type="text" class="form-control form-control-sm" name="vend_bill_pin_no" value="<%=vend_bill_pin_no%>" maxLength="40" readonly>
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
											<input type="text" class="form-control form-control-sm" name="vend_bill_country" value="<%=vend_bill_country%>" maxLength="40" readonly>
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
				    				<%-- <input type="radio" name="invoice_category" value="P" onchange="refresh();" <%if(invoice_category.equals("P")){ %>checked<%}%>>&nbsp;Product --%>
				    				&nbsp;&nbsp;
				    				<input type="radio" name="invoice_category" value="S" onchange="refresh();" checked>&nbsp;Service				
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Port Of Discharge </b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="port_of_discharge" value="<%=port_of_discharge%>" maxLength="80">
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
	      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="inv_dt" value="<%=inv_dt%>" maxLength="10" onblur="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');" onchange="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');" autocomplete="off">
	      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
      						</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Port Of Loading </b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="port_of_loading" value="<%=port_of_loading%>" maxLength="80">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Per-carriage By </b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="per_carriage" value="<%=per_carriage%>" maxLength="80">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Final Destination </b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="final_destination" value="<%=final_destination %>" maxLength="80">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Vessel/Flight No </b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="reference_no" value="<%=reference_no %>" maxLength="80">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Country Of Origin </b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="country_of_origin" value="<%=country_of_origin%>" maxLength="80">
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
				    				<select class="form-select form-select-sm" name="invoice_raised_in" id="invoice_raised_in">
										<option value="0">--Select--</option> 
					  					<option value="1">INR</option>
										<option value="2">USD</option>
										<option value="3">GBP</option>
										<option value="4">EURO</option>
										<option value="5">YEN</option>
									</select>
									<script>document.forms[0].invoice_raised_in.value="<%=invoice_raised_in%>"</script>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Terms Of Delivery And Payment </b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
						    <div class="form-group row">
						        <div class="col-sm-12 col-xs-12 col-md-12">
						            <textarea class="form-control form-control-sm" name="pay_terms"  maxlength="100" rows="2"><%=pay_terms%></textarea>
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
												<th>HSN/SAC Code</th>
												<th>Description Of Goods</th>
												<th>UOM</th>
												<th>Dimensions</th>
												<th>Net Wt.</th>
												<th>Gross Wt.</th>
												<th>Quantity</th>
												<th>Packing Details</th>
												<th>Rate</th>
												<th>Amount</th>
												<th>Add/Del line</th>
											</tr>
										</thead>
										<tbody id="itemTab">
											<%for(int i=0;i<VLINE_NO.size(); i++){ %>
												<tr>
													<td align="center">
														<input type="text" class="form-control form-control-sm" name="sac_code" value="<%=VSAC_CODE.elementAt(i)%>" maxlength='20' style="width:100px;text-align:right;">
													</td>
													<td align="center">
														<textarea class="form-control form-control-sm" name="desc_item" maxLength="125" cols="75" rows="3" style="width:250px;"><%=VGAMT_DES.elementAt(i)%></textarea>
													</td>
													<td align="center">
														<input type="text" class="form-control form-control-sm" name="uom" value="<%=VUOM_NO.elementAt(i)%>" placeholder="UOM" maxlength='20' style="width:100px;text-align:right;">
													</td>
													<td align="center">
														<textarea class="form-control form-control-sm" name="dimensions" maxLength="100" cols="75" rows="3" style="width:250px;"><%=VDIMENSION.elementAt(i) %></textarea>
													</td>
													<td align="center">
														<input type="text" class="form-control form-control-sm" name="net_wt" value="<%=VNET_WT.elementAt(i) %>"  maxlength='80' style="width:100px;text-align:right;">
													</td>
													<td align="center">
														<input type="text" class="form-control form-control-sm" name="gross_wt" value="<%=VGROSS_WT.elementAt(i) %>"  maxlength='80' style="width:100px;text-align:right;">
													</td>
													<td align="center">
														<input type="text" class="form-control form-control-sm" name="qty" value="<%=VQTY.elementAt(i)%>" id="qty<%=i+1%>" maxlength='20' style="width:100px;text-align:right;" onchange="calcAmount('<%=VLINE_NO.elementAt(i)%>');">
													</td>
													<td align="center">
														<input type="text" class="form-control form-control-sm" name="pack_dtls" value="<%=VPACK_DTLS.elementAt(i) %>"  maxlength='80' style="width:100px;text-align:left;">
													</td>
													<td align="center">
														<input type="text" class="form-control form-control-sm" name="rate" value="<%=VPRICE.elementAt(i)%>" id="rate<%=i+1%>" maxlength='20' style="width:100px;text-align:right;" onchange="calcAmount('<%=VLINE_NO.elementAt(i)%>');">
													</td>
													<td align="center">
														<input type="text" class="form-control form-control-sm" name="amount" id="amount<%=i+1%>" value="<%=VGITEM_AMT.elementAt(i)%>" maxlength='20' style="width:100px;text-align:right;" readOnly>
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
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="" maxlength="80" style="width:250px;font-weight: bold;" value="Gross Amount" readOnly>
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;' name="currency" disabled>
														<option value="1">INR</option>
														<option value="2">USD</option>
														<option value="3">GBP</option>
														<option value="4">EURO</option>
														<option value="5">YEN</option>
													</select>
													<script>document.forms[0].currency.value="<%=invoice_raised_in%>"</script>
												</td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="total_amount" value="<%=gross_amount%>" maxlength='20' style="width:100px;text-align:right;" readOnly>
												</td>
												<td></td>
											</tr>
											<tr>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="" maxlength="80" style="width:250px;font-weight: bold;" value="Invoice Amount" readOnly>
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;' name="inv_amt_unit" disabled>
														<!-- <option value="0">--Select--</option> -->
									  					<option value="1">INR</option>
														<option value="2">USD</option>
														<option value="3">GBP</option>
														<option value="4">EURO</option>
														<option value="5">YEN</option>
														<!-- <option value="2">USD</option> -->
													</select>
													<script>document.forms[0].inv_amt_unit.value="<%=invoice_raised_in%>"</script>
												</td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="total_amt_inr" value="<%=net_amt%>" maxlength='20' style="width:100px;text-align:right;" readOnly>
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
				      				<textarea class="form-control" name="remark1" cols="75" rows="6" onKeyDown="textCounter(this.form.remark1,this.form.remLen1,999);" onKeyUp="textCounter(this.form.remark1,this.form.remLen1,999);"> <%=remark1%></textarea>
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

<input type="hidden" name="option" value="RE_EXPORT_INV">
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
<input type="hidden" class="form-control form-control-sm" name="no_of_line" value="" maxlength="2">

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