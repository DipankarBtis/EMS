<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
function refresh()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var flag = true;
	var msg="";
	
	  if(month!=null && year!=null )
	  {
		if(trim(month)!="" && trim(year)!="")
		{
		    var u = document.forms[0].u.value;
			if(flag==true)
			{
				var url = "rpt_table_2_lng_import.jsp?u="+u+"&month="+month+"&year="+year;
				
				document.getElementById("loading").style.visibility = "visible";
				location.replace(url);
			}
		}
		else
		{
		   alert("Please Select Valid Month or Year!");
		   flag = false;
		}
	}
	else
	{
	   alert("Please Select Valid Month OR Year!");
	   flag = false;
	}
}

function exportToXls()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var mnth = document.forms[0].mnth.value;
	var owner_abbr = document.forms[0].owner_abbr.value;
	
	var filename =filename = owner_abbr+"_"+"Table_2_LNG_Import- " +mnth+"/"+year+".xls";
	var url = "xls_table_2_lng_import.jsp?fileName="+filename+"&month="+month+"&year="+year;
	location.replace(url);
} 

</script>
<%@ include file="../util/common_js.jsp"%>
<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
</head>
<%
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();
int filter_start_year = CommonVariable.filter_start_year;
String sysdate=utildate.getSysdate();
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");

if(month.length() == 1)
{
	month="0"+month; 
}
String date = "01/"+month+"/"+year;
String mnth = utildate.getShortMonthName(date);
purchase.setCallFlag("IMPORT_OF_LNG_2");
purchase.setComp_cd(owner_cd);
purchase.setMonth(month);
purchase.setYear(year);

purchase.init();

Vector VTOTAL_QTY_MMBTU = purchase.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_MT = purchase.getVTOTAL_QTY_MT();
Vector VTOTAL_QTY_MMSCM = purchase.getVTOTAL_QTY_MMSCM();
Vector VMONTH = purchase.getVMONTH();
Vector VTOTAL_USD = purchase.getVTOTAL_USD();
Vector VTOTAL_INR = purchase.getVTOTAL_INR();
Vector VCONTRACT_TYPE = purchase.getVCONTRACT_TYPE();
Vector VCOUNTRY_NM = purchase.getVCOUNTRY_NM();
Vector VSALES_PRICE = purchase.getVSALES_PRICE();
Vector VGCV = purchase.getVGCV();

String dest_port = purchase.getDest_Port();
String profile_cd = purchase.getProfile_Cd();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form>
<div class="box-body">
  <div class="row">
    <div class="col-md-12 col-sm-12 col-xs-12">
     <div class="card cardmain">
       <div class="card-header cdheader">
		<div class="d-flex  justify-content-between">
			<div class="topheader">
			  Table 2 LNG Import
			</div>
			<div class="col-auto" onclick="exportToXls();" style="color:green;">
				<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
			</div>	
		</div>
	   </div>
       <div class="card-body cdbody">
		<div class="row">
		  <div class="col-sm-4 col-xs-4 col-md-4"></div>
			<div class="col-auto">
				<div class="form-group row">
					<label class="form-label"><b>Month</b></label>
		  		</div>
			</div>
			<div class="col-sm-4 col-xs-4 col-md-4">
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
			  <div class="col-auto">
				<div class="form-group row">
					<label class="form-label"><b>Year</b></label>
		  		</div>
			  </div>
			  <div class="col">
  				<select class="form-select form-select-sm" name="year" onchange="">
  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
						<option value="<%=i%>"><%=i%></option>
					<%} %>
				</select>
				<script>document.forms[0].year.value="<%=year%>"</script>
			  </div>
			  <div class="col-auto">
				 <input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
			  </div>
		    </div>
		   </div>
		  </div>
	   </div>
	   <div class="card-body cdbody">
	    <div class="row">
	      <div class="table-responsive">
	       <table class="table table-bordered table-hover ">
	         <thead>
		         <tr>
		          <th>Sr.<br>No</th>
		          <th>Ship un-loading<br> month /year</th>
		          <th>Importing Company Code</th>
		          <th>Type of Contract <br>(Term/Spot & others)</th>
		          <th>Source/ Origin  Country Code</th>
		          <th>Destination Port Code <br>(Unloading Port)</th>
		          <th>Quantity in Metric Tonnes as per Invoice</th>
		          <th>Quantity  in MMBTU as per Invoice</th>
		          <th>Gross Calorific Value (Kcal/SCM)</th>
		          <th>Quantity in MMSCM</th>
		          <th>Value of import(USD)</th>
		          <th>Value of import<br>(Rs. Crores) </th>
		          <th>Indicative DES Price <br>(US$/ MMBTU)</th>
		         </tr>
	         </thead>
	         <tbody>
	         <%if(VMONTH.size()>0){ %>
	         <%int K=0;%>
	            <% for(int i=0;i<VMONTH.size();i++){ 
	            K+=1; %>
	            <tr>
	           <td align="center"><%=K %></td>
	           <td><%=VMONTH.elementAt(i) %></td>
	           <td align="center"><%=profile_cd%></td>
	           <td align="center"><%=VCONTRACT_TYPE.elementAt(i)%></td>
	           <td align="center"><%=VCOUNTRY_NM.elementAt(i)%></td>
	           <td align="right"><%=dest_port%></td>
	           <td align="right"><%=VTOTAL_QTY_MT.elementAt(i) %></td>
	           <td align="right"><%=VTOTAL_QTY_MMBTU.elementAt(i) %></td>
	           <td align="right"><%= VGCV.elementAt(i)%></td>
	           <td align="right"><%=VTOTAL_QTY_MMSCM.elementAt(i) %></td>
	           <td align="right"><%=VTOTAL_USD.elementAt(i) %></td>
	           <td align="right"><%=VTOTAL_INR.elementAt(i) %></td>
	           <td align="right"><%=VSALES_PRICE.elementAt(i) %></td>
	           </tr>
	           <%}%>
	           <%} else{ %>
	             <tr>
	               <td colspan = "13" align="center"><%=utilmsg.infoMessage("<b>...No data for selected month based on Actual Arrival Date!...</b>") %></td>
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
<input type="hidden" name="mnth" value="<%=mnth%>">

</form>
</body>
</html>