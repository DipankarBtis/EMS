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
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<%
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");

String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String seq_no=request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
String seq_rev_no=request.getParameter("seq_rev_no")==null?"":request.getParameter("seq_rev_no");
String gx=request.getParameter("gx")==null?"":request.getParameter("gx");
String isReversal=request.getParameter("isReversal")==null?"":request.getParameter("isReversal");
String invoice_no=request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");

HttpServletRequest req = request;
pdfFile.setRequest(req);
pdfFile.setComp_cd(owner_cd);
pdfFile.setCounterparty_cd(counterparty_cd);
pdfFile.setComp_logo(owner_logo);
pdfFile.setSeq_no(seq_no);
pdfFile.setSeq_rev_no(seq_rev_no);
pdfFile.setGx(gx);
pdfFile.setInv_no(invoice_no);
pdfFile.setIsReversal(isReversal);
pdfFile.setEmp_cd(emp_cd);
pdfFile.setForm_id(formCd);
pdfFile.setForm_nm(formNm);
pdfFile.setMod_cd(mod_cd);
pdfFile.setMod_nm(mod_nm);
pdfFile.setCallFlag("SEC_ADVANCE_REMITTANCE");
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/work_data"+owner_cd+""+CommonVariable.security_adv_path+""+file_nm;

String msg1=pdfFile.getMsg();
String msg_type1=pdfFile.getMsg_type();
%>
<body <%if(!file_nm.equals("")){%>onload="refreshParent('<%=gx%>');"<%}%>>
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
					    	Invoice Remittance PDF
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