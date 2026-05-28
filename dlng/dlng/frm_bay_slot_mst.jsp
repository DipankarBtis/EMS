<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Arth patel 20250120 : Form for Bay and Slot Master-->
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh(opration)
{
	var fill_station = document.forms[0].fill_station.value;
	var u = document.forms[0].u.value;
	var url = "frm_bay_slot_mst.jsp?opration="+opration+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var bay_name=document.forms[0].bay_name.value;
	var slot_cald_type=document.forms[0].slot_cald_type.value;
	var eff_dt=document.forms[0].eff_dt.value;
	var slot_start_time=document.forms[0].slot_start_time.value;
	var slot_interval=document.forms[0].slot_interval.value;
	var fill_station=document.forms[0].fill_station.value;
	
	var msg="";
	var flag=true;
	
	if(trim(fill_station)=="")
	{
		msg+="Select Filling station!\n";
		flag=false;
	}
	if(trim(bay_name)=="")
	{
		msg+="Enter Bay Name!\n";
		flag=false;
	}
	if(trim(eff_dt)=="")
	{
		msg+="Enter Effective date!\n";
		flag=false;
	}
	if(trim(slot_cald_type)=="0")
	{
		msg+="Select Slot calendar Type!\n";
		flag=false;
	}
	if(trim(slot_start_time)=="")
	{
		msg+="Enter Slot Start Time!\n";
		flag=false;
	}
	if(trim(slot_interval)=="")
	{
		msg+="Enter Slot Interval!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" the Bay and Slot Details?");
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

function doModify(VBAY_CD,VBAY_NAME,VEFF_DT,VSTATUS,VSLOT_CALD_TYPE,VSLOT_START_TIME,VSLOT_INTERVAL,VFILLING_STATION_CD)
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
	document.forms[0].bay_cd.value=VBAY_CD;
	document.forms[0].bay_name.value=VBAY_NAME;
	document.forms[0].eff_dt.value=VEFF_DT;
	document.forms[0].status_flag.value=VSTATUS;
	document.forms[0].slot_cald_type.value=VSLOT_CALD_TYPE;
	document.forms[0].slot_start_time.value=VSLOT_START_TIME;
	document.forms[0].slot_interval.value=VSLOT_INTERVAL;
	document.forms[0].fill_station.value=VFILLING_STATION_CD;
	
	
	document.forms[0].opration.value="MODIFY";
}

function doClear()
{
	document.getElementById("flexSwitchCheckChecked").checked=true;
	document.forms[0].bay_name.value="";
	document.forms[0].eff_dt.value="";
	document.forms[0].slot_cald_type.value="0";
	document.forms[0].slot_start_time.value="";
	document.forms[0].slot_interval.value="03:00";
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

function setSlotStartTime(type)
{
	var slot_type = type.value;
	
	if(slot_type=="C")
	{
		document.forms[0].slot_start_time.value="00:00";
		document.getElementById('slot_start_tm').readOnly = true; 
	}
	else if(slot_type=="G")
	{
		document.forms[0].slot_start_time.value="06:00";
		document.getElementById('slot_start_tm').readOnly = true; 
	}
	else if(slot_type=="U")
	{
		document.forms[0].slot_start_time.value="";
		document.getElementById('slot_start_tm').readOnly = false; 
	}
	else
	{
		document.forms[0].slot_start_time.value="";
		document.getElementById('slot_start_tm').readOnly = true;
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
String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String sysdate=utildate.getSysdate();
dlngmaster.setCallFlag("BAY_MST");
dlngmaster.setOpration(opration);
dlngmaster.setComp_cd(owner_cd);
dlngmaster.init();

Vector VFILL_STATION_CD = dlngmaster.getVFILL_STATION_CD();
Vector VFILL_STATION_NM = dlngmaster.getVFILL_STATION_NM();
Vector VFILL_STATION_ABBR = dlngmaster.getVFILL_STATION_ABBR();
Vector VEFF_DT = dlngmaster.getVEFF_DT();
Vector VSTATUS = dlngmaster.getVSTATUS();
Vector VFILLING_STATION_CD = dlngmaster.getVFILLING_STATION_CD();
Vector VFILLING_STATION_NAME = dlngmaster.getVFILLING_STATION_NAME();
Vector VBAY_CD = dlngmaster.getVBAY_CD();
Vector VBAY_NAME = dlngmaster.getVBAY_NAME();
Vector VSLOT_CALD_TYPE = dlngmaster.getVSLOT_CALD_TYPE();
Vector VSLOT_START_TIME = dlngmaster.getVSLOT_START_TIME();
Vector VSLOT_INTERVAL = dlngmaster.getVSLOT_INTERVAL();

String slot_interval="03:00";
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
				    		Bay and Slot Master
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
								<label class="btn btn-outline-secondary subbtngrp" 
									data-bs-toggle="modal" 
									data-bs-target="#addFillingStation"
									onclick="doClear();">Add New Bay and Slot</label>
							</div>
						</div>
					</div>
					<%} %>
					<div class="row m-b-5">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="bay_master">
								<thead>
									<tr>
										<th></th>
										<th>Fill Station<br><div align="center"><input class="form-control form-control-sm" type="text" id="fi_nm" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
										<th>Bay Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="name" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
										<th>Effective Date</th>
										<th>Status<br><div align="center"><input class="form-control form-control-sm" type="text" id="st" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
										<th>Slot Calendar Type<br><div align="center"><input class="form-control form-control-sm" type="text" id="ty" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
										<th>Slot Start Time<br><div align="center"><input class="form-control form-control-sm" type="text" id="time" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
										<th>Slot Interval<br><div align="center"><input class="form-control form-control-sm" type="text" id="int" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
									</tr>
								</thead>
								<tbody>
									<%if(VBAY_CD.size()>0){ %>
										<%for(int i=0; i<VBAY_CD.size(); i++){ %>
										<tr>
										<td align="center">
											<%if(write_access.equals("Y")){ %>
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#addFillingStation" 
													onclick="doModify('<%=VBAY_CD.elementAt(i)%>','<%=VBAY_NAME.elementAt(i) %>','<%=VEFF_DT.elementAt(i) %>','<%=VSTATUS.elementAt(i) %>'
													,'<%=VSLOT_CALD_TYPE.elementAt(i) %>','<%=VSLOT_START_TIME.elementAt(i) %>','<%=VSLOT_INTERVAL.elementAt(i) %>','<%=VFILLING_STATION_CD.elementAt(i)%>');">
													</i>
												</font>
												<%} %>
											</td>
											<td align="center"><%=VFILLING_STATION_NAME.elementAt(i) %></td>
											<td align="center"><%=VBAY_NAME.elementAt(i) %></td>
											<td align="center"><%=VEFF_DT.elementAt(i) %></td>
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
	    									<td align="center">
	    										<%if(VSLOT_CALD_TYPE.elementAt(i).equals("C")){%>
													Calendar Day
													<%}else if(VSLOT_CALD_TYPE.elementAt(i).equals("G")){ %>
													Gas Day
													<%}else{ %>
													Custom Day
													<%} %>
	    									</td>
	    									<td align="center"><%=VSLOT_START_TIME.elementAt(i) %></td>
	    									<td align="center"><%=VSLOT_INTERVAL.elementAt(i) %></td>
										</tr>
										<%} %>
									<%}else{ %>
									<tr>
										<td colspan="8" align="center">
										<%=utilmsg.infoMessage("<b>No Bay and Slot Configured!</b>")%>
										</td>
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
<div class="modal fade" id="addFillingStation" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Bay and Slot Details
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" onclick="doClear()">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Fill Station<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="fill_station">
										<option value="">--Select--</option>
										<%for(int i=0;i<VFILL_STATION_CD.size();i++){ %>
										<option value="<%=VFILL_STATION_CD.elementAt(i)%>"><%=VFILL_STATION_ABBR.elementAt(i)%> - <%=VFILL_STATION_NM.elementAt(i)%></option>
										<%} %>
									</select>
				    			</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Bay Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
						    	<div class="col-sm-12 col-xs-12 col-md-12">
						    		<input type="text" class="form-control form-control-sm" name="bay_name" value="" maxlength="100">
						    		<input type="hidden" name="bay_cd" value="">
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
				    			<label class="form-label"><b>Effective Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);validateEffDt();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Slot Calendar Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="slot_cald_type" >
										<option value="0">--Select--</option>
										<option value="C">Calendar Day</option>
										<option value="G">Gas Day</option>
										<!-- <option value="U">Custom Day</option> -->
									</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Slot Start Time<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="slot_start_time" id="slot_start_tm" value="" maxLength="5" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off" >
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<!-- HM : Currently max allowed interval is 24:00, if needed to increase will need to handle impact in NOM/SCH/ALLOC -->
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Slot Interval<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="slot_interval" value="<%=slot_interval %>" maxLength="5" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
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

<input type="hidden" name="option" value="BAY_MST">
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

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("bay_master");
  	
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