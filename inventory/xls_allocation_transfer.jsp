<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_EnergyBank" id="energyBank" scope="request"></jsp:useBean>
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
String sysdate=utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

energyBank.setCallFlag("ALLOCATION_TRANSFER_REPORT");
energyBank.setComp_cd(owner_cd);
energyBank.setFrom_Dt(from_dt);
energyBank.setTo_Dt(to_dt);
energyBank.init();

Vector VCOUNTERPARTY_CD = energyBank.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = energyBank.getVCOUNTERPARTY_NM();
Vector VSEL_AGMT_NO = energyBank.getVSEL_AGMT_NO();
Vector VSEL_AGMT_REV_NO = energyBank.getVSEL_AGMT_REV_NO();
Vector VSEL_CONT_NO = energyBank.getVSEL_CONT_NO();
Vector VSEL_CONT_REV_NO = energyBank.getVSEL_CONT_REV_NO();
Vector VSEL_CONT_TYPE = energyBank.getVSEL_CONT_TYPE();
Vector VSEL_CONT_TYPE_NM = energyBank.getVSEL_CONT_TYPE_NM();
Vector VCUST_DISPLAY_CONT_DTL = energyBank.getVCUST_DISPLAY_CONT_DTL();
Vector VSOURCE_CARGO_DTL = energyBank.getVSOURCE_CARGO_DTL();
Vector VDESTINATION_CARGO_DTL = energyBank.getVDESTINATION_CARGO_DTL();
Vector VALLOC_QTY_TO_CUST = energyBank.getVALLOC_QTY_TO_CUST();
Vector VTRANSFER_TYPE = energyBank.getVTRANSFER_TYPE();
Vector VENT_DT = energyBank.getVENT_DT();
Vector VEMP_NM = energyBank.getVEMP_NM();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="9" rowspan="2" align="left">Allocation Transfer Report</th>
		</tr>
	</table >
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>Sr#</th>
				<th>Counter Party</th>
				<th>Contract#</th>
				<th>Contract Type</th>
				<th>Source Cargo#</th>
				<th>Destination Cargo#</th>
				<th>Volume Transferred</th>
				<th>Transferred Type</th>
				<th>Modified On</th>
				<th>Modified By</th>
			</tr>
		</thead>
		<tbody>
			<%if(VCOUNTERPARTY_CD.size()>0){
				for(int i=0;i<VCOUNTERPARTY_CD.size();i++){%>
				<tr>
					<td align="center"><%=i+1 %></td>
					<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
					<td align="center"><%=VCUST_DISPLAY_CONT_DTL.elementAt(i) %></td>
					<td align="center"><%=VSEL_CONT_TYPE_NM.elementAt(i) %></td>
					<td align="center"><%=VSOURCE_CARGO_DTL.elementAt(i) %></td>
					<td align="center"><%=VDESTINATION_CARGO_DTL.elementAt(i) %></td>
					<td align="center"><%=VALLOC_QTY_TO_CUST.elementAt(i) %></td>
					<td align="center"><%=VTRANSFER_TYPE.elementAt(i) %></td>
					<td align="center"><%=VENT_DT.elementAt(i) %></td>
					<td align="center"><%=VEMP_NM.elementAt(i) %></td>
				</tr>
			<%} 
			}else{%>
				<tr>
					<td colspan="9" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
				</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>