<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>


function refresh()
{
	var start_month = document.forms[0].start_month.value;
	var end_month = document.forms[0].end_month.value;
	var from_year = document.forms[0].from_year.value;
	var to_year = document.forms[0].to_year.value;
	var tmp_month = document.forms[0].tmp_from_month.value;
	var tmp_year = document.forms[0].tmp_from_year.value;
	var tmp_month_to = document.forms[0].tmp_to_month.value;
	var tmp_year_to = document.forms[0].tmp_to_year.value;
	var flag;
	var msg="";
	
	function validFinancialYear(start_month,from_year,end_month,to_year)
	{
	   function getFY(month, year)
	     {
	        return month>=4?year:year-1;
	     }
       
	     const fy_from = getFY(start_month,from_year);
	     const fy_to = getFY(end_month,to_year);

	     if(fy_from!==fy_to)
	     {
	        alert("Selected months belong to different financial year!");
	        return false;
	     }
	     
		 const monthdiff = (to_year*12+end_month)-(from_year*12+start_month);
		
		 if( monthdiff<0)
		 {
		   alert("From Month/Year should not be less than To Month/Year!");
		   return false;
		 }
		 
		 if( monthdiff>11)
		 {
		   alert("Month/Year range cannot exceed 12 months!");
		   return false;
		 }
           return true
      }
	
	  if(start_month!=null && from_year!=null && end_month!=null && to_year!=null)
	  {
		if(trim(start_month)!="" && (from_year)!="" && (end_month)!="" && (to_year)!="")
		{
			start_month = Number(start_month);
			end_month   = Number(end_month);
			from_year   = Number(from_year);
			to_year     = Number(to_year);
			flag = 	validFinancialYear(start_month, from_year, end_month, to_year);
		    var u = document.forms[0].u.value;
			if(flag==true)
			{
				var url = "rpt_import_of_lng.jsp?u="+u+"&from_year="+from_year+
				"&start_month="+start_month+"&to_year="+to_year+"&end_month="+end_month;
				
				document.getElementById("loading").style.visibility = "visible";
				location.replace(url);
			}
			else
			{
				document.forms[0].start_month.value=tmp_month;
				document.forms[0].from_year.value=tmp_year;
				document.forms[0].end_month.value=tmp_month_to;
				document.forms[0].to_year.value=tmp_year_to;
			}
		}
	}
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
String from_year=request.getParameter("from_year")==null?""+currentYear:request.getParameter("from_year");
String start_month=request.getParameter("start_month")==null?""+currentMonth:request.getParameter("start_month");
String to_year=request.getParameter("to_year")==null?""+currentYear:request.getParameter("to_year");
String end_month=request.getParameter("end_month")==null?""+currentMonth:request.getParameter("end_month");
if(start_month.length() == 1)
{
	start_month="0"+start_month; 
}
if(end_month.length() == 1)
{
	end_month="0"+end_month; 
}
purchase.setCallFlag("IMPORT_OF_LNG");
purchase.setComp_cd(owner_cd);
purchase.setMonth(start_month);
purchase.setYear(from_year);
purchase.setMonth_to(end_month);
purchase.setYear_to(to_year);
purchase.init();

Vector VQTY_MMBTU = purchase.getVQTY_MMBTU();
Vector VQTY_MT = purchase.getVQTY_MT();
Vector VQTY_MMSCM = purchase.getVQTY_MMSCM();
Vector VMONTH = purchase.getVMONTH();
Vector VUSD = purchase.getVUSD();
String mmbtu_qty_sum = purchase.getMmbtu_sum();
String mt_qty_sum = purchase.getMt_sum();
String mmscm_qty_sum = purchase.getMmscm_sum();

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
				<div class="topheader">Import Of LNG</div>
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
			  				<select class="form-select form-select-sm" name="start_month" onchange="">
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
							<script>document.forms[0].start_month.value="<%=start_month%>"</script>
						</div>
						<div class="col">
			  				<select class="form-select form-select-sm" name="from_year" onchange="">
			  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
									<option value="<%=i%>"><%=i%></option>
								<%} %>
							</select>
							<script>document.forms[0].from_year.value="<%=from_year%>"</script>
						</div>
					</div>
				</div>
				<div class="col-auto">
					<div class="form-group row">
						<label class="form-label"><b>to</b></label>
					</div>
				</div>
				<div class="col-sm-3 col-xs-3 col-md-3">
					<div class="form-group row">
						<div class="col">
			  				<select class="form-select form-select-sm" name="end_month" onchange="">
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
							<script>document.forms[0].end_month.value="<%=end_month%>"</script>
						</div>
						<div class="col">
			  				<select class="form-select form-select-sm" name="to_year" onchange="">
			  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
									<option value="<%=i%>"><%=i%></option>
								<%} %>
							</select>
							<script>document.forms[0].to_year.value="<%=to_year%>"</script>
						</div>
						<div class="col-auto">
							<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
						</div>
					</div>
		  		</div>
			<div class="col-sm-2 col-xs-2 col-md-2"></div>
		 </div>
	   </div>
	   
	   <div class="card-body cdbody">
	   <div class="row m-b-5">
			<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Import Of LNG during <%=from_year %>-<%=Integer.parseInt(from_year.substring(2))+1 %></label>
	   </div>
	   <%--  <div class="row">
		   <div class="col-md-12 col-sm-12 col-xs-12">
		      <div class="card-header cdheader">
			      <div class="d-flex  justify-content-center">
					<div class="topheader" >Import Of LNG during <%=from_year %>-<%=Integer.parseInt(from_year.substring(2))+1 %></div>
				 </div>
		      </div>
		   </div>
	    </div> --%>
	   <!-- </div> -->
	    
	  <!-- <div class="card-body cdbody"> -->
	    <div class="row">
	      <div class="table-responsive">
	       <table class="table table-bordered table-hover ">
	         <thead>
		         <tr>
		         <th rowspan="3" valign="middle">Sr.<br>No</th>
		         <th rowspan="3"  valign="middle">Month</th>
		         <th colspan="10" style="background-color:#336699; color:white">Shell Energy India Private Limited</th>
		         </tr>
		         <tr>
		         <th colspan="6">Quantity</th>
		         <th colspan="4">Value</th>
		         </tr>
		         <tr>
		          <th colspan="2">MMBTU</th>
		          <th colspan="2">MT</th>
		          <th colspan="2">MMSCM</th>
		          <th colspan="2">USD</th>
		          <th colspan="2">INR</th>
		         </tr>
	         </thead>
	         <tbody>
	         <%int K=0;%>
	            <% for(int i=0;i<VMONTH.size();i++){ 
	            K+=1; %>
	            <tr>
	           <td align="center"><%=K %></td>
	           <td align="center"><%=VMONTH.elementAt(i) %></td>
	           <td colspan="2" align="right"><%=VQTY_MMBTU.elementAt(i) %></td>
	           <td colspan="2" align="right"><%=VQTY_MT.elementAt(i) %></td>
	           <td colspan="2" align="right"><%=VQTY_MMSCM.elementAt(i) %></td>
	           <td colspan="2" align="right"><%=VUSD.elementAt(i) %></td>
	           <td colspan="2" align="right">J</td>
	           </tr>
	           <%} %>
	          <!--  <th>&nbsp;</th>
	           <th>Total</th>
	           <th colspan="2">Total MMBTU</th>
	           <th colspan="2">Total MT</th>
	           <th colspan="2">Total MMSCM</th>
	           <th colspan="2">Total USD</th>
	           <th colspan="2">Total INR</th> -->
	           <tr>
	            <td>&nbsp;</td>
	            <td align="center"> <b>Total:</b></td>
	            <td colspan="2" align="right"> <%=mmbtu_qty_sum %></td>
	            <td colspan="2" align="right"> <%=mt_qty_sum %></td>
	            <td colspan="2" align="right"> <%= mmscm_qty_sum%></td>
	            <td colspan="2" align="right"> Total USD:</td>
	            <td colspan="2" align="right"> Total INR</td>
	           </tr>
	         </tbody> 
	       </table>
	      </div>
	    </div>
	  </div>
	  </div>
    </div>
  </div>
</div>

<input type="hidden" name="tmp_from_month" value="<%=start_month%>">
<input type="hidden" name="tmp_from_year" value="<%=from_year%>">
<input type="hidden" name="tmp_to_month" value="<%=end_month%>">
<input type="hidden" name="tmp_to_year" value="<%=to_year%>">

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