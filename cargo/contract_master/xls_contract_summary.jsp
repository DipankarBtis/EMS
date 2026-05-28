<%@page import= "java.util.*"%>

<!--
Coded By		  : Harsh Maheta  start->20230304
purpose			  : XLS file for Excel Export functionality
Code Reviewed by  :   
Status	  		  : Developed
-->

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<jsp:useBean class="com.etrm.fms.contract_master.DB_ContractMaster_Report" id="contract" scope="request"></jsp:useBean>
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

contract.setCallFlag("CONTRACT_SUMMARY");
contract.setSegmentType(segmentType);
contract.setCounterparty_cd(counterparty_cd);
contract.setFrom_dt(from_dt);
contract.setComp_cd(owner_cd);
contract.setTo_dt(to_dt);
contract.init();

Vector VCOUNTERPARTY_CD= contract.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = contract.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = contract.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = contract.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = contract.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = contract.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = contract.getVCONT_NO();
Vector VCONT_REV_NO = contract.getVCONT_REV_NO();
Vector VAGMT_NO = contract.getVAGMT_NO();
Vector VAGMT_REV_NO = contract.getVAGMT_REV_NO();
Vector VTCQ = contract.getVTCQ();
Vector VTCQ_MMSCM = contract.getVTCQ_MMSCM();
Vector VSIGNING_DT = contract.getVSIGNING_DT();
Vector VSTART_DT = contract.getVSTART_DT();
Vector VEND_DT = contract.getVEND_DT();
Vector VRATE = contract.getVRATE();
Vector VRATE_UNIT = contract.getVRATE_UNIT();
Vector VDIS_CONT_MAPPING = contract.getVDIS_CONT_MAPPING();
Vector VCONT_STATUS = contract.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = contract.getVCONT_STATUS_FLG();
Vector VCONT_REF = contract.getVCONT_REF();
Vector VENT_DT = contract.getVENT_DT();
Vector VENT_BY = contract.getVENT_BY();
Vector VAPRV_DT = contract.getVAPRV_DT();
Vector VAPRV_BY = contract.getVAPRV_BY();
Vector VPRICE_TYPE = contract.getVPRICE_TYPE();
Vector VDELV_POINT = contract.getVDELV_POINT();
Vector VPLANT_UNIT = contract.getVPLANT_UNIT();
Vector VIS_ALLOCATED = contract.getVIS_ALLOCATED();
Vector VSUPPLIED_QTY_MMBTU = contract.getVSUPPLIED_QTY_MMBTU();
Vector VSUPPLIED_QTY_MMSCM = contract.getVSUPPLIED_QTY_MMSCM();
Vector VBALANCE_QTY_MMBTU = contract.getVBALANCE_QTY_MMBTU();
Vector VBALANCE_QTY_MMSCM = contract.getVBALANCE_QTY_MMSCM();
Vector VALLOC_MIN_DT = contract.getVALLOC_MIN_DT();
Vector VALLOC_MAX_DT = contract.getVALLOC_MAX_DT();
Vector VBU_POINT = contract.getVBU_POINT();//HARSH20230313

Vector VSEGMENT = contract.getVSEGMENT();
Vector VSEGMENT_TYPE = contract.getVSEGMENT_TYPE();
Vector VTEMP_SEGMENT = contract.getVTEMP_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = contract.getVTEMP_SEGMENT_TYPE();
Vector VINDEX = contract.getVINDEX();
Vector VDUE_DATE = contract.getVDUE_DATE();
Vector VEXCHNG_RATE_NM = contract.getVEXCHNG_RATE_NM();

Vector VTOTAL_MMBTU = contract.getVTOTAL_MMBTU();
Vector VTOTAL_SCM = contract.getVTOTAL_SCM();
Vector VTOTALSUPPLIED_MMBTU = contract.getVTOTALSUPPLIED_MMBTU();
Vector VTOTALSUPPLIED_SCM = contract.getVTOTALSUPPLIED_SCM();
Vector VTOTALBALANCE_MMBTU = contract.getVTOTALBALANCE_MMBTU();
Vector VTOTALBALANCE_SCM = contract.getVTOTALBALANCE_SCM();

%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="27" rowspan="" align="left">Contract Summary Report (Generated For <%=from_dt%> To <%=to_dt%>)</th>
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
									<th>Contract Ref#</th>
									<th>Signing Date</th>
									<th>Contract Period</th>
									<th>Status</th>	
									<th>Price Type</th>																			
									<th>Contract Price</th>
									<th>Currency/MMBTU</th>
									<th>Exchange Rate</th>
									<th>Payment Due Period(Days)</th>
									<th>Business Unit</th><!-- HARSH20230313 -->
									<th>Delivery Point</th>
									<th>Customer Plants</th><!-- HARSHMAHETA20230915 -->
									<th>Contract Enter By </th>
									<th>Contract Enter Dt </th>
									<th>Contract Approved By </th>
									<th>Contract Approved Dt </th>
									<th>Allocation Start Date</th>
									<th>Last Allocation Date</th>
									<th>Booked MMBTU</th>
									<th>Booked MMSCM</th>
									<th>Supplied as on MMBTU</th>
									<th>Supplied as on MMSCM </th>
									<th>Balance as on MMBTU</th>
									<th>Balance as on MMSCM</th>
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
											<td><%=VDIS_CONT_MAPPING.elementAt(i)%></td>
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
											<td><%=VBU_POINT.elementAt(i) %></td> <!-- HARSH20230313 -->
											<td><%=VDELV_POINT.elementAt(i) %></td>
											<td><%=VPLANT_UNIT.elementAt(i) %></td><!-- HARSHMAHETA20230915 -->
											<td align="center"><%=VENT_BY.elementAt(i)%></td>
											<td align="center"><%=VENT_DT.elementAt(i)%></td>
											<td align="center"><%=VAPRV_BY.elementAt(i)%></td>
											<td align="center"><%=VAPRV_DT.elementAt(i)%></td>
											<td align="center"><%=VALLOC_MIN_DT.elementAt(i)%></td>
											<td align="center"><%=VALLOC_MAX_DT.elementAt(i)%></td>
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
												<td colspan="21" align="right"><b>Total :</b></td>
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