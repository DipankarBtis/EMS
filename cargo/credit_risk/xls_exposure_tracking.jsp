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

credit_risk.setCallFlag("EXPOSURE_TRACKING");
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
Vector VPRICE_TYPE = credit_risk.getVPRICE_TYPE();
Vector VRATE = credit_risk.getVRATE();
Vector VRATE_UNIT = credit_risk.getVRATE_UNIT();
Vector VRATE_UNIT_NM = credit_risk.getVRATE_UNIT_NM();
Vector VTCQ = credit_risk.getVTCQ();
Vector VDCQ = credit_risk.getVDCQ();
Vector VBILLED_AMT = credit_risk.getVBILLED_AMT();
Vector VUNBILLED_AMT = credit_risk.getVUNBILLED_AMT();
Vector VUNBILLED_CURRENT_MONTH = credit_risk.getVUNBILLED_CURRENT_MONTH(); 	
Vector VFORWARD_NOTIONAL = credit_risk.getVFORWARD_NOTIONAL(); 	
Vector VGROSS_EXPOSURE = credit_risk.getVGROSS_EXPOSURE(); 	
Vector VGROSS_EXPOSURE_TAX = credit_risk.getVGROSS_EXPOSURE_TAX();
Vector VCOLLATERAL_VALUE = credit_risk.getVCOLLATERAL_VALUE();
Vector VCOLLATERAL_INFO = credit_risk.getVCOLLATERAL_INFO();
Vector VNET_EXPOSURE = credit_risk.getVNET_EXPOSURE();
Vector VLIMIT = credit_risk.getVLIMIT();
Vector VCREDIT_EXCEED = credit_risk.getVCREDIT_EXCEED();
Vector VNET_EXPOSURE_USD = credit_risk.getVNET_EXPOSURE_USD();
Vector VCREDIT_EXCEED_USD = credit_risk.getVCREDIT_EXCEED_USD();

Vector VCONSUMED_LIMIT = credit_risk.getVCONSUMED_LIMIT();
Vector VCREDIT_EXCEED_INFO = credit_risk.getVCREDIT_EXCEED_INFO();

Vector VLIMIT_VALUE_LINKED_COMP = credit_risk.getVLIMIT_VALUE_LINKED_COMP();
Vector VPARENT_LIMIT_VALUE = credit_risk.getVPARENT_LIMIT_VALUE();

Vector VINDEX = credit_risk.getVINDEX();
Vector VEXPOSURE_HEADING = credit_risk.getVEXPOSURE_HEADING();
Vector VEXPOSURE_HEADING_FLAG = credit_risk.getVEXPOSURE_HEADING_FLAG();

String total_igx_ac_rece = credit_risk.getTotal_igx_ac_rece();
String total_igx_unbilled_rece = credit_risk.getTotal_igx_unbilled_rece();
String total_igx_delv_curr_mth = credit_risk.getTotal_igx_delv_curr_mth();
String total_igx_fwd_not = credit_risk.getTotal_igx_fwd_not();
String total_igx_gross_expo = credit_risk.getTotal_igx_gross_expo();
String total_igx_gross_expo_incl_tax = credit_risk.getTotal_igx_gross_expo_incl_tax();
String total_igx_net_expo = credit_risk.getTotal_igx_net_expo();
String total_igx_limit = credit_risk.getTotal_igx_limit();
String total_igx_cr_exceed = credit_risk.getTotal_igx_cr_exceed();
String total_igx_cr_exceed_usd = credit_risk.getTotal_igx_cr_exceed_usd();
String total_igx_net_expo_usd = credit_risk.getTotal_igx_net_expo_usd();

