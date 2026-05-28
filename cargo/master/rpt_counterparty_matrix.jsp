<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function exportToXls()
{
	var url = "xls_counterparty_matrix.jsp?fileName=Counterparty_Matrix_Report.xls";

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
dbcounterpty.setCallFlag("COUNTERPARTY_MATRIX");
dbcounterpty.setComp_cd(owner_cd);
dbcounterpty.init();

Vector VCOUNTERPARTY_CD = dbcounterpty.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcounterpty.getVCOUNTERPARTY_ABBR();
Vector VENTITY_ROLE = dbcounterpty.getVENTITY_ROLE();
Vector VREQUESTER = dbcounterpty.getVREQUESTER();
Vector VAPPROVER = dbcounterpty.getVAPPROVER();
Vector VCOLOR = dbcounterpty.getVCOLOR();
Vector VCLEARANCE = dbcounterpty.getVCLEARANCE();
Vector VCATEGORY = dbcounterpty.getVCATEGORY();
Vector VSTATUS = dbcounterpty.getVSTATUS();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form>
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
					    	Counterparty Matrix
					    </div>
					    <div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
				   </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead id="tbsearch"> 
									<tr>
										<th>Counterparty</th>
										<th>ABBR</th>
										<th>Status</th>
										<th>Category</th>
										<th>Clearance</th>
										<th>Entity Role</th>
										<th>Entity Role Requested By</th>
										<th>Entity Role Approved By</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPARTY_CD.size() > 0){ %>
									<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){%>
										<tr>
											<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
											<td><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
											<td align="center">
												<div align="center">
													<font style="color:<%if(VSTATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
														<i class="fa fa-circle fa-lg" ></i>
														&nbsp;
													</font>
													<%if(VSTATUS.elementAt(i).equals("Y")){%>
													Active
													<%}else{ %>
													Deactivated
													<%} %>
												</div>
											</td>
											<td align="center"><%=VCATEGORY.elementAt(i)%></td>
											<td align="center"><%=VCLEARANCE.elementAt(i)%></td>											
											<td align="center"><span class="badge rounded-pill" style="background:<%=VCOLOR.elementAt(i)%>;color:black;font-size:12px;"><%=VENTITY_ROLE.elementAt(i)%></span></td>
											<td><%=VREQUESTER.elementAt(i)%></td>
											<td><%=VAPPROVER.elementAt(i)%></td>
										</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="8"><%=utilmsg.infoMessage("<b>No Counterparty Configured!</b>") %></td>
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
			$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
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