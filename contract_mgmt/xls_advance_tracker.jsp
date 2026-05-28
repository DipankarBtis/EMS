<%@page import= "java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String yestdate = utildate.getPreviousDate();
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

String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");

String st_dt = request.getParameter("st_dt")==null?"":request.getParameter("st_dt");
String en_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");


cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setCounterparty_cd(counterparty_cd);
cont_mgmt.setAgmt_no(agmt_no);
cont_mgmt.setAgmt_rev_no(agmt_rev_no);
cont_mgmt.setCont_no(cont_no);
cont_mgmt.setCont_rev_no(cont_rev_no);
cont_mgmt.setContract_type(contract_type);
cont_mgmt.setCallFlag("ADVANCE_TRACKER");
cont_mgmt.init();

Vector VMST_COUNTERPARTY_CD = cont_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = cont_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = cont_mgmt.getVMST_COUNTERPARTY_ABBR();

Vector VGAS_DT = cont_mgmt.getVGAS_DT();
Vector VDESC = cont_mgmt.getVDESC();
Vector VCREDIT = cont_mgmt.getVCREDIT();
Vector VDEBIT = cont_mgmt.getVDEBIT();
Vector VBALANCE_AMT = cont_mgmt.getVBALANCE_AMT();

String cont_ref_no=cont_mgmt.getCont_ref_no();
String dealDisplayMap=cont_mgmt.getDealDisplayMap();
String counterparty_nm=cont_mgmt.getCounterparty_nm();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="6" rowspan="1" align="left"><font size="6">Advance | Payment Tracker</font></th>
		</tr>
	</table>
	<br>
	<table  width="100%" border="1">
		<tr>
			<th colspan="6" rowspan="1" align="left">Counterparty : <%=counterparty_nm %><br>Deal Map : <%=dealDisplayMap%><br>Cont/Trade Ref : <%=cont_ref_no%><br>Contract Duration : <%=st_dt%> - <%=en_dt%></textarea>				    				
			</th>
		</tr>
	</table>
	<br>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>Sr. No.</th>
				<th>Date</th>
				<th>Description</th>
				<th>Debit (INR)</th>
				<th>Credit (INR)</th>
				<th>Balance (INR)</th>											
			</tr>
		</thead>
		<tbody>
		<%if(VGAS_DT.size() > 0){ int sr_count = 0;%>
			<%for(int i=VGAS_DT.size()-1; i>=0; i--){sr_count++; %>
			<tr>	
				<td align="center"><%=sr_count%></td>									
				<td align="center"><%=VGAS_DT.elementAt(i)%></td>
				<td align="left" width="50%"><%=VDESC.elementAt(i)%></td>
				<td align="right" <%if(!VDEBIT.elementAt(i).equals("-")){%> style="color: red;" <%}%>><%=VDEBIT.elementAt(i).equals("-")?"":VDEBIT.elementAt(i)%></td>
				<td align="right" <%if(!VCREDIT.elementAt(i).equals("-")){%> style="color: green;" <%}%>><%=VCREDIT.elementAt(i).equals("-")?"":VCREDIT.elementAt(i)%></td>
				<td align="right" <%if(VBALANCE_AMT.elementAt(i).toString().contains("-") ){%> style="color: red;" <%}else{%>style="color: green;"<%}%>>
					<%=VBALANCE_AMT.elementAt(i)%>
				</td>
			</tr>
			<%} %>
		<%} %>		
		</tbody>
	</table>
</body>
</html>