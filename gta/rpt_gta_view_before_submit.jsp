<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<Script>
var newWindow_att1;
function openAtt1()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var cont_no = document.forms[0].cont_no.value;
	var period_start_dt = document.forms[0].period_start_dt.value;
	var period_end_dt = document.forms[0].period_end_dt.value;
	
	var url="../gta/rpt_gta_view_attachment1.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+
			"&agmt_no="+agmt_no+"&cont_no="+cont_no+"&from_dt="+period_start_dt+"&to_dt="+period_end_dt;
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

function openAtt2()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var cont_no = document.forms[0].cont_no.value;
	var period_start_dt = document.forms[0].period_start_dt.value;
	var period_end_dt = document.forms[0].period_end_dt.value;
	var bu_plant_seq = document.forms[0].bu_plant_seq.value;
	
	var url="../gta/rpt_gta_view_attachment2.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+
			"&agmt_no="+agmt_no+"&cont_no="+cont_no+"&from_dt="+period_start_dt+"&to_dt="+period_end_dt+"&bu_plant_seq="+bu_plant_seq;
	if(!newWindow_att1 || newWindow_att1.closed)
	{
		newWindow_att1 = window.open(url,"Attachment2","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow_att1.close();
		newWindow_att1 = window.open(url,"Attachment2","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}
</Script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GTA_Remittance" id="remittance" scope="request"></jsp:useBean>
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
String trans_bu_seq=request.getParameter("trans_bu_seq")==null?"":request.getParameter("trans_bu_seq");
String bu_plant_seq = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String period_start_dt=request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
String inv_type = request.getParameter("inv_type")==null?"":request.getParameter("inv_type");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");

String price_cd=request.getParameter("price_cd")==null?"":request.getParameter("price_cd");
String invoice_raised_in=request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in");

String bu_contact_person=request.getParameter("bu_contact_person")==null?"":request.getParameter("bu_contact_person");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");

String adj_plusmin=request.getParameter("adj_plusmin")==null?"":request.getParameter("adj_plusmin");

String allo_qty=request.getParameter("allo_qty")==null?"":request.getParameter("allo_qty");
String neg_qty=request.getParameter("neg_qty")==null?"":request.getParameter("neg_qty");
String pos_qty=request.getParameter("pos_qty")==null?"":request.getParameter("pos_qty");
String unauth_qty=request.getParameter("unauth_qty")==null?"":request.getParameter("unauth_qty");
String def_qty=request.getParameter("def_qty")==null?"":request.getParameter("def_qty");
String prk_qty=request.getParameter("prk_qty")==null?"":request.getParameter("prk_qty");

String inv_type_nm="";
if(inv_type.equals("S"))
{
	inv_type_nm="System Generated";
}
else if(inv_type.equals("P"))
{
	inv_type_nm="Party Generated";
}

remittance.setCallFlag("GTA_INVOICE_DETAIL");
remittance.setComp_cd(owner_cd);
remittance.setCounterparty_cd(counterparty_cd);
remittance.setAgmt_no(agmt_no);
remittance.setAgmt_rev_no(agmt_rev);
remittance.setCont_no(cont_no);
remittance.setCont_rev_no(cont_rev);
remittance.setContract_type(contract_type);
remittance.setTrans_bu_seq(trans_bu_seq);
remittance.setBilling_cycle(billing_cycle);
remittance.setPeriod_start_dt(period_start_dt);
remittance.setPeriod_end_dt(period_end_dt);
remittance.setBu_unit(bu_unit);
remittance.setInvoice_type(invoice_type);
remittance.setPrice_cd(price_cd);
remittance.setInvoice_raised_in(invoice_raised_in);
remittance.setBu_contact_person_cd(bu_contact_person);
remittance.init();

String signing_dt = remittance.getSigning_dt();
String contRef = remittance.getContRef();

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
				display+="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+party_sub_tax_struct[i].value+"</font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='left'><font size='1.5px' face='Arial'>"+party_sub_tax_amt[i].value+"&nbsp;</font></div></td></tr>";
			}
		}
		else
		{
			//NOT REQUIRED FOR SINGLE TAX COMPONENT
			//display="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+party_sub_tax_struct.value+"</font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+party_sub_tax_amt.value+"&nbsp;</font></div></td></tr>";
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
				display+="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+sys_sub_tax_struct[i].value+"</font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='left'><font size='1.5px' face='Arial'>"+sys_sub_tax_amt[i].value+"&nbsp;</font></div></td></tr>";
			}
		}
		else
		{
			//NOT REQUIRED FOR SINGLE TAX COMPONENT
			//display="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+sys_sub_tax_struct.value+"</font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+sys_sub_tax_amt.value+"&nbsp;</font></div></td></tr>";
		}
	}
