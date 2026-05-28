<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String prevdate = utildate.getPreviousDate();
String sysdate=utildate.getSysdate();

String gas_dt = request.getParameter("gas_dt")==null?prevdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "1");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String counterparty_cd=request.getParameter("counterparty_cd")==null?sysdate:request.getParameter("counterparty_cd");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String bu_seq=request.getParameter("bu_seq")==null?"0":request.getParameter("bu_seq");	// SagarB20250922 Added this variable for showing BU
String bu_abbr=request.getParameter("bu_abbr")==null?"":request.getParameter("bu_abbr");	// SagarB20250922 Added this variable for showing BU
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

cont_mgmt.setCallFlag("DELIVERY_REPORT");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setFrom_dt(from_dt);
cont_mgmt.setTo_dt(to_dt);
cont_mgmt.setCounterparty_cd(counterparty_cd);
cont_mgmt.setBu_seq(bu_seq);		// SagarB20250922 Added this variable for showing BU list
cont_mgmt.init();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = cont_mgmt.getVCOUNTERPARTY_NM();
Vector VTRANSPORTER_CD = cont_mgmt.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = cont_mgmt.getVTRANSPORTER_ABBR();
Vector VCONT_BU_PLANT_SEQ = cont_mgmt.getVCONT_BU_PLANT_SEQ();
Vector VCONT_BU_PLANT_MAP = cont_mgmt.getVCONT_BU_PLANT_MAP();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = cont_mgmt.getVCOUNTERPARTY_PLANT_ABBR();
Vector VTITTLE_DISP_CONT_NO = cont_mgmt.getVTITTLE_DISP_CONT_NO();

Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();
Vector VQTY_MT = cont_mgmt.getVQTY_MT();
Vector VCONT_REF = cont_mgmt.getVCONT_REF();
Vector VBUYER_NOM = cont_mgmt.getVBUYER_NOM();
Vector VSELLER_NOM = cont_mgmt.getVSELLER_NOM();

Vector VINDEX = cont_mgmt.getVINDEX();
Vector VSUB_INDEX = cont_mgmt.getVSUB_INDEX();

Vector VLOAD_START_DT = cont_mgmt.getVLOAD_START_DT();
Vector VLOAD_START_TIME = cont_mgmt.getVLOAD_START_TIME();
Vector VLOAD_END_DT = cont_mgmt.getVLOAD_END_DT();
Vector VLOAD_END_TIME = cont_mgmt.getVLOAD_END_TIME();

Vector VTRUCK_REG_NUM =  cont_mgmt.getVTRUCK_REG_NUM();
Vector VTRUCK_CD =  cont_mgmt.getVTRUCK_CD();
Vector VTRUCK_TRANS_CD =  cont_mgmt.getVTRUCK_TRANS_CD();
Vector VGAS_DT =  cont_mgmt.getVGAS_DT();

