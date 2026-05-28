<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>

function refreshParent(msg,msg_type)
{
	window.opener.opnerRefresh(msg,msg_type);
	window.close();
}
function enableDiv(obj,index)
{
	var counterparty_cd =  document.getElementById("counterparty_cd"+index);
	var agmt_no =  document.getElementById("agmt_no"+index);
	var agmt_rev_no =  document.getElementById("agmt_rev_no"+index);
	var cont_no =  document.getElementById("cont_no"+index);
	var cont_rev_no = document.getElementById("cont_rev_no"+index);
	var contract_type = document.getElementById("contract_type"+index);
	var balance_qty = document.getElementById("balance_qty"+index);
	var rate = document.getElementById("rate"+index);
	var rate_unit = document.getElementById("rate_unit"+index);
	var cargo_no = document.getElementById("cargo_no"+index);
	
	var allocate_qty = document.getElementById("allocate_qty"+index);
	
	var tcq_sign = document.forms[0].tcq_sign.value;
	
	var balFlag = false;
	
	if(obj.checked)
    {
		if(tcq_sign == "+")
		{
	    	if(parseFloat(balance_qty.value)>0)
	        {
	    		counterparty_cd.disabled=false;
	    		agmt_no.disabled=false;
	    		agmt_rev_no.disabled=false;
	    		cont_no.disabled=false;
	    		cont_rev_no.disabled=false;
	    		contract_type.disabled=false;
	    		balance_qty.disabled=false;
	    		rate.disabled=false;
	    		rate_unit.disabled=false;
	    		cargo_no.disabled=false;
	    		
	    		allocate_qty.disabled=false;
	        }
	        else
	        {
	          	balFlag = true;
	        }
		}
		else
		{
			counterparty_cd.disabled=false;
    		agmt_no.disabled=false;
    		agmt_rev_no.disabled=false;
    		cont_no.disabled=false;
    		cont_rev_no.disabled=false;
    		contract_type.disabled=false;
    		balance_qty.disabled=false;
    		rate.disabled=false;
    		rate_unit.disabled=false;
    		cargo_no.disabled=false;
    		
    		allocate_qty.disabled=false;
		}
	}
    else
    {
    	counterparty_cd.disabled=true;
   		agmt_no.disabled=true;
   		agmt_rev_no.disabled=true;
   		cont_no.disabled=true;
   		cont_rev_no.disabled=true;
   		contract_type.disabled=true;
   		balance_qty.disabled=true;
   		rate.disabled=true;
		rate_unit.disabled=true;
		cargo_no.disabled=true;
		
   		allocate_qty.disabled=true;
    }
	
	if(balFlag==true)
    {
        alert("You can not Select Cargo having balance Quantity less or equal ZERO(0)!");
        obj.checked = false;
    } 
}

function calculateTotalTCQ()
{
	var allocate_qty = document.forms[0].allocate_qty;
	var chk = document.forms[0].chk;
	var var_tcq = parseFloat(""+document.forms[0].var_tcq.value);
	
	var sum=parseFloat("0");
	var remaining=parseFloat("0");
	
	if(allocate_qty!=null && allocate_qty.length!=undefined)
    { 
		for(var i=0;i<chk.length;i++)
        { 
        	if(chk[i].checked)
         	{ 
        		if(allocate_qty[i].value == "" || allocate_qty[i].value == null || allocate_qty[i].value == " ")
        		{
        			
        		}
        		else
        		{
        			sum = parseFloat(sum) + parseFloat(allocate_qty[i].value)
        			remaining = var_tcq - parseFloat(sum)
        		}
         	}
        }
    }
	else
	{
		if(chk.checked)
     	{ 
    		if(allocate_qty.value == "" || allocate_qty.value == null || allocate_qty.value == " ")
    		{
    			
    		}
    		else
    		{
    			sum = parseFloat(sum) + parseFloat(allocate_qty.value)
    			remaining = var_tcq - parseFloat(sum)
    		}
     	}
	}
	
	if(parseFloat(sum) > 0)
	{
		document.forms[0].total_qty.value = round(sum,2);
	}
	else if(parseFloat(sum)==0)
	{
		document.forms[0].total_qty.value = "";
	}
	
	document.forms[0].remaining_qty.value = round(remaining,2);
	
	if(parseFloat(remaining)==0)
	{
		document.forms[0].remaining_qty.style.background="#d1e7dd";
	}
	else
	{
		document.forms[0].remaining_qty.style.background="pink";
	}
}

