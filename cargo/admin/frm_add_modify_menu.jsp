<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
		document.getElementById("status_flag").value="Y";
	}
	else
	{
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="N";
	}
}

function refresh()
{
	var module_cd = document.forms[0].module_cd.value;
	var menu_cd = document.forms[0].menu_cd.value;
	
	var pre_module_cd = document.forms[0].pre_module_cd.value;
	 
	if(pre_module_cd != module_cd)
	{
		menu_cd="0";
	}
	
	var u = document.forms[0].u.value;
	 
	var url = "frm_add_modify_menu.jsp?module_cd="+module_cd+"&menu_cd="+menu_cd+"&u="+u;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function hidegrp_id()
{	
	if (document.forms[0].group_cd.value == 'other')
	{
		document.getElementById("otherGropeDiv").style.display = "block";
		document.getElementById("groupDiv").className = "col-sm-5 col-xs-5 col-md-5";
		document.forms[0].new_grp_nm.style.visibility='visible'; 
		document.forms[0].new_grp_nm.focus();
		document.forms[0].grpNm.value = '';
	}
	else
	{
		document.getElementById("groupDiv").className = "col-sm-9 col-xs-9 col-md-9";
		document.getElementById("otherGropeDiv").style.display = "none";
		document.forms[0].new_grp_nm.style.visibility='hidden'; 
		document.forms[0].grpNm.value=document.forms[0].group_cd[document.forms[0].group_cd.selectedIndex].innerText;
	}
}

function doSubmit()
{
	var module_cd = document.forms[0].module_cd.value;
	var menu_cd = document.forms[0].menu_cd.value;
	var menu_nm = document.forms[0].menu_nm.value;
	var menu_type = document.forms[0].menu_type.value;
	var menu_path = document.forms[0].menu_path.value;
	var group_priority = document.forms[0].group_priority.value;
	
	var group_cd = document.forms[0].group_cd.value;
	var new_grp_nm = document.forms[0].new_grp_nm.value;
	
	var msg="";
	var flag=true;
	
	if(module_cd=="" || trim(module_cd) == "" || module_cd == "0")
	{
		msg+="Please Select Module!\n";
		flag=false;
	}
	if(menu_cd=="" || trim(menu_cd) == "")
	{
		msg+="Please Select Menu!\n";
		flag=false;
	}
	if(menu_nm=="" || trim(menu_nm) == "")
	{
		msg+="Please Enter Menu Name!\n";
		flag=false;
	}
	if(menu_path=="" || trim(menu_path) == "")
	{
		msg+="Please Enter Menu Path!\n";
		flag=false;
	}
	if(menu_type=="" || trim(menu_type) == "")
	{
		msg+="Please Select Menu Type!\n";
		flag=false;
	}
	if(group_priority=="" || trim(group_priority) == "")
	{
		msg+="Please Enter Menu Priority!\n";
		flag=false;
	}
	
	if(group_cd == "other")
	{
		if(new_grp_nm=="" || trim(new_grp_nm) == "")
		{
			msg+="Please Enter Sub Module Name!\n";
			flag=false;
		}
	}
	else if(group_cd == "0")
	{
		msg+="Please Select Sub Module Name!\n";
		flag=false;
	}
	
	document.forms[0].grpNm.value=document.forms[0].group_cd[document.forms[0].group_cd.selectedIndex].innerText;
	
	if(flag)
	{
		var a
		if(menu_cd!="0")
		{
			a= confirm("Do you Want to Modify Menu Detail?");
			document.forms[0].opration.value="MODIFY";
		}
		else
		{
			a= confirm("Do you Want to Add Menu Detail?");	
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
<jsp:useBean class="com.etrm.fms.admin.DataBean_Admin" id="dbadmin" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String module_cd=request.getParameter("module_cd")==null?"0":request.getParameter("module_cd");
String menu_cd=request.getParameter("menu_cd")==null?"0":request.getParameter("menu_cd");

dbadmin.setCallFlag("MENU_MST");
dbadmin.setModule_cd(module_cd);
dbadmin.setMenu_cd(menu_cd);
dbadmin.init();

Vector VMODULE_CD = dbadmin.getVMODULE_CD();
Vector VMODULE_NM = dbadmin.getVMODULE_NM();
Vector VGROUP_CD = dbadmin.getVGROUP_CD();
Vector VGROUP_NM = dbadmin.getVGROUP_NM();

Vector VMENU_CD = dbadmin.getVMENU_CD();
Vector VMENU_NM = dbadmin.getVMENU_NM();
Vector VMENU_GROUP_CD = dbadmin.getVMENU_GROUP_CD();
Vector VMENU_GROUP_NM = dbadmin.getVMENU_GROUP_NM();

String group_cd=dbadmin.getGroup_cd();
if(group_cd.equals("")){group_cd="0";}
String menu_name = dbadmin.getMenu_name();
String menu_path = dbadmin.getMenu_path();
String menu_type = dbadmin.getMenu_type();
String group_priority = dbadmin.getGroup_priority();
String status_flag = dbadmin.getStatus_flag();
if(status_flag.equals("")){status_flag="Y";}

%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_admin" >
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
				<div class="card-header cdheader topheader">
				    Add/Modify Menu
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<label class="form-label"><b>Module Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-9 col-xs-9 col-md-9">  
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
					</div>
					<div class="row m-b-5">	
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Menu<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-9 col-xs-9 col-md-9">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="menu_cd" id="menu_cd" onchange="refresh();">
										<option value="0">New Menu</option>
										<%for(int i=0;i<VMENU_CD.size();i++){%>
											<option value="<%=VMENU_CD.elementAt(i)%>"><%=VMENU_NM.elementAt(i)%></option>
										<%}%>
									</select>
									<script>document.forms[0].menu_cd.value='<%=menu_cd%>';</script>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Menu Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						
						<div class="col-sm-9 col-xs-9 col-md-9">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="menu_nm" value="<%=menu_name%>" size="40" maxLength="50" title="Max Length 50 chars">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<label class="form-label"><b>Sub Module Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div id="groupDiv" class="col-sm-9 col-xs-9 col-md-9">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="group_cd" id="group_cd" onchange="hidegrp_id();" onblur="hidegrp_id();">
										<option value="0"> -- Select -- </option>
										<option value="other">other</option>
										<%for(int i=0;i<VMENU_GROUP_CD.size();i++){%>
											<option value="<%=VMENU_GROUP_CD.elementAt(i)%>"><%=VMENU_GROUP_NM.elementAt(i)%></option>
										<%}%>
									</select>
									<script>document.forms[0].group_cd.value = '<%=group_cd%>';</script>
									<input type="hidden" name="grpNm" value="">
				    			</div>
				  			</div>
						</div>
						<div id="otherGropeDiv" class="col-sm-4 col-xs-4 col-md-4" style="display:none;">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="new_grp_nm" value="" maxLength="25" style="visibility: hidden;">
				    			</div>
				    		</div> 
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<label class="form-label"><b>Menu Path<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-9 col-xs-9 col-md-9">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="menu_path" value="<%=menu_path%>" maxLength="60" title="Max Length 60 chars">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<label class="form-label"><b>Menu Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-9 col-xs-9 col-md-9">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="menu_type" id="menu_type">
			      						<option value="F" <%if(menu_type.equals("F")){%> selected="selected" <%}%> >Form</option>
			      						<option value="R" <%if(menu_type.equals("R")){%> selected="selected" <%}%> >Report</option>
									</select>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<label class="form-label"><b>Menu Priority<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-9 col-xs-9 col-md-9">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="group_priority" value="<%=group_priority%>" onkeyup="checkForNumber(this);" maxLength="2">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<label class="form-label"><b>Status</b></label>
						</div>
						<div class="col-sm-9 col-xs-9 col-md-9">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="form-check form-switch">
										<input class="form-check-input" name="active" type="checkbox" role="switch" id="flexSwitchCheckChecked" <%if(status_flag.equals("Y")){ %>checked<%} %> onclick="setActiveInactive(this);">
									  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb">
									  		Active
									  	</label>
									  	<input type="hidden" name="status_flag" id="status_flag" value="<%=status_flag%>">
									</div>
									<script>setActiveInactive(document.forms[0].active)</script>
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
		<div class="col-md-2 col-sm-2 col-xs-2"></div>		
	</div>
</div>


<input type="hidden" name="option" value="MENU_MST">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="pre_module_cd" value="<%=module_cd%>">

<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">

<input type="hidden" name="u" value="<%=u%>">
</form>
</body>
</html>