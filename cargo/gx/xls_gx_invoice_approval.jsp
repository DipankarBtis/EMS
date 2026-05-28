<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.gx.DataBean_Gx_Invoice" id="gx_inv" scope="request"></jsp:useBean>
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

gx_inv.setCallFlag("GX_REMITTANCE_FO_APPROVAL");
gx_inv.setComp_cd(owner_cd);
//gx_inv.setSegment(segment);
gx_inv.setFrom_dt(from_dt);
gx_inv.setTo_dt(to_dt);
gx_inv.init();

Vector VSEGMENT = gx_inv.getVSEGMENT();
Vector VSEGMENT_TYPE = gx_inv.getVSEGMENT_TYPE();

Vector VINVOICE_DT = gx_inv.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = gx_inv.getVINVOICE_DUE_DT();
Vector VINVOICE_NO = gx_inv.getVINVOICE_NO();
Vector VINVOICE_SEQ = gx_inv.getVINVOICE_SEQ();
//Vector VPERIOD_START_DT = gx_inv.getVPERIOD_START_DT();
//Vector VPERIOD_END_DT = gx_inv.getVPERIOD_END_DT();
Vector VALLOC_QTY = gx_inv.getVALLOC_QTY();
Vector VTXN_RATE = gx_inv.getVTXN_RATE();
Vector VRATE_UNIT = gx_inv.getVRATE_UNIT();
Vector VGROSS_AMT = gx_inv.getVGROSS_AMT();
Vector VTAX_AMT = gx_inv.getVTAX_AMT();
Vector VINVOICE_AMT = gx_inv.getVINVOICE_AMT();
//Vector VEXCHNAGE_RATE = gx_inv.getVEXCHNAGE_RATE();
//Vector VEXCHNAGE_RATE_DATE = gx_inv.getVEXCHNAGE_RATE_DATE();
Vector VBU_PLANT_SEQ = gx_inv.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = gx_inv.getVBU_PLANT_ABBR();
Vector VDEAL_NO = gx_inv.getVDEAL_NO();
//Vector VSALE_AMT = gx_inv.getVSALE_AMT();
Vector VADJ_SIGN = gx_inv.getVADJ_SIGN();
Vector VADJ_AMT = gx_inv.getVADJ_AMT();
Vector VNET_PAYABLE = gx_inv.getVNET_PAYABLE();
Vector VTCS_TDS = gx_inv.getVTCS_TDS();
Vector VTCS_TDS_AMT = gx_inv.getVTCS_TDS_AMT();
Vector VTCS_TDS_FACTOR = gx_inv.getVTCS_TDS_FACTOR();
Vector VINVOICE_RAISED_IN = gx_inv.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = gx_inv.getVPAYMENT_DONE_IN();
Vector VFINANCIAL_YEAR = gx_inv.getVFINANCIAL_YEAR();
Vector VCONTRACT_TYPE = gx_inv.getVCONTRACT_TYPE();
//Vector VSAP_EXCHANG_FLAG = gx_inv.getVSAP_EXCHANG_FLAG();
Vector VSAP_APPROVAL_FLAG = gx_inv.getVSAP_APPROVAL_FLAG();

Vector VINVOICE_TYPE =gx_inv.getVINVOICE_TYPE();
Vector VTYPE_FLAG = gx_inv.getVTYPE_FLAG();
Vector VTYPE_NM= gx_inv.getVTYPE_NM();
Vector VCASH_FLOW = gx_inv.getVCASH_FLOW();

/* Vector VGROSS_AMT_USD = gx_inv.getVGROSS_AMT_USD();
Vector VTAX_AMT_USD = gx_inv.getVTAX_AMT_USD();
Vector VINVOICE_AMT_USD = gx_inv.getVINVOICE_AMT_USD();
Vector VADJ_AMT_USD = gx_inv.getVADJ_AMT_USD();
Vector VNET_PAYABLE_USD = gx_inv.getVNET_PAYABLE_USD();
Vector VTCS_TDS_AMT_USD = gx_inv.getVTCS_TDS_AMT_USD(); */
Vector VTCS_TDS_STRUCT_CD = gx_inv.getVTCS_TDS_STRUCT_CD();
Vector VTCS_TDS_EFF_DT = gx_inv.getVTCS_TDS_EFF_DT();
//Vector VTCS_TDS_DONE = gx_inv.getVTCS_TDS_DONE();

