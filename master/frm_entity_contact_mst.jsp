<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh(opration)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var entity_role = document.forms[0].entity_role.value;
	var prev_entity_role = document.forms[0].prev_entity_role.value;
	
	if(entity_role != prev_entity_role)
	{
		counterparty_cd ="0";
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_entity_contact_mst.jsp?opration="+opration+"&u="+u+"&counterparty_cd="+counterparty_cd+"&entity_role="+entity_role;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

enableButton = true;
function doSubmit()
{
	var entity_role = document.forms[0].entity_role.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var person_name = document.forms[0].person_name.value;
	var eff_dt = document.forms[0].eff_dt.value;
	var designation = document.forms[0].designation.value;
	var email = document.forms[0].email.value;
	
	var opration = document.forms[0].opration.value;
	
	var address_type= document.forms[0].address_type;
	var count_addr_type=parseInt("0");
	
	var msg="";
	var flag=true;
	
	if(entity_role=="" || entity_role=="0" || trim(entity_role)=="")
	{
		msg+="Select Entity!\n";
		flag=false
	}
	if(counterparty_cd=="" || counterparty_cd=="0" || trim(counterparty_cd)=="")
	{
		msg+="Select Counterparty!\n";
		flag=false
	}
	if(person_name=="" || trim(person_name)=="")
	{
		msg+="Enter Person Name!\n";
		flag=false
	}
	if(eff_dt=="" || trim(eff_dt)=="")
	{
		msg+="Enter Eff Date!\n";
		flag=false
	}
	/*if(designation=="" || trim(designation)=="") AS PER VIJAY FEEDBACK 20230915
	{
		msg+="Enter Designation!\n";
		flag=false
	}*/
	
	if(email=="" || trim(email)=="")
	{
		msg+="Enter Email-Id!\n";
		flag=false
	}
	
	if(address_type!=null && address_type.length!=undefined)
 	{
  		for(var i=0;i<address_type.length;i++)
  		{
 			if(address_type[i].checked)
   			{
 				count_addr_type=parseInt(count_addr_type)+1;
   			} 
  		} 
 	}
 	else if(address_type!=null)
 	{
 		if(address_type.checked)
   		{
 			count_addr_type=parseInt(count_addr_type)+1;
   		}
 	}
	
	if(parseInt(count_addr_type) == 0)
	{
		msg+="Select atleast one(1) address type for Submit!\n";
		flag=false
	}
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" Entity Contact Detail?")
		if(a)
		{
			document.forms[0].option.value="ENTITY_CONTACT_DETAILS"
			document.getElementById("loading").style.visibility = "visible";
			editAllowedOnCpStatus = true;
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function setActiveInactive(obj)
{
	if(obj.checked)
	{
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status").value="Y";
	}
	else
	{
		document.getElementById("lb").innerHTML="Inactive";
		document.getElementById("status").value="N";
	}
}

function setActiveInactive2(obj,index)
{
	if(obj.checked)
	{
		document.getElementById("lb_flag"+index).innerHTML="Active";
		document.getElementById("flag"+index).value="Y";
	}
	else
	{
		document.getElementById("lb_flag"+index).innerHTML="Inactive";
		document.getElementById("flag"+index).value="N";
	}
}

function doModify(SEQ_NO,PERSON_NM,EFF_DT,DESIGNATION,STATUS,ADDR_FLAG,ADD_ADDRESS,PHONE,MOBILE,FAX1,FAX2,EMAIL,NOM,JT,INV,FM,PM,OTHER,RM,FLAG,TO_NOM,TO_FM,TO_INV,TO_JT,TO_PM,TO_OTHER,TO_RM,DLNG_NOM,DLNG_JT,DLNG_INV,DLNG_FM,DLNG_PM,DLNG_OTHER,DLNG_RM,DLNG_F402,DLNG_TO_NOM,DLNG_TO_FM,DLNG_TO_INV,DLNG_TO_JT,DLNG_TO_PM,DLNG_TO_OTHER,DLNG_TO_RM,DLNG_TO_F402)
{
	document.forms[0].person_name.value=PERSON_NM;
	document.forms[0].seq_no.value=SEQ_NO;
	document.forms[0].eff_dt.value=EFF_DT;
	document.forms[0].designation.value=DESIGNATION;
	if(STATUS=='Y')
	{
		document.forms[0].active.checked=true;
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status").value="Y";
	}
	else
	{
		document.forms[0].active.checked=false;
		document.getElementById("lb").innerHTML="Inactive";
		document.getElementById("status").value="N";
	}
	
	var address_type= document.forms[0].address_type;
	var additional_address = document.forms[0].additional_address;
	var phone = document.forms[0].phone;
	var fax1 = document.forms[0].fax1;
	var fax2 = document.forms[0].fax2;
	
	var to_nom = document.forms[0].to_nom;
	var to_inv = document.forms[0].to_inv;
	var to_jt = document.forms[0].to_jt;
	var to_fm = document.forms[0].to_fm;	
	var to_pm = document.forms[0].to_pm;
	var to_other = document.forms[0].to_other;
	var to_rm = document.forms[0].to_rm;
	
	var dlng_to_nom = document.forms[0].dlng_to_nom;
	var dlng_to_inv = document.forms[0].dlng_to_inv;
	var dlng_to_jt = document.forms[0].dlng_to_jt;
	var dlng_to_fm = document.forms[0].dlng_to_fm;	
	var dlng_to_pm = document.forms[0].dlng_to_pm;
	var dlng_to_other = document.forms[0].dlng_to_other;
	var dlng_to_rm = document.forms[0].dlng_to_rm;
	var dlng_to_f402 = document.forms[0].dlng_to_f402;
	
	var sepAddrFlag = ADDR_FLAG.split("@@");
	var sepAddrLine = ADD_ADDRESS.split("@@");
	var sepPhone = PHONE.split("@@");
	var sepFax1 = FAX1.split("@@");
	var sepFax2 = FAX2.split("@@");
	var sepFlag = FLAG.split("@@");
	
	var sepToNom = TO_NOM.split("@@");
	var sepToInv = TO_INV.split("@@");
	var sepToJt = TO_JT.split("@@");
	var sepToFm = TO_FM.split("@@");
	var sepToPm = TO_PM.split("@@");
	var sepToOther = TO_OTHER.split("@@");
	var sepToRm = TO_RM.split("@@");
	
	var entity_role = document.forms[0].entity_role.value;
	
	if(entity_role == "C" || entity_role == "T" || entity_role == "B" || entity_role == "G")
	{
		var sepDlngToNom = DLNG_TO_NOM.split("@@");
		var sepDlngToInv = DLNG_TO_INV.split("@@");
		var sepDlngToJt = DLNG_TO_JT.split("@@");
		var sepDlngToFm = DLNG_TO_FM.split("@@");
		var sepDlngToPm = DLNG_TO_PM.split("@@");
		var sepDlngToOther = DLNG_TO_OTHER.split("@@");
		var sepDlngToRm = DLNG_TO_RM.split("@@");
		var sepDlngToF402 = DLNG_TO_F402.split("@@");
	}
	
	if(address_type!=null && address_type.length!=undefined)
 	{
  		for(var i=0;i<address_type.length;i++)
  		{
   			for(var j=0;j<sepAddrFlag.length;j++)
   			{
     			if(address_type[i].value == sepAddrFlag[j])
     			{
     				address_type[i].checked = true;
     				address_type[i].style.pointerEvents = "none";
     				additional_address[i].value=sepAddrLine[j];
     				phone[i].value=sepPhone[j];
     				fax1[i].value=sepFax1[j];
     				fax2[i].value=sepFax2[j];
     				
     				to_nom[i].value=sepToNom[j];
     	  			to_inv[i].value=sepToInv[j];
     	  			to_jt[i].value=sepToJt[j];
     	  			to_fm[i].value=sepToFm[j];
     	  			to_pm[i].value=sepToPm[j];
     	  			to_other[i].value=sepToOther[j];
     	  			to_rm[i].value=sepToRm[j];
     	  			
     	  			if(entity_role == "C" || entity_role == "T" || entity_role == "B" || entity_role == "G")
     	  			{
     	  				dlng_to_nom[i].value=sepDlngToNom[j];
         	  			dlng_to_inv[i].value=sepDlngToInv[j];
         	  			dlng_to_jt[i].value=sepDlngToJt[j];
         	  			dlng_to_fm[i].value=sepDlngToFm[j];
         	  			dlng_to_pm[i].value=sepDlngToPm[j];
         	  			dlng_to_other[i].value=sepDlngToOther[j];
         	  			dlng_to_rm[i].value=sepDlngToRm[j];
         	  			dlng_to_f402[i].value=sepDlngToF402[j];
     	  			}
     				
     				if(sepFlag[j]=='Y')
     				{
     					document.forms[0].active_flag[i].checked=true;
     					document.getElementById("lb_flag"+i).innerHTML="Active";
     					document.getElementById("flag"+i).value="Y";
     				}
     				else
     				{
     					document.forms[0].active_flag[i].checked=false;
     					document.getElementById("lb_flag"+i).innerHTML="Inactive";
     					document.getElementById("flag"+i).value="N";
     				}
     				
     				enable_disable(address_type[i],i);
     			}
   			} 
  		} 
 	}
 	else if(address_type!=null)
 	{
   		for(var j=0;j<sepAddrFlag.length;j++)
   		{
   			if(address_type.value == sepAddrFlag[j])
     		{
   				address_type.checked = true;
   				address_type.style.pointerEvents = "none";
   				additional_address.value=sepAddrLine[j];
 				phone.value=sepPhone[j];
 				fax1.value=sepFax1[j];
 				fax2.value=sepFax2[j];
 				
 				to_nom.value=sepToNom[j];
 	  			to_inv.value=sepToInv[j];
 	  			to_jt.value=sepToJt[j];
 	  			to_fm.value=sepToFm[j];
 	  			to_pm.value=sepToPm[j];
 	  			to_other.value=sepToOther[j];
 	  			to_rm.value=sepToRm[j];
 	  			
 	  			if(entity_role == "C" || entity_role == "T" || entity_role == "B" || entity_role == "G")
 	  			{
 	  				dlng_to_nom.value=sepDlngToNom[j];
 	 	  			dlng_to_inv.value=sepDlngToInv[j];
 	 	  			dlng_to_jt.value=sepDlngToJt[j];
 	 	  			dlng_to_fm.value=sepDlngToFm[j];
 	 	  			dlng_to_pm.value=sepDlngToPm[j];
 	 	  			dlng_to_other.value=sepDlngToOther[j];
 	 	  			dlng_to_rm.value=sepDlngToRm[j];
 	 	  			dlng_to_f402.value=sepDlngToF402[j];
 	  			}
 				
 				if(sepFlag[j]=='Y')
 				{
 					document.forms[0].active_flag.checked=true;
 					document.getElementById("lb_flag0").innerHTML="Active";
 					document.getElementById("flag0").value="Y";
 				}
 				else
 				{
 					document.forms[0].active_flag.checked=false;
 					document.getElementById("lb_flag0").innerHTML="Inactive";
 					document.getElementById("flag0").value="N";
 				}
 				
 				enable_disable(address_type,"0");
     		}
   		} 
 	}
	
	document.forms[0].mobile.value=MOBILE;
	document.forms[0].email.value=EMAIL;
	if(NOM=="Y")
	{
		document.forms[0].nom.checked=true;
	}
	else
	{
		document.forms[0].nom.checked=false;	
	}
	
	if(INV=="Y")
	{
		document.forms[0].inv.checked=true;
	}
	else
	{
		document.forms[0].inv.checked=false;	
	}
	
	if(JT=="Y")
	{
		document.forms[0].jt.checked=true;
	}
	else
	{
		document.forms[0].jt.checked=false;	
	}
	
	if(FM=="Y")
	{
		document.forms[0].fm.checked=true;
	}
	else
	{
		document.forms[0].fm.checked=false;	
	}
	
	if(PM=="Y")
	{
		document.forms[0].pm.checked=true;
	}
	else
	{
		document.forms[0].pm.checked=false;	
	}
	
	if(OTHER=="Y")
	{
		document.forms[0].other.checked=true;
	}
	else
	{
		document.forms[0].other.checked=false;	
	}
	
	if(RM=="Y")
	{
		document.forms[0].rm.checked=true;
	}
	else
	{
		document.forms[0].rm.checked=false;	
	}
	
	var entity_role = document.forms[0].entity_role.value;
	
	if(entity_role == "C" || entity_role == "T" || entity_role == "B" || entity_role == "G")
	{
		if(DLNG_NOM=="Y")
		{
			document.forms[0].dlng_nom.checked=true;
		}
		else
		{
			document.forms[0].dlng_nom.checked=false;	
		}
		
		if(DLNG_INV=="Y")
		{
			document.forms[0].dlng_inv.checked=true;
		}
		else
		{
			document.forms[0].dlng_inv.checked=false;	
		}
		
		if(DLNG_JT=="Y")
		{
			document.forms[0].dlng_jt.checked=true;
		}
		else
		{
			document.forms[0].dlng_jt.checked=false;	
		}
		
		if(DLNG_FM=="Y")
		{
			document.forms[0].dlng_fm.checked=true;
		}
		else
		{
			document.forms[0].dlng_fm.checked=false;	
		}
		
		if(DLNG_PM=="Y")
		{
			document.forms[0].dlng_pm.checked=true;
		}
		else
		{
			document.forms[0].dlng_pm.checked=false;	
		}
		
		if(DLNG_OTHER=="Y")
		{
			document.forms[0].dlng_other.checked=true;
		}
		else
		{
			document.forms[0].dlng_other.checked=false;	
		}
		
		if(DLNG_RM=="Y")
		{
			document.forms[0].dlng_rm.checked=true;
		}
		else
		{
			document.forms[0].dlng_rm.checked=false;	
		}

		if(DLNG_F402=="Y")
		{
			document.forms[0].dlng_f402.checked=true;
		}
		else
		{
			document.forms[0].dlng_f402.checked=false;	
		}
	}
	
	document.forms[0].opration.value="MODIFY";
}

function doClear()
{
	document.forms[0].person_name.value="";
	document.forms[0].seq_no.value="";
	document.forms[0].eff_dt.value="";
	document.forms[0].designation.value="";
	
	document.forms[0].active.checked=true;
	document.getElementById("lb").innerHTML="Active";
	document.getElementById("status").value="Y";
	
	document.forms[0].mobile.value="";
	document.forms[0].nom.checked=false;
	document.forms[0].inv.checked=false;
	document.forms[0].jt.checked=false;
	document.forms[0].fm.checked=false;	
	document.forms[0].pm.checked=false;
	document.forms[0].other.checked=false;	
	document.forms[0].rm.checked=false;
	
	var entity_role = document.forms[0].entity_role.value;
	if(entity_role == "C" || entity_role == "T" || entity_role == "B" || entity_role == "G")
	{
		document.forms[0].dlng_nom.checked=false;
		document.forms[0].dlng_inv.checked=false;
		document.forms[0].dlng_jt.checked=false;
		document.forms[0].dlng_fm.checked=false;	
		document.forms[0].dlng_pm.checked=false;
		document.forms[0].dlng_other.checked=false;	
		document.forms[0].dlng_rm.checked=false;
		document.forms[0].dlng_f402.checked=false;
	}
	
	var address_type = document.forms[0].address_type;
	var additional_address = document.forms[0].additional_address;
	var phone = document.forms[0].phone;
	var fax1 = document.forms[0].fax1;
	var fax2 = document.forms[0].fax2;
	var flag = document.forms[0].flag;
	
	var to_nom = document.forms[0].to_nom;
	var to_inv = document.forms[0].to_inv;
	var to_jt = document.forms[0].to_jt;
	var to_fm = document.forms[0].to_fm;	
	var to_pm = document.forms[0].to_pm;
	var to_other = document.forms[0].to_other;
	var to_rm = document.forms[0].to_rm;
	
	var dlng_to_nom = document.forms[0].dlng_to_nom;
	var dlng_to_inv = document.forms[0].dlng_to_inv;
	var dlng_to_jt = document.forms[0].dlng_to_jt;
	var dlng_to_fm = document.forms[0].dlng_to_fm;	
	var dlng_to_pm = document.forms[0].dlng_to_pm;
	var dlng_to_other = document.forms[0].dlng_to_other;
	var dlng_to_rm = document.forms[0].dlng_to_rm;
	var dlng_to_f402 = document.forms[0].dlng_to_f402;
	
	if(address_type!=null && address_type.length!=undefined)
 	{
  		for(var i=0;i<address_type.length;i++)
  		{
  			address_type[i].checked=false;
  			address_type[i].style.pointerEvents = "auto";
  			additional_address[i].value="";
  			phone[i].value="";
  			fax1[i].value="";
  			fax2[i].value="";
  			
  			to_nom[i].value="";
  			to_inv[i].value="";
  			to_jt[i].value="";
  			to_fm[i].value="";
  			to_pm[i].value="";
  			to_other[i].value="";
  			to_rm[i].value="";

  			dlng_to_nom[i].value="";
  			dlng_to_inv[i].value="";
  			dlng_to_jt[i].value="";
  			dlng_to_fm[i].value="";
  			dlng_to_pm[i].value="";
  			dlng_to_other[i].value="";
  			dlng_to_rm[i].value="";
  			dlng_to_f402[i].value="";
  			
  			document.forms[0].active_flag[i].checked=true;
  			document.getElementById("lb_flag"+i).innerHTML="Active";
  			document.getElementById("flag"+i).value="Y";
  			
  			enable_disable(address_type[i],i);
  		} 
 	}
 	else if(address_type!=null)
 	{
 		address_type.checked=false;
 		address_type.style.pointerEvents = "auto";
		additional_address.value="";
		phone.value="";
		fax1.value="";
		fax2.value="";
		
		to_nom.value="";
		to_inv.value="";
		to_jt.value="";
		to_fm.value="";
		to_pm.value="";
		to_other.value="";
		to_rm.value="";

		dlng_to_nom.value="";
		dlng_to_inv.value="";
		dlng_to_jt.value="";
		dlng_to_fm.value="";
		dlng_to_pm.value="";
		dlng_to_other.value="";
		dlng_to_rm.value="";
		dlng_to_f402.value="";
		
		document.forms[0].active_flag.checked=true;
		document.getElementById("lb_flag0").innerHTML="Active";
		document.getElementById("flag0").value="Y";
		
		enable_disable(address_type,"0");
 	}
	
	document.forms[0].opration.value="INSERT";
}

function hide_show(id1,id2)
{	
	if(document.getElementById(id1).style.display=='none'){
		document.getElementById(id1).style.display='table-row-group';
		document.getElementById(id2).className='fa fa-compress';
	}else{
		document.getElementById(id1).style.display='none';
		document.getElementById(id2).className='fa fa-expand';
	}
	
	if(document.forms[0].prev_display.value != "" && document.forms[0].prev_display1.value !="")
	{
		if(document.forms[0].prev_display.value != id1 && document.forms[0].prev_display1.value != id2)
		{
			document.getElementById(document.forms[0].prev_display.value).style.display='none';
			document.getElementById(document.forms[0].prev_display1.value).className='fa fa-expand';
		}
	}
	document.forms[0].prev_display.value=id1;
	document.forms[0].prev_display1.value=id2;
} 

function enable_disable(obj,index)
{
	var additional_address = document.getElementById("additional_address"+index);
	var phone = document.getElementById("phone"+index);
	var fax1 = document.getElementById("fax1"+index);
	var fax2 = document.getElementById("fax2"+index);
	var flag = document.getElementById("flag"+index);
	
	var to_nom = document.getElementById("to_nom"+index);
	var to_inv = document.getElementById("to_inv"+index);
	var to_jt = document.getElementById("to_jt"+index);
	var to_fm = document.getElementById("to_fm"+index);
	var to_pm = document.getElementById("to_pm"+index);
	var to_other = document.getElementById("to_other"+index);
	var to_rm = document.getElementById("to_rm"+index);
	
	var dlng_to_nom = document.getElementById("dlng_to_nom"+index);
	var dlng_to_inv = document.getElementById("dlng_to_inv"+index);
	var dlng_to_jt = document.getElementById("dlng_to_jt"+index);
	var dlng_to_fm = document.getElementById("dlng_to_fm"+index);
	var dlng_to_pm = document.getElementById("dlng_to_pm"+index);
	var dlng_to_other = document.getElementById("dlng_to_other"+index);
	var dlng_to_rm = document.getElementById("dlng_to_rm"+index);
	var dlng_to_f402 = document.getElementById("dlng_to_f402"+index);
	
	if(obj.checked)
	{
		additional_address.disabled=false;
		phone.disabled=false;
		fax1.disabled=false;
		fax2.disabled=false;
		flag.disabled=false;
		
		to_nom.disabled=false;
		to_inv.disabled=false;
		to_jt.disabled=false;
		to_fm.disabled=false;
		to_pm.disabled=false;
		to_other.disabled=false;
		to_rm.disabled=false;
		
		dlng_to_nom.disabled=false;
		dlng_to_inv.disabled=false;
		dlng_to_jt.disabled=false;
		dlng_to_fm.disabled=false;
		dlng_to_pm.disabled=false;
		dlng_to_other.disabled=false;
		dlng_to_rm.disabled=false;
		dlng_to_f402.disabled=false;
	}
	else
	{
		additional_address.disabled=true;
		phone.disabled=true;
		fax1.disabled=true;
		fax2.disabled=true;
		flag.disabled=true;
		
		to_nom.disabled=true;
		to_inv.disabled=true;
		to_jt.disabled=true;
		to_fm.disabled=true;
		to_pm.disabled=true;
		to_other.disabled=true;
		to_rm.disabled=true;
		
		dlng_to_nom.disabled=true;
		dlng_to_inv.disabled=true;
		dlng_to_jt.disabled=true;
		dlng_to_fm.disabled=true;
		dlng_to_pm.disabled=true;
		dlng_to_other.disabled=true;
		dlng_to_rm.disabled=true;
		dlng_to_f402.disabled=true;
	}
}

/* function entityBasedNotType()
{
	var entity_role = document.forms[0].entity_role.value;
	
	var nomDiv = document.getElementById("nomDiv");
	var invDiv = document.getElementById("invDiv");
	var jtDiv = document.getElementById("jtDiv");
	var fmDiv = document.getElementById("fmDiv");
	var pmDiv = document.getElementById("pmDiv");
	var othDiv = document.getElementById("othDiv");
	var rmDiv = document.getElementById("rmDiv");
	
	var nomDlngDiv = document.getElementById("nomDlngDiv");
	var invDlngDiv = document.getElementById("invDlngDiv");
	var jtDlngDiv = document.getElementById("jtDlngDiv");
	var fmDlngDiv = document.getElementById("fmDlngDiv");
	var pmDlngDiv = document.getElementById("pmDlngDiv");
	var othDlngDiv = document.getElementById("othDlngDiv");
	var rmDlngDiv = document.getElementById("rmDlngDiv");
	
	if(entity_role != "" || entity_role != "0")
	{
		if(entity_role == "C")
		{
			invDiv.style.display = "block";
			invDlngDiv.style.display = "block";
			
			jtDiv.style.display = "block";
			jtDlngDiv.style.display = "block";
			
			rmDiv.style.display = "none";
			rmDlngDiv.style.display = "none";
		}
		else if(entity_role == "T" || entity_role == "R")
		{
			rmDiv.style.display = "block";
			rmDlngDiv.style.display = "block";
			
			jtDiv.style.display = "block";
			jtDlngDiv.style.display = "block";
			
			invDiv.style.display = "none";
			invDlngDiv.style.display = "none";
		}
		else if(entity_role == "V" || entity_role == "S" || entity_role == "H")
		{
			rmDiv.style.display = "block";
			rmDlngDiv.style.display = "block";

			invDiv.style.display = "none";
			invDlngDiv.style.display = "none";
			
			jtDiv.style.display = "none";
			jtDlngDiv.style.display = "none";
		}
		else if(entity_role == "G")
		{
			//HM20250201 : Might Need to hide RM. After the Feedback.
		}
	}
} */

//Wait for the DOM to be fully loaded
document.addEventListener("DOMContentLoaded", function() {
    entityBasedNotType(); // Call your function when the DOM is ready
});

function entityBasedNotType() {
    var entity_role = document.forms[0].entity_role.value;
    
    // Get the elements by their IDs
    var nomDiv = document.getElementById("nomDiv");
    var invDiv = document.getElementById("invDiv");
    var jtDiv = document.getElementById("jtDiv");
    var fmDiv = document.getElementById("fmDiv");
    var pmDiv = document.getElementById("pmDiv");
    var othDiv = document.getElementById("othDiv");
    var rmDiv = document.getElementById("rmDiv");
    
    var nomDlngDiv = document.getElementById("nomDlngDiv");
    var invDlngDiv = document.getElementById("invDlngDiv");
    var jtDlngDiv = document.getElementById("jtDlngDiv");
    var fmDlngDiv = document.getElementById("fmDlngDiv");
    var pmDlngDiv = document.getElementById("pmDlngDiv");
    var othDlngDiv = document.getElementById("othDlngDiv");
    var rmDlngDiv = document.getElementById("rmDlngDiv");
    var frm402DlngDiv = document.getElementById("frm402DlngDiv");

    // Store the entity role in localStorage (optional)
    localStorage.setItem('entity_role', entity_role);
    
    // Make sure the entity_role is not empty and not "0"
    if (entity_role !== "" && entity_role !== "0")
    {
        if (entity_role == "C")
        {
            if (rmDiv) rmDiv.style.display = "none";
            if (rmDlngDiv) rmDlngDiv.style.display = "none";
            if (frm402DlngDiv) frm402DlngDiv.style.display = "none";
        }
        else if (entity_role == "T" || entity_role == "R")
        {
            if (invDiv) invDiv.style.display = "none";
            if (invDlngDiv) invDlngDiv.style.display = "none";
            if (frm402DlngDiv) frm402DlngDiv.style.display = "none";
        }
        else if (entity_role == "V" || entity_role == "S" || entity_role == "H")
        {
            if (invDiv) invDiv.style.display = "none";
            if (invDlngDiv) invDlngDiv.style.display = "none";
            if (frm402DlngDiv) frm402DlngDiv.style.display = "none";
            
            if (jtDiv) jtDiv.style.display = "none";
            if (jtDlngDiv) jtDlngDiv.style.display = "none";
            if (frm402DlngDiv) frm402DlngDiv.style.display = "none";
        }
        else if (entity_role == "G")
        {
        	if (frm402DlngDiv) frm402DlngDiv.style.display = "none";
            // HM20250201 : Might Need to hide RM. After the Feedback.
        }
    }
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");

if(entity_role.equals("B"))
{
	if(!owner_cd.equals(""))
	{
		counterparty_cd=owner_cd;
	}
}

dbcounterpty.setCallFlag("ENTITY_CONTACT_DETAILS");
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

Vector VPLANT_SEQ_NO = dbcounterpty.getVPLANT_SEQ_NO();
Vector VPLANT_ABBR = dbcounterpty.getVPLANT_ABBR();

Vector VBU_PLANT_SEQ_NO = dbcounterpty.getVBU_PLANT_SEQ_NO();
Vector VBU_PLANT_ABBR = dbcounterpty.getVBU_PLANT_ABBR();

Vector VSEQ_NO = dbcounterpty.getVSEQ_NO();
Vector VEFF_DT = dbcounterpty.getVEFF_DT();
Vector VPERSON_NM = dbcounterpty.getVPERSON_NM();
Vector VDESIGNATION = dbcounterpty.getVDESIGNATION();
Vector VPHONE = dbcounterpty.getVPHONE();
Vector VMOBILE = dbcounterpty.getVMOBILE();
Vector VFAX1 = dbcounterpty.getVFAX1();
Vector VFAX2 = dbcounterpty.getVFAX2();
Vector VEMAIL = dbcounterpty.getVEMAIL();
Vector VADDR_FLAG = dbcounterpty.getVADDR_FLAG();
Vector VADDR_TYPE = dbcounterpty.getVADDR_TYPE();
Vector VADD_ADDRESS = dbcounterpty.getVADD_ADDRESS();
Vector VNOM = dbcounterpty.getVNOM();
Vector VINV = dbcounterpty.getVINV();
Vector VJT = dbcounterpty.getVJT();
Vector VFM = dbcounterpty.getVFM();
Vector VPM = dbcounterpty.getVPM();
Vector VOTHER = dbcounterpty.getVOTHER();
Vector VRM = dbcounterpty.getVRM();

Vector VDLNG_NOM = dbcounterpty.getVDLNG_NOM();
Vector VDLNG_INV = dbcounterpty.getVDLNG_INV();
Vector VDLNG_JT = dbcounterpty.getVDLNG_JT();
Vector VDLNG_FM = dbcounterpty.getVDLNG_FM();
Vector VDLNG_PM = dbcounterpty.getVDLNG_PM();
Vector VDLNG_OTHER = dbcounterpty.getVDLNG_OTHER();
Vector VDLNG_RM = dbcounterpty.getVDLNG_RM();
Vector VDLNG_F402 = dbcounterpty.getVDLNG_F402();

Vector VSTATUS = dbcounterpty.getVSTATUS();
Vector VADDRESS_TYPE = dbcounterpty.getVADDRESS_TYPE();
Vector VADDRESS_NAME = dbcounterpty.getVADDRESS_NAME();
Vector VINDEX = dbcounterpty.getVINDEX();
Vector VFLAG = dbcounterpty.getVFLAG();

Vector VTEMP_PHONE = dbcounterpty.getVTEMP_PHONE();
Vector VTEMP_FAX1 = dbcounterpty.getVTEMP_FAX1();
Vector VTEMP_FAX2 = dbcounterpty.getVTEMP_FAX2();
Vector VTEMP_ADDR_FLAG = dbcounterpty.getVTEMP_ADDR_FLAG();
Vector VTEMP_ADD_ADDRESS = dbcounterpty.getVTEMP_ADD_ADDRESS();
Vector VTEMP_FLAG = dbcounterpty.getVTEMP_FLAG();

Vector VTEMP_TO_NOM = dbcounterpty.getVTEMP_TO_NOM();
Vector VTEMP_TO_INV = dbcounterpty.getVTEMP_TO_INV();
Vector VTEMP_TO_JT = dbcounterpty.getVTEMP_TO_JT();
Vector VTEMP_TO_FM = dbcounterpty.getVTEMP_TO_FM();
Vector VTEMP_TO_PM = dbcounterpty.getVTEMP_TO_PM();
Vector VTEMP_TO_OTHER = dbcounterpty.getVTEMP_TO_OTHER();
Vector VTEMP_TO_RM = dbcounterpty.getVTEMP_TO_RM();

Vector VTEMP_DLNG_TO_NOM = dbcounterpty.getVTEMP_DLNG_TO_NOM();
Vector VTEMP_DLNG_TO_INV = dbcounterpty.getVTEMP_DLNG_TO_INV();
Vector VTEMP_DLNG_TO_JT = dbcounterpty.getVTEMP_DLNG_TO_JT();
Vector VTEMP_DLNG_TO_FM = dbcounterpty.getVTEMP_DLNG_TO_FM();
Vector VTEMP_DLNG_TO_PM = dbcounterpty.getVTEMP_DLNG_TO_PM();
Vector VTEMP_DLNG_TO_OTHER = dbcounterpty.getVTEMP_DLNG_TO_OTHER();
Vector VTEMP_DLNG_TO_RM = dbcounterpty.getVTEMP_DLNG_TO_RM();
Vector VTEMP_DLNG_TO_F402 = dbcounterpty.getVTEMP_DLNG_TO_F402();

%>
<body>
<script>
	window.onload = function() {
		entityBasedNotType();
	}
</script>
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
							Entity Contact Master
						</div>
					 	<div class="btn-group">
							<select class="btn btn-outline-secondary btngrp <%if(!entity_role.equals("0")){%>btnactive<%}%>" name="entity_role" onchange="refresh('INSERT')">
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
				      				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=opration%>');" id="select_box">
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
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<%if(!entity_role.equals("0")){ %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Add/Modify Entity Contact Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" 
									data-bs-toggle="modal" 
									data-bs-target="#myModal"
									onclick="doClear();">Add New Contact Detail</label>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th rowspan="2"></th>
										<th rowspan="2">Eff Date</th>
										<th rowspan="2">Status</th>
										<th rowspan="2">Person Name</th>
										<th rowspan="2">Designation</th>
										<th rowspan="2">Mobile</th>
										<th rowspan="2">Email</th>
										<th rowspan="2">Address Type</th>
										<th rowspan="2">Additional Address Line</th>
										<th rowspan="2">Phone</th>
										<th rowspan="2">Fax 1</th>
										<th rowspan="2">Fax 2</th>
										<th colspan="7">RLNG Notification Type</th>
										<%if(entity_role.equals("C") || entity_role.equals("B") || entity_role.equals("T") || entity_role.equals("G")){ %>
										<th 
										<%if(entity_role.equals("B")){ %>
										colspan="8"
										<%}else{ %>
										colspan="7"
										<%} %>
										>DLNG Notification Type</th>
										<%} %>
									</tr>
									<tr>
										<th title="Nomination">NOM</th>
										<th title="Join Ticket/Allocation">JT/AO</th>
										<th title="Invoice">INV</th>
										<th title="Forced Measure">FM</th>
										<th title="Preventive Measure">PM</th>
										<th title="?">Other</th>
										<th title="Remittance">RM</th>
										<%if(entity_role.equals("C") || entity_role.equals("B") || entity_role.equals("T") || entity_role.equals("G")){ %>
										<th title="Nomination">NOM</th>
										<th title="Join Ticket/Allocation">JT/AO</th>
										<th title="Invoice">INV</th>
										<th title="Forced Measure">FM</th>
										<th title="Preventive Measure">PM</th>
										<th title="DLNG ?">Other</th>
										<th title="Remittance">RM</th>
										<%if(entity_role.equals("B")){ %>
										<th title="Form 402">Form 402</th>
										<%}%>
										<%} %>
									</tr>
								</thead>
								<tbody>
								<%int j=0;int k=0;
								if(VSEQ_NO.size()>0){ %>
								<%for(int i=0;i<VSEQ_NO.size(); i++){ 
									int size = Integer.parseInt(""+VINDEX.elementAt(i));
								%>
									<tr>
										<td>
											<div align="center">
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" 
													data-bs-toggle="modal" data-bs-target="#myModal"
													onclick="doClear();doModify('<%=VSEQ_NO.elementAt(i)%>','<%=VPERSON_NM.elementAt(i)%>','<%=VEFF_DT.elementAt(i)%>',
													'<%=VDESIGNATION.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>','<%=VTEMP_ADDR_FLAG.elementAt(i)%>',
													'<%=VTEMP_ADD_ADDRESS.elementAt(i)%>','<%=VTEMP_PHONE.elementAt(i)%>','<%=VMOBILE.elementAt(i)%>',
													'<%=VTEMP_FAX1.elementAt(i)%>','<%=VTEMP_FAX2.elementAt(i)%>','<%=VEMAIL.elementAt(i)%>',
													'<%=VNOM.elementAt(i)%>','<%=VJT.elementAt(i)%>','<%=VINV.elementAt(i)%>',
													'<%=VFM.elementAt(i)%>','<%=VPM.elementAt(i)%>','<%=VOTHER.elementAt(i)%>','<%=VRM.elementAt(i)%>',
													'<%=VTEMP_FLAG.elementAt(i)%>','<%=VTEMP_TO_NOM.elementAt(i)%>','<%=VTEMP_TO_FM.elementAt(i)%>',
													'<%=VTEMP_TO_INV.elementAt(i)%>','<%=VTEMP_TO_JT.elementAt(i)%>',
													'<%=VTEMP_TO_PM.elementAt(i)%>','<%=VTEMP_TO_OTHER.elementAt(i)%>','<%=VTEMP_TO_RM.elementAt(i)%>'
													<%if(entity_role.equals("C") || entity_role.equals("B") || entity_role.equals("T") || entity_role.equals("G")){ %>
													,'<%=VDLNG_NOM.elementAt(i)%>','<%=VDLNG_JT.elementAt(i)%>','<%=VDLNG_INV.elementAt(i)%>',
													'<%=VDLNG_FM.elementAt(i)%>','<%=VDLNG_PM.elementAt(i)%>','<%=VDLNG_OTHER.elementAt(i)%>','<%=VDLNG_RM.elementAt(i)%>','<%=VDLNG_F402.elementAt(i)%>',
													'<%=VTEMP_DLNG_TO_NOM.elementAt(i)%>','<%=VTEMP_DLNG_TO_FM.elementAt(i)%>',
													'<%=VTEMP_DLNG_TO_INV.elementAt(i)%>','<%=VTEMP_DLNG_TO_JT.elementAt(i)%>',
													'<%=VTEMP_DLNG_TO_PM.elementAt(i)%>','<%=VTEMP_DLNG_TO_OTHER.elementAt(i)%>','<%=VTEMP_DLNG_TO_RM.elementAt(i)%>','<%=VTEMP_DLNG_TO_F402.elementAt(i)%>'
													<%}%>);"></i>
												</font>
											</div>
										</td>
										<td align="center"><%=VEFF_DT.elementAt(i)%></td>
										<td align="center">
											<div align="center">
												<font style="color:<%if(VSTATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font>
												<%if(VSTATUS.elementAt(i).equals("Y")){%>
												Active
												<%}else{ %>
												Inactive
												<%} %>
											</div>
										</td>
										<td><%=VPERSON_NM.elementAt(i)%></td>
										<td><%=VDESIGNATION.elementAt(i)%></td>
										<td align="center"><%=VMOBILE.elementAt(i)%></td>
										<td><%=VEMAIL.elementAt(i)%></td>
										<td colspan="5" style="background:#bce6ff;" onclick="hide_show('tbody<%=i %>','hidCont<%=i%>');" valign="middle" align="left">
    										<span id="hidCont<%=i%>" class="fa fa-expand" title="Click here to show Address Type"></span>
    									</td>
										<td align="center" <%if(VNOM.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VNOM.elementAt(i)%></td>
										<td align="center" <%if(VJT.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VJT.elementAt(i)%></td>
										<td align="center" <%if(VINV.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VINV.elementAt(i)%></td>
										<td align="center" <%if(VFM.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VFM.elementAt(i)%></td>
										<td align="center" <%if(VPM.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VPM.elementAt(i)%></td>
										<td align="center" <%if(VOTHER.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VOTHER.elementAt(i)%></td>
										<td align="center" <%if(VRM.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VRM.elementAt(i)%></td>
										<%if(entity_role.equals("C") || entity_role.equals("B") || entity_role.equals("T") || entity_role.equals("G")){ %>
										<!-- DLNG -->
										<td align="center" <%if(VDLNG_NOM.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VDLNG_NOM.elementAt(i)%></td>
										<td align="center" <%if(VDLNG_JT.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VDLNG_JT.elementAt(i)%></td>
										<td align="center" <%if(VDLNG_INV.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VDLNG_INV.elementAt(i)%></td>
										<td align="center" <%if(VDLNG_FM.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VDLNG_FM.elementAt(i)%></td>
										<td align="center" <%if(VDLNG_PM.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VDLNG_PM.elementAt(i)%></td>
										<td align="center" <%if(VDLNG_OTHER.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VDLNG_OTHER.elementAt(i)%></td>
										<td align="center" <%if(VDLNG_RM.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VDLNG_RM.elementAt(i)%></td>
										<%if(entity_role.equals("B")){ %>
										<td align="center" <%if(VDLNG_F402.elementAt(i).equals("Y")){ %>style="color:green;font-weight: bold;"<%} %>><%=VDLNG_F402.elementAt(i)%></td>
										<%} %>
										<%} %>
									</tr>
									<%if(size>0){k=0;%>
									<tbody id="tbody<%=i%>" style="display:none;">
										<%for(j=j; j<VADDR_TYPE.size(); j++){ 
										k+=1;
										%>
											<tr>
												<%if(k==1){ %><td colspan="7" rowspan="<%=size%>" style="background:white;"></td><%} %>
												<td align="left">
													<font style="color:<%if(VFLAG.elementAt(j).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
														<i class="fa fa-circle fa-lg" ></i>
														&nbsp;
													</font>
													<%=VADDR_TYPE.elementAt(j)%>
												</td>
												<td><%=VADD_ADDRESS.elementAt(j)%></td>
												<td><%=VPHONE.elementAt(j)%></td>
												<td align="center"><%=VFAX1.elementAt(j)%></td>
												<td align="center"><%=VFAX2.elementAt(j) %></td>
												<%if(k==1){ %><td colspan="14" rowspan="<%=size%>" style="background:white;"></td><%}%>
											</tr>
										<%
											if(k==size)
											{
												j=j+1;
												break;
											}
										} %>
									</tbody>
									<%} %>
								<%} %>
								<%}else{ %>
									<tr>
										<td 
										<%if(entity_role.equals("C") || entity_role.equals("B") || entity_role.equals("T") || entity_role.equals("G")){ %>
											<%if(entity_role.equals("B")){ %>
											colspan="27"
											<%}else{%>
											colspan="26"
											<%} %>
										<%}else{ %>colspan="19"<%} %>align="center">
										<%=utilmsg.infoMessage("<b>No Entity Contact Detail Configured!</b>")%>
										</td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<%} %>	
				</div>
			</div>
		</div>
	</div>
</div>

<!-- MODEL -->
<div class="modal fade" id="myModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Entity Contact Details
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Person Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="person_name" value="" maxLength="100">
				      				<input type="hidden" class="form-control form-control-sm" name="seq_no" value="">
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
				      					<input type="hidden" name="old_plant_eff_dt" value="">
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" value="" 
			      						maxLength="10" onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Designation</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="designation" value="" maxLength="50">
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
									<div class="form-check form-switch">
										<input class="form-check-input" name="active" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked onclick="setActiveInactive(this);">
									  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb">
									  		Active
									  	</label>
									  	<input type="hidden" name="status" id="status" value="Y">
									</div>
								</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Mobile</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="mobile" value="" maxLength="20">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Email<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="email" value="" maxLength="40">
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
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Address Type</label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th></th>
										<th>Address Type</th>
										<th>Additional Address Line</th>
										<th>Phone</th>
										<th>Fax 1</th>
										<th>Fax 2</th>
										<th>Flag</th>
									</tr>
								</thead>
								<tbody>
								<%for(int i=0; i<VADDRESS_TYPE.size(); i++){ %>
									<tr>
										<td align="center">
											<input type="checkbox" class="form-check-input" name="address_type" 
											value="<%=VADDRESS_TYPE.elementAt(i)%>" onclick="enable_disable(this,'<%=i%>')">
											
											<input type="hidden" name="to_nom" id="to_nom<%=i%>" disabled>
											<input type="hidden" name="to_inv" id="to_inv<%=i%>" disabled>
											<input type="hidden" name="to_fm" id="to_fm<%=i%>" disabled>
											<input type="hidden" name="to_pm" id="to_pm<%=i%>" disabled>
											<input type="hidden" name="to_jt" id="to_jt<%=i%>" disabled>
											<input type="hidden" name="to_other" id="to_other<%=i%>" disabled>
											<input type="hidden" name="to_rm" id="to_rm<%=i%>" disabled>
											
											<input type="hidden" name="dlng_to_nom" id="dlng_to_nom<%=i%>" disabled>
											<input type="hidden" name="dlng_to_inv" id="dlng_to_inv<%=i%>" disabled>
											<input type="hidden" name="dlng_to_fm" id="dlng_to_fm<%=i%>" disabled>
											<input type="hidden" name="dlng_to_pm" id="dlng_to_pm<%=i%>" disabled>
											<input type="hidden" name="dlng_to_jt" id="dlng_to_jt<%=i%>" disabled>
											<input type="hidden" name="dlng_to_other" id="dlng_to_other<%=i%>" disabled>
											<input type="hidden" name="dlng_to_rm" id="dlng_to_rm<%=i%>" disabled>
											<input type="hidden" name="dlng_to_f402" id="dlng_to_f402<%=i%>" disabled>
										</td>
										<td><b><%=VADDRESS_NAME.elementAt(i)%></b></td>
										<td align="center">
											<div style="width:300px;">
												<input type="text" class="form-control form-control-sm" name="additional_address" id="additional_address<%=i%>" value="" maxLength="100" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="phone" id="phone<%=i%>" value="" maxLength="20" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:150px;">
												<input type="text" class="form-control form-control-sm" name="fax1" id="fax1<%=i%>" value="" maxLength="20" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:150px;">
												<input type="text" class="form-control form-control-sm" name="fax2" id="fax2<%=i%>" value="" maxLength="20" disabled>
											</div>
										</td>
										<td align="center">
											<div class="form-check form-switch">
												<input class="form-check-input" name="active_flag" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked onclick="setActiveInactive2(this,'<%=i%>');">
											  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb_flag<%=i%>">
											  		Active
											  	</label>
											  	<input type="hidden" name="flag" id="flag<%=i%>" value="Y" disabled>
											</div>
										</td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> RLNG Notification Type</label>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="form-group row">
								<div class="col-auto" align="center" id="nomDiv">
									<input type="checkbox" class="form-check-input" name="nom" value="Y">&nbsp;<b>NOM</b>
								</div>
								<div class="col-auto" align="center" id="jtDiv">
									<input type="checkbox" class="form-check-input" name="jt" value="Y">&nbsp;<b>JT/AO</b>
								</div>
								<div class="col-auto" align="center" id="invDiv">
									<input type="checkbox" class="form-check-input" name="inv" value="Y">&nbsp;<b>INV</b>
								</div>
								<div class="col-auto" align="center" id="fmDiv">
									<input type="checkbox" class="form-check-input" name="fm" value="Y">&nbsp;<b>FM</b>
								</div>
								<div class="col-auto" align="center" id="pmDiv">
									<input type="checkbox" class="form-check-input" name="pm" value="Y">&nbsp;<b>PM</b>
								</div>
								<div class="col-auto" align="center" id="othDiv">
									<input type="checkbox" class="form-check-input" name="other" value="Y">&nbsp;<b>Other</b>
								</div>
								<div class="col-auto" align="center" id="rmDiv">
									<input type="checkbox" class="form-check-input" name="rm" value="Y">&nbsp;<b>RM</b>
								</div>
							</div>
						</div>
					</div>
					<%if(entity_role.equals("C") || entity_role.equals("B") || entity_role.equals("T") || entity_role.equals("G")){ %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> DLNG Notification Type</label>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="form-group row">
								<div class="col-auto" align="center" id="nomDlngDiv">
									<input type="checkbox" class="form-check-input" name="dlng_nom" value="Y">&nbsp;<b>NOM</b>
								</div>
								<div class="col-auto" align="center" id="jtDlngDiv">
									<input type="checkbox" class="form-check-input" name="dlng_jt" value="Y">&nbsp;<b>JT/AO</b>
								</div>
								<div class="col-auto" align="center" id="invDlngDiv">
									<input type="checkbox" class="form-check-input" name="dlng_inv" value="Y">&nbsp;<b>INV</b>
								</div>
								<div class="col-auto" align="center" id="fmDlngDiv">
									<input type="checkbox" class="form-check-input" name="dlng_fm" value="Y">&nbsp;<b>FM</b>
								</div>
								<div class="col-auto" align="center" id="pmDlngDiv">
									<input type="checkbox" class="form-check-input" name="dlng_pm" value="Y">&nbsp;<b>PM</b>
								</div>
								<div class="col-auto" align="center" id="othDlngDiv">
									<input type="checkbox" class="form-check-input" name="dlng_other" value="Y">&nbsp;<b>Other</b>
								</div>
								<div class="col-auto" align="center" id="rmDlngDiv">
									<input type="checkbox" class="form-check-input" name="dlng_rm" value="Y">&nbsp;<b>RM</b>
								</div>
								<div class="col-auto" align="center" id="frm402DlngDiv">
									<input type="checkbox" class="form-check-input" name="dlng_f402" value="Y">&nbsp;<b>Form 402</b>
								</div>
							</div>
						</div>
					</div>
					<%} %>
				</div>
			</div>
			<div class="modal-footer cdfooter">
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

<input type="hidden" name="option" value="ENTITY_CONTACT_DETAILS">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="prev_entity_role" value="<%=entity_role%>">
<input type="hidden" name="old_value" value="">

<input type="hidden" name="prev_display" value="">
<input type="hidden" name="prev_display1" value="">

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