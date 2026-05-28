<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GTA_Remittance" id="gta" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
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
String sysdate = utildate.getSysdate();
String report_dt=request.getParameter("report_dt")==null?sysdate:request.getParameter("report_dt");
String automationFlag=request.getParameter("automationFlag")==null?"":request.getParameter("automationFlag");
String isGenerateXML=request.getParameter("isGenerateXML")==null?"":request.getParameter("isGenerateXML");

gta.setCallFlag("GTA_ACCRUAL_ACCOUNTING");
gta.setComp_cd(owner_cd);
gta.setReport_dt(report_dt);
gta.setAutomation_flag(automationFlag);
gta.setIsGenerateXML(isGenerateXML);
gta.setRequest(request);
gta.init();

Vector VCOUNTERPTY_CD = gta.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = gta.getVCOUNTERPTY_ABBR();
Vector VCOUNTERPTY_NM = gta.getVCOUNTERPTY_NM();
Vector VPRODUCTION_MONTH = gta.getVPRODUCTION_MONTH();
Vector VSTART_DT = gta.getVSTART_DT();
Vector VEND_DT = gta.getVEND_DT();
Vector VTRANS_BU_SEQ = gta.getVTRANS_BU_SEQ();
Vector VTRANS_BU_ABBR = gta.getVTRANS_BU_ABBR();
Vector VBU_PLANT_SEQ = gta.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = gta.getVBU_PLANT_ABBR();
Vector VPERIOD_START_DT = gta.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = gta.getVPERIOD_END_DT();
Vector VDIS_CONT_MAPPING = gta.getVDIS_CONT_MAPPING();
Vector VCONT_REF_NO = gta.getVCONT_REF_NO();
Vector VBILLING_FREQ_NM = gta.getVBILLING_FREQ_NM();
Vector VINVOICE_DUE_DT = gta.getVINVOICE_DUE_DT();
Vector VACCRUAL_QTY = gta.getVACCRUAL_QTY();
Vector VACCRUAL_AMT = gta.getVACCRUAL_AMT();
Vector VCASH_FLOW_NM = gta.getVCASH_FLOW_NM();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="14" rowspan="2" align="left">Accounting GTA Accrual Report for <%=report_dt%></th>
		</tr>
	</table >
	<table width="100%" border="1">
		<thead>
			<tr>	
				<th>Sr#</th>									
				<th>Transporter</th>
				<th>Contract No</th>
				<th>Contract/Trade Ref#</th>
				<th>Contract Period</th>
				<th>Plant</th>
				<th>Business Unit</th>
				<th>Cash Flow</th>
				<th>Production Month</th>
				<th>Billing Cycle</th>
				<th>Billing Period</th>
				<th>Invoice Due Date</th>
				<th>Accrual MMBTU</th>										
				<th>Accrual Amount <br> (INR)</th>
			</tr>
		</thead>
		<tbody>
		<%if(VCOUNTERPTY_CD.size() > 0){ %>
			<%for(int i=0; i<VCOUNTERPTY_CD.size(); i++){ %>
			<tr>
				<td><%=i+1%></td>
				<td align="center" title="<%=VCOUNTERPTY_NM.elementAt(i)%>">
					<%=VCOUNTERPTY_ABBR.elementAt(i)%>
				</td>
				<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i) %>										
															
				</td>
				<td align="center">
					<%=VCONT_REF_NO.elementAt(i) %>
				</td>
				<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
				<td align="center"><%=VTRANS_BU_ABBR.elementAt(i)%></td>
				<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
				<td align="center"><%=VCASH_FLOW_NM.elementAt(i)%></td>
				<td align="center"><%=VPRODUCTION_MONTH.elementAt(i) %></td>
				<td align="center">
					<span><b><%=VBILLING_FREQ_NM.elementAt(i)%></b></span>																								
				</td>
				<td align="center">
					<%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%>
				</td>
				<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
				<td align="right"><%=VACCRUAL_QTY.elementAt(i) %></td>
				<td align="right"><%=VACCRUAL_AMT.elementAt(i)%></td>									
			</tr>
			<%} %>
		<%}else{ %>
			<tr>
				<td align="center" colspan="14"><b>No Accrual Data Available!</b></td>
			</tr>
		<%} %>
		</tbody>
	</table>
</body>
</html>