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
	var cargo_type=document.forms[0].cargo_type.value;
	var user_dt=document.forms[0].user_dt.value;
	var due_dt=document.forms[0].due_dt.value;
	var exchang_cd=document.forms[0].exchng_nm.value;
	var desc_item = document.forms[0].desc_item.value;
	var amount = document.forms[0].amount.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var inv_type = document.forms[0].inv_type.value;
	var operation = document.forms[0].operation.value;
	var accord = document.forms[0].accord.value;
	var u = document.forms[0].u.value;

	
	var url = "frm_oth_inv_hppl_seipl.jsp?supp_cd="+supp_cd+"&vend_cd="+vend_cd+"&inv_dt="+inv_dt+"&currency="+curr
			+"&desc_item="+desc_item+"&amount="+amount+"&sac_cd="+sac_cd+"&cargo_type="+cargo_type+"&user_dt="+user_dt+
			"&due_dt="+due_dt+"&exchng_nm="+exchang_cd+"&accord="+accord+"&month="+month+"&year="+year+"&inv_type="+inv_type+"&operation="+operation+"&u="+u;

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
	var vend_gstin_no = document.forms[0].vend_gstin_no.value;
	var supp_gstin_no = document.forms[0].supp_gstin_no.value;
	var sac_cd = document.forms[0].sac_cd.value;
	var inv_dt = document.forms[0].inv_dt.value;
	var exchg_val = document.forms[0].exchng_rate.value;
	var exchng_eff_dt = document.forms[0].exchng_eff_dt.value;
	var tax_struct = document.forms[0].tax_struct_nm.value;
	var desc_item = document.forms[0].desc_item.value;
	var amount = document.forms[0].amount.value;
	var u = document.forms[0].u.value;
	
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
	if(amount=="" || amount=="0" || trim(amount)=="")
	{
		msg+="Enter Line Item Amount!\n";
		flag=false
	}
	if(exchg_val=="" || exchg_val=="0" || trim(exchg_val)=="")
	{
		msg+="Exchange Rate value Should not be 0!\n";
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

function calcAmount(operation,index) 
{
	var length=document.forms[0].cargo_rate.length;
	var amount = 0;
	//var exchg_val = Math.round(document.forms[0].exchng_rate.value);
	var exchg_val = document.forms[0].exchng_rate.value;
    var total_amount = document.forms[0].total_amount.value;
    var exchang_cd=document.forms[0].exchng_nm.value;
    
	if(length==undefined)
	{
		var port=document.forms[0].dis_port.value;
		var mmbtu = document.getElementById('qty_mmbtu0').innerHTML;
		var cargo_rate=document.forms[0].cargo_rate.value;
		var cargo_amt=document.forms[0].cargo_amt.value;
		if(exchang_cd == "0")
	    {
	 		alert("Please Select Exchange Rate First!");
	 		document.forms[0].exchng_nm.value="0";
	    }
		
		if(port=='1')
		{
			document.forms[0].cargo_rate.readOnly=false;
			if(cargo_rate == "")
			{
				document.forms[0].cargo_rate.value="0.0000";
				document.forms[0].cargo_amt.value="0.00";
			}
			else 
			{
				cargo_amt = cargo_rate*mmbtu;
				document.forms[0].cargo_amt.value = parseFloat(Math.round(cargo_amt)).toFixed(2);
				amount += round(parseFloat(cargo_amt),0);
			}
		}
		else if(port=='2')
		{
			document.forms[0].cargo_rate.readOnly=false;
			if(cargo_rate == "")
			{
				document.forms[0].cargo_rate.value="0.0000";
				document.forms[0].cargo_amt.value="0.00";
			}
			else 
			{
				cargo_amt = cargo_rate*mmbtu;
				document.forms[0].cargo_amt.value = parseFloat(Math.round(cargo_amt)).toFixed(2);
				amount += round(parseFloat(cargo_amt),0);
			}
		}
		else 
		{
			document.forms[0].cargo_rate.readOnly=true;
			document.forms[0].cargo_amt.readOnly=true;
			document.forms[0].cargo_rate.value="0.0000";
			document.forms[0].cargo_amt.value="0.00";
		}
		document.forms[0].amount.value = parseFloat(Math.round(amount)).toFixed(2);
		document.forms[0].total_amount.value = parseFloat(Math.round(exchg_val*amount)).toFixed(2);
	}
	else 
	{
		if(exchang_cd == "0")
	    {
	 		alert("Please Select Exchange Rate First!");
	 		document.forms[0].dis_port[index].value="0";
	    }
		for(var i=0;i<length;i++)
		{
			var port=document.forms[0].dis_port[i].value;
			var mmbtu = document.getElementById('qty_mmbtu' + i).innerHTML;
			var cargo_rate=document.forms[0].cargo_rate[i].value;
			var cargo_amt=document.forms[0].cargo_amt[i].value;
			if(port=='1')
			{
				document.forms[0].cargo_rate[index].readOnly=false;
				document.forms[0].cargo_amt[index].readOnly=true;
				
					if(cargo_rate == "")
					{
						document.forms[0].cargo_rate[index].value="0.0000";
						document.forms[0].cargo_amt[index].value="0.00";
					}
					else 
					{
						cargo_amt = cargo_rate*mmbtu;
						document.forms[0].cargo_amt[index].value = parseFloat(Math.round(cargo_amt)).toFixed(2);
						amount += round(parseFloat(cargo_amt),0);
					}
			}
			else if(port=='2')
			{
				document.forms[0].cargo_rate[index].readOnly=false;
				document.forms[0].cargo_amt[index].readOnly=true;
				
					if(cargo_rate == "")
					{
						document.forms[0].cargo_rate[index].value="0.0000";
						document.forms[0].cargo_amt[index].value="0.00";
					}
					else 
					{
						cargo_amt = cargo_rate*mmbtu;
						document.forms[0].cargo_amt[index].value = parseFloat(Math.round(cargo_amt)).toFixed(2);
						amount += round(parseFloat(cargo_amt),0);
					}
			}
			else 
			{
				document.forms[0].cargo_rate[i].readOnly=true;
				document.forms[0].cargo_amt[i].readOnly=true;
				document.forms[0].cargo_rate[i].value="0.0000";
				document.forms[0].cargo_amt[i].value="0.00";
			}
		}
		document.forms[0].amount.value = parseFloat(Math.round(amount)).toFixed(2);
		
		document.forms[0].total_amount.value = parseFloat(Math.round(exchg_val*amount)).toFixed(2);
	}
	var tot_amt=Number(document.forms[0].tax_amt.value)  + Number(document.forms[0].total_amount.value);
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
	document.forms[0].total_amt_inr.value = parseFloat(Math.round(tot_amt)).toFixed(2);
	
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

function enableRate(dis_port,index)
{
	if(dis_port!="" && dis_port!="0")
	{
		document.getElementById('cargo_rate'+index).readOnly=false;
	}
	else
	{
		document.getElementById('cargo_rate'+index).readOnly=true;
	}
}
function printPDF(comp_cd,fin_year,inv_seq)
{
	var supp_cd = document.forms[0].supp_cd.value;
	var is_print="0";
	var inv_type="HS";
	
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
String currency = request.getParameter("currency")==null?"2":request.getParameter("currency");
String exchng_rate = request.getParameter("exchng_rate")==null?"":request.getParameter("exchng_rate");
String exchng_eff_dt = request.getParameter("exchng_eff_dt")==null?"":request.getParameter("exchng_eff_dt");
String tax_structure=request.getParameter("tax_structure")==null?"0":request.getParameter("tax_structure");
String desc_item = request.getParameter("desc_item")==null?"":request.getParameter("desc_item");
String amount = request.getParameter("amount")==null?"":request.getParameter("amount");
String accord = request.getParameter("accord")==null?"":request.getParameter("accord");
String type = request.getParameter("type")==null?"":request.getParameter("type");
String cargo_type = request.getParameter("cargo_type")==null?"0":request.getParameter("cargo_type");
String exchng_nm = request.getParameter("exchng_nm")==null?"0":request.getParameter("exchng_nm");
String user_dt = request.getParameter("user_dt")==null?sysdate:request.getParameter("user_dt");
String dis_port = request.getParameter("dis_port")==null?"0":request.getParameter("dis_port");

String exchang_val = request.getParameter("exchang_val")==null?"":request.getParameter("exchang_val");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String inv_no = request.getParameter("inv_no")==null?"":request.getParameter("inv_no");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String p_start_dt = utildate.getFirstDateOfMonth(month, year);
String p_end_dt = utildate.getLastDateOfMonth(month, year);
String finYr = request.getParameter("fin_year")==null?"":request.getParameter("fin_year");

other_inv.setCallFlag("OTH_INV_HPPL_SEIPL");
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
other_inv.setUser_dt(user_dt);
other_inv.setCargo_type(cargo_type);
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
Vector VEXCHNG_RATE_CD = other_inv.getVEXCHNG_RATE_CD();
Vector VEXCHNG_RATE_NM = other_inv.getVEXCHNG_RATE_NM();
Vector VEXCHNG_RATE = other_inv.getVEXCHNG_RATE();

Vector VCARGO_REF = other_inv.getVCARGO_REF();
Vector VCARGO_DT = other_inv.getVCARGO_DT();
Vector VSHIP_CD = other_inv.getVSHIP_CD();
Vector VQTY_MMBTU = other_inv.getVQTY_MMBTU();
Vector VCARGO_TYPE = other_inv.getVCARGO_TYPE();
Vector VSHIP_NAME = other_inv.getVSHIP_NAME();
Vector VDIS_PORT = other_inv.getVDIS_PORT();
Vector VRATE = other_inv.getVRATE();
Vector VCARGO_AMT = other_inv.getVCARGO_AMT();

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

int index = VEXCHNG_RATE_CD.indexOf(exchng_nm);

if (index != -1) 
{
	exchang_val = ""+VEXCHNG_RATE.elementAt(index);
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

if(operation.equals("MODIFY"))
{
	user_dt = other_inv.getUser_dt();
	cargo_type = other_inv.getCargo_type();
	sac_cd =  other_inv.getSac_cd();
	inv_dt = other_inv.getInv_dt();
	due_dt = other_inv.getDue_dt();

	currency = other_inv.getCurrency();
	exchng_nm = other_inv.getExchng_rate();
	exchng_eff_dt = other_inv.getExchng_eff_dt();
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
	exchang_val = other_inv.getExchang_val();
	remark1 = other_inv.getremark1();
	remark2 = other_inv.getremark2();
	remark3 = other_inv.getRemark3();
}
int len_multi_tax=0;


if(remark1.equals(""))
{
	remark1="Payment to be made in the name of \"Hazira Port Private Limited\" either by the Demand Draft Payable at Ahmedabad or by wire transfer. \nDD Charges is to your account.";
}
if(remark2.equals(""))
{
	remark2="The Invoice is raised converting USD into INR using the TT selling Rate notified by the State Bank Of India, Ahmedabad Branch on the day preceding to the date of Invoice." ;
}
if(remark3.equals(""))
{
	remark3="The Bank Details, for Wire Transfer through RTGS, are as follows:  \nHazira Port Private Limited\nBank Name: CITIBANK N.A.\nAccount Number: 522615056\nBank Address: Bombay Mutual Building, 293, Dr. D N Road, Fort, Mumbai-400001 \nIFSC CODE: CITI0100000";
}
%>
<body onload="<%if(!msg.equals("")){%>refreshParent('<%=accord%>','<%=msg%>','<%=msg_type%>');<%}%> <%if(operation.equals("MODIFY") && type.equals("EDIT")){for(int x=0;x<VSHIP_CD.size();x++){%>enableRate('<%=VDIS_PORT.elementAt(x) %>','<%=x %>');<%}} %>" 
<%if(type.equals("VIEW") && operation.equals("MODIFY")) {%>style="pointer-events: none;"<%} %>>
<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_other_invoice">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<%-- <%if(!msg.equals("")){
				if(msg_type.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
				<%}else if(msg_type.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
				<%}
			} %> --%>
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
							PFA Fees Invoice (HPPL-SEIPL)
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
	      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="inv_dt" value="<%=inv_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);checkInvoiceDueDate(this,document.forms[0].due_dt,'F');" autocomplete="off">
	      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
      						</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Payment Due Date</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
		      				<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="due_dt" value="<%=due_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);checkInvoiceDueDate(document.forms[0].inv_dt,this,'F');" autocomplete="off">
	      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
      						</div>
				    	</div>
					</div>
				    <div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Cargo Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="cargo_type" onchange="refresh();">
										<option value="0">--Select--</option> 
					  					<option value="LTCORA">LTCORA</option>
					  					<option value="SALES">SALES</option>
									</select>
									<script>document.forms[0].cargo_type.value="<%=cargo_type%>"</script>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>User Defined Day</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
		      				<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="user_dt" value="<%=user_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
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
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Exchange Rate<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="exchng_nm" onchange="refresh();">
										<option value="0">--Select--</option> 
					  					<%for(int i=0;i<VEXCHNG_RATE_CD.size();i++){ %>
												<option value="<%=VEXCHNG_RATE_CD.elementAt(i)%>"><%=VEXCHNG_RATE_NM.elementAt(i)%> - <%=VEXCHNG_RATE.elementAt(i)%></option>
											<%} %>
									</select>
									<script>document.forms[0].exchng_nm.value="<%=exchng_nm%>"</script>
									
				    			</div>
				    		</div>
				    	</div>
				    </div>
				    <%if(!cargo_type.equals("0")){%>
				    <div id="cargo_details">
				    	&nbsp;
					    <div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="table-responsive">
									<table class="table table-bordered table-hover">
										<thead>
											<tr>
												<th>Discharge Port</th>
												<th>Cargo Ref#</th>
												<th>Cargo Date</th>
												<th>Cargo Type</th>
												<th>Cargo Name</th>
												<th>MMBTU</th>
												<th>Rate(USD)</th>
												<th>Amount</th>
											</tr>
										</thead>
										<tbody id="cargo_itemTab">
											<% if(VSHIP_CD.size()>0) {%>
												<%for(int i=0;i<VSHIP_CD.size();i++) { %>
													<%if(operation.equals("MODIFY")) {
													dis_port =""+VDIS_PORT.elementAt(i);}%>
													<tr>
														<td align="center" > 
															<select class="form-select form-select-sm" name="dis_port" onchange="calcAmount('<%=operation%>','<%=i %>');" id="dis_port<%=i%>" >
																<option value="0">--Select--</option> 
																<option value="1">Hazira</option>
																<option value="2">Dahej</option>
															</select>
															<script>document.getElementById('dis_port<%=i%>').value="<%=dis_port%>"</script>
														</td>
														<td align="center"><%=VCARGO_REF.elementAt(i) %></td>
														<td align="center"><%=VCARGO_DT.elementAt(i) %></td>
														<td align="center"><%=VCARGO_TYPE.elementAt(i) %></td>
														<td align="center"><%=VSHIP_NAME.elementAt(i) %></td>
														<td align="center" id="qty_mmbtu<%=i%>"><%=VQTY_MMBTU.elementAt(i) %></td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="cargo_rate" id="cargo_rate<%=i%>" value="<%=VRATE.elementAt(i) %>" maxlength='20' style="width:100px; text-align:right;" onchange="calcAmount('<%=operation%>','<%=i %>');" readOnly>
														</td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="cargo_amt" id="cargo_amt<%=i%>" value="<%=VCARGO_AMT.elementAt(i) %>" maxlength='20' style="width:100px; text-align:right;" onchange="calcAmount('<%=operation%>','<%=i %>');"  readOnly>
														</td>
													</tr>
													<input type="hidden" name="cargo_ref" value="<%=VCARGO_REF.elementAt(i) %>">
													<input type="hidden" name="cargo_dt" value="<%=VCARGO_DT.elementAt(i) %>">
													<input type="hidden" name="ship_cd" value="<%=VSHIP_CD.elementAt(i) %>">
													<input type="hidden" name="qty_mmbtu" value="<%=VQTY_MMBTU.elementAt(i) %>">
												<%}
												} else {%>
												<tr>
													<td align="center" colspan="10"><%=utilmsg.infoMessage("<b>No "+cargo_type+" Cargo is Available!</b>") %></td>
												</tr>
											<%} %>
										</tbody>
									</table>
								</div>
							</div>
					    </div>
				    </div>
				    <% }%>
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
												<th>Rate</th>
												<th>Effective date</th>
												<th>Amount</th>
											</tr>
										</thead>
										<tbody id="itemTab">
											<tr>
												<td align="center">
												</td>
												<td align="center">
													<textarea class="form-control form-control-sm" name="desc_item" maxLength="80" cols="75" rows="1" style="width:250px;"><%=desc_item%></textarea>
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;' name="currency" >
														<option value="2">USD</option>
													</select>
													<script>document.forms[0].currency.value="<%=currency%>"</script>
												</td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="amount" value="<%=sale_amt%>" maxlength='20' style="width:100px;text-align:right;" readOnly>
												</td>
											</tr>
										</tbody>
										<tbody>
											
											<tr>
												<td align="center">
													<!-- <input type="checkbox" class="form-check-input"> -->
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="exchange_textarea" maxlength="80" style="width:250px;font-weight: bold;" value="Exchange Rate (INR/USD)" readOnly>
												</td>
												<td align="center">
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="exchng_rate" value="<%=exchang_val%>" maxlength='20' style="width:100px;text-align:right;" readOnly>
												</td>
												<td align="center">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm date fmsdtpick" name="exchng_eff_dt" <%if(!exchang_val.equals("")) {%>value="<%=user_dt%>"<%} else {%>value=""<% } %> maxLength="10" style="width:50px; pointer-events:none; background-color: #e9ecef" autocomplete="off" >
	      												<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
	      											</div>
												</td>
												<td align="center"></td>
											</tr>
											<tr>
												<td align="center">
													<!-- <input type="checkbox" class="form-check-input"> -->
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="" maxlength="80" style="width:250px;font-weight: bold;" value="Gross Amount (INR)" readOnly>
												</td>
												<td align="center">
													<select class="form-select form-select-sm" style='width:100px;'>
														<option value="1">INR</option>
													</select>
												</td>
												<td align="center"></td>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="total_amount" value="<%=gross_amount%>" maxlength='20' style="width:100px;text-align:right;" readOnly>
												</td>
											</tr>
											<tr>
												<td align="center">
													<input type="button" class="btn btn-sm config_btn" value="Tax Config" onclick="openTaxStructMst('<%=supp_state %>','<%=vend_state %>');">
													<input type="hidden" name="tax_cd" value="<%=tax_struct_cd%>">
													<input type="hidden" name="tax_dt" value="<%=tax_struct_dt%>">
													<input type="hidden" name="temp_tax_cd" value="<%=tax_struct_cd%>">
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="tax_struct_info" maxlength="80" style="width:250px;font-weight: bold;" value="Tax (<%=tax_struct_info%>)" readOnly>
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
												<td align="center"></td>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="tax_amt" value="<%=total_gst%>" maxlength='20' style="width:100px;text-align:right;" readOnly>
												</td>
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
															style="width:250px;font-weight: bold; text-align: right;" readOnly value="<%=((Vector) temp.elementAt(1)).elementAt(j)%>">								
														</td>
														<td align="center">
															<select class="form-select form-select-sm" style='width:100px;' name="sub_tax_amt_unit" id="sub_tax_amt_unit_<%=j%>">
											  					<option value="1">INR</option>
															</select>
															<script>document.getElementById("sub_tax_amt_unit_<%=j%>").value="<%=invoice_raised_in%>"</script>
														</td>
														<td align="center"></td>
														<td align="center"></td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="sub_tax_amt" value="<%=((Vector) temp.elementAt(2)).elementAt(j)%>" maxlength='20' style="width:100px;text-align:right;" readOnly>
														</td>
													</tr>
													<%}
												}
											} %>
											<tr>
												<td align="center"></td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="" maxlength="80" style="width:250px;font-weight: bold;" value="Invoice Amount" readOnly>
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
													<input type="text" class="form-control form-control-sm" name="total_amt_inr" value="<%=net_amt%>" maxlength='20' style="width:100px;text-align:right;" readOnly>
												</td>
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
				      				<textarea class="form-control" name="remark1" cols="75" rows="3" onKeyDown="textCounter(this.form.remark1,this.form.remLen1,999);" onKeyUp="textCounter(this.form.remark1,this.form.remLen1,999);"><%=remark1%></textarea>
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
				      				<textarea class="form-control" name="remark2" cols="75" rows="3" onKeyDown="textCounter(this.form.remark2,this.form.remLen2,999);" onKeyUp="textCounter(this.form.remark2,this.form.remLen2,999);"><%=remark2%></textarea>
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
				      				<textarea class="form-control" name="remark3" cols="75" rows="6" onKeyDown="textCounter(this.form.remark3,this.form.remLen3,999);" onKeyUp="textCounter(this.form.remark3,this.form.remLen3,999);"><%=remark3%></textarea>
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

<input type="hidden" name="option" value="HPPL_SEIPL">
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