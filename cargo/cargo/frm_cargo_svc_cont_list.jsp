<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(entity_type,countpty_cd,cont_full_no,cont_no,cont_linked_cargo,cont_linked_cargo_name)
{
	window.opener.setContractDetail(entity_type,countpty_cd,cont_full_no,cont_no,cont_linked_cargo,cont_linked_cargo_name);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.cargo.DB_cargo_service_cont_master" id="db_cargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String entity_type = request.getParameter("entity_type")==null?"":request.getParameter("entity_type");

String entity_name="";
String contract_type="";

if(entity_type.equals("S"))
{
	entity_name="Surveyor Agent";
	contract_type="Y";
}
else if(entity_type.equals("V"))
{
	entity_name="Vessel Agent";
	contract_type="A";
}
else if(entity_type.equals("H"))
{
	entity_name="Custom House Agent";
	contract_type="H";
}

db_cargo.setCallFlag("CARGO_SVC_CONT_LIST");
db_cargo.setCounterparty_cd(counterparty_cd);
db_cargo.setEntity_role(entity_type);
db_cargo.setContract_type(contract_type);
db_cargo.setComp_cd(owner_cd);
db_cargo.init();

Vector VCOUNTERPARTY_CD = db_cargo.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = db_cargo.getVCOUNTERPARTY_NM();
Vector VCONT_FULL_NO = db_cargo.getVCONT_FULL_NO();
Vector VCONT_LINKED_CARGO = db_cargo.getVCONT_LINKED_CARGO();
Vector VCONT_NO = db_cargo.getVCONT_NO();
Vector VSTART_DT = db_cargo.getVSTART_DT();
Vector VEND_DT = db_cargo.getVEND_DT();
Vector VCONT_NAME = db_cargo.getVCONT_NAME();
Vector VCONT_DISP_NAME = db_cargo.getVCONT_DISP_NAME();
Vector VCONT_STATUS = db_cargo.getVCONT_STATUS();
Vector VCONT_REF_NO = db_cargo.getVCONT_REF_NO();
Vector VCONT_LINKED_CARGO_NAME = db_cargo.getVCONT_LINKED_CARGO_NAME();
%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	<%=entity_name %> Contract List 
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr valign="top">
											<th>Select</th>
											<th>Counterparty</th>
											<th>Contract#<br><input class="form-control form-control-sm" type="text" id="table_Contact" onkeyup="Search(this,'2');" placeholder="Search.." /></th>
											<th>Contract Ref#<br><input class="form-control form-control-sm" type="text" id="table_Contract_Ref" onkeyup="Search(this,'3');" placeholder="Search.." /></th>
											<th>Contract Name</th>
											<th>Start - End Date</th>
											<th>Linked Cargo</th>
											<th>Status<br><input class="form-control form-control-sm" type="text" id="table_Status" onkeyup="Search(this,'7');" placeholder="Search.."/></th>
										</tr>
									</thead>
									<tbody>
									<%if(VCOUNTERPARTY_CD.size() > 0){ %>
										<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
										<tr>
											<td align="center"><input type="radio" 
											onclick="setValue('<%=entity_type%>',
											'<%=VCOUNTERPARTY_CD.elementAt(i)%>',
											'<%=VCONT_FULL_NO.elementAt(i)%>',
											'<%=VCONT_NO.elementAt(i)%>',
											'<%=VCONT_LINKED_CARGO.elementAt(i)%>',
											'<%=VCONT_LINKED_CARGO_NAME.elementAt(i)%>')"></td>
											<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
											<td align="center"><%=VCONT_FULL_NO.elementAt(i)%></td>
											<td><%=VCONT_REF_NO.elementAt(i) %></td>
											<td><%=VCONT_NAME.elementAt(i) %></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="center"><%=VCONT_LINKED_CARGO.elementAt(i)%></td>
											<% if(VCONT_STATUS.elementAt(i).equals("Y")){%>
												<td align="center">Active</td>
											<%} else{%>
												<td align="center">In-active</td>
											<%} %>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="8" align="center">
												<%=utilmsg.infoMessage("<b>Contract List is not Available!</b>") %>
											</td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="" align="right">
						<input type="button" class="btn btn-warning com-btn" value="Close" onclick="window.close();">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</form>
</body>

<script>
$(document).ready(function() {
	
	$('#tbsearch th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "Sr#")
		{
			$(this).html('<div align="center"><input class="form-control form-control-sm" type="text" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
		}
		else
		{
			$(this).html('<div align="center"><input class="form-control form-control-sm" type="text" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:100px"/></div>');
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