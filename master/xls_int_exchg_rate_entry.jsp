<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import= "java.util.*"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%

int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String rate_mode=request.getParameter("rate_mode")==null?"0":request.getParameter("rate_mode");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String component=request.getParameter("component")==null?"0":request.getParameter("component");
String currency_from=request.getParameter("currency_from")==null?"2":request.getParameter("currency_from");
String currency_to=request.getParameter("currency_to")==null?"1":request.getParameter("currency_to");

if(month.length() == 1)
{
	month="0"+month; 
}
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

dbmaster.setCallFlag("INTEREST_AND_EXCHANGE_RATE_ENTRY");
dbmaster.setRate_mode(rate_mode);
dbmaster.setMonth(month);
dbmaster.setYear(year);
dbmaster.setComponent(component);
dbmaster.setCurrency_from(currency_from);
dbmaster.setCurrency_to(currency_to);
dbmaster.init();

Vector VRATE_CD = dbmaster.getVRATE_CD();
Vector VRATE_NM = dbmaster.getVRATE_NM();

Vector VRATE_VALUE= dbmaster.getVRATE_VALUE();
Vector VTO_CURRENCY= dbmaster.getVTO_CURRENCY();
Vector VFROM_CURRENCY= dbmaster.getVFROM_CURRENCY();
Vector VRATE_REMARK= dbmaster.getVRATE_REMARK();
Vector VRATE_EFF_DT= dbmaster.getVRATE_EFF_DT();
Vector VCOLOR = dbmaster.getVCOLOR();

Vector VCURRENCY_CD= dbmaster.getVCURRENCY_CD();
Vector VCURRENCY_ABR= dbmaster.getVCURRENCY_ABR();

String component_flag = dbmaster.getComponent_flag();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="3" rowspan="2" align="center">Interest/ Exchange Rate Entry (
				<%if(rate_mode.equals("EXCHANGE")){ %>
					Exchange rate
				<%}else{ %>
					Interest rate
				<%} %> : <%=month %>/<%=year%>)</th>
		</tr>
	</table>
	<%if(!component.equals("0") && !component.equals("")){ %>
	<table width="100%" border="1">
		<thead>
		<%if(rate_mode.equals("EXCHANGE")){ %>
			<tr>
				<th>Date</th>
				<th>Exchange Rate</th>
				<th>Remark</th>
			</tr>
		<%}else{ %>
			<tr>
				<th>Date</th>
				<th>Interest Rate (%)</th>
				<th>Remark</th>
			</tr>
		<%} %>
		</thead>
		<tbody>
		<%for(int i=0; i<VRATE_EFF_DT.size(); i++){ %>
			<tr>
				<td align="center">
					<%=VRATE_EFF_DT.elementAt(i)%>
				</td>
				<%if(rate_mode.equals("EXCHANGE")){ %>
					<td align="center">
						<%if(!VRATE_VALUE.elementAt(i).equals("")){ %>
						1 <%=VCURRENCY_ABR.elementAt(VCURRENCY_CD.indexOf(currency_from))%> = <%=VRATE_VALUE.elementAt(i)%> <%=VCURRENCY_ABR.elementAt(VCURRENCY_CD.indexOf(currency_to))%>
						<%} %>
					</td>
				<%}else{ %>
					<td align="center">
						<%if(!VRATE_VALUE.elementAt(i).equals("")){ %>
						<%=VRATE_VALUE.elementAt(i)%>
						<%} %>
					</td>
				<%} %>				
				<td align="center">
					<%=VRATE_REMARK.elementAt(i)%>
				</td>
			</tr>
		<%} %>
		</tbody>
	</table>
	<%} %>
</body>
</html>