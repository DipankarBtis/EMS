<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Invoice" id="derv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
//String inv_type=request.getParameter("inv_type")==null?"":request.getParameter("inv_type");

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

derv.setCallFlag("DERV_INV_APPROVAL");
derv.setComp_cd(owner_cd);
derv.setFrom_dt(from_dt);
derv.setTo_dt(to_dt);
//derv.setInv_type(inv_type);
derv.init();

Vector VMST_INV_TYPE = derv.getVMST_INV_TYPE();
Vector VMST_INV_TYPE_FLG = derv.getVMST_INV_TYPE_FLG();
Vector VBU_NM = derv.getVBU_PLANT_ABBR();
Vector VCOUNTERPARTY_CD = derv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = derv.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = derv.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = derv.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = derv.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = derv.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = derv.getVCONT_NO();
Vector VAGMT_NO = derv.getVAGMT_NO();
Vector VDIS_CONT_MAPPING = derv.getVDEAL_MAPPING();
Vector VCONTRACT_TYPE = derv.getVCONT_TYPE();
Vector VPERIOD_START_DT = derv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = derv.getVPERIOD_END_DT();
Vector VFINANCIAL_YEAR = derv.getVFIN_YEAR();
Vector VINVOICE_NO = derv.getVINVOICE_NO();
Vector VINVOICE_SEQ = derv.getVINVOICE_SEQ();
Vector VBU_STATE_TIN = derv.getVBU_STATE_TIN();
Vector VINVOICE_DT = derv.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = derv.getVINVOICE_DUE_DT();
Vector VSALES_PRICE = derv.getVSELL_RATE();
Vector VSALES_PRICE_CD = derv.getVSELL_PRICE_CD();
Vector VSALES_PRICE_NM = derv.getVSELL_PRICE_NM();
Vector VGROSS_AMT = derv.getVGROSS_AMT();
Vector VINVOICE_AMT = derv.getVINVOICE_AMT();
Vector VNET_PAYABLE_AMT = derv.getVNET_PAYABLE_AMT();
Vector VPAY_RECV_AMT = derv.getVPAY_RECV_AMT();
Vector VPAY_RECV_DT = derv.getVPAY_RECV_DT();
Vector VSHORT_RECEIVED = derv.getVSHORT_RECEIVED();
Vector VSAP_APPROVAL_FLAG = derv.getVSAP_APPROVAL_FLG();
Vector VINVOICE_TYPE = derv.getVINVOICE_TYPE();
Vector VINDEX = derv.getVINDEX();

Vector VALLOC_QTY = derv.getVALLOC_QTY();
Vector VINVOICE_RAISED_IN = derv.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = derv.getVPAYMENT_DONE_IN();
Vector VSALES_AMT = derv.getVSELL_AMT();
Vector VCONT_REF_NO= derv.getVCONT_REF();
Vector VCASH_FLOW= derv.getVCASH_FLOW();
Vector VINV_REF= derv.getVINV_REF();
Vector VALLOC_QTY_UNIT = derv.getVALLOC_QTY_UNIT();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename=DerivativesActualsAccountingReport.xls");
%>
<table width="100%" border="1">
	<tr>
		<th nowrap="nowrap" style="font-size: 21" colspan="18" rowspan="" align="left">Derivatives Invoice (FO) Approval</th>
	</tr>
