<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Gas Exchange Remittance FO Approval</title>
<!-- <link rel="stylesheet" type="text/css" href="../font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="../css/common.css"> -->

<%@ include file="../util/common_js.jsp"%>
<script>
function Do_close()
{
	window.opener.refresh();
}

function doSubmit()
{
	var msg="";
	var flag=true;
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var a=confirm("On your Approval Gas Exchange Remittance will be Sent for SAP Posting. \nAre you Sure want to Proceed? ");
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

<jsp:useBean class="com.etrm.fms.gx.DataBean_Gx_Invoice" id="gx_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
//String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
//String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");

String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String type_flag=request.getParameter("type_flag")==null?"":request.getParameter("type_flag");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String invoice_no=request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
String sap_approval_flag=request.getParameter("sap_approval_flag")==null?"":request.getParameter("sap_approval_flag"); 
String xmlfile_nm=request.getParameter("xmlfile_nm")==null?"":request.getParameter("xmlfile_nm");
String gx_counterparty_cd=request.getParameter("gx_counterparty_cd")==null?"":request.getParameter("gx_counterparty_cd");
String bu_plant_seq=request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");

String inv_pdf_flag=request.getParameter("inv_pdf_flag")==null?"":request.getParameter("inv_pdf_flag");
String view_before=request.getParameter("view_before")==null?"":request.getParameter("view_before");

String remittance_no=request.getParameter("remittance_no")==null?"":request.getParameter("remittance_no");

//String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
//String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");

String activity_value = ""; //gx_inv.getActivity_value();

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
	gx_inv.setCallFlag("PARSE_SAP_XML");
}
else 
{
	gx_inv.setCallFlag("GX_SAP_XML");
}	

gx_inv.setRequest(request);
gx_inv.setContract_type(contract_type);
gx_inv.setFinancial_year(financial_year);
gx_inv.setInvoice_seq(invoice_seq);
gx_inv.setCounterparty_cd(counterparty_cd);
gx_inv.setType_flag(type_flag);
gx_inv.setInvoice_no(invoice_no);
gx_inv.setInvoice_type(invoice_type);
gx_inv.setComp_cd(owner_cd);
gx_inv.setXmlfile_name(xmlfile_nm);
gx_inv.setFile_path(appPath);
gx_inv.setSap_approval_flag(sap_approval_flag);
gx_inv.setGx_counterparty_cd(gx_counterparty_cd);
gx_inv.setBu_unit(bu_plant_seq);
gx_inv.setAgmt_no(agmt_no);
gx_inv.setCont_no(cont_no);
gx_inv.setEmp_cd(emp_cd);
gx_inv.setRemittance_no(remittance_no);
gx_inv.init();

String documentType=gx_inv.getDocumentType();
String postingDate=gx_inv.getPostingDate();
String accountingPeriodMonth=gx_inv.getAccountingPeriodMonth();
String accountingPeriodYear=gx_inv.getAccountingPeriodYear();
String headerCompanyCode=gx_inv.getHeaderCompanyCode();
String docHeaderText=gx_inv.getDocHeaderText();
String refNum =gx_inv.getRefNum();
String currency=gx_inv.getCurrency();
String counterparty_nm = gx_inv.getCounterparty_nm();
String counterparty_abbr = gx_inv.getCounterparty_abbr();
String documentNo=gx_inv.getDocumentNo();
String documentDate=gx_inv.getDocumentDate();
String gx_couterpty_nm = gx_inv.getGx_couterpty_nm();
String gx_couterpty_abbr = gx_inv.getGx_couterpty_abbr();

String zeroTotal =gx_inv.getZeroTotal();

String sap_msg_status = gx_inv.getSap_msg_status();
String sap_doc_no = gx_inv.getSap_doc_no();
String sap_ack_msg = gx_inv.geSap_ack_msg();

Vector VLINESEQNO =gx_inv.getVLINESEQNO();
Vector VPOSTINGKEY =gx_inv.getVPOSTINGKEY();
Vector VACCOUNT =gx_inv.getVACCOUNT();
Vector VCURRENCYAMOUNT =gx_inv.getVCURRENCYAMOUNT();
Vector VBUSINESSAREA =gx_inv.getVBUSINESSAREA();
Vector VITEMTEXT =gx_inv.getVITEMTEXT();
Vector VSHORTTEXT=gx_inv.getVSHORTTEXT();

%>

<body <%if(!msg.equals("")){ %>onload="Do_close();"<%} %>>

<%@ include file="../home/loading.jsp"%>
<form method="post" action="../servlet/Frm_Gx_Invoice">

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
											<b>&nbsp;&nbsp;Doc. Type : <%=documentType %> (<%=counterparty_abbr%> AP) Normal document</b>
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
											<td ><div align="center"><font size="2" face="Courier New"><b><%=gx_couterpty_nm%></b></font></div></td>
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
				<%if(view_before.equals("")) {%>
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
												<font style="color: blue;"><I><b>Do you want to Approve SAP XML Generation for Posting?</b></I></font>&nbsp;
												<input type="radio" name="rd" value="Y" onclick="doSubmit();">&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
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
				<%} %>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="INVOICE_SAP_APPROVE">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="invoice_no" value="<%=invoice_no%>">
<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="type_flag" value="<%=type_flag%>">
<input type="hidden" name="invoice_type" value="<%=invoice_type%>">
<input type="hidden" name="gx_counterparty_cd" value="<%=gx_counterparty_cd%>">
<input type="hidden" name="u" value="<%=u%>">

</form>
</body>
</html>