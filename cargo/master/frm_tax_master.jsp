<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function setActiveInactive(obj)
{
	if(obj.checked)
	{
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="1";
	}
	else
	{
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="0";
	}
}

function doModify(tax_cd,tax_nm,tax_alias_nm,tax_sht_nm,tax_app_dt,tax_status)
{
	document.forms[0].tax_cd.value=tax_cd;
	document.forms[0].tax_nm.value=tax_nm;
	document.forms[0].tax_short_nm.value=tax_sht_nm;
	document.forms[0].tax_alias_cd.value=tax_alias_nm;
	
	if(tax_status=='1')
	{
		document.forms[0].active.checked=true;
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="1";
	}
	else
	{
		document.forms[0].active.checked=false;
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="0";
	}
	
	document.forms[0].opration.value="MODIFY";
}

function doClear()
{
	document.forms[0].tax_cd.value="";
	document.forms[0].tax_nm.value="";
	document.forms[0].tax_short_nm.value="";
	document.forms[0].tax_alias_cd.value="";
	
	document.forms[0].active.checked=true;
	document.getElementById("lb").innerHTML="Active";
	document.getElementById("status_flag").value="1";
	
	document.forms[0].opration.value="INSERT";
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var tax_cd = document.forms[0].tax_cd;
	var tax_nm = document.forms[0].tax_nm;
	var u = document.forms[0].u.value;
	var flag = true;
	var msg = "";
	
	
	if(opration=="MODIFY")
	{
		if(tax_cd.value == "" || trim(tax_cd.value) == "")
		{
			flag=false;
			msg+="Tax CD Missing!\n";
		}
	}
	if(tax_nm.value == "" || trim(tax_nm.value) == "")
	{
		flag=false;
		msg+="Please Enter Tax Name!\n";
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
dbmaster.setCallFlag("TAX_MASTER");
dbmaster.init();

Vector VTAX_CD = dbmaster.getVTAX_CD();
Vector VTAX_NM = dbmaster.getVTAX_NM();
Vector VTAX_ALIAS_NM = dbmaster.getVTAX_ALIAS_NM();
Vector VTAX_SHT_NM = dbmaster.getVTAX_SHT_NM();
Vector VTAX_APP_DT = dbmaster.getVTAX_APP_DT();
Vector VTAX_STATUS = dbmaster.getVTAX_STATUS();
Vector VCOUNT = dbmaster.getVCOUNT();
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
							Tax Master
						</div>					  	
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#myModal" onclick="doClear();">Add New Tax</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th></th>
										<th>Tax Code</th>
										<th>
											Tax Name
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Tax Name" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>	
										</th>
										<th>
											Alias
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Alias" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Tax Short Name</th>
										<th>Status</th>
									</tr>
								</thead>
								<tbody>
								<%if(VTAX_CD.size() > 0){ %>
									<%for(int i=0; i<VTAX_CD.size(); i++){ %>
									<tr>
										<td width="5%" align="center" title="<%if(Integer.parseInt(""+VCOUNT.elementAt(i))>0){ %>Tax Structure Configured!<%}%>">
											<div align="center">
											<%if(Integer.parseInt(""+VCOUNT.elementAt(i))==0){ %>
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" 
													data-bs-toggle="modal" data-bs-target="#myModal" 
													onclick="doModify('<%=VTAX_CD.elementAt(i)%>','<%=VTAX_NM.elementAt(i)%>','<%=VTAX_ALIAS_NM.elementAt(i)%>',
													'<%=VTAX_SHT_NM.elementAt(i)%>','<%=VTAX_APP_DT.elementAt(i)%>','<%=VTAX_STATUS.elementAt(i)%>')"></i>
												</font>
											<%} %>
											</div>
										</td>
										<td align="center"><%=VTAX_CD.elementAt(i)%></td>
										<td><%=VTAX_NM.elementAt(i)%></td>
										<td><%=VTAX_ALIAS_NM.elementAt(i)%></td>
										<td><%=VTAX_SHT_NM.elementAt(i)%></td>
										<td align="center">
											<div align="center">
												<font style="color:<%if(VTAX_STATUS.elementAt(i).equals("1")){%>#a6ff4d<%}else{%>red<%}%>">
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font>
												<%if(VTAX_STATUS.elementAt(i).equals("1")){%>
												Active
												<%}else{ %>
												In-Active
												<%} %>
											</div>
										</td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="5"><%=utilmsg.infoMessage("<b>No Tax is Configured!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<font color="blue"><b>Note : Edit locked for Tax Master Code in Use</b></font>
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
					Add/Modify Tax
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Tax Name<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="text" class="form-control form-control-sm" name="tax_nm" maxlength="50">
					    			<input type="hidden" class="form-control form-control-sm" name="tax_cd" >
					    		</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Tax Short Name</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="text" class="form-control form-control-sm" name="tax_short_nm" maxlength="50">
					    		</div>
				  			</div>
						</div>
					</div>
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Tax Alias Code</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="text" class="form-control form-control-sm" name="tax_alias_cd" maxlength="10">
					    		</div>
				  			</div>
						</div><div class="col-sm-2 col-xs-2 col-md-2">
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
									  	<input type="hidden" name="status_flag" id="status_flag" value="1">
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

<input type="hidden" name="option" value="TAX_MASTER">
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