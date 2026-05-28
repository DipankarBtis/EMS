<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh()
{
	var customer_cd = document.forms[0].customer_cd.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_dlng_cont_duration_modification.jsp?customer_cd="+customer_cd+"&u="+u;;
	location.replace(url);
}


function doSubmit(index,btn)
{
	var new_start_dt = document.getElementById("new_start_dt"+index);
	var new_end_dt = document.getElementById("new_end_dt"+index);
	var temp_new_start_dt = document.getElementById("temp_new_start_dt"+index);
	var submit = document.getElementById("submit"+index);
	var approve = document.getElementById("approve"+index);
	var reject = document.getElementById("reject"+index);
	var max_dt = document.getElementById("max_dt"+index).value;
	var min_nom_dt = document.getElementById("min_nom_dt"+index).value;
	var signing_dt = document.getElementById("signing_dt"+index).value;
	
	var msg="";
	var flag=true;
	
	if(new_start_dt.value == "" || new_start_dt.value == " ")
	{
		msg+="Please Select the New Start Date!!\n";
		flag=false;
	}
	if(new_end_dt.value == "" || new_end_dt.value == " ")
	{
		msg+="Please Select the New End Date!!\n";
		flag=false;
	}
	
	if(trim(signing_dt) !="" && new_start_dt.value!="")
	{
		var value_0 = compareDate(signing_dt,new_start_dt.value);
		if(value_0 == "1")
		{
			msg+="New Start Date "+new_start_dt.value+" < Contract Date "+signing_dt+" not Allowed!\n";
			flag=false
		}
	}
	if(trim(min_nom_dt) !="" && new_start_dt.value!="")
	{
		var value_0 = compareDate(new_start_dt.value,min_nom_dt);
		if(value_0 == "1")
		{
			msg+="New Start Date "+new_start_dt.value+" > First Nomination Date "+min_nom_dt+" not Allowed!\n";
			flag=false
		}
	}
	if(trim(max_dt) !="" && new_end_dt.value!="")
	{
		var value_1 = compareDate(max_dt,new_end_dt.value);
		if(value_1 == "1") 
		{
			msg+="Contract End Date "+new_end_dt.value+" < Last Allocation/Invoice Date "+max_dt+" not Allowed!\n";
			flag=false
		}
	}
	
	if(btn=="approve" || btn=="reject")
	{
		if(temp_new_start_dt.value=="")
		{
			msg+="Please re-submit the Request and then proceed for approval!\n";
			flag=false;
		}
	}
	var oldvalue="";
	if(flag)
	{
		var con_msg="";
		if(btn=="approve")
		{
			con_msg+="Do you want to Approve??"
		}
		else if(btn=="reject")
		{
			con_msg+="Do you want to Reject??"
		}
		else
		{
			con_msg= "Do you want to change Contract Duration??";
		}
		var a = confirm(con_msg);
		if(a)
		{
			if(btn=="approve")
			{
				document.forms[0].option.value="DLNG_CONTRACT_DURATION_MODIFICATION_APPROVE";
			}
			else if(btn=="reject")
			{
				document.forms[0].option.value="DLNG_CONTRACT_DURATION_MODIFICATION_APPROVE";
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
<jsp:useBean class="com.etrm.fms.contract_master.DataBean_ContractMaster" id="contract" scope="page"/>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String contract_type = request.getParameter("contract_type")==null?"0":request.getParameter("contract_type");
String customer_cd = request.getParameter("customer_cd")==null?"0":request.getParameter("customer_cd");
String formId = request.getParameter("formId")==null?"0":request.getParameter("formId");
String formName = request.getParameter("FormName")==null?"0":request.getParameter("FormName");

contract.setCallFlag("DLNG_CONTRACT_DURATION_MODIFICATION");
contract.setCounterparty_cd(customer_cd);
contract.setComp_cd(owner_cd);
contract.init();

Vector VMST_COUNTERPARTY_CD = contract.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = contract.getVMST_COUNTERPARTY_NM();

Vector VDEAL_MAPPING = contract.getVDEAL_MAPPING();
Vector VCONT_REF_NO = contract.getVCONT_REF_NO();
Vector VAGMT_NO = contract.getVAGMT_NO();
Vector VAGMT_REV_NO = contract.getVAGMT_REV_NO();
Vector VCONT_NO = contract.getVCONT_NO();
Vector VCONT_REV_NO = contract.getVCONT_REV_NO();
Vector VSTART_DT = contract.getVSTART_DT();
Vector VEND_DT = contract.getVEND_DT();
Vector VCONTRACT_TYPE = contract.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = contract.getVCONTRACT_TYPE_NM();
Vector VSEGMENT = contract.getVSEGMENT();
Vector VNEW_START_DT = contract.getVNEW_START_DT();
Vector VNEW_END_DT = contract.getVNEW_END_DT();
Vector VMAX_DT = contract.getVMAX_DT();
Vector VMIN_NOM_DT = contract.getVMIN_NOM_DT();
Vector VSIGNING_DT = contract.getVSIGNING_DT();

NumberFormat nf = new DecimalFormat("###########0.00");
NumberFormat nf2 = new DecimalFormat("###########0.0000");

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_ContracMaster">


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
					    	DLNG Contract Duration Modification
					    </div>
	    			</div>
	    		</div>
	    		<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer<span class="s-red">*</span></b></label>
								</div>
				    			<div class="col">
				    				<select class="form-select form-select-sm" name="customer_cd" id="customer_cd" onchange="refresh();"> 
					   					<option value="0">--Select--</option>
					   					<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++) { %>
					   						<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_NM.elementAt(i)%></option>
									  	<%}%>
					   				</select>
					   				<script type="text/javascript">
					   					document.forms[0].customer_cd.value = "<%=customer_cd%>";
					   				</script>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4"></div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered">
									<thead>
										<tr>
								   			<th>SR.NO.</th>
								   			<th>Contract No</th>
								   			<th>Contract/Trade Ref#</th>
								   			<th>Start - End Date</th>
								   			<th>New Start Date</th>
								   			<th>New End Date</th>
								   			<th>Request</th>
								   			<th colspan="2">Approval</th>
								   		</tr>
									</thead>
									<tbody>
									<% int j=0;String rowcolor="";
									if(VDEAL_MAPPING.size()>0){
									
							   		for(int i=0; i<VDEAL_MAPPING.size(); i++)
							   		{ 
							   			if(i%2==0) {
											rowcolor="#E0EEE0";
										}else{ 
											rowcolor="";
										}
												%>
										   		<tr id="tr<%=i%>">
										   			<td align="center">
										   				<input type="radio" name="radio" id="radio<%=i %>" onclick="setEnableDisable(this,'<%=i%>');">
										   				<%=i+1%>
										   			</td>
										   			<td align="center">
										   				<%=VDEAL_MAPPING.elementAt(i) %>
										   				<input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>" disabled>
										   				<input type="hidden" name="agmt_rev" id="agmt_rev<%=i%>" value="<%=VAGMT_REV_NO.elementAt(i)%>" disabled>
										   				<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>" disabled>
										   				<input type="hidden" name="cont_rev" id="cont_rev<%=i%>" value="<%=VCONT_REV_NO.elementAt(i)%>" disabled>
										   				<input type="hidden" name="cont_type" id="cont_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled>
										   				<input type="hidden" name="segment" id="segment<%=i%>" value="<%=VSEGMENT.elementAt(i)%>" disabled>
										   				<input type="hidden" name="max_dt" id="max_dt<%=i%>" value="<%=VMAX_DT.elementAt(i)%>" disabled>
										   				<input type="hidden" name="min_nom_dt" id="min_nom_dt<%=i%>" value="<%=VMIN_NOM_DT.elementAt(i)%>" disabled>
										   				<input type="hidden" name="signing_dt" id="signing_dt<%=i%>" value="<%=VSIGNING_DT.elementAt(i)%>" disabled>
										   				<input type="hidden" name="start_dt" id="start_dt<%=i%>" value="<%=VSTART_DT.elementAt(i)%>" disabled>
										   			</td>
										   			<td align="center"><%=VCONT_REF_NO.elementAt(i)%></td>
										   			<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i) %></td>
										   			<td align="center">
										   				<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="new_start_dt" id="new_start_dt<%=i%>" value="<%=VNEW_START_DT.elementAt(i) %>" maxLength="10" 
								      						onblur="validateDate(this);checkStartEndDate(this,document.forms[0].new_end_dt,'F');" onchange="validateDate(this);checkStartEndDate(this,document.forms[0].new_end_dt,'F');" autocomplete="off" readOnly disabled>
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
							      						</div>
							      						<input type="hidden" name="temp_new_start_dt" id="temp_new_start_dt<%=i %>" value="<%=VNEW_START_DT.elementAt(i)%>">
										   			</td>
										   			<td align="center">
										   				<div class="input-group input-group-sm" >
								      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="new_end_dt" id="new_end_dt<%=i%>" value="<%=VNEW_END_DT.elementAt(i) %>" maxLength="10" 
								      						onblur="validateDate(this);checkStartEndDate(document.forms[0].new_start_dt,this,'T');" onchange="validateDate(this);checkStartEndDate(document.forms[0].new_start_dt,this,'T');" autocomplete="off" readOnly disabled>
								      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
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
							   			<td colspan="8"><div align="center"><%=utilmsg.infoMessage("<b>No Contract Duration Change Request Generated!</b>")%></div></td>
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
</div>	
	   		
<input type="hidden" name="last_index">
<input type="hidden" name="last_sub_index">

<%-- <input type="hidden" name="deal_size" value="<%=VFGSA.size()%>"> --%>
<input type="hidden" name="last_rowcolor" value="">
<input type="hidden" name="rowcolor_size" value="">

<input type='hidden' name='option' value="DLNG_CONTRACT_DURATION_MODIFICTION">
<input type="hidden" name="action" value="">

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
function setEnableDisable(obj, index) 
{
    var totalRows = <%=VDEAL_MAPPING.size()%>;

    for (var i = 0; i < totalRows; i++) 
    {
        var isSelected = (i == index);

        var new_start_dt = document.getElementById("new_start_dt" + i);
        var new_end_dt = document.getElementById("new_end_dt" + i);
        var submit = document.getElementById("submit" + i);
        var approve = document.getElementById("approve" + i);
        var reject = document.getElementById("reject" + i);

        var agmt_no = document.getElementById("agmt_no" + i);
        var agmt_rev = document.getElementById("agmt_rev" + i);
        var cont_no = document.getElementById("cont_no" + i);
        var cont_rev = document.getElementById("cont_rev" + i);
        var cont_type = document.getElementById("cont_type" + i);
        var segment = document.getElementById("segment" + i);
        var radio = document.getElementById("radio" + i);
        var max_dt = document.getElementById("max_dt" + i);
        var min_nom_dt = document.getElementById("min_nom_dt" + i);
        var signing_dt = document.getElementById("signing_dt" + i);
        var temp_new_start_dt = document.getElementById("temp_new_start_dt" + i);
        var start_dt = document.getElementById("start_dt" + i);

        var approve_access = document.forms[0].approve_access.value;
        var write_access = document.forms[0].write_access.value;

        if (isSelected && radio.checked) 
        {
            new_start_dt.readOnly = false;
            new_start_dt.disabled = false;

            new_end_dt.readOnly = false;
            new_end_dt.disabled = false;

            agmt_no.disabled = false;
            agmt_rev.disabled = false;
            cont_no.disabled = false;
            cont_rev.disabled = false;
            cont_type.disabled = false;
            segment.disabled = false;
            min_nom_dt.disabled = false;
            max_dt.disabled = false;
            signing_dt.disabled = false;
            start_dt.disabled = false;

            submit.disabled = (write_access !== "Y");

            if (approve_access === 'Y' && temp_new_start_dt.value!="") 
            {
                approve.disabled = false;
                reject.disabled = false;
            } 
            else 
            {
                approve.disabled = true;
                reject.disabled = true;

                if (temp_new_start_dt.value !== "") 
                {
                    alert("Approve/Reject Permission not available with current User!\n\n Please contact System Administrator!");
                }
            }
        } 
        else 
        {
            new_start_dt.readOnly = true;
            new_start_dt.disabled = true;

            new_end_dt.readOnly = true;
            new_end_dt.disabled = true;

            agmt_no.disabled = true;
            agmt_rev.disabled = true;
            cont_no.disabled = true;
            cont_rev.disabled = true;
            cont_type.disabled = true;
            segment.disabled = true;

            submit.disabled = true;
            approve.disabled = true;
            reject.disabled = true;
            max_dt.disabled = true;
            min_nom_dt.disabled = true;
            signing_dt.disabled = true;
            start_dt.disabled = true;
        }
    }
}

</script>
</form>
</body>
</html>