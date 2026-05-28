<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var customer_cd = document.forms[0].customer_cd.value;
	var prev_counterparty_cd = document.forms[0].prev_counterparty_cd.value;
	var prev_customer_cd = document.forms[0].prev_customer_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	
	var from_dt = document.forms[0].from_dt;
	var to_dt = document.forms[0].to_dt;
	
	if(from_dt!=undefined && from_dt!=null)
	{
		from_dt = document.forms[0].from_dt.value;
		to_dt = document.forms[0].to_dt.value;
	}
	else
	{
		from_dt="";
		to_dt="";
	}
	
	var hid_from_dt=document.forms[0].hid_from_dt.value;
	var hid_to_dt = document.forms[0].hid_to_dt.value;
	var cont_start_dt = document.forms[0].cont_start_dt.value;
	var cont_end_dt = document.forms[0].cont_end_dt.value;
	
	var prev_contract_type=document.forms[0].prev_contract_type.value;
	
	if(prev_contract_type != contract_type)
	{
		counterparty_cd="0";
		customer_cd="0"
		agmt_no="";
		agmt_rev_no="";
		cont_no="";
		cont_rev_no="";
		
		from_dt="";
		to_dt="";
	}
	else if(prev_counterparty_cd != counterparty_cd)
	{
		customer_cd="0"
		agmt_no="";
		agmt_rev_no="";
		cont_no="";
		cont_rev_no="";
		
		from_dt="";
		to_dt="";
	}
	else if(prev_customer_cd != customer_cd)
	{
		counterparty_cd="0";
		agmt_no="";
		agmt_rev_no="";
		cont_no="";
		cont_rev_no="";
		
		from_dt="";
		to_dt="";
	}
	
	var flag = true;
	
	if(trim(from_dt)!="" && trim(to_dt)!="")
   	{
		var compareDt =  compareDate(from_dt,to_dt);
		if(compareDt=="1")
		{
			if(value=="1")
			{
				alert("From Date should be less or equal To Date!!")
				document.forms[0].from_dt.value = hid_from_dt;
				flag = false
			}
			else
			{
				alert("To Date should be grater or equal From Date!!")
				document.forms[0].to_dt.value=hid_to_dt;
				flag = false
			}
		}
		else
		{
			var val1 = compareDate(from_dt,cont_start_dt);
		 	var val2 = compareDate(from_dt,cont_end_dt);
		 	
		 	if(val1=="2")
		 	{
		   		alert("From date must be in the range of GTC Start and End date..\nGTC Period ("+cont_start_dt+" - "+cont_end_dt+")..\n");
		   		document.forms[0].from_dt.value = hid_from_dt;
		   		flag = false
		 	}
		 	
		 	val1 = compareDate(to_dt,cont_end_dt);
		 	if(val1=="1")
		 	{
		   		alert("To date must be less or equal to GTC Start and End date..\nGTC Period ("+cont_start_dt+" - "+cont_end_dt+")..\n");
		   		document.forms[0].to_dt.value=hid_to_dt;
		   		flag = false
		 	}
		}
   	}
	else
	{
		if(trim(cont_no)!="")
		{
			alert("Please Select From and To Date!!");
			flag = false
		}
	}
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "rpt_gta_imbalance.jsp?counterparty_cd="+counterparty_cd+"&customer_cd="+customer_cd+
				"&u="+u+"&contract_type="+contract_type+"&from_dt="+from_dt+"&to_dt="+to_dt+
				"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}

var newWindow;
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var customer_cd = document.forms[0].customer_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	
	var flag=true;
	if(contract_type=="C")
	{
		if((customer_cd=="0" || trim(customer_cd) == "") && (contract_type=="0" || trim(contract_type) == ""))
		{
			alert("Select Customer!\nSelect Contract Type!")
			flag=false;
		}
		else if((customer_cd=="0" || trim(customer_cd) == ""))
		{
			alert("Select Customer!")
			flag=false;
		}
		else if((contract_type=="0" || trim(contract_type) == ""))
		{
			alert("Select Contract Type!")
			flag=false;
		}
	}
	else
	{
		if((counterparty_cd=="0" || trim(counterparty_cd) == "") && (contract_type=="0" || trim(contract_type) == ""))
		{
			alert("Select Transporter!\nSelect Contract Type!")
			flag=false;
		}
		else if((counterparty_cd=="0" || trim(counterparty_cd) == ""))
		{
			alert("Select Transporter!")
			flag=false;
		}
		else if((contract_type=="0" || trim(contract_type) == ""))
		{
			alert("Select Contract Type!")
			flag=false;
		}
	}
	
	var url="frm_gta_nom_contract_list.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&customer_cd="+customer_cd;
	
	if(flag)
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"GTA Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"GTA Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
}

