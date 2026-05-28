<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Sales Invoice</title>
<link rel="stylesheet" type="text/css" href="../font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="../css/common.css">
<%@ include file="../util/common_val.jsp" %>
<script>

function doSubmit()
{
	var msg="";
	var flag=true;
	
	var activityFlag = document.forms[0].activityFlag.value;
	var check_access = document.forms[0].check_access.value;
	var approve_access = document.forms[0].approve_access.value;
	var authorize_access = document.forms[0].authorize_access.value;
	
	var criteri_formula = document.forms[0].criteri_formula.value;
	var changed_qty_mmbtu = parseFloat(document.forms[0].changed_qty_mmbtu.value).toFixed(2);
	var new_qty_mmbtu = document.forms[0].new_qty_mmbtu.value;
	var main_qty_mmbtu = document.forms[0].main_qty_mmbtu.value;
	
	if(activityFlag=="APPROVE")
	{
		if(approve_access=="N")
		{
			msg+="You don't have Approval Rights!\n";
			flag=false;
		}
		
		
		/* var new_applicable_abbr = document.forms[0].new_applicable_abbr.value;
		if(document.forms[0].rd[0].checked && new_applicable_abbr=="TDS")
		{
			var tds_amt = document.forms[0].tds_amt.value;
			var new_tds_amt = document.forms[0].new_tds_amt.value;
			var tds_gross_amt = document.getElementById('tds_gross_amt').value;
			var recalc_tds_amt = document.getElementById('recalc_tds_amt').value;
			var tds_rd=document.forms[0].tds_rd;
		
			if(tds_rd[0].checked && tds_gross_amt.trim()=="")
			{
				msg+="Please Enter Gross Amount for TDS Refinement!\n";
				flag=false;
			}
			
			if(tds_rd[0].checked && recalc_tds_amt==tds_amt)
			{
				msg+="Recalculated|Refined TDS should be different from Saved TDS Value!\n";
				flag=false;
			}
		} */
	}
	else if(activityFlag=="CHECK")
	{
		if(check_access=="N")
		{
			msg+="You don't have Check Rights!\n";
			flag=false;
		}
	}
	else if(activityFlag=="AUTHORIZE")
	{
		if(check_access=="N")
		{
			msg+="You don't have Authorize Rights!\n";
			flag=false;
		}
	}
	
	if(flag)
	{
		var activity_nm=""
		var invNo="";
		
		var changeFlag=true;
		if(document.forms[0].rd[0].checked && criteri_formula.includes("QTY"))
		{
			if(main_qty_mmbtu == new_qty_mmbtu)
			{
				alert("Invoice Qty : "+main_qty_mmbtu+"\nSaved CR/DR Qty : "+new_qty_mmbtu+
						"\n\nCR/DR Criteria - Change in Quantity - is Selected!"+
						"\nNO Quantity Change detected in System!"+
						"\n\nApprove Blocked!");
				document.forms[0].rd[0].checked=false;
				changeFlag=false;
			}
			else
			{
				if(changed_qty_mmbtu != new_qty_mmbtu)
				{
					alert("System Allocated Qty : "+changed_qty_mmbtu+"\nSaved CR/DR Qty : "+new_qty_mmbtu+
							"\n\nCR/DR Criteria - Change in Quantity - is Selected!"+
							"\nChange in Quantity is not aligned with System Allocated Quantity!"+
							"\n\nApprove Blocked!"+
							"\n\nUse Modify option to Re-Submit this CR/DR Note with updated Quantity.");
					document.forms[0].rd[0].checked=false;
					changeFlag=false;
				}
			}
		}
		
		if(changeFlag)
		{
		
			if(document.forms[0].rd[0].checked)
			{
				if(activityFlag=="APPROVE")
				{
					activity_nm="Approve"
				}
				else if(activityFlag=="CHECK")
				{
					activity_nm="Check"
				}
				else if(activityFlag=="AUTHORIZE")
				{
					activity_nm="Authorize"
				}
				
				invNo+="Do you want to "+activity_nm+" Purchase Invoice?"
			}
			else if(document.forms[0].rd[1].checked)
			{
				activity_nm="Reject"
				invNo+="Do you want to "+activity_nm+" Purchase Invoice?\n\n"	
			}
			var a=confirm(invNo);
			if(a)
			{
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].submit();
			}
			else
			{
				document.forms[0].rd[0].checked=false;
				document.forms[0].rd[1].checked=false;
			}
		}
	}
	else
	{
		alert(msg);
		document.forms[0].rd[0].checked=false;
		document.forms[0].rd[1].checked=false;
	}
}

