<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var month_to = document.forms[0].month_to.value;
	var year_to = document.forms[0].year_to.value;
	//var segment = document.forms[0].segment.value;
	var paid_status = document.forms[0].paid_status.value;
	var tmp_month = document.forms[0].tmp_from_month.value;
	var tmp_year = document.forms[0].tmp_from_year.value;
	var tmp_month_to = document.forms[0].tmp_to_month.value;
	var tmp_year_to = document.forms[0].tmp_to_year.value;
	
	var flag=checkMonthYearRange(document.forms[0].month,document.forms[0].year,document.forms[0].month_to,document.forms[0].year_to);
	var u = document.forms[0].u.value;
	if(flag==true)
	{
		var url = "frm_dlng_receivable_tracking.jsp?u="+u+
				//"&segment="+segment+
				"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to+"&paid_status="+paid_status;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		document.forms[0].month.value=tmp_month;
		document.forms[0].year.value=tmp_year;
		document.forms[0].month_to.value=tmp_month_to;
		document.forms[0].year_to.value=tmp_year_to;
	}
}

function enable_disable(index)
{
	var chk = document.getElementById("chk"+index);
	
	var counterparty_cd = document.getElementById("counterparty_cd"+index);
	var agmt_no = document.getElementById("agmt_no"+index);
	var agmt_rev_no = document.getElementById("agmt_rev_no"+index);
	var cont_no = document.getElementById("cont_no"+index);
	var cont_rev_no = document.getElementById("cont_rev_no"+index);
	var contract_type = document.getElementById("contract_type"+index);
	var financial_year = document.getElementById("financial_year"+index);
	var invoice_seq = document.getElementById("invoice_seq"+index);
	var invoice_type = document.getElementById("invoice_type"+index);
	var type_flag = document.getElementById("type_flag"+index);
	var cargo_no = document.getElementById("cargo_no"+index);
	var invoice_no = document.getElementById("invoice_no"+index);
	var bu_state_tin = document.getElementById("bu_state_tin"+index);
	var net_payable_amt = document.getElementById("net_payable_amt"+index);
	var pay_received_amt = document.getElementById("pay_received_amt"+index);
	var total_received_amt = document.getElementById("total_received_amt"+index);
	var short_received = document.getElementById("short_received"+index);
	var pay_received_dt = document.getElementById("pay_received_dt"+index);
	var remark = document.getElementById("remark"+index);
	
	var adj_chk= document.getElementById("adj_chk_"+index);
	var adj_flag= document.getElementById("adj_flag_"+index);
	var adjust_amt= document.getElementById("adjust_amt_"+index);
	
	var hold_chk= document.getElementById("hold_chk_"+index);
	var hold_flag= document.getElementById("hold_flag_"+index);
	var hold_amt= document.getElementById("hold_amt_"+index);
	var cform_flag = document.getElementById("cform_flag_"+index);
	
	if(chk.checked)
	{
		counterparty_cd.disabled=false;
		agmt_no.disabled=false;
		agmt_rev_no.disabled=false;
		cont_no.disabled=false;
		cont_rev_no.disabled=false;
		contract_type.disabled=false;
		financial_year.disabled=false;
		invoice_seq.disabled=false;
		invoice_type.disabled=false;
		type_flag.disabled=false;
		cargo_no.disabled=false;
		invoice_no.disabled=false;
		bu_state_tin.disabled=false;
		net_payable_amt.disabled=false;
		pay_received_amt.disabled=false;
		total_received_amt.disabled=false;
		short_received.disabled=false;
		pay_received_dt.disabled=false;
		remark.disabled=false;
		
		adj_chk.disabled=false;
		adj_flag.disabled=false;
		adjust_amt.disabled=false;
		
		//hold_chk.disabled=false;
		hold_flag.disabled=false;
		hold_amt.disabled=false;
		cform_flag.disabled=false;
	}
	else
	{
		counterparty_cd.disabled=true;
		agmt_no.disabled=true;
		agmt_rev_no.disabled=true;
		cont_no.disabled=true;
		cont_rev_no.disabled=true;
		contract_type.disabled=true;
		financial_year.disabled=true;
		invoice_seq.disabled=true;
		invoice_type.disabled=true;
		type_flag.disabled=true;
		cargo_no.disabled=true;
		invoice_no.disabled=true;
		bu_state_tin.disabled=true;
		net_payable_amt.disabled=true;
		pay_received_amt.disabled=true;
		total_received_amt.disabled=true;
		short_received.disabled=true;
		pay_received_dt.disabled=true;
		remark.disabled=true;
		
		adj_chk.disabled=true;
		adj_chk.checked=false;
		
		adj_flag.disabled=true;
		adjust_amt.disabled=true;
		
		//hold_chk.disabled=true;
		hold_chk.checked=false;
		
		hold_flag.disabled=true;
		hold_amt.disabled=true;
		cform_flag.disabled=true;
	}
}

