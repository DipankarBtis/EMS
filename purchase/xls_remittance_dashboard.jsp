<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();
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

String from_dt = request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

remittance.setCallFlag("REMITTANCE_DASHBOARD");
remittance.setComp_cd(owner_cd);
remittance.setSegment(entity_role);
remittance.setPeriod_start_dt(from_dt);
remittance.setPeriod_end_dt(to_dt);
remittance.setCounterparty_cd(counterparty_cd);
remittance.init();

Vector VMST_SEGMENT = remittance.getVMST_SEGMENT();
Vector VMST_SEGMENT_NM = remittance.getVMST_SEGMENT_NM();

Vector VMST_COUNTERPARTY_CD = remittance.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = remittance.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = remittance.getVMST_COUNTERPARTY_NM();

Vector VHEADER_DISPLAY_SEGMENT = remittance.getVHEADER_DISPLAY_SEGMENT();
Vector VHEADER_DISPLAY_SEGMENT_TYP = remittance.getVHEADER_DISPLAY_SEGMENT_TYP();
Vector VDISPLAY_SEGMENT = remittance.getVDISPLAY_SEGMENT();
Vector VDISPLAY_SEGMENT_TYP = remittance.getVDISPLAY_SEGMENT_TYP();
Vector VSEGMENT_INDEX=remittance.getVSEGMENT_INDEX();

Vector VCOUNTERPARTY_CD=remittance.getVCOUNTERPTY_CD();
Vector VCOUNTERPARTY_ABBR=remittance.getVCOUNTERPTY_ABBR();
Vector VCOUNTERPARTY_NM=remittance.getVCOUNTERPTY_NM();
Vector VINVOICE_SEQ=remittance.getVINVOICE_SEQ();
Vector VDEAL_NO=remittance.getVDEAL_NO();
Vector VSEGMENT=remittance.getVSEGMENT();
Vector VMONTH=remittance.getVMONTH();
Vector VINDEX=remittance.getVINDEX();
Vector VSPLIT_VALUE=remittance.getVSPLIT_VALUE();
Vector VBU_NM=remittance.getVBU_NM();
Vector VINVOICE_TYPE=remittance.getVINVOICE_TYPE();
Vector VDISP_INVOICE_NO=remittance.getVDISP_INVOICE_NO();
Vector VDIS_REMITTANCE_NO=remittance.getVDIS_REMITTANCE_NO();
Vector VTYPE_FLAG=remittance.getVTYPE_FLAG();
Vector VINVOICE_NO=remittance.getVINVOICE_NO();
Vector VLINKED_INV_REF=remittance.getVLINKED_INV_REF();
Vector VCOLOR=remittance.getVCOLOR();
Vector VINV_STATUS=remittance.getVINV_STATUS();
Vector VFREQ_NM=remittance.getVFREQ_NM();
Vector VPERIOD_START_DT=remittance.getVPERIOD_START_DT();
Vector VPERIOD_END_DT=remittance.getVPERIOD_END_DT();
Vector VINVOICE_DT=remittance.getVINVOICE_DT();
Vector VINVOICE_DUE_DT=remittance.getVINVOICE_DUE_DT();
Vector VALLOC_QTY=remittance.getVALLOC_QTY();
Vector VQTY_UNIT=remittance.getVQTY_UNIT();
Vector VEXCHNAGE_RATE=remittance.getVEXCHNAGE_RATE();
Vector VTAX_STRUCT_DTL=remittance.getVTAX_STRUCT_DTL();
Vector VSALES_PRICE=remittance.getVSALES_PRICE();
Vector VSALES_PRICE_UNIT=remittance.getVSALES_PRICE_UNIT();
Vector VRATE_NM=remittance.getVRATE_NM();
Vector VGROSS_AMT_USD=remittance.getVGROSS_AMT_USD();
Vector VTRANSPORT_AMT_USD=remittance.getVTRANSPORT_AMT_USD();
Vector VOTHER_CHARGE_AMT_USD=remittance.getVOTHER_CHARGE_AMT_USD();
Vector VMARKET_AMT_USD=remittance.getVMARKET_AMT_USD();
Vector VTAX_AMT_USD=remittance.getVTAX_AMT_USD();
Vector VINVOICE_AMT_USD=remittance.getVINVOICE_AMT_USD();
Vector VTCS_AMT_USD=remittance.getVTCS_AMT_USD();
Vector VTDS_AMT_USD=remittance.getVTDS_AMT_USD();
Vector VADJ_SIGN_USD=remittance.getVADJ_SIGN_USD();
Vector VADJ_AMT_USD=remittance.getVADJ_AMT_USD();
Vector VNET_PAYABLE_USD=remittance.getVNET_PAYABLE_USD();
Vector VGROSS_AMT=remittance.getVGROSS_AMT();
Vector VTRANSPORT_AMT=remittance.getVTRANSPORT_AMT();
Vector VMARKET_AMT=remittance.getVMARKET_AMT();
Vector VOTHER_CHARGE_AMT=remittance.getVOTHER_CHARGE_AMT();
Vector VTAX_AMT=remittance.getVTAX_AMT();
Vector VTCS_AMT=remittance.getVTCS_AMT();
Vector VTDS_AMT=remittance.getVTDS_AMT();
Vector VADJ_SIGN=remittance.getVADJ_SIGN();
Vector VADJ_AMT=remittance.getVADJ_AMT();
Vector VNET_PAYABLE=remittance.getVNET_PAYABLE();
Vector VINVOICE_AMT=remittance.getVINVOICE_AMT();
Vector VIRP_CHK=remittance.getVIRP_CHK();
Vector VIRP_CHK_BY=remittance.getVIRP_CHK_BY();
Vector VIRP_CHK_DT=remittance.getVIRP_CHK_DT();
Vector VIRP_APPROVED=remittance.getVIRP_APPROVED();
Vector VIRP_APPROVED_BY=remittance.getVIRP_APPROVED_BY();
Vector VIRP_APPROVED_DT=remittance.getVIRP_APPROVED_DT();
Vector VFIN_APPROVED=remittance.getVFIN_APPROVED();
Vector VFIN_APPROVED_BY=remittance.getVFIN_APPROVED_BY();
Vector VFIN_APPROVED_DT=remittance.getVFIN_APPROVED_DT();
Vector VPRINT_PDF=remittance.getVPRINT_PDF();
Vector VPRINT_PDF_BY=remittance.getVPRINT_PDF_BY();
Vector VPRINT_PDF_DT=remittance.getVPRINT_PDF_DT();
Vector VSAP_APPROVAL_FLAG=remittance.getVSAP_APPROVAL_FLAG();
Vector VSAP_APPROVED_BY=remittance.getVSAP_APPROVED_BY();
Vector VSAP_APPROVED_DT=remittance.getVSAP_APPROVED_DT();
Vector VCNT_INDEX=remittance.getVCNT_INDEX();

