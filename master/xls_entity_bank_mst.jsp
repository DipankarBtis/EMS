<%@page import= "java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String opration=request.getParameter("opration")==null?"MODIFY":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String entity_role = request.getParameter("entity_role")==null?"C":request.getParameter("entity_role");
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
if(entity_role.equals("B"))
{
	if(!owner_cd.equals(""))
	{
		counterparty_cd=owner_cd;
	}
}

dbcounterpty.setCallFlag("ENTITY_BANK_MASTER");
dbcounterpty.setOpration(opration);
dbcounterpty.setCounterparty_cd(counterparty_cd);
dbcounterpty.setEntity_role(entity_role);
dbcounterpty.setComp_cd(owner_cd);
dbcounterpty.init();

Vector VCOUNTRY_CODE = dbcounterpty.getVCOUNTRY_CODE();
Vector VCOUNTRY_NM = dbcounterpty.getVCOUNTRY_NM();
Vector VISO_CODE = dbcounterpty.getVISO_CODE();
Vector VTIN =dbcounterpty.getVTIN();
Vector VSTATE_CODE = dbcounterpty.getVSTATE_CODE();
Vector VSTATE_NM = dbcounterpty.getVSTATE_NM();

Vector VCOUNTERPARTY_CD = dbcounterpty.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcounterpty.getVCOUNTERPARTY_ABBR();

Vector VNCF_CATEGORY = dbcounterpty.getVNCF_CATEGORY();

String name=dbcounterpty.getName();
String abbr=dbcounterpty.getAbbr();
String eff_dt=dbcounterpty.getEff_dt();

String bank_eff_dt=dbcounterpty.getBank_eff_dt();
String bank_name=dbcounterpty.getBank_name();
String bank_account_no=dbcounterpty.getBank_account_no();
String bank_branch=dbcounterpty.getBank_branch();
String ifsc_code=dbcounterpty.getIfsc_code();
String bank_state=dbcounterpty.getBank_state();
String bank_formula=dbcounterpty.getBank_formula();
String bank_category=dbcounterpty.getBank_category();

Vector VBANK_FORMULA = dbcounterpty.getVBANK_FORMULA();
Vector VBANK_EFF_DT = dbcounterpty.getVBANK_EFF_DT();
Vector VBANK_CATEGORY = dbcounterpty.getVBANK_CATEGORY();
Vector VBANK_NAME = dbcounterpty.getVBANK_NAME();
Vector VBANK_ACCOUNT_NO = dbcounterpty.getVBANK_ACCOUNT_NO();
Vector VIFSC_CODE = dbcounterpty.getVIFSC_CODE();
Vector VBANK_BRANCH = dbcounterpty.getVBANK_BRANCH();
Vector VBANK_STATE = dbcounterpty.getVBANK_STATE();

Vector VMST_COUNTERPARTY_ABBR = dbcounterpty.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = dbcounterpty.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_CD = dbcounterpty.getVMST_COUNTERPARTY_CD();

Vector VSECTOR_CD = dbcounterpty.getVSECTOR_CD();
Vector VSECTOR_NAME = dbcounterpty.getVSECTOR_NAME();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="9" rowspan="2" align="left">Entity Virtual Bank Account Master</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>Sr#</th>
				<th >Counterparty</th>
				<th >Bank Name</th>
				<th >Bank A/C No.</th>
				<th >IFSC | Swift code</th>
				<th >Branch</th>
				<th >State</th>
				<th >Eff Date</th>
				<th >Category</th>
			</tr>
		</thead>
		<tbody>
		<%if(VBANK_FORMULA.size()>0){ %>
		<%for(int i=0;i<VBANK_FORMULA.size(); i++){ %>
			<tr>
				<td align="center"><%=i+1%>.</td>
				<td><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i)%></td>
				<%-- <td><%=VBANK_FORMULA.elementAt(i)%></td> --%>
				<td><%=VBANK_NAME.elementAt(i)%></td>
				<td style="mso-number-format:'\@';"><%=VBANK_ACCOUNT_NO.elementAt(i)%></td>
				<td><%=VIFSC_CODE.elementAt(i)%></td>
				<td><%=VBANK_BRANCH.elementAt(i)%></td>
				<td><%=VBANK_STATE.elementAt(i)%></td>
				<td align="center"><%=VBANK_EFF_DT.elementAt(i)%></td>
				<td align="center">
				<%if(VBANK_CATEGORY.elementAt(i).equals("DERV")){%>
					Derivatives
				<%}else{ %>
					<%=VBANK_CATEGORY.elementAt(i) %>
				<%} %>
				</td>
			</tr>
		<%} %>
		<%}else{ %>
			<tr>
				<td align="center" colspan="9"><%=utilmsg.infoMessage("<b>Bank Detail is Not Config!</b>") %></td>
			</tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>