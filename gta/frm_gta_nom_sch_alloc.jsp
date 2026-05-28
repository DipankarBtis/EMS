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
	var customer_cd = document.forms[0].customer_cd.value;
	var prev_counterparty_cd = document.forms[0].prev_counterparty_cd.value;
	var prev_customer_cd = document.forms[0].prev_customer_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var gtc_type = document.forms[0].gtc_type.value;
	
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var hid_from_dt=document.forms[0].hid_from_dt.value;
	var hid_to_dt = document.forms[0].hid_to_dt.value;
	var cont_start_dt = document.forms[0].cont_start_dt.value;
	var cont_end_dt = document.forms[0].cont_end_dt.value;
	
	var prev_contract_type=document.forms[0].prev_contract_type.value;
	var prev_gtc_type = document.forms[0].prev_gtc_type.value;
	
	if(prev_contract_type != contract_type)
	{
		counterparty_cd="0";
		customer_cd="0"
		agmt_no="";
		agmt_rev_no="";
		cont_no="";
		cont_rev_no="";
		
		from_dt="";
		to_dt="";
	}
	else if(prev_counterparty_cd != counterparty_cd)
	{
		customer_cd="0"
		agmt_no="";
		agmt_rev_no="";
		cont_no="";
		cont_rev_no="";
		
		from_dt="";
		to_dt="";
	}
	else if(prev_customer_cd != customer_cd)
	{
		counterparty_cd="0";
		agmt_no="";
		agmt_rev_no="";
		cont_no="";
		cont_rev_no="";
		
		from_dt="";
		to_dt="";
	}
	
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
		   		alert("From date must be in the range of GTC Start and End date..\nGTC Period ("+cont_start_dt+" - "+cont_end_dt+")..\n");
		   		document.forms[0].from_dt.value = hid_from_dt;
		   		flag = false
		 	}
		 	
		 	val1 = compareDate(to_dt,cont_end_dt);
		 	if(val1=="1")
		 	{
		   		alert("To date must be less or equal to GTC Start and End date..\nGTC Period ("+cont_start_dt+" - "+cont_end_dt+")..\n");
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
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "frm_gta_nom_sch_alloc.jsp?counterparty_cd="+counterparty_cd+"&customer_cd="+customer_cd+
				"&gtc_type="+gtc_type+"&u="+u+"&contract_type="+contract_type+"&from_dt="+from_dt+"&to_dt="+to_dt+
				"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}

var newWindow;
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var customer_cd = document.forms[0].customer_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	
	var flag=true;
	if(contract_type=="C")
	{
		if((customer_cd=="0" || trim(customer_cd) == "") && (contract_type=="0" || trim(contract_type) == ""))
		{
			alert("Select Customer!\nSelect Contract Type!")
			flag=false;
		}
		else if((customer_cd=="0" || trim(customer_cd) == ""))
		{
			alert("Select Customer!")
			flag=false;
		}
		else if((contract_type=="0" || trim(contract_type) == ""))
		{
			alert("Select Contract Type!")
			flag=false;
		}
	}
	else
	{
		if((counterparty_cd=="0" || trim(counterparty_cd) == "") && (contract_type=="0" || trim(contract_type) == ""))
		{
			alert("Select Transporter!\nSelect Contract Type!")
			flag=false;
		}
		else if((counterparty_cd=="0" || trim(counterparty_cd) == ""))
		{
			alert("Select Transporter!")
			flag=false;
		}
		else if((contract_type=="0" || trim(contract_type) == ""))
		{
			alert("Select Contract Type!")
			flag=false;
		}
	}
	
	var url="frm_gta_nom_contract_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&customer_cd="+customer_cd;
	
	if(flag)
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"GTA Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"GTA Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function setContractDetail(countrpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no)
{
	var contract_type = document.forms[0].contract_type.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var gtc_type = document.forms[0].gtc_type.value;
	var customer_cd = document.forms[0].customer_cd.value;
	
	if(contract_type=="C")
	{
		counterparty_cd=countrpty_cd;
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gta_nom_sch_alloc.jsp?counterparty_cd="+counterparty_cd+"&customer_cd="+customer_cd+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&contract_type="+contract_type+
			"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&gtc_type="+gtc_type+
			"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setEnableDisabled(obj,index)
{
	/* var counterparty_cd = document.getElementById("counterparty_cd"+index);
	var agmt_no = document.getElementById("agmt_no"+index);
	var agmt_rev_no = document.getElementById("agmt_rev_no"+index);
	var cont_no = document.getElementById("cont_no"+index);
	var cont_rev_no = document.getElementById("cont_rev_no"+index);
	var contract_type = document.getElementById("contract_type"+index); */
	
	var gtc_type = document.forms[0].gtc_type.value;
	
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
	
	var exit_rd1 = document.getElementById("exit_rd1"+index);
	var exit_rd2 = document.getElementById("exit_rd2"+index);
	var exit_gcv = document.getElementById("exit_gcv"+index);
	var exit_ncv = document.getElementById("exit_ncv"+index);
	var exit_base = document.getElementById("exit_base"+index);
	var exit_qty_mmbtu = document.getElementById("exit_qty_mmbtu"+index);
	var exit_qty_scm = document.getElementById("exit_qty_scm"+index);
	var adjust_imb = document.getElementById("adjust_imb"+index);
	
	var mdq = document.getElementById("mdq"+index);
	var mdq_unit = document.getElementById("mdq_unit"+index);
	
	if(obj.checked)
	{
		/* counterparty_cd.disabled=false;
		agmt_no.disabled=false;
		agmt_rev_no.disabled=false;
		cont_no.disabled=false;
		cont_rev_no.disabled=false;
		contract_type.disabled=false; */
		
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
		
		if(gtc_type == "C")
		{
			rd1.style.pointerEvents = "none";
			rd2.style.pointerEvents = "none";
			gcv.readOnly=true;
			ncv.readOnly=true;
			base.readOnly=true;
			qty_mmbtu.readOnly=true;
			qty_scm.readOnly=true;
		}
		
		exit_rd1.disabled=false;
		exit_rd2.disabled=false;
		exit_gcv.disabled=false;
		exit_ncv.disabled=false;
		exit_base.disabled=false;
		exit_qty_mmbtu.disabled=false;
		exit_qty_scm.disabled=false;
		adjust_imb.disabled=false;
		
		mdq.disabled=false;
		mdq_unit.disabled=false;
		
		if(gtc_type != "N")
		{
			mdq.readOnly=true;
		}
		else
		{
			mdq.readOnly=false;
		}
	}
	else
	{
		/* counterparty_cd.disabled=true;
		agmt_no.disabled=true;
		agmt_rev_no.disabled=true;
		cont_no.disabled=true;
		cont_rev_no.disabled=true;
		contract_type.disabled=true; */
		
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
		
		if(gtc_type == "C")
		{
			rd1.style.pointerEvents = "none";
			rd2.style.pointerEvents = "none";
			gcv.readOnly=true;
			ncv.readOnly=true;
			base.readOnly=true;
			qty_mmbtu.readOnly=true;
			qty_scm.readOnly=true;
		}
		
		exit_rd1.disabled=true;
		exit_rd2.disabled=true;
		exit_gcv.disabled=true;
		exit_ncv.disabled=true;
		exit_base.disabled=true;
		exit_qty_mmbtu.disabled=true;
		exit_qty_scm.disabled=true;
		adjust_imb.disabled=true;
		
		mdq.disabled=true;
		mdq_unit.disabled=true;
		
		mdq.readOnly=true;
	}
}

function checkAll(obj)
{
	var chk = document.forms[0].chk;
	var is_done = document.forms[0].is_done;
	var is_active = document.forms[0].is_active;
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0; i<chk.length; i++)
			{
				if(is_done[i].value=="Y" && is_active[i].value=="Y")
				{
					if(obj.checked)
					{
						chk[i].checked=true;
						setEnableDisabled(chk[i],i)
					}
					else
					{
						chk[i].checked=false;
						setEnableDisabled(chk[i],i)
					}
				}
			}
		}
		else
		{
			if(is_done.value=="Y" && is_active.value=="Y")
			{
				if(obj.checked)
				{
					chk.checked=true;
					setEnableDisabled(chk,"0")
				}
				else
				{
					chk.checked=false;
					setEnableDisabled(chk,"0")
				}
			}
		}
	}
}