function Do_close(msg,msg_type,accroid)
{
	window.opener.refershPar(msg,msg_type,accroid);
	window.close();
}

function enableTDS(obj)
{
	var tds_amt = document.forms[0].tds_amt.value;
	var new_tds_amt = document.forms[0].new_tds_amt.value;
	
	if(obj.value=="Y")
	{
		document.getElementById("tds_input_div").style.display="";
		
		document.forms[0].isTDSalrted.value="Y";
	}
	else
	{
		document.getElementById("tds_input_div").style.display="none";
		
		//document.forms[0].isTDSalrted.value="N";
	}
	
	document.getElementById('tds_gross_amt').value="";
	document.getElementById('recalc_tds_amt').value="";
	document.getElementById('upt_val_id').innerHTML=document.forms[0].new_tds_amt.value;
	
	document.forms[0].final_tds_amt.value=document.forms[0].new_tds_amt.value;
}

function calcTDS()
{
	var gross1 = document.getElementById('tds_gross_amt').value;
	if(gross1!="")
	{
		var tds_factor=document.forms[0].new_tds_factor.value;
		var tdsAmt="";
		if(tds_factor != "")
		{
			//tdsAmt=((parseFloat(gross1)*parseFloat(tds_factor))/100).toFixed(2)
			
			tdsAmt = (Math.round(gross1 * tds_factor) / 100).toFixed(2);
		}
		
		document.forms[0].recalc_tds_amt.value=tdsAmt;
		
		document.getElementById('upt_val_id').innerHTML=tdsAmt;
		
		document.forms[0].final_tds_amt.value=tdsAmt;
	}
	else
	{
		document.getElementById('upt_val_id').innerHTML=document.forms[0].new_tds_amt.value;
		
		document.forms[0].final_tds_amt.value=document.forms[0].new_tds_amt.value;
	}
}