function setContractDetail(countrpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no)
{
	var contract_type = document.forms[0].contract_type.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var customer_cd = document.forms[0].customer_cd.value;
	
	if(contract_type=="C")
	{
		counterparty_cd=countrpty_cd;
	}
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_gta_imbalance.jsp?counterparty_cd="+counterparty_cd+"&customer_cd="+customer_cd+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&contract_type="+contract_type+
			"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function exportToXls()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var customer_cd = document.forms[0].customer_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	
	var from_dt = document.forms[0].from_dt;
	var to_dt = document.forms[0].to_dt;
	
	if(from_dt!=undefined && from_dt!=null)
	{
		from_dt = document.forms[0].from_dt.value;
		to_dt = document.forms[0].to_dt.value;
	}
	else
	{
		from_dt="";
		to_dt="";
	}
	
	var hid_from_dt=document.forms[0].hid_from_dt.value;
	var hid_to_dt = document.forms[0].hid_to_dt.value;
	var cont_start_dt = document.forms[0].cont_start_dt.value;
	var cont_end_dt = document.forms[0].cont_end_dt.value;
	
	var flag = true;
	
	if(trim(from_dt)!="" && trim(to_dt)!="")
   	{
		var compareDt =  compareDate(from_dt,to_dt);
		if(compareDt=="1")
		{
			if(value=="1")
			{
				alert("From Date should be less or equal To Date!!")
				flag = false
			}
			else
			{
				alert("To Date should be grater or equal From Date!!")
				flag = false
			}
		}
		else
		{
			var val1 = compareDate(from_dt,cont_start_dt);
		 	var val2 = compareDate(from_dt,cont_end_dt);
		 	
		 	if(val1=="2")
		 	{
		   		alert("From date must be in the range of GTC Start and End date..\nGTC Period ("+cont_start_dt+" - "+cont_end_dt+")..\n");
		   		flag = false
		 	}
		 	
		 	val1 = compareDate(to_dt,cont_end_dt);
		 	if(val1=="1")
		 	{
		   		alert("To date must be less or equal to GTC Start and End date..\nGTC Period ("+cont_start_dt+" - "+cont_end_dt+")..\n");
		   		flag = false
		 	}
		}
   	}
	else
	{
		if(trim(cont_no)!="")
		{
			alert("Please Select From and To Date!!");
			flag = false
		}
	}
	
	if(flag)
	{
		var url = "xls_gta_imbalance.jsp?counterparty_cd="+counterparty_cd+"&customer_cd="+customer_cd+
				"&contract_type="+contract_type+"&from_dt="+from_dt+"&to_dt="+to_dt+
				"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no
	
		location.replace(url);
	}
}
</script>
<%@ include file="../util/common_js.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GtaMaster" id="gta" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String customer_cd = request.getParameter("customer_cd")==null?"0":request.getParameter("customer_cd");
String contract_type=request.getParameter("contract_type")==null?"C":request.getParameter("contract_type");
String cont_map_id=request.getParameter("cont_map_id")==null?"":request.getParameter("cont_map_id");

String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");

String from_dt=request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?"":request.getParameter("to_dt");

gta.setCallFlag("GTC_IMBALANCE");
gta.setComp_cd(owner_cd);
gta.setCounterparty_cd(counterparty_cd);
gta.setContract_type(contract_type);
gta.setAgmt_no(agmt_no);
gta.setAgmt_rev_no(agmt_rev_no);
gta.setCont_no(cont_no);
gta.setCont_rev_no(cont_rev_no);
gta.setFrom_dt(from_dt);
gta.setTo_dt(to_dt);
gta.init();

Vector VCOUNTERPARTY_CD = gta.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = gta.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = gta.getVCOUNTERPARTY_ABBR();

