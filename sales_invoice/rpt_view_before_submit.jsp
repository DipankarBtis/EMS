<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<jsp:useBean class="com.etrm.fms.sales_invoice.DataBean_Sales_Invoice" id="sales_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utilDate" scope="request"></jsp:useBean>
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

String price_cd=request.getParameter("price_cd")==null?"":request.getParameter("price_cd");
String invoice_raised_in=request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in");
String inv_flag=request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");

String mkt_mrgin=request.getParameter("mkt_mrgin")==null?"":request.getParameter("mkt_mrgin");
String oth_chrg=request.getParameter("oth_chrg")==null?"":request.getParameter("oth_chrg");
String trans_tariff=request.getParameter("trans_tariff")==null?"":request.getParameter("trans_tariff");
String mkt_mrgin_amt=request.getParameter("mkt_mrgin_amt")==null?"":request.getParameter("mkt_mrgin_amt");
String oth_chrg_amt=request.getParameter("oth_chrg_amt")==null?"":request.getParameter("oth_chrg_amt");
String trans_tariff_amt=request.getParameter("trans_tariff_amt")==null?"":request.getParameter("trans_tariff_amt");
String isGrossIncTriff=request.getParameter("isGrossIncTriff")==null?"":request.getParameter("isGrossIncTriff");

String contact_person=request.getParameter("contact_person")==null?"":request.getParameter("contact_person");
String bu_contact_person=request.getParameter("bu_contact_person")==null?"":request.getParameter("bu_contact_person");
String invoice_dt=request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
String agmt_base=request.getParameter("agmt_base")==null?"":request.getParameter("agmt_base");
String tcs_tds=request.getParameter("tcs_tds")==null?"":request.getParameter("tcs_tds");
String adv_adj_flg=request.getParameter("adv_adj_flg")==null?"":request.getParameter("adv_adj_flg");

sales_inv.setCallFlag("SALES_INVOICE_DETAIL");
sales_inv.setComp_cd(owner_cd);
sales_inv.setCounterparty_cd(counterparty_cd);
sales_inv.setAgmt_no(agmt_no);
sales_inv.setAgmt_rev_no(agmt_rev);
sales_inv.setCont_no(cont_no);
sales_inv.setCargo_no(cargo_no);
sales_inv.setCont_rev_no(cont_rev);
sales_inv.setContract_type(contract_type);
sales_inv.setPlant_seq(plant_seq);
sales_inv.setBilling_cycle(billing_cycle);
sales_inv.setPeriod_start_dt(period_start_dt);
sales_inv.setPeriod_end_dt(period_end_dt);
sales_inv.setBu_unit(bu_unit);
sales_inv.setPrice_cd(price_cd);
sales_inv.setInvoice_raised_in(invoice_raised_in);
sales_inv.setContact_person_cd(contact_person);
sales_inv.setBu_contact_person_cd(bu_contact_person);
sales_inv.setInvoice_dt(invoice_dt);
sales_inv.setInv_flag(inv_flag);
sales_inv.init();

String signingDt = sales_inv.getSigningDt();
String contRef = sales_inv.getContRef();

String price_cd_nm = sales_inv.getPrice_cd_nm();
String invoice_raised_in_nm = sales_inv.getInvoice_raised_in_nm();

String couterpty_nm = sales_inv.getCouterpty_nm();
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

String agmtSigningDt=sales_inv.getAgmtSigningDt();
String arrivalDt=sales_inv.getArrivalDt();

%>
<script>
var sub_tax_struct=window.opener.document.forms[0].sub_tax_struct;
var sub_tax_amt=window.opener.document.forms[0].sub_tax_amt;

var receipt_voucher = window.opener.document.getElementsByName("receipt_voucher");

