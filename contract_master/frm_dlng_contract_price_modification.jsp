<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh()
{
	var customer_cd = document.forms[0].customer_cd.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_contract_price_modification.jsp?customer_cd="+customer_cd+"&u="+u;;
	location.replace(url);
}

function setEnableDisable(obj,index,sub_index)
{
	if(obj.checked)
	{
		var new_sales_rate = document.getElementById("new_sales_rate"+index)
		var new_eff_dt = document.getElementById("new_eff_dt"+index)
		var submit = document.getElementById("submit"+index)
		var approve = document.getElementById("approve"+index)
		var reject = document.getElementById("reject"+index)
		
		var fgsa = document.getElementById("fgsa"+index)
		var fgsa_rev = document.getElementById("fgsa_rev"+index)
		var sn = document.getElementById("sn"+index)
		var sn_rev = document.getElementById("sn_rev"+index)
		var cont_type = document.getElementById("cont_type"+index)
		var segment = document.getElementById("segment"+index)
		var price_flag = document.getElementById("price_flag"+index)
		var modification_seq_no = document.getElementById("modification_seq_no"+index)
		
		var sales_rate = document.getElementById("sales_rate"+index)
		var rate_unit = document.getElementById("rate_unit"+index)
		var price_type = document.getElementById("price_type"+index)
		
		var approve_access = document.forms[0].approve_access.value;
		var write_access = document.forms[0].write_access.value;
		
		new_sales_rate.readOnly=false;
		new_sales_rate.disabled=false;
		
		new_eff_dt.readOnly=false;
		new_eff_dt.disabled=false;
		
		fgsa.disabled=false;
		fgsa_rev.disabled=false;
		sn.disabled=false;
		sn_rev.disabled=false;
		cont_type.disabled=false;
		segment.disabled=false;
		price_flag.disabled=false;
		modification_seq_no.disabled=false;
		
		sales_rate.disabled=false;
		rate_unit.disabled=false;
		
		for(var i=1; i <= parseInt(sub_index); i++)
		{
			var cargo_no = document.getElementById("cargo_no"+index+"_"+i)
			var cargo_seq = document.getElementById("cargo_seq"+index+"_"+i)
			var cost_price = document.getElementById("cost_price"+index+"_"+i)
			var cost_unit = document.getElementById("cost_unit"+index+"_"+i)
			var alloc_qty = document.getElementById("alloc_qty"+index+"_"+i)
			var margin = document.getElementById("margin"+index+"_"+i)
			var new_margin = document.getElementById("new_margin"+index+"_"+i)
			var new_total_margin = document.getElementById("new_total_margin"+index+"_"+i)
			var cargo_sn_rev = document.getElementById("cargo_sn_rev"+index+"_"+i)
			var total_margin = document.getElementById("total_margin"+index+"_"+i)
			
			cargo_no.disabled=false;
			cargo_seq.disabled=false;
			cost_price.disabled=false;
			cost_unit.disabled=false;
			alloc_qty.disabled=false;
			margin.disabled=false;
			new_margin.disabled=false;
			new_total_margin.disabled=false;
			cargo_sn_rev.disabled=false;
			total_margin.disabled=false;
		}
		
		if(write_access=="Y")
		{
			submit.disabled=false;
		}
		else
		{
			submit.disabled=true;
		}
		
		if(price_flag.value == "R")
		{
			if(approve_access == 'Y')
			{
				approve.disabled=false;
				reject.disabled=false;
			}
			else
			{
				approve.disabled=true;
				reject.disabled=true;
				
				alert("Approve/Reject Permission not available with current User!\n\n Please contact System Administrator!");
			}
		}
		
		var last_index = document.forms[0].last_index;
		var last_sub_index = document.forms[0].last_sub_index;
		
		if(last_index.value != "" && last_sub_index.value != "" && last_index.value != index)
		{
			var new_sales_rate1 = document.getElementById("new_sales_rate"+last_index.value)
			var new_eff_dt1 = document.getElementById("new_eff_dt"+last_index.value)
			var submit1 = document.getElementById("submit"+last_index.value)
			var approve1 = document.getElementById("approve"+last_index.value)
			var reject1 = document.getElementById("reject"+last_index.value)
			
			var fgsa1 = document.getElementById("fgsa"+last_index.value)
			var fgsa_rev1 = document.getElementById("fgsa_rev"+last_index.value)
			var sn1 = document.getElementById("sn"+last_index.value)
			var sn_rev1 = document.getElementById("sn_rev"+last_index.value)
			var cont_type1 = document.getElementById("cont_type"+last_index.value)
			var segment1 = document.getElementById("segment"+last_index.value)
			var price_flag1 = document.getElementById("price_flag"+last_index.value)
			var modification_seq_no1 = document.getElementById("modification_seq_no"+last_index.value)
			
			var sales_rate1 = document.getElementById("sales_rate"+last_index.value)
			var rate_unit1 = document.getElementById("rate_unit"+last_index.value)
			
			var price_type1 = document.getElementById("price_type"+last_index.value)
			
			new_sales_rate1.readOnly=true;
			new_sales_rate1.disabled=true;
			//new_sales_rate1.value="";
			
			new_eff_dt1.readOnly=true;
			new_eff_dt1.disabled=true;
			//new_eff_dt1.value="";
			
			fgsa1.disabled=true;
			fgsa_rev1.disabled=true;
			sn1.disabled=true;
			sn_rev1.disabled=true;
			cont_type1.disabled=true;
			segment1.disabled=true;
			price_flag1.disabled=true;
			modification_seq_no1.disabled=true;
			
			sales_rate1.disabled=true;
			rate_unit1.disabled=true;
			
			for(var i=1; i <= parseInt(last_sub_index.value); i++)
			{
				var cargo_no = document.getElementById("cargo_no"+last_index.value+"_"+i)
				var cargo_seq = document.getElementById("cargo_seq"+last_index.value+"_"+i)
				var cost_price = document.getElementById("cost_price"+last_index.value+"_"+i)
				var cost_unit = document.getElementById("cost_unit"+last_index.value+"_"+i)
				var alloc_qty = document.getElementById("alloc_qty"+last_index.value+"_"+i)
				var margin = document.getElementById("margin"+last_index.value+"_"+i)
				var new_margin = document.getElementById("new_margin"+last_index.value+"_"+i)
				var new_total_margin = document.getElementById("new_total_margin"+last_index.value+"_"+i)
				var cargo_sn_rev = document.getElementById("cargo_sn_rev"+last_index.value+"_"+i)
				var total_margin = document.getElementById("total_margin"+last_index.value+"_"+i)
				
				cargo_no.disabled=true;
				cargo_seq.disabled=true;
				cost_price.disabled=true;
				cost_unit.disabled=true;
				alloc_qty.disabled=true;
				margin.disabled=true;
				new_margin.disabled=true;
				new_total_margin.disabled=true;
				cargo_sn_rev.disabled=true;
				total_margin.disabled=true;
				
				//new_margin.value="";
				//new_total_margin.value="";
			}
			
			submit1.disabled=true;
			approve1.disabled=true;
			reject1.disabled=true;
		}
		
		last_index.value=index;
		last_sub_index.value=sub_index;
	}
}

