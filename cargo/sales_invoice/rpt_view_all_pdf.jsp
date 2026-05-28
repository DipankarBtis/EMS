<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function openPdfFile(url)
{
	window.opener.openPdfFile(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.sales_invoice.DataBean_Sales_Invoice" id="sales_inv" scope="request"></jsp:useBean>
<%
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
String flag=request.getParameter("flag")==null?"":request.getParameter("flag");

sales_inv.setCallFlag("VIEW_ALL_PDF");
sales_inv.setComp_cd(owner_cd);
sales_inv.setFinancial_year(financial_year);
sales_inv.setBu_state_tin(bu_state_tin);
sales_inv.setInvoice_seq(invoice_seq);
sales_inv.setInvoice_type(invoice_type);
sales_inv.setFlag(flag);
sales_inv.init();

Vector VPDF_FILE_NAME = sales_inv.getVPDF_FILE_NAME();
Vector VPDF_FILE_PATH = sales_inv.getVPDF_FILE_PATH();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;
%>
<%if(VPDF_FILE_NAME.size()>0){ %>
<%for(int i=0;i<VPDF_FILE_NAME.size();i++){ 
String url=file_url+VPDF_FILE_PATH.elementAt(i)+VPDF_FILE_NAME.elementAt(i);
%>
<script>
openPdfFile('<%=url%>');
</script>
<%} %>
<script>
window.close();
</script>
<%}else{%>
<script>
window.close();
</script>
<%} %>
</html>