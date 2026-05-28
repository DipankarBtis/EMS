
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(cargo_no,cont_no,contract_type,counterparty_cd,ship_cd,ship_name,cargo_name,cont_name,arrival_dt,cont_rev,agmt_no,agmt_rev,agmt_type,allocation_status,conf_volume)
{
	window.opener.setArrivalValue(cargo_no,cont_no,contract_type,counterparty_cd,ship_cd,ship_name,cargo_name,cont_name,arrival_dt,cont_rev,agmt_no,agmt_rev,agmt_type,allocation_status,conf_volume);
	window.close();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.cargo.DataBean_Cargo_mst" id="cargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String operation = request.getParameter("operation")==null?"":request.getParameter("operation");

cargo.setCallFlag("CARGO_ARRIVAL_LIST");
cargo.setCounterparty_cd(counterparty_cd);
cargo.setContract_type(contract_type);
cargo.setComp_cd(owner_cd);
cargo.init();

String num_bl=cargo.getNum_bl();
String num_boe=cargo.getNum_boe();
Vector VBUYER_CD = cargo.getVBUYER_CD();
Vector VBUYER_NAME = cargo.getVBUYER_NAME();
Vector VCONT_NO = cargo.getVCONT_NO();
Vector VCONT_REV_NO = cargo.getVCONT_REV_NO();
Vector VAGMT_NO = cargo.getVAGMT_NO();
Vector VAGMT_REV_NO = cargo.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = cargo.getVCONTRACT_TYPE();
Vector VAGMT_TYPE = cargo.getVAGMT_TYPE();
Vector VCARGO_NO = cargo.getVCARGO_NO();
Vector VCARGO_REF = cargo.getVCARGO_REF();
Vector VSHIP_CD = cargo.getVSHIP_CD();
Vector VSHIP_NAME = cargo.getVSHIP_NAME();
Vector VCARGO_QTY = cargo.getVCARGO_QTY();
Vector VRATE = cargo.getVRATE();
Vector VRATE_UNIT = cargo.getVRATE_UNIT();
Vector VSTART_DT = cargo.getVSTART_DT();
Vector VCARGO_NAME= cargo.getVCARGO_NAME();
Vector VCONT_NAME = cargo.getVCONT_NAME();
Vector VAGMT_BASE= cargo.getVAGMT_BASE();
Vector VBL_CNT=cargo.getVBL_CNT();
Vector VBOE_CNT = cargo.getVBOE_CNT();
Vector VCARGO_STATUS = cargo.getVCARGO_STATUS();
Vector VFCC_FLAG =  cargo.getVFCC_FLAG();
Vector VALLOC_REV_NO = cargo.getVALLOC_REV_NO();
Vector VALLOC_STATUS = cargo.getVALLOC_STATUS();
Vector VTOTAL_BOE_NO = cargo.getVTOTAL_BOE_NO();
Vector VTOTAL_BL_NO = cargo.getVTOTAL_BL_NO();
Vector VCONT_REF_NO = cargo.getVCONT_REF_NO();
Vector <String> VALLOCATION_STATUS = cargo.getVALLOCATION_STATUS();

int numPend = 0;
int numAlloc = 0;
int numCustom = 0;

for (String element : VALLOCATION_STATUS)
{
	if (element.equals("Pending"))
	{
    	numPend++;
	} 
	else if (element.equals("Allocated"))
	{
		numAlloc++;
	}
	else if (element.equals("Custom"))
	{
		numCustom++;
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
						Nominated Cargo List
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
											<th >Counterparty</th>
											<th >Cargo No.</th>
											<th >Cargo Ref#<br><input class="form-control form-control-sm" type="text" id="table_Contact" onkeyup="Search(this,'2');" placeholder="Search.." style=""/></th>
											<th >Vessel's Name</th>
											<th >Cargo Status</th>
											<th >CN Ref#</th>
											<th >Arrival Date<br><input class="form-control form-control-sm" type="text" id="table_Contract_Ref" onkeyup="Search(this,'3');" placeholder="Search.." style=""/></th>
											<th >Confirmed Volume</th>
											<th >Confirmed Price</th>
											<th >Price Unit</th>
											<th >Delivery Type</th>
											<th>Arrival Status</th>
											<th>#BL</th>
											<th>#BOE</th>
										</tr>
									</thead>
									<tbody>
									<%if(numPend > 0 && !num_bl.equals("0") && !num_boe.equals("0")){ %>
										<%for(int i=0; i<VCARGO_NAME.size(); i++){ %>
											<%if(VALLOCATION_STATUS.elementAt(i).equals("Pending") && VCARGO_STATUS.elementAt(i).equals("Y") && VFCC_FLAG.elementAt(i).equals("Y")){ %>
											<tr>
												<td align="center">
													<input type="radio" name="rdo" onclick="setValue('<%=VCARGO_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VBUYER_CD.elementAt(i)%>','<%=VSHIP_CD.elementAt(i)%>','<%=VSHIP_NAME.elementAt(i)%>','<%=VCARGO_NAME.elementAt(i)%>',
													'<%=VCONT_NAME.elementAt(i)%>','<%=VSTART_DT.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VAGMT_TYPE.elementAt(i)%>','<%=VALLOCATION_STATUS.elementAt(i)%>','<%=VCARGO_QTY.elementAt(i) %>')">
												</td>
												<td align="left"><%=VBUYER_NAME.elementAt(i) %></td>
												<td align="center"><%=VCARGO_NAME.elementAt(i) %></td>
												<td><%=VCARGO_REF.elementAt(i) %></td>
												<td><%=VSHIP_NAME.elementAt(i) %></td>
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
												<td><%=VCONT_REF_NO.elementAt(i) %></td>
												<td align="center"><%=VSTART_DT.elementAt(i) %></td>
												<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" value="<%=VCARGO_QTY.elementAt(i) %>" style="text-align:right" readonly>	
													</div>
												</td>
												<td align="right"><%=VRATE.elementAt(i) %></td>
												<td align="center"> <%if(VRATE_UNIT.elementAt(i).equals("2")){%>USD/MMBTU<%}else{ %>INR/MMBTU<%} %></td>
												<td align="center"><%=VAGMT_BASE.elementAt(i) %></td>
												<td align="center"><%=VALLOC_STATUS.elementAt(i) %> (Rev <%=VALLOC_REV_NO.elementAt(i) %>)</td>
												<td align="center"><%=VTOTAL_BL_NO.elementAt(i) %></td>
												<td align="center"><%=VTOTAL_BOE_NO.elementAt(i) %></td>
											</tr>
											<%} %>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="15" align="center">
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
			</div>&nbsp;
			
			
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
						Custom Cargo List
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
											<th >Counterparty</th>
											<th >Cargo No.</th>
											<th >Cargo Ref#<br><input class="form-control form-control-sm" type="text" id="table_Contact" onkeyup="Search(this,'2');" placeholder="Search.." style=""/></th>
											<th >Vessel's Name</th>
											<th >Cargo Status</th>
											<th >CN Ref#</th>
											<th >Arrival Date<br><input class="form-control form-control-sm" type="text" id="table_Contract_Ref" onkeyup="Search(this,'3');" placeholder="Search.." style=""/></th>
											<th >Confirmed Volume</th>
											<th >Confirmed Price</th>
											<th >Price Unit</th>
											<th >Delivery Type</th>
											<th>Arrival Status</th>
											<th>#BL</th>
											<th>#BOE</th>
										</tr>
									</thead>
									<tbody>
									<%if(numCustom > 0 && !num_bl.equals("0") && !num_boe.equals("0")){ %>
										<%for(int i=0; i<VCARGO_NAME.size(); i++){ %>
											<%if(VALLOCATION_STATUS.elementAt(i).equals("Custom") && VCARGO_STATUS.elementAt(i).equals("Y") && VFCC_FLAG.elementAt(i).equals("Y")){ %>
											<tr>
												<td align="center">
													<input type="radio" name="rdo" onclick="setValue('<%=VCARGO_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VBUYER_CD.elementAt(i)%>','<%=VSHIP_CD.elementAt(i)%>','<%=VSHIP_NAME.elementAt(i)%>','<%=VCARGO_NAME.elementAt(i)%>',
													'<%=VCONT_NAME.elementAt(i)%>','<%=VSTART_DT.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VAGMT_TYPE.elementAt(i)%>','<%=VALLOCATION_STATUS.elementAt(i)%>','<%=VCARGO_QTY.elementAt(i) %>')">
												</td>
												<td align="left"><%=VBUYER_NAME.elementAt(i) %></td>
												<td align="center"><%=VCARGO_NAME.elementAt(i) %></td>
												<td><%=VCARGO_REF.elementAt(i) %></td>
												<td><%=VSHIP_NAME.elementAt(i) %></td>
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
												<td><%=VCONT_REF_NO.elementAt(i) %></td>
												<td align="center"><%=VSTART_DT.elementAt(i) %></td>
												<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" value="<%=VCARGO_QTY.elementAt(i) %>" style="text-align:right" readonly>	
													</div>
												</td>
												<td align="right"><%=VRATE.elementAt(i) %></td>
												<td align="center"> <%if(VRATE_UNIT.elementAt(i).equals("2")){%>USD/MMBTU<%}else{ %>INR/MMBTU<%} %></td>
												<td align="center"><%=VAGMT_BASE.elementAt(i) %></td>
												<td align="center"><%=VALLOC_STATUS.elementAt(i) %> (Rev <%=VALLOC_REV_NO.elementAt(i) %>)</td>
												<td align="center"><%=VTOTAL_BL_NO.elementAt(i) %></td>
												<td align="center"><%=VTOTAL_BOE_NO.elementAt(i) %></td>
											</tr>
											<%} %>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="15" align="center">
												<%=utilmsg.infoMessage("<b>Custom Cargo List is not Available!</b>") %>
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
						Arrived Cargo List
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
											<th >Counterparty</th>
											<th >Cargo No.</th>
											<th >Cargo Ref#<br><input class="form-control form-control-sm" type="text" id="table_Contact" onkeyup="Search(this,'2');" placeholder="Search.." style=""/></th>
											<th >Vessel's Name</th>
											<th >Cargo Status</th>
											<th >CN Ref#</th>
											<th >Arrival Date<br><input class="form-control form-control-sm" type="text" id="table_Contract_Ref" onkeyup="Search(this,'3');" placeholder="Search.." style=""/></th>
											<th >Confirmed Volume</th>
											<th >Confirmed Price</th>
											<th >Price Unit</th>
											<th >Delivery Type</th>
											<th>Arrival Status</th>
											<th>#BL</th>
											<th>#BOE</th>
										</tr>
									</thead>
									<tbody>
									<%if(numAlloc > 0){ %>
										<%for(int i=0; i<VCARGO_NAME.size(); i++){ %>
											<%if(VALLOCATION_STATUS.elementAt(i).equals("Allocated")){ %>
											<tr>
												<td align="center">
													<input type="radio" name="rdo" onclick="setValue('<%=VCARGO_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VBUYER_CD.elementAt(i)%>','<%=VSHIP_CD.elementAt(i)%>','<%=VSHIP_NAME.elementAt(i)%>','<%=VCARGO_NAME.elementAt(i)%>',
													'<%=VCONT_NAME.elementAt(i)%>','<%=VSTART_DT.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VAGMT_TYPE.elementAt(i)%>','<%=VALLOCATION_STATUS.elementAt(i)%>','<%=VCARGO_QTY.elementAt(i) %>')">
												</td>
												<td align="left"><%=VBUYER_NAME.elementAt(i) %></td>
												<td align="center"><%=VCARGO_NAME.elementAt(i) %></td>
												<td><%=VCARGO_REF.elementAt(i) %></td>
												<td><%=VSHIP_NAME.elementAt(i) %></td>
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
												<td><%=VCONT_REF_NO.elementAt(i) %></td>
												<td align="center"><%=VSTART_DT.elementAt(i) %></td>
												<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" value="<%=VCARGO_QTY.elementAt(i) %>" style="text-align:right" readonly>	
													</div>
												</td>
												<td align="right"><%=VRATE.elementAt(i) %></td>
												<td align="center"> <%if(VRATE_UNIT.elementAt(i).equals("2")){%>USD/MMBTU<%}else{ %>INR/MMBTU<%} %></td>
												<td align="center"><%=VAGMT_BASE.elementAt(i) %></td>
												<td align="center"><%=VALLOC_STATUS.elementAt(i) %> (Rev <%=VALLOC_REV_NO.elementAt(i) %>)</td>
											    <td align="center"><%=VTOTAL_BL_NO.elementAt(i) %></td>
												<td align="center"><%=VTOTAL_BOE_NO.elementAt(i) %></td>
											</tr>
											<%} %>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="15" align="center">
												<%=utilmsg.infoMessage("<b>Arrived Cargo List is not Available!</b>") %>
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