function calculateMargin(index, sub_index)
{
	var new_sales_rate = document.getElementById("new_sales_rate"+index)
	var new_eff_dt = document.getElementById("new_eff_dt"+index)
	
	var rate_unit = document.getElementById("rate_unit"+index)
	var exchgRate = document.forms[0].exchgRate
	
	for(var i=1; i <= parseInt(sub_index); i++)
	{
		var cost_price = document.getElementById("cost_price"+index+"_"+i)
		var cost_unit = document.getElementById("cost_unit"+index+"_"+i)
		var alloc_qty = document.getElementById("alloc_qty"+index+"_"+i)
		var new_margin = document.getElementById("new_margin"+index+"_"+i)
		var new_total_margin = document.getElementById("new_total_margin"+index+"_"+i)
		
		var margin = parseFloat("0.0000");
		if(new_sales_rate.value != "" && new_sales_rate.value != null)
		{	/*
			if(rate_unit.value == "1")
			{
				if(parseFloat(exchgRate.value) > 0)
				{
					margin = (parseFloat(new_sales_rate.value)/parseFloat(exchgRate.value)) - parseFloat(cost_price.value) 
					new_margin.value=round(margin,4);
				}
				else
				{
					margin=parseFloat("0.0000");
					new_margin.value=round(margin,4);
				}
			}
			else
			{
				margin = parseFloat(new_sales_rate.value) - parseFloat(cost_price.value) 
				new_margin.value=round(margin,4);
			}
			*/
			var SalesPrice = parseFloat("0.0000");
	   		var CostPrice = parseFloat("0.0000");
	   		
	   		if(rate_unit.value=="1")
	   		{
	   			if(exchgRate.value!=null && exchgRate.value!="" && parseFloat(exchgRate.value)!='0')
	   			{
	   				SalesPrice = (parseFloat(new_sales_rate.value)/parseFloat(exchgRate.value))
	   			}
	   			else
	   			{
	   				SalesPrice = parseFloat("0.0000");
	   			}
	   		}
	   		else
	   		{
	   			SalesPrice = parseFloat(new_sales_rate.value)
	   		}
	   		
	   		if(cost_unit.value=="1")
	   		{
	   			if(exchgRate.value!=null && exchgRate.value!="" && parseFloat(exchgRate.value)!='0')
	   			{
	   				CostPrice = (parseFloat(cost_price.value)/parseFloat(exchgRate.value))
	   			}
	   			else
	   			{
	   				CostPrice = parseFloat("0.0000");
	   			}
	   		}
	   		else
	   		{
	   			CostPrice = parseFloat(cost_price.value)
	   		}
	   		margin = parseFloat(SalesPrice) - parseFloat(CostPrice) 
			new_margin.value=round(margin,4);
	   		
			new_total_margin.value=round((parseFloat(alloc_qty.value) * parseFloat(margin)),2);
		}
		else
		{
			new_margin.value=""
			new_total_margin.value=""
		}
	}
}

