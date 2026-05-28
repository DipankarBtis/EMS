
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh()
{
	var u = document.forms[0].u.value;
	
		var url = "frm_pre_deal_creditCheck.jsp?u="+u;
				
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
}

function doSubmit()
{
	var counterparty = document.forms[0].counterparty.value;
	var sellBuy = document.forms[0].sellBuy.value;
	var startDt = document.forms[0].startDt.value;
	var endDt = document.forms[0].endDt.value;
	var value = document.forms[0].value.value;
	var currency = document.forms[0].currency.value;
	var spotTerm = document.forms[0].spotTerm.value;
	var deliveryTerm = document.forms[0].dlv_terms.value;
	var cont_comp = document.forms[0].cont_comp.value;
	var paymentTerm = document.forms[0].paymentTerm.value;
	var remark = document.forms[0].remark.value;
	
	var msg="";
	var flag=true;
	
	if(trim(counterparty)=="" || counterparty==0)
	{
		msg+="Please Select Counterparty!\n";
		flag=false;
	}
	if(trim(sellBuy)=="" || sellBuy==0)
	{
		msg+="Please Select Sell/Buy!\n";
		flag=false;
	}
	if(trim(startDt)=="" || startDt==0)
	{
		msg+="Please Enter Delivery Start Date!\n";
		flag=false;
	}
	if(trim(endDt)=="" || endDt==0)
	{
		msg+="Please Enter Delivery End Date!\n";
		flag=false;
	}	
	if(trim(value)=="" || value==0)
	{
		msg+="Please Enter Value!\n";
		flag=false;
	}
	if(trim(currency)=="" || currency==0)
	{
		msg+="Please Select Currency!\n";
		flag=false;
	}
	if(trim(deliveryTerm)=="" || deliveryTerm==0)
	{
		msg+="Please Select Delivery Term!\n";
		flag=false;
	}
	if(trim(spotTerm)=="" || spotTerm==0)
	{
		msg+="Please Select Spot/Term!\n";
		flag=false;
	}
	if(trim(cont_comp)=="" || cont_comp==0)
	{
		msg+="Please Select Contracting Company!\n";
		flag=false;
	}
	if(trim(paymentTerm)=="" || paymentTerm==0)
	{
		msg+="Please Enter Payment Terms!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Submit the Pre Deal Credit Check Request?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function deliveryTerms()
{
	var sell_buy = document.forms[0].sellBuy.value;
	if(sell_buy == 'Buy')
	{
		document.getElementById('dlv_terms_buy').style.display = "inline";
		document.getElementById('dlv_terms_sell').style.display = "none";
	}
	else
	{
		document.getElementById('dlv_terms_buy').style.display = "none";
		document.getElementById('dlv_terms_sell').style.display = "inline";
	}
}

function changeValue()
{
	var buy_dlv_terms = document.forms[0].buy_deliveryTerm.value;
	var sell_dlv_terms = document.forms[0].sell_deliveryTerm.value;
	if(trim(buy_dlv_terms)=="" || buy_dlv_terms==0)
	{
		document.forms[0].dlv_terms.value = sell_dlv_terms;
	}
	else if(trim(sell_dlv_terms)=="" || sell_dlv_terms==0)
	{
		document.forms[0].dlv_terms.value = buy_dlv_terms;
	}
}

function doApproveDeal(VREQ_ID,VCOUNTERPARTY_NAME,VCOUNTERPARTY_CD,VSEQ_NO,VSTATUS)
{
	document.forms[0].app_request_id.value = VREQ_ID;
	document.forms[0].app_counterparty_nm.value = VCOUNTERPARTY_NAME;
	document.forms[0].app_coutpty_cd.value = VCOUNTERPARTY_CD;
	document.forms[0].app_seq_no.value = VSEQ_NO;
	
	if(VSTATUS=="")
	{
		document.forms[0].app_status.value = "P";
	}
	else
	{
		document.forms[0].app_status.value = VSTATUS;
	}
	
	document.forms[0].opration.value = "MODIFY";
}

function doApproveSubmit()
{
	var app_request_id = document.forms[0].app_request_id.value;	
	var app_counterparty_nm = document.forms[0].app_counterparty_nm.value;
	var app_coutpty_cd = document.forms[0].app_coutpty_cd.value;
	var app_seq_no = document.forms[0].app_seq_no.value;
	var app_status = document.forms[0].app_status.value;
	
	var msg="";
	var flag=true;
	
	if(trim(app_status)=="" || app_status==0)
	{
		msg+="Please Select Status!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Approve the Pre Deal Credit Check?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function openfile(countpty_cd,sell_buy,btn)
{
	var newWindow = "";
	if(btn == "CR")
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow= window.open("rpt_credit_rating.jsp?counterparty_cd="+countpty_cd+"&sell_buy="+sell_buy,"rpt_credit_rating","top=10,left=10,width=1000,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
		    newWindow= window.open("rpt_credit_rating.jsp?counterparty_cd="+countpty_cd+"&sell_buy="+sell_buy,"rpt_credit_rating","top=10,left=10,width=1000,height=600,scrollbars=1");
		}
	}else{
		if(!newWindow || newWindow.closed)
		{
			newWindow= window.open("rpt_walkforward.jsp?counterparty_cd="+countpty_cd+"&sell_buy="+sell_buy,"rpt_walkforward_exposure","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
		    newWindow= window.open("rpt_walkforward.jsp?counterparty_cd="+countpty_cd+"&sell_buy="+sell_buy,"rpt_walkforward_exposure","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function checkEffDate()
{
	var start_dt = document.forms[0].startDt.value;
	var end_dt = document.forms[0].endDt.value;
	
	if(trim(start_dt)!="" && trim(end_dt)!="")
	{
		var val_dt = compareDate(start_dt,end_dt);
		if(parseInt(val_dt) == 1)
		{
			alert('Delivery End date must be greater or equal to Delivery Start Date !!');
			document.forms[0].endDt.value="";
			return false;
		}
	}
}

</script>
</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DataBean_CreditRisk" id="credit_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String company_cd = owner_cd;

credit_risk.setCallFlag("PRE_DEAL_CREDITCHECK_REQ");
credit_risk.setComp_cd(owner_cd);
credit_risk.init();

String company_abbr = credit_risk.getCompany_abbr();
Vector VMST_COUNTERPARTY_CD = credit_risk.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = credit_risk.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = credit_risk.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_CD = credit_risk.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NAME = credit_risk.getVCOUNTERPARTY_NAME();
Vector VSEQ_NO = credit_risk.getVSEQ_NO();
Vector VVALUE = credit_risk.getVVALUE();
Vector VCURRENCY = credit_risk.getVCURRENCY();
Vector VSTART_DT = credit_risk.getVSTART_DT();
Vector VEND_DT = credit_risk.getVEND_DT();
Vector VBUY_SELL = credit_risk.getVBUY_SELL();
Vector VDLV_TERMS = credit_risk.getVDLV_TERMS();
Vector VSPOT_TERMS = credit_risk.getVSPOT_TERMS();
Vector VPAYMENT_TERMS = credit_risk.getVPAYMENT_TERMS();
Vector VREQ_ID = credit_risk.getVREQ_ID();
Vector VREQ_DT = credit_risk.getVREQ_DT();
Vector VREQ_REMARK = credit_risk.getVREQ_REMARK();
Vector VSTATUS = credit_risk.getVSTATUS();
Vector VAPRV_DT = credit_risk.getVAPRV_DT();
Vector VAPRV_BY = credit_risk.getVAPRV_BY();
Vector VAPRV_REMARK = credit_risk.getVAPRV_REMARK();
Vector VCONT_COMP = credit_risk.getVCONT_COMP();
Vector VREQ_BY = credit_risk.getVREQ_BY();

%>

<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_CreditRisk">
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
					 <div class="d-flex justify-content-between">
						<div class="topheader">
				    		Pre Deal Credit Check  
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#AddNewRequest">Add Credit Check Request</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered">
									<thead>
										<tr>
											<th>Sr#</th>
											<th>Request ID</th>
											<th>Counterparty</th>
											<th>Buy/Sell</th>
											<th>Value</th>
											<th>Currency</th>
											<th>Payment Terms</th>
											<th>Delivery Start Date</th>
											<th>Delivery End Date</th>
											<th>Delivery Terms</th>
											<th>Spot/Terms</th>
											<th>Shell Contracting Co.</th>
											<th>Requested By</th>
											<th>Status</th>
											<th>Remarks</th>
											<th>Approval By</th>
											<th>Approval Remark</th>
											<th>Report</th>
										</tr>
									</thead>
									<tbody>
									<%int k=0;
									if(VCOUNTERPARTY_CD.size()!=0)
									{ 
										for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){
										k+=1;%>
											<tr valign="middle">
												<td align="right">
													<%if(approve_access.equals("Y") && VSTATUS.elementAt(i).equals("P")){ %>
													<font title="Click to Edit" style="color:var(--header_color)">
														<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#preCheckApprModal" 
														onclick="doApproveDeal('<%=VREQ_ID.elementAt(i)%>','<%=VCOUNTERPARTY_NAME.elementAt(i)%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VSEQ_NO.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>')">
														</i>
													</font>
													<%} %>
													<%=k %>
												</td>
												<td align="center"><%=VREQ_ID.elementAt(i) %></td>
												<td align="center"><%=VCOUNTERPARTY_NAME.elementAt(i) %></td>
												<td align="center"><%=VBUY_SELL.elementAt(i) %></td>
												<td align="right"><%=VVALUE.elementAt(i) %></td>
												<td align="center"><%=VCURRENCY.elementAt(i) %></td>
												<td align="center"><%=VPAYMENT_TERMS.elementAt(i) %></td>
												<td align="center"><%=VSTART_DT.elementAt(i) %></td>
												<td align="center"><%=VEND_DT.elementAt(i) %></td>
												<td align="center"><%=VDLV_TERMS.elementAt(i) %></td>
												<td align="center"><%=VSPOT_TERMS.elementAt(i) %></td>
												<td align="center"><%=VCONT_COMP.elementAt(i) %></td>
												<td align="center"><%=VREQ_BY.elementAt(i) %><br><%=VREQ_DT.elementAt(i) %></td>
												<td align="center">
													<%if(VSTATUS.elementAt(i).equals("P")){ %>
														<b><span class="alert alert-primary">Pending</span></b>
													<%}else if(VSTATUS.elementAt(i).equals("A")){  %>
														<b><span class="alert alert-success">Approve</span></b>
													<%}else if(VSTATUS.elementAt(i).equals("R")){  %>
														<b><span class="alert alert-danger">Reject</span></b>
													<%} %>
												</td>
												<td align="center"><%=VREQ_REMARK.elementAt(i) %></td>
												<td align="center"><%=VAPRV_BY.elementAt(i) %><br><%=VAPRV_DT.elementAt(i) %></td>
												<td align="center"><%=VAPRV_REMARK.elementAt(i) %></td>
												<td  align="center" >
												<%if(VBUY_SELL.elementAt(i).equals("Sell")){%>
													<i title="Click To Open Exposure Report" class="fa fa-film fa-lg" 
													onclick="openfile('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VBUY_SELL.elementAt(i)%>','EXPO');"
													style="color: #ff3399;">													
													</i>													
												<%}else{%>
													<i title="Click To Open Credit rating Report" class="fa fa-star-half-o fa-lg" 
													onclick="openfile('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VBUY_SELL.elementAt(i)%>','CR');"
													style="color: #008080;">
													</i>
												<%}%>
												</td>
											</tr>
											<%} %>
										<%}else{ %>
											<tr><td colspan="20" align="center"><%=utilmsg.infoMessage("<b>Pre Deal credit check Request Not Available!</b>") %></td></tr>
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

<div class="modal fade" id="AddNewRequest" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
        			Add New Pre Delivery Credit check Request
        		</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
        	</div>
        	<div class="modal-body mdbody">
      			<div class="cdbody">
					<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Counterparty<span class="s-red">*</span></b></label>
				  			</div>
						</div>
	   					<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="counterparty">
				      					<option value="0">---Select---</option>
				      					<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
											<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
						      		</select>
						      	</div>
						    </div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">
						</div>
	      				<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Sell/Buy<span class="s-red">*</span></b></label>
				  			</div>
						</div>
	   					<div class="col-sm-1 col-xs-1 col-md-1">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="sellBuy" onchange="deliveryTerms()">
				      					<option value="Sell">Sell</option>
										<option value="Buy">Buy</option>
						      		</select>
						      	</div>
						    </div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Delivery Start Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
	   					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="startDt" value="" size="10" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);checkEffDate();" autocomplete="off" >
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
						      	</div>
						    </div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
						</div>
	      				<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Delivery End Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="endDt" value="" size="10" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);checkEffDate();" autocomplete="off" >
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
						      	</div>
						    </div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Value<span class="s-red">*</span></b></label>
				  			</div>
						</div>
	   					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="value">
						      	</div>
						    </div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
						</div>
	      				<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Currency<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="currency">
				      					<option value="1">INR</option>
										<option value="2">USD</option>
						      		</select>
						      	</div>
						    </div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Delivery Term<span class="s-red">*</span></b></label>
				  			</div>
						</div>
	   					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div id="dlv_terms_sell" >
					    				<select class="form-select form-select-sm" name="sell_deliveryTerm" onchange="changeValue()">
					      					<option value="0">---Select---</option>
					      					<option value="Ex-terminal">Ex-terminal</option>
											<option value="Delivered">Delivered</option>
							      		</select>
							      	</div>
							      	<div id="dlv_terms_buy" style="display:none" >
					    				<select class="form-select form-select-sm" name="buy_deliveryTerm" onchange="changeValue()">
					      					<option value="0">---Select---</option>
					      					<option value="DES">DES</option>
											<option value="FOB">FOB</option>
							      		</select>
							      	</div>
							      	<input type="hidden" name="dlv_terms" id="dlv_terms">
						      	</div>
						    </div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
						</div>
	      				<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Spot/terms<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="spotTerm">
										<option value="0">---Select---</option>
				      					<option value="Spot">Spot</option>
										<option value="Terms">Terms</option>
						      		</select>
						      	</div>
						    </div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Shell Contracting Co.<span class="s-red">*</span></b></label>
				  			</div>
						</div>
	   					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="cont_comp">
				      					<option value="<%=company_cd%>"><%=company_abbr %></option>
						      		</select>
						      	</div>
						    </div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
						</div>
	      				<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Payment Terms<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="paymentTerm">	
						      	</div>
						    </div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Remark :</b></label>
				  			</div>
						</div>
	   					<div class="col-sm-12 col-xs-12 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="remark">
						      	</div>
						    </div>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div align="right">
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

<div class="modal fade" id="preCheckApprModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Approve Pre-Deal Credit Check
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="card-body cdbody">
					<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Counterparty Name:<span class="s-red">*</span></b></label>
				  			</div>
						</div>
	   					<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="app_counterparty_nm" value="" readOnly style="font-weight:bold;">
						      	</div>
						    </div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">
						</div>
	      				<div class="col-sm-1 col-xs-1 col-md-1">  
							<div class="form-group row">
				    			<label class="form-label"><b>Request ID :<span class="s-red">*</span></b></label>
				  			</div>
						</div>
	   					<div class="col-sm-1 col-xs-1 col-md-1">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="app_request_id" value="" readOnly style="font-weight:bold;">
						      	</div>
						    </div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Status:<span class="s-red">*</span></b></label>
				  			</div>
						</div>
	   					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="app_status">
				      					<option value="0">---Select---</option>
				      					<option value="A">Approve</option>
				      					<option value="R">Reject</option>
				      					<option value="P">Pending</option>
						      		</select>
						      	</div>
						    </div>
						</div>
      				</div >
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Approval Message :</b></label>
				  			</div>
						</div>
	   					<div class="col-sm-8 col-xs-8 col-md-8">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="app_msg" value="" >
						      	</div>
						    </div>
						</div>
      				</div>
				</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-end">
					<%if(write_access.equals("Y")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doApproveSubmit();">
					<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
					<%} %>
				</div>
      		</div>
      	</div>
	</div>
</div>

<input type="hidden" name="app_seq_no" value="">
<input type="hidden" name="app_coutpty_cd" value="">
<input type="hidden" name="option" value="PRE_DEAL_CHECK_REQ">
<input type="hidden" name="opration" value="INSERT">

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