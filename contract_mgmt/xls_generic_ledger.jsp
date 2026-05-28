<%@page import= "java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String yestdate = utildate.getPreviousDate();
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

String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");

String st_dt = request.getParameter("st_dt")==null?"":request.getParameter("st_dt");
String en_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");


cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setCounterparty_cd(counterparty_cd);
cont_mgmt.setAgmt_no(agmt_no);
cont_mgmt.setAgmt_rev_no(agmt_rev_no);
cont_mgmt.setCont_no(cont_no);
cont_mgmt.setCont_rev_no(cont_rev_no);
cont_mgmt.setContract_type(contract_type);
cont_mgmt.setCallFlag("GENERIC_LEDGER");
cont_mgmt.init();

Vector VMST_COUNTERPARTY_CD = cont_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = cont_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = cont_mgmt.getVMST_COUNTERPARTY_ABBR();

Vector VGAS_DT = cont_mgmt.getVGAS_DT();
Vector VDCQ = cont_mgmt.getVDCQ();
Vector VCOLOR_ALLOC = cont_mgmt.getVCOLOR_ALLOC();
Vector VPRICE = cont_mgmt.getVPRICE();
Vector VPRICE_UNIT = cont_mgmt.getVPRICE_UNIT();
Vector VEXCHG_RATE = cont_mgmt.getVEXCHG_RATE();
Vector VLC = cont_mgmt.getVLC();
Vector VBG = cont_mgmt.getVBG();
Vector VADVANCE_AMT = cont_mgmt.getVADVANCE_AMT();
Vector VCASH_DEPOSIT = cont_mgmt.getVCASH_DEPOSIT();
Vector VAPPROVED_EXCEED_VAL = cont_mgmt.getVAPPROVED_EXCEED_VAL();
Vector VTOTAL_ALLOWABLE_CREDIT = cont_mgmt.getVTOTAL_ALLOWABLE_CREDIT();

Vector VGROSS_AMT = cont_mgmt.getVGROSS_AMT();
Vector VOTHER_CHRG_AMT = cont_mgmt.getVOTHER_CHRG_AMT();
Vector VTAX_AMT = cont_mgmt.getVTAX_AMT();
Vector VTOTAL_GROSS_AMT = cont_mgmt.getVTOTAL_GROSS_AMT();
Vector VNET_AMT = cont_mgmt.getVNET_AMT();
Vector VCUMULATIVE_NET_AMT = cont_mgmt.getVCUMULATIVE_NET_AMT();
Vector VTDS_AMT  = cont_mgmt.getVTDS_AMT();
Vector VPAY_RECEIVED_AMT = cont_mgmt.getVPAY_RECEIVED_AMT();
Vector VCUMULATIVE_PAY_RECEIVED_AMT = cont_mgmt.getVCUMULATIVE_PAY_RECEIVED_AMT();
Vector VBALANCE_AMT = cont_mgmt.getVBALANCE_AMT();


Vector VQTY_INFO = cont_mgmt.getVQTY_INFO();
Vector VADV_INFO  = cont_mgmt.getVADV_INFO();
Vector VADV_ADJUST = cont_mgmt.getVADV_ADJUST();
Vector VHOLD_AMT = cont_mgmt.getVHOLD_AMT();
Vector VPAY_TOTAL = cont_mgmt.getVPAY_TOTAL();
Vector VADV_ADJ_TOTAL = cont_mgmt.getVADV_ADJ_TOTAL();
Vector VCUMULATIVE_ADV_ADJ_AMT = cont_mgmt.getVCUMULATIVE_ADV_ADJ_AMT();

