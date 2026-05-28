<%@page import="com.etrm.fms.util.CommonVariable"%>
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
  	var username=document.forms[0].username.value;
  	var password=document.forms[0].password.value;
  	var val=username.toUpperCase();
  	
  	var msg = "";
	
  	if(username == "" && password =="")
  	{
  		msg = "Please Enter Username and Password!!"
  		flag=false;
  	}
  	else if(username == "")
  	{
  		msg = "Please Enter Username!!"
  		flag=false;
  	}
  	else if(password == "")
  	{
  		msg = "Please Enter Password!!"
  		flag=false;
  	}
  	
  	if(!flag)
  	{
  		alert(msg)
  	}
  	else
  	{
  		document.forms[0].action="index.jsp";
  		document.forms[0].submit();
  	}  
}
function resetPass()
{
	var inputUserID = document.forms[0].inputUserID_nm.value;
	var inputEmail_Id = document.forms[0].inputEmail_Id.value;
	var opration = document.forms[0].opration.value;
	
	if(inputUserID.trim()=="" && inputEmail_Id.trim()=="")
  	{
  		alert("User ID and Email ID can not be Empty!!");
  	}
	else
  	{
		var a = confirm("New Password will be sent to registered E-mail Id associated with this account! \n\nDo you want to continue?");
		if(a)
		{
			document.forms[0].option.value="FORGET_PASSWORD";
			document.forms[0].submit();
		}
  	}
}

function releaseLock()
{
	var inputUserID = document.forms[0].inputUserID_nm.value;
	var inputEmail_Id = document.forms[0].inputEmail_Id.value;
	var opration = document.forms[0].opration.value;
	
	if(inputUserID.trim()=="" && inputEmail_Id.trim()=="")
  	{
  		alert("User ID and Email ID can not be Empty!!");
  	}
	else
  	{
		var a = confirm("Do you want to Unlock your Account?");
		if(a)
		{
			document.forms[0].option.value="RELEASE_LOCK";
			document.forms[0].submit();
		}
  	}
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
<jsp:useBean class="com.etrm.fms.util.DataBean_LoginAlter" id="LoginAlter" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");

LoginAlter.setCallFlag("LOGIN_PAGE");
LoginAlter.init();

Vector VCOMPANY_CD = LoginAlter.getVCOMPANY_CD();
Vector VCOMPANY_NM = LoginAlter.getVCOMPANY_NM();
Vector VCOMPANY_ABBR = LoginAlter.getVCOMPANY_ABBR();
%>
<body>
<div class="limiter">
	<div class="container-login100">
		<div class="wrap-login100">
			<form action="../servlet/Frm_reset_password" class="login100-form validate-form p-l-25 p-r-25 p-t-90"  method="post" id="logIn"  >
				<span class="login100-form-title" style="border-radius: 50%;width:100px;height:100px;overflow:hidden;top:calc(-100px/2);left: calc(50% - 50px)">
					<span style="top:35%;left:15%; position: absolute;font-size: 30px;font-family: JosefinSans-Bold;line-height: 1.2;text-align: center;padding: 0px;"><%=CommonVariable.app_name%></span>
				</span>
				<!-- <img src="../images/Shell_logo.png" height="30px"></img>&nbsp; -->
				<div class="wrap-input100 validate-input m-b-16" data-validate="Please enter username">
					<input class="input100"  type="text" name="username" placeholder="Username" maxlength="20">
					<span class="focus-input100"></span>
				</div>
				<div class="wrap-input100 validate-input m-b-16" data-validate = "Please enter password">
					<input class="input100" type="password" name="password" placeholder="Password" maxlength="20">
					<span class="focus-input100"></span>
				</div>
				<div class="text-right p-t-13 p-b-23"></div>
				<div class="container-login100-form-btn">
					<input type="button" class="login100-form-btn" name="btn_signIn" id="btn_signIn" value="Sign in" onclick="checkVal('')">	
					<!-- class="login100-form-btn" -->		
				</div>
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
			 		<div align="right">
						<label class="form-label" style="color: #33ffff; cursor:pointer; font-weight: bold; font-style: italic;" data-bs-toggle="modal" data-bs-target="#forgetPassModal" ><u>Forgot your Password?</u></label>
					</div>
					
			<!----------Forget Password Modal----------->
				<div class="modal top fade" id="forgetPassModal" tabindex="-1" aria-labelledby="forgetPassModalLabel"
				     aria-hidden="true" data-mdb-backdrop="true" data-mdb-keyboard="true">
				    <div class="modal-dialog modal-dialog-scrollable modal-Lg">
				        <div class="modal-content text-center">
				            <div class="modal-header cdheader">
				            	<div class="topheader">
				                	Forgot Password
				                </div>
				                <input type="button" class="btn-close" data-bs-dismiss="modal">
				            </div>
				            <div id="modal_body" class="modal-body mdbody">
				            	<div id="cdbody" class="cdbody">
					                <div class="row m-b-5">
										<div class="col-sm-12 col-xs-12 col-md-12">
							               	<div class="form-group">
										   		<input type="text" class="form-control" name="inputUserID_nm" id="inputUserID" placeholder="User ID">
										 	</div>
										 </div>
									</div>
									<b>OR</b>
									 <div class="row m-b-5">
										<div class="col-sm-12 col-xs-12 col-md-12">
							               	<div class="form-group">
										   		<input type="text" class="form-control" name="inputEmail_Id" id="inputEmail_Id" placeholder="Email ID">
										 	</div>
										 </div>
									</div>
					        	</div>
				            </div>
				            <div class="modal-footer cdfooter">
								<div class="d-flex justify-content-between">
									<input type="button" class="btn btn-warning com-btn" value="Release Lock" onclick="releaseLock();">
									<input type="button" class="btn btn-warning com-btn" value="Reset Password" onclick="resetPass('');">
								</div>	
				      		</div>
				        </div>
				    </div>
				</div>
				<!----------/Forget Password Modal----------->
				<input type="hidden" name="option" value="FORGET_PASSWORD">
				<input type="hidden" name="opration" value="">
			</form>
		</div>
	</div>
</div>
</body>
</html>