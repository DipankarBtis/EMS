<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.admin.DB_Admin_Report" id="dbadminRpt" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateutil" scope="page"></jsp:useBean>
<%

String firstDtOfMth = ""+dateutil.getFirstDateOfMonth();
String sysdate = ""+dateutil.getSysdate();

String from_dt=request.getParameter("from_dt")==null?firstDtOfMth:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

dbadminRpt.setCallFlag("SYSTEM_ERROR");
dbadminRpt.setFrom_dt(from_dt);
dbadminRpt.setTo_dt(to_dt);
dbadminRpt.init();

Vector VERROR_CD = dbadminRpt.getVERROR_CD();
Vector VERROR_ENT_DT = dbadminRpt.getVERROR_ENT_DT();
Vector VERROR_SRC_FILE = dbadminRpt.getVERROR_SRC_FILE();
Vector VERROR_FUNC_NM = dbadminRpt.getVERROR_FUNC_NM();
Vector VERROR_MSG = dbadminRpt.getVERROR_MSG();
Vector VERROR_STACK_TRACE = dbadminRpt.getVERROR_STACK_TRACE();

String fileName = "FMSng_System_Error_Log_"+from_dt.replaceAll("/", "")+"_"+to_dt.replaceAll("/", "")+".xls";

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition", "inline; filename="+fileName);
%>
<table border="1">
	<thead>
		<tr>
			<th>Error#</th>
			<th>Entry On</th>
			<th>Source File</th>
			<th>Function</th>
			<th>Error Msg</th>
			<th>Stack Trace</th>									
		</tr>
	</thead>
	<tbody id="mainTbody">
	<%if (VERROR_CD.size()>0) { %>
		<%for(int i=0; i<VERROR_CD.size(); i++){ %>
			<tr>
				<td><div align="center"><%=VERROR_CD.elementAt(i)%></div></td>
				<td><div align="center">&nbsp;<%=VERROR_ENT_DT.elementAt(i)%></div></td>
				<td><div ><%=VERROR_SRC_FILE.elementAt(i)%></div></td>
				<td><div><%=VERROR_FUNC_NM.elementAt(i)%></div></td>
				<td><div><%=VERROR_MSG.elementAt(i)%></div></td>
				<td><div><%=VERROR_STACK_TRACE.elementAt(i).toString().replaceAll("<br>", "\n")%></div></td>
			</tr>
		<%} %>
	<%}else{ %>
		<tr>
			<td colspan="6"><div align="center">
				<%=utilmsg.infoMessage("<b>No System Error Captured!</b>")%></div>
			</td>
		</tr>
	<%} %>
	</tbody>
</table>
</body>

</html>