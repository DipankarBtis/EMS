<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "frm_buy_weekly_seller_nom.jsp?counterparty_cd="+counterparty_cd+
				"&u="+u+"&gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+
				"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no;
	
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
		newWindow = window.open("frm_nomination_contract_list.jsp?gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+"&nomination_type=S","Trader Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_nomination_contract_list.jsp?gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+"&nomination_type=S","Trader Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_type,cargo_no)
{
	var gas_dt = document.forms[0].gas_dt.value;
	var nomination_freq = document.forms[0].nomination_freq.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_buy_weekly_seller_nom.jsp?counterparty_cd="+countpty_cd+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&st_dt="+st_dt+"&end_dt="+end_dt+"&gas_dt="+gas_dt+
			"&nomination_freq="+nomination_freq+
			"&u="+u+"&contract_type="+cont_type+"&cargo_no="+cargo_no;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setEnableBuWise(obj,index)
{
	var bu_size = document.getElementById("index"+index);
	
	if(obj.checked)
	{
		for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
		{
			document.getElementById("chk_bu"+index+""+j).disabled=false;
			document.getElementById("bu_plant_seq"+index+""+j).disabled=false;
			document.getElementById("index1"+index+""+j).disabled=false;
		}
		
		document.getElementById("week_gas_dt"+index).disabled=false;
		document.getElementById("index"+index).disabled=false;
		document.getElementById("ivalue"+index).disabled=false;
	}
	else
	{
		for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
		{
			document.getElementById("chk_bu"+index+""+j).disabled=true;
			document.getElementById("chk_bu"+index+""+j).checked=false;
			document.getElementById("bu_plant_seq"+index+""+j).disabled=true;
			
			var plant_size = document.getElementById("index1"+index+""+j);
			
			for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
			{
				document.getElementById("chk_plant"+index+""+j+""+k).disabled=true;
				document.getElementById("chk_plant"+index+""+j+""+k).checked=false;
				
				var gen_dt = document.getElementById("gen_dt"+index+""+j+""+k);
				var gen_time = document.getElementById("gen_time"+index+""+j+""+k);
				var rd1 = document.getElementById("rd1"+index+""+j+""+k);
				var rd2 = document.getElementById("rd2"+index+""+j+""+k);
				var gcv = document.getElementById("gcv"+index+""+j+""+k);
				var ncv = document.getElementById("ncv"+index+""+j+""+k);
				var qty_mmbtu = document.getElementById("qty_mmbtu"+index+""+j+""+k);
				var qty_scm = document.getElementById("qty_scm"+index+""+j+""+k);
				
				var plant_seq = document.getElementById("plant_seq"+index+""+j+""+k);
				var transporter_plant_cd = document.getElementById("transporter_cd"+index+""+j+""+k);
				var transporter_plant_seq = document.getElementById("transporter_plant_seq"+index+""+j+""+k);
				
				var tmp_gen_dt = document.getElementById("tmp_gen_dt"+index+""+j+""+k);
				var tmp_gen_time = document.getElementById("tmp_gen_time"+index+""+j+""+k);
				
				gen_dt.disabled=true;
				gen_time.disabled=true;
				rd1.disabled=true;
				rd2.disabled=true;
				gcv.disabled=true;
				ncv.disabled=true;
				qty_mmbtu.disabled=true;
				qty_scm.disabled=true;
				
				plant_seq.disabled=true;
				transporter_plant_cd.disabled=true;
				transporter_plant_seq.disabled=true;
			}
		}
		document.getElementById("week_gas_dt"+index).disabled=true;
		document.getElementById("index"+index).disabled=true;
		document.getElementById("ivalue"+index).disabled=true;
	} 
}

function setEnablePlantWise(obj,index,index1)
{
	var plant_size = document.getElementById("index1"+index+""+index1);
	
	if(obj.checked)
	{
		for(var j=1 ; j <= parseInt(plant_size.value) ; j++)
		{
			var nom_block=document.getElementById("nom_block"+index+""+index1+""+j).value;
			
			if(nom_block!="Y")
			{
				document.getElementById("chk_plant"+index+""+index1+""+j).disabled=false;
			}
		}
		document.getElementById("index1"+index+""+index1).disabled=false;
	}
	else
	{
		for(var j=1 ; j <= parseInt(plant_size.value) ; j++)
		{
			document.getElementById("chk_plant"+index+""+index1+""+j).disabled=true;
			document.getElementById("chk_plant"+index+""+index1+""+j).checked=false;
			
			var gen_dt = document.getElementById("gen_dt"+index+""+index1+""+j);
			var gen_time = document.getElementById("gen_time"+index+""+index1+""+j);
			var rd1 = document.getElementById("rd1"+index+""+index1+""+j);
			var rd2 = document.getElementById("rd2"+index+""+index1+""+j);
			var gcv = document.getElementById("gcv"+index+""+index1+""+j);
			var ncv = document.getElementById("ncv"+index+""+index1+""+j);
			var qty_mmbtu = document.getElementById("qty_mmbtu"+index+""+index1+""+j);
			var qty_scm = document.getElementById("qty_scm"+index+""+index1+""+j);
			
			var plant_seq = document.getElementById("plant_seq"+index+""+index1+""+j);
			var transporter_plant_cd = document.getElementById("transporter_cd"+index+""+index1+""+j);
			var transporter_plant_seq = document.getElementById("transporter_plant_seq"+index+""+index1+""+j);
			
			var tmp_gen_dt = document.getElementById("tmp_gen_dt"+index+""+index1+""+j);
			var tmp_gen_time = document.getElementById("tmp_gen_time"+index+""+index1+""+j);
			
			gen_dt.disabled=true;
			gen_time.disabled=true;
			rd1.disabled=true;
			rd2.disabled=true;
			gcv.disabled=true;
			ncv.disabled=true;
			qty_mmbtu.disabled=true;
			qty_scm.disabled=true;
			
			plant_seq.disabled=true;
			transporter_plant_cd.disabled=true;
			transporter_plant_seq.disabled=true;
		}
		document.getElementById("index1"+index+""+index1).disabled=true;
	}
}
function setEnableDisabled(obj,index, index1, index2)
{
	var gen_dt = document.getElementById("gen_dt"+index+""+index1+""+index2);
	var gen_time = document.getElementById("gen_time"+index+""+index1+""+index2);
	var rd1 = document.getElementById("rd1"+index+""+index1+""+index2);
	var rd2 = document.getElementById("rd2"+index+""+index1+""+index2);
	var gcv = document.getElementById("gcv"+index+""+index1+""+index2);
	var ncv = document.getElementById("ncv"+index+""+index1+""+index2);
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index+""+index1+""+index2);
	var qty_scm = document.getElementById("qty_scm"+index+""+index1+""+index2);
	
	var plant_seq = document.getElementById("plant_seq"+index+""+index1+""+index2);
	var transporter_plant_cd = document.getElementById("transporter_cd"+index+""+index1+""+index2);
	var transporter_plant_seq = document.getElementById("transporter_plant_seq"+index+""+index1+""+index2);
	
	var tmp_gen_dt = document.getElementById("tmp_gen_dt"+index+""+index1+""+index2);
	var tmp_gen_time = document.getElementById("tmp_gen_time"+index+""+index1+""+index2);
	
	if(obj.checked)
	{
		gen_dt.disabled=false;
		gen_time.disabled=false;
		rd1.disabled=false;
		rd2.disabled=false;
		gcv.disabled=false;
		ncv.disabled=false;
		qty_mmbtu.disabled=false;
		qty_scm.disabled=false;
		
		plant_seq.disabled=false;
		transporter_plant_cd.disabled=false;
		transporter_plant_seq.disabled=false;
		
		//gen_dt.value=tmp_gen_dt.value;
		//gen_time.value=tmp_gen_time.value;
		
		//gen_dt.style.background="white";
		//gen_time.style.background="white";
	}
	else
	{
		gen_dt.disabled=true;
		gen_time.disabled=true;
		rd1.disabled=true;
		rd2.disabled=true;
		gcv.disabled=true;
		ncv.disabled=true;
		qty_mmbtu.disabled=true;
		qty_scm.disabled=true;
		
		plant_seq.disabled=true;
		transporter_plant_cd.disabled=true;
		transporter_plant_seq.disabled=true;
	}
}

enableButton = true;
function doSubmit()
{
	var chk = document.forms[0].chk;
	var gas_dt = document.forms[0].gas_dt;
	
	var msg="";
	var flag=true;
	var chk_count=parseInt("0");
	editAllowedOnCpStatus = true;
	
	if(chk!=null && chk.length!=undefined)
	{
		for(var i=0; i<chk.length; i++)
		{
			if(chk[i].checked)
			{
				chk_count++;
				
				var chk_bu_count=parseInt("0");
				var bu_size = document.getElementById("index"+i);
				for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
				{
					var chk_bu=document.getElementById("chk_bu"+i+""+j)
					if(chk_bu.checked)
					{
						chk_bu_count++;
						var chk_plant_count=parseInt("0");
						var plant_size = document.getElementById("index1"+i+""+j);
						for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
						{
							var chk_plant = document.getElementById("chk_plant"+i+""+j+""+k)
							if(chk_plant.checked)
							{
								chk_plant_count++;
								
								var week_gas_dt = document.getElementById("week_gas_dt"+i+""+j+""+k);
								var gen_dt = document.getElementById("gen_dt"+i+""+j+""+k);
								var gen_time = document.getElementById("gen_time"+i+""+j+""+k);
								var gcv = document.getElementById("gcv"+i+""+j+""+k);
								var ncv = document.getElementById("ncv"+i+""+j+""+k);
								var qty_mmbtu = document.getElementById("qty_mmbtu"+i+""+j+""+k);
								var qty_scm = document.getElementById("qty_scm"+i+""+j+""+k);	
								
								if(trim(gen_dt.value)=="")
								{
									msg+="Enter Gen Date for ROW - "+parseInt(i+1)+"!\n";
									flag=false;
								}
								if(trim(gen_time.value)=="")
								{
									msg+="Enter Gen Time for ROW - "+parseInt(i+1)+"!\n";
									flag=false;
								}
								if(trim(gcv.value)=="")
								{
									msg+="Enter GCV for ROW - "+parseInt(i+1)+"!\n";
									flag=false;
								}
								if(trim(ncv.value)=="")
								{
									msg+="Enter NCV for ROW - "+parseInt(i+1)+"!\n";
									flag=false;
								}
								if(trim(qty_mmbtu.value)=="")
								{
									msg+="Enter Energy(MMBTU) for ROW - "+parseInt(i+1)+"!\n";
									flag=false;
								}
								if(trim(qty_scm.value)=="")
								{
									msg+="Enter Energy(SCM) for ROW - "+parseInt(i+1)+"!\n";
									flag=false;
								}
							}
						}
						if(parseInt(chk_plant_count) == 0)
						{
							msg+="Please Select Atleast One Plant ROW - "+parseInt(i+1)+"!\n";
							flag=false;
						}
					}
				}
				if(parseInt(chk_bu_count) == 0)
				{
					msg+="Please Select Atleast One BUs ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
			}
		}
	}
	else
	{
		var i=0;
		if(chk.checked)
		{
			chk_count++;
			
			var chk_bu_count=parseInt("0");
			var bu_size = document.getElementById("index"+i);
			for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
			{
				var chk_bu=document.getElementById("chk_bu"+i+""+j)
				if(chk_bu.checked)
				{
					chk_bu_count++;
					var chk_plant_count=parseInt("0");
					var plant_size = document.getElementById("index1"+i+""+j);
					for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
					{
						var chk_plant = document.getElementById("chk_plant"+i+""+j+""+k)
						if(chk_plant.checked)
						{
							chk_plant_count++;
							
							var week_gas_dt = document.getElementById("week_gas_dt"+i+""+j+""+k);
							var gen_dt = document.getElementById("gen_dt"+i+""+j+""+k);
							var gen_time = document.getElementById("gen_time"+i+""+j+""+k);
							var gcv = document.getElementById("gcv"+i+""+j+""+k);
							var ncv = document.getElementById("ncv"+i+""+j+""+k);
							var qty_mmbtu = document.getElementById("qty_mmbtu"+i+""+j+""+k);
							var qty_scm = document.getElementById("qty_scm"+i+""+j+""+k);	
							
							if(trim(gen_dt.value)=="")
							{
								msg+="Enter Gen Date for ROW - "+parseInt(i+1)+"!\n";
								flag=false;
							}
							if(trim(gen_time.value)=="")
							{
								msg+="Enter Gen Time for ROW - "+parseInt(i+1)+"!\n";
								flag=false;
							}
							if(trim(gcv.value)=="")
							{
								msg+="Enter GCV for ROW - "+parseInt(i+1)+"!\n";
								flag=false;
							}
							if(trim(ncv.value)=="")
							{
								msg+="Enter NCV for ROW - "+parseInt(i+1)+"!\n";
								flag=false;
							}
							if(trim(qty_mmbtu.value)=="")
							{
								msg+="Enter Energy(MMBTU) for ROW - "+parseInt(i+1)+"!\n";
								flag=false;
							}
							if(trim(qty_scm.value)=="")
							{
								msg+="Enter Energy(SCM) for ROW - "+parseInt(i+1)+"!\n";
								flag=false;
							}
						}
					}
					if(parseInt(chk_plant_count) == 0)
					{
						msg+="Please Select Atleast One Plant ROW - "+parseInt(i+1)+"!\n";
						flag=false;
					}
				}
			}
			if(parseInt(chk_bu_count) == 0)
			{
				msg+="Please Select Atleast One BUs ROW - "+parseInt(i+1)+"!\n";
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
		var a = confirm("Do you want to Submit Seller Nomination?");
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

function calculateSCM(index)
{
	var bu_size = document.getElementById("index"+index);
	
	for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
	{
		var plant_size = document.getElementById("index1"+index+""+j);
		for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
		{
			var rd1 = document.getElementById("rd1"+index+""+j+""+k);
			var rd2 = document.getElementById("rd2"+index+""+j+""+k);
			var base = document.getElementById("base"+index+""+j+""+k);
			var gcv = document.getElementById("gcv"+index+""+j+""+k);
			var ncv = document.getElementById("ncv"+index+""+j+""+k);
			var qty_mmbtu = document.getElementById("qty_mmbtu"+index+""+j+""+k);
			var qty_scm = document.getElementById("qty_scm"+index+""+j+""+k);
			
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
				deviding_factor = parseFloat("1");
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
	}
}

function checkQty(i,j,k)
{
	var qty_mmbtu = document.getElementById("qty_mmbtu"+i+""+j+""+k);
	var buyer_nom_qty = document.getElementById("buyer_nom_qty"+i+""+j+""+k);
	
	if(parseFloat(qty_mmbtu.value) > parseFloat(buyer_nom_qty.value))
	{
		alert("Seller Nomination "+parseFloat(qty_mmbtu.value)+" MMBTU can not exceed Buyer Nomination "+parseFloat(buyer_nom_qty.value)+" MMBTU");
		qty_mmbtu.value = buyer_nom_qty.value;
	}
}

function checkAll(obj)
{
	var chk = document.forms[0].chk;
	var cp_status = document.forms[0].cp_status;
	
	if(chk!=null && chk.length!=undefined)
	{
		for(var i=0; i<chk.length; i++)
		{
			var bu_size = document.getElementById("index"+i);
			
			if(obj.checked)
			{
				if(cp_status[i].value != "N")
				{
					chk[i].checked=true;
				}
			}
			else
			{
				chk[i].checked=false;
			}
			
			if(chk[i].checked)
			{
				for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
				{
					document.getElementById("chk_bu"+i+""+j).disabled=false;
					document.getElementById("bu_plant_seq"+i+""+j).disabled=false;
				}
				
				document.getElementById("week_gas_dt"+i).disabled=false;
				document.getElementById("index"+i).disabled=false;
				document.getElementById("ivalue"+i).disabled=false;
			}
			else
			{
				for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
				{
					document.getElementById("chk_bu"+i+""+j).disabled=true;
					document.getElementById("chk_bu"+i+""+j).checked=false;
					document.getElementById("bu_plant_seq"+i+""+j).disabled=true;
					
					var plant_size = document.getElementById("index1"+i+""+j);
					
					for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
					{
						document.getElementById("chk_plant"+i+""+j+""+k).disabled=true;
						document.getElementById("chk_plant"+i+""+j+""+k).checked=false;
						
						var gen_dt = document.getElementById("gen_dt"+i+""+j+""+k);
						var gen_time = document.getElementById("gen_time"+i+""+j+""+k);
						var rd1 = document.getElementById("rd1"+i+""+j+""+k);
						var rd2 = document.getElementById("rd2"+i+""+j+""+k);
						var gcv = document.getElementById("gcv"+i+""+j+""+k);
						var ncv = document.getElementById("ncv"+i+""+j+""+k);
						var qty_mmbtu = document.getElementById("qty_mmbtu"+i+""+j+""+k);
						var qty_scm = document.getElementById("qty_scm"+i+""+j+""+k);
						
						var plant_seq = document.getElementById("plant_seq"+i+""+j+""+k);
						var transporter_plant_cd = document.getElementById("transporter_cd"+i+""+j+""+k);
						var transporter_plant_seq = document.getElementById("transporter_plant_seq"+i+""+j+""+k);
						
						var tmp_gen_dt = document.getElementById("tmp_gen_dt"+i+""+j+""+k);
						var tmp_gen_time = document.getElementById("tmp_gen_time"+i+""+j+""+k);
						
						gen_dt.disabled=true;
						gen_time.disabled=true;
						rd1.disabled=true;
						rd2.disabled=true;
						gcv.disabled=true;
						ncv.disabled=true;
						qty_mmbtu.disabled=true;
						qty_scm.disabled=true;
						
						plant_seq.disabled=true;
						transporter_plant_cd.disabled=true;
						transporter_plant_seq.disabled=true;
					}
				}
				document.getElementById("week_gas_dt"+i).disabled=true;
				document.getElementById("index"+i).disabled=true;
				document.getElementById("ivalue"+i).disabled=true;
			}
		}
	}
	else
	{
		var i=0;
		var bu_size = document.getElementById("index"+i);
		
		if(obj.checked)
		{
			if(cp_status.value != "N")
			{
				chk[i].checked=true;
			}
		}
		else
		{
			chk.checked=false;
		}
		
		if(chk.checked)
		{
			for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
			{
				document.getElementById("chk_bu"+i+""+j).disabled=false;
				document.getElementById("bu_plant_seq"+i+""+j).disabled=false;
			}
			
			document.getElementById("week_gas_dt"+i).disabled=false;
			document.getElementById("index"+i).disabled=false;
			document.getElementById("ivalue"+i).disabled=false;
		}
		else
		{
			for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
			{
				document.getElementById("chk_bu"+i+""+j).disabled=true;
				document.getElementById("chk_bu"+i+""+j).checked=false;
				document.getElementById("bu_plant_seq"+i+""+j).disabled=true;
				
				
				var plant_size = document.getElementById("index1"+i+""+j);
				
				for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
				{
					document.getElementById("chk_plant"+i+""+j+""+k).disabled=true;
					document.getElementById("chk_plant"+i+""+j+""+k).checked=false;
					
					var gen_dt = document.getElementById("gen_dt"+i+""+j+""+k);
					var gen_time = document.getElementById("gen_time"+i+""+j+""+k);
					var rd1 = document.getElementById("rd1"+i+""+j+""+k);
					var rd2 = document.getElementById("rd2"+i+""+j+""+k);
					var gcv = document.getElementById("gcv"+i+""+j+""+k);
					var ncv = document.getElementById("ncv"+i+""+j+""+k);
					var qty_mmbtu = document.getElementById("qty_mmbtu"+i+""+j+""+k);
					var qty_scm = document.getElementById("qty_scm"+i+""+j+""+k);
					
					var plant_seq = document.getElementById("plant_seq"+i+""+j+""+k);
					var transporter_plant_cd = document.getElementById("transporter_cd"+i+""+j+""+k);
					var transporter_plant_seq = document.getElementById("transporter_plant_seq"+i+""+j+""+k);
					
					var tmp_gen_dt = document.getElementById("tmp_gen_dt"+i+""+j+""+k);
					var tmp_gen_time = document.getElementById("tmp_gen_time"+i+""+j+""+k);
					
					gen_dt.disabled=true;
					gen_time.disabled=true;
					rd1.disabled=true;
					rd2.disabled=true;
					gcv.disabled=true;
					ncv.disabled=true;
					qty_mmbtu.disabled=true;
					qty_scm.disabled=true;
					
					plant_seq.disabled=true;
					transporter_plant_cd.disabled=true;
					transporter_plant_seq.disabled=true;
				}
			}
			document.getElementById("week_gas_dt"+i).disabled=true;
			document.getElementById("index"+i).disabled=true;
			document.getElementById("ivalue"+i).disabled=true;
		}
	}
}

function checkBuAll(obj)
{
	var chk = document.forms[0].chk;
	
	var count=parseInt("0");
	
	if(chk!=null && chk.length!=undefined)
	{
		for(var i=0; i<chk.length; i++)
		{
			var bu_size = document.getElementById("index"+i);
			if(chk[i].checked)
			{
				count++;
				for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
				{
					if(obj.checked)
					{
						document.getElementById("chk_bu"+i+""+j).checked=true;
					}
					else
					{
						document.getElementById("chk_bu"+i+""+j).checked=false;
					}
					
					if(document.getElementById("chk_bu"+i+""+j).checked)
					{
						document.getElementById("chk_bu"+i+""+j).disabled=false;
						document.getElementById("bu_plant_seq"+i+""+j).disabled=false;
						document.getElementById("index1"+i+""+j).disabled=false;
						
						var plant_size = document.getElementById("index1"+i+""+j);
						
						for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
						{
							var nom_block=document.getElementById("nom_block"+i+""+j+""+k).value;
							
							if(nom_block!="Y")
							{
								document.getElementById("chk_plant"+i+""+j+""+k).disabled=false;
							}
							document.getElementById("chk_plant"+i+""+j+""+k).checked=false;
						}
					}
					else
					{
						document.getElementById("chk_bu"+i+""+j).disabled=true;
						document.getElementById("bu_plant_seq"+i+""+j).disabled=true;
						document.getElementById("index1"+i+""+j).disabled=true;
						
						var plant_size = document.getElementById("index1"+i+""+j);
						
						for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
						{
							document.getElementById("chk_plant"+i+""+j+""+k).disabled=true;
							document.getElementById("chk_plant"+i+""+j+""+k).checked=false;
							
							var gen_dt = document.getElementById("gen_dt"+i+""+j+""+k);
							var gen_time = document.getElementById("gen_time"+i+""+j+""+k);
							var rd1 = document.getElementById("rd1"+i+""+j+""+k);
							var rd2 = document.getElementById("rd2"+i+""+j+""+k);
							var gcv = document.getElementById("gcv"+i+""+j+""+k);
							var ncv = document.getElementById("ncv"+i+""+j+""+k);
							var qty_mmbtu = document.getElementById("qty_mmbtu"+i+""+j+""+k);
							var qty_scm = document.getElementById("qty_scm"+i+""+j+""+k);
							
							var plant_seq = document.getElementById("plant_seq"+i+""+j+""+k);
							var transporter_plant_cd = document.getElementById("transporter_cd"+i+""+j+""+k);
							var transporter_plant_seq = document.getElementById("transporter_plant_seq"+i+""+j+""+k);
							
							var tmp_gen_dt = document.getElementById("tmp_gen_dt"+i+""+j+""+k);
							var tmp_gen_time = document.getElementById("tmp_gen_time"+i+""+j+""+k);
							
							gen_dt.disabled=true;
							gen_time.disabled=true;
							rd1.disabled=true;
							rd2.disabled=true;
							gcv.disabled=true;
							ncv.disabled=true;
							qty_mmbtu.disabled=true;
							qty_scm.disabled=true;
							
							plant_seq.disabled=true;
							transporter_plant_cd.disabled=true;
							transporter_plant_seq.disabled=true;
						}
					}
				}
			}
		}
	}
	else
	{
		var i=0;
		var bu_size = document.getElementById("index"+i);
		if(chk.checked)
		{
			count++;
			for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
			{
				if(obj.checked)
				{
					document.getElementById("chk_bu"+i+""+j).checked=true;
				}
				else
				{
					document.getElementById("chk_bu"+i+""+j).checked=false;
				}
				
				if(document.getElementById("chk_bu"+i+""+j).checked)
				{
					document.getElementById("chk_bu"+i+""+j).disabled=false;
					document.getElementById("bu_plant_seq"+i+""+j).disabled=false;
					document.getElementById("index1"+i+""+j).disabled=false;
					
					var plant_size = document.getElementById("index1"+i+""+j);
					
					for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
					{
						var nom_block=document.getElementById("nom_block"+i+""+j+""+k).value;
						
						if(nom_block!="Y")
						{
							document.getElementById("chk_plant"+i+""+j+""+k).disabled=false;
						}
						document.getElementById("chk_plant"+i+""+j+""+k).checked=false;
					}
				}
				else
				{
					document.getElementById("chk_bu"+i+""+j).disabled=true;
					document.getElementById("bu_plant_seq"+i+""+j).disabled=true;
					document.getElementById("index1"+i+""+j).disabled=true;
					
					var plant_size = document.getElementById("index1"+i+""+j);
					
					for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
					{
						document.getElementById("chk_plant"+i+""+j+""+k).disabled=true;
						document.getElementById("chk_plant"+i+""+j+""+k).checked=false;
						
						var gen_dt = document.getElementById("gen_dt"+i+""+j+""+k);
						var gen_time = document.getElementById("gen_time"+i+""+j+""+k);
						var rd1 = document.getElementById("rd1"+i+""+j+""+k);
						var rd2 = document.getElementById("rd2"+i+""+j+""+k);
						var gcv = document.getElementById("gcv"+i+""+j+""+k);
						var ncv = document.getElementById("ncv"+i+""+j+""+k);
						var qty_mmbtu = document.getElementById("qty_mmbtu"+i+""+j+""+k);
						var qty_scm = document.getElementById("qty_scm"+i+""+j+""+k);
						
						var plant_seq = document.getElementById("plant_seq"+i+""+j+""+k);
						var transporter_plant_cd = document.getElementById("transporter_cd"+i+""+j+""+k);
						var transporter_plant_seq = document.getElementById("transporter_plant_seq"+i+""+j+""+k);
						
						var tmp_gen_dt = document.getElementById("tmp_gen_dt"+i+""+j+""+k);
						var tmp_gen_time = document.getElementById("tmp_gen_time"+i+""+j+""+k);
						
						gen_dt.disabled=true;
						gen_time.disabled=true;
						rd1.disabled=true;
						rd2.disabled=true;
						gcv.disabled=true;
						ncv.disabled=true;
						qty_mmbtu.disabled=true;
						qty_scm.disabled=true;
						
						plant_seq.disabled=true;
						transporter_plant_cd.disabled=true;
						transporter_plant_seq.disabled=true;
					}
				}
			}
		}
	}
	
	if(parseInt(count) <= 0)
	{
		alert("Please select atleast one ROW!");
		obj.checked=false;
	}
}

function checkPlantAll(obj)
{
	var chk = document.forms[0].chk;
	
	var count=parseInt("0");
	
	if(chk!=null && chk.length!=undefined)
	{
		for(var i=0; i<chk.length; i++)
		{
			var bu_size = document.getElementById("index"+i);
			if(chk[i].checked)
			{
				for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
				{
					if(document.getElementById("chk_bu"+i+""+j).checked)
					{
						count++;
						var plant_size = document.getElementById("index1"+i+""+j);
						for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
						{
							var nom_block = document.getElementById("nom_block"+i+""+j+""+k).value;
							if(obj.checked)
							{
								if(nom_block!="Y")
								{
									document.getElementById("chk_plant"+i+""+j+""+k).checked=true;
								}
							}
							else
							{
								document.getElementById("chk_plant"+i+""+j+""+k).checked=false;
							}
							
							var gen_dt = document.getElementById("gen_dt"+i+""+j+""+k);
							var gen_time = document.getElementById("gen_time"+i+""+j+""+k);
							var rd1 = document.getElementById("rd1"+i+""+j+""+k);
							var rd2 = document.getElementById("rd2"+i+""+j+""+k);
							var gcv = document.getElementById("gcv"+i+""+j+""+k);
							var ncv = document.getElementById("ncv"+i+""+j+""+k);
							var qty_mmbtu = document.getElementById("qty_mmbtu"+i+""+j+""+k);
							var qty_scm = document.getElementById("qty_scm"+i+""+j+""+k);
							
							var plant_seq = document.getElementById("plant_seq"+i+""+j+""+k);
							var transporter_plant_cd = document.getElementById("transporter_cd"+i+""+j+""+k);
							var transporter_plant_seq = document.getElementById("transporter_plant_seq"+i+""+j+""+k);
							
							var tmp_gen_dt = document.getElementById("tmp_gen_dt"+i+""+j+""+k);
							var tmp_gen_time = document.getElementById("tmp_gen_time"+i+""+j+""+k);
							
							if(document.getElementById("chk_plant"+i+""+j+""+k).checked)
							{
								gen_dt.disabled=false;
								gen_time.disabled=false;
								rd1.disabled=false;
								rd2.disabled=false;
								gcv.disabled=false;
								ncv.disabled=false;
								qty_mmbtu.disabled=false;
								qty_scm.disabled=false;
								
								plant_seq.disabled=false;
								transporter_plant_cd.disabled=false;
								transporter_plant_seq.disabled=false;
							}
							else
							{	
								gen_dt.disabled=true;
								gen_time.disabled=true;
								rd1.disabled=true;
								rd2.disabled=true;
								gcv.disabled=true;
								ncv.disabled=true;
								qty_mmbtu.disabled=true;
								qty_scm.disabled=true;
								
								plant_seq.disabled=true;
								transporter_plant_cd.disabled=true;
								transporter_plant_seq.disabled=true;
							}
						}
					}
				}
			}
		}
	}
	else
	{
		var i=0;
		var bu_size = document.getElementById("index"+i);
		if(chk.checked)
		{
			for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
			{
				if(document.getElementById("chk_bu"+i+""+j).checked)
				{
					count++;
					var plant_size = document.getElementById("index1"+i+""+j);
					for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
					{
						var nom_block = document.getElementById("nom_block"+i+""+j+""+k).value;
						if(obj.checked)
						{
							if(nom_block!="Y")
							{
								document.getElementById("chk_plant"+i+""+j+""+k).checked=true;
							}
						}
						else
						{
							document.getElementById("chk_plant"+i+""+j+""+k).checked=false;
						}
						
						var gen_dt = document.getElementById("gen_dt"+i+""+j+""+k);
						var gen_time = document.getElementById("gen_time"+i+""+j+""+k);
						var rd1 = document.getElementById("rd1"+i+""+j+""+k);
						var rd2 = document.getElementById("rd2"+i+""+j+""+k);
						var gcv = document.getElementById("gcv"+i+""+j+""+k);
						var ncv = document.getElementById("ncv"+i+""+j+""+k);
						var qty_mmbtu = document.getElementById("qty_mmbtu"+i+""+j+""+k);
						var qty_scm = document.getElementById("qty_scm"+i+""+j+""+k);
						
						var plant_seq = document.getElementById("plant_seq"+i+""+j+""+k);
						var transporter_plant_cd = document.getElementById("transporter_cd"+i+""+j+""+k);
						var transporter_plant_seq = document.getElementById("transporter_plant_seq"+i+""+j+""+k);
						
						var tmp_gen_dt = document.getElementById("tmp_gen_dt"+i+""+j+""+k);
						var tmp_gen_time = document.getElementById("tmp_gen_time"+i+""+j+""+k);
						
						if(document.getElementById("chk_plant"+i+""+j+""+k).checked)
						{
							gen_dt.disabled=false;
							gen_time.disabled=false;
							rd1.disabled=false;
							rd2.disabled=false;
							gcv.disabled=false;
							ncv.disabled=false;
							qty_mmbtu.disabled=false;
							qty_scm.disabled=false;
							
							plant_seq.disabled=false;
							transporter_plant_cd.disabled=false;
							transporter_plant_seq.disabled=false;
						}
						else
						{	
							gen_dt.disabled=true;
							gen_time.disabled=true;
							rd1.disabled=true;
							rd2.disabled=true;
							gcv.disabled=true;
							ncv.disabled=true;
							qty_mmbtu.disabled=true;
							qty_scm.disabled=true;
							
							plant_seq.disabled=true;
							transporter_plant_cd.disabled=true;
							transporter_plant_seq.disabled=true;
						}
					}
				}
			}
		}
	}
	
	if(parseInt(count) <= 0)
	{
		alert("Please select atleast one BUs!");
		obj.checked=false;
	}
}

function doPaste(index,index1,index2)
{
	var pastedText = event.clipboardData.getData('text/plain');
	var arr=pastedText.split(/\n/);
	
	var m=parseInt("0");
	var pasteLen=arr.length;
	
	var indexSize = index+""+index1+""+index2;
	
	var chk = document.forms[0].chk;
	
	if(chk!=null && chk.length!=undefined)
	{
		window.setTimeout(function() 
		{
			for(var i=0; i<chk.length; i++)
			{
				var bu_size = document.getElementById("index"+i);
				if(chk[i].checked)
				{
					for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
					{
						if(document.getElementById("chk_bu"+i+""+j).checked)
						{
							var plant_size = document.getElementById("index1"+i+""+j);
							for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
							{
								var indexSize1 = i+""+j+""+k;
								
								if(parseInt(indexSize1) >= parseInt(indexSize))
								{
									var chk_plant=document.getElementById("chk_plant"+i+""+j+""+k);
									var qty_mmbtu = document.getElementById("qty_mmbtu"+i+""+j+""+k);
									
									if(parseInt(m) < parseInt(pasteLen))
									{
										if(chk_plant.checked)
										{
											qty_mmbtu.value=arr[m];
											
											negNumber(qty_mmbtu);
											checkNumber1(qty_mmbtu,9,2);
											checkQty(i,j,k);
											calculateSCM(i);
										}
										m++;
									}
								}
							}
						}
					}
				}
			}
		}, 50);
	}
	else
	{
		var i=0;
		var bu_size = document.getElementById("index"+i);
		window.setTimeout(function() 
		{
			if(chk.checked)
			{
				for(var j=1 ; j <= parseInt(bu_size.value) ; j++)
				{
					if(document.getElementById("chk_bu"+i+""+j).checked)
					{
						var plant_size = document.getElementById("index1"+i+""+j);
						for(var k=1 ; k <= parseInt(plant_size.value) ; k++)
						{
							var indexSize1 = i+""+j+""+k;
							
							if(parseInt(indexSize1) >= parseInt(indexSize))
							{
								var chk_plant=document.getElementById("chk_plant"+i+""+j+""+k);
								var qty_mmbtu = document.getElementById("qty_mmbtu"+i+""+j+""+k);
								
								if(parseInt(m) < parseInt(pasteLen))
								{
									if(chk_plant.checked)
									{
										qty_mmbtu.value=arr[m];
										
										negNumber(qty_mmbtu);
										checkNumber1(qty_mmbtu,9,2);
										checkQty(i,j,k);
										calculateSCM(i);
									}
									m++;
								}
							}
						}
					}
				}
			}
		}, 50);
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_Buy_Nom_Alloc_Mgmt" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");

String st_dt = request.getParameter("st_dt")==null?"":request.getParameter("st_dt");
String en_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");

String gas_dt = request.getParameter("gas_dt")==null?sysdate:request.getParameter("gas_dt");

String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");

String nomination_freq=request.getParameter("nomination_freq")==null?"W":request.getParameter("nomination_freq");
String cargo_no=request.getParameter("cargo_no")==""?"0":request.getParameter("cargo_no");

purchase.setCallFlag("WEEKLY_SELLER_NOM");
//purchase.setClearance(clearance);
purchase.setCounterparty_cd(counterparty_cd);
purchase.setAgmt_no(agmt_no);
purchase.setAgmt_rev_no(agmt_rev_no);
purchase.setCont_no(cont_no);
purchase.setCont_rev_no(cont_rev_no);
purchase.setContract_type(contract_type);
purchase.setComp_cd(owner_cd);
purchase.setGas_dt(gas_dt);
purchase.setNomination_freq(nomination_freq);
purchase.setCargo_no(cargo_no);
//purchase.setTransporter_cd(trans_cd);
//purchase.setTrans_plant_seq(trans_plant_seq);
//purchase.setPlant_seq(sel_trad_plant);
//purchase.setBu_plant_seq(sel_bu_plant);
purchase.init();

Vector VCOUNTERPARTY_CD = purchase.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = purchase.getVCOUNTERPARTY_NM();
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
Vector VGAS_DATE = purchase.getVGAS_DATE();
Vector VDCQ = purchase.getVDCQ();
Vector VMDCQ = purchase.getVMDCQ();
Vector VBUYER_NOM_REV_NO = purchase.getVBUYER_NOM_REV_NO();
Vector VBUYER_NOM = purchase.getVBUYER_NOM();

Vector VINDEX = purchase.getVINDEX();
Vector VINDEX1 = purchase.getVINDEX1();
Vector VCOUNTERPATY_STATUS = purchase.getVCOUNTERPATY_STATUS();

Vector VTAX_DTL = purchase.getVTAX_DTL();
Vector VNOM_BLOCK = purchase.getVNOM_BLOCK();

String start_dt = purchase.getStart_dt();
String end_dt = purchase.getEnd_dt();
String cont_name = purchase.getCont_name();
String dcq = purchase.getDcq();
String mdcq_percentage = purchase.getMdcq_percentage();
if(mdcq_percentage.equals("")){
	mdcq_percentage="100";
}
if(dcq.equals("")){
	dcq="0";
}

String displayContNm="";
if(!start_dt.equals("") && !end_dt.equals(""))
{
	displayContNm=cont_name+"  ("+start_dt+" - "+end_dt+")";
}

String gen_dt="";
String gen_time="";
String dateTime = purchase.getDateTime();
if(gen_dt.equals(""))
{
	String split[] = dateTime.split(" ");
	gen_dt = split[0];
	gen_time = split[1];
}

double day_total_buyNom_qty = purchase.getDay_total_buyNom_qty();

int count = utildate.getDays(st_dt, gas_dt);
int count1 = utildate.getDays(en_dt, gas_dt);

if(count >= 0 && count1 > 0)
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
							<%if(nomination_freq.equals("W")){%>Weekly
	    					<%}else if(nomination_freq.equals("M")){%>Monthly
	    					<%}else if(nomination_freq.equals("F")){%> Fortnightly
	    					<%} %> Seller Nomination (Purchase)
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
				    				<input type="text" class="form-control form-control-sm" name="cont_name" value="<%=displayContNm%>" maxLength="50" readOnly style="font-weight:bold;">
				    				<input type="hidden" class="form-control form-control-sm" name="counterparty_cd" value="<%=counterparty_cd%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_no" value="<%=cont_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_rev_no" value="<%=cont_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cargo_no" value="<%=cargo_no%>" readOnly>
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
      				<%if(!counterparty_cd.equals("") && !counterparty_cd.equals("0")){ %>
      				<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th><input class="form-check-input" type="checkbox" name="chkAll" onclick="checkAll(this);"></th>
										<th>Gas Date</th>
										<th>DCQ</th>
										<th>MDCQ</th>
										<th><input class="form-check-input" type="checkbox" name="chkBuAll" onclick="checkBuAll(this);">&nbsp;Business Unit</th>
										<th><input class="form-check-input" type="checkbox" name="chkPlantAll" onclick="checkPlantAll(this);">&nbsp;Trader Plant</th>
										<th>Tax</th>
										<th>Rev#</th>
										<th>Buyer Nomination (Rev)<br>(MMBTU)</th>
										<th>Energy<br>(MMBTU)</th>
										<th>Energy<br>(SCM)</th>
										<th>Gen Date:Time</th>
										<th>Calorific Value Base<br>KCal/SCM</th>
									</tr>
								</thead>
								<tbody>
								<%if(VGAS_DATE.size()>0) {%>
								<%int j=0; int k=0;
								
								for(int i=0;i<VGAS_DATE.size(); i++)
								{ 
									if(Integer.parseInt(""+VINDEX.elementAt(i)) > 0)
									{
										int n=0;
										for(j=j; j<VBU_PLANT_SEQ.size(); j++)
										{
											n=n+1;
											if(Integer.parseInt(""+VINDEX1.elementAt(j)) > 0)
											{
												int m=0;
												for(k=k; k<VCOUNTERPARTY_PLANT_SEQ.size(); k++)
												{
													m=m+1;
													int rowspan=Integer.parseInt(""+VINDEX.elementAt(i))*Integer.parseInt(""+VINDEX1.elementAt(j));
													int rowspan1=Integer.parseInt(""+VINDEX1.elementAt(j));
													%>
													
													<tr>
														<%if((n==1 && m==1))	
														{%>
															<td align="center" valign="middle" rowspan="<%=rowspan%>">
																<input class="form-check-input" type="checkbox" name="chk" id="chk<%=i%>" onclick="setEnableBuWise(this,'<%=i%>');calculateSCM('<%=i%>');"
																<%if(VCOUNTERPATY_STATUS.elementAt(i).equals("N")) {%>disabled style="pointer-events: none;"<%} %>>
																<input type="hidden" name="index" id="index<%=i%>" value="<%=VINDEX.elementAt(i)%>" disabled>
																<input type="hidden" name="ivalue" id="ivalue<%=i%>" value="<%=i%>" disabled>
																<input type="hidden" name="cp_status" id="cp_status<%=i%>" value="<%=VCOUNTERPATY_STATUS.elementAt(i)%>" disabled>
															</td>
															<td align="center" rowspan="<%=rowspan%>">
																<div style="width:100px;">
																	<div class="input-group input-group-sm">
												      					<input type="text" class="form-control form-control-sm date" name="week_gas_dt" id="week_gas_dt<%=i%>" value="<%=VGAS_DATE.elementAt(i)%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" readOnly disabled>
												      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
												      				</div>
											      				</div>
															</td>
															<td align="right" rowspan="<%=rowspan%>">
																<%=VDCQ.elementAt(i)%>
																<input type="hidden" name="dcq_qty" id="dcq_qty<%=i%>" value="<%=VDCQ.elementAt(i)%>">
															</td>
															<td align="right" rowspan="<%=rowspan%>">
																<%=VMDCQ.elementAt(i)%>
																<input type="hidden" name="mdcq_qty" id="mdcq_qty<%=i%>" value="<%=VMDCQ.elementAt(i)%>">
															</td>
														<%} %>
														<%if(m<=1){ %>
															<td align="left" rowspan="<%=rowspan1%>">
																<input class="form-check-input" type="checkbox" name="chk_bu" id="chk_bu<%=i%><%=n%>" disabled onclick="setEnablePlantWise(this,'<%=i%>','<%=n%>')">&nbsp;<%=VBU_PLANT_ABBR.elementAt(j)%>
																<input type="hidden" name="index1<%=i%><%=n%>" id="index1<%=i%><%=n%>" value="<%=VINDEX1.elementAt(j)%>" disabled>
																<input type="hidden" name="bu_plant_seq<%=i%><%=n%>" id="bu_plant_seq<%=i%><%=n%>" value="<%=VBU_PLANT_SEQ.elementAt(j)%>" disabled>
															</td>
														<%} %>
														<td align="left" <%if(VNOM_BLOCK.elementAt(k).equals("Y")) {%>style="background: #df9fbf;" title="Invoice Generated!"<%} %>>
															<input class="form-check-input" type="checkbox" name="chk_plant" id="chk_plant<%=i%><%=n%><%=m%>" 
															disabled onclick="setEnableDisabled(this,'<%=i%>','<%=n%>','<%=m%>')">&nbsp;<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(k)%>
															
															<input type="hidden" name="plant_seq<%=i%><%=n%><%=m%>" id="plant_seq<%=i%><%=n%><%=m%>" value="<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(k)%>" disabled>
															<input type="hidden" name="transporter_cd<%=i%><%=n%><%=m%>" id="transporter_cd<%=i%><%=n%><%=m%>" value="<%=VTRANSPORTER_CD.elementAt(k)%>" disabled>
															<input type="hidden" name="transporter_plant_seq<%=i%><%=n%><%=m%>" id="transporter_plant_seq<%=i%><%=n%><%=m%>" value="<%=VTRANSPORTER_PLANT_SEQ.elementAt(k)%>" disabled>
															<input type="hidden" name="nom_block<%=i%><%=n%><%=m%>" id="nom_block<%=i%><%=n%><%=m%>" value="<%=VNOM_BLOCK.elementAt(k)%>" disabled>
														</td>
														<td><%=VTAX_DTL.elementAt(k) %></td>
														<td align="center">
															<%=VNOM_REV_NO.elementAt(k) %>
														</td>
														<td align="right">
															<%=VBUYER_NOM.elementAt(k)%> (<%=VBUYER_NOM_REV_NO.elementAt(k)%>)
															<input type="hidden" name="buyer_nom_qty<%=i%><%=n%><%=m%>" id="buyer_nom_qty<%=i%><%=n%><%=m%>" value="<%=VBUYER_NOM.elementAt(k)%>">
														</td>
														<td align="center">
															<div style="width:100px;">
																<input type="text" class="form-control form-control-sm" name="qty_mmbtu<%=i%><%=n%><%=m%>" id="qty_mmbtu<%=i%><%=n%><%=m%>" value="<%=VQTY_MMBTU.elementAt(k)%>" 
																style="text-align:right;background:<%=VNOM_COLOR.elementAt(k)%>" 
																onpaste="doPaste('<%=i%>','<%=n%>','<%=m%>');" 
																onblur="negNumber(this);checkNumber1(this,9,2);checkQty('<%=i%>','<%=n%>','<%=m%>');calculateSCM('<%=i%>');" disabled>
																<input type="hidden" class="form-control form-control-sm" name="tmp_qty_mmbtu<%=i%><%=n%><%=m%>" id="tmp_qty_mmbtu<%=i%><%=n%><%=m%>" value="<%=VQTY_MMBTU.elementAt(k)%>" style="text-align:right" disabled>
															</div>
														</td>
														<td align="center">
															<div style="width:100px;">
																<input type="text" class="form-control form-control-sm" name="qty_scm<%=i%><%=n%><%=m%>" id="qty_scm<%=i%><%=n%><%=m%>" value="<%=VQTY_SCM.elementAt(k)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
															</div>
														</td>
														<td align="center">
															<div style="width:220px;">
																<div class="row m-b-5">
																	<div class="col">
																		<div class="input-group input-group-sm">
													      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="gen_dt<%=i%><%=n%><%=m%>" id="gen_dt<%=i%><%=n%><%=m%>" value="<%=VGEN_DT.elementAt(k)%>" maxLength="10" 
													      					style="background:<%=VNOM_COLOR.elementAt(k)%>"
													      					onblur="validateDate(this);" onchange="validateDate(this);" disabled>
													      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
													      				</div>
												      				</div>
												      				<div class="col">
													      				<div class="input-group input-group-sm" >
												      						<input type="text" class="form-control form-control-sm" name="gen_time<%=i%><%=n%><%=m%>" id="gen_time<%=i%><%=n%><%=m%>" value="<%=VGEN_TIME.elementAt(k)%>" maxLength="5" 
												      						style="width:15px;background:<%=VNOM_COLOR.elementAt(k)%>"
												      						onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off" disabled>
												      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
											      						</div>
										      						</div>
										      						<input type="hidden" name="tmp_gen_dt<%=i%><%=n%><%=m%>" id="tmp_gen_dt<%=i%><%=n%><%=m%>" value="<%=gen_dt%>">
										      						<input type="hidden" name="tmp_gen_time<%=i%><%=n%><%=m%>" id="tmp_gen_time<%=i%><%=n%><%=m%>" value="<%=gen_time%>">
										      					</div>
										      				</div>
														</td>
														<td align="center">
															<div style="width:300px;">
																<div class="row m-b-5">
																	<div class="col">
																		<input type="radio" name="rd<%=i%><%=n%><%=m%>" id="rd1<%=i%><%=n%><%=m%>" onclick="calculateSCM('<%=i%>');" <%if(VBASE.elementAt(k).equals("GCV")){ %>checked<%} %> disabled>&nbsp;GCV
												      				</div>
												      				<div class="col">
												      					<input type="text" class="form-control form-control-sm" name="gcv<%=i%><%=n%><%=m%>" id="gcv<%=i%><%=n%><%=m%>" value="<%=VGCV.elementAt(k)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(k)%>" onblur="checkNumber1(this,7,2);calculateSCM('<%=i%>');" disabled>
												      				</div>
												      				<div class="col">
												      					<input type="radio" name="rd<%=i%><%=n%><%=m%>" id="rd2<%=i%><%=n%><%=m%>" onclick="calculateSCM('<%=i%>');" <%if(VBASE.elementAt(k).equals("NCV")){ %>checked<%} %> disabled>&nbsp;NCV 
												      				</div>
												      				<div class="col">
													      				<input type="text" class="form-control form-control-sm" name="ncv<%=i%><%=n%><%=m%>" id="ncv<%=i%><%=n%><%=m%>" value="<%=VNCV.elementAt(k)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(k)%>" onblur="checkNumber1(this,7,2);calculateSCM('<%=i%>');" disabled>
										      						</div>
										      						<input type="hidden" name="base<%=i%><%=n%><%=m%>" id="base<%=i%><%=n%><%=m%>" value="<%=VBASE.elementAt(k)%>">
										      					</div>
										      				</div>
														</td>
													</tr>
													
													<%if(Integer.parseInt(""+VINDEX1.elementAt(j)) == m)
													{
														k=k+1;
														break;
													}
												} %>
											<%} %>
											<%if(Integer.parseInt(""+VINDEX.elementAt(i)) == n)
											{
												j=j+1;
												break;
											}
										}%>
									<%}%>
								<%}%>
								<%}else{ %>
									<tr>
										<td colspan="15" align="center"><%=utilmsg.infoMessage("<b>Buyer Nomination is not Done for the Selected Period!</b>")%></td>
									</tr>
								<%} %>	
								</tbody>
							</table>
						</div>
					</div>
					<%}else{ %>
						<%=utilmsg.infoMessage("<b>Select Contract Detail!</b>")%>
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


<input type="hidden" name="option" value="WEEKLY_SELLER_NOM">
<input type="hidden" name="prev_counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">

<input type="hidden" name="contract_st_dt" value="<%=start_dt%>">
<input type="hidden" name="contract_end_dt" value="<%=end_dt%>">

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