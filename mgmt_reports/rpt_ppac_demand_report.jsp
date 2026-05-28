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
	var month=document.forms[0].month.value;
	var year=document.forms[0].year.value;
	var u=document.forms[0].u.value;
	var state_cd=document.forms[0].state_cd.value;
	var force_mark_gj=document.forms[0].force_mark_gj;
	
	if(force_mark_gj.checked)
	{
		force_mark_gj.value="Y";
	}
	else
	{
		force_mark_gj.value="N";
	}
	
	var url="rpt_ppac_demand_report.jsp?month="+month+"&year="+year+"&state_cd="+state_cd+"&force_mark_gj="+force_mark_gj.value+"&u="+u;
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function exportToXls()
{
	var sysdate = document.forms[0].sysdate.value;
	var comp_abbr = document.forms[0].comp_abbr.value;
	var month=document.forms[0].month.value;
	var year=document.forms[0].year.value;
	var state_cd=document.forms[0].state_cd.value;
	
	var force_mark_gj=document.forms[0].force_mark_gj;
	
	if(force_mark_gj.checked)
	{
		force_mark_gj.value="Y";
	}
	else
	{
		force_mark_gj.value="N";
	}
	
	var sysdate1 = sysdate.replace("/","_");
	
	//var fileName = comp_abbr+"_"+"PPAC_Demand_Report_"+month+"_"+year+"_"+sysdate1+".xls";
	var fileName = comp_abbr+month+year+"_"+sysdate1+".xls";
	
	var url="xls_ppac_demand_report.jsp?month="+month+"&year="+year+"&fileName="+fileName+"&state_cd="+state_cd+"&force_mark_gj="+force_mark_gj.value;
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
String state_cd=request.getParameter("state_cd")==null?"0":request.getParameter("state_cd");
String force_mark_gj=request.getParameter("force_mark_gj")==null?"Y":request.getParameter("force_mark_gj");

if(month.length() == 1)
{
	month="0"+month; 
}

int filter_start_year = CommonVariable.filter_start_year;

govt_rpt.setCallFlag("PPAC_DEMAND_RPT");
govt_rpt.setComp_cd(owner_cd);
govt_rpt.setMonth(month);
govt_rpt.setYear(year);
govt_rpt.setState_cd(state_cd);
govt_rpt.setForce_mark_gj(force_mark_gj);
govt_rpt.init();

Vector VMST_STATE_CD = govt_rpt.getVMST_STATE_CD();

Vector VCOMPANY_CODE = govt_rpt.getVCOMPANY_CODE(); 
Vector VPRODUCT_CD = govt_rpt.getVPRODUCT_CD();
Vector VTRANSPORT_MODE = govt_rpt.getVTRANSPORT_MODE();
Vector VSTATE_CD = govt_rpt.getVSTATE_CD();
Vector VREVENUE_DIST_CD = govt_rpt.getVREVENUE_DIST_CD();
Vector VCUST_CATEGORY = govt_rpt.getVCUST_CATEGORY();
Vector VEND_USE = govt_rpt.getVEND_USE();
Vector VQTY_MMBTU = govt_rpt.getVQTY_MMBTU();
Vector VQTY_MMSCM = govt_rpt.getVQTY_MMSCM();
Vector VQTY_MT = govt_rpt.getVQTY_MT();
Vector VMONTH_YEAR = govt_rpt.getVMONTH_YEAR();
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
					    	PPAC Monthly Demand Report
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
						<div class="col-sm-1 col-xs-1 col-md-1">
							<div class="form-group row">
							</div>
						</div>						
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<input type="checkbox" class="form-check-input" name="force_mark_gj" value="<%=force_mark_gj %>" <%if(force_mark_gj.equals("Y")){%>checked<%}%>>
									<label class="form-label"><b>Force Mark GJ</b></label>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>State Code</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="state_cd">
										<option value="0">--All--</option>
										<%for(int i=0;i<VMST_STATE_CD.size();i++){ %>
											<option value="<%=VMST_STATE_CD.elementAt(i)%>"><%=VMST_STATE_CD.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].state_cd.value="<%=state_cd%>"</script>
								</div>
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
					<div class="table-responsive">
						<table class="table table-bordered" id="example">
							<thead id="tbsearch">
								<tr>
									<th>Sr#</th>
									<th>Company Code</th>
									<th>Product Code</th>
									<th>Transport Mode</th>
									<th>State Code</th>
									<th>Revenue District Code</th>
									<th>Company Category</th>
									<th>End Use</th>
									<th>Quantity Supplied<br>(MMBTU)</th>
									<th>Quantity Supplied<br>(MT)</th>
									<th>Month & Year</th>
								</tr>
							</thead>
							<tbody>
								<%if(VMONTH_YEAR.size()>0){%>
									<%for(int i=0;i<VMONTH_YEAR.size();i++){%>
										<tr>
											<td align="center"><%=(i+1)%></td>
											<td align="center"><%=VCOMPANY_CODE.elementAt(i)%></td>
											<td align="center"><%=VPRODUCT_CD.elementAt(i)%></td>
											<td align="center"><%=VTRANSPORT_MODE.elementAt(i)%></td>
											<td align="center"><%=VSTATE_CD.elementAt(i)%></td>
											<td align="center"><%=VREVENUE_DIST_CD.elementAt(i)%></td>
											<td align="center"><%=VCUST_CATEGORY.elementAt(i)%></td>
											<td align="center"><%=VEND_USE.elementAt(i)%></td>
											<td align="right"><%=VQTY_MMBTU.elementAt(i)%></td>
											<td align="right"><%=VQTY_MT.elementAt(i)%></td>
											<td align="center"><%=VMONTH_YEAR.elementAt(i)%></td>
										</tr>
									<%} %>
								<%}else{%>
									<tr>
										<td colspan="11" align="center"><%=utilmsg.infoMessage("<b>No PPAC Demand Data Found!</b>") %></td>
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