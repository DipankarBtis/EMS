<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.mail_recipient_config.DataBean_Mail_Recipient_Config" id="mail_config" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sel_module_nm=request.getParameter("sel_module_nm")==null?"0":request.getParameter("sel_module_nm");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String company = request.getParameter("company")==null?"":request.getParameter("company");
String owner_cd="";
if(company.equals(""))
{
	if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
	{
		owner_cd="";
	}  
	else
	{
		owner_cd=""+session.getAttribute("comp_cd");
	}
}
else
{
	owner_cd = company;
}
mail_config.setCallFlag("MAIL_RECIPIENT_DTL");
mail_config.setSel_module_nm(sel_module_nm);
mail_config.setComp_cd(owner_cd);
mail_config.init();

Vector VMODULE_NM_MST = mail_config.getVMODULE_NM_MST();
Vector VFORM_NM_MST = mail_config.getVFORM_NM_MST();
Vector VMODULE_NM=mail_config.getVMODULE_NM();

Vector VSUP_MODULE_NM=mail_config.getVSUP_MODULE_NM();
Vector VSUP_MENU_NM=mail_config.getVSUP_MENU_NM();
Vector VSUP_RPT_FREQ=mail_config.getVSUP_RPT_FREQ();
Vector VSUP_GEN_TYPE=mail_config.getVSUP_GEN_TYPE();
Vector VSUP_SEQ_NO=mail_config.getVSUP_SEQ_NO();
Vector VSUPPORT_FLAG=mail_config.getVSUPPORT_FLAG();
Vector VSUPPORT_FLAG_NM=mail_config.getVSUPPORT_FLAG_NM();
Vector VSTATUS=mail_config.getVSTATUS();
Vector VSTATUS_NM=mail_config.getVSTATUS_NM();
Vector VSTATUS_COLOR=mail_config.getVSTATUS_COLOR();

Vector VEMP_CD = mail_config.getVEMP_CD();
Vector VEMP_NM = mail_config.getVEMP_NM();
Vector VEMP_EMAIL = mail_config.getVEMP_EMAIL();
Vector VEMP_STATUS = mail_config.getVEMP_STATUS();
Vector VEMP_STATUS_NM = mail_config.getVEMP_STATUS_NM();
Vector VEMP_EXIST = mail_config.getVEMP_EXIST();
Vector VINDEX = mail_config.getVINDEX();

Vector VTO_EMAIL = mail_config.getVTO_EMAIL();
Vector VTO_SEQ_NO = mail_config.getVTO_SEQ_NO();
Vector VTO_EMP_STATUS = mail_config.getVTO_EMP_STATUS();
Vector VCC_EMP_CD = mail_config.getVCC_EMP_CD();
Vector VCC_EMP_NM = mail_config.getVCC_EMP_NM();
Vector VCC_EMP_EMAIL = mail_config.getVCC_EMP_EMAIL();
Vector VCC_EMP_STATUS = mail_config.getVCC_EMP_STATUS();
Vector VCC_EMP_STATUS_NM = mail_config.getVCC_EMP_STATUS_NM();
Vector VCC_EMP_EXIST = mail_config.getVCC_EMP_EXIST();

Vector VCC_EMAIL = mail_config.getVCC_EMAIL();
Vector VCC_SEQ_NO = mail_config.getVCC_SEQ_NO();

Vector VCC_USER_CD = mail_config.getVCC_USER_CD();
Vector VTO_USER_CD = mail_config.getVTO_USER_CD();
Vector VCC_USER_NM = mail_config.getVCC_USER_NM();
Vector VTO_USER_NM = mail_config.getVTO_USER_NM();
Vector VGENERATION_TYPE = mail_config.getVGENERATION_TYPE();
Vector VFREQ_IN_DAYS = mail_config.getVFREQ_IN_DAYS();

String RECIPIENT_CD = mail_config.getRECIPIENT_CD();
String MODULE_NM = mail_config.getMODULE_NM();
String MENU_NM = mail_config.getMENU_NM();
String REPORT_FREQ = mail_config.getREPORT_FREQ();
String GENERATION_TYPE = mail_config.getGENERATION_TYPE();
String STOP_FLAG = mail_config.getSTOP_FLAG();
String MON = mail_config.getMON();
String TUE = mail_config.getTUE();
String WED = mail_config.getWED() ;
String THU = mail_config.getTHU();
String FRI = mail_config.getFRI();
String SAT = mail_config.getSAT();
String SUN = mail_config.getSUN();
String SEQ_NO = mail_config.getSEQ_NO();
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "inline; filename="+fileName);
    
%>

	<table width="100%" border="1">
		<tr>
			<th colspan="9" rowspan="2" align="left">Mail Recipient Configured Details</th>
		</tr>
	</table>
	<%if(VMODULE_NM.size()>0){ %>
		<%int j=0,k=0,l=0,m=0;
		for(int i=0; i<VMODULE_NM.size(); i++)
		{ 
			int index=Integer.parseInt(""+VINDEX.elementAt(i));
			%>	
				<table width="100%" border="1">
					<tr>
						<th colspan="9" rowspan="1" align="left" bgcolor="gold"><%=VMODULE_NM.elementAt(i)%></th>
					</tr>
				</table>
				<table width="100%" border="1">
					<thead>
						<tr>
							<th>Sr#</th>
							<th>Status</th>
							<th>Module</th>
							<th>Form/Reports</th>
							<th>Frequency</th>
							<th>Type</th>
							<th>Feature Support</th>
							<!-- <th>Details</th> -->
							<th>To List</th>
							<th>Cc List</th>
						</tr>
					</thead>
					<tbody>
						<%k=0; for(l=l; l<VSUP_MENU_NM.size(); l++)
						{
							k+=1;
						%>
						<tr class="content1">
							<td align="center">
								<%=k %>
							</td> 
							<td align="center">
								<%=VSTATUS_NM.elementAt(l)%>
							</td>
							<td align="center"><%=VSUP_MODULE_NM.elementAt(l)%></td>
							<td align="center"><%=VSUP_MENU_NM.elementAt(l) %></td>
							<td align="center"><%=VSUP_RPT_FREQ.elementAt(l) %>
								<br><%if(VGENERATION_TYPE.elementAt(l).equals("Auto")){%>
									<div style="color: blue"><%=VFREQ_IN_DAYS.elementAt(l)%></div>
								<%}%>
							</td>
							<td align="center"><%=VSUP_GEN_TYPE.elementAt(l) %></td>
							<td align="center"><%=VSUPPORT_FLAG_NM.elementAt(l)%></td>
							<!-- <td></td> -->
							<td><%=VTO_EMAIL.elementAt(l)%></td>
							<td><%=VCC_EMAIL.elementAt(l)%></td>
						</tr>
						<%if(k==index){%>
							<%l=l+1;
							break;} %>
					<%} %>
					</tbody>
				</table>
			<%}%>
		<%}%>
</body>
</html>