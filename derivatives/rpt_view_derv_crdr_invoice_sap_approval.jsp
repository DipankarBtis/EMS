
<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Derivatives Invoice FO Approval</title>

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
	var inv_type = document.forms[0].invoice_type.value;
	var inv="";
	
	if(inv_type=="R")
	{
		inv="Remittance";
	}
	else
	{
		inv="Invoice";
	}
	
	var msg="";
	var flag=true;
	
	if(approve_access=="N")
	{
		msg+="You don't have Approval Rights!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a=confirm("On your Approval "+inv+" data will be Sent for SAP Posting. \n Are you Sure want to Proceed? ");
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

<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Invoice" id="derv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String crdr_inv_no=request.getParameter("crdr_inv_no")==null?"":request.getParameter("crdr_inv_no");
String sel_inv_no=request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
String sap_approval_flag=request.getParameter("sap_approval_flag")==null?"":request.getParameter("sap_approval_flag");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String instrument_no=request.getParameter("instrument_no")==null?"":request.getParameter("instrument_no");
String crdr_type=request.getParameter("crdr_type")==null?"":request.getParameter("crdr_type");

String xmlfile_nm=request.getParameter("xmlfile_nm")==null?"":request.getParameter("xmlfile_nm");
String inv_pdf_flag=request.getParameter("inv_pdf_flag")==null?"":request.getParameter("inv_pdf_flag");

String activity_value = ""; //purchase.getActivity_value();

String workDir=CommonVariable.work_dir+owner_cd;
String sapxml_dir="";
if(sap_approval_flag.equals("Y"))
{
	sapxml_dir=CommonVariable.sap_xml;
}
else
{
	sapxml_dir=CommonVariable.temp_sap_xml;
}

String appPath = request.getServletContext().getRealPath(workDir+"/"+sapxml_dir+"/");

/*String[] all_agmt_no = agmt_no.split(", ");
String[] all_cont_type = contract_type.split(", ");
String[] all_cont_no = cont_no.split(", ");
String[] all_instrument_no = instrument_no.split(", ");*/

//for(int i=0;i<all_cont_no.length;i++)
{
	if(sap_approval_flag.equals("Y"))
	{
		derv.setCallFlag("CRDR_PARSE_SAP_XML");
	}
	else
	{
		derv.setCallFlag("CRDR_DERV_SAP_XML");
	}
	
	derv.setSap_approval_flag(sap_approval_flag);
	derv.setRequest(request);
	//derv.setCont_type(all_cont_type[i]);
	derv.setFy_year(financial_year);
	derv.setInvoice_seq(invoice_seq);
	derv.setCounterparty_cd(counterparty_cd);
	derv.setInvoice_no(crdr_inv_no);
	derv.setSel_inv_no(sel_inv_no);
	derv.setCrdr_type(crdr_type);
	derv.setInv_type(invoice_type);
	derv.setComp_cd(owner_cd);
	derv.setXmlfile_name(xmlfile_nm);
	derv.setFile_path(appPath);
	derv.setBu_state_tin(bu_state_tin);
	//derv.setAgmt_no(all_agmt_no[i]);
	//derv.setCont_no(all_cont_no[i]);
	//derv.setInstrument_no(all_instrument_no[i]);
	derv.setEmp_cd(emp_cd);
	derv.init();
}


String documentType=derv.getDocumentType();
String documentDate=derv.getDocumentDate();
String postingDate=derv.getPostingDate();
String accountingPeriodMonth=derv.getAccountingPeriodMonth();
String accountingPeriodYear=derv.getAccountingPeriodYear();
String headerCompanyCode=derv.getHeaderCompanyCode();
String docHeaderText=derv.getDocHeaderText();
String refNum =derv.getRefNum();
String currency=derv.getCurrency();
String documentNo=derv.getDocumentNo();
String counterparty_nm = derv.getCounterparty_nm();
String counterparty_abbr = derv.getCounterparty_abbr();

String zeroTotal =derv.getZeroTotal();

Vector VLINESEQNO =derv.getVLINESEQNO();
Vector VPOSTINGKEY =derv.getVPOSTINGKEY();
Vector VACCOUNT =derv.getVACCOUNT();
Vector VCURRENCYAMOUNT =derv.getVCURRENCYAMOUNT();
Vector VBUSINESSAREA =derv.getVBUSINESSAREA();
Vector VITEMTEXT =derv.getVITEMTEXT();
Vector VSHORTTEXT = derv.getVSHORTTEXT();

%>

<body <%if(!msg.equals("")){ %>onload="Do_close('<%=accroid%>');"<%} %>>

<%@ include file="../home/loading.jsp"%>
<form method="post" action="../servlet/Frm_Derivatives_Invoice">

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
											<b>&nbsp;&nbsp;Doc. Type : <%=documentType %> (<%=counterparty_abbr%> <%if(invoice_type.equals("R")){ %>AP<%}else{ %>AR<%} %>) Normal document</b>
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
										<td ><div align="right"><font size="2" face="Courier New"><b><%=VCURRENCYAMOUNT.elementAt(i) %></b></font></div></td>
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
										<font style="color: red;"><I><b>SAP Posting Approval Blocked ( PDF Generation pending !! )</b></I></font>									
									<%}else {%>
										<%if(!sap_approval_flag.equals("Y")){ %>
											<%if (zeroTotal.equals("0.00")) { %>
												<font style="color: blue;"><I><b>Do you want to Approve SAP XML Generation for Posting?</b></I></font>&nbsp;
												<input type="radio" name="rd" value="Y" onclick="doSubmit();">&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
											<%}else {%>
												<font style="color: red;"><I><b>SAP Posting Approval Blocked ( Re-check Invoice !! )</b></I></font>
											<%} %>
										<%}else{ %>
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


<input type="hidden" name="option" value="DERV_CRDR_SAP_APPROVE">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="invoice_no" value="<%=crdr_inv_no%>">
<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="invoice_type" value="<%=invoice_type%>">
<input type="hidden" name="crdr_type" value="<%=crdr_type%>">
<input type="hidden" name="sel_inv_no" value="<%=sel_inv_no%>">
<input type="hidden" name="bu_state_tin" value="<%=bu_state_tin%>">
<input type="hidden" name="sap_approval_flag" value="<%=sap_approval_flag%>">

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