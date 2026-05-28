<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
	var tmp_year_to = document.forms[0].tmp_to_year.value;
	
	var flag=checkMonthYearRange(document.forms[0].month,document.forms[0].year,document.forms[0].month_to,document.forms[0].year_to);
	var u = document.forms[0].u.value;
	
	if(flag==true)
	{
		var url = "rpt_inventory_stock.jsp?u="+u+"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to;
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
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var month_to = document.forms[0].month_to.value;
	var year_to = document.forms[0].year_to.value;
    var owner_abbr = document.forms[0].owner_abbr.value;
    var fileName = owner_abbr+"_Stock_Inventory- "+month+"/"+year+" _To_ "+month_to+"/"+year_to+".xls";
    
    var url  = "xls_inventory_stock.jsp?fileName="+fileName+"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to;
    location.replace(url);
}
</script>

<jsp:useBean class="com.etrm.fms.inventory.DataBean_TankTerminal" id="inventory" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<% 
String sysdate = utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();
int filter_start_year = CommonVariable.filter_start_year;

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
String from_dt  = "01"+"/"+month+"/"+year;
String last_dt = utildate.getLastDateOfMonth(month_to, year_to);
String to_dt  = last_dt;

inventory.setCallFlag("INV_STOCK_DTL");
inventory.setComp_cd(owner_cd);
inventory.setFrom_Dt(from_dt);
inventory.setTo_Dt(to_dt);
inventory.init();

Vector VCARGO_NO = inventory.getVCARGO_NO();
Vector VEFF_DT = inventory.getVEFF_DT();
Vector VTANK_VOLUME = inventory.getVTANK_VOLUME();
Vector VUNLOADED_QTY = inventory.getVUNLOADED_QTY();
Vector VTANK_MMBTU = inventory.getVTANK_MMBTU();
Vector VBALANCE_QTY = inventory.getVBALANCE_QTY();
Vector VSEL_BALANCE_QTY = inventory.getVSEL_BALANCE_QTY();
%>
</head>

<body>
<%@ include file="../home/header.jsp"%>
<form>
<div class="box-body">
  <div class="row">
    <div class="col-md-12 col-sm-12 col-xs-12">
      <div class="card cardmain">
         <div class="card-header cdheader">
			<div class="d-flex  justify-content-between">
			   <div class="topheader">Inventory Stock</div>
				  <a>
					<span class="input-group-text">
					 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
					</span>
				  </a>
			</div>
		  </div>
    	  <div class="card-body cdbody">
			<div class="row">
			 <div class="col-sm-3 col-xs-3 col-md-3"></div>
				<div class="col-auto">
					<div class="form-group row">
						<label class="form-label"><b>Month/Year</b></label>
			  		</div>
				</div>
				<div class="col-sm-2 col-xs-2 col-md-2">
				  <div class="form-group row">
		  			<div class="col">
		  				<select class="form-select form-select-sm" name="month" onchange="">
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
		  				<select class="form-select form-select-sm" name="year" onchange="">
		  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
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
		  				<select class="form-select form-select-sm" name="month_to" onchange="">
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
		  				<select class="form-select form-select-sm" name="year_to" onchange="">
		  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
								<option value="<%=i%>"><%=i%></option>
							<%} %>
						</select>
						<script>document.forms[0].year_to.value="<%=year_to%>"</script>
					</div>
				 </div>
		  		</div>
	  		    
                <div class="col-auto">
			       <input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
		        </div>
				<div class="col-sm-3 col-xs-3 col-md-3"></div>
			</div>
		</div> 
		<div class="card-body cdbody">
		  <div class="row">
			<div class="col-sm-12 col-xs-12 col-md-12">&nbsp;</div>
		  </div>
		  <div class="col-md-12 col-sm-12 col-xs-12">
			<div class="row">
			 <div class="table-responsive">
			<table class="table table-bordered table-hover" id="filterbysearch" >
				<thead>
					<tr>
					<th>SR.No#</th>
					<th>Date</th>
					<th>Tank Cap.</th>
					<th>Dead Stock</th>
					<th>Eff.Stock</th>
					<th>TP Obligation</th>
					<th>SEIPL-Stock(MMBTU)</th>
					</tr>
				</thead>
				<tbody>
					<%for(int i=0;i<VEFF_DT.size();i++){ %>
					<tr>
						<td align="center"><%=i+1 %></td>
						<td align="center"><%=VEFF_DT.elementAt(i)%></td>
						<td align="right"><%=VTANK_VOLUME.elementAt(i) %></td>
						<td align="right"><%=VUNLOADED_QTY.elementAt(i) %></td>
						<td align="right"><%=VTANK_MMBTU.elementAt(i) %></td>
						<td align="right"><%=VBALANCE_QTY.elementAt(i) %></td>
						<td align="right"><%=VSEL_BALANCE_QTY.elementAt(i) %></td>
					</tr>
					<% } %>
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
<input type="hidden" name="owner_abbr" value="<%=owner_abbr%>">
<input type="hidden" name="temp_from_dt" value="<%=from_dt%>">
<input type="hidden" name="temp_to_dt" value="<%=to_dt%>">

</form>
</body>
</html>