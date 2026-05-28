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
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="dlng_inv" scope="request"></jsp:useBean>
<%
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
String flag=request.getParameter("flag")==null?"":request.getParameter("flag");

dlng_inv.setCallFlag("DLNG_VIEW_ALL_PDF");
dlng_inv.setComp_cd(owner_cd);
dlng_inv.setFinancial_year(financial_year);
dlng_inv.setBu_state_tin(bu_state_tin);
dlng_inv.setInvoice_seq(invoice_seq);
dlng_inv.setInvoice_type(invoice_type);
dlng_inv.setFlag(flag);
dlng_inv.init();

Vector VPDF_FILE_NAME = dlng_inv.getVPDF_FILE_NAME();
Vector VPDF_FILE_PATH = dlng_inv.getVPDF_FILE_PATH();

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