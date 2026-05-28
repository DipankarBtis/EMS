<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh()
{
	var module_cd = document.forms[0].module_cd.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_access_right_matrix.jsp?module_cd="+module_cd+"&u="+u;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function exportToXls()
{
	var module_cd = document.forms[0].module_cd.value;
	var sub_module_cd = document.forms[0].sub_module_cd.value;
	var sysdate = document.forms[0].sysdate.value;
	
	sysdate = sysdate.toString();
	sysdate = sysdate.split('/').join('');
	
	var url = "xls_access_right_matrix.jsp?fileName=Access_Right_Matrix "+sysdate+".xls&module_cd="+module_cd+"&sub_module_cd="+sub_module_cd ;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.admin.DB_Admin_Report" id="dbadmin" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String module_cd=request.getParameter("module_cd")==null?"0":request.getParameter("module_cd");
String group_cd=request.getParameter("group_cd")==null?"0":request.getParameter("group_cd");
String sub_module_cd=request.getParameter("sub_module_cd")==null?"All":request.getParameter("sub_module_cd");

String company_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");

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
<%@ include file="../home/header.jsp"%>
<form>

<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	 Access Right Matrix
					    </div>
					    <div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>Module</b></label>
					  		</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="module_cd" id="module_cd" onchange="refresh();">
										<option value="0">All</option>
										<%for(int i=0;i<VMODULE_CD.size();i++){%>
										<option value="<%=VMODULE_CD.elementAt(i)%>"><%=VMODULE_NM.elementAt(i)%></option><%}%>
									</select>
									<script>document.forms[0].module_cd.value='<%=module_cd%>';</script>									
				    			</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;User Association </label>
					</div>
					<div class="accordion">
						<div class="accordion-item accor_item">
							<h2 class="accordion-header" id="UserInfoheading">
 								<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#UserInfocollapse" aria-expanded="false" aria-controls="UserInfocollapse">
					    			User Detail&nbsp;
					      		</button>	
					      	</h2>
							<div id="UserInfocollapse" class="accordion-collapse collapse" aria-labelledby=UserInfoheading">
					      		<div class="accordion-body accor-body">
									<div class="row">
										<div class="table-responsive">
											<table class="table table-bordered table-hover">
												<thead>
													<tr>															
														<th></th>
														<%for(int a=0; a<VGROUP_CD.size();a++){ %>
														<th><%=VGROUP_NM.elementAt(a)%></th>
														<%} %>
													</tr>
												</thead>
												<tbody>
													<tr>															
														<td style="width: 5%;"><b>User Detail</b></td>
														<%for(int a=0; a<VUSER_ACCESS_GROUP.size();a++){ %>
														<td style="white-space: normal"><%=VUSER_ACCESS_GROUP.elementAt(a)%></td>
														<%} %>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%int j=0,k=0,l=0,m=0,b=0,c=0;
				for(int i=0; i<VTEMP_MODULE_CD.size();i++)
				{ 
					int index=Integer.parseInt(""+VINDEX.elementAt(i));
				%>
					<%if(i!=0){ %>
					&nbsp;
					<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;<%=VTEMP_MODULE_NM.elementAt(i)%> </label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
						<%k=0;
						for(j=j;j<VSUB_MODULE_CD.size(); j++)
						{
							int sub_index =Integer.parseInt(""+VSUB_INDEX.elementAt(j));
							k+=1;
						%>
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading<%=j%>">
   										<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="false" aria-controls="collapse<%=j%>">
							    			<%=VSUB_MODULE_NM.elementAt(j)%>&nbsp;<font color="blue">(<%=sub_index%> Items)</font>
							      		</button>	
							      		<script>document.forms[0].sub_module_cd.value='<%=sub_module_cd%>';</script>										      		
							    	</h2>
									<div id="collapse<%=j%>" class="accordion-collapse collapse" aria-labelledby="heading<%=j%>">
							      		<div class="accordion-body accor-body">
							        		<div class="row">
												<div class="table-responsive">
													<table class="table table-bordered table-hover">
														<thead>
															<tr>															
																<th rowspan="2">Form/Report</th>
																<%for(int a=0; a<VGROUP_CD.size();a++){ %>
																<th colspan="9"><%=VGROUP_NM.elementAt(a)%></th>
																<%} %>
															</tr>
															<tr>
																<%for(int a=0; a<VGROUP_CD.size();a++){ %>
																<th style="writing-mode: vertical-lr;">Read</th>
																<th style="writing-mode: vertical-lr;">Write</th>
																<th style="writing-mode: vertical-lr;">Check</th>
																<th style="writing-mode: vertical-lr;">Print</th>
																<th style="writing-mode: vertical-lr;">Delete</th>
																<th style="writing-mode: vertical-lr;">Audit</th>
																<th style="writing-mode: vertical-lr;">Authorize</th>
																<th style="writing-mode: vertical-lr;">Approve</th>
																<th style="writing-mode: vertical-lr;">Execute</th>
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
																			<td align="center"><%=VREAD_ACS.elementAt(b)%></td>
																			<td align="center"><%=VWRITE_ACS.elementAt(b)%></td>
																			<td align="center"><%=VCHECK_ACS.elementAt(b)%></td>
																			<td align="center"><%=VPRINT_ACS.elementAt(b)%></td>
																			<td align="center"><%=VDELETE_ACS.elementAt(b)%></td>
																			<td align="center"><%=VAUDIT_ACS.elementAt(b)%></td>
																			<td align="center"><%=VAUTHORIZE_ACS.elementAt(b)%></td>
																			<td align="center"><%=VAPPROVE_ACS.elementAt(b)%></td>
																			<td align="center"><%=VEXECUTE_ACS.elementAt(b)%></td>
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
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<%if(k==index){
								j+=1;
								break;
							}%>
						<%} %>
						</div>
					</div>
				<%} %>			
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="read_access" value="<%=read_access%>">
<input type="hidden" name="write_access" value="<%=write_access%>">
<input type="hidden" name="check_access" value="<%=check_access%>">
<input type="hidden" name="print_access" value="<%=print_access%>">
<input type="hidden" name="delete_access" value="<%=delete_access%>">  	
<input type="hidden" name="audit_access" value="<%=audit_access%>">
<input type="hidden" name="authorize_access" value="<%=authorize_access%>">
<input type="hidden" name="approve_access" value="<%=approve_access%>">
<input type="hidden" name="execute_access" value="<%=execute_access%>">
<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<input type="hidden" name="u" value="<%=u%>">

</form>
</body>
</html>