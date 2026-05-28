<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.accounting.DataBean_Accounting" id="accounting" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String month_to=request.getParameter("month_to")==null?""+currentMonth:request.getParameter("month_to");
String year_to=request.getParameter("year_to")==null?""+currentYear:request.getParameter("year_to");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");
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

if(month.length() == 1)
{
	month="0"+month; 
}
if(month_to.length() == 1)
{
	month_to="0"+month_to; 
}

accounting.setCallFlag("SALES_REGISTER");
accounting.setComp_cd(owner_cd);
accounting.setMonth(month);
accounting.setYear(year);
accounting.setMonth_to(month_to);
accounting.setYear_to(year_to);
accounting.setSegment(segment);
accounting.init();

Vector VSEGMENT = accounting.getVSEGMENT();
Vector VSEGMENT_TYPE = accounting.getVSEGMENT_TYPE();
Vector VCOUNTERPARTY_CD = accounting.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = accounting.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = accounting.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = accounting.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = accounting.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = accounting.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = accounting.getVCONT_NO();
Vector VCONT_REV_NO = accounting.getVCONT_REV_NO();
Vector VAGMT_NO = accounting.getVAGMT_NO();
Vector VAGMT_REV_NO = accounting.getVAGMT_REV_NO();
Vector VDIS_CONT_MAPPING = accounting.getVDIS_CONT_MAPPING();
Vector VCONTRACT_TYPE = accounting.getVCONTRACT_TYPE();
Vector VPERIOD_START_DT = accounting.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = accounting.getVPERIOD_END_DT();
Vector VSALES_PRICE = accounting.getVSALES_PRICE();
Vector VSALES_PRICE_CD = accounting.getVSALES_PRICE_CD();
Vector VSALES_PRICE_NM = accounting.getVSALES_PRICE_NM();
Vector VFINANCIAL_YEAR = accounting.getVFINANCIAL_YEAR();
Vector VINVOICE_RAISED_IN = accounting.getVINVOICE_RAISED_IN();
Vector VINVOICE_TYPE = accounting.getVINVOICE_TYPE();
Vector VINVOICE_NO = accounting.getVINVOICE_NO();
Vector VINVOICE_SEQ = accounting.getVINVOICE_SEQ();
Vector VINVOICE_DT = accounting.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = accounting.getVINVOICE_DUE_DT();



Vector VGROSS_AMT = accounting.getVGROSS_AMT();
Vector VTAX_AMT = accounting.getVTAX_AMT();
Vector VINVOICE_AMT = accounting.getVINVOICE_AMT();
Vector VTCS_AMT = accounting.getVTCS_AMT();
Vector VTCS_FACTOR = accounting.getVTCS_FACTOR();
Vector VTDS_GROSS_PERCENT = accounting.getVTDS_GROSS_PERCENT();
Vector VTDS_GROSS_AMT = accounting.getVTDS_GROSS_AMT();
Vector VTDS_TAX_PERCENT = accounting.getVTDS_TAX_PERCENT();
Vector VTDS_TAX_AMT = accounting.getVTDS_TAX_AMT();
Vector VTAX_STRUCT_DTL = accounting.getVTAX_STRUCT_DTL();
Vector VTDS_TCS_FLAG = accounting.getVTDS_TCS_FLAG();
Vector VNET_PAYABLE_AMT = accounting.getVNET_PAYABLE_AMT();


Vector VCONTRACT_TYPE_NM = accounting.getVCONTRACT_TYPE_NM();
Vector VMONTH_NM = accounting.getVMONTH_NM();
Vector VTCQ = accounting.getVTCQ();
Vector VSUPPLIED_QTY_MMBTU = accounting.getVSUPPLIED_QTY_MMBTU();
Vector VBALANCE_QTY_MMBTU = accounting.getVBALANCE_QTY_MMBTU();
Vector VSTART_DT = accounting.getVSTART_DT();
Vector VEND_DT = accounting.getVEND_DT();
Vector VALLOC_QTY = accounting.getVALLOC_QTY();
Vector VEXCHNG_RATE = accounting.getVEXCHNG_RATE();

