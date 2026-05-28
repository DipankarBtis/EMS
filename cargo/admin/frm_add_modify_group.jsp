<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function addrow()
{
	var max_seq = document.getElementById("group_size").value;
	var new_seq_no = parseInt(max_seq)+1;
	var tab_name = document.getElementById("modTab");
	var row_new = document.createElement("tr"); 
	//row_new.style.fontSize='13px';
	row_new.id = 'row'+new_seq_no;
	
	var td01 = document.createElement("td");
	var div01 = document.createElement("DIV");
	div01.align='center';
	var input01 = document.createElement("input")
	input01.name = "group_cd";
	input01.id = "group_cd"+new_seq_no;
	input01.type = "text";
	input01.className="form-control form-control-sm";
	input01.maxlength = "2";
	input01.readOnly = true;
	input01.value = new_seq_no;
	input01.style.width='75px';
	
	div01.appendChild(input01);
	
	var td02 = document.createElement("td");
	var div02 = document.createElement("DIV");
	div02.align='center';
	var input02 = document.createElement("input")
	input02.name = "group_nm";
	input02.id = "group_nm"+new_seq_no;
	input02.type = "text";
	input02.className="form-control form-control-sm";
	input02.maxlength = "50";
	input02.value = "";
	input02.style.width='380px';
	
	div02.appendChild(input02);
	
	var td04 = document.createElement("td");
	var div04 = document.createElement("DIV");
	div04.className='form-check form-switch';
	
	var input04 = document.createElement("input")
	input04.name = "active";
	input04.id = "flexSwitchCheckChecked";
	input04.type = "checkbox";
	input04.className="form-check-input";
	input04.role = "switch";
	input04.setAttribute("onclick","setActiveInactive(this,'"+new_seq_no+"');");
	
	var input004 = document.createElement("lable")
	input004.id="lb"+new_seq_no;
	input004.for = "flexSwitchCheckChecked";
	input004.className='form-check-label';
	
	var input0004 = document.createElement("input")
	input0004.name = "active_flag";
	input0004.id = "active_flag"+new_seq_no;
	input0004.type = "hidden";
	input0004.value = "N";
	
	
	div04.appendChild(input04);
	div04.appendChild(input004);
	div04.appendChild(input0004);
	
	var td05 = document.createElement("td");
	td05.className="text-center"
	var input05 = document.createElement("a")
	input05.setAttribute("onclick","removeRow('"+row_new.id+"','"+new_seq_no+"');");
	var i=document.createElement("i");
	i.className="fa fa-minus-circle fa-2x";
		
	input05.appendChild(i);

	td01.appendChild(div01);
	td02.appendChild(div02);
	//td03.appendChild(div03);
	td04.appendChild(div04);
	td05.appendChild(input05);
	
	row_new.appendChild(td01);
	row_new.appendChild(td02);
	//row_new.appendChild(td03);
	row_new.appendChild(td04);
	row_new.appendChild(td05);
	tab_name.appendChild(row_new);
	
	document.getElementById("lb"+new_seq_no).innerHTML="Inactive";
	document.getElementById("group_size").value=new_seq_no;
}

function removeRow(row_id, seq_no)
{
	var row_cnt = document.forms[0].group_size.value;
	
	if(parseInt(row_cnt) == parseInt(seq_no))
	{
		if(parseFloat(row_cnt) > 0)
		{
			document.forms[0].group_size.value = parseFloat(row_cnt)-1;
		}
		var row = document.getElementById(row_id);
		row.parentNode.removeChild(row);
	}
	else
	{
		alert("Please First Remove Last Row!!");
	}
}

function setActiveInactive(obj, seq_no)
{
	if(obj.checked)
	{
		document.getElementById("lb"+seq_no).innerHTML="Active";
		document.getElementById("active_flag"+seq_no).value="Y";
	}
	else
	{
		document.getElementById("lb"+seq_no).innerHTML="Inactive";
		document.getElementById("active_flag"+seq_no).value="N";
	}
}

