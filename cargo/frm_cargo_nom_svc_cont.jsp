<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(cont_full_no,entity_type,entity_name,ref_no,cont_map_no)
{
	document.forms[0].cont_full_no.value=cont_full_no;
	document.forms[0].entity_type.value=entity_type;
	document.forms[0].entity_name.value=entity_name;
	document.forms[0].ref_no.value=ref_no;
	document.forms[0].cont_map_no.value=cont_map_no;
	
	document.forms[0].isSelected.value = "Y";
}

function doProceed()
{
	var isSelected = document.forms[0].isSelected.value;
	
	var cont_full_no = document.forms[0].cont_full_no.value;
	var entity_type = document.forms[0].entity_type.value;
	var entity_name = document.forms[0].entity_name.value;
	var ref_no = document.forms[0].ref_no.value;
	var cont_map_no = document.forms[0].cont_map_no.value;
	
	if(isSelected == "Y")
	{
		var a = confirm("Do you want to proceed?");
		
		if(a)
		{
			alert("Submit contract after proceeding.");
			
			window.opener.setContractDetail(cont_full_no,entity_type,entity_name,ref_no,cont_map_no);
			window.close();
		}
	}
	else
	{
		alert("Please select any contract to proceed!!");
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.cargo.DB_cargo_service_cont_master" id="db_cargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String entity_type = request.getParameter("entity_type")==null?"":request.getParameter("entity_type");
String cargo_startdt = request.getParameter("cargo_startdt")==null?"":request.getParameter("cargo_startdt");
String cargo_enddt = request.getParameter("cargo_enddt")==null?"":request.getParameter("cargo_enddt");

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
db_cargo.setCargo_startdt(cargo_startdt);
db_cargo.setCargo_enddt(cargo_enddt);
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
Vector VCONT_MAP_NO = db_cargo.getVCONT_MAP_NO();

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
											<th>Counterparty<br><input class="form-control form-control-sm" type="text" id="table_counterparty" onkeyup="Search(this,'1');" placeholder="Search.." /></th>
											<th>Contract#<br><input class="form-control form-control-sm" type="text" id="table_Contact" onkeyup="Search(this,'2');" placeholder="Search.." /></th>
											<th>Contract Ref#<br><input class="form-control form-control-sm" type="text" id="table_Contract_Ref" onkeyup="Search(this,'3');" placeholder="Search.." /></th>
											<th>Contract Name<br><input class="form-control form-control-sm" type="text" id="table_Contact_name" onkeyup="Search(this,'4');" placeholder="Search.." /></th>
											<th>Start - End Date</th>
											<!-- <th>Linked Cargo</th> -->
											<th>Status<br><input class="form-control form-control-sm" type="text" id="table_Status" onkeyup="Search(this,'6');" placeholder="Search.."/></th>
										</tr>
									</thead>
									<tbody>
									<%if(VCOUNTERPARTY_CD.size() > 0){ %>
										<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
										<tr>
											<td align="center"><input type="radio" 
											onclick="setValue('<%=VCONT_FULL_NO.elementAt(i)%>','<%=entity_type%>','<%=VCOUNTERPARTY_NM.elementAt(i)%>','<%=VCONT_REF_NO.elementAt(i) %>','<%=VCONT_MAP_NO.elementAt(i)%>')"></td>
											<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
											<td align="center"><%=VCONT_FULL_NO.elementAt(i)%></td>
											<td><%=VCONT_REF_NO.elementAt(i) %></td>
											<td><%=VCONT_NAME.elementAt(i) %></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<%-- <td align="center"><%=VCONT_LINKED_CARGO.elementAt(i)%></td> --%>
											<% if(VCONT_STATUS.elementAt(i).equals("Y")){%>
												<td align="center">Active</td>
											<%} else{%>
												<td align="center">In-active</td>
											<%} %>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="7" align="center">
												<%=utilmsg.infoMessage("<b>No Contract is Available for "+entity_name+"!</b>") %>
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
						<input type="button" class="btn btn-warning com-btn" value="Proceed" onclick="doProceed();">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="isSelected" value="">

<input type="hidden" name="cont_full_no" value="">
<input type="hidden" name="cont_map_no" value="">
<input type="hidden" name="entity_name" value="">
<input type="hidden" name="entity_type" value="">
<input type="hidden" name="ref_no" value="">

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