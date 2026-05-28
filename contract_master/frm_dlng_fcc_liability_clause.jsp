<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function doSubmit()
{
	var ld_price_per = document.forms[0].ld_price_per.value;	
	var top_price_per = document.forms[0].top_price_per.value;	
	var mug_price_per = document.forms[0].mug_price_per.value;
	var msg="";
	var flag=true;
	
	if(document.forms[0].liab_lq_damg.checked==true)
 	{
	 	if(ld_price_per==null || trim(ld_price_per)=='')
		{
				msg += "Please Select Price (% of Contract Price) Field  of Liquidated Damages !!!\n";
				flag = false;
		}
	}
	if(document.forms[0].liab_take_pay.checked==true)
 	{
		if(top_price_per==null || trim(top_price_per)=='')
		{
				msg += "Please Select Price (% of Contract Price) Field of Take Or Pay  !!!\n";
				flag = false;
		}
	}
	
	if(document.forms[0].liab_makeup.checked==true)
 	{
		if(mug_price_per==null || trim(mug_price_per)=='')
		{
				msg += "Please Select Price (% of Contract Price) Field of Make Up Gas  !!!\n";
				flag = false;
		}
	}  
	if(flag)
	{
		var a = confirm("Do You Want To Submit Liability Clause?");	
		if(a)
		{
			alert("Proceed and Submit Contract! ");
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function enableLDDetails()
{
	if(document.forms[0].liab_lq_damg.checked)
	{
		document.forms[0].ld_price_per.disabled=false;
		document.forms[0].ld_promise.disabled=false;
		document.forms[0].ld_low_per.disabled=false;
		document.forms[0].ld_chk.disabled=false;
		document.forms[0].remark.disabled=false;
		document.forms[0].ld_from.disabled=false;
		document.forms[0].ld_to.disabled=false;
		
	}
	else 
	{
		document.forms[0].ld_price_per.disabled=true;
		document.forms[0].ld_promise.disabled=true;
		document.forms[0].ld_low_per.disabled=true;
		document.forms[0].ld_chk.disabled=true;
		document.forms[0].remark.disabled=true;
		document.forms[0].ld_from.disabled=true;
		document.forms[0].ld_to.disabled=true;
	}
}

function enableTOPDetails()
{
	if(document.forms[0].liab_take_pay.checked)
	{
		document.forms[0].top_price_per.disabled=false;
		document.forms[0].top_per.disabled=false;
		document.forms[0].top_obligation.disabled=false;
		document.forms[0].top_remark.disabled=false;
		document.forms[0].top_promise.disabled=false;
		document.forms[0].top_chk.disabled=false;
		document.forms[0].top_from.disabled=false;
		document.forms[0].top_to.disabled=false;
	}
	else
	{
		document.forms[0].top_price_per.disabled=true;
		document.forms[0].top_per.disabled=true;
		document.forms[0].top_obligation.disabled=true;
		document.forms[0].top_remark.disabled=true;
		document.forms[0].top_promise.disabled=true;
		document.forms[0].top_chk.disabled=true;
		document.forms[0].top_from.disabled=true;
		document.forms[0].top_to.disabled=true;
	}
}

function enableMUGDetails()
{
	if(document.forms[0].liab_makeup.checked)
	{
		document.forms[0].mug_period_per.disabled=false;
		document.forms[0].recovery_day.disabled=false;
		document.forms[0].mug_price_per.disabled=false;
		document.forms[0].mug_remark.disabled=false;
	}
	else
	{
		document.forms[0].mug_period_per.disabled=true;
		document.forms[0].recovery_day.disabled=true;
		document.forms[0].mug_price_per.disabled=true;
		document.forms[0].mug_remark.disabled=true;
	}
}

function checkValuePrecision(value,precision,maxVal,id)
{
	try
	{
		if(value == "" || value == null)
		{
			
		}
		else
		{
			var number = parseFloat(value);
	        if (isNaN(number))
	        {
	            alert("Value is not a valid number!!");
	            
	            document.getElementById(id).value = "";
	        }
	        else
	        {
	        	if(value.includes('.'))
				{
					var parts = value.split('.');
					var integerPart = parts[0];
					var decimalPart = parts[1] || '';
					 
					
					if (integerPart.length <= maxVal)
					{
						//return true;
					}
					else
					{
						alert("Provided Value should be in ("+maxVal+", "+precision+") format!!");
						document.getElementById(id).value = "";
						//return false;
					}
					
					if (decimalPart.length <= precision)
					{
						//return true;
					}
					else
					{
						alert("Provided Value should be in ("+maxVal+", "+precision+") format!!");
						document.getElementById(id).value = "";
					}
				}
				else
				{
					if (value.length <= maxVal)
					 {
						 //return true;
					 }
					 else
					 {
						 alert("Provided Value should be in ("+maxVal+", "+precision+") format!!");
						 document.getElementById(id).value = "";
					 }
				}
	        }
		}
    }
	catch (error)
	{
        //alert(error);
        return false;
    }
}

function countLdEffPrice()
{
	var per = document.forms[0].ld_price_per.value;
	var cont_price = document.forms[0].cont_price.value;
	var cont_price_unit = document.forms[0].cont_price_unit.value;
	var eff_price = "";
	
	if(cont_price!="" && per!="")
	{
		eff_price = (cont_price*per)/100;
	}
	
	if(cont_price_unit=="USD")
	{
		document.forms[0].ld_eff_price.value=eff_price.toFixed(4);
	}
	else
	{
		document.forms[0].ld_eff_price.value=eff_price.toFixed(2);
	}
}

function countTopEffPrice()
{
	var per = document.forms[0].top_price_per.value;
	var cont_price = document.forms[0].cont_price.value;
	var cont_price_unit = document.forms[0].cont_price_unit.value;
	var eff_price = "";
	
	if(cont_price!="" && per!="")
	{
		eff_price = (cont_price*per)/100;
	}
	
	if(cont_price_unit=="USD")
	{
		document.forms[0].top_eff_price.value=eff_price.toFixed(4);
	}
	else
	{
		document.forms[0].top_eff_price.value=eff_price.toFixed(2);
	}
}

function countMugEffPrice()
{
	var per = document.forms[0].mug_price_per.value;
	var cont_price = document.forms[0].cont_price.value;
	var cont_price_unit = document.forms[0].cont_price_unit.value;
	var eff_price = "";
	
	if(cont_price!="" && per!="")
	{
		eff_price = (cont_price*per)/100;
	}
	
	if(cont_price_unit=="USD")
	{
		document.forms[0].mug_eff_price.value=eff_price.toFixed(4);
	}
	else
	{
		document.forms[0].mug_eff_price.value=eff_price.toFixed(2);
	}
}

function countActObligationQty()
{
	var top_per = document.forms[0].top_per.value;
	var top_chk = document.forms[0].top_chk.value;
	var qty = "";
	var act_Obligation_qty = "";
	
	if(trim(top_per)!="" && top_chk!="")
	{
		if(top_chk=="D")
		{
			qty=document.forms[0].cont_dcq.value;
		}
		else if(top_chk=="T")
		{
			qty=document.forms[0].cont_tcq.value;
		}
		
		act_Obligation_qty=(qty*top_per)/100;
	}
	
	document.forms[0].top_obligation.value=act_Obligation_qty;
	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_master.DataBean_ContractMaster" id="cargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String clearance=request.getParameter("clearance")==null?"":request.getParameter("clearance");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String agreement_type = request.getParameter("agreement_type")==null?"F":request.getParameter("agreement_type");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String end_dt=request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String rate=request.getParameter("rate")==null?"":request.getParameter("rate");
String rate_unit=request.getParameter("rate_unit")==null?"":request.getParameter("rate_unit");
String tcq=request.getParameter("tcq")==null?"":request.getParameter("tcq");
String dcq=request.getParameter("dcq")==null?"":request.getParameter("dcq");

cargo.setCallFlag("CONT_LIABILITY_CLAUSE");
cargo.setComp_cd(owner_cd);
cargo.setCont_start_dt(start_dt);
//cargo.setCont_end_dt(end_dt);
cargo.setCounterparty_cd(counterparty_cd);
cargo.setAgmt_no(agmt_no);
cargo.setAgmt_rev_no(agmt_rev_no);
cargo.setAgreement_type(agreement_type);
cargo.setContract_type(contract_type);
cargo.setCont_no(cont_no);
cargo.setCont_rev_no(cont_rev_no);
cargo.init();

String liab_lq_damg=cargo.getLiab_lq_damg();
String ld_price_per=cargo.getLd_price_per();
String ld_promise=cargo.getLd_promise();
String ld_low_per=cargo.getLd_low_per();
String ld_chk=cargo.getLd_chk();
String remark=cargo.getRemark();
String liab_take_pay=cargo.getLiab_take_pay();
String top_price_per=cargo.getTop_price_per();
String top_promise=cargo.getTop_promise();
String top_per=cargo.getTop_per();
String top_chk=cargo.getTop_chk();
String top_obligation=cargo.getTop_obligation();
String top_remark=cargo.getTop_remark();
String liab_makeup=cargo.getLiab_makeup();
String mug_price_per=cargo.getMug_price_per();
String mug_period_per=cargo.getMug_period_per();
String recovery_day=cargo.getRecovery_day();
String mug_remark=cargo.getMug_remark();
String ld_from = cargo.getLd_from();
String ld_to = cargo.getLd_to();
String top_from = cargo.getTop_from();
String top_to = cargo.getTop_to();
String display_map_id = cargo.getDisplay_map_id();
String couterparty_abbr = cargo.getCouterparty_abbr();

if(rate_unit.equals("1"))
{
	rate_unit="INR";
}
else if(rate_unit.equals("2"))
{
	rate_unit="USD";
}

%>
<body style="pointer-events: none;" onload="enableLDDetails();enableTOPDetails();enableMUGDetails();countLdEffPrice();countTopEffPrice();countMugEffPrice();countActObligationQty();">
<form method="post" action="../servlet/Frm_ContracMaster">
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
					    	<%=couterparty_abbr%> [<%=display_map_id%>] Liability Clause
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<input type="checkbox" class="form-check-input" name="liab_lq_damg" value="Y" onclick="enableLDDetails()" <%if(liab_lq_damg.equals("Y")){ %>checked<%} %>>&nbsp;<label class="form-label"><b>Liquidated Damages</b></label>  
						</div>
					</div>
					<!-- <div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Price (% of Contract Price)</label>
					</div> -->
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Price (% of Contract Price)<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="ld_price_per" id="ld_price_per" value="<%=ld_price_per %>" size="8" maxlength="8" onblur="checkValuePrecision(this.value,'2','3','ld_price_per');negNumber(this);appPercentage(this);countLdEffPrice();" >
			      						<span class="input-group-text">%</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Effective Price</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="ld_eff_price" id="ld_eff_price" value="" size="8" maxlength="8" readonly>
			      						<span class="input-group-text"><%=rate_unit %>/MMBTU</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
					</div>
					<div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">
		    				<font color=""><b>Promise Quantity Frequency</b></font>
		    			</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<div class="input-group input-group-sm" >
					    				<select class="form-select form-select-sm" name="ld_promise">
					    					<option value="D">Daily</option>
					                        <option value="W">Weekly</option>
					                        <option value="F">Fortnightly </option>
					                        <option value="M">Monthly </option>
					                        <option value="Q">Quarterly </option>
					                        <option value="IC">Invoice Cycle</option>
					                        <option value="T">TCQ</option>
					                        <option value="DP">Defined Period</option>
					                        <option value="SP">Supply Period</option>
					    				</select>
					    				<script>document.forms[0].ld_promise.value="<%=ld_promise%>"</script>
					    			</div>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-6 col-xs-6 col-md-6">
							<div class="form-group row">  
								<div class="col-sm-1 col-xs-1 col-md-1">
									<div class="form-group row">				    			
										<div class="col-auto">
											<label class="form-label">&nbsp;<b>From</b></label>
										</div>
									</div>
								</div>
								<div class="col-sm-5 col-xs-5 col-md-5">
									<div class="input-group input-group-sm"">
										<input type="text" class="form-control form-control-sm date fmsdtpick" name="ld_from" id="ld_from" value="<%=ld_from %>" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">	
										<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>	
									</div>							
								</div>	
								<div class="col-sm-1 col-xs-1 col-md-1">
									<div class="form-group row">				    			
										<div class="col-auto">
											<label class="form-label">&nbsp;<b>To</b></label>
										</div>
									</div>
								</div>
								<div class="col-sm-5 col-xs-5 col-md-5">
									<div class="input-group input-group-sm"">
										<input type="text" class="form-control form-control-sm date fmsdtpick" name="ld_to" id="ld_to" value="<%=ld_to %>" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">	
										<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
									</div>							
								</div>
							</div>
						</div>
				    </div>
				    <div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">
		    				<font color=""><b>Liability</b></font>
		    			</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">  
								<div class="col-sm-3 col-xs-3 col-md-3">
									<div class="input-group input-group-sm"">
										<input type="text" class="form-control form-control-sm" name="ld_low_per" id="ld_low_per" value="<%=ld_low_per%>" onblur="checkValuePrecision(this.value,'2','3','ld_low_per');negNumber(this);appPercentage(this);">	
										<span class="input-group-text">%</span>	
									</div>							
								</div>	
								<div class="col-sm-3 col-xs-3 col-md-3">
									<div class="form-group row">				    			
										<div class="col-auto">
											<label class="form-label">&nbsp;<b>of Lower of</b></label>
										</div>
									</div>
								</div>
								<div class="col-sm-6 col-xs-6 col-md-6">
									<div class="form-group row">				    			
						  				<div class="input-group input-group-sm" >
						    				<select class="form-select form-select-sm" name="ld_chk">
						    					<option value="L" selected="selected"> DCQ/PNDCQ/MDCQ </option>
						    					<!-- <option value="" selected="selected">--Select--</option>
						    					<option value="D">DCQ</option>
						                        <option value="P">PNDCQ</option>
						                        <option value="M">MDCQ </option> -->
						    				</select>
						    				<script>document.forms[0].ld_chk.value="<%=ld_chk%>"</script>
						    			</div>
									</div>
								</div>
							</div>
						</div>	
				    </div>
				    <div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Remark</b></label>
						</div>
						
						<div class="col-sm-10 col-xs-10 col-md-10">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="remark" value="<%=remark %>" size="80" maxlength="200">
		      						</div>
				    			</div>
				    		</div>
				    	</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<input type="checkbox" class="form-check-input" name="liab_take_pay" value="Y" onclick="enableTOPDetails()" <%if(liab_take_pay.equals("Y")){ %>checked<%} %>>&nbsp;<label class="form-label"><b>Take or Pay</b></label> 
						</div>
					</div>
					<!-- <div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Price (% of Contract Price)</label>
					</div> -->
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Price (% of Contract Price)<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="top_price_per" id="top_price_per" value="<%=top_price_per %>" size="8" maxlength="8" onblur="checkValuePrecision(this.value,'2','3','top_price_per');negNumber(this);appPercentage(this);countTopEffPrice()">
			      						<span class="input-group-text">%</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Effective Price</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="top_eff_price" id="top_eff_price" value="" size="8" maxlength="8" readonly >
			      						<span class="input-group-text"><%=rate_unit %>/MMBTU</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
					</div>
					 <div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">
		    				<font color=""><b>Promise Quantity Frequency</b></font>
		    			</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="input-group input-group-sm" >
				    				<select class="form-select form-select-sm" name="top_promise">
				    					<option value="D">Daily</option>
				                        <option value="W">Weekly</option>
				                        <option value="F">Fortnightly </option>
				                        <option value="M">Monthly </option>
				                        <option value="Q">Quarterly </option>
				                        <option value="IC">Invoice Cycle</option>
				                        <option value="T">TCQ</option>
				                        <option value="DP">Defined Period</option>
				                        <option value="SP">Supply Period</option>
				    				</select>
				    				<script>document.forms[0].top_promise.value="<%=top_promise%>"</script>
				    			</div>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-6 col-xs-6 col-md-6">
							<div class="form-group row">  
								<div class="col-sm-1 col-xs-1 col-md-1">
									<div class="form-group row">				    			
										<div class="col-auto">
											<label class="form-label">&nbsp;<b>From</b></label>
										</div>
									</div>
								</div>
								<div class="col-sm-5 col-xs-5 col-md-5">
									<div class="input-group input-group-sm"">
										<input type="text" class="form-control form-control-sm date fmsdtpick" name="top_from" id="top_from" value="<%=top_from %>" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">	
										<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>	
									</div>							
								</div>	
								<div class="col-sm-1 col-xs-1 col-md-1">
									<div class="form-group row">				    			
										<div class="col-auto">
											<label class="form-label">&nbsp;<b>To</b></label>
										</div>
									</div>
								</div>
								<div class="col-sm-5 col-xs-5 col-md-5">
									<div class="input-group input-group-sm"">
										<input type="text" class="form-control form-control-sm date fmsdtpick" name="top_to" id="top_to" value="<%=top_to %>" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">	
										<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>	
									</div>							
								</div>
							</div>
						</div>
				    </div>
					<div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">
		    				<font color=""><b>Take or Pay</b></font>
		    			</div>
		    			<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">  
								<div class="col-sm-5 col-xs-5 col-md-5">
									<div class="input-group input-group-sm" >
										<input type="text" class="form-control form-control-sm" name="top_per" id="top_per" value="<%=top_per %>" size="5" maxlength="8" onblur="checkValuePrecision(this.value,'2','3','top_per');negNumber(this);appPercentage(this);countActObligationQty();">
										<span class="input-group-text">%</span>
									</div>
								</div>
								<div class="col-sm-1 col-xs-1 col-md-1">
									<div class="form-group row">				    			
										<div class="col-auto">
											<label class="form-label">&nbsp;of</label>
										</div>
									</div>
								</div>
								<div class="col-sm-6 col-xs-6 col-md-6">
									<div class="form-group row">				    			
						  				<div class="input-group input-group-sm" >
						    				<select class="form-select form-select-sm" name="top_chk" onchange="countActObligationQty();">
						    					<option value="" selected="selected">--Select--</option>
						    					<option value="D">DCQ</option>
						                        <option value="T">TCQ</option>
						    				</select>
						    				<script>document.forms[0].top_chk.value="<%=top_chk%>"</script>
						    			</div>
									</div>
								</div>
							</div>
						</div>
				    </div>
				    <div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">
		    				<font color=""><b>Actual Obligation To Take</b></font>
		    			</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="top_obligation" value="<%=top_obligation %>" size="8" maxlength="11" onblur="negNumber(this);" readonly>
			      						<span class="input-group-text">MMBTU</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
				    </div>
				   
				    <div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Remark</b></label>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="top_remark" value="<%=top_remark %>" size="80" maxlength="200">
		      						</div>
				    			</div>
				    		</div>
				    	</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<input type="checkbox" class="form-check-input" name="liab_makeup" value="Y" onclick="enableMUGDetails()" <%if(liab_makeup.equals("Y")){ %>checked<%} %>>&nbsp;<label class="form-label"><b>Make Up Gas</b></label> 
						</div>
					</div>
					<!-- <div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Price (% of Contract Price)</label>
					</div> -->
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Price (% of Contract Price)<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="mug_price_per" id="mug_price_per" value="<%=mug_price_per %>" size="8" maxlength="8" onblur="checkValuePrecision(this.value,'2','3','mug_price_per');negNumber(this);appPercentage(this);countMugEffPrice();">
			      						<span class="input-group-text">%</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Effective Price</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="mug_eff_price" id="mug_eff_price" value="" size="8" maxlength="8" readonly>
			      						<span class="input-group-text"><%=rate_unit %>/MMBTU</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Make Up Period</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">  
								<div class="col-sm-6 col-xs-6 col-md-6">
									<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="mug_period_per" id="mug_period_per" value="<%=mug_period_per %>" size="5" maxlength="8" onblur="checkValuePrecision(this.value,'2','2','mug_period_per');negNumber(this);appPercentage(this);">
			      						<span class="input-group-text">%</span>
		      						</div>
								</div>
								<div class="col-sm-6 col-xs-6 col-md-6">
									<div class="form-group row">				    			
										<div class="col-auto">
											<label class="form-label">&nbsp;of Supply Period</label>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Recovery Period</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="recovery_day" id="recovery_day" value="<%=recovery_day %>" size="5" maxlength="5" onblur="checkValuePrecision(this.value,'0','3','recovery_day');negNumber(this);">
			      						<span class="input-group-text">Days</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
					</div>
					
				    <div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Remark</b></label>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="mug_remark" value="<%=mug_remark %>" size="80" maxlength="200">
		      						</div>
				    			</div>
				    		</div>
				    	</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="CONT_LIABILITY_CLAUSE">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="start_dt" value="<%=start_dt%>">
<input type="hidden" name="agreement_type" value="<%=agreement_type%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>">
<input type="hidden" name="cont_price" value="<%=rate%>">
<input type="hidden" name="cont_price_unit" value="<%=rate_unit%>">
<input type="hidden" name="cont_tcq" value="<%=tcq%>">
<input type="hidden" name="cont_dcq" value="<%=dcq%>">

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