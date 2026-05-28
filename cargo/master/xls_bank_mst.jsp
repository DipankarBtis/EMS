<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Harsh Maheta 20230809 : Export to Excel For Bank Master-->
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
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

dbmaster.setCallFlag("BANK_MST");
dbmaster.init();

Vector VBANK_NAME = dbmaster.getVBANK_NAME();
Vector VBANK_CD = dbmaster.getVBANK_CD();
Vector VBANK_BRANCH = dbmaster.getVBANK_BRANCH();
Vector VEFF_DT = dbmaster.getVEFF_DT();
Vector VBANK_ABBR = dbmaster.getVBANK_ABBR();
Vector VINDEX = dbmaster.getVINDEX();
Vector VSTATE_CD =dbmaster.getVSTATE_CD();
Vector VSTATE_NM =dbmaster.getVSTATE_NM();
Vector VCOUNTRY_CD =dbmaster.getVCOUNTRY_CD();
Vector VCOUNTRY_NM =dbmaster.getVCOUNTRY_NM();
Vector VBANK_ADDR = dbmaster.getVBANK_ADDR();
Vector VBANK_CITY = dbmaster.getVBANK_CITY();
Vector VBANK_STATE = dbmaster.getVBANK_STATE();
Vector VBANK_COUNTRY = dbmaster.getVBANK_COUNTRY();
Vector VBANK_PIN = dbmaster.getVBANK_PIN();
Vector VBANK_PHONE = dbmaster.getVBANK_PHONE();
Vector VBANK_MOBILE = dbmaster.getVBANK_MOBILE();
Vector VBANK_ALT_PHONE = dbmaster.getVBANK_ALT_PHONE();
Vector VBANK_FAX1 = dbmaster.getVBANK_FAX1();
Vector VBANK_FAX2 = dbmaster.getVBANK_FAX2();
Vector VBANK_EMAIL = dbmaster.getVBANK_EMAIL();
Vector VBANK_REMARKS = dbmaster.getVBANK_REMARKS();
Vector VBANK_IFSC_CD = dbmaster.getVBANK_IFSC_CD();
Vector VBANK_STATUS_FLAG = dbmaster.getVBANK_STATUS_FLAG();
Vector VBANK_STATE_CD = dbmaster.getVBANK_STATE_CD();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="18" rowspan="2" align="left">Bank Master</th>
		</tr>
	</table>
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>Bank Name</th>
		    	<th>Bank ABBR</th>				    		
		    	<th>Branch Name</th>
		    	<th>Bank IFSC</th>
		    	<th>Status</th>
		    	<th>Eff Date</th>
		    	<th>Addr</th>
		    	<th>City</th>
		    	<th>PIN</th>
		    	<th>State</th>
		    	<th>Country</th>
		    	<th>Phone</th>
		    	<th>Mobile</th>
		    	<th>Email</th>
		    	<th>Alt Phone</th>
		    	<th>Remarks</th>
		    	<th>Fax1</th>
		    	<th>Fax2</th>
			</tr>
		</thead>
		<tbody>
		<%int j=0;int k=0;
		if(VBANK_CD.size()>0){%>
			<%for(int i=0; i<VBANK_CD.size(); i++){ 
			int size = Integer.parseInt(""+VINDEX.elementAt(i));
			%>
				<tr>
					<td align="center"><%=VBANK_NAME.elementAt(i)%></td>
					<td align="center"><%=VBANK_ABBR.elementAt(i)%></td>
					<td align="center"><%=VBANK_BRANCH.elementAt(i)%></td>
					<td align="center"><%=VBANK_IFSC_CD.elementAt(i)%></td>
					<td align="center">
						<%if(VBANK_STATUS_FLAG.elementAt(i).equals("Y")){%>
						Active
						<%}else{ %>
						Inactive
						<%} %>
					</td>
					<td align="center"><%=VEFF_DT.elementAt(i)%></td>
					<td align="center"><%=VBANK_ADDR.elementAt(i)%></td>
					<td align="center"><%=VBANK_CITY.elementAt(i)%></td>
					<td align="center"><%=VBANK_PIN.elementAt(i)%></td>
					<td align="center"><%=VBANK_STATE.elementAt(i)%></td>
					<td align="center"><%=VBANK_COUNTRY.elementAt(i)%></td>
					<td align="center"><%=VBANK_PHONE.elementAt(i)%></td>
					<td align="center"><%=VBANK_MOBILE.elementAt(i)%></td>
					<td align="center"><%=VBANK_EMAIL.elementAt(i)%></td>
					<td align="center"><%=VBANK_ALT_PHONE.elementAt(i)%></td>
					<td align="center"><%=VBANK_REMARKS.elementAt(i)%></td>
					<td align="center"><%=VBANK_FAX1.elementAt(i)%></td>
					<td align="center"><%=VBANK_FAX2.elementAt(i)%></td>
				</tr>
			<%} %>
		<%}else{ %>
			<tr>
				<td colspan="18" align="center"><%=utilmsg.infoMessage("<b>No Bank Details Available!</b>") %></td>
			</tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>