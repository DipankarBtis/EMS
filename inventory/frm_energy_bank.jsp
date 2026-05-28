<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function doAllocation()
{
	var chk = document.forms[0].chk;
	var counterparty_cd = document.forms[0].counterparty_cd;
	var agmt_no = document.forms[0].agmt_no;
	var agmt_rev_no = document.forms[0].agmt_rev_no;
	var cont_no = document.forms[0].cont_no;
	var cont_rev_no = document.forms[0].cont_rev_no;
	var contract_type = document.forms[0].contract_type;
	var balance_qty = document.forms[0].balance_qty;
	var cargo_no = document.forms[0].cargo_no;
	
	var cargo_status_flg = document.forms[0].cargo_status_flg;
	
	var chk_count = parseInt("0");
	
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
		else
		{
			if(chk.checked)
			{
				chk_count++;
				
				if(parseInt(balance_qty.value) <= 0)
				{
					msg="Select Cargo having Balance Qty > 0! ";
					flag=false;
				}
				else if(cargo_status_flg.value == "X" && contract_type.value == "N")
				{
					msg="Select Cargo having Confirmed Status! ";
					flag=false;
				}
				else
				{
					if(multiContNo == "")
					{
						multiCountpty+=counterparty_cd.value;
						multiAgmtNo+=agmt_no.value;
						multiAgmtRev+=agmt_rev_no.value;
						multiContNo+=cont_no.value;
						multiContRev+=cont_rev_no.value;
						multiContTyp+=contract_type.value;
						multiCargoNo+=cargo_no.value;
					}
					else
					{
						multiCountpty+="@@"+counterparty_cd.value;
						multiAgmtNo+="@@"+agmt_no.value;
						multiAgmtRev+="@@"+agmt_rev_no.value;
						multiContNo+="@@"+cont_no.value;
						multiContRev+="@@"+cont_rev_no.value;
						multiContTyp+="@@"+contract_type.value;
						multiCargoNo+="@@"+cargo_no.value;
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
		var u = document.forms[0].u.value;
		
		var url = "frm_allocation.jsp?multiCountpty="+multiCountpty+"&multiAgmtNo="+multiAgmtNo+"&multiAgmtRev="+multiAgmtRev+
				"&multiContNo="+multiContNo+"&multiContRev="+multiContRev+"&multiContTyp="+multiContTyp+"&multiCargoNo="+multiCargoNo+
				"&u="+u;	
		location.replace(url);
	}
}

function doPriceModification()
{
	var chk = document.forms[0].chk;
	var counterparty_cd = document.forms[0].counterparty_cd;
	var agmt_no = document.forms[0].agmt_no;
	var agmt_rev_no = document.forms[0].agmt_rev_no;
	var cont_no = document.forms[0].cont_no;
	var cont_rev_no = document.forms[0].cont_rev_no;
	var contract_type = document.forms[0].contract_type;
	var balance_qty = document.forms[0].balance_qty;
	var cargo_no = document.forms[0].cargo_no;
	var chk_count = parseInt("0");
	
	var msg="";
	var flag=true;
	
	var multiCountpty="";
	var multiAgmtNo="";
	var multiAgmtRev="";
	var multiContNo="";
	var multiContRev="";
	var multiContTyp="";
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					chk_count++;
					
					if(multiContNo == "")
					{
						multiCountpty+=counterparty_cd[i].value;
						multiAgmtNo+=agmt_no[i].value;
						multiAgmtRev+=agmt_rev_no[i].value;
						multiContNo+=cont_no[i].value;
						multiContRev+=cont_rev_no[i].value;
						multiContTyp+=contract_type[i].value;						
					}
					else
					{
						multiCountpty+="@"+counterparty_cd[i].value;
						multiAgmtNo+="@"+agmt_no[i].value;
						multiAgmtRev+="@"+agmt_rev_no[i].value;
						multiContNo+="@"+cont_no[i].value;
						multiContRev+="@"+cont_rev_no[i].value;
						multiContTyp+="@"+contract_type[i].value;						
					}
					if(counterparty_cd[i].value=='0')
					{
						if(cargo_no[i].value=='0')
						{
							msg+="Own Volume Account Price Modification is not allowed!\n";
						}
						else
						{
							msg+="Pseudo Cargo Price Modification is not allowed!\n";
						}
						flag=false;
						//break;
					}
					
				}
			}
		}
		else
		{
			if(chk.checked)
			{
				chk_count++;
				
				if(multiContNo == "")
				{
					multiCountpty+=counterparty_cd.value;
					multiAgmtNo+=agmt_no.value;
					multiAgmtRev+=agmt_rev_no.value;
					multiContNo+=cont_no.value;
					multiContRev+=cont_rev_no.value;
					multiContTyp+=contract_type.value;					
				}
				else
				{
					multiCountpty+="@"+counterparty_cd.value;
					multiAgmtNo+="@"+agmt_no.value;
					multiAgmtRev+="@"+agmt_rev_no.value;
					multiContNo+="@"+cont_no.value;
					multiContRev+="@"+cont_rev_no.value;
					multiContTyp+="@"+contract_type.value;					
				}
				if(counterparty_cd.value=='0')
				{
					if(counterparty_cd.value=='0'&&cargo_no.value=='0')
					{
						msg+="Own Volume Account Price Modification is not allowed!\n";
					}
					else
					{
						msg+="Pseudo Cargo Price Modification is not allowed!\n";
					}	
					flag=false;
				}
			}		
		}
	}
	
	if(parseInt(chk_count) == 0)
	{
		alert("Select atleast ONE(1) Purchase Contract# for Price Modification!");
	}
	else if(!flag)
	{
		alert(msg);
	}
	else
	{
		var u = document.forms[0].u.value;
		
		var url = "frm_trade_cont_price_modification.jsp?multiCountpty="+multiCountpty+"&multiAgmtNo="+multiAgmtNo+"&multiAgmtRev="+multiAgmtRev+
				"&multiContNo="+multiContNo+"&multiContRev="+multiContRev+"&multiContTyp="+multiContTyp+
				"&u="+u;	
		location.replace(url);
	}
}

