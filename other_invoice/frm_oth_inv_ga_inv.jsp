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
	var bu_unit = document.forms[0].bu_unit.value;
	//var invoice_category = document.forms[0].invoice_category.value;
	//var exchng_eff_dt = document.forms[0].exchng_eff_dt.value;
	//var tax_struct = document.forms[0].tax_structure.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var inv_type = document.forms[0].inv_type.value;
	var operation = document.forms[0].operation.value;
	var accord = document.forms[0].accord.value;
	var u = document.forms[0].u.value;
	
		var url = "frm_oth_inv_ga_inv.jsp?supp_cd="+supp_cd+"&vend_cd="+vend_cd+"&inv_dt="+inv_dt+"&bu_unit="+bu_unit+"&accord="+accord
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
	
	var len=document.forms[0].amount.length;	
	if(len==undefined)
	{
		if(document.forms[0].desc_item.value=='' || document.forms[0].sac_code.value=='' || document.forms[0].amount.value=='')
		{
			if(document.forms[0].desc_item.value=='')
			{
				msg += "Please Enter Line Item Description\n";
	  	 	 	flag = false;
			}
			if(document.forms[0].sac_code.value=='')
			{
				msg += "Please Enter SAC/HSN Code\n";
	  	 	 	flag = false;
			}
			if(document.forms[0].amount.value=='')
			{
				msg += "Please Enter Line Item Amount\n";
	  	 	 	flag = false;
			}
		}
	}
	else 
	{
		var cnt=0,cnt1=0,cnt2=0;
		for(var i=0;i<len;i++)
		{
			if(document.forms[0].desc_item[i].value=='' || document.forms[0].sac_code[i].value=='' || document.forms[0].amount[i].value=='')
			{
 	 			if(document.forms[0].desc_item[i].value=='')
 	 	 		{
 	 				cnt++;
 	 	 	 		flag = false;
 	 	 	 	}
 	 			if(document.forms[0].sac_code[i].value=='')
 				{
 	 				cnt1++;
 	 	 	 		flag = false;
 				}
 	 			if(document.forms[0].amount[i].value=='')
 				{
 	 				cnt2++;
 	 	 	 		flag = false;
 				}
			}
		}
		
		if(cnt>0)
 		{
			msg += "Item Description Cannot Be Blank For any line Item\n";
 		}
 		if(cnt1>0)
 		{
 			msg += "SAC/HSN Code Cannot Be Blank For any line Item\n";
 		}
 		if(cnt2>0)
 		{
 			msg += "Line Item Amount Cannot Be Blank For any line Item\n";
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



function removeRow(row_id, seq_no)
{
	var row_cnt = document.forms[0].item_size.value;
	
	if(parseInt(row_cnt) == parseInt(seq_no))
	{
		var total_amt = document.forms[0].total_amount.value;
		
		if(total_amt!='' && total_amt!=null && total_amt!=undefined)
		{
			var amt = document.getElementById("amount"+seq_no).value;
			total_amt -= amt;
			document.forms[0].total_amount.value = parseFloat(Math.round(total_amt)).toFixed(2);
			taxCalcAmt1();
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



function calcAmount1(index) 
{
    var total = 0;
    var totalRows = parseInt(document.getElementById("item_size").value) || 1;

    for (var i = 1; i <= totalRows; i++) 
    {
        if (!document.getElementById("amount" + i)) continue;
        var rowAmt = parseFloat(document.getElementById("amount" + i).value) || 0;
        total += rowAmt;
        
    }
    document.forms[0].total_amount.value = parseFloat(Math.round(total)).toFixed(2);
    taxCalcAmt1();
}

function taxCalcAmt1()
{
	var totalRows = parseInt(document.getElementById("item_size").value) || 1;
	
	var total_amount = document.forms[0].total_amount.value;
	var tax_name="";
	var tax1="";
	var tax2="";
	var tot_tax = 0;
	var sub_tax_amt="";
	
	if (total_amount == "" || total_amount == " ") 
	{
		total_amount = "0";
	}
	
	for (var i = 1; i <= totalRows; i++) 
    {
		var tax_name = document.getElementById("tax_struct_nm1"+ i).value;
		var amount = (parseFloat(document.getElementById("amount" + i).value) || 0).toFixed(2);

		var sub_tax_name=tax_name.split(", ");
		
		if(tax_name.includes(","))
		{
			tax2=0;
			for(j=0;j<sub_tax_name.length;j++) 
	 		{
	 			tax2 = Math.round((sub_tax_name[0].split(",")[0].split(" ")[1].split("%")[0] * amount) / 100);
				sub_tax_amt = tax2;
	 		} 
			document.getElementById("tax_amount"+ i).value=parseFloat(Math.round(sub_tax_amt)).toFixed(2);
			document.getElementById("tax_amount2"+ i).value=parseFloat(Math.round(sub_tax_amt)).toFixed(2);
			
			tot_tax = tot_tax + Number(document.getElementById("tax_amount"+ i).value) + Number(document.getElementById("tax_amount2"+ i).value);	
			
			document.getElementById("total_line_amt"+ i).value = parseFloat(Math.round(Number(sub_tax_amt * 2) + Number(document.getElementById("amount"+ i).value))).toFixed(2);
		}
		else 
		{
			if(!tax_name || tax_name.trim() === "")
			{
				tax1 = 0;
				tax_amount = amount * tax1 / 100;
			}
			else
			{
				tax1 = (tax_name.split(",")[0].split(" ")[1].split("%")[0] * amount) / 100;
				tax_amount = tax1;
			}
			document.getElementById("tax_amount"+ i).value=parseFloat(Math.round(tax_amount)).toFixed(2);
			
			tot_tax = tot_tax + Number(document.getElementById("tax_amount"+ i).value);	
			
			document.getElementById("total_line_amt"+ i).value = parseFloat(Math.round(Number(document.getElementById("tax_amount"+ i).value) + Number(document.getElementById("amount"+ i).value))).toFixed(2);
		}
    }
	document.forms[0].total_tax.value = parseFloat(tot_tax).toFixed(2);
	
	var tot_amt = 0;
	
	tot_amt += Number(document.forms[0].total_tax.value) + Number(document.forms[0].total_amount.value);
	
	document.forms[0].total_amt_inr.value = parseFloat(tot_amt).toFixed(2); 

}

function openTaxStructMst1(supp_state,vend_state,index)
{
	var type="";
	
	if(document.forms[0].invoice_category.value != "")
	{
		type="GA";
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
			newWindow = window.open("frm_other_inv_tax_structure_list.jsp?type="+type+"&index="+index+"&supp_state="+supp_state+"&vend_state="+vend_state,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_other_inv_tax_structure_list.jsp?type="+type+"&index="+index+"&supp_state="+supp_state+"&vend_state="+vend_state,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function setTaxStructDetail1(tax_struct_cd,tax_struct_nm,tax_struct_eff_dt,index)
{
	//var info = "Tax ("+tax_struct_nm+")";
	document.getElementById("tax_struct_nm1" + index).value=tax_struct_nm
	document.getElementById("tax_struct_info1" + index).value = tax_struct_nm;
	if(tax_struct_nm.includes("CGST"))
	{
		document.getElementById("tax_struct_info1" + index).value = tax_struct_nm.split(", ")[0];
		document.getElementById("tax_struct_info2" + index).value = tax_struct_nm.split(", ")[1];
	}
	
	document.getElementById("tax_cd1" + index).value=tax_struct_cd
	document.getElementById("tax_dt1" + index).value=tax_struct_eff_dt
	document.getElementById("tax_struct_nm").value=tax_struct_nm
	
	taxCalcAmt1();
}
function printPDF(comp_cd,fin_year,bu_seq,inv_seq)
{
	var supp_cd = document.forms[0].supp_cd.value;
	var is_print="0";
	var inv_type="GA";
	
	var url = "../other_invoice/pdf_other_invoice.jsp?comp_cd="+comp_cd+"&is_print="+is_print+"&fin_year="+fin_year+
		"&inv_type="+inv_type+"&supp_cd="+supp_cd+"&bu_seq="+bu_seq+"&inv_seq="+inv_seq;
	
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
String operation=request.getParameter("operation")==null?"INSERT":request.getParameter("operation");
String inv_type=request.getParameter("inv_type")==null?"":request.getParameter("inv_type");
String inv_dt = request.getParameter("inv_dt")==null?"":request.getParameter("inv_dt");
String currency = request.getParameter("currency")==null?"1":request.getParameter("currency");
String tax_structure=request.getParameter("tax_structure")==null?"0":request.getParameter("tax_structure");
String desc_item = request.getParameter("desc_item")==null?"":request.getParameter("desc_item");
String amount = request.getParameter("amount")==null?"":request.getParameter("amount");
String accord = request.getParameter("accord")==null?"":request.getParameter("accord");
String type = request.getParameter("type")==null?"":request.getParameter("type");
String invoice_category = request.getParameter("invoice_category")==null?"S":request.getParameter("invoice_category");
String bu_unit = request.getParameter("bu_unit")==null?"2":request.getParameter("bu_unit");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String inv_no = request.getParameter("inv_no")==null?"":request.getParameter("inv_no");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String p_start_dt = utildate.getFirstDateOfMonth(month, year);
String p_end_dt = utildate.getLastDateOfMonth(month, year);
String finYr = request.getParameter("fin_year")==null?"":request.getParameter("fin_year");

other_inv.setCallFlag("OTH_INV_GA_INVOICE");
other_inv.setSupplier_Cd(supp_cd);
other_inv.setVendor_cd(vend_cd);
other_inv.setBu_unit(bu_unit);
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
Vector VTAX_STRUCTURE_CD=other_inv.getVTAX_STRUCTURE_CD();
Vector VTAX_STRUCTURE_DESC=other_inv.getVTAX_STRUCTURE_DESC();
Vector VMULTI_TAX_STRUCT=other_inv.getVMULTI_TAX_STRUCT();
Vector VTAX_STRUCT_APP_DT=other_inv.getVTAX_STRUCT_APP_DT();
Vector VTAX_AMT=other_inv.getVTAX_AMT();
Vector VLINE_NO=other_inv.getVLINE_NO();
Vector VGITEM_AMT=other_inv.getVGITEM_AMT();
Vector VGAMT_DES=other_inv.getVGAMT_DES();
Vector VSAC_CODE=other_inv.getVSAC_CODE();
Vector VBU_CD=other_inv.getVBU_CD();
Vector VBU_NM=other_inv.getVBU_NM();
Vector VSAC_CD=other_inv.getVSAC_CD();
Vector VSAC_DESC=other_inv.getVSAC_DESC();
Vector VTAX_STRUCTURE=other_inv.getVTAX_STRUCTURE();
Vector VTAX_STRUCTURE1=other_inv.getVTAX_STRUCTURE1();
Vector VITEM_TOTAL=other_inv.getVITEM_TOTAL(); 
Vector VSAC_VAL=other_inv.getVSAC_VAL(); 

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
String supp_state_tin=other_inv.getSupp_state_tin();
String activity_value = other_inv.getActivity_value();

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
String tax_amt = "";
String sale_amt = "";
String total_gst = "";
String gross_amount = "";
String net_amt = "";
String remark1 = "";
String remark2 = "";
String fin_yr = "";
String inv_seq = "";
String tax_struct_dt="";
String tax_struct_cd="";
String tax_struct_info="";

String tax_struct_dt1[]={""};
String tax_struct_cd1[]={""};
String tax_struct_info1[]={""};
supp_cd = other_inv.getSupp_cd();
bu_unit = other_inv.getBu_unit();
if(operation.equals("MODIFY"))
{
	invoice_category = other_inv.getInvoice_category();
	
	vend_cd = other_inv.getVendor_cd();
	inv_dt = other_inv.getInv_dt();
	currency = other_inv.getCurrency();
	tax_structure = other_inv.getTax_name();
	tax_amt = other_inv.getTax_amt();
	total_gst = other_inv.getTotal_gst();
	desc_item = other_inv.getDesc_item();
	sale_amt = other_inv.getSale_amt();
	gross_amount = other_inv.getGross_amt();
	net_amt = other_inv.getNet_amt();
	remark1 = other_inv.getremark1();
	remark2 = other_inv.getremark2();
	fin_yr = other_inv.getFinanical_yr();
	inv_seq = other_inv.getInvoice_seq();
	tax_struct_dt = other_inv.getTax_struct_dt();
	tax_struct_cd = other_inv.getTax_struct_cd();
	tax_struct_info = other_inv.getTax_struct_info();
	remark1 = other_inv.getremark1();
}
/* if(remark1.equals(""))
{
	if(supp_abbr.equals("HPPL"))
	{
		remark1="The Bank Details, for Wire Transfer through RTGS, are as follows:  \nHazira Port Private Limited\nBank Name: CITIBANK N.A.\nAccount Number: 522615056\nBank Address: Bombay Mutual Building, 293, Dr. D N Road, Fort, Mumbai-400001 \nIFSC CODE: CITI0100000";
	}
	else if(supp_abbr.equals("SEIPL"))
	{
		remark1="The Bank Details, for Wire Transfer through RTGS, are as follows:  \nShell Energy India Private Limited\nBank Name: CITIBANK N.A.\nAccount Number: 522606081\nBank Address: Bombay Mutual Building, 293, Dr. D N Road, Fort, Mumbai-400001 \nIFSC CODE: CITI0100000";
	}
	else
	{
		remark1="The Bank Details, for Wire Transfer through RTGS, are as follows:  \nHazira Port Private Limited\nBank Name: CITIBANK N.A.\nAccount Number: 522615056\nBank Address: Bombay Mutual Building, 293, Dr. D N Road, Fort, Mumbai-400001 \nIFSC CODE: CITI0100000";
	}
} */
int len_multi_tax=0;
%>
<body onload="<%if(!msg.equals("")){%>refreshParent('<%=accord%>','<%=msg%>','<%=msg_type%>');<%}%> " 
<%if(type.equals("VIEW") && operation.equals("MODIFY")) {%>style="pointer-events: none;"<%} %> >
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
							GNA Invoice
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
						    			<label class="form-label"><b>Business Unit <span class="s-red">*</span></b></label>
						  			</div>
								</div>
								<div class="col-sm-10 col-xs-10 col-md-10">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
							      			<select class="form-select form-select-sm" name="bu_unit" style="pointer-events:none; background-color: #e9ecef">
												<option value="0">--Select--</option>
											<%for(int i=0;i<VBU_CD.size();i++){ %>
												<option value="<%=VBU_CD.elementAt(i)%>"><%=VBU_NM.elementAt(i)%></option>
											<%} %>
											</select>
											<script>document.forms[0].bu_unit.value="<%=bu_unit%>"</script>
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
	      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="inv_dt" value="<%=inv_dt%>" maxLength="10" onblur="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');" onchange="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');" autocomplete="off">
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
				     <div id="lineItem">
				    	&nbsp;
					    <div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="table-responsive">
									<table class="table table-bordered table-hover">
										 <thead>
										    <tr>
										    	<th rowspan="2">Sr#</th>
										        <th rowspan="2">Line Item</th>
										        <th rowspan="2">HSN/SAC Code</th>
										        <th rowspan="2">Amount</th>
										        <th rowspan="2">Tax</th>
										        <% if(supp_state.equals(vend_state)) { %>
											        <th colspan="2">CGST</th>
											        <th colspan="2">SGST</th>
											    <%} else {%> 
											   		<th colspan="2">IGST</th>
											    <%}%>
											    <th rowspan="2">Total Amount</th>
										        <th rowspan="2">Add/Del line</th>
										    </tr>
										    <tr>
										        <% if(supp_state.equals(vend_state)) { %>
											        <th>Rate</th>
											        <th>Amount</th>
											        <th>Rate</th>
											        <th>Amount</th>
											    <%} else {%> 
											   		<th>Rate</th>
											        <th>Amount</th>
											    <%}%>
										    </tr>
										</thead>
										<tbody id="itemTab">
											<%for(int i=0;i<VLINE_NO.size(); i++){%>
												<tr>
													<td align="center">
														<input type="text" class="form-control form-control-sm" name="item_seq" value="<%=VLINE_NO.elementAt(i)%>" style="width:40px;text-align:center;" onblur="checkNumber1(this,2,0);" readOnly>
													</td>
													<td align="center">
														<textarea class="form-control form-control-sm" name="desc_item" maxLength="100" cols="75" rows="1" style="width:250px;"><%=VGAMT_DES.elementAt(i)%></textarea>
													</td>
													<td align="center">
														<div style="display:flex; justify-content:center; align-items:center; width:100%; margin:auto;">
															<select class="form-select form-select-sm" name="sac_code" id="sac_code<%=i+1%>" style="width:100px;">
																<option value="0">--Select--</option>
																	<%for(int j=0;j<VSAC_CD.size();j++){ %>
																<option value="<%=VSAC_CD.elementAt(j)%>"><%=VSAC_CODE.elementAt(j)%></option>
																	<%} %>
															</select>
															<script>
																document.getElementById("sac_code<%=i+1%>").value="<%=VSAC_VAL.elementAt(i)%>" 
															</script>
														</div>	
													</td>
													<td align="center">
														<input type="text" class="form-control form-control-sm" name="amount" id="amount<%=i+1%>" value="<%=VGITEM_AMT.elementAt(i)%>" maxlength='20' style="width:100px;text-align:right;" onchange="calcAmount1('<%=VLINE_NO.elementAt(i)%>');" autocomplete="off">
													</td>
													<td align="center">
														<div style="display:flex; justify-content:center; align-items:center; gap:6px; width:100%; margin:auto;">
															<input type="button" class="btn btn-sm config_btn" value="Tax Config" onclick="openTaxStructMst1('<%=supp_state %>','<%=vend_state %>','<%=VLINE_NO.elementAt(i)%>');">
															<input type="hidden" name="tax_cd1" id="tax_cd1<%=i+1%>" value="<%=VTAX_STRUCTURE_CD.elementAt(i)%>">
															<input type="hidden" name="tax_dt1" id="tax_dt1<%=i+1%>" value="<%=VTAX_STRUCT_APP_DT.elementAt(i)%>">
															<input type="hidden" name="temp_tax_cd1" id="temp_tax_cd1<%=i+1%>" value="<%=VTAX_STRUCTURE_CD.elementAt(i)%>">
															<input type="hidden" name="tax_struct_nm" id="tax_struct_nm" value="<%=VTAX_STRUCTURE_DESC.elementAt(i)%>">
															<input type="hidden" name="tax_struct_nm1" id="tax_struct_nm1<%=i+1%>" value="<%=VTAX_STRUCTURE_DESC.elementAt(i)%>">	
														</div>		
													</td>
													
												    <% if(supp_state.equals(vend_state)) { %>
												        <td align="center"> 
												        	<input type="text" class="form-control form-control-sm" name="tax_struct_info1" id="tax_struct_info1<%=i+1%>" value="<%=VTAX_STRUCTURE.elementAt(i)%>" style="width:70px;font-weight:bold;" readOnly>
												        </td>
												        <td align="center">
												            <input type="text" class="form-control form-control-sm" name="tax_amount" id="tax_amount<%=i+1%>" value="<%=VTAX_AMT.elementAt(i)%>" style="width:100px;text-align:right;" readOnly>
												        </td>
												        <td align="center">
												            <input type="text" class="form-control form-control-sm" name="tax_struct_info2" id="tax_struct_info2<%=i+1%>" value="<%=VTAX_STRUCTURE1.elementAt(i)%>" style="width:70px;font-weight:bold;" readOnly>
												        </td>
												        <td align="center"> 
												        	<input type="text" class="form-control form-control-sm" name="tax_amount2" id="tax_amount2<%=i+1%>" value="<%=VTAX_AMT.elementAt(i)%>" style="width:100px;text-align:right;" readOnly>
												        </td>
												    <% } else { %>
												        <td align="center">
												            <input type="text" class="form-control form-control-sm" name="tax_struct_info1" id="tax_struct_info1<%=i+1%>" value="<%=VTAX_STRUCTURE_DESC.elementAt(i)%>" style="width:80px;font-weight:bold;" readOnly>
												        </td>
												        <td align="center">
												        	<input type="text" class="form-control form-control-sm" name="tax_amount" id="tax_amount<%=i+1%>" value="<%=VTAX_AMT.elementAt(i)%>" style="width:100px;text-align:right;" readOnly>
												        </td>
												    <% } %>
												     <td align="center">
												         <input type="text" class="form-control form-control-sm" name="total_line_amt" id="total_line_amt<%=i+1%>" value="<%=VITEM_TOTAL.elementAt(i)%>" style="width:100px;text-align:right;" readOnly>
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
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="" maxlength="80" style="width:250px;font-weight: bold;" value="Gross Amount" readOnly>
												</td>
												<td align="center">
												</td>
												<td align="center">
												</td>
												<td align="center">
												</td>
												
												<% if(supp_state.equals(vend_state)) { %>
											        <td align="center"> 
											        </td>
											        <td align="center">
											        </td>
											        <td align="center">
											        </td>
											        <td align="center"> 
											        </td>
												 <% } else { %>
											        <td align="center">
											        </td>
											        <td align="center">
											        </td>
												 <% } %>
												  <td align="center">
												  	<input type="text" class="form-control form-control-sm" name="total_amount" value="<%=gross_amount%>" maxlength='20' style="width:100px;text-align:right;" readOnly>
												  </td>
												  <td align="center">
												</td>
											</tr>
											<tr>
												<td align="center">
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="" maxlength="80" style="width:250px;font-weight: bold;" value="Total Tax Amount" readOnly>
												</td>
												<td align="center">
												</td>
												<td align="center">
												</td>
												<td align="center">
												</td>
												<% if(supp_state.equals(vend_state)) { %>
											        <td align="center"> 
											        </td>
											        <td align="center">
											        </td>
											        <td align="center">
											        </td>
											        <td align="center"> 
											        </td>
												 <% } else { %>
											        <td align="center">
											        </td>
											        <td align="center">
											        </td>
												 <% } %>
												 <td align="center">
												 	<input type="text" class="form-control form-control-sm" name="total_tax" value="<%=tax_amt%>" maxlength='20' style="width:100px;text-align:right;" readOnly>
												  </td>
												  <td align="center">
												</td>
											</tr>
											<tr>
												<td align="center">
												</td>
												<td align="center">
													<input type="text" class="form-control form-control-sm" name="" maxlength="80" style="width:250px;font-weight: bold;" value="Invoice Amount" readOnly>
												</td>
												<td align="center">
												</td>
												<td align="center">
												</td>
												<td align="center">
												</td>
												<% if(supp_state.equals(vend_state)) { %>
											        <td align="center"> 
											        </td>
											        <td align="center">
											        </td>
											        <td align="center">
											        </td>
											        <td align="center"> 
											        </td>
												 <% } else { %>
											        <td align="center">
											        </td>
											        <td align="center">
											        </td>
												 <% } %>
												 <td align="center">
												 	<input type="text" class="form-control form-control-sm" name="total_amt_inr" value="<%=net_amt%>" maxlength='20' style="width:100px;text-align:right;" readOnly>
												  </td>
												  <td align="center">
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
				    			<label class="form-label"><b>Remark</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<font size="1" face="arial, helvetica, sans-serif">&nbsp;( You may enter up to 1000 characters. )&nbsp;
										<input readonly type=text name="remLen1" size="3" maxlength="3" class=""> characters left
									</font><br>
				      				<textarea class="form-control" name="remark1" cols="75" rows="6" onKeyDown="textCounter(this.form.remark1,this.form.remLen1,999);" onKeyUp="textCounter(this.form.remark1,this.form.remLen1,999);"><%=remark1%></textarea>
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
						        <span class="fa-stack fa-lg" title="Print Temp PDF" onclick="printPDF('<%=owner_cd%>','<%=finYr%>','<%=bu_unit%>','<%=inv_seq%>');" style="position: relative;pointer-events: auto;<%if(operation.equals("INSERT")){ %>display:none;<%} %>">
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

<input type="hidden" name="option" value="GA_INVOICE">
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
<input type="hidden" name="supp_state_tin" value="<%=supp_state_tin%>">
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

<script type="text/javascript">
    var VSAC_CD = [
        <% for(int j=0; j<VSAC_CD.size(); j++) { %>
            "<%=VSAC_CD.elementAt(j)%>"<% if(j < VSAC_CD.size()-1) { %>, <% } %>
        <% } %>
    ];

    var VSAC_CODE = [
        <% for(int j=0; j<VSAC_CODE.size(); j++) { %>
            "<%=VSAC_CODE.elementAt(j)%>"<% if(j < VSAC_CODE.size()-1) { %>, <% } %>
        <% } %>
    ];
    
    var VSAC_VAL = [ 
    	<% for(int j=0; j<VSAC_VAL.size(); j++) { %>
    		"<%=VSAC_VAL.elementAt(j)%>"<% if(j < VSAC_VAL.size()-1) { %>, <% } %>
    	<% } %> 
    ];
</script>

<script>
function addrow() 
{

    var max_seq = document.getElementById("item_size").value;
    var prev_minus = document.getElementById("minus" + max_seq);
    var prev_plus = document.getElementById("plus" + max_seq);
    var new_seq_no = parseInt(max_seq)+1;
    var inv_cat = document.forms[0].invoice_category.value
    var supp_state = document.forms[0].supp_state.value
    var vend_state = document.forms[0].vend_state.value
    
    if (new_seq_no > 7) 
    {
        alert("Maximum number of lines(7) added for the Invoice!");
        return;
    }
    else 
    {
		var tab = document.getElementById("itemTab");
		var tr = document.createElement("tr");
		tr.id = "row" + new_seq_no;

		var td01 = document.createElement("td");
		td01.align = "center"; 

		var input01 = document.createElement("input");
		input01.type = "text";
		input01.className = "form-control form-control-sm";
		input01.name = "item_seq";
		input01.value = new_seq_no; 
		input01.style.width = "40px";
		input01.style.textAlign = "center";
		input01.readOnly = true;
		input01.setAttribute("onblur", "checkNumber1(this,2,0);");

		td01.appendChild(input01);
		tr.appendChild(td01);

		
		var tdDesc = document.createElement("td");
		tdDesc.align = "center";
		var desc = document.createElement("textarea");
		desc.className = "form-control form-control-sm";
		desc.name = "desc_item";
		desc.maxLength = "100";
		desc.cols = "75";
		desc.rows = "1";
		desc.style = "width:250px;";
		tdDesc.appendChild(desc);
		tr.appendChild(tdDesc);

	    var tdSac = document.createElement("td");
	    tdSac.align = "center";

	    var divWrapper = document.createElement("div");
	    divWrapper.style.display = "flex";
	    divWrapper.style.justifyContent = "center";
	    divWrapper.style.alignItems = "center";
	    divWrapper.style.width = "100%";
	    divWrapper.style.margin = "auto";

	    var sacSelect = document.createElement("select");
	    sacSelect.className = "form-select form-select-sm";
	    sacSelect.name = "sac_code";
	    sacSelect.id = "sac_code" + new_seq_no;
	    sacSelect.style.width = "100px";

	    var opt0 = document.createElement("option");
	    opt0.value = "0";
	    opt0.text = "--Select--";
	    sacSelect.appendChild(opt0);

	    for (var j = 0; j < VSAC_CD.length; j++) {
	        var opt = document.createElement("option");
	        opt.value = VSAC_CD[j];
	        opt.text = VSAC_CODE[j];
	        sacSelect.appendChild(opt);
	    }

	    divWrapper.appendChild(sacSelect);
	    tdSac.appendChild(divWrapper);
	    tr.appendChild(tdSac);

		var tdAmt = document.createElement("td");
		tdAmt.align = "center";
		var amt = document.createElement("input");
		amt.type = "text";
		amt.className = "form-control form-control-sm";
		amt.name = "amount";
		amt.id = "amount" + new_seq_no;
		amt.maxLength = "20";
		amt.style = "width:100px;text-align:right;";
		amt.setAttribute("onchange","calcAmount1('" + new_seq_no + "');");
		tdAmt.appendChild(amt);
		tr.appendChild(tdAmt);

		var tdTax = document.createElement("td");
		tdTax.align = "center";
		var flexDiv = document.createElement("div");
		flexDiv.style = "display:flex; justify-content:center; align-items:center; gap:6px; width:100%; margin:auto;";
		var taxBtn = document.createElement("input");
		taxBtn.type = "button";
		taxBtn.className = "btn btn-sm config_btn";
		taxBtn.value = "Tax Config";
		taxBtn.setAttribute("onclick", "openTaxStructMst1('" + supp_state + "', '" + vend_state + "', '" + new_seq_no + "');");
		flexDiv.appendChild(taxBtn);
		var hidden1 = document.createElement("input");
		hidden1.type = "hidden";
		hidden1.name = "tax_cd1";
		hidden1.id = "tax_cd1" + new_seq_no;
		flexDiv.appendChild(hidden1);
		var hidden2 = document.createElement("input");
		hidden2.type = "hidden";
		hidden2.name = "tax_dt1";
		hidden2.id = "tax_dt1" + new_seq_no;
		flexDiv.appendChild(hidden2);
		var hidden3 = document.createElement("input");
		hidden3.type = "hidden";
		hidden3.name = "temp_tax_cd1";
		hidden3.id = "temp_tax_cd1" + new_seq_no;
		flexDiv.appendChild(hidden3);
		var hidden4 = document.createElement("input");
		hidden4.type = "hidden";
		hidden4.name = "tax_struct_nm1";
		hidden4.id = "tax_struct_nm1" + new_seq_no;
		flexDiv.appendChild(hidden4);
		tdTax.appendChild(flexDiv);
		tr.appendChild(tdTax);

		if (supp_state === vend_state) {
		    var tdInfo1 = document.createElement("td");
		    tdInfo1.align = "center";
		    var info1 = document.createElement("input");
		    info1.type = "text";
		    info1.className = "form-control form-control-sm";
		    info1.name = "tax_struct_info1";
		    info1.id = "tax_struct_info1" + new_seq_no;
		    info1.style = "width:70px;font-weight:bold;";
		    info1.readOnly = true;
		    tdInfo1.appendChild(info1);
		    tr.appendChild(tdInfo1);

		    var tdTaxAmt = document.createElement("td");
		    tdTaxAmt.align = "center";
		    var taxAmt = document.createElement("input");
		    taxAmt.type = "text";
		    taxAmt.className = "form-control form-control-sm";
		    taxAmt.name = "tax_amount";
		    taxAmt.id = "tax_amount" + new_seq_no;
		    taxAmt.style = "width:100px;text-align:right;";
		    taxAmt.readOnly = true;
		    tdTaxAmt.appendChild(taxAmt);
		    tr.appendChild(tdTaxAmt);

		    var tdInfo2 = document.createElement("td");
		    tdInfo2.align = "center";
		    var info2 = document.createElement("input");
		    info2.type = "text";
		    info2.className = "form-control form-control-sm";
		    info2.name = "tax_struct_info2";
		    info2.id = "tax_struct_info2" + new_seq_no;
		    info2.style = "width:70px;font-weight:bold;";
		    info2.readOnly = true;
		    tdInfo2.appendChild(info2);
		    tr.appendChild(tdInfo2);

		    var tdTaxAmt2 = document.createElement("td");
		    tdTaxAmt2.align = "center";
		    var taxAmt2 = document.createElement("input");
		    taxAmt2.type = "text";
		    taxAmt2.className = "form-control form-control-sm";
		    taxAmt2.name = "tax_amount2";
		    taxAmt2.id = "tax_amount2" + new_seq_no;
		    taxAmt2.style = "width:100px;text-align:right;";
		    taxAmt2.readOnly = true;
		    tdTaxAmt2.appendChild(taxAmt2);
		    tr.appendChild(tdTaxAmt2);
		} else {
		    var tdInfo = document.createElement("td");
		    tdInfo.align = "center";
		    var info = document.createElement("input");
		    info.type = "text";
		    info.className = "form-control form-control-sm";
		    info.name = "tax_struct_info1";
		    info.id = "tax_struct_info1" + new_seq_no;
		    info.style = "width:80px;font-weight:bold;";
		    info.readOnly = true;
		    tdInfo.appendChild(info);
		    tr.appendChild(tdInfo);

		    var tdTaxAmt = document.createElement("td");
		    tdTaxAmt.align = "center";
		    var taxAmt = document.createElement("input");
		    taxAmt.type = "text";
		    taxAmt.className = "form-control form-control-sm";
		    taxAmt.name = "tax_amount";
		    taxAmt.id = "tax_amount" + new_seq_no;
		    taxAmt.style = "width:100px;text-align:right;";
		    taxAmt.readOnly = true;
		    tdTaxAmt.appendChild(taxAmt);
		    tr.appendChild(tdTaxAmt);
		}

		var tdTotal = document.createElement("td");
		tdTotal.align = "center";
		var total = document.createElement("input");
		total.type = "text";
		total.className = "form-control form-control-sm";
		total.name = "total_line_amt";
		total.id = "total_line_amt" + new_seq_no;
		total.style = "width:100px;text-align:right;";
		total.readOnly = true;
		tdTotal.appendChild(total);
		tr.appendChild(tdTotal);

		var tdIcons = document.createElement("td");
		tdIcons.align = "center";
		var minus = document.createElement("a");
		minus.id = "minus" + new_seq_no;
		minus.setAttribute("onclick", "removeRow('row" + new_seq_no + "', '" + new_seq_no + "');");
		var mIcon = document.createElement("i");
		mIcon.className = "fa fa-minus-circle fa-2x";
		minus.appendChild(mIcon);
		var plus = document.createElement("a");
		plus.id = "plus" + new_seq_no;
		plus.setAttribute("onclick", "addrow();");
		var pIcon = document.createElement("i");
		pIcon.className = "fa fa-plus-circle fa-2x";
		plus.appendChild(pIcon);
		tdIcons.appendChild(minus);
		tdIcons.appendChild(document.createTextNode(" "));
		tdIcons.appendChild(plus);
		tr.appendChild(tdIcons);

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
</script>

</form>
</body>
</html>