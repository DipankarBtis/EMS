<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function setValues(tax_cd,tax_nm,tax_type,status,rmk)
{
	document.forms[0].tax_cd.value=tax_cd;
	document.forms[0].tax_name.value=tax_nm;
	document.forms[0].tax_type.value=tax_type;
	document.forms[0].status.value=status;
	document.forms[0].remark.value=rmk;
	
	document.forms[0].opration.value="MODIFY";
}

function doSubmit()
{
	var tax_name=document.forms[0].tax_name.value;
	var tax_type=document.forms[0].tax_type.value;
	var status=document.forms[0].status.value;
	var opration = document.forms[0].opration.value;
	var u = document.forms[0].u.value;
	var msg="";
	var flag=true;
	
	if(trim(tax_name) == "")
	{
		msg+="Enter Tax Name!\n";
		flag=false;
	}
	if(trim(tax_type) == "" || tax_type == "0")
	{
		msg+="Select Tax Type!\n";
		flag=false;
	}
	if(trim(status) == "" || status == "0")
	{
		msg+="Select Status!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" Government Statutory Tax Detail?")
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
dbmaster.setCallFlag("GOVT_STAT_TAX_MST");
dbmaster.init();

Vector VSTAT_CD = dbmaster.getVSTAT_CD();
Vector VSTAT_NM = dbmaster.getVSTAT_NM();
Vector VSTAT_TYPE = dbmaster.getVSTAT_TYPE();
Vector VSTAT_STATUS = dbmaster.getVSTAT_STATUS();
Vector VSTAT_REMARK = dbmaster.getVSTAT_REMARK();
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
						Government Statutory Tax Master
					</div>
				</div>
			</div>
		</div>
	</div>
	&nbsp;
	<div class="row">
		<div class="col-md-5 col-sm-5 col-xs-5">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="topheader">
						Add/Modify Tax Detail
					</div>
					<div class="card-body cdbody">
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Tax Name<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="tax_name" value="" maxLength="50">
					      				<input type="hidden" name="tax_cd" value="">
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Tax Type<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<select class="form-select form-select-sm" name="tax_type">
					      					<option value="0">--Select--</option>
											<option value="S">Commodity Invoice Tax</option>
											<option value="R">Commodity | Service Invoice Tax</option>
											<option value="G">General Identification Number</option>
										</select>
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Status<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<select class="form-select form-select-sm" name="status">
					      					<option value="0">--Select--</option>
											<option value="Y">Active</option>
											<option value="N">In-Active</option>
										</select>
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Remark</b></label>
					  			</div>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<textarea class="form-control form-control-sm" name="remark" cols="75" rows="3" maxlength="100" ></textarea>
					    			</div>
					  			</div>
							</div>
						</div>
					</div>
					<div class="card-footer cdfooter text-center">
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
		<div class="col-md-1 col-sm-1 col-xs-1">
		&nbsp;
		</div>
		<div class="col-md-6 col-sm-6 col-xs-6">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="topheader">
						Government Statutory Tax Detail
					</div>
					<div class="card-body cdbody">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th></th>
										<th>Tax Name</th>
										<th>Tax Type</th>
										<th>Status</th>
										<th>Remark</th>
									</tr>
								</thead>
								<tbody>
									<%if(VSTAT_CD.size() > 0){ %>
										<%for(int i=0; i< VSTAT_CD.size(); i++){ %>
										<tr>
											<td>
												<div align="center">
													<font title="Click to Edit" style="color:var(--header_color)"><i class="fa fa-edit fa-lg" onclick="setValues('<%=VSTAT_CD.elementAt(i)%>','<%=VSTAT_NM.elementAt(i)%>','<%=VSTAT_TYPE.elementAt(i)%>','<%=VSTAT_STATUS.elementAt(i)%>','<%=VSTAT_REMARK.elementAt(i)%>')"></i></font>
												</div>
											</td>
											<td><div><%=VSTAT_NM.elementAt(i)%></div></td>
											<td>
												<div>
													<%if(VSTAT_TYPE.elementAt(i).equals("S")){%>
														Commodity Invoice Tax Type
													<%} else if(VSTAT_TYPE.elementAt(i).equals("R")){%>
														Commodity | Service Invoice Tax Type
													<%} else if(VSTAT_TYPE.elementAt(i).equals("G")){%>
														General Identification Number
													<%} %>
												</div>
											</td>
											<td><div><%if(VSTAT_STATUS.elementAt(i).equals("N")){%>In-Active<%}else{ %>Active<%} %></div></td>
											<td><div><%=VSTAT_REMARK.elementAt(i)%></div></td>
										</tr>
										<%} %>
									<%}else{ %>
									<tr>
										<td colspan="5" align="center">
										<%=utilmsg.infoMessage("<b>No Government Statutory Tax Configured!</b>")%>
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
<input type="hidden" name="option" value="GOVT_STAT_TAX_MST">
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