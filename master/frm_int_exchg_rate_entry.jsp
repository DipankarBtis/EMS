<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var rate_mode = document.forms[0].rate_mode.value;
	var prev_rate_mode = document.forms[0].prev_rate_mode.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var component = document.forms[0].component.value;
	var currency_from = document.forms[0].currency_from.value;
	var currency_to = document.forms[0].currency_to.value;
	var temp_currency_from = document.forms[0].temp_currency_from.value;
	var temp_currency_to = document.forms[0].temp_currency_to.value;
	
	if(prev_rate_mode != rate_mode)
	{
		component="0";
	}
	
	var msg="";
	var flag=true
	if(currency_from==currency_to)
	{
		//document.forms[0].currency_from.value=temp_currency_from
		//document.forms[0].currency_to.value=temp_currency_to
		msg="Exchange Currency can not be same!";
		flag=false;
	}
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "frm_int_exchg_rate_entry.jsp?rate_mode="+rate_mode+"&u="+u+"&month="+month+"&year="+year+"&component="+component+
				"&currency_from="+currency_from+"&currency_to="+currency_to;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg)
	}
}

function doSubmit()
{
	var rate_mode = document.forms[0].rate_mode.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var component = document.forms[0].component.value;
	var currency_from = document.forms[0].currency_from.value;
	var currency_to = document.forms[0].currency_to.value;
	
	var msg="";
	var flag=true;
	
	if(month=="0" || trim(month) == "")
	{
		msg+="Select Month!\n";
		flag=false;
	}
	if(year=="0" || trim(year) == "")
	{
		msg+="Select Year!\n";
		flag=false;
	}
	if(rate_mode=="0" || trim(rate_mode) == "")
	{
		msg+="Select Rate Mode!\n";
		flag=false;
	}
	if(component=="0" || trim(component) == "")
	{
		msg+="Select Rate Name!\n";
		flag=false;
	}
	
	if(rate_mode=="EXCHANGE")
	{
		if((currency_from=="0" || trim(currency_from) == "") || (currency_to=="0" || trim(currency_to) == ""))
		{
			msg+="Select Exchange!\n";
			flag=false;
		}
	}
	
	if(flag)
	{
		var a = confirm("Do you Want to Submit?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function exportToXls()
{
	var rate_mode = document.forms[0].rate_mode.value;
	var prev_rate_mode = document.forms[0].prev_rate_mode.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var component = document.forms[0].component.value;
	
	var url = "xls_int_exchg_rate_entry.jsp?fileName=Interest/Exchange Rate Entry.xls&rate_mode="+rate_mode+
	"&month="+month+"&year="+year+"&component="+component;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%

int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String rate_mode=request.getParameter("rate_mode")==null?"0":request.getParameter("rate_mode");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String component=request.getParameter("component")==null?"0":request.getParameter("component");
String currency_from=request.getParameter("currency_from")==null?"2":request.getParameter("currency_from");
String currency_to=request.getParameter("currency_to")==null?"1":request.getParameter("currency_to");

if(month.length() == 1)
{
	month="0"+month; 
}

dbmaster.setCallFlag("INTEREST_AND_EXCHANGE_RATE_ENTRY");
dbmaster.setRate_mode(rate_mode);
dbmaster.setMonth(month);
dbmaster.setYear(year);
dbmaster.setComponent(component);
dbmaster.setCurrency_from(currency_from);
dbmaster.setCurrency_to(currency_to);
dbmaster.init();

Vector VRATE_CD = dbmaster.getVRATE_CD();
Vector VRATE_NM = dbmaster.getVRATE_NM();

Vector VRATE_VALUE= dbmaster.getVRATE_VALUE();
Vector VTO_CURRENCY= dbmaster.getVTO_CURRENCY();
Vector VFROM_CURRENCY= dbmaster.getVFROM_CURRENCY();
Vector VRATE_REMARK= dbmaster.getVRATE_REMARK();
Vector VRATE_EFF_DT= dbmaster.getVRATE_EFF_DT();
Vector VCOLOR = dbmaster.getVCOLOR();

Vector VCURRENCY_CD= dbmaster.getVCURRENCY_CD();
Vector VCURRENCY_ABR= dbmaster.getVCURRENCY_ABR();

String component_flag = dbmaster.getComponent_flag();
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_master">

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
							Interest/ Exchange Rate Entry
						</div>
						<div class="row justify-content-end">
							<div class="col-auto" onclick="exportToXls();" style="color:green;">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
							</div>
							<div class="col-auto">
								<div class="btn-group">
									<select class="btn btn-outline-secondary btngrp <%if(!rate_mode.equals("0")){%>btnactive<%}%>" name="rate_mode" onchange="refresh();">
										<option value="0">-Select Rate Mode-</option>
										<option value="INTEREST">Interest Rate</option>
										<option value="EXCHANGE">Exchange Rate</option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<script>document.forms[0].rate_mode.value="<%=rate_mode%>"</script>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col">
									<label class="form-label"><b>Month/Year<span class="s-red">*</span></b></label>
					  			</div>
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
					  					<%for(int i=(currentYear+1); i > (currentYear-10);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
					  		</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Rate Name<span class="s-red">*</span></b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="component" onchange="refresh();">
										<option value="0">--Select--</option>
										<%for(int i=0; i < VRATE_CD.size();i++){ %>
											<option value="<%=VRATE_CD.elementAt(i)%>"><%=VRATE_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].component.value="<%=component%>"</script>
								</div>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3" <%if(!rate_mode.equals("EXCHANGE")){ %>style="display:none;"<%} %>>
							<div class="form-group row">
								<div class="col">
									<label class="form-label"><b>Exchange<span class="s-red">*</span></b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="currency_from" onchange="refresh();" style="pointer-events: none;">
										<%for(int j=0; j<VCURRENCY_CD.size(); j++){ %>
										<option value="<%=VCURRENCY_CD.elementAt(j)%>"><%=VCURRENCY_ABR.elementAt(j)%></option>
										<%} %>
									</select>
									<script>document.forms[0].currency_from.value="<%=currency_from%>"</script>
									<input type="hidden" name="temp_currency_from" value="<%=currency_from%>">
								</div>
								<div class="col-auto">
									<label class="form-label"><b>to</b></label>
					  			</div>
								<div class="col">
					  				<select class="form-select form-select-sm" name="currency_to" onchange="refresh();" style="pointer-events: none;">
										<%for(int j=0; j<VCURRENCY_CD.size(); j++){ %>
										<option value="<%=VCURRENCY_CD.elementAt(j)%>"><%=VCURRENCY_ABR.elementAt(j)%></option>
										<%} %>
									</select>
									<script>document.forms[0].currency_to.value="<%=currency_to%>"</script>
									<input type="hidden" name="temp_currency_to" value="<%=currency_to%>">
								</div>
					  		</div>
						</div>
					</div>
					<%if(!component.equals("0") && !component.equals("")){ %>
					<div class="row m-b-5">
						<label class="form-label subheader"></label>
					</div>
					<div class="row">
						<div class="col-md-2 col-sm-2 col-xs-2"></div>
						<div class="col-md-8 col-sm-8 col-xs-8">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
									<%if(rate_mode.equals("EXCHANGE")){ %>
										<tr>
											<th>Date</th>
											<th>Exchange Rate</th>
											<th>Remark</th>
										</tr>
									<%}else{ %>
										<tr>
											<th>Date</th>
											<th>Interest Rate (%)</th>
											<th>Remark</th>
										</tr>
									<%} %>
									</thead>
									<tbody>
									<%for(int i=0; i<VRATE_EFF_DT.size(); i++){ %>
										<tr>
											<td align="center">
												<div style="width:100px;">
													<div class="input-group input-group-sm">
								      					<input type="text" class="form-control form-control-sm date" name="rate_eff_dt" value="<%=VRATE_EFF_DT.elementAt(i)%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" readOnly>
								      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
								      				</div>
							      				</div> 
											</td>
											<%if(rate_mode.equals("EXCHANGE")){ %>
												<td align="center">
													<div style="width:200px;">
														<div class="row m-b-5">
															<div class="col-auto">
																1
															</div>
															<div class="col-auto">
																<%=VCURRENCY_ABR.elementAt(VCURRENCY_CD.indexOf(currency_from))%>
															</div>
															<div class="col-auto">
																=
															</div>
															<div class="col">
																<input type="text" class="form-control form-control-sm" name="rate_value" value="<%=VRATE_VALUE.elementAt(i)%>"  onblur="checkNumber1(this,6,4);" 
																style="text-align:right;background:<%=VCOLOR.elementAt(i)%>" <%if(component_flag.equals("Y")){ %>readOnly<%} %>>
															</div>
															<div class="col-auto">
																<%=VCURRENCY_ABR.elementAt(VCURRENCY_CD.indexOf(currency_to))%>
															</div>
														</div>
													</div>
												</td>
											<%}else{ %>
												<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" name="rate_value" value="<%=VRATE_VALUE.elementAt(i)%>"  onblur="checkNumber1(this,4,2);" style="text-align:right;background:<%=VCOLOR.elementAt(i)%>">
													</div>
												</td>
											<%} %>
											<td align="center">
												<div style="width:400px;">
													<textarea class="form-control form-control-sm"  name="remark" rows="1" maxlength="150" style="background:<%=VCOLOR.elementAt(i)%>"><%=VRATE_REMARK.elementAt(i)%></textarea>
												</div>
											</td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
						<div class="col-md-2 col-sm-2 col-xs-2"></div>
					</div>
					<%} %>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();" <%if(component_flag.equals("Y")){ %>disabled<%} %>>
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="INTEREST_AND_EXCHANGE_RATE_ENTRY">
<input type="hidden" name="prev_rate_mode" value="<%=rate_mode%>">

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