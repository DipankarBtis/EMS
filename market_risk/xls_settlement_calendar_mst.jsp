<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_MarketRisk" id="dbmarket" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%

String settle_curve = request.getParameter("settle_curve")==null?"0":request.getParameter("settle_curve");
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

dbmarket.setCallFlag("SETTLEMENT_MST");
dbmarket.setSettle_curve(settle_curve);
dbmarket.setYear(year);
dbmarket.init();

Vector VCONT_MONTH = dbmarket.getVCONT_MONTH(); 
Vector VCURVE_NM = dbmarket.getVCURVE_NM(); 
Vector VSETTLE_START_DT = dbmarket.getVSETTLE_START_DT();
Vector VSETTLE_END_DT = dbmarket.getVSETTLE_END_DT();
Vector VSETTLE_DT = dbmarket.getVSETTLE_DT();
Vector VSETTLE_CURVE = dbmarket.getVSETTLE_CURVE();
Vector VINDEX = dbmarket.getVINDEX();
Vector VSUB_INDEX = dbmarket.getVSUB_INDEX();
Vector VSETTLE_TYPE = dbmarket.getVSETTLE_TYPE();
Vector VYEAR = dbmarket.getVYEAR();
Vector VTEMP_YEAR = dbmarket.getVTEMP_YEAR();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="5" rowspan="2" align="center">Settlement Calendar</th>
		</tr>
	</table>
				
	<table border="1" class="table table-bordered table-hover" id="example">
		<thead>
			<tr>
				<th>Contract Month</th>
				<th>Settle Start Date</th>
				<th>Settle End Date</th>
				<th>Settle Date</th>
				<th>Settle Curve</th>
			</tr>
		</thead>
		<tbody>
		<%if(VCONT_MONTH.size() > 0){ %>
			<%for(int i=0; i<VCONT_MONTH.size(); i++){ %>
			<tr id="r<%=i%>">
				<td align="center"><%=VCONT_MONTH.elementAt(i) %></td>
				<td align="center"><%=VSETTLE_START_DT.elementAt(i) %></td>
				<td align="center"><%=VSETTLE_END_DT.elementAt(i) %></td>
				<td align="center"><%=VSETTLE_DT.elementAt(i)%></td>
				<td align="center"><%=VSETTLE_CURVE.elementAt(i)%></td>
			</tr>
			<%} %>
		<%}else{ %>
			<tr>
				<td colspan="5" align="center">
					<%=utilmsg.infoMessage("<b>Settlement Calendar is not Available!</b>")%>
				</td>
			</tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>