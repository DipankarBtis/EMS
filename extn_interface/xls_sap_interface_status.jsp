
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>

<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sap_interface" id="dbmgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdt = utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdt:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdt:request.getParameter("to_dt");
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

dbmgmt.setCallFlag("SAP_INTERFACE_STATUS");
dbmgmt.setComp_cd(owner_cd);
dbmgmt.setFrom_dt(from_dt);
dbmgmt.setTo_dt(to_dt);
dbmgmt.init();

String bu_region = dbmgmt.getBu_Region();
Vector V_BU_ABBR = dbmgmt.getV_BU_ABBR();
Vector V_PLANT_ABBR = dbmgmt.getV_PLANT_ABBR();
Vector V_FMS_REF = dbmgmt.getV_FMS_REF();
Vector V_POST_STATUS = dbmgmt.getV_POST_STATUS();
Vector V_POST_DT=dbmgmt.getV_POST_DT();
Vector V_POST_TIME=dbmgmt.getV_POST_TIME();
Vector V_IDOC_NO=dbmgmt.getV_IDOC_NO();
Vector V_IDOC_STATUS=dbmgmt.getV_IDOC_STATUS();
Vector V_STATUS_MSG=dbmgmt.getV_STATUS_MSG();
Vector V_DOC_NO=dbmgmt.getV_DOC_NO();
Vector V_COMPANY_CODE=dbmgmt.getV_COMPANY_CODE();
Vector V_FISCAL_YR=dbmgmt.getV_FISCAL_YR();
Vector V_MSG_STATUS=dbmgmt.getV_MSG_STATUS();
Vector V_CONT_NO = dbmgmt.getV_CONT_NO();
Vector V_DUE_DT=dbmgmt.getV_DUE_DT();
Vector V_APPROVE_DT = dbmgmt.getV_APPROVE_DT();
Vector V_APPROVED_BY = dbmgmt.getV_APPROVED_BY();
Vector V_APPROVED_FLAG = dbmgmt.getV_APPROVED_FLAG();
Vector V_NET_PAYABLE_AMT = dbmgmt.getV_NET_PAYABLE_AMT();
Vector V_SAP_POSTED = dbmgmt.getV_SAP_POSTED();
Vector VINDEX = dbmgmt.getVINDEX();
Vector VSAP_INDEX = dbmgmt.getVSAP_INDEX();

Vector VACC_BU_ABBR = dbmgmt.getVACC_BU_ABBR();
Vector VACC_PLANT_ABBR = dbmgmt.getVACC_PLANT_ABBR();
Vector VACC_FMS_REF = dbmgmt.getVACC_FMS_REF();
Vector VACC_POST_STATUS = dbmgmt.getVACC_POST_STATUS();
Vector VACC_POST_DT=dbmgmt.getVACC_POST_DT();
Vector VACC_POST_TIME=dbmgmt.getVACC_POST_TIME();
Vector VACC_IDOC_NO=dbmgmt.getVACC_IDOC_NO();
Vector VACC_IDOC_STATUS=dbmgmt.getVACC_IDOC_STATUS();
Vector VACC_STATUS_MSG=dbmgmt.getVACC_STATUS_MSG();
Vector VACC_DOC_NO=dbmgmt.getVACC_DOC_NO();
Vector VACC_COMPANY_CODE=dbmgmt.getVACC_COMPANY_CODE();
Vector VACC_FISCAL_YR=dbmgmt.getVACC_FISCAL_YR();
Vector VACC_MSG_STATUS=dbmgmt.getVACC_MSG_STATUS();
Vector VACC_CONT_NO = dbmgmt.getVACC_CONT_NO();
Vector VACC_DUE_DT=dbmgmt.getVACC_DUE_DT();
Vector VACC_APPROVE_DT = dbmgmt.getVACC_APPROVE_DT();
Vector VACC_APPROVED_BY = dbmgmt.getVACC_APPROVED_BY();
Vector VACC_APPROVED_FLAG = dbmgmt.getVACC_APPROVED_FLAG();
Vector VACC_NET_PAYABLE_AMT = dbmgmt.getVACC_NET_PAYABLE_AMT();
Vector VACC_SAP_POSTED = dbmgmt.getVACC_SAP_POSTED();
Vector VACC_INDEX = dbmgmt.getVACC_INDEX();
Vector VACC_SAP_INDEX = dbmgmt.getVACC_SAP_INDEX();

