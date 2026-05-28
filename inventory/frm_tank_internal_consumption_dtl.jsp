<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import ="java.time.Month" %>
<%@page import ="java.text.DecimalFormat" %>
<%@page import ="java.text.NumberFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Harsh Maheta 20250303 : Form for Internal Consumption Details-->
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function doSubmit()
{
	var msg="";
	var flag=true;
	
	if(flag)
	{
		var a = confirm("Do you want to Insert the Internal Consumption Details?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else if(trim(msg).length>0)
	{
		alert(msg);
	}
}

function refresh()
{
	var year = document.forms[0].year.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_tank_internal_consumption_dtl.jsp?u="+u+"&year="+year;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function sumUp(obj,addUpId)
{
	var total = 0;
    var inputs = document.getElementsByName(obj);
    var isValid = true;

    for (var i = 0; i < inputs.length; i++)
    {
        var value = inputs[i].value.trim();
       
        if (value !== '')
        {
            if (!isNumeric(value))
            {
                isValid = false;
                alert("Please enter a valid number in all input fields!!");
                inputs[i].value="";
                break;
            }
           
            total += parseFloat(value);
        }
    }

    if (isValid) 
    {
    	document.getElementById(addUpId).value = total.toFixed(2);
    }
    else 
    {
        document.getElementById(addUpId).value = '';
    }
}
function sumRow(i)
{
	var total = 0;
	
    var lng_write_off = parseFloat(document.getElementById('lng_write_off'+i).value)|| 0;
    var flaring = parseFloat(document.getElementById('flaring'+i).value)|| 0;
    var auxilary_consumption = parseFloat(document.getElementById('auxilary_consumption'+i).value)|| 0;
    var scv_fuel_consumption = parseFloat(document.getElementById('scv_fuel_consumption'+i).value)|| 0;
    var sug = parseFloat(document.getElementById('sug'+i).value)|| 0;
    var other_consumption = parseFloat(document.getElementById('other_consumption'+i).value)|| 0;
    var mass_balancing = parseFloat(document.getElementById('mass_balancing'+i).value)|| 0;
    
    var isValid = true;

    total = parseFloat(lng_write_off + flaring + auxilary_consumption + scv_fuel_consumption + sug + other_consumption + mass_balancing);
    
    if(!isNumeric(total))
    {
    	isValid=false;
    }
    
    if (isValid) 
    {
    	document.getElementById('total_consumption'+i).value = total.toFixed(2);
    }
    else 
    {
        document.getElementById('total_consumption'+i).value = '';
    }
}
function isNumeric(value) 
{
    return !isNaN(parseFloat(value)) && isFinite(value);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_TankTerminal" id="dbterminal" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysDt=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String year = request.getParameter("year")==null?""+currentYear:request.getParameter("year");

dbterminal.setCallFlag("INTERNAL_CONSUMPTION_DTL");
dbterminal.setComp_cd(owner_cd);
dbterminal.setYear(year);
dbterminal.init();

Vector VLNG_WRITE_OFF = dbterminal.getVLNG_WRITE_OFF();
Vector VFLARING = dbterminal.getVFLARING();
Vector VAUXILARY_CONSUMPTION = dbterminal.getVAUXILARY_CONSUMPTION();
Vector VSCV_FUEL_CONSUMPTION = dbterminal.getVSCV_FUEL_CONSUMPTION();
Vector VSUG = dbterminal.getVSUG();
Vector VOTHER_CONSUMPTION = dbterminal.getVOTHER_CONSUMPTION();
Vector VMASS_BALANCING = dbterminal.getVMASS_BALANCING();
Vector VTOTAL_CONSUMPTION = dbterminal.getVTOTAL_CONSUMPTION();

Vector VMONTH_LIST = new Vector();
Vector VMONTH_NO = new Vector();

int monthNo=0;
for (Month month : Month.values())
{
	++monthNo;
	VMONTH_NO.add(monthNo);
	VMONTH_LIST.add(month);
}
int filter_start_year = CommonVariable.filter_start_year;
NumberFormat nf = new DecimalFormat("###########0.00");

double sum_lng_write_off=0.00;
double sum_flaring=0.00;
double sum_auxilary_consumption=0.00;
double sum_scv_fuel_consumption=0.00;
double sum_sug=0.00;
double sum_other_consumption=0.00;
double sum_mass_balancing=0.00;
double sum_total_consumption=0.00;

for (int i = 0; i < VLNG_WRITE_OFF.size(); i++)
{
	String numStr = ""+VLNG_WRITE_OFF.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_lng_write_off += num;
}
for (int i = 0; i < VFLARING.size(); i++)
{
	String numStr = ""+VFLARING.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_flaring += num;
}
for (int i = 0; i < VAUXILARY_CONSUMPTION.size(); i++)
{
	String numStr = ""+VAUXILARY_CONSUMPTION.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_auxilary_consumption += num;
}
for (int i = 0; i < VSCV_FUEL_CONSUMPTION.size(); i++)
{
	String numStr = ""+VSCV_FUEL_CONSUMPTION.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_scv_fuel_consumption += num;
}
for (int i = 0; i < VSUG.size(); i++)
{
	String numStr = ""+VSUG.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_sug += num;
}
for (int i = 0; i < VOTHER_CONSUMPTION.size(); i++)
{
	String numStr = ""+VOTHER_CONSUMPTION.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_other_consumption += num;
}
for (int i = 0; i < VMASS_BALANCING.size(); i++)
{
	String numStr = ""+VMASS_BALANCING.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_mass_balancing += num;
}
for (int i = 0; i < VTOTAL_CONSUMPTION.size(); i++)
{
	String numStr = ""+VTOTAL_CONSUMPTION.elementAt(i);
	if(numStr.isEmpty() || numStr.equals(""))
	{
		numStr="0.00";
	}
	double num = Double.parseDouble(numStr);
	sum_total_consumption += num;
}
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_TankTerminal">
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
				    		Internal Consumption Details
	   	 				</div>
					 	<a href="../inventory/xls_tank_internal_consumption_dtl.jsp?fileName=Internal Consumption Details.xls&year=<%=year%>" download="Tank Details">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div align="center">
							<div class="col-sm-3 col-xs-3 col-md-3">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>Year</b></label>
						  			</div>
									<div class="col">
						  				<select class="form-select form-select-sm" name="year" onchange="refresh();">
						  					<%-- <%for(int i=(currentYear+1); i > (currentYear-10);i--){ %> --%>
						  					<%for(int i=(currentYear+1); i >filter_start_year;i--){ %>
												<option value="<%=i%>"><%=i%></option>
											<%} %>
										</select>
										<script>document.forms[0].year.value="<%=year%>"</script>
									</div>
						  		</div>
						  	</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr>
											<th>Month</th>
											<th>LNG Write Off<br>(MMBTU)</th>
											<th>Flaring</th>
											<th>Power Fuel Consumption<br>(MMBTU)</th>
											<th>SCV Fuel Consumption<br>(MMBTU)</th>
											<th>SUG<br>(MMBTU)</th>
											<th>Other Consumption<br>(MMBTU)</th>
											<th>Unaccounted Losses<br>(MMBTU)</th>
											<th>Total Consumption<br>(MMBTU)</th>
										</tr>
									</thead>
									<tbody id="mainTbody">
										<%for(int i=0; i<VMONTH_LIST.size();i++){ %>
										<tr>
											<td align="center">
												<b><%=VMONTH_LIST.elementAt(i) %> </b>
												<input type="hidden" name="month_no" id="month_no<%=i%>" value="<%=VMONTH_NO.elementAt(i)%> ">
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="lng_write_off" id="lng_write_off<%=i %>" value="<%=VLNG_WRITE_OFF.elementAt(i) %>" <%if(!VLNG_WRITE_OFF.elementAt(i).equals("")){ %>style="background:#8eecbd;text-align:right"<%} %> onblur="checkNumber1(this,9,2);negNumber(this);" onchange="sumUp('lng_write_off','sum_lng_write_off');sumRow(<%=i%>);">
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="flaring" id="flaring<%=i %>" value="<%=VFLARING.elementAt(i) %>" <%if(!VFLARING.elementAt(i).equals("")){ %>style="background:#8eecbd;text-align:right"<%} %>  onblur="checkNumber1(this,9,2);negNumber(this);" onchange="sumUp('flaring','sum_flaring');sumRow(<%=i%>);">
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="auxilary_consumption" id="auxilary_consumption<%=i %>" value="<%=VAUXILARY_CONSUMPTION.elementAt(i) %>" <%if(!VAUXILARY_CONSUMPTION.elementAt(i).equals("")){ %>style="background:#8eecbd;text-align:right"<%} %> onblur="checkNumber1(this,9,2);negNumber(this);" onchange="sumUp('auxilary_consumption','sum_auxilary_consumption');sumRow(<%=i%>);">
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="scv_fuel_consumption" id="scv_fuel_consumption<%=i %>" value="<%=VSCV_FUEL_CONSUMPTION.elementAt(i) %>" <%if(!VSCV_FUEL_CONSUMPTION.elementAt(i).equals("")){ %>style="background:#8eecbd;text-align:right"<%} %> onblur="checkNumber1(this,9,2);negNumber(this);" onchange="sumUp('scv_fuel_consumption','sum_scv_fuel_consumption');sumRow(<%=i%>);">
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="sug" id="sug<%=i %>" value="<%=VSUG.elementAt(i) %>" <%if(!VSUG.elementAt(i).equals("")){ %>style="background:#8eecbd;text-align:right"<%} %> onblur="checkNumber1(this,9,2);negNumber(this);" onchange="sumUp('sug','sum_sug');sumRow(<%=i%>);">
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="other_consumption" id="other_consumption<%=i %>" value="<%=VOTHER_CONSUMPTION.elementAt(i) %>" <%if(!VOTHER_CONSUMPTION.elementAt(i).equals("")){ %>style="background:#8eecbd;text-align:right"<%} %> onblur="checkNumber1(this,9,2);negNumber(this);"onchange="sumUp('other_consumption','sum_other_consumption');sumRow(<%=i%>);">
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="mass_balancing" id="mass_balancing<%=i %>" value="<%=VMASS_BALANCING.elementAt(i) %>" <%if(!VMASS_BALANCING.elementAt(i).equals("")){ %>style="background:#8eecbd;text-align:right"<%} %> onblur="checkNumber1(this,9,2);negNumber(this);" onchange="sumUp('mass_balancing','sum_mass_balancing');sumRow(<%=i%>);">
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="total_consumption" id="total_consumption<%=i %>" value="<%=VTOTAL_CONSUMPTION.elementAt(i) %>" style="text-align:right" onblur="checkNumber1(this,9,2);negNumber(this);" onchange="sumUp('total_consumption','sum_total_consumption');" readonly>
												    	</div>
										  			</div>
												</div>
											</td>
										</tr>
										<%} %>
										<tr>
											<td>
												<b>Sum of Total :</b>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input style="text-align:right;" type="text" class="form-control form-control-sm" name="sum_lng_write_off" id="sum_lng_write_off" value="<%=nf.format(sum_lng_write_off) %>" readonly>
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input style="text-align:right;" type="text" class="form-control form-control-sm" name="sum_flaring" id="sum_flaring" value="<%=nf.format(sum_flaring) %>" readonly>
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input style="text-align:right;" type="text" class="form-control form-control-sm" name="sum_auxilary_consumption" id="sum_auxilary_consumption" value="<%=nf.format(sum_auxilary_consumption) %>" readonly>
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input style="text-align:right;" type="text" class="form-control form-control-sm" name="sum_scv_fuel_consumption" id="sum_scv_fuel_consumption" value="<%=nf.format(sum_scv_fuel_consumption) %>" readonly>
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input style="text-align:right;" type="text" class="form-control form-control-sm" name="sum_sug" id="sum_sug" value="<%=nf.format(sum_sug) %>" readonly>
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input style="text-align:right;" type="text" class="form-control form-control-sm" name="sum_other_consumption" id="sum_other_consumption" value="<%=nf.format(sum_other_consumption) %>" readonly>
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input style="text-align:right;" type="text" class="form-control form-control-sm" name="sum_mass_balancing" id="sum_mass_balancing" value="<%=nf.format(sum_mass_balancing) %>" readonly>
												    	</div>
										  			</div>
												</div>
											</td>
											<td align="center">
												<div class="col-sm-8 col-xs-8 col-md-8">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input style="text-align:right;" type="text" class="form-control form-control-sm" name="sum_total_consumption" id="sum_total_consumption" value="<%=nf.format(sum_total_consumption) %>" readonly>
												    	</div>
										  			</div>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer cdfooter">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
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
</div>

<input type="hidden" name="option" value="INTERNAL_CONSUMPTION_DTL">
<input type="hidden" name="opration" value="INSERT">

<input type="hidden" name="prev_display" value="">
<input type="hidden" name="prev_display1" value="">

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
<input type="hidden" name="validation" value="">

<script type="text/javascript">
function isExistNM()
{
	var opration = document.forms[0].opration.value;
	
	var tank_cd ="0";
	if(opration == "MODIFY")
	{
		tank_cd = document.forms[0].tank_cd.value;
	}
	var tank_nm = document.forms[0].tank_name.value;
	
	var info="";
	
	$.post("../servlet/DB_Inventory_Ajax?setCallType=IsExistNM&tank_cd="+tank_cd+"&tank_nm="+tank_nm+
			"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.TANK_DTL, function(index_1, json_1) {
				if(parseInt(json_1.NAME) > 0)
				{
					info+="Tank Name already Exist!\n";
				}
				if(info!="")
				{
					alert(info)
					document.forms[0].tank_name.value="";
				}
			});
		});
	});
	
	return info;
}

function isExistPiTag()
{
	var opration = document.forms[0].opration.value;
	
	var tank_cd ="0";
	if(opration == "MODIFY")
	{
		tank_cd = document.forms[0].tank_cd.value;
	}
	var tank_pi_tag = document.forms[0].tank_pi_tag.value;
	
	var info="";
	
	$.post("../servlet/DB_Inventory_Ajax?setCallType=IsExistPitag&tank_cd="+tank_cd+
			"&tank_pi_tag="+tank_pi_tag+"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.TANK_DTL, function(index_1, json_1) {
				if(parseInt(json_1.PI_TAG) > 0)
				{
					info+="Pi Tag already Exist!";
				}
				
				if(info!="")
				{
					alert(info)
					document.forms[0].tank_pi_tag.value="";
				}
			});
		});
	});
}
</script>
</form>
</body>
</html>