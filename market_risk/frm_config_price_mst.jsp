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
	var msg = "Please Check the Following Field(s):\n\n";
	var flag = true;
	
	var i = 0;
	var index = 0;
	
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var price_type = document.forms[0].price_type.value;
	var currency = document.forms[0].currency.value;
	var slope = document.forms[0].slope.value;
	var constant = document.forms[0].constant.value;
	var physical_curve = document.forms[0].physical_curve.value;		
	var rate = document.forms[0].rate.value;
	var seq_no = document.forms[0].seq_no.value;
	var price_deci = document.forms[0].price_deci.value;
	var prem_disc_rate = document.forms[0].prem_disc_rate.value;
	var final_price_deci = document.forms[0].final_price_deci.value
	
	var line_start_date = document.forms[0].line_start_date;
	var line_end_date = document.forms[0].line_end_date;
	var line_seq_no = document.forms[0].line_seq_no;
	
	var price_range = document.forms[0].price_range.value;
	
	var mapping_id = document.forms[0].mapping_id.value;
	
	if(trim(mapping_id)=='')
	{
		msg += "Contract Mapping id is Missing!\n";
		flag = false;
	}
	if(trim(from_dt)=='')
	{
		msg += "Please Enter From Date!\n";
		flag = false;
	}
	if(trim(to_dt)=='')
	{
		msg += "Please Enter To Date!\n";
		flag = false;
	}
	if(trim(price_type)=="")
	{
		msg += "Please Select Pricing Type!\n";
		flag = false;
	}
	if(physical_curve==null || physical_curve=='' || physical_curve== ' ' || physical_curve=='0')
	{
		msg += "Please Select Physical Curve !!!\n";
		flag = false;
	}
	
	if(price_type=='M')
	{
		var opal_min = document.forms[0].opal_min.value;
		var multi_type = document.forms[0].multi_type.value;
		var formula = document.forms[0].formula.value;
		var formula1 = document.forms[0].formula1.value;
		var curve_nm = document.forms[0].curve_nm.value;
		
		var curve = document.getElementById('curve_nm').value;
		
		if(trim(opal_min)=="")
		{
			msg += "Please Select Curve Logic!\n";
			flag = false;
		}
		
		if(opal_min == "MIN" || opal_min == "MAX" || opal_min == "AVG" || opal_min == "MIN_ADV")
		{
			var count=parseInt("0");
			var ppac_count=parseInt("0");
			var oth_count=parseInt("0");
			for (var option of document.getElementById('curve_nm').options)
		    {
		        if(option.selected && option.value!="") 
		        {
		        	count+=1;
		        	
		        	var curveName=option.value;
		        	
		        	var MIN_slope = document.getElementById(curveName+"_slope").value;
					var MIN_constant = document.getElementById(curveName+"_constant").value;
					var MIN_price_range = document.getElementById(curveName+"_price_range").value;
					
					var MIN_formula = document.getElementById(curveName+"_formula").value;
					var MIN_formula1 = document.getElementById(curveName+"_formula1").value;
					var MIN_multi_type = document.getElementById(curveName+"_multi_type").value;
					
					var MIN_price_deci = document.getElementById(curveName+"_price_deci").value;
					var MIN_prem_disc_rate = document.getElementById(curveName+"_prem_disc_rate").value;
					
					if(opal_min == "MIN_ADV" && curveName=="PPAC")
					{
						ppac_count+=1;
					}
					
					if(opal_min == "MIN_ADV" && curveName!="PPAC")
					{
						oth_count+=1;
						
						if(MIN_multi_type == "" || MIN_multi_type == " ")
						{
							msg += "Please Select Settled(M-)/Forward(M+) Multi-leg for "+curveName+"!\n";
							flag = false;
						}
						if(MIN_formula == "" || MIN_formula == " ")
						{
							msg += "Please Select Leg 1 for "+curveName+"!\n";
							flag = false;
						}
						if(MIN_formula1 == "" || MIN_formula1 == " ")
						{
							msg += "Please Select Leg 2 for "+curveName+"!\n";
							flag = false;
						}
						if(trim(MIN_formula)!="" && trim(MIN_formula1)!="" )
						{
							if(parseInt(MIN_formula) > parseInt(MIN_formula1))
							{
								msg += "Leg1 should be <= Leg 2 for "+curveName+"!\n";
								flag = false;
							}
						}
					} 
					
					if(trim(MIN_slope)=='')
					{
						msg += "Please Enter Slope for "+curveName+"!\n";
						flag = false;
					}
					if(trim(MIN_constant)=='')
					{
						msg += "Please Enter Constant for "+curveName+"!\n";
						flag = false;
					}
					if(trim(MIN_price_range)=="")
					{
						msg += "Please Select Settle Price/Mth Logic for "+curveName+"!\n";
						flag = false;
					}
					
					if(MIN_price_range == "O")
					{
						var numDay=document.getElementById(curveName+"_days").value;
						if(trim(numDay)=="")
						{
							msg += "Please enter # Days for "+curveName+"!\n";
							flag = false;
						}
					}
					else if(MIN_price_range=="D")
					{
						var price_start_dt =document.getElementById(curveName+"_price_start_dt").value;
						var price_end_dt =document.getElementById(curveName+"_price_end_dt").value;
						
						if(price_start_dt != "" && price_end_dt != "")
						{
							var value_0 = compareDate(price_start_dt,price_end_dt);
							
							if(value_0 == "1") //comparing from and to date
							{
								msg += "Please Correct Day Range!\nFrom date "+price_start_dt+" > To date "+price_end_dt+" not Allowed for "+curveName+"!\n";
								flag = false;
							}
						}
						else if(trim(price_start_dt)=="" || trim(price_end_dt)=="")
						{
							msg += "Please specify Day Range for "+curveName+"!\n";
							flag = false;
						}
					}
					if(trim(MIN_price_deci)=='')
					{
						msg += "Please Select Price Upto(Decimal) for "+curveName+"!\n";
						flag = false;
					}
					if(trim(MIN_prem_disc_rate)=='')
					{
						msg += "Please Enter Premium(+)/Discount(-) $/MMbtu for "+curveName+"!\n";
						flag = false;
					}
		        }	
		    }
			if(count <= 1)
			{
				msg += "Please Select atlest two Financial Curve!\n";
				flag = false;
			}
			
			if(opal_min == "MIN_ADV" && ppac_count == 0)
			{
				msg += "PPAC Financial Curve is mandetory for MIN ADV Logic!\n";
				flag = false;
			}
			
			if(opal_min == "MIN_ADV" && ppac_count == 1 && oth_count > 1)
			{
				msg += "Please Select only ONE(1) Financial Curve along with PPAC!\n";
				flag = false;
			}
			if(final_price_deci=='')
			{
				msg+= "Please Select Final Price Upto(Decimal)!\n";
				flag = false;
			}
		}
		else
		{
			if(curve == "")
			{
				msg += "Please Select Financial Curve!\n";
				flag = false;
			}
			if(trim(slope)=='')
			{
				msg += "Please Enter Slope!\n";
				flag = false;
			}
			if(trim(constant)=='')
			{
				msg += "Please Enter Constant!\n";
				flag = false;
			}
			if(price_deci=='')
			{
				msg+= "Please Select Price Upto(Decimal)!\n";
				flag = false;
			}
			if(prem_disc_rate=='')
			{
				msg+= "Please Enter Premium(+)/Discount(-) $/MMbtu!\n";
				flag = false;
			}
			if(final_price_deci=='')
			{
				msg+= "Please Select Final Price Upto(Decimal)!\n";
				flag = false;
			}
			
			if(opal_min == "MULTI_LEG")
			{
				if(multi_type == "" || multi_type == " ")
				{
					msg += "Please Select Settled(M-)/Forward(M+) Multi-leg!\n";
					flag = false;
				}
				if(formula == "" || formula == " ")
				{
					msg += "Please Select Leg 1!\n";
					flag = false;
				}
				if(formula1 == "" || formula1 == " ")
				{
					msg += "Please Select Leg 2!\n";
					flag = false;
				}
				if(trim(formula)!="" && trim(formula1)!="" )
				{
					if(parseInt(formula) > parseInt(formula1))
					{
						msg += "Leg1 should be <= Leg 2!\n";
						flag = false;
					}
				}
			} 
			
			if(trim(price_range)=="")
			{
				msg += "Please Select Settle Price/Mth Logic!\n";
				flag = false;
			}
			
			if(price_range == "O")
			{
				var numDay=document.forms[0].days.value;
				if(trim(numDay)=="")
				{
					msg += "Please enter # Days!\n";
					flag = false;
				}
			}
			else if(price_range=="D")
			{
				var price_start_dt =document.forms[0].price_start_dt.value;
				var price_end_dt =document.forms[0].price_end_dt.value;
				
				if(price_start_dt != "" && price_end_dt != "")
				{
					var value_0 = compareDate(price_start_dt,price_end_dt);
					
					if(value_0 == "1") //comparing from and to date
					{
						msg += "Please Correct Day Range!\nFrom date "+price_start_dt+" > To date "+price_end_dt+" not Allowed!!\n";
						flag = false;
					}
				}
				else if(trim(price_start_dt)=="" || trim(price_end_dt)=="")
				{
					msg += "Please specify Day Range!\n";
					flag = false;
				}
			}
		}
	}
	else
	{
		if(trim(rate)=="")
		{
			msg += "Please Enter Fixed Price!\n";
			flag = false;
		}
	}
	
	var dateOverWrite_count=parseInt("0");
	if(line_start_date!=null && line_start_date!=undefined)
	{
		if(line_start_date.length!=undefined)
		{
			for(var i=0;i<line_start_date.length;i++)
			{
				if(line_seq_no[i].value!=seq_no)
				{
					var value_1 = compareDate(line_start_date[i].value,to_dt);
					var value_11 = compareDate(line_end_date[i].value,from_dt);
					
					if(value_1 != '1' && value_11 != '2')
					{
						dateOverWrite_count++;
					}
				}
			}
		}
		else
		{
			if(line_seq_no.value!=seq_no)
			{
				var value_1 = compareDate(line_start_date.value,to_dt);
				var value_11 = compareDate(line_end_date.value,from_dt);
				
				if(value_1 != '1' && value_11 != '2')
				{
					dateOverWrite_count++;
				}
			}
		}
	}
	
	if(parseInt(dateOverWrite_count) > 0)
	{
		msg += "Pricing Line Duration already covered, Please check date range!\n";
		flag = false;
	}
	
	if(flag)
	{
		var a = confirm("Do You Want To Submit Price Specification Details For Selected Deal?");
		
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


function doModify(seq_no, from_dt, to_dt, price_type, slope, constant, remark, physical_curve, currency, rate,
		curve_nm, price_range, price_start_dt, price_end_dt, curve_logic, formula_desc, MIN_price_dt, MIN_formula_desc, counterparty_abbr, counterparty_cd, contract_no,cont_ref,cont_status,
		price_deci,final_price_deci,prem_disc_rate)
{ 	
	document.forms[0].from_dt.value=from_dt;
	document.forms[0].to_dt.value=to_dt;
	document.forms[0].temp_from_dt.value=from_dt;
	document.forms[0].temp_to_dt.value=to_dt;
	
	document.forms[0].price_type.value=price_type;
	document.forms[0].seq_no.value=seq_no;
	document.forms[0].physical_curve.value=physical_curve;
	document.forms[0].remark.value=remark;
	document.forms[0].currency.value=currency;
	document.forms[0].rate.value=rate;
	
	document.forms[0].opal_min.value=curve_logic;
	document.forms[0].formula_desc.value=formula_desc;
	
	
	if(price_type=="F")
	{
		document.getElementById('tr1').style.display="none";
		document.getElementById('tr2').style.display="none";
		document.getElementById('tr3').style.display="none";
		document.getElementById('tr4').style.display=""; 
		document.forms[0].opal_min.value="";
		document.getElementById('op1').style.display="none";
		document.getElementById('op2').style.display="none";
		document.getElementById('op3').style.display="none";
		document.getElementById('op4').style.display="none";
		document.getElementById('curve_nm').multiple=false;
		document.getElementById('inf').style.display="none";
		
		var ele = document.getElementsByClassName("om");
		 for (var i = 0; i < ele.length; i++){
		        ele[i].style.display = "none";
		    }
		
		document.forms[0].remark.value=remark;
		//document.forms[0].ppac_price.value=PPAC_price; JD- WILL CHECK THIS LATER ON
		//document.forms[0].unit_price.value=UnitPrice; JD- WILL CHECK THIS LATER ON
	}
	else if(price_type=="M")
	{
		if(curve_logic == "MULTI_LEG")
		{
			document.getElementById('tr1').style.display="";
			document.getElementById('tr2').style.display="";
			document.getElementById('tr3').style.display="";
			document.getElementById('tr4').style.display="none";
			
			var ele = document.getElementsByClassName("om");
			 for (var i = 0; i < ele.length; i++){
			        ele[i].style.display = "none";
			    }
			 
			document.getElementById('o1').disabled=false;
			document.getElementById('curve_nm').multiple=false;
			document.getElementById('inf').style.display="none";
			document.getElementById('op1').style.display="inline";
			document.getElementById('op2').style.display="inline";
			document.getElementById('op3').style.display="inline";
			document.getElementById('op4').style.display="inline";
			
			document.getElementById('tr5').style.display="";
			document.getElementById('price_deci_h1').style.display="";
			document.getElementById('price_deci_h2').style.display="";
			document.getElementById('price_disc_h1').style.display="";
			document.getElementById('price_disc_h2').style.display="";
			document.getElementById('final_price_deci_h1').style.display="";
			document.getElementById('final_price_deci_h2').style.display="";
			
			if(formula_desc!="")
			{
				var split = formula_desc.split("@");
				if(split.length==4)
				{
					document.forms[0].multi_type.value=split[1];
					document.forms[0].formula.value=split[2];
					document.forms[0].formula1.value=split[3];
				}
				else if(split.length==3)
				{
					document.forms[0].formula.value=split[1];
					document.forms[0].formula1.value=split[2];
				}
				else if(split.length==2)
				{
					document.forms[0].formula.value=split[1];
				}
			}
			
			document.forms[0].curve_nm.value=curve_nm;
			document.forms[0].slope.value=slope;
			document.forms[0].constant.value=constant;
			
			document.forms[0].price_deci.value=price_deci;
			document.forms[0].final_price_deci.value=final_price_deci;
			document.forms[0].prem_disc_rate.value=prem_disc_rate;
			
			document.forms[0].price_range.value=price_range;
			
			//if(price_range.length>1)
			if(price_range.length>1 && price_range.startsWith("O"))
			{
				document.forms[0].price_range.value="O";
				
				var othAvg = price_range.substring(1,price_range.length)
				
				document.getElementById('days').style.display="inline";
				document.getElementById('days1').style.display="inline";
				document.forms[0].days.disabled=false;
				document.forms[0].days.value=othAvg;
				
				document.forms[0].price_start_dt.style.display="none";
				document.forms[0].price_start_dt.disabled=true;
				document.forms[0].price_end_dt.style.display="none";
				document.forms[0].price_end_dt.disabled=true;
				//document.getElementById('avg1').style.display="none";
				document.getElementById('avg2').style.display="none";
				document.getElementById('avg3').style.display="none";
				//document.getElementById('avg4').style.display="none";
				//document.getElementById('avg5').style.display="none";
			}
			else if(price_range=="D")//if(price_start_dt != "" && price_end_dt != "")
			{
				document.forms[0].price_start_dt.style.display="inline";
				document.forms[0].price_start_dt.disabled=false;
				document.forms[0].price_end_dt.style.display="inline";
				document.forms[0].price_end_dt.disabled=false;
				
				//document.getElementById('avg1').style.display="inline";
				document.getElementById('avg2').style.display="inline";
				document.getElementById('avg3').style.display="inline";
				//document.getElementById('avg4').style.display="inline";
				//document.getElementById('avg5').style.display="inline";
				
				document.getElementById('days').style.display="none";
				document.getElementById('days1').style.display="none";
				document.forms[0].days.disabled=true;
				
				document.forms[0].price_start_dt.value=price_start_dt; 
				document.forms[0].price_end_dt.value=price_end_dt; 
			}
			else
			{
				document.getElementById('days').style.display="none";
				document.getElementById('days1').style.display="none";
				document.forms[0].days.disabled=true;
				
				document.forms[0].price_start_dt.style.display="none";
				document.forms[0].price_start_dt.disabled=true;
				document.forms[0].price_end_dt.style.display="none";
				document.forms[0].price_end_dt.disabled=true;
				//document.getElementById('avg1').style.display="none";
				document.getElementById('avg2').style.display="none";
				document.getElementById('avg3').style.display="none";
				//document.getElementById('avg4').style.display="none";
				//document.getElementById('avg5').style.display="none";
			}
		}
		else if(curve_logic == "MIN" || curve_logic == "MAX" || curve_logic == "AVG" || curve_logic == "MIN_ADV")
		{
			document.getElementById('tr1').style.display="";
			document.getElementById('tr2').style.display="";
			document.getElementById('tr3').style.display="none";
			document.getElementById('tr4').style.display="none";
			document.getElementById('tr5').style.display="none";
			
			var ele = document.getElementsByClassName("om");
			for (var i = 0; i < ele.length; i++)
			{
				ele[i].style.display = "none";
			}

			document.getElementById('o1').disabled=true;
			document.getElementById('o1').selected=false;
			document.getElementById('curve_nm').multiple=true;
			document.getElementById('inf').style.display="inline";
			
			document.getElementById('op1').style.display="none";
			document.getElementById('op2').style.display="none";
			document.getElementById('op3').style.display="none";
			document.getElementById('op4').style.display="none";
			
			document.getElementById('final_price_deci_h1').style.display="";
			document.getElementById('final_price_deci_h2').style.display="";
			
			document.forms[0].curve_nm.value="";
			if(curve_nm!="")
			{
				var cu_split = curve_nm.split("<br>");
				var slope_split = slope.split("<br>");
				var con_split = constant.split("<br>");
				var price_range_split = price_range.split("<br>");
				var price_date_split = MIN_price_dt.split("<br>");
				var formula_desc_split = MIN_formula_desc.split("<br>");
				var price_deci_split = price_deci.split("<br>");
				var prem_disc_rate_split = prem_disc_rate.split("<br>");
				
				document.forms[0].final_price_deci.value=final_price_deci;
				
				for(var i=0; i<cu_split.length; i++)
				{
					for (var j=0; j< document.forms[0].curve_nm.length; j++)
				   	{		
				        if (document.forms[0].curve_nm.options[j].value == cu_split[i]) 
				        {
				        	document.forms[0].curve_nm.options[j].selected=true;
				        	
				        	var price_ind = j;
				        		    
						    var curve=cu_split[i];
						    var slop = slope_split[i];
						    var con = con_split[i];
						    var priceRange = price_range_split[i]; 
						    var priceDate = price_date_split[i];
						    var priceDeci = price_deci_split[i];
						    var premDeciRateSplit = prem_disc_rate_split[i];
						    
				        	document.getElementById(curve).style.display="";
			        		document.getElementById(curve+"_slope").disabled=false;
			        		document.getElementById(curve+"_constant").disabled=false;
			        		document.getElementById(curve+"_price_deci").disabled=false;
			        		document.getElementById(curve+"_prem_disc_rate").disabled=false;
			        		document.getElementById(curve+"_slope").value=slop;
			        		document.getElementById(curve+"_constant").value=con;
			        		document.getElementById(curve+"_price_range").value=priceRange;
			        		
			        		
			        		document.getElementById(curve+"_formula_desc").disabled=false;
			        		
			        		document.getElementById(curve+"_price_deci").value=priceDeci;
			        		document.getElementById(curve+"_prem_disc_rate").value=premDeciRateSplit;
			        		
			        		if(curve_logic=='MIN_ADV' && curve!="PPAC")
			        		{
			        			document.getElementById('MIN_4_'+curve).style.display="inline";
			        			document.getElementById('MIN_5_'+curve).style.display="inline";
			        			document.getElementById('MIN_6_'+curve).style.display="inline";
			        			document.getElementById('MIN_7_'+curve).style.display="inline";
			        			
			        			var temp_f = document.getElementById(curve+"_formula").value;
			        			var temp_f1 = document.getElementById(curve+"_formula1").value;
			        			var temp_mutityp = document.getElementById(curve+"_multi_type").value;
			        			
			        			if(trim(formula_desc_split[i])!="")
			        			{
			        				var split = formula_desc_split[i].split("@");
			        				if(split.length==4)
			        				{
			        					document.getElementById(curve+"_multi_type").value=split[1];
			        					document.getElementById(curve+"_formula").value=split[2];
			        					document.getElementById(curve+"_formula1").value=split[3];
			        				}
			        				else if(split.length==3)
			        				{
			        					document.getElementById(curve+"_formula").value=split[1];
			        					document.getElementById(curve+"_formula1").value=split[2];
			        				}
			        				else if(split.length==2)
			        				{
			        					document.getElementById(curve+"_formula")=split[1];
			        				}
			        			}
			        			
			        			document.getElementById(curve+"_formula_desc").value=formula_desc_split[i];
			        		}
			        		else
			        		{
			        			document.getElementById('MIN_4_'+curve).style.display="none";
			        			document.getElementById('MIN_5_'+curve).style.display="none";
			        			document.getElementById('MIN_6_'+curve).style.display="none";
			        			document.getElementById('MIN_7_'+curve).style.display="none";
			        			
			        			document.getElementById(curve+"_formula_desc").value="";
			        		}
			        		
			        		if(priceRange != undefined && priceRange !="" && priceRange !=" ")
			        		{
				        		//if(priceRange.length>1)
				        		if(priceRange.length>1 && priceRange.startsWith("O"))
				        		{
				        			var othAvg = priceRange.substring(1,priceRange.length)
				        			document.getElementById(curve+"_price_range").value="O";
				        			document.getElementById(curve+"_price_range").disabled=false;
					        		document.getElementById(curve+"_days").value=othAvg;
					        		document.getElementById(curve+"_days").disabled=false;
					        		
					        		document.getElementById(curve+"_price_start_dt").disabled=false;
					        		document.getElementById(curve+"_price_end_dt").disabled=false;
				        			
					        		document.getElementById('MIN_0_'+price_ind).style.display="inline";
				        			document.getElementById('MIN_1_'+price_ind).style.display="inline";
				        			document.getElementById('MIN_2_'+price_ind).style.display="none";
				        			document.getElementById('MIN_3_'+price_ind).style.display="none";
				        			
				        		}
				        		else if(priceRange=="D") //(priceDate!="" && priceDate!="-")
				        		{
				        			var dtsplit = priceDate.split("-");
				        			
				        			document.getElementById(curve+'_price_start_dt').value=dtsplit[0];
				        			document.getElementById(curve+'_price_end_dt').value=dtsplit[1];
				        			
				        			document.getElementById(curve+"_price_range").disabled=false;
					        		document.getElementById(curve+"_price_start_dt").disabled=false;
					        		document.getElementById(curve+"_price_end_dt").disabled=false;
					        		document.getElementById(curve+"_days").disabled=false;
					        		
					        		document.getElementById('MIN_0_'+price_ind).style.display="none";
				        			document.getElementById('MIN_1_'+price_ind).style.display="none";
				        			document.getElementById('MIN_2_'+price_ind).style.display="inline";
				        			document.getElementById('MIN_3_'+price_ind).style.display="inline";
					        		
				        		}
				        		else
				        		{
				        			document.getElementById(curve+"_price_range").disabled=false;
					        		document.getElementById(curve+"_price_start_dt").disabled=false;
					        		document.getElementById(curve+"_price_end_dt").disabled=false;
					        		document.getElementById(curve+"_days").disabled=false;
					        		
					        		document.getElementById('MIN_0_'+price_ind).style.display="none";
				        			document.getElementById('MIN_1_'+price_ind).style.display="none";
				        			document.getElementById('MIN_2_'+price_ind).style.display="none";
				        			document.getElementById('MIN_3_'+price_ind).style.display="none";
					        		
				        		}
			        		}
			        		else
			        		{
			        			document.getElementById(curve+"_price_range").value="";
			        			document.getElementById(curve+"_price_range").disabled=false;
				        		document.getElementById(curve+"_price_start_dt").disabled=false;
				        		document.getElementById(curve+"_price_end_dt").disabled=false;
				        		document.getElementById(curve+"_days").disabled=false;
				        		
				        		document.getElementById('MIN_0_'+price_ind).style.display="none";
			        			document.getElementById('MIN_1_'+price_ind).style.display="none";
			        			document.getElementById('MIN_2_'+price_ind).style.display="none";
			        			document.getElementById('MIN_3_'+price_ind).style.display="none";
			        		}
			        		///////////////
				        }
				    } 
				}
			}
		}
		else
		{
			document.getElementById('tr1').style.display="";
			document.getElementById('tr2').style.display="";
			document.getElementById('tr3').style.display="";
			document.getElementById('tr4').style.display="none";
			document.forms[0].remark.readOnly=false;
			document.getElementById('curve_nm').multiple=false;
			document.getElementById('inf').style.display="none";
			document.getElementById('op1').style.display="none";
			document.getElementById('op2').style.display="none";
			document.getElementById('op3').style.display="none";
			document.getElementById('op4').style.display="none";
			
			document.getElementById('tr5').style.display="";
			document.getElementById('price_deci_h1').style.display="";
			document.getElementById('price_deci_h2').style.display="";
			document.getElementById('price_disc_h1').style.display="";
			document.getElementById('price_disc_h2').style.display="";
			document.getElementById('final_price_deci_h1').style.display="";
			document.getElementById('final_price_deci_h2').style.display="";
			
			var ele = document.getElementsByClassName("om");
			 for (var i = 0; i < ele.length; i++){
			        ele[i].style.display = "none";
			    }
		 
			document.forms[0].remark.value=remark;
			document.forms[0].curve_nm.value=curve_nm;
			document.forms[0].slope.value=slope;
			document.forms[0].constant.value=constant;
			document.forms[0].price_deci.value=price_deci;
			document.forms[0].final_price_deci.value=final_price_deci;
			document.forms[0].prem_disc_rate.value=prem_disc_rate;
			
			document.forms[0].price_range.value=price_range;
			
			//if(price_range.length>1)
			if(price_range.length>1 && price_range.startsWith("O"))
			{
				document.forms[0].price_range.value="O";
				
				var othAvg = price_range.substring(1,price_range.length)
				
				document.getElementById('days').style.display="inline";
				document.getElementById('days1').style.display="inline";
				document.forms[0].days.disabled=false;
				document.forms[0].days.value=othAvg;
				
				document.forms[0].price_start_dt.style.display="none";
				document.forms[0].price_start_dt.disabled=true;
				document.forms[0].price_end_dt.style.display="none";
				document.forms[0].price_end_dt.disabled=true;
				//document.getElementById('avg1').style.display="none";
				document.getElementById('avg2').style.display="none";
				document.getElementById('avg3').style.display="none";
				//document.getElementById('avg4').style.display="none";
				//document.getElementById('avg5').style.display="none";
			}
			else if(price_range=="D")//if(price_start_dt != "" && price_end_dt != "")
			{
				document.forms[0].price_start_dt.style.display="inline";
				document.forms[0].price_start_dt.disabled=false;
				document.forms[0].price_end_dt.style.display="inline";
				document.forms[0].price_end_dt.disabled=false;
				
				//document.getElementById('avg1').style.display="inline";
				document.getElementById('avg2').style.display="inline";
				document.getElementById('avg3').style.display="inline";
				//document.getElementById('avg4').style.display="inline";
				//document.getElementById('avg5').style.display="inline";
				
				document.getElementById('days').style.display="none";
				document.getElementById('days1').style.display="none";
				document.forms[0].days.disabled=true;
				
				document.forms[0].price_start_dt.value=price_start_dt; 
				document.forms[0].price_end_dt.value=price_end_dt; 
			}
			else
			{
				document.getElementById('days').style.display="none";
				document.getElementById('days1').style.display="none";
				document.forms[0].days.disabled=true;
				
				document.forms[0].price_start_dt.style.display="none";
				document.forms[0].price_start_dt.disabled=true;
				document.forms[0].price_end_dt.style.display="none";
				document.forms[0].price_end_dt.disabled=true;
				//document.getElementById('avg1').style.display="none";
				document.getElementById('avg2').style.display="none";
				document.getElementById('avg3').style.display="none";
				//document.getElementById('avg4').style.display="none";
				//document.getElementById('avg5').style.display="none";
			}
		}
	}
	
	var sysdate=document.forms[0].sysdate.value;
	var value_1 = compareDate(sysdate,from_dt);
	
	/*if(seq_no=="1")
	{
		document.forms[0].from_dt.style.pointerEvents = "none";
		document.forms[0].from_dt.readOnly=true;
		document.forms[0].to_dt.style.pointerEvents = "none";
		document.forms[0].to_dt.readOnly=true;
	}
	else*/
	//COMMENTED ON 26-04-2024
	/*if(value_1=="1")
	{
		document.forms[0].from_dt.style.pointerEvents = "none";
		document.forms[0].from_dt.readOnly=true;
		document.forms[0].price_type.style.pointerEvents = "none";
		document.forms[0].physical_curve.style.pointerEvents = "none";
		document.forms[0].currency.style.pointerEvents = "none";
		document.forms[0].rate.readOnly=true;
	}
	else
	{
		document.forms[0].from_dt.style.pointerEvents = "auto";
		document.forms[0].from_dt.readOnly=false;
		document.forms[0].price_type.style.pointerEvents = "auto";
		document.forms[0].physical_curve.style.pointerEvents = "auto";
		document.forms[0].currency.style.pointerEvents = "auto";
		document.forms[0].rate.readOnly=false;
	}*/
	
	document.forms[0].opration.value="MODIFY";

	var mapped_cont_no = document.forms[0].mapped_cont_no.value;
	var cn_contract_status = document.forms[0].cn_contract_status.value;
	
	var old_value = "CP="+counterparty_cd+"#CPABBR="+counterparty_abbr+"#CONTNO="+mapped_cont_no+"#SLOPE="+slope+"#CONSTANT="+constant+"#CURVE_NM="+curve_nm+"#CONTREFNO="+cont_ref+"#CONT_STATUS="+cn_contract_status;
	
	document.forms[0].old_value.value=old_value;
}

function allowPriceLineEdit()
{
	var sysdate=document.forms[0].sysdate.value;
	var opration=document.forms[0].opration.value;
	
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var temp_from_dt = document.forms[0].temp_from_dt.value;
	var temp_to_dt = document.forms[0].temp_to_dt.value;
	
	var cont_start_dt = document.forms[0].cont_start_dt.value;
	var cont_end_dt = document.forms[0].cont_end_dt.value;
	
	if(from_dt != "" && to_dt != "")
	{
		var value_0 = compareDate(from_dt,to_dt);
		var value_1 = compareDate(from_dt,cont_start_dt);
		var value_11 = compareDate(from_dt,cont_end_dt);
		var value_2 = compareDate(to_dt,cont_start_dt);
		var value_22 = compareDate(to_dt,cont_end_dt);
		var value_3 = compareDate(sysdate,temp_from_dt);
		var value_33 = compareDate(sysdate,to_dt);
		
		if(value_0 == "1") //comparing from and to date
		{
			alert("Price line From date "+from_dt+" > To date "+to_dt+" not Allowed!");
		}
		else if(value_1 == "2" ||  value_11 == "1") //comparing from dt with contract date
		{
			alert("Price line ("+from_dt+" - "+to_dt+") should be with in range of Contract period ("+cont_start_dt+" - "+cont_end_dt+")!");
		}
		else if(value_2 == "2" ||  value_22 == "1") //comparing to dt with contract date
		{
			alert("Price line ("+from_dt+" - "+to_dt+") should be with in range of Contract period ("+cont_start_dt+" - "+cont_end_dt+")!");
		}
		else if(value_3 == "1" &&  value_33 == "1" && opration=="MODIFY") //comparing to dt with system date, iff from_dt < system date
		{
			alert("Price line To date "+to_dt+" should not be less than System date "+sysdate+"!");
		}
	}
}

//HARSH20210728 NEW FUNCTION
function setDisplay()
{
	var price_type = document.forms[0].price_type.value;
	
	if(price_type == "F" || price_type == "")  
	{
		var ele = document.getElementsByClassName("om");
		for (var i = 0; i < ele.length; i++)
		{
		       ele[i].style.display = "none";
		}		 
		if ( price_type == "")
		{
			document.getElementById('tr4').style.display="none";
		}	
		else
		{
			document.getElementById('tr4').style.display="";
		}	
		 
		document.getElementById('tr1').style.display="none";
		document.getElementById('tr2').style.display="none";
		document.getElementById('tr3').style.display="none";
		document.getElementById('tr5').style.display="none";
		  
		document.forms[0].remark.value="";
		document.forms[0].opal_min.value="";
		document.forms[0].formula.value="";
		document.forms[0].formula1.value="";	
	}
	/* else if(price_type == "Float(Complex)")
	{
		document.getElementById('tr1').style.display="table-row";
		document.getElementById('tr2').style.display="table-row";
		document.getElementById('tr3').style.display="table-row";
		document.getElementById('tr4').style.display="none";
		document.getElementById('curve_nm').multiple=false;
		document.getElementById('inf').style.display="nonr";
	} */
	else if(price_type == "M")
	{
		var ele = document.getElementsByClassName("om");
		for (var i = 0; i < ele.length; i++)
		{
			ele[i].style.display = "none";
		}
		document.getElementById('tr1').style.display="";
		document.getElementById('tr2').style.display="";
		document.getElementById('tr3').style.display="";
		document.getElementById('tr4').style.display="none";
		document.getElementById('tr5').style.display="";
		/*document.getElementById('curve_nm').multiple=false;
		document.getElementById('inf').style.display="none" */;
		document.forms[0].remark.value="";
		document.forms[0].opal_min.value="";
		document.forms[0].formula.value="";
		document.forms[0].formula1.value="";
		
	}
	else
	{
		document.getElementById('tr1').style.display="none";
		document.getElementById('tr2').style.display="none";
		document.getElementById('tr3').style.display="none"; 
		document.getElementById('tr4').style.display="none"; 
		document.getElementById('tr5').style.display="none"; 
	}
	setFormula();
}

//HARSH20210728 NEW FUNCTION
function setFormula()
{
	var opal_min = document.forms[0].opal_min.value;
	var formula = document.forms[0].formula.value;
	var formula1 = document.forms[0].formula1.value;
	var multi_type = document.forms[0].multi_type.value;
	
	if(opal_min=="MULTI_LEG")
	{
		document.getElementById('op1').style.display="inline";
		document.getElementById('op2').style.display="inline";
		document.getElementById('op3').style.display="inline";
		document.getElementById('op4').style.display="inline";
	}
	else
	{
		document.getElementById('op1').style.display="none";
		document.getElementById('op2').style.display="none";
		document.getElementById('op3').style.display="none";
		document.getElementById('op4').style.display="none";
	}
	
	if(opal_min=='MULTI_LEG')
	{	
		//document.getElementById('o1').style.display="inline";
		document.getElementById('o1').disabled=false;
		document.getElementById('curve_nm').multiple=false;
		//document.getElementById('o1').selected=true;
		document.getElementById('inf').style.display="none";
		document.getElementById('tr2').style.display="";
		document.getElementById('tr3').style.display="";
		document.getElementById('tr5').style.display="";
		document.getElementById('price_deci_h1').style.display="";
		document.getElementById('price_deci_h2').style.display="";
		document.getElementById('price_disc_h1').style.display="";
		document.getElementById('price_disc_h2').style.display="";
		document.getElementById('final_price_deci_h1').style.display="";
		document.getElementById('final_price_deci_h2').style.display="";
		
		var ele = document.getElementsByClassName("om");
		 for (var i = 0; i < ele.length; i++){
		        ele[i].style.display = "none";
		    }
		
		var selected = "";
		if (multi_type != "")
		{
			selected=opal_min+"@"+multi_type;				
			
			if(formula!="")
			{
				selected+="@"+formula;
				
				if(formula1!="")
				{
					selected+="@"+formula1;
				}
			}
		}
		document.forms[0].formula_desc.value=selected;
	}
	else if(opal_min=='MIN'|| opal_min=='MAX' || opal_min=='AVG' || opal_min=='MIN_ADV')
	{
		//document.getElementById('o1').style.display="none";
		document.getElementById('o1').disabled=true;
		document.getElementById('o1').selected=false;
		document.getElementById('curve_nm').multiple=true;
		document.getElementById('inf').style.display="inline";
		document.getElementById('op3').style.display="none";
		document.getElementById('op4').style.display="none";
		document.getElementById('tr2').style.display="";
		document.getElementById('tr3').style.display="none";
		document.getElementById('tr5').style.display="";
		document.getElementById('price_deci_h1').style.display="none";
		document.getElementById('price_deci_h2').style.display="none";
		document.getElementById('price_disc_h1').style.display="none";
		document.getElementById('price_disc_h2').style.display="none";
		document.getElementById('final_price_deci_h1').style.display="";
		document.getElementById('final_price_deci_h2').style.display="";
		
		var selected = "";
		var temp_adv_formula = "";
		var sel=[];
		for (var option of document.getElementById('curve_nm').options)
	    {
	        if (option.selected) {
	        	var id= option.value;
	        	if(id!="")
	        	{
	        		document.getElementById(option.value).style.display="";
	        		document.getElementById(option.value+"_slope").disabled=false;
	        		document.getElementById(option.value+"_constant").disabled=false;
	        		document.getElementById(option.value+"_price_range").disabled=false;
	        		document.getElementById(option.value+"_days").disabled=false;
	    			document.getElementById(option.value+"_price_start_dt").disabled=false;
	        		document.getElementById(option.value+"_price_end_dt").disabled=false;
	        		document.getElementById(option.value+"_formula_desc").disabled=false;
	        		document.getElementById(option.value+"_price_deci").disabled=false;
	        		document.getElementById(option.value+"_prem_disc_rate").disabled=false;
	        		
	        		if(opal_min=='MIN_ADV' && option.value!="PPAC")
	        		{
	        			document.getElementById('MIN_4_'+option.value).style.display="inline";
	        			document.getElementById('MIN_5_'+option.value).style.display="inline";
	        			document.getElementById('MIN_6_'+option.value).style.display="inline";
	        			document.getElementById('MIN_7_'+option.value).style.display="inline";
	        			
	        			var temp_f = document.getElementById(option.value+"_formula").value;
	        			var temp_f1 = document.getElementById(option.value+"_formula1").value;
	        			var temp_mutityp = document.getElementById(option.value+"_multi_type").value;
	        			
	        			var temp_selected="";
	        			if (temp_mutityp != "")
	        			{
	        				temp_selected="MULTI_LEG@"+temp_mutityp;
	        				temp_adv_formula="("+temp_mutityp;
	        				
	        				if(temp_f!="")
	        				{
	        					temp_selected+="@"+temp_f;
	        					temp_adv_formula+=","+temp_f;
	        					
	        					if(temp_f1!="")
	        					{
	        						temp_selected+="@"+temp_f1;
	        						temp_adv_formula+=","+temp_f1+")";
	        					}
	        				}
	        			}
	        			document.getElementById(option.value+"_formula_desc").value=temp_selected;
	        		}
	        		else
	        		{
	        			document.getElementById('MIN_4_'+option.value).style.display="none";
	        			document.getElementById('MIN_5_'+option.value).style.display="none";
	        			document.getElementById('MIN_6_'+option.value).style.display="none";
	        			document.getElementById('MIN_7_'+option.value).style.display="none";
	        			
	        			document.getElementById(option.value+"_formula_desc").value="";
	        		}
	        	}
	        	if(selected=="")
	        	{
	        		if(id !="")
	        		{
	        			selected=opal_min+"@"+option.value;
	        		}
	        	}
	        	else
	        	{
	        		selected+="@"+option.value;
	        	}
	        	
	        	if(opal_min=='MIN_ADV' && option.value!="PPAC")
        		{
	        		selected+=""+temp_adv_formula;
        		}
	        	
	            sel.push(option.value);
	        }
	        else
			{
	        	var id= option.value;
	        	if(id!="")
	        	{
	        		document.getElementById(id).style.display="none";
	        		document.getElementById(option.value+"_slope").disabled=true;
	        		document.getElementById(option.value+"_constant").disabled=true;
	        		document.getElementById(option.value+"_price_range").disabled=true;
	        		document.getElementById(option.value+"_days").disabled=true;
	    			document.getElementById(option.value+"_price_start_dt").disabled=true;
	        		document.getElementById(option.value+"_price_end_dt").disabled=true;
	        		document.getElementById(option.value+"_formula_desc").disabled=true;
	        		document.getElementById(option.value+"_price_deci").disabled=true;
	        		document.getElementById(option.value+"_prem_disc_rate").disabled=true;
	        		
	        		if(opal_min=='MIN_ADV' && option.value!="PPAC")
	        		{
	        			document.getElementById('MIN_4_'+option.value).style.display="inline";
	        			document.getElementById('MIN_5_'+option.value).style.display="inline";
	        			document.getElementById('MIN_6_'+option.value).style.display="inline";
	        			document.getElementById('MIN_7_'+option.value).style.display="inline";
	        			
	        			var temp_f = document.getElementById(option.value+"_formula").value;
	        			var temp_f1 = document.getElementById(option.value+"_formula1").value;
	        			var temp_mutityp = document.getElementById(option.value+"_multi_type").value;
	        			
	        			var temp_selected="";
	        			if (temp_mutityp != "")
	        			{
	        				temp_selected="MULTI_LEG@"+temp_mutityp;				
	        				
	        				if(temp_f!="")
	        				{
	        					temp_selected+="@"+temp_f;
	        					
	        					if(temp_f1!="")
	        					{
	        						temp_selected+="@"+temp_f1;
	        					}
	        				}
	        			}
	        			document.getElementById(option.value+"_formula_desc").value=temp_selected;
	        		}
	        		else
	        		{
	        			document.getElementById('MIN_4_'+option.value).style.display="none";
	        			document.getElementById('MIN_5_'+option.value).style.display="none";
	        			document.getElementById('MIN_6_'+option.value).style.display="none";
	        			document.getElementById('MIN_7_'+option.value).style.display="none";	
	        			
	        			document.getElementById(option.value+"_formula_desc").value="";
	        		}
	        	}
			}
	    }
		
		document.forms[0].formula_desc.value=selected;
	}
	else if(opal_min=='SINGLE')
	{
		document.getElementById('o1').disabled=false;
		document.getElementById('curve_nm').multiple=false;
		document.getElementById('inf').style.display="none";
		document.getElementById('tr2').style.display="";
		document.getElementById('tr3').style.display="";
		document.getElementById('tr5').style.display="";
		document.getElementById('price_deci_h1').style.display="";
		document.getElementById('price_deci_h2').style.display="";
		document.getElementById('price_disc_h1').style.display="";
		document.getElementById('price_disc_h2').style.display="";
		document.getElementById('final_price_deci_h1').style.display="";
		document.getElementById('final_price_deci_h2').style.display="";
		
		document.forms[0].formula_desc.value="";
		
		var ele = document.getElementsByClassName("om");
		for (var i = 0; i < ele.length; i++)
		{
			ele[i].style.display = "none";
		}	
	}
	else
	{
		document.getElementById('o1').disabled=false;
		document.getElementById('curve_nm').multiple=false;
		document.getElementById('tr2').style.display="none";
		//document.getElementById('curve_nm').multiple=false;
		document.getElementById('inf').style.display="none";
		//document.getElementById('op3').style.display="none";
		//document.getElementById('op4').style.display="none";
		document.getElementById('tr3').style.display="none";	
		document.getElementById('tr5').style.display="none";	
		document.forms[0].formula_desc.value="";
		
		var ele = document.getElementsByClassName("om");
		for (var i = 0; i < ele.length; i++)
		{
			ele[i].style.display = "none";
		}					
	}	
}

//HARSH20210729 NEW FUNCATION 
function openEditBox(index,curve)
{
	if(index == '' && curve == '')
	{
		var price_range = document.forms[0].price_range.value;
		
		if(price_range == "O")
		{
			document.getElementById('days').style.display="inline";
			document.getElementById('days1').style.display="inline";
			document.forms[0].days.disabled=false;
			
			document.forms[0].price_start_dt.style.display="none";
			document.forms[0].price_start_dt.disabled=true;
			document.forms[0].price_end_dt.style.display="none";
			document.forms[0].price_end_dt.disabled=true;
			//document.getElementById('avg1').style.display="none";
			document.getElementById('avg2').style.display="none";
			document.getElementById('avg3').style.display="none";
			//document.getElementById('avg4').style.display="none";
			//document.getElementById('avg5').style.display="none";
		}
		else if(price_range == "D")
		{
			document.forms[0].price_start_dt.style.display="inline";
			document.forms[0].price_start_dt.disabled=false;
			document.forms[0].price_end_dt.style.display="inline";
			document.forms[0].price_end_dt.disabled=false;
			
			//document.getElementById('avg1').style.display="inline";
			document.getElementById('avg2').style.display="inline";
			document.getElementById('avg3').style.display="inline";
			//document.getElementById('avg4').style.display="inline";
			//document.getElementById('avg5').style.display="inline";
			
			document.getElementById('days').style.display="none";
			document.getElementById('days1').style.display="none";
			document.forms[0].days.disabled=true;
		}
		else
		{
			document.getElementById('days').style.display="none";
			document.getElementById('days1').style.display="none";
			document.forms[0].days.disabled=true;
			
			document.forms[0].price_start_dt.style.display="none";
			document.forms[0].price_start_dt.disabled=true;
			document.forms[0].price_end_dt.style.display="none";
			document.forms[0].price_end_dt.disabled=true;
			//document.getElementById('avg1').style.display="none";
			document.getElementById('avg2').style.display="none";
			document.getElementById('avg3').style.display="none";
			//document.getElementById('avg4').style.display="none";
			//document.getElementById('avg5').style.display="none";
		}
	}
	else if(index != '' && curve != '')
	{
		var price_range = document.getElementById(curve+'_price_range').value;
		if(price_range == "O")
		{
			document.getElementById('MIN_0_'+index).style.display="inline";
			document.getElementById('MIN_1_'+index).style.display="inline";
			document.getElementById('MIN_2_'+index).style.display="none";
			document.getElementById('MIN_3_'+index).style.display="none";
			document.getElementById(curve+"_price_start_dt").value="";
			document.getElementById(curve+"_price_end_dt").value="";
			document.getElementById(curve+"_days").disabled=false;
			document.getElementById(curve+"_price_range").disabled=false;
			document.getElementById(curve+"_price_start_dt").disabled=false;
    		document.getElementById(curve+"_price_end_dt").disabled=false;
    		
		}
		else if(price_range == "D")
		{
			document.getElementById('MIN_2_'+index).style.display="inline";
			document.getElementById('MIN_3_'+index).style.display="inline";
			document.getElementById('MIN_0_'+index).style.display="none";
			document.getElementById('MIN_1_'+index).style.display="none";
			document.getElementById(curve+"_price_range").disabled=false;
    		document.getElementById(curve+"_price_start_dt").disabled=false;
    		document.getElementById(curve+"_price_end_dt").disabled=false;
    		document.getElementById(curve+"_days").disabled=false;
		}
		else
		{
			document.getElementById('MIN_0_'+index).style.display="none";
			document.getElementById('MIN_1_'+index).style.display="none";
			document.getElementById('MIN_2_'+index).style.display="none";
			document.getElementById('MIN_3_'+index).style.display="none";
			document.getElementById(curve+"_price_start_dt").value="";
			document.getElementById(curve+"_price_end_dt").value="";
			document.getElementById(curve+"_days").disabled=false;
			document.getElementById(curve+"_price_range").disabled=false;
			document.getElementById(curve+"_price_start_dt").disabled=false;
    		document.getElementById(curve+"_price_end_dt").disabled=false;
		}
	}
}

function checkZero(obj)
{
	var days = obj.value;
	var number = /^[0-9]+$/;
	if(days.match(number))
	{
		if(days > 0)
		{
		}
		else
		{
			alert("Please Enter the number of days greater or equal to 1");
			obj.value="";
			return false;
		}
	}
	else
	{
		alert("Please Enter Only Numeric Value !!");
		obj.value="";
		return false;
	}
}
</script>

</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_VariablePricing" id="market_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String cont_end_dt=request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String cont_ref=request.getParameter("cont_ref")==null?"":request.getParameter("cont_ref");
String cont_status=request.getParameter("cont_status")==null?"":request.getParameter("cont_status");
String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
String cn_contract_status=request.getParameter("cn_contract_status")==null?"":request.getParameter("cn_contract_status");

String contract_no ="";
if(contract_type.equalsIgnoreCase("S"))
{
	contract_no = contract_type+""+agmt_no+"-"+cont_no+"-"+cargo_no;
}
else if(contract_type.equalsIgnoreCase("S"))
{
	contract_no = contract_type+""+agmt_no+"-"+cont_no;
}
else
{
	contract_no = contract_type+""+cont_no;
}


String mapping_id=counterparty_cd+"-"+agmt_no+"-"+cont_no;
if(contract_type.equals("N"))
{
	mapping_id+="-"+cargo_no;
}

market_risk.setCallFlag("VARIABLE_PRICE_CONFIG");
market_risk.setCounterparty_cd(counterparty_cd);
market_risk.setComp_cd(owner_cd);
market_risk.setAgmt_no(agmt_no);
market_risk.setAgmt_rev_no(agmt_rev_no);
market_risk.setCont_no(cont_no);
market_risk.setCont_rev_no(cont_rev_no);
market_risk.setContract_type(contract_type);
market_risk.setCargo_no(cargo_no);
market_risk.setMapping_id(mapping_id);
market_risk.init();

String counterparty_abbr=market_risk.getCounterparty_abbr();
String display_map_id = market_risk.getDisplay_map_id();

Vector VSTART_DT = market_risk.getVSTART_DT();
Vector VEND_DT = market_risk.getVEND_DT();
Vector VPRICE_TYPE = market_risk.getVPRICE_TYPE();
Vector VPRICE_RANGE_NM = market_risk.getVPRICE_RANGE_NM();
Vector VPRICE_RATE = market_risk.getVPRICE_RATE();
Vector VPRICE_RATE_UNIT = market_risk.getVPRICE_RATE_UNIT();
Vector VPRICE_RATE_UNIT_NM = market_risk.getVPRICE_RATE_UNIT_NM();
Vector VPHYSICAL_CURVE = market_risk.getVPHYSICAL_CURVE();
Vector VSLOPE = market_risk.getVSLOPE();
Vector VCONSTANT = market_risk.getVCONSTANT();
Vector VREMARK = market_risk.getVREMARK();
Vector VSEQ_NO = market_risk.getVSEQ_NO();

Vector VIS_RADIO_ENABLE = market_risk.getVIS_RADIO_ENABLE();
Vector VPHYSICAL_CURVE_MST = market_risk.getVPHYSICAL_CURVE_MST();
Vector VFINANCIAL_CURVE_MST = market_risk.getVFINANCIAL_CURVE_MST();
Vector VCURVE_NM = market_risk.getVCURVE_NM();
Vector VPRICE_RANGE = market_risk.getVPRICE_RANGE();
Vector VPRICE_START_DT = market_risk.getVPRICE_START_DT();
Vector VPRICE_END_DT = market_risk.getVPRICE_END_DT();

Vector VCURVE_LOGIC = market_risk.getVCURVE_LOGIC();
Vector VFORMULA = market_risk.getVFORMULA();
Vector VMIN_PRICE_ST_END_DT = market_risk.getVMIN_PRICE_ST_END_DT();
Vector VMIN_FORMULA = market_risk.getVMIN_FORMULA();
Vector VPRICE_DECI = market_risk.getVPRICE_DECI();
Vector VFINAL_PRICE_DECI = market_risk.getVFINAL_PRICE_DECI();
Vector VPREM_DISC_RATE = market_risk.getVPREM_DISC_RATE();

String num_price_line = "";
String price_line_start_dt = "";
String price_line_end_dt = "";
String isDefaulter="";
if (VSTART_DT.size() <= 0)
{
	price_line_start_dt = cont_start_dt;
	price_line_end_dt = cont_end_dt;
	//	isDefaulter="Y";	
}	
%>
<body>
<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_VariablePricing">

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
					    	Price Configuration <%=counterparty_abbr%> (<%=display_map_id%>) [Contract Period : <%=cont_start_dt %> - <%=cont_end_dt %>]
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th></th>										
										<th>From</th>
										<th>To</th>
										<th>Pricing Type</th>
										<th>Curve Logic</th>										
										<th>Price Detail</th>										
										<th>Settle Price/Mth Logic</th>
										<th>Slope</th>
										<th>Constant</th>
										<th>Premium(+)/ Discount(-)</th>
										<th>Price Upto (Decimal)</th>
										<th>Final Price Upto (Decimal)</th>
										<th>Physical Curve</th>
										<th>Price Formula</th>
										<th>Remark</th>
									</tr>
								</thead>
								<tbody>
								<%if(VPRICE_TYPE.size()>0) {%>
									<%for(int i=0;i<VPRICE_TYPE.size(); i++){%>	
									<tr>
										<td align="center">
											<input type="radio" name="rd" 
												<%-- <%if (VIS_RADIO_ENABLE.elementAt(i).equals("N")){ %>
													disabled
												<%} %> --%>
												onclick="doModify('<%=VSEQ_NO.elementAt(i)%>','<%=VSTART_DT.elementAt(i)%>','<%=VEND_DT.elementAt(i)%>',
													'<%=VPRICE_TYPE.elementAt(i)%>','<%=VSLOPE.elementAt(i)%>','<%=VCONSTANT.elementAt(i)%>',
													'<%=VREMARK.elementAt(i)%>','<%=VPHYSICAL_CURVE.elementAt(i)%>','<%=VPRICE_RATE_UNIT.elementAt(i)%>',
													'<%=VPRICE_RATE.elementAt(i)%>','<%=VCURVE_NM.elementAt(i)%>','<%=VPRICE_RANGE.elementAt(i)%>',
													'<%=VPRICE_START_DT.elementAt(i)%>', '<%=VPRICE_END_DT.elementAt(i)%>',
													'<%=VCURVE_LOGIC.elementAt(i)%>','<%=VFORMULA.elementAt(i)%>','<%=VMIN_PRICE_ST_END_DT.elementAt(i)%>','<%=VMIN_FORMULA.elementAt(i)%>',
													'<%=counterparty_abbr%>','<%=counterparty_cd%>','<%=contract_no%>','<%=cont_ref%>','<%=cont_status%>','<%=VPRICE_DECI.elementAt(i)%>','<%=VFINAL_PRICE_DECI.elementAt(i)%>','<%=VPREM_DISC_RATE.elementAt(i)%>');">&nbsp;&nbsp;
											<%=i+1%>
										</td>
										<td align="center">
											<input type="hidden" name="line_start_date" id="line_start_date<%=i%>" value="<%=VSTART_DT.elementAt(i)%>" >
											<input type="hidden" name="line_seq_no" id="line_seq_no<%=i%>" value="<%=VSEQ_NO.elementAt(i)%>" >
											<%=VSTART_DT.elementAt(i)%>
										</td>
										<td align="center">
											<input type="hidden" name="line_end_date" id="line_end_date<%=i%>" value="<%=VEND_DT.elementAt(i)%>" >
											<%=VEND_DT.elementAt(i)%>
										</td>																	
										<td align="center" >
											<%if(VPRICE_TYPE.elementAt(i).equals("F")) {%>
												FIXED PRICE
											<%}else{%>
												CURVE PRICE
											<%}%>
										</td>
										<td align="center"><%=VCURVE_LOGIC.elementAt(i)%></td>
										<td align="center">
											<%if(VPRICE_TYPE.elementAt(i).equals("F")) {%>
												<%=VPRICE_RATE.elementAt(i)%> <%=VPRICE_RATE_UNIT_NM.elementAt(i)%> /MMBTU</td>
											<%}else{%>
												<%=VCURVE_NM.elementAt(i) %>
											<%}%></td>											
										<td align="center"><%=VPRICE_RANGE_NM.elementAt(i)%></td>										
										<td align="right"><%=VSLOPE.elementAt(i)%></td>
										<td align="right"><%=VCONSTANT.elementAt(i)%></td>
										<td align="right"><%=VPREM_DISC_RATE.elementAt(i) %></td>
										<td align="center"><%=VPRICE_DECI.elementAt(i) %></td>
										<td align="center"><%=VFINAL_PRICE_DECI.elementAt(i) %></td>
										<td align="center"><%=VPHYSICAL_CURVE.elementAt(i)%></td>	
										<td align="center"><%=VFORMULA.elementAt(i)%></td>									
										<td align="center"><%=VREMARK.elementAt(i)%></td>																			
									</tr>
									<%} %>
								<%}else{ %>
								<tr>
									<td colspan="15">
										<div align="center"><%=utilmsg.infoMessage("<b>No Price Line Configured!</b>")%></div>
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
	&nbsp;
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Pricing Details
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>From Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=price_line_start_dt%>" maxLength="10" 
			      						onchange="validateDate(this);" autocomplete="off"> <!-- allowPriceLineEdit(); -->
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<input type="hidden" name="seq_no" value="">
		      						<input type="hidden" name="temp_from_dt" value="">
	      						</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>To Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=price_line_end_dt %>" maxLength="10" 
			      						onchange="validateDate(this);" autocomplete="off"> <!-- allowPriceLineEdit(); -->
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<input type="hidden" name="temp_to_dt" value="">
	      						</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Pricing Type<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="price_type" id="price_type" onchange="setDisplay();">
										<option value="">--Select--</option>
										<option value="F">FIXED PRICE</option>
						    			<option value="M">CURVE PRICE</option>					    			
									</select>
								</div>
							</div>
						</div>		
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Physical Curve<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="physical_curve" >
										<option value="0">--Select--</option>
										<!-- <option value="RLNG_PHYS_INDIA">RLNG_PHYS_INDIA</option> -->
										<% for(int j=0; j<VPHYSICAL_CURVE_MST.size(); j++){ %>
 											<option value="<%=VPHYSICAL_CURVE_MST.elementAt(j)%>"><%=VPHYSICAL_CURVE_MST.elementAt(j)%></option>
 										<%} %>												    			
									</select>
								</div>
							</div>
						</div>				
					</div>	
					
					<!-- // START :: SWITCH ON PRICE TYPE	 -->					 									
					<!-- FLOAT PRICE -->
					<div class="row m-b-5" id="tr1" style="display:none;">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Curve Logic<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">				    			
			    					<select class="form-select form-select-sm" name="opal_min" onchange="setFormula();">
						 				<option value="">--Select--</option>
						 				<option value="SINGLE">SINGLE</option>
						 				<option value="MIN">MIN</option>
						 				<option value="MAX">MAX</option>
						 				<option value="AVG">AVG</option>
						 				<option value="MULTI_LEG">MULTI_LEG</option>
						 				<option value="MIN_ADV">MIN(PPAC, MULTI_LEG)</option>						 				
						 			</select>				    			
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div id="op1" style="display:none;">
				    				<label class="form-label"><b>Multi Period</b></label>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-auto">				    			
			    					<div id="op4" style="display:none;">
							 			<select class="form-select form-select-sm" name="multi_type" onchange="setFormula();">
							 				<option value="">--Select--</option>							 			
							 				<option value="Settled">Settled (M-)</option>
							 				<option value="Forward">Forward (M+)</option>
							 			</select>
							 		</div>
							 	</div>
								<div class="col-auto">				    			
			    					<div id="op2" style="display:none;">
							 			<select class="form-select form-select-sm" name="formula" onchange="setFormula();">
							 				<option value="">--Leg 1--</option>
							 				<%for(int i=0; i<=36; i++){ %>
							 				<option value="<%=i%>">M<%=i%></option>
							 				<%} %>
							 			</select>
							 		</div>
							 	</div>
							 	<div class="col-auto">
							 		<div id="op3" style="display:none;">
							 			<select class="form-select form-select-sm" name="formula1" onchange="setFormula();">
							 				<option value="">--Leg 2--</option>
							 				<%for(int i=0; i<=36; i++){ %>
							 				<option value="<%=i%>">M<%=i%></option>
							 				<%} %>
							 			</select>
							 		</div>				    			
				  				</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5" id="tr2" style="display:none;">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Financial Curve<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">				    			
			    					<select class="form-select form-select-sm"  name="curve_nm" id="curve_nm" onchange="setFormula()">
			    						<option value="" id="o1">-Select-</option>
						 				<% for(int j=0; j<VFINANCIAL_CURVE_MST.size(); j++){ %>
			 							<option value="<%=VFINANCIAL_CURVE_MST.elementAt(j)%>"><%=VFINANCIAL_CURVE_MST.elementAt(j)%></option>
			 							<%} %>
						 			</select>
						 			<span id="inf" style="display:none;"><font color="red" size="2">Press ctrl/shift for multiple selection!</font></span>				    			
				  				</div>
				  			</div>
						</div>
					</div>
					
					<!-- PB 20260227: added below -->
					<div class="row m-b-5" id="tr5" style="display:none;">
						<div class="col-sm-2 col-xs-2 col-md-2" style="display:none;" id="price_deci_h1">  
							<div class="form-group row">
				    			<label class="form-label"><b>Price Upto(Decimal)<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" style="display:none;" id="price_deci_h2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">		
								<select class="form-select form-select-sm" name="price_deci" id="price_deci" value="" onBlur="checkNumber1(this,1,0);">
										<option value="">-Select-</option>
										<option value="0">0 Decimal</option>
										<option value="1">1 Decimal</option>
										<option value="2">2 Decimal</option>
										<option value="3">3 Decimal</option>
										<option value="4">4 Decimal</option>
									</select>
									<!-- <input type="number" class="form-control form-control-sm" name="price_deci" id="price_deci" value="" onBlur="checkNumber1(this,1,0);">  -->
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2" style="display:none;" id="price_disc_h1">  
							<div class="form-group row">
				    			<label class="form-label"><b>Premium(+)/Discount(-) $/MMbtu<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" style="display:none;" id="price_disc_h2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">		
									<input type="number" Title="For Discount enter with Minus(-) sign" class="form-control form-control-sm" name="prem_disc_rate" id="prem_disc_rate" value="" onBlur="checkNumber1(this,6,4);" onchange="checkNumber1(this,6,4);"> 
				  				</div>
				  			</div>
						</div>
						<!-- <div class="col-sm-2 col-xs-2 col-md-2" style="display:none;" id="final_price_deci_h1">  
							<div class="form-group row">
				    			<label class="form-label"><b>Final Price Upto(Decimal)<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" style="display:none;" id="final_price_deci_h2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">	
									<select class="form-select form-select-sm" name="final_price_deci" id="final_price_deci" value="" onBlur="checkNumber1(this,1,0);" >
										<option value="">-Select-</option>
										<option value="0">0 Decimal</option>
										<option value="1">1 Decimal</option>
										<option value="2">2 Decimal</option>
										<option value="3">3 Decimal</option>
										<option value="4">4 Decimal</option>
									</select>	
									<input type="number" class="form-control form-control-sm" name="final_price_deci" id="final_price_deci" value="" onBlur="checkNumber1(this,1,0);"> 
				  				</div>
				  			</div>
						</div> -->
					</div>
					
					<div class="row m-b-5" id="tr3" style="display:none;">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Slope<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">				    			
			    					<input type="text" class="form-control form-control-sm" name="slope" value="" onBlur="checkNumber1(this,9,6);">				    			
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Constant<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">				    			
			    					<input type="text" class="form-control form-control-sm" name="constant" value="" onBlur="checkNumber1(this,9,6);">				    			
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Settle Price/Mth Logic<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">				    			
			    					<select class="form-select form-select-sm" name="price_range" id="price_range" onchange="openEditBox('','');" >
										<option value="">-Select-</option>
										<option value="A">Avg.</option>
										<option value="F">Final Settle Date</option>
										<option value="O">Other Avg.</option>
										<option value="D">Avg. Settle Date</option>
									</select>				    			
				  				</div>
				  				<input type="hidden" name="ppac_validity_dt" value="" size="10" maxlength="11" onBlur="validateDate(this);" readonly>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2" style="display:none;" id="days1">
							<label class="form-label"><b># Days: </b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" style="display:none;" id="days">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
							 		<input type="text" class="form-control form-control-sm" name="days" onblur="checkZero(this);" disabled>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2" style="display:none;" id="avg2">
							<label class="form-label"><b>Day Range</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" style="display:none;" id="avg3">
							<div class="form-group row">
								<div class="col">
									<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="price_start_dt" value="" maxLength="10" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
								</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col">
									<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="price_end_dt" value="" maxLength="10" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
								</div>
							</div>
						</div>
					</div>
					
					<%for(int i=0; i<VFINANCIAL_CURVE_MST.size(); i++){ 
					String fin_curv_ele=""+VFINANCIAL_CURVE_MST.elementAt(i);
					%>
					<div class="row m-b-5 om" id="<%=fin_curv_ele%>" style="display:none;">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
								<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;<%=fin_curv_ele%></label>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2" id="MIN_4_<%=fin_curv_ele%>" style="display:none">  
							<div class="form-group row">
				    			<label class="form-label"><b>Multi Period<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" id="MIN_5_<%=fin_curv_ele%>" style="display:none">  
							<div class="form-group row">
								<div class="col-auto">				    			
						 			<select class="form-select form-select-sm" name="MIN_multi_type" id="<%=fin_curv_ele%>_multi_type" onchange="setFormula();">
						 				<option value="">--Select--</option>							 			
						 				<option value="Settled">Settled (M-)</option>
						 				<option value="Forward">Forward (M+)</option>
						 			</select>
							 	</div>
								<div class="col-auto">				    			
						 			<select class="form-select form-select-sm" name="MIN_formula" id="<%=fin_curv_ele%>_formula" onchange="setFormula();">
						 				<option value="">--Leg 1--</option>
						 				<%for(int j=0; j<=12; j++){ %>
						 				<option value="<%=j%>">M<%=j%></option>
						 				<%} %>
						 			</select>
							 	</div>
							 	<div class="col-auto">
						 			<select class="form-select form-select-sm" name="MIN_formula1" id="<%=fin_curv_ele%>_formula1" onchange="setFormula();">
						 				<option value="">--Leg 2--</option>
						 				<%for(int j=0; j<=12; j++){ %>
						 				<option value="<%=j%>">M<%=j%></option>
						 				<%} %>
						 			</select>			    			
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2" id="MIN_6_<%=fin_curv_ele%>">  
							<div class="form-group row">
				    			<label class="form-label"><b>Price Formula<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" id="MIN_7_<%=fin_curv_ele%>">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<textarea class="form-control form-control-sm"  name="MIN_formula_desc" id="<%=fin_curv_ele%>_formula_desc" rows="1" maxlength="150" readonly disabled></textarea>
				      			</div>
				  			</div>
						</div>
						
						<!-- PB 20250227 -->
						<!-- <div class="col-sm-2 col-xs-2 col-md-2"> 
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
						</div> -->
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Price Upto(Decimal)<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">		
								<select class="form-select form-select-sm" name="MIN_price_deci" id="<%=fin_curv_ele%>_price_deci" value="" onBlur="checkNumber1(this,1,0);" disabled>
										<option value="">-Select-</option>
										<option value="0">0 Decimal</option>
										<option value="1">1 Decimal</option>
										<option value="2">2 Decimal</option>
										<option value="3">3 Decimal</option>
										<option value="4">4 Decimal</option>
									</select>
									<!-- <input type="number" class="form-control form-control-sm" name="price_deci" id="price_deci" value="" onBlur="checkNumber1(this,1,0);">  -->
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Premium(+)/Discount(-) $/MMbtu<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">		
									<input type="number" Title="For Discount enter with Minus(-) sign" class="form-control form-control-sm" name="MIN_prem_disc_rate" id="<%=fin_curv_ele%>_prem_disc_rate" value="" onBlur="checkNumber1(this,6,4);" onchange="checkNumber1(this,6,4);"" disabled> 
				  				</div>
				  			</div>
						</div>
						<!-- <div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Final Price Upto(Decimal)<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">	
									<select class="form-select form-select-sm" name="MIN_final_price_deci" id="MIN_final_price_deci" value="" onBlur="checkNumber1(this,1,0);" >
										<option value="">-Select-</option>
										<option value="0">0 Decimal</option>
										<option value="1">1 Decimal</option>
										<option value="2">2 Decimal</option>
										<option value="3">3 Decimal</option>
										<option value="4">4 Decimal</option>
									</select>	
									<input type="number" class="form-control form-control-sm" name="final_price_deci" id="final_price_deci" value="" onBlur="checkNumber1(this,1,0);"> 
				  				</div>
				  			</div>
						</div> -->
						
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Slope<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">				    			
			    					<input type="text" class="form-control form-control-sm" name="MIN_slope" id="<%=fin_curv_ele%>_slope" 
			    					value="" onBlur="checkNumber1(this,9,6);" disabled>				    			
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Constant<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">				    			
			    					<input type="text" class="form-control form-control-sm" name="MIN_constant" id="<%=fin_curv_ele%>_constant" 
			    					value="" onBlur="checkNumber1(this,9,6);" disabled>				    			
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Settle Price/Mth</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">				    			
			    					<select class="form-select form-select-sm" name="MIN_price_range" id="<%=fin_curv_ele%>_price_range" onchange="openEditBox('<%=i+1%>','<%=fin_curv_ele%>');" disabled>
										<option value="">-Select-</option>
										<option value="A">Avg.</option>
										<option value="F">Final Settle Date</option>
										<option value="O">Other (Avg.)</option>
										<option value="D">Avg. Settle Date</option>
									</select>				    			
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2" id="MIN_0_<%=i+1%>" style="display:none">  
							<div class="form-group row">
				    			<label class="form-label"><b>#Days</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" id="MIN_1_<%=i+1%>" style="display:none">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="MIN_days" id="<%=fin_curv_ele%>_days" onblur="checkZero(this);" disabled>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2" id="MIN_2_<%=i+1%>" style="display:none">  
							<div class="form-group row">
				    			<label class="form-label"><b>Day Range</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" id="MIN_3_<%=i+1%>" style="display:none">  
							<div class="form-group row">
								<div class="col">
									<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="MIN_price_start_dt" id="<%=fin_curv_ele%>_price_start_dt" value="" maxLength="10" 
			      						onchange="validateDate(this);" autocomplete="off" disabled>
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
								</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col">
									<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="MIN_price_end_dt" id="<%=fin_curv_ele%>_price_end_dt" value="" maxLength="10" 
			      						onchange="validateDate(this);" autocomplete="off" disabled>
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
								</div>
							</div>
						</div>
					</div>
					<%} %>
					
					<!-- FIXED PRICE  -->
					<div class="row m-b-5" id="tr4" style="display:none;">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Fixed Price<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col">				    			
			    					<input type="text" class="form-control form-control-sm" name="rate" value="" onBlur="checkNumber1(this,8,4);">				    			
				  				</div>
				  				<div class="col">
				  					<select class="form-select form-select-sm" name="currency">
				      					<option value="1">INR/MMBTU</option>
				      					<option value="2">USD/MMBTU</option>
				      				</select>
				  				</div>
				  			</div>
						</div>
					</div>	
										
					<!-- // END :: SWITCH ON PRICE TYPE -->
					<br>
					<div class="row m-b-5">	
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Price Formula</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<textarea class="form-control form-control-sm"  name="formula_desc" rows="1" maxlength="150" readonly></textarea>
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
				    				<textarea class="form-control form-control-sm"  name="remark" rows="1" maxlength="150"></textarea>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2" style="display:none;" id="final_price_deci_h1">  
							<div class="form-group row">
				    			<label class="form-label"><b>Final Price Upto(Decimal)<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" style="display:none;" id="final_price_deci_h2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">	
									<select class="form-select form-select-sm" name="final_price_deci" id="final_price_deci" value="" onBlur="checkNumber1(this,1,0);" >
										<option value="">-Select-</option>
										<option value="0">0 Decimal</option>
										<option value="1">1 Decimal</option>
										<option value="2">2 Decimal</option>
										<option value="3">3 Decimal</option>
										<option value="4">4 Decimal</option>
									</select>	
									<!-- <input type="number" class="form-control form-control-sm" name="final_price_deci" id="final_price_deci" value="" onBlur="checkNumber1(this,1,0);"> --> 
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


<input type="hidden" name="option" value="VARIABLE_PRICE_CONFIG">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="old_value" value="">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="mapped_cont_no" value="<%=display_map_id%>">
<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="cont_start_dt" value="<%=cont_start_dt%>">
<input type="hidden" name="cont_end_dt" value="<%=cont_end_dt%>">
<input type="hidden" name="cont_ref" value="<%=cont_ref%>">
<input type="hidden" name="cont_status" value="<%=cont_status%>">
<input type="hidden" name="cargo_no" value="<%=cargo_no%>">
<input type="hidden" name="mapping_id" value="<%=mapping_id%>">
<input type="hidden" name="display_map_id" value="<%=display_map_id%>">
<input type="hidden" name="cn_contract_status" value="<%=cn_contract_status%>">

<input type="hidden" name="sysdate" value="<%=sysdate%>">

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

<%if(isDefaulter.equals("Y")){ %>
<script>
/* document.forms[0].from_dt.style.pointerEvents = "none";
document.forms[0].from_dt.readOnly=true;
document.forms[0].to_dt.style.pointerEvents = "none";
document.forms[0].to_dt.readOnly=true;
document.forms[0].remark.value="Fallback Price Line";
document.forms[0].remark.readOnly=true; */
</script>
<%} %>
</form>
</body>
</html>