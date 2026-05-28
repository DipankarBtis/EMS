<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function doSubmit()
{
	var user_cd = document.forms[0].user_cd.value;
	var user_id = document.forms[0].user_id.value;
	var current_pwd = document.forms[0].current_pwd.value;
	var new_pwd = document.forms[0].new_pwd.value;
	var confirm_pwd = document.forms[0].confirm_pwd.value;
	
	var msg="";
	var flag=true;
	
	if(user_cd == "" || trim(user_cd) == "")
	{
		msg+="User CD is Missing!\n"
		flag=false;
	}
	if(user_id == "" || trim(user_id) == "")
	{
		msg+="User ID is Missing!\n"
		flag=false;
	}
	if(current_pwd == "" || trim(current_pwd) == "")
	{
		msg+="Enter Current Password!\n"
		flag=false;
	}
	if(new_pwd == "" || trim(new_pwd) == "")
	{
		msg+="Enter New Password!\n"
		flag=false;
	}
	if(confirm_pwd == "" || trim(confirm_pwd) == "")
	{
		msg+="Enter Confirm Password!\n"
		flag=false;
	}
	
	if(user_id == new_pwd)
   	{
		alert("User ID and New Password are must be different ! \n Please try onother one !");
   		document.forms[0].new_pwd.value = '';
   		document.forms[0].confirm_pwd.value = '';
     	document.forms[0].new_pwd.focus();
   		return false;
   	}
	else if(current_pwd == new_pwd)
	{
		alert("Current Password and New Password are must be different ! \n Please try onother one !");
   		document.forms[0].new_pwd.value = '';
   		document.forms[0].confirm_pwd.value = '';
     	document.forms[0].new_pwd.focus();
   		return false;
	}
	else if(confirm_pwd != new_pwd)
   	{
		alert("New Password must be same as Confirm Password !");
     	document.forms[0].confirm_pwd.value = '';
     	document.forms[0].confirm_pwd.focus();
     	return false;
   	}
	
	if(flag)
	{
		var a= confirm("Do you want to Change your Current Password?")
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

function passwd_std_chk(passwd,obj,minimum,maximum)
{
	//minimum="8";
	//maximum="15";
	var min = parseInt(""+minimum);
	var max = parseInt(""+maximum);
	var lenfl = true;

	if(passwd != "")  
	{
		var spacecheck = /^.+\s.+$/g;
		var endspacecheck = /\s+$/;
		var message = "Password not confirming standards defined by Administrator ...\n\n";
		
		if(spacecheck.test(passwd)) 
		{
			lenfl = false;
			message += "- Space not allowed in Password !";
		}
		else if(endspacecheck.test(passwd)) 
		{
			lenfl = false;
			message += "- Space not allowed at the end in Password !";
		}
		else
		{
			if(passwd.length>0)
			{
				if(passwd.length<min || passwd.length>max)
				{
					lenfl = false;
					message += "- Password length should be <"+min+" - "+max+"> alphanumeric characters long !!!\n"
				}
			}
			else
			{
				lenfl = false;
				message += "- Password length should be atleast "+min+" alphanumeric characters long !!!\n";
			}

		    var regex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*]).{"+min+","+max+"}$");
			if(!regex.test(passwd)) 
			{
				lenfl = false;
				message += "- Please, Enter Valid Password !!!\n";	
			}
			
			var userId = document.forms[0].user_id.value;
			if(passwd==userId)
			{
				lenfl = false;
				message += "- Please, Enter different Password - Password can not be same as User Login ID !!!\n";
			}
		}
		if(!lenfl)
		{
			alert(message);
			obj.value = '';
		}	
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.admin.DataBean_Admin" id="dbadmin" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String exped=request.getParameter("exped")==null?"":request.getParameter("exped");

String user_id = ""+session.getAttribute("emp_uid");
String user_cd = ""+session.getAttribute("emp_cd");
if(session.getAttribute("emp_uid")==null)
{
	user_id="";
	user_cd="";
}
dbadmin.setCallFlag("CHANGE_PASSWORD");
dbadmin.init();

String uid_min_length = dbadmin.getUid_min_length();
String uid_max_length = dbadmin.getUid_max_length();
String pid_min_length = dbadmin.getPid_min_length();
String pid_max_length = dbadmin.getPid_max_length();
String no_password_notrep = dbadmin.getNo_password_notrep();
String password_exp = dbadmin.getPassword_exp();
String rem_days = dbadmin.getRem_days();
String dormant_days = dbadmin.getDormant_days();
%>

<%if(msg_type.equals("S")){ %>
<script>
alert("<%=msg%>\n\nPlease Login again with your updated credential!");

var url="../sess/Expire.jsp?expire_msg=PwdChng"
location.replace(url);
</script>
<%} %>

<body>
<%-- <%@ include file="../home/header.jsp"%> --%>
<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_admin">

<div class="box-body">
	<div class="row">
		<div class="col-md-3 col-sm-3 col-xs-3"></div>
		<div class="col-md-6 col-sm-6 col-xs-6">
			<%if(!msg.equals("")){
				if(msg_type.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
				<%}else if(msg_type.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
				<%}
			} %>
			<div class="card cardmain">
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<%if(!exped.equals("")){ %><div align="left"><%=utilmsg.infoMessage("<b>Your Password is Expired, Please change your Password!</b>") %></div>
						<%}else{ %><div align="left"><%=utilmsg.infoMessage("<b>Please change your system generated Password!</b>") %></div><%} %>
					</div>
				</div>
			</div>
			&nbsp;
			<div class="card cardmain">
				<div class="card-header cdheader topheader">
				    Change Password
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<label class="form-label"><b>User ID<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-8 col-xs-8 col-md-8">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<lable class='form-label form-control form-control-sm'><b><%=user_id%></b></lable>
				      				<input type="hidden" name="user_id" value="<%=user_id%>" readonly>
				    				<input type="hidden" name="user_cd" value="<%=user_cd%>">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<label class="form-label"><b>Current Password<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-8 col-xs-8 col-md-8">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="password" class="form-control form-control-sm" name="current_pwd" value="" maxLength="40" placeholder="Current Password..."> <!-- onblur="isPassExist();"> -->
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<label class="form-label"><b>New Password<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-8 col-xs-8 col-md-8">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="password" class="form-control form-control-sm" name="new_pwd" value="" maxLength="40" placeholder="New Password..." onblur="passwd_std_chk(this.value,this,'<%=pid_min_length%>','<%=pid_max_length%>');">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<label class="form-label"><b>Confirm Password<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-8 col-xs-8 col-md-8">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="password" class="form-control form-control-sm" name="confirm_pwd" value="" placeholder="Confirm Password...">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						<%=utilmsg.infoMessage("<b>Note :</b><br><font color='red'>Password to be minimum of "+pid_min_length+" chars long &amp; maximum of "+pid_max_length+" chars long.</font><br><font color='blue'><b>Password should contain following CRITERIA(s):</b><br>(1) Atleast 1 Capital Alphabet,<br>(2) Atleast 1 Small Alphabet,<br>(3) Atleast 1 Numeric Digit, and<br>(4) Atleast 1 Special Character.</font>") %>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-3 col-sm-3 col-xs-3"></div>
	</div>
</div>

<input type="hidden" name="option" value="CHANGE_PASSWORD">

<input type="hidden" name="pid_min_length" value="<%=pid_min_length%>">
<input type="hidden" name="pid_max_length" value="<%=pid_max_length%>">

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

<input type="hidden" name="isRequiredChngPwd" value="Y">

</form>

</body>
</html>