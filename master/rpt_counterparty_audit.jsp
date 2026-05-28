<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_counterparty_audit.jsp?u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var url = "xls_counterparty_audit.jsp?fileName=Counterparty Modification Audit Report.xls&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt = request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

dbcounterpty.setCallFlag("COUNTERPARTY_AUDIT_REPORT");
dbcounterpty.setCounterparty_cd(counterparty_cd);
dbcounterpty.setFrom_dt(from_dt);
dbcounterpty.setTo_dt(to_dt);
dbcounterpty.init();

Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_DETAIL = dbcounterpty.getVCOUNTERPARTY_DETAIL();
Vector VCOUNTERPARTY_REGISTER_ADDRESS_DETAIL = dbcounterpty.getVCOUNTERPARTY_REGISTER_ADDRESS_DETAIL();
Vector VSTATUS = dbcounterpty.getVSTATUS();
Vector VLAST_UPDATE = dbcounterpty.getVLAST_UPDATE();
Vector VLAST_UPDATE_BY = dbcounterpty.getVLAST_UPDATE_BY();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form action="">

<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
							Counterparty Modification - Audit Report						
						</div>
						<div class="col-auto" onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
							  	</div>
								<div class="col-auto">
									<div class="input-group input-group-sm" >
				      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" maxLength="10" 
					      				onchange="validateDate(this);refresh();">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      				</div>
				      			</div>
				      			<div class="col-auto">
									<label class="form-label"><b>To</b></label>
							  	</div>
							  	<div class="col-auto">
				      				<div class="input-group input-group-sm" >
				      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" maxLength="10" 
					      				onchange="validateDate(this);refresh();">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      				</div>
				      			</div>
							</div>
						</div>
	      			</div>	      		
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Last Update</th>
										<th>
											Last Update By
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_LastUpdateBy" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>
											Counterparty
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Counterparty" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Counterparty Detail</th>
										<th>Counterparty Register Address & Contact Detail</th>
										<th>Status</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPARTY_DETAIL.size()>0){ %>
									<%for(int i=0;i<VCOUNTERPARTY_DETAIL.size(); i++){ %>
									<tr>
										<td align="center"><%=i+1%></td>
										<td align="center"><%=VLAST_UPDATE.elementAt(i)%></td>
										<td><%=VLAST_UPDATE_BY.elementAt(i)%></td>
										<td><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
										<td><%=VCOUNTERPARTY_DETAIL.elementAt(i)%></td>
										<td><%=VCOUNTERPARTY_REGISTER_ADDRESS_DETAIL.elementAt(i)%></td>
										<td><%=VSTATUS.elementAt(i) %></td>
									</tr>
									<%} %>
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

</form>
</body>
<script>
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example");
  	
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