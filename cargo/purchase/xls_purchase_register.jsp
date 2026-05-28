<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="purchase" scope="request"></jsp:useBean>
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

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String month_to=request.getParameter("month_to")==null?""+currentMonth:request.getParameter("month_to");
String year_to=request.getParameter("year_to")==null?""+currentYear:request.getParameter("year_to");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");

if(month.length() == 1)
{
	month="0"+month; 
}
if(month_to.length() == 1)
{
	month_to="0"+month_to; 
}

purchase.setCallFlag("PURCHASE_REGISTER");
purchase.setComp_cd(owner_cd);
purchase.setMonth(month);
purchase.setYear(year);
purchase.setMonth_to(month_to);
purchase.setYear_to(year_to);
purchase.setSegment(segment);
purchase.init();

Vector VINVOICE_DT = purchase.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = purchase.getVINVOICE_DUE_DT();
Vector VINVOICE_NO = purchase.getVINVOICE_NO();
Vector VINVOICE_SEQ = purchase.getVINVOICE_SEQ();
Vector VPERIOD_START_DT = purchase.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = purchase.getVPERIOD_END_DT();
Vector VFREQ = purchase.getVFREQ();
Vector VFREQ_NM = purchase.getVFREQ_NM();
Vector VALLOC_QTY = purchase.getVALLOC_QTY();
Vector VSALES_PRICE = purchase.getVSALES_PRICE();
Vector VSALES_PRICE_UNIT = purchase.getVSALES_PRICE_UNIT();
Vector VRATE_NM = purchase.getVRATE_NM();
Vector VGROSS_AMT = purchase.getVGROSS_AMT();
Vector VTAX_AMT = purchase.getVTAX_AMT();
Vector VINVOICE_AMT = purchase.getVINVOICE_AMT();
Vector VADJ_SIGN = purchase.getVADJ_SIGN();
Vector VADJ_AMT = purchase.getVADJ_AMT();
Vector VNET_PAYABLE = purchase.getVNET_PAYABLE();
Vector VEXCHNAGE_RATE = purchase.getVEXCHNAGE_RATE();
Vector VSALES_PRICE_USD = purchase.getVSALES_PRICE_USD();
Vector VSALES_PRICE_UNIT_USD = purchase.getVSALES_PRICE_UNIT_USD();
Vector VRATE_NM_USD = purchase.getVRATE_NM_USD();
Vector VGROSS_AMT_USD = purchase.getVGROSS_AMT_USD();
Vector VTAX_AMT_USD = purchase.getVTAX_AMT_USD();
Vector VINVOICE_AMT_USD = purchase.getVINVOICE_AMT_USD();
Vector VADJ_SIGN_USD = purchase.getVADJ_SIGN_USD();
Vector VADJ_AMT_USD = purchase.getVADJ_AMT_USD();
Vector VNET_PAYABLE_USD = purchase.getVNET_PAYABLE_USD();
Vector VMONTH = purchase.getVMONTH();
Vector VCOUNTERPTY_CD = purchase.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = purchase.getVCOUNTERPTY_ABBR();
Vector VDEAL_NO = purchase.getVDEAL_NO();
Vector VBU_NM = purchase.getVBU_NM();
Vector VSEGMENT = purchase.getVSEGMENT();

Vector VDISPLAY_SEGMENT = purchase.getVDISPLAY_SEGMENT();
Vector VDISPLAY_SEGMENT_TYP = purchase.getVDISPLAY_SEGMENT_TYP();
Vector VINDEX = purchase.getVINDEX();
Vector VCOLOR = purchase.getVCOLOR();
Vector VLINKED_INV_REF = purchase.getVLINKED_INV_REF();
Vector VPAY_RECV_AMT =  purchase.getVPAY_RECV_AMT();
Vector VPAY_STATUS = purchase.getVPAY_STATUS();
Vector VINV_STATUS = purchase.getVINV_STATUS();
Vector VINVOICE_TYPE = purchase.getVINVOICE_TYPE();
Vector VDIS_REMITTANCE_NO = purchase.getVDIS_REMITTANCE_NO();
Vector VTCS_AMT=purchase.getVTCS_AMT();
Vector VTCS_AMT_USD = purchase.getVTCS_AMT_USD();
Vector VTDS_AMT=purchase.getVTDS_AMT();
Vector VTDS_AMT_USD = purchase.getVTDS_AMT_USD();

Vector VQTY_UNIT = purchase.getVQTY_UNIT();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
				
<table  width="100%" border="1">
	<tr>
		<th colspan="34" rowspan="2" align="left">Purchase Register From <%=month %>/<%=year %> To <%=month_to %>/<%=year_to %></th>
	</tr>
