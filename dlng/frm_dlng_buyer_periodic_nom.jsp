<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
.icon-wrapper {
  display: inline-block;
  position: relative;
}

.icon-wrapper .fa-eye {
  font-size: 2em;
  transform: scaleX(-1); /* flip-horizontal */
}

.icon-wrapper .fa-truck {
  position: absolute;
  right: -12px;   /* tweak to align horizontally */
  top: 2px;       /* tweak to align vertically */
  font-size: 1.2em;  /* bigger than default */
  color: #000;
}
</style>
<%@ include file="../util/common_js.jsp"%>

<script>

function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var prev_counterparty_cd = document.forms[0].prev_counterparty_cd.value;
	
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var cargo_no = document.forms[0].cargo_no.value;
	
	var gas_dt = document.forms[0].gas_dt.value;
	var tmp_gas_dt = document.forms[0].tmp_gas_dt.value;
	
	var contract_st_dt = document.forms[0].contract_st_dt.value;
	var contract_end_dt = document.forms[0].contract_end_dt.value;
	
	var nomination_freq = document.forms[0].nomination_freq.value;
	
	var msg="";
	var flag=true;
	
	/*
	if(trim(gas_dt) != "" && trim(contract_st_dt) != "" && trim(contract_end_dt) != "")
	{
		var compareDt =  compareDate(gas_dt,contract_st_dt);
		var compareDt1 =  compareDate(gas_dt,contract_end_dt);
		if((compareDt=="1" && compareDt1=="1") || (compareDt=="2" && compareDt1=="2"))
		{
			msg+="Select Gas Date("+gas_dt+") in range of Contract Period("+contract_st_dt+" - "+contract_end_dt+")!";
			flag=false;
			document.forms[0].gas_dt.value=tmp_gas_dt;
		}
	}

	if(counterparty_cd != prev_counterparty_cd)
	{
		cont_no="";
		cont_rev_no="";
		agmt_no="";
		agmt_rev_no="";
	}
	*/	
	
	counterparty_cd="0";
	cont_no="";
	cont_rev_no="";
	agmt_no="";
	agmt_rev_no="";
	cargo_no="0";
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "frm_dlng_buyer_periodic_nom.jsp?counterparty_cd="+counterparty_cd+"&u="+u+
				"&gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+
				"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&cargo_no="+cargo_no;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}

var newWindow;
function openContList()
{
	var gas_dt = document.forms[0].gas_dt.value;
	var nomination_freq = document.forms[0].nomination_freq.value;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_dlng_nom_contract_list.jsp?gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+"&nomination_type=B","DLNG Contract List","top=10,left=10,width=1300,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_dlng_nom_contract_list.jsp?gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+"&nomination_type=B","DLNG Contract List","top=10,left=10,width=1300,height=600,scrollbars=1");
	}
}

function setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_type,cargo_no)
{
	var gas_dt = document.forms[0].gas_dt.value;
	var nomination_freq = document.forms[0].nomination_freq.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_buyer_periodic_nom.jsp?counterparty_cd="+countpty_cd+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&st_dt="+st_dt+"&end_dt="+end_dt+"&gas_dt="+gas_dt+
			"&nomination_freq="+nomination_freq+"&u="+u+"&contract_type="+cont_type+"&cargo_no="+cargo_no;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function checkAll(obj,range,j)
{
	var chk = document.forms[0].chk;
	var nom_block = document.forms[0].nom_block;
	
	var min=parseInt("0");
	var max=parseInt("0");
	
	var split=range.split("-");
	min=parseInt(split[0]);
	max=parseInt(split[1]);
	
	if(chk!=null && chk.length!=undefined)
	{
		for(var i=0; i<chk.length; i++)
		{
			if(min <= i && max >= i)
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
				calculateSCM(j,i);
			}
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
		calculateSCM(j,i);
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
	
	var week_gas_dt = document.getElementById("week_gas_dt"+index);
	var gen_dt = document.getElementById("gen_dt"+index);
	var gen_time = document.getElementById("gen_time"+index);
	//var rd1 = document.getElementById("rd1"+index);
	//var rd2 = document.getElementById("rd2"+index);
	var gcv = document.getElementById("gcv"+index);
	var ncv = document.getElementById("ncv"+index);
	var base = document.getElementById("base"+index);
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	var tmp_qty_mmbtu = document.getElementById("tmp_qty_mmbtu"+index);
	var qty_scm = document.getElementById("qty_scm"+index);
	var qty_mt = document.getElementById("qty_mt"+index);
	var isRowInvoiced = document.getElementById("isRowInvoiced"+index);
	
	var truck_icon = document.getElementById("truck_icon_" + index);
	
	var sf_id = document.getElementById("sf_id"+index);
	
	var cp_status = document.getElementById("cp_status"+index);
	var plant_seq = document.getElementById("plant_seq"+index);
	var trans_cd = document.getElementById("trans_cd"+index);
	var trans_plant_seq = document.getElementById("trans_plant_seq"+index);
	var bu_plant_seq = document.getElementById("bu_plant_seq"+index);
	
	var l_index = document.getElementById("index_"+index);
	var index1 = document.getElementById("index1"+index);
	
	var truck_chk = document.getElementsByName('truck_chk_'+index).length;
	
	if(obj.checked && isRowInvoiced.value =='N')
	{
		l_index.disabled=false;
		index1.disabled=false;
	
		//counterparty_cd.disabled=false;
		//agmt_no.disabled=false;
		//agmt_rev_no.disabled=false;
		//cont_no.disabled=false;
		//cont_rev_no.disabled=false;
		//contract_type.disabled=false;
		
		week_gas_dt.disabled=false;
		gen_dt.disabled=false;
		gen_time.disabled=false;
		//rd1.disabled=false;
		//rd2.disabled=false;
		gcv.disabled=false;
		ncv.disabled=false;
		base.disabled=false;
		
		qty_mmbtu.disabled=false;
		tmp_qty_mmbtu.disabled=false;
		qty_scm.disabled=false;
		qty_mt.disabled=false;
		qty_mmbtu.readOnly=false;
		tmp_qty_mmbtu.readOnly=false;

		sf_id.disabled=false;
		
		plant_seq.disabled=false;
		cp_status.disabled=false;
		trans_cd.disabled=false;
		trans_plant_seq.disabled=false;
		bu_plant_seq.disabled=false;
		
		truck_icon.style.pointerEvents = "auto";
		
		//FOR TRUCK
        if(parseInt(truck_chk) > 0)
		{
			for(var i=0;i<parseInt(truck_chk);i++)
			{
				var truck_chk_obj = document.getElementById("truck_chk_"+i+"_"+index);
				//truck_chk_obj.disabled=false;
				setEnableDisableTruck(truck_chk_obj,i,index);
			}
		}
	}
	else
	{
		l_index.disabled=true;
		index1.disabled=true;
		
		//counterparty_cd.disabled=true;
		//agmt_no.disabled=true;
		//agmt_rev_no.disabled=true;
		//cont_no.disabled=true;
		//cont_rev_no.disabled=true;
		//contract_type.disabled=true;
		
		week_gas_dt.disabled=true;
		gen_dt.disabled=true;
		gen_time.disabled=true;
		//rd1.disabled=true;
		//rd2.disabled=true;
		gcv.disabled=true;
		ncv.disabled=true;
		base.disabled=true;
		
		qty_mmbtu.disabled=true;
		qty_scm.disabled=true;
		qty_mmbtu.readOnly=true;
		tmp_qty_mmbtu.readOnly=true;
		qty_scm.readOnly=true;
		qty_mt.readOnly=true;
		
		sf_id.disabled=true;
		
		plant_seq.disabled=true;
		cp_status.disabled=true;
		trans_cd.disabled=true;
		trans_plant_seq.disabled=true;
		bu_plant_seq.disabled=true;
		
		truck_icon.style.pointerEvents = "none";
        
        //FOR TRUCK
        if(parseInt(truck_chk) > 0)
		{
			for(var i=0;i<parseInt(truck_chk);i++)
			{
				var truck_chk_obj = document.getElementById("truck_chk_"+i+"_"+index);
				//truck_chk_obj.disabled=true;
				setEnableDisableTruck(truck_chk_obj,i,index);
			}
		}
	}
}

function setEnableDisableTruck(obj,j_index,i_index)
{
	var truck_trans_cd = document.getElementById("truck_trans_cd_"+j_index+"_"+i_index)
	var truck_cd = document.getElementById("truck_cd_"+j_index+"_"+i_index)
	var truck_index = document.getElementById("truck_index_"+j_index+"_"+i_index)
	var truck_cap_mmbtu = document.getElementById("roundedcapInMmbtu_"+j_index+"_"+i_index)
	var nom_qunt_mmbtu = document.getElementById("nom_qunt_mmbtu_"+j_index+"_"+i_index)
	var nom_qunt_mt = document.getElementById("nom_qunt_mt_"+j_index+"_"+i_index)

	var filling_station = document.getElementById("filling_station"+j_index+"_"+i_index)
	var sel_bay = document.getElementById("sel_bay"+j_index+"_"+i_index)
	var sel_slot = document.getElementById("sel_slot"+j_index+"_"+i_index)
	var disp_arrival_dt = document.getElementById("disp_arrival_dt"+j_index+"_"+i_index)
	var arrival_dt = document.getElementById("arrival_dt"+j_index+"_"+i_index)
	var arrival_time = document.getElementById("arrival_time"+j_index+"_"+i_index)
	var next_avl_hrs = document.getElementById("next_avl_hrs_"+j_index+"_"+i_index)
	var truck_remark = document.getElementById("truck_remark"+j_index+"_"+i_index)
	
	if(obj.checked)
	{
		truck_trans_cd.disabled=false;
		truck_cd.disabled=false;
		truck_index.disabled=false;
		truck_cap_mmbtu.disabled=false;
		nom_qunt_mmbtu.disabled=false;
		nom_qunt_mt.disabled=false;
		filling_station.disabled=false;
		sel_bay.disabled=false;
		sel_slot.disabled=false;
		arrival_dt.disabled=false;
		arrival_time.disabled=false;
		next_avl_hrs.disabled=false;
		truck_remark.disabled=false;
		
		
		/* var counterparty_cd = document.getElementById("counterparty_cd"+i_index).value;
		var agmt_no = document.getElementById("agmt_no"+i_index).value;
		var cont_no = document.getElementById("cont_no"+i_index).value;
		var contract_type = document.getElementById("contract_type"+i_index).value;
		var agmt_rev_no = document.getElementById("agmt_rev_no"+i_index).value;
		var cont_rev_no = document.getElementById("cont_rev_no"+i_index).value; */
		
		var comp_cd = document.forms[0].comp_cd.value;
		var counterparty_cd =document.forms[0].counterparty_cd.value;
		var agmt_no = document.forms[0].agmt_no.value;
		var cont_no = document.forms[0].cont_no.value;
		var contract_type = document.forms[0].contract_type.value;
		var agmt_rev_no = document.forms[0].agmt_rev_no.value;
		var cont_rev_no = document.forms[0].cont_rev_no.value;
		
		if(filling_station.value != "" && filling_station.value != "undefined")
		{
			fetchFillStDeatils(i_index,comp_cd,counterparty_cd,agmt_no,cont_no,contract_type,agmt_rev_no,cont_rev_no);
		}
	}
	else
	{
		truck_trans_cd.disabled=true;
		truck_cd.disabled=true;
		truck_index.disabled=true;
		truck_cap_mmbtu.disabled=true;
		nom_qunt_mmbtu.disabled=true;
		nom_qunt_mt.disabled=true;
		filling_station.disabled=true;
		sel_bay.disabled=true;
		sel_slot.disabled=true;
		arrival_dt.disabled=true;
		arrival_time.disabled=true;
		next_avl_hrs.disabled=true;
		truck_remark.disabled=true;
	}
	
	calcRemainBlncQty(i_index);
}

function calcRemainBlncQty(index)
{
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index)
	
	var truck_chk = document.getElementsByName("truck_chk_"+index).length;
	
	var total_qunt_mmbtu = document.getElementById("total_qunt_mmbtu"+index)
	var total_qunt_mt = document.getElementById("total_qunt_mt"+index)
	var balance_qty = document.getElementById("balance_qty_"+index)
	
	var tot_qty_mmbtu=parseFloat("0");
	var tot_qty_mt=parseFloat("0");
	var rem_qty_mmbtu=parseFloat("0");
	
	for(var i=0;i<parseInt(truck_chk);i++)
	{
		var truck_chk_obj = document.getElementById("truck_chk_"+i+"_"+index);
		
		if(truck_chk_obj.checked)
		{
			var nom_qunt_mmbtu_ = document.getElementById("nom_qunt_mmbtu_"+i+"_"+index);
			var nom_qunt_mt = document.getElementById("nom_qunt_mt_"+i+"_"+index);
			
			if(nom_qunt_mmbtu_.value != "")
			{
				tot_qty_mmbtu=tot_qty_mmbtu + parseFloat(nom_qunt_mmbtu_.value);
			}
			
			if(nom_qunt_mt.value != "")
			{
				tot_qty_mt=tot_qty_mt + parseFloat(nom_qunt_mt.value);
			}
		}
	}
	
	if(qty_mmbtu.value!="")
	{
		rem_qty_mmbtu = parseFloat(qty_mmbtu.value) - tot_qty_mmbtu;
		
		balance_qty.value=round(rem_qty_mmbtu,2);
	}
}

function checkTCQ(index)
{
	var week_gas_dt = document.forms[0].week_gas_dt;
	var gen_dt = document.forms[0].gen_dt;
	var gen_time = document.forms[0].gen_time;
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var tmp_qty_mmbtu = document.forms[0].tmp_qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	var chk = document.forms[0].chk;
	
	var tcq = document.forms[0].tcq;
	var totalBuyerNom = document.forms[0].totalBuyerNom;
	var total_nom_qty = totalBuyerNom.value;
	
	var tot_mmbtu =  parseFloat("0");
	
	var msg="";
	var flag=true;
	var chk_count=parseInt("0");
	
	if(gen_time!=null && gen_time.length!=undefined)
	{
		var tot_BuyNom = parseFloat("0");
		
		for(var i=0; i<gen_time.length; i++)
		{
			if(trim(qty_mmbtu[i].value) != "")
			{
				tot_mmbtu = tot_mmbtu + parseFloat(qty_mmbtu[i].value);
			}
		}
		
		for(var j=0; j<tmp_qty_mmbtu.length; j++)
		{
			if(trim(tmp_qty_mmbtu[j].value) != "")
			{
				total_nom_qty = total_nom_qty - parseFloat(tmp_qty_mmbtu[j].value);
			}
		}
		
		tot_BuyNom=parseFloat(total_nom_qty) + tot_mmbtu;
		
		if(parseFloat(tot_BuyNom) > parseFloat(tcq.value))
		{
			alert("Contract TCQ : "+parseFloat(tcq.value)+"\nEffective Buyer Nom (Best value of Allocation,Buyer Nomination) : "+parseFloat(tot_BuyNom)+"\n\nThe Buyer Nomination Qty should not be > Contract TCQ!");
			document.getElementById('qty_mmbtu'+index).value = "";
			document.getElementById('qty_scm'+index).value = "";
			document.getElementById('qty_mt'+index).value = "";
		}
	}
	else
	{
		if(trim(qty_mmbtu.value) != "")
		{
			tot_mmbtu = tot_mmbtu + parseFloat(qty_mmbtu.value);
		}
		
		if(trim(tmp_qty_mmbtu.value) != "")
		{
			total_nom_qty = total_nom_qty - parseFloat(tmp_qty_mmbtu.value);
		}
		
		var tot_BuyNom=parseFloat(total_nom_qty) + tot_mmbtu;

		if(parseFloat(tot_BuyNom) > parseFloat(tcq.value))
		{
			alert("Contract TCQ : "+parseFloat(tcq.value)+"\nEffective Buyer Nom (Best value of Allocation,Buyer Nomination) : "+parseFloat(tot_BuyNom)+"\n\nThe Buyer Nomination Qty should not be > Contract TCQ!");
			
			document.getElementById('qty_mmbtu'+index).value = "";
			document.getElementById('qty_scm'+index).value = "";
			document.getElementById('qty_mt'+index).value = "";
		}
	}
}

enableButton = true;
function doSubmit()
{
	var chk = document.forms[0].chk;
	var week_gas_dt = document.forms[0].week_gas_dt;
	var gen_dt = document.forms[0].gen_dt;
	var gen_time = document.forms[0].gen_time;
	//var rd1 = document.getElementById("rd1"+index);
	//var rd2 = document.getElementById("rd2"+index);
	var gcv = document.forms[0].gcv;
	var ncv = document.forms[0].ncv;
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	var cp_status = document.forms[0].cp_status;
	
	var tcq = document.forms[0].tcq;
	
	var tot_mmbtu =  parseFloat("0");
	
	var msg="";
	var flag=true;
	var chk_count=parseInt("0");
	
	if(gen_time!=null && gen_time.length!=undefined)
	{
		for(var i=0; i<gen_time.length; i++)
		{
			if(chk[i].checked)
			{
				chk_count++;
				if(trim(cp_status[i].value)=="N")
				{
					msg+="Counterparty is De-active ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				} 
				if(trim(week_gas_dt[i].value)=="")
				{
					msg+="Missing Gas Date for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				} 
				if(trim(gen_dt[i].value)=="")
				{
					msg+="Enter Gen Date for ROW - "+parseInt(i+1)+"!\n";
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
				
			}
		}
	}
	else
	{
		if(chk.checked)
		{ 	chk_count++
			if(trim(cp_status.value)=="N")
			{
				msg+="Counterparty is De-active ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			if(trim(week_gas_dt.value)=="")
			{
				msg+="Missing Gas Date!\n";
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
		}
	}
	
	if(parseInt(chk_count) == 0)
	{
		msg+="Please Select Atleast One ROW for Submit!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Submit Buyer Nomination?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			editAllowedOnCpStatus = true;
			document.forms[0].submit();
		}
	}
	else if(msg != "")
	{
		alert(msg);
	}
}

function calculateSCM(j_index,index)
{
	//grid one
	var rd1 = document.getElementById("rd1_"+j_index);
	var rd2 = document.getElementById("rd2_"+j_index);
	
	var grid_gcv = document.getElementById("grid_gcv_"+j_index).value;
	var grid_ncv = document.getElementById("grid_ncv_"+j_index).value;
	
	document.getElementById("gcv"+index).value=grid_gcv;
	document.getElementById("ncv"+index).value=grid_ncv;
	
	//var rd1 = document.getElementById("rd1"+index);
	//var rd2 = document.getElementById("rd2"+index);
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
	
	var index1 = document.getElementById("index1"+index);
	/* for(var i=1;i<=parseInt(index1.value);i++)
	{
		var sub_qty_mmbtu = document.getElementById("sub_qty_mmbtu"+index+""+i);
		var sub_qty_scm = document.getElementById("sub_qty_scm"+index+""+i);	
		
		if((sub_qty_mmbtu.value!=null && trim(sub_qty_mmbtu.value) !=''))
		{
			var scm = ""+round(((parseFloat(""+sub_qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),0);
			if(isNaN(scm))
			{
				sub_qty_scm.value="";
			}
			else
			{
				sub_qty_scm.value = scm;
			}
		}
	} */
}

function updateGcvNcv(j_index)
{
	var sub_index = document.forms[0].sub_index;
	
	var chk = document.forms[0].chk;
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	
	var i=0;
	var m=0;
	
	if(sub_index!=null && sub_index!=undefined)
	{
		if(sub_index.length!=undefined)
		{
			for(var j=0; j<sub_index.length; j++)
			{
				var subIndex = parseInt(sub_index[j].value);
				
				m=0;
				
				if(qty_mmbtu.length!=undefined)
				{
					for(i=i; i<qty_mmbtu.length; i++)
					{
						m=m+1;
						
						if(chk[i].checked && parseInt(j)==parseInt(j_index))
						{
							calculateSCM(j,i);
							//totalQty();
						}
						
						if(parseInt(m)==parseInt(subIndex))
						{
							i=parseInt(i)+1;
							break;
						}
					}
				}
			}
		}
		else
		{
			var subIndex = parseInt(sub_index.value);
			m=0;
			if(qty_mmbtu.length!=undefined)
			{
				for(i=i; i<qty_mmbtu.length; i++)
				{
					m=m+1;
					
					if(chk[i].checked)
					{
						calculateSCM("0",i);
						//totalQty();
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
				if(chk.checked)
				{
					calculateSCM("0","0");
					//totalQty();
				}
			}
		}
	}
}


function checkQty(index)
{
	var mdcq_qty = document.getElementById("mdcq_qty"+index);
	var gasDt = document.getElementById("week_gas_dt"+index);
	
	var week_gas_dt = document.forms[0].week_gas_dt;
	
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	var tmp_qty_mmbtu = document.forms[0].tmp_qty_mmbtu;
	
	var tot_mmbtu =  parseFloat("0");
	var tot_tmp_mmbtu = parseFloat("0");
	
	if(qty_mmbtu!=null && qty_mmbtu!=undefined)
	{
		if(qty_mmbtu.length!=undefined)
		{
			for(var i=0; i<qty_mmbtu.length; i++)
			{
				if(gasDt.value == week_gas_dt[i].value)
				{
					if(trim(qty_mmbtu[i].value)!="")
					{
						tot_mmbtu = parseFloat(tot_mmbtu) + parseFloat(qty_mmbtu[i].value);
					}
					else
					{
						//qty_mmbtu[i].value="0";
						//qty_scm[i].value="0";
					}
					if(trim(tmp_qty_mmbtu[i].value)!="")
					{
						tot_tmp_mmbtu = parseFloat(tot_tmp_mmbtu) + parseFloat(tmp_qty_mmbtu[i].value); 
					}
				}
			}
		}
		else
		{
			if(gasDt.value == week_gas_dt.value)
			{
				if(trim(qty_mmbtu.value)!="")
				{
					tot_mmbtu = parseFloat(tot_mmbtu) + parseFloat(qty_mmbtu.value);
				}
				if(trim(tmp_qty_mmbtu.value)!="")
				{
					tot_tmp_mmbtu = parseFloat(tot_tmp_mmbtu) + parseFloat(tmp_qty_mmbtu.value); 
				}
			}
		}
	}
	
	if(parseFloat(mdcq_qty.value) < parseFloat(tot_mmbtu))
	{
		alert(""+gasDt.value+"  Nomination "+parseFloat(tot_mmbtu)+" MMBTU > MDCQ Qty("+mdcq_qty.value+" MMBTU) \n\nDo you want to Proceed?");
		return;
	}
}

function totalSubQty(index)
{
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	var qty_scm = document.getElementById("qty_scm"+index);
	
	var index1 = document.getElementById("index1"+index);
	var sub_tot_mmbtu=parseFloat("0");
	var sub_tot_scm=parseFloat("0");
	
	for(var k=1;k<=parseInt(index1.value);k++)
	{
		var sub_chk = document.getElementById("chk"+index+""+k);
		var sub_qty_mmbtu = document.getElementById("sub_qty_mmbtu"+index+""+k);
		var sub_qty_scm = document.getElementById("sub_qty_scm"+index+""+k);	
		
		if(sub_chk.checked)
		{
			if(trim(sub_qty_mmbtu.value) != "")
			{
				sub_tot_mmbtu = sub_tot_mmbtu + parseFloat(sub_qty_mmbtu.value)
			}
			if(trim(sub_qty_scm.value) != "")
			{
				sub_tot_scm = sub_tot_scm + parseFloat(sub_qty_scm.value)
			}
		}
	}
	
	if(parseInt(index1.value)>0)
	{
		if(parseFloat(sub_tot_mmbtu)>=0)
		{
			qty_mmbtu.value=round(parseFloat(sub_tot_mmbtu),2);
			qty_scm.value=round(parseFloat(sub_tot_scm),2);
		}
	}
}

function resetTruckToTable(i) 
{
	
    var truckSelect = document.getElementById("sel_truck_" + i);
    var truckRegNum = truckSelect.options[truckSelect.selectedIndex].text;
    var truckCd = truckSelect.value;
    
    var truckCapInM3 = document.getElementById("addTruckCapInM3_" + i);
    var truckCapInMt = document.getElementById("addTruckCapInMt_" + i);
    var truckCapInMmbtu = document.getElementById("addTruckCapInMmbtu_" + i);
    var truckTransCd = document.getElementById("add_truck_trans_cd_" + i);

    var nomQntMmbtu = document.getElementById("add_nom_qunt_mmbtu_" + i);
    var nomQntMt = document.getElementById("add_nom_qunt_mt_" + i);

    var fillingStation = document.getElementById("add_filling_station_" + i);
    var bay = document.getElementById("add_sel_bay_" + i);
    var slot = document.getElementById("add_sel_slot_" + i);

    var arrivalDate = document.getElementById("add_disp_arrival_dt_" + i);
    var arrivalTime = document.getElementById("add_arrival_time_" + i);
    var nextAvailableHrs = document.getElementById("add_next_avl_hrs_" + i);
    var truckRemark = document.getElementById("add_truck_remark_" + i);

    truckSelect.value = "";
    truckCapInM3.textContent = "-";
    truckCapInMt.textContent = "-";
    truckCapInMmbtu.textContent = "-";
    truckTransCd.value = "";
    nomQntMmbtu.value = "";
    nomQntMt.value = "";
    fillingStation.value = "";
    bay.value = "";
    slot.value = "";
    arrivalDate.value = "";
    arrivalTime.value = "";
    nextAvailableHrs.value = "";
    truckRemark.value = "";
    truckRemark.value = "";
}

function addTruckToTable(i) 
{
	
    var emptyTruckListRow = document.getElementById("emptyTruckListRow" + i);
    var truckSelect = document.getElementById("sel_truck_" + i);
    var truckRegNum = truckSelect.options[truckSelect.selectedIndex].text;
    var truckCd = truckSelect.value;
    
    // Extract capacity and other details
    var truckCapInM3 = document.getElementById("addTruckCapInM3_" + i)?.textContent.trim() || "";
    var truckCapInMt = document.getElementById("addTruckCapInMt_" + i)?.textContent.trim() || "";
    var truckCapInMmbtu = document.getElementById("addTruckCapInMmbtu_" + i)?.textContent.trim() || "";
    var truckTransCd = document.getElementById("add_truck_trans_cd_" + i).value;

    // Extract the truck nomination quantities
    var nomQntMmbtu = document.getElementById("add_nom_qunt_mmbtu_" + i).value;
    var nomQntMt = document.getElementById("add_nom_qunt_mt_" + i).value;

    // Get the filling station and bay details
    var fillingStation = document.getElementById("add_filling_station_" + i).value;
    var fillingStationSelect = document.getElementById("add_filling_station_" + i);
    var fillingStationText = fillingStationSelect.options[fillingStationSelect.selectedIndex].textContent;
    
    var bay = document.getElementById("add_sel_bay_" + i).value;
    var baySelect = document.getElementById("add_sel_bay_" + i);
    var bayText = baySelect.options[baySelect.selectedIndex].textContent;
    
    var slot = document.getElementById("add_sel_slot_" + i).value;
    var slotSelect = document.getElementById("add_sel_slot_" + i);
    var slotText = slotSelect.options[slotSelect.selectedIndex].textContent;

    // Get arrival details
    var arrivalDate = document.getElementById("add_disp_arrival_dt_" + i).value;
    var arrivalTime = document.getElementById("add_arrival_time_" + i).value;
    var nextAvailableHrs = document.getElementById("add_next_avl_hrs_" + i).value;
    var truckRemark = document.getElementById("add_truck_remark_" + i).value;

    if (!truckCd) {
        alert("Please select a truck.");
        return;
    }
    
    var msg="";
    var flag=true;
    
    if(trim(nomQntMmbtu)=="")
	{
		msg+="Enter Truck Nomination Qty(MMBTU) To Add!\n";
		flag=false;
	}
	if(trim(nomQntMt)=="")
	{
		msg+="Enter Truck Nomination Qty(MT) To Add Truck!\n";
		flag=false;
	}
	if(trim(fillingStation)=="" || fillingStation=="0")
	{
		msg+="Select Filling Station To Add Truck!\n";
		flag=false;
	}
	if(trim(bay)=="" || bay=="0")
	{
		msg+="Select Bay To Add Truck!\n";
		flag=false;
	}
	if(trim(slot)=="" || slot=="0")
	{
		msg+="Select Slot To Add Truck!\n";
		flag=false;
	}
	if(trim(arrivalDate)=="")
	{
		msg+="Enter Arrival Date To Add Truck!\n";
		flag=false;
	}
	if(trim(arrivalTime)=="")
	{
		msg+="Enter Arrival Time To Add Truck!\n";
		flag=false;
	}
	if(trim(nextAvailableHrs)=="")
	{
		msg+="Enter Next Available Hrs To Add Truck!\n";
		flag=false;
	}
	
	if(flag)
	{
		//emptyTruckListRow.style.display="none";
		if (emptyTruckListRow) 
		{
			emptyTruckListRow.style.display = "none";
		}
		
		var numberOfRows = document.getElementById("truck_list_" + i).rows.length;
		var j = parseInt(numberOfRows);
		
		// Create a new row for the table
	    var newRow = document.createElement("tr");

	    var cell1 = createCheckboxCell(j, i, truckCd,truckTransCd);
	    newRow.appendChild(cell1);
	    
	    var cell2 = createTextCell(truckRegNum,"REG_NUM",j, i);
	    newRow.appendChild(cell2);
	   
	    var cell3 = createTextCell(truckCapInM3,"M3",j, i);
	    newRow.appendChild(cell3);
	    
	    var cell4 = createTextCell(truckCapInMt,"MT",j, i);
	    newRow.appendChild(cell4);
	    
	    var cell5 = createTextCell(truckCapInMmbtu,"MMBTU",j, i);
	    newRow.appendChild(cell5);
	    
	    var cell6 = createInputCell(nomQntMmbtu, "nom_qunt_mmbtu", j, i);
	    newRow.appendChild(cell6);
	    
	    var cell7 = createInputCell(nomQntMt, "nom_qunt_mt", j, i);
	    newRow.appendChild(cell7);
	    
	    var cell8 = createSelectCell(fillingStation, "filling_station", j, i,fillingStationText);
	    newRow.appendChild(cell8);
	    
	    var cell9 = createSelectCell(bay, "sel_bay", j, i,bayText);
	    newRow.appendChild(cell9);
	    
	    var availAfter = cell2.truck_availAfter;
	    var truck_reg_no = cell2.truck_reg_no;
	    
	    var cell10 = createSelectCell(slot, "sel_slot", j, i, slotText,availAfter,truck_reg_no);
	    newRow.appendChild(cell10);
	    
	    /* var cell11 = createDateTimeCell(arrivalDate, arrivalTime, j, i);
	    newRow.appendChild(cell11); */
	    
	    var cell11a = createDateCell(arrivalDate, j, i);
	    newRow.appendChild(cell11a);

	    var cell11b = createTimeCell(arrivalTime, j, i);
	    newRow.appendChild(cell11b);

	    var cell12 = createInputCell(nextAvailableHrs, "next_avl_hrs", j, i);
	    newRow.appendChild(cell12);
	    
	    var cell13 = createTextareaCell(truckRemark, "truck_remark", j, i);
	    newRow.appendChild(cell13);
		
	    // Append the new row to the table
	    document.getElementById("truck_list_" + i).appendChild(newRow);
	    
	    resetTruckToTable(i);
	}
	else
	{
		alert(msg);
		return;
	}
	
	calcRemainBlncQty(i);
}

// Helper functions to create table cells

function createCheckboxCell(j, i, truckCd,truckTransCd) {
    var td = document.createElement("td");
    td.align = "center";
    
    var checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.className = "form-check-input";
    checkbox.name = "truck_chk_" + i;
    checkbox.id = "truck_chk_"+j+"_"  + i;
    checkbox.checked = true;
    checkbox.setAttribute("onclick", "setEnableDisableTruck(this, '" + j + "', '" + i + "');");
    
    var hiddenCd = document.createElement("input");
    hiddenCd.type = "hidden";
    hiddenCd.name = "truck_trans_cd_" + i;
    hiddenCd.id = "truck_trans_cd_"+j+"_"  + i;
    hiddenCd.value = truckTransCd;

    var hiddenCd1 = document.createElement("input");
    hiddenCd1.type = "hidden";
    hiddenCd1.name = "truck_cd_" + i;
    hiddenCd1.id = "truck_cd_"+j+"_"  + i;
    hiddenCd1.value = truckCd;

    var hiddenCd2 = document.createElement("input");
    hiddenCd2.type = "hidden";
    hiddenCd2.name = "truck_index_" + i;
    hiddenCd2.id = "truck_index_"+j+"_"  + i;
    hiddenCd2.value = j;

    td.appendChild(checkbox);
    td.appendChild(hiddenCd);
    td.appendChild(hiddenCd1);
    td.appendChild(hiddenCd2);
    return td;
}

function createTextCell(value,objText,j,i) {
    var td = document.createElement("td");
    td.align = "center";
    td.textContent = value || 'N/A';

    if(objText=="MMBTU")
    {
    	var hiddenCd2 = document.createElement("input");
        hiddenCd2.type = "hidden";
        hiddenCd2.name = "roundedcapInMmbtu";
        hiddenCd2.id = "roundedcapInMmbtu_"+j+"_"  + i;
        hiddenCd2.value = value;
        td.appendChild(hiddenCd2);
    }

    else if(objText=="REG_NUM")
    {
    	var truck_reg_no = document.getElementById("add_truck_reg_no_" + i).value;
    	var truck_availAfter = document.getElementById("add_truck_availAfter_" + i).value;
    	
    	var hiddenCd3 = document.createElement("input");
        hiddenCd3.type = "hidden";
        hiddenCd3.name = "truck_reg_no_"+i;
        hiddenCd3.id = "truck_reg_no_"+j+"_"+ i;
        hiddenCd3.value = truck_reg_no;
        td.appendChild(hiddenCd3);

        var hiddenCd4 = document.createElement("input");
        hiddenCd4.type = "hidden";
        hiddenCd4.name = "truck_availAfter_"+i;
        hiddenCd4.id = "truck_availAfter_"+j+"_"+ i;
        hiddenCd4.value = truck_availAfter;
        td.appendChild(hiddenCd4);
        
        td.truck_reg_no = truck_reg_no;
        td.truck_availAfter = truck_availAfter;
    }
 
    return td;
}

function createInputCell(value, namePrefix, j, i) {
    var td = document.createElement("td");
    td.align = "center";
    
    var input = document.createElement("input");
    input.type = "text";
    input.className = "form-control form-control-sm";
    input.name = namePrefix + "_" + i;
    input.id = namePrefix + "_"+j+"_"  + i;
    input.value = value || 'N/A';
    input.style.textAlign = "right";
    //input.disabled = true; // Disabled as per your original code

    if(namePrefix == "nom_qunt_mt")
    {
    	input.readOnly=true;
    }
    
    if(namePrefix == "nom_qunt_mmbtu")
    {
    	input.setAttribute("onchange", 
    	        "negNumber(this);checkNumber1(this,9,2);calcRemainBlncQty('" + i + 
    	        "');document.getElementById('nom_qunt_mt_" + j + "_" + i + 
    	        "').value=getMtValueOFMmbtu(this,'" + j + "','" + i + "');"
    	    );
    }
    
    if (namePrefix === "next_avl_hrs")
    {
        input.setAttribute("maxlength", "3");
        input.setAttribute("onchange", "checkNextAvailHrs(this);");
        input.setAttribute("onblur", "checkNextAvailHrs(this);");

        var inputGroup = document.createElement("div");
        inputGroup.className = "input-group input-group-sm";

        var span = document.createElement("span");
        span.className = "input-group-text";

        var icon = document.createElement("i");
        icon.className = "fa fa-clock-o fa-lg";

        span.appendChild(icon);
        inputGroup.appendChild(input);
        inputGroup.appendChild(span);
        td.appendChild(inputGroup);
    } 
    else
    {
        td.appendChild(input);
    }
    
   // td.appendChild(input);
    return td;
}

function createSelectCell(value, namePrefix, j, i, text,availAfter,truck_reg_no)
{
    var td = document.createElement("td");
    td.align = "center";
    
    var select = document.createElement("select");
    select.className = "form-select form-select-sm";
    select.name = namePrefix + "_" + i;
    select.id = namePrefix + "" + j + "_" + i;

    var sourceSelect = document.getElementById("add_"+namePrefix + "_" + i);

    if (sourceSelect) {
        for (var a = 0; a < sourceSelect.options.length; a++) {
            var option = document.createElement("option");
            option.value = sourceSelect.options[a].value;
            option.textContent = sourceSelect.options[a].textContent;

            if (sourceSelect.options[a].value === value) {
                option.selected = true;
            }

            select.appendChild(option);
        }
    }

    var comp_cd = document.forms[0].comp_cd.value;
    var j_index = document.getElementById("truck_list_" + i).rows.length;
    var no_truck = document.getElementById("truck_list_" + i).rows.length + 1;

    if (namePrefix === "filling_station") {
        select.setAttribute("onchange", "fetchBayDeatils('"+j+"', this.value, '" + comp_cd + "', " + i + ");");
    } else if (namePrefix === "sel_bay") {
        select.setAttribute("onchange", "fetchSlotDeatils('"+j+"', this.value, '" + comp_cd + "', " + i + ");");
    } 
    else if (namePrefix === "sel_slot")
    {
        select.setAttribute("onchange", 
                "setArrivalSlot('" + j + "', this.options[this.selectedIndex].text, " + i + ");" +
                "checkTruckIsAvailable(this, '" + i + "', '" + j_index + "', '" + availAfter + "', '" + truck_reg_no + "');"+
                "checkSlotSelectedElse(this.value, " + i + ", '" + j + "', " + no_truck + ");" 
            );
    }
 
    td.appendChild(select);
    return td;
}

function createDateCell(arrivalDate, j, i) {
    var td = document.createElement("td");
    td.align = "center";

    var inputGroup = document.createElement("div");
    inputGroup.className = "input-group input-group-sm";

    var dateInput = document.createElement("input");
    dateInput.type = "text";
    dateInput.className = "form-control form-control-sm date fmsdtpick";
    dateInput.name = "disp_arrival_dt_" + i;
    dateInput.id = "disp_arrival_dt" + j + "_" + i;
    dateInput.value = arrivalDate || "";
    dateInput.maxLength = "10";
    dateInput.readOnly = true;

    var span = document.createElement("span");
    span.className = "input-group-text";
    var icon = document.createElement("i");
    icon.className = "fa fa-calendar fa-lg";
    span.appendChild(icon);

    inputGroup.appendChild(dateInput);
    inputGroup.appendChild(span);

    td.appendChild(inputGroup);

    // Hidden input
    var hiddenInput = document.createElement("input");
    hiddenInput.type = "hidden";
    hiddenInput.name = "arrival_dt_" + i;
    hiddenInput.id = "arrival_dt" + j + "_" + i;
    hiddenInput.value = arrivalDate || "";

    td.appendChild(hiddenInput);

    return td;
}

function createTimeCell(arrivalTime, j, i) {
    var td = document.createElement("td");
    td.align = "center";

    var inputGroup = document.createElement("div");
    inputGroup.className = "input-group input-group-sm";

    var timeInput = document.createElement("input");
    timeInput.type = "text";
    timeInput.className = "form-control form-control-sm";
    timeInput.name = "arrival_time_" + i;
    timeInput.id = "arrival_time" + j + "_" + i;
    timeInput.value = arrivalTime || "";
    timeInput.maxLength = "6";
    timeInput.readOnly = false;

    var span = document.createElement("span");
    span.className = "input-group-text";
    var icon = document.createElement("i");
    icon.className = "fa fa-clock-o fa-lg";
    span.appendChild(icon);

    inputGroup.appendChild(timeInput);
    inputGroup.appendChild(span);

    td.appendChild(inputGroup);

    return td;
}

function createTextareaCell(value, namePrefix, j, i) {
    var td = document.createElement("td");
    td.align = "center";
    
    var textarea = document.createElement("textarea");
    textarea.className = "form-control";
    textarea.name = namePrefix + "_" + i;
    textarea.id = namePrefix + ""+j+"_"  + i;
    textarea.cols = 30;
    textarea.rows = 1;
    textarea.maxLength = 500;
    //textarea.disabled = true;
    textarea.textContent = value || '';

    td.appendChild(textarea);
    return td;
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_Dlng_ContractMgmt" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String nomination_freq=request.getParameter("nomination_freq")==null?"W":request.getParameter("nomination_freq");

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");

String st_dt = request.getParameter("st_dt")==null?"":request.getParameter("st_dt");
String en_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");

String gas_dt = request.getParameter("gas_dt")==null?sysdate:request.getParameter("gas_dt");

cont_mgmt.setCallFlag("PERIODIC_BUYER_NOM");
cont_mgmt.setCounterparty_cd(counterparty_cd);
cont_mgmt.setAgmt_no(agmt_no);
cont_mgmt.setAgmt_rev_no(agmt_rev_no);
cont_mgmt.setCont_no(cont_no);
cont_mgmt.setCont_rev_no(cont_rev_no);
cont_mgmt.setContract_type(contract_type);
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setGas_dt(gas_dt);
cont_mgmt.setNomination_freq(nomination_freq);
cont_mgmt.setCargo_no(cargo_no);
cont_mgmt.init();

String gcv="9802.80";
String ncv="8831.35";

Vector VTRANSPORTER_CD = cont_mgmt.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = cont_mgmt.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_PLANT_SEQ = cont_mgmt.getVTRANSPORTER_PLANT_SEQ();
Vector VTRANSPORTER_PLANT_ABBR = cont_mgmt.getVTRANSPORTER_PLANT_ABBR();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = cont_mgmt.getVCOUNTERPARTY_PLANT_ABBR();
Vector VCOUNTERPATY_STATUS = cont_mgmt.getVCOUNTERPATY_STATUS();

Vector VBU_CD = cont_mgmt.getVBU_CD();
Vector VBU_PLANT_SEQ = cont_mgmt.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = cont_mgmt.getVBU_PLANT_ABBR();
Vector VTRUCK_LINKED = cont_mgmt.getVTRUCK_LINKED();

Vector VGAS_DT = cont_mgmt.getVGAS_DT();

Vector VNOM_REV_NO = cont_mgmt.getVNOM_REV_NO();
Vector VGEN_TIME = cont_mgmt.getVGEN_TIME();
Vector VGEN_DT = cont_mgmt.getVGEN_DT();
Vector VBASE = cont_mgmt.getVBASE();
Vector VGCV = cont_mgmt.getVGCV();
Vector VNCV = cont_mgmt.getVNCV();
Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();
Vector VQTY_SCM = cont_mgmt.getVQTY_SCM();
Vector VQTY_MT = cont_mgmt.getVQTY_MT();
Vector VNOM_COLOR = cont_mgmt.getVNOM_COLOR();
Vector VGAS_DATE = cont_mgmt.getVGAS_DATE();
Vector VDCQ = cont_mgmt.getVDCQ();
Vector VDCQ_MT = cont_mgmt.getVDCQ_MT();
Vector VMDCQ_QTY = cont_mgmt.getVMDCQ_QTY();
Vector VINDEX = cont_mgmt.getVINDEX();
Vector VINDEX1 = cont_mgmt.getVINDEX1();
Vector VTRANS_INDEX = cont_mgmt.getVTRANS_INDEX();
Vector VSUB_INDEX = cont_mgmt.getVSUB_INDEX();
Vector VGAS_INDEX = cont_mgmt.getVGAS_INDEX();

Vector VTAX_DTL = cont_mgmt.getVTAX_DTL();
Vector VCUSTOMER_CODE = cont_mgmt.getVCUSTOMER_CODE();
Vector VNOM_BLOCK = cont_mgmt.getVNOM_BLOCK();
Vector VCARGO_NO = cont_mgmt.getVCARGO_NO();

Vector VSUB_NOM_REV_NO = cont_mgmt.getVSUB_NOM_REV_NO();
Vector VSUB_QTY_MMBTU = cont_mgmt.getVSUB_QTY_MMBTU();
Vector VSUB_QTY_SCM = cont_mgmt.getVSUB_QTY_SCM();
Vector VSUB_CT_REF = cont_mgmt.getVSUB_CT_REF();
Vector VSUB_UTR_REF = cont_mgmt.getVSUB_UTR_REF();
Vector VSUB_IS_EXIST = cont_mgmt.getVSUB_IS_EXIST();
Vector VSUB_SEQ_NO = cont_mgmt.getVSUB_SEQ_NO();
Vector VSUB_NOM_COLOR = cont_mgmt.getVSUB_NOM_COLOR();

Vector VNOM_SF_ID = cont_mgmt.getVNOM_SF_ID(); //SF ID
Vector VSUB_NOM_SF_ID = cont_mgmt.getVSUB_NOM_SF_ID(); //SF ID for Sub lines

Vector VROWSPAN = cont_mgmt.getVROWSPAN();
Vector VENTRY_RANGE = cont_mgmt.getVENTRY_RANGE();

Vector VMST_TRUCK_TRANS_CD = cont_mgmt.getVMST_TRUCK_TRANS_CD();
Vector VMST_TRUCK_CD = cont_mgmt.getVMST_TRUCK_CD();
Vector VMST_TRUCK_REG_NUM = cont_mgmt.getVMST_TRUCK_REG_NUM();
Vector VMST_FILLST_CD = cont_mgmt.getVMST_FILLST_CD();
Vector VMST_FILLST_NM = cont_mgmt.getVMST_FILLST_NM();
Vector VMST_FILLST_ABBR = cont_mgmt.getVMST_FILLST_ABBR();

Vector VTOTAL_TRUCK_TRANS_CD = cont_mgmt.getVTOTAL_TRUCK_TRANS_CD();
Vector VTOTAL_TRUCK_CD = cont_mgmt.getVTOTAL_TRUCK_CD();
Vector VTOTAL_TRUCK_REG_NUM = cont_mgmt.getVTOTAL_TRUCK_REG_NUM();
Vector VTOTAL_TRUCK_VOL_M3 = cont_mgmt.getVTOTAL_TRUCK_VOL_M3();
Vector VTOTAL_TRUCK_VOL_MT = cont_mgmt.getVTOTAL_TRUCK_VOL_MT();
Vector VTOTAL_TRUCK_LOAD_CAP = cont_mgmt.getVTOTAL_TRUCK_LOAD_CAP();

Vector VTOTAL_QTY_MMBTU = cont_mgmt.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_MT = cont_mgmt.getVTOTAL_QTY_MT();
Vector VTOTAL_FILL_STATION_CD = cont_mgmt.getVTOTAL_FILL_STATION_CD();
Vector VTOTAL_BAY_CD = cont_mgmt.getVTOTAL_BAY_CD();
Vector VTOTAL_SLOT_START_TIME = cont_mgmt.getVTOTAL_SLOT_START_TIME();
Vector VTOTAL_SLOT_END_TIME = cont_mgmt.getVTOTAL_SLOT_END_TIME();
Vector VTOTAL_ARRIVAL_DT = cont_mgmt.getVTOTAL_ARRIVAL_DT();
Vector VTOTAL_ARRIVAL_TIME = cont_mgmt.getVTOTAL_ARRIVAL_TIME();
Vector VTOTAL_NEXT_AVAIL_HRS = cont_mgmt.getVTOTAL_NEXT_AVAIL_HRS();
Vector VTOTAL_REMARK = cont_mgmt.getVTOTAL_REMARK();
Vector VTOTAL_AVAIL_DT = cont_mgmt.getVTOTAL_AVAIL_DT();
Vector VTOTAL_NOM_BLOCK = cont_mgmt.getVTOTAL_NOM_BLOCK();

Vector VALLOCATED_MMBTU = cont_mgmt.getVALLOCATED_MMBTU();

String counterparty_nm = cont_mgmt.getCounterparty_nm();
String start_dt = cont_mgmt.getStart_dt();
String end_dt = cont_mgmt.getEnd_dt();
String cont_name = cont_mgmt.getCont_name();
String dcq = cont_mgmt.getDcq();
String mdcq_percentage = cont_mgmt.getMdcq_percentage();
String tcq = cont_mgmt.getTcq();
String cont_ref = cont_mgmt.getCont_ref();
String dis_mapp = cont_mgmt.getDis_mapp();

double totalBuyerNom=cont_mgmt.getTotalBuyerNom();

if(mdcq_percentage.equals("")){
	mdcq_percentage="100";
}
if(dcq.equals("")){
	dcq="0";
}

String displayContNm="";
if(!start_dt.equals("") && !end_dt.equals(""))
{
	displayContNm=dis_mapp+" ("+cont_ref+")  ("+start_dt+" - "+end_dt+")";
}

String gen_dt="";
String gen_time="";
String dateTime = cont_mgmt.getDateTime();
if(gen_dt.equals(""))
{
	String split[] = dateTime.split(" ");
	gen_dt = split[0];
	gen_time = split[1];
}

int count = utildate.getDays(st_dt, gas_dt);
int count1 = utildate.getDays(en_dt, gas_dt);

if(count > 0 && count1 > 0)
{
	gas_dt=start_dt;
}
else if(count < 0 && count1 <= 0)
{
	gas_dt = end_dt;
}

Vector VTEMP_TOTAL_NOM_BLOCK = new Vector();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Dlng_ContractMgmt">

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
							DLNG 
							<%if(nomination_freq.equals("W")){%>Weekly
	    					<%}else if(nomination_freq.equals("M")){%>Monthly
	    					<%}else if(nomination_freq.equals("F")){%> Fortnightly
	    					<%} %> Buyer Nomination
						</div>
						<div class="btn-group">
							<select class="btn btn-outline-secondary btngrp btnactive" name="nomination_freq" onchange="refresh();">
								<option value="M">Monthly</option>
				    			<option value="W">Weekly</option>
				    			<option value="F">Fortnightly</option>
				    		</select>
						</div>
						<script>
							document.forms[0].nomination_freq.value="<%=nomination_freq%>"
						</script>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label">
				    					<b>Gas <%if(nomination_freq.equals("W")){%>week
				    					<%}else if(nomination_freq.equals("M")){%>month
				    					<%}else if(nomination_freq.equals("F")){%> fortnight
				    					<%} %> of<span class="s-red">*</span></b></label>
				    			</div>
				    			<div class="col">
				    				<div class="input-group input-group-sm" >
					      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="gas_dt" id="gas_dt" value="<%=gas_dt%>" maxLength="10" 
					      				onchange="validateDate(this);refresh();">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      				<input type="hidden" name="tmp_gas_dt" value="<%=gas_dt%>">
				      				</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    			<!-- <label class="form-label"><b>Contract No<span class="s-red">*</span></b></label> -->
				    			<input type="button" class="btn rounded-pill btn-info btn-sm" value="Select Contract" onclick="openContList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<textarea class="form-control" rows="4" readonly style="font-weight:bold;">Counterparty : <%=counterparty_nm %>&#13;&#10;Deal Map : <%=dis_mapp%>&#13;&#10;Cont/Trade Ref : <%=cont_ref%>&#13;&#10;Conatrct Duration : <%=start_dt%> - <%=end_dt%></textarea>
				    				<%-- <input type="text" class="form-control form-control-sm" name="cont_name" value="<%=displayContNm%>" maxLength="50" readOnly style="font-weight:bold;"> --%>
				    				<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>" maxLength="6" readOnly>
				      				<input type="hidden" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" name="cont_no" value="<%=cont_no%>" maxLength="6" readOnly>
				      				<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" name="cargo_no" value="<%=cargo_no%>">
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
      			<%if(!counterparty_cd.equals("") && !counterparty_cd.equals("0")){ %>
      				<%int j=0,k=0,l=0,m=0,p=0,q=0,w=0,y=0;%>
      				<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr style="background:#bce6ff;color:#0c63e4;">
										<th colspan="15">
											<div class="row m-b-5" style="background:#bce6ff;color:#0c63e4;">
												<div class="col-sm-0 col-xs-3 col-md-3">
							      				</div>	
							      				<div class="col-sm-6 col-xs-3 col-md-6">
									    			<div class="form-group row justify-content-center" >
														<div class="col-auto">
															<label class="form-label"><b>
															<input type="radio" name="rd<%=j%>" id="rd1_<%=j%>" onclick="updateGcvNcv('<%=j%>')" checked>&nbsp;GCV : 
															</b></label>
														</div>
									    				<div class="col-auto">
									    					<input type="text" class="form-control form-control-sm" name="grid_gcv" id="grid_gcv_<%=j%>" value="<%=gcv%>" 
									    					style="width:80px;text-align:right;" onblur="checkNumber1(this,9,4);updateGcvNcv('<%=j%>');">
									    				</div>
									    				<div class="col-auto">
															<label class="form-label"><b>KCal/SCM</b></label>
														</div>
									    				<div class="col-auto">
									    					<label class="form-label"><b>
									    					<input type="radio" name="rd<%=j%>" id="rd2_<%=j%>" onclick="updateGcvNcv('<%=j%>')">&nbsp;NCV : 
									    					</b></label>
									    				</div>
									    				<div class="col-auto">
									    					<input type="text" class="form-control form-control-sm" name="grid_ncv" id="grid_ncv_<%=j%>" value="<%=ncv%>" 
									    					style="width:80px;text-align:right;" onblur="checkNumber1(this,9,4);updateGcvNcv('<%=j%>');">
									    				</div>
									    				<div class="col-auto">
															<label class="form-label"><b>KCal/SCM</b></label>
														</div>
									    			</div>
									    		</div>
									    		<div class="col-sm-0 col-xs-3 col-md-3">
							      				</div>
							      			</div>
										</th>
									</tr>
									<tr>
										<th rowspan="2">Gas Date</th>
										<th rowspan="2"><input class="form-check-input" type="checkbox" name="chkAll" onclick="checkAll(this,'<%=VENTRY_RANGE.elementAt(j)%>','<%=j%>');"></th>
										<th rowspan="2">Business Unit</th>
										<th rowspan="2">Customer Plant</th>
										<th rowspan="2">Tax</th>
										<th colspan="2">DCQ</th>
										<th rowspan="2" style="background: #000066; color: white;">Supplied Qty</th>
										<th colspan="3">Nomination Qty</th>
										<th rowspan="2">Link Truck</th>
										<th rowspan="2">Rev#</th>
										<th rowspan="2">SF ID</th>
										<th rowspan="2">Gen Date:Time</th>
										<!-- <th>Calorific Value Base<br>KCal/SCM</th> -->
									</tr>
									<tr>
										<th>MMBTU</th>
										<th>MT</th>
										<th>MMBTU</th>
										<th>SCM</th>
										<th>MT</th>
									</tr>
								</thead>
								<tbody>
									<%if(1>0)
									{ 
										y=0;
										for(w=w; w<VGAS_DT.size(); w++) 
										{
											String temp_gas_dt=""+VGAS_DT.elementAt(w);
											String rowspan=""+VROWSPAN.elementAt(w);
											
											int gas_index=Integer.parseInt(""+VGAS_INDEX.elementAt(w));
											y+=1;
											
											m=0;%>
											<%for(l=l; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
											{ 
												int index1=Integer.parseInt(""+VINDEX1.elementAt(l));
												m+=1;
											%>
												<tr>
													<%if(m==1){ %>
													<td align="center" rowspan="<%=rowspan%>"
													>
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
													<td align="center" valign="middle"
													<%
														VTEMP_TOTAL_NOM_BLOCK=(Vector) VTOTAL_NOM_BLOCK.elementAt(l);
														if(VTEMP_TOTAL_NOM_BLOCK.contains("Y")&& (contract_type.equals("O") || contract_type.equals("Q"))) {%>style="background: #df9fbf;" title="Invoice Generated!"<%} %>
													>
														<input type="checkbox" class="form-check-input" name="chk" id="chk<%=l%>" 
														onclick="setEnableDisabled(this,'<%=l%>');calculateSCM('<%=j%>','<%=l%>');">
														<input type="hidden" name="index1" id="index1<%=l%>" value="<%=index1%>" disabled>
														<input type="hidden" name="index" id="index_<%=l%>" value="<%=l%>" disabled>
														<input type="hidden" name="nom_block" id="nom_block<%=l%>" value="<%//=VNOM_BLOCK.elementAt(l)%>" disabled>
														<input type="hidden" name="week_gas_dt" id="week_gas_dt<%=l%>" value="<%=temp_gas_dt%>" disabled>
														<input type="hidden" name="isRowInvoiced" id="isRowInvoiced<%=l%>" 
														<%if(VTEMP_TOTAL_NOM_BLOCK.contains("Y") && (contract_type.equals("O") || contract_type.equals("Q"))) {%>
														value="Y"<%}else{ %>value="N"<%} %>
														>
													</td>
													<td align="center">
														<%=VBU_PLANT_ABBR.elementAt(l)%>
														<input type="hidden" name="bu_plant_seq" id="bu_plant_seq<%=l%>" value="<%=VBU_PLANT_SEQ.elementAt(l)%>" disabled>
														<input type="hidden" name="trans_cd" id="trans_cd<%=l%>" value="<%//=trans_cd%>" disabled>
									      				<input type="hidden" name="trans_plant_seq" id="trans_plant_seq<%=l%>" value="<%//=trans_plant_seq%>" disabled>
									      			</td>
													<td align="center">
														<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%>
														<%if(!VCUSTOMER_CODE.elementAt(l).equals("")){%>
														<br><font style="background:#ADD8E6;"><%=VCUSTOMER_CODE.elementAt(l)%></font>
														<%} %>
														<input type="hidden" name="plant_seq" id="plant_seq<%=l%>" value="<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(l)%>" disabled>
														<input type="hidden" name="cp_status" id="cp_status<%=l%>" value="<%=VCOUNTERPATY_STATUS.elementAt(l)%>" disabled>
													</td>
													<td>
														<%=VTAX_DTL.elementAt(l)%>
													</td>
													<td align="right">
														<%=VDCQ.elementAt(l)%>
														<input type="hidden" value="<%=VDCQ.elementAt(l)%>" name="dcq" id="dcq<%=l%>">
														<input type="hidden" value="<%=VMDCQ_QTY.elementAt(l)%>" name="mdcq_qty" id="mdcq_qty<%=l%>">
													</td>
													<td align="right">
														<%=VDCQ_MT.elementAt(l)%>
														<input type="hidden" value="<%=VDCQ_MT.elementAt(l)%>" name="dcq" id="dcq<%=l%>">
													</td>
													<td align="right" style="background: #b3f0ff;">
														<%=VALLOCATED_MMBTU.elementAt(l)%>
													</td>
													<td align="center">
														<div style="width:100px;">
															<input type="text" class="form-control form-control-sm" name="qty_mmbtu" id="qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" 
															style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" 
															<%-- onpaste="doPaste('<%=j%>','<%=l%>');"  --%>
															onblur="negNumber(this);checkNumber1(this,9,2);checkQty('<%=l%>');calculateSCM('<%=j%>','<%=l%>');calculateMT('<%=l%>');" disabled> <%-- checkTCQ('<%=l%>'); --%>
															<input type="hidden" class="form-control form-control-sm" name="tmp_qty_mmbtu" id="tmp_qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" style="text-align:right" disabled>
														</div>
													</td>
													<td align="center">
														<div style="width:100px;">
															<input type="text" class="form-control form-control-sm" name="qty_scm" id="qty_scm<%=l%>" value="<%=VQTY_SCM.elementAt(l)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
														</div>
													</td>
													<td align="center">
														<div style="width:100px;">
															<input type="text" class="form-control form-control-sm" name="qty_mt" id="qty_mt<%=l%>" value="<%=VQTY_MT.elementAt(l)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
														</div>
													</td>																				
													<td valign="middle" align="center">
													<%-- <i id="truck_icon_<%=l%>"
													<%if(VTRUCK_LINKED.elementAt(l).equals("Y")){%>
														style="pointer-events: none;color:#00cc00" title="Linked Truck" 
													<%}else{ %>
														style="pointer-events: none;color:#997300" title="Link Truck" 				
													<%} %>
													class="fa fa-truck fa-2x fa-flip-horizontal" aria-hidden="true" 
													onclick="LinkTruck('<%=l%>','<%=dis_mapp%>','<%=gas_dt%>','<%=owner_cd%>',
													'<%=counterparty_cd%>','<%=agmt_no%>','<%=cont_no%>',
													'<%=contract_type%>','<%=agmt_rev_no%>',
													'<%=cont_rev_no%>','<%=cont_ref%>','<%=counterparty_nm%>'
													);"></i> --%>
													
													<span class="icon-wrapper" 
													onclick="LinkTruck('<%=l%>','<%=dis_mapp%>','<%=temp_gas_dt%>','<%=owner_cd%>',
														'<%=counterparty_cd%>','<%=agmt_no%>','<%=cont_no%>',
														'<%=contract_type%>','<%=agmt_rev_no%>',
														'<%=cont_rev_no%>','<%=cont_ref%>','<%=counterparty_nm%>'
														);"
													>
													  <i id="truck_icon_<%=l%>"
														<%if(VTRUCK_LINKED.elementAt(l).equals("Y")){%>
															style="pointer-events: none;color:#00cc00" title="Linked Truck" 
														<%}else{ %>
															style="pointer-events: none;color:#997300" title="Link Truck" 				
														<%} %>
														class="fa fa-truck fa-2x fa-flip-horizontal" aria-hidden="true" 
														></i>
														<i class="fa fa-eye"></i>
													</span>
													</td>
													<td align="center"><%=VNOM_REV_NO.elementAt(l)%></td>
													<td align="center">
													<%-- <%if(index1==0 && !VNOM_SF_ID.elementAt(l).equals("")){ %> --%>
														<div class="row m-b-5">
															<div class="col">
																<input type="button" class="btn btn-sm config_btn" id="" title="<%//=VNOM_SF_ID.elementAt(l)%>" style="display: none; border-color: #ffb3ff; background-color: #ffb3ff;" value="SF">
															</div>																							
														</div>
													<%-- <%} %> --%>
														<input type="hidden" name="sf_id" id="sf_id<%=l%>" value="<%//=VNOM_SF_ID.elementAt(l)%>" disabled>
													</td>
													<td align="center">
														<div style="width:220px;">
															<div class="row m-b-5">
																<div class="col">
																	<div class="input-group input-group-sm">
												      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="gen_dt" id="gen_dt<%=l%>" value="<%=VGEN_DT.elementAt(l)%>" maxLength="10" 
												      					style="background:<%=VNOM_COLOR.elementAt(l)%>"
												      					onblur="validateDate(this);" onchange="validateDate(this);" disabled>
												      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
												      				</div>
											      				</div>
											      				<div class="col">
												      				<div class="input-group input-group-sm" >
											      						<input type="text" class="form-control form-control-sm" name="gen_time" id="gen_time<%=l%>" value="<%=VGEN_TIME.elementAt(l)%>" maxLength="5" 
											      						style="width:15px;background:<%=VNOM_COLOR.elementAt(l)%>"
											      						onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off" disabled>
											      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
										      						</div>
										      						<input type="hidden" class="form-control form-control-sm" name="gcv" id="gcv<%=l%>" value="<%=VGCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" disabled>
									      							<input type="hidden" class="form-control form-control-sm" name="ncv" id="ncv<%=l%>" value="<%=VNCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" disabled>
									      							<input type="hidden" name="base" id="base<%=l%>" value="<%=VBASE.elementAt(l)%>" disabled>
									      						</div>
									      					</div>
									      				</div>
													</td>
													<%-- <td align="center">
														<div style="width:300px;">
															<div class="row m-b-5">
																<div class="col">
																	<input type="radio" name="rd<%=l%>" id="rd1<%=l%>" onclick="calculateSCM('<%=j%>','<%=l%>');" <%if(VBASE.elementAt(l).equals("GCV")){ %>checked<%} %> disabled>&nbsp;GCV
											      				</div>
											      				<div class="col">
											      					<input type="text" class="form-control form-control-sm" name="gcv" id="gcv<%=l%>" value="<%=VGCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);calculateSCM('<%=j%>','<%=l%>');" disabled>
											      				</div>
											      				<div class="col">
											      					<input type="radio" name="rd<%=l%>" id="rd2<%=l%>" onclick="calculateSCM('<%=j%>','<%=l%>');" <%if(VBASE.elementAt(l).equals("NCV")){ %>checked<%} %> disabled>&nbsp;NCV 
											      				</div>
											      				<div class="col">
												      				<input type="text" class="form-control form-control-sm" name="ncv" id="ncv<%=l%>" value="<%=VNCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);calculateSCM('<%=j%>','<%=l%>');" disabled>
									      						</div>
									      						<input type="hidden" name="base" id="base<%=l%>" value="<%=VBASE.elementAt(l)%>" disabled>
									      					</div>
									      				</div>
													</td>--%>
												</tr>
												<%if(m==gas_index)
												{%>
													<%l=l+1;
													break;
												}%>
											<%} %>
											<%-- <%if(y==sub_index)
											{%>
												<%w=w+1;
												break;
											}%> --%>
										<%} %>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
				<%}else{ %>
					<div align="center">
						<%=utilmsg.infoMessage("<b>Select Contract Detail!</b>")%>
					</div>
				<%} %>
				</div>
				<%if(!counterparty_cd.equals("") && !counterparty_cd.equals("0")){ %>
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
				<%}%>
			</div>
		</div>
	</div>
</div>
<%for(int i=0; i<VCOUNTERPARTY_PLANT_SEQ.size(); i++)
{
	Vector VTEMP_MST_TRUCK_TRANS_CD = new Vector();
	Vector VTEMP_MST_TRUCK_CD = new Vector();
	Vector VTEMP_MST_TRUCK_REG_NUM = new Vector();
	Vector VTEMP_MST_FILLST_CD = new Vector();
	Vector VTEMP_MST_FILLST_NM = new Vector();
	Vector VTEMP_MST_FILLST_ABBR = new Vector();

	Vector VTEMP_TRUCK_TRANS_CD = new Vector();
	Vector VTEMP_TRUCK_CD = new Vector();
	Vector VTEMP_TRUCK_REG_NUM = new Vector();
	Vector VTEMP_TRUCK_VOL_M3= new Vector();
	Vector VTEMP_TRUCK_VOL_MT = new Vector();
	Vector VTEMP_TRUCK_LOAD_CAP = new Vector();
	
	Vector VTEMP_SUM_QTY_MMBTU = new Vector();
	
	Vector VTEMP_QTY_MMBTU = new Vector();	
	Vector<Double> VTEMP_DOUBLE_QTY_MMBTU = new Vector();	
	Vector VTEMP_QTY_MT = new Vector();
	Vector VTEMP_FILL_STATION_CD = new Vector();
	Vector VTEMP_BAY_CD = new Vector();
	Vector VTEMP_SLOT_START_TIME = new Vector();
	Vector VTEMP_SLOT_END_TIME = new Vector();
	Vector VTEMP_ARRIVAL_DT = new Vector();
	Vector VTEMP_ARRIVAL_TIME = new Vector();
	Vector VTEMP_NEXT_AVAIL_HRS = new Vector();
	Vector VTEMP_REMARK = new Vector();
	Vector VTEMP_AVAIL_DT= new Vector();
	Vector VTEMP_NOM_BLOCK = new Vector();
	
	VTEMP_MST_TRUCK_TRANS_CD=(Vector) VMST_TRUCK_TRANS_CD.elementAt(i);
	VTEMP_MST_TRUCK_CD=(Vector) VMST_TRUCK_CD.elementAt(i);
	VTEMP_MST_TRUCK_REG_NUM=(Vector) VMST_TRUCK_REG_NUM.elementAt(i);
	VTEMP_MST_FILLST_CD=(Vector) VMST_FILLST_CD.elementAt(i);
	VTEMP_MST_FILLST_NM=(Vector) VMST_FILLST_NM.elementAt(i);
	VTEMP_MST_FILLST_ABBR=(Vector) VMST_FILLST_ABBR.elementAt(i);

	VTEMP_TRUCK_TRANS_CD=(Vector) VTOTAL_TRUCK_TRANS_CD.elementAt(i);
	VTEMP_TRUCK_CD=(Vector) VTOTAL_TRUCK_CD.elementAt(i);
	VTEMP_TRUCK_REG_NUM=(Vector) VTOTAL_TRUCK_REG_NUM.elementAt(i);
	VTEMP_TRUCK_VOL_M3=(Vector) VTOTAL_TRUCK_VOL_M3.elementAt(i);
	VTEMP_TRUCK_VOL_MT=(Vector) VTOTAL_TRUCK_VOL_MT.elementAt(i);
	VTEMP_TRUCK_LOAD_CAP=(Vector) VTOTAL_TRUCK_LOAD_CAP.elementAt(i);

	VTEMP_QTY_MMBTU=(Vector) VTOTAL_QTY_MMBTU.elementAt(i);
	VTEMP_DOUBLE_QTY_MMBTU=(Vector) VTOTAL_QTY_MMBTU.elementAt(i);
	VTEMP_QTY_MT=(Vector) VTOTAL_QTY_MT.elementAt(i);
	VTEMP_FILL_STATION_CD=(Vector) VTOTAL_FILL_STATION_CD.elementAt(i);
	VTEMP_BAY_CD=(Vector) VTOTAL_BAY_CD.elementAt(i);
	VTEMP_SLOT_START_TIME=(Vector) VTOTAL_SLOT_START_TIME.elementAt(i);
	VTEMP_SLOT_END_TIME=(Vector) VTOTAL_SLOT_END_TIME.elementAt(i);
	VTEMP_ARRIVAL_DT=(Vector) VTOTAL_ARRIVAL_DT.elementAt(i);
	VTEMP_ARRIVAL_TIME=(Vector) VTOTAL_ARRIVAL_TIME.elementAt(i);
	VTEMP_NEXT_AVAIL_HRS=(Vector) VTOTAL_NEXT_AVAIL_HRS.elementAt(i);
	VTEMP_REMARK=(Vector) VTOTAL_REMARK.elementAt(i);
	VTEMP_AVAIL_DT=(Vector) VTOTAL_AVAIL_DT.elementAt(i);
	VTEMP_NOM_BLOCK=(Vector) VTOTAL_NOM_BLOCK.elementAt(i);

	double total_qty_sum = 0.0;
	for (Object obj : VTEMP_DOUBLE_QTY_MMBTU) {
        if (obj == null || obj.toString().trim().isEmpty()) {
            total_qty_sum += 0.0;
        } else {
            try {
                total_qty_sum += Double.parseDouble(obj.toString());
            } catch (NumberFormatException e) {
                // Optional: log or handle invalid values
                total_qty_sum += 0.0;
            }
        }
    }
%>
<div class="modal fade" id="LinkTruckModal_<%=i %>" data-bs-backdrop="static" data-bs-keyboard="false">
	<!-- <div class="modal-dialog modal-dialog-scrollable modal-xl"> -->
	<div class="modal-dialog modal-fullscreen">
   		<div class="modal-content">
			<div class="modal-header cdheader">
	    		<div class="topheader" id="link_truck_header">
					Link Truck ()
				</div>
	    		<input type="button" class="btn-close" data-bs-dismiss="modal" onclick="resetTruckToTable('<%=i%>');">
	  		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Total Quantity</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm" name="total_qunt_mmbtu" id="total_qunt_mmbtu<%=i%>"  value="<%=total_qty_sum %>"  autocomplete="off" readonly style="text-align: right">
	      						<span class="input-group-text"><b>MMBTU</b></span>
	      						<input type="text" class="form-control form-control-sm" name="total_qunt_m3" id="total_qunt_m3<%=i%>"  value=""  autocomplete="off" readonly style="text-align: right">
	      						<span class="input-group-text"><b>M3</b></span>
	      						<input type="text" class="form-control form-control-sm" name="total_qunt_mt" id="total_qunt_mt<%=i%>"  value=""  autocomplete="off" readonly style="text-align: right">
	      						<span class="input-group-text"><b>MT</b></span>
      						</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Balance Quantity</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="col-auto">
			    				<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm" name="balance_qty" id="balance_qty_<%=i%>" value="<%//=dens_material %>" 
		      						autocomplete="off" readonly style="text-align: right">
		      						<span class="input-group-text"><b>MMBTU</b></span>
	      						</div>
			    			</div>
						</div>
					</div>
      			</div>
      			<div class="cdbody" style="display: none">
      				<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Add Truck</label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="add_truck_table_<%=i%>">
								<thead>
									<tr>
										<th rowspan="2">Truck#</th>
										<th colspan="3">Truck Capacity</th>
										<th colspan="2">Truck Nomination Qty</th>
										<th colspan="3">Filling Station Association</th>
										<th colspan="2">Arrival</th>
										<th rowspan="2">Next Available<br>(In Hrs)</th>
										<th rowspan="2">Remarks</th>
									</tr>
									<tr>
										<th>M3</th>
										<th>MT</th>
										<th>MMBTU</th>
										<th>MMBTU</th>
										<th>MT</th>
										<th>Filling Station</th>
										<th>Bay</th>
										<th>Slot</th>
										<th>Date</th>
										<th>Time</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td align="center">
											<div style="width:100px;">
												<select class="form-select form-select-sm" name="sel_truck_<%=i%>" id="sel_truck_<%=i%>" 
												onchange="getTruckCapDtls('<%=i%>',this.value,'<%=gas_dt%>','<%=owner_cd%>',
												'<%=counterparty_cd%>','<%=agmt_no%>','<%=cont_no%>',
												'<%=contract_type%>','<%=agmt_rev_no%>',
												'<%=cont_rev_no%>','<%=gas_dt%>');">
							      					<option value="">--Select--</option>
							      					<%for(int x=0;x<VTEMP_MST_TRUCK_CD.size();x++){ %>
							      					<option value="<%=VTEMP_MST_TRUCK_CD.elementAt(x)%>"><%=VTEMP_MST_TRUCK_REG_NUM.elementAt(x)%></option>
							      					<%} %>
							      				</select>
							      				<input type="hidden" name="add_truck_trans_cd" id="add_truck_trans_cd_<%=i%>" value="">
							      				<input type="hidden" name="add_truck_availAfter" id="add_truck_availAfter_<%=i%>" value="">
							      				<input type="hidden" name="add_truck_reg_no" id="add_truck_reg_no_<%=i%>" value="">
											</div>
										</td>
										<td id="addTruckCapInM3_<%=i %>" align="center">-
										</td>
										<td id="addTruckCapInMt_<%=i %>" align="center">-
										</td>
										<td id="addTruckCapInMmbtu_<%=i %>" align="center">-
										</td>
										<td align="center">
											<div  style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="add_nom_qunt_mmbtu_<%=i%>" id="add_nom_qunt_mmbtu_<%=i%>"  value=""  autocomplete="off"  style="text-align: right"
												onchange="negNumber(this);checkNumber1(this,9,2);document.getElementById('add_nom_qunt_mt_<%=i%>').value=getMtValueOFMmbtu(this,'','<%=i%>');" 
												onblur="negNumber(this);checkNumber1(this,9,2);document.getElementById('add_nom_qunt_mt_<%=i%>').value=getMtValueOFMmbtu(this,'','<%=i%>');">
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="add_nom_qunt_mt_<%=i%>" id="add_nom_qunt_mt_<%=i%>"  value=""  autocomplete="off" readonly style="text-align: right"
												onchange="negNumber(this);checkNumber1(this,9,2);" 
												onblur="negNumber(this);checkNumber1(this,9,2);">
											</div>
										</td>
										<td align="center">
											<div style="width:200px;">
												<select class="form-select form-select-sm" name="add_filling_station_<%=i%>" id="add_filling_station_<%=i%>" 
												onchange="fetchBayDeatils('<%//=j%>',this.value,'<%=owner_cd%>','<%=i%>');">
											   	 	<option value="" selected="selected">--Select--</option>
											   	 	<%for(int x=0;x<VTEMP_MST_FILLST_CD.size();x++){ %>
								      				<option value="<%=VTEMP_MST_FILLST_CD.elementAt(x)%>"><%=VTEMP_MST_FILLST_ABBR.elementAt(x)%>-<%=VTEMP_MST_FILLST_NM.elementAt(x)%></option>
								      				<%} %>
											   	</select>
											</div>
										</td>
										<td align="center">	
											<div style="width:100px;">
												<select class="form-select form-select-sm" name="add_sel_bay_<%=i%>" id="add_sel_bay_<%=i%>"
												onchange="fetchSlotDeatils('<%//=j%>',this.value,'<%=owner_cd%>','<%=i%>');" > <!-- style="pointer-events: none;" -->
											   	 	 <option value="" selected="selected">--Select--</option>
											   	</select>
											</div>
										</td>
										<td align="center">
											<div style="width:200px;">
												<select class="form-select form-select-sm" name="add_sel_slot_<%=i%>" id="add_sel_slot_<%=i%>" 
												 onchange="setArrivalSlot('<%//=j%>',this.options[this.selectedIndex].text,'<%=i%>');
												 checkTruckIsAvailable(this,'<%=i%>','<%//=j%>','<%//=availAfter%>','<%//=VTEMP_TRUCK_REG_NUM.elementAt(j) %>');
												 checkSlotSelectedElse(this.value,'<%=i%>','<%//=j%>','<%=VTEMP_TRUCK_CD.size()%>');"> <!-- style="pointer-events: none;" -->
											   	 	 <option value="" selected="selected" >--Select--</option>
											   	</select>
											</div>
										</td>
										<td align="center">
											<div style="width:110px;">
												<div class="row m-b-5">
													<div class="col">
														<div class="input-group input-group-sm">
									      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="add_disp_arrival_dt" id="add_disp_arrival_dt_<%=i%>" value="<%//=arrival_dt%>" maxLength="10" 
									      					style="background:<%//=VNOM_COLOR.elementAt(l)%>"
									      					onblur="validateDate(this);" onchange="validateDate(this);" readonly>
									      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
									      					<input type="hidden" name="add_arrival_dt_<%=i%>" id="add_arrival_dt_<%=i%>" value="">
									      				</div>
								      				</div>
								      			</div>
								      		</div>
								      	</td>
								      	<td align="center">
											<div style="width:110px;">
												<div class="row m-b-5">
								      				<div class="col">
									      				<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm" name="add_arrival_time_<%=i%>" id="add_arrival_time_<%=i%>" value="<%//=arrival_time%>" maxLength="5" 
								      						style="width:15px;background:<%//=VNOM_COLOR.elementAt(l)%>"
								      						onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
								      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
							      						</div>
						      						</div>
						      					</div>
						      				</div>
										</td>
										<td align="center">	
											<div class="input-group input-group-sm" >
												<input type="text" class="form-control form-control-sm" name="add_next_avl_hrs_<%=i%>" id="add_next_avl_hrs_<%=i%>"  value="<%//=next_avl_hrs%>"  maxlength="3"
												onchange="checkNextAvailHrs(this);"
												onblur="checkNextAvailHrs(this);" >
												<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
											</div>
										</td>
										<td align="center">
						      				<textarea class="form-control" name="add_truck_remark_<%=i%>" id="add_truck_remark_<%=i%>" cols="30" rows="1" maxlength="500" ><%//=truck_remark %></textarea>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					&nbsp;
					<div class="d-flex justify-content-between" style="display: none">
						<input type="button" class="btn btn-warning com-btn" value="Reset" id="resetTruckBtn_<%=i%>" onclick="resetTruckToTable('<%=i%>')">
						<%if(write_access.equals("Y")){ %>
						<input 
						<%if(VTEMP_NOM_BLOCK.contains("Y") && (contract_type.equals("O") || contract_type.equals("Q"))){ %>disabled<%} %>
						type="button" class="btn btn-warning com-btn" value="Add" id="addTruckBtn_<%=i%>" onclick="addTruckToTable('<%=i%>')">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
					<%-- <div class="" align="left">
						<input type="button" class="btn btn-warning com-btn" value="Reset" id="resetTruckBtn_<%=i%>" onclick="resetTruckToTable('<%=i%>')">
					</div>
					<div class="" align="right">
						<input type="button" class="btn btn-warning com-btn" value="Add" id="addTruckBtn_<%=i%>" onclick="addTruckToTable('<%=i%>')">
					</div> --%>
      			</div>
      			<div class="cdbody">
      				<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Nominated Trucks</label>
					</div>
      				<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="nominated_truck_table_<%=i%>">
								<thead>
									<tr>
										<th rowspan="2">Select</th>
										<th rowspan="2">Truck#</th>
										<!-- <th rowspan="2">Truck Available From</th> -->
										<th colspan="3">Truck Capacity</th>
										<th colspan="2">Truck Nomination Qty</th>
										<th colspan="3">Filling Station Association</th>
										<th colspan="2">Arrival</th>
										<th rowspan="2">Next Available<br>(In Hrs)</th>
										<th rowspan="2">Remarks</th>
									</tr>
									<tr>
										<th>M3</th>
										<th>MT</th>
										<th>MMBTU</th>
										<th>MMBTU</th>
										<th>MT</th>
										<th>Filling Station</th>
										<th>Bay</th>
										<th>Slot</th>
										<th>Date</th>
										<th>Time</th>
									</tr>
								</thead>
								<tbody id="truck_list_<%=i%>">
								<%if(VTEMP_TRUCK_CD.size()>0){ %>
									<%for(int j=0; j<VTEMP_TRUCK_CD.size();j++){

										Double capInM3 = ((Double.parseDouble(""+VTEMP_TRUCK_VOL_M3.elementAt(j))*Double.parseDouble(""+VTEMP_TRUCK_LOAD_CAP.elementAt(j)))/100); 
										Double capInMT = ((Double.parseDouble(""+VTEMP_TRUCK_VOL_MT.elementAt(j))*Double.parseDouble(""+VTEMP_TRUCK_LOAD_CAP.elementAt(j)))/100); 
										
										Double m3ToMmbtuConv = 0.3531466672;
										
										Double capInMmbtu = (((Double.parseDouble(""+VTEMP_TRUCK_VOL_M3.elementAt(j))/m3ToMmbtuConv)*Double.parseDouble(""+VTEMP_TRUCK_LOAD_CAP.elementAt(j)))/100); 
										double roundedcapInMmbtu = Math.round(capInMmbtu * 100.0) / 100.0;
										
										String nom_qunt_mmbtu = (""+VTEMP_QTY_MMBTU.elementAt(j));
										String nom_qunt_mt = (""+VTEMP_QTY_MT.elementAt(j));
										
										String fill_st = ""+VTEMP_FILL_STATION_CD.elementAt(j);
										String get_bay = ""+VTEMP_BAY_CD.elementAt(j);
										String get_slot_st_time = ""+VTEMP_SLOT_START_TIME.elementAt(j);
										String get_slot_end_time = ""+VTEMP_SLOT_END_TIME.elementAt(j);
										String arrival_dt = ""+VTEMP_ARRIVAL_DT.elementAt(j);
										String arrival_time = ""+VTEMP_ARRIVAL_TIME.elementAt(j);
										String next_avl_hrs = ""+VTEMP_NEXT_AVAIL_HRS.elementAt(j);
										String truck_remark = ""+VTEMP_REMARK.elementAt(j);
										String availAfter = ""+VTEMP_AVAIL_DT.elementAt(j);
									%>
									<tr <%if(VTEMP_NOM_BLOCK.elementAt(j).equals("Y") || VTEMP_NOM_BLOCK.elementAt(j).equals("A")) {%>style="pointer-events: none;"<%}%>>
										<td align="center" valign="middle" 
										<%if(VTEMP_NOM_BLOCK.elementAt(j).equals("A")) {%>style="background: #ffe100; pointer-events: auto" title="Allocated!"<%} %>
										<%if(VTEMP_NOM_BLOCK.elementAt(j).equals("Y")) {%>style="background: #df9fbf; pointer-events: auto" title="Invoice Generated!"<%} %>>
											<input type="checkbox" class="form-check-input" name="truck_chk_<%=i%>" id="truck_chk_<%=j%>_<%=i%>" 
											 onclick="setEnableDisableTruck(this,'<%=j%>','<%=i%>');"
											 <%if(VTEMP_NOM_BLOCK.elementAt(j).equals("Y")) {%>disabled style="pointer-events: none;" checked<%}else{ %>
											 <%if(!VTEMP_QTY_MMBTU.elementAt(j).equals("")){ %>checked<%} %><%} %>><%-- setQuantAccordCap(this,'<%=j%>','<%=i%>');" --%>
											<input type="hidden" name="truck_trans_cd_<%=i%>" id="truck_trans_cd_<%=j%>_<%=i%>" value="<%=VTEMP_TRUCK_TRANS_CD.elementAt(j)%>" disabled>
											<input type="hidden" name="truck_cd_<%=i%>" id="truck_cd_<%=j%>_<%=i%>" value="<%=VTEMP_TRUCK_CD.elementAt(j)%>" disabled>
											<input type="hidden" name="truck_index_<%=i%>" id="truck_index_<%=j%>_<%=i%>" value="<%=j%>" disabled>
										</td>
										<td align="center">
											<%=VTEMP_TRUCK_REG_NUM.elementAt(j) %>
											<input type="hidden" name="truck_availAfter_<%=i%>" id="truck_availAfter_<%=j%>_<%=i%>" value="<%=availAfter%>">
											<input type="hidden" name="truck_reg_no_<%=i%>" id="truck_reg_no_<%=j%>_<%=i%>" value="<%=VTEMP_TRUCK_REG_NUM.elementAt(j)%>">
										</td>
										<td align="center">
											<%=capInM3 %>
											<input type="hidden" name="capInM3" id="capInM3_<%=j%>_<%=i%>" value="<%=capInM3 %>">
										</td>
										<td align="center">
											<%=capInMT %>
											<input type="hidden" name="capInMT" id="capInMT_<%=j%>_<%=i%>" value="<%=capInMT %>">
										</td>
										<td align="center">
											<%=roundedcapInMmbtu %>
											<input type="hidden" name="roundedcapInMmbtu" id="roundedcapInMmbtu_<%=j%>_<%=i%>" value="<%=roundedcapInMmbtu %>">
										</td>
										<td align="center">
											<div  style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="nom_qunt_mmbtu_<%=i%>" id="nom_qunt_mmbtu_<%=j%>_<%=i%>"  value="<%=nom_qunt_mmbtu %>"  autocomplete="off"  
												style="text-align: right;background:<%=VNOM_COLOR.elementAt(i)%>" disabled
												onchange="negNumber(this);checkNumber1(this,9,2);calcRemainBlncQty('<%=i%>');document.getElementById('nom_qunt_mt_<%=j%>_<%=i%>').value=getMtValueOFMmbtu(this,'<%=j%>','<%=i%>');" 
												onblur="negNumber(this);checkNumber1(this,9,2);calcRemainBlncQty('<%=i%>');document.getElementById('nom_qunt_mt_<%=j%>_<%=i%>').value=getMtValueOFMmbtu(this,'<%=j%>','<%=i%>');">
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="nom_qunt_mt_<%=i%>" id="nom_qunt_mt_<%=j%>_<%=i%>"  value="<%=nom_qunt_mt %>"  autocomplete="off" 
												readonly style="text-align: right; background:<%=VNOM_COLOR.elementAt(i)%>"  disabled
												onchange="negNumber(this);checkNumber1(this,9,2);" 
												onblur="negNumber(this);checkNumber1(this,9,2);">
											</div>
										</td>
										<td align="center">
											<div style="width:200px;">
												<select class="form-select form-select-sm" name="filling_station_<%=i%>" id="filling_station<%=j%>_<%=i%>" 
												onchange="fetchBayDeatils('<%=j%>',this.value,'<%=owner_cd%>','<%=i%>');"  disabled 
												style="background:<%=VNOM_COLOR.elementAt(i)%>"> <!-- style="pointer-events: none;" -->
											   	 	 <option value="" selected="selected">--Select--</option>
											   	</select>
											   	<input type="hidden" name="fill_st_<%=i%>" id="fill_st_<%=j%>_<%=i%>"  value="<%=fill_st%>">
											</div>
										</td>
										<td align="center">	
											<div style="width:100px;">
												<select class="form-select form-select-sm" name="sel_bay_<%=i%>" id="sel_bay<%=j%>_<%=i%>"
												onchange="fetchSlotDeatils('<%=j%>',this.value,'<%=owner_cd%>','<%=i%>');"  disabled
												style="background:<%=VNOM_COLOR.elementAt(i)%>"> <!-- style="pointer-events: none;" -->
											   	 	 <option value="" selected="selected">--Select--</option>
											   	</select>
											   	<input type="hidden" name="get_bay_<%=i%>" id="get_bay_<%=j%>_<%=i%>"  value="<%=get_bay%>">
											</div>
										</td>
										<td align="center">
											<div style="width:200px;">
												<select class="form-select form-select-sm" name="sel_slot_<%=i%>" id="sel_slot<%=j%>_<%=i%>" 
												 onchange="setArrivalSlot('<%=j%>',this.options[this.selectedIndex].text,'<%=i%>');
												 checkTruckIsAvailable(this,'<%=i%>','<%=j%>','<%=availAfter%>','<%=VTEMP_TRUCK_REG_NUM.elementAt(j) %>');
												 checkSlotSelectedElse(this.value,'<%=i%>','<%=j%>','<%=VTEMP_TRUCK_CD.size()%>');"  disabled
												 style="background:<%=VNOM_COLOR.elementAt(i)%>"> <!-- style="pointer-events: none;" -->
											   	 	 <option value="" selected="selected" >--Select--</option>
											   	</select>
											   	<input type="hidden" name="get_slot_<%=i%>" id="get_slot_<%=j%>_<%=i%>" value="<%=get_slot_st_time%>-<%=get_slot_end_time%>">
											</div>
										</td>
										<td align="center">
											<div style="width:110px;">
												<div class="row m-b-5">
													<div class="col">
														<div class="input-group input-group-sm">
									      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="disp_arrival_dt" id="disp_arrival_dt<%=j%>_<%=i%>" value="<%=arrival_dt%>" maxLength="10" 
									      					style="background:<%=VNOM_COLOR.elementAt(i)%>"
									      					onblur="validateDate(this);" onchange="validateDate(this);" disabled>
									      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
									      					<input type="hidden" name="arrival_dt_<%=i%>" id="arrival_dt<%=j%>_<%=i%>" value="<%=arrival_dt%>">
									      				</div>
								      				</div>
								      			</div>
								      		</div>
								      	</td>
								      	<td align="center">
											<div style="width:110px;">
												<div class="row m-b-5">
								      				<div class="col">
									      				<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm" name="arrival_time_<%=i%>" id="arrival_time<%=j%>_<%=i%>" value="<%=arrival_time%>" maxLength="5" 
								      						style="width:15px;background:<%=VNOM_COLOR.elementAt(i)%>"
								      						onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off"  disabled>
								      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
							      						</div>
						      						</div>
						      					</div>
						      				</div>
										</td>
										<td align="center">	
											<div class="input-group input-group-sm" >
												<input type="text" class="form-control form-control-sm" name="next_avl_hrs_<%=i%>" id="next_avl_hrs_<%=j%>_<%=i%>"  value="<%=next_avl_hrs%>"  maxlength="3"
												onchange="checkNextAvailHrs(this);"
												disabled
												style="background:<%=VNOM_COLOR.elementAt(i)%>">
												<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
											</div>
										</td>
										<td align="center">
						      				<textarea class="form-control" name="truck_remark_<%=i%>" id="truck_remark<%=j%>_<%=i%>" cols="30" rows="1" maxlength="500"  disabled
						      				style="background:<%=VNOM_COLOR.elementAt(i)%>"><%=truck_remark %></textarea>
										</td>
									</tr>
									<%} %>
								<%}%>
								</tbody>
							</table>
							<%if(VTEMP_TRUCK_CD.size()<=0){ %>
								<div align="center" id="emptyTruckListRow<%=i%>">
									<%=utilmsg.infoMessage("<b>No Nominated Truck Details Available!</b>") %>
								</div>
							<%} %>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter" style="display: none">
        		<div class="" align="right">
					<input 
					<%if(VTEMP_NOM_BLOCK.contains("Y") && (contract_type.equals("O") || contract_type.equals("Q"))){ %>disabled<%} %> 
					type="button" class="btn btn-warning com-btn" value="Submit" onclick="doTruckSubmit('<%=i%>')">
				</div>
      		</div>
      	</div>
	</div>
</div>
<%} %>
<script>
function LinkTruck(index,cont_mapid,gas_dt,comp_cd,counterparty_cd,agmt_no,cont_no,contract_type,agmt_rev_no,cont_rev_no,deal_ref_id,cp_abbr)
{
	var truckModal = document.getElementById('LinkTruckModal_'+index);
	
	var qty_mmbtu = document.getElementById('qty_mmbtu'+index).value;
	var qty_scm = document.getElementById('qty_scm'+index).value;
	var qty_mt = document.getElementById('qty_mt'+index).value;
	
	if(parseInt(qty_mmbtu)>=0 && qty_mmbtu!="" && qty_mmbtu.lenght!=0)
	{
		/* $("#LinkTruckModal_"+index).modal("show");
		
		var modalTitle = truckModal.querySelector('.topheader')

		modalTitle.textContent ='Link Truck : ' +cont_mapid+' ('+deal_ref_id+') [Gas Date: '+ gas_dt+']'; */
		
		var mmbtuToM3 =  document.forms[0].mmbtu_to_m3.value;
		var qty_m3 = round(parseFloat(qty_mmbtu)/parseFloat(mmbtuToM3),2);
		
		document.getElementById('total_qunt_mmbtu'+index).value=qty_mmbtu;
		document.getElementById('total_qunt_m3'+index).value=qty_m3;
		document.getElementById('total_qunt_mt'+index).value=qty_mt;

		$('select[name="filling_station_'+index+'"]').empty().append('<option value="">--Select--</option>');
		
		fetchFillStDeatils(index,comp_cd,counterparty_cd,agmt_no,cont_no,contract_type,agmt_rev_no,cont_rev_no);
		calcRemainBlncQty(index);
		
		/* $("#LinkTruckModal_"+index).modal("show");
		var modalTitle = truckModal.querySelector('.topheader')
		modalTitle.textContent ='Link Truck : ' +cont_mapid+' ('+deal_ref_id+') [Gas Date: '+ gas_dt+']';
		 */
		 
		 document.getElementById("loading").style.visibility = "visible";

		 setTimeout(function () {
		     // Show modal after delay
		     $("#LinkTruckModal_" + index).modal("show");

		     // Set modal title
		     var modalTitle = truckModal.querySelector('.topheader');
		     modalTitle.textContent = 'Link Truck [Gas Date: ' + gas_dt + '] : '+cp_abbr+' - '+ cont_mapid + ' (' + deal_ref_id + ')';

		     // Hide loading indicator
		     document.getElementById("loading").style.visibility = "hidden";

		 }, 3000); // 0.5-second delay
	}
	else
	{
		var rowNo = parseInt(index)+1;
		alert("Enter Energy (MMBTU) for Row - "+rowNo+"!");
	}
}

function getTruckCapDtls(i_index,truck_cd,gas_dt,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, agmt_rev_no, cont_rev_no,gas_date)
{
	if((truck_cd !="" && truck_cd!="0"))
	{
		$.post("../servlet/DB_Dlng_ContractMgmt_Ajax"+ "?truck_cd="+truck_cd+"&gas_dt="+gas_dt+
				"&comp_cd=" + comp_cd +
		            "&counterparty_cd=" + counterparty_cd +
		            "&agmt_no=" + agmt_no +
		            "&cont_no=" + cont_no +
		            "&contract_type=" + contract_type +
		            "&agmt_rev_no=" + agmt_rev_no +
		            "&cont_rev_no=" + cont_rev_no +
		            "&gas_date=" + gas_date +
				"&setCallType=fetchTruckDeatils", function(responseJson) {
			//console.log(responseJson);
			
			$.each(responseJson, function (index, json) {
				$.each(json.TRUCK_DTL, function (i, truckDtl) {
					
			        var truck_vol_m3 =truckDtl.TRUCK_VOL_M3;
			        var truck_vol_mt = truckDtl.TRUCK_VOL_MT;
			        var truck_load_cap = truckDtl.TRUCK_LOAD_CAP;
			        var truck_trans_cd = truckDtl.TRUCK_TRANS_CD;
			        var truck_trans_cd = truckDtl.TRUCK_TRANS_CD;
			        var truck_reg_no = truckDtl.TRUCK_REG_NUM;
			        var truck_availAfter = truckDtl.TRUCK_AVAILAFTER;
			        
			        var m3ToMmbtuConv = document.forms[0]?.m3_to_tonMMbtu?.value || 0.3531466672;
					
			        var capInMmbtu = (((truck_vol_m3) / m3ToMmbtuConv) * truck_load_cap) / 100;
			        var capInMt = (truck_vol_mt * truck_load_cap) / 100;
			        var capInM3 = (truck_vol_m3 * truck_load_cap) / 100;

			        var roundedcapInMmbtu = Math.round(capInMmbtu * 100.0) / 100.0;
			        var roundedcapInMt = Math.round(capInMt * 100.0) / 100.0;
			        var roundedcapInM3= Math.round(capInM3 * 100.0) / 100.0;
			        
			        $("#addTruckCapInM3_" + i_index).text(roundedcapInM3);
					$("#addTruckCapInMt_" + i_index).text(roundedcapInMt);
					$("#addTruckCapInMmbtu_" + i_index).text(roundedcapInMmbtu);
					
					document.getElementById("add_truck_trans_cd_" + i_index).value=truck_trans_cd;
					document.getElementById("add_truck_availAfter_" + i_index).value=truck_availAfter;
					document.getElementById("add_truck_reg_no_" + i_index).value=truck_reg_no;
				});
			});
		});
	}
	else
	{
		$("#addTruckCapInM3_" + i_index).text("-");
		$("#addTruckCapInMt_" + i_index).text("-");
		$("#addTruckCapInMmbtu_" + i_index).text("-");
	}
}

async function fetchFillStDeatils(i_index, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, agmt_rev_no, cont_rev_no) {
    if ((comp_cd !== "" && comp_cd !== "0") &&
        (counterparty_cd !== "" && counterparty_cd !== "0") &&
        (agmt_no !== "") &&
        (cont_no !== "" && cont_no !== "0") &&
        (contract_type !== "")) {

        const j_index_elements = document.getElementsByName('truck_index_' + i_index);

        const url = "../servlet/DB_Dlng_ContractMgmt_Ajax" +
            "?comp_cd=" + comp_cd +
            "&counterparty_cd=" + counterparty_cd +
            "&agmt_no=" + agmt_no +
            "&cont_no=" + cont_no +
            "&contract_type=" + contract_type +
            "&agmt_rev_no=" + agmt_rev_no +
            "&cont_rev_no=" + cont_rev_no +
            "&setCallType=fetchFillStDeatils";

        try {
            const response = await fetch(url, { method: 'POST' });
            const responseJson = await response.json();

            for (const json of responseJson) {
                if (!json.FILLST_DTL) continue;

                for (const fillSt of json.FILLST_DTL) {
                    const optionText = fillSt.STABBR + '-' + fillSt.STNAME;
                    const optionValue = fillSt.STCD;
                    const option = $('<option>', {
                        value: optionValue,
                        text: optionText
                    });

                    const select = $('select[name="filling_station_' + i_index + '"]');
                    if (select.find('option[value="' + optionValue + '"]').length === 0) {
                        select.append(option);
                    }

                    for (let j = 0; j < j_index_elements.length; j++) {
                        const fill_st = document.getElementById('fill_st_' + j + '_' + i_index)?.value;

                        if (fill_st !== "" && fill_st !== undefined) {
                            const selectElement = document.getElementById('filling_station' + j + '_' + i_index);
                            if (selectElement) {
                                selectElement.value = fill_st;

                                await fetchBayDeatils(j, fill_st, comp_cd, i_index); // Awaited correctly

                                const get_bay = document.getElementById('get_bay_' + j + '_' + i_index)?.value;
                                await fetchSlotDeatils(j, get_bay, comp_cd, i_index); // Awaited too
                            } else {
                                selectElement.value = "";
                            }
                        }

                        await delay(100); // Optional delay still applied
                    }
                }
            }

        } catch (error) {
            //console.error("Error fetching fill station details:", error);
        }
    }
}

function fetchBayDeatils(j_index, fillStCd, comp_cd, i_index) {
    return new Promise((resolve, reject) => {
    	
        if (comp_cd !== "" && comp_cd !== "0") {
            $.post("../servlet/DB_Dlng_ContractMgmt_Ajax" +
                "?comp_cd=" + comp_cd + "&fillStCd=" + fillStCd +
                "&setCallType=fetchBayDeatils", function (responseJson) {

                if (j_index === "") {
                    $('#add_sel_bay_' + i_index).empty().append('<option value="">--Select--</option>');
                    $('#add_sel_slot_' + i_index).empty().append('<option value="">--Select--</option>');
                } else {
                    $('#sel_bay' + j_index + "_" + i_index).empty().append('<option value="">--Select--</option>');
                    $('#sel_slot' + j_index + "_" + i_index).empty().append('<option value="">--Select--</option>');
                }

                $.each(responseJson, function (index, json) {
                    $.each(json.BAY_DTL, function (i, baySt) {
                        const option = $('<option>', {
                            value: baySt.BAYCD,
                            text: baySt.BAYNAME
                        });

                        if (j_index === "") {
                            $('#add_sel_bay_' + i_index).append(option);
                        } else {
                            $('#sel_bay' + j_index + '_' + i_index).append(option);

                            const get_bay = document.getElementById('get_bay_' + j_index + '_' + i_index)?.value;
                            const selectElement = document.getElementById('sel_bay' + j_index + '_' + i_index);

                            selectElement.value = get_bay || "";
                        }
                    });
                });

                resolve(); // Finish promise
            }).fail((xhr, status, error) => {
                //console.error("fetchBayDeatils failed:", error);
                reject(error);
            });
        } else {
            resolve(); // No-op but still resolve to keep flow
        }
    });
}


//Utility to convert minutes to HH:mm format (wraps after 24 hours)
function formatTime(minutes) {
  minutes = minutes % 1440; // Normalize to within 0 to 1439
  var hours = Math.floor(minutes / 60);
  var mins = minutes % 60;
  return String(hours).padStart(2, '0') + ':' + String(mins).padStart(2, '0');
}

function fetchSlotDeatils(j_index, bayCd, comp_cd, i_index) {
  return new Promise((resolve, reject) => {
      
  	const gasDateStr = document.forms[0].gas_dt.value;
  	
  	var fillSt="";
  	
  	if (j_index !== "") 
  	{
  		fillSt = document.getElementById('filling_station'+j_index+'_'+i_index).value;
  	}
  	else
  	{
  		fillSt = document.getElementById('add_filling_station'+'_'+i_index).value;
  	}

      if (comp_cd !== "" && comp_cd !== "0") {
          $.post("../servlet/DB_Dlng_ContractMgmt_Ajax" +
              "?comp_cd=" + comp_cd + "&bayCd=" + bayCd + "&gasDt=" + gasDateStr +"&fillSt=" + fillSt +
              "&setCallType=fetchSlotDeatils", function (responseJson) {

              const $slotSelect = j_index === ""
                  ? $('#add_sel_slot_' + i_index)
                  : $('#sel_slot' + j_index + "_" + i_index);

              $slotSelect.empty().append('<option value="">--Select--</option>');

              $.each(responseJson, function (index, json) {
                  $.each(json.BAY_DTL, function (i, baySt) {
                  	
                  	// Populate options
                      //var $slotSelect = $('#sel_slot_' + l_index );
                  	
                  	$slotSelect.append($('<option>', {
                          value: baySt.BAYSLOTVALUE,
                          text: baySt.BAYSLOTTEXT,
                          'data-available': baySt.SLOTAVAILABLE,
                          'data-slotVal': baySt.BAYSLOTVALUE,
                          style: baySt.SLOTAVAILABLE === "N" ? 'color: red;' : ''
                      }));
                  });

                  if (j_index !== "") {
                      var get_slot = document.getElementById('get_slot_' + j_index + '_' + i_index)?.value;
                      var selectElement = document.getElementById('sel_slot' + j_index + '_' + i_index);
                      selectElement.value = get_slot || "";
                      
                   // Add change event to prevent selecting unavailable options
      	            $slotSelect.off('change').on('change', function () {
      	                var selectedOption = this.options[this.selectedIndex];
      	                var isAvailable = selectedOption.getAttribute('data-available') !== "N";
      	                var slotVal = selectedOption.getAttribute('data-slotVal');

      	                if(slotVal !== get_slot)
      	                {
      	                	if (!isAvailable)
      		                {
      		                    alert("This slot is unavailable. Please choose another one.");
      		                    
      		                    if (get_slot != "") 
      		                    {
      		                    	this.value = get_slot; // Reset the selection
      		                    }
      		                    else
      		                    {
      		                    	this.value = ""; // Reset the selection
      		                    }
      		                }
      	                }
      	            });
                  }
                  else
                  {
                  	// Add change event to prevent selecting unavailable options
      	            $slotSelect.off('change').on('change', function () {
      	                var selectedOption = this.options[this.selectedIndex];
      	                var isAvailable = selectedOption.getAttribute('data-available') !== "N";
      	                var slotVal = selectedOption.getAttribute('data-slotVal');

      	                if (!isAvailable)
  		                {
  		                    alert("This slot is unavailable. Please choose another one.");
  		                    
  		                    if (get_slot != "") 
  		                    {
  		                    	this.value = get_slot; // Reset the selection
  		                    }
  		                    else
  		                    {
  		                    	this.value = ""; // Reset the selection
  		                    }
  		                }
      	            });
                  }
              });

              resolve(); // Finish promise
          }).fail((xhr, status, error) => {
              //console.error("fetchSlotDeatils failed:", error);
              reject(error);
          });
      } else {
          resolve(); // Skip and resolve
      }
  });
}


function checkSlotSelectedElse(objVal,i_index,j_index,no_truck)
{
	no_truck = document.getElementById("truck_list_" + i_index).rows.length;
		
	for(var j=0; j<parseInt(no_truck);j++)
	{
		var sel_slot =document.getElementById("sel_slot"+j+"_"+i_index).value;
		var sel_bay =document.getElementById("sel_bay"+j+"_"+i_index).value;
		var filling_station =document.getElementById("filling_station"+j+"_"+i_index).value;
		
		if(j_index=="")
		{
			var fillSt =document.getElementById("add_filling_station_"+i_index).value;
			var bayCd =document.getElementById("add_sel_bay_"+i_index).value;
			
			if(objVal === sel_slot 
					&& filling_station == fillSt
					&& sel_bay == bayCd)
			{
				alert("Slot ("+sel_slot+") is occupied!! \nSelect diffrent slot for selected truck!");
				document.getElementById("add_sel_slot_"+i_index).value="";
				break;
			}
		}
		else
		{
			var fillSt =document.getElementById("filling_station"+j_index+"_"+i_index).value;
			var bayCd =document.getElementById("sel_bay"+j_index+"_"+i_index).value;
			
			if(objVal === sel_slot 
					&& parseInt(j_index) !== parseInt(j) 
					&& filling_station == fillSt
					&& sel_bay == bayCd)
			{
				alert("Slot ("+sel_slot+") is occupied!! \nSelect diffrent slot for selected truck!");
				document.getElementById("sel_slot"+j_index+"_"+i_index).value="";
				break;
			}
		}
	}
}


function checkTruckIsAvailable(slot,i_index,j_index,availAfter,truck_reg_no)
{
	var selectedText =slot.options[slot.selectedIndex].text;
	
	if(j_index=="")
	{
    	availAfter = document.getElementById("add_truck_availAfter"+"_"+i_index).value;
    	truck_reg_no = document.getElementById("add_truck_reg_no"+"_"+i_index).value;
	}
	
	// Extract time and date using regex
    var match = selectedText.match(/(\d{2}:\d{2}) - \d{2}:\d{2} \((\d{2}\/\d{2}\/\d{4})\)/);
    
    if (match) 
    {
        var slotTime = match[1];
        var slotDate = match[2];

        // Combine to form full datetime
        var slotDateTimeStr = slotDate + " " + slotTime;

        // Parse both datetimes into JS Date objects (convert from DD/MM/YYYY)
        var parseDateTime = function (str) {
            var parts = str.split(/[/ :]/); // [dd, MM, yyyy, HH, mm]
            return new Date(parts[2], parts[1] - 1, parts[0], parts[3], parts[4]);
        };

        var availDateTime = parseDateTime(availAfter);
        var slotDateTime = parseDateTime(slotDateTimeStr);

        if (slotDateTime < availDateTime) 
        {
            alert("Truck is not available for this slot. Available after: " + availAfter);
            
            if(j_index=="")
        	{
            	document.getElementById("add_sel_slot"+"_"+i_index).value="";
        	}
        	else
        	{
        		 document.getElementById("sel_slot"+j_index+"_"+i_index).value="";
        	}
        } 
    } 
    else
    {
        alert("Invalid slot format.");
    }
}

function setArrivalSlot(index,selSlotVal,i_index)
{
	if(index=="")
	{
		var disp_arrival_dt =document.getElementById("add_disp_arrival_dt"+"_"+i_index);
		var arrival_dt =document.getElementById("add_arrival_dt"+"_"+i_index);
		var arrival_time =document.getElementById("add_arrival_time"+"_"+i_index);
		
		// Split the slot value to extract start time and date
	    var slotParts = selSlotVal.split(' - ');
	    var startTime = slotParts[0].trim();  // Extract start time
	    var datePart = slotParts[1].split(' (')[1].replace(')', '').trim();  // Extract date

	    // Set the arrival time and arrival date
	    arrival_time.value = startTime;
	    disp_arrival_dt.value = datePart;
	    arrival_dt.value = datePart;
	}
	else
	{
		var disp_arrival_dt =document.getElementById("disp_arrival_dt"+index+"_"+i_index);
		var arrival_dt =document.getElementById("arrival_dt"+index+"_"+i_index);
		var arrival_time =document.getElementById("arrival_time"+index+"_"+i_index);
		
		// Split the slot value to extract start time and date
	    var slotParts = selSlotVal.split(' - ');
	    var startTime = slotParts[0].trim();  // Extract start time
	    var datePart = slotParts[1].split(' (')[1].replace(')', '').trim();  // Extract date

	    // Set the arrival time and arrival date
	    arrival_time.value = startTime;
	    disp_arrival_dt.value = datePart;
	    arrival_dt.value = datePart;
	}
}

function checkNextAvailHrs(hrs) {
    const value = hrs.value.trim();

    // Reject if the value is empty or contains anything other than digits
    if (value === "" || !/^\d+$/.test(value)) {
        alert("Only Integer values are allowed!");
        hrs.value = "";
        return;
    }

    const intValue = parseInt(value, 10);

    if (intValue > 360) {
        alert("Next Available Hrs cannot be greater than 360!");
        hrs.value = "360";
    }
}

function delay(ms) {
	return new Promise(resolve => setTimeout(resolve, ms));
}


function getMtValueOFMmbtu(mmbtu,j_index,i_index)
{
	var convt_mmbtu_to_mt = document.forms[0].convt_mmbtu_to_mt.value; //For Converting MMBTU TO MT...
	
	var mt = parseFloat("0");
	
	if((mmbtu.value!=null && trim(mmbtu.value) !=''))
	{
		mt = ""+round(parseFloat(mmbtu.value) / parseFloat(convt_mmbtu_to_mt),2);
		
		if(isNaN(mt))
		{
			mt.value="";
		}
		else
		{
			mt.value = mt;
		}
	}
	
	return mt;
}

function doTruckSubmit(i_index)
{
	var gas_dt = document.forms[0].gas_dt;
	
	var truck_chk = document.getElementsByName('truck_chk_'+i_index);
	var truck_cd = document.getElementsByName('truck_cd_'+i_index);

	var nom_qunt_mmbtu = document.getElementsByName('nom_qunt_mmbtu_'+i_index);
	var nom_qunt_mt = document.getElementsByName('nom_qunt_mt_'+i_index);
	var filling_station = document.getElementsByName('filling_station_'+i_index);
	var sel_bay = document.getElementsByName('sel_bay_'+i_index);
	var sel_slot = document.getElementsByName('sel_slot_'+i_index);
	var arrival_dt = document.getElementsByName('arrival_dt_'+i_index);
	var arrival_time = document.getElementsByName('arrival_time_'+i_index);
	var next_avl_hrs = document.getElementsByName('next_avl_hrs_'+i_index);
	var balance_qty = document.getElementById("balance_qty_"+i_index);
	
	var gen_dt = document.getElementById("gen_dt"+i_index);
	var gen_time = document.getElementById("gen_time"+i_index);
	
	var msg="";
	var flag=true;
	var qtyFlag=true;
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
	
	if(parseFloat(balance_qty.value) != 0)
	{
		msg+="Balance quantity should be =0!\n";
		flag=false;
	}
	
	if(truck_chk!=null && truck_chk.length!=undefined)
	{
		for(var i=0; i<truck_chk.length; i++)
		{
			if(truck_chk[i].checked)
			{
				chk_count++;
				if(trim(nom_qunt_mmbtu[i].value)=="")
				{
					msg+="Enter Truck Nomination Qty(MMBTU) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(nom_qunt_mt[i].value)=="")
				{
					msg+="Enter Truck Nomination Qty(MT) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(filling_station[i].value)=="" || filling_station[i].value=="0")
				{
					msg+="Select Filling Station for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(sel_bay[i].value)=="" || sel_bay[i].value=="0")
				{
					msg+="Select Bay for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(sel_slot[i].value)=="" || sel_slot[i].value=="0")
				{
					msg+="Select Slot for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(arrival_dt[i].value)=="")
				{
					msg+="Enter Arrival Date for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(arrival_time[i].value)=="")
				{
					msg+="Enter Arrival Time for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(next_avl_hrs[i].value)=="")
				{
					msg+="Enter Next Available Hrs for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
			}
		}
	}
	else
	{
		if(truck_chk.checked)
		{ 	
			chk_count++
			if(trim(nom_qunt_mmbtu.value)=="")
			{
				msg+="Enter Truck Nomination Qty(MMBTU) for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			if(trim(nom_qunt_mt.value)=="")
			{
				msg+="Enter Truck Nomination Qty(MT) for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			if(trim(filling_station.value)=="" || filling_station.value=="0")
			{
				msg+="Select Filling Station for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			if(trim(sel_bay.value)=="" || sel_bay.value=="0")
			{
				msg+="Select Bay for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			if(trim(sel_slot.value)=="" || sel_slot.value=="0")
			{
				msg+="Select Slot for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			if(trim(arrival_dt.value)=="")
			{
				msg+="Enter Arrival Date for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			if(trim(arrival_time.value)=="")
			{
				msg+="Enter Arrival Time for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			if(trim(next_avl_hrs.value)=="")
			{
				msg+="Enter Next Available Hrs for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
		}
	}
	
	if(flag)
	{
		if(parseInt(chk_count) == 0)
		{
			var conf =confirm("No Truck/s are selected, On submission associated trucks will be released!\n\nDo you want to continue?");
		
			if(conf)
			{
				var a = confirm("Do you want to Submit Buyer Nomination?");
				if(a)
				{
					document.getElementById("loading").style.visibility = "visible";
					document.forms[0].submit();
				}
			}
		}
		else
		{
			var a = confirm("Do you want to Submit Buyer Nomination?");
			if(a)
			{
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].submit();
			}
		}
	}
	else
	{
		alert(msg);
	}
}

function calculateMT(index)
{
	var base = document.getElementById("base"+index);
	var gcv = document.getElementById("gcv"+index);
	var ncv = document.getElementById("ncv"+index);
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	var qty_mt = document.getElementById("qty_mt"+index);

	var convt_mmbtu_to_mt = document.forms[0].convt_mmbtu_to_mt.value; //For Converting MMBTU TO MT...
	
	if((qty_mmbtu.value!=null && trim(qty_mmbtu.value) !=''))
	{
		var mt = ""+round(parseFloat(qty_mmbtu.value) / parseFloat(convt_mmbtu_to_mt),2);
		if(isNaN(mt))
		{
			qty_mt.value="";
		}
		else
		{
			qty_mt.value = mt;
		}
	}
}
</script>

<input type="hidden" name="option" value="PERIODIC_BUYER_NOM">
<input type="hidden" name="comp_cd" value="<%=owner_cd%>">
<input type="hidden" name="prev_counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="submit_flag" value="NOM">

<input type="hidden" name="contract_st_dt" value="<%=start_dt%>">
<input type="hidden" name="contract_end_dt" value="<%=end_dt%>">

<input type="hidden" name="totalBuyerNom" value="<%=totalBuyerNom%>">
<input type="hidden" name="tcq" value="<%=tcq%>">

<input  type="hidden" name="mmbtu_to_tons" value="0.025219021687207">
<input type="hidden" name="mmbtu_to_m3" value="23.9">
<input type="hidden" name="m3_to_tonMMbtu" value="0.3531466672">
<input type="hidden" name="convt_mmbtu_to_mt" value="51.5">

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