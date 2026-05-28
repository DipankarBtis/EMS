<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="sales_inv" scope="request"></jsp:useBean>
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
String sel_inv_no = request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");

String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");

sales_inv.setCallFlag("CRDR_INVOICE_REPORT");
sales_inv.setComp_cd(owner_cd);
sales_inv.setCounterparty_cd(counterparty_cd);
sales_inv.setAgmt_no(agmt_no);
sales_inv.setAgmt_rev_no(agmt_rev);
sales_inv.setCont_no(cont_no);
sales_inv.setCont_rev_no(cont_rev);
sales_inv.setCargo_no(cargo_no);
sales_inv.setContract_type(contract_type);
sales_inv.setPlant_seq(plant_seq);
sales_inv.setBilling_cycle(billing_cycle);
sales_inv.setPeriod_start_dt(period_start_dt);
sales_inv.setPeriod_end_dt(period_end_dt);
sales_inv.setTemp_period_start_dt(period_start_dt);
sales_inv.setTemp_period_end_dt(period_end_dt);
sales_inv.setBu_unit(bu_unit);
sales_inv.setFinancial_year(financial_year);
sales_inv.setBu_state_tin(bu_state_tin);
sales_inv.setActivityFlag(activityFlag);
sales_inv.setInvoice_seq(invoice_seq);
sales_inv.setInv_flag(inv_flag);
sales_inv.setIs_attachment("1");
sales_inv.setSel_inv_no(sel_inv_no);
sales_inv.init();

String couterpty_nm = sales_inv.getCouterpty_nm();
String bu_contact_person_cd = sales_inv.getBu_contact_person_cd();
String contact_person_cd = sales_inv.getContact_person_cd();
String invoice_no=sales_inv.getInvoice_no();
String invoice_id_seq=sales_inv.getInvoice_id_seq();
String invoice_dt = sales_inv.getInvoice_dt();
String invoice_due_dt = sales_inv.getInvoice_due_dt();
String inv_period_start_dt=sales_inv.getInv_period_start_dt();
String inv_period_end_dt=sales_inv.getInv_period_end_dt();
String remark_1 =sales_inv.getRemark_1();
String remark_2 =sales_inv.getRemark_2();

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

//String tax_info=sales_inv.getTax_info();
//String bu_tax_info=sales_inv.getBu_tax_info();
String activity_value = sales_inv.getActivity_value();
String contRef=sales_inv.getContRef();
String dda_dt=sales_inv.getDda_dt();
String signingDt=sales_inv.getSigningDt();
String agmtSigningDt=sales_inv.getAgmtSigningDt();

String invoice_raised_in = sales_inv.getInvoice_raised_in();
String invoice_raised_in_nm = sales_inv.getInvoice_raised_in_nm();
String price_cd=sales_inv.getPrice_cd();
String price_cd_nm=sales_inv.getPrice_cd_nm();
String exchng_rate_cd=sales_inv.getExchng_rate_cd();
String exchang_rate_dt=sales_inv.getExchang_rate_dt();

String qty_mmbtu=sales_inv.getQty_mmbtu();
String price=sales_inv.getPrice();
String exchang_rate=sales_inv.getExchang_rate();
String gross_amt=sales_inv.getGross_amt();
String gross_amt1=sales_inv.getGross_amt1();
String transportation_charges = sales_inv.getTransportation_charges();
String transportation_amount = sales_inv.getTransportation_amount();
String marketing_margin = sales_inv.getMarketing_margin();
String marketing_margin_amount = sales_inv.getMarketing_margin_amount();
String other_charges = sales_inv.getOther_charges();
String other_charges_amount = sales_inv.getOther_charges_amount();
String gross_include_transport_tariff = sales_inv.getGross_include_transport_tariff();
String tax_amt = sales_inv.getTax_amt();
String tax_struct_cd=sales_inv.getTax_struct_cd();
String tax_struct_dt=sales_inv.getTax_struct_dt();
String tax_struct_dtl=sales_inv.getTax_struct_dtl();
String tax_info = sales_inv.getTax_info();
String tax_factor = sales_inv.getTax_factor();
String invoice_amt = sales_inv.getInvoice_amt();
String net_payable = sales_inv.getNet_payable();
String tds_amt = sales_inv.getTds_amt();
String tds_factor = sales_inv.getTds_factor();
String tds_struct_cd = sales_inv.getTds_struct_cd();
String tds_struct_dt = sales_inv.getTds_struct_dt();

