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
	
	var url = "frm_meter_ticket_reading.jsp?gas_dt="+gas_dt+"&u="+u;

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

function doSubmit()
{
	var gas_dt = document.forms[0].gas_dt.value;
	var gen_dt = document.forms[0].gen_dt.value;
	
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	
	var calc_gcv = document.forms[0].calc_gcv;
	var calc_ncv = document.forms[0].calc_ncv;
	var define_gcv = document.forms[0].define_gcv;
	var define_ncv = document.forms[0].define_ncv;
	
	
	var msg="";
	var flag=true;
	
	if(trim(gas_dt) == "")
	{
		msg+="Select Gas Day!\n";
		flag=false;
	}
	if(trim(gen_dt) == "")
	{
		msg+="Select Gen Date!\n";
		flag=false;
	}
	
	if(gas_dt!=null && trim(gas_dt) != "" && gen_dt!=null && trim(gen_dt) != "")
	{
		var value = compareDate(gas_dt,gen_dt);
	  	if(value==1)
	  	{
	    	msg += "Gen Date should be >= Gas Day!\n";
	    	flag = false;
	  	}
	}
	
	if(qty_mmbtu!=null && qty_mmbtu!=undefined)
	{
		if(qty_mmbtu.length!=undefined)
		{
			for(var i=0;i<qty_mmbtu.length; i++)
			{
				if(trim(qty_mmbtu[i].value)=="")
				{
					msg+="Enter Meter Qty MMBTU(Row#"+(parseInt(i)+1)+")!\n";
					flag = false;
				}
				else
				{
					if(parseFloat(qty_mmbtu[i].value)>0)
					{
						if(define_ncv[i].value==null || trim(define_ncv[i].value)=='')
						{
							msg += "NCV Of Defined Heat Value Field Can Not Be Blank !!!\n";
							flag = false;
						}
						else
						{
							if(parseFloat(define_ncv[i].value)<=0.0001)
							{
								msg += "NCV Of Defined Heat Value Field Has To Be Greater Than 0 (Zero) !!!\n";
								flag = false;
							}
						}
					}
				}
				if(trim(qty_scm[i].value)=="")
				{
					msg+="Enter Meter Qty SCM(Row#"+(parseInt(i)+1)+")!\n";
					flag = false;
				}
				if(!checkNumberLength(calc_gcv[i],"9","4"))
				{
					msg+="The Required  Format..(5 ,4) for Calculated GCV(Row#"+(parseInt(i)+1)+")!\n";
					flag = false;
				}
				if(!checkNumberLength(calc_ncv[i],"9","4"))
				{
					msg+="The Required  Format..(5 ,4) for Calculated NCV(Row#"+(parseInt(i)+1)+")!\n";
					flag = false;
				}
				if(!checkNumberLength(define_gcv[i],"9","4"))
				{
					msg+="The Required  Format..(5 ,4) for Define GCV(Row#"+(parseInt(i)+1)+")!\n";
					flag = false;
				}
				if(!checkNumberLength(define_ncv[i],"9","4"))
				{
					msg+="The Required  Format..(5 ,4) for Define NCV(Row#"+(parseInt(i)+1)+")!\n";
					flag = false;
				}
			}
		}
		else
		{
			if(trim(qty_mmbtu.value)=="")
			{
				msg+="Enter Meter Qty MMBTU(Row#1)!\n";
				flag = false;
			}
			else
			{
				if(parseFloat(qty_mmbtu.value)>0)
				{
					if(define_ncv.value==null || trim(define_ncv.value)=='')
					{
						msg += "NCV Of Defined Heat Value Field Can Not Be Blank!\n";
						flag = false;
					}
					else
					{
						if(parseFloat(define_ncv.value)<=0.0001)
						{
							msg += "NCV Of Defined Heat Value Field Has To Be Greater Than 0 (Zero)!\n";
							flag = false;
						}
					}
				}
			}
			if(trim(qty_scm.value)=="")
			{
				msg+="Enter Meter Qty SCM(Row#1)!\n";
				flag = false;
			}
			
			if(!checkNumberLength(calc_gcv,"9","4"))
			{
				msg+="The Required  Format..(5 ,4) for Calculated GCV(Row#1)!\n";
				flag = false;
			}
			if(!checkNumberLength(calc_ncv,"9","4"))
			{
				msg+="The Required  Format..(5 ,4) for Calculated NCV(Row#1)!\n";
				flag = false;
			}
			if(!checkNumberLength(define_gcv,"9","4"))
			{
				msg+="The Required  Format..(5 ,4) for Define GCV(Row#1)!\n";
				flag = false;
			}
			if(!checkNumberLength(define_ncv,"9","4"))
			{
				msg+="The Required  Format..(5 ,4) for Define NCV(Row#1)!\n";
				flag = false;
			}
		}
	}
	if(flag)
	{
		var a = confirm("Do you want to Submit Meter Ticket Reading?");
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

function checkNumberLength(obj,a,b)
{ 
	var c = parseInt(a)-parseInt(b);
	
	var fieldValue=obj.value;
    
    var len = 0;
    
    var str = fieldValue.substring(0,fieldValue.indexOf('.')).length;
	
	if(str == 0)
	{
		len = fieldValue.length;
	}
	else
	{
		len = str;
	}
	
	var msg="";
	var flag=true;
    
    if(obj.value!="" && obj.value!=null && obj.value!=' ')
    {
		if((parseInt(len) > parseInt(c)))
		{
			msg="Enter In the Required  Format.."+'('+c+' ,'+b+' )';
			flag = false;
		}
		else
		{
			var decallowed = b;  // how many decimals are allowed?
        
        	if(isNaN(fieldValue) || fieldValue == "")
        	{
        		msg="Enter In the Required  Format.."+'('+c+' ,'+b+' )';
        		flag = false;
        	}
      		else
      		{
         		if(fieldValue.indexOf('.') == -1) 
		    	{
		    		fieldValue += ".";
         		}
         	
         		dectext = fieldValue.substring(fieldValue.indexOf('.')+1, fieldValue.length);
         	
         		if(parseInt(dectext.length) > parseInt(decallowed))
            	{
         			msg="Enter In the Required  Format.."+'('+c+' ,'+b+' )';
         			flag = false;
            	}
         		else
         		{
              		flag=true;
            	}
        	}
		}
   	}
    
    return flag;
}

function calculateQty(index)
{
	var meter_nm = document.getElementById("meter_nm"+index)
	
	var qty_mmbtu = document.getElementById("qty_mmbtu"+index)
	var qty_scm = document.getElementById("qty_scm"+index)
	
	var reconcil_qty_mmbtu = document.getElementById("reconcil_qty_mmbtu"+index)
	var reconcil_qty_scm = document.getElementById("reconcil_qty_scm"+index)
	
	var calc_gcv = document.getElementById("calc_gcv"+index)
	var calc_ncv = document.getElementById("calc_ncv"+index)
	var define_gcv = document.getElementById("define_gcv"+index)
	var define_ncv = document.getElementById("define_ncv"+index)
	
	//------------------------------------------------------------------------------------------------
	//Hardcoded value used in calculation and copied from FMS-HP20231001
	//var multiplying_factor = 0.252*1000000;
	//var deviding_factor = 1.11;
	var multiplying_factor = document.forms[0].multiplyFactor.value;
	var deviding_factor = document.forms[0].dividFactor.value;
	var round_upto_digits = 4;
	var round_upto_digits_2 = 12;
	//------------------------------------------------------------------------------------------------
	
	var gcv=0;
	var ncv=0;
	
	var total_qty_mmbtu = document.getElementById("total_qty_mmbtu"+index)
	var total_qty_scm = document.getElementById("total_qty_scm"+index)
	
	if((qty_mmbtu.value != null && trim(qty_mmbtu.value) != "") && (reconcil_qty_mmbtu.value != null && trim(reconcil_qty_mmbtu.value) != ""))
	{
		total_qty_mmbtu.value=round((parseFloat(qty_mmbtu.value) + parseFloat(reconcil_qty_mmbtu.value)),2)	
	}
	else if((qty_mmbtu.value != null && trim(qty_mmbtu.value) != ""))
	{
		total_qty_mmbtu.value=round(parseFloat(qty_mmbtu.value),2)
	}
	else if((reconcil_qty_mmbtu.value != null && trim(reconcil_qty_mmbtu.value) != ""))
	{
		total_qty_mmbtu.value=round(parseFloat(reconcil_qty_mmbtu.value),2)
	}
	
	if((qty_scm.value != null && trim(qty_scm.value) != "") && (reconcil_qty_scm.value != null && trim(reconcil_qty_scm.value) != ""))
	{
		total_qty_scm.value=round((parseFloat(qty_scm.value) + parseFloat(reconcil_qty_scm.value)),2)	
	}
	else if((qty_scm.value != null && trim(qty_scm.value) != ""))
	{
		total_qty_scm.value=round(parseFloat(qty_scm.value),2)
	}
	else if((reconcil_qty_scm.value != null && trim(reconcil_qty_scm.value) != ""))
	{
		total_qty_scm.value=round(parseFloat(reconcil_qty_scm.value),2)
	}
	
	if((total_qty_mmbtu.value != null && trim(total_qty_mmbtu.value) != "") && (total_qty_scm.value != null && trim(total_qty_scm.value) != ""))
	{
		if(parseFloat(total_qty_scm.value)>0)
		{
			gcv = (parseFloat(total_qty_mmbtu.value)*multiplying_factor)/parseFloat(total_qty_scm.value);
			ncv = gcv/deviding_factor;
			
			calc_gcv.value = ""+round(gcv,round_upto_digits);
			calc_ncv.value = ""+round(ncv,round_upto_digits);
			
			define_gcv.value = ""+round(gcv,round_upto_digits);
			define_ncv.value = ""+round(ncv,round_upto_digits);
			
			/* if(parseFloat(gcv) > 9999.9999)
			{
				var a = confirm(""+meter_nm.value+" : Calculated GCV = "+round(gcv,round_upto_digits)+"\n\nAre you sure you want to Proceed?");
			} */
		}
		else
		{
			if(parseFloat(qty_scm.value)==0)
			{
				calc_gcv.value = "0.0000";
				calc_ncv.value = "0.0000";
			}
			else
			{
				alert("Please Verify That SCM Value For "+meter_nm.value+" Should Be Greater Than Or Equal To 0 !!!");
				qty_scm.value = "0.0000";
				calc_gcv.value = "0.0000";
				calc_ncv.value = "0.0000";
			}
		}
	}
	else
	{
		calc_gcv.value = "0.0000";
		calc_ncv.value = "0.0000";
	}
}

function totalQty()
{
	var sub_index = document.forms[0].sub_index;
	
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	var reconcil_qty_mmbtu = document.forms[0].reconcil_qty_mmbtu;
	var reconcil_qty_scm = document.forms[0].reconcil_qty_scm;
	var total_qty_mmbtu = document.forms[0].total_qty_mmbtu;
	var total_qty_scm = document.forms[0].total_qty_scm;
	var calc_gcv = document.forms[0].calc_gcv;
	var calc_ncv = document.forms[0].calc_ncv;
	var define_gcv = document.forms[0].define_gcv;
	var define_ncv = document.forms[0].define_ncv;
	
	var tot_qty_mmbtu = document.forms[0].tot_qty_mmbtu;
	var tot_qty_scm = document.forms[0].tot_qty_scm;
	var tot_reconcil_qty_mmbtu = document.forms[0].tot_reconcil_qty_mmbtu;
	var tot_reconcil_qty_scm = document.forms[0].tot_reconcil_qty_scm;
	var tot_total_qty_mmbtu = document.forms[0].tot_total_qty_mmbtu;
	var tot_total_qty_scm = document.forms[0].tot_total_qty_scm;
	var tot_calc_gcv = document.forms[0].tot_calc_gcv;
	var tot_calc_ncv = document.forms[0].tot_calc_ncv;
	var tot_define_gcv = document.forms[0].tot_define_gcv;
	var tot_define_ncv = document.forms[0].tot_define_ncv;
	
	var tot_mmbtu=parseFloat("0");
	var tot_scm=parseFloat("0");
	var tot_recon_mmbtu=parseFloat("0");
	var tot_recon_scm=parseFloat("0");
	var tot_total=parseFloat("0");
	var tot_total_scm=parseFloat("0");
	var tot_c_gcv=parseFloat("0");
	var tot_c_ncv=parseFloat("0");
	var tot_d_gcv=parseFloat("0");
	var tot_d_ncv=parseFloat("0");
	
	//------------------------------------------------------------------------------------------------
	//Hardcoded value used in calculation and copied from FMS-HP20231001
	//var multiplying_factor = 0.252*1000000;
	//var deviding_factor = 1.11;
	var multiplying_factor = document.forms[0].multiplyFactor.value;
	var deviding_factor = document.forms[0].dividFactor.value;
	var round_upto_digits = 4;
	var round_upto_digits_2 = 12;
	//------------------------------------------------------------------------------------------------
	var gcv=0;
	var ncv=0;
	
	var i=0;
	var m=0;
	var c_count_non_zero_gcv=0;
	var c_count_non_zero_ncv=0;
	var d_count_non_zero_gcv=0;
	var d_count_non_zero_ncv=0;
	if(sub_index!=null && sub_index!=undefined)
	{
		if(sub_index.length!=undefined)
		{
			for(var j=0; j<sub_index.length; j++)
			{
				var subIndex = parseInt(sub_index[j].value);
				
				tot_mmbtu=parseFloat("0");
				tot_scm=parseFloat("0");
				tot_recon_mmbtu=parseFloat("0");
				tot_recon_scm=parseFloat("0");
				tot_total=parseFloat("0");
				tot_total_scm=parseFloat("0");
				tot_c_gcv=parseFloat("0");
				tot_c_ncv=parseFloat("0");
				tot_d_gcv=parseFloat("0");
				tot_d_ncv=parseFloat("0");
				m=0;
				c_count_non_zero_gcv=0;
				c_count_non_zero_ncv=0;
				d_count_non_zero_gcv=0;
				d_count_non_zero_ncv=0;
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
						if(trim(reconcil_qty_mmbtu[i].value) != "")
						{
							tot_recon_mmbtu = tot_recon_mmbtu + parseFloat(reconcil_qty_mmbtu[i].value)
						}
						if(trim(reconcil_qty_scm[i].value) != "")
						{
							tot_recon_scm = tot_recon_scm + parseFloat(reconcil_qty_scm[i].value)
						}
						if(trim(total_qty_mmbtu[i].value) != "")
						{
							tot_total = tot_total + parseFloat(total_qty_mmbtu[i].value)
						}
						if(trim(total_qty_scm[i].value) != "")
						{
							tot_total_scm = tot_total_scm + parseFloat(total_qty_scm[i].value)
						}
						if(trim(calc_gcv[i].value) != "")
						{
							tot_c_gcv = tot_c_gcv + parseFloat(calc_gcv[i].value)
							
							if(parseFloat(calc_gcv[i].value) > 0)
							{
								c_count_non_zero_gcv+=1;
							}
						}
						if(trim(calc_ncv[i].value) != "")
						{
							tot_c_ncv = tot_c_ncv + parseFloat(calc_ncv[i].value)
							
							if(parseFloat(calc_ncv[i].value) > 0)
							{
								c_count_non_zero_ncv+=1;
							}
						}
						if(trim(define_gcv[i].value) != "")
						{
							tot_d_gcv = tot_d_gcv + parseFloat(define_gcv[i].value)
							
							if(parseFloat(define_gcv[i].value) > 0)
							{
								d_count_non_zero_gcv+=1;
							}
						}
						if(trim(define_ncv[i].value) != "")
						{
							tot_d_ncv = tot_d_ncv + parseFloat(define_ncv[i].value)
							
							if(parseFloat(define_ncv[i].value) > 0)
							{
								d_count_non_zero_ncv+=1;
							}
						}
						
						if(parseInt(m)==parseInt(subIndex))
						{
							i=parseInt(i)+1;
							break;
						}
					}
				}
				
				tot_qty_mmbtu[j].value=round(parseFloat(tot_mmbtu),2)
				tot_qty_scm[j].value =round(parseFloat(tot_scm),2)
				tot_reconcil_qty_mmbtu[j].value=round(parseFloat(tot_recon_mmbtu),2)
				tot_reconcil_qty_scm[j].value=round(parseFloat(tot_recon_scm),2)
				tot_total_qty_mmbtu[j].value=round(parseFloat(tot_total),2)
				tot_total_qty_scm[j].value=round(parseFloat(tot_total_scm),2)
				
				if(parseFloat(tot_total_scm)>0)
				{
					gcv = (parseFloat(tot_total)*multiplying_factor)/parseFloat(tot_total_scm);
					ncv = gcv/deviding_factor;
					
					tot_calc_gcv[j].value = ""+round(gcv,round_upto_digits);
					tot_calc_ncv[j].value = ""+round(ncv,round_upto_digits);
					
					tot_define_gcv[j].value = ""+round(gcv,round_upto_digits);
					tot_define_ncv[j].value = ""+round(ncv,round_upto_digits);
				}
				else
				{
					tot_calc_gcv[j].value="0.0000"
					tot_calc_ncv[j].value="0.0000"
					
					tot_define_gcv[j].value="0.0000"
					tot_define_ncv[j].value="0.0000"
				}
				
				/*if(c_count_non_zero_gcv > 0)
				{
					var temp = parseFloat(tot_c_gcv) / parseInt(c_count_non_zero_gcv)
					tot_calc_gcv[j].value=round(parseFloat(temp),4)
				}
				else
				{
					tot_calc_gcv[j].value="0.0000"
				}
				if(c_count_non_zero_ncv > 0)
				{
					var temp = parseFloat(tot_c_ncv) / parseInt(c_count_non_zero_ncv)
					tot_calc_ncv[j].value=round(parseFloat(temp),4)
				}
				else
				{
					tot_calc_ncv[j].value="0.0000"
				}
				if(d_count_non_zero_gcv > 0)
				{
					var temp = parseFloat(tot_d_gcv) / parseInt(d_count_non_zero_gcv)
					tot_define_gcv[j].value=round(parseFloat(temp),4)
				}
				else
				{
					tot_define_gcv[j].value="0.0000"
				}
				if(d_count_non_zero_ncv > 0)
				{
					var temp = parseFloat(tot_d_ncv) / parseInt(d_count_non_zero_ncv)
					tot_define_ncv[j].value=round(parseFloat(temp),4)
				}
				else
				{
					tot_define_ncv[j].value="0.0000"
				}*/
			}
		}
		else
		{
			var subIndex = parseInt(sub_index.value);
			m=0;
			if(qty_mmbtu.length!=undefined)
			{
				tot_mmbtu=parseFloat("0");
				tot_scm=parseFloat("0");
				tot_recon_mmbtu=parseFloat("0");
				tot_recon_scm=parseFloat("0");
				tot_total=parseFloat("0");
				tot_total_scm=parseFloat("0");
				tot_c_gcv=parseFloat("0");
				tot_c_ncv=parseFloat("0");
				tot_d_gcv=parseFloat("0");
				tot_d_ncv=parseFloat("0");
				
				c_count_non_zero_gcv=0;
				c_count_non_zero_ncv=0;
				d_count_non_zero_gcv=0;
				d_count_non_zero_ncv=0;
				
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
					if(trim(reconcil_qty_mmbtu[i].value) != "")
					{
						tot_recon_mmbtu = tot_recon_mmbtu + parseFloat(reconcil_qty_mmbtu[i].value)
					}
					if(trim(reconcil_qty_scm[i].value) != "")
					{
						tot_recon_scm = tot_recon_scm + parseFloat(reconcil_qty_scm[i].value)
					}
					if(trim(total_qty_mmbtu[i].value) != "")
					{
						tot_total = tot_total + parseFloat(total_qty_mmbtu[i].value)
					}
					if(trim(total_qty_scm[i].value) != "")
					{
						tot_total_scm = tot_total_scm + parseFloat(total_qty_scm[i].value)
					}
					if(trim(calc_gcv[i].value) != "")
					{
						tot_c_gcv = tot_c_gcv + parseFloat(calc_gcv[i].value)
						
						if(parseFloat(calc_gcv[i].value) > 0)
						{
							c_count_non_zero_gcv+=1;
						}
					}
					if(trim(calc_ncv[i].value) != "")
					{
						tot_c_ncv = tot_c_ncv + parseFloat(calc_ncv[i].value)
						
						if(parseFloat(calc_ncv[i].value) > 0)
						{
							c_count_non_zero_ncv+=1;
						}
					}
					if(trim(define_gcv[i].value) != "")
					{
						tot_d_gcv = tot_d_gcv + parseFloat(define_gcv[i].value)
						
						if(parseFloat(define_gcv[i].value) > 0)
						{
							d_count_non_zero_gcv+=1;
						}
					}
					if(trim(define_ncv[i].value) != "")
					{
						tot_d_ncv = tot_d_ncv + parseFloat(define_ncv[i].value)
						
						if(parseFloat(define_ncv[i].value) > 0)
						{
							d_count_non_zero_ncv+=1;
						}
					}
					
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
				if(trim(reconcil_qty_mmbtu.value) != "")
				{
					tot_recon_mmbtu = tot_recon_mmbtu + parseFloat(reconcil_qty_mmbtu.value)
				}
				if(trim(reconcil_qty_scm.value) != "")
				{
					tot_recon_scm = tot_recon_scm + parseFloat(reconcil_qty_scm.value)
				}
				if(trim(total_qty_mmbtu.value) != "")
				{
					tot_total = tot_total + parseFloat(total_qty_mmbtu.value)
				}
				if(trim(total_qty_scm.value) != "")
				{
					tot_total_scm = tot_total_scm + parseFloat(total_qty_scm.value)
				}
				if(trim(calc_gcv.value) != "")
				{
					tot_c_gcv = tot_c_gcv + parseFloat(calc_gcv.value)
					
					if(parseFloat(calc_gcv.value) > 0)
					{
						c_count_non_zero_gcv+=1;
					}
				}
				if(trim(calc_ncv.value) != "")
				{
					tot_c_ncv = tot_c_ncv + parseFloat(calc_ncv.value)
					
					if(parseFloat(calc_ncv.value) > 0)
					{
						c_count_non_zero_ncv+=1;
					}
				}
				if(trim(define_gcv.value) != "")
				{
					tot_d_gcv = tot_d_gcv + parseFloat(define_gcv.value)
					
					if(parseFloat(define_gcv.value) > 0)
					{
						d_count_non_zero_gcv+=1;
					}
				}
				if(trim(define_ncv.value) != "")
				{
					tot_d_ncv = tot_d_ncv + parseFloat(define_ncv.value)
					
					if(parseFloat(define_ncv.value) > 0)
					{
						d_count_non_zero_ncv+=1;
					}
				}
			}
			
			tot_qty_mmbtu.value=round(parseFloat(tot_mmbtu),2)
			tot_qty_scm.value =round(parseFloat(tot_scm),2)
			tot_reconcil_qty_mmbtu.value=round(parseFloat(tot_recon_mmbtu),2)
			tot_reconcil_qty_scm.value=round(parseFloat(tot_recon_scm),2)
			tot_total_qty_mmbtu.value=round(parseFloat(tot_total),2)
			tot_total_qty_scm.value=round(parseFloat(tot_total_scm),2)
			
			if(parseFloat(tot_total_scm)>0)
			{
				gcv = (parseFloat(tot_total)*multiplying_factor)/parseFloat(tot_total_scm);
				ncv = gcv/deviding_factor;
				
				tot_calc_gcv.value = ""+round(gcv,round_upto_digits);
				tot_calc_ncv.value = ""+round(ncv,round_upto_digits);
				
				tot_define_gcv.value = ""+round(gcv,round_upto_digits);
				tot_define_ncv.value = ""+round(ncv,round_upto_digits);
			}
			else
			{
				tot_calc_gcv.value="0.0000"
				tot_calc_ncv.value="0.0000"
				
				tot_define_gcv.value="0.0000"
				tot_define_ncv.value="0.0000"
			}
			
			/*if(c_count_non_zero_gcv > 0)
			{
				var temp = parseFloat(tot_c_gcv) / parseInt(c_count_non_zero_gcv)
				tot_calc_gcv.value=round(parseFloat(temp),4)
			}
			else
			{
				tot_calc_gcv.value="0.0000"
			}
			if(c_count_non_zero_ncv > 0)
			{
				var temp = parseFloat(tot_c_ncv) / parseInt(c_count_non_zero_ncv)
				tot_calc_ncv.value=round(parseFloat(temp),4)
			}
			else
			{
				tot_calc_ncv.value="0.0000"
			}
			if(d_count_non_zero_gcv > 0)
			{
				var temp = parseFloat(tot_d_gcv) / parseInt(d_count_non_zero_gcv)
				tot_define_gcv.value=round(parseFloat(temp),4)
			}
			else
			{
				tot_define_gcv.value="0.0000"
			}
			if(d_count_non_zero_ncv > 0)
			{
				var temp = parseFloat(tot_d_ncv) / parseInt(d_count_non_zero_ncv)
				tot_define_ncv.value=round(parseFloat(temp),4)
			}
			else
			{
				tot_define_ncv.value="0.0000"
			}*/
		}
	}
}