//Below function is added for allocation transfer 
function doTransfer()
{
	var chk = document.forms[0].chk;
	var counterparty_cd = document.forms[0].counterparty_cd;
	var agmt_no = document.forms[0].agmt_no;
	var agmt_rev_no = document.forms[0].agmt_rev_no;
	var cont_no = document.forms[0].cont_no;
	var cont_rev_no = document.forms[0].cont_rev_no;
	var contract_type = document.forms[0].contract_type;
	var balance_qty = document.forms[0].balance_qty;
	var cargo_no = document.forms[0].cargo_no;
	var pool_flag = document.forms[0].pool_flag;
	var chk_count = parseInt("0");
	var Countpty="";
	var AgmtNo="";
	var AgmtRev="";
	var ContNo="";
	var ContRev="";
	var ContTyp="";
	var CargoNo="";
	var PoolFlag="";
	var flag = true;
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					chk_count++;
					Countpty = counterparty_cd[i].value;
					AgmtNo = agmt_no[i].value;
					AgmtRev = agmt_rev_no[i].value;
					ContNo = cont_no[i].value;
					ContRev = cont_rev_no[i].value;
					ContTyp = contract_type[i].value;
					CargoNo = cargo_no[i].value;
					PoolFlag = pool_flag[i].value;
					allocated_qty = document.getElementById("allocated_qty"+i);
					if(parseFloat(allocated_qty.value)==0)
					{
						flag=false;
						chk[i].checked = false;
					}
				}
			}
		}
	}
	
	if(chk_count>1 || chk_count==0)
	{
		flag=false;
		alert("Select only ONE(1) Purchase Contract# for Alloction Transfer!")
	}
	else
	{
		if(flag==false)
		{
			alert("Select Purchase Contract# having Allocated Qty > ZERO(0)!");
		}
	}
	if(flag==true)
	{
		var u = document.forms[0].u.value;
		var a = confirm("Are you sure you want to proceed for transfer?");
		if(a)
		{
			var url = "frm_allocation_transfer.jsp?Countpty="+Countpty+"&AgmtNo="+AgmtNo+"&AgmtRev="+AgmtRev+
					"&ContNo="+ContNo+"&ContRev="+ContRev+"&ContTyp="+ContTyp+"&CargoNo="+CargoNo+
					"&u="+u+"&pool_flag="+PoolFlag;	
			location.replace(url);
		}
	}
	
}

