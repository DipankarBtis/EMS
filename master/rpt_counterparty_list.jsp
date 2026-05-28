<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(obj,counterparty_cd)
{
	window.opener.refreshChild(counterparty_cd);
	window.close();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
dbcounterpty.setCallFlag("COUNTERPARTY_LIST");
dbcounterpty.setComp_abbr(owner_abbr);
dbcounterpty.init();

Vector VCOUNTERPARTY_CD = dbcounterpty.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcounterpty.getVCOUNTERPARTY_ABBR();
Vector VSTATUS = dbcounterpty.getVSTATUS();
Vector VCLEARANCE = dbcounterpty.getVCLEARANCE();

%>
<body>
<form action="">

<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
							Counterparty Details
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Select</th>
										<th>
											Abbreviation
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_abbr" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>
											Counterparty
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_counterparty" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>
											Status
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_status" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>
											Clearance
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_clearance" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div>
										</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPARTY_CD.size() > 0){ %>
									<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
									<tr>
										<td width="5px" align="center">
											<input type="radio" name="rdo" onclick="setValue(this,'<%=VCOUNTERPARTY_CD.elementAt(i)%>')">
										</td>
										<td><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
										<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
										<td align="center">
											<div align="center">
												<font style="color:
												<%if(VSTATUS.elementAt(i).equals("Y")){%>#a6ff4d
												<%}else if(VSTATUS.elementAt(i).equals("N")){%>red
												<%}else{%>yellow
												<%}%>"
												>
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font>
												<%if(VSTATUS.elementAt(i).equals("Y")){%>
												Active
												<%}else if(VSTATUS.elementAt(i).equals("N")){ %>
												Deactivated
												<%}else{ %>
												E-Rate
												<%} %>
											</div>
										</td>
										<td align="center"><%=VCLEARANCE.elementAt(i)%></td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="5">
											<%=utilmsg.infoMessage("<b>No Counterparty Configured!</b>") %>
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