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
	
	var url = "frm_product_molecule_mst.jsp?opration="+opration+"&u="+u;

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
	
	var product_cd = document.forms[0].product_cd.value;
	var molecule_nm = document.forms[0].molecule_nm.value;
	var molecule_abbr = document.forms[0].molecule_abbr.value;
	
	if(product_cd=="" || product_cd=="0" || trim(product_cd)=="")
	{
		msg+="Select Product!\n";
		flag=false
	}
	
	if(molecule_nm=="" || molecule_nm=="0" || trim(molecule_nm)=="")
	{
		msg+="Enter Molecule Name!\n";
		flag=false
	}
	
	if(molecule_abbr=="" || molecule_abbr=="0" || trim(molecule_abbr)=="")
	{
		msg+="Enter Molecule ABBR!\n";
		flag=false
	}

	if(flag)
	{
		var a;
		
		a = confirm("Do you want to "+opration+" Moleclue Detail?")

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
	document.forms[0].product_cd.value="0";
	document.forms[0].molecule_cd.value="";
	document.forms[0].molecule_nm.value="";
	document.forms[0].molecule_abbr.value="";
	document.forms[0].desc.value="";
	
	document.forms[0].opration.value="INSERT";
}

function doModify(VPROD_CD,VMOLE_CD,VMOLE_NM,VMOLE_ABBR,VMOLE_DESC,VMOLE_FLAG)
{

	document.forms[0].product_cd.value=VPROD_CD;
	document.forms[0].molecule_cd.value=VMOLE_CD;
	document.forms[0].molecule_nm.value=VMOLE_NM;
	document.forms[0].molecule_abbr.value=VMOLE_ABBR;
	document.forms[0].desc.value=VMOLE_DESC;

	if(VMOLE_FLAG == 'Y')
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

var newWindow;
function exportToXls()
{
	var url = "xls_product_molecule_mst.jsp?fileName=Product Molecule Master Report.xls";

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String molecule_cd = request.getParameter("molecule_cd")==null?"0":request.getParameter("molecule_cd");
String product_cd = request.getParameter("product_cd")==null?"0":request.getParameter("product_cd");

dbmaster.setCallFlag("MOLECULE_MST");
dbmaster.setComp_cd(owner_cd);
//dbcargo.setproduct_cd(product_cd);
dbmaster.init();

Vector VPROD_CD = dbmaster.getVPROD_CD();
Vector VPROD_NM = dbmaster.getVPROD_NM();
Vector VPROD_ABBR = dbmaster.getVPROD_ABBR();

Vector VPROD_MOLE_NM = dbmaster.getVPROD_MOLE_NM();
Vector VPROD_MOLE_CD = dbmaster.getVPROD_MOLE_CD();
Vector VMOLE_CD = dbmaster.getVMOLE_CD();
Vector VMOLE_NM = dbmaster.getVMOLE_NM();
Vector VMOLE_ABBR = dbmaster.getVMOLE_ABBR();
Vector VMOLE_DESC = dbmaster.getVMOLE_DESC();
Vector VMOLE_FLAG = dbmaster.getVMOLE_FLAG();

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
							Product Molecule Master
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
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#moleculelModal" onclick="doClear();">Add New Molecule</label>
							</div>
						</div>
					</div><%} %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead id="tbsearch">
										<tr valign="top">
											<th >Select</th>
											<th >Name</th>
											<th >Abbreviation</th>
											<th >Product</th>
											<th >Description</th>
											<th>Status</th>
										</tr>
									</thead>
									<tbody>
									<%if(VMOLE_CD.size() > 0){ %>
										<%for(int i=0; i<VMOLE_CD.size(); i++){ %>
										<tr>
											<td align="center">
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#moleculelModal" 
													onclick="doModify('<%=VPROD_MOLE_CD.elementAt(i)%>','<%=VMOLE_CD.elementAt(i)%>','<%=VMOLE_NM.elementAt(i)%>','<%=VMOLE_ABBR.elementAt(i)%>','<%=VMOLE_DESC.elementAt(i)%>','<%=VMOLE_FLAG.elementAt(i)%>')">
													</i>
												</font>
											</td>
											<td align="center"><%=VMOLE_NM.elementAt(i)%></td>
											<td align="center"><%=VMOLE_ABBR.elementAt(i)%></td>
											<td align="center"><%=VPROD_MOLE_NM.elementAt(i)%></td>
											<td align="center"><%=VMOLE_DESC.elementAt(i)%></td>
											<td align="center">
												<div align="center">
													<font style="color:<%if(VMOLE_FLAG.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
														<i class="fa fa-circle fa-lg" ></i>
														&nbsp;
													</font>
													<%if(VMOLE_FLAG.elementAt(i).equals("Y")){%>
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
												<%=utilmsg.infoMessage("<b>Product Molecule List is not Available!</b>") %>
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
<div class="modal fade" id="moleculelModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Product Molecule
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Product <span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="product_cd">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VPROD_CD.size();i++){ %>
										<option value="<%=VPROD_CD.elementAt(i)%>"><%=VPROD_ABBR.elementAt(i)%> - <%=VPROD_NM.elementAt(i)%></option>
										<%} %>
									</select>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Molecule Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="molecule_nm" value="" maxLength="100" onblur="isExistNM();">
				      				<input type="hidden" class="form-control form-control-sm" name="molecule_cd" value="">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Molecule Abbreviation<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="molecule_abbr" value="" maxLength="40" onblur="isExistABBR();">
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Description</b></label>
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

<input type="hidden" name="option" value="MOLECULE_MST">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">

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
		//alert(i)
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
    	//tbody=tr[i].getElementsByTagName("tbody");alert(tbody)
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
function isExistNM()
{
	var opration = document.forms[0].opration.value;
	
	var molecule_cd ="0";
	var product_cd = document.forms[0].product_cd.value;
	if(opration == "MODIFY")
	{
		molecule_cd = document.forms[0].molecule_cd.value;
	}
	var molecule_nm = document.forms[0].molecule_nm.value;
	
	var info="";
	
	$.post("../servlet/DB_Master_Ajax?setCallType=IsExistProductName&product_cd="+product_cd+"&molecule_cd="+molecule_cd+
			"&molecule_nm="+molecule_nm+
			"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.MOLE_DTL, function(index_1, json_1) {
				if(parseInt(json_1.MOLE_NAME) > 0)
				{
					info+="Name is already Exist!\n";
				}
				if(info!="")
				{
					alert(info)
					document.forms[0].molecule_nm.value="";
				}
			});
		});
	});
	
	return info;
}

function isExistABBR()
{
	var opration = document.forms[0].opration.value;
	
	var molecule_cd ="0";
	var product_cd = document.forms[0].product_cd.value;
	if(opration == "MODIFY")
	{
		molecule_cd = document.forms[0].molecule_cd.value;
	}
	var molecule_abbr = document.forms[0].molecule_abbr.value;
	
	var info="";
	
	$.post("../servlet/DB_Master_Ajax?setCallType=IsExistProductABBR&product_cd="+product_cd+"&molecule_cd="+molecule_cd+
			"&molecule_abbr="+molecule_abbr+"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.MOLE_DTL, function(index_1, json_1) {
				if(parseInt(json_1.MOLE_ABBR) > 0)
				{
					info+="Abbreviation is already Exist!";
				}
				
				if(info!="")
				{
					alert(info)
					document.forms[0].molecule_abbr.value="";
				}
			});
		});
	});
}
</script>

</html>