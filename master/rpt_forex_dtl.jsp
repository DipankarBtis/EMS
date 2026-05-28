<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ page import="java.util.*" %>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var u = document.forms[0].u.value;
	
	var flag=true;
	var msg="";
	var flag=checkDateRangeOnApply(document.forms[0].from_dt,document.forms[0].to_dt);
	if(flag)
	{
		if(trim(from_dt)!="" && trim(to_dt)!="")
		{
			if(flag==true)
			{
				var url="rpt_forex_dtl.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&u="+u;
				document.getElementById("loading").style.visibility = "visible";
				location.replace(url);
			}
		}
	}
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var sysdate = document.forms[0].sysdate.value;
	var sysdate1 = sysdate.replace("/","_");
	
	var fileName = "ForexDtl_"+from_dt +"-"+ to_dt +".xls";
	
	var url="xls_forex_dtl.jsp?fileName="+fileName+"&from_dt="+from_dt+"&to_dt="+to_dt;
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DB_Master_Report" id="mst_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();


String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

mst_rpt.setCallFlag("EXCH_DETAIL");
mst_rpt.setFrom_dt(from_dt);
mst_rpt.setTo_dt(to_dt);
mst_rpt.init();

Vector VEXCH_NM = mst_rpt.getVEXCH_NM();
Vector VEXCH_CD = mst_rpt.getVEXCH_CD();
Vector VEFF_DT = mst_rpt.getVEFF_DT();
Vector VMULTI_EXCH = mst_rpt.getVMULTI_EXCH();

Vector V1 =(Vector)((Vector)((Vector)VMULTI_EXCH.elementAt(0)));
Vector V2 =(Vector)((Vector)((Vector)VMULTI_EXCH.elementAt(1)));
Vector V3 =(Vector)((Vector)((Vector)VMULTI_EXCH.elementAt(2)));
%>
<body>
<%@ include file="../home/header.jsp"%>
<form action="">
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
					    	Foreign Exchange Report
					    </div>
				    	<div class="d-flex justify-content-between">
						    <div class="col-auto">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x" onclick="exportToXls();" ></i></span>
							</div>&nbsp;&nbsp;
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
					  	<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filters" onclick="refresh();">
					  			</div>
					  		</div>
					  	</div>
					  	<div class="col-md-2 col-sm-0 col-xs-0"></div>
					</div>
				</div>
				<div class="card-body cdbody">
				    <div class="row">
				      	<div class="table-responsive">
				       		<table class="table table-bordered table-hover ">
						         <thead>
							         <tr>
								         <th>Date</th>
								         <%for (int i=0;i<VEXCH_NM.size();i++){%>
								         <th><%=VEXCH_NM.elementAt(i) %></th>
								         <%} %>
							         </tr>
						         </thead>
				         		<tbody>
							    <%int K=0; 
							    for(int i=0;i<VEFF_DT.size();i++){ 
								K+=1; %> 
									<tr>
										<td align="center"><%=VEFF_DT.elementAt(i) %></td>
										<td align="right"><%=V1.elementAt(i) %></td>
										<td align="right"><%=V2.elementAt(i) %></td>
										<td align="right"><%=V3.elementAt(i) %></td>
									</tr>
								<%}%>
								</tbody> 
				       		</table>
			      		</div>
				    </div>
	  			</div>
	  		</div>
	  	</div>
	</div>
</div>
	  	
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="comp_abbr" value="<%=owner_abbr%>">

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


<script>
function Search(obj, indx, j) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+j);
  	
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
</form>
</html>
