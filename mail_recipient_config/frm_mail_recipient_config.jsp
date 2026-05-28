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
	var mst_form_nm = document.forms[0].mst_form_nm.value;
	var prev_module_nm = document.forms[0].prev_module_nm.value;
	var mst_company = "";
	var prev_mst_company = document.forms[0].prev_mst_company.value;
		
	var chk_sei = document.forms[0].chk_sei;
	if(chk_sei.checked)
	{
		mst_company=chk_sei.value
	}
	
	if(prev_mst_company==0 && mst_company=="")
	{
		mst_module_nm="0";
	}
	
	if(prev_mst_company!=mst_company && mst_company!="")
	{
		mst_module_nm="0";
	}
	
	if(prev_module_nm != mst_module_nm)
	{
		mst_form_nm="0";
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_mail_recipient_config.jsp?&u="+u+"&sel_form_nm="+mst_form_nm+"&sel_module_nm="+mst_module_nm;
	if(mst_company!="")
	{
		url+="&company="+mst_company;
	}

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function refresh1(seq_no,freq,gen_type)
{
	var mst_module_nm = document.forms[0].mst_module_nm.value;
	var mst_form_nm = document.forms[0].mst_form_nm.value;
	var prev_mst_company = document.forms[0].prev_mst_company.value;
	var mst_company = "";				
	var chk_sei = document.forms[0].chk_sei;
	if(chk_sei.checked)
	{
		mst_company=chk_sei.value
	}
	var u = document.forms[0].u.value;
	
	var url = "frm_mail_recipient_config.jsp?&u="+u+"&sel_form_nm="+mst_form_nm+"&sel_module_nm="+mst_module_nm+
			"&sel_seq_no="+seq_no+"&sel_freq="+freq+"&sel_type="+gen_type;
	if(mst_company!="")
	{
		url+="&company="+mst_company;
	}
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setActiveInactive(obj)
{
	if(obj.checked)
	{
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("txt_stop_mail").value="N";
	}
	else
	{
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("txt_stop_mail").value="Y";
	}
}

function checkToEmpList(obj,index)
{
	if(obj.checked)
	{
		document.getElementById("to_emp_flag"+index).value="Y";
	}
	else
	{
		document.getElementById("to_emp_flag"+index).value="N";
	}
}

function checkCcEmpList(obj,index)
{
	if(obj.checked)
	{
		document.getElementById("cc_emp_flag"+index).value="Y";
	}
	else
	{
		document.getElementById("cc_emp_flag"+index).value="N";
	}
}

function doSubmit()
{
	var flag = true;
	var msg = "";
	
	var mst_module_nm = document.forms[0].mst_module_nm.value;
	var mst_form_nm = document.forms[0].mst_form_nm.value;
	var generation_type = document.forms[0].generation_type.value;
	var frequency = document.forms[0].frequency.value;
	var txt_stop_mail = document.forms[0].txt_stop_mail.value;
	var recipient_cd=document.forms[0].recipient_cd.value;
	
	if(trim(generation_type)=="" || generation_type == null)
	{
		msg="Please Select Generation Type!";
		flag=false;
	}
	if(txt_stop_mail == "" || txt_stop_mail==null)
	{
		msg+="\nPlease Select Email Status!";
		flag=false;
	}
	if(trim(frequency) == "" || frequency == null)
	{
		msg+="\nPlease Select Frequency!";
		flag=false;
	}

	if(frequency=='Daily' || frequency == 'Weekly')
	{
		var MON = document.getElementById("MON")
		var TUE = document.getElementById("TUE")
		var WED = document.getElementById("WED")
		var THU = document.getElementById("THU")
		var FRI = document.getElementById("FRI")
		var SAT = document.getElementById("SAT")
		var SUN = document.getElementById("SUN")

		if(!MON.checked && !TUE.checked && !WED.checked && !THU.checked && !FRI.checked && !SAT.checked && !SUN.checked)
		{
			msg+="\n Please Select Days!"
			flag=false;
		}
	}

	if(flag)
	{
		var m="";
		if(recipient_cd == "")
		{
			document.forms[0].operation.value="INSERT";
			m="Submit";
		}
		else
		{
			document.forms[0].operation.value="MODIFY";
			m="Modify";
		}
		
		var mmm="";
		if(txt_stop_mail=='N')
		{
			mmm="Active";
		}
		else if(txt_stop_mail=='Y')
		{
			mmm="In-Active";
		}	
			
		var mm="Report Name : "+mst_form_nm+"\nGeneration Type : "+generation_type+"\nFrequency : "+frequency+"\n Email Status : "+mmm+"\n\n";
		
		var a = confirm(mm+"Do you want to "+m+" Schedule / Recipient Details?");
		
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

function Toaddrow()
{
	var max_seq = document.forms[0].to_item_size.value;
	var new_seq_no = parseInt(max_seq)+1;
	
	var tab_name = document.getElementById("to_itemTab");
	var row_new = document.createElement("tr"); 
	row_new.id = 'to_row'+new_seq_no;
	
	var td01 = document.createElement("td");
	var div01 = document.createElement("DIV");
	div01.align='center';
	/* var input01 = document.createElement("label")
	input01.id = "to_lb"+new_seq_no;*/
	div01.appendChild(document.createTextNode(new_seq_no)); 
	
	var td02 = document.createElement("td");
	var div02 = document.createElement("DIV");
	div02.align='center';
	var input02 = document.createElement("input")
	input02.name = "to_email_id";
	input02.id = "to_email_id"+new_seq_no;
	input02.type = "text";
	input02.className="form-control form-control-sm";
	input02.setAttribute("onblur","validateEmail(this);");
	input02.maxlength = "50";
	input02.value = "";
	input02.style.width='250px';
	
	div02.appendChild(input02);
	
	var td03 = document.createElement("td");
	td03.className="text-center"
	var input03 = document.createElement("a")
	input03.setAttribute("onclick","removeRow('"+row_new.id+"','"+new_seq_no+"');");
	input03.id = "minus"+new_seq_no;
	var i=document.createElement("i");
	i.className="fa fa-trash fa-lg";
	
	input03.appendChild(i);
	
	td01.appendChild(div01);
	td02.appendChild(div02);
	td03.appendChild(input03);
	
	row_new.appendChild(td01);
	row_new.appendChild(td02);
	row_new.appendChild(td03);
	
	tab_name.appendChild(row_new);
	
	document.forms[0].to_item_size.value=new_seq_no;
}

function Ccaddrow()
{
	var max_seq = document.forms[0].cc_item_size.value;
	var new_seq_no = parseInt(max_seq)+1;
	
	var tab_name = document.getElementById("cc_itemTab");
	var row_new = document.createElement("tr"); 
	row_new.id = 'cc_row'+new_seq_no;
	
	var td01 = document.createElement("td");
	var div01 = document.createElement("DIV");
	div01.align='center';
	/* var input01 = document.createElement("label")
	input01.id = "to_lb"+new_seq_no;*/
	div01.appendChild(document.createTextNode(new_seq_no)); 
	
	var td02 = document.createElement("td");
	var div02 = document.createElement("DIV");
	div02.align='center';
	var input02 = document.createElement("input")
	input02.name = "cc_email_id";
	input02.id = "cc_email_id"+new_seq_no;
	input02.type = "text";
	input02.className="form-control form-control-sm";
	input02.setAttribute("onblur","validateEmail(this);");
	input02.maxlength = "50";
	input02.value = "";
	input02.style.width='250px';
	
	div02.appendChild(input02);
	
	var td03 = document.createElement("td");
	td03.className="text-center"
	var input03 = document.createElement("a")
	input03.setAttribute("onclick","removeRow('"+row_new.id+"','"+new_seq_no+"');");
	input03.id = "minus"+new_seq_no;
	var i=document.createElement("i");
	i.className="fa fa-trash fa-lg";
	
	input03.appendChild(i);
	
	td01.appendChild(div01);
	td02.appendChild(div02);
	td03.appendChild(input03);
	
	row_new.appendChild(td01);
	row_new.appendChild(td02);
	row_new.appendChild(td03);
	
	tab_name.appendChild(row_new);
	
	document.forms[0].cc_item_size.value=new_seq_no;
}

function removeRow(row_id, seq_no)
{
	var row_cnt = document.forms[0].to_item_size.value;
	
	var row = document.getElementById(row_id);
	row.parentNode.removeChild(row);
	
	if(parseFloat(row_cnt) > 0)
	{
		document.forms[0].to_item_size.value = parseFloat(row_cnt)-1;
	}
}

function deleteExistingEntry(recipient_cd, seq_no, email)
{
	var a = confirm("Do you want to Delete below Email id?\n\n"+email);
	
	if(a)
	{
		document.forms[0].del_recipient_cd.value=recipient_cd;
		document.forms[0].del_seq_no.value=seq_no;
		
		document.forms[0].operation.value="DELETE";
		document.forms[0].submit();
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.mail_recipient_config.DataBean_Mail_Recipient_Config" id="mail_config" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sel_module_nm=request.getParameter("sel_module_nm")==null?"0":request.getParameter("sel_module_nm");
String sel_form_nm=request.getParameter("sel_form_nm")==null?"0":request.getParameter("sel_form_nm");
String sel_freq=request.getParameter("sel_freq")==null?"":request.getParameter("sel_freq");
String sel_type=request.getParameter("sel_type")==null?"":request.getParameter("sel_type");
String sel_seq_no=request.getParameter("sel_seq_no")==null?"":request.getParameter("sel_seq_no");
String company=request.getParameter("company")==null?"":request.getParameter("company");
if(company.equals(""))
{
	company=owner_cd;
}

mail_config.setCallFlag("MAIL_RECIPIENT_CONFIG");
mail_config.setSel_module_nm(sel_module_nm);
mail_config.setSel_form_nm(sel_form_nm);
mail_config.setSel_seq_no(sel_seq_no);
mail_config.setSel_type(sel_type);
mail_config.setSel_freq(sel_freq);
mail_config.setComp_cd(company);
mail_config.init();

Vector VMODULE_NM_MST = mail_config.getVMODULE_NM_MST();
Vector VFORM_NM_MST = mail_config.getVFORM_NM_MST();

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

Vector VTO_EMAIL = mail_config.getVTO_EMAIL();
Vector VTO_SEQ_NO = mail_config.getVTO_SEQ_NO();

Vector VCC_EMP_CD = mail_config.getVCC_EMP_CD();
Vector VCC_EMP_NM = mail_config.getVCC_EMP_NM();
Vector VCC_EMP_EMAIL = mail_config.getVCC_EMP_EMAIL();
Vector VCC_EMP_STATUS = mail_config.getVCC_EMP_STATUS();
Vector VCC_EMP_STATUS_NM = mail_config.getVCC_EMP_STATUS_NM();
Vector VCC_EMP_EXIST = mail_config.getVCC_EMP_EXIST();

Vector VCC_EMAIL = mail_config.getVCC_EMAIL();
Vector VCC_SEQ_NO = mail_config.getVCC_SEQ_NO();

String RECIPIENT_CD = mail_config.getRECIPIENT_CD();
String MODULE_NM = mail_config.getMODULE_NM();
String MENU_NM = mail_config.getMENU_NM();
String REPORT_FREQ = mail_config.getREPORT_FREQ();
String GENERATION_TYPE = mail_config.getGENERATION_TYPE();
String STOP_FLAG = mail_config.getSTOP_FLAG();
String MON = mail_config.getMON();
String TUE = mail_config.getTUE();
String WED = mail_config.getWED();
String THU = mail_config.getTHU();
String FRI = mail_config.getFRI();
String SAT = mail_config.getSAT();
String SUN = mail_config.getSUN();
String SEQ_NO = mail_config.getSEQ_NO();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Mail_Recipient_Config">

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
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
							Mail Recipient Configuration
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row" >
								<div class="col" align="right">
									<label class="form-label">
										<input type="checkbox" class="form-check-input" name="chk_sei" value="0" onclick="refresh();" <%if(company.equals("0")){%>checked<%} %>>&nbsp;<b>SEI Central&nbsp;<span style="color:var(--top_header_font_color);"><i class="fa fa-universal-access" aria-hidden="true"></i></b>
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
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Form/Report</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="mst_form_nm" onchange="refresh();">
										<option value="0">--Select--</option>
										<%for(int i=0; i<VFORM_NM_MST.size(); i++){ %>
										<option value="<%=VFORM_NM_MST.elementAt(i)%>"><%=VFORM_NM_MST.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].mst_form_nm.value='<%=sel_form_nm%>';</script>
								</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
					</div>
				<%if(!sel_module_nm.equals("0") && !sel_form_nm.equals("0")){ %>
					&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Email Support Details</label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Module</th>
										<th>Form/Reports</th>
										<th>Frequency</th>
										<th>Type</th>
										<th>Feature Support</th>
										<th>Status</th>
									</tr>
								</thead>
								<tbody>
									<%for(int i=0; i<VSUP_MENU_NM.size(); i++){ %>
									<tr class="content1">
										<td align="center">
											<input class="form-check-input" type="radio" onchange="refresh1('<%=VSUP_SEQ_NO.elementAt(i)%>','<%=VSUP_RPT_FREQ.elementAt(i) %>','<%=VSUP_GEN_TYPE.elementAt(i) %>');" <%if(VSUP_SEQ_NO.elementAt(i).equals(SEQ_NO)){ %>checked<%} %>>&nbsp;
											<%=VSUP_SEQ_NO.elementAt(i)%>
										</td> 
										<td align="center"><%=VSUP_MODULE_NM.elementAt(i)%></td>
										<td align="center"><%=VSUP_MENU_NM.elementAt(i) %></td>
										<td align="center"><%=VSUP_RPT_FREQ.elementAt(i) %></td>
										<td align="center"><%=VSUP_GEN_TYPE.elementAt(i) %></td>
										<td align="center"><%=VSUPPORT_FLAG_NM.elementAt(i)%></td>
										<td align="center">
											<font style="color:<%=VSTATUS_COLOR.elementAt(i)%>">
												<i class="fa fa-circle fa-lg" ></i>
												&nbsp;
											</font>
											<%=VSTATUS_NM.elementAt(i)%>
										</td>
									</tr>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
					&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Email Schedule</label>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Email Status</b></label>
								</div>
								<div class="col">
									<div class="form-check form-switch">
										<input class="form-check-input" name="stop_mail" type="checkbox" role="switch" id="flexSwitchCheckChecked" 
										<%if(STOP_FLAG.equals("N")){%>checked<%}%> onclick="setActiveInactive(this);">
									  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb">
									  		<%if(STOP_FLAG.equals("N")){%>Active<%}else{ %>In-Active<%} %>
									  	</label>
									  	<input type="hidden" name="txt_stop_mail" id="txt_stop_mail" value="<%=STOP_FLAG%>">
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Email Frequency</b></label>
								</div>
								<div class="col">
									<%=REPORT_FREQ%>
								</div>
							</div>
						</div>
						<div class="col-sm-9 col-xs-9 col-md-9" <%if(!GENERATION_TYPE.equals("Auto") || REPORT_FREQ.equals("Monthly")){%>style="display:none;"<%} %>>  
							<div class="form-group row">
								<div class="col-auto">
									<input class="form-check-input" type="checkbox" name="MON" id="MON" value="Y" <%if(MON.equals("Y")){ %>checked<%} %>>&nbsp;Monday
								</div>
								<div class="col-auto">
									<input class="form-check-input" type="checkbox" name="TUE" id="TUE" value="Y" <%if(TUE.equals("Y")){ %>checked<%} %>>&nbsp;Tuesday
								</div>
								<div class="col-auto">
									<input class="form-check-input" type="checkbox" name="WED" id="WED" value="Y" <%if(WED.equals("Y")){ %>checked<%} %>>&nbsp;Wednesday
								</div>
								<div class="col-auto">
									<input class="form-check-input" type="checkbox" name="THU" id="THU" value="Y" <%if(THU.equals("Y")){ %>checked<%} %>>&nbsp;Thursday
								</div>
								<div class="col-auto">
									<input class="form-check-input" type="checkbox" name="FRI" id="FRI" value="Y" <%if(FRI.equals("Y")){ %>checked<%} %>>&nbsp;Friday
								</div>
								<div class="col-auto">
									<input class="form-check-input" type="checkbox" name="SAT" id="SAT" value="Y" <%if(SAT.equals("Y")){ %>checked<%} %>>&nbsp;Saturday
								</div>
								<div class="col-auto">
									<input class="form-check-input" type="checkbox" name="SUN" id="SUN" value="Y" <%if(SUN.equals("Y")){ %>checked<%} %>>&nbsp;Sunday
								</div>
							</div>
						</div>
					</div>
					&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Email Recipients</label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading_to_list">
	  									<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse_to_list" aria-expanded="false" aria-controls="collapse_to_list">
							    		To:
							    		</button>
							    	</h2>
							    	<div id="collapse_to_list" class="accordion-collapse collapse" aria-labelledby="heading_to_list">
							      		<div class="accordion-body accor-body">
							        		<div class="row">
							        			<div class="col-md-6 col-sm-6 col-xs-6">
							        				<div class="form-group row">
														<div class="col">
															<div class="card cardmain">
																<div class="card-header cdheader">
																	<div class="d-flex justify-content-between">
																		<label class="form-label subheader1">Select Employee/ User(s)</label>
																	</div>
																</div>
																<div class="card-body cdbody">
																	<div class="table-responsive">
																		<table class="table table-bordered table-hover">
																			<thead>
																				<tr>
																					<th></th>
																					<th>Employee Name</th>
																					<th>Email</th>
																					<th>Status</th>
																				</tr>
																			</thead>
																			<tbody>
																				<%for(int i=0; i<VEMP_CD.size(); i++){ %>
																				<tr>
																					<td align="center">
																						<input class="form-check-input" type="checkbox" name="to_emp_chk" onclick="checkToEmpList(this,'<%=i%>');" <%if(VEMP_EXIST.elementAt(i).equals("Y")){%>checked<%} %>>
																					</td>
																					<td>
																						<%=VEMP_NM.elementAt(i)%>
																						<input type="hidden" name="to_emp_cd" id="to_emp_cd<%=i%>" value="<%=VEMP_CD.elementAt(i)%>">
																						<input type="hidden" name="to_emp_email" id="to_emp_email<%=i%>" value="<%=VEMP_EMAIL.elementAt(i)%>">
																						<input type="hidden" name="to_emp_flag" id="to_emp_flag<%=i%>" value="<%=VEMP_EXIST.elementAt(i)%>">
																					</td>
																					<td><%=VEMP_EMAIL.elementAt(i)%></td>
																					<td align="center"><%=VEMP_STATUS_NM.elementAt(i)%></td>
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
												<div class="col-md-6 col-sm-6 col-xs-6">
							        				<div class="form-group row">
														<div class="col">
															<div class="card cardmain">
																<div class="card-header cdheader">
																	<div class="d-flex justify-content-between">
																		<label class="form-label subheader1">Configure Email Id</label>
																		<div class="btn-group">
																			<label class="btn btn-outline-secondary subbtngrp" onclick="Toaddrow();"><i class="fa fa-plus-circle"></i>&nbsp;Add New</label>
																		</div>
																	</div>
																</div>
																<div class="card-body cdbody">
																	<div class="table-responsive">
																		<table class="table table-bordered table-hover">
																			<thead>
																				<tr>
																					<th>Sr#</th>
																					<th>Email</th>
																					<th>Action</th>
																				</tr>
																			</thead>
																			<tbody id="to_itemTab">
																			<%for(int i=0; i<VTO_EMAIL.size(); i++){ %>
																				<tr>
																					<td align="center"><%=i+1%></td>
																					<td align="center">
																						<div style="width:250px;">
																							<input type="text" class="form-control form-control-sm" name="to_email_id" id="to_email_id<%=i%>" value="<%=VTO_EMAIL.elementAt(i)%>">
																						</div>
																					</td>
																					<td align="center">
																						<div style="width:50px;">
																							<i class="fa fa-trash fa-lg" onclick="deleteExistingEntry('<%=RECIPIENT_CD%>','<%=VTO_SEQ_NO.elementAt(i)%>','<%=VTO_EMAIL.elementAt(i)%>')"></i>
																						</div>
																					</td>
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
											</div>
										</div>
									</div>
								</div>
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading_cc_list">
	  									<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse_cc_list" aria-expanded="false" aria-controls="collapse_cc_list">
							    		Cc:
							    		</button>
							    	</h2>
							    	<div id="collapse_cc_list" class="accordion-collapse collapse" aria-labelledby="heading_cc_list">
							      		<div class="accordion-body accor-body">
							        		<div class="row">
							        			<div class="col-md-6 col-sm-6 col-xs-6">
							        				<div class="form-group row">
														<div class="col">
															<div class="card cardmain">
																<div class="card-header cdheader">
																	<div class="d-flex justify-content-between">
																		<label class="form-label subheader1">Select Employee/ User(s)</label>
																	</div>
																</div>
																<div class="card-body cdbody">
																	<div class="table-responsive">
																		<table class="table table-bordered table-hover">
																			<thead>
																				<tr>
																					<th></th>
																					<th>Employee Name</th>
																					<th>Email</th>
																					<th>Status</th>
																				</tr>
																			</thead>
																			<tbody>
																				<%for(int i=0; i<VCC_EMP_CD.size(); i++){ %>
																				<tr>
																					<td align="center">
																						<input class="form-check-input" type="checkbox" name="cc_emp_chk" onclick="checkCcEmpList(this,'<%=i%>');" <%if(VCC_EMP_EXIST.elementAt(i).equals("Y")){%>checked<%} %>>
																					</td>
																					<td>
																						<%=VCC_EMP_NM.elementAt(i)%>
																						<input type="hidden" name="cc_emp_cd" id="cc_emp_cd<%=i%>" value="<%=VCC_EMP_CD.elementAt(i)%>">
																						<input type="hidden" name="cc_emp_email" id="cc_emp_email<%=i%>" value="<%=VCC_EMP_EMAIL.elementAt(i)%>">
																						<input type="hidden" name="cc_emp_flag" id="cc_emp_flag<%=i%>" value="<%=VCC_EMP_EXIST.elementAt(i)%>">
																					</td>
																					<td><%=VCC_EMP_EMAIL.elementAt(i)%></td>
																					<td align="center"><%=VCC_EMP_STATUS_NM.elementAt(i)%></td>
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
												<div class="col-md-6 col-sm-6 col-xs-6">
							        				<div class="form-group row">
														<div class="col">
															<div class="card cardmain">
																<div class="card-header cdheader">
																	<div class="d-flex justify-content-between">
																		<label class="form-label subheader1">Configure Email Id</label>
																		<div class="btn-group">
																			<label class="btn btn-outline-secondary subbtngrp" onclick="Ccaddrow();"><i class="fa fa-plus-circle"></i>&nbsp;Add New</label>
																		</div>
																	</div>
																</div>
																<div class="card-body cdbody">
																	<div class="table-responsive">
																		<table class="table table-bordered table-hover">
																			<thead>
																				<tr>
																					<th>Sr#</th>
																					<th>Email</th>
																					<th>Action</th>
																				</tr>
																			</thead>
																			<tbody id="cc_itemTab">
																			<%for(int i=0; i<VCC_EMAIL.size(); i++){ %>
																				<tr>
																					<td align="center"><%=i+1%></td>
																					<td align="center">
																						<div style="width:250px;">
																							<input type="text" class="form-control form-control-sm" name="cc_email_id" id="cc_email_id<%=i%>" value="<%=VCC_EMAIL.elementAt(i)%>">
																						</div>
																					</td>
																					<td align="center">
																						<div style="width:50px;">
																							<i class="fa fa-trash fa-lg" onclick="deleteExistingEntry('<%=RECIPIENT_CD%>','<%=VCC_SEQ_NO.elementAt(i)%>','<%=VCC_EMAIL.elementAt(i)%>')"></i>
																						</div>
																					</td>
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
											</div>
										</div>
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
				<%}else{ %>
				</div>
				<%} %>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="MAIL_RECIPIENT_CONFIG">
<input type="hidden" name="operation" value="INSERT">

<input type="hidden" name="mst_company" value="<%=company%>">
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