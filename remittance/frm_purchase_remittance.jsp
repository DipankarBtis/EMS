<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var billing_cycle = document.forms[0].billing_cycle.value;
	var prev_billing_cycle = document.forms[0].prev_billing_cycle.value;
	
	var period_start_dt="";
	var period_end_dt="";
	
	var msg="";
	var flag = true;
	if(prev_billing_cycle == billing_cycle)
	{
		if(billing_cycle == "8")
		{
			period_start_dt=document.forms[0].period_start_dt.value;
			period_end_dt=document.forms[0].period_end_dt.value;
			
			if(trim(period_start_dt)=="" || trim(period_end_dt)=="")
			{
				msg="Enter Billing Period!";
				flag=false;
			}
		}
	}
	
	var read_access = document.forms[0].read_access.value;
	var write_access = document.forms[0].write_access.value;
	var check_access = document.forms[0].check_access.value;
	var print_access = document.forms[0].print_access.value;
	var delete_access = document.forms[0].delete_access.value;
	var audit_access = document.forms[0].audit_access.value;
	var authorize_access = document.forms[0].authorize_access.value;
	var approve_access = document.forms[0].approve_access.value;
	var execute_access = document.forms[0].execute_access.value;
	var formCd = document.forms[0].form_cd.value;
	var formNm = document.forms[0].form_nm.value;
	var mod_cd = document.forms[0].mod_cd.value;
	var mod_nm = document.forms[0].mod_nm.value;
	
	if(flag)
	{
		var url = "frm_purchase_remittance.jsp?&form_cd="+formCd+"&form_nm="+formNm+
				"&mod_cd="+mod_cd+"&mod_nm="+mod_nm+"&month="+month+"&year="+year+"&billing_cycle="+billing_cycle+
				"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+
				"&read_access="+read_access+"&write_access="+write_access+"&check_access="+check_access+
				"&print_access="+print_access+"&delete_access="+delete_access+"&audit_access="+audit_access+
				"&authorize_access="+authorize_access+"&approve_access="+approve_access+"&execute_access="+execute_access;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg)
	}
}

function checkStartEndDate(flag)
{
	var period_start_dt=document.forms[0].period_start_dt;
	var period_end_dt=document.forms[0].period_end_dt;
	
	var obj = period_start_dt;
	var obj1 = period_end_dt;
	
	if((obj.value!="" && trim(obj.value) != "" && obj.value != null) && (obj1.value!="" && trim(obj1.value) != "" && obj1.value != null))
	{
		if(flag=="F")
		{
			var count = compareDate(obj.value,obj1.value);
			if(parseInt(count) == 1)
			{
				alert("Billing Period Start Date should be less or equal Period End Date!")
				obj.value="";
				return false;
			}
		}
		else if(flag=="T")
		{
			var count = compareDate(obj.value,obj1.value);
			if(parseInt(count) == 1)
			{
				alert("Billing Period End Date should be grater or equal Period Start Date!")
				obj1.value="";
				return false;
			}
		}
	}
}


