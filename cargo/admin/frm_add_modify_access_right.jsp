<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var module_cd = document.forms[0].module_cd.value;
	var group_cd = document.forms[0].group_cd.value;
	var sub_module_cd = document.forms[0].sub_module_cd.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_add_modify_access_right.jsp?module_cd="+module_cd+"&group_cd="+group_cd+"&u="+u+"&sub_module_cd="+sub_module_cd;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setAccessFlag(obj,id)
{
	if(obj.checked)
	{
		document.getElementById(id).value="Y";
	}
	else
	{
		document.getElementById(id).value="N";
	}
}

function setVarticalAccessFlag(obj, checkbox_nm, flag_id)
{
	if(checkbox_nm!=null && checkbox_nm.length!=undefined)
	{
		for(var i=0; i<checkbox_nm.length; i++)
		{
			if(obj.checked)
			{
				checkbox_nm[i].checked=true;
				flag_id[i].value="Y";
			}
			else
			{
				checkbox_nm[i].checked=false;
				flag_id[i].value="N";
			}
		}
	}
	else
	{
		if(obj.checked)
		{
			checkbox_nm.checked=true;
			flag_id.value="Y";
		}
		else
		{
			checkbox_nm.checked=false;
			flag_id.value="N";
		}
	}
}

function setHorizontalAccessFlag(obj,index)
{
	if(obj.checked)
	{
		document.getElementById("read_acs"+index).checked=true;
		document.getElementById("write_acs"+index).checked=true;
		document.getElementById("check_acs"+index).checked=true;
		document.getElementById("print_acs"+index).checked=true;
		document.getElementById("delete_acs"+index).checked=true;
		document.getElementById("audit_acs"+index).checked=true;
		document.getElementById("authorize_acs"+index).checked=true;
		document.getElementById("approve_acs"+index).checked=true;
		document.getElementById("execute_acs"+index).checked=true;
		
		document.getElementById("read_acs_flag"+index).value="Y";
		document.getElementById("write_acs_flag"+index).value="Y";
		document.getElementById("check_acs_flag"+index).value="Y";
		document.getElementById("print_acs_flag"+index).value="Y";
		document.getElementById("delete_acs_flag"+index).value="Y";
		document.getElementById("audit_acs_flag"+index).value="Y";
		document.getElementById("authorize_acs_flag"+index).value="Y";
		document.getElementById("approve_acs_flag"+index).value="Y";
		document.getElementById("execute_acs_flag"+index).value="Y";
	}
	else
	{
		document.getElementById("read_acs"+index).checked=false;
		document.getElementById("write_acs"+index).checked=false;
		document.getElementById("check_acs"+index).checked=false;
		document.getElementById("print_acs"+index).checked=false;
		document.getElementById("delete_acs"+index).checked=false;
		document.getElementById("audit_acs"+index).checked=false;
		document.getElementById("authorize_acs"+index).checked=false;
		document.getElementById("approve_acs"+index).checked=false;
		document.getElementById("execute_acs"+index).checked=false;
		
		document.getElementById("read_acs_flag"+index).value="N";
		document.getElementById("write_acs_flag"+index).value="N";
		document.getElementById("check_acs_flag"+index).value="N";
		document.getElementById("print_acs_flag"+index).value="N";
		document.getElementById("delete_acs_flag"+index).value="N";
		document.getElementById("audit_acs_flag"+index).value="N";
		document.getElementById("authorize_acs_flag"+index).value="N";
		document.getElementById("approve_acs_flag"+index).value="N";
		document.getElementById("execute_acs_flag"+index).value="N";
	}
}

