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
	window.opener.refresh(accroid);
}

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

String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String purchase_type_flag=request.getParameter("purchase_type_flag")==null?"":request.getParameter("purchase_type_flag");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String invoice_no=request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
String sap_approval_flag=request.getParameter("sap_approval_flag")==null?"":request.getParameter("sap_approval_flag"); 
String xmlfile_nm=request.getParameter("xmlfile_nm")==null?"":request.getParameter("xmlfile_nm");
String bu_seq=request.getParameter("bu_seq")==null?"":request.getParameter("bu_seq");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");

String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
String boe_no=request.getParameter("boe_no")==null?"":request.getParameter("boe_no");
String inv_flag=request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");

String inv_pdf_flag=request.getParameter("inv_pdf_flag")==null?"":request.getParameter("inv_pdf_flag");
String invPg=request.getParameter("invPg")==null?"":request.getParameter("invPg");

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
	purchase.setCallFlag("PURCHASE_SAP_XML");
}	

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
purchase.setSap_approval_flag(sap_approval_flag);
purchase.setBu_unit(bu_seq);
purchase.setCont_no(cont_no);
purchase.setEmp_cd(emp_cd);
purchase.setInv_flag(inv_flag);
purchase.init();

String documentType=purchase.getDocumentType();
String postingDate=purchase.getPostingDate();
String accountingPeriodMonth=purchase.getAccountingPeriodMonth();
String accountingPeriodYear=purchase.getAccountingPeriodYear();
String headerCompanyCode=purchase.getHeaderCompanyCode();
String docHeaderText=purchase.getDocHeaderText();
String refNum =purchase.getRefNum();
String currency=purchase.getCurrency();
String counterparty_nm = purchase.getCounterparty_nm();
String counterparty_abbr = purchase.getCounterparty_abbr();
String documentNo=purchase.getDocumentNo();
String documentDate=purchase.getDocumentDate();

String zeroTotal =purchase.getZeroTotal();

Vector VLINESEQNO =purchase.getVLINESEQNO();
Vector VPOSTINGKEY =purchase.getVPOSTINGKEY();
Vector VACCOUNT =purchase.getVACCOUNT();
Vector VCURRENCYAMOUNT =purchase.getVCURRENCYAMOUNT();
Vector VBUSINESSAREA =purchase.getVBUSINESSAREA();
Vector VITEMTEXT =purchase.getVITEMTEXT();
Vector VSHORTTEXT = purchase.getVSHORTTEXT();

%>

<body <%if(!msg.equals("")){ %>onload="Do_close('<%=accroid%>');"<%} %>>

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
											<b>&nbsp;&nbsp;Doc. Type : <%=documentType %> (<%if(inv_flag.equals("CP") || inv_flag.equals("CF")) {%>Customs<%}else{%><%=counterparty_abbr%> AP<%}%>) Normal document</b>
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
											<%if(VITEMTEXT.elementAt(i).equals("TDS")){%> 
													style=" background: yellow"
											<%}%>><b><%=VCURRENCYAMOUNT.elementAt(i) %></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b><%=currency%></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b></b></font></div></td>
										<td ><div align="center"><font size="2" face="Courier New"><b></b></font></div></td>
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
				<div class="card-body cdbody">
					<div class="row">
						<table border="0" width="100%" align="center" cellpadding="2" cellspacing="0">
							<tr valign="middle">
								<td colspan="7">
									<div align="center">
									<%if(inv_pdf_flag.equals("N")){ %>
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
											<font style="color: green;"><I><b>Approved SAP XML Generated for Posting!</b></I></font>
										<%} %>
									<%} %>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="INVOICE_SAP_APPROVE">

<input type="hidden" name="accroid" value="<%=accroid%>">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="invoice_no" value="<%=invoice_no%>">
<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="purchase_type_flag" value="<%=purchase_type_flag%>">
<input type="hidden" name="invoice_type" value="<%=invoice_type%>">
<input type="hidden" name="inv_flag" value="<%=inv_flag%>">

<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<input type="hidden" name="u" value="<%=u%>">

</form>
</body>
</html>