<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function exportToXls()
{
	var rpt_dt = document.forms[0].rpt_dt.value;
	var comp_abbr = document.forms[0].comp_abbr.value;
	
	var parts = rpt_dt.split('/');
	var formattedDate = null;

	if (parts.length === 3) {
	    formattedDate = parts[2] + parts[1].padStart(2, '0') + parts[0].padStart(2, '0');
	}
	
	var url = "xls_qp_exposure.jsp?fileName=Exposure_"+formattedDate+".csv&rpt_dt="+rpt_dt;

	location.replace(url);
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.market_risk.DB_MR_ExposureReport" id="db_MarketRisk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String previousDate=utildate.getPreviousDate();
String rpt_dt = request.getParameter("rpt_dt")==null?previousDate:request.getParameter("rpt_dt");
String comp_abbr = owner_abbr;

db_MarketRisk.setCallFlag("EXPOSURE_QP_RPT");
db_MarketRisk.setReport_dt(rpt_dt);
db_MarketRisk.setComp_cd(owner_cd);
db_MarketRisk.init();

Vector VCOB_DT = db_MarketRisk.getVCOB_DT();
Vector VLEGAL_ENTITY = db_MarketRisk.getVLEGAL_ENTITY();
Vector VDEAL_NUM = db_MarketRisk.getVDEAL_NUM();
Vector VCOUNTERPARTY = db_MarketRisk.getVCOUNTERPARTY();
Vector VBUY_SELL = db_MarketRisk.getVBUY_SELL();
Vector VUNIT = db_MarketRisk.getVUNIT();
Vector VCONTRACT_MONTH = db_MarketRisk.getVCONTRACT_MONTH();
Vector VCURVE_NAME = db_MarketRisk.getVCURVE_NAME();
Vector VEXPOSURE = db_MarketRisk.getVEXPOSURE();
Vector VFINANCIAL_PHYSICAL = db_MarketRisk.getVFINANCIAL_PHYSICAL();
Vector VREALISED_UNREALISED = db_MarketRisk.getVREALISED_UNREALISED();
Vector VFORWARD_PRICE = db_MarketRisk.getVFORWARD_PRICE();
Vector VPRICE_TYPE = db_MarketRisk.getVPRICE_TYPE();
Vector VCONT_TYPE_NM = db_MarketRisk.getVCONT_TYPE_NM();
Vector VREMARKS = db_MarketRisk.getVREMARKS();

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
				    		Exposure Report 
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
									<th align="center">COB DT</th>
									<th align="center">LEGAL ENTITY<br><div align="center"><input class="form-control form-control-sm" type="text" id="leg_entity" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">CONTRACT TYPE<br><div align="center"><input class="form-control form-control-sm" type="text" id="cont_type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">DEAL NUM<br><div align="center"><input class="form-control form-control-sm" type="text" id="deal_num" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">COUNTERPARTY<br><div align="center"><input class="form-control form-control-sm" type="text" id="cp" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">BUY/SELL</th>
									<th align="center">PRICE TYPE</th>
									<th align="center">UNIT</th>
									<th align="center">CONTRACT MONTH</th>
									<th align="center">CURVE NAME</th>
									<th align="center">EXPOSURE</th>
									<th align="center">FINANCIAL/PHYSICAL
									<th align="center">REALISED/UNREALISED</th>
									<th align="center">FORWARD PRICE</th>
									<th align="center">REMARKS</th>
								</tr>
							</thead>
							<tbody>
								<%if(VCOB_DT.size()>0){
									for(int i=0;i<VCOB_DT.size();i++){%>
									<tr>
										<td align="center"><%=i+1 %></td>
										<td align="center"><%=VCOB_DT.elementAt(i)%></td>
										<td align="center"><%=VLEGAL_ENTITY.elementAt(i)%></td>
										<td align="center"><%=VCONT_TYPE_NM.elementAt(i)%></td>
										<td align="center"><%=VDEAL_NUM.elementAt(i)%></td>
										<td align="center"><%=VCOUNTERPARTY.elementAt(i)%></td>
										<td align="center"><%=VBUY_SELL.elementAt(i)%></td>
										<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
										<td align="center"><%=VUNIT.elementAt(i)%></td>
										<td align="center"><%=VCONTRACT_MONTH.elementAt(i)%></td>
										<td align="center"><%=VCURVE_NAME.elementAt(i)%></td>
										<td align="right"><%=VEXPOSURE.elementAt(i)%></td><%-- <%if(Integer.parseInt(""+VEXPOSURE.elementAt(i))<0){ %> style="color: red" <%} %> --%>
										<td align="center"><%=VFINANCIAL_PHYSICAL.elementAt(i)%></td>
										<td align="center"><%=VREALISED_UNREALISED.elementAt(i)%></td>
										<td align="right"><%=VFORWARD_PRICE.elementAt(i)%></td>
										<td align="center"><%=VREMARKS.elementAt(i)%></td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan=16 align="center"><%=utilmsg.infoMessage("<b>No QP Exposure Data is Available!</b>") %></td>
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

<input type="hidden" name="comp_abbr" value="<%=comp_abbr%>">

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