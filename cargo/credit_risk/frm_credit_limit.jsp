<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh(clearance)
{
	var prev_entity = document.forms[0].prev_entity.value;
	var prev_clearance = document.forms[0].prev_clearance.value;
	var entity = document.forms[0].entity.value;
	var entity_cd = document.forms[0].entity_cd.value;
	
	var limit_status = document.forms[1].limit_status.value;
	if(clearance=="I")
	{
		entity="C";
	}
	if(prev_clearance != clearance)
	{
		entity_cd="0";
	}
	if(prev_entity != entity)
	{
		entity_cd="0";
	}
	
	var u = document.forms[0].u.value;	
		var url = "frm_credit_limit.jsp?entity_cd="+entity_cd+"&clearance="+clearance+"&entity="+entity+"&limit_status="+limit_status+
				"&u="+u;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
}

function onRadio(val)
{
	var entity_cd = document.forms[0].entity_cd.value;
	var entity = document.forms[0].entity.value;
	var p_flag = document.forms[0].p_flag.value;
	
	if(val == 'C')
	{
		document.getElementById("cr").style.display="block";
		document.getElementById("po").style.display="none";
	}
	else if(val == 'P')
	{
		document.getElementById("cr").style.display="none";
		document.getElementById("po").style.display="block";
		
		if(entity=="C")
		{
			p_flag = "counterparty"
		}
		else if(entity=="B")
		{
			p_flag = "bank"
		}
	}
}

function doSubmit_cr()
{
	document.forms[0].pEntryDate.disabled=false;
	
	var entity_cd = document.forms[0].entity_cd.value;
	var entity = document.forms[0].entity.value;
	
	if(entity_cd!="0")
	{
		var rd = document.forms[0].rd.value;
		var rate_source = document.forms[0].rate_source.value;
		var credit_rating = document.forms[0].credit_rating.value;
		var cr_status = document.forms[0].cr_status.value;
		
		var parent_entity = document.forms[0].parent_entity.value;
		var ownership = document.forms[0].ownership.value;
		var pEntryDate = document.forms[0].pEntryDate.value;
		var p_cr_status = document.forms[0].p_cr_status.value;
		
		var opration = document.forms[0].opration.value;
		var msg="";
		var flag=true;
		
		if(rd=="P")
		{
			if(trim(parent_entity)=="" || parent_entity==0)
			{
				msg+="Please Select Parent Entity!\n";
				flag=false;
			}
			if(trim(ownership)=="" || ownership==0)
			{
				msg+="Please Enter Percent Ownership!\n";
				flag=false;
			}
			if(trim(pEntryDate)=="" || pEntryDate==0)
			{
				msg+="Please Select Parent Entry Date!\n";
				flag=false;
			}
			if(trim(p_cr_status)=="" || p_cr_status==0)
			{
				msg+="Please Select Status!\n";
				flag=false;
			}
		}
		else
		{
			if(trim(rate_source)=="" || rate_source==0)
			{
				msg+="Please Select Rate Source!\n";
				flag=false;
			}
			if(trim(credit_rating)=="" || credit_rating==0)
			{
				msg+="Please Select Credit Rating!\n";
				flag=false;
			}
			if(trim(cr_status)=="" || cr_status==0)
			{
				msg+="Please Select Status!\n";
				flag=false;
			}
		}
		if(flag)
		{
			var a = confirm("Do you want to "+opration+" the Credit Rating?");
			if(a)
			{
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].submit();
			}
			else
			{
				document.forms[0].pEntryDate.disabled=true;
			}
		}
		else
		{
			alert(msg);
		}
	}
	else
	{
		if(entity=="C")
		{
			alert("Please Select Any Counterparty!");
		}	
		else if(entity=="B")
		{
			alert("Please Select Any Bank!");
		}	
	}
}

function doSubmit_cl()
{
	var entity_cd = document.forms[0].entity_cd.value;
	var entity = document.forms[0].entity.value;
	
	if(entity_cd!="0")
	{
		var limit_id = document.forms[0].limit_id_rating.value;
		if(limit_id!="")
		{
			var limit_type = document.forms[1].limit_type.value;
			var action_type = document.forms[1].limit_action.value;
			var category = document.forms[1].limit_category.value;
			var amt = document.forms[1].limit_amount.value;
			var eff_dt = document.forms[1].limit_eff_date.value;
			
			var opration = document.forms[1].opration.value;
			var msg="";
			var flag=true;
			
			if(trim(limit_type)=="" || limit_type==0)
			{
				msg+="Please Select Limit Type!\n";
				flag=false;
			}
			if(trim(action_type)=="" || action_type==0)
			{
				msg+="Please Select Limit Action!\n";
				flag=false;
			}
			if(trim(category)=="" || category==0)
			{
				msg+="Please Select Category!\n";
				flag=false;
			}
			if(trim(amt)=="" || amt==0)
			{
				msg+="Please Enter Amount in INR!\n";
				flag=false;
			}
			if(trim(eff_dt)=="" || eff_dt==0)
			{
				msg+="Please Select Effective Date!\n";
				flag=false;
			}
			
			if(flag)
			{
				var a = confirm("Do you want to "+opration+" the Credit Limit?");
				if(a)
				{
					document.getElementById("loading").style.visibility = "visible";
					document.forms[1].submit();
				}
			}
			else
			{
				alert(msg);
			}
		}
		else
		{
			alert("Please Enter or Select Credit Rating/Parent Ownership!")
		}
	}
	else
	{
		if(entity=="C")
		{
			alert("Please Select Any Counterparty!");
		}	
		else if(entity=="B")
		{
			alert("Please Select Any Bank!");
		}	
	}
}

