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
<jsp:useBean class="com.etrm.fms.gx.DataBean_Gx_Invoice" id="gx_remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");

String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String gx_counterparty_cd = request.getParameter("gx_counterparty_cd")==null?"":request.getParameter("gx_counterparty_cd");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String gx_bu_plant_seq = request.getParameter("gx_bu_unit")==null?"":request.getParameter("gx_bu_unit");
String bu_plant_seq = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String inv_type = request.getParameter("inv_type")==null?"":request.getParameter("inv_type");
String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");

gx_remittance.setCallFlag("FINAL_PRINT_INVOICE_PDF");
gx_remittance.setComp_cd(owner_cd);
gx_remittance.setCounterparty_cd(counterparty_cd);
gx_remittance.setGx_counterparty_cd(gx_counterparty_cd);
gx_remittance.setAgmt_no(agmt_no);
gx_remittance.setAgmt_rev_no(agmt_rev);
gx_remittance.setCont_no(cont_no);
gx_remittance.setCont_rev_no(cont_rev);
gx_remittance.setContract_type(contract_type);
gx_remittance.setGx_bu_unit(gx_bu_plant_seq);
gx_remittance.setBu_unit(bu_unit);
gx_remittance.setInv_type(inv_type);
gx_remittance.setFinancial_year(financial_year);
gx_remittance.setInvoice_seq(invoice_seq);
gx_remittance.setInvoice_type(invoice_type);
gx_remittance.init();

HashMap<String, String> invoice_data = gx_remittance.getInvoice_data();

HttpServletRequest req = request;
pdfFile.setRequest(req);
pdfFile.setInv_type(inv_type);
pdfFile.setComp_cd(owner_cd);
pdfFile.setCounterparty_cd(counterparty_cd);
pdfFile.setGx_counterparty_cd(gx_counterparty_cd);
pdfFile.setCont_no(cont_no);
pdfFile.setAgmt_no(agmt_no);
pdfFile.setGx_bu_plant_seq(gx_bu_plant_seq);
pdfFile.setBu_plant_seq(bu_plant_seq);
pdfFile.setContract_type(contract_type);
pdfFile.setInvoice_data(invoice_data);
pdfFile.setFinancial_year(financial_year);
pdfFile.setInvoice_type(invoice_type);
pdfFile.setComp_logo(owner_logo);
pdfFile.setInv_seq(invoice_seq);
pdfFile.setEmp_cd(emp_cd);
pdfFile.setForm_id(formCd);
pdfFile.setForm_nm(formNm);
pdfFile.setMod_cd(mod_cd);
pdfFile.setMod_nm(mod_nm);
pdfFile.setCallFlag("GX_INVOICE_REMITTANCE");
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/work_data"+owner_cd+"/gx/invoice//"+file_nm;

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
					    	GX Remittance PDF
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