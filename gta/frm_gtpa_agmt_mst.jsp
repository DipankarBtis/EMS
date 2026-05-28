
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
	var agreement_type = document.forms[0].agreement_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gtpa_agmt_mst.jsp?counterparty_cd="+counterparty_cd+"&opration="+opration+"&u="+u+"&agreement_type="+agreement_type;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setActiveInactive(obj)
{
	if(obj.checked)
	{
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="Y";
	}
	else
	{
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="N";
	}
}

function doClear()
{
	document.forms[0].counterparty_cd.value="0";
	document.forms[0].agmt_name.value="";
	document.forms[0].agmt_no.value="";
	document.forms[0].agmt_rev_no.value="";
	document.forms[0].start_dt.value="";
	document.forms[0].end_dt.value="";
	document.forms[0].total_qty.value="";
	
	document.forms[0].calc_base[0].chacked=true;
	document.forms[0].calc_base[1].chacked=false;
	
	document.forms[0].active.checked=true;
	document.getElementById("lb").innerHTML="Active";
	document.getElementById("status_flag").value="Y";
	
	var entry_point_mapping = document.forms[0].entry_point_mapping;
	if(entry_point_mapping!=null && entry_point_mapping.length!=undefined)
 	{
  		for(var i=0;i<entry_point_mapping.length;i++)
  		{
   			entry_point_mapping[i].checked=false;
  		}
 	}
	else if(entry_point_mapping!=null)
	{
		entry_point_mapping.checked=false;
	}
	document.getElementById("EntryPointDisplay").innerHTML="";
	document.getElementById("EntryPointDisplay").style.display="none";
	
	var exit_point_mapping = document.forms[0].exit_point_mapping;
	if(exit_point_mapping!=null && exit_point_mapping.length!=undefined)
 	{
  		for(var i=0;i<exit_point_mapping.length;i++)
  		{
  			exit_point_mapping[i].checked=false;
  		}
 	}
	else if(exit_point_mapping!=null)
	{
		exit_point_mapping.checked=false;
	}
	
	document.getElementById("ExitPointDisplay").innerHTML="";
	document.getElementById("ExitPointDisplay").style.display="none";
		
	document.forms[0].opration.value="INSERT";
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agreement_type = document.forms[0].agreement_type.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var total_qty = document.forms[0].total_qty.value;
	var u = document.forms[0].u.value;
	var chk_trans_bu = document.forms[0].chk_trans_bu;
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var msg="";
	var flag=true;
	
	if(trim(counterparty_cd) == "" || counterparty_cd=="0")
	{
		msg+="Select Transporter!\n";
		flag=false;
	}
	if(trim(agreement_type) == "" || agreement_type=="0")
	{
		msg+="Select Agreement Type!\n";
		flag=false;
	}
	if(opration=="MODIFY")
	{
		if(trim(agmt_no) == "")
		{
			msg+="Agreement No is missing!\n";
			flag=false;
		}
		if(trim(agmt_rev_no) == "")
		{
			msg+="Agreement Rev No is missing!\n";
			flag=false;
		}
	}
	if(trim(start_dt) == "")
	{
		msg+="Select Start Date!\n";
		flag=false;
	}
	if(trim(end_dt) == "")
	{
		msg+="Select End Date!\n";
		flag=false;
	}
	if(trim(total_qty) == "")
	{
		msg+="Enter Total Qty!\n";
		flag=false;
	}
	
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
		msg += "Please Select Atleast One BU!\n";
		flag=false;
	}
	
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
			a= confirm("Do you Want to Modify?");
		}
		else
		{
			a= confirm("Do you Want to Submit?");
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

function doSubmitEntrySel()
{
	var entry_point_mapping = document.forms[0].entry_point_mapping;
	var entry_point_nm = document.forms[0].entry_point_nm;
	
	var display ="";
	if(entry_point_mapping!=null && entry_point_mapping.length!=undefined)
 	{
  		for(var i=0;i<entry_point_mapping.length;i++)
  		{
   			if(entry_point_mapping[i].checked)
   			{
   				if(display!="")
   				{
   					display+=", "+entry_point_nm[i].value;
   				}
   				else
   				{
   					display+=entry_point_nm[i].value;
   				}
   			} 
  		} 
 	}
 	else if(entry_point_mapping!=null)
 	{
   		if(entry_point_mapping.checked)
     	{
   			if(display!="")
			{
				display+=", "+entry_point_nm.value;
			}
			else
			{
				display+=entry_point_nm.value;
			}
   		} 
 	}
	
	document.getElementById("EntryPointDisplay").innerHTML=display;
	if(display != "")
	{
		document.getElementById("EntryPointDisplay").style.display="inline";
	}
}

function doSubmitExitSel()
{
	var exit_point_mapping = document.forms[0].exit_point_mapping;
	var exit_point_nm = document.forms[0].exit_point_nm;
	
	var display ="";
	if(exit_point_mapping!=null && exit_point_mapping.length!=undefined)
 	{
  		for(var i=0;i<exit_point_mapping.length;i++)
  		{
   			if(exit_point_mapping[i].checked)
   			{
   				if(display!="")
   				{
   					display+=", "+exit_point_nm[i].value;
   				}
   				else
   				{
   					display+=exit_point_nm[i].value;
   				}
   			} 
  		} 
 	}
 	else if(exit_point_mapping!=null)
 	{
   		if(exit_point_mapping.checked)
     	{
   			if(display!="")
			{
				display+=", "+exit_point_nm.value;
			}
			else
			{
				display+=exit_point_nm.value;
			}
   		} 
 	}
	
	document.getElementById("ExitPointDisplay").innerHTML=display;
	if(display != "")
	{
		document.getElementById("ExitPointDisplay").style.display="inline";
	}
}



var newWindow;
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agreement_type = document.forms[0].agreement_type.value;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_gtpa_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_gtpa_agmt_list.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type,"Agreement List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function openBillingDtl()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	var agreement_type = document.forms[0].agreement_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gtpa_billing_dtl.jsp?counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+
			"&u="+u+"&end_dt="+end_dt;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Agreement Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Agreement Billing Detail","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setAgmtDetail(countpty_cd,agmt_no,agmt_rev_no,agreement_type,agmt_nm)
{
	var opration = document.forms[0].opration.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gtpa_agmt_mst.jsp?counterparty_cd="+countpty_cd+"&opration="+opration+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&agreement_type="+agreement_type+
			"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setStatus(status)
{
	if(status=="Y")
	{
		document.forms[0].active.checked=true;
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="Y";
	}
	else
	{
		document.forms[0].active.checked=false;
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="N";
	}
}

function setBase(calc_base)
{
	if(calc_base=="GCV")
	{
		document.forms[0].calc_base[0].checked=true;
	}
	else if(calc_base=="NCV")
	{
		document.forms[0].calc_base[1].checked=true;
	}
	else
	{
		document.forms[0].calc_base[0].checked=true;
	}
}

function setValues(strTransBu,strBuPlant)
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

function checkAllEntryPoint(obj)
{
	var chk = document.forms[0].entry_point_mapping;
	if(chk!=null && chk.length!=undefined)
	{
		for(var i=0; i<chk.length; i++)
		{
			if(obj.checked)
			{
				chk[i].checked=true;
			}
			else
			{
				chk[i].checked=false;
			}
		}
	}
	else
	{
		if(obj.checked)
		{
			chk.checked=true;
		}
		else
		{
			chk.checked=false;
		}
	}
}
function checkAllExitPoint(obj)
{
	var chk = document.forms[0].exit_point_mapping;
	if(chk!=null && chk.length!=undefined)
	{
		for(var i=0; i<chk.length; i++)
		{
			if(obj.checked)
			{
				chk[i].checked=true;
			}
			else
			{
				chk[i].checked=false;
			}
		}
	}
	else
	{
		if(obj.checked)
		{
			chk.checked=true;
		}
		else
		{
			chk.checked=false;
		}
	}
}

function checkCont_Status(chk,flag)
{
	var chk_bu_plant = document.forms[0].chk_bu_plant;
	var chk_bu_plant_flg = document.forms[0].chk_bu_plant_flg.value;
	var chk_trans_bu = document.forms[0].chk_trans_bu;
	var chk_trans_bu_flg = document.forms[0].chk_trans_bu_flg.value;
	var chk_entry_point_flg = document.forms[0].chk_entry_point_flg.value;
	var chk_exit_pt_flg = document.forms[0].chk_exit_pt_flg.value;
	
	
	//For BU Unit
	if(flag=="BU")
	{
		var temp_chk_bu_plant_flg=chk_bu_plant_flg.split("@");
		if(temp_chk_bu_plant_flg!=null && temp_chk_bu_plant_flg.length!=undefined)
	 	{
	  		for(var i=0;i<temp_chk_bu_plant_flg.length;i++)
	  		{
     			if(temp_chk_bu_plant_flg[i] == chk.value && !chk.checked)
     			{
     				alert("Business Unit Configured in Contract! Can't be Unselected!");
     				chk.checked = true;
     				break;
     			}
	  		} 
	 	}
	 	else 
	 	{
   			if(temp_chk_bu_plant_flg[0] == chk.value && !chk.checked)
     		{
   				alert("Business Unit Configured in Contract! Can't be Unselected!");
   				chk.checked = true;
     		}
	 	}
	}
	
	//For Transporter Bu
	if(flag=="TRANS_BU")
	{
		var temp_chk_trans_bu_flg=chk_trans_bu_flg.split("@");
		if(temp_chk_trans_bu_flg!=null && temp_chk_trans_bu_flg.length!=undefined)
	 	{
	  		for(var i=0;i<temp_chk_trans_bu_flg.length;i++)
	  		{
     			if(temp_chk_trans_bu_flg[i] == chk.value && !chk.checked)
     			{
     				alert("Transporter BU Configured in Contract! Can't be Unselected!");
     				chk.checked = true;
     				break;
     			}
	  		} 
	 	}
	 	else 
	 	{
   			if(temp_chk_trans_bu_flg[0] == chk.value && !chk.checked)
     		{
   				alert("Transporter BU Configured in Contract! Can't be Unselected!");
   				chk.checked = true;
     		}
	 	}
	}
	
	//For Entry Point
	if(flag=="ENTRY")
	{
		var temp_chk_entry_point_flg=chk_entry_point_flg.split("@");
		if(temp_chk_entry_point_flg!=null && temp_chk_entry_point_flg.length!=undefined)
	 	{
	  		for(var i=0;i<temp_chk_entry_point_flg.length;i++)
	  		{
     			if(temp_chk_entry_point_flg[i] == chk.value && !chk.checked)
     			{
     				alert("Entry Point Configured in Contract! Can't be Unselected!");
     				chk.checked = true;
     				break;
     			}
	  		} 
	 	}
	 	else 
	 	{
   			if(temp_chk_entry_point_flg[0] == chk.value && !chk.checked)
     		{
   				alert("Entry Point Configured in Contract! Can't be Unselected!");
   				chk.checked = true;
     		}
	 	}
	}
	
	//For Exit Point
	if(flag=="EXIT")
	{
		var temp_chk_exit_pt_flg=chk_exit_pt_flg.split("@");
		if(temp_chk_exit_pt_flg!=null && temp_chk_exit_pt_flg.length!=undefined)
	 	{
	  		for(var i=0;i<temp_chk_exit_pt_flg.length;i++)
	  		{
     			if(temp_chk_exit_pt_flg[i] == chk.value && !chk.checked)
     			{
     				alert("Exit Point Configured in Contract! Can't be Unselected!");
     				chk.checked = true;
     				break;
     			}
	  		} 
	 	}
	 	else 
	 	{
   			if(temp_chk_exit_pt_flg[0] == chk.value && !chk.checked)
     		{
   				alert("Exit Point Configured in Contract! Can't be Unselected!");
   				chk.checked = true;
     		}
	 	}
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GtaMaster" id="gtpa" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String dateTime = utildate.getSysdateWithTime24hr();
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");

gtpa.setCallFlag("GTPA_AGMT");
gtpa.setCounterparty_cd(counterparty_cd);
gtpa.setComp_cd(owner_cd);
gtpa.setAgmt_no(agmt_no);
gtpa.setAgmt_rev_no(agmt_rev_no);
gtpa.setAgreement_type(agreement_type);
gtpa.setOpration(opration);
gtpa.init();

Vector VCOUNTERPARTY_CD = gtpa.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = gtpa.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = gtpa.getVCOUNTERPARTY_ABBR();

String start_dt = gtpa.getStart_dt();
String end_dt = gtpa.getEnd_dt();
String agmt_name = gtpa.getAgmt_name();
String agmt_trans_qty =gtpa.getAgmt_trans_qty();
String agmt_trans_qty_unit =gtpa.getAgmt_trans_qty_unit();
String agmt_calc_base =gtpa.getAgmt_calc_base();
String agmt_status = gtpa.getAgmt_status();
String sel_entry_mapping = gtpa.getSel_entry_mapping();
String sel_exit_mapping = gtpa.getSel_exit_mapping();

Vector VBU_CD = gtpa.getVBU_CD();
Vector VBU_PLANT_NM = gtpa.getVBU_PLANT_NM();
Vector VBU_PLANT_ABBR = gtpa.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = gtpa.getVBU_PLANT_SEQ_NO();

Vector VTRANS_BU_NM = gtpa.getVTRANS_BU_NM();
Vector VTRANS_BU_ABBR = gtpa.getVTRANS_BU_ABBR();
Vector VTRANS_BU_SEQ_NO = gtpa.getVTRANS_BU_SEQ_NO();

Vector VSEL_TRANS_BU_SEQ_NO = gtpa.getVSEL_TRANS_BU_SEQ_NO();
Vector VCONT_SEL_TRANS_BU_SEQ_NO = gtpa.getVCONT_SEL_TRANS_BU_SEQ_NO();
Vector VSEL_BU_PLANT_SEQ_NO = gtpa.getVSEL_BU_PLANT_SEQ_NO();
Vector VCONT_SEL_BU_PLANT_SEQ_NO = gtpa.getVCONT_SEL_BU_PLANT_SEQ_NO();
Vector VCONT_SEL_ENTRY_POINT = gtpa.getVCONT_SEL_ENTRY_POINT();
Vector VCONT_SEL_EXIT_POINT = gtpa.getVCONT_SEL_EXIT_POINT();

Vector VENTRY_COUNTERPTY_CD = gtpa.getVENTRY_COUNTERPTY_CD();
Vector VENTRY_COUNTERPTY_ABBR = gtpa.getVENTRY_COUNTERPTY_ABBR();
Vector VENTRY_PLANT_SEQ = gtpa.getVENTRY_PLANT_SEQ();
Vector VENTRY_PLANT_ABBR = gtpa.getVENTRY_PLANT_ABBR();
Vector VENTRY_PLANT_NM = gtpa.getVENTRY_PLANT_NM();
Vector VENTRY_METER_SEQ = gtpa.getVENTRY_METER_SEQ();
Vector VENTRY_METER_REF = gtpa.getVENTRY_METER_REF();
Vector VENTRY_MAPPING = gtpa.getVENTRY_MAPPING();
Vector VENTRY_STATUS = gtpa.getVENTRY_STATUS();
Vector VSEL_ENTRY_MAPPING = gtpa.getVSEL_ENTRY_MAPPING();

Vector VEXIT_COUNTERPTY_CD = gtpa.getVEXIT_COUNTERPTY_CD();
Vector VEXIT_COUNTERPTY_ABBR = gtpa.getVEXIT_COUNTERPTY_ABBR();
Vector VEXIT_PLANT_SEQ = gtpa.getVEXIT_PLANT_SEQ();
Vector VEXIT_PLANT_ABBR = gtpa.getVEXIT_PLANT_ABBR();
Vector VEXIT_PLANT_NM = gtpa.getVEXIT_PLANT_NM();
Vector VEXIT_ENTITY = gtpa.getVEXIT_ENTITY();
Vector VEXIT_ENTITY_NM = gtpa.getVEXIT_ENTITY_NM();
Vector VEXIT_MAPPING = gtpa.getVEXIT_MAPPING();
Vector VEXIT_STATUS = gtpa.getVEXIT_STATUS();
Vector VSEL_EXIT_MAPPING = gtpa.getVSEL_EXIT_MAPPING();

String no_of_billing_dtl=gtpa.getNo_of_billing_dtl();

if(agmt_status.equals(""))
{
	agmt_status="Y";
}
if(agreement_type.equals(""))
{
	agreement_type="0";
}

String strTransBu="";
for(int i=0;i<VSEL_TRANS_BU_SEQ_NO.size();i++)
{
	strTransBu = strTransBu + VSEL_TRANS_BU_SEQ_NO.elementAt(i)+"@";
}
String strBuPlant="";
for(int i=0;i<VSEL_BU_PLANT_SEQ_NO.size();i++)
{
	strBuPlant = strBuPlant + VSEL_BU_PLANT_SEQ_NO.elementAt(i)+"@";
}

String strContTransBu="";
for(int i=0;i<VCONT_SEL_TRANS_BU_SEQ_NO.size();i++)
{
	strContTransBu = strContTransBu + VCONT_SEL_TRANS_BU_SEQ_NO.elementAt(i)+"@";
}
String strContBuPlant="";
for(int i=0;i<VCONT_SEL_BU_PLANT_SEQ_NO.size();i++)
{
	strContBuPlant = strContBuPlant + VCONT_SEL_BU_PLANT_SEQ_NO.elementAt(i)+"@";

}
String strContEntryPoint="";
for(int i=0;i<VCONT_SEL_ENTRY_POINT.size();i++)
{
	strContEntryPoint = strContEntryPoint + VCONT_SEL_ENTRY_POINT.elementAt(i)+"@";
}
String strContExitpt="";
for(int i=0;i<VCONT_SEL_EXIT_POINT.size();i++)
{
	strContExitpt = strContExitpt + VCONT_SEL_EXIT_POINT.elementAt(i)+"@";
}

%>
<body onload="setEntryPointDtl('<%=sel_entry_mapping%>');setExitPointDtl('<%=sel_exit_mapping%>');setStatus('<%=agmt_status%>');setBase('<%=agmt_calc_base%>');setValues('<%=strTransBu%>','<%=strBuPlant%>');">
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
					    	GTA/MPSA
					    </div>
					    <div class="btn-group">
						  <lable class="btn btn-outline-secondary btngrp <%if(opration.equals("INSERT")){%>btnactive<%}%>" onclick="refresh('INSERT');"><i class="fa fa-plus-circle"></i>&nbsp;New</lable>
						  <lable class="btn btn-outline-secondary btngrp <%if(opration.equals("MODIFY")){%>btnactive<%}%>" onclick="refresh('MODIFY');"><i class="fa fa-edit"></i>&nbsp;Modify</lable>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
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
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Agreement Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="agreement_type" onchange="refresh('<%=opration%>');">
				    					<option value="0">--Select--</option>
				    					<option value="G">GTA - Transportation</option>
				    					<option value="P">MPSA - Parking</option>
				    				</select>
				    				<script>document.forms[0].agreement_type.value="<%=agreement_type%>"</script>
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
				    			<input type="button" class="btn btn-info btn-sm select_btn" value="Select Agreement" onclick="openContList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="cont_name" value="<%=agmt_name %>" maxLength="50" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Status<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="form-check form-switch">
										<input class="form-check-input" name="active" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked onclick="setActiveInactive(this);">
									  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb">
									  		Active
									  	</label>
									  	<input type="hidden" name="status_flag" id="status_flag" value="Y">
									</div>
				    			</div>
							</div>
						</div>
					</div>
					<%}else{ %>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Agreement#</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="agmt_name" value="<%=agmt_name %>" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="" readOnly>
				    				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="" readOnly>
				    			</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Status<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="form-check form-switch">
										<input class="form-check-input" name="active" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked onclick="setActiveInactive(this);">
									  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb">
									  		Active
									  	</label>
									  	<input type="hidden" name="status_flag" id="status_flag" value="Y">
									</div>
				    			</div>
							</div>
						</div>
					</div>
					<%}%>
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
				      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="start_dt" value="<%=start_dt %>" maxLength="10" 
			      						onblur="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');" 
			      						onchange="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');" autocomplete="off">
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
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="end_dt" value="<%=end_dt %>" maxLength="10" 
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
							<label class="form-label"><b>Total Qty<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
				      					<input type="text" class="form-control form-control-sm" name="total_qty" value="<%=agmt_trans_qty %>" onblur="checkNumber1(this,15,3);">
				    					<span class="input-group-text"><b>MMBTU</b></span>
				    				</div>
				    				<input type="hidden" name="unit_cd" value="1">
				    			</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Calorific Base<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<input type="radio" name="calc_base" value="GCV" checked>&nbsp;GCV
				    			</div>
				    			<div class="col-auto">
									<input type="radio" name="calc_base" value="NCV">&nbsp;NCV
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
					    					<input type="checkbox" name="chk_bu_plant" value="<%=VBU_PLANT_SEQ_NO.elementAt(i)%>" onchange="checkCont_Status(this,'BU');">&nbsp;<%=VBU_PLANT_ABBR.elementAt(i)%>&nbsp;&nbsp;
					    				<%}%>
					    				<input type="hidden" name="chk_bu_plant_flg" value="<%=strContBuPlant%>">
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
					    					<input type="checkbox" name="chk_trans_bu" value="<%=VTRANS_BU_SEQ_NO.elementAt(i)%>" onchange="checkCont_Status(this,'TRANS_BU');">&nbsp;<%=VTRANS_BU_ABBR.elementAt(i)%>&nbsp;&nbsp;
					    				<%}%>
					    				<input type="hidden" name="chk_trans_bu_flg" value="<%=strContTransBu%>">
					    				<input type="hidden" name="tmp_chk_trans_bu" value="<%=strTransBu%>">
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
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="button" class="btn btn-sm config_btn" value="Entry Pt Config" 
					    			data-bs-toggle="modal" data-bs-target="#EntryPointModel" onclick="showEntryTblRow();"  <%if(counterparty_cd.equals("0")){ %>disabled<%} %>>
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label" id="EntryPointDisplay" style="display:none;"></label>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="button" class="btn btn-sm config_btn" value="Exit Pt Config" 
					    			data-bs-toggle="modal" data-bs-target="#ExitPointModel" <%if(counterparty_cd.equals("0")){ %>disabled<%} %>>
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label" id="ExitPointDisplay" style="display:none;"></label>
				      			</div>
				  			</div>
						</div>
					</div>
					<br>
					<div <%if(opration.equals("MODIFY") && (!agmt_no.equals("0") && !agmt_no.equals(""))){ %><%}else{ %>style="display:none;"<%} %>>
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
					&nbsp;
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						</div>
					</div>
					<%if(!agmt_no.equals("0") && !agmt_no.equals("")){ %>
						<%if(no_of_billing_dtl.equals("0") || no_of_billing_dtl.equals("")){ %>
							<div class="row m-b-5">
								<div align="center" class="col-sm-12 col-xs-12 col-md-12">
									<%=utilmsg.infoMessage("<b>Billing details configuration is pending for Agreement.</b>")%>
								</div>
							</div>
						<%} %>
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

<div class="modal fade" id="EntryPointModel" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Entry Point List
				</div>
        	</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="table-responsive">
							<table class="table table-bordered" id="example">
								<thead>
									<tr>
										<th>
											<input type="checkbox" class="form-check-input" name="chk_all" id="chk_all" onclick="checkAllEntryPoint(this);">
										</th>
										<th>Transporter</th>
										<th>Entity</th>
										<th>Plant/Entry Point</th>
									</tr>
								</thead>
								<tbody>
								<%if(VENTRY_COUNTERPTY_CD.size()>0){ %>
									<%for(int i=0; i<VENTRY_COUNTERPTY_CD.size(); i++){ %>
									<tr style="<%if(!VENTRY_STATUS.elementAt(i).equals("Y")) {%>color: red;<%} %> display:none;" id="row_entry_<%=VENTRY_MAPPING.elementAt(i)%>">
										<td align="center">
											<input type="checkbox" class="form-check-input" name="entry_point_mapping" value="<%=VENTRY_MAPPING.elementAt(i)%>" 
											<%if(!VENTRY_STATUS.elementAt(i).equals("Y")) {%>style="pointer-events: none;"<%} %> onchange="checkCont_Status(this,'ENTRY');">
										</td>
										<td><%=VENTRY_COUNTERPTY_ABBR.elementAt(i)%></td>
										<td>Transporter</td>
										<td>
											<%=VENTRY_PLANT_NM.elementAt(i)%>
											<input type="hidden" name="entry_point_nm" value="<%=VENTRY_PLANT_NM.elementAt(i)%>">
										</td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="4" align="center"><%=utilmsg.infoMessage("<b>Entry List is not Available!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
      				</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Back" data-bs-target="#GtaAgmtModal" data-bs-toggle="modal" data-bs-dismiss="modal">
					<input type="button" class="btn btn-warning com-btn" value="Submit" 
					data-bs-target="#GtaAgmtModal" data-bs-toggle="modal" data-bs-dismiss="modal" 
					onclick="doSubmitEntrySel();">
				</div>
      		</div>
      	</div>
	</div>
</div>

<div class="modal fade" id="ExitPointModel" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Exit Point List
				</div>
        	</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="table-responsive">
							<table class="table table-bordered" id="example">
								<thead>
									<tr>
										<th>
											<input type="checkbox" class="form-check-input" name="chkall" id="chkall" onclick="checkAllExitPoint(this);">
										</th>
										<th>Counterparty</th>
										<th>Entity</th>
										<th>Exit Point</th>
									</tr>
								</thead>
								<tbody>
								<%if(VEXIT_COUNTERPTY_CD.size()>0){ %>
									<%for(int i=0; i<VEXIT_COUNTERPTY_CD.size(); i++){ %>
									<tr <%if(!VEXIT_STATUS.elementAt(i).equals("Y")) {%>style="color: red;"<%} %>>
										<td align="center">
											<input type="checkbox" class="form-check-input" name="exit_point_mapping" value="<%=VEXIT_MAPPING.elementAt(i)%>" 
											<%if(!VEXIT_STATUS.elementAt(i).equals("Y")) {%>style="pointer-events: none;"<%} %> onchange="checkCont_Status(this,'EXIT');">
										</td>
										<td><%=VEXIT_COUNTERPTY_ABBR.elementAt(i)%></td>
										<td><%=VEXIT_ENTITY_NM.elementAt(i)%></td>
										<td>
											<%=VEXIT_PLANT_NM.elementAt(i)%>
											<input type="hidden" name="exit_point_nm" value="<%=VEXIT_PLANT_NM.elementAt(i)%>">
										</td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="4" align="center"><%=utilmsg.infoMessage("<b>Exit List is not Available!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Back" data-bs-target="#GtaAgmtModal" data-bs-toggle="modal" data-bs-dismiss="modal">
					<input type="button" class="btn btn-warning com-btn" value="Submit" 
					data-bs-target="#GtaAgmtModal" data-bs-toggle="modal" data-bs-dismiss="modal" 
					onclick="doSubmitExitSel();">
				</div>
      		</div>
      	</div>
	</div>
</div>


<input type="hidden" name="option" value="GTA_AGREEMENT_MST">
<input type="hidden" name="old_value" value="">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="status" value="">
<input type="hidden" name="no_of_billing_dtl" value="">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="chk_entry_point_flg" value="<%=strContEntryPoint%>">
<input type="hidden" name="chk_exit_pt_flg" value="<%=strContExitpt%>">

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
function setEntryPointDtl(entryPoints)
{
	var entryPointMap=entryPoints.split("@@");
	
	var entry_point_mapping = document.forms[0].entry_point_mapping;
	
	for(var j=0;j<entryPointMap.length;j++)
	{
		if(entry_point_mapping!=null && entry_point_mapping.length!=undefined)
		{
	 		for(var i=0;i<entry_point_mapping.length;i++)
	 		{
	  			if(entry_point_mapping[i].value == entryPointMap[j])
	  			{
	  				entry_point_mapping[i].checked=true;
	  			} 
	 		} 
		}
		else if(entry_point_mapping!=null)
		{
			if(entry_point_mapping.value == entryPointMap[j])
			{
				entry_point_mapping.checked=true;
			} 
		}
	}
	
	doSubmitEntrySel()
}

function setExitPointDtl(exitPoints)
{
	var exitPointMap=exitPoints.split("@@");
				
	var exit_point_mapping = document.forms[0].exit_point_mapping;
	
	for(var j=0;j<exitPointMap.length;j++)
	{
		if(exit_point_mapping!=null && exit_point_mapping.length!=undefined)
	 	{
	  		for(var i=0;i<exit_point_mapping.length;i++)
	  		{
	   			if(exit_point_mapping[i].value == exitPointMap[j])
	   			{
	   				exit_point_mapping[i].checked=true;
	   			} 
	  		} 
	 	}
	 	else if(exit_point_mapping!=null)
	 	{
	 		if(exit_point_mapping.value == exitPointMap[j])
   			{
	 			exit_point_mapping.checked=true;
   			} 
	 	}
	}
	doSubmitExitSel();
}

function showEntryTblRow()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var entry_point_mapping = document.forms[0].entry_point_mapping;
	
	if(entry_point_mapping!=null && entry_point_mapping.length!=undefined)
 	{
  		for(var i=0;i<entry_point_mapping.length;i++)
  		{
  			var mapping=entry_point_mapping[i].value;
  			
  			var split_val=mapping.split("-");
  			
  			var trans_cd=""
  			if(mapping!="")
  			{
  				trans_cd=split_val[0];
  			}
  			
  			if(trans_cd!="" && trans_cd==counterparty_cd)
  			{
  				document.getElementById("row_entry_"+mapping).style.display="table-row";	
  			}
  			else
  			{
  				document.getElementById("row_entry_"+mapping).style.display="none";
  				entry_point_mapping[i].checked=false;
  			}
  		} 
 	}
 	else if(entry_point_mapping!=null)
 	{
 		var mapping=entry_point_mapping.value;
		
		var split_val=mapping.split("-");
		
		var trans_cd=""
		if(mapping!="")
		{
			trans_cd=split_val[0];
		}
		
		if(trans_cd!="" && trans_cd==counterparty_cd)
		{
			document.getElementById("row_entry_"+mapping).style.display="table-row";	
		}
		else
		{
			document.getElementById("row_entry_"+mapping).style.display="none";
			entry_point_mapping.checked=false;
		}
 	}
}
</script>

</form>
</body>
</html>