<%@page import= "java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Report" id="derv_contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sysdate=utildate.getSysdate();
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

derv_contract.setCallFlag("DERV_CONTRACT_SUMMARY");
derv_contract.setComp_cd(owner_cd);
derv_contract.setCounterparty_cd(counterparty_cd);
derv_contract.setFrom_dt(from_dt);
derv_contract.setTo_dt(to_dt);
derv_contract.init();

Vector VCOUNTERPARTY_CD = derv_contract.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = derv_contract.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = derv_contract.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = derv_contract.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = derv_contract.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = derv_contract.getVMST_COUNTERPARTY_ABBR();
Vector VQTY = derv_contract.getVQTY();
Vector VQTY_UNIT = derv_contract.getVQTY_UNIT();
Vector VSIGNING_DT = derv_contract.getVSIGNING_DT();
Vector VSTART_DT = derv_contract.getVSTART_DT();
Vector VEND_DT = derv_contract.getVEND_DT();
Vector VRATE = derv_contract.getVRATE();
Vector VRATE_UNIT = derv_contract.getVRATE_UNIT();
Vector VDISP_DEAL_ID = derv_contract.getVDISP_DEAL_ID();
Vector VCONT_REF = derv_contract.getVCONT_REF();
Vector VENT_DT = derv_contract.getVENT_DT();
Vector VENT_BY = derv_contract.getVENT_BY();
Vector VPRICE_TYPE = derv_contract.getVPRICE_TYPE();
Vector VPLANT_UNIT = derv_contract.getVPLANT_UNIT();
Vector BU_PLANT_NM = derv_contract.getVBU_PLANT();
Vector VINDEX = derv_contract.getVINDEX();
Vector VDUE_DATE = derv_contract.getVDUE_DATE();
Vector VEXCHANGE_RATE = derv_contract.getVEXCHANGE_RATE();
Vector VEXCHNG_RATE_NM = derv_contract.getVEXCHNG_RATE_NM();
Vector VTRADE_DT = derv_contract.getVTRADE_DT();
Vector VTOTAL_QTY = derv_contract.getVTOTAL_QTY();
Vector VBUY_SELL = derv_contract.getVBUY_SELL();
Vector VCURVE_NM = derv_contract.getVCURVE_NM();
Vector VPROD_NM = derv_contract.getVPROD_NM();
Vector VCONV_FACTOR = derv_contract.getVCONV_FACTOR();
Vector VDUE_DATE_IN = derv_contract.getVDUE_DATE_IN();
Vector VMONTH_YEAR = derv_contract.getVMONTH_YEAR();
Vector VHOLIDAY_STATE = derv_contract.getVHOLIDAY_STATE();
Vector VINSTRUMENT_STATUS = derv_contract.getVINSTRUMENT_STATUS();

String totalQty = derv_contract.getTotalQty();

%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="24" rowspan="" align="left">Derivatives Contract Summary Report (Generated For <%=from_dt%> To <%=to_dt%>)</th>
		</tr>
	</table>
	<table class="table table-bordered table-hover" width="100%" border="1">
		<thead>
			<tr>
				<th>Sr No</th>
				<th>Counterparty Name</th>
				<th>Contract Number</th>
				<th>Contract Ref.</th>
				<th>Trade Date</th>
				<th>Trade Confirmation Date</th>
				<th>Contract Month/Year</th>
				<th>Contract Period</th>
				<th>Status</th>
				<th>Product</th>
				<th>Curve Name</th>
				<th>Buy/Sell</th>
				<th>Price Type</th>																			
				<th>Contract Price</th>
				<th>Currency</th>
				<th>Qty</th>
				<th>Qty Unit</th>
				<th>Payment Due Period(Days)</th>
				<th>Holiday State</th>
				<th>Conversion Factor</th>
				<th>Business Unit</th>
				<th>Customer Plants</th>
				<th>Contract Enter By</th>
				<th>Contract Enter Date</th>
			</tr>
		</thead>
		<tbody>
			<%int k=0;
			if(VCOUNTERPARTY_CD.size() > 0){ %>
				<%for(int i=0;i<VCOUNTERPARTY_CD.size(); i++){ 
				k+=1;
				%>
					<tr>
						<td align="center"><%=k%></td>
						<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
						<td align="center"><%=VDISP_DEAL_ID.elementAt(i)%></td>
						<td align="center"><%=VCONT_REF.elementAt(i)%></td>
						<td align="center"><%=VTRADE_DT.elementAt(i)%></td>
						<td align="center"><%=VSIGNING_DT.elementAt(i)%></td>
						<td align="center"><%=VMONTH_YEAR.elementAt(i) %></td>
						<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
						<td align="center"><%=VINSTRUMENT_STATUS.elementAt(i) %></td>
						<td align="center"><%=VPROD_NM.elementAt(i) %></td>
						<td align="center"><%=VCURVE_NM.elementAt(i) %></td>
						<td align="center"><%=VBUY_SELL.elementAt(i) %></td>
						<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
						<td align="right"><%=VRATE.elementAt(i)%></td>
						<td align="center"><%=VRATE_UNIT.elementAt(i)%></td>
						<td align="right"><%=VQTY.elementAt(i)%></td>
						<td align="center"><%=VQTY_UNIT.elementAt(i)%></td>
						<td><%=VDUE_DATE.elementAt(i) %> <%=VDUE_DATE_IN.elementAt(i) %></td>
						<td><%=VHOLIDAY_STATE.elementAt(i) %></td>
						<td align="right"><%=VCONV_FACTOR.elementAt(i) %></td>
						<td><%=BU_PLANT_NM.elementAt(i) %></td>
						<td><%=VPLANT_UNIT.elementAt(i) %></td>
						<td align="center"><%=VENT_BY.elementAt(i)%></td>
						<td align="center"><%=VENT_DT.elementAt(i)%></td>
					</tr>
				<%} %>
				<tr>
					<td colspan="15" align="right"><b>Total :</b></td>
					<td align="right"><b><%=totalQty %></b></td>
					<td colspan="8"></td>
				</tr>
			<%}else{ %>
				<tr>
					<td colspan="24" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
				</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>