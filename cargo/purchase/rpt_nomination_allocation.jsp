<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh(clearance)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var prev_clearance = document.forms[0].prev_clearance.value;
	var prev_counterparty_cd = document.forms[0].prev_counterparty_cd.value;
	
	var sel_trad_plant = document.forms[0].sel_trad_plant.value;
	var sel_bu_plant = document.forms[0].sel_bu_plant.value;
	
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var from_dt = document.forms[0].from_dt;
	var to_dt = document.forms[0].to_dt;
	
	var cont_st_dt = document.forms[0].cont_st_dt.value;
	var cont_end_dt = document.forms[0].cont_end_dt.value;
	
	var tmp_from_dt = document.forms[0].tmp_from_dt.value;
	var tmp_to_dt = document.forms[0].tmp_to_dt.value;
	
	var hid_cont_no = document.forms[0].hid_cont_no.value;
	var cont_cargo = document.forms[0].cont_cargo.value;
	var contract_type = document.forms[0].contract_type.value;
	var cargo_no = document.forms[0].cargo_no.value;
	
	var msg = "";
	var flag=true;
	
	if(prev_clearance != clearance)
	{
		counterparty_cd="0";
		cont_no="";
		cont_rev_no="";
		agmt_no="";
		agmt_rev_no="";
		from_dt="";
		to_dt="";
	}
	else if(counterparty_cd != prev_counterparty_cd)
	{
		cont_no="";
		cont_rev_no="";
		agmt_no="";
		agmt_rev_no="";
		from_dt="";
		to_dt="";
	}
	else
	{
		if(from_dt==null && to_dt==null)
		{
			from_dt = tmp_from_dt;
			to_dt = tmp_to_dt;
		}
		else
		{
			from_dt = from_dt.value;
			to_dt = to_dt.value;
			
			if(trim(from_dt)!="" && trim(to_dt)!="")
		   	{
				var compareDt =  compareDate(from_dt,to_dt);
				if(compareDt=="1")
				{
					if(from_dt != tmp_from_dt)
					{
						msg+="From Date should be less or equal To Date!\n"
						document.forms[0].from_dt.value = tmp_from_dt;
						flag = false
					}
					else if(to_dt != tmp_to_dt)
					{
						msg+="To Date should be grater or equal From Date!\n"
						document.forms[0].to_dt.value=tmp_to_dt;
						flag = false
					}
				}
				else
				{
					var val1 = compareDate(from_dt,cont_st_dt);
				 	var val2 = compareDate(from_dt,cont_end_dt);
				 	
				 	if(val1=="2")
				 	{
				   		msg+="From date must be in the range of Contract Start and End date..\nContract Period ("+cont_st_dt+" - "+cont_end_dt+")..\n";
				   		document.forms[0].from_dt.value = tmp_from_dt;
				   		flag = false
				 	}
				 	
				 	val1 = compareDate(to_dt,cont_end_dt);
				 	if(val1=="1")
				 	{
				   		msg+="To date must be less or equal to Contract Start and End date..\nContract Period ("+cont_st_dt+" - "+cont_end_dt+")..\n";
				   		document.forms[0].to_dt.value=tmp_to_dt;
				   		flag = false
				 	}
				}
		   	}
			else
			{
				msg+="Please Select From and To Date!\n";
				flag = false
			}
		}
	}
		
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "rpt_nomination_allocation.jsp?counterparty_cd="+counterparty_cd+"&u="+u+
				"&clearance="+clearance+"&sel_trad_plant="+sel_trad_plant+"&sel_bu_plant="+sel_bu_plant+
				"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
				"&from_dt="+from_dt+"&to_dt="+to_dt+"&cont_name="+hid_cont_no+"&cont_st_dt="+cont_st_dt+"&cont_end_dt="+cont_end_dt+
				"&cont_cargo="+cont_cargo+"&cont_type="+contract_type+"&cargo_no="+cargo_no;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}