function update_rating(VCREDIT_RATING,VRATING_EFF_DT,VPARENT_OWENERSHIP_CD,VPARENT_OWENERSHIP,VREMARK,VSTATUS,VPARENT_ENT_DT,VPARENT_EXIT_DT,VREF_NO,RD,VLIMIT_ID)
{
	document.forms[0].pEntryDate.disabled = false;
	document.forms[0].cr_ref_no.value = VREF_NO;
	document.forms[0].limit_id_rating.value = VLIMIT_ID;
	var sysdate = document.forms[0].sysdate.value;
	var rate_source = document.forms[0].rate_source.value;
	var entity = document.forms[0].entity.value;
	var entity_cd = document.forms[0].entity_cd.value;
	var gx=document.forms[0].clearance.value;
	var ref_no = VREF_NO;
	
	var counterparty_cd="";
	var bank_cd="";
	
	if(entity=="C")
	{
		counterparty_cd = entity_cd;
		bank_cd = "0";
	}
	else if(entity=="B")
	{
		counterparty_cd = "0";
		bank_cd = entity_cd;
	}
		
	//if(RD=="P")
	{
		if(VPARENT_OWENERSHIP_CD!="")
		{
			document.forms[0].parent_entity.value = VPARENT_OWENERSHIP_CD;
		}
		else
		{
			document.forms[0].parent_entity.value = "0";
		}
		document.forms[0].ownership.value = VPARENT_OWENERSHIP;
		
		if(VPARENT_ENT_DT!="")
		{
			document.forms[0].pEntryDate.value = VPARENT_ENT_DT;
		}
		else
		{
			document.forms[0].pEntryDate.value = sysdate;
		}
		document.forms[0].pExitDate.value = VPARENT_EXIT_DT;
		document.forms[0].p_cr_status.value = VSTATUS;
		document.forms[0].p_cr_remark.value = VREMARK;
		
	}
	//else
	{
		if(VCREDIT_RATING!="")
		{
			document.forms[0].credit_rating.value = VCREDIT_RATING;
		}
		else
		{
			document.forms[0].credit_rating.value = "0";
		}
		
		document.forms[0].cr_status.value = VSTATUS;
		document.forms[0].cr_remark.value = VREMARK;
	}
	document.forms[0].pEntryDate.disabled = true;
	document.forms[0].opration.value="MODIFY";
	var old_value = "CP="+counterparty_cd+"#BANK_CD="+bank_cd+"#GX="+gx+"#REF_NO="+ref_no+"#RATE_SOURCE="+rate_source+"#CREDIT_RATING="+VCREDIT_RATING+"#CR_STATUS="+VSTATUS+"#CR_REMARK="+VREMARK+
					"#PARENT_ENTITY="+VPARENT_OWENERSHIP_CD+"#OWNERSHIP="+VPARENT_OWENERSHIP+"#PENTRYDATE="+VPARENT_ENT_DT+
					"#PEXITDATE="+VPARENT_EXIT_DT+"#PCR_STATUS="+VSTATUS+"#PCR_REMARK="+VREMARK+"#ENTITY="+entity;
	
	document.forms[0].CR_Rating_old_value.value=old_value;
}

function update_limit(VL_REF_NO,VL_LIMIT_TYPE,VL_ACTION_TYPE,VL_CATEGORY,VL_AMT,VL_EFF_DT,VL_EXP_DT,VL_REVIEW_DT,VL_REMARK,VL_LIMIT_ID,VL_SEQ_NO,ENTITY,ENTITY_CD)
{
	document.forms[1].entity1.value = ENTITY;
	document.forms[1].entity_cd1.value = ENTITY_CD;

	var entity1=document.forms[1].entity1.value;
	var entity_cd1=document.forms[1].entity_cd1.value;
	var gx=document.forms[1].clearance.value;
	
	document.forms[1].limit_type.value = VL_LIMIT_TYPE;
	document.forms[1].limit_action.value = VL_ACTION_TYPE;
	document.forms[1].limit_category.value = VL_CATEGORY;
	document.forms[1].limit_amount.value = VL_AMT;
	document.forms[1].limit_eff_date.value = VL_EFF_DT;
	document.forms[1].limit_exp_date.value = VL_EXP_DT;
	document.forms[1].limit_next_review_date.value = VL_REVIEW_DT;
	document.forms[1].limit_remark.value = VL_REMARK;
	document.forms[1].limit_id_dtl.value = VL_LIMIT_ID;
	document.forms[0].limit_id_rating.value = VL_LIMIT_ID;
	document.forms[1].cl_seq_no.value = VL_SEQ_NO;
	document.forms[1].cl_ref_no.value = VL_REF_NO;
	
	document.forms[1].opration.value="MODIFY";
	
	var counterparty_cd="";
	var bank_cd="";
	
	if(entity1=="C")
	{
		counterparty_cd = entity_cd1;
		bank_cd = "0";
	}
	else if(entity1=="B")
	{
		counterparty_cd = "0";
		bank_cd = entity_cd1;
	}
	
	var old_value = "CP="+counterparty_cd+"#BANK_CD="+bank_cd+"#GX="+gx+"#LIMIT_REF_NO="+VL_REF_NO+"#LIMIT_TYPE="+VL_LIMIT_TYPE+"#LIMIT_ACTION="+VL_ACTION_TYPE+
	"#LIMIT_CATE="+VL_CATEGORY+"#LIMIT_AMOUNT="+VL_AMT+"#EFF_DT="+VL_EFF_DT+
	"#EXP_DT="+VL_EXP_DT+"#REVIEW_DT="+VL_REVIEW_DT+"#LIMIT_STATUS="+"Y"+"#LIMIT_REMARKS="+VL_REMARK;

	document.forms[1].CR_Limit_old_value.value=old_value;
}

