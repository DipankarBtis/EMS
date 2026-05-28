<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script type="text/javascript">
function refresh()
{
	var from_dt=document.forms[0].from_dt.value;
	var to_dt=document.forms[0].to_dt.value;
	
	var u = document.forms[0].u.value;
	
	if(trim(from_dt)!="" &&  trim(to_dt)!="")
	{
		var url = "rpt_sys_error.jsp?u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.admin.DB_Admin_Report" id="dbadminRpt" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateutil" scope="page"></jsp:useBean>
<%

String firstDtOfMth = ""+dateutil.getFirstDateOfMonth();
String sysdate = ""+dateutil.getSysdate();

String from_dt=request.getParameter("from_dt")==null?firstDtOfMth:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

dbadminRpt.setCallFlag("SYSTEM_ERROR");
dbadminRpt.setFrom_dt(from_dt);
dbadminRpt.setTo_dt(to_dt);
dbadminRpt.init();

Vector VERROR_CD = dbadminRpt.getVERROR_CD();
Vector VERROR_ENT_DT = dbadminRpt.getVERROR_ENT_DT();
Vector VERROR_SRC_FILE = dbadminRpt.getVERROR_SRC_FILE();
Vector VERROR_FUNC_NM = dbadminRpt.getVERROR_FUNC_NM();
Vector VERROR_MSG = dbadminRpt.getVERROR_MSG();
Vector VERROR_STACK_TRACE = dbadminRpt.getVERROR_STACK_TRACE();
%>
<body>
<%@ include file="../home/header.jsp"%>

<form>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader topheader">
				    <div class="d-flex justify-content-between">
						<div class="topheader">
						    System Error Trail
						</div>
					    <a href="../admin/xls_sys_error.jsp?from_dt=<%=from_dt %>&to_dt=<%=to_dt %>" >
						 	<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</a>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);checkDateRange(this,document.forms[0].to_dt);" onchange="validateDate(this);checkDateRange(this,document.forms[0].to_dt);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);checkDateRange(document.forms[0].from_dt,this);" onchange="validateDate(this);checkDateRange(document.forms[0].from_dt,this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered" id="search_by_filter">
							<thead>
								<tr>
									<th>Error#</th>
									<th>Entry On<br><div align="center"><input class="form-control form-control-sm" type="text" id="Ent_dt" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Source File<br><div align="center"><input class="form-control form-control-sm" type="text" id="Source_File" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Function<br><div align="center"><input class="form-control form-control-sm" type="text" id="Function" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Error Msg<br><div align="center"><input class="form-control form-control-sm" type="text" id="Err_Msg" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Stack Trace<br><div align="center"><input class="form-control form-control-sm" type="text" id="Stack_Trace" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>									
								</tr>
							</thead>
							<tbody id="mainTbody">
							<%if (VERROR_CD.size()>0) { %>
								<%for(int i=0; i<VERROR_CD.size(); i++){ %>
									<tr>
										<td><div align="center"><%=VERROR_CD.elementAt(i)%></div></td>
										<td><div align="center"><%=VERROR_ENT_DT.elementAt(i)%></div></td>
										<td><div ><%=VERROR_SRC_FILE.elementAt(i)%></div></td>
										<td><div><%=VERROR_FUNC_NM.elementAt(i)%></div></td>
										<td><div><%=VERROR_MSG.elementAt(i)%></div></td>
										<td><div><%=VERROR_STACK_TRACE.elementAt(i)%></div></td>
									</tr>
								<%} %>
							<%}else{ %>
								<tr>
									<td colspan="6"><div align="center">
										<%=utilmsg.infoMessage("<b>No System Error Captured!</b>") %></div>
									</td>
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
</body>

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
</html>