Vector VTOTAL_ALLOC_QTY=remittance.getVTOTAL_ALLOC_QTY();
Vector VTOTAL_GROSS_USD=remittance.getVTOTAL_GROSS_USD();
Vector VTOTAL_INV_USD=remittance.getVTOTAL_INV_USD();
Vector VTOTAL_NET_PAY_USD=remittance.getVTOTAL_NET_PAY_USD();
Vector VTOTAL_GROSS_INR=remittance.getVTOTAL_GROSS_INR();
Vector VTOTAL_INV_INR=remittance.getVTOTAL_INV_INR();
Vector VTOTAL_NET_PAY_INR=remittance.getVTOTAL_NET_PAY_INR();
Vector VTOTAL_TAX_INR=remittance.getVTOTAL_TAX_INR();
Vector VTOTAL_TAX_USD=remittance.getVTOTAL_TAX_USD();

Vector VCONT_REF_NO=remittance.getVCONT_REF_NO();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
<table  width="100%" border="1">
	<tr>
		<th colspan="43" rowspan="2" align="left">Remittance Dashboard From <%=from_dt%> To <%=to_dt%></th>
	</tr>
</table >

<%int i=0;int k=0;
	for(int j=0; j<VDISPLAY_SEGMENT_TYP.size(); j++){ 
		int index = Integer.parseInt(""+VINDEX.elementAt(j));
	if(j!=0)
	{
	%>
	<%} %>
	<div class="row">
			<div class="col-sm-12 col-xs-12 col-md-12">
				&nbsp;
			</div>
	</div> 
	<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K")){%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="37" rowspan="2" align="left"><%=VDISPLAY_SEGMENT.elementAt(j)%></th>
		</tr>
	</table>
	<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
	<table  width="100%" border="1">
		<tr>
			<th colspan="36" rowspan="2" align="left"><%=VDISPLAY_SEGMENT.elementAt(j)%></th>
		</tr>
	</table>
	<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
	<table  width="100%" border="1">
		<tr>
			<th colspan="27" rowspan="2" align="left"><%=VDISPLAY_SEGMENT.elementAt(j)%></th>
		</tr>
	</table>
	<%}else{ %>
	<table  width="100%" border="1">
		<tr>
			<th colspan="43" rowspan="2" align="left"><%=VDISPLAY_SEGMENT.elementAt(j)%></th>
		</tr>
	</table>
	<%} %>
		
	<%-- <table  width="100%" border="1">
		<tr>
			<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K")){%>
				<th colspan="37" rowspan="2" align="left"><%=VDISPLAY_SEGMENT.elementAt(j)%></th>
			<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<th colspan="35" rowspan="2" align="left"><%=VDISPLAY_SEGMENT.elementAt(j)%></th>
			<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
				<th colspan="27" rowspan="2" align="left"><%=VDISPLAY_SEGMENT.elementAt(j)%></th>
			<%}else{ %>
				<th colspan="43" rowspan="2" align="left"><%=VDISPLAY_SEGMENT.elementAt(j)%></th>
				<th colspan="43" rowspan="2" align="left"><%=VDISPLAY_SEGMENT.elementAt(j)%></th>
			<%} %>
		</tr>
	</table> --%>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th rowspan="2">Sr#</th>
				<th rowspan="2">Segment
				</th>
				<th rowspan="2">BU
				</th>
				<%-- <th rowspan="2">Month
					<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_month<%=j%>" onkeyup="Search(this,'2','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
				</th> --%>
				<th rowspan="2">Trader
				</th>
				<th rowspan="2"><%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("N") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("CD") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("L")){ %>Cargo#<%}else{ %>Contract#<%} %>
				</th>
				<th rowspan="2"><%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("N") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("CD") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("L")){ %>Cargo Ref#<%}else{ %>Contract Ref#<%} %>
				</th>
				<th rowspan="2">Invoice Type</th>
				<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<th colspan="9">Invoice Details</th>
				<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<th colspan="10">Invoice Details</th>
				<%}else{ %>
					<th colspan="11">Invoice Details</th>
				<%} %>
				<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<th colspan="7">USD Details</th>
					<th colspan="7">INR Details</th>
				<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<th colspan="3">USD Details</th>
					<th colspan="3">INR Details</th>
				<%}else{ %>
					<th colspan="10">USD Details</th>
					<th colspan="10">INR Details</th>
				<%} %>
				<%-- <%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<th rowspan="2">Payment Status</th>
					<th rowspan="2">Payment Received (INR)</th>
				<%} %> --%>
				<th rowspan="2">IRP Check</th>
				<th rowspan="2">IRP Approval</th>
				<th rowspan="2">Fin Ops Finialization</th>
				<th rowspan="2">Print PDF</th>
				<th rowspan="2"><%if(owner_cd.equals("1")) {%>SAP XML<%}else{ %>SUN XML<%} %></th>
			</tr>
			<tr>
				<th>Invoice#
				</th>
				<th>Remittance#
				</th>
				<th>Invoice Status
				</th>
				<%-- <%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<th>Billing Cycle
					<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_billing_cycle<%=j%>" onkeyup="Search(this,'10','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
				</th>
				<%} %> --%>
				<th>Invoice Period</th>
				<th>Invoice Date
				</th>
				<th>Invoice Due Date</th>
				<th>Invoiced Qty</th>
				<th>Qty Unit</th>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
				<th>Exchange Rate</th>
				<th>Tax Structure</th>
				<%} %>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<th>Price/Qty</th>
				<%} %>
				<th>Gross Amount</th>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<th>Market Margin</th>
				<th>Other Charges</th>
				<th>Transportation Charges</th>
				<%} %>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<th>Tax</th>
				<%} %>
				<th>Invoice Amount</th>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<th>TCS Value</th>
					<th>TDS Value</th>
					<th>Adjustment Amount</th>
				<%} %>
				<th>Net Payable</th>
				<!-- <th>Price/Qty</th> -->
				<th>Gross Amount</th>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<th>Market Margin</th>
				<th>Other Charges</th>
				<th>Transportation Charges</th>
				<%} %>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<th>Tax</th>
				<%} %>
				<th>Invoice Amount</th>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<th>TCS Value</th>
					<th>TDS Value</th>
					<th>Adjustment Amount</th>
				<%} %>
				<th>Net Payable</th>
			</tr>
		</thead>
		<tbody>
		<%k=0;
		if(index > 0){ %>
			<%for(i=i; i<VINVOICE_SEQ.size(); i++){ 
				k+=1;
			%>
			<tr>
				<td align="center">
					<%=k%>
				</td>
				<td align="center"><%=VSEGMENT.elementAt(i)%></td>
				<td align="center"><%=VBU_NM.elementAt(i)%></td>
				<%-- <td align="center"><%=VMONTH.elementAt(i)%></td> --%>
				<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
				<td align="center"><%=VDEAL_NO.elementAt(i)%>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
						<%if(!VSPLIT_VALUE.elementAt(i).equals("")){ %>
							<font style="background:#ff99ff;">[Split <%=VSPLIT_VALUE.elementAt(i)%>%]</font>
						<%} %>
					<%} %>	
				</td>
				<td align="center"><%=VCONT_REF_NO.elementAt(i) %></td>
				<td align="center"><%=VINVOICE_TYPE.elementAt(i)%>
				 <span style="background-color: <%if(VTYPE_FLAG.elementAt(i).equals("FFLOW")){ %>#ffe6e6<%}else if(VTYPE_FLAG.elementAt(i).equals("PG")){%>#b3f0ff<%}else if(VTYPE_FLAG.elementAt(i).equals("SG")){%>#ffff66<%}%>;">
				 	<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>[<%=VTYPE_FLAG.elementAt(i) %>]<%} %>
				 </span>
				 </td>
				<td align="left"><%=VINVOICE_NO.elementAt(i)%> <%if(!VLINKED_INV_REF.elementAt(i).equals("")){%>(Ref:<%=VLINKED_INV_REF.elementAt(i)%>)<%} %></td>
				<td align="center"><%=VDIS_REMITTANCE_NO.elementAt(i) %>
				<td align="center" style="background: <%=VCOLOR.elementAt(i)%>"><%=VINV_STATUS.elementAt(i) %></td>
				<%-- <%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<td align="center"><%=VFREQ_NM.elementAt(i)%></td>
				<%} %> --%>
				<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
				<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
				<td align="right"><%=VALLOC_QTY.elementAt(i)%></td>
				<td align="center"><%=VQTY_UNIT.elementAt(i) %></td>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
				<td align="right"><%=VEXCHNAGE_RATE.elementAt(i)%></td>
				<td align="center"><%=VTAX_STRUCT_DTL.elementAt(i)%></td>
				<%} %>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<td align="right"><%=VSALES_PRICE.elementAt(i)%>&nbsp;<%=VRATE_NM.elementAt(i)%></td>
				<%} %>
				<td align="right"><%=VGROSS_AMT_USD.elementAt(i)%></td>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<td align="right"><%=VTRANSPORT_AMT_USD.elementAt(i)%></td>
				<td align="right"><%=VOTHER_CHARGE_AMT_USD.elementAt(i)%></td>
				<td align="right"><%=VMARKET_AMT_USD.elementAt(i)%></td>
				<%} %>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<td align="right"><%=VTAX_AMT_USD.elementAt(i) %></td>
				<%} %>
				<td align="right"><%=VINVOICE_AMT_USD.elementAt(i)%></td>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<td align="right"><%=VTCS_AMT_USD.elementAt(i) %></td>
					<td align="right"><%=VTDS_AMT_USD.elementAt(i) %></td>
					<td align="right"><%=VADJ_SIGN_USD.elementAt(i)%><%=VADJ_AMT_USD.elementAt(i)%></td>
				<%} %>
				<td align="right"><%=VNET_PAYABLE_USD.elementAt(i)%></td>
				<%-- <td align="right"><%=VSALES_PRICE.elementAt(i)%> --%><!-- &nbsp; --><%//=VRATE_NM.elementAt(i)%></td>
				<td align="right"><%=VGROSS_AMT.elementAt(i)%></td>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
				<td align="right"><%=VTRANSPORT_AMT.elementAt(i)%></td>
				<td align="right"><%=VOTHER_CHARGE_AMT.elementAt(i)%></td>
				<td align="right"><%=VMARKET_AMT.elementAt(i)%></td>
				<%} %>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<td align="right"><%=VTAX_AMT.elementAt(i) %></td>
				<%} %>
				<td align="right"><%=VINVOICE_AMT.elementAt(i)%></td>
				<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<td align="right"><%=VTCS_AMT.elementAt(i) %></td>
					<td align="right"><%=VTDS_AMT.elementAt(i) %></td>
					<td align="right"><%=VADJ_SIGN.elementAt(i)%><%=VADJ_AMT.elementAt(i)%></td>
				<%} %>
				<td align="right"><%=VNET_PAYABLE.elementAt(i)%></td>
				<td align="center"><%=VIRP_CHK_BY.elementAt(i) %> <%=VIRP_CHK_DT.elementAt(i) %></td>
				<td align="center"><%=VIRP_APPROVED_BY.elementAt(i) %> <%=VIRP_APPROVED_DT.elementAt(i) %></td>
				<td align="center"><%=VFIN_APPROVED_BY.elementAt(i) %> <%=VFIN_APPROVED_DT.elementAt(i) %></td>
				<td align="center"><%=VPRINT_PDF_BY.elementAt(i) %> <%=VPRINT_PDF_DT.elementAt(i) %></td>
				<td align="center"><%=VSAP_APPROVED_BY.elementAt(i) %> <%=VSAP_APPROVED_DT.elementAt(i) %></td>
			</tr>
			<%if(k==index)
			 {%>
				<tr>
					<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
						<td colspan="13" align="right"><b>Total :</b></td>
					<%}else{ %>
						<td colspan="13" align="right"><b>Total :</b></td>
					<%} %>
					<td align="right"><b><%=VTOTAL_ALLOC_QTY.elementAt(j) %></b></td>
					<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
						<td colspan="2"></td>
					<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
						<td colspan="3"></td>
					<%}else{ %>
						<td colspan="4"></td>
					<%} %>
					<td align="right"><b><%=VTOTAL_GROSS_USD.elementAt(j) %></b></td>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<td colspan="3"></td>
					<%} %>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
						<td colspan="1" align="right"><b><%=VTOTAL_TAX_USD.elementAt(j) %></b></td>
					<%} %>
					<td align="right"><b><%=VTOTAL_INV_USD.elementAt(j) %></b></td>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
						<td colspan="3"></td>
					<%} %>
					<td align="right"><b><%=VTOTAL_NET_PAY_USD.elementAt(j) %></b></td>
					<td align="right"><b><%=VTOTAL_GROSS_INR.elementAt(j) %></b></td>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<td colspan="3"></td>
					<%} %>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
						<td colspan="1" align="right"><b><%=VTOTAL_TAX_INR.elementAt(j) %></b></td>
					<%} %>
					<td align="right"><b><%=VTOTAL_INV_INR.elementAt(j) %></b></td>
					<%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
						<td colspan="3"></td>
					<%} %>
					<td align="right"><b><%=VTOTAL_NET_PAY_INR.elementAt(j) %></b></td>
					<%-- <%if(!VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") && !VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %> --%>
					<td colspan="5"></td>
					<%-- <%} %> --%>
				</tr> 	
			<%i=i+1;
				 break;
			 } %>
		<%}%>
		<%}else{ %>
			<tr>
				<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("G") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("K")){%>
					<td colspan="37" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Month!</b>") %></td>
				<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("X") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("Z")){ %>
					<td colspan="36" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Month!</b>") %></td>
				<%}else if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("V")){ %>
					<td colspan="27" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Month!</b>") %></td>
				<%}else{ %>
					<td colspan="43" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Month!</b>") %></td>
				<%} %>
			</tr>
		<%} %>
		</tbody>
	</table>
	<div class="row">
			<div class="col-sm-12 col-xs-12 col-md-12">
				&nbsp;
			</div>
		</div> 
	<%} %>
</body>
</html>