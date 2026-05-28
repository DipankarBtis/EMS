<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<%@page import="com.etrm.fms.util.CommonVariable"%>
<script>
function refreshParent(report_dt,url_main)
{
	window.opener.refreshParent(report_dt);
	refresh(url_main);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<%
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String report_dt=request.getParameter("report_dt")==null?"":request.getParameter("report_dt");
String plant_seq=request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String to_contact=request.getParameter("to_contact")==null?"":request.getParameter("to_contact");
String from_contact=request.getParameter("from_contact")==null?"":request.getParameter("from_contact");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String bu_plant_seq=request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
String rmk=request.getParameter("rmk")==null?"":request.getParameter("rmk");
String frq_type=request.getParameter("frq_type")==null?"":request.getParameter("frq_type");
String from_date = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
String to_date = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
String month_nm = request.getParameter("month_nm")==null?"":request.getParameter("month_nm");
String url_main = request.getParameter("url_main")==null?"":request.getParameter("url_main");

HttpServletRequest req = request;
pdfFile.setRequest(req);
pdfFile.setComp_cd(owner_cd);
pdfFile.setCounterparty_cd(counterparty_cd);
pdfFile.setComp_logo(owner_logo);
pdfFile.setEmp_cd(emp_cd);
pdfFile.setReport_dt(report_dt);
pdfFile.setTo_contact(to_contact);
pdfFile.setFrom_contact(from_contact);
pdfFile.setReport_dt(report_dt);
pdfFile.setPlant_seq(plant_seq);
pdfFile.setCont_no(cont_no);
pdfFile.setAgmt_no(agmt_no);
pdfFile.setContract_type(contract_type);
pdfFile.setBu_plant_seq(bu_plant_seq);
pdfFile.setFrq_type(frq_type);
pdfFile.setFrom_date(from_date);
pdfFile.setTo_date(to_date);
//pdfFile.setMonth_nm(month_nm);
pdfFile.setCallFlag("BUYER_NOM_TO_TRADER");
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/"+CommonVariable.work_dir+owner_cd+"/purchase/buyer_nom_to_trader//"+file_nm;



%>
<body <%if(!file_nm.equals("")){%>onload="refreshParent('<%=report_dt%>','<%=url_main%>');"<%}%>>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Buyer Nomination To Trader PDF
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