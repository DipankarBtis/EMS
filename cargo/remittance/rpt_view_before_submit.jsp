<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<jsp:useBean class="com.etrm.fms.remittance.DataBean_Remittance" id="remittance" scope="request"></jsp:useBean>
<%
String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");

String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String bu_plant_seq = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String period_start_dt=request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
String inv_type = request.getParameter("inv_type")==null?"":request.getParameter("inv_type");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");

String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
String boe_no=request.getParameter("boe_no")==null?"":request.getParameter("boe_no");
String inv_flag=request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");

String price_cd=request.getParameter("price_cd")==null?"":request.getParameter("price_cd");
String invoice_raised_in=request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in");

String contact_person=request.getParameter("contact_person")==null?"":request.getParameter("contact_person");
String bu_contact_person=request.getParameter("bu_contact_person")==null?"":request.getParameter("bu_contact_person");

String adj_plusmin=request.getParameter("adj_plusmin")==null?"":request.getParameter("adj_plusmin");
String applicable_flag=request.getParameter("applicable_flag")==null?"":request.getParameter("applicable_flag");

String inv_type_nm="";
if(inv_type.equals("S"))
{
	inv_type_nm="System Generated";
}
else if(inv_type.equals("P"))
{
	inv_type_nm="Party Generated";
}

String mnthYr="";
if(!period_end_dt.equals(""))
{
	mnthYr=period_end_dt.substring(3,period_end_dt.length());
}

remittance.setCallFlag("INVOICE_DETAIL");
remittance.setComp_cd(owner_cd);
remittance.setCounterparty_cd(counterparty_cd);
remittance.setAgmt_no(agmt_no);
remittance.setAgmt_rev_no(agmt_rev);
remittance.setCont_no(cont_no);
remittance.setCont_rev_no(cont_rev);
remittance.setContract_type(contract_type);
remittance.setPlant_seq(plant_seq);
remittance.setBilling_cycle(billing_cycle);
remittance.setPeriod_start_dt(period_start_dt);
remittance.setPeriod_end_dt(period_end_dt);
remittance.setBu_unit(bu_unit);
remittance.setInv_type(inv_type);
remittance.setPrice_cd(price_cd);
remittance.setInvoice_raised_in(invoice_raised_in);
remittance.setContact_person_cd(contact_person);
remittance.setBu_contact_person_cd(bu_contact_person);
remittance.setCargo_no(cargo_no);
remittance.setBoe_no(boe_no);
remittance.init();

String signing_dt = remittance.getSigning_dt();
String contRef = remittance.getContRef();
String cargoRef = remittance.getCargoRef();
String agmtSigningDt = remittance.getAgmtSigningDt();

String price_cd_nm = remittance.getPrice_cd_nm();
String invoice_raised_in_nm = remittance.getInvoice_raised_in_nm();

String couterpty_nm = remittance.getCouterpty_nm();
String bu_contact_person_nm = remittance.getBu_contact_person_nm();
String contact_person_nm = remittance.getContact_person_nm();
String plantAddress=remittance.getPlantAddress();
String plantCity=remittance.getPlantCity();
String plantState=remittance.getPlantState();
String plantPin=remittance.getPlantPin();
String plantNm=remittance.getPlantNm();

String bu_plantAddress=remittance.getBu_plantAddress();
String bu_plantCity=remittance.getBu_plantCity();
String bu_plantState=remittance.getBu_plantState();
String bu_plantPin=remittance.getBu_plantPin();
String bu_plantNm=remittance.getBu_plantNm();

String tax_info=remittance.getTax_info();
String bu_tax_info=remittance.getBu_tax_info();

String top_heading_nm=remittance.getTop_heading_nm();
String str_cargoname =remittance.getStr_cargoname();

String inv_lineitem="Invoice";
String tax_lineitem="Tax";
String gross_lineitem="Gross";
String exchg_lineitem="Exchange Rate";
if(inv_flag.equals("CP") || inv_flag.equals("CF"))
{
	inv_lineitem="Custom Duty";	
	tax_lineitem="Custom Duty";
	gross_lineitem="Invoice";
	exchg_lineitem="Custom Exchange Rate";
}

