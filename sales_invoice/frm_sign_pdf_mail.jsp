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

function Do_close()
{
	window.opener.refresh();
	window.close();
}

function doSubmit(index)
{
	var execute_access = document.forms[0].execute_access.value;
	
	var email_from = document.getElementById("email_from"+index).value;
	var email_to = document.getElementById("email_to"+index).value;
	var email_cc = document.getElementById("email_cc"+index).value;
	var email_bcc = document.getElementById("email_bcc"+index).value;
	var subject = document.getElementById("subject"+index).value;
	//var attachment = document.getElementById("attachment"+index).value;
	var temp_email_body = document.getElementById("temp_email_body"+index).value;
	var pdf_type_nm = document.getElementById("pdf_type_nm"+index).value;
	
	document.getElementById("email_body"+index).value=temp_email_body;
	var email_body = document.getElementById("email_body"+index).value;
	var u = document.forms[0].u.value;
	var msg="";
	var flag=true;
	
	if(execute_access=="N")
	{
		msg+="You don't have Execute Rights!\n";
		flag=false;
	}
	
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
	/* if(trim(attachment)=="")
	{
		msg+="Attachment Missing!\n";
		flag=false;
	} */
	if(trim(email_body)=="")
	{
		msg+="Email Body Empty!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Send Mail with "+pdf_type_nm+"?")
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
	var email_bcc = document.forms[0].email_bcc;
	var subject = document.forms[0].subject;
	//var attachment = document.forms[0].attachment;
	
	var execute_access = document.forms[0].execute_access.value;
	
	var msg="";
	var flag=true;
	
	if(execute_access=="N")
	{
		msg+="You don't have Execute Rights!\n";
		flag=false;
	}
	
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
		/* if(trim(attachment[i].value)=="")
		{
			msg+="Attachment Missing!\n";
			flag=false;
		} */
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
	//var attachment = document.getElementById("attachment"+index);
	var email_body = document.getElementById("email_body"+index);
	var temp_index = document.getElementById("index"+index);
	var pdf_size = document.getElementById("pdf_size"+index);
	var pdf_type = document.getElementById("pdf_type"+index);
	
	email_from.disabled=false;
	email_to.disabled=false;
	email_cc.disabled=false;
	email_bcc.disabled=false;
	subject.disabled=false;
	//attachment.disabled=false;
	email_body.disabled=false;
	temp_index.disabled=false;
	pdf_type.disabled=false;
	
	for(var i=0;i<parseInt(pdf_size.value);i++)
	{
		document.getElementById("attachment"+index+"_"+i).disabled=false;
	}
}

