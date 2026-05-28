<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="ISO-8859-1">
<jsp:useBean class="com.etrm.fms.inventory.DataBean_TankTerminal" id="inventory" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%

String sysdate = utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String month_to=request.getParameter("month_to")==null?""+currentMonth:request.getParameter("month_to");
String year_to=request.getParameter("year_to")==null?""+currentYear:request.getParameter("year_to");

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

inventory.setCallFlag("INV_STOCK_DTL");
inventory.setComp_cd(owner_cd);
inventory.setFrom_Dt(from_dt);
inventory.setTo_Dt(to_dt);
inventory.init();

Vector VCARGO_NO = inventory.getVCARGO_NO();
Vector VEFF_DT = inventory.getVEFF_DT();
Vector VTANK_VOLUME = inventory.getVTANK_VOLUME();
Vector VUNLOADED_QTY = inventory.getVUNLOADED_QTY();
Vector VTANK_MMBTU = inventory.getVTANK_MMBTU();
Vector VBALANCE_QTY = inventory.getVBALANCE_QTY();
Vector VSEL_BALANCE_QTY = inventory.getVSEL_BALANCE_QTY();
%>
</head>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="7" rowspan="" align="center">Stock Inventory</th>
		</tr>
	</table>
	<div class="row">
	 <div class="table-responsive">
	  <table class="table table-bordered table-hover" width="100%" border="1">
		<thead>
			<tr>
			<th>SR.No#</th>
			<th>Date</th>
			<th>Tank Cap.</th>
			<th>Dead Stock</th>
			<th>Eff.Stock</th>
			<th>TP Obligation</th>
			<th>SEIPL-Stock(MMBTU)</th>
			</tr>
		</thead>
		<tbody>
			<%for(int i=0;i<VEFF_DT.size();i++){ %>
			<tr>
				<td align="center"><%=i+1 %></td>
				<td align="center"><%=VEFF_DT.elementAt(i)%></td>
				<td align="right"><%=VTANK_VOLUME.elementAt(i) %></td>
				<td align="right"><%=VUNLOADED_QTY.elementAt(i) %></td>
				<td align="right"><%=VTANK_MMBTU.elementAt(i) %></td>
				<td align="right"><%=VBALANCE_QTY.elementAt(i) %></td>
				<td align="right"><%=VSEL_BALANCE_QTY.elementAt(i) %></td>
			</tr>
			<% } %>
		</tbody> 
	 </table>
    </div>
 </div>
</body>
</html>