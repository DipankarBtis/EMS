<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--   Created By Arth Patel on 20230505 : Report for User Details  -->
<!--   Modified By Harsh Maheta on 2023112  -->
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function enable_disable(flag)
{	
	var from_dt=document.forms[0].from_dt.value;
	var to_dt=document.forms[0].to_dt.value;
	var rd_val=flag;
	
	var u = document.forms[0].u.value;
	
	var url="rpt_user_details.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&rd_val="+rd_val+
		"&u="+u;
	
	location.replace(url);
}

function view_rpt()
{
	var from_dt=document.forms[0].from_dt.value;
	var to_dt=document.forms[0].to_dt.value;
	var u = document.forms[0].u.value;
	
	var flag="";
	if(document.forms[0].rd[0].checked)
	{
		flag="A";
	}
	else if(document.forms[0].rd[1].checked)
	{
		flag="E";
	}
	else if(document.forms[0].rd[2].checked)
	{
		flag="X";
	}
	
	var rd_val=flag;
	var flag=false;
	if(from_dt=='')
	{
		flag=true;
		msg="from Date field cannot be blank..";
	}
	if(to_dt=='')
	{
		flag=true;
		msg="To Date field cannot be blank..";
	}
	if(!flag)
	{
		location.replace("rpt_user_details.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&rd_val="+rd_val+"&u="+u);
	}
	else
	{
		alert(msg);
	}
}

function filter()
{
	
	var lock_status_dd = document.forms[0].lock_status_dd.value;
	var filter_status = document.forms[0].filter_status.value;
	var from_dt=document.forms[0].from_dt.value;
	var to_dt=document.forms[0].to_dt.value;
	var rd_val="A";
	var u = document.forms[0].u.value;
	
	if(document.forms[0].rd[0].checked)
	{
		rd_val="A";
	}
	else if(document.forms[0].rd[1].checked)
	{
		rd_val="E";
	}
	
	if(filter_status=='e'){
		filter_status="e";
	}
	else if (filter_status=='d'){
		filter_status="d"
	}
	else if(filter_status=='x'){
		filter_status=="x"
	}
	else if(filter_status==''){
		filter_status=="a"
	}
	
	var url = "rpt_user_details.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&rd_val="+rd_val+
			"&filter_status="+filter_status+"&lock_status_dd="+lock_status_dd+"&u="+u;
	location.replace(url);	
} 

function hide_show(id1,id2)
{	
	if(document.getElementById(id1).style.display=='none'){
		document.getElementById(id1).style.display='table-row-group';
		document.getElementById(id2).className='fa fa-compress';
	}else{
		document.getElementById(id1).style.display='none';
		document.getElementById(id2).className='fa fa-expand';
	}
	
	if(document.forms[0].prev_display.value != "" && document.forms[0].prev_display1.value !="")
	{
		if(document.forms[0].prev_display.value != id1 && document.forms[0].prev_display1.value != id2)
		{
			document.getElementById(document.forms[0].prev_display.value).style.display='none';
			document.getElementById(document.forms[0].prev_display1.value).className='fa fa-expand';
		}
	}
	document.forms[0].prev_display.value=id1;
	document.forms[0].prev_display1.value=id2;
} 

