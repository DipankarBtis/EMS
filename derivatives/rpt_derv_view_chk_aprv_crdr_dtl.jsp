<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Derivatives Invoice</title>
<link rel="stylesheet" type="text/css" href="../font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="../css/common.css">
<%@ include file="../util/common_val.jsp" %>
<script>
function setInvoiceNo()
{
	var e = document.forms[0].invoice_id_seq;
	var invoiceNo = e.options[e.selectedIndex].text;
	if(document.forms[0].invoice_id_seq.value=="")
	{
		document.forms[0].invoice_no.value="";
	}
	else
	{
		document.forms[0].invoice_no.value=invoiceNo;
	}
}
function doSubmit()
{
	var msg="";
	var flag=true;
	
	var activityFlag = document.forms[0].operation.value;
	var check_access = document.forms[0].check_access.value;
	var approve_access = document.forms[0].approve_access.value;
	var authorize_access = document.forms[0].authorize_access.value;
	var crdr_type = document.forms[0].crdr_type.value;
	var inv_amt = document.forms[0].inv_amt.value;
	var criteri_formula = document.forms[0].criteri_formula.value;
	var crdr_="";
	
	if(crdr_type=="CR")
	{
		inv_type="Credit Note";
	}
	else if(crdr_type=="DR")
	{
		inv_type="Debit Note";
	}
	
	var invoice_no = document.forms[0].invoice_no.value;
	if(activityFlag=="APPROVE")
	{
		if(approve_access=="N")
		{
			msg+="You don't have Approval Rights!\n";
			flag=false;
		}
		else
		{
			var invoice_id_seq = document.forms[0].invoice_id_seq.value;
			
			if(document.forms[0].rd[0].checked)
			{
				if(invoice_id_seq=="")
				{
					msg+="Select Invoice Seq No!\n";
					flag=false;
				}
				if(invoice_no=="")
				{
					msg+="Select Invoice No is Missing!\n";
					flag=false;
				}
			}
		}
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
		if(authorize_access=="N")
		{
			msg+="You don't have Authorize Rights!\n";
			flag=false;
		}
	}
	if(inv_amt=="0.00")
	{
		if(criteri_formula.includes("QTY"))
		{
			msg+="No Quantity Change Detected! Please Modify "+inv_type+" to Check Approve !\n";
			flag=false;
		}
		else if(criteri_formula.includes("FIXEDPRICE"))
		{
			msg+="No Fixed Price Change Detected! Please Modify "+inv_type+" to Check Approve !\n";
			flag=false;
		}
	}
	
	if(flag)
	{
		var activity_nm=""
		var invNo="";
		if(activityFlag=="CHECK")
		{
			if(document.forms[0].rd[0].checked)
			{
				activity_nm="Check"
				invNo+="Do you want to "+activity_nm+" Derivatives "+inv_type+"?"
			}
			else if(document.forms[0].rd[1].checked)
			{
				activity_nm="Reject"
				invNo+="Do you want to "+activity_nm+" Derivatives "+inv_type+"?\n\n"	
			}
		}
		else if(activityFlag=="AUTHORIZE")
		{
			if(document.forms[0].rd[0].checked)
			{
				activity_nm="Authorize"
				invNo+="Do you want to "+activity_nm+" Derivatives "+inv_type+"?"
			}
			else if(document.forms[0].rd[1].checked)
			{
				activity_nm="Reject"
				invNo+="Do you want to "+activity_nm+" Derivatives "+inv_type+"?\n\n"	
			}
		}
		else if(activityFlag=="APPROVE")
		{
			if(document.forms[0].rd[0].checked)
			{
				invNo="Invoice No : "+invoice_no+"\n\n";
				activity_nm="Approve"
				invNo+="Do you want to "+activity_nm+" Derivatives "+inv_type+"?"
			}
			else if(document.forms[0].rd[1].checked)
			{
				activity_nm="Reject"
				invNo+="Do you want to "+activity_nm+" Derivatives "+inv_type+"?\n\n"
				invNo+="Selected Invoice# will be Removed!";
				
				document.forms[0].invoice_id_seq.value="";
				document.forms[0].invoice_no.value="";
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
function Do_close(msg,msg_type,accroid)
{
	window.opener.refershPar(msg,msg_type,accroid);
	window.close();
}

var newWindow_att1;
function openAtt(counterparty_cd,bu_plant_seq,plant_seq,bu_st_cd,invoice_seq,ref_no,fin_yr,crdr_type,month,year)
{
	var u = document.forms[0].u.value;
	var url = "rpt_derv_view_crdr_attachment.jsp?counterparty_cd="+counterparty_cd+"&crdr_type="+crdr_type+"&invoice_seq="+invoice_seq+"&plant_seq="+plant_seq+
	"&financial_year="+fin_yr+"&bu_state_tin="+bu_st_cd+"&sel_inv_no="+ref_no+"&u="+u+"&month="+month+"&year="+year+"&bu_plant_seq="+bu_plant_seq;
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


</script>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Invoice" id="derv_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
String operation=request.getParameter("operation")==null?"INSERT":request.getParameter("operation");
String activityFlag=request.getParameter("activityFlag")==null?"":request.getParameter("activityFlag");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String sel_inv_no = request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String crdr_gen_type = request.getParameter("crdr_gen_type")==null?"":request.getParameter("crdr_gen_type");
String crdr_type = request.getParameter("crdr_type")==null?"":request.getParameter("crdr_type");
String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String bu_plant_seq = request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
if(month.length() == 1)
{
	month="0"+month; 
}

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

derv_inv.setCallFlag("DERV_CRDR_CHK_APRV");
derv_inv.setComp_cd(owner_cd);
derv_inv.setMonth(month);
derv_inv.setYear(year);
derv_inv.setCounterparty_cd(counterparty_cd);
derv_inv.setSel_inv_no(sel_inv_no);
derv_inv.setOperation(operation);
derv_inv.setInvoice_seq(invoice_seq);
derv_inv.setCrdr_type(crdr_type);
derv_inv.setBu_plant_seq(bu_plant_seq);
derv_inv.setPlant_seq(plant_seq);
derv_inv.setBu_state_tin(bu_state_tin);
derv_inv.init();

String comp_name=derv_inv.getComp_name();
String plant_abbr=derv_inv.getPlant_abbr();
String bu_plant_abbr=derv_inv.getBu_plant_abbr();
String counterparty_abbr=derv_inv.getCounterparty_abbr();
String counterparty_nm=derv_inv.getCounterparty_nm();

Vector VCOUNTERPARTY_CD = derv_inv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = derv_inv.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = derv_inv.getVCOUNTERPARTY_ABBR();
Vector VAGMT_TYPE = derv_inv.getVAGMT_TYPE();
Vector VAGMT_NO = derv_inv.getVAGMT_NO();
Vector VAGMT_REV = derv_inv.getVAGMT_REV();
Vector VCONT_TYPE = derv_inv.getVCONT_TYPE();
Vector VCONT_NO = derv_inv.getVCONT_NO();
Vector VCONT_REV = derv_inv.getVCONT_REV();
Vector VCONT_NAME = derv_inv.getVCONT_NAME();
Vector VCONT_REF = derv_inv.getVCONT_REF();
Vector VDEAL_MAPPING = derv_inv.getVDEAL_MAPPING();
Vector VINSTRUMENT_NO = derv_inv.getVINSTRUMENT_NO();
Vector VINSTRUMENT_TYPE = derv_inv.getVINSTRUMENT_TYPE();
Vector VBUY_SELL = derv_inv.getVBUY_SELL();
Vector VCARGO_ARRIVAL_DT = derv_inv.getVCARGO_ARRIVAL_DT();
Vector VBOOKED_MMBTU = derv_inv.getVBOOKED_MMBTU();
Vector VBOOKED_SCM = derv_inv.getVBOOKED_SCM();
Vector VQTY_UNIT = derv_inv.getVQTY_UNIT();
Vector VTRADE_DT = derv_inv.getVTRADE_DT();
Vector VRATE = derv_inv.getVRATE();
Vector VCONT_START_DT = derv_inv.getVCONT_START_DT();
Vector VCONT_END_DT = derv_inv.getVCONT_END_DT();
Vector VDEAL_PRICE_CURVE = derv_inv.getVDEAL_PRICE_CURVE();
Vector VDEAL_PROD_NM = derv_inv.getVDEAL_PROD_NM();
Vector VFLOAT_RATE = derv_inv.getVFLOAT_RATE();
Vector VINVGEN_FLAG = derv_inv.getVINVGEN_FLAG();
Vector VPLANT_SEQ = derv_inv.getVPLANT_SEQ();
Vector VPLANT_ABBR = derv_inv.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = derv_inv.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = derv_inv.getVBU_PLANT_ABBR();
Vector VPRICE_TYPE = derv_inv.getVPRICE_TYPE();
Vector VMST_PLANT_SEQ = derv_inv.getVMST_PLANT_SEQ();
Vector VMST_PLANT_ABBR = derv_inv.getVMST_PLANT_ABBR();
Vector VMST_BU_PLANT_SEQ = derv_inv.getVMST_BU_PLANT_SEQ();
Vector VMST_BU_PLANT_ABBR = derv_inv.getVMST_BU_PLANT_ABBR();
Vector VMST_CURVE_NM = derv_inv.getVMST_CURVE_NM();
Vector VSELL_RATE = derv_inv.getVSELL_RATE();
Vector VSELL_AMT = derv_inv.getVSELL_AMT();
Vector VBUY_RATE = derv_inv.getVBUY_RATE();
Vector VBUY_AMT = derv_inv.getVBUY_AMT();
Vector VINVOICE_AMT = derv_inv.getVINVOICE_AMT();
Vector VINSTRUMENT_DURATION = derv_inv.getVINSTRUMENT_DURATION();
Vector VCURVE_NM = derv_inv.getVCURVE_NM();
Vector VPERIOD_START_DT = derv_inv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = derv_inv.getVPERIOD_END_DT();
Vector VINV_FIXED_PRICE = derv_inv.getVINV_FIXED_PRICE();
Vector VMAIN_BOOKED_MMBTU = derv_inv.getVMAIN_BOOKED_MMBTU();
Vector VNEW_QTY = derv_inv.getVNEW_QTY();
Vector VNEW_FIXED_PRICE = derv_inv.getVNEW_FIXED_PRICE();

String crdr_remark=derv_inv.getCrdr_remark();
String remark2=derv_inv.getRemark2();
String crdr_dt=derv_inv.getCrdr_dt();
String inv_due_dt=derv_inv.getCrdr_due_dt();
String period_start_dt=derv_inv.getPeriod_start_dt();
String period_end_dt=derv_inv.getPeriod_end_dt();
String inv_ref=derv_inv.getInv_ref();
String plantAddress=derv_inv.getPlantAddress();
String plantCity=derv_inv.getPlantCity();
String plantState=derv_inv.getPlantState();
String plantPin=derv_inv.getPlantPin();
String plantNm=derv_inv.getPlantNm();
String tax_info=derv_inv.getTax_info();
String bu_tax_info=derv_inv.getBu_tax_info();
String invoice_no=derv_inv.getInvoice_no();
String invoice_id_seq=derv_inv.getInvoice_id_seq();
String activity_value=derv_inv.getActivity_value();
String total_inv_amt=derv_inv.getTotal_inv_amt();
String invoice_type=derv_inv.getInvoice_type();
String criteri_formula=derv_inv.getCriteri_formula();
financial_year=derv_inv.getFy_year();

String bank_formula=derv_inv.getBank_formula();

if(invoice_type.equals("I") && crdr_type.equals("DR") && (crdr_remark.equals("") || crdr_remark.equals("Please pay the invoiced amount by wire transfer at Bank Name: ")))
{
	crdr_remark="Please pay the invoiced amount by wire transfer at Bank Name: "+bank_formula;
}

String bu_plantAddress=derv_inv.getBu_plantAddress();
String bu_plantCity=derv_inv.getBu_plantCity();
String bu_plantState=derv_inv.getBu_plantState();
String bu_plantPin=derv_inv.getBu_plantPin();
String bu_plantNm=derv_inv.getBu_plantNm();

Vector VINVOICE_NO = derv_inv.getVINVOICE_NO();
Vector VINVOICE_ID_SEQ = derv_inv.getVINVOICE_ID_SEQ();

String crdrTypeNm="";
if(crdr_type.equals("CR"))
{
	crdrTypeNm = "Credit Note";
}
else if(crdr_type.equals("DR"))
{
	crdrTypeNm = "Debit Note";
}

String invTypeNm="";
if(invoice_type.equals("I"))
{
	invTypeNm = "Invoice";
}
else if(invoice_type.equals("R"))
{
	invTypeNm = "Remittance";
}

%>
<body <%if(!msg.equals("")){ %>onload="Do_close('<%=msg%>','<%=msg_type%>','<%=accroid%>');"<%} %> <%if(operation.equals("APPROVE")){ %>onload="setInvoiceNo();"<%} %>>

<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_Derivatives_Invoice">
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font face="Arial" style="font-size: 14px;">
					<%if(invoice_type.equals("I")){ %>
					ORIGINAL
					<%}else{ %>
					Remittance Advice
					<%} %>
					<br>
				</font>
				<font face="Arial" style="font-size: 18px;font-weight: bold;">
					<%=owner_nm%>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font face="Arial" style="font-size: 16px;">
					<%=crdrTypeNm %>
				</font>
			</div>
			<input type="hidden" class="form-control form-control-sm" name="crdr_type" value="<%=crdr_type %>" readonly>
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
					Registered Office:
					<br><%=owner_nm%>
					<br><%=bu_plantAddress%>,<%=bu_plantCity%>
					<br><%=bu_plantState%>&nbsp;-&nbsp;<%=bu_plantPin%>
				</font>
			</div>
		</td>
		<td colspan="1" width="10%"><div align="left"><font size="1.5px" face="Arial"><b></b></font></div></td>
		<td colspan="3" width="40%">
			<div align="left">
				<font size="1.5px" face="Arial">
					To:
					<br><%=counterparty_nm%>
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
								<font size="1.5px" face="Arial"><b><%=crdrTypeNm %> Date:&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=crdrTypeNm %> Due Date:&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<%if(invoice_type.equals("R")){ %>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b>Vendor Invoice No.:&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<%} %>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=owner_abbr%> <%if(invoice_type.equals("I")){%><%=crdrTypeNm %><%}else{%><%=invTypeNm %><%} %> Seq No:&nbsp;</b></font>
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
								<font size="1.5px" face="Arial"><b><%=crdr_dt%>&nbsp;</b></font>
							</div>
							<input type="hidden" class="form-control form-control-sm" name="crdr_dt" value="<%=crdr_dt %>" readonly>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=inv_due_dt%>&nbsp;</b></font>
							</div>
							<input type="hidden" class="form-control form-control-sm" name="due_dt" value="<%=inv_due_dt %>" readonly>
						</td>
					</tr>
					<%if(invoice_type.equals("R")){ %>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=inv_ref%>&nbsp;</b></font>
							</div>
							<input type="hidden" class="form-control form-control-sm" name="ref_no" value="<%=inv_ref %>" readonly>
						</td>
					</tr>
					<%} %>
					<tr>
						<td>
						<%if(operation.equals("APPROVE")){ %>
							<select name="invoice_id_seq" style="background:yellow;font-weight:bold;" onchange="setInvoiceNo();">
								<option value="">--Select--</option>
								<%for(int i=0; i<VINVOICE_ID_SEQ.size();i++){ %>
								<option value="<%=VINVOICE_ID_SEQ.elementAt(i)%>"><%=VINVOICE_NO.elementAt(i)%></option>
								<%} %>
							</select>
							<script>document.forms[0].invoice_id_seq.value="<%=invoice_id_seq%>"</script>
						<%}else{ %>
							<div align="right">
								<font size="1.5px" face="Arial"><b><%=invoice_no%>&nbsp;</b></font>
							</div>
						<%} %>
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
	<tr valign="middle"><td colspan="7">&nbsp;</td></tr>
</table>
<table width="100%"  border="1" align="center" cellpadding="2" cellspacing="0">
	<tr valign="bottom">
		<td width="5%"><div align="center"><font size="1.5px" face="Arial"><b>Sr.No.</b></font></div></td>
		<td width="8%"><div align="center"><font size="1.5px" face="Arial"><b>Trade Date</b></font></div></td>
		<td width="15%"><div align="center"><font size="1.5px" face="Arial"><b>Deal No.</b></font></div></td>
		<td width="8%"><div align="center"><font size="1.5px" face="Arial"><b>Item</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Index</b></font></div></td>
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Trade Term</b></font></div></td>
		<td width="8%"><div align="center"><font size="1.5px" face="Arial"><b>Attachment Reference</b></font></div></td>
		<!-- <td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Quantity</b></font></div></td>
		<td width="7%"><div align="center"><font size="1.5px" face="Arial"><b>Rate(BUY)</b></font></div></td>
		<td width="7%"><div align="center"><font size="1.5px" face="Arial"><b>Rate(SELL)</b></font></div></td> -->
		<td width="10%"><div align="center"><font size="1.5px" face="Arial"><b>Total Amount (USD)</b></font></div></td>
	</tr>
	<%int k=0;
	for(int i=0; i<VDEAL_MAPPING.size(); i++){ 
	k+=1;%>
	<tr>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=k %></font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=VTRADE_DT.elementAt(i) %></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial"><%=VDEAL_MAPPING.elementAt(i) %> (<%=VCONT_REF.elementAt(i) %>)</font>
			</div>
			<input type="hidden" class="form-check-input" name="agmtNo" id="agmtNo<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>">
			<input type="hidden" class="form-check-input" name="agmtRev" id="agmtRev<%=i%>" value="<%=VAGMT_REV.elementAt(i)%>">
			<input type="hidden" class="form-check-input" name="agmtType" id="agmtType<%=i%>" value="<%=VAGMT_TYPE.elementAt(i)%>">
			<input type="hidden" class="form-check-input" name="contNo" id="contNo<%=i%>" value="<%=VCONT_NO.elementAt(i)%>">
			<input type="hidden" class="form-check-input" name="contRev" id="contRev<%=i%>" value="<%=VCONT_REV.elementAt(i)%>">
			<input type="hidden" class="form-check-input" name="contType" id="contType<%=i%>" value="<%=VCONT_TYPE.elementAt(i)%>">
			<input type="hidden" class="form-check-input" name="instrumentNo" id="instrumentNo<%=i%>" value="<%=VINSTRUMENT_NO.elementAt(i)%>">
			<input type="hidden" class="form-check-input" name="buySell" id="buySell<%=i%>" value="<%=VBUY_SELL.elementAt(i)%>">
			<input type="hidden" class="form-check-input" name="cont_ref" id="cont_ref<%=i%>" value="<%=VCONT_REF.elementAt(i)%>">
			<input type="hidden" class="form-check-input" name="frm_dt" id="frm_dt<%=i%>" value="<%=VPERIOD_START_DT.elementAt(i)%>">
			<input type="hidden" class="form-check-input" name="to_dt" id="to_dt<%=i%>" value="<%=VPERIOD_END_DT.elementAt(i)%>">
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=VINSTRUMENT_TYPE.elementAt(i)%></font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=VCURVE_NM.elementAt(i)%></font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=VINSTRUMENT_DURATION.elementAt(i)%></font>
			</div>
		</td>
		<%-- <td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VBOOKED_MMBTU.elementAt(i)%> <%=VQTY_UNIT.elementAt(i) %></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VBUY_RATE.elementAt(i)%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VSELL_RATE.elementAt(i)%></font>
			</div>
		</td> --%>
		 <% if(i == 0){ %>
	        <td rowspan="<%=VDEAL_MAPPING.size()%>" align="center" valign="middle">
	            <font size="1.5px" face="Arial" color="blue">
				<a onclick="openAtt('<%=counterparty_cd%>','<%=bu_plant_seq%>','<%=plant_seq%>','<%=bu_state_tin%>','<%=invoice_seq%>','<%=sel_inv_no%>'
													,'<%=financial_year%>','<%=crdr_type%>','<%=month%>','<%=year%>')">
													
				Att 1</a>
			</font>
	        </td>
	    <% } %>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=VINVOICE_AMT.elementAt(i)%></font>
			</div>
		</td>
	</tr>
	<%} %>
	<tr>
		<%-- <td colspan="8"></td>
		<td align="center">
			<font size="1.5px" face="Arial" color="blue">
				<a onclick="openAtt('<%=counterparty_cd%>','<%=bu_plant_seq%>','<%=plant_seq%>','<%=bu_state_tin%>','<%=invoice_seq%>','<%=sel_inv_no%>'
													,'<%=financial_year%>','<%=crdr_type%>','<%=month%>','<%=year%>')">
													
				Att</a>
			</font>
		</td> --%>
		<td colspan="7">
			<div align="right">
				<font size="1.5px" face="Arial"><b>Total Invoice Amount(USD): </b></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=total_inv_amt%></font>
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
				<%=crdr_remark%>
				</font>
			</div>
		</td>
	</tr>
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<%if(invoice_type.equals("I")){ %>
	<tr valign="middle">
		<td colspan="7">
			<div align="left">
				<font size="1.5px" face="Arial"><b>For <%=owner_nm%><br><br><br><br>Authorised Signatory</b></font>
			</div>
		</td>
	</tr>
	<%}else if(invoice_type.equals("R")){ %>
		<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
		<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
		<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="1.5px" face="Arial">***This is Computer Generated Report And No Signature Required***</font>
			</div>
		</td>
	</tr>
	<%} %>
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
			<%if(operation.equals("CHECK") || operation.equals("APPROVE") || operation.equals("AUTHORIZE")){ %>
				<% if(operation.equals("CHECK")){%>Checked<%}else if(operation.equals("AUTHORIZE")){ %>Authorized<%}else if(operation.equals("APPROVE")){ %>Approved<%} %> OK:&nbsp;
				<input type="radio" name="rd" value="Y" onClick="doSubmit();" <%if(activity_value.equals("Y")){ %>checked<%} %>>&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="radio" name="rd" value="N" onClick="doSubmit();" <%if(activity_value.equals("N")){ %>checked<%} %>>&nbsp;<b>No</b>
			<%} %>
			</div>
		</td>
	</tr>
</table>

<input type="hidden" name="option" value="DERV_CRDR_INVOICE_APPROVAL">
<input type="hidden" name="operation" value="<%=operation%>">
<input type="hidden" name="activityFlag" value="<%=activityFlag%>">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<%-- <input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" value="<%=agmt_rev%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev" value="<%=cont_rev%>">
<input type="hidden" name="instrument_no" value="<%=instrument_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>"> --%>
<input type="hidden" name="plant_seq" value="<%=plant_seq%>">
<input type="hidden" name="period_start_dt" value="<%=period_start_dt%>">
<input type="hidden" name="period_end_dt" value="<%=period_end_dt%>">
<%-- <input type="hidden" name="billing_cycle" value="<%=billing_cycle%>"> --%>
<input type="hidden" name="bu_plant_seq" value="<%=bu_plant_seq%>">
<input type="hidden" name="bu_state_tin" value="<%=bu_state_tin%>">
<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
<input type="hidden" name="inv_amt" value="<%=total_inv_amt%>">
<input type="hidden" name="invoice_type" value="<%=invoice_type%>">
<input type="hidden" name="criteri_formula" value="<%=criteri_formula%>">

<input type="hidden" name="invoice_no" value="<%=invoice_no%>">
<input type="hidden" name="sel_inv_no" value="<%=sel_inv_no%>">
<%-- <input type="hidden" name="invoice_dt" value="<%=inv_dt%>">
<input type="hidden" name="invoice_due_dt" value="<%=inv_due_dt%>"> --%>

<input type="hidden" name="accroid" value="<%=accroid%>">

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