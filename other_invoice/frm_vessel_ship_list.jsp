<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(obj,ship_cd,ship_nm,ship_flag,ship_item)
{
	window.opener.shipDtl(ship_cd,ship_nm,ship_flag,ship_item);
	window.close();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.other_invoice.DataBean_Other_Invoice" id="dbcargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%

dbcargo.setCallFlag("VESSEL_SHIP_DTL");
dbcargo.setComp_cd(owner_cd);
dbcargo.init();

Vector VSHIP_CD = dbcargo.getVSHIP_CD();
Vector VSHIP_NAME = dbcargo.getVSHIP_NAME();
Vector VSHIP_FLAG = dbcargo.getVSHIP_FLAG();
Vector VSHIP_ITEM = dbcargo.getVSHIP_ITEM();
%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
						Vessel Details
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
											<th>Vessel Name<br><input class="form-control form-control-sm" type="text" id="table_Vessel_name" onkeyup="Search(this,'1');" placeholder="Search.." style=""/></th>
											<th>Vessel Flag<br><input class="form-control form-control-sm" type="text" id="table_Vessel_flag" onkeyup="Search(this,'2');" placeholder="Search.." style=""/></th>
											<th>Vessel Item<br><input class="form-control form-control-sm" type="text" id="table_item" onkeyup="Search(this,'3');" placeholder="Search.." style=""/></th>
										</tr>
									</thead>
									<tbody>
									<%if(VSHIP_CD.size() > 0){ %>
										<%for(int i=0; i<VSHIP_CD.size(); i++){ %>
										<tr>
											<td align="center">
												<input type="radio" name="rdo" onclick="setValue(this,'<%=VSHIP_CD.elementAt(i)%>','<%=VSHIP_NAME.elementAt(i)%>','<%=VSHIP_FLAG.elementAt(i)%>','<%=VSHIP_ITEM.elementAt(i)%>')">
											</td>
											<td ><%=VSHIP_NAME.elementAt(i)%></td>
											<td ><%=VSHIP_FLAG.elementAt(i)%></td>
											<td ><%=VSHIP_ITEM.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="4" align="center">
												<%=utilmsg.infoMessage("<b>Ship List is not Available!</b>") %>
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