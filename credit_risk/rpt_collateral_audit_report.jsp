
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var flag=true;
	var msg="";
	
	var count = compareDate(from_dt,to_dt);
	if(parseInt(count) == 1)
	{
		msg+="From Date should be less or equal To Date!";
		flag=false;
	}
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "rpt_collateral_audit_report.jsp?u="+u+
				"&from_dt="+from_dt+"&to_dt="+to_dt+"&counterparty_cd="+counterparty_cd;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
		document.forms[0].from_dt.value="";
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
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch2");
  	
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
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var url = "xls_collateral_audit_report.jsp?fileName=Audit_report.xls&counterparty_cd="+counterparty_cd+"&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CreditRisk_Report" id="cr_report" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt = request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

cr_report.setCallFlag("COLLATERAL_AUDIT_REPORT");
cr_report.setComp_cd(owner_cd);
cr_report.setCounterparty_cd(counterparty_cd);
cr_report.setFrom_dt(from_dt);
cr_report.setTo_dt(to_dt);
cr_report.init();

Vector VSECURITYDETAILS = cr_report.getVSECURITYDETAILS();
Vector VCOUNTERPARTY_CD = cr_report.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = cr_report.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_NAME = cr_report.getVCOUNTERPARTY_NAME();
Vector VCOUNTERPARTY_ABBR = cr_report.getVCOUNTERPARTY_ABBR();
Vector VLAST_UPDATE_DATE = cr_report.getVLAST_UPDATE_DATE();
Vector VLAST_UPDATE_BY = cr_report.getVLAST_UPDATE_BY();
Vector VDEAL_NO = cr_report.getVDEAL_NO();
Vector VREF_NO = cr_report.getVREF_NO();
Vector VSEC_TYPE = cr_report.getVSEC_TYPE();
Vector VBGCOLOR = cr_report.getVBGCOLOR();
Vector VCONT_REF = cr_report.getVCONT_REF();
Vector VAUDIT_TYPE = cr_report.getVAUDIT_TYPE();
Vector VCO_ABBR = cr_report.getVCO_ABBR();

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
				    		Audit report 
	   	 				</div>
	   	 				<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-15">
						<div align="center">
							<div class="col-sm-4 col-xs-4 col-md-4">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>From</b></label>
									</div>
									<div class="col-auto">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
									<div class="col-auto">
										<label class="form-label"><b>To</b></label>
									</div>
									<div class="col-auto">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
								</div>
							</div>
						</div>
					</div>
	      			<div class="form-group row">
	      				<div class="row m-b-5">
							<%-- <div class="col-auto">
								<label class="form-label"><b>Counterparty / Bank :</b></label>
							</div>
							<div class="col-auto">
								<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
									<option value="0" lable="0" selected="selected">--ALL--</option>
									<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i) %>"><%=VCOUNTERPARTY_NAME.elementAt(i) %></option>
									<%} %> 
								</select>
								<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
							</div>  --%>
							<input type="hidden" name="counterparty_cd" value="0"><!-- This input type hidden Is Added Because Dropdown has Been Removed -->
						</div>
					</div>	
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>Collateral Management Audit report </label>
					</div>
					<div class="table-responsive">
						<table class="table table-bordered" id="filterbysearch">
							<thead>
								<tr>
									<th>Sr#</th>
									<th align="center">Legal Entity<div align="center"><input class="form-control form-control-sm" type="text" id="legal_entity" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Last Updated On<div align="center"><input class="form-control form-control-sm" type="text" id="updated_on" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Last Updated By<div align="center"><input class="form-control form-control-sm" type="text" id="updated_by" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Counterparty Name<div align="center"><input class="form-control form-control-sm" type="text" id="counterparty_nm" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Counterparty ABBR<div align="center"><input class="form-control form-control-sm" type="text" id="counterparty_abbr" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Security Type<div align="center"><input class="form-control form-control-sm" type="text" id="sec_type" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Security Ref#<div align="center"><input class="form-control form-control-sm" type="text" id="sec_ref" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Contract#<br>[Contract/Trade Ref#]<div align="center"><input class="form-control form-control-sm" type="text" id="deal_no" onkeyup="Search(this,'8');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Change Type</th>
								</tr>
							</thead>
							<tbody>
							<%if(VSECURITYDETAILS.size()>0){
								int j=0;%>
								<%for(int i=0;i<VSECURITYDETAILS.size(); i++){ 
								 if(!VREF_NO.elementAt(i).equals("") && VAUDIT_TYPE.elementAt(i).equals("")){
									j++;%>
									<tr>
										<td align="center"><%=j%></td>
										<td align="center"><%=VCO_ABBR.elementAt(i)%></td>
										<td align="center"><%=VLAST_UPDATE_DATE.elementAt(i)%></td>
										<td align="center"><%=VLAST_UPDATE_BY.elementAt(i)%></td>
										<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
										<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
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
	   									<td align="center"><font style="background: <%=VBGCOLOR.elementAt(i)%>"><%=VREF_NO.elementAt(i) %></font></td>
										<td align="center"><%=VDEAL_NO.elementAt(i)%><br>
										</td>
										<td><%=VSECURITYDETAILS.elementAt(i) %></td>
									</tr>
									<%}%>
								<%} %>
								<%if(j<=0){ %>
									<tr><td colspan="10" align="center"><%=utilmsg.infoMessage("<b>Collateral Audit History Not Available for Selected Date!</b>") %></td></tr>
								<%} %>
							<%}else{ %>
								<tr><td colspan="10" align="center"><%=utilmsg.infoMessage("<b>Collateral Audit History Not Available for Selected Date!</b>") %></td></tr>
							<%} %>						
							</tbody>
						</table>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Credit Limit / Credit Rating Audit report </label>
					</div>
					<div class="table-responsive">
						<table class="table table-bordered" id="filterbysearch2">
							<thead>
								<tr>
									<th>Sr#</th>
									<th align="center">Legal Entity<div align="center"><input class="form-control form-control-sm" type="text" id="cr_legal_entity" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Last Updated On<div align="center"><input class="form-control form-control-sm" type="text" id="cr_updated_on" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Last Updated By<div align="center"><input class="form-control form-control-sm" type="text" id="cr_updated_by" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Counterparty/Bank Name<div align="center"><input class="form-control form-control-sm" type="text" id="cr_counterparty_nm" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">Counterparty/Bank ABBR<div align="center"><input class="form-control form-control-sm" type="text" id="cr_counterparty_abbr" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
									<!-- <th align="center">Security Type<div align="center"><input class="form-control form-control-sm" type="text" id="cr_sec_type" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th> -->
									<th align="center">Credit Rating/Limit Ref#<div align="center"><input class="form-control form-control-sm" type="text" id="cr_sec_ref" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
									<!-- <th align="center">Contract#<br>[Contract/Trade Ref#]<div align="center"><input class="form-control form-control-sm" type="text" id="cr_deal_no" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div></th> -->
									<th>Change Detail</th>
								</tr>
							</thead>
							<tbody>
							<%if(VSECURITYDETAILS.size()>0){
								int j=0; %>
								<%for(int i=0;i<VSECURITYDETAILS.size(); i++){ 
								 if(!VREF_NO.elementAt(i).equals("") && !VAUDIT_TYPE.elementAt(i).equals("")){
									j++;%>
									<tr>
										<td align="center"><%=j%></td>
										<td align="center"><%=VCO_ABBR.elementAt(i)%></td>
										<td align="center"><%=VLAST_UPDATE_DATE.elementAt(i)%></td>
										<td align="center"><%=VLAST_UPDATE_BY.elementAt(i)%></td>
										<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
										<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
										<%-- <td align="center">
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
	   									<%}%>><%=VSEC_TYPE.elementAt(i) %></span></td> --%>
	   									<td align="center"><font style="background: <%=VBGCOLOR.elementAt(i)%>"><%=VREF_NO.elementAt(i) %></font></td>
										<%-- <td align="center"><%=VDEAL_NO.elementAt(i)%></td> --%>
										<td><%=VSECURITYDETAILS.elementAt(i) %></td>
									</tr>
									<%}%>
								<%} %>
								<%if(j<=0){ %>
									<tr><td colspan="8" align="center"><%=utilmsg.infoMessage("<b>Credit Rating/Limit Audit History Not Available for Selected Date!</b>") %></td></tr>
								<%}%>
							<%}else{ %>
								<tr><td colspan="8" align="center"><%=utilmsg.infoMessage("<b>Credit Rating/Limit Audit History Not Available for Selected Date!</b>") %></td></tr>
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