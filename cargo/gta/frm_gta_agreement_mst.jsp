<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function setActiveInactive(obj)
{
	if(obj.checked)
	{
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="Y";
	}
	else
	{
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="N";
	}
}

function doClear()
{
	document.forms[0].counterparty_cd.value="0";
	document.forms[0].agmt_name.value="";
	document.forms[0].agmt_no.value="";
	document.forms[0].agmt_rev_no.value="";
	document.forms[0].start_dt.value="";
	document.forms[0].end_dt.value="";
	document.forms[0].total_qty.value="";
	
	document.forms[0].calc_base[0].chacked=true;
	document.forms[0].calc_base[1].chacked=false;
	
	document.forms[0].active.checked=true;
	document.getElementById("lb").innerHTML="Active";
	document.getElementById("status_flag").value="Y";
	
	var entry_point_mapping = document.forms[0].entry_point_mapping;
	if(entry_point_mapping!=null && entry_point_mapping.length!=undefined)
 	{
  		for(var i=0;i<entry_point_mapping.length;i++)
  		{
   			entry_point_mapping[i].checked=false;
  		}
 	}
	else if(entry_point_mapping!=null)
	{
		entry_point_mapping.checked=false;
	}
	document.getElementById("EntryPointDisplay").innerHTML="";
	document.getElementById("EntryPointDisplay").style.display="none";
	
	var exit_point_mapping = document.forms[0].exit_point_mapping;
	if(exit_point_mapping!=null && exit_point_mapping.length!=undefined)
 	{
  		for(var i=0;i<exit_point_mapping.length;i++)
  		{
  			exit_point_mapping[i].checked=false;
  		}
 	}
	else if(exit_point_mapping!=null)
	{
		exit_point_mapping.checked=false;
	}
	
	document.getElementById("ExitPointDisplay").innerHTML="";
	document.getElementById("ExitPointDisplay").style.display="none";
		
	document.forms[0].opration.value="INSERT";
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var total_qty = document.forms[0].total_qty.value;
	var u = document.forms[0].u.value;
	var msg="";
	var flag=true;
	
	if(trim(counterparty_cd) == "" || counterparty_cd=="0")
	{
		msg+="Select Transporter!\n";
		flag=false;
	}
	if(opration=="MODIFY")
	{
		if(trim(agmt_no) == "")
		{
			msg+="Agreement No is missing!\n";
			flag=false;
		}
		if(trim(agmt_rev_no) == "")
		{
			msg+="Agreement Rev No is missing!\n";
			flag=false;
		}
	}
	if(trim(start_dt) == "")
	{
		msg+="Select Start Date!\n";
		flag=false;
	}
	if(trim(end_dt) == "")
	{
		msg+="Select End Date!\n";
		flag=false;
	}
	if(trim(total_qty) == "")
	{
		msg+="Enter Total Qty!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a;
		if(opration=="MODIFY")
		{
			a= confirm("Do you Want to Modify?");
		}
		else
		{
			a= confirm("Do you Want to Submit?");
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

function doModify(counterparty_cd,agmt,agmt_rev,start_dt,end_dt,qty,unit_cd,status,calc_base,agmt_nm)
{
	document.forms[0].counterparty_cd.value=counterparty_cd;
	document.forms[0].agmt_name.value=agmt_nm;
	document.forms[0].agmt_no.value=agmt;
	document.forms[0].agmt_rev_no.value=agmt_rev;
	document.forms[0].start_dt.value=start_dt;
	document.forms[0].end_dt.value=end_dt;
	document.forms[0].total_qty.value=qty;
	
	if(calc_base=="GCV")
	{
		document.forms[0].calc_base[0].chacked=true;
	}
	else
	{
		document.forms[0].calc_base[1].chacked=false;
	}
	if(calc_base=="NCV")
	{
		document.forms[0].calc_base[1].chacked=true;
	}
	else
	{
		document.forms[0].calc_base[0].chacked=false;
	}
	
	if(status=="Y")
	{
		document.forms[0].active.checked=true;
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="Y";
	}
	else
	{
		document.forms[0].active.checked=false;
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="N";
	}
	
	document.forms[0].opration.value="MODIFY";
}

function doSubmitEntrySel()
{
	var entry_point_mapping = document.forms[0].entry_point_mapping;
	var entry_point_nm = document.forms[0].entry_point_nm;
	
	var display ="";
	if(entry_point_mapping!=null && entry_point_mapping.length!=undefined)
 	{
  		for(var i=0;i<entry_point_mapping.length;i++)
  		{
   			if(entry_point_mapping[i].checked)
   			{
   				if(display!="")
   				{
   					display+=", "+entry_point_nm[i].value;
   				}
   				else
   				{
   					display+=entry_point_nm[i].value;
   				}
   			} 
  		} 
 	}
 	else if(entry_point_mapping!=null)
 	{
   		if(entry_point_mapping.checked)
     	{
   			if(display!="")
			{
				display+=", "+entry_point_nm.value;
			}
			else
			{
				display+=entry_point_nm.value;
			}
   		} 
 	}
	
	document.getElementById("EntryPointDisplay").innerHTML=display;
	if(display != "")
	{
		document.getElementById("EntryPointDisplay").style.display="inline";
	}
}

function doSubmitExitSel()
{
	var exit_point_mapping = document.forms[0].exit_point_mapping;
	var exit_point_nm = document.forms[0].exit_point_nm;
	
	var display ="";
	if(exit_point_mapping!=null && exit_point_mapping.length!=undefined)
 	{
  		for(var i=0;i<exit_point_mapping.length;i++)
  		{
   			if(exit_point_mapping[i].checked)
   			{
   				if(display!="")
   				{
   					display+=", "+exit_point_nm[i].value;
   				}
   				else
   				{
   					display+=exit_point_nm[i].value;
   				}
   			} 
  		} 
 	}
 	else if(exit_point_mapping!=null)
 	{
   		if(exit_point_mapping.checked)
     	{
   			if(display!="")
			{
				display+=", "+exit_point_nm.value;
			}
			else
			{
				display+=exit_point_nm.value;
			}
   		} 
 	}
	
	document.getElementById("ExitPointDisplay").innerHTML=display;
	if(display != "")
	{
		document.getElementById("ExitPointDisplay").style.display="inline";
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GtaMaster" id="gta" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
gta.setCallFlag("GTA_AGREEMENT");
gta.setComp_cd(owner_cd);
gta.init();

Vector VCOUNTERPARTY_CD = gta.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = gta.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = gta.getVCOUNTERPARTY_ABBR();

Vector VCOUNTERPTY_CD = gta.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_NM = gta.getVCOUNTERPTY_NM();
Vector VAGMT_NO = gta.getVAGMT_NO();
Vector VAGMT_REV_NO = gta.getVAGMT_REV_NO();
Vector VTOTAL_QTY = gta.getVTOTAL_QTY();
Vector VUNIT_CD = gta.getVUNIT_CD();
Vector VSTART_DT = gta.getVSTART_DT();
Vector VEND_DT = gta.getVEND_DT();
Vector VCALC_BASE = gta.getVCALC_BASE();
Vector VSTATUS = gta.getVSTATUS();
Vector VAGMT_NAME = gta.getVAGMT_NAME();

Vector VENTRY_COUNTERPTY_CD = gta.getVENTRY_COUNTERPTY_CD();
Vector VENTRY_COUNTERPTY_ABBR = gta.getVENTRY_COUNTERPTY_ABBR();
Vector VENTRY_PLANT_SEQ = gta.getVENTRY_PLANT_SEQ();
Vector VENTRY_PLANT_ABBR = gta.getVENTRY_PLANT_ABBR();
Vector VENTRY_PLANT_NM = gta.getVENTRY_PLANT_NM();
Vector VENTRY_METER_SEQ = gta.getVENTRY_METER_SEQ();
Vector VENTRY_METER_REF = gta.getVENTRY_METER_REF();
Vector VENTRY_MAPPING = gta.getVENTRY_MAPPING();
Vector VENTRY_STATUS = gta.getVENTRY_STATUS();
Vector VSEL_ENTRY_MAPPING = gta.getVSEL_ENTRY_MAPPING();

Vector VEXIT_COUNTERPTY_CD = gta.getVEXIT_COUNTERPTY_CD();
Vector VEXIT_COUNTERPTY_ABBR = gta.getVEXIT_COUNTERPTY_ABBR();
Vector VEXIT_PLANT_SEQ = gta.getVEXIT_PLANT_SEQ();
Vector VEXIT_PLANT_ABBR = gta.getVEXIT_PLANT_ABBR();
Vector VEXIT_PLANT_NM = gta.getVEXIT_PLANT_NM();
Vector VEXIT_ENTITY = gta.getVEXIT_ENTITY();
Vector VEXIT_ENTITY_NM = gta.getVEXIT_ENTITY_NM();
Vector VEXIT_MAPPING = gta.getVEXIT_MAPPING();
Vector VEXIT_STATUS = gta.getVEXIT_STATUS();
Vector VSEL_EXIT_MAPPING = gta.getVSEL_EXIT_MAPPING();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_GtaMaster">
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
				    		Gas Transportation Agreement 
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#GtaAgmtModal" 
								onclick="doClear();">Add New Agreement</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr>
											<th></th>
											<th>Transporter</th>
											<th>Agreement#</th>
											<th>Status</th>				    		
									    	<th>Contract Period</th>
									    	<th>Total MMBTU</th>
									    	<th>Calorific Base</th>
									    </tr>
									</thead>
									<tbody>
									<%if(VAGMT_NO.size()>0){ %>
										<%for(int i=0;i<VAGMT_NO.size();i++){ %>
										<tr>
											<td align="center">
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" 
													data-bs-toggle="modal" data-bs-target="#GtaAgmtModal" 
													onclick="doClear();doModify('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>',
													'<%=VSTART_DT.elementAt(i)%>','<%=VEND_DT.elementAt(i)%>','<%=VTOTAL_QTY.elementAt(i)%>',
													'<%=VUNIT_CD.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>','<%=VCALC_BASE.elementAt(i)%>',
													'<%=VAGMT_NAME.elementAt(i)%>');
													setEntryPointDtl('<%=VSEL_ENTRY_MAPPING.elementAt(i)%>');
													setExitPointDtl('<%=VSEL_EXIT_MAPPING.elementAt(i)%>');
													showEntryTblRow();"></i>
												</font>
											</td>
											<td><%=VCOUNTERPTY_NM.elementAt(i)%></td>
											<td align="center"><%=VAGMT_NAME.elementAt(i)%></td>
											<td align="center">
												<div align="center">
													<font style="color:<%if(VSTATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
														<i class="fa fa-circle fa-lg" ></i>
														&nbsp;
													</font>
													<%if(VSTATUS.elementAt(i).equals("Y")){%>
													Active
													<%}else{ %>
													In-Active
													<%} %>
												</div>
											</td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="right"><%=VTOTAL_QTY.elementAt(i)%></td>
											<td align="center"><%=VCALC_BASE.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td align="center" colspan="7"><%=utilmsg.infoMessage("<b>No GTA Agreement is Created!</b>") %></td>
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

<div class="modal fade" id="GtaAgmtModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Gas Transporter Agreement
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Transporter<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="counterparty_cd">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Agreement#</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="agmt_name" value="" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="" readOnly>
				    			</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Start Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="start_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);checkStartEndDate(this,document.forms[0].end_dt,'F');" 
			      						onchange="validateDate(this);checkStartEndDate(this,document.forms[0].end_dt,'F');" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>End Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="end_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');" 
			      						onchange="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    		</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Total Qty<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
				      					<input type="text" class="form-control form-control-sm" name="total_qty" value="" onblur="checkNumber1(this,15,3);">
				    					<span class="input-group-text"><b>MMBTU</b></span>
				    				</div>
				    				<input type="hidden" name="unit_cd" value="1">
				    			</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Calorific Base<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<input type="radio" name="calc_base" value="GCV" checked>&nbsp;GCV
				    			</div>
				    			<div class="col-auto">
									<input type="radio" name="calc_base" value="NCV">&nbsp;NCV
				    			</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Status<span class="s-red">*</span></b></label>
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
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="button" class="btn btn-sm config_btn" value="Entry Pt Config" 
					    			data-bs-toggle="modal" data-bs-target="#EntryPointModel" onclick="showEntryTblRow();">
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label" id="EntryPointDisplay" style="display:none;"></label>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="button" class="btn btn-sm config_btn" value="Exit Pt Config" 
					    			data-bs-toggle="modal" data-bs-target="#ExitPointModel">
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label" id="ExitPointDisplay" style="display:none;"></label>
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

<div class="modal fade" id="EntryPointModel" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Entry Point List
				</div>
        	</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="table-responsive">
							<table class="table table-bordered" id="example">
								<thead>
									<tr>
										<th></th>
										<th>Transporter</th>
										<th>Entity</th>
										<th>Plant/Entry Point</th>
									</tr>
								</thead>
								<tbody>
								<%if(VENTRY_COUNTERPTY_CD.size()>0){ %>
									<%for(int i=0; i<VENTRY_COUNTERPTY_CD.size(); i++){ %>
									<tr style="<%if(!VENTRY_STATUS.elementAt(i).equals("Y")) {%>color: red;<%} %> display:none;" id="row_entry_<%=VENTRY_MAPPING.elementAt(i)%>">
										<td align="center">
											<input type="checkbox" class="form-check-input" name="entry_point_mapping" value="<%=VENTRY_MAPPING.elementAt(i)%>" 
											<%if(!VENTRY_STATUS.elementAt(i).equals("Y")) {%>style="pointer-events: none;"<%} %>>
										</td>
										<td><%=VENTRY_COUNTERPTY_ABBR.elementAt(i)%></td>
										<td>Transporter</td>
										<td>
											<%=VENTRY_PLANT_NM.elementAt(i)%>
											<input type="hidden" name="entry_point_nm" value="<%=VENTRY_PLANT_NM.elementAt(i)%>">
										</td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="4" align="center"><%=utilmsg.infoMessage("<b>Entry List is not Available!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
      				</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Back" data-bs-target="#GtaAgmtModal" data-bs-toggle="modal" data-bs-dismiss="modal">
					<input type="button" class="btn btn-warning com-btn" value="Submit" 
					data-bs-target="#GtaAgmtModal" data-bs-toggle="modal" data-bs-dismiss="modal" 
					onclick="doSubmitEntrySel();">
				</div>
      		</div>
      	</div>
	</div>
</div>

<div class="modal fade" id="ExitPointModel" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Exit Point List
				</div>
        	</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="table-responsive">
							<table class="table table-bordered" id="example">
								<thead>
									<tr>
										<th></th>
										<th>Counterparty</th>
										<th>Entity</th>
										<th>Exit Point</th>
									</tr>
								</thead>
								<tbody>
								<%if(VEXIT_COUNTERPTY_CD.size()>0){ %>
									<%for(int i=0; i<VEXIT_COUNTERPTY_CD.size(); i++){ %>
									<tr <%if(!VEXIT_STATUS.elementAt(i).equals("Y")) {%>style="color: red;"<%} %>>
										<td align="center">
											<input type="checkbox" class="form-check-input" name="exit_point_mapping" value="<%=VEXIT_MAPPING.elementAt(i)%>" 
											<%if(!VEXIT_STATUS.elementAt(i).equals("Y")) {%>style="pointer-events: none;"<%} %>>
										</td>
										<td><%=VEXIT_COUNTERPTY_ABBR.elementAt(i)%></td>
										<td><%=VEXIT_ENTITY_NM.elementAt(i)%></td>
										<td>
											<%=VEXIT_PLANT_NM.elementAt(i)%>
											<input type="hidden" name="exit_point_nm" value="<%=VEXIT_PLANT_NM.elementAt(i)%>">
										</td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="4" align="center"><%=utilmsg.infoMessage("<b>Exit List is not Available!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Back" data-bs-target="#GtaAgmtModal" data-bs-toggle="modal" data-bs-dismiss="modal">
					<input type="button" class="btn btn-warning com-btn" value="Submit" 
					data-bs-target="#GtaAgmtModal" data-bs-toggle="modal" data-bs-dismiss="modal" 
					onclick="doSubmitExitSel();">
				</div>
      		</div>
      	</div>
	</div>
</div>

<input type="hidden" name="option" value="GTA_AGREEMENT_MST">
<input type="hidden" name="opration" value="INSERT">

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
function setEntryPointDtl(entryPoints)
{
	var entryPointMap=entryPoints.split("@@");
	
	var entry_point_mapping = document.forms[0].entry_point_mapping;
	
	for(var j=0;j<entryPointMap.length;j++)
	{
		if(entry_point_mapping!=null && entry_point_mapping.length!=undefined)
		{
	 		for(var i=0;i<entry_point_mapping.length;i++)
	 		{
	  			if(entry_point_mapping[i].value == entryPointMap[j])
	  			{
	  				entry_point_mapping[i].checked=true;
	  			} 
	 		} 
		}
		else if(entry_point_mapping!=null)
		{
			if(entry_point_mapping.value == entryPointMap[j])
			{
				entry_point_mapping.checked=true;
			} 
		}
	}
	
	doSubmitEntrySel()
}

function setExitPointDtl(exitPoints)
{
	var exitPointMap=exitPoints.split("@@");
				
	var exit_point_mapping = document.forms[0].exit_point_mapping;
	
	for(var j=0;j<exitPointMap.length;j++)
	{
		if(exit_point_mapping!=null && exit_point_mapping.length!=undefined)
	 	{
	  		for(var i=0;i<exit_point_mapping.length;i++)
	  		{
	   			if(exit_point_mapping[i].value == exitPointMap[j])
	   			{
	   				exit_point_mapping[i].checked=true;
	   			} 
	  		} 
	 	}
	 	else if(exit_point_mapping!=null)
	 	{
	 		if(exit_point_mapping.value == exitPointMap[j])
   			{
	 			exit_point_mapping.checked=true;
   			} 
	 	}
	}
	doSubmitExitSel();
}

function showEntryTblRow()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var entry_point_mapping = document.forms[0].entry_point_mapping;
	
	if(entry_point_mapping!=null && entry_point_mapping.length!=undefined)
 	{
  		for(var i=0;i<entry_point_mapping.length;i++)
  		{
  			var mapping=entry_point_mapping[i].value;
  			
  			var split_val=mapping.split("-");
  			
  			var trans_cd=""
  			if(mapping!="")
  			{
  				trans_cd=split_val[0];
  			}
  			
  			if(trans_cd!="" && trans_cd==counterparty_cd)
  			{
  				document.getElementById("row_entry_"+mapping).style.display="table-row";	
  			}
  			else
  			{
  				document.getElementById("row_entry_"+mapping).style.display="none";
  				entry_point_mapping[i].checked=false;
  			}
  		} 
 	}
 	else if(entry_point_mapping!=null)
 	{
 		var mapping=entry_point_mapping.value;
		
		var split_val=mapping.split("-");
		
		var trans_cd=""
		if(mapping!="")
		{
			trans_cd=split_val[0];
		}
		
		if(trans_cd!="" && trans_cd==counterparty_cd)
		{
			document.getElementById("row_entry_"+mapping).style.display="table-row";	
		}
		else
		{
			document.getElementById("row_entry_"+mapping).style.display="none";
			entry_point_mapping.checked=false;
		}
 	}
}
</script>
</form>
</body>
</html>