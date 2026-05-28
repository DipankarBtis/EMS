
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh(clearance)
{
	var rpt_dt = document.forms[0].rpt_dt.value;

	var u = document.forms[0].u.value;
	
	var url = "rpt_gem_report.jsp.jsp?u="+u+"&rpt_dt="+rpt_dt+"&clearance="+clearance;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function exportToXls()
{
	var rpt_dt = document.forms[0].rpt_dt.value;
	
	var url = "xls_gem_report.jsp?fileName=Global Exposure Management Report "+rpt_dt+".xls&rpt_dt="+rpt_dt;

	location.replace(url);
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CR_ReceivableReport" id="dbcredit" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String previousDate=utildate.getPreviousDate();
String rpt_dt = request.getParameter("rpt_dt")==null?previousDate:request.getParameter("rpt_dt");

dbcredit.setCallFlag("GEM_REPORT");
dbcredit.setComp_cd(owner_cd);
//dbcredit.setRpt_dt(rpt_dt);
dbcredit.init();

Vector VINDUSTRY = dbcredit.getVINDUSTRY();
Vector VMARKET_TYPE = dbcredit.getVMARKET_TYPE();
Vector VCURRENCY = dbcredit.getVCURRENCY();
Vector VNET_EXPOSURE = dbcredit.getVNET_EXPOSURE();
Vector VOTHER_COLLATERAL = dbcredit.getVOTHER_COLLATERAL();
Vector VLC_AMOUNT = dbcredit.getVLC_AMOUNT();
Vector VCASH_COLLATERAL = dbcredit.getVCASH_COLLATERAL();
Vector VGROSS_EXPOSURE = dbcredit.getVGROSS_EXPOSURE();
Vector VFORWARD_MTM = dbcredit.getVFORWARD_MTM();
Vector VCURRENT_MONTH_UNDELIVERED = dbcredit.getVCURRENT_MONTH_UNDELIVERED();
Vector VUNBILLED_PAYABLE = dbcredit.getVUNBILLED_PAYABLE();
Vector VUNBILLED_RECEIVABLE = dbcredit.getVUNBILLED_RECEIVABLE();
Vector VACCOUNT_PAYABLE = dbcredit.getVACCOUNT_PAYABLE();
Vector VACCOUNT_RECEIVABLE = dbcredit.getVACCOUNT_RECEIVABLE();
Vector VLIMIT_VALUE = dbcredit.getVLIMIT_VALUE();
Vector VLIMIT_CATEGORY = dbcredit.getVLIMIT_CATEGORY();
Vector VFINAL_RATING = dbcredit.getVFINAL_RATING();
Vector VINTERNAL_RATING = dbcredit.getVINTERNAL_RATING();
Vector VMOODY_RATING = dbcredit.getVMOODY_RATING();
Vector VS_P_RATING = dbcredit.getVS_P_RATING();
Vector VULTIMATE_LEGAL_PARENT = dbcredit.getVULTIMATE_LEGAL_PARENT();
Vector VLEGAL_PARENT = dbcredit.getVLEGAL_PARENT();
Vector VCOUNTERPARTY_NAME = dbcredit.getVCOUNTERPARTY_NAME();
Vector VTRADING_ENTITY = dbcredit.getVTRADING_ENTITY();
Vector VCOUNTERPARTY_CD = dbcredit.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dbcredit.getVCOUNTERPARTY_ABBR();
Vector VDEAL_TYPE = dbcredit.getVDEAL_TYPE();
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="d-flex justify-content-between">
						<div class="topheader">
				    		Global Exposure Management Report
	   	 				</div>
	   	 				<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div align="center">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Report Date</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="rpt_dt" value="<%=rpt_dt%>" disabled="disabled" size="8" maxLength="10" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						<input type="hidden" name="report_dt" >
		      						</div>
		      						<script>
										document.forms[0].report_dt.value="<%=rpt_dt%>"
									</script>
				    			</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table id="search_by_filter" class="table table-bordered" id="filterbysearch">
							<thead>
								<tr>
									<th align="center">Sr#</th>
									<th align="center">TRADING ENTITY</th>
									<th align="center">COUNTERPARTY LONG NAME<br><div align="center"><input class="form-control form-control-sm" type="text" id="counterparty_abbr" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">DEAL TYPE<br><div align="center"><input class="form-control form-control-sm" type="text" id="counterparty_nm" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">LEGAL PARENT NAME</th>
									<th align="center">ULTIMATE LEGAL PARENT NAME</th>
									<th align="center">S&P RATING</th>
									<th align="center">MOODY RATING</th>
									<th align="center">INTERNAL RATING</th>
									<th align="center">FINAL RATING</th>
									<th align="center">LIMIT CATEGORY</th>
									<th align="center">CREDIT LIMIT<br><div align="center"><input class="form-control form-control-sm" type="text" id="ref_id" onkeyup="Search(this,'11');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">A/R</th>
									<th align="center">A/P</th>
									<th align="center">UNBILLED RECEIVABLE</th>
									<th align="center">UNBILLED PAYABLE</th>
									<th align="center">CURRENT MONTH UNDELIVERED</th>
									<th align="center">FORWARD MTM</th>
									<th align="center">GROSS EXPOSURE</th>
									<th align="center">CASH COLLATERAL</th>
									<th align="center">LC AMOUNT</th>
									<th align="center">OTHER COLLATERAL</th>
									<th align="center">NET EXPOSURE</th>
									<th align="center">CURRENCY</th>
									<th align="center">MARKET TYPE </th>
									<th align="center">INDUSTRY</th>
								</tr>
							</thead>
							<tbody>
								<%if(VCOUNTERPARTY_CD.size()>0){ %>
									<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
										<tr>
											<td align="center"><%=i+1 %>.</td>
											<td align="center"><%=VTRADING_ENTITY.elementAt(i)%></td>
											<td align="center"><%=VCOUNTERPARTY_NAME.elementAt(i)%></td>
											<td align="center"><%=VDEAL_TYPE.elementAt(i)%></td>
											<td align="center"><%=VLEGAL_PARENT.elementAt(i)%></td>
											<td align="center"><%=VULTIMATE_LEGAL_PARENT.elementAt(i)%></td>
											<td align="center"><%=VS_P_RATING.elementAt(i)%></td>
											<td align="center"><%=VMOODY_RATING.elementAt(i)%></td>
											<td align="center"><%=VINTERNAL_RATING.elementAt(i)%></td>
											<td align="center"><%=VFINAL_RATING.elementAt(i)%></td>
											<td align="center"><%=VLIMIT_CATEGORY.elementAt(i)%></td>
											<td align="right"><%=VLIMIT_VALUE.elementAt(i)%></td>
											<td align="right"><%=VACCOUNT_RECEIVABLE.elementAt(i)%></td>
											<td align="right"><%=VACCOUNT_PAYABLE.elementAt(i)%></td>
											<td align="right"><%=VUNBILLED_RECEIVABLE.elementAt(i)%></td>
											<td align="right"><%=VUNBILLED_PAYABLE.elementAt(i)%></td>
											<td align="right"><%=VCURRENT_MONTH_UNDELIVERED.elementAt(i)%></td>
											<td align="right"><%=VFORWARD_MTM.elementAt(i)%></td>
											<td align="right"><%=VGROSS_EXPOSURE.elementAt(i)%></td>
											<td align="right"><%=VCASH_COLLATERAL.elementAt(i)%></td>
											<td align="right"><%=VLC_AMOUNT.elementAt(i)%></td>
											<td align="right"><%=VOTHER_COLLATERAL.elementAt(i)%></td>
											<td align="right"><%=VNET_EXPOSURE.elementAt(i)%></td>
											<td align="center"><%=VCURRENCY.elementAt(i)%></td>
											<td align="center"><%=VMARKET_TYPE.elementAt(i)%></td>
											<td align="center"><%=VINDUSTRY.elementAt(i)%></td>
										</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="26" align="center"><%=utilmsg.infoMessage("<b>No Data Available!</b>") %></td>
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
<script>
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("search_by_filter");
  	
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
</script>
</body>
</html>