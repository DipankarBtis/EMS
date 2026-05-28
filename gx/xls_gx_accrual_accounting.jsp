<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.gx.DataBean_Gx_Invoice" id="gx" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}
String owner_abbr="";
if(session.getAttribute("comp_abbr")==null||session.getAttribute("comp_abbr")==""||session.getAttribute("comp_abbr").toString().equals("null"))
{
	owner_abbr="";
}  
else
{
	owner_abbr=""+session.getAttribute("comp_abbr");
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
gx.setCallFlag("GX_ACCRUAL_ACCOUNTING");
gx.setComp_cd(owner_cd);
gx.setReport_dt(report_dt);
//gx.setMonth(month);
//gx.setYear(year);
gx.setCounterparty_cd(counterparty_cd);
gx.setCont_mapp(contract_map);
gx.setAutomation_flag(automationFlag);
gx.setIsGenerateXML(isGenerateXML);
gx.setRequest(request);
gx.init();

Vector VCOUNTERPTY_CD = gx.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = gx.getVCOUNTERPTY_ABBR();
Vector VCOUNTERPTY_NM = gx.getVCOUNTERPTY_NM();
Vector VPRODUCTION_MONTH = gx.getVPRODUCTION_MONTH();
Vector VSTART_DT = gx.getVSTART_DT();
Vector VEND_DT = gx.getVEND_DT();
Vector VGX_BU_PLANT_SEQ = gx.getVGX_BU_PLANT_SEQ();
Vector VGX_BU_PLANT_ABBR = gx.getVGX_BU_PLANT_ABBR();
Vector VBU_PLANT_SEQ = gx.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = gx.getVBU_PLANT_ABBR();
Vector VDIS_CONT_MAPPING = gx.getVDIS_CONT_MAPPING();
Vector VCONT_REF_NO = gx.getVCONT_REF_NO();
Vector VINVOICE_DUE_DT = gx.getVINVOICE_DUE_DT();
Vector VACCRUAL_QTY = gx.getVACCRUAL_QTY();
Vector VACCRUAL_AMT = gx.getVACCRUAL_AMT();
Vector VMST_COUNTERPARTY_CD = gx.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = gx.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = gx.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_MAP_LIST = gx.getVCONT_MAP_LIST();
Vector VDIS_CONT_MAP_LIST = gx.getVDIS_CONT_MAP_LIST();
Vector VGROSS_AMT = gx.getVGROSS_AMT();
Vector VGX_COUNTERPTY_ABBR = gx.getVGX_COUNTERPTY_ABBR();
Vector VGX_COUNTERPTY_CD = gx.getVGX_COUNTERPTY_CD();
Vector VBUY_SELL = gx.getVBUY_SELL();
Vector VCONTRACT_TYPE_NM = gx.getVCONTRACT_TYPE_NM();

Vector VTAX_STRUCT_DTL = gx.getVTAX_STRUCT_DTL();
Vector VTAX_AMT = gx.getVTAX_AMT();
Vector VTAX_INFO = gx.getVTAX_INFO();
Vector VTOTAL_ACCRUAL_AMT = gx.getVTOTAL_ACCRUAL_AMT();

String str_tot_accrual_mmbtu = gx.getStr_tot_accrual_mmbtu();
String str_tot_accrual_amt = gx.getStr_tot_accrual_amt();
String str_accrual_tax_amt = gx.getStr_accrual_tax_amt();
String str_total_accrual_amt = gx.getStr_total_accrual_amt();
String fileName =owner_abbr+"_TransactionChargeAccrualReport.xls";

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<span style="font-weight: bold;font-size: 40px;"><%=owner_abbr%> Transaction Charge Accrual Report for <%=report_dt%></span>
	<br><br>
	<table width="100%" border="1">
		<thead>
			<tr>	
				<th>Sr#</th>									
				<th>On behalf of Counterparty</th>
				<th>Gas Exchange</th>
				<th>Buy/Sell</th>
				<th>Contract Type</th>
				<th>Contract#</th>
				<th>Contract/Trade Ref#</th>
				<th>Contract Period</th>
				<th>Gx Business Unit</th>
				<th>Business Unit</th>
				<th>Cash Flow</th>
				<th>Production Month</th>
				<th>Invoice Due Date</th>
				<th>Accrual MMBTU</th>
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
				<td align="right"><%=i+1 %></td>
				<td align="left"><%=VCOUNTERPTY_NM.elementAt(i)%></td>
				<td align="center"><%=VGX_COUNTERPTY_ABBR.elementAt(i)%></td>
				<td align="center"><%=VBUY_SELL.elementAt(i)%></td>
				<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i)%></td>
				<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i) %>	</td>
				<td align="center"><%=VCONT_REF_NO.elementAt(i)%></td>
				<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
				<td align="center"><%=VGX_BU_PLANT_ABBR.elementAt(i)%></td>
				<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
				<td align="center">Service Charge</td>
				<td align="center"><%=VPRODUCTION_MONTH.elementAt(i) %></td>										
				<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
				<td align="right"><%=VACCRUAL_QTY.elementAt(i) %></td>
				<td align="right"><%=VACCRUAL_AMT.elementAt(i)%></td>
				<td align="center"><%=VTAX_STRUCT_DTL.elementAt(i) %></td>
				<td align="right" title="<%=VTAX_INFO.elementAt(i)%>"><%=VTAX_AMT.elementAt(i) %></td>
				<td align="right"><%=VTOTAL_ACCRUAL_AMT.elementAt(i) %></td>
			</tr>
			<%} %>
		<%}else{ %>
			<tr>
				<td align="center" colspan="18"><b>No Accrual Data Available!</b></td>
			</tr>
		<%} %>
			<tr style="font-weight: bold;">
				<td colspan="13" align="right">Total : </td>
				<td align="right"><%=str_tot_accrual_mmbtu %></td>
				<td align="right"><%=str_tot_accrual_amt %></td>
				<td align="right"><%//=str_tot_accrual_mmbtu %></td>
				<td align="right"><%=str_accrual_tax_amt %></td>
				<td align="right"><%=str_total_accrual_amt%></td>
			</tr>
		</tbody>
	</table>
</body>
</html>