Vector VCUSTOMER_CD = gta.getVCUSTOMER_CD();
Vector VCUSTOMER_NM = gta.getVCUSTOMER_NM();
Vector VCUSTOMER_ABBR = gta.getVCUSTOMER_ABBR();

String counterparty_abbr = gta.getCounterparty_abbr();
String cont_ref_no = gta.getCont_ref_no();
String start_dt = gta.getStart_dt();
String end_dt = gta.getEnd_dt();
String entry_pt_mapping_id =gta.getEntry_pt_mapping_id();
String exit_pt_mapping_id =gta.getExit_pt_mapping_id();
String entry_pt_nm = gta.getEntry_pt_nm();
String exit_pt_nm = gta.getExit_pt_nm();
String linked_sales_cont_map=gta.getLinked_sales_cont_map();
String bu_plant_seq=gta.getBu_plant_seq();
String sip_pay_freq=gta.getSip_pay_freq();

String cont_name="";
String cont_mapping=counterparty_abbr+" - "+contract_type+agmt_no+"-"+agmt_rev_no+"-"+cont_no+"-"+cont_rev_no;

if(!cont_ref_no.equals(""))
{
	cont_name=cont_mapping+" ("+cont_ref_no+") ["+start_dt+" - "+end_dt+"]";
}

if(from_dt.equals(""))
{
	from_dt=gta.getFrom_dt();
}
if(to_dt.equals(""))
{
	to_dt=gta.getTo_dt();
}

Vector VGAS_DT = gta.getVGAS_DT();
Vector VMDQ = gta.getVMDQ();
Vector VNOM_ENTRY_QTY = gta.getVNOM_ENTRY_QTY();
Vector VNOM_EXIT_QTY = gta.getVNOM_EXIT_QTY();
Vector VSCH_ENTRY_QTY = gta.getVSCH_ENTRY_QTY();
Vector VSCH_EXIT_QTY = gta.getVSCH_EXIT_QTY();
Vector VALLOC_ENTRY_QTY = gta.getVALLOC_ENTRY_QTY();
Vector VALLOC_EXIT_QTY = gta.getVALLOC_EXIT_QTY();

Vector VTRANSMISSION_QTY = gta.getVTRANSMISSION_QTY();
Vector VDAILY_IMBALANCE = gta.getVDAILY_IMBALANCE();
Vector VADJ_IMBALANCE = gta.getVADJ_IMBALANCE();
Vector VCUMULATIVE_IMBALANCE = gta.getVCUMULATIVE_IMBALANCE();
Vector VDAILY_UNAUTHORIZED_OVERRUN = gta.getVDAILY_UNAUTHORIZED_OVERRUN();
Vector VCHARGEABLE_OVERRUN = gta.getVCHARGEABLE_OVERRUN();
Vector VCHARGEABLE_POSITIVE_IMBALANCE = gta.getVCHARGEABLE_POSITIVE_IMBALANCE();
Vector VCHARGEABLE_NEGETIVE_IMBALANCE = gta.getVCHARGEABLE_NEGETIVE_IMBALANCE();

String total_nom_entry_qty=gta.getTotal_nom_entry_qty();
String total_nom_exit_qty=gta.getTotal_nom_exit_qty();
String total_sch_entry_qty=gta.getTotal_sch_entry_qty();
String total_sch_exit_qty=gta.getTotal_sch_exit_qty();
String total_alloc_entry_qty=gta.getTotal_alloc_entry_qty();
String total_alloc_exit_qty=gta.getTotal_alloc_exit_qty();
String total_var_mdq=gta.getTotal_var_mdq();
String total_transmission_qty=gta.getTotal_transmission_qty();
String total_chargeable_overrun=gta.getTotal_chargeable_overrun();
String total_positive_imbalance=gta.getTotal_positive_imbalance();
String total_negitive_imbalance=gta.getTotal_negitive_imbalance();