Vector VSAP_INTERFACE_DISPLAY = dbmgmt.getVSAP_INTERFACE_DISPLAY();
Vector VTITLE = dbmgmt.getVTITLE();
Vector VACC_SAP_INTERFACE_DISPLAY = dbmgmt.getVACC_SAP_INTERFACE_DISPLAY();
Vector VACC_TITLE = dbmgmt.getVACC_TITLE();

Vector VMTM_BU_ABBR = dbmgmt.getVMTM_BU_ABBR();
Vector VMTM_PLANT_ABBR = dbmgmt.getVMTM_PLANT_ABBR();
Vector VMTM_FMS_REF = dbmgmt.getVMTM_FMS_REF();
Vector VMTM_POST_STATUS = dbmgmt.getVMTM_POST_STATUS();
Vector VMTM_POST_DT=dbmgmt.getVMTM_POST_DT();
Vector VMTM_POST_TIME=dbmgmt.getVMTM_POST_TIME();
Vector VMTM_IDOC_NO=dbmgmt.getVMTM_IDOC_NO();
Vector VMTM_IDOC_STATUS=dbmgmt.getVMTM_IDOC_STATUS();
Vector VMTM_STATUS_MSG=dbmgmt.getVMTM_STATUS_MSG();
Vector VMTM_DOC_NO=dbmgmt.getVMTM_DOC_NO();
Vector VMTM_COMPANY_CODE=dbmgmt.getVMTM_COMPANY_CODE();
Vector VMTM_FISCAL_YR=dbmgmt.getVMTM_FISCAL_YR();
Vector VMTM_MSG_STATUS=dbmgmt.getVMTM_MSG_STATUS();
Vector VMTM_CONT_NO = dbmgmt.getVMTM_CONT_NO();
Vector VMTM_DUE_DT=dbmgmt.getVMTM_DUE_DT();
Vector VMTM_APPROVE_DT = dbmgmt.getVMTM_APPROVE_DT();
Vector VMTM_APPROVED_BY = dbmgmt.getVMTM_APPROVED_BY();
Vector VMTM_APPROVED_FLAG = dbmgmt.getVMTM_APPROVED_FLAG();
Vector VMTM_NET_PAYABLE_AMT = dbmgmt.getVMTM_NET_PAYABLE_AMT();
Vector VMTM_SAP_POSTED = dbmgmt.getVMTM_SAP_POSTED();
Vector VMTM_INDEX = dbmgmt.getVMTM_INDEX();
Vector VMTM_SAP_INDEX = dbmgmt.getVMTM_SAP_INDEX();
Vector VMTM_SAP_INTERFACE_DISPLAY = dbmgmt.getVMTM_SAP_INTERFACE_DISPLAY();
Vector VMTM_TITLE = dbmgmt.getVMTM_TITLE();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="22" rowspan="2" align="left">SAP Interface Status Report (<%=from_dt%> - <%=to_dt%>)</th>
		</tr>
	</table>
	<br>
	<table width="100%" border="1">
		<tr>
			<th colspan="22" rowspan="2" align="left">Actual</th>
		</tr>
	</table>
	<br>
	<!-- Pratham Bhatt for optimization -->
	
		<%
		int ctn = 0;
		int l=0;
		int k=0;
		for(int i=0; i<VSAP_INTERFACE_DISPLAY.size();i++){ 
			for(int j=0; j<Integer.parseInt(""+VSAP_INDEX.elementAt(i));j++) {
		%>
	<table width="100%" border="1">
			<tr>
				<th colspan="22" rowspan="1" align="left"><%=VTITLE.elementAt(ctn++)%> (<%=VSAP_INTERFACE_DISPLAY.elementAt(i) %>)</th>
			</tr>
	</table>	
	<table width="100%" border="1">
		<thead>
			<tr>
				<th align="center">Sr#</th>
				<th align="center">Doc Num</th>
				<th align="center">Deal Num</th>
				<th align="center">Internal Legal Entity</th>
				<th align="center">External Legal Entity</th>
				<th align="center">Doc Status</th>
				<th align="center">Payment Due Date</th>
				<th align="center">Settle Date</th>
				<th align="center">Settled By</th>
				<th align="center">Invoice Type</th>
				<th align="center">Amount</th>
				<th align="center">Posted to AM</th>
				<th align="center">Region</th>
				<th align="center">Document Type</th>
				<th align="center">Posting Date</th>
				<th align="center">Message ID</th>
				<th align="center">Message Status</th>
				<th align="center">Posted to SAP</th>
				<th align="center">SAP Doc Num</th>
				<th align="center">SAP Acknowledgement</th>
				<th align="center">Sap Company Code</th>
				<th align="center">Rep Center</th>
			</tr>
		</thead>
		<tbody>
		<%
		int index = Integer.parseInt(""+VINDEX.elementAt((ctn-1)));
		if(Integer.parseInt(""+VINDEX.elementAt(ctn-1))>0){
		k=0; for(l=l; l<V_FMS_REF.size(); l++)
		{
			k+=1;
		%>
			<tr>
				<td align="center"><%=k%>.</td>
				<td align="center"><%=V_FMS_REF.elementAt(l)%></td>
				<td align="center"><%=V_CONT_NO.elementAt(l)%></td>
				<td align="center"><%=V_BU_ABBR.elementAt(l)%></td> 
				<td align="center"><%=V_PLANT_ABBR.elementAt(l)%></td> 
				<td align="center"><span class="alert alert-success"><b>Approve</b></span></td>
				<td align="center"><%=V_DUE_DT.elementAt(l)%></td>
				<td align="center"><%=V_APPROVE_DT.elementAt(l)%></td>
				<td align="center"><%=V_APPROVED_BY.elementAt(l)%></td>
				<td align="center">Invoiced</td><!-- * For Accrual it will be Accrual else invoice -->
				<td align="right"><%=V_NET_PAYABLE_AMT.elementAt(l)%></td>
				<%if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Receivable")){%>
				<td align="center"<%if(!V_APPROVED_FLAG.elementAt(l).equals("Y")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(V_APPROVED_FLAG.elementAt(l).equals("Y")){%><b>Yes</b><%} %></td>
				<%}else if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Payable")){%>
				<td align="center"<%if(!V_APPROVED_FLAG.elementAt(l).equals("A")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(V_APPROVED_FLAG.elementAt(l).equals("A")){%><b>Yes</b><%} %></td>
				<%}else if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Advance")){%>
				<td align="center"<%if(!V_APPROVED_FLAG.elementAt(l).equals("O")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(V_APPROVED_FLAG.elementAt(l).equals("O")){%><b>Yes</b><%} %></td>
				<%} %>
				<td align="center"><%=bu_region%></td>
				<%if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Receivable")){%>
				<td align="center">X2</td>
				<%}else{%>
				<td align="center">X1</td>
				<%} %>
				<td align="center"><%=V_POST_DT.elementAt(l)%></td>
				<td align="center"><%=V_IDOC_NO.elementAt(l)%></td>
				<td align="center" valign="middle">
				<%if(V_MSG_STATUS.elementAt(l).equals("S")){ %>
				<b>Success</b>
				<%}else if(V_MSG_STATUS.elementAt(l).equals("W")){ %>
				<b>Warning</b>
				<%}else if(V_MSG_STATUS.elementAt(l).equals("E")){ %>
				<b>Error</b>
				<%}else if(V_MSG_STATUS.elementAt(l).equals("I")){ %>
				<b>Info.</b>
				<%}%>
				</td>
				<td align="center"><%=V_SAP_POSTED.elementAt(l)%></td>
				<td align="center"><%=V_DOC_NO.elementAt(l) %></td>
				<td align="center"><%=V_STATUS_MSG.elementAt(l)%></td>
				<td align="center"><%=V_COMPANY_CODE.elementAt(l)%></td>
				<td align="center">SEI</td>
				</tr>
				<%if(k==index){%>
				<%l=l+1;
					break;}
				%>
				<%} %>
				<%}else{ %>
				<tr>
				<td colspan="22" align="center"><%=utilmsg.infoMessage("<b>No SAP Interface Status Data is Available For Receivable!</b>") %></td>
				</tr>
				<%} %>
				</tbody>
		</table>
		<br>
			<%} %>
		<%} %>
		
		<table width="100%" border="1">
			<tr>
				<th colspan="22" rowspan="2" align="left">Accrual</th>
			</tr>
		</table>
		<br>
		<%
		ctn=0;
		l=0;
		k=0;
		for(int i=0; i<VACC_SAP_INTERFACE_DISPLAY.size();i++){%>
		<%
		for(int j=0; j<Integer.parseInt(""+VACC_SAP_INDEX.elementAt(i));j++) {
		%>
		<table width="100%" border="1">
			<tr>
				<th colspan="22" rowspan="1" align="left"><%=VACC_TITLE.elementAt(ctn++)%> (<%=VACC_SAP_INTERFACE_DISPLAY.elementAt(i) %>)</th>
			</tr>
		</table>	
		<table width="100%" border="1">
			<thead>
				<tr>
					<th align="center">Sr#</th>
					<th align="center">Doc Num</th>
					<th align="center">Deal Num</th>
					<th align="center">Internal Legal Entity</th>
					<th align="center">External Legal Entity</th>
					<th align="center">Doc Status</th>
					<th align="center">Payment Due Date</th>
					<th align="center">Settle Date</th>
					<th align="center">Settled By</th>
					<th align="center">Invoice Type</th>
					<th align="center">Amount</th>
					<th align="center">Posted to AM</th>
					<th align="center">Region</th>
					<th align="center">Document Type</th>
					<th align="center">Posting Date</th>
					<th align="center">Message ID</th>
					<th align="center">Message Status</th>
					<th align="center">Posted to SAP</th>
					<th align="center">SAP Doc Num</th>
					<th align="center">SAP Acknowledgement</th>
					<th align="center">Sap Company Code</th>
					<th align="center">Rep Center</th>
				</tr>
			</thead>
			<tbody>
			<%
			int index = Integer.parseInt(""+VACC_INDEX.elementAt((ctn-1)));
			if(Integer.parseInt(""+VACC_INDEX.elementAt(ctn-1))>0){
			k=0; for(l=l; l<VACC_FMS_REF.size(); l++)
			{
				k+=1;
			%>
				<tr>
					<td align="center"><%=k%>.</td>
					<td align="center"><%=VACC_FMS_REF.elementAt(l)%></td>
					<td align="center"><%=VACC_CONT_NO.elementAt(l)%></td>
					<td align="center"><%=VACC_BU_ABBR.elementAt(l)%></td> 
					<td align="center"><%=VACC_PLANT_ABBR.elementAt(l)%></td> 
					<td align="center"><span class="alert alert-success"><b>Approve</b></span></td>
					<td align="center"><%=VACC_DUE_DT.elementAt(l)%></td>
					<td align="center"><%=VACC_APPROVE_DT.elementAt(l)%></td>
					<td align="center"><%=VACC_APPROVED_BY.elementAt(l)%></td>
					<td align="center">Invoiced</td><!-- * For Accrual it will be Accrual else invoice -->
					<td align="right"><%=VACC_NET_PAYABLE_AMT.elementAt(l)%></td>
					<%if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Receivable")){%>
					<td align="center"<%if(!VACC_APPROVED_FLAG.elementAt(l).equals("Y")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(VACC_APPROVED_FLAG.elementAt(l).equals("Y")){%><b>Yes</b><%} %></td>
					<%}else if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Payable")){%>
					<td align="center"<%if(!VACC_APPROVED_FLAG.elementAt(l).equals("A")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(VACC_APPROVED_FLAG.elementAt(l).equals("A")){%><b>Yes</b><%} %></td>
					<%}else if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Advance")){%>
					<td align="center"<%if(!VACC_APPROVED_FLAG.elementAt(l).equals("O")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(VACC_APPROVED_FLAG.elementAt(l).equals("O")){%><b>Yes</b><%} %></td>
					<%} %>
					<td align="center"><%=bu_region%></td>
					<%if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Receivable")){%>
					<td align="center">X4</td>
					<%}else{%>
					<td align="center">X3</td>
					<%} %>
					<td align="center"><%=VACC_POST_DT.elementAt(l)%></td>
					<td align="center"><%=VACC_IDOC_NO.elementAt(l)%></td>
					<td align="center" valign="middle">
					<%if(VACC_MSG_STATUS.elementAt(l).equals("S")){ %>
					<b>Success</b>
					<%}else if(VACC_MSG_STATUS.elementAt(l).equals("W")){ %>
					<b>Warning</b>
					<%}else if(VACC_MSG_STATUS.elementAt(l).equals("E")){ %>
					<b>Error</b>
					<%}else if(VACC_MSG_STATUS.elementAt(l).equals("I")){ %>
					<b>Info.</b>
					<%}%>
					</td>
					<td align="center"><%=VACC_SAP_POSTED.elementAt(l)%></td>
					<td align="center"><%=VACC_DOC_NO.elementAt(l) %></td>
					<td align="center"><%=VACC_STATUS_MSG.elementAt(l)%></td>
					<td align="center"><%=VACC_COMPANY_CODE.elementAt(l)%></td>
					<td align="center">SEI</td>
					</tr>
					<%if(k==index){%>
					<%l=l+1;
						break;}
					%>
					<%} %>
					<%}else{ %>
					<tr>
					<td colspan="22" align="center"><%=utilmsg.infoMessage("<b>No SAP Interface Status Data is Available For Receivable!</b>") %></td>
					</tr>
					<%} %>
					</tbody>
			</table>
			<%} %>
		<%} %>
		
		
		<table width="100%" border="1">
			<tr>
				<th colspan="22" rowspan="2" align="left">MTM</th>
			</tr>
		</table>
		<br>
		<%
		ctn=0;
		l=0;
		k=0;
		for(int i=0; i<VMTM_SAP_INTERFACE_DISPLAY.size();i++){%>
		<%
		for(int j=0; j<Integer.parseInt(""+VMTM_SAP_INDEX.elementAt(i));j++) {
		%>
		<table width="100%" border="1">
			<tr>
				<th colspan="22" rowspan="1" align="left"><%=VMTM_TITLE.elementAt(ctn++)%> (<%=VMTM_SAP_INTERFACE_DISPLAY.elementAt(i) %>)</th>
			</tr>
		</table>	
		<table width="100%" border="1">
			<thead>
				<tr>
					<th align="center">Sr#</th>
					<th align="center">Doc Num</th>
					<th align="center">Deal Num</th>
					<th align="center">Internal Legal Entity</th>
					<th align="center">External Legal Entity</th>
					<th align="center">Doc Status</th>
					<th align="center">Payment Due Date</th>
					<th align="center">Settle Date</th>
					<th align="center">Settled By</th>
					<th align="center">Invoice Type</th>
					<th align="center">Amount</th>
					<th align="center">Posted to AM</th>
					<th align="center">Region</th>
					<th align="center">Document Type</th>
					<th align="center">Posting Date</th>
					<th align="center">Message ID</th>
					<th align="center">Message Status</th>
					<th align="center">Posted to SAP</th>
					<th align="center">SAP Doc Num</th>
					<th align="center">SAP Acknowledgement</th>
					<th align="center">Sap Company Code</th>
					<th align="center">Rep Center</th>
				</tr>
			</thead>
			<tbody>
			<%
			int index = Integer.parseInt(""+VMTM_INDEX.elementAt((ctn-1)));
			if(Integer.parseInt(""+VMTM_INDEX.elementAt(ctn-1))>0){
			k=0; for(l=l; l<VMTM_FMS_REF.size(); l++)
			{
				k+=1;
			%>
				<tr>
					<td align="center"><%=k%>.</td>
					<td align="center"><%=VMTM_FMS_REF.elementAt(l)%></td>
					<td align="center"><%=VMTM_CONT_NO.elementAt(l)%></td>
					<td align="center"><%=VMTM_BU_ABBR.elementAt(l)%></td> 
					<td align="center"><%=VMTM_PLANT_ABBR.elementAt(l)%></td> 
					<td align="center"><span class="alert alert-success"><b>Approve</b></span></td>
					<td align="center"><%=VMTM_DUE_DT.elementAt(l)%></td>
					<td align="center"><%=VMTM_APPROVE_DT.elementAt(l)%></td>
					<td align="center"><%=VMTM_APPROVED_BY.elementAt(l)%></td>
					<td align="center">Invoiced</td><!-- * For Accrual it will be Accrual else invoice -->
					<td align="right"><%=VMTM_NET_PAYABLE_AMT.elementAt(l)%></td>
					<%if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Receivable")){%>
					<td align="center"<%if(!VMTM_APPROVED_FLAG.elementAt(l).equals("Y")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(VMTM_APPROVED_FLAG.elementAt(l).equals("Y")){%><b>Yes</b><%} %></td>
					<%}else if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Payable")){%>
					<td align="center"<%if(!VMTM_APPROVED_FLAG.elementAt(l).equals("A")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(VMTM_APPROVED_FLAG.elementAt(l).equals("A")){%><b>Yes</b><%} %></td>
					<%}else if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Advance")){%>
					<td align="center"<%if(!VMTM_APPROVED_FLAG.elementAt(l).equals("O")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(VMTM_APPROVED_FLAG.elementAt(l).equals("O")){%><b>Yes</b><%} %></td>
					<%} %>
					<td align="center"><%=bu_region%></td>
					<%if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Receivable")){%>
					<td align="center">X4</td>
					<%}else{%>
					<td align="center">X3</td>
					<%} %>
					<td align="center"><%=VMTM_POST_DT.elementAt(l)%></td>
					<td align="center"><%=VMTM_IDOC_NO.elementAt(l)%></td>
					<td align="center" valign="middle">
					<%if(VMTM_MSG_STATUS.elementAt(l).equals("S")){ %>
					<b>Success</b>
					<%}else if(VMTM_MSG_STATUS.elementAt(l).equals("W")){ %>
					<b>Warning</b>
					<%}else if(VMTM_MSG_STATUS.elementAt(l).equals("E")){ %>
					<b>Error</b>
					<%}else if(VMTM_MSG_STATUS.elementAt(l).equals("I")){ %>
					<b>Info.</b>
					<%}%>
					</td>
					<td align="center"><%=VMTM_SAP_POSTED.elementAt(l)%></td>
					<td align="center"><%=VMTM_DOC_NO.elementAt(l) %></td>
					<td align="center"><%=VMTM_STATUS_MSG.elementAt(l)%></td>
					<td align="center"><%=VMTM_COMPANY_CODE.elementAt(l)%></td>
					<td align="center">SEI</td>
					</tr>
					<%if(k==index){%>
					<%l=l+1;
						break;}
					%>
					<%} %>
					<%}else{ %>
					<tr>
					<td colspan="22" align="center"><%=utilmsg.infoMessage("<b>No SAP Interface Status Data is Available For Receivable!</b>") %></td>
					</tr>
					<%} %>
					</tbody>
			</table>
			<%} %>
		<%} %>
</body>
</html>