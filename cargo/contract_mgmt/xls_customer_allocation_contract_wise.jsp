<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import= "java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

cont_mgmt.setCallFlag("ALLOCATION_CONTRACT_WISE");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setSegmentType(segmentType);
cont_mgmt.setCounterparty_cd(counterparty_cd);
cont_mgmt.setFrom_dt(from_dt);
cont_mgmt.setTo_dt(to_dt);
cont_mgmt.init();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = cont_mgmt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = cont_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = cont_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = cont_mgmt.getVMST_COUNTERPARTY_ABBR();
Vector VDIS_CONT_MAPPING = cont_mgmt.getVDIS_CONT_MAPPING();
Vector VCONT_REF = cont_mgmt.getVCONT_REF();
Vector VSTART_DT = cont_mgmt.getVSTART_DT();
Vector VEND_DT = cont_mgmt.getVEND_DT();
Vector VAGMT_BASE = cont_mgmt.getVAGMT_BASE();

Vector VALLOCATION_DATA = cont_mgmt.getVALLOCATION_DATA();
Vector VCOLOR = cont_mgmt.getVCOLOR();

Vector VSEGMENT = cont_mgmt.getVSEGMENT();
Vector VSEGMENT_TYPE = cont_mgmt.getVSEGMENT_TYPE();

%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition", "inline; filename="+fileName);
%>

<%int x = (VCOUNTERPARTY_CD.size()*2)+1;{%>
<table  width="100%" border="1">
	<tr>
		<th nowrap="nowrap" style="font-size: 21" colspan="<%=x %>" rowspan="" align="left">Customer Allocation - Contract Wise (Generated For <%=from_dt%> To <%=to_dt%>)</th>
	</tr>
	<tr></tr>
</table>
<%} %>

<table  width="100%" border="1">
	<thead>
		<tr>
			<th valign="middle">Customer</th>
		<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
			<th colspan="2"><%=VCOUNTERPARTY_ABBR.elementAt(i) %></th>
		<%} %>
		</tr>
		<tr>
			<th valign="middle">Contract#</th>
		<%for(int i=0; i<VDIS_CONT_MAPPING.size(); i++){ %>
			<th colspan="2"><%=VDIS_CONT_MAPPING.elementAt(i) %>
			<%if(VAGMT_BASE.elementAt(i).equals("D")){ %>&nbsp;<font style="background:#a6ff4d">(DLV)</font><%} %>
			<br>[<%=VCONT_REF.elementAt(i)%>]</th>
		<%} %>
		</tr>
		<tr>
			<th valign="middle">Contract Period</th>
		<%for(int i=0; i<VSTART_DT.size(); i++){ %>
			<th colspan="2"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></th>
		<%} %>
		</tr>
		<tr>
			<th valign="middle">Gas Day</th>
		<%for(int i=0; i<VSTART_DT.size(); i++){ %>
			<th>MMBTU</th>
			<th>MMSCM</th>
		<%} %>
		</tr>
	</thead>
	<tbody>
		<%int k=0;
		int index=(VCOUNTERPARTY_CD.size()*2)+1;%>
		<%for(int j=0; j<VALLOCATION_DATA.size(); j++){ 
			k=k+1;
		%>
		<%if(k==1){ %><tr><%} %>
				<%if(k==1){%>
				<td align="center" style="background:<%=VCOLOR.elementAt(j)%>;"><b><%=VALLOCATION_DATA.elementAt(j)%></b></td>
				<%}else{%>
				<td align="right" style="background:<%=VCOLOR.elementAt(j)%>;"><%=VALLOCATION_DATA.elementAt(j)%></td>
				<%} %>	
			<%if(k==index){ %>
				</tr>
			<%	k=0;
				continue;
			}%>
		<%}%>
	</tbody>
</table>
</body>
</html>