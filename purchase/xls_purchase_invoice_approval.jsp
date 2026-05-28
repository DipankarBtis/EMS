<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sysdate = utildate.getSysdate();

String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");

String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

String owner_abbr="";
if(session.getAttribute("comp_abbr")==null||session.getAttribute("comp_abbr")==""||session.getAttribute("comp_abbr").toString().equals("null"))
{
	owner_abbr="";
}  
else
{
	owner_abbr=""+session.getAttribute("comp_abbr");
}

purchase.setCallFlag("PURCHASE_INVOICE_APPROVAL");
purchase.setComp_cd(owner_cd);
purchase.setSegment(segment);
purchase.setFrom_dt(from_dt);
purchase.setTo_dt(to_dt);
purchase.init();

Vector VSEGMENT = purchase.getVSEGMENT();
Vector VSEGMENT_TYPE = purchase.getVSEGMENT_TYPE();

Vector VCONT_NO = purchase.getVCONT_NO();
Vector VINVOICE_DT = purchase.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = purchase.getVINVOICE_DUE_DT();
Vector VINVOICE_NO = purchase.getVINVOICE_NO();
Vector VINVOICE_SEQ = purchase.getVINVOICE_SEQ();
Vector VPERIOD_START_DT = purchase.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = purchase.getVPERIOD_END_DT();
Vector VALLOC_QTY = purchase.getVALLOC_QTY();
Vector VSALES_PRICE = purchase.getVSALES_PRICE();
Vector VSALES_PRICE_UNIT = purchase.getVSALES_PRICE_UNIT();
Vector VGROSS_AMT = purchase.getVGROSS_AMT();
Vector VTAX_AMT = purchase.getVTAX_AMT();
Vector VINVOICE_AMT = purchase.getVINVOICE_AMT();
Vector VEXCHNAGE_RATE = purchase.getVEXCHNAGE_RATE();
Vector VEXCHNAGE_RATE_DATE = purchase.getVEXCHNAGE_RATE_DATE();
Vector VBU_NM = purchase.getVBU_NM();
Vector VBU_SEQ = purchase.getVBU_SEQ();
Vector VDEAL_NO = purchase.getVDEAL_NO();
Vector VSALE_AMT = purchase.getVSALE_AMT();
Vector VADJ_SIGN = purchase.getVADJ_SIGN();
Vector VADJ_AMT = purchase.getVADJ_AMT();
Vector VNET_PAYABLE = purchase.getVNET_PAYABLE();
Vector VTCS_TDS = purchase.getVTCS_TDS();
Vector VTCS_TDS_AMT = purchase.getVTCS_TDS_AMT();
Vector VTCS_TDS_FACTOR = purchase.getVTCS_TDS_FACTOR();
Vector VINVOICE_RAISED_IN = purchase.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = purchase.getVPAYMENT_DONE_IN();
Vector VFINANCIAL_YEAR = purchase.getVFINANCIAL_YEAR();
Vector VCONTRACT_TYPE = purchase.getVCONTRACT_TYPE();
Vector VSAP_EXCHANG_FLAG = purchase.getVSAP_EXCHANG_FLAG();
Vector VSAP_APPROVAL_FLAG = purchase.getVSAP_APPROVAL_FLAG();

Vector VINVOICE_TYPE =purchase.getVINVOICE_TYPE();
Vector VPAYMENT_TYPE_FLAG = purchase.getVPAYMENT_TYPE_FLAG();
Vector VPAYMENT_TYPE_NM= purchase.getVPAYMENT_TYPE_NM();

Vector VGROSS_AMT_USD = purchase.getVGROSS_AMT_USD();
Vector VTAX_AMT_USD = purchase.getVTAX_AMT_USD();
Vector VINVOICE_AMT_USD = purchase.getVINVOICE_AMT_USD();
Vector VADJ_AMT_USD = purchase.getVADJ_AMT_USD();
Vector VNET_PAYABLE_USD = purchase.getVNET_PAYABLE_USD();
Vector VTCS_TDS_AMT_USD = purchase.getVTCS_TDS_AMT_USD();
Vector VTCS_TDS_STRUCT_CD = purchase.getVTCS_TDS_STRUCT_CD();
Vector VTCS_TDS_EFF_DT = purchase.getVTCS_TDS_EFF_DT();
Vector VTCS_TDS_DONE = purchase.getVTCS_TDS_DONE();

Vector VCOUNTERPTY_CD = purchase.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = purchase.getVCOUNTERPTY_ABBR();
Vector VINDEX = purchase.getVINDEX();
Vector VCASH_FLOW = purchase.getVCASH_FLOW();
Vector VDIS_REMITTANCE_NO = purchase.getVDIS_REMITTANCE_NO();
Vector VQTY_UNIT = purchase.getVQTY_UNIT();

String fileName ="PurchaseActualsAccountingReport.xls";
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
<table  width="100%" border="1">
	<tr>
		<th colspan="33" rowspan="2" align="left">Purchase Remittance (FO) Approval</th>
	</tr>
