
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(id_no,cargo_ref,ship_cd,ship_name,buyer_cd,buyer_nm,start_dt,end_dt,act_arrv_dt,cargo_qty,qq_qty_mmbtu,csoc_mmbtu,qq_qty_cer_no,qq_qty_cer_dt,no_win_days,cargo_mapping)
{
	alert("Selected cargo will reset the previous attached Cargo details!!");
	
	window.opener.arrivedCargoDtl(id_no,cargo_ref,ship_cd,ship_name,buyer_cd,buyer_nm,start_dt,end_dt,act_arrv_dt,cargo_qty,qq_qty_mmbtu,csoc_mmbtu,qq_qty_cer_no,qq_qty_cer_dt,no_win_days,cargo_mapping);
	window.close();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.ltcora.DataBean_LtcoraMaster" id="ltcora" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utilDate" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String operation = request.getParameter("operation")==null?"":request.getParameter("operation");
String id_no = request.getParameter("id_no")==null?"":request.getParameter("id_no");
String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");

ltcora.setCallFlag("LTCORA_CARGO_ARRIVAL_LIST");
ltcora.setCounterparty_cd(counterparty_cd);
ltcora.setContract_type(contract_type);
ltcora.setComp_cd(owner_cd);
ltcora.setFrom_dt(from_dt);
ltcora.setTo_dt(to_dt);
ltcora.init();

//String num_bl=ltcora.getNum_bl();
//String num_boe=ltcora.getNum_boe();
Vector VBUYER_CD = ltcora.getVBUYER_CD();
Vector VBUYER_NAME = ltcora.getVBUYER_NAME();
Vector VCONT_NO = ltcora.getVCONT_NO();
Vector VCONT_REV_NO = ltcora.getVCONT_REV_NO();
Vector VAGMT_NO = ltcora.getVAGMT_NO();
Vector VAGMT_REV_NO = ltcora.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = ltcora.getVCONTRACT_TYPE();
Vector VAGMT_TYPE = ltcora.getVAGMT_TYPE();
Vector VCARGO_NO = ltcora.getVCARGO_NO();
Vector VCARGO_REF = ltcora.getVCARGO_REF();
Vector VSHIP_CD = ltcora.getVSHIP_CD();
Vector VSHIP_NAME = ltcora.getVSHIP_NAME();
Vector VCARGO_QTY = ltcora.getVCARGO_QTY();
Vector VRATE = ltcora.getVRATE();
Vector VRATE_UNIT = ltcora.getVRATE_UNIT();
Vector VSTART_DT = ltcora.getVSTART_DT();
Vector VEND_DT = ltcora.getVEND_DT();
Vector VCARGO_NAME= ltcora.getVCARGO_NAME();
Vector VCONT_NAME = ltcora.getVCONT_NAME();
Vector VAGMT_BASE= ltcora.getVAGMT_BASE();
//Vector VBL_CNT=ltcora.getVBL_CNT();
//Vector VBOE_CNT = ltcora.getVBOE_CNT();
Vector VCARGO_STATUS = ltcora.getVCARGO_STATUS();
Vector VFCC_FLAG =  ltcora.getVFCC_FLAG();
Vector VALLOC_REV_NO = ltcora.getVALLOC_REV_NO();
Vector VALLOC_STATUS = ltcora.getVALLOC_STATUS();
Vector VTOTAL_BOE_NO = ltcora.getVTOTAL_BOE_NO();
Vector VTOTAL_BL_NO = ltcora.getVTOTAL_BL_NO();
Vector VCONT_REF_NO = ltcora.getVCONT_REF_NO();
Vector VACT_ARRV_DT = ltcora.getVACT_ARRV_DT();
Vector VQQ_QTY_MMBTU = ltcora.getVQQ_QTY_MMBTU();
Vector VQQ_QTY_CER_NO = ltcora.getVQQ_QTY_CER_NO();
Vector VQQ_QTY_CER_DT = ltcora.getVQQ_QTY_CER_DT();
Vector VNO_WIN_DAYS =ltcora.getVNO_WIN_DAYS();
Vector VCARGO_MAPPING =ltcora.getVCARGO_MAPPING();
Vector VCARGO_ISATTACHED =ltcora.getVCARGO_ISATTACHED();

Vector <String> VALLOCATION_STATUS = ltcora.getVALLOCATION_STATUS();

%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
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
									<%if(VCARGO_NAME.size() > 0){ %>
										<%for(int i=0; i<VCARGO_NAME.size(); i++){ %>
											<%
											if(!VCARGO_QTY.elementAt(i).equals(""))
											{ %>
											<tr>
												<td align="center">
													<input type="radio" name="rdo" <%if(VCARGO_ISATTACHED.elementAt(i).equals("Y")){ %>disabled<%} %>
													onclick="setValue('<%=id_no %>','<%=VCARGO_REF.elementAt(i)%>',
													'<%=VSHIP_CD.elementAt(i)%>','<%=VSHIP_NAME.elementAt(i)%>',
													'<%=VBUYER_CD.elementAt(i)%>','<%=VBUYER_NAME.elementAt(i)%>',
													'<%=VSTART_DT.elementAt(i)%>','<%=VEND_DT.elementAt(i)%>',
													'<%=VACT_ARRV_DT.elementAt(i)%>',
													'<%=VCARGO_QTY.elementAt(i)%>',
													'<%=VQQ_QTY_MMBTU.elementAt(i)%>',
													'<%=Double.parseDouble(""+VCARGO_QTY.elementAt(i))/Double.parseDouble(""+VNO_WIN_DAYS.elementAt(i))%>',
													'<%=VQQ_QTY_CER_NO.elementAt(i)%>',
													'<%=VQQ_QTY_CER_DT.elementAt(i)%>',
													'<%=VNO_WIN_DAYS.elementAt(i)%>',
													'<%=VCARGO_MAPPING.elementAt(i)%>')">
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
											<%}else{ %>
												<tr>
													<td colspan="15" align="center">
														<%=utilmsg.infoMessage("<b>Arrived Cargo List is not Available!</b>") %>
													</td>
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