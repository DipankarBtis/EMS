<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh(clearance)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var prev_counterparty_cd = document.forms[0].prev_counterparty_cd.value;
	var prev_clearance = document.forms[0].prev_clearance.value;
	
	var contract_type = document.forms[0].contract_type.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	var cargo_no = document.forms[0].cargo_no.value;
	
	var from_dt = document.forms[0].from_dt;
	var to_dt = document.forms[0].to_dt;
	
	if(from_dt!=undefined && from_dt!=null)
	{
		from_dt = document.forms[0].from_dt.value;
		to_dt = document.forms[0].to_dt.value;
	}
	else
	{
		from_dt="";
		to_dt="";
	}
	
	if(prev_clearance != clearance)
	{
		counterparty_cd="0";
		agmt_no="";
		agmt_rev_no="";
		cont_no="";
		cont_rev_no="";
		
		from_dt="";
		to_dt="";
		contract_type="";
	}
	else if(prev_counterparty_cd != counterparty_cd)
	{
		agmt_no="";
		agmt_rev_no="";
		cont_no="";
		cont_rev_no="";
		
		from_dt="";
		to_dt="";
		contract_type="";
	}
	
	var hid_from_dt=document.forms[0].hid_from_dt.value;
	var hid_to_dt = document.forms[0].hid_to_dt.value;
	var cont_start_dt = document.forms[0].cont_start_dt.value;
	var cont_end_dt = document.forms[0].cont_end_dt.value;
	
	var flag = true;
	
	if(trim(from_dt)!="" && trim(to_dt)!="")
   	{
		var compareDt =  compareDate(from_dt,to_dt);
		if(compareDt=="1")
		{
			if(value=="1")
			{
				alert("From Date should be less or equal To Date!!")
				document.forms[0].from_dt.value = hid_from_dt;
				flag = false
			}
			else
			{
				alert("To Date should be grater or equal From Date!!")
				document.forms[0].to_dt.value=hid_to_dt;
				flag = false
			}
		}
		else
		{
			var val1 = compareDate(from_dt,cont_start_dt);
		 	var val2 = compareDate(from_dt,cont_end_dt);
		 	
		 	if(val1=="2")
		 	{
		   		alert("From date must be in the range of Contract Start and End date..\nContract Period ("+cont_start_dt+" - "+cont_end_dt+")..\n");
		   		document.forms[0].from_dt.value = hid_from_dt;
		   		flag = false
		 	}
		 	
		 	val1 = compareDate(to_dt,cont_end_dt);
		 	if(val1=="1")
		 	{
		   		alert("To date must be less or equal to Contract Start and End date..\nContract Period ("+cont_start_dt+" - "+cont_end_dt+")..\n");
		   		document.forms[0].to_dt.value=hid_to_dt;
		   		flag = false
		 	}
		}
   	}
	else
	{
		if(trim(cont_no)!="")
		{
			alert("Please Select From and To Date!!");
			flag = false
		}
	}
	
	var hid_from_dt=document.forms[0].hid_from_dt.value;
	var hid_to_dt = document.forms[0].hid_to_dt.value;
	var cont_start_dt = document.forms[0].cont_start_dt.value;
	var cont_end_dt = document.forms[0].cont_end_dt.value;
		
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "frm_buy_periodic_allocation.jsp?counterparty_cd="+counterparty_cd+
				"&clearance="+clearance+"&contract_type="+contract_type+"&from_date="+from_dt+"&to_date="+to_dt+
				"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&cargo_no="+cargo_no+
				"&u="+u;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}

