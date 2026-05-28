<%@page import="java.io.File"%>
<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function openPdfFile(url)
{
	window.open(url);
}

function Do_close(msg,msg_type)
{
	//alert(msg)
	window.opener.refresh_rpt(msg,msg_type);
	window.close();
}
function allMails(all_mail_sent)
{
	var msg="";
	var msg_type="";
	if(all_mail_sent=="Y")
	{
		msg="All Mails have been Sent!";
		msg_type="S";
	}
	else 
	{
		msg="Mail Sent Failed!";
		msg_type="E";
	}
	//alert(msg)
	window.opener.refresh_rpt(msg,msg_type);
	window.close();
}

function doSubmit(index)
{
	/*var email_from = document.forms[0].email_from.value;
	var email_to = document.forms[0].email_to.value;
	var email_cc = document.forms[0].email_cc.value;
	var subject = document.forms[0].subject.value;
	var attachment = document.forms[0].attachment.value;
	var email_body = document.forms[0].email_body.value;*/
	
	var email_from = document.getElementById("email_from"+index).value;
	var email_to = document.getElementById("email_to"+index).value;
	var email_cc = document.getElementById("email_cc"+index).value;
	var subject = document.getElementById("subject"+index).value;
	var attachment = document.getElementById("attachment"+index).value;
	var temp_email_body = document.getElementById("temp_email_body"+index).value;
	var u = document.forms[0].u.value;
	
	document.getElementById("email_body"+index).value=temp_email_body;
	var email_body = document.getElementById("email_body"+index).value;
	
	var msg="";
	var flag=true;
	
	if(trim(email_from)=="")
	{
		msg+="From Mail Id Missing!\n";
		flag=false;
	}
	if(trim(email_to)=="")
	{
		msg+="To Mail Id Missing!\n";
		flag=false;
	}
	/* if(trim(email_cc)=="")
	{
		msg+="Cc Mail Id Missing!\n";
		flag=false;
	} */
	if(trim(subject)=="")
	{
		msg+="Subject line cann't be blank!\n";
		flag=false;
	}
	if(trim(attachment)=="")
	{
		msg+="Attachment Missing!\n";
		flag=false;
	}
	if(trim(email_body)=="")
	{
		msg+="Email Body Empty!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Send Mail?")
		if(a)
		{
			setEnable(index);
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
		setDisable();
	}
}

