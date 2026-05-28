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
	
	var url = "frm_daily_allocation.jsp?gas_dt="+gas_dt+"&u="+u;

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

function setEnableDisabled(obj,index)
{
	var counterparty_cd = document.getElementById("counterparty_cd"+index);
	var agmt_no = document.getElementById("agmt_no"+index);
	var agmt_rev_no = document.getElementById("agmt_rev_no"+index);
	var cont_no = document.getElementById("cont_no"+index);
	var cont_rev_no = document.getElementById("cont_rev_no"+index);
	var contract_type = document.getElementById("contract_type"+index);
	var cargo_no = document.getElementById("cargo_no"+index);
	
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
	
	var l_index = document.getElementById("index_"+index);
	var index1 = document.getElementById("index1"+index);
	
	var moleConfigBtn = document.getElementById("moleConfigBtn"+index);
	var mole_chk = document.getElementsByName("mole_chk_"+index).length;
		
	if(obj.checked)
	{
		l_index.disabled=false;
		index1.disabled=false;
		
		counterparty_cd.disabled=false;
		agmt_no.disabled=false;
		agmt_rev_no.disabled=false;
		cont_no.disabled=false;
		cont_rev_no.disabled=false;
		contract_type.disabled=false;
		cargo_no.disabled=false;
		gen_time.disabled=false;
		rd1.disabled=false;
		rd2.disabled=false;
		gcv.disabled=false;
		ncv.disabled=false;
		base.disabled=false;
		
		qty_mmbtu.disabled=false;
		qty_scm.disabled=false;
		
		moleConfigBtn.style.display="";
		
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
		
		//FOR MOLECULE
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
		l_index.disabled=true;
		index1.disabled=true;
		
		counterparty_cd.disabled=true;
		agmt_no.disabled=true;
		agmt_rev_no.disabled=true;
		cont_no.disabled=true;
		cont_rev_no.disabled=true;
		contract_type.disabled=true;
		cargo_no.disabled=true;
		gen_time.disabled=true;
		rd1.disabled=true;
		rd2.disabled=true;
		gcv.disabled=true;
		ncv.disabled=true;
		base.disabled=true;
		
		qty_mmbtu.disabled=true;
		qty_scm.disabled=true;
		qty_mmbtu.readOnly=true;
		qty_scm.readOnly=true;
		
		plant_seq.disabled=true;
		trans_cd.disabled=true;
		trans_plant_seq.disabled=true;
		bu_plant_seq.disabled=true;
		
		moleConfigBtn.style.display="none";
		
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
		
		//FOR MOLECULE
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

function setSubEnableDisabled(obj,index,index1)
{
	var chk = document.getElementById("chk"+index);
	
	var sub_ct_ref = document.getElementById("sub_ct_ref"+index+""+index1);
	var sub_utr_ref = document.getElementById("sub_utr_ref"+index+""+index1);
	var sub_qty_mmbtu = document.getElementById("sub_qty_mmbtu"+index+""+index1);
	var sub_qty_scm = document.getElementById("sub_qty_scm"+index+""+index1);
	var sub_is_exist = document.getElementById("sub_is_exist"+index+""+index1);
	var sub_seq_no = document.getElementById("sub_seq_no"+index+""+index1);
	if(obj.checked)
	{
		sub_ct_ref.disabled=false;
		sub_utr_ref.disabled=false;
		sub_qty_mmbtu.disabled=false;
		sub_qty_scm.disabled=false;
		sub_seq_no.disabled=false;
		
		if(!chk.checked && sub_is_exist.value=="Y") //IF ENTRY EXIST TEHN SYSTEM WILL NOT ALLOW TO UNCHECK BUT THE OTHER INPUTE FIELD GETS DISBALED ONCE UNCHECKED MAIN
		{
			sub_ct_ref.disabled=true;
			sub_utr_ref.disabled=true;
			sub_qty_mmbtu.disabled=true;
			sub_qty_scm.disabled=true;
			sub_seq_no.disabled=true;
		}
	}
	else
	{
		sub_ct_ref.disabled=true;
		sub_utr_ref.disabled=true;
		sub_qty_mmbtu.disabled=true;
		sub_qty_scm.disabled=true;
		sub_seq_no.disabled=true;
	}		
}


function enabled()
{
	var chk = document.forms[0].chk;
	var is_exist = document.forms[0].is_exist;
	var nom_block = document.forms[0].nom_block;
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
					if(nom_block[i].value!="Y")
					{
						setEnableDisabled(chk[i],index)
					}
					else
					{
						moleConfigBtn.style.display="";
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
				if(nom_block.value!="Y")
				{
					setEnableDisabled(chk,index)
				}
				else
				{
					moleConfigBtn.style.display="";
					calcRemainMoleQty(index);
				}
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
		
	if(qty_mmbtu.value!=null && trim(qty_mmbtu.value) !='')
	{
		var scm = parseFloat("0");
		if (parseFloat(baseVal) != 0)
		{
			//DT20251024 Incident#BUG 2510010:changes done for rounding SCM val upto 2 decimal digits
			/* scm = ""+round(((parseFloat(""+qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),0); */
			scm = ""+round(((parseFloat(""+qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),2);  
		}	
		
		if(isNaN(scm))
		{
			//qty_scm.value=""; 
			qty_scm.value="0";
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
			var scm = parseFloat("0");
			if (parseFloat(baseVal) != 0)
			{ 
				//DT20251027 Incident#BUG 2510010:changes done for rounding SCM val upto 2 decimal digits
				/* scm = ""+round(((parseFloat(""+sub_qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),0); */
				scm = ""+round(((parseFloat(""+sub_qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),2);
			}
			
			if(isNaN(scm))
			{
				//sub_qty_scm.value="";
				sub_qty_scm.value="0";
			}
			else
			{
				sub_qty_scm.value = scm;
			}
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
					//DT20251125 Incident#BUG 2510010:changes done for rounding SCM val upto 2 decimal digits
					/* scm = ""+round(((parseFloat(""+mole_qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),0); */
					scm = ""+round(((parseFloat(""+mole_qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),2);
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

function doSubmit()
{
	var sub_index = document.forms[0].sub_index;
	var ticket_qty_mmbtu = document.forms[0].ticket_qty_mmbtu;
	var trans_abbr = document.forms[0].trans_abbr;
	
	var chk = document.forms[0].chk;
	var gas_dt = document.forms[0].gas_dt;
	var gen_dt = document.forms[0].gen_dt;
	var gen_time = document.forms[0].gen_time;
	var gcv = document.forms[0].gcv;
	var ncv = document.forms[0].ncv;
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	var int_map_id = document.forms[0].internal_map_id;
	var temp_dis_cont_mapping = document.forms[0].temp_dis_cont_mapping;
	
	var cont_internal_mapp_id = document.forms[0].cont_internal_mapp_id;
	var cont_buyer_nom = document.forms[0].cont_buyer_nom;
	var cont_tcq = document.forms[0].cont_tcq;
	
	var msg="";
	var flag=true;
	var tcq_msg="";
	var tcq_flag=true;
	var chk_count=parseInt("0");
	var sub_chk_count=parseInt("0");
	
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
	
	if(gas_dt.value!=null && trim(gas_dt.value) != "" && gen_dt.value!=null && trim(gen_dt.value) != "")
	{
		var value = compareDate(gas_dt.value,gen_dt.value);
	  	if(value==1)
	  	{
	    	msg += "Gen Date should be >= Gas Day!\n";
	    	flag = false;
	  	}
	}
	
	var i=0;
	var m=0;
	var ticketQty=0;
	var QtyMMBTU=0;
	if(sub_index!=null && sub_index!=undefined)
	{
		if(sub_index.length!=undefined)
		{
			for(var j=0; j<sub_index.length; j++)
			{
				var subIndex = parseInt(sub_index[j].value);
				ticketQty=ticket_qty_mmbtu[j].value
				if(gen_time!=null && gen_time.length!=undefined)
				{
					m=0;
					QtyMMBTU=0;
					sub_chk_count=0;
					for(i=i; i<gen_time.length; i++)
					{
						m=m+1;
						if(chk[i].checked)
						{
							chk_count++;
							sub_chk_count++;
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
							
							if(trim(qty_mmbtu[i].value)!="")
							{
								QtyMMBTU = parseFloat(QtyMMBTU) + parseFloat(qty_mmbtu[i].value);
								QtyMMBTU = round(parseFloat(QtyMMBTU),2);
							}
							
							////////////////MOLECULE/////////////////////////////////////
							var mole_chk = document.getElementsByName("mole_chk_"+i).length;
							
							var tot_mole_qty_mmbtu = document.getElementById("tot_mole_qty_mmbtu_"+i)
							var remain_mole_qty_mmbtu = document.getElementById("remain_mole_qty_mmbtu_"+i)
							
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
							
							if(mole_count > 0)
							{
								if(remain_mole_qty_mmbtu.value != "")
								{
									if(parseFloat(remain_mole_qty_mmbtu.value) != 0)
									{
										msg+="Remaining Molecule MMBTU should be = 0 for ROW - "+parseInt(i+1)+"!\n";
										flag=false;
									}
								}
								else
								{
									msg+="Remaining Molecule MMBTU shouldn't be blank for ROW - "+parseInt(i+1)+"!\n";
									flag=false;
								}
							}
							///////////////////////////////////////////////////////////////////
							
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
							
							var tot_mmbtu =  parseFloat("0");
							if(parseFloat(qty_mmbtu[i].value) > 0)
							{
								if(cont_internal_mapp_id!=null && cont_internal_mapp_id!=undefined)
								{
									if(cont_internal_mapp_id.length!=undefined)
									{
										for(var k=0; k<cont_internal_mapp_id.length; k++)
										{
											if(int_map_id[i].value==cont_internal_mapp_id[k].value)
											{
												tot_mmbtu = parseFloat(cont_buyer_nom[k].value) + parseFloat(getBuyerNomContractWise(cont_internal_mapp_id[k].value,i));
												
												if(parseFloat(tot_mmbtu) > parseFloat(cont_tcq[k].value))
												{
													tcq_msg+="Contract("+temp_dis_cont_mapping[i].value+") TCQ : "+parseFloat(cont_tcq[k].value)+"\nTotal Allocation : "+parseFloat(tot_mmbtu)+"\nThe Allocation Qty should not be > Contract TCQ!\n\n";
													tcq_flag=false;
												}
											}
										}
									}
									else
									{
										if(int_map_id[i].value==cont_internal_mapp_id.value)
										{
											tot_mmbtu = parseFloat(cont_buyer_nom.value) + parseFloat(getBuyerNomContractWise(cont_internal_mapp_id.value,i));
											
											if(parseFloat(tot_mmbtu) > parseFloat(cont_tcq.value))
											{
												tcq_msg+="Contract("+temp_dis_cont_mapping[i].value+") TCQ : "+parseFloat(cont_tcq.value)+"\nTotal Allocation : "+parseFloat(tot_mmbtu)+"\nThe Allocation Qty should not be > Contract TCQ!\n\n";
												tcq_flag=false;
											}
										}
									}
								}
							}
							
							if(parseInt(sub_count) == 0 && parseInt(index1.value) > 0)
							{
								msg+="Please Select Atleast One Sub ROW for ROW - "+parseInt(i+1)+" for Submit!\n";
								flag=false;
							}
						}
						
						if(parseInt(m)==parseInt(subIndex))
						{
							i=parseInt(i)+1;
							break;
						}
					}
					//alert(QtyMMBTU+"=="+ticketQty)
					if(sub_chk_count > 0)
					{
						if(parseFloat(QtyMMBTU) != parseFloat(ticketQty))
						{
							msg+="For "+trans_abbr[j].value+" Grid, Please Verify The Total Allocated QTY("+parseFloat(QtyMMBTU)+" MMBTU) With Meter Ticket QTY("+ticketQty+" MMBTU) - Both Are Different!\n";
							flag=false;
						}
					}
				}
			}
		}
		else
		{
			var subIndex = parseInt(sub_index.value);
			ticketQty=ticket_qty_mmbtu.value
			if(gen_time!=null && gen_time.length!=undefined)
			{
				m=0;
				QtyMMBTU=0;
				sub_chk_count=0;
				for(i=i; i<gen_time.length; i++)
				{
					m=m+1;
					if(chk[i].checked)
					{
						chk_count++;
						sub_chk_count++;
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
						
						if(trim(qty_mmbtu[i].value)!="")
						{
							QtyMMBTU = parseFloat(QtyMMBTU) + parseFloat(qty_mmbtu[i].value);
							QtyMMBTU = round(parseFloat(QtyMMBTU),2);
						}
						////////////////MOLECULE/////////////////////////////////////
						var mole_chk = document.getElementsByName("mole_chk_"+i).length;
						
						var tot_mole_qty_mmbtu = document.getElementById("tot_mole_qty_mmbtu_"+i)
						var remain_mole_qty_mmbtu = document.getElementById("remain_mole_qty_mmbtu_"+i)
						
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
						
						if(mole_count > 0)
						{
							if(remain_mole_qty_mmbtu.value != "")
							{
								if(parseFloat(remain_mole_qty_mmbtu.value) != 0)
								{
									msg+="Remaining Molecule MMBTU should be = 0 for ROW - "+parseInt(i+1)+"!\n";
									flag=false;
								}
							}
							else
							{
								msg+="Remaining Molecule MMBTU shouldn't be blank for ROW - "+parseInt(i+1)+"!\n";
								flag=false;
							}
						}
						///////////////////////////////////////////////////////////////////
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
						
						var tot_mmbtu =  parseFloat("0");
						if(parseFloat(qty_mmbtu[i].value) > 0)
						{
							if(cont_internal_mapp_id!=null && cont_internal_mapp_id!=undefined)
							{
								if(cont_internal_mapp_id.length!=undefined)
								{
									for(var k=0; k<cont_internal_mapp_id.length; k++)
									{
										if(int_map_id[i].value==cont_internal_mapp_id[k].value)
										{
											tot_mmbtu = parseFloat(cont_buyer_nom[k].value) + parseFloat(getBuyerNomContractWise(cont_internal_mapp_id[k].value,i));
											
											if(parseFloat(tot_mmbtu) > parseFloat(cont_tcq[k].value))
											{
												tcq_msg+="Contract("+temp_dis_cont_mapping[i].value+") TCQ : "+parseFloat(cont_tcq[k].value)+"\nTotal Allocation : "+parseFloat(tot_mmbtu)+"\nThe Allocation Qty should not be > Contract TCQ!\n\n";
												tcq_flag=false;
											}
										}
									}
								}
								else
								{
									if(int_map_id[i].value==cont_internal_mapp_id.value)
									{
										tot_mmbtu = parseFloat(cont_buyer_nom.value) + parseFloat(getBuyerNomContractWise(cont_internal_mapp_id.value,i));
										
										if(parseFloat(tot_mmbtu) > parseFloat(cont_tcq.value))
										{
											tcq_msg+="Contract("+temp_dis_cont_mapping[i].value+") TCQ : "+parseFloat(cont_tcq.value)+"\nTotal Allocation : "+parseFloat(tot_mmbtu)+"\nThe Allocation Qty should not be > Contract TCQ!\n\n";
											tcq_flag=false;
										}
									}
								}
							}
						}
						
						if(parseInt(sub_count) == 0 && parseInt(index1.value) > 0)
						{
							msg+="Please Select Atleast One Sub ROW for ROW - "+parseInt(i+1)+" for Submit!\n";
							flag=false;
						}
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
				sub_chk_count=0;
				if(chk.checked)
				{ 	
					chk_count++
					sub_chk_count++;
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
					
					if(trim(qty_mmbtu.value)!="")
					{
						QtyMMBTU = parseFloat(QtyMMBTU) + parseFloat(qty_mmbtu.value);
						QtyMMBTU = round(parseFloat(QtyMMBTU),2);
					}
					
					////////////////MOLECULE/////////////////////////////////////
					var mole_chk = document.getElementsByName("mole_chk_0").length;
					
					var tot_mole_qty_mmbtu = document.getElementById("tot_mole_qty_mmbtu_0")
					var remain_mole_qty_mmbtu = document.getElementById("remain_mole_qty_mmbtu_0")
					
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
								msg+="Enter Molecule MMBTU for "+mole_abbr.value+"!\n";
								flag=false;
							}
							
							mole_count++;
						}
					}
					
					if(mole_count > 0)
					{
						if(remain_mole_qty_mmbtu.value != "")
						{
							if(parseFloat(remain_mole_qty_mmbtu.value) != 0)
							{
								msg+="Remaining Molecule MMBTU should be = 0!\n";
								flag=false;
							}
						}
						else
						{
							msg+="Remaining Molecule MMBTU shouldn't be blank!\n";
							flag=false;
						}
					}
					///////////////////////////////////////////////////////////////////
					
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
					
					var tot_mmbtu =  parseFloat("0");
					if(parseFloat(qty_mmbtu.value) > 0)
					{
						if(cont_internal_mapp_id!=null && cont_internal_mapp_id!=undefined)
						{
							if(cont_internal_mapp_id.length!=undefined)
							{
								for(var k=0; k<cont_internal_mapp_id.length; k++)
								{
									if(int_map_id.value==cont_internal_mapp_id[k].value)
									{
										tot_mmbtu = parseFloat(cont_buyer_nom[k].value) + parseFloat(getBuyerNomContractWise(cont_internal_mapp_id[k].value,i));
										
										if(parseFloat(tot_mmbtu) > parseFloat(cont_tcq[k].value))
										{
											tcq_msg+="Contract("+temp_dis_cont_mapping.value+") TCQ : "+parseFloat(cont_tcq[k].value)+"\nTotal Allocation : "+parseFloat(tot_mmbtu)+"\nThe Allocation Qty should not be > Contract TCQ!\n\n";
											tcq_flag=false;
										}
									}
								}
							}
							else
							{
								if(int_map_id.value==cont_internal_mapp_id.value)
								{
									tot_mmbtu = parseFloat(cont_buyer_nom.value) + parseFloat(getBuyerNomContractWise(cont_internal_mapp_id.value,i));
									
									if(parseFloat(tot_mmbtu) > parseFloat(cont_tcq.value))
									{
										tcq_msg+="Contract("+temp_dis_cont_mapping.value+") TCQ : "+parseFloat(cont_tcq.value)+"\nTotal Allocation : "+parseFloat(tot_mmbtu)+"\nThe Allocation should not be > Contract TCQ!\n\n";
										tcq_flag=false;
									}
								}
							}
						}
					}
					
					if(parseInt(sub_count) == 0 && parseInt(index1.value) > 0)
					{
						msg+="Please Select Atleast One Sub ROW for ROW - 1 for Submit!\n";
						flag=false;
					}
				}
			}
			//alert(QtyMMBTU+"=="+ticketQty)
			
			if(sub_chk_count > 0)
			{
				if(parseFloat(QtyMMBTU) != parseFloat(ticketQty))
				{
					msg+="For "+trans_abbr.value+" Grid, Please Verify The Total Allocated QTY("+parseFloat(QtyMMBTU)+" MMBTU) With Meter Ticket QTY("+ticketQty+" MMBTU) - Both Are Different!\n";
					flag=false;
				}
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
		var a;
		if(!tcq_flag)
		{
			a = confirm(tcq_msg+"\n\nStill, Do you want to Submit Allocation?");
		}
		else
		{
			a = confirm("Do you want to Submit Allocation?");
		}
		
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
						
						calcRemainMoleQty(i);
						
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
					
					calcRemainMoleQty(i);
					
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
				
				calcRemainMoleQty("0");
			}
			
			tot_qty_mmbtu.value=round(parseFloat(tot_mmbtu),2)
			tot_qty_scm.value =round(parseFloat(tot_scm),2)
		}
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


function checkQty(index)
{
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	var buyer_nom_qty = document.getElementById("buyer_nom_qty"+index);
	
	if(parseFloat(buyer_nom_qty.value) == 0)
	{
		//alert("Missing Seller Nomination("+parseFloat(buyer_nom_qty.value)+" MMBTU)!!!");
		//qty_mmbtu.value = "";
	}
}

function checkTCQ(index)
{
	var cont_internal_mapp_id = document.forms[0].cont_internal_mapp_id;
	var cont_buyer_nom = document.forms[0].cont_buyer_nom;
	var cont_tcq = document.forms[0].cont_tcq;
	
	var int_map_id = document.getElementById("internal_map_id"+index);
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	var qty_scm = document.getElementById("qty_scm"+index);
	
	var tot_mmbtu =  parseFloat("0");
	
	if(trim(qty_mmbtu.value)!="")
	{
		if(parseFloat(qty_mmbtu.value) > 0)
		{
			if(cont_internal_mapp_id!=null && cont_internal_mapp_id!=undefined)
			{
				if(cont_internal_mapp_id.length!=undefined)
				{
					for(var i=0; i<cont_internal_mapp_id.length; i++)
					{
						if(int_map_id.value==cont_internal_mapp_id[i].value)
						{
							tot_mmbtu = parseFloat(cont_buyer_nom[i].value) + parseFloat(getBuyerNomContractWise(cont_internal_mapp_id[i].value,index));
							
							if(parseFloat(tot_mmbtu) > parseFloat(cont_tcq[i].value))
							{
								alert("Contract TCQ : "+parseFloat(cont_tcq[i].value)+"\nTotal Seller Nom : "+parseFloat(tot_mmbtu)+"\n\nThe Seller Nomination Qty should not be > Contract TCQ!");
								qty_mmbtu.value="";
								qty_scm.value="";
							}
						}
					}
				}
				else
				{
					if(int_map_id.value==cont_internal_mapp_id.value)
					{
						tot_mmbtu = parseFloat(cont_buyer_nom.value) + parseFloat(getBuyerNomContractWise(cont_internal_mapp_id.value,index));
						
						if(parseFloat(tot_mmbtu) > parseFloat(cont_tcq.value))
						{
							alert("Contract TCQ : "+parseFloat(cont_tcq.value)+"\nTotal Seller Nom : "+parseFloat(tot_mmbtu)+"\n\nThe Seller Nomination Qty should not be > Contract TCQ!");
							qty_mmbtu.value="";
							qty_scm.value="";
						}
					}
				}
			}
		}
	}
}

function getBuyerNomContractWise(cont_internal_mapp_id, index)
{
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var internal_map_id = document.forms[0].internal_map_id;
	var temp_nom_rev_no = document.forms[0].temp_nom_rev_no;
	
	var tot_mmbtu =  parseFloat("0");
	
	if(qty_mmbtu!=null && qty_mmbtu!=undefined)
	{
		if(qty_mmbtu.length!=undefined)
		{
			for(var j=0; j<qty_mmbtu.length; j++)
			{
				if(cont_internal_mapp_id == internal_map_id[j].value)
				{
					if(trim(qty_mmbtu[j].value)!="" && trim(temp_nom_rev_no[j].value)!="")
					{
						tot_mmbtu = parseFloat(tot_mmbtu) + parseFloat(qty_mmbtu[j].value);
					}
					else if(index==j && trim(qty_mmbtu[j].value)!="")
					{
						tot_mmbtu = parseFloat(tot_mmbtu) + parseFloat(qty_mmbtu[j].value);
					}
				}
				
			}
		}
		else
		{
			if(cont_internal_mapp_id == internal_map_id.value)
			{
				if(trim(qty_mmbtu.value)!="")
				{
					tot_mmbtu = parseFloat(tot_mmbtu) + parseFloat(qty_mmbtu.value);
				}
			}
		}
	}
	
	return tot_mmbtu;
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
										{							
											qty_mmbtu[i].value=arr[k];
											
											negNumber(qty_mmbtu[i]);
											checkNumber1(qty_mmbtu[i],9,2);
											checkQty(i);
											calculateSCM(i);
											totalQty();
										}
										k++;
									}
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
										calculateSCM(i);
										totalQty();
									}
									k++;
								}
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
								calculateSCM(i);
								totalQty();
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
String prevdate = utildate.getPreviousDate();

String gas_dt = request.getParameter("gas_dt")==null?prevdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "1");

cont_mgmt.setCallFlag("DAILY_ALLOCATION");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setGas_dt(gas_dt);
cont_mgmt.init();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = cont_mgmt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_STATUS = cont_mgmt.getVCOUNTERPARTY_STATUS();
Vector VTRANSPORTER_CD = cont_mgmt.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = cont_mgmt.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_STATUS = cont_mgmt.getVTRANSPORTER_STATUS();
Vector VTRANSPORTER_PLANT_SEQ = cont_mgmt.getVTRANSPORTER_PLANT_SEQ();
Vector VTRANSPORTER_PLANT_ABBR = cont_mgmt.getVTRANSPORTER_PLANT_ABBR();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = cont_mgmt.getVCOUNTERPARTY_PLANT_ABBR();

Vector VNOM_REV_NO = cont_mgmt.getVNOM_REV_NO();
Vector VGEN_TIME = cont_mgmt.getVGEN_TIME();
Vector VGEN_DT = cont_mgmt.getVGEN_DT();
Vector VBASE = cont_mgmt.getVBASE();
Vector VGCV = cont_mgmt.getVGCV();
Vector VNCV = cont_mgmt.getVNCV();
Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();
Vector VQTY_SCM = cont_mgmt.getVQTY_SCM();
Vector VNOM_COLOR = cont_mgmt.getVNOM_COLOR();
Vector VDCQ = cont_mgmt.getVDCQ();
Vector VCONT_NAME = cont_mgmt.getVCONT_NAME();
Vector VCONT_REF = cont_mgmt.getVCONT_REF();
Vector VMDCQ_QTY = cont_mgmt.getVMDCQ_QTY();
Vector VINTERNAL_MAP_ID = cont_mgmt.getVINTERNAL_MAP_ID();
Vector VBUYER_NOM_REV_NO = cont_mgmt.getVBUYER_NOM_REV_NO();
Vector VBUYER_NOM = cont_mgmt.getVBUYER_NOM();
Vector VBU_CD = cont_mgmt.getVBU_CD();
Vector VBU_PLANT_SEQ = cont_mgmt.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = cont_mgmt.getVBU_PLANT_ABBR();
Vector VCUSTOMER_CODE = cont_mgmt.getVCUSTOMER_CODE();
Vector VNOM_BLOCK = cont_mgmt.getVNOM_BLOCK();
Vector VCARGO_NO = cont_mgmt.getVCARGO_NO();

Vector VCONT_NO = cont_mgmt.getVCONT_NO();
Vector VCONT_REV_NO = cont_mgmt.getVCONT_REV_NO();
Vector VAGMT_NO = cont_mgmt.getVAGMT_NO();
Vector VAGMT_REV_NO = cont_mgmt.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = cont_mgmt.getVCONTRACT_TYPE();
Vector VDIS_CONT_MAPPING = cont_mgmt.getVDIS_CONT_MAPPING();
Vector VTRANS_PLANT_WISE_TOTAL_MMBTU = cont_mgmt.getVTRANS_PLANT_WISE_TOTAL_MMBTU();
Vector VTRANS_PLANT_WISE_TOTAL_SCM = cont_mgmt.getVTRANS_PLANT_WISE_TOTAL_SCM();
Vector VTOTAL_MMBTU_COLOR = cont_mgmt.getVTOTAL_MMBTU_COLOR();

Vector VINDEX = cont_mgmt.getVINDEX();
Vector VINDEX1 = cont_mgmt.getVINDEX1();
Vector VSUB_INDEX = cont_mgmt.getVSUB_INDEX();

Vector VCONT_INTERNAL_MAPP_ID = cont_mgmt.getVCONT_INTERNAL_MAPP_ID();
Vector VCONT_TCQ = cont_mgmt.getVCONT_TCQ();
Vector VCONT_BUYER_NOM = cont_mgmt.getVCONT_BUYER_NOM();

Vector VMETER_QTY_MMBTU = cont_mgmt.getVMETER_QTY_MMBTU();
Vector VMETER_QTY_SCM = cont_mgmt.getVMETER_QTY_SCM();
Vector VMETER_GCV = cont_mgmt.getVMETER_GCV();
Vector VMETER_NCV = cont_mgmt.getVMETER_NCV();

Vector VIS_EXIST = cont_mgmt.getVIS_EXIST();

Vector VSUB_NOM_REV_NO = cont_mgmt.getVSUB_NOM_REV_NO();
Vector VSUB_QTY_MMBTU = cont_mgmt.getVSUB_QTY_MMBTU();
Vector VSUB_QTY_SCM = cont_mgmt.getVSUB_QTY_SCM();
Vector VSUB_CT_REF = cont_mgmt.getVSUB_CT_REF();
Vector VSUB_UTR_REF = cont_mgmt.getVSUB_UTR_REF();
Vector VSUB_IS_EXIST = cont_mgmt.getVSUB_IS_EXIST();
Vector VSUB_SEQ_NO = cont_mgmt.getVSUB_SEQ_NO();
Vector VSUB_NOM_COLOR = cont_mgmt.getVSUB_NOM_COLOR();

Vector VSUB_BUYER_NOM_QTY = cont_mgmt.getVSUB_BUYER_NOM_QTY();
Vector VSUB_BUYER_NOM_REV = cont_mgmt.getVSUB_BUYER_NOM_REV();

Vector VPRODUCT_ABBR = cont_mgmt.getVPRODUCT_ABBR();
Vector VMOLECULE_ABBR = cont_mgmt.getVMOLECULE_ABBR();
Vector VMOLECULE_MAPPING = cont_mgmt.getVMOLECULE_MAPPING();

Vector VALLOC_MOLE_MAPPING = cont_mgmt.getVALLOC_MOLE_MAPPING();
Vector VALLOC_MOLE_SEQ_NO = cont_mgmt.getVALLOC_MOLE_SEQ_NO();
Vector VALLOC_MOLE_QTY_MMBTU = cont_mgmt.getVALLOC_MOLE_QTY_MMBTU();
Vector VALLOC_MOLE_QTY_SCM = cont_mgmt.getVALLOC_MOLE_QTY_SCM();
Vector VALLOC_MOLE_EXIST = cont_mgmt.getVALLOC_MOLE_EXIST();
Vector VALLOC_MOLE_COLOR = cont_mgmt.getVALLOC_MOLE_COLOR();

%>
<body onload="enabled();">
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
					    	Daily Allocation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
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
				<%if(VTRANSPORTER_CD.size() > 0){ %>
				<div class="card-body cdbody">
					<%int j=0,k=0,l=0,m=0,p=0,q=0;
					for(int i=0; i<VTRANSPORTER_CD.size(); i++)
					{ 
						String trans_cd=""+VTRANSPORTER_CD.elementAt(i);
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
					%>
					<%if(i>0){ %>&nbsp;<%} %>
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
								
								String gcv = ""+VMETER_GCV.elementAt(j);
								String ncv = ""+VMETER_NCV.elementAt(j);
							%>
								<input type="hidden" name="sub_index" value="<%=sub_index%>">
								<input type="hidden" name="trans_abbr" value="<%=VTRANSPORTER_PLANT_ABBR.elementAt(j)%>">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading<%=l%>">
    										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
								    			<font><%=VTRANSPORTER_PLANT_ABBR.elementAt(j)%>&nbsp;&nbsp;
								    			<%if(Double.parseDouble(""+VTRANS_PLANT_WISE_TOTAL_MMBTU.elementAt(j)) > 0){%>
								    				<font color="<%=VTOTAL_MMBTU_COLOR.elementAt(j) %>" style="background: white;padding: 2px 5px 4px 5px;border-radius: 30px;">
								    					[<%=VTRANS_PLANT_WISE_TOTAL_MMBTU.elementAt(j)%> MMBTU] [<%=VTRANS_PLANT_WISE_TOTAL_SCM.elementAt(j)%> SCM]
								    				</font>
								    			<%} %></font>
								      		</button>
								    	</h2>
								    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
								      		<div class="accordion-body accor-body">
								      			<div class="row m-b-5" style="background:#bce6ff;color:#0c63e4;">
								      				<div class="col-sm-1 col-xs-1 col-md-1">
								      					<div class="form-group row">
															<div class="col-auto">
																<label class="form-label"><b>TICKET QTY : </b></label>
															</div>
														</div>
													</div>
													<div class="col-sm-2 col-xs-2 col-md-2">
														<div class="form-group row">
										    				<div class="col-auto">
										    					<input type="text" class="form-control form-control-sm" name="ticket_qty_mmbtu" value="<%=VMETER_QTY_MMBTU.elementAt(j)%>" style="width:80px;text-align:right;" readOnly>
										    				</div>
										    				<div class="col-auto">
										    					<label class="form-label"><b>MMBTU</b></label>
										    				</div>
										    			</div>
										    		</div>
										    		<div class="col-sm-2 col-xs-2 col-md-2">
														<div class="form-group row">
										    				<div class="col-auto">
										    					<input type="text" class="form-control form-control-sm" name="ticket_qty_scm" value="<%=VMETER_QTY_SCM.elementAt(j)%>" style="width:80px;text-align:right;" readOnly>
										    				</div>
										    				<div class="col-auto">
										    					<label class="form-label"><b>SCM</b></label>
										    				</div>
										    			</div>
										    		</div>
										    		<div class="col-sm-1 col-xs-1 col-md-1">
										    		</div>
										    		<div class="col-sm-3 col-xs-3 col-md-3">
										    			<div class="form-group row">
															<div class="col-auto">
																<label class="form-label"><b>GCV : </b></label>
															</div>
										    				<div class="col-auto">
										    					<input type="text" class="form-control form-control-sm" value="<%=VMETER_GCV.elementAt(j)%>" style="width:80px;text-align:right;" readOnly>
										    				</div>
										    				<div class="col-auto">
																<label class="form-label"><b>KCal/SCM</b></label>
															</div>
														</div>
													</div>
													<div class="col-sm-3 col-xs-3 col-md-3">
										    			<div class="form-group row">
										    				<div class="col-auto">
										    					<label class="form-label"><b>NCV : </b></label>
										    				</div>
										    				<div class="col-auto">
										    					<input type="text" class="form-control form-control-sm" value="<%=VMETER_NCV.elementAt(j)%>" style="width:80px;text-align:right;" readOnly>
										    				</div>
										    				<div class="col-auto">
																<label class="form-label"><b>KCal/SCM</b></label>
															</div>
										    			</div>
										    		</div>
												</div>
								        		<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr>
																	<th></th>
																	<th>Contract#<br>[Contract/Trade Ref#]</th>
																	<th>Business Unit</th>
																	<th>Customer</th>
																	<th>Customer Plant<br><font style="background:#ADD8E6;">Customer Code</font></th>
																	<th>DCQ</th>
																	<th>Seller Nomination (Rev)<br>(MMBTU)</th>
																	<th>Pre-Day Method<br>(MMBTU)</th>
																	<th>Alloc Qty (MMBTU)</th>
																	<th>Alloc Qty (SCM)</th>
																	<th>Rev#</th>
																	<th>Map Molecule</th>
																	<th>Gen Time</th>
																	<th>Calorific Value Base<br>KCal/SCM</th>
																</tr>
															</thead>
															<tbody>
																<%m=0;
																if(sub_index>0){ %>
																	<%for(l=l; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
																	{
																		int index1=Integer.parseInt(""+VINDEX1.elementAt(l));
																		m+=1;
																	%>
																		<tr>
																			<td align="center" valign="middle" <%if(VNOM_BLOCK.elementAt(l).equals("Y")) {%>style="background: #df9fbf;" title="Invoice Generated!"<%} %>>
																				<input type="checkbox" class="form-check-input" name="chk" id="chk<%=l%>" 
																				onclick="setEnableDisabled(this,'<%=l%>');calculateSCM('<%=l%>');totalSubQty('<%=l%>');totalQty();" 
																				<%if(VNOM_BLOCK.elementAt(l).equals("Y")) {%>disabled style="pointer-events: none;"<%} %>
																				<%if(VTRANSPORTER_STATUS.elementAt(l).equals("N") || VCOUNTERPARTY_STATUS.elementAt(l).equals("N")) {%>disabled style="pointer-events: none;"<%} %>>
																				<input type="hidden" name="is_exist" id="is_exist<%=l%>" value="<%=VIS_EXIST.elementAt(l)%>">
																				<input type="hidden" name="nom_block" id="nom_block<%=l%>" value="<%=VNOM_BLOCK.elementAt(l)%>" disabled>
																				<input type="hidden" name="index1" id="index1<%=l%>" value="<%=index1%>" disabled>
																				<input type="hidden" name="index" id="index_<%=l%>" value="<%=l%>" disabled>
																			</td>
																			<td>
																				<%=VDIS_CONT_MAPPING.elementAt(l)%>
																				<%if(!VCONT_REF.elementAt(l).equals("")){%>
																					<br>(<%=VCONT_REF.elementAt(l)%>)
																				<%} %>
																				<input type="hidden" name="temp_dis_cont_mapping" id="temp_dis_cont_mapping_<%=l%>" value="<%=VDIS_CONT_MAPPING.elementAt(l)%> (<%=VCONT_REF.elementAt(l)%>)" disabled>
																			</td>
																			<td align="center">
																				<%=VBU_PLANT_ABBR.elementAt(l)%>
																				<input type="hidden" name="bu_plant_seq" id="bu_plant_seq<%=l%>" value="<%=VBU_PLANT_SEQ.elementAt(l)%>" disabled>
																			</td>
																			<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(l)%>">
																				<%=VCOUNTERPARTY_ABBR.elementAt(l) %>
																				<span 
																				<%if(VCOUNTERPARTY_STATUS.elementAt(l).equals("N")){ %>class='alert alert-danger' title="Counterparty Deactive "
																				<%}else if(VCOUNTERPARTY_STATUS.elementAt(l).equals("E")){ %>class='alert alert-warning'
																				<%} %>
																				><b> <%if(VCOUNTERPARTY_STATUS.elementAt(l).equals("N")){ %> De-active 
																				<%}else if(VCOUNTERPARTY_STATUS.elementAt(l).equals("E")){ %> E-Rate
																				<%} %> </b>
																				</span>
																				<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=l%>" value="<%=VCOUNTERPARTY_CD.elementAt(l)%>" disabled>
																				<input type="hidden" name="agmt_no" id="agmt_no<%=l%>" value="<%=VAGMT_NO.elementAt(l)%>" disabled>
						      													<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=l%>" value="<%=VAGMT_REV_NO.elementAt(l)%>" disabled>
															      				<input type="hidden" name="cont_no" id="cont_no<%=l%>" value="<%=VCONT_NO.elementAt(l)%>" disabled>
															      				<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=l%>" value="<%=VCONT_REV_NO.elementAt(l)%>" disabled>
															      				<input type="hidden" name="contract_type" id="contract_type<%=l%>" value="<%=VCONTRACT_TYPE.elementAt(l)%>" disabled>
															      				<input type="hidden" name="cargo_no" id="cargo_no<%=l%>" value="<%=VCARGO_NO.elementAt(l)%>" disabled>
															      				<input type="hidden" name="trans_cd" id="trans_cd<%=l%>" value="<%=trans_cd%>" disabled>
															      				<input type="hidden" name="trans_plant_seq" id="trans_plant_seq<%=l%>" value="<%=trans_plant_seq%>" disabled>
															      				<input type="hidden" name="internal_map_id" id="internal_map_id<%=l%>" value="<%=VINTERNAL_MAP_ID.elementAt(l)%>">
																			</td>
																			<td align="center">
																				<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%>
																				<%if(!VCUSTOMER_CODE.elementAt(l).equals("")){%>
																				<br><font style="background:#ADD8E6;"><%=VCUSTOMER_CODE.elementAt(l)%></font>
																				<%} %>
																				<input type="hidden" name="plant_seq" id="plant_seq<%=l%>" value="<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(l)%>" disabled>
																			</td>
																			<td align="right">
																				<%=VDCQ.elementAt(l)%>
																				<input type="hidden" value="<%=VDCQ.elementAt(l)%>" name="dcq" id="dcq<%=l%>">
																				<input type="hidden" value="<%=VMDCQ_QTY.elementAt(l)%>" name="mdcq_qty" id="mdcq_qty<%=l%>">
																			</td>
																			<td align="right">
																				<%=VBUYER_NOM.elementAt(l)%> (<%=VBUYER_NOM_REV_NO.elementAt(l)%>)
																				<input type="hidden" name="buyer_nom_qty" id="buyer_nom_qty<%=l%>" value="<%=VBUYER_NOM.elementAt(l)%>">
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" value="<%=VQTY_MMBTU.elementAt(l)%>" style="text-align:right" readOnly>
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="qty_mmbtu" id="qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" 
																					style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" 
																					<%-- onpaste="doPaste('<%=j%>','<%=l%>');" --%> 
																					onblur="negNumber(this);checkNumber1(this,9,2);checkQty('<%=l%>');calculateSCM('<%=l%>');totalQty();" disabled>
																					<input type="hidden" class="form-control form-control-sm" name="tmp_qty_mmbtu" id="tmp_qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" style="text-align:right" disabled>
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="qty_scm" id="qty_scm<%=l%>" value="<%=VQTY_SCM.elementAt(l)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
																				</div>
																			</td>
																			<td align="center">
																				<%=VNOM_REV_NO.elementAt(l)%>
																				<input type="hidden" name="temp_nom_rev_no" id="temp_nom_rev_no_<%=l%>" value="<%=VNOM_REV_NO.elementAt(l)%>" disabled>
																			</td>
																			<td>
																				<div class="row m-b-5">
																					<div class="col">
																						<input type="button" class="btn btn-sm config_btn" id="moleConfigBtn<%=l%>" style="display: none;" value="MM" onclick="variableMolecule('<%=l%>');">
																					</div>
																					<div class="col" id="hed_mole_qty<%=l%>">
																					</div>
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:75px;">
																					<div class="row m-b-5">
																						<div class="col">
																		      				<div class="input-group input-group-sm" >
																	      						<input type="text" class="form-control form-control-sm" name="gen_time" id="gen_time<%=l%>" value="<%=VGEN_TIME.elementAt(l)%>" maxLength="5" 
																	      						style="width:15px;background:<%=VNOM_COLOR.elementAt(l)%>"
																	      						onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off" disabled>
																	      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
																      						</div>
															      						</div>
															      					</div>
															      				</div>
																			</td>
																			<td align="center">
																				<div> <!-- style="width:300px;"> -->
																					<div class="row m-b-5">
																						<div class="col">
																							<input type="radio" name="rd<%=l%>" id="rd1<%=l%>" onclick="calculateSCM('<%=l%>');totalQty();" <%if(VBASE.elementAt(l).equals("GCV")){ %>checked<%} %> disabled>&nbsp;GCV
																	      				</div>
																	      				<%-- <div class="col">
																	      					<input type="text" class="form-control form-control-sm" name="gcv" id="gcv<%=l%>" value="<%=VGCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,7,2);calculateSCM('<%=l%>');" disabled>
																	      				</div> --%>
																	      				<input type="hidden" class="form-control form-control-sm" name="gcv" id="gcv<%=l%>" value="<%=gcv%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);calculateSCM('<%=l%>');" disabled>
																	      				<div class="col">
																	      					<input type="radio" name="rd<%=l%>" id="rd2<%=l%>" onclick="calculateSCM('<%=l%>');totalQty();" <%if(VBASE.elementAt(l).equals("NCV")){ %>checked<%} %> disabled>&nbsp;NCV 
																	      				</div>
																	      				<%-- <div class="col">
																		      				<input type="text" class="form-control form-control-sm" name="ncv" id="ncv<%=l%>" value="<%=VNCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,7,2);calculateSCM('<%=l%>');" disabled>
															      						</div> --%>
															      						<input type="hidden" class="form-control form-control-sm" name="ncv" id="ncv<%=l%>" value="<%=ncv%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);calculateSCM('<%=l%>');" disabled>
															      						<input type="hidden" name="base" id="base<%=l%>" value="<%=VBASE.elementAt(l)%>" disabled>
															      					</div>
															      				</div>
																			</td>
																		</tr>
																		<%q=0;
																		if(index1>0){ %>
																			<tbody id="tbody<%=j%>" style="display:;">
																				<tr style="font-weight: bold;background:#bce6ff;text-align: center;">
																					<td rowspan="<%=index1+2%>" colspan="5" style="background:white;"></td>
																					<td>Contract ID [UTR#]</td>
																					<td>Seller Nomination (Rev)<br>(MMBTU)</td>
																					<td></td>
																					<td>Energy (MMBTU)</td>
																					<td>Energy (SCM)</td>
																					<td>Rev#</td>
																					<td rowspan="<%=index1+2%>" colspan="3" style="background:white;"></td>
																				</tr>
																			<%for(p=p; p<VSUB_CT_REF.size(); p++)
																			{ 
																				q+=1;
																			%>
																				<tr>
																					<td>
																						<input type="checkbox" class="form-check-input" name="chk<%=l%>" id="chk<%=l%><%=q%>" 
																						<%if(VSUB_IS_EXIST.elementAt(p).equals("Y")){ %>style="pointer-events: none;" checked<%} %>
																						onclick="setSubEnableDisabled(this,'<%=l%>','<%=q%>');calculateSCM('<%=l%>');totalSubQty('<%=l%>');checkQty('<%=l%>');totalQty();" disabled>
																						<input type="hidden" name="sub_is_exist" id="sub_is_exist<%=l%><%=q%>" value="<%=VSUB_IS_EXIST.elementAt(p)%>">
																						<input type="hidden" name="sub_seq_no<%=l%>" id="sub_seq_no<%=l%><%=q%>" value="<%=VSUB_SEQ_NO.elementAt(p)%>">
																					<!-- </td>
																					<td align="center"> -->
																						&nbsp;
																						<%if(!VSUB_CT_REF.elementAt(p).equals("")){ %>
																							<%=VSUB_CT_REF.elementAt(p)%>
																							
																							<%if(!VSUB_UTR_REF.elementAt(p).equals("")){ %><br>
																								[<%=VSUB_UTR_REF.elementAt(p)%>]
																							<%} %>
																						<%}else{ %>
																							<font color="red">No Contract ID</font>
																						<%} %>
																						<input type="hidden" class="form-control form-control-sm" name="sub_ct_ref<%=l%>" id="sub_ct_ref<%=l%><%=q%>" value="<%=VSUB_CT_REF.elementAt(p)%>" readOnly disabled>
																						<input type="hidden" class="form-control form-control-sm" name="sub_utr_ref<%=l%>" id="sub_utr_ref<%=l%><%=q%>" value="<%=VSUB_UTR_REF.elementAt(p)%>" readOnly disabled>
																						
																					</td>
																					<td align="right">
																						<%=VSUB_BUYER_NOM_QTY.elementAt(p)%> (<%=VSUB_BUYER_NOM_REV.elementAt(p)%>)
																					</td>
																					<td></td>
																					<td align="center">
																						<div style="width:100px;">
																							<input type="text" class="form-control form-control-sm" name="sub_qty_mmbtu<%=l%>" id="sub_qty_mmbtu<%=l%><%=q%>" value="<%=VSUB_QTY_MMBTU.elementAt(p)%>" 
																							style="text-align:right;background:<%=VSUB_NOM_COLOR.elementAt(p)%>" 
																							onblur="negNumber(this);checkNumber1(this,9,2);calculateSCM('<%=l%>');totalSubQty('<%=l%>');checkQty('<%=l%>');totalQty();" disabled>
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
																				</tr>
																				<%if(q==index1){%>
																					<tr>
																						<td colspan="6">&nbsp;</td>
																					</tr>
																					<%p++;
																					break;
																				}%>
																			<%} %>
																			</tbody>
																		<%} %>
																		<%if(m==sub_index)
																		{%>
																			<tr>
																				<td colspan="8" align="right">
																					<b>Total(<%=VTRANSPORTER_PLANT_ABBR.elementAt(j)%>)</b>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_qty_mmbtu" value="<%=VTRANS_PLANT_WISE_TOTAL_MMBTU.elementAt(j)%>" style="text-align:right;font-weight: bold;" readOnly>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_qty_scm" value="<%=VTRANS_PLANT_WISE_TOTAL_SCM.elementAt(j)%>" style="text-align:right;font-weight: bold;" readOnly>
																					</div>
																				</td>
																				<td align="right" colspan="4">
																				</td>
																			</tr>
																			<%l=l+1;
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
				<%}else{ %>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div align="center"><%=utilmsg.infoMessage("<b>Seller Nomination is Pending for the Selected Gas Day!</b>") %></div>
					</div>
				</div>
				<%} %>
			</div>
		</div>
	</div>
</div>

<%for(int i=0; i<VCOUNTERPARTY_PLANT_SEQ.size(); i++){
	Vector TEMP_MOLE_MAP=new Vector();
	Vector TEMP_MOLE_SEQ_NO=new Vector();
	Vector TEMP_MOLE_QTY_MMBTU=new Vector();
	Vector TEMP_MOLE_QTY_SCM=new Vector();
	Vector TEMP_MOLE_EXIST=new Vector();
	Vector TEMP_MOLE_COLOR=new Vector();
	
	TEMP_MOLE_MAP=(Vector) VALLOC_MOLE_MAPPING.elementAt(i);
	TEMP_MOLE_SEQ_NO=(Vector) VALLOC_MOLE_SEQ_NO.elementAt(i);
	TEMP_MOLE_QTY_MMBTU=(Vector) VALLOC_MOLE_QTY_MMBTU.elementAt(i);
	TEMP_MOLE_QTY_SCM=(Vector) VALLOC_MOLE_QTY_SCM.elementAt(i);
	TEMP_MOLE_EXIST=(Vector) VALLOC_MOLE_EXIST.elementAt(i);
	TEMP_MOLE_COLOR=(Vector) VALLOC_MOLE_COLOR.elementAt(i);
%>
<div class="modal fade" id="MoleculeModal_<%=i %>" data-bs-backdrop="static" data-bs-keyboard="false">
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
											<input type="checkbox" class="form-check-input" name="mole_chk_<%=i%>" id="mole_chk_<%=j%>_<%=i%>" onclick="setEnableDisableMolecule(this,'<%=j%>','<%=i%>')" disabled 
											<%if(TEMP_MOLE_MAP.contains(mole_map)){ %>checked<%} %>
											<%if(mole_exist.equals("Y")){ %>style="pointer-events: none;"<%} %>>
											<input type="hidden" name="mole_mapping_<%=i%>" id="mole_mapping_<%=j%>_<%=i%>" value="<%=VMOLECULE_MAPPING.elementAt(j)%>" disabled>
											<input type="hidden" name="mole_abbr_<%=i%>" id="mole_abbr_<%=j%>_<%=i%>" value="<%=VMOLECULE_ABBR.elementAt(j)%>" disabled>
											<input type="hidden" name="mole_seq_no_<%=i%>" id="mole_seq_no_<%=j%>_<%=i%>" value="<%=mole_seq_no%>" disabled>
										</td>
										<td align="center"><%=VPRODUCT_ABBR.elementAt(j)%></td>
										<td align="center"><%=VMOLECULE_ABBR.elementAt(j)%></td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="mole_qty_mmbtu_<%=i%>" id="mole_qty_mmbtu_<%=j%>_<%=i%>" 
												value="<%=mole_qty_mmbtu%>" 
												style="text-align:right;background: <%=color%>"
												onkeyup="negNumber(this);checkNumber1(this,9,2);calculateSCM('<%=i%>');calcRemainMoleQty('<%=i%>');" 
												onblur="negNumber(this);checkNumber1(this,9,2);calculateSCM('<%=i%>');calcRemainMoleQty('<%=i%>');" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="mole_qty_scm_<%=i%>" id="mole_qty_scm_<%=j%>_<%=i%>" 
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
												<input type="text" class="form-control form-control-sm" name="tot_mole_qty_mmbtu" id="tot_mole_qty_mmbtu_<%=i%>" value="" 
												style="text-align:right;font-weight: bold;" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="tot_mole_qty_scm" id="tot_mole_qty_scm_<%=i%>" value="" 
												style="text-align:right;font-weight: bold;" readonly disabled>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="3" align="right">
											<b>Remaining MMBTU </b>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="remain_mole_qty_mmbtu" id="remain_mole_qty_mmbtu_<%=i%>" value="" 
												style="text-align:right;font-weight: bold;" disabled>
											</div>
										</td>
										<td align="center">
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="" align="right">
					<input type="button" class="btn btn-warning com-btn" value="Proceed" onclick="doMoleSubmit('<%=i%>')">
				</div>
      		</div>
      	</div>
	</div>
</div>
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
	var hed_mole_qty = document.getElementById("hed_mole_qty"+index);
	
	var mole_chk = document.getElementsByName("mole_chk_"+index).length;
	
	var tot_mole_qty_mmbtu = document.getElementById("tot_mole_qty_mmbtu_"+index)
	var tot_mole_qty_scm = document.getElementById("tot_mole_qty_scm_"+index)
	var remain_mole_qty_mmbtu = document.getElementById("remain_mole_qty_mmbtu_"+index)
	
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
	
	hed_mole_qty.innerHTML=tot_mole_qty_mmbtu.value
	
	if(qty_mmbtu.value!="")
	{
		rem_qty_mmbtu = parseFloat(qty_mmbtu.value) - tot_qty_mmbtu;
		
		remain_mole_qty_mmbtu.value=round(rem_qty_mmbtu,2);
	}
}

function doMoleSubmit(index)
{
	var mole_chk = document.getElementsByName("mole_chk_"+index).length;
	
	var tot_mole_qty_mmbtu = document.getElementById("tot_mole_qty_mmbtu_"+index)
	var remain_mole_qty_mmbtu = document.getElementById("remain_mole_qty_mmbtu_"+index)
	
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
	
	if(remain_mole_qty_mmbtu.value != "")
	{
		if(parseFloat(remain_mole_qty_mmbtu.value) != 0)
		{
			msg+="Remaining MMBTU should be = 0!\n";
			flag=false;
		}
	}
	else
	{
		msg+="Remaining MMBTU shouldn't be blank!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Procced?");
		if(a)
		{
			document.getElementById("hed_mole_qty"+index).innerHTML=tot_mole_qty_mmbtu.value;
			$("#MoleculeModal_"+index).modal("hide");
		}
	}
	else
	{
		alert(msg);
	}
}
</script>

<%for(int i=0; i<VCONT_INTERNAL_MAPP_ID.size(); i++){ %>
<input type="hidden" name="cont_internal_mapp_id" value="<%=VCONT_INTERNAL_MAPP_ID.elementAt(i)%>">
<input type="hidden" name="cont_buyer_nom" value="<%=VCONT_BUYER_NOM.elementAt(i)%>">
<input type="hidden" name="cont_tcq" value="<%=VCONT_TCQ.elementAt(i)%>">
<%} %>

<input type="hidden" name="option" value="DAILY_ALLOCATION">

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