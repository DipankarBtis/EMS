<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Attachment 1</title>
</head>
<jsp:useBean class="com.etrm.fms.sales_invoice.DataBean_Sales_Drcr_note" id="Drcr_note" scope="request"></jsp:useBean>
<%
String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
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
String inv_flag=request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
String drcr_flag=request.getParameter("drcr_flag")==null?"":request.getParameter("drcr_flag");
String drcr_seq=request.getParameter("drcr_seq")==null?"":request.getParameter("drcr_seq");
String criteria=request.getParameter("criteria")==null?"":request.getParameter("criteria");
String drcr_fin_yr=request.getParameter("drcr_fin_yr")==null?"":request.getParameter("drcr_fin_yr");

String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");

Drcr_note.setCallFlag("DRCR_NOTE_REPORT");
Drcr_note.setComp_cd(owner_cd);
Drcr_note.setCounterparty_cd(counterparty_cd);
Drcr_note.setAgmt_no(agmt_no);
Drcr_note.setAgmt_rev_no(agmt_rev);
Drcr_note.setCont_no(cont_no);
Drcr_note.setCont_rev_no(cont_rev);
Drcr_note.setCargo_no(cargo_no);
Drcr_note.setContract_type(contract_type);
Drcr_note.setPlant_seq(plant_seq);
Drcr_note.setBilling_cycle(billing_cycle);
Drcr_note.setPeriod_start_dt(period_start_dt);
Drcr_note.setPeriod_end_dt(period_end_dt);
Drcr_note.setBu_unit(bu_unit);
Drcr_note.setFinancial_year(financial_year);
Drcr_note.setBu_state_tin(bu_state_tin);
Drcr_note.setActivityFlag(activityFlag);
Drcr_note.setInvoice_seq(invoice_seq);
Drcr_note.setInv_flag(inv_flag);
Drcr_note.setdrcr_seq(drcr_seq);
Drcr_note.setdrcr_flag(drcr_flag);
Drcr_note.setdrcr_fin_yr(drcr_fin_yr);
Drcr_note.setIs_attachment("1");
Drcr_note.init();

String couterpty_nm = Drcr_note.getCouterpty_nm();
String bu_contact_person_cd = Drcr_note.getBu_contact_person_cd();
String contact_person_cd = Drcr_note.getContact_person_cd();
String invoice_no=Drcr_note.getInvoice_no();
String invoice_id_seq=Drcr_note.getInvoice_id_seq();
String invoice_dt = Drcr_note.getInvoice_dt();
String invoice_due_dt = Drcr_note.getInvoice_due_dt();
String invoice_amt = Drcr_note.getInvoice_amt();
String inv_period_start_dt=Drcr_note.getInv_period_start_dt();
String inv_period_end_dt=Drcr_note.getInv_period_end_dt();
String remark_1 =Drcr_note.getRemark_1();
String remark_2 =Drcr_note.getRemark_2();

String bu_contact_person_nm = Drcr_note.getBu_contact_person_nm();
String contact_person_nm = Drcr_note.getContact_person_nm();
String plantAddress=Drcr_note.getPlantAddress();
String plantCity=Drcr_note.getPlantCity();
String plantState=Drcr_note.getPlantState();
String plantPin=Drcr_note.getPlantPin();
String plantNm=Drcr_note.getPlantNm();
String drcr_dt=Drcr_note.getdrcr_dt();

String bu_plantAddress=Drcr_note.getBu_plantAddress();
String bu_plantCity=Drcr_note.getBu_plantCity();
String bu_plantState=Drcr_note.getBu_plantState();
String bu_plantPin=Drcr_note.getBu_plantPin();
String bu_plantNm=Drcr_note.getBu_plantNm();

String tax_info=Drcr_note.getTax_info();
String bu_tax_info=Drcr_note.getBu_tax_info();
String activity_value = Drcr_note.getActivity_value();
String contRef=Drcr_note.getContRef();
String dda_dt=Drcr_note.getDda_dt();
String signingDt=Drcr_note.getSigningDt();
String agmtSigningDt=Drcr_note.getAgmtSigningDt();

String total_exchng_label = Drcr_note.getTotal_exchng_label();
String total_exchng_label_rate = Drcr_note.getTotal_exchng_label_rate();
String source = Drcr_note.getSource();

Vector VATT2_EXCHANGE_DESC = Drcr_note.getVATT2_EXCHANGE_DESC();
Vector VATT2_RATE = Drcr_note.getVATT2_RATE();

Vector VINVOICE_ID_SEQ = Drcr_note.getVINVOICE_ID_SEQ();
Vector VINVOICE_NO = Drcr_note.getVINVOICE_NO();

//Vector VINVOICE_NO = Drcr_note.getVINVOICE_NO();
Vector VPRICE = Drcr_note.getVPRICE();
//Vector VPLANT_ABBR = Drcr_note.getVPLANT_ABBR();
Vector VBOE_ENTRY = Drcr_note.getVBOE_ENTRY();
Vector VCARGO_NM = Drcr_note.getVCARGO_NM();