Vector VSALES_AMT = accounting.getVSALES_AMT();
Vector VTRANSPORT_CHARGES_AMT = accounting.getVTRANSPORT_CHARGES_AMT();
Vector VMARKETING_MARGIN_AMT = accounting.getVMARKETING_MARGIN_AMT();
Vector VOTHER_CHARGES_AMT = accounting.getVOTHER_CHARGES_AMT();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="47" rowspan="2" align="left">Sales Register (<%=month%>/<%=year%> - <%=month_to%>/<%=year_to%>)</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th rowspan="2">SR#</th>
				<th rowspan="2">Month
				</th>
				<th rowspan="2">Segment
				</th>
				<th rowspan="2">Counterparty
				</th>
				<th colspan="5" style="background: #000066; color: white;">Contract Details</th>
				<th colspan="11" style="background: #000066; color: white;">Invoice Details</th>
				<th colspan="11" style="background: #000066; color: white;">INR</th>
				<th colspan="11" style="background: #000066; color: white;">USD</th>
			</tr>
			<tr>	
				<th>Contract#<br> [Contract/Trade Ref]
				</th>										
				<th>Contract Period</th>
				<th>Booked MMBTU</th>
				<th>Supplied MMBTU</th>
				<th>Balance MMBTU</th>
				<th>Invoice Type
				</th>										
				<th>Invoice#
				</th>
				<th>Invoice Date</th>
				<th>Invoice Due Date</th>
				<th>Billing Period</th>
				<th>Invoiced MMBTU</th>
				<th>Sales Rate</th>
				<th>Rate Unit/MMBTU
				</th>
				<th>Sales Amount</th>
				<th>Exchange Rate (INR/USD)</th>
				<th>Invoice Raised In
				</th>
				<th>Transportation Tariff</th>
				<th>Marketing Margin</th>
				<th>Other Charges </th>
				<th>Total Gross Amount</th>
				<th>Tax Structure
				</th>
				<th>Tax </th>
				<th>Invoice Amount</th>
				<th>TCS/TDS
				</th>
				<th>TCS/TDS%</th>
				<th>+TCS/-TDS Amount</th>
				<th>Net Receivable</th>
				<th>Transportation Tariff</th>
				<th>Marketing Margin</th>
				<th>Other Charges </th>
				<th>Gross Amt</th>
				<th>Tax Structure</th>
				<th>Tax </th>
				<th>Invoice Amt</th>
				<th>TCS/TDS</th>
				<th>TCS/TDS%</th>
				<th>+TCS/-TDS Amt</th>
				<th>Net Receivable</th>												
			</tr>
		</thead>
		<tbody>
		<%if(VCOUNTERPARTY_CD.size() > 0){ %>
			<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
			<tr>
				<td><%=i+1%></td>
				<td align="center"><%=VMONTH_NM.elementAt(i)%></td>
				<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i)%></td>
				<td><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
				<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i).toString().replace("<br>"," ")%></td>
				<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
				<td align="right"><%=VTCQ.elementAt(i)%></td>
				<td align="right"><%=VSUPPLIED_QTY_MMBTU.elementAt(i) %></td>
				<td align="right"><%=VBALANCE_QTY_MMBTU.elementAt(i) %></td>
				<td align="center"><%=VINVOICE_TYPE.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
				<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i) %></td>
				<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
				<td align="right"><%=VSALES_PRICE.elementAt(i)%></td>
				<td align="center" style="background: #b3f0ff;"><%=VSALES_PRICE_NM.elementAt(i) %></td>
				<td align="right"><%=VSALES_AMT.elementAt(i)%></td>
				<td align="right"><%=VEXCHNG_RATE.elementAt(i) %></td>
				<td align="center" style="background: #b3f0ff;"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>
				<td align="right"><%=VTRANSPORT_CHARGES_AMT.elementAt(i)%></td>
				<td align="right"><%=VMARKETING_MARGIN_AMT.elementAt(i)%></td>
				<td align="right"><%=VOTHER_CHARGES_AMT.elementAt(i)%></td>
				<td align="right"><%=VGROSS_AMT.elementAt(i)%></td>
				<td align="center"><%=VTAX_STRUCT_DTL.elementAt(i)%></td>
				<td align="right"><%=VTAX_AMT.elementAt(i)%></td>
				<td align="right"><%=VINVOICE_AMT.elementAt(i) %></td>
				<td align="center"><%=VTDS_TCS_FLAG.elementAt(i)%></td>
				<td align="right"><% if (VTDS_TCS_FLAG.elementAt(i).equals("TCS")) {%>
					<%=VTCS_FACTOR.elementAt(i) %> 
					<%}else{%> <%=VTDS_GROSS_PERCENT.elementAt(i)%> 
					<%}%> </td>
				<td align="right"><% if (VTDS_TCS_FLAG.elementAt(i).equals("TCS")) {%>
					<%=VTCS_AMT.elementAt(i) %>
					<%}else{%> <%=VTDS_GROSS_AMT.elementAt(i)%>
					<%}%> </td>	
				<td align="right"><%=VNET_PAYABLE_AMT.elementAt(i) %> </td>	
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<%}%>
		<%}else{%>
			<tr>
				<td colspan="47" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
			</tr>
		<%}%>
		</tbody>
	</table>
</div>
</body>
</html>