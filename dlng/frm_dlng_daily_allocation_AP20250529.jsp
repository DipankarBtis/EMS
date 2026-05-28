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
	
	var url = "frm_dlng_daily_allocation.jsp?gas_dt="+gas_dt+"&u="+u;

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
	var truck_cd = document.getElementById("truck_cd"+index);
	var truck_trans_cd = document.getElementById("truck_trans_cd"+index);
	
	var gen_time = document.getElementById("gen_time"+index);
	/* var rd1 = document.getElementById("rd1"+index);
	var rd2 = document.getElementById("rd2"+index); */
	var gcv = document.getElementById("gcv"+index);
	var ncv = document.getElementById("ncv"+index);
	var base = document.getElementById("base"+index);
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index);
	var qty_mt = document.getElementById("qty_mt"+index);
	var gcv_mmbtu = document.getElementById("gcv_mmbtu"+index);

	var load_start_dt = document.getElementById("load_start_dt"+index);
	var load_start_time = document.getElementById("load_start_time"+index);
	var load_end_dt = document.getElementById("load_end_dt"+index);
	var load_end_time = document.getElementById("load_end_time"+index);

	var plant_seq = document.getElementById("plant_seq"+index);
	//var trans_cd = document.getElementById("trans_cd"+index);
	var trans_plant_seq = document.getElementById("trans_plant_seq"+index);
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
		gen_time.disabled=false;
		//rd1.disabled=false;
		//rd2.disabled=false;
		gcv.disabled=false;
		ncv.disabled=false;
		base.disabled=false;
		
		qty_mmbtu.disabled=false;
		qty_mt.disabled=false;
		gcv_mmbtu.disabled=false;
		
		load_start_dt.disabled=false;
		load_start_time.disabled=false;
		load_end_dt.disabled=false;
		load_end_time.disabled=false;
		
		truck_trans_cd.disabled=false;
		truck_cd.disabled=false;
		
		if(parseInt(index1.value) > 0) //IF CT CONFIGURED
		{
			qty_mmbtu.readOnly=true;
			qty_mt.readOnly=true;
			gcv_mmbtu.readOnly=true;
			
			qty_mmbtu.style.pointerEvents = "none";
			qty_mt.style.pointerEvents = "none";
			gcv_mmbtu.style.pointerEvents = "none";
		}
		else
		{
			qty_mmbtu.readOnly=false;
			//qty_mt.readOnly=false;
			
			qty_mmbtu.style.pointerEvents = "auto";
			qty_mt.style.pointerEvents = "auto";
			gcv_mmbtu.style.pointerEvents = "auto";
		}
		
		plant_seq.disabled=false;
		//trans_cd.disabled=false;
		//trans_plant_seq.disabled=false;
		bu_plant_seq.disabled=false;
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
		gen_time.disabled=true;
		truck_trans_cd.disabled=true;
		truck_cd.disabled=true;
		/* rd1.disabled=true;
		rd2.disabled=true; */
		gcv.disabled=true;
		ncv.disabled=true;
		base.disabled=true;
		
		qty_mmbtu.disabled=true;
		qty_mt.disabled=true;
		gcv_mmbtu.disabled=true;
		
		load_start_dt.disabled=true;
		load_start_time.disabled=true;
		load_end_dt.disabled=true;
		load_end_time.disabled=true;

		truck_remark.disabled=true;
		
		plant_seq.disabled=true;
		//trans_cd.disabled=true;
		//trans_plant_seq.disabled=true;
		bu_plant_seq.disabled=true;
	}
}

