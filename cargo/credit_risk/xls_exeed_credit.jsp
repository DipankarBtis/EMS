<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DB_CR_ExposureTracking" id="credit_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String yesdate = utildate.getPreviousDate();

String report_dt=request.getParameter("report_dt")==null?yesdate:request.getParameter("report_dt");
String expo_type=request.getParameter("expo_type")==null?"R":request.getParameter("expo_type");
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

credit_risk.setCallFlag("CREDIT_EXCEED");
credit_risk.setReport_dt(report_dt);
credit_risk.setComp_cd(owner_cd);
credit_risk.setExpo_type(expo_type);
credit_risk.init();

Vector VACCOUNT = credit_risk.getVACCOUNT();
Vector VCOUNTERPARTY_CD = credit_risk.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = credit_risk.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = credit_risk.getVCOUNTERPARTY_ABBR();
Vector VAGMT_NO = credit_risk.getVAGMT_NO();
Vector VAGMT_REV_NO = credit_risk.getVAGMT_REV_NO();
Vector VCONT_NO = credit_risk.getVCONT_NO();
Vector VCONT_REV_NO = credit_risk.getVCONT_REV_NO();
Vector VDISPLAY_DEAL_MAP = credit_risk.getVDISPLAY_DEAL_MAP();
Vector VSIGNING_DT = credit_risk.getVSIGNING_DT();
Vector VSTART_DT = credit_risk.getVSTART_DT();
Vector VEND_DT = credit_risk.getVEND_DT();
Vector VRATE = credit_risk.getVRATE();
Vector VPRICE_TYPE = credit_risk.getVPRICE_TYPE();
Vector VRATE_UNIT = credit_risk.getVRATE_UNIT();
Vector VRATE_UNIT_NM = credit_risk.getVRATE_UNIT_NM();
Vector VTCQ = credit_risk.getVTCQ();
Vector VDCQ = credit_risk.getVDCQ();
Vector VCREDIT_EXCEED = credit_risk.getVCREDIT_EXCEED();
Vector VCREDIT_EXCEED_INFO = credit_risk.getVCREDIT_EXCEED_INFO();
Vector VCREDIT_EXCEED_USD = credit_risk.getVCREDIT_EXCEED_USD();
Vector VCREDIT_EXCEED_REASON = credit_risk.getVCREDIT_EXCEED_REASON();
Vector VCREDIT_EXCEED_REASON_FLAG = credit_risk.getVCREDIT_EXCEED_REASON_FLAG();


Vector VINDEX = credit_risk.getVINDEX();
Vector VEXPOSURE_HEADING = credit_risk.getVEXPOSURE_HEADING();
Vector VEXPOSURE_HEADING_FLAG = credit_risk.getVEXPOSURE_HEADING_FLAG();

String total_igx_cr_exceed = credit_risk.getTotal_igx_cr_exceed();
String total_igx_cr_exceed_usd = credit_risk.getTotal_igx_cr_exceed_usd();


String expo_type_nm="";
if(expo_type.equals("R"))
{
	expo_type_nm="Receivable";
}
else
{
	expo_type_nm="Payable";
}
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="12" rowspan="2" align="left">Credit Exceed Report <%=report_dt %></th>
		</tr>
	</table>
	<%int i=0;
		for(int j=0; j < VEXPOSURE_HEADING.size(); j++) { 
			String expo_head_flag=""+VEXPOSURE_HEADING_FLAG.elementAt(j);
			int index=Integer.parseInt(""+VINDEX.elementAt(j));
		%>
	<br>
	<label class="form-label subheader"><%=VEXPOSURE_HEADING.elementAt(j) %></label>
	<br>	
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>Sr#</th>
				<th>Account</th>										
				<th>Counterparty</th>
				<th>Contract#</th>
				<th>Signing Date</th>
				<th>Contract Period</th>
				<th>Price Type</th>
				<th>Price Rate</th>
				<th>Rate Unit</th>
				<th>Credit Exceed (INR)</th>
				<th>Credit Exceed (USD)</th>
				<th>Exceed Reason</th>
			</tr>
		</thead>
		<tbody>
		<%if(index>0){ 
			int k=0;
			int sr=0;
		%>
			<%for(i=i; i<VCOUNTERPARTY_CD.size(); i++){ 
				k=k+1;
				if (VCREDIT_EXCEED_REASON_FLAG.elementAt(i).equals("Y"))
				{sr+=1;
				%>
					<tr>
						<td align="center"><%=sr%></td>
						<td align="center"><%=VACCOUNT.elementAt(i)%></td>
						<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
						<td align="center"><%=VDISPLAY_DEAL_MAP.elementAt(i)%></td>
						<td align="center"><%=VSIGNING_DT.elementAt(i)%></td>
						<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
						<td align="center"><%=VPRICE_TYPE.elementAt(i) %></td>
						<td align="right"><%=VRATE.elementAt(i)%></td>
						<td align="center"><%=VRATE_UNIT_NM.elementAt(i)%></td>
						<td align="right" <%-- title= "<%=VCREDIT_EXCEED_INFO.elementAt(i)%>" --%>><%=VCREDIT_EXCEED.elementAt(i)%></td>
						<td align="right"><%=VCREDIT_EXCEED_USD.elementAt(i)%></td>
						<td><%=VCREDIT_EXCEED_REASON.elementAt(i) %></td>
					</tr>
				<%}
				if(k==index){%>
					<%if(expo_head_flag.equals("I")){%>
					<tr style="font-weight: bold;">
						<td colspan="9" align="right"><b>IGX Summary</b></td>
						<td align="right"><%=total_igx_cr_exceed %></td>
						<td align="right"><%=total_igx_cr_exceed_usd%></td>
						<td align="right"></td>
					</tr>
					<%} %>
					<%i=i+1;
					break;
			 	}%>
			<%} %>
		<%}else{ %>
			<tr>
				<td colspan="12">
					<div align="center"><b>No Exposure for the Report Date!</b></div>
				</td>
			</tr>
		<%} %>
		</tbody>
	</table>
	<%} %>
</body>
</html>