<%}%>
</script>
<body>
<form>
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="4" face="Arial">
					<b><u>Remittance Advise</b></u>
					<br><br>
				</font>
				<font size="4" face="Arial">
					<b><%=owner_nm%></b>
					<br>
				</font>	
				<font size="2" face="Arial">	
					<b>TAX INVOICE (<%=inv_type_nm%>)</b>
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
					In respect of <%if(invoice_type.equals("PC")) {%>Parking<%}else{ %>GTA<%} %> Contract(<%=contRef%>) between <%=couterpty_nm %> and <%=owner_nm%> 
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
	<tr valign="middle"><td colspan="7">&nbsp;</td></tr>
</table>

<table width="100%"  border="1" align="center" cellpadding="2" cellspacing="0">
	<tr valign="bottom">
		<td width="6%"><div align="center"><font size="1.5px" face="Arial"><b>Sr. No.</b></font></div></td>
		<td width="30%"><div align="center"><font size="1.5px" face="Arial"><b>Item</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Attachment<br>Reference</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Currency</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Quantity<br>(MMBTUS)</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Rate</b></font></div></td>
		<td width="14%"><div align="center"><font size="1.5px" face="Arial"><b>Amount</b></font></div></td>
	</tr>
	<%int srNo=0; %>
	<%if(invoice_type.equals("TC")){ %>
		<%if(!allo_qty.equals("")){ %>
			<%if(Double.parseDouble(""+allo_qty) > 0){%>
				<%srNo+=1;%>
				<tr valign="top">
					<td>
						<div align="center">
							<font size="1.5px" face="Arial"><%=srNo%></font>
						</div>
					</td>
					<td>
						<div align="left">
							<font size="1.5px" face="Arial">Transmission Charges</font>
						</div>
					</td>
					<td>
						<div align="center">
							<font size="1.5px" face="Arial" onclick="openAtt1();" color="blue">Att1</font>
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
								<!-- <script>document.write(window.opener.document.forms[0].party_transmission_amt.value);</script>&nbsp; -->
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_transmission_amt.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
				</tr>
			<%} %>
		<%} %>
		
		<%if(!def_qty.equals("")){ %>
			<%if(Double.parseDouble(""+def_qty) > 0){%>
				<%srNo+=1;%>
				<tr valign="top">
					<td>
						<div align="center">
							<font size="1.5px" face="Arial"><%=srNo%></font>
						</div>
					</td>
					<td>
						<div align="left">
							<font size="1.5px" face="Arial">Ship-or-Pay Charges</font>
						</div>
					</td>
					<td>
						<div align="center">
							<font size="1.5px" face="Arial" onclick="openAtt2();" color="blue">Att2</font>
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
								<script>document.write(window.opener.document.forms[0].party_deficiency_qty.value);</script>&nbsp;
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_deficiency_qty.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<%if(inv_type.equals("P")){ %>
								<script>document.write(window.opener.document.forms[0].party_ship_pay_rate.value);</script>&nbsp;
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_ship_pay_rate.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<%if(inv_type.equals("P")){ %>
								<!-- <script>document.write(window.opener.document.forms[0].party_deficiency_amt.value);</script>&nbsp; -->
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_deficiency_amt.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
				</tr>	
			<% }%>
		<%} %>
	<%}else if(invoice_type.equals("IC")){ 
		boolean att_done=false;
	%>
		<%if(!neg_qty.equals("")){ %>
			<%if(Double.parseDouble(""+neg_qty) > 0){ 
				att_done=true;
			%>
				<%srNo+=1;%>
				<tr valign="top">
					<td>
						<div align="center">
							<font size="1.5px" face="Arial"><%=srNo%></font>
						</div>
					</td>
					<td>
						<div align="left">
							<font size="1.5px" face="Arial">Negative Imbalance > 5%</font>
						</div>
					</td>
					<td>
						<div align="center">
							<font size="1.5px" face="Arial" onclick="openAtt1();" color="blue">Att1</font>
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
								<script>document.write(window.opener.document.forms[0].party_neg_imb_qty.value);</script>&nbsp;
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_neg_imb_qty.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<%if(inv_type.equals("P")){ %>
								<script>document.write(window.opener.document.forms[0].party_neg_imb_rate.value);</script>&nbsp;
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_neg_imb_rate.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<%if(inv_type.equals("P")){ %>
								<!-- <script>document.write(window.opener.document.forms[0].party_neg_imb_amt.value);</script>&nbsp; -->
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_neg_imb_amt.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
				</tr>
			<%} %>
		<%} %>
		
		<%if(!pos_qty.equals("")){ %>
			<%if(Double.parseDouble(""+pos_qty) > 0){ %>
				<%srNo+=1;%>
				<tr valign="top">
					<td>
						<div align="center">
							<font size="1.5px" face="Arial"><%=srNo%></font>
						</div>
					</td>
					<td>
						<div align="left">
							<font size="1.5px" face="Arial">Positive Imbalance > 10%</font>
						</div>
					</td>
					<td>
						<div align="center">
						<%if(!att_done){ 
							att_done=true;
						%>
							<font size="1.5px" face="Arial" onclick="openAtt1();" color="blue">Att1</font>
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
								<script>document.write(window.opener.document.forms[0].party_pos_imb_qty.value);</script>&nbsp;
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_pos_imb_qty.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<%if(inv_type.equals("P")){ %>
								<script>document.write(window.opener.document.forms[0].party_pos_imb_rate.value);</script>&nbsp;
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_pos_imb_rate.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<%if(inv_type.equals("P")){ %>
								<!-- <script>document.write(window.opener.document.forms[0].party_pos_imb_amt.value);</script>&nbsp; -->
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_pos_imb_amt.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
				</tr>
			<%} %>
		<%} %>
		
		<%if(!unauth_qty.equals("")){ %>
			<%if(Double.parseDouble(""+unauth_qty) > 0){ %>
				<%srNo+=1;%>
				<tr valign="top">
					<td>
						<div align="center">
							<font size="1.5px" face="Arial"><%=srNo%></font>
						</div>
					</td>
					<td>
						<div align="left">
							<font size="1.5px" face="Arial">Unauthorized Overrun</font>
						</div>
					</td>
					<td>
						<div align="center">
							<%if(!att_done){ 
								att_done=true;
							%>
								<font size="1.5px" face="Arial" onclick="openAtt1();" color="blue">Att1</font>
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
								<script>document.write(window.opener.document.forms[0].party_unauth_overrun_qty.value);</script>&nbsp;
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_unauth_overrun_qty.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<%if(inv_type.equals("P")){ %>
								<script>document.write(window.opener.document.forms[0].party_unauth_overrun_rate.value);</script>&nbsp;
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_unauth_overrun_rate.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<%if(inv_type.equals("P")){ %>
								<!-- <script>document.write(window.opener.document.forms[0].party_unauth_overrun_amt.value);</script>&nbsp; -->
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_unauth_overrun_amt.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
				</tr>
			<%} %>
		<%} %>
	<%}else if(invoice_type.equals("PC")){ 
		boolean att_done=false;
	%>
		<%if(!prk_qty.equals("")){ %>
			<%if(Double.parseDouble(""+prk_qty) > 0){ 
				att_done=true;
			%>
				<%srNo+=1;%>
				<tr valign="top">
					<td>
						<div align="center">
							<font size="1.5px" face="Arial"><%=srNo%></font>
						</div>
					</td>
					<td>
						<div align="left">
							<font size="1.5px" face="Arial">Parking Charges</font>
						</div>
					</td>
					<td>
						<div align="center">
							<!-- <font size="1.5px" face="Arial" onclick="openAtt1();" color="blue">Att1</font> -->
							<font size="1.5px" face="Arial" color="blue"></font>
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
								<script>document.write(window.opener.document.forms[0].party_parking_qty.value);</script>&nbsp;
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_parking_qty.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<%if(inv_type.equals("P")){ %>
								<script>document.write(window.opener.document.forms[0].party_parking_rate.value);</script>&nbsp;
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_parking_rate.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<%if(inv_type.equals("P")){ %>
								<!-- <script>document.write(window.opener.document.forms[0].party_parking_amt.value);</script>&nbsp; -->
								<%}else{ %>
								<script>document.write(window.opener.document.forms[0].sys_parking_amt.value);</script>&nbsp;
								<%} %>
							</font>
						</div>
					</td>
				</tr>
			<%} %>
		<%} %>
	<%} %>
	
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
			<div align="left">
				<font size="1.5px" face="Arial"></font>
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
			<div align="left">
				<font size="1.5px" face="Arial"></font>
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
			<div align="left">
				<font size="1.5px" face="Arial"></font>
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
			<div align="left">
				<font size="1.5px" face="Arial"></font>
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
			<div align="left">
				<font size="1.5px" face="Arial"></font>
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
			<div align="left">
				<font size="1.5px" face="Arial"></font>
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
			<div align="center">
				<br><br><br><br><br><br><br><br>
				<font size="1.5px" face="Arial"><i>*** This is Computer Generated Report and No Signature Required ***</i></font>
			</div>
		</td>
	</tr>
</table>

<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
<input type="hidden" class="form-control form-control-sm" name="cont_no" value="<%=cont_no%>" maxLength="6" readOnly>
<input type="hidden" class="form-control form-control-sm" name="contract_type" value="<%=contract_type%>" readOnly>
<input type="hidden" class="form-control form-control-sm" name="counterparty_cd" value="<%=counterparty_cd%>" readOnly>
<input type="hidden" class="form-control form-control-sm" name="period_start_dt" value="<%=period_start_dt%>" readOnly>
<input type="hidden" class="form-control form-control-sm" name="period_end_dt" value="<%=period_end_dt%>" readOnly>
<input type="hidden" class="form-control form-control-sm" name="bu_plant_seq" value="<%=bu_plant_seq%>" readOnly>
</form>
</body>
</html>