//Pratham bhatt 20250213 for pseudo mmbtu amodification 
function doPseudoModification()
{
	var chk = document.forms[0].chk;
	var counterparty_cd = document.forms[0].counterparty_cd;
	var agmt_no = document.forms[0].agmt_no;
	var agmt_rev_no = document.forms[0].agmt_rev_no;
	var cont_no = document.forms[0].cont_no;
	var cont_rev_no = document.forms[0].cont_rev_no;
	var contract_type = document.forms[0].contract_type;
	var contract_type_name = document.forms[0].contract_type_name;
	var balance_qty = document.forms[0].balance_qty;
	var cargo_no = document.forms[0].cargo_no;
	var pool_flag = document.forms[0].pool_flag;
	var booked_qty = document.forms[0].booked_qty;
	var balance_qty = document.forms[0].balance_qty;
	var allocated_qty = document.forms[0].allocated_qty;
	
	var chk_count = parseInt("0");
	var flag=false;
	var pseudo_cargo_no = "";
	var pseudo_booked_qty="";
	var pseudo_balance_qty="";
	var pseudo_alloc_qty="";
	var non_pseudo_count=parseInt("0");
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					if(pool_flag[i].value=="S")
					{
						chk_count++;
						pseudo_cargo_no=contract_type_name[i].value+"-"+cargo_no[i].value;
						pseudo_booked_qty = booked_qty[i].value;
						pseudo_balance_qty = balance_qty[i].value;
						pseudo_alloc_qty = allocated_qty[i].value;
						flag=true;
					}
					else
					{
						flag=false;
						non_pseudo_count++;
					}
				}
			}
		}
	}
	
	if(chk_count==0 || chk_count>1)
	{
		flag=false;
		alert("Select only ONE(1) Pseudo Cargo# for Modify Pseudo MMBTU!");
		return flag;
	}
	if(non_pseudo_count>0)
	{
		flag=false;
		alert("Select only Pseudo Cargo# for Modify Pseudo MMBTU");
		return flag;
	}
	if(flag==true)
	{
		document.getElementById("pseudo_hdr").innerHTML = "Modify Pseudo MMBTU ("+pseudo_cargo_no+")";
		document.getElementById("total_pseudo_mmbtu").value = pseudo_booked_qty;
		document.getElementById("bal_pseudo_mmbtu").value = pseudo_balance_qty;
		document.getElementById("alloc_pseudo_mmbtu").value = pseudo_alloc_qty;
		$('#pseudoMMBTU').modal('show');
	}
}

