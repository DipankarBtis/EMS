<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GtaMaster" id="gta" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String customer_cd = request.getParameter("customer_cd")==null?"0":request.getParameter("customer_cd");
String contract_type=request.getParameter("contract_type")==null?"C":request.getParameter("contract_type");
String cont_map_id=request.getParameter("cont_map_id")==null?"":request.getParameter("cont_map_id");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");

String from_dt=request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?"":request.getParameter("to_dt");

String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

gta.setCallFlag("GTC_IMBALANCE");
gta.setComp_cd(owner_cd);
gta.setCounterparty_cd(counterparty_cd);
gta.setContract_type(contract_type);
gta.setAgmt_no(agmt_no);
gta.setAgmt_rev_no(agmt_rev_no);
gta.setCont_no(cont_no);
gta.setCont_rev_no(cont_rev_no);
gta.setFrom_dt(from_dt);
gta.setTo_dt(to_dt);
gta.init();

Vector VCOUNTERPARTY_CD = gta.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = gta.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = gta.getVCOUNTERPARTY_ABBR();

Vector VCUSTOMER_CD = gta.getVCUSTOMER_CD();
Vector VCUSTOMER_NM = gta.getVCUSTOMER_NM();
Vector VCUSTOMER_ABBR = gta.getVCUSTOMER_ABBR();

String cont_ref_no = gta.getCont_ref_no();
String start_dt = gta.getStart_dt();
String end_dt = gta.getEnd_dt();
String entry_pt_mapping_id =gta.getEntry_pt_mapping_id();
String exit_pt_mapping_id =gta.getExit_pt_mapping_id();
String entry_pt_nm = gta.getEntry_pt_nm();
String exit_pt_nm = gta.getExit_pt_nm();
String linked_sales_cont_map=gta.getLinked_sales_cont_map();
String bu_plant_seq=gta.getBu_plant_seq();
String sip_pay_freq=gta.getSip_pay_freq();
String counterparty_name=gta.getCounterparty_name();

String cont_name="";
String cont_mapping=contract_type+agmt_no+"-"+agmt_rev_no+"-"+cont_no+"-"+cont_rev_no;

if(!cont_ref_no.equals(""))
{
	cont_name=cont_mapping+" ("+cont_ref_no+") ["+start_dt+" - "+end_dt+"]";
}

if(from_dt.equals(""))
{
	from_dt=gta.getFrom_dt();
}
if(to_dt.equals(""))
{
	to_dt=gta.getTo_dt();
}

Vector VGAS_DT = gta.getVGAS_DT();
Vector VMDQ = gta.getVMDQ();
Vector VNOM_ENTRY_QTY = gta.getVNOM_ENTRY_QTY();
Vector VNOM_EXIT_QTY = gta.getVNOM_EXIT_QTY();
Vector VSCH_ENTRY_QTY = gta.getVSCH_ENTRY_QTY();
Vector VSCH_EXIT_QTY = gta.getVSCH_EXIT_QTY();
Vector VALLOC_ENTRY_QTY = gta.getVALLOC_ENTRY_QTY();
Vector VALLOC_EXIT_QTY = gta.getVALLOC_EXIT_QTY();

Vector VTRANSMISSION_QTY = gta.getVTRANSMISSION_QTY();
Vector VDAILY_IMBALANCE = gta.getVDAILY_IMBALANCE();
Vector VADJ_IMBALANCE = gta.getVADJ_IMBALANCE();
Vector VCUMULATIVE_IMBALANCE = gta.getVCUMULATIVE_IMBALANCE();
Vector VDAILY_UNAUTHORIZED_OVERRUN = gta.getVDAILY_UNAUTHORIZED_OVERRUN();
Vector VCHARGEABLE_OVERRUN = gta.getVCHARGEABLE_OVERRUN();
Vector VCHARGEABLE_POSITIVE_IMBALANCE = gta.getVCHARGEABLE_POSITIVE_IMBALANCE();
Vector VCHARGEABLE_NEGETIVE_IMBALANCE = gta.getVCHARGEABLE_NEGETIVE_IMBALANCE();

