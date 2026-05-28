<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_val.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.admin.DataBean_Admin" id="dbadmin" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateutil" scope="page"></jsp:useBean>
<%

String firstDtOfMth = ""+dateutil.getFirstDateOfMonth();
String sysdate = ""+dateutil.getSysdate();

String from_dt=request.getParameter("from_dt")==null?firstDtOfMth:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

String company_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");

dbadmin.setCallFlag("AUDIT_TRAIL");
dbadmin.setFrom_dt(from_dt);
dbadmin.setTo_dt(to_dt);
dbadmin.setComp_cd(company_cd);
dbadmin.setEmp_cd(emp_cd);
dbadmin.init();

Vector VREMARK = dbadmin.getVREMARK();
Vector VAUDIT_DT = dbadmin.getVAUDIT_DT();
Vector VAUDIT_TIME = dbadmin.getVAUDIT_TIME();
Vector VNEW_VALUE = dbadmin.getVNEW_VALUE();
Vector VOLD_VALUE = dbadmin.getVOLD_VALUE();
Vector VMACHINE_ID = dbadmin.getVMACHINE_ID();
Vector VEMP_NM = dbadmin.getVEMP_NM();
Vector VMENU_NM = dbadmin.getVMENU_NM();
Vector VMODULE_NM = dbadmin.getVMODULE_NM();

String isSupAdmn = dbadmin.getIsSupAdmn();

String fileName = "EMS_Audit_Log_"+from_dt.replaceAll("/", "")+"_"+to_dt.replaceAll("/", "")+".xls";

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition", "inline; filename="+fileName);
%>
<table border="1">
	<thead>
		<tr>
			<th>Entry Date</th>
			<th>Entry Time</th>
			<th>Machine ID</th>
			<th>User</th>
			<th>Module</th>
			<th>Form Name</th>
			<th>Change/Activity</th>
			<%if(isSupAdmn.equals("Y")){ %>
			<th>New Value</th>
			<th>Old Value</th>
			<%} %>
		</tr>
	</thead>
	<tbody>
	<%for(int i=0; i<VAUDIT_DT.size(); i++){ %>
		<tr>
			<td><div><%=VAUDIT_DT.elementAt(i)%></div></td>
			<td><div><%=VAUDIT_TIME.elementAt(i)%></div></td>
			<td><div><%=VMACHINE_ID.elementAt(i)%></div></td>
			<td><div><%=VEMP_NM.elementAt(i)%></div></td>
			<td><div><%=VMODULE_NM.elementAt(i)%></div></td>
			<td><div><%=VMENU_NM.elementAt(i)%></div></td>
			<td><div><%=VREMARK.elementAt(i)%></div></td>
			<%if(isSupAdmn.equals("Y")){ %>
			<td><div><%=VNEW_VALUE.elementAt(i)%></div></td>
			<td><div><%=VOLD_VALUE.elementAt(i)%></div></td>
			<%} %>
		</tr>
	<%} %>
	</tbody>
</table>

</body>

</html>