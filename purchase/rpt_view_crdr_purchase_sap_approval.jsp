<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Purchase Remittance FO Approval</title>
<%@ include file="../util/common_js.jsp"%>
<script>
function Do_close(accroid)
{
	window.opener.refresh();
	//window.close();
}

enableButton=true;
function doSubmit()
{
	var msg="";
	var flag=true;
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var a=confirm("On your Approval Purchase Remittance will be Sent for SAP Posting. \nAre you Sure want to Proceed? ");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			editAllowedOnCpStatus = true;
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

</script>
</head>

<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

String operation=request.getParameter("operation")==null?"INSERT":request.getParameter("operation");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String sel_inv_no = request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
String invoice_type = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String crdr_gen_type = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
String sgpg_type = request.getParameter("sgpg_type")==null?"":request.getParameter("sgpg_type");
String crdr_no = request.getParameter("crdr_no")==null?"":request.getParameter("crdr_no");
String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String sap_approval_flag=request.getParameter("sap_approval_flag")==null?"":request.getParameter("sap_approval_flag"); 
String xmlfile_nm=request.getParameter("xmlfile_nm")==null?"":request.getParameter("xmlfile_nm");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String invoice_dt=request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
String inv_pdf_flag=request.getParameter("inv_pdf_flag")==null?"":request.getParameter("inv_pdf_flag");
String invPg=request.getParameter("invPg")==null?"":request.getParameter("invPg");
String purchase_type_flag=request.getParameter("purchase_type_flag")==null?"":request.getParameter("purchase_type_flag");


//String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
//String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");

String activity_value = ""; //purchase.getActivity_value();

String workDir=CommonVariable.work_dir+owner_cd;
String sapxml_dir="";
if (sap_approval_flag.equals("Y"))
{
	sapxml_dir=CommonVariable.sap_xml;
}
else
{
	sapxml_dir=CommonVariable.temp_sap_xml;
}
String appPath = request.getServletContext().getRealPath(workDir+"/"+sapxml_dir+"/");

if (sap_approval_flag.equals("Y"))
{
	purchase.setCallFlag("PARSE_SAP_XML");
}
else 
{
	purchase.setCallFlag("PURCHASE_CRDR_SAP_XML");
}	

purchase.setRequest(request);
purchase.setContract_type(contract_type);
purchase.setFinancial_year(financial_year);
purchase.setInvoice_seq(invoice_seq);
purchase.setCounterparty_cd(counterparty_cd);
purchase.setSgpg_type(sgpg_type);
purchase.setInvoice_type(invoice_type);
purchase.setComp_cd(owner_cd);
purchase.setXmlfile_name(xmlfile_nm);
purchase.setFile_path(appPath);
purchase.setSap_approval_flag(sap_approval_flag);
purchase.setBu_unit(bu_unit);
purchase.setCont_no(cont_no);
purchase.setEmp_cd(emp_cd);
purchase.setInv_flag(crdr_gen_type);
purchase.setInvoice_dt(invoice_dt);
purchase.setRemittance_no(crdr_no);
purchase.init();

String documentType=purchase.getDocumentType();
String postingDate=purchase.getPostingDate();
String accountingPeriodMonth=purchase.getAccountingPeriodMonth();
String accountingPeriodYear=purchase.getAccountingPeriodYear();
String headerCompanyCode=purchase.getHeaderCompanyCode();
String docHeaderText=purchase.getDocHeaderText();
String refNum =purchase.getRefNum();
String currency=purchase.getCurrency();
String local_currency=purchase.getLocal_currency();
String counterparty_nm = purchase.getCounterparty_nm();
String counterparty_abbr = purchase.getCounterparty_abbr();
String documentNo=purchase.getDocumentNo();
String documentDate=purchase.getDocumentDate();

String zeroTotal =purchase.getZeroTotal();

String sap_msg_status = purchase.getSap_msg_status();
String sap_doc_no = purchase.getSap_doc_no();
String sap_ack_msg = purchase.geSap_ack_msg();

Vector VLINESEQNO =purchase.getVLINESEQNO();
Vector VPOSTINGKEY =purchase.getVPOSTINGKEY();
Vector VACCOUNT =purchase.getVACCOUNT();
Vector VCURRENCYAMOUNT =purchase.getVCURRENCYAMOUNT();
Vector VLOCAL_CURRENCYAMOUNT =purchase.getVLOCAL_CURRENCYAMOUNT();
Vector VBUSINESSAREA =purchase.getVBUSINESSAREA();
Vector VITEMTEXT =purchase.getVITEMTEXT();
Vector VSHORTTEXT = purchase.getVSHORTTEXT();

%>

<body onload="<%if(!msg.equals("")){%>Do_close('<%=accroid%>');<%}%>">

<%@ include file="../home/loading.jsp"%>
<form method="post" action="../servlet/Frm_PurchaseReports">

<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	SAP P80 Post View
					    </div>					    				    
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0">
							<tr valign="middle">
								<td colspan="12">
									<div align="Left">
										<font size="2" face="Courier New">
											<br>
											<b>&nbsp;&nbsp;Doc. Type : <%=documentType %> (<%if(crdr_gen_type.equals("CP") || crdr_gen_type.equals("CF")) {%>Customs<%}else{%><%=counterparty_abbr%> AP<%}%>) Normal document</b>
											<br>
											<b>&nbsp;&nbsp;Doc. Number : <%=documentNo %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Company Code: <%=headerCompanyCode%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Fiscal Year: <%=accountingPeriodYear %></b>
											<br>
											<b>&nbsp;&nbsp;Doc. Date :  <%=documentDate %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Posting Date: <%=postingDate %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Period: <%=accountingPeriodMonth %></b>
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
					</div>
				</div>	
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr valign="bottom" style="background:#66ffff">
										<th><div align="center"><font size="2" face="Courier New"><b>Itm.</b></font></div></th>
										<th><div align="center"><font size="2" face="Courier New"><b>PK</b></font></div></th>
										<th><div align="center"><font size="2" face="Courier New"><b>CoCd</b></font></div></th>
										<th><div align="center"><font size="2" face="Courier New"><b>Account</b></font></div></th>
										<th><div align="center"><font size="2" face="Courier New"><b>Account short text</b></font></div></th>
										<th><div align="center"><font size="2" face="Courier New"><b>Amount</b></font></div></th>
										<th><div align="center"><font size="2" face="Courier New"><b>Crcy</b></font></div></th>
										<th><div align="center"><font size="2" face="Courier New"><b>Amount in LC</b></font></div></th>
										<th><div align="center"><font size="2" face="Courier New"><b>LCurr</b></font></div></th>
										<th><div align="center"><font size="2" face="Courier New"><b>BusA</b></font></div></th>
										<th><div align="center"><font size="2" face="Courier New"><b>Text</b></font></div></th>
									</tr>
								</thead>
								<tbody>
								<%for(int i=0;i<VLINESEQNO.size();i++){ %>
									<tr valign="bottom">
										<td ><div align="center"><font size="2" face="Courier New"><b><%=VLINESEQNO.elementAt(i) %></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b><%=VPOSTINGKEY.elementAt(i) %></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b><%=headerCompanyCode %></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b><%=VACCOUNT.elementAt(i) %></b></font></div></td>
										<%if(i==0){%>
											<td ><div align="center"><font size="2" face="Courier New"><b><%if(VACCOUNT.elementAt(i).equals("60535")) {%>SBI A/C Custom Duty<%}else{%><%=counterparty_nm%><%} %></b></font></div></td>
										<%}else{%>
											<td ><div align="center"><font size="2" face="Courier New"><b><%=VSHORTTEXT.elementAt(i) %></b></font></div></td>
										<%}%>	
										<td ><div align="right"><font size="2" face="Courier New"
											<%if(VITEMTEXT.elementAt(i).toString().contains("TDS")){%> 
													style=" background: yellow"
											<%}%>><b><%=VCURRENCYAMOUNT.elementAt(i) %></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b><%=currency%></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b><%=VLOCAL_CURRENCYAMOUNT.elementAt(i) %></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b><%if(VITEMTEXT.elementAt(i).equals("TDS")){%><%}else{%><%=local_currency%><%} %></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b><%=VBUSINESSAREA.elementAt(i) %></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b><%=VITEMTEXT.elementAt(i) %></b></font></div></td>		
									</tr>
								<%} %>	
									<tr style="background: yellow;">
										<td ><div align="center"><font size="2" face="Courier New"><b>*</b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b></b></font></div></td>			
										<td ><div align="center"><font size="2" face="Courier New"><b></b></font></div></td>
										<td ><div align="right"><font size="2" face="Courier New" <%if (!zeroTotal.equals("0.00")) { %> color="red"<%} %>><b><%=zeroTotal%></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b><%=currency%></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b></b></font></div></td>		
									</tr>	
								</tbody>	
							</table>
						</div>
					</div>
				</div>
				<%-- <%if(view_before.equals("")) {%> --%>
				<div class="card-body cdbody">
					<div class="row">
						<table border="0" width="100%" align="center" cellpadding="2" cellspacing="0">
							<tr valign="middle">
								<td colspan="7">
									<div align="center">
									<%if(VBUSINESSAREA.contains("")){%>
										<font style="color: red;"><I><b>SAP Posting Approval Blocked ( Business Area Association pending !! )</b></I></font>
									<%}else if(inv_pdf_flag.equals("N")){ %>
										<font style="color: red;"><I><b>SAP Posting Approval Blocked ( PDF Generation pending !! )</b></I></font>									
									<%}else {%>
										<%if(!sap_approval_flag.equals("Y")){ %>
											<%if (zeroTotal.equals("0.00")) { %>
												<%if(!invPg.equals("Y")){%>				
												<font style="color: blue;"><I><b>Do you want to Approve SAP XML Generation for Posting?</b></I></font>&nbsp;
												<input type="radio" name="rd" value="Y" onclick="doSubmit();">&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
												<%} %>
											<%}else {%>
												<font style="color: red;"><I><b>SAP Posting Approval Blocked ( Re-check Invoice !! )</b></I></font>
											<%} %>	
										<%}else {%>
											<%if (sap_msg_status.equals("S")){%>				
												<font style="color: green;"><I><b>Success SAP Application document posted [<%=sap_doc_no%>]!</b></I></font>
											<%}else if (sap_msg_status.equals("E")){%>
												<font style="color: red;"><I><b>Error SAP Application document not posted!</b></I></font>
											<%}else{%>
												<font style="color: green;"><I><b>Approved SAP XML Generated for Posting!</b></I></font>
											<%} %>
										<%} %>
									<%} %>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<%-- <%} %> --%>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="INVOICE_SAP_APPROVE">

<input type="hidden" name="accroid" value="<%=accroid%>">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="invoice_no" value="<%=crdr_no%>">
<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="purchase_type_nm" value="<%=sgpg_type%>">
<input type="hidden" name="purchase_type_flag" value="<%=purchase_type_flag%>">
<input type="hidden" name="invoice_type" value="<%=invoice_type%>">
<input type="hidden" name="inv_flag" value="<%=crdr_gen_type%>">
<input type="hidden" name="invoice_dt" value="<%=invoice_dt%>">

<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<input type="hidden" name="u" value="<%=u%>">

</form>
</body>
</html>