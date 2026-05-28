<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function doSubmit()
{
	var freq = document.forms[0].freq.value;
	var billing_flag = document.forms[0].billing_flag.value;
	var due_date = document.forms[0].due_date.value;
	var due_dt_in = document.forms[0].due_dt_in.value
	var inv_currency = document.forms[0].inv_currency.value;
	var payment_currency = document.forms[0].payment_currency.value;
	var inv_criteria = document.forms[0].inv_criteria.value;
	var exchng_rate = document.forms[0].exchng_rate.value;
	var exch_calc_base = document.forms[0].exch_calc_base.value;
	var rate = document.forms[0].rate.value;
	var u = document.forms[0].u.value;
	
	var billing_days = document.forms[0].billing_days.value;
	
	var rate_unit = document.forms[0].rate_unit.value;
	
	var msg="";
	var flag=true;
	
	if(trim(freq) == "")
	{
		msg+="Select Billing/Payment Freq!\n";
		flag=false;
	}
	if(trim(freq) == "O")
	{
		if(trim(billing_days)=="")
		{
			msg+="Enter Number of Days to define 'Other' Billing Freq!\n";
			flag=false;
		}
	}
	if(trim(billing_flag) == "")
	{
		msg+="Select Billing Period!\n";
		flag=false;
	}
	if(trim(due_date) == "")
	{
		msg+="Enter Payment Due Period!\n";
		flag=false;
	}
	if(trim(due_dt_in) == "")
	{
		msg+="Select Consider Due Date in!\n";
		flag=false;
	}
	if(trim(inv_currency) == "" || inv_currency == "0")
	{
		msg+="Select Invoice Raised In!\n";
		flag=false;
	}
	if(trim(payment_currency) == "" || payment_currency == "0")
	{
		msg+="Select Payment Done In!\n";
		flag=false;
	}
	if(trim(rate) == "")
	{
		msg+="Select Interest Rate!\n";
		flag=false;
	}
	
	if(rate_unit == "2")
	{
		if(trim(exchng_rate) == "")
		{
			msg+="Select Exchange Rate Reference!\n";
			flag=false;
		}
		if(exch_calc_base=="D")
		{
			if(trim(inv_criteria) == "")
			{
				msg+="Select Exchange Rate Calculation Criteria!\n";
				flag=false;
			}
		}
	}
	else
	{
		document.forms[0].exchng_rate.value="";
		document.forms[0].exch_calc_base.value="";
		document.forms[0].exchg_rate_note.value="";
		document.forms[0].exchng_rate.value="";
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Submit?");
		if(a)
		{
			//document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg)
	}
}

function showExchngCriteria(obj)
{
	if(obj.value=="D")
	{
		document.getElementById("exchngCri").style.display="flex";
	}
	else
	{
		
		document.getElementById("exchngCri").style.display="none";
		document.forms[0].inv_criteria.value="";
	}
}

function setBillingDays(obj)
{
	if(obj.value=="O")
	{
		document.forms[0].billing_days.readOnly=false;
		document.getElementById("id_days").style.display="flex";
	}
	else
	{
		document.forms[0].billing_days.value="";
		document.forms[0].billing_days.readOnly=true;
		document.getElementById("id_days").style.display="none";
	}
}

function enablechk()
{
	var exclude_sat = document.forms[0].exclude_sat;
	if(exclude_sat.checked)
	{
		document.getElementById("sat_chk").style.visibility="visible";
	}
	else
	{
		document.getElementById("sat_chk").style.visibility="hidden";
	}
}

function setValues(satvalue)
{
	var sat = document.forms[0].sat;
	
	var tmpSat = satvalue.split("-");
	
	if(sat!=null && sat.length!=undefined)
 	{
  		for(var i=0;i<sat.length;i++)
  		{
   			for(var j=0;j<tmpSat.length;j++)
   			{
     			if(sat[i].value == tmpSat[j]+"Y")
     			{
     				sat[i].checked = true;
     			}
   			} 
  		} 
 	}
 	else if(sat!=null)
 	{
   		for(var j=0;j<tmpSat.length;j++)
   		{
   			if(sat.value == tmpSat[j]+"Y")
     		{
   				sat.checked = true;
     		}
   		} 
 	}
}

function showDispState()
{
	var disp_holiday_state = document.forms[0].disp_holiday_state.value;
	document.getElementById("custDisplay").innerHTML=disp_holiday_state;
	if(disp_holiday_state != "")
	{
		document.getElementById("custDisplay").style.display="inline";
	}
	else
	{
		document.getElementById("custDisplay").style.display="none";
	}
}

function setPlantState(strPlant,holiday_state_map,strContptyCd)
{
	var chk_plant = document.forms[0].chk_plant;
	var chk_plant_contpty = document.forms[0].chk_plant_contpty;
	var holiday_state = document.forms[0].holiday_state;
	var plant_seq = strPlant.split("@");
	var contPty_cd = strContptyCd.split("@");
	
	var split_state_map = holiday_state_map.split("@@");   //2//01@02@03;   3//01@02@03
	var stateCd;
	var plantCd;
	var contPtyCd;
	var stateMap;
	
	for(var k=0; k<split_state_map.length; k++)
	{
		plantCd = split_state_map[k].split("//")[0];
		contPtyCd = split_state_map[k].split("//")[1];
		stateMap = split_state_map[k].split("//")[2];
		
		if(chk_plant!=null && chk_plant.length!=undefined)
	 	{
	  		for(var i=0;i<chk_plant.length;i++)
	  		{
	   			for(var j=0;j<plant_seq.length;j++)
	   			{
	     			if(chk_plant[i].value == plant_seq[j] && chk_plant_contpty[i].value==contPty_cd[j])
	     			{
	     				chk_plant[i].checked = true;
	     				
	     				if(plantCd===plant_seq[j] && contPtyCd===contPty_cd[j])
	     				{
	     					stateCd = stateMap.split("@");
	     					
	     					for(var l=0; l<stateCd.length; l++)
	     					{
	     						var dropdown = document.getElementById('holiday_state_'+i);
	     						
	     						for (let m = 0; m < dropdown.options.length; m++) 
	     						{
	     							if (dropdown.options[m].value === stateCd[l]) 
	     							{
     									dropdown.options[m].selected=true;
	     							}
	     						}
	     					}
	     				}
	     			}
	   			} 
	  		} 
	 	}
	 	else if(chk_plant!=null)
	 	{
	   		for(var j=0;j<plant_seq.length;j++)
	   		{
	   			if(chk_plant.value == plant_seq[j])
	     		{
	   				chk_plant.checked = true;
	   				if(plantCd===plant_seq[j])
     				{
     					stateCd = stateMap.split("@");
     					
     					for(var l=0; l<stateCd.length; l++)
     					{
     						var dropdown = document.getElementById('holiday_state_0');
     						
     						for (let m = 0; m < dropdown.options.length; m++) 
     						{
     							if (dropdown.options[m].value === stateCd[l]) 
     							{
     								dropdown.options[m].selected=true;
     							}
     						}
     					}
     				}
	     		}
	   		} 
	 	}
	}
}

function doSubmitCustConfig()
{
	var chk_plant = document.forms[0].chk_plant;
	var chk_plant_abbr = document.forms[0].chk_plant_abbr;
	var chk_plant_contpty = document.forms[0].chk_plant_contpty;
	
	var holiday_state;
	
	var msg="";
	var flag=true;
	
	var display ="";
	var display_state ="";
	var map="";
	var state_map="";
	
	var countCustPlant=parseInt("0");
	
	
	if(chk_plant!=null && chk_plant.length!=undefined)
 	{
  		for(var i=0;i<chk_plant.length;i++)
  		{
  			display_state ="";
  			state_map ="";
  			holiday_state = document.getElementById('holiday_state_'+i).selectedOptions;
  			
   			if(chk_plant[i].checked)
   			{
   				var countState=parseInt("0");
   				countCustPlant+=1;
   				
   				if(display!="")
   				{
   					display+="<br>"+chk_plant_abbr[i].value;
   					map+="@@"+chk_plant[i].value+"//"+chk_plant_contpty[i].value;
   				}
   				else
   				{
   					display+=chk_plant_abbr[i].value;
   					map+=chk_plant[i].value+"//"+chk_plant_contpty[i].value;
   				}
   				
   				if(holiday_state!=null && holiday_state.length!=undefined)
   			 	{
   			  		for(var j=0;j<holiday_state.length;j++)
   			  		{
   			  			var temp = document.getElementById('holiday_state_'+i).selectedOptions[j].value;
   			  			var temp_name = document.getElementById('holiday_state_'+i).selectedOptions[j].text;
   			  			
   			  			if(temp != "")
   			  			{
   			  				countState+=1;
   			  				if(display_state!="")
   			  				{
   			  					display_state+=", <font style='background:#99ffcc'>"+temp_name+"</font>";
   			  					state_map+="@"+temp;
   			  				}
   			  				else
  			  				{
   			  					display_state+="<font style='background:#99ffcc'>"+temp_name+"</font>";
   			  					state_map+=temp;
			  				}
   			  			}
   			  		} 
   			 	}
   			 	else if(holiday_state!=null)
   			 	{
   			 		var temp = document.getElementById('holiday_state_'+i).selectedOptions[0].value;
   			 		var temp_name = document.getElementById('holiday_state_'+i).selectedOptions[0].text;
   			 		if(temp != "")
		  			{
   			 			countState+=1;
   			 			if(display_state!="")
		  				{
		  					display_state+=", <font style='background:#99ffcc'>"+temp_name+"</font>";
		  					state_map+="@"+temp;
		  				}
		  				else
		  				{
		  					display_state+="<font style='background:#99ffcc'>"+temp_name+"</font>";
		  					state_map+=temp;
	  					}
		  			}
   			 	}
   				display+=" - "+display_state;
   				map+="//"+state_map;
   				
   				if(countState<=0)
   				{
   					msg += "Please Select Atleast One State for Selected Customer-Plant!\n";
   					flag=false;
   				}
   			} 
  		} 
 	}
 	else if(chk_plant!=null)
 	{
 		display_state ="";
 		state_map ="";
 		holiday_state = document.getElementById('holiday_state_0').selectedOptions;
   		if(chk_plant.checked)
     	{
   			var countState=parseInt("0");
			countCustPlant+=1;
   			
   			if(display!="")
			{
				display+="<br>"+chk_plant_abbr.value;
				map+="@@"+chk_plant.value+"//"+chk_plant_contpty.value;
			}
			else
			{
				display+=chk_plant_abbr.value;
				map+=chk_plant.value+"//"+chk_plant_contpty.value;
			}
   			
   			if(holiday_state!=null && holiday_state.length!=undefined)
		 	{
		  		for(var j=0;j<holiday_state.length;j++)
		  		{
		  			var temp = document.getElementById('holiday_state_0').selectedOptions[j].value;
		  			var temp_name = document.getElementById('holiday_state_0').selectedOptions[j].text;
		  			
		  			if(temp != "")
			  		{
		  				countState+=1;
		  				if(display_state!="")
		  				{
		  					display_state+=", <font style='background:#99ffcc'>"+temp_name+"</font>";
		  					state_map+="@"+temp;
		  				}
		  				else
		  				{
		  					display_state+="<font style='background:#99ffcc'>"+temp_name+"</font>";
		  					state_map+=temp;
		  				}
			  		}
		  		} 
		 	}
		 	else if(holiday_state!=null)
		 	{
		 		var temp = document.getElementById('holiday_state_0').selectedOptions[0].value;
		 		var temp_name = document.getElementById('holiday_state_0').selectedOptions[0].text;
	  			
		 		if(temp != "")
		  		{
		 			countState+=1;
		 			if(display_state!="")
	  				{
	  					display_state+=", <font style='background:#99ffcc'>"+temp_name+"</font>";
	  					state_map+="@"+temp;
	  				}
	  				else
	  				{
	  					display_state+="<font style='background:#99ffcc'>"+temp_name+"</font>";
	  					state_map+=temp;
	  				}
		  		}
	  		}
   			display+=" - "+display_state;
   			map+="//"+state_map;
   			
   			if(countState<=0)
			{
				msg += "Please Select Atleast One State for Selected Customer-Plant!\n";
				flag=false;
			}
   		} 
 	}
	
	if(countCustPlant<=0)
	{
		msg += "Please Select Atleast One Customer-Plant!\n";
		flag=false;
	}
	
	
	if(!flag)
	{
		alert(msg);	
	}
	else
	{
		document.getElementById("custDisplay").innerHTML=display;
			
		if(display != "")
		{
			document.getElementById("custDisplay").style.display="inline";
		}
		if(map!="")
		{
			document.forms[0].holidayState_map.value=map;
		}
		
		
		alert("Proceed and Submit Billing Details!");	
		$("#holidayState").modal("hide");
	}
}

function setBillingPeriod(obj)
{
	if(obj.value=="T")
	{
		/* document.forms[0].freq.value="O";
		document.forms[0].freq.style.pointerEvents = "none";
		
		document.forms[0].billing_days.readOnly=false;
		document.getElementById("id_days").style.display="flex"; */
		
		document.getElementById("bp_lbl").style.display="none";
		document.getElementById("bp_elmnt").style.display="none";
	}
	else
	{
		document.getElementById("bp_lbl").style.display="block";
		document.getElementById("bp_elmnt").style.display="block";
		
		document.forms[0].freq.value=document.forms[0].hid_billing_freq.value;
		document.forms[0].freq.style.pointerEvents = "auto";
		
		document.forms[0].billing_days.readOnly=true;
		document.getElementById("id_days").style.display="none";
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DataBean_Trader_Contract_Mst" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String clearance=request.getParameter("clearance")==null?"":request.getParameter("clearance");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String rate_unit=request.getParameter("rate_unit")==null?"2":request.getParameter("rate_unit");

purchase.setCallFlag("TRADER_BILLING_DTL");
purchase.setComp_cd(owner_cd);
purchase.setCont_start_dt(start_dt);
purchase.setCounterparty_cd(counterparty_cd);
purchase.setAgmt_no(agmt_no);
purchase.setAgmt_rev_no(agmt_rev_no);
purchase.setCont_no(cont_no);
purchase.setCont_rev_no(cont_rev_no);
purchase.setContract_type(contract_type);
purchase.init();

Vector VEXCHNG_RATE_CD = purchase.getVEXCHNG_RATE_CD();
Vector VEXCHNG_RATE_NM = purchase.getVEXCHNG_RATE_NM();
Vector VINT_RATE_CD = purchase.getVINT_RATE_CD();
Vector VINT_RATE_NM = purchase.getVINT_RATE_NM();

Vector VPLANT_NAME = purchase.getVPLANT_NAME();
Vector VTAX_STRUCT_CD = purchase.getVTAX_STRUCT_CD();
Vector VTAX_STRUCT_NM = purchase.getVTAX_STRUCT_NM();
Vector VTAX_SAP_CODE = purchase.getVTAX_SAP_CODE();
Vector VBU_PLANT_NM = purchase.getVBU_PLANT_NM();
Vector VINVOICE_TYPE = purchase.getVINVOICE_TYPE(); //Pratham Bhatt: for Invoice type
Vector VINVOICE_CATEGORY = purchase.getVINVOICE_CATEGORY();	//Pratham Bhatt: for Invoice Category
		
Vector VSELECTED_PLANT_SEQ = purchase.getVSELECTED_PLANT_SEQ();
Vector VSELECTED_PLANT_COUNTPTY_CD = purchase.getVSELECTED_PLANT_COUNTPTY_CD();
Vector VSELECTED_PLANT_ABBR = purchase.getVSELECTED_PLANT_ABBR();
Vector VSTATE_NM = purchase.getVSTATE_NM();
Vector VSTATE_CODE = purchase.getVSTATE_CODE();
Vector VPLANT_SEQ = purchase.getVPLANT_SEQ();
Vector VCONTPTY_CD = purchase.getVCONTPTY_CD();

String billing_freq=purchase.getBilling_freq();
String billing_flag=purchase.getBilling_flag();
String due_date=purchase.getDue_date();
String sec_due_date=purchase.getSec_due_date();
String inv_currency=purchase.getInv_currency();
String payment_currency=purchase.getPayment_currency();
String interest_rate_cd=purchase.getInterest_rate_cd();
String interest_cal_sign=purchase.getInterest_cal_sign();
String interest_cal_per=purchase.getInterest_cal_per();
String exchng_rate_cd=purchase.getExchng_rate_cd();
String exchng_cal=purchase.getExchng_cal();
String exchng_criteria=purchase.getExchng_criteria();
String exchng_note=purchase.getExchng_note();
String due_dt_in=purchase.getDue_dt_in();
String exclude_sat=purchase.getExclude_sat();
String billing_days=purchase.getBilling_days();
String display_map_id=purchase.getDisplay_map_id();
String counterparty_abbr=purchase.getCounterparty_abbr();
String sat_days = purchase.getSat_days();
String holiday_state = purchase.getHoliday_state();
String plant_seq = purchase.getPlant_seq();
String disp_holiday_state = purchase.getDisp_holiday_state();
String split_flag = purchase.getSplit_flag();

if(due_dt_in.equals(""))
{
	due_dt_in="C";
}

if(exchng_criteria.equals(""))
{
	exchng_criteria="DUE";
}

String strPlant="";
for(int i=0;i<VPLANT_SEQ.size();i++)
{
	strPlant = strPlant + VPLANT_SEQ.elementAt(i)+"@";
}

String strContptyCd="";
for(int i=0;i<VCONTPTY_CD.size();i++)
{
	strContptyCd = strContptyCd + VCONTPTY_CD.elementAt(i)+"@";
}
%>
<body style="pointer-events: none;" onload="setBillingPeriod(document.forms[0].billing_flag);setBillingDays(document.forms[0].freq);enablechk();setValues('<%=sat_days%>');showDispState();setPlantState('<%=strPlant%>','<%=holiday_state%>','<%=strContptyCd%>');">

<form method="post" action="../servlet/Frm_Trader_Contract_Mst">

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
					    	<%=counterparty_abbr%> [<%=display_map_id%>] Billing Details
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Billing on<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="billing_flag" onchange="setBillingPeriod(this);">
							    		<option value="">--Select--</option>
							     		<option value="T"> [T] TCQ Completion </option>
							     		<option value="B"> [B] Billing Period </option>
							     	</select>
							     	<script>document.forms[0].billing_flag.value="<%=billing_flag%>"</script>
				    			</div>
				    		</div>
				    	</div>
						<div class="col-sm-2 col-xs-2 col-md-2" id="bp_lbl">
							<label class="form-label"><b>Billing/Payment Freq<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" id="bp_elmnt">
							<div class="form-group row">
				    			<div class="col-6">
				    				<select class="form-select form-select-sm" name="freq" onchange="setBillingDays(this);">
				    					<option value="W">Weekly</option>
				    					<option value="F">Fortnightly</option>
				    					<option value="M">Monthly</option>
				    					<option value="Q">Quarterly</option>
				    					<option value="O">Other</option>
				    				</select>
				    				<script>document.forms[0].freq.value="<%=billing_freq%>"</script>
				    				<input type="hidden" name="hid_billing_freq" value="<%=billing_freq%>">
				    			</div>
				    			<div class="col-6">
				    				<div class="input-group input-group-sm" id="id_days">
					    				<input type="text" class="form-control form-control-sm" name="billing_days" 
					    				onblur="negNumber(this),checkNumber1(this,2,0);" size="5" value="<%=billing_days%>" style="background:#ffffb3;">
					    				<span class="input-group-text">Days</span>
					    			</div>
				    			</div>
				    		</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Payment Due Period<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="due_date" onblur="negNumber(this),checkNumber1(this,3,0);" value="<%=due_date%>">
			      						<span class="input-group-text">Days</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">
		    				<font color="blue"><b>Secondary Payment Due Period</b></font>
		    			</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="sec_due_date" onblur="negNumber(this),checkNumber1(this,3,0);" value="<%=sec_due_date%>">
			      						<span class="input-group-text">Days</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
		    				<font color=""><b>Consider Due Date in<span class="s-red">*</span></b></font>
		    			</div>
				    	<div class="col-sm-8 col-xs-8 col-md-8">
							<div class="form-group row">
				    			<div class="col-auto">
				    				<select class="form-select form-select-sm" name="due_dt_in">
				    					<option value="C">Calendar Days</option>
				    					<option value="B">Business Days</option>
				    				</select>
				    				<script>document.forms[0].due_dt_in.value="<%=due_dt_in%>"</script>
				    			</div>
				    			<div class="col-auto">
				    				<input type="checkbox" class="form-check-input" name="exclude_sat" value="Y" <%if(exclude_sat.equals("Y")){%>checked<%}%> onClick="enablechk();">&nbsp;<b>Exclude Saturday</b>
				    			</div>
				    			<div class="col-auto" id="sat_chk" style="visibility: hidden;">
				    				<div class="form-group row">
						    			<div class="col-auto">
						    				<input type="checkbox" class="form-check-input" name="sat" value="1Y" >&nbsp;<b>1</b>
						    			</div>
						    			<div class="col-auto">
						    				<input type="checkbox" class="form-check-input" name="sat" value="2Y" >&nbsp;<b>2</b>
						    			</div>
						    			<div class="col-auto">
						    				<input type="checkbox" class="form-check-input" name="sat" value="3Y" >&nbsp;<b>3</b>
						    			</div>
						    			<div class="col-auto">
						    				<input type="checkbox" class="form-check-input" name="sat" value="4Y" >&nbsp;<b>4</b>
						    			</div>
						    			<div class="col-auto">
						    				<input type="checkbox" class="form-check-input" name="sat" value="5Y" >&nbsp;<b>5</b>
						    			</div>
						    		</div>
						    	</div>
				    		</div>
				    	</div>
					</div>
					<div class="row m-b-5">
				    	<div class="col-sm-2 col-xs-2 col-md-2">
					    	<div class="col">
			    				<input type="button" class="btn btn-sm config_btn" value="Holiday State Config" data-bs-toggle="modal" data-bs-target="#holidayState" >
			    			</div>
			    		</div>
		    			<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label" id="custDisplay" style="display:none;"></label>
				    				<input type="hidden" name="holidayState_map" value="<%=holiday_state %>">
				      			</div>
				  			</div>
						</div>
				    </div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Invoice Raised In<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="inv_currency">
							    		<option value="1"> INR </option>
							     		<%if(rate_unit.equals("2")){ %>
							     		<option value="2"> USD </option>
							     		<%} %>
							     	</select>
							     	<script>document.forms[0].inv_currency.value="<%=inv_currency%>"</script>
				    			</div>
				    		</div>
				    	</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Payment Done In<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="payment_currency">
							    		<option value="1"> INR </option>
							     		<%-- <%if(rate_unit.equals("2")){ %>
							     		<option value="2"> USD </option>
							     		<%} %> --%>
							     	</select>
							     	<script>document.forms[0].payment_currency.value="<%=payment_currency%>"</script>
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
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Interest Rate Calculation</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Interest Rate<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">
							<div class="form-group row">
				    			<div class="col">
				    				<select class="form-select form-select-sm" name="rate">
							    		<option value="">--Select--</option>
							    		<%for(int i=0;i<VINT_RATE_CD.size();i++){%>	
							    		<option value="<%=VINT_RATE_CD.elementAt(i)%>"><%=VINT_RATE_NM.elementAt(i)%></option>
								     	<%}%>
							    	</select>
							    	<script>document.forms[0].rate.value="<%=interest_rate_cd%>"</script>
				    			</div>
				    			<div class="col-auto">
				    				<select class="form-select form-select-sm" name="plusmin">
							     		<option value=""></option>
							     		<option value="+"> + </option>
							     		<option value="-"> - </option>
							     	</select>
							     	<script>document.forms[0].plusmin.value="<%=interest_cal_sign%>"</script>
				    			</div>
				    			<div class="col">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="modeper" value="<%=interest_cal_per%>" style='text-align:right;' onblur="negNumber(this),checkNumber1(this,4,2);">
			      						<span class="input-group-text">(%)</span>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
					</div>
					<div <%if(rate_unit.equals("1")){%>style="display:none"<%}%>>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Exchange Rate Calculation</label>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">
								<label class="form-label"><b>Exchange Rate<span class="s-red">*</span></b></label>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<select class="form-select form-select-sm" name="exchng_rate">
								    		<option value="">--Select--</option>
								    		<%for(int i=0;i<VEXCHNG_RATE_CD.size();i++){%>	
								    		<option value="<%=VEXCHNG_RATE_CD.elementAt(i)%>"><%=VEXCHNG_RATE_NM.elementAt(i)%></option>
									     	<%}%>
								    	</select>
								    	<script>document.forms[0].exchng_rate.value="<%=exchng_rate_cd%>"</script>
								    </div>
								</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">
								<label class="form-label"><b>Exchange Rate Calculation<span class="s-red">*</span></b></label>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">
								<div class="form-group row">
					    			<div class="col">
					    				<select class="form-select form-select-sm" name="exch_calc_base" onchange="showExchngCriteria(this);">
								    		<option value="D">On Particular Day Base</option>
								     		<option value="A">On Daily Basis of Billing Period</option>
								     	</select>
								     	<script>document.forms[0].exch_calc_base.value="<%=exchng_cal%>"</script>
					    			</div>
									<div class="col">
					    				<select class="form-select form-select-sm" name="inv_criteria" id="exchngCri" <%if(!exchng_cal.equals("D")){ %>style="display:none;"<%} %>>
								    		<option value="">--Select--</option>
								     		<option value="INV">Date of Invoice</option>
								     		<option value="LST">Last Day of Billing Cycle</option>
								     		<option value="PRE">Previous Day of Billing Cycle</option>
								     	</select> 
								     	<script>document.forms[0].inv_criteria.value="<%=exchng_criteria%>"</script>
					    			</div>
					    		</div>
					    	</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">
								<label class="form-label"><b>Exchange Rate Consideration On</b></label>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<input type="text" class="form-control form-control-sm" name="exchg_rate_note" value="<%=exchng_note%>" size="80" maxlength="200">
					    			</div>
					    		</div>
					    	</div>
						</div>
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
					    	Applicable Taxes
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Business Unit</th>
										<th>Plant</th>
										<th>Invoice Category</th>
										<th>Invoice Type</th>
										<th>Tax Structure Details</th>
										<th>Tax SAP Code</th>
									</tr>
								</thead>
								<tbody>
								<%if(VPLANT_NAME.size()>0){ %>
									<%for(int i=0; i<VPLANT_NAME.size(); i++){ %>
										<tr style="background:#99ffcc;">
											<td align="center"><%=VBU_PLANT_NM.elementAt(i)%></td>
											<td align="center"><%=VPLANT_NAME.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_CATEGORY.elementAt(i)%></td>	<!-- Pratham Bhatt: for invoice Category -->
											<td align="center"><%=VINVOICE_TYPE.elementAt(i)%></td>	<!-- Pratham Bhatt: for invoice Type  -->
											<td>[<%=VTAX_STRUCT_CD.elementAt(i)%>]- <%=VTAX_STRUCT_NM.elementAt(i)%></td>
											<td align="center"><%=VTAX_SAP_CODE.elementAt(i)%></td>
										</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="6" align="center"><%=utilmsg.infoMessage("<b>No Taxes Configured!</b>") %></td>
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

<div class="modal fade" id="holidayState" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Holiday State
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th></th>
										<th>Plant</th>
										<th>State</th>
									</tr>
								</thead>
								<tbody>							
								<%if(VSELECTED_PLANT_SEQ.size() > 0) {
									int k =0;%>
							    	<%for(int i=0; i<VSELECTED_PLANT_SEQ.size(); i++){ 
							    		k++;%>	
							    		<tr>
			      							<td align="center">	
				      							<input type="checkbox" class="form-check-input" name="chk_plant" value="<%=VSELECTED_PLANT_SEQ.elementAt(i)%>">
				      							<input type="hidden" class="form-check-input" name="chk_plant_contpty" value="<%=VSELECTED_PLANT_COUNTPTY_CD.elementAt(i)%>">
				      						</td>
				      						<td align="center">
				      							<%=VSELECTED_PLANT_ABBR.elementAt(i)%>
				      							<input type="hidden" name="chk_plant_abbr" value="<%=VSELECTED_PLANT_ABBR.elementAt(i)%>">
				      						</td>
											<td align="center" >
												<div class="col-sm-6 col-xs-6 col-md-6"> 
													<select class="form-select form-select-sm"  name="holiday_state" id="holiday_state_<%=i %>" multiple="multiple">
										 				<% for(int j=0; j<VSTATE_CODE.size(); j++){ %>
							 							<option id="<%=VSTATE_CODE.elementAt(j) %>_<%=i %>_<%=j %>" value="<%=VSTATE_CODE.elementAt(j)%>"><%=VSTATE_NM.elementAt(j)%></option>
							 							<%} %>
										 			</select>
									 			</div>
									 			<span id="bl_inf"><font color="red" size="2">Press ctrl/shift for multiple selection!</font></span>
					    					</td>
								    	</tr>	
				    				<%}%>
			    				<%}else{ %>
				    				<tr>
			    						<td colspan="3" align="center"><%= utilmsg.warningMessage("Please configure Plants for Customer!")%></td>
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
					<input type="button" class="btn btn-warning com-btn" value="Proceed" onclick="doSubmitCustConfig();">
				</div>
      		</div>
      	</div>
	</div>
</div>

<input type="hidden" name="option" value="TRADER_BILLING_DTL">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>">  	
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="start_dt" value="<%=start_dt%>">
<input type="hidden" name="rate_unit" value="<%=rate_unit%>">
<input type="hidden" name="disp_holiday_state" value="<%=disp_holiday_state%>">
<input type="hidden" name="split_flag" value="<%=split_flag%>">

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