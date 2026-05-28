<%@page import= "java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<!--Harsh Maheta 20230504 : Added Export File Functionality-->
</head>

<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
int currentYear = utildate.getCurrentYear();

String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

dbmaster.setCallFlag("HOLIDAY_MST");
dbmaster.setYear(year);
dbmaster.init();

Vector VHOLIDAY_DT = dbmaster.getVHOLIDAY_DT();
Vector VHOLIDAY_NM = dbmaster.getVHOLIDAY_NM();
Vector VHOLIDAY_DAY = dbmaster.getVHOLIDAY_DAY();
Vector VHOLIDAY_FLAG = dbmaster.getVHOLIDAY_FLAG();
Vector VHOLI_STATE_CD = dbmaster.getVHOLI_STATE_CD();
Vector VHOLI_STATE_NM = dbmaster.getVHOLI_STATE_NM();

Vector VSTATE_CD =dbmaster.getVSTATE_CD();
Vector VSTATE_NM =dbmaster.getVSTATE_NM();

Vector VYear = dbmaster.getVYear();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="5" rowspan="2" align="center">Holiday Master <%=year%></th>
		</tr>
	</table>
				
	<table width="100%" class="table table-bordered table-hover" id="example" border="1">
		<thead>
			<tr>
				<th>Holiday Date</th>
				<th>Holiday Name</th>
				<th>Holiday Day</th>
				<th>State</th>
				<th>Status</th>
			</tr>
		</thead>
		<tbody>
		<%if(VHOLIDAY_DT.size() > 0){ %>
			<%for(int i=0; i<VHOLIDAY_DT.size(); i++){ %>
			<tr id="r<%=i%>">
				<td align="center"><%=VHOLIDAY_DT.elementAt(i) %></td>
				<td align="center"><%=VHOLIDAY_NM.elementAt(i) %></td>
				<td align="center"><%=VHOLIDAY_DAY.elementAt(i) %></td>
				<td align="center"><%=VHOLI_STATE_NM.elementAt(i)%></td>
				<td align="center">
					<div align="center">
						<%if(VHOLIDAY_FLAG.elementAt(i).equals("Y")){%>
						Active
						<%}else{ %>
						Cancelled
						<%} %>
					</div>
				</td>
			</tr>
			<%} %>
		<%}else{ %>
			<tr>
				<td align="center">
					<%=utilmsg.infoMessage("<b>Holiday List is not Available!</b>")%>
				</td>
			</tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>