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

String file_nm="GTA_ImbalanceReport_"+from_dt.replaceAll("/", "")+"_"+to_dt.replaceAll("/", "")+".xls";
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "inline; filename="+file_nm);  
%>
<h2><b>GTC Imbalance Report From <%=from_dt%> To <%=to_dt%></b></h2>
<table border="1">
	<thead>
		<tr>
			<th rowspan="2">Gas Day</th>
			<th rowspan="2">CT MDQ<br>(MMBTU)</th>
			<th colspan="3">Transporter Entry Point <font style="background:#ff99ff;color:var(--header_font_color);">(<%=entry_pt_nm%>)</font></th>
			<th colspan="3">Transporter Exit Point <font style="background:#ff99ff;;color:var(--header_font_color);">(<%=exit_pt_nm%>)</font></th>
			<th rowspan="2">Transmission<br>(MMBTU)</th>
			<th rowspan="2">Daily Imbalance(+/-)<br>(MMBTU)</th>
			<th rowspan="2">Cumulative Imbalance<br>(MMBTU)</th>
			<th rowspan="2">Daily Unauthorized Overrun<br>(MMBTU)</th>
			<th rowspan="2">Chargeable Overrun<br>(MMBTU)</th>
			<th rowspan="2">Chargeable +Ve Imbalance<br>(MMBTU)</th>
			<th rowspan="2">Chargeable -Ve Imbalance<br>(MMBTU)</th>
		</tr>
		<tr>
			<th style="background-color: #cfe2ff;">Nomination<br>(MMBTU)</th>
			<th style="background-color: #fff3cd;">Scheduling<br>(MMBTU)</th>
			<th style="background-color: #d1e7dd;">Allocation<br>(MMBTU)</th>
			<th style="background-color: #cfe2ff;">Nomination<br>(MMBTU)</th>
			<th style="background-color: #fff3cd;">Scheduling<br>(MMBTU)</th>
			<th style="background-color: #d1e7dd;">Allocation<br>(MMBTU)</th>
		</tr>
	</thead>
	<tbody>
		<%for(int i=0; i<VGAS_DT.size(); i++){ %>
		<tr>
			<td align="center">&nbsp;<%=VGAS_DT.elementAt(i)%>&nbsp;</td>
			<td align="right"><%=VMDQ.elementAt(i) %></td>
			<td align="right" style="background-color: #cfe2ff;color: #06357a;"><%=VNOM_ENTRY_QTY.elementAt(i)%></td>
			<td align="right" style="background-color: #fff3cd;color: #523e02;"><%=VSCH_ENTRY_QTY.elementAt(i)%></td>
			<td align="right" style="background-color: #d1e7dd;color: #0c4128;"><%=VALLOC_ENTRY_QTY.elementAt(i)%></td>
			<td align="right" style="background-color: #cfe2ff;color: #06357a;"><%=VNOM_EXIT_QTY.elementAt(i)%></td>
			<td align="right" style="background-color: #fff3cd;color: #523e02;"><%=VSCH_EXIT_QTY.elementAt(i) %></td>
			<td align="right" style="background-color: #d1e7dd;color: #0c4128;"><%=VALLOC_EXIT_QTY.elementAt(i) %></td>
			<td align="right"><%=VTRANSMISSION_QTY.elementAt(i)%></td>
			<td align="right"><%=VDAILY_IMBALANCE.elementAt(i) %></td>
			<td align="right"><%=VCUMULATIVE_IMBALANCE.elementAt(i) %></td>
			<td align="right"><%=VDAILY_UNAUTHORIZED_OVERRUN.elementAt(i) %></td>
			<td align="right"><%=VCHARGEABLE_OVERRUN.elementAt(i)%></td>
			<td align="right"><%=VCHARGEABLE_POSITIVE_IMBALANCE.elementAt(i)%></td>
			<td align="right"><%=VCHARGEABLE_NEGETIVE_IMBALANCE.elementAt(i)%></td>	
		</tr>
		<%} %>
		<tr style="font-weight: bold;">
			<td align="right">Total :</td>
			<td align="right"><%=total_var_mdq%></td>
			<td align="right" style="background-color: #cfe2ff;color: #06357a;"><%=total_nom_entry_qty%></td>
			<td align="right" style="background-color: #fff3cd;color: #523e02;"><%=total_sch_entry_qty%></td>
			<td align="right" style="background-color: #d1e7dd;color: #0c4128;"><%=total_alloc_entry_qty%></td>
			<td align="right" style="background-color: #cfe2ff;color: #06357a;"><%=total_nom_exit_qty%></td>
			<td align="right" style="background-color: #fff3cd;color: #523e02;"><%=total_sch_exit_qty%></td>
			<td align="right" style="background-color: #d1e7dd;color: #0c4128;"><%=total_alloc_exit_qty%></td>
			<td align="right"><%=total_transmission_qty%></td>
			<td align="right">-</td>
			<td align="right">-</td>
			<td align="right">-</td>
			<td align="right"><%=total_chargeable_overrun%></td>
			<td align="right"><%=total_positive_imbalance%></td>
			<td align="right"><%=total_negitive_imbalance%></td>	
		</tr>
	</tbody>
</table>
</body>
</html>