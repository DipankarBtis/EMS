<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String sel_bu_plant=request.getParameter("sel_bu_plant")==null?"0":request.getParameter("sel_bu_plant");//RG20250922 for adding BU wise filter
String sel_bu_plant_abbr=request.getParameter("sel_bu_plant_abbr")==null?"":request.getParameter("sel_bu_plant_abbr");//RG20250922 for adding BU wise filter

String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

dlng.setCallFlag("DLNG_ALLOCATION_TO_CUSTOMER");
dlng.setComp_cd(owner_cd);
dlng.setFrom_dt(from_dt);
dlng.setTo_dt(to_dt);
dlng.setCounterparty_cd(counterparty_cd);
dlng.setBu_plant(sel_bu_plant);//RG20250922 for adding BU filter
dlng.init();

Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_PLANT_SEQ = dlng.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_NM = dlng.getVCOUNTERPARTY_PLANT_NM();

Vector VGAS_DT = dlng.getVGAS_DT();
Vector VQTY_MMBTU = dlng.getVQTY_MMBTU();
Vector VQTY_SCM = dlng.getVQTY_SCM();
Vector VTOTAL_QTY_MMBTU = dlng.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_SCM = dlng.getVTOTAL_QTY_SCM();

Vector VINDEX = dlng.getVINDEX();

String file_nm="Dlng_AllocationToCustomer_"+to_dt.replaceAll("/", "")+".xls";
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "inline; filename="+file_nm);  
%>
<font size="4"><b>DLNG Customer Allocation (By Plant)  From <%=from_dt%> To <%=to_dt%></b></font>
<br>
<%if(!sel_bu_plant.equals("0") && (!sel_bu_plant.equals(""))){ %>
<font size="3"><b>Business Unit: <%=sel_bu_plant_abbr%></b></font>
<%} %>
<br>	
<%if(VCOUNTERPARTY_CD.size()>0){ %>
	<%int k=0,l=0,p=0;
	for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ 
		int index=Integer.parseInt(""+VINDEX.elementAt(i));
		int plant_size=((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).size();
	%>
	<%if(i!=0){ %><br><%} %>
	<font size="3"><b><%=VCOUNTERPARTY_ABBR.elementAt(i)%>&nbsp;-&nbsp;<%=VCOUNTERPARTY_NM.elementAt(i)%></b></font>
	<table border="1">
		<tr>
			<td rowspan="3" align="center"><b>Gas Day</b></td>
			<td rowspan="2" colspan="2" align="center"><b>Total Quantity Supplied</b></td>
			<td colspan="<%=plant_size*2%>" align="center"><b>Total Quantity Supplied To Plant</b></td>
		</tr>
		<tr>
			<%for(int j=0;j<plant_size;j++){ %>
			<td colspan="2" align="center"><b><%=((Vector) VCOUNTERPARTY_PLANT_NM.elementAt(i)).elementAt(j)%></b></td>
			<%} %>
		</tr>
		<tr>
			<td align="center"><b>MMBTU</b></td>
			<td align="center"><b>MT</b></td>
			<%for(int j=0;j<plant_size;j++){ %>
			<td align="center"><b>MMBTU</b></td>
			<td align="center"><b>MT</b></td>
			<%} %>
		</tr>
			
		<%int n=0;
		for(k=k;k<VGAS_DT.size();k++){ 
			n+=1;
		%>
			<tr>
				<td align="center">&nbsp;<%=VGAS_DT.elementAt(k)%>&nbsp;</td>
				<%int m=0;
				for(l=l;l<VQTY_MMBTU.size();l++){ 
					m+=1;
				%>
					<td align="right"><%=VQTY_MMBTU.elementAt(l)%></td>
					<td align="right"><%=VQTY_SCM.elementAt(l)%></td>
					<%if((plant_size+1) == m){
						l++;
						break;
					} %>
				<%} %>
			</tr>
			<%if(index == n){%>
			<tr style="font-weight:bold;">
				<td align="right">Total&nbsp;:&nbsp;</td>
				<%int o=0;
				for(p=p;p<VTOTAL_QTY_MMBTU.size();p++){ 
					o+=1;
				%>
					<td align="right"><%=VTOTAL_QTY_MMBTU.elementAt(p)%></td>
					<td align="right"><%=VTOTAL_QTY_SCM.elementAt(p)%></td>
					<%if((plant_size+1) == o){
						p++;
						break;
					} %>
				<%} %>
			</tr>
				<%k++;
				break;
			} %>
		<%} %>
	</table>
	<%} %>
<%} %>

</body>
</html>