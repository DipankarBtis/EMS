<%@page import= "java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sysdate=utildate.getSysdate();
String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
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

dlng.setCallFlag("DLNG_CONTRACT_SUMMARY");
dlng.setSegmentType(segmentType);
dlng.setCounterparty_cd(counterparty_cd);
dlng.setFrom_dt(from_dt);
dlng.setComp_cd(owner_cd);
dlng.setTo_dt(to_dt);
dlng.init();

Vector VCOUNTERPARTY_CD= dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = dlng.getVCONT_NO();
Vector VCONT_REV_NO = dlng.getVCONT_REV_NO();
Vector VAGMT_NO = dlng.getVAGMT_NO();
Vector VAGMT_REV_NO = dlng.getVAGMT_REV_NO();
Vector VTCQ = dlng.getVTCQ();
Vector VTCQ_MMSCM = dlng.getVTCQ_MMSCM();
Vector VSIGNING_DT = dlng.getVSIGNING_DT();
Vector VSTART_DT = dlng.getVSTART_DT();
Vector VEND_DT = dlng.getVEND_DT();
Vector VRATE = dlng.getVRATE();
Vector VRATE_UNIT = dlng.getVRATE_UNIT();
Vector VDIS_CONT_MAPPING = dlng.getVDIS_CONT_MAPPING();
Vector VCONT_STATUS = dlng.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = dlng.getVCONT_STATUS_FLG();
Vector VCONT_REF = dlng.getVCONT_REF();
Vector VENT_DT = dlng.getVENT_DT();
Vector VENT_BY = dlng.getVENT_BY();
Vector VAPRV_DT = dlng.getVAPRV_DT();
Vector VAPRV_BY = dlng.getVAPRV_BY();
Vector VPRICE_TYPE = dlng.getVPRICE_TYPE();
Vector VDELV_POINT = dlng.getVDELV_POINT();
Vector VPLANT_UNIT = dlng.getVPLANT_UNIT();
Vector VIS_ALLOCATED = dlng.getVIS_ALLOCATED();
Vector VSUPPLIED_QTY_MMBTU = dlng.getVSUPPLIED_QTY_MMBTU();
Vector VSUPPLIED_QTY_MMSCM = dlng.getVSUPPLIED_QTY_MMSCM();
Vector VBALANCE_QTY_MMBTU = dlng.getVBALANCE_QTY_MMBTU();
Vector VBALANCE_QTY_MMSCM = dlng.getVBALANCE_QTY_MMSCM();
Vector VALLOC_MIN_DT = dlng.getVALLOC_MIN_DT();
Vector VALLOC_MAX_DT = dlng.getVALLOC_MAX_DT();
Vector VBU_POINT = dlng.getVBU_POINT();

Vector VSEGMENT = dlng.getVSEGMENT();
Vector VSEGMENT_TYPE = dlng.getVSEGMENT_TYPE();
Vector VTEMP_SEGMENT = dlng.getVTEMP_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = dlng.getVTEMP_SEGMENT_TYPE();
Vector VINDEX = dlng.getVINDEX();
Vector VDUE_DATE = dlng.getVDUE_DATE();
Vector VEXCHNG_RATE_NM = dlng.getVEXCHNG_RATE_NM();

Vector VTOTAL_MMBTU = dlng.getVTOTAL_MMBTU();
Vector VTOTAL_SCM = dlng.getVTOTAL_SCM();
Vector VTOTALSUPPLIED_MMBTU = dlng.getVTOTALSUPPLIED_MMBTU();
Vector VTOTALSUPPLIED_SCM = dlng.getVTOTALSUPPLIED_SCM();
Vector VTOTALBALANCE_MMBTU = dlng.getVTOTALBALANCE_MMBTU();
Vector VTOTALBALANCE_SCM = dlng.getVTOTALBALANCE_SCM();
Vector VCONTRACT_TYPE = dlng.getVCONTRACT_TYPE();

