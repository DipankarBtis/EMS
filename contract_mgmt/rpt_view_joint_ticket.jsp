<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Vector"%>
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
		window1= window.open(url,"Joint Ticket","top=10,left=10,width=1000,height=600,scrollbars=1");
	}
	else
	{
		window1.close();
		window1= window.open(url,"Joint Ticket","top=10,left=10,width=1000,height=600,scrollbars=1");
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
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String plant_seq=request.getParameter("plant_seq")==null?"0":request.getParameter("plant_seq");
String bu_plant_seq=request.getParameter("bu_plant_seq")==null?"0":request.getParameter("bu_plant_seq");
String contact_person_cd=request.getParameter("contact_person_cd")==null?"":request.getParameter("contact_person_cd");
String file=request.getParameter("file")==null?"":request.getParameter("file");

if(!file.equals("ALL_PDF"))
{
	cont_mgmt.setCallFlag("VIEW_JOINT_TICKET");
	cont_mgmt.setComp_cd(owner_cd);
	cont_mgmt.setGas_dt(gas_dt);
	cont_mgmt.setCounterparty_cd(counterparty_cd);
	cont_mgmt.setContract_type(contract_type);
	cont_mgmt.setPlant_seq(plant_seq);
	cont_mgmt.setBu_plant_seq(bu_plant_seq);
	cont_mgmt.setContact_person_cd(contact_person_cd);
	cont_mgmt.setEmp_cd(emp_cd);
	cont_mgmt.init();
}

HashMap view_join_ticket_data = cont_mgmt.getView_join_ticket_data();
HashMap view_join_ticket_info = cont_mgmt.getView_join_ticket_info();

Vector VLINKCUSTID=cont_mgmt.getVLINKCUSTID();
Vector VLINKCONTID=cont_mgmt.getVLINKCONTID();
Vector VLINKJT_TRANSPORTER_NM=cont_mgmt.getVLINKJT_TRANSPORTER_NM();
Vector VLINKJT_TRANSPORTER_ABBR=cont_mgmt.getVLINKJT_TRANSPORTER_ABBR();
Vector VLINKJT_TRANSPORTER_QTY_MMBTU=cont_mgmt.getVLINKJT_TRANSPORTER_QTY_MMBTU();
Vector VLINKJT_QTY_SCM=cont_mgmt.getVLINKJT_QTY_SCM();
Vector VLINKJT_QTY_MMBTU=cont_mgmt.getVLINKJT_QTY_MMBTU();
Vector VLINKJT_SELLER_NOM_QTY_MMBTU=cont_mgmt.getVLINKJT_SELLER_NOM_QTY_MMBTU();
Vector VLINKJT_GCV=cont_mgmt.getVLINKJT_GCV();
Vector VLINKJT_NCV=cont_mgmt.getVLINKJT_NCV();

String tot_qty="";
String tot_mmbtu="";
String tot_scm="";
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
					<font size="2" face="Arial"><%=view_join_ticket_info.get("company_nm")%><br></font>
					<%=view_join_ticket_info.get("bu_plantAddress")%><br>
					<%=view_join_ticket_info.get("bu_plantCity")%><%if(!view_join_ticket_info.get("bu_plantPin").toString().trim().equals("")){%>&nbsp;-&nbsp;<%=view_join_ticket_info.get("bu_plantPin")%><%}%><%if(!view_join_ticket_info.get("bu_plantCity").toString().trim().equals("")){%>,<%}%><br>
					<%=view_join_ticket_info.get("bu_plantState")%><br>
					Tel:&nbsp;<%=view_join_ticket_info.get("ownPhone")%><br>
					Email:&nbsp;<%=view_join_ticket_info.get("ownEmail")%>
				</font>
			</div>
		</td>
	</tr>	
</table>
<hr>
<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="3">&nbsp;<br>&nbsp;</td>
	</tr>
	<tr>
    	<td width="50%">
			<div align="left">
				<font size="2" face="Arial">
					<b>To:</b><br>
					<%=view_join_ticket_info.get("contact_person_nm")%><br>
				 	<%=view_join_ticket_info.get("customer_nm")%><br>
					<%=view_join_ticket_info.get("plantAddress")%><br>
					<%=view_join_ticket_info.get("plantCity")%><%if(!view_join_ticket_info.get("plantPin").toString().trim().equals("")){%>&nbsp;-&nbsp;<%=view_join_ticket_info.get("plantPin")%><%}%><%if(!view_join_ticket_info.get("plantCity").toString().trim().equals("")){%>,<%}%><br>
					<%=view_join_ticket_info.get("plantState")%><br>
					Tel:&nbsp;<%=view_join_ticket_info.get("contact_person_phone")%><br>
					Fax:&nbsp;<%=view_join_ticket_info.get("contact_person_fax")%>
				</font>
			</div>
		</td>
		<td width="50%"></td>
	</tr>
</table>
<table width="80%" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td colspan="6"><div align="right"><font size=2.5 face="Arial"><b>Seq No:</b>&nbsp;<%=view_join_ticket_info.get("seq_no")%></font></div></td>	
	</tr>
</table>
<br>
<table width="80%"  border="1" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td colspan="6" style="background: #c2d1f0;"><div align="center"><font size="3px" face="Arial"><b>JOINT TICKET&nbsp;(<%=owner_abbr%> - <%=view_join_ticket_info.get("customer_abbr")%>&nbsp;:&nbsp;<%=view_join_ticket_info.get("plant_nm")%>)</b></font></div></td>
	</tr>
</table>
<br>
<table width="80%"  border="1" align="center" cellpadding="2" cellspacing="0">	
	<tr valign="bottom">
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Gas Day <br>(06:00 hours <br> starting on)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Seller Nominated QTY<br>(MMBTU)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Customer ID</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Contract ID</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>GCV<br>(Kcal/SCM)</b></font></div></td>
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Gas Delivered Quantity</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Transporter<br>Grid</b></font></div></td>
	</tr>
	<tr> 
		<td><div align="center"><font size="1.5px" face="Arial"><b>MMBTU</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>SCM</b></font></div></td>	
	</tr>
	<%
	if(view_join_ticket_data.containsKey("v_gas_dt"))
	{
		HashMap transport_wise = (HashMap) view_join_ticket_data.get("v_gas_dt");
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
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_sell_nom")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<%if(!transport_wise.get(""+key_count).equals("Total")){ %>
				<div align="center">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_customer_code")).get(""+key_count) %></font>
				</div>
				<%} %>
			</td>
			<td>
				<%if(!transport_wise.get(""+key_count).equals("Total")){ %>
				<div align="center">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_ct_ref")).get(""+key_count) %></font>
				</div>
				<%} %>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_base_val")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_mmbtu")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_scm")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_grid")).get(""+key_count) %></font>
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
		<div align="left"><font size="1.5px" face="Arial"><b>Note:</b></font></div>
		</td>
	</tr>