function calcPayableAmt(index,calc_flag)
{
	var gross_amt = document.getElementById("gross_amt"+index);

	var invoice_amt = document.getElementById("invoice_amt"+index);
	var net_payable_amt = document.getElementById("net_payable_amt"+index);
	var pay_received_amt = document.getElementById("pay_received_amt"+index);
	var short_received = document.getElementById("short_received"+index);
	var temp_short_received = document.getElementById("temp_short_received"+index);
	var total_received_amt = document.getElementById("total_received_amt"+index);
	var temp_total_received_amt = document.getElementById("temp_total_received_amt"+index);
	var temp_cont_map = document.getElementById("cont_map"+index);
	
	var adj_chk = document.getElementById("adj_chk_"+index);
	var adj_flag= document.getElementById("adj_flag_"+index);
	var total_adv_amt = document.getElementById("total_adv_amt_"+index);
	var adjust_amt= document.getElementById("adjust_amt_"+index);
	var cform_flag = document.getElementById("cform_flag_"+index);
	var hold_chk = document.getElementById("hold_chk_"+index);
	var hold_flag= document.getElementById("hold_flag_"+index);
	var hold_amt= document.getElementById("hold_amt_"+index);
	
	var pay_received_dt= document.getElementById("pay_received_dt"+index);
	var sysdate=document.forms[0].sysdate.value;
	
	var chk = document.forms[0].chk;
	var cont_map = document.forms[0].cont_map;
	var s_adj_chk = document.forms[0].adj_chk;
	var s_adjust_amt= document.forms[0].adjust_amt;
	var s_cform_flag= document.forms[0].cform_flag;
	var s_hold_flag= document.forms[0].hold_flag;
	var s_hold_amt= document.forms[0].hold_amt;
	
	var adv_cont_map = document.forms[0].adv_cont_map;
	var adv_cont_val= document.forms[0].adv_cont_val;
	
	if(calc_flag=="ADV")
	{
		if(adj_chk.checked)
		{	
			var short_amt=parseFloat("0");
			var adv_amt=parseFloat("0");
			
			var available_adv_amt=parseFloat("0");
			if(trim(total_adv_amt.value)!="")
			{
				available_adv_amt=round(parseFloat(total_adv_amt.value),2);
			}
			
			//
			if(adv_cont_map!=null && adv_cont_map!=undefined)
			{
				if(adv_cont_map.length!=undefined)
				{
					for(var i=0;i<adv_cont_map.length;i++)
					{
						var advContMap= adv_cont_map[i].value;
						
						if(advContMap==temp_cont_map.value)
						{
							var totAdvance=parseFloat(adv_cont_val[i].value);
							var totAllocatedAdv=parseFloat("0");
							
							if(chk!=null && chk!=undefined)
							{
								if(chk.length!=undefined)
								{
									for(var j=0;j<chk.length;j++)
									{
										if(chk[j].checked && j!=index)
										{
											var contMap=cont_map[j].value;
											if(advContMap==contMap)
											{
												if(trim(s_adjust_amt[j].value)!="" && s_adj_chk[j].checked)
												{
													totAllocatedAdv=totAllocatedAdv+parseFloat(s_adjust_amt[j].value);
													if(trim(s_hold_amt[j].value)!="" && s_hold_flag[j].value=="Y")
													{
														totAllocatedAdv=totAllocatedAdv+parseFloat(s_hold_amt[j].value);
													}
												}
											}
										}
									}
								}
								else
								{
									if(chk.checked && 0!=index)
									{
										var contMap=cont_map.value;
										if(advContMap==contMap)
										{
											if(trim(s_adjust_amt.value)!="" && s_adj_chk.checked)
											{
												totAllocatedAdv=totAllocatedAdv+parseFloat(s_adjust_amt.value);
												if(trim(s_hold_amt.value)!="" && s_hold_flag.value=="Y")
												{
													totAllocatedAdv=totAllocatedAdv+parseFloat(s_hold_amt.value);
												}
											}
										}
									}
								}
							}
							
							available_adv_amt=round(parseFloat(totAdvance) - parseFloat(totAllocatedAdv),2);
						}
					}
				}
				else
				{
					var advContMap= adv_cont_map.value;
					
					if(advContMap==temp_cont_map.value)
					{
						var totAdvance=parseFloat(adv_cont_val.value);
						var totAllocatedAdv=parseFloat("0");
						
						if(chk!=null && chk!=undefined)
						{
							if(chk.length!=undefined)
							{
								for(var j=0;j<chk.length;j++)
								{
									if(chk[j].checked && j!=index)
									{
										var contMap=cont_map[j].value;
										if(advContMap==contMap)
										{
											if(trim(s_adjust_amt[j].value)!="" && s_adj_chk[j].checked)
											{
												totAllocatedAdv=totAllocatedAdv+parseFloat(s_adjust_amt[j].value);
												if(trim(s_hold_amt[j].value)!="" && s_hold_flag[j].value=="Y")
												{
													totAllocatedAdv=totAllocatedAdv+parseFloat(s_hold_amt[j].value);
												}
											}
										}
									}
								}
							}
							else
							{
								if(chk.checked && 0!=index)
								{
									var contMap=cont_map.value;
									if(advContMap==contMap)
									{
										if(trim(s_adjust_amt.value)!="" && s_adj_chk.checked)
										{
											totAllocatedAdv=totAllocatedAdv+parseFloat(s_adjust_amt.value);
											if(trim(s_hold_amt.value)!="" && s_hold_flag.value=="Y")
											{
												totAllocatedAdv=totAllocatedAdv+parseFloat(s_hold_amt.value);
											}
										}
									}
								}
							}
						}
						
						available_adv_amt=round(parseFloat(totAdvance) - parseFloat(totAllocatedAdv),2);
					}
				}
			}
			
			if(trim(short_received.value)!="")
			{
				short_amt=round(parseFloat(short_received.value),2);
			}
			
			if(cform_flag.value=="Y")
			{
				if(trim(hold_amt.value)!="")
				{
					available_adv_amt=round(available_adv_amt-parseFloat(hold_amt.value),2)
				}
			}
			
			var temp_flag=true;
			if(parseFloat(available_adv_amt) >= parseFloat(short_amt))
			{
				temp_flag=true;
				pay_received_amt.value=round(parseFloat(short_amt),2);
				adjust_amt.value=round(parseFloat(short_amt),2);	
			}
			else if((parseFloat(available_adv_amt) < parseFloat(short_amt)) && parseFloat(available_adv_amt) > 0)
			{
				temp_flag=true;
				pay_received_amt.value=round(parseFloat(available_adv_amt),2);
				adjust_amt.value=round(parseFloat(available_adv_amt),2);
			}
			else if(parseFloat(available_adv_amt) <= 0)
			{
				temp_flag=false;
				alert("You don't have sufficient advance amount available for "+temp_cont_map.value+"!");
			}
			
			if(temp_flag)
			{
				adj_flag.value="Y";
				pay_received_amt.readOnly=true;
				pay_received_dt.value=sysdate;
				
				if(cform_flag.value=="Y")
				{
					hold_chk.checked=true;
					hold_flag.value="Y";
				}
			}
			else
			{
				adj_chk.checked=false;
				adj_flag.value="";
				pay_received_amt.readOnly=false;
				pay_received_dt.value="";
				
				hold_chk.checked=false;
				hold_flag.value="";
			}
		}
		else
		{
			adj_flag.value="";
			
			pay_received_amt.value="";
			adjust_amt.value="";
			
			pay_received_amt.readOnly=false;
			pay_received_dt.value="";
			
			hold_chk.checked=false;
			hold_flag.value="";
		}
	}
	
	//calc short receive
	if(trim(pay_received_amt.value)!="")
	{
		var total_recv;
		var short_amt;
		
		if(trim(temp_total_received_amt.value)!="")
		{
			total_recv=round(parseFloat(temp_total_received_amt.value) + parseFloat(pay_received_amt.value),2);
		}
		else
		{
			total_recv=round(parseFloat(pay_received_amt.value),2);
		}
		
		if(!isNaN(total_recv))
		{
			total_received_amt.value=round(parseFloat(total_recv),2)
		}
		else
		{
			total_received_amt.value="";
		}
		
		short_amt = round(parseFloat(net_payable_amt.value)-parseFloat(total_recv),2);
		
		if(!isNaN(short_amt))
		{
			short_received.value=round(parseFloat(short_amt),2)
		}
		else
		{
			short_received.value="";
		}
		
		if(parseFloat(pay_received_amt.value) > parseFloat(temp_short_received.value))
		{
			alert("Payment entered is more than Receivable for ROW - "+(parseInt(index)+1));
			
			short_received.value=temp_short_received.value;
			pay_received_amt.value="";
			total_received_amt.value=temp_total_received_amt.value;
		}
	}
	else
	{
		total_received_amt.value=temp_total_received_amt.value;
		
		if(trim(total_received_amt.value)!="")
		{
			var short_amt=round(parseFloat(net_payable_amt.value)-parseFloat(total_received_amt.value),2);
			
			if(!isNaN(short_amt))
			{
				short_received.value=round(parseFloat(short_amt),2)
			}
			else
			{
				short_received.value="";
			}
		}
		else
		{
			short_received.value=net_payable_amt.value;
		}
	}
}

