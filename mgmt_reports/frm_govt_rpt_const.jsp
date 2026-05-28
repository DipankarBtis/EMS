<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script type="text/javascript">
function refresh()
{
	var u = document.forms[0].u.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var month_to=document.forms[0].month_to.value;
	var year_to=document.forms[0].year_to.value;
	var tmp_month = document.forms[0].tmp_from_month.value;
	var tmp_year = document.forms[0].tmp_from_year.value;
	var tmp_month_to = document.forms[0].tmp_to_month.value;
	var tmp_year_to = document.forms[0].tmp_to_year.value
	
	var u = document.forms[0].u.value;
	
	var flag=checkMonthYearRange(document.forms[0].month,document.forms[0].year,document.forms[0].month_to,document.forms[0].year_to);
	
	if(flag)
	{
		var url="frm_govt_rpt_const.jsp?month="+month+"&year="+year+
				"&month_to="+month_to+"&year_to="+year_to+"&u="+u;
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

function doClear()
{
    var opration=document.forms[0].opration.value;

    var temp_cap_type=document.forms[0].temp_cap_type;
    var temp_cap_month=document.forms[0].temp_cap_month;
    var temp_cap_year=document.forms[0].temp_cap_year; 
    var temp_cap_val=document.forms[0].temp_cap_val;

    var temp_month=document.forms[0].temp_month.value;
    var temp_year=document.forms[0].temp_year.value;
    
    if(opration === "INSERT")
    {
        temp_cap_type.disabled = false;
        temp_cap_month.disabled = false;
        temp_cap_year.disabled = false;

        temp_cap_type.value = "";
        temp_cap_val.value = "";
        temp_cap_month.value = temp_month;
        temp_cap_year.value = temp_year;
    }
    else if(opration === "MODIFY")
    {
        temp_cap_val.value = "";

        temp_cap_type.disabled = true;
        temp_cap_month.disabled = true;
        temp_cap_year.disabled = true;
    }
}


function doSubmit()
{
	var temp_cap_type = document.forms[0].temp_cap_type.value;
	var temp_cap_val = document.forms[0].temp_cap_val.value;
	var temp_cap_month = document.forms[0].temp_cap_month.value;
	var temp_cap_year = document.forms[0].temp_cap_year.value;
	
	var msg="";
	var flag=true;
	
	var cap_month_nm="";
	if(temp_cap_month=="01")
	{
		cap_month_nm="January";
	}
	if(temp_cap_month=="02")
	{
		cap_month_nm="February";
	}
	if(temp_cap_month=="03")
	{
		cap_month_nm="March";
	}
	if(temp_cap_month=="04")
	{
		cap_month_nm="April";
	}
	if(temp_cap_month=="05")
	{
		cap_month_nm="May";
	}
	if(temp_cap_month=="06")
	{
		cap_month_nm="June";
	}
	if(temp_cap_month=="07")
	{
		cap_month_nm="July";
	}
	if(temp_cap_month=="08")
	{
		cap_month_nm="August";
	}
	if(temp_cap_month=="09")
	{
		cap_month_nm="September";
	}
	if(temp_cap_month=="10")
	{
		cap_month_nm="October";
	}
	if(temp_cap_month=="11")
	{
		cap_month_nm="November";
	}
	if(temp_cap_month=="12")
	{
		cap_month_nm="December"; 
	}
	
	if(trim(temp_cap_type)=="")
	{
		msg+="Select Capacity Type!\n";
		flag=false;
	}
	if(trim(temp_cap_val)=="")
	{
		msg+="Enter Capacity Value!";
		flag=false;
	}
	
	if(flag==true)
	{
		var a = confirm("You want insert\\update capacity value for "+cap_month_nm+"-"+temp_cap_year);
		if(a)
		{
			document.forms[0].cap_type.value=temp_cap_type;
			document.forms[0].cap_val.value=temp_cap_val;
			document.forms[0].cap_month.value=temp_cap_month;
			document.forms[0].cap_year.value=temp_cap_year;
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function doModify(capType,capMonth,capYear,capValue)
{
    document.forms[0].opration.value = "MODIFY";

    var temp_cap_type=document.forms[0].temp_cap_type;
    var temp_cap_month=document.forms[0].temp_cap_month;
    var temp_cap_year=document.forms[0].temp_cap_year;
    
    temp_cap_type.value=capType;
    temp_cap_month.value=capMonth;
    temp_cap_year.value=capYear;
    document.forms[0].temp_cap_val.value=capValue;
    
    temp_cap_type.disabled = true;
    temp_cap_month.disabled = true;
    temp_cap_year.disabled = true;
}


function chkNumber(obj)
{
	if(isNaN(obj.value))
	{
		alert("Capacity Value must be Numeric!");
		obj.value='';
		return false;
	}
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
String cap_month=request.getParameter("cap_month")==null?""+currentMonth:request.getParameter("cap_month");
String cap_year=request.getParameter("cap_year")==null?""+currentYear:request.getParameter("cap_year");

if(month.length() == 1)
{
	month="0"+month; 
}
if(month_to.length() == 1)
{
	month_to="0"+month_to; 
}
if(cap_month.length() == 1)
{
	cap_month="0"+cap_month; 
}

int filter_start_year = CommonVariable.filter_start_year;

govt_rpt.setCallFlag("CAP_MASTER");
govt_rpt.setComp_cd(owner_cd);
govt_rpt.setMonth(month);
govt_rpt.setYear(year);
govt_rpt.setMonth_to(month_to);
govt_rpt.setYear_to(year_to);
govt_rpt.init();

Vector VMST_CAP_TYPE = govt_rpt.getVMST_CAP_TYPE();
Vector VMST_CAP_TYPE_NM = govt_rpt.getVMST_CAP_TYPE_NM();
Vector VCAP_TYPE = govt_rpt.getVCAP_TYPE();
Vector VCAP_VAL = govt_rpt.getVCAP_VAL();
Vector VMONTH_YEAR = govt_rpt.getVMONTH_YEAR();
Vector VCAP_NM = govt_rpt.getVCAP_NM();
Vector VMONTH = govt_rpt.getVMONTH();
Vector VYEAR = govt_rpt.getVYEAR();
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_Govt_Report">

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
					    	Government Report Const
					    </div>
					    <!-- <div class="col-auto">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x" onclick="exportToXls();" ></i></span>
						</div> -->
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
					<%if(write_access.equals("Y")){ %>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#capModal" onclick="doClear();">Add Capacity Value</label>
							</div>
						</div>
					</div><%} %>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Month-Year</th>
										<th>Capacity Type</th>
										<th>Capacity Value</th>
									</tr>
								</thead>
								<tbody>
									<%if(VCAP_TYPE.size()>0){%>
										<%for(int i=0;i<VCAP_TYPE.size();i++){ %>
											<tr>
												<td align="center"><%=i+1%>&nbsp;
													<%if(write_access.equals("Y")){ %>
													<font title="Click to Edit" style="color:var(--header_color)">
														<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#capModal" 
														onclick="doModify('<%=VCAP_TYPE.elementAt(i)%>','<%=VMONTH.elementAt(i)%>','<%=VYEAR.elementAt(i)%>','<%=VCAP_VAL.elementAt(i)%>')">
														</i>
													</font>
													<%}%>
												</td>
												<td align="center"><%=VMONTH_YEAR.elementAt(i) %></td>
												<td align="center"><%=VCAP_NM.elementAt(i) %></td>
												<td align="right"><%=VCAP_VAL.elementAt(i) %></td>
											</tr>
										<%} %>
									<%}else{%>
									<tr>
										<td colspan="4" align="center"><%=utilmsg.infoMessage("<b>No Data Found!</b>") %></td>
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

<div class="modal fade" id="capModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add Capacity Value
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Capacity Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="temp_cap_type">
										<option value="">--Select--</option>
										<%for(int i=0;i<VMST_CAP_TYPE.size();i++){ %>
										<option value="<%=VMST_CAP_TYPE.elementAt(i)%>"><%=VMST_CAP_TYPE_NM.elementAt(i)%> (<%=VMST_CAP_TYPE.elementAt(i)%>)</option>
										<%} %>
									</select>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Capacity Value<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="temp_cap_val" value="" onchange="chkNumber(this);">
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>					
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<label class="form-label"><b>Month/Year</b></label>
					  		</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="temp_cap_month">
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
									<%-- <script>document.forms[0].cap_month.value="<%=cap_month%>"</script> --%>
								</div>
								<div class="col">
					  				<select class="form-select form-select-sm" name="temp_cap_year">
					  					<%for(int i=(currentYear); i > filter_start_year;i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<%-- <script>document.forms[0].cap_year.value="<%=cap_year%>"</script> --%>
								</div>
							</div>
						</div>
					</div>
				</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="doClear();">
					<%if(write_access.equals("Y")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
					<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
					<%} %>
				</div>
      		</div>
      	</div>
	</div>
</div>
<input type="hidden" name="option" value="CAP_MASTER">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="temp_month" value="<%=currentMonth%>">
<input type="hidden" name="temp_year" value="<%=currentYear%>">
<input type="hidden" name="tmp_from_month" value="<%=month%>">
<input type="hidden" name="tmp_from_year" value="<%=year%>">
<input type="hidden" name="tmp_to_month" value="<%=month_to%>">
<input type="hidden" name="tmp_to_year" value="<%=year_to%>">

<input type="hidden" name="cap_month" value="">
<input type="hidden" name="cap_year" value="">
<input type="hidden" name="cap_type" value="">
<input type="hidden" name="cap_val" value="">

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