<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.text.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--Harsh Maheta 20230615 : Developed Form for Holiday Calendar-->
<head>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_MarketRisk" id="dbmarket" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String spot_curve_type = request.getParameter("spot_curve_type")==null?"0":request.getParameter("spot_curve_type");
String settlement_dt = request.getParameter("settlement_dt")==null?"0":request.getParameter("settlement_dt");
String phy_fin = request.getParameter("phy_fin")==null?"":request.getParameter("phy_fin");
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

dbmarket.setCallFlag("FORWARD_PRICING_MST");
dbmarket.setSpot_curve_type(spot_curve_type);
dbmarket.setSettlement_dt(settlement_dt);
dbmarket.setPhy_fin(phy_fin);
dbmarket.init();

Vector VSPOT_CURVE_TYPE = dbmarket.getVSPOT_CURVE_TYPE();
Vector<String> VSPOT_REPORT_DATE = dbmarket.getVSPOT_REPORT_DATE();
Vector<String> VFORWARD_REPORT_DATE = dbmarket.getVFORWARD_REPORT_DATE();
Vector VCURVE_TYPE = dbmarket.getVCURVE_TYPE();
Vector VTEMP_CURVE_TYPE = dbmarket.getVTEMP_CURVE_TYPE();
Vector VCURVE_NM = dbmarket.getVCURVE_NM();
Vector VINDEX = dbmarket.getVINDEX();
Vector VFORWARD_REPORT_DT = dbmarket.getVFORWARD_REPORT_DT();
Vector VCURVE_DT = dbmarket.getVCURVE_DT();
Vector VCOMMODITY_TYPE = dbmarket.getVCOMMODITY_TYPE();
Vector VCURVE_UNIT = dbmarket.getVCURVE_UNIT();
Vector VPHYS_FIN = dbmarket.getVPHYS_FIN();
Vector VSETTLE_PRICE = dbmarket.getVSETTLE_PRICE();
Vector VENT_BY = dbmarket.getVENT_BY();
Vector VENT_DT = dbmarket.getVENT_DT();
Vector VMAX_CURVE_DT = dbmarket.getVMAX_CURVE_DT();
Vector VPHYS_FIN_MST = dbmarket.getVPHYS_FIN_MST();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
<table  width="100%" border="1">
	<tr>
		<th colspan="11" rowspan="2" align="center">Forward Pricing Report</th>
	</tr>
</table>
<%if(VTEMP_CURVE_TYPE.size()>0){ %>
	<%int j=0,k=0,l=0,m=0;
	for(int i=0; i<VTEMP_CURVE_TYPE.size(); i++)
	{ 
		int index=Integer.parseInt(""+VINDEX.elementAt(i));
		%>						
		<table width="100%" border="1">
			<thead>
				<tr>
					<th>Sr#</th>
					<th>Report Date</th>
					<th>Price curve</th>
					<th>Commodity Type</th>
					<th>Curve Type</th>
					<th>Curve Unit</th>
					<th>Physical/Financial</th>
					<th>Curve MM/YYYY</th>
					<th>Curve Value</th>
					<th>Uploaded Date</th>
					<th>Uploaded By</th>
				</tr>
			</thead>
			<tbody>
				<%k=0; for(l=l; l<VFORWARD_REPORT_DT.size(); l++)
				{
					k+=1;
				%>
					<tr id="r<%=i%>">
						<td align="center"><%=k%></td>
						<td align="center"><%=VFORWARD_REPORT_DT.elementAt(l)%></td>
						<td align="center"><%=VCURVE_NM.elementAt(l)%></td>
						<td align="center"><%=VCOMMODITY_TYPE.elementAt(l) %></td>
						<td align="center"><%=VCURVE_TYPE.elementAt(l)%></td>
						<td align="center"><%=VCURVE_UNIT.elementAt(i)%></td>
						<td align="center"><%=VPHYS_FIN.elementAt(l) %></td>
						<td align="center"><%=VCURVE_DT.elementAt(l) %></td>
						<td align="center"><%=VSETTLE_PRICE.elementAt(l) %></td>
						<td align="center"><%=VENT_DT.elementAt(l) %></td>
						<td align="center"><%=VENT_BY.elementAt(l) %></td>
					</tr>
					<%if(k==index){
						l=l+1;
						break;} %>
				<%} %>
			</tbody>
		</table>
	<%}%>
<%}else{ %>
	&nbsp;
	<table width="100%" border="1"><tr><td colspan="11" align="center"><%=utilmsg.infoMessage("<b>Select Forward pricing Date!</b>")%></td></tr></table>
<%} %>
</body>
</html>