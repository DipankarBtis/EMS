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
	
	var url = "frm_daily_transporter_nom.jsp?gas_dt="+gas_dt+"&u="+u;

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
	
	var gen_time = document.getElementById("gen_time"+index);
	var rd1 = document.getElementById("rd1"+index);
	var rd2 = document.getElementById("rd2"+index);
	var gcv = document.getElementById("gcv"+index);
	var ncv = document.getElementById("ncv"+index);
	var base = document.getElementById("base"+index);
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	var qty_scm = document.getElementById("qty_scm"+index);
	
	var gta_no = document.getElementById("gta_no"+index);
	var gtc_no = document.getElementById("gtc_no"+index);
	var entry_point_map = document.getElementById("entry_point_map"+index);
	var exit_point_map = document.getElementById("exit_point_map"+index);
	var cont_map = document.getElementById("cont_map"+index);
	
	var exit_rd1 = document.getElementById("exit_rd1"+index);
	var exit_rd2 = document.getElementById("exit_rd2"+index);
	var exit_gcv = document.getElementById("exit_gcv"+index);
	var exit_ncv = document.getElementById("exit_ncv"+index);
	var exit_base = document.getElementById("exit_base"+index);
	var exit_qty_mmbtu = document.getElementById("exit_qty_mmbtu"+index);
	var exit_qty_scm = document.getElementById("exit_qty_scm"+index);
	
	var plant_seq = document.getElementById("plant_seq"+index);
	var trans_cd = document.getElementById("trans_cd"+index);
	var trans_plant_seq = document.getElementById("trans_plant_seq"+index);
	var bu_plant_seq = document.getElementById("bu_plant_seq"+index);
	
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
		
		rd1.style.pointerEvents = "none";
		rd2.style.pointerEvents = "none";
		gcv.readOnly=true;
		ncv.readOnly=true;
		base.readOnly=true;
		qty_mmbtu.readOnly=true;
		qty_scm.readOnly=true;
		
		gta_no.disabled=false;
		gtc_no.disabled=false;
		entry_point_map.disabled=false;
		exit_point_map.disabled=false;
		cont_map.disabled=false;
		
		exit_rd1.disabled=false;
		exit_rd2.disabled=false;
		exit_gcv.disabled=false;
		exit_ncv.disabled=false;
		exit_base.disabled=false;
		exit_qty_mmbtu.disabled=false;
		exit_qty_scm.disabled=false;
		
		plant_seq.disabled=false;
		trans_cd.disabled=false;
		trans_plant_seq.disabled=false;
		bu_plant_seq.disabled=false;
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
		
		rd1.style.pointerEvents = "none";
		rd2.style.pointerEvents = "none";
		gcv.readOnly=true;
		ncv.readOnly=true;
		base.readOnly=true;
		qty_mmbtu.readOnly=true;
		qty_scm.readOnly=true;
		
		gta_no.disabled=true;
		gtc_no.disabled=true;
		entry_point_map.disabled=true;
		exit_point_map.disabled=true;
		cont_map.disabled=true;
		
		exit_rd1.disabled=true;
		exit_rd2.disabled=true;
		exit_gcv.disabled=true;
		exit_ncv.disabled=true;
		exit_base.disabled=true;
		exit_qty_mmbtu.disabled=true;
		exit_qty_scm.disabled=true;
		
		plant_seq.disabled=true;
		trans_cd.disabled=true;
		trans_plant_seq.disabled=true;
		bu_plant_seq.disabled=true;
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
	
	calculateExitSCM(index)
}