String total_nom_entry_qty=gta.getTotal_nom_entry_qty();
String total_nom_exit_qty=gta.getTotal_nom_exit_qty();
String total_sch_entry_qty=gta.getTotal_sch_entry_qty();
String total_sch_exit_qty=gta.getTotal_sch_exit_qty();
String total_alloc_entry_qty=gta.getTotal_alloc_entry_qty();
String total_alloc_exit_qty=gta.getTotal_alloc_exit_qty();
String total_var_mdq=gta.getTotal_var_mdq();
String total_transmission_qty=gta.getTotal_transmission_qty();
String total_chargeable_overrun=gta.getTotal_chargeable_overrun();
String total_positive_imbalance=gta.getTotal_positive_imbalance();
String total_negitive_imbalance=gta.getTotal_negitive_imbalance();

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
<font size="2" face="Arial"><b>Attachment 1 - GTA Contract&nbsp;(<%=cont_ref_no%>) Imbalance Report From <%=from_dt%> To <%=to_dt%></b></font>
<table>
<tr><td>&nbsp;</td></tr>
</table>
<table border="1" cellpadding="2" cellspacing="0">
	<tr>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Gas Day</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>CT MDQ<br>(MMBTU)</b></font></div></td>
		<td colspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Transporter Entry Point</b></font></div></td>
		<td colspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Transporter Exit Point</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Transmission<br>(MMBTU)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Daily Imbalance(+/-)<br>(MMBTU)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Adjusted Imbalance<br>(MMBTU)</b></font></div></td>	
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Cumulative Imbalance<br>(MMBTU)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Daily Unautdorized Overrun<br>(MMBTU)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Chargeable Overrun<br>(MMBTU)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Chargeable +Ve Imbalance<br>(MMBTU)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Chargeable -Ve Imbalance<br>(MMBTU)</b></font></div></td>
	</tr>
	<tr>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Nomination<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Scheduling<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Allocation<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Nomination<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Scheduling<br>(MMBTU)</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Allocation<br>(MMBTU)</b></font></div></td>
	</tr>
	<tbody>
		<%for(int i=0; i<VGAS_DT.size(); i++){ %>
		<tr>
			<td align="center"><font size="1.5px" face="Arial">&nbsp;<%=VGAS_DT.elementAt(i)%>&nbsp;</font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VMDQ.elementAt(i) %></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VNOM_ENTRY_QTY.elementAt(i)%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VSCH_ENTRY_QTY.elementAt(i)%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VALLOC_ENTRY_QTY.elementAt(i)%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VNOM_EXIT_QTY.elementAt(i)%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VSCH_EXIT_QTY.elementAt(i) %></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VALLOC_EXIT_QTY.elementAt(i) %></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VTRANSMISSION_QTY.elementAt(i)%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VDAILY_IMBALANCE.elementAt(i) %></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VADJ_IMBALANCE.elementAt(i) %></font></td>			
			<td align="right"><font size="1.5px" face="Arial"><%=VCUMULATIVE_IMBALANCE.elementAt(i) %></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VDAILY_UNAUTHORIZED_OVERRUN.elementAt(i) %></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VCHARGEABLE_OVERRUN.elementAt(i)%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VCHARGEABLE_POSITIVE_IMBALANCE.elementAt(i)%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=VCHARGEABLE_NEGETIVE_IMBALANCE.elementAt(i)%></font></td>	
		</tr>
		<%} %>
		<tr style="font-weight: bold;">
			<td align="right"><font size="1.5px" face="Arial">Total :</font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=total_var_mdq%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=total_nom_entry_qty%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=total_sch_entry_qty%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=total_alloc_entry_qty%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=total_nom_exit_qty%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=total_sch_exit_qty%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=total_alloc_exit_qty%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=total_transmission_qty%></font></td>
			<td align="right"><font size="1.5px" face="Arial">-</font></td>
			<td align="right"><font size="1.5px" face="Arial">-</font></td>
			<td align="right"><font size="1.5px" face="Arial">-</font></td>
			<td align="right"><font size="1.5px" face="Arial">-</font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=total_chargeable_overrun%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=total_positive_imbalance%></font></td>
			<td align="right"><font size="1.5px" face="Arial"><%=total_negitive_imbalance%></font></td>	
		</tr>
	</tbody>
</table>
<br>
<p><font size="1.5px" face="Arial"><b><%=att_note%></b></font></p>
</body>
</html>