var display="";
var adj_display="";
if(sub_tax_struct!=null && sub_tax_struct!=undefined)
{
	if(sub_tax_struct.length!=undefined)
	{
		for(var i=0;i<sub_tax_struct.length;i++)
		{
			display+="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+sub_tax_struct[i].value+"</font></div></td><td><div align='left'><font size='1.5px' face='Arial'></font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='left'><font size='1.5px' face='Arial'>"+sub_tax_amt[i].value+"&nbsp;</font></div></td></tr>";
			
			var tax_struct = sub_tax_struct[i].value;
			let nameParts = tax_struct.split(' ');
    		taxAbbr= nameParts[0];
    		
    		var tax_adjust = window.opener.document.getElementsByName(taxAbbr+"_adjust");
    		
    		var temp_info="";
    		if(receipt_voucher!=null && receipt_voucher!=undefined)
    		{
    			if(receipt_voucher.length!=undefined)
    			{
    				for(var j=0;j<receipt_voucher.length;j++)
    				{
    					if(receipt_voucher[j].value != "" && tax_adjust[j].value!="")
    					{
    						if(temp_info=="")
    						{
    							temp_info=receipt_voucher[j].value;
    						}
    						else
    						{
    							temp_info+=", "+receipt_voucher[j].value;
    						}
    					}
    				}
    			}
    		}
    		<%if(adv_adj_flg.equals("Y")){%>
			adj_display+="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='left'><font size='1.5px' face='Arial'>Adjustment for Advance "+taxAbbr+" paid against Receipt Voucher No "+temp_info+"</font></div></td><td><div align='left'><font size='1.5px' face='Arial'></font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+window.opener.document.getElementsByName(taxAbbr+"_total")[0].value+"&nbsp;</font></div></td></tr>";
			<%}%>
    	}
	}
	else
	{
		//NOT REQUIRED FOR SINGLE TAX COMPONENT
		//display="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+sub_tax_struct.value+"</font></div></td><td><div align='left'><font size='1.5px' face='Arial'></font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+sub_tax_amt.value+"&nbsp;</font></div></td></tr>";
		
		var tax_struct = sub_tax_struct.value;
		let nameParts = tax_struct.split(' ');
    	taxAbbr= nameParts[0];
    	
    	var tax_adjust = window.opener.document.getElementsByName(taxAbbr+"_adjust");
		
		var temp_info="";
		if(receipt_voucher!=null && receipt_voucher!=undefined)
		{
			if(receipt_voucher.length!=undefined)
			{
				for(var j=0;j<receipt_voucher.length;j++)
				{
					if(receipt_voucher[j].value != "" && tax_adjust[j].value!="")
					{
						if(temp_info=="")
						{
							temp_info=receipt_voucher[j].value;
						}
						else
						{
							temp_info+=", "+receipt_voucher[j].value;
						}
					}
				}
			}
		}
    	
		<%if(adv_adj_flg.equals("Y")){%>
		adj_display+="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='left'><font size='1.5px' face='Arial'>Adjustment for Advance "+taxAbbr+" paid against Receipt Voucher No "+temp_info+"</font></div></td><td><div align='left'><font size='1.5px' face='Arial'></font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+window.opener.document.getElementsByName(taxAbbr+"_total")[0].value+"&nbsp;</font></div></td></tr>";
		<%}%>
	}
}
//document.getElementById("subTaxDtl").innerHTML=display;


