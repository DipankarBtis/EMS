<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
/* function refresh() 
{
    var from_dt = document.forms[0].from_dt.value;
    var to_dt = document.forms[0].to_dt.value;
    var temp_from_dt = document.forms[0].temp_from_dt.value;
    var temp_to_dt = document.forms[0].temp_to_dt.value;
    var cargo_type = document.forms[0].cargo_type.value;
    
    
    var flag = true;
    var msg = "";
    
    if(cargo_type == "Select")
    {
    	alert("Please Select Cargo Type ");
    	flag = false;
    }
     var flag=checkDateRangeOnApply(document.forms[0].from_dt,document.forms[0].to_dt);
     var u = document.forms[0].u.value;

    if(flag)
 	{
 		if(trim(from_dt)!="" && trim(to_dt)!="")
 		{
 	    if (flag==true) 
       {
       	var url = "rpt_edq_adq_reconcilation.jsp?u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt+"&cargo_type="+cargo_type;
           document.getElementById("loading").style.visibility = "visible";
           location.replace(url);
       }
 		}
 	}
} */

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
	var cargo_type = document.forms[0].cargo_type.value;
	
	if(cargo_type == "Select")
    {
    	alert("Please Select Cargo Type ");
    	flag = false;
    }
	var flag=checkMonthYearRange(document.forms[0].month,document.forms[0].year,document.forms[0].month_to,document.forms[0].year_to);
	var u = document.forms[0].u.value;
	
	if(flag==true)
	{
		var url = "rpt_edq_adq_reconcilation.jsp?u="+u+"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to+"&cargo_type="+cargo_type;
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

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch");
  	
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

function exportToXls()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var month_to = document.forms[0].month_to.value;
	var year_to = document.forms[0].year_to.value;
    var owner_abbr = document.forms[0].owner_abbr.value;
    var cargo_type = document.forms[0].cargo_type.value;
    var cargo_typ;
   if(cargo_type == "S")
    {
	   cargo_typ = "Service"
    }
    else
   	{
    	cargo_typ = "Commodity"
   	} 
    var fileName = owner_abbr+"_EDQ-ADQ Reconciliation- "+month+"/"+year+" _To_ "+month_to+"/"+year_to+"-Cargo_type:- "+cargo_typ+".xls";
    
    var url  = "xls_edq_adq_reoncilation.jsp?fileName="+fileName+"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to+"&cargo_type="+cargo_type;
    location.replace(url);
}
</script>

<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_MGMT_Reports" id="mgmt_reports" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<% 
/* String sysdate  = utildate.getSysdate();
String firstDate="01"+sysdate.substring(2, sysdate.length());

String from_dt = request.getParameter("from_dt")==null?""+firstDate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?""+sysdate:request.getParameter("to_dt");
String cargo_type = request.getParameter("cargo_type")==null?"P":request.getParameter("cargo_type"); */


String sysdate = utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();
int filter_start_year = CommonVariable.filter_start_year;

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String month_to=request.getParameter("month_to")==null?""+currentMonth:request.getParameter("month_to");
String year_to=request.getParameter("year_to")==null?""+currentYear:request.getParameter("year_to");
String cargo_type = request.getParameter("cargo_type")==null?"P":request.getParameter("cargo_type");

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

mgmt_reports.setCallFlag("EDQ_ADQ_RECONCILATION");
mgmt_reports.setComp_cd(owner_cd);
mgmt_reports.setFrom_dt(from_dt);
mgmt_reports.setTo_dt(to_dt);
mgmt_reports.setCargo_type(cargo_type);
mgmt_reports.init();

