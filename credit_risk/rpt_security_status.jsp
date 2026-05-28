
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
	var from_dt = document.forms[0].from_dt.value;

	var u = document.forms[0].u.value;
	
		var url = "rpt_security_status.jsp?u="+u+"&from_dt="+from_dt+"&clearance="+clearance;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var clearance = document.forms[0].clearance.value;
	
	var url = "xls_security_status.jsp?fileName=Security Status Report "+from_dt+".xls&from_dt="+from_dt+"&clearance="+clearance;

	location.replace(url);
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CreditRisk_Report" id="cr_report" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String from_dt = request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String clearance = request.getParameter("clearance")==null?"K":request.getParameter("clearance");

cr_report.setCallFlag("SECURITY_STATUS_REPORT");
cr_report.setComp_cd(owner_cd);
cr_report.setFrom_dt(from_dt);
cr_report.setClearance(clearance);
cr_report.init();

Vector VSEC_CATEGORY = cr_report.getVSEC_CATEGORY();
Vector V_SEC_CATEGORY = cr_report.getV_SEC_CATEGORY();
Vector VSTATUS = cr_report.getVSTATUS();
Vector VVALUE = cr_report.getVVALUE();
Vector VVALUE_USD = cr_report.getVVALUE_USD();
Vector VISS_BANK_REF = cr_report.getVISS_BANK_REF();
Vector VEXPIRE_DT = cr_report.getVEXPIRE_DT();
Vector VREMARK = cr_report.getVREMARK();
Vector VDEAL_DTL = cr_report.getVDEAL_DTL();
Vector VREF_NO = cr_report.getVREF_NO();
Vector VSEC_TYPE = cr_report.getVSEC_TYPE();
Vector VCOUNTERPARTY_NAME = cr_report.getVCOUNTERPARTY_NAME();
Vector VINDEX = cr_report.getVINDEX();
Vector VDEAL_NO = cr_report.getVDEAL_NO();
Vector VSEC_CATEGRY = cr_report.getVSEC_CATEGRY();
Vector VPREVIOUS_DT = cr_report.getVPREVIOUS_DT();
Vector VCURRENCY = cr_report.getVCURRENCY();
Vector VLEGAL_ENTITY = cr_report.getVLEGAL_ENTITY();
Vector VDIS_CONTRACT_TYPE = cr_report.getVDIS_CONTRACT_TYPE();
Vector VSTATUS_NM = cr_report.getVSTATUS_NM();
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
				    		Security Status Report 
	   	 				</div>
	   	 				<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div align="center">
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Report Date</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" disabled="disabled" size="8" maxLength="10" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						<input type="hidden" name="report_dt" >
		      						</div>
		      						<script>
										document.forms[0].report_dt.value="<%=from_dt%>"
									</script>
				    			</div>
								<div class="col-auto">
				    			<div class="btn-group" align="center">
									<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("K")){%>btnactive<%}%>" onclick="refresh('K')">KYC</label>
									<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("I")){%>btnactive<%}%>" onclick="refresh('I')">IGX</label>
								</div>
								</div>						
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%int i=0;int k=0; int l=0; int j=0; int m=0; int c=0; int n=0; int b=0; int d=0;
					for(j=0; j<VSEC_CATEGORY.size(); j++){
					int index = Integer.parseInt(""+VINDEX.elementAt(j));
					%>
					<% if(j!=0)
						{%>
							<div class="row">
								<div class="col-sm-12 col-xs-12 col-md-12">&nbsp;
								</div>
							</div> 
						<%} %>
							<div class="row m-b-5">
								<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;<%=VSEC_CATEGORY.elementAt(j)%> Security</label>
							</div>
					<div class="table-responsive">
						<table class="table table-bordered" id="filterbysearch">
							<thead>
								<tr>
									<th align="center">Sr#</th>
									<th align="center">Legal Entity<div align="center"><input class="form-control form-control-sm" type="text" id="legal" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Counterparty Name<div align="center"><input class="form-control form-control-sm" type="text" id="countpty_nm" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Issuing Bank Ref#<div align="center"><input class="form-control form-control-sm" type="text" id="issu_bank_ref" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Contract Type<div align="center"><input class="form-control form-control-sm" type="text" id="cont_type" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Contract#</th>
									<th align="center">Security Ref#<div align="center"><input class="form-control form-control-sm" type="text" id="sec_ref" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Security Type</th>
									<th align="center">Security Value(INR)</th>
									<th align="center">Security Value(USD)</th>
									<th align="center">Days before Expiry Date</th>
									<th align="center">Expiry Date</th>
									<th align="center">Status<div align="center"><input class="form-control form-control-sm" type="text" id="status" onkeyup="Search(this,'12');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Remarks</th>
									<th align="center">Form C</th>
								</tr>
							</thead>
							<tbody>
								<%k=0;
								if(index > 0){
									for(i=i;i<VREF_NO.size(); i++){ 
									k+=1;%>
										<tr>
											<td align="center"><%=k%></td>
											<td align="center"><%=VLEGAL_ENTITY.elementAt(i) %></td>
											<td><%=VCOUNTERPARTY_NAME.elementAt(i) %></td>
											<td align="center"><%=VISS_BANK_REF.elementAt(i) %></td>
											<td align="left"><%=VDIS_CONTRACT_TYPE.elementAt(i) %></td>
											<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
											<td align="center"><%=VREF_NO.elementAt(i) %></td>
											<td align="center">
												<span 
												<%if(VSEC_TYPE.elementAt(i).equals("LC")){ %>
		    										class="alert alert-info"
		    									<%}else if(VSEC_TYPE.elementAt(i).equals("BG")){ %>
		    										class="alert alert-warning"
		    									<%}else if(VSEC_TYPE.elementAt(i).equals("PCG")){ %>
		    										class="alert alert-primary"
		    									<%}else if(VSEC_TYPE.elementAt(i).equals("ADV")){ %>
		    										class="alert alert-dark"
	    										<%}else if(VSEC_TYPE.elementAt(i).equals("OA")){ %>
		    										class="alert alert-danger"
		    									<%}%>><%=VSEC_TYPE.elementAt(i) %></span>
		    								</td>
											<td align="right"><%=VVALUE.elementAt(i) %></td>
											<td align="right">
											<%-- <%if(VCURRENCY.elementAt(i).equals("1")){ %>
												INR
											<%}else if(VCURRENCY.elementAt(i).equals("2")){  %>
												USD
											<%} %> --%>
												<%=VVALUE_USD.elementAt(i) %>
											</td>
											<td align="center"><%=VPREVIOUS_DT.elementAt(i) %></td>
											<td align="center"><%=VEXPIRE_DT.elementAt(i) %></td>
											<td align="center">
												<%=VSTATUS_NM.elementAt(i) %>
											</td>
											<td><%=VREMARK.elementAt(i) %></td>
											<td></td>
										</tr>
										<%if(k==index)
										{
											i=i+1;
											break;
										}%>
									<%} %>
								<%}else{ %>
									<tr><td colspan="13" align="center"><%=utilmsg.infoMessage("<b>No Security Status Availabe!</b>") %></td></tr>
								<%} %>
							</tbody>
						</table>
					</div>
				<%} %>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="clearance" value="<%=clearance%>">
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
<script>
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
</script>
</html>