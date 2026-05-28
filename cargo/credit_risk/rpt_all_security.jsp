<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh(clearance)
{
	var u = document.forms[0].u.value;
	
		var url = "rpt_all_security.jsp?clearance="+clearance+"&u="+u;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
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
	var clearance = document.forms[0].clearance.value;
	
	var url = "xls_all_security.jsp?fileName=All Security Report.xls&clearance="+clearance;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DB_CreditRisk_Report" id="credit_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String sec_category = request.getParameter("sec_category")==null?"":request.getParameter("sec_category");
String clearance = request.getParameter("clearance")==null?"K":request.getParameter("clearance");

credit_risk.setCallFlag("ALL_SECURITY_REPORT");
credit_risk.setCounterparty_cd(counterparty_cd);
credit_risk.setClearance(clearance);
credit_risk.setComp_cd(owner_cd);
credit_risk.init();

Vector VCOUNTERPARTY_CD = credit_risk.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NAME = credit_risk.getVCOUNTERPARTY_NAME();
Vector VSEC_CATEGORY = credit_risk.getVSEC_CATEGORY();
Vector VSEC_TYPE = credit_risk.getVSEC_TYPE();
Vector VSEC_REF_NO = credit_risk.getVSEC_REF_NO();
Vector VVALUE = credit_risk.getVVALUE();
Vector VRECEIVED_DATE = credit_risk.getVRECEIVED_DATE();
Vector VCURRENCY = credit_risk.getVCURRENCY();
Vector VSTATUS = credit_risk.getVSTATUS();
Vector VDEAL_NO = credit_risk.getVDEAL_NO();
Vector VISS_BANK_NM = credit_risk.getVISS_BANK_NM();
Vector VVALUE_FLUCTUATION = credit_risk.getVVALUE_FLUCTUATION();
Vector VDEAL_TYPE = credit_risk.getVDEAL_TYPE();
Vector VISS_BANK_REF = credit_risk.getVISS_BANK_REF();
Vector VADV_BANK_NM = credit_risk.getVADV_BANK_NM();
Vector VADV_BANK_REF = credit_risk.getVADV_BANK_REF();
Vector VCONFIRM_BANK_NM = credit_risk.getVCONFIRM_BANK_NM();
Vector VCONFIRM_BANK_REF = credit_risk.getVCONFIRM_BANK_REF();
Vector VREVIEW_DT = credit_risk.getVREVIEW_DT();
Vector VTENOR = credit_risk.getVTENOR();
Vector VISSUE_DT = credit_risk.getVISSUE_DT();
Vector VEXPIRE_DT = credit_risk.getVEXPIRE_DT();
Vector VREMARK = credit_risk.getVREMARK();
Vector VVALUE_VARIATION = credit_risk.getVVALUE_VARIATION();
Vector VGUARANTOR_NM = credit_risk.getVGUARANTOR_NM();
Vector VGUARANTOR_CD = credit_risk.getVGUARANTOR_CD();
Vector VISS_BANK_CD = credit_risk.getVISS_BANK_CD();
Vector VADV_BANK_CD = credit_risk.getVADV_BANK_CD();
Vector VCONFIRM_BANK_CD = credit_risk.getVCONFIRM_BANK_CD();
Vector VCANCEL_DT = credit_risk.getVCANCEL_DT();
Vector VRENEW_DT = credit_risk.getVRENEW_DT();
Vector VDEAL_DTL =credit_risk.getVDEAL_DTL();
Vector VSEQ_NO = credit_risk.getVSEQ_NO();
Vector VSEQ_REV_NO = credit_risk.getVSEQ_REV_NO();
Vector VCOUNTERPARTY_ABBR = credit_risk.getVCOUNTERPARTY_ABBR();
Vector VEXP_VAL = credit_risk.getVEXP_VAL();
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_CreditRisk">
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
				    		All Security Report 
	   	 				</div>
	   	 				<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-12">
							<div align="center">
								<div class="btn-group" align="center">
									<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("K")){%>btnactive<%}%>" onclick="refresh('K')">KYC</label>
									<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("I")){%>btnactive<%}%>" onclick="refresh('I')">IGX</label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="filterbysearch">
									<thead>
										<tr>
											<th>SR#</th>
											<th>Counterparty<div align="center"><input class="form-control form-control-sm" type="text" id="counterparty_nm" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Incoming/ Outgoing<div align="center"><input class="form-control form-control-sm" type="text" id="category" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Security Type<div align="center"><input class="form-control form-control-sm" type="text" id="security_type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Security Ref.No<div align="center"><input class="form-control form-control-sm" type="text" id="security_reference_no" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Status<div align="center"><input class="form-control form-control-sm" type="text" id="stats" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Deal Type<div align="center"><input class="form-control form-control-sm" type="text" id="dealType" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Deal No.<div align="center"><input class="form-control form-control-sm" type="text" id="dealNo" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Currency</th>
											<th>value</th>
											<th>Value Variation</th>
											<th>Value Fluctuation (%)</th>
											<th>Received / Authenticated Date</th>
											<th>Issuance Date</th>
											<th>Expire Date</th>
											<th>Cancellation / Restate Date</th>
											<th>Renew Date</th>
											<th>Tenor (Day)</th>
											<th>Issuing Bank name</th>
											<th>Issuing Bank's Reference</th>
											<th>Advising Bank Name</th>
											<th>Advising Bank's Reference</th>
											<th>Confirming Bank Name</th>
											<th>Confirming Bank's Reference</th>
											<th>Guarantor Name</th>
											<th>Remarks</th>
											<th>Review date</th>
										</tr>
									</thead>
									<tbody>
									<%int k=0;
									for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){
										k+=1;
										String security_type = ""+VSEC_TYPE.elementAt(i);
										String security_category = ""+VSEC_CATEGORY.elementAt(i);
										%>
										
										<tr>
											<td align="right"><%=k%></td>
											<td align="center"><%=VCOUNTERPARTY_NAME.elementAt(i) %></td>
											<td align="center">
												<%if(security_category.equals("R")){ %>
													Incoming
												<%}else if(security_category.equals("I")){ %>
													Outgoing
												<%} %>
											</td>
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
	    									<%}%>><%=VSEC_TYPE.elementAt(i) %></span></td>
											<td align="center"><%=VSEC_REF_NO.elementAt(i) %></td>
											<td align="center">
												<%if(VSTATUS.elementAt(i).equals("P")){ %>
													<span class="alert alert-primary">Pending</span>
												<%}else if(VSTATUS.elementAt(i).equals("O")){  %>
													<span class="alert alert-success">In Order</span>
												<%}else if(VSTATUS.elementAt(i).equals("C")){  %>
													<span class="alert alert-danger">Cancelled</span>
												<%}else if(VSTATUS.elementAt(i).equals("A")){  %>
													<span class="alert alert-secondary">Pending For Amendment</span>
												<%}else if(VSTATUS.elementAt(i).equals("R")){  %>
													<span class="alert alert-warning">Restated</span>
												<%}else if(VSTATUS.elementAt(i).equals("D")){  %>
													<span class="alert alert-info">Dummy</span>
												<%} %>
											</td>
											<td align="center"><%=VDEAL_TYPE.elementAt(i) %></td>
											<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
											<td align="center">
											<%if(VCURRENCY.elementAt(i).equals("1")){ %>
												INR
											<%}else if(VCURRENCY.elementAt(i).equals("2")){ %>
												USD
											<%}else{} %>
											</td>
											<td align="right"><%=VVALUE.elementAt(i) %></td>
											<td align="center"><%=VVALUE_VARIATION.elementAt(i) %></td>
											<td align="center"><%=VVALUE_FLUCTUATION.elementAt(i) %></td>
											<td align="center"><%=VRECEIVED_DATE.elementAt(i) %></td>
											<td align="center"><%=VISSUE_DT.elementAt(i) %></td>
											<td align="center"><%=VEXPIRE_DT.elementAt(i) %></td>
											<td align="center"><%=VCANCEL_DT.elementAt(i) %></td>
											<td align="center"><%=VRENEW_DT.elementAt(i) %></td>
											<td align="center"><%=VTENOR.elementAt(i) %></td>
											<td align="center"><%=VISS_BANK_NM.elementAt(i) %></td>
											<td align="center"><%=VISS_BANK_REF.elementAt(i) %></td>
											<td align="center"><%=VADV_BANK_NM.elementAt(i) %></td>
											<td align="center"><%=VADV_BANK_REF.elementAt(i) %></td>
											<td align="center"><%=VCONFIRM_BANK_NM.elementAt(i) %></td>
											<td align="center"><%=VCONFIRM_BANK_REF.elementAt(i) %></td>
											<td align="center"><%=VGUARANTOR_NM.elementAt(i) %></td>
											<td align="center"><%=VREMARK.elementAt(i) %></td>
											<td align="center"><%=VREVIEW_DT.elementAt(i) %></td>
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
</html>