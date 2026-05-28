<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var rate_mode = document.forms[0].rate_mode.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_rate_master.jsp?rate_mode="+rate_mode+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setActiveInactive(obj)
{
	if(obj.checked)
	{
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("rate_flag").value="Y";
	}
	else
	{
		document.getElementById("lb").innerHTML="Inactive";
		document.getElementById("rate_flag").value="N";
	}
}

function doModify(rate_cd,rate_nm,bank_abbr,rate_flag,component_flag,component1,component2)
{
	document.forms[0].rate_cd.value=rate_cd;
	document.forms[0].rate_nm.value=rate_nm;
	document.forms[0].bank_abbr.value=bank_abbr;
	
	if(rate_flag=='Y')
	{
		document.forms[0].active.checked=true;
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("rate_flag").value="Y";
	}
	else
	{
		document.forms[0].active.checked=false;
		document.getElementById("lb").innerHTML="Inactive";
		document.getElementById("rate_flag").value="N";
	}
	if(component_flag=="Y")
	{
		document.forms[0].comp_rate_flag.checked = true;
		
		document.forms[0].component1.value=component1
		document.forms[0].component2.value=component2
		
		document.forms[0].component1.style.pointerEvents = "auto";
		document.forms[0].component2.style.pointerEvents = "auto";
	}
	else
	{
		document.forms[0].comp_rate_flag.checked = false;
		
		document.forms[0].component1.value=""
		document.forms[0].component2.value=""
		
		document.forms[0].component1.style.pointerEvents = "none";
		document.forms[0].component2.style.pointerEvents = "none";
	}
	
	document.forms[0].opration.value="MODIFY";
}

function doClear()
{
	document.forms[0].rate_cd.value="";
	document.forms[0].rate_nm.value="";
	document.forms[0].bank_abbr.value="";
	
	document.forms[0].active.checked=true;
	document.getElementById("lb").innerHTML="Active";
	document.getElementById("rate_flag").value="Y";
	
	document.forms[0].comp_rate_flag.checked = false;
	
	document.forms[0].component1.value=""
	document.forms[0].component2.value=""
	
	document.forms[0].component1.style.pointerEvents = "none";
	document.forms[0].component2.style.pointerEvents = "none";
	
	document.forms[0].opration.value="INSERT";
}

