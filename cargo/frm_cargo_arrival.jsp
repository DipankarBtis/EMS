
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh(opration)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var u = document.forms[0].u.value;
	
	var url = "frm_cargo_arrival.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}


function doSubmit()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var cargo_number = document.forms[0].cargo_no.value;
	var ship_cd = document.forms[0].vessel_cd.value;
	var act_arival_dt = document.forms[0].act_arival_dt.value;
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
	if(trim(act_arival_dt) == "")
	{
		msg+="Enter Actual Arrival Date!\n";
		flag=false;
	}

	if(flag)
	{
		var a;
		if(opration=="MODIFY")
		{
			a = confirm("Do you want to Modify Cargo Allocation?");
		}
		else
		{
			a = confirm("Do you want to Create New Cargo Allocation?");
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

var newWindow;

function openCargoList(operation)
{
	var u = document.forms[0].u.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = "N";
	
	var url = "frm_cargo_arival_list.jsp?operation="+operation+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&u="+u;
	
	/* if(counterparty_cd=="0" || counterparty_cd=="")
	{
		alert("Select Trader!");
	}
	else */
	{
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
}

function doSubmitBl()
{
	var bl_ref = document.forms[0].bill_of_lad_no.value;
	var bill_of_lad_dt = document.forms[0].bill_of_lad_dt.value;
	var bill_of_lad_no = document.forms[0].bl_no.value;
	var imp_dept_no = document.forms[0].imp_dept_no.value;
	var imp_code = document.forms[0].imp_code.value;
	var endors_dt = document.forms[0].endors_dt.value;
	var msg="";
	var flag=true;
	
	if(trim(bill_of_lad_no)=="" || bill_of_lad_no==0)
	{
		msg+="Please Enter BL No!\n";
		flag=false;
	}
	if(trim(bl_ref)=="" || bl_ref==0)
	{
		msg+="Please Enter Bill of Lading Number!\n";
		flag=false;
	}
	if(trim(bill_of_lad_dt)=="" || bill_of_lad_dt==0)
	{
		msg+="Please Enter Bill Of Lading Date!\n";
		flag=false;
	}
	/* if(trim(imp_dept_no)=="" || imp_dept_no==0)
	{
		msg+="Please Enter Import Dept. No.!\n";
		flag=false;
	} 
	if(trim(imp_code)=="" || imp_code==0)
	{
		msg+="Please Enter Importer code!\n";
		flag=false;
	}
	if(trim(endors_dt)=="" || endors_dt==0)
	{
		msg+="Please Enter Endorsement Date!\n";
		flag=false;
	}*/
	
	if(flag)
	{
		var a = confirm("Do you want to Modify the BL Details?");
		if(a)
		{
			document.forms[0].opration.value="BL_UPDATE";
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function doSubmitBoe()
{
	var boe_ref = document.forms[0].boe_ref.value;
	var be_no = document.forms[0].boe_no.value;
	var be_dt = document.forms[0].be_dt.value;
	var business_unit = document.forms[0].business_unit.value;
	var trader_plant = document.forms[0].trader_plant.value;
	var boe_expected_qunt = document.forms[0].boe_expected_qunt.value;
	var mt = document.forms[0].mt.value;
	var m3 = document.forms[0].m3.value;
	var actual_qunt = document.forms[0].actual_qunt.value;
	var actual_mt = document.forms[0].actual_mt.value;
	var actual_m3 = document.forms[0].actual_m3.value;
	var CD_Checkbox = document.forms[0].CD_Checkbox.value;
	var port_code = document.forms[0].port_code.value;
	var boe_provisional_price = document.forms[0].boe_provisional_price.value;
	var boe_provisional_price_unit = document.forms[0].boe_provisional_price_unit.value;
	var boe_final_price = document.forms[0].boe_final_price.value;
	var boe_final_price_unit = document.forms[0].boe_final_price_unit.value;
	var msg="";
	var flag=true;
	
	/* if(CD_Checkbox=="N")
	{
		document.forms[0].port_code.value="";
	}
	else if(CD_Checkbox=="")
	{
		document.forms[0].port_code.value="";
	} */
	
	if(trim(be_no)=="" || be_no=="")
	{
		msg+="Please Enter BOE#!\n";
		flag=false;
	}
	if(trim(boe_ref)=="" || boe_ref=="")
	{
		msg+="Please Enter BOE Number!\n";
		flag=false;
	}
	if(trim(be_dt)=="" || be_dt=="")
	{
		msg+="Please Enter BOE Date!\n";
		flag=false;
	}
	if(trim(business_unit)=="" || business_unit==0)
	{
		msg+="Please Select Business Unit!\n";
		flag=false;
	}
	if(trim(trader_plant)=="" || trader_plant==0)
	{
		msg+="Please Select Trader Plant!\n";
		flag=false;
	}
	if((trim(boe_expected_qunt)=="" || boe_expected_qunt==0) && (trim(mt)=="" || mt==0) && (trim(m3)=="" || m3==0))
	{
		msg+="Please Enter Expected Quantity of LNG!\n";
		flag=false;
	}
	if((trim(actual_qunt)=="" || actual_qunt==0) || (trim(actual_mt)=="" || actual_mt==0) || (trim(actual_m3)=="" || actual_m3==0))
	{
		msg+="Please Enter Actual Quantity(MMBTU/MT/SCM) of LNG!\n";
		flag=false;
	}
	if(trim(port_code)=="" || port_code==0)
	{
		msg+="Please Select Port Code!\n";
		flag=false;
	}
	
	if(trim(boe_provisional_price)=="" || boe_provisional_price==0)
	{
		msg+="Please Enter BOE Provisional Price!\n";
		flag=false;
	}
	if(trim(boe_provisional_price_unit)=="" || boe_provisional_price_unit==0)
	{
		msg+="Please Select BOE Provisional Price Unit!\n";
		flag=false;
	}
	
	if(trim(boe_final_price)=="" || boe_final_price==0)
	{
		msg+="Please Enter BOE Final Price!\n";
		flag=false;
	}
	if(trim(boe_final_price_unit)=="" || boe_final_price_unit==0)
	{
		msg+="Please Select BOE Final Price Unit!\n";
		flag=false;
	}
	
	
	
	if(flag)
	{
		var a = confirm("Do you want to Modify the BOE Details?");
		if(a)
		{
			document.forms[0].opration.value="BOE_UPDATE";
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function setArrivalValue(cargo_no,cont_no,contract_type,counterparty_cd,ship_cd,ship_name,cargo_name,cont_name,arrival_dt,cont_rev,agmt_no,agmt_rev,agmt_type,allocation_status,conf_volume)
{
	var opration = document.forms[0].opration.value;
	var u = document.forms[0].u.value;
	
	var url = "frm_cargo_arrival.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+"&cargo_no="+cargo_no+"&cont_no="+cont_no+"&ship_cd="+ship_cd+
			"&contract_type="+contract_type+"&ship_name="+ship_name+"&cargo_name="+cargo_name+"&cont_name="+cont_name+"&arrival_dt="+arrival_dt+
			"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&agmt_type="+agmt_type+"&conf_volume="+conf_volume+"&allocation_status="+allocation_status+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doModifyBoe(VBOE_NO,VBOE_BU_SEQ,VBOE_PLANT_SEQ,VBOE_QTY,VBOE_QTY_UNIT,VBOE_REF,VBOE_DT,VBOE_QTY_MT,VBOE_QTY_SCM,VBOE_ACT_QTY,VBOE_ACT_QTY_UNIT,VBOE_ACT_QTY_MT,VBOE_ACT_QTY_SCM,
		VBOE_CUSTOM_DUTY,VBOE_LOAD_PORT,VDISP_BOE_NO,VLINKED_BL,VBOE_PROVISIONAL_PRICE,VBOE_PROVISIONAL_PRICE_UNIT,VBOE_FINAL_PRICE,VBOE_FINAL_PRICE_UNIT)
{
	document.forms[0].disp_boe_no.value=VDISP_BOE_NO;
	document.forms[0].boe_no.value=VBOE_NO;
	document.forms[0].business_unit.value=VBOE_BU_SEQ;
	document.forms[0].trader_plant.value=VBOE_PLANT_SEQ;
	document.forms[0].boe_expected_qunt.value=VBOE_QTY;
	document.forms[0].boe_expected_qunt_unit.value=VBOE_QTY_UNIT;
	document.forms[0].boe_ref.value=VBOE_REF;
	document.forms[0].be_dt.value=VBOE_DT;
	document.forms[0].mt.value=VBOE_QTY_MT;
	document.forms[0].m3.value=VBOE_QTY_SCM;
	document.forms[0].actual_qunt.value=VBOE_ACT_QTY;
	document.forms[0].actual_qunt_unit.value=VBOE_ACT_QTY_UNIT;
	document.forms[0].actual_mt.value=VBOE_ACT_QTY_MT;
	document.forms[0].actual_m3.value=VBOE_ACT_QTY_SCM;
	document.forms[0].linked_bls.value=VLINKED_BL;
	
	document.forms[0].boe_provisional_price.value=VBOE_PROVISIONAL_PRICE;
	if(VBOE_PROVISIONAL_PRICE_UNIT!="")
	{
		document.forms[0].boe_provisional_price_unit.value=VBOE_PROVISIONAL_PRICE_UNIT;
	}
	else
	{
		document.forms[0].boe_provisional_price_unit.value="2";
	}
	
	document.forms[0].boe_final_price.value=VBOE_FINAL_PRICE;
	if(VBOE_FINAL_PRICE_UNIT!="")
	{
		document.forms[0].boe_final_price_unit.value=VBOE_FINAL_PRICE_UNIT;
	}
	else
	{
		document.forms[0].boe_final_price_unit.value="2";
	}
	
	
	if(VBOE_CUSTOM_DUTY=="Y")
	{
		document.forms[0].CD_Checkbox.value="Y";
	}
	else if(VBOE_CUSTOM_DUTY=="N")
	{
		document.forms[0].CD_Checkbox.value="N";
	}
	else
	{
		document.forms[0].CD_Checkbox.value="";
	}
	
	document.forms[0].port_code.value=VBOE_LOAD_PORT;
	
	document.forms[0].boe_opration.value="BOE_MOD";
}

function doModifyBl(VBL_NO,VBL_REF,VBL_DT,VBL_IMPORT_DEPT_SNO,VBL_IMPORT_CD,VBL_ENDORSE_DT,VBL_REMARK,VDISP_BL_NO)
{
	document.forms[0].disp_bl_no.value=VDISP_BL_NO;
	document.forms[0].bl_no.value=VBL_NO;
	document.forms[0].bill_of_lad_no.value=VBL_REF;
	document.forms[0].bill_of_lad_dt.value=VBL_DT;
	document.forms[0].imp_dept_no.value=VBL_IMPORT_DEPT_SNO;
	document.forms[0].imp_code.value=VBL_IMPORT_CD;
	document.forms[0].endors_dt.value=VBL_ENDORSE_DT;
	document.forms[0].app_bill_notes.value=VBL_REMARK;
	
	document.forms[0].bl_opration.value="BL_MOD";
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
						alert("Provided value should be in ("+maxVal+", "+precision+") format!!");
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

function totalActQty(unloaded_qty)
{
	document.forms[0].expected_qunt.value=unloaded_qty;
}

function alertforBlBoe()
{
	alert("Bill of Lading Document Details And BOE Details Will be enable for Modification after Submitting Cargo Arrival and Documentation!")
}

function checkArrivalDt()
{
	var act_arival_dt = document.forms[0].act_arival_dt.value;
	var expected_start_dt = document.forms[0].expected_start_dt.value;
	var expected_end_dt = document.forms[0].expected_end_dt.value;
	
	var splitAct_arrival_dt = act_arival_dt.split("/");
	var splitExpected_start_dt = expected_start_dt.split("/");
	var splitExpected_end_dt = expected_end_dt.split("/");
	
	var temp_act_arival_dt = splitAct_arrival_dt[2]+splitAct_arrival_dt[1]+splitAct_arrival_dt[0];
	var temp_expected_start_dt = splitExpected_start_dt[2]+splitExpected_start_dt[1]+splitExpected_start_dt[0];
	var temp_expected_end_dt = splitExpected_end_dt[2]+splitExpected_end_dt[1]+splitExpected_end_dt[0];
	
	if(temp_act_arival_dt!="")
	{
		if(temp_act_arival_dt > temp_expected_end_dt || temp_act_arival_dt < temp_expected_start_dt)
		{
			alert("Actual Arrival Date should be in range of Nomination Request Window ("+expected_start_dt+"-"+expected_end_dt+") !");
			document.forms[0].act_arival_dt.value="";
		}
	}
}

function calculateGCV(obj)
{
	
	var ghv_qq = obj.value; //document.forms[0].ghv_qq.value;
	var multiplying_factor = 0.948;
	var gcv_qq = 0;
	
	if(ghv_qq!=null)
	{
		ghv_qq = trim(ghv_qq);
				
		if(ghv_qq!='0' && ghv_qq!='0.0' && ghv_qq!='0.00' && ghv_qq!='' && ghv_qq!=' ')
		{
			gcv_qq = parseFloat(ghv_qq)*multiplying_factor;
			document.forms[0].qq_gcv.value = round(gcv_qq, 4);	
			
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.cargo.DataBean_Cargo_mst" id="arrival" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String dateTime = utildate.getSysdateWithTime24hr();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String clearance = request.getParameter("clearance")==null?"KYC":request.getParameter("clearance");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String agmt_type = request.getParameter("agmt_type")==null?"M":request.getParameter("agmt_type");
String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
String ship_cd = request.getParameter("ship_cd")==null?"":request.getParameter("ship_cd");
String ship_name = request.getParameter("ship_name")==null?"":request.getParameter("ship_name");
String cargo_name = request.getParameter("cargo_name")==null?"":request.getParameter("cargo_name");
String cont_name = request.getParameter("cont_name")==null?"":request.getParameter("cont_name");
String arrival_dt = request.getParameter("arrival_dt")==null?"":request.getParameter("arrival_dt");
String allocation_status = request.getParameter("allocation_status")==null?"":request.getParameter("allocation_status");
String conf_volume = request.getParameter("conf_volume")==null?"":request.getParameter("conf_volume");

arrival.setCallFlag("CARGO_ARRIVAL");
arrival.setClearance(clearance);
arrival.setCounterparty_cd(counterparty_cd);
arrival.setCargo_no(cargo_no);
arrival.setCargo_number(cargo_no);
arrival.setShip_cd(ship_cd);
arrival.setCont_no(cont_no);
arrival.setCont_rev_no(cont_rev);
arrival.setContract_type(contract_type);
arrival.setComp_cd(owner_cd);
arrival.setOpration(opration);
arrival.setAgmt_no(agmt_no);
arrival.setAgmt_rev_no(agmt_rev);
arrival.setAgmt_type(agmt_type);
arrival.setAllocation_status(allocation_status);
arrival.init();


String alloc_status = arrival.getAlloc_status();
String alloc_rev = arrival.getAlloc_rev();
String act_arrv_dt = arrival.getAct_arrv_dt();
String booked_dt = arrival.getBooked_dt();
String booked_time = arrival.getBooked_time();
String float_dt = arrival.getFloat_dt();
String float_time = arrival.getFloat_time();
String pob_dt = arrival.getPob_dt();
String pob_time = arrival.getPob_time();
String uac_dt = arrival.getUac_dt();
String uac_time = arrival.getUac_time();
String uadc_dt = arrival.getUadc_dt();
String uadc_time = arrival.getUadc_time();
String departure_dt = arrival.getDeparture_dt();
String departure_time = arrival.getDeparture_time();
String cargo_details_notes = arrival.getCargo_details_notes();
String qq_cer_no = arrival.getQq_cer_no();
String qq_cer_dt = arrival.getQq_cer_dt();
String net_unloaded_qunt = arrival.getNet_unloaded_qunt();
String net_unloaded_qunt_mt = arrival.getNet_unloaded_qunt_mt();
String net_unloaded_qunt_m3 = arrival.getNet_unloaded_qunt_m3();
String dens_material = arrival.getDens_material();
String qq_ghv = arrival.getQq_ghv();
String qq_gcv = arrival.getQq_gcv();
String app_lab_notes = arrival.getApp_lab_notes();
String expected_qunt = arrival.getExpected_qunt();
String num_bl=arrival.getNum_bl();
String num_boe=arrival.getNum_boe();
String cargo_ref = arrival.getCargo_ref();
String cont_ref_no = arrival.getCont_ref_no();
String mandate_conf_vol = arrival.getMandate_conf_vol();
String conf_price = arrival.getConf_price();
String conf_price_unit = arrival.getRate_unit();
String win_start_dt = arrival.getWin_start_dt();
String win_end_dt = arrival.getWin_end_dt();
String tolerance_per = arrival.getTolerance_per();
String expected_start_dt = arrival.getExpected_start_dt();
String expected_end_dt = arrival.getExpected_end_dt();
String all_fast_dt = arrival.getAll_fast_dt();
String all_fast_time = arrival.getAll_fast_time();
String custom_start_dt = arrival.getCustom_start_dt();
String custom_start_time = arrival.getCustom_start_time();
String custom_end_dt = arrival.getCustom_end_dt();
String custom_end_time = arrival.getCustom_end_time();

Vector VCOUNTRY_NM = arrival.getVCOUNTRY_NM();
Vector VCOUNTRY_ISO = arrival.getVCOUNTRY_ISO();

Vector VSEL_PLANT_SEQ_NO = arrival.getVSEL_PLANT_SEQ_NO();
Vector VSEL_SPLIT_VALUE = arrival.getVSEL_SPLIT_VALUE();
Vector VSEL_BU_CD = arrival.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = arrival.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_TRAD_CD = arrival.getVSEL_TRAD_CD();
Vector VSEL_PLANT_ABBR = arrival.getVSEL_PLANT_ABBR();
Vector VSEL_BU_PLANT_ABBR = arrival.getVSEL_BU_PLANT_ABBR();

Vector VBL_NO = arrival.getVBL_NO();
Vector VDISP_BL_NO = arrival.getVDISP_BL_NO();
Vector VBL_QTY = arrival.getVBL_QTY();
Vector VBL_LOADED_QTY = arrival.getVBL_LOADED_QTY();
Vector VBL_QTY_UNIT = arrival.getVBL_QTY_UNIT();
Vector VBL_PRICE = arrival.getVBL_PRICE();
Vector VBL_PRICE_UNIT = arrival.getVBL_PRICE_UNIT();
Vector VBL_REF = arrival.getVBL_REF();
Vector VBL_DT = arrival.getVBL_DT();
Vector VBL_IMPORT_DEPT_SNO = arrival.getVBL_IMPORT_DEPT_SNO();
Vector VBL_IMPORT_CD = arrival.getVBL_IMPORT_CD();
Vector VBL_ENDORSE_DT = arrival.getVBL_ENDORSE_DT();
Vector VBL_QTY_MT = arrival.getVBL_QTY_MT();
Vector VBL_QTY_SCM = arrival.getVBL_QTY_SCM();
Vector VBL_REMARK = arrival.getVBL_REMARK();
Vector VBL_EXP_MT = arrival.getVBL_EXP_MT();
Vector VBL_EXP_SCM = arrival.getVBL_EXP_SCM();

Vector VBOE_NO = arrival.getVBOE_NO();
Vector VDISP_BOE_NO = arrival.getVDISP_BOE_NO();
Vector VBOE_QTY = arrival.getVBOE_QTY();
Vector VBOE_QTY_UNIT = arrival.getVBOE_QTY_UNIT();
Vector VBOE_PRICE = arrival.getVBOE_PRICE();
Vector VBOE_PRICE_UNIT = arrival.getVBOE_PRICE_UNIT();
Vector VBOE_BU_SEQ = arrival.getVBOE_BU_SEQ();
Vector VBOE_PLANT_SEQ = arrival.getVBOE_PLANT_SEQ();
Vector VBOE_REF = arrival.getVBOE_REF();
Vector VBOE_DT = arrival.getVBOE_DT();
Vector VBOE_QTY_MT = arrival.getVBOE_QTY_MT();
Vector VBOE_QTY_SCM = arrival.getVBOE_QTY_SCM();
Vector VBOE_ACT_QTY = arrival.getVBOE_ACT_QTY();
Vector VBOE_ACT_QTY_UNIT = arrival.getVBOE_ACT_QTY_UNIT();
Vector VBOE_ACT_QTY_MT = arrival.getVBOE_ACT_QTY_MT();
Vector VBOE_ACT_QTY_SCM = arrival.getVBOE_ACT_QTY_SCM();
Vector VBOE_CUSTOM_DUTY = arrival.getVBOE_CUSTOM_DUTY();
Vector VBOE_LOAD_PORT = arrival.getVBOE_LOAD_PORT();
Vector VBOE_PROVISIONAL_PRICE = arrival.getVBOE_PROVISIONAL_PRICE();
Vector VBOE_PROVISIONAL_PRICE_UNIT = arrival.getVBOE_PROVISIONAL_PRICE_UNIT();
Vector VBOE_FINAL_PRICE = arrival.getVBOE_FINAL_PRICE();
Vector VBOE_FINAL_PRICE_UNIT = arrival.getVBOE_FINAL_PRICE_UNIT();

Vector VBOE_BU_ABBR = arrival.getVBOE_BU_ABBR();
Vector VBOE_PLANT_NM = arrival.getVBOE_PLANT_NM();
Vector VBOE_PLANT_ABBR = arrival.getVBOE_PLANT_ABBR();
Vector VNOMINATION_REV_NO = arrival.getVNOMINATION_REV_NO();
Vector VLINKED_BL = arrival.getVLINKED_BL();

double totalBlMMBTU =arrival.getTotalBlMMBTU();
double totalBlMT =arrival.getTotalBlMT();
double totalBlSCM =arrival.getTotalBlSCM();

double totalBOEMMBTU =arrival.getTotalBOEMMBTU();
double totalBOEMT =arrival.getTotalBOEMT();
double totalBOESCM =arrival.getTotalBOESCM();

double totalActBOEMMBTU =arrival.getTotalActBOEMMBTU();
double totalActBOEMT =arrival.getTotalActBOEMT();
double totalActBOESCM =arrival.getTotalActBOESCM();

if(booked_time.equals(""))
{
	booked_time="00:00";
}
if(float_time.equals(""))
{
	float_time="00:00";
}
if(pob_time.equals(""))
{
	pob_time="00:00";
}
if(uac_time.equals(""))
{
	uac_time="00:00";
}
if(uadc_time.equals(""))
{
	uadc_time="00:00";
}
if(departure_time.equals(""))
{
	departure_time="00:00";
}
if(all_fast_time.equals(""))
{
	all_fast_time="00:00";
}
if(custom_start_time.equals(""))
{
	custom_start_time="00:00";
}
if(custom_end_time.equals(""))
{
	custom_end_time="00:00";
}

Vector VCOUNTERPARTY_CD = arrival.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = arrival.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = arrival.getVCOUNTERPARTY_ABBR();

%>
<body>
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
					    	Cargo Arrival & Documentation
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
								<label class="form-label"><b>Arrival Status:</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
			    				<div class="col-3">
				    				<label class="form-label"><b>
				    				<%if(allocation_status.equals("Allocated")){%>Unloaded<%}else if(allocation_status.equals("Pending")){ %>Expected<%}else if(allocation_status.equals("Custom")){ %>Custom<%} %>
				    				</b></label>
				    			</div>
				    			<%if(!allocation_status.equals("Pending")){ %>
				    			<div class="col-2">
				    				<input type="text" class="form-control form-control-sm" style="text-align:left;background:#99ffcc" disabled  value="<%=alloc_rev%>">		    				
				    				<input type="hidden"name="alloc_status" value="<%=allocation_status%>">			    				
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
				    				<input type="text" class="form-control form-control-sm" name="cargo_number" value="<%=cargo_name %>" maxLength="50" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="cargo_no" value="<%=cargo_no %>" maxLength="50" readOnly>
				      			</div>
				      			<div class="col-sm-6 col-xs-6 col-md-6">
				    				<input type="text" class="form-control form-control-sm" value="<%=cargo_ref %>" maxLength="50" readOnly>
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
				      				<input type="text" class="form-control form-control-sm" name="confirm_no" id="confirm_no" value="<%=cont_name %>" maxLength="25" readonly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_no" value="<%=cont_no %>" maxLength="25" readonly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_rev_no" value="<%=cont_rev %>" maxLength="25" readonly>
				      				<input type="hidden" class="form-control form-control-sm" name="contract_type" value="<%=contract_type %>" maxLength="25" readonly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no %>" maxLength="25" readonly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev %>" maxLength="25" readonly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_type" value="<%=agmt_type %>" maxLength="25" readonly>
				    			</div>
				    			<div class="col-sm-6 col-xs-6 col-md-6">
				    				<input type="text" class="form-control form-control-sm" value="<%=cont_ref_no %>" maxLength="50" readOnly>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<label class="form-label"><b>Confirmed Volume</b></label>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-6 col-xs-6 col-md-6">
				      	 			<input type="text" class="form-control form-control-sm" name="mandate_conf_vol" id="mandate_conf_vol" value="<%=mandate_conf_vol %>" maxLength="25" readOnly ><!-- onblur="checkValuePrecision(this.value, '6','14','mandate_conf_vol');" -->
				    			</div>
				    			<div class="col-sm-6 col-xs-6 col-md-6">
				    				<div class="input-group input-group-sm" >
					      	 			<input type="text" class="form-control form-control-sm" value="<%=tolerance_per %>" maxLength="25" readOnly >
					      	 			<span class="input-group-text"><b>%</b></span>
					      	 		</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label"><b>Confirmed Price</b></label>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="col-auto">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
					      				<input type="text" class="form-control form-control-sm" name="conf_price" id="conf_price" value="<%=conf_price %>" maxLength="25" readOnly ><!-- onblur="checkValuePrecision(this.value,'4','6','conf_price')" -->
					    				<input type="hidden" name="confirm_price_unit" value="<%=conf_price_unit%>">
					    				<%if(!conf_price_unit.equals("")) {%><span class="input-group-text"><b><%if(conf_price_unit.equals("2")) {%>USD/MMBTU<%}else{ %>INR/MMBTU<%} %></b></span><%} %>
					    			</div>
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
					</div>&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Cargo Arrival Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Vessel's Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="vessel_nm" id="vessel_nm" value="<%=ship_name %>" readonly>
				      				<input type="hidden" class="form-control form-control-sm" name="vessel_cd" id="vessel_cd" value="<%=ship_cd %>" readonly>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Actual Arrival Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="act_arival_dt" id="act_arival_dt" value="<%=act_arrv_dt %>" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);checkArrivalDt();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						<input type="hidden" class="form-control form-control-sm date fmsdtpick" name="expected_start_dt" id="expected_start_dt" value="<%=expected_start_dt %>">
			      						<input type="hidden" class="form-control form-control-sm date fmsdtpick" name="expected_end_dt" id="expected_end_dt" value="<%=expected_end_dt %>">
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Booked Date</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="booked_dt" value="<%=booked_dt %>" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="booked_time" value="<%=booked_time %>"  onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Float Date</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="float_dt" id="float_dt" value="<%=float_dt %>" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      					</div>
		      					<div class="col">
		      						<div class="input-group input-group-sm" >
		      							<input type="text" class="form-control form-control-sm" name="float_time" id="float_time"  value="<%=float_time %>"  onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
	      								<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
				    				</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Pilot On Board Date</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="pio_ob_dt" id="pio_ob_dt" value="<%=pob_dt %>" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="pio_ob_time" id="pio_ob_time"  value="<%=pob_time %>"  onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
		      					</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>All Fast Date</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="all_fast_dt" id="all_fast_dt" value="<%=all_fast_dt %>" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="all_fast_time" id="all_fast_time"  value="<%=all_fast_time %>"  onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Unloading Arm Connection Date</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="uac_dt" id="uac_dt" value="<%=uac_dt %>" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="uac_time" id="uac_time"  value="<%=uac_time %>"  onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
		      					</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Unloading Arm Dis. Connection Date</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="uadc_dt" id="uadc_dt" value="<%=uadc_dt %>" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="uadc_time" id="uadc_time"  value="<%=uadc_time %>"  onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Departure Date</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="departure_dt" id="departure_dt" value="<%=departure_dt %>" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="departure_time" id="departure_time"  value="<%=departure_time %>"  onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Custom Clearance Start Date</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="custom_start_dt" id="custom_start_dt" value="<%=custom_start_dt %>" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="custom_start_time" id="custom_start_time"  value="<%=custom_start_time %>"  onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Custom Clearance End Date</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="custom_end_dt" id="custom_end_dt" value="<%=custom_end_dt %>" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="custom_end_time" id="custom_end_time"  value="<%=custom_end_time %>"  onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
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
				    				<font size="1" face="arial, helvetica, sans-serif">&nbsp;( You may enter up to 150 characters. )&nbsp;
										<input readonly type=text name="remLen" size="3" maxlength="3" class=""> characters left
									</font><br>
				      				<textarea class="form-control" name="cargo_details_notes" cols="75" rows="1" onKeyDown="textCounter(this.form.cargo_details_notes,this.form.remLen,149);" onKeyUp="textCounter(this.form.cargo_details_notes,this.form.remLen,149);"><%=cargo_details_notes %></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
				
				<%-- <%if(allocation_status.equals("Allocated")){ %> --%>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Bill of Lading Document Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-between"> 
								<div></div>
								<!-- <div class="btn-group" align="right">
									<label class="btn btn-outline-secondary subbtngrp1" data-bs-toggle="modal" data-bs-target="#AddNewBl">Add New BL</label>
								</div> -->
							</div>
						</div>
					</div>
					<div class="row m-b-5">
				    	<div class="col-sm-6 col-xs-6 col-md-6">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>No. Of BL :</b></label>
								</div>
				    			<div class="col-auto">
			    					<input name="bl_number" id="bl_number" type="text" class="form-control form-control-sm" value="<%=num_bl %>" Readonly> <!-- onblur="blTable();" -->
				    			</div>
				    		</div>
				    	</div>
				    </div>
				    <div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="cargo_bl" style="width:100%;"> 
								<thead>
									<tr>
										<%if(write_access.equals("Y")){ %>
										<th rowspan="2">Select</th>
										<%}else{ %>
										<th rowspan="2">Sr#</th>
										<%} %>
										<th rowspan="2">BL#</th>
										<th rowspan="2">Bill Of Lading Date</th>
										<th rowspan="2">Bill Of Lading Number</th>
										
										<th rowspan="2">BL Price</th>
										<th rowspan="2">Price Unit</th>
										<th colspan="3">BL Quantity (REV)</th>
										<th rowspan="2">Import Dept. No.</th>
										<th rowspan="2">Importer Code</th>
										<th rowspan="2"><%=owner_abbr %> Endorsement Date</th>
										<th rowspan="2">Remark</th>
									</tr>
									<tr>
										<th>MMBTU</th>
										<th>MT</th>
										<th>SCM</th>
									</tr>
								</thead>
								<tbody id="itemTab_bl">
								<%if(VBL_NO.size()>0){ %>
									<%for(int i=0;i<VBL_NO.size();i++){%>
									<tr>
										<%if(write_access.equals("Y")){ %><td align="center">
											<font title="Click to Edit" style="color:var(--header_color)">
											<%if(!allocation_status.equals("Pending")){ %>
												<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#AddNewBl"
												onclick="doModifyBl('<%=VBL_NO.elementAt(i)%>','<%=VBL_REF.elementAt(i)%>','<%=VBL_DT.elementAt(i)%>','<%=VBL_IMPORT_DEPT_SNO.elementAt(i)%>',
												'<%=VBL_IMPORT_CD.elementAt(i)%>','<%=VBL_ENDORSE_DT.elementAt(i)%>','<%=VBL_REMARK.elementAt(i)%>','<%=VDISP_BL_NO.elementAt(i)%>')">
												</i>
											<%}else{ %>
												<i class="fa fa-edit fa-lg" onclick="alertforBlBoe()"></i>
											<%} %>
											</font>
										</td>
										<%}else{ %>
										<td><%=i+1%></td>
										<%} %>
										<td align="center"><%=VDISP_BL_NO.elementAt(i)%></td>
										<td align="center"><%=VBL_DT.elementAt(i)%></td>
										<td align="center"><%=VBL_REF.elementAt(i)%></td>
										
										<td align="right" style="background-color: #e9ecef"><%=VBL_PRICE.elementAt(i)%></td>
										<td align="center" style="background-color: #e9ecef"> <%if(VBL_PRICE_UNIT.elementAt(i).equals("2")){ %>USD/MMBTU<%}else{ %>INR/MMBTU<%} %></td>
										<td align="right" style="background-color: #e9ecef"><%=VBL_QTY.elementAt(i) %> (<%=VNOMINATION_REV_NO.elementAt(i) %>)</td>
										<td align="right" style="background-color: #e9ecef"><%=VBL_EXP_MT.elementAt(i) %></td>
										<td align="right" style="background-color: #e9ecef"><%=VBL_EXP_SCM.elementAt(i) %></td>
										<td align="center"><%=VBL_IMPORT_DEPT_SNO.elementAt(i)%></td>
										<td align="center"><%=VBL_IMPORT_CD.elementAt(i)%></td>
										<td align="center"><%=VBL_ENDORSE_DT.elementAt(i)%></td>
										<td><%=VBL_REMARK.elementAt(i)%></td>
									</tr>
									<%} %>
									<tbody>
										<tr>
											<td colspan="6" align="right" style="font-weight: bold; color: #203d78;">Total</td>
											<td align="right"><%if(totalBlMMBTU!=0){%><%=totalBlMMBTU %><%} %></td>
											<td align="right"><%if(totalBlMT!=0){%><%=totalBlMT %><%} %></td>
											<td align="right"><%if(totalBlSCM!=0){%><%=totalBlSCM %><%} %></td>
											<td colspan="4"></td>
										</tr>
									</tbody>
								<%}else{ %>
									<tr id="row">
										<td colspan="13" align="center">
											<%=utilmsg.infoMessage("<b>B/L Documentation Details is not Available!</b>") %>
										</td>
									</tr>
								<% }%>
								</tbody>
							</table>
						</div>
					</div> &nbsp;
					
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> BOE Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-between"> 
								<div></div>
								<!-- <div class="btn-group" align="right">
									<label class="btn btn-outline-secondary subbtngrp1" data-bs-toggle="modal" data-bs-target="#AddNewBoe">Add New BOE</label>
								</div> -->
							</div>
						</div>
					</div>
					 <div class="row m-b-5">
				    	<div class="col-sm-6 col-xs-6 col-md-6">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>No. Of BOE :</b></label>
								</div>
				    			<div class="col-auto">
			    					<input name="boe_number" id="boe_number" type="text" class="form-control form-control-sm" value="<%=num_boe %>" Readonly> <!-- onblur="boeTable();" -->
				    			</div>
				    		</div>
				    	</div>
				    </div>
				    <div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="cargo_boe" style="width:100%;"> 
								<thead>
									<tr>
										<%if(write_access.equals("Y")){ %>
										<th rowspan="2">Select</th>
										<%}else{ %>
										<th rowspan="2">Sr#</th>
										<%} %>
										<th rowspan="2">BOE#</th>
										<th rowspan="2">BOE Date</th>
										<th rowspan="2">BOE Number</th>
										<th rowspan="2">Business Unit</th>
										<th rowspan="2">Trader Plant</th>
										<th rowspan="2">BOE Price</th>
										<th rowspan="2">Price Unit</th>
										<th colspan="3">Expected Quantity Of LNG (REV)</th>
										<th colspan="3">Actual Quantity Of LNG</th>
										<th rowspan="2">Linked BL/s</th>
										<th rowspan="2">Custom Duty</th>
										<th rowspan="2">Port Code</th>
										<th rowspan="2">BOE Provisional Price</th>
										<th rowspan="2">BOE Provisional Price Unit</th>
										<th rowspan="2">BOE Final Price</th>
										<th rowspan="2">BOE Final Price Unit</th>
									</tr>
									<tr>
										<th>MMBTU</th>
										<th>MT</th>
										<th>SCM</th>
										<th>MMBTU</th>
										<th>MT</th>
										<th>SCM</th>
									</tr>
								</thead>
								<tbody id="itemTab_boe">
								<%if(VBOE_NO.size()>0){ %>
									<%for(int j=0;j<VBOE_NO.size();j++){%>
									<tr>
										<%if(write_access.equals("Y")){ %><td align="center">
											<font title="Click to Edit" style="color:var(--header_color)">
											<%if(!allocation_status.equals("Pending")){ %>
												<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#AddNewBoe" 
												onclick="doModifyBoe('<%=VBOE_NO.elementAt(j)%>','<%=VBOE_BU_SEQ.elementAt(j)%>','<%=VBOE_PLANT_SEQ.elementAt(j)%>','<%=VBOE_QTY.elementAt(j)%>','<%=VBOE_QTY_UNIT.elementAt(j)%>','<%=VBOE_REF.elementAt(j)%>','<%=VBOE_DT.elementAt(j)%>','<%=VBOE_QTY_MT.elementAt(j)%>',
												'<%=VBOE_QTY_SCM.elementAt(j)%>','<%=VBOE_ACT_QTY.elementAt(j)%>','<%=VBOE_ACT_QTY_UNIT.elementAt(j)%>','<%=VBOE_ACT_QTY_MT.elementAt(j)%>','<%=VBOE_ACT_QTY_SCM.elementAt(j)%>','<%=VBOE_CUSTOM_DUTY.elementAt(j)%>','<%=VBOE_LOAD_PORT.elementAt(j)%>','<%=VDISP_BOE_NO.elementAt(j)%>','<%=VLINKED_BL.elementAt(j)%>',
												'<%=VBOE_PROVISIONAL_PRICE.elementAt(j)%>','<%=VBOE_PROVISIONAL_PRICE_UNIT.elementAt(j)%>','<%=VBOE_FINAL_PRICE.elementAt(j)%>','<%=VBOE_FINAL_PRICE_UNIT.elementAt(j)%>')">
												</i>
											<%}else{ %>
												<i class="fa fa-edit fa-lg" onclick="alertforBlBoe()"></i>
											<%} %>
											</font>
										</td>
										<%}else{ %>
										<td><%=j+1%></td>
										<%} %>
										<td align="center"><%=VDISP_BOE_NO.elementAt(j) %></td>
										<td align="center"><%=VBOE_DT.elementAt(j)%></td>
										<td align="center"><%=VBOE_REF.elementAt(j)%></td>
										<td align="center"><%=VBOE_BU_ABBR.elementAt(j) %></td>
										<td align="center"><%=VBOE_PLANT_ABBR.elementAt(j) %></td>
										<td align="right" style="background-color: #e9ecef"><%=VBOE_PRICE.elementAt(j)%></td>
										<td align="center" style="background-color: #e9ecef"> <%if(VBOE_PRICE_UNIT.elementAt(j).equals("2")){ %>USD/MMBTU<%}else{ %>INR/MMBTU<%} %></td>
										<td align="right" style="background-color: #e9ecef"><%=VBOE_QTY.elementAt(j) %>(<%=VNOMINATION_REV_NO.elementAt(j) %>)
											<%-- <div class="col-sm-12 col-xs-12 col-md-12">  
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm" value="<%=VBOE_QTY.elementAt(j) %> (<%=VNOMINATION_REV_NO.elementAt(j) %>)" <%if(!VBOE_QTY.elementAt(j).equals("")){ %>style="background:#99ffcc; text-align: right;"<%} %> readonly>
						      						<span class="input-group-text"><b>MMBTU</b></span>
						      						<input type="text" class="form-control form-control-sm" value="<%=VBOE_QTY_MT.elementAt(j) %>" <%if(!VBOE_QTY_MT.elementAt(j).equals("")){ %>style="background:#99ffcc; text-align: right;"<%} %> readonly>
						      						<span class="input-group-text"><b>MT</b></span>
						      						<input type="text" class="form-control form-control-sm" value="<%=VBOE_QTY_SCM.elementAt(j) %>" <%if(!VBOE_QTY_SCM.elementAt(j).equals("")){ %>style="background:#99ffcc; text-align: right;"<%} %> readonly>
						      						<span class="input-group-text"><b>SCM</b></span> 
					      						</div>
											</div> --%>
										</td>
										<td align="right" style="background-color: #e9ecef"><%=VBOE_QTY_MT.elementAt(j) %></td>
										<td align="right" style="background-color: #e9ecef"><%=VBOE_QTY_SCM.elementAt(j) %></td>
										<td align="right"><%=VBOE_ACT_QTY.elementAt(j) %>
											<%-- <div class="col-sm-12 col-xs-12 col-md-12">  
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm" value="<%=VBOE_ACT_QTY.elementAt(j) %>" <%if(!VBOE_ACT_QTY.elementAt(j).equals("")){ %>style="background:#99ffcc; text-align: right;"<%} %> readonly>
						      						<span class="input-group-text"><b>MMBTU</b></span>
						      						<input type="text" class="form-control form-control-sm" value="<%=VBOE_ACT_QTY_MT.elementAt(j) %>" <%if(!VBOE_ACT_QTY_MT.elementAt(j).equals("")){ %>style="background:#99ffcc; text-align: right;"<%} %> readonly> 
						      						<span class="input-group-text"><b>MT</b></span>
						      						<input type="text" class="form-control form-control-sm" value="<%=VBOE_ACT_QTY_SCM.elementAt(j) %>" <%if(!VBOE_ACT_QTY_SCM.elementAt(j).equals("")){ %>style="background:#99ffcc; text-align: right;"<%} %> readonly>
						      						<span class="input-group-text"><b>SCM</b></span>
					      						</div>
											</div> --%>
										</td>
										<td align="right"><%=VBOE_ACT_QTY_MT.elementAt(j) %></td>
										<td align="right"><%=VBOE_ACT_QTY_SCM.elementAt(j) %></td>
										<td style="background-color: #e9ecef" align="center"><%=VLINKED_BL.elementAt(j)%></td>
										<td align="center">
											<%if(VBOE_CUSTOM_DUTY.elementAt(j).equals("Y")){%>Yes 
											<%}else if(VBOE_CUSTOM_DUTY.elementAt(j).equals("N")){ %>No<%}%>
										</td>
										<td><%=VBOE_LOAD_PORT.elementAt(j)%></td>
										<td align="right"><%=VBOE_PROVISIONAL_PRICE.elementAt(j)%></td>
										<td align="center"> <%if(VBOE_PROVISIONAL_PRICE_UNIT.elementAt(j).equals("2")){ %>USD/MMBTU<%}%></td>
										<td align="right"><%=VBOE_FINAL_PRICE.elementAt(j)%></td>
										<td align="center"> <%if(VBOE_FINAL_PRICE_UNIT.elementAt(j).equals("2")){ %>USD/MMBTU<%}%></td>
									</tr>
									<%} %>
									<tbody>
										<tr>
											<td colspan="8" align="right" style="font-weight: bold; color: #203d78;">Total</td>
											<td align="right"><%if(totalBOEMMBTU!=0){%><%=totalBOEMMBTU %><%} %></td>
											<td align="right"><%if(totalBOEMT!=0){%><%=totalBOEMT %><%} %></td>
											<td align="right"><%if(totalBOESCM!=0){%><%=totalBOESCM %><%} %></td>
											<td align="right"><%if(totalActBOEMMBTU!=0){%><%=totalActBOEMMBTU %><%} %></td>
											<td align="right"><%if(totalActBOEMT!=0){%><%=totalActBOEMT %><%} %></td>
											<td align="right"><%if(totalActBOESCM!=0){%><%=totalActBOESCM %><%} %></td>
											<td colspan="7"></td>
										</tr>
									</tbody>
								<%}else{ %>
									<tr id="row">
										<td colspan="21" align="center">
											<%=utilmsg.infoMessage("<b>BOE Details is not Available!</b>") %>
										</td>
									</tr>
								<% }%>
								</tbody>
							</table>
						</div>
					</div>&nbsp;
					<%-- <%} %> --%>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Lab Certificate of Quantity & Quantity Delivered</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Q&Q Cerificate No.</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="qq_cer_no" id="qq_cer_no" value="<%=qq_cer_no %>" maxLength="25">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Q&Q Certificate Date</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
			      				<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="qq_cer_dt" id="qq_cer_dt"  value="<%=qq_cer_dt %>" maxLength="10" 
		      						onblur="validateDate(this);" 
		      						onchange="validateDate(this);" autocomplete="off">
		      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
	      						</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Net Unloaded Quantity</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm" name="net_unloaded_qunt" id="net_unloaded_qunt"  value="<%=net_unloaded_qunt %>"  autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','net_unloaded_qunt');negNumber(this);" style="text-align: right" onchange="totalActQty(this.value);">
	      						<span class="input-group-text"><b>MMBTU</b></span>
	      						<input type="text" class="form-control form-control-sm" name="net_unloaded_qunt_mt" id="net_unloaded_qunt_mt"  value="<%=net_unloaded_qunt_mt %>"  autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','net_unloaded_qunt_mt');negNumber(this);" style="text-align: right">
	      						<span class="input-group-text"><b>MT</b></span>
	      						<input type="text" class="form-control form-control-sm" name="net_unloaded_qunt_m3" id="net_unloaded_qunt_m3"  value="<%=net_unloaded_qunt_m3 %>"  autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','net_unloaded_qunt_m3');negNumber(this);" style="text-align: right">
	      						<span class="input-group-text"><b>SCM</b></span>
      						</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Density Of Material</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="col-auto">
			    				<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm" name="dens_material" id="dens_material" value="<%=dens_material %>"  autocomplete="off" onblur="checkValuePrecision(this.value,'2','7','dens_material');negNumber(this);" style="text-align: right">
		      						<span class="input-group-text"><b>Kg/SCM</b></span>
	      						</div>
			    			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>GHV</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="col-auto">
			    				<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm" name="qq_ghv" id="qq_ghv" value="<%=qq_ghv %>"  autocomplete="off"  onblur="checkValuePrecision(this.value,'4','2','qq_ghv');calculateGCV(this);negNumber(this);" style="text-align: right">
		      						<span class="input-group-text"><b>MJ/Kg</b></span>
	      						</div>
			    			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>GCV</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="col-auto">
			    				<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm" name="qq_gcv" id="qq_gcv" value="<%=qq_gcv %>"  autocomplete="off" onblur="checkValuePrecision(this.value,'4','2','qq_gcv');negNumber(this);" style="text-align: right">
		      						<span class="input-group-text"><b>MMBTU/Tonnes</b></span>
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
				    				<font size="1" face="arial, helvetica, sans-serif">&nbsp;( You may enter up to 150 characters. )&nbsp;
										<input readonly type=text name="remLen1" size="3" maxlength="3" class=""> characters left
									</font><br>
				      				<textarea class="form-control" name="app_lab_notes" cols="75" rows="1" onKeyDown="textCounter(this.form.app_lab_notes,this.form.remLen1,149);" onKeyUp="textCounter(this.form.app_lab_notes,this.form.remLen1,149);"><%=app_lab_notes %></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Actual Unloaded Quantity</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Total Actual Unloaded Quantity of LNG</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm" name="expected_qunt" id="expected_qunt"  value="<%=expected_qunt %>"  autocomplete="off" onblur="checkValuePrecision(this.value,'2','9','expected_qunt');negNumber(this);" style="text-align: right" readonly> 
	      						<span class="input-group-text"><b>MMBTU</b></span>
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
</div>

<div class="modal fade" id="AddNewBl" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
        			Bill Of Lading Document Details
        		</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" >
        	</div>
        	<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> BL#<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" >
				      				<input type="text" class="form-control form-control-sm" name="disp_bl_no" id="disp_bl_no" value="" maxLength="25" readonly>
				      				<input type="hidden" class="form-control form-control-sm" name="bl_no" id="bl_no" value="" maxLength="25" readonly>
				    			</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Bill Of Lading Date<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="bill_of_lad_dt" id="bill_of_lad_dt"  value="" maxLength="10" 
		      						onblur="validateDate(this);" 
		      						onchange="validateDate(this);" autocomplete="off">
		      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
	      						</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Bill Of Lading Number<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" >
				      				<input type="text" class="form-control form-control-sm" name="bill_of_lad_no" id="bill_of_lad_no" value="" maxLength="25">
				    			</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Import Dept. No. </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm" name="imp_dept_no" id="imp_dept_no"  value=""  autocomplete="off">
	      						</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> Importer Code</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm" name="imp_code" id="imp_code"  value=""  autocomplete="off">
	      						</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b><%=owner_abbr %> Endorsement Date </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="endors_dt" id="endors_dt" value="" maxLength="10" 
		      						onblur="validateDate(this);" 
		      						onchange="validateDate(this);" autocomplete="off">
		      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
	      						</div>
							</div>
						</div>
						<!-- <div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Loaded Quantity<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" name="qty" id="qty">
	      							<input type="text" class="form-control form-control-sm" name="loaded_qunt" id="loaded_qunt"  value="" autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','loaded_qunt')">
	      							<span class="input-group-text"><b>MMBTU</b></span>
	      							<input type="text" class="form-control form-control-sm" name="loaded_qunt_mt" id="loaded_qunt_mt"  value="" autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','loaded_qunt_mt')">
	      							<span class="input-group-text"><b>MT</b></span>
	      							<input type="text" class="form-control form-control-sm" name="loaded_qunt_m3" id="loaded_qunt_m3"  value="" autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','loaded_qunt_m3')">
	      							<span class="input-group-text"><b>SCM</b></span>
								</div>
								<input type="hidden" class="form-control form-control-sm" name="loaded_qunt_unit" id="loaded_qunt_unit"  value="" >
				  			</div>
						</div> -->
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Remark</b></label>
							</div>
						</div>
						<div class="col-sm-12 col-xs-12 col-md-10"> 
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<textarea class="form-control" name="app_bill_notes" id="app_bill_notes" rows="1"></textarea>
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
        		<div class="topheader">
        			BOE Details
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
				      				<input type="text" class="form-control form-control-sm" name="disp_boe_no" id="disp_boe_no" value="" maxLength="25" readonly>
				      				<input type="hidden" class="form-control form-control-sm" name="boe_no" id="boe_no" value="" maxLength="25" readonly>
				    			</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Linked Bl/s<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="linked_bls" id="linked_bls" value="" readonly>
				    			</div>
							</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>BOE Number<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="boe_ref" id="boe_ref" value="" maxLength="25">
				    			</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>BOE Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="be_dt" id="be_dt" value="" maxLength="10" 
		      						onblur="validateDate(this);" 
		      						onchange="validateDate(this);" autocomplete="off">
		      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
	      						</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Business Unit<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<select class="form-select form-select-sm" name="business_unit" <%if(opration.equals("MODIFY")) {%>style="pointer-events: none;"<%} %>>
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
				    			<label class="form-label"><b> Trader Plant<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<select class="form-select form-select-sm" name="trader_plant" <%if(opration.equals("MODIFY")) {%>style="pointer-events: none;"<%} %>>
								   	 	 <%for(int i=0;i<VSEL_PLANT_SEQ_NO.size();i++){ %>
										<option value="<%=VSEL_PLANT_SEQ_NO.elementAt(i)%>"><%=VSEL_PLANT_ABBR.elementAt(i)%></option>
										<%} %>
								   	</select>
								</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Expected Quantity of LNG<span class="s-red">*</span> </b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" name="qty" id="qty">
	      							<input type="text" class="form-control form-control-sm" name="boe_expected_qunt" id="boe_expected_qunt"  value="" autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','boe_expected_qunt');negNumber(this);" readonly>
	      							<span class="input-group-text"><b>MMBTU</b></span>
	      							<input type="text" class="form-control form-control-sm" name="mt" id="mt"  value="" autocomplete="off" onblur="checkValuePrecision(this.value,'2','9','mt');negNumber(this);" readonly>
	      							<span class="input-group-text"><b>MT</b></span>
	      							<input type="text" class="form-control form-control-sm" name="m3" id="m3"  value="" autocomplete="off" onblur="checkValuePrecision(this.value,'2','10','m3');negNumber(this);" readonly>
	      							<span class="input-group-text"><b>SCM</b></span>
								</div>
								<input type="hidden" class="form-control form-control-sm" name="boe_expected_qunt_unit" id="boe_expected_qunt_unit"  value="">
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Actual Quantity Of LNG<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="input-group input-group-sm" name="qty" id="qty">
	      							<input type="text" class="form-control form-control-sm" name="actual_qunt" id="actual_qunt"  value="" autocomplete="off" onblur="checkValuePrecision(this.value,'2','8','actual_qunt');negNumber(this);">
	      							<span class="input-group-text"><b>MMBTU</b></span>
	      							<input type="text" class="form-control form-control-sm" name="actual_mt" id="actual_mt"  value="" autocomplete="off" onblur="checkValuePrecision(this.value,'2','9','actual_mt');negNumber(this);">
	      							<span class="input-group-text"><b>MT</b></span>
	      							<input type="text" class="form-control form-control-sm" name="actual_m3" id="actual_m3"  value="" autocomplete="off" onblur="checkValuePrecision(this.value,'2','10','actual_m3');negNumber(this);">
	      							<span class="input-group-text"><b>SCM</b></span>
								</div>
								<input type="hidden" class="form-control form-control-sm" name="actual_qunt_unit" id="actual_qunt_unit"  value="" >
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
				      				<select class="form-select form-select-sm" name="CD_Checkbox" id="CD_Checkbox" >
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
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> BOE Provisional Price<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="boe_provisional_price" id="boe_provisional_price" maxLength="7" onblur="checkValuePrecision(this.value,'4','2','boe_provisional_price');negNumber(this);">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="boe_provisional_price_unit">
				      					<option value="2">USD/MMBTU</option>
				      				</select>
				      				<%-- <script>document.forms[0].bl_price_unit.value="<%=bl_price_unit.trim()%>"</script> --%>
				    			</div>
				  			</div>
				  		</div>
				  		<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b> BOE Final Price<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="boe_final_price" id="boe_final_price" maxLength="7" onblur="checkValuePrecision(this.value,'4','2','boe_final_price');negNumber(this);">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="boe_final_price_unit">
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
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmitBoe();">
				</div>
      		</div>
        </div>
	</div>
</div>

<input type="hidden" name="option" value="CARGO_ARRIVAL">
<input type="hidden" name="allocation_status" value="<%=allocation_status%>">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="bl_opration" value="">
<input type="hidden" name="boe_opration" value="">
<input type="hidden" name="alloc_rev" value="<%=alloc_rev%>">

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