</table >
<%int i=0;int k=0;
for(int j=0; j<VSEGMENT_TYPE.size(); j++){ 
	int index = Integer.parseInt(""+VINDEX.elementAt(j));
%>	
<%if(j!=0){ %>
<br>
<%} %>
<span style="font-weight: bold;font-size: 24px;"><%=VSEGMENT.elementAt(j) %></span>
<table width="100%" border="1">
	<thead>
		<tr >
			<th rowspan="2">Type</th>
			<th rowspan="2">Trader</th>
			<th rowspan="2"><%=owner_abbr%> BU</th>
			<th rowspan="2"><%if(VSEGMENT_TYPE.elementAt(j).equals("N") || VSEGMENT_TYPE.elementAt(j).equals("CDF") || VSEGMENT_TYPE.elementAt(j).equals("CDP")){ %>Cargo#<%}else{ %>Contract#<%} %></th>
			<th rowspan="2">Billing Period</th>
			<th rowspan="2">Cash Flow</th> 
			<th rowspan="2">Invoice#</th>
			<th rowspan="2">Remittance#</th>
			<th rowspan="2">Invoice Date</th>
			<th rowspan="2">Invoice Due Date</th>
			<th rowspan="2">Invoiced Quantity</th>
			<th rowspan="2">Rate</th>
			<th rowspan="2" >Rate Unit</th>
			<th rowspan="2">Sales Amount</th>
			<th rowspan="2" >Invoice Raised In</th>
			<th rowspan="2">Exchange Rate Date</th>
			<th rowspan="2">Exchange Rate</th>
			<th rowspan="2" >Invoice Paid In</th>
			<th colspan="2">Gross Amount</th>
			<th colspan="2">Tax</th>
			<th colspan="2">Invoice Amount</th>
			<th rowspan="2">TCS/TDS</th>
			<th rowspan="2">TCS/TDS %</th>
			<th colspan="2">+TCS/-TDS Amount</th>
			<th colspan="2">Adjust Amount</th>
			<th colspan="2">Net Payable</th>
			<th rowspan="2">SAP Approval</th>
		</tr>
		<tr>
			<th>USD</th>
			<th>INR</th>
			<th>USD</th>
			<th>INR</th>
			<th>USD</th>
			<th>INR</th>
			<th>USD</th>
			<th>INR</th>
			<th>USD</th>
			<th>INR</th>
			<th>USD</th>
			<th>INR</th>										
		</tr>
	</thead>
	<tbody>
	<%k=0;
	if(index > 0){ %>
		<%for(i=i; i<VINVOICE_SEQ.size(); i++){ 
			k+=1;
		%>
		<tr>
			<td align="center"><%=VPAYMENT_TYPE_NM.elementAt(i) %></td>
			<td align="center"><%=VCOUNTERPTY_ABBR.elementAt(i) %></td>
			<td align="center"><%=VBU_NM.elementAt(i) %></td>
			<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
			<td align="center"><%=VPERIOD_START_DT.elementAt(i) %> - <%=VPERIOD_END_DT.elementAt(i) %></td>
			<td align="center"><%=VCASH_FLOW.elementAt(i) %></td>
			<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
			<td align="center"><%=VDIS_REMITTANCE_NO.elementAt(i) %></td>
			<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
			<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
			<td align="right"><%=VALLOC_QTY.elementAt(i) %> <%=VQTY_UNIT.elementAt(i) %></td>
			<td align="right"><%=VSALES_PRICE.elementAt(i) %></td>
			<td align="center" ><%=VSALES_PRICE_UNIT.elementAt(i) %></td>
			<td align="right"><%=VSALE_AMT.elementAt(i) %></td>	
			<td align="center" ><%=VINVOICE_RAISED_IN.elementAt(i) %></td>																													
			<td align="center"><%=VEXCHNAGE_RATE_DATE.elementAt(i) %></td>
			<td align="center"><%=VEXCHNAGE_RATE.elementAt(i) %></td>
			<td align="center" ><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
			<td align="right"><%=VGROSS_AMT_USD.elementAt(i) %></td>
			<td align="right"><%=VGROSS_AMT.elementAt(i) %></td>
			<td align="right"><%=VTAX_AMT_USD.elementAt(i) %></td>
			<td align="right"><%=VTAX_AMT.elementAt(i) %></td>
			<td align="right"><%=VINVOICE_AMT_USD.elementAt(i) %></td>
			<td align="right"><%=VINVOICE_AMT.elementAt(i) %></td>
			<td align="center"><%=VTCS_TDS.elementAt(i) %></td>
			<td align="center"><%=VTCS_TDS_FACTOR.elementAt(i)%></td>
			<td align="right"><%=VTCS_TDS_AMT_USD.elementAt(i) %></td>										
			<td align="right"><%=VTCS_TDS_AMT.elementAt(i) %></td>
			<td align="right"><%=VADJ_SIGN.elementAt(i)%><%=VADJ_AMT_USD.elementAt(i)%></td>										
			<td align="right"><%=VADJ_SIGN.elementAt(i)%><%=VADJ_AMT.elementAt(i)%></td>
			<td align="right"><%=VNET_PAYABLE_USD.elementAt(i)%></td>
			<td align="right"><%=VNET_PAYABLE.elementAt(i)%></td>
			<td align="center">											
				<%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
					Pending Approval
				<%}else if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
					Approved
				<%} %>	 
			</td>
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
			<td colspan="33" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
		</tr>
	<%} %>
	</tbody>
</table>
	
<%} %>
</body>	
</html>