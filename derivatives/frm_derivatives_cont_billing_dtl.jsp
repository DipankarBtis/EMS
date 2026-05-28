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
	var rate = document.forms[0].rate.value;
	
	var rate_unit = document.forms[0].rate_unit.value;
	var u = document.forms[0].u.value;
	
	var plusmin = document.forms[0].plusmin.value;
	var modeper = document.forms[0].modeper.value;
	
	var exclude_sat = document.forms[0].exclude_sat;
	var satCheckboxes = document.getElementsByName('sat');
	
	var msg="";
	var flag=true;
	
	if(trim(freq) == "")
	{
		msg+="Select Billing/Payment Freq!\n";
		flag=false;
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
	if(exclude_sat.checked)
	{
		var isChecked = false;
		for (var i = 0; i < satCheckboxes.length; i++) 
		{
			if (satCheckboxes[i].checked) 
			{
				isChecked = true;
		        break;
			}
		}
		if (!isChecked) 
		{
			msg+="Select Atleast one Saturday!\n";
			flag=false;
		}
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
	if(trim(plusmin) == "")
	{
		msg+="Select Interest Rate sign!\n";
		flag=false;
	}
	if(trim(modeper) == "")
	{
		msg+="Enter Interest Rate Percentage!\n";
		flag=false;
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

function setPlantState(strPlant,holiday_state_map)
{
	var chk_plant = document.forms[0].chk_plant;
	var holiday_state = document.forms[0].holiday_state;
	var plant_seq = strPlant.split("@");
	
	var split_state_map = holiday_state_map.split("@@");   2//01@02@03;   3//01@02@03
	var stateCd;
	var plantCd;
	var stateMap;
	for(var k=0; k<split_state_map.length; k++)
	{
		plantCd = split_state_map[k].split("//")[0];
		stateMap = split_state_map[k].split("//")[1];
		
		if(chk_plant!=null && chk_plant.length!=undefined)
	 	{
	  		for(var i=0;i<chk_plant.length;i++)
	  		{
	   			for(var j=0;j<plant_seq.length;j++)
	   			{
	     			if(chk_plant[i].value == plant_seq[j])
	     			{
	     				chk_plant[i].checked = true;
	     				
	     				if(plantCd===plant_seq[j])
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
   					map+="@@"+chk_plant[i].value;
   				}
   				else
   				{
   					display+=chk_plant_abbr[i].value;
   					map+=chk_plant[i].value;
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
				map+="@@"+chk_plant.value;
			}
			else
			{
				display+=chk_plant_abbr.value;
				map+=chk_plant.value;
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

</script>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DataBean_Derivarives_mst" id="db_derivative" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String clearance=request.getParameter("clearance")==null?"":request.getParameter("clearance");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"V":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String rate_unit=request.getParameter("rate_unit")==null?"2":request.getParameter("rate_unit");
String agmt_type="U";

db_derivative.setCallFlag("HEDGE_CONT_BILLING_DTL");
db_derivative.setComp_cd(owner_cd);
db_derivative.setCont_start_dt(start_dt);
db_derivative.setCounterparty_cd(counterparty_cd);
db_derivative.setAgmt_no(agmt_no);
db_derivative.setAgmt_rev_no(agmt_rev_no);
db_derivative.setCont_no(cont_no);
db_derivative.setCont_rev_no(cont_rev_no);
db_derivative.setContract_type(contract_type);
db_derivative.setAgmt_type(agmt_type);
db_derivative.init();

Vector VINT_RATE_CD = db_derivative.getVINT_RATE_CD();
Vector VINT_RATE_NM = db_derivative.getVINT_RATE_NM();

Vector VPLANT_NAME = db_derivative.getVPLANT_NAME();
Vector VTAX_STRUCT_CD = db_derivative.getVTAX_STRUCT_CD();
Vector VTAX_STRUCT_NM = db_derivative.getVTAX_STRUCT_NM();
Vector VTAX_SAP_CODE = db_derivative.getVTAX_SAP_CODE();
Vector VBU_PLANT_NM = db_derivative.getVBU_PLANT_NM();

Vector VSELECTED_PLANT_SEQ = db_derivative.getVSELECTED_PLANT_SEQ();
Vector VSELECTED_PLANT_ABBR = db_derivative.getVSELECTED_PLANT_ABBR();
Vector VSTATE_NM = db_derivative.getVSTATE_NM();
Vector VSTATE_CODE = db_derivative.getVSTATE_CODE();
Vector VPLANT_SEQ = db_derivative.getVPLANT_SEQ();

String billing_freq=db_derivative.getBilling_freq();
String billing_flag=db_derivative.getBilling_flag();
String due_date=db_derivative.getDue_date();
String sec_due_date=db_derivative.getSec_due_date();
String inv_currency=db_derivative.getInv_currency();
String payment_currency=db_derivative.getPayment_currency();
String interest_rate_cd=db_derivative.getInterest_rate_cd();
String interest_cal_sign=db_derivative.getInterest_cal_sign();
String interest_cal_per=db_derivative.getInterest_cal_per();
String display_map_id=db_derivative.getDisplay_map_id();
String counterparty_abbr=db_derivative.getCounterparty_abbr();
String sat_days = db_derivative.getSat_days();
String holiday_state = db_derivative.getHoliday_state();
String plant_seq = db_derivative.getPlant_seq();
String disp_holiday_state = db_derivative.getDisp_holiday_state();
String no_of_billing_dtl=db_derivative.getNo_of_billing_dtl();

String exchng_note=db_derivative.getExchng_note();
String due_dt_in=db_derivative.getDue_dt_in();
String exclude_sat=db_derivative.getExclude_sat();
if(due_dt_in.equals(""))
{
	due_dt_in="C";
}
if(payment_currency.equals(""))
{
	payment_currency="2";
}
if(inv_currency.equals(""))
{
	inv_currency="2";
}
if(billing_freq.equals(""))
{
	billing_freq="S";
}

String strPlant="";
for(int i=0;i<VPLANT_SEQ.size();i++)
{
	strPlant = strPlant + VPLANT_SEQ.elementAt(i)+"@";
}
%>
<body onload="enablechk();setValues('<%=sat_days%>');showDispState();setPlantState('<%=strPlant%>','<%=holiday_state%>');">
<form method="post" action="../servlet/Frm_DerivativesMaster">
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
			<%if(Integer.parseInt(no_of_billing_dtl)<Integer.parseInt(""+VSELECTED_PLANT_SEQ.size())){ %>
				<div class="row m-b-5">
					<div class="col-sm-12 col-xs-12 col-md-12">
						<%=utilmsg.errorMessage("<b>Billing Details Not configure for all Plants. Invoice will not appear for Plant/s missing billing detail!</b>")%>
					</div>
				</div>
			<%} %>
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
				    				<select class="form-select form-select-sm" name="billing_flag" style="pointer-events: none;">
							     		<option value="S"> [S] On Settlement </option>
							     	</select>
							     	<%-- <script>document.forms[0].billing_flag.value="<%=billing_flag%>"</script> --%>
				    			</div>
				    		</div>
				    	</div>
					
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Billing Period<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
			    				<select class="form-select form-select-sm" name="freq" >
			    					<option value="S" selected="selected">On Settlement</option>
			    				</select>
			    				<script>document.forms[0].freq.value="<%=billing_freq%>"</script>
				    		</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Payment Due Date Consideration</label>
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
					<div>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Invoice Currency</label>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">
								<label class="form-label"><b>Invoice Raised In<span class="s-red">*</span></b></label>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<select class="form-select form-select-sm" name="inv_currency">
								    		<!-- <option value="0">--Select--</option> -->
								     		<!-- <option value="1">INR</option> -->
								     		<option value="2">USD</option>
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
								    		<!-- <option value="0">--Select--</option> -->
								     		<!-- <option value="1">INR</option> -->
								     		<option value="2">USD</option>
								     	</select>
								     	<script>document.forms[0].payment_currency.value="<%=payment_currency%>"</script>
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

<input type="hidden" name="option" value="HEDGE_CONT_BILLING_DTL">
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