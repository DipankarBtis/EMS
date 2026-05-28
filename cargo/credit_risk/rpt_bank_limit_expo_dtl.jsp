<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh()
{
	var report_dt = document.forms[0].report_dt.value;
	var u = document.forms[0].u.value;
	
	var url = "rpt_bank_limit_expo_report.jsp?u="+u+"&report_dt="+report_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function showBankLimit(VMST_BANK_NM,VBANK_LIMIT,VBANK_LIMIT_USD)
{
	document.getElementById('bank_name').innerHTML = VMST_BANK_NM;
	document.getElementById('bank_limit_inr').innerHTML=VBANK_LIMIT;
	document.getElementById('bank_limit_usd').innerHTML=VBANK_LIMIT_USD;
	
	document.getElementById('bank_nm').value = VMST_BANK_NM;
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CR_ReceivableReport" id="dbcredit" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String report_dt = request.getParameter("report_dt")==null?sysdate:request.getParameter("report_dt");
String bank_nm = request.getParameter("bank_nm")==null?"":request.getParameter("bank_nm");
String bank_lmt_usd = request.getParameter("bank_lmt_usd")==null?"":request.getParameter("bank_lmt_usd");
String bank_lmt_inr = request.getParameter("bank_lmt_inr")==null?"":request.getParameter("bank_lmt_inr");

dbcredit.setCallFlag("BANK_LIMIT_EXPOSURE");
dbcredit.setComp_cd(owner_cd);
dbcredit.setReport_dt(report_dt);
dbcredit.init();

Vector VMST_BANK_NM = dbcredit.getVMST_BANK_NM();
Vector VMST_BANK_ABBR = dbcredit.getVMST_BANK_ABBR();
Vector VCOUNTERPARTY_NM = dbcredit.getVCOUNTERPARTY_NM();
Vector VDEAL_NO = dbcredit.getVDEAL_NO();
Vector VEXPIRE_DT = dbcredit.getVEXPIRE_DT();
Vector VISSUE_DT = dbcredit.getVISSUE_DT();
Vector VCONFIRM_BANK_NM = dbcredit.getVCONFIRM_BANK_NM();
Vector VISS_BANK_NM = dbcredit.getVISS_BANK_NM();
Vector VSEC_REF_NO = dbcredit.getVSEC_REF_NO();
Vector VCOUNTERPARTY_ABBR = dbcredit.getVCOUNTERPARTY_ABBR();
Vector VBANK_LIMIT = dbcredit.getVBANK_LIMIT();
Vector VBANK_LIMIT_USD = dbcredit.getVBANK_LIMIT_USD();
Vector VBANK_EXPOSURE_INR = dbcredit.getVBANK_EXPOSURE_INR();
Vector VBANK_EXPOSURE_USD = dbcredit.getVBANK_EXPOSURE_USD();
Vector VBANK_AVAILABILITY_INR = dbcredit.getVBANK_AVAILABILITY_INR();
Vector VBANK_AVAILABILITY_USD = dbcredit.getVBANK_AVAILABILITY_USD();
Vector VSTATUS = dbcredit.getVSTATUS();
Vector VCANCEL_DT = dbcredit.getVCANCEL_DT();
Vector VCO_ABBR = dbcredit.getVCO_ABBR();

%>
<body>
<form method="post" action="">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="d-flex justify-content-between">
						<div class="topheader">
				    		Bank Limit And Exposure Details
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div align="center">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Report Date</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="rpt_dt" value="<%=report_dt%>" disabled="disabled" size="8" maxLength="10" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<script>
										document.forms[0].report_dt.value="<%=report_dt%>"
									</script>
				    			</div>
							</div>
						</div>
					</div>&nbsp;
					<div class="row m-b-5">
						<div class="table-responsive">
							<table id="search_by_filter" class="table table-bordered" id="filterbysearch">
								<thead>
									<tr>
										<th align="center">Bank Name</th>
										<th align="center">Bank Limit (INR)</th>
										<th align="center">Bank Limit (USD)</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td align="center" id="bank_name"><%=bank_nm %></td>
										<td align="center" id="bank_limit_inr"><%=bank_lmt_inr %></td>
										<td align="center" id="bank_limit_usd"><%=bank_lmt_usd %></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
      					<div class="table-responsive">
							<table class="table table-bordered">
								<thead>
									<tr>
										<th align="center">Legal Entity</th>
										<th align="center">Customer Name</th>
										<th align="center">Deal No#</th>
										<th align="center">Security Ref#</th>
										<th align="center">Issuing Bank Name</th>
										<th align="center">Confirming Bank Name</th>
										<th align="center">Issue Date</th>
										<th align="center">Expire Date</th>
										<th align="center">Exposure(INR)</th>
										<th align="center">Exposure(USD)</th>
										<th align="center">Availability(INR)</th>
										<th align="center">Availability(USD)</th>
										<th align="center">Status</th>
										<th align="center">Cancellation/Restate Date</th>
									</tr>
								</thead>
								<tbody>
								<%int count=0;
								if(VCOUNTERPARTY_NM.size()>0){ %>
									<%for(int i=0;i<VCOUNTERPARTY_NM.size();i++){ %>
										<%if(VISS_BANK_NM.elementAt(i).equals(bank_nm) && VCONFIRM_BANK_NM.elementAt(i).equals("")){ count++;%>
											<tr>
												<td align="center"><%=VCO_ABBR.elementAt(i) %></td>
												<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
												<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
												<td align="center"><%=VSEC_REF_NO.elementAt(i) %></td>
												<td align="center"><%=VISS_BANK_NM.elementAt(i) %></td>
												<td align="center"><%=VCONFIRM_BANK_NM.elementAt(i) %></td>
												<td align="center"><%=VISSUE_DT.elementAt(i) %></td>
												<td align="center"><%=VEXPIRE_DT.elementAt(i) %></td>
												<td align="right"><%=VBANK_EXPOSURE_INR.elementAt(i) %></td>
												<td align="right"><%=VBANK_EXPOSURE_USD.elementAt(i) %></td>
												<td align="right"
													<%if (Double.parseDouble(""+VBANK_AVAILABILITY_INR.elementAt(i))<0){%>
														style="color: red"
													<%} %>>
														<%=VBANK_AVAILABILITY_INR.elementAt(i) %>
												</td>
												<td align="right"
													<%if (Double.parseDouble(""+VBANK_AVAILABILITY_USD.elementAt(i))<0){%>
														style="color: red"
													<%} %>>
														<%=VBANK_AVAILABILITY_USD.elementAt(i) %>
												</td>
												<td align="center"><b>
													<%if(VSTATUS.elementAt(i).equals("P")){ %>
														<span class="alert alert-primary">Pending</span>
													<%}else if(VSTATUS.elementAt(i).equals("O")){  %>
														<span class="alert alert-success">In Order</span>
													<%}else if(VSTATUS.elementAt(i).equals("C")){  %>
														<span class="alert alert-danger">Cancelled</span>
													<%}else if(VSTATUS.elementAt(i).equals("A")){  %>
														<span class="alert alert-secondary">Pending For Amendment</span>
													<%}else if(VSTATUS.elementAt(i).equals("R")){  %>
														<span class="alert alert-warning">Restated</span>
													<%}else if(VSTATUS.elementAt(i).equals("D")){  %>
														<span class="alert alert-info">Dummy</span>
													<%} %></b>
												</td>
												<td align="center"><%=VCANCEL_DT.elementAt(i) %></td>
											</tr>
										<%}else if(VCONFIRM_BANK_NM.elementAt(i).equals(bank_nm)){count++; %>
											<tr>
												<td align="center"><%=VCO_ABBR.elementAt(i) %></td>
												<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
												<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
												<td align="center"><%=VSEC_REF_NO.elementAt(i) %></td>
												<td align="center"><%=VISS_BANK_NM.elementAt(i) %></td>
												<td align="center"><%=VCONFIRM_BANK_NM.elementAt(i) %></td>
												<td align="center"><%=VISSUE_DT.elementAt(i) %></td>
												<td align="center"><%=VEXPIRE_DT.elementAt(i) %></td>
												<td align="right"><%=VBANK_EXPOSURE_INR.elementAt(i) %></td>
												<td align="right"><%=VBANK_EXPOSURE_USD.elementAt(i) %></td>
												<td align="right"
													<%if (Double.parseDouble(""+VBANK_AVAILABILITY_INR.elementAt(i))<0){%>
														style="color: red"
													<%} %>>
														<%=VBANK_AVAILABILITY_INR.elementAt(i) %>
												</td>
												<td align="right"
													<%if (Double.parseDouble(""+VBANK_AVAILABILITY_USD.elementAt(i))<0){%>
														style="color: red"
													<%} %>>
														<%=VBANK_AVAILABILITY_USD.elementAt(i) %>
												</td>
												<td align="center"><b>
													<%if(VSTATUS.elementAt(i).equals("P")){ %>
														<span class="alert alert-primary">Pending</span>
													<%}else if(VSTATUS.elementAt(i).equals("O")){  %>
														<span class="alert alert-success">In Order</span>
													<%}else if(VSTATUS.elementAt(i).equals("C")){  %>
														<span class="alert alert-danger">Cancelled</span>
													<%}else if(VSTATUS.elementAt(i).equals("A")){  %>
														<span class="alert alert-secondary">Pending For Amendment</span>
													<%}else if(VSTATUS.elementAt(i).equals("R")){  %>
														<span class="alert alert-warning">Restated</span>
													<%}else if(VSTATUS.elementAt(i).equals("D")){  %>
														<span class="alert alert-info">Dummy</span>
													<%} %></b>
												</td>
												<td align="center"><%=VCANCEL_DT.elementAt(i) %></td>
											</tr>
										<%} %>
									<%} %>
								<%}
								if(count==0){%>
								<tr>
									<td colspan="14" align="center"><%=utilmsg.infoMessage("<b>Bank Limit Data Is Not Available!</b>") %></td>
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
</div>

<input type="hidden" name="bank_nm" value="<%=bank_nm%>">
<input type="hidden" name="bank_lmt_usd" value="<%=bank_lmt_usd%>">
<input type="hidden" name="bank_lmt_inr" value="<%=bank_lmt_inr%>">

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