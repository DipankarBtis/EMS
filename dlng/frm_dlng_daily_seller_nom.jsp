<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<style>
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
            qty_mmbtu.readOnly = true;
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
    				setEnableDisableTruck(truck_chk_obj,i,selectedIndex);
    			}
    		}
        }
    }
}

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
			editAllowedOnCpStatus = true;
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
	var balance_qty = document.getElementById("balance_qty_"+i_index);
	var temp_balance_qty = document.getElementById("temp_balance_qty_"+i_index)
	var total_qunt_mmbtu = document.getElementById("total_qunt_mmbtu"+i_index);

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
	
	if(parseFloat(temp_balance_qty.value) > parseFloat(total_qunt_mmbtu.value))
	{
		msg+="Total Seller Nomination Quantity should be less than or equal to Buyer Nomination Quantity!\n";
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
	
	var tcq_chk_flag = checkTCQ(i_index);
	if (!tcq_chk_flag)
	{
		flag=false;
	}	
	
	if(flag)
	{
		if(parseInt(chk_count) == 0)
		{
			var conf =confirm("No Truck/s are selected, On submission associated trucks will be released!\n\nDo you want to continue?");
		
			if(conf)
			{
				var a = confirm("Do you want to Submit Seller Nomination?");
				if(a)
				{
					document.getElementById("loading").style.visibility = "visible";
					document.forms[0].submit();
				}
			}
		}
		else
		{
			var a = confirm("Do you want to Submit Seller Nomination?");
			if(a)
			{
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].submit();
			}
		}
	}
	else if (msg != "")
	{
		alert(msg);
	}
}


