<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.accounting.DataBean_Accounting" id="accounting" scope="request"></jsp:useBean>
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
accounting.setCallFlag("ACCRUAL_ACCOUNTING");
accounting.setComp_cd(owner_cd);
accounting.setReport_dt(report_dt);
accounting.setMonth(month);
accounting.setYear(year);
accounting.setCounterparty_cd(counterparty_cd);
accounting.setCont_mapp(contract_map);
accounting.setAutomation_flag(automationFlag);
accounting.setIsGenerateXML(isGenerateXML);
accounting.setRequest(request);
accounting.init();

Vector VCOUNTERPTY_CD = accounting.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = accounting.getVCOUNTERPTY_ABBR();
Vector VCOUNTERPTY_NM = accounting.getVCOUNTERPTY_NM();
Vector VPRODUCTION_MONTH = accounting.getVPRODUCTION_MONTH();
Vector VSTART_DT = accounting.getVSTART_DT();
Vector VEND_DT = accounting.getVEND_DT();
Vector VPLANT_SEQ = accounting.getVPLANT_SEQ();
Vector VPLANT_ABBR = accounting.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = accounting.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = accounting.getVBU_PLANT_ABBR();
Vector VPERIOD_START_DT = accounting.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = accounting.getVPERIOD_END_DT();
Vector VDIS_CONT_MAPPING = accounting.getVDIS_CONT_MAPPING();
Vector VCONT_REF_NO = accounting.getVCONT_REF_NO();
Vector VBILLING_FREQ_NM = accounting.getVBILLING_FREQ_NM();
Vector VINVOICE_DUE_DT = accounting.getVINVOICE_DUE_DT();
Vector VACCRUAL_QTY = accounting.getVACCRUAL_QTY();
Vector VACCRUAL_AMT = accounting.getVACCRUAL_AMT();
Vector VMST_COUNTERPARTY_CD = accounting.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = accounting.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = accounting.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_MAP_LIST = accounting.getVCONT_MAP_LIST();
Vector VDIS_CONT_MAP_LIST = accounting.getVDIS_CONT_MAP_LIST();
Vector VSALES_PRICE_NM = accounting.getVSALES_PRICE_NM();
Vector VGROSS_AMT = accounting.getVGROSS_AMT();
Vector VEXCHNG_RATE = accounting.getVEXCHNG_RATE();
Vector VCASH_FLOW = accounting.getVCASH_FLOW();

String str_tot_accrual_mmbtu = accounting.getStr_tot_accrual_mmbtu();
String str_tot_accrual_amt = accounting.getStr_tot_accrual_amt();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="16" rowspan="2" align="left">Accounting Sales Accrual Report for <%=report_dt%></th>
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
				<th>Accrual MMBTU</th>
				<th>Accrual Amount <br> (USD)</th>
				<th>Exchage Rate </th>
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
				<td align="center">
					<%=VDIS_CONT_MAPPING.elementAt(i) %>
				</td>
				<td align="center">
					<%=VCONT_REF_NO.elementAt(i) %>
				</td>
				<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
				<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
				<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
				<td align="center"><%=VCASH_FLOW.elementAt(i)%></td>
				<td align="center"><%=VPRODUCTION_MONTH.elementAt(i) %></td>
				<td align="center">
					<span><b><%=VBILLING_FREQ_NM.elementAt(i)%></b></span>																								
				</td>
				<td align="center">
					<%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%>
				</td>
				<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
				<td align="right"><%=VACCRUAL_QTY.elementAt(i) %></td>
				<td align="right"><%if (VSALES_PRICE_NM.elementAt(i).equals("USD")){ %><%=VACCRUAL_AMT.elementAt(i)%><%}%></td>
				<td align="right"><%if (VSALES_PRICE_NM.elementAt(i).equals("USD") && VEXCHNG_RATE.elementAt(i).equals("0.00")){ %>
						<span style="color:red">Missing Exchange Rate</span>						
					<%} else {%><%=VEXCHNG_RATE.elementAt(i)%><%}%></td>
				<td align="right"><%if (!VACCRUAL_AMT.elementAt(i).equals("0.00") && VGROSS_AMT.elementAt(i).equals("0.00")){ %>
						<span style="color:red">Error</span>						
					<%} else {%><%=VGROSS_AMT.elementAt(i) %><%}%></td>
			</tr>
			<%} %>
		<%}else{ %>
			<tr>
				<td align="center" colspan="16"><b>No Accrual Data Available!</b></td>
			</tr>
		<%} %>
			<tr style="font-weight: bold;">
				<td colspan="12" align="right">Total : </td>
				<td align="right"><%=str_tot_accrual_mmbtu %></td>
				<td align="right"><%//=str_tot_accrual_mmbtu %></td>
				<td align="right"><%//=str_tot_accrual_mmbtu %></td>
				<td align="right"><%=str_tot_accrual_amt %></td>
			</tr>
		</tbody>
	</table>
</body>
</html>