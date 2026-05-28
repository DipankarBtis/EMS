<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Free Flow Invoice</title>
<link rel="stylesheet" type="text/css" href="../font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="../css/common.css">
<%@ include file="../util/common_val.jsp" %>
<script>
function setInvoiceNo()
{
	var e = document.forms[0].invoice_id_seq;
	var invoiceNo = e.options[e.selectedIndex].text;
	if(document.forms[0].invoice_id_seq.value=="")
	{
		document.forms[0].invoice_no.value="";
	}
	else
	{
		document.forms[0].invoice_no.value=invoiceNo;
	}
}
function doSubmit()
{
	var msg="";
	var flag=true;
	
	var activityFlag = document.forms[0].activityFlag.value;
	var check_access = document.forms[0].check_access.value;
	var approve_access = document.forms[0].approve_access.value;
	
	var invoice_no = document.forms[0].invoice_no.value;
	if(activityFlag=="APPROVE")
	{
		if(approve_access=="N")
		{
			msg+="You don't have Approval Rights!\n";
			flag=false;
		}
		else
		{
			var invoice_id_seq = document.forms[0].invoice_id_seq.value;
			
			if(document.forms[0].rd[0].checked)
			{
				if(invoice_id_seq=="")
				{
					msg+="Select Invoice Seq No!\n";
					flag=false;
				}
				if(invoice_no=="")
				{
					msg+="Select Invoice No is Missing!\n";
					flag=false;
				}
			}
		}
	}
	else if(activityFlag=="CHECK")
	{
		if(check_access=="N")
		{
			msg+="You don't have Check Rights!\n";
			flag=false;
		}
	}
	
	if(flag)
	{
		var activity_nm=""
		var invNo="";
		if(activityFlag=="CHECK")
		{
			if(document.forms[0].rd[0].checked)
			{
				activity_nm="Check"
				invNo+="Do you want to "+activity_nm+" DLNG Invoice?"
			}
			else if(document.forms[0].rd[1].checked)
			{
				activity_nm="Reject"
				invNo+="Do you want to "+activity_nm+" DLNG Invoice?\n\n"	
			}
		}
		else if(activityFlag=="APPROVE")
		{
			if(document.forms[0].rd[0].checked)
			{
				invNo="Invoice No : "+invoice_no+"\n\n";
				activity_nm="Approve"
				invNo+="Do you want to "+activity_nm+" DLNG Invoice?"
			}
			else if(document.forms[0].rd[1].checked)
			{
				activity_nm="Reject"
				invNo+="Do you want to "+activity_nm+" DLNG Invoice?\n\n"
				invNo+="Selected Invoice# will be Removed!";
				
				document.forms[0].invoice_id_seq.value="";
				document.forms[0].invoice_no.value="";
			}
		}
		var a=confirm(invNo);
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
function Do_close(msg,msg_type,accroid)
{
	window.opener.refershPar(msg,msg_type,accroid);
	window.close();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="sales_inv" scope="request"></jsp:useBean>
<%
//String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
//String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String period_start_dt=request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String activityFlag=request.getParameter("activityFlag")==null?"":request.getParameter("activityFlag");
String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");

String mapping_id=request.getParameter("mapping_id")==null?"":request.getParameter("mapping_id");
String address_type=request.getParameter("address_type")==null?"":request.getParameter("address_type");
String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");

if(!mapping_id.equals("") && !mapping_id.equals("0"))
{
	String[] temp = mapping_id.split("-");
	contract_type=temp[0];
	agmt_no=temp[1];
	agmt_rev=temp[2];
	cont_no=temp[3];
	cont_rev=temp[4];
	if(temp.length==6)
	{
		cargo_no=temp[5];
	}
}

//String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
//String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");

sales_inv.setCallFlag("DLNG_FFLOW_INVOICE_REPORT");
sales_inv.setComp_cd(owner_cd);
sales_inv.setCounterparty_cd(counterparty_cd);
sales_inv.setAgmt_no(agmt_no);
sales_inv.setAgmt_rev_no(agmt_rev);
sales_inv.setCont_no(cont_no);
sales_inv.setCont_rev_no(cont_rev);
sales_inv.setCargo_no(cargo_no);
sales_inv.setContract_type(contract_type);
sales_inv.setAddress_type(address_type);
sales_inv.setBilling_cycle(billing_cycle);
sales_inv.setPeriod_start_dt(period_start_dt);
sales_inv.setPeriod_end_dt(period_end_dt);
sales_inv.setBu_unit(bu_unit);
sales_inv.setFinancial_year(financial_year);
sales_inv.setBu_state_tin(bu_state_tin);
sales_inv.setActivityFlag(activityFlag);
sales_inv.setInvoice_seq(invoice_seq);
sales_inv.setInvoice_type(invoice_type);
sales_inv.init();

String couterpty_nm = sales_inv.getCouterpty_nm();
String bu_contact_person_cd = sales_inv.getBu_contact_person_cd();
String contact_person_cd = sales_inv.getContact_person_cd();
String invoice_no=sales_inv.getInvoice_no();
String invoice_id_seq=sales_inv.getInvoice_id_seq();
String invoice_dt = sales_inv.getInvoice_dt();
String invoice_due_dt = sales_inv.getInvoice_due_dt();
String invoice_amt = sales_inv.getInvoice_amt();
String inv_period_start_dt=sales_inv.getInv_period_start_dt();
String inv_period_end_dt=sales_inv.getInv_period_end_dt();
String remark_1 =sales_inv.getRemark_1();
String remark_2 =sales_inv.getRemark_2();
String amount_in_word=sales_inv.getAmount_in_word();
String note=sales_inv.getNote();
String other_inv_str=sales_inv.getOther_inv_str();
String invoice_raised_in=sales_inv.getInvoice_raised_in();

String bu_contact_person_nm = sales_inv.getBu_contact_person_nm();
String contact_person_nm = sales_inv.getContact_person_nm();
String plantAddress=sales_inv.getPlantAddress();
String plantCity=sales_inv.getPlantCity();
String plantState=sales_inv.getPlantState();
String plantPin=sales_inv.getPlantPin();
String plantNm=sales_inv.getPlantNm();

String bu_plantAddress=sales_inv.getBu_plantAddress();
String bu_plantCity=sales_inv.getBu_plantCity();
String bu_plantState=sales_inv.getBu_plantState();
String bu_plantPin=sales_inv.getBu_plantPin();
String bu_plantNm=sales_inv.getBu_plantNm();

String tax_info=sales_inv.getTax_info();
String bu_tax_info=sales_inv.getBu_tax_info();
String activity_value = sales_inv.getActivity_value();
String contRef=sales_inv.getContRef();
String dda_dt=sales_inv.getDda_dt();
String signingDt=sales_inv.getSigningDt();
String agmtSigningDt=sales_inv.getAgmtSigningDt();

String linked_invoice = sales_inv.getLinked_invoice();

Vector VPDF_COL1 = sales_inv.getVPDF_COL1();
Vector VPDF_COL2 = sales_inv.getVPDF_COL2();
Vector VPDF_COL3 = sales_inv.getVPDF_COL3();
Vector VPDF_COL4 = sales_inv.getVPDF_COL4();
Vector VPDF_COL5 = sales_inv.getVPDF_COL5();
Vector VPDF_COL6 = sales_inv.getVPDF_COL6();
Vector VPDF_COL7 = sales_inv.getVPDF_COL7();

Vector VINVOICE_ID_SEQ = sales_inv.getVINVOICE_ID_SEQ();
Vector VINVOICE_NO = sales_inv.getVINVOICE_NO();

String inv_nm="";
/* if(invoice_type.equals("CR"))
{
	inv_nm="Credit Note";
}
else if(invoice_type.equals("DR"))
{
	inv_nm="Debit Note";
}
else if(invoice_type.equals("LP"))
{
	inv_nm="Late Payment Invoice";
}
else if(invoice_type.equals("OR"))
{
	inv_nm="Other";
} */
inv_nm=other_inv_str;
%>
<body <%if(!msg.equals("")){ %>onload="Do_close('<%=msg%>','<%=msg_type%>','<%=accroid%>');"<%} %> onload="setInvoiceNo();">

<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_DLNG_Invoice">
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="2" face="Arial">
					<b>ORIGINAL</b>
					<br>
				</font>
				<font size="4" face="Arial">
					<b><%=owner_nm%></b>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="8" face="Arial" style="font-size: 16px;font-weight: bold;">	
					<%=inv_nm%>
				</font>
				<%if(contract_type.equals("Q") || contract_type.equals("O")) {%>
				<%if(!invoice_type.equals("CCR") && !invoice_type.equals("CDR")) {%>
				<font size="2" face="Arial">
					<br>
					<b>Tax Invoice issued under Rule 46 of the Central Goods and Services Tax Rules, 2017</b>
				</font>
				<%} %>
				<%} %>
			</div>
		</td>
	</tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="1px" face="Arial">
					<b></b>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">&nbsp;</div>
		</td>
	</tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="1px" face="Arial">
					<%-- In respect of 
					<%if(contract_type.equals("S")){ %>Supply Notice (SN-<%=agmt_no%>-<%=cont_no%>)
					<%}else if(contract_type.equals("L")){ %>Letter of Agreement (LOA-<%=cont_no%>)
					<%}else if(contract_type.equals("X")){ %>Exchange Transaction (TCN-<%=cont_no%>) 
					<%} %> executed on pursuant to Framework Gas Sales Agreement executed on <%=dda_dt%>
					<br>
					between <%=owner_nm%> and <%=couterpty_nm %> --%>
					
					In respect of 
					<%if(contract_type.equals("F")){ %>Supply Notice (<%=contRef%>) executed on <%=signingDt%> pursuant to Framework Gas Sales Agreement executed on <%=agmtSigningDt%>
					<%}else if(contract_type.equals("O")){ %><%-- CN (<%=contRef%>) executed on <%=signingDt%> pursuant to Framework --%> LTCORA Agreement executed on <%=agmtSigningDt%> & CN (<%=contRef%>) executed on <%=signingDt%>
					<%}else if(contract_type.equals("Q")){ %><%-- Period (<%=contRef%>) executed on <%=signingDt%> pursuant to Framework --%> LTCORA Agreement executed on <%=agmtSigningDt%>
					<%}else if(contract_type.equals("E")){ %>Letter of Agreement (<%=contRef%>) executed on <%=signingDt%> 
					<%}else if(contract_type.equals("W")){ %>Exchange Transaction (<%=contRef%>) executed on <%=signingDt%>
					<%} %> 
					<br>
					between <%=owner_nm%> and <%=couterpty_nm %>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">&nbsp;</div>
		</td>
	</tr>
	<tr valign="top">
		<td colspan="3" width="50%">
			<div align="left">
				<font size="1.5px" face="Arial">
					Business Unit:<%-- &nbsp;<%=bu_plantNm%> --%> 
					<br><%=owner_nm%>
					<br><%=bu_plantAddress%>,<%=bu_plantCity%>
					<br><%=bu_plantState%>&nbsp;-&nbsp;<%=bu_plantPin%>
				</font>
			</div>
		</td>
		<td colspan="1" width="10%"><div align="left"><font size="1.5px" face="Arial"><b>To:</b></font></div></td>
		<td colspan="3" width="40%">
			<div align="left">
				<font size="1.5px" face="Arial">
					<%=contact_person_nm%>,
					<br><%=couterpty_nm%>
					<br><%=plantAddress%>,<%=plantCity%>
					<br><%=plantState %>&nbsp;-&nbsp;<%=plantPin%>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="top"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="top">
		<td colspan="3">
			<div>
				<font size="1.5px" face="Arial">
					<%=bu_tax_info %>
				</font>
			</div>
		</td>
		<td colspan="1">&nbsp;</td>
 		<td colspan="3">
 			<div>
 				<font size="1.5px" face="Arial">
 					<%=tax_info %>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="top">
		<td colspan="7">&nbsp;</td>
	</tr>
	<tr valign="middle">
		<td colspan="4"></td>
		<td colspan="2" width="25%">
			<div align="center">
				<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b>Invoice Date:&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<%if(!invoice_due_dt.equals("")){ %>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b>Payment Due Date:&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<%} %>
					<tr>
						<td>
							<div align="right">
							<%if(invoice_type.equals("CR") || invoice_type.equals("CCR")) {%>
								<font size="1.5px" face="Arial"><b><%=owner_abbr%> Credit Note:&nbsp;</b></font>
							<%} else if(invoice_type.equals("DR") || invoice_type.equals("CDR")) {%>
								<font size="1.5px" face="Arial"><b><%=owner_abbr%> Debit Note:&nbsp;</b></font>
							<%} else {%>
								<font size="1.5px" face="Arial"><b><%=owner_abbr%> Invoice Seq No:&nbsp;</b></font>
							<%} %>
							</div>
						</td>
					</tr>
					<%if(!linked_invoice.equals("")){ %>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b>Reference Invoice#:&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<%} %>
				</table>
			</div>
		</td>
		<td colspan="1" width="15%">
			<div align="center">
				<table width="100%"  border="1" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=invoice_dt%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<%if(!invoice_due_dt.equals("")){ %>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=invoice_due_dt%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<%} %>
					<tr>
						<td>
						<%if(activityFlag.equals("APPROVE")){ %>
							<select name="invoice_id_seq" style="background:yellow;font-weight:bold;" onchange="setInvoiceNo();">
								<option value="">--Select--</option>
								<%for(int i=0; i<VINVOICE_ID_SEQ.size();i++){ %>
								<option value="<%=VINVOICE_ID_SEQ.elementAt(i)%>"><%=VINVOICE_NO.elementAt(i)%></option>
								<%} %>
							</select>
							<script>document.forms[0].invoice_id_seq.value="<%=invoice_id_seq%>"</script>
						<%}else{ %>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=invoice_no%>&nbsp;</b></font>
							</div>
						<%} %>
						</td>
					</tr>
					<%if(!linked_invoice.equals("")){ %>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=linked_invoice%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<%} %>
				</table>
			</div>
		</td>
	</tr>
	<%if(!invoice_type.equals("UG") && !invoice_type.equals("ST")){ %>
	<%-- <tr valign="middle">
		<td colspan="3">
			<div align="right">
				<font size="1.5px" face="Arial"><b>For the Billing Period</b></font>
			</div>
		</td>
		<td colspan="1">
			<div align="right">&nbsp;</div>
		</td>
		<td colspan="1" width="15%">
			<div align="center">
				<table width="100%"  border="1" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=inv_period_start_dt%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
		<td colspan="1" width="10%">
			<div align="center">
				<font size="1.5px" face="Arial"><b>to</b></font>
			</div>
		</td>
		<td colspan="1" width="15%">
			<div align="center">
				<table width="100%"  border="1" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=inv_period_end_dt%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr> --%>
	<%} %>
	<tr valign="middle"><td colspan="7">&nbsp;</td></tr>
</table>
<table width="100%"  border="1" align="center" cellpadding="2" cellspacing="0">
	<tr valign="bottom">
		<td width="6%"><div align="center"><font size="1.5px" face="Arial"><b>Sr. No.</b></font></div></td>
		<td width="34%"><div align="center"><font size="1.5px" face="Arial"><b>Item</b></font></div></td>
		<!-- <td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Attachment<br>Reference</b></font></div></td> -->
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Currency</b></font></div></td>
		<td width="15%"><div align="center"><font size="1.5px" face="Arial"><b>Quantity<br>(MMBTU)</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Rate</b></font></div></td>
		<td width="15%"><div align="center"><font size="1.5px" face="Arial"><b>Amount</b></font></div></td>
	</tr>
	<%for(int i=0; i<VPDF_COL1.size(); i++){ %>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=VPDF_COL1.elementAt(i) %></font>
			</div>
		</td>
		<td>
			<div <%if(Double.parseDouble(""+VPDF_COL1.elementAt(i))/(int)Double.parseDouble(""+VPDF_COL1.elementAt(i))>1){%>align="right"<%}else{%>align="left"<%} %>>
				<font size="1.5px" face="Arial"><%=VPDF_COL2.elementAt(i) %></font>
			</div>
		</td>
		<%-- <td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=VPDF_COL3.elementAt(i) %></font>
			</div>
		</td> --%>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=VPDF_COL4.elementAt(i) %></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VPDF_COL5.elementAt(i) %></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VPDF_COL6.elementAt(i) %></font>
			</div>
		</td>
		<td>
			<div <%if(Double.parseDouble(""+VPDF_COL1.elementAt(i))/(int)Double.parseDouble(""+VPDF_COL1.elementAt(i))>1){%>align="left"<%}else{%>align="right"<%} %>>
				<font size="1.5px" face="Arial"><%=VPDF_COL7.elementAt(i) %></font>
			</div>
		</td>
	</tr>
	<%} %>
</table>
<table border="0" width="100%" align="center" cellpadding="2" cellspacing="0">
	<tr align="center"><td colspan="7">&nbsp;</td></tr>
	<%-- <tr valign="middle">
		<td colspan="7">
			<div align="left">
				<font size="1px" face="Arial">
				Amount(in word) : <%=amount_in_word%>
				</font>
			</div>
		</td>
	</tr> --%>
	<tr valign="middle">
		<td colspan="7">
			<div align="left">
				<font size="1px" face="Arial"><%=note%></font>
			</div>
		</td>
	</tr>
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="left">
				<font size="1.5px" face="Arial"><b>For <%=owner_nm%><br><br><br><br>Authorised Signatory</b></font>
			</div>
		</td>
	</tr>
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
			<%if(activityFlag.equals("CHECK") || activityFlag.equals("APPROVE")){ %>
				<% if(activityFlag.equals("CHECK")){%>Checked<%}else if(activityFlag.equals("APPROVE")){ %>Approved<%} %> OK:&nbsp;
				<input type="radio" name="rd" value="Y" onClick="doSubmit();" <%if(activity_value.equals("Y")){ %>checked<%} %>>&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="radio" name="rd" value="N" onClick="doSubmit();" <%if(activity_value.equals("N")){ %>checked<%} %>>&nbsp;<b>No</b>
			<%} %>
			</div>
		</td>
	</tr>
</table>

<input type="hidden" name="option" value="F_FLOW_INVOICE_APPROVAL">
<input type="hidden" name="activityFlag" value="<%=activityFlag%>">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" value="<%=agmt_rev%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev" value="<%=cont_rev%>">
<input type="hidden" name="cargo_no" value="<%=cargo_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="address_type" value="<%=address_type%>">
<input type="hidden" name="period_start_dt" value="<%=period_start_dt%>">
<input type="hidden" name="period_end_dt" value="<%=period_end_dt%>">
<input type="hidden" name="billing_cycle" value="<%=billing_cycle%>">
<input type="hidden" name="bu_unit" value="<%=bu_unit%>">
<input type="hidden" name="bu_state_tin" value="<%=bu_state_tin%>">
<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
<input type="hidden" name="invoice_type" value="<%=invoice_type%>">
<input type="hidden" name="contract_ref" value="<%=contRef%>">
<input type="hidden" name="invoice_raised_in" value="<%=invoice_raised_in%>">

<input type="hidden" name="invoice_no" value="<%=invoice_no%>">
<input type="hidden" name="invoice_dt" value="<%=invoice_dt%>">
<input type="hidden" name="invoice_due_dt" value="<%=invoice_due_dt%>">
<input type="hidden" name="invoice_amt" value="<%=invoice_amt%>">
<input type="hidden" name="subject_line" value="<%=other_inv_str%>">

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