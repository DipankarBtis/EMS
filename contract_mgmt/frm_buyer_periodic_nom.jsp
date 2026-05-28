<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
		var url = "frm_buyer_periodic_nom.jsp?counterparty_cd="+counterparty_cd+"&u="+u+
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
		newWindow = window.open("frm_nomination_contract_list.jsp?gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+"&nomination_type=B","Supply Contract List","top=10,left=10,width=1300,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_nomination_contract_list.jsp?gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+"&nomination_type=B","Supply Contract List","top=10,left=10,width=1300,height=600,scrollbars=1");
	}
}

function setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_type,cargo_no)
{
	var gas_dt = document.forms[0].gas_dt.value;
	var nomination_freq = document.forms[0].nomination_freq.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_buyer_periodic_nom.jsp?counterparty_cd="+countpty_cd+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&st_dt="+st_dt+"&end_dt="+end_dt+"&gas_dt="+gas_dt+
			"&nomination_freq="+nomination_freq+"&u="+u+"&contract_type="+cont_type+"&cargo_no="+cargo_no;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function checkAll(obj,range,j)
{
	var chk = document.forms[0].chk;
	var nom_block = document.forms[0].nom_block;
	var is_active = document.forms[0].is_active;
	
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
					if(nom_block[i].value != "Y" && is_active[i].value == "Y")
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
			if(nom_block.value != "Y" && is_active.value == "Y")
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
	var qty_scm = document.getElementById("qty_scm"+index);
	var sf_id = document.getElementById("sf_id"+index);
	
	var plant_seq = document.getElementById("plant_seq"+index);
	var trans_cd = document.getElementById("trans_cd"+index);
	var trans_plant_seq = document.getElementById("trans_plant_seq"+index);
	var bu_plant_seq = document.getElementById("bu_plant_seq"+index);
	
	var l_index = document.getElementById("index_"+index);
	var index1 = document.getElementById("index1"+index);
	
	if(obj.checked)
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
		qty_scm.disabled=false;
		sf_id.disabled=false;
		
		if(parseInt(index1.value) > 0) //IF CT CONFIGURED
		{
			qty_mmbtu.readOnly=true;
			qty_scm.readOnly=true;
			
			qty_mmbtu.style.pointerEvents = "none";
			qty_scm.style.pointerEvents = "none";
		}
		else
		{
			qty_mmbtu.readOnly=false;
			//qty_scm.readOnly=false;
			
			qty_mmbtu.style.pointerEvents = "auto";
			qty_scm.style.pointerEvents = "auto";
		}
		
		plant_seq.disabled=false;
		trans_cd.disabled=false;
		trans_plant_seq.disabled=false;
		bu_plant_seq.disabled=false;
		
		for(var i=1;i<=parseInt(index1.value);i++)
		{
			var chk = document.getElementById("chk"+index+""+i);
			chk.disabled=false;
			
			//if(parseInt(index1.value) == 1) //DEFAULT WILL BE TICKED IF SINGLE CONTRACT ID(CT REF)
			{
				chk.checked=true;
			}
			
			setSubEnableDisabled(chk,index,i)
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
		qty_scm.readOnly=true;
		
		sf_id.disabled=true;
		
		plant_seq.disabled=true;
		trans_cd.disabled=true;
		trans_plant_seq.disabled=true;
		bu_plant_seq.disabled=true;
		
		for(var i=1;i<=parseInt(index1.value);i++)
		{
			var chk = document.getElementById("chk"+index+""+i);
			var sub_is_exist = document.getElementById("sub_is_exist"+index+""+i);
			
			chk.disabled=true;
			
			if(sub_is_exist.value=="N") //IF ENTRY EXIST TEHN SYSTEM WILL NOT ALLOW TO UNCHECK
			{
				chk.checked=false;
			}
			
			setSubEnableDisabled(chk,index,i)
		}
	}
}

function setSubEnableDisabled(obj,index,index1)
{
	var chk = document.getElementById("chk"+index);
	
	var sub_ct_ref = document.getElementById("sub_ct_ref"+index+""+index1);
	var sub_utr_ref = document.getElementById("sub_utr_ref"+index+""+index1);
	var temp_sub_ct_ref = document.getElementById("temp_sub_ct_ref"+index+""+index1);
	var sub_qty_mmbtu = document.getElementById("sub_qty_mmbtu"+index+""+index1);
	var sub_qty_scm = document.getElementById("sub_qty_scm"+index+""+index1);
	var sub_is_exist = document.getElementById("sub_is_exist"+index+""+index1);
	var sub_seq_no = document.getElementById("sub_seq_no"+index+""+index1);
	var sub_sf_id = document.getElementById("sub_sf_id"+index+""+index1);
	
	if(obj.checked)
	{
		sub_ct_ref.disabled=false;
		sub_utr_ref.disabled=false;
		temp_sub_ct_ref.disabled=false;
		sub_qty_mmbtu.disabled=false;
		sub_qty_scm.disabled=false;
		sub_seq_no.disabled=false;
		sub_sf_id.disabled=false;
		
		if(!chk.checked && sub_is_exist.value=="Y") //IF ENTRY EXIST TEHN SYSTEM WILL NOT ALLOW TO UNCHECK BUT THE OTHER INPUTE FIELD GETS DISBALED ONCE UNCHECKED MAIN
		{
			sub_ct_ref.disabled=true;
			sub_utr_ref.disabled=true;
			temp_sub_ct_ref.disabled=true;
			sub_qty_mmbtu.disabled=true;
			sub_qty_scm.disabled=true;
			sub_seq_no.disabled=true;
			sub_sf_id.disabled=true;
		}
	}
	else
	{
		sub_ct_ref.disabled=true;
		sub_utr_ref.disabled=true;
		temp_sub_ct_ref.disabled=true;
		sub_qty_mmbtu.disabled=true;
		sub_qty_scm.disabled=true;
		sub_seq_no.disabled=true;
		sub_sf_id.disabled=true;
	}		
}

enableButton=true;
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
	
	var totalBuyerNom = document.forms[0].totalBuyerNom.value;
	var tcq = document.forms[0].tcq.value;
	
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
				
				var sub_count=parseInt("0");
				var index1 = document.getElementById("index1"+i);
				for(var k=1;k<=parseInt(index1.value);k++)
				{
					var sub_chk = document.getElementById("chk"+i+""+k);
					var sub_qty_mmbtu = document.getElementById("sub_qty_mmbtu"+i+""+k);
					var sub_qty_scm = document.getElementById("sub_qty_scm"+i+""+k);	
					
					if(sub_chk.checked)
					{
						sub_count++;
						if(trim(sub_qty_mmbtu.value)=="")
						{
							msg+="Enter Sub Energy(MMBTU) for ROW - "+parseInt(k)+"!\n";
							flag=false;
						}
						if(trim(sub_qty_scm.value)=="")
						{
							msg+="Enter Sub Energy(SCM) for ROW - "+parseInt(k)+"!\n";
							flag=false;
						}
					}
				}
				
				if(parseInt(sub_count) == 0 && parseInt(index1.value) > 0)
				{
					msg+="Please Select Atleast One Sub ROW for ROW - "+parseInt(i+1)+" for Submit!\n";
					flag=false;
				}
			}
			
			if(trim(qty_mmbtu[i].value) != "")
			{
				tot_mmbtu = tot_mmbtu + parseFloat(qty_mmbtu[i].value);
			}			
		}
	}
	else
	{
		if(chk.checked)
		{ 	chk_count++
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
			
			var sub_count=parseInt("0");
			var index1 = document.getElementById("index10");
			for(var k=1;k<=parseInt(index1.value);k++)
			{
				var sub_chk = document.getElementById("chk0"+k);
				var sub_qty_mmbtu = document.getElementById("sub_qty_mmbtu0"+k);
				var sub_qty_scm = document.getElementById("sub_qty_scm0"+k);	
				
				if(sub_chk.checked)
				{
					sub_count++;
					if(trim(sub_qty_mmbtu.value)=="")
					{
						msg+="Enter Sub Energy(MMBTU) for ROW - "+parseInt(k)+"!\n";
						flag=false;
					}
					if(trim(sub_qty_scm.value)=="")
					{
						msg+="Enter Sub Energy(SCM) for ROW - "+parseInt(k)+"!\n";
						flag=false;
					}
				}
			}
			
			if(parseInt(sub_count) == 0 && parseInt(index1.value) > 0)
			{
				msg+="Please Select Atleast One Sub ROW for ROW - 1 for Submit!\n";
				flag=false;
			}
		}
		
		if(trim(qty_mmbtu.value) != "")
		{
			tot_mmbtu = tot_mmbtu + parseFloat(qty_mmbtu.value);
		}
	}
	
	if(parseInt(chk_count) == 0)
	{
		msg+="Please Select Atleast One ROW for Submit!\n";
		flag=false;
	}
	
	var tot_BuyNom=parseFloat(totalBuyerNom) + tot_mmbtu;
	
	/*if(parseFloat(tot_BuyNom) > parseFloat(tcq))
	{
		alert("Contract TCQ : "+parseFloat(tcq)+"\nTotal Buyer Nom : "+parseFloat(tot_BuyNom)+"\n\nThe Buyer Nomination Qty should not be > Contract TCQ!");
	}	
	else*/ 
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
	else
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
		//DT20251121 Incident#BUG 2510010:changes done for rounding SCM val upto 2 decimal digits
		/* var scm = ""+round(((parseFloat(""+qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),0); */
		var scm = ""+round(((parseFloat(""+qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),2);
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
	for(var i=1;i<=parseInt(index1.value);i++)
	{
		var sub_qty_mmbtu = document.getElementById("sub_qty_mmbtu"+index+""+i);
		var sub_qty_scm = document.getElementById("sub_qty_scm"+index+""+i);	
		
		if((sub_qty_mmbtu.value!=null && trim(sub_qty_mmbtu.value) !=''))
		{
			//DT20251121 Incident#BUG 2510010:changes done for rounding SCM val upto 2 decimal digits
			/* var scm = ""+round(((parseFloat(""+sub_qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),0); */
			var scm = ""+round(((parseFloat(""+sub_qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),2);
			if(isNaN(scm))
			{
				sub_qty_scm.value="";
			}
			else
			{
				sub_qty_scm.value = scm;
			}
		}
	}
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


/*async function doPaste(index,index1)
{
	var pastedText = event.clipboardData.getData('text/plain');
	var arr=pastedText.split(/\n/);
	
	var sub_index = document.forms[0].sub_index;
	
	var chk = document.forms[0].chk;
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	
	var i=0;
	var m=0;
	
	var k=parseInt("0");
	var pasteLen=arr.length;
	
	if(sub_index!=null && sub_index!=undefined)
	{
		if(sub_index.length!=undefined)
		{
			window.setTimeout(function() 
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
							
							if((parseInt(i) >= parseInt(index1)) && (parseInt(j) == parseInt(index)))
							{
								if(parseInt(k) < parseInt(pasteLen))
								{
									if(chk[i].checked)
									{
										if(trim(arr[k])!= "")
										{	qty_mmbtu[i].value=arr[k];
											
											negNumber(qty_mmbtu[i]);
											checkNumber1(qty_mmbtu[i],9,2);
											checkQty(i);
											checkTCQ(i)
											calculateSCM(j,i);
											//totalQty();
										}
										k++;
									}
								}
								else
								{
									break;
								}
							}
							if(parseInt(m)==parseInt(subIndex))
							{
								i=parseInt(i)+1;
								break;
							}
						}
					}
				}
			}, 50);
		}
		else
		{
			var subIndex = parseInt(sub_index.value);
			
			m=0;
			if(qty_mmbtu.length!=undefined)
			{
				window.setTimeout(function() 
				{
					for(i=i; i<qty_mmbtu.length; i++)
					{
						m=m+1;
						if(parseInt(i) >= parseInt(index1))
						{
							if(parseInt(k) < parseInt(pasteLen))
							{
								if(chk[i].checked)
								{
									if(trim(arr[k])!= "")
									{							
										qty_mmbtu[i].value=arr[k];
										
										negNumber(qty_mmbtu[i]);
										checkNumber1(qty_mmbtu[i],9,2);
										checkQty(i);
										checkTCQ(i);
										calculateSCM("0",i);
										//totalQty();
									}
									k++;
								}
							}
							else
							{
								break;
							}
						}
						if(parseInt(m)==parseInt(subIndex))
						{
							i=parseInt(i)+1;
							break;
						}
					}
				}, 50);
			}
			else
			{
				window.setTimeout(function() 
				{
					if(parseInt(k) < parseInt(pasteLen))
					{
						if(chk.checked)
						{
							if(trim(arr[k])!= "")
							{							
								qty_mmbtu.value=arr[k];
								
								negNumber(qty_mmbtu);
								checkNumber1(qty_mmbtu,9,2);
								checkQty(i);
								checkTCQ(i);
								calculateSCM("0",i);
								//totalQty();
							}
							k++;
						}
					}
				}, 50);
			}
		}
	}
}*/
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DataBean_ContractMgmt" id="cont_mgmt" scope="request"></jsp:useBean>
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
Vector VBU_CD = cont_mgmt.getVBU_CD();
Vector VBU_PLANT_SEQ = cont_mgmt.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = cont_mgmt.getVBU_PLANT_ABBR();
Vector VCOUNTERPARTY_STATUS = cont_mgmt.getVCOUNTERPARTY_STATUS();
Vector VTRANSPORTER_STATUS = cont_mgmt.getVTRANSPORTER_STATUS();

Vector VGAS_DT = cont_mgmt.getVGAS_DT();

Vector VNOM_REV_NO = cont_mgmt.getVNOM_REV_NO();
Vector VGEN_TIME = cont_mgmt.getVGEN_TIME();
Vector VGEN_DT = cont_mgmt.getVGEN_DT();
Vector VBASE = cont_mgmt.getVBASE();
Vector VGCV = cont_mgmt.getVGCV();
Vector VNCV = cont_mgmt.getVNCV();
Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();
Vector VQTY_SCM = cont_mgmt.getVQTY_SCM();
Vector VNOM_COLOR = cont_mgmt.getVNOM_COLOR();
Vector VGAS_DATE = cont_mgmt.getVGAS_DATE();
Vector VDCQ = cont_mgmt.getVDCQ();
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

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_ContractMgmt">

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
				    				<input type="hidden" class="form-control form-control-sm" name="counterparty_cd" value="<%=counterparty_cd%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_no" value="<%=cont_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_rev_no" value="<%=cont_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" name="cargo_no" value="<%=cargo_no%>">
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
      			<%if(!counterparty_cd.equals("") && !counterparty_cd.equals("0")){ %>
      				<%int j=0,k=0,l=0,m=0,p=0,q=0,w=0,y=0;
					for(int i=0; i<VTRANSPORTER_CD.size(); i++)
					{ 
						String trans_cd=""+VTRANSPORTER_CD.elementAt(i);
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
					%>
					<%if(i!=0){ %>&nbsp;<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VTRANSPORTER_ABBR.elementAt(i)%>
							<span 
							<%if(VTRANSPORTER_STATUS.elementAt(l).equals("N")){ %>class='alert alert-danger' title="Counterparty Deactive "
							<%}else if(VTRANSPORTER_STATUS.elementAt(l).equals("E")){ %>class='alert alert-warning'
							<%} %>
							><b> <%if(VTRANSPORTER_STATUS.elementAt(l).equals("N")){ %> De-active 
							<%}else if(VTRANSPORTER_STATUS.elementAt(l).equals("E")){ %> E-Rate
							<%} %> </b>
							</span>
						</label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
						<%k=0;
						if(index > 0){ %>
							<%for(j=j;j<VTRANSPORTER_PLANT_SEQ.size(); j++) 
							{
								String trans_plant_seq=""+VTRANSPORTER_PLANT_SEQ.elementAt(j);
								int sub_index = Integer.parseInt(""+VSUB_INDEX.elementAt(j));
								k+=1;
							%>
								<input type="hidden" name="sub_index" value="<%=sub_index%>">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading<%=j%>">
    										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="false" aria-controls="collapse<%=j%>">
								    		<%=VTRANSPORTER_PLANT_ABBR.elementAt(j)%>
								      		</button>
								    	</h2>
								    	<div id="collapse<%=j%>" class="accordion-collapse collapse" aria-labelledby="heading<%=j%>">
								      		<div class="accordion-body accor-body">
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
														<!-- </div>
													</div>
													<div class="col-sm-6 col-xs-3 col-md-2">
										    			<div class="form-group row"> -->
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
								        		<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr>
																	<th>Gas Date</th>
																	<th><input class="form-check-input" type="checkbox" name="chkAll" onclick="checkAll(this,'<%=VENTRY_RANGE.elementAt(j)%>','<%=j%>');"></th>
																	<th>Business Unit</th>
																	<th>Customer Plant<br><font style="background:#ADD8E6;">Customer Code</font></th>
																	<th>Tax</th>
																	<th>DCQ</th>
																	<th>Energy (MMBTU)</th>
																	<th>Energy (SCM)</th>
																	<th>Rev#</th>
																	<th>SF ID</th>
																	<th>Gen Date:Time</th>
																	<!-- <th>Calorific Value Base<br>KCal/SCM</th> -->
																</tr>
															</thead>
															<tbody>
																<%if(sub_index>0)
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
																				<td align="center" rowspan="<%=rowspan%>">
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
																				<td align="center" valign="middle" <%if(VNOM_BLOCK.elementAt(l).equals("Y")) {%>style="background: #df9fbf;" title="Invoice Generated!"<%} %>>
																					<input type="checkbox" class="form-check-input" name="chk" id="chk<%=l%>" 
																					onclick="setEnableDisabled(this,'<%=l%>');calculateSCM('<%=j%>','<%=l%>');"
																					<%if(VNOM_BLOCK.elementAt(l).equals("Y")) {%>disabled style="pointer-events: none;"<%} %>
																					<%if(VTRANSPORTER_STATUS.elementAt(l).equals("N") || VCOUNTERPARTY_STATUS.elementAt(l).equals("N")) {%>disabled style="pointer-events: none;"<%} %>>
																					<input type="hidden" name="index1" id="index1<%=l%>" value="<%=index1%>" disabled>
																					<input type="hidden" name="index" id="index_<%=l%>" value="<%=l%>" disabled>
																					<input type="hidden" name="nom_block" id="nom_block<%=l%>" value="<%=VNOM_BLOCK.elementAt(l)%>" disabled>
																					<input type="hidden" name="is_active" id="is_active<%=l%>" 
																					<%if(VTRANSPORTER_STATUS.elementAt(l).equals("N") || VCOUNTERPARTY_STATUS.elementAt(l).equals("N")) {%>
																					value="N"<%}else{ %>value="Y"<%} %>
																					disabled>
																					<input type="hidden" name="week_gas_dt" id="week_gas_dt<%=l%>" value="<%=temp_gas_dt%>" disabled>
																				</td>
																				<td align="center">
																					<%=VBU_PLANT_ABBR.elementAt(l)%>
																					<input type="hidden" name="bu_plant_seq" id="bu_plant_seq<%=l%>" value="<%=VBU_PLANT_SEQ.elementAt(l)%>" disabled>
																					<input type="hidden" name="trans_cd" id="trans_cd<%=l%>" value="<%=trans_cd%>" disabled>
																      				<input type="hidden" name="trans_plant_seq" id="trans_plant_seq<%=l%>" value="<%=trans_plant_seq%>" disabled>
																      			</td>
																				<td align="center">
																					<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%>
																					<%if(!VCUSTOMER_CODE.elementAt(l).equals("")){%>
																					<br><font style="background:#ADD8E6;"><%=VCUSTOMER_CODE.elementAt(l)%></font>
																					<%} %>
																					<input type="hidden" name="plant_seq" id="plant_seq<%=l%>" value="<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(l)%>" disabled>
																				</td>
																				<td>
																					<%=VTAX_DTL.elementAt(l)%>
																				</td>
																				<td align="right">
																					<%=VDCQ.elementAt(l)%>
																					<input type="hidden" value="<%=VDCQ.elementAt(l)%>" name="dcq" id="dcq<%=l%>">
																					<input type="hidden" value="<%=VMDCQ_QTY.elementAt(l)%>" name="mdcq_qty" id="mdcq_qty<%=l%>">
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="qty_mmbtu" id="qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" 
																						style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" 
																						<%-- onpaste="doPaste('<%=j%>','<%=l%>');"  --%>
																						onblur="negNumber(this);checkNumber1(this,9,2);checkQty('<%=l%>');calculateSCM('<%=j%>','<%=l%>');" disabled>
																						<%-- checkTCQ('<%=l%>'); --%>
																						<input type="hidden" class="form-control form-control-sm" name="tmp_qty_mmbtu" id="tmp_qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" style="text-align:right" disabled>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="qty_scm" id="qty_scm<%=l%>" value="<%=VQTY_SCM.elementAt(l)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
																					</div>
																				</td>																				
																				<td align="center"><%=VNOM_REV_NO.elementAt(l)%></td>
																				<td align="center">
																				<%if(index1==0 && !VNOM_SF_ID.elementAt(l).equals("")){ %>
																					<div class="row m-b-5">
																						<div class="col">
																							<input type="button" class="btn btn-sm config_btn" id="" title="<%=VNOM_SF_ID.elementAt(l)%>" style="border-color: #ffb3ff; background-color: #ffb3ff;" value="SF">
																						</div>																							
																					</div>
																				<%} %>
																					<input type="hidden" name="sf_id" id="sf_id<%=l%>" value="<%=VNOM_SF_ID.elementAt(l)%>" disabled>
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
																			<%q=0;
																			if(index1>0){ %>
																				<%-- <tbody id="tbody<%=j%>" style="display:;"> --%>
																					<tr style="font-weight: bold;background:#bce6ff;text-align: center;">
																						<td rowspan="<%=index1+2%>" colspan="4" style="background:white;"></td>
																						<td>Contract ID [UTR#]</td>
																						<td>Energy (MMBTU)</td>
																						<td>Energy (SCM)</td>
																						<td>Rev#</td>
																						<th>SF ID</th>																						
																						<td rowspan="<%=index1+2%>" colspan="2" style="background:white;"></td>
																					</tr>
																				<%for(p=p; p<VSUB_CT_REF.size(); p++)
																				{ 
																					q+=1;
																				%>
																					<tr>
																						<td>
																							<div style="width:150px;">
																								<div class="row m-b-5">
																									<div class="col-auto">
																										<input type="checkbox" class="form-check-input" name="chk<%=l%>" id="chk<%=l%><%=q%>" 
																										<%if(VSUB_IS_EXIST.elementAt(p).equals("Y")){ %>style="pointer-events: none;" checked<%} %>
																										onclick="setSubEnableDisabled(this,'<%=l%>','<%=q%>');calculateSCM('<%=j%>','<%=l%>');totalSubQty('<%=l%>');checkQty('<%=l%>');" disabled>
																										<input type="hidden" name="sub_is_exist" id="sub_is_exist<%=l%><%=q%>" value="<%=VSUB_IS_EXIST.elementAt(p)%>">
																										<input type="hidden" name="sub_seq_no<%=l%>" id="sub_seq_no<%=l%><%=q%>" value="<%=VSUB_SEQ_NO.elementAt(p)%>">
																									</div>
																								<!-- </td>
																								<td align="center"> -->
																									<div class="col">
																										<%if(!VSUB_CT_REF.elementAt(p).equals("")){ %>
																											<%-- <%=VSUB_CT_REF.elementAt(p)%> --%>
																											<input type="text" class="form-control form-control-sm" name="sub_ct_ref<%=l%>" id="sub_ct_ref<%=l%><%=q%>" value="<%=VSUB_CT_REF.elementAt(p)%>" disabled>
																											<%if(!VSUB_UTR_REF.elementAt(p).equals("")){ %>
																												[<%=VSUB_UTR_REF.elementAt(p)%>]
																											<%} %>
																										<%}else{ %>
																											<!-- <font color="red">No Contract ID</font> -->
																											<input type="text" class="form-control form-control-sm" name="sub_ct_ref<%=l%>" id="sub_ct_ref<%=l%><%=q%>" value="<%=VSUB_CT_REF.elementAt(p)%>" placeholder="No Contract ID" disabled>
																										<%} %>
																										
																										<input type="hidden" class="form-control form-control-sm" name="temp_sub_ct_ref<%=l%>" id="temp_sub_ct_ref<%=l%><%=q%>" value="<%=VSUB_CT_REF.elementAt(p)%>" readOnly disabled>
																										<input type="hidden" class="form-control form-control-sm" name="sub_utr_ref<%=l%>" id="sub_utr_ref<%=l%><%=q%>" value="<%=VSUB_UTR_REF.elementAt(p)%>" readOnly disabled>
																									</div>
																								</div>
																							</div>
																						</td>
																						<td align="center">
																							<div style="width:100px;">
																								<input type="text" class="form-control form-control-sm" name="sub_qty_mmbtu<%=l%>" id="sub_qty_mmbtu<%=l%><%=q%>" value="<%=VSUB_QTY_MMBTU.elementAt(p)%>" 
																								style="text-align:right;background:<%=VSUB_NOM_COLOR.elementAt(p)%>" 
																								onblur="negNumber(this);checkNumber1(this,9,2);calculateSCM('<%=j%>','<%=l%>');totalSubQty('<%=l%>');checkQty('<%=l%>');" disabled>
																								<%-- checkTCQ('<%=l%>'); --%>
																							</div>
																						</td>
																						<td align="center">
																							<div style="width:100px;">
																								<input type="text" class="form-control form-control-sm" name="sub_qty_scm<%=l%>" id="sub_qty_scm<%=l%><%=q%>" value="<%=VSUB_QTY_SCM.elementAt(p)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
																							</div>
																						</td>																						
																						<td align="center">
																							<%=VSUB_NOM_REV_NO.elementAt(p) %>
																						</td>
																						<td align="center">
																						<%if(!VSUB_NOM_SF_ID.elementAt(p).equals("")){ %>
																							<div class="row m-b-5">
																								<div class="col">
																									<input type="button" class="btn btn-sm config_btn" id="" title="<%=VSUB_NOM_SF_ID.elementAt(p)%>" style="border-color: #ffb3ff; background-color: #ffb3ff;" value="SF">
																								</div>																							
																							</div>
																						<%}%>	
																							<input type="hidden" name="sub_sf_id<%=l%>" id="sub_sf_id<%=l%><%=q%>" value="<%=VSUB_NOM_SF_ID.elementAt(p)%>" disabled>
																						</td>
																					</tr>
																					<%if(q==index1){%>
																						<tr>
																							<td colspan="5">&nbsp;</td>
																						</tr>
																						<%p++;
																						break;
																					}%>
																				<%} %>
																				<!-- </tbody> -->
																			<%} %>
																			<%if(m==gas_index)
																			{%>
																				<%l=l+1;
																				break;
																			}%>
																		<%} %>
																		<%if(y==sub_index)
																		{%>
																			<%w=w+1;
																			break;
																		}%>
																	<%} %>
																<%} %>
															</tbody>
														</table>
													</div>
												</div>
								      		</div>
								    	</div>
								    </div>
								</div>
								<%if(k==index)
								{
									j=j+1;
									break;
								}%>
							<%} %>
						<%} %>
						</div>
					</div>
					<%} %>
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

<input type="hidden" name="option" value="PERIODIC_BUYER_NOM">
<input type="hidden" name="prev_counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">

<input type="hidden" name="contract_st_dt" value="<%=start_dt%>">
<input type="hidden" name="contract_end_dt" value="<%=end_dt%>">

<input type="hidden" name="totalBuyerNom" value="<%=totalBuyerNom%>">
<input type="hidden" name="tcq" value="<%=tcq%>">

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