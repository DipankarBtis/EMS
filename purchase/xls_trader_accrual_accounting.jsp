<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="purchase" scope="request"></jsp:useBean>
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
purchase.setCallFlag("PURCHASE_ACCRUAL_ACCOUNTING");
purchase.setComp_cd(owner_cd);
purchase.setReport_dt(report_dt);
purchase.setMonth(month);
purchase.setYear(year);
purchase.setCounterparty_cd(counterparty_cd);
purchase.setCont_mapp(contract_map);
purchase.setAutomation_flag(automationFlag);
purchase.setIsGenerateXML(isGenerateXML);
purchase.setRequest(request);
purchase.init();

Vector VCOUNTERPTY_CD = purchase.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = purchase.getVCOUNTERPTY_ABBR();
Vector VCOUNTERPTY_NM = purchase.getVCOUNTERPTY_NM();
Vector VPRODUCTION_MONTH = purchase.getVPRODUCTION_MONTH();
Vector VSTART_DT = purchase.getVSTART_DT();
Vector VEND_DT = purchase.getVEND_DT();
Vector VPLANT_SEQ = purchase.getVPLANT_SEQ();
Vector VPLANT_ABBR = purchase.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = purchase.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = purchase.getVBU_PLANT_ABBR();
Vector VPERIOD_START_DT = purchase.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = purchase.getVPERIOD_END_DT();
Vector VDIS_CONT_MAPPING = purchase.getVDIS_CONT_MAPPING();
Vector VCONT_REF_NO = purchase.getVCONT_REF_NO();
Vector VBILLING_FREQ_NM = purchase.getVBILLING_FREQ_NM();
Vector VINVOICE_DUE_DT = purchase.getVINVOICE_DUE_DT();
Vector VACCRUAL_QTY = purchase.getVACCRUAL_QTY();
Vector VACCRUAL_AMT = purchase.getVACCRUAL_AMT();
Vector VMST_COUNTERPARTY_CD = purchase.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = purchase.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = purchase.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_MAP_LIST = purchase.getVCONT_MAP_LIST();
Vector VDIS_CONT_MAP_LIST = purchase.getVDIS_CONT_MAP_LIST();
Vector VSALES_PRICE_NM = purchase.getVSALES_PRICE_NM();
Vector VGROSS_AMT = purchase.getVGROSS_AMT();
Vector VEXCHNG_RATE = purchase.getVEXCHNG_RATE();
Vector VSPLIT_FLAG = purchase.getVSPLIT_FLAG();
Vector VSPLIT_VALUE = purchase.getVSPLIT_VALUE();
Vector VBOE_NO = purchase.getVBOE_NO();
Vector VBOE_NM = purchase.getVBOE_NM();
Vector VINV_FLAG = purchase.getVINV_FLAG();

Vector VTAX_STRUCT_DTL = purchase.getVTAX_STRUCT_DTL();
Vector VTAX_AMT = purchase.getVTAX_AMT();
Vector VTAX_INFO = purchase.getVTAX_INFO();
Vector VTOTAL_ACCRUAL_AMT = purchase.getVTOTAL_ACCRUAL_AMT();

Vector VQTY_UNIT = purchase.getVQTY_UNIT();
Vector VQTY_UNIT_NM = purchase.getVQTY_UNIT_NM();

