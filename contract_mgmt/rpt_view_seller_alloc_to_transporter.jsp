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
		window1= window.open(url,"Allocation To Transporter","top=10,left=10,width=1000,height=600,scrollbars=1");
	}
	else
	{
		window1.close();
		window1= window.open(url,"Allocation To Transporter","top=10,left=10,width=1000,height=600,scrollbars=1");
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
<jsp:useBean class="com.etrm.fms.contract_mgmt.DataBean_ContractMgmt" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String prevdate = utildate.getPreviousDate();

String gas_dt = request.getParameter("gas_dt")==null?prevdate:request.getParameter("gas_dt");

String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");
String emp_cd=""+session.getAttribute("emp_cd")==null?"":""+session.getAttribute("emp_cd");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String plant_seq=request.getParameter("plant_seq")==null?"0":request.getParameter("plant_seq");
String contact_person_cd=request.getParameter("contact_person_cd")==null?"":request.getParameter("contact_person_cd");
String file=request.getParameter("file")==null?"":request.getParameter("file");

if(!file.equals("ALL_PDF"))
{
	cont_mgmt.setCallFlag("VIEW_SELLER_ALLOC_TO_TRANSPORTER");
	cont_mgmt.setComp_cd(owner_cd);
	cont_mgmt.setGas_dt(gas_dt);
	cont_mgmt.setTransCd(counterparty_cd);
	cont_mgmt.setTransPlantSeq(plant_seq);
	cont_mgmt.setContact_person_cd(contact_person_cd);
	cont_mgmt.setEmp_cd(emp_cd);
	cont_mgmt.init();
}

HashMap view_seller_alloc_to_trans_data = cont_mgmt.getView_seller_alloc_to_trans_data();
HashMap view_seller_alloc_to_trans_info = cont_mgmt.getView_seller_alloc_to_trans_info();

%>

<%if(file.equals("HTML")){ %>
<body>
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="top">
		<td width="50%">
			<div align="left" >
				<img src="../images/Shell_logo.png" height="50px"/>
			</div>
		</td>
		<td width="50%">
			<div align="right">
				<font size="2" face="Arial">
					<font size="2" face="Arial"><%=view_seller_alloc_to_trans_info.get("company_nm")%><br></font>
					<%=view_seller_alloc_to_trans_info.get("ownAddress")%><br>
					<%=view_seller_alloc_to_trans_info.get("ownCity")%><%if(!view_seller_alloc_to_trans_info.get("ownPin").toString().trim().equals("")){%>&nbsp;-&nbsp;<%=view_seller_alloc_to_trans_info.get("ownPin")%><%}%><%if(!view_seller_alloc_to_trans_info.get("ownCity").toString().trim().equals("")){%>,<%}%><br>
					<%=view_seller_alloc_to_trans_info.get("ownState")%><br>
					Tel:&nbsp;<%=view_seller_alloc_to_trans_info.get("ownPhone")%><br>
					Email:&nbsp;<%=view_seller_alloc_to_trans_info.get("ownEmail")%><br>
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
					<%=view_seller_alloc_to_trans_info.get("contact_person_nm")%><br>
				 	<%=view_seller_alloc_to_trans_info.get("transporter_nm")%><br>
					<%=view_seller_alloc_to_trans_info.get("plantAddress")%><br>
					<%=view_seller_alloc_to_trans_info.get("plantCity")%><%if(!view_seller_alloc_to_trans_info.get("plantPin").toString().trim().equals("")){%>&nbsp;-&nbsp;<%=view_seller_alloc_to_trans_info.get("plantPin")%><%}%><%if(!view_seller_alloc_to_trans_info.get("plantCity").toString().trim().equals("")){%>,<%}%><br>
					<%=view_seller_alloc_to_trans_info.get("plantState")%><br>
					Tel:&nbsp;<%=view_seller_alloc_to_trans_info.get("contact_person_phone")%><br>
					Fax:&nbsp;<%=view_seller_alloc_to_trans_info.get("contact_person_fax")%>
				</font>
			</div>
		</td>
		<td width="50%"></td>
	</tr>
</table>
<br>
<table width="80%" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td colspan="6"><div align="right"><font size=2.5 face="Arial"><b>Seq No:</b>&nbsp;<%=view_seller_alloc_to_trans_info.get("seq_no")%></font></div></td>		
	</tr>
	<tr>
	</tr>
</table>
<br>
<table width="80%"  border="1" align="center" cellpadding="2" cellspacing="0">
	<br>
	<tr>
		<td colspan="6" style="background: #c2d1f0;">
			<div align="center"><font size="3px" face="Arial"><b>Allocation to Transporter (<%=view_seller_alloc_to_trans_info.get("transporter_abbr")%> : <%=view_seller_alloc_to_trans_info.get("transporter_plant_nm")%>)</b></font>
			</div>
		</td>
	</tr>
</table>
<br>
<table width="80%"  border="1" align="center" cellpadding="2" cellspacing="0">	
	<tr valign="bottom">
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Gas Day <br>(06:00 hours <br> starting on)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Seller Nomination<br>(MMBTU)</b></font></div></td>
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Calorific Value Base<br>(Kcal/SCM)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Total Send out (MMBTU)</b></font></div></td>
	</tr>
	<tr> 
		<td><div align="center"><font size="1.5px" face="Arial"><b>GCV</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>NCV</b></font></div></td>	
	</tr>
	<%
	if(view_seller_alloc_to_trans_data.containsKey("v_gas_dt"))
	{
		HashMap transport_wise = (HashMap) view_seller_alloc_to_trans_data.get("v_gas_dt");
		int key_count=0;
	    for(int i=0; i<transport_wise.size();i++) 
	    {
	    	key_count+=1;
	    %>
	    <tr valign="top" <%if(key_count==transport_wise.size()){ %>style="font-weight:bold;"<%} %>>
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=transport_wise.get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_sell_nom")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_gcv")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_ncv")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_send_out")).get(""+key_count) %></font>
				</div>
			</td>
		</tr>	
	    <% }%>
	<%} %>