String transmission_qty_formula="Monthly Ship/Pay:\nTransmission MMBTU = Exit Pt. Allocation";
String daily_transmission_qty_formula="Daily Ship/Pay:\nIF (Exit Pt. Allocation >= shippay% of MDQ)\n{Transmission MMBTU = Exit Pt. Allocation}\nELSE\n{Transmission MMBTU = MDQ * shippay%}";
if (sip_pay_freq.equals("D")){transmission_qty_formula=daily_transmission_qty_formula;} 
String daily_imbalance_formula="Daily Imbalance\n=(Entry Pt. Allocation - Exit Pt. Allocation)";
/*String unauthorize_overrun_formula="IF(Exit Pt. Scheduling > MDQ){\n\tIF(Exit Pt. Allocation > Exit Pt. Scheduling){\n\t\tUnauthorized Overrun=Exit Pt. Allocation - Exit Pt. Scheduling\n\t}ELSE{Unauthorized Overrun=0}\n}"
									+"ELSE{\n\tIF(Exit Pt. Allocation > MDQ){\n\tUnauthorized Overrun=Exit Pt. Allocation - MDQ\n\t}ELSE{Unauthorized Overrun=0}\n}";*/
/* String unauthorize_overrun_formula="IF(Exit Pt. Scheduling != MDQ){\n\tIF(Exit Pt. Allocation > Exit Pt. Scheduling){\n\t\tUnauthorized Overrun=Exit Pt. Allocation - Exit Pt. Scheduling\n\t}}ELSE{Unauthorized Overrun=0}\n}"; */
String unauthorize_overrun_formula="IF(Exit Pt. Scheduling >= MDQ){\n\tIF(Exit Pt. Allocation > Exit Pt. Scheduling){\n\t\tUnauthorized Overrun=Exit Pt. Allocation - Exit Pt. Scheduling\n\t}ELSE{Unauthorized Overrun=0}\n}"
		+"ELSE IF(Exit Pt. Scheduling < MDQ){\n\tIF(Exit Pt. Allocation > Exit Pt. Scheduling){\n\tUnauthorized Overrun=Exit Pt. Allocation - MDQ\n\t}ELSE{Unauthorized Overrun=0}\n}";
/*String chargeable_overrun_formula="IF(Exit Pt. Scheduling > MDQ){\n\tIF(Exit Pt. Allocation > 110% of Exit Pt. Scheduling){\n\t\tChargeable Overrun=Exit Pt. Allocation - 110% of Exit Pt. Scheduling\n\t}ELSE{Chargeable Overrun=0}\n}"
								+"ELSE{\n\tIF(Exit Pt. Allocation > 110% of Exit Pt. Scheduling){\n\t\tChargeable Overrun=Exit Pt. Allocation - (MDQ + 10% of Exit Pt. Scheduling)"
								+"\n\t}ELSE IF(Exit Pt. Allocation > 110% of MDQ){\n\t\tChargeable Overrun=Exit Pt. Allocation - 110% of MDQ\n\t}ELSE{Chargeable Overrun=0}\n}";*/
/* String chargeable_overrun_formula="IF(Exit Pt. Scheduling != MDQ){\n\tIF(Exit Pt. Allocation > 110% of Exit Pt. Scheduling){\n\t\tChargeable Overrun=Exit Pt. Allocation - 110% of Exit Pt. Scheduling\n\t}}ELSE{Chargeable Overrun=0}\n}"; */
String chargeable_overrun_formula="IF(Exit Pt. Scheduling >= MDQ){\n\tIF(Exit Pt. Allocation > 110% of Exit Pt. Scheduling){\n\t\tChargeable Overrun=Exit Pt. Allocation - 110% of Exit Pt. Scheduling\n\t}ELSE{Chargeable Overrun=0}\n}"
		+"ELSE IF(Exit Pt. Scheduling <> MDQ){\n\tIF(Exit Pt. Allocation > 110% of Exit Pt. Scheduling){\n\t\tChargeable Overrun=Exit Pt. Allocation - (MDQ + 10% of Exit Pt. Scheduling)"
		+"\n\t}ELSE{Chargeable Overrun=0}\n}";
