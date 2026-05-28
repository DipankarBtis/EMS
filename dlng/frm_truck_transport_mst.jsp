<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Arth patel 20250102 : Form for Truck Transporter Master-->
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var u = document.forms[0].u.value;
	
	var url = "frm_truck_transport_mst.jsp?u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	var truck_trans_nm=document.forms[0].truck_trans_nm.value;
	var truck_trans_abbr=document.forms[0].truck_trans_abbr.value;
	var eff_dt=document.forms[0].eff_dt.value;
	var truck_trans_address=document.forms[0].truck_trans_address.value;
	var truck_trans_city=document.forms[0].truck_trans_city.value;
	var state_cd=document.forms[0].state_cd.value;
	
	var msg="";
	var flag=true;
	
	if(trim(truck_trans_nm)=="")
	{
		msg+="Enter Truck transporter Name!\n";
		flag=false;
	}
	if(trim(truck_trans_abbr)=="")
	{
		msg+="Enter Truck transporter Abbreviation!\n";
		flag=false;
	}
	if(trim(truck_trans_address)=="")
	{
		msg+="Enter Registered Address!\n";
		flag=false;
	}
	if(trim(eff_dt)=="")
	{
		msg+="Enter Eff Date!\n";
		flag=false;
	}
	if(trim(truck_trans_city)=="")
	{
		msg+="Enter City!\n";
		flag=false;
	}
	if(trim(state_cd)=="")
	{
		msg+="Select State!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" the Truck Transporter Details?");
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

function doModify(VTRUCK_TRANS_CD,VTRUCK_TRANS_NAME,VTRUCK_TRANS_ABBR,VEFF_DT,VADDR,VCITY,VPIN,VSTATE,VSTATUS)
{
	if(VSTATUS=="N")
	{
		document.getElementById("flexSwitchCheckChecked").checked=false;
		document.getElementById("lb").innerHTML="Inactive";
	}
	else if(VSTATUS=="Y")
	{
		document.getElementById("flexSwitchCheckChecked").checked=true;
		document.getElementById("lb").innerHTML="Active";
	}
	document.forms[0].truck_trans_cd.value=VTRUCK_TRANS_CD;
	document.forms[0].truck_trans_nm.value=VTRUCK_TRANS_NAME;
	document.forms[0].truck_trans_abbr.value=VTRUCK_TRANS_ABBR;
	document.forms[0].eff_dt.value=VEFF_DT;
	document.forms[0].truck_trans_address.value=VADDR;
	document.forms[0].truck_trans_city.value=VCITY;
	document.forms[0].truck_trans_pin.value=VPIN;
	document.forms[0].state_cd.value=VSTATE;
	document.forms[0].status_flag.value=VSTATUS;
	
	document.forms[0].opration.value="MODIFY";
}

function doClear()
{
	document.getElementById("flexSwitchCheckChecked").checked=true;
	document.forms[0].truck_trans_cd.value="";
	document.forms[0].truck_trans_nm.value="";
	document.forms[0].truck_trans_abbr.value="";
	document.forms[0].eff_dt.value="";
	document.forms[0].truck_trans_address.value="";
	document.forms[0].truck_trans_city.value="";
	document.forms[0].truck_trans_pin.value="";
	document.forms[0].state_cd.value="";
	
	var stat_cd = document.forms[0].stat_cd;
	var stat_no = document.forms[0].stat_no;
	var stat_eff_dt = document.forms[0].stat_eff_dt;
	var stat_remark = document.forms[0].stat_remark;
	if(stat_no!=null && stat_no.length!=undefined)
	{
		for(var i=0; i<stat_no.length; i++)
		{
			stat_no[i].value="";
			stat_eff_dt[i].value="";
			stat_remark[i].value="";
		}
	}
	else
	{
		if(stat_cd!=null)
		{
			stat_no.value="";
			stat_eff_dt.value="";
			stat_remark.value="";
		}
	}
	
	document.forms[0].opration.value="INSERT";
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
/*
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
} */
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Master" id="dlngmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%

dlngmaster.setCallFlag("TRUCK_TRANS_MST");
dlngmaster.init();

Vector VTRUCK_TRANS_CD = dlngmaster.getVTRUCK_TRANS_CD();
Vector VTRUCK_TRANS_NAME = dlngmaster.getVTRUCK_TRANS_NAME();
Vector VTRUCK_TRANS_ABBR = dlngmaster.getVTRUCK_TRANS_ABBR();
Vector VEFF_DT = dlngmaster.getVEFF_DT();
Vector VADDR = dlngmaster.getVADDR();
Vector VCITY = dlngmaster.getVCITY();
Vector VPIN = dlngmaster.getVPIN();
Vector VTRUCK_TRANS_STATE_CD = dlngmaster.getVTRUCK_TRANS_STATE_CD();
Vector VTRUCK_TRANS_STATE_NAME = dlngmaster.getVTRUCK_TRANS_STATE_NAME();
Vector VSTATUS = dlngmaster.getVSTATUS();
Vector VINDEX = dlngmaster.getVINDEX();
Vector VSTATE_CD = dlngmaster.getVSTATE_CD();
Vector VSTATE_NM = dlngmaster.getVSTATE_NM();

Vector VSTAT_CD = dlngmaster.getVSTAT_CD();
Vector VSTAT_NM = dlngmaster.getVSTAT_NM();
Vector VSTAT_TYPE = dlngmaster.getVSTAT_TYPE();
Vector VSTAT_STATUS = dlngmaster.getVSTAT_STATUS();
Vector VTAX_ID = dlngmaster.getVTAX_ID();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_DLNG_Master">
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
				    		Truck Transporter Master
	   	 				</div>
					 	<!-- <a href="../dlng/xls_truck_transport_mst.jsp?fileName=Truck Transporter Details.xls" download="Truck Transporter Details">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a> -->
				    </div>
				</div>
				<div class="card-body cdbody">
				<%if(write_access.equals("Y")){ %>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#TruckTransModal" onclick="doClear();">Add New Truck Transporter</label>
							</div>
						</div>
					</div><%} %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="trans">
									<thead>
										<tr>
											<%if(write_access.equals("Y")){ %><th></th><%} %>
											<th>Truck Transporter Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="name" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
									    	<th>Truck Transporter ABBR<br><div align="center"><input class="form-control form-control-sm" type="text" id="abbr" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>	
									    	<th>Status<br><div align="center"><input class="form-control form-control-sm" type="text" id="stat" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>			    		
									    	<th>Eff Date<br><div align="center"><input class="form-control form-control-sm" type="text" id="effdt" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
									    	<th>State<br><div align="center"><input class="form-control form-control-sm" type="text" id="st" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
									    	<th>TAX/ID<br><div align="center"><input class="form-control form-control-sm" type="text" id="tx" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
										</tr>
									</thead>
									<tbody id="mainTbody">
										<%int j=0;int k=0;
									if(VTRUCK_TRANS_CD.size()>0){%>
										<%for(int i=0; i<VTRUCK_TRANS_CD.size(); i++){ 
										int size = Integer.parseInt(""+VINDEX.elementAt(i));
										%>
											<tr>
												<%if(write_access.equals("Y")){ %><td align="center">
													<font title="Click to Edit" style="color:var(--header_color)">
														<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#TruckTransModal" 
														onclick="fetchTaxId('<%=VTRUCK_TRANS_CD.elementAt(i)%>');doModify('<%=VTRUCK_TRANS_CD.elementAt(i)%>','<%=VTRUCK_TRANS_NAME.elementAt(i)%>','<%=VTRUCK_TRANS_ABBR.elementAt(i)%>','<%=VEFF_DT.elementAt(i)%>','<%=VADDR.elementAt(i)%>'
														,'<%=VCITY.elementAt(i)%>','<%=VPIN.elementAt(i)%>','<%=VTRUCK_TRANS_STATE_CD.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>');">
														</i>
													</font>
												</td><%} %>
		    									<td align="center"><%=VTRUCK_TRANS_NAME.elementAt(i)%></td>
		    									<td align="center"><%=VTRUCK_TRANS_ABBR.elementAt(i)%></td>
		    									<td align="center">
		    										<div align="center">
														<font style="color:<%if(VSTATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
															<i class="fa fa-circle fa-lg" ></i>
															&nbsp;
														</font>
														<%if(VSTATUS.elementAt(i).equals("Y")){%>
														Active
														<%}else{ %>
														Inactive
														<%} %>
													</div>
		    									</td>
		    									<td align="center"><%=VEFF_DT.elementAt(i)%></td>
		    									<td align="center"><%=VTRUCK_TRANS_STATE_NAME.elementAt(i)%></td>
		    									<td align="center"><%=VTAX_ID.elementAt(i)%></td>
											</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="6" align="center"><%=utilmsg.infoMessage("<b>No Truck Transporter Data Available!</b>") %></td>
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
<div class="modal fade" id="TruckTransModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Truck Transporter Details
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" onclick="doClear()">
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
						    		<input type="text" class="form-control form-control-sm" name="truck_trans_nm" value="" onblur="isExistNM();" maxlength="100">
						    		<input type="hidden" name="truck_trans_cd" value="">
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
									<input type="text" class="form-control form-control-sm" name="truck_trans_abbr" value="" onblur="isExistABBR()" maxlength="10">
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
				    			<label class="form-label"><b>Registered Address<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="truck_trans_address" value=""  maxlength="200">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>City<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="truck_trans_city" value=""  maxlength="40">
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
				    				<select class="form-select form-select-sm" name="state_cd">
				      					<option value="">--Select--</option>
				      					<%for(int i=0; i<VSTATE_CD.size(); i++){ %>
				      						<option value="<%=VSTATE_CD.elementAt(i)%>"><%=VSTATE_NM.elementAt(i)%></option>
				      					<%} %>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>PIN/ZIP Code</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="truck_trans_pin" value="" maxlength="8" >
				    			</div>
				  			</div>
						</div>
					</div>
					<div>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<div class="row m-b-5 subheader">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="form-group row" align="left">
					    			<label class="form-label"><b>Tax/ID Detail</b></label>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th>Tax/ID Type</th>
											<th>Tax/ID Name</th>
											<th>Tax/ID No</th>
											<th>Eff Date</th>
											<th>Remark</th>
										</tr>
									</thead>
									<tbody>
										<%if(VSTAT_CD.size() > 0){ %>
											<%for(int i=0; i< VSTAT_CD.size(); i++){ %>
											<tr>
												<td>
													<div>
														<%=VSTAT_TYPE.elementAt(i)%>
														<input type="hidden" class="form-control form-control-sm" name="stat_cd" value="<%=VSTAT_CD.elementAt(i)%>">
													</div>
												</td>
												<td><div><%=VSTAT_NM.elementAt(i)%></div></td>
												<td>
													<div align="center">
														<input type="text" class="form-control form-control-sm" name="stat_no" value="" style="width:150px;">
													</div>
												</td>
												<td>
													<div align="center">
														<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="stat_eff_dt" value="" 
								      						maxLength="10" onblur="validateDate(this);" 
								      						onchange="validateDate(this);" autocomplete="off" style="width:100px;">
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      						</div>
													</div>
												</td>
												<td>
													<div align="center">
														<textarea class="form-control form-control-sm" name="stat_remark" cols="35" rows="2" maxlength="100" style="width:150px;"></textarea>
													</div>
												</td>
											</tr>
											<%}%>
										<%}else{ %>
										<tr>
											<td colspan="11" align="center">
											<%=utilmsg.infoMessage("<b>No Tax/ID Detail Configured!</b>")%>
											</td>
										</tr>
										<%} %>
								</table>
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

<input type="hidden" name="option" value="TRUCK_TRANS_MST">
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
<script>
function isExistNM()
{
	var opration = document.forms[0].opration.value;
	
	var truck_trans_cd ="0";
	if(opration == "MODIFY")
	{
		truck_trans_cd = document.forms[0].truck_trans_cd.value;
	}
	var truck_trans_nm = document.forms[0].truck_trans_nm.value;
	
	var info="";
	
	$.post("../servlet/DB_Dlng_Ajax?setCallType=IsExistNM&truck_trans_cd="+truck_trans_cd+"&truck_trans_nm="+truck_trans_nm+
			"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.TRUCK_TRANS_DTL, function(index_1, json_1) {
				if(parseInt(json_1.NAME) > 0)
				{
					info+="Name is already Exist!\n";
				}
				if(info!="")
				{
					alert(info)
					document.forms[0].truck_trans_nm.value="";
				}
			});
		});
	});
	
	return info;
}

