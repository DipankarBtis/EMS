<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="cont_mgmt" scope="request"></jsp:useBean>
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

cont_mgmt.setCallFlag("ALLOCATION_TO_CUSTOMER");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setFrom_dt(from_dt);
cont_mgmt.setTo_dt(to_dt);
cont_mgmt.setCounterparty_cd(counterparty_cd);
cont_mgmt.setBu_plant(sel_bu_plant);//RG20250922 for adding BU filter
cont_mgmt.init();

Vector VMST_COUNTERPARTY_CD = cont_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = cont_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = cont_mgmt.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = cont_mgmt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_NM = cont_mgmt.getVCOUNTERPARTY_PLANT_NM();

Vector VGAS_DT = cont_mgmt.getVGAS_DT();
Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();
Vector VQTY_SCM = cont_mgmt.getVQTY_SCM();
Vector VTOTAL_QTY_MMBTU = cont_mgmt.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_SCM = cont_mgmt.getVTOTAL_QTY_SCM();

Vector VINDEX = cont_mgmt.getVINDEX();
Vector VINDEX1 = cont_mgmt.getVINDEX1();

Vector VSEGMENT = cont_mgmt.getVSEGMENT();

String file_nm="AllocationToCustomer_"+to_dt.replaceAll("/", "")+".xls";
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "inline; filename="+file_nm);  
%>
<font size="4"><b>Customer Allocation (By Plant)     From <%=from_dt%> To <%=to_dt%></b></font>
<br>
<%if(!sel_bu_plant.equals("0") && (!sel_bu_plant.equals(""))){ %>
<font size="3"><b>Business Unit: <%=sel_bu_plant_abbr%></b></font>
<%} %>
<br>	
<%if(VCOUNTERPARTY_CD.size()>0){ %>
	<%int k=0,l=0,p=0;
	for(int a=0;a<VSEGMENT.size();a++){ 
		int index1=Integer.parseInt(""+VINDEX1.elementAt(a));
	%>
		<font size="3"><b><%=VSEGMENT.elementAt(a)%></b></font>
		<br>
		<%
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
				<td align="center"><b>SCM</b></td>
				<%for(int j=0;j<plant_size;j++){ %>
				<td align="center"><b>MMBTU</b></td>
				<td align="center"><b>SCM</b></td>
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
<%} %>

</body>
</html>