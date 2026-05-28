<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh(opration)
{
	var user_cd=document.forms[0].user_cd.value;
	
	var prv_opration=document.forms[0].prv_opration.value;
	if(prv_opration!=opration)
	{
		user_cd="0";
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_add_modify_user.jsp?opration="+opration+"&user_cd="+user_cd+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function doSubmit(minimum,maximum)
{
	var user_nm = document.forms[0].user_nm.value;
	var user_id = document.forms[0].user_id.value;
	var email_id = document.forms[0].email_id.value;
	var status = document.forms[0].status.value;
	var default_profile = document.forms[0].default_profile.value;
		
	var opration = document.forms[0].opration.value;
	
	var min = parseInt(""+minimum);
	var max = parseInt(""+maximum);

	var msg="";
	var flag=true;
	
	if(opration == "INSERT")
	{
		isExistUserID();
		isExistEmailID();
	}
	
	if(opration == "MODIFY")
	{
		isExistEmailID();
		
		var user_cd = document.forms[0].user_cd.value;
		if(user_cd == "" || user_cd == "0")
		{
			msg+="Please Select User!\n";
			flag=false;
		}
	}
	
	if(user_nm=="" || trim(user_nm)=="")
	{
		msg+="Please Enter User Name!\n";
		flag=false;
	}
	if(user_id=="" || trim(user_id)=="")
	{
		msg+="Please Enter User ID!\n";
		flag=false;
	}
	if(email_id=="" || trim(email_id)=="")
	{
		msg+="Please Enter Email ID!\n";
		flag=false;
	}
	if(status=="" || trim(status)=="")
	{
		msg+="Please Select Status!\n";
		flag=false;
	}
	if(default_profile=="" || trim(default_profile)=="")
	{
		msg+="Please Select Default Profile!\n";
		flag=false;
	}	
	if(user_id.length<min || user_id.length>max)
	{
		msg+="UserID length should be <"+min+" - "+max+"> characters long !!!\n";
		document.forms[0].user_id.value ="";
		
		flag=false;
	}
	
	
	if(flag)
	{
		var a;
		if(opration == "MODIFY")
		{
			a= confirm("Do you Want to Modify User("+user_nm+")?");
		}
		else
		{
			a = confirm("Do you Want to Add New User("+user_nm+")?");
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

var newWindow;
function openList()
{
	var opration = document.forms[0].opration.value;
	
	var url = "rpt_user_list.jsp?opration="+opration;

	if(!newWindow || newWindow.closed)
	{
		newWindow= window.open(url,"User List","top=10,left=70,width=800,height=700,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow= window.open(url,"User List","top=10,left=70,width=800,height=700,scrollbars=1");
	}
}

function refreshChild(user_cd)
{
	var opration = document.forms[0].opration.value;
	
	document.forms[0].user_cd.value=user_cd;
	
	refresh(opration);
}
</script>

</head>
<jsp:useBean class="com.etrm.fms.admin.DataBean_Admin" id="dbadmin" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateutil" scope="page"></jsp:useBean>
<%
String sysdate = ""+dateutil.getSysdate();
String sysdatePlusOneYr = ""+dateutil.getSysdatePlusOneYear();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String user_cd=request.getParameter("user_cd")==null?"0":request.getParameter("user_cd");

dbadmin.setCallFlag("USER_MST");
dbadmin.setEmp_cd(emp_cd);
dbadmin.setUser_cd(user_cd);
dbadmin.setOpration(opration);
dbadmin.init();

Vector VEMP_CD = dbadmin.getVEMP_CD();
Vector VEMP_NM = dbadmin.getVEMP_NM();

Vector VCOMPANY_CD = dbadmin.getVCOMPANY_CD();
Vector VCOMPANY_NM = dbadmin.getVCOMPANY_NM();
Vector VCOMPANY_ABBR = dbadmin.getVCOMPANY_ABBR();

String emp_nm = dbadmin.getEmp_nm();
String emp_uid = dbadmin.getEmp_uid();
String email_id = dbadmin.getEmail_id();
String emp_status = dbadmin.getEmp_status();
if(emp_status.equals("")){
	emp_status="Y";
}

String remark = dbadmin.getRemark();
String phone_no = dbadmin.getPhone_no();
String mobile_no = dbadmin.getMobile_no();
String default_profile = dbadmin.getDefault_profile();

String uid_min_length = dbadmin.getUid_min_length();
String uid_max_length = dbadmin.getUid_max_length();
String pid_min_length = dbadmin.getPid_min_length();
String pid_max_length = dbadmin.getPid_max_length();
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_admin">

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
				<div class="card-header cdheader ">
				    <div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Add/Modify User
					    </div>
					    <div class="btn-group">
						  <label class="btn btn-outline-secondary btngrp <%if(opration.equals("INSERT")){%>btnactive<%}%>" onclick="refresh('INSERT');"><i class="fa fa-plus-circle"></i>&nbsp;New</label>
						  <label class="btn btn-outline-secondary btngrp <%if(opration.equals("MODIFY")){%>btnactive<%}%>" onclick="refresh('MODIFY');"><i class="fa fa-edit"></i>&nbsp;Modify</label>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5" <%if(opration.equals("INSERT")) {%>style="display: none;"<%} %>>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
			    				<div class="col-sm-12 col-xs-12 col-md-12">
			    					<span class="btn btn-info btn-sm select_btn" onclick="openList();" style="font-weight: bold;">
				    					<i class="fa fa-list-ul"></i>&nbsp;Select User
				    				</span>
			    				</div>
			    			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="user_cd" id="user_cd" onchange="refresh('<%=opration%>');" style="pointer-events: none;">
				      					<option value="0">--Select--</option>
				      					<%for(int i=0; i<VEMP_CD.size(); i++){ %>
				      					<option value="<%=VEMP_CD.elementAt(i)%>"><%=VEMP_NM.elementAt(i)%></option>
				      					<%} %>
				      				</select>
				      				<script>document.forms[0].user_cd.value='<%=user_cd%>';</script>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>User Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="user_nm" value="<%=emp_nm%>" maxLength="40">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>User ID<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="user_id" value="<%=emp_uid%>" <%if(opration.equals("MODIFY")) {%>readonly<%} %> onblur="isExistUserID();">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Email ID<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="email_id" value="<%=email_id%>" maxLength="40" onBlur="validateEmail(this);isExistEmailID();">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Status<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="status" id="status">
										<option value="Y">Enabled</option>
										<option value="N">Disabled</option>
										<!-- <option value="D">Dormant</option> -->
									</select>
									<script>document.forms[0].status.value='<%=emp_status%>';</script>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Phone No</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="phone_no" value="<%=phone_no%>" maxLength="20">
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Mobile No</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="mobile_no" value="<%=mobile_no%>" maxLength="20">
				    			</div>
				    		</div>
				    	</div>
				    </div>
				    <div class="row m-b-5">
				    <div class="col-sm-2 col-xs-2 col-md-2">  
						<div class="form-group row">
			    			<label class="form-label"><b>Default Profile<span class="s-red">*</span></b></label>
			  			</div>
					</div>
					<div class="col-sm-4 col-xs-4 col-md-4">  
						<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="default_profile" id="default_profile">
				      					<option value="">--Select--</option>
				      					<%for(int i=0; i<VCOMPANY_CD.size(); i++){ %>
				      						<option value="<%=VCOMPANY_CD.elementAt(i)%>"><%=VCOMPANY_ABBR.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].default_profile.value='<%=default_profile%>';</script>
				    			</div>
				  			</div>
					</div>						
				    <%-- <div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Default Profile<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="default_profile" id="default_profile">
				      					<option value="">--Select--</option>
				      					<%for(int i=0; i<VCOMPANY_CD.size(); i++){ %>
				      						<option value="<%=VCOMPANY_CD.elementAt(i)%>"><%=VCOMPANY_ABBR.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].default_profile.value='<%=default_profile%>';</script>
				    			</div>
				  			</div>
						</div>--%>											
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
				    				<textarea class="form-control form-control-sm" name="remark" cols="75" rows="2" maxlength="150"><%=remark%></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit('<%=uid_min_length%>','<%=uid_max_length%>');">
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

<input type="hidden" name="option" value="USER_MST">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="prv_opration" value="<%=opration%>">

<input type="hidden" name="pwd_min_len" value="<%=pid_min_length%>">
<input type="hidden" name="pwd_max_len" value="<%=pid_max_length%>">

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

<%if(opration.equals("INSERT") && !user_cd.equals("0")){ %>
<script>
document.forms[0].user_nm.readOnly=true;
document.forms[0].user_id.readOnly=true;
//document.forms[0].email_id.readOnly=true;
</script>
<%}else if(opration.equals("INSERT")){ %>
<script>document.forms[0].status.style.pointerEvents = "none";</script>
<%} %>

<script>
function isExistUserID()
{
	var opration = document.forms[0].opration.value;
	var user_id = document.forms[0].user_id.value;
	var user_cd = document.forms[0].user_cd.value;
	
	var info="";
	
	$.post("../servlet/DB_Admin_Ajax?setCallType=IsExistUserID&opration="+opration+"&user_id="+user_id+"&user_cd="+user_cd, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.USER_DTL, function(index_1, json_1) {
				if(parseInt(json_1.USER_ID) > 0)
				{
					info+="User ID is already Exist!";
				}
				
				if(info!="")
				{
					alert(info)
					document.forms[0].user_id.value="";
				}
			});
		});
	});
}
function isExistEmailID()
{
	var opration = document.forms[0].opration.value;
	var email_id = document.forms[0].email_id.value;
	
	var info="";
	
	$.post("../servlet/DB_Admin_Ajax?setCallType=IsExistEmailID&opration="+opration+"&email_id="+email_id, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.USER_DTL, function(index_1, json_1) {
				if(parseInt(json_1.EMAIL_ID) > 0)
				{
					info+="Account already Exist for entered Email Id!";
				}
				
				if(info!="")
				{
					alert(info)
					document.forms[0].email_id.value="";
				}
			});
		});
	});
}
</script>
</form>
</body>
</html>