</table>
<table width="40%"  border="1" align="left" cellpadding="2" cellspacing="0" style="margin-left:10%;">
	<tr valign="bottom">
		<td><div align="center"><font size="1.5px" face="Arial"><b>Sr#</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Contract</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Gas Delivered Quantity<br>(MMBTU)</b></font></div></td>
	</tr>
	<%
	if(view_join_ticket_data.containsKey("v_contracts"))
	{
		HashMap contract_wise = (HashMap) view_join_ticket_data.get("v_contracts");
		int key_count=0;
	    for(int i=0; i<contract_wise.size();i++) 
	    {
	    	key_count+=1;
	    %>
	    <tr valign="top">
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=key_count %></font>
				</div>
			</td>
			<td>
				<div align="left">
					<font size="1.5px" face="Arial"><%=contract_wise.get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_cont_mmbtu")).get(""+key_count) %></font>
				</div>
			</td>
		</tr>	
	    <% }%>
	<%} %>
</table>


<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="bottom">
		<td colspan="1" width="20%">
			<div align="left">
				<font size="2px" face="Arial">
					<br><br><br><br><br><%=view_join_ticket_info.get("emp_nm")%><br>
					_________________________
					<br>
					Authorised Signatory
					<br>
					<%=view_join_ticket_info.get("company_nm")%>
				</font>
			</div>
		</td>
	</tr>
</table>
&nbsp;
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
	response.setHeader("content-Disposition","inline; filename=JoinTicket_"+gas_dt.replaceAll("/", "")+".xls");
%>
<table width="80%"  border="1" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td colspan="8" style="background: #c2d1f0;"><div align="center"><font size="3px" face="Arial"><b>JOINT TICKET&nbsp;(<%=owner_abbr%> - <%=view_join_ticket_info.get("customer_abbr")%>&nbsp;:&nbsp;<%=view_join_ticket_info.get("plant_nm")%>)</b></font></div></td>
	</tr>
