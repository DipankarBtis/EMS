<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
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
String pay_status=request.getParameter("pay_status")==null?"unpaid":request.getParameter("pay_status");

if(month.length() == 1)
{
	month="0"+month; 
}
if(month_to.length() == 1)
{
	month_to="0"+month_to; 
}

purchase.setCallFlag("PAYABLE_TRACKING");
purchase.setComp_cd(owner_cd);
purchase.setMonth(month);
purchase.setYear(year);
purchase.setMonth_to(month_to);
purchase.setYear_to(year_to);
purchase.setPay_status(pay_status);
purchase.init();

Vector VSEGMENT = purchase.getVSEGMENT();
Vector VSEGMENT_TYPE = purchase.getVSEGMENT_TYPE();

Vector VCOUNTERPTY_CD = purchase.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = purchase.getVCOUNTERPTY_ABBR();
Vector VMST_COUNTERPARTY_CD = purchase.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = purchase.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = purchase.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = purchase.getVCONT_NO();
Vector VCONT_REV_NO = purchase.getVCONT_REV_NO();
Vector VAGMT_NO = purchase.getVAGMT_NO();
Vector VAGMT_REV_NO = purchase.getVAGMT_REV_NO();
Vector VDIS_CONT_MAPPING = purchase.getVDIS_CONT_MAPPING();
Vector VCONTRACT_TYPE = purchase.getVCONTRACT_TYPE();
Vector VPERIOD_START_DT = purchase.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = purchase.getVPERIOD_END_DT();
Vector VFINANCIAL_YEAR = purchase.getVFINANCIAL_YEAR();
Vector VINVOICE_NO = purchase.getVINVOICE_NO();
Vector VINVOICE_SEQ = purchase.getVINVOICE_SEQ();
Vector VBU_UNIT = purchase.getVBU_UNIT();
Vector VINVOICE_DT = purchase.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = purchase.getVINVOICE_DUE_DT();
Vector VSALES_PRICE = purchase.getVSALES_PRICE();
Vector VSALES_PRICE_CD = purchase.getVSALES_PRICE_CD();
Vector VSALES_PRICE_NM = purchase.getVSALES_PRICE_NM();
Vector VGROSS_AMT = purchase.getVGROSS_AMT();
Vector VTAX_AMT = purchase.getVTAX_AMT();
Vector VINVOICE_AMT = purchase.getVINVOICE_AMT();
Vector VNET_PAYABLE = purchase.getVNET_PAYABLE();
Vector VTCS_AMT = purchase.getVTCS_AMT();
Vector VTCS_FACTOR = purchase.getVTCS_FACTOR();
Vector VTDS_AMT = purchase.getVTDS_AMT();
Vector VTDS_FACTOR = purchase.getVTDS_FACTOR();
Vector VPAY_RECV_AMT = purchase.getVPAY_RECV_AMT();
Vector VPAY_RECV_DT = purchase.getVPAY_RECV_DT();
Vector VTAX_STRUCT_DTL = purchase.getVTAX_STRUCT_DTL();
Vector VSHORT_RECEIVED = purchase.getVSHORT_RECEIVED();
Vector VPAY_RECV_HISTORY = purchase.getVPAY_RECV_HISTORY();
Vector VINVOICE_RAISED_IN = purchase.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = purchase.getVPAYMENT_DONE_IN();
Vector VINVOICE_TYPE = purchase.getVINVOICE_TYPE();
Vector VINDEX = purchase.getVINDEX();
Vector VINV_FLG = purchase.getVINV_FLG();
Vector VRATE_NM = purchase.getVRATE_NM();
Vector VINV_RAISED_IN = purchase.getVINV_RAISED_IN();
Vector VREMITTANCE_NO = purchase.getVREMITTANCE_NO();
Vector VDIS_REMITTANCE_NO = purchase.getVDIS_REMITTANCE_NO();
Vector VPAY_COLOR = purchase.getVPAY_COLOR();

Vector VGROSS_AMT_USD = purchase.getVGROSS_AMT_USD();
Vector VTAX_AMT_USD = purchase.getVTAX_AMT_USD();					
Vector VINVOICE_AMT_USD = purchase.getVINVOICE_AMT_USD();
Vector VNET_PAYABLE_USD = purchase.getVNET_PAYABLE_USD();
Vector VTCS_AMT_USD = purchase.getVTCS_AMT_USD();
Vector VTDS_AMT_USD = purchase.getVTDS_AMT_USD();
Vector VINVOICE_FLAG = purchase.getVINVOICE_FLAG();
%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

<table  width="100%" border="1">
	<tr>
		<th colspan="31" rowspan="2" align="left">Payable Tracking From <%=month %>/<%=year %> To <%=month_to %>/<%=year_to %></th>
	</tr>
</table >


