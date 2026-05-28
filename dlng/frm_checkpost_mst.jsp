<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Arth patel 20250102 : Form for Truck type Master-->
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var u = document.forms[0].u.value;
	
	var url = "frm_checkpost_mst.jsp?u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var checkpost_name=document.forms[0].checkpost_name.value;
	var eff_dt=document.forms[0].eff_dt.value;
	var state_cd=document.forms[0].state_cd.value;
	
	
	var msg="";
	var flag=true;
	
	if(trim(checkpost_name)=="")
	{
		msg+="Enter CheckPost Name!\n";
		flag=false;
	}
	if(trim(eff_dt)=="")
	{
		msg+="Enter Eff date!\n";
		flag=false;
	}
	if(trim(state_cd)=="")
	{
		msg+="Select State!\n";
		flag=false;
	}
	
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" the CheckPost Details?");
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

function doModify(VCHECKPOST_CD,VCHECKPOST_NAME,VCHECKPOST_STATE_CD,VEFF_DT,VSTATUS)
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
	document.forms[0].checkpost_cd.value=VCHECKPOST_CD;
	document.forms[0].checkpost_name.value=VCHECKPOST_NAME;
	document.forms[0].state_cd.value=VCHECKPOST_STATE_CD;
	document.forms[0].eff_dt.value=VEFF_DT;
	document.forms[0].status_flag.value=VSTATUS;
	
	document.forms[0].opration.value="MODIFY";
}

function doClear()
{
	document.getElementById("flexSwitchCheckChecked").checked=true;
	document.forms[0].checkpost_cd.value="";
	document.forms[0].checkpost_name.value="";
	document.forms[0].state_cd.value="";
	document.forms[0].eff_dt.value="";
	
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

function validateEffDt()
{
	var eff_dt = document.forms[0].eff_dt.value;
	var sysdate = document.forms[0].sysdate.value;
	
	var spliteff_dt = eff_dt.split("/");
	var splitsysdate = sysdate.split("/");
	
	var temp_eff_dt = spliteff_dt[2]+spliteff_dt[1]+spliteff_dt[0];
	var temp_sysdate = splitsysdate[2]+splitsysdate[1]+splitsysdate[0];
	
	if(eff_dt!="")
	{
		if(temp_eff_dt > temp_sysdate) //to comapre with System date....
		{
			alert("Eff date ("+eff_dt+") should be less than or equals to System date ("+sysdate+") !");
			document.forms[0].eff_dt.value="";
			return false;
		}
	}
}


</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Master" id="dlngmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
dlngmaster.setCallFlag("CHECKPOST_MST");
dlngmaster.init();

Vector VCHECKPOST_CD = dlngmaster.getVCHECKPOST_CD();
Vector VCHECKPOST_NAME = dlngmaster.getVCHECKPOST_NAME();
Vector VCHECKPOST_STATE_CD = dlngmaster.getVCHECKPOST_STATE_CD();
Vector VCHECKPOST_STATE_NM = dlngmaster.getVCHECKPOST_STATE_NM();
Vector VEFF_DT = dlngmaster.getVEFF_DT();
Vector VSTATUS = dlngmaster.getVSTATUS();
Vector VSTATE_CD = dlngmaster.getVSTATE_CD();
Vector VSTATE_NM = dlngmaster.getVSTATE_NM();

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
				    		CheckPost Master
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
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#TruckTypeModal" onclick="doClear();">Add New CheckPost</label>
							</div>
						</div>
					</div><%} %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="checkpost_mst">
									<thead>
										<tr>
											<%if(write_access.equals("Y")){ %><th></th><%} %>
											<th>CheckPost Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="name" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
											<th>State<br><div align="center"><input class="form-control form-control-sm" type="text" id="state" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
									    	<th>Eff Date<br><div align="center"><input class="form-control form-control-sm" type="text" id="effdt" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
									    	<th>Status</th>			    		
										</tr>
									</thead>
									<tbody id="mainTbody">
									<%int j=0;int k=0;
									if(VCHECKPOST_CD.size()>0){%>
										<%for(int i=0; i<VCHECKPOST_CD.size(); i++){
										%>
											<tr>
												<%if(write_access.equals("Y")){ %><td align="center">
													<font title="Click to Edit" style="color:var(--header_color)">
														<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#TruckTypeModal" 
														onclick="doModify('<%=VCHECKPOST_CD.elementAt(i)%>','<%=VCHECKPOST_NAME.elementAt(i) %>','<%=VCHECKPOST_STATE_CD.elementAt(i) %>','<%=VEFF_DT.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>');">
														</i>
													</font>
												</td><%} %>
		    									<td align="center"><%=VCHECKPOST_NAME.elementAt(i)%></td>
		    									<td align="center"><%=VCHECKPOST_STATE_NM.elementAt(i) %></td>
		    									<td align="center"><%=VEFF_DT.elementAt(i)%></td>
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
											</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="5" align="center"><%=utilmsg.infoMessage("<b>No CheckPost Data Available!</b>") %></td>
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
<div class="modal fade" id="TruckTypeModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify CheckPost Details
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" onclick="doClear()">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>CheckPost Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
						    	<div class="col-sm-12 col-xs-12 col-md-12">
						    		<input type="text" class="form-control form-control-sm" name="checkpost_name" value="" onBlur="isExistCheckpost();" maxlength="50">
						    		<input type="hidden" name="checkpost_cd" value="">
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
				    			<label class="form-label"><b>State<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="state_cd" onBlur="isExistCheckpost();">
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
				    			<label class="form-label"><b>Eff Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);validateEffDt()" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
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

<input type="hidden" name="option" value="CHECKPOST_MST">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="sysdate" value="<%=sysdate%>">

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
function isExistCheckpost()
{
	var opration = document.forms[0].opration.value;
	
	var checkpost_name = document.forms[0].checkpost_name.value;
	var state_cd = document.forms[0].state_cd.value;
	
	var info="";
	
	$.post("../servlet/DB_Dlng_Ajax?setCallType=IsExistCheckpost&checkpost_name="+checkpost_name+"&state_cd="+state_cd+
			"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.CHECKPOST_DTL, function(index_1, json_1) {
				if(parseInt(json_1.CHECKPOST) > 0)
				{
					info+="Checkpost Name is already Exist for Selected State!\n";
				}
				if(info!="")
				{
					alert(info)
					document.forms[0].checkpost_name.value="";
				}
			});
		});
	});
	
	return info;
}

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("checkpost_mst");
  	
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
</script>

</form>
</body>
</html>