</table>
<br><br>
<table width="80%"  border="1" align="left" cellpadding="2" cellspacing="0" style="margin-left:10%;">
	<tr valign="bottom">
		<td rowspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Customer</b></font></div></td>
		<td rowspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Customer ID</b></font></div></td>
		<td rowspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Contract ID</b></font></div></td>
		<td rowspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Seller Nomination<br>(MMBTU)</b></font></div></td>
		<td colspan="4"><div align="center"><font size="1.5px" face="Arial"><b>Allocation</b></font></div></td>
	</tr>
	<tr> 
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>GCV Based</b></font></div></td>
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>NCV Based</b></font></div></td>	
	</tr>
	<tr> 
		<td><div align="center"><font size="1.5px" face="Arial"><b>MMBTU</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>SCM</b></font></div></td>	
		<td><div align="center"><font size="1.5px" face="Arial"><b>MMBTU</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>SCM</b></font></div></td>	
	</tr>
	<%
	if(view_seller_alloc_to_trans_data.containsKey("v_countpty_plantNm"))
	{
		HashMap contract_wise = (HashMap) view_seller_alloc_to_trans_data.get("v_countpty_plantNm");
		int key_count=0;
	    for(int i=0; i<contract_wise.size();i++) 
	    {
	    	key_count+=1;
	    %>
	    <tr valign="top">
			<td>
				<div align="left">
					<font size="1.5px" face="Arial"><%=contract_wise.get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_customer_code")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_ct_ref")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_sell_nom_custplantwise")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_gcv_mmbtu")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_gcv_scm")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_ncv_mmbtu")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_ncv_scm")).get(""+key_count) %></font>
				</div>
			</td>
		</tr>	
	    <% }%>
	<%} %>
</table>
<br><br>
<table width="80%" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td>
		<div align="left"><font size="1.5px" face="Arial"><b>Note : Kindly Note that Energy at NHV is Provided For reference only. It will be not used for commercial purpose.</b></font></div>
		</td>
	</tr>