function doSubmit()
{
	var module_cd = document.forms[0].module_cd.value;
	var group_cd = document.forms[0].group_cd.value;
	
	var msg = "";
	var flag = true;
	
	if(group_cd == "" || trim(group_cd)=="" || group_cd == "0")
	{
		msg+="Please Select Access Group!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a= confirm("Do you Want to Submit Access Rights Detail?");
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
<jsp:useBean class="com.etrm.fms.admin.DataBean_Admin" id="dbadmin" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String module_cd=request.getParameter("module_cd")==null?"0":request.getParameter("module_cd");
String group_cd=request.getParameter("group_cd")==null?"0":request.getParameter("group_cd");
String sub_module_cd=request.getParameter("sub_module_cd")==null?"All":request.getParameter("sub_module_cd");

String company_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");

dbadmin.setCallFlag("ACCESS_RIGHT_MST");
dbadmin.setModule_cd(module_cd);
dbadmin.setGroup_cd(group_cd);
dbadmin.setSub_module_cd(sub_module_cd);
dbadmin.setComp_cd(company_cd);
dbadmin.setEmp_cd(emp_cd);
dbadmin.init();

Vector VMODULE_CD = dbadmin.getVMODULE_CD();
Vector VMODULE_NM = dbadmin.getVMODULE_NM();
Vector VGROUP_CD = dbadmin.getVGROUP_CD();
Vector VGROUP_NM = dbadmin.getVGROUP_NM();
Vector VSUB_MODULE_CD = dbadmin.getVSUB_MODULE_CD();
Vector VSUB_MODULE_NM = dbadmin.getVSUB_MODULE_NM();

Vector VMENU_CD = dbadmin.getVMENU_CD();
Vector VMENU_NM = dbadmin.getVMENU_NM();
Vector VREAD_ACS = dbadmin.getVREAD_ACS();
Vector VWRITE_ACS = dbadmin.getVWRITE_ACS();
Vector VCHECK_ACS = dbadmin.getVCHECK_ACS();
Vector VPRINT_ACS = dbadmin.getVPRINT_ACS();
Vector VDELETE_ACS = dbadmin.getVDELETE_ACS();
Vector VAUDIT_ACS = dbadmin.getVAUDIT_ACS();
Vector VAUTHORIZE_ACS = dbadmin.getVAUTHORIZE_ACS();
Vector VAPPROVE_ACS = dbadmin.getVAPPROVE_ACS();
Vector VEXECUTE_ACS = dbadmin.getVEXECUTE_ACS();

%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_admin">

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
				<div class="card-header cdheader topheader">
				    Add/Modify Access Right
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-15">
						<div class="col-sm-3 col-xs-3 co1-md-3"></div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class=""><b>Access Group</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="group_cd" id="group_cd" onchange="refresh();">
										<option value="0"> -- Select -- </option>
										<%for(int i=0;i<VGROUP_CD.size();i++){%>
											<option value="<%=VGROUP_CD.elementAt(i)%>"><%=VGROUP_NM.elementAt(i)%></option>
										<%}%>
									</select>
									<script>document.forms[0].group_cd.value='<%=group_cd%>';</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 co1-md-3"></div>
					</div>
					<div class="row m-b-15">
						<div class="col-sm-3 col-xs-3 co1-md-3"></div>
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class=""><b>Module</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="module_cd" id="module_cd" onchange="refresh();">
										<option value="0"> -- Select -- </option>
										<%for(int i=0;i<VMODULE_CD.size();i++){%>
											<option value="<%=VMODULE_CD.elementAt(i)%>"><%=VMODULE_NM.elementAt(i)%></option>
										<%}%>
									</select>
									<script>document.forms[0].module_cd.value='<%=module_cd%>';</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 co1-md-3"></div>
					</div>
					<div class="row m-b-15">
						<div class="col-sm-3 col-xs-3 co1-md-3"></div>
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class=""><b>Sub Module</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="sub_module_cd" id="sub_module_cd" onchange="refresh();">
										<option value="All">All</option>
										<%for(int i=0;i<VSUB_MODULE_CD.size();i++){%>
											<option value="<%=VSUB_MODULE_CD.elementAt(i)%>"><%=VSUB_MODULE_NM.elementAt(i)%></option>
										<%}%>
									</select>
									<script>document.forms[0].sub_module_cd.value='<%=sub_module_cd%>';</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 co1-md-3"></div>
					</div>
					<%if(!group_cd.equals("0") && !module_cd.equals("0")){ %> 
					<div class="table-responsive">
						<table class="table table-bordered">
							<thead>
								<tr>
									<th rowspan="2">Form/Report</th>
									<th rowspan="2">Check<br>All</th>
									<th colspan="9">Access Rights</th>
								</tr>
								<tr>
									<th><input type="checkbox" class="form-check-input" onclick="setVarticalAccessFlag(this,document.forms[0].read_acs,document.forms[0].read_acs_flag)"> Read</th>
									<th><input type="checkbox" class="form-check-input" onclick="setVarticalAccessFlag(this,document.forms[0].write_acs,document.forms[0].write_acs_flag)"> Write</th>
									<th><input type="checkbox" class="form-check-input" onclick="setVarticalAccessFlag(this,document.forms[0].check_acs,document.forms[0].check_acs_flag)"> Check</th>
									<th><input type="checkbox" class="form-check-input" onclick="setVarticalAccessFlag(this,document.forms[0].print_acs,document.forms[0].print_acs_flag)"> Print</th>
									<th><input type="checkbox" class="form-check-input" onclick="setVarticalAccessFlag(this,document.forms[0].delete_acs,document.forms[0].delete_acs_flag)"> Delete</th>
									<th><input type="checkbox" class="form-check-input" onclick="setVarticalAccessFlag(this,document.forms[0].audit_acs,document.forms[0].audit_acs_flag)"> Audit</th>
									<th><input type="checkbox" class="form-check-input" onclick="setVarticalAccessFlag(this,document.forms[0].authorize_acs,document.forms[0].authorize_acs_flag)"> Authorize</th>
									<th><input type="checkbox" class="form-check-input" onclick="setVarticalAccessFlag(this,document.forms[0].approve_acs,document.forms[0].approve_acs_flag)"> Approve</th>
									<th><input type="checkbox" class="form-check-input" onclick="setVarticalAccessFlag(this,document.forms[0].execute_acs,document.forms[0].execute_acs_flag)"> Execute</th>
								</tr>
							</thead>
							<tbody>
								<%for(int i=0; i<VMENU_CD.size(); i++){ %>
								<tr>
									<td>
										<%=VMENU_NM.elementAt(i)%>
										<input type="hidden" name="menu_cd" value="<%=VMENU_CD.elementAt(i)%>">
									</td>
									<td>
										<div align="center">
											<input type="checkbox" class="form-check-input" name="chkAll<%=i%>" id="chkAll<%=i%>" onclick="setHorizontalAccessFlag(this,'<%=i%>')">
										</div>
									</td>
									<td>
										<div align="center">
											<input type="checkbox" class="form-check-input" name="read_acs" id="read_acs<%=i%>" onclick="setAccessFlag(this,'read_acs_flag<%=i%>');" <%if(VREAD_ACS.elementAt(i).equals("Y")){ %>checked<%} %>>
											<input type="hidden" name="read_acs_flag" id="read_acs_flag<%=i%>" value="<%=VREAD_ACS.elementAt(i)%>">
										</div>
									</td>
									<td>
										<div align="center">
											<input type="checkbox" class="form-check-input" name="write_acs" id="write_acs<%=i%>" onclick="setAccessFlag(this,'write_acs_flag<%=i%>');" <%if(VWRITE_ACS.elementAt(i).equals("Y")){ %>checked<%} %>>
											<input type="hidden" name="write_acs_flag" id="write_acs_flag<%=i%>" value="<%=VWRITE_ACS.elementAt(i)%>">
										</div>
									</td>
									<td>
										<div align="center">
											<input type="checkbox" class="form-check-input" name="check_acs" id="check_acs<%=i%>" onclick="setAccessFlag(this,'check_acs_flag<%=i%>');" <%if(VCHECK_ACS.elementAt(i).equals("Y")){ %>checked<%} %>>
											<input type="hidden" name="check_acs_flag" id="check_acs_flag<%=i%>" value="<%=VCHECK_ACS.elementAt(i)%>">
										</div>
									</td>
									<td>
										<div align="center">
											<input type="checkbox" class="form-check-input" name="print_acs"  id="print_acs<%=i%>" onclick="setAccessFlag(this,'print_acs_flag<%=i%>');" <%if(VPRINT_ACS.elementAt(i).equals("Y")){ %>checked<%} %>>
											<input type="hidden" name="print_acs_flag" id="print_acs_flag<%=i%>" value="<%=VPRINT_ACS.elementAt(i)%>">
										</div>
									</td>
									<td>
										<div align="center">
											<input type="checkbox" class="form-check-input" name="delete_acs" id="delete_acs<%=i%>" onclick="setAccessFlag(this,'delete_acs_flag<%=i%>');" <%if(VDELETE_ACS.elementAt(i).equals("Y")){ %>checked<%} %>>
											<input type="hidden" name="delete_acs_flag" id="delete_acs_flag<%=i%>" value="<%=VDELETE_ACS.elementAt(i)%>">
										</div>
									</td>
									<td>
										<div align="center">
											<input type="checkbox" class="form-check-input" name="audit_acs" id="audit_acs<%=i%>" onclick="setAccessFlag(this,'audit_acs_flag<%=i%>');" <%if(VAUDIT_ACS.elementAt(i).equals("Y")){ %>checked<%} %>>
											<input type="hidden" name="audit_acs_flag" id="audit_acs_flag<%=i%>" value="<%=VAUDIT_ACS.elementAt(i)%>">
										</div>
									</td>
									<td>
										<div align="center">
											<input type="checkbox" class="form-check-input" name="authorize_acs" id="authorize_acs<%=i%>" onclick="setAccessFlag(this,'authorize_acs_flag<%=i%>');" <%if(VAUTHORIZE_ACS.elementAt(i).equals("Y")){ %>checked<%} %>>
											<input type="hidden" name="authorize_acs_flag" id="authorize_acs_flag<%=i%>" value="<%=VAUTHORIZE_ACS.elementAt(i)%>">
										</div>
									</td>
									<td>
										<div align="center">
											<input type="checkbox" class="form-check-input" name="approve_acs" id="approve_acs<%=i%>" onclick="setAccessFlag(this,'approve_acs_flag<%=i%>');" <%if(VAPPROVE_ACS.elementAt(i).equals("Y")){ %>checked<%} %>>
											<input type="hidden" name="approve_acs_flag" id="approve_acs_flag<%=i%>" value="<%=VAPPROVE_ACS.elementAt(i)%>">
										</div>
									</td>
									<td>
										<div align="center">
											<input type="checkbox" class="form-check-input" name="execute_acs" id="execute_acs<%=i%>" onclick="setAccessFlag(this,'execute_acs_flag<%=i%>');" <%if(VEXECUTE_ACS.elementAt(i).equals("Y")){ %>checked<%} %>>
											<input type="hidden" name="execute_acs_flag" id="execute_acs_flag<%=i%>" value="<%=VEXECUTE_ACS.elementAt(i)%>">
										</div>
									</td>
								</tr>
								<%} %>
							</tbody>
						</table>
					</div>
					<%}else{ %>
					<div align="center"><%=utilmsg.infoMessage("<b>Please Select Group Name and Module Name!</b>") %></div>
					<%} %>
				</div>
				<%if(!group_cd.equals("0") && VMENU_CD.size()>0){ %>
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
				<%} %>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="ACCESS_RIGHT_MST">

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