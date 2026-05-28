<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GTA_Remittance" id="remittance" scope="request"></jsp:useBean>
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

remittance.setCallFlag("GTA_REMITTANCE_FO_APPROVAL");
remittance.setComp_cd(owner_cd);
//remittance.setSegment(segment);
remittance.setFrom_dt(from_dt);
remittance.setTo_dt(to_dt);
remittance.init();

Vector VSEGMENT = remittance.getVSEGMENT();
Vector VSEGMENT_TYPE = remittance.getVSEGMENT_TYPE();

Vector VINVOICE_DT = remittance.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = remittance.getVINVOICE_DUE_DT();
Vector VINVOICE_NO = remittance.getVINVOICE_NO();
Vector VINVOICE_SEQ = remittance.getVINVOICE_SEQ();
//Vector VPERIOD_START_DT = remittance.getVPERIOD_START_DT();
//Vector VPERIOD_END_DT = remittance.getVPERIOD_END_DT();
Vector VALLOC_QTY = remittance.getVALLOC_QTY();
Vector VTXN_RATE = remittance.getVTXN_RATE();
Vector VRATE_UNIT = remittance.getVRATE_UNIT();
Vector VGROSS_AMT = remittance.getVGROSS_AMT();
Vector VTAX_AMT = remittance.getVTAX_AMT();
Vector VINVOICE_AMT = remittance.getVINVOICE_AMT();
//Vector VEXCHNAGE_RATE = remittance.getVEXCHNAGE_RATE();
//Vector VEXCHNAGE_RATE_DATE = remittance.getVEXCHNAGE_RATE_DATE();
Vector VBU_PLANT_ABBR = remittance.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ = remittance.getVBU_PLANT_SEQ();
Vector VDEAL_NO = remittance.getVDEAL_NO();
//Vector VSALE_AMT = remittance.getVSALE_AMT();
Vector VADJ_SIGN = remittance.getVADJ_SIGN();
Vector VADJ_AMT = remittance.getVADJ_AMT();
Vector VNET_PAYABLE = remittance.getVNET_PAYABLE();
Vector VTCS_TDS = remittance.getVTCS_TDS();
Vector VTCS_TDS_AMT = remittance.getVTCS_TDS_AMT();
Vector VTCS_TDS_FACTOR = remittance.getVTCS_TDS_FACTOR();
Vector VINVOICE_RAISED_IN = remittance.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = remittance.getVPAYMENT_DONE_IN();
Vector VFINANCIAL_YEAR = remittance.getVFINANCIAL_YEAR();
Vector VCONTRACT_TYPE = remittance.getVCONTRACT_TYPE();
Vector VSAP_APPROVAL_FLAG = remittance.getVSAP_APPROVAL_FLAG();

Vector VINVOICE_TYPE =remittance.getVINVOICE_TYPE();
Vector VTYPE_FLAG = remittance.getVTYPE_FLAG();
Vector VTYPE_NM= remittance.getVTYPE_NM();

Vector VTCS_TDS_STRUCT_CD = remittance.getVTCS_TDS_STRUCT_CD();
Vector VTCS_TDS_EFF_DT = remittance.getVTCS_TDS_EFF_DT();

Vector VCOUNTERPARTY_CD = remittance.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = remittance.getVCOUNTERPARTY_ABBR();
Vector VINDEX = remittance.getVINDEX();
Vector V_TYPE_NM = remittance.getV_TYPE_NM();

Vector VCONT_NO = remittance.getVCONT_NO();
Vector VAGMT_NO = remittance.getVAGMT_NO();
Vector VCASH_FLOW= remittance.getVCASH_FLOW();