String cont_ref_no=cont_mgmt.getCont_ref_no();
String dealDisplayMap=cont_mgmt.getDealDisplayMap();
String counterparty_nm=cont_mgmt.getCounterparty_nm();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="29" rowspan="1" align="left"><font size="6">Generic Ledger</font></th>
		</tr>
	</table>
	<br>
	<table  width="100%" border="1">
		<tr>
			<th colspan="29" rowspan="1" align="left">Counterparty : <%=counterparty_nm %><br>Deal Map : <%=dealDisplayMap%><br>Cont/Trade Ref : <%=cont_ref_no%><br>Contract Duration : <%=st_dt%> - <%=en_dt%></textarea>				    				
			</th>
		</tr>
	</table>
	<br>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th rowspan="2">Gas Date</th>
				<th rowspan="2">Exchange Rate<br>(INR/USD)</th>
				<th colspan="8">Allowable Credit (INR)</th>
				<th colspan="8">Consumed Credit | Exposure (INR)</th>											
				<th colspan="5">Payment Received Details (INR)</th>
				<th colspan="4">Advance Adjustment Details (INR)</th>
				<th rowspan="2">Balance (INR)</th>																						
				<th rowspan="2">Rule</th>
			</tr>					
			<tr>
				<th>Credit Exceed Days</th>
				<th>LC</th>
				<th>BG</th>
				<th>Cash Deposit</th>
				<th>Advance</th>
				<th>PCG</th>
				<th>Limit</th>
				<th>Total</th>
				<th>Qty</th>
				<th>Gas Price /MMBTU</th>
				<th>Gross Amount</th>
				<th title="Weighted Avg. of applicable Transportation Charges, Marketing Margin, Other Charges">Eff. Other Charges</th>
				<th>Total Gross Amount</th>
				<th>Tax</th>
				<th>Net Amount</th>
				<th>Cumulative<br>Net Amount</th>
				<th title="Nutralizing on first payment received">TDS</th>
				<th>Payment Received</th>
				<th>Advance Adj. Payment</th>
				<th>Total Recv. Payment</th>
				<th>Cumulative<br>Recv. Payment</th>
				<th>Hold Amount</th>												
				<th>Advance Adj. Payment</th>
				<th title="= Payment Adjusted + Hold Amount">Total Adj. Advance</th>
				<th>Cumulative<br>Adj. Advance</th>										
			</tr>
		</thead>
		<tbody>
		<%if(VGAS_DT.size() > 0){ %>
			<%for(int i=0; i<VGAS_DT.size(); i++){ %>
			<tr <%if(VGAS_DT.elementAt(i).equals(yestdate)){%> style="background: #a6ff4d;" <%}%>>										
				<td align="center"><%=VGAS_DT.elementAt(i)%></td>
				<td align="right"><%=VEXCHG_RATE.elementAt(i)%></td>
				<td align="right"><%=VAPPROVED_EXCEED_VAL.elementAt(i)%></td>
				<td align="right"><%=VLC.elementAt(i)%></td>
				<td align="right"><%=VBG.elementAt(i)%></td>
				<td align="right"><%=VCASH_DEPOSIT.elementAt(i) %></td>
				<td align="right" title="<%=VADV_INFO.elementAt(i)%>"><%=VADVANCE_AMT.elementAt(i) %></td>
				<td></td>
				<td></td>
				<td align="right" style="font-weight: bold;"><%=VTOTAL_ALLOWABLE_CREDIT.elementAt(i)%></td>
				<td align="right" title="<%=VQTY_INFO.elementAt(i)%>" style="color: <%=VCOLOR_ALLOC.elementAt(i)%>;"><%=VDCQ.elementAt(i)%></td>
				<td align="right"><%=VPRICE.elementAt(i)%> <%=VPRICE_UNIT.elementAt(i)%></td>
				<td align="right"><%=VGROSS_AMT.elementAt(i)%></td>
				<td align="right"><%=VOTHER_CHRG_AMT.elementAt(i)%></td>
				<td align="right"><%=VTOTAL_GROSS_AMT.elementAt(i)%></td>
				<td align="right"><%=VTAX_AMT.elementAt(i)%></td>
				<td align="right"><%=VNET_AMT.elementAt(i)%></td>
				<td align="right" style="font-weight: bold;"><%=VCUMULATIVE_NET_AMT.elementAt(i)%></td>
				<td align="right"><%=VTDS_AMT.elementAt(i)%></td>
				<td align="right"><%=VPAY_RECEIVED_AMT.elementAt(i) %></td>
				<td align="right"><%=VADV_ADJUST.elementAt(i)%></td>
				<td align="right"><%=VPAY_TOTAL.elementAt(i) %></td>
				<td align="right" style="font-weight: bold;"><%=VCUMULATIVE_PAY_RECEIVED_AMT.elementAt(i)%></td>
				<td align="right"><%=VHOLD_AMT.elementAt(i) %></td>
				<td align="right"><%=VADV_ADJUST.elementAt(i)%></td>																						
				<td align="right"><%=VADV_ADJ_TOTAL.elementAt(i)%></td>											
				<td align="right" style="font-weight: bold;"><%=VCUMULATIVE_ADV_ADJ_AMT.elementAt(i) %></td>
				<td align="right" style="font-weight: bold; <%if(VBALANCE_AMT.elementAt(i).toString().contains("-") ){%> color: red; <%}else{%>color: green;<%}%>"><%=VBALANCE_AMT.elementAt(i)%></td>
				<td></td>
			</tr>
			<%} %>
		<%} %>
		</tbody>
	</table>
</body>
</html>