</table>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="bottom">
		<td colspan="1" width="20%">
			<div align="left">
				<font size="2px" face="Arial">
					<br><br><br><br><br><%=view_seller_alloc_to_trans_info.get("emp_nm")%>
					_________________________
					<br>
					Authorised Signatory
					<br>
					<%=view_seller_alloc_to_trans_info.get("company_nm")%>
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
	response.setHeader("content-Disposition","inline; filename=AllocationToTransporter_"+gas_dt.replaceAll("/", "")+".xls");
%>
<table width="80%"  border="1" align="center" cellpadding="2" cellspacing="0">
	<br>
	<tr>
		<td colspan="5" style="background: #c2d1f0;">
			<div align="center"><font size="3px" face="Arial"><b>Allocation to Transporter (<%=view_seller_alloc_to_trans_info.get("transporter_abbr")%> : <%=view_seller_alloc_to_trans_info.get("transporter_plant_nm")%>)</b></font>
			</div>
		</td>
	</tr>
</table>
<br>
<table width="80%"  border="1" align="center" cellpadding="2" cellspacing="0">	
	<tr valign="bottom">
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Gas Day <br>(06:00 hours <br> starting on)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Seller Nomination<br>(MMBTU)</b></font></div></td>
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Calorific Value Base<br>(Kcal/SCM)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Total Send out (MMBTU)</b></font></div></td>
	</tr>
	<tr> 
		<td><div align="center"><font size="1.5px" face="Arial"><b>GCV</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>NCV</b></font></div></td>	
	</tr>
	<%
	if(view_seller_alloc_to_trans_data.containsKey("v_gas_dt"))
	{
		HashMap transport_wise = (HashMap) view_seller_alloc_to_trans_data.get("v_gas_dt");
		int key_count=0;
	    for(int i=0; i<transport_wise.size();i++) 
	    {
	    	key_count+=1;
	    %>
	    <tr valign="top"> <%-- <%if(key_count==transport_wise.size()){ %>style="font-weight:bold;"<%} %>> --%>
			<td>
				<div align="center">
					<font size="1.5px" face="Arial">&nbsp;<%=transport_wise.get(""+key_count) %>&nbsp;</font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_sell_nom")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_gcv")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_ncv")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_send_out")).get(""+key_count) %></font>
				</div>
			</td>
		</tr>	
	    <% }%>
	<%} %>
</table>
<br><br>
<table width="80%"  border="1" align="left" cellpadding="2" cellspacing="0" style="margin-left:10%;">
	<tr valign="bottom">
		<td rowspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Customer</b></font></div></td>
		<td rowspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Customer ID</b></font></div></td>
		<td rowspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Contract ID</b></font></div></td>
		<td rowspan="3"><div align="center"><font size="1.5px" face="Arial"><b>Seller Nomination<br>(MMBTU)</b></font></div></td>
		<td colspan="4"><div align="center"><font size="1.5px" face="Arial"><b>Allocation</b></font></div></td>
	</tr>
	<tr> 
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>GCV Based</b></font></div></td>
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>NCV Based</b></font></div></td>	
	</tr>
	<tr> 
		<td><div align="center"><font size="1.5px" face="Arial"><b>MMBTU</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>SCM</b></font></div></td>	
		<td><div align="center"><font size="1.5px" face="Arial"><b>MMBTU</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>SCM</b></font></div></td>	
	</tr>
	<%
	if(view_seller_alloc_to_trans_data.containsKey("v_countpty_plantNm"))
	{
		HashMap contract_wise = (HashMap) view_seller_alloc_to_trans_data.get("v_countpty_plantNm");
		int key_count=0;
	    for(int i=0; i<contract_wise.size();i++) 
	    {
	    	key_count+=1;
	    %>
	    <tr valign="top">
			<td>
				<div align="left">
					<font size="1.5px" face="Arial"><%=contract_wise.get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_customer_code")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_ct_ref")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_sell_nom_custplantwise")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_gcv_mmbtu")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_gcv_scm")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_ncv_mmbtu")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_seller_alloc_to_trans_data.get("v_ncv_scm")).get(""+key_count) %></font>
				</div>
			</td>
		</tr>	
	    <% }%>
	<%} %>
