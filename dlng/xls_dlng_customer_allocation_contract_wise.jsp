<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import= "java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String sel_bu_plant=request.getParameter("sel_bu_plant")==null?"0":request.getParameter("sel_bu_plant");
String buplant_abbr=request.getParameter("buplant_abbr")==null?"":request.getParameter("buplant_abbr");//RG20250926 for adding BU filter
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

dlng.setCallFlag("DLNG_ALLOCATION_CONTRACT_WISE");
dlng.setComp_cd(owner_cd);
dlng.setSegmentType(segmentType);
dlng.setCounterparty_cd(counterparty_cd);
dlng.setFrom_dt(from_dt);
dlng.setTo_dt(to_dt);
dlng.setBu_plant(sel_bu_plant);//RG20250922 for adding BU filter
dlng.init();

Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();
Vector VDIS_CONT_MAPPING = dlng.getVDIS_CONT_MAPPING();
Vector VCONT_REF = dlng.getVCONT_REF();
Vector VSTART_DT = dlng.getVSTART_DT();
Vector VEND_DT = dlng.getVEND_DT();
Vector VAGMT_BASE = dlng.getVAGMT_BASE();

Vector VALLOCATION_DATA = dlng.getVALLOCATION_DATA();
Vector VCOLOR = dlng.getVCOLOR();

Vector VSEGMENT = dlng.getVSEGMENT();
Vector VSEGMENT_TYPE = dlng.getVSEGMENT_TYPE();
Vector VCONTRACT_TYPE_NM = dlng.getVCONTRACT_TYPE_NM();
Vector VTOTAL_MMBTU = dlng.getVTOTAL_MMBTU();
Vector VTOTAL_MT = dlng.getVTOTAL_MT();
%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition", "inline; filename="+fileName);
%>

<%int x = (VCOUNTERPARTY_CD.size()*2)+1;{%>
<table  width="100%" border="1">
	<tr>
		<th nowrap="nowrap" style="font-size: 21" colspan="<%=x %>" rowspan="" align="left">DLNG Customer Allocation - Contract Wise (Generated For <%=from_dt%> To <%=to_dt%>)<%if(!sel_bu_plant.equals("0") && (!sel_bu_plant.equals(""))) {%> For Business Unit: <%=buplant_abbr%><%} %></th>
	</tr>
	<tr></tr>
</table>
<%} %>

<table  width="100%" border="1">
	<thead>
		<tr>
			<th valign="middle">Customer</th>
		<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
			<th colspan="2"><%=VCOUNTERPARTY_NM.elementAt(i) %></th>
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
			<th>MT</th>
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
		<tr>
			<td align="right"><b>Total:</b></td>
			<%for(int i=0; i<VSTART_DT.size(); i++){ %>
				<td align="right"><b><%=VTOTAL_MMBTU.elementAt(i) %></b></td>
				<td align="right"><b><%=VTOTAL_MT.elementAt(i) %></b></td>
			<%} %>
		</tr>
	</tbody>
</table>
</body>
</html>