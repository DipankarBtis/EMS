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
		alert("PDF has been Generated!")
		window.close();
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.sales_invoice.DataBean_Sales_Invoice" id="sales_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<%
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String mapping_id=request.getParameter("mapping_id")==null?"0":request.getParameter("mapping_id");
String address_type=request.getParameter("address_type")==null?"0":request.getParameter("address_type");
String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String invoice_type=request.getParameter("invoice_type")==null?"0":request.getParameter("invoice_type");
String inv_no=request.getParameter("inv_no")==null?"":request.getParameter("inv_no");
String inv_seq=request.getParameter("inv_seq")==null?"":request.getParameter("inv_seq");
String financial=request.getParameter("financial")==null?"":request.getParameter("financial");

String is_print=request.getParameter("is_print")==null?"0":request.getParameter("is_print");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
String ff_print_pdf_type=request.getParameter("ff_print_pdf_type")==null?"":request.getParameter("ff_print_pdf_type");

String temp_print_pdf_type=ff_print_pdf_type;
String[] pdf_type=temp_print_pdf_type.split(",");

String file_nm="";
String pdfpath="";
for(int i=0; i<pdf_type.length; i++)
{
	ff_print_pdf_type=pdf_type[i];

	HttpServletRequest req = request;
	pdfFile.setRequest(req);
	pdfFile.setInv_type(invoice_type);
	pdfFile.setInv_seq(inv_seq);
	pdfFile.setInv_no(inv_no);
	pdfFile.setFinancial_year(financial);
	pdfFile.setMapping_id(mapping_id);
	pdfFile.setComp_cd(owner_cd);
	pdfFile.setCounterparty_cd(counterparty_cd);
	pdfFile.setIs_print(is_print);
	pdfFile.setBu_state_tin(bu_state_tin);
	pdfFile.setPrint_pdf_type(ff_print_pdf_type);
	pdfFile.setComp_logo(owner_logo);
	pdfFile.setEmp_cd(emp_cd);
	pdfFile.setForm_id(formCd);
	pdfFile.setForm_nm(formNm);
	pdfFile.setMod_cd(mod_cd);
	pdfFile.setMod_nm(mod_nm);
	pdfFile.setCallFlag("SALES_F_FLOW_INVOICE");
	pdfFile.init();
	
	String file_url = pdfFile.getFile_url();
	file_nm = pdfFile.getFile_nm();
	pdfpath = file_url+"/"+CommonVariable.work_dir+owner_cd+""+CommonVariable.freeflow_inv_path+""+file_nm;
	//System.out.println(pdfpath);
}	

String msg1=pdfFile.getMsg();
String msg_type1=pdfFile.getMsg_type();
%>
<body <%if(!file_nm.equals("") && is_print.equals("1")){%>onload="refreshParent('<%=pdf_type.length%>','<%=accroid%>');"<%}%>>
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
					    	Sell Invoice PDF
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