%>
<body>
<form action="">
<%if(inv_flag.equals("UG")) {%>
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="7">&nbsp;</td>
	</tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center"><font size="4" face="Arial"><b>ATTACHMENT 1 - Value of SUG</b></font></div>
		</td>
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
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b>Payment Due Date:&nbsp;</b></font>
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
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=invoice_due_dt%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
</table>	
<br>
<br>
<table width="70%" border="1" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td><div align="center"><font size="2px" face="Arial"><b>Bill Of Entry No.</b></font></div></td>
		<td><div align="center"><font size="2px" face="Arial"><b>Cargo Name</b></font></div></td>
		<td><div align="center"><font size="2px" face="Arial"><b>INR</b></font></div></td>
	</tr>
	<%for(int i=0;i<VCARGO_NM.size(); i++) {%>
	<tr>
		<td align="center"><font size="1.75px" face="Arial"><%=VBOE_ENTRY.elementAt(i) %></font></td>
		<td align="center"><font size="1.75px" face="Arial"><%=VCARGO_NM.elementAt(i) %></font></td>
		<td align="right"><font size="1.75px" face="Arial"><%=VPRICE.elementAt(i) %>&nbsp;</font></td>
	</tr>
	<%} %>
</table>
<br>
<br>
<br>
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td><div align="center"><font size="1px" face="Arial">The Price Of LNG Cargo Is Provided By <%=couterpty_nm%></font></div></td>
	</tr>
</table>
<%}else{%>
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="7">&nbsp;</td>
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
	<%-- <tr valign="top">
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
	</tr> --%>
	<tr valign="middle">
		<td colspan="7">
		<%if(criteria.equals("1")) {%>
			<div align="center"><font size="4" face="Arial"><b>ATTACHMENT 1 - Applicable Exchange Rate</b></font></div>
		<%} else if(criteria.equals("2")) {%>
			<div align="center"><font size="4" face="Arial"><b>ATTACHMENT 1 - Applicable Sales Rate</b></font></div>
		<%} else {%>
			<div align="center"><font size="4" face="Arial"><b>ATTACHMENT 1 - Applicable Quantity</b></font></div>
		<%} %>
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
								<%if(drcr_flag.equals("DR")) {%>
								<font size="1.5px" face="Arial"><b>Debit Note Date:&nbsp;</b></font>
							<%} else {%>
								<font size="1.5px" face="Arial"><b>Credit Note Date:&nbsp;</b></font>
							<%} %>
							</div>
						</td>
					</tr>
					<%if(invoice_due_dt.equals("")) {%>
					
					<%} else {%>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b>Payment Due Date:&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<% }%>
					
					<tr>
						<td>
							<div align="right">
								<%if(drcr_flag.equals("DR")) {%>
									<font size="1.5px" face="Arial"><b><%=owner_abbr%> Debit Note No:&nbsp;</b></font>
								<%} else {%>
									<font size="1.5px" face="Arial"><b><%=owner_abbr%> Credit Note No:&nbsp;</b></font>
								<%} %>	
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
								<font size="1.5px" face="Arial"><b><%=drcr_dt%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<%if(invoice_due_dt.equals("")) {%>
					
					<%} else {%>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=invoice_due_dt%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<% }%>
					<tr>
						<td>
							<div align="right">
							<%if(activityFlag.equals("CHECK")) {%>
									<font size="1.5px" face="Arial"><b>&nbsp;</b></font>
									<input type="hidden" name="drcr_invoice_no" value="">
							<%} else {%> 
								<select name="invoice_id_seq" style="background:yellow;font-weight:bold;" onchange="setInvoiceNo();">
								<option value="">--Select--</option>
								<%for(int i=0; i<VINVOICE_ID_SEQ.size();i++){ %>
								<option value="<%=VINVOICE_ID_SEQ.elementAt(i)%>"><%=VINVOICE_NO.elementAt(i)%></option>
								 <%} %> 
							</select> 
							<script>document.forms[0].invoice_id_seq.value="<%=invoice_id_seq%>"</script>
							<%} %>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<%if(!inv_flag.equals("ST")) {%>
	<tr valign="middle">
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
	</tr>
	<%} %>
	<tr valign="middle"><td colspan="7">&nbsp;</td></tr>
</table>
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
<%for(int i=0; i<VATT2_EXCHANGE_DESC.size(); i++){%>
	<tr>
		<td colspan="1" width="20%">
			<div align="left">
				<font size="1.5px" face="Arial"><%=VATT2_EXCHANGE_DESC.elementAt(i) %></font>
			</div>
		</td>
		<td colspan="1" width="20%">
			<div align="right">
				<font size="1.5px" face="Arial"><%=VATT2_RATE.elementAt(i) %></font>
			</div>
		</td>
		<td width="60%"></td>
	</tr>
	<%} %>
	<%-- <tr><td colspan="2"></td></tr>
	<tr>
		<td colspan="1" width="40%">
			<div align="left">
				<font size="1.5px" face="Arial"><b><%=total_exchng_label%></b></font>
			</div>
		</td>
		<td colspan="1" width="20%">
			<div align="left">
				<font size="1.5px" face="Arial"><b><%=total_exchng_label_rate%></b></font>
			</div>
		</td>
	</tr> --%>
	<%-- <tr>
		<td colspan="2" width="40%">
			<div align="left">
				<font size="1.5px" face="Arial"><b>Source : <%=source%></b></font>
			</div>
		</td>
	</tr> --%>
</table>
<%} %>
</form>
</body>
</html>