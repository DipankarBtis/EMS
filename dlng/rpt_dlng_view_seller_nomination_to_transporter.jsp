<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script>
function openPdf(file_nm,url)
{
	var msg="";
	var msg_type="";
	if(file_nm!="")
	{
		msg="PDF has been Generated!";
		msg_type="S";
	}
	else
	{
		msg="PDF Generation Failed!";
		msg_type="E";
	}
	//alert(msg)
	window.opener.refresh_rpt(msg,msg_type);
	var window1="";
	if(!window1 || window1.closed)
	{
		window1= window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1000,height=600,scrollbars=1");
	}
	else
	{
		window1.close();
		window1= window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1000,height=600,scrollbars=1");
	}
	
	//window.close();
}

function allPdf(all_pdf_print)
{
	var msg="";
	var msg_type="";
	if(all_pdf_print=="Y")
	{
		msg="All PDFs have been Generated!";
		msg_type="S";
	}
	else 
	{
		msg="PDF Generation Failed!";
		msg_type="E";
	}
	//alert(msg)
	window.opener.refresh_rpt(msg,msg_type);
	window.close();
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DB_Pdf_Generation1" id="pdfFile" scope="request"/>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sys_dt_time = utildate.getSysdateWithTime24hr();
String prevdate = utildate.getPreviousDate();

String gas_dt = request.getParameter("gas_dt")==null?prevdate:request.getParameter("gas_dt");
String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");
String emp_cd=""+session.getAttribute("emp_cd")==null?"":""+session.getAttribute("emp_cd");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
//String truck_cd=request.getParameter("truck_cd")==null?"0":request.getParameter("truck_cd");
String contact_person_cd=request.getParameter("contact_person_cd")==null?"":request.getParameter("contact_person_cd");
String file=request.getParameter("file")==null?"":request.getParameter("file");

if(!file.equals("ALL_PDF"))
{
	dlng.setCallFlag("DLNG_VIEW_SELLER_NOMINATION_TO_TRANSPORTER");
	dlng.setComp_cd(owner_cd);
	dlng.setEmp_cd(emp_cd);
	dlng.setGas_dt(gas_dt);
	dlng.setCounterparty_cd(counterparty_cd);
	//dlng.setTruck_cd(truck_cd);
	dlng.setContact_person_cd(contact_person_cd);
	dlng.init();
}

HashMap view_data = dlng.getView_data();
HashMap view_info = dlng.getView_info();
%>
<%if(file.equals("HTML")){ %>
<body>
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="top">
		<td width="50%">
			<div align="left" >
				<img src="../<%=CommonVariable.company_logo_path%>/<%=owner_logo%>" height="50px"/>
			</div>
		</td>
		<td width="50%">
			<div align="right">
				<font size="2" face="Arial">
					<font size="2" face="Arial"><%=view_info.get("company_nm")%><br></font>
					<%=view_info.get("ownAddress")%><br>
					<%=view_info.get("ownCity")%><%if(!view_info.get("ownPin").toString().trim().equals("")){%>&nbsp;-&nbsp;<%=view_info.get("ownPin")%><%}%><%if(!view_info.get("ownCity").toString().trim().equals("")){%>,<%}%><br>
					<%=view_info.get("ownState")%><br>
					Tel:&nbsp;<%=view_info.get("ownPhone")%><br>
					Email:&nbsp;<%=view_info.get("ownEmail")%><br>
				</font>
			</div>
		</td>
	</tr>	
</table>
<hr>
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="3">&nbsp;<br>&nbsp;</td>
	</tr>
	<tr>
    	<td width="50%">
			<div align="left">
				<font size="2" face="Arial">
					<b>To:</b><br>
					<%=view_info.get("contact_person_nm")%><br>
				 	<%=view_info.get("transporter_nm")%><br>
					<%=view_info.get("plantAddress")%><br>
					<%=view_info.get("plantCity")%><%if(!view_info.get("plantPin").toString().trim().equals("")){%>&nbsp;-&nbsp;<%=view_info.get("plantPin")%><%}%><%if(!view_info.get("plantCity").toString().trim().equals("")){%>,<%}%><br>
					<%=view_info.get("plantState")%><br>
					Tel:&nbsp;<%=view_info.get("contact_person_phone")%><br>
					Fax:&nbsp;<%=view_info.get("contact_person_fax")%>
				</font>
			</div>
		</td>
		<td width="50%"></td>
	</tr>
</table>
<br>
<table width="80%" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td colspan="6"><div align="right"><font size=2.5 face="Arial"><b>Seq No:</b>&nbsp;<%=view_info.get("seq_no")%></font></div></td>		
	</tr>
	<tr>
		<td colspan="6"><div align="right"><font size=2.5 face="Arial"><b>Date:</b>&nbsp;<%=sys_dt_time%></font></div></td>		
	</tr>
</table>
<br>
<table width="80%"  border="1" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td colspan="12" style="background: #c2d1f0;"><div align="center"><font size="3px" face="Arial"><b>SELLER NOMINATION&nbsp;(<%=owner_abbr%> - <%=view_info.get("transporter_abbr")%>&nbsp;:&nbsp;<%=view_info.get("transporter_nm")%>)</b></font></div></td>
	</tr>
</table>
<br>
<table width="80%"  border="1" align="center" cellpadding="2" cellspacing="0">	
	<tr valign="bottom">
		<td rowspan="2"><div align="center"><font size="2.5px" face="Arial"><b>Gas Day</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="2.5px" face="Arial"><b>Customer Name</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="2.5px" face="Arial"><b>Customer Plant</b></font></div></td>
		<td colspan="4"><div align="center"><font size="2.5px" face="Arial"><b>Truck Confirmation</b></font></div></td>
		<td colspan="3"><div align="center"><font size="2.5px" face="Arial"><b>Nomination</b></font></div></td>				
		<td rowspan="2"><div align="center"><font size="2.5px" face="Arial"><b>Check Post</b></font></div></td>				
		<td rowspan="2"><div align="center"><font size="2.5px" face="Arial"><b>Seller's Comment</b></font></div></td>				
	</tr>
	<tr valign="bottom">
		<td><div align="center"><font size="2.5px" face="Arial"><b>Truck No</b></font></div></td>
		<td><div align="center"><font size="2.5px" face="Arial"><b>Scheduled Date</b></font></div></td>
		<td><div align="center"><font size="2.5px" face="Arial"><b>Scheduled Time</b></font></div></td>
		<td><div align="center"><font size="2.5px" face="Arial"><b>Driver Name</b></font></div></td>
		<td><div align="center"><font size="2.5px" face="Arial"><b>MMBTU</b></font></div></td>
		<td><div align="center"><font size="2.5px" face="Arial"><b>MT</b></font></div></td>
		<td><div align="center"><font size="2.5px" face="Arial"><b>Rev No.</b></font></div></td>
	</tr>
	<%
	if(view_data.containsKey("v_gas_dt"))
	{
		HashMap transport_wise = (HashMap) view_data.get("v_gas_dt");
		int key_count=0;
	    for(int i=0; i<transport_wise.size();i++) 
	    {
	    	key_count+=1;
	    %>
	    <tr valign="top" <%if(key_count==transport_wise.size()){ %>style="font-weight:bold;"<%} %>>
			<%if(key_count==transport_wise.size()){ %>
			<td colspan="7">
				<div align="right">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_customer")).get(""+key_count)%>&nbsp;:&nbsp;</font>
				</div>
			</td>
			<%}else{ %>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=transport_wise.get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_customer")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_customer_plant")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_truck")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_nom_dt")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_nom_time")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_driver")).get(""+key_count) %></font>
				</div>
			</td>
			
			<%} %>
			<td>
				<div align="right">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_mmbtu")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_mt")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_nom_rev")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_checkpost")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_remark")).get(""+key_count) %></font>
				</div>
			</td>		
		</tr>	
	    <% }%>
	<%} %>
