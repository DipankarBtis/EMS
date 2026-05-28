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
	var address_type = document.forms[0].address_type.value;
	var notification_type = document.forms[0].notification_type.value;
	
	var entity_role = document.forms[0].entity_role.value;
	var prev_entity_role = document.forms[0].prev_entity_role.value;
	
	if(entity_role != prev_entity_role)
	{
		counterparty_cd ="0";
		address_type="0";
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_entity_email_setup.jsp?u="+u+"&counterparty_cd="+counterparty_cd+"&entity_role="+entity_role+
			"&address_type="+address_type+"&notification_type="+notification_type;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var entity_role = document.forms[0].entity_role.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var address_type = document.forms[0].address_type.value;
	var notification_type = document.forms[0].notification_type.value;
	
	var rd_to= document.forms[0].rd_to;
	var chk= document.forms[0].chk;
	var to_flag= document.forms[0].to_flag;
	
	var msg="";
	var flag=true;
	
	if(entity_role=="" || entity_role=="0" || trim(entity_role)=="")
	{
		msg+="Select Entity!\n";
		flag=false
	}
	if(counterparty_cd=="" || counterparty_cd=="0" || trim(counterparty_cd)=="")
	{
		msg+="Select Counterparty!\n";
		flag=false
	}
	if(address_type=="" || address_type=="0" || trim(address_type)=="")
	{
		msg+="Select Address/Plant!\n";
		flag=false
	}
	if(notification_type=="" || notification_type=="0" || trim(notification_type)=="")
	{
		msg+="Select Notification Type!\n";
		flag=false
	}
	
	var count=parseInt("0");
	if(rd_to!=null && rd_to.length!=undefined)
 	{
  		for(var i=0;i<rd_to.length;i++)
  		{
  			if(rd_to[i].checked)
  			{
  				count=parseInt(count)+1;
  			}
  		}
 	}
	else if(rd_to!=null)
	{
		if(rd_to.checked)
		{
			count=parseInt(count)+1;
		}
	}
	
	if(parseInt(count)==0)
	{
		msg+="Select atleast one(1) To Contact Person for Submit!\n";
		flag=false
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Submit Entity Email Setup Detail?")
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

function setToFlag()
{
	var rd_to= document.forms[0].rd_to;
	var chk= document.forms[0].chk;
	var to_flag= document.forms[0].to_flag;
	
	if(rd_to!=null && rd_to.length!=undefined)
 	{
  		for(var i=0;i<rd_to.length;i++)
  		{
  			if(rd_to[i].checked)
  			{
  				chk[i].checked=false;
  				to_flag[i].value="Y";
  			}
  			else
  			{
  				chk[i].checked=true;
  				to_flag[i].value="N";
  			}
  		}
 	}
	else if(rd_to!=null)
	{
		if(rd_to.checked)
		{
			chk.checked=false;
			to_flag.value="Y";
		}
		else
		{
			chk.checked=true;
			to_flag.value="N";
		}
	}
}

function checkToFlag()
{
	var to_flag= document.forms[0].to_flag;
	var count=parseInt("0");
	
	if(to_flag!=null && to_flag.length!=undefined)
 	{
  		for(var i=0;i<to_flag.length;i++)
  		{
  			if(to_flag[i].value=="Y")
  			{
  				count=parseInt(count)+1;
  			}
  		}
 	}
	else if(to_flag!=null)
	{
		if(to_flag[i].value=="Y")
		{
			count=parseInt(count)+1;
		}
	}
	
	if(parseInt(count)>0)
	{
		setToFlag();
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
String address_type = request.getParameter("address_type")==null?"0":request.getParameter("address_type");
String notification_type = request.getParameter("notification_type")==null?"0":request.getParameter("notification_type");

if(entity_role.equals("B"))
{
	if(!owner_cd.equals(""))
	{
		counterparty_cd=owner_cd;
	}
}

dbcounterpty.setCallFlag("ENTITY_EMAIL_SETUP");
dbcounterpty.setCounterparty_cd(counterparty_cd);
dbcounterpty.setEntity_role(entity_role);
dbcounterpty.setComp_cd(owner_cd);
dbcounterpty.setNotification_type(notification_type);
dbcounterpty.setAddress_type(address_type);
dbcounterpty.init();

Vector VCOUNTERPARTY_CD = dbcounterpty.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcounterpty.getVCOUNTERPARTY_ABBR();
Vector VADDRESS_TYPE = dbcounterpty.getVADDRESS_TYPE();
Vector VADDRESS_NAME = dbcounterpty.getVADDRESS_NAME();

Vector VSEQ_NO = dbcounterpty.getVSEQ_NO();
Vector VPERSON_NM = dbcounterpty.getVPERSON_NM();
Vector VEFF_DT = dbcounterpty.getVEFF_DT();
Vector VTO_LIST_FLAG = dbcounterpty.getVTO_LIST_FLAG();
Vector VEMAIL = dbcounterpty.getVEMAIL();
%>
<body onload="checkToFlag();">
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
							Entity Email Setup
						</div>
					 	<div class="btn-group">
							<select class="btn btn-outline-secondary btngrp <%if(!entity_role.equals("0")){%>btnactive<%}%>" name="entity_role" onchange="refresh('INSERT')">
								<option value="0">Select Entity Roles</option>
								<option value="C">Customer</option>
				    			<option value="T">Trader</option>
				    			<option value="R">Transporter</option>
				    			<option value="V">Vessel Agent</option>
				    			<option value="H">Custom House Agent</option>
				    			<option value="S">Surveyor</option>
				    			<option value="B">Business Owner</option>
				    			<option value="G">Gas Exchange</option>
							</select>
						</div>
						<script>
							document.forms[0].entity_role.value="<%=entity_role%>"
						</script>						  	
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b>Counterparty<span class="s-red">*</span></b></label>
				    			</div>
				    			<div class="col-auto">
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh();" id="select_box">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<%if(entity_role.equals("B")){ %>
									<script>document.forms[0].counterparty_cd.style.pointerEvents = "none";</script>
									<%}%>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b>Address/Plant<span class="s-red">*</span></b></label>
				    			</div>
				    			<div class="col-auto">
				    				<select class="form-select form-select-sm" name="address_type" onchange="refresh();" id="select_box">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VADDRESS_TYPE.size();i++){ %>
										<option value="<%=VADDRESS_TYPE.elementAt(i)%>"><%=VADDRESS_NAME.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].address_type.value="<%=address_type%>"</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b>Notification Type<span class="s-red">*</span></b></label>
				    			</div>
				    			<div class="col-auto">
				    				<select class="form-select form-select-sm" name="notification_type" onchange="refresh();" id="select_box">
										<option value="0">--Select--</option>
										<option value="NOM">Nomination</option>
										<option value="JT">Join Ticket/ Allocation</option>
										<option value="INV">Invoice</option>
										<option value="FM">Force Measure</option>
										<option value="PM">Preventive Measure</option>
										<option value="OTHER">Other</option>
										<option value="RM">Remittance</option>
									</select>
									<script>document.forms[0].notification_type.value="<%=notification_type%>"</script>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Configure To and Cc List</label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Contact Person</th>
										<th>Email</th>
										<th>To</th>
										<th>Cc</th>
									</tr>
								</thead>
								<tbody>
								<%if(VSEQ_NO.size()>0){ %>
									<%for(int i=0;i<VSEQ_NO.size();i++){ %>
									<tr>
										<td align="center"><%=i+1%></td>
										<td>
											<%=VPERSON_NM.elementAt(i)%>
											<input type="hidden" name="seq_no" value="<%=VSEQ_NO.elementAt(i)%>">
											<input type="hidden" name="eff_dt" value="<%=VEFF_DT.elementAt(i)%>">
										</td>
										<td><%=VEMAIL.elementAt(i)%></td>
										<td align="center">
											<input type="checkbox" name="rd_to" <%if(VTO_LIST_FLAG.elementAt(i).equals("Y")){ %>checked<%} %>
											 onclick="setToFlag();">
											<input type="hidden" name="to_flag" value="<%=VTO_LIST_FLAG.elementAt(i)%>">
										</td>
										<td align="center">
											<input type="checkbox" class="form-check-input" name="chk" value="Y" disabled>
										</td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="5"><%=utilmsg.infoMessage("<b>Contact Person not Configured for Selected Criteria!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
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
</div>

<input type="hidden" name="option" value="ENTITY_EMAIL_SETUP">
<input type="hidden" name="prev_entity_role" value="<%=entity_role%>">

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