
<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Sales Invoice FO Approval</title>
<!-- <link rel="stylesheet" type="text/css" href="../font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="../css/common.css"> -->

<%@ include file="../util/common_js.jsp"%>
<script>
function Do_close(accroid)
{
	window.opener.refresh(accroid);
}

enableButton=true;
function doSubmit()
{
	var u = document.forms[0].u.value;
	var approve_access = document.forms[0].approve_access.value;
	
	var msg="";
	var flag=true;
	
	if(approve_access=="N")
	{
		msg+="You don't have Approval Rights!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a=confirm("On your Approval Invoice data will be Sent for SAP Posting. \n Are you Sure want to Proceed? ");
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

<jsp:useBean class="com.etrm.fms.accounting.DataBean_Accounting" id="accounting" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
//String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
//String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
/* String emp_cd="";
if(session.getAttribute("emp_cd")==null||session.getAttribute("emp_cd")==""||session.getAttribute("emp_cd").toString().equals("null"))
{
	emp_cd="";
}  
else
{
	emp_cd=""+session.getAttribute("emp_cd");
} */

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String type_flag=request.getParameter("type_flag")==null?"":request.getParameter("type_flag");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String invoice_no=request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
String sap_approval_flag=request.getParameter("sap_approval_flag")==null?"":request.getParameter("sap_approval_flag");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");


String xmlfile_nm=request.getParameter("xmlfile_nm")==null?"":request.getParameter("xmlfile_nm");
String inv_pdf_flag=request.getParameter("inv_pdf_flag")==null?"":request.getParameter("inv_pdf_flag");

String gen_type=request.getParameter("gen_type")==null?"":request.getParameter("gen_type");		//Added for SUN Approval.

//String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
//String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");

String activity_value = ""; //purchase.getActivity_value();

String workDir=CommonVariable.work_dir+owner_cd;
String sapxml_dir="";
if (!gen_type.equals("S") && sap_approval_flag.equals("Y"))
{
	sapxml_dir=CommonVariable.sap_xml;
}
else if(gen_type.equals("S") && sap_approval_flag.equals("Y"))
{
	sapxml_dir=CommonVariable.temp_sap_xml;
}
else
{
	sapxml_dir=CommonVariable.temp_sap_xml;
}
String appPath = request.getServletContext().getRealPath(workDir+"/"+sapxml_dir+"/");

if(gen_type.equals("S") && sap_approval_flag.equals("Y"))
{
	accounting.setCallFlag("SALES_SAP_XML");
	accounting.setSap_approval_flag("");
}
else
{
	if (sap_approval_flag.equals("Y"))
	{
		accounting.setCallFlag("PARSE_SAP_XML");
	}
	else 
	{
		accounting.setCallFlag("CRDR_SALES_SAP_XML");
	}	
	accounting.setSap_approval_flag(sap_approval_flag);
}
accounting.setRequest(request);
accounting.setContract_type(contract_type);
accounting.setFinancial_year(financial_year);
accounting.setInvoice_seq(invoice_seq);
accounting.setCounterparty_cd(counterparty_cd);
accounting.setType_flag(type_flag);
accounting.setInvoice_no(invoice_no);
accounting.setInvoice_type(invoice_type);
accounting.setComp_cd(owner_cd);
accounting.setXmlfile_name(xmlfile_nm);
accounting.setFile_path(appPath);
accounting.setBu_state_tin(bu_state_tin);
//accounting.setSap_approval_flag(sap_approval_flag);
accounting.setAgmt_no(agmt_no);
accounting.setCont_no(cont_no);
accounting.setEmp_cd(emp_cd);
accounting.init();

if(gen_type.equals(""))
{
	gen_type=accounting.getGen_type();
}

String documentType=accounting.getDocumentType();
String documentDate=accounting.getDocumentDate();
String postingDate=accounting.getPostingDate();
String accountingPeriodMonth=accounting.getAccountingPeriodMonth();
String accountingPeriodYear=accounting.getAccountingPeriodYear();
String headerCompanyCode=accounting.getHeaderCompanyCode();
String docHeaderText=accounting.getDocHeaderText();
String refNum =accounting.getRefNum();
String currency=accounting.getCurrency();
String documentNo=accounting.getDocumentNo();
String counterparty_nm = accounting.getCounterparty_nm();
String counterparty_abbr = accounting.getCounterparty_abbr();

String zeroTotal =accounting.getZeroTotal();

String sap_msg_status = accounting.getSap_msg_status();
String sap_doc_no = accounting.getSap_doc_no();
String sap_ack_msg = accounting.geSap_ack_msg();

Vector VLINESEQNO =accounting.getVLINESEQNO();
Vector VPOSTINGKEY =accounting.getVPOSTINGKEY();
Vector VACCOUNT =accounting.getVACCOUNT();
Vector VCURRENCYAMOUNT =accounting.getVCURRENCYAMOUNT();
Vector VBUSINESSAREA =accounting.getVBUSINESSAREA();
Vector VITEMTEXT =accounting.getVITEMTEXT();
Vector VSHORTTEXT = accounting.getVSHORTTEXT();

%>

<body <%if(!msg.equals("")){ %>onload="Do_close('<%=accroid%>');"<%} %>>

<%@ include file="../home/loading.jsp"%>
<form method="post" action="../servlet/Frm_Accounting">

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
											<b>&nbsp;&nbsp;Doc. Type : <%=documentType %> (<%=counterparty_abbr%> AR) Normal document</b>
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
											<td ><div align="center"><font size="2" face="Courier New"><b><%=counterparty_nm%></b></font></div></td>
										<%}else{%>
											<td ><div align="center"><font size="2" face="Courier New"><b><%=VSHORTTEXT.elementAt(i) %></b></font></div></td>
										<%}%>	
										<td ><div align="right"><font size="2" face="Courier New"
											<%if(VITEMTEXT.elementAt(i).toString().contains("TDS")){%> 
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
									<%if(VBUSINESSAREA.contains("")){%>
										<font style="color: red;"><I><b>SAP Posting Approval Blocked ( Business Area Association pending !! )</b></I></font>
									<%}else if(inv_pdf_flag.equals("N")){ %>
										<%if(gen_type.equals("S")){%>
											<font style="color: red;"><I><b>SUN Posting Approval Blocked ( PDF Generation pending !! )</b></I></font>									
										<%}else{%>
											<font style="color: red;"><I><b>SAP Posting Approval Blocked ( PDF Generation pending !! )</b></I></font>									
										<%}%>
									<%}else {%>
										<%if(!sap_approval_flag.equals("Y")){ %>
											<%if (zeroTotal.equals("0.00")) { %>
												<%if(gen_type.equals("S")){%>
													<font style="color: blue;"><I><b>Do you want to Approve SUN XML Generation for Posting?</b></I></font>&nbsp;
												<%}else{%>
													<font style="color: blue;"><I><b>Do you want to Approve SAP XML Generation for Posting?</b></I></font>&nbsp;
												<%} %>
												<input type="radio" name="rd" value="Y" onclick="doSubmit();">&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
											<%}else {%>
												<%if(gen_type.equals("S")){%>
													<font style="color: red;"><I><b>SUN Posting Approval Blocked ( Re-check Invoice !! )</b></I></font>
												<%}else{%>
													<font style="color: red;"><I><b>SAP Posting Approval Blocked ( Re-check Invoice !! )</b></I></font>
												<%}%>
											<%} %>
										<%}else {%>
											<%if (sap_msg_status.equals("S")){%>
												<%if(gen_type.equals("S")){%>
													<font style="color: green;"><I><b>Success SUN Application document posted [<%=sap_doc_no%>]!</b></I></font>
												<%}else{%>
													<font style="color: green;"><I><b>Success SAP Application document posted [<%=sap_doc_no%>]!</b></I></font>
												<%}%>				
											<%}else if (sap_msg_status.equals("E")){%>
												<%if(gen_type.equals("S")){%>
													<font style="color: red;"><I><b>Error SUN Application document not posted!</b></I></font>
												<%}else{%>
													<font style="color: red;"><I><b>Error SAP Application document not posted!</b></I></font>
												<%} %>
											<%}else{%>
												<%if(gen_type.equals("S")){%>
													<font style="color: green;"><I><b>Approved SUN XML Generated for Posting!</b></I></font>
												<%}else{%>
													<font style="color: green;"><I><b>Approved SAP XML Generated for Posting!</b></I></font>
												<%}%>
											<%} %>
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


<input type="hidden" name="option" value="CRDR_INVOICE_SAP_APPROVE">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="invoice_no" value="<%=invoice_no%>">
<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="type_flag" value="<%=type_flag%>">
<input type="hidden" name="invoice_type" value="<%=invoice_type%>">
<input type="hidden" name="bu_state_tin" value="<%=bu_state_tin%>">
<input type="hidden" name="sap_approval_flag" value="<%=sap_approval_flag%>">
<input type="hidden" name="gen_type" value="<%=gen_type%>">

<input type="hidden" name="accroid" value="<%=accroid%>">

<input type="hidden" name="read_access" value="<%=read_access%>">
<input type="hidden" name="write_access" value="<%=write_access%>">
<input type="hidden" name="check_access" value="<%=check_access%>">
<input type="hidden" name="print_access" value="<%=print_access%>">
<input type="hidden" name="delete_access" value="<%=delete_access%>">  	
<input type="hidden" name="audit_access" value="<%=audit_access%>">
<input type="hidden" name="authorize_access" value="<%=authorize_access%>">
<input type="hidden" name="approve_access" value="<%=approve_access%>">
<input type="hidden" name="execute_access" value="<%=execute_access%>">
<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<input type="hidden" name="u" value="<%=u%>">

</form>
</body>
</html>