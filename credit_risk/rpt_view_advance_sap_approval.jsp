
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
function Do_close(gx)
{
	window.opener.refresh(gx);
	//window.close();
}

function doSubmit()
{
	var msg="";
	var flag=true;
	
	var approve_access = document.forms[0].approve_access.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var counterparty_nm = document.forms[0].counterparty_nm.value;
	var counterparty_abbr = document.forms[0].counterparty_abbr.value;
	var sec_type = document.forms[0].sec_type.value;
	var value = document.forms[0].value.value;
	var currency = document.forms[0].currency.value;
	var sec_ref_no = document.forms[0].sec_ref_no.value;
	var status = document.forms[0].status.value;
	var sec_type = document.forms[0].sec_type.value;
	var gx = document.forms[0].gx.value;
	var sap_approval_flag = document.forms[0].sap_approval_flag.value;
	var isReversal = document.forms[0].isReversal.value;
	if(isReversal=="Y")
	{
		isReversal  = "";
	}
	else
	{
		isReversal = document.forms[0].isReversal.value;
	}
	var deal_no = document.forms[0].deal_no.value;
	var sec_category = document.forms[0].sec_category.value;
	
	var old_value = "CP="+counterparty_cd+"#NAME="+counterparty_nm+"#ABBR="+counterparty_abbr+"#SEC_TYPE="+sec_type+"#SEC_CATEGORY="+sec_category+"#DEAL_TYPE="+""+"#DEAL_NO="+deal_no+"#VALUE="+value+"#CURRENCY="+currency+
					"#FLUCTUATION="+""+"#VARIATION="+""+"#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+
				    "#CONF_BANK_REF="+""+"#RECIEVED_DT="+""+"#REVIEW_DT="+""+"#ISSUANCE_DT="+""+"#EXPIRY_DT="+""+"#STATUS="+status+"#TENOR="+""+"#REMARK="+""+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+sec_ref_no+"#CANCEL_DT="+""+"#GX="+gx+"#REVERSAL="+isReversal+"#SAP_APPROVAL="+sap_approval_flag;

	document.forms[0].old_value.value=old_value;
	
	if(approve_access!="Y")
	{
		alert("You dont have Approval Rights for this page!");
	}
	else
	{
		if(flag)
		{
			var a=confirm("On your Approval Invoice data will be Sent for SAP Posting. \n Are you Sure want to Proceed? ");
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
}
</script>
</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DataBean_Advance" id="credit_risk" scope="request"></jsp:useBean>
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


String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String clearance=request.getParameter("clearance")==null?"":request.getParameter("clearance");
String seq_no=request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
String seq_rev_no=request.getParameter("seq_rev_no")==null?"":request.getParameter("seq_rev_no");
String sap_approval_flag=request.getParameter("sap_approval_flag")==null?"":request.getParameter("sap_approval_flag");

String xmlfile_nm=request.getParameter("xmlfile_nm")==null?"":request.getParameter("xmlfile_nm");
String sec_ref_no=request.getParameter("sec_ref_no")==null?"":request.getParameter("sec_ref_no");
String isReversal=request.getParameter("isReversal")==null?"":request.getParameter("isReversal");
String isPrintPdf=request.getParameter("isPrintPdf")==null?"":request.getParameter("isPrintPdf");
String sec_int_ref=request.getParameter("sec_int_ref")==null?"":request.getParameter("sec_int_ref");

String activity_value = ""; 

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
	credit_risk.setCallFlag("PARSE_SAP_XML");
}
else 
{
	credit_risk.setCallFlag("ADV_SAP_XML");
}	
credit_risk.setRequest(request);

credit_risk.setCounterparty_cd(counterparty_cd);
credit_risk.setGx(clearance);
credit_risk.setSeq_no(seq_no);
credit_risk.setSeq_rev_no(seq_rev_no);
credit_risk.setComp_cd(owner_cd);
credit_risk.setXmlfile_name(xmlfile_nm);
credit_risk.setFile_path(appPath);
//credit_risk.setBu_state_tin(bu_state_tin); 
credit_risk.setSap_approval_flag(sap_approval_flag);
credit_risk.setSec_ref_no(sec_ref_no);
credit_risk.setIsReversal(isReversal);
credit_risk.setEmp_cd(emp_cd);
credit_risk.setSec_int_ref(sec_int_ref);
credit_risk.init();

String documentType=credit_risk.getDocumentType();
String documentDate=credit_risk.getDocumentDate();
String postingDate=credit_risk.getPostingDate();
String accountingPeriodMonth=credit_risk.getAccountingPeriodMonth();
String accountingPeriodYear=credit_risk.getAccountingPeriodYear();
String headerCompanyCode=credit_risk.getHeaderCompanyCode();
String docHeaderText=credit_risk.getDocHeaderText();
String refNum =credit_risk.getRefNum();
String currency=credit_risk.getCurrency();
String documentNo=credit_risk.getDocumentNo();
String counterparty_nm = credit_risk.getCounterparty_nm();
String counterparty_abbr = credit_risk.getCounterparty_abbr();
String sec_type = credit_risk.getSec_type();
String status = credit_risk.getStatus();
String value = credit_risk.getValue();
String deal_no = credit_risk.getDealno();
String sec_category = credit_risk.getSec_category();

String zeroTotal =credit_risk.getZeroTotal();

String sap_msg_status = credit_risk.getSap_msg_status();
String sap_doc_no = credit_risk.getSap_doc_no();
String sap_ack_msg = credit_risk.geSap_ack_msg();

Vector VLINESEQNO =credit_risk.getVLINESEQNO();
Vector VPOSTINGKEY =credit_risk.getVPOSTINGKEY();
Vector VACCOUNT =credit_risk.getVACCOUNT();
Vector VCURRENCYAMOUNT =credit_risk.getVCURRENCYAMOUNT();
Vector VBUSINESSAREA =credit_risk.getVBUSINESSAREA();
Vector VITEMTEXT =credit_risk.getVITEMTEXT();
Vector VSEC_TYPE =credit_risk.getVSEC_TYPE();
Vector VSTATUS =credit_risk.getVSTATUS();
Vector VSHORTTEXT = credit_risk.getVSHORTTEXT();

%>

<body <%if(!msg.equals("")){ %>onload="Do_close('<%=clearance%>');"<%} %>>

<%@ include file="../home/loading.jsp"%>
<form method="post" action="../servlet/Frm_Advance">

<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	SAP P80 Post View <%if(isReversal.equals("Y")){%>(Reversal)<%}%>
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
											<%if(VITEMTEXT.elementAt(i).toString().startsWith("TDS")){%> 
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
									<%}else if(sap_approval_flag.equals("Y")) {%>
										<font style="color: green;"><I><b>Approved SAP XML Generated for Posting!</b></I></font>
									<%} else if(!sap_approval_flag.equals("Y") && (isPrintPdf.equals("Y") || isPrintPdf.equals("X"))){ %>
										<%if(!sap_approval_flag.equals("Y")) {%>
											<%if (zeroTotal.equals("0.00")) { %>
												<font style="color: blue;"><I><b>Do you want to Approve SAP XML Generation for Posting?</b></I></font>&nbsp;
												<input type="radio" name="rd" value="Y" onclick="doSubmit();">&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
											<%}else {%>
												<font style="color: red;"><I><b>SAP Posting Approval Blocked ( Re-check Advance Entry !! )</b></I></font>
											<%} %>
										<%}else{ %>
											<%if (sap_msg_status.equals("S")){%>
												<font style="color: green;"><I><b>Success SAP Application document posted [<%=sap_doc_no%>]!</b></I></font>
											<%}else if (sap_msg_status.equals("E")){%>
												<font style="color: red;"><I><b>Error SAP Application document not posted!</b></I></font>
											<%} %>
										<%} %>
									<%}else if(!isPrintPdf.equals("Y")){%>
										<font style="color: red;"><I><b>SAP XML Generation will be enabled post print PDF!</b></I></font>&nbsp;
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

<input type="hidden" name="option" value="ADV_SAP_APPROVE">

<input type="hidden" name="old_value" value="">
<input type="hidden" name="sec_type" value="<%=sec_type%>">
<input type="hidden" name="status" value="<%=status%>">
<input type="hidden" name="value" value="<%=value%>">
<input type="hidden" name="currency" value="<%=currency%>">
<input type="hidden" name="deal_no" value="<%=deal_no%>">
<input type="hidden" name="sec_category" value="<%=sec_category%>">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="counterparty_nm" value="<%=counterparty_nm%>">
<input type="hidden" name="counterparty_abbr" value="<%=counterparty_abbr%>">
<input type="hidden" name="gx" value="<%=clearance%>">
<input type="hidden" name="seq_no" value="<%=seq_no%>">
<input type="hidden" name="seq_rev_no" value="<%=seq_rev_no%>">
<input type="hidden" name="sap_approval_flag" value="<%=sap_approval_flag%>">
<input type="hidden" name="sec_ref_no" value="<%=sec_ref_no%>">
<input type="hidden" name="isReversal" value="<%=isReversal%>">

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