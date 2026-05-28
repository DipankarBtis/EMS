<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>

function doClear()
{
	document.forms[0].hedge_dt.value="";
	document.forms[0].exposure_value.value="";
	
	document.forms[0].opration.value="INSERT";
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var hedge_dt = document.forms[0].hedge_dt;
	var exposure_value = document.forms[0].exposure_value;
	var u = document.forms[0].u.value;
	var flag = true;
	var msg = "";
	
	
	if(hedge_dt.value == "" || trim(hedge_dt.value) == "")
	{
		flag=false;
		msg+="Enter Hedge Date!\n";
	}
	if(exposure_value.value == "" || trim(exposure_value.value) == "")
	{
		flag=false;
		msg+="Enter Direct Exposure Value!\n";
	}
	
	if(flag)
	{
		var a= confirm("Do you Want to Submit?");
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
<jsp:useBean class="com.etrm.fms.derivatives.DataBean_Derivarives_mst" id="derivatives" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
derivatives.setCallFlag("DIRECT_EXPOSURE");
derivatives.setComp_cd(owner_cd);
derivatives.init();

Vector VBALANCE_QTY = derivatives.getVBALANCE_QTY();
Vector VHEDGE_DT = derivatives.getVHEDGE_DT();

%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_DerivativesMaster">

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
							Direct Exposure Master
						</div>					  	
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#myModal" onclick="doClear();">Add New Value</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th></th>
										<th>Hedge Date</th>
										<th>
											Direct Exposure Amount
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Tax Name" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>	
										</th>
									</tr>
								</thead>
								<tbody>
								<%if(VHEDGE_DT.size() > 0){ %>
									<%for(int i=0; i<VHEDGE_DT.size(); i++){ %>
									<tr>
										<td width="5%" align="center">
											<div align="center">
											</div>
										</td>
										<td align="center"><%=VHEDGE_DT.elementAt(i)%></td>
										<td align="right"><%=VBALANCE_QTY.elementAt(i)%></td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="3"><%=utilmsg.infoMessage("<b>No Direct Exposure Value Configured!</b>") %></td>
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
					Add/Modify Direct Exposure
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Hedge Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="hedge_dt" value="" maxLength="10" 
			      						onblur="" onchange="" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
					    		</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Direct Exposure</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="text" class="form-control form-control-sm" name="exposure_value" maxlength="50">
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

<input type="hidden" name="option" value="DIRECT_EXPOSURE">
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