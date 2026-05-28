<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_type)
{
	window.opener.setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_type);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="contract_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");

contract_mgmt.setCounterparty_cd(counterparty_cd);
contract_mgmt.setComp_cd(owner_cd);
contract_mgmt.setCallFlag("LEDGER_CONTRACT_LIST");
contract_mgmt.init();

Vector VCOUNTERPARTY_CD = contract_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = contract_mgmt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = contract_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCONT_NO = contract_mgmt.getVCONT_NO();
Vector VCONT_REV_NO = contract_mgmt.getVCONT_REV_NO();
Vector VAGMT_NO = contract_mgmt.getVAGMT_NO();
Vector VAGMT_REV_NO = contract_mgmt.getVAGMT_REV_NO();
Vector VTCQ = contract_mgmt.getVTCQ();
Vector VQTY_UNIT = contract_mgmt.getVQTY_UNIT();
Vector VSTART_DT = contract_mgmt.getVSTART_DT();
Vector VEND_DT = contract_mgmt.getVEND_DT();
Vector VCONT_NAME = contract_mgmt.getVCONT_NAME();
Vector VCONT_STATUS = contract_mgmt.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = contract_mgmt.getVCONT_STATUS_FLG();
Vector VCONT_REF_NO = contract_mgmt.getVCONT_REF_NO();
Vector VRATE_FORMULA = contract_mgmt.getVRATE_FORMULA();
Vector VIS_ALLOCATED = contract_mgmt.getVIS_ALLOCATED();
Vector VCONTRACT_TYPE = contract_mgmt.getVCONTRACT_TYPE();
Vector VDIS_CONT_MAPPING = contract_mgmt.getVDIS_CONT_MAPPING();
Vector VCONTRACT_TYPE_NM = contract_mgmt.getVCONTRACT_TYPE_NM();

%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	Supply Contract List
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover serchtbl" id="example0">
									<thead id="tbsearch0">
										<tr valign="top">
											<th>Select</th>
											<th class="tbser0">Customer</th>											
											<th class="tbser0">Contact#</th>
											<th class="tbser0">Contract Type</th>
											<th>Contract Rev#</th>
											<th class="tbser0">Contract/ Trade Ref#</th>											
											<th>Start - End Date</th>
											<th>Rate</th>
											<th>TCQ</th>
											<th>Allocation Status</th>
											<th class="tbser0">Status</th>
										</tr>
									</thead>
									<tbody>
									<%if(VCOUNTERPARTY_CD.size() > 0){ %>
										<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
										<tr>
											<td align="center">
												<input type="radio" 												
												onclick="setValue('<%=VCOUNTERPARTY_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>',
													'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
													'<%=VCONT_REV_NO.elementAt(i)%>','<%=VSTART_DT.elementAt(i)%>','<%=VEND_DT.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>')">
											</td>
											<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>																					
											<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i)%></td>
											<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i)%></td>
											<td align="center"><%=VCONT_REV_NO.elementAt(i)%></td>
											<td><%=VCONT_REF_NO.elementAt(i) %></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="right"><%=VRATE_FORMULA.elementAt(i)%></td>
											<td align="right"><%=VTCQ.elementAt(i)%>&nbsp;<%=VQTY_UNIT.elementAt(i)%></td>
											<td align="center">
												<%if(VIS_ALLOCATED.elementAt(i).equals("Y")){ %>
								    			<i class="fa fa-check-circle fa-2x" style="color:green;"></i>
								    			<%}else{ %>
								    			<i class="fa fa-times-circle fa-2x" style="color:red;"></i>
								    			<%} %>
											</td>
											<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="11" align="center">
												<%=utilmsg.infoMessage("<b>Supply Contact List is not Available!</b>") %>
											</td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</form>

<script>
$(document).ready(function() {	
	$('.serchtbl').each(function(k){
		$('#tbsearch'+k).each(function(j){						
			$('#tbsearch'+k+' th').each(function(i){
				var title = $(this).text();
				if($(this).hasClass('tbser'+k))
				{
					$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+''+k+'" onkeyup="Search(this,'+i+','+k+');" placeholder="Search '+title+'" style="width:100px"/></div>');
				}
			});		
		});
	});
});
	
function Search(obj, indx, tblid) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+tblid);
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	//tbody=tr[i].getElementsByTagName("tbody");alert(tbody)
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
</body>

</html>