function change_status_inac(i,limit_id,entity,entity_cd,seq_no,ref_no,inact_dt)
{
	document.forms[1].entity1.value = entity;
	document.forms[1].entity_cd1.value = entity_cd;

	var entity1=document.forms[1].entity1.value;
	var entity_cd1=document.forms[1].entity_cd1.value;
	
	var gx=document.forms[1].clearance.value;	
	var inactive = document.getElementById('inactive_btn').value;
	var limit_type = document.forms[1].limit_type.value;
	var limit_action = document.forms[1].limit_action.value;
	var limit_category = document.forms[1].limit_category.value;
	var limit_amount = document.forms[1].limit_amount.value;
	var limit_eff_date=document.forms[1].limit_eff_date.value;
	var limit_exp_date=document.forms[1].limit_exp_date.value;
	var limit_next_review_date=document.forms[1].limit_next_review_date.value;
	var limit_remark=document.forms[1].limit_remark.value;
		
	document.forms[1].limit_id_dtl.value = limit_id;
	document.forms[1].opration.value="INACTIVE";
	document.forms[1].limit_status.value=inactive;
	document.forms[1].cl_ref_no.value=ref_no;
	document.forms[1].cl_seq_no.value = seq_no;
	
	var counterparty_cd="";
	var bank_cd="";
	
	if(entity1=="C")
	{
		counterparty_cd = entity_cd1;
		bank_cd = "0";
	}
	else if(entity1=="B")
	{
		counterparty_cd = "0";
		bank_cd = entity_cd1;
	}
	
	var old_value = "CP="+counterparty_cd+"#BANK_CD="+bank_cd+"#GX="+gx+"#LIMIT_REF_NO="+ref_no+"#LIMIT_TYPE="+limit_type+"#LIMIT_ACTION="+limit_action+
	"#LIMIT_CATE="+limit_category+"#LIMIT_AMOUNT="+limit_amount+"#EFF_DT="+limit_eff_date+
	"#EXP_DT="+limit_exp_date+"#REVIEW_DT="+limit_next_review_date+"#LIMIT_STATUS="+"Y"+"#LIMIT_REMARKS="+limit_remark;

	document.forms[1].CR_Limit_old_value.value=old_value;
	
	var a = confirm("Do you want to Inactive the Credit Limit?");
	if(a)
	{
		document.getElementById("loading").style.visibility = "visible";
		document.forms[1].submit();
	}
	else
	{
		document.getElementById('inactive_btn').value = "Y";
		
	}
}

function checkEffDateLimit()
{
	var currentDate=document.forms[1].sysdate.value;
	var eff_dt=document.forms[1].limit_eff_date.value;
	var exp_dt=document.forms[1].limit_exp_date.value;
	var val_dt1 = compareDate(currentDate,eff_dt);
	var val_dt2 = compareDate(eff_dt,exp_dt);
	
	if(parseInt(val_dt1) == 1)
	{
		alert('Effective Date must be greater or equal to sysdate !!');
		document.forms[1].limit_eff_date.value="";
		return false;
	}
	
}

function checkExpDateLimit()
{
	var currentDate=document.forms[1].sysdate.value;
	var eff_dt=document.forms[1].limit_eff_date.value;
	var exp_dt=document.forms[1].limit_exp_date.value;
	var val_dt2 = compareDate(eff_dt,exp_dt);
	
	if(parseInt(val_dt2) == 1)
	{
		alert('Expiration Date must be greater or equal to Effective Date !!');
		document.forms[1].limit_exp_date.value="";
		return false;
	}
	
}

function checkEffDateCredit()
{
	var entry_dt = document.forms[0].pEntryDate.value;
	var exit_dt = document.forms[0].pExitDate.value;
	
	var val_dt = compareDate(entry_dt,exit_dt);
	if(parseInt(val_dt) == 1)
	{
		alert('Exit Date must be greater or equal to Entry Date !!');
		document.forms[0].pExitDate.value="";
		return false;
	}
}

function isNumeric()
{
	var per_own = document.forms[0].ownership.value;
	var number = /^[0-9]+$/;
	
	if(per_own.match(number))
	{
		if(per_own >= 0 && per_own <= 100)
		{
		}
		else
		{
			alert("Enter Percent Ownership Between 0 and 100 !!");
			document.forms[0].ownership.value="";
			return false;
		}
	}
	else
	{
		alert("Enter Only Numeric Value !!");
		document.forms[0].ownership.value="";
		return false;
	}
}