String fileName ="GTAActualsAccountingReport.xls";
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
<span style="font-weight: bold;font-size: 40px;">GTA Remittance (FO) Approval</span>
<br><br>
<%int i=0;int k=0;
for(int j=0; j<VSEGMENT_TYPE.size(); j++){ 
	int index = Integer.parseInt(""+VINDEX.elementAt(j));
if(j!=0)
{%>
<br>
<%} %>
<span style="font-weight: bold;font-size: 24px;">GTA Remittance [<%=VSEGMENT.elementAt(j) %>]<span style="color:blue;">(<%=VINDEX.elementAt(j)%> Items)</span></span>
<table border="1">
	<thead>
		<tr>
			<th rowspan="2">Type</th>
			<th rowspan="2">Transporter</th>
			<th rowspan="2"><%=owner_abbr%> BU</th>
			<th rowspan="2">Contract#</th>
			<th rowspan="2">Cash Flow</th>
			<th rowspan="2">Invoice#</th>
			<th rowspan="2">Invoice Date</th>
			<th rowspan="2">Invoice Due Date</th>
			<th rowspan="2" >Invoice Raised In</th>
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
			<td align="center"><%=VTYPE_NM.elementAt(i) %></td>
			<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i) %></td>
			<td align="center"><%=VBU_PLANT_ABBR.elementAt(i) %></td>
			<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
			<td align="center"><%=VCASH_FLOW.elementAt(i) %></td>
			<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
			<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
			<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
			<td align="center" ><%=VINVOICE_RAISED_IN.elementAt(i) %></td>																													
			<td align="center" ><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
			<td align="right"></td>
			<td align="right"><%=VGROSS_AMT.elementAt(i) %></td>
			<td align="right"></td>
			<td align="right"><%=VTAX_AMT.elementAt(i) %></td>
			<td align="right"></td>
			<td align="right"><%=VINVOICE_AMT.elementAt(i) %></td>
			<td align="center"><%=VTCS_TDS.elementAt(i) %></td>
			<td align="center"><%=VTCS_TDS_FACTOR.elementAt(i)%></td>
			<td align="right"></td>										
			<td align="right"><%=VTCS_TDS_AMT.elementAt(i) %></td>
			<td align="right"></td>										
			<td align="right"><%=VADJ_SIGN.elementAt(i)%><%=VADJ_AMT.elementAt(i)%></td>
			<td align="right"></td>
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
			<td colspan="30" align="center"><b>No Invoice is Generated for Selected Period!</b></td>
		</tr>
	<%} %>
	</tbody>
</table>
<%} %>
<span style="font-weight: bold;font-size: 24px;">GTA Free Flow Remittance<span style="color:blue;">(<%=V_TYPE_NM.size()%> Items)</span></span></span>
<table border="1">
	<thead>
		<tr>
			<th>Sr#</th>
			<th>Type</th>
			<th>Transporter</th>
			<th><%=owner_abbr%> BU</th>
			<th>Contract#</th>
			<th>Cash Flow</th>
			<th>Invoice#</th>
			<th>Invoice Date</th>
			<th>Invoice Due Date</th>
			<th>Invoice Raised In</th>
			<th>Invoice Paid In</th>
			<th>Gross Amount</th>
			<th>Tax</th>
			<th>Invoice Amount</th>
			<th>TCS/TDS</th>
			<th>TCS/TDS %</th>
			<th>+TCS/-TDS Amount</th>
			<th>Adjust Amount</th>
			<th>Net Payable</th>
		</tr>									
	</thead>
	<tbody>
	<%k=0;i=0;
	if(V_TYPE_NM.size() > 0){ %>
	<%for(i=i; i<VINVOICE_SEQ.size(); i++){
	if(VTYPE_NM.elementAt(i).equals("FFLOW"))
	{
	k+=1;
	%>
		<tr>
			<td align="right"><%=k %></td>	
			<td align="center"><%=VTYPE_NM.elementAt(i) %></td>
			<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i) %></td>
			<td align="center"><%=VBU_PLANT_ABBR.elementAt(i) %></td>
			<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
			<td align="center"><%=VCASH_FLOW.elementAt(i) %></td>
			<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
			<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
			<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
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
		</tr>
		<%
			} 
		}%>
		<%}else{ %>
		<tr>
			<td colspan="30" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
		</tr>
	<%} %>
	</tbody>
</table>
</body>
</html>