String isEodProcessDone=credit_risk.getIsEodProcessDone();
String eodProcessDoneOn=credit_risk.getEodProcessDoneOn();

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
			<th colspan="22" rowspan="2" align="left">Exposure Tracking Report <%=report_dt%></th>
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
					<th rowspan="2">Sr#</th>
					<th rowspan="2">Account</th>										
					<th rowspan="2">Counterparty</th>
					<th rowspan="2">Contract#</th>
					<th rowspan="2">Signing Date</th>
					<th rowspan="2">Contract Period</th>
					<th rowspan="2">Price Type</th>
					<th rowspan="2">Price Rate</th>
					<th rowspan="2">Rate Unit</th>
					<th colspan="11">INR</th>
					<th colspan="2">USD</th>
				</tr>
				<tr>
					<th>Account <%=expo_type_nm %></th>
					<th>Unbilled <%=expo_type_nm %></th>
					<th>Undelivered Current Month</th>
					<th>Forward Notional Next Month</th>
					<th>Gross Exposure</th>
					<th>Gross Exposure<br>(Incl. Tax)</th>
					<th>Collateral Value</th>
					<th>Net Exposure</th>
					<th>Own Limit</th>
					<th>Parent Limit</th>
					<th>Credit Exceed</th>
					<th>Net Exposure</th>
					<th>Credit Exceed</th>
				</tr>
			</thead>
			<tbody>
			<%if(index>0){ 
				int k=0;
			%>
				<%for(i=i; i<VCOUNTERPARTY_CD.size(); i++){ 
					k=k+1;
				%>
				<tr>
					<td align="center"><%=k%></td>
					<td align="center"><%=VACCOUNT.elementAt(i)%></td>
					<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
					<td align="center"><%=VDISPLAY_DEAL_MAP.elementAt(i)%></td>
					<td align="center"><%=VSIGNING_DT.elementAt(i)%></td>
					<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
					<td align="center"><%=VPRICE_TYPE.elementAt(i) %></td>
					<td align="right"><%=VRATE.elementAt(i)%></td>
					<td align="center"><%=VRATE_UNIT_NM.elementAt(i)%></td>
					<td align="right"><%=VBILLED_AMT.elementAt(i)%></td>
					<td align="right"><%=VUNBILLED_AMT.elementAt(i)%></td>
					<td align="right"><%=VUNBILLED_CURRENT_MONTH.elementAt(i)%></td>
					<td align="right"><%=VFORWARD_NOTIONAL.elementAt(i)%></td>
					<td align="right"><%=VGROSS_EXPOSURE.elementAt(i)%></td>
					<td align="right"><%=VGROSS_EXPOSURE_TAX.elementAt(i)%></td>
					<td align="right"><%=VCOLLATERAL_VALUE.elementAt(i)%></td>
					<td align="right"><%=VNET_EXPOSURE.elementAt(i)%></td>
					<td align="right">
						<%if(isEodProcessDone.equals("Y")){ %>
							<%=VLIMIT.elementAt(i) %><br>
						<%} else {%>
							<%if(!VLIMIT_VALUE_LINKED_COMP.elementAt(i).equals("")) 
							{ %>
								<font color=blue><%=VLIMIT.elementAt(i) %></font>
							<%} 
							else 
							{ %> 
								<%=VLIMIT.elementAt(i) %>
							<%} %>
						<%} %>		
					</td>
					<td align="right">
						<%if(isEodProcessDone.equals("Y")){ %>
							<%-- <%=VLIMIT.elementAt(i) %><br> --%>
							<font><%=VPARENT_LIMIT_VALUE.elementAt(i) %></font>
						<%} else {%>
							<%if(!VLIMIT_VALUE_LINKED_COMP.elementAt(i).equals("")) 
							{ %>
								<%-- <font color=blue><%=VLIMIT.elementAt(i) %></font><br> --%>
								<font><%=VPARENT_LIMIT_VALUE.elementAt(i) %></font>
							<%} 
							else 
							{ %> 
								<%-- <%=VLIMIT.elementAt(i) %><br> --%>
								<font><%=VPARENT_LIMIT_VALUE.elementAt(i) %></font>
							<%} %>
						<%} %>	 
					</td>
					<td align="right"><%=VCREDIT_EXCEED.elementAt(i)%></td>
					<td align="right"><%=VNET_EXPOSURE_USD.elementAt(i) %></td>
					<td align="right"><%=VCREDIT_EXCEED_USD.elementAt(i)%></td>
				</tr>
					<%if(k==index){%>
						<%if(expo_head_flag.equals("I")){%>
						<tr style="font-weight: bold;">
							<td colspan="9" align="right"><b>IGX Summary</b></td>
							<td align="right"><%=total_igx_ac_rece%></td>
							<td align="right"><%=total_igx_unbilled_rece%></td>
							<td align="right"><%=total_igx_delv_curr_mth%></td>
							<td align="right"><%=total_igx_fwd_not%></td>
							<td align="right"><%=total_igx_gross_expo%></td>
							<td align="right"><%=total_igx_gross_expo_incl_tax%></td>
							<td align="right"></td>
							<td align="right"><%=total_igx_net_expo%></td>
							<td align="right"><%=total_igx_limit %></td>
							<td align="right"><%=total_igx_cr_exceed %></td>
							<td align="right"><%=total_igx_net_expo_usd %></td>
							<td align="right"><%=total_igx_cr_exceed_usd%></td>
						</tr>
						<%} %>
						<%i=i+1;
						break;
					 }%>
				<%} %>
			<%}else{ %>
				<tr>
					<td colspan="22">
						<font><b>No Exposure for the Report Date <%=report_dt%></b></font>
					</td>
				</tr>
			<%} %>
				</tbody>
			</table>
	<%} %>
<%if(isEodProcessDone.equals("Y")){ %>
<font><b>Exposure Data Freezed on <%=eodProcessDoneOn%> for the Report Date <%=report_dt%></b></font>					
<%} %>
</body>

</html>