Vector VTOTAL_SELLER_MMBTU =  cont_mgmt.getVTOTAL_SELLER_MMBTU();
Vector VTOTAL_BUYER_MMBTU =  cont_mgmt.getVTOTAL_BUYER_MMBTU();
Vector VTOTAL_ALLOC_MMBTU =  cont_mgmt.getVTOTAL_ALLOC_MMBTU();
Vector VTOTAL_ALLOC_MT =  cont_mgmt.getVTOTAL_ALLOC_MT();
Vector VCONT_NAME =  cont_mgmt.getVCONT_NAME();
Vector VBU_PLANT_ABBR =  cont_mgmt.getVBU_PLANT_ABBR();
%>
<body onload="">
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

	<table width="100%" border="1">
		<tr>
			<th colspan="8" rowspan="2" align="left">DLNG Delivery Report (<%=from_dt%> - <%=to_dt%>)</th>
		</tr>
			
		<!-- SagarB20250929 Added this variable for showing BU Abbr in exported excel -->
		<tr></tr>
		<tr>
			<th colspan="8" rowspan="2" align="left">
				<%if(!bu_seq.equals("0") && (!bu_seq.equals(""))){ %>
				<font><b>Business Unit : <%=bu_abbr%></b></font>
				<%} %>
			</th>
		</tr>
		
	</table>
	<%if(VTITTLE_DISP_CONT_NO.size() > 0){ %>
		<%int j=0,k=0,l=0,m=0,p=0,q=0;
		for(int i=0; i<VTITTLE_DISP_CONT_NO.size(); i++)
		{
			String disp_cont=""+VTITTLE_DISP_CONT_NO.elementAt(i);
			int index=Integer.parseInt(""+VINDEX.elementAt(i));
		%>
		<%if(i>0){ %>&nbsp;<%} %>
			<table width="100%" border="1">
				<tr>
					<th colspan="10" rowspan="1" align="left"><%=VTITTLE_DISP_CONT_NO.elementAt(i)%> (<%=VCONT_REF.elementAt(i)%>)</th>
				</tr>
			</table>
				<%k=0;
				if(index > 0){ %>
					<%for(j=j;j<VCONT_BU_PLANT_SEQ.size(); j++) 
					{
						String trans_plant_seq=""+VCONT_BU_PLANT_SEQ.elementAt(j);
						int sub_index = Integer.parseInt(""+VSUB_INDEX.elementAt(j));
						k+=1;
					%>
					
					<table width="100%" border="1">
						<tr>
							<th colspan="10" rowspan="1" align="left"><%=VCONT_BU_PLANT_MAP.elementAt(j).toString().replace("@", " : ")%></th>
						</tr>
					</table>				
					<table width="100%" border="1">
						<thead>
							<tr>
								<th rowspan="2">Gas Date</th>
								<th rowspan="2">Contract#</th>
								<th rowspan="2">Business Unit</th>
								<th rowspan="2">Truck#</th>
								<th colspan="2">Nomination Qty<br>(MMBTU)</th>
								<th colspan="2">Loaded Qty</th>
								<th colspan="2">Arrival</th>
							</tr>
							<tr>
								<th>Buyer Nom</th>
								<th>Seller Nom</th>
								<th>MMBTU</th>
								<th>MT</th>
								<th>Loading Start</th>
								<th>Loading End</th>
							</tr>
						</thead>
						<tbody>
							<%m=0;
							if(sub_index>0)
							{ %>
								<%for(l=l; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
								{
									m+=1;
								%>
									<tr>
										<td align="center">
											<%=VGAS_DT.elementAt(l)%>
										</td>
										<td align="center">
											<%=VCONT_NAME.elementAt(l)%>
										</td>
										<td align="center">
											<%=VBU_PLANT_ABBR.elementAt(l)%>
										</td>
										<td align="center">
											<%=VTRUCK_REG_NUM.elementAt(l)%>
										</td>
										<td align="right"
										<%if(!VSELLER_NOM.elementAt(l).equals("") && VBUYER_NOM.elementAt(l).equals("-")) {%>
										style="color:blue"
										<%} %>
										>
											<%if(!VSELLER_NOM.elementAt(l).equals("") && VBUYER_NOM.elementAt(l).equals("-")) {%>
												<%=VSELLER_NOM.elementAt(l)%>
											<%}else{ %>
												<%=VBUYER_NOM.elementAt(l)%>
											<%} %>
										</td>
										<td align="right">
											<%=VSELLER_NOM.elementAt(l)%>
										</td>
										<td align="right">
											<%=VQTY_MMBTU.elementAt(l)%>
										</td>
										<td align="right">
											<%=VQTY_MT.elementAt(l)%>
										</td>
										<td align="center">
											<div>
												<div>
													<div class="col">
														<%=VLOAD_START_DT.elementAt(l)%>
									      				<%=VLOAD_START_TIME.elementAt(l)%>
						      						</div>
						      					</div>
						      				</div>
										</td>
										<td align="center">
											<div>
												<div>
													<div class="col">
														<%=VLOAD_END_DT.elementAt(l)%>
									      				<%=VLOAD_END_TIME.elementAt(l)%>
						      						</div>
						      					</div>
						      				</div>
										</td>
									</tr>
									
									<%if(m==sub_index)
									{%>
										<%l=l+1;
										break;
									}%>
								<%} %>
								<tr>
									<td colspan="4"><div align="right"><b>Total:</b></div></td>
									<td align="right"><b><%=VTOTAL_BUYER_MMBTU.elementAt(j) %></b></td>
									<td align="right"><b><%=VTOTAL_SELLER_MMBTU.elementAt(j) %></b></td>
									<td align="right"><b><%=VTOTAL_ALLOC_MMBTU.elementAt(j) %></b></td>
									<td align="right"><b><%=VTOTAL_ALLOC_MT.elementAt(j) %></b></td>
									<td colspan="2"></td>
								</tr>
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
		<table width="100%" border="1">
			<tr>
				<th colspan="10" rowspan="1" align="center">
					<b>No Delivery Data available for the Duration!!!</b>
				</th>
			</tr>
		</table>
		<%} %>
</body>
</html>