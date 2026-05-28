<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var customer_cd = document.forms[0].customer_cd.value;
	var clearance = document.forms[0].clearance.value;
	var prev_clearance = document.forms[0].prev_clearance.value;
	
	if(prev_clearance != clearance)
	{
		customer_cd="0";
	}
	
	var multiCountpty=document.forms[0].multiCountpty.value;
	var multiAgmtNo=document.forms[0].multiAgmtNo.value;
	var multiAgmtRev=document.forms[0].multiAgmtRev.value;
	var multiContNo=document.forms[0].multiContNo.value;
	var multiContRev=document.forms[0].multiContRev.value;
	var multiContTyp=document.forms[0].multiContTyp.value;
	var multiCargoNo=document.forms[0].multiCargoNo.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_allocation.jsp?customer_cd="+customer_cd+"&clearance="+clearance+
		"&multiCountpty="+multiCountpty+"&multiAgmtNo="+multiAgmtNo+"&multiAgmtRev="+multiAgmtRev+
		"&multiContNo="+multiContNo+"&multiContRev="+multiContRev+"&multiContTyp="+multiContTyp+"&multiCargoNo="+multiCargoNo+
		"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function goBack()
{
	var u = document.forms[0].u.value;
	
	var a = confirm("Are You sure You want to go Back?")
	if(a)
	{
		var url = "frm_energy_bank.jsp?u="+u;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}

function Enabled(index)
{
	var counterparty_cd=document.getElementById("counterparty_cd"+index);
	var agmt_no=document.getElementById("agmt_no"+index);
	var agmt_rev_no=document.getElementById("agmt_rev_no"+index);
	var cont_no=document.getElementById("cont_no"+index);
	var cont_rev_no=document.getElementById("cont_rev_no"+index);
	var contract_type=document.getElementById("contract_type"+index);
	var rate=document.getElementById("rate"+index);
	var rate_unit=document.getElementById("rate_unit"+index);
	var avg_margin=document.getElementById("avg_margin"+index);
	
	var sel_cargo_size = document.forms[0].sel_cargo_size;
	var prev_index = document.forms[0].prev_index;
	var selected_index = document.forms[0].selected_index;
	
	counterparty_cd.disabled = false;
	agmt_no.disabled = false;
	agmt_rev_no.disabled = false;
	cont_no.disabled = false;
	cont_rev_no.disabled = false;
	contract_type.disabled = false;
	rate.disabled = false;
	rate_unit.disabled = false;
	avg_margin.disabled = false;
	
	for(var i=0; i<parseInt(sel_cargo_size.value); i++)
	{
		var sel_cont_no=document.getElementById("sel_cont_no"+index+"_"+i);
		var sel_cargo_no=document.getElementById("sel_cargo_no"+index+"_"+i);
		var sel_rate=document.getElementById("sel_rate"+index+"_"+i);
		var sel_rate_unit=document.getElementById("sel_rate_unit"+index+"_"+i);
		
		var alloc_qty=document.getElementById("alloc_qty"+index+"_"+i);
		var margin=document.getElementById("margin"+index+"_"+i);
		var total_margin=document.getElementById("total_margin"+index+"_"+i);
		
		sel_cont_no.disabled = false;
		sel_cargo_no.disabled = false;
		sel_rate.disabled = false;
		sel_rate_unit.disabled = false;
		
		alloc_qty.disabled = false;
		margin.disabled = false;
		total_margin.disabled = false;
	}
	
	if(trim(prev_index.value) != "")
	{
		Disabled(prev_index.value);
	}
	
	prev_index.value=index;
	selected_index.value=index;
}

function Disabled(index)
{
	var counterparty_cd=document.getElementById("counterparty_cd"+index);
	var agmt_no=document.getElementById("agmt_no"+index);
	var agmt_rev_no=document.getElementById("agmt_rev_no"+index);
	var cont_no=document.getElementById("cont_no"+index);
	var cont_rev_no=document.getElementById("cont_rev_no"+index);
	var contract_type=document.getElementById("contract_type"+index);
	var rate=document.getElementById("rate"+index);
	var rate_unit=document.getElementById("rate_unit"+index);
	var avg_margin=document.getElementById("avg_margin"+index);
	
	var sel_cargo_size = document.forms[0].sel_cargo_size;
	
	counterparty_cd.disabled = true;
	agmt_no.disabled = true;
	agmt_rev_no.disabled = true;
	cont_no.disabled = true;
	cont_rev_no.disabled = true;
	contract_type.disabled = true;
	rate.disabled = true;
	rate_unit.disabled = true;
	avg_margin.disabled = true;
	
	for(var i=0; i<parseInt(sel_cargo_size.value); i++)
	{
		var sel_cont_no=document.getElementById("sel_cont_no"+index+"_"+i);
		var sel_cargo_no=document.getElementById("sel_cargo_no"+index+"_"+i);
		var sel_rate=document.getElementById("sel_rate"+index+"_"+i);
		var sel_rate_unit=document.getElementById("sel_rate_unit"+index+"_"+i);
		
		var alloc_qty=document.getElementById("alloc_qty"+index+"_"+i);
		var margin=document.getElementById("margin"+index+"_"+i);
		var total_margin=document.getElementById("total_margin"+index+"_"+i);
		
		sel_cont_no.disabled = true;
		sel_cargo_no.disabled = true;
		sel_rate.disabled = true;
		sel_rate_unit.disabled = true;
		
		alloc_qty.disabled = true;
		margin.disabled = true;
		total_margin.disabled = true;
	}
}

function checkBalanceQty(index)
{
	var sel_cargo_size = document.forms[0].sel_cargo_size;
	
	for(var i=0; i<parseInt(sel_cargo_size.value); i++)
	{
		var sel_cont_no=document.getElementById("sel_cont_no"+index+"_"+i);
		var sel_cargo_no=document.getElementById("sel_cargo_no"+index+"_"+i);
		var sel_balance_qty=document.getElementById("sel_balance_qty"+index+"_"+i);
		var alloc_qty=document.getElementById("alloc_qty"+index+"_"+i);
		
		if(alloc_qty.value!=null && alloc_qty.value!="")
	    {
			if(parseFloat(alloc_qty.value) > parseFloat(sel_balance_qty.value))
			{
				alert("Allocated MMBTU should be <= Balance MMBTU for ROW - "+(parseInt(index)+1)+" (Cont#"+sel_cont_no.value+")")	
				alloc_qty.value=""
				return false;
			}
		}
	}
}

function totalAlloc(index)
{
	var counterparty_cd=document.getElementById("counterparty_cd"+index);
	var agmt_no=document.getElementById("agmt_no"+index);
	var agmt_rev_no=document.getElementById("agmt_rev_no"+index);
	var cont_no=document.getElementById("cont_no"+index);
	var cont_rev_no=document.getElementById("cont_rev_no"+index);
	var contract_type=document.getElementById("contract_type"+index);
	var rate=document.getElementById("rate"+index);
	var rate_unit=document.getElementById("rate_unit"+index);
	var tcq=document.getElementById("tcq"+index);
	var avg_margin=document.getElementById("avg_margin"+index);
	
	var total_alloc_qty = document.getElementById("total_alloc_qty"+index)
	var rem_alloc_qty = document.getElementById("rem_alloc_qty"+index)
	
	var sel_cargo_size = document.forms[0].sel_cargo_size;
	var exchgRate = document.forms[0].exchgRate;
	
	var totalAllocatedQty = parseFloat("0");
	var remAllocatedQty = parseFloat("0");
	
	var SumTotalMargin = parseFloat("0");
	
	for(var i=0; i<parseInt(sel_cargo_size.value); i++)
	{
		var sel_cont_no=document.getElementById("sel_cont_no"+index+"_"+i);
		var sel_cargo_no=document.getElementById("sel_cargo_no"+index+"_"+i);
		var sel_rate=document.getElementById("sel_rate"+index+"_"+i);
		var sel_rate_unit=document.getElementById("sel_rate_unit"+index+"_"+i);
		
		var alloc_qty=document.getElementById("alloc_qty"+index+"_"+i);
		var margin=document.getElementById("margin"+index+"_"+i);
		var total_margin=document.getElementById("total_margin"+index+"_"+i);
		
		var difference = parseFloat("0.0000");
		if(sel_rate.value!=null && sel_rate.value!="" && parseFloat(rate.value)!='0' && rate.value!=null && rate.value!="")
		{
			var SalesPrice = parseFloat("0.0000");
	   		var CostPrice = parseFloat("0.0000");
	   		
	   		//FOR SELL
	   		if(rate_unit.value=="1")
	   		{
				if(exchgRate.value!=null && exchgRate.value!="" && parseFloat(exchgRate.value)!='0')
	   			{
					SalesPrice = (parseFloat(rate.value)/parseFloat(exchgRate.value))
				}
				else
				{
			   		SalesPrice = parseFloat("0.0000");
				}
	   		}
	   		else
	   		{
	   			SalesPrice = parseFloat(rate.value)
	   		}
	   		
	   		//FOR BUY
	   		if(sel_rate_unit.value=="1")
	   		{
				if(exchgRate.value!=null && exchgRate.value!="" && parseFloat(exchgRate.value)!='0')
	   			{
					CostPrice = (parseFloat(sel_rate.value)/parseFloat(exchgRate.value))
				}
				else
				{
					CostPrice = parseFloat("0.0000");
				}
	   		}
	   		else
	   		{
	   			CostPrice = parseFloat(sel_rate.value)
	   		}
	   		
	   		difference = parseFloat(SalesPrice) - parseFloat(CostPrice)
			margin.value = round(difference,4);
		}
	   	else
	   	{
	   		margin.value="0.0000"
	   	}
		
		if(alloc_qty.value!=null && alloc_qty.value!="")
	    {
			var total = parseFloat("0.0000");
		    	total = parseFloat(alloc_qty.value) * parseFloat(difference);
		    	total_margin.value = round(total,4);
		    	
		    	SumTotalMargin = parseFloat(SumTotalMargin) + parseFloat(round(total,4)); 
		}
		
		if(alloc_qty.value!="")
		{
			totalAllocatedQty = parseFloat(totalAllocatedQty) + parseFloat(alloc_qty.value);
			remAllocatedQty = parseFloat(tcq.value) - parseFloat(totalAllocatedQty);
		}
	}
	
	if((totalAllocatedQty>0 || totalAllocatedQty<0) && (SumTotalMargin>0 || SumTotalMargin<0))
	{
		avg_margin.value = round((SumTotalMargin/totalAllocatedQty),4);
	}
	else if((totalAllocatedQty>0 || totalAllocatedQty<0) && SumTotalMargin==0)
	{
		avg_margin.value = "0.0000";
	}
	else
    {
    	avg_margin.value = "";
    }
	
	total_alloc_qty.value=parseFloat(totalAllocatedQty);
	rem_alloc_qty.value=parseFloat(remAllocatedQty);
}

function doSubmit()
{
	var select_rd = document.forms[0].select_rd;
	var sel_cargo_size = document.forms[0].sel_cargo_size;
	var exchgRate = document.forms[0].exchgRate;
	
	var chkCount=parseInt("0");
	var allocCount=parseInt("0");
	
	var flag = true;
	
	var alertInfo="";
	
	if(select_rd!=null && select_rd!=undefined)
	{
		if(select_rd.length!=undefined)
		{
			for(var i=0; i<select_rd.length; i++)
			{
				if(select_rd[i].checked)
				{
					chkCount++;
					var rate=document.getElementById("rate"+i);
					var rate_unit=document.getElementById("rate_unit"+i);
					var rate_unit_nm=document.getElementById("rate_unit_nm"+i);
					
					var tcq=document.getElementById("tcq"+i);
					var avg_margin=document.getElementById("avg_margin"+i);
					var total_alloc_qty = document.getElementById("total_alloc_qty"+i)
					var rem_alloc_qty = document.getElementById("rem_alloc_qty"+i)
					
					var pur_cont_dtl="";
					for(var j=0; j<parseInt(sel_cargo_size.value); j++)
					{
						var dis_sel_cont_no=document.getElementById("dis_sel_cont_no"+i+"_"+j);
						var sel_cont_no=document.getElementById("sel_cont_no"+i+"_"+j);
						var sel_rate=document.getElementById("sel_rate"+i+"_"+j);
						var sel_rate_unit_nm=document.getElementById("sel_rate_unit_nm"+i+"_"+j);
						var alloc_qty=document.getElementById("alloc_qty"+i+"_"+j);
						
						if(trim(alloc_qty.value)!="")
						{
							allocCount++;
							
							if(pur_cont_dtl == "")
							{
								pur_cont_dtl+=""+dis_sel_cont_no.value;
							}
							else
							{
								pur_cont_dtl+=", "+dis_sel_cont_no.value;
							}
						}
					}
					
					alertInfo="Purchase Contacts = "+pur_cont_dtl+
						"\nSale Price = "+rate.value+" ("+rate_unit_nm.value+"/MMBTU)\nTotal Qty = "+total_alloc_qty.value+"  (MMBTU)\n";
					if(rate_unit.value=="1")
					{
						alertInfo+="Exchange Rate = "+exchgRate.value+" (Shell Treasury Rate)\n";
					}
					alertInfo+="Weighted Avg Margin = "+avg_margin.value+" ($/MMBTU)";
					
					if(parseInt(allocCount) <= 0)
					{
						alert("Enter Allocated Qty in atleast ONE(1) Purchase Contract for ROW - "+(parseInt(i)+1)+"!");
						flag= false;
					}
					
					if(total_alloc_qty.value != "")
					{
						if(parseFloat(tcq.value) != parseFloat(total_alloc_qty.value))	
						{
							alert("Total Allocated Qty should equal Contract TCQ for the selected Contract(row#"+(parseInt(i)+1)+")!");
							flag= false;
						}
					}
					else
					{
						alert("Total Allocated Qty should equal Contract TCQ for the selected Contract(row#"+(parseInt(i)+1)+")!");
						flag= false;
					}
				}
			}
		}
		else
		{
			var i=0;
			if(select_rd.checked)
			{
				chkCount++;
				var rate=document.getElementById("rate"+i);
				var rate_unit=document.getElementById("rate_unit"+i);
				var rate_unit_nm=document.getElementById("rate_unit_nm"+i);
				
				var tcq=document.getElementById("tcq"+i);
				var avg_margin=document.getElementById("avg_margin"+i);
				var total_alloc_qty = document.getElementById("total_alloc_qty"+i)
				var rem_alloc_qty = document.getElementById("rem_alloc_qty"+i)
				
				var pur_cont_dtl="";
				for(var j=0; j<parseInt(sel_cargo_size.value); j++)
				{
					var dis_sel_cont_no=document.getElementById("dis_sel_cont_no"+i+"_"+j);
					var sel_cont_no=document.getElementById("sel_cont_no"+i+"_"+j);
					var sel_rate=document.getElementById("sel_rate"+i+"_"+j);
					var sel_rate_unit_nm=document.getElementById("sel_rate_unit_nm"+i+"_"+j);
					var alloc_qty=document.getElementById("alloc_qty"+i+"_"+j);
					
					if(trim(alloc_qty.value)!="")
					{
						allocCount++;
						
						if(pur_cont_dtl == "")
						{
							pur_cont_dtl+=""+dis_sel_cont_no.value;
						}
						else
						{
							pur_cont_dtl+=", "+dis_sel_cont_no.value;
						}
					}	
				}
				
				alertInfo="Purchase Contacts = "+pur_cont_dtl+
					"\nSale Price = "+rate.value+" ("+rate_unit_nm.value+"/MMBTU)\nTotal Qty = "+total_alloc_qty.value+" (MMBTU)\n";
				if(rate_unit.value=="1")
				{
					alertInfo+="Exchange Rate = "+exchgRate.value+" (Shell Treasury Rate)\n";
				}
				alertInfo+="Weighted Avg Margin = "+avg_margin.value+" ($/MMBTU)";
				if(parseInt(allocCount) <= 0)
				{
					alert("Enter Allocated Qty in atleast ONE(1) Purchase Contract for ROW - "+(parseInt(i)+1)+"!");
					flag= false;
				}
				
				if(total_alloc_qty.value != "")
				{
					if(parseFloat(tcq.value) != parseFloat(total_alloc_qty.value))	
					{
						alert("Total Allocated Qty should equal Contract TCQ for the selected Contract(row#"+(parseInt(i)+1)+")!");
						flag= false;
					}
				}
				else
				{
					alert("Total Allocated Qty should equal Contract TCQ for the selected Contract(row#"+(parseInt(i)+1)+")!");
					flag= false;
				}
			}
		}
	}
	
	if(parseInt(chkCount) <= 0)
	{
		alert("Select atleast ONE(1) Row for Submit!");
		return false;
	}
	else if(flag)
	{
		var a = confirm(alertInfo+"\n\nDo you want to Submit MMBTU Allocation?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_EnergyBank" id="energyBank" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
NumberFormat nf = new DecimalFormat("###########0.00");
NumberFormat nf2 = new DecimalFormat("###########0.0000");

String multiCountpty=request.getParameter("multiCountpty")==null?"":request.getParameter("multiCountpty");
String multiAgmtNo=request.getParameter("multiAgmtNo")==null?"":request.getParameter("multiAgmtNo");
String multiAgmtRev=request.getParameter("multiAgmtRev")==null?"":request.getParameter("multiAgmtRev");
String multiContNo=request.getParameter("multiContNo")==null?"":request.getParameter("multiContNo");
String multiContRev=request.getParameter("multiContRev")==null?"":request.getParameter("multiContRev");
String multiContTyp=request.getParameter("multiContTyp")==null?"":request.getParameter("multiContTyp");
String multiCargoNo=request.getParameter("multiCargoNo")==null?"":request.getParameter("multiCargoNo");

String customer_cd=request.getParameter("customer_cd")==null?"0":request.getParameter("customer_cd");
String clearance=request.getParameter("clearance")==null?"KYC":request.getParameter("clearance");

energyBank.setCallFlag("MMBTU_ALLOCATION");
energyBank.setMultiCountpty(multiCountpty);
energyBank.setMultiAgmtNo(multiAgmtNo);
energyBank.setMultiAgmtRev(multiAgmtRev);
energyBank.setMultiContNo(multiContNo);
energyBank.setMultiContTyp(multiContTyp);
energyBank.setMultiCargoNo(multiCargoNo);
energyBank.setComp_cd(owner_cd);
energyBank.setCustomer_cd(customer_cd);
energyBank.setClearance(clearance);
energyBank.init();

String[] SEL_CARGO = multiContNo.split("@@");

Vector VCUSTOMER_CD = energyBank.getVCUSTOMER_CD();
Vector VCUSTOMER_NM = energyBank.getVCUSTOMER_NM();
Vector VCUSTOMER_ABBR = energyBank.getVCUSTOMER_ABBR();

Vector VCOUNTERPARTY_CD = energyBank.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = energyBank.getVCOUNTERPARTY_ABBR();
Vector VAGMT_NO = energyBank.getVAGMT_NO();
Vector VAGMT_REV_NO = energyBank.getVAGMT_REV_NO();
Vector VCONT_NO = energyBank.getVCONT_NO();
Vector VCONT_REV_NO = energyBank.getVCONT_REV_NO();
Vector VCONTRACT_TYPE = energyBank.getVCONTRACT_TYPE();
Vector VTCQ = energyBank.getVTCQ();
Vector VRATE = energyBank.getVRATE();
Vector VRATE_UNIT = energyBank.getVRATE_UNIT();
Vector VRATE_UNIT_NM = energyBank.getVRATE_UNIT_NM();
Vector VDISPLAY_CONT_DTL = energyBank.getVDISPLAY_CONT_DTL();
Vector VCONT_REF = energyBank.getVCONT_REF();

Vector VSEL_TRADER_ABBR = energyBank.getVSEL_TRADER_ABBR();
Vector VSEL_TRADER_NM = energyBank.getVSEL_TRADER_NM();
Vector VSEL_AGMT_NO = energyBank.getVSEL_AGMT_NO();
Vector VSEL_AGMT_REV_NO = energyBank.getVSEL_AGMT_REV_NO();
Vector VSEL_CONT_NO = energyBank.getVSEL_CONT_NO();
Vector VSEL_CONT_REV_NO = energyBank.getVSEL_CONT_REV_NO();
Vector VSEL_RATE = energyBank.getVSEL_RATE();
Vector VSEL_RATE_UNIT = energyBank.getVSEL_RATE_UNIT();
Vector VSEL_RATE_UNIT_NM = energyBank.getVSEL_RATE_UNIT_NM();
Vector VSEL_BALANCE_QTY = energyBank.getVSEL_BALANCE_QTY();
Vector VSEL_DISPLAY_CONT_DTL = energyBank.getVSEL_DISPLAY_CONT_DTL();
Vector VSEL_COLOR = energyBank.getVSEL_COLOR();
Vector VSEL_CARGO_NO = energyBank.getVSEL_CARGO_NO();

double exchgRate = energyBank.getExchgRate();

%>
<body>
<%@ include file="../home/header.jsp"%>
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
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	MMBTU Allocation
					    </div>
					    <span class="btn rounded-circle" style="background:var(--header_color);color:var(--header_font_color);" title="Back" onclick="goBack();">
						  &nbsp;<i class="fa fa-step-backward fa-2x"></i>&nbsp;
						</span>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Customer<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-auto">
									<select class="form-select form-select-sm" name="clearance" onchange="refresh();">
										<option value="KYC">KYC</option>
										<option value="IGX">IGX</option>
									</select>
									<script>document.forms[0].clearance.value="<%=clearance%>"</script>
								</div>
				    			<div class="col">
				      				<select class="form-select form-select-sm" name="customer_cd" onchange="refresh();">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCUSTOMER_CD.size();i++){ %>
										<option value="<%=VCUSTOMER_CD.elementAt(i)%>"><%=VCUSTOMER_ABBR.elementAt(i)%> - <%=VCUSTOMER_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].customer_cd.value="<%=customer_cd%>"</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
						</div>
					</div>
				</div>
				<!-- <div class="card-header cdheader"> -->
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="form-group row justify-content-center">
								<div class="col-auto">
									<a class="btn" style="background:#ccffcc;">Unloaded</a>
								</div>
								<div class="col-auto">
									<a class="btn" style="background:#ff66d9;">Expected</a>
								</div>
							</div>
						</div>
					</div>
				<!-- </div> -->
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th rowspan="2">Sr#</th>
										<th rowspan="2">Sell Contract#</th>
										<th colspan="5" style="background:#e6e6e6;">Purchase Detail(s)</th>
										<th colspan="6">Sell Detail(s)</th>
									</tr>
									<tr>
										<th style="background:#e6e6e6;">Trader</th>
										<th style="background:#e6e6e6;">Contract#</th>
										<th style="background:#e6e6e6;">Cost Price</th>
										<th style="background:#e6e6e6;">Currency/ MMBTU</th>
										<th style="background:#e6e6e6;">Balance MMBTU</th>
										<th>Sales Price</th>
										<th>Currency/ MMBTU</th>
										<th>TCQ</th>
										<th>Allocated Qty<br>(MMBTU)</th>
										<th>Total Allocated Qty<br>(MMBTU)</th>
										<th>Remaining Allocated Qty<br>(MMBTU)</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCONT_NO.size()>0){ %>
									<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
										<%for(int j=0; j<VSEL_CONT_NO.size(); j++)
										{ 	
											int rowspan=VSEL_CONT_NO.size();
										%>
										<tr>
											<%if(j==0){ %>
											<td rowspan="<%=rowspan%>" align="center">
												<input type="radio" name="select_rd" onclick="Enabled('<%=i%>')">&nbsp;
												<%=i+1%>
											</td>
											<td rowspan="<%=rowspan%>">
												<%=VCOUNTERPARTY_ABBR.elementAt(i)%>-<font color="blue"><%=VDISPLAY_CONT_DTL.elementAt(i)%></font>
												<%if(!VCONT_REF.elementAt(i).equals("")){ %><br>(<%=VCONT_REF.elementAt(i)%>)<%}%>
												<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>" disabled>
												<input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>" disabled>
												<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=i%>" value="<%=VAGMT_REV_NO.elementAt(i)%>" disabled>
												<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>" disabled>
												<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=i%>" value="<%=VCONT_REV_NO.elementAt(i)%>" disabled>
												<input type="hidden" name="contract_type" id="contract_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled> 
											</td>
											<%} %>
											<td align="center" style="background:#e6e6e6;" title="<%=VSEL_TRADER_NM.elementAt(j)%>">
												<%=VSEL_TRADER_ABBR.elementAt(j)%>
											</td>
											<td align="center" style="background:#e6e6e6;">
												<div style="width:120px;">
													<input type="text" size="10" name="dis_sel_cont_no<%=i%>" id="dis_sel_cont_no<%=i%>_<%=j%>" class="form-control form-control-sm" value="<%=VSEL_DISPLAY_CONT_DTL.elementAt(j) %>" style="background:<%=VSEL_COLOR.elementAt(j)%>" readOnly>
													<input type="hidden" name="sel_cont_no<%=i%>" id="sel_cont_no<%=i%>_<%=j%>" value="<%=VSEL_CONT_NO.elementAt(j)%>" disabled>
													<input type="hidden" name="sel_cargo_no<%=i%>" id="sel_cargo_no<%=i%>_<%=j%>" value="<%=VSEL_CARGO_NO.elementAt(j)%>" disabled>
												</div>
											</td>
											<td align="center" style="background:#e6e6e6;">
												<div style="width:75px;">
													<input type="text" class="form-control form-control-sm" name="sel_rate<%=i%>" id="sel_rate<%=i%>_<%=j%>" value="<%=VSEL_RATE.elementAt(j) %>" style="text-align:right;" disabled readOnly>
												</div>
											</td>
											<td align="center" style="background:#e6e6e6;">
												<%=VSEL_RATE_UNIT_NM.elementAt(j) %>
												<input type="hidden" name="sel_rate_unit<%=i%>" id="sel_rate_unit<%=i%>_<%=j%>" value="<%=VSEL_RATE_UNIT.elementAt(j)%>" disabled>
												<input type="hidden" name="sel_rate_unit_nm<%=i%>" id="sel_rate_unit_nm<%=i%>_<%=j%>" value="<%=VSEL_RATE_UNIT_NM.elementAt(j)%>">
											</td>
											<td align="center" style="background:#e6e6e6;">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="sel_balance_qty<%=i%>" id="sel_balance_qty<%=i%>_<%=j%>" value="<%=VSEL_BALANCE_QTY.elementAt(j) %>" style="text-align:right;" readOnly>
												</div>
											</td>
											
											<%if(j==0){ %>
											<td rowspan="<%=rowspan%>" align="right">
												<%=VRATE.elementAt(i) %>
												<input type="hidden" name="rate" id="rate<%=i%>" value="<%=VRATE.elementAt(i)%>" disabled>
											</td>
											<td rowspan="<%=rowspan%>" align="center">
												<%=VRATE_UNIT_NM.elementAt(i) %>
												<input type="hidden" name="rate_unit" id="rate_unit<%=i%>" value="<%=VRATE_UNIT.elementAt(i)%>" disabled>
												<input type="hidden" name="rate_unit_nm" id="rate_unit_nm<%=i%>" value="<%=VRATE_UNIT_NM.elementAt(i)%>">
											</td>
											<td rowspan="<%=rowspan%>" align="right">
												<%=VTCQ.elementAt(i) %>
												<input type="hidden" name="tcq" id="tcq<%=i%>" value="<%=VTCQ.elementAt(i)%>">
											</td>
											<%} %>
											
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="alloc_qty<%=i%>" id="alloc_qty<%=i%>_<%=j%>" 
													value="" style="text-align:right;" disabled 
													onkeyup="checkNumber1(this,10,2);checkBalanceQty('<%=i%>'),totalAlloc('<%=i%>');" 
													onblur="checkNumber1(this,10,2);totalAlloc('<%=i%>');">
													
													<input type="hidden" class="form-control form-control-sm" name="margin<%=i%>" id="margin<%=i%>_<%=j%>" value="" style="text-align:right;" disabled readOnly>
													<input type="hidden" class="form-control form-control-sm" name="total_margin<%=i%>" id="total_margin<%=i%>_<%=j%>" value="" style="text-align:right;" disabled readOnly>
												</div>
											</td>
											
											<%if(j==0){ %>
											<td rowspan="<%=rowspan%>" align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="total_alloc_qty" id="total_alloc_qty<%=i%>" value="" style="text-align:right;" readOnly>
													<input type="hidden" class="form-control form-control-sm" name="avg_margin" id="avg_margin<%=i%>" value="" style="text-align:right;" disabled readOnly>
												</div>
											</td>
											<td rowspan="<%=rowspan%>" align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="rem_alloc_qty" id="rem_alloc_qty<%=i%>" value="" style="text-align:right;" readOnly>
												</div>
											</td>
											<%} %>
										</tr>
										<%} %>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="13" align="center">
										<%if(customer_cd.equals("0")){ %>
										<%=utilmsg.infoMessage("<b>Select Customer!</b>") %>
										<%}else{ %>
										<%=utilmsg.infoMessage("<b>No Contract is Pending for Allocation!</b>") %>
										<%} %>
										</td>
									</tr>
								<%} %>
								</tbody>
							</table>
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

<input type="hidden" name="option" value="MMBTU_ALLOCATION">
<input type="hidden" name="prev_clearance" value="<%=clearance%>">
<input type="hidden" name="selected_index" value="">

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

<input type="hidden" name="multiCountpty" value="<%=multiCountpty%>">
<input type="hidden" name="multiAgmtNo" value="<%=multiAgmtNo%>">
<input type="hidden" name="multiAgmtRev" value="<%=multiAgmtRev%>">
<input type="hidden" name="multiContNo" value="<%=multiContNo%>">
<input type="hidden" name="multiContRev" value="<%=multiContRev%>">
<input type="hidden" name="multiContTyp" value="<%=multiContTyp%>">
<input type="hidden" name="multiCargoNo" value="<%=multiCargoNo%>">

<input type="hidden" name="sel_cargo_size" value="<%=SEL_CARGO.length%>">
<input type="hidden" name="prev_index" value="">
<input type="hidden" name="exchgRate" value="<%=nf2.format(exchgRate)%>">

</form>
</body>
</html>