</table>
<br>
<table width="80%"  border="1" align="center" cellpadding="2" cellspacing="0">	
	<tr valign="bottom">
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Gas Day <br>(06:00 hours <br> starting on)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Seller Nominated QTY<br>(MMBTU)</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Customer ID</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Contract ID</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>GCV<br>(Kcal/SCM)</b></font></div></td>
		<td colspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Gas Delivered Quantity</b></font></div></td>
		<td rowspan="2"><div align="center"><font size="1.5px" face="Arial"><b>Transporter<br>Grid</b></font></div></td>
	</tr>
	<tr> 
		<td><div align="center"><font size="1.5px" face="Arial"><b>MMBTU</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>SCM</b></font></div></td>	
	</tr>
	<%
	if(view_join_ticket_data.containsKey("v_gas_dt"))
	{
		HashMap transport_wise = (HashMap) view_join_ticket_data.get("v_gas_dt");
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
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_sell_nom")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<%if(!transport_wise.get(""+key_count).equals("Total")){ %>
				<div align="center">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_customer_code")).get(""+key_count) %></font>
				</div>
				<%} %>
			</td>
			<td>
				<%if(!transport_wise.get(""+key_count).equals("Total")){ %>
				<div align="center">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_ct_ref")).get(""+key_count) %></font>
				</div>
				<%} %>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_base_val")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_mmbtu")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_scm")).get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_grid")).get(""+key_count) %></font>
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
		<div align="left"><font size="1.5px" face="Arial"><b>Note:</b></font></div>
		</td>
	</tr>
</table>
<table width="40%"  border="1" align="left" cellpadding="2" cellspacing="0" style="margin-left:10%;">
	<tr valign="bottom">
		<td><div align="center"><font size="1.5px" face="Arial"><b>Sr#</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Contract</b></font></div></td>
		<td><div align="center"><font size="1.5px" face="Arial"><b>Gas Delivered Quantity<br>(MMBTU)</b></font></div></td>
	</tr>
	<%
	if(view_join_ticket_data.containsKey("v_contracts"))
	{
		HashMap contract_wise = (HashMap) view_join_ticket_data.get("v_contracts");
		int key_count=0;
	    for(int i=0; i<contract_wise.size();i++) 
	    {
	    	key_count+=1;
	    %>
	    <tr valign="top">
			<td>
				<div align="center">
					<font size="1.5px" face="Arial"><%=key_count %></font>
				</div>
			</td>
			<td>
				<div align="left">
					<font size="1.5px" face="Arial"><%=contract_wise.get(""+key_count) %></font>
				</div>
			</td>
			<td>
				<div align="right">
					<font size="1.5px" face="Arial"><%=((HashMap) view_join_ticket_data.get("v_cont_mmbtu")).get(""+key_count) %></font>
				</div>
			</td>
		</tr>	
	    <% }%>
	<%} %>
</table>

