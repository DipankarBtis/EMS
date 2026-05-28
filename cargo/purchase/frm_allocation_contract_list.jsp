<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_cargo,cargo_no,cont_type)
{
	window.opener.setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_cargo,cargo_no,cont_type);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DataBean_Trader_Contract_Mst" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");

if(clearance.equals("IGX"))
{
	contract_type="I";
}
else
{
	contract_type="D";
}

purchase.setCallFlag("TRADER_CONT_LIST");
purchase.setCounterparty_cd(counterparty_cd);
purchase.setContract_type(contract_type);
purchase.setComp_cd(owner_cd);
purchase.setClearance(clearance);
purchase.init();

Vector VBUYER_CD = purchase.getVBUYER_CD();
Vector VBUYER_NAME = purchase.getVBUYER_NAME();
Vector VCONT_NO = purchase.getVCONT_NO();
Vector VCONT_REV_NO = purchase.getVCONT_REV_NO();
Vector VAGMT_NO = purchase.getVAGMT_NO();
Vector VAGMT_REV_NO = purchase.getVAGMT_REV_NO();
Vector VTCQ = purchase.getVTCQ();
Vector VQTY_UNIT = purchase.getVQTY_UNIT();
Vector VSTART_DT = purchase.getVSTART_DT();
Vector VEND_DT = purchase.getVEND_DT();
Vector VCONT_NAME = purchase.getVCONT_NAME();
Vector VCONT_STATUS = purchase.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = purchase.getVCONT_STATUS_FLG();
Vector VFCC_FLAG = purchase.getVFCC_FLAG();
Vector VCONT_REF_NO = purchase.getVCONT_REF_NO();
Vector VRECEIVED_QTY = purchase.getVRECEIVED_QTY();
Vector VCONT_CARGO = purchase.getVCONT_CARGO();
Vector VCARGO_NO = purchase.getVCARGO_NO();
Vector VCONTRACT_TYPE = purchase.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = purchase.getVCONTRACT_TYPE_NM();
Vector VDISP_CONT_NO = purchase.getVDISP_CONT_NO();

%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	Trader Contract List
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
											<th>Contact#<br><input class="form-control form-control-sm" type="text" id="table_Contact" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></th>
											<th>Contract Type<br><input class="form-control form-control-sm" type="text" id="table_Contact_type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></th>
											<th><%if(contract_type.equals("I")){ %>Trade Ref#<%}else{%>Contract Ref#<%} %><br><input class="form-control form-control-sm" type="text" id="table_Contract_Ref" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></th>
											<th>Contract Name</th>
											<th>Start - End Date</th>
											<th>TCQ</th>
											<th>Received Qty</th>
											<th>Status<br><input class="form-control form-control-sm" type="text" id="table_Status" onkeyup="Search(this,'9');" placeholder="Search.." style="width:100px"/></th>
										</tr>
									</thead>
									<tbody>
									<%if(VBUYER_CD.size() > 0){ %>
										<%for(int i=0; i<VBUYER_CD.size(); i++){ %>
										<tr>
											<td align="center">
												<input type="radio" <%if(!VCONT_STATUS_FLG.elementAt(i).equals("Y") && !VFCC_FLAG.elementAt(i).equals("Y")){ %>disabled<%} %>
												onclick="setValue('<%=VBUYER_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>'
												,'<%=VCONT_REV_NO.elementAt(i)%>','<%=VSTART_DT.elementAt(i)%>','<%=VEND_DT.elementAt(i)%>'
												,'<%=VCONT_CARGO.elementAt(i)%>','<%=VCARGO_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>')">
											</td>
											<td align="center"><%=VBUYER_NAME.elementAt(i)%></td>
											<td align="center"><%=VDISP_CONT_NO.elementAt(i)%></td>
											<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i)%></td>
											<td><%=VCONT_REF_NO.elementAt(i) %></td>
											<td><%=VCONT_NAME.elementAt(i) %></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="right"><%=VTCQ.elementAt(i)%>&nbsp;<%=VQTY_UNIT.elementAt(i)%></td>
											<td align="right"><%=VRECEIVED_QTY.elementAt(i)%></td>
											<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="9" align="center">
												<%=utilmsg.infoMessage("<b>Trader Contact List is not Available!</b>") %>
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