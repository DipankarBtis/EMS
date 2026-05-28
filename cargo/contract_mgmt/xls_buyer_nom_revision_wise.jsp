<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String nextdate = utildate.getNextDate();

String gas_dt = request.getParameter("gas_dt")==null?nextdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "-1");

String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

cont_mgmt.setCallFlag("BUYER_NOM_REV_WISE");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setGas_dt(gas_dt);
cont_mgmt.init();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VTRANSPORTER_CD = cont_mgmt.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = cont_mgmt.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_PLANT_SEQ = cont_mgmt.getVTRANSPORTER_PLANT_SEQ();
Vector VTRANSPORTER_PLANT_ABBR = cont_mgmt.getVTRANSPORTER_PLANT_ABBR();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = cont_mgmt.getVCOUNTERPARTY_PLANT_ABBR();
Vector VBU_CD = cont_mgmt.getVBU_CD();
Vector VBU_PLANT_SEQ = cont_mgmt.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = cont_mgmt.getVBU_PLANT_ABBR();

Vector VNOM_REV_NO = cont_mgmt.getVNOM_REV_NO();
Vector VGEN_TIME = cont_mgmt.getVGEN_TIME();
Vector VGEN_DT = cont_mgmt.getVGEN_DT();
Vector VBASE = cont_mgmt.getVBASE();
Vector VGCV = cont_mgmt.getVGCV();
Vector VNCV = cont_mgmt.getVNCV();
Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();
Vector VQTY_SCM = cont_mgmt.getVQTY_SCM();
//Vector VNOM_COLOR = cont_mgmt.getVNOM_COLOR();
Vector VDCQ = cont_mgmt.getVDCQ();
Vector VMDCQ_QTY = cont_mgmt.getVMDCQ_QTY();
Vector VCONT_NAME = cont_mgmt.getVCONT_NAME();
Vector VCONT_REF = cont_mgmt.getVCONT_REF();

//Vector VINTERNAL_MAP_ID = cont_mgmt.getVINTERNAL_MAP_ID();
Vector VTAX_DTL = cont_mgmt.getVTAX_DTL();

Vector VCONT_NO = cont_mgmt.getVCONT_NO();
Vector VCONT_REV_NO = cont_mgmt.getVCONT_REV_NO();
Vector VAGMT_NO = cont_mgmt.getVAGMT_NO();
Vector VAGMT_REV_NO = cont_mgmt.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = cont_mgmt.getVCONTRACT_TYPE();
Vector VDIS_CONT_MAPPING = cont_mgmt.getVDIS_CONT_MAPPING();
//Vector VTRANS_PLANT_WISE_TOTAL_MMBTU = cont_mgmt.getVTRANS_PLANT_WISE_TOTAL_MMBTU();
//Vector VTRANS_PLANT_WISE_TOTAL_SCM = cont_mgmt.getVTRANS_PLANT_WISE_TOTAL_SCM();

Vector VINDEX = cont_mgmt.getVINDEX();
Vector VSUB_INDEX = cont_mgmt.getVSUB_INDEX();

String file_nm="BuyerNomination_RevisionWise_"+gas_dt.replaceAll("/", "")+".xls";
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "inline; filename="+file_nm);
    
%>
<table border="1">
<tr>
	<td align="left" colspan="13">
		<font size="4"><b>Buyer Nomination Revision Wise - Gas Day <%=gas_dt%></b></font>
	</td>
</tr>
<%if(VTRANSPORTER_CD.size()>0)
{ %>	
	<%int j=0,k=0,l=0,m=0;
	for(int i=0; i<VTRANSPORTER_CD.size(); i++)
	{ 
		String trans_cd=""+VTRANSPORTER_CD.elementAt(i);
		int index=Integer.parseInt(""+VINDEX.elementAt(i));
	%>
		<%k=0;
		if(index > 0){ %>
			<%for(j=j;j<VTRANSPORTER_PLANT_SEQ.size(); j++) 
			{
				String trans_plant_seq=""+VTRANSPORTER_PLANT_SEQ.elementAt(j);
				int sub_index = Integer.parseInt(""+VSUB_INDEX.elementAt(j));
				k+=1;
			%>
				<tr>
					<td colspan="13"></td>
				</tr>
				<tr>
					<td colspan="13"><b><%=VTRANSPORTER_ABBR.elementAt(i)%>&nbsp;-&nbsp;<%=VTRANSPORTER_PLANT_ABBR.elementAt(j)%></b></td>
				</tr>
				<tr>
					<th rowspan="2">Gen Day</th>
					<th rowspan="2">Gen Time</th>
					<th rowspan="2">Rev#</th>
					<th rowspan="2">Customer</th>
					<th rowspan="2">Customer Plant</th>
					<th rowspan="2">Business Unit</th>
					<th rowspan="2">Contract#</th>
					<th rowspan="2">Contract/Trade Ref#</th>
					<th rowspan="2">DCQ</th>
					<th rowspan="2">Energy (MMBTU)</th>
					<th rowspan="2">Energy (SCM)</th>
					<th colspan="2">Calorific Value Base(KCal/SCM)</th>																
				</tr>
				<tr>
					<th>GCV</th>
					<th>NCV</th>
				</tr>
					<%m=0;
					if(sub_index>0){ %>
						<%for(l=l; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
						{ 
							m+=1;
						%>
							<tr>
								<td align="center">&nbsp;<%=VGEN_DT.elementAt(l) %>&nbsp;</td>
								<td align="center"><%=VGEN_TIME.elementAt(l) %></td>
								<td align="center"><b><%=VNOM_REV_NO.elementAt(l) %></b></td>
								<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(l)%></td>
								<td align="center"><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%></td>
								<td align="center"><%=VBU_PLANT_ABBR.elementAt(l)%></td>
								<td align="center"><%=VDIS_CONT_MAPPING.elementAt(l)%></td>
								<td align="center">&nbsp;<%=VCONT_REF.elementAt(l)%></td>
								<td align="right"><%=VDCQ.elementAt(l) %></td>																			
								<td align="right"><%=VQTY_MMBTU.elementAt(l) %></td>
								<td align="right"><%=VQTY_SCM.elementAt(l) %></td>
								<td align="right"><%=VGCV.elementAt(l)%></td>
								<td align="right"><%=VNCV.elementAt(l)%></td>
							</tr>
							
							<%if(m==sub_index)
							{%>
								<%l=l+1;
								break;
							}%>
						<%} %>
					<%} %>		
				<%if(k==index)
				{
					j=j+1;
					break;
				}%>
			<%} %>
		<%} %>
		
	<%} %>
<%}else{ %>
	<tr>
		<td colspan="12">
			<div align="center">
			<b>No Buyer Nomination Done for Selected Gas Day!</b>
			</div>
		</td>
	</tr>
<%} %>
</table>

</form>
</body>
</html>