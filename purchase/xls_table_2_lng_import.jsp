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
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
if(month.length() == 1)
{
	month="0"+month; 
}
String date = "01/"+month+"/"+year;
String mnth = utildate.getShortMonthName(date);
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

purchase.setCallFlag("IMPORT_OF_LNG_2");
purchase.setComp_cd(owner_cd);
purchase.setMonth(month);
purchase.setYear(year);
purchase.init();

Vector VTOTAL_QTY_MMBTU = purchase.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_MT = purchase.getVTOTAL_QTY_MT();
Vector VTOTAL_QTY_MMSCM = purchase.getVTOTAL_QTY_MMSCM();
Vector VMONTH = purchase.getVMONTH();
Vector VTOTAL_USD = purchase.getVTOTAL_USD();
Vector VTOTAL_INR = purchase.getVTOTAL_INR();
Vector VCONTRACT_TYPE = purchase.getVCONTRACT_TYPE();
Vector VCOUNTRY_NM = purchase.getVCOUNTRY_NM();
Vector VSALES_PRICE = purchase.getVSALES_PRICE();
Vector VGCV = purchase.getVGCV();

String dest_port = purchase.getDest_Port();
String profile_cd = purchase.getProfile_Cd();
String comp_nm = purchase.getComp_nm();

%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="12" rowspan="" align="center">Table 2 LNG Import</th>
		</tr>
	</table>
	<div class="row">
		<div class="table-responsive">
			<table class="table table-bordered table-hover" width="100%" border="1">
				<thead>
				 <tr style="background-color:#92D050; color:black">
				  <!-- <th></th> -->
				  <th align="left">1</th>
				  <th align="left">2</th>
				  <th align="left">3</th>
				  <th align="left">4</th>
				  <th align="left">5</th>
				  <th align="left">6</th>
				  <th align="left">7</th>
				  <th align="left">8</th>
				  <th align="left">9</th>
				  <th align="left">10</th>
				  <th align="left">11</th>
				  <th align="left">12</th>
				 </tr>
				 <tr style="background-color:#FFFF00; color:black">
		          <!-- <th>Sr.<br>No</th> -->
		          <th>Ship un-loading month /year</th>
		          <th>Importing Company Code</th>
		          <th>Type of Contract (Term/Spot & others)</th>
		          <th>Source/ Origin  Country Code</th>
		          <th>Destination Port Code (Unloading Port)</th>
		          <th>Quantity in Metric Tonnes as per Invoice</th>
		          <th>Quantity  in MMBTU as per Invoice</th>
		          <th>Gross Calorific Value (Kcal/SCM)</th>
		          <th>Quantity in MMSCM</th>
		          <th>Value of import(USD)</th>
		          <th>Value of import(Rs. Crores) </th>
		          <th>Indicative DES Price (US$/ MMBTU)</th>
		         </tr>
			   </thead>
			   <tbody>
	           <%if(VMONTH.size()>0){ %>
	         <%int K=0;%>
	            <% for(int i=0;i<VMONTH.size();i++){ 
	            K+=1; %>
	            <tr>
	           <%-- <td align="center"><%=K %></td> --%>
	           <td style="mso-number-format:'\@';"><%=VMONTH.elementAt(i) %></td>
	           <td align="center"><%=profile_cd%></td>
	           <td align="center"><%=VCONTRACT_TYPE.elementAt(i)%></td>
	           <td align="center"><%=VCOUNTRY_NM.elementAt(i)%></td>
	           <td align="right"><%=dest_port%></td>
	           <td align="right"><%=VTOTAL_QTY_MT.elementAt(i) %></td>
	           <td align="right"><%=VTOTAL_QTY_MMBTU.elementAt(i) %></td>
	           <td align="right"><%= VGCV.elementAt(i)%></td>
	           <td align="right"><%=VTOTAL_QTY_MMSCM.elementAt(i) %></td>
	           <td align="right"><%=VTOTAL_USD.elementAt(i) %></td>
	           <td align="right"><%=VTOTAL_INR.elementAt(i) %></td>
	           <td align="right"><%=VSALES_PRICE.elementAt(i) %></td>
	           </tr>
	           <%}%>
	           <%} else{ %>
	             <tr>
	               <td colspan = "12" align="center"><%=utilmsg.infoMessage("<b>...No data for selected month based on Actual Arrival Date!...</b>") %></td>
	             </tr>
	           <%} %>
	         </tbody> 
			</table>
		</div>
	</div>
</body>
</html>