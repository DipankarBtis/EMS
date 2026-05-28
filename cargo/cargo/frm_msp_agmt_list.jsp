<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no)
{
	window.opener.setAgmtDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.cargo.DataBean_Cargo_mst" id="db_cargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");

db_cargo.setCallFlag("MSP_AGMT_LIST");
db_cargo.setCounterparty_cd(counterparty_cd);
db_cargo.setAgreement_type(agreement_type);
db_cargo.setComp_cd(owner_cd);
db_cargo.init();

Vector VBUYER_CD = db_cargo.getVBUYER_CD();
Vector VBUYER_NAME = db_cargo.getVBUYER_NAME();
Vector VAGMT_NO = db_cargo.getVAGMT_NO();
Vector VAGMT_FULL_NO = db_cargo.getVAGMT_FULL_NO();
Vector VAGMT_REV_NO = db_cargo.getVAGMT_REV_NO();
Vector VAGMT_REF_NO = db_cargo.getVAGMT_REF_NO();
Vector VAGMT_NAME = db_cargo.getVAGMT_NAME();
Vector VSTART_DT = db_cargo.getVSTART_DT();
Vector VEND_DT = db_cargo.getVEND_DT();
Vector VAGMT_BASE = db_cargo.getVAGMT_BASE();
Vector VAGMT_TYP = db_cargo.getVAGMT_TYP();
Vector VAGMT_STATUS = db_cargo.getVAGMT_STATUS();

%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	MSPA List
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
											<th>Counterparty<br><div align="center"><input class="form-control form-control-sm" type="text" id="counterparty" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Agreement#<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contact" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Agreement Rev#</th>
											<th>Agreement Ref#<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract_Ref" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Agreement Name</th>
											<th>Start - End Date</th>
											<th>Agreement Base</th>
											<th>Agreement Type</th>
											<th>Status<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Status" onkeyup="Search(this,'9');" placeholder="Search.." style="width:100px"/></div></th>
										</tr>
									</thead>
									<tbody>
									<%if(VBUYER_CD.size() > 0){ %>
										<%for(int i=0; i<VBUYER_CD.size(); i++){ %>
										<tr>
											<td align="center"><input type="radio" onclick="setValue('<%=VBUYER_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=agreement_type%>')"></td>
											<td><%=VBUYER_NAME.elementAt(i)%></td>
											<td align="center"><%=VAGMT_FULL_NO.elementAt(i)%></td>
											<td align="center"><%=VAGMT_REV_NO.elementAt(i)%></td>
											<td><%=VAGMT_REF_NO.elementAt(i) %></td>
											<td><%=VAGMT_NAME.elementAt(i) %></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="center"><%=VAGMT_BASE.elementAt(i)%></td>
											<td align="center"><%=VAGMT_TYP.elementAt(i)%></td>
											<td align="center"><%=VAGMT_STATUS.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="10" align="center">
												<%=utilmsg.infoMessage("<b>Agreement List is not Available!</b>") %>
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