function calculateMT(j_index,l_index)
{
	var rd1 = document.getElementById("rd1"+j_index);
	var rd2 = document.getElementById("rd2"+j_index);
	var qty_mmbtu = document.getElementById("qty_mmbtu"+l_index);
	var qty_mt = document.getElementById("qty_mt"+l_index);

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

function calculateMMBTU(j_index,l_index)
{
	var rd1 = document.getElementById("rd1"+j_index);
	var rd2 = document.getElementById("rd2"+j_index);
	var qty_mmbtu = document.getElementById("qty_mmbtu"+l_index);
	var qty_mt = document.getElementById("qty_mt"+l_index);
	var gcv_mmbtu = document.getElementById("gcv_mmbtu"+l_index).value;

	//var convt_mmbtu_to_mt = document.forms[0].convt_mmbtu_to_mt.value; //For Converting MMBTU TO MT...
	
	if((qty_mt.value!=null && trim(qty_mt.value) !=''))
	{
		var mmbtu = ""+round(parseFloat(qty_mt.value) * parseFloat(gcv_mmbtu),2);
		
		if(isNaN(mmbtu))
		{
			qty_mmbtu.value="";
		}
		else
		{
			qty_mmbtu.value = mmbtu;
		}
	}
}

function checkTruckCap(j_index,l_index)
{
	var truck_vol_mmbtu = document.getElementById("truck_vol_mmbtu"+l_index).value;
	var truck_vol_mt = document.getElementById("truck_vol_mt"+l_index).value;

	var qty_mmbtu = document.getElementById("qty_mmbtu"+l_index).value;
	var qty_mt = document.getElementById("qty_mt"+l_index).value;

	var msg ="";
	var flag=true;
	
	if((qty_mt!=null && trim(qty_mt) !='') && (qty_mmbtu!=null && trim(qty_mmbtu) !=''))
	{
		console.log("Truck Cap : "+parseFloat(truck_vol_mt)+" - "+parseFloat(truck_vol_mmbtu));
		console.log("Ent Val : "+parseFloat(qty_mt)+" - "+parseFloat(qty_mmbtu));
		
		if(parseFloat(truck_vol_mmbtu)<parseFloat(qty_mmbtu))
		{
			msg+= ("Allocation Qty(MMBTU) should be less than Truck Capacity(MMBTU)!\n");
			flag=false;
			
			document.getElementById("qty_mmbtu"+l_index).value="";
			document.getElementById("qty_mt"+l_index).value="";
		}

		if(parseFloat(truck_vol_mt)<parseFloat(qty_mt))
		{
			msg+= ("Allocation Qty(MT) should be less than Truck Capacity(MT)!\n");
			flag=false;
			
			document.getElementById("qty_mmbtu"+l_index).value="";
			document.getElementById("qty_mt"+l_index).value="";
		}
		
		if(flag==false)
		{
			alert(msg);
		}
	}
	else
	{
		alert("Enter Valid Allocation quantity!!");
		document.getElementById("qty_mmbtu"+l_index).value="";
		document.getElementById("qty_mt"+l_index).value="";
	}
}

/* function calculateSCM(index)
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
				scm = ""+round(((parseFloat(""+sub_qty_mmbtu.value)*multiplying_factor)/(baseVal*deviding_factor)),0);
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
} */

function doSubmit()
{
	var truck_chk = document.forms[0].chk;
	var gas_dt = document.forms[0].gas_dt;
	var gen_dt = document.forms[0].gen_dt;
	var gen_time = document.forms[0].gen_time;
	var gcv = document.forms[0].gcv;
	var ncv = document.forms[0].ncv;
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_mt = document.forms[0].qty_mt;
	
	var load_start_dt =document.forms[0].load_start_dt;
	var load_start_time =document.forms[0].load_start_time;
	var load_end_dt =document.forms[0].load_end_dt;
	var load_end_time =document.forms[0].load_end_time;
	
	var msg="";
	var flag=true;
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
	if(truck_chk!=null && truck_chk.length!=undefined)
	{
		for(var i=0; i<truck_chk.length; i++)
		{
			if(truck_chk[i].checked)
			{
				chk_count++;
				if(trim(qty_mmbtu[i].value)=="")
				{
					msg+="Enter Truck Allocation Qty(MMBTU) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(qty_mt[i].value)=="")
				{
					msg+="Enter Truck Allocation Qty(MT) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(load_start_dt[i].value)=="")
				{
					msg+="Enter Loading Start Date for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(load_start_time[i].value)=="")
				{
					msg+="Enter Loading Start Time for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(load_end_dt[i].value)=="")
				{
					msg+="Enter Loading End Date for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				if(trim(load_end_time[i].value)=="")
				{
					msg+="Enter Loading End Time for ROW - "+parseInt(i+1)+"!\n";
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
			if(trim(qty_mmbtu.value)=="")
			{
				msg+="Enter Truck Allocation Qty(MMBTU) for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			if(trim(qty_mt.value)=="")
			{
				msg+="Enter Truck Allocation Qty(MT) for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			if(trim(load_start_dt.value)=="")
			{
				msg+="Enter Loading Start Date for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			if(trim(load_start_time.value)=="")
			{
				msg+="Enter Loading Start Time for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			if(trim(load_end_dt.value)=="")
			{
				msg+="Enter Loading End Date for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			if(trim(load_end_time.value)=="")
			{
				msg+="Enter Loading End Time for ROW - "+parseInt(i+1)+"!\n";
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
		var a = confirm("Do you want to Submit DLNG Daily Allocation?");
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

function validateLoadTimes({
	l_index,
	vslotStartTime,
	vslotEndTime,
	varrivalDate
}) {
	function toDateTime(dateStr, timeStr)
	{
		if (!dateStr || !timeStr) return new Date(NaN); // prevent invalid dates

		const [day, month, year] = dateStr.split('/').map(Number);
		const [hour, minute] = timeStr.trim().split(':').map(Number);

		if (
			isNaN(day) || isNaN(month) || isNaN(year) ||
			isNaN(hour) || isNaN(minute)
		) {
			return new Date(NaN);
		}

		return new Date(year, month - 1, day, hour, minute);
	}

	var loadStartDate =document.getElementById('load_start_dt' + l_index).value;
	var loadStartTime =document.getElementById('load_start_time' + l_index).value
	var loadEndDate =document.getElementById('load_end_dt' + l_index).value
	var loadEndTime =document.getElementById('load_end_time' + l_index).value

	let msg = "";
	let isValid = true;
	
	const loadStart = toDateTime(loadStartDate, loadStartTime);
	const loadEnd = toDateTime(loadEndDate, loadEndTime);
	const slotStart = toDateTime(varrivalDate, vslotStartTime);
	const slotEnd = toDateTime(varrivalDate, vslotEndTime);

	if (loadEndTime !== "" && loadEndTime !== null) {
		// Condition 1
		if (!(loadStart >= slotStart && loadStart <= slotEnd)) {
			msg += "Load Start Date/Time must be within the available slot window!\n";
			document.getElementById('load_start_dt' + l_index).value = "";
			document.getElementById('load_start_time' + l_index).value = "";
			isValid = false;
		}

		// Condition 2
		if (!(loadEnd >= slotStart && loadEnd <= slotEnd)) {
			msg += "Load End Date/Time must be within the available slot window!\n";
			document.getElementById('load_end_dt' + l_index).value = "";
			document.getElementById('load_end_time' + l_index).value = "";
			isValid = false;
		}

		// Condition 3
		if (!(loadStart <= loadEnd)) {
			msg += "Load Start Time must be before or equal to Load End Time!\n";
			document.getElementById('load_end_dt' + l_index).value = "";
			document.getElementById('load_end_time' + l_index).value = "";
			isValid = false;
		}
	}

	if (!isValid) {
		alert(msg);
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_Dlng_ContractMgmt" id="cont_mgmt" scope="request"></jsp:useBean>
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
Vector VTRANSPORTER_CD = cont_mgmt.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = cont_mgmt.getVTRANSPORTER_ABBR();
Vector VCONT_BU_PLANT_SEQ = cont_mgmt.getVCONT_BU_PLANT_SEQ();
Vector VCONT_BU_PLANT_MAP = cont_mgmt.getVCONT_BU_PLANT_MAP();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = cont_mgmt.getVCOUNTERPARTY_PLANT_ABBR();
Vector VTITTLE_DISP_CONT_NO = cont_mgmt.getVTITTLE_DISP_CONT_NO();

Vector VNOM_REV_NO = cont_mgmt.getVNOM_REV_NO();
Vector VGEN_TIME = cont_mgmt.getVGEN_TIME();
Vector VGEN_DT = cont_mgmt.getVGEN_DT();
Vector VBASE = cont_mgmt.getVBASE();
Vector VGCV = cont_mgmt.getVGCV();
Vector VNCV = cont_mgmt.getVNCV();
Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();
Vector VQTY_MT = cont_mgmt.getVQTY_MT();
Vector VGCV_MMBTU = cont_mgmt.getVGCV_MMBTU();
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
Vector VCONT_PLANT_WISE_TOTAL_MMBTU = cont_mgmt.getVCONT_PLANT_WISE_TOTAL_MMBTU();
Vector VCONT_PLANT_WISE_TOTAL_SCM = cont_mgmt.getVCONT_PLANT_WISE_TOTAL_SCM();
Vector VTOTAL_MMBTU_COLOR = cont_mgmt.getVTOTAL_MMBTU_COLOR();

Vector VINDEX = cont_mgmt.getVINDEX();
Vector VINDEX1 = cont_mgmt.getVINDEX1();
Vector VSUB_INDEX = cont_mgmt.getVSUB_INDEX();

/* Vector VMETER_QTY_MMBTU = cont_mgmt.getVMETER_QTY_MMBTU();
Vector VMETER_QTY_SCM = cont_mgmt.getVMETER_QTY_SCM();
Vector VMETER_GCV = cont_mgmt.getVMETER_GCV();
Vector VMETER_NCV = cont_mgmt.getVMETER_NCV(); */

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

/* Vector VREMARK = cont_mgmt.getVREMARK();
Vector VNEXT_AVAIL_HRS = cont_mgmt.getVNEXT_AVAIL_HRS();
Vector VARRIVAL_TIME = cont_mgmt.getVARRIVAL_TIME();
*/
Vector VARRIVAL_DT = cont_mgmt.getVARRIVAL_DT();
Vector VSLOT_END_TIME = cont_mgmt.getVSLOT_END_TIME();
Vector VSLOT_START_TIME = cont_mgmt.getVSLOT_START_TIME();


Vector VLOAD_START_DT = cont_mgmt.getVLOAD_START_DT();
Vector VLOAD_START_TIME = cont_mgmt.getVLOAD_START_TIME();
Vector VLOAD_END_DT = cont_mgmt.getVLOAD_END_DT();
Vector VLOAD_END_TIME = cont_mgmt.getVLOAD_END_TIME();
 
Vector VDISP_SLOT_DTL = cont_mgmt.getVDISP_SLOT_DTL();
Vector VBAY_CD = cont_mgmt.getVBAY_CD();
Vector VBAY_NM = cont_mgmt.getVBAY_NM();
Vector VFILL_STATION_CD = cont_mgmt.getVFILL_STATION_CD();
Vector VFILL_STATION_ABBR = cont_mgmt.getVFILL_STATION_ABBR();
Vector VTRUCK_LOAD_CAP =  cont_mgmt.getVTRUCK_LOAD_CAP();
Vector VTRUCK_VOL_MT =  cont_mgmt.getVTRUCK_VOL_MT();
Vector VTRUCK_VOL_MMBTU =  cont_mgmt.getVTRUCK_VOL_MMBTU();
Vector VTRUCK_VOL_M3 =  cont_mgmt.getVTRUCK_VOL_M3();
Vector VTRUCK_REG_NUM =  cont_mgmt.getVTRUCK_REG_NUM();
Vector VTRUCK_CD =  cont_mgmt.getVTRUCK_CD();
Vector VTRUCK_TRANS_CD =  cont_mgmt.getVTRUCK_TRANS_CD();

Vector VCONT_MST_FILLST_CD =  cont_mgmt.getVCONT_MST_FILLST_CD();
Vector VCONT_MST_FILLST_NM =  cont_mgmt.getVCONT_MST_FILLST_NM();
Vector VCONT_MST_FILLST_ABBR =  cont_mgmt.getVCONT_MST_FILLST_ABBR();
%>
<body onload="">
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
					    	Daily Truck Loading
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
				<%if(VTITTLE_DISP_CONT_NO.size() > 0){ %>
				<div class="card-body cdbody">
					<%int j=0,k=0,l=0,m=0,p=0,q=0;
					for(int i=0; i<VTITTLE_DISP_CONT_NO.size(); i++)
					{ 
						String disp_cont=""+VTITTLE_DISP_CONT_NO.elementAt(i);
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
					%>
					<%if(i>0){ %>&nbsp;<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VTITTLE_DISP_CONT_NO.elementAt(i)%> (<%=VCONT_REF.elementAt(i)%>)</label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
						<%k=0;
						if(index > 0){ %>
							<%for(j=j;j<VCONT_BU_PLANT_SEQ.size(); j++) 
							{
								String trans_plant_seq=""+VCONT_BU_PLANT_SEQ.elementAt(j);
								int sub_index = Integer.parseInt(""+VSUB_INDEX.elementAt(j));
								k+=1;
								
								/* String gcv = "";//+VMETER_GCV.elementAt(j);
								String ncv = "";//+VMETER_NCV.elementAt(j); */
								
								String gcv="9802.80";
								String ncv="8831.35";
							%>
								<input type="hidden" name="sub_index" value="<%=sub_index%>">
								<input type="hidden" name="trans_abbr" value="<%=VCONT_BU_PLANT_MAP.elementAt(j)%>">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading<%=l%>">
    										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
								    			<font><%=VCONT_BU_PLANT_MAP.elementAt(j).toString().replace("@", "  <i class='fa fa-truck fa-lg fa-flip-horizontal' aria-hidden='true' style='pointer-events: none;color:#008080'></i>  ")%>&nbsp;&nbsp;
								    			<%-- <%if(Double.parseDouble(""+VCONT_PLANT_WISE_TOTAL_MMBTU.elementAt(j)) > 0){%>
								    				<font color="<%=VTOTAL_MMBTU_COLOR.elementAt(j)%>" style="background: white;padding: 2px 5px 4px 5px;border-radius: 30px;">
								    					[<%=VCONT_PLANT_WISE_TOTAL_MMBTU.elementAt(j)%> MMBTU] [<%=VCONT_PLANT_WISE_TOTAL_SCM.elementAt(j)%> SCM]
								    				</font>
								    			<%} %> --%>
								    			</font>
								      		</button>
								    	</h2>
								    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
								      		<div class="accordion-body accor-body">
								        		<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr style="background:#bce6ff;color:#0c63e4;">
																	<th colspan="14">
																		<div class="row m-b-5" style="background:#bce6ff;color:#0c63e4;">
																			<div class="col-sm-0 col-xs-3 col-md-3">
														      				</div>	
														      				<div class="col-sm-6 col-xs-3 col-md-6">
																    			<div class="form-group row justify-content-center" >
																					<div class="col-auto">
																						<label class="form-label"><b>
																						<input type="radio" name="rd<%=j%>" id="rd1<%=j%>" onclick="updateGcvNcv('<%=j%>')" checked>&nbsp;GCV : 
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
																    					<input type="radio" name="rd<%=j%>" id="rd2<%=j%>" onclick="updateGcvNcv('<%=j%>')">&nbsp;NCV : 
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
																	<th rowspan="2">Select</th>
																	<th rowspan="2">Truck#</th>
																	<th colspan="3">Truck Capacity</th>
																	<th rowspan="2">Seller Nomination (Rev)<br>(MMBTU)</th>
																	<th colspan="3">Truck Allocation Qty</th>
																	<th rowspan="3">Filling Station Association</th>
																	<th colspan="2">Arrival</th>
																	<!-- <th rowspan="2">Next Available<br>(In Hrs)</th> -->
																	<th rowspan="2">Status</th>
																	<th rowspan="2">Gen Time</th>
																	<!-- <th rowspan="2">Calorific Value Base<br>KCal/SCM</th> -->
																</tr>
																<tr>
																	<th>M3</th>
																	<th>MT</th>
																	<th>MMBTU</th>
																	<th>MT</th>
																	<th>GCV/MMBTU</th>
																	<th>MMBTU</th>
																	<!-- <th>Filling Station</th>
																	<th>Bay</th>
																	<th>Slot</th> -->
																	<th>Loading Start</th>
																	<th>Loading End</th>
																</tr>
															</thead>
															<tbody>
																<%m=0;
																if(sub_index>0){ %>
																	<%for(l=l; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
																	{
																		m+=1;
																		
																		Vector VCONT_FILL_ST_CD = new Vector();
																		Vector VCONT_FILL_ST_NM = new Vector();
																		Vector VCONT_FILL_ST_ABBR = new Vector();
																		
																		VCONT_FILL_ST_CD = (Vector) VCONT_MST_FILLST_CD.elementAt(l);
																		VCONT_FILL_ST_NM = (Vector) VCONT_MST_FILLST_NM.elementAt(l);
																		VCONT_FILL_ST_ABBR = (Vector) VCONT_MST_FILLST_ABBR.elementAt(l);
																	%>
																		<tr>
																			<td align="center" valign="middle" <%if(VNOM_BLOCK.elementAt(l).equals("Y")) {%>style="background: #df9fbf;" title="Invoice Generated!"<%} %>>
																				<input type="checkbox" class="form-check-input" name="chk" id="chk<%=l%>" 
																				onclick="setEnableDisabled(this,'<%=l%>');calculateMMBTU('<%=j %>','<%=l%>');" <%-- totalSubQty('<%=l%>');totalQty(); --%>
																				<%if(VNOM_BLOCK.elementAt(l).equals("Y")) {%>disabled style="pointer-events: none;"<%} %>>
																				<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=l%>" value="<%=VCOUNTERPARTY_CD.elementAt(l)%>" disabled>
																				<input type="hidden" name="agmt_no" id="agmt_no<%=l%>" value="<%=VAGMT_NO.elementAt(l)%>" disabled>
						      													<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=l%>" value="<%=VAGMT_REV_NO.elementAt(l)%>" disabled>
															      				<input type="hidden" name="cont_no" id="cont_no<%=l%>" value="<%=VCONT_NO.elementAt(l)%>" disabled>
															      				<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=l%>" value="<%=VCONT_REV_NO.elementAt(l)%>" disabled>
															      				<input type="hidden" name="contract_type" id="contract_type<%=l%>" value="<%=VCONTRACT_TYPE.elementAt(l)%>" disabled>
															      				<input type="hidden" name="plant_seq" id="plant_seq<%=l%>" value="<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(l)%>" disabled>
															      				<input type="hidden" name="bu_plant_seq" id="bu_plant_seq<%=l%>" value="<%=VBU_PLANT_SEQ.elementAt(l)%>" disabled>
															      				<input type="hidden" name="truck_trans_cd" id="truck_trans_cd<%=l%>" value="<%=VTRUCK_TRANS_CD.elementAt(l)%>" disabled>
															      				<input type="hidden" name="truck_cd" id="truck_cd<%=l%>" value="<%=VTRUCK_CD.elementAt(l)%>" disabled>
															      				<input type="hidden" name="internal_map_id" id="internal_map_id<%=l%>" value="<%=VINTERNAL_MAP_ID.elementAt(l)%>">
																				<input type="hidden" name="is_exist" id="is_exist<%=l%>" value="<%=VIS_EXIST.elementAt(l)%>">
																				<input type="hidden" name="nom_block" id="nom_block<%=l%>" value="<%=VNOM_BLOCK.elementAt(l)%>" disabled>
																				<input type="hidden" name="index1" id="index1<%=l%>" value="<%//=index1%>" disabled>
																				<input type="hidden" name="index" id="index_<%=l%>" value="<%=l%>" disabled>
																			</td>
																			<td>
																				<%=VTRUCK_REG_NUM.elementAt(l)%>
																			</td>
																			<td>
																				<%=VTRUCK_VOL_M3.elementAt(l)%>
																			</td>
																			<td>
																				<%=VTRUCK_VOL_MT.elementAt(l)%>
																				<input type="hidden" name="truck_vol_mt" id="truck_vol_mt<%=l%>" value="<%=VTRUCK_VOL_MT.elementAt(l)%>">
																			</td>
																			<td>
																				<%=VTRUCK_VOL_MMBTU.elementAt(l)%>
																				<input type="hidden" name="truck_vol_mmbtu" id="truck_vol_mmbtu<%=l%>" value="<%=VTRUCK_VOL_MMBTU.elementAt(l)%>">
																			</td>
																			<td align="right">
																				<%=VBUYER_NOM.elementAt(l)%> (<%=VBUYER_NOM_REV_NO.elementAt(l)%>)
																				<input type="hidden" name="buyer_nom_qty" id="buyer_nom_qty<%=l%>" value="<%=VBUYER_NOM.elementAt(l)%>">
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="qty_mt" id="qty_mt<%=l%>" value="<%=VQTY_MT.elementAt(l)%>" style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" disabled
																					onchange="calculateMMBTU('<%=j %>','<%=l%>');checkTruckCap('<%=j %>','<%=l%>');"
																					onblur="calculateMMBTU('<%=j %>','<%=l%>');checkTruckCap('<%=j %>','<%=l%>');">
																					<input type="hidden" name="gcv" id="gcv<%=l%>" value="<%=VGCV.elementAt(l)%>" disabled>
																					<input type="hidden" name="ncv" id="ncv<%=l%>" value="<%=VNCV.elementAt(l)%>" disabled>
																					<input type="hidden" name="base" id="base<%=l%>" value="<%=VBASE.elementAt(l)%>" disabled>
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="gcv_mmbtu" id="gcv_mmbtu<%=l%>" value="<%=VGCV_MMBTU.elementAt(l)%>" style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" disabled 
																					onchange="calculateMMBTU('<%=j %>','<%=l%>');checkTruckCap('<%=j %>','<%=l%>');"
																					onblur="calculateMMBTU('<%=j %>','<%=l%>');checkTruckCap('<%=j %>','<%=l%>');">
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="qty_mmbtu" id="qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" disabled 
																					onchange="checkTruckCap('<%=j %>','<%=l%>');"
																					onblur="checkTruckCap('<%=j %>','<%=l%>');">
																				</div>
																			</td>
																			<td align="center">
																				<%=VFILL_STATION_ABBR.elementAt(l)%> [<%=VBAY_NM.elementAt(l)%>]<br><%=VDISP_SLOT_DTL.elementAt(l)%>
																				<%-- <div style="width:200px;">
																					<select class="form-select form-select-sm" name="filling_station" id="filling_station_<%=l%>" 
																					onchange="fetchBayDeatils('<%=l%>',this.value,'<%=owner_cd%>');"  disabled 
																					<%if(VNOM_COLOR.elementAt(l).equals("")){ %>
																					 style="pointer-events: none;background:#e9ecef"
																					 <%}else{ %>
																					 style="pointer-events: none;background:<%=VNOM_COLOR.elementAt(l)%>"
																					 <%} %>
																					 > <!-- style="pointer-events: none;" -->
																				   	 	 <!-- <option value="" selected="selected">--Select--</option> -->
																				   	 	 <%for(int g=0;g<VCONT_FILL_ST_CD.size();g++){ %>
																				   	 	 <option value="<%=VCONT_FILL_ST_CD.elementAt(g)%>"><%=VCONT_FILL_ST_ABBR.elementAt(g)%> - <%=VCONT_FILL_ST_NM.elementAt(g)%></option>
																				   	 	 <%} %>
																				   	</select>
																				   	<script>
																				   		document.getElementById('filling_station_'+<%=l%>).value="<%=VFILL_STATION_CD.elementAt(l)%>";
																				   		fetchBayDeatils('<%=l%>','<%=VFILL_STATION_CD.elementAt(l)%>','<%=owner_cd%>');
																				   	</script>
																				</div> --%>
																			</td>
																			<%-- <td align="center">	
																				<div style="width:100px;">
																					<select class="form-select form-select-sm" name="sel_bay" id="sel_bay_<%=l%>"
																					onchange="fetchSlotDeatils('<%=l%>',this.value,'<%=owner_cd%>');"  disabled 
																					<%if(VNOM_COLOR.elementAt(l).equals("")){ %>
																					 style="pointer-events: none;background:#e9ecef"
																					 <%}else{ %>
																					 style="pointer-events: none;background:<%=VNOM_COLOR.elementAt(l)%>"
																					 <%} %>
																					 > <!-- style="pointer-events: none;" -->
																				   	 	 <option value="<%=VBAY_CD.elementAt(l)%>" selected="selected"><%=VBAY_NM.elementAt(l)%></option>
																				   	</select>
																				   	<input type="hidden" name="get_bay" id="get_bay_<%=l%>" value="<%=VBAY_CD.elementAt(l)%>">
																				   	<script>
																				   		document.getElementById('sel_bay_'+<%=l%>).value="<%=VBAY_CD.elementAt(l)%>";
																				   		fetchSlotDeatils('<%=l%>','<%=VBAY_CD.elementAt(l)%>','<%=owner_cd%>');
																				   	</script>
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:200px;">
																					<select class="form-select form-select-sm" name="sel_slot" id="sel_slot_<%=l%>" 
																					 onchange="setArrivalSlot('<%=l%>',this.options[this.selectedIndex].text);"  disabled 
																					 <%if(VNOM_COLOR.elementAt(l).equals("")){ %>
																					 style="pointer-events: none;background:#e9ecef"
																					 <%}else{ %>
																					 style="pointer-events: none;background:<%=VNOM_COLOR.elementAt(l)%>"
																					 <%} %>
																					 > <!-- style="pointer-events: none;" -->
																				   	 	 <option value="<%=VSLOT_START_TIME.elementAt(l)%>-<%=VSLOT_END_TIME.elementAt(l)%>" selected="selected" ><%=VDISP_SLOT_DTL.elementAt(l)%></option>
																				   	</select>
																				   	<input type="hidden" name="get_slot" id="get_slot_<%=l%>" value="<%=VSLOT_START_TIME.elementAt(l)%>-<%=VSLOT_END_TIME.elementAt(l)%>">
																				   	<script>
																				   		document.getElementById('sel_slot_'+<%=l%>).value="<%=VSLOT_START_TIME.elementAt(l)%>-<%=VSLOT_END_TIME.elementAt(l)%>"
																				   	</script>
																				</div>
																			</td> --%>
																			<td align="center">
																				<div style="width:220px;">
																					<div class="row m-b-5">
																						<div class="col">
																							<div class="input-group input-group-sm">
																		      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="load_start_dt" id="load_start_dt<%=l%>" value="<%=VLOAD_START_DT.elementAt(l)%>" maxLength="10" 
																		      					style="background:<%=VNOM_COLOR.elementAt(l)%>"
																		      					onblur="validateDate(this);"
																		      					onchange="validateDate(this);validateLoadTimes({
																									    l_index: <%=l %>,
																									    vslotStartTime: '<%= VSLOT_START_TIME.elementAt(l) %>',
																									    vslotEndTime: '<%= VSLOT_END_TIME.elementAt(l) %>',
																									    varrivalDate: '<%= VARRIVAL_DT.elementAt(l) %>'
																									});" disabled>
																		      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
																		      				</div>
																	      				</div>
																	      				<div class="col">
																		      				<div class="input-group input-group-sm" >
																	      						<input type="text" class="form-control form-control-sm" name="load_start_time" id="load_start_time<%=l%>" value="<%=VLOAD_START_TIME.elementAt(l)%>" maxLength="5" 
																	      						style="background:<%=VNOM_COLOR.elementAt(l)%>"
																	      						onblur="validateTime(this);" 
																			      					onchange="validateTime(this);validateLoadTimes({
																									    l_index: <%=l %>,
																									    vslotStartTime: '<%= VSLOT_START_TIME.elementAt(l) %>',
																									    vslotEndTime: '<%= VSLOT_END_TIME.elementAt(l) %>',
																									    varrivalDate: '<%= VARRIVAL_DT.elementAt(l) %>'
																									});"
																		      					 autocomplete="off"  disabled>
																	      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
																      						</div>
															      						</div>
															      					</div>
															      				</div>
																			</td>
																			<td align="center">
																				<div style="width:220px;">
																					<div class="row m-b-5">
																						<div class="col">
																							<div class="input-group input-group-sm">
																		      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="load_end_dt" id="load_end_dt<%=l%>" value="<%=VLOAD_END_DT.elementAt(l)%>" maxLength="10" 
																		      					style="background:<%=VNOM_COLOR.elementAt(l)%>"
																		      					onblur="validateDate(this);" 
																		      					onchange="validateDate(this);validateLoadTimes({
																									    l_index: <%=l %>,
																									    vslotStartTime: '<%= VSLOT_START_TIME.elementAt(l) %>',
																									    vslotEndTime: '<%= VSLOT_END_TIME.elementAt(l) %>',
																									    varrivalDate: '<%= VARRIVAL_DT.elementAt(l) %>'
																									});"
																									 disabled>
																		      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
																		      				</div>
																	      				</div>
																	      				<div class="col">
																		      				<div class="input-group input-group-sm" >
																	      						<input type="text" class="form-control form-control-sm" name="load_end_time" id="load_end_time<%=l%>" value="<%=VLOAD_END_TIME.elementAt(l)%>" maxLength="5" 
																	      						style="background:<%=VNOM_COLOR.elementAt(l)%>"
																	      						onblur="validateTime(this);" 
																			      					onchange="validateTime(this);validateLoadTimes({
																									    l_index: <%=l %>,
																									    vslotStartTime: '<%= VSLOT_START_TIME.elementAt(l) %>',
																									    vslotEndTime: '<%= VSLOT_END_TIME.elementAt(l) %>',
																									    varrivalDate: '<%= VARRIVAL_DT.elementAt(l) %>'
																									});"  
																		      					autocomplete="off"  disabled>
																	      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
																      						</div>
															      						</div>
															      					</div>
															      				</div>
																			</td>
																			<%-- <td align="center">	
																				<div class="input-group input-group-sm" >
																					<input type="text" class="form-control form-control-sm" name="next_avl_hrs" id="next_avl_hrs_<%=l%>"  value="<%=VNEXT_AVAIL_HRS.elementAt(l)%>"  maxlength="3"
																					onchange="checkNextAvailHrs(this);"
																					onblur="checkNextAvailHrs(this);" disabled style="background:<%=VNOM_COLOR.elementAt(l)%>">
																					<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
																				</div>
																			</td> --%>
																			<td align="center">
															      				<%-- <textarea class="form-control" name="truck_remark" id="truck_remark_<%=l%>" cols="100" rows="1" maxlength="500"  disabled 
															      				style="width:100px;background:<%=VNOM_COLOR.elementAt(l)%>"><%=VREMARK.elementAt(l) %></textarea> --%>
															      				<div align="center">
																					<font style="color:<%if(VIS_EXIST.elementAt(l).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
																						<i class="fa fa-circle fa-lg" ></i>
																						&nbsp;
																					</font>
																					<%if(VIS_EXIST.elementAt(l).equals("Y")){%>
																					Loaded
																					<%}else{ %>
																					Loading
																					<%} %>
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
																		</tr>
																		
																		<%if(m==sub_index)
																		{%>
																			<%-- <tr>
																				<td colspan="6" align="right">
																					<b>Total(<%=VBU_PLANT_ABBR.elementAt(j)%>)</b>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_qty_mmbtu" value="<%=VCONT_PLANT_WISE_TOTAL_MMBTU.elementAt(j)%>" style="text-align:right;font-weight: bold;" readOnly>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_qty_scm" value="<%=VCONT_PLANT_WISE_TOTAL_SCM.elementAt(j)%>" style="text-align:right;font-weight: bold;" readOnly>
																					</div>
																				</td>
																				<td align="right" colspan="7">
																				</td>
																			</tr> --%>
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

<script>

</script>

<input type="hidden" name="option" value="DAILY_ALLOCATION">

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