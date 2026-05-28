<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Invoice" id="derv_accounting" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
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
String sysdate = utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();
String report_dt=request.getParameter("report_dt")==null?sysdate:request.getParameter("report_dt");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_map=request.getParameter("contract_map")==null?"":request.getParameter("contract_map");
String automationFlag=request.getParameter("automationFlag")==null?"":request.getParameter("automationFlag");
String isGenerateXML=request.getParameter("isGenerateXML")==null?"":request.getParameter("isGenerateXML");

if(month.length() == 1)
{
	month="0"+month; 
}
derv_accounting.setCallFlag("DERV_MTM_ACCRUAL_ACCOUNTING");
derv_accounting.setComp_cd(owner_cd);
derv_accounting.setReport_dt(report_dt);
derv_accounting.setMonth(month);
derv_accounting.setYear(year);
derv_accounting.setCounterparty_cd(counterparty_cd);
derv_accounting.setCont_mapp(contract_map);
derv_accounting.setAutomation_flag(automationFlag);
derv_accounting.setIsGenerateXML(isGenerateXML);
derv_accounting.setRequest(request);
derv_accounting.init();

String isFreezed=derv_accounting.getIsFreezed();
String eodProcessDoneOn=derv_accounting.getEodProcessDoneOn();

String tot_qty=derv_accounting.getTot_qty();
String tot_gross_amt =derv_accounting.getTot_gross_amt();
String tot_buy_amt=derv_accounting.getTot_buy_amt();
String tot_sell_amt=derv_accounting.getTot_sell_amt();

Vector VCOUNTERPTY_CD = derv_accounting.getVCOUNTERPARTY_CD();
Vector VCOUNTERPTY_ABBR = derv_accounting.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPTY_NM = derv_accounting.getVCOUNTERPARTY_NM();
Vector VPRODUCTION_MONTH = derv_accounting.getVPRODUCTION_MONTH();
Vector VSTART_DT = derv_accounting.getVCONT_START_DT();
Vector VEND_DT = derv_accounting.getVCONT_END_DT();
Vector VPLANT_SEQ = derv_accounting.getVPLANT_SEQ();
Vector VPLANT_ABBR = derv_accounting.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = derv_accounting.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = derv_accounting.getVBU_PLANT_ABBR();
Vector VPERIOD_START_DT = derv_accounting.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = derv_accounting.getVPERIOD_END_DT();
Vector VDEAL_MAPPING = derv_accounting.getVDEAL_MAPPING();
Vector VCONT_REF = derv_accounting.getVCONT_REF();
Vector VBILLING_FREQ = derv_accounting.getVBILLING_FREQ();
Vector VINVOICE_DUE_DT = derv_accounting.getVINVOICE_DUE_DT();
Vector VACCRUAL_QTY = derv_accounting.getVACCRUAL_QTY();
Vector VACCRUAL_AMT = derv_accounting.getVACCRUAL_AMT();
Vector VMST_COUNTERPARTY_CD = derv_accounting.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = derv_accounting.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = derv_accounting.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_MAP_LIST = derv_accounting.getVCONT_MAP_LIST();
Vector VDIS_CONT_MAP_LIST = derv_accounting.getVDIS_CONT_MAP_LIST();
Vector VSELL_PRICE_NM = derv_accounting.getVSELL_PRICE_NM();
Vector VGROSS_AMT = derv_accounting.getVGROSS_AMT();
Vector VEXCHNG_RATE = derv_accounting.getVEXCHNG_RATE();
Vector VCASH_FLOW = derv_accounting.getVCASH_FLOW();
Vector VQTY_UNIT = derv_accounting.getVQTY_UNIT();
Vector VBUY_AMT = derv_accounting.getVBUY_AMT();
Vector VSELL_AMT = derv_accounting.getVSELL_AMT();
Vector VDEAL_PROD_NM = derv_accounting.getVDEAL_PROD_NM();
Vector VBUY_SELL = derv_accounting.getVBUY_SELL();
Vector VFLOAT_PRICE = derv_accounting.getVFLOAT_PRICE();
Vector VFIXED_PRICE = derv_accounting.getVFIXED_PRICE();
Vector VMONTH = derv_accounting.getVMONTH();
Vector VYEAR = derv_accounting.getVYEAR();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="20" rowspan="2" align="left">Derivatives MTM Report for <%=report_dt%></th>
		</tr>
	</table >
	<table width="100%" border="1">
		<thead>
			<tr>										
				<th>Sr#</th>
				<th>Trader</th>
				<th>Contract No</th>
				<th>Contract/Trade Ref#</th>
				<th>BUY/SELL</th>
				<th>Product</th>
				<th>Month</th>
				<th>Year</th>
				<th>Plant</th>
				<th>Business Unit</th>
				<th>Cash Flow</th>
				<th>Production Month</th>
				<th>Invoice Due Date</th>
				<th>Fixed Price</th>
				<th>Float Price</th>
				<th>Accrual Qty</th>
				<th>Qty Unit</th>
				<th>Buy Amount</th>
				<th>Sell Amount</th>
				<th>Accrual Amount <br> (USD)</th>
			</tr>
		</thead>
		<tbody>
		<%if(VCOUNTERPTY_CD.size() > 0){ %>
			<%for(int i=0; i<VCOUNTERPTY_CD.size(); i++){ %>
			<tr>
				<td align="center"><%=i+1 %></td>
				<td align="center"><%=VCOUNTERPTY_ABBR.elementAt(i)%> - <%=VCOUNTERPTY_NM.elementAt(i)%></td>
				<td align="center"><%=VDEAL_MAPPING.elementAt(i) %></td>
				<td align="center"><%=VCONT_REF.elementAt(i) %></td>
				<td align="center"><%=VBUY_SELL.elementAt(i) %></td>
				<td align="center"><%=VDEAL_PROD_NM.elementAt(i) %></td>
				<td align="center"><%=VMONTH.elementAt(i) %></td>
				<td align="center"><%=VYEAR.elementAt(i) %></td>
				<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
				<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
				<td align="center"><%=VCASH_FLOW.elementAt(i)%></td>
				<td align="center"><%=VPRODUCTION_MONTH.elementAt(i) %></td>
				<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
				<td align="right"><%=VFIXED_PRICE.elementAt(i) %></td>
				<td align="right"><%=VFLOAT_PRICE.elementAt(i) %></td>
				<td align="right"><%=VACCRUAL_QTY.elementAt(i) %></td>
				<td align="right"><%=VQTY_UNIT.elementAt(i) %></td>
				<td align="right"><%=VBUY_AMT.elementAt(i) %></td>
				<td align="right"><%=VSELL_AMT.elementAt(i) %></td>
				<td align="right">
					<span style="color:<%if(Double.parseDouble(""+VGROSS_AMT.elementAt(i))<0){%>red<%}else{%>green<%}%>">
						<%=VGROSS_AMT.elementAt(i)%>
					</span>
				</td>
			</tr>
			<%} %>
			<tr>
				<td colspan="15" align="right"><b>Total:</b></td>
				<td align="right"><b><%=tot_qty %></b></td>
				<td></td>
				<td align="right"><b><%=tot_buy_amt %></b></td>
				<td align="right"><b><%=tot_sell_amt %></b></td>
				<td align="right"><b>
					<span style="color:<%if(Double.parseDouble(tot_gross_amt)<0){%>red<%}else{%>green<%}%>">
						<%=tot_gross_amt%>
					</span></b>
				</td>
			</tr>
		<%}else{ %>
			<tr>
				<td align="center" colspan="20"><b>No Accrual Data Available!</b></td>
			</tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>