<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ page import="java.util.*" %>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var u = document.forms[0].u.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var url="rpt_jodi_data.jsp?month="+month+"&year="+year+"&u="+u;
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function exportToXls()
{
	var comp_abbr=document.forms[0].comp_abbr.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var month_nm = document.forms[0].month_nm.value; 
	
	var fileName=comp_abbr+"_Jodi_Data_MoPNG-"+month_nm+" "+year.substring(2)+".xls";
	var url="xls_jodi_data.jsp?month="+month+"&year="+year+"&fileName="+fileName;
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_Govt_Reports" id="govt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");

if(month.length() == 1)
{
	month="0"+month; 
}

String month_nm=utildate.getMonthNameMON("01/"+month+"/"+year);

int filter_start_year = CommonVariable.filter_start_year;

govt_rpt.setCallFlag("JODI_DATA_RPT");
govt_rpt.setComp_cd(owner_cd);
govt_rpt.setMonth(month);
govt_rpt.setYear(year);
govt_rpt.init();

Vector VNATURAL_GAS_DESC = govt_rpt.getVNATURAL_GAS_DESC();
Vector VLNG_DESC = govt_rpt.getVLNG_DESC();
Vector VUNLOADED_DATA = govt_rpt.getVUNLOADED_DATA();

String sales_mmbtu = govt_rpt.getSales_mmbtu();
String sales_mmscm = govt_rpt.getSales_mmscm();
String sales_tera_joules = govt_rpt.getSales_tera_joules();
String unloaded_mmbtu = govt_rpt.getUnloaded_mmbtu();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form action="">
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
					    	Monthly JODI Gas Data
					    </div>
					    <div class="col-auto">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x" onclick="exportToXls();" ></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
							</div>
						</div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>Month/Year</b></label>
					  		</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="month">
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
					  				<select class="form-select form-select-sm" name="year">
					  					<%for(int i=(currentYear); i > filter_start_year;i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-sm-1 col-xs-1 col-md-1">
									<div class="form-group row">
										<div class="col-auto">
											<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Natural Gas Details</label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered">
								<thead>
									<tr>
										<th rowspan="3">Sr#<br>&nbsp;<br>&nbsp;</th>
										<th rowspan="3">Description<br>&nbsp;<br>&nbsp;</th>
										<th colspan="3">Total</th>
									</tr>
									<tr>
										<th>MMBTU</th>
										<th>Million M<sup>3</sup><br>(at 15&deg;C, 760 mm hg)</th>
										<th>TeraJoules</th>
									</tr>
									<tr>
										<th></th>
										<th>A</th>
										<th>B</th>
									</tr>
								</thead>
								<tbody>
									<%//if(!sales_mmbtu.equals("")){
										int ctn=0;
									%>
										<%for(int i=0;i<VNATURAL_GAS_DESC.size();i++){%>
											<tr>
												<%if(VNATURAL_GAS_DESC.elementAt(i).equals("Pipeline")||VNATURAL_GAS_DESC.elementAt(i).equals("of which: Power Generation")){%>
													<td align="center"></td>
												<%}else{%>
													<td align="center"><%=++ctn%></td>
												<%}%>
												<td><%if(VNATURAL_GAS_DESC.elementAt(i).equals("Pipeline")||VNATURAL_GAS_DESC.elementAt(i).equals("of which: Power Generation")){%>&nbsp;&nbsp;<%}%><%=VNATURAL_GAS_DESC.elementAt(i) %></td>								
												<%if(VNATURAL_GAS_DESC.elementAt(i).equals("Gross Inland Deliveries")){%>
													<td align="right"><b><%=sales_mmbtu%></b></td>
													<td align="right"><b><%=sales_mmscm %></b></td>
													<td align="right"><b><%=sales_tera_joules %></b></td>
												<%}else{%>
													<td align="right"></td>
													<td align="right"></td>
													<td align="right"></td>
												<%}%>
											</tr>
										<%} %>
									<%-- <%} else{%>
										<tr>
											<td colspan="5" align="center"><%=utilmsg.infoMessage("<b>No Natural Gas Details Found!</b>") %></td>
										</tr>
									<%} %> --%>
								</tbody>
							</table>
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> LNG Details</label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Description</th>
										<th>Total</th>
									</tr>
								</thead>
								<tbody>
									<%//if(!unloaded_mmbtu.equals("")){
										int ctn1=0;
									%>
										<%for(int i=0;i<VLNG_DESC.size();i++){%>
											<tr>
												<%if(VLNG_DESC.elementAt(i).equals("&nbsp;&nbsp;of which: Power Generation <b>(in TeraJoules)</b>")){%>
													<td align="center"></td>
												<%}else{%>
													<td align="center"><%=++ctn1%></td>
												<%} %>
												<td><%=VLNG_DESC.elementAt(i) %></td>
												<td align="right"><b><%=VUNLOADED_DATA.elementAt(i) %></b></td>
											</tr>
										<%}%>
									<%-- <%}else{%>
										<tr>
											<td colspan="3" align="center"><%=utilmsg.infoMessage("<b>No LNG Details Found!</b>") %></td>
										</tr>
									<%}%> --%>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="comp_abbr" value="<%=owner_abbr %>">
<input type="hidden" name="month_nm" value="<%=month_nm %>">
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
</html>
