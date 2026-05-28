<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function meterID(obj)
{
	var id = "M"+obj.value;
	document.forms[0].meter_id.value=id;
}

function setStatus(obj)
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

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	var u = document.forms[0].u.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var plant_seq = document.forms[0].plant_seq.value;
	var meter_seq = document.forms[0].meter_seq.value;
	
	var msg="";
	var flag=true;
	
	if(counterparty_cd=="0" || trim(counterparty_cd)=="")
	{
		msg+="Select Counterparty!\n";
		flag=false;
	}
	if(opration=="MODIFY")
	{
		if(meter_seq=="0" || trim(meter_seq)=="")
		{
			msg+="Meter Seq is Missing!\n";
			flag=false;
		}
	}
	if(plant_seq=="0" || trim(plant_seq)=="")
	{
		msg+="Select Plant!\n";
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
		alert(msg)
	}
}

function doModify(countpty_cd,plant_seq,meter_seq,meter_id,meter_ref,status,specifi,note)
{
	document.forms[0].counterparty_cd.value=countpty_cd;
	
	fetchPlantList(plant_seq);
	
	//document.forms[0].plant_seq.value=plant_seq;
	document.forms[0].meter_seq.value=meter_seq;
	document.forms[0].meter_id.value=meter_id;
	document.forms[0].meter_ref.value=meter_ref;
	document.forms[0].specification.value=specifi;
	document.forms[0].note.value=note;
	
	if(status=='Y')
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
	document.forms[0].counterparty_cd.style.pointerEvents = "none";
	document.forms[0].plant_seq.style.pointerEvents = "none";
	
	document.forms[0].counterparty_cd.style.background="#99ffcc";
	document.forms[0].plant_seq.style.background="#99ffcc";
	document.forms[0].meter_id.style.background="#99ffcc";
}

function doClear()
{
	document.forms[0].counterparty_cd.value='0';
	
	fetchPlantList('0');
	
	document.forms[0].plant_seq.value="0";
	document.forms[0].meter_seq.value="";
	document.forms[0].meter_id.value="";
	document.forms[0].meter_ref.value="";
	document.forms[0].specification.value="";
	document.forms[0].note.value="";
	
	document.forms[0].active.checked=true;
	document.getElementById("lb").innerHTML="Active";
	document.getElementById("status_flag").value="Y";
	
	document.forms[0].opration.value="INSERT";
	document.forms[0].counterparty_cd.style.pointerEvents = "auto";
	document.forms[0].plant_seq.style.pointerEvents = "auto";
	
	document.forms[0].counterparty_cd.style.background="";
	document.forms[0].plant_seq.style.background="";
	document.forms[0].meter_id.style.background="";
}


</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String entity_role = request.getParameter("entity_role")==null?"R":request.getParameter("entity_role");

dbmaster.setCallFlag("METER_MASTER");
dbmaster.setComp_cd(owner_cd);
dbmaster.setEntity(entity_role);
dbmaster.init();

Vector VCOUNTERPARTY_CD = dbmaster.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbmaster.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbmaster.getVCOUNTERPARTY_ABBR();

Vector VTRANSPORTER_CD = dbmaster.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = dbmaster.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_PLANT_SEQ = dbmaster.getVTRANSPORTER_PLANT_SEQ();
Vector VTRANSPORTER_PLANT_ABBR = dbmaster.getVTRANSPORTER_PLANT_ABBR();

Vector VINDEX = dbmaster.getVINDEX();
Vector VSUB_INDEX = dbmaster.getVSUB_INDEX();

