<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.sales_invoice.DataBean_Sales_Invoice" id="sales_inv" scope="request"></jsp:useBean>
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

String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");

sales_inv.setCallFlag("SALES_INVOICE_REPORT");
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
String price_cd_nm = sales_inv.getPrice_cd_nm();
String gross_amt = sales_inv.getGross_amt();

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

Vector VATT1_DATE = sales_inv.getVATT1_DATE();
Vector VATT1_DCQ = sales_inv.getVATT1_DCQ();
Vector VATT1_BUYNOM = sales_inv.getVATT1_BUYNOM();
Vector VATT1_SELLNOM_PNG = sales_inv.getVATT1_SELLNOM_PNG();
Vector VATT1_SELLNOM_REGAS = sales_inv.getVATT1_SELLNOM_REGAS();
Vector VATT1_NG_PNG = sales_inv.getVATT1_NG_PNG();
Vector VATT1_NG_REGAS = sales_inv.getVATT1_NG_REGAS();
Vector VATT1_NG_TOT_DLV_GAS = sales_inv.getVATT1_NG_TOT_DLV_GAS();
Vector VATT1_CUMULATIVE_QTY_BILLING_PERIOD = sales_inv.getVATT1_CUMULATIVE_QTY_BILLING_PERIOD();
Vector VATT1_CUMULATIVE_QTY_TRANSCT_SUPPLY_PERIOD = sales_inv.getVATT1_CUMULATIVE_QTY_TRANSCT_SUPPLY_PERIOD();

Vector VATT_INVOICE_NO = sales_inv.getVATT_INVOICE_NO();
Vector VQTY = sales_inv.getVQTY();
Vector VPLANT_ABBR = sales_inv.getVPLANT_ABBR();
Vector VBOE_ENTRY = sales_inv.getVBOE_ENTRY();
Vector VCARGO_NM = sales_inv.getVCARGO_NM();

String total_dcq=sales_inv.getTotal_dcq();
String total_buy_nom=sales_inv.getTotal_buy_nom();
String total_sell_pnq=sales_inv.getTotal_sell_pnq();
String total_sell_regas=sales_inv.getTotal_sell_regas();
String total_delv_pnq=sales_inv.getTotal_delv_pnq();
String total_delv_regas=sales_inv.getTotal_delv_regas();
String total_delv_qty=sales_inv.getTotal_delv_qty();

