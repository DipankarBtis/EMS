<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refreshParent(accroid)
{
	window.opener.refresh(accroid);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DB_Pdf_Generation1" id="pdfFile" scope="request"/>
<%
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");

String operation=request.getParameter("operation")==null?"INSERT":request.getParameter("operation");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String sel_inv_no = request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
String invoice_type = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String crdr_gen_type = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
String sgpg_type = request.getParameter("sgpg_type")==null?"":request.getParameter("sgpg_type");
String crdr_no = request.getParameter("crdr_no")==null?"":request.getParameter("crdr_no");
String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

HttpServletRequest req = request;
pdfFile.setRequest(req);
pdfFile.setComp_cd(owner_cd);
pdfFile.setCounterparty_cd(counterparty_cd);
pdfFile.setContract_type(contract_type);
pdfFile.setSel_inv_no(sel_inv_no);
pdfFile.setSgpg_type(sgpg_type);
pdfFile.setFinancial_year(financial_year);
pdfFile.setInv_seq(invoice_seq);
pdfFile.setInvoice_type(crdr_gen_type);
pdfFile.setInv_flag(crdr_gen_type);
pdfFile.setCrdr_inv_no(crdr_no);
pdfFile.setInvoice_dt(invoice_dt);
pdfFile.setComp_logo(owner_logo);
pdfFile.setEmp_cd(emp_cd);
pdfFile.setForm_id(formCd);
pdfFile.setForm_nm(formNm);
pdfFile.setMod_cd(mod_cd);
pdfFile.setMod_nm(mod_nm);
pdfFile.setCallFlag("PDF_CRDR_INVOICE_REMITTANCE");
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/work_data"+owner_cd+"/purchase/invoice//"+file_nm;

String msg1=pdfFile.getMsg();
String msg_type1=pdfFile.getMsg_type();
%>
<body <%if(!file_nm.equals("")){%>onload="refreshParent('<%=accroid%>');"<%}%>>
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
					    	Purchase CRDR PDF
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="ratio ratio-1x1">
							  <iframe src="<%=pdfpath%>" title="Purchase CrDr PDF" allowfullscreen width="100%"></iframe>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>