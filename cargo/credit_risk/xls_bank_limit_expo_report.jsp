<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CR_ReceivableReport" id="dbcredit" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String report_dt = request.getParameter("report_dt")==null?sysdate:request.getParameter("report_dt");
String bank_nm = request.getParameter("bank_nm")==null?"":request.getParameter("bank_nm");
String bank_lmt_usd = request.getParameter("bank_lmt_usd")==null?"":request.getParameter("bank_lmt_usd");
String bank_lmt_inr = request.getParameter("bank_lmt_inr")==null?"":request.getParameter("bank_lmt_inr");
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

dbcredit.setCallFlag("BANK_LIMIT_EXPOSURE");
dbcredit.setComp_cd(owner_cd);
dbcredit.setReport_dt(report_dt);
dbcredit.init();

Vector VMST_BANK_NM = dbcredit.getVMST_BANK_NM();
Vector VMST_BANK_ABBR = dbcredit.getVMST_BANK_ABBR();
Vector VBANK_LIMIT = dbcredit.getVBANK_LIMIT();
Vector VBANK_LIMIT_USD = dbcredit.getVBANK_LIMIT_USD();
Vector VEXPOSURE_INR = dbcredit.getVEXPOSURE_INR();
Vector VEXPOSURE_USD = dbcredit.getVEXPOSURE_USD();
Vector VAVAILABILITY_INR = dbcredit.getVAVAILABILITY_INR();
Vector VAVAILABILITY_USD = dbcredit.getVAVAILABILITY_USD();
Vector VCOUNTERPARTY_NM = dbcredit.getVCOUNTERPARTY_NM();
Vector VDEAL_NO = dbcredit.getVDEAL_NO();
Vector VEXPIRE_DT = dbcredit.getVEXPIRE_DT();
Vector VISSUE_DT = dbcredit.getVISSUE_DT();
Vector VCONFIRM_BANK_NM = dbcredit.getVCONFIRM_BANK_NM();
Vector VISS_BANK_NM = dbcredit.getVISS_BANK_NM();
Vector VSEC_REF_NO = dbcredit.getVSEC_REF_NO();
Vector VCOUNTERPARTY_ABBR = dbcredit.getVCOUNTERPARTY_ABBR();
Vector VBANK_EXPOSURE_INR = dbcredit.getVBANK_EXPOSURE_INR();
Vector VBANK_EXPOSURE_USD = dbcredit.getVBANK_EXPOSURE_USD();
Vector VBANK_AVAILABILITY_INR = dbcredit.getVBANK_AVAILABILITY_INR();
Vector VBANK_AVAILABILITY_USD = dbcredit.getVBANK_AVAILABILITY_USD();
Vector VSTATUS = dbcredit.getVSTATUS();
Vector VCANCEL_DT = dbcredit.getVCANCEL_DT();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="17" rowspan="2" align="left">Bank Limit And Exposure Report (<%=report_dt%>)</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th align="center">Sr#</th>
				<th align="center">Bank Name</th>
				<th align="center">Bank Limit(INR)</th>
				<th align="center">Bank Limit(USD)</th>
				<th align="center">Customer Name</th>
				<th align="center">Deal No#</th>
				<th align="center">Security Ref#</th>
				<th align="center">Issuing Bank Name</th>
				<th align="center">Confirming Bank Name</th>
				<th align="center">Issue Date</th>
				<th align="center">Expire Date</th>
				<th align="center">Exposure(INR)</th>
				<th align="center">Exposure(USD)</th>
				<th align="center">Availability(INR)</th>
				<th align="center">Availability(USD)</th>
				<th align="center">Status</th>
				<th align="center">Cancellation/Restate Date</th>
			</tr>
		</thead>
		<tbody>
		<%if(VMST_BANK_NM.size()>0){ %>
			<%for(int i=0;i<VMST_BANK_NM.size();i++){%>
				<tr>
					<td align="center"><%=i+1 %></td>
					<td align="center"><%=VMST_BANK_NM.elementAt(i)%></td>
					<td align="right"><%=VBANK_LIMIT.elementAt(i)%></td>
					<td align="right"><%=VBANK_LIMIT_USD.elementAt(i)%></td>
					<td colspan="7"></td>
					<td align="right" 
						<%if (Double.parseDouble(""+VEXPOSURE_INR.elementAt(i))>Double.parseDouble(""+VBANK_LIMIT.elementAt(i))){%>
							style="color: red"
						<%} %>>
						<%=VEXPOSURE_INR.elementAt(i)%>
					</td>
					<td align="right"
					<%if (Double.parseDouble(""+VEXPOSURE_USD.elementAt(i))>Double.parseDouble(""+VBANK_LIMIT_USD.elementAt(i))){%>
							style="color: red"
						<%} %>>
						<%=VEXPOSURE_USD.elementAt(i)%>
					</td>
					<td align="right"><%=VAVAILABILITY_INR.elementAt(i)%></td>
					<td align="right"><%=VAVAILABILITY_USD.elementAt(i)%></td>
					<td colspan="2"></td>
				</tr>
				<tbody>
				<%int j=0,k=0; if(VCOUNTERPARTY_NM.size()>0){ %>
					<%for(j=j;j<VCOUNTERPARTY_NM.size();j++){
						k+=1;
						%>
						<%if(VMST_BANK_NM.elementAt(i).equals(VISS_BANK_NM.elementAt(j)) && VCONFIRM_BANK_NM.elementAt(j).equals("")){ %>
							<tr>
								<td colspan="3"></td>
								<td align="center"><%=VCOUNTERPARTY_NM.elementAt(j) %></td>
								<td align="center"><%=VDEAL_NO.elementAt(j) %></td>
								<td align="center"><%=VSEC_REF_NO.elementAt(j) %></td>
								<td align="center"><%=VISS_BANK_NM.elementAt(j) %></td>
								<td align="center"><%=VCONFIRM_BANK_NM.elementAt(j) %></td>
								<td align="center"><%=VISSUE_DT.elementAt(j) %></td>
								<td align="center"><%=VEXPIRE_DT.elementAt(j) %></td>
								<td align="right"><%=VBANK_EXPOSURE_INR.elementAt(j) %></td>
								<td align="right"><%=VBANK_EXPOSURE_USD.elementAt(j) %></td>
								<td align="right"
									<%if (Double.parseDouble(""+VBANK_AVAILABILITY_INR.elementAt(j))<0){%>
										style="color: red"
									<%} %>>
										<%=VBANK_AVAILABILITY_INR.elementAt(j) %>
								</td>
								<td align="right"
									<%if (Double.parseDouble(""+VBANK_AVAILABILITY_USD.elementAt(j))<0){%>
										style="color: red"
									<%} %>>
										<%=VBANK_AVAILABILITY_USD.elementAt(j) %>
								</td>
								<td align="center">
									<%if(VSTATUS.elementAt(j).equals("P")){ %>
										Pending
									<%}else if(VSTATUS.elementAt(j).equals("O")){  %>
										In Order
									<%}else if(VSTATUS.elementAt(j).equals("C")){  %>
										Cancelled
									<%}else if(VSTATUS.elementAt(j).equals("A")){  %>
										Pending For Amendment
									<%}else if(VSTATUS.elementAt(j).equals("R")){  %>
										Restated
									<%}else if(VSTATUS.elementAt(j).equals("D")){  %>
										Dummy
									<%} %>
								</td>
								<td align="center"><%=VCANCEL_DT.elementAt(j) %></td>
							</tr>
							<%}else if(VMST_BANK_NM.elementAt(i).equals(VCONFIRM_BANK_NM.elementAt(j))){ %>
							<tr>
								<td colspan="3"></td>
								<td align="center"><%=VCOUNTERPARTY_NM.elementAt(j) %></td>
								<td align="center"><%=VDEAL_NO.elementAt(j) %></td>
								<td align="center"><%=VSEC_REF_NO.elementAt(j) %></td>
								<td align="center"><%=VISS_BANK_NM.elementAt(j) %></td>
								<td align="center"><%=VCONFIRM_BANK_NM.elementAt(j) %></td>
								<td align="center"><%=VISSUE_DT.elementAt(j) %></td>
								<td align="center"><%=VEXPIRE_DT.elementAt(j) %></td>
								<td align="right"><%=VBANK_EXPOSURE_INR.elementAt(j) %></td>
								<td align="right"><%=VBANK_EXPOSURE_USD.elementAt(j) %></td>
								<td align="right"
									<%if (Double.parseDouble(""+VBANK_AVAILABILITY_INR.elementAt(j))<0){%>
										style="color: red"
									<%} %>>
										<%=VBANK_AVAILABILITY_INR.elementAt(j) %>
								</td>
								<td align="right"
									<%if (Double.parseDouble(""+VBANK_AVAILABILITY_USD.elementAt(j))<0){%>
										style="color: red"
									<%} %>>
										<%=VBANK_AVAILABILITY_USD.elementAt(j) %>
								</td>
								<td align="center">
									<%if(VSTATUS.elementAt(j).equals("P")){ %>
										Pending
									<%}else if(VSTATUS.elementAt(j).equals("O")){  %>
										In Order
									<%}else if(VSTATUS.elementAt(j).equals("C")){  %>
										Cancelled
									<%}else if(VSTATUS.elementAt(j).equals("A")){  %>
										Pending For Amendment
									<%}else if(VSTATUS.elementAt(j).equals("R")){  %>
										Restated
									<%}else if(VSTATUS.elementAt(j).equals("D")){  %>
										Dummy
									<%} %>
								</td>
								<td align="center"><%=VCANCEL_DT.elementAt(j) %></td>
							</tr>
							<%} %>
						<%
							if(k==VCOUNTERPARTY_NM.size())
							{
								j=j+1;
								break;
							}
						} %>
					<%}else{ %>
					<tr>
						<td colspan="17" align="center"><%=utilmsg.infoMessage("<b>Bank Limit Data Is Not Available!</b>") %></td>
					</tr>
					<%} %>
				<%} %>
			<%}else{ %>
				<tr>
					<td colspan=17 align="center"><%=utilmsg.infoMessage("<b>Bank Limit Data Is Not Available!</b>") %></td>
				</tr>
			<%} %>
			</tbody>
		</tbody>
	</table>
</body>
</html>