function isNumeric_limit()
{
	var limit_amount = document.forms[1].limit_amount.value;
	var number = /^-?\d*\.?\d+$/;
	
	if(limit_amount.match(number))
	{
		checkNumber1(document.forms[1].limit_amount,'14','2')
	}
	else
	{
		alert("Enter Only Numeric Value !!");
		document.forms[1].limit_amount.value="";
		return false;
	}
}
var newWindow;
function showDetails(clearance,entity,entity_cd,limit_status,cust_nm,total)
{
	if(total.length > 0)
	{
		var url="rpt_limit_usage_dtl.jsp?clearance="+clearance+"&entity="+entity+"&entity_cd="+entity_cd+"&limit_status="+limit_status+"&cust_nm="+cust_nm+"&total="+total;
		if(!newWindow || newWindow.closed)
		{
			newWindow= window.open(url,"rpt_limit_usage_dtl","top=10,left=10,width=1200,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
		    newWindow= window.open(url,"rpt_limit_usage_dtl","top=10,left=10,width=1200,height=600,scrollbars=1");
		}
	}
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DataBean_CreditRisk" id="db_limit" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%

String sysdate=utildate.getSysdate();
String clearance = request.getParameter("clearance")==null?"K":request.getParameter("clearance");
String rd_val = request.getParameter("rd_val") == null?"C":request.getParameter("rd_val");
String p_flag = request.getParameter("p_flag") == null?"":request.getParameter("p_flag");
String entity = request.getParameter("entity") == null?"C":request.getParameter("entity");
String entity_cd = request.getParameter("entity_cd") == null?"0":request.getParameter("entity_cd");
String limit_status = request.getParameter("limit_status") == null?"0":request.getParameter("limit_status");

db_limit.setCallFlag("CREDIT_LIMIT_LIST");
db_limit.setClearance(clearance);
db_limit.setComp_cd(owner_cd);
db_limit.setEntity(entity);
db_limit.setEntity_cd(entity_cd);
db_limit.setLimit_status(limit_status);
db_limit.init();

Vector VMST_BANK_CD = db_limit.getVMST_BANK_CD();
Vector VMST_BANK_NM = db_limit.getVMST_BANK_NM();
Vector VMST_BANK_ABBR = db_limit.getVMST_BANK_ABBR();
Vector VMST_BRANCH_NAME = db_limit.getVMST_BRANCH_NAME();
Vector VCOUNTERPARTY_NAME = db_limit.getVCOUNTERPARTY_NAME();
Vector VMST_COUNTERPARTY_CD = db_limit.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = db_limit.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = db_limit.getVMST_COUNTERPARTY_ABBR();
Vector VPARENT_OWENERSHIP_NAME = db_limit.getVPARENT_OWENERSHIP_NAME(); 
Vector VPARENT_OWENERSHIP_ABBR = db_limit.getVPARENT_OWENERSHIP_ABBR();
Vector VPARENT_OWENERSHIP_CD = db_limit.getVPARENT_OWENERSHIP_CD();
Vector VCREDIT_RATING = db_limit.getVCREDIT_RATING();
Vector VRATING_EFF_DT = db_limit.getVRATING_EFF_DT();
Vector VPARENT_OWENERSHIP = db_limit.getVPARENT_OWENERSHIP();
Vector VREMARK = db_limit.getVREMARK();
Vector VSTATUS = db_limit.getVSTATUS();
Vector VLIMIT_ID = db_limit.getVLIMIT_ID();
Vector VPARENT_ENT_DT = db_limit.getVPARENT_ENT_DT();
Vector VPARENT_EXIT_DT = db_limit.getVPARENT_EXIT_DT();
Vector VENT_DT = db_limit.getVENT_DT();
Vector VREF_NO = db_limit.getVREF_NO();

Vector VL_REF_NO = db_limit.getVL_REF_NO();
Vector VL_LIMIT_TYPE = db_limit.getVL_LIMIT_TYPE();
Vector VL_ACTION_TYPE = db_limit.getVL_ACTION_TYPE();
Vector VL_CATEGORY = db_limit.getVL_CATEGORY();
Vector VL_AMT = db_limit.getVL_AMT();
Vector VL_ENT_DT = db_limit.getVL_ENT_DT();
Vector VL_EFF_DT = db_limit.getVL_EFF_DT();
Vector VL_EXP_DT = db_limit.getVL_EXP_DT();
Vector VL_INACT_DT = db_limit.getVL_INACT_DT();
Vector VL_REVIEW_DT = db_limit.getVL_REVIEW_DT();
Vector VL_REMARK = db_limit.getVL_REMARK();
Vector VL_STATUS = db_limit.getVL_STATUS();
Vector VL_SEQ_NO = db_limit.getVL_SEQ_NO();
Vector VL_LIMIT_ID = db_limit.getVL_LIMIT_ID();
Vector VAVAILABLE = db_limit.getVAVAILABLE();
Vector VTOTAL_LIMIT = db_limit.getVTOTAL_LIMIT();
Vector VUNSECURED = db_limit.getVUNSECURED();
Vector VTEMPORARY = db_limit.getVTEMPORARY();
Vector VADJUST_USAGE = db_limit.getVADJUST_USAGE();
Vector VUSAGE = db_limit.getVUSAGE();
Vector VNET_USAGE = db_limit.getVNET_USAGE();
Vector VUSED = db_limit.getVUSED();
Vector VYESNO = db_limit.getVYESNO();
Vector VL_DT_FLAG = db_limit.getVL_DT_FLAG();
Vector VINFO = db_limit.getVINFO();

Vector VPO_COUNTERPARTY_CD = db_limit.getVPO_COUNTERPARTY_CD();
Vector VPO_COUNTERPARTY_NM = db_limit.getVPO_COUNTERPARTY_NM();
Vector VPO_COUNTERPARTY_ABBR = db_limit.getVPO_COUNTERPARTY_ABBR();
Vector VPO_BANK_CD = db_limit.getVPO_BANK_CD();
Vector VPO_BANK_NM = db_limit.getVPO_BANK_NM();
Vector VPO_BANK_ABBR = db_limit.getVPO_BANK_ABBR();
Vector VPO_BRANCH_NAME = db_limit.getVPO_BRANCH_NAME();

%>

<body>
<%@ include file="../home/header.jsp"%>

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
				<form method="post" id="form1" action="../servlet/Frm_CreditRisk">
					<div class="card-header cdheader">
						 <div class="d-flex justify-content-between">
							<div class="topheader">
					    		Credit Limit Mgmt/Credit Rating 
		   	 				</div>
					    </div>
					</div>
					<div class="card-body cdbody">
						<div class="row" >
							<div class="col-sm-3 col-xs-3 col-md-12">
								<div align="center">
									<div class="btn-group" >
										<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("K")){%>btnactive<%}%>" onclick="refresh('K')">KYC</label>
										<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("I")){%>btnactive<%}%>" onclick="refresh('I')">IGX</label>
									</div>
								</div>
							</div>
						</div>					
						<div class="row">						
							<div class="col-sm-3 col-xs-3 col-md-12">
								<div class="d-flex justify-content-between"> 
									<div class="form-group row">
										<div class="col-auto">
											<select class="form-select form-select-sm" name="entity" onchange="refresh('<%=clearance%>')">
												<option value="C" selected="selected"><b>Counterparty</b></option>
												<%if(!clearance.equals("I")){ %>
													<option value="B"><b>Bank</b></option>
												<%} %>
											</select>
											<script>document.forms[0].entity.value="<%=entity%>"</script>
										</div>
										<div class="col-auto">
											<select class="form-select form-select-sm" name="entity_cd" onchange="refresh('<%=clearance%>')">
												<option value="0" lable="0" selected="selected">--Select--</option>
												<%if(entity.equals("C")){ %>
													<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
														<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
													<%} %> 
												<%}else{ %>
													<%for(int i=0; i<VMST_BANK_CD.size(); i++){ %>
							      						<option value="<%=VMST_BANK_CD.elementAt(i)%>"><%=VMST_BANK_NM.elementAt(i)%> (<%=VMST_BRANCH_NAME.elementAt(i)%>)</option>
							      					<%} %>
												<%} %>
											</select>
											<script>document.forms[0].entity_cd.value="<%=entity_cd%>"</script>
										</div> 
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="card-header cdheader">
						 <div class="d-flex justify-content-between">
							<div class="topheader">
					    		Credit Rating details 
					    		<%if(!entity_cd.equals("0")){%>for <%=VCOUNTERPARTY_NAME.elementAt(0) %><%} %>
		   	 				</div>
					    </div>
					</div>
					<div class="card-body cdbody">
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="table-responsive">
									<table class="table table-bordered">
										<thead>
											<tr>
												<th>Sr#</th>
												<th>Ref No#</th>
												<th>Entry Date</th>
												<th>Credit Rating</th>
												<th>Parent<br>Ownership Name</th>
												<th>Parent Ownership(%)</th>
												<th>Parent Entry Date</th>
												<th>Parent Exit Date</th>
												<th>Remark</th>
												<th>Status</th>
											</tr>
										</thead>
										<tbody>
										<%int k=0;
										if(VCREDIT_RATING.size()!=0){ 
											for(int i=0; i<VCREDIT_RATING.size(); i++){
											k+=1;
											%>
											<tr>
												<td align="right">
													<%if(VYESNO.elementAt(i).equals("N")){%><input type="radio" name="chk" onclick="update_rating('<%=VCREDIT_RATING.elementAt(i) %>','<%=VRATING_EFF_DT.elementAt(i)%>','<%=VPARENT_OWENERSHIP_CD.elementAt(i)%>','<%=VPARENT_OWENERSHIP.elementAt(i) %>',
																						'<%=VREMARK.elementAt(i) %>','<%=VSTATUS.elementAt(i) %>','<%=VPARENT_ENT_DT.elementAt(i) %>','<%=VPARENT_EXIT_DT.elementAt(i) %>','<%=VREF_NO.elementAt(i) %>',document.forms[0].rd.value,'<%=VLIMIT_ID.elementAt(i)%>')"><%} %>
												<%=k %></td>
												<td align="center"><%=VREF_NO.elementAt(i) %></td>
												<td align="center"><%=VENT_DT.elementAt(i) %></td>
												<td align="center"><%=VCREDIT_RATING.elementAt(i) %></td>
												<td align="center"><%=VPARENT_OWENERSHIP_NAME.elementAt(i) %></td>
												<td align=right><%=VPARENT_OWENERSHIP.elementAt(i) %></td>
												<td align="center"><%=VPARENT_ENT_DT.elementAt(i) %></td>
												<td align="center"><%=VPARENT_EXIT_DT.elementAt(i) %></td>
												<td align="center"><%=VREMARK.elementAt(i) %></td>
												<td>
												<%if(VSTATUS.elementAt(i).equals("Y")){ %>
													Authorized
												<%}else{ %>
													Unauthorized
												<%} %>
												</td>
											</tr>
											<%} %>
										<%}else{ %>
											<tr><td colspan="10" align="center"><%=utilmsg.infoMessage("<b>Rating List Is Not Available!</b>") %></td></tr>
										<%} %>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					<div class="card-body cdbody">
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;<b>Add/Modify Credit Rating/Parent Ownership</b> </label>
						</div>
						<div align="center">
							<input type="radio" name="rd" value="C" <%if(rd_val.equals("C")){ %> checked="checked" <%} %> onclick="onRadio('C')" >&nbsp;<b>Credit Rating</b>&nbsp; &nbsp; &nbsp;
							<%if(!clearance.equals("I") || entity.equals("B")){ %>
			    				<input type="radio" name="rd" value="P" <%if(rd_val.equals("P")){ %> checked="checked" <%} %> onclick="onRadio('P')">&nbsp;<b>Parent Ownership</b> &nbsp; &nbsp;
			    			<%} %>
						</div>&nbsp;
						<div id="cr">
							<div class="row m-b-5">
		      					<div class="col-sm-1 col-xs-1 col-md-1">  
									<div class="form-group row">
						    			<label class="form-label"><b>Rating Source :<span class="s-red">*</span></b></label>
						  			</div>
								</div>
			   					<div class="col-sm-3 col-xs-3 col-md-3">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						    				<select class="form-select form-select-sm" name="rate_source">
						      					<option value="Shell">Shell</option>
								      		</select>
								      	</div>
								    </div>
								</div>
								<div class="col-sm-2 col-xs-2 col-md-2">
								</div>
			      				<div class="col-sm-1 col-xs-1 col-md-1">  
									<div class="form-group row">
						    			<label class="form-label"><b>Credit Rating :<span class="s-red">*</span></b></label>
						  			</div>
								</div>
			   					<div class="col-sm-3 col-xs-3 col-md-3">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
											<select class="form-select form-select-sm" name="credit_rating">
												<option value="0" selected="selected">---Select---</option>
						      					<option value="A">A</option>
												<option value="B">B</option>
												<option value="C">C</option>
												<option value="D">D</option>
												<option value="E">E</option>
								      		</select>
								      	</div>
								    </div>
								</div>
		      				</div>
		      				<div class="row m-b-5">
		      					<div class="col-sm-1 col-xs-1 col-md-1">  
									<div class="form-group row">
						    			<label class="form-label"><b>Status :<span class="s-red">*</span></b></label>
						  			</div>
								</div>
			   					<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						    				<select class="form-select form-select-sm" name="cr_status">
						      					<option value="Y">Authorized</option>
												<option value="N">Unauthorized</option>
								      		</select>
								      	</div>
								    </div>
								</div>
							</div>
							<div class="row m-b-5">
		      					<div class="col-sm-1 col-xs-1 col-md-1">  
									<div class="form-group row">
						    			<label class="form-label"><b>Remark :</b></label>
						  			</div>
								</div>
			   					<div class="col-sm-12 col-xs-12 col-md-11">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-10">
						    				<input type="text" class="form-control form-control-sm" name="cr_remark">
								      	</div>
								    </div>
								</div>
							</div>
						</div>&nbsp;
						<div id="po" style="display: none;">
							<div class="row m-b-5">
		      					<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>Parent Leagel Entity :<span class="s-red">*</span></b></label>
						  			</div>
								</div>
			   					<div class="col-sm-3 col-xs-3 col-md-3">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						    				<select class="form-select form-select-sm" name="parent_entity">
						      				<%if(entity.equalsIgnoreCase("B")){ %>
						      					<option value="0" selected="selected">---Select---</option>
						      					<%for(int i=0; i<VPO_BANK_CD.size(); i++){ %>
						      						<option value="<%=VPO_BANK_CD.elementAt(i)%>"><%=VPO_BANK_NM.elementAt(i)%> (<%=VPO_BRANCH_NAME.elementAt(i)%>)</option>
						      					<%} %>
						      				<%}else{ %>
						      					<option value="0" selected="selected">---Select---</option>
						      					<%for(int i=0;i<VPO_COUNTERPARTY_CD.size();i++){ %>
													<option value="<%=VPO_COUNTERPARTY_CD.elementAt(i)%>"><%=VPO_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VPO_COUNTERPARTY_NM.elementAt(i) %></option>
												<%} %> 
						      				<%} %>
								      		</select>
								      	</div>
								    </div>
								</div>
								<div class="col-sm-1 col-xs-1 col-md-1">
								</div>
			      				<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>Percent Ownership(%) :<span class="s-red">*</span></b></label>
						  			</div>
								</div>
			   					<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
											<input type="text" class="form-control form-control-sm" name="ownership" onchange="isNumeric()">
								      	</div>
								    </div>
								</div>
		      				</div>
		      				<div class="row m-b-5">
		      					<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>Parent Entry Date :<span class="s-red">*</span></b></label>
						  			</div>
								</div>
			   					<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						      				<div class="input-group input-group-sm" >
					      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="pEntryDate" value="<%=sysdate %>" size="10" maxLength="10" 
					      						onblur="validateDate(this);" onchange="validateDate(this);checkEffDateCredit();" autocomplete="off" disabled>
					      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      						</div>
						    			</div>
						  			</div>
					  			</div>
					  			<div class="col-sm-2 col-xs-2 col-md-2">
								</div>
			      				<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>Parent Exit Date:</b></label>
						  			</div>
								</div>
			   					<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						      				<div class="input-group input-group-sm" >
					      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="pExitDate" value="" size="10" maxLength="10" 
					      						onblur="validateDate(this);" onchange="validateDate(this);checkEffDateCredit();" autocomplete="off">
					      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      						</div>
						    			</div>
						  			</div>
					  			</div>
		      				</div>
		      				<div class="row m-b-5">
		      					<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>Status :<span class="s-red">*</span></b></label>
						  			</div>
								</div>
			   					<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-12">
						    				<select class="form-select form-select-sm" name="p_cr_status">
						      					<option value="Y">Authorized</option>
												<option value="N">Unauthorized</option>
								      		</select>
								      	</div>
								    </div>
								</div>
							</div>
							<div class="row m-b-5">
		      					<div class="col-sm-2 col-xs-2 col-md-2">  
									<div class="form-group row">
						    			<label class="form-label"><b>Remark :</b></label>
						  			</div>
								</div>
			   					<div class="col-sm-12 col-xs-12 col-md-10">  
									<div class="form-group row">
						    			<div class="col-sm-12 col-xs-12 col-md-10">
						    				<input type="text" class="form-control form-control-sm" name="p_cr_remark">
								      	</div>
								    </div>
								</div>
							</div>
						</div>&nbsp;
						<div class="d-flex justify-content-between">
							<input type="button" class="btn btn-warning com-btn" id="cr_reset" value=" Reset " >&nbsp;&nbsp;
							<input type="button" class="btn btn-warning com-btn" id="cr_submit" value="Submit" onclick="doSubmit_cr();">
						</div>
					</div>
					<input type="hidden" name="option" value="CREDIT_RATING_DTLS">
					<input type="hidden" name="opration" value="INSERT">
					<input type="hidden" name="prev_clearance" value="<%=clearance%>">
					<input type="hidden" name="clearance" value="<%=clearance%>">
					<input type="hidden" name="prev_entity" value="<%=entity%>">
					<input type="hidden" name="p_flag" value="">
					<input type="hidden" name="cr_ref_no" value="">
					<input type="hidden" name="sysdate" value="<%=sysdate%>">
					<input type="hidden" name="limit_id_rating" id="limit_id_rating" >
					
					<input type="hidden" name="CR_Rating_old_value" value="">
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
				<form method="post" id="form2" action="../servlet/Frm_CreditRisk">
					<div class="card-header cdheader">
						 <div class="d-flex justify-content-between">
							<div class="topheader">
					    		Credit Limit Management
		   	 				</div>
					    </div>
					</div>
					<div class="card-body cdbody">
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="table-responsive">
									<table class="table table-bordered">
										<thead>
											<tr>
												<th>Available Limit<br>(INR)</th>
												<th>Net Usage<br>(INR)</th>
												<th>Usage (INR)</th>
												<th>Total Limit<br>(INR)</th>
												<th>Unsecured Limit<br>(INR)</th>
												<th>Temporary Limit<br>(INR)</th>
												<th>Usage Adjustment<br>(INR)</th>
												<th>% Used</th>
											</tr>
										</thead>
										<tbody>
										<%int b=0;
										if(VL_REF_NO.size()!=0){ 
											for(int i=0; i<VAVAILABLE.size(); i++){
											b+=1;
											
											String split_used = ""+VUSED.elementAt(i);
											String[] splited_Strings = split_used.replace(".", "@").split("@");
											String split_used1 = splited_Strings[0];
											String split_used2 = splited_Strings[1].substring(0,2);
											String used = ""+split_used1+"."+split_used2;
											%>
											<tr>
												<td align="right" style="background:#99ffcc"><%=VAVAILABLE.elementAt(i) %></td>
												<td align="right"><%=VNET_USAGE.elementAt(i) %></td>
												<td align="right" title="Click to View Detail" onclick="showDetails('<%=clearance%>','<%=entity%>','<%=entity_cd%>','<%=limit_status%>','<%if(VCOUNTERPARTY_NAME.size()!=0){%><%=VCOUNTERPARTY_NAME.elementAt(0)%><%}%>','<%=VUSAGE.elementAt(i)%>');"><%=VUSAGE.elementAt(i) %></td>
												<td align="right"><%=VTOTAL_LIMIT.elementAt(i) %></td>
												<td align="right"><%=VUNSECURED.elementAt(i) %></td>
												<td align="right"><%=VTEMPORARY.elementAt(i) %></td>
												<td align="right"><%=VADJUST_USAGE.elementAt(i) %></td>
												<td align="right"><%=used %> %</td>
											</tr>
											<%} %>
										<%}else{ %>
											<tr><td colspan="9" align="center"><%=utilmsg.infoMessage("<b>Data Not Available!</b>") %></td></tr>
										<%} %>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					<div class="card-body cdbody">
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;<b>Credit Limit Details <%if(VCOUNTERPARTY_NAME.size()!=0){%>for <%=VCOUNTERPARTY_NAME.elementAt(0) %><%} %></b> </label>
						</div>
						<div align="center">
							<div class="col-sm-4 col-xs-4 col-md-2">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>Status :</b></label>
									</div>
									<div class="col-auto">
					      				<div class="input-group input-group-sm" >
				      						<select class="form-select form-select-sm" name="limit_status" onchange="refresh('<%=clearance%>')">
												<option value="0" selected="selected">--All--</option>
												<option value="Y">Active</option>
												<option value="N">Inactive</option>
											</select>
											<script>document.forms[1].limit_status.value='<%=limit_status %>'</script>
			      						</div>
					    			</div>
								</div>
							</div>
						</div>&nbsp;
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="table-responsive">
									<table class="table table-bordered">
										<thead>
											<tr>
												<th>Sr#</th>
												<th>Ref. No#</th>
												<th>Limit Type</th>
												<th>Limit Action</th>
												<th>Categorization</th>
												<th>Amount (INR)</th>
												<th>Entry Date</th>
												<th>Effective Date</th>
												<th>Expiration Date</th>
												<th>Inactivation date</th>
												<th>Review Date</th>
												<th>Remarks</th>
												<th>Status</th>
												<!-- <th></th> -->
											</tr>
										</thead>
										<tbody>
										<%int a=0;
										if(VL_REF_NO.size()!=0){ 
											for(int i=0; i<VL_REF_NO.size(); i++){
											a+=1;
											%>
											<tr <%if(VL_STATUS.elementAt(i).equals("N")){ %> style="background:#e6e6e6;"<%} %>>
												<td align="center">
													<%if(VL_DT_FLAG.elementAt(i).equals("N")){
														if(!VL_STATUS.elementAt(i).equals("N")){%><input type="radio" name="chk" onclick="update_limit('<%=VL_REF_NO.elementAt(i)%>','<%=VL_LIMIT_TYPE.elementAt(i)%>','<%=VL_ACTION_TYPE.elementAt(i)%>','<%=VL_CATEGORY.elementAt(i)%>','<%=VL_AMT.elementAt(i)%>','<%=VL_EFF_DT.elementAt(i) %>','<%=VL_EXP_DT.elementAt(i) %>',
																																						'<%=VL_REVIEW_DT.elementAt(i) %>','<%=VL_REMARK.elementAt(i) %>','<%=VL_LIMIT_ID.elementAt(i) %>','<%=VL_SEQ_NO.elementAt(i)%>','<%=entity %>','<%=entity_cd%>')">
													<%} 
													}%>
												<%=a %></td>
												<td align="center"><%=VL_REF_NO.elementAt(i) %></td>
												<td align="center"><%=VL_LIMIT_TYPE.elementAt(i) %></td>
												<td align="center"><%=VL_ACTION_TYPE.elementAt(i) %></td>
												<td align="center"><%=VL_CATEGORY.elementAt(i) %></td>
												<td align="right"><%=VL_AMT.elementAt(i) %></td>
												<td align="center"><%=VL_ENT_DT.elementAt(i) %></td>
												<td align="center"><%=VL_EFF_DT.elementAt(i) %></td>
												<td align="center"><%=VL_EXP_DT.elementAt(i) %></td>
												<td align="center"><%=VL_INACT_DT.elementAt(i) %></td>
												<td align="center"><%=VL_REVIEW_DT.elementAt(i) %></td>
												<td align="center"><%=VL_REMARK.elementAt(i) %></td>
												<td align="center">
												<%if(VL_STATUS.elementAt(i).equals("Y")){ %>
												<div class="form-check form-switch">
													<input class="form-check-input" name="inactive_btn" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked onclick="change_status_inac('<%=a %>','<%=VL_LIMIT_ID.elementAt(i) %>','<%=entity %>','<%=entity_cd%>','<%= VL_SEQ_NO.elementAt(i)%>','<%=VL_REF_NO.elementAt(i)%>','<%=VL_INACT_DT.elementAt(i)%>')">
													  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb">
													  		Active
													  	</label>
												  	<input type="hidden" name="inactive_btn" id="inactive_btn" value="Y">
												</div>
													
												<%}else{ %>
													Inactive
												<%} %>
												</td>
											</tr>
											<%} %>
										<%}else{ %>
											<tr><td colspan="13" align="center"><%=utilmsg.infoMessage("<b>Limit List Is Not Available!</b>") %></td></tr>
										<%} %>
										</tbody>
									</table>
								</div>
							</div>
						</div>&nbsp;
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;<b>Add/Modify Credit Limit</b> </label>
						</div>&nbsp;
						<div class="row m-b-5">
	      					<div class="col-sm-1 col-xs-1 col-md-1">  
								<div class="form-group row">
					    			<label class="form-label"><b>Limit Type :<span class="s-red">*</span></b></label>
					  			</div>
							</div>
		   					<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<select class="form-select form-select-sm" name="limit_type">
					    					<option value="0" selected="selected">---Select---</option>
					      					<option value="Unsecured">Unsecured</option>
											<option value="Temporary">Temporary</option>
											<option value="Other">Other</option>
											<option value="Dummy">Dummy</option>
							      		</select>
							      	</div>
							    </div>
							</div>
							<div class="col-sm-1 col-xs-1 col-md-1">
							</div>
							<div class="col-sm-1 col-xs-1 col-md-1">  
								<div class="form-group row">
					    			<label class="form-label"><b>Limit Action :<span class="s-red">*</span></b></label>
					  			</div>
							</div>
		   					<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<select class="form-select form-select-sm" name="limit_action">
					    					<option value="0" selected="selected">---Select---</option>
					      					<option value="Adjust Limit">Adjust Limit</option>
											<option value="Adjust Usage">Adjust Usage</option>
							      		</select>
							      	</div>
							    </div>
							</div>
							<div class="col-sm-1 col-xs-1 col-md-1">
							</div>
							<div class="col-sm-1 col-xs-1 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Categorization :<span class="s-red">*</span></b></label>
					  			</div>
							</div>
		   					<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<select class="form-select form-select-sm" name="limit_category">
					    					<option value="0" selected="selected">---Select---</option>
					      					<option value="Physical">Physical</option>
											<option value="Contingency">Contingency</option>
											<option value="Financial">Financial</option>
							      		</select>
							      	</div>
							    </div>
							</div>
						</div>
						<div class="row m-b-5">
	      					<div class="col-sm-1 col-xs-1 col-md-1">  
								<div class="form-group row">
					    			<label class="form-label"><b>Amount (INR) :<span class="s-red">*</span></b></label>
					  			</div>
							</div>
		   					<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<div class="col-sm-12 col-xs-12 col-md-12">
						    				<input type="text" class="form-control form-control-sm" name="limit_amount" onchange="isNumeric_limit();">
						    			</div>
							      	</div>
							    </div>
							</div>
						</div>
						<div class="row m-b-5">
	      					<div class="col-sm-1 col-xs-1 col-md-1">  
								<div class="form-group row">
					    			<label class="form-label"><b>Effective Date:<span class="s-red">*</span></b></label>
					  			</div>
							</div>
		   					<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="limit_eff_date" value="<%=sysdate %>" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);checkEffDateLimit();" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-1 col-xs-1 col-md-1">
							</div>
							<div class="col-sm-1 col-xs-1 col-md-1">  
								<div class="form-group row">
					    			<label class="form-label"><b>Expiration Date:</b></label>
					  			</div>
							</div>
		   					<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="limit_exp_date" value="" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);checkExpDateLimit();" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-1 col-xs-1 col-md-1">
							</div>
							<div class="col-sm-1 col-xs-1 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Next Review Date:</b></label>
					  			</div>
							</div>
		   					<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="limit_next_review_date" value="" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
	      					<div class="col-sm-1 col-xs-1 col-md-1">  
								<div class="form-group row">
					    			<label class="form-label"><b>Remark :</b></label>
					  			</div>
							</div>
		   					<div class="col-sm-12 col-xs-12 col-md-11">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<input type="text" class="form-control form-control-sm" name="limit_remark">
							      	</div>
							    </div>
							</div>
						</div>&nbsp;
						<div align="right">
							<input type="button" class="btn btn-warning com-btn" id="limit_submit" value="Submit" onclick="doSubmit_cl();" >
						</div>
					</div>
					<input type="hidden" name="option" value="CREDIT_LIMIT_DTLS">
					<input type="hidden" name="opration" value="INSERT">
					<input type="hidden" name="p_flag" value="">
					<input type="hidden" name="limit_id_dtl" value="<%if(VLIMIT_ID.size()!=0){%><%=VLIMIT_ID.elementAt(0)%><%}%>">
					<input type="hidden" name="entity1" value="<%=entity%>">
					<input type="hidden" name="entity_cd1" value="<%=entity_cd%>">
					<input type="hidden" name="cl_seq_no" value="" >
					<input type="hidden" name="cl_ref_no" value="" >
					<input type="hidden" name="sysdate" value="<%=sysdate %>" >
					<input type="hidden" name="CR_Limit_old_value" value="">
					
					<input type="hidden" name="prev_clearance" value="<%=clearance%>">
					<input type="hidden" name="clearance" value="<%=clearance%>">
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
			</div>
		</div>
	</div>
</div>
</html>
