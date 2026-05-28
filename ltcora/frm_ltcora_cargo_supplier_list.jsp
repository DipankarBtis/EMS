<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(obj,trader_cd,trader_name,trader_abbr,id_no)
{
	window.opener.supplierDtl(trader_cd,trader_name,trader_abbr,id_no);
	window.close();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.ltcora.DataBean_LtcoraMaster" id="dbltcora" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String id_no = request.getParameter("id_no")==null?"":request.getParameter("id_no");
String operation = request.getParameter("operation")==null?"":request.getParameter("operation");

dbltcora.setCallFlag("LTCORA_TRADER_DTL");
dbltcora.setComp_cd(owner_cd);
dbltcora.init();

Vector VCOUNTERPARTY_CD = dbltcora.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbltcora.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbltcora.getVCOUNTERPARTY_ABBR();
%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
						Trader Details
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr valign="top">
											<th >Select</th>
											<th >Trader ABBR<br><input class="form-control form-control-sm" type="text" id="table_trader_abbr" onkeyup="Search(this,'1');" placeholder="Search.." style=""/></th>
											<th >Trader Name<br><input class="form-control form-control-sm" type="text" id="table_trader_name" onkeyup="Search(this,'2');" placeholder="Search.." style=""/></th>
										</tr>
									</thead>
									<tbody>
									<%if(VCOUNTERPARTY_CD.size() > 0){ %>
										<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
										<tr>
											<td align="center">
												<input type="radio" name="rdo" onclick="setValue(this,'<%=VCOUNTERPARTY_CD.elementAt(i)%>',
												'<%=VCOUNTERPARTY_NM.elementAt(i)%>','<%=VCOUNTERPARTY_ABBR.elementAt(i)%>','<%=id_no%>')">
											</td>
											<td ><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
											<td ><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="3" align="center">
												<%=utilmsg.infoMessage("<b>Trader List is not Available!</b>") %>
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