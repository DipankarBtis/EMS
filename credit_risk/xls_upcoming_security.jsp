<%@page import= "java.util.*"%>
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

cr_report.setCallFlag("UPCOMING_SECURITY_REPORT");
cr_report.setComp_cd(owner_cd);
cr_report.setFrom_dt(from_dt);
cr_report.setClearance(clearance);
cr_report.init();

Vector VSTATUS = cr_report.getVSTATUS();
Vector VVALUE = cr_report.getVVALUE();
Vector VEXPIRE_DT = cr_report.getVEXPIRE_DT();
Vector VREMARK = cr_report.getVREMARK();
Vector VDEAL_DTL = cr_report.getVDEAL_DTL();
Vector VREF_NO = cr_report.getVREF_NO();
Vector VSEC_TYPE = cr_report.getVSEC_TYPE();
Vector VCOUNTERPARTY_NAME = cr_report.getVCOUNTERPARTY_NAME();
Vector VINDEX = cr_report.getVINDEX();
Vector VDEAL_NO = cr_report.getVDEAL_NO();
Vector VSEC_CATEGRY = cr_report.getVSEC_CATEGRY();
Vector VCURRENCY = cr_report.getVCURRENCY();
Vector VDUE_DT = cr_report.getVDUE_DT();
Vector VDEAL_TYPE = cr_report.getVDEAL_TYPE();
Vector VACCOUNT = cr_report.getVACCOUNT();
Vector VDIS_DT = cr_report.getVDIS_DT();
Vector VLEGAL_ENTITY = cr_report.getVLEGAL_ENTITY();
if(clearance.equals("K"))
{
	clearance = "KYC";
}
else
{
	clearance = "IGX";
}
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="14" rowspan="" align="center">Upcoming Security Report of <%=clearance %> (Generated For Report Date: <%=from_dt%> )</th>
		</tr>
		<tr></tr>
	</table>
	
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>SR#</th>
				<th align="center">Legal Entity</th>
				<th align="center">Counterparty Name</th>
				<th align="center">Security Type</th>
				<th align="center">Security Ref No#</th>
				<th align="center">Deal Type</th>
				<th align="center">Contract#</th>
				<th align="center">Security Value</th>
				<th align="center">Currency</th>
				<th align="center">Security Due Date</th>
				<th align="center">Buy/Sell</th>
				<th align="center">Discharge Date</th>
				<th align="center">Status</th>
				<th align="center">Remarks</th>
			</tr>
		</thead>
		<tbody>
		<%if(VCOUNTERPARTY_NAME.size()>0){ %>
			<%int i =0; int k=0;
			for(i=i;i<VREF_NO.size(); i++){ 
			k+=1;%>
				<tr>
					<td align="center"><%=k%></td>
					<td align="center"><%=VLEGAL_ENTITY.elementAt(i) %></td>
					<td><%=VCOUNTERPARTY_NAME.elementAt(i) %></td>
					<td align="center"><%=VSEC_TYPE.elementAt(i) %></td>
					<td align="center"><%=VREF_NO.elementAt(i) %></td>
					<td align="center"><%=VDEAL_TYPE.elementAt(i) %></td>
					<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
					<td align="right"><%=VVALUE.elementAt(i) %></td>
					<td align="center">
					<%if(VCURRENCY.elementAt(i).equals("1")){ %>
						INR
					<%}else if(VCURRENCY.elementAt(i).equals("2")){  %>
						USD
					<%} %>
					</td>
					<td align="center"><%=VDUE_DT.elementAt(i) %></td>
					<td align="center"><%=VACCOUNT.elementAt(i) %></td>
					<td align="center"><%=VDIS_DT.elementAt(i) %></td>
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
				</tr>
			<%} %>
			<%}else{ %>
				<tr>
					<td colspan="14">
						<div align="center"><%=utilmsg.infoMessage("<b>No Upcoming Security Available For Report Date!</b>")%></div>
					</td>
				</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>