</table>
<%-- <br><br>
<table width="80%" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td>
		<div align="left"><font size="2.5px" face="Arial"><b>Note:</b>&nbsp;<%=view_info.get("remark")%></font></div>
		</td>
	</tr>
</table> --%>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="bottom">
		<td colspan="1" width="20%">
			<div align="left">
				<font size="2px" face="Arial">
					<br><br><br><br><br><%=view_info.get("emp_nm")%>
					<br>(<%=view_info.get("emp_ph_no")%>)<br>
					_________________________
					<br>
					Authorised Signatory
					<br>
					<%=view_info.get("company_nm")%>
				</font>
			</div>
		</td>
	</tr>
</table>&nbsp;

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="bottom">
		<td colspan="1" width="20%">
			<div align="center">
				<font size="2px" face="Arial">
					<i>This is a Computer Generated Report!</i>
				</font>
			</div>
		</td>
	</tr>
</table>
</body>
<%}else if(file.equals("XLS")){ %>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename=DLNG_Seller_NominationToTransporter_"+gas_dt.replaceAll("/", "")+".xls");
%>
<table>
	<tr>
		<td colspan="12">
			<font size="4"><b>DLNG Seller Nomination To Transporter&nbsp;(<%=owner_abbr%> - <%=view_info.get("transporter_abbr")%>&nbsp;:&nbsp;<%=view_info.get("transporter_nm")%>) - Gas Day <%=gas_dt%></b></font>
		</td>
	</tr>
</table>
<br>
<br>
<table width="80%"  border="1" align="center" cellpadding="2" cellspacing="0">	
	<tr valign="bottom">
		<td rowspan="2"><div align="center"><font size="2.5px" face="Arial"><b>Gas Day</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="2.5px" face="Arial"><b>Customer Name</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="2.5px" face="Arial"><b>Customer Plant</b></font></div></td>
		<td colspan="4"><div align="center"><font size="2.5px" face="Arial"><b>Truck Confirmation</b></font></div></td>
		<td colspan="3"><div align="center"><font size="2.5px" face="Arial"><b>Nomination</b></font></div></td>				
		<td rowspan="2"><div align="center"><font size="2.5px" face="Arial"><b>Check Post</b></font></div></td>				
		<td rowspan="2"><div align="center"><font size="2.5px" face="Arial"><b>Seller's Comment</b></font></div></td>				
	</tr>
	<tr valign="bottom">
		<td><div align="center"><font size="2.5px" face="Arial"><b>Truck No</b></font></div></td>
		<td><div align="center"><font size="2.5px" face="Arial"><b>Scheduled Date</b></font></div></td>
		<td><div align="center"><font size="2.5px" face="Arial"><b>Scheduled Time</b></font></div></td>
		<td><div align="center"><font size="2.5px" face="Arial"><b>Driver Name</b></font></div></td>
		<td><div align="center"><font size="2.5px" face="Arial"><b>MMBTU</b></font></div></td>
		<td><div align="center"><font size="2.5px" face="Arial"><b>MT</b></font></div></td>
		<td><div align="center"><font size="2.5px" face="Arial"><b>Rev No.</b></font></div></td>
	</tr>
	<%
	if(view_data.containsKey("v_gas_dt"))
	{
		HashMap transport_wise = (HashMap) view_data.get("v_gas_dt");
		int key_count=0;
	    for(int i=0; i<transport_wise.size();i++) 
	    {
	    	key_count+=1;
	    %>
	   <tr valign="top" <%if(key_count==transport_wise.size()){ %>style="font-weight:bold;"<%} %>>
			<%if(key_count==transport_wise.size()){ %>
			<td colspan="7">
				<div align="right">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_customer")).get(""+key_count)%>&nbsp;:&nbsp;</font>
				</div>
			</td>
			<%}else{ %>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=transport_wise.get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_customer")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_customer_plant")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_truck")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_nom_dt")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_nom_time")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_driver")).get(""+key_count) %></font>
				</div>
			</td>
			
			<%} %>
			<td>
				<div align="right">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_mmbtu")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_mt")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_nom_rev")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_checkpost")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="2.5px" face="Arial"><%=((HashMap) view_data.get("v_remark")).get(""+key_count) %></font>
				</div>
			</td>		
		</tr>	
	    <% }%>
	<%} %>
