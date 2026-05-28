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

enableButton = true;
function doSubmit()
{
	var entity_role = document.forms[0].entity_role.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var address_type = document.forms[0].address_type.value;
	var notification_type = document.forms[0].notification_type.value;
	
	var contact_items_no= document.forms[0].contact_items_no.value;
	var dlng_contact_items_no= document.forms[0].dlng_contact_items_no.value;
	
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
	
	if(entity_role=="C")
	{
		var to_flagContainsY = false;  // To track if 'Y' is selected in to_flag
	    var dlng_to_flagContainsY = false;
		
		if(parseInt(contact_items_no)>0)
		{
			var to_flag= document.forms[0].to_flag;
			
			if (parseInt(contact_items_no)==1)
			{
				var toFlag = document.getElementById("to_flag0");
				
				if (toFlag.value === 'Y')
				{
		            to_flagContainsY = true;
		        }
			} 
			else
			{
				for (var i = 0; i < to_flag.length; i++)
				{
					if (to_flag[i].value === 'Y')
					{
			            to_flagContainsY = true;
			            break;  // Exit loop once 'Y' is found
			        }
			    }
			}
			
			if (!to_flagContainsY)
			{
				msg+="Select Atleast One Person with 'To' Configuration!\n";
		        flag = false;
		    }
		}
		
		if(parseInt(dlng_contact_items_no)>0)
		{
			var dlng_to_flag= document.forms[0].dlng_to_flag;
			
			if (parseInt(dlng_contact_items_no)==1)
			{
				 var toFlag = document.getElementById("dlng_to_flag0");
				 
				if (toFlag.value === 'Y')
				{
		            dlng_to_flagContainsY = true;
		        }
			}
			else
			{
				for (var i = 0; i < dlng_to_flag.length; i++)
				{
					if (dlng_to_flag[i].value === 'Y')
					{
			            dlng_to_flagContainsY = true;
			            break;  // Exit loop once 'Y' is found
			        }
			    }
			}
			
			if (!dlng_to_flagContainsY)
			{
				msg+="Select Atleast One Person with 'To' DLNG Configuration!\n";
		        flag = false;
		    }
		}
		
		if(parseInt(contact_items_no)==0 && parseInt(dlng_contact_items_no)==0)
		{
			msg+="No DLNG/Configuration List Available!\n";
	        flag = false;
		}
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Submit Entity Email Setup Detail?")
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			editAllowedOnCpStatus = true;
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function setToFlag(rowIndex,rd)
{
	var entity_role = document.forms[0].entity_role.value;
    var toFlag = document.getElementById("to_flag" + rowIndex);
    var radio_to = document.getElementById("rd_to" + rowIndex);
    var radio_cc = document.getElementById("rd_cc" + rowIndex);
    var radio_bcc = document.getElementById("rd_bcc" + rowIndex);
    
    if (rd == 'Y') 
    {
        radio_cc.checked = false;
        if(entity_role == "B")
    	{
        	radio_bcc.checked = false;
    	}
        toFlag.value = 'Y';
    }
    else if (rd == 'N')
    {
        radio_to.checked = false;
        if(entity_role == "B")
    	{
        	radio_bcc.checked = false;
    	}
        toFlag.value = 'N';
    }
    else if (rd == 'B')
    {
        radio_to.checked = false;
        radio_cc.checked = false;
        toFlag.value = 'B';
    }
}

function setDlngToFlag(rowIndex,rd)
{
	var entity_role = document.forms[0].entity_role.value;
    var toFlag = document.getElementById("dlng_to_flag" + rowIndex);
    var radio_to = document.getElementById("dlng_rd_to" + rowIndex);
    var radio_cc = document.getElementById("dlng_rd_cc" + rowIndex);
    var radio_bcc = document.getElementById("dlng_rd_bcc" + rowIndex);
    
    if (rd == 'Y') 
    {
        radio_cc.checked = false;
        if(entity_role == "B")
    	{
        	radio_bcc.checked = false;
    	}
        toFlag.value = 'Y';
    }
    else if (rd == 'N')
    {
        radio_to.checked = false;
        if(entity_role == "B")
    	{
        	radio_bcc.checked = false;
    	}
        toFlag.value = 'N';
    }
    else if (rd == 'B')
    {
        radio_to.checked = false;
        radio_cc.checked = false;
        toFlag.value = 'B';
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

Vector VDLNG_SEQ_NO = dbcounterpty.getVDLNG_SEQ_NO();
Vector VDLNG_PERSON_NM = dbcounterpty.getVDLNG_PERSON_NM();
Vector VDLNG_EFF_DT = dbcounterpty.getVDLNG_EFF_DT();
Vector VDLNG_TO_LIST_FLAG = dbcounterpty.getVDLNG_TO_LIST_FLAG();
Vector VDLNG_EMAIL = dbcounterpty.getVDLNG_EMAIL();

Vector VNOT_TYPE = new Vector();
Vector VNOT_TYPE_NM = new Vector();

if(entity_role.equals("C"))
{
	VNOT_TYPE.add("NOM");
	VNOT_TYPE.add("JT");
	VNOT_TYPE.add("INV");
	
	VNOT_TYPE_NM.add("Nomination");
	VNOT_TYPE_NM.add("Join Ticket/ Allocation");
	VNOT_TYPE_NM.add("Invoice");
}
else if(entity_role.equals("T") || entity_role.equals("R"))
{
	VNOT_TYPE.add("NOM");
	VNOT_TYPE.add("JT");
	VNOT_TYPE.add("RM");
	
	VNOT_TYPE_NM.add("Nomination");
	VNOT_TYPE_NM.add("Join Ticket/ Allocation");
	VNOT_TYPE_NM.add("Remittance");
}
else if(entity_role.equals("V") || entity_role.equals("S") || entity_role.equals("H"))
{
	VNOT_TYPE.add("RM");
	VNOT_TYPE_NM.add("Remittance");
}
else if(entity_role.equals("B"))
{
	VNOT_TYPE.add("NOM");
	VNOT_TYPE.add("JT");
	VNOT_TYPE.add("INV");
	VNOT_TYPE.add("RM");
	VNOT_TYPE.add("F402");
	
	VNOT_TYPE_NM.add("Nomination");
	VNOT_TYPE_NM.add("Join Ticket/ Allocation");
	VNOT_TYPE_NM.add("Invoice");
	VNOT_TYPE_NM.add("Remittance");
	VNOT_TYPE_NM.add("Form 402");
}
else if(entity_role.equals("G"))
{
	VNOT_TYPE.add("NOM");
	VNOT_TYPE.add("JT");
	VNOT_TYPE.add("INV");
	VNOT_TYPE.add("RM");
	
	VNOT_TYPE_NM.add("Nomination");
	VNOT_TYPE_NM.add("Join Ticket/ Allocation");
	VNOT_TYPE_NM.add("Invoice");
	VNOT_TYPE_NM.add("Remittance");
}

%>
<body onload="">
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
										<%for(int i=0;i<VNOT_TYPE.size();i++){ %>
										<option value="<%=VNOT_TYPE.elementAt(i)%>"><%=VNOT_TYPE_NM.elementAt(i)%></option>
										<%} %>
										<option value="FM">Force Measure</option>
										<option value="PM">Preventive Measure</option>
										<option value="OTHER">Other</option>
										<!-- <option value="NOM">Nomination</option>
										<option value="JT">Join Ticket/ Allocation</option>
										<option value="INV">Invoice</option>
										<option value="FM">Force Measure</option>
										<option value="PM">Preventive Measure</option>
										<option value="OTHER">Other</option>
										<option value="RM">Remittance</option> -->
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
					<%if(!entity_role.equals("0")){ %>
					<%if(!notification_type.equals("F402")){%>
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
										<%if(entity_role.equals("B")){%>
										<th>Bcc</th>
										<%} %>
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
										    <input type="radio" name="rd_to<%=i %>" id="rd_to<%=i %>"
										        <% if(VTO_LIST_FLAG.elementAt(i).equals("Y")) { %> class="form-check-input" checked<% } %>
										        onchange="setToFlag(<%=i%>,'Y');" 
										        <% if(entity_role.equals("B") && (!notification_type.equals("RM") && !notification_type.equals("F402"))) { %>disabled<% } %> >
										    <input type="hidden" name="to_flag" id="to_flag<%=i%>" value="<%=VTO_LIST_FLAG.elementAt(i)%>">
										</td>
										<td align="center">
										    <input type="radio" name="rd_cc<%=i %>" id="rd_cc<%=i %>"
										        <% if(VTO_LIST_FLAG.elementAt(i).equals("N")) { %> class="form-check-input" checked<% } %>
										        <% if(VTO_LIST_FLAG.elementAt(i).equals("")) { %> checked<% } %>
										        onchange="setToFlag(<%=i%>,'N');">
										</td>
										<% if(entity_role.equals("B")) { %>
										<td align="center">
										    <input type="radio" name="rd_bcc<%=i %>" id="rd_bcc<%=i %>"
										        <% if(VTO_LIST_FLAG.elementAt(i).equals("B")) { %> class="form-check-input" checked<% } %>
										        onchange="setToFlag(<%=i%>,'B');">
										</td>
										<% } %>
																				
										<%-- <td align="center">
											<input type="radio" class="form-check-input" name="rd_to" <%if(VTO_LIST_FLAG.elementAt(i).equals("Y")){ %>checked<%} %>
											 onclick="setToFlag(<%=i%>);" <%if(entity_role.equals("B") && !notification_type.equals("RM")) {%>disabled<%} %>>
											<input type="hidden" name="to_flag" value="<%=VTO_LIST_FLAG.elementAt(i)%>">
										</td>
										<td align="center">
											<input type="radio" class="form-check-input" name="chk" value="Y" <%if(VTO_LIST_FLAG.elementAt(i).equals("N")){ %>checked<%} %>
											onclick="setToFlag(<%=i%>);">
										</td>
										<%if(entity_role.equals("B")){%>
										<td align="center">
											<input type="radio" class="form-check-input" name="bcc_chk" value="Y" onclick="setToFlag(<%=i%>);"
											<%if(VTO_LIST_FLAG.elementAt(i).equals("B")){ %>checked<%} %>>
										</td>
										<%} %> --%>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="6"><%=utilmsg.infoMessage("<b>Contact Person not Configured for Selected Criteria!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<%} %>
					<%if(entity_role.equals("C") || entity_role.equals("B") || entity_role.equals("T") || entity_role.equals("G")){ %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> DLNG Configure To and Cc List</label>
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
										<%if(entity_role.equals("B")){%>
										<th>Bcc</th>
										<%} %>
									</tr>
								</thead>
								<tbody>
								<%if(VDLNG_SEQ_NO.size()>0){ %>
									<%for(int i=0;i<VDLNG_SEQ_NO.size();i++){ %>
									<tr>
										<td align="center"><%=i+1%></td>
										<td>
											<%=VDLNG_PERSON_NM.elementAt(i)%>
											<input type="hidden" name="dlng_seq_no" value="<%=VDLNG_SEQ_NO.elementAt(i)%>">
											<input type="hidden" name="dlng_eff_dt" value="<%=VDLNG_EFF_DT.elementAt(i)%>">
										</td>
										<td><%=VDLNG_EMAIL.elementAt(i)%></td>
										<td align="center">
										    <input type="radio" name="dlng_rd_to<%=i %>" id="dlng_rd_to<%=i %>"
										        <% if(VDLNG_TO_LIST_FLAG.elementAt(i).equals("Y")) { %> class="form-check-input" checked<% } %>
										        onchange="setDlngToFlag(<%=i%>,'Y');" 
										        <% if(entity_role.equals("B") && (!notification_type.equals("RM") && !notification_type.equals("F402"))) { %>disabled<% } %> >
										    <input type="hidden" name="dlng_to_flag" id="dlng_to_flag<%=i%>" value="<%=VDLNG_TO_LIST_FLAG.elementAt(i)%>">
										</td>
										<td align="center">
										    <input type="radio" name="dlng_rd_cc<%=i %>" id="dlng_rd_cc<%=i %>"
										        <% if(VDLNG_TO_LIST_FLAG.elementAt(i).equals("N")) { %>checked class="form-check-input" <% } %>
										        <% if(VDLNG_TO_LIST_FLAG.elementAt(i).equals("")) { %>checked<% } %>
										        onchange="setDlngToFlag(<%=i%>,'N');">
										</td>
										<% if(entity_role.equals("B")) { %>
										<td align="center">
										    <input type="radio" name="dlng_rd_bcc<%=i %>" id="dlng_rd_bcc<%=i %>"
										        <% if(VDLNG_TO_LIST_FLAG.elementAt(i).equals("B")) { %> class="form-check-input" checked<% } %>
										        onchange="setDlngToFlag(<%=i%>,'B');">
										</td>
										<% } %>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="6"><%=utilmsg.infoMessage("<b>Contact Person not Configured for Selected Criteria!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<%} %>
				<%}else{ %>
					<div align="center"><%=utilmsg.infoMessage("<b>Select Entity to configure To and CC Details!</b>")%></div>
				<%} %>
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
<input type="hidden" name="contact_items_no" value="<%=VSEQ_NO.size()%>">
<input type="hidden" name="dlng_contact_items_no" value="<%=VDLNG_SEQ_NO.size()%>">

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