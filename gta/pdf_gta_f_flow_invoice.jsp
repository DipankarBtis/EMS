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
<jsp:useBean class="com.etrm.fms.gta.DataBean_GTA_Remittance" id="remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String trans_counterparty_cd = request.getParameter("trans_counterparty_cd")==null?"0":request.getParameter("trans_counterparty_cd");
//String trans_bu_unit=request.getParameter("trans_bu_unit")==null?"0":request.getParameter("trans_bu_unit");

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
//pdfFile.setTrans_counterparty_cd(trans_counterparty_cd);
pdfFile.setIs_print(is_print);
pdfFile.setComp_logo(owner_logo);
pdfFile.setCallFlag("GTA_F_FLOW_INVOICE");
pdfFile.setEmp_cd(emp_cd);
pdfFile.setForm_id(formCd);
pdfFile.setForm_nm(formNm);
pdfFile.setMod_cd(mod_cd);
pdfFile.setMod_nm(mod_nm);
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/"+CommonVariable.work_dir+owner_cd+"/gta/f_flow_invoice//"+file_nm;

String msg1=pdfFile.getMsg();
String msg_type1=pdfFile.getMsg_type();
%>
<body <%if(!file_nm.equals("") && is_print.equals("1")){%>onload="refreshParent();"<%}%>>
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
					    	GTA Remittance View
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