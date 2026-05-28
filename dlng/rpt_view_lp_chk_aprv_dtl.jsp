<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>DLNG Late Payment Invoice</title>
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
				invNo+="Do you want to "+activity_nm+" Late Payment Invoice?"
			}
			else if(document.forms[0].rd[1].checked)
			{	
				activity_nm="Reject"
				invNo+="Do you want to "+activity_nm+" Late Payment Invoice?\n\n"	
			}
		}
		else if(activityFlag=="APPROVE")
		{
			if(document.forms[0].rd[0].checked)
			{
				invNo="Invoice No : "+invoice_no+"\n\n";
				activity_nm="Approve"
				invNo+="Do you want to "+activity_nm+" Late Payment Invoice?"
			}
			else if(document.forms[0].rd[1].checked)
			{
				activity_nm="Reject"
				invNo+="Do you want to "+activity_nm+" Late Payment Invoice?\n\n"
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

var newWindow_att1;
function openAtt1(url)
{
	if(!newWindow_att1 || newWindow_att1.closed)
	{
		newWindow_att1 = window.open(url,"Attachment1","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow_att1.close();
		newWindow_att1 = window.open(url,"Attachment1","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

var newWindow_att2;
function openAtt2(url)
{
	if(!newWindow_att2 || newWindow_att2.closed)
	{
		newWindow_att2 = window.open(url,"Attachment2","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow_att2.close();
		newWindow_att2 = window.open(url,"Attachment2","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="dlng_inv" scope="request"></jsp:useBean>
<%
//String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
//String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String plant_seq=request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String period_start_dt=request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
String bu_unit=request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String activityFlag=request.getParameter("activityFlag")==null?"":request.getParameter("activityFlag");
String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
String accroid=request.getParameter("accroid")==null?"":request.getParameter("accroid");
String inv_flag=request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");

String ori_inv_flag=request.getParameter("ori_inv_flag")==null?"":request.getParameter("ori_inv_flag");
String exist_fin_yr=request.getParameter("exist_fin_yr")==null?"":request.getParameter("exist_fin_yr");
String ori_inv_seq=request.getParameter("ori_inv_seq")==null?"":request.getParameter("ori_inv_seq");

String cont_start_dt=request.getParameter("cont_start_dt")==null?"":request.getParameter("cont_start_dt");
String cont_end_dt=request.getParameter("cont_end_dt")==null?"":request.getParameter("cont_end_dt");

String ori_inv_no=request.getParameter("ori_inv_no")==null?"":request.getParameter("ori_inv_no");

//String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
//String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");


String truck_cd=request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");
String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"":request.getParameter("truck_trans_cd");
String mapping_id=request.getParameter("mapping_id")==null?"":request.getParameter("mapping_id");

dlng_inv.setCallFlag("DLNG_LP_INVOICE_REPORT");
dlng_inv.setComp_cd(owner_cd);
dlng_inv.setCounterparty_cd(counterparty_cd);
dlng_inv.setAgmt_no(agmt_no);
dlng_inv.setAgmt_rev_no(agmt_rev);
dlng_inv.setCont_no(cont_no);
dlng_inv.setCont_rev_no(cont_rev);
dlng_inv.setCargo_no(cargo_no);
dlng_inv.setContract_type(contract_type);
dlng_inv.setPlant_seq(plant_seq);
dlng_inv.setBilling_cycle(billing_cycle);
dlng_inv.setPeriod_start_dt(period_start_dt);
dlng_inv.setPeriod_end_dt(period_end_dt);
dlng_inv.setBu_unit(bu_unit);
dlng_inv.setFinancial_year(financial_year);
dlng_inv.setBu_state_tin(bu_state_tin);
dlng_inv.setActivityFlag(activityFlag);
dlng_inv.setInvoice_seq(invoice_seq);
dlng_inv.setInv_flag(inv_flag);

dlng_inv.setOri_inv_flag(ori_inv_flag);
dlng_inv.setExist_financial_year(exist_fin_yr);
dlng_inv.setOri_invoice_seq(ori_inv_seq);
dlng_inv.setOri_invoice_no(ori_inv_no);
dlng_inv.setCont_start_dt(cont_start_dt);
dlng_inv.setCont_end_dt(cont_end_dt);
dlng_inv.setTruck_cd(truck_cd);
dlng_inv.setTruck_trans_cd(truck_trans_cd);
dlng_inv.setMapping_id(mapping_id);
dlng_inv.init();

String couterpty_nm = dlng_inv.getCouterpty_nm();
String bu_contact_person_cd = dlng_inv.getBu_contact_person_cd();
String contact_person_cd = dlng_inv.getContact_person_cd();
String invoice_no=dlng_inv.getInvoice_no();
String invoice_id_seq=dlng_inv.getInvoice_id_seq();
String invoice_dt = dlng_inv.getInvoice_dt();
String invoice_due_dt = dlng_inv.getInvoice_due_dt();
String invoice_amt = dlng_inv.getInvoice_amt();
String inv_period_start_dt=dlng_inv.getInv_period_start_dt();
String inv_period_end_dt=dlng_inv.getInv_period_end_dt();
String remark_1 =dlng_inv.getRemark_1();
String remark_2 =dlng_inv.getRemark_2();
String invoice_raised_in=dlng_inv.getInvoice_raised_in();

String bu_contact_person_nm = dlng_inv.getBu_contact_person_nm();
String contact_person_nm = dlng_inv.getContact_person_nm();
String plantAddress=dlng_inv.getPlantAddress();
String plantCity=dlng_inv.getPlantCity();
String plantState=dlng_inv.getPlantState();
String plantPin=dlng_inv.getPlantPin();
String plantNm=dlng_inv.getPlantNm();

String bu_plantAddress=dlng_inv.getBu_plantAddress();
String bu_plantCity=dlng_inv.getBu_plantCity();
String bu_plantState=dlng_inv.getBu_plantState();
String bu_plantPin=dlng_inv.getBu_plantPin();
String bu_plantNm=dlng_inv.getBu_plantNm();

String tax_info=dlng_inv.getTax_info();
String bu_tax_info=dlng_inv.getBu_tax_info();
String activity_value = dlng_inv.getActivity_value();
String contRef=dlng_inv.getContRef();
String dda_dt=dlng_inv.getDda_dt();
String signingDt=dlng_inv.getSigningDt();
String agmtSigningDt=dlng_inv.getAgmtSigningDt();

Vector VPDF_COL1 = dlng_inv.getVPDF_COL1();
Vector VPDF_COL2 = dlng_inv.getVPDF_COL2();
Vector VPDF_COL3 = dlng_inv.getVPDF_COL3();
Vector VPDF_COL4 = dlng_inv.getVPDF_COL4();
Vector VPDF_COL5 = dlng_inv.getVPDF_COL5();
Vector VPDF_COL6 = dlng_inv.getVPDF_COL6();
Vector VPDF_COL7 = dlng_inv.getVPDF_COL7();

Vector VINVOICE_ID_SEQ = dlng_inv.getVINVOICE_ID_SEQ();
Vector VINVOICE_NO = dlng_inv.getVINVOICE_NO();
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
					<br>
					<b>DEBIT NOTE</b>
				</font>
				<%if(contract_type.equals("B") || contract_type.equals("M")) {%>
				<font size="2" face="Arial">
					<br>
					<b>Tax Invoice issued under Rule 46 of the Central Goods and Services Tax Rules, 2017</b>
				</font>
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
					<%if(contract_type.equals("F")){ %>Supply Notice (<%=contRef%>) executed on <%=signingDt%> pursuant to Framework LNG Sales Agreement executed on <%=agmtSigningDt%>
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
					Business Unit:<%-- &nbsp;<%=bu_plantNm%>  --%>
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
								<font size="1.5px" face="Arial"><b>Debit Note Date:&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=owner_abbr%> Debit Note Seq No:&nbsp;</b></font>
							</div>
						</td>
					</tr>
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
				</table>
			</div>
		</td>
	</tr>
	<tr valign="middle"><td colspan="7">&nbsp;</td></tr>
</table>
<table width="100%"  border="1" align="center" cellpadding="2" cellspacing="0">
	<tr valign="bottom">
		<td width="6%"><div align="center"><font size="1.5px" face="Arial"><b>Sr. No.</b></font></div></td>
		<td width="34%"><div align="center"><font size="1.5px" face="Arial"><b>Item</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Data</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Currency</b></font></div></td>
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
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=VPDF_COL3.elementAt(i) %></font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=VPDF_COL4.elementAt(i) %></font>
			</div>
		</td>
		<%if(inv_flag.equals("ST") && i==0) {%>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial" color="blue"><%=VPDF_COL6.elementAt(i) %></font>
			</div>
		</td>
		<%}else{ %>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VPDF_COL6.elementAt(i) %></font>
			</div>
		</td>
		<%} %>
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
	<tr valign="middle">
		<td colspan="7">
			<div align="left">
				<font size="1px" face="Arial">
				<%=remark_1.replace("\n","<br>")%>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="left">
				<font size="1px" face="Arial"><%=remark_2%></font>
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

<input type="hidden" name="option" value="LP_INVOICE_APPROVAL">
<input type="hidden" name="activityFlag" value="<%=activityFlag%>">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>"> 
<input type="hidden" name="agmt_rev" value="<%=agmt_rev%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev" value="<%=cont_rev%>">
<input type="hidden" name="cargo_no" value="<%=cargo_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="plant_seq" value="<%=plant_seq%>">
<input type="hidden" name="period_start_dt" value="<%=period_start_dt%>">
<input type="hidden" name="period_end_dt" value="<%=period_end_dt%>">
<input type="hidden" name="billing_cycle" value="<%=billing_cycle%>">
<input type="hidden" name="bu_unit" value="<%=bu_unit%>">
<input type="hidden" name="bu_state_tin" value="<%=bu_state_tin%>">
<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
<input type="hidden" name="contract_ref" value="<%=contRef%>">
<input type="hidden" name="invoice_raised_in" value="<%=invoice_raised_in%>">

<input type="hidden" name="cont_start_dt" value="<%=cont_start_dt%>">
<input type="hidden" name="cont_end_dt" value="<%=cont_end_dt%>">

<input type="hidden" name="ori_inv_no" value="<%=ori_inv_no%>">
<input type="hidden" name="ori_inv_seq" value="<%=ori_inv_seq%>">
<input type="hidden" name="ori_inv_flag" value="<%=ori_inv_flag%>">
<input type="hidden" name="exist_fin_yr" value="<%=exist_fin_yr%>">
<input type="hidden" name="truck_cd" value="<%=truck_cd%>">
<input type="hidden" name="truck_trans_cd" value="<%=truck_trans_cd%>">
<input type="hidden" name="mapping_id" value="<%=mapping_id%>">

<input type="hidden" name="invoice_no" value="<%=invoice_no%>">
<input type="hidden" name="invoice_dt" value="<%=invoice_dt%>">
<input type="hidden" name="invoice_due_dt" value="<%=invoice_due_dt%>">
<input type="hidden" name="invoice_amt" value="<%=invoice_amt%>">

<input type="hidden" name="accroid" value="<%=accroid%>">
<input type="hidden" name="inv_flag" value="<%=inv_flag%>">

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