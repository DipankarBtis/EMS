<%@page import="java.text.SimpleDateFormat"%>
<%@page import= "java.util.*" %>

<!--
Coded By		  : Harsh Maheta  start->20230228
purpose			  : XLS file for Excel Export functionality
Code Reviewed by  :   
Status	  		  : Developing
-->

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title></title>
<script type="text/javascript">
</script>
</head>

<jsp:useBean class="com.etrm.fms.incident.DataBean_Incident" id="db_incident" ></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="util" ></jsp:useBean>

<%
String sysdate = util.getSysdate();

String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String callflag = request.getParameter("INCIDENT")==null?"":request.getParameter("INCIDENT");
String filter_status = request.getParameter("filter_status")==null?"0":request.getParameter("filter_status");
String filter_root_cause = request.getParameter("filter_root_cause")==null?"0":request.getParameter("filter_root_cause");
String filter_priority = request.getParameter("filter_priority")==null?"0":request.getParameter("filter_priority");
String filter_incident_type = request.getParameter("filter_incident_type")==null?"0":request.getParameter("filter_incident_type");
String filter_initiated_By = request.getParameter("initiated_By")==null?"0":request.getParameter("initiated_By");

HttpServletRequest req = request;

String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

db_incident.setCallFlag("INCIDENT_MST_XLS");
db_incident.setComp_cd(owner_cd);
db_incident.setFilter_initiated_by(filter_initiated_By);
db_incident.setFilter_root_cause(filter_root_cause);
db_incident.setFilter_status(filter_status);
db_incident.setFilter_priority(filter_priority);
db_incident.setFilter_incident_type(filter_incident_type);
db_incident.init();

Vector VINCIDENT_ID = db_incident.getVINCIDENT_ID();
Vector VINCIDENT_TYPE = db_incident.getVINCIDENT_TYPE();
Vector VINCIDENT_TITLE = db_incident.getVINCIDENT_TITLE();
Vector VPRIORITY = db_incident.getVPRIORITY();
Vector VINCIDENT_DTL = db_incident.getVINCIDENT_DTL();
Vector VTARGET_DT = db_incident.getVTARGET_DT();
Vector VASSIGN_TO = db_incident.getVASSIGN_TO();
Vector VASSIGN_TO_NM = db_incident.getVASSIGN_TO_NM();
Vector VLIVE_DT = db_incident.getVLIVE_DT();
Vector VSTATUS = db_incident.getVSTATUS();
Vector VENT_BY = db_incident.getVENT_BY();
Vector VENT_DT = db_incident.getVENT_DT();
Vector VMODIFY_BY = db_incident.getVMODIFY_BY();
Vector VMODIFY_DT = db_incident.getVMODIFY_DT();
Vector VROOT_CAUSE = db_incident.getVROOT_CAUSE();
Vector VITM_APP_FLAG = db_incident.getVITM_APP_FLAG();
Vector VPO_APP_FLAG = db_incident.getVPO_APP_FLAG();
Vector VPROD_APP_DT = db_incident.getVPROD_APP_DT();
Vector VPROD_ROLLOUT_DT = db_incident.getVPROD_ROLLOUT_DT();
Vector VROLLOUT_SCHEDULE = db_incident.getVROLLOUT_SCHEDULE();
%>

<body>

<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition", "inline; filename="+fileName);
%>
<table width="100%" border="1">	
			
	<thead>
		<tr>
			<br><th colspan="19" rowspan="2" align="center">Incident Tracker Report (generated on <%=sysdate%>)</th>		
		</tr>
		<br>
		<tr></tr>
		<br>
		<tr>
			<th>Incident ID</th>
			<th>Priority</th>
			<th>Incident Type</th>
			<th>Incident Title</th>
			<th>Incident Details</th>
			<th>Target Date</th>
			<th>Assign To</th>
			<th>Initial ITM Approval</th>
			<th>Initial PO Approval</th>
			<th>Status</th>
			<th>Resolution</th>
			<!-- <th>Go Live Date</th> -->
			<th>Initiated By </th>
			<th>Initiated on</th>
			<th>Last Modified By</th>
			<th>Last Modified On</th>
			<th>Approval Date</th>
			<th>Go Live Date</th>
			<th>Go Live Schedule</th>
			<th>Age</th>
		</tr>
	</thead>
	
	<tbody>
		<%for(int i=0; i<VINCIDENT_ID.size(); i++) {%>
		<tr>
			<td><%= VINCIDENT_ID.elementAt(i)%></td>
			<td><%= VPRIORITY.elementAt(i)%></td>
			<td><%= VINCIDENT_TYPE.elementAt(i)%></td>
			<td><%= VINCIDENT_TITLE.elementAt(i)%></td>
			<td><%= VINCIDENT_DTL.elementAt(i)%></td>
			<td><%= VTARGET_DT.elementAt(i)%></td>
			<td><%= VASSIGN_TO_NM.elementAt(i)%></td>
			<td align="center" valign="middle">
				<%if(VITM_APP_FLAG.elementAt(i).equals("Y")){ %>
					Yes
				<%}else if(VITM_APP_FLAG.elementAt(i).equals("N")){ %>
					No
				<%}else if(VITM_APP_FLAG.elementAt(i).equals("")){ %>
					
				<%}%>
			</td>
			<td align="center" valign="middle">
				<%if(VPO_APP_FLAG.elementAt(i).equals("Y")){ %>
					Yes
				<%}else if(VPO_APP_FLAG.elementAt(i).equals("N")){ %>
					No
				<%}else if(VPO_APP_FLAG.elementAt(i).equals("")){ %>
					
				<%}%>
			</td>
			<td><%= VSTATUS.elementAt(i)%></td>
			<td align="center">
				<%if(VROOT_CAUSE.elementAt(i).equals("IMP")){%>
					Implemented
				<%}else if(VROOT_CAUSE.elementAt(i).equals("ADNAB")){%>
					As designed/Not A BUG
				<%}else if(VROOT_CAUSE.elementAt(i).equals("WAP")){%>
					Workaround Provided
				<%}else if(VROOT_CAUSE.elementAt(i).equals("SNI")){%>
					System/Network Issue
				<%}%>
			</td>
			<%-- <td><%= VLIVE_DT.elementAt(i)%></td> --%>
			<td><%= VENT_BY.elementAt(i)%></td>
			<td><%= VENT_DT.elementAt(i)%></td>
			<td><%= VMODIFY_BY.elementAt(i)%></td>
			<td><%= VMODIFY_DT.elementAt(i)%></td>
			<td><%= VPROD_APP_DT.elementAt(i)%></td>
			<td><%= VPROD_ROLLOUT_DT.elementAt(i)%></td>
			<td align="center">
			<%if(VROLLOUT_SCHEDULE.elementAt(i).equals("U")){%>
				URGENT
			<% } else if(VROLLOUT_SCHEDULE.elementAt(i).equals("S")){ %>
				SCHEDULED
			<% } %>
			</td>					
			<%
			
			String strdate = ""+VENT_DT.elementAt(i);
			String stpdate = ""+sysdate;
			
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			
			Date d1 = null;
			Date d2 = null;
			
			d1 = format.parse(strdate);
			d2 = format.parse(stpdate);
			
			// In miliseconds
			long diff = d2.getTime() - d1.getTime();
			long diffDays = (diff / (24*60*60*1000));  
			
			if(diffDays<0){
				diffDays = -diffDays;
			}
			
			%>
			<td><%= diffDays%></td>
				
		</tr>
							
		<%}%>
				
	</tbody>	
	</table>
</body>
</html>
<!--  Harsh Maheta  end->20230301 -->