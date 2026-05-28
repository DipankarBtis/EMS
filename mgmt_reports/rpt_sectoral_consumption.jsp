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
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var month_to = document.forms[0].month_to.value;
	var year_to = document.forms[0].year_to.value;
	var tmp_month = document.forms[0].tmp_from_month.value;
	var tmp_year = document.forms[0].tmp_from_year.value;
	var tmp_month_to = document.forms[0].tmp_to_month.value;
	var tmp_year_to = document.forms[0].tmp_to_year.value
	
	var u = document.forms[0].u.value;
	
	var flag=checkMonthYearRange(document.forms[0].month,document.forms[0].year,document.forms[0].month_to,document.forms[0].year_to);
	if(flag==true)
	{
		var url="rpt_sectoral_consumption.jsp?month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to+"&u="+u;
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		document.forms[0].month.value=tmp_month;
		document.forms[0].year.value=tmp_year;
		document.forms[0].month_to.value=tmp_month_to;
		document.forms[0].year_to.value=tmp_year_to;
	}
}

function exportToXls()
{
	var sysdate = document.forms[0].sysdate.value;
	var comp_abbr = document.forms[0].comp_abbr.value;
	var month=document.forms[0].month.value;
	var year=document.forms[0].year.value;
	var month_to = document.forms[0].month_to.value;
	var year_to = document.forms[0].year_to.value;
	var sysdate1 = sysdate.replace("/","_");
	
	//var fileName = comp_abbr+"_"+"3A_Sectorial_Consumption_"+month+"_"+year+"_"+sysdate1+".xls";
	var fileName = comp_abbr+"_"+"3A_Sectorial_Consumption"+".xls";
	
	var url="xls_sectoral_consumption.jsp?month="+month+"&year="+year+"&fileName="+fileName+"&month_to="+month_to+"&year_to="+year_to;
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
String month_to=request.getParameter("month_to")==null?""+currentMonth:request.getParameter("month_to");
String year_to=request.getParameter("year_to")==null?""+currentYear:request.getParameter("year_to");

if(month.length() == 1)
{
	month="0"+month; 
}
if(month_to.length() == 1)
{
	month_to="0"+month_to; 
}


int filter_start_year = CommonVariable.filter_start_year;

govt_rpt.setCallFlag("SECTORAL_CONSUMPTION_RPT");
govt_rpt.setComp_cd(owner_cd);
govt_rpt.setMonth(month);
govt_rpt.setYear(year);
govt_rpt.setMonth_to(month_to);
govt_rpt.setYear_to(year_to);
govt_rpt.init();

Vector VMONTH_YEAR = govt_rpt.getVMONTH_YEAR();
Vector VSUPP_COMP_CD = govt_rpt.getVSUPP_COMP_CD();
Vector VPRODUCT_CD = govt_rpt.getVPRODUCT_CD();
Vector VSECTOR_CD = govt_rpt.getVSECTOR_CD();
Vector VRECEV_COMP_CD = govt_rpt.getVRECEV_COMP_CD();
Vector VSTATE_CD = govt_rpt.getVSTATE_CD();
Vector VQTY_MMSCM = govt_rpt.getVQTY_MMSCM();
Vector VQTY_MMBTU = govt_rpt.getVQTY_MMBTU();
Vector VCALORIFIC_VAL = govt_rpt.getVCALORIFIC_VAL();

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
					    	<!-- Sectoral Consumption Report -->
					    	PPAC-3A Sectorial Report	<!-- AS PER VIJAY'S FEED BACK -->
					    </div>
					    <div class="col-auto">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x" onclick="exportToXls();" ></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
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
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>to</b></label>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col">
					  				<select class="form-select form-select-sm" name="month_to">
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
									<script>document.forms[0].month_to.value="<%=month_to%>"</script>
								</div>
								<div class="col">
					  				<select class="form-select form-select-sm" name="year_to">
					  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year_to.value="<%=year_to%>"</script>
								</div>
							</div>
				  		</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
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
									<th>Sr#</th>
									<th>Month & year</th>
									<th>Supplying Company Code</th>
									<th>Product Code</th>
									<th>Sector Code</th>
									<th>Receiving Company Code</th>
									<th>State Code</th>
									<th>Quantity Supplied<br>(MMBTU)</th>
									<th>Quantity Supplied<br>(MMSCM)</th>
									<th>Average Calorific Value<br>(Kcal/SCM)</th>
								</tr>
							</thead>
							<tbody>
								<%if(VMONTH_YEAR.size()>0){%>
									<%for(int i=0;i<VMONTH_YEAR.size();i++){%>
										<tr>
											<td align="center"><%=(i+1)%></td>
											<td align="center"><%=VMONTH_YEAR.elementAt(i)%></td>
											<td align="center"><%=VSUPP_COMP_CD.elementAt(i)%></td>
											<td align="center"><%=VPRODUCT_CD.elementAt(i)%></td>
											<td align="center"><%=VSECTOR_CD.elementAt(i)%></td>
											<td align="center"><%=VRECEV_COMP_CD.elementAt(i)%></td>
											<td align="center"><%=VSTATE_CD.elementAt(i)%></td>
											<td align="center"><%=VQTY_MMBTU.elementAt(i)%></td>
											<td align="right"><%=VQTY_MMSCM.elementAt(i)%></td>
											<td align="right"><%=VCALORIFIC_VAL.elementAt(i)%></td>
										</tr>
									<%} %>
								<%}else{%>
									<tr>
										<td colspan="10" align="center"><%=utilmsg.infoMessage("<b>No Data Found!</b>") %></td>
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

<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="comp_abbr" value="<%=owner_abbr%>">

<input type="hidden" name="tmp_from_month" value="<%=month%>">
<input type="hidden" name="tmp_from_year" value="<%=year%>">
<input type="hidden" name="tmp_to_month" value="<%=month_to%>">
<input type="hidden" name="tmp_to_year" value="<%=year_to%>">

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
</html>