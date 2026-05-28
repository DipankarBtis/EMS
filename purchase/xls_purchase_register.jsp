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

Vector VSPLIT_VALUE = purchase.getVSPLIT_VALUE();
Vector VTYPE_FLAG = purchase.getVTYPE_FLAG();

Vector VSEGMENT_INDEX = purchase.getVSEGMENT_INDEX();
Vector VHEADER_DISPLAY_SEGMENT = purchase.getVHEADER_DISPLAY_SEGMENT();
Vector VHEADER_DISPLAY_SEGMENT_TYP = purchase.getVHEADER_DISPLAY_SEGMENT_TYP();
Vector VTRANSPORT_AMT = purchase.getVTRANSPORT_AMT();
Vector VMARKET_AMT = purchase.getVMARKET_AMT();
Vector VOTHER_CHARGE_AMT = purchase.getVOTHER_CHARGE_AMT();
Vector VTRANSPORT_AMT_USD = purchase.getVTRANSPORT_AMT_USD();
Vector VMARKET_AMT_USD = purchase.getVMARKET_AMT_USD();
Vector VOTHER_CHARGE_AMT_USD = purchase.getVOTHER_CHARGE_AMT_USD();
Vector VTAX_STRUCT_DTL = purchase.getVTAX_STRUCT_DTL();
Vector VTOTAL_ALLOC_QTY = purchase.getVTOTAL_ALLOC_QTY();
Vector VTOTAL_GROSS_USD = purchase.getVTOTAL_GROSS_USD();
Vector VTOTAL_GROSS_INR = purchase.getVTOTAL_GROSS_INR();
Vector VTOTAL_INV_USD = purchase.getVTOTAL_INV_USD();
Vector VTOTAL_INV_INR = purchase.getVTOTAL_INV_INR();
Vector VTOTAL_NET_PAY_USD = purchase.getVTOTAL_NET_PAY_USD();
Vector VTOTAL_NET_PAY_INR = purchase.getVTOTAL_NET_PAY_INR();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
				
<table  width="100%" border="1">
	<tr>
		<th colspan="40" rowspan="2" align="left">Purchase Register From <%=month %>/<%=year %> To <%=month_to %>/<%=year_to %></th>
	</tr>
