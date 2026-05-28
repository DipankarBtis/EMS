<%@page import="com.etrm.fms.util.UtilBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<%
String report_dt = request.getParameter("report_dt")==null?"":request.getParameter("report_dt");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String bu_plant_seq = request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
String to_contact = request.getParameter("to_contact")==null?"":request.getParameter("to_contact");
String from_contact = request.getParameter("from_contact")==null?"":request.getParameter("from_contact");
String rmk = request.getParameter("rmk")==null?"":request.getParameter("rmk");
String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
String frq_type=request.getParameter("frq_type")==null?"":request.getParameter("frq_type");

UtilBean utilBean = new UtilBean();
String counterparty_nm = utilBean.getCounterpartyName(counterparty_cd);

HttpServletRequest req = request;

pdfFile.setRequest(req);
pdfFile.setComp_cd(owner_cd);
pdfFile.setCounterparty_cd(counterparty_cd);
pdfFile.setCont_no(cont_no);
pdfFile.setAgmt_no(agmt_no);
pdfFile.setPlant_seq(plant_seq);
pdfFile.setBu_plant_seq(bu_plant_seq);
pdfFile.setContract_type(contract_type);
pdfFile.setReport_dt(report_dt);
pdfFile.setTo_contact(to_contact);
pdfFile.setFrom_contact(from_contact);
pdfFile.setRemark(rmk);
pdfFile.setFrq_type(frq_type);
pdfFile.setCallFlag("BUYER_NOM_TO_TRADER");
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/pdf_reports/purchase/daily_nomination/pdf_files//"+file_nm;
System.out.println(pdfpath);
%>
<body bgcolor="white">
<iframe src="<%=pdfpath%>" height="800px" width="100%">
</iframe>
</body>
</html>