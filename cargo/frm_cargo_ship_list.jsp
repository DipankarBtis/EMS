<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(obj,ship_cd,cargo_no,cont_no,conf_volume,conf_price,start_dt,end_dt,counterparty_cd)
{
	window.opener.shipDtl(ship_cd,cargo_no,cont_no,conf_volume,conf_price,start_dt,end_dt,counterparty_cd);
	window.close();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.cargo.DataBean_Cargo_mst" id="dbcargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String operation = request.getParameter("operation")==null?"":request.getParameter("operation");

String cargo_number = request.getParameter("cargo_number")==null?"":request.getParameter("cargo_number");
String confirm_no = request.getParameter("confirm_no")==null?"":request.getParameter("confirm_no");
String mandate_conf_vol = request.getParameter("mandate_conf_vol")==null?"":request.getParameter("mandate_conf_vol");
String conf_price = request.getParameter("conf_price")==null?"":request.getParameter("conf_price");
String win_start_dt = request.getParameter("win_start_dt")==null?"":request.getParameter("win_start_dt");
String win_end_dt = request.getParameter("win_end_dt")==null?"":request.getParameter("win_end_dt");

dbcargo.setCallFlag("CARGO_NOMINATION_SHIP_DTL");
dbcargo.setCounterparty_cd(counterparty_cd);
dbcargo.setContract_type(contract_type);
dbcargo.setComp_cd(owner_cd);
dbcargo.init();

Vector VSHIP_CD = dbcargo.getVSHIP_CD();
Vector VSHIP_NAME = dbcargo.getVSHIP_NAME();
Vector VSHIP_CALL_SIGN = dbcargo.getVSHIP_CALL_SIGN();
Vector VSHIP_FLAG = dbcargo.getVSHIP_FLAG();
Vector VSHIP_IMO_NO = dbcargo.getVSHIP_IMO_NO();
Vector VSHIP_CLASS_SOC = dbcargo.getVSHIP_CLASS_SOC();
Vector VINMARSAT_NO = dbcargo.getVINMARSAT_NO();
Vector VSHIP_OWNER_NAME = dbcargo.getVSHIP_OWNER_NAME();
Vector VSHIP_OPERATOR_NAME = dbcargo.getVSHIP_OPERATOR_NAME();
Vector VSHIP_FAX_NO = dbcargo.getVSHIP_FAX_NO();
Vector VSHIP_TELEX_NO = dbcargo.getVSHIP_TELEX_NO();
Vector VSHIP_EMAIL = dbcargo.getVSHIP_EMAIL();
Vector VGROSS_TONNAGE = dbcargo.getVGROSS_TONNAGE();
Vector VCARGO_CAPACITY = dbcargo.getVCARGO_CAPACITY();
Vector VVOLUME_UNIT = dbcargo.getVVOLUME_UNIT();
Vector VPERCENTAGE_CAPACITY = dbcargo.getVPERCENTAGE_CAPACITY();
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
											<th >Select</th>
											<th >Vessel Name<br><input class="form-control form-control-sm" type="text" id="table_Vessel_name" onkeyup="Search(this,'1');" placeholder="Search.." style=""/></th>
											<th >Vessel Code<br><input class="form-control form-control-sm" type="text" id="table_Vessel_code" onkeyup="Search(this,'2');" placeholder="Search.." style=""/></th>
											<th >Vessel IMO No#<br><input class="form-control form-control-sm" type="text" id="table_Imo_no" onkeyup="Search(this,'3');" placeholder="Search.." style=""/></th>
											<th >INMARSAT No#<br><input class="form-control form-control-sm" type="text" id="table_Inmarsat" onkeyup="Search(this,'4');" placeholder="Search.." style=""/></th>
										</tr>
									</thead>
									<tbody>
									<%if(VSHIP_CD.size() > 0){ %>
										<%for(int i=0; i<VSHIP_CD.size(); i++){ %>
										<tr>
											<td align="center">
												<input type="radio" name="rdo" onclick="setValue(this,'<%=VSHIP_CD.elementAt(i)%>','<%=cargo_number%>','<%=confirm_no%>','<%=contract_type%>','<%=counterparty_cd%>')">
											</td>
											<td ><%=VSHIP_NAME.elementAt(i)%></td>
											<td ><%=VSHIP_CALL_SIGN.elementAt(i)%></td>
											<td ><%=VSHIP_IMO_NO.elementAt(i)%></td>
											<td ><%=VINMARSAT_NO.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="11" align="center">
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