<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refreshParent(pdfLen,accroid)
{
	window.opener.refresh(accroid);
	if(parseInt(pdfLen)>1)
	{
		//alert("PDF has been Generated!")
		window.close();
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DB_Pdf_Generation1" id="pdfFile" scope="request"/>
<%

String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
String operation=request.getParameter("operation")==null?"INSERT":request.getParameter("operation");
String activityFlag=request.getParameter("activityFlag")==null?"":request.getParameter("activityFlag");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String sel_inv_no = request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String crdr_gen_type = request.getParameter("crdr_gen_type")==null?"":request.getParameter("crdr_gen_type");
String crdr_type = request.getParameter("crdr_type")==null?"":request.getParameter("crdr_type");
String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String bu_plant_seq = request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
String print_pdf_type = request.getParameter("print_pdf_type")==null?"":request.getParameter("print_pdf_type");
String crdr_inv_no = request.getParameter("crdr_inv_no")==null?"":request.getParameter("crdr_inv_no");
String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

String temp_print_pdf_type=print_pdf_type;
String[] pdf_type=temp_print_pdf_type.split(",");

String file_nm="";
String pdfpath="";
for(int i=0; i<pdf_type.length; i++)
{
	print_pdf_type=pdf_type[i];
	
	HttpServletRequest req = request;
	pdfFile.setRequest(req);
	pdfFile.setComp_cd(owner_cd);
	pdfFile.setCounterparty_cd(counterparty_cd);
	pdfFile.setSel_inv_no(sel_inv_no);
	pdfFile.setInv_seq(invoice_seq);
	pdfFile.setCrdr_type(crdr_type);
	pdfFile.setBu_plant_seq(bu_plant_seq);
	pdfFile.setPlant_seq(plant_seq);
	pdfFile.setBu_state_tin(bu_state_tin);
	pdfFile.setFinancial_year(financial_year);
	pdfFile.setCrdr_inv_no(crdr_inv_no);
	pdfFile.setInvoice_type(invoice_type);
	pdfFile.setComp_logo(owner_logo);
	pdfFile.setPrint_pdf_type(print_pdf_type);
	pdfFile.setEmp_cd(emp_cd);
	pdfFile.setForm_id(formCd);
	pdfFile.setForm_nm(formNm);
	pdfFile.setMod_cd(mod_cd);
	pdfFile.setMod_nm(mod_nm);
	pdfFile.setCallFlag("PDF_DERV_CRDR_INVOICE");
	pdfFile.init();
	
	String file_url = pdfFile.getFile_url();
	file_nm = pdfFile.getFile_nm();
	if(invoice_type.equals("R"))
	{
		pdfpath = file_url+"/work_data"+owner_cd+""+CommonVariable.derv_remittance_path+""+file_nm;
	}
	else
	{
		pdfpath = file_url+"/work_data"+owner_cd+""+CommonVariable.derv_inv_path+""+file_nm;
	}
}

String msg1=pdfFile.getMsg();
String msg_type1=pdfFile.getMsg_type();
%>
<body <%if(!file_nm.equals("")){%>onload="refreshParent('<%=pdf_type.length%>','<%=accroid%>');"<%}%>>
<%if(pdf_type.length==1){ %>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<%if(!msg1.equals("")){
				if(msg_type1.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg1)%></div>
				<%}else if(msg_type1.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg1)%></div>
				<%}
			} %>
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Derivatives Credit/Debit Note PDF
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="ratio ratio-1x1">
							  <iframe src="<%=pdfpath%>" title="Purchase Remittance PDF" allowfullscreen width="100%"></iframe>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<%} %>
</body>
</html>