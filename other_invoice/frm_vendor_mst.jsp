<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh(opration)
{
	var vendor_cd ="0";
	if(opration == "MODIFY")
	{
		vendor_cd = document.forms[0].vendor_cd.value;
	}
	var entity_role = document.forms[0].entity_role.value;
	var u = document.forms[0].u.value;
	
	var url = "frm_vendor_mst.jsp?opration="+opration+"&u="+u+"&vendor_cd="+vendor_cd+"&entity_role="+entity_role;

	document.getElementById("loading").style.visibility = "visible";
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
	var eff_dt = document.forms[0].eff_dt.value;
	var name = document.forms[0].name.value;
	var abbr = document.forms[0].abbr.value;
	var pan_no = document.forms[0].pan_no.value;
	var gstin_no = document.forms[0].gstin_no.value;
	var payee_nm = document.forms[0].payee_nm.value;
	var payee_acc_no = document.forms[0].payee_acc_no.value;
	var ifsc = document.forms[0].ifsc.value;
	var payee = document.forms[0].payee.value;
	
	var reg_eff_dt = document.forms[0].reg_eff_dt;
	var address_type = document.forms[0].address_type;
	var address = document.forms[0].address;
	var city = document.forms[0].city;
	var state = document.forms[0].state;
	var pin = document.forms[0].pin;
	var country = document.forms[0].country;
	
	var temp_pan_no = document.forms[0].temp_pan_no.value;
	
	var opration = document.forms[0].opration.value;
	
	var msg="";
	var flag=true;
	
	if(opration == "MODIFY")
	{
		var vendor_cd = document.forms[0].vendor_cd.value;
		if(vendor_cd=="" || vendor_cd=="0" || trim(vendor_cd)=="")
		{
			msg+="Select Vendor!\n";
			flag=false
		}
	}
	
	if(eff_dt=="" || trim(eff_dt)=="")
	{
		msg+="Enter Eff Date of Vendor!\n";
		flag=false
	}
	if(name=="" || trim(name)=="")
	{
		msg+="Enter Vendor Name!\n";
		flag=false
	}
	if(abbr=="" || trim(abbr)=="")
	{
		msg+="Enter Vendor Abbr!\n";
		flag=false
	}
	if(pan_no=="" || trim(pan_no)=="")
	{
		msg+="Enter Vendor PAN No!\n";
		flag=false
	}
	if (payee == "") 
	{
		
		if (gstin_no=="" || trim(gstin_no)=="") 
		{
			msg+="Enter Vendor GSTIN No!\n";
		}
		
	}
	else 
	{
		
		if (payee_acc_no=="" || trim(payee_acc_no)=="") 
		{
			msg+="Enter Payee Account No!\n"
		}
		if (payee_nm=="" || trim(payee_nm)=="") 
		{
			msg+="Enter Payee Name!\n"
		}
		if (ifsc=="" || trim(ifsc)=="") 
		{
			msg+="Enter IFSC Code!\n"
		}
		
	}
	var addChkbox = document.forms[0].add_chk;
	if(addChkbox!=null && addChkbox.length!=undefined)
	{
		for(var i=0; i<addChkbox.length; i++)
		{
			if(addChkbox[i].checked)
			{
				var adddd="";
				if(address_type[i].value=="R")
				{
					adddd="Registered Address";
				}
				else if(address_type[i].value=="C")
				{
					adddd="Correspondence Address";
				}
				else if(address_type[i].value=="B")
				{
					adddd="Billing Address";
				}
				
				if(reg_eff_dt[i].value=="" || trim(reg_eff_dt[i].value)=="")
				{
					msg+="Enter Eff Date of "+adddd+"!\n";
					flag=false
				}
				if(address[i].value=="" || trim(address[i].value)=="")
				{
					msg+="Enter "+adddd+"!\n";
					flag=false
				}
				if(city[i].value=="" || trim(city[i].value)=="")
				{
					msg+="Enter City of "+adddd+"!\n";
					flag=false
				}
				if(state[i].value=="" || state[i].value=="0" || trim(state[i].value)=="")
				{
					msg+="Enter State of "+adddd+"!\n";
					flag=false
				}
				if(pin[i].value=="" || trim(pin[i].value)=="")
				{
					msg+="Enter Pin of "+adddd+"!\n";
					flag=false
				}
				if(country[i].value=="" || country[i].value=="0" || trim(country[i].value)=="")
				{
					msg+="Select Country of "+adddd+"!\n";
					flag=false
				} 
			}
		}
	}
	
	if(flag)
	{
		var a;
		
		if(opration == "MODIFY")
		{
			var pan_msg="";
			if(pan_no != temp_pan_no && trim(temp_pan_no)!="")
			{
				pan_msg ="This will also overwrite configured PAN number ";
	     		pan_msg += "\n"+temp_pan_no;
	     		pan_msg += "\n\nDo you sure want to continue?"
			}
			
			var b;
			if(pan_msg!="")
			{
				b = confirm(pan_msg);
			}
			else
			{
				b=true;
			}
			
			if(b)
			{
				a = confirm("Do you want to "+opration+" Vendor Detail?")
			}
		}
		else
		{
			a = confirm("Do you want to "+opration+" Vendor Detail?")
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

var newWindow;
function openList(entity_role)
{
	var url = "rpt_vendor_list.jsp?entity_role="+entity_role;;

	if(!newWindow || newWindow.closed)
	{
		newWindow= window.open(url,"Vendor List","top=10,left=70,width=800,height=700,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow= window.open(url,"Vendor List","top=10,left=70,width=800,height=700,scrollbars=1");
	}
}

function refreshChild(vendor_cd)
{
	var opration = document.forms[0].opration.value;
	
	document.forms[0].vendor_cd.value=vendor_cd;
	
	refresh(opration);
}

function changeInvoiceType(val) 
{
	if(val == 'B') 
	{
		document.getElementById("gstin_div").style.visibility = "visible";
		
		document.getElementById("payee_div").style.visibility = "hidden";
		document.getElementById("ifsc_div").style.visibility = "hidden";
		document.forms[0].payee.value = "";
	}
	else 
	{
		document.getElementById("gstin_div").style.visibility = "hidden";
		
		document.getElementById("payee_div").style.visibility = "visible";
		document.getElementById("ifsc_div").style.visibility = "visible";
		document.forms[0].payee.value = "0";
	}
}

function enabledAddress(i)
{
	var addChkbox = document.forms[0].add_chk;
	
	if(addChkbox[i].checked)
	{
		document.forms[0].reg_eff_dt[i].disabled=false;
		document.forms[0].address_type[i].disabled=false;
		document.forms[0].address[i].disabled=false;
		document.forms[0].city[i].disabled=false;
		document.forms[0].zone[i].disabled=false;
		document.forms[0].state[i].disabled=false;
		document.forms[0].pin[i].disabled=false;
		document.forms[0].country[i].disabled=false;
		document.forms[0].phone[i].disabled=false;
		document.forms[0].alt_phone[i].disabled=false;
		document.forms[0].cell[i].disabled=false;
		document.forms[0].fax1[i].disabled=false;
		document.forms[0].fax2[i].disabled=false;
		document.forms[0].email[i].disabled=false;
	}
	else
	{
		document.forms[0].reg_eff_dt[i].disabled=true;
		document.forms[0].address_type[i].disabled=true;
		document.forms[0].address[i].disabled=true;
		document.forms[0].city[i].disabled=true;
		document.forms[0].zone[i].disabled=true;
		document.forms[0].state[i].disabled=true;
		document.forms[0].pin[i].disabled=true;
		document.forms[0].country[i].disabled=true;
		document.forms[0].phone[i].disabled=true;
		document.forms[0].alt_phone[i].disabled=true;
		document.forms[0].cell[i].disabled=true;
		document.forms[0].fax1[i].disabled=true;
		document.forms[0].fax2[i].disabled=true;
		document.forms[0].email[i].disabled=true;
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.other_invoice.DataBean_Other_Invoice" id="dbOtherInvoice" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String vendor_cd = request.getParameter("vendor_cd")==null?"0":request.getParameter("vendor_cd");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");

dbOtherInvoice.setCallFlag("VENDOR_MASTER");
dbOtherInvoice.setOpration(opration);
dbOtherInvoice.setVendor_cd(vendor_cd);
dbOtherInvoice.setEntity_role(entity_role);
dbOtherInvoice.init();

Vector VCOUNTRY_CODE = dbOtherInvoice.getVCOUNTRY_CODE();
Vector VCOUNTRY_NM = dbOtherInvoice.getVCOUNTRY_NM();
Vector VISO_CODE = dbOtherInvoice.getVISO_CODE();
Vector VTIN =dbOtherInvoice.getVTIN();
Vector VSTATE_CODE = dbOtherInvoice.getVSTATE_CODE();
Vector VSTATE_NM = dbOtherInvoice.getVSTATE_NM();


String name=dbOtherInvoice.getName();
String abbr=dbOtherInvoice.getAbbr();
String eff_dt=dbOtherInvoice.getEff_dt();
String service_no=dbOtherInvoice.getService_no();
String service_dt=dbOtherInvoice.getService_dt();
String pan_no=dbOtherInvoice.getPan_no();
String pan_dt=dbOtherInvoice.getPan_dt();
String gst_tin_no=dbOtherInvoice.getGST_TIN_NO();
String gst_tin_dt=dbOtherInvoice.getGST_TIN_DT();
String cst_tin_no=dbOtherInvoice.getCST_TIN_NO();
String cst_tin_dt=dbOtherInvoice.getCST_TIN_DT();
String tan_no=dbOtherInvoice.getTAN_NO();
String tan_dt=dbOtherInvoice.getTAN_ISSUE_DT();
String gstin_no=dbOtherInvoice.getGSTIN_NO();
String gstin_dt=dbOtherInvoice.getGSTIN_DT();
String web_addr=dbOtherInvoice.getWeb_addr();
String payee_acc_no = dbOtherInvoice.getPAYEE_ACC_NO();
String ifsc = dbOtherInvoice.getIFSC();
String payee_nm = dbOtherInvoice.getPAYEE_NM();
String[] addr_type = {"Registered", "Correspondence", "Billing"};
String notes=dbOtherInvoice.getNotes();
String business_flag = dbOtherInvoice.getBUSINESS_FLAG();

if(business_flag.equals(""))
{
	business_flag = "B";	
}


String[] reg_eff_dt=dbOtherInvoice.getREG_EFF_DT(); 
String[] address_type = {"R", "C", "B"};
String[] address=dbOtherInvoice.getADDRESS();
String[] city=dbOtherInvoice.getCITY();
String[] pin=dbOtherInvoice.getPIN();
String[] state=dbOtherInvoice.getSTATE();
String[] zone=dbOtherInvoice.getZONE();
String[] country=dbOtherInvoice.getCOUNTRY();
String[] phone=dbOtherInvoice.getPHONE();
String[] mobile=dbOtherInvoice.getMOBILE();
String[] alt_phone=dbOtherInvoice.getALT_PHONE();
String[] fax1=dbOtherInvoice.getFAX1();
String[] fax2=dbOtherInvoice.getFAX2();
String[] email=dbOtherInvoice.getEMAIL();

String old_value = "";
if(business_flag.equals("C"))
{
	old_value="VD="+vendor_cd+"#NAME="+name+"#ABBR="+abbr+"#PANNO="+pan_no+"#PANDT="+pan_dt+"#GSTTIN="+gst_tin_no+"#GSTTINDT="+
			gst_tin_dt+"#CSTTIN="+cst_tin_no+"#CSTTINDT="+cst_tin_dt+"#TANNO="+tan_no+"#TANDT="+tan_dt+"#SERVICENO="+service_no+"#SERVICEDT="+service_dt+
			"#GSTIN="+gstin_no+"#GSTINDT="+gstin_dt+"#WEBADDR="+web_addr+"#NOTES="+notes+"#BUSINESSFLAG="+business_flag+"#PAYEEACC="+payee_acc_no+"#IFSC="+ifsc+
			"#PAYEENAME"+payee_nm;
			/* +"#ADD="+address+"#CITY="+city+"#STATE="+state+"#ZONE="+zone+"#PIN="+pin+"#COUNTRY="+country+"#PH="+phone+"#FAX1="+fax1+
			"#FAX2="+fax2+"#CELL="+mobile+"#EMAIL="+email+"#ALTPH="+alt_phone+"#REFFDT="+reg_eff_dt; */
}
else 
{	
	old_value="VD="+vendor_cd+"#NAME="+name+"#ABBR="+abbr+"#PANNO="+pan_no+"#PANDT="+pan_dt+"#GSTTIN="+gst_tin_no+"#GSTTINDT="+
			gst_tin_dt+"#CSTTIN="+cst_tin_no+"#CSTTINDT="+cst_tin_dt+"#TANNO="+tan_no+"#TANDT="+tan_dt+"#SERVICENO="+service_no+"#SERVICEDT="+service_dt+
			"#GSTIN="+gstin_no+"#GSTINDT="+gstin_dt+"#WEBADDR="+web_addr+"#NOTES="+notes+"#BUSINESSFLAG="+business_flag;
			/* +"#ADD="+address+"#CITY="+city+"#STATE="+state+"#ZONE="+zone+"#PIN="+pin+"#COUNTRY="+country+"#PH="+phone+"#FAX1="+fax1+"#FAX2="+fax2+"#CELL="+mobile+"#EMAIL="+email+
			"#ALTPH="+alt_phone+"#REFFDT="+reg_eff_dt; */
}
%>
<body onload=" <%if(entity_role.equals("S")){%>enabledAddress('0');<%}%> enabledAddress('1');enabledAddress('2');">
<%@ include file="../home/header.jsp"%>
<%if(!owner_cd.equals("2")) {%>
<div class="box-body">
	<div class="row">
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
		<div class="col-md-8 col-sm-8 col-xs-8">
			<div class="card cardmain">
				<div class="card-header cdheader ">
				</div>
				<div class="card-body cdbody">
					<div class="alert alert-info">
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="form-group row" align="center">
									<label class="form-label"  style="font-size:40px;font-weight: 700;"><i class='fa fa-exclamation-circle fa-lg'></i> Feature Not Supported</label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
				</div>
			</div>   
		</div>
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
	</div>
</div>
<%}else{ %>
<form method="post" action="../servlet/Frm_other_invoice">

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
							Other Entity Master
						</div>
					 	<%-- <div class="btn-group">
							<label class="btn btn-outline-secondary btngrp <%if(opration.equals("INSERT")){%>btnactive<%}%>" onclick="refresh('INSERT');"><i class="fa fa-plus-circle"></i>&nbsp;New</label>
						  	<label class="btn btn-outline-secondary btngrp <%if(opration.equals("MODIFY")){%>btnactive<%}%>" onclick="refresh('MODIFY');"><i class="fa fa-edit"></i>&nbsp;Modify</label>
						</div> --%>
						<div class="btn-group">
							<select class="btn btn-outline-secondary btngrp <%if(!entity_role.equals("0")){%>btnactive<%}%>" name="entity_role" onchange="refresh('INSERT')">
								<option value="0">Select Entity Roles</option>
								<option value="V">Vendor</option>
				    			<option value="S">Supplier</option>
							</select>
						</div>
						<script>
							document.forms[0].entity_role.value="<%=entity_role%>"
						</script>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="d-flex justify-content-between" >
						<div ></div>
						<%if(entity_role.equals("V") && !entity_role.equals("0")) {%>
						<div class="btn-group" >
							<label class="btn btn-outline-secondary btngrp <%if(opration.equals("INSERT")){%>btnactive<%}%>" onclick="refresh('INSERT');"><i class="fa fa-plus-circle"></i>&nbsp;New</label>
							<label class="btn btn-outline-secondary btngrp <%if(opration.equals("MODIFY")){%>btnactive<%}%>" onclick="refresh('MODIFY');"><i class="fa fa-edit"></i>&nbsp;Modify</label>
						</div>
						<%} else {%>
						<%opration = "MODIFY";}%>
					</div>
					<%if(opration.equalsIgnoreCase("MODIFY") && !entity_role.equals("0")){%>
						<div class="row m-b-5">
							<div class="col-sm-3 col-xs-3 col-md-3">
								<div class="form-group row">
				    				<div class="col-sm-12 col-xs-12 col-md-12">
				    					<span class="btn btn-info btn-sm select_btn" onclick="openList('<%=entity_role%>');" style="font-weight: bold;">
					    					<i class="fa fa-list-ul"></i>&nbsp;Select <%if(entity_role.equals("0")){%>Entity<%} else if(entity_role.equals("V")) {%>Vendor<%} else {%>Supplier<%}%>
					    				</span>
				    				</div>
				    			</div>
							</div>
						</div>
					<%} %>
					
					<div class="row m-b-3">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i><%if(entity_role.equals("0")){%>Entity<%} else if(entity_role.equals("V")) {%>Vendor<%} else {%>Supplier<%}%> Details</label>
					</div>
					
					<%if(entity_role.equals("V") || entity_role.equals("0")) {%>
						<div class="row m-b-3">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Name<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="name" value="<%=name%>" maxLength="100">
					      				<input type="hidden" class="form-control form-control-sm" name="vendor_cd" value="<%=vendor_cd%>">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Eff Date<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" value="<%=eff_dt%>" maxLength="10" onblur="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');" onchange="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Abbreviation<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="abbr" value="<%=abbr%>" maxLength="20" onblur="isExistABBR();">	
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">
								<label class="form-label"><b>Invoice Type</b></label>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
						      				<input type="radio" name="invoice_type" value="B" <%if(business_flag.equals("B")){%>checked<%}%> onclick="changeInvoiceType('B');">&nbsp;B2B&nbsp;&nbsp;
					      					<input type="radio" name="invoice_type" value="C" <%if(business_flag.equals("C")){%>checked<%}%> onclick="changeInvoiceType('C');">&nbsp;B2C&nbsp;&nbsp;
					    			</div>
					  			</div>
							</div>
						</div>
						<!-- Service Tax No -->
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Service Tax No<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="service_no" value="<%=service_no%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Service Tax Eff Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="service_dt" value="<%=service_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<!-- PAN No -->
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>PAN No<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="pan_no" value="<%=pan_no%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>PAN Eff Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="pan_dt" value="<%=pan_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<!-- GST TIN No -->
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>GST TIN No</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="gst_tin_no" value="<%=gst_tin_no%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>GST TIN Eff Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="gst_tin_dt" value="<%=gst_tin_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<!-- CST TIN No -->
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>CST TIN No</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="cst_tin_no" value="<%=cst_tin_no%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>CST TIN Eff Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="cst_tin_dt" value="<%=cst_tin_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<!-- TAN No -->
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>TAN No</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="tan_no" value="<%=tan_no%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>TAN Eff Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="tan_dt" value="<%=tan_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<!-- GSTIN No -->
						<div <%if(business_flag.equals("C")){ %> style="visibility: hidden;" <%} %> class="row m-b-5" id = "gstin_div">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>GSTIN No<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="gstin_no" value="<%=gstin_no%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>GSTIN Eff Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="gstin_dt" value="<%=gstin_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						
						<div <%if(business_flag.equals("B")){ %> style="visibility: hidden;" <%} %> class="row m-b-5" id = "payee_div">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Payee Name<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="payee_nm" value="<%=payee_nm%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Payee Account Number<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="payee_acc_no" value="<%=payee_acc_no%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
						</div>
						
						<div <%if(business_flag.equals("B")){ %> style="visibility: hidden;" <%} %> class="row m-b-5" id = "ifsc_div">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>IFSC<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="ifsc" value="<%=ifsc%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
						</div>
						
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Web-Site Address</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input class="form-control form-control-sm" name="web_addr" value="<%=web_addr%>" maxlength="100">
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Remark</b></label>
					  			</div>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<font size="1" face="arial, helvetica, sans-serif">&nbsp;( You may enter up to 2048 characters. )&nbsp;
											<input readonly type=text name="remLen" size="3" maxlength="3" class=""> characters left
										</font><br>
					      				<textarea class="form-control" name="notes" cols="75" rows="3" onKeyDown="textCounter(this.form.notes,this.form.remLen,2047);" onKeyUp="textCounter(this.form.notes,this.form.remLen,2047);"><%=notes%></textarea>
					    			</div>
					  			</div>
							</div>
						</div>
					<%} else {%> 
					<div class="row m-b-3">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Name<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="name" value="<%=name%>" maxLength="100" readOnly>
					      				<input type="hidden" class="form-control form-control-sm" name="vendor_cd" value="<%=vendor_cd%>">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Eff Date<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" value="<%=eff_dt%>" maxLength="10" onblur="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');" onchange="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');" autocomplete="off" style = "pointer-events: none;" readOnly>
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Abbreviation<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="abbr" value="<%=abbr%>" maxLength="20" readOnly>	
					    			</div>
					  			</div>
							</div>
						</div>
						<!-- Service Tax No -->
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Service Tax No<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="service_no" value="<%=service_no%>" maxLength="20" readOnly>
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Service Tax Eff Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="service_dt" value="<%=service_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" style = "pointer-events: none;" readOnly>
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<!-- PAN No -->
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>PAN No<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="pan_no" value="<%=pan_no%>" maxLength="20" readOnly>
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>PAN Eff Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="pan_dt" value="<%=pan_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" style = "pointer-events: none;" readOnly>
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<!-- GST TIN No -->
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>GST TIN No</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="gst_tin_no" value="<%=gst_tin_no%>" maxLength="20" readOnly> 
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>GST TIN Eff Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="gst_tin_dt" value="<%=gst_tin_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" style = "pointer-events: none;" readOnly>
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<!-- CST TIN No -->
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>CST TIN No</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="cst_tin_no" value="<%=cst_tin_no%>" maxLength="20" readOnly>
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>CST TIN Eff Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="cst_tin_dt" value="<%=cst_tin_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" style = "pointer-events: none;" readOnly>
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<!-- TAN No -->
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>TAN No</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="tan_no" value="<%=tan_no%>" maxLength="20" readOnly>
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>TAN Eff Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="tan_dt" value="<%=tan_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" style = "pointer-events: none;" readOnly>
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<!-- GSTIN No -->
						<div class="row m-b-5" id = "gstin_div">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>GSTIN No<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="gstin_no" value="<%=gstin_no%>" maxLength="20" readOnly>
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>GSTIN Eff Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="gstin_dt" value="<%=gstin_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" style = "pointer-events: none;" readOnly>
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Web-Site Address</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input class="form-control form-control-sm" name="web_addr" value="<%=web_addr%>" maxlength="100" readOnly>
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Remark</b></label>
					  			</div>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<font size="1" face="arial, helvetica, sans-serif">&nbsp;( You may enter up to 2048 characters. )&nbsp;
											<input readonly type=text name="remLen" size="3" maxlength="3" class=""> characters left
										</font><br>
					      				<textarea class="form-control" name="notes" cols="75" rows="3" onKeyDown="textCounter(this.form.notes,this.form.remLen,2047);" onKeyUp="textCounter(this.form.notes,this.form.remLen,2047);" style = "pointer-events: none;" readOnly><%=notes%></textarea>
					    			</div>
					  			</div>
							</div>
						</div>
					<%} %>
					
					<% for (int j = 0; j < addr_type.length; j++) { %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					
					<!-- Registered Address & Communication Details -->
					<div class="row m-b-5">
						<label class="form-label subheader"> <input class="form-check-input" type="checkbox" name="add_chk" onclick="enabledAddress('<%=j%>')" <%if(j == 0 && !entity_role.equals("S")) {%> checked <%}%> <%if(entity_role.equals("S")) {%>style = "display:none;" <%}%>> <%= addr_type[j] %> Address & Communication Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Address<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="address" value="<%=address[j]%>" maxLength="150">
				      				<input type="hidden" name="address_type" value="<%=address_type[j]%>">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<label class="form-label"><b>Eff Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="reg_eff_dt" value="<%=reg_eff_dt[j]%>" maxLength="10" onblur="validateDate(this);checkEffectiveDate(this,'<%=reg_eff_dt[j]%>');" onchange="validateDate(this);checkEffectiveDate(this,'<%=reg_eff_dt[j]%>');" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>City<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="city" value="<%=city[j]%>" maxLength="40">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>State/Province<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									
									<input type="text" class="form-control form-control-sm" name="state" value="<%=state[j]%>" maxLength="40">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Zone</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="zone" id="zone<%=j%>">
										<option value="0">--Select--</option>
										<option value="N">North</option>
										<option value="S">South</option>
										<option value="E">East</option>
										<option value="W">West</option>
										<option value="C">Central</option>
									</select>
									<script>document.getElementById("zone<%=j%>").value="<%=zone[j]%>"</script>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>PIN/ZIP Code<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="pin" value="<%=pin[j]%>" maxLength="6" onkeyup="checkForNumber(this);">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Country<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="country" id="country<%=j%>">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTRY_NM.size();i++){ %>
										<option value="<%=VCOUNTRY_NM.elementAt(i)%>"><%=VCOUNTRY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>
									<%-- document.forms[0].country.value="<%=country[j]%>" --%>
										document.getElementById("country<%=j%>").value="<%=country[j]%>" 
									</script>
									
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Phone</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="phone" value="<%=phone[j]%>" maxLength="20">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Alternate Phone</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="alt_phone" value="<%=alt_phone[j]%>" maxLength="20">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Cell</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="cell" value="<%=mobile[j]%>" maxLength="20">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Fax-1</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="fax1" value="<%=fax1[j]%>" maxLength="20">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Fax-2</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="fax2" value="<%=fax2[j]%>" maxLength="20">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>E-mail</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="email" value="<%=email[j]%>" maxLength="40" onBlur="validateEmail(this);">
				    			</div>
				  			</div>
						</div>
					</div>
					<% } %>
					</div>
					<div  <%if(!entity_role.equals("V")){ %> style="display: none;" <%} %>  class="card-footer cdfooter text-center">
						<div class="d-flex justify-content-between">
							<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
							<%if(write_access.equals("Y")){ %>
							<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
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

<input type="hidden" name="option" value="VENDOR_MST">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="old_value" value="<%=old_value%>">

<input type="hidden" name="sysdate" value="<%=sysdate%>">

<input type="hidden" name="temp_pan_no" value="<%=pan_no%>">
<input type="hidden" name="payee" value="">

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
<input type="hidden" name="entity_tpye" value="<%=entity_role%>">
<input type="hidden" name="u" value="<%=u%>">

<script>

function isExistABBR()
{
	var opration = document.forms[0].opration.value;
	
	var vendor_cd ="0";
	if(opration == "MODIFY")
	{
		vendor_cd = document.forms[0].vendor_cd.value;
	}
	var abbr = document.forms[0].abbr.value;
	
	var info="";
	
	$.post("../servlet/DB_Other_Invoice_Ajax?setCallType=IsExistVendorABBR&vendor_cd="+vendor_cd+
			"&vendor_abbr="+abbr+"&opration="+opration, 
		function(responseJson) 
		{
		$.each(responseJson, function(index, json) {
			$.each(json.VENDOR_DTL, function(index_1, json_1) {
				if(parseInt(json_1.ABBR) > 0)
				{
					info+="Abbreviation already Exists!";
				}
				
				if(info!="")
				{
					alert(info)
					document.forms[0].abbr.value="";
				}
			});
		});
	});
}

</script>
</form>
<%} %>
</body>
</html>