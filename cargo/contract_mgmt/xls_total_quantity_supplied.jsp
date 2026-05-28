<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="mgmt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String firstDate="01/"+sysdate.substring(3, sysdate.length());
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}
String from_dt=request.getParameter("from_dt")==null?firstDate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");

mgmt_rpt.setCallFlag("TOTAL_SUPPLIED_QTY");
mgmt_rpt.setComp_cd(owner_cd);
mgmt_rpt.setFrom_dt(from_dt);
mgmt_rpt.setTo_dt(to_dt);
mgmt_rpt.init();

String total_mmbtu = mgmt_rpt.getTotal_mmbtu();
String total_scm = mgmt_rpt.getTotal_scm();
Vector VCOUNTERPARTY_CD = mgmt_rpt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = mgmt_rpt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = mgmt_rpt.getVCOUNTERPARTY_ABBR();
Vector VSUPPLIED_QTY_MMBTU = mgmt_rpt.getVSUPPLIED_QTY_MMBTU();
Vector VSUPPLIED_QTY_MMSCM = mgmt_rpt.getVSUPPLIED_QTY_MMSCM();
Vector VGAS_DT = mgmt_rpt.getVGAS_DT();
Vector VTOTALSUPPLIED_SCM = mgmt_rpt.getVTOTALSUPPLIED_SCM();
Vector VTOTALSUPPLIED_MMBTU = mgmt_rpt.getVTOTALSUPPLIED_MMBTU();
Vector VINDEX = mgmt_rpt.getVINDEX();
Vector VCOLOR = mgmt_rpt.getVCOLOR();
Vector V_COLOR = mgmt_rpt.getV_COLOR();
Vector VGRANDTOTALSUPPLIED_MMBTU = mgmt_rpt.getVGRANDTOTALSUPPLIED_MMBTU();
Vector VGRANDTOTALSUPPLIED_SCM = mgmt_rpt.getVGRANDTOTALSUPPLIED_SCM();
Vector VGRAND_COLOR = mgmt_rpt.getVGRAND_COLOR();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="13" rowspan="" align="center">Total Quantity Supplied Report (Generated For Date: <%=from_dt%>-<%=to_dt %> )</th>
		</tr>
		<tr></tr>
	</table>

	<table width="100%" border="1">
		<thead>
			<tr>
				<th></th>
				<th colspan="2" align="center">Total Quantity Supplied</th>
				<%for (int i=0; i<VCOUNTERPARTY_CD.size(); i++) {%>
					<th colspan="2" align="center">Total Quantity Supplied to <br><%=VCOUNTERPARTY_NM.elementAt(i) %></th>
				<%} %>
			</tr>
			<tr>
				<th>Gas Day Starting <br>Date/Time</th>
				<th align="center">MMBTU</th>
				<th align="center">SCM</th>
				<%for (int i=0; i<VCOUNTERPARTY_CD.size(); i++) {%>
					<th align="center">MMBTU</th>
					<th align="center">SCM</th>
				<%} %>
			</tr>
		</thead>
		<tbody>
		<%int a=0; int j=0; int k=0;
		for(j=0; j<VGAS_DT.size(); j++) {
		int index = Integer.parseInt(""+VINDEX.elementAt(j));
		k+=1;%>
			<tr>
				<td align="center"><b><%=VGAS_DT.elementAt(j) %></b></td>
				<td align="right" style="background:<%=V_COLOR.elementAt(j)%>;"><%=VTOTALSUPPLIED_MMBTU.elementAt(j) %></td>
				<td align="right" style="background:<%=V_COLOR.elementAt(j)%>;"><%=VTOTALSUPPLIED_SCM.elementAt(j) %></td>
				<%for (int i=0; i<VCOUNTERPARTY_CD.size(); i++) {%>
					<td align="right" style="background:<%=VCOLOR.elementAt(a)%>;"><%=VSUPPLIED_QTY_MMBTU.elementAt(a) %></td>
					<td align="right" style="background:<%=VCOLOR.elementAt(a)%>;"><%=VSUPPLIED_QTY_MMSCM.elementAt(a) %></td>
				<%a++;} %>
			</tr>
		<%} %>
			<tr>
				<td align="right"><b>Total :</b></td>
				<td align="right"><b><%=total_mmbtu %></b></td>
				<td align="right"><b><%=total_scm %></b></td>
				<%for (int i=0; i<VCOUNTERPARTY_CD.size(); i++) {%>
					<td align="right"><b><%=VGRANDTOTALSUPPLIED_MMBTU.elementAt(i) %></b></td>
					<td align="right"><b><%=VGRANDTOTALSUPPLIED_SCM.elementAt(i) %></b></td>
				<%} %>
			</tr>
		</tbody>
	</table>
						
</body>
</html>