function doSubmit()
{
	var chk = document.forms[0].chk;
	
	var cont_map = document.forms[0].cont_map;
	
	var pay_received_amt = document.forms[0].pay_received_amt;
	var pay_received_dt = document.forms[0].pay_received_dt;
	
	var adj_chk = document.forms[0].adj_chk;
	var adjust_amt= document.forms[0].adjust_amt;
	
	var adv_cont_map = document.forms[0].adv_cont_map;
	var adv_cont_val= document.forms[0].adv_cont_val;
	
	var hold_flag= document.forms[0].hold_flag;
	var hold_amt= document.forms[0].hold_amt;
	
	var msg="";
	var flag=true;
	
	var countChk=parseInt("0");
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					countChk=parseInt(countChk) + 1;
					
					if(trim(pay_received_amt[i].value) == "")
					{
						msg+="Enter Actual Received for ROW - "+(parseInt(i)+1)+"!\n";
						flag=false;
					}
					if(trim(pay_received_dt[i].value) == "")
					{
						msg+="Enter Received Date for ROW - "+(parseInt(i)+1)+"!\n";
						flag=false;
					}
				}
			}
		}
		else
		{
			if(chk.checked)
			{
				countChk=parseInt(countChk) + 1;
				
				if(trim(pay_received_amt.value) == "")
				{
					msg+="Enter Actual Received for ROW - 1!\n";
					flag=false;
				}
				if(trim(pay_received_dt.value) == "")
				{
					msg+="Enter Received Date for ROW - 1!\n";
					flag=false;
				}
			}
		}
	}
	
	if(parseInt(countChk)<=0)
	{
		alert("Please Select atleast one(1) ROW for Submit!");
	}
	else
	{
		if(adv_cont_map!=null && adv_cont_map!=undefined)
		{
			if(adv_cont_map.length!=undefined)
			{
				for(var i=0;i<adv_cont_map.length;i++)
				{
					var advContMap= adv_cont_map[i].value;
					
					var totAdvance=parseFloat(adv_cont_val[i].value);
					var totAllocatedAdv=parseFloat("0");
					
					//
					if(chk!=null && chk!=undefined)
					{
						if(chk.length!=undefined)
						{
							for(var j=0;j<chk.length;j++)
							{
								if(chk[j].checked)
								{
									var contMap=cont_map[j].value;
									if(advContMap==contMap)
									{
										if(trim(adjust_amt[j].value)!="" && adj_chk[j].checked)
										{
											totAllocatedAdv=totAllocatedAdv+parseFloat(adjust_amt[j].value);
											if(trim(hold_amt[j].value)!="" && hold_flag[j].value=="Y")
											{
												totAllocatedAdv=totAllocatedAdv+parseFloat(hold_amt[j].value);
											}
										}
									}
								}
							}
						}
						else
						{
							if(chk.checked)
							{
								var contMap=cont_map.value;
								if(advContMap==contMap)
								{
									if(trim(adjust_amt.value)!="" && adj_chk.checked)
									{
										totAllocatedAdv=totAllocatedAdv+parseFloat(adjust_amt.value);
										if(trim(hold_amt.value)!="" && hold_flag.value=="Y")
										{
											totAllocatedAdv=totAllocatedAdv+parseFloat(hold_amt.value);
										}
									}
								}
							}
						}
					}
					//
					
					if(totAllocatedAdv > totAdvance)
					{
						//msg+="Adjust Amount("+round(parseFloat(totAllocatedAdv),2)+" shouldn't be > Available Advance("+round(parseFloat(totAdvance),2)+")) for "+advContMap+"!\n";
						msg+="Available Advance Amount is "+round(parseFloat(totAdvance),2)+" for "+advContMap+"!\nCan't Adjust "+round(parseFloat(totAllocatedAdv),2)+"!";
						flag=false;
					}
				}
			}
			else
			{
				var advContMap= adv_cont_map.value;
				
				var totAdvance=parseFloat(adv_cont_val.value);
				var totAllocatedAdv=parseFloat("0");
				
				//
				if(chk!=null && chk!=undefined)
				{
					if(chk.length!=undefined)
					{
						for(var j=0;j<chk.length;j++)
						{
							if(chk[j].checked)
							{
								var contMap=cont_map[j].value;
								if(advContMap==contMap)
								{
									if(trim(adjust_amt[j].value)!="" && adj_chk[j].checked)
									{
										totAllocatedAdv=totAllocatedAdv+parseFloat(adjust_amt[j].value);
										if(trim(hold_amt[j].value)!="" && hold_flag[j].value=="Y")
										{
											totAllocatedAdv=totAllocatedAdv+parseFloat(hold_amt.value);
										}
									}
								}
							}
						}
					}
					else
					{
						if(chk.checked)
						{
							var contMap=cont_map.value;
							if(advContMap==contMap)
							{
								if(trim(adjust_amt.value)!="" && adj_chk.checked)
								{
									totAllocatedAdv=totAllocatedAdv+parseFloat(adjust_amt.value);
									if(trim(hold_amt.value)!="" && hold_flag.value=="Y")
									{
										totAllocatedAdv=totAllocatedAdv+parseFloat(hold_amt.value);
									}
								}
							}
						}
					}
				}
				//
				
				if(totAllocatedAdv > totAdvance)
				{
					//msg+="Adjust Amount("+round(parseFloat(totAllocatedAdv),2)+" shouldn't be > Available Advance("+round(parseFloat(totAdvance),2)+")) for "+advContMap+"!\n";
					msg+="Available Advance Amount is "+round(parseFloat(totAdvance),2)+" for "+advContMap+"!\nCan't Adjust "+round(parseFloat(totAllocatedAdv),2)+"!";
					flag=false;
				}
			}
		}
		
		if(flag)
		{
			var a = confirm("Do you want to Submit?")
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
}

function checkPayReceviDt(obj,index)
{
	var sysdate=document.forms[0].sysdate.value;
	var count = compareDate(obj.value,sysdate);
	if(parseInt(count) == 1)
	{
		alert("Pay Received Date should not be grater then Sysdate!")
		obj.value="";
		return false;
	}
}
function exportToXls()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var month_to = document.forms[0].month_to.value;
	var year_to = document.forms[0].year_to.value;
	var paid_status = document.forms[0].paid_status.value;
	//var segment = document.forms[0].segment.value;
	
	var url = "xls_dlng_receivable_tracking.jsp?fileName=DLNG Receivable Tracking Report.xls&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to+"&paid_status="+paid_status;

	location.replace(url);
}
</script>

