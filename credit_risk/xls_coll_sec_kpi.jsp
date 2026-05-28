
<%@page import="java.util.*"%>
<%@page import= "java.text.NumberFormat"%>
<%@page import= "java.text.DecimalFormat"%>
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
NumberFormat nf = new DecimalFormat("###########0.00");
NumberFormat nf2 = new DecimalFormat("###,###,###,##0.00");

String sysdate=utildate.getSysdate();
int curr_year=utildate.getCurrentYear();
String from_dt = request.getParameter("from_dt")==null?"01/01/"+curr_year:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?"31/12/"+curr_year:request.getParameter("to_dt");
String filter_security_type = request.getParameter("filter_security_type")==null?"0":request.getParameter("filter_security_type");
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

dbcredit.setCallFlag("KPI_REPORT");
dbcredit.setComp_cd(owner_cd);
dbcredit.setFilter_security_type(filter_security_type);
dbcredit.setFrom_dt(from_dt);
dbcredit.setTo_dt(to_dt);
dbcredit.init();

Vector VCOUNTERPARTY_NM = dbcredit.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcredit.getVCOUNTERPARTY_ABBR();
Vector VSEC_CATEGORY = dbcredit.getVSEC_CATEGORY();
Vector VSEC_TYPE = dbcredit.getVSEC_TYPE();
Vector VSEC_REF_NO = dbcredit.getVSEC_REF_NO();
Vector VVALUE = dbcredit.getVVALUE();
Vector VRECEIVED_DATE = dbcredit.getVRECEIVED_DATE();
Vector VMST_BANK_NM = dbcredit.getVMST_BANK_NM();
Vector VMST_BANK_CD = dbcredit.getVMST_BANK_CD();
Vector VMST_BANK_ABBR = dbcredit.getVMST_BANK_ABBR();
Vector VMST_BRANCH_NAME = dbcredit.getVMST_BRANCH_NAME();
Vector VDEAL_NO = dbcredit.getVDEAL_NO();
Vector VISS_BANK_NM = dbcredit.getVISS_BANK_NM();
Vector VDEAL_TYPE = dbcredit.getVDEAL_TYPE();
Vector VISS_BANK_REF = dbcredit.getVISS_BANK_REF();
Vector VADV_BANK_NM = dbcredit.getVADV_BANK_NM();
Vector VADV_BANK_REF = dbcredit.getVADV_BANK_REF();
Vector VCONFIRM_BANK_NM = dbcredit.getVCONFIRM_BANK_NM();
Vector VCONFIRM_BANK_REF = dbcredit.getVCONFIRM_BANK_REF();
Vector VADV_BANK_CD = dbcredit.getVADV_BANK_CD();
Vector VISS_BANK_CD = dbcredit.getVISS_BANK_CD();
Vector VCONFIRM_BANK_CD = dbcredit.getVCONFIRM_BANK_CD();
Vector VCANCEL_DT = dbcredit.getVCANCEL_DT();
Vector VEXP_VAL = dbcredit.getVEXP_VAL();
Vector VFROM_DT = dbcredit.getVFROM_DT();
Vector VTO_DT = dbcredit.getVTO_DT();
Vector VISSUE_DT = dbcredit.getVISSUE_DT();
Vector VEXPIRE_DT = dbcredit.getVEXPIRE_DT();
Vector VREMARK = dbcredit.getVREMARK();
Vector VSTATUS = dbcredit.getVSTATUS();
Vector VVALUE_USD = dbcredit.getVVALUE_USD();
Vector VEXCHANGE_RATE = dbcredit.getVEXCHANGE_RATE();
Vector VLEGAL_ENTITY = dbcredit.getVLEGAL_ENTITY();

