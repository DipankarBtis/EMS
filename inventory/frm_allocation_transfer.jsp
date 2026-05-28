<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function doShortTransfer()
{
	var transfer_type = document.forms[0].transfer_type;
	var sel_dest_crgo = document.getElementById("sel_dest_cargo");
	var transfer_btn_div = document.getElementById("transfer_btn_div");
	var short_transfer = document.forms[0].short_transfer;
	var full_replacement = document.forms[0].full_replacement;
	var dest_cargo_flag = document.forms[0].dest_cargo_flag;
	
	if(full_replacement.checked)
	{
		full_replacement.checked = false;
	}
	if(short_transfer.checked)
	{
		transfer_type.value = "short_transfer";
		if(dest_cargo_flag.value == "false")
		{
			if(sel_dest_cargo.style.display=="none")
			{
				sel_dest_cargo.style.display = 'block';
				transfer_btn_div.style.display = 'block';
			}
		}
	}
	else
	{
		transfer_type.value="";
		if(dest_cargo_flag.value == "false")
		{
			sel_dest_cargo.style.display = 'none';
			transfer_btn_div.style.display = 'none';
		}
	}
}

function doFullReplacement()
{
	var transfer_type = document.forms[0].transfer_type;
	var sel_dest_crgo = document.getElementById("sel_dest_cargo");
	var transfer_btn_div = document.getElementById("transfer_btn_div");
	var short_transfer = document.forms[0].short_transfer;
	var full_replacement = document.forms[0].full_replacement;
	var dest_cargo_flag = document.forms[0].dest_cargo_flag;
	
	if(short_transfer.checked)
	{
		short_transfer.checked=false;
	}
	if(full_replacement.checked)
	{
		transfer_type.value = "full_replacement";
		if(dest_cargo_flag.value == "false")
		{
			if(sel_dest_cargo.style.display=="none")
			{
				sel_dest_cargo.style.display = 'block';
				transfer_btn_div.style.display = 'block';
			}
		}
	}
	else
	{
		transfer_type.value="";
		if(dest_cargo_flag.value == "false")
		{
			sel_dest_cargo.style.display = 'none';
			transfer_btn_div.style.display = 'none';
		}
	}
}

