<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sap_interface" id="db_intrface" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
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

String sysdate=utildate.getSysdate();
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt = request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String entity_role = request.getParameter("entity_role")==null?"C":request.getParameter("entity_role");

db_intrface.setCallFlag("SAP_RECON_RPT");
db_intrface.setComp_cd(owner_cd);
db_intrface.setCounterparty_cd(counterparty_cd);
db_intrface.setEntity_role(entity_role);
db_intrface.setFrom_dt(from_dt);
db_intrface.setTo_dt(to_dt);
db_intrface.init();

Vector VMST_COUNTERPARTY_CD = db_intrface.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = db_intrface.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = db_intrface.getVMST_COUNTERPARTY_NM();

Vector VCOUNTERPARTY_CD = db_intrface.getVCOUNTERPARTY_CD();
Vector VCO_CD = db_intrface.getVCO_CD();
Vector VDEAL_MAP = db_intrface.getVDEAL_MAP();
Vector VINVOICE_NO = db_intrface.getVINVOICE_NO();
Vector VACCOUNT_PERIOD_YR = db_intrface.getVACCOUNT_PERIOD_YR();
Vector VACCOUNT_PERIOD_MONTH = db_intrface.getVACCOUNT_PERIOD_MONTH();
Vector VPERIOD_START_DT = db_intrface.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = db_intrface.getVPERIOD_END_DT();
Vector VGL_CD = db_intrface.getVGL_CD();
Vector VGL_DESC = db_intrface.getVGL_DESC();
Vector VVENDOR_CD = db_intrface.getVVENDOR_CD();
Vector VCURRENCY = db_intrface.getVCURRENCY();
Vector VDOC_TYPE = db_intrface.getVDOC_TYPE();
Vector VPK = db_intrface.getVPK();
Vector VDOC_NO=db_intrface.getVDOC_NO();
Vector VITEMTEXT=db_intrface.getVITEMTEXT();
Vector VTRANS_TYPE=db_intrface.getVTRANS_TYPE();
Vector VSAP_APPROVED_BY=db_intrface.getVSAP_APPROVED_BY();
Vector VPOST_DT = db_intrface.getVPOST_DT();
Vector VDOC_DT = db_intrface.getVDOC_DT();
Vector VALLOC_QTY = db_intrface.getVALLOC_QTY();
Vector VAMT = db_intrface.getVAMT();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="23" rowspan="2" align="left">SAP Recon Report</th>
		</tr>
	</table >
	<table width="100%" border="1">
		<thead>
			<th>CoCd</th>
			<th>Contract Ref#</th>
			<th>FMS Reference#</th>
			<th>Year</th>
			<th>Billing Peroid</th>
			<th>Month</th>
			<th>Vendor Cd</th>
			<th>GL</th>
			<th>GL Description</th>
			<th>Currency</th>
			<th>Doc.Type</th>
			<th>P/Ky</th>
			<th>SAP Document No</th>
			<th>GL Line Item- Text</th>
			<th>Transaction Type</th>
			<th>User Name</th>
			<th>Amt in Loc -USD</th>
			<th>Amt in Doc Currency-INR</th>
			<th>Document Date</th>
			<th>Posting Date</th>
			<th>Quantity</th>
			<th>SAP Code</th>
			<th>Difference Value FMS and SAP</th>
		</thead>
		<tbody>
			<%if(VCOUNTERPARTY_CD.size()>0){%>
				<% int k=0; 
				for(int i=0;i<VCOUNTERPARTY_CD.size();i++){%>
					<tr>
						<td><%=VCO_CD.elementAt(i)%></td>
						<td><%=VDEAL_MAP.elementAt(i)%></td>
						<td><%=VINVOICE_NO.elementAt(i) %></td>
						<td><%=VACCOUNT_PERIOD_YR.elementAt(i)%></td>
						<td><%=VPERIOD_START_DT.elementAt(i)%>-<%=VPERIOD_END_DT.elementAt(i)%></td>
						<td><%=VACCOUNT_PERIOD_MONTH.elementAt(i) %></td>
						<td><%=VVENDOR_CD.elementAt(i) %></td>
						<td><%=VGL_CD.elementAt(i)%></td>
						<td><%=VGL_DESC.elementAt(i) %></td>
						<td><%=VCURRENCY.elementAt(i) %></td>
						<td><%=VDOC_TYPE.elementAt(i) %></td>
						<td><%=VPK.elementAt(i) %></td>
						<td><%=VDOC_NO.elementAt(i) %></td>
						<td><%=VITEMTEXT.elementAt(i) %></td>
						<td><%=VTRANS_TYPE.elementAt(i) %></td>
						<td><%=VSAP_APPROVED_BY.elementAt(i) %></td>
						<td></td>
						<td><%=VAMT.elementAt(i) %></td>
						<td><%=VDOC_DT.elementAt(i) %></td>
						<td><%=VPOST_DT.elementAt(i) %></td>
						<td><%=VALLOC_QTY.elementAt(i) %></td>
						<td><%=VVENDOR_CD.elementAt(i) %></td>
						<td></td>
					</tr>
				<%} %>
			<%}else{ %>
				<tr>
					<td colspan="23" align="center"><%=utilmsg.infoMessage("<b>No Invoice Data found!</b>") %></td>
				</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>