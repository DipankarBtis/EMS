
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

var newWindow;
function openBankList(VMST_BANK_CD,VBANK_LIMIT,VBANK_LIMIT_USD)
{
	var report_dt = document.forms[0].report_dt.value;
	
	document.forms[0].bank_cd.value = VMST_BANK_CD;
	var bank_cd = document.forms[0].bank_cd.value;
	
	document.forms[0].bank_lmt_inr.value = VBANK_LIMIT;
	var bank_lmt_inr = document.forms[0].bank_lmt_inr.value;
	
	document.forms[0].bank_lmt_usd.value  = VBANK_LIMIT_USD;
	var bank_lmt_usd = document.forms[0].bank_lmt_usd.value; 
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("rpt_bank_limit_expo_dtl.jsp?report_dt="+report_dt+"&bank_cd="+bank_cd+"&bank_lmt_inr="+bank_lmt_inr+"&bank_lmt_usd="+bank_lmt_usd,"Bank Limit List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("rpt_bank_limit_expo_dtl.jsp?report_dt="+report_dt+"&bank_cd="+bank_cd+"&bank_lmt_inr="+bank_lmt_inr+"&bank_lmt_usd="+bank_lmt_usd,"Bank Limit List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function exportToXls()
{
	var report_dt = document.forms[0].report_dt.value;
	
	var bank_cd = document.forms[0].bank_cd.value;
	
	var bank_lmt_inr = document.forms[0].bank_lmt_inr.value;

	var bank_lmt_usd = document.forms[0].bank_lmt_usd.value; 
	
	var url = "xls_bank_limit_expo_report.jsp?fileName=Bank Limit And Exposure Report "+report_dt+".xls&report_dt="+report_dt+"&bank_cd="+bank_cd+"&bank_lmt_inr="+bank_lmt_inr+"&bank_lmt_usd="+bank_lmt_usd;

	location.replace(url);
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CR_ReceivableReport" id="dbcredit" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String report_dt = request.getParameter("report_dt")==null?sysdate:request.getParameter("report_dt");
String bank_cd = request.getParameter("bank_cd")==null?"":request.getParameter("bank_cd");
String bank_nm = request.getParameter("bank_nm")==null?"":request.getParameter("bank_nm");
String bank_lmt_usd = request.getParameter("bank_lmt_usd")==null?"":request.getParameter("bank_lmt_usd");
String bank_lmt_inr = request.getParameter("bank_lmt_inr")==null?"":request.getParameter("bank_lmt_inr");

dbcredit.setCallFlag("BANK_LIMIT_EXPOSURE");
dbcredit.setComp_cd(owner_cd);
dbcredit.setReport_dt(report_dt);
dbcredit.init();

Vector VMST_BANK_CD = dbcredit.getVMST_BANK_CD();
Vector VMST_BANK_NM = dbcredit.getVMST_BANK_NM();
Vector VMST_BANK_ABBR = dbcredit.getVMST_BANK_ABBR();
Vector VBANK_LIMIT = dbcredit.getVBANK_LIMIT();
Vector VBANK_LIMIT_USD = dbcredit.getVBANK_LIMIT_USD();
Vector VEXPOSURE_INR = dbcredit.getVEXPOSURE_INR();
Vector VEXPOSURE_USD = dbcredit.getVEXPOSURE_USD();
Vector VAVAILABILITY_INR = dbcredit.getVAVAILABILITY_INR();
Vector VAVAILABILITY_USD = dbcredit.getVAVAILABILITY_USD();

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
				    		Bank Limit And Exposure Report
	   	 				</div>
	   	 				<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
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
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="report_dt" id="report_dt" value="<%=report_dt%>" maxLength="10" 
						      				onchange="validateDate(this);refresh();">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
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
									<th align="center"></th>
									<th align="center">Sr#</th>
									<th align="center">Bank Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="bank_nm_search" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Bank Limit(INR)</th>
									<th align="center">Bank Limit(USD)</th>
									<th align="center">Exposure(INR)</th>
									<th align="center">Exposure(USD)</th>
									<th align="center">Availability(INR)</th>
									<th align="center">Availability(USD)</th>
								</tr>
							</thead>
							<tbody>
								<%if(VMST_BANK_NM.size()>0){ %>
									<%for(int i=0;i<VMST_BANK_NM.size();i++){%>
										<tr>
											<td align="center">
												<%-- <%if(write_access.equals("Y")){ %> --%>
												<font title="Click to View Details" style="color:black">
													<i class="fa fa-eye fa-lg"
													onclick=" openBankList('<%=VMST_BANK_CD.elementAt(i)%>','<%=VBANK_LIMIT.elementAt(i)%>','<%=VBANK_LIMIT_USD.elementAt(i)%>');"></i>
												</font>&nbsp;
												<%-- <%}%> --%>
											</td>
											<td align="center"><%=i+1%></td>
											<td align="center"><%=VMST_BANK_NM.elementAt(i)%></td>
											<td align="right"><%=VBANK_LIMIT.elementAt(i)%></td>
											<td align="right"><%=VBANK_LIMIT_USD.elementAt(i)%></td>
											<td align="right" 
												<%if (Double.parseDouble(""+VEXPOSURE_INR.elementAt(i))>Double.parseDouble(""+VBANK_LIMIT.elementAt(i))){%>
													style="color: red"
												<%} %>>
												<%=VEXPOSURE_INR.elementAt(i)%>
											</td>
											<td align="right"
											<%if (Double.parseDouble(""+VEXPOSURE_USD.elementAt(i))>Double.parseDouble(""+VBANK_LIMIT_USD.elementAt(i))){%>
													style="color: red"
												<%} %>>
												<%=VEXPOSURE_USD.elementAt(i)%>
											</td>
											<td align="right"><%=VAVAILABILITY_INR.elementAt(i)%></td>
											<td align="right"><%=VAVAILABILITY_USD.elementAt(i)%></td>
										</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="8" align="center"><%=utilmsg.infoMessage("<b>Bank Limit Data Is Not Available!</b>") %></td>
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
<input type="hidden" name="bank_cd" value="<%=bank_cd%>">
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