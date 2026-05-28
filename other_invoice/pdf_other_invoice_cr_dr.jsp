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
<jsp:useBean class="com.etrm.fms.other_invoice.DataBean_Oth_Inv_CR_DR_pdf_Gen" id="other_inv" scope="request"></jsp:useBean>
<%
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");

String accrod =request.getParameter("accrod")==null?"":request.getParameter("accrod");
String inv_no = request.getParameter("inv_no")==null?"":request.getParameter("inv_no");
String inv_type=request.getParameter("inv_type")==null?"":request.getParameter("inv_type");
String print_pdf_type=request.getParameter("pdf_type")==null?"O":request.getParameter("pdf_type");
String sel_inv_no=request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
String cr_dr_type=request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");
String inv_seq = request.getParameter("inv_seq")==null?"":request.getParameter("inv_seq");
String finYr = request.getParameter("fin_year")==null?"":request.getParameter("fin_year");
String supp_cd=request.getParameter("supp_cd")==null?"":request.getParameter("supp_cd");
String bu_seq=request.getParameter("bu_seq")==null?"":request.getParameter("bu_seq");
String is_print=request.getParameter("is_print")==null?"0":request.getParameter("is_print");

String temp_print_pdf_type=print_pdf_type;
String[] pdf_type=temp_print_pdf_type.split(",");

String file_nm="";
String pdfpath="";
for(int i=0; i<pdf_type.length; i++)
{
	print_pdf_type=pdf_type[i];
/* 	System.out.println("Pdf::"+print_pdf_type); */
	
	HttpServletRequest req = request;
	other_inv.setRequest(req);
	other_inv.setComp_cd(owner_cd);
	other_inv.setComp_logo(owner_logo);
	other_inv.setEmp_cd(emp_cd);
	other_inv.setForm_id(formCd);
	other_inv.setForm_nm(formNm);
	other_inv.setMod_cd(mod_cd);
	other_inv.setMod_nm(mod_nm);
	other_inv.setInv_type(inv_type);
	other_inv.setInv_No(inv_no);
	other_inv.setPrint_pdf_type(print_pdf_type);
	other_inv.setCr_dr_type(cr_dr_type);
	other_inv.setSel_inv_no(sel_inv_no);
	other_inv.setInv_seq(inv_seq);
	other_inv.setFin_yr(finYr);
	other_inv.setSupplier_Cd(supp_cd);
	other_inv.setBu_unit(bu_seq);
	other_inv.setIs_print(is_print);
	other_inv.setCallFlag("PDF_OTHER_INVOICE_CR_DR");
	other_inv.init();
	
	String file_url = other_inv.getFile_url();
	file_nm = other_inv.getFile_nm();
	pdfpath = file_url+"/work_data"+owner_cd+""+CommonVariable.ig_inv_path+""+file_nm;
}

String msg1=other_inv.getMsg();
String msg_type1=other_inv.getMsg_type();
%>
<body <%if(!file_nm.equals("") && is_print.equals("1")){%>onload="refreshParent('<%=pdf_type.length%>','<%=accrod%>');"<%}%>>
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
					    	Other Invoice PDF
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="ratio ratio-1x1">
							  <iframe src="<%=pdfpath%>" title="Other Invoice PDF" allowfullscreen width="100%"></iframe>
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