</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();
int filter_start_year = CommonVariable.filter_start_year;

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String month_to=request.getParameter("month_to")==null?""+currentMonth:request.getParameter("month_to");
String year_to=request.getParameter("year_to")==null?""+currentYear:request.getParameter("year_to");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");
String paid_status=request.getParameter("paid_status")==null?"U":request.getParameter("paid_status");

if(month.length() == 1)
{
	month="0"+month; 
}
if(month_to.length() == 1)
{
	month_to="0"+month_to; 
}

dlng.setCallFlag("DLNG_RECEIVABLE_TRACKING");
dlng.setComp_cd(owner_cd);
dlng.setMonth(month);
dlng.setYear(year);
dlng.setMonth_to(month_to);
dlng.setYear_to(year_to);
dlng.setPaid_status(paid_status);
dlng.init();

//Vector VSEGMENT = dlng.getVSEGMENT();
//Vector VSEGMENT_TYPE = dlng.getVSEGMENT_TYPE();

Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = dlng.getVCONT_NO();
Vector VCONT_REV_NO = dlng.getVCONT_REV_NO();
Vector VAGMT_NO = dlng.getVAGMT_NO();
Vector VAGMT_REV_NO = dlng.getVAGMT_REV_NO();
Vector VDIS_CONT_MAPPING = dlng.getVDIS_CONT_MAPPING();
Vector VCONTRACT_TYPE = dlng.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = dlng.getVCONTRACT_TYPE_NM();
Vector VPERIOD_START_DT = dlng.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = dlng.getVPERIOD_END_DT();
Vector VFINANCIAL_YEAR = dlng.getVFINANCIAL_YEAR();
Vector VINVOICE_NO = dlng.getVINVOICE_NO();
Vector VINVOICE_SEQ = dlng.getVINVOICE_SEQ();
Vector VBU_STATE_TIN = dlng.getVBU_STATE_TIN();
Vector VINVOICE_DT = dlng.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = dlng.getVINVOICE_DUE_DT();
Vector VSALES_PRICE = dlng.getVSALES_PRICE();
Vector VSALES_PRICE_CD = dlng.getVSALES_PRICE_CD();
Vector VSALES_PRICE_NM = dlng.getVSALES_PRICE_NM();
Vector VGROSS_AMT = dlng.getVGROSS_AMT();
Vector VTAX_AMT = dlng.getVTAX_AMT();
Vector VINVOICE_AMT = dlng.getVINVOICE_AMT();
Vector VNET_PAYABLE_AMT = dlng.getVNET_PAYABLE_AMT();
Vector VTCS_AMT = dlng.getVTCS_AMT();
Vector VTCS_FACTOR = dlng.getVTCS_FACTOR();
Vector VTDS_GROSS_PERCENT = dlng.getVTDS_GROSS_PERCENT();
Vector VTDS_GROSS_AMT = dlng.getVTDS_GROSS_AMT();
Vector VTDS_TAX_PERCENT = dlng.getVTDS_TAX_PERCENT();
Vector VTDS_TAX_AMT = dlng.getVTDS_TAX_AMT();
Vector VPAY_RECV_AMT = dlng.getVPAY_RECV_AMT();
Vector VPAY_RECV_DT = dlng.getVPAY_RECV_DT();
Vector VTAX_STRUCT_DTL = dlng.getVTAX_STRUCT_DTL();
Vector VTDS_TCS_FLAG = dlng.getVTDS_TCS_FLAG();
Vector VSHORT_RECEIVED = dlng.getVSHORT_RECEIVED();
Vector VPAY_RECV_HISTORY = dlng.getVPAY_RECV_HISTORY();
Vector VINVOICE_RAISED_IN = dlng.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = dlng.getVPAYMENT_DONE_IN();
Vector VINVOICE_TYPE = dlng.getVINVOICE_TYPE();
Vector VTYPE_FLAG = dlng.getVTYPE_FLAG();
Vector VTOTAL_ADV_AMT = dlng.getVTOTAL_ADV_AMT();
Vector VCONT_MAP = dlng.getVCONT_MAP();
Vector VCFORM_FLAG = dlng.getVCFORM_FLAG();
Vector VHOLD_AMT = dlng.getVHOLD_AMT();
Vector VCARGO_NO = dlng.getVCARGO_NO();
Vector VRECV_DT = dlng.getVRECV_DT();
Vector VRECV_REMARK = dlng.getVRECV_REMARK();
Vector VRECV_AMT = dlng.getVRECV_AMT();

