<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<jsp:useBean class="com.etrm.fms.contract_master.DB_ContractMaster_Report" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String agmtType = request.getParameter("agmtType")==null?"0":request.getParameter("agmtType");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
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

contract.setCallFlag("ALL_CONT_STAT_REPORT");
contract.setComp_cd(owner_cd);
contract.setAgmtType(agmtType);
contract.setCounterparty_cd(counterparty_cd);
contract.init();

Vector VMST_COUNTERPARTY_CD = contract.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = contract.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = contract.getVMST_COUNTERPARTY_NM();

Vector VAGMT_NO = contract.getVAGMT_NO();
Vector VAGMT_REV = contract.getVAGMT_REV_NO();
Vector VAGMT_DISP_MAP = contract.getVAGMT_DISP_MAP();
Vector VAGMT_START_DT = contract.getVAGMT_START_DT();
Vector VAGMT_END_DT = contract.getVAGMT_END_DT();
Vector VEXPIRY_STATUS = contract.getVEXPIRY_STATUS();

Vector VCONT_NO = contract.getVCONT_NO();
Vector VCONT_REV_NO = contract.getVCONT_REV_NO();
Vector VCONTRACT_TYPE = contract.getVCONTRACT_TYPE();
Vector VSIGNING_DT = contract.getVSIGNING_DT();
Vector VSTART_DT = contract.getVSTART_DT();
Vector VEND_DT = contract.getVEND_DT();
Vector VCONT_STATUS_FLG = contract.getVCONT_STATUS_FLG();
Vector VCONT_STATUS = contract.getVCONT_STATUS();
Vector VDIS_CONT_MAPPING = contract.getVDIS_CONT_MAPPING();
Vector VINDEX = contract.getVINDEX();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th nowrap="nowrap" style="font-size: 21" colspan="7" rowspan="" align="left">Statistical Report of All Contracts </th>
		</tr>
	</table>
	<table width="100%" border="1">
					<th colspan="7"></th>	
	</table>
	<%if(!agmtType.equals("0")){%>
		<%if(VAGMT_NO.size()>0){%>
			<%int j=0,k=0;
			for(int i=0;i<VAGMT_NO.size();i++){
				int index = Integer.parseInt(""+VINDEX.elementAt(i));
			%>
				<%if(i!=0){%>
					<table width="100%" border="1">
						<th colspan="7"></th>	
					</table>
				<%} %>
				<table width="100%" border="1">
					<th colspan="7"><%=VAGMT_DISP_MAP.elementAt(i)%>&nbsp;&nbsp;<%if(!agmtType.equals("T")){%>(Rev no:<%=VAGMT_REV.elementAt(i)%>)<%} %>&nbsp;&nbsp;&nbsp;[<%=VAGMT_START_DT.elementAt(i)%>-<%=VAGMT_END_DT.elementAt(i)%>]</th>	
				</table>
				<div class="row">
	   				<div class="table-responsive">
	   					<table width="100%" border="1">
	   						<thead>
	   							<tr>
	   								<th>Sr#</th>
	   								<th>Contract#</th>
	   								<th>Contract Rev. No.</th>
	   								<th>Signing Date</th>
	   								<th>Start Date</th>
	   								<th>End Date</th>
	   								<th>Status</th>
	   							</tr>
	   						</thead>
	   						<tbody>
	   							<%k=0;
	   							if(index>0){
	   							%>
	   								<%for(j=j;j<VCONT_NO.size();j++){
	   								k+=1;%>
		    							<tr>
		    								<td><%=k%></td>
		    								<td><%=VDIS_CONT_MAPPING.elementAt(j) %></td>
		    								<td><%=VCONT_REV_NO.elementAt(j) %></td>
		    								<td><%=VSIGNING_DT.elementAt(j) %></td>
		    								<td><%=VSTART_DT.elementAt(j) %></td>
		    								<td><%=VEND_DT.elementAt(j) %></td>
		    								<td><%=VCONT_STATUS.elementAt(j) %></td>
		    							</tr>
	   									<%if(k==index){
	   										j=j+1;
											break;
	   									}%>
	   								<%} %>
	   							<%}else{%>
	   								<tr>
	    								<td colspan="7" align="center"><b>No Contract is Available!</b></td>
	   								</tr>
	   							<%}%>
	   						</tbody>
	   					</table>
	   				</div>
	   			</div>
				<%} %>
			<%}else{%>
				 <div class="row">
				 	<div class="col-md-12 col-sm-12 col-xs-12">
				 		<table width="100%" border="1">
							<tr>
								<th nowrap="nowrap" style="font-size: 21" colspan="7" rowspan="" align="left"><b>No Agreement is Available!</b> </th>
							</tr>
						</table>
				 	</div>
				 </div>
			<%} %>
		<%}else{%>
				 <div class="row">
				 	<div class="col-md-12 col-sm-12 col-xs-12">
				 	<table width="100%" border="1">
							<tr>
								<th nowrap="nowrap" style="font-size: 21" colspan="7" rowspan="" align="left"><b>Please select any Agreement!</b></th>
							</tr>
						</table>
				 	</div>
				 </div>
		<%} %>
</body>
</html>