<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refreshParent(gx)
{
	window.opener.refreshParent(gx);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<%
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String seq_no=request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
String seq_rev_no=request.getParameter("seq_rev_no")==null?"":request.getParameter("seq_rev_no");
String gx=request.getParameter("gx")==null?"":request.getParameter("gx");
String isReversal=request.getParameter("isReversal")==null?"":request.getParameter("isReversal");

HttpServletRequest req = request;
pdfFile.setRequest(req);
pdfFile.setComp_cd(owner_cd);
pdfFile.setCounterparty_cd(counterparty_cd);
pdfFile.setComp_logo(owner_logo);
pdfFile.setSeq_no(seq_no);
pdfFile.setSeq_rev_no(seq_rev_no);
pdfFile.setGx(gx);
pdfFile.setIsReversal(isReversal);
pdfFile.setEmp_cd(emp_cd);
pdfFile.setCallFlag("SEC_ADVANCE_REMITTANCE");
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/work_data"+owner_cd+"/security/adv//"+file_nm;


%>
<body <%if(!file_nm.equals("")){%>onload="refreshParent('<%=gx%>');"<%}%>>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Invoice Remittance PDF
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