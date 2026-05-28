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
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String nextdate = utildate.getNextDate();

// Get value after Page Refresh()
String from_dt = request.getParameter("from_dt")==null?nextdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?nextdate:request.getParameter("to_dt");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String transporter_cd=request.getParameter("transporter_cd")==null?"0":request.getParameter("transporter_cd");
String transporter_plant_seq=request.getParameter("transporter_plant_seq")==null?"0":request.getParameter("transporter_plant_seq");
String chk_diff = request.getParameter("chk_diff")==null?"":request.getParameter("chk_diff");
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

//Send Value to Java
cont_mgmt.setCallFlag("NOM_ALLOC");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setFrom_dt(from_dt);
cont_mgmt.setTo_dt(to_dt);
cont_mgmt.setCounterparty_cd(counterparty_cd);
cont_mgmt.setTransporter_cd(transporter_cd);
cont_mgmt.setTransporter_plant_seq(transporter_plant_seq);
cont_mgmt.setChk_diff(chk_diff);
cont_mgmt.init();

// Get Vectors from Java
Vector VMST_COUNTERPARTY_CD = cont_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = cont_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = cont_mgmt.getVMST_COUNTERPARTY_ABBR();

Vector VMST_TRANSPORTER_CD = cont_mgmt.getVMST_TRANSPORTER_CD();
Vector VMST_TRANSPORTER_NM = cont_mgmt.getVMST_TRANSPORTER_NM();
Vector VMST_TRANSPORTER_ABBR = cont_mgmt.getVMST_TRANSPORTER_ABBR();
Vector VMST_TRANSPORTER_PLANT_SEQ = cont_mgmt.getVMST_TRANSPORTER_PLANT_SEQ();
Vector VMST_TRANSPORTER_PLANT_ABBR = cont_mgmt.getVMST_TRANSPORTER_PLANT_ABBR();


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
Vector VDCQ = cont_mgmt.getVDCQ();
Vector VMDCQ_QTY = cont_mgmt.getVMDCQ_QTY();
Vector VCONT_NAME = cont_mgmt.getVCONT_NAME();
Vector VCONT_REF = cont_mgmt.getVCONT_REF();

Vector VTAX_DTL = cont_mgmt.getVTAX_DTL();

Vector VCONT_NO = cont_mgmt.getVCONT_NO();
Vector VCONT_REV_NO = cont_mgmt.getVCONT_REV_NO();
Vector VAGMT_NO = cont_mgmt.getVAGMT_NO();
Vector VAGMT_REV_NO = cont_mgmt.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = cont_mgmt.getVCONTRACT_TYPE();
Vector VDIS_CONT_MAPPING = cont_mgmt.getVDIS_CONT_MAPPING();

Vector VINDEX = cont_mgmt.getVINDEX();
Vector VSUB_INDEX = cont_mgmt.getVSUB_INDEX();
Vector VGAS_DT = cont_mgmt.getVGAS_DT();

Vector VQTY_SELLER_MMBTU = cont_mgmt.getVQTY_SELLER_MMBTU();
Vector VQTY_SELLER_SCM = cont_mgmt.getVQTY_SELLER_SCM();
Vector VQTY_ALLOC_MMBTU = cont_mgmt.getVQTY_ALLOC_MMBTU();
Vector VQTY_ALLOC_SCM = cont_mgmt.getVQTY_ALLOC_SCM();
Vector VCOLOR = cont_mgmt.getVCOLOR();
Vector VCOLOR_ALLOC = cont_mgmt.getVCOLOR_ALLOC();
Vector VAGMT_BASE = cont_mgmt.getVAGMT_BASE();

String file_nm="Seller_vs_buyer_nom_vs_allocation_"+to_dt.replaceAll("/", "")+".xls";
%>
<body>
	<%
	response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "inline; filename="+file_nm);  
	%>	
	<font size="4"><b>Buyer vs Seller Nomination vs Allocation Report From <%=from_dt%> To <%=to_dt%></b></font>
	<br>					  				
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
						<br>
						<font size=3><b><%=VTRANSPORTER_ABBR.elementAt(i)%>&nbsp;:&nbsp;<%=VTRANSPORTER_PLANT_ABBR.elementAt(j)%></b></font>
      					<table border="1">
							<thead>
							<tr>
								<th rowspan="2">Gas Day</th>																	
								<th rowspan="2">Customer</th>
								<th rowspan="2">Customer Plant</th>
								<th rowspan="2">Contract#</th>
								<th rowspan="2">Contract/Trade Ref#</th>
								<th rowspan="2">DCQ</th>
								<th colspan="2">Buyer Nomination</th>	
								<th colspan="2">Seller Nomination</th>
								<th colspan="2">Allocation</th>																
							</tr>
							<tr>
								<th>MMBTU</th>
								<th>SCM</th>
								<th>MMBTU</th>
								<th>SCM</th>
								<th>MMBTU</th>
								<th>SCM</th>
							</tr>
						</thead>
						<tbody>
							<%m=0;
							if(sub_index>0){ %>
								<%for(l=l; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
								{ 
									m+=1;
								%>
									<tr>
										<td align="center"><%=VGAS_DT.elementAt(l) %></td>
										<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(l)%></td>
										<td align="center"><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%></td>
										<td align="center"><%=VDIS_CONT_MAPPING.elementAt(l)%>
											<%if(VAGMT_BASE.elementAt(l).equals("D")){ %>&nbsp;<font style="background:#a6ff4d">(DLV)</font><%} %>
										</td>
										<td align="center">&nbsp;<%=VCONT_REF.elementAt(l)%></td>
										<td align="right"><%=VDCQ.elementAt(l) %></td>																			
										<td align="right"><%=VQTY_MMBTU.elementAt(l) %></td>
										<td align="right"><%=VQTY_SCM.elementAt(l) %></td>
										<td align="right" style="color:<%=VCOLOR.elementAt(l)%>"><%=VQTY_SELLER_MMBTU.elementAt(l) %></td>
										<td align="right"><%=VQTY_SELLER_SCM.elementAt(l) %></td>
										<td align="right" style="color:<%=VCOLOR_ALLOC.elementAt(l)%>"><%=VQTY_ALLOC_MMBTU.elementAt(l) %></td>
										<td align="right"><%=VQTY_ALLOC_SCM.elementAt(l) %></td>
									</tr>
									
									<%if(m==sub_index)
									{%>
										<%l=l+1;
										break;
									}%>
								<%} %>
							<%} %>
						</tbody>
					</table>
					<%if(k==index)
					{
						j=j+1;
						break;
					}%>
				<%} %>
			<%} %>
		<%} %>
	<%}else{ %>
		<%=utilmsg.infoMessage("<b>No Buyer Nomination Done for Selected Gas Day!</b>") %>
	<%} %>				

</body>
</html>