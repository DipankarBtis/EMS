<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="dlng_inv" scope="request"></jsp:useBean>
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
String bu_state_tin=request.getParameter("bu_state_tin")==null?"0":request.getParameter("bu_state_tin");
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

String ori_inv_flag=request.getParameter("ori_inv_flag")==null?"":request.getParameter("ori_inv_flag");
String ori_invoice_no=request.getParameter("ori_invoice_no")==null?"":request.getParameter("ori_invoice_no");
String exist_fin_yr=request.getParameter("exist_financial_year")==null?"":request.getParameter("exist_financial_year");
String ori_inv_seq=request.getParameter("ori_invoice_seq")==null?"":request.getParameter("ori_invoice_seq");

String int_total_percentage=request.getParameter("int_total_percentage")==null?"":request.getParameter("int_total_percentage");
String interest_days=request.getParameter("interest_days")==null?"":request.getParameter("interest_days");
String truck_cd=request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");
String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"":request.getParameter("truck_trans_cd");

dlng_inv.setCallFlag("LP_INVOICE_DETAIL");
dlng_inv.setComp_cd(owner_cd);
dlng_inv.setCounterparty_cd(counterparty_cd);
dlng_inv.setAgmt_no(agmt_no);
dlng_inv.setAgmt_rev_no(agmt_rev);
dlng_inv.setCont_no(cont_no);
dlng_inv.setCargo_no(cargo_no);
dlng_inv.setCont_rev_no(cont_rev);
dlng_inv.setContract_type(contract_type);
dlng_inv.setPlant_seq(plant_seq);
dlng_inv.setBilling_cycle(billing_cycle);
dlng_inv.setPeriod_start_dt(period_start_dt);
dlng_inv.setPeriod_end_dt(period_end_dt);
dlng_inv.setBu_unit(bu_unit);
dlng_inv.setPrice_cd(price_cd);
dlng_inv.setInvoice_raised_in(invoice_raised_in);
dlng_inv.setContact_person_cd(contact_person);
dlng_inv.setBu_contact_person_cd(bu_contact_person);
dlng_inv.setInvoice_dt(invoice_dt);
dlng_inv.setInv_flag(inv_flag);

dlng_inv.setBu_state_tin(bu_state_tin);
dlng_inv.setOri_inv_flag(ori_inv_flag);
dlng_inv.setExist_financial_year(exist_fin_yr);
dlng_inv.setOri_invoice_seq(ori_inv_seq);
dlng_inv.setTruck_cd(truck_cd);
dlng_inv.setTruck_trans_cd(truck_trans_cd);

dlng_inv.init();



String signingDt = dlng_inv.getSigningDt();
String contRef = dlng_inv.getContRef();

String price_cd_nm = dlng_inv.getPrice_cd_nm();
String invoice_raised_in_nm = dlng_inv.getInvoice_raised_in_nm();

String couterpty_nm = dlng_inv.getCouterpty_nm();
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

String agmtSigningDt=dlng_inv.getAgmtSigningDt();
String arrivalDt=dlng_inv.getArrivalDt();

String ori_inv_due_dt=dlng_inv.getOri_inv_due_dt();
String ori_inv_net_amt=dlng_inv.getOri_inv_net_amt();
String ori_inv_payrecv_amt=dlng_inv.getOri_inv_payrecv_amt();
String ori_inv_payrecv_dt=dlng_inv.getOri_inv_payrecv_dt();
String ori_inv_dt=dlng_inv.getOri_inv_dt();

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
			display+="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+sub_tax_struct[i].value+"</font></div></td><td><div align='left'><font size='1.5px' face='Arial'></font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='left'><font size='1.5px' face='Arial'>"+sub_tax_amt[i].value+"&nbsp;</font></div></td></tr>";
			
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
<%--     		<%if(adv_adj_flg.equals("Y")){%> --%>
<%-- 			adj_display+="<tr valign='top'><td><div align='center'><font size='1.5px' face='Arial'></font></div></td><td><div align='left'><font size='1.5px' face='Arial'>Adjustment for Advance "+taxAbbr+" paid against Receipt Voucher No "+temp_info+"</font></div></td><td><div align='left'><font size='1.5px' face='Arial'></font></div></td><td><div align='center'><font size='1.5px' face='Arial'><%=invoice_raised_in_nm%></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'></font></div></td><td><div align='right'><font size='1.5px' face='Arial'>"+window.opener.document.getElementsByName(taxAbbr+"_total")[0].value+"&nbsp;</font></div></td></tr>"; --%>
<%-- 			<%}%> --%>
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
					In respect of 
					<%if(contract_type.equals("F")){ %>Supply Notice (<%=contRef%>) executed on <%=signingDt%> pursuant to Framework LNG Sales Agreement executed on <%=agmtSigningDt%>
					<%}else if(contract_type.equals("E")){ %>Letter of Agreement (<%=contRef%>) executed on <%=signingDt%> 
					<%}else if(contract_type.equals("W")){ %>Exchange Transaction (<%=contRef%>) executed on <%=signingDt%>
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
	<%int srNo=0; %>
	<%
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
				<font size="1.5px" face="Arial">Delayed Payment Invoice Generated Against Invoice No :<br> <%=ori_invoice_no%> Dated <%=ori_inv_dt %></font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="center">
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
				<font size="1.5px" face="Arial">Net Amount</font>
			</div>
		</td>
		<td>
			<div align="center">
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
				<font size="1.5px" face="Arial">
					
				</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=ori_inv_net_amt %></font>
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
				<font size="1.5px" face="Arial">Due Date</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=ori_inv_due_dt %></font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"></font>
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
					 Payment Received Date
				</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=ori_inv_payrecv_dt %></font>
			</div>
		</td>
		<td>
			<div align="center">
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
				<font size="1.5px" face="Arial"><%=ori_inv_payrecv_amt %></font>
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
				<font size="1.5px" face="Arial">No Of Days For Late Payment Invoice</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=interest_days %> Days</font>
			</div>
		</td>
		<td>
			<div align="center">
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
				<font size="1.5px" face="Arial"></font>
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
				<font size="1.5px" face="Arial"> Interest Rate</font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=int_total_percentage %> %</font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"></font>
			</div>
		</td>
	</tr>
	
	<%if(contract_type.equals("B") || contract_type.equals("M")){ %>
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
					<font size="1.5px" face="Arial">
						<script>document.write(window.opener.document.forms[0].tax_amt.value);</script>&nbsp;
					</font>
				</div>
			</td>
		</tr>
		<script>document.write(display)</script>
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
				<font size="1.5px" face="Arial"> Payable Amount </font>
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
				<font size="1.5px" face="Arial">
					<script>document.write(window.opener.document.forms[0].net_payable.value);</script>&nbsp;
				</font>
			</div>
		</td>
	</tr>
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