function exportToXls(clearance)
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var prev_clearance = document.forms[0].prev_clearance.value;
	var prev_counterparty_cd = document.forms[0].prev_counterparty_cd.value;
	
	var sel_trad_plant = document.forms[0].sel_trad_plant.value;
	var sel_bu_plant = document.forms[0].sel_bu_plant.value;
	
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var cont_st_dt = document.forms[0].cont_st_dt.value;
	var cont_end_dt = document.forms[0].cont_end_dt.value;
	
	var tmp_from_dt = document.forms[0].tmp_from_dt.value;
	var tmp_to_dt = document.forms[0].tmp_to_dt.value;
	
	var hid_cont_no = document.forms[0].hid_cont_no.value;
	var cont_cargo = document.forms[0].cont_cargo.value;
	var contract_type = document.forms[0].contract_type.value;
	var cargo_no = document.forms[0].cargo_no.value;
	
	var url ="xls_nomination_allocation.jsp?fileName=Nomination And Allocation From "+from_dt+" To "+to_dt+".xls&counterparty_cd="+counterparty_cd+
	"&clearance="+clearance+"&sel_trad_plant="+sel_trad_plant+"&sel_bu_plant="+sel_bu_plant+
	"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
	"&from_dt="+from_dt+"&to_dt="+to_dt+"&cont_name="+hid_cont_no+"&cont_st_dt="+cont_st_dt+"&cont_end_dt="+cont_end_dt+
	"&cont_cargo="+cont_cargo+"&cont_type="+contract_type+"&cargo_no="+cargo_no

	location.replace(url);
}

var newWindow;
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var clearance = document.forms[0].clearance.value;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_allocation_contract_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&clearance="+clearance,"Trader Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_allocation_contract_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&clearance="+clearance,"Trader Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_cargo,cargo_no,cont_type)
{
	var clearance = document.forms[0].clearance.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_nomination_allocation.jsp?counterparty_cd="+countpty_cd+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&st_dt="+st_dt+"&end_dt="+end_dt+
			"&cont_cargo="+cont_cargo+"&cargo_no="+cargo_no+"&cont_type="+cont_type+
			"&u="+u+"&clearance="+clearance;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_Buy_Nom_Alloc_Mgmt" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String clearance = request.getParameter("clearance")==null?"KYC":request.getParameter("clearance");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");

String stdt = request.getParameter("st_dt")==null?"":request.getParameter("st_dt");
String enddt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String from_dt = request.getParameter("from_dt")==null?stdt:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?enddt:request.getParameter("to_dt");

String sel_trad_plant =request.getParameter("sel_trad_plant")==null?"0":request.getParameter("sel_trad_plant");
String sel_bu_plant =request.getParameter("sel_bu_plant")==null?"0":request.getParameter("sel_bu_plant");
String cont_cargo =request.getParameter("cont_cargo")==null?"":request.getParameter("cont_cargo");
String cargo_no =request.getParameter("cargo_no")==""?"0":request.getParameter("cargo_no");

String contract_type=request.getParameter("cont_type")==null?"":request.getParameter("cont_type");

if(contract_type.equals(""))
{
	if(clearance.equals("IGX"))
	{
		contract_type="I";
	}
	else if(clearance.equals("KYC"))
	{
		contract_type="D";
	}
}

purchase.setCallFlag("REPORT_NOMINATION_ALLOCATION");
purchase.setClearance(clearance);
purchase.setCounterparty_cd(counterparty_cd);
purchase.setAgmt_no(agmt_no);
purchase.setAgmt_rev_no(agmt_rev_no);
purchase.setCont_no(cont_no);
purchase.setCont_rev_no(cont_rev_no);
purchase.setContract_type(contract_type);
purchase.setComp_cd(owner_cd);
purchase.setFrom_date(from_dt);
purchase.setTo_date(to_dt);
purchase.setPlant_seq(sel_trad_plant);
purchase.setBu_plant_seq(sel_bu_plant);
purchase.setCont_cargo(cont_cargo);
purchase.setCargo_no(cargo_no);
purchase.init();

Vector VCOUNTERPARTY_CD = purchase.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = purchase.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = purchase.getVCOUNTERPARTY_ABBR();

