<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.admin.DB_Admin_Report" id="dbadmin" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>

<%
String module_cd=request.getParameter("module_cd")==null?"0":request.getParameter("module_cd");
String group_cd=request.getParameter("group_cd")==null?"0":request.getParameter("group_cd");
String sub_module_cd=request.getParameter("sub_module_cd")==null?"All":request.getParameter("sub_module_cd");

String company_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
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

dbadmin.setCallFlag("ACCESS_RIGHT_MATRIX");
dbadmin.setModule_cd(module_cd);
dbadmin.setGroup_cd(group_cd);
dbadmin.setSub_module_cd(sub_module_cd);
dbadmin.setComp_cd(company_cd);
dbadmin.init();

Vector VMODULE_CD = dbadmin.getVMODULE_CD();
Vector VMODULE_NM = dbadmin.getVMODULE_NM();
Vector VGROUP_CD = dbadmin.getVGROUP_CD();
Vector VGROUP_NM = dbadmin.getVGROUP_NM();
Vector VSUB_MODULE_CD = dbadmin.getVSUB_MODULE_CD();
Vector VSUB_MODULE_NM = dbadmin.getVSUB_MODULE_NM();

Vector VMENU_CD = dbadmin.getVMENU_CD();
Vector VMENU_NM = dbadmin.getVMENU_NM();

Vector VSUB_MENU_SEQ = dbadmin.getVSUB_MENU_SEQ();
Vector VREAD_ACS = dbadmin.getVREAD_ACS();
Vector VWRITE_ACS = dbadmin.getVWRITE_ACS();
Vector VCHECK_ACS = dbadmin.getVCHECK_ACS();
Vector VPRINT_ACS = dbadmin.getVPRINT_ACS();
Vector VDELETE_ACS = dbadmin.getVDELETE_ACS();
Vector VAUDIT_ACS = dbadmin.getVAUDIT_ACS();
Vector VAUTHORIZE_ACS = dbadmin.getVAUTHORIZE_ACS();
Vector VAPPROVE_ACS = dbadmin.getVAPPROVE_ACS();
Vector VEXECUTE_ACS = dbadmin.getVEXECUTE_ACS();

Vector VINDEX = dbadmin.getVINDEX();
Vector VSUB_INDEX = dbadmin.getVSUB_INDEX();
Vector VINNER_SUB_INDEX = dbadmin.getVINNER_SUB_INDEX();

Vector VTEMP_MODULE_CD = dbadmin.getVTEMP_MODULE_CD();
Vector VTEMP_MODULE_NM = dbadmin.getVTEMP_MODULE_NM();

Vector VUSER_ACCESS_GROUP = dbadmin.getVUSER_ACCESS_GROUP();

