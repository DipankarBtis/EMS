<%@page import="com.etrm.fms.util.UtilBean"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
.tooltip-text {
    visibility: hidden;
    position: absolute;
    z-index: 1;
    width: 300px;
    color: #000000;
    font-size: 12px;
   /*  font-weight: bold; */
    background-color: #ffffe0;
    border: 1px solid #000000; /* Added a border for better visibility */
   /*  border-radius: 8px; */
    padding: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* Added a subtle shadow for depth */
    text-align: left; /* Adjusted text alignment */
    pointer-events: none;
}

.tooltip-text i {
    margin-right: 5px;
}

.hover-text:hover .tooltip-text {
    visibility: visible;
}

.hover-text {
    position: relative;
    display: inline-block;
    margin: 4px;
    font-family: Arial;
}

/* Adjusted positioning */
.tooltip-text {
    top: 100%;
    left: 50%;
    transform: translateX(-50%);
}

</style>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
var newWindow;

function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_ltcora_cargo_dtl.jsp?counterparty_cd="+counterparty_cd+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit() {
    var chk = document.forms[0].chk;
    var vessel_name = document.forms[0].vessel_name;
    var supplier_name = document.forms[0].supplier_name;
    var window_start_dt = document.forms[0].window_start_dt;
    var window_end_dt = document.forms[0].window_end_dt;
    var edq_qty = document.forms[0].edq_qty;
    var csoc_qty = document.forms[0].csoc_qty;
  
    var storage_start_dt = document.forms[0].storage_start_dt;
    var storage_days = document.forms[0].storage_days;
    var storage_ext_days = document.forms[0].storage_ext_days;
    
    var boe_no = document.forms[0].boe_no;
    var cargo_number = document.forms[0].cargo_number;
    
    var msg = "";
    var flag = true;

    var countChk = 0;

    if (chk != null && chk != undefined) {
        if (chk.length != undefined) {
            for (var i = 0; i < chk.length; i++) {
                if (chk[i].checked) {
                    countChk++;
                	
                    if (trim(boe_no[i].value) == "") 
                    {
                    	var boe_confirm = confirm("Nomination For "+cargo_number[i].value+" can not be initiated without BOE#!!\n Do you still want to proceed?\n");
                    	
                    	if (boe_confirm)
                        {
                    		 flag = true;
                        }
                    	else
                    	{
                    		 msg += "Please provide BOE# for ROW - " + (i + 1) + "!\n";
                             flag = false;
                    	}
                    }
                    
                    if (trim(vessel_name[i].value) == "") {
                        msg += "Select Vessel Name for ROW - " + (i + 1) + "!\n";
                        flag = false;
                    }
                    /* if (trim(supplier_name[i].value) == "") {
                        msg += "Select Supplier for ROW - " + (i + 1) + "!\n";
                        flag = false;
                    } */
                    if (trim(window_start_dt[i].value) == "") {
                        msg += "Enter Arrival Window From Date for ROW - " + (i + 1) + "!\n";
                        flag = false;
                    }
                    if (trim(window_end_dt[i].value) == "") {
                        msg += "Enter Arrival Window To Date for ROW - " + (i + 1) + "!\n";
                        flag = false;
                    }
                    if (trim(edq_qty[i].value) == "" || trim(edq_qty[i].value) == "0.00") {
                        msg += "Enter EDQ MMBTU for ROW - " + (i + 1) + "!\n";
                        flag = false;
                    }
                    if (trim(csoc_qty[i].value) == "" || trim(csoc_qty[i].value) == "0.00") {
                        msg += "Enter CSOC MMBTU for ROW - " + (i + 1) + "!\n";
                        flag = false;
                    }
                    
                    if (trim(storage_start_dt[i].value) == "" || trim(storage_days[i].value) == "") {
                        msg += "Enter Storage Days Details for ROW - " + (i + 1) + "!\n";
                        flag = false;
                    }
                }
            }
        } 
        else 
        {
            if (chk.checked)
            {
                countChk++;

                if (trim(vessel_name.value) == "") {
                    msg += "Select Vessel Name for ROW - 1!\n";
                    flag = false;
                }
                if (trim(supplier_name.value) == "") {
                    msg += "Select Supplier for ROW - 1!\n";
                    flag = false;
                }
                if (trim(window_start_dt.value) == "") {
                    msg += "Enter Arrival Window From Date for ROW - 1!\n";
                    flag = false;
                }
                if (trim(window_end_dt.value) == "") {
                    msg += "Enter Arrival Window To Date for ROW - 1!\n";
                    flag = false;
                }
                if (trim(edq_qty.value) == "") {
                    msg += "Enter EDQ MMBTU for ROW - 1!\n";
                    flag = false;
                }
                if (trim(csoc_qty.value) == "") {
                    msg += "Enter CSOC MMBTU for ROW - 1!\n";
                    flag = false;
                }
                if (trim(storage_start_dt.value) == "" || trim(storage_days.value) == "") {
                    msg += "Enter Storage Days Details for ROW - 1!\n";
                    flag = false;
                }
            }
        }
    }

    if (countChk <= 0)
    {
        alert("Please Select at least one (1) ROW for Submit!");
    } 
    else 
    {
        if (flag)
        {
            var a = confirm("Do you want to Submit?");
            if (a)
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
}

function openContractList()
{
	var u = document.forms[0].u.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var msg="";
	var flag=true;
	
	if(trim(counterparty_cd) == "" || counterparty_cd == "0")
	{
		msg+="Select Trader!\n";
		flag=false;
	}
	
	var url = "frm_ltcora_cargo_cont_list.jsp?counterparty_cd="+counterparty_cd+"&u="+u;
	
	if(flag)
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
	else
	{
		alert(msg);
	}
}

function openADQDtl(index)
{
	var u = document.forms[0].u.value;

	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var window_start_dt = document.getElementById('window_start_dt'+index).value;
	var window_end_dt = document.getElementById('window_end_dt'+index).value;
	var cargo_number = document.getElementById('cargo_number'+index).value;
	var cargo_no = document.getElementById('cargo_no'+index).value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_ref_no = document.forms[0].cont_ref_no.value;
	var cont_rev_no = document.forms[0].cont_rev.value;
	var cont_name = document.forms[0].cont_name.value;
	var no_cargo = document.forms[0].no_cargo.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var contract_type = document.forms[0].contract_type.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var agmt_type = document.forms[0].agmt_type.value;
	var buy_sell = document.forms[0].buy_sell.value;
	
	var chk = document.getElementById("chk"+index);
	
	var msg="";
	var flag=true;
	
	if(trim(window_start_dt) == "")
	{
		msg+="Enter Arrival Window From Date!\n";
		flag=false;
	}
	if(trim(window_end_dt) == "")
	{
		msg+="Enter Arrival Window To Date!\n";
		flag=false;
	}
	
	var url = "frm_ltcora_cardo_adq_dtl.jsp?cargoRef="+cargo_number+"&counterparty_cd="+counterparty_cd+
			"&window_start_dt="+window_start_dt+"&window_end_dt="+window_end_dt+"&cont_no="+cont_no+
			"&cont_ref_no="+cont_ref_no+"&cont_rev="+cont_rev_no+"&cargo_no="+cargo_no+
			"&cont_name="+cont_name+"&no_cargo="+no_cargo+"&agmt_no="+agmt_no+"&contract_type="+contract_type+
			"&agmt_rev="+agmt_rev+"&agmt_type="+agmt_type+"&buy_sell="+buy_sell+"&u="+u;
	
	if(chk.checked)
	{
		if(flag)
		{
			if(!newWindow || newWindow.closed)
			{
				newWindow = window.open(url,"ADQ MMBTU List","top=10,left=10,width=1100,height=600,scrollbars=1");
			}
			else
			{
				newWindow.close();
				newWindow = window.open(url,"ADQ MMBTU List","top=10,left=10,width=1100,height=600,scrollbars=1");
			}
		}
		else
		{
			alert(msg);
		}
	}
	else
	{
		alert("Please Select Cargo!!");
	}
}

function setContDetail(countpty_cd,cont_no,cont_rev_no,cont_type,disp_cont_no,cont_ref_no,cont_name,no_cargo,agmt_no,contract_type,agmt_rev,agmt_type,buy_sell,start_dt,end_dt,sug_per)
{
	var u = document.forms[0].u.value;

	var url = "frm_ltcora_cargo_dtl.jsp?counterparty_cd="+countpty_cd+"&cont_no="+cont_no+
			"&disp_cont_no="+disp_cont_no+"&cont_ref_no="+cont_ref_no+"&cont_rev="+cont_rev_no+
			"&cont_name="+cont_name+"&no_cargo="+no_cargo+"&agmt_no="+agmt_no+"&contract_type="+contract_type+
			"&agmt_rev="+agmt_rev+"&agmt_type="+agmt_type+"&buy_sell="+buy_sell+"&start_dt="+start_dt+"&end_dt="+end_dt+"&sug_per="+sug_per+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setsuppQty(index)
{
	var edq_qty = document.getElementById("edq_qty"+index).value;
	var adq_qty = document.getElementById("adq_qty"+index).value;
	
	var sug_per = document.forms[0].sug_per.value;
	
	if(adq_qty == "" || adq_qty == "0.00")
	{
		var supp_qty = edq_qty - ((edq_qty*sug_per)/100);
	}
	else
	{
		var supp_qty = adq_qty - ((adq_qty*sug_per)/100);
	}

	document.getElementById("supp_qty"+index).value=supp_qty.toFixed(2);;
}

function openVarCSOC(index)
{
	var u = document.forms[0].u.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var window_start_dt = document.getElementById('window_start_dt'+index).value;
	var window_end_dt = document.getElementById('window_end_dt'+index).value;
	var cargo_number = document.getElementById('cargo_number'+index).value;
	var cargo_no = document.getElementById('cargo_no'+index).value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_ref_no = document.forms[0].cont_ref_no.value;
	var cont_rev_no = document.forms[0].cont_rev.value;
	var cont_name = document.forms[0].cont_name.value;
	var no_cargo = document.forms[0].no_cargo.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var contract_type = document.forms[0].contract_type.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var agmt_type = document.forms[0].agmt_type.value;
	var buy_sell = document.forms[0].buy_sell.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	
	var chk = document.getElementById("chk"+index);
	
	var storage_start_dt = document.getElementById("storage_start_dt"+index).value;
	var storage_days = document.getElementById("storage_days"+index).value;
	var storage_ext_days = document.getElementById("storage_ext_days"+index).value;
	
	var msg="";
	var flag=true;
	
	var url = "frm_ltcora_cargo_var_csoc_dtl.jsp?cargoRef="+cargo_number+"&counterparty_cd="+counterparty_cd+
	"&cont_no="+cont_no+"&cont_ref_no="+cont_ref_no+"&cont_rev="+cont_rev_no+"&cargo_no="+cargo_no+
	"&cont_name="+cont_name+"&no_cargo="+no_cargo+"&agmt_no="+agmt_no+"&contract_type="+contract_type+
	"&agmt_rev="+agmt_rev+"&agmt_type="+agmt_type+"&buy_sell="+buy_sell+
	"&start_dt="+storage_start_dt+"&storage_days="+storage_days+"&storage_ext_days="+storage_ext_days+"&u="+u;
	
	if(chk.checked)
	{
		if(flag)
		{
			if(!newWindow || newWindow.closed)
			{
				newWindow = window.open(url,"Variable CSOC","top=10,left=10,width=1100,height=600,scrollbars=1");
			}
			else
			{
				newWindow.close();
				newWindow = window.open(url,"Variable CSOC","top=10,left=10,width=1100,height=600,scrollbars=1");
			}
		}
		else
		{
			alert(msg);
		}
	}
	else
	{
		alert("Please Select Cargo!!");
	}
}

function openStorageDtls(index)
{
	var u = document.forms[0].u.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var actual_recpt_dt = document.getElementById("actual_recpt_dt"+index).value;
	var window_start_dt = document.getElementById('window_start_dt'+index).value;
	var window_end_dt = document.getElementById('window_end_dt'+index).value;
	var cargo_number = document.getElementById('cargo_number'+index).value;
	var cargo_no = document.getElementById('cargo_no'+index).value;
	var supp_qty = document.getElementById('supp_qty'+index).value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_ref_no = document.forms[0].cont_ref_no.value;
	var cont_rev_no = document.forms[0].cont_rev.value;
	var cont_name = document.forms[0].cont_name.value;
	var no_cargo = document.forms[0].no_cargo.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var contract_type = document.forms[0].contract_type.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var agmt_type = document.forms[0].agmt_type.value;
	var buy_sell = document.forms[0].buy_sell.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	
	var chk = document.getElementById("chk"+index);
	
	var msg="";
	var flag=true;
	
	var url = "frm_ltcora_cargo_storage_dtl.jsp?cargoRef="+cargo_number+"&counterparty_cd="+counterparty_cd+"&id_no="+index+
	"&cont_no="+cont_no+"&cont_ref_no="+cont_ref_no+"&cont_rev="+cont_rev_no+"&cargo_no="+cargo_no+"&window_start_dt="+window_start_dt+
	"&cont_name="+cont_name+"&no_cargo="+no_cargo+"&agmt_no="+agmt_no+"&contract_type="+contract_type+"&actual_recpt_dt="+actual_recpt_dt+
	"&agmt_rev="+agmt_rev+"&agmt_type="+agmt_type+"&buy_sell="+buy_sell+"&start_dt="+start_dt+"&end_dt="+end_dt+"&supp_qty="+supp_qty+"&u="+u;
	
	if(chk.checked)
	{
		if(flag)
		{
			if(!newWindow || newWindow.closed)
			{
				newWindow = window.open(url,"Storage Details","top=10,left=10,width=1100,height=600,scrollbars=1");
			}
			else
			{
				newWindow.close();
				newWindow = window.open(url,"Storage Details","top=10,left=10,width=1100,height=600,scrollbars=1");
			}
		}
		else
		{
			alert(msg);
		}
	}
	else
	{
		alert("Please Select Cargo!!");
	}
}

function setStorageDetail(id_no,storage_days,expn_days,storage_start_dt)
{
	document.getElementById("storage_start_dt"+id_no).value  = storage_start_dt;
	document.getElementById("storage_days"+id_no).value  = storage_days;
	document.getElementById("storage_ext_days"+id_no).value  = expn_days;
}

function selectShip(id_no)
{
	var u = document.forms[0].u.value;
	var chk_flag = document.forms[0].chk_flag.value;
	
	var chk = document.getElementById("chk"+id_no);
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var msg="";
	var flag=true;
	
	var url = "frm_ltcora_cargo_vessel_list.jsp?id_no="+id_no+"&counterparty_cd="+counterparty_cd+"&u="+u;
	
	if(chk.checked)
	{
		if(flag)
		{
			if(!newWindow || newWindow.closed)
			{
				newWindow = window.open(url,"Vessel List","top=10,left=10,width=1100,height=600,scrollbars=1");
			}
			else
			{
				newWindow.close();
				newWindow = window.open(url,"Vessel List","top=10,left=10,width=1100,height=600,scrollbars=1");
			}
		}
		else
		{
			alert(msg);
		}
	}
	else
	{
		alert("Please Select Cargo!!");
	}
}

function shipDtl(id_no,ship_cd,ship_nm,counterparty_cd)
{
	document.getElementById('vessel_name'+id_no).value = ship_nm;
	document.getElementById('vessel_cd'+id_no).value = ship_cd;
	
	//document.getElementById('vessel_name'+id_no).style="background:#9effce";
}

function selectSupplier(id_no)
{
	var u = document.forms[0].u.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var chk = document.getElementById("chk"+id_no);
	
	var msg="";
	var flag=true;
	
	var url = "frm_ltcora_cargo_supplier_list.jsp?id_no="+id_no+"&counterparty_cd="+counterparty_cd+"&u="+u;
	
	if(chk.checked)
	{
		if(flag)
		{
			if(!newWindow || newWindow.closed)
			{
				newWindow = window.open(url,"Supplier List","top=10,left=10,width=1100,height=600,scrollbars=1");
			}
			else
			{
				newWindow.close();
				newWindow = window.open(url,"Supplier List","top=10,left=10,width=1100,height=600,scrollbars=1");
			}
		}
		else
		{
			alert(msg);
		}
	}
	else
	{
		alert("Please Select Cargo!!");
	}
}

function supplierDtl(trader_cd,trader_name,trader_abbr,id_no)
{
	document.getElementById('supplier_name'+id_no).value  = trader_name;
	document.getElementById('supplier_cd'+id_no).value  = trader_cd;
	
	//document.getElementById('supplier_name'+id_no).style="background:#9effce";
}

function enable_disable(index)
{
	var chk = document.getElementById("chk"+index);
	
	var cargo_number = document.getElementById("cargo_number"+index);
	var vessel_name = document.getElementById("vessel_name"+index);
	var supplier_name = document.getElementById("supplier_name"+index);
	var vessel_cd = document.getElementById("vessel_cd"+index);
	var supplier_cd = document.getElementById("supplier_cd"+index);
	var window_start_dt = document.getElementById("window_start_dt"+index);
	var window_end_dt = document.getElementById("window_end_dt"+index);
	
	var actual_recpt_dt = document.getElementById("actual_recpt_dt"+index);
	var edq_qty = document.getElementById("edq_qty"+index);
	var adq_qty = document.getElementById("adq_qty"+index);
	var supp_qty = document.getElementById("supp_qty"+index);
	var csoc_qty = document.getElementById("csoc_qty"+index);
	var boe_qty = document.getElementById("boe_qty"+index);
	var boe_no = document.getElementById("boe_no"+index);
	var boe_dt = document.getElementById("boe_dt"+index);
	var qq_no = document.getElementById("qq_no"+index);
	var qq_dt = document.getElementById("qq_dt"+index);
	var cargo_no = document.getElementById("cargo_no"+index);
	
	var storage_start_dt = document.getElementById("storage_start_dt"+index);
	var storage_days = document.getElementById("storage_days"+index);
	var storage_ext_days = document.getElementById("storage_ext_days"+index);
	
	if(chk.checked)
	{
		cargo_no.disabled=false;
		cargo_number.disabled=false;
		vessel_name.disabled=false;
		supplier_name.disabled=false;
		vessel_cd.disabled=false;
		supplier_cd.disabled=false;
		window_start_dt.disabled=false;
		window_end_dt.disabled=false;
		actual_recpt_dt.disabled=false;
		edq_qty.disabled=false;
		adq_qty.disabled=false;
		supp_qty.disabled=false;
		csoc_qty.disabled=false;
		boe_qty.disabled=false;
		boe_no.disabled=false;
		boe_dt.disabled=false;
		qq_no.disabled=false;
		qq_dt.disabled=false;
		storage_start_dt.disabled=false;
		storage_days.disabled=false;
		storage_ext_days.disabled=false;
		
		var sup_nm = document.getElementById('supplier_name'+index).value;
		var vess_nm = document.getElementById('vessel_name'+index).value;
		
		if(sup_nm == "")
		{
			document.getElementById('supplier_name'+index).style="background:#ffffff";
		}
		if(vess_nm == "")
		{
			document.getElementById('vessel_name'+index).style="background:#ffffff";
		}
	}
	else
	{
		cargo_no.disabled=true;
		cargo_number.disabled=true;
		vessel_name.disabled=true;
		supplier_name.disabled=true;
		vessel_cd.disabled=true;
		supplier_cd.disabled=true;
		window_start_dt.disabled=true;
		window_end_dt.disabled=true;
		actual_recpt_dt.disabled=true;
		edq_qty.disabled=true;
		adq_qty.disabled=true;
		supp_qty.disabled=true;
		csoc_qty.disabled=true;
		boe_qty.disabled=true;
		boe_no.disabled=true;
		boe_dt.disabled=true;
		qq_no.disabled=true;
		qq_dt.disabled=true;
		storage_start_dt.disabled=true;
		storage_days.disabled=true;
		storage_ext_days.disabled=true;
		
		var sup_nm = document.getElementById('supplier_name'+index).value;
		var vess_nm = document.getElementById('vessel_name'+index).value;
		
		if(sup_nm == "")
		{
			document.getElementById('supplier_name'+index).style="background:#e9ecef";
		}
		if(vess_nm == "")
		{
			document.getElementById('vessel_name'+index).style="background:#e9ecef";
		}
	}
}

function setTotalADQDetail(total_qty_value,id_no)
{
	document.getElementById('adq_qty'+id_no).value  = total_qty_value;
	
	setsuppQty(id_no);
}

function checkRateFormat(id, index, a, b) {
    let rateInput = document.getElementById(id + index).value.trim();

    let hasDecimal = rateInput.includes('.');

    let parts = rateInput.split('.');
    let integerPart = parts[0];
    let decimalPart = parts[1];

    if (integerPart.length > a) {
    	alert("Please, Enter In the Required  Format..("+a+" ,"+b+")");
        document.getElementById(id + index).value = "";
        return;
    }

    if (hasDecimal && decimalPart.length > b) {
        alert("Please, Enter In the Required  Format..("+a+" ,"+b+")");
        document.getElementById(id + index).value = "";
        return;
    }

    if (!hasDecimal && rateInput.length > a) {
    	 alert("Please, Enter In the Required  Format..("+a+" ,"+b+")");
        document.getElementById(id + index).value = "";
        return;
    }

    if (isNaN(rateInput) || parseFloat(rateInput) <= 0) {
        alert("Please enter a valid Number!!");
        document.getElementById(id + index).value = "";
        return;
    }
}

function parseDate(dateStr) {
    const parts = dateStr.split('/');
    
    if (parts.length !== 3) 
    {
        return null;
    }
    const [day, month, year] = parts;
    return new Date(year+"-"+month+"-"+day);
}

function validateDtToPeriod(entDtValue,index) 
{
    const startDtValue = document.forms[0].start_dt.value;
    const endDtValue = document.forms[0].end_dt.value;

    const startDt = parseDate(startDtValue);
    const endDt = parseDate(endDtValue);
    const entDt = parseDate(entDtValue);
    
    if (!startDt || !endDt || !entDt) {
        alert("One or more dates are invalid.");
        return;
    }

    if (entDt < startDt || entDt > endDt)
    {
        alert("The entered date exceeds contract period!!");
        
        document.getElementById('window_start_dt'+index).value="";
        document.getElementById('window_end_dt'+index).value="";
    } 
}

function openRequestModification(index)
{
	var u = document.forms[0].u.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var cargo_no = document.getElementById('cargo_no'+index).value;
	var cargo_number = document.getElementById('cargo_number'+index).value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_ref_no = document.forms[0].cont_ref_no.value;
	var cont_rev_no = document.forms[0].cont_rev.value;
	var cont_name = document.forms[0].cont_name.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var contract_type = document.forms[0].contract_type.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var agmt_type = document.forms[0].agmt_type.value;
	var buy_sell = document.forms[0].buy_sell.value;
	var sug_per = document.forms[0].sug_per.value;
	var chk = document.getElementById("chk"+index);
	var flag=true;
	
	var url = "frm_ltcora_cargo_request_modification.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agmt_type+"&sug_per="+sug_per+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev+"&contract_type="+contract_type+"&cargo_no="+cargo_no+"&cargo_number="+cargo_number+
			"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&buy_sale="+buy_sell+"&u="+u;
	
	if(chk.checked)
	{
		if(flag)
		{
			if(!newWindow || newWindow.closed)
			{
				newWindow = window.open(url,"Cargo Request for Modification","top=10,left=10,width=1100,height=600,scrollbars=1");
			}
			else
			{
				newWindow.close();
				newWindow = window.open(url,"Cargo Request for Modification","top=10,left=10,width=1100,height=600,scrollbars=1");
			}
		}
		else
		{
			alert(msg);
		}
	}
	else
	{
		alert("Please Select Cargo!!");
	}
	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.ltcora.DataBean_LtcoraMaster" id="dbltcora" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>

<%
String sysdate = utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String counterparty_name=utilBean.getCounterpartyName(counterparty_cd);
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
String disp_cont_no = request.getParameter("disp_cont_no")==null?"":request.getParameter("disp_cont_no");
String cont_name = request.getParameter("cont_name")==null?"":request.getParameter("cont_name");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String buy_sell = request.getParameter("buy_sell")==null?"":request.getParameter("buy_sell");
int no_cargo = Integer.parseInt(request.getParameter("no_cargo")==null?"0":request.getParameter("no_cargo"));
String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String sug_per = request.getParameter("sug_per")==null?"0.00":request.getParameter("sug_per");

String cargo_no="";

//String cargo_pre_no=owner_cd+contract_type+counterparty_cd+"-"+agmt_no+"-"+cont_no+cargo_no;


dbltcora.setCallFlag("LTCORA_CN_CARGO_DTL");
dbltcora.setComp_cd(owner_cd);
dbltcora.setCounterparty_cd(counterparty_cd);
dbltcora.setAgmt_no(agmt_no);
dbltcora.setAgmt_rev_no(agmt_rev);
dbltcora.setAgmt_type(agmt_type);
dbltcora.setCont_no(cont_no);
dbltcora.setCont_rev_no(cont_rev);
dbltcora.setContract_type(contract_type);
dbltcora.setBuy_sale(buy_sell);
dbltcora.setNo_cargo(""+no_cargo);
dbltcora.init();

Vector VCOUNTERPARTY_CD = dbltcora.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbltcora.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbltcora.getVCOUNTERPARTY_ABBR();

Vector VCARGO_REF_NO = dbltcora.getVCARGO_REF_NO();
Vector VSHIP_CD = dbltcora.getVSHIP_CD();
Vector VSUPP_CD = dbltcora.getVSUPP_CD();
Vector VSHIP_NM = dbltcora.getVSHIP_NM();
Vector VSUPP_NM = dbltcora.getVSUPP_NM();
Vector VEDQ_FROM_DT = dbltcora.getVEDQ_FROM_DT();
Vector VEDQ_TO_DT = dbltcora.getVEDQ_TO_DT();
Vector VACTUAL_RECPT_DT = dbltcora.getVACTUAL_RECPT_DT();
Vector VEDQ_QTY = dbltcora.getVEDQ_QTY();
Vector VCSOC_QTY = dbltcora.getVCSOC_QTY();
Vector VBOE_QTY = dbltcora.getVBOE_QTY();
Vector VBOE_NO = dbltcora.getVBOE_NO();
Vector VBOE_DT = dbltcora.getVBOE_DT();
Vector VQQ_NO = dbltcora.getVQQ_NO();
Vector VQQ_DT = dbltcora.getVQQ_DT();
Vector VTOTAL_ADQ_QTY = dbltcora.getVTOTAL_ADQ_QTY();
Vector VSTORAGE_DAYS = dbltcora.getVSTORAGE_DAYS();
Vector VSTORAGE_EXT_DAYS = dbltcora.getVSTORAGE_EXT_DAYS();
Vector VSUG_PER = dbltcora.getVSUG_PER();

NumberFormat nf = new DecimalFormat("###########0.00");
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_LtcoraMaster">
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
					    	LTCORA (Buy) CN/Period Cargo Details
					    </div>
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
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh();">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="button" class="btn btn-info btn-sm select_btn" value="Select CN/Period" onclick="openContractList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="cont_name" value="<%=cont_name%>" readOnly>
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label title="Confirmation No#" class="form-label"><b>LTCORA (Buy) CN/Period</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-6 col-xs-6 col-md-6">
				    				<input type="text" class="form-control form-control-sm" name="cargo_number_disp" value="<%=disp_cont_no%>" maxLength="50" readOnly>
				    				<input type="hidden"name="cont_cargo_number" value="<%=disp_cont_no%>">
				      			</div>
				      			<div class="col-sm-6 col-xs-6 col-md-6">
				    				<input type="text" class="form-control form-control-sm" name="cont_ref_no" value="<%=cont_ref_no%>" maxLength="50" readOnly>
				      				<input type="hidden" name="buy_sell" id="buy_sell" value="<%=buy_sell%>">
				      				<input type="hidden" name="agmt_no" id="agmt_no" value="<%=agmt_no%>">
				      				<input type="hidden" name="agmt_rev" id="agmt_rev" value="<%=agmt_rev%>">
				      				<input type="hidden" name="agmt_type" id="agmt_type" value="<%=agmt_type%>">
				      				<input type="hidden" name="contract_type" id="contract_type" value="<%=contract_type%>">
				      				<input type="hidden" name="cont_rev" id="cont_rev" value="<%=cont_rev%>">
				      				<input type="hidden" name="cont_no" id="cont_no" value="<%=cont_no%>">
				      				<input type="hidden" name="no_cargo" id="no_cargo" value="<%=no_cargo%>">
				      				<input type="hidden" name="sug_per" id="sug_per" value="<%=sug_per%>">
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Contract Period</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="disp_start_dt" value="<%=start_dt%> - <%=end_dt%>" maxLength="10" disabled="disabled">
			      						<!-- <span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span> -->
		      						</div>
				    			</div>
				  			</div>
						</div>
						<%-- <div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="disp_end_dt" value="<%=end_dt%>" maxLength="10" disabled="disabled">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div> --%>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>No. of Cargo</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="no_cargo_number" value="<%=no_cargo%>" maxLength="50" disabled>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<%if(!cont_name.equals("")){%>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=counterparty_name%> <%if(contract_type.equals("G")){%>CN <%}else if(contract_type.equals("P")){ %>Period <%} %>Cargo Details</label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="payable">
								<thead>
									<tr>
										<th rowspan="2"><%if(write_access.equals("Y")){%>Select<%}else{ %>Sr#<%} %></th>
										<th rowspan="2">
											Cargo Ref No
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Cargo_Ref_No" onkeyup="Search(this,'1','');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">
											 Vessel<span class="s-red">*</span>
											<!-- <br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Vessel_name" onkeyup="Search(this,'2','');" placeholder="Search.." style="width:100px"/></div> -->
										</th>
										<th rowspan="2">
											Supplier
											<!-- <br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Supplier_name" onkeyup="Search(this,'3','');" placeholder="Search.." style="width:100px"/></div> -->
										</th>
										<th colspan="2">Arrival Window<span class="s-red">*</span></th>
										<th rowspan="2">
											Actual Receipt Date
										</th>
										<th rowspan="2">EDQ MMBTU<span class="s-red">*</span></th>
										<th rowspan="2">ADQ MMBTU</th>
										<th rowspan="2" >Qty To Be Supplied</th>
										<th rowspan="2" >CSOC MMBTU<span class="s-red">*</span></th>
										<th rowspan="2">Variable CSOC</th>												
										<th rowspan="2">BOE MMBTU</th>
										<th rowspan="2">BOE Number</th>
										<th rowspan="2">BOE Date</th>
										<th rowspan="2">Q & Q Certificate#</th>
										<th rowspan="2">Q & Q Certificate Date</th>
										<th rowspan="2">Storage Details<span class="s-red">*</span></th>
										<th rowspan="2" >Commercial Terms</th>
										<th rowspan="2">Closure Date</th>
										<th rowspan="2">Quantity Allocated till Clouser</th>
										<th rowspan="2"></th>
										<th rowspan="2"></th>
									</tr>
									<tr>
										<th>From Date</th>
										<th>To Date</th>
									</tr>
								</thead>
								<tbody>
								<%for(int i=0;i<VCARGO_REF_NO.size();i++){ %>
									<tr>
										<td align="center">
											<%if(write_access.equals("Y")){%>
											<input type="checkbox" class="form-check-input" name="chk" id="chk<%=i%>" onclick="enable_disable('<%=i%>');">
											<input type="hidden" name="chk_flag" id="chk_flag<%=i%>" value="N" >
											<%}else {%><%=i+1 %><%} %>
										</td>
										<td align="center"><%=VCARGO_REF_NO.elementAt(i)%>
											<input type="hidden" name="cargo_number" id="cargo_number<%=i%>" value="<%=VCARGO_REF_NO.elementAt(i)%>" disabled maxlength="20">
											<input type="hidden" name="cargo_no" id="cargo_no<%=i%>" value="<%=i+1%>" disabled>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
													<input type="text" class="form-control form-control-sm" name="vessel_name" id="vessel_name<%=i %>" value="<%=VSHIP_NM.elementAt(i)%>" readonly <%if(!VSHIP_NM.elementAt(i).equals("")){%>style="background: #9effce"<%} %>>
													<span onclick="selectShip('<%=i%>');" title="Select Vessel" style="background: #31d2f2" class="input-group-text"><i class="fa fa-ship" aria-hidden="true"></i></span>
													<input type="hidden" name="vessel_cd" id="vessel_cd<%=i %>" value="<%=VSHIP_CD.elementAt(i)%>" disabled>
												</div>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
													<input type="text" class="form-control form-control-sm" name="supplier_name" id="supplier_name<%=i %>" value="<%=VSUPP_NM.elementAt(i)%>" readonly <%if(!VSUPP_NM.elementAt(i).equals("")){%>style="background: #9effce"<%} %>>
													<span style="background: #31d2f2" title="Select Supplier" onclick="selectSupplier('<%=i%>');"x class="input-group-text"><i class="fa fa-building-o" aria-hidden="true"></i></span>
													<input type="hidden" name="supplier_cd" id="supplier_cd<%=i %>" value="<%=VSUPP_CD.elementAt(i)%>" disabled>
												</div>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="window_start_dt" id="window_start_dt<%=i %>" 
						      						value="<%=VEDQ_FROM_DT.elementAt(i)%>" maxLength="10" 
													onblur="validateDate(this);checkStartEndDate(this,document.forms[0].window_end_dt<%=i %>,'F');" 
			      									onchange="validateDate(this);checkStartEndDate(this,document.forms[0].window_end_dt<%=i %>,'F');validateDtToPeriod(document.getElementById('window_start_dt<%=i %>').value,'<%=i%>')"
			      									autocomplete="off" disabled <%if(!VEDQ_FROM_DT.elementAt(i).equals("")){%>style="background: #9effce"<%} %>>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div> 
					      					</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="window_end_dt" id="window_end_dt<%=i %>" 
						      						value="<%=VEDQ_TO_DT.elementAt(i)%>" maxLength="10" 
						      						onblur="validateDate(this);checkStartEndDate(document.forms[0].window_start_dt<%=i %>,this,'T');" 
			      									onchange="validateDate(this);checkStartEndDate(document.forms[0].window_start_dt<%=i %>,this,'T');validateDtToPeriod(document.getElementById('window_end_dt<%=i %>').value,'<%=i%>')" autocomplete="off" disabled 
			      									<%if(!VEDQ_TO_DT.elementAt(i).equals("")){%>style="background: #9effce"<%} %>>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div> 
					      					</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="actual_recpt_dt" id="actual_recpt_dt<%=i%>" 
						      						value="<%=VACTUAL_RECPT_DT.elementAt(i)%>" maxLength="10" onchange="validateDate(this);" autocomplete="off" disabled
						      						<%if(!VACTUAL_RECPT_DT.elementAt(i).equals("")){%>style="background: #9effce"<%} %>>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div> 
					      					</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="edq_qty" id="edq_qty<%=i%>" value="<%=VEDQ_QTY.elementAt(i)%>" disabled 
												onblur="checkRateFormat('edq_qty','<%=i%>','8','2');setsuppQty(<%=i %>);negNumber(this);" maxlength="11"
												<%if(!VEDQ_QTY.elementAt(i).equals("0.00")){%>style="background: #9effce"<%} %>>
											</div>
										</td>
										<td align="center">
										    <div class="form-group d-flex justify-content-between">
										        <div style="width:100px;">
										            <input type="text" class="form-control form-control-sm" name="adq_qty" id="adq_qty<%=i%>" value="<%=VTOTAL_ADQ_QTY.elementAt(i)%>" readonly disabled>
										        </div>
										        <div style="width:150px;">
										            <input type="button" name="adq_qty_btn" id="adq_qty_btn" class="btn btn-sm config_btn" value="Unloaded Quantity" onclick="openADQDtl('<%=i%>');" style="font-weight: bold;">
										        </div>
										        <!-- <div style="width:100px;">
										            <select class="form-select form-select-sm" name="" onchange="">
										                <option value="0">Days</option>
										            </select>
										        </div> -->
										    </div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
													<%String sug = ""+VSUG_PER.elementAt(i);
														String sug_percentage ="";
														if(!sug.equals(""))
														{
															sug_percentage=sug;
														}
														else
														{
															sug_percentage=sug_per;
														}%>
													<% 
													
													String supp_qty ="";
													String total_adq_str = "";
													
													if(!VTOTAL_ADQ_QTY.elementAt(i).equals(""))
													{
														total_adq_str = ""+VTOTAL_ADQ_QTY.elementAt(i);
													}
													
													if(!total_adq_str.equals(""))
													{ 
														supp_qty = ""+(Double.parseDouble(""+total_adq_str)- ((Double.parseDouble(""+total_adq_str)*Double.parseDouble(sug_percentage)/100)));
													}
													else
													{
														supp_qty = ""+(Double.parseDouble(""+VEDQ_QTY.elementAt(i))- ((Double.parseDouble(""+VEDQ_QTY.elementAt(i))*Double.parseDouble(sug_percentage)/100)));
													}
													
													%>
													<div class="hover-text">
														<input type="text" class="form-control form-control-sm" name="supp_qty" id="supp_qty<%=i%>" value="<%=supp_qty%>" readonly disabled>
														<span class="tooltip-text" id="bottom">
															<i class='fa fa-info-circle fa-lg'></i>&nbsp;
															<%
															if(supp_qty.equals(""))
															{
																supp_qty = "0";
															}
															
															total_adq_str = "";
															
															if(!VTOTAL_ADQ_QTY.elementAt(i).equals(""))
															{
																total_adq_str = ""+VTOTAL_ADQ_QTY.elementAt(i);
															}
															
															if(!total_adq_str.equals(""))
															{ %>
																Supplied Quantity <br>= IF (ADQ > 0) {ADQ MMBTU - (ADQ MMBTU*SUG%)}<br>&nbsp;&nbsp;&nbsp;ELSE { EDQ MMBTU - (EDQ MMBTU*SUG%)}<%if(!total_adq_str.equals("")){ %><br>=<%=total_adq_str%>-(<%=total_adq_str%>*<%=sug_percentage%>%)<br>=<%=(supp_qty)%><%} %>
															<%}else{ %>
																Supplied Quantity <br>= IF (ADQ > 0) {ADQ MMBTU - (ADQ MMBTU*SUG%)}<br>&nbsp;&nbsp;&nbsp;ELSE { EDQ MMBTU - (EDQ MMBTU*SUG%)}<%if(!VEDQ_QTY.elementAt(i).equals("0.00")){ %><br>=<%=VEDQ_QTY.elementAt(i)%>-(<%=VEDQ_QTY.elementAt(i)%>*<%=sug_percentage%>%)<br>=<%=(supp_qty) %><%} %>
															<%} %>
														</span>
													</div>
												</div>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
													<input type="text" class="form-control form-control-sm" name="csoc_qty" id="csoc_qty<%=i%>" value="<%=VCSOC_QTY.elementAt(i)%>" 
													disabled onblur="checkRateFormat('csoc_qty','<%=i%>','7','2');negNumber(this);" maxlength="10"
													<%if(!VCSOC_QTY.elementAt(i).equals("0.00")){%>style="background: #9effce"<%} %>>
												</div>
											</div>
										</td>
										<td align="center">
											<div >
												<input type="button" name="sel_cha_btn" id="sel_cha_btn" class="btn btn-sm config_btn" value="Variable CSOC" onclick="openVarCSOC('<%=i%>')" style="font-weight: bold;" <%if(VCSOC_QTY.elementAt(i).equals("0.00") || VCSOC_QTY.elementAt(i).equals("")){%>disabled="disabled"<%} %>>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
													<input type="text" class="form-control form-control-sm" name="boe_qty" id="boe_qty<%=i%>" value="<%=VBOE_QTY.elementAt(i)%>" 
													disabled onblur="checkRateFormat('boe_qty','<%=i%>','8','2');negNumber(this);" maxlength="11"
													<%if(!VBOE_QTY.elementAt(i).equals("0.00")){%>style="background: #9effce"<%} %>>
												</div>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
													<input type="text" class="form-control form-control-sm" name="boe_no" id="boe_no<%=i%>" value="<%=VBOE_NO.elementAt(i)%>"
													 disabled maxlength="25" <%if(!VBOE_NO.elementAt(i).equals("")){%>style="background: #9effce"<%} %>>
												</div>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="boe_dt" id="boe_dt<%=i%>" 
						      						value="<%=VBOE_DT.elementAt(i)%>" maxLength="10" onchange="validateDate(this);" autocomplete="off" disabled
						      						<%if(!VBOE_DT.elementAt(i).equals("")){%>style="background: #9effce"<%} %>>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div> 
					      					</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
													<input type="text" class="form-control form-control-sm" name="qq_no" id="qq_no<%=i%>" value="<%=VQQ_NO.elementAt(i)%>" 
													disabled maxlength="50" <%if(!VQQ_NO.elementAt(i).equals("")){%>style="background: #9effce"<%} %>>
												</div>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="qq_dt" id="qq_dt<%=i%>" 
						      						value="<%=VQQ_DT.elementAt(i)%>" maxLength="10" onchange="validateDate(this);" autocomplete="off" disabled
						      						<%if(!VQQ_DT.elementAt(i).equals("")){%>style="background: #9effce"<%} %>>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div> 
					      					</div>
										</td>
										<td align="center" title="">
											<div >
												<input type="button" name="storage_dlt_btn" id="storage_dlt_btn" class="btn btn-sm config_btn" value="Storage Details" onclick="openStorageDtls('<%=i %>');" style="font-weight: bold;">
												<input type="hidden" name="storage_start_dt" id="storage_start_dt<%=i%>" value="<%=VACTUAL_RECPT_DT.elementAt(i)%>" disabled>
												<input type="hidden" name="storage_days" id="storage_days<%=i%>" value="<%=VSTORAGE_DAYS.elementAt(i)%>" disabled>
												<input type="hidden" name="storage_ext_days" id="storage_ext_days<%=i%>" value="<%=VSTORAGE_EXT_DAYS.elementAt(i)%>" disabled>
											</div>
										</td>		
										<td align="center">
											<div >
												<input type="button" name="req_mod_btn" id="req_mod_btn" class="btn btn-sm request_btn" value="Request for Modification" onclick="openRequestModification('<%=i %>');" <%if(VSHIP_CD.elementAt(i).equals("")){ %>style="background:#e6e6e6;color:black; font-weight: bold; pointer-events : none;"  <%} %>>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="closure_dt" id="closure_dt<%=i%>" 
						      						value="" maxLength="10" onchange="validateDate(this);" autocomplete="off" disabled>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div> 
					      					</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
													<input type="text" class="form-control form-control-sm" name="closure_qty" id="closure_qty<%=i %>" value="" disabled>
												</div>
											</div>
										</td>
										<td align="center">
											<div >
												<input type="button" name="sel_cha_btn" id="sel_cha_btn" class="btn btn-sm config_btn" value="Request for Closure" onclick=""style="background:#e6e6e6;color:black;" >
											</div>
										</td>
										<td align="center">
											<div >
												<input type="button" name="sel_cha_btn" id="sel_cha_btn" class="btn btn-sm config_btn" value="Request For Re-Open" onclick=""style="background:#e6e6e6;color:black;" >
											</div>
										</td>
									</tr>
									<%} %>
									<%-- <tr>
										<td colspan="26">
											<div align="center"><%=utilmsg.infoMessage("<b>No LTCORA & CN Cargo Details Available!</b>")%></div>
										</td>
									</tr> --%>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<%} %>
				<div class="card-footer cdfooter text-center">
					<%if(!cont_name.equals("")){%>
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="LTCORA_CN_CARGO_DTL">
<input type="hidden" name="start_dt" value="<%=start_dt%>">
<input type="hidden" name="end_dt" value="<%=end_dt%>">

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
<input type="hidden" name="sysdate" value="<%=sysdate%>">

</form>
</body>
<script>
function Search(obj, indx, j) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("payable"+j);
  	
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
</script>
</html>