Vector VMETER_SEQ = dbmaster.getVMETER_SEQ();
Vector VMETER_ID = dbmaster.getVMETER_ID();
Vector VMETER_REF = dbmaster.getVMETER_REF();
Vector VSPECIFICATION = dbmaster.getVSPECIFICATION();
Vector VNOTE = dbmaster.getVNOTE();
Vector VSTATUS = dbmaster.getVSTATUS();
Vector VMODIFY_BY = dbmaster.getVMODIFY_BY();
Vector VMODIFY_DT = dbmaster.getVMODIFY_DT();
Vector VENT_BY = dbmaster.getVENT_BY();
Vector VENT_DT = dbmaster.getVENT_DT();

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
					    	Meter Master
					    </div>
					    <div class="btn-group">
							<select class="btn btn-outline-secondary btngrp <%if(!entity_role.equals("0")){%>btnactive<%}%>" name="entity_role" onchange="refresh('MODIFY')">
								<!-- <option value="0">Select Entity Roles</option>
								<option value="C">Customer</option>
				    			<option value="T">Trader</option> -->
				    			<option value="R">Transporter</option>
				    			<!-- <option value="B">Business Owner</option> -->
							</select>
						</div>
						<script>
							document.forms[0].entity_role.value="<%=entity_role%>"
						</script>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#myModal" onclick="doClear();">Add New Meter</label>
							</div>
						</div>
					</div>
					<%int j=0,k=0,l=0,m=0;
					for(int i=0; i<VTRANSPORTER_CD.size(); i++)
					{ 
						String trans_cd=""+VTRANSPORTER_CD.elementAt(i);
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
					%>
					&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VTRANSPORTER_ABBR.elementAt(i)%></label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
						<%k=0;
						if(index > 0){ %>
							<%for(j=j;j<VTRANSPORTER_PLANT_SEQ.size(); j++) 
							{
								String trans_plant_seq=""+VTRANSPORTER_PLANT_SEQ.elementAt(j);
								int sub_index = Integer.parseInt(""+VSUB_INDEX.elementAt(j));
								k+=1;
							%>
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading<%=l%>">
    												<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
								    		<%=VTRANSPORTER_PLANT_ABBR.elementAt(j)%>
								      		</button>
								    	</h2>
								    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
								      		<div class="accordion-body accor-body">
								        		<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr>
																	<th></th>
																	<th>Meter Seq#</th>
																	<th>Meter ID</th>
																	<th>Meter Ref</th>
																	<th>Status</th>
																	<th>Specification</th>
																	<th>Note</th>
																	<th>Enter By/ Date</th>
																	<th>Modify By/ Date</th>
																</tr>
															</thead>
															<tbody>
																<%m=0;
																if(sub_index>0){ %>
																	<%for(l=l; l<VMETER_SEQ.size(); l++)
																	{ 
																		m+=1;
																	%>
																		<tr>
																			<td width="5%" align="center">
																				<font title="Click to Edit" style="color:var(--header_color)">
																					<i class="fa fa-edit fa-lg" 
																					data-bs-toggle="modal" data-bs-target="#myModal" 
																					onclick="doModify('<%=trans_cd%>','<%=trans_plant_seq%>','<%=VMETER_SEQ.elementAt(l)%>','<%=VMETER_ID.elementAt(l)%>','<%=VMETER_REF.elementAt(l)%>',
																					'<%=VSTATUS.elementAt(l)%>','<%=VSPECIFICATION.elementAt(l)%>','<%=VNOTE.elementAt(l)%>')"></i>
																				</font>
																			</td>
																			<td align="center"><%=VMETER_SEQ.elementAt(l) %></td>
																			<td align="center"><%=VMETER_ID.elementAt(l) %></td>
																			<td align="center"><%=VMETER_REF.elementAt(l) %></td>
																			<td align="center">
																				<div align="center">
																					<font style="color:<%if(VSTATUS.elementAt(l).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
																						<i class="fa fa-circle fa-lg" ></i>
																						&nbsp;
																					</font>
																					<%if(VSTATUS.elementAt(l).equals("Y")){%>
																					Active
																					<%}else{ %>
																					In-Active
																					<%} %>
																				</div>
																			</td>
																			<td align="center"><%=VSPECIFICATION.elementAt(l) %></td>
																			<td align="center"><%=VNOTE.elementAt(l) %></td>
																			<td align="center"><%=VENT_BY.elementAt(l)%><br><%=VENT_DT.elementAt(l)%></td>
																			<td align="center"><%=VMODIFY_BY.elementAt(l)%><br><%=VMODIFY_DT.elementAt(l)%></td>
																		</tr>
																		<%if(m==sub_index)
																		{%>
																			<%l=l+1;
																			break;
																		}%>
																	<%} %>
																<%} %>
															</tbody>
														</table>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<%if(k==index)
								{
									j=j+1;
									break;
								}%>
							<%} %>
						<%} %>
						</div>
					</div>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- MODEL -->
<div class="modal fade" id="myModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Meter
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Select Counterparty<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="fetchPlantList('0');">
										<option value="0">--Select--</option>
										<%for(int p=0;p<VCOUNTERPARTY_CD.size();p++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(p)%>"><%=VCOUNTERPARTY_ABBR.elementAt(p)%> - <%=VCOUNTERPARTY_NM.elementAt(p)%></option>
										<%} %>
									</select>
					    		</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Select Plant<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="plant_seq" id="plant_seq" onchange="">
									</select>
					    		</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Meter ID</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="meter_id" maxlength="20" readOnly>
									<input type="hidden" class="form-control form-control-sm" name="meter_seq">
					    		</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Meter Ref</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="meter_ref" maxlength="20">
					    		</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Specification</b></label>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<textarea class="form-control form-control-sm" name="specification" cols="75" rows="2" maxlength="100"></textarea>
					    		</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Note</b></label>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<textarea class="form-control form-control-sm" name="note" cols="75" rows="2" maxlength="250"></textarea>
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
										<input class="form-check-input" name="active" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked onclick="setStatus(this);">
									  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb">
									  		Active
									  	</label>
									  	<input type="hidden" name="status_flag" id="status_flag" value="Y">
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

<input type="hidden" name="option" value="METER_MASTER">
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
function fetchPlantList(seq)
{
	var entity_role = document.forms[0].entity_role.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	if((counterparty_cd != "" && counterparty_cd!="0") && (entity_role != "" && entity_role !="0"))
	{
		$.post("../servlet/DB_Master_Ajax"+"?counterparty_cd="+counterparty_cd+"&entity="+entity_role+"&setCallType=TransPlantDtl", function(responseJson) {
			//console.log(responseJson);
			var option = "<option value='0'>---Select---</option>"	
			$.each(responseJson, function(index, json) {
				$.each(json.PLANT_DTL, function(index_1, json_1) {
					option+="<option value="+json_1.SEQ_NO+">"+json_1.PLANT_ABBR+"</option>"
				});
			});
			document.getElementById('plant_seq').innerHTML=option;
			document.forms[0].plant_seq.value=seq;
		});
	}
}
</script>

</form>
</body>
</html>