var newWindow;
function openActivity1(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,period_st_dt,period_end_dt)
{
	var billing_cycle = document.forms[0].billing_cycle.value;
	
	var read_access = document.forms[0].read_access.value;
	var write_access = document.forms[0].write_access.value;
	var check_access = document.forms[0].check_access.value;
	var print_access = document.forms[0].print_access.value;
	var delete_access = document.forms[0].delete_access.value;
	var audit_access = document.forms[0].audit_access.value;
	var authorize_access = document.forms[0].authorize_access.value;
	var approve_access = document.forms[0].approve_access.value;
	var execute_access = document.forms[0].execute_access.value;
	var formCd = document.forms[0].form_cd.value;
	var formNm = document.forms[0].form_nm.value;
	var mod_cd = document.forms[0].mod_cd.value;
	var mod_nm = document.forms[0].mod_nm.value;
	
	var url = "frm_prepare_seller_payment.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
			"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+
			"&form_cd="+formCd+"&form_nm="+formNm+"&mod_cd="+mod_cd+"&mod_nm="+mod_nm+
			"&read_access="+read_access+"&write_access="+write_access+"&check_access="+check_access+
			"&print_access="+print_access+"&delete_access="+delete_access+"&audit_access="+audit_access+
			"&authorize_access="+authorize_access+"&approve_access="+approve_access+"&execute_access="+execute_access;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare Seller Payment","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare Seller Payment","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function openActivity2(counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_type,plant_seq,period_st_dt,period_end_dt)
{
	var billing_cycle = document.forms[0].billing_cycle.value;
	
	var read_access = document.forms[0].read_access.value;
	var write_access = document.forms[0].write_access.value;
	var check_access = document.forms[0].check_access.value;
	var print_access = document.forms[0].print_access.value;
	var delete_access = document.forms[0].delete_access.value;
	var audit_access = document.forms[0].audit_access.value;
	var authorize_access = document.forms[0].authorize_access.value;
	var approve_access = document.forms[0].approve_access.value;
	var execute_access = document.forms[0].execute_access.value;
	var formCd = document.forms[0].form_cd.value;
	var formNm = document.forms[0].form_nm.value;
	var mod_cd = document.forms[0].mod_cd.value;
	var mod_nm = document.forms[0].mod_nm.value;
	
	var url = "frm_prepare_seller_dr_cr.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+"&cont_no="+cont_no+
		"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_st_dt+
		"&period_end_dt="+period_end_dt+"&billing_cycle="+billing_cycle+
		"&form_cd="+formCd+"&form_nm="+formNm+"&mod_cd="+mod_cd+"&mod_nm="+mod_nm+
		"&read_access="+read_access+"&write_access="+write_access+"&check_access="+check_access+
		"&print_access="+print_access+"&delete_access="+delete_access+"&audit_access="+audit_access+
		"&authorize_access="+authorize_access+"&approve_access="+approve_access+"&execute_access="+execute_access;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare Seller CR DR","top=10,left=10,width=1300,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare Seller CR DR","top=10,left=10,width=1300,height=600,scrollbars=1");
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.remittance.DataBean_Remittance" id="remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String read_access=request.getParameter("read_access")==null?"N":request.getParameter("read_access");
String write_access=request.getParameter("write_access")==null?"N":request.getParameter("write_access");
String check_access=request.getParameter("check_access")==null?"N":request.getParameter("check_access");
String print_access=request.getParameter("print_access")==null?"N":request.getParameter("print_access");
String delete_access=request.getParameter("delete_access")==null?"N":request.getParameter("delete_access");
String audit_access=request.getParameter("audit_access")==null?"N":request.getParameter("audit_access");
String authorize_access=request.getParameter("authorize_access")==null?"N":request.getParameter("authorize_access");
String approve_access=request.getParameter("approve_access")==null?"N":request.getParameter("approve_access");
String execute_access=request.getParameter("execute_access")==null?"N":request.getParameter("execute_access");
String formCd=request.getParameter("form_cd")==null?"0":request.getParameter("form_cd");
String formNm=request.getParameter("form_nm")==null?"":request.getParameter("form_nm");
String mod_cd=request.getParameter("mod_cd")==null?"0":request.getParameter("mod_cd");
String mod_nm=request.getParameter("mod_nm")==null?"":request.getParameter("mod_nm");

String msg=request.getParameter("msg")==null?"":request.getParameter("msg");
String msg_type=request.getParameter("msg_type")==null?"":request.getParameter("msg_type");

String owner_cd=""+session.getAttribute("comp_cd")==null?"":""+session.getAttribute("comp_cd");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
if(billing_cycle.equals(""))
{
	if(Integer.parseInt(date_num) > 15)
	{
		billing_cycle="2";
	}
	else
	{
		billing_cycle="1";
	}
}

String period_start_dt=request.getParameter("period_start_dt")==null?sysdate:request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?sysdate:request.getParameter("period_end_dt");
if(period_start_dt.equals(""))
{
	period_start_dt=sysdate;
}
if(period_end_dt.equals(""))
{
	period_end_dt=sysdate;
}

remittance.setCallFlag("REMITTANCE_PREPARATION_LIST");
remittance.setComp_cd(owner_cd);
remittance.setMonth(month);
remittance.setYear(year);
remittance.setBilling_cycle(billing_cycle);
if(billing_cycle.equals("8"))
{
	remittance.setPeriod_start_dt(period_start_dt);
	remittance.setPeriod_end_dt(period_end_dt);
}
remittance.init();

Vector VCOUNTERPTY_CD = remittance.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = remittance.getVCOUNTERPTY_ABBR();
Vector VCONT_NO = remittance.getVCONT_NO();
Vector VCONT_REV_NO = remittance.getVCONT_REV_NO();
Vector VAGMT_NO = remittance.getVAGMT_NO();
Vector VAGMT_REV_NO = remittance.getVAGMT_REV_NO();
Vector VSTART_DT = remittance.getVSTART_DT();
Vector VEND_DT = remittance.getVEND_DT();
Vector VCONT_NAME = remittance.getVCONT_NAME();
Vector VCONT_REF_NO = remittance.getVCONT_REF_NO();
Vector VCONTRACT_TYPE = remittance.getVCONTRACT_TYPE();
Vector VPLANT_SEQ = remittance.getVPLANT_SEQ();
Vector VPLANT_ABBR = remittance.getVPLANT_ABBR();
Vector VDEAL_NO = remittance.getVDEAL_NO();
Vector VPERIOD_START_DT = remittance.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = remittance.getVPERIOD_END_DT();
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
					    	Remittance Preparation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<%if(billing_cycle.equals("8")){ %>
						<div class="col-sm-1 col-xs-1 col-md-1"></div>
						<%}else{ %>
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
						<%} %>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Month/Year</b></label>
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
									<label class="form-label"><b>Billing Cycle</b></label>
					  			</div>
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="billing_cycle" onchange="refresh();">
					  					<option value="1">1st-Fortnight</option>
										<option value="2">2nd-Fortnight</option>
										<option value="3">1st-Weekly</option>
										<option value="4">2nd-Weekly</option>
										<option value="5">3rd-Weekly</option>
										<option value="6">4th-Weekly</option>
										<option value="9">5th-Weekly</option>
										<option value="7">Monthly</option>
										<option value="8">Other</option>
					  				</select>
					  				<script>document.forms[0].billing_cycle.value="<%=billing_cycle%>"</script>
					  			</div>
					  		</div>
					  	</div>
					  	<%if(billing_cycle.equals("8")){ %>
					  	<div class="col-sm-4 col-xs-4 col-md-4">
					  		<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Billing Period</b></label>
					  			</div>
					  			<div class="col">
					  				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="period_start_dt" value="<%=period_start_dt%>" maxLength="10" 
			      						onchange="validateDate(this);checkStartEndDate('F');refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      					</div>
					  			<div class="col">
					  				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="period_end_dt" value="<%=period_end_dt%>" maxLength="10" 
			      						onchange="validateDate(this);checkStartEndDate('T');refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
					  			</div>
					  		</div>
					  	</div>
					  	<div class="col-sm-1 col-xs-1 col-md-1"></div>
					  	<%}else{ %>
					  	<div class="col-sm-3 col-xs-3 col-md-3"></div>
					  	<%} %>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Purchase Payment/ Remittance Generation</label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th>Trader</th>
											<th>Contract No</th>
											<th>Plant</th>
											<th>Start - End Date</th>
											<th>Billing Period</th>
											<th>Activity-I<br>Seller Invoice</th>
											<th>Activity-II<br>CR DR Note</th>
											<th>Activity-III<br>Invoice Payment/ Remittance</th>
										</tr>
									</thead>
									<tbody>
									<%if(VCOUNTERPTY_CD.size() > 0){ %>
										<%for(int i=0; i<VCOUNTERPTY_CD.size(); i++){ %>
										<tr>
											<td align="center">
												<%=VCOUNTERPTY_ABBR.elementAt(i)%>
											</td>
											<td align="center">
												<font color="blue"><%=VDEAL_NO.elementAt(i)%></font> [<%=VCONT_REF_NO.elementAt(i)%>]
											</td>
											<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
											<td align="center">
												<input type="button" class="btn btn-warning com-btn" value="Generate" 
												onclick="openActivity1('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
												'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>')">
											</td>
											<td align="center">
												<input type="button" class="btn btn-warning com-btn" value="Generate" 
												onclick="openActivity2('<%=VCOUNTERPTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
												'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VPLANT_SEQ.elementAt(i)%>','<%=VPERIOD_START_DT.elementAt(i)%>','<%=VPERIOD_END_DT.elementAt(i)%>')">
											</td>
											<td align="center">
												<input type="button" class="btn btn-warning com-btn" value="Prepare">
											</td>
										</tr>
										<%} %>
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
</div>

<input type="hidden" name="prev_billing_cycle" value="<%=billing_cycle%>">
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

</form>
</body>
</html>