var newWindow_att1;
function openAtt1(url)
{
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

var newWindow_att2;
function openAtt2(url)
{
	if(!newWindow_att2 || newWindow_att2.closed)
	{
		newWindow_att2 = window.open(url,"Attachment2","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow_att2.close();
		newWindow_att2 = window.open(url,"Attachment2","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.remittance.DataBean_Remittance" id="pur_inv" scope="request"></jsp:useBean>
<%

String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
String operation=request.getParameter("operation")==null?"INSERT":request.getParameter("operation");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String sel_inv_no = request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
String invoice_type = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String crdr_gen_type = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
String sgpg_type = request.getParameter("sgpg_type")==null?"":request.getParameter("sgpg_type");
String crdr_no = request.getParameter("crdr_no")==null?"":request.getParameter("crdr_no");
String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String activityFlag = request.getParameter("activityFlag")==null?"":request.getParameter("activityFlag");


String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

pur_inv.setCallFlag("CRDR_CHK_APRV_DTL");
pur_inv.setComp_cd(owner_cd);
pur_inv.setCounterparty_cd(counterparty_cd);
pur_inv.setSel_inv_no(sel_inv_no);
pur_inv.setOperation(operation);
pur_inv.setSgpg_type(sgpg_type);
pur_inv.setFinancial_year(financial_year);
pur_inv.setInvoice_seq(invoice_seq);
pur_inv.setCrdr_gen_type(crdr_gen_type);
pur_inv.setActivityFlag(activityFlag);
pur_inv.setCrdr_inv_no(crdr_no);
pur_inv.init();

Vector VCOUNTERPARTY_CD = pur_inv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = pur_inv.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = pur_inv.getVCOUNTERPARTY_ABBR();

Vector VINVOICE_NO_LIST = pur_inv.getVINVOICE_NO_LIST();
Vector VCRITERIA_FLAG = pur_inv.getVCRITERIA_FLAG();
Vector VCRITERIA_NAME = pur_inv.getVCRITERIA_NAME();
Vector VCRITERIA_HIDE = pur_inv.getVCRITERIA_HIDE();

Vector VCONTACT_PERSON = pur_inv.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = pur_inv.getVCONTACT_PERSON_CD();
Vector VBU_CONTACT_PERSON = pur_inv.getVBU_CONTACT_PERSON();
Vector VBU_CONTACT_PERSON_CD = pur_inv.getVBU_CONTACT_PERSON_CD();

String criteri_formula = pur_inv.getCriteri_formula();

String couterpty_abbr=pur_inv.getCouterpty_abbr();
String couterpty_nm=pur_inv.getCouterpty_nm();
String cont_no = pur_inv.getCont_no();
String cont_rev_no = pur_inv.getCont_rev_no();
String agmt_no = pur_inv.getAgmt_no();
String agmt_rev_no = pur_inv.getAgmt_rev_no();
String cargo_no = pur_inv.getCargo_no();
String contract_type = pur_inv.getContract_type();
String deal_no=pur_inv.getDeal_no();
String contract_ref=pur_inv.getContract_ref();
plant_seq=pur_inv.getPlant_seq();
String plant_abbr=pur_inv.getPlant_abbr();
bu_unit=pur_inv.getBu_unit();
String bu_plant_abbr=pur_inv.getBu_plant_abbr();
String period_start_dt=pur_inv.getPeriod_start_dt();
String period_end_dt=pur_inv.getPeriod_end_dt();
String bu_contact_person_cd = pur_inv.getBu_contact_person_cd();
String contact_person_cd = pur_inv.getContact_person_cd();

String invoice_raised_in = pur_inv.getInvoice_raised_in();
String invoice_raised_in_nm = pur_inv.getInvoice_raised_in_nm();
String price_cd=pur_inv.getPrice_cd();
String price_cd_nm=pur_inv.getPrice_cd_nm();
String exchng_rate_cd=pur_inv.getExchng_rate_cd();
String exchang_rate_dt=pur_inv.getExchang_rate_dt();

String billing_freq = pur_inv.getBilling_freq();
String invoice_dt = pur_inv.getInvoice_dt();
String invoice_due_dt = pur_inv.getInvoice_due_dt();
String qty_mmbtu=pur_inv.getQty_mmbtu();
String price=pur_inv.getPrice();
String exchang_rate=pur_inv.getExchang_rate();
String gross_amt=pur_inv.getGross_amt();
String gross_amt1=pur_inv.getGross_amt1();
String transportation_charges = pur_inv.getTransportation_charges();
String transportation_amount = pur_inv.getTransportation_amount();
String marketing_margin = pur_inv.getMarketing_margin();
String marketing_margin_amount = pur_inv.getMarketing_margin_amount();
String other_charges = pur_inv.getOther_charges();
String other_charges_amount = pur_inv.getOther_charges_amount();
String gross_include_transport_tariff = pur_inv.getGross_include_transport_tariff();
String tax_amt = pur_inv.getTax_amt();
String tax_struct_cd=pur_inv.getTax_struct_cd();
String tax_struct_dt=pur_inv.getTax_struct_dt();
String tax_struct_dtl=pur_inv.getTax_struct_dtl();
String tax_info = pur_inv.getTax_info();
String tax_factor = pur_inv.getTax_factor();
String invoice_amt = pur_inv.getInvoice_amt();
String net_payable = pur_inv.getNet_payable();
String invoice_ref = pur_inv.getInvoice_ref();

String applicable_amt = "";//pur_inv.getApplicable_amt();

String applicable_flag = pur_inv.getApplicable_flag();
String applicable_abbr = pur_inv.getApplicable_abbr();
String tcs_factor = pur_inv.getTcs_factor();
String tcs_amount = pur_inv.getTcs_amount();
String tcs_struct_cd=pur_inv.getTcs_struct_cd();
String tcs_struct_dt=pur_inv.getTcs_struct_dt();
String tds_factor = pur_inv.getTds_factor();
String tds_amount = pur_inv.getTds_amount();
String tds_struct_cd=pur_inv.getTds_struct_cd();
String tds_struct_dt=pur_inv.getTds_struct_dt();

String invoice_check_flag = pur_inv.getInvoice_check_flag();
String invoice_check_dt = pur_inv.getInvoice_check_dt();
String invoice_check_by = pur_inv.getInvoice_check_by();
String invoice_check_nm = pur_inv.getInvoice_check_nm();
String invoice_auth_flag = pur_inv.getInvoice_auth_flag();
String invoice_auth_dt = pur_inv.getInvoice_auth_dt();
String invoice_auth_by = pur_inv.getInvoice_auth_by();
String invoice_auth_nm = pur_inv.getInvoice_auth_nm();
String invoice_aprv_flag = pur_inv.getInvoice_aprv_flag();
String invoice_aprv_dt = pur_inv.getInvoice_aprv_dt();
String invoice_aprv_by = pur_inv.getInvoice_aprv_by();
String invoice_aprv_nm = pur_inv.getInvoice_aprv_nm();

String imb_qty="";//pur_inv.getImb_qty();
String imb_amt="";//pur_inv.getImb_amt();
String ship_or_pay_qty="";//pur_inv.getShip_or_pay_qty();
String ship_or_pay_amt="";//pur_inv.getShip_or_pay_amt();
String ovrun_qty="";//pur_inv.getOvrun_qty();
String ovrun_amt="";//pur_inv.getOvrun_amt();
String att_file_name="";//pur_inv.getAtt_file_name();

String main_invoice_dt =pur_inv.getMain_invoice_dt();
String main_invoice_due_dt =pur_inv.getMain_invoice_due_dt();
String main_qty_mmbtu=pur_inv.getMain_qty_mmbtu();
String main_price=pur_inv.getMain_price();
String main_exchang_rate=pur_inv.getMain_exchang_rate();
String main_gross_amt=pur_inv.getMain_gross_amt();
String main_gross_amt1=pur_inv.getMain_gross_amt1();
String main_transportation_charges =pur_inv.getMain_transportation_charges();
String main_transportation_amount =pur_inv.getMain_transportation_amount();
String main_marketing_margin =pur_inv.getMain_marketing_margin();
String main_marketing_margin_amount =pur_inv.getMain_marketing_margin_amount();
String main_other_charges =pur_inv.getMain_other_charges();
String main_other_charges_amount =pur_inv.getMain_other_charges_amount();
String main_gross_include_transport_tariff =pur_inv.getMain_gross_include_transport_tariff();
String main_tax_amt =pur_inv.getMain_tax_amt();
String main_tax_struct_cd=pur_inv.getMain_tax_struct_cd();
String main_tax_struct_dt=pur_inv.getMain_tax_struct_dt();
String main_tax_struct_dtl=pur_inv.getMain_tax_struct_dtl();
String main_tax_info ="";//pur_inv.getMain_tax_info();
String main_tax_factor ="";//pur_inv.getMain_tax_factor();
String main_invoice_amt =pur_inv.getMain_invoice_amt();
String main_net_payable =pur_inv.getMain_net_payable();
String main_tds_amount =pur_inv.getMain_tds_amount();
String main_tds_factor =pur_inv.getMain_tds_factor();
String main_tds_struct_cd =pur_inv.getMain_tds_struct_cd();
String main_tds_struct_dt =pur_inv.getMain_tds_struct_dt();
String main_tcs_amount =pur_inv.getMain_tcs_amount();
String main_tcs_factor =pur_inv.getMain_tcs_factor();
String main_tcs_struct_cd =pur_inv.getMain_tcs_struct_cd();
String main_tcs_struct_dt =pur_inv.getMain_tcs_struct_dt();
String main_invoice_ref =pur_inv.getMain_invoice_ref();

String new_qty_mmbtu=pur_inv.getNew_qty_mmbtu();
String new_price=pur_inv.getNew_price();
String new_exchang_rate=pur_inv.getNew_exchang_rate();
String new_gross_amt=pur_inv.getNew_gross_amt();
String new_gross_amt1=pur_inv.getNew_gross_amt1();
String new_transportation_charges =pur_inv.getNew_transportation_charges();
String new_transportation_amount =pur_inv.getNew_transportation_amount();
String new_marketing_margin =pur_inv.getNew_marketing_margin();
String new_marketing_margin_amount =pur_inv.getNew_marketing_margin_amount();
String new_other_charges =pur_inv.getNew_other_charges();
String new_other_charges_amount =pur_inv.getNew_other_charges_amount();
String new_gross_include_transport_tariff =pur_inv.getNew_gross_include_transport_tariff();
String new_tax_amt =pur_inv.getNew_tax_amt();
String new_tax_struct_cd=pur_inv.getNew_tax_struct_cd();
String new_invoice_amt =pur_inv.getNew_invoice_amt();
String new_net_payable =pur_inv.getNew_net_payable();
String new_tds_amount =pur_inv.getNew_tds_amount();
String new_tds_factor =pur_inv.getNew_tds_factor();
String new_tds_struct_cd =pur_inv.getNew_tds_struct_cd();
String new_tds_struct_dt =pur_inv.getNew_tds_struct_dt();
String new_tcs_amount =pur_inv.getNew_tcs_amount();
String new_tcs_factor =pur_inv.getNew_tcs_factor();
String new_tcs_struct_cd =pur_inv.getNew_tcs_struct_cd();
String new_tcs_struct_dt =pur_inv.getNew_tcs_struct_dt();

Vector VPDF_COL1 = pur_inv.getVPDF_COL1();
Vector VPDF_COL2 = pur_inv.getVPDF_COL2();
Vector VPDF_COL3 = pur_inv.getVPDF_COL3();
Vector VPDF_COL4 = pur_inv.getVPDF_COL4();
Vector VPDF_COL5 = pur_inv.getVPDF_COL5();
Vector VPDF_COL6 = pur_inv.getVPDF_COL6();
Vector VPDF_COL7 = pur_inv.getVPDF_COL7();

String changed_qty_mmbtu =pur_inv.getChanged_qty_mmbtu();
String remark_1=pur_inv.getRemark_1();
sgpg_type=pur_inv.getSgpg_type();

String bu_contact_person_nm = pur_inv.getBu_contact_person_nm();
String contact_person_nm = pur_inv.getContact_person_nm();
String plantAddress=pur_inv.getPlantAddress();
String plantCity=pur_inv.getPlantCity();
String plantState=pur_inv.getPlantState();
String plantPin=pur_inv.getPlantPin();
String plantNm=pur_inv.getPlantNm();

String bu_plantAddress=pur_inv.getBu_plantAddress();
String bu_plantCity=pur_inv.getBu_plantCity();
String bu_plantState=pur_inv.getBu_plantState();
String bu_plantPin=pur_inv.getBu_plantPin();
String bu_plantNm=pur_inv.getBu_plantNm();

String bu_tax_info=pur_inv.getBu_tax_info();
String activity_value=pur_inv.getActivity_value();
String info=pur_inv.getInfo();

String invTypeNm="";
if(invoice_type.equals("CR"))
{
	invTypeNm="Credit Note";
}
else if(invoice_type.equals("DR"))
{
	invTypeNm="Debit Note";
}

boolean isMainGrossIncTriff = pur_inv.getIsMainGrossIncTriff();
boolean isGrossIncTriff = pur_inv.getIsGrossIncTriff();
boolean isNewGrossIncTriff = pur_inv.getIsNewGrossIncTriff();
Vector VMULTI_TAX_STRUCT = pur_inv.getVMULTI_TAX_STRUCT();
Vector VMAIN_MULTI_TAX_STRUCT = pur_inv.getVMAIN_MULTI_TAX_STRUCT();
Vector VNEW_MULTI_TAX_STRUCT = pur_inv.getVNEW_MULTI_TAX_STRUCT();

%>
<body <%if(!msg.equals("")){ %>onload="Do_close('<%=msg%>','<%=msg_type%>','<%=accroid%>');"<%} %>>

<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_Remittance">
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="2" face="Arial">
					<b>Remittance Advise</b>
					<br>
				</font>
				<font size="4" face="Arial">
					<b><font style="font-size: 18px;"><%=owner_nm%></font></b>
					<br>
					<b><%=invTypeNm %></b>
					<br>
					<%=info %>
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
								<font size="1.5px" face="Arial"><b><%=invTypeNm %> Date:&nbsp;</b></font>
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
								<font size="1.5px" face="Arial"><b><%=invTypeNm%> No:&nbsp;</b></font>
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
								<font size="1.5px" face="Arial"><b><%=crdr_no%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
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
								<font size="1.5px" face="Arial"><b><%=invoice_ref%>&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=main_invoice_ref%>&nbsp;</b></font>
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
				<font size="1.5px" face="Arial"><b>Billing Period</b></font>
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
								<font size="1.5px" face="Arial"><b><%=period_start_dt%>&nbsp;</b></font>
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
								<font size="1.5px" face="Arial"><b><%=period_end_dt%>&nbsp;</b></font>
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
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Attachment<br>Reference</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Currency</b></font></div></td>
		<td width="15%"><div align="center"><font size="1.5px" face="Arial"><b>Quantity<br>(MMBTU)</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Rate</b></font></div></td>
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
				<font size="1.5px" face="Arial" color="blue"><%=VPDF_COL3.elementAt(i) %></font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=VPDF_COL4.elementAt(i) %></font>
			</div>
		</td>
		
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VPDF_COL5.elementAt(i) %></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VPDF_COL6.elementAt(i) %></font>
			</div>
		</td>
		<td>
			<div <%if(Double.parseDouble(""+VPDF_COL1.elementAt(i))/(int)Double.parseDouble(""+VPDF_COL1.elementAt(i))>1){%>align="left"<%}else{%>align="right"<%} %>>
				<font size="1.5px" face="Arial"><%=VPDF_COL7.elementAt(i) %></font>
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
				<%=remark_1%>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="1.5px" face="Arial"><b>*** This is Computer Generated Report and No Signature Required ***</b></font>
			</div>
		</td>
	</tr>
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
</table>
<%--<%if(activityFlag.equals("APPROVE")) {%>
	<%if(new_applicable_abbr.equals("TDS")) {%>
		<table border="1" width="75%" align="center" cellpadding="2" cellspacing="0">
			<tr valign="middle" <%if(activity_value.equals("Y")) {%>style="display: none;"<%} %>>
				<td>
					<div align="center">
						<b>TDS/TCS Applicable : <%=new_applicable_abbr%><%if(new_tds_struct_cd.equals("")) {%><br/><br/><font color="red">(TDS is applicable for this Invoice. Please configure TDS factor and re-try Approval process)</font><%} %></b><br/><br/>
						TDS Recalculated : <b><%=new_tds_amt%> INR</b> (Submitted TDS Value : <b><%=tds_amt%> INR</b>)
						<br/><br/>
						Refine TDS : <input type="radio" name="tds_rd" value="Y" onClick="enableTDS(this);">&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="tds_rd" value="N" onClick="enableTDS(this);" checked>&nbsp;<b>No</b>
						<br/><br/>
						<div style="display: none;" id="tds_input_div">
							Enter Gross Amount : <input type="text" name="tds_gross_amt" id="tds_gross_amt" onblur="calcTDS();" maxlength="13">&nbsp;&nbsp;&nbsp;
							Calculated TDS :<input type="text" name="recalc_tds_amt" id="recalc_tds_amt" style="background: #e9ecef;" readOnly>
							<br/><br/>
						</div>
						On Approval TDS value will be updated as <font id="upt_val_id"><%=new_tds_amt%></font> INR
					</div>
				</td>
			</tr>
			<tr valign="middle" <%if(!activity_value.equals("Y")) {%>style="display: none;"<%} %>>
				<td>
					<div align="center">
						<b>TDS/TCS Applicable : <%=applicable_abbr%></b><br/><br/>
						Submitted TDS Value : <b><%=tds_amt%> INR</b>
					</div>
				</td>
			</tr>
		</table>
	<%}else{ %>
		<table border="1" width="75%" align="center" cellpadding="2" cellspacing="0">
			<tr valign="middle">
				<td>
					<div align="center"><b>TDS/TCS Applicable : <%=new_applicable_abbr%></b></div>
				</td>
			</tr>
		</table>
	<%} %>
<%} %> --%>
<table border="0" width="100%" align="center" cellpadding="2" cellspacing="0">
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
			<%if(activityFlag.equals("CHECK") || activityFlag.equals("AUTHORIZE")){ %>&nbsp;
				<% if(activityFlag.equals("CHECK")){%>Checked<%}else if(activityFlag.equals("AUTHORIZE")){ %>Authorized<%} %> OK:&nbsp;
				<input type="radio" name="rd" value="Y" onClick="doSubmit();" <%if(activity_value.equals("Y")){ %>checked<%} %>>&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="radio" name="rd" value="N" onClick="doSubmit();" <%if(activity_value.equals("N")){ %>checked<%} %>>&nbsp;<b>No</b>
			<%}else if(activityFlag.equals("APPROVE")){ %>
				<% if(activityFlag.equals("APPROVE")){ %>Approved<%} %> OK:&nbsp;
				<input type="radio" name="rd" value="A" onClick="doSubmit();" <%if(activity_value.equals("A")){ %>checked<%} %>>&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="radio" name="rd" value="R" onClick="doSubmit();" <%if(activity_value.equals("R")){ %>checked<%} %>>&nbsp;<b>No</b>
			<%} %>
			</div>
		</td>
	</tr>
</table>

<input type="hidden" name="option" value="PURCHASE_CRDR_CHK_APRV">
<input type="hidden" name="activityFlag" value="<%=activityFlag%>">
<input type="hidden" name="opration" value="<%=activityFlag%>">
<input type="hidden" name="operation" value="<%=activityFlag%>">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cargo_no" value="<%=cargo_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="plant_seq" value="<%=plant_seq%>">
<input type="hidden" name="period_start_dt" value="<%=period_start_dt%>">
<input type="hidden" name="period_end_dt" value="<%=period_end_dt%>">
<input type="hidden" name="bu_unit" value="<%=bu_unit%>">
<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
<input type="hidden" name="invoice_raised_in" value="<%=invoice_raised_in%>">
<input type="hidden" name="inv_flag" value="<%=crdr_gen_type%>">
<input type="hidden" name="crdr_no" value="<%=crdr_no%>">
<input type="hidden" name="sel_inv_no" value="<%=sel_inv_no%>">
<input type="hidden" name="sgpg_type" value="<%=sgpg_type%>">
<input type="hidden" name="criteri_formula" value="<%=criteri_formula%>">
<input type="hidden" name="changed_qty_mmbtu" value="<%=changed_qty_mmbtu%>">
<input type="hidden" name="new_qty_mmbtu" value="<%=new_qty_mmbtu%>">
<input type="hidden" name="main_qty_mmbtu" value="<%=main_qty_mmbtu%>">

<input type="hidden" name="invoice_dt" value="<%=invoice_dt%>">
<input type="hidden" name="invoice_due_dt" value="<%=invoice_due_dt%>">
<input type="hidden" name="invoice_amt" value="<%=invoice_amt%>">

<input type="hidden" name="accroid" value="<%=accroid%>">
<input type="hidden" name="tds_factor" value="<%=tds_factor%>">
<input type="hidden" name="tds_struct_cd" value="<%=tds_struct_cd%>">
<input type="hidden" name="applicable_abbr" value="<%=applicable_abbr%>">
<input type="hidden" name="new_tds_factor" value="<%=new_tds_factor%>">
<input type="hidden" name="new_tds_struct_cd" value="<%=new_tds_struct_cd%>">
<%-- <input type="hidden" name="final_tds_amt" value="<%=new_tds_amt%>">
<input type="hidden" name="isTDSalrted" value="<%if(!new_tds_amt.equals(tds_amt) && !new_tds_amt.equals("")){%>Y<%}%>"> --%>

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
<input type="hidden" name="u" value="<%=u%>">

</form>
</body>
</html>