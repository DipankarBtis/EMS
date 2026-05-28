<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script type="text/javascript">
function refresh()
{
	var user_cd = document.forms[0].user_cd.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_add_modify_group_allocation.jsp?user_cd="+user_cd+"&u="+u;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setValues(grp_cd,frm_dt,to_dt,grp_nm)
{
	document.forms[0].group_cd.value=grp_cd;
	document.forms[0].from_dt.value=frm_dt;
	document.forms[0].to_dt.value=to_dt;
	
	var user_cd = document.forms[0].user_cd.value;
	
	var old_value = "UserCd="+user_cd+"##GrpCd="+grp_cd+"##GrpNm="+grp_nm+"##FromDt="+frm_dt+"##ToDt="+to_dt;
	document.forms[0].old_value.value=old_value;
	
	document.getElementById("group_cd").disabled=true;
	
	document.forms[0].temp_group_cd.value=grp_cd;
	document.forms[0].opration.value='Modify';
}

function doDelete(grp_cd,frm_dt,to_dt,grp_nm)
{
	var delete_access = document.forms[0].delete_access.value;
	
	if(delete_access=="N")
	{
		alert("You dont have DELETE permission!")
	}
	else
	{
		var e = document.forms[0].user_cd;
		var user_nm = e.options[e.selectedIndex].text
		
		var a = confirm("Do you want to DELETE following User Access Group for "+user_nm+"?\n\nGroup Name : "+grp_nm+"\nFrom Date : "+frm_dt+"\nTo Date : "+to_dt+"");
		if(a)
		{
			document.forms[0].group_cd.value=grp_cd;
			document.forms[0].from_dt.value=frm_dt;
			document.forms[0].to_dt.value=to_dt;
			
			var e = document.forms[0].group_cd;
			var group_nm = e.options[e.selectedIndex].text;
			document.forms[0].group_nm.value=group_nm;
			
			document.getElementById("group_cd").disabled=true;
			
			document.forms[0].temp_group_cd.value=grp_cd;
			document.forms[0].opration.value='Delete';
			
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
}

function doSubmit()
{
	var user_cd = document.forms[0].user_cd.value;
	var group_cd = document.forms[0].group_cd.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var e = document.forms[0].group_cd;
	var group_nm = e.options[e.selectedIndex].text;
	document.forms[0].group_nm.value=group_nm;
	
	var msg="";
	var flag=true;
	
	if(user_cd == "0" || trim(user_cd) == "")
	{
		msg+="Please Select User!\n";
		flag=false;
	}
	if(group_cd == "0" || trim(group_cd) == "")
	{
		msg+="Please Select Group!\n";
		flag=false;
	}
	if(from_dt == "" || trim(from_dt) == "")
	{
		msg+="Please Enter From Date!\n";
		flag=false;
	}
	if(to_dt == "" || trim(to_dt) == "")
	{
		msg+="Please Enter To Date!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to add/edit The Group for the Selected User?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

var newWindow;
function openList()
{	
	var url = "rpt_active_user_list.jsp"

	if(!newWindow || newWindow.closed)
	{
		newWindow= window.open(url,"User List","top=10,left=70,width=800,height=700,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow= window.open(url,"User List","top=10,left=70,width=800,height=700,scrollbars=1");
	}
}

function refreshChild(user_cd)
{
	document.forms[0].user_cd.value=user_cd;
	
	refresh();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.admin.DataBean_Admin" id="dbadmin" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateutil" scope="page"></jsp:useBean>
<%
String sysdate = ""+dateutil.getSysdate();
String sysdatePlusOneYr = ""+dateutil.getSysdatePlusOneYear();

String user_cd=request.getParameter("user_cd")==null?"0":request.getParameter("user_cd");

String company_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");

dbadmin.setCallFlag("ACCESS_GROUP_ALLOCATION");
dbadmin.setEmp_cd(emp_cd);
dbadmin.setUser_cd(user_cd);
dbadmin.setComp_cd(company_cd);
dbadmin.init();

Vector VEMP_CD = dbadmin.getVEMP_CD();
Vector VEMP_NM = dbadmin.getVEMP_NM();

Vector VGROUP_CD = dbadmin.getVGROUP_CD();
Vector VGROUP_NM = dbadmin.getVGROUP_NM();

Vector VALLOCATED_GRP_CD = dbadmin.getVALLOCATED_GRP_CD();
Vector VALLOCATED_GRP_NM = dbadmin.getVALLOCATED_GRP_NM();
Vector VALLOCATED_FRM_DT = dbadmin.getVALLOCATED_FRM_DT();
Vector VALLOCATED_TO_DT = dbadmin.getVALLOCATED_TO_DT();

String isSupAdmn = dbadmin.getIsSupAdmn();
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_admin">

<div class="box-body">
	<div class="row">
		<div class="col-md-1 col-sm-1 col-xs-1"></div>
		<div class="col-md-4 col-sm-4 col-xs-4">
			<%if(!msg.equals("")){
				if(msg_type.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
				<%}else if(msg_type.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
				<%}
			} %>
			<div class="card cardmain">
				<div class="card-header cdheader">
					<label class="form-label topheader">Access Group Allocation/Modification</label>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
			    					<span class="btn btn-info btn-sm select_btn" onclick="openList();" style="font-weight: bold;">
				    					<i class="fa fa-list-ul"></i>&nbsp;Select User
									</span>
			    				</div>
				  			</div>
						</div>
						<div class="col-sm-9 col-xs-9 col-md-9">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="user_cd" id="user_cd" onchange="refresh();" style="pointer-events: none;">
				      					<option value="0">--Select--</option>
				      					<%for(int i=0; i<VEMP_CD.size(); i++){ %>
				      					<option value="<%=VEMP_CD.elementAt(i)%>"><%=VEMP_NM.elementAt(i)%></option>
				      					<%} %>
				      				</select>
				      				<script>document.forms[0].user_cd.value='<%=user_cd%>';</script>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Group<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-9 col-xs-9 col-md-9">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="group_cd" id="group_cd">
				      					<option value="0">--Select--</option>
				      					<%for(int i=0; i<VGROUP_CD.size(); i++){ %>
				      					<option value="<%=VGROUP_CD.elementAt(i)%>"><%=VGROUP_NM.elementAt(i)%></option>
				      					<%} %>
				      				</select>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<label class="form-label"><b>From Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-9 col-xs-9 col-md-9">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
					      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=sysdate%>" maxLength="10" onblur="validateDate(this);checkFromToDate(this,document.forms[0].to_dt,'F');" onchange="validateDate(this);checkFromToDate(this,document.forms[0].to_dt,'F');">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      				</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<label class="form-label"><b>To Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-9 col-xs-9 col-md-9">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
					      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=sysdatePlusOneYr%>" maxLength="10" onblur="validateDate(this);checkFromToDate(document.forms[0].from_dt,this,'T');" onchange="validateDate(this);checkFromToDate(document.forms[0].from_dt,this,'T');">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      				</div>
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-1 col-sm-1 col-xs-1">&nbsp;</div>
		<div class="col-md-5 col-sm-5 col-xs-5">
			<div class="card cardmain">
				<div class="card-header cdheader topheader">
					Allocated Groups
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered table-hover">
							<thead>
								<tr>
									<th></th>
									<th>Group Name</th>
									<th>From Date</th>
									<th>To Date</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<%if(VALLOCATED_GRP_CD.size()>0){ %>
									<%for(int i=0; i<VALLOCATED_GRP_CD.size(); i++){ %>
									<tr>
										<td valign="middle">
											<%if(isSupAdmn.equals("Y")){ %>
												<div align="center">
													<font title="Click to Edit" style="color:var(--header_color)"><i class="fa fa-edit fa-lg" onclick="setValues('<%=VALLOCATED_GRP_CD.elementAt(i)%>','<%=VALLOCATED_FRM_DT.elementAt(i)%>','<%=VALLOCATED_TO_DT.elementAt(i)%>','<%=VALLOCATED_GRP_NM.elementAt(i)%>')"></i></font>
												</div>
											<%}else{ %>
												<%if(VALLOCATED_GRP_NM.elementAt(i).equals("Administrator") || VALLOCATED_GRP_NM.elementAt(i).equals("Admin")){ %>
													<div align="center">
														<font title="Click to Edit" style="color:gray"><i class="fa fa-edit fa-lg"></i></font>
													</div>
												<%}else{ %>
													<div align="center">
														<font title="Click to Edit" style="color:var(--header_color)"><i class="fa fa-edit fa-lg" onclick="setValues('<%=VALLOCATED_GRP_CD.elementAt(i)%>','<%=VALLOCATED_FRM_DT.elementAt(i)%>','<%=VALLOCATED_TO_DT.elementAt(i)%>','<%=VALLOCATED_GRP_NM.elementAt(i)%>')"></i></font>
													</div>
												<%} %>
											<%} %>
										</td>
										<td><%=VALLOCATED_GRP_NM.elementAt(i) %></td>
										<td><div align="center"><%=VALLOCATED_FRM_DT.elementAt(i) %></div></td>
										<td><div align="center"><%=VALLOCATED_TO_DT.elementAt(i) %></div></td>
										<td>
										<%if(isSupAdmn.equals("Y")){ %>
											<div align="center">
												<font title="Click to Delete" style="color:red">
													<i class="fa fa-trash fa-lg" onclick="doDelete('<%=VALLOCATED_GRP_CD.elementAt(i)%>','<%=VALLOCATED_FRM_DT.elementAt(i)%>','<%=VALLOCATED_TO_DT.elementAt(i)%>','<%=VALLOCATED_GRP_NM.elementAt(i)%>');"></i>
												</font>
											</div>
										<%}else{ %>
											<%if(VALLOCATED_GRP_NM.elementAt(i).equals("Administrator") || VALLOCATED_GRP_NM.elementAt(i).equals("Admin")){ %>
												<div align="center">
													<font title="Click to Delete" style="color:grey">
														<i class="fa fa-trash fa-lg"></i>
													</font>
												</div>
											<%}else{ %>
												<div align="center">
													<font title="Click to Delete" style="color:red">
														<i class="fa fa-trash fa-lg" onclick="doDelete('<%=VALLOCATED_GRP_CD.elementAt(i)%>','<%=VALLOCATED_FRM_DT.elementAt(i)%>','<%=VALLOCATED_TO_DT.elementAt(i)%>','<%=VALLOCATED_GRP_NM.elementAt(i)%>');"></i>
													</font>
												</div>
											<%} %>
										<%} %>
										</td>
									</tr>
									<%} %>
								<%}else{ %>
								<tr>
									<td colspan="5"><div align="center"><b>No Groups Allocated!</b></div></td>
								</tr>
								<%} %>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-1 col-sm-1 col-xs-1"></div>
	</div>
</div>	

<input type="hidden" name="option" value="ACCESS_GROUP_ALLOCATION">
<input type="hidden" name="old_value" value="">
<input type="hidden" name="group_nm" value="">
<input type="hidden" name="temp_group_cd" value="">
<input type="hidden" name="opration" value="">

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