<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head></head>

<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_oth_inv_sun_interface" id="sun_master" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sysdt = utildate.getSysdate();
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


sun_master.setCallFlag("SUN_ENTITY_ACC_CD");
sun_master.setComp_cd(owner_cd);
sun_master.init();

Vector VVENDOR_CD = sun_master.getVVENDOR_CD();
Vector VVENDOR_NM = sun_master.getVVENDOR_NM();
Vector VVENDOR_ABBR = sun_master.getVVENDOR_ABBR();
Vector VSUN_ACCOUNT = sun_master.getVSUN_ACCOUNT();
%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
<table width="100%" border="1">
	<tr>
		<th colspan="4" rowspan="2" align="center">Other Entity Sun Account Code [Vendor]</th>
	</tr>
</table>
<br>
<table width="100%" border="1">
	<thead id="tbsearch">
		<tr>
			<th>Sr#</th>
			<th>Vendor ABBR</th>
			<th>Vendor Name</th>
			<th>SUN Account Code</th>
		</tr>
	</thead>
	<tbody>
		<%if(VVENDOR_CD.size()!=0){%>
			<%for(int i=0; i<VVENDOR_CD.size();i++){%>
				<tr>
					<td align="center">
						<%=i+1 %>
					</td>
					<td align="center">
						<%=VVENDOR_ABBR.elementAt(i)%>
					</td> 
					<td><%=VVENDOR_NM.elementAt(i)%></td>
					<td>
						<%=VSUN_ACCOUNT.elementAt(i)%>
					</td>
				</tr>
			<%} %>
		<%}else{%>
			<tr>
				<td colspan="4" align="center"><%=utilmsg.infoMessage("<b>No Vendor found!</b>") %></td>
			</tr>
		<%} %>
	</tbody>
</table>
</body>
</html>