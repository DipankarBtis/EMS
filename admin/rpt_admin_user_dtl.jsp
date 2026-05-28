<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.net.InetAddress" %>

<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh()
{
	var from_dt=document.forms[0].from_dt.value;
	var to_dt=document.forms[0].to_dt.value;
	
	var u = document.forms[0].u.value;
	
	if(trim(from_dt)!="" &&  trim(to_dt)!="")
	{
		var url = "rpt_admin_user_details.jsp?u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}
</script>
</head>

<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"/>
<jsp:useBean class="com.etrm.fms.admin.DataBean_Admin" id="fetch" scope="page"/>

<%
    String user_cd = (String) session.getAttribute("user_cd");

    String sysdate = utildate.getSysdate();
    String firstDate = "01/" + sysdate.substring(3);

    String from_dt = request.getParameter("from_dt") == null ? firstDate : request.getParameter("from_dt");
    String to_dt   = request.getParameter("to_dt") == null ? sysdate : request.getParameter("to_dt");
  //  String company_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
    String company_cd = session.getAttribute("comp_cd") == null ? "": session.getAttribute("comp_cd").toString();

    
    fetch.setCallFlag("ACTIVE_USERS");
    fetch.setFrom_dt(from_dt);
    fetch.setTo_dt(to_dt);
    fetch.setComp_cd(company_cd);
    fetch.setEmp_cd(emp_cd);
    fetch.init();

    Vector VEMP_CD     = fetch.getVEMP_CD();
    Vector VAUDIT_DT   = fetch.getVAUDIT_DT();
    Vector VAUDIT_TIME = fetch.getVAUDIT_TIME();
    Vector VMACHINE_ID = fetch.getVMACHINE_ID();
    Vector VMODULE_NM  = fetch.getVMODULE_NM();
    Vector VMENU_NM  = fetch.getVMENU_NM();
    Vector VREMARK  = fetch.getVREMARK();
    Vector VCOMPANY_ABBR  = fetch.getVCOMPANY_ABBR();
    int rowCount = (VAUDIT_DT != null) ? VAUDIT_DT.size() : 0;
   // String isSupAdmn = dbadmin.getIsSupAdmn();

%>


<%
String systemName = InetAddress.getLocalHost().getHostName();
String loginUser  = System.getProperty("user.name");

request.setAttribute("systemName", systemName);
request.setAttribute("loginUser", loginUser);
%>

<body>
<%@ include file="../home/header.jsp"%>
<form>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader topheader">
				    <div class="d-flex justify-content-between">
						<div class="topheader">User Logged In Details</div>
					   <%--  <a href="../admin/xls_admin_user_dtl.jsp?from_dt=<%=from_dt %>&to_dt=<%=to_dt %>" >
						 	<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</a> --%>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
						<div class="topheader">Active Users Management</div>
								</div>																
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered" id="search_by_filter">
							<thead>
								<tr>
									<th>Login Date</th>
									<th>Login Time</th>
									<th>Machine ID<br><div align="center"><input class="form-control form-control-sm" type="text" id="Machine_ID" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Profile<br><div align="center"><input class="form-control form-control-sm" type="text" id="User" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
									<th>User Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="User" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Laptop Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="User" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
									
									<th>Module Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="Module" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Form Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="Form_Name" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
<!-- 									<th>Remarks<br><div align="center"><input class="form-control form-control-sm" type="text" id="Change_Activity" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div></th>		 -->
									<th>Status<br><div align="center"><input class="form-control form-control-sm" type="text" id="Change_Activity" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div></th>											
								</tr>
							</thead>
							<tbody id="mainTbody">
							<%for(int i=0; i<VAUDIT_DT.size(); i++){ %>
								<tr>
									<td><div><%=VAUDIT_DT.elementAt(i)%></div></td>
									<td><div><%=VAUDIT_TIME.elementAt(i)%></div></td>
									<td><div><%=VMACHINE_ID.elementAt(i)%></div></td>
									<td><div><%=VCOMPANY_ABBR.elementAt(i)%></div></td> 
									<td><div><%=VEMP_CD.elementAt(i)%></div></td>
									<td><div><%=loginUser %></div></td>									
   								    <td><div><%=VMODULE_NM.elementAt(i)%></div></td>
									<td><div><%=VMENU_NM.elementAt(i)%></div></td>
<%-- 								<td><div><%=VREMARK.elementAt(i)%></div></td> --%>
									<td align="center">
<%
    String remark = (String) VREMARK.elementAt(i);

    if (remark != null && remark.equalsIgnoreCase("login")) {
%>
        <span style="color:green; font-weight:bold;">Active</span>
<%
    } else if (remark != null && remark.equalsIgnoreCase("logout")) {
%>
        <span style="color:red; font-weight:bold;">Inactive</span>
<%
    } else {
%>
        <span style="color:gray;"></span>
<%
    }
%>
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
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("search_by_filter");
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
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
</script>
</html>
