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
dlng_inv.setIs_attachment("1");
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
String qty_mmbtu=dlng_inv.getQty_mmbtu();
String gross_amt=dlng_inv.getGross_amt();
String net_payable=dlng_inv.getNet_payable();

Vector VTOTAL_TRUCK_INV_QTY = dlng_inv.getVTOTAL_TRUCK_INV_QTY();

Vector VATT1_COL1 = dlng_inv.getVATT1_COL1();
Vector VATT1_COL2 = dlng_inv.getVATT1_COL2();
Vector VATT1_COL3 = dlng_inv.getVATT1_COL3();
Vector VATT1_COL4 = dlng_inv.getVATT1_COL4();
Vector VATT1_COL5 = dlng_inv.getVATT1_COL5();
Vector VATT1_COL6 = dlng_inv.getVATT1_COL6();

Vector VATT1_DATE = dlng_inv.getVATT1_DATE();
Vector VATT1_DCQ = dlng_inv.getVATT1_DCQ();
Vector VATT1_TRCUK_REG_NO = dlng_inv.getVATT1_TRCUK_REG_NO();
Vector VATT1_BUYNOM = dlng_inv.getVATT1_BUYNOM();
Vector VATT1_SELLNOM_PNG = dlng_inv.getVATT1_SELLNOM_PNG();
Vector VATT1_SELLNOM_REGAS = dlng_inv.getVATT1_SELLNOM_REGAS();
Vector VATT1_NG_PNG = dlng_inv.getVATT1_NG_PNG();
Vector VATT1_NG_REGAS = dlng_inv.getVATT1_NG_REGAS();
Vector VATT1_NG_TOT_DLV_GAS = dlng_inv.getVATT1_NG_TOT_DLV_GAS();
Vector VATT1_CUMULATIVE_QTY_BILLING_PERIOD = dlng_inv.getVATT1_CUMULATIVE_QTY_BILLING_PERIOD();
Vector VATT1_CUMULATIVE_QTY_TRANSCT_SUPPLY_PERIOD = dlng_inv.getVATT1_CUMULATIVE_QTY_TRANSCT_SUPPLY_PERIOD();

String total_dcq=dlng_inv.getTotal_dcq();
String total_buy_nom=dlng_inv.getTotal_buy_nom();
String total_sell_pnq=dlng_inv.getTotal_sell_pnq();
String total_sell_regas=dlng_inv.getTotal_sell_regas();
String total_delv_pnq=dlng_inv.getTotal_delv_pnq();
String total_delv_regas=dlng_inv.getTotal_delv_regas();
String total_delv_qty=dlng_inv.getTotal_delv_qty();
%>
<body>
<form action="">
<%if(contract_type.equals("B") || contract_type.equals("M")) {%>
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
			<div align="center"><font size="4" face="Arial"><b>ATTACHMENT 1 - Service Invoice Details</b></font></div>
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
<table width="100%"  border="1" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td><div align="center"><font size="1.5px" face="Arial"><b>Sr. No</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Supply Date</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Truck No.</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Truck Quantity<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Rate <br>(<%=price_cd_nm%>/<%=unit_desc%>)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Amount<br>(<%=price_cd_nm%>)</b></font></div></td>
	</tr>
	<%for(int i=0; i<VATT1_COL1.size(); i++){ %>
	<tr valign="top">
		<td><div align="center"><font size="1.5px" face="Arial"><%=VATT1_COL1.elementAt(i)%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=VATT1_COL2.elementAt(i)%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=VATT1_COL3.elementAt(i)%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=VATT1_COL4.elementAt(i)%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=VATT1_COL5.elementAt(i)%></font>&nbsp;</div></td>
		<td><div align="right"><font size="1.5px" face="Arial"><%=VATT1_COL6.elementAt(i)%></font>&nbsp;</div></td>
	</tr>
	<%} %>
	<tr valign="middle">
	<td colspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Total</b></font></div></td>
	<%if(unit_desc.equals("TRUCK")) {%>
		<td><div align="right"><font size="1.5px" face="Arial"><%=VTOTAL_TRUCK_INV_QTY.elementAt(0) %></font></div></td>
	<%}else if(unit_desc.equals("MT")){%>
		<td><div align="right"><font size="1.5px" face="Arial"><%=VTOTAL_TRUCK_INV_QTY.elementAt(1) %></font></div></td>
	<%}else{ %>
		<td><div align="right"><font size="1.5px" face="Arial"><%=qty_mmbtu%></font></div></td>
	<%} %>
	<td></td>
	<td><div align="right"><font size="1.7px" face="Arial"><b><%=gross_amt%></b></font></div></td>
	
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
	<tr valign="bottom">
		<td colspan="4"></td>
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Terminal Co. Nomination</b></font></div></td>
		<td colspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Liquefied Natural Gas<br>(Truck Loaded)</b></font></div></td>
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Cumulative Liquefied Natural Gas/RLNG<br>Quantities</b></font></div></td>
	</tr>
	<tr valign="bottom">
		<td><div align="center"><font size="1.5px" face="Arial"><b>Billing<br>Period<br>Day#</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Date</b></font></div></td>
		<!-- <td><div align="center"><font size="1.5px" face="Arial"><b>Contract Send Out Capacity<br>(MMBTU)</b></font></div></td> -->
		<td><div align="center"><font size="1.5px" face="Arial"><b>Truck No.</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Shipper<br>Nomination<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>NDQ<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>RE LNG<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>NDQ<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>RE LNG<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Total Liquefied <br>Natural Gas<br>(Truck Loaded)<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Billing<br>Period<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Storage<br>Duration<br>(MMBTU)</b></font></div></td>
	</tr>
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
		<%-- <td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VATT1_DCQ.elementAt(i)%></font>&nbsp;
			</div>
		</td> --%>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=VATT1_TRCUK_REG_NO.elementAt(i)%></font>&nbsp;
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
		<td colspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Total</b></font></div></td>
		<%-- <td><div align="right"><font size="1.5px" face="Arial"><b><%=total_dcq %></b></font></div></td> --%>
		<!-- <td><div align="right"><font size="1.5px" face="Arial"><b></b></font></div></td> -->
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