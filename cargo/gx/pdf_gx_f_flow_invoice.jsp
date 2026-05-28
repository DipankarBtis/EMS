<%@page import="com.etrm.fms.util.CommonVariable"%>
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
<jsp:useBean class="com.etrm.fms.gx.DataBean_Gx_Invoice" id="remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<%
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String gx_counterparty_cd = request.getParameter("gx_counterparty_cd")==null?"0":request.getParameter("gx_counterparty_cd");
String gx_bu_unit=request.getParameter("gx_bu_unit")==null?"0":request.getParameter("gx_bu_unit");

//String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String mapping_id=request.getParameter("mapping_id")==null?"0":request.getParameter("mapping_id");
//String address_type=request.getParameter("address_type")==null?"0":request.getParameter("address_type");
String invoice_type=request.getParameter("invoice_type")==null?"0":request.getParameter("invoice_type");
String inv_no=request.getParameter("inv_no")==null?"":request.getParameter("inv_no");
String inv_seq=request.getParameter("inv_seq")==null?"":request.getParameter("inv_seq");
String financial=request.getParameter("financial")==null?"":request.getParameter("financial");

String is_print=request.getParameter("is_print")==null?"0":request.getParameter("is_print");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");

HttpServletRequest req = request;
pdfFile.setRequest(req);
pdfFile.setInv_type(invoice_type);
pdfFile.setInv_seq(inv_seq);
pdfFile.setInv_no(inv_no);
pdfFile.setFinancial_year(financial);
pdfFile.setMapping_id(mapping_id);
pdfFile.setComp_cd(owner_cd);
pdfFile.setCounterparty_cd(counterparty_cd);
pdfFile.setGx_counterparty_cd(gx_counterparty_cd);
pdfFile.setIs_print(is_print);
pdfFile.setComp_logo(owner_logo);
pdfFile.setCallFlag("GX_F_FLOW_INVOICE");
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/"+CommonVariable.work_dir+owner_cd+"/gx/f_flow_invoice//"+file_nm;


if(is_print.equals("1"))
{
	remittance.setCallFlag("GX_FINAL_PRINT_FFLOW_INVOICE_PDF");
	remittance.setComp_cd(owner_cd);
	remittance.setCounterparty_cd(counterparty_cd);
	remittance.setInvoice_type(invoice_type);
	remittance.setFinancial_year(financial);
	remittance.setInvoice_seq(inv_seq);
	remittance.setContract_type(contract_type);
	remittance.setFile_nm(file_nm);
	remittance.setEmp_cd(emp_cd);
	remittance.setGx_counterparty_cd(gx_counterparty_cd);
	remittance.init();
}

%>
<body <%if(!file_nm.equals("") && is_print.equals("1")){%>onload="refreshParent();"<%}%>>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Gas Exchange Remittance View
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