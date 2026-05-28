<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh(operation)
{
	var invoice_type = document.forms[0].invoice_type.value;
	var sel_inv_no = document.forms[0].sel_inv_no.value;
	var cr_dr_type = "";
	if(document.getElementsByName("cr_dr_type").length > 0)
	{
		cr_dr_type = document.forms[0].cr_dr_type.value;
	}
	
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var prev_month = document.forms[0].prev_month.value;
	var prev_year = document.forms[0].prev_year.value;
	
	var accroid = document.forms[0].accroid.value;
	var u = document.forms[0].u.value;
	
	if(prev_month!=month || prev_year!=year)
	{
		sel_inv_no="";
	}
	
	var msg="";
	var flag = true;
	if(flag)
	{
		var url = "frm_oth_inv_scrap_fixed_asset_crdr.jsp?u="+u+"&invoice_type="+invoice_type+"&cr_dr_type="+cr_dr_type+
				"&month="+month+"&year="+year+"&operation="+operation+"&accroid="+accroid+"&sel_inv_no="+sel_inv_no;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}

function changeTblhed(obj)
{
	var typ="";
	if(obj=="CR")
	{
		typ="Credit Note";
	}
	else if(obj=="DR")
	{
		typ="Debit Note";
	}
	typ=typ+" [B-A]";
	document.getElementById("th_invType").innerHTML=typ
}

async function calc(index)
{
	var new_qty=document.getElementById("new_item_qty"+index).value;
	var new_price=document.getElementById("new_item_price"+index).value;
	
	if(new_price!="")
	{
		document.getElementById("new_item_amt"+index).value = parseFloat(Math.round(parseFloat(new_qty)*parseFloat(new_price))).toFixed(2);
		
		if(document.forms[0].invoice_category.value=="P")
	    {
			document.getElementById("item_tax_struct_info"+index).value = document.getElementById("new_item_tax_struct_info"+index).value;
			document.getElementById("item_tax_struct_nm"+index).value = document.getElementById("new_item_tax_struct_info"+index).value;
	    }
	}
	
	let fields;
	if(document.forms[0].invoice_category.value=="P")
    {
		fields = ["item_qty", "item_price", "item_amt", "item_tax_amt"];
		
		var new_tax_struct_info = document.getElementById("new_item_tax_struct_info"+index).value;
		var new_tax_struct_factor="";
		if(new_tax_struct_info.startsWith("IGST"))
		{
			new_tax_struct_factor=new_tax_struct_info.split(",")[0].split(" ")[1].split("%")[0];
			sub_tax_amt = parseFloat((new_tax_struct_factor*document.getElementById("new_item_amt"+index).value)/100).toFixed(2);
		}
		else
		{
			new_tax_struct_info = new_tax_struct_info.split(", ");
			var tmp_tax=parseFloat("0");
			//for(i=0;i<new_tax_struct_info.length;i++) 
	 		{
	 			tax2=parseFloat(((new_tax_struct_info[0].split(",")[0].split(" ")[1].split("%")[0]*2) * document.getElementById("new_item_amt"+index).value) / 100).toFixed(2);
				tmp_tax=parseFloat(tmp_tax)+parseFloat(Math.round(tax2).toFixed(2));
	 		}
			sub_tax_amt=tmp_tax;
		}
		document.getElementById("new_item_tax_amt"+index).value = parseFloat(Math.round(sub_tax_amt)).toFixed(2);
    }
	else 
	{
		fields = ["item_qty", "item_price", "item_amt"];
	}
	
	fields.forEach(field => {
		
        var mainValue = document.getElementById("inv_"+field+index).value;
        var newValue = document.getElementById("new_"+field+index).value;
        if (mainValue !== "" && newValue !== "") 
        {
        	const diff = parseFloat(newValue) - parseFloat(mainValue);
        	if(field == "item_qty")
        	{
        		document.getElementById(field+index).value = diff;
        	}
        	else
        	{
        		document.getElementById(field+index).value = diff.toFixed(2);
        	}
        	
        }
    });
	
	window.setTimeout(function() 
 	{
    	calculateGrandTotal();
    	calc1();
 	}, 400);
	
}

async function calc1()
{
	var gross = document.forms[0].new_gross_amt.value;
	
	if(gross!="")
	{
		if(document.forms[0].invoice_category.value=="P")
	    {
			var invAmt="";
			if(sub_tax_amt!="")
			{
				invAmt=parseFloat(parseFloat(gross)+parseFloat(document.forms[0].new_tax_amt.value)).toFixed(2);
			}
			document.forms[0].new_net_payable.value=parseFloat(Math.round(invAmt)).toFixed(2);
	    }
		else
		{
			var new_tax_struct_info=document.forms[0].new_tax_struct_info.value;
			document.forms[0].tax_struct_info.value=new_tax_struct_info;
			var new_tax_struct_factor="";
			if(new_tax_struct_info.startsWith("IGST"))
			{
				new_tax_struct_factor=new_tax_struct_info.split(",")[0].split(" ")[1].split("%")[0];
				sub_tax_amt = parseFloat((new_tax_struct_factor*gross)/100).toFixed(2);
			}
			else
			{
				new_tax_struct_info = new_tax_struct_info.split(", ");
				var tmp_tax=parseFloat("0");
		 		{
		 			tax2=parseFloat(((new_tax_struct_info[0].split(",")[0].split(" ")[1].split("%")[0]*2) * gross) / 100).toFixed(2);
					tmp_tax=parseFloat(tmp_tax)+parseFloat(Math.round(tax2).toFixed(2));
					
		 		}
				sub_tax_amt=tmp_tax;
			}
			
			document.forms[0].tax_factor.value=new_tax_struct_factor;
			document.forms[0].new_tax_amt.value=parseFloat(Math.round(sub_tax_amt)).toFixed(2);
			
			var invAmt="";
			if(sub_tax_amt!="")
			{
				invAmt=parseFloat(parseFloat(gross)+parseFloat(sub_tax_amt)).toFixed(2);
			}
			document.forms[0].new_net_payable.value=parseFloat(Math.round(invAmt)).toFixed(2);
		}
	}
	
	window.setTimeout(function() 
	{
		difference();
	}, 400); 

}


function difference()
{

	const fields1 = [
        "gross_amt", "tax_amt", "net_payable"
    ];
	
	fields1.forEach(field => {
		
		var mainValue = document.getElementsByName("main_"+field)[0].value;
	    var newValue = document.getElementsByName("new_"+field)[0].value;
        if (mainValue !== "" && newValue !== "") 
        {
        	const diff = parseFloat(newValue) - parseFloat(mainValue);
         	document.getElementsByName(field)[0].value = diff.toFixed(2);
        }
    });
	
	if(document.forms[0].invoice_category.value=="S")
    {
		var tmp_grs_amt= parseFloat(document.getElementsByName("gross_amt")[0].value);
		var tmp_tax_amt= parseFloat(document.getElementsByName("tax_amt")[0].value);
		
		if(document.getElementById("criteria_TAXP").checked)
		{
			if(tmp_tax_amt%2!=0)
			{
				tmp_tax_amt+=1;
				document.getElementsByName("tax_amt")[0].value=tmp_tax_amt.toFixed(2);
				document.getElementsByName("net_payable")[0].value=parseFloat(tmp_tax_amt+tmp_grs_amt).toFixed(2);
			}
		}
		else 
		{
			var tot_tax = parseFloat("0");
			var new_tax_struct_factor="";
			if(new_tax_struct_info.startsWith("IGST"))
			{
				new_tax_struct_factor=new_tax_struct_info.split(",")[0].split(" ")[1].split("%")[0];
				tot_tax = Math.round(parseFloat((new_tax_struct_factor*tmp_grs_amt)/100).toFixed(2)).toFixed(2);
			}
			else //CGST,SGST
			{
				new_tax_struct_info = new_tax_struct_info.split(", ");
				//for(i=0;i<new_tax_struct_info.length;i++)  //need to recheck this part when sub tax table is in place
		 		{
					var temp_tax1 = parseFloat(((new_tax_struct_info[0].split(",")[0].split(" ")[1].split("%")[0]) * tmp_grs_amt) / 100).toFixed(2);
					temp_tax1=Math.round(temp_tax1);
					tot_tax=temp_tax1*2;
		 		}
			}
			document.getElementsByName("tax_amt")[0].value=tot_tax.toFixed(2);
			document.getElementsByName("net_payable")[0].value=parseFloat(tot_tax+tmp_grs_amt).toFixed(2);
		}
    }
}
 
var newWindow;
function doSubmit()
{	
    var supp_cd = document.forms[0].supplier_cd.value;
    var vendor_cd = document.forms[0].vendor_cd.value;
    var period_start_dt = document.forms[0].period_start_dt.value;
    var period_end_dt = document.forms[0].period_end_dt.value;
    var sel_inv_no = document.forms[0].sel_inv_no.value;
    var invoice_type = document.forms[0].invoice_type.value;
    
    var invoice_dt = document.forms[0].invoice_dt.value;
	var invoice_due_dt = document.forms[0].invoice_due_dt.value;
	var gross_amt = document.forms[0].gross_amt.value;
	var tax_amt = document.forms[0].tax_amt.value;
	var net_payable = document.forms[0].net_payable.value;
	
	var criteria = document.getElementsByName("criteria");
	var cr_dr_type = document.forms[0].cr_dr_type.value;
	var operation = document.forms[0].operation.value;
	
	var remark1 = document.forms[0].remark1.value;
    
    var msg="";
	var flag=true;
	
	if(cr_dr_type.trim() === "") 
    {
        msg += "Select CR/DR Type!\n";
        flag = false;
    }
	if(invoice_type.trim() === "") 
    {
        msg += "Select Invoice Type!\n";
        flag = false;
    }
    if(supp_cd.trim() === "") 
    {
        msg += "Missing Supplier Code!\n";
        flag = false;
    }
    if(vendor_cd.trim() === "") 
    {
        msg += "Missing Vendor Code!\n";
        flag = false;
    }
   
    
    var criChkCnt=parseInt("0");
    if(criteria.length > 0)
    {
    	for(var i=0; i<criteria.length; i++)
    	{
    		var chngin=criteria[i].value;
    		
    		if(criteria[i].checked)
    		{
    			criChkCnt++;
    		}
    	}
    }
    
    if(criChkCnt==0)
    {
    	msg += "Please select atleast ONE(1) Invoice Criteria!\n";
        flag = false;
    }
	
	if(trim(invoice_dt)=="")
	{
		msg+="Enter Invoice Date!\n";
		flag=false;
	}
	
	if(trim(cr_dr_type)!="CR")
	{
		if(trim(invoice_due_dt)=="")
		{
			msg+="Enter Invoice Due Date!\n";
			flag=false;
		}	
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
	if(trim(tax_amt)=="")
	{
		msg+="Tax Amount missing!\n";
		flag=false;
	}
	if(trim(net_payable)=="")
	{
		msg+="Net Payable missing!\n";
		flag=false;
	}
	else
	{
		if(parseFloat(net_payable) > 0 && cr_dr_type=="CR")
		{
			msg+="You are supose to generate Debit Note but you have selected Credit Note..Please change the Invoice Type!\n";
			flag=false;
		}
		else if(parseFloat(net_payable) < 0 && cr_dr_type=="DR")
		{
			msg+="You are supose to generate Credit Note but you have selected Debit Note..Please change the Invoice Type!\n";
			flag=false;
		}
			
	}
	
	if(flag)
	{
		
		var a=confirm("Do you want to Submit Credit/Debit Note?");
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

function Do_close(msg,msg_type,accroid)
{
	window.opener.refershPar(msg,msg_type,accroid);
	window.close();
}

function openTaxStructMst(supp_state,vend_state)
{
	var type="S";
	
	if(type=="")
	{
		alert("Select Invoice Category!")	
	}
	else
	{
		
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

function openTaxStructMst1(supp_state,vend_state,index)
{
	var type="";
	
	if(document.forms[0].invoice_category.value != "")
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
			newWindow = window.open("frm_other_inv_tax_structure_list.jsp?type="+type+"&index="+index+"&supp_state="+supp_state+"&vend_state="+vend_state,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_other_inv_tax_structure_list.jsp?type="+type+"&index="+index+"&supp_state="+supp_state+"&vend_state="+vend_state,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function setTaxStructDetail(tax_struct_cd,tax_struct_nm,tax_struct_eff_dt)
{
	var info = "Tax Config ("+tax_struct_nm+")";
	document.forms[0].tax_config_btn.value=info;
	document.forms[0].new_tax_struct_info.value=tax_struct_nm;
	document.forms[0].new_tax_struct_cd.value=tax_struct_cd;
	document.forms[0].new_tax_dt.value=tax_struct_eff_dt;
	calc1();

}
function setTaxStructDetail1(tax_struct_cd,tax_struct_nm,tax_struct_eff_dt,index)
{
	document.getElementById("new_item_tax_struct_nm" + index).value=tax_struct_nm
	document.getElementById("new_item_tax_struct_info" + index).value = tax_struct_nm;
	document.getElementById("new_item_tax_cd" + index).value=tax_struct_cd
	document.getElementById("new_item_tax_dt" + index).value=tax_struct_eff_dt
	calc(index);
}

function refreshParent(accroid,msg,msg_type)
{
	window.opener.refershPar(accroid,msg,msg_type);
	window.close();
}
function printPDF(comp_cd,fin_year,inv_seq,cr_dr,sel_inv_no)
{
	var supp_cd = document.forms[0].supplier_cd.value;
	var is_print="0";
	var inv_type="SFA";
	
	var url = "../other_invoice/pdf_other_invoice_cr_dr.jsp?comp_cd="+comp_cd+"&is_print="+is_print+"&fin_year="+fin_year+
		"&inv_type="+inv_type+"&supp_cd="+supp_cd+"&inv_seq="+inv_seq+"&cr_dr_type="+cr_dr+"&sel_inv_no="+sel_inv_no;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"PDF Other Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"PDF Other Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.other_invoice.DataBean_Other_Invoice" id="other_inv" scope="request"></jsp:useBean>
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

String operation=request.getParameter("operation")==null?"INSERT":request.getParameter("operation");
String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
String sel_inv_no = request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String cr_dr_type = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
if(month.length() == 1)
{
	month="0"+month; 
}
String p_start_dt = utildate.getFirstDateOfMonth(month, year);
String p_end_dt = utildate.getLastDateOfMonth(month, year);
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
String type = request.getParameter("type")==null?"":request.getParameter("type");

other_inv.setCallFlag("SFA_CRDR_LIST");
other_inv.setComp_cd(owner_cd);
other_inv.setMonth(month);
other_inv.setYear(year);
other_inv.setInv_type(invoice_type);
other_inv.setSel_inv_no(sel_inv_no);
other_inv.setCr_dr_type(cr_dr_type);
other_inv.setFin_yr(financial_year);
other_inv.setInv_seq(invoice_seq);
other_inv.setOpration(operation);
other_inv.init();

Vector VINVOICE_TYPE = other_inv.getVINVOICE_TYPE();
Vector VINVOICE_TYPE_NM = other_inv.getVINVOICE_TYPE_NM();
Vector VSEL_INVOICE_NO = other_inv.getVSEL_INVOICE_NO();
Vector VCRITERIA_FLAG = other_inv.getVCRITERIA_FLAG();
Vector VCRITERIA_NAME = other_inv.getVCRITERIA_NAME();
Vector VCRITERIA_HIDE = other_inv.getVCRITERIA_HIDE();
Vector IS_CRDR_SUBMITTED = other_inv.getIS_CRDR_SUBMITTED();

Vector VITEM_DES = other_inv.getVITEM_DES();
Vector VSAC_VAL = other_inv.getVSAC_VAL();
Vector VUOM_NO = other_inv.getVUOM_NO();

Vector VMAIN_QTY = other_inv.getVMAIN_QTY();
Vector VMAIN_PRICE = other_inv.getVMAIN_PRICE();
Vector VMAIN_ITEM_AMT = other_inv.getVMAIN_ITEM_AMT();
Vector VMAIN_TAX_STRUCT_DESC = other_inv.getVMAIN_TAX_STRUCT_DESC();
Vector VMAIN_TAX_STRUCT_APP_DT = other_inv.getVMAIN_TAX_STRUCT_APP_DT();
Vector VMAIN_TAX_STRUCTURE_CD = other_inv.getVMAIN_TAX_STRUCTURE_CD();
Vector VMAIN_TAX_AMT = other_inv.getVMAIN_TAX_AMT();

Vector VNEW_QTY = other_inv.getVNEW_QTY();
Vector VNEW_PRICE = other_inv.getVNEW_PRICE();
Vector VNEW_ITEM_AMT = other_inv.getVNEW_ITEM_AMT();
Vector VNEW_TAX_STRUCT_DESC = other_inv.getVNEW_TAX_STRUCT_DESC();
Vector VNEW_TAX_STRUCT_APP_DT = other_inv.getVNEW_TAX_STRUCT_APP_DT();
Vector VNEW_TAX_STRUCTURE_CD = other_inv.getVNEW_TAX_STRUCTURE_CD();
Vector VNEW_TAX_AMT = other_inv.getVNEW_TAX_AMT();

Vector VQTY = other_inv.getVQTY();
Vector VPRICE = other_inv.getVPRICE();
Vector VITEM_AMT = other_inv.getVITEM_AMT();
Vector VTAX_STRUCTURE_CD = other_inv.getVTAX_STRUCTURE_CD();
Vector VTAX_STRUCTURE_DESC = other_inv.getVTAX_STRUCTURE_DESC();
Vector VTAX_STRUCT_APP_DT = other_inv.getVTAX_STRUCT_APP_DT();
Vector VTAX_AMT = other_inv.getVTAX_AMT();

String supp_nm = other_inv.getSupp_nm();
String supp_cd = other_inv.getSupp_cd();
String vendor_nm = other_inv.getVendor_name();
String vendor_cd = other_inv.getVendor_cd();
String supp_state=other_inv.getSupplier_State();
String vend_state=other_inv.getVendor_State();
String main_invoice_dt = other_inv.getMain_invoice_dt();
String main_invoice_due_dt = other_inv.getMain_invoice_due_dt();
String price_cd = other_inv.getPrice_cd();
String price_cd_nm = other_inv.getPrice_cd_nm();
String main_gross_amt = other_inv.getMain_gross_amt();
String main_tax_amt = other_inv.getMain_tax_amt();
String main_net_payable = other_inv.getMain_net_payable();
String invoice_raised_in = other_inv.getInvoice_raised_in();
String invoice_raised_in_nm = other_inv.getInvoice_raised_in_nm();
String main_tax_struct_info = other_inv.getMain_tax_struct_info();
String main_tax_struct_cd = other_inv.getMain_tax_struct_cd();
String main_tax_struct_dt = other_inv.getMain_tax_struct_dt();

String crdr_invoice_dt = other_inv.getCrdr_invoice_dt();
String crdr_invoice_due_dt = other_inv.getCrdr_invoice_due_dt();
String new_gross_amt = other_inv.getNew_gross_amt();
String crdr_gross_amt = other_inv.getGross_amt();
String new_tax_amt = other_inv.getNew_tax_amt();
String crdr_tax_amt = other_inv.getTax_amt();
String new_net_payable = other_inv.getNew_net_payable();
String crdr_net_payable = other_inv.getNet_payable();
String new_tax_struct_cd = other_inv.getNew_tax_struct_cd();
String new_tax_struct_info = other_inv.getNew_tax_struct_info();
String new_tax_struct_dt=other_inv.getNew_tax_struct_dt();
String tax_struct_cd = other_inv.getTax_struct_cd();
String tax_struct_info = other_inv.getTax_struct_info();
String period_start_dt = other_inv.getPeriod_start_dt();
String period_end_dt = other_inv.getPeriod_end_dt();
String crdr_remark = other_inv.getCrdr_remark();
String financial_yr = other_inv.getFinancial_year();
String criteri_formula=other_inv.getCriteri_formula();
String invoice_category=other_inv.getInvoice_category();

if(crdr_invoice_dt.equals(""))
{
	crdr_invoice_dt=sysdate;
}
if(period_start_dt.equals(""))
{
	period_start_dt=p_start_dt;
}
if(period_end_dt.equals(""))
{
	period_end_dt=p_end_dt;
}

%>
<body onload="initCheckedRows(); <%if(!msg.equals("")){%>refreshParent('<%=accroid%>','<%=msg%>','<%=msg_type%>');<%}%>
<%if(!criteri_formula.equals("")) {%>selectCriteria('<%=criteri_formula%>');<%}%>
<%if(operation.equals("MODIFY")){%>changeTblhed('<%=cr_dr_type%>')<%} %>"
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
					    	Other Invoice Credit/Debit Detail
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row" <%if(operation.equals("MODIFY")){%>style="pointer-events: none;"<%}%>>
						<div class="col-sm-6 col-xs-2 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Month/Year</b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="month" onchange="refresh('<%=operation%>');">
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
					  				<select class="form-select form-select-sm" name="year" onchange="refresh('<%=operation%>');">
					  					<%for(int i=(currentYear+1); i > filter_start_year;i--){ %>
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
					    			<label class="form-label"><b>Invoice Type</b></label>
					  			</div>
								<div class="col">  
									<select class="form-select form-select-sm" name="invoice_type" onchange="refresh('<%=operation%>');" style="pointer-events:none;">
										<option value="">--Select--</option>
										<%for(int i=0;i<VINVOICE_TYPE.size();i++){ %>
										<option value="<%=VINVOICE_TYPE.elementAt(i)%>"><%=VINVOICE_TYPE_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].invoice_type.value="<%=invoice_type%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-xs-2 col-md-4">	
							<div class="form-group row">
								<div class="col-auto">
						    		<label class="form-label"><b>Invoice No</b></label>
						  		</div>				  	
								<div class="col"> 
									<select class="form-select form-select-sm" name="sel_inv_no" onchange="refresh('<%=operation%>');">
										<option value="">--Select--</option>
										<%for(int i=0;i<VSEL_INVOICE_NO.size();i++){ %>
										<option value="<%=VSEL_INVOICE_NO.elementAt(i)%>"><%=VSEL_INVOICE_NO.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].sel_inv_no.value="<%=sel_inv_no%>"</script>								
								</div>
							</div>	
						</div>	
					  	<div class="col-sm-1 col-xs-1 col-md-1"></div>
					</div>
				</div>
				<%if(!sel_inv_no.equals("")) {%>
				<div class="card-body cdbody">
					<div class="row m-b-5"> <%-- <%if(operation.equals("MODIFY")){%>style="pointer-events: none;"<%}%>> --%>
						<div class="col-sm-6 col-xs-2 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>CR/DR Type</b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="cr_dr_type" onchange="changeTblhed(this.value);"> <%-- onchange="refresh('<%=operation%>');"> --%>
					  					<option value="">--Select--</option>
										<option value="CR">Credit Note</option>
										<option value="DR">Debit Note</option>
									</select>
									<script>document.forms[0].cr_dr_type.value="<%=cr_dr_type%>"</script>
								</div>
					  		</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Invoice Criteria</b></label>
					  			</div>
					  			<%for(int i=0;i<VCRITERIA_FLAG.size();i++){ %>
					  			<div class="col-auto" <%if(VCRITERIA_HIDE.elementAt(i).equals("Y")) {%>style="display:none;"<%} %>>
 				  					<label class="form-label">
 				  						<input type="checkbox" class="form-check-input" name="criteria" id="criteria_<%=VCRITERIA_FLAG.elementAt(i)%>" 
 				  							value="<%=VCRITERIA_FLAG.elementAt(i)%>" 
 				  							onclick="criteriFormula();<%if(invoice_category.equals("S") && VCRITERIA_FLAG.elementAt(i).equals("TAXP")) {%>
 				  							toggleAllRows(this);<%}%>setTimeout(()=>enableNewValues(this),150);">
 				  						<span style="background:#e6e6ff;">&nbsp;<%=VCRITERIA_NAME.elementAt(i) %></span>
 				  					</label>
				  				</div>
				  				<%} %>
					  		</div>
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
												<th>Supplier Name</th>
												<th>Vendor Name</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td align="center"><%=supp_nm %>
													<input type="hidden" name="supplier_cd" value="<%=supp_cd%>">
													<input type="hidden" name="supplier_nm" value="<%=supp_nm%>">
													<input type="hidden" name="supp_state" value="<%=supp_state%>">
													<input type="hidden" name="invoice_category" value="<%=invoice_category%>">
												</td>
												<td align="center"><%=vendor_nm %>
													<input type="hidden" name="vendor_cd" value="<%=vendor_cd%>">
													<input type="hidden" name="vendor_nm" value="<%=vendor_nm%>">
													<input type="hidden" name="vend_state" value="<%=vend_state%>">
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					<%if(invoice_category.equals("P")) {%>
						<div class="card-body cdbody">
							<div class="row m-b-5">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<div class="table-responsive">
										<table class="table table-bordered table-hover">
											<thead>
												<tr>
													<th><input type="checkbox" class="form-check-input" name="chk_all" id="chk_all" onclick="toggleAllRows(this);"></th>
													<th>HSN/SAC CODE</th>
													<th>Item Description</th>
													<th>Unit</th>
													<th>Quantity</th>
													<th>Rate</th>
													<th>Amount</th>
													<th>Tax Rate</th>
													<th>Tax Amount</th>
												</tr>
											</thead>
											<tbody id="cargo_itemTab">
												<%for(int i=0;i<VMAIN_QTY.size();i++) { %>
													<tr>
														<td align="center"><input type="checkbox"  class="form-check-input" name="chk" id="chk<%=i%>" onclick="toggleExpandRow(this,<%=i%>);" <%if(IS_CRDR_SUBMITTED.elementAt(i).equals("Y")) {%>checked<%} %>></td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="item_hsn" id="item_hsn<%=i%>" value="<%=VSAC_VAL.elementAt(i) %>" maxlength='20' style="width:100px; text-align:right;" readOnly disabled>
														</td>
														<td align="center">
															<textarea class="form-control form-control-sm" name="item_des" id="item_des<%=i%>" maxLength="80" cols="75" rows="1" style="width:250px;" readOnly disabled><%=VITEM_DES.elementAt(i)%></textarea>
														</td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="item_unit" id="item_unit<%=i%>" value="<%=VUOM_NO.elementAt(i) %>" maxlength='20' style="width:100px; text-align:right;" readOnly disabled>
														</td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="item_qty" id="item_qty<%=i%>" value="<%=VQTY.elementAt(i) %>" maxlength='20' style="width:100px; text-align:right;" readOnly disabled>
														</td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="item_price" id="item_price<%=i%>" value="<%=VPRICE.elementAt(i) %>" maxlength='20' style="width:100px; text-align:right;" readOnly disabled>
														</td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="item_amt" id="item_amt<%=i%>" value="<%=VITEM_AMT.elementAt(i) %>" maxlength='20' style="width:100px; text-align:right;" readOnly disabled>
														</td>
														<td align="center">
															<div style="display:flex; justify-content:center; align-items:center; gap:6px; width:100%; margin:auto;">
																<input type="hidden" name="item_tax_cd" id="item_tax_cd<%=i%>" value="<%=VTAX_STRUCTURE_CD.elementAt(i)%>">
																<input type="hidden" name="item_tax_dt" id="item_tax_dt<%=i%>" value="<%=VTAX_STRUCT_APP_DT.elementAt(i)%>">
																
																<input type="text" class="form-control form-control-sm" name="item_tax_struct_info" id="item_tax_struct_info<%=i%>" value="<%=VTAX_STRUCTURE_DESC.elementAt(i)%>" style="width:130px;font-weight: bold;text-align:left;" readOnly disabled>
																<input type="hidden" name="item_tax_struct_nm" id="item_tax_struct_nm<%=i%>" value="<%=VTAX_STRUCTURE_DESC.elementAt(i)%>">	
															</div>	
														</td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="item_tax_amt" id="item_tax_amt<%=i%>" value="<%=VTAX_AMT.elementAt(i)%>" maxlength='20' style="width:100px;text-align:right;" readOnly disabled>
														</td>
													</tr>
												<%}%>
											</tbody>
										</table>
									</div>
								</div>
						    </div>
						</div>
					<%} else {%>
						<div class="card-body cdbody">
							<div class="row m-b-5">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<div class="table-responsive">
										<table class="table table-bordered table-hover">
											<thead>
												<tr>
													<th><input type="checkbox" class="form-check-input" name="chk_all" id="chk_all" onclick="toggleAllRows(this);"></th>
													<th>HSN/SAC CODE</th>
													<th>Item Description</th>
													<th>Unit</th>
													<th>Quantity</th>
													<th>Rate</th>
													<th>Amount</th>
												</tr>
											</thead>
											<tbody id="cargo_itemTab">
												<%for(int i=0;i<VMAIN_QTY.size();i++) { %>
													<tr>
														<td align="center"><input type="checkbox"  class="form-check-input" name="chk" id="chk<%=i%>" onclick="toggleExpandRow(this,<%=i%>);" <%if(IS_CRDR_SUBMITTED.elementAt(i).equals("Y")) {%>checked<%} %>></td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="item_hsn" id="item_hsn<%=i%>" value="<%=VSAC_VAL.elementAt(i) %>" maxlength='20' style="width:100px; text-align:right;" readOnly disabled>
														</td>
														<td align="center">
															<textarea class="form-control form-control-sm" name="item_des" id="item_des<%=i%>" maxLength="80" cols="75" rows="1" style="width:250px;" readOnly disabled><%=VITEM_DES.elementAt(i)%></textarea>
														</td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="item_unit" id="item_unit<%=i%>" value="<%=VUOM_NO.elementAt(i) %>" maxlength='20' style="width:100px; text-align:right;" readOnly disabled>
														</td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="item_qty" id="item_qty<%=i%>" value="<%-- <%=VQTY_MMBTU.elementAt(i) %> --%>" maxlength='20' style="width:100px; text-align:right;" readOnly disabled>
														</td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="item_price" id="item_price<%=i%>" value="<%-- <%=VRATE.elementAt(i) %> --%>" maxlength='20' style="width:100px; text-align:right;" readOnly disabled>
														</td>
														<td align="center">
															<input type="text" class="form-control form-control-sm" name="item_amt" id="item_amt<%=i%>" value="<%-- <%=VCARGO_AMT.elementAt(i) %> --%>" maxlength='20' style="width:100px; text-align:right;" readOnly disabled>
														</td>
													</tr>
												<%}%>
											</tbody>
										</table>
									</div>
								</div>
						    </div>
						</div>
					<%} %>
					<div class="card-body cdbody">
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="table-responsive">
									<table class="table table-bordered table-hover">
										<thead>
											<tr>
												<th></th>
												<th><%=sel_inv_no%> [A]</th>
												<th>Revised Invoice [B]</th>
												<th id="th_invType"><%if(invoice_type.equals("CR")) {%>Credit Note<%}else if(invoice_type.equals("DR")){ %>Debit Note<%} %> [B-A]</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td><b>Invoice Date<span class="s-red">*</span></b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="main_invoice_dt" value="<%=main_invoice_dt%>" maxLength="10" autocomplete="off" disabled readonly>
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      						</div>
						      						</div>
												</td>
												<td align="center"></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="invoice_dt" value="<%=crdr_invoice_dt%>" maxLength="10" 
								      						onblur="validateDate(this);" 
								      						onchange="validateDate(this);" autocomplete="off">
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      						</div>
							      						<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
							      						<input type="hidden" name="exist_invoice_seq" value="<%=invoice_seq%>">
							      						<input type="hidden" name="financial_year" value="<%=financial_year%>">
						      						</div>
												</td>
											</tr>
											<tr>
												<td><b>Invoice Due Date<span class="s-red">*</span></b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="main_invoice_due_dt" value="<%=main_invoice_due_dt%>" maxLength="10" autocomplete="off" disabled readonly>
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      						</div>
							      					</div>
												</td>
												<td align="center"></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="invoice_due_dt" value="<%=crdr_invoice_due_dt%>" maxLength="10" 
								      						onblur="validateDate(this);" 
								      						onchange="validateDate(this);" autocomplete="off">
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      						</div>
							      					</div>
												</td>
											</tr>
											<tr>
												<td><b>Gross Amount</b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_gross_amt" value="<%=main_gross_amt%>" readOnly>
															<span class="input-group-text"><%=price_cd_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_gross_amt" value="<%=new_gross_amt%>" 
															onblur="negNumber(this);" readOnly>
															<span class="input-group-text"><%=price_cd_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="gross_amt" value="<%=crdr_gross_amt%>" readOnly>
															<span class="input-group-text"><%=price_cd_nm%></span>
														</div>
													</div>
												</td>
											</tr>
											<tr>
											<%if(invoice_category.equals("S")) {%>
												<td><b>Tax (<%=main_tax_struct_info%>)</b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_tax_amt" value="<%=main_tax_amt%>" title="<%//=main_tax_info%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
														<input type="hidden" name="main_tax_struct_cd" value="<%=main_tax_struct_cd%>">
														<input type="hidden" name="main_tax_dt" value="<%=main_tax_struct_dt%>">
														<input type="hidden" name="main_tax_struct_info" value="<%=main_tax_struct_info%>">
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div>
															<input type="button" class="btn btn-sm config_btn" name="tax_config_btn" value="Tax Config (<%=new_tax_struct_info%>)" onclick="openTaxStructMst('<%=supp_state %>','<%=vend_state %>');" disabled>
														</div>
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_tax_amt" value="<%=new_tax_amt%>" title="" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
														<input type="hidden" name="new_tax_struct_cd" value="<%=new_tax_struct_cd%>">
														<input type="hidden" name="tax_factor" value="">
														<input type="hidden" name="new_tax_struct_info" value="<%=new_tax_struct_info%>">
														<input type="hidden" name="new_tax_dt" value="<%=new_tax_struct_dt%>">
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="tax_amt" value="<%=crdr_tax_amt%>" title="" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
														<input type="hidden" name="tax_struct_cd" value="<%=tax_struct_cd%>">
														<input type="hidden" name="tax_struct_info" value="<%=tax_struct_info%>">
													</div>
												</td>
											<%} else {%>
												<td><b>Total Tax Amount</b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_tax_amt" value="<%=main_tax_amt%>" title="<%//=main_tax_info%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_tax_amt" value="<%=new_tax_amt%>" title="" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="tax_amt" value="<%=crdr_tax_amt%>" title="" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
											<%} %>
											</tr>
											<tr>
												<td><b>Net Payable</b></td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="main_net_payable" value="<%=main_net_payable%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="new_net_payable" value="<%=new_net_payable%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
												<td align="center">
													<div style="width:200px;">
														<div class="input-group input-group-sm" >
															<input type="text" class="form-control form-control-sm" name="net_payable" value="<%=crdr_net_payable%>" readOnly>
															<span class="input-group-text"><%=invoice_raised_in_nm%></span>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td><b>Remark-1</b></td>
												<td align="center" colspan="3">
													<textarea class="form-control form-control-sm" rows="3" cols="75" name="remark1"><%=crdr_remark%></textarea>
												</td>
											</tr>
										</tbody>
									</table>
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
							        <span class="fa-stack fa-lg" title="Print Temp PDF" onclick="printPDF('<%=owner_cd%>','<%=financial_year%>','<%=invoice_seq%>','<%=cr_dr_type%>','<%=sel_inv_no%>');" style="position: relative;pointer-events: auto;<%if(operation.equals("INSERT")){ %>display:none;<%} %>">
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
				<%} %>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="GENERATE_SCRAP_CR_DR">
<input type="hidden" name="opration" value="<%=operation%>">
<input type="hidden" name="operation" value="<%=operation%>">
<input type="hidden" name="period_start_dt" value="<%=period_start_dt%>">
<input type="hidden" name="period_end_dt" value="<%=period_end_dt%>">

<input type="hidden" name="prev_month" value="<%=month%>">
<input type="hidden" name="prev_year" value="<%=year%>">
<input type="hidden" name="accroid" value="<%=accroid%>">
<input type="hidden" name="criteri_formula" value="<%=criteri_formula%>">
<input type="hidden" name="invoice_raised_in" value="<%=invoice_raised_in%>">
<input type="hidden" name="exist_fin_yr" value="<%=financial_yr%>">
<input type="hidden" name="price_cd" value="<%=price_cd%>">

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
var invoiceVec = [];
</script>

 <script>
 
var VMAIN_QTY_JS = [];
var VMAIN_PRICE_JS = [];
var VMAIN_ITEM_AMT_JS = [];
var VNEW_QTY_JS = [];
var VNEW_PRICE_JS = [];
var VNEW_ITEM_AMT_JS = [];

if(document.forms[0].invoice_category.value=="P")
{
	var VMAIN_TAX_STRUCT_DESC_JS = [];
	var VMAIN_TAX_STRUCT_APP_DT_JS = [];
	var VMAIN_TAX_STRUCTURE_CD_JS = [];
	var VMAIN_TAX_AMT_JS = [];
	
	var VNEW_TAX_STRUCT_DESC_JS = [];
	var VNEW_TAX_STRUCT_APP_DT_JS = [];
	var VNEW_TAX_STRUCTURE_CD_JS = [];
	var VNEW_TAX_AMT_JS = [];
}
</script>
<%
for(int i=0; i<VMAIN_QTY.size(); i++) {
%>
<script>
VMAIN_QTY_JS[<%=i%>] = "<%=VMAIN_QTY.get(i)%>";
VMAIN_PRICE_JS[<%=i%>] = "<%=VMAIN_PRICE.get(i)%>";
VMAIN_ITEM_AMT_JS[<%=i%>] = "<%=VMAIN_ITEM_AMT.get(i)%>";

VNEW_QTY_JS[<%=i%>] = "<%=VNEW_QTY.get(i)%>";
VNEW_PRICE_JS[<%=i%>] = "<%=VNEW_PRICE.get(i)%>";
VNEW_ITEM_AMT_JS[<%=i%>] = "<%=VNEW_ITEM_AMT.get(i)%>";

if(document.forms[0].invoice_category.value=="P")
{
	VMAIN_TAX_STRUCT_DESC_JS[<%=i%>] = "<%=VMAIN_TAX_STRUCT_DESC.get(i)%>";
	VMAIN_TAX_STRUCT_APP_DT_JS[<%=i%>] = "<%=VMAIN_TAX_STRUCT_APP_DT.get(i)%>";
	VMAIN_TAX_STRUCTURE_CD_JS[<%=i%>] = "<%=VMAIN_TAX_STRUCTURE_CD.get(i)%>";
	VMAIN_TAX_AMT_JS[<%=i%>] = "<%=VMAIN_TAX_AMT.get(i)%>";
	
	VNEW_TAX_STRUCT_DESC_JS[<%=i%>] = "<%=VNEW_TAX_STRUCT_DESC.get(i)%>";
	VNEW_TAX_STRUCT_APP_DT_JS[<%=i%>] = "<%=VNEW_TAX_STRUCT_APP_DT.get(i)%>";
	VNEW_TAX_STRUCTURE_CD_JS[<%=i%>] = "<%=VNEW_TAX_STRUCTURE_CD.get(i)%>";
	VNEW_TAX_AMT_JS[<%=i%>] = "<%=VNEW_TAX_AMT.get(i)%>";
}


</script>
<%
}
%>

<script>
var IS_CRDR_SUBMITTED_JS = [];
</script>

<%
for(int i=0; i<IS_CRDR_SUBMITTED.size(); i++){
%>
<script>
IS_CRDR_SUBMITTED_JS[<%=i%>] = "<%=IS_CRDR_SUBMITTED.get(i)%>";
</script>
<%
}
%>

<script>
function toggleAllRows(headerCb) 
{
    var operation = document.forms[0].operation.value;
    var chk = document.getElementsByName("chk");

    for (var i = 0; i < chk.length; i++) 
    {
        // MODIFY mode protection
        if (operation === "MODIFY" && !headerCb.checked) 
        {
            // If already submitted → DO NOT uncheck
            if (IS_CRDR_SUBMITTED_JS[i] === "Y") 
            {
                chk[i].checked = true;
                continue;
            }
        }

        // Normal behavior
        if (chk[i].checked !== headerCb.checked) 
        {
            chk[i].checked = headerCb.checked;
            chk[i].onclick();
        }
    }
}

function toggleExpandRow(cb, idx) 
{
	var criteri_formula = document.forms[0].criteri_formula.value;
	var supp_state = document.forms[0].supp_state.value;
	var vend_state = document.forms[0].vend_state.value;
	
	if (cb.checked) 
	{
		document.getElementById("item_hsn"+idx).disabled=false;
		document.getElementById("item_des"+idx).disabled=false;
		document.getElementById("item_unit"+idx).disabled=false;
		document.getElementById("item_qty"+idx).disabled=false;
		document.getElementById("item_price"+idx).disabled=false;
		document.getElementById("item_amt"+idx).disabled=false;
		
		if(document.forms[0].invoice_category.value=="P")
	    {
			document.getElementById("item_tax_struct_info"+idx).disabled=false;
			document.getElementById("item_tax_amt"+idx).disabled=false;
	    }
	}
	else
	{
		window.setTimeout(function() 
		{
			calc(idx);
	    	calculateGrandTotal();
	    	calc1();
	    	difference();
	    	
		}, 400);
		
		document.getElementById("item_hsn"+idx).disabled=true;
		document.getElementById("item_des"+idx).disabled=true;
		document.getElementById("item_unit"+idx).disabled=true;
		document.getElementById("item_qty"+idx).disabled=true;
		document.getElementById("item_price"+idx).disabled=true;
		document.getElementById("item_amt"+idx).disabled=true;
		
		document.getElementById("item_qty"+idx).value="";
		document.getElementById("item_price"+idx).value="";
		document.getElementById("item_amt"+idx).value="";
		
		if(document.forms[0].invoice_category.value=="P")
	    {
			document.getElementById("item_tax_struct_info"+idx).disabled=true;
			document.getElementById("item_tax_amt"+idx).disabled=true;
			
			document.getElementById("item_tax_struct_info"+idx).value="";
			document.getElementById("item_tax_amt"+idx).value="";
	    }
	} 
	 

    const mainRow = cb.closest("tr");
    const tbody = mainRow.parentNode;

    //const key = mainRow.cells[1].innerText.trim();
    const rowClass = "exp_" + idx;

    // Collapse
    if (!cb.checked) {
        document.querySelectorAll("." + rowClass)
            .forEach(r => r.remove());
        return;
    }

    // Prevent duplicate
    if (document.querySelector("." + rowClass)) return;

    const td = (html = "") => {
        const c = document.createElement("td");
        c.innerHTML = html;
        return c;
    };

 // ---------- Invoice Value (A) ----------
    const rowA = document.createElement("tr");
    rowA.className = rowClass + " sub-row";
    rowA.id = "invRow" + idx;
    
    // ---------- New Value (B) ----------
    const rowB = document.createElement("tr");
    rowB.className = rowClass + " sub-row";
    rowB.id = "newRow" + idx;
    
  	if(document.forms[0].invoice_category.value=="P")
    {
	    rowA.append(
	        td(),
	        td(),
	        td(),
	        td('<div align="right"><b>Invoiced Value (A)</b></div>'),
	        td('<div align="center"><input id="inv_item_qty'+idx+'" name="inv_item_qty" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
	        td('<div align="center"><input id="inv_item_price'+idx+'" name="inv_item_price" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
	        td('<div align="center"><input id="inv_item_amt'+idx+'" name="inv_item_amt" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
	      	td('<div style="display:flex; justify-content:center; align-items:center; gap:6px; width:100%; margin:auto;">' +
	        		'<input type="hidden" name="inv_item_tax_cd" id="inv_item_tax_cd'+idx+'">' +
	        		'<input type="hidden" name="inv_item_tax_dt" id="inv_item_tax_dt'+idx+'">' +
	        		'<input type="hidden" name="inv_item_tax_struct_nm" id="inv_item_tax_struct_nm'+idx+'">' +
	        		'<input type="text" class="form-control form-control-sm" name="inv_item_tax_struct_info" id="inv_item_tax_struct_info'+idx+'" style="width:130px;font-weight: bold;text-align:left;" readonly>' +
	        '</div>'),
	        td('<div align="center"><input id="inv_item_tax_amt'+idx+'" name="inv_item_tax_amt" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
	    );
	    
	    rowB.append(
		        td(),
		        td(),
		        td(),
		        td('<div align="right"><b>New Value (B)</b></div>'),
		        td('<div align="center"><input id="new_item_qty'+idx+'" name="new_item_qty" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly onchange="calc('+idx+');"></div>'),
		        td('<div align="center"><input id="new_item_price'+idx+'" name="new_item_price" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly onchange="calc('+idx+');"></div>'),
		        td('<div align="center"><input id="new_item_amt'+idx+'" name="new_item_amt" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
		      	td('<div align="center">' +
		      			'<input type="button" class="btn btn-sm config_btn" id="new_tax_btn' + idx + '" value="Tax Config" onclick="openTaxStructMst1(\'' + supp_state + '\', \'' + vend_state + '\', \'' + idx + '\');" readonly disabled>' +
		        		'<input type="hidden" name="new_item_tax_cd" id="new_item_tax_cd'+idx+'">' +
		        		'<input type="hidden" name="new_item_tax_dt" id="new_item_tax_dt'+idx+'">' +
		        		'<input type="hidden" name="new_item_tax_struct_nm" id="new_item_tax_struct_nm'+idx+'">' +
		        		'<input type="text" class="form-control form-control-sm" name="new_item_tax_struct_info" id="new_item_tax_struct_info'+idx+'" style="width:130px;font-weight: bold;text-align:left;" readonly>' +
		        '</div>'),
		        td('<div align="center"><input id="new_item_tax_amt'+idx+'" name="new_item_tax_amt" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
		 );
    }
    else
    {
    	rowA.append(
   	        td(),
   	        td(),
   	        td(),
   	        td('<div align="right"><b>Invoiced Value (A)</b></div>'),
   	        td('<div align="center"><input id="inv_item_qty'+idx+'" name="inv_item_qty" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
   	        td('<div align="center"><input id="inv_item_price'+idx+'" name="inv_item_price" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
   	        td('<div align="center"><input id="inv_item_amt'+idx+'" name="inv_item_amt" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
    	 );
    	
    	rowB.append(
       	        td(),
       	        td(),
       	        td(),
       	        td('<div align="right"><b>New Value (B)</b></div>'),
       	        td('<div align="center"><input id="new_item_qty'+idx+'" name="new_item_qty" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly onchange="calc('+idx+');"></div>'),
       	        td('<div align="center"><input id="new_item_price'+idx+'" name="new_item_price" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly onchange="calc('+idx+');"></div>'),
       	        td('<div align="center"><input id="new_item_amt'+idx+'" name="new_item_amt" class="form-control form-control-sm" style="width:100px;text-align:right;" readonly></div>'),
        );
    }

    tbody.insertBefore(rowA, mainRow.nextSibling);
    tbody.insertBefore(rowB, rowA.nextSibling);
    
    document.getElementById("inv_item_qty"+idx).value = VMAIN_QTY_JS[idx];
    document.getElementById("inv_item_price"+idx).value = VMAIN_PRICE_JS[idx];
    document.getElementById("inv_item_amt"+idx).value = VMAIN_ITEM_AMT_JS[idx];

    document.getElementById("new_item_qty"+idx).value = VNEW_QTY_JS[idx];
    document.getElementById("new_item_price"+idx).value = VNEW_PRICE_JS[idx];
    document.getElementById("new_item_amt"+idx).value = VNEW_ITEM_AMT_JS[idx];
    
    if(document.forms[0].invoice_category.value=="P")
    {
    	  document.getElementById("inv_item_tax_cd"+idx).value = VMAIN_TAX_STRUCTURE_CD_JS[idx];
    	  document.getElementById("inv_item_tax_dt"+idx).value = VMAIN_TAX_STRUCT_APP_DT_JS[idx];
    	  document.getElementById("inv_item_tax_struct_nm"+idx).value = VMAIN_TAX_STRUCT_DESC_JS[idx];
    	  document.getElementById("inv_item_tax_struct_info"+idx).value = VMAIN_TAX_STRUCT_DESC_JS[idx];
    	  document.getElementById("inv_item_tax_amt"+idx).value = VMAIN_TAX_AMT_JS[idx];
    	  
    	  document.getElementById("new_item_tax_cd"+idx).value = VNEW_TAX_STRUCTURE_CD_JS[idx];
    	  document.getElementById("new_item_tax_dt"+idx).value = VNEW_TAX_STRUCT_APP_DT_JS[idx];
    	  document.getElementById("new_item_tax_struct_nm"+idx).value = VNEW_TAX_STRUCT_DESC_JS[idx];
    	  document.getElementById("new_item_tax_struct_info"+idx).value = VNEW_TAX_STRUCT_DESC_JS[idx];
    	  document.getElementById("new_item_tax_amt"+idx).value = VNEW_TAX_AMT_JS[idx];
    }
    
    selectCriteria(criteri_formula);
} 

async function initCheckedRows() 
{
    var i = 0;

    while (true) 
    {
        var cb = document.getElementById("chk" + i);
       
        if (!cb) break;

        if (cb.checked) 
        {
            toggleExpandRow(cb, i);
            calc(i)
        }
        i++;
    }
}

function criteriFormula()
{
	var rowCount = <%=VMAIN_QTY.size()%>;
	var criteria = document.getElementsByName("criteria");
	var formula="";
	if(criteria.length > 0)
    {
    	for(var i=0; i<criteria.length; i++)
    	{
    		if(criteria[i].checked)
    		{
    			if(formula=="")
    			{
    				formula=criteria[i].value;
    			}
    			else
    			{
    				formula=formula+"#"+criteria[i].value;
    			}
    		}
    	}
    }
	
	document.forms[0].criteri_formula.value=formula;
}

function selectCriteria(formula)
{
	var criteria = document.getElementsByName("criteria");
	var split_formula = formula.split("#");
	
	for(var i=0; i<split_formula.length; i++)
	{
		var criteriaVal=split_formula[i];
		
		if(criteria.length > 0)
	    {
	    	for(var j=0; j<criteria.length; j++)
	    	{
	    		if(criteria[j].value == criteriaVal)
	    		{
	    			criteria[j].checked=true;
	    			enableNewValues(criteria[j]);
	    		}
	    	}
	    }
	}
}

function calculateGrandTotal()
{
	var rowCount = <%=VMAIN_QTY.size()%>;
    var chk = document.getElementsByName('chk');
    var grandTotal = 0.0;
    var grandTax = 0.0;

    for (var i = 0; i < rowCount; i++)
    {
        if (chk[i] && chk[i].checked)
        {
            var amtFld = document.getElementById('new_item_amt'+i).value;
            
            if (amtFld && amtFld.trim() !== "")
            {
                var val = parseFloat(amtFld);
                if (!isNaN(val))
                {
                    grandTotal += val;
                }
            }
            
            if(document.forms[0].invoice_category.value=="P")
		    {
            	var taxFld = document.getElementById('new_item_tax_amt'+i).value;
            	
            	if (taxFld && taxFld.trim() !== "")
                {
                    var val1 = parseFloat(taxFld);
                    if (!isNaN(val1))
                    {
                    	grandTax += val1;
                    }
                }
		    }
        }
        else 
        {
       		var amtFld = VMAIN_ITEM_AMT_JS[i];
       		
       		if (amtFld && amtFld.trim() !== "")
            {
                var val = parseFloat(amtFld);
                if (!isNaN(val))
                {
                    grandTotal += val;
                }
            }
              
            if(document.forms[0].invoice_category.value=="P")
  		    {
				var taxFld = VMAIN_TAX_AMT_JS[i];
            	
            	if (taxFld && taxFld.trim() !== "")
                {
                    var val1 = parseFloat(taxFld);
                    if (!isNaN(val1))
                    {
                    	grandTax += val1;
                    }
                }
  		    }
        }
    }
    
    document.forms[0].new_gross_amt.value = parseFloat(Math.round(grandTotal)).toFixed(2);
    
    if(document.forms[0].invoice_category.value=="P")
    {
    	document.forms[0].new_tax_amt.value = grandTax.toFixed(2);
    }
} 

function enableNewValues(obj)
{
	var cr_dr_type=document.forms[0].cr_dr_type.value;
	
	if(cr_dr_type=="" || cr_dr_type=="0")
	{
		alert("Plese Select CR/DR Type First!")
		obj.checked=false;
	}
	else
	{	setTimeout(function () 
		{
			var rowCount = <%=VMAIN_QTY.size()%>;
			var chk = document.getElementsByName("chk");
			
			if(obj.value=="QTY")
			{
				for (var j = 0; j < rowCount; j++) 
			    {
					if (chk[j].checked)
				    {
						if(obj.checked)
						{
							if(VNEW_QTY_JS[j]=="")
			    			{
								if(document.getElementById("new_item_qty"+j).value == "")
								{
									document.getElementById("new_item_qty"+j).value = VMAIN_QTY_JS[j];
								    document.getElementById("new_item_price"+j).value = VMAIN_PRICE_JS[j];
								    document.getElementById("new_item_amt"+j).value = VMAIN_ITEM_AMT_JS[j];
								}
							    if(document.forms[0].invoice_category.value=="P")
							    {
							    	document.getElementById("new_item_tax_cd"+j).value = VMAIN_TAX_STRUCTURE_CD_JS[j];
							   		document.getElementById("new_item_tax_dt"+j).value = VMAIN_TAX_STRUCT_APP_DT_JS[j];
							   		document.getElementById("new_item_tax_struct_nm"+j).value = VMAIN_TAX_STRUCT_DESC_JS[j];
							    	document.getElementById("new_item_tax_struct_info"+j).value = VMAIN_TAX_STRUCT_DESC_JS[j];
							    	document.getElementById("new_item_tax_amt"+j).value = VMAIN_TAX_AMT_JS[j];
							    }
			    			}
							 document.getElementById("new_item_qty"+j).readOnly=false;
							window.setTimeout(function() 
							{
								calc(j);
							}, 400);
						}
						else 
						{
							document.getElementById("new_item_qty"+j).readOnly=true;
						}
					}
			    }
				document.forms[0].new_gross_amt.value = document.forms[0].main_gross_amt.value;
				
				if(document.forms[0].invoice_category.value=="P")
				{
					document.forms[0].new_tax_amt.value=document.forms[0].main_tax_amt.value;
				}
				else if(document.forms[0].invoice_category.value=="S")
			    {
					new_tax_struct_info = document.forms[0].main_tax_struct_info.value;
					document.forms[0].new_tax_struct_info.value=new_tax_struct_info;
					document.forms[0].tax_config_btn.value="Tax Config ("+new_tax_struct_info+")";
					document.forms[0].new_tax_struct_cd.value=document.forms[0].main_tax_struct_cd.value;
					document.forms[0].tax_struct_cd.value=document.forms[0].main_tax_struct_cd.value;
					document.forms[0].new_tax_dt.value=document.forms[0].main_tax_dt.value;
					document.forms[0].new_tax_amt.value=document.forms[0].main_tax_amt.value;
			    }
				
				document.forms[0].new_net_payable.value=document.forms[0].main_net_payable.value; 
			}
			if(obj.value=="RATE")
			{
				for (var j = 0; j < rowCount; j++) 
			    {
					if (chk[j].checked)
				    {
						if(obj.checked)
						{
							if(VNEW_QTY_JS[j]=="")
			    			{
								if(document.getElementById("new_item_price"+j).value == "")
								{
									document.getElementById("new_item_qty"+j).value = VMAIN_QTY_JS[j];
								    document.getElementById("new_item_price"+j).value = VMAIN_PRICE_JS[j];
								    document.getElementById("new_item_amt"+j).value = VMAIN_ITEM_AMT_JS[j];
								}
							    if(document.forms[0].invoice_category.value=="P")
							    {
							    	document.getElementById("new_item_tax_cd"+j).value = VMAIN_TAX_STRUCTURE_CD_JS[j];
							   		document.getElementById("new_item_tax_dt"+j).value = VMAIN_TAX_STRUCT_APP_DT_JS[j];
							   		document.getElementById("new_item_tax_struct_nm"+j).value = VMAIN_TAX_STRUCT_DESC_JS[j];
							    	document.getElementById("new_item_tax_struct_info"+j).value = VMAIN_TAX_STRUCT_DESC_JS[j];
							    	document.getElementById("new_item_tax_amt"+j).value = VMAIN_TAX_AMT_JS[j];
							    }
			    			}
							document.getElementById("new_item_price"+j).readOnly=false;
							window.setTimeout(function() 
							{
								calc(j);
								
							}, 400);
						}
						else 
						{
							document.getElementById("new_item_price"+j).readOnly=true;
						}
					}
			    }
				document.forms[0].new_gross_amt.value = document.forms[0].main_gross_amt.value;
				
				if(document.forms[0].invoice_category.value=="P")
				{
					document.forms[0].new_tax_amt.value=document.forms[0].main_tax_amt.value;
				}
				else if(document.forms[0].invoice_category.value=="S")
			    {
					new_tax_struct_info = document.forms[0].main_tax_struct_info.value;
					document.forms[0].new_tax_struct_info.value=new_tax_struct_info;
					document.forms[0].tax_config_btn.value="Tax Config ("+new_tax_struct_info+")";
					document.forms[0].new_tax_struct_cd.value=document.forms[0].main_tax_struct_cd.value;
					document.forms[0].tax_struct_cd.value=document.forms[0].main_tax_struct_cd.value;
					document.forms[0].new_tax_dt.value=document.forms[0].main_tax_dt.value;
					document.forms[0].new_tax_amt.value=document.forms[0].main_tax_amt.value;
			    }
				
				document.forms[0].new_net_payable.value=document.forms[0].main_net_payable.value; 
			}
			if(obj.value=="TAXP")
			{
				for (var j = 0; j < rowCount; j++) 
			    {
					if (chk[j].checked)
				    {
						if(obj.checked)
						{
							if(VNEW_QTY_JS[j]=="")
			    			{
								if(document.forms[0].invoice_category.value=="P")
								{
									if(document.getElementById("new_item_tax_struct_info"+j).value == "")
									{
										document.getElementById("new_item_qty"+j).value = VMAIN_QTY_JS[j];
									    document.getElementById("new_item_price"+j).value = VMAIN_PRICE_JS[j];
									    document.getElementById("new_item_amt"+j).value = VMAIN_ITEM_AMT_JS[j];
								   		document.getElementById("new_item_tax_dt"+j).value = VMAIN_TAX_STRUCT_APP_DT_JS[j];
								   		document.getElementById("new_item_tax_struct_nm"+j).value = VMAIN_TAX_STRUCT_DESC_JS[j];
								    	document.getElementById("new_item_tax_struct_info"+j).value = VMAIN_TAX_STRUCT_DESC_JS[j];
								    	document.getElementById("new_item_tax_amt"+j).value = VMAIN_TAX_AMT_JS[j];
									}
								}
								else
								{
									
									document.getElementById("new_item_qty"+j).value = VMAIN_QTY_JS[j];
								    document.getElementById("new_item_price"+j).value = VMAIN_PRICE_JS[j];
								    document.getElementById("new_item_amt"+j).value = VMAIN_ITEM_AMT_JS[j];
								    
								}
			    			}
							if(document.forms[0].invoice_category.value=="P")
							{
								document.getElementById("new_tax_btn"+j).disabled=false;	
							}
							else
							{
								document.forms[0].tax_config_btn.disabled=false;
								
								if(document.forms[0].new_tax_struct_info.value=="")
								{
									new_tax_struct_info = document.forms[0].main_tax_struct_info.value;
									document.forms[0].new_tax_struct_info.value=new_tax_struct_info;
									document.forms[0].tax_config_btn.value="Tax Config ("+new_tax_struct_info+")";
									document.forms[0].new_tax_struct_cd.value=document.forms[0].main_tax_struct_cd.value;
									document.forms[0].tax_struct_cd.value=document.forms[0].main_tax_struct_cd.value;
									document.forms[0].new_tax_dt.value=document.forms[0].main_tax_dt.value;
									document.forms[0].new_tax_amt.value=document.forms[0].main_tax_amt.value;
								}
								
							}
							calc(j);
						}
						else 
						{
							if(document.forms[0].invoice_category.value=="P")
							{
								document.getElementById("new_tax_btn"+j).disabled=true;
							}
							else 
							{
								document.forms[0].tax_config_btn.disabled=true;
							}
						}
					}
			    }
				document.forms[0].new_gross_amt.value = document.forms[0].main_gross_amt.value;
				
				if(document.forms[0].invoice_category.value=="P")
				{
					document.forms[0].new_tax_amt.value=document.forms[0].main_tax_amt.value;
				}
				
				
				document.forms[0].new_net_payable.value=document.forms[0].main_net_payable.value; 
			}	
			criteriFormula();
		}, 200);
	}
}

</script>

</form>
</body>
</html>