Vector VGAS_DT = purchase.getVGAS_DT();
Vector VBUYER_QTY_MMBTU = purchase.getVBUYER_QTY_MMBTU();
Vector VBUYER_QTY_SCM = purchase.getVBUYER_QTY_SCM();
Vector VSELLER_QTY_MMBTU = purchase.getVSELLER_QTY_MMBTU();
Vector VSELLER_QTY_SCM = purchase.getVSELLER_QTY_SCM();
Vector VQTY_MMBTU = purchase.getVQTY_MMBTU();
Vector VQTY_SCM = purchase.getVQTY_SCM();
Vector VDCQ = purchase.getVDCQ();
Vector VMDCQ = purchase.getVMDCQ();
Vector VCOLOR = purchase.getVCOLOR();
Vector VBUYER_COLOR = purchase.getVBUYER_COLOR();
Vector VSELLER_COLOR = purchase.getVSELLER_COLOR();

Vector VSEL_PLANT_SEQ_NO = purchase.getVSEL_PLANT_SEQ_NO();
Vector VSEL_PLANT_ABBR = purchase.getVSEL_PLANT_ABBR();
Vector VSEL_BU_CD = purchase.getVSEL_BU_CD();
Vector VSEL_BU_PLANT_SEQ_NO = purchase.getVSEL_BU_PLANT_SEQ_NO();
Vector VSEL_BU_PLANT_ABBR = purchase.getVSEL_BU_PLANT_ABBR();

String start_dt = purchase.getStart_dt();
String end_dt = purchase.getEnd_dt();
String cont_name = purchase.getCont_name();
String dcq = purchase.getDcq();
String mdcq_percentage = purchase.getMdcq_percentage();
if(mdcq_percentage.equals("")){
	mdcq_percentage="100";
}
if(dcq.equals("")){
	dcq="0";
}

String displayContNm="";
if(!start_dt.equals("") && !end_dt.equals(""))
{
	displayContNm=cont_name+"  ("+start_dt+" - "+end_dt+")";
}


