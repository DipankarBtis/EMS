<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
function refresh()
{
	var financial_year=document.forms[0].financial_year.value;
	var flag = true;
	var msg="";
	
	  if(financial_year!=null && trim(financial_year)!="" && financial_year!="0")
	  {
		    var u = document.forms[0].u.value;
			if(flag==true)
			{
				var url = "rpt_import_of_lng.jsp?u="+u+"&financial_year="+financial_year;
				document.getElementById("loading").style.visibility = "visible";
				location.replace(url);
			}
	 }
	 else
	 {
	   alert("Please Select Valid Financial Year!");
	   flag = false;
	 }
} 

function exportToXls()
{
	var financial_year=document.forms[0].financial_year.value;
	var owner_abbr = document.forms[0].owner_abbr.value;
	var beg_mon = financial_year.split("-")[0];
	var lst_mon = financial_year.split("-")[1];
	
	var filename = owner_abbr+"_"+"Monthly Import of LNG_MoPNG- "+"APR-"+beg_mon+"_"+"MAR-"+lst_mon+".xls";
	var url = "xls_import_of_lng.jsp?fileName="+filename+"&financial_year="+financial_year;
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
String curr_fin = utildate.getFinancialYear(sysdate);
String financial_year=request.getParameter("financial_year")==null?""+curr_fin:request.getParameter("financial_year");
String fin_yr = financial_year.split("-")[0];

purchase.setCallFlag("IMPORT_OF_LNG");
purchase.setComp_cd(owner_cd);
purchase.setYear(fin_yr);
purchase.setYear_to(filter_start_year+1+"");
purchase.init();

Vector VTOTAL_QTY_MMBTU = purchase.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_MT = purchase.getVTOTAL_QTY_MT();
Vector VTOTAL_QTY_MMSCM = purchase.getVTOTAL_QTY_MMSCM();
Vector VMONTH = purchase.getVMONTH();
Vector VTOTAL_USD = purchase.getVTOTAL_USD();
Vector VTOTAL_INR = purchase.getVTOTAL_INR();
Vector VFINANCIAL_YEAR = purchase.getVFINANCIAL_YEAR();

String mmbtu_qty_sum = purchase.getMmbtu_sum();
String mt_qty_sum = purchase.getMt_sum();
String mmscm_qty_sum = purchase.getMmscm_sum();
String total_usd_sum = purchase.getUSD_sum();
String total_inr_sum = purchase.getINR_sum();
String comp_nm = purchase.getComp_nm();

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
				  Import of LNG_MoPNG
				</div>
				<div class="col-auto" onclick="exportToXls();" style="color:green;">
					<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
				</div>	
			</div>
	   </div>
       <div class="card-body cdbody">
		<div class="row">
		  <div class="col-sm-3 col-xs-3 col-md-3"></div>
			<div class="col-sm-4 col-xs-4 col-md-4">
				<div class="form-group row">
					<div class="col-auto">
						<label class="form-label"><b>Financial Year<span class="s-red">*</span> </b></label>
					</div>
					<div class="col">
						<!-- <div class="btn-group"> -->
							<select class="form-select form-select-sm" name="financial_year" onchange="">
								<option value="0">--Select--</option>
								<%for(int i=0;i<VFINANCIAL_YEAR.size();i++){ %>
								<option value="<%=VFINANCIAL_YEAR.elementAt(i)%>"><%=VFINANCIAL_YEAR.elementAt(i)%></option>
								<%} %>
							</select>
						<!-- </div> -->
						<script>
							document.forms[0].financial_year.value="<%=financial_year%>"
						</script>
					</div>
					<div class="col-auto">
				       <input type="button" class="btn btn-warning com-btn"
					     value="Apply Filter" onclick="refresh();">
			        </div>
	    		</div>
			</div>
		 </div>
	   </div>
	   <div class="card-body cdbody">
	   <div class="row m-b-5" align="center">
			<label class="form-label subheader">Import Of LNG during <%=financial_year%></label>
	   </div>
	    <div class="row">
	      <div class="table-responsive">
	       <table class="table table-bordered table-hover ">
	         <thead>
		         <tr>
		         <th rowspan="3" valign="middle"><b>Sr.<br>No</b></th>
		         <th rowspan="3"  valign="middle"><b>Month</b></th>
		         <th colspan="10" style="<%if(owner_cd.equals("2")){ %>background-color:#336699; color:white<%} else{ %>background-color:#FFD700; color:black<%}%>"><b><%=comp_nm %></b></th>
		         </tr>
		         <tr>
		         <th colspan="6"><b>Quantity</b></th>
		         <th colspan="4"><b>Value</b></th>
		         </tr>
		         <tr>
		          <th colspan="2"><b>MMBTU</b></th>
		          <th colspan="2"><b>MT</b></th>
		          <th colspan="2"><b>MMSCM</b></th>
		          <th colspan="2"><b>USD</b></th>
		          <th colspan="2"><b>Rs. Crores</b></th>
		         </tr>
	         </thead>
	         <tbody>
	         <%int K=0;%>
	            <% for(int i=0;i<VMONTH.size();i++){ 
	            K+=1; %>
	            <tr>
	           <td align="center"><%=K %></td>
	           <td align="center"><%=VMONTH.elementAt(i) %></td>
	           <td colspan="2" align="right"><%=VTOTAL_QTY_MMBTU.elementAt(i) %></td>
	           <td colspan="2" align="right"><%=VTOTAL_QTY_MT.elementAt(i) %></td>
	           <td colspan="2" align="right"><%=VTOTAL_QTY_MMSCM.elementAt(i) %></td>
	           <td colspan="2" align="right"><%=VTOTAL_USD.elementAt(i) %></td>
	           <td colspan="2" align="right"><%=VTOTAL_INR.elementAt(i) %></td>
	           </tr>
	           <%}%>
	           <tr>
	            <td>&nbsp;</td>
	            <td align="center"> <b>Total:</b></td>
	            <td colspan="2" align="right"> <b><%=mmbtu_qty_sum %></b></td>
	            <td colspan="2" align="right"> <b><%=mt_qty_sum %></b></td>
	            <td colspan="2" align="right"> <b><%= mmscm_qty_sum%></b></td>
	            <td colspan="2" align="right"><b><%=total_usd_sum%></b></td>
	            <td colspan="2" align="right"><b><%=total_inr_sum%></b></td>
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

<input type="hidden" name="tmp_from_year" value="<%=fin_yr%>">
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

</form>
</body>
</html>