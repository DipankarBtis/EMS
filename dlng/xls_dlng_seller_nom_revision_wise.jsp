<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String nextdate = utildate.getNextDate();

String gas_dt = request.getParameter("gas_dt")==null?nextdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "-1");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

dlng.setCallFlag("DLNG_SELLER_NOM_REV_WISE");
dlng.setComp_cd(owner_cd);
dlng.setGas_dt(gas_dt);
dlng.setCounterparty_cd(counterparty_cd);
dlng.init();

Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VTRANSPORTER_CD = dlng.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = dlng.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_TRUCK = dlng.getVTRANSPORTER_TRUCK();
Vector VTRANSPORTER_TRUCK_NO = dlng.getVTRANSPORTER_TRUCK_NO();
Vector VCOUNTERPARTY_PLANT_SEQ = dlng.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = dlng.getVCOUNTERPARTY_PLANT_ABBR();
Vector VBU_CD = dlng.getVBU_CD();
Vector VBU_PLANT_SEQ = dlng.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = dlng.getVBU_PLANT_ABBR();

Vector VNOM_REV_NO = dlng.getVNOM_REV_NO();
Vector VGEN_TIME = dlng.getVGEN_TIME();
Vector VGEN_DT = dlng.getVGEN_DT();
Vector VBASE = dlng.getVBASE();
Vector VGCV = dlng.getVGCV();
Vector VNCV = dlng.getVNCV();
Vector VQTY_MMBTU = dlng.getVQTY_MMBTU();
Vector VQTY_SCM = dlng.getVQTY_SCM();
//Vector VNOM_COLOR = dlng.getVNOM_COLOR();
Vector VDCQ = dlng.getVDCQ();
Vector VMDCQ_QTY = dlng.getVMDCQ_QTY();
Vector VCONT_NAME = dlng.getVCONT_NAME();
Vector VCONT_REF = dlng.getVCONT_REF();

Vector VCONT_NO = dlng.getVCONT_NO();
Vector VCONT_REV_NO = dlng.getVCONT_REV_NO();
Vector VAGMT_NO = dlng.getVAGMT_NO();
Vector VAGMT_REV_NO = dlng.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = dlng.getVCONTRACT_TYPE();
Vector VDIS_CONT_MAPPING = dlng.getVDIS_CONT_MAPPING();

Vector VINDEX = dlng.getVINDEX();
Vector VSUB_INDEX = dlng.getVSUB_INDEX();
Vector VBUYER_NOM_REV_NO = dlng.getVBUYER_NOM_REV_NO();
Vector VBUYER_NOM = dlng.getVBUYER_NOM();
Vector VCONTRACT_TYPE_NM = dlng.getVCONTRACT_TYPE_NM();

String file_nm="Dlng_SellerNomination_RevisionWise_"+gas_dt.replaceAll("/", "")+".xls";
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "inline; filename="+file_nm);
    
%>
<table border="1">
<tr>
	<td align="left" colspan="15">
		<font size="4"><b>DLNG Seller Nomination Revision Wise - Gas Day <%=gas_dt%></b></font>
	</td>
</tr>
</table>
<%if(VCOUNTERPARTY_CD.size()>0)
{ %>	
	<%int j=0,k=0,l=0,m=0;
	for(int i=0; i<VCOUNTERPARTY_CD.size(); i++)
	{ 
		
		String cp_cd=""+VCOUNTERPARTY_CD.elementAt(i);
		int index=Integer.parseInt(""+VINDEX.elementAt(i));
	%>
		<%k=0;
		if(index > 0){ %>
			<table border="1">
				<tr>
					<td colspan="15"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
				</tr>
			</table>
			<table border="1">
				<thead>
					<tr>
						<th rowspan="2">Gen Day</th>
						<th rowspan="2">Gen Time</th>
						<th rowspan="2">Rev#</th>
						<th rowspan="2">Transporter</th>
						<th rowspan="2">Truck#</th>
						<th rowspan="2">Customer Plant</th>
						<th rowspan="2">Business Unit</th>
						<th rowspan="2">Contract Type</th>
						<th rowspan="2">Contract#<br>[Contract/Trade Ref#]</th>
						<th rowspan="2">DCQ</th>
						<th rowspan="2">Buyer Nomination (Rev)<br>(MMBTU)</th>
						<th rowspan="2">Energy (MMBTU)</th>
						<th rowspan="2">Energy (MT)</th>
						<th colspan="2">Calorific Value Base<br>KCal/SCM</th>																
					</tr>
					<tr>
						<th>GCV</th>
						<th>NCV</th>
					</tr>
				</thead>
				<tbody>
					<%m=0;
					if(index>0){ %>
						<%for(l=l; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
						{ 
							m+=1;
						%>
							<tr>
								<td align="center"><%=VGEN_DT.elementAt(l) %></td>
								<td align="center"><%=VGEN_TIME.elementAt(l) %></td>
								<td align="center"><b><%=VNOM_REV_NO.elementAt(l) %></b></td>
								<td align="center"><%=VTRANSPORTER_ABBR.elementAt(l)%></td>
								<td align="center"><%=VTRANSPORTER_TRUCK_NO.elementAt(l)%></td>
								<td align="center"><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%></td>
								<td align="center"><%=VBU_PLANT_ABBR.elementAt(l)%></td>
								<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(l)%></td>
								<td>
									<%=VDIS_CONT_MAPPING.elementAt(l)%>
									<%if(!VCONT_REF.elementAt(l).equals("")){%>
										<br>(<%=VCONT_REF.elementAt(l)%>)
									<%} %>
								</td>
								<td align="right"><%=VDCQ.elementAt(l) %></td>
								<td align="right"><%=VBUYER_NOM.elementAt(l)%>&nbsp;<%if(!VBUYER_NOM_REV_NO.elementAt(l).equals("")){ %>(<%=VBUYER_NOM_REV_NO.elementAt(l)%>)<%} %></td>																			
								<td align="right"><%=VQTY_MMBTU.elementAt(l) %></td>
								<td align="right"><%=VQTY_SCM.elementAt(l) %></td>
								<td align="right"><%=VGCV.elementAt(l)%></td>
								<td align="right"><%=VNCV.elementAt(l)%></td>
							</tr>
							
							<%if(m==index)
							{%>
								<%l=l+1;
								break;
							}%>
						<%} %>
					<%} %>
				</tbody>
			</table>
			<table border="1">
				<tr>
					<td colspan="15"></td>
				</tr>
			</table>
		<%} %>
	<%} %>
<%}else{ %>
	<table><tr><td colspan="15"><b>No Seller Nomination Done for Selected Gas Day!</b></td></tr></table>
<%} %>
</body>
</html>