%>
<body>
<%@ include file="../home/header.jsp"%>
<form>
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
							Nomination & Allocation Report
						</div>
						<div align="right" onclick="exportToXls('<%=clearance%>');" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12" align="center">
				    				<div class="btn-group">
										<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("KYC")){%>btnactive<%}%>" onclick="refresh('KYC');">KYC</label>
										<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("IGX")){%>btnactive<%}%>" onclick="refresh('IGX');">IGX</label>
									</div>
				    				<!-- <input type="radio" name="rd_clear">&nbsp; KYC &nbsp;&nbsp;
				    				<input type="radio" name="rd_clear">&nbsp; IGX -->
				    			</div>
				  			</div>
						</div>
					</div>
					&nbsp;
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Trader<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=clearance%>');">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    			<!-- <label class="form-label"><b>Contract No<span class="s-red">*</span></b></label> -->
				    			<input type="button" class="btn rounded-pill btn-info btn-sm" value="Select Contract" onclick="openContList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="cont_name" value="<%=displayContNm%>" maxLength="50" readOnly style="font-weight:bold;">
				      				<input type="hidden" class="form-control form-control-sm" name="hid_cont_no" value="<%=cont_name%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_no" value="<%=agmt_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="agmt_rev_no" value="<%=agmt_rev_no%>" maxLength="2" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_no" value="<%=cont_no%>" maxLength="6" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="cont_rev_no" value="<%=cont_rev_no%>" maxLength="2" readOnly>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-6 col-xs-6 col-md-6" align="center">
							<div class="btn-group">
								<select class="btn btn-sm select_btn" name="sel_trad_plant" onchange="refresh('<%=clearance%>');">
									<option value="0">All Trader Plant</option>
									<%for(int i=0; i<VSEL_PLANT_SEQ_NO.size(); i++){ %>
									<option value="<%=VSEL_PLANT_SEQ_NO.elementAt(i)%>"><%=VSEL_PLANT_ABBR.elementAt(i)%></option>
									<%} %>
								</select>
								<select class="btn btn-sm select_btn" name="sel_bu_plant" onchange="refresh('<%=clearance%>');">
									<option value="0">All BU</option>
									<%for(int i=0; i<VSEL_BU_PLANT_SEQ_NO.size(); i++){ %>
									<option value="<%=VSEL_BU_PLANT_SEQ_NO.elementAt(i)%>"><%=VSEL_BU_PLANT_ABBR.elementAt(i)%></option>
									<%} %>
								</select>
							</div>
							<script>document.forms[0].sel_trad_plant.value="<%=sel_trad_plant%>"</script>
							<script>document.forms[0].sel_bu_plant.value="<%=sel_bu_plant%>"</script>
						</div>
					</div>
				</div>
			</div>
			&nbsp;
			<div class="card cardmain" <%if((cont_no.equals("") || cont_no.equals("0")) && (cont_rev_no.equals("") || cont_rev_no.equals("0"))){%>style="display:none;"<%} %>>
				<div class="card-header cdheader">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Gas Day/s<span class="s-red">*</span></b></label>
								</div>
			    				<div class="col">
				      				<div class="input-group input-group-sm" >
					      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" maxLength="10" 
					      				onchange="validateDate(this);refresh('<%=clearance%>');">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      				<%-- onchange="validateDate(this);checkFromToDate(this,document.forms[0].to_dt,'F');refresh('<%=clearance%>');"> --%>
				      				</div>
				    			</div>
				    			<div class="col-1">
				    				<label class="form-label"><b>To</b></label>
				    			</div>
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
					      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" maxLength="10" 
					      				onchange="validateDate(this);refresh('<%=clearance%>');">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      				
					      				<%-- onchange="validateDate(this);checkFromToDate(document.forms[0].from_dt,this,'T');refresh('<%=clearance%>');"> --%>
				      				</div>
				    			</div>
				    		</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th rowspan="2">Gas Day</th>
										<th rowspan="2">DCQ<br>(MMBTU)</th>
										<th rowspan="2">MDCQ<br>(MMBTU)</th>
										<th colspan="2">Buyer Nomination</th>
										<th colspan="2" title="Entry Point Energy">Seller Nomination</th>
										<th colspan="2" title="Exit Point Energy">Allocation</th>
									</tr>
									<tr>
										<th>Energy (MMBTU)</th>
										<th>Energy (SCM)</th>
										<th>Energy (MMBTU)</th>
										<th>Energy (SCM)</th>
										<th>Energy (MMBTU)</th>
										<th>Energy (SCM)</th>
									</tr>
								</thead>
								<tbody>
								<%for(int i=0; i<VGAS_DT.size(); i++){ %>
									<tr>
										<td align="center"><%=VGAS_DT.elementAt(i) %></td>
										<td align="right"><%=VDCQ.elementAt(i) %></td>
										<td align="right"><%=VMDCQ.elementAt(i) %></td>
										<td align="right" style="background:<%=VBUYER_COLOR.elementAt(i)%>"><%=VBUYER_QTY_MMBTU.elementAt(i) %></td>
										<td align="right" style="background:<%=VBUYER_COLOR.elementAt(i)%>"><%=VBUYER_QTY_SCM.elementAt(i) %></td>
										<td align="right" style="background:<%=VSELLER_COLOR.elementAt(i)%>"><%=VSELLER_QTY_MMBTU.elementAt(i) %></td>
										<td align="right" style="background:<%=VSELLER_COLOR.elementAt(i)%>"><%=VSELLER_QTY_SCM.elementAt(i) %></td>
										<td align="right" style="background:<%=VCOLOR.elementAt(i)%>"><%=VQTY_MMBTU.elementAt(i) %></td>
										<td align="right" style="background:<%=VCOLOR.elementAt(i)%>"><%=VQTY_SCM.elementAt(i) %></td>
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

<input type="hidden" name="prev_clearance" value="<%=clearance%>">
<input type="hidden" name="prev_counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="clearance" value="<%=clearance%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">

<input type="hidden" name="tmp_from_dt" value="<%=from_dt%>">
<input type="hidden" name="tmp_to_dt" value="<%=to_dt%>">
<input type="hidden" name="cont_st_dt" value="<%=start_dt%>">
<input type="hidden" name="cont_end_dt" value="<%=end_dt%>">
<input type="hidden" name="cont_cargo" value="<%=cont_cargo%>">
<input type="hidden" name="cargo_no" value="<%=cargo_no%>">

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