function doSubmit()
{
	var msg = "";
	var flag = true;
	
	var i = 0;
	var row_no = 0;
	var total_qty = 0;
	var tcq_quantity = "0";
	
	var count = 0;
	var tcq_sign = document.forms[0].tcq_sign.value;
	
	var allocate_qty = document.forms[0].allocate_qty;
	var chk = document.forms[0].chk;
	var u = document.forms[0].u.value;
	
	var bal_qty_1 = parseFloat("0");
	var bal_qty_2 = parseFloat("0");
	
	var var_tcq = parseFloat(""+document.forms[0].var_tcq.value);
	
	if(allocate_qty!=null && allocate_qty.length!=undefined)
    { 
		for(var i=0;i<chk.length;i++)
        {
			if(chk[i].checked)
			{
				tcq_quantity = allocate_qty[i].value;
			
				if(tcq_quantity==null || trim(tcq_quantity)=='' || trim(tcq_quantity)=='0' || trim(tcq_quantity)=='0.0' || trim(tcq_quantity)=='0.00')
				{
					msg += "TCQ Reconciliation QTY Can Not Be Empty or Zero for the Selected Cargo : "+document.forms[0].cont_no[i].value+" !\n\n";
					flag = false;
				}
				else
				{
					total_qty += parseFloat(tcq_quantity);
				}
			}
		}
	}
	else
	{
		if(chk.checked)
		{
			tcq_quantity = allocate_qty.value;
		
			if(tcq_quantity==null || trim(tcq_quantity)=='' || trim(tcq_quantity)=='0' || trim(tcq_quantity)=='0.0' || trim(tcq_quantity)=='0.00')
			{
				msg += "TCQ Reconciliation QTY Can Not Be Empty or Zero for the Selected Cargo : "+document.forms[0].cont_rev_no.value+" !\n\n";
				flag = false;
			}
			else
			{
				total_qty += parseFloat(tcq_quantity);
			}
		}
	}
	
	msg+="Total MMBTU Entered = "+total_qty+",\nRequested TCQ Reconciliation MMBTU = "+var_tcq+"\n\n";
	
	if(total_qty!=var_tcq)
	{
		msg += "Total MMBTU Should be exactly equal to Requested TCQ Reconciliation MMBTU for TCQ Modification!\n";
		flag = false;
	}
		
	if(flag)
	{
		var a = confirm(msg+"Do You Want To Submit TCQ Reconciliation Details For Selected Contract?");		
		if(a)
		{
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function checkQty(obj, balance, cont, alloc_quantity, tcq_sign)
{
	if(tcq_sign=='+')
	{
		if(parseFloat(obj.value) > parseFloat(balance))
		{
			alert("Please enter Reconciliantion Qty less or equal to Balance Qty for Cargo : "+cont);
			obj.value="";
			calculateTotalTCQ()
		}
		else if(parseFloat(obj.value) <= parseFloat(0))
		{
			alert("Please enter Reconciliantion Qty Greater than ZERO(0) for Cargo : "+cont);
			obj.value="";
			calculateTotalTCQ()
		}	
	}
	else if(tcq_sign=='-')
	{
		if(alloc_quantity!=null && trim(alloc_quantity)!='' && trim(alloc_quantity)!='0' && trim(alloc_quantity)!='0.0' && trim(alloc_quantity)!='0.00')
		{
			var alloc_qty = parseFloat(alloc_quantity);
			
			if(parseFloat(obj.value)>alloc_qty)
			{
				alert("You are not permitted to enter TCQ QTY greater than Already Contract Allocated QTY for Cargo : "+cont+"!");
				obj.value="";
				calculateTotalTCQ()
			}
		}
		else
		{
			alert("You are not permitted to enter TCQ QTY greater than Already Contract Allocated QTY for Cargo : "+cargo_ref_cd+"!");
			obj.value="";
			calculateTotalTCQ()
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_EnergyBank" id="energyBank" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String counterparty_abbr = request.getParameter("counterparty_abbr")==null?"":request.getParameter("counterparty_abbr");
String clearance = request.getParameter("clearance")==null?"KYC":request.getParameter("clearance");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String rate = request.getParameter("rate")==null?"":request.getParameter("rate");
String rate_unit=request.getParameter("rate_unit")==null?"2":request.getParameter("rate_unit");
String deal_map=request.getParameter("deal_map")==null?"":request.getParameter("deal_map");

String tcq_sign=request.getParameter("tcq_sign")==null?"":request.getParameter("tcq_sign");
String var_tcq=request.getParameter("var_tcq")==null?"":request.getParameter("var_tcq");
String tcq=request.getParameter("tcq")==null?"":request.getParameter("tcq");

if(tcq_sign.equals("plus"))
{
	tcq_sign="+";
}
else if(tcq_sign.equals("minus"))
{
	tcq_sign="-";
}

energyBank.setCallFlag("TCQ_MODIFICATION");
energyBank.setComp_cd(owner_cd);
energyBank.setCounterparty_cd(counterparty_cd);
energyBank.setAgmt_no(agmt_no);
energyBank.setAgmt_rev_no(agmt_rev_no);
energyBank.setCont_no(cont_no);
energyBank.setCont_rev_no(cont_rev_no);
energyBank.setContract_type(contract_type);
energyBank.setTcq_sign(tcq_sign);
energyBank.init();

Vector VINDEX = energyBank.getVINDEX();
Vector VCARGO_POOL_FLAG = energyBank.getVCARGO_POOL_FLAG();
Vector VCARGO_POOL_NM = energyBank.getVCARGO_POOL_NM();

Vector VCOUNTERPARTY_CD = energyBank.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = energyBank.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = energyBank.getVCOUNTERPARTY_ABBR();
Vector VCONT_NO = energyBank.getVCONT_NO();
Vector VCONT_REV_NO = energyBank.getVCONT_REV_NO();
Vector VCONT_NAME = energyBank.getVCONT_NAME();
Vector VSTART_DT = energyBank.getVSTART_DT();
Vector VEND_DT = energyBank.getVEND_DT();
Vector VRATE = energyBank.getVRATE();
Vector VRATE_UNIT = energyBank.getVRATE_UNIT();
Vector VRATE_UNIT_NM = energyBank.getVRATE_UNIT_NM();
Vector VCONT_STATUS = energyBank.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = energyBank.getVCONT_STATUS_FLG();
Vector VPRICE_TYPE = energyBank.getVPRICE_TYPE();
Vector VBOOKED_QTY = energyBank.getVBOOKED_QTY();
Vector VAGMT_NO = energyBank.getVAGMT_NO();
Vector VAGMT_REV_NO = energyBank.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = energyBank.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = energyBank.getVCONTRACT_TYPE_NM();
Vector VMIN_ALLOC_DT = energyBank.getVMIN_ALLOC_DT();
Vector VMAX_ALLOC_DT = energyBank.getVMAX_ALLOC_DT();
Vector VUNLOADED_QTY = energyBank.getVUNLOADED_QTY();
Vector VAVAIL_FOR_SALE_QTY = energyBank.getVAVAIL_FOR_SALE_QTY();
Vector VALLOCATED_QTY = energyBank.getVALLOCATED_QTY();
Vector VBALANCE_QTY = energyBank.getVBALANCE_QTY();
Vector VCONT_REF = energyBank.getVCONT_REF();
Vector VALLOC_QTY_CONTRACT_WISE = energyBank.getVALLOC_QTY_CONTRACT_WISE();

Vector VCARGO_NO = energyBank.getVCARGO_NO();
Vector VPURCHASE_MAP_ID = energyBank.getVPURCHASE_MAP_ID();

%>
<body  <%if(!msg.equals("")){%>onload="refreshParent('<%=msg%>','<%=msg_type%>');"<%}%>>
<form method="post" action="../servlet/Frm_EnergyBank">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<%if(!msg.equals("")){
				if(msg_type.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
				<%}else if(msg_type.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
				<%}
			} %>
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	TCQ Modification
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-6 col-sm-6 col-xs-6">
							<div class="form-group row">
				    			<label class="form-label"><b><%=counterparty_abbr%> - <%=deal_map%></b></label>
				  			</div>
						</div>
						<div class="col-md-6 col-sm-6 col-xs-6">
							<div class="form-group row">
				    			<label class="form-label"><b>TCQ for Reconciliation : <font style="background: #a6ff4d;"><%=tcq_sign%><%=var_tcq%></font></b></label>
				  			</div>
						</div>
					</div>
					<%int i=0;int k=0;
					for(int j=0; j<VCARGO_POOL_FLAG.size(); j++){ 
					int index = Integer.parseInt(""+VINDEX.elementAt(j));
					String tbl_id = "tbl_"+VCARGO_POOL_FLAG.elementAt(j);
					%>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading<%=j%>">
   										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="false" aria-controls="collapse<%=j%>">
							    		<%=VCARGO_POOL_NM.elementAt(j)%>
							      		</button>
							    	</h2>
							    	<div id="collapse<%=j%>" class="accordion-collapse collapse" aria-labelledby="heading<%=j%>">
							      		<div class="accordion-body accor-body">
											<div class="row">
												<div class="table-responsive">
													<table class="table table-bordered table-hover" id="<%=tbl_id%>">
														<thead>
															<tr>
																<th></th>
																<th>
																	Purchase Contract#
																	<br>
																	<div align="center">
																		<input class="form-control form-control-sm" type="text" id="<%=VCARGO_POOL_FLAG.elementAt(j)%>_pur_cont" 
																		onkeyup="Search(this,'1','<%=tbl_id%>');" placeholder="Search.." style="width:100px"/>
																	</div>
																</th>
																<th>Contract Type</th>
																<th>Counterparty</th>
																<th>Contract Name</th>
																<th>Contract/Cargo/Trade Ref#</th>
																<th>Status</th>
																<th>Contract Period</th>
																<th>Allocation Start Date</th>
																<th>Last Allocation Date</th>
																<th>Price Type</th>
																<th>Currency/MMBTU</th>
																<th>Price</th>
																<th>MMBTU Booked</th>
																<th>MMBTU Unloaded</th>
																<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Avail for Sale </th>
																<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Allocated</th>
																<%if(VCARGO_POOL_FLAG.elementAt(j).equals("P")){ %>
																<th><%=counterparty_abbr%> - <%=deal_map%> Allocated MMBTU</th>
																<%} %>
																<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> Balance MMBTU</th>
																<th>TCQ Reconciliation MMBTU</th>
															</tr>
														</thead>
														<tbody>
														<%k=0;
														if(index > 0)
														{ %>
															<%for(i=i; i<VCOUNTERPARTY_CD.size(); i++)
															{
																k+=1;
															%>
																<tr>
																	<td align="center">
																		<input type="checkbox" class="form-check-input" name="chk" id="chk<%=i%>" 
																		onclick="enableDiv(this,'<%=i%>');">
																	</td>
																	<td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>"><%=VPURCHASE_MAP_ID.elementAt(i)%></td>
																	<td align="center">
																		<span
																			<%if(VCONTRACT_TYPE.elementAt(i).equals("D")){ %>
														    					style="background: #66ffd9;"
														    				<%}else if(VCONTRACT_TYPE.elementAt(i).equals("N")){ %>
														    					style="background: skyblue;"
														    				<%}else if(VCONTRACT_TYPE.elementAt(i).equals("I")){ %>
														    					style="background: pink;"
														    				<%} %>	
																		><b><%=VCONTRACT_TYPE_NM.elementAt(i)%></b></span>
																	</td>
																	<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%> : <%=VCOUNTERPARTY_ABBR.elementAt(i)%>">
																		<%=VCOUNTERPARTY_NM.elementAt(i)%>
																		<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>" disabled>
																		<input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>" disabled>
																		<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=i%>" value="<%=VAGMT_REV_NO.elementAt(i)%>" disabled>
																		<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>" disabled>
																		<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=i%>" value="<%=VCONT_REV_NO.elementAt(i)%>" disabled>
																		<input type="hidden" name="contract_type" id="contract_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled>
																		<input type="hidden" name="balance_qty" id="balance_qty<%=i%>" value="<%=VBALANCE_QTY.elementAt(i)%>" disabled>
																		<input type="hidden" name="rate" id="rate<%=i%>" value="<%=VRATE.elementAt(i)%>" disabled>
																		<input type="hidden" name="rate_unit" id="rate_unit<%=i%>" value="<%=VRATE_UNIT.elementAt(i)%>" disabled>
																		<input type="hidden" name="alloc_qty_contWise" id="alloc_qty_contWise<%=i%>" value="<%=VALLOC_QTY_CONTRACT_WISE.elementAt(i)%>" disabled>
																		<input type="hidden" name="cargo_no" id="cargo_no<%=i%>" value="<%=VCARGO_NO.elementAt(i)%>" disabled>
																	</td>
																	<td><%=VCONT_NAME.elementAt(i)%></td>
																	<td><%=VCONT_REF.elementAt(i)%></td>
																	<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
																	<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
																	<td align="center"><%=VMIN_ALLOC_DT.elementAt(i) %></td>
																	<td align="center"><%=VMAX_ALLOC_DT.elementAt(i) %></td>
																	<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
																	<td align="center"><%=VRATE_UNIT_NM.elementAt(i)%></td>
																	<td align="right"><%=VRATE.elementAt(i)%></td>
																	<td align="right"><%=VBOOKED_QTY.elementAt(i)%></td>
																	<td align="right"><%=VUNLOADED_QTY.elementAt(i)%></td>
																	<td align="right"><%=VAVAIL_FOR_SALE_QTY.elementAt(i)%></td>
																	<td align="right"><%=VALLOCATED_QTY.elementAt(i)%></td>
																	<%if(VCARGO_POOL_FLAG.elementAt(j).equals("P")){ %>
																	<td align="right"><%=VALLOC_QTY_CONTRACT_WISE.elementAt(i)%></td>
																	<%} %>
																	<td align="right"><%=VBALANCE_QTY.elementAt(i)%></td>
																	<td>
																		<input type="text" class="form-control form-control-sm" name="allocate_qty" id="allocate_qty<%=i%>" value="" style="width:100px;" 
																		onblur="negNumber(this);checkNumber1(this,10,2);checkQty(this,'<%=VBALANCE_QTY.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VALLOC_QTY_CONTRACT_WISE.elementAt(i) %>','<%=tcq_sign %>');"
																		onkeyup="calculateTotalTCQ();" disabled>
																	</td>
																</tr>
																<%
																if(k==index)
																{
																	i=i+1;
																	break;
																}%>
															<%} %>
														<%}else{ %>
															<tr>
																<td <%if(VCARGO_POOL_FLAG.elementAt(j).equals("P")){ %>colspan="21"<%}else{%>colspan="20"<%} %> align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
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
					<%} %>
					<div class="row">
						<div class="col-md-6 col-sm-6 col-xs-6">
						</div>
						<div class="col-md-6 col-sm-6 col-xs-6">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Remaining MMBTU :</b></label>
								</div>
								<div class="col-auto">
									<input type="text" class="form-control form-control-sm" name="remaining_qty" readOnly>
								</div>
								<div class="col-auto">
									<label class="form-label"><b>Total MMBTU :</b></label>
								</div>
								<div class="col-auto">
									<input type="text" class="form-control form-control-sm" name="total_qty" readOnly>
								</div>
				    		</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="option" value="TCQ_MODIFICATION">

<input type="hidden" name="tcq_sign" value="<%=tcq_sign%>">
<input type="hidden" name="var_tcq" value="<%=var_tcq%>">
<input type="hidden" name="tcq" value="<%=tcq%>">

<input type="hidden" name="sales_counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="sales_agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="sales_agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="sales_cont_no" value="<%=cont_no%>">
<input type="hidden" name="sales_cont_rev_no" value="<%=cont_rev_no%>">
<input type="hidden" name="sales_contract_type" value="<%=contract_type%>">
<input type="hidden" name="sales_rate" value="<%=rate%>">
<input type="hidden" name="sales_rate_unit" value="<%=rate_unit%>">

<input type="hidden" name="read_access" value="<%=read_access%>">
<input type="hidden" name="write_access" value="<%=write_access%>">
<input type="hidden" name="check_access" value="<%=check_access%>">
<input type="hidden" name="print_access" value="<%=print_access%>">
<input type="hidden" name="delete_access" value="<%=delete_access%>">  	
<input type="hidden" name="audit_access" value="<%=audit_access%>">
<input type="hidden" name="authorize_access" value="<%=authorize_access%>">
<input type="hidden" name="approve_access" value="<%=approve_access%>">
<input type="hidden" name="execute_access" value="<%=execute_access%>">
<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<input type="hidden" name="u" value="<%=u%>">
</form>
</body>
</html>