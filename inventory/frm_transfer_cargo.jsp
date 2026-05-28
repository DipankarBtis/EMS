
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function goBack()
{
	var u = document.forms[0].u.value;
	var Countpty = document.forms[0].Countpty.value;
	var AgmtNo = document.forms[0].AgmtNo.value;
	var AgmtRev = document.forms[0].AgmtRev.value;
	var ContNo = document.forms[0].ContNo.value;
	var ContRev = document.forms[0].ContRev.value;
	var ContTyp = document.forms[0].ContTyp.value;
	var CargoNo = document.forms[0].CargoNo.value;
	var pool_flag = document.forms[0].pool_flag.value;
	
	var a = confirm("Are You sure You want to go Back?")
	
	if(a)
	{
		var url = "frm_allocation_transfer.jsp?Countpty="+Countpty+"&AgmtNo="+AgmtNo+"&AgmtRev="+AgmtRev+
		"&ContNo="+ContNo+"&ContRev="+ContRev+"&ContTyp="+ContTyp+"&CargoNo="+CargoNo+
		"&u="+u+"&pool_flag="+pool_flag;
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}

function Enable(index)
{
	var chk = document.getElementById("chk"+index);
	var sel_cargo_size = document.forms[0].sel_cargo_size;
	var transfer_alloc_qty = document.forms[0].transfer_alloc_qty;
	var total_transfer_alloc_qty = document.getElementById("total_transfer_alloc_qty"+index);
	var total_rec_qty = document.getElementById("total_rec_qty"+index);
	var transfer_type = document.forms[0].transfer_type;
	var total_transfer = document.getElementById("total_transfer");
	var total_rec_transfer = document.getElementById("total_rec_transfer");
	var bal_qty = document.forms[0].bal_qty;
	var pur_alloc_qty = document.forms[0].pur_alloc_qty;
	var remaining_qty=document.forms[0].remaining_qty;
	var len = $("[name='chk']:checked").length;
	var diff = "";
	
	if(transfer_type.value == "short_transfer")
	{
		diff = parseFloat(bal_qty.value).toFixed(2);
	}
	else if(transfer_type.value == "full_replacement")
	{
		diff = parseFloat(pur_alloc_qty.value).toFixed(2);
	}

	if(chk.checked)
	{
		for(var i=0;i<parseInt(sel_cargo_size.value);i++)
		{
			document.getElementById("transfer_alloc_qty"+index+"_"+i).disabled = false;
		}
		/*if(transfer_type.value != "")
		{
			if(len == 1 && total_transfer.value=="")
			{
				remaining_qty.value = diff;
			}
		}*/
	}
	else
	{
		var sum = parseFloat("0");
		for(var i=0;i<parseInt(sel_cargo_size.value);i++)
		{
			if(document.getElementById("transfer_alloc_qty"+index+"_"+i).value != "")
			{
				sum+=parseFloat(document.getElementById("transfer_alloc_qty"+index+"_"+i).value);
			}
			document.getElementById("transfer_alloc_qty"+index+"_"+i).disabled = true;
			document.getElementById("transfer_alloc_qty"+index+"_"+i).value = "";
		}
		
		total_transfer_alloc_qty.value="";
		total_rec_qty.value="";
		if(len == 0)
		{
			total_transfer.value="";
			total_rec_transfer.value="";
		}
		else
		{
			if(isNaN(parseFloat(total_transfer.value)))	//For handling the Null Total quantities 
			{
				total_transfer.value="";
				total_rec_transfer.value="";
			}
			else
			{
				total_transfer.value=(parseFloat(total_transfer.value)-sum).toFixed(2);
				total_rec_transfer.value=(parseFloat(total_transfer.value)*-1).toFixed(2);
			}
		}
		
		if(transfer_type.value != "")
		{
			/*if(len == 0)
			{
				remaining_qty.value="";
			}*/
			/*else
			{*/
				if(isNaN(parseFloat(remaining_qty.value)))	//For handling the Null remaining quantities 
				{
					if(len == 1 && total_transfer_alloc_qty.value=="")
					{
						remaining_qty.value = diff.toFixed(2);
					}
					else
					{
						remaining_qty.value="";
					}
				}
				else
				{
					if(transfer_type.value == "short_transfer")
					{
						if(len == 1 && total_transfer.value=="")
						{
							remaining_qty.value = diff.toFixed(2);
						}
						else
						{
							remaining_qty.value = (parseFloat(remaining_qty.value)-sum).toFixed(2);
// 							remaining_qty.value = parseFloat(bal_qty.value)+sum;
						}
					}
					else if(transfer_type.value == "full_replacement")
					{
						if(len == 1 && total_transfer.value=="")
						{
							remaining_qty.value = diff.toFixed(2);
						}
						else
						{
							remaining_qty.value = (pur_alloc_qty.value-total_transfer.value).toFixed(2);
							//remaining_qty.value = (parseFloat(pur_alloc_qty.value)-parseFloat(total_transfer.value)).toFixed(2);
						}
					}
				}
			//}
		}
	}
}

function checkBalanceQty(index)
{
	var sel_cargo_size = document.forms[0].sel_cargo_size;
	var total_transfer_alloc_qty = document.getElementById("total_transfer_alloc_qty"+index);
	var transferable_qty = document.getElementById("transferable_qty"+index);
	var total_rec_qty = document.getElementById("total_rec_qty"+index);
	var transferable_qty = document.getElementById("transferable_qty"+index);
	var total_transfer = document.getElementById("total_transfer");
	var total_rec_transfer = document.getElementById("total_rec_transfer");
	
	for(var i=0; i<parseInt(sel_cargo_size.value); i++)
	{
		var transfer_alloc_qty = document.getElementById("transfer_alloc_qty"+index+"_"+i);
		var avail_qty = document.getElementById("avail_qty"+index+"_"+i);
		var pur_cont_disp_no = document.getElementById("pur_cont_disp_no"+index+"_"+i);
		
		if(transfer_alloc_qty != null || transfer_alloc_qty !="")
		{
			if(parseFloat(transfer_alloc_qty.value) > parseFloat(avail_qty.value))
			{
				alert("Allocated MMBTU should be <= Balance MMBTU for ROW - "+(parseInt(index)+1)+" (Cont#"+pur_cont_disp_no.value+")");	
				transfer_alloc_qty.value="";
				return false;
			}
		}
	}
}

function totalAlloc(index)
{
	var sum = parseFloat("0");
	var sel_cargo_size = document.forms[0].sel_cargo_size;
	var total_transfer_alloc_qty = document.getElementById("total_transfer_alloc_qty"+index);
	var total_rec_qty = document.getElementById("total_rec_qty"+index);
	var transferable_qty = document.getElementById("transferable_qty"+index);
	var alloc_qty = document.getElementById("alloc_qty"+index);
	var total_transfer = document.getElementById("total_transfer");
	var total_rec_transfer = document.getElementById("total_rec_transfer");
	var transfer_type = document.forms[0].transfer_type;
	var bal_qty = document.forms[0].bal_qty;
	var msg = "";
	
	for(var i=0; i<parseInt(sel_cargo_size.value); i++)
	{
		var transfer_alloc_qty = document.getElementById("transfer_alloc_qty"+index+"_"+i);
		var avail_qty = document.getElementById("avail_qty"+index+"_"+i);
		var pur_cont_disp_no = document.getElementById("pur_cont_disp_no"+index+"_"+i);
		
		if(transfer_alloc_qty.value !="")
		{
			sum=(parseFloat(sum)+parseFloat(transfer_alloc_qty.value)).toFixed(2);
		}
		else
		{
			sum=parseFloat(sum)+0;
		}
	
		total_transfer_alloc_qty.value = (parseFloat(sum)).toFixed(2);
		total_rec_qty.value = (parseFloat(sum)*(-1)).toFixed(2);
	}
}

function checkZero(index)
{
	var sel_cargo_size = document.forms[0].sel_cargo_size;
	
	for(var i=0; i<parseInt(sel_cargo_size.value); i++)
	{
		var transfer_alloc_qty = document.getElementById("transfer_alloc_qty"+index+"_"+i);
		var pur_cont_disp_no = document.getElementById("pur_cont_disp_no"+index+"_"+i);
		
		if(parseFloat(transfer_alloc_qty.value)==0 || parseFloat(transfer_alloc_qty.value)<0)
		{
			alert("Transfer Allocated Quantity must be greater than ZERO(0) for the destination cargo "+pur_cont_disp_no.value);
			transfer_alloc_qty.value="";
			return false;
		}
	}
}

function calculateTotalAlloc(obj)
{
	var contract_size = document.forms[0].contract_size.value;
	var total_transfer = document.getElementById("total_transfer");
	var total_rec_transfer = document.getElementById("total_rec_transfer");
	var sum = parseFloat("0");
	var transfer_type = document.forms[0].transfer_type;
	var bal_qty = document.forms[0].bal_qty;
	var chk = document.getElementsByName("chk");
	var sel_cargo_size = document.forms[0].sel_cargo_size;
	var pur_alloc_qty = document.forms[0].pur_alloc_qty;
	var diff = "";
	var remaining_qty=document.forms[0].remaining_qty;
	var transfer_type = document.forms[0].transfer_type;
	var msg="";
	
	if(transfer_type.value == "short_transfer")
	{
		diff = parseFloat(bal_qty.value);
	}
	else if(transfer_type.value == "full_replacement")
	{
		diff = parseFloat(pur_alloc_qty.value);
	}
	else
	{
		diff = parseFloat("0");
	}
	
	for(var i=0;i<parseInt(contract_size);i++)
	{
		var total_transfer_alloc_qty = document.getElementById("total_transfer_alloc_qty"+i);
		var total_rec_qty = document.getElementById("total_rec_qty"+i);
		var alloc_qty = document.getElementById("alloc_qty"+i);
		var transferable_qty = document.getElementById("transferable_qty"+i);
		var transfer = parseFloat("0");
		
		if(transfer_type.value != "")
		{
			transfer = parseFloat(alloc_qty.value)
		}
		else
		{
			transfer = parseFloat(alloc_qty.value)>parseFloat(transferable_qty.value)?parseFloat(transferable_qty.value):parseFloat(alloc_qty.value);
		}
		
		if(parseFloat(obj.value)<0)
		{
			msg="Transfer Allocated Quantity must be greater than ZERO(0)!";
			alert(msg);
			total_transfer_alloc_qty.value-= parseFloat(obj.value).toFixed(2);
			total_rec_qty.value=(parseFloat(total_transfer_alloc_qty.value)*(-1)).toFixed(2);
			obj.value="";
		}
		if(total_transfer_alloc_qty.value!="")
		{
			if(parseFloat(total_transfer_alloc_qty.value) > parseFloat(transfer))
			{
				if(transfer_type.value == "")
				{
					msg="Allocated Quantity: "+alloc_qty.value+"\nTransferable Quantity: "+transferable_qty.value+"\nTotal Transferable Quantity from Cargo: "+parseFloat(transfer);
					
					msg+="\n\nSum of Transferred Allocated Quantity ("+parseFloat(total_transfer_alloc_qty.value)+") should be less than Allocated Quantity ("+parseFloat(transfer)+") for the deal";
				}
				else
				{
					msg="Sum of Transferred Allocated Quantity  ("+parseFloat(total_transfer_alloc_qty.value)+") should be less or equal to Allocated Quantity ("+parseFloat(transfer)+") for the deal ";
				}
				alert(msg);
				total_transfer_alloc_qty.value = (parseFloat(total_transfer_alloc_qty.value)-parseFloat(obj.value)).toFixed(2);
				total_rec_qty.value=(parseFloat(total_transfer_alloc_qty.value)*(-1)).toFixed(2);
				obj.value="";
			}
		
			sum+=parseFloat(total_transfer_alloc_qty.value);
			if(transfer_type.value == "short_transfer")
			{
				diff+=parseFloat(total_transfer_alloc_qty.value);
				if(parseFloat(diff)>0)
				{
					alert("Transfer short volume "+parseFloat(bal_qty.value)+" MMBTU only!");
					total_transfer_alloc_qty.value-= parseFloat(obj.value).toFixed(2);
					total_rec_qty.value=(parseFloat(total_transfer_alloc_qty.value)*(-1)).toFixed(2);
					sum-= parseFloat(obj.value);
					diff = parseFloat(bal_qty.value)+parseFloat(sum);
					obj.value="";
				}
			}
			else if(transfer_type.value == "full_replacement")
			{
				diff-=parseFloat(total_transfer_alloc_qty.value);
			}
		}
		
		else
		{
			sum+=parseFloat("0");
			diff-=parseFloat("0");
		}
		
	}
	if(transfer_type.value != "" && transfer_type.value != "pseudo_replacement")
	{
		remaining_qty.value = parseFloat(diff).toFixed(2);
	}
	total_transfer.value= parseFloat(sum).toFixed(2);
	total_rec_transfer.value = (parseFloat(sum)*(-1)).toFixed(2);
}

function doSubmit()
{
	var flag = true;
	var chk_count = parseInt("0");
	var msg="";
	var chk = document.getElementsByName("chk");
	var total_rec_transfer = document.getElementById("total_rec_transfer");
	var total_transfer = document.getElementById("total_transfer");
	var sel_cargo_size = document.forms[0].sel_cargo_size;
	var counter_party ="";
	var pur_alloc_qty = document.forms[0].pur_alloc_qty;
	var transfer_type = document.forms[0].transfer_type;
	var bal_qty = document.forms[0].bal_qty;
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					chk_count++;
					var buy_name = document.getElementById("buy_name"+i);
					var buy_cont_disp = document.getElementById("buy_cont_disp"+i);
					var total_rec_qty = document.getElementById("total_rec_qty"+i);
					if(total_rec_qty.value == "")
					{
						flag = false;
						msg+="Please enter transfer quantity for atleast ONE(1) cargo, for buyer "+buy_name.value+" ("+buy_cont_disp.value+")";
						break;
					}
				}
			}
		}
	}
	if(chk_count==0)
	{
		flag = false;
		msg+="Please check alteast ONE(1) deal!\n";
	}
	else
	{
		if(total_rec_qty.value != "")
		{
			if(transfer_type.value=="full_replacement")
			{
				if(parseFloat(pur_alloc_qty.value)!= parseFloat(total_transfer.value))
				{
					flag=false;
					msg+="Replace Full volume "+parseFloat(pur_alloc_qty.value)+" MMBTU only!";
				}
			}
			if(transfer_type.value=="short_transfer")
			{
				if(parseFloat(total_transfer.value)!=parseFloat(bal_qty.value)*(-1))
				{
					flag=false;
					msg+="Transfer short volume "+parseFloat(bal_qty.value)+" MMBTU only!";
				}
			}
		}
	}
	if(flag==true)
	{
		msg = "Total Transfered Quantity:"+total_transfer.value;
		msg+="\nTotal Reconciliation Quantity for parent Cargo:"+total_rec_transfer.value;
		msg+="\nDo you want to transfer MMBTU?"
		a=confirm(msg);
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
</script>
</head>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_EnergyBank" id="energyBank" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String cont_dtl = request.getParameter("cont_dtl")==null?"":request.getParameter("cont_dtl");
String cont_dtl_type = request.getParameter("cont_dtl_type")==null?"":request.getParameter("cont_dtl_type");
String Countpty = request .getParameter("Countpty")==null?"":request.getParameter("Countpty");
String AgmtNo = request.getParameter("AgmtNo")==null?"":request.getParameter("AgmtNo");
String AgmtRev = request.getParameter("AgmtRev")==null?"":request.getParameter("AgmtRev");
String ContNo = request.getParameter("ContNo")==null?"":request.getParameter("ContNo");
String ContRev = request.getParameter("ContRev")==null?"":request.getParameter("ContRev");
String ContTyp = request.getParameter("ContTyp")==null?"":request.getParameter("ContTyp");
String CargoNo = request.getParameter("CargoNo")==null?"": request.getParameter("CargoNo");
String multiCountpty = request.getParameter("multiCountpty")==null?"":request.getParameter("multiCountpty");
String multiAgmtNo = request.getParameter("multiAgmtNo")==null?"":request.getParameter("multiAgmtNo");
String multiAgmtRev = request.getParameter("multiAgmtRev")==null?"":request.getParameter("multiAgmtRev");
String multiContNo= request.getParameter("multiContNo")==null?"":request.getParameter("multiContNo");
String multiContRev= request.getParameter("multiContRev")==null?"":request.getParameter("multiContRev");
String multiContTyp= request.getParameter("multiContTyp")==null?"":request.getParameter("multiContTyp");
String multiCargoNo= request.getParameter("multiCargoNo")==null?"":request.getParameter("multiCargoNo");
String transfer_type = request.getParameter("transfer_type")==null?"":request.getParameter("transfer_type");
String bal_qty = request.getParameter("bal_qty")==null?"":request.getParameter("bal_qty");
String pur_alloc_qty = request.getParameter("pur_alloc_qty")==null?"":request.getParameter("pur_alloc_qty");
String pool_flag = request.getParameter("pool_flag")==null?"":request.getParameter("pool_flag");

String[] SEL_CARGO = multiContNo.split("@@");

energyBank.setCallFlag("TRANSFER_CARGO");
energyBank.setMultiCountpty(Countpty);
energyBank.setMultiAgmtNo(AgmtNo);
energyBank.setMultiAgmtRev(AgmtRev);
energyBank.setMultiContNo(ContNo);
energyBank.setMultiContTyp(ContTyp);
energyBank.setMultiCargoNo(CargoNo);
energyBank.setTempMultiCountpty(multiCountpty);
energyBank.setTempMultiAgmtNo(multiAgmtNo);
energyBank.setTempMultiAgmtRev(multiAgmtRev);
energyBank.setTempMultiContNo(multiContNo);
energyBank.setTempMultiContTyp(multiContTyp);
energyBank.setTempMultiCargoNo(multiCargoNo);
energyBank.setComp_cd(owner_cd);
energyBank.setTransferType(transfer_type);
energyBank.init();

Vector VCUSTOMER_CD = energyBank.getVCUSTOMER_CD();
Vector VCUSTOMER_ABBR = energyBank.getVCUSTOMER_ABBR();
Vector VCUSTOMER_NM = energyBank.getVCUSTOMER_NM();
Vector VCUST_AGMT_NO = energyBank.getVCUST_AGMT_NO();
Vector VCUST_AGMT_REV_NO = energyBank.getVCUST_AGMT_REV_NO();
Vector VCUST_CONT_NO= energyBank.getVCUST_CONT_NO();
Vector VCUST_CONT_REV_NO = energyBank.getVCUST_CONT_REV_NO();
Vector VALLOC_QTY_TO_CUST = energyBank.getVALLOC_QTY_TO_CUST();
Vector VCUST_DISPLAY_CONT_DTL = energyBank.getVCUST_DISPLAY_CONT_DTL();
Vector VCUST_START_DT = energyBank.getVCUST_START_DT();
Vector VCUST_END_DT = energyBank.getVCUST_END_DT();
Vector VCUST_BOOKED_QTY = energyBank.getVCUST_BOOKED_QTY();
Vector VCUST_CONT_STATUS = energyBank.getVCUST_CONT_STATUS();
Vector VCUST_CONT_STATUS_FLAG = energyBank.getVCUST_CONT_STATUS_FLAG();
Vector VCUST_RATE = energyBank.getVCUST_RATE();
Vector VCUST_RATE_UNIT = energyBank.getVCUST_RATE_UNIT();
Vector VCUST_RATE_UNIT_NM = energyBank.getVCUST_RATE_UNIT_NM();
Vector VCUST_SUPPLIED_QTY = energyBank.getVCUST_SUPPLIED_QTY();
Vector VCUST_CONT_TYPE = energyBank.getVCUST_CONT_TYPE();
Vector VCUST_TRANSERABLE_QTY = energyBank.getVCUST_TRANSERABLE_QTY();
Vector VCUST_CONTRACT_TYPE_NM = energyBank.getVCUST_CONTRACT_TYPE_NM();
Vector VCUST_CONT_REF = energyBank.getVCUST_CONT_REF();

Vector VSEL_DISPLAY_CONT_DTL = energyBank.getVSEL_DISPLAY_CONT_DTL();
Vector VSEL_CONT_NO = energyBank.getVSEL_CONT_NO();
Vector VSEL_CONT_TYPE = energyBank.getVSEL_CONT_TYPE();
Vector VSEL_BALANCE_QTY = energyBank.getVSEL_BALANCE_QTY();
Vector VSEL_COLOR = energyBank.getVSEL_COLOR();
Vector VSEL_CARGO_NO = energyBank.getVSEL_CARGO_NO();
Vector VSEL_RATE = energyBank.getVSEL_RATE();
Vector VSEL_RATE_UNIT = energyBank.getVSEL_RATE_UNIT();
Vector VSEL_RATE_UNIT_NM = energyBank.getVSEL_RATE_UNIT_NM();
Vector VSEL_CONT_TYPE_NM = energyBank.getVSEL_CONT_TYPE_NM();
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
							Transfer MMBTU [<%=cont_dtl %>]
						</div>
						<span class="btn rounded-circle" style="background:var(--header_color);color:var(--header_font_color);" title="Back" onclick="goBack();">
							  &nbsp;<i class="fa fa-step-backward fa-2x"></i>&nbsp;
						</span>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="form-group row justify-content-center">
								<div class="col-auto">
									<a class="btn" style="background:#ccffcc;">Unloaded</a>
								</div>
								<div class="col-auto">
									<a class="btn" style="background:#ff66d9;">Expected</a>
								</div>
								<div class="col-auto">
									<a class="btn" style="background:#ffb380;">Pseudo</a>
								</div>
								<div class="col-auto">
									<a class="btn" style="background:#09F4FE;">OWN Volume</a>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<!-- <div class="form-group row"> -->
		    				<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Active-SN/LOA [<%=cont_dtl %>] <%if(transfer_type.equals("short_transfer")){%>[Short Transfer: <%=bal_qty%>]<%}else if(transfer_type.equals("full_replacement")){%>[Full Replacement: <%=pur_alloc_qty%>]<%}else if(transfer_type.equals("pseudo_replacement")){%>[Pseudo Replacement]<%}%></label>
				  		<!-- </div> -->
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="transfer_table">
								<thead>
									<tr>
										<th align="center" rowspan=2></th>
										<th align="center" rowspan=2>Buyer<br>Details</th>
										<th align="center" rowspan=2>Buyer <br> Contract#</th>
										<th align="center" rowspan=2>Buyer <br> Contract Type</th>
										<th align="center" colspan=5>Parent Cargo Allocation Details</th>
										<th align="center" rowspan=2>Transferable QTY for the deal<br>(MMBTU)</th>
										<th align="center" colspan=6>Destination Cargo Allocation Details</th>
										<th align="center" rowspan=2>Sum of Transfered Allocated QTY(MMBTU)</th>
										<th align="center" rowspan=2>Reconciliation QTY for parent Cargo<br>(MMBTU)</th>
									</tr>
									<tr>
										<th align="center">Cargo REF#</th>
										<th align="center">Cargo Contract Type</th>
										<th align="center">Sales Price</th>
										<th align="center">Currency/MMBTU</th>
										<th align="center">Allocated QTY(MMBTU)</th>
										<th align="center">Cargo REF#</th>
										<th align="center">Cargo Contract Type</th>
										<th align="center">Cargo Avail Qty</th>
										<th align="center">Cost Price</th>
										<th align="center">Currency/MMBTU</th>
										<th align="center">Transfer Allocated QTY(MMBTU)</th>
									</tr>
								</thead>
								<tbody>
									<%if(VCUSTOMER_CD.size()>0){
									for(int i=0;i<VCUSTOMER_CD.size();i++){
										for(int j=0;j<SEL_CARGO.length;j++){
									%>
									<tr>
										<%if(j==0){%>
											<td rowspan="<%=SEL_CARGO.length%>" align="center"><input type="checkbox" name="chk" id="chk<%=i%>" onclick="Enable('<%=i%>');" value=<%=i %>>&nbsp;</td>
											<td rowspan="<%=SEL_CARGO.length%>" align="center">
												<%=VCUSTOMER_NM.elementAt(i)%> 
												<input type="hidden" name="buy_cd<%=i%>" id="buy_cd<%=i%>" value='<%=VCUSTOMER_CD.elementAt(i)%>'>
												<input type="hidden" name="buy_name<%=i%>" id="buy_name<%=i%>" value='<%=VCUSTOMER_NM.elementAt(i)%>'>
												<input type="hidden" name="buy_cont_disp<%=i%>" id="buy_cont_disp<%=i%>" value='<%=VCUST_DISPLAY_CONT_DTL.elementAt(i) %>'>
												<input type="hidden" name="buy_agmt_no<%=i%>" id="buy_agmt_no<%=i%>" value='<%=VCUST_AGMT_NO.elementAt(i)%>'>
												<input type="hidden" name="buy_agmt_rev_no<%=i%>" id="buy_agmt_rev_no<%=i%>" value='<%=VCUST_AGMT_REV_NO.elementAt(i)%>'>
												<input type="hidden" name="buy_cont_no<%=i%>" id="buy_cont_no<%=i%>" value='<%=VCUST_CONT_NO.elementAt(i)%>'>
												<input type="hidden" name="buy_cont_rev_no<%=i%>" id="buy_cont_rev_no<%=i%>" value='<%=VCUST_CONT_REV_NO.elementAt(i)%>'>
												<input type="hidden" name="buy_cont_type<%=i%>" id="buy_cont_type<%=i%>" value='<%=VCUST_CONT_TYPE.elementAt(i) %>'>
												<input type="hidden" name="sales_price<%=i%>" id="sales_price<%=i%>" value='<%=VCUST_RATE.elementAt(i)%>'>
												<input type="hidden" name="sales_price_rate_unit<%=i%>" id="sales_price_rate_unit<%=i%>" value='<%=VCUST_RATE_UNIT.elementAt(i)%>'>
											</td> 
											<td rowspan="<%=SEL_CARGO.length%>" align="center"><%=VCUST_DISPLAY_CONT_DTL.elementAt(i) %> [<%=VCUST_CONT_REF.elementAt(i)%>]</td>
											<td rowspan="<%=SEL_CARGO.length%>" align="center"><%=VCUST_CONTRACT_TYPE_NM.elementAt(i) %></td>
											<td rowspan="<%=SEL_CARGO.length%>" align="center"><%=cont_dtl %></td>
											<td rowspan="<%=SEL_CARGO.length%>" align="center"><%=cont_dtl_type %></td>
											<td rowspan="<%=SEL_CARGO.length%>" align="center"><%=VCUST_RATE.elementAt(i) %></td>
											<td rowspan="<%=SEL_CARGO.length%>" align="center"><%=VCUST_RATE_UNIT_NM.elementAt(i) %></td>
											<td rowspan="<%=SEL_CARGO.length%>" align="center">
												<%=VALLOC_QTY_TO_CUST.elementAt(i) %>
												<input type="hidden" name="alloc_qty" id="alloc_qty<%=i%>" value='<%=VALLOC_QTY_TO_CUST.elementAt(i) %>'>
											</td>
											<td rowspan="<%=SEL_CARGO.length%>" align="center">
												<%=VCUST_TRANSERABLE_QTY.elementAt(i) %>
												<input type="hidden" name="transferable_qty" id="transferable_qty<%=i%>" value="<%=VCUST_TRANSERABLE_QTY.elementAt(i)%>">
											</td>
										<%} %>
										<td align="center" >
											<div style="background:<%=VSEL_COLOR.elementAt(j)%>">
												<%=VSEL_DISPLAY_CONT_DTL.elementAt(j) %>
											</div>
											<input type="hidden" value="<%=VSEL_DISPLAY_CONT_DTL.elementAt(j)%>" name="pur_cont_disp_no<%=i%>" id="pur_cont_disp_no<%=i%>_<%=j%>">
											<input type="hidden" value="<%=VSEL_CONT_TYPE.elementAt(j)%>" name="pur_cont_type<%=i%>" id="pur_cont_type<%=i%>_<%=j%>">
											<input type="hidden" value="<%=VSEL_CONT_NO.elementAt(j) %>" name="pur_cont_no<%=i%>" id="pur_cont_no<%=i%>_<%=j%>">
											<input type="hidden" value="<%=VSEL_CARGO_NO.elementAt(j) %>" name="pur_cargo_no<%=i%>" id="pur_cargo_no<%=i%>_<%=j%>">
											<input type="hidden" value="<%=VSEL_RATE.elementAt(j) %>" name="cost_price<%=i%>" id="cost_price<%=i%>_<%=j%>">
											<input type="hidden" value="<%=VSEL_RATE_UNIT.elementAt(j) %>" name="cost_price_rate_unit<%=i%>" id="cost_price_rate_unit<%=i%>_<%=j%>">
										</td>
										<td align="center">
											<%=VSEL_CONT_TYPE_NM.elementAt(j) %>
										</td>
										<td align="center">
											<%=VSEL_BALANCE_QTY.elementAt(j) %>
											<input type="hidden" name="avail_qty<%=i%>" id="avail_qty<%=i%>_<%=j%>" value="<%=VSEL_BALANCE_QTY.elementAt(j)%>">
										</td>
										<td><%=VSEL_RATE.elementAt(j) %></td>
										<td><%=VSEL_RATE_UNIT_NM.elementAt(j) %></td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="transfer_alloc_qty<%=i%>" id="transfer_alloc_qty<%=i%>_<%=j%>" value="" style="text-align:right;" 
												onkeyup="checkNumber1(this,10,2);checkBalanceQty('<%=i%>');totalAlloc('<%=i%>');calculateTotalAlloc(this);" onchange="checkZero('<%=i%>');checkNumber1(this,10,2);" disabled>
											</div>
										</td>
										<%if(j==0){%>
											<td rowspan="<%=SEL_CARGO.length%>" align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="total_transfer_alloc_qty" id="total_transfer_alloc_qty<%=i%>" value="" style="text-align:right;" readOnly>
												</div>
											</td>
											<td rowspan="<%=SEL_CARGO.length%>" align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="total_rec_qty" id="total_rec_qty<%=i%>" value="" style="text-align:right;" readOnly>
												</div>
											</td>
										<%} %>
									</tr>
									<%} 
									}%>
									<tr>
										<td colspan=16 align="right">Total:</td>
										<td align="center">
											<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="total_transfer" id="total_transfer" value="" style="text-align:right;" readOnly>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="total_rec_transfer" id="total_rec_transfer" value="" style="text-align:right;" readOnly>
											</div>
										</td>
									</tr>
									<%if(!transfer_type.equals("") && !transfer_type.equals("pseudo_replacement")){							%>
										<tr>
											<td align="right" colspan=16>Remaining:</td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="remaining_qty" id="remaining_qty" value="<%if(transfer_type.equals("short_transfer")){%><%=bal_qty%><%}else if(transfer_type.equals("full_replacement")){%><%=pur_alloc_qty%><%} %>" style="text-align:right;" readOnly>
												</div>
											</td>
											<td></td>
										</tr>
									<%} %>
								<%}else{%>
									<tr>
										<td colspan=18 align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
									</tr>
								<%}%>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Transfer" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Transfer" disabled>
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>		
</div>
<input type="hidden" name="option" value="MMBTU_TRANSFER">
<input type="hidden" name="sel_cargo_size" value="<%=SEL_CARGO.length %>">
<input type="hidden" name="contract_size" value="<%=VCUSTOMER_CD.size() %>">
<input type="hidden" name="old_pur_cont_no" value="<%=ContNo%>">
<input type="hidden" name="old_pur_cargo_no" value="<%=CargoNo %>">
<input type="hidden" name="Countpty" value="<%=Countpty %>">
<input type="hidden" name="AgmtNo" value="<%=AgmtNo %>">
<input type="hidden" name="AgmtRev" value="<%=AgmtRev %>">
<input type="hidden" name="ContNo" value="<%=ContNo %>">
<input type="hidden" name="ContRev" value="<%=ContRev %>">
<input type="hidden" name="ContTyp" value="<%=ContTyp %>">
<input type="hidden" name="CargoNo" value="<%=CargoNo %>">
<input type="hidden" name="transfer_type" value="<%=transfer_type%>">
<input type="hidden" name="bal_qty" value="<%=bal_qty%>">
<input type="hidden" name="pur_alloc_qty" value="<%=pur_alloc_qty%>">
<input type="hidden" name="pool_flag" value="<%=pool_flag%>">

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