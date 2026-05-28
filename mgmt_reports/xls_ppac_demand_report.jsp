<%@page import= "java.util.*"%>
<%@ page import="java.text.SimpleDateFormat, java.util.Date, java.util.Locale" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>

<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_Govt_Reports" id="govt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String state_cd=request.getParameter("state_cd")==null?"0":request.getParameter("state_cd");
String force_mark_gj=request.getParameter("force_mark_gj")==null?"Y":request.getParameter("force_mark_gj");

if(month.length() == 1)
{
	month="0"+month; 
}

String owner_cd="";

if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

govt_rpt.setCallFlag("PPAC_DEMAND_RPT");
govt_rpt.setComp_cd(owner_cd);
govt_rpt.setMonth(month);
govt_rpt.setYear(year);
govt_rpt.setState_cd(state_cd);
govt_rpt.setForce_mark_gj(force_mark_gj);
govt_rpt.init();

Vector VCOMPANY_CODE = govt_rpt.getVCOMPANY_CODE(); 
Vector VPRODUCT_CD = govt_rpt.getVPRODUCT_CD();
Vector VTRANSPORT_MODE = govt_rpt.getVTRANSPORT_MODE();
Vector VSTATE_CD = govt_rpt.getVSTATE_CD();
Vector VREVENUE_DIST_CD = govt_rpt.getVREVENUE_DIST_CD();
Vector VCUST_CATEGORY = govt_rpt.getVCUST_CATEGORY();
Vector VEND_USE = govt_rpt.getVEND_USE();
Vector VQTY_MMBTU = govt_rpt.getVQTY_MMBTU();
Vector VQTY_MMSCM = govt_rpt.getVQTY_MMSCM();
Vector VQTY_MT = govt_rpt.getVQTY_MT();
Vector VMONTH_YEAR = govt_rpt.getVMONTH_YEAR();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<%-- <table width="100%" border="1">
		<tr>
			<th colspan="11" rowspan="" align="center">PPAC Demand Report (Generated For: <%=month%>-<%=year%> )</th>
		</tr>
	</table> --%>
<!-- 	<table width="100%">
		<tr>
			<th colspan="9" rowspan="" align="center"></th>
		</tr>
	</table> -->
	<table width="100%" border="1">
		<thead>
			<tr>
				<!-- <th>Sr#</th>-->
				<th style="background-color: #BFBFBF;">Company Code</th> 
				<th style="background-color: #BFBFBF;">PRODUCT_CODE</th>
				<th style="background-color: #BFBFBF;">TRANSPORT_MODE</th>
				<th style="background-color: #BFBFBF;">STATE_CODE</th>
				<th style="background-color: #BFBFBF;">REVENUE_DISTRICT_CODE</th>
				<th style="background-color: #BFBFBF;">CUSTOMER_CATEGORY</th>
				<th style="background-color: #BFBFBF;">END_USE</th>
				<!-- <th>Quantity Supplied<br>(MMBTU)</th> -->
				<th style="background-color: #BFBFBF;">QUANTITY</th>
				<th style="background-color: #BFBFBF;">MMM_YY</th>
			</tr>
		</thead>
		<tbody>
			<%if(VMONTH_YEAR.size()>0){%>
				<%for(int i=0;i<VMONTH_YEAR.size();i++){%>
					<tr>
						<%-- <td align="center"><%=(i+1)%></td>--%>
						<td align="center"><%=VCOMPANY_CODE.elementAt(i)%></td> 
						<td align="center"><%=VPRODUCT_CD.elementAt(i)%></td>
						<td align="center"><%=VTRANSPORT_MODE.elementAt(i)%></td>
						<td align="center"><%=VSTATE_CD.elementAt(i)%></td>
						<td align="center"><%=VREVENUE_DIST_CD.elementAt(i)%></td>
						<td align="center"><%=VCUST_CATEGORY.elementAt(i)%></td>
						<td align="center"><%=VEND_USE.elementAt(i)%></td>
						<%-- <td align="right"><%=VQTY_MMBTU.elementAt(i)%></td> --%>
						<td align="right" style="background-color: #FCFBC9;"><%=VQTY_MT.elementAt(i)%></td>
						<%
							SimpleDateFormat dbFormat = new SimpleDateFormat("MMM-yy", Locale.ENGLISH);
							Date date=dbFormat.parse(""+VMONTH_YEAR.elementAt(i));
						%>
						<td align="center" style="mso-number-format:'mmm-yy'"><%=dbFormat.format(date)%></td>
					</tr>
				<%} %>
			<%}else{%>
				<tr>
					<td colspan="8" align="center"><%=utilmsg.infoMessage("<b>No PPAC Demand Data Found!</b>") %></td>
				</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>