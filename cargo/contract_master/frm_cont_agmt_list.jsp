<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(countpty_cd,agmt_no,agmt_rev_no,agmt_type,agmt_nm)
{
	window.opener.setAgmtDetail(countpty_cd,agmt_no,agmt_rev_no,agmt_type,agmt_nm);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_master.DataBean_ContractMaster" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String agreement_type = request.getParameter("agreement_type")==null?"F":request.getParameter("agreement_type");

contract.setCallFlag("AGREEMENT_LIST");
contract.setCounterparty_cd(counterparty_cd);
contract.setAgreement_type(agreement_type);
contract.setComp_cd(owner_cd);
contract.init();

Vector VBUYER_CD = contract.getVBUYER_CD();
Vector VBUYER_NAME = contract.getVBUYER_NAME();
Vector VAGMT_NO = contract.getVAGMT_NO();
Vector VAGMT_REV_NO = contract.getVAGMT_REV_NO();
Vector VSTART_DT = contract.getVSTART_DT();
Vector VEND_DT = contract.getVEND_DT();
Vector VCONT_NAME = contract.getVCONT_NAME();
Vector VCONT_STATUS = contract.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = contract.getVCONT_STATUS_FLG();
Vector VCONT_REF_NO = contract.getVCONT_REF_NO();
Vector VAGMT_BASE = contract.getVAGMT_BASE();
Vector VAGMT_TYPE = contract.getVAGMT_TYPE();
Vector VDISP_AGMT_NO = contract.getVDISP_AGMT_NO();
%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	Agreement List
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
											<td align="center"><input type="radio" onclick="setValue('<%=VBUYER_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=agreement_type%>','<%=VCONT_NAME.elementAt(i) %>')"></td>
											<td align="left"><%=VBUYER_NAME.elementAt(i)%></td>
											<td align="center"><%=VDISP_AGMT_NO.elementAt(i)%></td>
											<td align="center"><%=VAGMT_REV_NO.elementAt(i)%></td>
											<td><%=VCONT_REF_NO.elementAt(i) %></td>
											<td><%=VCONT_NAME.elementAt(i) %></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="center"><%=VAGMT_BASE.elementAt(i)%></td>
											<td align="center"><%=VAGMT_TYPE.elementAt(i)%></td>
											<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
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