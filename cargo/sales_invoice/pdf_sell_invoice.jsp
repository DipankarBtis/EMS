<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refreshParent(pdfLen)
{
	window.opener.refresh();
	if(parseInt(pdfLen)>1)
	{
		alert("PDF has been Generated!")
		window.close();
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.sales_invoice.DataBean_Sales_Invoice" id="sales_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<%
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");

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
String print_pdf_type=request.getParameter("print_pdf_type")==null?"":request.getParameter("print_pdf_type");

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
	pdfFile.setCont_no(cont_no);
	pdfFile.setAgmt_no(agmt_no);
	pdfFile.setPlant_seq(plant_seq);
	pdfFile.setBu_plant_seq(bu_unit);
	pdfFile.setContract_type(contract_type);
	pdfFile.setPeriod_start_dt(period_start_dt);
	pdfFile.setPeriod_end_dt(period_end_dt);
	pdfFile.setFinancial_year(financial_year);
	pdfFile.setPrint_pdf_type(print_pdf_type);
	pdfFile.setInv_seq(invoice_seq);
	pdfFile.setBu_state_tin(bu_state_tin);
	pdfFile.setComp_logo(owner_logo);
	pdfFile.setCallFlag("PDF_SELL_INVOICE");
	pdfFile.init();
	
	String file_url = pdfFile.getFile_url();
	file_nm = pdfFile.getFile_nm();
	pdfpath = file_url+"/work_data"+owner_cd+""+CommonVariable.sales_inv_path+""+file_nm;
	
	sales_inv.setCallFlag("UPDATE_PRINT_INVOICE_FLAG");
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
	sales_inv.setEmp_cd(emp_cd);
	sales_inv.setFile_nm(file_nm);
	sales_inv.setPrint_pdf_type(print_pdf_type);
	sales_inv.setInvoice_seq(invoice_seq);
	sales_inv.init();
}
%>
<body <%if(!file_nm.equals("")){%>onload="refreshParent('<%=pdf_type.length%>');"<%}%>>
<%if(pdf_type.length==1){ %>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Sell Invoice PDF
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<iframe src="<%=pdfpath%>" width="100%" height="600px">
							</iframe>
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