%>
<script>
var display="";
<%if(inv_type.equals("P")){ %>
	var party_sub_tax_struct=window.opener.document.forms[0].party_sub_tax_struct;
	var party_sub_tax_amt=window.opener.document.forms[0].party_sub_tax_amt;
	
	
	if(party_sub_tax_struct!=null && party_sub_tax_struct!=undefined)
	{
		if(party_sub_tax_struct.length!=undefined)
		{
			for(var i=0;i<party_sub_tax_struct.length;i++)
			{
				display+="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+party_sub_tax_struct[i].value+"</font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='left'><font size='1.5px' face='Arial'>"+party_sub_tax_amt[i].value+"&nbsp;</font></div></td></tr>";
			}
		}
		else
		{
			//NOT REQUIRED FOR SINGLE TAX COMPONENT
			//display="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+party_sub_tax_struct.value+"</font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+party_sub_tax_amt.value+"&nbsp;</font></div></td></tr>";
		}
	}
<%}else{%>
	var sys_sub_tax_struct=window.opener.document.forms[0].sys_sub_tax_struct;
	var sys_sub_tax_amt=window.opener.document.forms[0].sys_sub_tax_amt;
	
	
	if(sys_sub_tax_struct!=null && sys_sub_tax_struct!=undefined)
	{
		if(sys_sub_tax_struct.length!=undefined)
		{
			for(var i=0;i<sys_sub_tax_struct.length;i++)
			{
				display+="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+sys_sub_tax_struct[i].value+"</font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='left'><font size='1.5px' face='Arial'>"+sys_sub_tax_amt[i].value+"&nbsp;</font></div></td></tr>";
			}
		}
		else
		{
			//NOT REQUIRED FOR SINGLE TAX COMPONENT
			//display="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+sys_sub_tax_struct.value+"</font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+sys_sub_tax_amt.value+"&nbsp;</font></div></td></tr>";
		}
	}
<%}%>
</script>
<body>
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="4" face="Arial">
					<b>REMITTANCE</b>
					<br><br>
				</font>
				<font size="4" face="Arial">
					<b><%=owner_nm%></b>
					<br>
				</font>
				<font size="2" face="Arial">
				<%if(inv_flag.equals("CP")){ %>
					<b>PROVISIONAL CUSTOM DUTY CHALLAN (<%=inv_type_nm%>)</b>
				<%}else if(inv_flag.equals("CF")){ %>
					<b>FINAL ASSESSED CUSTOM DUTY (<%=inv_type_nm%>)</b>
				<%} else if(contract_type.equals("N") && inv_flag.equals("PF")){%>
					<b>PROFORMA INVOICE (<%=inv_type_nm%>)</b>
					<br><font color="red" size="1.5"><b>FOR CUSTOMS CLEARANCE PURPOSES ONLY</b></font>
				<%} else if(contract_type.equals("N") && inv_flag.equals("F")){%>	
					<%if(inv_type.equals("P")){ %>
					<script>
					if(window.opener.document.forms[0].party_diff_price.value != "")
					{
						if(parseFloat(window.opener.document.forms[0].party_diff_price.value) < 0)
						{
							document.write("<b>CREDIT NOTE (<%=inv_type_nm%>)</b>");
						}
						else
						{
							document.write("<b>INVOICE (<%=inv_type_nm%>)</b>");
						}
					}
					else
					{
						document.write("<b>INVOICE (<%=inv_type_nm%>)</b>");
					}
					</script>&nbsp;
					<%}else{ %>
					<script>
					if(window.opener.document.forms[0].sys_diff_price.value != "")
					{
						if(parseFloat(window.opener.document.forms[0].sys_diff_price.value) < 0)
						{
							document.write("<b>CREDIT NOTE (<%=inv_type_nm%>)</b>");
						}
						else
						{
							document.write("<b>INVOICE (<%=inv_type_nm%>)</b>");
						}
					}
					else
					{
						document.write("<b>INVOICE (<%=inv_type_nm%>)</b>");
					}
					</script>
					<%}%>
				<%} else if(contract_type.equals("N") && inv_flag.equals("P")){%>	
					<b>INVOICE (<%=inv_type_nm%>)</b>
				<%}else{%>
					<b>TAX INVOICE (<%=inv_type_nm%>)</b>
				<%}%>
				</font>
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
	<%if(!inv_flag.equals("CP") && !inv_flag.equals("CF")) {%>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
			<%if(contract_type.equals("N")) {%>
				<font size="1px" face="Arial">
					In respect of Confirmation Notice(<%=contRef%>/<%=cargoRef%>) executed on <%=signing_dt%> pursuant to MSPA executed on <%=agmtSigningDt%>
					<br>
					between <%=top_heading_nm %> and <%=owner_nm%> 
				</font>
			<%}else{ %>
				<font size="1px" face="Arial">
					In respect of <%if(contract_type.equals("G") || contract_type.equals("P")){%>LTCORA <%}%>Purchase Contract(<%=contRef%>) executed on <%=signing_dt%>
					<br>
					between <%=top_heading_nm %> and <%=owner_nm%> 
				</font>
			<%} %>
			</div>
		</td>
	</tr>
	<%} %>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">&nbsp;</div>
		</td>
	</tr>
	<tr valign="top">
		<td colspan="3" width="50%">
			<div align="left">
				<font size="1.5px" face="Arial">
					<b>From:</b> 
					<br><%=owner_nm%>
					<br><%=bu_plantAddress%>,<%=bu_plantCity%>
					<br><%=bu_plantState%>&nbsp;-&nbsp;<%=bu_plantPin%>					
				</font>
			</div>
		</td>
		<td colspan="1" width="10%"><div align="left"><font size="1.5px" face="Arial"></font></div></td>
		<td colspan="3" width="40%">
			<div align="left">
				<font size="1.5px" face="Arial">
					<%-- <%=bu_contact_person_nm%>,<br> --%>
					<%if(inv_flag.equals("CP") || inv_flag.equals("CF")){ %>
					<b>To:</b>
					<br>Indian Customs
					<%}else{ %>
					<b>To:</b>
					<br><%=couterpty_nm%>
					<br><%=plantAddress%>,<%=plantCity%>
					<br><%=plantState %>&nbsp;-&nbsp;<%=plantPin%>
					<%} %>
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
 		<%if(!inv_flag.equals("CP") && !inv_flag.equals("CF")) {%>
 			<div>
 				<font size="1.5px" face="Arial">
 					<%=tax_info %>
				</font>
			</div>
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
								<font size="1.5px" face="Arial"><b>Remittance#:&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=inv_lineitem%> Date:&nbsp;</b></font>
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
								<font size="1.5px" face="Arial"><b><%=inv_lineitem%> Seq No:&nbsp;</b></font>
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
								<font size="1.5px" face="Arial">
									<b>
									<%if(inv_type.equals("P")){ %>
									<script>document.write(window.opener.document.forms[0].party_system_invoice_no.value);</script>&nbsp;
									<%}else{ %>
									<script>document.write(window.opener.document.forms[0].system_invoice_no.value);</script>&nbsp;
									<%} %>
									</b>
								</font>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial">
									<b>
									<%if(inv_type.equals("P")){ %>
									<script>document.write(window.opener.document.forms[0].party_invoice_dt.value);</script>&nbsp;
									<%}else{ %>
									<script>document.write(window.opener.document.forms[0].sys_invoice_dt.value);</script>&nbsp;
									<%} %>
									</b>
								</font>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial">
									<b>
									<%if(inv_type.equals("P")){ %>
									<script>document.write(window.opener.document.forms[0].party_invoice_due_dt.value);</script>&nbsp;
									<%}else{ %>
									<script>document.write(window.opener.document.forms[0].sys_invoice_due_dt.value);</script>&nbsp;
									<%} %>
									</b>
								</font>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial">
									<b>
									<%if(inv_type.equals("P")){ %>
									<script>document.write(window.opener.document.forms[0].party_invoice_no.value);</script>&nbsp;
									<%}else{ %>
									<script>document.write(window.opener.document.forms[0].sys_invoice_no.value);</script>&nbsp;
									<%} %>
									</b>
								</font>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<%if(!inv_flag.equals("UG")){ %>
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
								<font size="1.5px" face="Arial">
									<b><script>document.write(window.opener.document.forms[0].period_start_dt.value);</script>&nbsp;</b>
								</font>
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
								<font size="1.5px" face="Arial"><b><script>document.write(window.opener.document.forms[0].period_end_dt.value);</script>&nbsp;</b></font>
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

<table width="100%"  border="1" align="center" cellpadding="2" cellspacing="0">
	<tr valign="bottom">
		<td width="6%"><div align="center"><font size="1.5px" face="Arial"><b>Sr. No.</b></font></div></td>
		<td width="34%"><div align="center"><font size="1.5px" face="Arial"><b>Item</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Currency</b></font></div></td>
		<td width="15%"><div align="center"><font size="1.5px" face="Arial"><b>Quantity<br>(MMBTUS)</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Rate</b></font></div></td>
		<td width="15%"><div align="center"><font size="1.5px" face="Arial"><b>Amount</b></font></div></td>
	</tr>
	<%int srNo=0; %>
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
			<%if(contract_type.equals("N")){ %>
				<font size="1.5px" face="Arial">Liquid Natural Gas (LNG)</font>
			<%}else if(inv_flag.equals("UG")){ %>
				<font size="1.5px" face="Arial">Natural Gas (Discharge during <%=mnthYr%>)</font>
			<%}else if(contract_type.equals("G") || contract_type.equals("P")){ %>
				<font size="1.5px" face="Arial">Natural Gas (Regasified)</font>
			<%}else{ %>
				<font size="1.5px" face="Arial">Natural Gas</font>
			<%} %>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=price_cd_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_alloc_qty.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_alloc_qty.value);</script>&nbsp;
					<%} %>
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
				<%if(!inv_flag.equals("UG")){ %>
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_price.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_price.value);</script>&nbsp;
					<%} %>
				<%} %>
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
				<%if((contract_type.equals("N") && inv_flag.equals("F")) || inv_flag.equals("UG")) {}else{%>
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_gross_amt.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_gross_amt.value);</script>&nbsp;
					<%} %>
				<%} %>
				</font>
			</div>
		</td>
	</tr>
	
	<%if(inv_flag.equals("UG")) {%>
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial">SUG (<script>document.write(window.opener.document.forms[0].sys_sug_percent.value);</script>% of above)</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=price_cd_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<script>document.write(window.opener.document.forms[0].sys_sug_qty.value);</script>&nbsp;
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<script>document.write(window.opener.document.forms[0].sys_price.value);</script>&nbsp;
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
				<script>document.write(window.opener.document.forms[0].sys_gross_amt.value);</script>&nbsp;
				</font>
			</div>
		</td>
	</tr>
	<%} %>
	
	<%if(contract_type.equals("N") && inv_flag.equals("F")) {%>
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial">Difference in Price</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=price_cd_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_diff_price.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_diff_price.value);</script>&nbsp;
					<%} %>
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_gross_amt.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_gross_amt.value);</script>&nbsp;
					<%} %>
				</font>
			</div>
		</td>
	</tr>
	<%} %>
	
	<%if(!price_cd.equals(invoice_raised_in)){ %>
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial"><%=exchg_lineitem%></font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial">INR/USD</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_exchng_rate.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_exchng_rate.value);</script>&nbsp;
					<%} %>
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
	</tr>
	<%} %>
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial"><%=gross_lineitem%> Amount</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_gross_amt1.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_gross_amt1.value);</script>&nbsp;
					<%} %>
				</font>
			</div>
		</td>
	</tr>
	
	<%if(inv_flag.equals("CP") || inv_flag.equals("CF")) {%>
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial">CIF Value of Cargo</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_cif_amt.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_cif_amt.value);</script>&nbsp;
					<%} %>
				</font>
			</div>
		</td>
	</tr>
	
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial">Assessable Value</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_assessable_amt.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_assessable_amt.value);</script>&nbsp;
					<%} %>
				</font>
			</div>
		</td>
	</tr>
	<%} %>
	
	<%if((!contract_type.equals("N") && !inv_flag.equals("P")) || (inv_flag.equals("CP") || inv_flag.equals("CF"))){ %>
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial"><%=tax_lineitem%>(
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_tax_struct_dtl.value);</script>
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_tax_struct_dtl.value);</script>
					<%} %>
					)
				</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_tax_amt.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_tax_amt.value);</script>&nbsp;
					<%} %>
				</font>
			</div>
		</td>
	</tr>
	<script>document.write(display)</script>
	<%} %>
	
	<%if(inv_flag.equals("CF")){ %>
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial">Custom Duty Paid</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_cd_paid_amt.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_cd_paid_amt.value);</script>&nbsp;
					<%} %>
				</font>
			</div>
		</td>
	</tr>
	<%} %>
	
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial"><%=inv_lineitem%> Amount</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_invoice_amt.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_invoice_amt.value);</script>&nbsp;
					<%} %>
				</font>
			</div>
		</td>
	</tr>
	
	<%if(applicable_flag.equals("Y")){ %>
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial">TCS</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<!-- <script>document.write(window.opener.document.forms[0].party_tcs_factor.value);</script>%&nbsp; -->
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_tcs_factor.value);</script>%&nbsp;
					<%} %>
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_tcs_amt.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_tcs_amt.value);</script>&nbsp;
					<%} %>
				</font>
			</div>
		</td>
	</tr>
	<%} %>
	
	<%if(!adj_plusmin.equals("")){ %>
	<%srNo+=1; %>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial">Adjustment Amount</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<script>
					document.write(window.opener.document.forms[0].party_adj_plusmin.value);
					document.write(window.opener.document.forms[0].party_adj_amt.value);</script>&nbsp;
					<%}else{ %>
					<script>
					document.write(window.opener.document.forms[0].sys_adj_plusmin.value);
					document.write(window.opener.document.forms[0].sys_adj_amt.value);</script>&nbsp;
					<%} %>
				</font>
			</div>
		</td>
	</tr>
	<%} %>
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><b><%=srNo%></b></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial"><b>Net Amount Payable</b></font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=invoice_raised_in_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_net_payable.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_net_payable.value);</script>&nbsp;
					<%} %>
				</font>
			</div>
		</td>
	</tr>
</table>
<table border="0" width="100%" align="center" cellpadding="2" cellspacing="0">
	<%if(contract_type.equals("N") || inv_flag.equals("UG")) {%>
	<tr align="center"><td colspan="7">&nbsp;</td></tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="left">
				<font size="1px" face="Arial">
				<%=str_cargoname %>
				</font>
			</div>
		</td>
	</tr>
	<%} %>
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="left">
				<font size="1.5px" face="Arial"><b>For <%=owner_nm%><br><br><br><br>Authorised Signatory</b></font>
			</div>
		</td>
	</tr>
</table>
</body>
</html>