var newWindow;
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var clearance = document.forms[0].clearance.value;
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_trader_contract_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&clearance="+clearance+"&fcc_flag=N","Trader Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_trader_contract_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&clearance="+clearance+"&fcc_flag=N","Trader Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,contract_type,cargo_no)
{
	var clearance = document.forms[0].clearance.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_buy_periodic_allocation.jsp?counterparty_cd="+countpty_cd+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
			"&u="+u+"&clearance="+clearance+"&contract_type="+contract_type+"&cargo_no="+cargo_no;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function checkAll(obj)
{
	var chk = document.forms[0].chk;
	var nom_block = document.forms[0].nom_block;
	var cp_status = document.forms[0].cp_status;
	if(chk!=null && chk.length!=undefined)
	{
		for(var i=0; i<chk.length; i++)
		{
			if(obj.checked)
			{
				if(nom_block[i].value != "Y" && cp_status[i].value != "N")
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
			if(nom_block.value != "Y" && cp_status.value != "N")
			{
				chk.checked=true;
			}
		}
		else
		{
			chk.checked=false;
		}
		
		setEnableDisabled(chk,"0");
		calculateSCM("0");
	}
}


function setEnableDisabled(obj,index)
{
	//var counterparty_cd = document.getElementById("counterparty_cd"+index);
	//var agmt_no = document.getElementById("agmt_no"+index);
	//var agmt_rev_no = document.getElementById("agmt_rev_no"+index);
	//var cont_no = document.getElementById("cont_no"+index);
	//var cont_rev_no = document.getElementById("cont_rev_no"+index);
	//var contract_type = document.getElementById("contract_type"+index);
	
	var gas_dt = document.getElementById("gas_dt"+index);
	var gen_dt = document.getElementById("gen_dt"+index);
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
	
	var i_index = document.getElementById("index_"+index);
	
	var moleConfigBtn = document.getElementById("moleConfigBtn"+index);
	var mole_chk = document.getElementsByName("mole_chk_"+index).length;
	
	if(obj.checked)
	{
		//counterparty_cd.disabled=false;
		//agmt_no.disabled=false;
		//agmt_rev_no.disabled=false;
		//cont_no.disabled=false;
		//cont_rev_no.disabled=false;
		//contract_type.disabled=false;
		
		gas_dt.disabled=false;
		gen_dt.disabled=false;
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
		
		i_index.disabled=false;
		
		//moleConfigBtn.style.display="";
		moleConfigBtn.disabled = false;
		
		if(parseInt(mole_chk) > 0)
		{
			for(var i=0;i<parseInt(mole_chk);i++)
			{
				var mole_chk_obj = document.getElementById("mole_chk_"+i+"_"+index);
				mole_chk_obj.disabled=false;
				setEnableDisableMolecule(mole_chk_obj,i,index)
			}
		}
	}
	else
	{
		//counterparty_cd.disabled=true;
		//agmt_no.disabled=true;
		//agmt_rev_no.disabled=true;
		//cont_no.disabled=true;
		//cont_rev_no.disabled=true;
		//contract_type.disabled=true;
		
		gas_dt.disabled=true;
		gen_dt.disabled=true;
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
		
		i_index.disabled=true;
		
		//moleConfigBtn.style.display="none";
		moleConfigBtn.disabled = true;
		
		if(parseInt(mole_chk) > 0)
		{
			for(var i=0;i<parseInt(mole_chk);i++)
			{
				var mole_chk_obj = document.getElementById("mole_chk_"+i+"_"+index);
				mole_chk_obj.disabled=true;
				setEnableDisableMolecule(mole_chk_obj,i,index)
			}
		}
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
	
	//FOR MOLECULE
	var mole_chk = document.getElementsByName("mole_chk_"+index).length;
	for(var i=0;i<parseInt(mole_chk);i++)
	{
		var mole_chk_obj = document.getElementById("mole_chk_"+i+"_"+index);
		
		if(mole_chk_obj.checked)
		{
			var mole_qty_mmbtu = document.getElementById("mole_qty_mmbtu_"+i+"_"+index);
			var mole_qty_scm = document.getElementById("mole_qty_scm_"+i+"_"+index);
			
			if((mole_qty_mmbtu.value!=null && trim(mole_qty_mmbtu.value) !=''))
			{
				var scm = parseFloat("0");
				if (parseFloat(baseVal) != 0)
				{ 
					scm = ""+round(((parseFloat(""+mole_qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),0);
				}
				
				if(isNaN(scm))
				{
					//mole_qty_scm_.value="";
					mole_qty_scm.value="0";
				}
				else
				{
					mole_qty_scm.value = scm;
				}
			}
			
		}
	}
}
enableButton = true;
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
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	
	var msg="";
	var flag=true;
	var chk_count=parseInt("0");
	
	if(trim(counterparty_cd)=="" || trim(counterparty_cd)=="0")
	{
		msg+="Select Counterparty!\n";
		flag=false;
	}
	if(trim(agmt_no)=="")
	{
		msg+="Agreement# is Missing!\n";
		flag=false;
	}
	if(trim(cont_no)=="")
	{
		msg+="Contract# is Missing!\n";
		flag=false;
	}
	if(trim(contract_type)=="" || trim(contract_type)=="0")
	{
		msg+="Contract Type is Missing!\n";
		flag=false;
	}
	if(gen_time!=null && gen_time.length!=undefined)
	{
		for(var i=0; i<gen_time.length; i++)
		{
			if(chk[i].checked)
			{
				chk_count++;
				if(trim(gas_dt[i].value)=="")
				{
					msg+="Select Gas Date ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				} 
				if(trim(gen_dt[i].value)=="")
				{
					msg+="Select Gen Date ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
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
				
				////////////////MOLECULE/////////////////////////////////////
				var mole_chk = document.getElementsByName("mole_chk_"+i).length;
				
				var tot_mole_qty_mmbtu = document.getElementById("tot_mole_qty_mmbtu_"+i)
				
				var mole_count=parseInt("0");
				for(var p=0;p<parseInt(mole_chk);p++)
				{
					var mole_chk_obj = document.getElementById("mole_chk_"+p+"_"+i);
					var mole_abbr = document.getElementById("mole_abbr_"+p+"_"+i);
					
					if(mole_chk_obj.checked)
					{
						var mole_qty_mmbtu = document.getElementById("mole_qty_mmbtu_"+p+"_"+i);
						
						if(trim(mole_qty_mmbtu.value) == "")
						{
							msg+="Enter Molecule MMBTU for "+mole_abbr.value+" for ROW - "+parseInt(i+1)+"!\n";
							flag=false;
						}
						
						mole_count++;
					}
				}
				
				if(mole_count == 0)
				{
					msg+="Enter Molecule MMBTU for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				///////////////////////////////////////////////////////////////////
			}
		}
	}
	else
	{
		if(chk.checked)
		{ 	chk_count++
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
			
			////////////////MOLECULE/////////////////////////////////////
			var mole_chk = document.getElementsByName("mole_chk_0").length;
			
			var tot_mole_qty_mmbtu = document.getElementById("tot_mole_qty_mmbtu_0")
			
			var mole_count=parseInt("0");
			for(var p=0;p<parseInt(mole_chk);p++)
			{
				var mole_chk_obj = document.getElementById("mole_chk_"+p+"_0");
				var mole_abbr = document.getElementById("mole_abbr_"+p+"_0");
				
				if(mole_chk_obj.checked)
				{
					var mole_qty_mmbtu = document.getElementById("mole_qty_mmbtu_"+p+"_0");
					
					if(trim(mole_qty_mmbtu.value) == "")
					{
						msg+="Enter Molecule MMBTU for "+mole_abbr.value+" for ROW - "+parseInt(1)+"!\n";
						flag=false;
					}
					
					mole_count++;
				}
			}
			
			if(mole_count == 0)
			{
				msg+="Enter Molecule MMBTU for ROW - "+parseInt(1)+"!\n";
				flag=false;
			}
			///////////////////////////////////////////////////////////////////
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
			editAllowedOnCpStatus = true;
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

function enabled()
{
	var chk = document.forms[0].chk;
	var is_exist = document.forms[0].is_exist;
	var nom_block = document.forms[0].nom_block;
	var cp_status = document.forms[0].cp_status;
	var index=0;
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0; i<chk.length; i++)
			{
				index=i;
				var moleConfigBtn = document.getElementById("moleConfigBtn"+index);
				
				if(is_exist[i].value=="Y")
				{
					chk[i].style.pointerEvents = "none";
					chk[i].checked=true;
					if(nom_block[i].value!="Y" && cp_status[i].value != "N")
					{
						setEnableDisabled(chk[i],index)
					}
					else
					{
						//moleConfigBtn.style.display="";
						moleConfigBtn.disabled = false;
						calcRemainMoleQty(index);
					}
				}
			}
		}
		else
		{
			index=0;
			var moleConfigBtn = document.getElementById("moleConfigBtn"+index);
			
			if(is_exist.value=="Y")
			{
				chk.style.pointerEvents = "none";
				chk.checked=true;
				if(nom_block.value!="Y" && cp_status.value!="N")
				{
					setEnableDisabled(chk,index)
				}
				else
				{
					//moleConfigBtn.style.display="";
					moleConfigBtn.disabled = false;
					calcRemainMoleQty(index);
				}
			}
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_Buy_Nom_Alloc_Mgmt" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String clearance = request.getParameter("clearance")==null?"KYC":request.getParameter("clearance");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String cargo_no = request.getParameter("cargo_no")==""?"0":request.getParameter("cargo_no");

String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");

String from_date=request.getParameter("from_date")==null?"":request.getParameter("from_date");
String to_date=request.getParameter("to_date")==null?"":request.getParameter("to_date");

purchase.setCallFlag("PERIODIC_ALLOCATION");
purchase.setComp_cd(owner_cd);
purchase.setClearance(clearance);
purchase.setCounterparty_cd(counterparty_cd);
purchase.setAgmt_no(agmt_no);
purchase.setAgmt_rev_no(agmt_rev_no);
purchase.setCont_no(cont_no);
purchase.setCont_rev_no(cont_rev_no);
purchase.setContract_type(contract_type);
purchase.setFrom_date(from_date);
purchase.setTo_date(to_date);
purchase.setCargo_no(cargo_no);
purchase.init();

if(from_date.equals(""))
{
	from_date=purchase.getFrom_date();
}
if(to_date.equals(""))
{
	to_date=purchase.getTo_date();
}

Vector VCOUNTERPARTY_CD = purchase.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = purchase.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = purchase.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPATY_STATUS = purchase.getVCOUNTERPATY_STATUS();

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

Vector VTAX_DTL = purchase.getVTAX_DTL();
Vector VNOM_BLOCK = purchase.getVNOM_BLOCK();

Vector VGAS_DT = purchase.getVGAS_DT();
Vector VINDEX = purchase.getVINDEX();

Vector VIS_EXIST = purchase.getVIS_EXIST();

Vector VPRODUCT_ABBR = purchase.getVPRODUCT_ABBR();
Vector VMOLECULE_ABBR = purchase.getVMOLECULE_ABBR();
Vector VMOLECULE_MAPPING = purchase.getVMOLECULE_MAPPING();

Vector VALLOC_MOLE_MAPPING = purchase.getVALLOC_MOLE_MAPPING();
Vector VALLOC_MOLE_SEQ_NO = purchase.getVALLOC_MOLE_SEQ_NO();
Vector VALLOC_MOLE_QTY_MMBTU = purchase.getVALLOC_MOLE_QTY_MMBTU();
Vector VALLOC_MOLE_QTY_SCM = purchase.getVALLOC_MOLE_QTY_SCM();
Vector VALLOC_MOLE_EXIST = purchase.getVALLOC_MOLE_EXIST();
Vector VALLOC_MOLE_COLOR = purchase.getVALLOC_MOLE_COLOR();

String start_dt = purchase.getStart_dt();
String end_dt = purchase.getEnd_dt();
String cont_name = purchase.getCont_name();
String contract_type_nm = purchase.getContract_type_nm();

if(!start_dt.equals("") && !end_dt.equals(""))
{
	cont_name+=" ("+start_dt+" - "+end_dt+")";
}

String trans_cd = "0"; //NOT IN USED 10/11/2022
String trans_plant_seq = "0"; //NOT IN USED 10/11/2022

%>
<body ><!-- onload="enabled();" -->
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
					    	Periodic Allocation (Purchase)
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12" align="center">
				    				<div class="btn-group">
										<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("KYC")){%>btnactive<%}%>" onclick="refresh('KYC');">KYC</label>
										<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("IGX")){%>btnactive<%}%>" onclick="refresh('IGX');">IGX</label>
									</div>
				    			</div>
				  			</div>
						</div>
					</div>
					&nbsp;
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Trader<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=clearance%>');">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    			<!-- <label class="form-label"><b>Contract No<span class="s-red">*</span></b></label> -->
				    			<input type="button" class="btn btn-info btn-sm select_btn" value="Select Contract" onclick="openContList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="" value="<%=cont_name%>" style="font-weight: bold;" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_no" value="<%=cont_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_rev_no" value="<%=cont_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cargo_no" value="<%=cargo_no%>" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="contract_type" value="<%=contract_type%>"readOnly>
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Contract Type</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="contract_type_nm" value="<%=contract_type_nm%>" style="font-weight: bold;" readOnly>
				      			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-2 col-xs-2 col-md-2"></div>
						<div class="col-sm-8 col-xs-8 col-md-8">
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>Gas Day : From</b></label>
									</div>
									<div class="col-auto">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_date%>" size="8" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);refresh('<%=clearance%>');" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					    			<div class="col-auto">
										<label class="form-label"><b>To</b></label>
									</div>
									<div class="col-auto">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_date%>" size="8" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);refresh('<%=clearance%>');" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					    		</div>
					    	</div>
			    		</div>
			    		<div class="col-sm-2 col-xs-2 col-md-2"></div>
		    		</div>
		    	</div>
		    	<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Gas Date</th>
										<th><input class="form-check-input" type="checkbox" name="chkAll" onclick="checkAll(this);"></th>
										<th>Business Unit</th>
										<th>Trader Plant</th>
										<th>Tax</th>
										<th>Rev#</th>
										<th>DCQ</th>
										<th>MDCQ</th>
										<th>Seller Nomination (Rev)<br>(MMBTU)</th>
										<th>Energy (MMBTU)</th>
										<th>Energy (SCM)</th>
										<th>Map Molecule</th>
										<th>Gen Date:Time</th>
										<th>Calorific Value Base<br>KCal/SCM</th>
									</tr>
								</thead>
								<tbody>
								<%int i=0,k=0;
								if(VCOUNTERPARTY_PLANT_SEQ.size()>0){ %>
									<%for(int j=0; j<VGAS_DT.size(); j++) {
										
										String temp_gas_dt=""+VGAS_DT.elementAt(j);
										int index=Integer.parseInt(""+VINDEX.elementAt(j));
										k=0;
										if(index>0){
									%>
											<%for(i=i; i<VCOUNTERPARTY_PLANT_SEQ.size(); i++){
												k+=1;
											%>
												<tr>
													<%if(k==1){ %>
													<td align="center" rowspan="<%=index%>">
														<div style="width:100px;">
															<div class="row m-b-5">
																<div class="col">
																	<div class="input-group input-group-sm">
												      					<input type="text" class="form-control form-control-sm date fmsdtpick" value="<%=temp_gas_dt%>" maxLength="10" 
												      					onblur="validateDate(this);" onchange="validateDate(this);" disabled>
												      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
												      				</div>
											      				</div>
											      			</div>
											      		</div>
													</td>
													<%} %>
													<td align="center" valign="middle" <%if(VNOM_BLOCK.elementAt(i).equals("Y")) {%>style="background: #df9fbf;" title="Invoice Generated!"<%} %>>
														<input type="checkbox" class="form-check-input" name="chk" 
														onclick="setEnableDisabled(this,'<%=i%>');calculateSCM('<%=i%>');" 
														<%if(VNOM_BLOCK.elementAt(i).equals("Y") || VCOUNTERPATY_STATUS.elementAt(i).equals("N")) {%>disabled style="pointer-events: none;"<%} %>>
														
														<input type="hidden" name="nom_block" id="nom_block<%=i%>" value="<%=VNOM_BLOCK.elementAt(i)%>" disabled>
														<input type="hidden" name="gas_dt" id="gas_dt<%=i%>" value="<%=temp_gas_dt%>" disabled>
														<input type="hidden" name="index" id="index_<%=i%>" value="<%=i%>" disabled>
														<input type="hidden" name="is_exist" id="is_exist<%=i%>" value="<%=VIS_EXIST.elementAt(i)%>">
														<input type="hidden" name="cp_status" id="cp_status<%=i%>" value="<%=VCOUNTERPATY_STATUS.elementAt(i)%>" disabled>
													</td>
													<td align="center">
														<%=VBU_PLANT_ABBR.elementAt(i)%>
														<input type="hidden" name="bu_plant_seq" id="bu_plant_seq<%=i%>" value="<%=VBU_PLANT_SEQ.elementAt(i)%>" disabled>
													</td>
													<td align="center">
														<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(i)%>
														<input type="hidden" name="plant_seq" id="plant_seq<%=i%>" value="<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(i)%>" disabled>
														<input type="hidden" value="<%=counterparty_cd%>" disabled>
														<%-- <input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>" disabled>
			   											<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=i%>" value="<%=VAGMT_REV_NO.elementAt(i)%>" disabled>
									      				<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>" disabled>
									      				<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=i%>" value="<%=VCONT_REV_NO.elementAt(i)%>" disabled>
									      				<input type="hidden" name="contract_type" id="contract_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled> --%>
									      				<input type="hidden" name="trans_cd" id="trans_cd<%=i%>" value="<%=trans_cd%>" disabled>
									      				<input type="hidden" name="trans_plant_seq" id="trans_plant_seq<%=i%>" value="<%=trans_plant_seq%>" disabled>
									      				<input type="hidden" name="internal_map_id" id="internal_map_id<%=i%>" value="<%=VINTERNAL_MAP_ID.elementAt(i)%>">
													</td>
													<td><%=VTAX_DTL.elementAt(i) %></td>
													<td align="center"><%=VNOM_REV_NO.elementAt(i)%></td>
													<td align="right">
														<%=VDCQ.elementAt(i)%>
														<input type="hidden" value="<%=VDCQ.elementAt(i)%>" name="dcq" id="dcq<%=i%>">
														<input type="hidden" value="<%=VMDCQ_QTY.elementAt(i)%>" name="mdcq_qty" id="mdcq_qty<%=i%>">
													</td>
													<td align="right">
														<%=VMDCQ_QTY.elementAt(i)%>
													</td>
													<td align="right">
														<%=VBUYER_NOM.elementAt(i)%> (<%=VBUYER_NOM_REV_NO.elementAt(i)%>)
														<input type="hidden" name="buyer_nom_qty" id="buyer_nom_qty<%=i%>" value="<%=VBUYER_NOM.elementAt(i)%>">
													</td>
													<td align="center">
														<div style="width:100px;">
															<input type="text" class="form-control form-control-sm" name="qty_mmbtu" id="qty_mmbtu<%=i%>" value="<%=VQTY_MMBTU.elementAt(i)%>" 
															style="text-align:right;background:<%=VNOM_COLOR.elementAt(i)%>" 
															onpaste="doPaste('<%=i%>');" 
															onblur="negNumber(this);checkNumber1(this,9,2);checkQty('<%=i%>');calculateSCM('<%=i%>');calcRemainMoleQty('<%=i%>')" disabled readonly>
															<input type="hidden" class="form-control form-control-sm" name="tmp_qty_mmbtu" id="tmp_qty_mmbtu<%=i%>" value="<%=VQTY_MMBTU.elementAt(i)%>" style="text-align:right" disabled>
														</div>
													</td>
													<td align="center">
														<div style="width:100px;">
															<input type="text" class="form-control form-control-sm" name="qty_scm" id="qty_scm<%=i%>" value="<%=VQTY_SCM.elementAt(i)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
														</div>
													</td>
													<td>
														<div class="row m-b-5">
															<div class="col">
																<input type="button" class="btn btn-sm config_btn" id="moleConfigBtn<%=i%>" value="MM" onclick="variableMolecule('<%=i%>');" disabled="disabled">
															</div>
															<%-- <div style="width:110px;" class="col" id="hed_mole_qty<%=i%>"></div> --%>
														</div>
													</td>
													<td align="center">
														<div style="width:220px;">
															<div class="row m-b-5">
																<div class="col">
																	<div class="input-group input-group-sm">
												      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="gen_dt" id="gen_dt<%=i%>" value="<%=VGEN_DT.elementAt(i)%>" maxLength="10" 
												      					style="background:<%=VNOM_COLOR.elementAt(i)%>"
												      					onblur="validateDate(this);" onchange="validateDate(this);" disabled>
												      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
												      				</div>
											      				</div>
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
												<%if(k==index){ 
													i+=1;
													break;
												}%>
											<%} %>
										<%} %>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="14" align="center"><%=utilmsg.infoMessage("<b>Seller Nomination is not Done for the Selected Gas Day!</b>")%></td>
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

<%
int x=0;
for(int y=0; y<VGAS_DT.size(); y++) {
for(x=x; x<VCOUNTERPARTY_PLANT_SEQ.size(); x++){
	Vector TEMP_MOLE_MAP=new Vector();
	Vector TEMP_MOLE_SEQ_NO=new Vector();
	Vector TEMP_MOLE_QTY_MMBTU=new Vector();
	Vector TEMP_MOLE_QTY_SCM=new Vector();
	Vector TEMP_MOLE_EXIST=new Vector();
	Vector TEMP_MOLE_COLOR=new Vector();
	
	TEMP_MOLE_MAP=(Vector) VALLOC_MOLE_MAPPING.elementAt(x);
	TEMP_MOLE_SEQ_NO=(Vector) VALLOC_MOLE_SEQ_NO.elementAt(x);
	TEMP_MOLE_QTY_MMBTU=(Vector) VALLOC_MOLE_QTY_MMBTU.elementAt(x);
	TEMP_MOLE_QTY_SCM=(Vector) VALLOC_MOLE_QTY_SCM.elementAt(x);
	TEMP_MOLE_EXIST=(Vector) VALLOC_MOLE_EXIST.elementAt(x);
	TEMP_MOLE_COLOR=(Vector) VALLOC_MOLE_COLOR.elementAt(x);
%>
<div class="modal fade" id="MoleculeModal_<%=x%>" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
   		<div class="modal-content">
			<div class="modal-header cdheader">
	    		<div class="topheader">
					Molecule Mapping
				</div>
	    		<input type="button" class="btn-close" data-bs-dismiss="modal">
	  		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>Select</th>
										<th>Product ABBR</th>
										<th>Molecule ABBR</th>
										<th>MMBTU</th>
										<th>SCM</th>
									</tr>
								</thead>
								<tbody>
								<%for(int j=0; j<VMOLECULE_MAPPING.size();j++){
									
									String mole_map=""+VMOLECULE_MAPPING.elementAt(j);
									String mole_seq_no="",mole_qty_mmbtu="",mole_qty_scm="",color="",mole_exist="";
									
									int map_index=TEMP_MOLE_MAP.indexOf(mole_map);
									if(map_index > -1){
										mole_seq_no=""+TEMP_MOLE_SEQ_NO.elementAt(map_index);
										mole_qty_mmbtu=""+TEMP_MOLE_QTY_MMBTU.elementAt(map_index);
										mole_qty_scm=""+TEMP_MOLE_QTY_SCM.elementAt(map_index);
										color=""+TEMP_MOLE_COLOR.elementAt(map_index);
										mole_exist=""+TEMP_MOLE_EXIST.elementAt(map_index);
									}
								%>
									<tr>
										<td align="center">
											<input type="checkbox" class="form-check-input" name="mole_chk_<%=x%>" id="mole_chk_<%=j%>_<%=x%>" onclick="setEnableDisableMolecule(this,'<%=j%>','<%=x%>')" disabled 
											<%if(TEMP_MOLE_MAP.contains(mole_map)){ %>checked<%} %>
											<%if(mole_exist.equals("Y")){ %>style="pointer-events: none;"<%} %>>
											<input type="hidden" name="mole_mapping_<%=x%>" id="mole_mapping_<%=j%>_<%=x%>" value="<%=VMOLECULE_MAPPING.elementAt(j)%>" disabled>
											<input type="hidden" name="mole_abbr_<%=x%>" id="mole_abbr_<%=j%>_<%=x%>" value="<%=VMOLECULE_ABBR.elementAt(j)%>" disabled>
											<input type="hidden" name="mole_seq_no_<%=x%>" id="mole_seq_no_<%=j%>_<%=x%>" value="<%=mole_seq_no%>" disabled>
										</td>
										<td align="center"><%=VPRODUCT_ABBR.elementAt(j)%></td>
										<td align="center"><%=VMOLECULE_ABBR.elementAt(j)%></td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="mole_qty_mmbtu_<%=x%>" id="mole_qty_mmbtu_<%=j%>_<%=x%>" 
												value="<%=mole_qty_mmbtu%>" 
												style="text-align:right;background: <%=color%>"
												onkeyup="negNumber(this);checkNumber1(this,9,2);calculateSCM('<%=x%>');calcRemainMoleQty('<%=x%>');" 
												onblur="negNumber(this);checkNumber1(this,9,2);calculateSCM('<%=x%>');calcRemainMoleQty('<%=x%>');" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="mole_qty_scm_<%=x%>" id="mole_qty_scm_<%=j%>_<%=x%>" 
												value="<%=mole_qty_scm %>" 
												style="text-align:right;"
												onblur="negNumber(this);checkNumber1(this,9,2);" readonly disabled>
											</div>
										</td>
									</tr>
								<%} %>
									<tr>
										<td colspan="3" align="right">
											<b>Total Molecule </b>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="tot_mole_qty_mmbtu" id="tot_mole_qty_mmbtu_<%=x%>" value="" 
												style="text-align:right;font-weight: bold;" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="tot_mole_qty_scm" id="tot_mole_qty_scm_<%=x%>" value="" 
												style="text-align:right;font-weight: bold;" readonly disabled>
											</div>
										</td>
									</tr>
									<%-- <tr>
										<td colspan="3" align="right">
											<b>Remaining MMBTU </b>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="remain_mole_qty_mmbtu" id="remain_mole_qty_mmbtu_<%=x%>" value="" 
												style="text-align:right;font-weight: bold;" disabled>
											</div>
										</td>
										<td align="center">
										</td>
									</tr> --%>
								</tbody>
							</table>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
      			<%if(!VNOM_BLOCK.elementAt(x).equals("Y")) {%>
        		<div class="" align="right">
					<input type="button" class="btn btn-warning com-btn" value="Proceed" onclick="doMoleSubmit('<%=x%>')">
				</div>
				<%} %>
      		</div>
      	</div>
	</div>
</div>
<%} %>
<%} %>
<script type="text/javascript">
function variableMolecule(index)
{
	$("#MoleculeModal_"+index).modal("show");
}
function setEnableDisableMolecule(obj,j_index,i_index)
{
	var mole_mapping = document.getElementById("mole_mapping_"+j_index+"_"+i_index)
	var mole_seq_no = document.getElementById("mole_seq_no_"+j_index+"_"+i_index)
	var mole_qty_mmbtu = document.getElementById("mole_qty_mmbtu_"+j_index+"_"+i_index)
	var mole_qty_scm = document.getElementById("mole_qty_scm_"+j_index+"_"+i_index);
	
	if(obj.checked)
	{
		mole_mapping.disabled=false;
		mole_seq_no.disabled=false;
		mole_qty_mmbtu.disabled=false;
		mole_qty_scm.disabled=false;
	}
	else
	{
		mole_mapping.disabled=true;
		mole_seq_no.disabled=true;
		mole_qty_mmbtu.disabled=true;
		mole_qty_scm.disabled=true;
	}
	
	calcRemainMoleQty(i_index);
}

function calcRemainMoleQty(index)
{
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index)
	var qty_scm = document.getElementById("qty_scm"+index)
	var tmp_qty_mmbtu = document.getElementById("tmp_qty_mmbtu"+index)
	//var hed_mole_qty = document.getElementById("hed_mole_qty"+index);
	
	var mole_chk = document.getElementsByName("mole_chk_"+index).length;
	
	var tot_mole_qty_mmbtu = document.getElementById("tot_mole_qty_mmbtu_"+index)
	var tot_mole_qty_scm = document.getElementById("tot_mole_qty_scm_"+index)
	
	var tot_qty_mmbtu=parseFloat("0");
	var tot_qty_scm=parseFloat("0");
	var rem_qty_mmbtu=parseFloat("0");
	
	for(var i=0;i<parseInt(mole_chk);i++)
	{
		var mole_chk_obj = document.getElementById("mole_chk_"+i+"_"+index);
		
		if(mole_chk_obj.checked)
		{
			var mole_qty_mmbtu = document.getElementById("mole_qty_mmbtu_"+i+"_"+index);
			var mole_qty_scm = document.getElementById("mole_qty_scm_"+i+"_"+index);
			
			if(mole_qty_mmbtu.value != "")
			{
				tot_qty_mmbtu=tot_qty_mmbtu + parseFloat(mole_qty_mmbtu.value);
			}
			
			if(mole_qty_scm.value != "")
			{
				tot_qty_scm=tot_qty_scm + parseFloat(mole_qty_scm.value);
			}
		}
	}
	
	tot_mole_qty_mmbtu.value=round(tot_qty_mmbtu,2);
	tot_mole_qty_scm.value=round(tot_qty_scm,2);
	
	//hed_mole_qty.innerHTML=tot_mole_qty_mmbtu.value
	qty_mmbtu.value=tot_mole_qty_mmbtu.value
	qty_scm.value=tot_mole_qty_scm.value
	tmp_qty_mmbtu.value=tot_mole_qty_mmbtu.value
	
	if(qty_mmbtu.value!="")
	{
		rem_qty_mmbtu = parseFloat(qty_mmbtu.value) - tot_qty_mmbtu;
		
	}
}

function doMoleSubmit(index)
{
	var mole_chk = document.getElementsByName("mole_chk_"+index).length;
	
	var tot_mole_qty_mmbtu = document.getElementById("tot_mole_qty_mmbtu_"+index);
	var tot_mole_qty_scm = document.getElementById("tot_mole_qty_scm_"+index);
	
	var msg="";
	var flag=true;
	
	for(var i=0;i<parseInt(mole_chk);i++)
	{
		var mole_chk_obj = document.getElementById("mole_chk_"+i+"_"+index);
		var mole_abbr = document.getElementById("mole_abbr_"+i+"_"+index);
		
		if(mole_chk_obj.checked)
		{
			var mole_qty_mmbtu = document.getElementById("mole_qty_mmbtu_"+i+"_"+index);
			
			if(trim(mole_qty_mmbtu.value) == "")
			{
				msg+="Enter MMBTU for "+mole_abbr.value+"!\n";
				flag=false;
			}
		}
	}
	
	if(tot_mole_qty_mmbtu.value=="")
	{
		msg+="Total Molecule can not be Empty!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Procced?");
		if(a)
		{
			document.getElementById("tmp_qty_mmbtu"+index).value=tot_mole_qty_mmbtu.value;
			document.getElementById("qty_mmbtu"+index).value=tot_mole_qty_mmbtu.value;
			document.getElementById("qty_scm"+index).value=tot_mole_qty_scm.value;
			//document.getElementById("hed_mole_qty"+index).innerHTML=tot_mole_qty_mmbtu.value;
			$("#MoleculeModal_"+index).modal("hide");
		}
	}
	else
	{
		alert(msg);
	}
}
</script>

<input type="hidden" name="option" value="PERIODIC_ALLOC">
<input type="hidden" name="prev_counterparty_cd" value="<%=counterparty_cd%>">

<input type="hidden" name="prev_clearance" value="<%=clearance%>">
<input type="hidden" name="clearance" value="<%=clearance%>">

<input type="hidden" name="hid_from_dt" value="<%=from_date%>">
<input type="hidden" name="hid_to_dt" value="<%=to_date%>">
<input type="hidden" name="cont_start_dt" value="<%=start_dt%>">
<input type="hidden" name="cont_end_dt" value="<%=end_dt%>">

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