function fillZero()
{
	var qty_mmbtu = document.forms[0].qty_mmbtu;
	var qty_scm = document.forms[0].qty_scm;
	
	if(qty_mmbtu!=null && qty_mmbtu!=undefined)
	{
		if(qty_mmbtu.length!=undefined)
		{
			for(var i=0; i<qty_mmbtu.length; i++)
			{
				if(trim(qty_mmbtu[i].value) == "")
				{
					qty_mmbtu[i].value="0.00";
				}
				if(trim(qty_scm[i].value) == "")
				{
					qty_scm[i].value="0.00";
				}
			}
		}
		else
		{
			if(trim(qty_mmbtu.value) == "")
			{
				qty_mmbtu.value="0.00";
			}
			if(trim(qty_scm.value) == "")
			{
				qty_scm.value="0.00";
			}
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DataBean_ContractMgmt" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String prevdate = utildate.getPreviousDate();

String gas_dt = request.getParameter("gas_dt")==null?prevdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "1");

cont_mgmt.setCallFlag("METER_TICKET_READING");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setGas_dt(gas_dt);
cont_mgmt.init();

String gen_time = cont_mgmt.getGen_time();

String multiplyFactor = cont_mgmt.getMultiplyFactor();
String dividFactor = cont_mgmt.getDividFactor();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_STATUS = cont_mgmt.getVCOUNTERPARTY_STATUS();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = cont_mgmt.getVCOUNTERPARTY_PLANT_ABBR();

Vector VINDEX = cont_mgmt.getVINDEX();
Vector VSUB_INDEX = cont_mgmt.getVSUB_INDEX();

Vector VMETER_SEQ = cont_mgmt.getVMETER_SEQ();
Vector VMETER_ID = cont_mgmt.getVMETER_ID();
Vector VMETER_REF  = cont_mgmt.getVMETER_REF();
Vector VMETER_STATUS  = cont_mgmt.getVMETER_STATUS();
Vector VMETER_EFF_DT  = cont_mgmt.getVMETER_EFF_DT();
Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();
Vector VQTY_SCM = cont_mgmt.getVQTY_SCM();
Vector VQTY_BTU = cont_mgmt.getVQTY_BTU();
Vector VRECONCIL_QTY_MMBTU = cont_mgmt.getVRECONCIL_QTY_MMBTU();
Vector VRECONCIL_QTY_SCM = cont_mgmt.getVRECONCIL_QTY_SCM();
Vector VRECONCIL_QTY_BTU = cont_mgmt.getVRECONCIL_QTY_BTU();
Vector VTOTAL_QTY_MMBTU = cont_mgmt.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_SCM = cont_mgmt.getVTOTAL_QTY_SCM();
Vector VCALC_GCV = cont_mgmt.getVCALC_GCV();
Vector VCALC_NCV = cont_mgmt.getVCALC_NCV();
Vector VDEFINE_GCV = cont_mgmt.getVDEFINE_GCV();
Vector VDEFINE_NCV = cont_mgmt.getVDEFINE_NCV();
Vector VNOM_COLOR = cont_mgmt.getVNOM_COLOR();

Vector VTOT_QTY_MMBTU = cont_mgmt.getVTOT_QTY_MMBTU();
Vector VTOT_QTY_SCM = cont_mgmt.getVTOT_QTY_SCM();
Vector VTOT_QTY_BTU = cont_mgmt.getVTOT_QTY_BTU();
Vector VTOT_RECONCIL_QTY_MMBTU = cont_mgmt.getVTOT_RECONCIL_QTY_MMBTU();
Vector VTOT_RECONCIL_QTY_SCM = cont_mgmt.getVTOT_RECONCIL_QTY_SCM();
Vector VTOT_RECONCIL_QTY_BTU = cont_mgmt.getVTOT_RECONCIL_QTY_BTU();
Vector VTOT_TOTAL_QTY_MMBTU = cont_mgmt.getVTOT_TOTAL_QTY_MMBTU();
Vector VTOT_TOTAL_QTY_SCM = cont_mgmt.getVTOT_TOTAL_QTY_SCM();
Vector VTOT_CALC_GCV = cont_mgmt.getVTOT_CALC_GCV();
Vector VTOT_CALC_NCV = cont_mgmt.getVTOT_CALC_NCV();
Vector VTOT_DEFINE_GCV = cont_mgmt.getVTOT_DEFINE_GCV();
Vector VTOT_DEFINE_NCV = cont_mgmt.getVTOT_DEFINE_NCV();

Vector VSELLER_NOM = cont_mgmt.getVSELLER_NOM();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_ContractMgmt">

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
					    	Meter Ticket Reading
					    </div>
		    			<input type="button" class="btn btn-info btn-sm select_btn" value="FILL ZERO" style="font-weight: bold;" onclick="fillZero();">
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
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Gen Time</b></label>
								</div>
			    				<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="gen_time" id="gen_time" value="<%=gen_time%>" maxLength="5" 
			      						onblur="validateTime(this);" onchange="validateTime(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-clock-o fa-lg"></i></span>
		      						</div>
	      						</div>
				    		</div>
						</div>
					</div>
					<%int j=0,k=0,l=0,m=0;
					for(int i=0; i<VCOUNTERPARTY_CD.size(); i++)
					{ 
						String countpty_cd=""+VCOUNTERPARTY_CD.elementAt(i);
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
					%>
					&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VCOUNTERPARTY_ABBR.elementAt(i)%>
						<span 
						<%if(VCOUNTERPARTY_STATUS.elementAt(i).equals("N")){ %>class='alert alert-danger' title="Counterparty Deactive "
						<%}else if(VCOUNTERPARTY_STATUS.elementAt(i).equals("E")){ %>class='alert alert-warning'
						<%} %>
						><b> <%if(VCOUNTERPARTY_STATUS.elementAt(i).equals("N")){ %> De-active 
						<%}else if(VCOUNTERPARTY_STATUS.elementAt(i).equals("E")){ %> E-Rate
						<%} %> </b>
						</span>
						</label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
						<%k=0;
						if(index > 0){ %>
							<%for(j=j;j<VCOUNTERPARTY_PLANT_SEQ.size(); j++) 
							{
								String countpty_plant_seq=""+VCOUNTERPARTY_PLANT_SEQ.elementAt(j);
								int sub_index = Integer.parseInt(""+VSUB_INDEX.elementAt(j));
								k+=1;
							%>
								<input type="hidden" name="sub_index" value="<%=sub_index%>">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading<%=l%>">
    										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
								    			<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(j)%>
								    			<%if(Double.parseDouble(""+VSELLER_NOM.elementAt(j)) > 0 && Double.parseDouble(""+VTOT_QTY_MMBTU.elementAt(j)) == 0){%>
								    			<font style="color:red;">
													&nbsp;&nbsp;&nbsp;<i class="fa fa-circle fa-lg"></i>
												</font>
												<%}else if(Double.parseDouble(""+VTOT_QTY_MMBTU.elementAt(j)) > 0){%>
								    			<font style="color:#00cc00;">
													&nbsp;&nbsp;&nbsp;<i class="fa fa-circle fa-lg"></i>
												</font>
												<%}%>
								      		</button>
								    	</h2>
								    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
								      		<div class="accordion-body accor-body">
								        		<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover"
														<%if(VCOUNTERPARTY_STATUS.elementAt(i).equals("N")){ %>style="pointer-events: none"<%} %>>
															<thead>
																<tr>
																	<th rowspan="2">Meter#</th>
																	<th colspan="2">Meter Qty</th>
																	<th colspan="2">Reconciliation Qty</th>
																	<th colspan="2">Total Qty</th>
																	<th colspan="2">Calculated Heat Value (KCal/SCM)</th>
																	<th colspan="2">Define Heat Value (KCal/SCM)</th>
																</tr>
																<tr>
																	<th>MMBTU</th>
																	<th>SCM</th>
																	<th>MMBTU</th>
																	<th>SCM</th>
																	<th>MMBTU</th>
																	<th>SCM</th>
																	<th>GCV</th>
																	<th>NCV</th>
																	<th>GCV</th>
																	<th>NCV</th>
																</tr>
															</thead>
															<tbody>
																<%m=0;
																if(sub_index>0){ %>
																	<%for(l=l; l<VMETER_SEQ.size(); l++)
																	{ 
																		m+=1;
																	%>
																		<tr <%if(VMETER_STATUS.elementAt(l).equals("N")){ %>style="pointer-events: none" <%} %>>
																			<td
																			<%if(VMETER_STATUS.elementAt(l).equals("N")){ %>style="color:red; pointer-events: auto" title="In-Active Meter since <%=VMETER_EFF_DT.elementAt(l)%>"<%} %>
																			>
																				<%=VMETER_ID.elementAt(l)%> (<%=VMETER_REF.elementAt(l)%>)
																				<input type="hidden" name="counterparty_cd" value="<%=countpty_cd%>">
																				<input type="hidden" name="plant_seq" value="<%=countpty_plant_seq%>">
																				<input type="hidden" name="meter_seq" value="<%=VMETER_SEQ.elementAt(l)%>">
																				<input type="hidden" name="meter_type" value="R">
																				<input type="hidden" name="meter_nm" id="meter_nm<%=l%>" value="<%=VMETER_ID.elementAt(l)%> (<%=VMETER_REF.elementAt(l)%>)">
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="qty_mmbtu" id="qty_mmbtu<%=l%>" value="<%=VQTY_MMBTU.elementAt(l)%>" 
																					style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,10,2);calculateQty('<%=l%>');totalQty();">
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="qty_scm" id="qty_scm<%=l%>" value="<%=VQTY_SCM.elementAt(l)%>" 
																					style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,10,2);calculateQty('<%=l%>');totalQty();">
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="reconcil_qty_mmbtu" id="reconcil_qty_mmbtu<%=l%>" value="<%=VRECONCIL_QTY_MMBTU.elementAt(l)%>" 
																					style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,10,2);calculateQty('<%=l%>');totalQty();">
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="reconcil_qty_scm" id="reconcil_qty_scm<%=l%>" value="<%=VRECONCIL_QTY_SCM.elementAt(l)%>" 
																					style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,10,2);calculateQty('<%=l%>');totalQty();">
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="total_qty_mmbtu" id="total_qty_mmbtu<%=l%>" value="<%=VTOTAL_QTY_MMBTU.elementAt(l)%>" 
																					style="text-align:right;background:<%//=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,10,2);" readOnly>
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="total_qty_scm" id="total_qty_scm<%=l%>" value="<%=VTOTAL_QTY_SCM.elementAt(l)%>" 
																					style="text-align:right;background:<%//=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,10,2);" readOnly>
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="calc_gcv" id="calc_gcv<%=l%>" value="<%=VCALC_GCV.elementAt(l)%>" 
																					style="text-align:right;background:<%//=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);" readOnly>
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="calc_ncv" id="calc_ncv<%=l%>" value="<%=VCALC_NCV.elementAt(l)%>" 
																					style="text-align:right;background:<%//=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);" readOnly>
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="define_gcv" id="define_gcv<%=l%>" value="<%=VDEFINE_GCV.elementAt(l)%>" 
																					style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);totalQty();">
																				</div>
																			</td>
																			<td align="center">
																				<div style="width:100px;">
																					<input type="text" class="form-control form-control-sm" name="define_ncv" id="define_ncv<%=l%>" value="<%=VDEFINE_NCV.elementAt(l)%>" 
																					style="text-align:right;background:<%=VNOM_COLOR.elementAt(l)%>" onblur="checkNumber1(this,9,4);totalQty();">
																				</div>
																			</td>
																		</tr>
																		<%if(m==sub_index)
																		{%>
																			<tr>
																				<td align="right"><b>Total (<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(j)%>)</b></td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_qty_mmbtu" value="<%=VTOT_QTY_MMBTU.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_qty_scm" value="<%=VTOT_QTY_SCM.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_reconcil_qty_mmbtu" value="<%=VTOT_RECONCIL_QTY_MMBTU.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_reconcil_qty_scm" value="<%=VTOT_RECONCIL_QTY_SCM.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_total_qty_mmbtu" value="<%=VTOT_TOTAL_QTY_MMBTU.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_total_qty_scm" value="<%=VTOT_TOTAL_QTY_SCM.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_calc_gcv" value="<%=VTOT_CALC_GCV.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_calc_ncv" value="<%=VTOT_CALC_NCV.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_define_gcv" value="<%=VTOT_DEFINE_GCV.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
																				</td>
																				<td align="center">
																					<div style="width:100px;">
																						<input type="text" class="form-control form-control-sm" name="tot_define_ncv" value="<%=VTOT_DEFINE_NCV.elementAt(j)%>" style="text-align:right;" readOnly>
																					</div>
																				</td>
																			</tr>
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
				<%if(VCOUNTERPARTY_CD.size() > 0){ %>
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
				<%} %>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="METER_TICKET_READING">

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

<input type="hidden" name="multiplyFactor" value="<%=multiplyFactor%>">
<input type="hidden" name="dividFactor" value="<%=dividFactor%>">

</form>
</body>
</html>