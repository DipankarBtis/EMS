<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Harsh Maheta 20230807 : Form for Bank Master-->
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var filter_bank_abbr = document.forms[0].filter_bank_abbr.value;
	var filter_bank_nm = document.forms[0].filter_bank_nm.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_bank_mst.jsp?u="+u+"&filter_bank_nm="+filter_bank_nm+"&filter_bank_abbr="+filter_bank_abbr;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function hide_show(id1,id2)
{	
	if(document.getElementById(id1).style.display=='none'){
		document.getElementById(id1).style.display='table-row-group';
		document.getElementById(id2).className='fa fa-compress';
	}else{
		document.getElementById(id1).style.display='none';
		document.getElementById(id2).className='fa fa-expand';
	}
	
	if(document.forms[0].prev_display.value != "" && document.forms[0].prev_display1.value !="")
	{
		if(document.forms[0].prev_display.value != id1 && document.forms[0].prev_display1.value != id2)
		{
			document.getElementById(document.forms[0].prev_display.value).style.display='none';
			document.getElementById(document.forms[0].prev_display1.value).className='fa fa-expand';
		}
	}
	document.forms[0].prev_display.value=id1;
	document.forms[0].prev_display1.value=id2;
} 

function doSubmit()
{
	var bank_nm = document.forms[0].bank_nm.value;
	var bank_abbr = document.forms[0].bank_abbr.value;
	var bank_branch = document.forms[0].bank_branch.value;
	var eff_dt = document.forms[0].eff_dt.value;
	var bank_addr =document.forms[0].bank_addr.value;
	var bank_city =document.forms[0].bank_city.value;
	var bank_pin =document.forms[0].bank_pin.value;
	var state_cd =document.forms[0].state_cd.value;
	var country_cd =document.forms[0].country_cd.value;
	var bank_phone =document.forms[0].bank_phone.value;
	var bank_mobile =document.forms[0].bank_mobile.value;
	var bank_alt_phone =document.forms[0].bank_alt_phone.value;
	var bank_ifsc_code =document.forms[0].bank_ifsc_code.value;
	var fax1 =document.forms[0].fax1.value;
	var fax2 =document.forms[0].fax2.value;
	var status_flag =document.forms[0].status_flag.value;
	var bank_email = document.forms[0].bank_email.value;
	
	var opration = document.forms[0].opration.value;
	
	var msg="";
	var flag=true;
	
	if(trim(bank_nm)=="")
	{
		msg+="Enter Bank Name!\n";
		flag=false;
	}
	if(trim(bank_abbr)=="")
	{
		msg+="Enter Bank ABBR!\n";
		flag=false;
	}
	if(trim(eff_dt)=="")
	{
		msg+="Select Eff Date!\n";
		flag=false;
	}
	if(trim(bank_ifsc_code)=="")
	{
		msg+="Enter IFSC Code!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" the Bank Details?");
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

function doClear()
{
	document.forms[0].bank_nm.value="";
	document.forms[0].bank_abbr.value="";
	document.forms[0].bank_branch.value="";
	
	document.forms[0].opration.value="INSERT";
}
function doModify(VBANK_CD,VBANK_NAME,VBANK_ABBR,VBANK_BRANCH,VEFF_DT,VBANK_ADDR,VBANK_CITY,VBANK_PHONE,
				VBANK_STATE,VBANK_COUNTRY,VBANK_PIN,VBANK_MOBILE,VBANK_EMAIL,VBANK_ALT_PHONE,
				VBANK_IFSC_CD,VBANK_REMARKS,VBANK_FAX1,VBANK_FAX2,VBANK_STATUS_FLAG,VBANK_REMARKS)
{
	if(VBANK_STATUS_FLAG=="N")
	{
		document.getElementById("flexSwitchCheckChecked").checked=false;
		document.getElementById("lb").innerHTML="Inactive";
	}
	else if(VBANK_STATUS_FLAG=="Y")
	{
		document.getElementById("flexSwitchCheckChecked").checked=true;
		document.getElementById("lb").innerHTML="Active";
	}
	document.forms[0].bank_cd.value=VBANK_CD;
	document.forms[0].bank_nm.value=VBANK_NAME;
	document.forms[0].bank_abbr.value=VBANK_ABBR;
	document.forms[0].bank_branch.value=VBANK_BRANCH;
	document.forms[0].eff_dt.value=VEFF_DT;
	document.forms[0].bank_addr.value=VBANK_ADDR;
	document.forms[0].bank_city.value=VBANK_CITY;
	document.forms[0].bank_pin.value=VBANK_PIN;
	document.forms[0].state_cd.value=VBANK_STATE;
	document.forms[0].country_cd.value=VBANK_COUNTRY;
	document.forms[0].bank_phone.value=VBANK_PHONE;
	document.forms[0].bank_mobile.value=VBANK_MOBILE;
	document.forms[0].bank_alt_phone.value=VBANK_ALT_PHONE;
	document.forms[0].bank_ifsc_code.value=VBANK_IFSC_CD;
	document.forms[0].fax1.value=VBANK_FAX1;
	document.forms[0].fax2.value=VBANK_FAX2;
	document.forms[0].status_flag.value=VBANK_STATUS_FLAG;
	document.forms[0].bank_email.value=VBANK_EMAIL;
	document.forms[0].remarks.value=VBANK_REMARKS;
	
	document.forms[0].opration.value="MODIFY";
}
function setActiveInactive(obj)
{
	if(obj.checked)
	{
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="Y";
	}
	else
	{
		document.getElementById("lb").innerHTML="Inactive";
		document.getElementById("status_flag").value="N";
	}
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
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String filter_bank_abbr = request.getParameter("filter_status")==null?"0":request.getParameter("filter_status");
String filter_bank_nm = request.getParameter("filter_priority")==null?"0":request.getParameter("filter_priority");

dbmaster.setCallFlag("BANK_MST");
//dbmaster.setFilter_priority(filter_bank_abbr);
//dbmaster.setFilter_incident_type(filter_bank_nm);
dbmaster.init();

Vector VBANK_NAME = dbmaster.getVBANK_NAME();
Vector VBANK_CD = dbmaster.getVBANK_CD();
Vector VBANK_BRANCH = dbmaster.getVBANK_BRANCH();
Vector VEFF_DT = dbmaster.getVEFF_DT();
Vector VBANK_ABBR = dbmaster.getVBANK_ABBR();
Vector VINDEX = dbmaster.getVINDEX();
Vector VSTATE_CD =dbmaster.getVSTATE_CD();
Vector VSTATE_NM =dbmaster.getVSTATE_NM();
Vector VCOUNTRY_CD =dbmaster.getVCOUNTRY_CD();
Vector VCOUNTRY_NM =dbmaster.getVCOUNTRY_NM();
Vector VBANK_ADDR = dbmaster.getVBANK_ADDR();
Vector VBANK_CITY = dbmaster.getVBANK_CITY();
Vector VBANK_STATE = dbmaster.getVBANK_STATE();
Vector VBANK_COUNTRY = dbmaster.getVBANK_COUNTRY();
Vector VBANK_PIN = dbmaster.getVBANK_PIN();
Vector VBANK_PHONE = dbmaster.getVBANK_PHONE();
Vector VBANK_MOBILE = dbmaster.getVBANK_MOBILE();
Vector VBANK_ALT_PHONE = dbmaster.getVBANK_ALT_PHONE();
Vector VBANK_FAX1 = dbmaster.getVBANK_FAX1();
Vector VBANK_FAX2 = dbmaster.getVBANK_FAX2();
Vector VBANK_EMAIL = dbmaster.getVBANK_EMAIL();
Vector VBANK_REMARKS = dbmaster.getVBANK_REMARKS();
Vector VBANK_IFSC_CD = dbmaster.getVBANK_IFSC_CD();
Vector VBANK_STATUS_FLAG = dbmaster.getVBANK_STATUS_FLAG();
Vector VBANK_STATE_CD = dbmaster.getVBANK_STATE_CD();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_master">
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
				    		Bank Master
	   	 				</div>
					 	<a href="../master/xls_bank_mst.jsp?fileName=Bank Details.xls" download="Bank Details">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a>
				    </div>
				</div>
				<div class="card-body cdbody">
				<%if(write_access.equals("Y")){ %>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#BankModal" onclick="doClear();">Add New Bank</label>
							</div>
						</div>
					</div><%} %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr>
											<th>Select</th>
											<th>Bank Name</th>
									    	<th>Bank ABBR</th>				    		
									    	<th>Branch Name</th>
									    	<th>Bank IFSC</th>
									    	<th>Status</th>
									    	<th>Eff Date</th>
									    	<th>Addr</th>
									    	<th>City</th>
									    	<th>PIN</th>
									    	<th>State</th>
									    	<th>Country</th>
									    	<th>Phone</th>
									    	<th>Mobile</th>
									    	<th>Email</th>
									    	<th>Alt Phone</th>
									    	<th>Remarks</th>
									    	<th>Fax1</th>
									    	<th>Fax2</th>
										</tr>
									</thead>
									<tbody id="mainTbody">
									<%int j=0;int k=0;
									if(VBANK_CD.size()>0){%>
										<%for(int i=0; i<VBANK_CD.size(); i++){ 
										int size = Integer.parseInt(""+VINDEX.elementAt(i));
										%>
											<tr>
												<td align="center">
													<font title="Click to Edit" style="color:var(--header_color)">
														<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#BankModal" 
														onclick="doModify('<%=VBANK_CD.elementAt(i)%>','<%=VBANK_NAME.elementAt(i)%>',
																		'<%=VBANK_ABBR.elementAt(i)%>','<%=VBANK_BRANCH.elementAt(i)%>','<%=VEFF_DT.elementAt(i) %>',
																		'<%=VBANK_ADDR.elementAt(i) %>','<%=VBANK_CITY.elementAt(i) %>','<%=VBANK_PHONE.elementAt(i) %>',
																		'<%=VBANK_STATE.elementAt(i) %>','<%=VBANK_COUNTRY.elementAt(i) %>','<%=VBANK_PIN.elementAt(i) %>',
																		'<%=VBANK_MOBILE.elementAt(i) %>','<%=VBANK_EMAIL.elementAt(i) %>','<%=VBANK_ALT_PHONE.elementAt(i) %>',
																		'<%=VBANK_IFSC_CD.elementAt(i) %>','<%=VBANK_REMARKS.elementAt(i) %>','<%=VBANK_FAX1.elementAt(i) %>',
																		'<%=VBANK_FAX2.elementAt(i) %>','<%=VBANK_STATUS_FLAG.elementAt(i) %>','<%=VBANK_REMARKS.elementAt(i)%>');">
														</i>
													</font>
												</td>
		    									<td align="center"><%=VBANK_NAME.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_ABBR.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_BRANCH.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_IFSC_CD.elementAt(i)%></td>
		    									<td align="center">
		    										<div align="center">
														<font style="color:<%if(VBANK_STATUS_FLAG.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
															<i class="fa fa-circle fa-lg" ></i>
															&nbsp;
														</font>
														<%if(VBANK_STATUS_FLAG.elementAt(i).equals("Y")){%>
														Active
														<%}else{ %>
														Inactive
														<%} %>
													</div>
		    									</td>
		    									<td align="center"><%=VEFF_DT.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_ADDR.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_CITY.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_PIN.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_STATE.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_COUNTRY.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_PHONE.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_MOBILE.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_EMAIL.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_ALT_PHONE.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_REMARKS.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_FAX1.elementAt(i)%></td>
		    									<td align="center"><%=VBANK_FAX2.elementAt(i)%></td>
											</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="19" align="center"><%=utilmsg.infoMessage("<b>No Bank Data Available!</b>") %></td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="BankModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Bank
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
						    	<div class="col-sm-12 col-xs-12 col-md-12">
						    		<input type="text" class="form-control form-control-sm" name="bank_nm" value="" >
						    		<input type="hidden" name="bank_cd" value="">
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
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
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
									<input type="text" class="form-control form-control-sm" name="bank_abbr" value="" >
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Status</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
			      					<div class="form-check form-switch">
										<input class="form-check-input" name="active" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked onclick="setActiveInactive(this);">
									  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb">
									  		Active
									  	</label>
									  	<input type="hidden" name="status_flag" id="status_flag" value="Y">
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
						<div class="form-group row">
			    			<label class="form-label"><b>Branch</b></label>
			  			</div>
					</div>
					<div class="col-sm-4 col-xs-4 col-md-4">  
						<div class="form-group row">
			    			<div class="col-sm-12 col-xs-12 col-md-12">
								<input type="text" class="form-control form-control-sm" name="bank_branch" value="" >
			    			</div>
			  			</div>
					</div>
					<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>IFSC Code<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="bank_ifsc_code" value="" >
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Address</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="bank_addr" value="" >
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>City</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="bank_city" value="" >
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>State/Province</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="state_cd" value="" >
									<%-- <select class="form-select form-select-sm" name="state_cd">
					    				<option value="" selected="selected" >--Select--</option>
					    				<%for(int i=0;i<VSTATE_CD.size(); i++){ %>
										<option value="<%=VSTATE_NM.elementAt(i)%>"><%=VSTATE_NM.elementAt(i)%></option>
										<%} %>
					    			</select> --%>
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
				    				<select class="form-select form-select-sm" name="country_cd">
					    				<option value="" selected="selected" >--Select--</option>
					    				<%for(int i=0;i<VCOUNTRY_CD.size(); i++){ %>
										<option value="<%=VCOUNTRY_NM.elementAt(i)%>"><%=VCOUNTRY_NM.elementAt(i)%></option>
										<%} %>
					    			</select>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>PIN/ZIP Code</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="bank_pin" value="" >
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
									<input type="text" class="form-control form-control-sm" name="bank_phone" value="" >
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
									<input type="text" class="form-control form-control-sm" name="bank_alt_phone" value="" >
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
									<input type="text" class="form-control form-control-sm" name="bank_mobile" value="" >
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
									<input type="text" class="form-control form-control-sm" name="bank_email" value="" >
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Fax-1</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="fax1" value="" >
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Fax-2</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="fax2" value="" >
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
				      				<textarea class="form-control" name="remarks" cols="75" rows="3" onKeyDown="textCounter(this.form.remarks,this.form.remLen,150);" onKeyUp="textCounter(this.form.remarks,this.form.remLen,150);"></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
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

<input type="hidden" name="option" value="BANK_MST">
<input type="hidden" name="opration" value="INSERT">

<input type="hidden" name="prev_display" value="">
<input type="hidden" name="prev_display1" value="">

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