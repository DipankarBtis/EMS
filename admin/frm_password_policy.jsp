<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh()
{
	var eff_dt = document.forms[0].eff_dt.value;

	var u = document.forms[0].u.value;
	
	var url = "frm_password_policy.jsp?u="+u+"&eff_dt="+eff_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var eff_dt = document.forms[0].eff_dt.value;
	
	var uid_min_length = document.getElementById('uid_min_length').value;
	var uid_max_length = document.getElementById('uid_max_length').value;
	
	var pid_min_length = document.getElementById('pid_min_length').value;
	var pid_max_length = document.getElementById('pid_max_length').value;
	
	var no_password_notrep = document.getElementById('no_password_notrep').value;
	var password_exp = document.getElementById('password_exp').value;
	var rem_days = document.getElementById('rem_days').value;
	var dormant_days = document.getElementById('dormant_days').value;
	
	var otp_timer = document.getElementById('otp_timer').value;		//OTP
	var max_otp = document.getElementById('max_otp').value;			//OTP
	var otp_min_length = document.getElementById('otp_min_length').value;	//OTP
	var otp_max_length = document.getElementById('otp_max_length').value;	//OTP	
	
	/* var uid_dt = document.getElementById('uid_dt').value ;
	var pid_dt = document.getElementById('pid_dt').value ;
	var no_password_notrep_dt = document.getElementById('no_password_notrep_dt').value ;
	var password_exp_dt = document.getElementById('password_exp_dt').value ;
	var rem_days_dt = document.getElementById('rem_days_dt').value ; */
	
	var msg="";
	var flag=true;
	
	if(uid_min_length == "" || trim(uid_min_length) == "")
	{
		msg+="User ID minimum length is Missing!\n"
		flag=false;
	}
	if(uid_max_length == "" || trim(uid_max_length) == "")
	{
		msg+="User ID maximum length is Missing!\n"
		flag=false;
	}
	if(pid_min_length == "" || trim(pid_min_length) == "")
	{
		msg+="Password minimum length is Missing! \n"
		flag=false;
	}
	if(pid_max_length == "" || trim(pid_max_length) == "")
	{
		msg+="Password maximum length is Missing! \n"
		flag=false;
	}
	
	if(no_password_notrep == "" || trim(no_password_notrep) == "")
	{
		msg+="Enter No. of previous passwords not to repeat!\n"
		flag=false;
	}
	else 
	{
		if(parseInt(no_password_notrep)>14 || parseInt(no_password_notrep)< 3)
		{
			msg+="Previous Passwords No Repeat Count : Value range <3 - 14>!\n"
			flag=false;
		}
	}	
		
	if(password_exp == "" || trim(password_exp) == "")
	{
		msg+="Enter Password Expiry Days!\n"
		flag=false;
	}
	else 
	{
		if(parseInt(password_exp)>35 || parseInt(password_exp)< 7)
		{
			msg+="Password Expiry Days : Value range <7 - 35>!\n"
			flag=false;
		}
	} 	
	
	if(rem_days == "" || trim(rem_days) == "")
	{
		msg+="Enter Reminder days!\n"
		flag=false;
	}
	else 
	{
		if(parseInt(rem_days)>10 || parseInt(rem_days)< 3)
		{
			msg+="Password Expiry Reminder Days : Value range <3 - 10>!\n"
			flag=false;
		}
	}	
	
	if(dormant_days == "" || trim(dormant_days) == "")
	{
		msg+="Enter Dormant Tolerance Days!\n"
		flag=false;
	}
	else 
	{
		if(parseInt(dormant_days)>365 || parseInt(dormant_days)< 10)
		{
			msg+="Dormant Tolerance Days : Value range <10 - 365>!\n"
			flag=false;
		}
	}
	
	if(trim(pid_min_length) != "" && trim(pid_max_length) != "")
	{
		if(parseInt(pid_min_length)>30 || parseInt(pid_min_length)< 4)
		{
			msg+="Password Length(Min) : Value range <4 - 30>!\n"
			flag=false;
		}
		if(parseInt(pid_max_length)>30 || parseInt(pid_max_length) < parseInt(pid_min_length))
		{
			msg+="Password Length(Max) : Value range <"+pid_min_length+" - 30>!\n"
			flag=false;
		}
	}
	
	if(trim(uid_min_length) != "" && trim(uid_max_length) != "")
	{
		if(parseInt(uid_min_length)>10 || parseInt(uid_min_length)< 4)
		{
			msg+="User ID Length(Min) : Value range <4 - 10>!\n"
			flag=false;
		}
		if(parseInt(uid_max_length)>10 || parseInt(uid_max_length) < parseInt(uid_min_length))
		{
			msg+="User ID Length(Max) : Value range <"+uid_min_length+" - 10>!\n"
			flag=false;
		}
	}
	
	if(otp_timer == "" || trim(otp_timer) == "")
	{
		msg+="OTP Max Wait(min) is missing\n"
		flag=false;
	}
	if(max_otp == "" || trim(max_otp) == "")
	{
		msg+="Max OTP is missing\n"
		flag=false;
	} 
	if(otp_max_length == "" || trim(otp_max_length) == "")
	{
		msg+="OTP Length (max) is missing\n"
		flag=false;
	}
	if(otp_min_length == "" || trim(otp_min_length) == "")
	{
		msg+="OTP Length (min) is missing\n"
		flag=false;
	}
	if(parseInt(otp_timer)>=6 || parseInt(otp_timer)<=1)
	{
		msg+="OTP Max Wait (min) : Value range <2 - 5>!\n"
		flag=false;
	}
	if(parseInt(max_otp)>=4 || parseInt(max_otp)<=1)
	{
		msg+="Max Otp : Value range <2 - 3>!\n"
		flag=false;
	}
	if(trim(otp_min_length) != "" && trim(otp_max_length) != "")
	{
		if(parseInt(otp_min_length)>6 || parseInt(otp_min_length)<4)
		{
			msg+="OTP length (min) : Value range <4 - 6>!\n"
			flag=false;
		}
		if(parseInt(otp_max_length)>6 || parseInt(otp_max_length) < parseInt(otp_min_length))
		{
			msg+="OTP length (max) : Value range<"+otp_min_length+" - 6>!\n"
			flag=false;
		}
	}
	if(flag)
	{
		var a= confirm("Changed Admin Policy will be effective from "+eff_dt+".\n\nDo you want to continue?")
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

function validateNumInput()
{
	var uid_min_length = document.getElementById('uid_min_length').value;
	var uid_max_length = document.getElementById('uid_max_length').value;
	
	var pid_min_length = document.getElementById('pid_min_length').value;
	var pid_max_length = document.getElementById('pid_max_length').value;
	
	var no_password_notrep = document.getElementById('no_password_notrep').value;
	var password_exp = document.getElementById('password_exp').value;
	var rem_days = document.getElementById('rem_days').value;
	var dormant_days = document.getElementById('dormant_days').value;
	
	var otp_timer = document.getElementById('otp_timer').value;		//OTP
	var max_otp = document.getElementById('max_otp').value;			//OTP
	var otp_min_length = document.getElementById('otp_min_length').value;	//OTP
	var otp_max_length = document.getElementById('otp_max_length').value;	//OTP	
	
	if(isNaN(uid_min_length))
	{
		alert('User ID Length Range must be Numeric!');
		document.getElementById('uid_min_length').value = '';
	}
	if(isNaN(uid_max_length))
	{
		alert('User ID Length Range must be Numeric!');
		document.getElementById('uid_max_length').value = '';
	}
	if(isNaN(pid_min_length))
	{
		alert('Password Length Range must be Numeric!');
		document.getElementById('pid_min_length').value = '';
	}
	if(isNaN(pid_max_length))
	{
		alert('Password Length Range must be Numeric!');
		document.getElementById('pid_max_length').value = '';
	}
	if(isNaN(no_password_notrep))
	{
		alert('No. of previous passwords not to repeat must be Numeric!');
		document.getElementById('no_password_notrep').value = '';
	}
	if(isNaN(password_exp))
	{
		alert('Password Expiry Days must be Numeric!');
		document.getElementById('password_exp').value = '';
	}
	if(isNaN(rem_days))
	{
		alert('Reminder days must be Numeric!');
		document.getElementById('rem_days').value = '';
	}
	if(isNaN(dormant_days))
	{
		alert('Dormant Tolerance Days must be Numeric!');
		document.getElementById('dormant_days').value = '';
	}
	if(isNaN(otp_timer))
	{
		alert('OTP Max Wait must be Numeric!');
		document.getElementById('otp_timer').value = '';
	}
	if(isNaN(max_otp))
	{
		alert('Max OTP must be Numeric!');
		document.getElementById('max_otp').value = '';
	}
	if(isNaN(otp_min_length))
	{
		alert('OTP length must be Numeric!');
		document.getElementById('otp_min_length').value = '';
	}
	if(isNaN(otp_max_length))
	{
		alert('OTP length must be Numeric!');
		document.getElementById('otp_max_length').value = '';
	}
}

function validateDateInput()
{
	
	var current_dt = new Date().toISOString().split('T')[0];
	
	if(uid_dt < current_dt){
		alert('Please enter a date not less than the current date.');
		document.getElementById('uid_dt').value = current_dt;
	}
}

function validate_value()
{
	var dormant_days = document.getElementById('dormant_days').value;
	var no_password_notrep = document.getElementById('no_password_notrep').value;
	var password_exp = document.getElementById('password_exp').value;
	var rem_days = document.getElementById('rem_days').value;
	var otp_timer = document.getElementById('otp_timer').value;		//OTP
	var max_otp = document.getElementById('max_otp').value;			//OTP
	
	if(dormant_days != "" || trim(dormant_days) != "")
	{
		if(parseInt(dormant_days)>365 || parseInt(dormant_days)< 10)
		{
			alert("Dormant Tolerance Days : Value range <10 - 365>!");
			document.getElementById('dormant_days').value="";
			flag=false;
		}
	}
	if(no_password_notrep != "" || trim(no_password_notrep) != "")
	{
		if(parseInt(no_password_notrep)>14 || parseInt(no_password_notrep)< 3)
		{
			alert("Previous Passwords No Repeat Count : Value range <3 - 14>!");
			document.getElementById('no_password_notrep').value="";
			flag=false;
		}
	}
	if(password_exp != "" || trim(password_exp) != "")
	{
		if(parseInt(password_exp)>35 || parseInt(password_exp)< 7)
		{
			alert("Password Expiry Days : Value range <7 - 35>!");
			document.getElementById('password_exp').value="";
			flag=false;
		}
	}
	if(rem_days != "" || trim(rem_days) != "")
	{
		if(parseInt(rem_days)>10 || parseInt(rem_days)< 3)
		{
			alert("Password Expiry Reminder Days : Value range <3 - 10>!");
			document.getElementById('rem_days').value="";
			flag=false;
		}
	}
	if(otp_timer !="" || trim(otp_timer) != "")
	{
		if(parseInt(otp_timer)>5 || parseInt(otp_timer)<2)
		{
			alert("OTP Max Wait (min) : Value range <2 - 5>!");
			document.getElementById('otp_timer').value="";	
			flag=false;
		}
	}
	if(max_otp != "" || trim(max_otp) != "")
	{
		if(max_otp>3 || max_otp<2)
		{
			alert("Max Otp : Value range <2 - 3>!");
			document.getElementById('max_otp').value="";
			flag=false;
		}
	}
}


function validate_min_value()
{
	var uid_min_length = document.getElementById('uid_min_length').value;
	var uid_max_length = document.getElementById('uid_max_length').value;
	var pid_min_length = document.getElementById('pid_min_length').value;
	var pid_max_length = document.getElementById('pid_max_length').value;
	
	var otp_min_length = document.getElementById('otp_min_length').value;	//OTP
	var otp_max_length = document.getElementById('otp_max_length').value;	//OTP	
	var msg="";
	var flag=true;
	
	if(trim(uid_min_length) != "" && trim(uid_max_length) != "")
	{
		if(parseInt(uid_min_length)>10 || parseInt(uid_min_length)< 4)
		{
			msg+="User ID Length(Min) : Value range <4 - 10>!\n";
			document.getElementById('uid_min_length').value="";
			flag=false;
		}
	}
	if(trim(pid_min_length) != "" && trim(pid_max_length) != "")
	{
		if(parseInt(pid_min_length)>30 || parseInt(pid_min_length)< 4)
		{
			msg+="Password Length(Min) : Value range <4 - 30>!\n";
			document.getElementById('pid_min_length').value="";
			flag=false;
		}
	}
	if(trim(otp_min_length) != "" && trim(otp_max_length) != "")
	{
		if(parseInt(otp_min_length)>6 || parseInt(otp_min_length)<4)
		{
			msg+="OTP length (min) : Value range <4 - 6>!\n";
			document.getElementById('otp_min_length').value = "";
			flag=false;
		}
	}
	if(flag==false)
	{
		alert(msg);
	}
}
function validate_max_value()
{
	var uid_min_length = document.getElementById('uid_min_length').value;
	var uid_max_length = document.getElementById('uid_max_length').value;
	var pid_min_length = document.getElementById('pid_min_length').value;
	var pid_max_length = document.getElementById('pid_max_length').value;
	var otp_min_length = document.getElementById('otp_min_length').value;	//OTP
	var otp_max_length = document.getElementById('otp_max_length').value;	//OTP	
	var msg="";
	var flag=true;
	if(trim(uid_min_length) != "" && trim(uid_max_length) != "")
	{
		if(parseInt(uid_max_length)>10 || parseInt(uid_max_length) < parseInt(uid_min_length))
		{
			msg+="User ID Length(Max) : Value range <"+uid_min_length+" - 10>!\n";
			uid_max_length = document.getElementById('uid_max_length').value="";
			flag=false;
		}
	}
	if(trim(pid_min_length) != "" && trim(pid_max_length) != "")
	{
		 if(parseInt(pid_max_length)>30 || parseInt(pid_max_length) < parseInt(pid_min_length))
		{
			msg+="Password Length(Max) : Value range <"+pid_min_length+" - 30>!\n";
			document.getElementById('pid_max_length').value="";
			flag=false;
		} 
	}
	if(trim(otp_min_length) != "" && trim(otp_max_length) != "")
	{
		if(parseInt(otp_max_length)>6 || parseInt(otp_max_length) < parseInt(otp_min_length))
		{
			msg+="OTP length (max) : Value range<"+otp_min_length+" - 6>!\n";
			document.getElementById('otp_max_length').value="";
			flag=false;
		}
	}
	if(flag==false)
	{
		alert(msg);
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.admin.DataBean_Admin" id="dbadmin" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateutil" scope="page"></jsp:useBean>

<%
String user_id = ""+session.getAttribute("emp_uid");
String user_cd = ""+session.getAttribute("emp_cd");
if(session.getAttribute("emp_uid")==null)
{
	user_id="";
	user_cd="";
}
String sysdate = ""+dateutil.getSysdate();
String eff_dt=request.getParameter("eff_dt")==null?sysdate:request.getParameter("eff_dt");

dbadmin.setCallFlag("PASSWORD_POLICY");
dbadmin.setEmp_cd(user_cd);
dbadmin.setEff_dt(eff_dt);
dbadmin.init();

String uid_min_length = dbadmin.getUid_min_length();
String uid_max_length = dbadmin.getUid_max_length();
String pid_min_length = dbadmin.getPid_min_length();
String pid_max_length = dbadmin.getPid_max_length();
String no_password_notrep = dbadmin.getNo_password_notrep();
String password_exp = dbadmin.getPassword_exp();
String rem_days = dbadmin.getRem_days();
String dormant_days = dbadmin.getDormant_days();
String max_admn = dbadmin.getMax_admn();
String max_sup_admn = dbadmin.getMax_sup_admn();
String isSupAdmn = dbadmin.getIsSupAdmn();
String otp_timer = dbadmin.getOtp_timer();
String max_otp = dbadmin.getMax_otp();
String otp_max_length = dbadmin.getOtp_max_length();
String otp_min_length = dbadmin.getOtp_min_length();

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
				<div class="card-header cdheader topheader">
				    User/Password Policy
				</div>
				<div class="card-body cdbody">
					<div align="right">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Effective On</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm " name="eff_dt" value="<%=eff_dt%>" size="8" maxLength="10" autocomplete="off" onchange="validateDate(this);" readonly="readonly"> <!-- refresh();"> -->
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						<!-- date fmsdtpick -->
		      						</div>
		      						<script>
										document.forms[0].eff_dt.value="<%=eff_dt%>"
									</script>
				    			</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> User Policy</label>
					</div>							
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>User ID Length <span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="input-group input-group-sm">
								<input type="text" class="form-control form-control-sm" name="uid_min_length" id="uid_min_length" placeholder="Min" size="10" value="<%=uid_min_length %>" style="text-align: right;" onblur="validateNumInput();" onchange="validate_min_value()">
	   							<span class="input-group-text"><b>TO</b></span>
	   							<input type="text" class="form-control form-control-sm" name="uid_max_length" id="uid_max_length" placeholder="Max" size="10" value="<%=uid_max_length %>" style="text-align: right;" onblur="validateNumInput();" onchange="validate_max_value()">
	   						</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">  </div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<label class="form-label"><b>Dormant Tolerance Days <span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="input-group input-group-sm">
								<input type="text" class="form-control form-control-sm" name="dormant_days" id="dormant_days" size="10" value="<%=dormant_days %>" style="text-align: right;" onblur="validateNumInput();" onchange="validate_value()">	   							
	   						</div>
						</div>
					</div>
					<div class="row m-b-5" <%if(!isSupAdmn.equals("Y")){ %>style="display: none;"<%} %>>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Max Administrators<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3"> 
							<div class="input-group input-group-sm">
								<input type="text" class="form-control form-control-sm" name="max_admn" id="max_admn" size="10" value="<%=max_admn %>" style="text-align: right;" onblur="validateNumInput();" disabled="disabled">
								<input type="hidden" class="form-control form-control-sm" name="max_admn" id="max_admn" size="10" value="<%=max_admn %>" style="text-align: right;" onblur="validateNumInput();">
	   						</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">  </div>
						<div class="col-sm-3 col-xs-3 col-md-3"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Max Admins<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3"> 
							<div class="input-group input-group-sm">
								<input type="text" class="form-control form-control-sm" name="max_sup_admn" id="max_sup_admn" size="10" value="<%=max_sup_admn %>" style="text-align: right;" onblur="validateNumInput();" disabled="disabled">
								<input type="hidden" class="form-control form-control-sm" name="max_sup_admn" id="max_sup_admn" size="10" value="<%=max_sup_admn %>" style="text-align: right;" onblur="validateNumInput();">
	   						</div>
						</div>
					</div>
					&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Password Policy</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Password Length<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="input-group input-group-sm">
								<input type="text" class="form-control form-control-sm" name="pid_min_length" id="pid_min_length" placeholder="Min" size="10" value="<%=pid_min_length %>" style="text-align: right;" onblur="validateNumInput();" onchange="validate_min_value();">
	   							<span class="input-group-text"><b>TO</b></span>
	   							<input type="text" class="form-control form-control-sm" name="pid_max_length" id="pid_max_length" placeholder="Max" size="10" value="<%=pid_max_length %>" style="text-align: right;" onblur="validateNumInput();" onchange="validate_max_value();">
	   						</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1"> </div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<label class="form-label"><b>Previous Passwords No Repeat Count<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="input-group input-group-sm">
								<input type="text" class="form-control form-control-sm" name="no_password_notrep" id="no_password_notrep" size="10" value="<%=no_password_notrep %>" style="text-align: right;" onblur="validateNumInput();" onchange="validate_value()">
	   						</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Password Expiry Days<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="input-group input-group-sm">
								<input type="text" class="form-control form-control-sm" name="password_exp" id="password_exp" size="10" value="<%=password_exp %>" style="text-align: right;" onblur="validateNumInput();" onchange="validate_value()">
	   						</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1"> </div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
				    			<label class="form-label"><b>Expiry Reminder days<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3"> 
							<div class="input-group input-group-sm">
								<input type="text" class="form-control form-control-sm" name="rem_days" id="rem_days" size="10" value="<%=rem_days %>" style="text-align: right;" onblur="validateNumInput();" onchange="validate_value()">
	   						</div>
						</div>
					</div>
					
					<!-- FOr OTP Details Configuration -->
					&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Login OTP Policy</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>OTP Max Wait (in min)<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="input-group input-group-sm">
								<input type="text" class="form-control form-control-sm" name="otp_timer" id="otp_timer" size="10" value="<%=otp_timer %>" style="text-align: right;" onblur="validateNumInput();" onchange="validate_value()">
	   						</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1"> </div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<label class="form-label"><b>Max OTP <span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="input-group input-group-sm">
								<input type="text" class="form-control form-control-sm" name="max_otp" id="max_otp" size="10" value="<%=max_otp %>" style="text-align: right;" onblur="validateNumInput();" onchange="validate_value()">
	   						</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>OTP Length<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="input-group input-group-sm">
								<input type="text" class="form-control form-control-sm" name="otp_min_length" id="otp_min_length" placeholder="Min" size="10" value="<%=otp_min_length %>" style="text-align: right;" onblur="validateNumInput();" onchange="validate_min_value();">
	   							<span class="input-group-text"><b>TO</b></span>
	   							<input type="text" class="form-control form-control-sm" name="otp_max_length" id="otp_max_length" placeholder="Max" size="10" value="<%=otp_max_length %>" style="text-align: right;" onblur="validateNumInput();" onchange="validate_max_value();">
	   						</div> 
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1"> </div>
						<%-- <div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
				    			<label class="form-label"><b>Expiry Reminder days<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3"> 
							<div class="input-group input-group-sm">
								<input type="text" class="form-control form-control-sm" name="rem_days" id="rem_days" size="10" value="<%=rem_days %>" style="text-align: right;" onblur="validateNumInput();">
	   						</div>
						</div> --%>
					</div>
					
					
				</div>
				&nbsp;
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
		<div class="col-md-3 col-sm-3 col-xs-3"></div>
	</div>
</div>

<input type="hidden" name="option" value="PASSWORD_POLICY">

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