Vector VCOUNTERPARTY_CD = gx_inv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = gx_inv.getVCOUNTERPARTY_ABBR();
Vector VINDEX = gx_inv.getVINDEX();
Vector VGX_COUNTERPTY_CD = gx_inv.getVGX_COUNTERPTY_CD();
Vector VGX_COUNTERPTY_ABBR = gx_inv.getVGX_COUNTERPTY_ABBR();

String fileName ="TransactionChargeActualsReport.xls";

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
<span style="font-weight: bold;font-size: 40px;">Transaction Charge Actuals Report</span>
<br><br>
<%int i=0;int k=0;
for(int j=0; j<VSEGMENT_TYPE.size(); j++){ 
	int index = Integer.parseInt(""+VINDEX.elementAt(j));
if(j!=0)
{%>
<br>
<%} %>
<span style="font-weight: bold;font-size: 24px;">Transaction Charge Remittance [<%=VSEGMENT.elementAt(j) %>]<span style="color:blue;">(<%=VINDEX.elementAt(j)%> Items)</span></span>
<table border="1">
	<thead>
		<tr>
			<th>Sr#</th>
			<th >Type</th>
			<th >Counterparty</th>
			<th ><%=owner_abbr%> BU</th>
			<th >Contract#</th>
			<th >Cash Flow</th>
			<th >Invoice#</th>
			<th >Invoice Date</th>
			<th >Invoice Due Date</th>
			<th >Invoiced MMBTU</th>
			<th >Rate</th>
			<th >Rate Unit</th>
			<th >Invoice Raised In</th>
			<th >Invoice Paid In</th>
			<th >Gross Amount</th>
			<th >Tax</th>
			<th >Invoice Amount</th>
			<th >TCS/TDS</th>
			<th >TCS/TDS %</th>
			<th >+TCS/-TDS Amount</th>
			<th >Adjust Amount</th>
			<th >Net Payable</th>
			<!-- <th >SAP Approval</th> -->
		</tr>		
	</thead>
	<tbody>
	<%k=0;
	if(index > 0){ %>
		<%for(i=i; i<VINVOICE_SEQ.size(); i++){ 
			k+=1;
		%>
		<tr>
			<td align="right"><%=k %></td>
			<td align="center"><%=VTYPE_NM.elementAt(i) %></td>
			<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i) %></td>
			<td align="center"><%=VBU_PLANT_ABBR.elementAt(i) %></td>
			<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
			<td align="center"><%=VCASH_FLOW.elementAt(i)%></td>
			<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
			<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
			<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
			<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
			<td align="right"><%=VTXN_RATE.elementAt(i) %></td>
			<td align="center"><%=VRATE_UNIT.elementAt(i) %></td>
			<td align="center"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>																									
			<td align="center"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>			
			<td align="right"><%=VGROSS_AMT.elementAt(i) %></td>			
			<td align="right"><%=VTAX_AMT.elementAt(i) %></td>			
			<td align="right"><%=VINVOICE_AMT.elementAt(i) %></td>
			<td align="center"><%=VTCS_TDS.elementAt(i) %></td>
			<td align="center"><%=VTCS_TDS_FACTOR.elementAt(i)%></td>													
			<td align="right"><%=VTCS_TDS_AMT.elementAt(i) %></td>													
			<td align="right"><%=VADJ_SIGN.elementAt(i)%><%=VADJ_AMT.elementAt(i)%></td>			
			<td align="right"><%=VNET_PAYABLE.elementAt(i)%></td>
			<%-- <td align="center">
				<%if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
					 Approved
				<%}else{%>
					Pending Approval	
					<%} %>
			</td> --%>
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
			<td colspan="30" align="center"><b>No Invoice is Generated for Selected Period!</b></td>
		</tr>
	<%} %>
	</tbody>
</table>
	
<%} %>

</body>
</html>