Vector VSTORAGE_DATE = sales_inv.getVSTORAGE_DATE();
Vector VSTORAGE_INVENTORY = sales_inv.getVSTORAGE_INVENTORY();
Vector VOFFTAKE_QTY = sales_inv.getVOFFTAKE_QTY();
Vector VSTORAGE_CHARGE = sales_inv.getVSTORAGE_CHARGE();
Vector VSTORAGE_AMT = sales_inv.getVSTORAGE_AMT();
Vector VUSER_DEFINE = sales_inv.getVUSER_DEFINE();
Vector VRATE_TYPE = sales_inv.getVRATE_TYPE();
Vector VDISCOUNT_FLAG = sales_inv.getVDISCOUNT_FLAG();
%>
<body>
<form action="">
<%if(inv_flag.equals("UG")){ %>
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="7">&nbsp;</td>
	</tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center"><font size="4" face="Arial"><b>ATTACHMENT 1 - LNG Quantity</b></font></div>
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
<table width="100%"  border="1" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td><div align="center"><font size="2px" face="Arial"><b>Bill Of Entry No.</b></font></div></td>
		<td><div align="center"><font size="2px" face="Arial"><b>Cargo Name</b></font></div></td>
		<td><div align="center"><font size="2px" face="Arial"><b><%=owner_abbr %> Tax Invoice Seq No</b></font></div></td>
		<td><div align="center"><font size="2px" face="Arial"><b>Plant Name</b></font></div></td>
		<td><div align="center"><font size="2px" face="Arial"><b>Share Of Respective Plant<br>(MMBTU)</b></font></div></td>
	</tr>
	<%for(int i=0;i<VATT_INVOICE_NO.size(); i++) {%>
	<tr>
		<td align="center"><font size="1.75px" face="Arial"><%=VBOE_ENTRY.elementAt(i) %></font></td>
		<td align="center"><font size="1.75px" face="Arial"><%=VCARGO_NM.elementAt(i) %></font></td>
		<td align="center"><font size="1.75px" face="Arial"><%=VATT_INVOICE_NO.elementAt(i) %></font></td>
		<td align="center"><font size="1.75px" face="Arial"><%=VPLANT_ABBR.elementAt(i) %></font></td>
		<td align="right"><font size="1.75px" face="Arial"><%=VQTY.elementAt(i) %>&nbsp;</font></td>
	</tr>
	<%} %>
	<tr>
		<td colspan="4"><div align="right"><font size="1.85px" face="Arial"><b>Total : </b></font></div></td>
		<td><div align="right"><font size="1.85px" face="Arial"><b><%=total_delv_qty%>&nbsp;</b></font></div></td>
	</tr>
</table>
<br>
<br>
<br>
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td><div align="center"><font size="1px" face="Arial">The Plantwise Quantity Share Of LNG Cargo Quantity Is As Provided By <%=couterpty_nm%></font></div></td>
	</tr>
</table>
<%}else if(inv_flag.equals("ST")){ %>
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
			<div align="center"><font size="4" face="Arial"><b>ATTACHMENT 1 - Storage Inventory & Storage Charges</b></font></div>
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
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=owner_abbr%> Tax Invoice Seq No:&nbsp;</b></font>
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
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=invoice_no%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<tr valign="middle"><td colspan="7">&nbsp;</td></tr>
</table>
<table width="100%"  border="1" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td><div align="center"><font size="1.5px" face="Arial"><b>Date</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Storage Inventory (MMBTU)<br>(A)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Offtake (MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Storage Charge (<%=price_cd_nm%>/MMBTU)<br>(B)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Storage Charges (<%=price_cd_nm%>)<br>(A*B)</b></font></div></td>
	</tr>
	<%for(int i=0; i<VSTORAGE_DATE.size(); i++){ %>
	<tr valign="top">
		<td><div align="center"><font size="1.5px" face="Arial"><%=VSTORAGE_DATE.elementAt(i)%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=VSTORAGE_INVENTORY.elementAt(i)%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=VOFFTAKE_QTY.elementAt(i)%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=VSTORAGE_CHARGE.elementAt(i)%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=VSTORAGE_AMT.elementAt(i)%></font>&nbsp;</div></td>
	</tr>
	<%} %>
	<tr valign="top">
		<td colspan="4"><div align="right"><font size="1.5px" face="Arial"><b>Agreegate Storage Charges (<%=price_cd_nm%>) : </b></font></div></td>	
		<td><div align="right"><font size="1.5px" face="Arial"><b><%=gross_amt%></b></font></div></td>
	</tr>
</table>
<%}else{ %>
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
			<div align="center"><font size="4" face="Arial"><b>ATTACHMENT 1 - Nominated Quantities</b></font></div>
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
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=owner_abbr%> Tax Invoice Seq No:&nbsp;</b></font>
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
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=invoice_no%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
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
	<tr valign="middle"><td colspan="7">&nbsp;</td></tr>
</table>
<table width="100%"  border="1" align="center" cellpadding="0" cellspacing="0">
<%if(contract_type.equals("O") || contract_type.equals("Q")) {%>
	<tr valign="bottom">
		<td colspan="4"></td>
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Terminal Co. Nomination</b></font></div></td>
		<td colspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Natural Gas (Regasified)</b></font></div></td>
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Cumulative Natural Gas (Regasified) Quantities</b></font></div></td>
	</tr>
	<tr valign="bottom">
		<td><div align="center"><font size="1.5px" face="Arial"><b>Billing<br>Period<br>Day#</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Date</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Contract Send Out Capacity<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Shipper<br>Nomination<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>NDQ<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>RE Gas<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>NDQ<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>RE Gas<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Total Natural<br>Gas (Regasified)<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Billing<br>Period<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Storage<br>Duration<br>(MMBTU)</b></font></div></td>
	</tr>
<%}else{ %>
	<tr valign="bottom">
		<td colspan="4"></td>
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Seller Nomination</b></font></div></td>
		<td colspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Natural Gas (Delivered)</b></font></div></td>
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Cumulative Quantities</b></font></div></td>
	</tr>
	<tr valign="bottom">
		<td><div align="center"><font size="1.5px" face="Arial"><b>Billing<br>Period<br>Day#</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Date</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>DCQ<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Buyer<br>Nomination<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>PNQ<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>RE Gas<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>PNQ<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>RE Gas<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Total<br>Delivered<br>Gas<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Billing<br>Period<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Transaction<br>Supply<br>Period<br>(MMBTU)</b></font></div></td>
	</tr>
<%} %>
	<%for(int i=0; i<VATT1_DATE.size(); i++){ %>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=i+1%></font>&nbsp;
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=VATT1_DATE.elementAt(i)%></font>&nbsp;
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VATT1_DCQ.elementAt(i)%></font>&nbsp;
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VATT1_BUYNOM.elementAt(i)%></font>&nbsp;
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VATT1_SELLNOM_PNG.elementAt(i)%></font>&nbsp;
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VATT1_SELLNOM_REGAS.elementAt(i)%></font>&nbsp;
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VATT1_NG_PNG.elementAt(i)%></font>&nbsp;
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VATT1_NG_REGAS.elementAt(i)%></font>&nbsp;
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VATT1_NG_TOT_DLV_GAS.elementAt(i)%></font>&nbsp;
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VATT1_CUMULATIVE_QTY_BILLING_PERIOD.elementAt(i)%></font>&nbsp;
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VATT1_CUMULATIVE_QTY_TRANSCT_SUPPLY_PERIOD.elementAt(i)%></font>&nbsp;
			</div>
		</td>
	</tr>
	<%} %>
	<tr valign="bottom">
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Total</b></font></div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><b><%=total_dcq %></b></font></div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><b><%=total_buy_nom%></b></font></div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><b><%=total_sell_pnq %></b></font></div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><b><%=total_sell_regas %></b></font></div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><b><%=total_delv_pnq %></b></font></div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><b><%=total_delv_regas %></b></font></div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><b><%=total_delv_qty %></b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"></font></div></td>
	</tr>
</table>
<%} %>
</form>
</body>
</html>