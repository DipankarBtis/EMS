<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh(opration)
{
	var counterparty_cd ="0";
	if(opration == "MODIFY")
	{
		counterparty_cd = document.forms[0].counterparty_cd.value;
	}
	var entity_role = document.forms[0].entity_role.value;
	var prev_entity_role = document.forms[0].prev_entity_role.value;
	
	if(entity_role != prev_entity_role)
	{
		counterparty_cd ="0";
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_entity_mst.jsp?opration="+opration+"&u="+u+"&counterparty_cd="+counterparty_cd+"&entity_role="+entity_role;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
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

function setValues(seq_no,name,abbr,addr,city,pin,state,zone,sector,eff_dt,oth_state_flg)
{
	document.forms[0].plant_seq_no.value=seq_no;
	document.forms[0].plant_name.value=name;
	document.forms[0].plant_abbr.value=abbr;
	document.forms[0].plant_addr.value=addr;
	document.forms[0].plant_city.value=city;
	document.forms[0].plant_pin.value=pin;
	document.forms[0].oth_state_flg.value=oth_state_flg;
	if(oth_state_flg=='Y')
	{
		document.forms[0].plant_state.value="other";
		document.forms[0].new_state_nm.value=state;
		hidegrp_id();
	}
	else
	{
		document.forms[0].plant_state.value=state;
		hidegrp_id();
	}
	
	document.forms[0].plant_zone.value=zone;
	document.forms[0].plant_sector.value=sector;
	document.forms[0].plant_eff_dt.value=eff_dt;
	document.forms[0].old_plant_eff_dt.value=eff_dt;
	document.forms[0].opration.value="MODIFY";
}

function setBuValues(seq_no,name,abbr,addr,city,pin,state,zone,eff_dt)
{
	document.forms[0].bu_seq_no.value=seq_no;
	document.forms[0].bu_name.value=name;
	document.forms[0].bu_abbr.value=abbr;
	document.forms[0].bu_addr.value=addr;
	document.forms[0].bu_city.value=city;
	document.forms[0].bu_pin.value=pin;
	document.forms[0].bu_state.value=state;
	document.forms[0].bu_zone.value=zone;
	document.forms[0].bu_eff_dt.value=eff_dt;
	document.forms[0].old_bu_eff_dt.value=eff_dt;
	document.forms[0].opration.value="MODIFY";
}

function doReset()
{
	document.forms[0].plant_seq_no.value="";
	document.forms[0].plant_name.value="";
	document.forms[0].plant_abbr.value="";
	document.forms[0].plant_addr.value="";
	document.forms[0].plant_city.value="";
	document.forms[0].plant_pin.value="";
	document.forms[0].plant_state.value="0";
	document.forms[0].new_state_nm.value="";
	document.forms[0].plant_zone.value="0";
	document.forms[0].plant_eff_dt.value="";
	document.forms[0].old_plant_eff_dt.value="";
	document.forms[0].oth_state_flg.value="";
	hidegrp_id();
	var stat_cd = document.forms[0].stat_cd;
	var stat_no = document.forms[0].stat_no;
	var stat_eff_dt = document.forms[0].stat_eff_dt;
	var stat_remark = document.forms[0].stat_remark;
	if(stat_no!=null && stat_no.length!=undefined)
	{
		for(var i=0; i<stat_no.length; i++)
		{
			if(stat_cd[i].value!="1001")
			{
				stat_no[i].value="";
				stat_eff_dt[i].value="";
			}
			else
			{
				document.getElementById("spanPan").style.display="inline"
			}
			
			
			stat_remark[i].value="";
		}
	}
	else
	{
		if(stat_cd!=null)
		{
			if(stat_cd.value!="1001")
			{
				stat_no.value="";
				stat_eff_dt.value="";
			}
			else
			{
				document.getElementById("spanPan").style.display="inline"
			}
			
			stat_remark.value="";
		}
	}	
}

function doBuReset()
{
	document.forms[0].bu_seq_no.value="";
	document.forms[0].bu_name.value="";
	document.forms[0].bu_abbr.value="";
	document.forms[0].bu_addr.value="";
	document.forms[0].bu_city.value="";
	document.forms[0].bu_pin.value="";
	document.forms[0].bu_state.value="0";
	document.forms[0].bu_zone.value="0";
	document.forms[0].bu_eff_dt.value="";
	document.forms[0].old_bu_eff_dt.value="";
	
	var bu_stat_cd = document.forms[0].bu_stat_cd;
	var bu_stat_no = document.forms[0].bu_stat_no;
	var bu_stat_eff_dt = document.forms[0].bu_stat_eff_dt;
	var bu_stat_remark = document.forms[0].bu_stat_remark;
	if(bu_stat_no!=null && bu_stat_no.length!=undefined)
	{
		for(var i=0; i<bu_stat_no.length; i++)
		{
			if(bu_stat_cd[i].value!="1001")
			{
				bu_stat_no[i].value="";
				bu_stat_eff_dt[i].value="";
			}
			else
			{
				document.getElementById("buSpanPan").style.display="inline"
			}
			bu_stat_remark[i].value="";
		}
	}
	else
	{
		if(bu_stat_cd!=null)
		{
			if(bu_stat_cd.value!="1001")
			{
				bu_stat_no.value="";
				bu_stat_eff_dt.value="";
			}
			else
			{
				document.getElementById("buSpanPan").style.display="inline"
			}
			
			bu_stat_remark.value="";
		}
	}	
}

function doSubmit()
{
	var eff_dt = document.forms[0].eff_dt.value;
	var name = document.forms[0].name.value;
	var abbr = document.forms[0].abbr.value;
	var pan_no = document.forms[0].pan_no.value;
	var sap_code = document.forms[0].sap_code.value;
	var category = document.forms[0].category.value;
	var ncf_category = document.forms[0].ncf_category.value;
	
	var reg_eff_dt = document.forms[0].reg_eff_dt;
	var address_type = document.forms[0].address_type;
	var address = document.forms[0].address;
	var city = document.forms[0].city;
	var state = document.forms[0].state;
	var pin = document.forms[0].pin;
	var country = document.forms[0].country; 
	
	var addChkbox = document.forms[0].add_chk;
	
	var opration = document.forms[0].opration.value;
	
	var msg="";
	var flag=true;
	
	if(opration == "MODIFY")
	{
		var counterparty_cd = document.forms[0].counterparty_cd.value;
		if(counterparty_cd=="" || counterparty_cd=="0" || trim(counterparty_cd)=="")
		{
			msg+="Select Counterparty!\n";
			flag=false
		}
	}
	
	if(eff_dt=="" || trim(eff_dt)=="")
	{
		msg+="Enter Eff Date of Counterparty!\n";
		flag=false
	}
	if(name=="" || trim(name)=="")
	{
		msg+="Enter Counterparty Name!\n";
		flag=false
	}
	if(abbr=="" || trim(abbr)=="")
	{
		msg+="Enter Counterparty Abbr!\n";
		flag=false
	}
	if(pan_no=="" || trim(pan_no)=="")
	{
		msg+="Enter Counterparty PAN No!\n";
		flag=false
	}
	if(sap_code=="" || trim(sap_code)=="")
	{
		msg+="Enter Counterparty SAP Code!\n";
		flag=false
	}
	if(category=="" || trim(category)=="" || category == "0")
	{
		msg+="Select Category!\n";
		flag=false
	}
	if(ncf_category=="" || trim(ncf_category)=="" || ncf_category == "0")
	{
		msg+="Select NCF Category!\n";
		flag=false
	}
	if(addChkbox!=null && addChkbox.length!=undefined)
	{
		for(var i=0; i<addChkbox.length; i++)
		{
			if(addChkbox[i].checked)
			{
				var adddd="";
				if(address_type[i].value=="R")
				{
					adddd="Register Address";
				}
				else if(address_type[i].value=="C")
				{
					adddd="Correspondence Address";
				}
				else if(address_type[i].value=="B")
				{
					adddd="Billing Address";
				}
				
				if(reg_eff_dt[i].value=="" || trim(reg_eff_dt[i].value)=="")
				{
					msg+="Enter Eff Date of "+adddd+"!\n";
					flag=false
				}
				if(address[i].value=="" || trim(address[i].value)=="")
				{
					msg+="Enter "+adddd+"!\n";
					flag=false
				}
				if(city[i].value=="" || trim(city[i].value)=="")
				{
					msg+="Enter City of "+adddd+"!\n";
					flag=false
				}
				if(state[i].value=="" || state[i].value=="0" || trim(state[i].value)=="")
				{
					msg+="Enter State of "+adddd+"!\n";
					flag=false
				}
				if(pin[i].value=="" || trim(pin[i].value)=="")
				{
					msg+="Enter Pin of "+adddd+"!\n";
					flag=false
				}
				if(country[i].value=="" || country[i].value=="0" || trim(country[i].value)=="")
				{
					msg+="Select Country of "+adddd+"!\n";
					flag=false
				} 
			}
		}
	}
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" Counterparty Detail?")
		if(a)
		{
			document.forms[0].opration.value="MODIFY";
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function doPlantSubmit()
{
	var plant_eff_dt = document.forms[0].plant_eff_dt.value;
	var plant_name = document.forms[0].plant_name.value;
	var plant_abbr = document.forms[0].plant_abbr.value;
	var plant_addr = document.forms[0].plant_addr.value;
	var plant_city = document.forms[0].plant_city.value;
	var plant_state = document.forms[0].plant_state.value;
	var plant_pin = document.forms[0].plant_pin.value;
	
	var opration = document.forms[0].opration.value;
	var entity_role = document.forms[0].entity_role.value;
	
	var msg="";
	var flag=true;
	
	if(opration == "MODIFY")
	{
		var counterparty_cd = document.forms[0].counterparty_cd.value;
		if(counterparty_cd=="" || counterparty_cd=="0" || trim(counterparty_cd)=="")
		{
			msg+="Select Counterparty!\n";
			flag=false
		}
	}
	
	if(entity_role=="" || trim(entity_role)=="" || entity_role=="0")
	{
		msg+="Select Entity Role!\n";
		flag=false
	}
	if(plant_eff_dt=="" || trim(plant_eff_dt)=="")
	{
		msg+="Enter Eff Date of Plant!\n";
		flag=false
	}
	if(plant_name=="" || trim(plant_name)=="")
	{
		msg+="Enter Plant Name!\n";
		flag=false
	}
	if(plant_abbr=="" || trim(plant_abbr)=="")
	{
		msg+="Enter Plant Abbr!\n";
		flag=false
	}
	if(plant_addr=="" || trim(plant_addr)=="")
	{
		msg+="Enter Plant Address!\n";
		flag=false
	}
	if(plant_city=="" || trim(plant_city)=="")
	{
		msg+="Enter City of Plant!\n";
		flag=false
	}
	if(plant_state=="" || plant_state=="0" || trim(plant_state)=="")
	{
		msg+="Enter State of Plant!\n";
		flag=false
	}
	if(plant_pin=="" || trim(plant_pin)=="")
	{
		msg+="Enter Pin of Plant!\n";
		flag=false
	}
	
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" Counterparty Plant Detail?")
		if(a)
		{
			document.forms[0].option.value="COUNTERPARTY_PLANT_MST"
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function doBuSubmit()
{
	var bu_eff_dt = document.forms[0].bu_eff_dt.value;
	var bu_name = document.forms[0].bu_name.value;
	var bu_abbr = document.forms[0].bu_abbr.value;
	var bu_addr = document.forms[0].bu_addr.value;
	var bu_city = document.forms[0].bu_city.value;
	var bu_state = document.forms[0].bu_state.value;
	var bu_pin = document.forms[0].bu_pin.value;
	
	var opration = document.forms[0].opration.value;
	var entity_role = document.forms[0].entity_role.value;
	
	var msg="";
	var flag=true;
	
	if(opration == "MODIFY")
	{
		var counterparty_cd = document.forms[0].counterparty_cd.value;
		if(counterparty_cd=="" || counterparty_cd=="0" || trim(counterparty_cd)=="")
		{
			msg+="Select Counterparty!\n";
			flag=false
		}
	}
	
	if(entity_role=="" || trim(entity_role)=="" || entity_role=="0")
	{
		msg+="Select Entity Role!\n";
		flag=false
	}
	if(bu_eff_dt=="" || trim(bu_eff_dt)=="")
	{
		msg+="Enter Eff Date of Bu!\n";
		flag=false
	}
	if(bu_name=="" || trim(bu_name)=="")
	{
		msg+="Enter Bu Name!\n";
		flag=false
	}
	if(bu_abbr=="" || trim(bu_abbr)=="")
	{
		msg+="Enter Bu Abbr!\n";
		flag=false
	}
	if(bu_addr=="" || trim(bu_addr)=="")
	{
		msg+="Enter Bu Address!\n";
		flag=false
	}
	if(bu_city=="" || trim(bu_city)=="")
	{
		msg+="Enter City of Bu!\n";
		flag=false
	}
	if(bu_state=="" || bu_state=="0" || trim(bu_state)=="")
	{
		msg+="Enter State of Bu!\n";
		flag=false
	}
	if(bu_pin=="" || trim(bu_pin)=="")
	{
		msg+="Enter Pin of Bu!\n";
		flag=false
	}
	
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" Counterparty BU Detail?")
		if(a)
		{
			document.forms[0].option.value="COUNTERPARTY_BU_MST"
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function changeStatus(obj)
{
	var kyc_flag = document.forms[0].kyc_flg.value;
	if(obj.checked)
	{
		document.forms[0].kyc_flg.value="Y";
	}
	else
	{
		document.forms[0].kyc_flg.value="N";
	}
}

function setVal()
{
	document.forms[0].opration.value="INSERT";
}

function doBankSubmit()
{
	var bank_name = document.forms[0].bank_name.value;
	var bank_eff_dt = document.forms[0].bank_eff_dt.value;
	var account_no = document.forms[0].account_no.value;
	var bank_branch = document.forms[0].bank_branch.value;
	var ifsc_code = document.forms[0].ifsc_code.value;
	var bank_state = document.forms[0].bank_state.value;
	var bank_category = document.forms[0].bank_category.value;
	
	var opration = document.forms[0].opration.value;
	var entity_role = document.forms[0].entity_role.value;
	
	var msg="";
	var flag=true;
	
	if(opration == "MODIFY")
	{
		var counterparty_cd = document.forms[0].counterparty_cd.value;
		if(counterparty_cd=="" || counterparty_cd=="0" || trim(counterparty_cd)=="")
		{
			msg+="Select Counterparty!\n";
			flag=false
		}
	}
	
	if(entity_role=="" || trim(entity_role)=="" || entity_role=="0")
	{
		msg+="Select Entity Role!\n";
		flag=false
	}
	if(bank_category=="" || trim(bank_category)=="")
	{
		msg+="Select Category!\n";
		flag=false
	}
	if(bank_eff_dt=="" || trim(bank_eff_dt)=="")
	{
		msg+="Enter Eff Date of Bank!\n";
		flag=false
	}
	if(bank_name=="" || trim(bank_name)=="")
	{
		msg+="Enter Bank Name!\n";
		flag=false
	}
	if(account_no=="" || trim(account_no)=="")
	{
		msg+="Enter Account No!\n";
		flag=false
	}
	if(bank_branch=="" || trim(bank_branch)=="")
	{
		msg+="Enter Branch!\n";
		flag=false
	}
	if(ifsc_code=="" || trim(ifsc_code)=="")
	{
		if(bank_category == "DERV")
		{
			msg+="Enter Swift Code!\n";
			flag=false
		}
		else
		{
			msg+="Enter IFSC Code!\n";
			flag=false
		}
	}
	if(bank_state=="" || bank_state=="0" || trim(bank_state)=="")
	{
		msg+="Select State of Bank!\n";
		flag=false
	}
	
	if(flag)
	{
		alert("Same Eff. Date will overwrite the Bank details,\nDifferent Eff. Date will create new entry!");
		var a = confirm("Do you want to "+opration+" Counterparty Bank Detail?")
		if(a)
		{
			document.forms[0].option.value="COUNTERPARTY_BANK_MST"
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
	
}

function setBankDtl(bank_eff_dt,bank_name,bank_account_no,bank_branch,bank_state,ifsc_code,bank_category)
{
	if(bank_category == "DERV")
	{
		var lbl_ifsc = document.getElementById('lbl_ifsc');
		lbl_ifsc.innerHTML = "<b>Swift Code<span class=\"s-red\">*</span></b>";
	}
	
	document.forms[0].bank_name.value=bank_name;
	
	document.forms[0].bank_eff_dt.value=bank_eff_dt;
	document.forms[0].old_bank_eff_dt.value=bank_eff_dt;
	
	document.forms[0].account_no.value=bank_account_no;
	document.forms[0].bank_branch.value=bank_branch;
	document.forms[0].ifsc_code.value=ifsc_code;
	document.forms[0].bank_category.value=bank_category;
	if(trim(bank_state)=="")
	{
		document.forms[0].bank_state.value="0";
	}
	else
	{
		document.forms[0].bank_state.value=bank_state;	
	}
	
	
}

function hidegrp_id()
{	
	if (document.forms[0].plant_state.value == 'other')
	{
		document.getElementById("otherStateDiv").style.display = "block";
		document.getElementById("stateDiv").className = "col-sm-2 col-xs-2 col-md-2";
		document.forms[0].new_state_nm.style.visibility='visible'; 
		document.forms[0].new_state_nm.focus();
		document.forms[0].stateNm.value = '';
	}
	else
	{
		document.getElementById("stateDiv").className = "col-sm-4 col-xs-4 col-md-4";
		document.getElementById("otherStateDiv").style.display = "none";
		document.forms[0].new_state_nm.style.visibility='hidden'; 
		document.forms[0].stateNm.value=document.forms[0].plant_state[document.forms[0].plant_state.selectedIndex].innerText;
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String opration=request.getParameter("opration")==null?"MODIFY":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");

if(entity_role.equals("B"))
{
	if(!owner_cd.equals(""))
	{
		counterparty_cd=owner_cd;
	}
}

dbcounterpty.setCallFlag("ENTITY_MASTER");
dbcounterpty.setOpration(opration);
dbcounterpty.setCounterparty_cd(counterparty_cd);
dbcounterpty.setEntity_role(entity_role);
dbcounterpty.setComp_cd(owner_cd);
dbcounterpty.init();

Vector VCOUNTRY_CODE = dbcounterpty.getVCOUNTRY_CODE();
Vector VCOUNTRY_NM = dbcounterpty.getVCOUNTRY_NM();
Vector VISO_CODE = dbcounterpty.getVISO_CODE();
Vector VTIN =dbcounterpty.getVTIN();
Vector VSTATE_CODE = dbcounterpty.getVSTATE_CODE();
Vector VSTATE_NM = dbcounterpty.getVSTATE_NM();

Vector VCOUNTERPARTY_CD = dbcounterpty.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcounterpty.getVCOUNTERPARTY_ABBR();

Vector VNCF_CATEGORY = dbcounterpty.getVNCF_CATEGORY();

String name=dbcounterpty.getName();
String abbr=dbcounterpty.getAbbr();
String eff_dt=dbcounterpty.getEff_dt();
String pan_no=dbcounterpty.getPan_no();
String pan_dt=dbcounterpty.getPan_dt();
String notes=dbcounterpty.getNotes();
String kyc_flg=dbcounterpty.getKyc_flg();
String igx_flg=dbcounterpty.getIgx_flg();
String business_unit=dbcounterpty.getBusiness_unit();
String status=dbcounterpty.getStatus();
String category=dbcounterpty.getCategory();
String ncf_category=dbcounterpty.getNcf_category();
String web_addr=dbcounterpty.getWeb_addr();
String sap_code=dbcounterpty.getSap_code();
String requester_approver_note=dbcounterpty.getRequester_approver_note();

/* String reg_eff_dt=dbcounterpty.getReg_eff_dt();
String address=dbcounterpty.getAddress();
String city=dbcounterpty.getCity();
String pin=dbcounterpty.getPin();
String state=dbcounterpty.getState();
String zone=dbcounterpty.getZone();
String country=dbcounterpty.getCountry();
String phone=dbcounterpty.getPhone();
String mobile=dbcounterpty.getMobile();
String alt_phone=dbcounterpty.getAlt_phone();
String fax1=dbcounterpty.getFax1();
String fax2=dbcounterpty.getFax2();
String email=dbcounterpty.getEmail(); */

String[] REG_EFF_DT=dbcounterpty.getREG_EFF_DT();
String[] ADDRESS_FLAG=dbcounterpty.getADDRESS_FLAG();
String[] ADDRESS=dbcounterpty.getADDRESS();
String[] CITY=dbcounterpty.getCITY();
String[] PIN=dbcounterpty.getPIN();
String[] STATE=dbcounterpty.getSTATE();
String[] ZONE=dbcounterpty.getZONE();
String[] COUNTRY=dbcounterpty.getCOUNTRY();
String[] PHONE=dbcounterpty.getPHONE();
String[] MOBILE=dbcounterpty.getMOBILE();
String[] ALT_PHONE=dbcounterpty.getALT_PHONE();
String[] FAX1=dbcounterpty.getFAX1();
String[] FAX2=dbcounterpty.getFAX2();
String[] EMAIL=dbcounterpty.getEMAIL();

Vector VPLANT_SEQ_NO = dbcounterpty.getVPLANT_SEQ_NO();
Vector VPLANT_EFF_DT = dbcounterpty.getVPLANT_EFF_DT();
Vector VPLANT_NAME = dbcounterpty.getVPLANT_NAME();
Vector VPLANT_ABBR = dbcounterpty.getVPLANT_ABBR();
Vector VPLANT_ADDR = dbcounterpty.getVPLANT_ADDR();
Vector VPLANT_STATE = dbcounterpty.getVPLANT_STATE();
Vector VPLANT_ZONE = dbcounterpty.getVPLANT_ZONE();
Vector VPLANT_ZONE_NM = dbcounterpty.getVPLANT_ZONE_NM();
Vector VPLANT_CITY = dbcounterpty.getVPLANT_CITY();
Vector VPLANT_PIN = dbcounterpty.getVPLANT_PIN();
Vector VPLANT_SECTOR = dbcounterpty.getVPLANT_SECTOR();
Vector VPLANT_SECTOR_NM = dbcounterpty.getVPLANT_SECTOR_NM(); 
Vector VPLANT_STATUS = dbcounterpty.getVPLANT_STATUS();
Vector VTAX_ID = dbcounterpty.getVTAX_ID();
Vector VOTH_STATE_FLG = dbcounterpty.getVOTH_STATE_FLG();

Vector VBU_PLANT_SEQ_NO = dbcounterpty.getVBU_PLANT_SEQ_NO();
Vector VBU_PLANT_EFF_DT = dbcounterpty.getVBU_PLANT_EFF_DT();
Vector VBU_PLANT_NAME = dbcounterpty.getVBU_PLANT_NAME();
Vector VBU_PLANT_ABBR = dbcounterpty.getVBU_PLANT_ABBR();
Vector VBU_PLANT_ADDR = dbcounterpty.getVBU_PLANT_ADDR();
Vector VBU_PLANT_STATE = dbcounterpty.getVBU_PLANT_STATE();
Vector VBU_PLANT_ZONE = dbcounterpty.getVBU_PLANT_ZONE();
Vector VBU_PLANT_ZONE_NM = dbcounterpty.getVBU_PLANT_ZONE_NM();
Vector VBU_PLANT_CITY = dbcounterpty.getVBU_PLANT_CITY();
Vector VBU_PLANT_PIN = dbcounterpty.getVBU_PLANT_PIN();
Vector VBU_PLANT_STATUS = dbcounterpty.getVBU_PLANT_STATUS();
Vector VBU_TAX_ID = dbcounterpty.getVBU_TAX_ID();

Vector VSTAT_CD = dbcounterpty.getVSTAT_CD();
Vector VSTAT_NM = dbcounterpty.getVSTAT_NM();
Vector VSTAT_TYPE = dbcounterpty.getVSTAT_TYPE();
Vector VSTAT_STATUS = dbcounterpty.getVSTAT_STATUS();
Vector VSTAT_REMARK = dbcounterpty.getVSTAT_REMARK();

String bank_eff_dt=dbcounterpty.getBank_eff_dt();
String bank_name=dbcounterpty.getBank_name();
String bank_account_no=dbcounterpty.getBank_account_no();
String bank_branch=dbcounterpty.getBank_branch();
String ifsc_code=dbcounterpty.getIfsc_code();
String bank_state=dbcounterpty.getBank_state();
String bank_formula=dbcounterpty.getBank_formula();
String bank_category=dbcounterpty.getBank_category();

Vector VBANK_FORMULA = dbcounterpty.getVBANK_FORMULA();
Vector VBANK_EFF_DT = dbcounterpty.getVBANK_EFF_DT();
Vector VBANK_CATEGORY = dbcounterpty.getVBANK_CATEGORY();

Vector VSECTOR_CD = dbcounterpty.getVSECTOR_CD();
Vector VSECTOR_NAME = dbcounterpty.getVSECTOR_NAME();

/* String old_value="CP="+counterparty_cd+"#NAME="+name+"#ABBR="+abbr+"#PANNO="+pan_no+"#PANDT="+pan_dt+"#KYC="+kyc_flg+"#IGX="+igx_flg+"#EFFDT="+eff_dt+
"#ADD="+address+"#CITY="+city+"#STATE="+state+"#ZONE="+zone+"#PIN="+pin+"#COUNTRY="+country+"#PH="+phone+"#FAX1="+fax1+
"#FAX2="+fax2+"#CELL="+mobile+"#EMAIL="+email+"#ALTPH="+alt_phone+"#REFFDT="+reg_eff_dt+"#STATUS="+status;
 */
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_counterparty">

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
							Entity Master
						</div>
					 	<div class="btn-group">
							<select class="btn btn-outline-secondary btngrp <%if(!entity_role.equals("0")){%>btnactive<%}%>" name="entity_role" onchange="refresh('MODIFY')">
								<option value="0">Select Entity Roles</option>
								<option value="C">Customer</option>
				    			<option value="T">Trader</option>
				    			<option value="R">Transporter</option>
				    			<option value="V">Vessel Agent</option>
				    			<option value="H">Custom House Agent</option>
				    			<option value="S">Surveyor</option>
				    			<option value="B">Business Owner</option>
				    			<option value="G">Gas Exchange</option>
							</select>
						</div>
						<script>
							document.forms[0].entity_role.value="<%=entity_role%>"
						</script>						  	
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Counterparty<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm searchable-select" name="counterparty_cd" onchange="refresh('<%=opration%>');" id="select_box">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<%if(entity_role.equals("B")){ %>
									<script>document.forms[0].counterparty_cd.style.pointerEvents = "none";</script>
									<%}%>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Counterparty Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="name" value="<%=name%>" maxLength="100">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Eff Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date" name="eff_dt" value="<%=eff_dt%>" maxLength="10" onblur="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');" onchange="validateDate(this);checkEffectiveDate(this,'<%=eff_dt%>');" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Abbreviation<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="abbr" value="<%=abbr%>" maxLength="20">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Clearance</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="checkbox" name="kyc_chk" <%if(kyc_flg.equals("Y")){%>checked<%} %> onclick="changeStatus(this)">&nbsp;<label class="form-label">KYC</label>&nbsp;&nbsp;&nbsp;
				      				<input type="checkbox" name="igx_chk" <%if(igx_flg.equals("Y")){%>checked<%} %>>&nbsp;<label class="form-label">IGX</label>
				      				<input type="hidden" name="kyc_flg" value="<%=kyc_flg%>">
    								<input type="hidden" name="igx_flg" value="<%=igx_flg%>">
    								<script>
    									document.forms[0].igx_chk.style.pointerEvents = "none";
							    	</script>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>PAN No<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="pan_no" value="<%=pan_no%>" maxLength="20">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>PAN Eff Date</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date" name="pan_dt" value="<%=pan_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>SAP Code<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="sap_code" value="<%=sap_code%>" maxLength="20">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>NCF Category<span class="s-red">*</span></b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="ncf_category">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VNCF_CATEGORY.size();i++){ %>
										<option value="<%=VNCF_CATEGORY.elementAt(i) %>"><%=VNCF_CATEGORY.elementAt(i) %></option>
										<%} %>
									</select>
									<script>
										document.forms[0].ncf_category.value="<%=ncf_category%>"
									</script>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Web-Site Address</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input class="form-control form-control-sm" name="web_addr" value="<%=web_addr%>" maxlength="100">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Category<span class="s-red">*</span></b></label>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="category">
										<option value="0">--Select--</option>
										<option value="Group">Group (Shell 100% owned entity)</option>
										<option value="Affiliate">Affiliate (Shell is not 100% owned entity)</option>
										<option value="3rd Party">3rd Party (external party)</option>
									</select>
									<script>
										document.forms[0].category.value="<%=category%>"
									</script>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>KYC Remark</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<textarea class="form-control form-control-sm" name="notes" cols="75" rows="3"><%=notes%></textarea>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Requester/Approver Notes</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<textarea class="form-control form-control-sm" cols="75" rows="3" readOnly><%=requester_approver_note%></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
					<%if(entity_role.equals("B")){ %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<input type="button" class="btn btn-sm config_btn" value="Bank Detail Config" 
				    				data-bs-toggle="modal" data-bs-target="#BankModal" 
				    				onclick="setBankDtl('<%=bank_eff_dt%>','<%=bank_name%>','<%=bank_account_no%>','<%=bank_branch%>','<%=bank_state%>','<%=ifsc_code%>','<%=bank_category%>');">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<textarea class="form-control form-control-sm" cols="75" rows="4" readOnly><%=bank_formula%></textarea>
				    			</div>
				    		</div>
				    	</div>
					</div>
					<%}else if(entity_role.equals("C")){ %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
				    				<label class="form-label"><b>Bank Details</b></label>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<textarea class="form-control form-control-sm" cols="75" rows="4" readOnly><%=bank_formula%></textarea>
				    			</div>
				    		</div>
				    	</div>
					</div>
					<%} %>
					<%for(int k=0;k<3;k++){ %>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<div class="row m-b-5">
						<%if(k==0){ %>
							<label class="form-label subheader" style="pointer-events: none;"><input class="form-check-input" type="checkbox" name="add_chk" checked style="pointer-events: none;"> Registered Address & Communication Details</label>
						<%}else if(k==1){ %>
							<label class="form-label subheader"><input class="form-check-input" type="checkbox" name="add_chk" onclick="enabledAddress('<%=k%>')"> Correspondence Address & Communication Details</label>
						<%}else if(k==2){ %>
							<label class="form-label subheader"><input class="form-check-input" type="checkbox" name="add_chk" onclick="enabledAddress('<%=k%>')"> Billing Address & Communication Details</label>
						<%} %>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Address<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="address" value="<%=ADDRESS[k]%>" maxLength="150">
					      				<input type="hidden" name="address_type" value="<%=ADDRESS_FLAG[k]%>">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2"> 
								<div class="form-group row">
					    			<label class="form-label"><b>Eff Date<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date" name="reg_eff_dt" value="<%=REG_EFF_DT[k]%>" maxLength="10" 
				      						onblur="validateDate(this);checkEffectiveDate(this,'<%=REG_EFF_DT[k]%>');" 
				      						onchange="validateDate(this);checkEffectiveDate(this,'<%=REG_EFF_DT[k]%>');" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>City<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="city" value="<%=CITY[k]%>" maxLength="40">
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>State/Province<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<%-- <select class="form-select form-select-sm" name="state" id="state<%=k%>">
											<option value="0">--Select--</option>
											<%for(int i=0;i<VSTATE_NM.size();i++){ %>
											<option value="<%=VSTATE_NM.elementAt(i)%>"><%=VSTATE_NM.elementAt(i)%></option>
											<%} %>
										</select>
										<script>
										document.forms[0].state[<%=k%>].value="<%=STATE[k]%>"
										document.getElementById("state<%=k%>").value="<%=STATE[k]%>"
										</script> --%>
										
										<input type="text" class="form-control form-control-sm" name="state" id="state<%=k%>" value="<%=STATE[k]%>" maxLength="40">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Zone</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<select class="form-select form-select-sm" name="zone" id="zone<%=k%>">
											<option value="0">--Select--</option>
											<option value="N">North</option>
											<option value="S">South</option>
											<option value="E">East</option>
											<option value="W">West</option>
											<option value="C">Central</option>
										</select>
										<script>
										<%-- document.forms[0].zone[<%=k%>].value="<%=ZONE[k]%>" --%>
										document.getElementById("zone<%=k%>").value="<%=ZONE[k]%>"
										</script>
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>PIN/ZIP Code<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="pin" value="<%=PIN[k]%>" maxLength="6" onkeyup="checkForNumber(this);">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Country<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<select class="form-select form-select-sm" name="country" id="country<%=k%>">
											<option value="0">--Select--</option>
											<%for(int i=0;i<VCOUNTRY_NM.size();i++){ %>
											<option value="<%=VCOUNTRY_NM.elementAt(i)%>"><%=VCOUNTRY_NM.elementAt(i)%></option>
											<%} %>
										</select>
										<script>
										<%-- document.forms[0].country[<%=k%>].value="<%=COUNTRY[k]%>" --%>
										document.getElementById("country<%=k%>").value="<%=COUNTRY[k]%>"
										</script>
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Phone</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="phone" value="<%=PHONE[k]%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Alternate Phone</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="alt_phone" value="<%=ALT_PHONE[k]%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Cell</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="cell" value="<%=MOBILE[k]%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Fax-1</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="fax1" value="<%=FAX1[k]%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Fax-2</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="fax2" value="<%=FAX2[k]%>" maxLength="20">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>E-mail</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="email" value="<%=EMAIL[k]%>" maxLength="40" onBlur="validateEmail(this);">
					    			</div>
					  			</div>
							</div>
						</div>
					<%} %>
					<%if(!entity_role.equals("G") && !entity_role.equals("V") && !entity_role.equals("H") && !entity_role.equals("S")){ %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
					<%if(entity_role.equals("R")){ %>
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Entry/Exit Point Address & Communication Details</label>
					<%}else{ %>
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Plant(s)/Office(s) Address & Communication Details</label>
					<%} %>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<lable class="btn btn-outline-secondary subbtngrp" 
									data-bs-toggle="modal" 
									data-bs-target="#myModal" 
									onclick="doReset();setVal();">Add New Plant</lable>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th></th>
										<th>Plant Name</th>
										<th>Plant ABBR</th>
										<th>Address</th>
										<th>City</th>
										<th>Pin</th>
										<th>State</th>
										<%if(!entity_role.equals("R")){ %>
										<th>Zone</th>
										<th>Sector</th>
										<%} %>
										<th>Eff Date</th>
										<%if(!entity_role.equals("R")){ %>
										<th>Tax/ID</th>
										<%} %>
									</tr>
								</thead>
								<tbody>
									<%if(VPLANT_SEQ_NO.size()>0){ %>
										<%for(int i=0; i<VPLANT_SEQ_NO.size(); i++){ %>
										<tr>
											<td>
												<div align="center">
													<font title="Click to Edit" style="color:var(--header_color)">
														<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#myModal" id="myBtn"
															onclick="fetchTaxId('<%=VPLANT_SEQ_NO.elementAt(i)%>');setValues('<%=VPLANT_SEQ_NO.elementAt(i)%>','<%=VPLANT_NAME.elementAt(i)%>','<%=VPLANT_ABBR.elementAt(i)%>','<%=VPLANT_ADDR.elementAt(i)%>','<%=VPLANT_CITY.elementAt(i)%>', 
															'<%=VPLANT_PIN.elementAt(i) %>','<%=VPLANT_STATE.elementAt(i) %>','<%=VPLANT_ZONE.elementAt(i) %>','<%=VPLANT_SECTOR.elementAt(i) %>','<%=VPLANT_EFF_DT.elementAt(i) %>','<%=VOTH_STATE_FLG.elementAt(i)%>')">
														</i>
													</font>
												</div>	
											</td>
											<td><div><%=VPLANT_NAME.elementAt(i) %></div></td>
											<td><div><%=VPLANT_ABBR.elementAt(i) %></div></td>
											<td><div><%=VPLANT_ADDR.elementAt(i) %></div></td>
											<td><div><%=VPLANT_CITY.elementAt(i) %></div></td>
											<td><div><%=VPLANT_PIN.elementAt(i) %></div></td>
											<td><div><%=VPLANT_STATE.elementAt(i) %></div></td>
											<%if(!entity_role.equals("R")){ %>
											<td><div><%=VPLANT_ZONE_NM.elementAt(i) %></div></td>
											<td><div><%=VPLANT_SECTOR_NM.elementAt(i) %></div></td>
											<%} %>
											<td><div><%=VPLANT_EFF_DT.elementAt(i) %></div></td>
											<%if(!entity_role.equals("R")){ %>
											<td><div><%=VTAX_ID.elementAt(i)%></div></td>
											<%} %>
										</tr>
										<%} %>
									<%}else{ %>
									<tr>
										<td colspan="<%if(!entity_role.equals("R")){ %>11<%}else{%>8<%} %>" align="center">
										<%=utilmsg.infoMessage("<b>No Plant Configured!</b>")%>
										</td>
									</tr>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<%} %>
					<%if(entity_role.equals("R") || entity_role.equals("G") || entity_role.equals("V") || entity_role.equals("H") || entity_role.equals("S")){ %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Business Unit Address & Communication Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<lable class="btn btn-outline-secondary subbtngrp" 
									data-bs-toggle="modal" 
									data-bs-target="#BuModal" 
									onclick="doBuReset();setVal();">Add New BU</lable>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th></th>
										<th>BU Name</th>
										<th>BU ABBR</th>
										<th>Address</th>
										<th>City</th>
										<th>Pin</th>
										<th>State</th>
										<th>Zone</th>
										<th>Eff Date</th>
										<th>Tax/ID</th>
									</tr>
								</thead>
								<tbody>
									<%if(VBU_PLANT_SEQ_NO.size()>0){ %>
										<%for(int i=0; i<VBU_PLANT_SEQ_NO.size(); i++){ %>
										<tr>
											<td>
												<div align="center">
													<font title="Click to Edit" style="color:var(--header_color)">
														<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#BuModal" id="myBuBtn"
															onclick="fetchBuTaxId('<%=VBU_PLANT_SEQ_NO.elementAt(i)%>');
															setBuValues('<%=VBU_PLANT_SEQ_NO.elementAt(i)%>','<%=VBU_PLANT_NAME.elementAt(i)%>',
															'<%=VBU_PLANT_ABBR.elementAt(i)%>','<%=VBU_PLANT_ADDR.elementAt(i)%>',
															'<%=VBU_PLANT_CITY.elementAt(i)%>', '<%=VBU_PLANT_PIN.elementAt(i) %>',
															'<%=VBU_PLANT_STATE.elementAt(i) %>','<%=VBU_PLANT_ZONE.elementAt(i) %>',
															'<%=VBU_PLANT_EFF_DT.elementAt(i) %>')">
														</i>
													</font>
												</div>	
											</td>
											<td><div><%=VBU_PLANT_NAME.elementAt(i) %></div></td>
											<td><div><%=VBU_PLANT_ABBR.elementAt(i) %></div></td>
											<td><div><%=VBU_PLANT_ADDR.elementAt(i) %></div></td>
											<td><div><%=VBU_PLANT_CITY.elementAt(i) %></div></td>
											<td><div><%=VBU_PLANT_PIN.elementAt(i) %></div></td>
											<td><div><%=VBU_PLANT_STATE.elementAt(i) %></div></td>
											<td><div><%=VBU_PLANT_ZONE_NM.elementAt(i) %></div></td>
											<td><div><%=VBU_PLANT_EFF_DT.elementAt(i) %></div></td>
											<td><div><%=VBU_TAX_ID.elementAt(i)%></div></td>
										</tr>
										<%} %>
									<%}else{ %>
									<tr>
										<td colspan="10" align="center">
										<%=utilmsg.infoMessage("<b>No BU Configured!</b>")%>
										</td>
									</tr>
									<%} %>
								</tbody>
							</table>
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
			</div>
		</div>
	</div>
</div>

<!-- MODEL FOR PLANT -->
<div class="modal fade" id="myModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Plant Details
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Plant Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="plant_name" value="" maxLength="50">
				      				<input type="hidden" class="form-control form-control-sm" name="plant_seq_no" value="">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Plant ABBR<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="plant_abbr" value="" maxLength="20">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Address<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="plant_addr" value="" maxLength="200">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>City<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="plant_city" value="" maxLength="40">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>PIN<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="plant_pin" value="" maxLength="8" onkeyup="checkForNumber(this);">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>State/Province<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div id="stateDiv" class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="plant_state" onchange="hidegrp_id();" onblur="hidegrp_id();">
										<option value="0">--Select--</option>
										<option value="other">other</option>
										<%for(int i=0;i<VSTATE_NM.size();i++){ %>
										<option value="<%=VSTATE_NM.elementAt(i)%>"><%=VSTATE_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<input type="hidden" name="stateNm" value="">
				    			</div>
				  			</div>
						</div>
						<div id="otherStateDiv" class="col-sm-2 col-xs-2 col-md-2" style="display:none;">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="new_state_nm" value="" maxLength="25" style="visibility: hidden;">
				    				<input type="hidden" class="form-control form-control-sm" name="oth_state_flg" value="">
				    			</div>
				    		</div> 
						</div>
					</div>
					<div class="row m-b-5" <%if(entity_role.equals("R")){ %>style="display:none;"<%} %>>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Zone</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="plant_zone">
										<option value="0">--Select--</option>
										<option value="N">North</option>
										<option value="S">South</option>
										<option value="E">East</option>
										<option value="W">West</option>
										<option value="C">Central</option>
									</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Sector</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="plant_sector">
										<option value="">--Select--</option>
										<%for(int i=0;i<VSECTOR_CD.size();i++){ %>
										<option value="<%=VSECTOR_CD.elementAt(i)%>"><%=VSECTOR_NAME.elementAt(i)%></option>
										<%} %>
									</select>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Eff Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
				      					<input type="hidden" name="old_plant_eff_dt" value="">
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="plant_eff_dt" value="" 
			      						maxLength="10" onblur="validateDate(this);checkEffectiveDate(this,document.forms[0].old_plant_eff_dt.value);" 
			      						onchange="validateDate(this);checkEffectiveDate(this,document.forms[0].old_plant_eff_dt.value);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div <%if(entity_role.equals("R")) {%>style="display:none;"<%} %>>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<div class="row m-b-5 subheader">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="form-group row" align="left">
					    			<label class="form-label"><b>Tax/ID Detail</b></label>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th>Tax/ID Type</th>
											<th>Tax/ID Name</th>
											<th>Tax/ID No</th>
											<th>Eff Date</th>
											<th>Remark</th>
										</tr>
									</thead>
									<tbody>
										<%if(VSTAT_CD.size() > 0){ %>
											<%for(int i=0; i< VSTAT_CD.size(); i++){ %>
											<tr>
												<td>
													<div>
														<%=VSTAT_TYPE.elementAt(i)%>
														<input type="hidden" class="form-control form-control-sm" name="stat_cd" value="<%=VSTAT_CD.elementAt(i)%>">
													</div>
												</td>
												<td><div><%=VSTAT_NM.elementAt(i)%></div></td>
												<td>
													<div align="center">
														<%if(VSTAT_CD.elementAt(i).equals("1001")){ %>
														<input type="text" class="form-control form-control-sm" name="stat_no" value="<%=pan_no%>" style="width:150px;" readonly="readonly">
														<span class="s-red" id="spanPan">*Submit Once to Save</span>
														<%}else{ %>
														<input type="text" class="form-control form-control-sm" name="stat_no" value="" style="width:150px;">
														<%} %>
													</div>
												</td>
												<td>
													<div align="center">
														<div class="input-group input-group-sm" >
															<%if(VSTAT_CD.elementAt(i).equals("1001")){ %>
								      						<input type="text" class="form-control form-control-sm date" name="stat_eff_dt" value="<%=pan_dt%>" 
								      						maxLength="10" onblur="validateDate(this);" 
								      						onchange="validateDate(this);" autocomplete="off" style="width:100px;" readOnly>
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
								      						<%}else{ %>
								      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="stat_eff_dt" value="" 
								      						maxLength="10" onblur="validateDate(this);" 
								      						onchange="validateDate(this);" autocomplete="off" style="width:100px;">
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
								      						<%} %>
							      						</div>
													</div>
												</td>
												<td>
													<div align="center">
														<textarea class="form-control form-control-sm" name="stat_remark" cols="35" rows="2" maxlength="100" style="width:150px;"></textarea>
													</div>
												</td>
											</tr>
											<%}%>
										<%}else{ %>
										<tr>
											<td colspan="11" align="center">
											<%=utilmsg.infoMessage("<b>No Tax/ID Detail Configured!</b>")%>
											</td>
										</tr>
										<%} %>
								</table>
							</div>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
					<%if(write_access.equals("Y")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doPlantSubmit();">
					<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
					<%} %>
				</div>
      		</div>
    	</div>
  	</div>
</div>

<!-- MODEL FOR BU -->
<div class="modal fade" id="BuModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Business Unit Details
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>BU Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="bu_name" value="" maxLength="50">
				      				<input type="hidden" class="form-control form-control-sm" name="bu_seq_no" value="">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>BU ABBR<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="bu_abbr" value="" maxLength="20">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Address<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="bu_addr" value="" maxLength="200">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>City<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="bu_city" value="" maxLength="40">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>PIN<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="bu_pin" value="" maxLength="8" onkeyup="checkForNumber(this);">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>State/Province<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="bu_state">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VSTATE_NM.size();i++){ %>
										<option value="<%=VSTATE_NM.elementAt(i)%>"><%=VSTATE_NM.elementAt(i)%></option>
										<%} %>
									</select>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Zone</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="bu_zone">
										<option value="0">--Select--</option>
										<option value="N">North</option>
										<option value="S">South</option>
										<option value="E">East</option>
										<option value="W">West</option>
										<option value="C">Central</option>
									</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Eff Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
				      					<input type="hidden" name="old_bu_eff_dt" value="">
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="bu_eff_dt" value="" 
			      						maxLength="10" onblur="validateDate(this);checkEffectiveDate(this,document.forms[0].old_bu_eff_dt.value);" 
			      						onchange="validateDate(this);checkEffectiveDate(this,document.forms[0].old_bu_eff_dt.value);" autocomplete="off">
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
					<div class="row m-b-5 subheader">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="form-group row" align="left">
				    			<label class="form-label"><b>Tax/ID Detail</b></label>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>Tax/ID Type</th>
										<th>Tax/ID Name</th>
										<th>Tax/ID No</th>
										<th>Eff Date</th>
										<th>Remark</th>
									</tr>
								</thead>
								<tbody>
									<%if(VSTAT_CD.size() > 0){ %>
										<%for(int i=0; i< VSTAT_CD.size(); i++){ %>
										<tr>
											<td>
												<div>
													<%=VSTAT_TYPE.elementAt(i)%>
													<input type="hidden" class="form-control form-control-sm" name="bu_stat_cd" value="<%=VSTAT_CD.elementAt(i)%>">
												</div>
											</td>
											<td><div><%=VSTAT_NM.elementAt(i)%></div></td>
											<td>
												<div align="center">
													<%if(VSTAT_CD.elementAt(i).equals("1001")){ %>
													<input type="text" class="form-control form-control-sm" name="bu_stat_no" value="<%=pan_no%>" style="width:150px;" readonly="readonly">
													<span class="s-red" id="buSpanPan">*Submit Once to Save</span>
													<%}else{ %>
													<input type="text" class="form-control form-control-sm" name="bu_stat_no" value="" style="width:150px;">
													<%} %>
												</div>
											</td>
											<td>
												<div align="center">
													<div class="input-group input-group-sm" >
														<%if(VSTAT_CD.elementAt(i).equals("1001")){ %>
							      						<input type="text" class="form-control form-control-sm date" name="bu_stat_eff_dt" value="<%=pan_dt%>" 
							      						maxLength="10" onblur="validateDate(this);" 
							      						onchange="validateDate(this);" autocomplete="off" style="width:100px;" readOnly>
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      						<%}else{ %>
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="bu_stat_eff_dt" value="" 
							      						maxLength="10" onblur="validateDate(this);" 
							      						onchange="validateDate(this);" autocomplete="off" style="width:100px;">
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      						<%} %>
						      						</div>
												</div>
											</td>
											<td>
												<div align="center">
													<textarea class="form-control form-control-sm" name="bu_stat_remark" cols="35" rows="2" maxlength="100" style="width:150px;"></textarea>
												</div>
											</td>
										</tr>
										<%}%>
									<%}else{ %>
									<tr>
										<td colspan="11" align="center">
										<%=utilmsg.infoMessage("<b>No Tax/ID Detail Configured!</b>")%>
										</td>
									</tr>
									<%} %>
							</table>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
					<%if(write_access.equals("Y")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doBuSubmit();">
					<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
					<%} %>
				</div>
      		</div>
    	</div>
  	</div>
</div>


<!-- Bank Detail -->
<div class="modal fade" id="BankModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Bank Detail Config
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Category<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">				    			
			    					<select class="form-select form-select-sm"  name="bank_category" id="bank_category" onchange="fetchBankDtl()">
										<option value="RLNG">RLNG</option>
										<option value="DLNG">DLNG</option>
			 							<option value="DERV">Derivatives</option>
						 			</select>
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label" style="color:#842029;"><b><i class='fa fa-exclamation-triangle fa-lg'></i>&nbsp;Eff Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
				      					<input type="hidden" name="old_bank_eff_dt" value="">
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="bank_eff_dt" value="" 
			      						maxLength="10" onblur="validateDate(this);checkEffectiveDate(this,document.forms[0].old_bank_eff_dt.value);" 
			      						onchange="validateDate(this);checkEffectiveDate(this,document.forms[0].old_bank_eff_dt.value);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Bank Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="bank_name" value="" maxLength="100">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>A/C No<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="account_no" value="" maxLength="20">
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Branch<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="bank_branch" value="" maxLength="100">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label id="lbl_ifsc" class="form-label"><b>IFSC code<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ifsc_code" value="" maxLength="20">
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>State<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="bank_state">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VSTATE_NM.size();i++){ %>
										<option value="<%=VSTATE_NM.elementAt(i)%>"><%=VSTATE_NM.elementAt(i)%></option>
										<%} %>
									</select>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="form-group row">
					    		<div class="col-sm-12 col-xs-12 col-md-12">
					    			<span><%=utilmsg.errorMessage("<b>Same Eff. Date will overwrite the Bank details, Different Eff. Date will create new entry!</b>")%></span>
					    		</div>
					    	</div>
					    </div>
					</div>
				</div>
			</div>
			<div class="modal-footer cdfooter">
				<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
					<%if(write_access.equals("Y")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doBankSubmit();">
					<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
					<%} %>
				</div>	
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
	      			<div class="row m-b-5">
				    	<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Bank Detail(s)</label>
					</div>
					<div class="row m-b-5">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Bank Detail</th>
										<th>Eff Date</th>
										<th>Category</th>
									</tr>
								</thead>
								<tbody>
								<%if(VBANK_FORMULA.size()>0){ %>
								<%for(int i=0;i<VBANK_FORMULA.size(); i++){ %>
									<tr>
										<td align="center"><%=i+1%></td>
										<td><%=VBANK_FORMULA.elementAt(i)%></td>
										<td align="center"><%=VBANK_EFF_DT.elementAt(i)%></td>
										<td align="center">
										<%if(VBANK_CATEGORY.elementAt(i).equals("DERV")){%>
											Derivatives
										<%}else{ %>
											<%=VBANK_CATEGORY.elementAt(i) %>
										<%} %>
										</td>
									</tr>
								<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="4"><%=utilmsg.infoMessage("<b>Bank Detail is Not Config!</b>") %></td>
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
</div>


<input type="hidden" name="option" value="ENTITY_MST">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="prev_entity_role" value="<%=entity_role%>">
<input type="hidden" name="old_value" value="">

<input type="hidden" name="business_unit" value="<%=business_unit%>">
<input type="hidden" name="status" value="<%=status%>">

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
document.forms[0].eff_dt.readOnly=true;
document.forms[0].name.readOnly=true;
document.forms[0].abbr.readOnly=true;
document.forms[0].pan_no.readOnly=true;
document.forms[0].pan_dt.readOnly=true;
document.forms[0].notes.readOnly=true;
document.forms[0].sap_code.readOnly=true;
document.forms[0].kyc_chk.style.pointerEvents = "none";
document.forms[0].igx_chk.style.pointerEvents = "none";

<%if(!entity_role.equals("B") && !entity_role.equals("G")) {%>
document.forms[0].reg_eff_dt[0].readOnly=true;
document.forms[0].address[0].readOnly=true;
document.forms[0].city[0].readOnly=true;
document.forms[0].zone[0].style.pointerEvents = "none";
document.forms[0].state[0].readOnly=true;
//document.forms[0].state[0].style.pointerEvents = "none";
document.forms[0].pin[0].readOnly=true;
document.forms[0].country[0].style.pointerEvents = "none";
document.forms[0].phone[0].readOnly=true;
document.forms[0].alt_phone[0].readOnly=true;
document.forms[0].cell[0].readOnly=true;
document.forms[0].fax1[0].readOnly=true;
document.forms[0].fax2[0].readOnly=true;
document.forms[0].email[0].readOnly=true;

document.forms[0].reg_eff_dt[1].classList.add("fmsdtpick");
document.forms[0].reg_eff_dt[2].classList.add("fmsdtpick");
<%}else{%>
document.forms[0].reg_eff_dt[0].classList.add("fmsdtpick");
document.forms[0].reg_eff_dt[1].classList.add("fmsdtpick");
document.forms[0].reg_eff_dt[2].classList.add("fmsdtpick");
<%}%>

var addChk = document.forms[0].add_chk;
if(addChk!=null && addChk.length!=undefined)
{
	for(var i=0; i<addChk.length; i++)
	{
		if(!addChk[i].checked)
		{
			document.forms[0].reg_eff_dt[i].disabled=true;
			document.forms[0].address_type[i].disabled=true;
			document.forms[0].address[i].disabled=true;
			document.forms[0].city[i].disabled=true;
			document.forms[0].zone[i].disabled=true;
			document.forms[0].state[i].disabled=true;
			document.forms[0].pin[i].disabled=true;
			document.forms[0].country[i].disabled=true;
			document.forms[0].phone[i].disabled=true;
			document.forms[0].alt_phone[i].disabled=true;
			document.forms[0].cell[i].disabled=true;
			document.forms[0].fax1[i].disabled=true;
			document.forms[0].fax2[i].disabled=true;
			document.forms[0].email[i].disabled=true;
		}
	}
}

function enabledAddress(i)
{
	var addChkbox = document.forms[0].add_chk;
	if(addChkbox[i].checked)
	{
		document.forms[0].reg_eff_dt[i].disabled=false;
		document.forms[0].address_type[i].disabled=false;
		document.forms[0].address[i].disabled=false;
		document.forms[0].city[i].disabled=false;
		document.forms[0].zone[i].disabled=false;
		document.forms[0].state[i].disabled=false;
		document.forms[0].pin[i].disabled=false;
		document.forms[0].country[i].disabled=false;
		document.forms[0].phone[i].disabled=false;
		document.forms[0].alt_phone[i].disabled=false;
		document.forms[0].cell[i].disabled=false;
		document.forms[0].fax1[i].disabled=false;
		document.forms[0].fax2[i].disabled=false;
		document.forms[0].email[i].disabled=false;
	}
	else
	{
		document.forms[0].reg_eff_dt[i].disabled=true;
		document.forms[0].address_type[i].disabled=true;
		document.forms[0].address[i].disabled=true;
		document.forms[0].city[i].disabled=true;
		document.forms[0].zone[i].disabled=true;
		document.forms[0].state[i].disabled=true;
		document.forms[0].pin[i].disabled=true;
		document.forms[0].country[i].disabled=true;
		document.forms[0].phone[i].disabled=true;
		document.forms[0].alt_phone[i].disabled=true;
		document.forms[0].cell[i].disabled=true;
		document.forms[0].fax1[i].disabled=true;
		document.forms[0].fax2[i].disabled=true;
		document.forms[0].email[i].disabled=true;
	}
}


function fetchTaxId(seq_no)
{
	var entity_role = document.forms[0].entity_role.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	if((seq_no !="" && seq_no!="0") && (counterparty_cd != "" && counterparty_cd!="0") && (entity_role != "" && entity_role !="0"))
	{
		$.post("../servlet/DB_Master_Ajax"+ "?counterparty_cd="+counterparty_cd+"&entity="+entity_role+"&plant_seq_no="+seq_no+"&setCallType=fetchTaxID", function(responseJson) {
			//console.log(responseJson);
			$.each(responseJson, function(index, json) {
				$.each(json.TAX_ID, function(index_1, json_1) {
					if(parseInt(json.TAX_ID.length) == 1)
					{
						if(json_1.STAT_CD == "1001")
						{
							if(json_1.STAT_NO == "")
							{
								document.forms[0].stat_no.value="<%=pan_no%>";
								document.getElementById("spanPan").style.display="inline"
							}
							else
							{
								document.forms[0].stat_no.value=json_1.STAT_NO;
								document.getElementById("spanPan").style.display="none"
							}
							
							if(json_1.STAT_EFF_DT == "")
							{
								document.forms[0].stat_eff_dt.value="<%=pan_dt%>";
							}
							else
							{
								document.forms[0].stat_eff_dt.value=json_1.STAT_EFF_DT;
							}
						}
						else
						{
							document.forms[0].stat_no.value=json_1.STAT_NO;
							document.getElementById("spanPan").style.display="none"
							
							document.forms[0].stat_eff_dt.value=json_1.STAT_EFF_DT;
						}
						document.forms[0].stat_remark.value=json_1.STAT_REMARK;
					}
					else
					{
						if(json_1.STAT_CD == "1001")
						{
							if(json_1.STAT_NO == "")
							{
								document.forms[0].stat_no[index_1].value="<%=pan_no%>";
								document.getElementById("spanPan").style.display="inline"
							}
							else
							{
								document.forms[0].stat_no[index_1].value=json_1.STAT_NO;
								document.getElementById("spanPan").style.display="none"
							}
							
							if(json_1.STAT_EFF_DT == "")
							{
								document.forms[0].stat_eff_dt[index_1].value="<%=pan_dt%>";
							}
							else
							{
								document.forms[0].stat_eff_dt[index_1].value=json_1.STAT_EFF_DT;
							}
						}
						else
						{
							document.forms[0].stat_no[index_1].value=json_1.STAT_NO;
							document.getElementById("spanPan").style.display="none"
							
							document.forms[0].stat_eff_dt[index_1].value=json_1.STAT_EFF_DT;
						}
						document.forms[0].stat_remark[index_1].value=json_1.STAT_REMARK;
					}
				});
			});
		});
	}
}

function fetchBuTaxId(seq_no)
{
	var entity_role = document.forms[0].entity_role.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	if((seq_no !="" && seq_no!="0") && (counterparty_cd != "" && counterparty_cd!="0") && (entity_role != "" && entity_role !="0"))
	{
		$.post("../servlet/DB_Master_Ajax?counterparty_cd="+counterparty_cd+"&entity="+entity_role+"&plant_seq_no="+seq_no+"&setCallType=fetchBuTaxID", function(responseJson) {
			//console.log(responseJson);
			$.each(responseJson, function(index, json) {
				$.each(json.TAX_ID, function(index_1, json_1) {
					if(parseInt(json.TAX_ID.length) == 1)
					{
						if(json_1.STAT_CD == "1001")
						{
							if(json_1.STAT_NO == "")
							{
								document.forms[0].bu_stat_no.value="<%=pan_no%>";
								document.getElementById("buSpanPan").style.display="inline"
							}
							else
							{
								document.forms[0].bu_stat_no.value=json_1.STAT_NO;
								document.getElementById("buSpanPan").style.display="none"
							}
							
							if(json_1.STAT_EFF_DT == "")
							{
								document.forms[0].bu_stat_eff_dt.value="<%=pan_dt%>";
							}
							else
							{
								document.forms[0].bu_stat_eff_dt.value=json_1.STAT_EFF_DT;
							}
						}
						else
						{
							document.forms[0].bu_stat_no.value=json_1.STAT_NO;
							document.getElementById("buSpanPan").style.display="none"
							
							document.forms[0].bu_stat_eff_dt.value=json_1.STAT_EFF_DT;
						}
						document.forms[0].bu_stat_remark.value=json_1.STAT_REMARK;
					}
					else
					{
						if(json_1.STAT_CD == "1001")
						{
							if(json_1.STAT_NO == "")
							{
								document.forms[0].bu_stat_no[index_1].value="<%=pan_no%>";
								document.getElementById("buSpanPan").style.display="inline"
							}
							else
							{
								document.forms[0].bu_stat_no[index_1].value=json_1.STAT_NO;
								document.getElementById("buSpanPan").style.display="none"
							}
							
							if(json_1.STAT_EFF_DT == "")
							{
								document.forms[0].bu_stat_eff_dt[index_1].value="<%=pan_dt%>";
							}
							else
							{
								document.forms[0].bu_stat_eff_dt[index_1].value=json_1.STAT_EFF_DT;
							}
						}
						else
						{
							document.forms[0].bu_stat_no[index_1].value=json_1.STAT_NO;
							document.getElementById("buSpanPan").style.display="none"
							
							document.forms[0].bu_stat_eff_dt[index_1].value=json_1.STAT_EFF_DT;
						}
						document.forms[0].bu_stat_remark[index_1].value=json_1.STAT_REMARK;
					}
				});
			});
		});
	}
}

function fetchBankDtl()
{
	var entity_role = document.forms[0].entity_role.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var bank_category = document.forms[0].bank_category.value;
	
	if(bank_category == "DERV")
	{
		var lbl_ifsc = document.getElementById('lbl_ifsc');
		lbl_ifsc.innerHTML = "<b>Swift Code<span class=\"s-red\">*</span></b>";
	}
	
	if((bank_category !="" && bank_category!="0") && (counterparty_cd != "" && counterparty_cd!="0") && (entity_role != "" && entity_role !="0"))
	{
		$.post("../servlet/DB_Master_Ajax?counterparty_cd="+counterparty_cd+"&entity="+entity_role+"&bank_category="+bank_category+"&setCallType=fetchBankDtl", function(responseJson) {
			console.log(responseJson);
			$.each(responseJson, function(index, json) 
			{
                document.forms[0].bank_category.value = json.BANK_CATEGORY;
                document.forms[0].bank_eff_dt.value = json.BANK_EFF_DT;
                document.forms[0].bank_name.value = json.BANK_NAME;
                document.forms[0].account_no.value = json.BANK_ACCOUNT_NO;
                document.forms[0].bank_branch.value = json.BANK_BRANCH;
                document.forms[0].ifsc_code.value = json.IFSC_CODE;
                document.forms[0].bank_state.value = json.BANK_STATE;
                document.forms[0].old_bank_eff_dt.value=json.BANK_EFF_DT;
			});
		});
	}
}
</script>
</form>
</body>
</html>