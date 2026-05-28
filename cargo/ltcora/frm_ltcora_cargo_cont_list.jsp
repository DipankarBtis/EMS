<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(countpty_cd,cont_no,cont_rev_no,cont_type,disp_cont_no,cont_ref_no,cont_name,no_cargo,agmt_no,contract_type,agmt_rev,agmt_type,buy_sell,start_dt,end_dt,sug_per)
{
	window.opener.setContDetail(countpty_cd,cont_no,cont_rev_no,cont_type,disp_cont_no,cont_ref_no,cont_name,no_cargo,agmt_no,contract_type,agmt_rev,agmt_type,buy_sell,start_dt,end_dt,sug_per);
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
ltcora.setComp_cd(owner_cd);
ltcora.init();

Vector VCOUNTERPARTY_CD = ltcora.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = ltcora.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = ltcora.getVCOUNTERPARTY_ABBR();
Vector VCONT_NO = ltcora.getVCONT_NO();
Vector VCONT_REV_NO = ltcora.getVCONT_REV_NO();
Vector VAGMT_NO = ltcora.getVAGMT_NO();
Vector<String> VCONT_TYPE = ltcora.getVCONT_TYPE();
Vector VAGMT_REV_NO = ltcora.getVAGMT_REV_NO();
Vector VAGMT_TYPE = ltcora.getVAGMT_TYPE();
Vector VSTART_DT = ltcora.getVSTART_DT();
Vector VEND_DT = ltcora.getVEND_DT();
Vector VCONT_NAME = ltcora.getVCONT_NAME();
Vector VCONT_STATUS = ltcora.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = ltcora.getVCONT_STATUS_FLG();
Vector VCONT_CARGO_NO = ltcora.getVCONT_CARGO_NO();
Vector VCONT_REF_NO = ltcora.getVCONT_REF_NO();
Vector VDEAL_MAPPING = ltcora.getVDEAL_MAPPING();
Vector VBUY_SELL = ltcora.getVBUY_SELL();
Vector VSUG_PER = ltcora.getVSUG_PER();

int numPeriod = 0;
int numCn = 0;

for (String element : VCONT_TYPE)
{
	if (element.equals("P"))
	{
		numPeriod++;
	} 
	else if (element.equals("G"))
	{
		numCn++;
	}
}

%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	LTCORA CN List
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
											<th>Contact#<br><input class="form-control form-control-sm" type="text" id="table_Contact" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></th>
											<th>Contact Rev#</th>
											<th>Contract Ref#<br><input class="form-control form-control-sm" type="text" id="table_Contract_Ref" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></th>
											<th>Contract Name</th>
											<th>Start - End Date</th>
											<th>No. of Cargo</th>
											<th>Status<br><input class="form-control form-control-sm" type="text" id="table_Status" onkeyup="Search(this,'8');" placeholder="Search.." style="width:100px"/></th>
										</tr>
									</thead>
									<tbody>
									<%if(numCn> 0){ %>
										<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
										<%if(VCONT_TYPE.elementAt(i).equals("G")){ %>
										<tr>
											<td align="center"><input type="radio" onclick="setValue('<%=VCOUNTERPARTY_CD.elementAt(i) %>'
											,'<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>'
											,'<%=contract_type%>','<%=VDEAL_MAPPING.elementAt(i)%>'
											,'<%=VCONT_REF_NO.elementAt(i)%>','<%=VCONT_NAME.elementAt(i)%>',
											'<%=VCONT_CARGO_NO.elementAt(i)%>',
											'<%=VAGMT_NO.elementAt(i)%>',
											'<%=VCONT_TYPE.elementAt(i)%>',
											'<%=VAGMT_REV_NO.elementAt(i)%>',
											'<%=VAGMT_TYPE.elementAt(i)%>',
											'<%=VBUY_SELL.elementAt(i)%>',
											'<%=VSTART_DT.elementAt(i)%>',
											'<%=VEND_DT.elementAt(i)%>',
											'<%=VSUG_PER.elementAt(i)%>')" <%if(!VCONT_STATUS.elementAt(i).equals("Approved")){ %>disabled title="Contract is not Approved." <%} %>></td>											
											<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
											<td align="center"><%=VDEAL_MAPPING.elementAt(i)%></td>
											<td align="center"><%=VCONT_REV_NO.elementAt(i)%></td>
											<td><%=VCONT_REF_NO.elementAt(i) %></td>
											<td><%=VCONT_NAME.elementAt(i) %></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>	
											<td align="center"><%=VCONT_CARGO_NO.elementAt(i)%></td>											
											<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
										</tr>
										<%} %>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="10" align="center">
												<%=utilmsg.infoMessage("<b>LTCORA CN List not Available!</b>") %>
											</td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>&nbsp;
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	LTCORA Period List
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
											<th>Contact#<br><input class="form-control form-control-sm" type="text" id="table_Contact" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></th>
											<th>Contact Rev#</th>
											<th>Contract Ref#<br><input class="form-control form-control-sm" type="text" id="table_Contract_Ref" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></th>
											<th>Contract Name</th>
											<th>Start - End Date</th>
											<th>No. of Cargo</th>
											<th>Status<br><input class="form-control form-control-sm" type="text" id="table_Status" onkeyup="Search(this,'8');" placeholder="Search.." style="width:100px"/></th>
										</tr>
									</thead>
									<tbody>
									<%if(numPeriod > 0){ %>
										<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
										<%if(VCONT_TYPE.elementAt(i).equals("P")){ %>
										<tr>
											<td align="center"><input type="radio" onclick="setValue('<%=VCOUNTERPARTY_CD.elementAt(i) %>'
											,'<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>'
											,'<%=contract_type%>','<%=VDEAL_MAPPING.elementAt(i)%>'
											,'<%=VCONT_REF_NO.elementAt(i)%>','<%=VCONT_NAME.elementAt(i)%>',
											'<%=VCONT_CARGO_NO.elementAt(i)%>',
											'<%=VAGMT_NO.elementAt(i)%>',
											'<%=VCONT_TYPE.elementAt(i)%>',
											'<%=VAGMT_REV_NO.elementAt(i)%>',
											'<%=VAGMT_TYPE.elementAt(i)%>',
											'<%=VBUY_SELL.elementAt(i)%>',
											'<%=VSTART_DT.elementAt(i)%>',
											'<%=VEND_DT.elementAt(i)%>',
											'<%=VSUG_PER.elementAt(i)%>')" <%if(!VCONT_STATUS.elementAt(i).equals("Approved")){ %>disabled title="Contract is not Approved." <%} %>></td>											
											<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
											<td align="center"><%=VDEAL_MAPPING.elementAt(i)%></td>
											<td align="center"><%=VCONT_REV_NO.elementAt(i)%></td>
											<td><%=VCONT_REF_NO.elementAt(i) %></td>
											<td><%=VCONT_NAME.elementAt(i) %></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>	
											<td align="center"><%=VCONT_CARGO_NO.elementAt(i)%></td>											
											<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
										</tr>
										<%} %>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="10" align="center">
												<%=utilmsg.infoMessage("<b>LTCORA Period List not Available!</b>") %>
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