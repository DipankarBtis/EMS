<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.text.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<jsp:useBean class="com.etrm.fms.gta.DB_GtaMaster_Report" id="gta" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String customer_cd=request.getParameter("customer_cd")==null?"0":request.getParameter("customer_cd");
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

gta.setCallFlag("GTA_CONTRACT_SUMMARY");
gta.setComp_cd(owner_cd);
gta.setSegmentType(segmentType);
gta.setCounterparty_cd(counterparty_cd);
gta.setFrom_dt(from_dt);
gta.setTo_dt(to_dt);
gta.setCustomer_cd(customer_cd);
gta.init();

Vector VCOUNTERPARTY_CD = gta.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = gta.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = gta.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = gta.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = gta.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = gta.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = gta.getVCONT_NO();
Vector VCONT_REV_NO = gta.getVCONT_REV_NO();
Vector VAGMT_NO = gta.getVAGMT_NO();
Vector VAGMT_REV_NO = gta.getVAGMT_REV_NO();
Vector VMDQ = gta.getVMDQ();
Vector VENTRY_POINT_NAME = gta.getVENTRY_POINT_NAME();
Vector VEXIT_POINT_NAME = gta.getVEXIT_POINT_NAME();
Vector VSTART_DT = gta.getVSTART_DT();
Vector VEND_DT = gta.getVEND_DT();
Vector VDIS_CONT_MAPPING = gta.getVDIS_CONT_MAPPING();
Vector VCONT_REF = gta.getVCONT_REF();
Vector VENT_DT = gta.getVENT_DT();
Vector VENT_BY = gta.getVENT_BY();
Vector VALLOC_MIN_DT = gta.getVALLOC_MIN_DT();
Vector VALLOC_MAX_DT = gta.getVALLOC_MAX_DT();
Vector VBU_POINT = gta.getVBU_POINT();

Vector VENTRY_QTY_MMBTU = gta.getVENTRY_QTY_MMBTU();
Vector VENTRY_QTY_MMSCM = gta.getVENTRY_QTY_MMSCM();
Vector VEXIT_QTY_MMBTU = gta.getVEXIT_QTY_MMBTU();
Vector VEXIT_QTY_MMSCM = gta.getVEXIT_QTY_MMSCM();

Vector VSEGMENT = gta.getVSEGMENT();
Vector VSEGMENT_TYPE = gta.getVSEGMENT_TYPE();
Vector VTEMP_SEGMENT = gta.getVTEMP_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = gta.getVTEMP_SEGMENT_TYPE();
Vector VINDEX = gta.getVINDEX();

Vector VIMBALANCE_QTY = gta.getVIMBALANCE_QTY();
Vector VLINKED_SALES_CONT = gta.getVLINKED_SALES_CONT();

Vector VCUSTOMER_CD = gta.getVCUSTOMER_CD();
Vector VCUSTOMER_NM = gta.getVCUSTOMER_NM();
Vector VCUSTOMER_ABBR = gta.getVCUSTOMER_ABBR();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="18" rowspan="2" align="left">CT | Parking Summary Report (<%=from_dt%>-<%=to_dt%>)</th>
		</tr>
	</table>
	<%int i=0;int k=0;
		for(int j=0; j<VTEMP_SEGMENT.size(); j++){ 
			int index = Integer.parseInt(""+VINDEX.elementAt(j));
		if(j!=0)
		{
		%>
			<table width="100%" border="1">
				<tr>
					<th colspan="18" ></th>
				</tr>
			</table>
		
		<%} %>
		<div class="row m-b-5"><label ><b><%=VTEMP_SEGMENT.elementAt(j) %></b></label></div>
			<div class="row">
				<div class="table-responsive">
					<table class="table table-bordered table-hover" width="100%" border="1">
						<thead>
							<tr>
								<th rowspan="2">Sr#</th>
								<th rowspan="2">Transporter</th>
								<th rowspan="2">Contract#</th>
								<th rowspan="2">Contract Ref#</th>
								<th rowspan="2">Contract Period</th>
								<th rowspan="2">Business Unit</th>
								<th rowspan="2">Entry - Exit Point</th>
								<th rowspan="2">Contract Enter By </th>
								<th rowspan="2">Contract Enter Date </th>
								<th rowspan="2">MDQ<br>(MMBTU)</th>
								<th rowspan="2">Allocation<br>Start Date</th>
								<th rowspan="2" style="background:#000066;color:white;">Last<br>Allocation Date</th>
								<th colspan="2">Entry Energy</th>
								<th colspan="2">Exit Energy</th>
								<th rowspan="2">Cumulative<br>Imbalance</th>
								<%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("K")){ %>
								<th rowspan="2" style="background:#b3f0ff;">Linked CT Contract</th>
								<%}else{ %>
								<th rowspan="2" style="background:#b3f0ff;">Linked Sales Contract</th>
								<%} %>
							</tr>
							<tr>
								<th>MMBTU</th>
								<th>MMSCM</th>
								<th>MMBTU</th>
								<th>MMSCM</th>
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
									<td><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
									<td><%=VDIS_CONT_MAPPING.elementAt(i)%></td>
									<td><%=VCONT_REF.elementAt(i)%></td>
									<td><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
									<td><%=VBU_POINT.elementAt(i) %></td>
									<td align="center"><%=VENTRY_POINT_NAME.elementAt(i) %> - <%=VEXIT_POINT_NAME.elementAt(i) %></td>
									<td align="center"><%=VENT_BY.elementAt(i)%></td>
									<td align="center"><%=VENT_DT.elementAt(i)%></td>
									<td align="right"><%=VMDQ.elementAt(i)%></td>
									<td align="center"><%=VALLOC_MIN_DT.elementAt(i)%></td>
									<td align="center"><%=VALLOC_MAX_DT.elementAt(i)%></td>
									<td align="right"><%=VENTRY_QTY_MMBTU.elementAt(i)%></td>
									<td align="right"><%=VENTRY_QTY_MMSCM.elementAt(i)%></td>
									<td align="right"><%=VEXIT_QTY_MMBTU.elementAt(i)%></td>
									<td align="right"><%=VEXIT_QTY_MMSCM.elementAt(i)%></td>
									<td align="right"><%=VIMBALANCE_QTY.elementAt(i)%></td>
									<td style="background:#b3f0ff;"><%=""+VLINKED_SALES_CONT.elementAt(i).toString().replace("<br>", " ")%></td>
								</tr>
								<%if(k==index)
								{
									i=i+1;
									break;
								}%>
							<%} %>
						<%}else{ %>
							<tr>
								<td colspan="18" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
							</tr>
						<%} %>
						</tbody>
					</table>
				</div>
			</div>		
		<%}%>
</body>
</html>