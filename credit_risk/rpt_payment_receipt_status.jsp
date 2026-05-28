
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh(clearance)
{
	var rpt_dt = document.forms[0].rpt_dt.value;

	var u = document.forms[0].u.value;
	
	var url = "rpt_payment_receipt_status.jsp?u="+u+"&rpt_dt="+rpt_dt+"&clearance="+clearance;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function exportToXls()
{
	var rpt_dt = document.forms[0].rpt_dt.value;
	
	var url = "xls_payment_receipt_status.jsp?fileName=Payment Receipt Status Report "+rpt_dt+".xls&rpt_dt="+rpt_dt;

	location.replace(url);
}
</script>
</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CreditRisk_Report" id="dbcredit" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String previousDate=utildate.getPreviousDate();
String rpt_dt = request.getParameter("rpt_dt")==null?previousDate:request.getParameter("rpt_dt");

dbcredit.setCallFlag("PAYMENT_RECEIPT_STATUS");
dbcredit.setRpt_dt(rpt_dt);
dbcredit.setComp_cd(owner_cd);
dbcredit.init();

Vector VCOUNTERPARTY_NAME = dbcredit.getVCOUNTERPARTY_NAME();
Vector VCOUNTERPARTY_ABBR = dbcredit.getVCOUNTERPARTY_ABBR();
Vector VINVOICE_NO = dbcredit.getVINVOICE_NO();
Vector VDEAL_NO = dbcredit.getVDEAL_NO();
Vector VNET_DUE_DT = dbcredit.getVNET_DUE_DT();
Vector VRECV_AMT = dbcredit.getVRECV_AMT();
Vector VAMT_DC = dbcredit.getVAMT_DC();
Vector VDUE_AMT = dbcredit.getVDUE_AMT();
Vector VCO_CODE = dbcredit.getVCO_CODE();
Vector VCO_ABBR = dbcredit.getVCO_ABBR();

Vector VSEC_TYPE = dbcredit.getVSEC_TYPE();
Vector VSEC_REF_NO = dbcredit.getVSEC_REF_NO();
Vector VDEAL_REF_NO = dbcredit.getVDEAL_REF_NO();
Vector VPAY_RECV_DT = dbcredit.getVPAY_RECV_DT();
Vector VPAYMENT_STATUS = dbcredit.getVPAYMENT_STATUS();
Vector VEXPIRE_DT = dbcredit.getVEXPIRE_DT();
Vector VSTATUS = dbcredit.getVSTATUS();
Vector VSTATUS_NM = dbcredit.getVSTATUS_NM();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="d-flex justify-content-between">
						<div class="topheader">
				    		Payment Receipt Status Report
	   	 				</div>
	   	 				<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div align="center">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Report Date:</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="rpt_dt" value="<%=rpt_dt%>" disabled="disabled" size="8" maxLength="10" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						<input type="hidden" name="report_dt" >
		      						</div>
		      						<script>
										document.forms[0].report_dt.value="<%=rpt_dt%>"
									</script>
				    			</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table id="search_by_filter" class="table table-bordered" id="filterbysearch">
							<thead>
								<tr>
									<th align="center">Sr#</th>
									<th align="center">Entity</th>
									<th align="center">Customer Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="counterparty_nm" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Security Type</th>
									<th align="center">Security Ref#</th>
									<th align="center">Deal Ref#</th>
									<th align="center">Invoice No.</th>
									<th align="center">Invoice Amount(INR)</th>
									<th align="center">Invoice Due Date</th>
									<th align="center">Received Amount(INR)</th>
									<th align="center">Payment Received Date<br><div align="center"><input class="form-control form-control-sm" type="text" id="ref_id" onkeyup="Search(this,'10');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Payment Status</th>
									<th align="center">Security Expiry Date</th>
									<th align="center">Security Status</th>
								</tr>
							</thead>
							<tbody>
								<%if(VCOUNTERPARTY_NAME.size()>0){
									for(int i=0;i<VCOUNTERPARTY_NAME.size();i++){%>
									<tr>
										<td align="center"><%=i+1%></td>
										<td align="center"><%=VCO_ABBR.elementAt(i)%></td>
										<td align="center"><%=VCOUNTERPARTY_NAME.elementAt(i)%></td>
										<td align="center"><b>
											<span 
											<%if(VSEC_TYPE.elementAt(i).equals("LC")){ %>
	    										class="alert alert-info"
	    									<%}else if(VSEC_TYPE.elementAt(i).equals("BG")){ %>
	    										class="alert alert-warning"
	    									<%}else if(VSEC_TYPE.elementAt(i).equals("PCG")){ %>
	    										class="alert alert-primary"
	    									<%}else if(VSEC_TYPE.elementAt(i).equals("ADV")){ %>
	    										class="alert alert-dark"
    										<%}else if(VSEC_TYPE.elementAt(i).equals("OA")){ %>
	    										class="alert alert-danger"
	    									<%}%>><%=VSEC_TYPE.elementAt(i) %></span></b>
	    								</td>
										<td align="center"><%=VSEC_REF_NO.elementAt(i)%></td>
										<td align="center"><%=VDEAL_REF_NO.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
										<td align="right"><%=VAMT_DC.elementAt(i)%></td>
										<td align="center"><%=VNET_DUE_DT.elementAt(i)%></td>
										<td align="right"><%=VRECV_AMT.elementAt(i)%></td>
										<td align="center"><%=VPAY_RECV_DT.elementAt(i)%></td>
										<td <%if(VPAYMENT_STATUS.elementAt(i).equals("Not Received")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>align="center"><b><%=VPAYMENT_STATUS.elementAt(i)%></b></td>
										<td align="center"><%=VEXPIRE_DT.elementAt(i)%></td>
										<td align="center">
											<%=VSTATUS_NM.elementAt(i)%>
										</td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan=14 align="center"><%=utilmsg.infoMessage("<b>No Payment Receipt Status Data is Available!</b>") %></td>
									</tr>
								<%} %>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="read_access" value="<%=read_access%>">
<input type="hidden" name="write_access" value="<%=write_access%>">
<input type="hidden" name="check_access" value="<%=check_access%>">
<input type="hidden" name="print_access" value="<%=print_access%>">
<input type="hidden" name="delete_access" value="<%=delete_access%>">  	
<input type="hidden" name="audit_access" value="<%=audit_access%>">
<input type="hidden" name="authorize_access" value="<%=authorize_access%>">
<input type="hidden" name="approve_access" value="<%=approve_access%>">
<input type="hidden" name="execute_access" value="<%=execute_access%>">
<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<input type="hidden" name="u" value="<%=u%>">

</form>
<script>
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("search_by_filter");
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	if (td) 
    	{
      		txtValue = td.textContent || td.innerText;
      		if (txtValue.toLocaleLowerCase().indexOf(filter) > -1) {
        		tr[i].style.display = "";
        		count++;
      		} else {
      			tr[i].style.display = "none";
      		}
    	}       
  	}
}
</script>
</body>
</html>