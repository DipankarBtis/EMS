<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>FMSng</title>

<link href="../bootstrap/bootstrap5/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="../font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="../css/datepicker/bootstrap-datepicker3.css">
<link rel="stylesheet" type="text/css" href="../css/util.css">
<link rel="stylesheet" type="text/css" href="../css/main.css">
<link href="../css/navbar.css" rel="stylesheet">
<link href="../css/responsive.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="../css/common.css">

<script src="../bootstrap/bootstrap5/js/bootstrap.bundle.min.js"></script>
<script src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/datepicker/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="../js/datepicker/bootstrap-datepicker.es.min.js"></script>

<script src="../js/util.js"></script>
<script>
function checkVal(counterVal) 
{
	var flag=true;
	var otpval = document.forms[0].otpval.value;  
  	var msg = "";
	var msg_div=document.getElementById("msg");
  	var url = document.forms[0].url.value;
  	var emp_uid = document.forms[0].emp_uid.value;
  	var emp_nm = document.forms[0].emp_nm.value;
  	var emp_cd = document.forms[0].emp_cd.value;
  	var email = document.forms[0].email.value;
  	
  	url = "profile_selection.jsp?comp_profile=1";
  	
	if(otpval != "")
  	{
  		document.forms[0].action = "otp_handler.jsp?url="+url+"&otp_flag="+otp_flag+"&resend_flag="+false+"&emp_uid="+emp_uid+"&emp_cd="+emp_cd+"&emp_nm="+emp_nm+"&email="+email;
		document.forms[0].submit();
  	}
  	else
  	{
  		msg = "OTP is Not Entered"
  		msg_div.innerHTML = "<div class='container'><div class='alert alert-danger'><i class='fa fa-exclamation-triangle fa-lg'></i>&nbsp;"+msg+"</div></div>";
  	}
}

function resendOTP()
{
	//var comp_cd = document.forms[0].comp_cd.value;
	var emp_uid = document.forms[0].emp_uid.value;
  	var emp_nm = document.forms[0].emp_nm.value;
  	var emp_cd = document.forms[0].emp_cd.value;
  	var email = document.forms[0].email.value;
	var url = document.forms[0].url.value;
	var count_trials = document.forms[0].count_trials.value;
	document.forms[0].action = "otp_handler.jsp?url="+url+"&otp_flag="+otp_flag+"&resend_flag="+true+"&count_trials="+count_trials+"&emp_uid="+emp_uid+"&emp_cd="+emp_cd+"&emp_nm="+emp_nm+"&email="+email;
	//document.forms[0].action = "otp_handler.jsp?url="+url+"&otp_flag="+otp_flag+"&comp_cd="+comp_cd+"&emp_cd="+emp_cd+"&email="+email+"&resend_flag="+true+"&count_trials="+count_trials;
	document.forms[0].submit();
}

</script>
<style>
body {
	background-image: url("../images/shell_login_bg_pic.jpg");	
   	background-attachment: fixed;  
  	background-size: 100% 100%;
  	/*   
  	background-size: cover;
  	margin: 0;
  	padding: 0; 
  	*/
}
</style>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.admin.DataBean_Admin" id="dbadmin" scope="request"></jsp:useBean> 
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateutil" scope="page"></jsp:useBean>
<%
String sysdate = ""+dateutil.getSysdate();
String eff_dt=request.getParameter("eff_dt")==null?sysdate:request.getParameter("eff_dt");
String count_trials=request.getParameter("count_trials")==null?"0":request.getParameter("count_trials");
String emp_cd=request.getParameter("emp_cd")==null?"":request.getParameter("emp_cd");
//String owner_cd=session.getAttribute("comp_cd")==null?"":session.getAttribute("comp_cd").toString();
String email = request.getParameter("email")==null?"":request.getParameter("email");
String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");
String url = request.getParameter("url")==null?"":request.getParameter("url");
String emp_uid = request.getParameter("emp_uid")==null?"":request.getParameter("emp_uid");
String emp_nm = request.getParameter("emp_nm")==null?"":request.getParameter("emp_nm");