<%}else if(file.equals("PDF")){ %>
<% 
HttpServletRequest req = request;

pdfFile.setCallFlag("PDF_JOINT_TICKET");
pdfFile.setRequest(req);
pdfFile.setView_join_ticket_data(view_join_ticket_data);
pdfFile.setView_join_ticket_info(view_join_ticket_info);
pdfFile.setComp_logo(owner_logo);
pdfFile.setComp_cd(owner_cd);
pdfFile.setEmp_cd(emp_cd);
pdfFile.setGas_dt(gas_dt);
pdfFile.setCounterparty_cd(counterparty_cd);
pdfFile.setPlant_seq(plant_seq);
pdfFile.setContract_type(contract_type);
pdfFile.setBu_plant_seq(bu_plant_seq);
pdfFile.setContact_person_cd(contact_person_cd);
pdfFile.setVLINKCUSTID(VLINKCUSTID);
pdfFile.setVLINKCONTID(VLINKCONTID);
pdfFile.setVLINKJT_TRANSPORTER_NM(VLINKJT_TRANSPORTER_NM);
pdfFile.setVLINKJT_TRANSPORTER_ABBR(VLINKJT_TRANSPORTER_ABBR);
pdfFile.setVLINKJT_TRANSPORTER_QTY_MMBTU(VLINKJT_TRANSPORTER_QTY_MMBTU);
pdfFile.setVLINKJT_QTY_SCM(VLINKJT_QTY_SCM);
pdfFile.setVLINKJT_QTY_MMBTU(VLINKJT_QTY_MMBTU);
pdfFile.setVLINKJT_SELLER_NOM_QTY_MMBTU(VLINKJT_SELLER_NOM_QTY_MMBTU);
pdfFile.setVLINKJT_GCV(VLINKJT_GCV);
pdfFile.setVLINKJT_NCV(VLINKJT_NCV);
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/"+CommonVariable.work_dir+owner_cd+""+CommonVariable.join_ticket_pdf_path+""+file_nm;
%>
<script>
openPdf('<%=file_nm%>','<%=pdfpath%>');
</script>
<%}else if(file.equals("ALL_PDF")){ %>
<% 
String[] all_counterparty_cd = counterparty_cd.split("@@");
String[] all_contract_type = contract_type.split("@@");
String[] all_plant_seq = plant_seq.split("@@");
String[] all_contact_person_cd = contact_person_cd.split("@@");
String[] all_bu_plant_seq = bu_plant_seq.split("@@");

for(int i=0;i<all_counterparty_cd.length;i++)
{
	
	cont_mgmt.setCallFlag("VIEW_JOINT_TICKET");
	cont_mgmt.setComp_cd(owner_cd);
	cont_mgmt.setEmp_cd(emp_cd);
	cont_mgmt.setGas_dt(gas_dt);
	cont_mgmt.setCounterparty_cd(all_counterparty_cd[i]);
	cont_mgmt.setContract_type(all_contract_type[i]);
	cont_mgmt.setPlant_seq(all_plant_seq[i]);
	cont_mgmt.setContact_person_cd(all_contact_person_cd[i]);
	cont_mgmt.setBu_plant_seq(all_bu_plant_seq[i]);
	cont_mgmt.init();
	
	view_join_ticket_data = cont_mgmt.getView_join_ticket_data();
	view_join_ticket_info = cont_mgmt.getView_join_ticket_info();
	
	VLINKCUSTID=cont_mgmt.getVLINKCUSTID();
	VLINKCONTID=cont_mgmt.getVLINKCONTID();
	VLINKJT_TRANSPORTER_NM=cont_mgmt.getVLINKJT_TRANSPORTER_NM();
	VLINKJT_TRANSPORTER_ABBR=cont_mgmt.getVLINKJT_TRANSPORTER_ABBR();
	VLINKJT_TRANSPORTER_QTY_MMBTU=cont_mgmt.getVLINKJT_TRANSPORTER_QTY_MMBTU();
	VLINKJT_QTY_SCM=cont_mgmt.getVLINKJT_QTY_SCM();
	VLINKJT_QTY_MMBTU=cont_mgmt.getVLINKJT_QTY_MMBTU();
	VLINKJT_SELLER_NOM_QTY_MMBTU=cont_mgmt.getVLINKJT_SELLER_NOM_QTY_MMBTU();
	VLINKJT_GCV=cont_mgmt.getVLINKJT_GCV();
	VLINKJT_NCV=cont_mgmt.getVLINKJT_NCV();
	HttpServletRequest req = request;
	
	pdfFile.setCallFlag("PDF_JOINT_TICKET");
	pdfFile.setRequest(req);
	pdfFile.setView_join_ticket_data(view_join_ticket_data);
	pdfFile.setView_join_ticket_info(view_join_ticket_info);
	pdfFile.setComp_logo(owner_logo);
	pdfFile.setEmp_cd(emp_cd);
	pdfFile.setComp_cd(owner_cd);
	pdfFile.setGas_dt(gas_dt);
	pdfFile.setCounterparty_cd(all_counterparty_cd[i]);
	pdfFile.setPlant_seq(all_plant_seq[i]);
	pdfFile.setContract_type(all_contract_type[i]);
	pdfFile.setContact_person_cd(all_contact_person_cd[i]);
	pdfFile.setBu_plant_seq(all_bu_plant_seq[i]);
	pdfFile.setVLINKCUSTID(VLINKCUSTID);
	pdfFile.setVLINKCONTID(VLINKCONTID);
	pdfFile.setVLINKJT_TRANSPORTER_NM(VLINKJT_TRANSPORTER_NM);
	pdfFile.setVLINKJT_TRANSPORTER_ABBR(VLINKJT_TRANSPORTER_ABBR);
	pdfFile.setVLINKJT_TRANSPORTER_QTY_MMBTU(VLINKJT_TRANSPORTER_QTY_MMBTU);
	pdfFile.setVLINKJT_QTY_SCM(VLINKJT_QTY_SCM);
	pdfFile.setVLINKJT_QTY_MMBTU(VLINKJT_QTY_MMBTU);
	pdfFile.setVLINKJT_SELLER_NOM_QTY_MMBTU(VLINKJT_SELLER_NOM_QTY_MMBTU);
	pdfFile.setVLINKJT_GCV(VLINKJT_GCV);
	pdfFile.setVLINKJT_NCV(VLINKJT_NCV);
	pdfFile.init();
	
	String file_url = pdfFile.getFile_url();
	String file_nm = pdfFile.getFile_nm();
	String pdfpath = file_url+"/"+CommonVariable.work_dir+owner_cd+""+CommonVariable.join_ticket_pdf_path+""+file_nm;
}
String all_pdf_print=pdfFile.getAll_pdf_print();
%>
<script>
allPdf('<%=all_pdf_print%>');
</script>
<%} %>
</html>