<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Purchase Invoice Approval</title>
<script>
function Do_close(msg,msg_type)
{
	window.opener.refershPar(msg,msg_type);
	window.close();
}
</script>
</head>

<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");

String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String purchase_type_flag=request.getParameter("purchase_type_flag")==null?"":request.getParameter("purchase_type_flag");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String invoice_no=request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");

String xmlfile_nm=request.getParameter("xmlfile_nm")==null?"":request.getParameter("xmlfile_nm");

String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");

String activity_value = ""; //purchase.getActivity_value();

String workDir=CommonVariable.work_dir+owner_cd;
String sapxml_dir=CommonVariable.sap_xml;
String appPath = request.getServletContext().getRealPath(workDir+"/"+sapxml_dir+"/");

purchase.setCallFlag("PURCHASE_SAP_XML");
purchase.setRequest(request);
purchase.setContract_type(contract_type);
purchase.setFinancial_year(financial_year);
purchase.setInvoice_seq(invoice_seq);
purchase.setCounterparty_cd(counterparty_cd);
purchase.setPurchase_type_flag(purchase_type_flag);
purchase.setInvoice_no(invoice_no);
purchase.setInvoice_type(invoice_type);
purchase.setComp_cd(owner_cd);
purchase.setXmlfile_name(xmlfile_nm);
purchase.setFile_path(appPath);
purchase.init();

String documentType=purchase.getDocumentType();
String postingDate=purchase.getPostingDate();
String accountingPeriodMonth=purchase.getAccountingPeriodMonth();
String accountingPeriodYear=purchase.getAccountingPeriodYear();
String headerCompanyCode=purchase.getHeaderCompanyCode();
String docHeaderText=purchase.getDocHeaderText();
String refNum =purchase.getRefNum();
String currency=purchase.getCurrency();

Vector VLINESEQNO =purchase.getVLINESEQNO();
Vector VPOSTINGKEY =purchase.getVPOSTINGKEY();
Vector VACCOUNT =purchase.getVACCOUNT();
Vector VCURRENCYAMOUNT =purchase.getVCURRENCYAMOUNT();
Vector VBUSINESSAREA =purchase.getVBUSINESSAREA();
Vector VITEMTEXT =purchase.getVITEMTEXT();

%>

<body>

<%@ include file="../home/loading.jsp"%>
<form method="post" action="../servlet/Frm_PurchaseReports">

<table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="12">
			<div align="center">
				<font size="4" face="Courier New">
					<b>SAP P80 Post View</b>
					<br>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="middle"><td colspan="7">&nbsp;</td></tr>
</table>	
<table width="90%"  border="1" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="12">
			<div align="Left">
				<font size="2" face="Courier New">
					<br>
					<b>&nbsp;&nbsp;Doc. Type : <%=documentType %> (Trader ABBR AP) Normal document</b>
					<br>
					<b>&nbsp;&nbsp;Doc. Number :  ? &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Company Code: <%=headerCompanyCode%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Fiscal Year: <%=accountingPeriodYear %></b>
					<br>
					<b>&nbsp;&nbsp;Doc. Date :  ? &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Posting Date: <%=postingDate %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Period: <%=accountingPeriodMonth %></b>
					<br>
					<b>&nbsp;&nbsp;Calculate Tax :  []</b>
					<br>
					<b>&nbsp;&nbsp;Ref. Doc. :  <%=refNum%></b>
					<br>
					<b>&nbsp;&nbsp;Doc. Currency:  <%=currency %></b>
					<br>
					<b>&nbsp;&nbsp;Doc. Hdr Text:  <%=docHeaderText%></b>
					<br><br>
				</font>
			</div>
		</td>
	</tr>
</table>
<table>
	<tr valign="middle"><td colspan="12">&nbsp;</td></tr>
</table>	
<table width="90%"  border="1" align="center" cellpadding="2" cellspacing="0">
	<thead>
		<tr valign="bottom" style="background:#66ffff">
			<th width="4%"><div align="center"><font size="2" face="Courier New"><b>Itm.</b></font></div></th>
			<th width="4%"><div align="center"><font size="2" face="Courier New"><b>PK</b></font></div></th>
			<th width="6%"><div align="center"><font size="2" face="Courier New"><b>CoCd</b></font></div></th>
			<th width="10%"><div align="center"><font size="2" face="Courier New"><b>Account</b></font></div></th>
			<th width="16%"><div align="center"><font size="2" face="Courier New"><b>Account short text</b></font></div></th>
			<th width="15%"><div align="center"><font size="2" face="Courier New"><b>Amount</b></font></div></th>
			<th width="6%"><div align="center"><font size="2" face="Courier New"><b>Crcy</b></font></div></th>
			<th width="15%"><div align="center"><font size="2" face="Courier New"><b>Amount in LC</b></font></div></th>
			<th width="4%"><div align="center"><font size="2" face="Courier New"><b>LCurr</b></font></div></th>
			<th width="4%"><div align="center"><font size="2" face="Courier New"><b>BusA</b></font></div></th>
			<th width="16%"><div align="center"><font size="2" face="Courier New"><b>Text</b></font></div></th>
		</tr>
	</thead>
	<tbody>
	<%for(int i=0;i<VLINESEQNO.size();i++){ %>
		<tr valign="bottom">
			<td width="4%"><div align="center"><font size="2" face="Courier New"><b><%=VLINESEQNO.elementAt(i) %></b></font></div></td>
			<td width="4%"><div align="center"><font size="2" face="Courier New"><b><%=VPOSTINGKEY.elementAt(i) %></b></font></div></td>
			<td width="6%"><div align="center"><font size="2" face="Courier New"><b><%=headerCompanyCode %></b></font></div></td>
			<td width="10%"><div align="center"><font size="2" face="Courier New"><b><%=VACCOUNT.elementAt(i) %></b></font></div></td>
			<td width="16%"><div align="center"><font size="2" face="Courier New"><b></b></font></div></td>
			<td width="15%"><div align="right"><font size="2" face="Courier New"><b><%=VCURRENCYAMOUNT.elementAt(i) %></b></font></div></td>
			<td width="6%"><div align="center"><font size="2" face="Courier New"><b><%=currency%></b></font></div></td>
			<td width="15%"><div align="center"><font size="2" face="Courier New"><b></b></font></div></td>
			<td width="4%"><div align="center"><font size="2" face="Courier New"><b></b></font></div></td>
			<td width="4%"><div align="center"><font size="2" face="Courier New"><b><%=VBUSINESSAREA.elementAt(i) %></b></font></div></td>
			<td width="16%"><div align="center"><font size="2" face="Courier New"><b><%=VITEMTEXT.elementAt(i) %></b></font></div></td>		
		</tr>
	<%} %>		
	</tbody>
</table>

<input type="hidden" name="option" value="GENERATE_INVOICE_XML">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="invoice_no" value="<%=invoice_no%>">
<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="purchase_type_flag" value="<%=purchase_type_flag%>">
<input type="hidden" name="invoice_type" value="<%=invoice_type%>">

</form>
</body>
</html>