<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh()
{
	var financial_year=document.forms[0].financial_year.value;
	var turnover_cd =document.forms[0].turnover_cd.value;
	
	var entity_role = document.forms[0].entity_role.value;
	var prev_entity_role = document.forms[0].prev_entity_role.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_entity_turnover_dtl.jsp?u="+u+"&turnover_cd="+turnover_cd+"&financial_year="+financial_year+"&entity_role="+entity_role;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var financial_year=document.forms[0].financial_year.value;
	var turnover_cd=document.forms[0].turnover_cd.value;
	var entity_role=document.forms[0].entity_role.value;
	
	var chk=document.forms[0].chk;
	
	var msg="";
	var flag=true;
	
	if(trim(entity_role)=="" || entity_role=="0")
	{
		msg+="Select Entity!\n";
		flag=false;
	}
	if(trim(financial_year)=="" || financial_year=="0")
	{
		msg+="Select Financial Year!\n";
		flag=false;
	}
	if(trim(turnover_cd)=="" || turnover_cd=="0")
	{
		msg+="Select Turnover!\n";
		flag=false;
	}
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					if(!document.getElementById("rdY"+i).checked && !document.getElementById("rdN"+i).checked)
					{
						msg+="Select either Yes or No for Row - "+(parseInt(i)+1)+"!\n";
						flag=false;
					}
				}
			}
		}
		else
		{
			if(!document.getElementById("rdY0").checked && !document.getElementById("rdN0").checked)
			{
				msg+="Select either Yes or No for Row - 1!\n";
				flag=false;
			}
		}
	}
	else
	{
		msg+="Select atleast one(1) Row for Submit!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Submit?")
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

function CheckBoxchecked(obj,index)
{
	if(obj.checked)
	{
		document.getElementById("chkFlag"+index).value="Y";
		
		document.getElementById("chkFlag"+index).disabled=false;
		document.getElementById("index"+index).disabled=false;
		document.getElementById("counterparty_cd"+index).disabled=false;
		document.getElementById("rdY"+index).disabled=false;
		document.getElementById("rdN"+index).disabled=false;
	}
	else
	{
		document.getElementById("chkFlag"+index).value="N";
		
		document.getElementById("chkFlag"+index).disabled=true;
		document.getElementById("index"+index).disabled=true;
		document.getElementById("counterparty_cd"+index).disabled=true;
		document.getElementById("rdY"+index).disabled=true;
		document.getElementById("rdN"+index).disabled=true;
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String opration=request.getParameter("opration")==null?"MODIFY":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
String financial_year = request.getParameter("financial_year")==null?"0":request.getParameter("financial_year");
String turnover_cd = request.getParameter("turnover_cd")==null?"0":request.getParameter("turnover_cd");

dbcounterpty.setCallFlag("ENTITY_TURNOVER_ENTRY");
dbcounterpty.setEntity_role(entity_role);
dbcounterpty.setComp_cd(owner_cd);
dbcounterpty.setFinancial_year(financial_year);
dbcounterpty.setTurnover_cd(turnover_cd);
dbcounterpty.init();

Vector VFINANCIAL_YEAR = dbcounterpty.getVFINANCIAL_YEAR();

Vector VCOUNTERPARTY_CD = dbcounterpty.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcounterpty.getVCOUNTERPARTY_ABBR();
Vector VTURNOVER_FLAG = dbcounterpty.getVTURNOVER_FLAG();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_counterparty">

<div class="box-body">
	<div class="row">
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
		<div class="col-md-8 col-sm-8 col-xs-8">
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
							Entity Turnover Entry
						</div>
					 	<div class="btn-group">
							<select class="btn btn-outline-secondary btngrp <%if(!entity_role.equals("0")){%>btnactive<%}%>" name="entity_role" onchange="refresh()">
								<option value="0">Select Entity Roles</option>
								<option value="C">Customer</option>
				    			<option value="T">Trader</option>
				    			<!-- <option value="R">Transporter</option> -->
				    			<option value="B">Business Owner</option> 
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
									<label class="form-label"><b>Financial Year </b></label>
								</div>
								<div class="col-auto">
									<div class="btn-group">
										<select class="btn btn-outline-secondary subbtngrp <%if(!financial_year.equals("0")){%>btnactive<%}%>" name="financial_year" onchange="refresh()">
											<option value="0">--Select--</option>
											<%for(int i=0;i<VFINANCIAL_YEAR.size();i++){ %>
											<option value="<%=VFINANCIAL_YEAR.elementAt(i)%>"><%=VFINANCIAL_YEAR.elementAt(i)%></option>
											<%} %>
										</select>
									</div>
									<script>
										document.forms[0].financial_year.value="<%=financial_year%>"
									</script>
								</div>
				    		</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b><%if(entity_role.equals("T")){ %>TCS Certificate<%}else{%>Turnover <%} %></b></label>
								</div>
								<div class="col-auto">
									<div class="btn-group">
										<select class="btn btn-outline-secondary subbtngrp <%if(!turnover_cd.equals("0")){%>btnactive<%}%>" name="turnover_cd" onchange="refresh()">
											<option value="0">--Select--</option>
											<%if(entity_role.equals("T")){ %>
											<option value="1">TCS u/s 206C(1M)of IT Act</option>
											<%}else{ %>
											<option value="1">Above 10 Cr</option>
											<%} %>
										</select>
									</div>
									<script>
										document.forms[0].turnover_cd.value="<%=turnover_cd%>"
									</script>
								</div>
							</div>
						</div>
					</div>
					&nbsp;
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Counterparty Name</th>
										<th colspan="2">Turnover</th>
									</tr>
								</thead>
								<tbody>
								<%if(entity_role.equals("0")){ %>
									<tr>
										<td align="center" colspan="4"><%=utilmsg.infoMessage("<b>Select Entity!</b>") %></td>
									</tr>
								<%}else{ %>
									<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
									<tr>
										<td align="center" width="35px;">
											<input type="checkbox" class="form-check-input" name="chk" onclick="CheckBoxchecked(this,'<%=i%>')">&nbsp;&nbsp;<%=i+1%>
											<input type="hidden" name="chkFlag" id="chkFlag<%=i%>" value="N" disabled>
											<input type="hidden" name="index" id="index<%=i%>" value="<%=i%>" disabled> 
										</td>
										<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%> :: <%=VCOUNTERPARTY_ABBR.elementAt(i)%>">
											<%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i) %>
											<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>" disabled>
										</td>
										<td align="center">
											<input type="radio" name="turnover_flag<%=i%>" id="rdY<%=i%>" value="Y" <%if(VTURNOVER_FLAG.elementAt(i).equals("Y")){%>checked<%}%> disabled>&nbsp;&nbsp;<span>Yes</span>
										</td>
										<td align="center">
											<input type="radio" name="turnover_flag<%=i%>" id="rdN<%=i%>" value="N" <%if(VTURNOVER_FLAG.elementAt(i).equals("N")){%>checked<%}%> disabled>&nbsp;&nbsp;<span>No</span>
										</td>
									</tr>
									<%} %>
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
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
	</div>
</div>

<input type="hidden" name="option" value="ENTITY_TURNOVER_ENTRY">
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