</table>
	<%int i=0;int k=0;
	for(int l=0; l<VMST_INV_TYPE_FLG.size();l++){
	int index=Integer.parseInt(""+VINDEX.elementAt(l));
	if(l!=0)
	{
	%>
	<div class="row">
		<div class="col-sm-12 col-xs-12 col-md-12">
			&nbsp;
		</div>
	</div> 
	<%} %>
	<div class="row m-b-5">
		<label ><b><%=VMST_INV_TYPE.elementAt(l) %></b></label>
			</div>
				<div class="row">
					<div class="table-responsive">
						<table class="table table-bordered table-hover" width="100%" border="1">
							<tr>
								<th>SR No</th>
								<th>Trader</th>
								<th><%=owner_abbr%> BU</th>
								<th>Contract#</th>
								<th>Contract/Trade Ref#</th>
								<th>Billing Period</th>
								<th>Cash Flow</th>
								<th>Invoice#</th>
								<%if(VMST_INV_TYPE_FLG.elementAt(l).equals("R")){ %>
								<th>Remittance#</th>
								<%} %>
								<th>Invoice Date</th>
								<th>Invoice Due Date</th>
								<th>Invoiced Qty</th>
								<th>Qty Unit</th>
								<!-- <th>Rate</th>
								<th style="background: #000066; color: white;">Rate Unit / MMBTU</th> -->
								<th>Sales Amount</th>
								<th>Invoice Raised In</th>
								<th>Invoice Paid In</th>
								<th>Gross Amount</th>
								<th>Invoice Amount</th>
								<%if(VMST_INV_TYPE_FLG.elementAt(l).equals("R")){ %>
								<th>Net Payable</th>
								<%}else{ %>
								<th>Net Receivable</th>
								<%} %>
							</tr>
						<%k=0;
						if(index > 0){ %>
							<%for(i=i; i<VCOUNTERPARTY_CD.size(); i++){ 
							k+=1;%>
							<tr>
								<td align="center"><%=k%></td>
								<td title="<%=VCOUNTERPARTY_NM.elementAt(i)%>">
									<%=VCOUNTERPARTY_NM.elementAt(i)%>
									<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>" disabled>
									<input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>" disabled>
									<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>" disabled>
									<input type="hidden" name="contract_type" id="contract_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled>
									<input type="hidden" name="financial_year" id="financial_year<%=i%>" value="<%=VFINANCIAL_YEAR.elementAt(i)%>" disabled>
									<input type="hidden" name="invoice_seq" id="invoice_seq<%=i%>" value="<%=VINVOICE_SEQ.elementAt(i)%>" disabled>
									<input type="hidden" name="bu_state_tin" id="bu_state_tin<%=i%>" value="<%=VBU_STATE_TIN.elementAt(i)%>" disabled>
								</td>
								<td align="center"><%=VBU_NM.elementAt(i) %></td>
								<td align="center"><div style="width:300px; word-wrap: break-word; white-space: normal;"><%=VDIS_CONT_MAPPING.elementAt(i) %></div></td>
								<td align="center"><div style="width:300px; word-wrap: break-word; white-space: normal;"><%=VCONT_REF_NO.elementAt(i) %></div></td>
								<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
								<td align="center"><%=VCASH_FLOW.elementAt(i)%></td>
								<%if(VMST_INV_TYPE_FLG.elementAt(l).equals("R")){ %>
									<td align="center"><%=VINV_REF.elementAt(i)%></td>
									<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
								<%}else{ %>
									<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
								<%} %>
								<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
								<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
								<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
								<td align="right"><%=VALLOC_QTY_UNIT.elementAt(i) %></td>
								<%-- <td align="right"><%=VSALES_PRICE.elementAt(i)%></td>
								<td align="center" style="background: #b3f0ff;"><%=VSALES_PRICE_NM.elementAt(i) %></td> --%>
								<td align="right"><%=VSALES_AMT.elementAt(i)%></td>
								<td><%=VINVOICE_RAISED_IN.elementAt(i) %></td>
								<td><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
								<td> <%=VGROSS_AMT.elementAt(i)%>
									<input type="hidden" name="gross_amt" id="gross_amt<%=i%>" value="<%=VGROSS_AMT.elementAt(i)%>">
								</td>
								<td align="right">
									<%=VINVOICE_AMT.elementAt(i) %>
									<input type="hidden" name="invoice_amt" id="invoice_amt<%=i%>" value="<%=VINVOICE_AMT.elementAt(i)%>">
								</td>
								<td align="right">
									<%=VNET_PAYABLE_AMT.elementAt(i)%>
								</td>	
							</tr>
							<%if(k==index){
								i=i+1;
								break;
							} %>
							<%} %>
						<%}else{ %>
							<tr>
								<%if(VMST_INV_TYPE_FLG.elementAt(l).equals("R")){ %>
									<td colspan="19" align="center"><%=utilmsg.infoMessage("<b>No Remittance is Generated for Selected Period!</b>") %></td>
								<%}else{%>
									<td colspan="20" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
								<%} %>
							</tr>
						<%} %>
				</table>
			</div>
		</div>
	<%}%>
</body>
</html>