function doPseudoReplacement()
{
	var transfer_type = document.forms[0].transfer_type;
	var pseudo_replacement = document.forms[0].pseudo_replacement;
	var sel_dest_crgo = document.getElementById("sel_dest_cargo");
	var transfer_btn_div = document.getElementById("transfer_btn_div");
	var dest_cargo_flag = document.forms[0].dest_cargo_flag;
	
	if(pseudo_replacement.checked)
	{
		transfer_type.value = "pseudo_replacement";
		if(dest_cargo_flag.value == "false")
		{
			if(sel_dest_cargo.style.display=="none")
			{
				sel_dest_cargo.style.display = 'block';
				transfer_btn_div.style.display = 'block';
			}
		}
	}
	else
	{
		transfer_type.value = "";
		if(dest_cargo_flag.value == "false")
		{
			sel_dest_cargo.style.display = 'none';
			transfer_btn_div.style.display = 'none';
		}
	}
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

function doSubmit()
{
	var Countpty = document.forms[0].Countpty.value;
	var AgmtNo = document.forms[0].AgmtNo.value;
	var AgmtRev = document.forms[0].AgmtRev.value;
	var ContNo = document.forms[0].ContNo.value;
	var ContRev = document.forms[0].ContRev.value;
	var ContTyp = document.forms[0].ContTyp.value;
	var CargoNo = document.forms[0].CargoNo.value;
	var cont_dtl = document.forms[0].cont_dtl.value;
	var cont_dtl_type = document.forms[0].cont_dtl_type.value;
	var chk = document.forms[0].chk;
	var counterparty_cd = document.forms[0].counterparty_cd;
	var agmt_no = document.forms[0].agmt_no;
	var agmt_rev_no = document.forms[0].agmt_rev_no;
	var cont_no = document.forms[0].cont_no;
	var cont_rev_no = document.forms[0].cont_rev_no;
	var contract_type = document.forms[0].contract_type;
	var balance_qty = document.forms[0].balance_qty;
	var cargo_no = document.forms[0].cargo_no;
	var u = document.forms[0].u.value;
	var cargo_status_flg = document.forms[0].cargo_status_flg;
	var chk_count = parseInt("0");
	var transfer_type = document.forms[0].transfer_type;
	var bal_qty = document.forms[0].bal_qty; 
	var pur_alloc_qty = document.forms[0].pur_alloc_qty;
	var pool_flag = document.forms[0].pool_flag;
	
	var msg="";
	var flag=true;
	
	var multiCountpty="";
	var multiAgmtNo="";
	var multiAgmtRev="";
	var multiContNo="";
	var multiContRev="";
	var multiContTyp="";
	var multiCargoNo="";
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					chk_count++;
					
					if(parseInt(balance_qty[i].value) <= 0)
					{
						msg="Select Cargo having Balance Qty > 0! ";
						flag=false;
						break;
					}
					else if(cargo_status_flg[i].value == "X" && contract_type[i].value == "N")
					{
						msg="Unselect Cargo With Canceled Status and Proceed!";
						flag=false;
						break;
					}
					else
					{
						if(multiContNo == "")
						{
							multiCountpty+=counterparty_cd[i].value;
							multiAgmtNo+=agmt_no[i].value;
							multiAgmtRev+=agmt_rev_no[i].value;
							multiContNo+=cont_no[i].value;
							multiContRev+=cont_rev_no[i].value;
							multiContTyp+=contract_type[i].value;
							multiCargoNo+=cargo_no[i].value;
						}
						else
						{
							multiCountpty+="@@"+counterparty_cd[i].value;
							multiAgmtNo+="@@"+agmt_no[i].value;
							multiAgmtRev+="@@"+agmt_rev_no[i].value;
							multiContNo+="@@"+cont_no[i].value;
							multiContRev+="@@"+cont_rev_no[i].value;
							multiContTyp+="@@"+contract_type[i].value;
							multiCargoNo+="@@"+cargo_no[i].value;
						}
					}
				}
			}
		}
	}
	if(parseInt(chk_count) == 0)
	{
		alert("Select atleast ONE(1) Purchase Contract# for Allocation!");
	}
	else if(!flag)
	{
		alert(msg);
	}
	else
	{
		var a = confirm("Are you sure you want to proceed?")
		if(a)
		{
			var url = "frm_transfer_cargo.jsp?cont_dtl="+cont_dtl+"&Countpty="+Countpty+"&AgmtNo="+AgmtNo+"&AgmtRev="+AgmtRev+"&ContNo="+ContNo
			+"&ContRev="+ContRev+"&ContTyp="+ContTyp+"&CargoNo="+CargoNo+"&multiCountpty="+multiCountpty
			+"&multiAgmtNo="+multiAgmtNo+"&multiAgmtRev="+multiAgmtRev+"&multiContNo="+multiContNo+"&multiContRev="+multiContRev
			+"&multiContTyp="+multiContTyp+"&multiCargoNo="+multiCargoNo+"&u="+u+"&pool_flag="+pool_flag.value+"&cont_dtl_type="+cont_dtl_type;
			if(transfer_type.value!="")
			{
				url+="&transfer_type="+transfer_type.value+"&bal_qty="+bal_qty.value+"&pur_alloc_qty="+pur_alloc_qty.value;
			}
			location.replace(url);
		}
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_EnergyBank" id="energyBank" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String Countpty = request .getParameter("Countpty")==null?"":request.getParameter("Countpty");
String AgmtNo = request.getParameter("AgmtNo")==null?"":request.getParameter("AgmtNo");
String AgmtRev = request.getParameter("AgmtRev")==null?"":request.getParameter("AgmtRev");
String ContNo = request.getParameter("ContNo")==null?"":request.getParameter("ContNo");
String ContRev = request.getParameter("ContRev")==null?"":request.getParameter("ContRev");
String ContTyp = request.getParameter("ContTyp")==null?"":request.getParameter("ContTyp");
String CargoNo = request.getParameter("CargoNo")==null?"": request.getParameter("CargoNo");
String pool_flag = request.getParameter("pool_flag")==null?"":request.getParameter("pool_flag");

energyBank.setCallFlag("MMBTU_ALLOCATION_TRANSFER");
energyBank.setMultiCountpty(Countpty);
energyBank.setMultiAgmtNo(AgmtNo);
energyBank.setMultiAgmtRev(AgmtRev);
energyBank.setMultiContNo(ContNo);
energyBank.setMultiContTyp(ContTyp);
energyBank.setMultiCargoNo(CargoNo);
energyBank.setComp_cd(owner_cd);
energyBank.init();

Vector VSEL_DISPLAY_CONT_DTL = energyBank.getVSEL_DISPLAY_CONT_DTL();
Vector VSEL_CONT_TYPE = energyBank.getVSEL_CONT_TYPE();
Vector VSEL_CONT_TYPE_NM = energyBank.getVSEL_CONT_TYPE_NM();
Vector VSEL_CONT_NAME = energyBank.getVSEL_CONT_NAME();
Vector VSEL_CONT_REF= energyBank.getVSEL_CONT_REF();
Vector VSEL_CONT_STATUS = energyBank.getVSEL_CONT_STATUS();
Vector VSEL_START_DT = energyBank.getVSEL_START_DT();
Vector VSEL_END_DT = energyBank.getVSEL_END_DT();
Vector VSEL_MIN_ALLOC_DT = energyBank.getVSEL_MIN_ALLOC_DT();
Vector VSEL_MAX_ALLOC_DT = energyBank.getVSEL_MAX_ALLOC_DT();
Vector VSEL_PRICE_TYPE = energyBank.getVSEL_PRICE_TYPE();
Vector VSEL_RATE = energyBank.getVSEL_RATE();
Vector VSEL_RATE_UNIT_NM = energyBank.getVSEL_RATE_UNIT_NM();
Vector VSEL_BOOKED_QTY = energyBank.getVSEL_BOOKED_QTY();
Vector VSEL_UNLOADED_QTY = energyBank.getVSEL_UNLOADED_QTY();
Vector VSEL_ALLOCATED_QTY = energyBank.getVSEL_ALLOCATED_QTY();
Vector VSEL_BALANCE_QTY = energyBank.getVSEL_BALANCE_QTY();
Vector VSEL_AVAIL_QTY = energyBank.getVSEL_AVAIL_QTY();

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
Vector VCUST_RATE_UNIT_NM = energyBank.getVCUST_RATE_UNIT_NM();
Vector VCUST_SUPPLIED_QTY = energyBank.getVCUST_SUPPLIED_QTY();
Vector VCUST_CONTRACT_TYPE_NM = energyBank.getVCUST_CONTRACT_TYPE_NM();
Vector VCUST_CONT_REF = energyBank.getVCUST_CONT_REF();

Vector VCARGO_POOL_NM = energyBank.getVCARGO_POOL_NM();
Vector VCARGO_POOL_FLAG = energyBank.getVCARGO_POOL_FLAG();
Vector VINDEX = energyBank.getVINDEX();
Vector VCOUNTERPARTY_CD = energyBank.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = energyBank.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = energyBank.getVCOUNTERPARTY_ABBR();
Vector VPURCHASE_MAP_ID = energyBank.getVPURCHASE_MAP_ID();
Vector VCONTRACT_TYPE_NM=energyBank.getVCONTRACT_TYPE_NM();
Vector VCONTRACT_TYPE = energyBank.getVCONTRACT_TYPE();
Vector VAGMT_NO = energyBank.getVAGMT_NO();
Vector VAGMT_REV_NO = energyBank.getVAGMT_REV_NO();
Vector VCONT_NO = energyBank.getVCONT_NO();
Vector VCONT_REV_NO = energyBank.getVCONT_REV_NO();
Vector VBALANCE_QTY = energyBank.getVBALANCE_QTY();
Vector VCARGO_NO = energyBank.getVCARGO_NO();
Vector VCARGO_STATUS_FLG = energyBank.getVCARGO_STATUS_FLG();
Vector VALLOCATED_QTY = energyBank.getVALLOCATED_QTY();
Vector VCONT_NAME = energyBank.getVCONT_NAME();
Vector VCONT_REF = energyBank.getVCONT_REF();
Vector VCONT_STATUS = energyBank.getVCONT_STATUS();
Vector VSTART_DT = energyBank.getVSTART_DT();
Vector VEND_DT = energyBank.getVEND_DT();
Vector VMIN_ALLOC_DT = energyBank.getVMIN_ALLOC_DT();
Vector VMAX_ALLOC_DT = energyBank.getVMAX_ALLOC_DT();
Vector VPRICE_TYPE = energyBank.getVPRICE_TYPE();
Vector VRATE_UNIT_NM = energyBank.getVRATE_UNIT_NM();
Vector VRATE = energyBank.getVRATE();
Vector VBOOKED_QTY = energyBank.getVBOOKED_QTY();
Vector VUNLOADED_QTY_INFO = energyBank.getVUNLOADED_QTY_INFO();
Vector VUNLOADED_QTY = energyBank.getVUNLOADED_QTY();
Vector VAVAIL_FOR_SALE_QTY_INFO = energyBank.getVAVAIL_FOR_SALE_QTY_INFO();
Vector VAVAIL_FOR_SALE_QTY = energyBank.getVAVAIL_FOR_SALE_QTY();
Vector VREMARK = energyBank.getVREMARK();

String totalSupplied_Qty = energyBank.getTotalAlloc_Qty();
boolean dest_cargo_flag = false;
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
					    	MMBTU Allocation Transfer
					    </div>
					    <span class="btn rounded-circle" style="background:var(--header_color);color:var(--header_font_color);" title="Back" onclick="goBack();">
						  &nbsp;<i class="fa fa-step-backward fa-2x"></i>&nbsp;
						</span>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
	    				<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Transfer MMBTU [<%=VSEL_DISPLAY_CONT_DTL.elementAt(0)%>]</label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<%if(pool_flag.equals("S")){%>
											<th>Pseudo Cargo#</th>
										<%}else{%>
											<th>Purchase Contract#</th>
										<%}%>
										<%if(!pool_flag.equals("O")){ %>
											<th>Contract Type</th>
										<%} %>
										<%if(!pool_flag.equals("S")&&!pool_flag.equals("O")){%>
											<th>Contract Name</th>
											<th>Contract/Cargo/Trade Ref#</th>
											<th>Status</th>
											<th>Contract Period</th>
											<th>Allocation Start Date</th>
											<th>Last Allocation Date </th>
											<th>Price Type</th>
											<th>Currency/MMBTU</th>
											<th>Price</th>
										<%}%>
										<%if(pool_flag.equals("O")){%>
											<th>Projected MMBTU</th>
										<%}else{%>
											<th>MMBTU Booked</th>
										<%}%>
										<%if(!pool_flag.equals("S")){%>
											<%if(!pool_flag.equals("O")){%>
											<th>MMBTU Unloaded(&Projected)</th>
											<%} %>
											<th><%if(pool_flag.equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Avail for Sale </th>
										<%}%>
										<th>MMBTU Allocated</th>
										<th>Balance MMBTU</th>
									</tr>
								</thead>
								<tbody>
									<%if(VSEL_DISPLAY_CONT_DTL.size()>0){
									for(int i=0;i<VSEL_DISPLAY_CONT_DTL.size();i++){%>
									<tr>
										<td align="center"><%=VSEL_DISPLAY_CONT_DTL.elementAt(i)%></td>
										<input type="hidden" name="cont_dtl" value="<%=VSEL_DISPLAY_CONT_DTL.elementAt(i)%>"/>
										<input type="hidden" name="cont_dtl_type" value="<%=VSEL_CONT_TYPE_NM.elementAt(i)%>"/>
										<%if(!pool_flag.equals("O")){%>
										<td align="center">
											<span
													<%if(VSEL_CONT_TYPE.elementAt(i).equals("D")){ %>
								    					style="background: #66ffd9;"
								    				<%}else if(VSEL_CONT_TYPE.elementAt(i).equals("N")){ %>
								    					style="background: skyblue;"
								    				<%}else if(VSEL_CONT_TYPE.elementAt(i).equals("I")){ %>
								    					style="background: pink;"
								    				<%}else if(VSEL_CONT_TYPE.elementAt(i).equals("T")){ %>
							    						style="background: #E9CCEE;"
							    					<%} %>
												><b><%=VSEL_CONT_TYPE_NM.elementAt(i)%></b></span>
										</td>
										<%} %>
										<%if(!pool_flag.equals("S")&&!pool_flag.equals("O")){%>
											<td align="center"><%=VSEL_CONT_NAME.elementAt(i) %></td>
											<td align="center"><%=VSEL_CONT_REF.elementAt(i) %></td>
											<td align="center"><%=VSEL_CONT_STATUS.elementAt(i) %></td>
											<td align="center"><%=VSEL_START_DT.elementAt(i)%> - <%=VSEL_END_DT.elementAt(i)%></td>
											<td align="center"><%=VSEL_MIN_ALLOC_DT.elementAt(i) %></td>
											<td align="center"><%=VSEL_MAX_ALLOC_DT.elementAt(i) %></td>
											<td align="center"><%=VSEL_PRICE_TYPE.elementAt(i) %></td>
											<td align="center"><%=VSEL_RATE_UNIT_NM.elementAt(i) %></td>
											<td align="center"><%=VSEL_RATE.elementAt(i) %></td>
										<%}%>
										<td align="center"><%=VSEL_BOOKED_QTY.elementAt(i) %></td>
										<%if(!pool_flag.equals("S")){%>
											<%if(!pool_flag.equals("O")){%>
												<td align="center"><%=VSEL_UNLOADED_QTY.elementAt(i) %></td>
											<%}%>
												<td align="center"><%=VSEL_AVAIL_QTY.elementAt(i) %></td>
										<%}%>
										<td align="center">
											<%=VSEL_ALLOCATED_QTY.elementAt(i) %>
											<input type="hidden" name="pur_alloc_qty" value="<%=VSEL_ALLOCATED_QTY.elementAt(i)%>" >
										</td>
										<td align="center">
											<%=VSEL_BALANCE_QTY.elementAt(i) %>
											<input type="hidden" name="bal_qty" id="pur_bal_qty" value="<%=VSEL_BALANCE_QTY.elementAt(i)%>"> 
										</td>
									</tr>
									<%}
									}else{%>
									<tr>
										<td colspan="15" align="center">
											<%=utilmsg.infoMessage("<b>No Cargo Details available</b>") %>
										</td>
									</tr>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div> 
					<div class="row">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> SN/LOA [<%=VSEL_DISPLAY_CONT_DTL.elementAt(0)%>]</label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="sn_loa_table">
								<thead>
									<tr>
										<th>Sr No.</th>
										<th>
											Buyer
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="sn_loa_buyer" 
												onkeyup="Search1(this,'1','sn_loa_table');" placeholder="Search.." style="width:100px"/>
											</div>
										</th>
										<th>
											Sales Contract#
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="sn_loa_cont" 
												onkeyup="Search1(this,'2','sn_loa_table');" placeholder="Search.." style="width:100px"/>
											</div>
										</th>
										<th>
											Sales Contract Type
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="sn_loa_cont_type" 
												onkeyup="Search1(this,'3','sn_loa_table');" placeholder="Search.." style="width:100px"/>
											</div>
										</th>
										<th>
											Sales Contract Period
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="sn_loa_cont_period" 
												onkeyup="Search1(this,'4','sn_loa_table');" placeholder="Search.." style="width:100px"/>
											</div>
										</th>
										<th>
											TCQ (MMBTU) 
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="sn_loa_tcq" 
												onkeyup="Search1(this,'5','sn_loa_table');" placeholder="Search.." style="width:100px"/>
											</div>
										</th>
										<th>
											Supplied QTY (MMBTU)
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="sn_loa_supp_qty" 
												onkeyup="Search1(this,'6','sn_loa_table');" placeholder="Search.." style="width:100px"/>
											</div>
										</th>
										<th>
											Status
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="sn_loa_status" 
												onkeyup="Search1(this,'7','sn_loa_table');" placeholder="Search.." style="width:100px"/>
											</div>
										</th>
										<th>
											Allocation QTY (MMBTU) 
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="sn_loa_alloc_qty" 
												onkeyup="Search1(this,'8','sn_loa_table');" placeholder="Search.." style="width:100px"/>
											</div>
										</th>
										<th>
											Currency/MMBTU
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="sn_loa_currency" 
												onkeyup="Search1(this,'9','sn_loa_table');" placeholder="Search.." style="width:100px"/>
											</div>
										</th> 
										<th>
											Sales Price
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="sn_loa_sp" 
												onkeyup="Search1(this,'10','sn_loa_table');" placeholder="Search.." style="width:100px"/>
											</div>
										</th> 
									</tr>
								</thead>
								<tbody>
 								<% int expire_count=0;
 								if(VCUSTOMER_CD.size()>0){
 								for(int i=0;i<VCUSTOMER_CD.size();i++){ %> 
									<tr>
										<td align="center"><%=i+1%></td>
										<td align="center"><%=VCUSTOMER_NM.elementAt(i) %></td>
										<td align="center"><%=VCUST_DISPLAY_CONT_DTL.elementAt(i) %> [<%=VCUST_CONT_REF.elementAt(i)%>]</td>
										<td align="center"><%=VCUST_CONTRACT_TYPE_NM.elementAt(i) %></td>
										<td align="center"><%=VCUST_START_DT.elementAt(i)%> - <%=VCUST_END_DT.elementAt(i) %></td>
										<td align="center"><%=VCUST_BOOKED_QTY.elementAt(i) %></td>
										<td align="center"><%=VCUST_SUPPLIED_QTY.elementAt(i) %></td>
										<td align="center"><%=VCUST_CONT_STATUS.elementAt(i) %></td>
										<%if(VCUST_CONT_STATUS.elementAt(i).toString().equals("EXPIRE"))
										{
											expire_count+=1;
										}
										%>
										<td align="center"><%=VALLOC_QTY_TO_CUST.elementAt(i) %></td>
										<td align="center"><%=VCUST_RATE_UNIT_NM.elementAt(i) %></td>
										<td align="center"><%=VCUST_RATE.elementAt(i) %></td>
									</tr>
								<%}%> 
									<tr>
										<td align="right" colspan="8">Total QTY</td>
										<td align="center"><%=totalSupplied_Qty%></td>
										<td colspan=2></td>
									</tr>
								<%} else{%>
									<tr>
										<td colspan="11" align="center"> 
											<%=utilmsg.infoMessage("<b>This cargo is not allocated to any deal!</b>") %>
										</td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<%if((Float.parseFloat(VSEL_BALANCE_QTY.elementAt(0).toString())<0 || ((pool_flag.equals("E"))&&(VCUSTOMER_CD.size()>0)) || ((pool_flag.equals("S"))&&(VCUSTOMER_CD.size()>0)))){%>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<div class="row">
		    				<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Transfer Type</label>
						</div>
						<div class="row">
								<div class="col-sm-2 col-xs-2 col-md-2" <%if(Float.parseFloat(VSEL_BALANCE_QTY.elementAt(0).toString())>0){%> style="display:none;" <%} %>>  
									<div class="form-group row">
										<span><input type="checkbox" name="short_transfer" onclick="doShortTransfer()"/>
						    			<label class="form-label"><b>Short Transfer Only</b></label></span>
						  			</div>
								</div>
								<div class="col-sm-2 col-xs-2 col-md-2" <%if(!pool_flag.equals("E")){%> style="display:none;"<%}%>>  
									<div class="form-group row">
										<span><input type="checkbox" name="full_replacement" onclick="doFullReplacement()"/>
						    			<label class="form-label"><b>Full Replacement</b></label></span>
						  			</div>
								</div>
								<!-- For PSEUDO Transfer-->
								<div class="col-sm-2 col-xs-2 col-md-2" <%if(!pool_flag.equals("S")){%> style="display:none;"<%}%>>  
									<div class="form-group row">
										<span><input type="checkbox" name="pseudo_replacement" onclick="doPseudoReplacement()"/>
						    			<label class="form-label"><b>Pseudo Replacement</b></label></span>
						  			</div>
								</div>
								
						</div>
					<%}%>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div> 
					<div id="sel_dest_cargo" <%if(expire_count==VCUSTOMER_CD.size()){dest_cargo_flag = false;%>style="display:none;"<%}else{dest_cargo_flag = true;}%>>
						<div class="row">
		    				<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Destination Cargo</label>
						</div>
						<%int i,k,j=0;
						for(i=0; i<VCARGO_POOL_FLAG.size();i++){
							int index = Integer.parseInt(""+VINDEX.elementAt(i));
							String tbl_id = "tbl_"+VCARGO_POOL_FLAG.elementAt(i);
						%>
						<div class="accordion">
							<div class="accordion-item accor_item">
								<h2 class="accordion-header" id="heading">
	    							<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=i %>" aria-expanded="false" aria-controls="collapse<%=i %>">
									    		<%=VCARGO_POOL_NM.elementAt(i) %>
									</button>
								</h2>
								<div id="collapse<%=i %>" class="accordion-collapse collapse" aria-labelledby="heading1">
					    			<div class="accordion-body accor-body">
										<div class="row">
											<div class="table-responsive">
												<table class="table table-bordered table-hover" id="<%=tbl_id%>">
													<thead>
														<tr>
															<th></th>
															<%if(!VCARGO_POOL_FLAG.elementAt(i).equals("O")){%>
																<th>
																	Contract Type
																	<br>
																	<div align="center">
																		<input class="form-control form-control-sm" type="text" id="<%=VCARGO_POOL_FLAG.elementAt(i)%>_pur_cont_type" 
																		onkeyup="Search(this,'1','<%=tbl_id%>');" placeholder="Search.." style="width:100px"/>
																	</div>
																</th>
															<%} %>
															<th>
																<%if(VCARGO_POOL_FLAG.elementAt(i).equals("S")){%>
																	Pseudo Cargo#
																<%}else{%>
																	Purchase Contract#
																<%} %>
																<br>
																<div align="center">
																	<input class="form-control form-control-sm" type="text" id="<%=VCARGO_POOL_FLAG.elementAt(i)%>_pur_cont" 
																	onkeyup="Search(this,'2','<%=tbl_id%>');" placeholder="Search.." style="width:100px"/>
																</div>
															</th>

															<%if(!VCARGO_POOL_FLAG.elementAt(i).equals("S")&&!VCARGO_POOL_FLAG.elementAt(i).equals("O")){%>
																<th>
																	Contract/Cargo/Trade Ref#
																	<br>
																	<div align="center">
																		<input class="form-control form-control-sm" type="text" id="<%=VCARGO_POOL_FLAG.elementAt(i)%>_pur_cont_ref" 
																		onkeyup="Search(this,'3','<%=tbl_id%>');" placeholder="Search.." style="width:100px"/>
																	</div>
																</th>
																<th>
																	Counterparty
																	<br>
																	<div align="center">
																		<input class="form-control form-control-sm" type="text" id="<%=VCARGO_POOL_FLAG.elementAt(i)%>_pur_cont_cp" 
																		onkeyup="Search(this,'4','<%=tbl_id%>');" placeholder="Search.." style="width:100px"/>
																	</div>
																</th>
																<!-- <th>Contract Name</th> -->
																<th>Status</th>
																<th>Contract Period</th>
																<th>Allocation Start Date</th>
																<th>Last Allocation Date</th>
																<th>Price Type</th>
																<th>Currency/MMBTU</th>
																<th>Price</th>
															<%}%>
															<th>MMBTU Booked</th>
															<%if(!VCARGO_POOL_FLAG.elementAt(i).equals("S")){%>
																<%if(!VCARGO_POOL_FLAG.elementAt(i).equals("O")){%>
																<th>MMBTU Unloaded <%if(!VCARGO_POOL_FLAG.elementAt(i).equals("E")){ %><font color="#a0333a">(& Projected)</font><br><%} %></th>
																<%} %>
																<th><%if(VCARGO_POOL_FLAG.elementAt(i).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Avail for Sale </th>
															<%} %>
																<th><%if(VCARGO_POOL_FLAG.elementAt(i).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Allocated</th>
																<th><%if(VCARGO_POOL_FLAG.elementAt(i).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> Balance MMBTU</th>
															<%if(!VCARGO_POOL_FLAG.elementAt(i).equals("S")&&!VCARGO_POOL_FLAG.elementAt(i).equals("O")){%>
																<th>Remark</th>
															<%}%>
														</tr>
													</thead>
													<tbody>
														<%k=0;
														if(index > 0)
														{ %>
															<%for(j=j; j<VCOUNTERPARTY_CD.size(); j++)
															  {
																k+=1;
															%>
																<tr>
																	<td align="center"><input type="checkbox" class="form-check-input" name="chk" id="chk<%=j%>"></td>
																	<%if(!VCARGO_POOL_FLAG.elementAt(i).equals("O")){%>
																	<td align="center">
																		<span
																			<%if(VCONTRACT_TYPE.elementAt(j).equals("D")){ %>
														    					style="background: #66ffd9;"
														    				<%}else if(VCONTRACT_TYPE.elementAt(j).equals("N")){ %>
														    					style="background: skyblue;"
														    				<%}else if(VCONTRACT_TYPE.elementAt(j).equals("I")){ %>
														    					style="background: pink;"
														    				<%}else if(VCONTRACT_TYPE.elementAt(j).equals("T")){  %>	
													    						style="background: #E9CCEE;"
														    				<%}%>	
														    				
																		><b><%=VCONTRACT_TYPE_NM.elementAt(j)%></b></span>
																	</td>
																	<%} %>
																	<td align="center" title="REV : <%=VCONT_REV_NO.elementAt(j)%>">
																		<%=VPURCHASE_MAP_ID.elementAt(j)%>
																		<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=j%>" value="<%=VCOUNTERPARTY_CD.elementAt(j)%>">
																		<input type="hidden" name="agmt_no" id="agmt_no<%=j%>" value="<%=VAGMT_NO.elementAt(j)%>">
																		<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=j%>" value="<%=VAGMT_REV_NO.elementAt(j)%>">
																		<input type="hidden" name="cont_no" id="cont_no<%=j%>" value="<%=VCONT_NO.elementAt(j)%>">
																		<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=j%>" value="<%=VCONT_REV_NO.elementAt(j)%>">
																		<input type="hidden" name="contract_type" id="contract_type<%=j%>" value="<%=VCONTRACT_TYPE.elementAt(j)%>">
																		<input type="hidden" name="balance_qty" id="balance_qty<%=j%>" value="<%=VBALANCE_QTY.elementAt(j)%>">
																		<input type="hidden" name="cargo_no" id="cargo_no<%=j%>" value="<%=VCARGO_NO.elementAt(j)%>">
																		
																		<input type="hidden" name="cargo_status_flg" id="cargo_status_flg<%=j%>" value="<%=VCARGO_STATUS_FLG.elementAt(j)%>">
																		<input type="hidden" name="allocated_qty" id="allocated_qty<%=j%>" value="<%=VALLOCATED_QTY.elementAt(j)%>">
																	</td>

																	<%if(!VCARGO_POOL_FLAG.elementAt(i).equals("S")&&!VCARGO_POOL_FLAG.elementAt(i).equals("O")){ %>
																		<td><%=VCONT_REF.elementAt(j)%></td>
																		<td title="<%=VCOUNTERPARTY_CD.elementAt(j)%> : <%=VCOUNTERPARTY_ABBR.elementAt(j)%>">
																			<%=VCOUNTERPARTY_NM.elementAt(j)%>
																		</td>
																		<%-- <td><%=VCONT_NAME.elementAt(j)%></td> --%>
																		<td align="center"><%=VCONT_STATUS.elementAt(j)%></td>
																		<td align="center"><%=VSTART_DT.elementAt(j)%> - <%=VEND_DT.elementAt(j)%></td>
																		<td align="center"><%=VMIN_ALLOC_DT.elementAt(j) %></td>
																		<td align="center"><%=VMAX_ALLOC_DT.elementAt(j) %></td>
																		<td align="center"><%=VPRICE_TYPE.elementAt(j)%></td>
																		<td align="center"><%=VRATE_UNIT_NM.elementAt(j)%></td>
																		<td align="right"><%=VRATE.elementAt(j)%></td>
																	<%}%>
																	<td align="right"><%=VBOOKED_QTY.elementAt(j)%></td>
																	<%if(!VCARGO_POOL_FLAG.elementAt(i).equals("S")){ %>
																		<%if(!VCARGO_POOL_FLAG.elementAt(i).equals("O")){%>
																		<td align="right" <%if(!VCARGO_POOL_FLAG.elementAt(i).equals("E")){ %>title="<%=VUNLOADED_QTY_INFO.elementAt(j)%>"<%}%>><%=VUNLOADED_QTY.elementAt(j)%></td>
																		<%} %>
																		<td align="right" title="<%=VAVAIL_FOR_SALE_QTY_INFO.elementAt(j)%>"><%=VAVAIL_FOR_SALE_QTY.elementAt(j)%></td>
																	<%} %>
																	<td align="right"><%=VALLOCATED_QTY.elementAt(j)%></td>
																	<td align="right"><%=VBALANCE_QTY.elementAt(j)%></td>
																	<%if(!VCARGO_POOL_FLAG.elementAt(i).equals("S")&&!VCARGO_POOL_FLAG.elementAt(i).equals("O")){%>
																		<td><%=VREMARK.elementAt(j)%></td>
																	<%}%>
																</tr>
																<%
																if(k==index)
																{
																	j=j+1;
																	break;
																}%>
															<%} %>
														<%}else{%>
															<tr>
																<td colspan="20" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
															</tr>
														<%}%>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<%} %>
					</div>
					<!--/ For Selecting Destination Cargo  -->
				</div>
				<div class="card-footer cdfooter text-center" id="transfer_btn_div" <%if(expire_count==VCUSTOMER_CD.size()){%>style="display:none;"<%}%>>
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Proceed" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Transfer" disabled>
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
	<input type="hidden" name="CargoNo" value="<%=CargoNo%>">
	<input type="hidden" name="ContTyp" value="<%=ContTyp%>">
	<input type="hidden" name="ContRev" value="<%=ContRev%>">
	<input type="hidden" name="ContNo" value="<%=ContNo%>">
	<input type="hidden" name="AgmtRev" value="<%=AgmtRev%>">
	<input type="hidden" name="AgmtNo" value="<%=AgmtNo%>">
	<input type="hidden" name="Countpty" value="<%=Countpty%>">
	<input type="hidden" name="transfer_type" value="">
	<input type="hidden" name="pool_flag" value="<%=pool_flag%>">
 	<input type="hidden" name="dest_cargo_flag" value="<%=dest_cargo_flag %>">
 	
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
<script>
function Search(obj, indx, tbl_id) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById(tbl_id);
  	
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
function Search1(obj, indx, tbl_id) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById(tbl_id);
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length-1; i++) 
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