Vector VDCQ = dlng.getVDCQ();
Vector VTOTAL_DCQ = dlng.getVTOTAL_DCQ();
%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="27" rowspan="" align="left">DLNG Contract Summary Report (Generated For <%=from_dt%> To <%=to_dt%>)</th>
		</tr>
	</table>
	
		<%int i=0;int k=0;
		for(int j=0; j<VTEMP_SEGMENT.size(); j++){ 
		int index = Integer.parseInt(""+VINDEX.elementAt(j));
		if(j!=0)
		{
		%>
		<div class="row">
			<div class="col-sm-12 col-xs-12 col-md-12">
				&nbsp;
			</div>
		</div> 
		<%} %>
		<div class="row m-b-5">
			<label ><b><%=VTEMP_SEGMENT.elementAt(j) %></b></label>
				</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" width="100%" border="1">
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
									<th>Status</th>	
									<th>Price Type</th>																			
									<th>Contract Price</th>
									<th>Currency/MMBTU</th>
									<th>Exchange Rate</th>
									<th>Payment Due Period(Days)</th>
									<th>Business Unit</th>
									<th>Customer Plants</th>
									<th>Contract Enter By </th>
									<th>Contract Enter Dt </th>
									<th>Contract Approved By </th>
									<th>Contract Approved Dt </th>
									<th>Allocation Start Date</th>
									<th>Last Allocation Date</th>
									<th>DCQ</th>
									<th>Booked MMBTU</th>
									<th>Booked MT</th>
									<th>Supplied as on MMBTU</th>
									<th>Supplied as on MT </th>
									<th>Balance as on MMBTU</th>
									<th>Balance as on MT</th>
								</tr>
							</thead>
							<tbody>
	
								<%	k=0;
									if(index > 0){ %>
									<%	for(i=i;i<VCOUNTERPARTY_CD.size(); i++){ 
									k+=1;
									%>				
										<tr>
											<td align="center"><%=k%></td>
											<td><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
											<td><%=VCONTRACT_TYPE.elementAt(i)%></td>
											<td><%=VCONT_REF.elementAt(i)%></td>
											<td><%=VSIGNING_DT.elementAt(i)%></td>
											<td><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td <%if(VIS_ALLOCATED.elementAt(i).equals("Y")){ %>style="background:#99ffcc;"<%}else{ %>style="background:#ffffcc;"<%} %>>
												<%=VCONT_STATUS.elementAt(i)%>
											</td>
											<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
											<td align="right"><%=VRATE.elementAt(i)%></td>
											<td align="center"><%=VRATE_UNIT.elementAt(i)%></td>
											<td><%=VEXCHNG_RATE_NM.elementAt(i) %></td>
											<td><%=VDUE_DATE.elementAt(i) %></td>
											<td><%=VBU_POINT.elementAt(i) %></td>
											<td><%=VPLANT_UNIT.elementAt(i) %></td>
											<td align="center"><%=VENT_BY.elementAt(i)%></td>
											<td align="center"><%=VENT_DT.elementAt(i)%></td>
											<td align="center"><%=VAPRV_BY.elementAt(i)%></td>
											<td align="center"><%=VAPRV_DT.elementAt(i)%></td>
											<td align="center"><%=VALLOC_MIN_DT.elementAt(i)%></td>
											<td align="center"><%=VALLOC_MAX_DT.elementAt(i)%></td>
											<td align="center"><%=VDCQ.elementAt(i)%></td>
											<td align="center"><%=VTCQ.elementAt(i)%></td>
											<td align="center"><%=VTCQ_MMSCM.elementAt(i)%></td>
											<td align="center"><%=VSUPPLIED_QTY_MMBTU.elementAt(i)%></td>
											<td align="center"><%=VSUPPLIED_QTY_MMSCM.elementAt(i)%></td>
											<td align="center"><%=VBALANCE_QTY_MMBTU.elementAt(i)%></td>
											<td align="center"><%=VBALANCE_QTY_MMSCM.elementAt(i)%></td>
										</tr>
										<%if(k==index)
										{%>
											<tr>
												<td colspan="20" align="right"><b>Total :</b></td>
												<td align="right"><b><%=VTOTAL_DCQ.elementAt(j)%></b></td>
												<td align="right"><b><%=VTOTAL_MMBTU.elementAt(j) %></b></td>
												<td align="right"><b><%=VTOTAL_SCM.elementAt(j) %></b></td>
												<td align="right"><b><%=VTOTALSUPPLIED_MMBTU.elementAt(j) %></b></td>
												<td align="right"><b><%=VTOTALSUPPLIED_SCM.elementAt(j) %></b></td>
												<td align="right"><b><%=VTOTALBALANCE_MMBTU.elementAt(j) %></b></td>												
												<td align="right"><b><%=VTOTALBALANCE_SCM.elementAt(j) %></b></td>
											</tr>
											<% i=i+1;
											break;
										}%>
									<%} %>
										<%}else{ %>
											<tr>
												<td colspan="27" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
											</tr>
									<%} %>		
								</tbody>
							</table>
						</div>
					</div>
				<%}%>
</body>
</html>
<!--  Harsh Maheta  end->20230310 -->