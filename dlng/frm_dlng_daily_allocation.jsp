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

function setEnableDisabled(obj, index) 
{
    var totalRows = document.querySelectorAll("input[type='radio'][name='chk']").length;
    
    for (let i = 0; i < totalRows; i++) 
    {
    	var isSelected = (parseInt(i) === parseInt(index));
    	var nom_block = document.getElementById("nom_block" + i);
    	
    	var isNomBlockM = nom_block && nom_block.value === 'M';
    	
        const elements = [
            "counterparty_cd", "agmt_no", "agmt_rev_no", "cont_no", "cont_rev_no",
            "contract_type", "gen_time", "gcv", "ncv", "base", "qty_mmbtu", "qty_mt",
            "gcv_mmbtu", "load_start_dt", "load_start_time", "load_end_dt",
            "load_end_time", "next_avail_hrs", "truck_cd", "truck_trans_cd",
            "plant_seq", "trans_plant_seq", "bu_plant_seq", "index_", "index1",
            "filling_station_", "bay_cd_", "slot_start_time_", "slot_end_time_","cargo_no"
        ];

        elements.forEach(function (id) {

            var element = document.getElementById(id + i);

            if (element) {

                // Normal enable/disable based on radio selection
                element.disabled = !isSelected;

                // Fields to block when nom_block = 'M'
                const blockedFields = [
                    "qty_mt", "gcv_mmbtu",
                    "load_start_dt", "load_start_time",
                    "load_end_dt", "load_end_time"
                ];

                if (isSelected && isNomBlockM && blockedFields.includes(id)) {

                    // Prevent mouse + typing
                    element.style.pointerEvents = "none";
                    element.readOnly = true;

                } 
            }
        });
        
        var qty_mmbtu = document.getElementById("qty_mmbtu" + index);
        var qty_mt = document.getElementById("qty_mt" + index);
        var gcv_mmbtu = document.getElementById("gcv_mmbtu" + index);
        var index1 = document.getElementById("index1" + index);
        
        if (isSelected) 
        {
        	if (qty_mmbtu) {
                qty_mmbtu.readOnly = false;
                qty_mmbtu.style.pointerEvents = "auto";
            }
        	if (qty_mt) {
                qty_mt.readOnly = false;
                qty_mt.style.pointerEvents = "auto";
            }
            if (gcv_mmbtu) {
                gcv_mmbtu.readOnly = false;
                gcv_mmbtu.style.pointerEvents = "auto";
            }
        }
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
	//Below function removed after Vijay's feedback : 20250623
	
	/* var truck_vol_mmbtu = document.getElementById("truck_vol_mmbtu"+l_index).value;
	var truck_vol_mt = document.getElementById("truck_vol_mt"+l_index).value;

	var qty_mmbtu = document.getElementById("qty_mmbtu"+l_index).value;
	var qty_mt = document.getElementById("qty_mt"+l_index).value;

	var msg ="";
	var flag=true;
	
	if((qty_mt!=null && trim(qty_mt) !='') && (qty_mmbtu!=null && trim(qty_mmbtu) !=''))
	{
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
	} */
}

enableButton = true;
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
	var next_avail_hrs =document.forms[0].next_avail_hrs;

	var total_nom =document.forms[0].total_nom;
	var cont_tcq =document.forms[0].cont_tcq;
	
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
				if(trim(next_avail_hrs[i].value)=="")
				{
					msg+="Enter Next Avilable Hrs for ROW - "+parseInt(i+1)+"!\n";
					flag=false;
				}
				
				if(flag)
				{
					checkTCQ(qty_mmbtu[i],i,total_nom[i].value,cont_tcq[i].value);
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
			if(trim(next_avail_hrs.value)=="")
			{
				msg+="Enter Next Available Hrs for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			
			if(flag)
			{
				checkTCQ(qty_mmbtu,'0',total_nom.value,cont_tcq.value);
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

function checkTCQ(qty_mmbtu,index,cont_buyer_nom,cont_tcq)
{
	var cont_internal_mapp_id = document.forms[0].cont_internal_mapp_id;
	
	var tot_mmbtu =  parseFloat("0");
	var flag = true;
	
	tot_mmbtu = parseFloat(cont_buyer_nom) + parseFloat(qty_mmbtu.value);
	var allowedNom = parseFloat(cont_tcq) - parseFloat(cont_buyer_nom);
	
	if(parseFloat(tot_mmbtu) > parseFloat(cont_tcq))
	{
		alert("Effective Allocated MMBTU "+parseFloat(tot_mmbtu)+" is exceeding Contract TCQ : "+parseFloat(cont_tcq)+"");
		flag = false;
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_Dlng_ContractMgmt" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String prevdate = utildate.getPreviousDate();
String sysdate = utildate.getSysdate();

String gas_dt = request.getParameter("gas_dt")==null?sysdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "1");

cont_mgmt.setCallFlag("DAILY_ALLOCATION");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setGas_dt(gas_dt);
cont_mgmt.init();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = cont_mgmt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPATY_STATUS = cont_mgmt.getVCOUNTERPATY_STATUS();

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
Vector VCONT_TCQ = cont_mgmt.getVCONT_TCQ();

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
Vector VNEXT_AVAIL_HRS = cont_mgmt.getVNEXT_AVAIL_HRS();
 
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

Vector VCONT_BUYER_NOM =  cont_mgmt.getVCONT_BUYER_NOM();
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
									<label class="form-label"><b>Loading Day</b></label>
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
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VTITTLE_DISP_CONT_NO.elementAt(i)%></label>
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
								
								String gcv="9802.80";
								String ncv="8831.35";
							%>
								<input type="hidden" name="sub_index" value="<%=sub_index%>">
								<input type="hidden" name="trans_abbr" value="<%=VCONT_BU_PLANT_MAP.elementAt(j)%>">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading<%=j%>">
    										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="false" aria-controls="collapse<%=j%>">
								    			<font>Truck Loading : <%=VCONT_BU_PLANT_MAP.elementAt(j)%></font>
								    			<%if(Double.parseDouble(""+VCONT_PLANT_WISE_TOTAL_MMBTU.elementAt(j)) > 0){%>
								    				&nbsp;<font color="<%=VTOTAL_MMBTU_COLOR.elementAt(j)%>" style="background: white;padding: 2px 5px 4px 5px;border-radius: 30px;">
								    					[Scheduled : <%=VCONT_PLANT_WISE_TOTAL_MMBTU.elementAt(j)%> MMBTU] [Balance : <%=VCONT_PLANT_WISE_TOTAL_SCM.elementAt(j)%> MMBTU]
								    				</font>
								    			<%} %>
								      		</button>
								    	</h2>
								    	<div id="collapse<%=j%>" class="accordion-collapse collapse" aria-labelledby="heading<%=j%>">
								      		<div class="accordion-body accor-body">
								        		<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover" id="filterbysearch_<%=j%>">
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
																	<th rowspan="2">Contract#<br>[Contract Ref#]<div align="center"><input class="form-control form-control-sm" type="text" id="contNum_<%=j%>" onkeyup="Search(this,'1','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
																	<th rowspan="2">Business Unit<div align="center"><input class="form-control form-control-sm" type="text" id="buUnit_<%=j%>" onkeyup="Search(this,'2','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
																	<!-- <th rowspan="2">Customer Plant</th> -->
																	<th rowspan="2">Truck#<div align="center"><input class="form-control form-control-sm" type="text" id="truckReg_<%=j%>" onkeyup="Search(this,'3','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
																	<th colspan="3">Truck Capacity</th>
																	<th rowspan="2">Seller Nomination (Rev)<br>(MMBTU)</th>
																	<th colspan="3">Truck Loaded Qty</th>
																	<th rowspan="2">Filling Station Association<div align="center"><input class="form-control form-control-sm" type="text" id="fillSt_<%=j%>" onkeyup="Search(this,'11','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
																	<th colspan="2">Arrival</th>
																	<th rowspan="2">Next Available<br>(In Hrs)</th>
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
																				<input type="radio" class="form-check-input" name="chk" id="chk<%=l%>" 
																				onclick="setEnableDisabled(this,'<%=l%>');
																				" <%--<%if(VNOM_COLOR.elementAt(l).equals("")) {%>calculateMMBTU('<%=j %>','<%=l%>');<%} %> --%>
																				<%if(VNOM_BLOCK.elementAt(l).equals("Y") || VCOUNTERPATY_STATUS.elementAt(i).equals("N")) {%>disabled style="pointer-events: none;"<%} %>>
																				<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=l%>" value="<%=VCOUNTERPARTY_CD.elementAt(l)%>" disabled>
																				<input type="hidden" name="agmt_no" id="agmt_no<%=l%>" value="<%=VAGMT_NO.elementAt(l)%>" disabled>
						      													<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=l%>" value="<%=VAGMT_REV_NO.elementAt(l)%>" disabled>
															      				<input type="hidden" name="cont_no" id="cont_no<%=l%>" value="<%=VCONT_NO.elementAt(l)%>" disabled>
															      				<input type="hidden" name="cargo_no" id="cargo_no<%=l%>" value="<%=VCARGO_NO.elementAt(l)%>" disabled>
															      				<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=l%>" value="<%=VCONT_REV_NO.elementAt(l)%>" disabled>
															      				<input type="hidden" name="contract_type" id="contract_type<%=l%>" value="<%=VCONTRACT_TYPE.elementAt(l)%>" disabled>
															      				<input type="hidden" name="plant_seq" id="plant_seq<%=l%>" value="<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(l)%>" disabled>
															      				<input type="hidden" name="bu_plant_seq" id="bu_plant_seq<%=l%>" value="<%=VBU_PLANT_SEQ.elementAt(l)%>" disabled>
															      				<input type="hidden" name="truck_trans_cd" id="truck_trans_cd<%=l%>" value="<%=VTRUCK_TRANS_CD.elementAt(l)%>" disabled>
															      				<input type="hidden" name="truck_cd" id="truck_cd<%=l%>" value="<%=VTRUCK_CD.elementAt(l)%>" disabled>
															      				<input type="hidden" name="internal_map_id" id="internal_map_id<%=l%>" value="<%=VINTERNAL_MAP_ID.elementAt(l)%>">
																				<input type="hidden" name="is_exist" id="is_exist<%=l%>" value="<%=VIS_EXIST.elementAt(l)%>">
																				<input type="hidden" name="nom_block" id="nom_block<%=l%>" value="<%=VNOM_BLOCK.elementAt(l)%>">
																				<input type="hidden" name="index1" id="index1<%=l%>" value="<%//=index1%>" disabled>
																				<input type="hidden" name="index" id="index_<%=l%>" value="<%=l%>" disabled>
																				<input type="hidden" name="total_nom" id="total_nom_<%=l%>" value="<%=VCONT_BUYER_NOM.elementAt(l)%>">
																				<input type="hidden" name="cont_tcq" id="cont_tcq_<%=l%>" value="<%=VCONT_TCQ.elementAt(l)%>">
																			</td>
																			<td align="center">
																				<%=VCONT_NAME.elementAt(l)%><br>
																				(<%=VCONT_REF.elementAt(l)%>)
																			</td>
																			<td align="center">
																				<%=VBU_PLANT_ABBR.elementAt(l)%>
																			</td>
																			<%-- <td align="center">
																				<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%>
																			</td> --%>
																			<td align="center">
																				<%=VTRUCK_REG_NUM.elementAt(l)%>
																			</td>
																			<td align="right">
																				<%=VTRUCK_VOL_M3.elementAt(l)%>
																			</td>
																			<td align="right">
																				<%=VTRUCK_VOL_MT.elementAt(l)%>
																				<input type="hidden" name="truck_vol_mt" id="truck_vol_mt<%=l%>" value="<%=VTRUCK_VOL_MT.elementAt(l)%>">
																			</td>
																			<td align="right">
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
																					onchange="calculateMMBTU('<%=j %>','<%=l%>');checkTruckCap('<%=j %>','<%=l%>');checkNumber1(this,10,2);"
																					onblur="calculateMMBTU('<%=j %>','<%=l%>');checkTruckCap('<%=j %>','<%=l%>');checkNumber1(this,10,2);">
																					<input type="hidden" name="gcv" id="gcv<%=l%>" value="<%=VGCV.elementAt(l)%>" disabled>
																					<input type="hidden" name="ncv" id="ncv<%=l%>" value="<%=VNCV.elementAt(l)%>" disabled>
																					<input type="hidden" name="base" id="base<%=l%>" value="<%=VBASE.elementAt(l)%>" disabled>
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="gcv_mmbtu" id="gcv_mmbtu<%=l%>" value="<%=VGCV_MMBTU.elementAt(l)%>" style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" disabled 
																					onchange="calculateMMBTU('<%=j %>','<%=l%>');checkTruckCap('<%=j %>','<%=l%>');checkNumber1(this,17,12);"
																					onblur="calculateMMBTU('<%=j %>','<%=l%>');checkTruckCap('<%=j %>','<%=l%>');checkNumber1(this,17,12);">
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="qty_mmbtu" id="qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" disabled 
																					onchange="checkTruckCap('<%=j %>','<%=l%>');checkNumber1(this,10,2);checkTCQ(this,'<%=l%>','<%=VCONT_BUYER_NOM.elementAt(l)%>','<%=VCONT_TCQ.elementAt(l)%>');"
																					onblur="checkTruckCap('<%=j %>','<%=l%>');checkNumber1(this,10,2);">
																				</div>
																			</td>
																			<td align="center">
																				<%=VFILL_STATION_ABBR.elementAt(l)%> [<%=VBAY_NM.elementAt(l)%>]<br><%=VDISP_SLOT_DTL.elementAt(l)%>
																				<input type="hidden" name=filling_station id="filling_station_<%=l%>" value="<%=VFILL_STATION_CD.elementAt(l)%>" disabled>
																				<input type="hidden" name="bay_cd" id="bay_cd_<%=l%>" value="<%=VBAY_CD.elementAt(l)%>" disabled>
																				<input type="hidden" name="slot_start_time" id="slot_start_time_<%=l%>" value="<%=VSLOT_START_TIME.elementAt(l)%>" disabled>
																				<input type="hidden" name="slot_end_time" id="slot_end_time_<%=l%>" value="<%=VSLOT_END_TIME.elementAt(l)%>" disabled>
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
																		      					disabled>
																		      					<%-- onchange="validateDate(this);validateLoadTimes({
																									    l_index: <%=l %>,
																									    vslotStartTime: '<%= VSLOT_START_TIME.elementAt(l) %>',
																									    vslotEndTime: '<%= VSLOT_END_TIME.elementAt(l) %>',
																									    varrivalDate: '<%= VARRIVAL_DT.elementAt(l) %>'
																									});"  --%>
																		      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
																		      				</div>
																	      				</div>
																	      				<div class="col">
																		      				<div class="input-group input-group-sm" >
																	      						<input type="text" class="form-control form-control-sm" name="load_start_time" id="load_start_time<%=l%>" value="<%=VLOAD_START_TIME.elementAt(l)%>" maxLength="5" 
																	      						style="background:<%=VNOM_COLOR.elementAt(l)%>"
																	      						onblur="validateTime(this);" 
																		      					 autocomplete="off"  disabled>
																		      					 <%-- onchange="validateTime(this);validateLoadTimes({
																									    l_index: <%=l %>,
																									    vslotStartTime: '<%= VSLOT_START_TIME.elementAt(l) %>',
																									    vslotEndTime: '<%= VSLOT_END_TIME.elementAt(l) %>',
																									    varrivalDate: '<%= VARRIVAL_DT.elementAt(l) %>'
																									});" --%>
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
																									 disabled>
																									 <%-- onchange="validateDate(this);validateLoadTimes({
																									    l_index: <%=l %>,
																									    vslotStartTime: '<%= VSLOT_START_TIME.elementAt(l) %>',
																									    vslotEndTime: '<%= VSLOT_END_TIME.elementAt(l) %>',
																									    varrivalDate: '<%= VARRIVAL_DT.elementAt(l) %>'
																									});" --%>
																		      					<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
																		      				</div>
																	      				</div>
																	      				<div class="col">
																		      				<div class="input-group input-group-sm" >
																	      						<input type="text" class="form-control form-control-sm" name="load_end_time" id="load_end_time<%=l%>" value="<%=VLOAD_END_TIME.elementAt(l)%>" maxLength="5" 
																	      						style="background:<%=VNOM_COLOR.elementAt(l)%>"
																	      						onblur="validateTime(this);" 
																		      					autocomplete="off"  disabled>
																		      					<%-- onchange="validateTime(this);validateLoadTimes({
																									    l_index: <%=l %>,
																									    vslotStartTime: '<%= VSLOT_START_TIME.elementAt(l) %>',
																									    vslotEndTime: '<%= VSLOT_END_TIME.elementAt(l) %>',
																									    varrivalDate: '<%= VARRIVAL_DT.elementAt(l) %>'
																									});"  --%>
																	      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
																      						</div>
															      						</div>
															      					</div>
															      				</div>
																			</td>
																			<td align="center">	
																				<div class="input-group input-group-sm" >
																					<input type="text" class="form-control form-control-sm" name="next_avail_hrs" id="next_avail_hrs<%=l%>"  value="<%=VNEXT_AVAIL_HRS.elementAt(l)%>"  maxlength="3"
																					onchange="checkNextAvailHrs(this);"
																					disabled style="background:<%=VNOM_COLOR.elementAt(l)%>" readonly="readonly">
																					<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
																				</div>
																			</td>
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
																				<td colspan="10" align="right">
																					<b>Total (<%=VCONT_BU_PLANT_MAP.elementAt(j)%>)</b>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_qty_mmbtu" value="<%=VCONT_PLANT_WISE_TOTAL_MMBTU.elementAt(j)%>" style="text-align:right;font-weight: bold;" readOnly>
																					</div>
																				</td>
																				<td align="right" colspan="7">
																				</td>
																			</tr>
																			<tr>
																				<td colspan="10" align="right">
																					<b>Balance (<%=VCONT_BU_PLANT_MAP.elementAt(j)%>)</b>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="blnc_qty_mmbtu" id="blnc_qty_mmbtu_<%=j%>" value="<%=VCONT_PLANT_WISE_TOTAL_SCM.elementAt(j)%>" style="text-align:right;font-weight: bold;" readOnly>
																					</div>
																				</td>
																				<td align="right" colspan="7">
																				</td>
																			</tr> --%>
																			<%l=l+1;
																			break;
																		}%>
																	<%} %>
																<%}else{ %>
																	<tr>
																		<td colspan ="17">
																			<div align="center"><%=utilmsg.infoMessage("<b>Seller Nomination is Pending for "+VCONT_BU_PLANT_MAP.elementAt(j)+" on Selected Gas Day!</b>") %></div>
																		</td>
																	</tr>
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
function Search(obj, indx, l) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch_"+l);
  	
  	tr = table.getElementsByTagName("tr");
  	
  	for (i = 3; i < tr.length; i++) 
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