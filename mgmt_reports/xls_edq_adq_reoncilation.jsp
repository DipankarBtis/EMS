<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="ISO-8859-1">
<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_MGMT_Reports" id="mgmt_reports" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
/* String sysdate  = utildate.getSysdate();
String firstDate="01"+sysdate.substring(2, sysdate.length());

String from_dt = request.getParameter("from_dt")==null?""+firstDate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?""+sysdate:request.getParameter("to_dt");
String cargo_type = request.getParameter("cargo_type")==null?"P":request.getParameter("cargo_type"); */


String sysdate = utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String month_to=request.getParameter("month_to")==null?""+currentMonth:request.getParameter("month_to");
String year_to=request.getParameter("year_to")==null?""+currentYear:request.getParameter("year_to");
String cargo_type = request.getParameter("cargo_type")==null?"P":request.getParameter("cargo_type");

if(month.length() == 1)
{
	month="0"+month; 
}
if(month_to.length() == 1)
{
	month_to="0"+month_to; 
}
String from_dt  = "01"+"/"+month+"/"+year;
String last_dt = utildate.getLastDateOfMonth(month_to, year_to);
String to_dt  = last_dt;
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


mgmt_reports.setCallFlag("EDQ_ADQ_RECONCILATION");
mgmt_reports.setComp_cd(owner_cd);
mgmt_reports.setFrom_dt(from_dt);
mgmt_reports.setTo_dt(to_dt);
mgmt_reports.setCargo_type(cargo_type);
mgmt_reports.init();

Vector VMST_COUNTERPARTY_CD = mgmt_reports.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = mgmt_reports.getVMST_COUNTERPARTY_ABBR();
Vector VBOE_QTY = mgmt_reports.getVBOE_QTY();
Vector VBOE_NO = mgmt_reports.getVBOE_NO();
Vector VBOE_DT = mgmt_reports.getVBOE_DT();
Vector VARRIVAL_DT = mgmt_reports.getVARRIVAL_DT();
Vector VEDQ_QTY = mgmt_reports.getVEDQ_QTY();
Vector VADQ_QTY = mgmt_reports.getVADQ_QTY();
Vector VCARGO_REF = mgmt_reports.getVCARGO_REF();
Vector VSHIP_NAME = mgmt_reports.getVSHIP_NAME();
Vector VDIFF_MMBTU = mgmt_reports.getVDIFF_MMBTU();
Vector VALLOCATED_QTY = mgmt_reports.getVALLOCATED_QTY();
Vector VCUST_ABBR_SN = mgmt_reports.getVCUST_ABBR_SN();
Vector VCARGO_NO = mgmt_reports.getVCARGO_NO();


%>
</head>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="12" rowspan="" align="center">EDQ-ADQ Reconciliation</th>
		</tr>
	</table>
	<div class="row">
			 <div class="table-responsive">
			  <table class="table table-bordered table-hover" width="100%" border="1">
				<thead>
					<tr>
						<th rowspan="2">Sr.No.</th>
						<th rowspan="2">Cargo No</th>
						<th rowspan="2">Cargo_ref#</th>
						<th rowspan="2">Vessel Name</th>
						<th rowspan="2">EDQ<br>(MMBTU)</th>
						<th rowspan="2">ADQ<br>(MMBTU)</th>
						<th rowspan="2">Difference<br>(MMBTU)</th>
						<th colspan="2">Cargo Allocated To Which Customer</th>
						<th rowspan="2">BOE NO</th>
						<th rowspan="2">BOE QTY <br>(MMBTU)</th>
						<th rowspan="2">BOE DATE</th>
						<th rowspan="2">Cargo <br>Arrival Date</th>
					</tr>
					
					<tr>
						<th colspan="1">CUSTOMER-(Contract#)</th>
						<th colspan="1">ALLOCATED QTY<br>(MMBTU)</th>
					</tr>
				</thead>
				<tbody>
					<%int k=0;
						if(VMST_COUNTERPARTY_CD.size() > 0){ %>
							<%for(int i=0;i<VMST_COUNTERPARTY_CD.size(); i++){ 
							
								k+=1;%>
							<tr>
								<td align="right"><%= k %></td>
								<td align="center"><%=VCARGO_NO.elementAt(i)%></td>
								<td align="center"><%=VCARGO_REF.elementAt(i)%></td>
								<td align="center"><%= VSHIP_NAME.elementAt(i) %></td>
								<td align="right"><%= VEDQ_QTY.elementAt(i) %></td>
								<td align="right"><%= VADQ_QTY.elementAt(i) %></td>
								<td align="right"><%= VDIFF_MMBTU.elementAt(i) %></td>
								<td align="left" colspan="1"><%=VCUST_ABBR_SN.elementAt(i) %></td>
								<%-- <td align="right" colspan="1"><%= VALLOCATED_QTY.elementAt(i) %></td> --%>
								<td><%= VALLOCATED_QTY.elementAt(i).toString().replace("\u00A0","").replace("&nbsp;","").replace("&nbsp","").trim() %></td>
								<td align="right" ><%= VBOE_NO.elementAt(i) %></td>
								<td align="right" ><%= VBOE_QTY.elementAt(i) %></td>
								<td align="center" ><%= VBOE_DT.elementAt(i) %></td>
								<td align="center" ><%= VARRIVAL_DT.elementAt(i) %></td>
							</tr>
						<% } %>
					    <%}else{ %>
						<tr>
							<td colspan="12" align="center"><%=utilmsg.infoMessage("<b>No Data Available for Selected Period!</b>") %></td>
						</tr>
				    	<%} %> 
				</tbody> 
			 </table>
		    </div>
		  </div>
</body>
</html>