function doSubmit()
{
	var group_cd = document.forms[0].group_cd;
	var group_nm = document.forms[0].group_nm;
	
	var flag = true;
	var msg = "";
	
	if(group_cd!=null && group_cd.length!=undefined)
	{
		for(var i=0; i<group_cd.length; i++)
		{
			if(group_cd[i].value == "" || trim(group_cd[i].value) == "")
			{
				flag=false;
				msg+="Please Enter Group Cd for ROW-"+(parseInt(i)+1)+"\n";
			}
			if(group_nm[i].value == "" || trim(group_nm[i].value) == "")
			{
				flag=false;
				msg+="Please Enter Group Name for ROW-"+(parseInt(i)+1)+"\n";
			}
		}
	}
	else
	{
		if(group_cd.value == "" || trim(group_cd.value) == "")
		{
			flag=false;
			msg+="Please Enter Group Cd\n";
		}
		if(group_nm.value == "" || trim(group_nm.value) == "")
		{
			flag=false;
			msg+="Please Enter Group Name\n";
		}
	}
	
	if(flag)
	{
		var a = confirm("Do you Want to Add or Modify Group Detail?");
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
</script>
</head>
<body>
<%@ include file="../home/header.jsp"%>

<jsp:useBean class="com.etrm.fms.admin.DataBean_Admin" id="dbadmin" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String company_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");

dbadmin.setCallFlag("GROUP_MST");
dbadmin.setEmp_cd(emp_cd);
dbadmin.init();

Vector VGROUP_CD = dbadmin.getVGROUP_CD();
Vector VGROUP_NM = dbadmin.getVGROUP_NM();
Vector VACTIVE = dbadmin.getVACTIVE();

%>
<form method="post" action="../servlet/Frm_admin">
<div class="box-body">
	<div class="row">
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
		<div class="col-md-8 col-sm-8 col-xs-8">
			<div class="col-xs-12">
				<%if(!msg.equals("")){
					if(msg_type.equals("S")){%>
						<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
					<%}else if(msg_type.equals("E")){%>
						<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
					<%}
				} %>
				<div class="card cardmain">
					<div class="card-header cdheader topheader">
					    Add/Modify Access Group
					</div>
					<div class="card-body cdbody">
						<div class="table-responsive">
							<table class="table table-bordered">
								<thead>
									<tr>
										<th>Group Code</th>
										<th>Group Name</th>
										<th>Active</th>
										<th></th>
									</tr>
								</thead>
								<tbody id="modTab">
								<%for(int i=0; i<VGROUP_CD.size(); i++){ %>
									<tr>
										<td>
											<div align="center">
												<input class="form-control form-control-sm" name="group_cd" id="group_cd<%=VGROUP_CD.elementAt(i) %>" type="text" maxlength="2" style="width:75px;" value="<%=VGROUP_CD.elementAt(i) %>" readonly>
											</div>
										</td>
										<td>
											<div align="center">
												<input class="form-control form-control-sm" name="group_nm" id="group_nm<%=VGROUP_NM.elementAt(i) %>" type="text" maxlength="50" style="width:380px;" value="<%=VGROUP_NM.elementAt(i) %>" <%if(VGROUP_CD.elementAt(i).equals("1") || VGROUP_CD.elementAt(i).equals("2")) {%>readonly<%} %>>
											</div>
										</td>
										<td>
											<div class="form-check form-switch">
												<input class="form-check-input" name="active" type="checkbox" role="switch" id="flexSwitchCheckChecked" <%if(VACTIVE.elementAt(i).equals("Y")){%>checked<%}%> onclick="setActiveInactive(this,'<%=VGROUP_CD.elementAt(i)%>')" <%if(VGROUP_CD.elementAt(i).equals("1") || VGROUP_CD.elementAt(i).equals("2")) {%>disabled<%} %>>
											  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb<%=VGROUP_CD.elementAt(i)%>">
											  		<%if(VACTIVE.elementAt(i).equals("Y")){%>Active<%}else{%>Inactive<%} %>
											  	</label>
											  	<input type="hidden" name="active_flag" id="active_flag<%=VGROUP_CD.elementAt(i) %>" value="<%=VACTIVE.elementAt(i)%>">
											</div>
										</td>
										<td class="text-center">
											<%if((VGROUP_CD.size() - 1) == i){ %>
											<a onclick="addrow();">
												<i class="fa fa-plus-circle fa-2x"></i>
											</a>
											<%} %>
										</td>
									</tr>
								<%} %>
								</tbody>
							</table>
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
		</div>
	</div>
</div>

<input type="hidden" name="option" value="GROUP_MST">
<input type="hidden" name="module_size" id="group_size" value="<%=VGROUP_CD.size()%>">

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