<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="dlng_inv" scope="request"></jsp:useBean>
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
String accroid=request.getParameter("accroid")==null?"":request.getParameter("accroid");
String inv_flag=request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
String sell_cont_map=request.getParameter("sell_cont_map")==null?"":request.getParameter("sell_cont_map");


String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");


dlng_inv.setCallFlag("DLNG_SERVICE_INVOICE_REPORT");
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
dlng_inv.setSell_cont_map(sell_cont_map);
dlng_inv.setIs_attachment("2");
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
String sac_no=dlng_inv.getSac_no();
String activity_value = dlng_inv.getActivity_value();
String contRef=dlng_inv.getContRef();
String dda_dt=dlng_inv.getDda_dt();
String signingDt=dlng_inv.getSigningDt();
String agmtSigningDt=dlng_inv.getAgmtSigningDt();

String unit_desc=dlng_inv.getUnit_desc();
String price_cd_nm=dlng_inv.getPrice_cd_nm();
String net_payable=dlng_inv.getNet_payable();

String total_exchng_label=dlng_inv.getTotal_exchng_label();
String total_exchng_label_rate=dlng_inv.getTotal_exchng_label_rate();
String source=dlng_inv.getSource();

Vector VATT2_EXCHANGE_DESC = dlng_inv.getVATT2_EXCHANGE_DESC();
Vector VATT2_RATE = dlng_inv.getVATT2_RATE();
%>
<body>
<form action="">
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
			<div align="center"><font size="4" face="Arial"><b>ATTACHMENT 2 - Exchange Rate Applicable</b></font></div>
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
	<tr valign="middle"><td colspan="7">&nbsp;</td></tr>
</table>
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<%for(int i=0; i<VATT2_EXCHANGE_DESC.size(); i++){ %>
	<tr>
		<td colspan="1" width="40%">
			<div align="left">
				<font size="1.5px" face="Arial"><%=VATT2_EXCHANGE_DESC.elementAt(i) %></font>
			</div>
		</td>
		<td colspan="1" width="20%">
			<div align="left">
				<font size="1.5px" face="Arial"><%=VATT2_RATE.elementAt(i) %></font>
			</div>
		</td>
	</tr>
	<%} %>
	<tr><td colspan="2"></td></tr>
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
	</tr>
	<tr>
		<td colspan="2" width="40%">
			<div align="left">
				<font size="1.5px" face="Arial"><b>Source : <%=source%></b></font>
			</div>
		</td>
	</tr>
</table>
</form>
</body>
</html>