function checkQty(index)
{
	var mdcq_qty = document.getElementById("mdcq_qty"+index);
	var int_map_id = document.getElementById("internal_map_id"+index); 
	
	var buyer_nom_qty = document.getElementById("buyer_nom_qty"+index);
	var index_qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	
	if(parseFloat(index_qty_mmbtu.value) > parseFloat(buyer_nom_qty.value))
	{
		alert("Seller Nomination "+parseFloat(index_qty_mmbtu.value)+" MMBTU can not exceed Buyer Nomination "+parseFloat(buyer_nom_qty.value)+" MMBTU!");
		index_qty_mmbtu.value = buyer_nom_qty.value;
		
		return;
	}
	
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
	
	var truck_chk = document.getElementsByName('truck_chk_'+index);
	
	var truck_rows = document.getElementById("truck_list_" + index).rows.length;
	
	var int_map_id = document.getElementById("internal_map_id"+index);
	//var qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	//var qty_scm = document.getElementById("qty_scm"+index);
 
	var tot_mmbtu =  parseFloat("0");
	
	var flag = true;
	
	if(truck_chk!=null && truck_chk.length!=undefined)
	{
		if(parseInt(truck_rows) > 0)
		{
			if(cont_internal_mapp_id!=null && cont_internal_mapp_id!=undefined)
			{
				if(cont_internal_mapp_id.length!=undefined)
				{
					for(var i=0; i<cont_internal_mapp_id.length; i++)
					{
						if(int_map_id.value==cont_internal_mapp_id[i].value)
						{
							var tmp_qty_mmbtu = document.getElementById("tmp_qty_mmbtu"+i).value;
							tot_mmbtu = parseFloat(cont_buyer_nom[i].value) + parseFloat(getBuyerNomContractWise(cont_internal_mapp_id[i].value,index));
							var allowedNom = parseFloat(cont_tcq[i].value) - parseFloat(cont_buyer_nom[i].value);
							
							for(var x=0; x<truck_rows; x++)
							{
								if(parseFloat(tot_mmbtu) > parseFloat(cont_tcq[i].value) && truck_chk[x].checked )
								{
									//alert("Contract TCQ : "+parseFloat(cont_tcq[i].value)+"\nTotal Seller Nom : "+parseFloat(tot_mmbtu)+"\n\nThe Seller Nomination Qty should not be > Contract TCQ!");
									alert("Contract TCQ : "+parseFloat(cont_tcq[i].value)+"\nEffective Seller Nom (Best value of Allocation,Seller Nomination) : "+parseFloat(tot_mmbtu)+"\n\nSeller Nomination Qty should not be > Contract TCQ!");
									//\n\nAllowed Nomination Quantity: "+allowedNom
									var nom_qunt_mmbtu = document.getElementById("nom_qunt_mmbtu_"+x+"_"+index)
									var nom_qunt_mt = document.getElementById("nom_qunt_mt_"+x+"_"+index)
									
									nom_qunt_mmbtu.value="";									
									calculateMT(index);
									calculateSCM(index);
									
									flag = false;
									break;
								}
							}
						}
						
						if(!flag){
							break;
						}
					}
				}
				else
				{
					if(int_map_id.value==cont_internal_mapp_id.value)
					{
						var tmp_qty_mmbtu = document.getElementById("tmp_qty_mmbtu0").value;
						tot_mmbtu = parseFloat(cont_buyer_nom.value) + parseFloat(getBuyerNomContractWise(cont_internal_mapp_id.value,index));
						var allowedNom = parseFloat(cont_tcq.value) - parseFloat(cont_buyer_nom.value);
						
						if(parseFloat(tot_mmbtu) > parseFloat(cont_tcq.value))
						{
							for(var x=0; x<truck_rows; x++)
							{
								if(parseFloat(tot_mmbtu) > parseFloat(cont_tcq.value) && truck_chk[x].checked )
								{
									//alert("Contract TCQ : "+parseFloat(cont_tcq[i].value)+"\nTotal Seller Nom : "+parseFloat(tot_mmbtu)+"\n\nThe Seller Nomination Qty should not be > Contract TCQ!");
									alert("Contract TCQ : "+parseFloat(cont_tcq.value)+"\nEffective Seller Nom (Best value of Allocation,Seller Nomination) : "+parseFloat(tot_mmbtu)+"\n\nSeller Nomination Qty should not be > Contract TCQ!");
									// \n\nAllowed Nomination Quantity: "+allowedNom
									var nom_qunt_mmbtu = document.getElementById("nom_qunt_mmbtu_"+x+"_"+index)
									var nom_qunt_mt = document.getElementById("nom_qunt_mt_"+x+"_"+index)
									
									nom_qunt_mmbtu.value=="";									
									calculateMT(index);
									calculateSCM(index);
									
									flag = false;
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	return flag;
}

function getBuyerNomContractWise(cont_internal_mapp_id,index)
{
	var nom_qunt_mmbtu = document.getElementsByName("nom_qunt_mmbtu_"+index);
	var internal_map_id = document.getElementsByName("internal_map_id");
	
	var tot_mmbtu =  parseFloat("0");
	
	if(nom_qunt_mmbtu!=null && nom_qunt_mmbtu!=undefined)
	{
		if(nom_qunt_mmbtu.length!=undefined)
		{
			for(var j=0; j<nom_qunt_mmbtu.length; j++)
			{
				if(cont_internal_mapp_id == internal_map_id[index].value)
				{
					if(trim(nom_qunt_mmbtu[j].value)!="")
					{
						tot_mmbtu = parseFloat(tot_mmbtu) + parseFloat(nom_qunt_mmbtu[j].value);
					}
				}
				
			}
		}
		else
		{
			if(cont_internal_mapp_id == internal_map_id.value)
			{
				if(trim(nom_qunt_mmbtu.value)!="")
				{
					tot_mmbtu = parseFloat(tot_mmbtu) + parseFloat(nom_qunt_mmbtu.value);
				}
			}
		}
	}
	
	return tot_mmbtu;
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
	checkTCQ(i);
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
    	td.id = "TruckCapInMmbtu_" + j + "_" + i;
    	
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
    			"negNumber(this); " +
    		    "checkNumber1(this, 9, 2); " +
    		    "calcRemainBlncQty('" + i + "'); " +
    		    "checkTCQ('" + i + "'); " +
    		    "document.getElementById('nom_qunt_mt_" + j + "_" + i + "').value = getMtValueOFMmbtu(this, '" + j + "', '" + i + "'); " +
    		    "checkTruckCapacity(this, 'TruckCapInMmbtu_" + j + "_" + i + "');"
    	);
    	
    	// Hidden input
        var hiddenInput = document.createElement("input");
        hiddenInput.type = "hidden";
        hiddenInput.name = "truck_allocated_qty_" + i;
        hiddenInput.id = "truck_allocated_qty_" + j + "_" + i;
        hiddenInput.value = "0" || "";
        td.appendChild(hiddenInput);
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
        select.setAttribute("onchange", "fetchBayDeatils('"+j_index+"', this.value, '" + comp_cd + "', " + i + ");");
    } else if (namePrefix === "sel_bay") {
        select.setAttribute("onchange", "fetchSlotDeatils('"+j_index+"', this.value, '" + comp_cd + "', " + i + ");");
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

/* function createSelectCell(value, namePrefix, j, i, text) {
    var td = document.createElement("td");
    td.align = "center";
    
    var select = document.createElement("select");
    select.className = "form-select form-select-sm";
    select.name = namePrefix + "_" + i;
    select.id = namePrefix + ""+j+"_"  + i;
    //select.disabled = true;

    var option = document.createElement("option");
    option.value = value;
    option.selected = true;
    option.textContent = text;

    select.appendChild(option);
    td.appendChild(select);
    return td;
}
 */

function createDateCell(arrivalDate, j, i)
{
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

    inputGroup.appendChild(dateInput);

    var span = document.createElement("span");
    span.className = "input-group-text";
    var icon = document.createElement("i");
    icon.className = "fa fa-calendar fa-lg";
    span.appendChild(icon);
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

function createTimeCell(arrivalTime, j, i)
{
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

    inputGroup.appendChild(timeInput);

    var span = document.createElement("span");
    span.className = "input-group-text";
    var icon = document.createElement("i");
    icon.className = "fa fa-clock-o fa-lg";
    span.appendChild(icon);
    inputGroup.appendChild(span);

    td.appendChild(inputGroup);

    return td;
}
 

/* function createDateTimeCell(arrivalDate, arrivalTime, j, i) 
{
    var td = document.createElement("td");
    td.align = "center";
    
    var div = document.createElement("div");
    div.style.width = "220px";
    
    var rowDiv = document.createElement("div");
    rowDiv.className = "row m-b-5";

    // Arrival Date
    var col1 = document.createElement("div");
    col1.className = "col";
    var inputGroup1 = document.createElement("div");
    inputGroup1.className = "input-group input-group-sm";
    
    var dateInput = document.createElement("input");
    dateInput.type = "text";
    dateInput.className = "form-control form-control-sm date fmsdtpick";
    dateInput.name = "disp_arrival_dt_" + i;
    dateInput.id = "disp_arrival_dt"+j+"_"  + i;
    dateInput.value = arrivalDate || "";
    dateInput.maxLength = "10";
    inputGroup1.appendChild(dateInput);
    var span1 = document.createElement("span");
    span1.className = "input-group-text";
    var icon1 = document.createElement("i");
    icon1.className = "fa fa-calendar fa-lg";
    span1.appendChild(icon1);
    inputGroup1.appendChild(span1);
    col1.appendChild(inputGroup1);
    dateInput.readOnly=true;
    
    var hiddenCd2 = document.createElement("input");
    hiddenCd2.type = "hidden";
    hiddenCd2.name = "arrival_dt_"+i;
    hiddenCd2.id = "arrival_dt"+j+"_"  + i;
    hiddenCd2.value = arrivalDate || "";
    td.appendChild(hiddenCd2);
    
    // Arrival Time
    var col2 = document.createElement("div");
    col2.className = "col";
    var inputGroup2 = document.createElement("div");
    inputGroup2.className = "input-group input-group-sm";
    
    var timeInput = document.createElement("input");
    timeInput.type = "text";
    timeInput.className = "form-control form-control-sm";
    timeInput.name = "arrival_time_" + i;
    timeInput.id = "arrival_time"+j+"_"  + i;
    timeInput.value = arrivalTime || "";
    timeInput.maxLength = "6";
    inputGroup2.appendChild(timeInput);
    var span2 = document.createElement("span");
    span2.className = "input-group-text";
    var icon2 = document.createElement("i");
    icon2.className = "fa fa-clock-o fa-lg";
    span2.appendChild(icon2);
    inputGroup2.appendChild(span2);
    col2.appendChild(inputGroup2);
    timeInput.readOnly=false;

    rowDiv.appendChild(col1);
    rowDiv.appendChild(col2);
    div.appendChild(rowDiv);
    td.appendChild(div);
    return td;
} */

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

function getTruckCapDtls(i_index,truck_cd,gas_dt,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, agmt_rev_no, cont_rev_no)
{
	if((truck_cd !="" && truck_cd!="0"))
	{
		$.post("../servlet/DB_Dlng_ContractMgmt_Ajax"+ "?truck_cd="+truck_cd+"&gas_date="+gas_dt+
				"&comp_cd=" + comp_cd +
		            "&counterparty_cd=" + counterparty_cd +
		            "&agmt_no=" + agmt_no +
		            "&cont_no=" + cont_no +
		            "&contract_type=" + contract_type +
		            "&agmt_rev_no=" + agmt_rev_no +
		            "&cont_rev_no=" + cont_rev_no +
				"&setCallType=fetchTruckDeatils", function(responseJson) {
			//console.log(responseJson);
			
			$.each(responseJson, function (index, json) {
				$.each(json.TRUCK_DTL, function (i, truckDtl) {
					
			        var truck_vol_m3 =truckDtl.TRUCK_VOL_M3;
			        var truck_vol_mt = truckDtl.TRUCK_VOL_MT;
			        var truck_load_cap = truckDtl.TRUCK_LOAD_CAP;
			        var truck_trans_cd = truckDtl.TRUCK_TRANS_CD;
			        var truck_reg_no = truckDtl.TRUCK_REG_NUM;
			        var truck_availAfter = truckDtl.TRUCK_AVAILAFTER;
			        
			        var m3ToMmbtuConv = document.forms[0]?.m3_to_tonMMbtu?.value || 0.3531466672;
			        var convt_mmbtu_to_mt = document.forms[0]?.convt_mmbtu_to_mt?.value || 51.5;
			        
			        //var capInMmbtu = (((truck_vol_m3) / m3ToMmbtuConv) * truck_load_cap) / 100;
			        var capInMt = (truck_vol_mt * truck_load_cap) / 100;
			        var capInM3 = (truck_vol_m3 * truck_load_cap) / 100;
			       
			        var capInMmbtu = capInMt * convt_mmbtu_to_mt; //HM20250909 : MMBTU conversion changed (Mt-> MMBTU) as per Vijay's Feedback.

			        var roundedcapInMmbtu = Math.round(capInMmbtu * 100.0) / 100.0;
			        var roundedcapInMt = Math.round(capInMt * 100.0) / 100.0;
			        var roundedcapInM3= Math.round(capInM3 * 100.0) / 100.0;
			        
			        $("#addTruckCapInM3_" + i_index).text(roundedcapInM3.toFixed(2));
					$("#addTruckCapInMt_" + i_index).text(roundedcapInMt.toFixed(2));
					$("#addTruckCapInMmbtu_" + i_index).text(roundedcapInMmbtu.toFixed(2));
					
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
	var nomQntMmbtu = document.getElementById("add_nom_qunt_mmbtu_" + i_index);
    var nomQntMt = document.getElementById("add_nom_qunt_mt_" + i_index);
 
    var fillingStation = document.getElementById("add_filling_station_" + i_index);
    var bay = document.getElementById("add_sel_bay_" + i_index);
    var slot = document.getElementById("add_sel_slot_" + i_index);
 
    var arrivalDate = document.getElementById("add_disp_arrival_dt_" + i_index);
    var arrivalTime = document.getElementById("add_arrival_time_" + i_index);
    var nextAvailableHrs = document.getElementById("add_next_avl_hrs_" + i_index);
    var truckRemark = document.getElementById("add_truck_remark_" + i_index);
 
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

function checkTruckCapacity(obj,id)
{
	var truckCapInMmbtu = document.getElementById(id)?.textContent.trim() || "";
	
	if(truckCapInMmbtu !== "" && truckCapInMmbtu !== "-")
	{
		var capValMmbtu = parseFloat(truckCapInMmbtu);
		
		if(capValMmbtu < parseFloat(obj.value))
		{
			alert("Truck Nomination Qty should be less than or equle to Truck Capacity!");
			
			obj.value ="";
		}
		else
		{
			
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_Dlng_ContractMgmt" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
NumberFormat nf = new DecimalFormat("###########0.00");

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
Vector VCOUNTERPATY_STATUS = cont_mgmt.getVCOUNTERPATY_STATUS();

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
Vector VQTY_MT = cont_mgmt.getVQTY_MT();
Vector VNOM_COLOR = cont_mgmt.getVNOM_COLOR();
Vector VTRUCK_NOM_COLOR = cont_mgmt.getVTRUCK_NOM_COLOR();
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
Vector VBUYER_NOM = cont_mgmt.getVBUYER_NOM();
Vector VBUYER_NOM_REV_NO = cont_mgmt.getVBUYER_NOM_REV_NO();
Vector VALLOCATED_MMBTU = cont_mgmt.getVALLOCATED_MMBTU();

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

Vector VMST_TRUCK_TRANS_CD = cont_mgmt.getVMST_TRUCK_TRANS_CD();
Vector VMST_TRUCK_CD = cont_mgmt.getVMST_TRUCK_CD();
Vector VMST_TRUCK_REG_NUM = cont_mgmt.getVMST_TRUCK_REG_NUM();
Vector VMST_FILLST_CD = cont_mgmt.getVMST_FILLST_CD();
Vector VMST_FILLST_NM = cont_mgmt.getVMST_FILLST_NM();
Vector VMST_FILLST_ABBR = cont_mgmt.getVMST_FILLST_ABBR();

Vector VTOTAL_QTY_MMBTU = cont_mgmt.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_MT = cont_mgmt.getVTOTAL_QTY_MT();
Vector VTOTAL_FILL_STATION_CD = cont_mgmt.getVTOTAL_FILL_STATION_CD();
Vector VTOTAL_BAY_CD = cont_mgmt.getVTOTAL_BAY_CD();
Vector VTOTAL_SLOT_START_TIME = cont_mgmt.getVTOTAL_SLOT_START_TIME();
Vector VTOTAL_SLOT_END_TIME = cont_mgmt.getVTOTAL_SLOT_END_TIME();
Vector VTOTAL_ARRIVAL_DT = cont_mgmt.getVTOTAL_ARRIVAL_DT();
Vector VTOTAL_NOM_BLOCK = cont_mgmt.getVTOTAL_NOM_BLOCK();
Vector VTOTAL_ARRIVAL_TIME = cont_mgmt.getVTOTAL_ARRIVAL_TIME();
Vector VTOTAL_NEXT_AVAIL_HRS = cont_mgmt.getVTOTAL_NEXT_AVAIL_HRS();
Vector VTOTAL_REMARK = cont_mgmt.getVTOTAL_REMARK();
Vector VTOTAL_AVAIL_DT = cont_mgmt.getVTOTAL_AVAIL_DT();
Vector VTRUCK_LINKED = cont_mgmt.getVTRUCK_LINKED();
Vector VTOTAL_TRUCK_ALLOCATED_QTY = cont_mgmt.getVTOTAL_TRUCK_ALLOCATED_QTY();

Vector MSG_TRUCK_REG_NO = new Vector();
Vector MSG_QTY = new Vector();
Vector MSG_ALLOW_CREDIT = new Vector();
Vector MSG_CONSUMED_AMT = new Vector();
Vector MSG_BALANCE = new Vector();
Vector MSG_SUBMITTED = new Vector();
Vector MSG_DEAL_NO = new Vector();

if(session.getAttribute("MSG_TRUCK_REG_NO")==null||session.getAttribute("MSG_TRUCK_REG_NO")==""||session.getAttribute("MSG_TRUCK_REG_NO").toString().equals("null"))
{
	
}
else
{
	MSG_TRUCK_REG_NO=(Vector) session.getAttribute("MSG_TRUCK_REG_NO");
	MSG_DEAL_NO=(Vector) session.getAttribute("MSG_DEAL_NO");
	MSG_QTY=(Vector) session.getAttribute("MSG_QTY");
	MSG_ALLOW_CREDIT=(Vector) session.getAttribute("MSG_ALLOW_CREDIT");
	MSG_CONSUMED_AMT=(Vector) session.getAttribute("MSG_CONSUMED_AMT");
	MSG_BALANCE=(Vector) session.getAttribute("MSG_BALANCE");
	MSG_SUBMITTED=(Vector) session.getAttribute("MSG_SUBMITTED");
}
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
			
			<%if(MSG_TRUCK_REG_NO.size() > 0) {%>
			<div class="row m-b-5">
				<div class="alert alert-secondary alert-dismissible fade show" role="alert">
				  	<%for(int i=0 ;i<MSG_TRUCK_REG_NO.size(); i++) {%>
				  		<%if(MSG_SUBMITTED.elementAt(i).toString().equals("E")) {%>
				  			<%=utilmsg.errorMessage("Contract# : "+MSG_DEAL_NO.elementAt(i)+", Truck : "+MSG_TRUCK_REG_NO.elementAt(i)+", Qty : "+MSG_QTY.elementAt(i)+", Allowable Credit : "+MSG_ALLOW_CREDIT.elementAt(i)+", Required Amount : "+MSG_CONSUMED_AMT.elementAt(i)+", Deficit Amount "+MSG_BALANCE.elementAt(i))%>
				  		<%}else{ %>
							<%=utilmsg.successMessage("Contract# : "+MSG_DEAL_NO.elementAt(i)+", Truck : "+MSG_TRUCK_REG_NO.elementAt(i)+", Qty : "+MSG_QTY.elementAt(i)+", Allowable Credit : "+MSG_ALLOW_CREDIT.elementAt(i)+", Consumed Amount : "+MSG_CONSUMED_AMT.elementAt(i)+", Balance Amount "+MSG_BALANCE.elementAt(i))%>
						<%} %>
					<%} %>
				  	<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" onclick="resetSessionData();"></button>
				</div>
			</div>
			<%} %>
			
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
							<table class="table table-bordered table-hover"  id="filterbysearch">
								<thead>
									<tr style="background:#bce6ff;color:#0c63e4;">
										<th colspan="17">
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
										<th rowspan="2">Customer<div align="center"><input class="form-control form-control-sm" type="text" id="countParty" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Customer Plant<div align="center"><input class="form-control form-control-sm" type="text" id="cpPlant" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Contract#<br>[Contract/Trade Ref#]<div align="center"><input class="form-control form-control-sm" type="text" id="contractNum" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Business Unit<div align="center"><input class="form-control form-control-sm" type="text" id="buPlant" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Tax</th>
										<th colspan="2">DCQ</th>
										<th rowspan="2" style="background: #000066; color: white;">Supplied Qty</th>
										<th rowspan="3">Buyer Nomination (Rev)<br>(MMBTU)</th>
										<th colspan="3">Scheduled Qty</th>
										<th rowspan="2">Link Truck</th>
										<th rowspan="2">Rev#</th>
										<th rowspan="2">SF ID</th>
										<th rowspan="2">Gen Time</th>
										<!-- <th rowspan="2">Calorific Value Base<br>KCal/SCM</th> -->
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
									<%m=0;
									if(1>0){ %>
										<%for(l=0; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
										{ 
											m+=1;
										%>
											<tr>
												<td align="center" valign="middle"
												<%
												Vector VTEMP_TOTAL_NOM_BLOCK = new Vector();
												VTEMP_TOTAL_NOM_BLOCK=(Vector) VTOTAL_NOM_BLOCK.elementAt(l);
												if(VTEMP_TOTAL_NOM_BLOCK.contains("Y")&& (VCONTRACT_TYPE.elementAt(l).equals("O") || VCONTRACT_TYPE.elementAt(l).equals("Q"))) {%>style="background: #df9fbf;" title="Invoice Generated!"<%} %>
												>
													<input type="radio" class="form-check-input" name="chk" id="chk<%=l%>" 
													onclick="setEnableDisabled(this,'<%=l%>');calculateSCM('<%=l%>');calculateMT('<%=l%>');totalQty();">
													<input type="hidden" name="index1" id="index1<%=l%>" value="<%//=index1%>" disabled>
													<input type="hidden" name="index" id="index_<%=l%>" value="<%=l%>" disabled>
													<input type="hidden" name="total_rows" id="total_rows" value="<%=VCOUNTERPARTY_PLANT_SEQ.size()%>">
												</td>
												<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(l)%>">
													<%=VCOUNTERPARTY_ABBR.elementAt(l) %>
													<span 
													<%if(VCOUNTERPATY_STATUS.elementAt(l).equals("N")){ %>class='alert alert-danger' title="Counterparty Deactive "
													<%}else if(VCOUNTERPATY_STATUS.elementAt(l).equals("E")){ %>class='alert alert-warning'
													<%} %>
													><b> <%if(VCOUNTERPATY_STATUS.elementAt(l).equals("N")){ %> De-active 
													<%}else if(VCOUNTERPATY_STATUS.elementAt(l).equals("E")){ %> E-Rate
													<%} %> </b>
													</span>
													<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=l%>" value="<%=VCOUNTERPARTY_CD.elementAt(l)%>" disabled>
													<input type="hidden" name="agmt_no" id="agmt_no<%=l%>" value="<%=VAGMT_NO.elementAt(l)%>" disabled>
     													<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=l%>" value="<%=VAGMT_REV_NO.elementAt(l)%>" disabled>
								      				<input type="hidden" name="cont_no" id="cont_no<%=l%>" value="<%=VCONT_NO.elementAt(l)%>" disabled>
								      				<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=l%>" value="<%=VCONT_REV_NO.elementAt(l)%>" disabled>
								      				<input type="hidden" name="contract_type" id="contract_type<%=l%>" value="<%=VCONTRACT_TYPE.elementAt(l)%>" disabled>
								      				<input type="hidden" name="cargo_no" id="cargo_no<%=l%>" value="<%=VCARGO_NO.elementAt(l)%>" disabled>
								      				<input type="hidden" name="trans_cd" id="trans_cd<%=l%>" value="<%//=trans_cd%>" disabled>
								      				<input type="hidden" name="internal_map_id" id="internal_map_id<%=l%>" value="<%=VINTERNAL_MAP_ID.elementAt(l)%>">
													<input type="hidden" name="cp_status" id="cp_status<%=l%>" value="<%=VCOUNTERPATY_STATUS.elementAt(l)%>">
												</td>
												<td align="center">
													<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%>
													<%if(!VCUSTOMER_CODE.elementAt(l).equals("")){%>
													<br><font style="background:#ADD8E6;"><%=VCUSTOMER_CODE.elementAt(l)%></font>
													<%} %>
													<input type="hidden" name="plant_seq" id="plant_seq<%=l%>" value="<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(l)%>" disabled>
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
													<input type="hidden" value="<%=VDCQ_MT.elementAt(l)%>" name="dcq_mt" id="dcq_mt<%=l%>">
													<%-- <input type="hidden" value="<%=VMDCQ_QTY.elementAt(l)%>" name="mdcq_qty" id="mdcq_qty<%=l%>"> --%>
												</td>
												<td align="right" style="background: #b3f0ff;">
													<%=VALLOCATED_MMBTU.elementAt(l)%>
												</td>
												<td align="right">
													<%=VBUYER_NOM.elementAt(l)%> (<%=VBUYER_NOM_REV_NO.elementAt(l)%>)
													<input type="hidden" name="buyer_nom_qty" id="buyer_nom_qty<%=l%>" value="<%=VBUYER_NOM.elementAt(l)%>">
												</td>
												<td align="center">
													<div style="width:100px;">
														<input type="text" class="form-control form-control-sm" name="qty_mmbtu" id="qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" 
														style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" 
														<%-- onpaste="doPaste('<%=l%>');"  --%>
														onblur="negNumber(this);checkNumber1(this,9,2);checkQty('<%=l%>');calculateMT('<%=l%>');calculateSCM('<%=l%>');totalQty();" disabled>
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
														<input type="text" class="form-control form-control-sm" name="qty_mt" id="qty_mt<%=l%>" value="<%=VQTY_MT.elementAt(l)%>" style="text-align:right" onblur="checkNumber1(this,11,2);" readonly disabled>
													</div>
												</td>
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
												'<%=VCONT_REV_NO.elementAt(l)%>','<%=VCONT_REF.elementAt(l)%>','<%=VCOUNTERPARTY_ABBR.elementAt(l)%>',
												'<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(l)%>','<%=VBU_PLANT_SEQ.elementAt(l)%>');"></i>
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
						<div align="center"><%=utilmsg.infoMessage("<b>No Buyer Nomination Done for the Selected Gas Day!</b>") %></div>
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
	
	Vector VTEMP_MST_TRUCK_TRANS_CD = new Vector();
	Vector VTEMP_MST_TRUCK_CD = new Vector();
	Vector VTEMP_MST_TRUCK_REG_NUM = new Vector();
	Vector VTEMP_MST_FILLST_CD = new Vector();
	Vector VTEMP_MST_FILLST_NM = new Vector();
	Vector VTEMP_MST_FILLST_ABBR = new Vector();
	
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
	Vector VTEMP_AVAIL_DT= new Vector();
	Vector VTEMP_TRUCK_ALLOCATED_QTY= new Vector();
	Vector VTEMP_NOM_BLOCK = new Vector();
	Vector VTEMP_TRUCK_NOM_COLOR = new Vector();
	
	VTEMP_TRUCK_TRANS_CD=(Vector) VTOTAL_TRUCK_TRANS_CD.elementAt(i);
	VTEMP_TRUCK_CD=(Vector) VTOTAL_TRUCK_CD.elementAt(i);
	VTEMP_TRUCK_REG_NUM=(Vector) VTOTAL_TRUCK_REG_NUM.elementAt(i);
	VTEMP_TRUCK_VOL_M3=(Vector) VTOTAL_TRUCK_VOL_M3.elementAt(i);
	VTEMP_TRUCK_VOL_MT=(Vector) VTOTAL_TRUCK_VOL_MT.elementAt(i);
	VTEMP_TRUCK_LOAD_CAP=(Vector) VTOTAL_TRUCK_LOAD_CAP.elementAt(i);
	
	VTEMP_MST_TRUCK_TRANS_CD=(Vector) VMST_TRUCK_TRANS_CD.elementAt(i);
	VTEMP_MST_TRUCK_CD=(Vector) VMST_TRUCK_CD.elementAt(i);
	VTEMP_MST_TRUCK_REG_NUM=(Vector) VMST_TRUCK_REG_NUM.elementAt(i);
	VTEMP_MST_FILLST_CD=(Vector) VMST_FILLST_CD.elementAt(i);
	VTEMP_MST_FILLST_NM=(Vector) VMST_FILLST_NM.elementAt(i);
	VTEMP_MST_FILLST_ABBR=(Vector) VMST_FILLST_ABBR.elementAt(i);
	
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
	VTEMP_AVAIL_DT=(Vector) VTOTAL_AVAIL_DT.elementAt(i);
	VTEMP_TRUCK_ALLOCATED_QTY=(Vector) VTOTAL_TRUCK_ALLOCATED_QTY.elementAt(i);
	VTEMP_NOM_BLOCK=(Vector) VTOTAL_NOM_BLOCK.elementAt(i);
	VTEMP_TRUCK_NOM_COLOR=(Vector) VTRUCK_NOM_COLOR.elementAt(i);
%>
<div class="modal fade" id="LinkTruckModal_<%=i %>" data-bs-backdrop="static" data-bs-keyboard="false" 
<%if(VCOUNTERPATY_STATUS.elementAt(i).equals("N")) {%>style="pointer-events: none;"<%} %>>
	<div class="modal-dialog modal-fullscreen">
   		<div class="modal-content">
			<div class="modal-header cdheader">
	    		<div class="topheader" id="link_truck_header">
					Link Truck ()
				</div>
	    		<input type="button" class="btn-close" data-bs-dismiss="modal"  onclick="resetTruckToTable('<%=i%>');">
	  		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Buyer Nomination Quantity</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm" name="total_qunt_mmbtu" id="total_qunt_mmbtu<%=i%>"  value=""  autocomplete="off" readonly style="text-align: right">
	      						<span class="input-group-text"><b>MMBTU</b></span>
	      						<input type="hidden" class="form-control form-control-sm" name="total_qunt_m3" id="total_qunt_m3<%=i%>"  value=""  autocomplete="off" readonly style="text-align: right">
	      						<!-- <span class="input-group-text"><b>M3</b></span> -->
	      						<input type="hidden" class="form-control form-control-sm" name="total_qunt_mt" id="total_qunt_mt<%=i%>"  value=""  autocomplete="off" readonly style="text-align: right">
	      						<!-- <span class="input-group-text"><b>MT</b></span> -->
      						</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Total Seller Nomination Quantity</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="col-auto">
			    				<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm" name="balance_qty" id="balance_qty_<%=i%>" value="" 
		      						autocomplete="off" readonly style="text-align: right">
		      						<span class="input-group-text"><b>MMBTU</b></span>
		      						<input type="hidden" name="temp_balance_qty" id="temp_balance_qty_<%=i%>">
	      						</div>
			    			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
				    			<div id="creditmsg_<%=i%>" align="center"></div>
				  			</div>
						</div>
					</div>
      			</div>
      			<div class="cdbody">
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
											<div style="width:200px;">
												<select class="form-select form-select-sm searchable-select" name="sel_truck_<%=i%>" id="sel_truck_<%=i%>" 
												onchange="getTruckCapDtls('<%=i%>',this.value,'<%=gas_dt%>','<%=owner_cd%>',
												'<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
												'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>',
												'<%=VCONT_REV_NO.elementAt(i)%>');">
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
												onchange="negNumber(this);checkNumber1(this,9,2);document.getElementById('add_nom_qunt_mt_<%=i%>').value=getMtValueOFMmbtu(this,'','<%=i%>');checkTruckCapacity(this,'addTruckCapInMmbtu_<%=i%>');" 
												onblur="negNumber(this);checkNumber1(this,9,2);document.getElementById('add_nom_qunt_mt_<%=i%>').value=getMtValueOFMmbtu(this,'','<%=i%>');checkTruckCapacity(this,'addTruckCapInMmbtu_<%=i%>');">
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
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" id="resetTruckBtn_<%=i%>" onclick="resetTruckToTable('<%=i%>')">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Add" id="addTruckBtn_<%=i%>" onclick="addTruckToTable('<%=i%>')" 
						<%if(VTEMP_NOM_BLOCK.contains("Y") && (VCONTRACT_TYPE.elementAt(i).equals("O") || VCONTRACT_TYPE.elementAt(i).equals("Q"))){ %>disabled<%} %> 
						<%if(VCOUNTERPATY_STATUS.elementAt(i).equals("N")) {%>disabled style="pointer-events: none;"<%} %>>
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Add" disabled>
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
										Double mtToMmbtuConv = 51.5;
										//Double capInMmbtu = (((Double.parseDouble(""+VTEMP_TRUCK_VOL_M3.elementAt(j))/m3ToMmbtuConv)*Double.parseDouble(""+VTEMP_TRUCK_LOAD_CAP.elementAt(j)))/100); 
										Double capInMmbtu = capInMT * mtToMmbtuConv;  //HM20250909 : MMBTU conversion changed (Mt-> MMBTU) as per Vijay's Feedback.
										
										double doublecapInMmbtu = Math.round(capInMmbtu * 100.0) / 100.0;
										String roundedcapInMmbtu = nf.format(doublecapInMmbtu);
										
										String nom_qunt_mmbtu = (""+VTEMP_QTY_MMBTU.elementAt(j));
										String nom_qunt_mt = (""+VTEMP_QTY_MT.elementAt(j));
										String truck_allocated_qty = (""+VTEMP_TRUCK_ALLOCATED_QTY.elementAt(j));
										
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
										<%if(VTEMP_NOM_BLOCK.elementAt(j).equals("A")) {%>style="background: #ffe100; pointer-events: auto" title="Allocated : <%=truck_allocated_qty%>"<%} %>
										<%if(VTEMP_NOM_BLOCK.elementAt(j).equals("Y")) {%>style="background: #df9fbf; pointer-events: auto" title="Invoice Generated!"<%} %>>
											<input type="checkbox" class="form-check-input" name="truck_chk_<%=i%>" id="truck_chk_<%=j%>_<%=i%>" 
											 onclick="setEnableDisableTruck(this,'<%=j%>','<%=i%>');"
											 <%if(VTEMP_NOM_BLOCK.elementAt(j).equals("Y") || VTEMP_NOM_BLOCK.elementAt(j).equals("A")) {%>disabled style="pointer-events: none;" checked<%}else{ %>
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
										<td align="center" id="TruckCapInMmbtu_<%=j%>_<%=i %>">
											<%=roundedcapInMmbtu %>
											<input type="hidden" name="roundedcapInMmbtu" id="roundedcapInMmbtu_<%=j%>_<%=i%>" value="<%=roundedcapInMmbtu %>">
										</td>
										<td align="center">
											<div  style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="nom_qunt_mmbtu_<%=i%>" id="nom_qunt_mmbtu_<%=j%>_<%=i%>"  value="<%=nom_qunt_mmbtu %>"  autocomplete="off"  
												style="text-align: right;background:<%=VTEMP_TRUCK_NOM_COLOR.elementAt(j)%>" disabled
												onchange="negNumber(this);checkNumber1(this,9,2);calcRemainBlncQty('<%=i%>');checkTCQ('<%=i %>');document.getElementById('nom_qunt_mt_<%=j%>_<%=i%>').value=getMtValueOFMmbtu(this,'<%=j%>','<%=i%>');checkTruckCapacity(this,'TruckCapInMmbtu_<%=j%>_<%=i%>');" 
												onblur="negNumber(this);checkNumber1(this,9,2);calcRemainBlncQty('<%=i%>');document.getElementById('nom_qunt_mt_<%=j%>_<%=i%>').value=getMtValueOFMmbtu(this,'<%=j%>','<%=i%>');checkTruckCapacity(this,'TruckCapInMmbtu_<%=j%>_<%=i%>');">
												<input type="hidden" name ="truck_allocated_qty_<%=i%>" id="truck_allocated_qty_<%=j%>_<%=i%>" value="<%=truck_allocated_qty%>">
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="nom_qunt_mt_<%=i%>" id="nom_qunt_mt_<%=j%>_<%=i%>"  value="<%=nom_qunt_mt %>"  autocomplete="off" readonly 
												style="text-align: right;background:<%=VTEMP_TRUCK_NOM_COLOR.elementAt(j)%>"  disabled
												onchange="negNumber(this);checkNumber1(this,9,2);" 
												onblur="negNumber(this);checkNumber1(this,9,2);">
											</div>
										</td>
										<td align="center">
											<div style="width:200px;">
												<select class="form-select form-select-sm" name="filling_station_<%=i%>" id="filling_station<%=j%>_<%=i%>" 
												onchange="fetchBayDeatils('<%=j%>',this.value,'<%=owner_cd%>','<%=i%>');"  disabled
												style="background:<%=VTEMP_TRUCK_NOM_COLOR.elementAt(j)%>"> <!-- style="pointer-events: none;" -->
											   	 	 <option value="" selected="selected">--Select--</option>
											   	</select>
											   	<input type="hidden" name="fill_st_<%=i%>" id="fill_st_<%=j%>_<%=i%>"  value="<%=fill_st%>">
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<select class="form-select form-select-sm" name="sel_bay_<%=i%>" id="sel_bay<%=j%>_<%=i%>"
												onchange="fetchSlotDeatils('<%=j%>',this.value,'<%=owner_cd%>','<%=i%>');"  disabled 
												style="background:<%=VTEMP_TRUCK_NOM_COLOR.elementAt(j)%>"> <!-- style="pointer-events: none;" -->
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
												 style="background:<%=VTEMP_TRUCK_NOM_COLOR.elementAt(j)%>"> <!-- style="pointer-events: none;" -->
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
									      					style="background:<%=VTEMP_TRUCK_NOM_COLOR.elementAt(j)%>"
									      					onblur="validateDate(this);" onchange="validateDate(this);" disabled >
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
								      						style="width:15px;background:<%=VTEMP_TRUCK_NOM_COLOR.elementAt(j)%>"
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
												style="background:<%=VTEMP_TRUCK_NOM_COLOR.elementAt(j)%>">
												<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
											</div>
										</td>
										<td align="center">
						      				<textarea class="form-control" name="truck_remark_<%=i%>" id="truck_remark<%=j%>_<%=i%>" cols="30" rows="1" maxlength="500"  disabled
						      				style="background:<%=VTEMP_TRUCK_NOM_COLOR.elementAt(j)%>"><%=truck_remark %></textarea>
										</td>
									</tr>
									<%} %>
								<%} %>
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
      		<div class="modal-footer cdfooter">
        		<div class="" align="right">
					<input 
					<%if(VTEMP_NOM_BLOCK.contains("Y") && (VCONTRACT_TYPE.elementAt(i).equals("O") || VCONTRACT_TYPE.elementAt(i).equals("Q"))){ %>disabled<%} %> 
					<%if(VCOUNTERPATY_STATUS.elementAt(i).equals("N")) {%>disabled style="pointer-events: none;"<%} %>
					type="button" class="btn btn-warning com-btn" value="Submit" onclick="doTruckSubmit('<%=i%>')">
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

	
function LinkTruck(index,cont_mapid,gas_dt,comp_cd,counterparty_cd,agmt_no,cont_no,contract_type,agmt_rev_no,cont_rev_no,deal_ref_id,cp_abbr,plant_seq,bu_unit)
{
	var truckModal = document.getElementById('LinkTruckModal_'+index);
	
	var buyer_nom_qty = document.getElementById('buyer_nom_qty'+index).value;
	//var qty_mmbtu = document.getElementById('qty_mmbtu'+index).value;
	//var qty_scm = document.getElementById('qty_scm'+index).value;
	//var qty_mt = document.getElementById('qty_mt'+index).value;
	
	if(parseInt(buyer_nom_qty)>=0 && buyer_nom_qty!="" && buyer_nom_qty.lenght!=0)
	{
		var mmbtuToM3 =  document.forms[0].mmbtu_to_m3.value;
		var qty_m3 = round(parseFloat(buyer_nom_qty)/parseFloat(mmbtuToM3),2);
		
		document.getElementById('total_qunt_mmbtu'+index).value=buyer_nom_qty;
		//document.getElementById('total_qunt_m3'+index).value=qty_m3;
		//document.getElementById('total_qunt_mt'+index).value=qty_mt;

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
		     //modalTitle.textContent = 'Link Truck : '+cp_abbr+' - '+ cont_mapid + ' (' + deal_ref_id + ') [Gas Date: ' + gas_dt + ']';
		     modalTitle.textContent = 'Link Truck [Gas Date: ' + gas_dt + '] : '+cp_abbr+' - '+ cont_mapid + ' (' + deal_ref_id + ')';
		     // Hide loading indicator
		     document.getElementById("loading").style.visibility = "hidden";

		 }, 3000); // 3-second delay
		 
		 checkAllowableCredit(index,buyer_nom_qty,counterparty_cd,agmt_no,cont_no,contract_type,plant_seq,bu_unit);
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
	var truck_allocated_qty = document.getElementById("truck_allocated_qty_"+j_index+"_"+i_index)
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
		truck_allocated_qty.disabled=false;
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
		truck_allocated_qty.disabled=true;
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
	//checkTCQ(i_index);
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
	checkTCQ(i_index);
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
	var temp_balance_qty = document.getElementById("temp_balance_qty_"+index)
	
	var tot_qty_mmbtu=parseFloat("0");
	var tot_qty_mt=parseFloat("0");
	var rem_qty_mmbtu=parseFloat("0");
	
	for(var i=0;i<parseInt(truck_chk);i++)
	{
		var truck_chk_obj = document.getElementById("truck_chk_"+i+"_"+index);
		
		if(truck_chk_obj.checked)
		{
			var nom_qunt_mmbtu_ = document.getElementById("nom_qunt_mmbtu_"+i+"_"+index);
			var truck_allocated_qty = document.getElementById("truck_allocated_qty_"+i+"_"+index);
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
		//rem_qty_mmbtu = parseFloat(qty_mmbtu.value) - tot_qty_mmbtu;
		rem_qty_mmbtu = tot_qty_mmbtu;
		
		balance_qty.value=round(rem_qty_mmbtu,2);
	}
	
	//calcAllocRemainBlncQty(index);
}

function calcAllocRemainBlncQty(index)
{
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index)
	//var hed_mole_qty = document.getElementById("hed_mole_qty"+index);
	
	var truck_chk = document.getElementsByName("truck_chk_"+index).length;
	
	var total_qunt_mmbtu = document.getElementById("total_qunt_mmbtu"+index)
	var total_qunt_mt = document.getElementById("total_qunt_mt"+index)
	var balance_qty = document.getElementById("temp_balance_qty_"+index)
	
	var tot_qty_mmbtu=parseFloat("0");
	var tot_qty_mt=parseFloat("0");
	var rem_qty_mmbtu=parseFloat("0");
	
	for(var i=0;i<parseInt(truck_chk);i++)
	{
		var truck_chk_obj = document.getElementById("truck_chk_"+i+"_"+index);
		
		if(truck_chk_obj.checked)
		{
			var nom_qunt_mmbtu_ = document.getElementById("nom_qunt_mmbtu_"+i+"_"+index);
			var truck_allocated_qty = document.getElementById("truck_allocated_qty_"+i+"_"+index);
			var nom_qunt_mt = document.getElementById("nom_qunt_mt_"+i+"_"+index);
			
			if(nom_qunt_mmbtu_.value != "")
			{				 
				if(truck_allocated_qty.value != "" && parseFloat(truck_allocated_qty.value)!= "0")
				{
					tot_qty_mmbtu=tot_qty_mmbtu + parseFloat(truck_allocated_qty.value);
				}
				else
				{
					tot_qty_mmbtu=tot_qty_mmbtu + parseFloat(nom_qunt_mmbtu_.value);
				}
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
		//rem_qty_mmbtu = parseFloat(qty_mmbtu.value) - tot_qty_mmbtu;
		rem_qty_mmbtu = tot_qty_mmbtu;
		
		balance_qty.value=round(rem_qty_mmbtu,2);
	}
}

function calcAllocRemainBlncQty_Bkp(index)
{
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index)
	//var hed_mole_qty = document.getElementById("hed_mole_qty"+index);
	
	var truck_chk = document.getElementsByName("truck_chk_"+index).length;
	
	var total_qunt_mmbtu = document.getElementById("total_qunt_mmbtu"+index)
	var total_qunt_mt = document.getElementById("total_qunt_mt"+index)
	var balance_qty = document.getElementById("temp_balance_qty_"+index)
	
	var tot_qty_mmbtu=parseFloat("0");
	var tot_qty_mt=parseFloat("0");
	var rem_qty_mmbtu=parseFloat("0");
	
	for(var i=0;i<parseInt(truck_chk);i++)
	{
		var truck_chk_obj = document.getElementById("truck_chk_"+i+"_"+index);
		
		if(truck_chk_obj.checked)
		{
			var nom_qunt_mmbtu_ = document.getElementById("nom_qunt_mmbtu_"+i+"_"+index);
			var truck_allocated_qty = document.getElementById("truck_allocated_qty_"+i+"_"+index);
			var nom_qunt_mt = document.getElementById("nom_qunt_mt_"+i+"_"+index);
			
			if(nom_qunt_mmbtu_.value != "")
			{				 
				if(truck_allocated_qty.value != "" && parseFloat(truck_allocated_qty.value)!= "0")
				{
					tot_qty_mmbtu=tot_qty_mmbtu + parseFloat(truck_allocated_qty.value);
				}
				else
				{
					tot_qty_mmbtu=tot_qty_mmbtu + parseFloat(nom_qunt_mmbtu_.value);
				}
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
		//rem_qty_mmbtu = parseFloat(qty_mmbtu.value) - tot_qty_mmbtu;
		rem_qty_mmbtu = tot_qty_mmbtu;
		
		balance_qty.value=round(rem_qty_mmbtu,2);
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

        //if(availAfter == "")
    	{
        	var no_truck = document.getElementById("truck_list_" + i_index).rows.length;	
        	
        	var latestDate = null;
        	for (var j = 0; j < parseInt(no_truck); j++) 
        	{
        	    var get_truck_reg_no = document.getElementById("truck_reg_no_" + j + "_" + i_index).value;

        	    if(truck_reg_no == get_truck_reg_no)
        	    {
        	    	var arrival_dt = document.getElementById("arrival_dt" + j + "_" + i_index).value;
             	    var arrival_time = document.getElementById("arrival_time" + j + "_" + i_index).value;
             	   
             	    var slotStartTime = "";        
    	    		var slotEndTime = "";           
    	    		var slotEndDate = "";
             	   
             	    if(j_index=="")
             	    {
             	    	var get_slot = document.getElementById("sel_slot" + j + "_" + i_index);
             	    	var get_slot_text = get_slot.options[get_slot.selectedIndex].text;
               	    
               	  
             	    	if (get_slot && get_slot.selectedIndex !== -1) 
             	    	{
             	    		var get_slot_text = get_slot.options[get_slot.selectedIndex].text;
             			  
             	    		// Extract start time, end time, and date using split
             	    		var timeParts = get_slot_text.split(" ");
             	    		slotStartTime = timeParts[0];         
             	    		slotEndTime = timeParts[2];           
             	    		slotEndDate = get_slot_text.match(/\((\d{2}\/\d{2}\/\d{4})\)/)[1];
               		   
             	    		// Check if end time is past midnight
             	    		var startHour = parseInt(slotStartTime.split(":")[0]);
             	    		var endHour = parseInt(slotEndTime.split(":")[0]);
             	    		
             	    		if (endHour < startHour) {
             	    			// Add one day to slotEndDate
             	    			var dateParts = slotEndDate.split("/"); // [dd, mm, yyyy]
             	    			var dateObj = new Date(
             	    					parseInt(dateParts[2]),      // year
             	    					parseInt(dateParts[1]) - 1,  // month (0-based)
             	    					parseInt(dateParts[0])       // day
             	    			);

             	    			dateObj.setDate(dateObj.getDate() + 1);

             	    			// Format back to DD/MM/YYYY
             	    			slotEndDate =
             	    				("0" + dateObj.getDate()).slice(-2) + "/" +
             	    				("0" + (dateObj.getMonth() + 1)).slice(-2) + "/" +
             	    				dateObj.getFullYear();
             	    		}
             	    	}
             	    }
             	    else
             	    {
             	    	
             	    	if(parseInt(j) != parseInt(j_index))
             	    	{
             	    		var get_slot = document.getElementById("sel_slot" + j + "_" + i_index);
             	    		var get_slot_text = get_slot.options[get_slot.selectedIndex].text;
                   	    
             	    		
                   	  
             	    		if (get_slot && get_slot.selectedIndex !== -1) 
             	    		{
             	    			var get_slot_text = get_slot.options[get_slot.selectedIndex].text;
                 			  
             	    			// Extract start time, end time, and date using split
             	    			var timeParts = get_slot_text.split(" ");
             	    			slotStartTime = timeParts[0];         
             	    			slotEndTime = timeParts[2];           
             	    			slotEndDate = get_slot_text.match(/\((\d{2}\/\d{2}\/\d{4})\)/)[1];
                   		   
             	    			// Check if end time is past midnight
             	    			var startHour = parseInt(slotStartTime.split(":")[0]);
             	    			var endHour = parseInt(slotEndTime.split(":")[0]);
                   		    
             	    			if (endHour < startHour) {
             	    				// Add one day to slotEndDate
             	    				var dateParts = slotEndDate.split("/"); // [dd, mm, yyyy]
             	    				var dateObj = new Date(
             	    						parseInt(dateParts[2]),      // year
             	    						parseInt(dateParts[1]) - 1,  // month (0-based)
             	    						parseInt(dateParts[0])       // day
             	    				);

             	    				dateObj.setDate(dateObj.getDate() + 1);

             	    				// Format back to DD/MM/YYYY
             	    				slotEndDate =
             	    					("0" + dateObj.getDate()).slice(-2) + "/" +
             	    					("0" + (dateObj.getMonth() + 1)).slice(-2) + "/" +
             	    					dateObj.getFullYear();
             	    			}
             	    		}
             	    	}
             	    }
             	    
             	    var next_avl_hrs = parseFloat(document.getElementById("next_avl_hrs_" + j + "_" + i_index).value);

             	    if (!isNaN(next_avl_hrs) && slotEndDate!=="") 
            	    {
            	        var parts = slotEndDate.split("/");
            	        var timeParts = slotEndTime.split(":");

            	        var dateObj = new Date(
            	            parseInt(parts[2]),         // Year
            	            parseInt(parts[1]) - 1,     // Month (0-based)
            	            parseInt(parts[0]),         // Day
            	            parseInt(timeParts[0]),     // Hour
            	            parseInt(timeParts[1])      // Minute
            	        );

            	        // Add availability hours
            	        dateObj.setTime(dateObj.getTime() + next_avl_hrs * 60 * 60 * 1000);

            	        if (latestDate === null || dateObj > latestDate) {
            	            latestDate = dateObj;
            	        }
            	    }
        	    }
        	}

        	var inPageavailAfter ="";
        	
        	if (latestDate !== null) {
        		inPageavailAfter =
        	        ("0" + latestDate.getDate()).slice(-2) + "/" +
        	        ("0" + (latestDate.getMonth() + 1)).slice(-2) + "/" +
        	        latestDate.getFullYear() + " " +
        	        ("0" + latestDate.getHours()).slice(-2) + ":" +
        	        ("0" + latestDate.getMinutes()).slice(-2);
        	}
    	}
        
        var inPageAvailDateTime = parseDateTime(inPageavailAfter);
        var availDateTime = "";//parseDateTime(availAfter);
        
        var disp_availAfter ="";
        
        if (inPageAvailDateTime < parseDateTime(availAfter) || inPageavailAfter=="") 
        {
        	availDateTime = parseDateTime(availAfter);
        	
        	disp_availAfter = availAfter;
        }
        else
        {
        	availDateTime = parseDateTime(inPageavailAfter);
        	disp_availAfter = inPageavailAfter;
        }
        
        
        var slotDateTime = parseDateTime(slotDateTimeStr);

        
        if (slotDateTime < availDateTime) 
        {
            alert("Truck is not available for this slot. Available after: " + disp_availAfter);
            
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

/*
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
*/

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

/* function setArrivalSlot(index,selSlotVal,i_index)
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
} */

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

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch");
  	
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

function checkAllowableCredit(ori_index,buyer_nom_qty,counterparty_cd,agmt_no,cont_no,contract_type,plant_seq,bu_unit)
{
	var gas_dt = document.forms[0].gas_dt.value;
	
	$.post("../servlet/DB_Dlng_ContractMgmt_Ajax?setCallType=checkAllowableCredit"+
			"&counterparty_cd=" + counterparty_cd +
            "&agmt_no=" + agmt_no +
            "&cont_no=" + cont_no +
            "&contract_type=" + contract_type +
            "&plant_seq=" + plant_seq +
            "&bu_unit=" + bu_unit+
            "&buyer_qty="+ buyer_nom_qty +
            "&report_dt="+ gas_dt, function (responseJson) {
		console.log(responseJson);
		$.each(responseJson, function(index, json) {
			$.each(json.MSG_DTL, function(index_1, json_1) {
				
				if(json_1.MSG_TYPE == "E")
				{
					document.getElementById("creditmsg_"+ori_index).innerHTML=json_1.MSG;
					document.getElementById("creditmsg_"+ori_index).style.fontWeight="bold";
					document.getElementById("creditmsg_"+ori_index).style.color="red";
				}
				else
				{
					document.getElementById("creditmsg_"+ori_index).innerHTML=json_1.MSG;
					document.getElementById("creditmsg_"+ori_index).style.fontWeight="bold";
					document.getElementById("creditmsg_"+ori_index).style.color="green";
				}
			});
		});
    });
}

function resetSessionData()
{
     $.post("../servlet/DB_Dlng_ContractMgmt_Ajax?setCallType=resetSessionData", function (responseJson) {

               
    });
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