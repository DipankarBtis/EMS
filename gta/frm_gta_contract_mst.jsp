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
	var contract_type = document.forms[0].contract_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gta_contract_mst.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+"&u="+u+"&contract_type="+contract_type;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var newWindow;
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var agreement_type = document.forms[0].agreement_type.value;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_gta_contract_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&agreement_type="+agreement_type,"GTA Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_gta_contract_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&agreement_type="+agreement_type,"GTA Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setContractDetail(agmt_no,agmt_rev_no,cont_no,cont_rev_no,agmt_type,countpty_cd,cont_type)
{
	var opration = document.forms[0].opration.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gta_contract_mst.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&contract_type="+cont_type+
			"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&u="+u+"&agreement_type="+agmt_type;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function openAgreementList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agreement_type = document.forms[0].agreement_type.value;

	if(counterparty_cd=="0" || counterparty_cd=="")
	{
		alert("Select Transporter!");
	}
	else
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open("frm_gta_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open("frm_gta_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function setAgreementDetail(agmt_no,agmt_rev_no,agmt_nm,agmt_type)
{
	var opration = document.forms[0].opration.value;
	var contract_type = document.forms[0].contract_type.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gta_contract_mst.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&contract_type="+contract_type+"&agmt_nm="+agmt_nm+
			"&u="+u+"&agreement_type="+agmt_type;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function openSalesContList()
{
	var customer_cd=document.forms[0].customer_cd.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var entry_point = document.forms[0].entry_point.value;
	var exit_point = document.forms[0].exit_point.value;
	
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	
	var msg="";
	var flag=true;
	
	var bu_unit="";
	var chkFlg=false;
	
	if(trim(start_dt) == "")
	{
		msg+="Enter Start Date!\n";
		flag=false;
	}
	if(trim(end_dt) == "")
	{
		msg+="Enter End Date!\n";
		flag=false;
	}
	if(trim(entry_point) == "")
	{
		msg+="Select Entry Point!\n";
		flag=false;
	}
	if(trim(exit_point) == "")
	{
		msg+="Select Exit Point!\n";
		flag=false;
	}
	
	if(chk_bu_plant!=null && chk_bu_plant!=undefined)
	{
		if(chk_bu_plant.length!=undefined)
		{
			for(var i=0;i<chk_bu_plant.length;i++)
			{
				if(chk_bu_plant[i].checked)
				{
					bu_unit=chk_bu_plant[i].value;	
					chkFlg=true;
				}
			}
		}
		else
		{
			if(chk_bu_plant.checked)
			{
				bu_unit=chk_bu_plant.value;	
				chkFlg=true;
			}
		}
	}
	
	if(chkFlg==false)
	{
		msg += "Please Select Atleast One Business Unit!\n";
		flag=false;
	}	
	
	var url ="frm_sales_cont_list.jsp?customer_cd="+customer_cd+"&start_dt="+start_dt+"&end_dt="+end_dt+
			"&entry_point="+entry_point+"&exit_point="+exit_point+"&bu_unit="+bu_unit;
	
	/* if(customer_cd=="0" || customer_cd=="")
	{
		alert("Select Customer!");
	} */
	if(flag)
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Sales Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Sales Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
	else
	{
		alert(msg);
	}
}

function openCTList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var entry_point = document.forms[0].entry_point.value;
	var exit_point = document.forms[0].exit_point.value;	
	var customer_cd = document.forms[0].customer_cd.value;
	var sales_cont_map = document.forms[0].sales_cont_map.value;
	
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	
	var msg="";
	var flag=true;
	
	var bu_unit="";
	var chkFlg=false;
	
	if(chk_bu_plant!=null && chk_bu_plant!=undefined)
	{
		if(chk_bu_plant.length!=undefined)
		{
			for(var i=0;i<chk_bu_plant.length;i++)
			{
				if(chk_bu_plant[i].checked)
				{
					bu_unit=chk_bu_plant[i].value;	
					chkFlg=true;
				}
			}
		}
		else
		{
			if(chk_bu_plant.checked)
			{
				bu_unit=chk_bu_plant.value;	
				chkFlg=true;
			}
		}
	}
	
	if(chkFlg==false)
	{
		msg += "Please Select Atleast One Business Unit!\n";
		flag=false;
	}

	if(flag)
	{
		var url="frm_ct_ref_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+
				"&start_dt="+start_dt+"&end_dt="+end_dt+"&entry_point="+entry_point+"&exit_point="+exit_point+
				"&customer_cd="+customer_cd+"&sales_cont_map="+sales_cont_map+"&bu_unit="+bu_unit;
		
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"CT Reference List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"CT Reference List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
	else
	{
		alert(msg);
	}
}

function setSalesContDetail(customer_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,cont_type,dealNo,contRef)
{
	var sales_cont_nm=dealNo+" ("+contRef+")";
	var sales_cont_map=cont_type+"-"+agmt_no+"-"+agmt_rev_no+"-"+cont_no+"-"+cont_rev_no;
		
	document.forms[0].customer_cd.value=customer_cd;
	document.forms[0].sales_cont_nm.value=sales_cont_nm;
	
	document.forms[0].sales_cont_map.value=sales_cont_map;
}

function setCtSeqNo(seq_no,ct_ref_no)
{
	document.forms[0].ct_seq_no.value=seq_no;
	document.forms[0].ct_ref_no.value=ct_ref_no;
}

function openBillingDtl()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var agreement_type = document.forms[0].agreement_type.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gta_contract_billing_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
			"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+
			"&u="+u+"&end_dt="+end_dt+"&agreement_type="+agreement_type;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Contract Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Contract Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}


function doSubmit()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var opration = document.forms[0].opration.value;
	var contract_type = document.forms[0].contract_type.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var cont_ref_no = document.forms[0].cont_ref_no.value;
	var ct_ref_no = document.forms[0].ct_ref_no.value;
	var entry_point = document.forms[0].entry_point.value;
	var exit_point = document.forms[0].exit_point.value;
	var mdq = document.forms[0].mdq.value;
	var mdq_unit_cd = document.forms[0].mdq_unit_cd.value;
	
	var customer_cd = document.forms[0].customer_cd.value;
	var sales_cont_map = document.forms[0].sales_cont_map.value;
	
	var calc_base = document.forms[0].calc_base;
	var gcv = document.forms[0].gcv.value;
	var ncv = document.forms[0].ncv.value;
	
	var transportation_rate = document.forms[0].transportation_rate.value;
	var positive_imbalance_rate = document.forms[0].positive_imbalance_rate.value;
	var negative_imbalance_rate = document.forms[0].negative_imbalance_rate.value;
	var ship_pay_charge = document.forms[0].ship_pay_charge.value;
	var unauth_overrun_charge = document.forms[0].unauth_overrun_charge.value;
	
	var ship_or_pay_percent = document.forms[0].ship_or_pay_percent.value;
	
	var no_of_billing_dtl = document.forms[0].no_of_billing_dtl.value;
	
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var chk_trans_bu = document.forms[0].chk_trans_bu;
	
	var max_date = document.forms[0].max_date.value;
	var min_nom_date = document.forms[0].min_nom_date.value;
	
	var msg="";
	var flag=true;
	
	if(counterparty_cd == "" || counterparty_cd == "0")
	{
		msg+="Select Counterparty!\n";
		flag=false;
	}
	if(trim(contract_type) == "")
	{
		msg+="Select GTC Type!\n";
		flag=false;
	}	
	if(agmt_no == "" || agmt_no == "0")
	{
		msg+="Select Agreement!\n";
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
	if(trim(cont_ref_no) == "")
	{
		msg+="Enter Cont Ref#!\n";
		flag=false;
	}
	/* if(trim(ct_ref_no) == "")
	{
		msg+="Enter CT Ref#!\n";
		flag=false;
	} */
	if(trim(start_dt) == "")
	{
		msg+="Enter Start Date!\n";
		flag=false;
	}
	if(trim(end_dt) == "")
	{
		msg+="Enter End Date!\n";
		flag=false;
	}
	if(trim(start_dt)!="" && trim(end_dt)!="")
   	{
    	var agmt_start_dt = document.forms[0].agmt_start_dt.value;
      	var agmt_end_dt = document.forms[0].agmt_end_dt.value;
     	
      	var val = compareDate(end_dt,start_dt);
      	
      	if(val=="1" || val=="0")
      	{
        	val1 = compareDate(start_dt,agmt_start_dt);
         	val2 = compareDate(start_dt,agmt_end_dt);
         	
         	if(val1=="2")
         	{
         		msg+="Start date must be in the range of GT Agreement Start and End date..\nGT Agreement Period ("+agmt_start_dt+" - "+agmt_end_dt+")..\n";
           		flag = false;
         	}
         	
         	val1 = compareDate(end_dt,agmt_end_dt);
         	if(val1=="1")
         	{
         		msg+="End date must be less or equal to GT Agreement End date..\nGT Agreement Period ("+agmt_start_dt+" - "+agmt_end_dt+")..\n";
           		flag = false;
         	} 
      	}
      	else
      	{
      		msg+="Start date must be Less than or equal to end date..\nGT Agreement Period ("+agmt_start_dt+" - "+agmt_end_dt+")..\n";
        	flag = false;
      	}
   	}
   	else
   	{
   		msg += "Please Insert Start and End Date of GTC..\n";
     	flag = false; 
   	}
	
	if(opration=="MODIFY")
	{
		if(trim(max_date) !="")
		{
			var value_1 = compareDate(max_date,end_dt);
			if(value_1 == "1") 
			{
				msg+="Contract End Date "+end_dt+" < Last Allocation/Invoice Date "+max_date+" not Allowed!\n";
				flag=false
			}
		}
		
		if(trim(min_nom_date) !="")
		{
			var value_0 = compareDate(start_dt,min_nom_date);
			if(value_0 == "1")
			{
				msg+="Contract Start Date "+start_dt+" > First Nomination Date "+min_nom_date+" not Allowed!\n";
				flag=false
			}
		}	
	}
	
	if(trim(entry_point) == "")
	{
		msg+="Select Entry Point!\n";
		flag=false;
	}
	if(trim(exit_point) == "")
	{
		msg+="Select Exit Point!\n";
		flag=false;
	}
	if(trim(mdq) == "")
	{
		msg+="Enter MDQ!\n";
		flag=false;
	}
	if(trim(mdq_unit_cd) == "")
	{
		msg+="Select MDQ Unit!\n";
		flag=false;
	}
	if(!calc_base[0].checked && !calc_base[1].checked)
	{
		msg+="Select Calorific Base!\n";
		flag=false;
	}
	if(trim(gcv) == "")
	{
		msg+="Enter GCV!\n";
		flag=false;
	}
	if(trim(ncv) == "")
	{
		msg+="Enter NCV!\n";
		flag=false;
	}
	if(contract_type=="C")
	{
		/* if(trim(customer_cd) == "")
		{
			msg+="Select Customer!\n";
			flag=false;
		} */
		if(trim(sales_cont_map) == "")
		{
			msg+="Sales Contract Mapping is Missing!\n";
			flag=false;
		}
	}
	if(trim(transportation_rate) == "")
	{
		msg+="Enter Transportation Rate!\n";
		flag=false;
	}
	if(trim(positive_imbalance_rate) == "")
	{
		msg+="Enter Positive Imbalance Rate!\n";
		flag=false;
	}
	if(trim(negative_imbalance_rate) == "")
	{
		msg+="Enter Negitive Imbalance Rate!\n";
		flag=false;
	}
	if(trim(ship_pay_charge) == "")
	{
		msg+="Enter Ship or Pay Charge!\n";
		flag=false;
	}
	if(trim(unauth_overrun_charge) == "")
	{
		msg+="Enter Unauthorize Overrun Charge!\n";
		flag=false;
	}
	if(trim(ship_or_pay_percent) == "")
	{
		msg+="Enter Ship or Pay Percentage!\n";
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
		msg += "Please Select Atleast One Business Unit!\n";
		flag=false;
	}
	
	//FOR TRANSPORTER BUSINESS UNIT
	chkFlg = false;
	if(chk_trans_bu!=null && chk_trans_bu!=undefined)
	{
		if(chk_trans_bu.length!=undefined)
		{
			for(var i=0;i<chk_trans_bu.length;i++)
			{
				if(chk_trans_bu[i].checked)
				{
					chkFlg = true;	
				}
			}
		}
		else
		{
			if(chk_trans_bu.checked)
			{
				chkFlg = true;	
			}
		}
	}
	if(chkFlg==false)
	{
		msg += "Please Select Atleast One Transporter BU!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a;
		if(opration=="MODIFY")
		{
			a = confirm("Do you want to Modify GTA Contract?");
		}
		else
		{
			a = confirm("Do you want to Create New GTA Contract?");
		}
		if(a)
		{
			var temp_msg="";
			if(trim(no_of_billing_dtl) != "")
			{
				if(parseInt(no_of_billing_dtl) <= 0)
				{
					temp_msg += "Please filling the Billing Detail after Submitting Contract Detail!\n";
				}
			}
			else
			{
				temp_msg += "Please filling the Billing Detail after Submitting Contract Detail!!\n";
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

function setAmount(obj)
{
	var positive_imbalance_rate = document.forms[0].positive_imbalance_rate;
	var negative_imbalance_rate = document.forms[0].negative_imbalance_rate;
	var ship_pay_charge = document.forms[0].ship_pay_charge;
	var unauth_overrun_charge = document.forms[0].unauth_overrun_charge;
	
	if(trim(obj.value)!="")
	{
		positive_imbalance_rate.value=round(parseFloat(obj.value)/2,2);
		negative_imbalance_rate.value=round(parseFloat(obj.value)/2,2);
		unauth_overrun_charge.value=round(parseFloat(obj.value)/2,2);
		ship_pay_charge.value=round(parseFloat(obj.value),2);
	}
}

function setValues(strBuPlant,strTransBu)
{
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var chk_trans_bu = document.forms[0].chk_trans_bu;
	
	var sepBuPlant = strBuPlant.split("@");
	var sepTransBu = strTransBu.split("@");
	
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
	
	//FOR TRANSPORTER BU
	if(chk_trans_bu!=null && chk_trans_bu.length!=undefined)
 	{
  		for(var i=0;i<chk_trans_bu.length;i++)
  		{
   			for(var j=0;j<sepTransBu.length;j++)
   			{
     			if(chk_trans_bu[i].value == sepTransBu[j])
     			{
     				chk_trans_bu[i].checked = true;
     			}
   			} 
  		} 
 	}
 	else if(chk_trans_bu!=null)
 	{
   		for(var j=0;j<sepTransBu.length;j++)
   		{
   			if(chk_trans_bu.value == sepTransBu[j])
     		{
   				chk_trans_bu.checked = true;
     		}
   		} 
 	}
}

function handleComma(input,field_nm) 
{
	if (input.value.includes(',')) 
    {
      alert("Comma(,) not allowed for "+field_nm);
      input.value = input.value.replace(/,/g, '');
	}
}

function checkNom_Status()
{
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var bu_nom_flg = document.forms[0].bu_nom_flg;
	var chk_trans_bu = document.forms[0].chk_trans_bu;
	var trans_bu_nom_flg = document.forms[0].trans_bu_nom_flg;
	
	//For BU Unit
	if(bu_nom_flg!=null && bu_nom_flg.length!=undefined)
 	{
  		for(var i=0;i<bu_nom_flg.length;i++)
  		{
   			//for(var j=0;j<bu_nom_flg.length;j++)
   			{
     			if(bu_nom_flg[i].value == 'Y' && !chk_bu_plant[i].checked)
     			{
     				alert("Nomination is already done, can't select other business unit!");
     				chk_bu_plant[i].checked = true;
     				break;
     			}
   			} 
  		} 
 	}
 	else 
 	{
   		//for(var j=0;j<bu_nom_flg.length;j++)
   		{
   			if(bu_nom_flg.value == 'Y' && !chk_bu_plant.checked)
     		{
   				alert("Nomination is already done, can't select other business unit!");
   				chk_bu_plant.checked = true;
     		}
   		} 
 	}
	
	//For Transporter Bu
	if(trans_bu_nom_flg!=null && trans_bu_nom_flg.length!=undefined)
 	{
  		for(var i=0;i<trans_bu_nom_flg.length;i++)
  		{
   			//for(var j=0;j<bu_nom_flg.length;j++)
   			{
     			if(trans_bu_nom_flg[i].value == 'Y' && !chk_trans_bu[i].checked)
     			{
     				alert("Remittance Generated, can't select other Transporter BU!");
     				chk_trans_bu[i].checked = true;
     				break;
     			}
   			} 
  		} 
 	}
 	else 
 	{
   		//for(var j=0;j<bu_nom_flg.length;j++)
   		{
   			if(trans_bu_nom_flg.value == 'Y' && !chk_trans_bu.checked)
     		{
   				alert("Remittance Generated, can't select other Transporter BU!");
   				chk_trans_bu.checked = true;
     		}
   		} 
 	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GtaMaster" id="gta" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type = request.getParameter("contract_type")==null?"0":request.getParameter("contract_type");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String agmt_nm = request.getParameter("agmt_nm")==null?"":request.getParameter("agmt_nm");
String agreement_type = request.getParameter("agreement_type")==null?"G":request.getParameter("agreement_type");

gta.setCallFlag("GTA_CONTRACT");
gta.setComp_cd(owner_cd);
gta.setCounterparty_cd(counterparty_cd);
gta.setAgmt_no(agmt_no);
gta.setAgmt_rev_no(agmt_rev_no);
gta.setCont_no(cont_no);
gta.setCont_rev_no(cont_rev_no);
gta.setContract_type(contract_type);
gta.setAgreement_type(agreement_type);
gta.setOpration(opration);
gta.init();

Vector VCOUNTERPARTY_CD = gta.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = gta.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = gta.getVCOUNTERPARTY_ABBR();

Vector VCUSTOMER_CD = gta.getVCUSTOMER_CD();
Vector VCUSTOMER_NM = gta.getVCUSTOMER_NM();
Vector VCUSTOMER_ABBR = gta.getVCUSTOMER_ABBR();

Vector VENTRY_MAPPING = gta.getVENTRY_MAPPING();
Vector VENTRY_POINT_NAME = gta.getVENTRY_POINT_NAME();
Vector VEXIT_MAPPING = gta.getVEXIT_MAPPING();
Vector VEXIT_POINT_NAME = gta.getVEXIT_POINT_NAME();

Vector VBU_CD = gta.getVBU_CD();
Vector VBU_PLANT_NM = gta.getVBU_PLANT_NM();
Vector VBU_PLANT_ABBR = gta.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = gta.getVBU_PLANT_SEQ_NO();

Vector VTRANS_BU_NM = gta.getVTRANS_BU_NM();
Vector VTRANS_BU_ABBR = gta.getVTRANS_BU_ABBR();
Vector VTRANS_BU_SEQ_NO = gta.getVTRANS_BU_SEQ_NO();

Vector VSEL_BU_CD = gta.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = gta.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_BU_PLANT_ABBR = gta.getVSEL_BU_PLANT_ABBR();

Vector VSEL_TRANS_BU_SEQ_NO = gta.getVSEL_TRANS_BU_SEQ_NO();
Vector VNOM_SEL_BU_CHK = gta.getVNOM_SEL_BU_CHK();
Vector VNOM_SEL_TRANS_BU_CHK = gta.getVNOM_SEL_TRANS_BU_CHK();

String cont_name =gta.getCont_name();
String cont_ref_no =gta.getCont_ref_no();
String ct_ref_no =gta.getCt_ref_no();
String start_dt =gta.getStart_dt();
String end_dt =gta.getEnd_dt();
String entry_pt_mapping_id =gta.getEntry_pt_mapping_id();
String exit_pt_mapping_id =gta.getExit_pt_mapping_id();
String mdq =gta.getMdq();
String mdq_unit =gta.getMdq_unit();
String rate_unit =gta.getRate_unit();
String transport_rate =gta.getTransport_rate();
String positive_imb_rate =gta.getPositive_imb_rate();
String negetive_imb_rate =gta.getNegetive_imb_rate();
String unauth_overrun_rate =gta.getUnauth_overrun_rate();
String sip_pay_rate =gta.getSip_pay_rate();
String sip_pay_freq =gta.getSip_pay_freq();
String sip_pay_percent =gta.getSip_pay_percent();
String ct_seq_no =gta.getCt_seq_no();
String no_of_billing_dtl=gta.getNo_of_billing_dtl();

String customer_cd=gta.getCustomer_cd();
String sales_cont_nm=gta.getSales_cont_nm();
String sales_cont_map=gta.getSales_cont_map();

String calc_base=gta.getCalc_base();
String gcv=gta.getGcv();
String ncv=gta.getNcv();

String agmt_calc_base=gta.getAgmt_calc_base();
String agmt_start_dt =gta.getAgmt_start_dt();
String agmt_end_dt =gta.getAgmt_end_dt();

String max_date = gta.getMax_date();
String min_nom_date = gta.getMin_nom_date();

String nom_count =gta.getNom_count();

if(customer_cd.equals(""))
{
	customer_cd="0";
}
if(calc_base.equals(""))
{
	calc_base=agmt_calc_base;
}

String strBuPlant="";
for(int i=0;i<VSEL_BU_PLANT_SEQ_NO.size();i++)
{
	strBuPlant = strBuPlant + VSEL_BU_PLANT_SEQ_NO.elementAt(i)+"@";
}
String strTransBu="";
for(int i=0;i<VSEL_TRANS_BU_SEQ_NO.size();i++)
{
	strTransBu = strTransBu + VSEL_TRANS_BU_SEQ_NO.elementAt(i)+"@";
}
%>
<body onload="setValues('<%=strBuPlant%>','<%=strTransBu%>');">
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_GtaMaster">

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
					    	Capacity Tranche
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
				    			<label class="form-label"><b>Select Transporter<span class="s-red">*</span></b></label>
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
									<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
									<input type="hidden" class="form-control form-control-sm" name="cont_no" value="<%=cont_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_rev_no" value="<%=cont_rev_no%>" maxLength="2" readOnly>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">	
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>CT Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">
								<div class="col">
				    				<select class="form-select form-select-sm" name="contract_type" onchange="refresh('<%=opration%>');">
				    					<option value="0">--Select--</option>
				    					<option value="R">Transporter</option>
				    					<option value="C">Customer</option>
				    				</select>
				    				<script>document.forms[0].contract_type.value="<%=contract_type%>"</script>
				      			</div>
				      			<%if(opration.equals("INSERT")){ %>
				      			<div class="col-auto">
				    				<input type="button" class="btn btn-info btn-sm select_btn" value="Select Agreement" style="font-weight: bold;" onclick="openAgreementList();">
				    			</div>
				    			<div class="col">
				    				<input type="text" class="form-control form-control-sm" name="agmt_nm" value="<%=agmt_nm%>" readOnly>
				    			</div>
				    			<%} %>
							</div>
						</div>
					</div>
					<%if(opration.equalsIgnoreCase("MODIFY")){ %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    			<!-- <label class="form-label"><b>Contract No<span class="s-red">*</span></b></label> -->
				    			<input type="button" class="btn btn-info btn-sm select_btn" value="Select GTC" onclick="openContList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="cont_name" value="<%=cont_name%>" maxLength="50" readOnly>
				    			</div>
				  			</div>
						</div>
						<%-- <div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Status</b></label>

				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label"><b><%//=cont_status%></b></label>
				      			</div>
				  			</div>
						</div> --%>
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
				      				<input type="text" class="form-control form-control-sm" name="cont_ref_no" value="<%=cont_ref_no%>" maxLength="25" oninput="handleComma(this,'Contract Ref#')">
				    			</div>
				  			</div>
						</div>
						<!-- <div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>CT Ref#<span class="s-red">*</span></b></label>
				  			</div>
						</div> 
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ct_ref_no" value="<%=ct_ref_no%>" maxLength="25">
				    			</div>
				  			</div>
						</div>-->
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
		      						<input type="hidden" name="agmt_start_dt" value="<%=agmt_start_dt%>">
		      						<input type="hidden" name="temp_start_dt" value="<%=start_dt%>">
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
		      						<input type="hidden" name="agmt_end_dt" value="<%=agmt_end_dt%>">
				    			</div>
				    		</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Entry Point<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="entry_point" <%if(Integer.parseInt(nom_count)>0){ %>style="pointer-events: none;"<%} %>>
				    					<option value="">--Select--</option>
										<%for(int i=0;i<VENTRY_MAPPING.size();i++){ %>
										<option value="<%=VENTRY_MAPPING.elementAt(i)%>"><%=VENTRY_POINT_NAME.elementAt(i)%></option>
										<%} %>
				    				</select>
				    				<script>document.forms[0].entry_point.value="<%=entry_pt_mapping_id%>"</script>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Exit Point<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="exit_point" <%if(Integer.parseInt(nom_count)>0){ %>style="pointer-events: none;"<%} %>>
				    					<option value="">--Select--</option>
										<%for(int i=0;i<VEXIT_MAPPING.size();i++){ %>
										<option value="<%=VEXIT_MAPPING.elementAt(i)%>"><%=VEXIT_POINT_NAME.elementAt(i)%></option>
										<%} %>
				    				</select>
				    				<script>document.forms[0].exit_point.value="<%=exit_pt_mapping_id%>"</script>
				    			</div>
				    		</div>
				    	</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>MDQ<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<input type="text" class="form-control form-control-sm" name="mdq" value="<%=mdq%>" onblur="checkNumber1(this,11,3);">
				    			</div>
				    			<div class="col-auto">
									<select class="form-select form-select-sm" name="mdq_unit_cd" style="pointer-events: none;">
				    					<option value="1">MMBTU</option>
				    				</select>
				    			</div>	
				    			<%if(opration.equals("MODIFY")){ %>
				    			<div class="col-auto">
				    				<input type="button" class="btn btn-sm config_btn" value="Variable MDQ Config">
				    			</div>			    		
				    			<%} %>	
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Calorific Base<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<input type="radio" name="calc_base" value="GCV" <%if(calc_base.equals("GCV")){ %>checked<%} %> style="pointer-events: none;">&nbsp;GCV
			      				</div>
			      				<div class="col-auto">
			      					<input type="text" class="form-control form-control-sm" name="gcv" value="<%=gcv%>" style="width:75px;text-align:right;" onblur="checkNumber1(this,7,2);" <%if(Integer.parseInt(nom_count)>0){ %>readOnly<%} %>>
			      				</div>
			      				<div class="col-auto">
			      					<input type="radio" name="calc_base" value="NCV" <%if(calc_base.equals("NCV")){ %>checked<%} %> style="pointer-events: none;">&nbsp;NCV 
			      				</div>
			      				<div class="col-auto">
				      				<input type="text" class="form-control form-control-sm" name="ncv" value="<%=ncv%>" style="width:75px;text-align:right;" onblur="checkNumber1(this,7,2);" <%if(Integer.parseInt(nom_count)>0){ %>readOnly<%} %>>
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
					    					<%-- <input type="checkbox" class="form-check-input" name="chk_bu_plant" value="<%=VBU_PLANT_SEQ_NO.elementAt(i)%>">&nbsp;<%=VBU_PLANT_ABBR.elementAt(i)%>&nbsp;&nbsp; --%>
					    					<input type="radio" name="chk_bu_plant" value="<%=VBU_PLANT_SEQ_NO.elementAt(i)%>" onchange="checkNom_Status();">&nbsp;<%=VBU_PLANT_ABBR.elementAt(i)%>&nbsp;&nbsp;
					    					<input type="hidden" name="bu_nom_flg" value="<%=VNOM_SEL_BU_CHK.elementAt(i)%>">
					    				<%}%>
				    				<%}else{ %>
				    					<%= utilmsg.warningMessage("Please configure Business Plants!")%>
				    				<%} %>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Transporter BU<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<%if(VTRANS_BU_SEQ_NO.size() > 0) {%>
					    				<%for(int i=0; i<VTRANS_BU_SEQ_NO.size(); i++){ %>
					    					<input type="radio" name="chk_trans_bu" value="<%=VTRANS_BU_SEQ_NO.elementAt(i)%>" onchange="checkNom_Status();">&nbsp;<%=VTRANS_BU_ABBR.elementAt(i)%>&nbsp;&nbsp;
					    					<input type="hidden" name="trans_bu_nom_flg" value="<%=VNOM_SEL_TRANS_BU_CHK.elementAt(i)%>">
					    					
					    				<%}%>
					    				<input type="hidden" name="temp_chk_trans_bu" <%if(opration.equals("MODIFY")){ %>value="<%=VSEL_TRANS_BU_SEQ_NO.elementAt(0)%>"<%}else{ %>value=""<%} %>>
				    				<%}else{ %>
				    					<%= utilmsg.warningMessage("Please configure Transporter Business Units!")%>
				    				<%} %>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Ship or Pay Frequency<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="col-auto">
								<input type="radio" name="sip_pay_freq" value="D" <%if(sip_pay_freq.equals("D")){ %>checked<%}else{%>checked<%} %>>&nbsp;Daily&nbsp;&nbsp;&nbsp;
								<input type="radio" name="sip_pay_freq" value="M" <%if(sip_pay_freq.equals("M")){ %>checked<%} %>>&nbsp;Monthly
		      				</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Ship or Pay(%)<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="ship_or_pay_percent" value="<%=sip_pay_percent%>" onblur="checkNumber1(this,5,2);">
			      				</div>
			      			</div>
						</div>
					</div>
					<div <%if(!contract_type.equals("C")){ %>style="display:none;"<%} %>>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Sales Contract Detail</label>
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
								<input type="button" class="btn btn-sm config_btn" value="Link Sales Contract" onclick="openSalesContList();" style="font-weight: bold;">
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4"> 
								<div class="form-group row">		
									<div class="col">
										<input type="text" class="form-control form-control-sm" name="sales_cont_nm" value="<%=sales_cont_nm%>" style="background:#99ffcc;" readOnly>
				    					<input type="hidden" name="sales_cont_map" value="<%=sales_cont_map%>">
				    					<input type="hidden" name="customer_cd" value="<%=customer_cd%>">
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
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Link CT Reference</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<input type="button" class="btn btn-sm config_btn" value="Link CT" onclick="openCTList();" style="font-weight: bold;">
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">		
								<div class="col">
				    				<input type="text" class="form-control form-control-sm" name="ct_ref_no" value="<%=ct_ref_no%>" maxLength="50" style="background:#99ffcc;" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="ct_seq_no" value="<%=ct_seq_no%>">					    				
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
				    			<label class="form-label"><b>Transportation Rate<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="transportation_rate" 
				      				value="<%=transport_rate%>" onblur="checkNumber1(this,7,4);setAmount(this);">
				    			</div>
				    			<div class="col">
		      						<select class="form-select form-select-sm" name="trans_rate_cd" style="pointer-events: none;">
		      							<option value="1">INR / MMBTU</option>
		      							<option value="2">USD / MMBTU</option>
		      						</select>
		      					</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Positive Imbalance Rate<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="positive_imbalance_rate" value="<%=positive_imb_rate%>" onblur="checkNumber1(this,7,4);">
				    			</div>
				    			<div class="col">
		      						<select class="form-select form-select-sm" name="pos_imb_rate_cd" style="pointer-events: none;">
		      							<option value="1">INR / MMBTU</option>
		      							<option value="2">USD / MMBTU</option>
		      						</select>
		      					</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Negative Imbalance Rate<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="negative_imbalance_rate" value="<%=negetive_imb_rate%>" onblur="checkNumber1(this,7,4);">
				    			</div>
				    			<div class="col">
		      						<select class="form-select form-select-sm" name="neg_imb_rate_cd" style="pointer-events: none;">
		      							<option value="1">INR / MMBTU</option>
		      							<option value="2">USD / MMBTU</option>
		      						</select>
		      					</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Ship or Pay Charge<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="ship_pay_charge" value="<%=sip_pay_rate%>" onblur="checkNumber1(this,7,4);">
				    			</div>
				    			<div class="col">
		      						<select class="form-select form-select-sm" name="ship_pay_rate_cd" style="pointer-events: none;">
		      							<option value="1">INR / MMBTU</option>
		      							<option value="2">USD / MMBTU</option>
		      						</select>
		      					</div>
				  			</div>
						</div>	
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Unauthorize overrun Charge<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="unauth_overrun_charge" value="<%=unauth_overrun_rate%>" onblur="checkNumber1(this,7,4);">
				    			</div>
				    			<div class="col">
		      						<select class="form-select form-select-sm" name="unauth_overrun_rate_cd" style="pointer-events: none;">
		      							<option value="1">INR / MMBTU</option>
		      							<option value="2">USD / MMBTU</option>
		      						</select>
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
						<%if(no_of_billing_dtl.equals("0") || no_of_billing_dtl.equals("")){ %>
							<div class="row m-b-5">
								<div align="center" class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.errorMessage("<b>Billing details configuration is pending for Contract.</b>")%>
								</div>
							</div>
						<%} %>
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

<input type="hidden" name="option" value="GTA_CONTRACT_MST">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="agreement_type" value="<%=agreement_type%>">

<input type="hidden" name="no_of_billing_dtl" value="<%=no_of_billing_dtl%>">
<input type="hidden" name="max_date" value="<%=max_date%>">
<input type="hidden" name="min_nom_date" value="<%=min_nom_date%>">

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