<%int i=0;int k=0;
	for(int j=0; j<VSEGMENT_TYPE.size(); j++){ 
		int index = Integer.parseInt(""+VINDEX.elementAt(j));
	if(j!=0)
	{%>
	<%} %>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th align="left" colspan="31"><%=VSEGMENT.elementAt(j) %></th>
			</tr>
	
					<tr>
						<th rowspan="2">Trader</th>
						<th rowspan="2">Contract#</th>
						<th rowspan="2">Billing Period</th>
						<th rowspan="2">Remittance#<br>[Invoice Type]</th>
						<th rowspan="2">Invoice#</th>
						<th rowspan="2">Invoice Date</th>
						<th rowspan="2">Purchase Rate</th>
						<th rowspan="2" style="background: #000066; color: white;">Rate Unit/ MMBTU</th>
						<th rowspan="2" style="background: #000066; color: white;">Invoice Raised In</th>	
						<th colspan="2">Gross Amount</th>
						<th rowspan="2">Tax Structure</th>
						<th colspan="2">Tax Amount</th>
						<th colspan="2">Invoice Amount</th>
						<th colspan="3">TCS</th>
						<th colspan="3">TDS</th>
						<th colspan="2">Net Payable</th>
						<th rowspan="2">Invoice Due Date</th>
						<th rowspan="2" style="background: #000066; color: white;">Invoice Paid In</th>
						<th rowspan="2">Total Paid</th>										
						<th rowspan="2">Short Paid</th>
						<th rowspan="2">Actual Paid</th>
						<th rowspan="2">Payment Date</th>
						<th rowspan="2">Remark</th>
					</tr>
					<tr>
						<th>USD</th>
						<th>INR</th>
						<th>USD</th>
						<th>INR</th>
						<th>USD</th>
						<th>INR</th>
						<th>TCS(%)</th>
						<th>TCS(USD)</th>
						<th>TCS(INR)</th>
						<th>TDS(%)</th>
						<th>TDS(USD)</th>
						<th>TDS(INR)</th>
						<th>USD</th>
						<th>INR</th>
					</tr>
				</thead>
				<tbody>
				<%k=0;
				if(index > 0){ %>
					<%for(i=i; i<VCOUNTERPTY_CD.size(); i++){ 
						k+=1; %>
					<tr>
						<td align="center"><%=VCOUNTERPTY_ABBR.elementAt(i)%></td>
						<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i) %></td>
						<td align="center"><%=VPERIOD_START_DT.elementAt(i) %>-<%=VPERIOD_END_DT.elementAt(i) %></td>
						<td align="center"><%=VDIS_REMITTANCE_NO.elementAt(i)%></td>
						<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
						<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
						<td align="right"><%=VSALES_PRICE.elementAt(i) %></td>
						<td align="center" style="background: #b3f0ff;"><%=VSALES_PRICE_NM.elementAt(i) %></td>
						<td align="center" style="background: #b3f0ff;"><%=VINV_RAISED_IN.elementAt(i) %></td>
						<td align="right"><%=VGROSS_AMT_USD.elementAt(i) %></td>
						<td align="right"><%=VGROSS_AMT.elementAt(i) %></td>
						<td align="center"><%=VTAX_STRUCT_DTL.elementAt(i) %></td>
						<td align="right"><%=VTAX_AMT_USD.elementAt(i) %></td>
						<td align="right"><%=VTAX_AMT.elementAt(i) %></td>
						<td align="right"><%=VINVOICE_AMT_USD.elementAt(i) %></td>
						<td align="right"><%=VINVOICE_AMT.elementAt(i) %></td>
						<td align="right"><%=VTCS_FACTOR.elementAt(i) %></td>
						<td align="right"><%=VTCS_AMT_USD.elementAt(i) %></td>
						<td align="right"><%=VTCS_AMT.elementAt(i) %></td>
						<td align="right"><%=VTDS_FACTOR.elementAt(i) %></td>
						<td align="right"><%=VTDS_AMT_USD.elementAt(i) %></td>
						<td align="right"><%=VTDS_AMT.elementAt(i) %></td>
						<td align="right"><%=VNET_PAYABLE_USD.elementAt(i) %></td>
						<td align="right"><%=VNET_PAYABLE.elementAt(i) %></td>
						<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
						<td align="center"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
						<td align="right" style="background: <%=VPAY_COLOR.elementAt(i)%>"><%=VPAY_RECV_AMT.elementAt(i)%></td>		
						<td align="right"><%=VSHORT_RECEIVED.elementAt(i)%></td>
						<td align="center"></td>
						<td align="center"></td>
						<td align="center"></td>
					</tr>
					<%
						if(k==index)
						{
							i=i+1;
							break;
						}
					} %>
				<%} else {%>
					<tr>
						<td colspan="31">
							<div align="center"><%=utilmsg.infoMessage("<b>No Remittance generated for Report Period!</b>")%></div>
						</td>
					</tr>
				<%} %>
				</tbody>
			</table>
	<%} %>
</body>
</html>