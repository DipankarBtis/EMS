<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var u=document.forms[0].u.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var url="rpt_sap_recon_consolidate_report.jsp?u="+u+"&month="+month+"&year="+year;//+"&gl_cd="+gl_cd
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function exportToXls()
{
	var sysdate = document.forms[0].sysdate.value;
	var month=document.forms[0].month.value;
	var year=document.forms[0].year.value;
	var u=document.forms[0].u.value;
	
	sysdate = sysdate.toString();
	sysdate = sysdate.split('/').join('');
	
	var url = "xls_sap_recon_consolidate_report.jsp?fileName=SAP Recon Consolidate Report "+sysdate+".xls&u="+u+"&month="+month+"&year="+year;
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sap_interface" id="db_intrface" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt = request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
String gl_cd = request.getParameter("gl_cd")==null?"0":request.getParameter("gl_cd");
String tds = request.getParameter("tds")==null?"Y":request.getParameter("tds");
String tcs = request.getParameter("tcs")==null?"Y":request.getParameter("tcs");
String tax = request.getParameter("tax")==null?"Y":request.getParameter("tax");

if(month.length() == 1)
{
	month="0"+month; 
}

int filter_start_year = CommonVariable.filter_start_year;

db_intrface.setCallFlag("SAP_RECON_CONSOLIDATE_RPT");
db_intrface.setComp_cd(owner_cd);
db_intrface.setCounterparty_cd(counterparty_cd);
db_intrface.setMonth(month);
db_intrface.setYear(year);
db_intrface.setEntity_role(entity_role);
db_intrface.setGl_cd(gl_cd);
db_intrface.setTds_flag(tds);
db_intrface.setTcs_flag(tcs);
db_intrface.setTax_flag(tax);
db_intrface.init();

String amt_sum = db_intrface.getAmt_sum();
String amt_usd_sum = db_intrface.getAmt_usd_sum();
String sap_sum = db_intrface.getSap_sum();
String sap_sum_usd = db_intrface.getSap_Sum_Usd();
String delta_sap_sum=db_intrface.getDelta_Sap_Sum();
String abs_delta_sap_sum = db_intrface.getAbs_delta_sap_sum();

Vector VCON_GL = db_intrface.getVCON_GL();
Vector VCON_CURR = db_intrface.getVCON_CURR();
Vector VCON_COCD = db_intrface.getVCON_COCD();
Vector VCON_PERIOD=db_intrface.getVCON_PERIOD();
Vector VCON_GL_DECR=db_intrface.getVCON_GL_DECR();
Vector VCON_YEAR=db_intrface.getVCON_YEAR();
Vector VCON_MONTH=db_intrface.getVCON_MONTH();
Vector VCON_EMS_AMT = db_intrface.getVCON_EMS_AMT();
Vector VCON_EMS_AMT_USD = db_intrface.getVCON_EMS_AMT_USD();
Vector VCON_SAP_AMT = db_intrface.getVCON_SAP_AMT();
Vector VCON_SAP_AMT_USD = db_intrface.getVCON_SAP_AMT_USD();
Vector VCON_DIFF_AMT = db_intrface.getVCON_DIFF_AMT();
Vector VCON_ABS_DIFF_AMT = db_intrface.getVCON_ABS_DIFF_AMT();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<%if(!msg.equals("")){
				if(msg_type.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
				<%}else if(msg_type.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
				<%}
			} %>
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
					    	SAP Recon (Consolidate) Report
					    </div>
					    <div class="row justify-content-end">
						    <div class="col-auto">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x" onclick="exportToXls();" ></i></span>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
							</div>
						</div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>Month/Year</b></label>
					  		</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="month" onchange="refresh();">
										<option value="01" label="January">January</option>
										<option value="02" label="February">February</option>
										<option value="03" label="March">March</option>
										<option value="04" label="April">April</option>
										<option value="05" label="May">May</option>
										<option value="06" label="June">June</option>
										<option value="07" label="July">July</option>
										<option value="08" label="August">August</option>
										<option value="09" label="September">September</option>
										<option value="10" label="October">October</option>
										<option value="11" label="November">November</option>
										<option value="12" label="December">December</option>
									</select>
									<script>document.forms[0].month.value="<%=month%>"</script>
								</div>
								<div class="col">
					  				<select class="form-select form-select-sm" name="year" onchange="refresh();">
					  					<%for(int i=(currentYear); i > filter_start_year;i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered" id="example">
							<thead id="tbsearch">
								<tr>
									<th>CoCd</th>
									<th>Period</th>		
									<th>GL Account</th>
									<th>GL Description</th>
									<th>Currency</th>
									<th>Year</th>
									<th>Month</th>
									<th>EMS USD Value</th>
									<th>SAP USD Value</th>
									<th>EMS INR Value</th>
									<th>SAP INR Value</th>
									<th>Difference Value</th>
									<th>ABS Difference Value</th>
								</tr>
							</thead>
							<tbody>
								<%if(VCON_GL.size()>0){%>
									<%for(int i=0;i<VCON_GL.size();i++){%>
										<tr>
											<td align="center"><%=VCON_COCD.elementAt(i) %></td>
											<td align="center"><%=VCON_PERIOD.elementAt(i) %></td>
											<td align="center"><%=VCON_GL.elementAt(i)%></td>
											<td align="center"><%=VCON_GL_DECR.elementAt(i) %></td>
											<td align="center"><%=VCON_CURR.elementAt(i) %></td>
											<td align="center"><%=VCON_YEAR.elementAt(i) %></td>
											<td align="center"><%=VCON_MONTH.elementAt(i) %></td>
											<td align="right"><%=VCON_EMS_AMT_USD.elementAt(i) %></td>
											<td align="right"><%=VCON_SAP_AMT_USD.elementAt(i) %></td>		<!-- SAP USD VALUE -->
											<td align="right"><%=VCON_EMS_AMT.elementAt(i) %></td>
											<td align="right"><%=VCON_SAP_AMT.elementAt(i) %></td>		<!-- SAP INR VALUE -->
											<td align="right" <%if(!VCON_DIFF_AMT.elementAt(i).equals("") && (Double.parseDouble(VCON_DIFF_AMT.elementAt(i).toString())!=0)){%>style="color:red;"<%}%>><%=VCON_DIFF_AMT.elementAt(i) %></td>
											<td align="right"><%=VCON_ABS_DIFF_AMT.elementAt(i) %></td>
										</tr>
									<%} %>
										<tr>
											<td align="right" colspan="7">Total</td>
											<td align="right"><%=amt_usd_sum %></td>
											<td align="right"><%=sap_sum_usd %></td>
											<td align="right"><%=amt_sum %></td>
											<td align="right"><%=sap_sum %></td>
											<td align="right"><%=delta_sap_sum %></td>
											<td align="right"><%=abs_delta_sap_sum %></td>
										</tr>
								<%}else{%>
									<tr>
										<td colspan="13" align="center"><%=utilmsg.infoMessage("<b>No GL Account Data Found!</b>") %></td>
									</tr>
								<%} %>
							</tbody>
						</table>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div align="center"><%=utilmsg.infoMessage("<b>Note: The Month/Year will show result with respect to Posting Month/Year!</b>") %></div>
   					</div>
   				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="sysdate" value="<%=sysdate%>">
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
</body>
<script>
$(document).ready(function() {
	
	$('#tbsearch th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "Sr#")
		{
			//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
		}
		else
		{
			$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:100px"/></div>');
		}
	});
	
});

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example");
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	//tbody=tr[i].getElementsByTagName("tbody");alert(tbody)
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
</html>