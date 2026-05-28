<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GTA_Remittance" id="remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();


String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"C":request.getParameter("contract_type");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String bu_plant_seq = request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");

String from_dt=request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?"":request.getParameter("to_dt");

String month_nm=utildate.getMonthName(to_dt);

String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

remittance.setCallFlag("ATTACHMENT2");
remittance.setComp_cd(owner_cd);
remittance.setCounterparty_cd(counterparty_cd);
remittance.setContract_type(contract_type);
remittance.setAgmt_no(agmt_no);
remittance.setCont_no(cont_no);
remittance.setPeriod_start_dt(from_dt);
remittance.setPeriod_end_dt(to_dt);
remittance.setBu_unit(bu_plant_seq);
remittance.init();

String deficiency_qty=remittance.getDeficiency_qty();
String contRef=remittance.getContRef();
String mdq_qty=remittance.getMdq_qty();
String transmissionQty=remittance.getTransmissionQty();
String ship_pay_percent=remittance.getShip_pay_percent();
String ship_pay_qty=remittance.getShip_pay_qty();
String counterparty_name=remittance.getCounterparty_name();

String att_note="Note : This is System generated data and might differ for Party generated remittance";
%>
<body>
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td colspan="7">
			<div align="center">
				<font size="4" face="Arial">
					<b><%=counterparty_name%></b>
				</font>
			</div>
		</td>
	</tr>
</table>
<br>
<font size="2" face="Arial"><b>Attachment 2 - GTA Contract&nbsp;(<%=contRef%>) Ship/Pay Charge Report for <%=month_nm%> <%=to_dt.substring(6, to_dt.length()) %></b></font>
<table>
<tr><td>&nbsp;</td></tr>
</table>
<br><br>
<span><font size="1.5px" face="Arial"><b>Transmission Quantity for the Month : </b><%=transmissionQty %> MMBTU</font></span><br>
<span><font size="1.5px" face="Arial"><b>MDQ for the Month : </b><%=mdq_qty%> MMBTU</font></span><br>
<span><font size="1.5px" face="Arial"><b>Ship-or-Pay Percentage : </b><%=ship_pay_percent %>%</font></span><br>
<span><font size="1.5px" face="Arial"><b>Ship-or-Pay Quantity : </b><%=ship_pay_qty %> MMBTU</font></span><br>
<span><font size="1.5px" face="Arial"><b>Ship-or-Pay Chargeable Quantity : </b><%=deficiency_qty%> MMBTU</font></span><br>
<br>
<p><font size="1.5px" face="Arial"><b><%=att_note%></b></font></p>
</body>
</html>