<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">

function change_password()
{
	var url = "../admin/fms_change_password.jsp?u=aEWaBVqZoYMBs";
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>

<%
	String expin = request.getParameter("expin")==null?"":request.getParameter("expin");
%>
<body>
<%@ include file="../home/header.jsp"%>

<%if(!expin.equals("")){%>
	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
	   <div class="modal-dialog modal-dialog-centered" role="document">
		<div class="modal-content">
		  <!-- <div class="modal-header">
			<h5 class="modal-title" id="exampleModalLabel">Password Expiry Alert!</h5>
			<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		  </div> -->
		  <div class="modal-header cdheader">
        		<div class="topheader">
					Password Expiry Alert!
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
		  	<div class="modal-body mdbody">
     			<div class="cdbody">
	     			<div class="row m-b-5">
	     				<div class="d-flex justify-content-center">
							<%=utilmsg.infoMessage("<b>Your Password Expiring in "+expin+" days, Please change your Password!</b>") %>
						</div>
						&nbsp;
						<div class="d-flex justify-content-center">
						`	<button type="button" class="btn btn-warning" onclick="change_password();"><b>Change Password</b></button>
					  	</div>
					</div>
				</div>
			  </div>
			</div>
		  </div>
		</div>
	<script>
	var myModal = new bootstrap.Modal(document.getElementById('myModal'), {})
	myModal.show()
	</script>
<%} %>
</body>
</html>