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

function Do_close(msg)
{
	alert(msg)
	window.opener.refresh();
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


function setEnable(index) 
{
	var email_from = document.getElementById("email_from"+index);
	var email_to = document.getElementById("email_to"+index);
	var email_cc = document.getElementById("email_cc"+index);
	var subject = document.getElementById("subject"+index);
	var attachment = document.getElementById("attachment"+index);
	var email_body = document.getElementById("email_body"+index);
	
	email_from.disabled=false;
	email_to.disabled=false;
	email_cc.disabled=false;
	subject.disabled=false;
	attachment.disabled=false;
	email_body.disabled=false;
}

function setDisable() 
{
	var email_from = document.forms[0].email_from;
	var email_to = document.forms[0].email_to;
	var email_cc = document.forms[0].email_cc;
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
			subject.disabled=true;
			attachment.disabled=true;
			email_body.disabled=true;
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_Buy_Nom_Alloc_Mgmt" id="alloc" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String prevdate = utildate.getPreviousDate();


String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String report_dt=request.getParameter("report_dt")==null?"":request.getParameter("report_dt");
String plant_seq=request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String to_contact=request.getParameter("to_contact")==null?"":request.getParameter("to_contact");
String from_contact=request.getParameter("from_contact")==null?"":request.getParameter("from_contact");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String bu_plant_seq=request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
String rmk=request.getParameter("rmk")==null?"":request.getParameter("rmk");
String frq_type=request.getParameter("frq_type")==null?"":request.getParameter("frq_type");
String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
String month_nm = request.getParameter("month_nm")==null?"":request.getParameter("month_nm");

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;

String path ="/"+CommonVariable.work_dir+owner_cd+"//purchase//buyer_nom_to_trader//";
String file_path = request.getRealPath(path);

alloc.setCallFlag("SEND_MAIL_BUYER_NOM_TO_TRADER");
alloc.setCounterparty_cd(counterparty_cd);
alloc.setComp_cd(owner_cd);
alloc.setReport_dt(report_dt);
alloc.setTo_contact(to_contact);
alloc.setFrom_contact(from_contact);
alloc.setPlant_seq(plant_seq);
alloc.setCont_no(cont_no);
alloc.setAgmt_no(agmt_no);
alloc.setContract_type(contract_type);
alloc.setBu_plant_seq(bu_plant_seq);
alloc.setFrq_type(frq_type);
alloc.setFile_path(file_path);
alloc.init();

Vector VMAIL_FROM_LIST = alloc.getVMAIL_FROM_LIST();
Vector VMAIL_TO_LIST = alloc.getVMAIL_TO_LIST();
Vector VMAIL_CC_LIST = alloc.getVMAIL_CC_LIST();
Vector VMAIL_SUBJECT = alloc.getVMAIL_SUBJECT();
Vector VMAIL_ATTACHMENT = alloc.getVMAIL_ATTACHMENT();
Vector VMAIL_ATTACHMENT_PATH = alloc.getVMAIL_ATTACHMENT_PATH();
Vector VMAIL_BODY = alloc.getVMAIL_BODY();

%>
<body <%if(!msg.equals("")){ %>onload="Do_close('<%=msg%>');"<%} %>>
<%@ include file="../home/loading.jsp"%>
<form method="post" action="../servlet/Frm_Buy_Nom_Alloc_Mgmt">
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