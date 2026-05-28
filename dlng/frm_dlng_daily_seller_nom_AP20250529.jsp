<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<style>
/* .table-bordered > tbody > tr > td   
{
   border: 0px solid #ddd;
   border-bottom: 2px solid #ddd;
}
.table-bordered > tbody {
	border: 1px solid #ddd;
} */
</style>
<script>
function refresh()
{
	var gas_dt = document.forms[0].gas_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_daily_seller_nom.jsp?gas_dt="+gas_dt+"&u="+u;

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

function setEnableDisabled(obj, selectedIndex)
{
    var totalRows = parseInt(document.getElementById("total_rows").value);

    for (var index = 0; index < totalRows; index++)
    {
    	var isSelected = (parseInt(index) === parseInt(selectedIndex));
        
        var prefixIds = [
            "counterparty_cd", "agmt_no", "agmt_rev_no", "cont_no", "cont_rev_no",
            "contract_type", "cargo_no", "sf_id", "gen_time", "rd1", "rd2",
            "gcv", "ncv", "base", "qty_mmbtu", "qty_scm","qty_mt", "plant_seq",
            "trans_cd", "bu_plant_seq", "index_", "index1","truck_icon_"
        ];

        var truck_chk = document.getElementsByName('truck_chk_'+selectedIndex).length;

        // Enable or disable common fields
        prefixIds.forEach(function (id) {
            var element = document.getElementById(id + index);
            if (element) {
                element.disabled = !isSelected;
            }
        });

        var qty_mmbtu = document.getElementById("qty_mmbtu" + index);
        var qty_scm = document.getElementById("qty_scm" + index);
        var qty_mt = document.getElementById("qty_mt" + index);
        var truck_icon = document.getElementById("truck_icon_" + index);

        if (isSelected)
        {
            qty_mmbtu.readOnly = false;
            qty_scm.readOnly = true;
            qty_mt.readOnly = true;
            qty_mmbtu.style.pointerEvents = "auto";
            qty_scm.style.pointerEvents = "none";
            qty_mt.style.pointerEvents = "none";
            truck_icon.style.pointerEvents = "auto";

            //FOR TRUCK
            if(parseInt(truck_chk) > 0)
    		{
    			for(var i=0;i<parseInt(truck_chk);i++)
    			{
    				var truck_chk_obj = document.getElementById("truck_chk_"+i+"_"+selectedIndex);
    				//truck_chk_obj.disabled=false;
    				setEnableDisableTruck(truck_chk_obj,i,selectedIndex);
    			}
    		}
        }
        else
        {
            qty_mmbtu.readOnly = true;
            qty_scm.readOnly = true;
            qty_mt.readOnly = true;
            qty_mmbtu.style.pointerEvents = "none";
            qty_scm.style.pointerEvents = "none";
            qty_mt.style.pointerEvents = "none";
            truck_icon.style.pointerEvents = "none";
            
            //FOR TRUCK
            if(parseInt(truck_chk) > 0)
    		{
    			for(var i=0;i<parseInt(truck_chk);i++)
    			{
    				var truck_chk_obj = document.getElementById("truck_chk_"+i+"_"+selectedIndex);
    				//truck_chk_obj.disabled=true;
    				setEnableDisableTruck(truck_chk_obj,i,selectedIndex);
    			}
    		}
        }
    }
}

/* function setEnableDisabled(obj,index)
{
	var counterparty_cd = document.getElementById("counterparty_cd"+index);
	var agmt_no = document.getElementById("agmt_no"+index);
	var agmt_rev_no = document.getElementById("agmt_rev_no"+index);
	var cont_no = document.getElementById("cont_no"+index);
	var cont_rev_no = document.getElementById("cont_rev_no"+index);
	var contract_type = document.getElementById("contract_type"+index);
	var cargo_no = document.getElementById("cargo_no"+index);
	var sf_id = document.getElementById("sf_id"+index);
	
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
	var bu_plant_seq = document.getElementById("bu_plant_seq"+index);
	
	var l_index = document.getElementById("index_"+index);
	var index1 = document.getElementById("index1"+index);
	
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
		sf_id.disabled=false;
		gen_time.disabled=false;
		rd1.disabled=false;
		rd2.disabled=false;
		gcv.disabled=false;
		ncv.disabled=false;
		base.disabled=false;
		
		qty_mmbtu.disabled=false;
		qty_scm.disabled=false;
		
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
		
		counterparty_cd.disabled=true;
		agmt_no.disabled=true;
		agmt_rev_no.disabled=true;
		cont_no.disabled=true;
		cont_rev_no.disabled=true;
		contract_type.disabled=true;
		cargo_no.disabled=true;
		sf_id.disabled=true;
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
} */
/* 
function setSubEnableDisabled(obj,index,index1)
{
	var chk = document.getElementById("chk"+index);
	
	var sub_ct_ref = document.getElementById("sub_ct_ref"+index+""+index1);
	var temp_sub_ct_ref = document.getElementById("temp_sub_ct_ref"+index+""+index1);
	var sub_utr_ref = document.getElementById("sub_utr_ref"+index+""+index1);
	var sub_qty_mmbtu = document.getElementById("sub_qty_mmbtu"+index+""+index1);
	var sub_qty_scm = document.getElementById("sub_qty_scm"+index+""+index1);
	var sub_is_exist = document.getElementById("sub_is_exist"+index+""+index1);
	var sub_seq_no = document.getElementById("sub_seq_no"+index+""+index1);
	var sub_sf_id = document.getElementById("sub_sf_id"+index+""+index1);
	
	if(obj.checked)
	{
		sub_ct_ref.disabled=false;
		temp_sub_ct_ref.disabled=false;
		sub_utr_ref.disabled=false;
		sub_qty_mmbtu.disabled=false;
		sub_qty_scm.disabled=false;
		sub_seq_no.disabled=false;
		sub_sf_id.disabled=false;
		
		if(!chk.checked && sub_is_exist.value=="Y") //IF ENTRY EXIST TEHN SYSTEM WILL NOT ALLOW TO UNCHECK BUT THE OTHER INPUTE FIELD GETS DISBALED ONCE UNCHECKED MAIN
		{
			sub_ct_ref.disabled=true;
			temp_sub_ct_ref.disabled=true;
			sub_utr_ref.disabled=true;
			sub_qty_mmbtu.disabled=true;
			sub_qty_scm.disabled=true;
			sub_seq_no.disabled=true;
			sub_sf_id.disabled=true;
		}
	}
	else
	{
		sub_ct_ref.disabled=true;
		temp_sub_ct_ref.disabled=true;
		sub_utr_ref.disabled=true;
		sub_qty_mmbtu.disabled=true;
		sub_qty_scm.disabled=true;
		sub_seq_no.disabled=true;
		sub_sf_id.disabled=true;
	}		
} */


/* function calculateSCM(j_index,index)
{
	//grid one
	var rd1 = document.getElementById("rd1_"+j_index);
	var rd2 = document.getElementById("rd2_"+j_index);
	
	var grid_gcv = document.getElementById("grid_gcv_"+j_index).value;
	var grid_ncv = document.getElementById("grid_ncv_"+j_index).value;
	
	//var rd1 = document.getElementById("rd1"+index);
	//var rd2 = document.getElementById("rd2"+index);
	var base = document.getElementById("base"+index);
	/* var gcv = document.getElementById("gcv"+index);
	var ncv = document.getElementById("ncv"+index); 
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	var qty_scm = document.getElementById("qty_scm"+index);
	
	var baseVal = parseFloat("0");
	
	var deviding_factor = parseFloat("1");
	
	if(rd1.checked)
	{
		baseVal = parseFloat(gcv.value);
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
	for(var i=1;i<=parseInt(index1.value);i++)
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
	}
} */

function calculateSCM(index)
{
	/* var rd1 = document.getElementById("rd1"+index);
	var rd2 = document.getElementById("rd2"+index); */
	
	var rd1 = document.getElementById("rd1");
	var rd2 = document.getElementById("rd2");
	
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

function calculateMT(index)
{
	/* var rd1 = document.getElementById("rd1"+index);
	var rd2 = document.getElementById("rd2"+index); */
	
	var rd1 = document.getElementById("rd1");
	var rd2 = document.getElementById("rd2");
	
	var base = document.getElementById("base"+index);
	var gcv = document.getElementById("gcv"+index);
	var ncv = document.getElementById("ncv"+index);
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	var qty_mt = document.getElementById("qty_mt"+index);
	
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
							calculateSCM(i);
							calculateMT(i);
							totalQty();
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
						calculateSCM(i);
						calculateMT(i);
						totalQty();
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
					calculateSCM("0");
					calculateMT("0");
					totalQty();
				}
			}
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
	var qty_mt = document.forms[0].qty_mt;
	
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
				if(trim(qty_scm[i].value)=="")
				{
					msg+="Enter Energy(MT) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				
				var sub_count=parseInt("0");
				var index1 = document.getElementById("index1"+i);
				for(var k=1;k<=parseInt(index1.value);k++)
				{
					var sub_chk = document.getElementById("chk"+i+""+k);
					var sub_qty_mmbtu = document.getElementById("sub_qty_mmbtu"+i+""+k);
					var sub_qty_scm = document.getElementById("sub_qty_scm"+i+""+k);	
					
					var sub_ct_ref = document.getElementById("sub_ct_ref"+i+""+k);
					var temp_sub_ct_ref = document.getElementById("temp_sub_ct_ref"+i+""+k);
					
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
						
						if(sub_ct_ref.value != temp_sub_ct_ref.value)
						{
						}
					}
				}
				
				if(parseInt(sub_count) == 0 && parseInt(index1.value) > 0)
				{
					msg+="Please Select Atleast One Sub ROW for ROW - "+parseInt(i+1)+" for Submit!\n";
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
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function doTruckSubmit(i_index)
{
	var gas_dt = document.forms[0].gas_dt;
	var gen_dt = document.forms[0].gen_dt;
	var gen_time = document.forms[0].gen_time;
	
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
	var balance_qty = document.getElementById("balance_qty_"+i_index)
	
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
	
	if(parseInt(chk_count) == 0)
	{
		msg+="Please Select Atleast One Truck ROW for Submit!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Submit Seller Nomination?");
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
	var mdcq_qty = document.getElementById("mdcq_qty"+index);
	var int_map_id = document.getElementById("internal_map_id"+index); 
	
	var gas_dt = document.forms[0].gas_dt;
	
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	var qty_mt = document.forms[0].qty_mt;
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
						qty_mt[i].value="0";
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
							tot_mmbtu = parseFloat(cont_buyer_nom[i].value) + parseFloat(getBuyerNomContractWise(cont_internal_mapp_id[i].value));
							
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
						tot_mmbtu = parseFloat(cont_buyer_nom.value) + parseFloat(getBuyerNomContractWise(cont_internal_mapp_id.value));
						
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

function getBuyerNomContractWise(cont_internal_mapp_id)
{
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var internal_map_id = document.forms[0].internal_map_id;
	
	var tot_mmbtu =  parseFloat("0");
	
	if(qty_mmbtu!=null && qty_mmbtu!=undefined)
	{
		if(qty_mmbtu.length!=undefined)
		{
			for(var j=0; j<qty_mmbtu.length; j++)
			{
				if(cont_internal_mapp_id == internal_map_id[j].value)
				{
					if(trim(qty_mmbtu[j].value)!="")
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

</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_Dlng_ContractMgmt" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String nextdate = utildate.getNextDate();

String gas_dt = request.getParameter("gas_dt")==null?nextdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "-1");

cont_mgmt.setCallFlag("DAILY_SELLER_NOM");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setGas_dt(gas_dt);
cont_mgmt.init();

String gcv="9802.80";
String ncv="8831.35";

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = cont_mgmt.getVCOUNTERPARTY_NM();
Vector VTRANSPORTER_CD = cont_mgmt.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = cont_mgmt.getVTRANSPORTER_ABBR();
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
Vector VDCQ_MT = cont_mgmt.getVDCQ_MT();
Vector VCONT_NAME = cont_mgmt.getVCONT_NAME();
Vector VCONT_REF = cont_mgmt.getVCONT_REF();
Vector VMDCQ_QTY = cont_mgmt.getVMDCQ_QTY();
Vector VINTERNAL_MAP_ID = cont_mgmt.getVINTERNAL_MAP_ID();
Vector VBU_CD = cont_mgmt.getVBU_CD();
Vector VBU_PLANT_SEQ = cont_mgmt.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = cont_mgmt.getVBU_PLANT_ABBR();
Vector VTAX_DTL = cont_mgmt.getVTAX_DTL();
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

Vector VINDEX = cont_mgmt.getVINDEX();
Vector VINDEX1 = cont_mgmt.getVINDEX1();
Vector VSUB_INDEX = cont_mgmt.getVSUB_INDEX();

Vector VCONT_INTERNAL_MAPP_ID = cont_mgmt.getVCONT_INTERNAL_MAPP_ID();
Vector VCONT_TCQ = cont_mgmt.getVCONT_TCQ();
Vector VCONT_BUYER_NOM = cont_mgmt.getVCONT_BUYER_NOM();

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
Vector VTRUCK_LINKED = cont_mgmt.getVTRUCK_LINKED();
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
					    	DLNG Daily Seller Nomination
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
				<%if(VCOUNTERPARTY_PLANT_SEQ.size() > 0){ %>
				<div class="card-body cdbody">
					<%int j=0,k=0,l=0,m=0,p=0,q=0;%>
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
															<input type="radio" name="rd<%=j%>" id="rd1" onclick="updateGcvNcv('<%=j%>')" checked>&nbsp;GCV : 
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
									    					<input type="radio" name="rd<%=j%>" id="rd2" onclick="updateGcvNcv('<%=j%>')">&nbsp;NCV : 
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
										<th rowspan="2"></th>
										<th rowspan="2">Contract#<br>[Contract/Trade Ref#]</th>
										<th rowspan="2">Business Unit</th>
										<th rowspan="2">Customer</th>
										<th rowspan="2">Customer Plant</th>
										<th rowspan="2">Tax</th>
										<th colspan="2">DCQ</th>
										<th rowspan="2">Energy (MMBTU)</th>
										<th rowspan="2">Energy (SCM)</th>
										<th rowspan="2">Energy (MT)</th>
										<th rowspan="2">Rev#</th>
										<th rowspan="2">SF ID</th>
										<th rowspan="2">Gen Time</th>
										<!-- <th rowspan="2">Calorific Value Base<br>KCal/SCM</th> -->
										<th rowspan="2">Link Truck</th>
									</tr>
									<tr>
										<th>MMBTU</th>
										<th>MT</th>
									</tr>
								</thead>
								<tbody>
									<%m=0;
									if(1>0){ %>
										<%for(l=0; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
										{ 
											m+=1;
										%>
											<tr>
												<td align="center" valign="middle" <%if(VNOM_BLOCK.elementAt(l).equals("Y")) {%>style="background: #df9fbf;" title="Invoice Generated!"<%} %>>
													<input type="radio" class="form-check-input" name="chk" id="chk<%=l%>" 
													onclick="setEnableDisabled(this,'<%=l%>');calculateSCM('<%=l%>');calculateMT('<%=l%>');totalQty();"
													<%if(VNOM_BLOCK.elementAt(l).equals("Y")) {%>disabled style="pointer-events: none;"<%} %>>
													<input type="hidden" name="index1" id="index1<%=l%>" value="<%//=index1%>" disabled>
													<input type="hidden" name="index" id="index_<%=l%>" value="<%=l%>" disabled>
													<input type="hidden" name="total_rows" id="total_rows" value="<%=VCOUNTERPARTY_PLANT_SEQ.size()%>">
												</td>
												<td align="center">
													<%=VDIS_CONT_MAPPING.elementAt(l)%>
													<%if(!VCONT_REF.elementAt(l).equals("")){%>
														<br>(<%=VCONT_REF.elementAt(l)%>)
													<%} %>
												</td>
												<td align="center">
													<%=VBU_PLANT_ABBR.elementAt(l)%>
													<input type="hidden" name="bu_plant_seq" id="bu_plant_seq<%=l%>" value="<%=VBU_PLANT_SEQ.elementAt(l)%>" disabled>
												</td>
												<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(l)%>">
													<%=VCOUNTERPARTY_ABBR.elementAt(l) %>
													<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=l%>" value="<%=VCOUNTERPARTY_CD.elementAt(l)%>" disabled>
													<input type="hidden" name="agmt_no" id="agmt_no<%=l%>" value="<%=VAGMT_NO.elementAt(l)%>" disabled>
     													<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=l%>" value="<%=VAGMT_REV_NO.elementAt(l)%>" disabled>
								      				<input type="hidden" name="cont_no" id="cont_no<%=l%>" value="<%=VCONT_NO.elementAt(l)%>" disabled>
								      				<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=l%>" value="<%=VCONT_REV_NO.elementAt(l)%>" disabled>
								      				<input type="hidden" name="contract_type" id="contract_type<%=l%>" value="<%=VCONTRACT_TYPE.elementAt(l)%>" disabled>
								      				<input type="hidden" name="cargo_no" id="cargo_no<%=l%>" value="<%=VCARGO_NO.elementAt(l)%>" disabled>
								      				<input type="hidden" name="trans_cd" id="trans_cd<%=l%>" value="<%//=trans_cd%>" disabled>
								      				<input type="hidden" name="internal_map_id" id="internal_map_id<%=l%>" value="<%=VINTERNAL_MAP_ID.elementAt(l)%>">
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
												<td align="right">
													<%=VDCQ.elementAt(l)%>
													<input type="hidden" value="<%=VDCQ_MT.elementAt(l)%>" name="dcq" id="dcq<%=l%>">
													<%-- <input type="hidden" value="<%=VMDCQ_QTY.elementAt(l)%>" name="mdcq_qty" id="mdcq_qty<%=l%>"> --%>
												</td>
												<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" name="qty_mmbtu" id="qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" 
														style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" 
														<%-- onpaste="doPaste('<%=l%>');"  --%>
														onblur="negNumber(this);checkNumber1(this,9,2);checkQty('<%=l%>');checkTCQ('<%=l%>');calculateMT('<%=l%>');calculateSCM('<%=l%>');totalQty();" disabled>
														<input type="hidden" class="form-control form-control-sm" name="tmp_qty_mmbtu" id="tmp_qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" style="text-align:right" disabled>
													</div>
												</td>
												<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" name="qty_scm" id="qty_scm<%=l%>" value="<%=VQTY_SCM.elementAt(l)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
													
														<input type="hidden" class="form-control form-control-sm" name="gcv" id="gcv<%=l%>" value="<%=VGCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);calculateSCM('<%=l%>');calculateMT('<%=l%>');" disabled>
														<input type="hidden" class="form-control form-control-sm" name="ncv" id="ncv<%=l%>" value="<%=VNCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);calculateSCM('<%=l%>');calculateMT('<%=l%>');" disabled>
														<input type="hidden" name="base" id="base<%=l%>" value="<%=VBASE.elementAt(l)%>" disabled>
													</div>
												</td>
												<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" name="qty_mt" id="qty_mt<%=l%>" value="<%=VQTY_SCM.elementAt(l)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
													</div>
												</td>
												<td align="center"><%=VNOM_REV_NO.elementAt(l)%></td>
												<td align="center">
												<%-- <%if(index1==0 && !VNOM_SF_ID.elementAt(l).equals("")){ %> --%>
													<div class="row m-b-5">
														<div class="col">
															<input type="button" class="btn btn-sm config_btn" id="" title="<%//=VNOM_SF_ID.elementAt(l)%>" style="display:none; border-color: #ffb3ff; background-color: #ffb3ff;" value="SF">
														</div>																							
													</div>
												<%-- <%} %> --%>
													<input type="hidden" name="sf_id" id="sf_id<%=l%>" value="<%//=VNOM_SF_ID.elementAt(l)%>" disabled>
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
												<%-- <td align="center">
													<div style="width:300px;">
														<div class="row m-b-5">
															<div class="col">
																<input type="radio" name="rd<%=l%>" id="rd1<%=l%>" onclick="calculateSCM('<%=l%>');" <%if(VBASE.elementAt(l).equals("GCV")){ %>checked<%} %> disabled>&nbsp;GCV
										      				</div>
										      				<div class="col">
										      					<input type="text" class="form-control form-control-sm" name="gcv" id="gcv<%=l%>" value="<%=VGCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,7,2);calculateSCM('<%=l%>');" disabled>
										      				</div>
										      				<div class="col">
										      					<input type="radio" name="rd<%=l%>" id="rd2<%=l%>" onclick="calculateSCM('<%=l%>');" <%if(VBASE.elementAt(l).equals("NCV")){ %>checked<%} %> disabled>&nbsp;NCV 
										      				</div>
										      				<div class="col">
											      				<input type="text" class="form-control form-control-sm" name="ncv" id="ncv<%=l%>" value="<%=VNCV.elementAt(l)%>" style="width:75px;text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,7,2);calculateSCM('<%=l%>');" disabled>
								      						</div>
								      						<input type="hidden" name="base" id="base<%=l%>" value="<%=VBASE.elementAt(l)%>" disabled>
								      					</div>
								      				</div>
												</td> --%>
												<td valign="middle" align="center"><i id="truck_icon_<%=l%>"
												<%if(VTRUCK_LINKED.elementAt(l).equals("Y")){%>
													style="pointer-events: none;color:#00cc00" title="Linked Truck" 
												<%}else{ %>
													style="pointer-events: none;color:#997300" title="Link Truck" 				
												<%} %>
												class="fa fa-truck fa-2x fa-flip-horizontal" aria-hidden="true" 
												onclick="LinkTruck('<%=l%>','<%=VDIS_CONT_MAPPING.elementAt(l)%>','<%=gas_dt%>','<%=owner_cd%>',
												'<%=VCOUNTERPARTY_CD.elementAt(l)%>','<%=VAGMT_NO.elementAt(l)%>','<%=VCONT_NO.elementAt(l)%>',
												'<%=VCONTRACT_TYPE.elementAt(l)%>','<%=VAGMT_REV_NO.elementAt(l)%>',
												'<%=VCONT_REV_NO.elementAt(l)%>','<%=VCONT_REF.elementAt(l)%>'
												);"></i>
												</td>
											</tr>
										<%} %>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%-- <%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %> --%>
					</div>
				</div>
				<%}else{ %>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div align="center"><%=utilmsg.infoMessage("<b>No DLNG Contract is Active or Approved for the Selected Gas Day!</b>") %></div>
					</div>
				</div>
				<%} %>
			</div>
		</div>
	</div>
</div>
<%for(int i=0; i<VCOUNTERPARTY_PLANT_SEQ.size(); i++)
{
	Vector VTEMP_TRUCK_TRANS_CD = new Vector();
	Vector VTEMP_TRUCK_CD = new Vector();
	Vector VTEMP_TRUCK_REG_NUM = new Vector();
	Vector VTEMP_TRUCK_VOL_M3= new Vector();
	Vector VTEMP_TRUCK_VOL_MT = new Vector();
	Vector VTEMP_TRUCK_LOAD_CAP = new Vector();
	
	Vector VTEMP_QTY_MMBTU = new Vector();	
	Vector VTEMP_QTY_MT = new Vector();
	Vector VTEMP_FILL_STATION_CD = new Vector();
	Vector VTEMP_BAY_CD = new Vector();
	Vector VTEMP_SLOT_START_TIME = new Vector();
	Vector VTEMP_SLOT_END_TIME = new Vector();
	Vector VTEMP_ARRIVAL_DT = new Vector();
	Vector VTEMP_ARRIVAL_TIME = new Vector();
	Vector VTEMP_NEXT_AVAIL_HRS = new Vector();
	Vector VTEMP_REMARK = new Vector();
	
	VTEMP_TRUCK_TRANS_CD=(Vector) VTOTAL_TRUCK_TRANS_CD.elementAt(i);
	VTEMP_TRUCK_CD=(Vector) VTOTAL_TRUCK_CD.elementAt(i);
	VTEMP_TRUCK_REG_NUM=(Vector) VTOTAL_TRUCK_REG_NUM.elementAt(i);
	VTEMP_TRUCK_VOL_M3=(Vector) VTOTAL_TRUCK_VOL_M3.elementAt(i);
	VTEMP_TRUCK_VOL_MT=(Vector) VTOTAL_TRUCK_VOL_MT.elementAt(i);
	VTEMP_TRUCK_LOAD_CAP=(Vector) VTOTAL_TRUCK_LOAD_CAP.elementAt(i);

	VTEMP_QTY_MMBTU=(Vector) VTOTAL_QTY_MMBTU.elementAt(i);
	VTEMP_QTY_MT=(Vector) VTOTAL_QTY_MT.elementAt(i);
	VTEMP_FILL_STATION_CD=(Vector) VTOTAL_FILL_STATION_CD.elementAt(i);
	VTEMP_BAY_CD=(Vector) VTOTAL_BAY_CD.elementAt(i);
	VTEMP_SLOT_START_TIME=(Vector) VTOTAL_SLOT_START_TIME.elementAt(i);
	VTEMP_SLOT_END_TIME=(Vector) VTOTAL_SLOT_END_TIME.elementAt(i);
	VTEMP_ARRIVAL_DT=(Vector) VTOTAL_ARRIVAL_DT.elementAt(i);
	VTEMP_ARRIVAL_TIME=(Vector) VTOTAL_ARRIVAL_TIME.elementAt(i);
	VTEMP_NEXT_AVAIL_HRS=(Vector) VTOTAL_NEXT_AVAIL_HRS.elementAt(i);
	VTEMP_REMARK=(Vector) VTOTAL_REMARK.elementAt(i);
%>
<div class="modal fade" id="LinkTruckModal_<%=i %>" data-bs-backdrop="static" data-bs-keyboard="false">
	<!-- <div class="modal-dialog modal-dialog-scrollable modal-xl"> -->
	<div class="modal-dialog modal-fullscreen">
   		<div class="modal-content">
			<div class="modal-header cdheader">
	    		<div class="topheader" id="link_truck_header">
					Link Truck ()
				</div>
	    		<input type="button" class="btn-close" data-bs-dismiss="modal">
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
	      						<input type="text" class="form-control form-control-sm" name="total_qunt_mmbtu" id="total_qunt_mmbtu<%=i%>"  value=""  autocomplete="off" readonly style="text-align: right">
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
      			<div class="cdbody">
      				<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th rowspan="2">Select</th>
										<th rowspan="2">Truck#</th>
										<!-- <th rowspan="2">Truck Available From</th> -->
										<th colspan="3">Truck Capacity</th>
										<th colspan="2">Truck Nomination Qty</th>
										<th colspan="3">Filling Station Association</th>
										<th rowspan="2">Arrival</th>
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
									</tr>
								</thead>
								<tbody id="truck_list">
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
									%>
									<tr>
										<td align="center" valign="middle">
											<input type="checkbox" class="form-check-input" name="truck_chk_<%=i%>" id="truck_chk_<%=j%>_<%=i%>" 
											 onclick="setEnableDisableTruck(this,'<%=j%>','<%=i%>');"
											 <%if(!VTEMP_QTY_MMBTU.elementAt(j).equals("")){ %>checked<%} %>><%-- setQuantAccordCap(this,'<%=j%>','<%=i%>');" --%>
											<input type="hidden" name="truck_trans_cd_<%=i%>" id="truck_trans_cd_<%=j%>_<%=i%>" value="<%=VTEMP_TRUCK_TRANS_CD.elementAt(j)%>" disabled>
											<input type="hidden" name="truck_cd_<%=i%>" id="truck_cd_<%=j%>_<%=i%>" value="<%=VTEMP_TRUCK_CD.elementAt(j)%>" disabled>
											<input type="hidden" name="truck_index_<%=i%>" id="truck_index_<%=j%>_<%=i%>" value="<%=j%>" disabled>
										</td>
										<td align="center">
											<%=VTEMP_TRUCK_REG_NUM.elementAt(j) %>
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
												<input type="text" class="form-control form-control-sm" name="nom_qunt_mmbtu_<%=i%>" id="nom_qunt_mmbtu_<%=j%>_<%=i%>"  value="<%=nom_qunt_mmbtu %>"  autocomplete="off"  style="text-align: right" disabled
												onchange="negNumber(this);checkNumber1(this,9,2);calcRemainBlncQty('<%=i%>');document.getElementById('nom_qunt_mt_<%=j%>_<%=i%>').value=getMtValueOFMmbtu(this,'<%=j%>','<%=i%>');" 
												onblur="negNumber(this);checkNumber1(this,9,2);calcRemainBlncQty('<%=i%>');document.getElementById('nom_qunt_mt_<%=j%>_<%=i%>').value=getMtValueOFMmbtu(this,'<%=j%>','<%=i%>');">
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="nom_qunt_mt_<%=i%>" id="nom_qunt_mt_<%=j%>_<%=i%>"  value="<%=nom_qunt_mt %>"  autocomplete="off" readonly style="text-align: right"  disabled
												onchange="negNumber(this);checkNumber1(this,9,2);" 
												onblur="negNumber(this);checkNumber1(this,9,2);">
											</div>
										</td>
										<td align="center">
											<div style="width:200px;">
												<select class="form-select form-select-sm" name="filling_station_<%=i%>" id="filling_station<%=j%>_<%=i%>" 
												onchange="fetchBayDeatils('<%=j%>',this.value,'<%=owner_cd%>','<%=i%>');"  disabled> <!-- style="pointer-events: none;" -->
											   	 	 <option value="" selected="selected">--Select--</option>
											   	</select>
											   	<input type="hidden" name="fill_st_<%=i%>" id="fill_st_<%=j%>_<%=i%>"  value="<%=fill_st%>">
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<select class="form-select form-select-sm" name="sel_bay_<%=i%>" id="sel_bay<%=j%>_<%=i%>"
												onchange="fetchSlotDeatils('<%=j%>',this.value,'<%=owner_cd%>','<%=i%>');"  disabled> <!-- style="pointer-events: none;" -->
											   	 	 <option value="" selected="selected">--Select--</option>
											   	</select>
											   	<input type="hidden" name="get_bay_<%=i%>" id="get_bay_<%=j%>_<%=i%>"  value="<%=get_bay%>">
											</div>
										</td>
										<td align="center">
											<div style="width:200px;">
												<select class="form-select form-select-sm" name="sel_slot_<%=i%>" id="sel_slot<%=j%>_<%=i%>" 
												 onchange="setArrivalSlot('<%=j%>',this.options[this.selectedIndex].text,'<%=i%>');
												 checkSlotSelectedElse(this.value,'<%=i%>','<%=j%>','<%=VTEMP_TRUCK_CD.size()%>');"  disabled> <!-- style="pointer-events: none;" -->
											   	 	 <option value="" selected="selected" >--Select--</option>
											   	</select>
											   	<input type="hidden" name="get_slot_<%=i%>" id="get_slot_<%=j%>_<%=i%>" value="<%=get_slot_st_time%>-<%=get_slot_end_time%>">
											</div>
										</td>
										<td align="center">
											<div style="width:220px;">
												<div class="row m-b-5">
													<div class="col">
														<div class="input-group input-group-sm">
									      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="disp_arrival_dt" id="disp_arrival_dt<%=j%>_<%=i%>" value="<%=arrival_dt%>" maxLength="10" 
									      					style="background:<%//=VNOM_COLOR.elementAt(l)%>"
									      					onblur="validateDate(this);" onchange="validateDate(this);" disabled>
									      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
									      					<input type="hidden" name="arrival_dt_<%=i%>" id="arrival_dt<%=j%>_<%=i%>" value="<%=arrival_dt%>">
									      				</div>
								      				</div>
								      				<div class="col">
									      				<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm" name="arrival_time_<%=i%>" id="arrival_time<%=j%>_<%=i%>" value="<%=arrival_time%>" maxLength="5" 
								      						style="width:15px;background:<%//=VNOM_COLOR.elementAt(l)%>"
								      						onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off"  disabled>
								      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
							      						</div>
						      						</div>
						      					</div>
						      				</div>
										</td>
										<td align="center">	
											<div class="input-group input-group-sm" >
												<input type="text" class="form-control form-control-sm" name="next_avl_hrs_<%=i%>" id="next_avl_hrs<%=j%>_<%=i%>"  value="<%=next_avl_hrs%>"  maxlength="3"
												onchange="checkNextAvailHrs(this);"
												onblur="checkNextAvailHrs(this);"  disabled>
												<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
											</div>
										</td>
										<td align="center">
						      				<textarea class="form-control" name="truck_remark_<%=i%>" id="truck_remark<%=j%>_<%=i%>" cols="30" rows="1" maxlength="500"  disabled><%=truck_remark %></textarea>
										</td>
									</tr>
									<%} %>
								<%}else{ %>
								<tr>
									<td colspan="13" align="center"><%=utilmsg.infoMessage("<b>No Truck Details Available!</b>") %></td>
								</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="" align="right">
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doTruckSubmit('<%=i%>')">
				</div>
      		</div>
      	</div>
	</div>
</div>
<%} %>
<script>
function delay(ms) {
	return new Promise(resolve => setTimeout(resolve, ms));
}

async function fetchFillStDeatils(i_index, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, agmt_rev_no, cont_rev_no) {
    if ((comp_cd !== "" && comp_cd !== "0") &&
        (counterparty_cd !== "" && counterparty_cd !== "0") &&
        (agmt_no !== "") &&
        (cont_no !== "" && cont_no !== "0") &&
        (contract_type !== "")) {

        const j_index_elements = document.getElementsByName('truck_index_' + i_index);

        $.post("../servlet/DB_Dlng_ContractMgmt_Ajax" +
            "?comp_cd=" + comp_cd +
            "&counterparty_cd=" + counterparty_cd +
            "&agmt_no=" + agmt_no +
            "&cont_no=" + cont_no +
            "&contract_type=" + contract_type +
            "&agmt_rev_no=" + agmt_rev_no +
            "&cont_rev_no=" + cont_rev_no +
            "&setCallType=fetchFillStDeatils",
            async function (responseJson) {

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

                            if (fill_st !== "") {
                                const selectElement = document.getElementById('filling_station' + j + '_' + i_index);
                                if (selectElement) {
                                    selectElement.value = fill_st;

                                    fetchBayDeatils(j, fill_st, comp_cd, i_index);

                                    const get_bay = document.getElementById('get_bay_' + j + '_' + i_index)?.value;
                                    fetchSlotDeatils(j, get_bay, comp_cd, i_index);
                                } else {
                                    selectElement.value = "";
                                }
                            }

                            await delay(100);
                        }
                    }
                }
            });
    }
}
	
function LinkTruck(index,cont_mapid,gas_dt,comp_cd,counterparty_cd,agmt_no,cont_no,contract_type,agmt_rev_no,cont_rev_no,deal_ref_id)
{
	var truckModal = document.getElementById('LinkTruckModal_'+index);
	
	var qty_mmbtu = document.getElementById('qty_mmbtu'+index).value;
	var qty_scm = document.getElementById('qty_scm'+index).value;
	var qty_mt = document.getElementById('qty_mt'+index).value;
	
	if(parseInt(qty_mmbtu)>=0 && qty_mmbtu!="" && qty_mmbtu.lenght!=0)
	{
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
		     modalTitle.textContent = 'Link Truck : ' + cont_mapid + ' (' + deal_ref_id + ') [Gas Date: ' + gas_dt + ']';

		     // Hide loading indicator
		     document.getElementById("loading").style.visibility = "hidden";

		 }, 3000); // 3-second delay
		
	}
	else
	{
		var rowNo = parseInt(index)+1;
		alert("Enter Energy (MMBTU) for Row - "+rowNo+"!");
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
	var next_avl_hrs = document.getElementById("next_avl_hrs"+j_index+"_"+i_index)
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
		
		var comp_cd = document.forms[0].comp_cd.value;
		var counterparty_cd = document.getElementById("counterparty_cd"+i_index).value;
		var agmt_no = document.getElementById("agmt_no"+i_index).value;
		var cont_no = document.getElementById("cont_no"+i_index).value;
		var contract_type = document.getElementById("contract_type"+i_index).value;
		var agmt_rev_no = document.getElementById("agmt_rev_no"+i_index).value;
		var cont_rev_no = document.getElementById("cont_rev_no"+i_index).value;
		
		if(filling_station.value != "")
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

function setQuantAccordCap(obj, j_index, i_index)
{
	var nom_qunt_mmbtu_elem = document.getElementById("nom_qunt_mmbtu_" + j_index + "_" + i_index);
	var nom_qunt_mt_elem = document.getElementById("nom_qunt_mt_" + j_index + "_" + i_index);

	var total_qunt_mmbtu = document.getElementById("total_qunt_mmbtu" + i_index).value;
	var total_qunt_mt = document.getElementById("total_qunt_mt" + i_index).value;

	var roundedcapInMmbtu = document.getElementById("roundedcapInMmbtu_" + j_index + "_" + i_index).value;
	var balance_qty_elem = document.getElementById("balance_qty_" + i_index);
	var balance_qty = balance_qty_elem ? balance_qty_elem.value : 0;

	if (obj.checked)
	{
		if (parseFloat(roundedcapInMmbtu) < parseFloat(total_qunt_mmbtu)) // Capacity is less than Total Quantity
		{
			nom_qunt_mmbtu_elem.value = roundedcapInMmbtu;
			nom_qunt_mt_elem.value = getMtValueOFMmbtu(roundedcapInMmbtu, j_index, i_index);
		}
		else // Capacity is sufficient for Total Quantity
		{
			//alert(roundedcapInMmbtu +" : "+ balance_qty);
			
			if (parseFloat(roundedcapInMmbtu) > parseFloat(balance_qty))
			{
				nom_qunt_mmbtu_elem.value = total_qunt_mmbtu;
				nom_qunt_mt_elem.value = total_qunt_mt; // getMtValueOFMmbtu(total_qunt_mmbtu, j_index, i_index);
			}
			else
			{
				// If capacity is not greater than balance quantity, set it to balance quantity instead
				nom_qunt_mmbtu_elem.value = balance_qty;
				nom_qunt_mt_elem.value = getMtValueOFMmbtu(balance_qty, j_index, i_index);
			}
		}
	}
	else
	{
		nom_qunt_mmbtu_elem.value = "";
		nom_qunt_mt_elem.value = "";
	}

	calcRemainBlncQty(i_index);
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

function calcRemainBlncQty(index)
{
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index)
	//var hed_mole_qty = document.getElementById("hed_mole_qty"+index);
	
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
	
	//tot_mole_qty_mmbtu.value=round(tot_qty_mmbtu,2);
	//tot_mole_qty_scm.value=round(tot_qty_scm,2);
	
	//hed_mole_qty.innerHTML=tot_mole_qty_mmbtu.value
	
	if(qty_mmbtu.value!="")
	{
		rem_qty_mmbtu = parseFloat(qty_mmbtu.value) - tot_qty_mmbtu;
		
		balance_qty.value=round(rem_qty_mmbtu,2);
	}
}

/* function fetchFillStDeatils(i_index,comp_cd,counterparty_cd,agmt_no,cont_no,contract_type,agmt_rev_no,cont_rev_no)
{
	if((comp_cd !="" && comp_cd!="0") 
			&& (counterparty_cd != "" && counterparty_cd!="0") 
			&& (agmt_no != "")
			&& (cont_no != "" && cont_no !="0")
			&& (contract_type != ""))
	{
		var j_index = document.getElementsByName('truck_index_'+i_index);
		
		$.post("../servlet/DB_Dlng_ContractMgmt_Ajax"+ "?comp_cd="+comp_cd+"&counterparty_cd="+counterparty_cd+"&agmt_no="+agmt_no+
				"&cont_no="+cont_no+"&contract_type="+contract_type+
				"&agmt_rev_no="+agmt_rev_no+"&cont_rev_no="+cont_rev_no+
				"&setCallType=fetchFillStDeatils", function(responseJson) {
			//console.log(responseJson);
			
			$.each(responseJson, function (index, json) {
				$.each(json.FILLST_DTL, function (i, fillSt) {
					
			        var optionText =fillSt.STABBR +'-'+ fillSt.STNAME;
			        var optionValue = fillSt.STCD;
			        var option = $('<option>', {
			            value: optionValue,
			            text: optionText
			        });
			        //$('select[name="filling_station_'+i_index+'"]').append(option);
			         var select = $('select[name="filling_station_' + i_index + '"]');
					if (select.find('option[value="' + optionValue + '"]').length === 0) {
					    select.append(option);
					}
			        
			        for(var j=0 ; j<j_index.length; j++)
					{
			        	var fill_st = document.getElementById('fill_st_'+j+'_'+i_index).value;
			        	
			        	if (fill_st != "") 
			        	{
			        	    var selectElement = document.getElementById('filling_station' + j + '_' + i_index);

			        	    if (selectElement)
			        	    {
			        	        selectElement.value = fill_st;
			        	        
			        	        fetchBayDeatils(j,fill_st,comp_cd,i_index);
		        	        	
		        	        	var get_bay = document.getElementById('get_bay_' + j + '_' + i_index).value;
			        	        
			        	        fetchSlotDeatils(j, get_bay, comp_cd, i_index);
			        	    } 
			        	    else
			        	    {
			        	    	selectElement.value = "";
			        	    }
			        	}
					} 
			        //$('#filling_station'+i_index).append(option);
				});
			});
		});
	}
} */

function fetchBayDeatils(j_index,fillStCd,comp_cd,i_index)
{
	if((comp_cd !="" && comp_cd!="0"))
	{
		$.post("../servlet/DB_Dlng_ContractMgmt_Ajax"+ "?comp_cd="+comp_cd+"&fillStCd="+fillStCd+
				"&setCallType=fetchBayDeatils", function(responseJson) {
			//console.log(responseJson);
			
			$('#sel_bay'+j_index+"_"+i_index).empty().append('<option value="">--Select--</option>');
			$('#sel_slot'+j_index+"_"+i_index).empty().append('<option value="">--Select--</option>');
			
			//Clearing all previous options (Except Select)
			
			$.each(responseJson, function (index, json) {
				$.each(json.BAY_DTL, function (i, baySt) {
					
			        var optionText =baySt.BAYNAME;
			        var optionValue = baySt.BAYCD;
			        
			        var option = $('<option>', {
			            value: optionValue,
			            text: optionText
			        });
			        
			        $('#sel_bay'+j_index+'_'+i_index).append(option);
			        
			        var get_bay = document.getElementById('get_bay_'+j_index+'_'+i_index).value;
			        
			        var selectElement = document.getElementById('sel_bay' + j_index + '_' + i_index);
			        
			        if (get_bay != "") 
		        	{
		        	    if(selectElement)
		        	    {
		        	    	selectElement.value = get_bay;
		        	    }
		        	    else
				        {
				        	selectElement.value="";
				        }
		        	}
			        else
			        {
			        	selectElement.value="";
			        }
				});
			});
		});
	}
}

function checkSlotSelectedElse(objVal,i_index,j_index,no_truck)
{
	for(var j=0; j<parseInt(no_truck);j++)
	{
		var sel_slot =document.getElementById("sel_slot"+j+"_"+i_index).value;
		
		if(objVal === sel_slot && parseInt(j_index) !== parseInt(j))
		{
			alert("Slot ("+sel_slot+") is occupied!! \nSelect diffrent slot for selected truck!");
			document.getElementById("sel_slot"+j_index+"_"+i_index).value="";
			break;
		}
	}
}

//Utility to convert minutes to HH:mm format (wraps after 24 hours)
function formatTime(minutes) {
    minutes = minutes % 1440; // Normalize to within 0 to 1439
    var hours = Math.floor(minutes / 60);
    var mins = minutes % 60;
    return String(hours).padStart(2, '0') + ':' + String(mins).padStart(2, '0');
}

function fetchSlotDeatils(j_index, bayCd, comp_cd, i_index) {
	
	var gasDateStr = document.forms[0].gas_dt.value; 
	
    if (comp_cd !== "" && comp_cd !== "0") {
        $.post("../servlet/DB_Dlng_ContractMgmt_Ajax" + "?comp_cd=" + comp_cd + "&bayCd=" + bayCd + "&gasDt=" + gasDateStr +
            "&setCallType=fetchSlotDeatils", function (responseJson) {

            $('#sel_slot' + j_index + "_" + i_index).empty().append('<option value="">--Select--</option>');

            $.each(responseJson, function (index, json) {
                $.each(json.BAY_DTL, function (i, baySt) {

                	// Populate options
                    var $slotSelect = $('#sel_slot' + j_index + "_" + i_index);

                    $slotSelect.append($('<option>', {
                        value: baySt.BAYSLOTVALUE,
                        text: baySt.BAYSLOTTEXT,
                        disabled: baySt.SLOTAVAILABLE === "N",
                        style: baySt.SLOTAVAILABLE === "N" ? 'color: red;' : ''
                    }));
                    
					var get_slot = document.getElementById('get_slot_'+j_index+'_'+i_index).value;
			        
			        var selectElement = document.getElementById('sel_slot' + j_index + '_' + i_index);
			        
			        if (get_slot != "") 
		        	{
		        	    if(selectElement)
		        	    {
		        	    	selectElement.value = get_slot;
		        	    }
		        	    else
				        {
				        	selectElement.value="";
				        }
		        	}
			        else
			        {
			        	selectElement.value="";
			        } 
                });
            });
        });
    }
}

/* // Helper function to format minutes to HH:mm
function formatTime(minutes) {
    var hours = Math.floor(minutes / 60);
    var mins = minutes % 60;
    return String(hours).padStart(2, '0') + ':' + String(mins).padStart(2, '0');
} */


function setArrivalSlot(index,selSlotVal,i_index)
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

function checkNextAvailHrs(hrs)
{
	if(hrs.value != "" && hrs.length!=0)
	{
		if(parseInt(hrs.value)>360)
		{
			alert("Next Available Hrs can not be grater than 360!");
			hrs.value = "360";
		}
	}
}

</script>

<%for(int i=0; i<VCONT_INTERNAL_MAPP_ID.size(); i++){ %>
<input type="hidden" name="cont_internal_mapp_id" value="<%=VCONT_INTERNAL_MAPP_ID.elementAt(i)%>">
<input type="hidden" name="cont_buyer_nom" value="<%=VCONT_BUYER_NOM.elementAt(i)%>">
<input type="hidden" name="cont_tcq" value="<%=VCONT_TCQ.elementAt(i)%>">
<%} %>

<input type="hidden" name="option" value="SELLER_NOM">
<input type="hidden" name="comp_cd" value="<%=owner_cd%>">

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