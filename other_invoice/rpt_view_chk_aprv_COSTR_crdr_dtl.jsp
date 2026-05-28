<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Other Invoice</title>
<link rel="stylesheet" type="text/css" href="../font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="../css/common.css">
<%@ include file="../util/common_val.jsp" %>
<script>

function doSubmit()
{
	var msg="";
	var flag=true;
	
	var operation = document.forms[0].operation.value;
	var check_access = document.forms[0].check_access.value;
	var approve_access = document.forms[0].approve_access.value;

	if(operation=="APPROVE")
	{
		if(approve_access=="N")
		{
			msg+="You don't have Approval Rights!\n";
			flag=false;
		}
	}
	else if(operation=="CHECK")
	{
		if(check_access=="N")
		{
			msg+="You don't have Check Rights!\n";
			flag=false;
		}
	}
	
	if(flag)
	{
		
		var activity_nm=""
		var invNo="";
		if(operation=="CHECK")
		{
			if(document.forms[0].rd[0].checked)
			{
				activity_nm="Check"
					invNo+="Do you want to "+activity_nm+" Invoice?"	
			}
			else if(document.forms[0].rd[1].checked)
			{
				activity_nm="Reject"
				invNo+="Do you want to "+activity_nm+" Invoice?\n\n"	
			}
		}
		else if(operation=="APPROVE")
		{
			if(document.forms[0].rd[0].checked)
			{
				activity_nm="Approve"
					invNo+="Do you want to "+activity_nm+" Invoice?\n\n"	
			}
			else if(document.forms[0].rd[1].checked)
			{
				activity_nm="Reject"
				invNo+="Do you want to "+activity_nm+" Invoice?\n\n"	
				invNo+="Selected Invoice No. will be Removed!";
				
				document.forms[0].invoice_id_seq.value="";
				document.forms[0].inv_no.value="";
			}
		}
		var a=confirm(invNo);
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}
function Do_close(accroid,msg,msg_type)
{
	window.opener.refershPar(accroid,msg,msg_type);
	window.close();
}

function setInvoiceNo()
{
	var e = document.forms[0].invoice_id_seq;
	var invoiceNo = e.options[e.selectedIndex].text;
	if(document.forms[0].invoice_id_seq.value=="")
	{
		document.forms[0].inv_no.value="";
	}
	else
	{
		document.forms[0].inv_no.value=invoiceNo;
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.other_invoice.DataBean_Other_Invoice" id="other_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
String operation=request.getParameter("operation")==null?"INSERT":request.getParameter("operation");
String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
String sel_inv_no = request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String cr_dr_type = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
if(month.length() == 1)
{
	month="0"+month; 
}
String p_start_dt = utildate.getFirstDateOfMonth(month, year);
String p_end_dt = utildate.getLastDateOfMonth(month, year);
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
//String type = request.getParameter("type")==null?"":request.getParameter("type");

other_inv.setCallFlag("COSTR_CRDR_APROVAL");
other_inv.setComp_cd(owner_cd);
other_inv.setMonth(month);
other_inv.setYear(year);
other_inv.setInv_type(invoice_type);
other_inv.setSel_inv_no(sel_inv_no);
other_inv.setCr_dr_type(cr_dr_type);
other_inv.setFin_yr(financial_year);
other_inv.setInv_seq(invoice_seq);
other_inv.setOpration(operation);
other_inv.init();

Vector VPDF_COL1 = other_inv.getVPDF_COL1();
Vector VPDF_COL2 = other_inv.getVPDF_COL2();
Vector VPDF_COL3 = other_inv.getVPDF_COL3();
Vector VPDF_COL4 = other_inv.getVPDF_COL4();
Vector VPDF_COL5 = other_inv.getVPDF_COL5();
Vector VPDF_COL6 = other_inv.getVPDF_COL6();
Vector VPDF_COL7 = other_inv.getVPDF_COL7();
Vector VINVOICE_NO = other_inv.getVINVOICE_NO();
Vector VINVOICE_ID_SEQ = other_inv.getVINVOICE_ID_SEQ();

String activity_value = other_inv.getActivity_value();
String supp_address=other_inv.getSupplier_Address();
String supp_state=other_inv.getSupplier_State();
String supp_city=other_inv.getSupplier_City();
String supp_gstin_no=other_inv.getSupp_gstin_no();
String supp_pan_no=other_inv.getSupplier_Pan_No();
String supp_pin=other_inv.getSupp_pin();
String sac_code=other_inv.getSac_code();
String sac_des=other_inv.getSac_des();
String supp_state_tin=other_inv.getSupp_state_tin();
String vend_state_tin=other_inv.getVend_state_tin();
String supp_nm=other_inv.getSupp_nm();
String supp_abbr=other_inv.getSupp_abbr();

String vendor_name  = other_inv.getVendor_name();
String vend_address=other_inv.getVendor_Address();
String vend_state=other_inv.getVendor_State();
String vend_city=other_inv.getVendor_City();
String vend_gstin_no=other_inv.getVendor_GstTin_No();
String vend_pan_no=other_inv.getVendor_Pan_No();
String country=other_inv.getVendor_Country();
String pin=other_inv.getVendor_Pin_No();
String vend_abbr=other_inv.getAbbr();

String vessel_cd = other_inv.getVessel_cd();
String vessel_nm = other_inv.getVessel_nm();
String vessel_item = other_inv.getVessel_item();
String vessel_agent_nm = other_inv.getVessel_agent_nm();
String vessel_flag = other_inv.getVessel_flag();
String importer_nm = other_inv.getImporter_nm();
vend_state=other_inv.getVendor_State();
supp_state=other_inv.getSupplier_State();

String inv_no = other_inv.getInv_No();
String inv_id_seq = other_inv.getInv_id_seq();

String supp_cd = other_inv.getSupp_cd();
String vend_cd = other_inv.getVendor_cd();
String sac_cd =  other_inv.getSac_cd();
String main_invoice_dt = other_inv.getMain_invoice_dt();
String main_invoice_due_dt = other_inv.getMain_invoice_due_dt();
String main_qty_mmbtu = other_inv.getMain_qty_mmbtu();
String main_grt = other_inv.getMain_grt();
String main_berthing_hrs = other_inv.getMain_berthing_hrs();
String main_berthing_slots = other_inv.getMain_berthing_slots();
String main_rate = other_inv.getMain_rate();
String price_cd = other_inv.getPrice_cd();
String price_cd_nm = other_inv.getPrice_cd_nm();
String main_gross_amt = other_inv.getMain_gross_amt();
String main_tax_amt = other_inv.getMain_tax_amt();
String main_net_payable = other_inv.getMain_net_payable();
String invoice_raised_in = other_inv.getInvoice_raised_in();
String invoice_raised_in_nm = other_inv.getInvoice_raised_in_nm();
String main_tax_struct_info = other_inv.getMain_tax_struct_info();
String main_tax_struct_cd = other_inv.getMain_tax_struct_cd();

String crdr_invoice_dt = other_inv.getCrdr_invoice_dt();
String crdr_invoice_due_dt = other_inv.getCrdr_invoice_due_dt();
String new_qty_mmbtu = other_inv.getNew_qty_mmbtu();
String crdr_qty_mmbtu = other_inv.getQty_mmbtu();
String new_rate = other_inv.getNew_rate();
String crdr_rate = other_inv.getRate();
String new_grt = other_inv.getNew_grt();
String crdr_grt = other_inv.getGrt();
String new_berthing_hrs = other_inv.getNew_berthing_hrs();
String crdr_berthing_hrs = other_inv.getBerthing_hrs();
String new_berthing_slots = other_inv.getNew_berthing_slots();
String crdr_berthing_slots = other_inv.getBerthing_slots();
String new_gross_amt = other_inv.getNew_gross_amt();
String crdr_gross_amt = other_inv.getGross_amt();
String new_tax_amt = other_inv.getNew_tax_amt();
String crdr_tax_amt = other_inv.getTax_amt();
String new_net_payable = other_inv.getNew_net_payable();
String crdr_net_payable = other_inv.getNet_payable();
String new_tax_struct_cd = other_inv.getNew_tax_struct_cd();
String new_tax_struct_info = other_inv.getNew_tax_struct_info();
String tax_struct_cd = other_inv.getTax_struct_cd();
String tax_struct_info = other_inv.getTax_struct_info();
String period_start_dt = other_inv.getPeriod_start_dt();
String period_end_dt = other_inv.getPeriod_end_dt();
String financial_yr = other_inv.getFinancial_year();
String reason = other_inv.getReason();

String criteri_formula=other_inv.getCriteri_formula();
String amt_in_word = other_inv.getAmt_in_word();

String cr_dr_type_nm = "";
if(cr_dr_type.equals("CR"))
{
	cr_dr_type_nm="Credit Note";
}
else if(cr_dr_type.equals("DR"))
{
	cr_dr_type_nm="Debit Note";
}
String remark1 = other_inv.getremark1();
String remark2=other_inv.getremark2();
String remark3=other_inv.getRemark3();
String remark4=other_inv.getRemark4();

remark2 = remark2.replace("(b)", "<br> (b)");
%>
<body onload="<%if(!msg.equals("")){ %>Do_close('<%=accroid%>','<%=msg%>','<%=msg_type%>');<%} %><%if(operation.equals("APPROVE")){ %>setInvoiceNo();<%} %>">

<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_other_invoice">
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<!-- <font size="2" face="Arial">
					<b>ORIGINAL</b>
					<br>
				</font> -->
				<font size="4" face="Arial">
					<b><%=supp_nm%></b>
					<br>
					<b><%=cr_dr_type_nm %></b>
				</font>
				<font size="2" face="Arial">
					<br>
					<b><%=cr_dr_type_nm %> issued under Rule 53 of the Central Goods and Services Tax Rules, 2017</b>
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
			<div align="center">&nbsp;</div>
		</td>
	</tr>
	<tr valign="top">
		<td colspan="3" width="50%">
			<div align="left">
				<font size="1.5px" face="Arial">
					Registered Office:&nbsp; 
					<br><b><%=supp_nm%></b>
					<br><%=supp_address%>,<%=supp_city%>
					<br><%=supp_state%>&nbsp;-&nbsp;<%=supp_pin%>
				</font>
			</div>
		</td>
		<td colspan="1" width="10%">
		</td>
		<td colspan="3" width="40%">
			<div align="left">
				<font size="1.5px" face="Arial">
					To:&nbsp;
					<br><b><%=vendor_name%></b>,
					<br><%=vend_address%>,<%=vend_city%>
					<br><%=vend_state %>&nbsp;<%if(!pin.equals("")) {%>-&nbsp;<%=pin%><%} %>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="top"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="top">
		<td colspan="3">
			<div>
				<font size="1.5px" face="Arial">
					State: <%=supp_state %>
				<br>State Code: <%=supp_state_tin%>
				<br>GSTIN: <%=supp_gstin_no%>
				<br>PAN: <%=supp_pan_no%>
				<br>SAC: <%=sac_code%>
				<br>Description of Service: <%=sac_des%>
				<br>Place of Supply: <%=supp_state%>
				</font>
			</div>
		</td>
		<td colspan="1" width="10%">
			<div align="center"><font size="1.5px" face="Arial"></font></div>
		</td>
		<td colspan="3" width="40%">
			<div align="left">
				<font size="1.5px" face="Arial">
				<%if(!vend_state.equals("")) {%>State: <%=vend_state %><%} %>
				<%if(!vend_state_tin.equals("")) {%><br>State Code: <%=vend_state_tin%><%} %>
				<%if(!vend_gstin_no.equals("")) {%><br>GSTIN: <%=vend_gstin_no%><%} %>
				<%if(!vend_pan_no.equals("")) {%><br>PAN: <%=vend_pan_no%><%} %>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="top">
		<td colspan="7">&nbsp;</td>
	</tr>
	<tr valign="top">
		<td colspan="7">&nbsp;</td>
	</tr>
	<tr valign="middle">
	
		<td colspan="3"></td>
		<td colspan="2" width="18%">
			<div align="center">
				<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<font face="Arial"><b><%=cr_dr_type_nm %> No:&nbsp;</b></font>
				
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=cr_dr_type_nm %> Date:&nbsp;</b></font>							
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
							<%if(operation.equals("APPROVE")){ %>
								<select name="invoice_id_seq" style="background:yellow;font-weight:bold;pointer-events: none;v" onchange="setInvoiceNo();">
									<option value="">--Select--</option>
									<%for(int i=0; i<VINVOICE_ID_SEQ.size();i++){ %>
									<option value="<%=VINVOICE_ID_SEQ.elementAt(i)%>"><%=VINVOICE_NO.elementAt(i)%></option>
									<%} %>
								</select>
								<script>document.forms[0].invoice_id_seq.value="<%=inv_id_seq%>"</script>
							<%}else{ %>
								<div align="center">
									<font size="1.5px" face="Arial"><b><%=inv_no%>&nbsp;</b></font>
								</div>
							<%} %>
						</td>
					</tr>
					<tr>
						<td>
							<div align="center">
								<font size="1.5px" face="Arial"><b><%=crdr_invoice_dt%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="center">
								<font size="1.5px" face="Arial"><b><%=crdr_invoice_due_dt%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	<tr valign="middle"><td colspan="7">&nbsp;</td></tr>
</table>&nbsp;
<table width="100%"  border="1" align="center" cellpadding="2" cellspacing="0">
	<tr valign="bottom">
		<td width="6%"><div align="center"><font size="1.5px" face="Arial"><b>Sr. No.</b></font></div></td>
		<td width="34%"><div align="center"><font size="1.5px" face="Arial"><b>Description</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Currency</b></font></div></td>
		<td width="15%"><div align="center"><font size="1.5px" face="Arial"><b>Amount</b></font></div></td>
	</tr>
	<%for(int i=0; i<VPDF_COL1.size(); i++){ %>
		<tr valign="top">
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=VPDF_COL1.elementAt(i) %></font>
				</div>
			</td>
			<td>
				<div <%if(Double.parseDouble(""+VPDF_COL1.elementAt(i))/(int)Double.parseDouble(""+VPDF_COL1.elementAt(i))>1){%>align="right"<%}else{%>align="left"<%} %>>
					<font size="1.5px" face="Arial"><%=VPDF_COL2.elementAt(i) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=VPDF_COL3.elementAt(i) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=VPDF_COL4.elementAt(i) %></font>
				</div>
			</td>
		</tr>
	<%} %>
</table>
<table border="0" width="100%" align="center" cellpadding="2" cellspacing="0">
	<tr align="center"><td colspan="7">&nbsp;</td></tr>
</table>
<table width="100%" cellpadding="0" cellspacing="0" border="1">
	<tr>
		<td width="100%">
			<font size="1.5px" face="Arial"><b>Amount in Words &nbsp;&nbsp;| &nbsp;Rupees&nbsp;<%=amt_in_word %>&nbsp;Only</b></font>
		</td>
	</tr>
</table>
<table border="0" width="100%" align="center" cellpadding="2" cellspacing="0">
	<tr align="center">
		<td colspan="7">&nbsp;</td>
	</tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="left">
				<font size="1px" face="Arial">
					Reason for <%=cr_dr_type_nm%> : <%=reason%>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="left">
				<font size="1.5px" face="Arial"><b>For <%=supp_nm%><br><br><br><br>Authorised Signatory</b></font>
			</div>
		</td>
	</tr>
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
</table>
<%if(cr_dr_type.equals("DR")){%>
<table border="0" width="100%" align="center" cellpadding="2" cellspacing="0">
	<tr><td colspan="2"><font size="1.5px" face="Arial"><b>Notes:</b></font></td></tr>
	<tr><td valign="top"><font size="1.5px" face="Arial">1</font></td><td  align="left"  style="text-align: left;"><font size="1.5px" face="Arial">&nbsp; <%=remark1%></font></td></tr>
	<tr></tr>
</table>&nbsp;&nbsp;
<%} %>
<table border="0" width="100%" align="center" cellpadding="2" cellspacing="0">
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
			<%if(operation.equals("CHECK") || operation.equals("APPROVE")){ %>
				<% if(operation.equals("CHECK")){%>Checked<%}else if(operation.equals("APPROVE")){ %>Approved<%} %> OK:&nbsp;
				<input type="radio" name="rd" value="Y" onClick="doSubmit();" <%if(activity_value.equals("Y")){ %>checked<%} %>>&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="radio" name="rd" value="N" onClick="doSubmit();" <%if(activity_value.equals("N")){ %>checked<%} %>>&nbsp;<b>No</b>
			<%} %>
			</div>
		</td>
	</tr>
</table>

<input type="hidden" name="option" value="COSTR_CRDR_APPROVAL">
<input type="hidden" name="operation" value="<%=operation%>">
<input type="hidden" name="inv_no" value="<%=inv_no%>" >
<input type="hidden" name="inv_type" value="<%=invoice_type%>">
<input type="hidden" name="inv_seq" value="<%=invoice_seq%>">
<input type="hidden" name="fin_year" value="<%=financial_year%>">
<input type="hidden" name="supp_cd" value="<%=supp_cd%>">
<input type="hidden" name="vend_abbr" value="<%=vend_abbr%>">
<input type="hidden" name="supp_abbr" value="<%=supp_abbr%>">
<input type="hidden" name="supp_nm" value="<%=supp_nm%>">
<input type="hidden" name="supp_nm" value="<%=supp_nm%>">
<input type="hidden" name="sel_inv_no" value="<%=sel_inv_no%>">
<input type="hidden" name="cr_dr_type" value="<%=cr_dr_type%>">

<input type="hidden" name="read_access" value="<%=read_access%>">
<input type="hidden" name="write_access" value="<%=write_access%>">
<input type="hidden" name="check_access" value="<%=check_access%>">
<input type="hidden" name="print_access" value="<%=print_access%>">
<input type="hidden" name="delete_access" value="<%=delete_access%>">  	
<input type="hidden" name="audit_access" value="<%=audit_access%>">
<input type="hidden" name="authorize_access" value="<%=authorize_access%>">
<input type="hidden" name="approve_access" value="<%=approve_access%>">
<input type="hidden" name="execute_access" value="<%=execute_access%>">
<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<input type="hidden" name="accord" value="<%=accroid%>">
<input type="hidden" name="u" value="<%=u%>">

</form>
</body>
</html>
</body>
</html>