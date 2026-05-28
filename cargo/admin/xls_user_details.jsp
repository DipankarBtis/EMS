<%@page import="java.text.SimpleDateFormat"%>
<%@page import= "java.util.*" %>
<!-- Work Started By Arth Patel on 20230517 : Export to Excel for User Details Report -->
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title></title>
<script type="text/javascript">
</script>
</head>

<jsp:useBean class="com.etrm.fms.admin.DB_Admin_Report" id="userDtl" scope="page"/>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="util"/>
<%
String sysdate = util.getSysdate();
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String user_cd=(String)session.getAttribute("user_cd");
String company_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
String from_dt = request.getParameter("from_dt") == null?userDtl.getFrom_dt():request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt") == null?userDtl.getSet_to_dt():request.getParameter("to_dt");
String rd_val = request.getParameter("rd_val") == null?"A":request.getParameter("rd_val");
String filter_status = request.getParameter("filter_status") == null?"a":request.getParameter("filter_status");

HttpServletRequest req = request;
userDtl.setCallFlag("USER DETAILS");
userDtl.setFrom_dt(from_dt);
userDtl.setSet_to_dt(to_dt);
userDtl.setRd_flag(rd_val);
userDtl.setFilter_status(filter_status);
userDtl.init();

Vector VCOMPANY_CD = userDtl.getVCOMPANY_CD();
Vector VCOMPANY_ABBR = userDtl.getVCOMPANY_ABBR();

Vector VEMP_NM = userDtl.getVEMP_NM();
Vector VEMP_CD = userDtl.getVEMP_CD();
Vector VEMP_UID = userDtl.getVEMP_UID();  
Vector VEMAIL_ID = userDtl.getVEMAIL_ID();  
Vector VSTATUS = userDtl.getVSTATUS();
Vector VLOCK_STATUS = userDtl.getVLOCK_STATUS();
Vector VGROUP_NM = userDtl.getVGROUP_NM();
Vector VENT_DT=userDtl.getVENT_DT();
Vector VINDEX = userDtl.getVINDEX();

%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition", "inline; filename="+fileName);
%>
<table width="100%" border="1">
	<thead>
		<tr>
			<br><th colspan="12" rowspan="2" align="center">User Details Report (generated on <%=sysdate %>)</th>		
		</tr>
		<br>
		<tr></tr>
		<br>
		<tr></tr>
		<tr>
			<%if(rd_val.equals("E")){ %>
				<th colspan="12">User Created Between &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;From Date : <%=from_dt %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; To Date : <%=to_dt %></th>
			<%}else { %>
				<%if(filter_status.equals("e")){ %>
					<th colspan="12">All Enabled Users</th>
				<%}else if(filter_status.equals("d")){ %>
					<th colspan="12">All Disabled Users</th>
				<%}else if(filter_status.equals("x")){ %>
					<th colspan="12">All Dormant Users</th>
				<%}else{ %>
					<th colspan="12">All Users</th>
				<%} %>
			<%} %>
		</tr>
		<br>
		<tr>
			<th>SR NO.</th>
			<th colspan="2">USER NAME</th>
			<th>LOGIN ID</th>
			<th>CREATED ON</th>
			<th colspan="3">EMAIL</th>
			<th>STATUS</th>
			<%for(int i=0; i<VCOMPANY_CD.size(); i++){ %>
			<th><%=VCOMPANY_ABBR.elementAt(i)%> Active Access Group (Valid Till)</th>
			<%} %>
			<th>Lock Status</th>
		</tr>
	</thead>
	<tbody>
		<%if(VEMP_CD.size()>0){%>
			<%for(int i=0;i<VEMP_CD.size();i++){ %>
				<tr>
					<td><%=i+1%></td>
					<td colspan="2"><%=VEMP_NM.elementAt(i)%></td>
					<td><%=VEMP_UID.elementAt(i)%></td>
					<td><%=VENT_DT.elementAt(i) %></td>
					<td colspan="3"><%=VEMAIL_ID.elementAt(i)%></td>
					<td><%=VSTATUS.elementAt(i)%></td>
					<%for(int z=0; z<((Vector) VGROUP_NM.elementAt(i)).size(); z++) {%>
					<td><%=((Vector) VGROUP_NM.elementAt(i)).elementAt(z)%></td>
					<%} %>
					<td align="center">
					<%if(VLOCK_STATUS.elementAt(i).equals("Y")){ %>
						Locked
					<%}else{ %>
						Unlocked
					<%} %>
					</td>
				</tr>
			<%} %>
		<%}else{ %>
			<tr>
				<td colspan="12" align="center"><b><font>No User Available!</font></b></td>
			</tr>
		<%} %>
	</tbody>
</table>

</body>
</html>