function doSendAll()
{
	var email_from = document.forms[0].email_from;
	var email_to = document.forms[0].email_to;
	var email_cc = document.forms[0].email_cc;
	var subject = document.forms[0].subject;
	var attachment = document.forms[0].attachment;
	
	var msg="";
	var flag=true;
	
	for(var i=0;i<subject.length;i++)
	{ 
		setEnable(i);
		
		var temp_email_body = document.getElementById("temp_email_body"+i).value;
		document.getElementById("email_body"+i).value=temp_email_body;
		
		var email_body = document.forms[0].email_body;
		
		if(trim(email_from[i].value)=="")
		{
			msg+="From Mail Id Missing!\n";
			flag=false;
		}
		if(trim(email_to[i].value)=="")
		{
			msg+="To Mail Id Missing!\n";
			flag=false;
		}
		/* if(trim(email_cc[i].value)=="")
		{
			msg+="Cc Mail Id Missing!\n";
			flag=false;
		} */
		if(trim(subject[i].value)=="")
		{
			msg+="Subject line cann't be blank!\n";
			flag=false;
		}
		if(trim(attachment[i].value)=="")
		{
			msg+="Attachment Missing!\n";
			flag=false;
		}
		if(trim(email_body[i].value)=="")
		{
			msg+="Email Body Empty!\n";
			flag=false;
		}
	}	
	
	if(flag)
	{
		var a = confirm("Do you want to Send All Mails together?")
		if(a)
		{			
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
		setDisable();
	}
}

function setEnable(index) 
{
	var email_from = document.getElementById("email_from"+index);
	var email_to = document.getElementById("email_to"+index);
	var email_cc = document.getElementById("email_cc"+index);
	var email_bcc = document.getElementById("email_bcc"+index);
	var subject = document.getElementById("subject"+index);
	var attachment = document.getElementById("attachment"+index);
	var email_body = document.getElementById("email_body"+index);
	
	email_from.disabled=false;
	email_to.disabled=false;
	email_cc.disabled=false;
	email_bcc.disabled=false;
	subject.disabled=false;
	attachment.disabled=false;
	email_body.disabled=false;
}

function setDisable() 
{
	var email_from = document.forms[0].email_from;
	var email_to = document.forms[0].email_to;
	var email_cc = document.forms[0].email_cc;
	var email_bcc = document.forms[0].email_bcc;
	var subject = document.forms[0].subject;
	var attachment = document.forms[0].attachment;
	var email_body = document.forms[0].email_body;
	
	if(email_to!=null && email_to!=undefined)
	{
		if(email_to.length!=undefined)
		{
			for(var i=0;i<email_to.length; i++)
			{
				email_from[i].disabled=true;
				email_to[i].disabled=true;
				email_cc[i].disabled=true;
				email_bcc[i].disabled=true;
				subject[i].disabled=true;
				attachment[i].disabled=true;
				email_body[i].disabled=true;
			}
		}
		else
		{
			email_from.disabled=true;
			email_to.disabled=true;
			email_cc.disabled=true;
			email_bcc.disabled=true;
			subject.disabled=true;
			attachment.disabled=true;
			email_body.disabled=true;
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DataBean_ContractMgmt" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String prevdate = utildate.getPreviousDate();

String gas_dt = request.getParameter("gas_dt")==null?prevdate:request.getParameter("gas_dt");

String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");
String ip=""+session.getAttribute("ip")==null?"":""+session.getAttribute("ip");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String plant_seq=request.getParameter("plant_seq")==null?"0":request.getParameter("plant_seq");
String contact_person_cd=request.getParameter("contact_person_cd")==null?"":request.getParameter("contact_person_cd");
String file=request.getParameter("file")==null?"":request.getParameter("file");

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;

String path ="/"+CommonVariable.work_dir+owner_cd+"/"+CommonVariable.nom_to_transporter_pdf_path;
String file_path = request.getRealPath(path);

Vector VMAIL_FROM_LIST = new Vector();
Vector VMAIL_TO_LIST = new Vector();
Vector VMAIL_CC_LIST = new Vector();
Vector VMAIL_BCC_LIST = new Vector();
Vector VMAIL_SUBJECT = new Vector();
Vector VMAIL_ATTACHMENT = new Vector();
Vector VMAIL_ATTACHMENT_PATH = new Vector();
Vector VMAIL_BODY = new Vector();

if(file.equals("ALL_MAIL"))
{
	String[] all_counterparty_cd = counterparty_cd.split("@@");
	String[] all_plant_seq = plant_seq.split("@@");
	String[] all_contact_person_cd = contact_person_cd.split("@@");
	
	for(int i=0;i<all_counterparty_cd.length;i++)
	{
		cont_mgmt.setCallFlag("SEND_MAIL_SELLER_TO_TRANS");
		cont_mgmt.setComp_cd(owner_cd);
		cont_mgmt.setGas_dt(gas_dt);
		cont_mgmt.setCounterparty_cd(all_counterparty_cd[i]);
		cont_mgmt.setPlant_seq(all_plant_seq[i]);
		cont_mgmt.setContact_person_cd(all_contact_person_cd[i]);
		cont_mgmt.setEmp_cd(emp_cd);
		cont_mgmt.setFile_path(file_path);
		cont_mgmt.setFile(file);
		cont_mgmt.setIp(ip);
		cont_mgmt.setForm_id(formCd);
		cont_mgmt.setForm_name(formNm);
		cont_mgmt.setMod_id(mod_cd);
		cont_mgmt.setMod_name(mod_nm);
		cont_mgmt.init();
	}
}
else
{

	cont_mgmt.setCallFlag("SEND_MAIL_SELLER_TO_TRANS");
	cont_mgmt.setComp_cd(owner_cd);
	cont_mgmt.setGas_dt(gas_dt);
	cont_mgmt.setCounterparty_cd(counterparty_cd);
	cont_mgmt.setPlant_seq(plant_seq);
	cont_mgmt.setContact_person_cd(contact_person_cd);
	cont_mgmt.setEmp_cd(emp_cd);
	cont_mgmt.setFile_path(file_path);
	cont_mgmt.setFile(file);
	cont_mgmt.init();
	
	VMAIL_FROM_LIST = cont_mgmt.getVMAIL_FROM_LIST();
	VMAIL_TO_LIST = cont_mgmt.getVMAIL_TO_LIST();
	VMAIL_CC_LIST = cont_mgmt.getVMAIL_CC_LIST();
	VMAIL_BCC_LIST = cont_mgmt.getVMAIL_BCC_LIST();
	VMAIL_SUBJECT = cont_mgmt.getVMAIL_SUBJECT();
	VMAIL_ATTACHMENT = cont_mgmt.getVMAIL_ATTACHMENT();
	VMAIL_ATTACHMENT_PATH = cont_mgmt.getVMAIL_ATTACHMENT_PATH();
	VMAIL_BODY = cont_mgmt.getVMAIL_BODY();
}
String all_mail_sent=cont_mgmt.getAll_mail_sent();
%>
<%if(file.equals("ALL_MAIL")){ %>
<script>
allMails('<%=all_mail_sent%>');
</script>
<%}else{ %>
<body <%if(!msg.equals("")){ %>onload="Do_close('<%=msg%>','<%=msg_type%>');"<%} %>>
<%@ include file="../home/loading.jsp"%>
<form method="post" action="../servlet/Frm_ContractMgmt">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Send Mail
					    </div>
					    <!-- <div class="btn-group">
							<label class="btn btn-outline-secondary btngrp" onclick="doSendAll()">
								<i class="fa fa-envelope-o fa-lg"></i>&nbsp;Send All
							</label>
						</div> -->
					</div>
				</div>
				<div class="card-body cdbody">
				<%for(int i=0; i<VMAIL_TO_LIST.size(); i++)
				{ %>
					<%-- <%if(i!=0){ %>&nbsp;<%} %>
					<div class="accordion">
						<div class="accordion-item accor_item">
							<h2 class="accordion-header" id="heading<%=i%>">
 								<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=i%>" aria-expanded="false" aria-controls="collapse<%=i%>">
					    		<%=VMAIL_PDF_TYPE_NM.elementAt(i)%>
					      		</button>
					    	</h2>
					    	<div id="collapse<%=i%>" class="accordion-collapse collapse" aria-labelledby="heading<%=i%>">
					      		<div class="accordion-body accor-body">	 --%>
									<div class="card-body cdbody" style="background:#c2d1f0;">
										<div class="row m-b-5">
											<div class="col-auto">
												<div class="row h-100">
													<div class="col-12">
														<div class="btn card h-100 p-2" onclick="doSubmit('<%=i%>');" style="text-align:center;background: #e9ecef;border: 1px solid #ced4da; justify-content: center;">
															<span>
																<i class="fa fa-envelope fa-2x" style="color:var(--header_color)"></i>
																<br>
																<span style="font-weight: bold">Send Mail</span>
															</span>
														</div>
													</div>
												</div>
											</div>
											<div class="col">
												<div class="row">
													<div class="col-12">
														<div class="input-group mb-1">				
															<span class="input-group-text" style="font-weight: bold">From:</span>
															<input type="text" class="form-control" size="100" name="email_from" id="email_from<%=i%>" readonly="readonly" style="background-color: white;" value="<%=VMAIL_FROM_LIST.elementAt(i)%>" disabled>
														</div>
														<input type="hidden" class="form-control" size="100" name="pdf_type_nm" id="pdf_type_nm<%=i%>" readonly="readonly" style="background-color: white;" value="<%//=VMAIL_PDF_TYPE_NM.elementAt(i)%>" disabled>
													</div>
													<div class="col-12">
														<div class="input-group mb-1">				
															<span class="input-group-text" style="font-weight: bold">To...</span>
															<input type="text" class="form-control" name="email_to" id="email_to<%=i%>" size="100" readonly="readonly" style="background-color: white;" value="<%=VMAIL_TO_LIST.elementAt(i)%>" disabled>
														</div>
													</div>
													<div class="col-12">
														<div class="input-group mb-1">				
															<span class="input-group-text" style="font-weight: bold">Cc...</span>
															<input type="text" class="form-control" name="email_cc" id="email_cc<%=i%>" size="100" value="<%=VMAIL_CC_LIST.elementAt(i)%>" readonly="readonly" style="background-color: white;" disabled>
														</div>
													</div>
													<div class="col-12">
														<div class="input-group mb-1">				
															<span class="input-group-text" style="font-weight: bold">Bcc...</span>
															<input type="text" class="form-control" name="email_bcc" id="email_bcc<%=i%>" size="100" value="<%=VMAIL_BCC_LIST.elementAt(i)%>" readonly="readonly" style="background-color: white;" disabled>
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="row m-b-5">
											<div class="col">	
												<div class="row">
													<div class="col-12">
														<div class="input-group mb-1">				
															<span class="input-group-text" style="font-weight: bold">Subject:</span>
															<input type="text" class="form-control" name="subject" id="subject<%=i%>" size="100" value="<%=VMAIL_SUBJECT.elementAt(i)%>" 
																readonly="readonly" style="background-color: white;" disabled>
														</div>
													</div>
													<div class="col-12">
														<div class="input-group mb-1">				
															<span class="input-group-text" style="font-weight: bold">Attached:</span>
															<span class="form-control" style="cursor: pointer;" title="Click to View Attachment"
															onclick="openPdfFile('<%=url%><%=VMAIL_ATTACHMENT_PATH.elementAt(i)%><%=VMAIL_ATTACHMENT.elementAt(i)%>');">
																<i class="fa fa-file-pdf-o" style="color:red;"></i>&nbsp;<%=VMAIL_ATTACHMENT.elementAt(i) %>
															</span>
														</div>
														<input type="hidden" class="form-control" name="attachment" id="attachment<%=i%>" title="Click to View Attachment" size="100" 
														value="<%=file_path%><%=File.separator%><%=VMAIL_ATTACHMENT.elementAt(i)%>" disabled>																
													</div>				
												</div>
											</div>
										</div>	
									</div>
									<div class="card-body cdbody" style="background:#e6e6e6;">	
										<div class="form-group">
							  				<textarea class="form-control" rows="12" cols="95" style="overflow: auto;" name="temp_email_body" id="temp_email_body<%=i%>"><%=VMAIL_BODY.elementAt(i) %></textarea>
							  				<textarea class="form-control" rows="12" cols="95" style="overflow: auto;display:none;" name="email_body" id="email_body<%=i%>" disabled></textarea>
										</div>
									</div>	
								<!-- </div>
							</div>
						</div>
					</div> -->				
				<%} %>						
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="SEND_MAIL">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="gas_dt" value="<%=gas_dt%>">
<input type="hidden" name="plant_seq" value="<%=plant_seq%>">
<input type="hidden" name="mail_file_nm" value="frm_seller_nom_to_trans_send_mail.jsp">

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
<%} %>
</html>