</table >
	<%int i=0;int k=0;
	for(int j=0; j<VDISPLAY_SEGMENT_TYP.size(); j++){ 
		int index = Integer.parseInt(""+VINDEX.elementAt(j));
	if(j!=0)
	{
	%>
	<%} %>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th align="left" colspan="33"><%=VDISPLAY_SEGMENT.elementAt(j) %></th>
			</tr>
			<tr>
				<th rowspan="2">Sr#</th>
				<th rowspan="2">Segment</th>
				<th rowspan="2">Month</th>
				<th rowspan="2">Trader</th>
				<th rowspan="2"><%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("N") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("CD") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("L")){ %>Cargo#<%}else{ %>Contract#<%} %></th>
				<th rowspan="2">BU</th>
				<th rowspan="2">Invoice Type</th>
				<th colspan="10">Invoice Details</th>
				<th colspan="7">USD Details</th>
				<th colspan="7">INR Details</th>
				<th rowspan="2">Payment Status</th>
				<th rowspan="2">Payment Received (INR)</th>
			</tr>
			<tr>
				<th>Invoice#</th>
				<th>Remittance#</th>
				<th>Invoice Status</th>
				<th>Billing Cycle</th>
				<th>Invoice Period</th>
				<th>Invoice Date</th>
				<th>Invoice Due Date</th>
				<th>Invoiced QTY</th>
				<th>Exchange Rate</th>
				<th>Price/Qty</th>
				<th>Gross Amount</th>
				<th>Tax</th>
				<th>Invoice Amount</th>
				<th>TCS Value</th>
				<th>TDS Value</th>
				<th>Adjustment Amount</th>
				<th>Net Payable</th>
				<th>Gross Amount</th>
				<th>Tax</th>
				<th>Invoice Amount</th>
				<th>TCS Value</th>
				<th>TDS Value</th>
				<th>Adjustment Amount</th>
				<th>Net Payable</th>
			</tr>
		</thead>
		<tbody>
		<%k=0;
		if(index > 0){ %>
			<%for(i=i; i<VINVOICE_SEQ.size(); i++){ 
				k+=1;
			%>
			<tr>
				<td align="center">
					<%=k%>
				</td>
				<td align="center"><%=VSEGMENT.elementAt(i)%></td>
				<td align="center"><%=VMONTH.elementAt(i)%></td>
				<td align="center"><%=VCOUNTERPTY_ABBR.elementAt(i)%></td>
				<td align="center"><%=VDEAL_NO.elementAt(i)%></td>
				<td align="center"><%=VBU_NM.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_TYPE.elementAt(i)%></td>
				<td align="left"><%=VINVOICE_NO.elementAt(i)%> <%if(!VLINKED_INV_REF.elementAt(i).equals("")){%>(Ref:<%=VLINKED_INV_REF.elementAt(i)%>)<%} %></td>
				<td align="center"><%=VDIS_REMITTANCE_NO.elementAt(i) %>
				<td align="center" style="background: <%=VCOLOR.elementAt(i)%>"><%=VINV_STATUS.elementAt(i) %></td>
				<td align="center"><%=VFREQ_NM.elementAt(i)%></td>
				<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
				<td align="right"><%=VALLOC_QTY.elementAt(i)%> <%=VQTY_UNIT.elementAt(i) %></td>
				<td align="right"><%=VEXCHNAGE_RATE.elementAt(i)%></td>
				<td align="right"><%=VSALES_PRICE.elementAt(i)%>&nbsp;<%=VRATE_NM.elementAt(i)%></td>
				<td align="right"><%=VGROSS_AMT_USD.elementAt(i)%></td>
				<td align="right"><%=VTAX_AMT_USD.elementAt(i) %></td>
				<td align="right"><%=VINVOICE_AMT_USD.elementAt(i)%></td>
				<td align="right"><%=VTCS_AMT_USD.elementAt(i) %></td>
				<td align="right"><%=VTDS_AMT_USD.elementAt(i) %></td>
				<td align="right"><%=VADJ_SIGN_USD.elementAt(i)%><%=VADJ_AMT_USD.elementAt(i)%></td>
				<td align="right"><%=VNET_PAYABLE_USD.elementAt(i)%></td>
				<td align="right"><%=VGROSS_AMT.elementAt(i)%></td>
				<td align="right"><%=VTAX_AMT.elementAt(i) %></td>
				<td align="right"><%=VINVOICE_AMT.elementAt(i)%></td>
				<td align="right"><%=VTCS_AMT.elementAt(i) %></td>
				<td align="right"><%=VTDS_AMT.elementAt(i) %></td>
				<td align="right"><%=VADJ_SIGN.elementAt(i)%><%=VADJ_AMT.elementAt(i)%></td>
				<td align="right"><%=VNET_PAYABLE.elementAt(i)%></td>
				<td align="center"><%=VPAY_STATUS.elementAt(i) %></td>
				<td align="right"><%=VPAY_RECV_AMT.elementAt(i) %></td>
			</tr>
			<%
				if(k==index)
				{
					i=i+1;
					break;
				}
			} %>
		<%}else{ %>
			<tr>
				<td colspan="33" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Month!</b>") %></td>
			</tr>
		<%} %>
		</tbody>
	</table>
			
	<%} %>
				


</body>

</html>