</table>
<br>
<table width="80%" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td colspan="8">
		<div align="left"><font size="1.5px" face="Arial"><b>Note : Kindly Note that Energy at NHV is Provided For reference only. It will be not used for commercial purpose.</b></font></div>
		</td>
	</tr>
</table>
<%}else if(file.equals("PDF")){ %>
<% 
HttpServletRequest req = request;

pdfFile.setCallFlag("PDF_ALLOCATION_TO_TRANSPORTER");
pdfFile.setRequest(req);
pdfFile.setView_seller_alloc_to_trans_data(view_seller_alloc_to_trans_data);
pdfFile.setView_seller_alloc_to_trans_info(view_seller_alloc_to_trans_info);
pdfFile.setComp_logo(owner_logo);
pdfFile.setComp_cd(owner_cd);
pdfFile.setGas_dt(gas_dt);
pdfFile.setEmp_cd(emp_cd);
pdfFile.setCounterparty_cd(counterparty_cd);
pdfFile.setPlant_seq(plant_seq);
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/"+CommonVariable.work_dir+owner_cd+""+CommonVariable.alloc_to_trans_pdf_path+""+file_nm;
%>
<script>
openPdf('<%=file_nm%>','<%=pdfpath%>');
</script>
<%}else if(file.equals("ALL_PDF")){ %>
<%
String[] all_counterparty_cd = counterparty_cd.split("@@");
String[] all_plant_seq = plant_seq.split("@@");
String[] all_contact_person_cd = contact_person_cd.split("@@");

for(int i=0;i<all_counterparty_cd.length;i++)
{
	
	cont_mgmt.setCallFlag("VIEW_SELLER_ALLOC_TO_TRANSPORTER");
	cont_mgmt.setComp_cd(owner_cd);
	cont_mgmt.setEmp_cd(emp_cd);
	cont_mgmt.setGas_dt(gas_dt);
	cont_mgmt.setTransCd(all_counterparty_cd[i]);
	cont_mgmt.setTransPlantSeq(all_plant_seq[i]);
	cont_mgmt.setContact_person_cd(all_contact_person_cd[i]);
	cont_mgmt.init();
	
	view_seller_alloc_to_trans_data = cont_mgmt.getView_seller_alloc_to_trans_data();
	view_seller_alloc_to_trans_info = cont_mgmt.getView_seller_alloc_to_trans_info();
	HttpServletRequest req = request;
	
	pdfFile.setCallFlag("PDF_ALLOCATION_TO_TRANSPORTER");
	pdfFile.setRequest(req);
	pdfFile.setView_seller_alloc_to_trans_data(view_seller_alloc_to_trans_data);
	pdfFile.setView_seller_alloc_to_trans_info(view_seller_alloc_to_trans_info);
	pdfFile.setComp_logo(owner_logo);
	pdfFile.setComp_cd(owner_cd);
	pdfFile.setEmp_cd(emp_cd);
	pdfFile.setGas_dt(gas_dt);
	pdfFile.setCounterparty_cd(all_counterparty_cd[i]);
	pdfFile.setPlant_seq(all_plant_seq[i]);
	pdfFile.setContact_person_cd(all_contact_person_cd[i]);
	pdfFile.init();
	
	String file_url = pdfFile.getFile_url();
	String file_nm = pdfFile.getFile_nm();
	String pdfpath = file_url+"/"+CommonVariable.work_dir+owner_cd+""+CommonVariable.alloc_to_trans_pdf_path+""+file_nm;
}
String all_pdf_print=pdfFile.getAll_pdf_print();
%>
<script>
allPdf('<%=all_pdf_print%>');
</script>
<%} %>
</html>