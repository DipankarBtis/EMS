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
String paid_status=request.getParameter("paid_status")==null?"U":request.getParameter("paid_status");
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

accounting.setCallFlag("RECEIVABLE_TRACKING");
accounting.setComp_cd(owner_cd);
accounting.setMonth(month);
accounting.setYear(year);
accounting.setMonth_to(month_to);
accounting.setYear_to(year_to);
accounting.setPaid_status(paid_status);
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
Vector VCARGO_NO = accounting.getVCARGO_NO();
Vector VDIS_CONT_MAPPING = accounting.getVDIS_CONT_MAPPING();
Vector VCONTRACT_TYPE = accounting.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = accounting.getVCONTRACT_TYPE_NM();
Vector VPERIOD_START_DT = accounting.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = accounting.getVPERIOD_END_DT();
Vector VFINANCIAL_YEAR = accounting.getVFINANCIAL_YEAR();
Vector VINVOICE_NO = accounting.getVINVOICE_NO();
Vector VINVOICE_SEQ = accounting.getVINVOICE_SEQ();
Vector VBU_STATE_TIN = accounting.getVBU_STATE_TIN();
Vector VINVOICE_DT = accounting.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = accounting.getVINVOICE_DUE_DT();
Vector VSALES_PRICE = accounting.getVSALES_PRICE();
Vector VSALES_PRICE_CD = accounting.getVSALES_PRICE_CD();
Vector VSALES_PRICE_NM = accounting.getVSALES_PRICE_NM();
Vector VGROSS_AMT = accounting.getVGROSS_AMT();
Vector VTAX_AMT = accounting.getVTAX_AMT();
Vector VINVOICE_AMT = accounting.getVINVOICE_AMT();
Vector VNET_PAYABLE_AMT = accounting.getVNET_PAYABLE_AMT();
Vector VTCS_AMT = accounting.getVTCS_AMT();
Vector VTCS_FACTOR = accounting.getVTCS_FACTOR();
Vector VTDS_GROSS_PERCENT = accounting.getVTDS_GROSS_PERCENT();
Vector VTDS_GROSS_AMT = accounting.getVTDS_GROSS_AMT();
Vector VTDS_TAX_PERCENT = accounting.getVTDS_TAX_PERCENT();
Vector VTDS_TAX_AMT = accounting.getVTDS_TAX_AMT();
Vector VPAY_RECV_AMT = accounting.getVPAY_RECV_AMT();
Vector VPAY_RECV_DT = accounting.getVPAY_RECV_DT();
Vector VTAX_STRUCT_DTL = accounting.getVTAX_STRUCT_DTL();
Vector VTDS_TCS_FLAG = accounting.getVTDS_TCS_FLAG();
Vector VSHORT_RECEIVED = accounting.getVSHORT_RECEIVED();
Vector VPAY_RECV_HISTORY = accounting.getVPAY_RECV_HISTORY();
Vector VINVOICE_RAISED_IN = accounting.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = accounting.getVPAYMENT_DONE_IN();
Vector VINVOICE_TYPE = accounting.getVINVOICE_TYPE();
Vector VTYPE_FLAG = accounting.getVTYPE_FLAG();
Vector VTOTAL_ADV_AMT = accounting.getVTOTAL_ADV_AMT();
Vector VCONT_MAP = accounting.getVCONT_MAP();
Vector VCFORM_FLAG = accounting.getVCFORM_FLAG();
Vector VHOLD_AMT = accounting.getVHOLD_AMT();
Vector VRECV_DT = accounting.getVRECV_DT();
Vector VRECV_REMARK = accounting.getVRECV_REMARK();
Vector VRECV_AMT = accounting.getVRECV_AMT();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="27" rowspan="2" align="left">Receivable Tracking (<%=month%>/<%=year%> - <%=month_to%>/<%=year_to%>)</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th rowspan="2">Sr#</th>
				<th rowspan="2">
					Customer
				</th>
				<th rowspan="2">
					Contract#
				</th>
				<th rowspan="2">Billing Period</th>
				<th rowspan="2">
					Invoice#
				</th>
				<th rowspan="2">Invoice Date</th>
				<th rowspan="2">Sales Rate</th>
				<th rowspan="2" style="background: #000066; color: white;">Rate Unit/ MMBTU</th>
				<th rowspan="2" style="background: #000066; color: white;">Invoice Raised In</th>										
				<th rowspan="2">Gross Amount (INR)</th>
				<th rowspan="2">Tax Structure</th>
				<th rowspan="2">Tax Amount (INR)</th>
				<th rowspan="2">Invoice Amount (INR)</th>
				<th colspan="2">TCS (INR)</th>
				<th colspan="2">TDS (INR)</th>
				<th rowspan="2">Net Receivable</th>
				<th rowspan="2">Invoice Due Date</th>
				<th rowspan="2" style="background: #000066; color: white;">Invoice Paid In</th>
				<th rowspan="2">Adjust Advance Amount (INR)</th>
				<th rowspan="2">Hold Amount (INR)</th>
				<th rowspan="2">Total Received (INR)</th>										
				<th rowspan="2">Short Received (INR)</th>
				<th rowspan="2">Last Actual Received (INR)</th>
				<th rowspan="2">Last Received Date</th>
				<th rowspan="2">Remark</th>
			</tr>
			<tr>
				<th>TCS(%)</th>
				<th>TCS Values</th>
				<th>TDS(%)</th>
				<th>TDS Values</th>
			</tr>
		</thead>
		<tbody>
		<%if(VCOUNTERPARTY_CD.size() > 0){ %>
			<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
			<tr>
				<td>
					<%=i+1 %>
				</td>
				<td><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
				<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i).toString().replace("<br>"," ")%></td>
				<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
				<td align="right"><%=VSALES_PRICE.elementAt(i)%></td>
				<td align="center" style="background: #b3f0ff;"><%=VSALES_PRICE_NM.elementAt(i) %></td>
				<td align="center" style="background: #b3f0ff;"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>
				<td align="right">
					<%=VGROSS_AMT.elementAt(i)%>
				</td>
				<td align="center"><%=VTAX_STRUCT_DTL.elementAt(i)%></td>
				<td align="right">
					<%=VTAX_AMT.elementAt(i)%>
				</td>
				<td align="right">
					<%=VINVOICE_AMT.elementAt(i) %>
				</td>
				<td align="right"><%=VTCS_FACTOR.elementAt(i) %></td>
				<td align="right"><%=VTCS_AMT.elementAt(i) %></td>										
				<td align="center"><%=VTDS_GROSS_PERCENT.elementAt(i)%></td>
				<td align="center"><%=VTDS_GROSS_AMT.elementAt(i)%></td>										
				<td align="right"><%=VNET_PAYABLE_AMT.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
				<td align="center" style="background: #b3f0ff;"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
				<td align="right"><%if(Double.parseDouble(""+VTOTAL_ADV_AMT.elementAt(i)) <= 0){%><%=VTOTAL_ADV_AMT.elementAt(i)%><%}%></td>
				<td align="center"><%if(!VCFORM_FLAG.elementAt(i).equals("Y")){%><%=VHOLD_AMT.elementAt(i)%><%}%></td>
				<td align="center" title="<%=VPAY_RECV_HISTORY.elementAt(i)%>">
					<%=VPAY_RECV_AMT.elementAt(i)%>
				</td>										
				<td align="right">
					<%=VSHORT_RECEIVED.elementAt(i)%>
				</td>
				<td align="right"><%=VRECV_AMT.elementAt(i) %></td>
				<td align="right"><%=VRECV_DT.elementAt(i) %></td>
				<td align="right"><%=VRECV_REMARK.elementAt(i) %></td>
			</tr>
			<%} %>
		<%} else {%>
			<tr>
				<td colspan="27">
					<div align="center"><%=utilmsg.infoMessage("<b>No Invoice generated for Report Period!</b>")%></div>
				</td>
			</tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>