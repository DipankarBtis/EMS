<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<jsp:useBean class="com.etrm.fms.contract_master.DataBean_ContractMaster" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
<%
String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
String nomination_freq=request.getParameter("nomination_freq")==null?"W":request.getParameter("nomination_freq");
String nomination_type=request.getParameter("nomination_type")==null?"B":request.getParameter("nomination_type");

contract.setCallFlag("NOM_CONTRACT_LIST");
contract.setGas_dt(gas_dt);
contract.setNomination_freq(nomination_freq);
contract.setComp_cd(owner_cd);
contract.setNomination_type(nomination_type);
contract.init();

Vector VBUYER_CD = contract.getVBUYER_CD();
Vector VBUYER_NAME = contract.getVBUYER_NAME();
Vector VBUYER_ABBR = contract.getVBUYER_ABBR();
Vector VCONT_NO = contract.getVCONT_NO();
Vector VCONT_REV_NO = contract.getVCONT_REV_NO();
Vector VAGMT_NO = contract.getVAGMT_NO();
Vector VAGMT_REV_NO = contract.getVAGMT_REV_NO();
Vector VTCQ = contract.getVTCQ();
Vector VQTY_UNIT = contract.getVQTY_UNIT();
Vector VSTART_DT = contract.getVSTART_DT();
Vector VEND_DT = contract.getVEND_DT();
Vector VCONT_NAME = contract.getVCONT_NAME();
Vector VCONT_STATUS = contract.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = contract.getVCONT_STATUS_FLG();
Vector VFCC_FLAG = contract.getVFCC_FLAG();
Vector VCONT_REF_NO = contract.getVCONT_REF_NO();
Vector VRATE_FORMULA = contract.getVRATE_FORMULA();
Vector VIS_ALLOCATED = contract.getVIS_ALLOCATED();
Vector VCONTRACT_TYPE = contract.getVCONTRACT_TYPE();
Vector VSUPPLIED_MMBTU = contract.getVSUPPLIED_MMBTU();
Vector VDIS_CONT_MAPPING = contract.getVDIS_CONT_MAPPING(); 
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
								<table class="table table-bordered" id="example">
									<thead>
										<tr valign="top">
											<th>Select</th>
											<th>Customer<br><input class="form-control form-control-sm" type="text" id="table_Customer" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></th>											
											<th>Contact#<br><input class="form-control form-control-sm" type="text" id="table_Contact" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></th>
											<th>Contract Type<br><input class="form-control form-control-sm" type="text" id="table_Contact_type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></th>
											<th>Contract Rev#</th>
											<th>Contract/ Trade Ref#<br><input class="form-control form-control-sm" type="text" id="table_Contract_Ref" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></th>											
											<th>Start - End Date</th>
											<th>Rate</th>
											<th>TCQ</th>
											<th>Allocation Status</th>
											<th>Status<br><input class="form-control form-control-sm" type="text" id="table_Status" onkeyup="Search(this,'10');" placeholder="Search.." style="width:100px"/></th>
											<th>Supplied MMBTU</th>
										</tr>
									</thead>
									<tbody>
									<%if(VBUYER_CD.size() > 0){ %>
										<%for(int i=0; i<VBUYER_CD.size(); i++){ 
											String cont_type_nm =  utilBean.getContractTypeName(""+VCONTRACT_TYPE.elementAt(i));
										%>
										<tr>
											<td align="center">
												<input type="radio" <%if(!VCONT_STATUS_FLG.elementAt(i).equals("Y") && !VFCC_FLAG.elementAt(i).equals("Y")){ %>disabled<%}else if(!VIS_ALLOCATED.elementAt(i).equals("Y")){%>disabled<%} %>
												onclick="setValue('<%=VBUYER_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>',
													'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
													'<%=VCONT_REV_NO.elementAt(i)%>','<%=VSTART_DT.elementAt(i)%>','<%=VEND_DT.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>')">
											</td>
											<td align="center" title="<%=VBUYER_NAME.elementAt(i)%>"><%=VBUYER_ABBR.elementAt(i)%></td>																					
											<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i)%></td>
											<td align="center"><%=cont_type_nm%></td>
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
											<td align="right"><%=VSUPPLIED_MMBTU.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="13" align="center">
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