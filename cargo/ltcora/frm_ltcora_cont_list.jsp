<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,cont_type)
{
	window.opener.setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,cont_type);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.ltcora.DataBean_LtcoraMaster" id="ltcora" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>

<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String buy_sale=request.getParameter("buy_sale")==null?"":request.getParameter("buy_sale");
String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");

ltcora.setCallFlag("LTCORA_CONTRACT_LIST");
ltcora.setCounterparty_cd(counterparty_cd);
ltcora.setContract_type(contract_type);
ltcora.setComp_cd(owner_cd);
ltcora.setBuy_sale(buy_sale);
ltcora.setAgreement_type(agreement_type);
ltcora.init();

Vector VCOUNTERPARTY_CD = ltcora.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = ltcora.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = ltcora.getVCOUNTERPARTY_ABBR();
Vector VCONT_NO = ltcora.getVCONT_NO();
Vector VCONT_REV_NO = ltcora.getVCONT_REV_NO();
Vector VAGMT_NO = ltcora.getVAGMT_NO();
Vector VAGMT_REV_NO = ltcora.getVAGMT_REV_NO();
Vector VSTART_DT = ltcora.getVSTART_DT();
Vector VEND_DT = ltcora.getVEND_DT();
Vector VCONT_NAME = ltcora.getVCONT_NAME();
Vector VCONT_STATUS = ltcora.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = ltcora.getVCONT_STATUS_FLG();
Vector VCONT_REF_NO = ltcora.getVCONT_REF_NO();
Vector VDEAL_MAPPING = ltcora.getVDEAL_MAPPING();

 
 %>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	LTCORA CN/Period List
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
											<th>Counterparty<div align="center"><input class="form-control form-control-sm" type="text" id="counterparty" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Contact#<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contact" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Contact Rev#</th>
											<th>Contract Ref#<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract_Ref" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Contract Name</th>
											<th>Start - End Date</th>
											<th>Status<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Status" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div></th>
										</tr>
									</thead>
									<tbody>
									<%if(VCOUNTERPARTY_CD.size() > 0){ %>
										<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
										<tr>
											<td align="center"><input type="radio" onclick="setValue('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=contract_type%>')"></td>											
											<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
											<td align="center"><%=VDEAL_MAPPING.elementAt(i)%></td>
											<td align="center"><%=VCONT_REV_NO.elementAt(i)%></td>
											<td><%=VCONT_REF_NO.elementAt(i) %></td>
											<td><%=VCONT_NAME.elementAt(i) %></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>											
											<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="10" align="center">
												<%=utilmsg.infoMessage("<b>LTCORA CN/Period List not Available!</b>") %>
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