var gross_adjust = window.opener.document.getElementsByName("gross_adjust");
var adjGross_info="Adjustment for Advance Gross paid against Receipt Voucher No ";
var temp_info="";
if(receipt_voucher!=null && receipt_voucher!=undefined)
{
	if(receipt_voucher.length!=undefined)
	{
		for(var i=0;i<receipt_voucher.length;i++)
		{
			if(receipt_voucher[i].value != "" && gross_adjust[i].value!="")
			{
				if(temp_info=="")
				{
					temp_info=receipt_voucher[i].value;
				}
				else
				{
					temp_info+=", "+receipt_voucher[i].value;
				}
			}
		}
	}
}
adjGross_info=adjGross_info+""+temp_info;
</script>
<body>
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
					<%if(contract_type.equals("Q") || contract_type.equals("O")) {%>
					<b>TAX INVOICE</b>
					<%}else if(bu_plantState.equals(plantState)){ %>
					<b>TAX INVOICE</b>
					<%}else{ %>
					<b>RETAIL INVOICE</b>
					<%} %>
				</font>
				<%if(contract_type.equals("Q") || contract_type.equals("O")) {%>
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
					In respect of 
					<%if(contract_type.equals("S")){ %>Supply Notice (<%=contRef%>) executed on <%=signingDt%> pursuant to Framework Gas Sales Agreement executed on <%=agmtSigningDt%>
					<%}else if(contract_type.equals("O")){ %><%--  pursuant to Framework --%> LTCORA Agreement executed on <%=agmtSigningDt%> & CN (<%=contRef%>) executed on <%=signingDt%>
					<%}else if(contract_type.equals("Q")){ %><%-- Period (<%=contRef%>) executed on <%=signingDt%> pursuant to Framework --%> LTCORA Agreement executed on <%=agmtSigningDt%>
					<%}else if(contract_type.equals("L")){ %>Letter of Agreement (<%=contRef%>) executed on <%=signingDt%> 
					<%}else if(contract_type.equals("X")){ %>Exchange Transaction (<%=contRef%>) executed on <%=signingDt%>
					<%} %> 
					<br>
					between <%=owner_nm%> and <%=couterpty_nm%> 
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
								<font size="1.5px" face="Arial">
									<b>
									<script>document.write(window.opener.document.forms[0].invoice_dt.value);</script>&nbsp;
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
									<script>document.write(window.opener.document.forms[0].invoice_due_dt.value);</script>&nbsp;
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
									<script>document.write(window.opener.document.forms[0].invoice_no.value);</script>&nbsp;
									</b>
								</font>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<%if(!inv_flag.equals("UG") && !inv_flag.equals("ST")){ %>
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
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Attachment<br>Reference</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Currency</b></font></div></td>
		<td width="15%"><div align="center"><font size="1.5px" face="Arial"><b>Quantity<br>(MMBTU)</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Rate</b></font></div></td>
		<td width="15%"><div align="center"><font size="1.5px" face="Arial"><b>Amount</b></font></div></td>
	</tr>
	<%int srNo=0; %>
	<%if(inv_flag.equals("UG")) {
		String mnthYr="";
		if(!period_end_dt.equals(""))
		{
			mnthYr=period_end_dt.substring(3,period_end_dt.length());
			String[] split= mnthYr.split("/");
			mnthYr=utilDate.getMonthName(period_end_dt)+"-"+split[1];
		}
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
				<font size="1.5px" face="Arial">Actual Quantity of LNG discharged during month of <%=mnthYr%></font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial">Att1</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%//=price_cd_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<script>document.write(window.opener.document.forms[0].alloc_qty.value);</script>&nbsp;
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<script></script>&nbsp;
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<script></script>&nbsp;
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
				<font size="1.5px" face="Arial">
					<script>document.write(window.opener.document.forms[0].sug_percent.value);</script>% of above as SUG (System Use Gas)
				</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%//=price_cd_nm%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<script>document.write(window.opener.document.forms[0].sug_qty.value);</script>&nbsp;
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<script></script>&nbsp;
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<script></script>&nbsp;
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
				<font size="1.5px" face="Arial">
					Value of SUG (only for the purpose of GST payment on LTCORA Services)
				</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"></font>
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
					<script></script>&nbsp;
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<script>document.write(window.opener.document.forms[0].price.value);</script>&nbsp;
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<script>document.write(window.opener.document.forms[0].gross_amt.value);</script>&nbsp;
				</font>
			</div>
		</td>
	</tr>
	<%}else{ %>
		<%if(contract_type.equals("O") || contract_type.equals("Q")){ %>
			<%if(inv_flag.equals("ST")) {%>
				<%srNo+=1;%>
				<tr valign="top">
					<td>
						<div align="center">
							<font size="1.5px" face="Arial"><%=srNo%></font>
						</div>
					</td>
					<td>
						<div align="left">
							<font size="1.5px" face="Arial">Storage Charges For The Extended Storage Duration For Cargo Arrived on <%=arrivalDt%></font>
						</div>
					</td>
					<td>
						<div align="center">
							<font size="1.5px" face="Arial">Att1</font>
						</div>
					</td>
					<td>
						<div align="center">
							<font size="1.5px" face="Arial"><%=price_cd_nm%></font>
						</div>
					</td>
					<td>
						<div align="center">
							<font size="1.5px" face="Arial">
								As per Att1
							</font>
						</div>
					</td>
					<td>
						<div align="center">
							<font size="1.5px" face="Arial">
								As per Att1
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<script>document.write(window.opener.document.forms[0].gross_amt.value);</script>&nbsp;
							</font>
						</div>
					</td>
				</tr>
			<%}else{ %>
				<%srNo+=1;%>
				<tr valign="top">
					<td>
						<div align="center">
							<font size="1.5px" face="Arial"><%=srNo%></font>
						</div>
					</td>
					<td>
						<div align="left">
							<font size="1.5px" face="Arial">Natural Gas (Regasified)</font>
						</div>
					</td>
					<td>
						<div align="center">
							<font size="1.5px" face="Arial">Att1</font>
						</div>
					</td>
					<td>
						<div align="center">
							<font size="1.5px" face="Arial"><%//=price_cd_nm%></font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<script>document.write(window.opener.document.forms[0].alloc_qty.value);</script>&nbsp;
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<!-- <script>document.write(window.opener.document.forms[0].price.value);</script>&nbsp; -->
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<!-- <script>document.write(window.opener.document.forms[0].gross_amt.value);</script>&nbsp; -->
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
							<font size="1.5px" face="Arial">LTCORA Tariff</font>
						</div>
					</td>
					<td>
						<div align="center">
							<font size="1.5px" face="Arial"></font>
						</div>
					</td>
					<td>
						<div align="center">
							<font size="1.5px" face="Arial"><%=price_cd_nm%>/MMBTU</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<!-- <script>document.write(window.opener.document.forms[0].alloc_qty.value);</script>&nbsp; -->
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<script>document.write(window.opener.document.forms[0].price.value);</script>&nbsp;
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<!-- <script>document.write(window.opener.document.forms[0].gross_amt.value);</script>&nbsp; -->
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
							<font size="1.5px" face="Arial">Gross Amount</font>
						</div>
					</td>
					<td>
						<div align="center">
							<font size="1.5px" face="Arial"></font>
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
								<!-- <script>document.write(window.opener.document.forms[0].alloc_qty.value);</script>&nbsp; -->
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<script></script>&nbsp;
							</font>
						</div>
					</td>
					<td>
						<div align="right">
							<font size="1.5px" face="Arial">
								<script>document.write(window.opener.document.forms[0].gross_amt.value);</script>&nbsp;
							</font>
						</div>
					</td>
				</tr>
				<%} %>
			<%} %>
		<%}else{ %>
			<%srNo+=1;%>
			<tr valign="top">
				<td>
					<div align="center">
						<font size="1.5px" face="Arial"><%=srNo%></font>
					</div>
				</td>
				<td>
					<div align="left">
						<font size="1.5px" face="Arial">Natural Gas</font>
					</div>
				</td>
				<td>
					<div align="center">
						<font size="1.5px" face="Arial">Att1</font>
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
							<script>document.write(window.opener.document.forms[0].alloc_qty.value);</script>&nbsp;
						</font>
					</div>
				</td>
				<td>
					<div align="right">
						<font size="1.5px" face="Arial">
							<script>document.write(window.opener.document.forms[0].price.value);</script>&nbsp;
						</font>
					</div>
				</td>
				<td>
					<div align="right">
						<font size="1.5px" face="Arial">
							<script>document.write(window.opener.document.forms[0].gross_amt.value);</script>&nbsp;
						</font>
					</div>
				</td>
			</tr>
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
			<div align="center">
				<font size="1.5px" face="Arial">Att2</font>
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
					<script>document.write(window.opener.document.forms[0].exchng_rate.value);</script>&nbsp;
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
	
	<%if(!inv_flag.equals("UG")) {%>
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
					<script>document.write(window.opener.document.forms[0].gross_amt1.value);</script>&nbsp;
				</font>
			</div>
		</td>
	</tr>
	<%} %>
	
	<%-- <%if(agmt_base.equals("D")){ %> --%>
	<%if(!trans_tariff.equals("")){ 
		if(Double.parseDouble(trans_tariff_amt) > 0){%>
		<%srNo+=1;%>
		<tr valign="top">
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=srNo%></font>
				</div>
			</td>
			<td>
				<div align="left">
					<font size="1.5px" face="Arial">Transportation Tariff</font>
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
					<font size="1.5px" face="Arial">
						<script>document.write(window.opener.document.forms[0].transportation_tariff.value);</script>&nbsp;
					</font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial">
						<script>document.write(window.opener.document.forms[0].transportation_amount.value);</script>&nbsp;
					</font>
				</div>
			</td>
		</tr>
		<%} %>
	<%} %>
	
	<%if(!mkt_mrgin.equals("")){ 
		if(Double.parseDouble(mkt_mrgin_amt) > 0){%>
		<%srNo+=1;%>
		<tr valign="top">
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=srNo%></font>
				</div>
			</td>
			<td>
				<div align="left">
					<font size="1.5px" face="Arial">Marketing Margin</font>
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
					<font size="1.5px" face="Arial">
						<script>document.write(window.opener.document.forms[0].marketing_margin.value);</script>&nbsp;
					</font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial">
						<script>document.write(window.opener.document.forms[0].marketing_margin_amount.value);</script>&nbsp;
					</font>
				</div>
			</td>
		</tr>
		<%} %>
	<%} %>
	
	<%if(!oth_chrg.equals("")){ 
		if(Double.parseDouble(oth_chrg_amt) > 0){%>
		<%srNo+=1;%>
		<tr valign="top">
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=srNo%></font>
				</div>
			</td>
			<td>
				<div align="left">
					<font size="1.5px" face="Arial">Other Charges</font>
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
					<font size="1.5px" face="Arial">
						<script>document.write(window.opener.document.forms[0].other_charges.value);</script>&nbsp;
					</font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial">
						<script>document.write(window.opener.document.forms[0].other_charges_amount.value);</script>&nbsp;
					</font>
				</div>
			</td>
		</tr>
		<%} %>
	<%} %>
	
	<%if(isGrossIncTriff.equals("true")){ %>
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial">Total Gross Amount</font>
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
					<script>document.write(window.opener.document.forms[0].gross_include_transport_tariff.value);</script>&nbsp;
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
				<font size="1.5px" face="Arial">Tax(
					<script>document.write(window.opener.document.forms[0].tax_struct_dtl.value);</script>
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
					<script>document.write(window.opener.document.forms[0].tax_amt.value);</script>&nbsp;
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
				<font size="1.5px" face="Arial">Invoice Amount <%if(inv_flag.equals("UG")) {%>- GST on SUG<%} %></font>
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
					<script>document.write(window.opener.document.forms[0].invoice_amt.value);</script>&nbsp;
				</font>
			</div>
		</td>
	</tr>	
	
	<%if(tcs_tds.equals("TCS")){ %>
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
				<font size="1.5px" face="Arial">
					<script>document.write(window.opener.document.forms[0].tcs_factor.value);</script>%&nbsp;
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial">
					<script>document.write(window.opener.document.forms[0].tcs_amt.value);</script>&nbsp;
				</font>
			</div>
		</td>
	</tr>
	<%} %>
	
	<%if(adv_adj_flg.equals("Y")){ %>
	<%srNo+=1;%>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=srNo%></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial"><script>document.write(adjGross_info)</script></font>
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
					<script>document.write(window.opener.document.forms[0].gross_total.value);</script>&nbsp;
				</font>
			</div>
		</td>
	</tr>
	<script>document.write(adj_display)</script>
	<%srNo+=1;%>
	<tr valign="top" style="font-weight: bold;">
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
					<script>document.write(window.opener.document.forms[0].adjusted_net_payable.value);</script>&nbsp;
				</font>
			</div>
		</td>
	</tr>
	<%}else{ %>
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
					<script>document.write(window.opener.document.forms[0].net_payable.value);</script>&nbsp;
				</font>
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
				<script>document.write(window.opener.document.forms[0].remark1.value);</script>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="left">
				<font size="1px" face="Arial"><script>document.write(window.opener.document.forms[0].remark2.value);</script></font>
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
</table>



</body>
</html>