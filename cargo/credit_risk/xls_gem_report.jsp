<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CR_ReceivableReport" id="dbcredit" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String previousDate=utildate.getPreviousDate();
String rpt_dt = request.getParameter("rpt_dt")==null?previousDate:request.getParameter("rpt_dt");
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

dbcredit.setCallFlag("GEM_REPORT");
dbcredit.setComp_cd(owner_cd);
//dbcredit.setRpt_dt(rpt_dt);
dbcredit.init();

Vector VINDUSTRY = dbcredit.getVINDUSTRY();
Vector VMARKET_TYPE = dbcredit.getVMARKET_TYPE();
Vector VCURRENCY = dbcredit.getVCURRENCY();
Vector VNET_EXPOSURE = dbcredit.getVNET_EXPOSURE();
Vector VOTHER_COLLATERAL = dbcredit.getVOTHER_COLLATERAL();
Vector VLC_AMOUNT = dbcredit.getVLC_AMOUNT();
Vector VCASH_COLLATERAL = dbcredit.getVCASH_COLLATERAL();
Vector VGROSS_EXPOSURE = dbcredit.getVGROSS_EXPOSURE();
Vector VFORWARD_MTM = dbcredit.getVFORWARD_MTM();
Vector VCURRENT_MONTH_UNDELIVERED = dbcredit.getVCURRENT_MONTH_UNDELIVERED();
Vector VUNBILLED_PAYABLE = dbcredit.getVUNBILLED_PAYABLE();
Vector VUNBILLED_RECEIVABLE = dbcredit.getVUNBILLED_RECEIVABLE();
Vector VACCOUNT_PAYABLE = dbcredit.getVACCOUNT_PAYABLE();
Vector VACCOUNT_RECEIVABLE = dbcredit.getVACCOUNT_RECEIVABLE();
Vector VLIMIT_VALUE = dbcredit.getVLIMIT_VALUE();
Vector VLIMIT_CATEGORY = dbcredit.getVLIMIT_CATEGORY();
Vector VFINAL_RATING = dbcredit.getVFINAL_RATING();
Vector VINTERNAL_RATING = dbcredit.getVINTERNAL_RATING();
Vector VMOODY_RATING = dbcredit.getVMOODY_RATING();
Vector VS_P_RATING = dbcredit.getVS_P_RATING();
Vector VULTIMATE_LEGAL_PARENT = dbcredit.getVULTIMATE_LEGAL_PARENT();
Vector VLEGAL_PARENT = dbcredit.getVLEGAL_PARENT();
Vector VCOUNTERPARTY_NAME = dbcredit.getVCOUNTERPARTY_NAME();
Vector VTRADING_ENTITY = dbcredit.getVTRADING_ENTITY();
Vector VCOUNTERPARTY_CD = dbcredit.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dbcredit.getVCOUNTERPARTY_ABBR();
Vector VDEAL_TYPE = dbcredit.getVDEAL_TYPE();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="26" rowspan="2" align="left">Global Exposure Management Report <%=rpt_dt %></th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th align="center">Sr#</th>
				<th align="center">TRADING ENTITY</th>
				<th align="center">COUNTERPARTY LONG NAME</th>
				<th align="center">DEAL TYPE</th>
				<th align="center">LEGAL PARENT NAME</th>
				<th align="center">ULTIMATE LEGAL PARENT NAME</th>
				<th align="center">S&P RATING</th>
				<th align="center">MOODY RATING</th>
				<th align="center">INTERNAL RATING</th>
				<th align="center">FINAL RATING</th>
				<th align="center">LIMIT CATEGORY</th>
				<th align="center">CREDIT LIMIT</th>
				<th align="center">A/R</th>
				<th align="center">A/P</th>
				<th align="center">UNBILLED RECEIVABLE</th>
				<th align="center">UNBILLED PAYABLE</th>
				<th align="center">CURRENT MONTH UNDELIVERED</th>
				<th align="center">FORWARD MTM</th>
				<th align="center">GROSS EXPOSURE</th>
				<th align="center">CASH COLLATERAL</th>
				<th align="center">LC AMOUNT</th>
				<th align="center">OTHER COLLATERAL</th>
				<th align="center">NET EXPOSURE</th>
				<th align="center">CURRENCY</th>
				<th align="center">MARKET TYPE </th>
				<th align="center">INDUSTRY</th>
			</tr>
		</thead>
		<tbody>
			<%if(VCOUNTERPARTY_CD.size()>0){ %>
				<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
					<tr>
						<td align="center"><%=i+1 %>.</td>
						<td align="center"><%=VTRADING_ENTITY.elementAt(i)%></td>
						<td align="center"><%=VCOUNTERPARTY_NAME.elementAt(i)%></td>
						<td align="center"><%=VDEAL_TYPE.elementAt(i)%></td>
						<td align="center"><%=VLEGAL_PARENT.elementAt(i)%></td>
						<td align="center"><%=VULTIMATE_LEGAL_PARENT.elementAt(i)%></td>
						<td align="center"><%=VS_P_RATING.elementAt(i)%></td>
						<td align="center"><%=VMOODY_RATING.elementAt(i)%></td>
						<td align="center"><%=VINTERNAL_RATING.elementAt(i)%></td>
						<td align="center"><%=VFINAL_RATING.elementAt(i)%></td>
						<td align="center"><%=VLIMIT_CATEGORY.elementAt(i)%></td>
						<td align="right"><%=VLIMIT_VALUE.elementAt(i)%></td>
						<td align="right"><%=VACCOUNT_RECEIVABLE.elementAt(i)%></td>
						<td align="right"><%=VACCOUNT_PAYABLE.elementAt(i)%></td>
						<td align="right"><%=VUNBILLED_RECEIVABLE.elementAt(i)%></td>
						<td align="right"><%=VUNBILLED_PAYABLE.elementAt(i)%></td>
						<td align="right"><%=VCURRENT_MONTH_UNDELIVERED.elementAt(i)%></td>
						<td align="right"><%=VFORWARD_MTM.elementAt(i)%></td>
						<td align="right"><%=VGROSS_EXPOSURE.elementAt(i)%></td>
						<td align="right"><%=VCASH_COLLATERAL.elementAt(i)%></td>
						<td align="right"><%=VLC_AMOUNT.elementAt(i)%></td>
						<td align="right"><%=VOTHER_COLLATERAL.elementAt(i)%></td>
						<td align="right"><%=VNET_EXPOSURE.elementAt(i)%></td>
						<td align="center"><%=VCURRENCY.elementAt(i)%></td>
						<td align="center"><%=VMARKET_TYPE.elementAt(i)%></td>
						<td align="center"><%=VINDUSTRY.elementAt(i)%></td>
					</tr>
				<%} %>
			<%}else{ %>
				<tr>
					<td colspan="26" align="center"><%=utilmsg.infoMessage("<b>No GEM Report Data Available!</b>") %></td>
				</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>