String chargeable_positive_imbalance_formula="Chargeable +Ve Imbalance = Cumulative Imbalance - 10% of MDQ\nIF(Chargeable +Ve Imbalance > 0, Chargeable +Ve Imbalance, 0)";
String chargeable_negetive_imbalance_formula="Chargeable -Ve Imbalance = Cumulative Imbalance + 5% of MDQ\nIF(Chargeable -Ve Imbalance > 0), 0, Chargeable -Ve Imbalance)";

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post">
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
							CT Imbalance Report
						</div>
						<div>
							<div class="btn-group" onclick="exportToXls();">
								<label><i class="fa fa-file-excel-o fa-2x excel_icon"></i></label>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<label class="form-label"><b>Contract Type</b></label>
								</div>
							</div>
						</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="contract_type" onchange="refresh();">
										<option value="C">Customer</option>
						    			<option value="R">Transporter</option>
						    		</select>
						    		<script>document.forms[0].contract_type.value="<%=contract_type%>"</script>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<%if(contract_type.equals("C")){ %>
										<label class="form-label"><b>Customer</b></label>
									<%}else{ %>
										<label class="form-label"><b>Transporter</b></label>
									<%} %>
								</div>
							</div>
						</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<!-- CUSTOMER MASTER -->
									<select class="form-select form-select-sm" name="customer_cd" onchange="refresh();" <%if(!contract_type.equals("C")){ %>style="display:none"<%} %>>
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCUSTOMER_CD.size();i++){ %>
										<option value="<%=VCUSTOMER_CD.elementAt(i)%>"><%=VCUSTOMER_ABBR.elementAt(i)%> - <%=VCUSTOMER_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].customer_cd.value="<%=customer_cd%>"</script>
									
									<!-- TRANSPORTER MASTER -->
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh();" <%if(contract_type.equals("C")){ %>style="display:none"<%} %>>
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
				      				<input type="hidden" class="form-control form-control-sm" name="sales_cont_map" value="<%=linked_sales_cont_map%>" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="bu_plant_seq" value="<%=bu_plant_seq%>" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="entry_pt_mapping_id" value="<%=entry_pt_mapping_id%>" readOnly>
				      				<input type="hidden" class="form-control form-control-sm" name="exit_pt_mapping_id" value="<%=exit_pt_mapping_id%>" readOnly>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="button" class="btn btn-info btn-sm select_btn" value="Select Capacity Tranche" onclick="openContList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" value="<%=cont_name%>" readOnly style="font-weight:bold;">
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<%if(!cont_no.equals("")){ %>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Gas Day : From</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
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
										<th rowspan="2">CT MDQ<br>(MMBTU)</th>
										<th colspan="3">Transporter Entry Point <font style="background:#ff99ff;color:var(--header_font_color);">(<%=entry_pt_nm%>)</font></th>
										<th colspan="3">Transporter Exit Point <font style="background:#ff99ff;;color:var(--header_font_color);">(<%=exit_pt_nm%>)</font></th>
										<th rowspan="2" title="<%=transmission_qty_formula%>">Transmission<br>(MMBTU)</th>
										<th rowspan="2" title="<%=daily_imbalance_formula%>">Daily Imbalance(+/-)<br>(MMBTU)</th>
										<th rowspan="2" title="=Adjusted Imbalance">Adjusted Imbalance<br>(MMBTU)</th>
										<th rowspan="2" title="=Cummulative (Daily + Adjusted) Imbalance">Cumulative Imbalance<br>(MMBTU)</th>
										<th rowspan="2" title="<%=unauthorize_overrun_formula%>">Daily Unauthorized Overrun<br>(MMBTU)</th>
										<th rowspan="2" title="<%=chargeable_overrun_formula%>">Chargeable Overrun<br>(MMBTU)</th>
										<th rowspan="2" title="<%=chargeable_positive_imbalance_formula%>">Chargeable +Ve Imbalance<br>(MMBTU)</th>
										<th rowspan="2" title="<%=chargeable_negetive_imbalance_formula%>">Chargeable -Ve Imbalance<br>(MMBTU)</th>
									</tr>
									<tr>
										<th style="background-color: #cfe2ff;">Nomination<br>(MMBTU)</th>
										<th style="background-color: #fff3cd;">Scheduling<br>(MMBTU)</th>
										<th style="background-color: #d1e7dd;">Allocation<br>(MMBTU)</th>
										<th style="background-color: #cfe2ff;">Nomination<br>(MMBTU)</th>
										<th style="background-color: #fff3cd;">Scheduling<br>(MMBTU)</th>
										<th style="background-color: #d1e7dd;">Allocation<br>(MMBTU)</th>
									</tr>
								</thead>
								<tbody>
									<%for(int i=0; i<VGAS_DT.size(); i++){ %>
									<tr>
										<td align="center"><%=VGAS_DT.elementAt(i)%></td>
										<td align="right"><%=VMDQ.elementAt(i) %></td>
										<td align="right" style="background-color: #cfe2ff;color: #06357a;"><%=VNOM_ENTRY_QTY.elementAt(i)%></td>
										<td align="right" style="background-color: #fff3cd;color: #523e02;"><%=VSCH_ENTRY_QTY.elementAt(i)%></td>
										<td align="right" style="background-color: #d1e7dd;color: #0c4128;"><%=VALLOC_ENTRY_QTY.elementAt(i)%></td>
										<td align="right" style="background-color: #cfe2ff;color: #06357a;"><%=VNOM_EXIT_QTY.elementAt(i)%></td>
										<td align="right" style="background-color: #fff3cd;color: #523e02;"><%=VSCH_EXIT_QTY.elementAt(i) %></td>
										<td align="right" style="background-color: #d1e7dd;color: #0c4128;"><%=VALLOC_EXIT_QTY.elementAt(i) %></td>
										<td align="right"><%=VTRANSMISSION_QTY.elementAt(i)%></td>
										<td align="right"><%=VDAILY_IMBALANCE.elementAt(i) %></td>
										<td align="right"><%=VADJ_IMBALANCE.elementAt(i) %></td>
										<td align="right"><%=VCUMULATIVE_IMBALANCE.elementAt(i) %></td>
										<td align="right"><%=VDAILY_UNAUTHORIZED_OVERRUN.elementAt(i) %></td>
										<td align="right"><%=VCHARGEABLE_OVERRUN.elementAt(i)%></td>
										<td align="right"><%=VCHARGEABLE_POSITIVE_IMBALANCE.elementAt(i)%></td>
										<td align="right"><%=VCHARGEABLE_NEGETIVE_IMBALANCE.elementAt(i)%></td>	
									</tr>
									<%} %>
									<tr style="font-weight: bold;">
										<td align="right">Total :</td>
										<td align="right"><%=total_var_mdq%></td>
										<td align="right" style="background-color: #cfe2ff;color: #06357a;"><%=total_nom_entry_qty%></td>
										<td align="right" style="background-color: #fff3cd;color: #523e02;"><%=total_sch_entry_qty%></td>
										<td align="right" style="background-color: #d1e7dd;color: #0c4128;"><%=total_alloc_entry_qty%></td>
										<td align="right" style="background-color: #cfe2ff;color: #06357a;"><%=total_nom_exit_qty%></td>
										<td align="right" style="background-color: #fff3cd;color: #523e02;"><%=total_sch_exit_qty%></td>
										<td align="right" style="background-color: #d1e7dd;color: #0c4128;"><%=total_alloc_exit_qty%></td>
										<td align="right"><%=total_transmission_qty%></td>
										<td align="right">-</td>
										<td align="right">-</td>
										<td align="right">-</td>
										<td align="right">-</td>
										<td align="right"><%=total_chargeable_overrun%></td>
										<td align="right"><%=total_positive_imbalance%></td>
										<td align="right"><%=total_negitive_imbalance%></td>	
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<%}else{ %>
				<div class="card-body cdbody">
					<div align="center">
						<%if(contract_type.equals("C") && (customer_cd.equals("") || customer_cd.equals("0"))){ %>
							<%=utilmsg.infoMessage("<b>Select Customer!</b>") %>
						<%}else if(contract_type.equals("R") && (counterparty_cd.equals("") || counterparty_cd.equals("0"))){ %> 
							<%=utilmsg.infoMessage("<b>Select Transporter!</b>") %>
						<%}else{ %>
							<%=utilmsg.infoMessage("<b>Select GTC Contract!</b>") %>
						<%} %>
					</div>
				</div>
				<%} %>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="hid_from_dt" value="<%=from_dt %>">
<input type="hidden" name="hid_to_dt" value="<%=to_dt%>">

<input type="hidden" name="prev_counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="prev_customer_cd" value="<%=customer_cd%>">
<input type="hidden" name="prev_contract_type" value="<%=contract_type%>">

<input type="hidden" name="cont_start_dt" value="<%=start_dt%>">
<input type="hidden" name="cont_end_dt" value="<%=end_dt%>">

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