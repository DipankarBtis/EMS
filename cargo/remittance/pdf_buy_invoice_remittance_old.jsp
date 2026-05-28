<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.remittance.DataBean_Remittance" id="remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String bu_plant_seq = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String period_start_dt=request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
String inv_type = request.getParameter("inv_type")==null?"":request.getParameter("inv_type");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");

remittance.setCallFlag("SELLER_PAYMENT_DETAIL");
remittance.setComp_cd(owner_cd);
remittance.setCounterparty_cd(counterparty_cd);
remittance.setAgmt_no(agmt_no);
remittance.setAgmt_rev_no(agmt_rev);
remittance.setCont_no(cont_no);
remittance.setCont_rev_no(cont_rev);
remittance.setContract_type(contract_type);
remittance.setPlant_seq(plant_seq);
remittance.setBilling_cycle(billing_cycle);
remittance.setPeriod_start_dt(period_start_dt);
remittance.setPeriod_end_dt(period_end_dt);
remittance.setBu_unit(bu_unit);
remittance.setInv_type(inv_type);
remittance.init();

HashMap<String, String> invoice_data = remittance.getInvoice_data();

HttpServletRequest req = request;
pdfFile.setRequest(req);
pdfFile.setInv_type(inv_type);
pdfFile.setComp_cd(owner_cd);
pdfFile.setCounterparty_cd(counterparty_cd);
pdfFile.setCont_no(cont_no);
pdfFile.setAgmt_no(agmt_no);
pdfFile.setPlant_seq(plant_seq);
pdfFile.setBu_plant_seq(bu_plant_seq);
pdfFile.setContract_type(contract_type);
pdfFile.setPeriod_start_dt(period_start_dt);
pdfFile.setPeriod_end_dt(period_end_dt);
pdfFile.setInvoice_data(invoice_data);
pdfFile.setFinancial_year(financial_year);
pdfFile.setCallFlag("INVOICE_REMITTANCE");
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/work_data"+owner_cd+"/purchase/invoice//"+file_nm;
System.out.println(pdfpath);
%>
<body>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Invoice Remittance PDF
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
</body>
</html>