function doSubmit(index,btn,sub_index,cargo_alloc_index)
{
	var new_sales_rate = document.getElementById("new_sales_rate"+index)
	var new_eff_dt = document.getElementById("new_eff_dt"+index)
	var submit = document.getElementById("submit"+index)
	var approve = document.getElementById("approve"+index)
	var reject = document.getElementById("reject"+index)
	
	var msg="";
	var flag=true;
	
	//var val_dt = compareDate(cont_end_dt.value,EffDt.value);
	
	//var val_dt = compareDate(EffDt.value,last_eff_dt.value);
	
	if(new_sales_rate.value == "0" || new_sales_rate.value == "0.00" || new_sales_rate.value == "0.000" || new_sales_rate.value == "0.0000" || new_sales_rate.value == "" || new_sales_rate.value == " ")
	{
		msg+="Please Enter the New Sales Rate!!";
		flag=false;
	}
	if(new_eff_dt.value == "" || new_eff_dt.value == " ")
	{
		msg+="\nPlease Select the New Effective Date!!";
		flag=false;
	}
	/* if(val_dt == "2" || val_dt == "0")
	{
		msg="\nThe Effective Date should be grater than Last Effective Date!!";
		flag=false
	}	 */
	
	/* if(reject.value="Reject")
	{
		let text;
		  let person = prompt("Please enter your name:", "Harry Potter");
		  if (person == null || person == "") {
		    text = "User cancelled the prompt.";
		  } else {
		    text = "Hello " + person + "! How are you today?";
		  }
		  //document.getElementById("demo").innerHTML = text;
	} */
	
	var count_margin=parseInt("0");
	var count_total_margin=parseInt("0");
	for(var i=1; i <= parseInt(sub_index); i++)
	{
		var cargo_no = document.getElementById("cargo_no"+index+"_"+i)
		var cargo_seq = document.getElementById("cargo_seq"+index+"_"+i)
		var cost_price = document.getElementById("cost_price"+index+"_"+i)
		var alloc_qty = document.getElementById("alloc_qty"+index+"_"+i)
		var margin = document.getElementById("margin"+index+"_"+i)
		var new_margin = document.getElementById("new_margin"+index+"_"+i)
		var new_total_margin = document.getElementById("new_total_margin"+index+"_"+i)
		var cargo_sn_rev = document.getElementById("cargo_sn_rev"+index+"_"+i)
		var total_margin = document.getElementById("total_margin"+index+"_"+i)
		
		if(new_margin.value=="NaN" || new_margin.value=="")
		{
			count_margin = parseInt(count_margin)+1;
		}
		
		if(new_total_margin.value=="NaN" || new_total_margin.value=="")
		{
			count_total_margin = parseInt(count_total_margin)+1;
		}
	}
	
	if(parseInt(count_margin) > 0)
	{
		msg+="\nNew Margin cann't be Blank!!";
		flag=false;
	}
	
	if(parseInt(count_total_margin) > 0)
	{
		msg+="\nNew Total Margin cann't be Blank!!";
		flag=false;
	}
	
	if(btn=="approve" || btn=="reject")
	{
		if(parseInt(sub_index) != parseInt(cargo_alloc_index))
		{
			msg+="\nPlease re-submit the Request and then proceed for approval";
			flag=false;
		}
	}
	var oldvalue="";
	if(flag)
	{
		var con_msg="";
		if(btn=="approve")
		{
			/* con_msg="Please check following data :\n\n"
			con_msg+="Cargo Ref cd : "+cargo_ref_cd.value+"\n"
			con_msg+="Cargo Prov Price : "+price.value+"\n";
			con_msg+="Cargo Requested Price : "+hid_price_to.value+"\n";
			con_msg+="Eff Date : "+hid_EffDt.value+"\n\n"; */
			con_msg+="Do you want to Approve??"
		}
		else if(btn=="reject")
		{
			/* con_msg="Please check following data :\n\n"
			con_msg+="Cargo Ref cd : "+cargo_ref_cd.value+"\n"
			con_msg+="Cargo Prov Price : "+price.value+"\n";
			con_msg+="Cargo Requested Price : "+hid_price_to.value+"\n";
			con_msg+="Eff Date : "+hid_EffDt.value+"\n\n"; */
			con_msg+="Do you want to Reject??"
		}
		else
		{
			con_msg= "Do you want to change Sales rate??";
		}
		var a = confirm(con_msg);
		if(a)
		{
			if(btn=="approve")
			{
				document.forms[0].option.value="Contract_Price_Modification_Approve";
			}
			else if(btn=="reject")
			{
				document.forms[0].option.value="Contract_Price_Modification_Approve";
			}
			/* if(hid_EffDt.value != "")
			{
				oldvalue="cargo="+cargo_ref_cd.value+"#Trader="+trd_cd.value+"#ProvPrice="+price.value+"#RequestedPrice="+hid_price_to.value+"#EffDt="+hid_EffDt.value+"#status=Requested";
			} */
			//document.forms[0].old_value.value=oldvalue;
			document.forms[0].action.value=btn;
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

var PriceInfoWindow;
function openPriceInformation(index,sub_index,ContType, buyer_cd,fgsa_cd,fgsa_revno,sn_cd,sn_revno,start_dt,end_dt,buyer_name)
{
	var e = document.getElementById("customer_cd");
	buyer_name = e.options[e.selectedIndex].text;
	if(start_dt != "" & end_dt != "") 
	{
		if(!PriceInfoWindow || PriceInfoWindow.closed)
		{
			PriceInfoWindow = window.open("rpt_price_change_variable.jsp?index="+index+"&sub_index="+sub_index+"&buyer_cd="+buyer_cd+"&cont_type="+ContType+"&fgsa_cd="+fgsa_cd+"&fgsa_revno="+fgsa_revno+"&sn_cd="+sn_cd+"&sn_revno="+sn_revno+"&start_dt="+start_dt+"&end_dt="+end_dt+"&buyer_name="+buyer_name,"VariablePrice","top=10,left=10,width=800,height=200,scrollbars=1");
		}
		else
		{
			PriceInfoWindow.close();
			PriceInfoWindow = window.open("rpt_price_change_variable.jsp?index="+index+"&sub_index="+sub_index+"&buyer_cd="+buyer_cd+"&cont_type="+ContType+"&fgsa_cd="+fgsa_cd+"&fgsa_revno="+fgsa_revno+"&sn_cd="+sn_cd+"&sn_revno="+sn_revno+"&start_dt="+start_dt+"&end_dt="+end_dt+"&buyer_name="+buyer_name,"VariablePrice","top=10,left=10,width=800,height=200,scrollbars=1");
		}
	}
	else
	{
		alert("Contract Start - End Date are missing!!");
	}
}

function refershParice(price,index,sub_index)
{
	document.getElementById("new_sales_rate"+index).value=price;
	document.getElementById("new_eff_dt"+index).value="";
	calculateMargin(index, sub_index)
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_master.DB_Contract_Price_Modification" id="contract" scope="page"/>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String contract_type = request.getParameter("contract_type")==null?"0":request.getParameter("contract_type");
String customer_cd = request.getParameter("customer_cd")==null?"0":request.getParameter("customer_cd");
String formId = request.getParameter("formId")==null?"0":request.getParameter("formId");
String formName = request.getParameter("FormName")==null?"0":request.getParameter("FormName");

contract.setCallFlag("DLNGPriceModification");
contract.setCustomer_cd(customer_cd);
contract.setComp_cd(owner_cd);
contract.init();

Vector VCUSTOMR_CD_MST = contract.getVCUSTOMR_CD_MST();
Vector VCUSTOMR_NM_MST = contract.getVCUSTOMR_NM_MST();

Vector VCONTRACT_DETAIL = contract.getVCONTRACT_DETAIL();
Vector VFGSA = contract.getVFGSA();
Vector VFGSA_REV = contract.getVFGSA_REV();
Vector VSN = contract.getVSN();
Vector VSN_REV = contract.getVSN_REV();
Vector VCONTRACT_TYPE = contract.getVCONTRACT_TYPE();
Vector VSEGMENT = contract.getVSEGMENT();
Vector VCONT_ST_DT = contract.getVCONT_ST_DT();
Vector VCONT_END_DT = contract.getVCONT_END_DT();
Vector VSALES_RATE = contract.getVSALES_RATE();
Vector VRATE_UNIT = contract.getVRATE_UNIT();

Vector VINDEX = contract.getVINDEX();

Vector VCARGO_NO = contract.getVCARGO_NO();
Vector VCARGO_SEQ = contract.getVCARGO_SEQ();
Vector VCOST_PRICE = contract.getVCOST_PRICE();
Vector VALLOC_QTY = contract.getVALLOC_QTY();
Vector VMARGIN = contract.getVMARGIN();

Vector VNEW_SALES_RATE = contract.getVNEW_SALES_RATE();
Vector VNEW_MARGIN = contract.getVNEW_MARGIN();
Vector VNEW_TOTAL_MARGIN = contract.getVNEW_TOTAL_MARGIN();

Vector VMODIFICATION_SEQ_NO = contract.getVMODIFICATION_SEQ_NO();
Vector VPRICE_EFF_DT = contract.getVPRICE_EFF_DT();
Vector VPRICE_FLAG = contract.getVPRICE_FLAG();

Vector VCARGO_SN_REV = contract.getVCARGO_SN_REV();
Vector VTOTAL_MARGIN = contract.getVTOTAL_MARGIN();

Vector VPRICE_CHANGE_HISTORY = contract.getVPRICE_CHANGE_HISTORY();
Vector VPRICE_TYPE = contract.getVPRICE_TYPE();
Vector VLAST_EFF_DT = contract.getVLAST_EFF_DT();
Vector VCARGO_ALLOC_INDEX = contract.getVCARGO_ALLOC_INDEX();

Vector VCP_UNIT = contract.getVCP_UNIT();
Vector VCP_UNIT_NM = contract.getVCP_UNIT_NM();

Vector VPUR_MAP_ID = contract.getVPUR_MAP_ID();

double exchgRate = contract.getExchgRate();

NumberFormat nf = new DecimalFormat("###########0.00");
NumberFormat nf2 = new DecimalFormat("###########0.0000");

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Contract_Price_Modification">


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
					    	Contract (DLNG) Price Modification
					    </div>
	    			</div>
	    		</div>
	    		<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer<span class="s-red">*</span></b></label>
								</div>
				    			<div class="col">
				    				<select class="form-select form-select-sm" name='customer_cd' id="customer_cd" onchange="refresh();"> 
					   					<option value="0">--Select--</option>
					   					<%for(int i=0;i<VCUSTOMR_CD_MST.size();i++) { %>
					   						<option value="<%=VCUSTOMR_CD_MST.elementAt(i)%>"><%=VCUSTOMR_NM_MST.elementAt(i)%></option>
									  	<%}%> 
					   				</select>
					   				<script type="text/javascript">
					   					document.forms[0].customer_cd.value = '<%=customer_cd%>';
					   				</script>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4"></div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered">
									<thead>
										<tr>
								   			<th>SR.NO.</th>
								   			<th>Contract <br> Contract/Trade Ref#</th>
								   			<th>Start - End Date</th>
								   			<th>Purchase<br>Contract#</th>
								   			<th>Purchase Price</th>
								   			<th>Allocated MMBTU</th>
								   			<th>Sales Rate</th>
								   			<th>Margin<br>($/MMBTU)</th>
								   			<th>Eff Date</th>
								   			<th>New Sales Rate</th>
								   			<th>New Margin<br>($/MMBTU)</th>
								   			<th>Total Margin<br>($/MMBTU)</th>
								   			<th>New Eff Date</th>
								   			<th>Request</th>
								   			<th colspan="2">Approval</th>
								   			<th>History</th>
								   		</tr>
									</thead>
									<tbody>
									<% int j=0;String rowcolor="";
									if(VCONTRACT_DETAIL.size()>0){
									
							   		for(int i=0; i<VCONTRACT_DETAIL.size(); i++)
							   		{ 
							   			if(i%2==0) {
											rowcolor="#E0EEE0";
										}else{ 
											rowcolor="";
										}
							   			if(Integer.parseInt(""+VINDEX.elementAt(i)) > 0)
										{
							   				int n=0;
							   				for(j=j; j<VCARGO_NO.size(); j++)
											{
												n+=1;
												if(n==1)
												{%>
										   		<tr id="tr<%=i%>">
										   			<td rowspan="<%=VINDEX.elementAt(i)%>">
										   				<input type="radio" name="radio" onclick="setEnableDisable(this,'<%=i%>','<%=VINDEX.elementAt(i)%>');">
										   				<input type="hidden" name="index" value="<%=VINDEX.elementAt(i)%>">
										   				<input type="hidden" name="cargo_alloc_index" value="<%=VCARGO_ALLOC_INDEX.elementAt(i)%>">
										   				
										   				<%=i+1%>
										   			</td>
										   			<td align="center" rowspan="<%=VINDEX.elementAt(i)%>">
										   				<%if(VSEGMENT.elementAt(i).equals("DLNG")){ %>
										   				<font color="#990000"><%=VCONTRACT_DETAIL.elementAt(i) %></font>
										   				<%}else if(VSEGMENT.elementAt(i).equals("IGX")){%>
										   				<font color="#990099"><%=VCONTRACT_DETAIL.elementAt(i) %></font>
										   				<%}else{ %>
										   				<%=VCONTRACT_DETAIL.elementAt(i) %>
										   				<%} %>
										   				<input type="hidden" name="fgsa" id="fgsa<%=i%>" value="<%=VFGSA.elementAt(i)%>" disabled>
										   				<input type="hidden" name="fgsa_rev" id="fgsa_rev<%=i%>" value="<%=VFGSA_REV.elementAt(i)%>" disabled>
										   				<input type="hidden" name="sn" id="sn<%=i%>" value="<%=VSN.elementAt(i)%>" disabled>
										   				<input type="hidden" name="sn_rev" id="sn_rev<%=i%>" value="<%=VSN_REV.elementAt(i)%>" disabled>
										   				<input type="hidden" name="cont_type" id="cont_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled>
										   				<input type="hidden" name="segment" id="segment<%=i%>" value="<%=VSEGMENT.elementAt(i)%>" disabled>
										   				<input type="hidden" name="price_flag" id="price_flag<%=i%>" value="<%=VPRICE_FLAG.elementAt(i)%>" disabled>
										   				<input type="hidden" name="modification_seq_no" id="modification_seq_no<%=i%>" value="<%=VMODIFICATION_SEQ_NO.elementAt(i)%>" disabled>
										   			</td>
										   			<td align="center" rowspan="<%=VINDEX.elementAt(i)%>"><%=VCONT_ST_DT.elementAt(i)%> - <%=VCONT_END_DT.elementAt(i) %></td>
										   			<td align="center">
										   				<%=VPUR_MAP_ID.elementAt(j) %>
										   				<input type="hidden" name="cargo_no" id="cargo_no<%=i%>_<%=n%>" value="<%=VCARGO_NO.elementAt(j) %>" size=10  readOnly disabled>
										   				<input type="hidden" name="cargo_seq" id="cargo_seq<%=i%>_<%=n%>" value="<%=VCARGO_SEQ.elementAt(j) %>" size=10  readOnly disabled>
										   				<input type="hidden" name="cargo_sn_rev" id="cargo_sn_rev<%=i%>_<%=n%>" value="<%=VCARGO_SN_REV.elementAt(j) %>" size=10  readOnly disabled>
										   			</td>
										   			<td align="right">
										   				<%=VCOST_PRICE.elementAt(j) %>&nbsp;<%=VCP_UNIT_NM.elementAt(j)%>&nbsp;
										   				<input type="hidden" name="cost_price" id="cost_price<%=i%>_<%=n%>" value="<%=VCOST_PRICE.elementAt(j) %>" size=10  readOnly disabled>
										   				<input type="hidden" name="cost_unit" id="cost_unit<%=i%>_<%=n%>" value="<%=VCP_UNIT.elementAt(j) %>" size=10  readOnly disabled>
										   			</td>
										   			<td align="right">
										   				<%=VALLOC_QTY.elementAt(j) %>&nbsp;
										   				<input type="hidden" name="alloc_qty" id="alloc_qty<%=i%>_<%=n%>" value="<%=VALLOC_QTY.elementAt(j) %>" size=10  readOnly disabled>
										   			</td>
										   			<td rowspan="<%=VINDEX.elementAt(i)%>" align="right">
										   				<%=VSALES_RATE.elementAt(i)%>&nbsp;<%if(VRATE_UNIT.elementAt(i).toString().trim().equals("1")){ %>(INR/MMBTU)<%}else{ %>($/MMBTU)<%} %>
										   				<input type="hidden" name="sales_rate" id="sales_rate<%=i%>" value="<%=VSALES_RATE.elementAt(i) %>" size=10  readOnly disabled>
										   				<input type="hidden" name="rate_unit" id="rate_unit<%=i%>" value="<%=VRATE_UNIT.elementAt(i).toString().trim() %>" size=10  readOnly disabled>
										   			</td>
										   			<td align="right">
										   				<%=VMARGIN.elementAt(j) %>&nbsp;
										   				<input type="hidden" name="margin" id="margin<%=i%>_<%=n%>" value="<%=VMARGIN.elementAt(j) %>" size=10  readOnly disabled>
										   				<input type="hidden" name="total_margin" id="total_margin<%=i%>_<%=n%>" value="<%=VTOTAL_MARGIN.elementAt(j) %>" size=10  readOnly disabled>
										   			</td>
										   			<td rowspan="<%=VINDEX.elementAt(i)%>"><%=VLAST_EFF_DT.elementAt(i) %></td>
										   			<td rowspan="<%=VINDEX.elementAt(i)%>">
										   				<input type="text" class="form-control form-control-sm" name="new_sales_rate" id="new_sales_rate<%=i%>" value="<%=VNEW_SALES_RATE.elementAt(j)%>" size=10 style="text-align:right" onBlur="<%if(VRATE_UNIT.elementAt(i).toString().trim().equals("1")){ %>checkNumber1(this,8,4);<%}else{ %>checkNumber1(this,6,4);<%} %>negNumber(this);" onKeyUp="calculateMargin('<%=i%>','<%=VINDEX.elementAt(i)%>')" readOnly disabled > 
										   				&nbsp;<%if(VRATE_UNIT.elementAt(i).toString().trim().equals("1")){ %>(INR/MMBTU)<%}else{ %>($/MMBTU)<%} %>
										   				<input type="hidden" name="price_type" id="price_type<%=i%>" value="<%=VPRICE_TYPE.elementAt(i)%>"  size=10 style="text-align:right" readOnly disabled > 
										   			</td>
										   			<td>
										   				<input type="text" class="form-control form-control-sm" name="new_margin" id="new_margin<%=i%>_<%=n%>" value="<%=VNEW_MARGIN.elementAt(j)%>" size=10 style="text-align:right"  readOnly disabled>
										   			</td>
										   			<td>
										   				<input type="text" class="form-control form-control-sm" name="new_total_margin" id="new_total_margin<%=i%>_<%=n%>" value="<%=VNEW_TOTAL_MARGIN.elementAt(j)%>" size=14 style="text-align:right"  readOnly disabled> 
										   			</td>
										   			<td rowspan="<%=VINDEX.elementAt(i)%>">
										   				<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="new_eff_dt" id="new_eff_dt<%=i%>" value="<%=VPRICE_EFF_DT.elementAt(i) %>" maxLength="10" 
								      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" readOnly disabled>
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      						</div>
										   			</td>
										   			<td rowspan="<%=VINDEX.elementAt(i)%>">
										   				<input type="button" class="btn btn-sm request_btn" name="btn_submit" id="submit<%=i%>" value="Request" disabled onclick="doSubmit('<%=i%>','request','<%=VINDEX.elementAt(i)%>','<%=VCARGO_ALLOC_INDEX.elementAt(i)%>');">
										   			</td>
										   			<td rowspan="<%=VINDEX.elementAt(i)%>">
										   				<input type="button" class="btn btn-sm config_btn" name="approve" id="approve<%=i%>" value="Approve" disabled onclick="doSubmit('<%=i%>','approve','<%=VINDEX.elementAt(i)%>','<%=VCARGO_ALLOC_INDEX.elementAt(i)%>');">
										   			</td>
										   			<td rowspan="<%=VINDEX.elementAt(i)%>">	
										   				<input type="button" class="btn btn-danger btn-sm" style="border-radius: 50px;font-weight: bold;" name="reject" id="reject<%=i%>" value="Reject" disabled onclick="doSubmit('<%=i%>','reject','<%=VINDEX.elementAt(i)%>','<%=VCARGO_ALLOC_INDEX.elementAt(i)%>');">
										   			</td>
										   			<td rowspan="<%=VINDEX.elementAt(i)%>">
										   				<%=VPRICE_CHANGE_HISTORY.elementAt(i) %>	
										   			</td>
										   		</tr>
										   		<%}else{ %>
										   		<tr id="sub_tr<%=i%>_<%=n%>">
										   			<td align="center">
										   				<%=VPUR_MAP_ID.elementAt(j) %>
										   				<input type="hidden" name="cargo_no" id="cargo_no<%=i%>_<%=n%>" value="<%=VCARGO_NO.elementAt(j) %>" size=10  readOnly disabled>
										   				<input type="hidden" name="cargo_seq" id="cargo_seq<%=i%>_<%=n%>" value="<%=VCARGO_SEQ.elementAt(j) %>" size=10  readOnly disabled>
										   				<input type="hidden" name="cargo_sn_rev" id="cargo_sn_rev<%=i%>_<%=n%>" value="<%=VCARGO_SN_REV.elementAt(j) %>" size=10  readOnly disabled>
										   			</td>
										   			<td align="right">
										   				<%=VCOST_PRICE.elementAt(j) %>&nbsp;<%=VCP_UNIT_NM.elementAt(j)%>&nbsp;
										   				<input type="hidden" name="cost_price" id="cost_price<%=i%>_<%=n%>" value="<%=VCOST_PRICE.elementAt(j) %>" size=10  readOnly disabled>
										   				<input type="hidden" name="cost_unit" id="cost_unit<%=i%>_<%=n%>" value="<%=VCP_UNIT.elementAt(j) %>" size=10  readOnly disabled>
										   			</td>
										   			<td align="right">
										   				<%=VALLOC_QTY.elementAt(j) %>&nbsp;
										   				<input type="hidden" name="alloc_qty" id="alloc_qty<%=i%>_<%=n%>" value="<%=VALLOC_QTY.elementAt(j) %>" size=10 readOnly disabled>
										   			</td>
										   			<td align="right">
										   				<%=VMARGIN.elementAt(j) %>&nbsp;
										   				<input type="hidden" name="margin" id="margin<%=i%>_<%=n%>" value="<%=VMARGIN.elementAt(j) %>" size=10  readOnly disabled>
										   				<input type="hidden" name="total_margin" id="total_margin<%=i%>_<%=n%>" value="<%=VTOTAL_MARGIN.elementAt(j) %>" size=10  readOnly disabled>
										   			</td>
										   			<td>
										   				<input type="text" class="form-control form-control-sm" name="new_margin" id="new_margin<%=i%>_<%=n%>" value="<%=VNEW_MARGIN.elementAt(j)%>" size=10 style="text-align:right" readOnly disabled>
										   			</td>
										   			<td>
										   				<input type="text" class="form-control form-control-sm" name="new_total_margin" id="new_total_margin<%=i%>_<%=n%>" value="<%=VNEW_TOTAL_MARGIN.elementAt(j)%>" size=14 style="text-align:right" readOnly disabled>
										   			</td>
										   		</tr>
										   		<%} 
										   		if(Integer.parseInt(""+VINDEX.elementAt(i)) == n)
												{
													j=j+1;
													break;
												}%>
									   		<%} 
									   	}%>
							   		<%} %>
							   		<%}else{ %>
							   		<tr>
							   			<td colspan="17"><div align="center"><%=utilmsg.infoMessage("<b>No Contract Price Change Request Generated!</b>")%></div></td>
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
	   		
<input type="hidden" name="last_index">
<input type="hidden" name="last_sub_index">

<input type="hidden" name="deal_size" value="<%=VFGSA.size()%>">
<input type="hidden" name="last_rowcolor" value="">
<input type="hidden" name="rowcolor_size" value="">

<input type='hidden' name='option' value="Contract_Price_Modifiction">
<input type="hidden" name="action" value="">

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
<input type="hidden" name="exchgRate" value="<%=exchgRate%>">

</form>
</body>
</html>