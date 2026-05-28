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
String mmbtu_sign = request.getParameter("mmbtu_sign")==null?">":request.getParameter("mmbtu_sign");

energyBank.setCallFlag("ENERGY_BANK");
energyBank.setComp_cd(owner_cd);
energyBank.setBalanceMMBTURange(mmbtu_sign);
energyBank.init();

String comp_abbr = energyBank.getComp_Abbr();

Vector VINDEX = energyBank.getVINDEX();
Vector VCARGO_POOL_FLAG = energyBank.getVCARGO_POOL_FLAG();
Vector VCARGO_POOL_NM = energyBank.getVCARGO_POOL_NM();

Vector VCOUNTERPARTY_CD = energyBank.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = energyBank.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = energyBank.getVCOUNTERPARTY_ABBR();
Vector VCONT_NO = energyBank.getVCONT_NO();
Vector VCONT_REV_NO = energyBank.getVCONT_REV_NO();
Vector VCONT_NAME = energyBank.getVCONT_NAME();
Vector VSTART_DT = energyBank.getVSTART_DT();
Vector VEND_DT = energyBank.getVEND_DT();
Vector VRATE = energyBank.getVRATE();
Vector VRATE_UNIT = energyBank.getVRATE_UNIT();
Vector VRATE_UNIT_NM = energyBank.getVRATE_UNIT_NM();
Vector VCONT_STATUS = energyBank.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = energyBank.getVCONT_STATUS_FLG();
Vector VPRICE_TYPE = energyBank.getVPRICE_TYPE();
Vector VBOOKED_QTY = energyBank.getVBOOKED_QTY();
Vector VAGMT_NO = energyBank.getVAGMT_NO();
Vector VAGMT_REV_NO = energyBank.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = energyBank.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = energyBank.getVCONTRACT_TYPE_NM();
Vector VMIN_ALLOC_DT = energyBank.getVMIN_ALLOC_DT();
Vector VMAX_ALLOC_DT = energyBank.getVMAX_ALLOC_DT();
Vector VUNLOADED_QTY = energyBank.getVUNLOADED_QTY();
Vector VUNLOADED_QTY_INFO = energyBank.getVUNLOADED_QTY_INFO();
Vector VAVAIL_FOR_SALE_QTY = energyBank.getVAVAIL_FOR_SALE_QTY();
Vector VAVAIL_FOR_SALE_QTY_INFO = energyBank.getVAVAIL_FOR_SALE_QTY_INFO();
Vector VALLOCATED_QTY = energyBank.getVALLOCATED_QTY();
Vector VBALANCE_QTY = energyBank.getVBALANCE_QTY();
Vector VBALANCE_QTY_INFO = energyBank.getVBALANCE_QTY_INFO();
Vector VCONT_REF = energyBank.getVCONT_REF();
Vector VREMARK = energyBank.getVREMARK();

Vector VCARGO_NO = energyBank.getVCARGO_NO();
Vector VPURCHASE_MAP_ID = energyBank.getVPURCHASE_MAP_ID();
Vector VCARGO_STATUS_FLG = energyBank.getVCARGO_STATUS_FLG();

