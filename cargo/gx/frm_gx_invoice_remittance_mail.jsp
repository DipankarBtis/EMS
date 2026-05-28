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
	var u = document.forms[0].u.value;
	var temp_email_body = document.getElementById("temp_email_body"+index).value;
	//var pdf_type_nm = document.getElementById("pdf_type_nm"+index).value;
	
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
		var a = confirm("Do you want to Send Mail with PDFs?")
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
	var subject = document.getElementById("subject"+index);
	//var attachment = document.getElementById("attachment"+index);
	var email_body = document.getElementById("email_body"+index);
	
	email_from.disabled=false;
	email_to.disabled=false;
	email_cc.disabled=false;
	subject.disabled=false;
	//attachment.disabled=false;
	email_body.disabled=false;
	
	var attachment = document.forms[0].attachment;
	if(attachment!=null && attachment!=undefined)
	{
		if(attachment.length!=undefined)
		{
			for(var i=0;i<attachment.length;i++)
			{
				attachment[i].disabled=false;
			}
		}
		else
		{
			attachment.disabled=false;
		}
	}
}

function setDisable() 
{
	var email_from = document.forms[0].email_from;
	var email_to = document.forms[0].email_to;
	var email_cc = document.forms[0].email_cc;
	var subject = document.forms[0].subject;
	//var attachment = document.forms[0].attachment;
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
				//attachment[i].disabled=true;
				email_body[i].disabled=true;
			}
		}
		else
		{
			email_from.disabled=true;
			email_to.disabled=true;
			email_cc.disabled=true;
			subject.disabled=true;
			//attachment.disabled=true;
			email_body.disabled=true;
		}
	}
	
	var attachment = document.forms[0].attachment;
	if(attachment!=null && attachment!=undefined)
	{
		if(attachment.length!=undefined)
		{
			for(var i=0;i<attachment.length;i++)
			{
				attachment[i].disabled=true;
			}
		}
		else
		{
			attachment.disabled=true;
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gx.DataBean_Gx_Invoice" id="gx_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String gx_counterparty_cd=request.getParameter("gx_counterparty_cd")==null?"":request.getParameter("gx_counterparty_cd");
String gx_bu_unit = request.getParameter("gx_bu_unit")==null?"":request.getParameter("gx_bu_unit");
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

gx_inv.setCallFlag("SEND_GX_TXN_REMITTANCE_MAIL");
gx_inv.setComp_cd(owner_cd);
gx_inv.setCounterparty_cd(counterparty_cd);
gx_inv.setAgmt_no(agmt_no);
gx_inv.setAgmt_rev_no(agmt_rev);
gx_inv.setCont_no(cont_no);
gx_inv.setCont_rev_no(cont_rev);
gx_inv.setContract_type(contract_type);
gx_inv.setGx_counterparty_cd(gx_counterparty_cd);
gx_inv.setGx_bu_unit(gx_bu_unit);
gx_inv.setBu_unit(bu_unit);
gx_inv.setFinancial_year(financial_year);
gx_inv.setInvoice_seq(invoice_seq);
gx_inv.setMail_inv_type(mail_inv_type);
gx_inv.setInvoice_type(invoice_type);
gx_inv.init();

Vector VMAIL_FROM_LIST = gx_inv.getVMAIL_FROM_LIST();
Vector VMAIL_TO_LIST = gx_inv.getVMAIL_TO_LIST();
Vector VMAIL_CC_LIST = gx_inv.getVMAIL_CC_LIST();
Vector VMAIL_SUBJECT = gx_inv.getVMAIL_SUBJECT();
Vector VMAIL_ATTACHMENT = gx_inv.getVMAIL_ATTACHMENT();
Vector VMAIL_ATTACHMENT_PATH = gx_inv.getVMAIL_ATTACHMENT_PATH();
Vector VMAIL_BODY = gx_inv.getVMAIL_BODY();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;

String path ="/"+CommonVariable.work_dir+owner_cd+"/"+CommonVariable.gx_inv_path;
if(mail_inv_type.equals("F"))
{
	path ="/"+CommonVariable.work_dir+owner_cd+"/"+CommonVariable.gx_freeflow_inv_path;
}
String file_path = request.getRealPath(path);
//System.out.println(file_path);
%>
<body <%if(!msg.equals("")){ %>onload="Do_close();"<%} %>>
<%@ include file="../home/loading.jsp"%>
<form method="post" action="../servlet/Frm_Gx_Invoice">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Send Mail
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%for(int i=0; i<VMAIL_SUBJECT.size(); i++)
				{ %>
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
											<span class="form-control" style="cursor: pointer;" title="Click to View Attachment">
											<%for(int k=0;k<((Vector)VMAIL_ATTACHMENT.elementAt(i)).size();k++){ %>
												<spnn onclick="openPdfFile('<%=url%><%=VMAIL_ATTACHMENT_PATH.elementAt(i)%><%=((Vector)VMAIL_ATTACHMENT.elementAt(i)).elementAt(k)%>');">
													<i class="fa fa-file-pdf-o" style="color:red;"></i>&nbsp;<%=((Vector)VMAIL_ATTACHMENT.elementAt(i)).elementAt(k)%>
												</spnn>
											<%} %>
											</span>
										</div>
										<%for(int k=0;k<((Vector)VMAIL_ATTACHMENT.elementAt(i)).size();k++){ %>
											<input type="hidden" class="form-control" name="attachment" id="attachment<%=i%>" title="Click to View Attachment" size="100" 
											value="<%=file_path%><%=File.separator%><%=((Vector)VMAIL_ATTACHMENT.elementAt(i)).elementAt(k)%>" disabled>
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
				<%} %>						
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="SEND_INVOICE_MAIL">

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