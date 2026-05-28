<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="dlngInv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String chk=request.getParameter("chk")==null?"F":request.getParameter("chk");		// SagarB20251006 Added for check variable
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
dlngInv.setCallFlag("DLNG_INVOICE_FO_APPROVAL");
dlngInv.setComp_cd(owner_cd);
dlngInv.setFrom_dt(from_dt);
dlngInv.setTo_dt(to_dt);
dlngInv.setSegment(segment);
dlngInv.setChk(chk);	// SagarB20251006 Added for Checkbox
dlngInv.init();

Vector VSEGMENT = dlngInv.getVSEGMENT();
Vector VSEGMENT_TYPE = dlngInv.getVSEGMENT_TYPE();
Vector VPLANT_ABBR = dlngInv.getVPLANT_ABBR();
Vector VPLANT_SEQ = dlngInv.getVPLANT_SEQ();
Vector VBU_NM = dlngInv.getVBU_NM();
Vector VTRUCK_CD = dlngInv.getVTRUCK_CD();
Vector VTRUCK_NO = dlngInv.getVTRUCK_NO();
Vector VCOUNTERPARTY_CD = dlngInv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dlngInv.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dlngInv.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = dlngInv.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlngInv.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlngInv.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = dlngInv.getVCONT_NO();
Vector VCONT_REV_NO = dlngInv.getVCONT_REV_NO();
Vector VAGMT_NO = dlngInv.getVAGMT_NO();
Vector VAGMT_REV_NO = dlngInv.getVAGMT_REV_NO();
Vector VDIS_CONT_MAPPING = dlngInv.getVDIS_CONT_MAPPING();
Vector VCONTRACT_TYPE = dlngInv.getVCONTRACT_TYPE();
Vector VPERIOD_START_DT = dlngInv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = dlngInv.getVPERIOD_END_DT();
Vector VFINANCIAL_YEAR = dlngInv.getVFINANCIAL_YEAR();
Vector VINVOICE_NO = dlngInv.getVINVOICE_NO();
Vector VINVOICE_SEQ = dlngInv.getVINVOICE_SEQ();
Vector VBU_STATE_TIN = dlngInv.getVBU_STATE_TIN();
Vector VINVOICE_DT = dlngInv.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = dlngInv.getVINVOICE_DUE_DT();
Vector VSALES_PRICE = dlngInv.getVSALES_PRICE();
Vector VSALES_PRICE_CD = dlngInv.getVSALES_PRICE_CD();
Vector VSALES_PRICE_NM = dlngInv.getVSALES_PRICE_NM();
Vector VGROSS_AMT = dlngInv.getVGROSS_AMT();
Vector VTAX_AMT = dlngInv.getVTAX_AMT();
Vector VINVOICE_AMT = dlngInv.getVINVOICE_AMT();
Vector VNET_PAYABLE_AMT = dlngInv.getVNET_PAYABLE_AMT();
Vector VTCS_AMT = dlngInv.getVTCS_AMT();
Vector VTDS_GROSS_PERCENT = dlngInv.getVTDS_GROSS_PERCENT();
Vector VTDS_GROSS_AMT = dlngInv.getVTDS_GROSS_AMT();
Vector VTDS_TAX_PERCENT = dlngInv.getVTDS_TAX_PERCENT();
Vector VTDS_TAX_AMT = dlngInv.getVTDS_TAX_AMT();
Vector VPAY_RECV_AMT = dlngInv.getVPAY_RECV_AMT();
Vector VPAY_RECV_DT = dlngInv.getVPAY_RECV_DT();
Vector VTAX_STRUCT_DTL = dlngInv.getVTAX_STRUCT_DTL();
Vector VTDS_TCS_FLAG = dlngInv.getVTDS_TCS_FLAG();
Vector VSHORT_RECEIVED = dlngInv.getVSHORT_RECEIVED();
Vector VSAP_APPROVAL_FLAG = dlngInv.getVSAP_APPROVAL_FLAG();
Vector VTYPE_FLAG = dlngInv.getVTYPE_FLAG();

Vector VTCS_TDS = dlngInv.getVTCS_TDS();
Vector VALLOC_QTY = dlngInv.getVALLOC_QTY();
Vector VINVOICE_RAISED_IN = dlngInv.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = dlngInv.getVPAYMENT_DONE_IN();
Vector VSALES_AMT = dlngInv.getVSALES_AMT();
Vector VINVOICE_TYPE=dlngInv.getVINVOICE_TYPE();
Vector VCONT_REF_NO= dlngInv.getVCONT_REF_NO();
Vector VCASH_FLOW= dlngInv.getVCASH_FLOW();

Vector VFIN_SYS = dlngInv.getVFIN_SYS();//PB 20250627 for Sun
Vector VALLOC_MT = dlngInv.getVALLOC_MT();// SagarB20251006 for MT
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="29" rowspan="2" align="left">DLNG Sales Actual Report (<%=from_dt%> - <%=to_dt%>) <%if(chk.equals("T")){ %> For All Invoices<%} %> </th>		<!--  SagarB20251006 for all invoices -->
		</tr>
	</table>
	<table width="100%" border="1">
		<tr>
			<th >SR#</th>
			<!-- <th >Truck No</th> -->
			<th >Customer</th>
			<th >Plant</th>
			<th ><%=owner_abbr%> BU</th>
			<th >Contract#</th>
			<th >Contract/Trade Ref#</th>
			<th >Cash Flow</th>
			<th >Invoice#</th>
			<th >Invoice Date</th>
			<th >Invoiced MMBTU</th>
			<th >Invoiced MT</th>		<!-- SagarB20251006 Added MT Column	 -->
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
			<th >Billing Period</th>
			<th >Invoice Due Date</th>
			<!-- <th rowspan="2">SAP Approval</th> -->
		</tr>
		<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
		<tr>
			<td align="center"><%=i+1%></td>
			<%-- <td align="center"><%=VTRUCK_NO.elementAt(i)%></td> --%>
			<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%>">
				<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
			</td>
			<td align="center"><%=VPLANT_ABBR.elementAt(i) %></td>
			<td align="center"><%=VBU_NM.elementAt(i) %></td>
			<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i) %></td>
			<td align="center"><%=VCONT_REF_NO.elementAt(i) %></td>
			<td align="center"><%=VCASH_FLOW.elementAt(i)%></td>
			<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
			<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
			<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
			<td align="right"><%=VALLOC_MT.elementAt(i) %></td>		<!-- SagarB20251006 Added MT data -->
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
			<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
			<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>	
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