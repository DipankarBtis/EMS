<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh(opration)
{
	var counterparty_cd ="0";
	if(opration == "MODIFY")
	{
		counterparty_cd = document.forms[0].counterparty_cd.value;
	}
	var entity_role = document.forms[0].entity_role.value;
	var prev_entity_role = document.forms[0].prev_entity_role.value;
	
	if(entity_role != prev_entity_role)
	{
		counterparty_cd ="0";
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_entity_bank_mst.jsp?opration="+opration+"&u="+u+"&counterparty_cd="+counterparty_cd+"&entity_role="+entity_role;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

enableButton = true;
function doBankSubmit()
{
	var bank_name = document.forms[0].bank_name.value;
	var bank_eff_dt = document.forms[0].bank_eff_dt.value;
	var account_no = document.forms[0].account_no.value;
	var bank_branch = document.forms[0].bank_branch.value;
	var ifsc_code = document.forms[0].ifsc_code.value;
	var bank_state = document.forms[0].bank_state.value;
	var bank_category = document.forms[0].bank_category.value;
	var opration = document.forms[0].opration.value;
	var old_bank_eff_dt = document.forms[0].old_bank_eff_dt.value;
	
	if(bank_eff_dt != old_bank_eff_dt)
	{
		opration = "INSERT";
	}
	var entity_role = document.forms[0].entity_role.value;
	
	var msg="";
	var flag=true;
	
	if(opration == "MODIFY")
	{
		var counterparty_cd = document.forms[0].counterparty_cd.value;
		if(counterparty_cd=="" || counterparty_cd=="0" || trim(counterparty_cd)=="")
		{
			msg+="Select Counterparty!\n";
			flag=false
		}
	}
	
	if(entity_role=="" || trim(entity_role)=="" || entity_role=="0")
	{
		msg+="Select Entity Role!\n";
		flag=false
	}
	if(bank_category=="" || trim(bank_category)=="")
	{
		msg+="Select Category!\n";
		flag=false
	}
	if(bank_eff_dt=="" || trim(bank_eff_dt)=="")
	{
		msg+="Enter Eff Date of Bank!\n";
		flag=false
	}
	if(bank_name=="" || trim(bank_name)=="")
	{
		msg+="Enter Bank Name!\n";
		flag=false
	}
	if(account_no=="" || trim(account_no)=="")
	{
		msg+="Enter Account No!\n";
		flag=false
	}
	if(bank_branch=="" || trim(bank_branch)=="")
	{
		msg+="Enter Branch!\n";
		flag=false
	}
	if(ifsc_code=="" || trim(ifsc_code)=="")
	{
		if(bank_category == "DERV")
		{
			msg+="Enter Swift Code!\n";
			flag=false
		}
		else
		{
			msg+="Enter IFSC Code!\n";
			flag=false
		}
	}
	if(bank_state=="" || bank_state=="0" || trim(bank_state)=="")
	{
		msg+="Select State of Bank!\n";
		flag=false
	}
	
	if(flag)
	{
		alert("Same Eff. Date will overwrite the Bank details,\nDifferent Eff. Date will create new entry!");
		var a = confirm("Do you want to "+opration+" Counterparty Bank Detail?")
		if(a)
		{
			document.forms[0].option.value="COUNTERPARTY_BANK_MST"
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
function exportToXls()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var entity_role = document.forms[0].entity_role.value;
	
	var url = "xls_entity_bank_mst.jsp?fileName=Entity Virtual Account Master.xls&counterparty_cd="+counterparty_cd+"&entity_role="+entity_role;

	location.replace(url);
	
	/* if(counterparty_cd != "" && counterparty_cd != "0")
	{

	}
	else
	{
		alert("Select Counterparty!")
	} */
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String opration=request.getParameter("opration")==null?"MODIFY":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String entity_role = request.getParameter("entity_role")==null?"C":request.getParameter("entity_role");

if(entity_role.equals("B"))
{
	if(!owner_cd.equals(""))
	{
		counterparty_cd=owner_cd;
	}
}

dbcounterpty.setCallFlag("ENTITY_BANK_MASTER");
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

Vector VNCF_CATEGORY = dbcounterpty.getVNCF_CATEGORY();

String name=dbcounterpty.getName();
String abbr=dbcounterpty.getAbbr();
String eff_dt=dbcounterpty.getEff_dt();

String bank_eff_dt=dbcounterpty.getBank_eff_dt();
String bank_name=dbcounterpty.getBank_name();
String bank_account_no=dbcounterpty.getBank_account_no();
String bank_branch=dbcounterpty.getBank_branch();
String ifsc_code=dbcounterpty.getIfsc_code();
String bank_state=dbcounterpty.getBank_state();
String bank_formula=dbcounterpty.getBank_formula();
String bank_category=dbcounterpty.getBank_category();

Vector VBANK_FORMULA = dbcounterpty.getVBANK_FORMULA();
Vector VBANK_EFF_DT = dbcounterpty.getVBANK_EFF_DT();
Vector VBANK_CATEGORY = dbcounterpty.getVBANK_CATEGORY();
Vector VBANK_NAME = dbcounterpty.getVBANK_NAME();
Vector VBANK_ACCOUNT_NO = dbcounterpty.getVBANK_ACCOUNT_NO();
Vector VIFSC_CODE = dbcounterpty.getVIFSC_CODE();
Vector VBANK_BRANCH = dbcounterpty.getVBANK_BRANCH();
Vector VBANK_STATE = dbcounterpty.getVBANK_STATE();

Vector VSECTOR_CD = dbcounterpty.getVSECTOR_CD();
Vector VSECTOR_NAME = dbcounterpty.getVSECTOR_NAME();

//Used in TABLE
Vector VMST_COUNTERPARTY_ABBR = dbcounterpty.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = dbcounterpty.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_CD = dbcounterpty.getVMST_COUNTERPARTY_CD();

/* String old_value="CP="+counterparty_cd+"#NAME="+name+"#ABBR="+abbr+"#PANNO="+pan_no+"#PANDT="+pan_dt+"#KYC="+kyc_flg+"#IGX="+igx_flg+"#EFFDT="+eff_dt+
"#ADD="+address+"#CITY="+city+"#STATE="+state+"#ZONE="+zone+"#PIN="+pin+"#COUNTRY="+country+"#PH="+phone+"#FAX1="+fax1+
"#FAX2="+fax2+"#CELL="+mobile+"#EMAIL="+email+"#ALTPH="+alt_phone+"#REFFDT="+reg_eff_dt+"#STATUS="+status;
 */
%>
<body>
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
						<div>
							<div class="topheader">
								Entity Virtual Bank Account Master
							</div>
						</div>
						<div class="d-flex justify-content-between">
						 	<div class="btn-group">
								<select class="btn btn-outline-secondary btngrp <%if(!entity_role.equals("0")){%>btnactive<%}%>" name="entity_role" onchange="refresh('MODIFY')">
									<!-- <option value="0">Select Entity Roles</option> -->
									<option value="C">Customer</option>
					    			<!-- <option value="T">Trader</option>
					    			<option value="R">Transporter</option>
					    			<option value="V">Vessel Agent</option>
					    			<option value="H">Custom House Agent</option>
					    			<option value="S">Surveyor</option>
					    			<option value="B">Business Owner</option>
					    			<option value="G">Gas Exchange</option> -->
								</select>
							</div>&nbsp;
							<div onclick="exportToXls();" style="color:green;">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
							</div>
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
						<div class="col-sm-2 col-xs-2 col-md-2" align="right">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Counterparty<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm searchable-select" name="counterparty_cd" onchange="refresh('<%=opration%>');" id="select_box">
										<option value="0">--All--</option>
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
						<div class="col-sm-3 col-xs-3 col-md-3" align="right">
							<input class="form-control form-control-sm" type="text" id="globalSearch" onkeyup="globalSearchTable(this,'0')" placeholder="Search.." style="width:200px"/>
						</div>
					</div>
					<%-- <%if(!counterparty_cd.equals("") && !counterparty_cd.equals("0")){ %> --%>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=name.equals("")?"Counterparty":name%> Bank Details</label>
					</div>
					<%if(write_access.equals("Y")){ %>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" 
									<%if(!counterparty_cd.equals("") && !counterparty_cd.equals("0")){ %>
									data-bs-toggle="modal" data-bs-target="#BankModal" <%} %>
				    				onclick="<%if(!counterparty_cd.equals("") && !counterparty_cd.equals("0")){ %>
				    				setBankDtl('<%=bank_eff_dt%>','<%=bank_name%>','<%=bank_account_no%>','<%=bank_branch%>','<%=bank_state%>','<%=ifsc_code%>','<%=bank_category%>');
				    				<%}else{ %>alert('Select Counterparty to configure details!')<%} %>"
				    			>Bank Details Config</label>
							</div>
						</div>
					</div>
					<%} %>
					<div class="row m-b-5">
						<div class="table-responsive">
							<table id="ems_table0" class="table table-bordered table-hover ems_sorttbl">
								<thead id="ems_tbsort0">
									<tr>
										<th>Sr#</th>
										<th class="ems_thsort0">Counterparty</th>
										<th class="ems_thsort0">Bank Name</th>
										<th class="ems_thsort0">Bank A/C No.</th>
										<th class="ems_thsort0">IFSC | Swift code</th>
										<th class="ems_thsort0">Branch</th>
										<th class="ems_thsort0">State</th>
										<th class="ems_thsort0">Eff Date</th>
										<th class="ems_thsort0">Category</th>
									</tr>
								</thead>
								<tbody>
								<%if(VBANK_FORMULA.size()>0){ %>
								<%for(int i=0;i<VBANK_FORMULA.size(); i++){ %>
									<tr>
										<td align="center"><%=i+1%>.</td>
										<td title="<%=VMST_COUNTERPARTY_NM.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%></td>
										<%-- <td><%=VBANK_FORMULA.elementAt(i)%></td> --%>
										<td><%=VBANK_NAME.elementAt(i)%></td>
										<td><%=VBANK_ACCOUNT_NO.elementAt(i)%></td>
										<td><%=VIFSC_CODE.elementAt(i)%></td>
										<td><%=VBANK_BRANCH.elementAt(i)%></td>
										<td><%=VBANK_STATE.elementAt(i)%></td>
										<td align="center"><%=VBANK_EFF_DT.elementAt(i)%></td>
										<td align="center">
										<%if(VBANK_CATEGORY.elementAt(i).equals("DERV")){%>
											Derivatives
										<%}else{ %>
											<%=VBANK_CATEGORY.elementAt(i) %>
										<%} %>
										</td>
									</tr>
								<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="9"><%=utilmsg.infoMessage("<b>Bank Detail is Not Config!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<%-- <%}else{ %>
						&nbsp;<div align="center"><%=utilmsg.infoMessage("<b>Select counterpaty for Bank details!</b>") %></div>
					<%} %> --%>
				</div>
				<%-- <div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doBankSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
				</div> --%>
			</div>
		</div>
	</div>
</div>
<!-- Bank Detail -->
<div class="modal fade" id="BankModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Bank Detail Config
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Category<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">				    			
			    					<select class="form-select form-select-sm"  name="bank_category" id="bank_category" onchange="fetchBankDtl()">
										<option value="RLNG">RLNG</option>
										<option value="DLNG">DLNG</option>
			 							<option value="DERV">Derivatives</option>
						 			</select>
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label" style="color:#842029;"><b><i class='fa fa-exclamation-triangle fa-lg'></i>&nbsp;Eff Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
				      					<input type="hidden" name="old_bank_eff_dt" value="">
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="bank_eff_dt" value="" 
			      						maxLength="10" onblur="validateDate(this);checkEffectiveDate(this,document.forms[0].old_bank_eff_dt.value);" 
			      						onchange="validateDate(this);checkEffectiveDate(this,document.forms[0].old_bank_eff_dt.value);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Bank Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="bank_name" value="" maxLength="100">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>A/C No<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="account_no" value="" maxLength="20">
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Branch<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="bank_branch" value="" maxLength="100">
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label id="lbl_ifsc" class="form-label"><b>IFSC code<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ifsc_code" value="" maxLength="20">
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>State<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="bank_state">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VSTATE_NM.size();i++){ %>
										<option value="<%=VSTATE_NM.elementAt(i)%>"><%=VSTATE_NM.elementAt(i)%></option>
										<%} %>
									</select>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="form-group row">
					    		<div class="col-sm-12 col-xs-12 col-md-12">
					    			<span><%=utilmsg.errorMessage("<b>Same Eff. Date will overwrite the Bank details, Different Eff. Date will create new entry!</b>")%></span>
					    		</div>
					    	</div>
					    </div>
					</div>
				</div>
			</div>
			<div class="modal-footer cdfooter">
				<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
					<%if(write_access.equals("Y")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doBankSubmit();">
					<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
					<%} %>
				</div>	
      		</div>
      		<%-- <div class="modal-body mdbody">
      			<div class="cdbody">
	      			<div class="row m-b-5">
				    	<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Bank Detail(s)</label>
					</div>
					<div class="row m-b-5">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Bank Detail</th>
										<th>Eff Date</th>
										<th>Category</th>
									</tr>
								</thead>
								<tbody>
								<%if(VBANK_FORMULA.size()>0){ %>
								<%for(int i=0;i<VBANK_FORMULA.size(); i++){ %>
									<tr>
										<td align="center"><%=i+1%></td>
										<td><%=VBANK_FORMULA.elementAt(i)%></td>
										<td align="center"><%=VBANK_EFF_DT.elementAt(i)%></td>
										<td align="center">
										<%if(VBANK_CATEGORY.elementAt(i).equals("DERV")){%>
											Derivatives
										<%}else{ %>
											<%=VBANK_CATEGORY.elementAt(i) %>
										<%} %>
										</td>
									</tr>
								<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="4"><%=utilmsg.infoMessage("<b>Bank Detail is Not Config!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
      		</div> --%>
      	</div>
	</div>
</div>
<input type="hidden" name="option" value="ENTITY_BANK_MST">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="prev_entity_role" value="<%=entity_role%>">
<input type="hidden" name="old_value" value="">

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
function fetchBankDtl()
{
	var entity_role = document.forms[0].entity_role.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var bank_category = document.forms[0].bank_category.value;
	
	if(bank_category == "DERV")
	{
		var lbl_ifsc = document.getElementById('lbl_ifsc');
		lbl_ifsc.innerHTML = "<b>Swift Code<span class=\"s-red\">*</span></b>";
	}
	
	if((bank_category !="" && bank_category!="0") && (counterparty_cd != "" && counterparty_cd!="0") && (entity_role != "" && entity_role !="0"))
	{
		$.post("../servlet/DB_Master_Ajax?counterparty_cd="+counterparty_cd+"&entity="+entity_role+"&bank_category="+bank_category+"&setCallType=fetchBankDtl", function(responseJson) {
			console.log(responseJson);
			$.each(responseJson, function(index, json) 
			{
                document.forms[0].bank_category.value = json.BANK_CATEGORY;
                document.forms[0].bank_eff_dt.value = json.BANK_EFF_DT;
                document.forms[0].bank_name.value = json.BANK_NAME;
                document.forms[0].account_no.value = json.BANK_ACCOUNT_NO;
                document.forms[0].bank_branch.value = json.BANK_BRANCH;
                document.forms[0].ifsc_code.value = json.IFSC_CODE;
                document.forms[0].bank_state.value = json.BANK_STATE;
                document.forms[0].old_bank_eff_dt.value=json.BANK_EFF_DT;
			});
		});
	}
}
function setBankDtl(bank_eff_dt,bank_name,bank_account_no,bank_branch,bank_state,ifsc_code,bank_category)
{
	document.forms[0].bank_category.value="";

	/* document.forms[0].bank_name.value=bank_name;
	
	document.forms[0].bank_eff_dt.value=bank_eff_dt;
	document.forms[0].old_bank_eff_dt.value=bank_eff_dt;
	
	document.forms[0].account_no.value=bank_account_no;
	document.forms[0].bank_branch.value=bank_branch;
	document.forms[0].ifsc_code.value=ifsc_code;
	document.forms[0].bank_category.value=bank_category;
	if(trim(bank_state)=="")
	{
		document.forms[0].bank_state.value="0";
	}
	else
	{
		document.forms[0].bank_state.value=bank_state;	
	} */
}
</script>
</form>
</body>
</html>