
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script src="../js/chart_js/chart.js"></script>
<script>

function refresh(clearance)
{

	var u = document.forms[0].u.value;
	
		var url = "rpt_limit_summary.jsp?u="+u+"&clearance="+clearance;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function exportToXls(clearance)
{
	
	var url = "xls_limit_summary.jsp?fileName=Limit Summary Report.xls&clearance="+clearance;	

	location.replace(url);
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CreditRisk_Report" id="cr_report" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String clearance = request.getParameter("clearance")==null?"K":request.getParameter("clearance");
String report_dt = request.getParameter("report_dt")==null?sysdate:request.getParameter("report_dt");

cr_report.setCallFlag("LIMIT_SUMMARY");
cr_report.setClearance(clearance);
cr_report.setComp_cd(owner_cd);
cr_report.init();

Vector VCOUNTERPARTY_CD = cr_report.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NAME = cr_report.getVCOUNTERPARTY_NAME();
Vector VCREDIT_RATING = cr_report.getVCREDIT_RATING();
Vector VAVAILABLE = cr_report.getVAVAILABLE();
Vector VTOTAL_LIMIT = cr_report.getVTOTAL_LIMIT();
Vector VUNSECURED = cr_report.getVUNSECURED();
Vector VTEMPORARY = cr_report.getVTEMPORARY();
Vector VADJUST_USAGE = cr_report.getVADJUST_USAGE();
Vector VUSAGE = cr_report.getVUSAGE();
Vector VNET_USAGE = cr_report.getVNET_USAGE();
Vector VUSED = cr_report.getVUSED();
Vector VCOUNTERPARTY_ABBR = cr_report.getVCOUNTERPARTY_ABBR();
Vector VUSAGE_1 = cr_report.getVUSAGE_1();
Vector VUSAGE_2 = cr_report.getVUSAGE_2();

%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="">
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
				    		Limit Summary Report 
	   	 				</div>
	   	 				<div onclick="exportToXls('<%=clearance %>')" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-7">
							<div class="d-flex justify-content-between">
								<div class="form-group row">
									<div class="col-auto">
											<label class="form-label"><b>Report Date </b></label>
									</div>
									<div class="col-auto">
					      				<div class="input-group input-group-sm" >
				      				   		<input type="text" class="form-control form-control-sm" name="report_dt" value="<%=report_dt %>" readonly>
			      						</div>
				      				</div>
			      				</div>
					    		<div align="center">
									<div class="btn-group" >
										<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("K")){%>btnactive<%}%>" onclick="refresh('K')">KYC</label>
										<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("I")){%>btnactive<%}%>" onclick="refresh('I')">IGX</label>
									</div>
								</div>
							</div>
					    </div>
   					</div>
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered" id="filterbysearch">
							<thead>
								<tr>
									<th>SR#</th>
									<th>Counterparty/<br> Bank CD</th>
									<th>Counterparty/<br> Bank Name</th>
									<th>Counterparty/<br> Bank ABBR</th>
									<th>Credit rating</th>
									<th>Available limit (INR)</th>
									<th>Net Usage (INR)</th>
									<th>Usage (INR)</th>
									<th>Total limit (INR)</th>
									<th>Unsecured limit (INR)</th>
									<th>Temporary limit (INR)</th>
									<th>Usage Adjustment (INR)</th>
									<th>% Used</th>
								</tr>
							</thead>
							<tbody>
							<%int k=0;
							if(VCOUNTERPARTY_NAME.size()!=0)
							{ 
								for(int i=0; i<VCOUNTERPARTY_NAME.size(); i++){
								k+=1;
								
								String split_used = ""+VUSED.elementAt(i);
								String[] splited_Strings = split_used.replace(".", "@").split("@");
								String split_used1 = splited_Strings[0];
								String split_used2 = splited_Strings[1].substring(0,2);
								String used = ""+split_used1+"."+split_used2;
								%>
									<tr>
										<td align="center"><%=k %></td>
										<td align="center"><%=VCOUNTERPARTY_CD.elementAt(i) %></td>
										<td align="center"><%=VCOUNTERPARTY_NAME.elementAt(i) %></td>
										<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i) %></td>
										<td align="center"><%=VCREDIT_RATING.elementAt(i) %></td>
										<td align="right"><%=VAVAILABLE.elementAt(i) %></td>
										<td align="right"><%=VNET_USAGE.elementAt(i) %></td>
										<td align="right" title="SEMTIPL : <%=VUSAGE_1.elementAt(i)%> SEIPL : <%=VUSAGE_2.elementAt(i)%>"><%=VUSAGE.elementAt(i) %></td>
										<td align="right"><%=VTOTAL_LIMIT.elementAt(i) %></td>
										<td align="right"><%=VUNSECURED.elementAt(i) %></td>
										<td align="right"><%=VTEMPORARY.elementAt(i) %></td>
										<td align="right"><%=VADJUST_USAGE.elementAt(i) %></td>
										<td align="right"><%=used%> %</td>
									</tr>
								<%} %>
							<%}else{ %>
								<tr><td colspan="13" align="center"><%=utilmsg.infoMessage("<b>Limit Summary Data Is Not Available!</b>") %></td></tr>
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
</body>
</html>