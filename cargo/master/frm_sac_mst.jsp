<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh(opration)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_sac_mst.jsp?opration="+opration+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function textCounter(field, countfield, maxlimit)
{
	if(field.value.length > maxlimit)
	{
		field.value = field.value.substring(0, maxlimit);
	}
	else
	{
		countfield.value = maxlimit - field.value.length;
	}
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var msg="";
	var flag=true;
	
	var sac_nm = document.forms[0].sac_nm.value;
	var sac_cd = document.forms[0].sac_cd.value;
	var desc = document.forms[0].desc.value;
	if(sac_nm=="" || sac_nm=="0" || trim(sac_nm)=="")
	{
		msg+="Enter SAC Name!\n";
		flag=false
	}
	if(desc=="" || desc=="0" || trim(desc)=="")
	{
		msg+="Enter SAC Description!\n";
		flag=false
	}
	
	if(flag)
	{
		var a;
		
		a = confirm("Do you want to "+opration+" SAC Detail?")

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
	document.forms[0].sac_cd.value="";
	document.forms[0].sac_nm.value="";
	document.forms[0].desc.value="";
	document.forms[0].remark.value="";
	document.forms[0].opration.value="INSERT";
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
		document.getElementById("lb").innerHTML="Deactive";
		document.getElementById("status_flag").value="N";
	}
}

function doModify(VSAC_CD,VSAC_NM,VSAC_DESC,VSAC_REMARKS,VSAC_FLAG)
{

	document.forms[0].sac_cd.value=VSAC_CD;
	document.forms[0].sac_nm.value=VSAC_NM;
	document.forms[0].desc.value=VSAC_DESC;
	document.forms[0].remark.value=VSAC_REMARKS;

	if(VSAC_FLAG == 'Y')
	{
		document.forms[0].active.checked=true;
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="Y";
	}
	else
	{
		document.forms[0].active.checked=false;
		document.getElementById("lb").innerHTML="Deactive";
		document.getElementById("status_flag").value="N";
	}
	
	document.forms[0].opration.value="MODIFY";
}

var newWindow;

function exportToXls()
{
	var url = "xls_sac_mst.jsp?fileName=SAC Master Report.xls";

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="sacMst" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String sac_cd = request.getParameter("sac_cd")==null?"0":request.getParameter("sac_cd");

sacMst.setCallFlag("SAC_MST");
sacMst.setComp_cd(owner_cd);
sacMst.init();

Vector VSAC_CD = sacMst.getVSAC_CD();
Vector VSAC_NAME = sacMst.getVSAC_NAME();
Vector VSAC_DESC = sacMst.getVSAC_DESC();
Vector VSAC_REMARKS = sacMst.getVSAC_REMARKS();
Vector VSAC_FLAG = sacMst.getVSAC_FLAG();
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
							SAC Master
						</div>
						<!-- <div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div> -->
					</div>
				</div>
				<div class="card-body cdbody">
					<%if(write_access.equals("Y")){ %>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#saclModal" onclick="doClear();">Add New SAC</label>
							</div>
						</div>
					</div><%} %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead id="tbsearch">
										<tr valign="top">
											<%if(write_access.equals("Y")){ %>
											<th >Select</th>
											<%}else{ %>
											<th>Sr#</th>
											<%} %>
											<th >Name</th>
											<th >Description</th>
											<th >Remark</th>
											<th>Status</th>
										</tr>
									</thead>
									<tbody>
									<%if(VSAC_CD.size() > 0){ %>
										<%for(int i=0; i<VSAC_CD.size(); i++){ %>
										<tr>
											<%if(write_access.equals("Y")){ %><td align="center">
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#saclModal" 
													onclick="doModify('<%=VSAC_CD.elementAt(i)%>','<%=VSAC_NAME.elementAt(i)%>','<%=VSAC_DESC.elementAt(i)%>','<%=VSAC_REMARKS.elementAt(i)%>','<%=VSAC_FLAG.elementAt(i)%>')">
													</i>
												</font>
											</td>
											<%}else{ %>
											<td><%=i+1%></td>
											<%} %>
											<td><%=VSAC_NAME.elementAt(i)%></td>
											<td align="center"><%=VSAC_DESC.elementAt(i) %></td>
											<td><%=VSAC_REMARKS.elementAt(i) %></td>
											<td align="center">
												<div align="center">
													<font style="color:<%if(VSAC_FLAG.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
														<i class="fa fa-circle fa-lg" ></i>
														&nbsp;
													</font>
													<%if(VSAC_FLAG.elementAt(i).equals("Y")){%>
													Active
													<%}else{ %>
													Deactive
													<%} %>
												</div>
											</td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="16" align="center">
												<%=utilmsg.infoMessage("<b>SAC Code List is not Available!</b>") %>
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
</div>
<div class="modal fade" id="saclModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify SAC
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>SAC Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="sac_nm" value="" maxLength="100">
				      				<input type="hidden" class="form-control form-control-sm" name="sac_cd" value="">
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
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<font size="1" face="arial, helvetica, sans-serif">&nbsp;( You may enter up to 500 characters. )&nbsp;
										<input readonly type=text name="remLen" size="3" maxlength="3" class=""> characters left
									</font><br>
				      				<textarea class="form-control" name="desc" cols="75" rows="2" onKeyDown="textCounter(this.form.desc,this.form.remLen,500);" onKeyUp="textCounter(this.form.desc,this.form.remLen,500);"></textarea>
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
						<div class="col-sm-8 col-xs-8 col-md-8">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="remark" value="" maxLength="40" >
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
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

<input type="hidden" name="option" value="SAC_MST">
<!-- <input type="hidden" name="option" value="VESSEL_MST"> -->
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">

<input type="hidden" name="form_clearance" value="KYC">
<input type="hidden" name="temp_pan_no" value="">

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
$(document).ready(function() {
	
	$('#tbsearch th').each(function(i){
		var title = $(this).text();
		if(title == "Select")
		{
			//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
		}
		else
		{
			$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:100px"/></div>');
		}
	});
	
});

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