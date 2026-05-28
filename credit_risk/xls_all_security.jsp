
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DB_CreditRisk_Report" id="credit_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String sec_category = request.getParameter("sec_category")==null?"":request.getParameter("sec_category");
String clearance = request.getParameter("clearance")==null?"K":request.getParameter("clearance");
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

credit_risk.setCallFlag("ALL_SECURITY_REPORT");
credit_risk.setCounterparty_cd(counterparty_cd);
credit_risk.setClearance(clearance);
credit_risk.setComp_cd(owner_cd);
credit_risk.init();

Vector VCOUNTERPARTY_CD = credit_risk.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NAME = credit_risk.getVCOUNTERPARTY_NAME();
Vector VSEC_CATEGORY = credit_risk.getVSEC_CATEGORY();
Vector VSEC_TYPE = credit_risk.getVSEC_TYPE();
Vector VSEC_REF_NO = credit_risk.getVSEC_REF_NO();
Vector VVALUE = credit_risk.getVVALUE();
Vector VRECEIVED_DATE = credit_risk.getVRECEIVED_DATE();
Vector VCURRENCY = credit_risk.getVCURRENCY();
Vector VSTATUS = credit_risk.getVSTATUS();
Vector VDEAL_NO = credit_risk.getVDEAL_NO();
Vector VISS_BANK_NM = credit_risk.getVISS_BANK_NM();
Vector VVALUE_FLUCTUATION = credit_risk.getVVALUE_FLUCTUATION();
Vector VDEAL_TYPE = credit_risk.getVDEAL_TYPE();
Vector VISS_BANK_REF = credit_risk.getVISS_BANK_REF();
Vector VADV_BANK_NM = credit_risk.getVADV_BANK_NM();
Vector VADV_BANK_REF = credit_risk.getVADV_BANK_REF();
Vector VCONFIRM_BANK_NM = credit_risk.getVCONFIRM_BANK_NM();
Vector VCONFIRM_BANK_REF = credit_risk.getVCONFIRM_BANK_REF();
Vector VREVIEW_DT = credit_risk.getVREVIEW_DT();
Vector VTENOR = credit_risk.getVTENOR();
Vector VISSUE_DT = credit_risk.getVISSUE_DT();
Vector VEXPIRE_DT = credit_risk.getVEXPIRE_DT();
Vector VREMARK = credit_risk.getVREMARK();
Vector VVALUE_VARIATION = credit_risk.getVVALUE_VARIATION();
Vector VGUARANTOR_NM = credit_risk.getVGUARANTOR_NM();
Vector VGUARANTOR_CD = credit_risk.getVGUARANTOR_CD();
Vector VISS_BANK_CD = credit_risk.getVISS_BANK_CD();
Vector VADV_BANK_CD = credit_risk.getVADV_BANK_CD();
Vector VCONFIRM_BANK_CD = credit_risk.getVCONFIRM_BANK_CD();
Vector VCANCEL_DT = credit_risk.getVCANCEL_DT();
Vector VRENEW_DT = credit_risk.getVRENEW_DT();
Vector VDEAL_DTL =credit_risk.getVDEAL_DTL();
Vector VSEQ_NO = credit_risk.getVSEQ_NO();
Vector VSEQ_REV_NO = credit_risk.getVSEQ_REV_NO();
Vector VCOUNTERPARTY_ABBR = credit_risk.getVCOUNTERPARTY_ABBR();
Vector VEXP_VAL = credit_risk.getVEXP_VAL();
Vector VLEGAL_ENTITY = credit_risk.getVLEGAL_ENTITY();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="28" rowspan="2" align="left">All Security Report <%if(clearance.equals("K")){%>(KYC)<%}else{ %>(IGX)<%} %></th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>SR#</th>
				<th>Legal Entity</th>
				<th>Counterparty</th>
				<th>Incoming/ Outgoing</th>
				<th>Security Type</th>
				<th>Security Ref.No</th>
				<th>Status</th>
				<th>Deal Type</th>
				<th>Deal No.</th>
				<th>Currency</th>
				<th>value</th>
				<th>Value Variation</th>
				<th>Value Fluctuation (%)</th>
				<th>Received / Authenticated Date</th>
				<th>Issuance Date</th>
				<th>Expire Date</th>
				<th>Cancellation / Restate Date</th>
				<th>Renew Date</th>
				<th>Tenor (Day)</th>
				<th>Issuing Bank name</th>
				<th>Issuing Bank's Reference</th>
				<th>Advising Bank Name</th>
				<th>Advising Bank's Reference</th>
				<th>Confirming Bank Name</th>
				<th>Confirming Bank's Reference</th>
				<th>Guarantor Name</th>
				<th>Remarks</th>
				<th>Review date</th>
			</tr>
		</thead>
		<tbody>
		<%int k=0;
		for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){
			k+=1;
			String security_type = ""+VSEC_TYPE.elementAt(i);
			String security_category = ""+VSEC_CATEGORY.elementAt(i);
			%>
			
			<tr>
				<td align="right"><%=k%></td>
				<td align="center"><%=VLEGAL_ENTITY.elementAt(i) %></td>
				<td align="center"><%=VCOUNTERPARTY_NAME.elementAt(i) %></td>
				<td align="center">
					<%if(security_category.equals("R")){ %>
						Incoming
					<%}else if(security_category.equals("I")){ %>
						Outgoing
					<%} %>
				</td>
				<td align="center">
					<%=VSEC_TYPE.elementAt(i) %></td>
				<td align="center"><%=VSEC_REF_NO.elementAt(i) %></td>
				<td align="center">
					<%if(VSTATUS.elementAt(i).equals("P")){ %>
						Pending
					<%}else if(VSTATUS.elementAt(i).equals("O")){  %>
						In Order
					<%}else if(VSTATUS.elementAt(i).equals("C")){  %>
						Cancelled
					<%}else if(VSTATUS.elementAt(i).equals("A")){  %>
						Pending For Amendment
					<%}else if(VSTATUS.elementAt(i).equals("R")){  %>
						Restated
					<%}else if(VSTATUS.elementAt(i).equals("D")){  %>
						Dummy
					<%} %>
				</td>
				<td align="center"><%=VDEAL_TYPE.elementAt(i) %></td>
				<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
				<td align="center">
				<%if(VCURRENCY.elementAt(i).equals("1")){ %>
					INR
				<%}else if(VCURRENCY.elementAt(i).equals("2")){ %>
					USD
				<%}else{} %>
				</td>
				<td align="right"><%=VVALUE.elementAt(i) %></td>
				<td align="center"><%=VVALUE_VARIATION.elementAt(i) %></td>
				<td align="center"><%=VVALUE_FLUCTUATION.elementAt(i) %></td>
				<td align="center"><%=VRECEIVED_DATE.elementAt(i) %></td>
				<td align="center"><%=VISSUE_DT.elementAt(i) %></td>
				<td align="center"><%=VEXPIRE_DT.elementAt(i) %></td>
				<td align="center"><%=VCANCEL_DT.elementAt(i) %></td>
				<td align="center"><%=VRENEW_DT.elementAt(i) %></td>
				<td align="center"><%=VTENOR.elementAt(i) %></td>
				<td align="center"><%=VISS_BANK_NM.elementAt(i) %></td>
				<td align="center"><%=VISS_BANK_REF.elementAt(i) %></td>
				<td align="center"><%=VADV_BANK_NM.elementAt(i) %></td>
				<td align="center"><%=VADV_BANK_REF.elementAt(i) %></td>
				<td align="center"><%=VCONFIRM_BANK_NM.elementAt(i) %></td>
				<td align="center"><%=VCONFIRM_BANK_REF.elementAt(i) %></td>
				<td align="center"><%=VGUARANTOR_NM.elementAt(i) %></td>
				<td align="center"><%=VREMARK.elementAt(i) %></td>
				<td align="center"><%=VREVIEW_DT.elementAt(i) %></td>
			</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>