<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_ltcora_cn_cargo_dtl.jsp?u="+u+"&month="+month+"&year="+year;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function exportToXls()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var url = "xls_ltcora_cn_cargo_dtl.jsp?fileName=LTCORA CN Cargo Details.xls&month="+month+"&year="+year;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.accounting.DataBean_Accounting" id="accounting" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String str_month =""+currentMonth;
if(str_month.length()==1)
{
	str_month = "0"+str_month;
}

String month=request.getParameter("month")==null?""+str_month:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");

accounting.setCallFlag("LTCORA_CN_CARGO_RPT");
accounting.setComp_cd(owner_cd);
accounting.setMonth(month);
accounting.setYear(year);
accounting.init();

Vector VCOUNTERPARTY_CD = accounting.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = accounting.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = accounting.getVCOUNTERPARTY_NM();

Vector VSHIP_CD = accounting.getVSHIP_CD();
Vector VSUPP_CD = accounting.getVSUPP_CD();
Vector VCARGO_REF_NO = accounting.getVCARGO_REF_NO();
Vector VSHIP_NM = accounting.getVSHIP_NM();
Vector VSUPP_NM = accounting.getVSUPP_NM();
Vector VACTUAL_RECPT_DT = accounting.getVACTUAL_RECPT_DT();
Vector VTOTAL_ADQ_QTY = accounting.getVTOTAL_ADQ_QTY();

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
					    	LTCORA CN Cargo Details
					    </div>
					   	<div class="d-flex justify-content-between">
					   		 <div onclick="exportToXls();" style="color:green;">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
							</div>&nbsp;
					   	</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="d-flex justify-content-center">	
							<div class="col-auto">
								<div class="form-group row">
									<label class="form-label"><b>Month/Year</b></label>
						  		</div>
							</div>&nbsp;&nbsp;&nbsp;
							<div class="col-sm-2 col-xs-2 col-md-2">
								<div class="form-group row">
						  			<div class="col">
						  				<select class="form-select form-select-sm" name="month" onchange="refresh();">
											<option value="01" label="January">January</option>
											<option value="02" label="February">February</option>
											<option value="03" label="March">March</option>
											<option value="04" label="April">April</option>
											<option value="05" label="May">May</option>
											<option value="06" label="June">June</option>
											<option value="07" label="July">July</option>
											<option value="08" label="August">August</option>
											<option value="09" label="September">September</option>
											<option value="10" label="October">October</option>
											<option value="11" label="November">November</option>
											<option value="12" label="December">December</option>
										</select>
										<script>document.forms[0].month.value="<%=month%>"</script>
									</div>
									<div class="col">
						  				<select class="form-select form-select-sm" name="year" onchange="refresh();">
						  					<%for(int i=(currentYear+1); i > (currentYear-10);i--){ %>
												<option value="<%=i%>"><%=i%></option>
											<%} %>
										</select>
										<script>document.forms[0].year.value="<%=year%>"</script>
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
								<thead id="tbsearch">
									<tr>
										<th>Sr#</th>
										<th>Customer Name</th>
										<th>Cargo Ref#</th>
										<th>Vessel Name</th>
										<th>Supplier Name</th>
										<th>Actual Receipt Date</th>
										<th>Actual Unloaded Qty <br>(MMBTU)</th>
									</tr>
								</thead>
								<tbody>
								<%int i=0,k=0;
								if(VCOUNTERPARTY_CD.size() > 0){ %>
									<%for(i=0; i<VCOUNTERPARTY_CD.size(); i++){ 
									k+=1;
									%>
									<tr>
										<td align="center"><%=k%></td>
										<td align="center" title="<%=VCOUNTERPARTY_ABBR.elementAt(i) %>"><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
										<td align="center"><%=VCARGO_REF_NO.elementAt(i) %></td>
										<td align="center"><%=VSHIP_NM.elementAt(i) %></td>
										<td align="center"><%=VSUPP_NM.elementAt(i) %></td>
										<td align="center"><%=VACTUAL_RECPT_DT.elementAt(i) %></td>
										<td align="center"><%=VTOTAL_ADQ_QTY.elementAt(i) %></td>
									</tr>
									<%} %>
								<%}else{%>
									<tr>
										<td colspan="7" align="center"><%=utilmsg.infoMessage("<b>No Cargo Data Available!</b>") %></td>
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
$(document).ready(function() {
	
	$('#tbsearch th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "Sr#")
		{
			//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
		}
		else
		{
			$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:100px"/></div>');
		}
	});
	
});

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
    	//tbody=tr[i].getElementsByTagName("tbody");alert(tbody)
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