function changeLockStatus(emp_cd,user_nm,change_status,mod_type,i)
{
	document.forms[0].mod_type.value=mod_type;
	document.forms[0].emp_cd.value=emp_cd;
	document.forms[0].user_nm.value=user_nm;
	document.forms[0].change_status.value=change_status;
	
	if(change_status=="N")
	{
		var a = confirm("Do you want to Lock the User?");
		
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
		else
		{
			//document.getElementById("flexSwitchCheckCheckedLock"+i).checked = false;
			//document.getElementById('inactive_btn').value = "N";
		}
	}
	else
	{
		var a = confirm("Do you want to Unlock the User?");
		
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
		else
		{
			//document.getElementById("flexSwitchCheckCheckedLock"+i).checked = true;
			//document.getElementById('inactive_btn').value = "Y";
		}
	}
}
function changeStatus(emp_cd,user_nm,change_status,mod_type,i)
{
	document.forms[0].mod_type.value=mod_type;
	document.forms[0].emp_cd.value=emp_cd;
	document.forms[0].user_nm.value=user_nm;
	document.forms[0].change_status.value=change_status;
	
	if(change_status=="N")
	{
		var a = confirm("Do you want to Enable the User?");
		
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else if(change_status=="Y")
	{
		var a = confirm("Do you want to Disable the User?");
		
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else if(change_status=="D")
	{
		var a = confirm("Do you want to Recover Dormant User?");
		
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
}
function resetUserPassword(emp_uid,comp_cd,lock_status,user_status)
{
	document.forms[1].inputUserID_nm.value=emp_uid;
	document.forms[1].comp_cd.value=comp_cd;
	
	
	if(user_status == "Enabled")
	{
		if(lock_status != "Y")
		{
			if(emp_uid != "" || emp_uid != null)
			{
				var a = confirm("Do you want to Reset User password?");
				
				if(a)
				{
					document.getElementById("loading").style.visibility = "visible";
					document.forms[1].submit();
				}
			}
		}
		else
		{
			alert("User Is Locked!!");
		}
	}
	else if(user_status == "Dormant")
	{
		alert("User Is Dormant!!");
	}
	else if(user_status == "Disabled")
	{
		alert("User Is Disabled!!");
	}
	else if(user_status == "Removed")
	{
		alert("User Is Removed!!");
	}
	else
	{
		alert("User status invalid!!");
	}	
}
</script>

<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateUtil" scope="request"></jsp:useBean>
<jsp:useBean id="util" class="com.etrm.fms.util.UtilBean" scope="page"/>
<jsp:useBean class="com.etrm.fms.admin.DB_Admin_Report" id="userDtl" scope="page"/>
<%
String user_cd=(String)session.getAttribute("user_cd");
String sysdate=dateUtil.getSysdate();
String from_dt = request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String rd_val = request.getParameter("rd_val")==null?"A":request.getParameter("rd_val");
String filter_status = request.getParameter("filter_status")==null?"a":request.getParameter("filter_status");
String lock_status_dd = request.getParameter("lock_status_dd")==null?"A":request.getParameter("lock_status_dd");

userDtl.setCallFlag("USER DETAILS");
userDtl.setFrom_dt(from_dt);
userDtl.setSet_to_dt(to_dt);
userDtl.setRd_flag(rd_val);
userDtl.setFilter_status(filter_status);
userDtl.setLock_status_dd(lock_status_dd);
userDtl.init();

/* if(from_dt.equals(""))
{
	from_dt=userDtl.getFrom_dt();
	to_dt=userDtl.getSet_to_dt();
} */

Vector VCOMPANY_CD = userDtl.getVCOMPANY_CD();
Vector VCOMPANY_ABBR = userDtl.getVCOMPANY_ABBR();

Vector VEMP_NM = userDtl.getVEMP_NM();
Vector VEMP_CD = userDtl.getVEMP_CD();
Vector VEMP_UID = userDtl.getVEMP_UID();  
Vector VEMAIL_ID = userDtl.getVEMAIL_ID();  
Vector VSTATUS = userDtl.getVSTATUS();
Vector VLOCK_STATUS = userDtl.getVLOCK_STATUS();
Vector VGROUP_NM = userDtl.getVGROUP_NM();
Vector VENT_DT=userDtl.getVENT_DT();
Vector VINDEX = userDtl.getVINDEX();

Vector V_EMP_UID = userDtl.getV_EMP_UID();
Vector V_EMP_NM = userDtl.getV_EMP_NM();
Vector V_EMP_STATUS = userDtl.getV_EMP_STATUS();
Vector V_MODIFY_DT = userDtl.getV_MODIFY_DT();
Vector V_SEQ_NO = userDtl.getV_SEQ_NO();
Vector V_MODIFIED_BY = userDtl.getV_MODIFIED_BY();
Vector V_LOCKED_STATUS = userDtl.getV_LOCKED_STATUS();
Vector VDORMANT_DAYS_COUNT = userDtl.getVDORMANT_DAYS_COUNT();

%>

<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_admin">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<%if(!msg.equals("")){
				if(msg_type.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
				<%}else if(msg_type.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
				<%}
			} %>
			<div class="card cardmain">
				<div class="card-header cdheader topheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
						    User Details
						</div>
					    <a href="../admin/xls_user_details.jsp?fileName=UserDetailsReport.xls&user_cd=<%=user_cd %>&company_cd=<%=comp_cd %>&from_dt=<%=from_dt %>&to_dt=<%=to_dt %>&rd_val=<%=rd_val %>&filter_status=<%=filter_status %>&lock_status_dd=<%=lock_status_dd%>" download="User Details Report.xls">
						 	<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</a>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-7">
							<div class="d-flex justify-content-between">
								<div <%if(rd_val.equals("E")){ %>style="display: none;"<%} %>>
									<div class="form-group row">
										<div class="col-auto">
												<label class="form-label"><b>Report Date </b></label>
										</div>
										<div class="col-auto">
						      				<div class="input-group input-group-sm" >
					      						<input type="text" class="form-control form-control-sm date fmsdtpick"  value="<%=sysdate%>" disabled size="10" maxLength="10" onblur="if(validateDate(this)){}; checkDate();">
					      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      					</div>
					      				</div>
				      				</div>
								</div>
								<div <%if(rd_val.equals("X") || rd_val.equals("A")){ %>style="display: none;"<%} %>>
								 	<div class="form-group row">
										<div class="col-auto">
											<label class="form-label"><b>From</b></label>
										</div>
										<div class="col-auto">
						      				<div class="input-group input-group-sm" >
					      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="10" maxLength="10" onblur="if(validateDate(this)){}; checkFromToDate(this,document.forms[0].to_dt,'F');" onchange="checkFromToDate(this,document.forms[0].to_dt,'F'); view_rpt();"> <!--  -->
					      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      						</div>
						    			</div>
										<div class="col-auto">
											<label class="form-label"><b>To</b></label>
										</div>
										<div class="col-auto">
						      				<div class="input-group input-group-sm" >
					      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="10" maxLength="10" onblur="if(validateDate(this)){}; checkFromToDate(document.forms[0].from_dt,this,'T');" onchange="checkFromToDate(document.forms[0].from_dt,this,'T'); view_rpt();">
					      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      						</div>
						    			</div>
									</div>
								</div>
					    		<div align="center">
									<input type="radio" name="rd" value="A" <%if(rd_val.equals("A")){ %> checked="checked" <%} %> onclick="enable_disable('A')" >&nbsp;<b>All</b>&nbsp; &nbsp; &nbsp;
					    			<input type="radio" name="rd" value="E" <%if(rd_val.equals("E")){ %> checked="checked" <%} %> onclick="enable_disable('E')">&nbsp;<b>Created On</b> &nbsp; &nbsp;
								</div>
							</div>
					    </div>
   					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div align="left"><%=utilmsg.infoMessage("<b>User will go dormant if no activity observed for "+VDORMANT_DAYS_COUNT.elementAt(0)+" days !</b>") %></div>
   					</div>
   				</div>
   				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered" id="filterbysearch">
							<thead>
								<tr>
									<th>Sr#</th>
									<th style="width:20%">User Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="UserName" onkeyup="Search(this,'1','<%=VEMP_CD.size()%>');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Login ID<%-- <br><div align="center"><input class="form-control form-control-sm" type="text" id="UserId" onkeyup="Search(this,'2','<%=VEMP_CD.size()%>');" placeholder="Search.." style="width:100px"/></div> --%></th>
									<th>Created On</th>
									<th style="width:25%">Email</th>
									<th style="width:10%">Status
										 <%//if(rd_val.equals("A"))
										 { %>
											<select class="form-select form-select-sm" name="filter_status" onchange="filter()" >
									    		<option  value="a">All</option>
									    		<option  value="e" <%if(filter_status.equals("e")){ %> selected="selected" <%} %>>Enabled</option>
									    		<option  value="d" <%if(filter_status.equals("d")){ %> selected="selected" <%} %>>Disabled</option>
									    		<option  value="x" <%if(filter_status.equals("x")){ %> selected="selected" <%} %>>Dormant</option>
									    		<option  value="r" <%if(filter_status.equals("r")){ %> selected="selected" <%} %>>Removed</option>
									    	</select>
									    <%}%>
									</th>
									<th>Lock/Unlock<%//if(rd_val.equals("A"))
										 { %>
											<select class="form-select form-select-sm" name="lock_status_dd" onchange="filter()" >
									    		<option  value="A">All</option>
									    		<option  value="L">Locked</option>
									    		<option  value="U">Unlocked</option>
									    	</select>
									    	<script>document.forms[0].lock_status_dd.value="<%=lock_status_dd%>"</script>
									    <%}%>
									</th>
									<%for(int i=0; i<VCOMPANY_CD.size(); i++){ %>
									<th style="width:25%"><%=VCOMPANY_ABBR.elementAt(i)%> Active Access Group (Valid Till)</th>
									<%} %>
									<th>Reset Password</th>
								</tr>
							</thead>
							<tbody id="mainTbody">
							<%int j=0;int k=0;
								if(VEMP_CD.size()>0)
								{
									for (int i=0;i<VEMP_CD.size();i++)
									{
											int size = Integer.parseInt(""+VINDEX.elementAt(i));
										%>
										<tr class = "">
											<input type="hidden" name="dtl_size" id="dtl_size<%=i%>" value="<%=size%>">
											<td <%if(size>1){ %>onclick="hide_show('tbody<%=i %>','hidCont<%=i%>');<%}%>"><%=i+1%> &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;
												<%if(size==0 || size==1){ %>
												<%}else{%>
													<i style="align: right; display:null;" id="hidCont<%=i%>" class="fa fa-expand" title="Click here to show User's summary"></i>
												<%}%>
											</td>
											<td><%=VEMP_NM.elementAt(i)%></td>
											<td><%=VEMP_UID.elementAt(i)%></td>
											<td style="text-align:center"><%=VENT_DT.elementAt(i) %></td>
											<td style="text-align:center"><%=VEMAIL_ID.elementAt(i)%></td>
											<td style="text-align:center">
												<%if(VSTATUS.elementAt(i).equals("Enabled")){ %>
													<i title="Click To Disable User" class="fa fa-user fa-lg" aria-hidden="true" onclick="changeStatus('<%=VEMP_CD.elementAt(i)%>','<%=VEMP_NM.elementAt(i)%>','Y','user_status','<%=i%>')"></i>
	    										<%}else if(VSTATUS.elementAt(i).equals("Disabled")){ %>
	    											<i title="Click To Enable User" style="color: red"  class="fa fa-user-times fa-lg"  aria-hidden="true" onclick="changeStatus('<%=VEMP_CD.elementAt(i)%>','<%=VEMP_NM.elementAt(i)%>','N','user_status','<%=i%>')"></i>
	    										<%}else if(VSTATUS.elementAt(i).equals("Dormant")){ %>
	    											<i title="Click To Recover Dormant User" style="color: #ff9933"  class="fa fa-user-circle-o fa-lg" aria-hidden="true" onclick="changeStatus('<%=VEMP_CD.elementAt(i)%>','<%=VEMP_NM.elementAt(i)%>','D','user_status','<%=i%>')"></i>
	    										<%}else if(VSTATUS.elementAt(i).equals("Removed")){ %>
	    											<i title="Removed" style="color: gray"  class="fa fa-user-times fa-lg"  aria-hidden="true"></i>
	    										<%}%>
											</td>
											<td align="center">
											<%if(VLOCK_STATUS.elementAt(i).equals("Y")){ %>
												<i title="Click To Unlock User" style="color: red" class="fa fa-lock fa-lg" aria-hidden="true" onclick="changeLockStatus('<%=VEMP_CD.elementAt(i)%>','<%=VEMP_NM.elementAt(i)%>','Y','lock_status','<%=i%>')"></i>&nbsp;
											<%}else{ %>
												<i title="Click To Lock User" class="fa fa-unlock fa-lg" aria-hidden="true" onclick="changeLockStatus('<%=VEMP_CD.elementAt(i)%>','<%=VEMP_NM.elementAt(i)%>','N','lock_status','<%=i%>')"></i>&nbsp;
											<%} %>
											</td>
											<%for(int z=0; z<((Vector) VGROUP_NM.elementAt(i)).size(); z++) {%>
											<td><%=((Vector) VGROUP_NM.elementAt(i)).elementAt(z)%></td>
											<%} %>
											<td align="center"><i title="Click To Reset User Password" style="color: #008080" class="fa fa-key fa-lg" aria-hidden="true" onclick="resetUserPassword('<%=VEMP_UID.elementAt(i)%>','<%=comp_cd%>','<%=VLOCK_STATUS.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>')"></i></td>
											
											<input type="hidden" name="userId" value="<%=VEMP_UID.elementAt(i)%>">
										</tr>
										<%if(size>0)
										{k=0;%>
											<tbody id="tbody<%=i%>" style="display:none;">
												<tr style="text-align:center;font-weight:bold;background:#bce6ff;color:#0c63e4;">
													<td colspan="2" rowspan="<%=size+1%>" style="background:white;"></td>
													<td>Sr#</td>
													<td>Modified On</td>
													<td>Modified By</td>
													<td>Status</td>
													<td>Lock Status</td>
													<td style="background:white;" rowspan="<%=size+1%>" colspan="<%=1+VCOMPANY_CD.size()%>"></td>
												</tr>
												<%for(j=j; j<= V_SEQ_NO.size(); j++)
												{
													k+=1;
												%>
												<tr>
													<td style="text-align:center"><%=V_SEQ_NO.elementAt(j) %></td>
													<td style="text-align:center"><%=V_MODIFY_DT.elementAt(j) %></td>
													<td><%=V_MODIFIED_BY.elementAt(j) %></td>
													<td style="text-align:center">
														<%if(V_EMP_STATUS.elementAt(j).equals("Enabled")){ %>
															<i class="fa fa-user fa-lg" aria-hidden="true"></i>
			    										<%}else if(V_EMP_STATUS.elementAt(j).equals("Disabled")){ %>
			    											<i style="color: red"  class="fa fa-user-times fa-lg"  aria-hidden="true"></i>
			    										<%}else if(V_EMP_STATUS.elementAt(j).equals("Dormant")){ %>
		    												<i title="Click To Recover Dormant User" style="color: #ff9933"  class="fa fa-user-circle-o fa-lg" aria-hidden="true"></i>
			    										<%}%>
													</td>
													<td style="text-align:center"><%if(V_LOCKED_STATUS.elementAt(j).equals("Y")){ %>
															<i style="color: red" class="fa fa-lock fa-lg" aria-hidden="true" ></i>&nbsp;
														<%}else{ %>
															<i class="fa fa-unlock fa-lg" aria-hidden="true" ></i>&nbsp;
														<%} %>
													</td>
												</tr>	
												<%if(k==size)
													{
														j=j+1;
														break;
													}
												}%>
											</tbody>
										<%} %>
									<%} %>
								<%}else{ %>
								<tr>
									<td colspan="<%=8+VCOMPANY_CD.size()%>" align="center"><%=utilmsg.infoMessage("<b>No User Available!</b>")%></td>
								</tr>
								<%} %>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="USER_DTL">
<input type="hidden" name="mod_type" value="">
<input type="hidden" name="emp_cd" value="">
<input type="hidden" name="user_nm" value="">
<input type="hidden" name="change_status" value="">
<input type="hidden" name="prev_display" value="">
<input type="hidden" name="prev_display1" value="">
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

<form method="post" action="../servlet/Frm_reset_password">

<input type="hidden" name="rpt_nm" value="USER_DTL">
<input type="hidden" name="option" value="RESET_PASSWORD">
<input type="hidden" name="comp_cd" value="">
<input type="hidden" name="inputUserID_nm" value="">
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
</head>

<script>
$(document).ready(function() {
	
	$('#tbsearch th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "Sr#")
		{
			$(this).html('<div align="center"><input class="form-control form-control-sm" type="text" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
		}
		else
		{
			$(this).html('<div align="center"><input class="form-control form-control-sm" type="text" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:100px"/></div>');
		}
	});
	
});

function Search(obj, indx,main_row) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch");
  	tr = table.getElementsByTagName("tr");
  	
  	tbody = document.getElementById("mainTbody");
  	tbody_tr = tbody.getElementsByTagName("tr");
  	
  	for (k = 0; k < tbody_tr.length*main_row; k++) 
  	{
  		dtl_size = document.getElementsByName("dtl_size")[k].value;
  	  	
	   	for (i = 1; i < tr.length; i++) 
	   	{
	      	td = tr[i].getElementsByTagName("td")[indx];
	      	if (td) 
	      	{
	       		txtValue = td.textContent || td.innerText;
	       		
	       		if (txtValue.toLocaleLowerCase().indexOf(filter) > -1) 
	       		{
	          		//tr[i].style.display = "";
	          		
	          		for (j = 0; j < dtl_size; j++) 
	        	   	{
	          			//alert(i+j);
	          			tr[i+j].style.display = "";
	        	   	}
	          		count++;
	       		} 
	       		else 
	       		{
	       			tr[i].style.display = "none";
	       		}
	      	}       
	   	}
  	}
}
</script>
</html>