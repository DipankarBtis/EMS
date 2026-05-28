<%@page import= "java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<jsp:useBean class="com.etrm.fms.accounting.DataBean_Accounting" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sysdate=utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
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

contract.setFrom_dt(from_dt);
contract.setComp_cd(owner_cd);
contract.setTo_dt(to_dt);
contract.init();


contract.setCallFlag("DIGITAL_SIGNATURE");
contract.setComp_cd(owner_cd);
contract.setFrom_dt(from_dt);
contract.setTo_dt(to_dt);
contract.init();


Vector VCOUNTERPARTY_CD = contract.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = contract.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = contract.getVCOUNTERPARTY_ABBR();

Vector VINVOICE_NO = contract.getVINVOICE_NO();
Vector VINVOICE_TYPE = contract.getVINVOICE_TYPE();
Vector VINVOICE_DT = contract.getVINVOICE_DT();
Vector VPDF_TYPE = contract.getVPDF_TYPE();
Vector VSIGNED_BY = contract.getVSIGNED_BY();
Vector VENT_BY = contract.getVENT_BY();
Vector VPDF_NAME = contract.getVPDF_NAME();
Vector VCONT_NO = contract.getVCONT_NO();
Vector VCONTRACT_TYPE = contract.getVCONTRACT_TYPE();
Vector VBU_STATE = contract.getVBU_STATE();
Vector VPDF_FILE_PATH = contract.getVPDF_FILE_PATH();
Vector VDEAL_NO = contract.getVDEAL_NO();
Vector VCONT_REF_NO = contract.getVCONT_REF_NO();

%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="13" rowspan="" align="left">Digital Signature Summary Report (Generated For <%=from_dt%> To <%=to_dt%>)</th>
		</tr>
	</table>
	<div class="row">
		<div class="table-responsive">
			<table class="table table-bordered table-hover" width="100%" border="1">
				<thead>
					<tr>
			            <th>Sr#</th>
						<th>Customer</th>
						<th>BU State Name</th>
						<th>Contract Type</th>
						<th>Contract#</th>
						<th>Contract Ref</th>
						<th>PDF Name</th>
						<th>Invoice No.</th>
						<th>Invoice Type</th>
						<th>Invoice Date</th>
						<th>PDF Type</th>
						<th>Signed by</th>
						<th>Entered by</th>
					</tr>
			   </thead>
			   <tbody>
	           <%int k=0;
	          	 if(VCOUNTERPARTY_CD.size() > 0){ %>
					<%for(int i=0;i<VCOUNTERPARTY_CD.size(); i++){ 
						k+=1;%>						
						<tr>
							<td align="center"><%= k %></td>
							<td> <%=VCOUNTERPARTY_NM.elementAt(i)%> </td>
							<td align="center"><%= VBU_STATE.elementAt(i) %></td>
							<td align="center"><%= VCONTRACT_TYPE.elementAt(i) %></td>
							<%if (VINVOICE_TYPE.elementAt(i).equals("DERIVATIVE INVOICE")) { %>
							<td align="center"><div style="width:400px; word-wrap: break-word; white-space: normal;"><%=VDEAL_NO.elementAt(i) %></div></td>
							<%}else{ %>
							<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
							<%} %>
							<td align="center"><%=VCONT_REF_NO.elementAt(i) %></td>
							<td><%= VPDF_NAME.elementAt(i) %></td>
							<td align="center"><%= VINVOICE_NO.elementAt(i) %></td>
							<td align="center" ><%= VINVOICE_TYPE.elementAt(i) %></td>
							<td align="center"><%= VINVOICE_DT.elementAt(i) %></td>
							<td align="center"><%= VPDF_TYPE.elementAt(i) %></td>
							<td align="center"><%= VSIGNED_BY.elementAt(i) %></td>
							<td align="center"><%= VENT_BY.elementAt(i) %></td>	   
						</tr>   
					<%}%>
				<%}else{ %>
					<tr>
						<td colspan="13" align="center"><%=utilmsg.infoMessage("<b>Digital Signature Data Not Available for Selected Period!</b>") %></td>
					</tr>					
				<%} %>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>