<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh()
{
	var mst_module_nm = document.forms[0].mst_module_nm.value;
	//var mst_form_nm = document.forms[0].mst_form_nm.value;
	var prev_module_nm = document.forms[0].prev_module_nm.value;
	var prev_mst_company = document.forms[0].prev_mst_company.value;
	var mst_company = "";
	var chk_sei = document.forms[0].chk_sei;
	if(chk_sei.checked)
	{
		mst_company=chk_sei.value
	}
	
	if(prev_mst_company!=mst_company && mst_company!="")
	{
		mst_module_nm="0";
	}
	if(prev_mst_company==0 && mst_company=="")
	{
		mst_module_nm="0";
	}
	
	/* if(prev_module_nm != mst_module_nm)
	{
		mst_form_nm="0";
	} */
	
	var u = document.forms[0].u.value;
	
	
	var url = "rpt_mail_recipient_config.jsp?&u="+u+"&sel_module_nm="+mst_module_nm;
	if(mst_company!="")
	{
		url+="&company="+mst_company;
	}
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function exportToXls()
{
	var mst_company = "";
	var chk_sei = document.forms[0].chk_sei;
	if(chk_sei.checked)
	{
		mst_company=chk_sei.value
	}
	var mst_module_nm = document.forms[0].mst_module_nm.value;
	var prev_module_nm = document.forms[0].prev_module_nm.value;
	var url = "xls_mail_recipient_config.jsp?fileName=Mail Recipient Configured Details.xls&sel_module_nm="+mst_module_nm;
	{
		url+="&company="+mst_company;
	}
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.mail_recipient_config.DataBean_Mail_Recipient_Config" id="mail_config" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sel_module_nm=request.getParameter("sel_module_nm")==null?"0":request.getParameter("sel_module_nm");
String company = request.getParameter("company")==null?"":request.getParameter("company");
if(company.equals(""))
{
	company=owner_cd;
}
mail_config.setCallFlag("MAIL_RECIPIENT_DTL");
mail_config.setSel_module_nm(sel_module_nm);
mail_config.setComp_cd(company);
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
<%@ include file="../home/header.jsp"%>
<form>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
							Mail Recipient Configured Details
						</div>
						<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
<!-- 					<div align="center"> -->
<!-- 						<div class="col-sm-2 col-xs-2 col-md-2"> -->
							<div class="row" align="center">
									<div class="col-sm-2 col-xs-2 col-md-2">
									</div>
									<div class="col-sm-3 col-xs-3 col-md-3">
										<div class="form-group row" >
											<div class="col" align="right">
												<label class="form-label" style="background-color: var(--sei_profile_bgclr);">
													<input type="checkbox" class="form-check-input" name="chk_sei" value="0" onclick="refresh();" <%if(company.equals("0")){%>checked<%} %>>&nbsp;<b>SEI Profile</b>
												</label>
											</div>
										</div>
									</div>
									<div class="col-sm-3 col-xs-3 col-md-3"> 
										<div class="form-group row">
											<div class="col-auto">
												<label class="form-label"><b>Module</b></label>
											</div>
											<div class="col">
												<select class="form-select form-select-sm" name="mst_module_nm" onchange="refresh();">
													<option value="0">--Select--</option>
													<%for(int i=0; i<VMODULE_NM_MST.size(); i++){ %>
													<option value="<%=VMODULE_NM_MST.elementAt(i)%>"><%=VMODULE_NM_MST.elementAt(i)%></option>
													<%} %>
												</select>
												<script>document.forms[0].mst_module_nm.value='<%=sel_module_nm%>';</script>
											</div>
										</div> 
									</div>
<!-- 										<label class="form-label"><b>Module</b></label> -->
<!-- 									<div class="col"> -->
<!-- 										<select class="form-select form-select-sm" name="mst_module_nm" onchange="refresh();"> -->
<!-- 											<option value="0">--Select--</option> -->
<%-- 											<%for(int i=0; i<VMODULE_NM_MST.size(); i++){ %> --%>
<%-- 											<option value="<%=VMODULE_NM_MST.elementAt(i)%>"><%=VMODULE_NM_MST.elementAt(i)%></option> --%>
<%-- 											<%} %> --%>
<!-- 										</select> -->
<%-- 										<script>document.forms[0].mst_module_nm.value='<%=sel_module_nm%>';</script> --%>
<!-- 									</div> -->
								</div>
<!-- 							</div> -->
<!-- 						</div> -->
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Email Support Details</label>
					</div>
					<%if(VMODULE_NM.size()>0){ %>
					<%int j=0,k=0,l=0,m=0;
					for(int i=0; i<VMODULE_NM.size(); i++)
					{ 
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
						%>	
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
									<div class="accordion">
										<div class="accordion-item accor_item">
											<h2 class="accordion-header" id="heading<%=l%>">
		   										<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
									    			<%=VMODULE_NM.elementAt(i)%>&nbsp;<font color="blue">(<%=index%>)</font>
									      		</button>	
									    	</h2>
											<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
									      		<div class="accordion-body accor-body">
									        		<div class="row">
														<div class="table-responsive">
															<table class="table table-bordered table-hover">
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
																			<font style="color:<%=VSTATUS_COLOR.elementAt(l)%>">
																				<i class="fa fa-circle fa-lg" ></i>
																				&nbsp;
																			</font>
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
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						<%}%>
					<%}else{ %>
						<div align="center"><%=utilmsg.infoMessage("<b>No Mail Recipient Configuration Details Available!</b>")%></div>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="MAIL_RECIPIENT_CONFIG">
<input type="hidden" name="operation" value="INSERT">

<input type="hidden" name="prev_mst_company" value="<%=company%>">
		
<input type="hidden" name="prev_module_nm" value="<%=sel_module_nm%>">

<input type="hidden" name="to_item_size" value="<%=VTO_EMAIL.size()%>">
<input type="hidden" name="cc_item_size" value="<%=VCC_EMAIL.size()%>">

<input type="hidden" name="generation_type" value="<%=GENERATION_TYPE%>">
<input type="hidden" name="recipient_cd" value="<%=RECIPIENT_CD%>">
<input type="hidden" name="frequency" value="<%=REPORT_FREQ%>">
<input type="hidden" name="seq_no" value="<%=SEQ_NO%>">

<input type="hidden" name="del_recipient_cd" value="">
<input type="hidden" name="del_seq_no" value="">

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