function doSubmitPseudoMMBTU()
{
	var pseudo_mmbtu = document.forms[0].pseudo_mmbtu;
	var balance_qty = document.forms[0].balance_qty;
	var contract_type = document.forms[0].contract_type;
	var contract_type_name = document.forms[0].contract_type_name;
	var cargo_no = document.forms[0].cargo_no;
	var chk = document.forms[0].chk;
	var pseudo_mmbtu_sign = document.forms[0].pseudo_mmbtu_sign;
	var bal_qty = "";
	var cont_type ="";
	var cont_type_nm ="";
	var pseudo_cargo="";
	var flag = true;
	
	if(pseudo_mmbtu.value == "")
	{
		flag=false;
		alert("Enter Modification MMBTU for selected Pseudo Cargo!");
		return flag;
	}
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					bal_qty=balance_qty[i].value;
					cont_type=contract_type[i].value;
					cont_type_nm=contract_type_name[i].value;
					pseudo_cargo=cargo_no[i].value;
				}
			}
		}
	}
	if(pseudo_mmbtu.value>bal_qty && pseudo_mmbtu_sign.value=='-')
	{
		flag=false;
		alert("MMBTU Reduction is not allowed for Qty > Balance MMBTU "+bal_qty+"!");
		return flag;
	}
	if(parseFloat(pseudo_mmbtu.value)==0)
	{
		flag=false;
		alert("ZERO(0) is not a valid input!");
		return flag;
	}
	
	if(flag==true)
	{
		var a = confirm("Do you want to modify Pseudo MMBTU?");
		if(a)
		{
			document.forms[0].pseudo_cargo_no.value = pseudo_cargo;
			document.forms[0].pseudo_contract_type.value = cont_type;
			document.forms[0].pseudo_contract_type_name.value = cont_type_nm;
			document.forms[0].option.value = "PSEUDO_MMBTU_MODIFICATION";
			document.forms[0].method = "post";
			document.forms[0].action = "../servlet/Frm_EnergyBank";
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
}

function doClear()	//PB20250214: for clearing the previous value of the modal
{
	document.forms[0].pseudo_mmbtu.value="";
}

function refresh()
{
	var mmbtu_sign = document.forms[0].mmbtu_sign.value;
	var u = document.forms[0].u.value;
	var url = "frm_energy_bank.jsp?&u="+u+"&mmbtu_sign="+mmbtu_sign;

document.getElementById("loading").style.visibility = "visible";
location.replace(url);
}

function exportToXls()
{
	var sysdate = document.forms[0].sysdate.value;
	var comp_abbr = document.forms[0].comp_abbr.value;
	sysdate = sysdate.toString();
	sysdate = sysdate.split('/').join('');
	var mmbtu_sign = document.forms[0].mmbtu_sign.value;
	var u = document.forms[0].u.value;
	
	var url = "xls_energy_bank.jsp?fileName="+comp_abbr+"-Energy Bank Report "+sysdate+".xls&mmbtu_sign="+mmbtu_sign;

	location.replace(url);
}

var newWindow;

function viewProjectedLTCORADetail()
{
	var u = document.forms[0].u.value;
	var url = "rpt_own_projected_mmbtu.jsp?u="+u;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Own Volume Projected MMBTU","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Own Volume Projected MMBTU","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_EnergyBank" id="energyBank" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String mmbtu_sign = request.getParameter("mmbtu_sign")==null?">":request.getParameter("mmbtu_sign");

energyBank.setCallFlag("ENERGY_BANK");
energyBank.setComp_cd(owner_cd);
energyBank.setBalanceMMBTURange(mmbtu_sign);
energyBank.init();

String comp_abbr = energyBank.getComp_Abbr();

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
Vector VUNLOADED_QTY_INFO = energyBank.getVUNLOADED_QTY_INFO();
Vector VAVAIL_FOR_SALE_QTY = energyBank.getVAVAIL_FOR_SALE_QTY();
Vector VAVAIL_FOR_SALE_QTY_INFO = energyBank.getVAVAIL_FOR_SALE_QTY_INFO();
Vector VALLOCATED_QTY = energyBank.getVALLOCATED_QTY();
Vector VBALANCE_QTY = energyBank.getVBALANCE_QTY();
Vector VBALANCE_QTY_INFO = energyBank.getVBALANCE_QTY_INFO();
Vector VCONT_REF = energyBank.getVCONT_REF();
Vector VREMARK = energyBank.getVREMARK();

Vector VCARGO_NO = energyBank.getVCARGO_NO();
Vector VPURCHASE_MAP_ID = energyBank.getVPURCHASE_MAP_ID();
Vector VCARGO_STATUS_FLG = energyBank.getVCARGO_STATUS_FLG();

Vector VTOTAL_BALANCE_MMBTU = energyBank.getVTOTAL_BALANCE_MMBTU();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form>
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
					    	Energy Bank
					    </div>
					     <div align="right" onclick="exportToXls();" style="color:green;">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-5 col-xs-5 col-md-5">
<!-- 						<div class="col-sm-12 col-xs-12 col-md-12">   -->
							<div class="form-group row">
								<div class="col-auto">
				    				<input type="button" class="btn btn-sm request_btn" value="Volume Allocation" onclick="doAllocation();">
				    			</div>
				    			<div class="col-auto" style="display:none;">
				    				<input type="button" class="btn btn-sm request_btn" value="Reconcile Allocation">
				    			</div>
				    			<div class="col-auto">
<!-- 				    				<input type="button" class="btn btn-sm request_btn" value="Allocation Transfer"> --> 
				    				<input type="button" class="btn btn-sm request_btn" value="Allocation Transfer" onclick="doTransfer();"> <!-- pb20241227 -->
				    			</div>
				    			<div class="col-auto">
				    				<input type="button" class="btn btn-sm request_btn" value="Price Modification" onclick="doPriceModification();">
				    			</div>
				    			<div class="col-auto">
				    				<input type="button" class="btn btn-sm request_btn" value="Modify Pseudo Volume" onclick="doPseudoModification();">
				    			</div>
				    			<div class="col-auto" style="display:none;">
				    				<input type="button" class="btn btn-sm request_btn" value="Expected Contract Cancellation">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-5 col-xs-5 col-md-5">
							<div class="form-group row">
								<div class="col-auto">
								</div>
							</div>
						</div>
						<div class="col-auto">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Balance Volume</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="mmbtu_sign" onchange="refresh()">
										<option value="0">--All--</option>
										<option value=">">>0</option>
										<option value="<"><0</option>
										<option value="=">=0</option>
									</select>
									<script>document.forms[0].mmbtu_sign.value="<%=mmbtu_sign%>"</script>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%int i=0;int k=0, ctn=0;
						for(int j=0; j<VCARGO_POOL_FLAG.size(); j++){ 
						int index = Integer.parseInt(""+VINDEX.elementAt(j));
						String tbl_id = "tbl_"+VCARGO_POOL_FLAG.elementAt(j);
					%>
						<div class="accordion">
							<div class="accordion-item accor_item">
								<h2 class="accordion-header" id="heading">
	    							<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=j %>" aria-expanded="false" aria-controls="collapse<%=j %>">
											 <%=VCARGO_POOL_NM.elementAt(j) %>&nbsp;<font color="blue">(Balance MMBTU: <%=VTOTAL_BALANCE_MMBTU.elementAt(ctn++) %>)</font>
									</button>
								</h2>
								<div id="collapse<%=j%>" class="accordion-collapse collapse" aria-labelledby="heading1">
									<div class="accordion-body accor-body">
										<div class="row">
											<div class="table-responsive">
												<table class="table table-bordered table-hover" id="<%=tbl_id%>">
													<thead>
														<tr>
															<th></th>
															<%if(!VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>
																<th>
																	Contract Type
																	<br>
																	<div align="center">
																		<input class="form-control form-control-sm" type="text" id="<%=VCARGO_POOL_FLAG.elementAt(j)%>_pur_cont_type" 
																		onkeyup="Search(this,'1','<%=tbl_id%>');" placeholder="Search.." style="width:100px"/>
																	</div>
																</th>
															<%}%>
															<%if((!VCARGO_POOL_FLAG.elementAt(j).equals("S"))&&(!VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
																<th>
																	Counterparty
																	<br>
																	<div align="center">
																		<input class="form-control form-control-sm" type="text" id="<%=VCARGO_POOL_FLAG.elementAt(j)%>_pur_cont_cp" 
																		onkeyup="Search(this,'2','<%=tbl_id%>');" placeholder="Search.." style="width:100px"/>
																	</div>
																</th>
																<th>
																	Contract/Cargo/Trade Ref#
																	<br>
																	<div align="center">
																		<input class="form-control form-control-sm" type="text" id="<%=VCARGO_POOL_FLAG.elementAt(j)%>_pur_cont_ref" 
																		onkeyup="Search(this,'3','<%=tbl_id%>');" placeholder="Search.." style="width:100px"/>
																	</div>
																</th>
																<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> Balance MMBTU</th>
																<th>MMBTU Unloaded <%if(!VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">(& Projected)</font><br><%} %></th>
																<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Avail for Sale </th>
																<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Allocated</th>
																<th>MMBTU Booked</th>
															<%} %>
															<th>
																<%if(VCARGO_POOL_FLAG.elementAt(j).equals("S")){%>
																	Pseudo Cargo#
																<%}else{%>
																	Purchase Contract#
																<%} %>
																<br>
																<div align="center">
																	<input class="form-control form-control-sm" type="text" id="<%=VCARGO_POOL_FLAG.elementAt(j)%>_pur_cont" 
																	onkeyup="Search(this,'9','<%=tbl_id%>');" placeholder="Search.." style="width:100px"/>
																</div>
															</th>
															<%if((!VCARGO_POOL_FLAG.elementAt(j).equals("S"))&&(!VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
<!-- 																<th>Contract Name</th> -->
																<th>Status</th>
																<th>Contract Period</th>
																<th>Allocation Start Date</th>
																<th>Last Allocation Date</th>
																<th>Price Type</th>
																<th>Currency/MMBTU</th>
																<th>Price</th>
															<%} %>
															<%if((VCARGO_POOL_FLAG.elementAt(j).equals("S"))||(VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
																<%if(VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>
																	<th>Projected MMBTU</th>
																<%}else{%>
																	<th>MMBTU Booked</th>
																<%}%>
															<%} %>
															<%if(VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>
																<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Avail for Sale </th>
															<%}%>
															<%if((VCARGO_POOL_FLAG.elementAt(j).equals("S"))||(VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
																<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> MMBTU Allocated</th>
																<th><%if(VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %><font color="#a0333a">Expected</font><br><%} %> Balance MMBTU</th>
															<%} %>
															<%if((!VCARGO_POOL_FLAG.elementAt(j).equals("S"))&&(!VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%><th>Remark</th><%}%>
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
																	<td align="center"><input type="checkbox" class="form-check-input" name="chk" id="chk<%=i%>"></td>
																	<%-- <td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>"><%=VCONT_NO.elementAt(i)%></td> --%>
																		<%if(!VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>
																		<td align="center">
																			<span
																				<%if(VCONTRACT_TYPE.elementAt(i).equals("D")){ %>
															    					style="background: #66ffd9;"
															    				<%}else if(VCONTRACT_TYPE.elementAt(i).equals("N")){ %>
															    					style="background: skyblue;"
															    				<%}else if(VCONTRACT_TYPE.elementAt(i).equals("I")){ %>
															    					style="background: pink;"
															    				<%}else if(VCONTRACT_TYPE.elementAt(i).equals("T")){  %>	
															    					style="background: #E9CCEE;"
															    				<%} %>
																			><b><%=VCONTRACT_TYPE_NM.elementAt(i)%></b></span>
																		</td>
																	<%} %>
																	<%if((!VCARGO_POOL_FLAG.elementAt(j).equals("S"))&&(!VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
																		<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%> : <%=VCOUNTERPARTY_ABBR.elementAt(i)%>">
																			<%=VCOUNTERPARTY_NM.elementAt(i)%>
																		</td>
																		<td><%=VCONT_REF.elementAt(i)%></td>
																		<td align="right" title="<%=VBALANCE_QTY_INFO.elementAt(i) %>" <%if(Double.parseDouble(VBALANCE_QTY.elementAt(i).toString())<0){%> style="color:red;font-weight: bold;"<%}%>>
																			<%=VBALANCE_QTY.elementAt(i)%>
																		</td>
																		<td align="right" <%if(!VCARGO_POOL_FLAG.elementAt(j).equals("E")){ %>title="<%=VUNLOADED_QTY_INFO.elementAt(i)%>"<%}%>><%=VUNLOADED_QTY.elementAt(i)%></td>
																		<td align="right" title="<%=VAVAIL_FOR_SALE_QTY_INFO.elementAt(i)%>"><%=VAVAIL_FOR_SALE_QTY.elementAt(i)%></td>
																		<td align="right"><%=VALLOCATED_QTY.elementAt(i)%></td>
																		<td align="right"><%=VBOOKED_QTY.elementAt(i)%> <%if(VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>&nbsp;&nbsp;<span title="LTCORA changes after <%=sysdate%>" onclick="viewProjectedLTCORADetail();"><i class='fa fa-info-circle fa-lg'></i></span><%} %></td>
																	<%} %>
																	<td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>">
																		<%=VPURCHASE_MAP_ID.elementAt(i)%>
																		<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>">
																		<input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>">
																		<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=i%>" value="<%=VAGMT_REV_NO.elementAt(i)%>">
																		<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>">
																		<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=i%>" value="<%=VCONT_REV_NO.elementAt(i)%>">
																		<input type="hidden" name="contract_type" id="contract_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>">
																		<input type="hidden" name="balance_qty" id="balance_qty<%=i%>" value="<%=VBALANCE_QTY.elementAt(i)%>">
																		<input type="hidden" name="cargo_no" id="cargo_no<%=i%>" value="<%=VCARGO_NO.elementAt(i)%>">
																		<input type="hidden" name="contract_type_name" id="contract_type_name" value="<%=VCONTRACT_TYPE_NM.elementAt(i)%>">
																		<input type="hidden" name="booked_qty" id="booked_qty" value="<%=VBOOKED_QTY.elementAt(i)%>">
																		
																		<input type="hidden" name="cargo_status_flg" id="cargo_status_flg<%=i%>" value="<%=VCARGO_STATUS_FLG.elementAt(i)%>">
																		<input type="hidden" name="allocated_qty" id="allocated_qty<%=i%>" value="<%=VALLOCATED_QTY.elementAt(i)%>">
																		<input type="hidden" name="pool_flag" id="pool_flag<%=i%>" value="<%=VCARGO_POOL_FLAG.elementAt(j)%>">
																	</td>
																	<%if((!VCARGO_POOL_FLAG.elementAt(j).equals("S"))&&(!VCARGO_POOL_FLAG.elementAt(j).equals("O"))){ %>
																		<%--<td><%=VCONT_NAME.elementAt(i)%></td> --%>
																		<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
																		<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
																		<td align="center"><%=VMIN_ALLOC_DT.elementAt(i) %></td>
																		<td align="center"><%=VMAX_ALLOC_DT.elementAt(i) %></td>
																		<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
																		<td align="center"><%=VRATE_UNIT_NM.elementAt(i)%></td>
																		<td align="right"><%=VRATE.elementAt(i)%></td>
																	<% }%>
																	
																	<%if((VCARGO_POOL_FLAG.elementAt(j).equals("S"))||(VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
																		<td align="right"><%=VBOOKED_QTY.elementAt(i)%> <%if(VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>&nbsp;&nbsp;<span title="LTCORA changes after <%=sysdate%>" onclick="viewProjectedLTCORADetail();"><i class='fa fa-info-circle fa-lg'></i></span><%} %></td>
																	<%} %>																
																	<%if(VCARGO_POOL_FLAG.elementAt(j).equals("O")){%>
																		<td align="right" title="<%=VAVAIL_FOR_SALE_QTY_INFO.elementAt(i)%>"><%=VAVAIL_FOR_SALE_QTY.elementAt(i)%></td>
																	<%}%>
																	<%if((VCARGO_POOL_FLAG.elementAt(j).equals("S"))||(VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%>
																		<td align="right"><%=VALLOCATED_QTY.elementAt(i)%></td>
																		<td align="right" title="<%=VBALANCE_QTY_INFO.elementAt(i) %>" <%if(Double.parseDouble(VBALANCE_QTY.elementAt(i).toString())<0){%> style="color:red;font-weight: bold;"<%}%>>
																			<%=VBALANCE_QTY.elementAt(i)%>
																		</td>
																	<%} %>
																	<%if((!VCARGO_POOL_FLAG.elementAt(j).equals("S"))&&(!VCARGO_POOL_FLAG.elementAt(j).equals("O"))){%><td><%=VREMARK.elementAt(i)%></td><%}%>
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
																<td colspan="20" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
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
					<%}%>
				</div>
				<div class="card-footer cdfooter text-center">
				</div>
			</div>
		</div>
	</div>
	<!-- Modal for Pseudo cargo  -->
	<div class="modal fade" id="pseudoMMBTU" data-bs-backdrop="static" data-bs-keyboard="false">
		<div class="modal-dialog modal-dialog-scrollable modal-lg">
	    	<div class="modal-content">
	    		<div class="modal-header cdheader">
	        		<div class="topheader" id="pseudo_hdr">
					</div>
					<input type="button" class="btn-close" data-bs-dismiss="modal" onclick="doClear();">
	      		</div>
	      		<div class="modal-body mdbody">
	      			<div class="cdbody">
						<div class="row m-b-10">
							<div class="col-sm-2 col-xs-2 col-md-2">
								<label class="form-label"><b>Total MMBTU</b></label> 
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">
								<div class="form-group row">
									<div class="col-sm-12 col-xs-12 col-md-12">
						    			<input type="text" class="form-control form-control-sm" id="total_pseudo_mmbtu" readOnly>
						    		</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">
								<label class="form-label"><b>Allocated MMBTU</b></label>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">
								<div class="form-group row">
									<div class="col-sm-12 col-xs-12 col-md-12">
										<input type="text" class="form-control form-control-sm" id="alloc_pseudo_mmbtu" readOnly>
						    		</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">
								<label class="form-label"><b>Balance MMBTU</b></label>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">
								<div class="form-group row">
									<div class="col-sm-12 col-xs-12 col-md-12">
										<input type="text" class="form-control form-control-sm" id="bal_pseudo_mmbtu" readOnly>
						    		</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-4 col-xs-4 col-md-4">
								<label class="form-label"><b>Modification MMBTU<span class="s-red">*</span></b></label>
							</div>
							<div class="col-sm-8 col-xs-8 col-md-8">
								<div class="form-group row">
									<div class="col-auto">
			      						<select class="form-select form-select-sm" name="pseudo_mmbtu_sign">
			      							<option value="+">+</option>
			      							<option value="-">-</option>
			      						</select>
			      					</div>
									<div class="col-sm-10 col-xs-10 col-md-10">
						    			<input type="text" class="form-control form-control-sm" name="pseudo_mmbtu" maxlength="10" onkeyup="checkNumber1(this,10,2)">
						    		</div>
					  			</div>
							</div>
						</div>
	      			</div>
	      		</div>
	      		<div class="modal-footer cdfooter">
	      			<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="doClear();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitPseudoMMBTU()">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
	      		</div>
	      	</div>
		</div>
	</div>
	<!--/modal for Pseudo Cargo  -->
</div>
<input type="hidden" name="comp_abbr" value="<%=comp_abbr%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
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

<input type="hidden" name="option" value="">
<input type="hidden" name="pseudo_contract_type" value="">
<input type="hidden" name="pseudo_contract_type_name" value="">
<input type="hidden" name="pseudo_cargo_no" value="">

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
</script>
</html>