function calculateExitSCM(index)
{
	var exit_rd1 = document.getElementById("exit_rd1"+index);
	var exit_rd2 = document.getElementById("exit_rd2"+index);
	var exit_base = document.getElementById("exit_base"+index);
	var exit_gcv = document.getElementById("exit_gcv"+index);
	var exit_ncv = document.getElementById("exit_ncv"+index);
	var exit_qty_mmbtu = document.getElementById("exit_qty_mmbtu"+index);
	var exit_qty_scm = document.getElementById("exit_qty_scm"+index);
	
	var baseVal = parseFloat("0");
	
	var deviding_factor = parseFloat("1");
	
	
	if(exit_rd1.checked)
	{
		if(trim(exit_gcv.value) !=""){
			baseVal = parseFloat(exit_gcv.value);
		}
		deviding_factor = parseFloat("1");
		exit_base.value="GCV"
	}
	else if(exit_rd2.checked)
	{
		if(trim(exit_ncv.value) !=""){
			baseVal = parseFloat(exit_ncv.value);
		}
		deviding_factor = parseFloat("1.11");
		exit_base.value="NCV"
	}
	
	var multiplying_factor_2 = 0.252; //For Converting MMBTU TO MMSCM ...
	var multiplying_factor = 0.252*1000000; //For Converting MMBTU TO SCM ...
	
	if((exit_qty_mmbtu.value!=null && trim(exit_qty_mmbtu.value) !=''))
	{
		//DT20251121 Incident#BUG 2510010:changes done for rounding SCM val upto 2 decimal digits
		/* var scm = ""+round(((parseFloat(""+exit_qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),0); */
		var scm = ""+round(((parseFloat(""+exit_qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),2);
		if(isNaN(scm))
		{
			exit_qty_scm.value="";
		}
		else
		{
			exit_qty_scm.value = scm;
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
	
	var gta_no = document.forms[0].gta_no;
	var gtc_no = document.forms[0].gtc_no;
	var entry_point_map = document.forms[0].entry_point_map;
	var exit_point_map = document.forms[0].exit_point_map;
	var cont_map = document.forms[0].cont_map;
	
	var exit_gcv = document.forms[0].exit_gcv;
	var exit_ncv = document.forms[0].exit_ncv;
	var exit_qty_mmbtu = document.forms[0].exit_qty_mmbtu;
	var exit_qty_scm = document.forms[0].exit_qty_scm;
	
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
				
				if(trim(gta_no[i].value)=="")
				{
					msg+="GTA No is Missing for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(gtc_no[i].value)=="")
				{
					msg+="GTC No is Missing for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(entry_point_map[i].value)=="")
				{
					msg+="Entry Point Map is Missing for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(exit_point_map[i].value)=="")
				{
					msg+="Exit Point Map is Missing for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(cont_map[i].value)=="")
				{
					msg+="Cont Map is Missing for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(exit_gcv[i].value)=="")
				{
					msg+="Enter Exit GCV for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(exit_ncv[i].value)=="")
				{
					msg+="Enter Exit NCV for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(exit_qty_mmbtu[i].value)=="")
				{
					msg+="Enter Exit Energy(MMBTU) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(exit_qty_scm[i].value)=="")
				{
					msg+="Enter Exit Energy(SCM) for ROW - "+parseInt(i+1)+"!\n";
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
			
			if(trim(gta_no.value)=="")
			{
				msg+="GTA No is Missing!\n";
				flag=false;
			}
			if(trim(gtc_no.value)=="")
			{
				msg+="GTC No is Missing!\n";
				flag=false;
			}
			if(trim(entry_point_map.value)=="")
			{
				msg+="Entry Point Map is Missing!\n";
				flag=false;
			}
			if(trim(exit_point_map.value)=="")
			{
				msg+="Exit Point Map is Missing!\n";
				flag=false;
			}
			if(trim(cont_map.value)=="")
			{
				msg+="Cont Map is Missing!\n";
				flag=false;
			}
			if(trim(exit_gcv.value)=="")
			{
				msg+="Enter Exit GCV!\n";
				flag=false;
			}
			if(trim(exit_ncv.value)=="")
			{
				msg+="Enter Exit NCV!\n";
				flag=false;
			}
			if(trim(exit_qty_mmbtu.value)=="")
			{
				msg+="Enter Exit Energy(MMBTU)!\n";
				flag=false;
			}
			if(trim(exit_qty_scm.value)=="")
			{
				msg+="Enter Exit Energy(SCM)!\n";
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
		var a = confirm("Do you want to Submit Transporter Nomination?");
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
	var mdcq_qty = document.getElementById("mdcq_qty"+index);
	var int_map_id = document.getElementById("internal_map_id"+index); 
	
	var gas_dt = document.forms[0].gas_dt;
	
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	var tmp_qty_mmbtu = document.forms[0].tmp_qty_mmbtu;
	var internal_map_id = document.forms[0].internal_map_id;
	
	var tot_mmbtu =  parseFloat("0");
	var tot_tmp_mmbtu = parseFloat("0");
	
	if(qty_mmbtu!=null && qty_mmbtu!=undefined)
	{
		if(qty_mmbtu.length!=undefined)
		{
			for(var i=0; i<qty_mmbtu.length; i++)
			{
				if(int_map_id.value == internal_map_id[i].value)
				{
					if(trim(qty_mmbtu[i].value)!="")
					{
						tot_mmbtu = parseFloat(tot_mmbtu) + parseFloat(qty_mmbtu[i].value);
					}
					else
					{
						qty_mmbtu[i].value="0";
						qty_scm[i].value="0";
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
			if(int_map_id.value == internal_map_id.value)
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
		alert(""+gas_dt.value+"  Nomination "+parseFloat(tot_mmbtu)+" MMBTU > MDCQ Qty("+mdcq_qty.value+" MMBTU) \n\nDo you want to Proceed?");
	}
}

function totalQty()
{
	var sub_index = document.forms[0].sub_index;
	
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	
	var exit_qty_mmbtu = document.forms[0].exit_qty_mmbtu;
	var exit_qty_scm = document.forms[0].exit_qty_scm;
	
	var tot_qty_mmbtu = document.forms[0].tot_qty_mmbtu;
	var tot_qty_scm = document.forms[0].tot_qty_scm;
	
	var exit_tot_qty_mmbtu = document.forms[0].exit_tot_qty_mmbtu;
	var exit_tot_qty_scm = document.forms[0].exit_tot_qty_scm;
	
	var tot_mmbtu=parseFloat("0");
	var tot_scm=parseFloat("0");
	
	var exit_tot_mmbtu=parseFloat("0");
	var exit_tot_scm=parseFloat("0");
	
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
				exit_tot_mmbtu=parseFloat("0");
				exit_tot_scm=parseFloat("0");
				
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
						
						if(trim(exit_qty_mmbtu[i].value) != "")
						{
							exit_tot_mmbtu = exit_tot_mmbtu + parseFloat(exit_qty_mmbtu[i].value)
						}
						if(trim(exit_qty_scm[i].value) != "")
						{
							exit_tot_scm = exit_tot_scm + parseFloat(exit_qty_scm[i].value)
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
				
				exit_tot_qty_mmbtu[j].value=round(parseFloat(exit_tot_mmbtu),2)
				exit_tot_qty_scm[j].value =round(parseFloat(exit_tot_scm),2)
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
					
					if(trim(exit_qty_mmbtu[i].value) != "")
					{
						exit_tot_mmbtu = exit_tot_mmbtu + parseFloat(exit_qty_mmbtu[i].value)
					}
					if(trim(exit_qty_scm[i].value) != "")
					{
						exit_tot_scm = exit_tot_scm + parseFloat(exit_qty_scm[i].value)
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
				
				if(trim(exit_qty_mmbtu.value) != "")
				{
					exit_tot_mmbtu = exit_tot_mmbtu + parseFloat(exit_qty_mmbtu.value)
				}
				if(trim(exit_qty_scm.value) != "")
				{
					exit_tot_scm = exit_tot_scm + parseFloat(exit_qty_scm.value)
				}
			}
			
			tot_qty_mmbtu.value=round(parseFloat(tot_mmbtu),2)
			tot_qty_scm.value =round(parseFloat(tot_scm),2)
			
			exit_tot_qty_mmbtu.value=round(parseFloat(exit_tot_mmbtu),2)
			exit_tot_qty_scm.value =round(parseFloat(exit_tot_scm),2)
		}
	}
}

async function doPaste(index,index1)
{
	var pastedText = event.clipboardData.getData('text/plain');
	var arr=pastedText.split(/\n/);
	
	var sub_index = document.forms[0].sub_index;
	
	var chk = document.forms[0].chk;
	var exit_qty_mmbtu = document.forms[0].exit_qty_mmbtu;
	
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
					if(exit_qty_mmbtu.length!=undefined)
					{
						for(i=i; i<exit_qty_mmbtu.length; i++)
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
											exit_qty_mmbtu[i].value=arr[k];
											
											negNumber(exit_qty_mmbtu[i]);
											checkNumber1(exit_qty_mmbtu[i],9,2);
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
			if(exit_qty_mmbtu.length!=undefined)
			{
				window.setTimeout(function() 
				{
					for(i=i; i<exit_qty_mmbtu.length; i++)
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
										exit_qty_mmbtu[i].value=arr[k];
										
										negNumber(exit_qty_mmbtu[i]);
										checkNumber1(exit_qty_mmbtu[i],9,2);
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
								exit_qty_mmbtu.value=arr[k];
								
								negNumber(exit_qty_mmbtu);
								checkNumber1(exit_qty_mmbtu,9,2);
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
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DataBean_ContractMgmt" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String nextdate = utildate.getNextDate();

String gas_dt = request.getParameter("gas_dt")==null?nextdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "-1");

cont_mgmt.setCallFlag("DAILY_TRANSPORTER_NOM");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setGas_dt(gas_dt);
cont_mgmt.init();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
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
Vector VTAX_DTL = cont_mgmt.getVTAX_DTL();

Vector VEXIT_BASE = cont_mgmt.getVEXIT_BASE();
Vector VEXIT_GCV = cont_mgmt.getVEXIT_GCV();
Vector VEXIT_NCV = cont_mgmt.getVEXIT_NCV();
Vector VEXIT_QTY_MMBTU = cont_mgmt.getVEXIT_QTY_MMBTU();
Vector VEXIT_QTY_SCM = cont_mgmt.getVEXIT_QTY_SCM();

Vector VLINKED_GTC = cont_mgmt.getVLINKED_GTC();

Vector VCONT_NO = cont_mgmt.getVCONT_NO();
Vector VCONT_REV_NO = cont_mgmt.getVCONT_REV_NO();
Vector VAGMT_NO = cont_mgmt.getVAGMT_NO();
Vector VAGMT_REV_NO = cont_mgmt.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = cont_mgmt.getVCONTRACT_TYPE();
Vector VDIS_CONT_MAPPING = cont_mgmt.getVDIS_CONT_MAPPING();
Vector VTRANS_PLANT_WISE_TOTAL_MMBTU = cont_mgmt.getVTRANS_PLANT_WISE_TOTAL_MMBTU();
Vector VTRANS_PLANT_WISE_TOTAL_SCM = cont_mgmt.getVTRANS_PLANT_WISE_TOTAL_SCM();

Vector VTRANS_PLANT_WISE_EXIT_TOTAL_MMBTU = cont_mgmt.getVTRANS_PLANT_WISE_EXIT_TOTAL_MMBTU();
Vector VTRANS_PLANT_WISE_EXIT_TOTAL_SCM = cont_mgmt.getVTRANS_PLANT_WISE_EXIT_TOTAL_SCM();

Vector VINDEX = cont_mgmt.getVINDEX();
Vector VSUB_INDEX = cont_mgmt.getVSUB_INDEX();

Vector VIS_GTC = cont_mgmt.getVIS_GTC();

Vector VGTA_NO = cont_mgmt.getVGTA_NO();
Vector VGTC_NO = cont_mgmt.getVGTC_NO();

Vector VENTRY_POINT_MAP = cont_mgmt.getVENTRY_POINT_MAP();
Vector VEXIT_POINT_MAP = cont_mgmt.getVEXIT_POINT_MAP();
Vector VCONT_MAPPING = cont_mgmt.getVCONT_MAPPING();

//System.out.println(VINDEX+"==="+VSUB_INDEX+"=="+VNOM_REV_NO);
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
					    	Daily Transporter Nomination
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
					<%int j=0,k=0,l=0,m=0;
					for(int i=0; i<VTRANSPORTER_CD.size(); i++)
					{ 
						String trans_cd=""+VTRANSPORTER_CD.elementAt(i);
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
					%>
					&nbsp;
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
										<h2 class="accordion-header" id="heading<%=l%>">
    												<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
								    		<%=VTRANSPORTER_PLANT_ABBR.elementAt(j)%>
								      		</button>
								    	</h2>
								    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
								      		<div class="accordion-body accor-body">
								        		<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr>
																	<th rowspan="2"></th>
																	<th rowspan="2">Gen Time</th>
																	<th rowspan="2">Rev#</th>
																	<th rowspan="2">Customer</th>
																	<th rowspan="2">Customer Plant</th>
																	<th rowspan="2">Tax</th>
																	<th rowspan="2">Business Unit</th>
																	<th rowspan="2">Contract#<br>[Contract/Trade Ref#]</th>
																	<th rowspan="2">DCQ</th>
																	<th rowspan="2">Buyer Nomination (Rev)<br>(MMBTU)</th>
																	<th colspan="3">Transporter Entry Point</th>
																	<th rowspan="2">GTC</th>
																	<th colspan="3">Transporter Exit Point</th>
																</tr>
																<tr>
																	<th>Calorific Value Base<br>KCal/SCM</th>
																	<th>Energy (MMBTU)</th>
																	<th>Energy (SCM)</th>
																	<th>Calorific Value Base<br>KCal/SCM</th>
																	<th>Energy (MMBTU)</th>
																	<th>Energy (SCM)</th>
																</tr>
															</thead>
															<tbody>
																<%m=0;
																if(sub_index>0){ %>
																	<%for(l=l; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
																	{ 
																		m+=1;
																	%>
																		<tr>
																			<td align="center" valign="middle">
																				<input type="checkbox" class="form-check-input" name="chk" onclick="setEnableDisabled(this,'<%=l%>');calculateSCM('<%=l%>');totalQty();" 
																				<%if(VIS_GTC.elementAt(l).toString().equals("0")){ %>style="pointer-events: none;"<%} %>
																				<%if(VTRANSPORTER_STATUS.elementAt(l).equals("N") || VCOUNTERPARTY_STATUS.elementAt(l).equals("N")) {%>disabled style="pointer-events: none;"<%} %>>
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
																			<td align="center"><%=VNOM_REV_NO.elementAt(l)%></td>
																			<td align="center">
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
															      				<input type="hidden" name="trans_cd" id="trans_cd<%=l%>" value="<%=trans_cd%>" disabled>
															      				<input type="hidden" name="trans_plant_seq" id="trans_plant_seq<%=l%>" value="<%=trans_plant_seq%>" disabled>
															      				<input type="hidden" name="internal_map_id" id="internal_map_id<%=l%>" value="<%=VINTERNAL_MAP_ID.elementAt(l)%>">
																			</td>
																			<td align="center">
																				<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%>
																				<input type="hidden" name="plant_seq" id="plant_seq<%=l%>" value="<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(l)%>" disabled>
																			</td>
																			<td>
																				<%=VTAX_DTL.elementAt(l)%>
																			</td>
																			<td align="center">
																				<%=VBU_PLANT_ABBR.elementAt(l)%>
																				<input type="hidden" name="bu_plant_seq" id="bu_plant_seq<%=l%>" value="<%=VBU_PLANT_SEQ.elementAt(l)%>" disabled>
																			</td>
																			<td>
																				<%=VDIS_CONT_MAPPING.elementAt(l)%>
																				<%if(!VCONT_REF.elementAt(l).equals("")){%>
																					<br>(<%=VCONT_REF.elementAt(l)%>)
																				<%} %>
																			</td>
																			<td align="right">
																				<%=VDCQ.elementAt(l)%>
																				<input type="hidden" value="<%=VDCQ.elementAt(l)%>" name="dcq" id="dcq<%=l%>">
																				<input type="hidden" value="<%=VMDCQ_QTY.elementAt(l)%>" name="mdcq_qty" id="mdcq_qty<%=l%>">
																			</td>
																			<td align="right">
																				<%=VBUYER_NOM.elementAt(l)%> (<%=VBUYER_NOM_REV_NO.elementAt(l)%>)
																			</td>
																			<td align="center">
																				<div style="width:300px;">
																					<div class="row m-b-5">
																						<div class="col">
																							<input type="radio" name="rd<%=l%>" id="rd1<%=l%>" onclick="calculateSCM('<%=l%>');totalQty();" <%if(VBASE.elementAt(l).equals("GCV")){ %>checked<%} %> disabled>&nbsp;GCV
																	      				</div>
																	      				<div class="col">
																	      					<input type="text" class="form-control form-control-sm" name="gcv" id="gcv<%=l%>" value="<%=VGCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);calculateSCM('<%=l%>');" disabled>
																	      				</div>
																	      				<div class="col">
																	      					<input type="radio" name="rd<%=l%>" id="rd2<%=l%>" onclick="calculateSCM('<%=l%>');totalQty();" <%if(VBASE.elementAt(l).equals("NCV")){ %>checked<%} %> disabled>&nbsp;NCV 
																	      				</div>
																	      				<div class="col">
																		      				<input type="text" class="form-control form-control-sm" name="ncv" id="ncv<%=l%>" value="<%=VNCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);calculateSCM('<%=l%>');" disabled>
															      						</div>
															      						<input type="hidden" name="base" id="base<%=l%>" value="<%=VBASE.elementAt(l)%>" disabled>
															      					</div>
															      				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="qty_mmbtu" id="qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" 
																					style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" 
																					<%-- onblur="checkNumber1(this,9,2);checkQty('<%=l%>');calculateSCM('<%=l%>');totalQty();" disabled> --%>
																					onblur="checkNumber1(this,9,2);calculateSCM('<%=l%>');totalQty();" disabled>
																					<input type="hidden" class="form-control form-control-sm" name="tmp_qty_mmbtu" id="tmp_qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" style="text-align:right" disabled>
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="qty_scm" id="qty_scm<%=l%>" value="<%=VQTY_SCM.elementAt(l)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
																				</div>
																			</td>
																			<td>
																				<%=VLINKED_GTC.elementAt(l) %>
																				<input type="hidden" name="gta_no" id="gta_no<%=l%>" value="<%=VGTA_NO.elementAt(l)%>" disabled>
																				<input type="hidden" name="gtc_no" id="gtc_no<%=l%>" value="<%=VGTC_NO.elementAt(l)%>" disabled>
																				<input type="hidden" name="entry_point_map" id="entry_point_map<%=l%>" value="<%=VENTRY_POINT_MAP.elementAt(l)%>" disabled>
																				<input type="hidden" name="exit_point_map" id="exit_point_map<%=l%>" value="<%=VEXIT_POINT_MAP.elementAt(l)%>" disabled>
																				<input type="hidden" name="cont_map" id="cont_map<%=l%>" value="<%=VCONT_MAPPING.elementAt(l)%>" disabled>
																			</td>
																			<td align="center">
																				<div style="width:300px;">
																					<div class="row m-b-5">
																						<div class="col">
																							<input type="radio" name="exit_rd<%=l%>" id="exit_rd1<%=l%>" onclick="calculateSCM('<%=l%>');totalQty();" <%if(VEXIT_BASE.elementAt(l).equals("GCV")){ %>checked<%} %> style="pointer-events: none;" disabled>&nbsp;GCV
																	      				</div>
																	      				<div class="col">
																	      					<input type="text" class="form-control form-control-sm" name="exit_gcv" id="exit_gcv<%=l%>" value="<%=VEXIT_GCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);calculateSCM('<%=l%>');" disabled>
																	      				</div>
																	      				<div class="col">
																	      					<input type="radio" name="exit_rd<%=l%>" id="exit_rd2<%=l%>" onclick="calculateSCM('<%=l%>');totalQty();" <%if(VEXIT_BASE.elementAt(l).equals("NCV")){ %>checked<%} %> style="pointer-events: none;" disabled>&nbsp;NCV 
																	      				</div>
																	      				<div class="col">
																		      				<input type="text" class="form-control form-control-sm" name="exit_ncv" id="exit_ncv<%=l%>" value="<%=VEXIT_NCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);calculateSCM('<%=l%>');" disabled>
															      						</div>
															      						<input type="hidden" name="exit_base" id="exit_base<%=l%>" value="<%=VEXIT_BASE.elementAt(l)%>" disabled>
															      					</div>
															      				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="exit_qty_mmbtu" id="exit_qty_mmbtu<%=l%>" value="<%=VEXIT_QTY_MMBTU.elementAt(l)%>" 
																					style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" 
																					<%-- onblur="checkNumber1(this,9,2);checkQty('<%=l%>');calculateSCM('<%=l%>');totalQty();" disabled> --%>
																					onpaste="doPaste('<%=j%>','<%=l%>');" 
																					onblur="negNumber(this);checkNumber1(this,9,2);calculateSCM('<%=l%>');totalQty();" disabled>
																					<input type="hidden" class="form-control form-control-sm" name="exit_tmp_qty_mmbtu" id="exit_tmp_qty_mmbtu<%=l%>" value="<%=VEXIT_QTY_MMBTU.elementAt(l)%>" style="text-align:right" disabled>
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="exit_qty_scm" id="exit_qty_scm<%=l%>" value="<%=VEXIT_QTY_SCM.elementAt(l)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
																				</div>
																			</td>
																		</tr>
																		<%if(m==sub_index)
																		{%>
																			<tr>
																				<td colspan="11" align="right">
																					<b>Total(<%=VTRANSPORTER_PLANT_ABBR.elementAt(j)%>)</b>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_qty_mmbtu" value="<%=VTRANS_PLANT_WISE_TOTAL_MMBTU.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_qty_scm" value="<%=VTRANS_PLANT_WISE_TOTAL_SCM.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
																				</td>
																				<td colspan="2"></td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="exit_tot_qty_mmbtu" value="<%=VTRANS_PLANT_WISE_EXIT_TOTAL_MMBTU.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="exit_tot_qty_scm" value="<%=VTRANS_PLANT_WISE_EXIT_TOTAL_SCM.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
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
				<%if(VTRANSPORTER_CD.size() > 0){ %>
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

<input type="hidden" name="option" value="TRANSPORTER_NOM">

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