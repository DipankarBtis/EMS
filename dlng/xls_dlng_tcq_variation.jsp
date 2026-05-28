<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String firstDate="01/"+sysdate.substring(3, sysdate.length());

String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?firstDate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
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


dlng.setCallFlag("DLNG_TCQ_VARIATION");
dlng.setComp_cd(owner_cd);
dlng.setSegmentType(segmentType);
dlng.setCounterparty_cd(counterparty_cd);
dlng.setFrom_dt(from_dt);
dlng.setTo_dt(to_dt);
dlng.init();

Vector VSEGMENT = dlng.getVSEGMENT();
Vector VSEGMENT_TYPE = dlng.getVSEGMENT_TYPE();
Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VTEMP_SEGMENT = dlng.getVTEMP_SEGMENT();
Vector VINDEX = dlng.getVINDEX();
Vector VTEMP_SEGMENT_TYPE = dlng.getVTEMP_SEGMENT_TYPE();
Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VDIS_CONT_MAPPING = dlng.getVDIS_CONT_MAPPING();
Vector VCONT_REF = dlng.getVCONT_REF();
Vector VSIGNING_DT = dlng.getVSIGNING_DT();
Vector VSTART_DT = dlng.getVSTART_DT();
Vector VEND_DT = dlng.getVEND_DT();
Vector VIS_ALLOCATED = dlng.getVIS_ALLOCATED();
Vector VCONT_STATUS = dlng.getVCONT_STATUS();
Vector VFINAL_TCQ = dlng.getVFINAL_TCQ();
Vector VTCQ = dlng.getVTCQ();
Vector VVARIATION_TCQ = dlng.getVVARIATION_TCQ();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="10" rowspan="" align="center">DLNG TCQ Variation Report (Generated For <%=from_dt%> To <%=to_dt%>)</th>
		</tr>
	</table>
	<%int i=0;int k=0;
	for(int j=0; j<VTEMP_SEGMENT.size(); j++)
	{ 
	int index = Integer.parseInt(""+VINDEX.elementAt(j));%>
	<div class="row m-b-5">
		<label><%=VTEMP_SEGMENT.elementAt(j) %></label>
	</div>
		<table width="100%" border="1">
			<thead>
				<tr>
					<th>Sr#</th>
					<th>Customer</th>
					<th>Contract#</th>
					<%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("X")){ %>
					<th>Trade Ref#</th>
					<%}else{ %>
					<th>Contract Ref#</th>
					<%} %>
					<th>Signing Date</th>
					<th>Contract Period</th>
					<!-- <th>Contract Closure Date</th> -->
					<th>Status</th>	
					<th>Final TCQ <br>(MMBTU)</th>																			
					<th>Contractual TCQ<br>(MMBTU)</th>
					<th>Variation<br>(MMBTU)</th>
				</tr>
			</thead>
			<tbody>
			<%k=0;
			if(index > 0){ %>
				<%for(i=i;i<VCOUNTERPARTY_CD.size(); i++){ 
				k+=1;
				%>
					<tr>
						<td align="center"><%=k%></td>
						<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
						<td><%=VDIS_CONT_MAPPING.elementAt(i)%></td>
						<td><%=VCONT_REF.elementAt(i)%></td>
						<td><%=VSIGNING_DT.elementAt(i)%></td>
						<td><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
						<!-- <td></td> -->
						<td <%if(VIS_ALLOCATED.elementAt(i).equals("Y")){ %>style="background:#99ffcc;"<%}else{ %>style="background:#ffffcc;"<%} %>>
							<%=VCONT_STATUS.elementAt(i)%>
						</td>
						<td align="right"><%=VFINAL_TCQ.elementAt(i) %></td>
						<td align="right"><%=VTCQ.elementAt(i) %></td>
						<td align="right"><%=VVARIATION_TCQ.elementAt(i) %></td>
					</tr>
					<%if(k==index)
					{
						i=i+1;
						break;
					}%>
				<%} %>
			<%}else{ %>
				<tr>
					<td colspan="10" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
				</tr>
			<%} %>
			</tbody>
		</table>
	<%}%>
</body>
</html>