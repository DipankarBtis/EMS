<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var gas_dt = document.forms[0].gas_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_buy_daily_allocation.jsp?gas_dt="+gas_dt+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function nextDate(day_no)
{
	//var clearance = document.forms[0].clearance.value;
	
	var dt = document.forms[0].gas_dt.value;
	if(dt!="")
	{
	   	var split = dt.split("/");
		var d_dt = split[0];
		var m_dt = split[1];
		var y_dt = split[2];
		
		var dt1 = new Date(y_dt+"-"+m_dt+"-"+d_dt);
		if(day_no == "-1")
		{
			dt1.setDate(dt1.getDate()-1);
		}
		else
		{
			dt1.setDate(dt1.getDate()+1);
		}
		var day = dt1.getDate();
		if(parseInt(day) < 10)
		{
			day="0"+day;
		}
		var month = dt1.getMonth()+1;
		var year = dt1.getFullYear();
		if(parseInt(month) < 10)
		{
			month="0"+month;
		}
		var to_dt= day+"/"+month+"/"+year;
		
		document.forms[0].gas_dt.value=to_dt;
		
		//refresh(clearance);
		refresh();
	}
}

function checkAll(obj)
{
	var chk = document.forms[0].chk;
	var nom_block = document.forms[0].nom_block;
	
	if(chk!=null && chk.length!=undefined)
	{
		for(var i=0; i<chk.length; i++)
		{
			if(obj.checked)
			{
				if(nom_block[i].value != "Y")
				{
					chk[i].checked=true;
				}
			}
			else
			{
				chk[i].checked=false;
			}
			
			setEnableDisabled(chk[i],i);
			calculateSCM(i);
		}
	}
	else
	{
		if(obj.checked)
		{
			if(nom_block.value != "Y")
			{
				chk.checked=true;
			}
		}
		else
		{
			chk.checked=false;
		}
		
		setEnableDisabled(chk,"0");
		calculateSCM(i);
	}
}
function setEnableDisabled(obj,index)
{
	var counterparty_cd = document.getElementById("counterparty_cd"+index);
	var agmt_no = document.getElementById("agmt_no"+index);
	var agmt_rev_no = document.getElementById("agmt_rev_no"+index);
	var cont_no = document.getElementById("cont_no"+index);
	var cont_rev_no = document.getElementById("cont_rev_no"+index);
	var contract_type = document.getElementById("contract_type"+index);
	
	var gen_time = document.getElementById("gen_time"+index);
	var rd1 = document.getElementById("rd1"+index);
	var rd2 = document.getElementById("rd2"+index);
	var gcv = document.getElementById("gcv"+index);
	var ncv = document.getElementById("ncv"+index);
	var base = document.getElementById("base"+index);
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	var qty_scm = document.getElementById("qty_scm"+index);
	
	var plant_seq = document.getElementById("plant_seq"+index);
	var trans_cd = document.getElementById("trans_cd"+index);
	var trans_plant_seq = document.getElementById("trans_plant_seq"+index);
	var bu_plant_seq = document.getElementById("bu_plant_seq"+index);
	
	var cargo_no = document.getElementById("cargo_no"+index);
	
	if(obj.checked)
	{
		counterparty_cd.disabled=false;
		agmt_no.disabled=false;
		agmt_rev_no.disabled=false;
		cont_no.disabled=false;
		cont_rev_no.disabled=false;
		contract_type.disabled=false;
		gen_time.disabled=false;
		rd1.disabled=false;
		rd2.disabled=false;
		gcv.disabled=false;
		ncv.disabled=false;
		base.disabled=false;
		qty_mmbtu.disabled=false;
		qty_scm.disabled=false;
		
		plant_seq.disabled=false;
		trans_cd.disabled=false;
		trans_plant_seq.disabled=false;
		bu_plant_seq.disabled=false;
		cargo_no.disabled=false;
	}
	else
	{
		counterparty_cd.disabled=true;
		agmt_no.disabled=true;
		agmt_rev_no.disabled=true;
		cont_no.disabled=true;
		cont_rev_no.disabled=true;
		contract_type.disabled=true;
		gen_time.disabled=true;
		rd1.disabled=true;
		rd2.disabled=true;
		gcv.disabled=true;
		ncv.disabled=true;
		base.disabled=true;
		qty_mmbtu.disabled=true;
		qty_scm.disabled=true;
		
		plant_seq.disabled=true;
		trans_cd.disabled=true;
		trans_plant_seq.disabled=true;
		bu_plant_seq.disabled=true;
		cargo_no.disabled=true;
	}
}