String applicable_flag = sales_inv.getApplicable_flag();
String applicable_abbr = sales_inv.getApplicable_abbr();

String applicable_amt = sales_inv.getApplicable_amt();
String TCS_factor = sales_inv.getTCS_factor();
String tcs_struct_cd = sales_inv.getTcs_struct_cd();
String tcs_struct_dt = sales_inv.getTcs_struct_dt();

String main_invoice_dt = sales_inv.getMain_invoice_dt();
String main_invoice_due_dt = sales_inv.getMain_invoice_due_dt();
String main_qty_mmbtu=sales_inv.getMain_qty_mmbtu();
String main_price=sales_inv.getMain_price();
String main_exchang_rate=sales_inv.getMain_exchang_rate();
String main_gross_amt=sales_inv.getMain_gross_amt();
String main_gross_amt1=sales_inv.getMain_gross_amt1();
String main_transportation_charges = sales_inv.getMain_transportation_charges();
String main_transportation_amount = sales_inv.getMain_transportation_amount();
String main_marketing_margin = sales_inv.getMain_marketing_margin();
String main_marketing_margin_amount = sales_inv.getMain_marketing_margin_amount();
String main_other_charges = sales_inv.getMain_other_charges();
String main_other_charges_amount = sales_inv.getMain_other_charges_amount();
String main_gross_include_transport_tariff = sales_inv.getMain_gross_include_transport_tariff();
String main_tax_amt = sales_inv.getMain_tax_amt();
String main_tax_struct_cd=sales_inv.getMain_tax_struct_cd();
String main_tax_struct_dt=sales_inv.getMain_tax_struct_dt();
String main_tax_struct_dtl=sales_inv.getMain_tax_struct_dtl();
String main_tax_info = sales_inv.getMain_tax_info();
String main_tax_factor = sales_inv.getMain_tax_factor();
String main_invoice_amt = sales_inv.getMain_invoice_amt();
String main_net_payable = sales_inv.getMain_net_payable();
String main_tds_amt = sales_inv.getMain_tds_amt();
String main_tds_factor = sales_inv.getMain_tds_factor();
String main_tds_struct_cd = sales_inv.getMain_tds_struct_cd();
String main_tds_struct_dt = sales_inv.getMain_tds_struct_dt();
String main_applicable_amt = sales_inv.getMain_applicable_amt();
String main_TCS_factor = sales_inv.getMain_TCS_factor();
String main_tcs_struct_cd = sales_inv.getMain_tcs_struct_cd();
String main_tcs_struct_dt = sales_inv.getMain_tcs_struct_dt();

String new_invoice_dt = sales_inv.getNew_invoice_dt();
String new_invoice_due_dt = sales_inv.getNew_invoice_due_dt();
String new_qty_mmbtu=sales_inv.getNew_qty_mmbtu();
String new_price=sales_inv.getNew_price();
String new_exchang_rate=sales_inv.getNew_exchang_rate();
String new_gross_amt=sales_inv.getNew_gross_amt();
String new_gross_amt1=sales_inv.getNew_gross_amt1();
String new_transportation_charges = sales_inv.getNew_transportation_charges();
String new_transportation_amount = sales_inv.getNew_transportation_amount();
String new_marketing_margin = sales_inv.getNew_marketing_margin();
String new_marketing_margin_amount = sales_inv.getNew_marketing_margin_amount();
String new_other_charges = sales_inv.getNew_other_charges();
String new_other_charges_amount = sales_inv.getNew_other_charges_amount();
String new_gross_include_transport_tariff = sales_inv.getNew_gross_include_transport_tariff();
String new_tax_amt = sales_inv.getNew_tax_amt();
String new_tax_struct_cd=sales_inv.getNew_tax_struct_cd();
String new_tax_struct_dt=sales_inv.getNew_tax_struct_dt();
String new_tax_struct_dtl=sales_inv.getNew_tax_struct_dtl();
String new_tax_info = sales_inv.getNew_tax_info();
String new_tax_factor = sales_inv.getNew_tax_factor();
String new_invoice_amt = sales_inv.getNew_invoice_amt();
String new_net_payable = sales_inv.getNew_net_payable();
String new_tds_amt = sales_inv.getNew_tds_amt();
String new_tds_factor = sales_inv.getNew_tds_factor();
String new_tds_struct_cd = sales_inv.getNew_tds_struct_cd();
String new_tds_struct_dt = sales_inv.getNew_tds_struct_dt();
String new_applicable_amt = sales_inv.getNew_applicable_amt();
String new_TCS_factor = sales_inv.getNew_TCS_factor();
String new_tcs_struct_cd = sales_inv.getNew_tcs_struct_cd();
String new_tcs_struct_dt = sales_inv.getNew_tcs_struct_dt();

