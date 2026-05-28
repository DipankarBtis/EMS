<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script type="text/javascript">
function refresh()
{
	var entity_role = document.forms[0].entity_role.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_sap_business_area.jsp?u="+u+"&entity_role="+entity_role;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var msg="";
	var flag=true;
	
	var contract_type = document.forms[0].contract_type.value;
	var business_area = document.forms[0].business_area.value;
	var desc = document.forms[0].desc.value;
	var eff_dt = document.forms[0].eff_dt.value;
	
	if(trim(contract_type)=="")
	{
		msg+="Select Contract Type!\n";
		flag=false
	}
	
	if(trim(business_area)=="")
	{
		msg+="Enter Business Area SAP Code!\n";
		flag=false
	}
	
	if(trim(desc)=="")
	{
		msg+="Enter Business Area SAP Code description!\n";
		flag=false
	}
	
	if(trim(eff_dt)=="")
	{
		msg+="Select Effective Date!\n";
		flag=false
	}
	

	if(flag)
	{
		var a = "";		
		a = confirm("Do you want to "+opration+" SAP Business Area Detail ?")

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
	document.forms[0].contract_type.value="";
	document.forms[0].business_area.value="";
	document.forms[0].desc.value="";
	//document.forms[0].eff_dt.value=eff_dt;
	$("input[name='eff_dt']").datepicker("update", "");
	
	document.forms[0].opration.value="INSERT";
}

function doModify(contract_type,business_area,desc,eff_dt)
{
	
	document.forms[0].contract_type.value=contract_type;
	document.forms[0].business_area.value=business_area;
	document.forms[0].desc.value=desc;
	document.forms[0].old_eff_dt.value=eff_dt;
	$("input[name='eff_dt']").datepicker("update", eff_dt);
	
	document.forms[0].opration.value="MODIFY";
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sap_interface" id="sap" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");

String entity_role = request.getParameter("entity_role")==null?"C":request.getParameter("entity_role");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String business_area = request.getParameter("business_area")==null?"":request.getParameter("business_area");

sap.setCallFlag("SAP_BUSINESS_AREA_DEAL_MAP");
sap.setEntity_role(entity_role);
sap.setComp_cd(owner_cd);
sap.init();

Vector VCONTRACT_TYPE_MST = sap.getVCONTRACT_TYPE_MST();
Vector VCONTRACT_TYPE_NM_MST = sap.getVCONTRACT_TYPE_NM_MST();

Vector VCONTRACT_TYPE = sap.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = sap.getVCONTRACT_TYPE_NM();
Vector VEFF_DT = sap.getVEFF_DT();
Vector VBA_CODE = sap.getVBA_CODE();
Vector VBA_DESC = sap.getVBA_DESC();
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_sap_interface">

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
							Business Area SAP Code
						</div>
					 	<div class="btn-group">
							<select class="btn btn-outline-secondary btngrp <%if(!entity_role.equals("")){%>btnactive<%}%>" name="entity_role" onchange="refresh()">
								<option value="">Select Entity Roles</option>
								<option value="C">Customer</option>
				    			<option value="T">Trader</option>
				    			<option value="R">Transporter</option>
				    			<option value="V">Vessel Agent</option>
				    			<option value="H">Custom House Agent</option>
				    			<option value="S">Surveyor</option>
				    			<!-- <option value="B">Business Owner</option> -->
				    			<!-- <option value="G">Gas Exchange</option> -->
							</select>
						</div>
						<script>
							document.forms[0].entity_role.value="<%=entity_role%>"
						</script>						  	
					</div>
				</div>
				<div class="card-body cdbody">
					<%if(write_access.equals("Y")){ %>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#moleculelModal" onclick="doClear();">Add Business Area Code</label>
							</div>
						</div>
					</div><%} %>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Edit</th>
										<th>Contract Type</th>
										<th>Eff Date</th>
										<th>Business Area Code</th>
										<th>Description</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCONTRACT_TYPE.size() > 0){ %>
									<%for(int i=0; i<VCONTRACT_TYPE.size(); i++){ %>
									<tr>
										<td align="center">
											<font title="Click to Edit" style="color:var(--header_color)">
												<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#moleculelModal" 
												onclick="doModify('<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VBA_CODE.elementAt(i)%>','<%=VBA_DESC.elementAt(i)%>','<%=VEFF_DT.elementAt(i)%>')">
												</i>
											</font>
										</td>
										<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i)%> (<%=VCONTRACT_TYPE.elementAt(i)%>)</td>
										<td align="center"><%=VEFF_DT.elementAt(i)%></td>
										<td align="center"><%=VBA_CODE.elementAt(i)%></td>
										<td align="left"><%=VBA_DESC.elementAt(i)%></td>
									</tr>	
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="5" align="center">
											<%=utilmsg.infoMessage("<b>Business Area Mapping List not Available!</b>") %>
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
<div class="modal fade" id="moleculelModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Business Area SAP Code
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Contract Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="contract_type">
										<option value="">--Select--</option>
										<%for(int i=0;i<VCONTRACT_TYPE_MST.size();i++){ %>
										<option value="<%=VCONTRACT_TYPE_MST.elementAt(i)%>"><%=VCONTRACT_TYPE_NM_MST.elementAt(i)%> (<%=VCONTRACT_TYPE_MST.elementAt(i)%>)</option>
										<%} %>
									</select>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Business Area SAP Code<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="business_area" value="" maxLength="4">
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>					
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Description<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">    
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="desc" value="" maxLength="50">
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
				      					<input type="hidden" name="old_eff_dt" value="">
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);checkEffectiveDate(this,document.forms[0].old_eff_dt.value);" 
			      						onchange="validateDate(this);checkEffectiveDate(this,document.forms[0].old_eff_dt.value);" autocomplete="off">
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

<input type="hidden" name="option" value="SAP_BUSINESS_AREA">
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

</form>
</body>
</html>