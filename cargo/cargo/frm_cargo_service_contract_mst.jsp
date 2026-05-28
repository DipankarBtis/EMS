<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh(opration)
{
	var entity_type = document.forms[0].entity_type.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var prev_entity_role = document.forms[0].prev_entity_role.value;
	
	if(entity_type != prev_entity_role)
	{
		counterparty_cd ="0";
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_cargo_service_contract_mst.jsp?entity_type="+entity_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var newWindow;
function openContList()
{
	var entity_type = document.forms[0].entity_type.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	if(entity_type == "" || entity_type == "0")
	{
		alert("Select Entity Type!!");
	}
	else
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open("frm_cargo_svc_cont_list.jsp?entity_type="+entity_type+"&counterparty_cd="+counterparty_cd,"Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_cargo_svc_cont_list.jsp?entity_type="+entity_type+"&counterparty_cd="+counterparty_cd,"Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function doSubmit()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var ent_dt = document.forms[0].ent_dt.value;
	var ent_time = document.forms[0].ent_time.value;
	var cont_ref_no = document.forms[0].cont_ref_no.value;
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	var signing_dt = document.forms[0].signing_dt.value;
	var signing_time = document.forms[0].signing_time.value;
	
	var chk_plant = document.forms[0].chk_plant;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	
	var prov_svc_rate = document.forms[0].prov_svc_rate.value;
	var final_svc_rate = document.forms[0].final_svc_rate.value;
	
	var prov_svc_rate_unit1 = document.forms[0].prov_svc_rate_unit1.value;
	var final_svc_rate_unit1 = document.forms[0].final_svc_rate_unit1.value;
	
	var prov_svc_rate_unit2 = document.forms[0].prov_svc_rate_unit2.value;
	var final_svc_rate_unit2 = document.forms[0].final_svc_rate_unit2.value;
	
	var msg="";
	var flag=true;
	
	var entity_type = document.forms[0].entity_type.value;
	
	if(entity_type == "" || entity_type == "0")
	{
		msg+="Select Entity Type!!\n";
		flag=false;
	}
	
	if(counterparty_cd == "" || counterparty_cd == "0")
	{
		msg+="Select Counterparty!\n";
		flag=false;
	}
	
	if(cont_ref_no == "" || cont_ref_no == "0")
	{
		msg+="Select Contract Ref#!\n";
		flag=false;
	}

	if(trim(signing_dt) == "")
	{
		msg+="Enter Contract(Signing) Date!\n";
		flag=false;
	}
	if(trim(signing_time) == "")
	{
		msg+="Enter Contract(Signing) Time!\n";
		flag=false;
	}
	
	if(opration=="MODIFY")
	{
		var cont_no = document.forms[0].cont_no.value;
		if(cont_no == "" || cont_no == "0")
		{
			msg+="Select Contact!\n";
			flag=false;
		}
	}
	
	if(trim(ent_dt) == "")
	{
		msg+="Enter Contract Enter Date!\n";
		flag=false;
	}
	if(trim(ent_time) == "")
	{
		msg+="Enter Contract Enter Time!\n";
		flag=false;
	}
	
	if(trim(start_dt) == "")
	{
		msg+="Enter Contract Start Date!\n";
		flag=false;
	}
	
	if(trim(end_dt) == "")
	{
		msg+="Enter Contract End Date!\n";
		flag=false;
	}
	
	if(prov_svc_rate == "" || prov_svc_rate_unit1 == "" || prov_svc_rate_unit2 == "")
	{
		msg+="Enter Contract Provisional Service Charge Details!\n";
		flag=false;
	}
	
	if(final_svc_rate == "" || final_svc_rate_unit1 == "" || final_svc_rate_unit2 == "")
	{
		msg+="Enter Contract Final Service Charge Details!\n";
		flag=false;
	}
	
	//FOR TRADER PLANT
	var chkFlg = false;
	if(chk_plant!=null && chk_plant!=undefined)
	{
		if(chk_plant.length!=undefined)
		{
			for(var i=0;i<chk_plant.length;i++)
			{
				if(chk_plant[i].checked)
				{
					chkFlg = true;	
				}
			}
		}
		else
		{
			if(chk_plant.checked)
			{
				chkFlg = true;	
			}
		}
	}
	if(chkFlg==false)
	{
		msg += "Please Select Atleast One Agent-Plant!\n";
		flag=false;
	}
	
	//FOR BUSINESS UNIT
	chkFlg = false;
	if(chk_bu_plant!=null && chk_bu_plant!=undefined)
	{
		if(chk_bu_plant.length!=undefined)
		{
			for(var i=0;i<chk_bu_plant.length;i++)
			{
				if(chk_bu_plant[i].checked)
				{
					chkFlg = true;	
				}
			}
		}
		else
		{
			if(chk_bu_plant.checked)
			{
				chkFlg = true;	
			}
		}
	}
	if(chkFlg==false)
	{
		msg += "Please Select Atleast One Business Unit Plant!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a;
		if(opration=="MODIFY")
		{
			a = confirm("Do you want to Modify Service Contract?");
		}
		else
		{
			a = confirm("Do you want to Create New Service Contract?");
		}
		if(a)
		{
			var temp_msg="";
			if(trim(no_of_billing_dtl) != "")
			{
				if(parseInt(no_of_billing_dtl) <= 0)
				{
					temp_msg += "Please fill-in the Billing Detail after Submitting Contract Detail!\n";
				}
			}
			else
			{
				temp_msg += "Please fill-in the Billing Detail after Submitting Contract Detail!!\n";
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

function setValues(strPlant,strBuPlant)
{
	var chk_plant = document.forms[0].chk_plant;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	
	var sepPlant = strPlant.split("@");
	var sepBuPlant = strBuPlant.split("@");

	//FOR PLANT
	if(chk_plant!=null && chk_plant.length!=undefined)
 	{
  		for(var i=0;i<chk_plant.length;i++)
  		{
   			for(var j=0;j<sepPlant.length;j++)
   			{
     			if(chk_plant[i].value == sepPlant[j])
     			{
     				chk_plant[i].checked = true;
     			}
   			} 
  		} 
 	}
 	else if(chk_plant!=null)
 	{
   		for(var j=0;j<sepPlant.length;j++)
   		{
   			if(chk_plant.value == sepPlant[j])
     		{
   				chk_plant.checked = true;
     		}
   		} 
 	}
	
	//FOR BU PLANT
	if(chk_bu_plant!=null && chk_bu_plant.length!=undefined)
 	{
  		for(var i=0;i<chk_bu_plant.length;i++)
  		{
   			for(var j=0;j<sepBuPlant.length;j++)
   			{
     			if(chk_bu_plant[i].value == sepBuPlant[j])
     			{
     				chk_bu_plant[i].checked = true;
     			}
   			} 
  		} 
 	}
 	else if(chk_bu_plant!=null)
 	{
   		for(var j=0;j<sepBuPlant.length;j++)
   		{
   			if(chk_bu_plant.value == sepBuPlant[j])
     		{
   				chk_bu_plant.checked = true;
     		}
   		} 
 	}
}

function setContractDetail(entity_type,countpty_cd,cont_full_no,cont_no,cont_linked_cargo,cont_linked_cargo_name)
{
	var opration = document.forms[0].opration.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_cargo_service_contract_mst.jsp?entity_type="+entity_type+"&opration="+opration+"&cont_linked_cargo_name="+cont_linked_cargo_name+
			"&counterparty_cd="+countpty_cd+"&cont_no="+cont_no+"&cont_linked_cargo="+cont_linked_cargo+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function checkProvRateFormat()
{
    let rateInput = document.getElementById('prov_svc_rate').value.trim();
    let unit1 = document.getElementById('prov_svc_rate_unit1').value;
    let unit2 = document.getElementById('prov_svc_rate_unit2').value;
    
    let formatRegex;
    let alertMessage;
    
    if(rateInput == "")
    {
    	
    }
    else
    {
    	if (unit1 === '1')
        {
            if (unit2 === '0')
            { 
                formatRegex = /^\d{1,6}(\.\d{1,2})?$/;
                alertMessage = ("Please, Enter In the Required  Format..(6 ,2)");
            } 
            else
            {
                formatRegex = /^\d{1,4}(\.\d{1,2})?$/;
                alertMessage = ("Please, Enter In the Required  Format..(4 ,2)");
            }
        } 
        else if (unit1 === '2')
        {
            if (unit2 === '0')
            {
                formatRegex = /^\d{1,6}(\.\d{1,4})?$/;
                alertMessage = ("Please, Enter In the Required  Format..(6 ,4)");
            } 
            else if (unit2 === '1') 
            {
                formatRegex = /^\d{1,2}(\.\d{1,4})?$/;
                alertMessage = ("Please, Enter In the Required  Format..(2 ,4)");
            } 
            else
            {
                formatRegex = /^\d{1,2}(\.\d{1,4})?$/;
                alertMessage = ("Please, Enter In the Required  Format..(2 ,4)");
            }
        } 
        else
        {
        	return;
        }

        if (!formatRegex.test(rateInput))
        {
            alert(alertMessage);
            document.getElementById('prov_svc_rate').value = "";
        }
    }
}
  
function checkFinalRateFormate()
{ 
	let rateInput = document.getElementById('final_svc_rate').value.trim();
    let unit1 = document.getElementById('final_svc_rate_unit1').value;
    let unit2 = document.getElementById('final_svc_rate_unit2').value;
    
    let formatRegex;
    let alertMessage;
    
    if(rateInput == "")
    {
    	
    }
    else
    {
    	if (unit1 === '1')
        {
            if (unit2 === '0')
            { 
                formatRegex = /^\d{1,6}(\.\d{1,2})?$/;
                alertMessage = ("Please, Enter In the Required  Format..(6 ,2)");
            } 
            else
            {
                formatRegex = /^\d{1,4}(\.\d{1,2})?$/;
                alertMessage = ("Please, Enter In the Required  Format..(4 ,2)");
            }
        } 
        else if (unit1 === '2')
        {
            if (unit2 === '0')
            {
                formatRegex = /^\d{1,6}(\.\d{1,4})?$/;
                alertMessage = ("Please, Enter In the Required  Format..(6 ,4)");
            } 
            else if (unit2 === '1') 
            {
                formatRegex = /^\d{1,2}(\.\d{1,4})?$/;
                alertMessage = ("Please, Enter In the Required  Format..(2 ,4)");
            } 
            else
            {
                formatRegex = /^\d{1,2}(\.\d{1,4})?$/;
                alertMessage = ("Please, Enter In the Required  Format..(2 ,4)");
            }
        } 
        else
        {
        	return;
        }

        if (!formatRegex.test(rateInput))
        {
            alert(alertMessage);
            document.getElementById('final_svc_rate').value = "";
        }
    }
}

function openBillingDtl()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var cont_no = document.forms[0].cont_no.value;
	var entity_type = document.forms[0].entity_type.value;
	var start_dt = document.forms[0].start_dt.value;
	var final_rate_unit = document.forms[0].final_svc_rate_unit1.value;
	var provisional_rate_unit = document.forms[0].prov_svc_rate_unit1.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_svc_billing_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
			"&start_dt="+start_dt+"&entity_type="+entity_type+"&u="+u+"&final_rate_unit="+final_rate_unit+"&provisional_rate_unit="+provisional_rate_unit;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Cargo Service Contract Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Cargo Service Contract Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}
</script>
</head>

<jsp:useBean class="com.etrm.fms.cargo.DB_cargo_service_cont_master" id="db_cargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String dateTime = utildate.getSysdateWithTime24hr();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String entity_type =request.getParameter("entity_type")==null?"0":request.getParameter("entity_type");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String cont_no = request.getParameter("cont_no")==null?"0":request.getParameter("cont_no");
String cont_linked_cargo = request.getParameter("cont_linked_cargo")==null?"":request.getParameter("cont_linked_cargo");
String cont_linked_cargo_name = request.getParameter("cont_linked_cargo_name")==null?"":request.getParameter("cont_linked_cargo_name");

String entity_name="";
String contract_type="";

if(entity_type.equals("S"))
{
	entity_name="Surveyor Agent";
	contract_type="Y";
}
else if(entity_type.equals("V"))
{
	entity_name="Vessel Agent";
	contract_type="A";
}
else if(entity_type.equals("H"))
{
	entity_name="CH Agent";
	contract_type="H";
}

db_cargo.setCallFlag("CARGO_SVC_CONT_MST");
db_cargo.setCounterparty_cd(counterparty_cd);
db_cargo.setEntity_role(entity_type);
db_cargo.setCont_no(cont_no);
db_cargo.setContract_type(contract_type);
db_cargo.setComp_cd(owner_cd);
db_cargo.setOpration(opration);
db_cargo.init();

Vector VCOUNTERPARTY_CD = db_cargo.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = db_cargo.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = db_cargo.getVCOUNTERPARTY_ABBR();

Vector VPLANT_ABBR = db_cargo.getVPLANT_ABBR();
Vector VPLANT_SEQ_NO = db_cargo.getVPLANT_SEQ_NO();
Vector VBU_CD = db_cargo.getVBU_CD();
Vector VBU_PLANT_NM = db_cargo.getVBU_PLANT_NM();
Vector VBU_PLANT_ABBR = db_cargo.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = db_cargo.getVBU_PLANT_SEQ_NO();

Vector VSEL_BU_CD = db_cargo.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = db_cargo.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_BU_PLANT_ABBR = db_cargo.getVSEL_BU_PLANT_ABBR();
Vector VSEL_SPLIT_VALUE = db_cargo.getVSEL_SPLIT_VALUE();
Vector VSEL_PLANT_ABBR = db_cargo.getVSEL_PLANT_ABBR();
Vector VSEL_PLANT_SEQ_NO = db_cargo.getVSEL_PLANT_SEQ_NO();
Vector VSEL_AGENT_CD = db_cargo.getVSEL_AGENT_CD();

String cont_disp_no = db_cargo.getCont_disp_no();
//String cont_no = db_cargo.getCont_no();
String signing_dt = db_cargo.getSigning_dt();
String signing_time = db_cargo.getSigning_time();
String cont_ref_no = db_cargo.getCont_ref_no();
String start_dt = db_cargo.getStart_dt();
String end_dt = db_cargo.getEnd_dt();
String prov_svc_rate =db_cargo.getProv_svc_rate();
String prov_svc_rate_unit1 =db_cargo.getProv_svc_rate_unit1();
String prov_svc_rate_unit2 =db_cargo.getProv_svc_rate_unit2();
String final_svc_rate =db_cargo.getFinal_svc_rate();
String final_svc_rate_unit1 =db_cargo.getFinal_svc_rate_unit1();
String final_svc_rate_unit2 = db_cargo.getFinal_svc_rate_unit2();
String ent_dt=db_cargo.getEnt_dt();
String ent_time=db_cargo.getEnt_time();
String cont_status = db_cargo.getCont_Status();
String no_of_billing_dtl = db_cargo.getNo_of_billing_dtl();
String cargo_disp_full_name = db_cargo.getCargo_disp_full_name();

if(ent_dt.equals(""))
{
	String split[] = dateTime.split(" ");
	ent_dt = split[0];
	ent_time = split[1];
}
if(signing_time.equals("")){
	signing_time="00:00";
}
String strPlant="";
String strBuPlant="";
for(int i=0;i<VSEL_PLANT_SEQ_NO.size();i++)
{
	strPlant = strPlant + VSEL_PLANT_SEQ_NO.elementAt(i)+"@";
}
for(int i=0;i<VSEL_BU_PLANT_SEQ_NO.size();i++)
{
	strBuPlant = strBuPlant + VSEL_BU_PLANT_SEQ_NO.elementAt(i)+"@";
}

%>
<body onload="setValues('<%=strPlant%>','<%=strBuPlant%>');">
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_cargo_service_cont_master">

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
					    	Cargo Service Contract
					    </div>
					    <div class="btn-group">
						  <label class="btn btn-outline-secondary btngrp <%if(opration.equals("INSERT")){%>btnactive<%}%>" onclick="refresh('INSERT');">
						  	<i class="fa fa-plus-circle"></i>&nbsp;New
						  </label>
						  <label class="btn btn-outline-secondary btngrp <%if(opration.equals("MODIFY")){%>btnactive<%}%>" onclick="refresh('MODIFY');">
						  	<i class="fa fa-edit"></i>&nbsp;Modify
						  </label>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12"> 
							<div class="form-group row">
							&nbsp;
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Cargo Service Entity<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="entity_type" onchange="refresh('<%=opration%>');">
										<option value="0">--Select--</option>
										<option value="S">Surveyor Agent</option>
										<option value="V">Vessel Agent</option>
										<option value="H">Custom House Agent</option>
									</select>
									<script>document.forms[0].entity_type.value="<%=entity_type%>"</script>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select <%=entity_name%> <span class="s-red">*</span></b></label>
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
					</div>
					<%if(opration.equals("MODIFY")){ %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    			<!-- <label class="form-label"><b>Contract No<span class="s-red">*</span></b></label> -->
				    			<input type="button" class="btn btn-info btn-sm select_btn" value="Select <%//=entity_name%> contract" onclick="openContList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="cont_name" value="<%=cont_disp_no%>" maxLength="50" readOnly>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Status</b></label>

				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    			<% if(cont_status.equals("Y")){%>
				    				<label class="form-label"><b>Active</b></label>
				    			<%} else{%>
				    				<label class="form-label"><b>In-active</b></label>
				    			<%} %>
				    				
				    				<input type="hidden" name="cont_status" value="<%=cont_status%>">
				      			</div>
				  			</div>
						</div>
					</div>
					<%} %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Contract Ref#<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="cont_ref_no" value="<%=cont_ref_no%>" maxlength="25">
				      				<input type="hidden" name="contract_type" value="<%=contract_type%>">
				      				<input type="hidden" name="cont_no" value="<%=cont_no%>">
				    			</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Contract Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="signing_dt" value="<%=signing_dt%>" maxLength="10" 
			      						onblur="validateDate(this);checkSigningStartDt(document.forms[0].start_dt, this.value);" 
			      						onchange="validateDate(this);checkSigningStartDt(document.forms[0].start_dt,this.value);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="signing_time" value="<%=signing_time%>" maxLength="5" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Contract Enter Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date" name="ent_dt" value="<%=ent_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" readOnly>
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="ent_time" value="<%=ent_time%>" maxLength="5" onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off" readOnly>
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Start Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="start_dt" value="<%=start_dt%>" maxLength="10" 
			      						onblur="validateDate(this);checkStartEndDate(this,document.forms[0].end_dt,'F');" 
			      						onchange="validateDate(this);checkStartEndDate(this,document.forms[0].end_dt,'F');" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    		</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>End Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="end_dt" value="<%=end_dt%>" maxLength="10" 
			      						onblur="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');" 
			      						onchange="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
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
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<%if(VBU_CD.size() > 0) {%>
					    				<%for(int i=0; i<VBU_CD.size(); i++){ %>
					    					<input type="checkbox" class="form-check-input" name="chk_bu_plant" value="<%=VBU_PLANT_SEQ_NO.elementAt(i)%>">&nbsp;<%=VBU_PLANT_ABBR.elementAt(i)%>&nbsp;&nbsp;
					    				<%}%>
				    				<%}else{ %>
				    					<%=utilmsg.warningMessage("Please configure Business Units!")%>
				    				<%} %>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b><%=entity_name%> BU<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<%if(VPLANT_SEQ_NO.size() > 0) {%>
					    				<%for(int i=0; i<VPLANT_SEQ_NO.size(); i++){ %>
					    					<input type="checkbox" class="form-check-input" name="chk_plant" value="<%=VPLANT_SEQ_NO.elementAt(i)%>">&nbsp;<%=VPLANT_ABBR.elementAt(i)%>
					    				<%}%>
				    				<%}else{ %>
				    					<%= utilmsg.warningMessage("Please configure Plants for Selected "+entity_name+"!")%>
				    				<%} %>
				    			</div>
				  			</div>
						</div>
					</div>
					<div>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Cargo Contract Detail</label>
						</div>
						<%-- <div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">
								<label class="form-label"><b>Select Customer<span class="s-red">*</span></b></label>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="col-auto">
									<select class="form-select form-select-sm" name="customer_cd">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCUSTOMER_CD.size();i++){ %>
										<option value="<%=VCUSTOMER_CD.elementAt(i)%>"><%=VCUSTOMER_ABBR.elementAt(i)%> - <%=VCUSTOMER_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].customer_cd.value="<%=customer_cd%>"</script>
								</div>	
							</div>
						</div> --%>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Linked Cargo</b></label>
					  			</div>
							</div>
							<div class="col"> 
								<div class="form-group row">		
									<div class="col">
										<input type="text" class="form-control form-control-sm" name="sales_cont_nm" value="<%=cargo_disp_full_name%>" <%if(!cargo_disp_full_name.equals("")){ %>style="background:#99ffcc;"<%} %> readOnly>
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
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Rate Components</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Provisional Service Charge<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
								    <input id="prov_svc_rate" type="text" class="form-control form-control-sm" name="prov_svc_rate" value="<%=prov_svc_rate%>" onblur="negNumber(this);checkProvRateFormat()">
								</div>
								<div class="col">
								    <select id="prov_svc_rate_unit1" class="form-select form-select-sm" name="prov_svc_rate_unit1" onchange="checkProvRateFormat()">
								        <option value="1">INR</option>
								        <option value="2">USD</option>
								    </select>
								    <script>document.forms[0].prov_svc_rate_unit1.value = "<%=prov_svc_rate_unit1%>";</script>
								</div>
								<div class="col-auto"> / </div>
								<div class="col">
								    <select id="prov_svc_rate_unit2" class="form-select form-select-sm" name="prov_svc_rate_unit2" onchange="checkProvRateFormat()">
								        <option value="1">MMBTU</option> <!-- Always refer FMS_ENERGY_UNIT -->
								        <option value="5">MT</option>
								        <option value="3">SCM</option>
								        <option value="0">Lumpsum</option> <!-- Entered 0 as it is not an Energy Unit -->
								    </select>
								    <script>document.forms[0].prov_svc_rate_unit2.value = "<%=prov_svc_rate_unit2%>";</script>
								</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Final Service Charge<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="final_svc_rate" id="final_svc_rate" onblur="negNumber(this);checkFinalRateFormate()" value="<%=final_svc_rate%>">
				    			</div>
				    			<div class="col">
		      						<select class="form-select form-select-sm" name="final_svc_rate_unit1" id="final_svc_rate_unit1" onchange="checkFinalRateFormate();">
		      							<option value="1">INR</option>
		      							<option value="2">USD</option>
		      						</select>
		      						<script>document.forms[0].final_svc_rate_unit1.value="<%=final_svc_rate_unit1%>"</script>
		      					</div>
				    			<div class="col-auto">
				    			/
				    			</div>
				    			<div class="col">
		      						<select class="form-select form-select-sm" name="final_svc_rate_unit2" id="final_svc_rate_unit2" onchange="checkFinalRateFormate();">
		      							<option value="1">MMBTU</option> <!-- Always refer FMS_ENERGY_UNIT -->
		      							<option value="5">MT</option>
		      							<option value="3">SCM</option>
		      							<option value="0">Lumpsum</option> <!-- Entered 0 as it is not an Energy Unit -->
		      						</select>
		      						<script>document.forms[0].final_svc_rate_unit2.value="<%=final_svc_rate_unit2%>"</script>
		      					</div>
				  			</div>
						</div>
					</div>
					<div <%if(opration.equals("MODIFY") && (!cont_no.equals("0") && !cont_no.equals(""))){ %><%}else{ %>style="display:none;"<%} %>>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Billing Details</label>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<input type="button" class="btn btn-sm config_btn" value="Configure Billing" onclick="openBillingDtl();">
					      			</div>
					  			</div>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    			</div>
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
		</div> &nbsp;
		<%if(!cont_no.equals("0") && !cont_no.equals("")){ %>
		<%if(no_of_billing_dtl.equals("0") || no_of_billing_dtl.equals("")){ %>
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-body cdbody">
					<%if(!cont_no.equals("0") && !cont_no.equals("")){ %>
						<%if(no_of_billing_dtl.equals("0") || no_of_billing_dtl.equals("")){ %>
							<div class="row m-b-5">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.errorMessage("<b>Billing Detail is not Configured. The Contract will not appear for FCC!</b>")%>
								</div>
							</div>
						<%} %>
					<%} %>
				</div>
			</div>
		</div>
		<%} %>
		<%} %>
	</div>
</div>

<input type="hidden" name="option" value="CARGO_SVC_CONT_MST">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="prev_entity_role" value="<%=entity_type%>">
<input type="hidden" name="no_of_billing_dtl" value="<%=no_of_billing_dtl%>">

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