function calculateSCM(index)
{
	var rd1 = document.getElementById("rd1"+index);
	var rd2 = document.getElementById("rd2"+index);
	var base = document.getElementById("base"+index);
	var gcv = document.getElementById("gcv"+index);
	var ncv = document.getElementById("ncv"+index);
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	var qty_scm = document.getElementById("qty_scm"+index);
	
	var baseVal = parseFloat("0");
	
	var deviding_factor = parseFloat("1");
	
	if(rd1.checked)
	{
		baseVal = parseFloat(gcv.value);;
		deviding_factor = parseFloat("1");
		base.value="GCV"
	}
	else if(rd2.checked)
	{
		baseVal = parseFloat(ncv.value);
		deviding_factor = parseFloat("1.11");
		base.value="NCV"
	}
	
	var multiplying_factor_2 = 0.252; //For Converting MMBTU TO MMSCM ...
	var multiplying_factor = 0.252*1000000; //For Converting MMBTU TO SCM ...
	
	if((qty_mmbtu.value!=null && trim(qty_mmbtu.value) !=''))
	{
		var scm = ""+round(((parseFloat(""+qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),0);
		if(isNaN(scm))
		{
			qty_scm.value="";
		}
		else
		{
			qty_scm.value = scm;
		}
	}
}

function doSubmit()
{
	var chk = document.forms[0].chk;
	var gas_dt = document.forms[0].gas_dt;
	var gen_dt = document.forms[0].gen_dt;
	var gen_time = document.forms[0].gen_time;
	//var rd1 = document.getElementById("rd1"+index);
	//var rd2 = document.getElementById("rd2"+index);
	var gcv = document.forms[0].gcv;
	var ncv = document.forms[0].ncv;
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	
	var msg="";
	var flag=true;
	var chk_count=parseInt("0");
	
	if(trim(gas_dt.value)=="")
	{
		msg+="Enter Gas Date!\n";
		flag=false;
	} 
	if(trim(gen_dt.value)=="")
	{
		msg+="Enter Gen Date!\n";
		flag=false;
	}
	
	
	if(gen_time!=null && gen_time.length!=undefined)
	{
		for(var i=0; i<gen_time.length; i++)
		{
			if(chk[i].checked)
			{
				chk_count++;
				if(trim(gen_time[i].value)=="")
				{
					msg+="Enter Gen Time for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(gcv[i].value)=="")
				{
					msg+="Enter GCV for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(ncv[i].value)=="")
				{
					msg+="Enter NCV for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(qty_mmbtu[i].value)=="")
				{
					msg+="Enter Energy(MMBTU) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(qty_scm[i].value)=="")
				{
					msg+="Enter Energy(SCM) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
			}
		}
	}
	else
	{
		if(chk.checked)
		{ 	chk_count++
			if(trim(gen_time.value)=="")
			{
				msg+="Enter Gen Time!\n";
				flag=false;
			}
			if(trim(gcv.value)=="")
			{
				msg+="Enter GCV!\n";
				flag=false;
			}
			if(trim(ncv.value)=="")
			{
				msg+="Enter NCV!\n";
				flag=false;
			}
			if(trim(qty_mmbtu.value)=="")
			{
				msg+="Enter Energy(MMBTU)!\n";
				flag=false;
			}
			if(trim(qty_scm.value)=="")
			{
				msg+="Enter Energy(SCM)!\n";
				flag=false;
			}
		}
	}
	
	if(parseInt(chk_count) == 0)
	{
		msg+="Please Select Atleast One ROW for Submit!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Submit Allocation Data?");
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

function checkQty(index)
{
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	var buyer_nom_qty = document.getElementById("buyer_nom_qty"+index);
	
	/*01/06/2023 - AS DISCUSSED WITH MAM - FOLOWING ALERT NOT REQUIRED FOR DAILY BUY ALLOCATION 
	if(parseFloat(qty_mmbtu.value) > parseFloat(buyer_nom_qty.value))
	{
		alert("Allocation "+parseFloat(qty_mmbtu.value)+" MMBTU can not exceed Seller Nomination "+parseFloat(buyer_nom_qty.value)+" MMBTU");
		qty_mmbtu.value = buyer_nom_qty.value;
	} */
}

function totalQty()
{
	var sub_index = document.forms[0].sub_index;
	
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	
	var tot_qty_mmbtu = document.forms[0].tot_qty_mmbtu;
	var tot_qty_scm = document.forms[0].tot_qty_scm;
	
	var tot_mmbtu=parseFloat("0");
	var tot_scm=parseFloat("0");
	
	var i=0;
	var m=0;
	
	if(sub_index!=null && sub_index!=undefined)
	{
		if(sub_index.length!=undefined)
		{
			for(var j=0; j<sub_index.length; j++)
			{
				var subIndex = parseInt(sub_index[j].value);
				
				tot_mmbtu=parseFloat("0");
				tot_scm=parseFloat("0");
				
				m=0;
				
				if(qty_mmbtu.length!=undefined)
				{
					for(i=i; i<qty_mmbtu.length; i++)
					{
						m=m+1;
						if(trim(qty_mmbtu[i].value) != "")
						{
							tot_mmbtu = tot_mmbtu + parseFloat(qty_mmbtu[i].value)
						}
						if(trim(qty_scm[i].value) != "")
						{
							tot_scm = tot_scm + parseFloat(qty_scm[i].value)
						}
						
						if(parseInt(m)==parseInt(subIndex))
						{
							i=parseInt(i)+1;
							break;
						}
					}
				}
				
				tot_qty_mmbtu[j].value=round(parseFloat(tot_mmbtu),2)
				tot_qty_scm[j].value =round(parseFloat(tot_scm),2)
			}
		}
		else
		{
			var subIndex = parseInt(sub_index.value);
			tot_mmbtu=parseFloat("0");
			tot_scm=parseFloat("0");
			m=0;
			if(qty_mmbtu.length!=undefined)
			{
				for(i=i; i<qty_mmbtu.length; i++)
				{
					m=m+1;
					if(trim(qty_mmbtu[i].value) != "")
					{
						tot_mmbtu = tot_mmbtu + parseFloat(qty_mmbtu[i].value)
					}
					if(trim(qty_scm[i].value) != "")
					{
						tot_scm = tot_scm + parseFloat(qty_scm[i].value)
					}
					
					if(parseInt(m)==parseInt(subIndex))
					{
						i=parseInt(i)+1;
						break;
					}
				}
			}
			else
			{
				if(trim(qty_mmbtu.value) != "")
				{
					tot_mmbtu = tot_mmbtu + parseFloat(qty_mmbtu.value)
				}
				if(trim(qty_scm.value) != "")
				{
					tot_scm = tot_scm + parseFloat(qty_scm.value)
				}
			}
			
			tot_qty_mmbtu.value=round(parseFloat(tot_mmbtu),2)
			tot_qty_scm.value =round(parseFloat(tot_scm),2)
		}
	}
}

function setNomCutoffTime(obj)
{
	if(obj.checked)
	{
		document.getElementById("cutoff_time_flag").value="Y";
	}
	else
	{
		document.getElementById("cutoff_time_flag").value="N";
	}
}

async function doPaste(index)
{
	var pastedText = event.clipboardData.getData('text/plain');
	var arr=pastedText.split(/\n/);
	
	var chk = document.forms[0].chk;
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	
	var j=parseInt("0");
	var pasteLen=arr.length;
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			window.setTimeout(function() 
			{	
				for(var i=0; i<chk.length; i++)
				{
					if(parseInt(i) >= parseInt(index))
					{
						if(parseInt(j) < parseInt(pasteLen))
						{
							if(chk[i].checked)
							{
								if(trim(arr[j])!= "")
								{							
									qty_mmbtu[i].value=arr[j];
									
									negNumber(qty_mmbtu[i]);
									checkNumber1(qty_mmbtu[i],9,2);
									checkQty(i);
									calculateSCM(i);
								}
								j++;
							}
						}
						else
						{
							break;
						}
					}
				}
			}, 50);
		}
		else
		{
			window.setTimeout(function() 
			{
				if(parseInt("0") >= parseInt(index))
				{
					if(parseInt(j) < parseInt(pasteLen))
					{
						if(chk.checked)
						{
							if(trim(arr[j])!= "")
							{
								qty_mmbtu.value=arr[j];
								
								negNumber(qty_mmbtu);
								checkNumber1(qty_mmbtu,9,2);
								checkQty("0");
								calculateSCM("0");
							}
							j++;
						}
					}
				}
			}, 50);
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_Buy_Nom_Alloc_Mgmt" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String temp_gas_dt=utildate.getDate(sysdate, "-1");
String gas_dt = request.getParameter("gas_dt")==null?temp_gas_dt:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "0");


purchase.setCallFlag("BUY_DAILY_ALLOC");
purchase.setComp_cd(owner_cd);
purchase.setGas_dt(gas_dt);
purchase.init();

Vector VCOUNTERPARTY_CD = purchase.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = purchase.getVCOUNTERPARTY_ABBR();

Vector VTRANSPORTER_CD = purchase.getVTRANSPORTER_CD();
Vector VTRANSPORTER_PLANT_SEQ = purchase.getVTRANSPORTER_PLANT_SEQ();
Vector VTRANSPORTER_PLANT_ABBR = purchase.getVTRANSPORTER_PLANT_ABBR();
Vector VCOUNTERPARTY_PLANT_SEQ = purchase.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = purchase.getVCOUNTERPARTY_PLANT_ABBR();
Vector VBU_CD = purchase.getVBU_CD();
Vector VBU_PLANT_SEQ = purchase.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = purchase.getVBU_PLANT_ABBR();

Vector VNOM_REV_NO = purchase.getVNOM_REV_NO();
Vector VGEN_TIME = purchase.getVGEN_TIME();
Vector VGEN_DT = purchase.getVGEN_DT();
Vector VBASE = purchase.getVBASE();
Vector VGCV = purchase.getVGCV();
Vector VNCV = purchase.getVNCV();
Vector VQTY_MMBTU = purchase.getVQTY_MMBTU();
Vector VQTY_SCM = purchase.getVQTY_SCM();
Vector VNOM_COLOR = purchase.getVNOM_COLOR();
Vector VDCQ = purchase.getVDCQ();
Vector VCONT_NAME = purchase.getVCONT_NAME();
Vector VCONT_REF = purchase.getVCONT_REF();
Vector VMDCQ_QTY = purchase.getVMDCQ_QTY();
Vector VINTERNAL_MAP_ID = purchase.getVINTERNAL_MAP_ID();
Vector VBUYER_NOM_REV_NO = purchase.getVBUYER_NOM_REV_NO();
Vector VBUYER_NOM = purchase.getVBUYER_NOM();

Vector VCONT_NO = purchase.getVCONT_NO();
Vector VCONT_REV_NO = purchase.getVCONT_REV_NO();
Vector VAGMT_NO = purchase.getVAGMT_NO();
Vector VAGMT_REV_NO = purchase.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = purchase.getVCONTRACT_TYPE();
Vector VDIS_CONT_MAPPING = purchase.getVDIS_CONT_MAPPING();
Vector VCARGO_NO = purchase.getVCARGO_NO();

Vector VTAX_DTL = purchase.getVTAX_DTL();
Vector VNOM_BLOCK = purchase.getVNOM_BLOCK();

Vector VCONTRACT_TYPE_NM = purchase.getVCONTRACT_TYPE_NM();

String trans_cd = "0"; //NOT IN USED 10/11/2022
String trans_plant_seq = "0"; //NOT IN USED 10/11/2022

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Buy_Nom_Alloc_Mgmt">

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
					    	Daily Allocation (Purchase)
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Gas Day<span class="s-red">*</span></b></label>
								</div>
			    				<div class="col">
				      				<div class="input-group input-group-sm" >
				      					<span class="input-group-text" onclick="nextDate('-1');" title="click for Back Date"><i class="fa fa-backward fa-lg"></i></span>
					      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="gas_dt" id="gas_dt" value="<%=gas_dt%>" maxLength="10" 
					      				onchange="validateDate(this);refresh();">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      					<span class="input-group-text" onclick="nextDate('1');" title="click for Next Date"><i class="fa fa-forward fa-lg"></i></span>
				      				</div>
				    			</div>
				    		</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Gen Day</b></label>
								</div>
			    				<div class="col">
				      				<div class="input-group input-group-sm" >
				      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="gen_dt" id="gen_dt" value="<%=gen_dt%>" maxLength="10" 
					      				onchange="validateDate(this);">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      				</div>
				    			</div>
				    		</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th><input class="form-check-input" type="checkbox" name="chkAll" onclick="checkAll(this);"></th>
										<th>
											Contract#<br>[Contract/Trade Ref#]
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="table_Contract" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/>
											</div>
										</th>
										<th>
											Contract Type 
										</th>
										<th>DCQ</th>
										<th>MDCQ</th>
										<th>
											Business Unit
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="table_Business Unit" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/>
											</div>
										</th>
										<th>
											Trader
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="table_Trader" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/>
											</div>
										</th>
										<th>
											Trader Plant
											<br>
											<div align="center">
												<input class="form-control form-control-sm" type="text" id="table_Trader Plant" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/>
											</div>
										</th>
										<th>Tax</th>
										<th>Rev#</th>
										<th>Seller Nomination (Rev)<br>(MMBTU)</th>
										<th>Energy (MMBTU)</th>
										<th>Energy (SCM)</th>
										<th>Gen Time</th>
										<th>Calorific Value Base<br>KCal/SCM</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPARTY_PLANT_SEQ.size()>0){ %>
									<%for(int i=0; i<VCOUNTERPARTY_PLANT_SEQ.size(); i++){%>
									<tr>
										<td align="center" valign="middle" <%if(VNOM_BLOCK.elementAt(i).equals("Y")) {%>style="background: #df9fbf;" title="Invoice Generated!"<%} %>>
											<input type="checkbox" class="form-check-input" name="chk" 
											onclick="setEnableDisabled(this,'<%=i%>');calculateSCM('<%=i%>');" 
											<%if(VNOM_BLOCK.elementAt(i).equals("Y")) {%>disabled style="pointer-events: none;"<%} %>>
											<input type="hidden" name="nom_block" id="nom_block<%=i%>" value="<%=VNOM_BLOCK.elementAt(i)%>" disabled>
										</td>
										<td>
											<%=VDIS_CONT_MAPPING.elementAt(i)%>
											<%if(!VCONT_REF.elementAt(i).equals("")){%>
												<br>(<%=VCONT_REF.elementAt(i)%>)
											<%} %>
										</td>
										<td align="center">
											<%=VCONTRACT_TYPE_NM.elementAt(i) %>
										</td>
										<td align="right">
											<%=VDCQ.elementAt(i)%>
											<input type="hidden" value="<%=VDCQ.elementAt(i)%>" name="dcq" id="dcq<%=i%>">
											<input type="hidden" value="<%=VMDCQ_QTY.elementAt(i)%>" name="mdcq_qty" id="mdcq_qty<%=i%>">
										</td>
										<td align="right">
											<%=VMDCQ_QTY.elementAt(i)%>
										</td>
										<td align="center">
											<%=VBU_PLANT_ABBR.elementAt(i)%>
											<input type="hidden" name="bu_plant_seq" id="bu_plant_seq<%=i%>" value="<%=VBU_PLANT_SEQ.elementAt(i)%>" disabled>
										</td>
										<td align="center">
											<%=VCOUNTERPARTY_ABBR.elementAt(i) %>
											<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>" disabled>
											<input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>" disabled>
   													<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=i%>" value="<%=VAGMT_REV_NO.elementAt(i)%>" disabled>
						      				<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>" disabled>
						      				<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=i%>" value="<%=VCONT_REV_NO.elementAt(i)%>" disabled>
						      				<input type="hidden" name="contract_type" id="contract_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled>
						      				<input type="hidden" name="trans_cd" id="trans_cd<%=i%>" value="<%=trans_cd%>" disabled>
						      				<input type="hidden" name="trans_plant_seq" id="trans_plant_seq<%=i%>" value="<%=trans_plant_seq%>" disabled>
						      				<input type="hidden" name="internal_map_id" id="internal_map_id<%=i%>" value="<%=VINTERNAL_MAP_ID.elementAt(i)%>">
											<input type="hidden" name="cargo_no" id="cargo_no<%=i%>" value="<%=VCARGO_NO.elementAt(i)%>" disabled>
										</td>
										<td align="center">
											<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(i)%>
											<input type="hidden" name="plant_seq" id="plant_seq<%=i%>" value="<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(i)%>" disabled>
										</td>
										<td><%=VTAX_DTL.elementAt(i) %></td>
										<td align="center"><%=VNOM_REV_NO.elementAt(i)%></td>
										<td align="right">
											<%=VBUYER_NOM.elementAt(i)%> (<%=VBUYER_NOM_REV_NO.elementAt(i)%>)
											<input type="hidden" name="buyer_nom_qty" id="buyer_nom_qty<%=i%>" value="<%=VBUYER_NOM.elementAt(i)%>">
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="qty_mmbtu" id="qty_mmbtu<%=i%>" value="<%=VQTY_MMBTU.elementAt(i)%>" 
												style="text-align:right;background:<%=VNOM_COLOR.elementAt(i)%>" 
												onpaste="doPaste('<%=i%>');" 
												onblur="negNumber(this);checkNumber1(this,9,2);checkQty('<%=i%>');calculateSCM('<%=i%>');" disabled>
												<input type="hidden" class="form-control form-control-sm" name="tmp_qty_mmbtu" id="tmp_qty_mmbtu<%=i%>" value="<%=VQTY_MMBTU.elementAt(i)%>" style="text-align:right" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="qty_scm" id="qty_scm<%=i%>" value="<%=VQTY_SCM.elementAt(i)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:75px;">
												<div class="row m-b-5">
													<div class="col">
									      				<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm" name="gen_time" id="gen_time<%=i%>" value="<%=VGEN_TIME.elementAt(i)%>" maxLength="5" 
								      						style="width:15px;background:<%=VNOM_COLOR.elementAt(i)%>"
								      						onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off" disabled>
								      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
							      						</div>
						      						</div>
						      					</div>
						      				</div>
										</td>
										<td align="center">
											<div style="width:300px;">
												<div class="row m-b-5">
													<div class="col">
														<input type="radio" name="rd<%=i%>" id="rd1<%=i%>" onclick="calculateSCM('<%=i%>');" <%if(VBASE.elementAt(i).equals("GCV")){ %>checked<%} %> disabled>&nbsp;GCV
								      				</div>
								      				<div class="col">
								      					<input type="text" class="form-control form-control-sm" name="gcv" id="gcv<%=i%>" value="<%=VGCV.elementAt(i)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(i)%>" onblur="checkNumber1(this,7,2);calculateSCM('<%=i%>');" disabled>
								      				</div>
								      				<div class="col">
								      					<input type="radio" name="rd<%=i%>" id="rd2<%=i%>" onclick="calculateSCM('<%=i%>');" <%if(VBASE.elementAt(i).equals("NCV")){ %>checked<%} %> disabled>&nbsp;NCV 
								      				</div>
								      				<div class="col">
									      				<input type="text" class="form-control form-control-sm" name="ncv" id="ncv<%=i%>" value="<%=VNCV.elementAt(i)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(i)%>" onblur="checkNumber1(this,7,2);calculateSCM('<%=i%>');" disabled>
						      						</div>
						      						<input type="hidden" name="base" id="base<%=i%>" value="<%=VBASE.elementAt(i)%>" disabled>
						      					</div>
						      				</div>
										</td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="15" align="center"><%=utilmsg.infoMessage("<b>Seller Nomination is not Done for the Selected Gas Day!</b>")%></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<%if(VCOUNTERPARTY_PLANT_SEQ.size()>0){ %>
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
				<%} %>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="DAILY_ALLOC">

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
    	//tbody=tr[i].getElementsByTagName("tbody");alert(tbody)
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