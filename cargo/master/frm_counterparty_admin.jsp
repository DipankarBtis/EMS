<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
			
	var u = document.forms[0].u.value;
	
	var url = "frm_counterparty_admin.jsp?counterparty_cd="+counterparty_cd+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
	
}

function doEnableDisabled(index)
{
	var counterpty_cd= document.getElementById('counterpty_cd'+index);
	var seq_no= document.getElementById('seq_no'+index);
	var aprv_note= document.getElementById('aprv_note'+index);
	var entityRole= document.getElementById('entityRole'+index);
	var approve= document.getElementById('approve'+index);
	var reject= document.getElementById('reject'+index);
	
	counterpty_cd.disabled=false;
	seq_no.disabled=false;
	aprv_note.disabled=false;
	entityRole.disabled=false;
	approve.disabled=false;
	reject.disabled=false;
	
	if(trim(document.forms[0].prev_index.value) != "")
	{
		if(document.forms[0].prev_index.value != index)
		{
			var prev_index = document.forms[0].prev_index.value;
			
			var prev_counterpty_cd= document.getElementById('counterpty_cd'+prev_index);
			var prev_seq_no= document.getElementById('seq_no'+prev_index);
			var prev_aprv_note= document.getElementById('aprv_note'+prev_index);
			var prev_entityRole= document.getElementById('entityRole'+prev_index);
			var prev_approve= document.getElementById('approve'+prev_index);
			var prev_reject= document.getElementById('reject'+prev_index);
			
			document.getElementById('rdbtn'+prev_index).checked=false;
			
			prev_counterpty_cd.disabled=true;
			prev_seq_no.disabled=true;
			prev_aprv_note.disabled=true;
			prev_entityRole.disabled=true;
			prev_approve.disabled=true;
			prev_reject.disabled=true;
		}
	}
	
	document.forms[0].prev_index.value=index;
}

function doApproveReject(index,req_flag)
{
	var counterpty_cd= document.getElementById('counterpty_cd'+index);
	var counterpty_nm= document.getElementById('counterpty_nm'+index);
	var seq_no= document.getElementById('seq_no'+index);
	var aprv_note= document.getElementById('aprv_note'+index);
	
	var approve_access = document.forms[0].approve_access.value
	
	var msg="";
	var flag=true;
	
	if(approve_access == "Y")
	{
		if(aprv_note.value == "" || trim(aprv_note.value) == "")
		{
			msg+="Please Enter Approver Note for the Selected ROW!\n";
			flag=false
		}
	}
	else
	{
		msg+="You do not have Approval Access!\n";
		flag=false
	}
	
	if(flag)
	{
		var a;
		if(req_flag=="A")
		{
			a=confirm("Do you want to Approve?")
		}
		else if(req_flag=="X")
		{
			a=confirm("Do you want to Reject?")
		}
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].counterparty_nm.value=counterpty_nm.value;
			document.forms[0].status.value=req_flag;
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}
function doSubmit()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var entity_role = document.forms[0].entity_role.value;
	var remark = document.forms[0].remark.value;
	
	var name = document.forms[0].name.value;
	document.forms[0].counterparty_nm.value=name;
	
	var msg="";
	var flag=true;
	
	if(counterparty_cd=="" || counterparty_cd=="0" || trim(counterparty_cd)=="")
	{
		msg+="Select Counterparty!\n";
		flag=false
	}
	if(entity_role=="" || entity_role=="0" || trim(entity_role)=="")
	{
		msg+="Select Entity Role!\n";
		flag=false
	}
	if(remark=="" || trim(remark)=="")
	{
		msg+="Enter Remark!\n";
		flag=false
	}
	
	if(flag)
	{
		var a = confirm("Do you want to generate Entity Role Request?")
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].status.value="R";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

