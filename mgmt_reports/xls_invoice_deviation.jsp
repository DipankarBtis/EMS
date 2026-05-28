<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="ISO-8859-1">
<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_MGMT_Reports" id="mgmt_reports" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sysdate  = utildate.getSysdate();
String firstDate="01"+sysdate.substring(2, sysdate.length());

String from_dt = request.getParameter("from_dt")==null?""+firstDate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?""+sysdate:request.getParameter("to_dt");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

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


mgmt_reports.setCallFlag("INVOICE_DEVIATION");
mgmt_reports.setComp_cd(owner_cd);
mgmt_reports.setFrom_dt(from_dt);
mgmt_reports.setTo_dt(to_dt);
mgmt_reports.setCounterparty_cd(counterparty_cd);

mgmt_reports.init();

Vector VCOUNTERPTY_CD = mgmt_reports.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_NM = mgmt_reports.getVCOUNTERPTY_NM();
Vector VINVOICE_NO = mgmt_reports.getVINVOICE_NO();
Vector VINVOICE_DT = mgmt_reports.getVINVOICE_DT();
Vector VDUE_DT_FLG = mgmt_reports.getVDUE_DT_FLG();
Vector VDUE_DT_REMARK = mgmt_reports.getVDUE_DT_REMARK();
Vector VEXCHNG_RATE_FLAG = mgmt_reports.getVEXCHNG_RATE_FLAG();
Vector VEXCHNG_DT_REMARK = mgmt_reports.getVEXCHNG_DT_REMARK();
Vector VBILLING_FREQ_FLAG = mgmt_reports.getVBILLING_FREQ_FLAG();
Vector VBILLING_DT_REMARK = mgmt_reports.getVBILLING_DT_REMARK();
Vector VCONT_NO = mgmt_reports.getVCONT_NO();

%>
</head>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="7" rowspan="" align="center">Invoice Deviation</th>
		</tr>
	</table>
	<div class="row">
		 <div class="table-responsive">
		  <table class="table table-bordered table-hover" width="100%" border="1">
			<thead>
				<tr>
					<th>Sr.no.</th>
					<th>Customer</th>
					<th>Invoice No</th>
					<th>Invoice Date</th>
					<th>Due Date Deviation</th>
					<th>Exchange Rate Deviation</th>
					<th>Billing Frequency Deviation</th>
				</tr>
			</thead>
			<tbody>
				<%int k=0;
					if(VCONT_NO.size() > 0){ %>
						<%for(int i=0;i<VCONT_NO.size(); i++){ 
						if((""+VDUE_DT_FLG.elementAt(i)).equalsIgnoreCase("Y") || (""+VEXCHNG_RATE_FLAG.elementAt(i)).equalsIgnoreCase("Y") 
								|| (""+VBILLING_FREQ_FLAG.elementAt(i)).equalsIgnoreCase("Y")){
							k+=1;%>
						<tr>
							<td align="right"><%= k %></td>
							<td><%=VCOUNTERPTY_NM.elementAt(i)%></td>
							<td align="left"><%= VINVOICE_NO.elementAt(i) %></td>
							<td align="center"><%= VINVOICE_DT.elementAt(i) %></td>
							<td align="center"><%= VDUE_DT_REMARK.elementAt(i) %></td>
							<td align="center"><%= VEXCHNG_DT_REMARK.elementAt(i) %></td>
							<td align="center"><%= VBILLING_DT_REMARK.elementAt(i) %></td>
						</tr>
					<% } 
						}%> 
				<%}else{ %>
					<tr>
						<td colspan="7" align="center"><%=utilmsg.infoMessage("<b>No Deviation Data Available for Selected Period!</b>") %></td>
					</tr>
			  	<%} %> 
			</tbody>
		 </table>
	    </div>
	</div>
</body>
</html>