dbadmin.setCallFlag("PASSWORD_POLICY");
dbadmin.setEff_dt(eff_dt);
//dbadmin.setComp_cd(owner_cd);
dbadmin.init();
String otp_timer = dbadmin.getOtp_timer();
%>
<body>
<div class="limiter">
	<div class="container-login100">
		<div class="wrap-login100">
			<form action="" class="login100-form validate-form p-l-25 p-r-25 p-t-90"  method="post" id="logIn"  >
				<span class="login100-form-title" style="border-radius: 50%;width:100px;height:100px;overflow:hidden;top:calc(-100px/2);left: calc(50% - 50px)">
					<span style="top:35%;left:15%; position: absolute;font-size: 30px;font-family: JosefinSans-Bold;line-height: 1.2;text-align: center;padding: 0px;">FMS<sup style="color:maroon;"><b>NG</b></sup></span>
				</span>
				<div class="wrap-input100 validate-input m-b-16" data-validate="Please enter username">
					<input class="input100"  type="text" name="otpval" placeholder="Enter OTP:" maxlength="10">
					<span class="focus-input100"></span>
				</div>
				<div class="container-login100-form-btn">
					<input type="button" class="login100-form-btn" name="btn_signIn" id="btn_signIn" value="Submit" onclick="checkVal('')"> 
				</div>
               	<div align="right">
						<label class="form-label" id="timer" style="color: #33ffff; cursor:default; font-weight: bold; font-style: italic;"><u>The OTP is valid for <span id="display_timer" >00:30</span></u></label>
						<label class="form-label" id="resend_otp" style="color: #33ffff; cursor:pointer; font-weight: bold; font-style: italic;" onclick="resendOTP()"><u>Resend OTP</u></label>
				</div>
               	<!-- <div align="right">
						<label class="form-label" style="color: #33ffff; cursor:pointer; font-weight: bold; font-style: italic;"><u>Resend OTP</u></label>
				</div> -->
				<div class= "p-t-10" id="msg">
               	  	<%if(!msg.equals("")){
						if(msg_type.equals("S")){%>
							<%=utilmsg.successMessage(msg)%>
						<%}else if(msg_type.equals("E")){%>
							<%= utilmsg.errorMessage(msg)%>
						<%}else {%>
						<%= utilmsg.errorMessage(msg)%>
					<%}
					} %>
               	</div> 
               	<br>
               	<input class="input100" type="hidden" name="url" value='<%=url%>'>
               	<input class="input100" type="hidden" name="emp_cd" value='<%=emp_cd%>'>
               	<input class="input100" type="hidden" name="emp_uid" value='<%=emp_uid%>'>
               	<input class="input100" type="hidden" name="emp_nm" value='<%=emp_nm%>'>
<%--                	<input class="input100" type="hidden" name="comp_cd" value='<%=owner_cd%>'> --%>
               	<input class="input100" type="hidden" name="email" value='<%=email%>'>
               	<input class="input100" type="hidden" name="otp_timer" value='<%=otp_timer%>'>
               	<input class="input100" type="hidden" name="count_trials" value='<%=count_trials%>'>
			</form>
		</div>
	</div>
</div>
</body>
<script>
var otp_flag = true;
//localStorage.setItem('','finished');

function setOtpStatus()
{
	otp_flag = false;
}
let countdown;
let interval;
//let max_time = 10;		//Counter max active time in seconds
var time_in_min = document.forms[0].otp_timer.value;
let max_time = parseInt(time_in_min)*60;		//Counter max active time in seconds 
function formatTime(seconds)
{
	const minutes = Math.floor(seconds/60);
	var secs = seconds % 60;
	var time = minutes.toString().padStart(2,'0')+':'+ secs.toString().padStart(2,'0');
	return time;
}

function startTimer(countdown)
{
	document.getElementById('display_timer').textContent = formatTime(countdown);
	document.getElementById("resend_otp").style.display="none";
	interval = setInterval(()=>{
		countdown--;
		localStorage.setItem('remainingTime',countdown);
		document.getElementById('display_timer').textContent = formatTime(countdown);
		localStorage.setItem('timerStatus','running');
		if(countdown<=0)
		{ 
			clearInterval(interval);
			localStorage.setItem('timerStatus','finished');
			document.getElementById("resend_otp").style.display="inline";
			localStorage.removeItem('remainingTime');
			setOtpStatus();
			document.getElementById("timer").style.display="none";
		}
	},1000);
}
window.onload = function ()
{
		const timerStatus = localStorage.getItem("timerStatus");
		countdown = localStorage.getItem('remainingTime')?parseInt(localStorage.getItem('remainingTime')):max_time;
		document.getElementById('display_timer').textContent = formatTime(countdown);
		
		if(timerStatus==='running')
		{
			startTimer(countdown);
		}
		else if(timerStatus==='finished')
		{
			document.getElementById("resend_otp").style.display="inline";
			document.getElementById("timer").style.display="none";
			setOtpStatus();
			
		}
		else
		{
			startTimer(countdown);
		}
};

document.getElementById("resend_otp").addEventListener('click',function(e){
	e.preventDefault();
	if(interval)
	{
		clearInterval(interval);
	}
	localStorage.removeItem('remainingTime');
	localStorage.setItem('timerStatus','running');
	startTimer(countdown);
});


</script>
</html>