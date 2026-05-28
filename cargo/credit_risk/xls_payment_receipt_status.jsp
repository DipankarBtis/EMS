<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CreditRisk_Report" id="dbcredit" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String previousDate=utildate.getPreviousDate();
String rpt_dt = request.getParameter("rpt_dt")==null?previousDate:request.getParameter("rpt_dt");
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
dbcredit.setCallFlag("PAYMENT_RECEIPT_STATUS");
dbcredit.setRpt_dt(rpt_dt);
dbcredit.setComp_cd(owner_cd);
dbcredit.init();

Vector VCOUNTERPARTY_NAME = dbcredit.getVCOUNTERPARTY_NAME();
Vector VCOUNTERPARTY_ABBR = dbcredit.getVCOUNTERPARTY_ABBR();
Vector VINVOICE_NO = dbcredit.getVINVOICE_NO();
Vector VDEAL_NO = dbcredit.getVDEAL_NO();
Vector VNET_DUE_DT = dbcredit.getVNET_DUE_DT();
Vector VRECV_AMT = dbcredit.getVRECV_AMT();
Vector VAMT_DC = dbcredit.getVAMT_DC();
Vector VDUE_AMT = dbcredit.getVDUE_AMT();
Vector VCO_CODE = dbcredit.getVCO_CODE();
Vector VCO_ABBR = dbcredit.getVCO_ABBR();

Vector VSEC_TYPE = dbcredit.getVSEC_TYPE();
Vector VSEC_REF_NO = dbcredit.getVSEC_REF_NO();
Vector VDEAL_REF_NO = dbcredit.getVDEAL_REF_NO();
Vector VPAY_RECV_DT = dbcredit.getVPAY_RECV_DT();
Vector VPAYMENT_STATUS = dbcredit.getVPAYMENT_STATUS();
Vector VEXPIRE_DT = dbcredit.getVEXPIRE_DT();
Vector VSTATUS = dbcredit.getVSTATUS();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table  width="100%" border="1">
		<tr>
			<th colspan="14" rowspan="2" align="left">Payment Receipt Status Report (<%=rpt_dt %>)</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th align="center">Sr#</th>
				<th align="center">Entity</th>
				<th align="center">Customer Name</th>
				<th align="center">Security Type</th>
				<th align="center">Security Ref#</th>
				<th align="center">Deal Ref#</th>
				<th align="center">Invoice No.</th>
				<th align="center">Invoice Amount(INR)</th>
				<th align="center">Invoice Due Date</th>
				<th align="center">Received Amount(INR)</th>
				<th align="center">Payment Received Date</th>
				<th align="center">Payment Status</th>
				<th align="center">Security Expiry Date</th>
				<th align="center">Security Status</th>
			</tr>
		</thead>
		<tbody>
			<%if(VCOUNTERPARTY_NAME.size()>0){
				for(int i=0;i<VCOUNTERPARTY_NAME.size();i++){%>
				<tr>
					<td align="center"><%=i+1%></td>
					<td align="center"><%=VCO_ABBR.elementAt(i)%></td>
					<td align="center"><%=VCOUNTERPARTY_NAME.elementAt(i)%></td>
					<td align="center"><%=VSEC_TYPE.elementAt(i) %></td>
					<td align="center"><%=VSEC_REF_NO.elementAt(i)%></td>
					<td align="center"><%=VDEAL_REF_NO.elementAt(i)%></td>
					<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
					<td align="right"><%=VAMT_DC.elementAt(i)%></td>
					<td align="center"><%=VNET_DUE_DT.elementAt(i)%></td>
					<td align="right"><%=VRECV_AMT.elementAt(i)%></td>
					<td align="center"><%=VPAY_RECV_DT.elementAt(i)%></td>
					<td <%if(VPAYMENT_STATUS.elementAt(i).equals("Not Received")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>align="center"><b><%=VPAYMENT_STATUS.elementAt(i)%></b></td>
					<td align="center"><%=VEXPIRE_DT.elementAt(i)%></td>
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
						<%}%>
					</td>
				</tr>
				<%} %>
			<%}else{ %>
				<tr>
					<td colspan=14 align="center"><%=utilmsg.infoMessage("<b>No Payment Receipt Status Data is Available!</b>") %></td>
				</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>