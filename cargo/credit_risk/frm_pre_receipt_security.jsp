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
	var security_type = document.forms[0].security_type.value;
	var received_dt = document.forms[0].received_dt.value;
	var currency = document.forms[0].currency.value;
	var amount = document.forms[0].amount.value;
	var u = document.forms[0].u.value;
	
	var msg="";
	var flag=true;
	
	if(trim(security_type) == "" || security_type=="0")
	{
		msg+="Select Security Type!\n";
		flag=false;
	}
	
	if(security_type!="OA")
	{
		if(trim(received_dt) == "")
		{
			msg+="Enter Expected Receipt(Due) Date!\n";
			flag=false;
		}
		if(trim(currency) == "" || currency=="0")
		{
			msg+="Select Currency!\n";
			flag=false;
		}
		if(trim(amount) == "")
		{
			msg+="Enter Security Amount!\n";
			flag=false;
		}
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Submit Security Details?");
		if(a)
		{
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function doModify(seq_no,map_seq,sec_ref,sec_type,due_dt,currency,amount,remark,seq_rev_no,status,countpty_name,countpty_abbr,deal_no,countpty_cd)
{
	var sec_category = document.forms[0].sec_category.value;
	document.forms[0].seq_no.value=seq_no;
	document.forms[0].seq_rev_no.value=seq_rev_no;
	document.forms[0].map_seq_no.value=map_seq;
	document.forms[0].security_ref.value=sec_ref;
	
	document.forms[0].security_type.value=sec_type;
	document.forms[0].received_dt.value=due_dt;
	document.forms[0].currency.value=currency;
	document.forms[0].amount.value=amount;
	document.forms[0].remark.value=remark;
	document.forms[0].deal_no.value=deal_no;
	
	document.forms[0].opration.value="MODIFY"
	
		if(status=="O")
		{
			status="In Order";
		}
		else if(status=="A")
		{
			status="Pending for Amendment";
		}
		else if(status=="P")
		{
			status="Pending";
		}
		else if(status=="C")
		{
			status="Cancelled";
		}
		else if(status=="R")
		{
			status="Restated";
		}
		
		if(sec_category=="R")
		{
			sec_category="Incoming";
		}
		else if(sec_category=="I")
		{
			sec_category="Outgoing";
		}
		
		if(currency=="1")
		{
			currency="INR";
		}
		else if(currency=="2")
		{
			currency="USD";
		}
	
	var old_value = "CP="+countpty_cd+"#NAME="+countpty_name+"#ABBR="+countpty_abbr+"#SEC_TYPE="+sec_type+"#SEC_CATEGORY="+sec_category+"#DEAL_TYPE="+""+"#DEAL_NO="+deal_no+"#VALUE="+amount+"#CURRENCY="+currency+
				 	"#FLUCTUATION="+""+"#VARIATION="+""+"#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+
	    		 	"#CONF_BANK_REF="+""+"#RECIEVED_DT="+due_dt+"#REVIEW_DT="+""+"#ISSUANCE_DT="+""+"#EXPIRY_DT="+""+"#STATUS="+status+"#TENOR="+""+"#REMARK="+remark+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+sec_ref+"#CANCEL_DT="+"";
	
	document.forms[0].pre_old_value.value=old_value;
}

function doClose(msg,msg_type)
{
	window.opener.securityRefresh(msg,msg_type);
	window.close();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DataBean_CreditRisk" id="credit_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String sec_category = request.getParameter("sec_category")==null?"":request.getParameter("sec_category");
String cont_start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String cont_end_dt=request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String display_map_id=utilBean.NewDealMappingId(owner_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");

String sec_category_nm="";
if(sec_category.equals("I")){
	sec_category_nm="Outgoing";
}else if(sec_category.equals("R")){
	sec_category_nm="Incoming";
}

String gx="K"; //THIS IS HARDCODED AS OF NOW 20230913

credit_risk.setCallFlag("PRE-RECEIPT_SECURITY");
credit_risk.setCounterparty_cd(counterparty_cd);
credit_risk.setComp_cd(owner_cd);
credit_risk.setAgmt_no(agmt_no);
credit_risk.setAgmt_rev_no(agmt_rev_no);
credit_risk.setCont_no(cont_no);
credit_risk.setCont_rev_no(cont_rev_no);
credit_risk.setContract_type(contract_type);
credit_risk.setGx(gx);
credit_risk.setSec_category(sec_category);
credit_risk.init();

String counterparty_abbr=credit_risk.getCounterparty_abbr();

Vector VCOUNTERPARTY_CD = credit_risk.getVCOUNTERPARTY_CD();
Vector VCONT_NO = credit_risk.getVCONT_NO();
Vector VCONT_REV_NO = credit_risk.getVCONT_REV_NO();
Vector VAGMT_NO = credit_risk.getVAGMT_NO();
Vector VAGMT_REV_NO = credit_risk.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = credit_risk.getVCONTRACT_TYPE();
Vector VSECURITY_TYPE = credit_risk.getVSECURITY_TYPE();
Vector VSECURITY_REF = credit_risk.getVSECURITY_REF();
Vector VSTATUS = credit_risk.getVSTATUS();
Vector VSTATUS_NM = credit_risk.getVSTATUS_NM();
Vector VCURRENCY = credit_risk.getVCURRENCY();
Vector VCURRENCY_NM = credit_risk.getVCURRENCY_NM();
Vector VAMOUNT = credit_risk.getVAMOUNT();
Vector VDUE_DT = credit_risk.getVDUE_DT();
Vector VISSUE_DT = credit_risk.getVISSUE_DT();
Vector VEXPIRE_DT = credit_risk.getVEXPIRE_DT();
Vector VREMARK = credit_risk.getVREMARK();
Vector VSEQ_NO = credit_risk.getVSEQ_NO();
Vector VMAP_SEQ_NO = credit_risk.getVMAP_SEQ_NO();
Vector VDEAL_MAPPING_ID=credit_risk.getVDEAL_MAPPING_ID();
Vector VSEQ_REV_NO = credit_risk.getVSEQ_REV_NO();
Vector VCOUNTERPARTY_ABBR = credit_risk.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NAME = credit_risk.getVCOUNTERPARTY_NAME();

%>
<body <%if(!msg.equals("")){ %>onload="doClose('<%=msg%>','<%=msg_type%>');"<%} %>>
<form method="post" action="../servlet/Frm_CreditRisk">

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
					    	<%=sec_category_nm%> Security Details For <%=counterparty_abbr%> (<%=display_map_id%>) [Contract Period : <%=cont_start_dt %> - <%=cont_end_dt %>]
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Sr#</th>
										<!-- <th>Contract Type</th>
										<th>Agreement#</th>
										<th>Agreement Rev#</th>
										<th>Contract#</th>
										<th>Contract Rev#</th> -->
										<th>Deal#</th>
										<th>Security Type</th>
										<th>Security Ref#</th>
										<th>Status</th>
										<th>Currency</th>
										<th>Amount</th>
										<th>Due Date</th>
										<th>Issuing Date</th>
										<th>Expire Date</th>
										<th>Remark</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPARTY_CD.size()>0) {%>
									<%for(int i=0;i<VCOUNTERPARTY_CD.size(); i++){
									String security_status = ""+VSTATUS.elementAt(i);
									%>
									<tr>
										<td align="center">
										<%if(security_status.equals("P") || security_status.equals("A")){%>
											<input type="radio" name="rd" onclick="doModify('<%=VSEQ_NO.elementAt(i)%>','<%=VMAP_SEQ_NO.elementAt(i)%>','<%=VSECURITY_REF.elementAt(i)%>','<%=VSECURITY_TYPE.elementAt(i)%>','<%=VDUE_DT.elementAt(i)%>','<%=VCURRENCY.elementAt(i)%>','<%=VAMOUNT.elementAt(i)%>','<%=VREMARK.elementAt(i)%>','<%=VSEQ_REV_NO.elementAt(i)%>','<%=VSTATUS_NM.elementAt(i)%>','<%=VCOUNTERPARTY_NAME.elementAt(i)%>','<%=VCOUNTERPARTY_ABBR.elementAt(i)%>','<%=display_map_id%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>');">&nbsp;&nbsp;
											<%} %>
											<%=i+1%>
										</td>
										<%-- <td align="center"><%=VCONTRACT_TYPE.elementAt(i)%></td>
										<td align="center"><%=VAGMT_NO.elementAt(i)%></td>
										<td align="center"><%=VAGMT_REV_NO.elementAt(i)%></td>
										<td align="center"><%=VCONT_NO.elementAt(i)%></td>
										<td align="center"><%=VCONT_REV_NO.elementAt(i)%></td> --%>
										<td align="center"><%=VDEAL_MAPPING_ID.elementAt(i)%></td>
										<td align="center"><%=VSECURITY_TYPE.elementAt(i)%></td>
										<td align="center"><%=VSECURITY_REF.elementAt(i)%></td>
										<td align="center"><%=VSTATUS_NM.elementAt(i)%></td>
										<td align="center"><%=VCURRENCY_NM.elementAt(i)%></td>
										<td align="right"><%=VAMOUNT.elementAt(i)%></td>
										<td align="center"><%=VDUE_DT.elementAt(i)%></td>
										<td align="center"><%=VISSUE_DT.elementAt(i)%></td>
										<td align="center"><%=VEXPIRE_DT.elementAt(i)%></td>
										<td><%=VREMARK.elementAt(i)%></td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="11" align="center"><%=utilmsg.infoMessage("<b>Security Not Configured!</b>") %></td>
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
					    	Pre-Receipt For <%=counterparty_abbr%> (<%=display_map_id%>) Deal Capturing
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Security Type<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="security_type">
										<option value="0">--Select--</option>
										<option value="LC">LC Letter of Credit</option>
						    			<option value="BG">BG Bank Guarantee</option>
						    			<option value="PCG">PCG Parent Corporate Guarantee</option>
						    			<option value="OA">Open Account</option>
						    			<option value="ADV">Advance Payment</option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Expected Receipt(Due) Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="received_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
	      						</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Currency<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="currency">
										<option value="0">--Select--</option>
										<option value="1">INR</option>
						    			<option value="2">USD</option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Amount<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="amount" value="" onblur="checkNumber1(this,16,4);">
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

<input type="hidden" name="option" value="PRE-RECEIPT_SECURITY">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="pre_old_value" value="">
<input type="hidden" name="deal_no" value="">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="sec_category" value="<%=sec_category%>">

<input type="hidden" name="seq_no" value="">
<input type="hidden" name="map_seq_no" value="">
<input type="hidden" name="seq_rev_no" value="">
<input type="hidden" name="security_ref" value="">

<input type="hidden" name="gx" value="<%=gx%>">

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