String criteri_formula = sales_inv.getCriteri_formula();
String reason = sales_inv.getReason();

//boolean isGrossIncTriff = sales_inv.getIsGrossIncTriff();
Vector VMULTI_TAX_STRUCT = sales_inv.getVMULTI_TAX_STRUCT();
Vector VMAIN_MULTI_TAX_STRUCT = sales_inv.getVMAIN_MULTI_TAX_STRUCT();
Vector VNEW_MULTI_TAX_STRUCT = sales_inv.getVNEW_MULTI_TAX_STRUCT();

String headFont="";
String NormalFont="";
if(inv_flag.equals("CR")) 
{
	headFont="CREDIT NOTE";
	NormalFont="Credit Note";
}
else if(inv_flag.equals("DR"))
{ 
	headFont="DEBIT NOTE";
	NormalFont="Debit Note";
}

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
					<b>Business Unit:</b><%-- &nbsp;<%=bu_plantNm%>  --%>
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
			<div align="center"><font size="4" face="Arial"><b>ATTACHMENT 1 - <%=NormalFont%> Detail</b></font></div>
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
								<font size="1.5px" face="Arial"><b><%=NormalFont%> Date:&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<%if(!invoice_due_dt.equals("")){%>
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
								<font size="1.5px" face="Arial"><b><%=owner_abbr%> <%=NormalFont%> No:&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b>Invoice Ref:&nbsp;</b></font>
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
					<%if(!invoice_due_dt.equals("")){%>
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
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=invoice_no%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=sel_inv_no%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
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
	<tr valign="middle"><td colspan="7">&nbsp;</td></tr>
</table>
<table width="100%"  border="1" align="center" cellpadding="0" cellspacing="0">
	<tr valign="bottom">
		<td><div align="center"><font size="1.5px" face="Arial"><b></b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>UOM</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b><%=sel_inv_no%> [A]</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Revised Invoice [B]</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b><%=NormalFont%> [B-A]</b></font></div></td>
	</tr>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Allocated Qty</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial">MMBTU</font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_qty_mmbtu%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_qty_mmbtu%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=qty_mmbtu%></font>&nbsp;</div></td>
	</tr>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Confirmed Price</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><%=price_cd_nm%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_price%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_price%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=price%></font>&nbsp;</div></td>
	</tr>
	<%if(!price_cd.equals(invoice_raised_in)){ %>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Gross Amount</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><%=price_cd_nm%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_gross_amt%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_gross_amt%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=gross_amt%></font>&nbsp;</div></td>
	</tr>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Exchange Rate</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial">INR/USD</font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_exchang_rate%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_exchang_rate%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=exchang_rate%></font>&nbsp;</div></td>
	</tr>
	<%} %>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Gross Amount</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_gross_amt1%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_gross_amt1%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=gross_amt1%></font>&nbsp;</div></td>
	</tr>
	<%if(!transportation_charges.equals("")){ %>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Transportation Tariff</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><%=invoice_raised_in_nm%>/MMBTU</font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_transportation_charges%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_transportation_charges%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=transportation_charges%></font>&nbsp;</div></td>
	</tr>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Transportation Tariff Amount</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_transportation_amount%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_transportation_amount%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=transportation_amount%></font>&nbsp;</div></td>
	</tr>
	<%} %>
	<%if(!marketing_margin.equals("")){ %>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Marketing Margin</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><%=invoice_raised_in_nm%>/MMBTU</font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_marketing_margin%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_marketing_margin%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=marketing_margin%></font>&nbsp;</div></td>
	</tr>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Marketing Margin Amount</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_marketing_margin_amount%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_marketing_margin_amount%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=marketing_margin_amount%></font>&nbsp;</div></td>
	</tr>
	<%} %>
	<%if(!other_charges.equals("")){ %>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Other Charges</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><%=invoice_raised_in_nm%>/MMBTU</font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_other_charges%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_other_charges%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=other_charges%></font>&nbsp;</div></td>
	</tr>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Other Charges Amount</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_other_charges_amount%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_other_charges_amount%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=other_charges_amount%></font>&nbsp;</div></td>
	</tr>
	<%} %>
	<%-- <%if(isGrossIncTriff){ %>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Total Gross Amount</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_gross_include_transport_tariff%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_gross_include_transport_tariff%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=gross_include_transport_tariff%></font>&nbsp;</div></td>
	</tr>
	<%} %> --%>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Tax Amount</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial">(<%=main_tax_struct_dtl%>) <%=main_tax_amt%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial">(<%=new_tax_struct_dtl%>) <%=new_tax_amt%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=tax_amt%></font>&nbsp;</div></td>
	</tr>
	<%if(VMAIN_MULTI_TAX_STRUCT.size()>0){
		for(int i=0;i<VMAIN_MULTI_TAX_STRUCT.size();i++)
		{
			Vector main_temp =(Vector)((Vector)((Vector)VMAIN_MULTI_TAX_STRUCT.elementAt(i)));
			
			Vector new_temp =(Vector)((Vector)((Vector)VNEW_MULTI_TAX_STRUCT.elementAt(i)));
			
			Vector temp =(Vector)((Vector)((Vector)VMULTI_TAX_STRUCT.elementAt(i)));
			
			if(((Vector) main_temp.elementAt(0)).size() > 1)
			{
				for(int j=0;j<((Vector) main_temp.elementAt(0)).size(); j++)
				{%>
					<tr valign="top">
						<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b><%=((Vector) main_temp.elementAt(1)).elementAt(j)%></b></font>&nbsp;</div></td>
						<td><div align="center"><font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>&nbsp;</div></td>
						<td><div align="right"><font size="1.5px" face="Arial">(<%=((Vector) main_temp.elementAt(1)).elementAt(j)%>) <%=((Vector) main_temp.elementAt(2)).elementAt(j)%></font>&nbsp;</div></td>
						<td><div align="right"><font size="1.5px" face="Arial">(<%=((Vector) new_temp.elementAt(1)).elementAt(j)%>) <%=((Vector) new_temp.elementAt(2)).elementAt(j)%></font>&nbsp;</div></td>
						<td><div align="right"><font size="1.5px" face="Arial"><%=((Vector) temp.elementAt(2)).elementAt(j)%></font>&nbsp;</div></td>
					</tr>
				<%}
			}
		}
	} %>
	<tr valign="top">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Invoice Amount</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_invoice_amt%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_invoice_amt%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=invoice_amt%></font>&nbsp;</div></td>
	</tr>
	<tr valign="top" style="font-weight: bold;">
		<td><div align="left">&nbsp;<font size="1.5px" face="Arial"><b>Net Payable</b></font>&nbsp;</div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=main_net_payable%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=new_net_payable%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=net_payable%></font>&nbsp;</div></td>
	</tr>
</table>
<table border="0" width="100%" align="center" cellpadding="2" cellspacing="0">
	<tr align="center"><td colspan="7">&nbsp;</td></tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="left">
				<font size="1px" face="Arial">
				<b>Reason for <%=NormalFont%> :</b> <%=reason%>.
				<br><br>
				<b>Note :</b> Negative(-) Value indicate Credit and Postive(+) Value indicate Debit.
				</font>
			</div>
		</td>
	</tr>
</table>
</form>
</body>
</html>