String inLCConfCount = dbcredit.getInLCConfCount();
String inLCConfAmt = dbcredit.getInLCConfAmt();
String inLCConfAmtUsd = dbcredit.getInLCConfAmtUsd();
String inLCCanCount = dbcredit.getInLCCanCount();
String inLCCanAmt = dbcredit.getInLCCanAmt();
String inLCCanAmtUsd = dbcredit.getInLCCanAmtUsd();
String inLCAdvCount = dbcredit.getInLCAdvCount();
String inLCAdvAmt = dbcredit.getInLCAdvAmt();
String inLCAdvAmtUsd = dbcredit.getInLCAdvAmtUsd();
String inBGConfCount = dbcredit.getInBGConfCount();
String inBGConfAmt = dbcredit.getInBGConfAmt();
String inBGConfAmtUsd = dbcredit.getInBGConfAmtUsd();
String inBGCanCount = dbcredit.getInBGCanCount();
String inBGCanAmt = dbcredit.getInBGCanAmt();
String inBGCanAmtUsd = dbcredit.getInBGCanAmtUsd();
String inBGAdvCount = dbcredit.getInBGAdvCount();
String inBGAdvAmt = dbcredit.getInBGAdvAmt();
String inBGAdvAmtUsd = dbcredit.getInBGAdvAmtUsd();
String inPCGConfCount = dbcredit.getInPCGConfCount();
String inPCGConfAmt = dbcredit.getInPCGConfAmt();
String inPCGConfAmtUsd = dbcredit.getInPCGConfAmtUsd();
String inPCGCanCount = dbcredit.getInPCGCanCount();
String inPCGCanAmt = dbcredit.getInPCGCanAmt();
String inPCGCanAmtUsd = dbcredit.getInPCGCanAmtUsd();
String inPCGAdvCount = dbcredit.getInPCGAdvCount();
String inPCGAdvAmt = dbcredit.getInPCGAdvAmt();
String inPCGAdvAmtUsd = dbcredit.getInPCGAdvAmtUsd();

