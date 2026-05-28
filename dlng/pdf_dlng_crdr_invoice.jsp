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

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

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
String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
String inv_flag=request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
String mapping_id=request.getParameter("mapping_id")==null?"":request.getParameter("mapping_id");
String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"":request.getParameter("truck_trans_cd");
String truck_cd=request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");

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
	pdfFile.setCargo_no(cargo_no);
	pdfFile.setPlant_seq(plant_seq);
	pdfFile.setBu_plant_seq(bu_unit);
	pdfFile.setContract_type(contract_type);
	pdfFile.setPeriod_start_dt(period_start_dt);
	pdfFile.setPeriod_end_dt(period_end_dt);
	pdfFile.setFinancial_year(financial_year);
	pdfFile.setPrint_pdf_type(print_pdf_type);
	pdfFile.setInv_seq(invoice_seq);
	pdfFile.setBu_state_tin(bu_state_tin);
	pdfFile.setBilling_cycle(billing_cycle);
	pdfFile.setComp_logo(owner_logo);
	pdfFile.setEmp_cd(emp_cd);
	pdfFile.setForm_id(formCd);
	pdfFile.setForm_nm(formNm);
	pdfFile.setMod_cd(mod_cd);
	pdfFile.setMod_nm(mod_nm);
	pdfFile.setInv_flag(inv_flag);
	pdfFile.setMapping_id(mapping_id);
	pdfFile.setTruck_cd(truck_cd);
	pdfFile.setTruck_trans_cd(truck_trans_cd);
	pdfFile.setCallFlag("PDF_DLNG_CRDR_INVOICE");
	pdfFile.init();
	
	String file_url = pdfFile.getFile_url();
	file_nm = pdfFile.getFile_nm();
	pdfpath = file_url+"/work_data"+owner_cd+""+CommonVariable.dlng_sales_inv_path+""+file_nm;
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
					    	DLNG Sales Credit/Debit Note PDF
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