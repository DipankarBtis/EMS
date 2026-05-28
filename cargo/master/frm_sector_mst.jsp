<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh()
{
	var filter_status = document.forms[0].filter_status.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_sector_mst.jsp?u="+u+"&filter_status="+filter_status;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
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
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="N";
	}
}

function doModify(sector_cd,sector_nm,sector_abbr,sector_type,status_flag)
{
	document.forms[0].sector_cd.value=sector_cd;
	document.forms[0].sector_nm.value=sector_nm;
	document.forms[0].sector_abbr.value=sector_abbr;
	document.forms[0].sector_type.value=sector_type;
	
	if(status_flag=='Y')
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

function doClear()
{
	document.forms[0].sector_cd.value="";
	document.forms[0].sector_nm.value="";
	document.forms[0].sector_abbr.value="";
	document.forms[0].sector_type.value="Y";
	
	document.forms[0].active.checked=true;
	document.getElementById("lb").innerHTML="Active";
	document.getElementById("status_flag").value="Y";
	
	document.forms[0].opration.value="INSERT";
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var sector_nm = document.forms[0].sector_nm;
	var sector_abbr = document.forms[0].sector_abbr;
	var sector_type = document.forms[0].sector_type;
	var sector_cd = document.forms[0].sector_cd;
	
	var flag = true;
	var msg = "";
	
	if(opration=="MODIFY")
	{
		if(sector_cd.value == "" || trim(sector_cd.value) == "")
		{
			flag=false;
			msg+="Sector CD Missing!\n";
		}
	}
	if(sector_nm.value == "" || trim(sector_nm.value) == "")
	{
		flag=false;
		msg+="Enter Sector Name!\n";
	}
	if(sector_abbr.value == "" || trim(sector_abbr.value) == "")
	{
		flag=false;
		msg+="Enter Sector ABBR!\n";
	}
	if(sector_type.value == "" || trim(sector_type.value) == "")
	{
		flag=false;
		msg+="Select Sector Type!\n";
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
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String filter_status = request.getParameter("filter_status")==null?"0":request.getParameter("filter_status");

dbmaster.setCallFlag("SECTOR_MST");
dbmaster.setFilter_status(filter_status);
dbmaster.init();

Vector VSECTOR_CD = dbmaster.getVSECTOR_CD();
Vector VSECTOR_NAME	= dbmaster.getVSECTOR_NAME();
Vector VSECTOR_ABBR = dbmaster.getVSECTOR_ABBR();
Vector VSECTOR_TYPE = dbmaster.getVSECTOR_TYPE();
Vector VSTATUS_FLAG = dbmaster.getVSTATUS_FLAG();
Vector VSECTOR_TYPE_NM = dbmaster.getVSECTOR_TYPE_NM();
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
					<div class="topheader">
						Energy Sector Master
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#myModal" onclick="doClear();">Add New Sector</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th></th>
										<th>
											Sector Name
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Sector Name" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>
											Sector Abbr
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Sector Abbr" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>
											Sector Type
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Sector Type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>
											Status
											<br>
											<div align="center">
												<select class="form-select form-select-sm" name="filter_status" onchange="refresh();" style="width:100px">
										    		<option value="0">--Select--</option>
										    		<option value="Y">Active</option>
										    		<option value="N">In-Active</option>
										    	</select>
										    	<script>document.forms[0].filter_status.value="<%=filter_status%>"</script>
									    	</div>
										</th>
									</tr>
								</thead>
								<tbody>
									<%for(int i=0; i<VSECTOR_CD.size(); i++){ %>
									<tr>
										<td width="5%" align="center">
											<div align="center">
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" 
													data-bs-toggle="modal" data-bs-target="#myModal" 
													onclick="doModify('<%=VSECTOR_CD.elementAt(i)%>','<%=VSECTOR_NAME.elementAt(i)%>','<%=VSECTOR_ABBR.elementAt(i)%>',
													'<%=VSECTOR_TYPE.elementAt(i)%>','<%=VSTATUS_FLAG.elementAt(i)%>');"></i>
												</font>
											</div>
										</td>
										<td><%=VSECTOR_NAME.elementAt(i)%></td>
										<td><%=VSECTOR_ABBR.elementAt(i)%></td>
										<td align="center"><%=VSECTOR_TYPE_NM.elementAt(i)%></td>
										<td>
											<div>
												<font style="color:<%if(VSTATUS_FLAG.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font>
												<%if(VSTATUS_FLAG.elementAt(i).equals("Y")){%>
												Active
												<%}else{ %>
												In-Active
												<%} %>
											</div>
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

<!-- MODEL -->
<div class="modal fade" id="myModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Sector Detail
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Sector Name<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="text" class="form-control form-control-sm" name="sector_nm" maxlength="50">
					    			<input type="hidden" class="form-control form-control-sm" name="sector_cd" >
					    		</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Sector ABBR<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="text" class="form-control form-control-sm" name="sector_abbr" maxlength="50">
					    		</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Sector Type<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<select class="form-select form-select-sm" name="sector_type">
					    				<option value="Y">Re-seller/CGD/LDC</option>
									    <option value="P">End Customer</option>
									    <option value="O">Others</option>
					    			</select>
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
					

<input type="hidden" name="option" value="SECTOR_MST">
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