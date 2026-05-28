<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
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
dbcounterpty.setCallFlag("COUNTERPARTY_MATRIX");
dbcounterpty.setComp_cd(owner_cd);
dbcounterpty.init();

Vector VCOUNTERPARTY_CD = dbcounterpty.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcounterpty.getVCOUNTERPARTY_ABBR();
Vector VENTITY_ROLE = dbcounterpty.getVENTITY_ROLE();
Vector VREQUESTER = dbcounterpty.getVREQUESTER();
Vector VAPPROVER = dbcounterpty.getVAPPROVER();
Vector VCOLOR = dbcounterpty.getVCOLOR();
Vector VCLEARANCE = dbcounterpty.getVCLEARANCE();
Vector VCATEGORY = dbcounterpty.getVCATEGORY();
Vector VNCF_CATEGORY = dbcounterpty.getVNCF_CATEGORY();
Vector VSTATUS = dbcounterpty.getVSTATUS();
Vector VEFF_DT = dbcounterpty.getVEFF_DT();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>


<table  width="100%" border="1">
	<tr>
		<th colspan="8" rowspan="2" align="left">Counterparty Matrix Report</th>
	</tr>
</table>

<table width="100%" border="1">
	<thead> 
		<tr>
			<th>Counterparty</th>
			<th>ABBR</th>
			<th>Eff. Date</th>
			<th>Status</th>
			<th>Category</th>
			<th>NCF Clearance</th>
			<th>Clearance</th>
			<th>Entity Role</th>
			<th>Entity Role Requested By</th>
			<th>Entity Role Approved By</th>
		</tr>
	</thead>
	<tbody>
	<%if(VCOUNTERPARTY_CD.size() > 0){ %>
		<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){%>
			<tr>
				<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
				<td><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
				<td><%=VEFF_DT.elementAt(i)%></td>
				<td align="center">
					<div align="center">
						<%if(VSTATUS.elementAt(i).equals("Y")){%>
						Active
						<%}else if(VSTATUS.elementAt(i).equals("N")){ %>
						Deactivated
						<%}else{ %>
						E-Rate
						<%} %>
					</div>
				</td>
				<td align="center"><%=VCATEGORY.elementAt(i)%></td>
				<td align="center"><%=VNCF_CATEGORY.elementAt(i)%></td>
				<td align="center"><%=VCLEARANCE.elementAt(i)%></td>
				<td align="center"><%=VENTITY_ROLE.elementAt(i)%></td>							
				<td><%=VREQUESTER.elementAt(i)%></td>
				<td><%=VAPPROVER.elementAt(i)%></td>
			</tr>
		<%} %>
	<%}else{ %>
		<tr>
			<td align="center" colspan="8"><%=utilmsg.infoMessage("<b>No Counterparty Configured!</b>") %></td>
		</tr>
	<%} %>	
	</tbody>
</table>

</body>
</html>