<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<jsp:useBean class="com.etrm.fms.gx.DataBean_Gx_Invoice" id="gx_remittance" scope="request"></jsp:useBean>
<%
String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String gx_counterparty_cd=request.getParameter("gx_counterparty_cd")==null?"":request.getParameter("gx_counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String gx_bu_unit=request.getParameter("gx_bu_unit")==null?"":request.getParameter("gx_bu_unit");
String bu_plant_seq = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");

String inv_type = request.getParameter("inv_type")==null?"":request.getParameter("inv_type");
String invoice_type =request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");

String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");

String price_cd=request.getParameter("price_cd")==null?"":request.getParameter("price_cd");
String invoice_raised_in=request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in");

String gx_bu_contact_person=request.getParameter("gx_bu_contact_person")==null?"":request.getParameter("gx_bu_contact_person");
String bu_contact_person=request.getParameter("bu_contact_person")==null?"":request.getParameter("bu_contact_person");

String adj_plusmin=request.getParameter("adj_plusmin")==null?"":request.getParameter("adj_plusmin");

String inv_type_nm="";
if(inv_type.equals("S"))
{
	inv_type_nm="System Generated";
}
else if(inv_type.equals("P"))
{
	inv_type_nm="Party Generated";
}

gx_remittance.setCallFlag("INVOICE_DETAIL");
gx_remittance.setComp_cd(owner_cd);
gx_remittance.setCounterparty_cd(counterparty_cd);
gx_remittance.setGx_counterparty_cd(gx_counterparty_cd);
gx_remittance.setAgmt_no(agmt_no);
gx_remittance.setAgmt_rev_no(agmt_rev);
gx_remittance.setCont_no(cont_no);
gx_remittance.setCont_rev_no(cont_rev);
gx_remittance.setContract_type(contract_type);
gx_remittance.setBu_unit(bu_unit);
gx_remittance.setGx_bu_unit(gx_bu_unit);
gx_remittance.setInv_type(inv_type);
gx_remittance.setPrice_cd(price_cd);
gx_remittance.setInvoice_raised_in(invoice_raised_in);
gx_remittance.setGx_bu_contact_person_cd(gx_bu_contact_person);
gx_remittance.setBu_contact_person_cd(bu_contact_person);
gx_remittance.setInvoice_type(invoice_type);
gx_remittance.init();

String signing_dt = gx_remittance.getSigning_dt();
String contRef = gx_remittance.getContRef();

String price_cd_nm = gx_remittance.getPrice_cd_nm();
String invoice_raised_in_nm = gx_remittance.getInvoice_raised_in_nm();

String couterpty_nm = gx_remittance.getCouterpty_nm();
String gx_couterpty_nm = gx_remittance.getGx_couterpty_nm();
String bu_contact_person_nm = gx_remittance.getBu_contact_person_nm();
String gx_bu_contact_person_nm = gx_remittance.getGx_bu_contact_person_nm();
String plantAddress=gx_remittance.getPlantAddress();
String plantCity=gx_remittance.getPlantCity();
String plantState=gx_remittance.getPlantState();
String plantPin=gx_remittance.getPlantPin();
String plantNm=gx_remittance.getPlantNm();

String bu_plantAddress=gx_remittance.getBu_plantAddress();
String bu_plantCity=gx_remittance.getBu_plantCity();
String bu_plantState=gx_remittance.getBu_plantState();
String bu_plantPin=gx_remittance.getBu_plantPin();
String bu_plantNm=gx_remittance.getBu_plantNm();

String tax_info=gx_remittance.getTax_info();
String bu_tax_info=gx_remittance.getBu_tax_info();

String sellBuy="";
if(contract_type.equals("I"))
{
	sellBuy="Buy";
}
else
{
	sellBuy="Sell";
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
					<b><u>REMITTANCE</b></u>
					<br><br>
				</font>				
				<font size="4" face="Arial">
					<b><%=owner_nm%></b>
					<br>
				</font>	
				<font size="2" face="Arial">	
					<b>TAX INVOICE(<%=inv_type_nm%>)</b>
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
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="1px" face="Arial">
					In respect of Exchange <%=sellBuy%> Transaction (<%=couterpty_nm %> - <%=contRef%>) executed on <%=signing_dt%>
					<br>
					between <%=gx_couterpty_nm%> and <%=owner_nm%> 
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
					<b>To:</b>
					<br><%=gx_couterpty_nm%>
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
								<font size="1.5px" face="Arial"><b>Remittance#:&nbsp;</b></font>
							</div>
						</td>
					</tr>
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
								<font size="1.5px" face="Arial"><b>Invoice Seq No:&nbsp;</b></font>
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
									<script>document.write(window.opener.document.forms[0].system_invoice_no.value);</script>&nbsp;
									<%}else{ %>
									<script>document.write(window.opener.document.forms[0].party_system_invoice_no.value);</script>&nbsp;
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
				<font size="1.5px" face="Arial">Transaction Fee</font>
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
					<%if(inv_type.equals("P")){ %>
					<script>document.write(window.opener.document.forms[0].party_price.value);</script>&nbsp;
					<%}else{ %>
					<script>document.write(window.opener.document.forms[0].sys_price.value);</script>&nbsp;
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
	<%if(price_cd.equals("2")){ %>
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial">Exchange Rate</font>
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
				<font size="1.5px" face="Arial">Gross Amount</font>
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
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial">Tax(
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
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial">Invoice Amount</font>
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