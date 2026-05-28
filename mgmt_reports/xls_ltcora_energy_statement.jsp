
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import= "java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_MGMT_Reports" id="mgmt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String buy_sell = request.getParameter("buy_sell")==null?"C":request.getParameter("buy_sell");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String disp_cont_no = request.getParameter("disp_cont_no")==null?"":request.getParameter("disp_cont_no");
String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String cont_name = request.getParameter("cont_name")==null?"":request.getParameter("cont_name");
String no_cargo = request.getParameter("no_cargo")==null?"":request.getParameter("no_cargo");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String sug_per = request.getParameter("sug_per")==null?"":request.getParameter("sug_per");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

mgmt_rpt.setCallFlag("ENERGY_STATEMENT");
mgmt_rpt.setComp_cd(owner_cd);
mgmt_rpt.setBuySell(buy_sell);
mgmt_rpt.setCounterparty_cd(counterparty_cd);
mgmt_rpt.setAgmt_type(agmt_type);
mgmt_rpt.setAgmt_no(agmt_no);
mgmt_rpt.setAgmt_rev(agmt_rev);
mgmt_rpt.setCont_no(cont_no);
mgmt_rpt.setCont_rev(cont_rev);
mgmt_rpt.setCont_type(contract_type);
mgmt_rpt.setSug(sug_per);
mgmt_rpt.init();

String signing_dt = mgmt_rpt.getSigning_dt();

Vector VMST_COUNTERPARTY_CD = mgmt_rpt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = mgmt_rpt.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = mgmt_rpt.getVMST_COUNTERPARTY_NM();

Vector VCONTACT_PERSON = mgmt_rpt.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_SEQ_NO = mgmt_rpt.getVCONTACT_PERSON_SEQ_NO();

Vector VDEAL_MAP = mgmt_rpt.getVDEAL_MAP();
Vector VQQ_NO = mgmt_rpt.getVQQ_NO();
Vector VQQ_DT = mgmt_rpt.getVQQ_DT();
Vector VSHIP_NM = mgmt_rpt.getVSHIP_NM();
Vector VSTORAGE_START_DT = mgmt_rpt.getVSTORAGE_START_DT();
Vector VSTORAGE_END_DT = mgmt_rpt.getVSTORAGE_END_DT();
Vector VBOE_NO = mgmt_rpt.getVBOE_NO();
Vector VBOE_DT = mgmt_rpt.getVBOE_DT();
Vector VUNLOADED_QTY = mgmt_rpt.getVUNLOADED_QTY();
Vector VSUG_QTY = mgmt_rpt.getVSUG_QTY();
Vector VREGASSIFY_QTY = mgmt_rpt.getVREGASSIFY_QTY();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition", "inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="13" rowspan="2" align="left">LTCORA Cargo Energy Statement</th>
		</tr>
	</table>
	<table  width="100%" border="1">
		<thead>
			<tr>
				<th rowspan="2">Sr#</th>
				<th rowspan="2">cargo#</th>
				<th rowspan="2">Q & Q Certificate#</th>
				<th rowspan="2">Q & Q Certificate Date</th>
				<th rowspan="2">Vessel Name</th>
				<th rowspan="2">Vessel Arrival Date</th>
				<th colspan="2">Storage Period</th>
				<th rowspan="2">Boe Number</th>
				<th rowspan="2">Boe Date</th>
				<th rowspan="2">Unloaded MMBTU</th>
				<th rowspan="2">SUG</th>
				<th rowspan="2">Regassification MMBTU</th>
				<!-- <th rowspan="2">PDF</th>
				<th rowspan="2">Excel</th> -->
			</tr>
			<tr>
				<th>Start Date</th>
				<th>End Date</th>
			</tr>
		</thead>
		<tbody>
			<%if(VDEAL_MAP.size()>0){%>
				<%for(int i=0;i<VDEAL_MAP.size();i++){%>
				<tr>
					<td align="left"><%=i+1%></td>
					<td align="center"><%=VDEAL_MAP.elementAt(i)%></td>
					<td align="center"><%=VQQ_NO.elementAt(i)%></td>
					<td align="center"><%=VQQ_DT.elementAt(i)%></td>
					<td align="center"><%=VSHIP_NM.elementAt(i)%></td>
					<td align="center"><%=VSTORAGE_START_DT.elementAt(i)%></td>
					<td align="center"><%=VSTORAGE_START_DT.elementAt(i)%></td>
					<td align="center"><%=VSTORAGE_END_DT.elementAt(i)%></td>
					<td align="center"><%=VBOE_NO.elementAt(i)%></td>
					<td align="center"><%=VBOE_DT.elementAt(i)%></td>
					<td align="right"><%=VUNLOADED_QTY.elementAt(i)%></td>
					<td align="right"><%=VSUG_QTY.elementAt(i)%></td>
					<td align="right"><%=VREGASSIFY_QTY.elementAt(i)%></td>
				</tr>
				<%}%>
			<%}else{%>
				<tr>
					<td colspan="13" align="center"><%=utilmsg.infoMessage("<b>No Cargo is Available!</b>") %></td>
				</tr>
			<%}%>
		</tbody>
	</table>
</body>
</html>