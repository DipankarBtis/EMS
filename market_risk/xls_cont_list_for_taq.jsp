<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_VariablePricing" id="market_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String fileName = "TAQ_Report.xls";
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}
String sysdt = utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdt:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdt:request.getParameter("to_dt");

market_risk.setCallFlag("TAQ_CONT_LIST");
market_risk.setComp_cd(owner_cd);
market_risk.setFrom_dt(from_dt);
market_risk.setTo_dt(to_dt);
market_risk.init();

Vector VCOUNTERPARTY_CD = market_risk.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = market_risk.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = market_risk.getVCOUNTERPARTY_ABBR();
Vector VCONTRACT_TYPE = market_risk.getVCONTRACT_TYPE();
Vector VDIS_CONTRACT_TYPE = market_risk.getVDIS_CONTRACT_TYPE();
Vector VCONT_NO = market_risk.getVCONT_NO();
Vector VCONT_REV_NO = market_risk.getVCONT_REV_NO();
Vector VAGMT_NO = market_risk.getVAGMT_NO();
Vector VAGMT_REV_NO = market_risk.getVAGMT_REV_NO();
//Vector VCARGO_NO = market_risk.getVCARGO_NO();
Vector VDISPLAY_DEAL_MAP = market_risk.getVDISPLAY_DEAL_MAP();
Vector VCONT_REF_NO = market_risk.getVCONT_REF_NO();
Vector VSIGNING_DT = market_risk.getVSIGNING_DT();
Vector VSTART_DT = market_risk.getVSTART_DT();
Vector VEND_DT = market_risk.getVEND_DT();
Vector VACCOUNT = market_risk.getVACCOUNT();
Vector VTCQ = market_risk.getVTCQ();
Vector VDCQ = market_risk.getVDCQ();
Vector VSUPPLIED_QTY_MMBTU = market_risk.getVSUPPLIED_QTY_MMBTU();
Vector VBALANCE_QTY_MMBTU = market_risk.getVBALANCE_QTY_MMBTU();
Vector VASSESSED_QTY_MMBTU =market_risk.getVASSESSED_QTY_MMBTU();
Vector VTAQ_CONFIGURED = market_risk.getVTAQ_CONFIGURED();
Vector VTAQ_REMARK = market_risk.getVTAQ_REMARK();
Vector VTAQ_DETAIL = market_risk.getVTAQ_DETAIL();
Vector VENTERED_BY = market_risk.getVENTERED_BY();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table class="table table-bordered table-hover" border="1">
		<tr>
			<td colspan="15" align="left"><font size="4"><b>TAQ Report (<%=from_dt%> - <%=to_dt %>) Generated on <%=sysdt %></b></font></td>
		</tr>	
	</table>
	<br>
	<table class="table table-bordered table-hover" border="1">
		<thead id="tbsearch">
			<tr>
				<th>Sr#</th>
				<th>Account</th>										
				<th>Counterparty</th>
				<th>Contract Type</th>
				<th>Contract#</th>
				<th>Contract/Trade Ref#</th>
				<th>Contract Period</th>
				<th>Contractual DCQ</th>
				<th>Contractual TCQ</th>
				<th>Supplied Quantity (MMBTU)</th>										
				<th>Balanced Quantity (MMBTU)</th>										
				<th>TAQ Details</th>
				<th>TAQ Remarks</th>
				<th>TAQ Last Updated by</th>
				<th>TAQ (Supplied MMBTU incld.)</th>
			</tr>
		</thead>
		<tbody>
		<%if(VCOUNTERPARTY_CD.size()>0){ %>
			<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
			<tr>
				<td align="center"><%=i+1%></td>
				<td align="center">
					<span
					<%if(VACCOUNT.elementAt(i).equals("Buy")){ %>
									class="alert" style="background: #ffccff; color: #cc00cc;"
								<%}else { %>
									class="alert alert-primary"
								<%}%>><b><%=VACCOUNT.elementAt(i)%></b></span>
							</td>
				<td title="<%=VCOUNTERPARTY_ABBR.elementAt(i)%>"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
				<td><%=VDIS_CONTRACT_TYPE.elementAt(i)%> </td>
				<td align="center"><%=VDISPLAY_DEAL_MAP.elementAt(i)%></td>
				<td><%=VCONT_REF_NO.elementAt(i)%></td>
				<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
				<td align="right"><%=VDCQ.elementAt(i) %></td>
				<td align="right"><%=VTCQ.elementAt(i)%></td>
				<td align="right"><%=VSUPPLIED_QTY_MMBTU.elementAt(i)%></td>
				<td align="right"><%=VBALANCE_QTY_MMBTU.elementAt(i)%></td>															
				<td><%=VTAQ_DETAIL.elementAt(i).toString().replaceAll("<br>", "\r") %></td>
				<td><%=VTAQ_REMARK.elementAt(i).toString().replaceAll("<br>", "\r") %></td>
				<td align="center"><%=VENTERED_BY.elementAt(i).toString().replaceAll("<br>", "\r") %></td>
				<td align="right"><%=VASSESSED_QTY_MMBTU.elementAt(i) %></td>	
			</tr>
			<%} %>
			<%}else{ %>
				<tr>
					<td colspan="15">
						<div align="center"><%=utilmsg.infoMessage("<b>No Exposure for the Report Date!</b>")%></div>
					</td>
				</tr>
			<%} %>		
		</tbody>
	</table>						
</body>
</html>