function setDisable() 
{
	var email_from = document.forms[0].email_from;
	var email_to = document.forms[0].email_to;
	var email_cc = document.forms[0].email_cc;
	var email_bcc = document.forms[0].email_bcc;
	var subject = document.forms[0].subject;
	//var attachment = document.forms[0].attachment;
	var email_body = document.forms[0].email_body;
	var temp_index = document.forms[0].index;
	var pdf_size = document.forms[0].pdf_size;
	var pdf_type = document.forms[0].pdf_type;
	
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
				//attachment[i].disabled=true;
				email_body[i].disabled=true;
				temp_index[i].disabled=true;
				pdf_type[i].disabled=true;
				
				for(var j=0;j<parseInt(pdf_size.value);j++)
				{
					document.getElementById("attachment"+i+"_"+j).disabled=false;
				}
			}
		}
		else
		{
			email_from.disabled=true;
			email_to.disabled=true;
			email_cc.disabled=true;
			email_bcc.disabled=true;
			subject.disabled=true;
			//attachment.disabled=true;
			email_body.disabled=true;
			temp_index.disabled=true;
			pdf_type.disabled=true;
			
			for(var j=0;j<parseInt(pdf_size.value);j++)
			{
				document.getElementById("attachment0_"+j).disabled=false;
			}
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.sales_invoice.DataBean_Sales_Invoice" id="sales_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String plant_seq=request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String period_start_dt=request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
String bu_unit=request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String mail_pdf_type=request.getParameter("mail_pdf_type")==null?"":request.getParameter("mail_pdf_type");
String mail_inv_type=request.getParameter("mail_inv_type")==null?"":request.getParameter("mail_inv_type");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");

sales_inv.setCallFlag("SEND_INVOICE_MAIL");
sales_inv.setComp_cd(owner_cd);
sales_inv.setCounterparty_cd(counterparty_cd);
sales_inv.setAgmt_no(agmt_no);
sales_inv.setAgmt_rev_no(agmt_rev);
sales_inv.setCont_no(cont_no);
sales_inv.setCont_rev_no(cont_rev);
sales_inv.setContract_type(contract_type);
sales_inv.setPlant_seq(plant_seq);
sales_inv.setBilling_cycle(billing_cycle);
sales_inv.setPeriod_start_dt(period_start_dt);
sales_inv.setPeriod_end_dt(period_end_dt);
sales_inv.setBu_unit(bu_unit);
sales_inv.setFinancial_year(financial_year);
sales_inv.setBu_state_tin(bu_state_tin);
sales_inv.setInvoice_seq(invoice_seq);
sales_inv.setMail_pdf_type(mail_pdf_type);
sales_inv.setMail_inv_type(mail_inv_type);
sales_inv.setInvoice_type(invoice_type);
sales_inv.init();

Vector VMAIL_PDF_TYPE = sales_inv.getVMAIL_PDF_TYPE();
Vector VMAIL_PDF_TYPE_NM = sales_inv.getVMAIL_PDF_TYPE_NM();
Vector VMAIL_FROM_LIST = sales_inv.getVMAIL_FROM_LIST();
Vector VMAIL_TO_LIST = sales_inv.getVMAIL_TO_LIST();
Vector VMAIL_CC_LIST = sales_inv.getVMAIL_CC_LIST();
Vector VMAIL_BCC_LIST = sales_inv.getVMAIL_BCC_LIST();
Vector VMAIL_SUBJECT = sales_inv.getVMAIL_SUBJECT();
Vector VMAIL_ATTACHMENT = sales_inv.getVMAIL_ATTACHMENT();
Vector VMAIL_ATTACHMENT_PATH = sales_inv.getVMAIL_ATTACHMENT_PATH();
Vector VMAIL_BODY = sales_inv.getVMAIL_BODY();
Vector VMAIL_ANNEXURE_ATTACHMENT_PATH = sales_inv.getVMAIL_ANNEXURE_ATTACHMENT_PATH();
Vector VMAIL_ANNEXURE_ATTACHMENT_FLAG = sales_inv.getVMAIL_ANNEXURE_ATTACHMENT_FLAG();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;

String path ="/"+CommonVariable.work_dir+owner_cd+"/"+CommonVariable.signed_sales_inv_path;
String annexure_path="";
if(mail_inv_type.equals("F"))
{
	path ="/"+CommonVariable.work_dir+owner_cd+"/"+CommonVariable.signed_freeflow_inv_path;
	annexure_path="/"+CommonVariable.work_dir+owner_cd+"/"+CommonVariable.freeflow_annexure_path+"/"+invoice_type+"_"+financial_year+"_"+bu_state_tin+"_"+invoice_seq+"/";
}
else
{
	annexure_path="/"+CommonVariable.work_dir+owner_cd+"/"+CommonVariable.crdr_annexure_path+"/"+owner_cd+"_"+financial_year+"_"+bu_state_tin+"_"+invoice_seq+"/";
}
String file_path = request.getRealPath(path);
String annexure_file_path = request.getRealPath(annexure_path);
//System.out.println(file_path);
%>
<body <%if(!msg.equals("")){ %>onload="Do_close();"<%} %>>
<%@ include file="../home/loading.jsp"%>
<form method="post" action="../servlet/Frm_Sales_Invoice">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Send Mail
					    </div>
					    <%if(VMAIL_PDF_TYPE.size()>1){ %>
					    <div class="btn-group">
							<label class="btn btn-outline-secondary btngrp" onclick="doSendAll()">
								<i class="fa fa-envelope-o fa-lg"></i>&nbsp;Send All
							</label>
						</div>
						<%} %>
					</div>
				</div>
				<div class="card-body cdbody">
				<%for(int i=0; i<VMAIL_PDF_TYPE.size(); i++)
				{ %>
					<%if(i!=0){ %>&nbsp;<%} %>
					<div class="accordion">
						<div class="accordion-item accor_item">
							<h2 class="accordion-header" id="heading<%=i%>">
 								<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=i%>" aria-expanded="false" aria-controls="collapse<%=i%>">
					    		<%=VMAIL_PDF_TYPE_NM.elementAt(i)%>
					      		</button>
					    	</h2>
					    	<div id="collapse<%=i%>" class="accordion-collapse collapse" aria-labelledby="heading<%=i%>">
					      		<div class="accordion-body accor-body">	
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
														<input type="hidden" class="form-control" size="100" name="pdf_type_nm" id="pdf_type_nm<%=i%>" readonly="readonly" style="background-color: white;" value="<%=VMAIL_PDF_TYPE_NM.elementAt(i)%>" disabled>
														<input type="hidden" class="form-control" size="100" name="pdf_type" id="pdf_type<%=i%>" readonly="readonly" style="background-color: white;" value="<%=VMAIL_PDF_TYPE.elementAt(i)%>" disabled>
													</div>
													<div class="col-12">
														<div class="input-group mb-1">				
															<span class="input-group-text" style="font-weight: bold">To...</span>
															<input type="text" class="form-control" name="email_to" id="email_to<%=i%>" size="100" value="<%=VMAIL_TO_LIST.elementAt(i)%>" readonly="readonly" style="background-color: white;" disabled>
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
															<span class="input-group-text" style="font-weight: bold">Bcc..</span>
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
														<input type="hidden" class="form-control" name="index" id="index<%=i%>" value="<%=i%>" disabled>
													</div>
													<div class="col-12">
														<%-- <div class="input-group mb-1">				
															<span class="input-group-text" style="font-weight: bold">Attached:</span>
															<span class="form-control" style="cursor: pointer;" title="Click to View Attachment"
															onclick="openPdfFile('<%=url%><%=VMAIL_ATTACHMENT_PATH.elementAt(i)%><%=VMAIL_ATTACHMENT.elementAt(i)%>');">
																<i class="fa fa-file-pdf-o" style="color:red;"></i>&nbsp;<%=VMAIL_ATTACHMENT.elementAt(i) %>
															</span>
														</div>
														<input type="hidden" class="form-control" name="attachment" id="attachment<%=i%>" title="Click to View Attachment" size="100" 
														value="<%=file_path%><%=VMAIL_ATTACHMENT.elementAt(i)%>" disabled> --%>
														
														<div class="input-group mb-1">				
															<span class="input-group-text" style="font-weight: bold">Attached:</span>
															<span class="form-control" style="cursor: pointer;" title="Click to View Attachment">
															<%for(int k=0;k<((Vector)VMAIL_ATTACHMENT.elementAt(i)).size();k++){ %>
																<spnn
																<%if(((Vector)VMAIL_ANNEXURE_ATTACHMENT_FLAG.elementAt(i)).elementAt(k).equals("Y")){ %> 
																	onclick="openPdfFile('<%=url%><%=VMAIL_ANNEXURE_ATTACHMENT_PATH.elementAt(i)%><%=((Vector)VMAIL_ATTACHMENT.elementAt(i)).elementAt(k)%>');"
																<%}else{ %>
																	onclick="openPdfFile('<%=url%><%=VMAIL_ATTACHMENT_PATH.elementAt(i)%><%=((Vector)VMAIL_ATTACHMENT.elementAt(i)).elementAt(k)%>');"
																<%}%>	
																>
																	<i class="fa fa-file-pdf-o" style="color:red;"></i>&nbsp;<%=((Vector)VMAIL_ATTACHMENT.elementAt(i)).elementAt(k)%>
																</spnn>
															<%} %>
															</span>
															<input type="hidden" name="pdf_size" id="pdf_size<%=i%>" value="<%=((Vector)VMAIL_ATTACHMENT.elementAt(i)).size()%>">
														</div>
														<%for(int k=0;k<((Vector)VMAIL_ATTACHMENT.elementAt(i)).size();k++){ %>
															<input type="hidden" class="form-control" name="attachment<%=i%>" id="attachment<%=i%>_<%=k%>" title="Click to View Attachment" size="100" 
															<%if(((Vector)VMAIL_ANNEXURE_ATTACHMENT_FLAG.elementAt(i)).elementAt(k).equals("Y")){ %>
																value="<%=annexure_file_path%><%=File.separator%><%=((Vector)VMAIL_ATTACHMENT.elementAt(i)).elementAt(k)%>" 
															<%}else{ %>
																value="<%=file_path%><%=File.separator%><%=((Vector)VMAIL_ATTACHMENT.elementAt(i)).elementAt(k)%>" 
															<%}%> disabled>
														<%} %>																
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
								</div>
							</div>
						</div>
					</div>				
				<%} %>						
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="SEND_INVOICE_MAIL">

<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="invoice_seq" value="<%=invoice_seq%>">
<input type="hidden" name="bu_state_tin" value="<%=bu_state_tin%>">
<input type="hidden" name="invoice_type" value="<%=invoice_type%>">
<input type="hidden" name="mail_inv_type" value="<%=mail_inv_type%>">

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