<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_MarketRisk" id="dbmarket" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%

String holiday_type = request.getParameter("holiday_type")==null?"0":request.getParameter("holiday_type");
String year=request.getParameter("year")==null?"0":request.getParameter("year");
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
dbmarket.setCallFlag("HOLIDAY_CLAND_MST");
dbmarket.setHoliday_type(holiday_type);
dbmarket.setYear(year);
dbmarket.init();

Vector VSETTLE_TYPE = dbmarket.getVSETTLE_TYPE();
Vector VHOLIDAY_NM = dbmarket.getVHOLIDAY_NM();
Vector VHOLIDAY_DT = dbmarket.getVHOLIDAY_DT();
Vector VHOLIDAY_STATUS = dbmarket.getVHOLIDAY_STATUS();
Vector VCURVE_NM = dbmarket.getVCURVE_NM();
Vector VINDEX = dbmarket.getVINDEX();
Vector VYEAR = dbmarket.getVYEAR();
Vector VTEMP_YEAR = dbmarket.getVTEMP_YEAR();
Vector VSETTLE_CURVE = dbmarket.getVSETTLE_CURVE();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="4" rowspan="2" align="center">Holiday Calendar</th>
		</tr>
	</table>
	<table border="1">
		<thead>
			<tr>
				<!-- <th></th> -->
				<th>Holiday Name</th>
				<th>Holiday Date</th>
				<th>Curve Name</th>
				<th>Holiday Status</th>
			</tr>
		</thead>
		<tbody>
		<%if(VHOLIDAY_DT.size()>0){%>
			<%for(int l=0; l<VHOLIDAY_DT.size(); l++){%>
			<tr>
				<td align="center"><%=VHOLIDAY_NM.elementAt(l) %></td>
				<td align="center"><%=VHOLIDAY_DT.elementAt(l) %></td>
				<td align="center"><%=VSETTLE_CURVE.elementAt(l) %></td>
				<td align="center"><%if(VHOLIDAY_STATUS.elementAt(l).equals("Y")){ %>Active<%}else{ %>Cancelled<%} %></td>
			</tr>
			<%} %>
		<%}else{ %><tr><td colspan="4" align="center">Holiday Calendar is not Available!</td></tr><%} %>
		</tbody>
	</table>
</body>
</html>