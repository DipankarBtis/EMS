<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_MGMT_Reports" id="mgmt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
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
String counterparty_nm = request.getParameter("counterparty_nm")==null?"":request.getParameter("counterparty_nm");
String cargo_number_disp = request.getParameter("cargo_number_disp")==null?"":request.getParameter("cargo_number_disp");
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

mgmt_rpt.setCallFlag("STORAGE_REPORT");
mgmt_rpt.setComp_cd(owner_cd);
mgmt_rpt.setBuySell(buy_sell);
mgmt_rpt.setCounterparty_cd(counterparty_cd);
mgmt_rpt.setAgmt_type(agmt_type);
mgmt_rpt.setAgmt_no(agmt_no);
mgmt_rpt.setAgmt_rev(agmt_rev);
mgmt_rpt.setCont_no(cont_no);
mgmt_rpt.setCont_rev(cont_rev);
mgmt_rpt.setCont_type(contract_type);
mgmt_rpt.init();

Vector VMST_COUNTERPARTY_CD = mgmt_rpt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = mgmt_rpt.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = mgmt_rpt.getVMST_COUNTERPARTY_NM();

Vector VDEAL_MAP = mgmt_rpt.getVDEAL_MAP();
Vector VSTORAGE_DAYS = mgmt_rpt.getVSTORAGE_DAYS();
Vector VSTORAGE_START_DT = mgmt_rpt.getVSTORAGE_START_DT();
Vector VSTORAGE_END_DT = mgmt_rpt.getVSTORAGE_END_DT();
Vector VSTORAGE_EXT_DAYS = mgmt_rpt.getVSTORAGE_EXT_DAYS();
Vector VSTORAGE_EXT_END_DT = mgmt_rpt.getVSTORAGE_EXT_END_DT();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="7" rowspan="" align="left">LTCORA Storage Duration Report </th>
		</tr>
	</table>
	<div class="table-responsive">
		<table class="table table-bordered table-hover" border="1">
			<thead>
				<tr>
					<th>Sr#</th>
					<th>Counterpaty</th>
					<th>Contract#<br>[Contract Ref#]</th>
					<th>Cargo No.</th>
					<th>Storage Days</th>
					<th>Storage Duration Start Date</th>
					<th>Storage Duration Start Date</th>
					<th>Extension Days</th>
					<th>Extended End Date</th>
				</tr>
			</thead>
			<tbody>
			<%if(VDEAL_MAP.size()>0 ){%>
				<%for(int i=0;i<VDEAL_MAP.size();i++){%>
					<tr>
						<td align="center"><%=i+1%></td>
						<td align="center"><%=counterparty_nm%></td>
						<td align="center"><%=cargo_number_disp%><br>[<%=cont_ref_no %>]</td>
						<td align="center"><%=VDEAL_MAP.elementAt(i) %></td>
						<td align="center"><%=VSTORAGE_DAYS.elementAt(i) %></td>
						<td align="center"><%=VSTORAGE_START_DT.elementAt(i) %></td>
						<td align="center"><%=VSTORAGE_END_DT.elementAt(i) %></td>
						<td align="center"><%=VSTORAGE_EXT_DAYS.elementAt(i) %></td>
						<td align="center"><%=VSTORAGE_EXT_END_DT.elementAt(i) %></td>
					</tr>
				<%}%>
			<%}else{%>
				<tr>
					<td colspan="7" align="center"><%=utilmsg.infoMessage("<b>No Cargo is Available!</b>") %></td>
				</tr>
			<%} %>
			</tbody>
		</table>
	</div>
</body>
</html>