</table>
<%-- <br><br>
<table width="80%" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td>
		<div align="left"><font size="2.5px" face="Arial"><b>Note:</b>&nbsp;<%=view_info.get("remark")%></font></div>
		</td>
	</tr>
</table> --%>
<%}else if(file.equals("PDF")){ %>
<% 
HttpServletRequest req = request;

pdfFile.setCallFlag("DLNG_PDF_SELLER_NOMINATION_TO_TRANSPORTER");
pdfFile.setRequest(req);
pdfFile.setView_data(view_data);
pdfFile.setView_info(view_info);
pdfFile.setComp_logo(owner_logo);
pdfFile.setComp_cd(owner_cd);
pdfFile.setEmp_cd(emp_cd);
pdfFile.setGas_dt(gas_dt);
pdfFile.setCounterparty_cd(counterparty_cd);
//pdfFile.setTruck_cd(truck_cd);
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/"+CommonVariable.work_dir+owner_cd+""+CommonVariable.nom_to_transporter_pdf_path+""+file_nm;
%>
<script>
openPdf('<%=file_nm%>','<%=pdfpath%>');
</script>
<%}else if(file.equals("ALL_PDF")){ %>
<% 
String[] all_counterparty_cd = counterparty_cd.split("@@");
//String[] all_truck_cd = truck_cd.split("@@");
String[] all_contact_person_cd = contact_person_cd.split("@@");

for(int i=0;i<all_counterparty_cd.length;i++)
{
	
	dlng.setCallFlag("DLNG_VIEW_SELLER_NOMINATION_TO_TRANSPORTER");
	dlng.setComp_cd(owner_cd);
	dlng.setEmp_cd(emp_cd);
	dlng.setGas_dt(gas_dt);
	dlng.setCounterparty_cd(all_counterparty_cd[i]);
	//dlng.setTruck_cd(all_truck_cd[i]);
	dlng.setContact_person_cd(all_contact_person_cd[i]);
	dlng.init();
	
	view_data = dlng.getView_data();
	view_info = dlng.getView_info();
	HttpServletRequest req = request;
	
	pdfFile.setCallFlag("DLNG_PDF_SELLER_NOMINATION_TO_TRANSPORTER");
	pdfFile.setRequest(req);
	pdfFile.setView_data(view_data);
	pdfFile.setView_info(view_info);
	pdfFile.setComp_logo(owner_logo);
	pdfFile.setComp_cd(owner_cd);
	pdfFile.setEmp_cd(emp_cd);
	pdfFile.setGas_dt(gas_dt);
	pdfFile.setCounterparty_cd(all_counterparty_cd[i]);
	//pdfFile.setTruck_cd(all_truck_cd[i]);
	pdfFile.setContact_person_cd(all_contact_person_cd[i]);
	pdfFile.init();
	
	String file_url = pdfFile.getFile_url();
	String file_nm = pdfFile.getFile_nm();
	String pdfpath = file_url+"/"+CommonVariable.work_dir+owner_cd+""+CommonVariable.nom_to_transporter_pdf_path+""+file_nm;
}
String all_pdf_print=pdfFile.getAll_pdf_print();
%>
<script>
allPdf('<%=all_pdf_print%>');
</script>
<%} %>
</html>