<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(counterparty_cd, agmt_type, agmt_no, contract_type, cont_no, cargo_no, ship_cd, opration)
{
	window.opener.cargoDtl(counterparty_cd, agmt_type, agmt_no, contract_type, cont_no, cargo_no, ship_cd, opration);
	window.close();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.cargo.DataBean_Cargo_mst" id="dbcargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String operation = request.getParameter("operation")==null?"":request.getParameter("operation");

dbcargo.setCallFlag("NOM_CARGO_LIST");
dbcargo.setCounterparty_cd(counterparty_cd);
dbcargo.setOpration(operation);
dbcargo.setComp_cd(owner_cd);
dbcargo.init();

Vector VCONTPARTY_CD = dbcargo.getVCONTPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcargo.getVCOUNTERPARTY_NM();
Vector VAGMT_TYPE = dbcargo.getVAGMT_TYPE();
Vector VAGMT_NO = dbcargo.getVAGMT_NO();
Vector VSTART_DT = dbcargo.getVSTART_DT();
Vector VRATE_UNIT = dbcargo.getVRATE_UNIT();
Vector VRATE = dbcargo.getVRATE();
Vector VCARGO_QTY = dbcargo.getVCARGO_QTY();
Vector VCARGO_STATUS = dbcargo.getVCARGO_STATUS();
Vector VCARGO_REF = dbcargo.getVCARGO_REF();
Vector VCARGO_NO = dbcargo.getVCARGO_NO();
Vector VCONT_NO = dbcargo.getVCONT_NO();
Vector VCONTRACT_TYPE = dbcargo.getVCONTRACT_TYPE();
Vector VEND_DT = dbcargo.getVEND_DT();
Vector VAGMT_BASE_NM = dbcargo.getVAGMT_BASE_NM();
Vector VAGMT_BASE = dbcargo.getVAGMT_BASE();
Vector <String> VNOMINATION_STATUS = dbcargo.getVNOMINATION_STATUS();
Vector VNOMINATION_REV_NO = dbcargo.getVNOMINATION_REV_NO();
Vector VSHIP_NAME = dbcargo.getVSHIP_NAME();
Vector VSHIP_CD = dbcargo.getVSHIP_CD();
Vector VDISP_CARGO_NO = dbcargo.getVDISP_CARGO_NO();
Vector VCONT_DISP_NAME = dbcargo.getVCONT_DISP_NAME();
Vector VTOTAL_BOE_NO = dbcargo.getVTOTAL_BOE_NO();
Vector VTOTAL_BL_NO = dbcargo.getVTOTAL_BL_NO();
Vector VCONT_REF_NO = dbcargo.getVCONT_REF_NO();
 
int numPend = 0;
int numNom = 0;

for (String element : VNOMINATION_STATUS)
{
	if (element.equals("Pending"))
	{
    	numPend++;
	} 
	else if (element.equals("Nominated"))
	{
		numNom++;
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
				    	Confirmed Cargo List
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
											<th>Counterparty</th>											
											<th >Cargo No.<br><input class="form-control form-control-sm" type="text" id="table_Contract_Ref" onkeyup="Search(this,'2');" placeholder="Search.." style=""/></th>
											<th >Cargo Ref#<br><input class="form-control form-control-sm" type="text" id="table_Contact" onkeyup="Search(this,'3');" placeholder="Search.." style=""/></th>
											<th >Cargo Status</th>
											<th >CN Ref#</th>
											<th >Cargo Delivery Window<br>(From-To)</th>
											<th >Confirmed MMBTU</th>
											
											<th >Confirmed Price</th>
											<th >Price Unit</th>
											<th >Delivery Type</th>
											<th >Nomination Status</th>
										</tr>
									</thead>
									<tbody>
									<%if(numPend > 0){ %>
										<%for(int i=0; i<VCONTPARTY_CD.size(); i++){ %>
											<%if(VNOMINATION_STATUS.elementAt(i).equals("Pending")){ %>
											<tr>
												<td align="center">
													<input type="radio" name="rdo" onclick="setValue('<%=VCONTPARTY_CD.elementAt(i)%>','<%=VAGMT_TYPE.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCARGO_NO.elementAt(i)%>','<%=VSHIP_CD.elementAt(i)%>','INSERT')">
												</td>
												<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
												<td align="center"><%=VDISP_CARGO_NO.elementAt(i)%></td>
												<td align="center"><%=VCARGO_REF.elementAt(i)%></td>
																								
												<td align="center">
													<div align="center">
													<font style="color:<%if(VCARGO_STATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
														<i class="fa fa-circle fa-lg" ></i>
														&nbsp;
													</font>
													<%if(VCARGO_STATUS.elementAt(i).equals("Y")){%>
													Confirmed
													<%}else{ %>
													Not-Confirmed
													<%} %>
												</div>
												</td>
												<td align="center"><%=VCONT_REF_NO.elementAt(i) %></td>
												<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
												<td align="right"><%=VCARGO_QTY.elementAt(i)%></td>
												<td align="right"><%=VRATE.elementAt(i)%></td>
												<td align="center"> <%if(VRATE_UNIT.elementAt(i).equals("2")){%>USD/MMBTU<%}else{ %>INR/MMBTU<%} %></td>
												<td align="center"><%=VAGMT_BASE_NM.elementAt(i)%></td>
												<td align="center"><%=VNOMINATION_STATUS.elementAt(i) %></td>
											</tr>
											<%} %>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="12" align="center">
												<%=utilmsg.infoMessage("<b>Confirmed Cargo List is not Available!</b>") %>
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
				    	Nominated Cargo List
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example1">
									<thead>
										<tr valign="top">
											<th >Select</th>
											<th>Counterparty</th>
											<th >Cargo No.<br><input class="form-control form-control-sm" type="text" id="table_cargo_no" onkeyup="Search(this,'2');" placeholder="Search.." style=""/></th>
											<th >Cargo Ref#<br><input class="form-control form-control-sm" type="text" id="table_cargo_ref" onkeyup="Search(this,'3');" placeholder="Search.." style=""/></th>		
											<th >Vessel Name<br><input class="form-control form-control-sm" type="text" id="table_vessel_nm" onkeyup="Search(this,'4');" placeholder="Search.." style=""/></th>									
											<th >Cargo Status</th>
											<th >CN Ref#</th>
											<th >Cargo Delivery Window<br>(From-To)</th>
											<th >Confirmed MMBTU</th>
											<th >Confirmed Price</th>
											<th >Price Unit</th>
											
											<th >Nomination Status</th>
											<th>#BL</th>
											<th>#BOE</th>
										</tr>
									</thead>
									<tbody>
									<%if(numNom > 0){ %>
										<%for(int i=0; i<VCONTPARTY_CD.size(); i++){ %>
											<%if(VNOMINATION_STATUS.elementAt(i).equals("Nominated")){ %>
											<tr>
												<td align="center">
													<input type="radio" name="rdo" onclick="setValue('<%=VCONTPARTY_CD.elementAt(i)%>','<%=VAGMT_TYPE.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCARGO_NO.elementAt(i)%>','<%=VSHIP_CD.elementAt(i)%>','MODIFY')">
												</td>
												<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
												<td align="center"><%=VDISP_CARGO_NO.elementAt(i)%></td>
												<td align="center"><%=VCARGO_REF.elementAt(i)%></td>	
												<td align="left"><%=VSHIP_NAME.elementAt(i) %></td>											
												<td align="center">
													<div align="center">
													<font style="color:<%if(VCARGO_STATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
														<i class="fa fa-circle fa-lg" ></i>
														&nbsp;
													</font>
													<%if(VCARGO_STATUS.elementAt(i).equals("Y")){%>
													Confirmed
													<%}else{ %>
													Not-Confirmed
													<%} %>
												</div>
												</td>
												<td align="center"><%=VCONT_REF_NO.elementAt(i) %></td>
												<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
												<td align="right"><%=VCARGO_QTY.elementAt(i)%></td>
												
												<td align="right"><%=VRATE.elementAt(i)%></td>
												<td align="center"> <%if(VRATE_UNIT.elementAt(i).equals("2")){%>USD/MMBTU<%}else{ %>INR/MMBTU<%} %></td>
												
												<td align="center"><%=VNOMINATION_STATUS.elementAt(i) %> (rev <%=VNOMINATION_REV_NO.elementAt(i) %> )</td>
												<td align="right"><%=VTOTAL_BL_NO.elementAt(i) %></td>
												<td align="right"><%=VTOTAL_BOE_NO.elementAt(i) %></td>
											</tr>
											<%} %>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="14" align="center">
												<%=utilmsg.infoMessage("<b>Nominated Cargo List is not Available!</b>") %>
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
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example1");
  	
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