%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="<%=(VGROUP_CD.size()*9+1)%>" rowspan="2" align="left">Access right Matrix Report <%if(!module_cd.equals("0")) {%>for <%= VMODULE_NM.elementAt(Integer.parseInt(""+module_cd)-1)%><%} %></th>
		</tr>
	</table>
	<table  width="100%" border="1">
		<thead>
			<tr>															
				<th></th>
				<%for(int a=0; a<VGROUP_CD.size();a++){ %>
				<th colspan="9"><%=VGROUP_NM.elementAt(a)%></th>
				<%} %>
			</tr>
		</thead>
		<tbody>
			<tr>															
				<td style="width: 5%;"><b>User Detail</b></td>
				<%for(int a=0; a<VUSER_ACCESS_GROUP.size();a++){ %>
				<td colspan="9"> <%=VUSER_ACCESS_GROUP.elementAt(a)%></td>
				<%} %>
			</tr>
		</tbody>
	</table>
	
	<%int j=0,k=0,l=0,m=0,b=0,c=0;
	for(int i=0; i<VTEMP_MODULE_CD.size();i++)
	{ 
		int index=Integer.parseInt(""+VINDEX.elementAt(i));
	%>
		<%if(i!=0){ %>
		&nbsp;
		<%} %>
			<%k=0;
			for(j=j;j<VSUB_MODULE_CD.size(); j++)
			{
				int sub_index =Integer.parseInt(""+VSUB_INDEX.elementAt(j));
				k+=1;
			%>
			<table  width="100%" border="1">
				<thead>
					<tr></tr>
					<tr>
						<th align="left" colspan="<%=(VGROUP_CD.size()*9+1)%>"><%=VTEMP_MODULE_NM.elementAt(i) %>--><%=VSUB_MODULE_NM.elementAt(j) %></th>
					</tr>
					<tr>															
						<th rowspan="2">Form/Report</th>
						<%for(int a=0; a<VGROUP_CD.size();a++){ %>
						<th colspan="9"><%=VGROUP_NM.elementAt(a)%></th>
						<%} %>
					</tr>
					<tr>
						<%for(int a=0; a<VGROUP_CD.size();a++){ %>
						<th >Read</th>
						<th >Write</th>
						<th >Check</th>
						<th >Print</th>
						<th >Delete</th>
						<th >Audit</th>
						<th >Authorize</th>
						<th >Approve</th>
						<th >Execute</th>
						<%} %>
					</tr>
				</thead>
				<tbody>
					<%m=0;
					if(sub_index>0){ %>
						<%for(l=l; l<VMENU_CD.size(); l++)
						{ 
							int inner_sub_index=Integer.parseInt(""+VINNER_SUB_INDEX.elementAt(l));
							m+=1;
						%> 
							<tr>																													
								<td><b><%=VMENU_NM.elementAt(l)%></b></td>
								<%c=0;
								for(b=b; b<VREAD_ACS.size();b++){
									c+=1;
								%>
									<td align="center" <%if(VREAD_ACS.elementAt(b).toString().equals("Y")){ %>style="background: #80ff80"<%} %>><%=VREAD_ACS.elementAt(b)%></td>
									<td align="center" <%if(VWRITE_ACS.elementAt(b).toString().equals("Y")){ %>style="background: #80ff80"<%} %>><%=VWRITE_ACS.elementAt(b)%></td>
									<td align="center" <%if(VCHECK_ACS.elementAt(b).toString().equals("Y")){ %>style="background: #80ff80"<%} %>><%=VCHECK_ACS.elementAt(b)%></td>
									<td align="center" <%if(VPRINT_ACS.elementAt(b).toString().equals("Y")){ %>style="background: #80ff80"<%} %>><%=VPRINT_ACS.elementAt(b)%></td>
									<td align="center" <%if(VDELETE_ACS.elementAt(b).toString().equals("Y")){ %>style="background: #80ff80"<%} %>><%=VDELETE_ACS.elementAt(b)%></td>
									<td align="center" <%if(VAUDIT_ACS.elementAt(b).toString().equals("Y")){ %>style="background: #80ff80"<%} %>><%=VAUDIT_ACS.elementAt(b)%></td>
									<td align="center" <%if(VAUTHORIZE_ACS.elementAt(b).toString().equals("Y")){ %>style="background: #80ff80"<%} %>><%=VAUTHORIZE_ACS.elementAt(b)%></td>
									<td align="center" <%if(VAPPROVE_ACS.elementAt(b).toString().equals("Y")){ %>style="background: #80ff80"<%} %>><%=VAPPROVE_ACS.elementAt(b)%></td>
									<td align="center" <%if(VEXECUTE_ACS.elementAt(b).toString().equals("Y")){ %>style="background: #80ff80"<%} %>><%=VEXECUTE_ACS.elementAt(b)%></td>
									<%if(c==inner_sub_index) 
									{
										b++;
										break;
									}%>
								<%}%>
							</tr>
							<%if(m==sub_index)
							{%>
								<%l=l+1;
								break;
							}%>
						<%}%>
					<%}%>																	 																
				</tbody>
			</table>
			<%if(k==index){
				j+=1;
				break;
			}%>
		<%} %>
	<%} %>
</body>
</html>