function doSubmit()
{
	var rate_mode = document.forms[0].rate_mode.value;
	var opration = document.forms[0].opration.value;
	
	var rate_cd = document.forms[0].rate_cd;
	var rate_nm = document.forms[0].rate_nm;
	var rate_flag = document.forms[0].rate_flag;
	
	var flag = true;
	var msg = "";
	
	if(rate_mode == "0")
	{
		flag=false;
		msg+="Please Select Rate Mode!";
	}
	else
	{
		if(opration=="MODIFY")
		{
			if(rate_cd.value == "" || trim(rate_cd.value) == "")
			{
				flag=false;
				msg+="Rate CD Missing!\n";
			}
		}
		if(rate_nm.value == "" || trim(rate_nm.value) == "")
		{
			flag=false;
			msg+="Please Enter Rate Name!\n";
		}
		if(rate_flag.value == "" || trim(rate_flag.value) == "") 
		{
			flag=false;
			msg+="Please Select State!\n";
		}
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

function enableComponents(obj)
{
	if (obj.checked)
	{
		document.forms[0].component1.style.pointerEvents = "auto";
		document.forms[0].component2.style.pointerEvents = "auto";
	}
	else 
	{
		document.forms[0].component1.style.pointerEvents = "none";
		document.forms[0].component2.style.pointerEvents = "none";
	}	
}

</script>

</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String rate_mode=request.getParameter("rate_mode")==null?"0":request.getParameter("rate_mode");

dbmaster.setCallFlag("RATE_MASTER");
dbmaster.setRate_mode(rate_mode);
dbmaster.init();

Vector VRATE_CD = dbmaster.getVRATE_CD();
Vector VRATE_NM = dbmaster.getVRATE_NM();
Vector VBANK_ABBR = dbmaster.getVBANK_ABBR();
Vector VRATE_FLAG = dbmaster.getVRATE_FLAG();

Vector VCOMPONENT_FLAG = dbmaster.getVCOMPONENT_FLAG();
Vector VCOMPONENT1 = dbmaster.getVCOMPONENT1();
Vector VCOMPONENT2 = dbmaster.getVCOMPONENT2();
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_master">

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
							Interest/Exchange Rate Master
						</div>
						<div class="btn-group">
							<select class="btn btn-outline-secondary btngrp <%if(!rate_mode.equals("0")){%>btnactive<%}%>" name="rate_mode" onchange="refresh();">
								<option value="0">-Select Rate Mode-</option>
								<option value="INTEREST">Interest Rate</option>
								<option value="EXCHANGE">Exchange Rate</option>
							</select>
						</div>
					</div>
					<script>document.forms[0].rate_mode.value="<%=rate_mode%>"</script>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#myModal" onclick="doClear();">Add New Rate</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th></th>
										<th>Description<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Description" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
										<th align="center">Bank Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Bank Name" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
										<th>State</th>
									</tr>
								</thead>
								<tbody>
								<%if(VRATE_CD.size() > 0){ %>
									<%for(int i=0; i<VRATE_CD.size(); i++){%>
									<tr>
										<td align="center" valign="middle" title="<%=VRATE_CD.elementAt(i)%>">
											<div align="center">
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" 
													data-bs-toggle="modal" data-bs-target="#myModal" 
													onclick="doModify('<%=VRATE_CD.elementAt(i)%>','<%=VRATE_NM.elementAt(i)%>','<%=VBANK_ABBR.elementAt(i)%>',
													'<%=VRATE_FLAG.elementAt(i)%>','<%=VCOMPONENT_FLAG.elementAt(i)%>','<%=VCOMPONENT1.elementAt(i)%>','<%=VCOMPONENT2.elementAt(i)%>');"></i>
												</font>
											</div>
										</td>
										<td><%=VRATE_NM.elementAt(i)%></td>
										<td><%=VBANK_ABBR.elementAt(i)%></td>
										<td align="center">
											<div align="center">
												<font style="color:<%if(VRATE_FLAG.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font>
												<%if(VRATE_FLAG.elementAt(i).equals("Y")){%>
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
										<td colspan="4" align="center">
										<%if(rate_mode.equals("0")){ %>
											<%=utilmsg.infoMessage("<b>Select Rate Mode to Proceed!</b>") %>
										<%}else{ %>
											<%=utilmsg.infoMessage("<b>No Rate Description is Configured!</b>") %>
										<%} %>
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
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
	</div>
</div>

<!-- MODEL -->
<div class="modal fade" id="myModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Rate Master
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Description<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" name="rate_nm" class="form-control form-control-sm" maxlength="100" value="">
				      				<input type="hidden" name="rate_cd" class="form-control form-control-sm" value="">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Bank Name</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" name="bank_abbr" class="form-control form-control-sm" maxlength="50" value="">
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
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="form-check form-switch">
										<input class="form-check-input" name="active" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked onclick="setActiveInactive(this);">
									  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb">
									  		Active
									  	</label>
									  	<input type="hidden" name="rate_flag" id="rate_flag" value="Y">
									</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div <%if(!rate_mode.equals("EXCHANGE")){%>style="display:none;"<%} %>>
						<div class="row m-b-5">
							<div class="col-sm-3 col-xs-3 col-md-3">  
								<div class="form-group row">
					    			<label class="form-label"><b><input type="checkbox" class="form-check-input" name="comp_rate_flag" value="Y" onclick="enableComponents(this);">&nbsp;Component Rates</b></label>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<select class="form-select form-select-sm" name="component1" style="pointer-events: none;">
											<option value="">--Select--</option>
											<%for(int i=0;i<VRATE_CD.size();i++){ %>
											<option value="<%=VRATE_CD.elementAt(i)%>"><%=VRATE_NM.elementAt(i)%></option>
											<%} %>
										</select>
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<select class="form-select form-select-sm" name="component2" style="pointer-events: none;">
											<option value="">--Select--</option>
											<%for(int i=0;i<VRATE_CD.size();i++){ %>
											<option value="<%=VRATE_CD.elementAt(i)%>"><%=VRATE_NM.elementAt(i)%></option>
											<%} %>
										</select>
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

<input type="hidden" name="option" value="RATE_MASTER">
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

<script>
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example");
  	
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
</html>