String outLCConfCount = dbcredit.getOutLCConfCount();
String outLCConfAmt = dbcredit.getOutLCConfAmt();
String outLCConfAmtUsd = dbcredit.getOutLCConfAmtUsd();
String outLCCanCount = dbcredit.getOutLCCanCount();
String outLCCanAmt = dbcredit.getOutLCCanAmt();
String outLCCanAmtUsd = dbcredit.getOutLCCanAmtUsd();
String outLCAdvCount = dbcredit.getOutLCAdvCount();
String outLCAdvAmt = dbcredit.getOutLCAdvAmt();
String outLCAdvAmtUsd = dbcredit.getOutLCAdvAmtUsd();
String outBGConfCount = dbcredit.getOutBGConfCount();
String outBGConfAmt = dbcredit.getOutBGConfAmt();
String outBGConfAmtUsd = dbcredit.getOutBGConfAmtUsd();
String outBGCanCount = dbcredit.getOutBGCanCount();
String outBGCanAmt = dbcredit.getOutBGCanAmt();
String outBGCanAmtUsd = dbcredit.getOutBGCanAmtUsd();
String outBGAdvCount = dbcredit.getOutBGAdvCount();
String outBGAdvAmt = dbcredit.getOutBGAdvAmt();
String outBGAdvAmtUsd = dbcredit.getOutBGAdvAmtUsd();
String outPCGConfCount = dbcredit.getOutPCGConfCount();
String outPCGConfAmt = dbcredit.getOutPCGConfAmt();
String outPCGConfAmtUsd = dbcredit.getOutPCGConfAmtUsd();
String outPCGCanCount = dbcredit.getOutPCGCanCount();
String outPCGCanAmt = dbcredit.getOutPCGCanAmt();
String outPCGCanAmtUsd = dbcredit.getOutPCGCanAmtUsd();
String outPCGAdvCount = dbcredit.getOutPCGAdvCount();
String outPCGAdvAmt = dbcredit.getOutPCGAdvAmt();
String outPCGAdvAmtUsd = dbcredit.getOutPCGAdvAmtUsd();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="21" rowspan="2" align="left">Collateral/Security KPI Report (<%=from_dt%> - <%=to_dt %>)</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th colspan="10">Incoming Security Summary</th>
				<th></th>
				<th colspan="10">Outgoing Security Summary</th>
			</tr>
			<tr>
				<th ></th>
				<th  colspan="3">Advised</th>
				<th  colspan="3">Confirmed</th>
				<th  colspan="3">Cancelled</th>
				<th></th>
				<th ></th>
				<th  colspan="3">Advised</th>
				<th  colspan="3">Confirmed</th>
				<th  colspan="3">Cancelled</th>
			</tr>
			<tr bgcolor="">
				<th></th>
				<th style="background-color: #000066; color: white">Count</th>
				<th>Value(INR)</th>
				<th>Value(USD)</th>
				<th style="background-color: #000066; color: white">Count</th>
				<th>Value(INR)</th>
				<th>Value(USD)</th>
				<th style="background-color: #000066; color: white">Count</th>
				<th>Value(INR)</th>
				<th>Value(USD)</th>
				<th></th>
				<th></th>
				<th style="background-color: #000066; color: white">Count</th>
				<th>Value(INR)</th>
				<th>Value(USD)</th>
				<th style="background-color: #000066; color: white">Count</th>
				<th>Value(INR)</th>
				<th>Value(USD)</th>
				<th style="background-color: #000066; color: white">Count</th>
				<th>Value(INR)</th>
				<th>Value(USD)</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td align="center" style="background-color: "><b>LC</b></td>
				<td style="background-color: #b3f0ff" align="center"><%=inLCAdvCount%></td>
				<td align="right"><%=inLCAdvAmt %></td>
				<td align="right"><%=inLCAdvAmtUsd %></td>
				<td style="background-color: #b3f0ff" align="right"><%=inLCConfCount %></td>
				<td align="right"><%=inLCConfAmt %></td>
				<td align="right"><%=inLCConfAmtUsd %></td>
				<td style="background-color: #b3f0ff" align="right"><%=inLCCanCount %></td>
				<td align="right"><%=inLCCanAmt %></td>
				<td align="right"><%=inLCCanAmtUsd %></td>
				<td></td>
				<td align="center" style="background-color: "><b>LC</b></td>
				<td style="background-color: #b3f0ff" align="center"><%=outLCAdvCount%></td>
				<td align="right"><%=outLCAdvAmt %></td>
				<td align="right"><%=outLCAdvAmtUsd %></td>
				<td style="background-color: #b3f0ff"align="right"><%=outLCConfCount %></td>
				<td align="right"><%=outLCConfAmt %></td>
				<td align="right"><%=outLCConfAmtUsd %></td>
				<td style="background-color: #b3f0ff" align="right"><%=outLCCanCount %></td>
				<td align="right"><%=outLCCanAmt %></td>
				<td align="right"><%=outLCCanAmtUsd %></td>
			</tr>
			<tr>
				<td align="center" style="background-color: "><b>BG</b></td>
				<td style="background-color: #b3f0ff" align="center"><%=inBGAdvCount%></td>
				<td align="right"><%=inBGAdvAmt %></td>
				<td align="right"><%=inBGAdvAmtUsd %></td>
				<td style="background-color: #b3f0ff" align="right"><%=inBGConfCount %></td>
				<td align="right"><%=inBGConfAmt %></td>
				<td align="right"><%=inBGConfAmtUsd %></td>
				<td style="background-color: #b3f0ff" align="right"><%=inBGCanCount %></td>
				<td align="right"><%=inBGCanAmt %></td>
				<td align="right"><%=inBGCanAmtUsd %></td>
				<td></td>
				<td align="center" style="background-color: "><b>BG</b></td>
				<td style="background-color: #b3f0ff" style="background-color: #b3f0ff" align="center"><%=outBGAdvCount%></td>
				<td align="right"><%=outBGAdvAmt %></td>
				<td align="right"><%=outBGAdvAmtUsd %></td>
				<td style="background-color: #b3f0ff" align="right"><%=outBGConfCount %></td>
				<td align="right"><%=outBGConfAmt %></td>
				<td align="right"><%=outBGConfAmtUsd %></td>
				<td style="background-color: #b3f0ff" align="right"><%=outBGCanCount %></td>
				<td align="right"><%=outBGCanAmt %></td>
				<td align="right"><%=outBGCanAmtUsd %></td>
			</tr>
			<tr>
				<td align="center" style="background-color: "><b>PCG</b></td>
				<td style="background-color: #b3f0ff" align="center"><%=inPCGAdvCount%></td>
				<td align="right"><%=inPCGAdvAmt %></td>
				<td align="right"><%=inPCGAdvAmtUsd %></td>
				<td style="background-color: #b3f0ff" align="right"><%=inPCGConfCount %></td>
				<td align="right"><%=inPCGConfAmt %></td>
				<td align="right"><%=inPCGConfAmtUsd %></td>
				<td style="background-color: #b3f0ff" align="right"><%=inPCGCanCount %></td>
				<td align="right"><%=inPCGCanAmt %></td>
				<td align="right"><%=inPCGCanAmtUsd %></td>
				<td></td>
				<td align="center" style="background-color: "><b>PCG</b></td>
				<td style="background-color: #b3f0ff" style="background-color: #b3f0ff" align="center"><%=outPCGAdvCount%></td>
				<td align="right"><%=outPCGAdvAmt %></td>
				<td align="right"><%=outPCGAdvAmtUsd %></td>
				<td style="background-color: #b3f0ff" align="right"><%=outPCGConfCount %></td>
				<td align="right"><%=outPCGConfAmt %></td>
				<td align="right"><%=outPCGConfAmtUsd %></td>
				<td style="background-color: #b3f0ff" align="right"><%=outPCGCanCount %></td>
				<td align="right"><%=outPCGCanAmt %></td>
				<td align="right"><%=outPCGCanAmtUsd %></td>
			</tr>
		</tbody>
	</table>&nbsp;
	<table width="100%" border="1">
		<thead>
			<tr>
				<th colspan="21">Incoming/Outgoing</th>
			</tr>
			<tr>
				<th align="center">Sr#</th>
				<th align="center">Legal Entity</th>
				<th align="center">Counterparty</th>
				<th align="center">Security Type</th>
				<th align="center">Incoming/ Outgoing</th>
				<th align="center">Deal Ref#</th>
				<th align="center">Security Ref#</th>
				<th align="center">Status</th>
				<th align="center">Security Value(INR)</th>
				<th align="center">Security Value(USD)</th>
				<th align="center">Received Date</th>
				<th align="center">Issued Date</th>
				<th align="center">Expire Date</th>
				<th align="center">Cancellation Date</th>
				<th align="center">Issuing Bank name</th>
				<th align="center">Issuing Bank's Reference</th>
				<th align="center">Advising Bank Name</th>
				<th align="center">Advising Bank's Reference</th>
				<th align="center">Confirming Bank Name</th>
				<th align="center">Confirming Bank's Reference</th>
				<th align="center">Remarks</th>
			</tr>
		</thead>
		<tbody>
		<%if(VCOUNTERPARTY_NM.size()>0){ %>
			<%for(int i=0;i<VCOUNTERPARTY_NM.size();i++) {%>
			<tr>
				<td align="center"><%=i+1%></td>
				<td align="center"><%=VLEGAL_ENTITY.elementAt(i)%></td>
				<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
				<td align="center"><%=VSEC_TYPE.elementAt(i) %></td>
				<td align="center"><%if(VSEC_CATEGORY.elementAt(i).equals("R")){%>Incoming<%}else if(VSEC_CATEGORY.elementAt(i).equals("I")) {%>Outgoing<%} %></td>
				<td align="center"><%=VDEAL_NO.elementAt(i)%></td>
				<td align="center"><%=VSEC_REF_NO.elementAt(i)%></td>
				<td align="center"><%=VSTATUS.elementAt(i)%></td>
				<td align="right"><%if(!VVALUE.elementAt(i).equals("")){%><%=nf2.format(Double.parseDouble(""+VVALUE.elementAt(i)))%><%}else{ %><%=nf2.format(Double.parseDouble(""+VVALUE_USD.elementAt(i))*Double.parseDouble(""+VEXCHANGE_RATE.elementAt(i)))%><%}%></td>
				<td align="right"><%if(!VVALUE_USD.elementAt(i).equals("")){%><%=nf2.format(Double.parseDouble(""+VVALUE_USD.elementAt(i)))%><%}else{ %><%=nf2.format(Double.parseDouble(""+VVALUE.elementAt(i))/Double.parseDouble(""+VEXCHANGE_RATE.elementAt(i)))%><%}%></td>
				<td align="center"><%=VRECEIVED_DATE.elementAt(i)%></td>
				<td align="center"><%=VISSUE_DT.elementAt(i)%></td>
				<td align="center"><%=VEXPIRE_DT.elementAt(i)%></td>
				<td align="center"><%=VCANCEL_DT.elementAt(i)%></td>
				<td align="center"><%=VISS_BANK_NM.elementAt(i)%></td>
				<td align="center"><%=VISS_BANK_REF.elementAt(i)%></td>
				<td align="center"><%=VADV_BANK_NM.elementAt(i)%></td>
				<td align="center"><%=VADV_BANK_REF.elementAt(i)%></td>
				<td align="center"><%=VCONFIRM_BANK_NM.elementAt(i)%></td>
				<td align="center"><%=VCONFIRM_BANK_REF.elementAt(i)%></td>
				<td align="center"><%=VREMARK.elementAt(i)%></td>
			</tr>
			<%} %>
		<%}else{%>
			<tr>
				<td colspan="21" align="center"><b>No Security Available in Selected Time Period!</b></td>
			</tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>