var newWindow;
function openList()
{
	var url = "rpt_counterparty_list_not_des.jsp";

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
	document.forms[0].counterparty_cd.value=counterparty_cd;
	
	refresh();
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

dbcounterpty.setCallFlag("COUNTERPARTY_ADMIN");
dbcounterpty.setComp_cd(owner_cd);
dbcounterpty.setCounterparty_cd(counterparty_cd);
dbcounterpty.init();

Vector VCOUNTERPARTY_CD = dbcounterpty.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcounterpty.getVCOUNTERPARTY_ABBR();

Vector VCOUNTERPTY_CD = dbcounterpty.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_NM = dbcounterpty.getVCOUNTERPTY_NM();
Vector VENTITY_ROLE = dbcounterpty.getVENTITY_ROLE();
Vector VREMARK = dbcounterpty.getVREMARK();
Vector VREQ_BY = dbcounterpty.getVREQ_BY();
Vector VREQ_DT = dbcounterpty.getVREQ_DT();
Vector VSEQ_NO = dbcounterpty.getVSEQ_NO();

String customer=dbcounterpty.getCustomer();
String trader=dbcounterpty.getTrader();
String transporter=dbcounterpty.getTransporter();
String vessel_agent=dbcounterpty.getVessel_agent();
String custom_hagent=dbcounterpty.getCustom_hagent();
String surveyor=dbcounterpty.getSurveyor();

String name=dbcounterpty.getName();

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
							Counterparty Administration
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
			    				<div class="col-sm-12 col-xs-12 col-md-12">
			    					<span class="btn btn-info btn-sm select_btn" onclick="openList();" style="font-weight: bold;">
				    					<i class="fa fa-list-ul"></i>&nbsp;Select Counterparty
				    				</span>
			    				</div>
			    			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="name" value="<%=name%>" style="font-weight: bold;" readOnly>
				      			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Entity Role Request</label>
					</div>
					<%-- <div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Counterparty<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh();">
				    					<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
				    				</select>
				    				<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
				    			</div>
				  			</div>
						</div>
					</div> --%>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Entity Role<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="entity_role">
				    					<option value="0">--Select--</option>
				    					<option value="C" <%if(customer.equals("Y")){ %>style="background:#ffcccc;" disabled<%} %>>Customer</option>
				    					<option value="T" <%if(trader.equals("Y")){ %>style="background:#ffcccc;" disabled<%} %>>Trader</option>
				    					<option value="R" <%if(transporter.equals("Y")){ %>style="background:#ffcccc;" disabled<%} %>>Transporter</option>
				    					<option value="V" <%if(vessel_agent.equals("Y")){ %>style="background:#ffcccc;" disabled<%} %>>Vessel Agent</option>
				    					<option value="H" <%if(custom_hagent.equals("Y")){ %>style="background:#ffcccc;" disabled<%} %>>Custom House Agent</option>
				    					<option value="S" <%if(surveyor.equals("Y")){ %>style="background:#ffcccc;" disabled<%} %>>Surveyor</option>
				    				</select>
				    				<input type="hidden" class="form-control form-control-sm" name="counterparty_cd" value="<%=counterparty_cd%>">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Remark<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<textarea class="form-control form-control-sm" name="remark" cols="75" rows="3"></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Request" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Request" disabled>
						<%} %>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Entity Role Approval Process</label>
					</div>
					<div class="table-responsive">
						<table class="table table-bordered table-hover">
							<thead>
								<tr>
									<th>Sr#</th>
									<th>Counterparty</th>
									<th>Entity Role</th>
									<th>Requester Remark</th>
									<th>Request By</th>
									<th>Request Date</th>
									<th>Approver Notes<span class="s-red">*</span></th>
									<th colspan="2">Approve/Reject</th>
								</tr>
							</thead>
							<tbody>
								<%if(VCOUNTERPTY_CD.size() > 0){ %>
									<%for(int i=0;i<VCOUNTERPTY_CD.size(); i++){ %>
									<tr>
										<td>
											<div align="center">
											<input type="radio" name="rdbtn" id="rdbtn<%=i%>" onclick="doEnableDisabled('<%=i%>');">
											<%=i+1%>
											</div></td>
										<td title="<%=VCOUNTERPTY_CD.elementAt(i)%>">
											<%=VCOUNTERPTY_NM.elementAt(i)%>
											<input type="hidden" name="counterpty_cd" id="counterpty_cd<%=i%>" value="<%=VCOUNTERPTY_CD.elementAt(i)%>" disabled>
											<input type="hidden" name="counterpty_nm" id="counterpty_nm<%=i%>" value="<%=VCOUNTERPTY_NM.elementAt(i)%>" disabled>
											<input type="hidden" name="seq_no" id="seq_no<%=i%>" value="<%=VSEQ_NO.elementAt(i)%>" disabled>
										</td>
										<td>
											<div align="center">
											<%if(VENTITY_ROLE.elementAt(i).equals("C")){%>Customer
											<%}else if(VENTITY_ROLE.elementAt(i).equals("T")) {%>Trader
											<%}else if(VENTITY_ROLE.elementAt(i).equals("V")) {%>Vessel Agent
											<%}else if(VENTITY_ROLE.elementAt(i).equals("H")) {%>Custom House Agent
											<%}else if(VENTITY_ROLE.elementAt(i).equals("S")) {%>Surveyor
											<%}else if(VENTITY_ROLE.elementAt(i).equals("R")) {%>Transporter<%} %>
											<input type="hidden" name="entityRole" id="entityRole<%=i%>" value="<%=VENTITY_ROLE.elementAt(i)%>" disabled>
											</div>
										</td>
										<td style="white-space: normal;"><%=VREMARK.elementAt(i)%></td>
										<td><div align="center"><%=VREQ_BY.elementAt(i) %></div></td>
										<td><div align="center"><%=VREQ_DT.elementAt(i) %></div></td>
										<td>
											<div align="center">
												<textarea class="form-control form-control-sm"  name="aprv_note" id="aprv_note<%=i%>" rows="2" maxlength="150" disabled></textarea>
											</div>
										</td>
										<td>
											<div align="center">
											<input type="button" class="btn btn-sm config_btn" value="Approve" id="approve<%=i%>" disabled onclick="doApproveReject('<%=i%>','A');">
											</div>
										</td>
										<td>
											<div align="center">
											<input type="button" class="btn btn-danger btn-sm" style="border-radius: 50px;font-weight: bold;" value="Reject" id="reject<%=i%>" disabled onclick="doApproveReject('<%=i%>','X');">
											</div>
										</td>
									</tr>
									<%} %>
								<%}else{ %>
								<tr>
									<td colspan="8" align="center">
										<%=utilmsg.infoMessage("<b>No Request is Pending for Approval!</b>")%>
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

<input type="hidden" name="option" value="COUNTERPARTY_ADMIN">
<input type="hidden" name="counterparty_nm" value="">
<input type="hidden" name="old_value" value="">
<input type="hidden" name="prev_index" value="">
<input type="hidden" name="status" value="">

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