</table >
	<%int i=0;int k=0;
	for(int j=0; j<VDISPLAY_SEGMENT_TYP.size(); j++){ 
		int index = Integer.parseInt(""+VINDEX.elementAt(j));
	if(j!=0)
	{
	%>
	<%} %>
	<div class="row">
			<div class="col-sm-12 col-xs-12 col-md-12">
				&nbsp;
			</div>
		</div> 
	<table  width="100%" border="1">
		<tr>
			<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K")){%>
				<th colspan="32" rowspan="2" align="left"><%=VDISPLAY_SEGMENT.elementAt(j)%> Purchase Register </th>
			<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<th colspan="30" rowspan="2" align="left"><%=VDISPLAY_SEGMENT.elementAt(j)%> Purchase Register </th>
			<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
				<th colspan="21" rowspan="2" align="left"><%=VDISPLAY_SEGMENT.elementAt(j)%> Purchase Register </th>
			<%}else{ %>
				<th colspan="40" rowspan="2" align="left"><%=VDISPLAY_SEGMENT.elementAt(j)%> Purchase Register </th>
			<%} %>
		</tr>
	</table >
	<table width="100%" border="1">
		<thead>
			<tr>
				<th rowspan="2">Sr#</th>
				<th rowspan="2">Segment</th>
				<th rowspan="2">Month</th>
				<th rowspan="2">Trader</th>
				<th rowspan="2"><%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("N") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("CD") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("L")){ %>Cargo#<%}else{ %>Contract#<%} %></th>
				<th rowspan="2">BU</th>
				<th rowspan="2">Invoice Type</th>
				<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<th colspan="8">Invoice Details</th>
				<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<th colspan="9">Invoice Details</th>
				<%}else{ %>
					<th colspan="11">Invoice Details</th>
				<%} %>
				<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<th colspan="7">USD Details</th>
					<th colspan="7">INR Details</th>
				<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<th colspan="3">USD Details</th>
					<th colspan="3">INR Details</th>
				<%}else{ %>
					<th colspan="10">USD Details</th>
					<th colspan="10">INR Details</th>
				<%} %>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<th rowspan="2">Payment Status</th>
				<th rowspan="2">Payment Received (INR)</th>
				<%} %>
			</tr>
			<tr>
				<th>Invoice#</th>
				<th>Remittance#</th>
				<th>Invoice Status</th>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<th>Billing Cycle</th>
				<%} %>
				<th>Invoice Period</th>
				<th>Invoice Date</th>
				<th>Invoice Due Date</th>
				<th>Invoiced Qty</th>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
				<th>Exchange Rate</th>
				<th>Tax Structure</th>
				<%} %>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<th>Price/Qty</th>
				<%} %>
				<th>Gross Amount</th>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<th>Market Margin</th>
				<th>Other Charges</th>
				<th>Transportation Charges</th>
				<%} %>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<th>Tax</th>
				<%} %>
				<th>Invoice Amount</th>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<th>TCS Value</th>
					<th>TDS Value</th>
					<th>Adjustment Amount</th>
				<%} %>
				<th>Net Payable</th>
				<!-- <th>Price/Qty</th> -->
				<th>Gross Amount</th>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<th>Market Margin</th>
				<th>Other Charges</th>
				<th>Transportation Charges</th>
				<%} %>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<th>Tax</th>
				<%} %>
				<th>Invoice Amount</th>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<th>TCS Value</th>
					<th>TDS Value</th>
					<th>Adjustment Amount</th>
				<%} %>
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
				<td align="center"><%=VDEAL_NO.elementAt(i)%>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
						<%if(!VSPLIT_VALUE.elementAt(i).equals("")){ %>
							<font style="background:#ff99ff;">[Split <%=VSPLIT_VALUE.elementAt(i)%>%]</font>
						<%} %>
					<%} %>	
				</td>
				<td align="center"><%=VBU_NM.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_TYPE.elementAt(i)%>
				 <span style="background-color: <%if(VTYPE_FLAG.elementAt(i).equals("FFLOW")){ %>#ffe6e6<%}else if(VTYPE_FLAG.elementAt(i).equals("PG")){%>#b3f0ff<%}else if(VTYPE_FLAG.elementAt(i).equals("SG")){%>#ffff66<%}%>;">
				 	<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>[<%=VTYPE_FLAG.elementAt(i) %>]<%} %>
				 </span>
				 </td>
				<td align="left"><%=VINVOICE_NO.elementAt(i)%> <%if(!VLINKED_INV_REF.elementAt(i).equals("")){%>(Ref:<%=VLINKED_INV_REF.elementAt(i)%>)<%} %></td>
				<td align="center"><%=VDIS_REMITTANCE_NO.elementAt(i) %>
				<td align="center" style="background: <%=VCOLOR.elementAt(i)%>"><%=VINV_STATUS.elementAt(i) %></td>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<td align="center"><%=VFREQ_NM.elementAt(i)%></td>
				<%} %>
				<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
				<td align="right"><%=VALLOC_QTY.elementAt(i)%> <%=VQTY_UNIT.elementAt(i) %></td>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
				<td align="right"><%=VEXCHNAGE_RATE.elementAt(i)%></td>
				<td align="center"><%=VTAX_STRUCT_DTL.elementAt(i)%></td>
				<%} %>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<td align="right"><%=VSALES_PRICE.elementAt(i)%>&nbsp;<%=VRATE_NM.elementAt(i)%></td>
				<%} %>
				<td align="right"><%=VGROSS_AMT_USD.elementAt(i)%></td>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<td align="right"><%=VTRANSPORT_AMT_USD.elementAt(i)%></td>
				<td align="right"><%=VOTHER_CHARGE_AMT_USD.elementAt(i)%></td>
				<td align="right"><%=VMARKET_AMT_USD.elementAt(i)%></td>
				<%} %>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<td align="right"><%=VTAX_AMT_USD.elementAt(i) %></td>
				<%} %>
				<td align="right"><%=VINVOICE_AMT_USD.elementAt(i)%></td>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<td align="right"><%=VTCS_AMT_USD.elementAt(i) %></td>
					<td align="right"><%=VTDS_AMT_USD.elementAt(i) %></td>
					<td align="right"><%=VADJ_SIGN_USD.elementAt(i)%><%=VADJ_AMT_USD.elementAt(i)%></td>
				<%} %>
				<td align="right"><%=VNET_PAYABLE_USD.elementAt(i)%></td>
				<%-- <td align="right"><%=VSALES_PRICE.elementAt(i)%> --%><!-- &nbsp; --><%//=VRATE_NM.elementAt(i)%></td>
				<td align="right"><%=VGROSS_AMT.elementAt(i)%></td>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<td align="right"><%=VTRANSPORT_AMT.elementAt(i)%></td>
				<td align="right"><%=VOTHER_CHARGE_AMT.elementAt(i)%></td>
				<td align="right"><%=VMARKET_AMT.elementAt(i)%></td>
				<%} %>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<td align="right"><%=VTAX_AMT.elementAt(i) %></td>
				<%} %>
				<td align="right"><%=VINVOICE_AMT.elementAt(i)%></td>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<td align="right"><%=VTCS_AMT.elementAt(i) %></td>
					<td align="right"><%=VTDS_AMT.elementAt(i) %></td>
					<td align="right"><%=VADJ_SIGN.elementAt(i)%><%=VADJ_AMT.elementAt(i)%></td>
				<%} %>
				<td align="right"><%=VNET_PAYABLE.elementAt(i)%></td>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<td align="center"><%=VPAY_STATUS.elementAt(i) %></td>
				<td align="right"><%=VPAY_RECV_AMT.elementAt(i) %></td>
				<%} %>
			</tr>
			<%
				if(k==index)
				{%>
				
				<tr>
					<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
						<td colspan="13" align="right"><b>Total :</b></td>
					<%}else{ %>
						<td colspan="14" align="right"><b>Total :</b></td>
					<%} %>
					<td align="right"><b><%=VTOTAL_ALLOC_QTY.elementAt(j) %></b></td>
					<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
						<td colspan="1"></td>
					<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
						<td colspan="2"></td>
					<%}else{ %>
						<td colspan="3"></td>
					<%} %>
					<td align="right"><b><%=VTOTAL_GROSS_USD.elementAt(j) %></b></td>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<td colspan="3"></td>
					<%} %>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
						<td colspan="1"></td>
					<%} %>
					<td align="right"><b><%=VTOTAL_INV_USD.elementAt(j) %></b></td>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
						<td colspan="3"></td>
					<%} %>
					<td align="right"><b><%=VTOTAL_NET_PAY_USD.elementAt(j) %></b></td>
					<td align="right"><b><%=VTOTAL_GROSS_INR.elementAt(j) %></b></td>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<td colspan="3"></td>
					<%} %>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
						<td colspan="1"></td>
					<%} %>
					<td align="right"><b><%=VTOTAL_INV_INR.elementAt(j) %></b></td>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
						<td colspan="3"></td>
					<%} %>
					<td align="right"><b><%=VTOTAL_NET_PAY_INR.elementAt(j) %></b></td>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<td colspan="2"></td>
					<%} %>
				</tr>
					<% i=i+1;
					break;
				}
			} %>
		<%}else{ %>
			<tr>
				<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K")){%>
					<td colspan="32" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Month!</b>") %></td>
				<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<td colspan="30" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Month!</b>") %></td>
				<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<td colspan="21" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Month!</b>") %></td>
				<%}else{ %>
					<td colspan="40" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Month!</b>") %></td>
				<%} %>
			</tr>
		<%} %>
		</tbody>
	</table>
	<div class="row">
			<div class="col-sm-12 col-xs-12 col-md-12">
				&nbsp;
			</div>
		</div> 
	<%} %>
				


</body>

</html>