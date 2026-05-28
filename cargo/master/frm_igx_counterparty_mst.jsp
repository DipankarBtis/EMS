<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh(opration)
{
	var counterparty_cd ="0";
	if(opration == "MODIFY")
	{
		counterparty_cd = document.forms[0].counterparty_cd.value;
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_igx_counterparty_mst.jsp?opration="+opration+"&u="+u+"&counterparty_cd="+counterparty_cd;

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
	var sap_code = document.forms[0].sap_code.value;
	
	var reg_eff_dt = document.forms[0].reg_eff_dt.value;
	var address = document.forms[0].address.value;
	var city = document.forms[0].city.value;
	var state = document.forms[0].state.value;
	var pin = document.forms[0].pin.value;
	var country = document.forms[0].country.value;
	
	var opration = document.forms[0].opration.value;
	
	var msg="";
	var flag=true;
	
	if(opration == "MODIFY")
	{
		var counterparty_cd = document.forms[0].counterparty_cd.value;
		if(counterparty_cd=="" || counterparty_cd=="0" || trim(counterparty_cd)=="")
		{
			msg+="Select Counterparty!\n";
			flag=false
		}
	}
	
	if(eff_dt=="" || trim(eff_dt)=="")
	{
		msg+="Enter Eff Date of Counterparty!\n";
		flag=false
	}
	if(name=="" || trim(name)=="")
	{
		msg+="Enter Counterparty Name!\n";
		flag=false
	}
	if(abbr=="" || trim(abbr)=="")
	{
		msg+="Enter Counterparty Abbr!\n";
		flag=false
	}
	if(pan_no=="" || trim(pan_no)=="")
	{
		msg+="Enter Counterparty PAN No!\n";
		flag=false
	}
	/* if(sap_code=="" || trim(sap_code)=="") //NOT MANDETORY FILED FOR IGX COUNTERPARTY ONLY
	{
		msg+="Enter Counterparty SAP Code!\n";
		flag=false
	} */
	if(reg_eff_dt=="" || trim(reg_eff_dt)=="")
	{
		msg+="Enter Eff Date of Register Address!\n";
		flag=false
	}
	if(address=="" || trim(address)=="")
	{
		msg+="Enter Register Address!\n";
		flag=false
	}
	if(city=="" || trim(city)=="")
	{
		msg+="Enter City of Register Address!\n";
		flag=false
	}
	if(state=="" || state=="0" || trim(state)=="")
	{
		msg+="Enter State of Register Address!\n";
		flag=false
	}
	if(pin=="" || trim(pin)=="")
	{
		msg+="Enter Pin of Register Address!\n";
		flag=false
	}
	if(country=="" || country=="0" || trim(country)=="")
	{
		msg+="Select Country of Register Address!\n";
		flag=false
	}
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" Counterparty Detail?")
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

function changeStatus(obj)
{
	if(obj.checked)
	{
		document.forms[0].igx_flg.value="Y";
	}
	else
	{
		document.forms[0].igx_flg.value="N";
	}
}

var newWindow;
function openList()
{
	var url = "rpt_counterparty_list.jsp";

	if(!newWindow || newWindow.closed)
	{
		newWindow= window.open(url,"Counterparty List","top=10,left=70,width=800,height=700,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow= window.open(url,"Counterparty List","top=10,left=70,width=800,height=700,scrollbars=1");
	}
}

function refreshChild(counterparty_cd)
{
	var opration = document.forms[0].opration.value;
	
	document.forms[0].counterparty_cd.value=counterparty_cd;
	
	refresh(opration);
}

function doUpdateStatus(status)
{
	var eff_dt = document.forms[0].eff_dt.value;
	var name = document.forms[0].name.value;
	var abbr = document.forms[0].abbr.value;
	var pan_no = document.forms[0].pan_no.value;
	var sap_code = document.forms[0].sap_code.value;
	
	var reg_eff_dt = document.forms[0].reg_eff_dt.value;
	var address = document.forms[0].address.value;
	var city = document.forms[0].city.value;
	var state = document.forms[0].state.value;
	var pin = document.forms[0].pin.value;
	var country = document.forms[0].country.value;
	
	var opration = document.forms[0].opration.value;
	
	var sysdate = document.forms[0].sysdate.value;
	
	var msg="";
	var flag=true;
	
	if(opration == "MODIFY")
	{
		var counterparty_cd = document.forms[0].counterparty_cd.value;
		if(counterparty_cd=="" || counterparty_cd=="0" || trim(counterparty_cd)=="")
		{
			msg+="Select Counterparty!\n";
			flag=false
		}
	}
	
	if(eff_dt=="" || trim(eff_dt)=="")
	{
		msg+="Enter Eff Date of Counterparty!\n";
		flag=false
	}
	if(name=="" || trim(name)=="")
	{
		msg+="Enter Counterparty Name!\n";
		flag=false
	}
	if(abbr=="" || trim(abbr)=="")
	{
		msg+="Enter Counterparty Abbr!\n";
		flag=false
	}
	if(pan_no=="" || trim(pan_no)=="")
	{
		msg+="Enter Counterparty PAN No!\n";
		flag=false
	}
	if(sap_code=="" || trim(sap_code)=="")
	{
		msg+="Enter Counterparty SAP Code!\n";
		flag=false
	}
	if(reg_eff_dt=="" || trim(reg_eff_dt)=="")
	{
		msg+="Enter Eff Date of Register Address!\n";
		flag=false
	}
	if(address=="" || trim(address)=="")
	{
		msg+="Enter Register Address!\n";
		flag=false
	}
	if(city=="" || trim(city)=="")
	{
		msg+="Enter City of Register Address!\n";
		flag=false
	}
	if(state=="" || state=="0" || trim(state)=="")
	{
		msg+="Enter State of Register Address!\n";
		flag=false
	}
	if(pin=="" || trim(pin)=="")
	{
		msg+="Enter Pin of Register Address!\n";
		flag=false
	}
	if(country=="" || country=="0" || trim(country)=="")
	{
		msg+="Select Country of Register Address!\n";
		flag=false
	}
	
	if(flag)
	{
		var status_nm="";
		if(status=="Y")
		{
			status_nm="Activate";
		}
		else if(status=="N")
		{
			status_nm="Deactivate";
		}
		var a = confirm("Do you want to "+status_nm+" the Counterparty "+abbr+" - "+name+"?")
		if(a)
		{
			var b = confirm("Counterparty "+abbr+" - "+name+" will be "+status_nm+"d with an immediate effect.\n\nAre you sure want to Continue? ")
			if(b)
			{
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].status.value=status;
				document.forms[0].eff_dt.value=sysdate;
				document.forms[0].submit();
			}
		}
	}
	else
	{
		alert(msg);
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

dbcounterpty.setCallFlag("COUNTERPARTY_MST");
dbcounterpty.setOpration(opration);
dbcounterpty.setCounterparty_cd(counterparty_cd);
dbcounterpty.init();

Vector VCOUNTRY_CODE = dbcounterpty.getVCOUNTRY_CODE();
Vector VCOUNTRY_NM = dbcounterpty.getVCOUNTRY_NM();
Vector VISO_CODE = dbcounterpty.getVISO_CODE();
Vector VTIN =dbcounterpty.getVTIN();
Vector VSTATE_CODE = dbcounterpty.getVSTATE_CODE();
Vector VSTATE_NM = dbcounterpty.getVSTATE_NM();

Vector VCOUNTERPARTY_CD = dbcounterpty.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcounterpty.getVCOUNTERPARTY_ABBR();

String name=dbcounterpty.getName();
String abbr=dbcounterpty.getAbbr();
String eff_dt=dbcounterpty.getEff_dt();
String pan_no=dbcounterpty.getPan_no();
String pan_dt=dbcounterpty.getPan_dt();
String notes=dbcounterpty.getNotes();
String kyc_flg=dbcounterpty.getKyc_flg();
String igx_flg=dbcounterpty.getIgx_flg();
String business_unit=dbcounterpty.getBusiness_unit();
String status=dbcounterpty.getStatus();
String sap_code=dbcounterpty.getSap_code();

String reg_eff_dt=dbcounterpty.getReg_eff_dt();
String address=dbcounterpty.getAddress();
String city=dbcounterpty.getCity();
String pin=dbcounterpty.getPin();
String state=dbcounterpty.getState();
String zone=dbcounterpty.getZone();
String country=dbcounterpty.getCountry();
String phone=dbcounterpty.getPhone();
String mobile=dbcounterpty.getMobile();
String alt_phone=dbcounterpty.getAlt_phone();
String fax1=dbcounterpty.getFax1();
String fax2=dbcounterpty.getFax2();
String email=dbcounterpty.getEmail();

String old_value="CP="+counterparty_cd+"#NAME="+name+"#ABBR="+abbr+"#PANNO="+pan_no+"#PANDT="+pan_dt+"#KYC="+kyc_flg+"#IGX="+igx_flg+"#EFFDT="+eff_dt+
"#ADD="+address+"#CITY="+city+"#STATE="+state+"#ZONE="+zone+"#PIN="+pin+"#COUNTRY="+country+"#PH="+phone+"#FAX1="+fax1+
"#FAX2="+fax2+"#CELL="+mobile+"#EMAIL="+email+"#ALTPH="+alt_phone+"#REFFDT="+reg_eff_dt+"#STATUS="+status+"#SAPCD="+sap_code+"#NOTES="+notes;

%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_counterparty">

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
							IGX Counterparty Master
						</div>
					 	<div class="btn-group">
							<label class="btn btn-outline-secondary btngrp <%if(opration.equals("INSERT")){%>btnactive<%}%>" onclick="refresh('INSERT');"><i class="fa fa-plus-circle"></i>&nbsp;New</label>
						  	<label class="btn btn-outline-secondary btngrp <%if(opration.equals("MODIFY")){%>btnactive<%}%>" onclick="refresh('MODIFY');"><i class="fa fa-edit"></i>&nbsp;Modify</label>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%-- <div class="row m-b-5" <%if(!opration.equalsIgnoreCase("MODIFY")){ %>style="display:none;"<%} %>>
						<div class="col-sm-3 col-xs-3 col-md-3">
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Counterparty<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
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
						<div class="col-sm-3 col-xs-3 col-md-3">
						</div>
					</div> --%>
					<%if(opration.equalsIgnoreCase("MODIFY")){ %>
						<div class="row m-b-5">
							<div class="col-sm-3 col-xs-3 col-md-3">
								<div class="form-group row">
				    				<div class="col-sm-12 col-xs-12 col-md-12">
				    					<span class="btn btn-info btn-sm select_btn" onclick="openList();" style="font-weight: bold;">
					    					<i class="fa fa-list-ul"></i>&nbsp;Select Counterparty
					    				</span>
				    				</div>
				    			</div>
							</div>
							<div class="col-sm-6 col-xs-6 col-md-6">
								<div class="form-group row">
				    				<div class="col-sm-12 col-xs-12 col-md-12">
				    					<%if(!counterparty_cd.equals("0")){ %>
											<%if(status.equals("N")){ %>
											<%=utilmsg.errorMessage("<b>Counterparty Deactivated since "+eff_dt+"</b>")%>
											<%}else{ %>
											<%=utilmsg.infoMessage("<b>Counterparty Active since "+eff_dt+"</b>")%>
											<%} %>
										<%} %>	
				    				</div>
				    			</div>
							</div>
							<div class="col-sm-3 col-xs-3 col-md-3">
							<%if(!counterparty_cd.equals("") && !counterparty_cd.equals("0") && !kyc_flg.equals("Y")){ %>
								<div class="d-flex justify-content-end">
									<div class="form-group row">
				    					<div class="col-sm-12 col-xs-12 col-md-12">
				    						<input type="button" class="btn btn-sm request_btn" value="<%if(status.equals("N")){%>Activate<%}else{%>Deactivate<%}%>" 
				    						onclick="<%if(status.equals("N")){%>doUpdateStatus('Y');<%}else{%>doUpdateStatus('N');<%}%>">
				    					</div>
				    				</div>
				    			</div>
				    		<%} %>
							</div>
						</div>
					<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Counterparty Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="name" value="<%=name%>" maxLength="100" onblur="isExistNM();">
				      				<input type="hidden" class="form-control form-control-sm" name="counterparty_cd" value="<%=counterparty_cd%>">
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
							<div class="form-group row">
				    			<label class="form-label"><b>Clearance</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="checkbox" name="kyc_chk" <%if(kyc_flg.equals("Y")){%>checked<%} %>>&nbsp;<label class="form-label">KYC</label>&nbsp;&nbsp;&nbsp;
				      				<input type="checkbox" name="igx_chk" <%if(igx_flg.equals("Y")){%>checked<%} %> onclick="changeStatus(this)">&nbsp;<label class="form-label">IGX</label>
				      				<input type="hidden" name="kyc_flg" value="<%=kyc_flg%>">
    								<input type="hidden" name="igx_flg" value="<%=igx_flg%>">
    								<script type="text/javascript">
    									document.forms[0].kyc_chk.style.pointerEvents = "none";
							    	</script>
				    			</div>
				  			</div>
						</div>
					</div>
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
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>SAP Code<!-- <span class="s-red">*</span> --></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="sap_code" value="<%=sap_code%>" maxLength="20" onblur="isExistSAPCode();">
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
				    				<font size="1" face="arial, helvetica, sans-serif">&nbsp;( You may enter up to 150 characters. )&nbsp;
										<input readonly type=text name="remLen" size="3" maxlength="3" class=""> characters left
									</font><br>
				      				<textarea class="form-control" name="notes" cols="75" rows="3" onKeyDown="textCounter(this.form.notes,this.form.remLen,149);" onKeyUp="textCounter(this.form.notes,this.form.remLen,149);"><%=notes%></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Registered Address & Communication Details</label>
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
				      				<input type="text" class="form-control form-control-sm" name="address" value="<%=address%>" maxLength="150">
				      				<input type="hidden" name="address_type" value="R">
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
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="reg_eff_dt" value="<%=reg_eff_dt%>" maxLength="10" onblur="validateDate(this);checkEffectiveDate(this,'<%=reg_eff_dt%>');" onchange="validateDate(this);checkEffectiveDate(this,'<%=reg_eff_dt%>');" autocomplete="off">
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
				      				<input type="text" class="form-control form-control-sm" name="city" value="<%=city%>" maxLength="40">
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
				      				<%-- <select class="form-select form-select-sm" name="state">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VSTATE_NM.size();i++){ %>
										<option value="<%=VSTATE_NM.elementAt(i)%>"><%=VSTATE_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].state.value="<%=state%>"</script> --%>
									
									<input type="text" class="form-control form-control-sm" name="state" value="<%=state%>" maxLength="40">
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
				      				<select class="form-select form-select-sm" name="zone">
										<option value="0">--Select--</option>
										<option value="N">North</option>
										<option value="S">South</option>
										<option value="E">East</option>
										<option value="W">West</option>
										<option value="C">Central</option>
									</select>
									<script>document.forms[0].zone.value="<%=zone%>"</script>
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
				      				<input type="text" class="form-control form-control-sm" name="pin" value="<%=pin%>" maxLength="6" onkeyup="checkForNumber(this);">
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
				      				<select class="form-select form-select-sm" name="country">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTRY_NM.size();i++){ %>
										<option value="<%=VCOUNTRY_NM.elementAt(i)%>"><%=VCOUNTRY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].country.value="<%=country%>"</script>
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
				      				<input type="text" class="form-control form-control-sm" name="phone" value="<%=phone%>" maxLength="20">
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
				      				<input type="text" class="form-control form-control-sm" name="alt_phone" value="<%=alt_phone%>" maxLength="20">
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
				      				<input type="text" class="form-control form-control-sm" name="cell" value="<%=mobile%>" maxLength="20">
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
				      				<input type="text" class="form-control form-control-sm" name="fax1" value="<%=fax1%>" maxLength="20">
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
				      				<input type="text" class="form-control form-control-sm" name="fax2" value="<%=fax2%>" maxLength="20">
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
				      				<input type="text" class="form-control form-control-sm" name="email" value="<%=email%>" maxLength="40" onBlur="validateEmail(this);">
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
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

<input type="hidden" name="option" value="COUNTERPARTY_MST">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="old_value" value="<%=old_value%>">

<input type="hidden" name="business_unit" value="<%=business_unit%>">
<input type="hidden" name="status" value="<%=status%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">

<input type="hidden" name="form_clearance" value="IGX">

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
<%if(kyc_flg.equals("Y")){%>
document.forms[0].eff_dt.readOnly=true;
document.forms[0].name.readOnly=true;
document.forms[0].abbr.readOnly=true;
document.forms[0].pan_no.readOnly=true;
document.forms[0].pan_dt.readOnly=true;
document.forms[0].notes.readOnly=true;

document.forms[0].reg_eff_dt.readOnly=true;
//document.forms[0].address_type.readOnly=true;
document.forms[0].address.readOnly=true;
document.forms[0].city.readOnly=true;
document.forms[0].state.readOnly=true;
//document.forms[0].zone.readOnly=true;
document.forms[0].pin.readOnly=true;
//document.forms[0].country.readOnly=true;
document.forms[0].phone.readOnly=true;
document.forms[0].alt_phone.readOnly=true;
document.forms[0].cell.readOnly=true;
document.forms[0].fax1.readOnly=true;
document.forms[0].fax2.readOnly=true;
document.forms[0].email.readOnly=true;

document.forms[0].eff_dt.style.pointerEvents = "none";
document.forms[0].pan_dt.style.pointerEvents = "none";
document.forms[0].reg_eff_dt.style.pointerEvents = "none";
//document.forms[0].state.style.pointerEvents = "none";
document.forms[0].zone.style.pointerEvents = "none";
document.forms[0].country.style.pointerEvents = "none";
<%}%>

function isExistNM()
{
	var opration = document.forms[0].opration.value;
	
	var counterparty_cd ="0";
	if(opration == "MODIFY")
	{
		counterparty_cd = document.forms[0].counterparty_cd.value;
	}
	var name = document.forms[0].name.value;
	
	var info="";
	
	$.post("../servlet/DB_Master_Ajax?setCallType=IsExistNM&counterparty_cd="+counterparty_cd+"&counterparty_nm="+name+
			"&opration="+opration, 
		function(responseJson) {
		console.log(responseJson);
		$.each(responseJson, function(index, json) {
			$.each(json.COUNTERPARTY_DTL, function(index_1, json_1) {
				if(parseInt(json_1.NAME) > 0)
				{
					info+="Name is already Exist!\n";
				}
				if(info!="")
				{
					alert(info)
					document.forms[0].name.value="";
				}
			});
		});
	});
	
	return info;
}

function isExistABBR()
{
	var opration = document.forms[0].opration.value;
	
	var counterparty_cd ="0";
	if(opration == "MODIFY")
	{
		counterparty_cd = document.forms[0].counterparty_cd.value;
	}
	var abbr = document.forms[0].abbr.value;
	
	var info="";
	
	$.post("../servlet/DB_Master_Ajax?setCallType=IsExistABBR&counterparty_cd="+counterparty_cd+
			"&counterparty_abbr="+abbr+"&opration="+opration, 
		function(responseJson) {
		console.log(responseJson);
		$.each(responseJson, function(index, json) {
			$.each(json.COUNTERPARTY_DTL, function(index_1, json_1) {
				if(parseInt(json_1.ABBR) > 0)
				{
					info+="Abbreviation is already Exist!";
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

function isExistSAPCode()
{
	var opration = document.forms[0].opration.value;
	
	var counterparty_cd ="0";
	if(opration == "MODIFY")
	{
		counterparty_cd = document.forms[0].counterparty_cd.value;
	}
	var sap_code = document.forms[0].sap_code.value;
	
	var info="";
	
	$.post("../servlet/DB_Master_Ajax?setCallType=IsExistSAPCode&counterparty_cd="+counterparty_cd+
			"&sap_code="+sap_code+"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.COUNTERPARTY_DTL, function(index_1, json_1) {
				if(parseInt(json_1.SAP_CODE) > 0)
				{
					info+="SAP Code is already Exist!";
				}
				
				if(info!="")
				{
					alert(info)
					document.forms[0].sap_code.value="";
				}
			});
		});
	});
}
</script>
</form>
</body>
</html>