function isExistABBR()
{
	var opration = document.forms[0].opration.value;
	
	var truck_trans_cd ="0";
	if(opration == "MODIFY")
	{
		truck_trans_cd = document.forms[0].truck_trans_cd.value;
	}
	var truck_trans_abbr = document.forms[0].truck_trans_abbr.value;
	
	var info="";
	
	$.post("../servlet/DB_Dlng_Ajax?setCallType=IsExistABBR&truck_trans_cd="+truck_trans_cd+
			"&truck_trans_abbr="+truck_trans_abbr+"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.TRUCK_TRANS_DTL, function(index_1, json_1) {
				if(parseInt(json_1.ABBR) > 0)
				{
					info+="Abbreviation is already Exist!";
				}
				
				if(info!="")
				{
					alert(info)
					document.forms[0].truck_trans_abbr.value="";
				}
			});
		});
	});
}

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("trans");
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	if (td) 
    	{
      		txtValue = td.textContent || td.innerText;
      		if (txtValue.toLocaleLowerCase().indexOf(filter) > -1) {
        		tr[i].style.display = "";
        		count++;
      		} else {
      			tr[i].style.display = "none";
      		}
    	}       
  	}
}

function fetchTaxId(truck_trans_cd)
{
	if((truck_trans_cd != "" && truck_trans_cd!="0"))
	{
		$.post("../servlet/DB_Dlng_Ajax"+ "?truck_trans_cd="+truck_trans_cd+"&setCallType=fetchTaxID", function(responseJson) {
			//console.log(responseJson);
			$.each(responseJson, function(index, json) {
				$.each(json.TAX_ID, function(index_1, json_1) {
					if(parseInt(json.TAX_ID.length) == 1)
					{
						/* if(json_1.STAT_CD == "1001")
						{
							if(json_1.STAT_NO == "")
							{
							}
							else
							{
								document.forms[0].stat_no.value=json_1.STAT_NO;
							}
							
							if(json_1.STAT_EFF_DT == "")
							{
							}
							else
							{
								document.forms[0].stat_eff_dt.value=json_1.STAT_EFF_DT;
							}
						}
						else */
						{
							document.forms[0].stat_no.value=json_1.STAT_NO;
							
							document.forms[0].stat_eff_dt.value=json_1.STAT_EFF_DT;
						}
						document.forms[0].stat_remark.value=json_1.STAT_REMARK;
					}
					else
					{
						/* if(json_1.STAT_CD == "1001")
						{
							if(json_1.STAT_NO == "")
							{
							}
							else
							{
								document.forms[0].stat_no[index_1].value=json_1.STAT_NO;
							}
							
							if(json_1.STAT_EFF_DT == "")
							{
							}
							else
							{
								document.forms[0].stat_eff_dt[index_1].value=json_1.STAT_EFF_DT;
							}
						}
						else */
						{
							document.forms[0].stat_no[index_1].value=json_1.STAT_NO;
							
							document.forms[0].stat_eff_dt[index_1].value=json_1.STAT_EFF_DT;
						}
						document.forms[0].stat_remark[index_1].value=json_1.STAT_REMARK;
					}
				});
			});
		});
	}
}
</script>
</form>
</body>
</html>