<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="ISO-8859-1">

<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String sysdate=utildate.getSysdate();
String financial_year=request.getParameter("financial_year")==null?""+currentYear:request.getParameter("financial_year");
String fin_yr = financial_year.split("-")[0];

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
purchase.setCallFlag("IMPORT_OF_LNG");
purchase.setComp_cd(owner_cd);
purchase.setYear(fin_yr);
purchase.init();

Vector VTOTAL_QTY_MMBTU = purchase.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_MT = purchase.getVTOTAL_QTY_MT();
Vector VTOTAL_QTY_MMSCM = purchase.getVTOTAL_QTY_MMSCM();
Vector VMONTH = purchase.getVMONTH();
Vector VTOTAL_USD = purchase.getVTOTAL_USD();
Vector VTOTAL_INR = purchase.getVTOTAL_INR();

String mmbtu_qty_sum = purchase.getMmbtu_sum();
String mt_qty_sum = purchase.getMt_sum();
String mmscm_qty_sum = purchase.getMmscm_sum();
String total_usd_sum = purchase.getUSD_sum();
String total_inr_sum = purchase.getINR_sum();
String comp_nm = purchase.getComp_nm();

%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="12" rowspan="" align="center">Import Of LNG during <%=financial_year%></th>
		</tr>
	</table>
	<div class="row">
		<div class="table-responsive">
			<table class="table table-bordered table-hover" width="100%" border="1">
				<thead>
				 <tr>
			         <th rowspan="3" valign="middle"><b>Sr.<br>No</b></th>
			         <th rowspan="3"  valign="middle"><b>Month</b></th>
			         <th colspan="10" style="background-color:#FFFF00; color:black"><b><%=comp_nm %></b></th>
		         </tr>
		         <tr>
			         <th colspan="6"><b>Quantity</b></th>
			         <th colspan="4"><b>Value</b></th>
		         </tr>
		         <tr>
			         <th colspan="2"><b>MMBTU</b></th>
			         <th colspan="2"><b>MT</b></th>
			         <th colspan="2"><b>MMSCM</b></th>
			         <th colspan="2"><b>USD</b></th>
			         <th colspan="2"><b>Rs. Crores</b></th>
		         </tr>
			   </thead>
			   <tbody>
	            <%int K=0;%>
	            <% for(int i=0;i<VMONTH.size();i++){ 
	            K+=1; %>
	            <tr>
		           <td align="center"><%=K %></td>
		           <td align="center" style="mso-number-format:'\@';"><%=VMONTH.elementAt(i) %></td>
		           <td colspan="2" align="right"><%=VTOTAL_QTY_MMBTU.elementAt(i) %></td>
		           <td colspan="2" align="right"><%=VTOTAL_QTY_MT.elementAt(i) %></td>
		           <td colspan="2" align="right"><%=VTOTAL_QTY_MMSCM.elementAt(i) %></td>
		           <td colspan="2" align="right"><%=VTOTAL_USD.elementAt(i) %></td>
		           <td colspan="2" align="right"><%=VTOTAL_INR.elementAt(i) %></td>
	           </tr>
	           <%} %>
	           <tr>
	            <td>&nbsp;</td>
	            <td align="center"> <b>Total:</b></td>
	            <td colspan="2" align="right"> <b><%=mmbtu_qty_sum %></b></td>
	            <td colspan="2" align="right"> <b><%=mt_qty_sum %></b></td>
	            <td colspan="2" align="right"> <b><%= mmscm_qty_sum%></b></td>
	            <td colspan="2" align="right"><b><%=total_usd_sum%></b></td>
	            <td colspan="2" align="right"><b><%=total_inr_sum%></b></td>
	           </tr>
	         </tbody> 
			</table>
		</div>
	</div>
</body>
</html>