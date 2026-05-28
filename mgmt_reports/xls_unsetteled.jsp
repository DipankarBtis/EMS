<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_Unsettled_Report" id="unset_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String report_dt = request.getParameter("report_dt")==null?sysdate:request.getParameter("report_dt");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
/*String owner_cd="";

if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}*/

unset_rpt.setCallFlag("UNSETTELED_RPT");
unset_rpt.setReport_dt(report_dt);
unset_rpt.init();

Vector VMST_COMPANY_CD = unset_rpt.getVMST_COMPANY_CD();
Vector VMST_COMPANY_ABBR = unset_rpt.getVMST_COMPANY_ABBR();
Vector VMST_COMPANY_NM = unset_rpt.getVMST_COMPANY_NM();

Vector VCOMP_INDEX = unset_rpt.getVCOMP_INDEX();
Vector VCOMPANY_CD = unset_rpt.getVCOMPANY_CD();
Vector VCOMPANY_ABBR = unset_rpt.getVCOMPANY_ABBR();
Vector VCOMPANY_NM = unset_rpt.getVCOMPANY_NM();
Vector VCOUNTERPARTY_CD = unset_rpt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = unset_rpt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = unset_rpt.getVCOUNTERPARTY_NM();
Vector VAGMT_NO = unset_rpt.getVAGMT_NO();
Vector VAGMT_REV = unset_rpt.getVAGMT_REV();
Vector VCONT_NO = unset_rpt.getVCONT_NO();
Vector VCONT_REV = unset_rpt.getVCONT_REV();
Vector VCONTRACT_TYPE = unset_rpt.getVCONTRACT_TYPE();
Vector VDISPLAY_DEAL_MAP = unset_rpt.getVDISPLAY_DEAL_MAP();
Vector VCONT_REF = unset_rpt.getVCONT_REF();
Vector VINVOICE_TYPE = unset_rpt.getVINVOICE_TYPE();
Vector VBILLING_FREQ_NM = unset_rpt.getVBILLING_FREQ_NM();
Vector VPERIOD_START_DT = unset_rpt.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = unset_rpt.getVPERIOD_END_DT();
Vector VINVOICE_DT = unset_rpt.getVINVOICE_DT();
Vector VACC_COLOR = unset_rpt.getVACC_COLOR();
Vector VINVOICE_DUE_DT = unset_rpt.getVINVOICE_DUE_DT();
Vector VSETTELED_AMT = unset_rpt.getVSETTELED_AMT();
Vector VSETTELED_CURR = unset_rpt.getVSETTELED_CURR();
Vector VDEAL_TYPE = unset_rpt.getVDEAL_TYPE();
Vector VCONT_START_DT = unset_rpt.getVCONT_START_DT();
Vector VCONT_END_DT = unset_rpt.getVCONT_END_DT();
Vector VPLANT_SEQ_NO = unset_rpt.getVPLANT_SEQ_NO();
Vector VBU_SEQ_NO = unset_rpt.getVBU_SEQ_NO();
Vector VPLANT_ABBR = unset_rpt.getVPLANT_ABBR();
Vector VBU_ABBR = unset_rpt.getVBU_ABBR();
Vector VDOC_STATUS = unset_rpt.getVDOC_STATUS();
Vector VOPS_FLAG = unset_rpt.getVOPS_FLAG();
Vector VCARGO_NO = unset_rpt.getVCARGO_NO();
Vector VINV_FLAG = unset_rpt.getVINV_FLAG();
Vector VSPLIT_FLAG = unset_rpt.getVSPLIT_FLAG();
Vector VSPLIT_VAL = unset_rpt.getVSPLIT_VAL();
Vector VBOE_NO = unset_rpt.getVBOE_NO();
Vector VBOE_NM = unset_rpt.getVBOE_NM();
Vector VREPORT_DT = unset_rpt.getVREPORT_DT();
Vector VNEXT_STEP = unset_rpt.getVNEXT_STEP();
Vector VINVOICE_NO = unset_rpt.getVINVOICE_NO();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table width="100%" border="1">
		<tr>
			<th colspan="21" rowspan="" align="center"><b>Un-Settled Report [<%=report_dt %>]</b></th>
		</tr>
	</table>
	
		
	<table width="100%" border="1">
		<thead>
			<tr>
				<th>Sr#</th>
				<th class="ems_thsort0">Report Date</th>
				<th class="ems_thsort0">Legal Entity</th>
				<th class="ems_thsort0">Business Unit</th>
				<th class="ems_thsort0">Counterparty</th>
				<th class="ems_thsort0">Plant</th>
				<th class="ems_thsort0">Contract#</th>
				<th class="ems_thsort0">Contract Ref/Trade Ref</th>
				<th class="ems_thsort0">Invoice#</th>
				<th class="ems_thsort0">Doc Status</th>
				<th class="ems_thsort0">Next Step</th>
				<th class="ems_thsort0">COM OPS Approved</th>
				<th class="ems_thsort0">Invoice Type</th>
				<th class="ems_thsort0">Billing Period</th>
				<th class="ems_thsort0">Deal Type</th>
				<th class="ems_thsort0">Settle Amount</th>
				<th class="ems_thsort0">Currency</th>
				<th class="ems_thsort0">Invoice Date</th>
				<th class="ems_thsort0">Payment Due Date</th>
				<th class="ems_thsort0">Billing Cycle</th>
				<!-- <th>OPS Approved</th> -->
				<th class="ems_thsort0">Contract Period</th>
			</tr>
		</thead>
		<tbody>
		<% if(VCOUNTERPARTY_CD.size() > 0){ %>
		<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){%>
			<tr>
				<td align="center"><%=i+1 %></td>
				<%-- <td align="center"><%=report_dt%></td> --%>
				<td align="center"><%=VREPORT_DT.elementAt(i)%></td>
				<td align="center"><%=VCOMPANY_ABBR.elementAt(i) %></td> 
				<td align="center"><%=VBU_ABBR.elementAt(i)%></td>
				<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
				<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
				<td align="center">
					<%=VDISPLAY_DEAL_MAP.elementAt(i) %>
					<%if(VSPLIT_FLAG.elementAt(i).equals("Y")){ %>
							<font style="background:#ff99ff;">[Split <%=VSPLIT_VAL.elementAt(i)%>%]</font>
					<%} %>	
					<%if(!VBOE_NO.elementAt(i).equals("0")){ %>
						<font style="background:#ff99ff;"><%=VBOE_NM.elementAt(i)%></font>
						<%if(VINV_FLAG.elementAt(i).equals("F")){ %>
							<font style="background:#ccff99;">Final</font>
						<%} %>
					<%} %>	
				</td>
				<td align="center"><%=VCONT_REF.elementAt(i) %></td>
				<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
				<td align="center"><%=VDOC_STATUS.elementAt(i) %></td>
				<td align="center"><%=VNEXT_STEP.elementAt(i) %></td>
				<td align="center"><%=VOPS_FLAG.elementAt(i) %></td>
				<td align="center"><%=VINVOICE_TYPE.elementAt(i) %></td>
				<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
				<td align="center"><%=VDEAL_TYPE.elementAt(i) %></td>
				<td align="right"><%=VSETTELED_AMT.elementAt(i) %></td>
				<td align="center"><%=VSETTELED_CURR.elementAt(i) %></td>
				<td align="center" <%if(VACC_COLOR.elementAt(i).toString().equals("R")){%>style="color:red;"<%} %>><%=VINVOICE_DT.elementAt(i) %></td>
				<td align="center" <%if(VACC_COLOR.elementAt(i).toString().equals("R")){%>style="color:red;"<%} %>><%=VINVOICE_DUE_DT.elementAt(i) %></td>
				<td align="center">
					<%-- <span 
						<%if(VBILLING_FREQ_NM.elementAt(i).equals("1st-Fortnight")){ %>
							class="alert alert-info"
						<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("2nd-Fortnight")){ %>
							class="alert alert-warning"
						<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("1st-Weekly")){ %>
							class="alert" style="background:#eeccff;color: #660099;"
						<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("2nd-Weekly")){ %>
							class="alert alert-dark"
						<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("3rd-Weekly")){ %>
							class="alert alert-success"
						<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("4th-Weekly")){ %>
							class="alert alert-danger"
						<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("5th-Weekly")){ %>
							class="alert" style="background:#e6ccff;color:#330066;"
						<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("Monthly")){ %>
							class="alert alert-primary"
						<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("Other")){ %>
							class="alert" style="background:#b3ffb3;color: #008000;"
						<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("Delivery Period")){ %>
							class="alert" style="background:#ff80bf;color:#33001a;"	
						<%} %>
						> --%><%=VBILLING_FREQ_NM.elementAt(i)%></span>																								
				</td>																		
				<td align="center"><%=VCONT_START_DT.elementAt(i)%>-<%=VCONT_END_DT.elementAt(i)%></td>
			</tr>
		<%}%>
		<%}else{ %>
			<tr>
				<td colspan="21" align="center"><%=utilmsg.infoMessage("<b>No Un-Setteled data found!</b>") %></td>
			</tr>
		<%} %> 
		</tbody>
	</table>
</body>
</html>