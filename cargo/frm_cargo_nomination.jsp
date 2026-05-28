<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh(opration)
{
	var u = document.forms[0].u.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var url = "frm_cargo_nomination.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function splitBLorNot()
{
	const checkbox = document.getElementById('BL_Checkbox');
	const add_bl_model = document.getElementById('add_bl_model');
	const no_of_split_bl = document.getElementById('no_of_split_bl');

	var bl_number = document.forms[0].bl_number.value;
	
	if (checkbox.checked) 
	{
		no_of_split_bl.style.display= 'block';
		add_bl_model.style.display= 'block';
		document.getElementById('BL_Checkbox').value="Y";
	} 
	else 
	{
		no_of_split_bl.style.display= 'none';
		
		if(parseInt(bl_number)==1)
		{
			add_bl_model.style.display= 'none';
		}
	}
}

function doSubmit()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var cargo_number = document.forms[0].cargo_number.value;
	var ship_cd = document.forms[0].ship_cd.value;
	var man_req_to_dt = document.forms[0].man_req_to_dt.value;
	var man_req_from_dt = document.forms[0].man_req_from_dt.value;
	
	var opration = document.forms[0].opration.value;
	
	var msg="";
	var flag=true;
	
	if(counterparty_cd == "" || counterparty_cd == "0")
	{
		msg+="Select Counterparty!\n";
		flag=false;
	}
	if(cargo_number == "" || cargo_number == null)
	{
		msg+="Select Cargo Details!\n";
		flag=false;
	}
	if(ship_cd == "" || ship_cd == "0")
	{
		msg+="Select Vessel Details!\n";
		flag=false;
	}
	if(trim(man_req_from_dt) == "")
	{
		msg+="Enter Request Window From Date!\n";
		flag=false;
	}
	if(trim(man_req_to_dt) == "")
	{
		msg+="Enter Request Window To Date!\n";
		flag=false;
	}

	if(flag)
	{
		var a;
		if(opration=="MODIFY")
		{
			a = confirm("Do you want to Modify Cargo Nomination?");
		}
		else
		{
			a = confirm("Do you want to Submit Cargo Nomination?");
			
		}
		
		if(a)
		{
			var temp_msg="";
			if(opration!="MODIFY")
			{				
				temp_msg += "Please add Bill of Entry(BOE) after Submitting Nomination!\n";						
				temp_msg += "Please add Bill of Lading(BL) after Submitting Nomination!\n";
			}
			
			if(trim(temp_msg) != "")
			{
				alert(temp_msg)
			}
			
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function checkValuePrecision(value,precision,maxVal,id)
{
	try
	{
		if(value == "" || value == null)
		{
			
		}
		else
		{
			var number = parseFloat(value);
	        if (isNaN(number))
	        {
	            alert("Value is not a valid number!!");
	            
	            document.getElementById(id).value = "";
	        }
	        else
	        {
	        	if(value.includes('.'))
				{
					var parts = value.split('.');
					var integerPart = parts[0];
					var decimalPart = parts[1] || '';
					 
					
					if (integerPart.length <= maxVal)
					{
						//return true;
					}
					else
					{
						alert("Provided Value should be in ("+maxVal+", "+precision+") format!!");
						document.getElementById(id).value = "";
						//return false;
					}
					
					if (decimalPart.length <= precision)
					{
						//return true;
					}
					else
					{
						alert("Provided Value should be in ("+maxVal+", "+precision+") format!!");
						document.getElementById(id).value = "";
					}
				}
				else
				{
					if (value.length <= maxVal)
					 {
						 //return true;
					 }
					 else
					 {
						 alert("Provided Value should be in ("+maxVal+", "+precision+") format!!");
						 document.getElementById(id).value = "";
					 }
				}
	        }
		}
    }
	catch (error)
	{
        //alert(error);
        return false;
    }
}

var newWindow;

function cargoDtl(counterparty_cd, agmt_type, agmt_no, contract_type, cont_no, cargo_no, ship_cd, opration)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_cargo_nomination.jsp?counterparty_cd="+counterparty_cd+"&agmt_type="+agmt_type+"&agmt_no="+agmt_no+
			"&contract_type="+contract_type+"&confirm_no="+cont_no+"&cargo_number="+cargo_no+"&ship_cd="+ship_cd+
			"&opration="+opration+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function shipDtl(ship_cd,cargo_no,cont_no,contract_type,counterparty_cd)
{
	var opration = document.forms[0].opration.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_cargo_nomination.jsp?ship_cd="+ship_cd+"&cargo_number="+cargo_no+"&confirm_no="+cont_no+"&opration="+opration+
	"&u="+u+"&counterparty_cd="+counterparty_cd;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function openCargoList(operation)
{
	var u = document.forms[0].u.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var url = "frm_cargo_list.jsp?operation="+operation+"&counterparty_cd="+counterparty_cd+"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Cargo List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Cargo List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}	
}

function openShipList(cargo_no,cont_no,contract_type)
{
	var u = document.forms[0].u.value;
	var opration = document.forms[0].opration.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var url = "frm_cargo_ship_list.jsp?cargo_number="+cargo_no+"&confirm_no="+cont_no+"&opration="+opration+
	"&u="+u+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Ship List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Ship List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}	
}

function doSubmitBl()
{
	var bl_qty = document.forms[0].bl_qty.value;
	var bl_qty_mt = document.forms[0].bl_qty_mt.value;
	var bl_qty_scm = document.forms[0].bl_qty_scm.value;
	var bl_price = document.forms[0].bl_price.value;
	var bl_price_unit = document.forms[0].bl_price_unit.value;
	var opration = document.forms[0].opration.value;
	var bl_opration = document.forms[0].bl_opration.value;
	
	document.forms[0].billing_opration.value = "BL_UPDATE";
	
	var msg="";
	var flag=true;
	
	if(trim(bl_qty)=="" || bl_qty==0 || trim(bl_qty_scm)=="" || bl_qty_scm==0 || trim(bl_qty_mt)=="" || bl_qty_mt==0)
	{
		msg+="Please Enter BL MMBTU/MT/SCM!\n";
		flag=false;
	}
	if(trim(bl_price)=="" || bl_price==0)
	{
		msg+="Please Enter BL Price!\n";
		flag=false;
	}
	if(trim(bl_price_unit)=="" || bl_price_unit==0)
	{
		msg+="Please Select BL Price Unit!\n";
		flag=false;
	}
	
	if(flag)
	{
		if(bl_opration == 'BL_MOD')
		{
			var a = confirm("Do you want to Modify the BL Details?");
			if(a)
			{
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].submit();
			}
		}
		else if(bl_opration == 'BL_INS')
		{
			var a = confirm("Do you want to Insert the BL Details?");
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

function doSubmitBoe()
{
	var business_unit = document.forms[0].bu_seq.value;
	var trader_plant = document.forms[0].plant_seq.value;
	var boe_qty = document.forms[0].boe_qty.value;
	var boe_qty_mt = document.forms[0].boe_qty_mt.value;
	var boe_qty_scm = document.forms[0].boe_qty_scm.value;
	var boe_price = document.forms[0].boe_price.value;
	var boe_price_unit = document.forms[0].boe_price_unit.value;
	var opration = document.forms[0].opration.value;
	
	var custDuty_flag = document.forms[0].custDuty_flag.value;
	
	document.forms[0].billing_opration.value = "BOE_UPDATE";
	
	var boe_opration = document.forms[0].boe_opration.value;
	var linked_bl = document.getElementById("linked_bl");
	
	var msg="";
	var flag=true;
	
	if(trim(business_unit)=="" || business_unit=="")
	{
		msg+="Please Select Business Unit!\n";
		flag=false;
	}
	if(trim(trader_plant)=="" || trader_plant=="")
	{
		msg+="Please Select Trader Plant!\n";
		flag=false;
	}
	if(trim(boe_qty)=="" || boe_qty==0 || trim(boe_qty_mt)=="" || boe_qty_mt==0 || trim(boe_qty_scm)=="" || boe_qty_scm==0)
	{
		msg+="Please Enter BOE Quantity MMBTU/MT/SCM!\n";
		flag=false;
	}
	if(trim(boe_price)=="" || boe_price==0)
	{
		msg+="Please Enter BOE Price!\n";
		flag=false;
	}
	if(trim(boe_price_unit)=="" || boe_price_unit==0)
	{
		msg+="Please Select BOE Price Unit!\n";
		flag=false;
	}
	
	var port_code = document.forms[0].port_code.value;
		
	if(port_code == "" || port_code==0)
	{
		msg+="Please Select Port Code!\n";
		flag=false;
	}

	if (linked_bl.selectedIndex === -1)
	{
		msg+="Please Select BL/s from Link BL!\n";
	}
	
	if(flag)
	{
		if(boe_opration == 'BOE_MOD')
		{
			var a = confirm("Do you want to modify the BOE Details?");
			
			if(a)
			{
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].submit();
			}
		}
		else if(boe_opration == 'BOE_INS')
		{
			var a = confirm("Do you want to insert the BOE Details?");
			
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

function detailsAlert(takenAction)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var cargo_number = document.forms[0].cargo_number.value;
	
	if(counterparty_cd == '0')
	{
		alert("Please select Counterparty to Add "+takenAction+" Details!!");
	}
	else
	{
		if(cargo_number=='')
		{
			alert("Please select Cargo to Add "+takenAction+" Details!!");
		}
	}
}

function doModifyBoe(VBOE_NO,VBOE_BU_SEQ,VBOE_PLANT_SEQ,VBOE_QTY,VBOE_QTY_UNIT,VBOE_PRICE,VBOE_PRICE_UNIT,VBOE_CUSTOM_DUTY,VBOE_LOAD_PORT,VBOE_LINKED_BL,VBL_AVAIL_FOR_ALL,VBOE_QTY_MT,VBOE_QTY_SCM)
{
	document.getElementById("BOE_MODEL").innerHTML = "Modify BOE Details";
	
	document.forms[0].disp_boe_no.value=VBOE_NO;
	document.forms[0].boe_no.value=VBOE_NO;
	document.forms[0].bu_seq.value=VBOE_BU_SEQ;
	document.forms[0].plant_seq.value=VBOE_PLANT_SEQ;
	document.forms[0].boe_qty.value=VBOE_QTY;
	//document.forms[0].boe_qty_unit.value=VBOE_QTY_UNIT;
	document.forms[0].boe_price.value=VBOE_PRICE;
	document.forms[0].boe_price_unit.value=VBOE_PRICE_UNIT;
	document.forms[0].boe_qty_mt.value=VBOE_QTY_MT;
	document.forms[0].boe_qty_scm.value=VBOE_QTY_SCM;
	
	if(VBOE_CUSTOM_DUTY=="Y")
	{
		document.forms[0].custDuty_flag.value="Y";
	}
	else if(VBOE_CUSTOM_DUTY=="N")
	{
		document.forms[0].custDuty_flag.value="N";
	}
	else
	{
		document.forms[0].custDuty_flag.value="";
	}
	
	document.forms[0].port_code.value=VBOE_LOAD_PORT;
   
	var dropdown = document.getElementById('linked_bl');
	
    for (var i = 0; i < dropdown.options.length; i++)
    {
        dropdown.options[i].selected = false;
    }
    
	var availableValues = VBL_AVAIL_FOR_ALL.replace(/^\[|\]$/g, '').split(',').map(function(item)
	{
        return item.trim().replace(/^'|'$/g, '');
    });
	
	Array.from(dropdown.options).forEach(function(option)
	{
		option.style.display = 'none';

		if (availableValues.includes(option.value))
		{
			option.style.display = 'block';
	    }
	});

	var selectedValuesArray = VBOE_LINKED_BL.split('@');

	selectedValuesArray.forEach(function(number)
	{
		var optionValue = "BL" + number.padStart(2, '0');

		var option = dropdown.querySelector('option[value="' + optionValue + '"]');

		if (option) 
		{
			option.selected = true;
			option.style.display = 'block';
	    }
	});
	
	document.forms[0].boe_opration.value="BOE_MOD";
}

function doModifyBl(VBL_NO,VBL_QTY,VBL_PRICE,VBL_PRICE_UNIT,VBL_QTY_MT,VBL_QTY_SCM)
{
	document.forms[0].disp_bl_no.value=VBL_NO;
	document.forms[0].bl_no.value=VBL_NO;
	document.forms[0].bl_qty.value=VBL_QTY;
	document.forms[0].bl_price.value=VBL_PRICE;
	document.forms[0].bl_price_unit.value=VBL_PRICE_UNIT;
	document.forms[0].bl_qty_mt.value=VBL_QTY_MT;
	document.forms[0].bl_qty_scm.value=VBL_QTY_SCM;
	document.forms[0].temp_bl_qty.value=VBL_QTY;
	
	document.forms[0].bl_opration.value="BL_MOD";
	
	document.getElementById("BL_MODEL").innerHTML = "Modify Bill Of Lading Document Details";
}

function doClearBl()
{
	document.getElementById("BL_MODEL").innerHTML = "Add New Bill Of Lading Document Details";
	
	document.forms[0].disp_bl_no.value=document.forms[0].temp_disp_bl_no.value;
	document.forms[0].bl_no.value="";
	document.forms[0].bl_qty.value="";
	document.forms[0].bl_price.value="";
	document.forms[0].bl_price_unit.value="2";
	document.forms[0].bl_qty_mt.value="";
	document.forms[0].bl_qty_scm.value="";
	
	document.forms[0].bl_opration.value="BL_INS";
}

function doClearBoe(VBL_AVAIL_FOR_ALL)
{
	document.getElementById("BOE_MODEL").innerHTML = "Add New BOE Details";
	
	document.forms[0].disp_boe_no.value=document.forms[0].temp_disp_boe_no.value;
	document.forms[0].boe_no.value="";
	document.forms[0].bu_seq.value="";
	document.forms[0].plant_seq.value="";
	document.forms[0].boe_qty.value="";
	//document.forms[0].boe_qty_unit.value="";
	document.forms[0].boe_price.value="";
	document.forms[0].boe_price_unit.value="2";
	document.forms[0].boe_qty_mt.value="";
	document.forms[0].boe_qty_scm.value="";
	
	document.forms[0].custDuty_flag.value="";
	
	document.forms[0].port_code.value="";
	
	var dropdown = document.getElementById('linked_bl');
	
    for (var i = 0; i < dropdown.options.length; i++)
    {
        dropdown.options[i].selected = false;
    }
    
    var availableValues = VBL_AVAIL_FOR_ALL.replace(/^\[|\]$/g, '').split(',').map(function(item)
	{
        return item.trim().replace(/^'|'$/g, '');
    });
	
	Array.from(dropdown.options).forEach(function(option)
	{
		option.style.display = 'none';

	    if (availableValues.includes(option.value))
	    {
	    	option.style.display = 'block';
	    }
	});
	
	document.forms[0].boe_opration.value="BOE_INS";
}

function exceedingBlDtl()
{
	var bl_number = document.forms[0].bl_number.value;
	
	var maxBLalw = "99";
	
	if(parseInt(bl_number) >= parseInt(maxBLalw))
	{
		alert("Bill of Lading Document Details for cargo can not exceed "+maxBLalw+" !!");
	}
}

function textCounter(field, countfield, maxlimit)
{
	if(field.value.length > maxlimit)
	{
		field.value = field.value.substring(0, maxlimit);
	}
	else
	{
		countfield.value = maxlimit - field.value.length;
	}
}

function setLinked_BL()
{
    var selectedOptions = document.getElementById('linked_bl').selectedOptions;
    var selectedValues = [];
    
    for (var i = 0; i < selectedOptions.length; i++)
    {
    	var value = selectedOptions[i].value;
        var numericValue = value.replace('BL', '');
        selectedValues.push(numericValue);
    }
    
    var selectedString = selectedValues.join('@');
    
    document.forms[0].sel_bl_list.value = selectedString;
}

function calcAllowedBlMMBTU(entVolume)
{
	var bl_opration = document.forms[0].bl_opration.value;
	var temp_bl_qty = parseFloat(document.forms[0].temp_bl_qty.value);
	
	var totalBlMMBTU = parseFloat(document.forms[0].totalBlMMBTU.value);
    var mandate_conf_vol = parseFloat(document.forms[0].mandate_conf_vol.value);
    var mandate_conf_vol_tol = parseFloat(document.forms[0].mandate_conf_vol_tol.value);
    
    var eff_mandate_conf_vol = mandate_conf_vol + ((mandate_conf_vol * mandate_conf_vol_tol) / 100);
    
    var temp_total = totalBlMMBTU + parseFloat(entVolume);
	
	if(bl_opration == 'BL_MOD')
	{
		var temp_available_quantity = totalBlMMBTU - parseFloat(temp_bl_qty);
		var exceed_total = temp_available_quantity + parseFloat(entVolume);
		
		var allowedBlMMBTU = eff_mandate_conf_vol - temp_available_quantity;
		
		if (entVolume > allowedBlMMBTU)
		{
			var alertMessage = "Sum of all BL Quantity ("+exceed_total+" MMBTU) entered > Max Confirmed Volume ("+eff_mandate_conf_vol+" MMBTU) Tolerance!\n"
			alert(alertMessage);
		    
		    //document.forms[0].bl_qty.value = "";
		}		
	}
	else if(bl_opration == 'BL_INS')
	{
		var allowedBlMMBTU = eff_mandate_conf_vol - totalBlMMBTU;
		
		if (entVolume > allowedBlMMBTU)
		{
			var alertMessage = "Sum of all BL Quantity ("+temp_total+" MMBTU) entered > Max Confirmed Volume ("+eff_mandate_conf_vol+" MMBTU) Tolerance!\n"
			alert(alertMessage);
		    
		    //rdocument.forms[0].bl_qty.value = "";
		}
	}
}

function setBOEQuantMtasBL(select,action)
{
var joint_bl_no_quant = document.forms[0].joint_bl_no_mt.value;
	
	var startIndex = joint_bl_no_quant.indexOf("[");
    var endIndex = joint_bl_no_quant.indexOf("]");
    var blNumbersStr = joint_bl_no_quant.substring(startIndex + 1, endIndex);
    var quantitiesStr = joint_bl_no_quant.substring(endIndex + 2, joint_bl_no_quant.length - 1);

    var blArray = blNumbersStr.split(", ");
    var qtyArray = quantitiesStr.split(", ");

    var combinedValues = [];

    var sum = 0;
    
    // Combine values
    for (var i = 0; i < blArray.length; i++)
    {
    	var combined = blArray[i] + "#" + qtyArray[i];
        combinedValues.push(combined);
    }
    
    var combBLandQuant = combinedValues.join(", ");

    var selectedOptions = select.selectedOptions;
    for (var i = 0; i < selectedOptions.length; i++)
    {
        var selectedOption = selectedOptions[i];
        var optionId = selectedOption.id;

        var regex = new RegExp(optionId + '#(\\d+\\.?\\d*)', 'g');
        var matches = combBLandQuant.match(regex);
        
        if (matches)
        {
            for (var j = 0; j < matches.length; j++)
            {
                var qty = parseFloat(matches[j].split('#')[1]);
                sum += qty;
            }
        }
        else
        {
        	sum += 0;
        }
    }

    document.forms[0].total_sel_bl_mt.value=sum;
    
    if(action == "ch" && sum!=0)
    {
    	document.forms[0].boe_qty_mt.value=sum;
    }
}

function setBOEQuantScmasBL(select,action)
{
var joint_bl_no_quant = document.forms[0].joint_bl_no_scm.value;
	
	var startIndex = joint_bl_no_quant.indexOf("[");
    var endIndex = joint_bl_no_quant.indexOf("]");
    var blNumbersStr = joint_bl_no_quant.substring(startIndex + 1, endIndex);
    var quantitiesStr = joint_bl_no_quant.substring(endIndex + 2, joint_bl_no_quant.length - 1);

    var blArray = blNumbersStr.split(", ");
    var qtyArray = quantitiesStr.split(", ");

    var combinedValues = [];

    var sum = 0;
    
    // Combine values
    for (var i = 0; i < blArray.length; i++)
    {
    	var combined = blArray[i] + "#" + qtyArray[i];
        combinedValues.push(combined);
    }
    
    var combBLandQuant = combinedValues.join(", ");

    var selectedOptions = select.selectedOptions;
    for (var i = 0; i < selectedOptions.length; i++)
    {
        var selectedOption = selectedOptions[i];
        var optionId = selectedOption.id;

        var regex = new RegExp(optionId + '#(\\d+\\.?\\d*)', 'g');
        var matches = combBLandQuant.match(regex);
        
        if (matches)
        {
            for (var j = 0; j < matches.length; j++)
            {
                var qty = parseFloat(matches[j].split('#')[1]);
                sum += qty;
            }
        }
        else
        {
        	sum += 0;
        }
    }

    document.forms[0].total_sel_bl_scm.value=sum;
    
    if(action == "ch" && sum!=0)
    {
    	document.forms[0].boe_qty_scm.value=sum;
    }
}

function setBOEQuantasBL(select,action)
{
	var joint_bl_no_quant = document.forms[0].joint_bl_no_quant.value;
	
	var startIndex = joint_bl_no_quant.indexOf("[");
    var endIndex = joint_bl_no_quant.indexOf("]");
    var blNumbersStr = joint_bl_no_quant.substring(startIndex + 1, endIndex);
    var quantitiesStr = joint_bl_no_quant.substring(endIndex + 2, joint_bl_no_quant.length - 1);

    var blArray = blNumbersStr.split(", ");
    var qtyArray = quantitiesStr.split(", ");

    var combinedValues = [];

    var sum = 0;
    
    // Combine values
    for (var i = 0; i < blArray.length; i++)
    {
    	var combined = blArray[i] + "#" + qtyArray[i];
        combinedValues.push(combined);
    }
    
    var combBLandQuant = combinedValues.join(", ");

    var selectedOptions = select.selectedOptions;
    for (var i = 0; i < selectedOptions.length; i++)
    {
        var selectedOption = selectedOptions[i];
        var optionId = selectedOption.id;

        var regex = new RegExp(optionId + '#(\\d+\\.?\\d*)', 'g');
        var matches = combBLandQuant.match(regex);
        
        if (matches)
        {
            for (var j = 0; j < matches.length; j++)
            {
                var qty = parseFloat(matches[j].split('#')[1]);
                sum += qty;
            }
        }
        else
        {
        	sum += 0;
        }
    }

    document.forms[0].total_sel_bl_quantity.value=sum;
    
    if(action == "ch")
    {
    	document.forms[0].boe_qty.value=sum;
    }
}

function setBOEPriceasBL(select,action)
{
	var joint_bl_no_price = document.forms[0].joint_bl_no_price.value;
	
	var startIndex = joint_bl_no_price.indexOf("[");
    var endIndex = joint_bl_no_price.indexOf("]");
    var blNumbersStr = joint_bl_no_price.substring(startIndex + 1, endIndex);
    var priceStr = joint_bl_no_price.substring(endIndex + 2, joint_bl_no_price.length - 1);

    var blArray = blNumbersStr.split(", ");
    var priceArray = priceStr.split(", ");

    var combinedValues = [];

    var sum = 0;
    var no_of_selVal = 0;
    // Combine values
    for (var i = 0; i < blArray.length; i++)
    {
    	var combined = blArray[i] + "#" + priceArray[i];
        combinedValues.push(combined);
    }
    
    var combBLandQuant = combinedValues.join(", ");

    var selectedOptions = select.selectedOptions;
    for (var i = 0; i < selectedOptions.length; i++)
    {
        var selectedOption = selectedOptions[i];
        var optionId = selectedOption.id;

        var regex = new RegExp(optionId + '#(\\d+\\.?\\d*)', 'g');
        var matches = combBLandQuant.match(regex);
        
        if (matches)
        {
            for (var j = 0; j < matches.length; j++)
            {
            	no_of_selVal+=1;
            	
                var price = parseFloat(matches[j].split('#')[1]);
                sum += price;
            }
        }
        else
        {
        	sum += 0;
        }
    }

    var avarage_price = (parseFloat(sum)/parseFloat(no_of_selVal)).toFixed(4);
    
    document.forms[0].total_sel_bl_price.value=avarage_price;

    if(avarage_price != "NaN")
    {
    	if(action == "ch")
   	    {
   	    	document.forms[0].boe_price.value=avarage_price;
   	    }
    }
    else
    {
    	if(action == "ch")
   	    {
   	    	document.forms[0].boe_price.value="";
   	    }
    }
}

function checkBOEquantLimit()
{
	var total_sel_bl_quantity = document.forms[0].total_sel_bl_quantity.value;
	var boe_qty = document.forms[0].boe_qty.value;
	
	if(parseFloat(boe_qty) > parseFloat(total_sel_bl_quantity))
	{
		alert("Entered BOE Quantity exceeds limit of "+total_sel_bl_quantity+" for selected BL/s!!");
		document.forms[0].boe_qty.value="";
	}
}

function openSvcContList(entity_type,man_req_from_dt,man_req_to_dt)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_cargo_nom_svc_cont.jsp?entity_type="+entity_type+"&cargo_startdt="+man_req_from_dt+"&cargo_enddt="+man_req_to_dt+"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Cargo Service Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Cargo Service Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setContractDetail(cont_full_no,entity_type,entity_name,ref_no,cont_map_no)
{
	if(entity_type == "V")
	{
		var disp_vessel_cont_dtl = document.forms[0].disp_vessel_cont_dtl.value =entity_name+"-"+cont_full_no+" ("+ref_no+")";
		var vessel_cont_dtl = document.forms[0].vessel_cont_dtl.value =cont_map_no;
		document.forms[0].disp_vessel_cont_dtl.style="background:#99ffcc";
		
		document.forms[0].vessel_cont_dtl.style="background:#99ffcc";
	}
	else if(entity_type == "H")
	{
		var disp_cha_cont_dtl = document.forms[0].disp_cha_cont_dtl.value=entity_name+"-"+cont_full_no+" ("+ref_no+")";
		var cha_cont_dtl = document.forms[0].cha_cont_dtl.value =cont_map_no;
		document.forms[0].disp_cha_cont_dtl.style="background:#99ffcc";
	}
	else if(entity_type == "S")
	{
		var disp_surveyor_cont_dtl = document.forms[0].disp_surveyor_cont_dtl.value=entity_name+"-"+cont_full_no+" ("+ref_no+")";
		var surveyor_cont_dtl = document.forms[0].surveyor_cont_dtl.value =cont_map_no;
		document.forms[0].disp_surveyor_cont_dtl.style="background:#99ffcc";
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.cargo.DataBean_Cargo_mst" id="dbcargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String dateTime = utildate.getSysdateWithTime24hr();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String ship_cd = request.getParameter("ship_cd")==null?"":request.getParameter("ship_cd");
String cargo_number = request.getParameter("cargo_number")==null?"":request.getParameter("cargo_number");
//String nom_status = request.getParameter("nom_status")==null?"":request.getParameter("nom_status");
String confirm_no = request.getParameter("confirm_no")==null?"":request.getParameter("confirm_no");
String clearance = request.getParameter("clearance")==null?"KYC":request.getParameter("clearance");

/* String cha_cont_dtl = request.getParameter("cha_cont_dtl")==null?"":request.getParameter("cha_cont_dtl");
String vessel_cont_dtl = request.getParameter("vessel_cont_dtl")==null?"":request.getParameter("vessel_cont_dtl");
String surveyor_cont_dtl = request.getParameter("surveyor_cont_dtl")==null?"":request.getParameter("surveyor_cont_dtl"); */

dbcargo.setCallFlag("CARGO_NOMINATION");
dbcargo.setCounterparty_cd(counterparty_cd);
dbcargo.setClearance(clearance);
dbcargo.setComp_cd(owner_cd);
dbcargo.setOpration(opration);
dbcargo.setCargo_number(cargo_number);
dbcargo.setConfirm_no(confirm_no);
dbcargo.setShip_cd(ship_cd);
dbcargo.init();

Vector VCOUNTERPARTY_CD = dbcargo.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcargo.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcargo.getVCOUNTERPARTY_ABBR();

Vector VCOUNTRY_NM = dbcargo.getVCOUNTRY_NM();
Vector VCOUNTRY_ISO = dbcargo.getVCOUNTRY_ISO();

Vector VBL_NO = dbcargo.getVBL_NO();
Vector VBL_QTY = dbcargo.getVBL_QTY();
Vector VBL_QTY_MT = dbcargo.getVBL_QTY_MT();
Vector VBL_QTY_SCM = dbcargo.getVBL_QTY_SCM();
Vector VBL_QTY_UNIT = dbcargo.getVBL_QTY_UNIT();
Vector VBL_PRICE = dbcargo.getVBL_PRICE();
Vector VBL_PRICE_UNIT = dbcargo.getVBL_PRICE_UNIT();
Vector VBL_AVAIL_FOR_ALL = dbcargo.getVBL_AVAIL_FOR_ALL();

Vector VBOE_NO = dbcargo.getVBOE_NO();
Vector VBOE_QTY = dbcargo.getVBOE_QTY();
Vector VBOE_QTY_MT = dbcargo.getVBOE_QTY_MT();
Vector VBOE_QTY_SCM = dbcargo.getVBOE_QTY_SCM();
Vector VBOE_QTY_UNIT = dbcargo.getVBOE_QTY_UNIT();
Vector VBOE_PRICE = dbcargo.getVBOE_PRICE();
Vector VBOE_PRICE_UNIT = dbcargo.getVBOE_PRICE_UNIT();
Vector VBOE_BU_SEQ = dbcargo.getVBOE_BU_SEQ();
Vector VBOE_PLANT_SEQ = dbcargo.getVBOE_PLANT_SEQ();
Vector VBOE_CUSTOM_DUTY = dbcargo.getVBOE_CUSTOM_DUTY();
Vector VBOE_LOAD_PORT = dbcargo.getVBOE_LOAD_PORT();
Vector VBOE_LINKED_BL = dbcargo.getVBOE_LINKED_BL();

Vector VBOE_BU_ABBR = dbcargo.getVBOE_BU_ABBR();
Vector VBOE_PLANT_NM = dbcargo.getVBOE_PLANT_NM();
Vector VBOE_PLANT_ABBR = dbcargo.getVBOE_PLANT_ABBR();

Vector VSEL_PLANT_SEQ_NO = dbcargo.getVSEL_PLANT_SEQ_NO();
Vector VSEL_SPLIT_VALUE = dbcargo.getVSEL_SPLIT_VALUE();
Vector VSEL_BU_CD = dbcargo.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = dbcargo.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_TRAD_CD = dbcargo.getVSEL_TRAD_CD();
Vector VSEL_PLANT_ABBR = dbcargo.getVSEL_PLANT_ABBR();
Vector VSEL_BU_PLANT_ABBR = dbcargo.getVSEL_BU_PLANT_ABBR();

Vector VOTH_SEL_TRAD_CD = dbcargo.getVOTH_SEL_TRAD_CD();
Vector VOTH_SEL_PLANT_SEQ_NO = dbcargo.getVOTH_SEL_PLANT_SEQ_NO();
Vector VOTH_SEL_PLANT_ABBR = dbcargo.getVOTH_SEL_PLANT_ABBR();
Vector VOTH_SEL_SPLIT_VALUE = dbcargo.getVOTH_SEL_SPLIT_VALUE();

Vector VBL_ACC_LIST = dbcargo.getVBL_ACC_LIST();
//VBL_ACC_LIST.addAll(VBL_AVAIL_FOR_ALL);
VBL_ACC_LIST.addAll(VBL_NO);

String signing_dt = dbcargo.getSigning_dt();
String ent_dt = dbcargo.getEnt_dt();
String ent_time = dbcargo.getEnt_time();
String start_dt = dbcargo.getStart_dt();
String end_dt = dbcargo.getEnd_dt();
String agmt_base = dbcargo.getAgmt_base();
String agmt_no = dbcargo.getAgmt_no();
String agmt_rev_no = dbcargo.getAgmt_rev_no();
String agmt_type = dbcargo.getAgmt_type();
String remark = dbcargo.getRemark();
String contpty_abbr = dbcargo.getContpty_abbr();
String cont_name = dbcargo.getCont_name();
String contract_type= dbcargo.getContract_type();
String cont_rev_no= dbcargo.getCont_rev_no();
String cont_ref_no = dbcargo.getCont_ref_no();
String cargo_ref = dbcargo.getCargo_ref();

String ship_name = dbcargo.getShip_name();
String ship_call_sign = dbcargo.getShip_call_sign();
String ship_flag = dbcargo.getShip_flag();
String ship_imo_no = dbcargo.getShip_imo_no();
String ship_class_soc = dbcargo.getShip_class_soc();
String inmarsat_no = dbcargo.getInmarsat_no();
String ship_owner_name = dbcargo.getShip_owner_name();
String ship_operator_name = dbcargo.getShip_operator_name();
String ship_fax_no = dbcargo.getShip_fax_no();
String ship_telex_no = dbcargo.getShip_telex_no();
String ship_email = dbcargo.getShip_email();
String gross_tonnage = dbcargo.getGross_tonnage();
String cargo_capacity = dbcargo.getCargo_capacity();
String volume_unit = dbcargo.getVolume_unit();
String percentage_capacity = dbcargo.getPercentage_capacity();
String ship_item = dbcargo.getShip_item();

String mandate_conf_vol = dbcargo.getMandate_conf_vol();
String mandate_conf_vol_tol = dbcargo.getMandate_conf_vol_tol();
String nom_status = dbcargo.getNom_status();
String nom_revision = dbcargo.getNom_revision();
String conf_price = dbcargo.getConf_price();
String conf_price_unit = dbcargo.getRate_unit();
String win_start_dt = dbcargo.getWin_start_dt();
String win_end_dt = dbcargo.getWin_end_dt();
String man_req_from_dt = dbcargo.getMan_req_from_dt();
String man_req_to_dt = dbcargo.getMan_req_to_dt();
String rate_unit = dbcargo.getRate_unit();
String allowed_lay_time_hrs = dbcargo.getAllowed_lay_time_hrs();
String allowed_lay_time_min = dbcargo.getAllowed_lay_time_min();
String demu_rate = dbcargo.getDemu_rate();
String demu_rate_unit = dbcargo.getDemu_rate_unit();
String country_origin =dbcargo.getCountry_origin();
String load_port =dbcargo.getLoad_port();
String num_bl =dbcargo.getNum_bl();
String num_boe =dbcargo.getNum_boe();
String liquefac_plant =dbcargo.getLiquefac_plant();
String liquefac_country =dbcargo.getLiquefac_country();
String liquefac_promotor =dbcargo.getLiquefac_promotor();
String liquefac_remark =dbcargo.getLiquefac_remark();
String split_bol =dbcargo.getSplit_bol();

String cha_cont_dtl = dbcargo.getCha_cont_dtl();
String vessel_cont_dtl = dbcargo.getVessel_cont_dtl();
String surveyor_cont_dtl = dbcargo.getSurveyor_cont_dtl();
String disp_cha_cont_dtl = dbcargo.getDisp_cha_cont_dtl();
String disp_vessel_cont_dtl = dbcargo.getDisp_vessel_cont_dtl();
String disp_surveyor_cont_dtl = dbcargo.getDisp_surveyor_cont_dtl();

double totalBlMMBTU =dbcargo.getTotalBlMMBTU();
double totalBlMT =dbcargo.getTotalBlMT();
double totalBlSCM =dbcargo.getTotalBlSCM();

double totalBOEMMBTU =dbcargo.getTotalBOEMMBTU();
double totalBOEMT =dbcargo.getTotalBOEMT();
double totalBOESCM =dbcargo.getTotalBOESCM();

String disp_bl_no = "";
String disp_boe_no = "";

if(!num_bl.equals(""))
{
	if(Integer.parseInt(num_bl)<9)
	{
		disp_bl_no = "BL0"+(Integer.parseInt(num_bl)+1);
	}
	else{
		disp_bl_no =	"BL"+(Integer.parseInt(num_bl)+1);
	}
}
if(!num_boe.equals(""))
{
	if(Integer.parseInt(num_boe)<9)
	{
		disp_boe_no = "BOE0"+(Integer.parseInt(num_boe)+1);
	}
	else{
		disp_boe_no =	"BOE"+(Integer.parseInt(num_boe)+1);
	}
}

if(split_bol.equals("")){
	split_bol="N";
}

if(num_bl.equals("")){
	num_bl="0";
}
if(num_boe.equals("")){
	num_boe="0";
}
/* 
if(agmt_base.equals("")){
	agmt_base="X";
}
if(agmt_type.equals("")){
	agmt_type="0";
}
 */
if(ent_dt.equals(""))
{
	String split[] = dateTime.split(" ");
	ent_dt = split[0];
	ent_time = split[1];
}
 
if(man_req_from_dt.equals(""))
{
	man_req_from_dt=win_start_dt;
}

if(man_req_to_dt.equals(""))
{
	man_req_to_dt=win_end_dt;
}

String cargo_number_disp="";
if(!cont_name.equals("") && !cargo_number.equals(""))
{
	cargo_number_disp = cont_name+"-"+cargo_number;
}
String confirm_no_disp = cont_name;
%>
<body onload="<%if(opration.equals("MODIFY")){ %>splitBLorNot();<%} %>">
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_CargoMaster">

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
					    	Cargo Nomination
					    </div>
					    <%-- <div class="btn-group">
						  <lable class="btn btn-outline-secondary btngrp <%if(opration.equals("INSERT")){%>btnactive<%}%>" onclick="refresh('INSERT');"><i class="fa fa-plus-circle"></i>&nbsp;New</lable>
						  <lable class="btn btn-outline-secondary btngrp <%if(opration.equals("MODIFY")){%>btnactive<%}%>" onclick="refresh('MODIFY');"><i class="fa fa-edit"></i>&nbsp;Modify</lable>
						</div> --%>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Trader<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=opration%>');">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Nomination Status:</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
			    				<div class="col-3">
				    				<label class="form-label"><b><%=nom_status%></b></label>
				    			</div>
				    			<%if(nom_status.equals("Nominated")){%>
				    			<div class="col-2">
				    				<input type="text" class="form-control form-control-sm" style="text-align:left;background:#99ffcc" disabled  value="<%=nom_revision%>">		    				
				    				<input type="hidden"name="nom_status" value="<%=nom_status%>">				    				
				      			</div>
				      			<%} %>
				  			</div>
						</div>											
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="button" name="sel_cargo_btn" id="sel_cargo_btn" class="btn btn-info btn-sm select_btn" value="Select Cargo" onclick="openCargoList('<%=opration%>');" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-6 col-xs-6 col-md-6">
				    				<input type="text" class="form-control form-control-sm" name="cargo_number_disp" value="<%=cargo_number_disp%>" maxLength="50" readOnly>
				    				<input type="hidden"name="cargo_number" value="<%=cargo_number%>">
				      			</div>
				      			<div class="col-sm-6 col-xs-6 col-md-6">
				    				<input type="text" class="form-control form-control-sm" value="<%=cargo_ref%>" maxLength="50" readOnly>
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label title="Confirmation No#" class="form-label"><b>Confirmation Notice (CN)</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-6 col-xs-6 col-md-6">
				      				<input type="text" class="form-control form-control-sm" name="confirm_no_disp" id="confirm_no_disp" value="<%=confirm_no_disp %>" maxLength="25" readOnly>
				      				<input type="hidden" name="confirm_no" id="confirm_no" value="<%=confirm_no %>">
				    			</div>
				    			<div class="col-sm-6 col-xs-6 col-md-6">
				    				<input type="text" class="form-control form-control-sm" value="<%=cont_ref_no%>" maxLength="50" readOnly>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Confirmed Volume</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-6 col-xs-6 col-md-6">
				      				<input type="text" class="form-control form-control-sm" name="mandate_conf_vol" id="mandate_conf_vol" value="<%=mandate_conf_vol %>" maxLength="25" readOnly ><!-- onblur="checkValuePrecision(this.value, '6','14','mandate_conf_vol');" -->
				    			</div>
				    			<div class="col-sm-6 col-xs-6 col-md-6">
				    				<div class="form-group row">
						    			<div class="col">
						      				<div class="input-group input-group-sm" >
				      							<input type="text" class="form-control form-control-sm" name="mandate_conf_vol_tol" id="mandate_conf_vol_tol" value="<%=mandate_conf_vol_tol %>" maxLength="25" readOnly >
				    							<span class="input-group-text"><b>Tolerance %</b></span>
				    						</div>
				    					</div>
				    				</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Confirmed Price</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="col-auto">
			    				<div class="input-group input-group-sm" >
				      				<input type="text" class="form-control form-control-sm" name="conf_price" id="conf_price" value="<%=conf_price %>" maxLength="25" readOnly ><!-- onblur="checkValuePrecision(this.value,'4','6','conf_price')" -->
				    				<input type="hidden" name="confirm_price_unit" value="<%=conf_price_unit%>">
				    				<%if(!conf_price_unit.equals("")) {%><span class="input-group-text"><b><%if(conf_price_unit.equals("2")) {%>USD/MMBTU<%}else{ %>INR/MMBTU<%} %></b></span><%} %>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Start Date</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="win_start_dt" id="win_start_dt" value="<%=win_start_dt %>" maxLength="10" readOnly
			      						onblur="validateDate(this);checkSigningStartDt(document.forms[0].start_dt, this.value);" 
			      						onchange="validateDate(this);checkSigningStartDt(document.forms[0].start_dt,this.value);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>End Date</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="win_end_dt" id="win_end_dt" value="<%=win_end_dt %>" maxLength="10" readOnly
			      						onblur="validateDate(this);checkSigningStartDt(document.forms[0].start_dt, this.value);" 
			      						onchange="validateDate(this);checkSigningStartDt(document.forms[0].start_dt,this.value);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Vessel information</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="button" class="btn btn-info btn-sm select_btn" value="Select Vessel" onclick="openShipList('<%=cargo_number%>','<%=confirm_no%>','<%=contract_type%>')" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="existing_ship" id="existing_ship" value="<%=ship_name%>" maxLength="50" readOnly>
				    				<input type="hidden" name="ship_cd" value="<%=ship_cd%>">
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Flag</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ship_flag" id="ship_flag" value="<%=ship_flag%>" maxLength="25" readOnly>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>IMO Number</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ship_imo_no" id="ship_imo_no" value="<%=ship_imo_no%>" maxLength="25" readOnly>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Classification Society</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ship_class_soc" id="ship_class_soc" value="<%=ship_class_soc%>" maxLength="25" readOnly>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Inmarsat Number</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="inmarsat_no" id="inmarsat_no" value="<%=inmarsat_no%>" maxLength="25" readOnly>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Vessel Owner</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ship_owner_name" id="ship_owner_name" value="<%=ship_owner_name%>" maxLength="25" readOnly>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Vessel Operator</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ship_operator_name" id="ship_operator_name" value="<%=ship_operator_name%>" maxLength="25" readOnly>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Vessel's Fax No.</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ship_fax_no" id="ship_fax_no" value="<%=ship_fax_no%>" maxLength="25" readOnly>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Vessel's Telex No.</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ship_telex_no" id="ship_telex_no" value="<%=ship_telex_no%>" maxLength="25" readOnly>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Vessel's Email</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ship_email" id="ship_email" value="<%=ship_email%>" maxLength="25" readOnly>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Gross Tonnage</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="col-auto">
			    				<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm" name="gross_tonnage" id="gross_tonnage" value="<%=gross_tonnage%>" maxLength="5"  readOnly><!-- onblur="checkValuePrecision(this.value,'2','11','gross_tonnage')" -->
		      						<span class="input-group-text"><b>Tonnes</b></span>
	      						</div>
			    			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Approx Cargo Capacity</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-6 col-xs-6 col-md-6">
				      				<input type="text" class="form-control form-control-sm" name="cargo_capacity" id="cargo_capacity" value="<%=cargo_capacity%>" maxLength="25" readOnly><!-- onblur="checkValuePrecision(this.value,'2','11')" -->
				    			</div>
				    			<div class="col-sm-4 col-xs-4 col-md-4">
									<div class="col-auto">
					    				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm" name="percentage_capacity" id="percentage_capacity" value="<%=percentage_capacity%>" readOnly maxLength="5" ><!-- onblur="checkValuePrecision(this.value,'2','5','percentage_capacity')" -->
				      						<span class="input-group-text"><b>%</b></span>
			      						</div>
					    			</div>
								</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Call Sign</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ship_call_sign" id="ship_call_sign" value="<%=ship_call_sign%>" maxLength="25" readOnly>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Nomination Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Requested Volume</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="col-auto">
			    				<div class="input-group input-group-sm" >
				      				<input type="text" class="form-control form-control-sm" name="mandate_req_vol" id="mandate_req_vol" value="<%=mandate_conf_vol %>" maxLength="25" readOnly onblur="checkValuePrecision(this.value,'6','14','mandate_req_vol');negNumber(this);">
				    				<span class="input-group-text"><b>MMBTU</b></span>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Request Window<span class="s-red">*</span></b></label>
						</div>
						
						<div class="col-sm-4 col-xs-4 col-md-4">  
				  			<div class="form-group row">
			    				<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="man_req_from_dt" id="man_req_from_dt"  value="<%=man_req_from_dt %>" maxLength="10">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col-1" align="center">
				    				<label class="form-label"><b>To</b></label>
				    			</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="man_req_to_dt" id="man_req_to_dt"  value="<%=man_req_to_dt %>" maxLength="10">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    		</div>
						</div>
					</div>
					
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Country of Origin</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
									<select class="form-select form-select-sm" name="country_org" id="country_org">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTRY_NM.size();i++){ %>
										<option value="<%=VCOUNTRY_NM.elementAt(i)%>"><%=VCOUNTRY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].country_org.value="<%=country_origin%>"</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Load Port</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
			    			<div class="col-sm-12 col-xs-12 col-md-12">
			    				<input type="text" class="form-control form-control-sm" name="load_port" id="load_port"  value="<%=load_port %>" maxLength="80">
			    			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Allowed Lay Time</b></label>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
		    				<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm" name="allowed_lay_time_hrs" id="allowed_lay_time_hrs"  value="<%=allowed_lay_time_hrs %>" maxLength="5" autocomplete="off" readOnly>
	      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
      						</div>
		    			</div>
		    			<div class="col-sm-2 col-xs-2 col-md-2">
		    				<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm" name="allowed_lay_time_min" id="allowed_lay_time_min"  value="<%=allowed_lay_time_min %>" maxLength="5" autocomplete="off" readOnly>
	      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
      						</div>
		    			</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Demurrage Rate</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">
				    			<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="demu_rate" value="<%=demu_rate %>" 
				      				<%if(opration.equals("MODIFY")) {%>readOnly<%} %>  readOnly><!-- onblur="checkValuePrecision(this.value,'2','10')" -->
		      					</div>
				    			<div class="col">
		      						<select class="form-select form-select-sm" name="demu_rate_unit" onchange="checkRateFormate(this);" <%if(opration.equals("MODIFY")) {%>style="pointer-events: none;"<%} %> disabled="disabled" >
		      							<option value="2">USD/Day</option>
		      							<!-- <option value="1">INR/Day</option> -->
		      						</select>
		      						<script>document.forms[0].demu_rate_unit.value="<%=demu_rate_unit%>"</script>
				    			</div>
				  			</div> 
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
			    					<input type="button" name="sel_cha_btn" id="sel_cha_btn" class="btn btn-sm config_btn" value="Select CHA Contract" onclick="openSvcContList('H','<%=man_req_from_dt %>','<%=man_req_to_dt %>');" style="font-weight: bold;">
				  				</div>
				  			</div>
						</div>
						<div class="col">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="disp_cha_cont_dtl" value="<%=disp_cha_cont_dtl %>" readOnly <%if(!disp_cha_cont_dtl.equals("")){ %>style="background:#99ffcc;"<%} %>>
				    				<input type="hidden" name="cha_cont_dtl" value="<%=cha_cont_dtl %>">
				    			</div>
				  			</div>
						</div>
					</div>
					
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
			    					<input type="button" name="sel_vessel_btn" id="sel_vessel_btn" class="btn btn-sm config_btn" value="Select Vessel's Agent" onclick="openSvcContList('V','<%=man_req_from_dt %>','<%=man_req_to_dt %>');" style="font-weight: bold;">
				  				</div>
				  			</div>
						</div>
						<div class="col">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" readOnly name="disp_vessel_cont_dtl" value="<%=disp_vessel_cont_dtl %>"  <%if(!disp_vessel_cont_dtl.equals("")){ %>style="background:#99ffcc;"<%} %>>
				      				<input type="hidden" name="vessel_cont_dtl" value="<%=vessel_cont_dtl %>">
				    			</div>
				  			</div>
						</div>
					</div>
					
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
			    					<input type="button" name="sel_surveyor_btn" id="sel_surveyor_btn" class="btn btn-sm config_btn" value="Select Surveyor" onclick="openSvcContList('S','<%=man_req_from_dt %>','<%=man_req_to_dt %>');" style="font-weight: bold;">
				  				</div>
				  			</div>
						</div>
						<div class="col">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" readOnly name="disp_surveyor_cont_dtl" value="<%=disp_surveyor_cont_dtl %>"  <%if(!disp_surveyor_cont_dtl.equals("")){ %>style="background:#99ffcc;"<%} %>>
				    				<input type="hidden" name="surveyor_cont_dtl" value="<%=surveyor_cont_dtl %>">
				    			</div>
				  			</div>
						</div>
					</div>
					
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Remark</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<font size="1" face="arial, helvetica, sans-serif">&nbsp;( You may enter up to 500 characters. )&nbsp;
										<input readonly type=text name="remLen" size="3" maxlength="3" class=""> characters left
									</font><br>
				      				<textarea class="form-control" name="remark" id="remark" cols="75" rows="1" onKeyDown="textCounter(this.form.remark,this.form.remLen,499);" onKeyUp="textCounter(this.form.remark,this.form.remLen,499);"><%=remark%></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
					<%if(opration.equals("MODIFY")){ %>
					&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Bill of Lading Document Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-between"> 
								<div class="col-sm-2 col-xs-2 col-md-2">  
								    <input title="Check to Apply Custom Duty" style="vertical-align: center" type="checkbox" name="BL_Checkbox" id="BL_Checkbox" onchange="splitBLorNot()" <%if(split_bol.equals("Y")){ %>checked<%} %> <%if(Integer.parseInt(num_bl)>1){ %>onclick="return false"<%} %>>
								    <label class="form-label"><b>Split Bill Of Lading</b></label>
								</div>
								<div id="add_bl_model" class="btn-group" align="right">
									<%if(counterparty_cd.equals("0") || cargo_number.equals("")){ %>
									<label class="btn btn-outline-secondary subbtngrp1" onclick="detailsAlert('BL');">Add New BL</label>
									<%}else{ %>
										<%if(Integer.parseInt(num_bl)<99){%>
										<label class="btn btn-outline-secondary subbtngrp1" data-bs-toggle="modal" data-bs-target="#AddNewBl" onclick="doClearBl();">Add New BL</label>
										<%}else{ %>
										<label class="btn btn-outline-secondary subbtngrp1" onclick="exceedingBlDtl();">Add New BL</label>
										<%} %>
									<%} %>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
				    	<div id="no_of_split_bl"  class="col-sm-6 col-xs-6 col-md-6">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>No. Of BL :</b></label>
								</div>
				    			<div class="col-auto">
			    					<input name="bl_number" id="bl_number" type="text" class="form-control form-control-sm" value="<%=num_bl%>" readonly>
				    			</div>
				    		</div>
				    	</div>
				    </div>
				    <div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="cargo_bl"> 
								<thead>
									<tr>
										<%if(write_access.equals("Y")){ %>
										<th rowspan="2">Select</th>
										<%}else{ %>
										<th rowspan="2">Sr#</th>
										<%} %>
										<th rowspan="2">BL#</th>
										<th rowspan="2">BL Price</th>
										<th rowspan="2">Price Unit</th>
										<th colspan="3">BL Quantity</th>
									</tr>
									<tr>
										<th>MMBTU</th>
										<th>MT</th>
										<th>SCM</th>
									</tr>
								</thead>
								<tbody id="itemTab_bl">
									<%if(VBL_QTY.size()>0){ %>
									<%for(int i=0;i<VBL_QTY.size();i++){%>
									<tr id="row">
										<%if(write_access.equals("Y")){ %><td align="center">
											<font title="Click to Edit" style="color:var(--header_color)">
												<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#AddNewBl" 
												onclick="doModifyBl('<%=VBL_NO.elementAt(i)%>','<%=VBL_QTY.elementAt(i)%>','<%=VBL_PRICE.elementAt(i)%>',
												'<%=VBL_PRICE_UNIT.elementAt(i)%>','<%=VBL_QTY_MT.elementAt(i)%>','<%=VBL_QTY_SCM.elementAt(i)%>')">
												</i>
											</font>
										</td>
										<%}else{ %>
										<td><%=i+1%></td>
										<%} %>
										<td align="center"><%=VBL_NO.elementAt(i)%></td>
										<td align="right"><%=VBL_PRICE.elementAt(i)%></td>
										<td align="center"> <%if(VBL_PRICE_UNIT.elementAt(i).equals("2")){ %>USD/MMBTU<%}else{ %>INR/MMBTU<%} %></td>
										<td align="right"><%=VBL_QTY.elementAt(i)%></td>
										<td align="right"><%=VBL_QTY_MT.elementAt(i)%></td>
										<td align="right"><%=VBL_QTY_SCM.elementAt(i)%></td>
									</tr>
									<%} %>
									<tbody>
										<tr>
											<td colspan="4" align="right" style="font-weight: bold; color: #203d78;">Total</td>
											<td align="right"><%=totalBlMMBTU %></td>
											<td align="right"><%=totalBlMT %></td>
											<td align="right"><%=totalBlSCM %></td>
										</tr>
									</tbody>
									<%}else{ %>
									<tr id="row">
										<td colspan="7" align="center">
											<%=utilmsg.infoMessage("<b>B/L Documentation Details is not Available!</b>") %>
										</td>
									</tr>
									<% }%>
								</tbody>
							</table>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> BOE Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-between"> 
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>No. Of BOE :</b></label>
									</div>
					    			<div class="col-auto">
				    					<input name="boe_number" id="boe_number" type="text" class="form-control form-control-sm" value="<%=num_boe%>" Readonly>
					    			</div>
								</div>
								<div class="btn-group" align="right">
									<%if(Integer.parseInt(num_bl)>Integer.parseInt(num_boe)){%>
										<%if(counterparty_cd.equals("0") || cargo_number.equals("")){ %>
										<label class="btn btn-outline-secondary subbtngrp1" onclick="detailsAlert('BOE');">Add New BOE</label>
										<%}else{ %>
										<label class="btn btn-outline-secondary subbtngrp1" data-bs-toggle="modal" data-bs-target="#AddNewBoe" onclick="doClearBoe('<%=VBL_AVAIL_FOR_ALL%>');">Add New BOE</label>
										<%} %>
									<%} %>
								</div>
							</div>
						</div>
					</div>
				    <div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="cargo_tb">
								<thead>
									<tr>
										<%if(write_access.equals("Y")){ %>
										<th rowspan="2">Select</th>
										<%}else{ %>
										<th rowspan="2">Sr#</th>
										<%} %>
										<th rowspan="2">BOE#</th>
										<th rowspan="2">Business Unit</th>
										<th rowspan="2">Trader Plant </th>
										<th rowspan="2">BOE Price</th>
										<th rowspan="2">Price unit</th>
										<th colspan="3">BOE MMBTU</th>
										<th rowspan="2">Linked BL/s</th>
										<th rowspan="2">Custom Duty</th>
										<th rowspan="2">Port Code</th>
									</tr>
									<tr>
										<th>MMBTU</th>
										<th>MT</th>
										<th>SCM</th>
									</tr>
								</thead>
								<tbody id="itemTab_boe">
									<%if(VBOE_QTY.size()>0){ %>
									<%for(int j=0;j<VBOE_QTY.size();j++){%>
									<tr id="row">
										<%if(write_access.equals("Y")){ %><td align="center">
											<font title="Click to Edit" style="color:var(--header_color)">
												<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#AddNewBoe" 
												onclick="doModifyBoe('<%=VBOE_NO.elementAt(j)%>','<%=VBOE_BU_SEQ.elementAt(j)%>','<%=VBOE_PLANT_SEQ.elementAt(j)%>',
												'<%=VBOE_QTY.elementAt(j)%>','<%=VBOE_QTY_UNIT.elementAt(j)%>','<%=VBOE_PRICE.elementAt(j)%>','<%=VBOE_PRICE_UNIT.elementAt(j)%>',
												'<%=VBOE_CUSTOM_DUTY.elementAt(j)%>','<%=VBOE_LOAD_PORT.elementAt(j)%>','<%=VBOE_LINKED_BL.elementAt(j)%>','<%=VBL_AVAIL_FOR_ALL%>',
												'<%=VBOE_QTY_MT.elementAt(j)%>','<%=VBOE_QTY_SCM.elementAt(j)%>');setBOEQuantasBL(document.forms[0].linked_bl,'cl');setBOEQuantMtasBL(document.forms[0].linked_bl,'cl');setBOEQuantScmasBL(document.forms[0].linked_bl,'cl');setLinked_BL();setBOEPriceasBL(document.forms[0].linked_bl,'cl');">
												</i>
											</font>
										</td>
										<%}else{ %>
										<td><%=j+1%></td>
										<%} %>
										<td align="center"><%=VBOE_NO.elementAt(j)%></td>
										<td align="center"><%=VBOE_BU_ABBR.elementAt(j)%></td>
										<td align="center"><%=VBOE_PLANT_ABBR.elementAt(j)%></td>
										<td align="right"><%=VBOE_PRICE.elementAt(j)%></td>
										<td align="center"> <%if(VBOE_PRICE_UNIT.elementAt(j).equals("2")){ %>USD/MMBTU<%}else{ %>INR/MMBTU<%} %></td>
										<td align="right"><%=VBOE_QTY.elementAt(j)%></td>
										<td align="right"><%=VBOE_QTY_MT.elementAt(j)%></td>
										<td align="right"><%=VBOE_QTY_SCM.elementAt(j)%></td>
										<td align="center"><%if(!VBOE_LINKED_BL.elementAt(j).equals("")){%>BL<%}%><%=VBOE_LINKED_BL.elementAt(j).toString().replace("@",", BL")%></td>
										<td align="center">
											<%if(VBOE_CUSTOM_DUTY.elementAt(j).equals("Y")){%>Yes 
											<%}else if(VBOE_CUSTOM_DUTY.elementAt(j).equals("N")){ %>No<%}%>
										</td>
										<td align="center">
											<%=VBOE_LOAD_PORT.elementAt(j)%>
										</td>
									</tr>
									<%} %>
									<tbody>
										<tr>
											<td colspan="6" align="right" style="font-weight: bold; color: #203d78;">Total</td>
											<td align="right"><%if(totalBOEMMBTU!=0){%><%=totalBOEMMBTU %><%} %></td>
											<td align="right"><%if(totalBOEMT!=0){%><%=totalBOEMT %><%} %></td>
											<td align="right"><%if(totalBOESCM!=0){%><%=totalBOESCM %><%} %></td>
											<td colspan="3"></td>
										</tr>
									</tbody>
									<%}else{ %>
									<tr id="row">
										<td colspan="12" align="center">
											<%=utilmsg.infoMessage("<b>BOE Details is not Available!</b>") %>
										</td>
									</tr>
									<% }%>
								</tbody>
							</table>
						</div>
					</div>
					<%} %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Liquefaction Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Plant</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="liquefac_plant" id="liquefac_plant" value="<%=liquefac_plant%>" maxLength="25">
				    			</div>
				  			</div>
						</div>
					
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Location(Country)</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				    				<select class="form-select form-select-sm" name="liquefac_country" id="liquefac_country">
		      							<option value="0">--Select--</option>
		      							<%for(int i=0;i<VCOUNTRY_NM.size();i++){ %>
										<option value="<%=VCOUNTRY_NM.elementAt(i)%>"><%=VCOUNTRY_NM.elementAt(i)%></option>
										<%} %>
		      						</select>
		      						<script>document.forms[0].liquefac_country.value="<%=liquefac_country%>"</script>
				  				</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Promoter</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="liquefac_promotor" id="liquefac_promotor" value="<%=liquefac_promotor%>">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Remark</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<font size="1" face="arial, helvetica, sans-serif">&nbsp;( You may enter up to 500 characters. )&nbsp;
										<input readonly type=text name="remLen1" size="3" maxlength="3" class=""> characters left
									</font><br>
				      				<textarea class="form-control" name="liquefac_remark" id="liquefac_remark" cols="75" rows="1" onKeyDown="textCounter(this.form.liquefac_remark,this.form.remLen1,499);" onKeyUp="textCounter(this.form.liquefac_remark,this.form.remLen1,499);"><%=liquefac_remark%></textarea>
				    			</div>
				  			</div>
				  		</div>
					</div>
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
			</div>
		</div>
	</div>
	&nbsp;
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Cargo Nomination Alerts!
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%if(!cargo_number.equals("0") && !cargo_number.equals("") && nom_status.equals("Nominated")){ %>
					<%if(num_bl.equals("0") || num_bl.equals("")){ %>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<%=utilmsg.errorMessage("<b>Bill of Lading(BL) Detail not Configured. Cargo will not appear for Arrival Documentation!</b>")%>
							</div>
						</div>
					<%} %>
					<%if((num_boe.equals("0") || num_boe.equals(""))){ %>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<%=utilmsg.errorMessage("<b>Bill of Entry(BoE) Detail not Configured. Cargo will not appear for Arrival Documentation!</b>")%>
							</div>
						</div>
					<%} %>
				<%} %>
				</div>
			</div>
		</div>
	</div>			
</div>

<div class="modal fade" id="AddNewBl" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div id="BL_MODEL" class="topheader">
        			Add New Bill Of Lading Document Details
        		</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" >
        	</div>
        	<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>BL#<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="disp_bl_no" id="disp_bl_no" value="<%=disp_bl_no%>" maxLength="25" readonly>
				    				<input type="hidden" name="temp_disp_bl_no" id="temp_disp_bl_no" value="<%=disp_bl_no%>">
				    			</div>
							</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>BL Quantity<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<!-- <div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="bl_qty" id="bl_qty" maxLength="21" onblur="checkValuePrecision(this.value,'6','14','bl_qty')">
				    				<input type="hidden" name="bl_qty_unit" value="1"> For MMBTU as "1"
				    			</div>
							</div> -->
							<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm" name="bl_qty" id="bl_qty"  value=""  autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','bl_qty');calcAllowedBlMMBTU(this.value);negNumber(this);" style="text-align: right" >
	      						<input type="hidden" class="form-control form-control-sm" name="temp_bl_qty" id="temp_bl_qty" value="">
	      						<span class="input-group-text"><b>MMBTU</b></span>
	      						<input type="text" class="form-control form-control-sm" name="bl_qty_mt" id="bl_qty_mt"  value=""  autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','bl_qty_mt');negNumber(this);" style="text-align: right">
	      						<span class="input-group-text"><b>MT</b></span>
	      						<input type="text" class="form-control form-control-sm" name="bl_qty_scm" id="bl_qty_scm"  value=""  autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','bl_qty_scm');negNumber(this);" style="text-align: right">
	      						<span class="input-group-text"><b>SCM</b></span>
      						</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> BL Price<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="bl_price" id="bl_price" maxLength="7" onblur="checkValuePrecision(this.value,'4','2','bl_price');negNumber(this);">
				      				<input type="hidden" name="bl_no" value="">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="bl_price_unit">
				      					<option value="2">USD/MMBTU</option>
				      				</select>
				      				<%-- <script>document.forms[0].bl_price_unit.value="<%=bl_price_unit.trim()%>"</script> --%>
				    			</div>
				  			</div>
				  		</div>
      				</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div align="right">
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitBl();">
				</div>
      		</div>
        </div>
	</div>
</div>

<div class="modal fade" id="AddNewBoe" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div id="BOE_MODEL" class="topheader">
        			Add New BOE Details
        		</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
        	</div>
        	<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>BOE#<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="disp_boe_no" id="disp_boe_no" value="<%=disp_boe_no%>" maxLength="25" readonly>
				      				<input type="hidden" name="temp_disp_boe_no" id="temp_disp_boe_no" value="<%=disp_boe_no%>">
				    			</div>
							</div>
						</div>
      				</div>
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Business Unit<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="bu_seq">
				      					<option value="" selected="selected">--Select--</option>
				      					<%for(int i=0;i<VSEL_BU_PLANT_SEQ_NO.size();i++){ %>
										<option value="<%=VSEL_BU_PLANT_SEQ_NO.elementAt(i)%>"><%=VSEL_BU_PLANT_ABBR.elementAt(i)%></option>
										<%} %>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Trader Plant<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="plant_seq">
				      					<option value="" selected="selected">--Select--</option>
				      					<%for(int i=0;i<VSEL_PLANT_SEQ_NO.size();i++){ %>
										<option value="<%=VSEL_PLANT_SEQ_NO.elementAt(i)%>"><%=VSEL_PLANT_ABBR.elementAt(i)%></option>
										<%} %>
				      				</select>
				    			</div>
				  			</div>
						</div>
					</div>
      				<div class="row m-b-5" id="tr2">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Link BL<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">				    			
			    					<select class="form-select form-select-sm"  name="linked_bl" id="linked_bl" multiple="multiple" onblur="setLinked_BL()" onchange="setBOEQuantasBL(this,'ch');setBOEQuantMtasBL(this,'ch');setBOEQuantScmasBL(this,'ch');setBOEPriceasBL(this,'ch')">
						 				<% for(int j=0; j<VBL_NO.size(); j++){ %>
			 							<option id="<%=VBL_NO.elementAt(j) %>" value="<%=VBL_NO.elementAt(j)%>"><%=VBL_NO.elementAt(j)%></option>
			 							<%} %>
						 			</select>
						 			<input type="hidden" name="joint_bl_no_quant" id="joint_bl_no_quant" value="<%=VBL_NO%><%=VBL_QTY%>">
						 			<input type="hidden" name="joint_bl_no_mt" id="joint_bl_no_mt" value="<%=VBL_NO%><%=VBL_QTY_MT%>">
						 			<input type="hidden" name="joint_bl_no_scm" id="joint_bl_no_scm" value="<%=VBL_NO%><%=VBL_QTY_SCM%>">
						 			<input type="hidden" name="total_sel_bl_quantity" id="total_sel_bl_quantity" value="">
						 			<input type="hidden" name="total_sel_bl_mt" id="total_sel_bl_mt" value="">
						 			<input type="hidden" name="total_sel_bl_scm" id="total_sel_bl_scm" value="">
						 			<input type="hidden" name="joint_bl_no_price" id="joint_bl_no_price" value="<%=VBL_NO%><%=VBL_PRICE%>">
						 			<input type="hidden" name="total_sel_bl_price" id="total_sel_bl_price" value="">
						 			<span id="bl_inf"><font color="red" size="2">Press ctrl/shift for multiple selection!</font></span>				    			
				  				</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>BOE Quantity<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<!-- <div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="boe_qty" id="boe_qty" maxLength="21" onblur="checkValuePrecision(this.value,'6','14','boe_qty')">
				      				<input type="hidden" name="boe_qty_unit" value="1"> For MMBTU as "1"
				      				<input type="hidden" name="boe_no" value="">
				    			</div>
							</div> -->
							<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm" name="boe_qty" id="boe_qty"  value=""  autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','boe_qty');checkBOEquantLimit();negNumber(this);" style="text-align: right" >
	      						<span class="input-group-text"><b>MMBTU</b></span>
	      						<input type="text" class="form-control form-control-sm" name="boe_qty_mt" id="boe_qty_mt"  value=""  autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','boe_qty_mt');negNumber(this);" style="text-align: right">
	      						<span class="input-group-text"><b>MT</b></span>
	      						<input type="text" class="form-control form-control-sm" name="boe_qty_scm" id="boe_qty_scm"  value=""  autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','boe_qty_scm');negNumber(this);" style="text-align: right">
	      						<span class="input-group-text"><b>SCM</b></span>
	      						<input type="hidden" name="boe_no" value="">
      						</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>BOE Price<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="boe_price"  id="boe_price" maxLength="7" onblur="checkValuePrecision(this.value,'4','2','boe_price');negNumber(this);">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="boe_price_unit">
				      					<option value="2">USD/MMBTU</option>
				      				</select>
				      				<%-- <script>document.forms[0].boe_price_unit.value="<%=boe_price_unit.trim()%>"</script> --%>
				    			</div>
				  			</div>
				  		</div>
      				</div>
      				<div align="left" class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Custom Duty Applicable</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="custDuty_flag" id="custDuty_flag" >
				      					<option value="" selected="selected">--Select--</option>
				      					<option value="Y">Yes</option>
				      					<option value="N">No</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div id="port_div1" class="col-sm-2 col-xs-2 col-md-2" style="display: block;">
							<label class="form-label"><b>Port Code<span class="s-red">*</span></b></label>
						</div>
						<div id="port_div2" class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				      				<select class="form-select form-select-sm" name="port_code" style="display: block;">
										<option value="">--Select--</option>
		      							<%for(int i=0;i<VCOUNTRY_ISO.size();i++){%>
										<option value="<%=VCOUNTRY_ISO.elementAt(i)%>"><%=VCOUNTRY_ISO.elementAt(i)%></option>
										<%} %>
									</select>
				    			</div>
				  			</div>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div align="right">
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitBoe();">
				</div>
      		</div>
        </div>
	</div>
</div>

<input type="hidden" name="option" value="CARGO_NOMINATION_MST">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="billing_opration" value="">
<input type="hidden" name="bl_opration" value="">
<input type="hidden" name="boe_opration" value="">

<input type="hidden" name="sel_bl_list" value="">

<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_type" value="<%=agmt_type%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>">
<input type="hidden" name="totalBlMMBTU" value="<%=totalBlMMBTU%>">

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

<script>
</script>

</form>
</body>
</html>