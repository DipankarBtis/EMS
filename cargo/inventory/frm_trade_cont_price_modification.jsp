<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function goBack()
{
	var u = document.forms[0].u.value;
	
	var a = confirm("Are You sure You want to go Back?")
	if(a)
	{
		var url = "frm_energy_bank.jsp?u="+u;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}

function setEnableDisable(obj,index)
{
	var counterparty_cd = document.getElementById("counterparty_cd"+index)
	var agmt_no = document.getElementById("agmt_no"+index)
	var agmt_rev_no = document.getElementById("agmt_rev_no"+index)
	var cont_no = document.getElementById("cont_no"+index)
	var cont_rev_no = document.getElementById("cont_rev_no"+index)
	var contract_type = document.getElementById("contract_type"+index)
	var rate = document.getElementById("rate"+index)
	var rate_unit = document.getElementById("rate_unit"+index)
	var new_rate = document.getElementById("new_rate"+index)
	var new_eff_dt = document.getElementById("new_eff_dt"+index)
	var change_seq_no = document.getElementById("change_seq_no"+index)
	var price_change_flag = document.getElementById("price_change_flag"+index)
	
	var submit = document.getElementById("submit"+index)
	var approve = document.getElementById("approve"+index)
	var reject = document.getElementById("reject"+index)
	
	var approve_access = document.forms[0].approve_access.value;
	
	if(obj.checked)
	{
		counterparty_cd.disabled=false;
		agmt_no.disabled=false;
		agmt_rev_no.disabled=false;
		cont_no.disabled=false;
		cont_rev_no.disabled=false;
		contract_type.disabled=false;
		rate.disabled=false;
		rate_unit.disabled=false;
		new_rate.disabled=false;
		new_eff_dt.disabled=false;
		change_seq_no.disabled=false;
		
		new_rate.readOnly=false;
		new_eff_dt.readOnly=false;
		
		submit.disabled=false;
		(price_change_flag.value == "R")
		{
			if(approve_access == 'Y')
			{
				approve.disabled=false;
				reject.disabled=false;
			}
			else
			{
				approve.disabled=true;
				reject.disabled=true;
				
				alert("Approve/Reject Permission not available with current User!\n\n Please contact System Administrator!");
			}
		}
		
		var lastSelectedIndex = document.forms[0].lastSelectedIndex
		
		if(lastSelectedIndex.value != "" && lastSelectedIndex.value != index)
		{
			var counterparty_cd1 = document.getElementById("counterparty_cd"+lastSelectedIndex.value)
			var agmt_no1 = document.getElementById("agmt_no"+lastSelectedIndex.value)
			var agmt_rev_no1 = document.getElementById("agmt_rev_no"+lastSelectedIndex.value)
			var cont_no1 = document.getElementById("cont_no"+lastSelectedIndex.value)
			var cont_rev_no1 = document.getElementById("cont_rev_no"+lastSelectedIndex.value)
			var contract_type1 = document.getElementById("contract_type"+lastSelectedIndex.value)
			var rate1 = document.getElementById("rate"+lastSelectedIndex.value)
			var rate_unit1 = document.getElementById("rate_unit"+lastSelectedIndex.value)
			var new_rate1 = document.getElementById("new_rate"+lastSelectedIndex.value)
			var new_eff_dt1 = document.getElementById("new_eff_dt"+lastSelectedIndex.value)
			var change_seq_no1 = document.getElementById("change_seq_no"+lastSelectedIndex.value)
			
			var submit1 = document.getElementById("submit"+lastSelectedIndex.value)
			var approve1 = document.getElementById("approve"+lastSelectedIndex.value)
			var reject1 = document.getElementById("reject"+lastSelectedIndex.value)
			
			counterparty_cd1.disabled=true;
			agmt_no1.disabled=true;
			agmt_rev_no1.disabled=true;
			cont_no1.disabled=true;
			cont_rev_no1.disabled=true;
			contract_type1.disabled=true;
			rate1.disabled=true;
			rate_unit1.disabled=true;
			new_rate1.disabled=true;
			new_eff_dt1.disabled=true;
			change_seq_no1.disabled=true;
			
			new_rate1.readOnly=true;
			new_eff_dt1.readOnly=true;
			
			submit1.disabled=true;
			approve1.disabled=true;
			reject1.disabled=true;
		}
		
		lastSelectedIndex.value=index;
	}
}

function doSubmit(index,btn)
{
	var counterparty_cd = document.getElementById("counterparty_cd"+index)
	var agmt_no = document.getElementById("agmt_no"+index)
	var agmt_rev_no = document.getElementById("agmt_rev_no"+index)
	var cont_no = document.getElementById("cont_no"+index)
	var cont_rev_no = document.getElementById("cont_rev_no"+index)
	var contract_type = document.getElementById("contract_type"+index)
	var rate = document.getElementById("rate"+index)
	var rate_unit = document.getElementById("rate_unit"+index)
	var new_rate = document.getElementById("new_rate"+index)
	var new_eff_dt = document.getElementById("new_eff_dt"+index)
	
	var hid_new_rate = document.getElementById("hid_new_rate"+index)
	var hid_new_eff_dt = document.getElementById("hid_new_eff_dt"+index)
	
	//var last_eff_dt = document.getElementById("last_eff_dt"+index)
	
	var msg="";
	var flag=true;
	
	//var val_dt = compareDate(EffDt.value,last_eff_dt.value);
	
	if(new_rate.value == "0" || new_rate.value == "0.00" || new_rate.value == "0.000" || new_rate.value == "0.0000" || new_rate.value == "" || new_rate.value == " ")
	{
		msg+="Please Enter the Final Cargo Price!!";
		flag=false;
	}
	if(new_eff_dt.value == "" || new_eff_dt.value == " ")
	{
		msg+="\nPlease Select the New Effective Date!!";
		flag=false;
	}
	/* if(val_dt == "2" || val_dt == "0")
	{
		msg="\nThe Effective Date should be grater than Last Effective Date!!";
		flag=false
	} */	
	
	if(flag)
	{
		var con_msg="";
		if(btn=="approve")
		{
			con_msg="Please check following data :\n\n"
			con_msg+="Cargo Ref cd : "+cont_no.value+"\n"
			con_msg+="Cargo Prov Price : "+rate.value+"\n";
			con_msg+="Cargo Requested Price : "+hid_new_rate.value+"\n";
			con_msg+="Eff Date : "+hid_new_eff_dt.value+"\n\n";
			con_msg+="Do you want to Approve Cargo Price??"
		}
		else if(btn=="reject")
		{
			con_msg="Please check following data :\n\n"
			con_msg+="Cargo Ref cd : "+cont_no.value+"\n"
			con_msg+="Cargo Prov Price : "+rate.value+"\n";
			con_msg+="Cargo Requested Price : "+hid_new_rate.value+"\n";
			con_msg+="Eff Date : "+hid_new_eff_dt.value+"\n\n";
			con_msg+="Do you want to Reject Cargo Price??"
		}
		else
		{
			con_msg= "Do you want to change Cargo Price?";
		}
		var a = confirm(con_msg);
		if(a)
		{
			if(btn=="approve")
			{
				new_rate.value=hid_new_rate.value
				new_eff_dt.value=hid_new_eff_dt.value
				document.forms[0].option.value="TRADER_PRICE_REQUEST_APPROVE";
			}
			else if(btn=="reject")
			{
				new_rate.value=hid_new_rate.value
				new_eff_dt.value=hid_new_eff_dt.value
				document.forms[0].option.value="TRADER_PRICE_REQUEST_APPROVE";
			}
			document.forms[0].action.value=btn;
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_EnergyBank" id="energyBank" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
NumberFormat nf = new DecimalFormat("###########0.00");
NumberFormat nf2 = new DecimalFormat("###########0.0000");

String multiCountpty=request.getParameter("multiCountpty")==null?"":request.getParameter("multiCountpty");
String multiAgmtNo=request.getParameter("multiAgmtNo")==null?"":request.getParameter("multiAgmtNo");
String multiAgmtRev=request.getParameter("multiAgmtRev")==null?"":request.getParameter("multiAgmtRev");
String multiContNo=request.getParameter("multiContNo")==null?"":request.getParameter("multiContNo");
String multiContRev=request.getParameter("multiContRev")==null?"":request.getParameter("multiContRev");
String multiContTyp=request.getParameter("multiContTyp")==null?"":request.getParameter("multiContTyp");

energyBank.setCallFlag("TRADER_PRICE_CHANGE");
energyBank.setMultiCountpty(multiCountpty);
energyBank.setMultiAgmtNo(multiAgmtNo);
energyBank.setMultiAgmtRev(multiAgmtRev);
energyBank.setMultiContNo(multiContNo);
energyBank.setMultiContTyp(multiContTyp);
energyBank.setComp_cd(owner_cd);
energyBank.init();

Vector VCOUNTERPARTY_CD = energyBank.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = energyBank.getVCOUNTERPARTY_ABBR();
Vector VAGMT_NO = energyBank.getVAGMT_NO();
Vector VAGMT_REV_NO = energyBank.getVAGMT_REV_NO();
Vector VCONT_NO = energyBank.getVCONT_NO();
Vector VCONT_REV_NO = energyBank.getVCONT_REV_NO();
Vector VCONTRACT_TYPE = energyBank.getVCONTRACT_TYPE();
Vector VSTART_DT = energyBank.getVSTART_DT();
Vector VEND_DT = energyBank.getVEND_DT();
Vector VRATE = energyBank.getVRATE();
Vector VRATE_UNIT = energyBank.getVRATE_UNIT();
Vector VRATE_UNIT_NM = energyBank.getVRATE_UNIT_NM();
Vector VCONT_REF = energyBank.getVCONT_REF();

Vector VNEW_RATE = energyBank.getVNEW_RATE();
Vector VCHANGE_SEQ_NO = energyBank.getVCHANGE_SEQ_NO();
Vector VFLAG = energyBank.getVFLAG();
Vector VNEW_EFF_DATE = energyBank.getVNEW_EFF_DATE();

double exchgRate = energyBank.getExchgRate();

%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_EnergyBank">
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
					    	Trader Price Modification
					    </div>
					   	<span class="btn rounded-circle" style="background:var(--header_color);color:var(--header_font_color);" title="Back" onclick="goBack();">
						  &nbsp;<i class="fa fa-step-backward fa-2x"></i>&nbsp;
						</span>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Contract#</th>
										<th>Trader</th>
										<th>Contract Period</th>
										<th>Price Type</th>
										<th>Currency/MMBTU</th>
										<th>Cost Price</th>
										<th>New Cost Price</th>
										<th>Eff Date</th>
										<th>Request</th>
								   		<th colspan="2">Approval</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPARTY_CD.size() > 0){ %>
									<%for(int i=0;i<VCOUNTERPARTY_CD.size(); i++){ %>
									<tr>
										<td align="center">
											<input type="radio" name="chk" onclick="setEnableDisable(this,'<%=i%>')">
											<%=i+1%>
											<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>" disabled>
											<input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=i%>" value="<%=VAGMT_REV_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=i%>" value="<%=VCONT_REV_NO.elementAt(i)%>" disabled>
											<input type="hidden" name="contract_type" id="contract_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled>
											<input type="hidden" name="rate" id="rate<%=i%>" value="<%=VRATE.elementAt(i)%>" disabled>
											<input type="hidden" name="rate_unit" id="rate_unit<%=i%>" value="<%=VRATE_UNIT.elementAt(i)%>" disabled>
										</td>
										<td><%=VCONT_NO.elementAt(i)%><br>[<%=VCONT_REF.elementAt(i)%>]</td>
										<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										<td align="center">Fixed</td>
										<td align="center"><%=VRATE_UNIT_NM.elementAt(i)%></td>
										<td align="right"><%=VRATE.elementAt(i)%></td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="new_rate" id="new_rate<%=i%>" value="<%=VNEW_RATE.elementAt(i)%>" size=10 style="text-align:right" 
												onBlur="<%if(VRATE_UNIT.elementAt(i).toString().trim().equals("1")){ %>checkNumber1(this,8,4);<%}else{ %>checkNumber1(this,6,4);<%} %>negNumber(this);" readOnly disabled >
												<input type="hidden" name="hid_new_rate" id="hid_new_rate<%=i%>" value="<%=VNEW_RATE.elementAt(i)%>">
												<input type="hidden" name="change_seq_no" id="change_seq_no<%=i%>" value="<%=VCHANGE_SEQ_NO.elementAt(i)%>" disabled>
												<input type="hidden" name="price_change_flag" id="price_change_flag<%=i%>" value="<%=VFLAG.elementAt(i)%>" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="new_eff_dt" id="new_eff_dt<%=i%>" value="<%=VNEW_EFF_DATE.elementAt(i) %>" maxLength="10" 
						      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" readOnly disabled>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div>
					      						<input type="hidden" name="hid_new_eff_dt" id="hid_new_eff_dt<%=i%>" value="<%=VNEW_EFF_DATE.elementAt(i)%>">
											</div>
										</td>
										<td align="center">
							   				<input type="button" class="btn btn-sm request_btn" name="btn_submit" id="submit<%=i%>" value="Request" disabled onclick="doSubmit('<%=i%>','request');">
							   			</td>
							   			<td align="center">
							   				<input type="button" class="btn btn-sm config_btn" name="approve" id="approve<%=i%>" value="Approve" disabled onclick="doSubmit('<%=i%>','approve');">
							   			</td>
							   			<td align="center">	
							   				<input type="button" class="btn btn-danger btn-sm" style="border-radius: 50px;font-weight: bold;" name="reject" id="reject<%=i%>" value="Reject" disabled onclick="doSubmit('<%=i%>','reject');">
							   			</td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="12"><%=utilmsg.infoMessage("<b>No Trader Contracts have Created!</b>") %></td>
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

<input type="hidden" name="option" value="TRADER_PRICE_CHANGE">

<input type="hidden" name="multiCountpty" value="<%=multiCountpty%>">
<input type="hidden" name="multiAgmtNo" value="<%=multiAgmtNo%>">
<input type="hidden" name="multiAgmtRev" value="<%=multiAgmtRev%>">
<input type="hidden" name="multiContNo" value="<%=multiContNo%>">
<input type="hidden" name="multiContRev" value="<%=multiContRev%>">
<input type="hidden" name="multiContTyp" value="<%=multiContTyp%>">

<input type="hidden" name="action" value="">
<input type="hidden" name="lastSelectedIndex" value="">
<input type="hidden" name="exchgRate" value="<%=nf2.format(exchgRate)%>">

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