String str_tot_accrual_mmbtu = purchase.getStr_tot_accrual_mmbtu();
String str_tot_accrual_amt = purchase.getStr_tot_accrual_amt();
String str_tot_accrual_amt_usd = purchase.getStr_tot_accrual_amt_usd();
String str_accrual_tax_amt = purchase.getStr_accrual_tax_amt();
String str_total_accrual_amt = purchase.getStr_total_accrual_amt();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="20" rowspan="2" align="left">Accounting Purchase Accrual Report for <%=report_dt%></th>
		</tr>
	</table >
	<table width="100%" border="1">
		<thead>
			<tr>	
				<th>Sr#</th>									
				<th>Customer</th>
				<th>Contract No</th>
				<th>Contract/Trade Ref#</th>
				<th>Contract Period</th>
				<th>Plant</th>
				<th>Business Unit</th>
				<th>Cash Flow</th>
				<th>Production Month</th>
				<th>Billing Cycle</th>
				<th>Billing Period</th>
				<th>Invoice Due Date</th>
				<th>Quantity Unit</th>
				<th>Accrual Quantity</th>
				<th>Accrual Amount <br> (USD)</th>
				<th>Exchage Rate </th>
				<th>Gross Amount <br> (INR)</th>
				<th>Tax Structure</th>
				<th>Tax Amount <br> (INR)</th>
				<th>Accrual Amount <br> (INR)</th>
			</tr>
		</thead>
		<tbody>
		<%if(VCOUNTERPTY_CD.size() > 0){ %>
			<%for(int i=0; i<VCOUNTERPTY_CD.size(); i++){ %>
			<tr>
				<td><%=i+1%></td>
				<td align="center" title="<%=VCOUNTERPTY_NM.elementAt(i)%>">
					<%=VCOUNTERPTY_ABBR.elementAt(i)%>
				</td>
				<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i) %>											
					<%if(VSPLIT_FLAG.elementAt(i).equals("Y")){ %>
							<font style="background:#ff99ff;">[Split <%=VSPLIT_VALUE.elementAt(i)%>%]</font>
					<%} %>	
					<%if(!VBOE_NO.elementAt(i).equals("0")){ %>
						<font style="background:#ff99ff;"><%=VBOE_NM.elementAt(i)%></font>
						<%if(VINV_FLAG.elementAt(i).equals("F")){ %>
							<font style="background:#ccff99;">Final</font>
						<%} %>
					<%} %>										
				</td>
				<td align="center">
					<%=VCONT_REF_NO.elementAt(i) %>
				</td>
				<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
				<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
				<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
				<td align="center">Commodity</td>
				<td align="center"><%=VPRODUCTION_MONTH.elementAt(i) %></td>
				<td align="center">
					<span><b><%=VBILLING_FREQ_NM.elementAt(i)%></b></span>																								
				</td>
				<td align="center">
					<%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%>
				</td>
				<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
				<td align="center"><%=VQTY_UNIT_NM.elementAt(i) %></td>
				<td align="right"><%=VACCRUAL_QTY.elementAt(i) %></td>
				<td align="right"><%if (VSALES_PRICE_NM.elementAt(i).equals("USD")){ %><%=VACCRUAL_AMT.elementAt(i)%><%}%></td>
				<td align="right"><%if (VSALES_PRICE_NM.elementAt(i).equals("USD") && VEXCHNG_RATE.elementAt(i).equals("0.00")){ %>
						<span style="color:red">Missing Exchange Rate</span>						
					<%} else {%><%=VEXCHNG_RATE.elementAt(i)%><%}%></td>
				<td align="right"><%if (!VACCRUAL_AMT.elementAt(i).equals("0.00") && VGROSS_AMT.elementAt(i).equals("0.00")){ %>
						<span style="color:red">Error</span>						
					<%} else {%><%=VGROSS_AMT.elementAt(i) %><%}%></td>
				<td align="center"><%=VTAX_STRUCT_DTL.elementAt(i) %></td>
				<td align="right" title="<%=VTAX_INFO.elementAt(i)%>"><%=VTAX_AMT.elementAt(i) %></td>
				<td align="right"><%=VTOTAL_ACCRUAL_AMT.elementAt(i) %></td>
			</tr>
			<%} %>
		<%}else{ %>
			<tr>
				<td align="center" colspan="20"><b>No Accrual Data Available!</b></td>
			</tr>
		<%} %>
			<tr style="font-weight: bold;">
				<td colspan="13" align="right">Total : </td>
				<td align="right"><%=str_tot_accrual_mmbtu %></td>
				<td align="right"><%=str_tot_accrual_amt_usd %></td>
				<td align="right"><%//=str_tot_accrual_mmbtu %></td>
				<td align="right"><%=str_tot_accrual_amt %></td>
				<td align="right"><%//=str_tot_accrual_mmbtu %></td>
				<td align="right"><%=str_accrual_tax_amt %></td>
				<td align="right"><%=str_total_accrual_amt%></td>
			</tr>
		</tbody>
	</table>
</body>
</html>