HashMap contWiseAdvAmt = dlng.getContWiseAdvAmt();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_DLNG_Invoice">

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
					    	DLNG Receivable Tracking
					    </div>
				   		<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					    <%-- <div class="d-flex justify-content-between">
					   		 <div onclick="exportToXls();" style="color:green;">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
							</div>&nbsp;
						    <div class="btn-group">
								<select class="btn btn-outline-secondary btngrp btnactive" name="segment" onchange="refresh();">
									<option value="">All</option>
									<%for(int i=0;i<VSEGMENT.size();i++){ %>
									<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT.elementAt(i)%></option>
									<%} %>
								</select>
							</div>
							<script>document.forms[0].segment.value="<%=segment%>"</script>
						</div> --%>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-2 col-xs-2 col-md-2"></div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>Month/Year</b></label>
					  		</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="month" onchange="refresh();">
										<option value="01" label="January">January</option>
										<option value="02" label="February">February</option>
										<option value="03" label="March">March</option>
										<option value="04" label="April">April</option>
										<option value="05" label="May">May</option>
										<option value="06" label="June">June</option>
										<option value="07" label="July">July</option>
										<option value="08" label="August">August</option>
										<option value="09" label="September">September</option>
										<option value="10" label="October">October</option>
										<option value="11" label="November">November</option>
										<option value="12" label="December">December</option>
									</select>
									<script>document.forms[0].month.value="<%=month%>"</script>
								</div>
								<div class="col">
					  				<select class="form-select form-select-sm" name="year" onchange="refresh();">
					  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
							</div>
						</div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>to</b></label>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col">
					  				<select class="form-select form-select-sm" name="month_to" onchange="refresh();">
										<option value="01" label="January">January</option>
										<option value="02" label="February">February</option>
										<option value="03" label="March">March</option>
										<option value="04" label="April">April</option>
										<option value="05" label="May">May</option>
										<option value="06" label="June">June</option>
										<option value="07" label="July">July</option>
										<option value="08" label="August">August</option>
										<option value="09" label="September">September</option>
										<option value="10" label="October">October</option>
										<option value="11" label="November">November</option>
										<option value="12" label="December">December</option>
									</select>
									<script>document.forms[0].month_to.value="<%=month_to%>"</script>
								</div>
								<div class="col">
					  				<select class="form-select form-select-sm" name="year_to" onchange="refresh();">
					  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year_to.value="<%=year_to%>"</script>
								</div>
							</div>
				  		</div>
				  		<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>Paid Status</b></label>
					  		</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<select class="form-select form-select-sm" name="paid_status" onchange="refresh();">
									<!-- <option value="">All</option> -->
									<option value="F">Fully Paid</option>
									<option value="U">Unpaid</option>
									<option value="P">Partial Paid</option>
								</select>
								<script>document.forms[0].paid_status.value="<%=paid_status%>"</script>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2"></div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th rowspan="2"></th>
										<th rowspan="2">
											Customer
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Customer" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">
											Contract#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Contract Type<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract_type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></th>
										<th rowspan="2">Billing Period</th>
										<th rowspan="2">
											Invoice#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_InvoiceNo" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Invoice Date</th>
										<th rowspan="2">Sales Rate</th>
										<th rowspan="2" style="background: #000066; color: white;">Rate Unit/ MMBTU</th>
										<th rowspan="2" style="background: #000066; color: white;">Invoice Raised In</th>										
										<th rowspan="2">Gross Amount (INR)</th>
										<th rowspan="2">Tax Structure</th>
										<th rowspan="2">Tax Amount (INR)</th>
										<th rowspan="2">Invoice Amount (INR)</th>
										<th colspan="2">TCS (INR)</th>
										<th colspan="2">TDS (INR)</th>
										<th rowspan="2">Net Receivable</th>
										<th rowspan="2">Invoice Due Date</th>
										<th rowspan="2" style="background: #000066; color: white;">Invoice Paid In</th>
										<th rowspan="2">Adjust Advance Amount (INR)</th>
										<th rowspan="2">Hold Amount (INR)</th>
										<th rowspan="2">Total Received (INR)</th>										
										<th rowspan="2">Short Received (INR)</th>
										<th rowspan="2">Actual Received (INR)</th>
										<th rowspan="2">Received Date</th>
										<th rowspan="2">Remark</th>
									</tr>
									<tr>
										<th>TCS(%)</th>
										<th>TCS Values</th>
										<th>TDS(%)</th>
										<th>TDS Values</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPARTY_CD.size() > 0){ %>
									<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
									<tr>
										<td>
											<input type="checkbox" class="form-check-input" name="chk" id="chk<%=i%>" onclick="enable_disable('<%=i%>');">
										</td>
										<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%> : <%=VCOUNTERPARTY_NM.elementAt(i)%>">
											<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
											<input type="hidden" name="cont_map" id="cont_map<%=i%>" value="<%=VCONT_MAP.elementAt(i)%>" disabled>
											<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>" disabled>
											<input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=i%>" value="<%=VAGMT_REV_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=i%>" value="<%=VCONT_REV_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="contract_type" id="contract_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled>
											<input type="hidden" name="financial_year" id="financial_year<%=i%>" value="<%=VFINANCIAL_YEAR.elementAt(i)%>" disabled>
											<input type="hidden" name="invoice_seq" id="invoice_seq<%=i%>" value="<%=VINVOICE_SEQ.elementAt(i)%>" disabled>
											<input type="hidden" name="bu_state_tin" id="bu_state_tin<%=i%>" value="<%=VBU_STATE_TIN.elementAt(i)%>" disabled>
											<input type="hidden" name="invoice_type" id="invoice_type<%=i%>" value="<%=VINVOICE_TYPE.elementAt(i)%>" disabled>
											<input type="hidden" name="type_flag" id="type_flag<%=i%>" value="<%=VTYPE_FLAG.elementAt(i)%>" disabled>
											<input type="hidden" name="cargo_no" id="cargo_no<%=i%>" value="<%=VCARGO_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="invoice_no" id="invoice_no<%=i%>" value="<%=VINVOICE_NO.elementAt(i)%>" disabled>
										</td>
										<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i) %></td>
										<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i) %></td>
										<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
										<td align="right"><%=VSALES_PRICE.elementAt(i)%></td>
										<td align="center" style="background: #b3f0ff;"><%=VSALES_PRICE_NM.elementAt(i) %></td>
										<td align="center" style="background: #b3f0ff;"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>
										<td align="right">
											<%=VGROSS_AMT.elementAt(i)%>
											<input type="hidden" name="gross_amt" id="gross_amt<%=i%>" value="<%=VGROSS_AMT.elementAt(i)%>">
										</td>
										<td align="center"><%=VTAX_STRUCT_DTL.elementAt(i)%></td>
										<td align="right">
											<%=VTAX_AMT.elementAt(i)%>
											<input type="hidden" name="tax_amt" id="tax_amt<%=i%>" value="<%=VTAX_AMT.elementAt(i)%>">
										</td>
										<td align="right">
											<%=VINVOICE_AMT.elementAt(i) %>
											<input type="hidden" name="invoice_amt" id="invoice_amt<%=i%>" value="<%=VINVOICE_AMT.elementAt(i)%>">
										</td>
										<td align="right"><%=VTCS_FACTOR.elementAt(i) %></td>
										<td align="right"><%=VTCS_AMT.elementAt(i) %></td>										
										<td align="center"><%=VTDS_GROSS_PERCENT.elementAt(i)%></td>
										<td align="center"><%=VTDS_GROSS_AMT.elementAt(i)%></td>										
										<td align="right"><input type="hidden" class="form-control form-control-sm" name="net_payable_amt" id="net_payable_amt<%=i%>" value="<%=VNET_PAYABLE_AMT.elementAt(i)%>" style="text-align:right;" disabled readOnly><%=VNET_PAYABLE_AMT.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
										<td align="center" style="background: #b3f0ff;"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
										<td align="center">
											<div style="width:130px;<%if(Double.parseDouble(""+VTOTAL_ADV_AMT.elementAt(i)) <= 0){%>display:none;<%}%>">
												<div class="row m-b-5">
													<div class="col-auto">
														<input type="checkbox" class="form-check-input" name="adj_chk" id="adj_chk_<%=i%>" onclick="calcPayableAmt('<%=i%>','ADV');" disabled>
														<input type="hidden" class="form-control form-control-sm" name="adj_flag" id="adj_flag_<%=i%>" disabled>
													</div>
													<div class="col">
														<input type="text" class="form-control form-control-sm" name="total_adv_amt" id="total_adv_amt_<%=i%>" 
														value="<%=VTOTAL_ADV_AMT.elementAt(i)%>" style="text-align:right;" disabled readOnly>
														
														<input type="hidden" class="form-control form-control-sm" name="adjust_amt" id="adjust_amt_<%=i%>" 
															value="<%//=VPAY_RECV_AMT.elementAt(i)%>" style="text-align:right;" disabled>
													</div>
												</div>
											</div>
										</td>
										<td align="center">
											<div style="width:130px;<%if(!VCFORM_FLAG.elementAt(i).equals("Y")){%>display:none;<%}%>">
												<div class="row m-b-5">
													<div class="col-auto">
														<input type="checkbox" class="form-check-input" name="hold_chk" id="hold_chk_<%=i%>" disabled>
														<input type="hidden" class="form-control form-control-sm" name="hold_flag" id="hold_flag_<%=i%>" disabled>
														<input type="hidden" class="form-control form-control-sm" name="cform_flag" id="cform_flag_<%=i%>" value="<%=VCFORM_FLAG.elementAt(i)%>" disabled>
													</div>
													<div class="col">
														<input type="text" class="form-control form-control-sm" name="hold_amt" id="hold_amt_<%=i%>" 
														value="<%=VHOLD_AMT.elementAt(i)%>" style="text-align:right;" disabled readOnly>
													</div>
												</div>
											</div>
										</td>
										<td align="center" title="<%=VPAY_RECV_HISTORY.elementAt(i)%>">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="total_received_amt" id="total_received_amt<%=i%>" 
												value="<%=VPAY_RECV_AMT.elementAt(i)%>" style="text-align:right;" 
												onkeyup="calcPayableAmt('<%=i%>');" onblur="checkNumber1(this,12,2);" disabled readOnly>
											</div>
											<input type="hidden" class="form-control form-control-sm" name="temp_total_received_amt" id="temp_total_received_amt<%=i%>" 
												value="<%=VPAY_RECV_AMT.elementAt(i)%>" style="text-align:right;" disabled>
										</td>										
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="short_received" id="short_received<%=i%>" 
												value="<%=VSHORT_RECEIVED.elementAt(i)%>" style="text-align:right;" disabled readOnly>
											</div>
											<input type="hidden" class="form-control form-control-sm" name="temp_short_received" id="temp_short_received<%=i%>" 
											value="<%=VSHORT_RECEIVED.elementAt(i)%>" style="text-align:right;" disabled readOnly>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="pay_received_amt" id="pay_received_amt<%=i%>" 
												<%if(paid_status.equals("F")){ %>value="<%=VRECV_AMT.elementAt(i)%>"<%}else{ %>value=""<%} %> style="text-align:right;" 
												onkeyup="calcPayableAmt('<%=i%>');" onblur="checkNumber1(this,12,2);" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="pay_received_dt" id="pay_received_dt<%=i%>" 
						      						maxLength="10" onchange="validateDate(this);checkPayReceviDt(this,'<%=i%>');" autocomplete="off" disabled <%if(paid_status.equals("F")){ %>value="<%=VRECV_DT.elementAt(i)%>"<%}else{ %>value=""<%} %>>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div>
					      					</div>
										</td>
										<td align="center">
											<div style="width:150px;">
											<textarea class="form-control form-control-sm" rows="1" cols="75" name="remark" id="remark<%=i%>" disabled><%if(paid_status.equals("F")){%> <%=VRECV_REMARK.elementAt(i)%> <%} %></textarea>
											</div>
										</td>
									</tr>
									<%} %>
								<%} else {%>
									<tr>
										<td colspan="28">
											<div align="center"><%=utilmsg.infoMessage("<b>No Invoice generated for Report Period!</b>")%></div>
										</td>
									</tr>
								<%} %>
								</tbody>
							</table>
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

<%
Iterator o = contWiseAdvAmt.keySet().iterator();
int rowc = 0;
int k=0;
while (o.hasNext()) 
{ 
	String key = (String) o.next();
	String value=""+contWiseAdvAmt.get(key);
	//System.out.println(key+"-"+value);
	%>
	<input type="hidden" name="adv_cont_map" id="adv_cont_map_<%=k%>" value="<%=key%>">
	<input type="hidden" name="adv_cont_val" id="adv_cont_val_<%=k%>" value="<%=value%>">
<%k=k+1;
}
%>

<input type="hidden" name="option" value="DLNG_RECEIVABLE_TRACKING">
<input type="hidden" name="tmp_from_month" value="<%=month%>">
<input type="hidden" name="tmp_from_year" value="<%=year%>">
<input type="hidden" name="tmp_to_month" value="<%=month_to%>">
<input type="hidden" name="tmp_to_year" value="<%=year_to%>">

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
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example");
  	
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