
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CreditRisk_Report" id="cr_report" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String from_dt = request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
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

cr_report.setCallFlag("SECURITY_STATUS_REPORT");
cr_report.setComp_cd(owner_cd);
cr_report.setFrom_dt(from_dt);
cr_report.setClearance(clearance);
cr_report.init();

Vector VSEC_CATEGORY = cr_report.getVSEC_CATEGORY();
Vector V_SEC_CATEGORY = cr_report.getV_SEC_CATEGORY();
Vector VSTATUS = cr_report.getVSTATUS();
Vector VVALUE = cr_report.getVVALUE();
Vector VVALUE_USD = cr_report.getVVALUE_USD();
Vector VISS_BANK_REF = cr_report.getVISS_BANK_REF();
Vector VEXPIRE_DT = cr_report.getVEXPIRE_DT();
Vector VREMARK = cr_report.getVREMARK();
Vector VDEAL_DTL = cr_report.getVDEAL_DTL();
Vector VREF_NO = cr_report.getVREF_NO();
Vector VSEC_TYPE = cr_report.getVSEC_TYPE();
Vector VCOUNTERPARTY_NAME = cr_report.getVCOUNTERPARTY_NAME();
Vector VINDEX = cr_report.getVINDEX();
Vector VDEAL_NO = cr_report.getVDEAL_NO();
Vector VSEC_CATEGRY = cr_report.getVSEC_CATEGRY();
Vector VPREVIOUS_DT = cr_report.getVPREVIOUS_DT();
Vector VCURRENCY = cr_report.getVCURRENCY();
Vector VLEGAL_ENTITY = cr_report.getVLEGAL_ENTITY();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="14" rowspan="2" align="left">Security Status Report <%=from_dt%></th>
		</tr>
	</table>
	<%int i=0;int k=0; int l=0; int j=0; int m=0; int c=0; int n=0; int b=0; int d=0;
		for(j=0; j<VSEC_CATEGORY.size(); j++){
		int index = Integer.parseInt(""+VINDEX.elementAt(j));
		%>
		<% if(j!=0)
			{%>
				<div class="row">
					<div class="col-sm-12 col-xs-12 col-md-12">&nbsp;
					</div>
				</div> 
			<%} %>
				<table width="100%" border="1">
					<tr>
						<th colspan="14" rowspan="1" align="left"><%=VSEC_CATEGORY.elementAt(j)%> Security</th>
					</tr>
				</table>
			<table width="100%" border="1">
				<thead>
					<tr>
						<th>SR#</th>
						<th align="center">Legal Entity</th>
						<th align="center">Counterparty Name</th>
						<th align="center">Issuing Bank Ref#</th>
						<th align="center">Contract#</th>
						<th align="center">Security Ref No#</th>
						<th align="center">Security Type</th>
						<th align="center">Security Value(INR)</th>
						<th align="center">Security Value(USD)</th>
						<th align="center">Day before Expiry Date</th>
						<th align="center">Expiry Date</th>
						<th align="center">Status</th>
						<th align="center">Remarks</th>
						<th align="center">Form C</th>
					</tr>
				</thead>
				<tbody>
					<%k=0;
					if(index > 0){
						for(i=i;i<VREF_NO.size(); i++){ 
						k+=1;%>
							<tr>
								<td align="center"><%=k%></td>
								<td align="center"><%=VLEGAL_ENTITY.elementAt(i) %></td>
								<td><%=VCOUNTERPARTY_NAME.elementAt(i) %></td>
								<td align="center"><%=VISS_BANK_REF.elementAt(i) %></td>
								<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
								<td align="center"><%=VREF_NO.elementAt(i) %></td>
								<td align="center">
									<%=VSEC_TYPE.elementAt(i) %>
   								</td>
								<td align="right"><%=VVALUE.elementAt(i) %></td>
								<td align="right">
								<%-- <%if(VCURRENCY.elementAt(i).equals("1")){ %>
									INR
								<%}else if(VCURRENCY.elementAt(i).equals("2")){  %>
									USD
								<%} %> --%>
									<%=VVALUE_USD.elementAt(i) %>
								</td>
								<td align="center"><%=VPREVIOUS_DT.elementAt(i) %></td>
								<td align="center"><%=VEXPIRE_DT.elementAt(i) %></td>
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
									<%} %>
								</td>
								<td><%=VREMARK.elementAt(i) %></td>
								<td></td>
							</tr>
							<%if(k==index)
							{
								i=i+1;
								break;
							}%>
						<%} %>
					<%}else{ %>
						<tr><td colspan="14" align="center"><b>No Security Status Available!</b></td></tr>
					<%} %>
				</tbody>
			</table>
		<%} %>
</body>
</html>