Vector VMST_COUNTERPARTY_CD = mgmt_reports.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = mgmt_reports.getVMST_COUNTERPARTY_ABBR();
Vector VBOE_QTY = mgmt_reports.getVBOE_QTY();
Vector VBOE_NO = mgmt_reports.getVBOE_NO();
Vector VBOE_DT = mgmt_reports.getVBOE_DT();
Vector VARRIVAL_DT = mgmt_reports.getVARRIVAL_DT();
Vector VEDQ_QTY = mgmt_reports.getVEDQ_QTY();
Vector VADQ_QTY = mgmt_reports.getVADQ_QTY();
Vector VCARGO_REF = mgmt_reports.getVCARGO_REF();
Vector VSHIP_NAME = mgmt_reports.getVSHIP_NAME();
Vector VDIFF_MMBTU = mgmt_reports.getVDIFF_MMBTU();
Vector VALLOCATED_QTY = mgmt_reports.getVALLOCATED_QTY();
Vector VCUST_ABBR_SN = mgmt_reports.getVCUST_ABBR_SN();
Vector VCARGO_NO = mgmt_reports.getVCARGO_NO();
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
			   <div class="topheader">EDQ-ADQ Reconciliation</div>
				  <a>
					<span class="input-group-text">
					 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
					</span>
				  </a>
			</div>
		  </div>
    	  <div class="card-body cdbody">
			<div class="row">
			 <div class="col-sm-2 col-xs-2 col-md-2"></div>
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
			       <label class="form-label"><b>Cargo Type </b></label>
                </div>
		        <div class="col">
				      <select class="form-select form-select-sm" name="cargo_type" onchange="">
					    <option value="P">Commodity</option>
					    <option value="S">Service</option>
		              </select>
		              <script>document.forms[0].cargo_type.value="<%=cargo_type%>"</script>
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
						<th rowspan="2">Sr.No.</th>
						<th rowspan="2">Cargo No<div align="center"><input class="form-control form-control-sm" type="text" id="cargo_no" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"></div></th>
						<th rowspan="2">Cargo_ref#<div align="center"><input class="form-control form-control-sm" type="text" id="cargo_ref" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"></div></th>
						<th rowspan="2">Vessel Name<div align="center"><input class="form-control form-control-sm" type="text" id="vessel_nm" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"></div></th>
						<th rowspan="2">EDQ<br>(MMBTU)</th>
						<th rowspan="2">ADQ<br>(MMBTU)</th>
						<th rowspan="2">Difference<br>(MMBTU)</th>
						<th colspan="2">Cargo Allocated To Which Customer</th>
						<th rowspan="2">BOE NO</th>
						<th rowspan="2">BOE QTY <br>(MMBTU)</th>
						<th rowspan="2">BOE DATE</th>
						<th rowspan="2">Cargo <br>Arrival Date</th>
					</tr>
					
					<tr>
						<th colspan="1">CUSTOMER(Contract#)</th>
						<th colspan="1">ALLOCATED QTY<br>(MMBTU)</th>
					</tr>
				</thead>
				<tbody>
					<%int k=0;
						if(VMST_COUNTERPARTY_CD.size() > 0){ %>
							<%for(int i=0;i<VMST_COUNTERPARTY_CD.size(); i++){ 
							
								k+=1;%>
							<tr>
								<td align="right"><%= k %></td>
								<td align="center"><%=VCARGO_NO.elementAt(i)%></td>
								<td align="center"><%=VCARGO_REF.elementAt(i)%></td>
								<td align="center"><%= VSHIP_NAME.elementAt(i) %></td>
								<td align="right"><%= VEDQ_QTY.elementAt(i) %></td>
								<td align="right"><%= VADQ_QTY.elementAt(i) %></td>
								<td align="right"><%= VDIFF_MMBTU.elementAt(i) %></td>
								<td align="left" colspan="1"><%=VCUST_ABBR_SN.elementAt(i) %></td>
								<td align="right" colspan="1"><%= VALLOCATED_QTY.elementAt(i) %></td>
								<td align="right" ><%= VBOE_NO.elementAt(i) %></td>
								<td align="right" ><%= VBOE_QTY.elementAt(i) %></td>
								<td align="center" ><%= VBOE_DT.elementAt(i) %></td>
								<td align="center" ><%= VARRIVAL_DT.elementAt(i) %></td>
							</tr>
						<% } %>
					    <%}else{ %>
						<tr>
							<td colspan="12" align="center"><%=utilmsg.infoMessage("<b>No Data Available for Selected Period!</b>") %></td>
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