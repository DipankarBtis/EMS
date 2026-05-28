<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(obj,vendor_cd)
{
	window.opener.refreshChild(vendor_cd);
	window.close();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.other_invoice.DataBean_Other_Invoice" id="dbOtherInvoice" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");

dbOtherInvoice.setCallFlag("VENDOR_LIST");
dbOtherInvoice.setComp_abbr(owner_abbr);
dbOtherInvoice.setEntity_role(entity_role);
dbOtherInvoice.init();

Vector VENDOR_CD = dbOtherInvoice.getVVENDOR_CD();
Vector VENDOR_NM = dbOtherInvoice.getVENDOR_NM();
Vector VENDOR_ABBR = dbOtherInvoice.getVENDOR_ABBR();
Vector VINVOICE_TYPE = dbOtherInvoice.getVINVOICE_TYPE();

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
						<%if(entity_role.equals("V")) {%>
							Vendor Details
						<%} else {%>
							Supplier Details
						<%} %>
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
											<%if(entity_role.equals("V")) {%>
												Vendor
											<%} else {%>
												Supplier
											<%} %>
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_vendor" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<%if(entity_role.equals("V")) {%>
											<th>
												Invoice Type
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_invoice_type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
											</th>
										<%} %>
									</tr>
								</thead>
								<tbody>
								<%if(VENDOR_CD.size() > 0){ %>
									<%for(int i=0;i<VENDOR_CD.size();i++){ %>
									<tr>
										<td width="5px" align="center">
											<input type="radio" name="rdo" onclick="setValue(this,'<%=VENDOR_CD.elementAt(i)%>')">
										</td>
										<td><%=VENDOR_ABBR.elementAt(i)%></td>
										<td><%=VENDOR_NM.elementAt(i)%></td>
										<%if(entity_role.equals("V")) {%>
											<td align="center"><%=VINVOICE_TYPE.elementAt(i)%></td>
										<%} %>	
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="5" align="center">
										<%if(entity_role.equals("V")) {%>
											<%=utilmsg.infoMessage("<b>No Vendor Configured!</b>") %>
										<%} else {%>
											<%=utilmsg.infoMessage("<b>No Supplier Configured!</b>") %>
										<%} %>
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