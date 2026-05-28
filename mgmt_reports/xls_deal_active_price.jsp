<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>

<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_DealActivePrice" id="db_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getPreviousDate();

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String action=request.getParameter("action")==null?"list":request.getParameter("action");
String rlng_dlng=request.getParameter("rlng_dlng")==null?"0":request.getParameter("rlng_dlng");
String buy_sell=request.getParameter("but_sell")==null?"0":request.getParameter("buy_sell");
String rpt_dt=request.getParameter("rpt_dt")==null?sysdate:request.getParameter("rpt_dt");
String chk_update_flag = request.getParameter("chk_update_flag")==null ? "" : request.getParameter("chk_update_flag");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String curMthYr=utildate.getMonthName(rpt_dt)+"-"+utildate.getCurrentYear(rpt_dt);

String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}
db_mgmt.setCallFlag("ACTIVE_DEAL_PRICE");
db_mgmt.setComp_cd(owner_cd);
db_mgmt.setCounterparty_cd(counterparty_cd);
db_mgmt.setReport_dt(rpt_dt);
db_mgmt.setAction(action);
db_mgmt.setChkUpdateFlag(chk_update_flag);
db_mgmt.setEmpCD(session.getAttribute("emp_cd").toString());
db_mgmt.init();


Vector VMST_COUNTERPARTY_CD = db_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = db_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = db_mgmt.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_NM = db_mgmt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_CD = db_mgmt.getVCOUNTERPARTY_CD();
Vector VCONTRACT_TYPE = db_mgmt.getVCONTRACT_TYPE();
Vector VCOUNTERPARTY_ABBR = db_mgmt.getVCOUNTERPARTY_ABBR();
Vector VDEAL_TYPE = db_mgmt.getVDEAL_TYPE();
Vector VCONT_PERIOD = db_mgmt.getVCONT_PERIOD();
Vector VCONT_STATUS = db_mgmt.getVCONT_STATUS();
Vector VPRICE_TYPE = db_mgmt.getVPRICE_TYPE();
Vector VAPPROVED_RATE = db_mgmt.getVAPPROVED_RATE();
Vector VAPPLICABLE_RATE = db_mgmt.getVAPPLICABLE_RATE();
Vector VTRADE_DT = db_mgmt.getVTRADE_DT();
Vector VBUY_SELL = db_mgmt.getVBUY_SALE();
Vector VAWAITING_RATE = db_mgmt.getVAWAITING_RATE();
Vector VDEAL_NUMBER = db_mgmt.getVDEAL_NUMBER();
Vector VDEAL_REF = db_mgmt.getVDEAL_REF();
Vector VFIN_CURV = db_mgmt.getVFIN_CURV();
Vector VPHY_CURV = db_mgmt.getVPHY_CURV();
Vector VAGMT_NO = db_mgmt.getVAGMT_NO();
Vector VAGMT_REV = db_mgmt.getVAGMT_REV();
Vector VCONT_NO = db_mgmt.getVCONT_NO();
Vector VCONT_REV = db_mgmt.getVCONT_REV();
Vector VCARGO_NO = db_mgmt.getVCARGO_NO();
Vector VAPPROVED_DATE = db_mgmt.getVAPPROVED_DATE();
Vector VRATE_EFF_DT = db_mgmt.getVRATE_EFF_DT();
Vector VBILL_FREQ = db_mgmt.getVBILL_FREQ();
Vector VBILL_FREQ_DT = db_mgmt.getVBILL_FREQ_DT();
Vector VPriceAppl = db_mgmt.getVPriceAppl();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="15" rowspan="2" align="center" style="font-size: 16px;"><b>DEAL ACTIVE PRICE (<%=rpt_dt %>)</b></th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th align="center">Sr#</th>
				<th align="center">Counterparty</th>
				<th align="center">Deal Id#/Ref</th>
				<th align="center">Buy-Sell/Deal Type</th>
				<th align="center">Contract Period</th>
				<th align="center">Price Type / Billing</th>
				<th align="center">Approved Price (MMBTU)</th>
				<th align="center">Awaiting for Approval</th>
				<th align="center">Applicable Price</th>
				<th align="center">Financial Curve</th>
			</tr>
		</thead>
		<tbody>
			<%if(VCOUNTERPARTY_CD.size()>0){
				for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
					<tr>
					<td align="left"><%=i+1 %>&nbsp;</td>
					<td align="left"><%=VCOUNTERPARTY_ABBR.elementAt(i)%>-<%=VCOUNTERPARTY_NM.elementAt(i)%></td>
					<td align="center"><%=VDEAL_NUMBER.elementAt(i)%><BR><%=VDEAL_REF.elementAt(i)%></td>
					<td align="left"><b><%=VBUY_SELL.elementAt(i)%></b>&nbsp;<%=VDEAL_TYPE.elementAt(i)%></td>
					<td align="center"><%=VCONT_PERIOD.elementAt(i)%></td>
					<td align="center"><B><%=VPRICE_TYPE.elementAt(i)%></B></span><BR><%=VBILL_FREQ.elementAt(i) %></td>
					<td align="center"><B><%=VAPPROVED_RATE.elementAt(i)%></B><BR><%=VAPPROVED_DATE.elementAt(i)%></td>
					<td align="right"><%=VAWAITING_RATE.elementAt(i)%></td>
					<td align="right"><%=VAPPLICABLE_RATE.elementAt(i)%><BR><%=VRATE_EFF_DT.elementAt(i) %></td>
					<td align="right"><%=VFIN_CURV.elementAt(i)%></td>
					</tr>
					<%} %>
					
					<%}else{ %>
					<tr>
					<td colspan=10 align="center"><%=utilmsg.infoMessage("<b>No Data Available for Active Deal Pricing!</b>") %></td>
					</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>