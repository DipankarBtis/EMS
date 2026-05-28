
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CR_ExposureTracking" id="cr_report" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String prev_date=utildate.getPreviousDate();
String from_dt = request.getParameter("from_dt")==null?prev_date:request.getParameter("from_dt");
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

cr_report.setCallFlag("IGX_MARGIN");
cr_report.setComp_cd(owner_cd);
cr_report.setFrom_dt(from_dt);
cr_report.init();

Vector VSTART_DT = cr_report.getVSTART_DT();
Vector VEND_DT = cr_report.getVEND_DT();
Vector VCONTRACT_TYPE = cr_report.getVCONTRACT_TYPE();
Vector VCOUNTERPARTY_CD = cr_report.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM  = cr_report.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = cr_report.getVCOUNTERPARTY_ABBR();
Vector VTCQ = cr_report.getVTCQ();
Vector VDCQ = cr_report.getVDCQ();
Vector VCONT_REF_NO = cr_report.getVCONT_REF_NO();
Vector VTRADE_REF_NO = cr_report.getVTRADE_REF_NO();
Vector VSIGNING_DT = cr_report.getVSIGNING_DT();
Vector VRATE = cr_report.getVRATE();
Vector VRATE_UNIT = cr_report.getVRATE_UNIT();
Vector VFCC_BY = cr_report.getVFCC_BY();
Vector VFCC_DT = cr_report.getVFCC_DT();
Vector VENT_BY = cr_report.getVENT_BY();
Vector VENT_DT = cr_report.getVENT_DT();
Vector VAGMT_BASE = cr_report.getVAGMT_BASE();
Vector VPOST_MARGIN = cr_report.getVPOST_MARGIN();
Vector VDISPLAY_DEAL_MAP = cr_report.getVDISPLAY_DEAL_MAP();
Vector VACCOUNT = cr_report.getVACCOUNT();
Vector VBG_DROPOFF_DT = cr_report.getVBG_DROPOFF_DT();
Vector VTRADE_VALUE = cr_report.getVTRADE_VALUE();
Vector VPOST_TRADE_MARGIN = cr_report.getVPOST_TRADE_MARGIN();
Vector VPRICE_TYPE = cr_report.getVPRICE_TYPE();
Vector VTAX = cr_report.getVTAX();
Vector VTEMP_REPORT_DT = cr_report.getVTEMP_REPORT_DT();
Vector VMARGIN_AVAIL = cr_report.getVMARGIN_AVAIL();
Vector VMARGIN_USED = cr_report.getVMARGIN_USED();
Vector VBG_VALUE = cr_report.getVBG_VALUE();
Vector VTEMP_MARGIN_USED = cr_report.getVTEMP_MARGIN_USED();
Vector VLEGAL_ENTITY = cr_report.getVLEGAL_ENTITY();

//HashMap<String, String> IGXDealNo = cr_report.getIGXDealNo();
HashMap<String, HashMap> HIGX_MARGIN_USED = cr_report.getHIGX_MARGIN_USED();
HashMap<String, HashMap> HIGX_BUYSELL = cr_report.getHIGX_BUYSELL();
HashMap<String, HashMap> HCOLOR = cr_report.getHCOLOR();
HashMap<String, String> HIGX_DEAL_NO = cr_report.getHIGX_DEAL_NO();

String bg_validity = cr_report.getBg_validity();
String bg_ref_no = cr_report.getBg_ref_no();
String bg_value = cr_report.getBg_value();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="17" rowspan="2" align="left">IGX Margining Report (<%=from_dt %>)</th>
		</tr>
	</table>&nbsp;
	
	<table width="100%" border="1">
		<tr>
			<th colspan="2"></th>
		<tr>
			<th>BG Reference :</th><td><%=bg_ref_no %></td>
		</tr>
		<tr>
			<th>BG Validity :</th><td><%=bg_validity %></td>
		</tr>
		<tr>
			<th>BG Value :</th><td><%=bg_value%></td>
		</tr>
		<tr>
			<th>Margin Used As on <%=from_dt%> :</th><td><%=VMARGIN_USED.elementAt(7)%></td>
		</tr>
		<tr>
			<th>Margin Available As on <%=from_dt%> :</th><td><%=VMARGIN_AVAIL.elementAt(7) %></td>
		</tr>
	</table>&nbsp;
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>SR#</th>
				<th align="center">Legal Entity</th>
				<th align="center">Counterparty Name</th>
				<th align="center">Contract#</th>
				<th align="center">Trade Ref#</th>
				<th align="center">Contract Ref#</th>
				<th align="center">Buy/Sell</th>
				<th align="center">Deal Date</th>
				<th align="center">Payment Due Date</th>
				<th align="center">Start Date-End Date</th>
				<th align="center">Price Type</th>
				<th align="center">Sales Rate (INR)</th>
				<th align="center">tax(%)</th>
				<th align="center">Post Trade Margin(%)</th>
				<th align="center">Total Volume(MMBTU)</th>
				<th align="center">BG Dropoff Date</th>
				<th align="center">Trade Value(INR)</th>
				<th align="center">Post Trade Margin(INR)</th>
			</tr>
		</thead>
		<tbody>
		<%if(VCOUNTERPARTY_CD.size()>0){
			int j=0;%>
			<%for(int i=0;i<VCOUNTERPARTY_CD.size(); i++){
			j++;%>
				<tr>
					<td align="center"><%=j %></td>
					<td align="center"><%=VLEGAL_ENTITY.elementAt(i) %></td>
					<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
					<td align="center"><%=VDISPLAY_DEAL_MAP.elementAt(i) %></td>
					<td align="center"><%=VTRADE_REF_NO.elementAt(i) %></td>
					<td align="center"><%=VCONT_REF_NO.elementAt(i) %></td>
					<td align="center"><%=VACCOUNT.elementAt(i) %></td>
					<td align="center"><%=VSIGNING_DT.elementAt(i) %></td>
					<td align="center">D+2</td>
					<td align="center"><%=VSTART_DT.elementAt(i) %> - <%=VEND_DT.elementAt(i) %></td>
					<td align="center"><%=VPRICE_TYPE.elementAt(i) %></td>
					<td align="right"><%=VRATE.elementAt(i) %></td>
					<td align="center"><%=VTAX.elementAt(i) %></td>
					<td align="center"><%=VPOST_MARGIN.elementAt(i) %></td>
					<td align="right"><%=VTCQ.elementAt(i) %></td>
					<td align="center"><%=VBG_DROPOFF_DT.elementAt(i) %></td>
					<td align="right"><%=VTRADE_VALUE.elementAt(i) %></td>
					<td align="right"><%=VPOST_TRADE_MARGIN.elementAt(i) %></td>
				</tr>
			<%} %>
		<%}else{ %>
			<tr><td colspan="18" align="center"><b>IGX Margining Report Is Not Available for Selected Date!</b></td></tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>