Vector VTOTAL_BALANCE_MMBTU = energyBank.getVTOTAL_BALANCE_MMBTU();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="18" rowspan="2" align="left">Energy Bank Details</th>
		</tr>
	</table>
	<%int i=0;int k=0, ctn=0;
		for(int j=0; j<VCARGO_POOL_FLAG.size(); j++){ 
		int index = Integer.parseInt(""+VINDEX.elementAt(j));
		String tbl_id = "tbl_"+VCARGO_POOL_FLAG.elementAt(j);
	%>
	<%if(j!=0)
	{%>
		&nbsp;
	<%} %>
		<table width="100%" border="1">
			<thead>
				<tr bgcolor="gold">
					<th align="left" colspan=<%if(VCARGO_POOL_FLAG.elementAt(j).equals("S")||VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>"6"<%}else{%>"18"<%} %>>
					 <%=VCARGO_POOL_NM.elementAt(j) %>&nbsp;<font color="blue">(<%=VTOTAL_BALANCE_MMBTU.elementAt(ctn++) %>)</font>
					</th>
				</tr>
				<tr>
					<th>Sr#</th>
					<%if(!VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>
						<th>Contract Type</th>
					<%}%>
					<%if((!VCARGO_POOL_FLAG.elementAt(j).equals("S"))&&(!VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
						<th>Counterparty</th>
						<th>Contract/Cargo/Trade Ref#</th>
						<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> Balance MMBTU</th>
						<th>MMBTU Unloaded <%if(!VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">(& Projected)</font><br><%} %></th>
						<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Avail for Sale </th>
						<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Allocated</th>
						<th>MMBTU Booked</th>
					<%} %>
					<th>
						<%if(VCARGO_POOL_FLAG.elementAt(j).equals("S")){%>
							Pseudo Cargo#
						<%}else{%>
							Purchase Contract#
						<%} %>
					</th>
					<%if((!VCARGO_POOL_FLAG.elementAt(j).equals("S"))&&(!VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
						<th>Status</th>
						<th>Contract Period</th>
						<th>Allocation Start Date</th>
						<th>Last Allocation Date</th>
						<th>Price Type</th>
						<th>Currency/MMBTU</th>
						<th>Price</th>
					<%} %>
					<%if((VCARGO_POOL_FLAG.elementAt(j).equals("S"))||(VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
						<%if(VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>
							<th>Projected MMBTU</th>
						<%}else{%>
							<th>MMBTU Booked</th>
						<%}%>
					<%} %>
					<%if(VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>
						<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Avail for Sale </th>
					<%}%>
					<%if((VCARGO_POOL_FLAG.elementAt(j).equals("S"))||(VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
						<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Allocated</th>
						<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> Balance MMBTU</th>
					<%} %>
					<%if((!VCARGO_POOL_FLAG.elementAt(j).equals("S"))&&(!VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%><th>Remark</th><%}%>
				</tr>
			</thead>
			<tbody>
				<%k=0;
				if(index > 0)
				{ %>
					<%for(i=i; i<VCOUNTERPARTY_CD.size(); i++)
					{
						k+=1;
					%>
						<tr>
							<td align="center"><%=k%></td>
							<%if(!VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>
							<td align="center">
								<span
									<%if(VCONTRACT_TYPE.elementAt(i).equals("D")){ %>
				    					style="background: #66ffd9;"
				    				<%}else if(VCONTRACT_TYPE.elementAt(i).equals("N")){ %>
				    					style="background: skyblue;"
				    				<%}else if(VCONTRACT_TYPE.elementAt(i).equals("I")){ %>
				    					style="background: pink;"
				    				<%}else if(VCONTRACT_TYPE.elementAt(i).equals("T")){  %>	
				    					style="background: #E9CCEE;"
				    				<%} %>
								><b><%=VCONTRACT_TYPE_NM.elementAt(i)%></b></span>
							</td>
							<%} %>
							<%if((!VCARGO_POOL_FLAG.elementAt(j).equals("S"))&&(!VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
								<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%> : <%=VCOUNTERPARTY_ABBR.elementAt(i)%>">
									<%=VCOUNTERPARTY_NM.elementAt(i)%>
								</td>
								<td><%=VCONT_REF.elementAt(i)%></td>
								<td align="right" title="<%=VBALANCE_QTY_INFO.elementAt(i) %>" <%if(Double.parseDouble(VBALANCE_QTY.elementAt(i).toString())<0){%> style="color:red;font-weight: bold;"<%}%>>
									<%=VBALANCE_QTY.elementAt(i)%>
								</td>
								<td align="right" <%if(!VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %>title="<%=VUNLOADED_QTY_INFO.elementAt(i)%>"<%}%>><%=VUNLOADED_QTY.elementAt(i)%></td>
								<td align="right" title="<%=VAVAIL_FOR_SALE_QTY_INFO.elementAt(i)%>"><%=VAVAIL_FOR_SALE_QTY.elementAt(i)%></td>
								<td align="right"><%=VALLOCATED_QTY.elementAt(i)%></td>
								<td align="right"><%=VBOOKED_QTY.elementAt(i)%> <%if(VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>&nbsp;&nbsp;<span title="LTCORA changes after <%=sysdate%>" onclick="viewProjectedLTCORADetail();"><i class='fa fa-info-circle fa-lg'></i></span><%} %></td>
							<%} %>
							<td align="center"><%=VPURCHASE_MAP_ID.elementAt(i)%></td>
							<%if((!VCARGO_POOL_FLAG.elementAt(j).equals("S"))&&(!VCARGO_POOL_FLAG.elementAt(j).equals("O"))){ %>
								<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
								<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
								<td align="center"><%=VMIN_ALLOC_DT.elementAt(i) %></td>
								<td align="center"><%=VMAX_ALLOC_DT.elementAt(i) %></td>
								<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
								<td align="center"><%=VRATE_UNIT_NM.elementAt(i)%></td>
								<td align="right"><%=VRATE.elementAt(i)%></td>
							<% }%>
							<%if((VCARGO_POOL_FLAG.elementAt(j).equals("S"))||(VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
								<td align="right"><%=VBOOKED_QTY.elementAt(i)%> <%if(VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>&nbsp;&nbsp;<span title="LTCORA changes after <%=sysdate%>" onclick="viewProjectedLTCORADetail();"><i class='fa fa-info-circle fa-lg'></i></span><%} %></td>
							<%} %>
							<%if(VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>
								<td align="right" title="<%=VAVAIL_FOR_SALE_QTY_INFO.elementAt(i)%>"><%=VAVAIL_FOR_SALE_QTY.elementAt(i)%></td>
							<%}%>
							<%if((VCARGO_POOL_FLAG.elementAt(j).equals("S"))||(VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
								<td align="right"><%=VALLOCATED_QTY.elementAt(i)%></td>
								<td align="right" title="<%=VBALANCE_QTY_INFO.elementAt(i) %>" <%if(Double.parseDouble(VBALANCE_QTY.elementAt(i).toString())<0){%> style="color:red;font-weight: bold;"<%}%>>
									<%=VBALANCE_QTY.elementAt(i)%>
								</td>
							<%} %>
							<%if((!VCARGO_POOL_FLAG.elementAt(j).equals("S"))&&(!VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
								<td><%=VREMARK.elementAt(i)%></td>
							<%}%>
						</tr>
						<%
						if(k==index)
						{
							i=i+1;
							break;
						}%>
					<%} %>
				<%}else{ %>
					<tr>
						<td colspan="20" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
					</tr>
				<%} %>
			</tbody>
		</table>
	<%} %>
	
</html>