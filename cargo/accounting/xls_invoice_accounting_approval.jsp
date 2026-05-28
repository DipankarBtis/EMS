<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.accounting.DataBean_Accounting" id="accounting" scope="request"></jsp:useBean>
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

accounting.setCallFlag("INVOICE_FO_APPROVAL");
accounting.setComp_cd(owner_cd);
accounting.setFrom_dt(from_dt);
accounting.setTo_dt(to_dt);
accounting.setSegment(segment);
accounting.init();

Vector VSEGMENT = accounting.getVSEGMENT();
Vector VSEGMENT_TYPE = accounting.getVSEGMENT_TYPE();
Vector VBU_NM = accounting.getVBU_NM();
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
Vector VTDS_GROSS_PERCENT = accounting.getVTDS_GROSS_PERCENT();
Vector VTDS_GROSS_AMT = accounting.getVTDS_GROSS_AMT();
Vector VTDS_TAX_PERCENT = accounting.getVTDS_TAX_PERCENT();
Vector VTDS_TAX_AMT = accounting.getVTDS_TAX_AMT();
Vector VPAY_RECV_AMT = accounting.getVPAY_RECV_AMT();
Vector VPAY_RECV_DT = accounting.getVPAY_RECV_DT();
Vector VTAX_STRUCT_DTL = accounting.getVTAX_STRUCT_DTL();
Vector VTDS_TCS_FLAG = accounting.getVTDS_TCS_FLAG();
Vector VSHORT_RECEIVED = accounting.getVSHORT_RECEIVED();
Vector VSAP_APPROVAL_FLAG = accounting.getVSAP_APPROVAL_FLAG();
Vector VTYPE_FLAG = accounting.getVTYPE_FLAG();

Vector VTCS_TDS = accounting.getVTCS_TDS();
Vector VALLOC_QTY = accounting.getVALLOC_QTY();
Vector VINVOICE_RAISED_IN = accounting.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = accounting.getVPAYMENT_DONE_IN();
Vector VSALES_AMT = accounting.getVSALES_AMT();
Vector VINVOICE_TYPE=accounting.getVINVOICE_TYPE();
Vector VCONT_REF_NO= accounting.getVCONT_REF_NO();
Vector VCASH_FLOW= accounting.getVCASH_FLOW();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename=SalesActualsAccountingReport.xls");
%>
<span style="font-weight: bold;font-size: 40px;">Sales Invoice (FO) Approval</span>
<br><br>
<table width="100%" border="1">
	<tr>
		<th >SR#</th>
		<th >Customer</th>
		<th ><%=owner_abbr%> BU</th>
		<th >Contract#</th>
		<th >Contract/Trade Ref#</th>
		<th >Billing Period</th>
		<th >Cash Flow</th>
		<th >Invoice#</th>
		<th >Invoice Date</th>
		<th >Invoice Due Date</th>
		<th >Invoiced MMBTU</th>
		<th >Rate</th>
		<th >Rate Unit / MMBTU</th>
		<th >Sales Amount</th>
		<th >Invoice Raised In</th>
		<th >Invoice Paid In</th>
		<th >Gross Amount</th>
		<th >Tax </th>
		<th >Invoice Amount</th>
		<th >TCS/TDS</th>
		<th >TCS/TDS%</th>
		<th >+TCS/-TDS Amount</th>
		<th >Net Receivable</th>
		<!-- <th rowspan="2">SAP Approval</th> -->
	</tr>
	<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
	<tr>
		<td align="center"><%=i+1%></td>
		<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%>">
			<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
		</td>
		<td align="center"><%=VBU_NM.elementAt(i) %></td>
		<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i) %></td>
		<td align="center"><%=VCONT_REF_NO.elementAt(i) %></td>
		<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
		<td align="center"><%=VCASH_FLOW.elementAt(i)%></td>
		<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
		<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
		<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
		<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
		<td align="right"><%=VSALES_PRICE.elementAt(i)%></td>
		<td align="center"><%=VSALES_PRICE_NM.elementAt(i) %></td>
		<td align="right"><%=VSALES_AMT.elementAt(i)%></td>
		<td align="center"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>
		<td align="center"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
		<td> <%=VGROSS_AMT.elementAt(i)%></td>
		<td align="right"><%=VTAX_AMT.elementAt(i)%></td>
		<td align="right"><%=VINVOICE_AMT.elementAt(i) %></td>
		<td><%=VTCS_TDS.elementAt(i) %></td>
		<td><%=VTDS_GROSS_PERCENT.elementAt(i) %></td>
		<td><%=VTDS_GROSS_AMT.elementAt(i) %></td>										
		<td align="right"><%=VNET_PAYABLE_AMT.elementAt(i)%></td>	
		<%-- <td align="center">
			<%if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
			Approved
			<%}else{%>
			Pending Approval
			<%} %>			
		</td>	 --%>
	</tr>
	<%} %>
</table>
						
</body>
</html>