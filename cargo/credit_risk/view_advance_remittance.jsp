<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Remittance</title>
<link rel="stylesheet" type="text/css" href="../font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="../css/common.css">

<script>
function refreshParent(gx)
{
	window.close();
	window.opener.refresh(gx);
}
var newWindow;
function printPDF(counterparty_cd,seq_no, seq_rev_no, gx, isReversal)
{
	var url = "pdf_advance_remittance.jsp?counterparty_cd="+counterparty_cd+"&seq_no="+seq_no+
		"&seq_rev_no="+seq_rev_no+"&gx="+gx+"&isReversal="+isReversal;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Advance Remittance PDF","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Advance Remittance PDF","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DataBean_CreditRisk" id="cr_inv" scope="request"></jsp:useBean>
<%
String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String seq_no=request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
String seq_rev_no=request.getParameter("seq_rev_no")==null?"":request.getParameter("seq_rev_no");
String gx=request.getParameter("gx")==null?"":request.getParameter("gx");
String isReversal=request.getParameter("isReversal")==null?"":request.getParameter("isReversal");


cr_inv.setCallFlag("VIEW_REMITTANCE");
cr_inv.setCounterparty_cd(counterparty_cd);
cr_inv.setSeq_no(seq_no);
cr_inv.setSeq_rev_no(seq_rev_no);
cr_inv.setGx(gx);
cr_inv.setIsReversal(isReversal);
cr_inv.setComp_cd(owner_cd);
cr_inv.init();

String counterparty_nm = cr_inv.getCounterparty_nm();
String gx_counterparty_nm = cr_inv.getGx_counterparty_nm();
String received_dt = cr_inv.getReceived_date();
String value = cr_inv.getValue();
String currency = cr_inv.getCurrency();
String pg_ref = cr_inv.getPg_ref();
String contRef = cr_inv.getContRef();
String signing_dt = cr_inv.getSigning_dt();
String remittance_no = cr_inv.getRemittance_no();
String status = cr_inv.getStatus();


String plantAddress=cr_inv.getPlantAddress();
String plantCity=cr_inv.getPlantCity();
String plantState=cr_inv.getPlantState();
String plantPin=cr_inv.getPlantPin();
String plantNm=cr_inv.getPlantNm();

String bu_plantAddress=cr_inv.getBu_plantAddress();
String bu_plantCity=cr_inv.getBu_plantCity();
String bu_plantState=cr_inv.getBu_plantState();
String bu_plantPin=cr_inv.getBu_plantPin();
String bu_plantNm=cr_inv.getBu_plantNm();

String tax_info=cr_inv.getTax_info();
String bu_tax_info=cr_inv.getBu_tax_info();
%>
<body>
<%@ include file="../home/loading.jsp"%>
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="4" face="Arial">
					<b><u>REMITTANCE</u></b>
					<br><br>
				</font>				
				<font size="4" face="Arial">
					<b><%=owner_nm%></b>
					<br>
				</font>	
				<font size="2" face="Arial">	
					<b><%if(isReversal.equals("N")){ %>ADVANCE <%} else {%>ADVANCE REVERSAL<%}%> </b>
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
					In respect of Exchange Transaction (<%=counterparty_nm %> - <%=contRef%>) executed on <%=signing_dt%>
					<br>
					between <%=gx_counterparty_nm%> and <%=owner_nm%>
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
					<br><%=gx_counterparty_nm%>
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
								<font size="1.5px" face="Arial"><b>Payment Due Date:&nbsp;</b></font>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b>Invoice#:&nbsp;</b></font>
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
								<font size="1.5px" face="Arial"><b>&nbsp;<%=remittance_no%></b></font>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b>&nbsp;<%=received_dt%></b></font>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								<font size="1.5px" face="Arial"><b>&nbsp;<%=pg_ref%></b></font>
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
		<td width="15%"><div align="center"><font size="1.5px" face="Arial"><b>Amount</b></font></div></td>
	</tr>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial">1</font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial">Gas Purchase AUD</font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><%=currency%></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><%=value%></font>
			</div>
		</td>
	</tr>
	<tr valign="top">
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><b>2</b></font>
			</div>
		</td>
		<td>
			<div align="left">
				<font size="1.5px" face="Arial"><b>Net Amount Payable</b></font>
			</div>
		</td>
		<td>
			<div align="center">
				<font size="1.5px" face="Arial"><b><%=currency%></b></font>
			</div>
		</td>
		<td>
			<div align="right">
				<font size="1.5px" face="Arial"><b><%=value%></b></font>
			</div>
		</td>
	</tr>
</table>
<br>
<br>
<table border="0" width="100%" align="center" cellpadding="2" cellspacing="0">
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="left">
				<font size="1.5px" face="Arial"><b>For <%=owner_nm%><br><br><br><br>Authorised Signatory</b></font>
			</div>
		</td>
	</tr>
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="middle"><td colspan="7"><div align="center">&nbsp;</div></td></tr>
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<%if(status.equals("O")){ %>
					<font style="color: blue;"><I><b>Do you want to Generate Advance Remittance PDF?</b></I></font>&nbsp;
					<input type="radio" name="rd" value="Y" 
					onclick="printPDF('<%=counterparty_cd%>','<%=seq_no%>', '<%=seq_rev_no%>', '<%=gx%>','<%=isReversal%>');">&nbsp;<b>Yes</b>&nbsp;&nbsp;&nbsp;&nbsp;
				<%}else {%>
					<font style="color: red;"><I><b>Post Approval Advance Remittance PDF Generation will be enabled!</b></I></font>
				<%} %>
			</div>
		</td>
	</tr>
</table>
<br>
<br>


	
</body>
</html>