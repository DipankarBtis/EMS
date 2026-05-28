<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refreshParent()
{
	window.opener.refresh();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GTA_Remittance" id="remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");

String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String trans_bu_seq = request.getParameter("trans_bu_seq")==null?"":request.getParameter("trans_bu_seq");
String bu_plant_seq = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String period_start_dt=request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
String inv_type = request.getParameter("inv_type")==null?"":request.getParameter("inv_type");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");

remittance.setCallFlag("GTA_FINAL_PRINT_INVOICE_PDF");
remittance.setComp_cd(owner_cd);
remittance.setCounterparty_cd(counterparty_cd);
remittance.setAgmt_no(agmt_no);
remittance.setAgmt_rev_no(agmt_rev);
remittance.setCont_no(cont_no);
remittance.setCont_rev_no(cont_rev);
remittance.setContract_type(contract_type);
remittance.setTrans_bu_seq(trans_bu_seq);
remittance.setBilling_cycle(billing_cycle);
remittance.setPeriod_start_dt(period_start_dt);
remittance.setPeriod_end_dt(period_end_dt);
remittance.setBu_unit(bu_unit);
remittance.setInv_type(inv_type);
remittance.setFinancial_year(financial_year);
remittance.setInvoice_seq(invoice_seq);
remittance.setInvoice_type(invoice_type);
remittance.init();

HashMap<String, String> invoice_data = remittance.getInvoice_data();

HttpServletRequest req = request;
pdfFile.setRequest(req);
pdfFile.setInv_type(inv_type);
pdfFile.setComp_cd(owner_cd);
pdfFile.setCounterparty_cd(counterparty_cd);
pdfFile.setCont_no(cont_no);
pdfFile.setAgmt_no(agmt_no);
pdfFile.setTrans_bu_seq(trans_bu_seq);
pdfFile.setBu_plant_seq(bu_plant_seq);
pdfFile.setContract_type(contract_type);
pdfFile.setPeriod_start_dt(period_start_dt);
pdfFile.setPeriod_end_dt(period_end_dt);
pdfFile.setInvoice_data(invoice_data);
pdfFile.setFinancial_year(financial_year);
pdfFile.setInvoice_type(invoice_type);
pdfFile.setInv_seq(invoice_seq);
pdfFile.setComp_logo(owner_logo);
pdfFile.setEmp_cd(emp_cd);
pdfFile.setForm_id(formCd);
pdfFile.setForm_nm(formNm);
pdfFile.setMod_cd(mod_cd);
pdfFile.setMod_nm(mod_nm);
pdfFile.setCallFlag("GTA_INVOICE_REMITTANCE");
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String sub_folder2="invoice";
if(invoice_type.equals("TC"))
{
	sub_folder2="transmission_invoice";
}
else if(invoice_type.equals("IC"))
{
	sub_folder2="imbalance_invoice";
}
else if(invoice_type.equals("PC"))
{
	sub_folder2="parking_invoice";
}
String pdfpath = file_url+"/work_data"+owner_cd+"/gta/"+sub_folder2+"//"+file_nm;

String msg1=pdfFile.getMsg();
String msg_type1=pdfFile.getMsg_type();

%>
<body <%if(!file_nm.equals("")){%>onload="refreshParent();"<%}%>>
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
					    	GTA Remittance PDF
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
</body>
</html>