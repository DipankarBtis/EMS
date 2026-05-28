<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><%=CommonVariable.app_name%></title>

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
      document.forms[0].action="login_index.jsp";
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
      document.forms[0].reset_pswd_flag.value = true;
//      document.forms[0].option.value="FORGET_PASSWORD";
      document.forms[0].action="login_index.jsp";
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
      document.forms[0].rel_lock_flag.value = true;
      //document.forms[0].option.value="RELEASE_LOCK";
      document.forms[0].action="login_index.jsp";
      document.forms[0].submit();
    }
    }
}

// forgetpass model
function forgetPass()
{
  //hiding the signin form 
  document.getElementById("user_name").style.display="none";
  document.getElementById("pswd").style.display="none";
  document.getElementById("temp").style.display="none";
  document.getElementById("signIn_btn").style.display="none";
  document.getElementById("forget_pass_lbl").style.display="none";
  document.getElementById("msg").style.display="none";
  
  //Showing the forgot password form 
  document.getElementById("forget_pass").style.display="block";
  document.getElementById("re_btn").style.display="block";
  document.getElementById("rel_lock").style.display="block";
  document.getElementById("reset_pswd").style.display="block";
}
// back model
function back()
{
  //showing the signin form 
  document.getElementById("user_name").style.display="block";
  document.getElementById("pswd").style.display="block";
  document.getElementById("temp").style.display="block";
  document.getElementById("signIn_btn").style.display="block";
  document.getElementById("forget_pass_lbl").style.display="block";
  document.getElementById("msg").style.display="block";
  document.getElementById("rel_lock").style.display="block";
  document.getElementById("reset_pswd").style.display="block";
  
  //Showing the forgot password form 
  document.getElementById("forget_pass").style.display="none";
  document.getElementById("re_btn").style.display="none";
  document.getElementById("rel_lock").style.display="none";
  document.getElementById("reset_pswd").style.display="none";
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
<jsp:useBean class="com.etrm.fms.admin.DataBean_Admin" id="dbadmin" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateutil" scope="page"></jsp:useBean>
<%
String sysdate = ""+dateutil.getSysdate();
String eff_dt=request.getParameter("eff_dt")==null?sysdate:request.getParameter("eff_dt");
String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");
String otp_flag = request.getParameter("otp_flag")==null?"false":request.getParameter("otp_flag");
String emp_uid = request.getParameter("emp_uid")==null?"":request.getParameter("emp_uid");    
String count_trials = request.getParameter("count_trials")==null?"0":request.getParameter("count_trials");
String option = request.getParameter("option")==null?"":request.getParameter("option");
boolean rel_lock_flag = Boolean.parseBoolean(request.getParameter("rel_lock_flag")==null?"false":request.getParameter("rel_lock_flag"));
boolean reset_pswd_flag = Boolean.parseBoolean(request.getParameter("reset_pswd_flag")==null?"false":request.getParameter("reset_pswd_flag"));
boolean frm_submit_flag = Boolean.parseBoolean(request.getParameter("frm_submit_flag")==null?"false":request.getParameter("frm_submit_flag"));

LoginAlter.setCallFlag("LOGIN_PAGE");
LoginAlter.init();

dbadmin.setCallFlag("PASSWORD_POLICY");
dbadmin.setEff_dt(eff_dt);
dbadmin.init();
String otp_timer = dbadmin.getOtp_timer();


boolean otp_status=Boolean.parseBoolean(otp_flag); 

Vector VCOMPANY_CD = LoginAlter.getVCOMPANY_CD();
Vector VCOMPANY_NM = LoginAlter.getVCOMPANY_NM();
Vector VCOMPANY_ABBR = LoginAlter.getVCOMPANY_ABBR();
%>
<%if(otp_status){ %>
<script type="text/javascript">
function checkOTPVal(emp_uid) 
{
  var flag=true;
  var otpval = document.forms[0].otpval.value;  
    var msg = "";
  var msg_div=document.getElementById("msg");
    document.forms[0].emp_uid.value=emp_uid;
    document.forms[0].resend_flag.value=false;
    document.forms[0].valid_otp_time.value=valid_otp_time;
  if(otpval != "")
    {
      document.forms[0].action = "iindex.jsp";
    document.forms[0].submit();
    }
    else
    {
      msg = "OTP is Not Entered"
      msg_div.innerHTML = "<div class='container'><div class='alert alert-danger'><i class='fa fa-exclamation-triangle fa-lg'></i>&nbsp;"+msg+"</div></div>";
    }
}
function resendOTP(emp_uid,count_trials)
{
  document.forms[0].emp_uid.value=emp_uid;
  document.forms[0].count_trials.value=count_trials;
  document.forms[0].resend_flag.value=true;
  document.forms[0].valid_otp_time.value=valid_otp_time;
  document.forms[0].action = "iindex.jsp";
  document.forms[0].submit();
}
</script>
<%} %>

<body>
<div class="limiter">
  <div class="container-login100">
    <div class="wrap-login100">
      <form  autocomplete="off" class="login100-form validate-form p-l-25 p-r-25 p-t-90"  method="post" id="logIn"  >
<!--      <form action="../servlet/Frm_reset_password" class="login100-form validate-form p-l-25 p-r-25 p-t-90"  method="post" id="logIn"  > -->
        <span class="login100-form-title" style="border-radius: 50%;width:100px;height:100px;overflow:hidden;top:calc(-100px/2);left: calc(50% - 50px)">
          <span style="top:35%;left:15%; position: absolute;font-size: 30px;font-family: JosefinSans-Bold;line-height: 1.2;text-align: center;padding: 0px;"><%=CommonVariable.app_name%></span>
        </span>
        <!-- <img src="../images/Shell_logo.png" height="30px"></img>&nbsp; -->
        <%if(!otp_status){%>
        <div class="wrap-input100 validate-input m-b-16" data-validate="Please enter username" id="user_name">
          <input autocomplete="off" class="input100"  type="text" name="username" placeholder="Username" maxlength="20">
          <span class="focus-input100"></span>
        </div>
        <div class="wrap-input100 validate-input m-b-16" data-validate = "Please enter password" id="pswd">
          <input autocomplete="off" class="input100" type="password" name="password" placeholder="Password" maxlength="20">
          <span class="focus-input100"></span>
        </div>
        <div class="text-right p-t-13 p-b-23" id="temp"></div>
        <div class="container-login100-form-btn" id="signIn_btn">
          <input type="button" class="login100-form-btn" name="btn_signIn" id="btn_ 
          signIn" value="Sign in" onclick="checkVal('')"> 
          <!-- class="login100-form-btn" -->    
        </div>
        
        <!-- forget password -->
        <div class="container mx-auto" style="display:none;" id="forget_pass">
          <div class="d-flex justify-content-center h3"  style="color: #33ffff;font-weight:bold;" id="forgot_pass_head">
                            Forgot Password
          </div>
          <br>
          <div class="wrap-input100 validate-input m-b-16" data-validate="Please enter userid"  id="re_user_id">
            <input autocomplete="off" type="text" class="input100" name="inputUserID_nm" id="inputUserID" placeholder="User ID">
          </div>
          <div class="container" id="or">
            <div class="d-flex justify-content-center h5" style="color:#33ffff;font-weight:bold;">
              <div class="text-right p-t-5 p-b-2"></div><!-- <div class="text-right p-t-10 p-b-23" ></div> -->
              OR 
              <div class="text-right p-t-10 p-b-12"></div><!-- <div class="text-right p-t-10 p-b-23" ></div> -->
            </div>
          </div>
          <div class="wrap-input100 validate-input m-b-16" data-validate = "Please enter password"  id="re_email">
            <input autocomplete="off" type="text" class="input100" name="inputEmail_Id" id="forgot_frm_inputEmail_Id" placeholder="Email ID">
          </div>
<!--          <div class="text-right p-t-13 p-b-23" ></div> -->
          <div class="container-login100-form-btn" id="re_btn">
            <div class=" d-flex justify-content-between">
              <input type="button" class="btn btn-warning com-btn" value="Release Lock" onclick="releaseLock();" id="rel_lock">
              <input type="button" class="btn btn-warning com-btn" value="Reset Password" onclick="resetPass('');" id="reset_pswd">
            </div>
          </div>
          <div class="text-right p-t-3 p-b-5" ></div>
          <div align="right">
              <label class="form-label" style="color: #33ffff; cursor:pointer; font-weight: bold; font-style: italic;" id="back_lbl" onclick="back()"><u>Back to Sign-In?</u></label>
          </div>
        </div>
        <!-- /forget password -->
        
        
        <%}else{%>
          <div class="wrap-input100 validate-input m-b-16" data-validate="Please Enter OTP">
            <input autocomplete="off" class="input100"  type="text" name="otpval" placeholder="Enter OTP:" maxlength="10"  onkeydown="return event.key != 'Enter';">
            <span class="focus-input100"></span>
          </div>
          <div class="container-login100-form-btn">
            <input type="button" class="login100-form-btn" name="btn_otp" id="btn_otp" value="Submit" onclick="checkOTPVal('<%=emp_uid%>')"> 
          </div>
                  <div align="right">
              <label class="form-label" id="timer" style="color: #33ffff; cursor:default; font-weight: bold; font-style: italic;"><u>The OTP is valid for <span id="display_timer" >00:30</span></u></label>
              <label class="form-label" id="resend_otp" style="color: #33ffff; cursor:pointer; font-weight: bold; font-style: italic;" onclick="resendOTP('<%=emp_uid%>','<%=count_trials%>')"><u>Resend OTP</u></label>
          </div>
        
        <%}%>
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
                <%if(!otp_status){%>
          <div align="right">
<!--            <label class="form-label" style="color: #33ffff; cursor:pointer; font-weight: bold; font-style: italic;" data-bs-toggle="modal" data-bs-target="#forgetPassModal" ><u>Forgot your Password?</u></label> -->
            <label class="form-label" style="color: #33ffff; cursor:pointer; font-weight: bold; font-style: italic;" id="forget_pass_lbl" onclick="forgetPass()"><u>Forgot your Password?</u></label>
          </div>
        <%} %>
          <br>
      <!----------Forget Password Modal----------->
<!--        <div class="modal top fade" id="forgetPassModal" tabindex="-1" aria-labelledby="forgetPassModalLabel" -->
<!--             aria-hidden="true" data-mdb-backdrop="true" data-mdb-keyboard="true"> -->
<!--            <div class="modal-dialog modal-dialog-scrollable modal-Lg"> -->
<!--                <div class="modal-content text-center"> -->
<!--                    <div class="modal-header cdheader"> -->
<!--                      <div class="topheader"> -->
<!--                          Forgot Password -->
<!--                        </div> -->
<!--                        <input type="button" class="btn-close" data-bs-dismiss="modal"> -->
<!--                    </div> -->
<!--                    <div id="modal_body" class="modal-body mdbody"> -->
<!--                      <div id="cdbody" class="cdbody"> -->
<!--                          <div class="row m-b-5"> -->
<!--                    <div class="col-sm-12 col-xs-12 col-md-12"> -->
<!--                              <div class="form-group"> -->
<!--                          <input type="text" class="form-control" name="inputUserID_nm" id="inputUserID" placeholder="User ID"> -->
<!--                      </div> -->
<!--                     </div> -->
<!--                  </div> -->
<!--                  <b>OR</b> -->
<!--                   <div class="row m-b-5"> -->
<!--                    <div class="col-sm-12 col-xs-12 col-md-12"> -->
<!--                              <div class="form-group"> -->
<!--                          <input type="text" class="form-control" name="inputEmail_Id" id="inputEmail_Id" placeholder="Email ID"> -->
<!--                      </div> -->
<!--                     </div> -->
<!--                  </div> -->
<!--                    </div> -->
<!--                    </div> -->
<!--                    <div class="modal-footer cdfooter"> -->
<!--                <div class="d-flex justify-content-between"> -->
<!--                  <input type="button" class="btn btn-warning com-btn" value="Release Lock" onclick="releaseLock();"> -->
<!--                  <input type="button" class="btn btn-warning com-btn" value="Reset Password" onclick="resetPass('');"> -->
<!--                </div>   -->
<!--                  </div> -->
<!--                </div> -->
<!--            </div> -->
<!--        </div> -->
        <!----------/Forget Password Modal----------->
        <input type="hidden" name="opration" value="">
        <input type="hidden" name="rel_lock_flag" value="<%=rel_lock_flag%>">
        <input type="hidden" name="reset_pswd_flag" value="<%=reset_pswd_flag%>">
        <%if(otp_status){%>
        <input type="hidden" name="option" value="<%=option%>">
        <input type="hidden" name="emp_uid" value="<%=emp_uid%>">
        <input class="input100" type="hidden" name="otp_timer" value='<%=otp_timer%>'>
        <input class="input100" type="hidden" name="valid_otp_time" value="">
        <input class="input100" type="hidden" name="resend_flag" value="">
        <input class="input100" type="hidden" name="count_trials" value="<%=count_trials %>">
        <input type="hidden" name="frm_submit_flag" value="<%=frm_submit_flag%>">
        <%} %>
      </form>
    </div>
  </div>
</div>
<%if(!otp_status){%>
  <script>
          localStorage.removeItem('remainingTime'); //Added on 20241025: for reseting OTP timer
        localStorage.removeItem('timerStatus');   //Added on 20241025: for reseting OTP timer
  </script>
<%}else{%>
  <script>
  //for submitting the forms 
  var frm_submit_flag = document.forms[0].frm_submit_flag.value;
  var option = document.forms[0].option.value;
  if(frm_submit_flag == "true")
  {
    document.getElementById("btn_otp").disabled = true;
    document.getElementById("resend_otp").style.display="none";
    document.getElementById("timer").style.display="none";
    document.forms[0].action = "../servlet/Frm_reset_password";
    document.forms[0].submit();
  }
  //timer for otp
    var valid_otp_time = true;
  
    function setOtpStatus()
    {
      valid_otp_time = false;
    }
    //let max_time = 120;   //Counter max active time in seconds
    var time_in_min = document.forms[0].otp_timer.value;
    let max_time = parseInt(time_in_min)*60;    //Counter max active time in seconds 
    let countdown;
    let interval;
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
      console.log("addEventList");
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
<%}%>
</body>
</html>