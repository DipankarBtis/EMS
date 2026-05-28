<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.text.*"%>
<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--Harsh Maheta 20230701 : Developed Form for Settlement Pricing-->
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_MarketRisk" id="dbmarket" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sysdate=utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
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
String spot_curve_type = request.getParameter("spot_curve_type")==null?"0":request.getParameter("spot_curve_type");
String price_curve_type = request.getParameter("price_curve_type")==null?"0":request.getParameter("price_curve_type");
String settlement_dt = request.getParameter("settlement_dt")==null?"0":request.getParameter("settlement_dt");
String upload_report_dt = request.getParameter("upload_report_dt")==null?sysdate:request.getParameter("upload_report_dt");
String delete_report_Dt = request.getParameter("delete_report_Dt")==null?"":request.getParameter("delete_report_Dt");

HttpServletRequest req = request;
dbmarket.setRequest(req);

dbmarket.setCallFlag("SETTLEMENT_PRICING_MST");
dbmarket.setSpot_curve_type(spot_curve_type);
dbmarket.setPrice_curve_type(price_curve_type);
dbmarket.setSettlement_dt(settlement_dt);
dbmarket.setUpload_report_dt(upload_report_dt);
dbmarket.setDelete_report_Dt(delete_report_Dt);
dbmarket.setFrom_dt(from_dt);
dbmarket.setTo_dt(to_dt);
dbmarket.init();

Vector VSPOT_CURVE_TYPE = dbmarket.getVSPOT_CURVE_TYPE();
Vector<String> VSPOT_REPORT_DATE = dbmarket.getVSPOT_REPORT_DATE();
Vector VPRICE_CURVE_TYPE = dbmarket.getVPRICE_CURVE_TYPE();
Vector VCURVE_TYPE = dbmarket.getVCURVE_TYPE();
Vector VACTUAL_CURVE_TYPE = dbmarket.getVACTUAL_CURVE_TYPE();
Vector VTEMP_CURVE_TYPE = dbmarket.getVTEMP_CURVE_TYPE();
Vector VCURVE_NM = dbmarket.getVCURVE_NM();
Vector VINDEX = dbmarket.getVINDEX();
Vector VSPOT_REPORT_DT = dbmarket.getVSPOT_REPORT_DT();
Vector VCURVE_DT = dbmarket.getVCURVE_DT();
Vector VCOMMODITY_TYPE = dbmarket.getVCOMMODITY_TYPE();
Vector VCURVE_UNIT = dbmarket.getVCURVE_UNIT();
Vector VPHYS_FIN = dbmarket.getVPHYS_FIN();
Vector VSETTLE_PRICE = dbmarket.getVSETTLE_PRICE();
Vector VENT_BY = dbmarket.getVENT_BY();
Vector VENT_DT = dbmarket.getVENT_DT();
Vector VMAX_CURVE_DT = dbmarket.getVMAX_CURVE_DT();
Vector VFILE_NM = dbmarket.getVFILE_NM();
Vector VFILE_SIZE = dbmarket.getVFILE_SIZE();
Vector VFILE_UP_BY = dbmarket.getVFILE_UP_BY();
Vector VFILE_UP_ON = dbmarket.getVFILE_UP_ON();
Vector VSETTLE_PRICE_AVG = dbmarket.getVSETTLE_PRICE_AVG();


%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="11" rowspan="2" align="left">SPOT (Settlement) Pricing Report (<%=from_dt %> - <%=to_dt %>)</th>
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
				<th>Product Type</th>
				<th>Price curve</th>
				<th>Commodity Type</th>
				<th>Curve Type</th>
				<th>Curve Unit</th>
				<th>Financial Curve</th>
				<th>Settle Date</th>
				<th>Curve Value</th>
				<th>Uploaded Date</th>
				<th>Uploaded By</th>
			</tr>
		</thead>
		<tbody>
			<%k=0; for(l=l; l<VSPOT_REPORT_DT.size(); l++)
			{
				k+=1;
			%>
				<tr id="r<%=i%>">
					<td align="center"><%=k%></td>
					<td align="center"><%=VCURVE_NM.elementAt(l)%></td>
					<td align="center"><%=VACTUAL_CURVE_TYPE.elementAt(l)%></td>
					<td align="center"><%=VCOMMODITY_TYPE.elementAt(l) %></td>
					<td align="center"><%=VCURVE_TYPE.elementAt(l)%></td>
					<td align="center"><%=VCURVE_UNIT.elementAt(l)%></td>
					<td align="center"><%=VPHYS_FIN.elementAt(l) %></td>
					<td align="center"><%=VSPOT_REPORT_DT.elementAt(l) %></td>
					<td align="right"><%=VSETTLE_PRICE.elementAt(l) %></td>
					<td align="center"><%=VENT_DT.elementAt(l) %></td>
					<td align="center"><%=VENT_BY.elementAt(l) %></td>
				</tr>
				<%if(k==index){%>
					<tr>
						<td colspan="8" align="right"><b>Average :</b></td>
						<td align="right" style="background-color: #cff4fc; border-color: #b6effb; color: #055160;"><b><%=VSETTLE_PRICE_AVG.elementAt(i) %></b></td>
						<td colspan="2"></td>
					</tr>
					<tr>
						<td colspan="11"></td>
					</tr>
					<%l=l+1;
					break;} %>
			<%} %>
		</tbody>
	</table>
	<%}%>
	<%}else{ %>
		<table width="100%" border="1"><tr><td colspan="11" align="center"><%=utilmsg.infoMessage("<b>Settlement is not Available!</b>")%></td></tr></table>
	<%} %>
</body>
</html>