function checkQty(index)
{
	var gtc_type = document.forms[0].gtc_type.value;
	
	if(gtc_type != "A")
	{
		/*
		var exit_tmp_qty_mmbtu = document.getElementById("exit_tmp_qty_mmbtu"+index);
		var exit_qty_mmbtu = document.getElementById("exit_qty_mmbtu"+index);
		
		if(parseFloat(exit_qty_mmbtu.value) > parseFloat(exit_tmp_qty_mmbtu.value))
		{
			alert("Exit Point Scheduling "+parseFloat(exit_qty_mmbtu.value)+" MMBTU can not exceed Exit Point Nomination "+parseFloat(exit_tmp_qty_mmbtu.value)+" MMBTU");
			exit_qty_mmbtu.value = exit_tmp_qty_mmbtu.value;
		}
		*/
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
		var scm = parseFloat("0");
		if (parseFloat(baseVal) != 0)
		{
			scm = ""+round(((parseFloat(""+qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),0);
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
		var scm = parseFloat("0");
		if (parseFloat(baseVal) != 0)
		{
			scm = ""+round(((parseFloat(""+exit_qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),0);
		}
		
		if(isNaN(scm))
		{
			//exit_qty_scm.value="";
			exit_qty_scm.value="0";
		}
		else
		{
			exit_qty_scm.value = scm;
		}
	}
}

enableButton=true;
function doSubmit()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var gtc_type = document.forms[0].gtc_type.value;
	
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	var sales_cont_map=document.forms[0].sales_cont_map.value;
	var bu_plant_seq=document.forms[0].bu_plant_seq.value;
	var entry_pt_mapping_id=document.forms[0].entry_pt_mapping_id.value;
	var exit_pt_mapping_id=document.forms[0].exit_pt_mapping_id.value;
	
	var chk = document.forms[0].chk;
	var gas_dt = document.forms[0].gas_dt;
	var gen_dt = document.forms[0].gen_dt;
	var gen_time = document.forms[0].gen_time;
		
	var gcv = document.forms[0].gcv;
	var ncv = document.forms[0].ncv;
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	
	var exit_gcv = document.forms[0].exit_gcv;
	var exit_ncv = document.forms[0].exit_ncv;
	var exit_qty_mmbtu = document.forms[0].exit_qty_mmbtu;
	var exit_qty_scm = document.forms[0].exit_qty_scm;
	
	var msg="";
	var flag=true;
	var chk_count=parseInt("0");
	
	if(counterparty_cd=="0" || trim(counterparty_cd)=="")
	{
		msg+="Select Transporter!\n";
		flag=false;
	}
	if(trim(contract_type)=="")
	{
		msg+="Select GTC Type!\n";
		flag=false;
	}
	if(trim(agmt_no)=="" && trim(cont_no)=="")
	{
		msg+="Select GTC!\n";
		flag=false;
	}
	if(trim(sales_cont_map)=="" && contract_type=="C")
	{
		msg+="Sales Contract Mapping is Missing!\n";
		flag=false;
	}
	if(trim(entry_pt_mapping_id)=="")
	{
		msg+="Entry Point Mapping is Missing!\n";
		flag=false;
	}
	if(trim(exit_pt_mapping_id)=="")
	{
		msg+="Exit Point Mapping is Missing!\n";
		flag=false;
	}
	if(trim(bu_plant_seq)=="")
	{
		msg+="Business Unit is Missing!\n";
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
					msg+="Enter Gas Date!\n";
					flag=false;
				} 
				if(trim(gen_dt[i].value)=="")
				{
					msg+="Enter Gen Date!\n";
					flag=false;
				}
				if(trim(gen_time[i].value)=="")
				{
					msg+="Enter Gen Time for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(gcv[i].value)=="")
				{
					msg+="Missing Entry Point GCV for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(ncv[i].value)=="")
				{
					msg+="Missing Entry Point NCV for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(qty_mmbtu[i].value)=="")
				{
					msg+="Missing Entry Point Energy(MMBTU) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(qty_scm[i].value)=="")
				{
					msg+="Missing Entry Point Energy(SCM) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				
				if(trim(exit_gcv[i].value)=="")
				{
					msg+="Enter Exit Point GCV for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(exit_ncv[i].value)=="")
				{
					msg+="Enter Exit Point NCV for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(exit_qty_mmbtu[i].value)=="")
				{
					msg+="Enter Exit Point Energy(MMBTU) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(exit_qty_scm[i].value)=="")
				{
					msg+="Enter Exit Point Energy(SCM) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
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
				msg+="Missing Entry Point GCV!\n";
				flag=false;
			}
			if(trim(ncv.value)=="")
			{
				msg+="Missing Entry Point NCV!\n";
				flag=false;
			}
			if(trim(qty_mmbtu.value)=="")
			{
				msg+="Missing Entry Point Energy(MMBTU)!\n";
				flag=false;
			}
			if(trim(qty_scm.value)=="")
			{
				msg+="Missing Entry Point Energy(SCM)!\n";
				flag=false;
			}
			
			if(trim(exit_gcv.value)=="")
			{
				msg+="Enter Exit Point GCV!\n";
				flag=false;
			}
			if(trim(exit_ncv.value)=="")
			{
				msg+="Enter Exit Point NCV!\n";
				flag=false;
			}
			if(trim(exit_qty_mmbtu.value)=="")
			{
				msg+="Enter Exit Point Energy(MMBTU)!\n";
				flag=false;
			}
			if(trim(exit_qty_scm.value)=="")
			{
				msg+="Enter Exit Point Energy(SCM)!\n";
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
		var type="";
		if(gtc_type=="N")
		{
			type="Nomination";
		}
		else if(gtc_type=="S")
		{
			type="Scheduling";
		}
		else if(gtc_type=="A")
		{
			type="Allocation";
		}
		var a = confirm("Do you want to Submit Transporter "+type+"?");
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

function totalQty()
{
	var gen_dt = document.forms[0].gen_dt;
	var gen_time = document.forms[0].gen_time;
	
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	var exit_qty_mmbtu = document.forms[0].exit_qty_mmbtu;
	var exit_qty_scm = document.forms[0].exit_qty_scm;
	var adjust_imb = document.forms[0].adjust_imb;
	
	var tot_qty_mmbtu = document.forms[0].tot_qty_mmbtu;
	var tot_qty_scm = document.forms[0].tot_qty_scm;
	var tot_exit_qty_mmbtu = document.forms[0].tot_exit_qty_mmbtu;
	var tot_exit_qty_scm = document.forms[0].tot_exit_qty_scm;
	var tot_adjust_imb = document.forms[0].tot_adjust_imb;
	
	var tot_entry_mmbtu=parseFloat("0");
	var tot_entry_scm=parseFloat("0");
	
	var tot_exit_mmbtu=parseFloat("0");
	var tot_exit_scm=parseFloat("0");
	
	var tot_adj_imb =parseFloat("0");
	
	if(gen_time!=null && gen_time!=undefined)
	{
		if(gen_time.length!=undefined)
		{
			for(var i=0; i<gen_time.length; i++)
			{
				if(trim(qty_mmbtu[i].value)!="")
				{
					tot_entry_mmbtu = tot_entry_mmbtu + parseFloat(qty_mmbtu[i].value)
				}
				if(trim(qty_scm[i].value)!="")
				{
					tot_entry_scm = tot_entry_scm + parseFloat(qty_scm[i].value)
				}
				
				if(trim(exit_qty_mmbtu[i].value)!="")
				{
					tot_exit_mmbtu = tot_exit_mmbtu + parseFloat(exit_qty_mmbtu[i].value)
				}
				if(trim(exit_qty_scm[i].value)!="")
				{
					tot_exit_scm = tot_exit_scm + parseFloat(exit_qty_scm[i].value)
				}
				
				if(trim(adjust_imb[i].value)!="")
				{
					tot_adj_imb = tot_adj_imb + parseFloat(adjust_imb[i].value)
				}
			}
		}
		else
		{
			if(trim(qty_mmbtu.value)!="")
			{
				tot_entry_mmbtu = tot_entry_mmbtu + parseFloat(qty_mmbtu.value)
			}
			if(trim(qty_scm.value)!="")
			{
				tot_entry_scm = tot_entry_scm + parseFloat(qty_scm[i].value)
			}
			
			if(trim(exit_qty_mmbtu.value)!="")
			{
				tot_exit_mmbtu = tot_exit_mmbtu + parseFloat(exit_qty_mmbtu.value)
			}
			if(trim(exit_qty_scm.value)!="")
			{
				tot_exit_scm = tot_exit_scm + parseFloat(exit_qty_scm.value)
			}
			
			if(trim(adjust_imb.value)!="")
			{
				tot_adj_imb = tot_adj_imb + parseFloat(adjust_imb.value)
			}
		}
	}
	
	tot_qty_mmbtu.value=round(parseFloat(tot_entry_mmbtu),3)
	tot_qty_scm.value =round(parseFloat(tot_entry_scm),2)
	tot_exit_qty_mmbtu.value=round(parseFloat(tot_exit_mmbtu),3)
	tot_exit_qty_scm.value =round(parseFloat(tot_exit_scm),2)
	tot_adjust_imb.value =round(parseFloat(tot_adj_imb),2)
}

async function doPaste(point,index)
{
	var pastedText = event.clipboardData.getData('text/plain');
	//alert(pastedText);
	
	var arr=pastedText.split(/\n/);
	//alert(arr.length);
	
	/* for(var i=0; i<arr.length; i++)
	{
		if(trim(arr[i])=="")
		{
			alert(i+"==Gets Blank!")
		}
		else
		{
			alert(i+"=="+arr[i])
		}
	} */
	
	var chk = document.forms[0].chk;
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var exit_qty_mmbtu = document.forms[0].exit_qty_mmbtu;
	var mdq = document.forms[0].mdq;
	
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
									if(point=='X')
									{									
										exit_qty_mmbtu[i].value=arr[j];
										
										negNumber(exit_qty_mmbtu[i]);
										checkNumber1(exit_qty_mmbtu[i],10,3);
										checkQty(i);
										calculateSCM(i);
									}
									else if(point=='E')
									{									
										qty_mmbtu[i].value=arr[j];
										
										negNumber(qty_mmbtu[i]);
										checkNumber1(qty_mmbtu[i],10,3);
										checkQty(i);
										calculateSCM(i);
									}
									else if(point=='M')
									{									
										mdq[i].value=arr[j];
										
										negNumber(mdq[i]);
										checkNumber1(mdq[i],11,3);
									}
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
								if(point=='X')
								{
									exit_qty_mmbtu.value=arr[j];
									
									negNumber(exit_qty_mmbtu);
									checkNumber1(exit_qty_mmbtu,10,3);
									checkQty("0");
									calculateSCM("0");
								}
								else if(point=='E')
								{
									qty_mmbtu.value=arr[j];
									
									negNumber(qty_mmbtu);
									checkNumber1(qty_mmbtu,10,3);
									checkQty("0");
									calculateSCM("0");
								}
								else if(point=='M')
								{									
									mdq.value=arr[j];
									
									negNumber(mdq);
									checkNumber1(mdq,11,3);
								}
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
<jsp:useBean class="com.etrm.fms.gta.DataBean_GtaMaster" id="gta" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String customer_cd = request.getParameter("customer_cd")==null?"0":request.getParameter("customer_cd");
String gtc_type=request.getParameter("gtc_type")==null?"N":request.getParameter("gtc_type");
String contract_type=request.getParameter("contract_type")==null?"C":request.getParameter("contract_type");
String cont_map_id=request.getParameter("cont_map_id")==null?"":request.getParameter("cont_map_id");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");

String from_dt=request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?"":request.getParameter("to_dt");

gta.setCallFlag("GTC_NOM_SCHE_ALLOC");
gta.setComp_cd(owner_cd);
gta.setCounterparty_cd(counterparty_cd);
gta.setContract_type(contract_type);
gta.setGtc_type(gtc_type);
gta.setAgmt_no(agmt_no);
gta.setAgmt_rev_no(agmt_rev_no);
gta.setCont_no(cont_no);
gta.setCont_rev_no(cont_rev_no);
gta.setFrom_dt(from_dt);
gta.setTo_dt(to_dt);
gta.init();

Vector VCOUNTERPARTY_CD = gta.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = gta.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = gta.getVCOUNTERPARTY_ABBR();

Vector VCUSTOMER_CD = gta.getVCUSTOMER_CD();
Vector VCUSTOMER_NM = gta.getVCUSTOMER_NM();
Vector VCUSTOMER_ABBR = gta.getVCUSTOMER_ABBR();

String counterparty_abbr = gta.getCounterparty_abbr();
String cont_ref_no = gta.getCont_ref_no();
String start_dt = gta.getStart_dt();
String end_dt = gta.getEnd_dt();
String entry_pt_mapping_id =gta.getEntry_pt_mapping_id();
String exit_pt_mapping_id =gta.getExit_pt_mapping_id();
String entry_pt_nm = gta.getEntry_pt_nm();
String exit_pt_nm = gta.getExit_pt_nm();
String linked_sales_cont_map=gta.getLinked_sales_cont_map();
String bu_plant_seq=gta.getBu_plant_seq();
String display_dealNo=gta.getDisplay_dealNo();

String cont_name="";
//String cont_mapping=counterparty_abbr+" - "+contract_type+agmt_no+"-"+agmt_rev_no+"-"+cont_no+"-"+cont_rev_no;

if(!cont_ref_no.equals(""))
{
	cont_name=display_dealNo+" ("+cont_ref_no+") ["+start_dt+" - "+end_dt+"]";
}

if(from_dt.equals(""))
{
	from_dt=gta.getFrom_dt();
}
if(to_dt.equals(""))
{
	to_dt=gta.getTo_dt();
}

Vector VGAS_DT = gta.getVGAS_DT();
Vector VNOM_REV_NO = gta.getVNOM_REV_NO();
Vector VGEN_TIME = gta.getVGEN_TIME();
Vector VGEN_DT = gta.getVGEN_DT();
Vector VBASE = gta.getVBASE();
Vector VGCV = gta.getVGCV();
Vector VNCV = gta.getVNCV();
Vector VQTY_MMBTU = gta.getVQTY_MMBTU();
Vector VQTY_SCM = gta.getVQTY_SCM();
Vector VNOM_COLOR = gta.getVNOM_COLOR();

Vector VEXIT_BASE = gta.getVEXIT_BASE();
Vector VEXIT_GCV = gta.getVEXIT_GCV();
Vector VEXIT_NCV = gta.getVEXIT_NCV();
Vector VEXIT_QTY_MMBTU = gta.getVEXIT_QTY_MMBTU();
Vector VEXIT_QTY_SCM = gta.getVEXIT_QTY_SCM();
Vector VADJ_IMBALANCE = gta.getVADJ_IMBALANCE();

Vector VMDQ = gta.getVMDQ();
Vector VMDQ_UNIT = gta.getVMDQ_UNIT();

Vector VIS_DONE = gta.getVIS_DONE();
Vector VIS_ACTIVE = gta.getVIS_ACTIVE();

String top_head="Capacity Tranche";
String head_lbl="Transporter";
if(contract_type.equals("K"))
{
	head_lbl="Parking";
	top_head="Parking";
}

String total_entry_qty_mmbtu = gta.getTotal_entry_qty_mmbtu();
String total_entry_qty_scm=gta.getTotal_entry_qty_scm();
String total_exit_qty_mmbtu=gta.getTotal_exit_qty_mmbtu();
String total_exit_qty_scm=gta.getTotal_exit_qty_scm();
String total_adj_imbalance=gta.getTotal_adj_imbalance();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_GtaMaster">
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
							Capacity Tranche | Parking Contract 
							<%if(gtc_type.equals("N")){ %>Nomination
							<%}else if(gtc_type.equals("S")){ %>Scheduling
							<%}else if(gtc_type.equals("A")){ %>Allocation
							<%} %>
						</div>
						<div class="btn-group">
							<select class="btn btn-outline-secondary btngrp btnactive" name="gtc_type" onchange="refresh();">
								<option value="N">Nomination</option>
				    			<option value="S">Scheduling</option>
				    			<option value="A">Allocation</option>
				    		</select>
						</div>
					</div>
					<script>document.forms[0].gtc_type.value="<%=gtc_type%>"</script>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<%-- <label class="form-label"><b><%=top_head%> Type</b></label> --%>
									<label class="form-label"><b>Contract Type</b></label>
								</div>
							</div>
						</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="contract_type" onchange="refresh();">
										<option value="C">Customer</option>
						    			<option value="R">Transporter</option>
						    			<option value="K">Parking</option>
						    		</select>
						    		<script>document.forms[0].contract_type.value="<%=contract_type%>"</script>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
								<%if(contract_type.equals("C")){ %>
									<label class="form-label"><b>Customer</b></label>
								<%}else{ %>
									<label class="form-label"><b>Transporter</b></label>
								<%} %>
								</div>
							</div>
						</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<!-- CUSTOMER MASTER -->
									<select class="form-select form-select-sm" name="customer_cd" onchange="refresh();" <%if(!contract_type.equals("C")){ %>style="display:none"<%} %>>
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCUSTOMER_CD.size();i++){ %>
										<option value="<%=VCUSTOMER_CD.elementAt(i)%>"><%=VCUSTOMER_ABBR.elementAt(i)%> - <%=VCUSTOMER_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].customer_cd.value="<%=customer_cd%>"</script>
									
									<!-- TRANSPORTER MASTER -->
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh();" <%if(contract_type.equals("C")){ %>style="display:none"<%} %>>
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
									<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
									<input type="hidden" class="form-control form-control-sm" name="cont_no" value="<%=cont_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_rev_no" value="<%=cont_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="sales_cont_map" value="<%=linked_sales_cont_map%>" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="bu_plant_seq" value="<%=bu_plant_seq%>" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="entry_pt_mapping_id" value="<%=entry_pt_mapping_id%>" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="exit_pt_mapping_id" value="<%=exit_pt_mapping_id%>" readOnly>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="button" class="btn btn-info btn-sm select_btn" value="Select <%=top_head%>" onclick="openContList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" value="<%=cont_name%>" readOnly style="font-weight:bold;">
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody" <%if(cont_no.equals("0") || cont_no.equals("")){ %>style="display: none;"<%} %>>
					<div class="row">
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Gas Day : From</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<input type="hidden" name="hid_from_dt" value="<%=from_dt %>">
			       					<input type="hidden" name="hid_to_dt" value="<%=to_dt%>">
				    			</div>
				    			<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    		</div>
			    		</div>
			    		<div class="col-sm-4 col-xs-4 col-md-4"></div>
		    		</div>
		    	</div>
			    <div class="card-body cdbody" <%if(cont_no.equals("0") || cont_no.equals("")){ %>style="display: none;"<%} %>>
			   		<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th rowspan="2">
											<input type="checkbox" class="form-check-input" name="chkAll" onclick="checkAll(this);">
										</th>
										<th rowspan="2">Gas Day</th>
										<th rowspan="2">Rev No</th>
										<th rowspan="2">MDQ<br>(MMBTU)</th>
										<th colspan="3"><%=head_lbl%> Entry Point <font style="background:#ff99ff;color:var(--header_font_color);">(<%=entry_pt_nm%>)</font></th>
										<th colspan="3"><%=head_lbl%> Exit Point <font style="background:#ff99ff;;color:var(--header_font_color);">(<%=exit_pt_nm%>)</font></th>
										<th rowspan="2" <%if(!gtc_type.equals("A")){ %>style="display:none;"<%} %>>Adjust Imbalance</th>
										<th rowspan="2">Gen Date:Time</th>
									</tr>
									<tr>
										<th>Calorific Value Base<br>(KCal/SCM)</th>
										<th>Energy<br>(MMBTU)</th>
										<th>Energy<br>(SCM)</th>
										<th>Calorific Value Base<br>(KCal/SCM)</th>
										<th>Energy<br>(MMBTU)</th>
										<th>Energy<br>(SCM)</th>
									</tr>
								</thead>
								<tbody>
								<%for(int i=0;i<VGAS_DT.size(); i++){ %>
									<tr>
										<td align="center" valign="middle">
											<input type="checkbox" class="form-check-input" name="chk" 
											onclick="setEnableDisabled(this,'<%=i%>');calculateSCM('<%=i%>');" 
											<%if(VIS_ACTIVE.elementAt(i).equals("N")){ %>disabled style="pointer-events: none;"<%} %>
											<%if(VIS_DONE.elementAt(i).equals("N")){ %>style="pointer-events: none;"<%} %>>
											<input type="hidden" name="is_done" id="is_done<%=i%>" value="<%=VIS_DONE.elementAt(i)%>">
											<input type="hidden" name="is_active" id="is_active<%=i%>" value="<%=VIS_ACTIVE.elementAt(i)%>">
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm">
							      					<input type="text" class="form-control form-control-sm date" name="gas_dt" id="gas_dt<%=i%>" value="<%=VGAS_DT.elementAt(i)%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" readOnly disabled>
							      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      				</div>
						      				</div>
						      			</td>
										<td align="center">
											<%=VNOM_REV_NO.elementAt(i)%>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="mdq" id="mdq<%=i%>" value="<%=VMDQ.elementAt(i)%>" 
												style="text-align:right;<%if(gtc_type.equals("N")){ %>background:<%=VNOM_COLOR.elementAt(i)%><%}%>" 
												<%if(gtc_type.equals("N")){ %>onpaste="doPaste('M','<%=i%>');"<%} %>
												onblur="negNumber(this);checkNumber1(this,11,3);" disabled>
												<input type="hidden" name="mdq_unit" id="mdq_unit<%=i%>" value="<%=VMDQ_UNIT.elementAt(i)%>" readonly disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:150px;">
												<div <%if(!VBASE.elementAt(i).equals("GCV")){ %>style="display:none;"<%} %>>
													<div class="row m-b-5">
														<div class="col">
															<input type="radio" name="rd<%=i%>" id="rd1<%=i%>" <%if(VBASE.elementAt(i).equals("GCV")){ %>checked<%} %> disabled>&nbsp;GCV
									      				</div>
									      				<div class="col">
									      					<input type="text" class="form-control form-control-sm" name="gcv" id="gcv<%=i%>" value="<%=VGCV.elementAt(i)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(i)%>" onblur="checkNumber1(this,9,4);" disabled>
									      				</div>
									      			</div>
								      			</div>
								      			<div <%if(!VBASE.elementAt(i).equals("NCV")){ %>style="display:none;"<%} %>>
									      			<div class="row m-b-5">
									      				<div class="col">
									      					<input type="radio" name="rd<%=i%>" id="rd2<%=i%>" <%if(VBASE.elementAt(i).equals("NCV")){ %>checked<%} %> disabled>&nbsp;NCV 
									      				</div>
									      				<div class="col">
										      				<input type="text" class="form-control form-control-sm" name="ncv" id="ncv<%=i%>" value="<%=VNCV.elementAt(i)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(i)%>" onblur="checkNumber1(this,9,4);" disabled>
							      						</div>
							      					</div>
						      					</div>
						      					<input type="hidden" name="base" id="base<%=i%>" value="<%=VBASE.elementAt(i)%>" disabled>
						      				</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="qty_mmbtu" id="qty_mmbtu<%=i%>" value="<%=VQTY_MMBTU.elementAt(i)%>" 
												style="text-align:right;background:<%=VNOM_COLOR.elementAt(i)%>" 
												onpaste="doPaste('E','<%=i%>');"
												onblur="negNumber(this);checkNumber1(this,11,3);checkQty('<%=i%>');calculateSCM('<%=i%>');totalQty();" disabled>
												<input type="hidden" class="form-control form-control-sm" name="tmp_qty_mmbtu" id="tmp_qty_mmbtu<%=i%>" value="<%=VQTY_MMBTU.elementAt(i)%>" style="text-align:right" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="qty_scm" id="qty_scm<%=i%>" value="<%=VQTY_SCM.elementAt(i)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:150px;">
												<div <%if(!VEXIT_BASE.elementAt(i).equals("GCV")){ %>style="display:none;"<%} %>>
													<div class="row m-b-5">
														<div class="col">
															<input type="radio" name="exit_rd<%=i%>" id="exit_rd1<%=i%>" <%if(VEXIT_BASE.elementAt(i).equals("GCV")){ %>checked<%} %> style="pointer-events: none;" disabled>&nbsp;GCV
									      				</div>
									      				<div class="col">
									      					<input type="text" class="form-control form-control-sm" name="exit_gcv" id="exit_gcv<%=i%>" value="<%=VEXIT_GCV.elementAt(i)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(i)%>" onblur="checkNumber1(this,9,4);" disabled>
									      				</div>
									      			</div>
								      			</div>
								      			<div <%if(!VEXIT_BASE.elementAt(i).equals("NCV")){ %>style="display:none;"<%} %>>
													<div class="row m-b-5">
									      				<div class="col">
									      					<input type="radio" name="exit_rd<%=i%>" id="exit_rd2<%=i%>" <%if(VEXIT_BASE.elementAt(i).equals("NCV")){ %>checked<%} %> style="pointer-events: none;" disabled>&nbsp;NCV 
									      				</div>
									      				<div class="col">
										      				<input type="text" class="form-control form-control-sm" name="exit_ncv" id="exit_ncv<%=i%>" value="<%=VEXIT_NCV.elementAt(i)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(i)%>" onblur="checkNumber1(this,9,4);" disabled>
							      						</div>
							      					</div>
						      					</div>
						      					<input type="hidden" name="exit_base" id="exit_base<%=i%>" value="<%=VEXIT_BASE.elementAt(i)%>" disabled>
						      				</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="exit_qty_mmbtu" id="exit_qty_mmbtu<%=i%>" value="<%=VEXIT_QTY_MMBTU.elementAt(i)%>" 
												style="text-align:right;background:<%=VNOM_COLOR.elementAt(i)%>" 
												onpaste="doPaste('X','<%=i%>');"
												onblur="negNumber(this);checkNumber1(this,10,3);checkQty('<%=i%>');calculateSCM('<%=i%>');totalQty();" disabled>
												<input type="hidden" class="form-control form-control-sm" name="exit_tmp_qty_mmbtu" id="exit_tmp_qty_mmbtu<%=i%>" value="<%=VEXIT_QTY_MMBTU.elementAt(i)%>" style="text-align:right" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="exit_qty_scm" id="exit_qty_scm<%=i%>" value="<%=VEXIT_QTY_SCM.elementAt(i)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
											</div>
										</td>
										<td align="center" <%if(!gtc_type.equals("A")){ %>style="display:none;"<%} %>>
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="adjust_imb" id="adjust_imb<%=i%>" value="<%=VADJ_IMBALANCE.elementAt(i)%>" 
												style="text-align:right;background:<%=VNOM_COLOR.elementAt(i)%>" 
												<%-- onpaste="doPaste('X','<%=i%>');" --%>
												onblur="checkNumber1(this,10,3);checkQty('<%=i%>');totalQty();" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:220px;">
												<div class="row m-b-5">
													<div class="col">
														<div class="input-group input-group-sm">
									      					<input type="text" class="form-control form-control-sm date" name="gen_dt" id="gen_dt<%=i%>" value="<%=VGEN_DT.elementAt(i)%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" disabled>
									      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
									      				</div>
								      				</div>
								      				<div class="col">
									      				<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm" name="gen_time" id="gen_time<%=i%>" value="<%=VGEN_TIME.elementAt(i)%>" maxLength="5" style="width:15px;" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off" disabled>
								      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
							      						</div>
						      						</div>
						      					</div>
						      				</div>
										</td>
									</tr>
								<%} %>
									<tr style="font-weight: bold;">
										<td colspan="5" align="right">Total : </td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="tot_qty_mmbtu" value="<%=total_entry_qty_mmbtu%>" style="text-align:right;font-weight: bold;" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="tot_qty_scm" value="<%=total_entry_qty_scm%>" style="text-align:right;font-weight: bold;" disabled>
											</div>
										</td>
										<td></td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="tot_exit_qty_mmbtu" value="<%=total_exit_qty_mmbtu%>" style="text-align:right;font-weight: bold;" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="tot_exit_qty_scm" value="<%=total_exit_qty_scm%>" style="text-align:right;font-weight: bold;" disabled>
											</div>
										</td>
										<td align="center" <%if(!gtc_type.equals("A")){ %>style="display: none;"<%}%>>
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="tot_adjust_imb" value="<%=total_adj_imbalance%>" style="text-align:right;font-weight: bold;" disabled>
											</div>
										</td>
										<td></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center" <%if(cont_no.equals("0") || cont_no.equals("")){ %>style="display: none;"<%} %>>
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

<input type="hidden" name="option" value="GTA_NOM_SCH_ALLOC">

<input type="hidden" name="prev_counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="prev_customer_cd" value="<%=customer_cd%>">
<input type="hidden" name="prev_contract_type" value="<%=contract_type%>">
<input type="hidden" name="prev_gtc_type" value="<%=gtc_type%>">

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