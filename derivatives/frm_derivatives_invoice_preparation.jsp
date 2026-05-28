<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var report_dt = document.forms[0].report_dt.value;
	var buy_sell = document.forms[0].buy_sell.value;
	var fy_year = document.forms[0].fy_year.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var inv_status_typ = document.forms[0].inv_status_typ.value;
	var plant_seq = document.forms[0].plant_seq.value;
	var bu_plant_seq = document.forms[0].bu_plant_seq.value;
	var financial_curve = document.forms[0].financial_curve.value;
	var msg="";
	var flag = true;
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "../derivatives/frm_derivatives_invoice_preparation.jsp?&u="+u+"&report_dt="+report_dt+"&plant_seq="+plant_seq+"&bu_plant_seq="+bu_plant_seq+
				"&buy_sell="+buy_sell+"&fy_year="+fy_year+"&counterparty_cd="+counterparty_cd+"&inv_status_typ="+inv_status_typ+"&financial_curve="+financial_curve;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg)
	}
}

function nextDate(day_no)
{
	//var clearance = document.forms[0].clearance.value;
	
	var dt = document.forms[0].report_dt.value;
	if(dt!="")
	{
	   	var split = dt.split("/");
		var d_dt = split[0];
		var m_dt = split[1];
		var y_dt = split[2];
		
		var dt1 = new Date(y_dt+"-"+m_dt+"-"+d_dt);
		if(day_no == "-1")
		{
			dt1.setDate(dt1.getDate()-1);
		}
		else
		{
			dt1.setDate(dt1.getDate()+1);
		}
		var day = dt1.getDate();
		if(parseInt(day) < 10)
		{
			day="0"+day;
		}
		var month = dt1.getMonth()+1;
		var year = dt1.getFullYear();
		if(parseInt(month) < 10)
		{
			month="0"+month;
		}
		var to_dt= day+"/"+month+"/"+year;
		
		document.forms[0].report_dt.value=to_dt;
		
		//refresh(clearance);
		refresh();
	}
}

var newWindow;
function prepare_inv(report_dt) 
{
	var u = document.forms[0].u.value;
    var checkboxes = document.getElementsByName("chk");
    var selCnt = 0;
    var flag = false;
    
    var year=document.forms[0].fy_year.value;
    var fy_year=year+"-"+(parseInt(year)+1);
    var split_dt=report_dt.split("/");
    var month=split_dt[1];

    var firstCounterpartyCd = "";
    var firstPlant = "";
    var firstBUPlant = "";
    var firstPriceCurve = "";
    var firstBuStateTin = "";
    
    var all_agmt_type = "";
    var all_agmt_no = "";
    var all_agmt_rev = "";
    var all_cont_no = "";
    var all_cont_rev = "";
    var all_cont_type = "";
    var all_instrument_no = "";
    var all_buy_sell = "";
    

    for (var i = 0; i < checkboxes.length; i++) 
    {
        if (checkboxes[i].checked) 
        {
            var currentCounterpartyCd = document.getElementById("countPty_cd" + i).value;
            var currentPlant = document.getElementById("countPty_plant" + i).value;
            var currentBUPlant = document.getElementById("bu_plant" + i).value;
            var currentPriceCurve = document.getElementById("price_curve" + i).value;
            var currentBuStateTin = document.getElementById("bu_state_tin" + i).value;

            if(all_cont_no=="")
	        {
            	all_agmt_type = document.getElementById("agmt_type" + i).value;
                all_agmt_no = document.getElementById("agmt_no" + i).value;
                all_agmt_rev = document.getElementById("agmt_rev" + i).value;
                all_cont_no = document.getElementById("cont_no" + i).value;
                all_cont_rev = document.getElementById("cont_rev" + i).value;
                all_cont_type = document.getElementById("cont_type" + i).value;
                all_instrument_no = document.getElementById("instrument_no" + i).value;
                all_buy_sell = document.getElementById("buySell" + i).value;
	        }
          	else
          	{
          		all_agmt_type = all_agmt_type+"@@"+document.getElementById("agmt_type" + i).value;
                all_agmt_no = all_agmt_no+"@@"+document.getElementById("agmt_no" + i).value;
                all_agmt_rev = all_agmt_rev+"@@"+document.getElementById("agmt_rev" + i).value;
                all_cont_no = all_cont_no+"@@"+document.getElementById("cont_no" + i).value;
                all_cont_rev = all_cont_rev+"@@"+document.getElementById("cont_rev" + i).value;
                all_cont_type = all_cont_type+"@@"+document.getElementById("cont_type" + i).value;
                all_instrument_no = all_instrument_no+"@@"+document.getElementById("instrument_no" + i).value;
                all_buy_sell = all_buy_sell+"@@"+document.getElementById("buySell" + i).value;
      		}
            if (selCnt === 0) 
            {
                firstCounterpartyCd = currentCounterpartyCd;
                firstPlant = currentPlant;
                firstBUPlant = currentBUPlant;
                firstPriceCurve = currentPriceCurve;
                firstBuStateTin = currentBuStateTin;
                
                flag = true;
            } 
            else 
            {
                if (currentCounterpartyCd !== firstCounterpartyCd) 
                {
                    alert("Please select deals with the same Counterparty only!");
                    return;
                }

                if (currentPlant !== firstPlant) 
                {
                    alert("Please select deals from the same Plant only!");
                    return;
                }

                if (currentBUPlant !== firstBUPlant) 
                {
                    alert("Please select deals from the same Business Unit only!");
                    return;
                }

                if (currentPriceCurve !== firstPriceCurve) 
                {
                    alert("Please select deals with the same Price Curve only!");
                    return;
                }
            }
            selCnt++;
        }
    }

    if (selCnt > 0 && flag && year!=0) 
    {
    	var a = confirm("Do you want to Generate Invoice ?");		
		if(a)
		{
			var url="../derivatives/frm_derv_inv_prep.jsp?u="+u+"&counterparty_cd="+firstCounterpartyCd+"&plant_seq="+firstPlant+"&bu_plant_seq="+currentBUPlant+"&agmt_type="+all_agmt_type+"&agmt_no="+all_agmt_no+"&agmt_rev="+all_agmt_rev+"&buy_sell="+all_buy_sell+
					"&cont_type="+all_cont_type+"&cont_no="+all_cont_no+"&cont_rev="+all_cont_rev+"&instrument_no="+all_instrument_no+"&year="+year+"&month="+month+"&fy_year="+fy_year+"&report_dt="+report_dt+"&financial_curve="+firstPriceCurve+"&bu_state_tin="+firstBuStateTin;
			//location.replace(url);
			
			if(!newWindow || newWindow.closed)
			{
				newWindow = window.open(url,"Generate Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
			}
			else
			{
				newWindow.close();
				newWindow = window.open(url,"Generate Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
			}
		}
    } 
    else if (selCnt === 0) 
    {
        alert("Select at least one Deal!");
    }
    else if	(year == 0)
   	{
    	alert("Please Select Valid Financial Year!");
   	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Invoice" id="derv_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String prevdate = utildate.getPreviousDate();
String sys_date_num = "0"; 
int currentYear = 0;
int currentMonth = 0;
String temp_billing_cycle="";
if(!sysdate.equals(""))
{
	String[] sys_temp = sysdate.split("/");
	sys_date_num=sys_temp[0];
}

String prvMonthDate="";
if(Integer.parseInt(sys_date_num) <= 15)
{
	temp_billing_cycle="2";
	prvMonthDate=utildate.getFirstDateOfPreviousMonth(sysdate);
}
else
{
	temp_billing_cycle="1";
	prvMonthDate=sysdate;
}

String date_num = "0"; 
if(!prvMonthDate.equals(""))
{
	String[] temp = prvMonthDate.split("/");
	date_num=temp[0];
	currentMonth=Integer.parseInt(temp[1]);
	currentYear=Integer.parseInt(temp[2]);
}

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String inv_status_typ=request.getParameter("inv_status_typ")==null?"0":request.getParameter("inv_status_typ");
String fy_year=request.getParameter("fy_year")==null?""+currentYear:request.getParameter("fy_year");
String buy_sell=request.getParameter("buy_sell")==null?"0":request.getParameter("buy_sell");
String report_dt=request.getParameter("report_dt")==null?prevdate:request.getParameter("report_dt");
String bu_plant_seq=request.getParameter("bu_plant_seq")==null?"0":request.getParameter("bu_plant_seq");
String plant_seq=request.getParameter("plant_seq")==null?"0":request.getParameter("plant_seq");
String financial_curve=request.getParameter("financial_curve")==null?"0":request.getParameter("financial_curve");

derv_inv.setCallFlag("DERIVATIVES_INVOICE_PREPARATION_LIST");
derv_inv.setComp_cd(owner_cd);
derv_inv.setInv_status_typ(inv_status_typ);
derv_inv.setCounterparty_cd(counterparty_cd);
derv_inv.setFy_year(fy_year);
derv_inv.setBuy_sell(buy_sell);
derv_inv.setReport_dt(report_dt);
derv_inv.setBu_plant_seq(bu_plant_seq);
derv_inv.setPlant_seq(plant_seq);
derv_inv.setFinancial_curve(financial_curve);
derv_inv.init();

String min_fy_year = derv_inv.getMin_fy_year();
String max_fy_year = derv_inv.getMax_fy_year();
String tot_commitment=derv_inv.getTot_commitment();
String tot_commitment_mmscm=derv_inv.getTot_commitment_mmscm();
String eod_procLastDt=derv_inv.getEod_procLastDt();

Vector VMST_COUNTERPARTY_CD = derv_inv.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = derv_inv.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = derv_inv.getVMST_COUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_CD = derv_inv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = derv_inv.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = derv_inv.getVCOUNTERPARTY_ABBR();
Vector VAGMT_TYPE = derv_inv.getVAGMT_TYPE();
Vector VAGMT_NO = derv_inv.getVAGMT_NO();
Vector VAGMT_REV = derv_inv.getVAGMT_REV();
Vector VCONT_TYPE = derv_inv.getVCONT_TYPE();
Vector VCONT_NO = derv_inv.getVCONT_NO();
Vector VCONT_REV = derv_inv.getVCONT_REV();
Vector VCONT_NAME = derv_inv.getVCONT_NAME();
Vector VCONT_REF = derv_inv.getVCONT_REF();
Vector VDEAL_MAPPING = derv_inv.getVDEAL_MAPPING();
Vector VINSTRUMENT_NO = derv_inv.getVINSTRUMENT_NO();
Vector VINSTRUMENT_TYPE = derv_inv.getVINSTRUMENT_TYPE();
Vector VBUY_SELL = derv_inv.getVBUY_SELL();
Vector VCARGO_ARRIVAL_DT = derv_inv.getVCARGO_ARRIVAL_DT();
Vector VBOOKED_MMBTU = derv_inv.getVBOOKED_MMBTU();
Vector VBOOKED_SCM = derv_inv.getVBOOKED_SCM();
Vector VQTY_UNIT = derv_inv.getVQTY_UNIT();
Vector VTRADE_DT = derv_inv.getVTRADE_DT();
Vector VRATE = derv_inv.getVRATE();
Vector VCONT_START_DT = derv_inv.getVCONT_START_DT();
Vector VCONT_END_DT = derv_inv.getVCONT_END_DT();
Vector VDEAL_PRICE_CURVE = derv_inv.getVDEAL_PRICE_CURVE();
Vector VDEAL_PROD_NM = derv_inv.getVDEAL_PROD_NM();
Vector VFLOAT_RATE = derv_inv.getVFLOAT_RATE();
Vector VINVGEN_FLAG = derv_inv.getVINVGEN_FLAG();
Vector VPLANT_SEQ = derv_inv.getVPLANT_SEQ();
Vector VPLANT_ABBR = derv_inv.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = derv_inv.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = derv_inv.getVBU_PLANT_ABBR();
Vector VPRICE_TYPE = derv_inv.getVPRICE_TYPE();
Vector VMST_PLANT_SEQ = derv_inv.getVMST_PLANT_SEQ();
Vector VMST_PLANT_ABBR = derv_inv.getVMST_PLANT_ABBR();
Vector VMST_BU_PLANT_SEQ = derv_inv.getVMST_BU_PLANT_SEQ();
Vector VMST_BU_PLANT_ABBR = derv_inv.getVMST_BU_PLANT_ABBR();
Vector VMST_CURVE_NM = derv_inv.getVMST_CURVE_NM();
Vector VBU_STATE_TIN = derv_inv.getVBU_STATE_TIN();
Vector VEXPOFREEZEESTATUS = derv_inv.getVEXPOFREEZEESTATUS();
Vector VEXPOFREEZEETOTALPNL = derv_inv.getVEXPOFREEZEETOTALPNL();
Vector VEXPOTOTALPNL = derv_inv.getVEXPOTOTALPNL();
Vector VINVOICE_NO = derv_inv.getVINVOICE_NO();
Vector VINVOICE_DT = derv_inv.getVINVOICE_DT();

if(fy_year.equals(max_fy_year))
{
	fy_year = Integer.parseInt(fy_year)-1+""; 
}

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//";
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;
%>
<body>
<%@ include file="../home/header.jsp"%>
<form action="">
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
					    	Derivatives Invoice Preparation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div align="center">
							<div class="col-sm-3 col-xs-3 col-md-3">
						  		<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>Report Date<span class="s-red">*</span></b></label>
									</div>
				    				<div class="col">
					      				<div class="input-group input-group-sm" >
					      					<span class="input-group-text" onclick="nextDate('-1');" title="click for Back Date"><i class="fa fa-backward fa-lg"></i></span>
						      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="report_dt" id="report_dt" value="<%=report_dt%>" maxLength="10" 
						      				onchange="validateDate(this);refresh();">
						      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      					<span class="input-group-text" onclick="nextDate('1');" title="click for Next Date"><i class="fa fa-forward fa-lg"></i></span>
					      				</div>
					    			</div>
					    		</div>
						  	</div>
					  	</div>
					</div>
					&nbsp;
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Financial Year</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="fy_year" onchange="refresh()">
										<option value="0" selected="selected">--Select--</option>
										<%for(int i=Integer.parseInt(max_fy_year)-1;i>=Integer.parseInt(min_fy_year); i--) {%> 			
							  				<option value="<%=i%>"><%=i%>-<%=i+1%></option>
						  				<%}%>		  		  
									</select>
									<script>document.forms[0].fy_year.value="<%=fy_year%>"</script>
								</div> 
							</div>
					  	</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0" selected="selected">--All--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
											<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %> 
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div> 
							</div>
						</div>
						<%-- <div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Invoice:</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="inv_status_typ" onchange="refresh()">
										<option value="0" selected="selected">--Select--</option>
										<option value="P">Pending</option>
										<option value="G">Generated</option>
									</select>
									<script>document.forms[0].inv_status_typ.value="<%=inv_status_typ%>"</script>
								</div> 
							</div>
						</div> --%>
						<input type="hidden" name="inv_status_typ" value="<%=inv_status_typ%>">
						
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Business Unit</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="bu_plant_seq" onchange="refresh()">
										<option value="0" selected="selected">--All--</option>
										<%for(int i=0; i<VMST_BU_PLANT_SEQ.size(); i++){ %>
										<option value="<%=VMST_BU_PLANT_SEQ.elementAt(i)%>"><%=VMST_BU_PLANT_ABBR.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].bu_plant_seq.value="<%=bu_plant_seq%>"</script>
								</div> 
							</div>
					  	</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Trader Plant</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="plant_seq" onchange="refresh()">
										<option value="0" selected="selected">--All--</option>
										<%for(int i=0; i<VMST_PLANT_SEQ.size(); i++){ %>
										<option value="<%=VMST_PLANT_SEQ.elementAt(i)%>"><%=VMST_PLANT_ABBR.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].plant_seq.value="<%=plant_seq%>"</script>
								</div> 
							</div>
						</div>
						<%-- <div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>BUY/SELL:</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="buy_sell" onchange="refresh()">
										<option value="0" selected="selected">--All--</option>
										<option value="BUY">BUY</option>
										<option value="SELL">SELL</option>
									</select>
									<script>document.forms[0].buy_sell.value="<%=buy_sell%>"</script>
								</div> 
							</div>
					  	</div> --%>
					  	<input type="hidden" name="buy_sell" value="<%=buy_sell%>">
						
					<!-- </div>
					<div class="row m-b-5"> -->
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Financial Curve</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="financial_curve" onchange="refresh()">
										<option value="0" selected="selected">--All--</option>
										<%for(int i=0; i<VMST_CURVE_NM.size(); i++){ %>
										<option value="<%=VMST_CURVE_NM.elementAt(i)%>"><%=VMST_CURVE_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].financial_curve.value="<%=financial_curve%>"</script>
								</div> 
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%if(write_access.equals("Y")){ %>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<i class="fa fa-cogs fa-2x" style="color: #008080; position: relative; left: -0.8em; " onclick="prepare_inv('<%=report_dt%>')" title="Prepare Invoice"></i>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div></div>
					</div>
					<%} %>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th><%if(write_access.equals("Y")){ %><%}else{ %>Sr#<%} %></th>		
											<th>Customer</th>
											<th>Contract No<br>[Contract/Trade Ref#]</th>
											<th>Contract Period</th>
											<th>Plant</th>
											<th>Business Unit</th>
											<th>Qty</th>
											<th>Qty Unit</th>
											<th>Trade Date</th>
											<th>Buy/Sell</th>					
											<th>Fixed<br>Price</th>
											<th>Price Type</th>
											<th>Float<br>Price</th>
											<th>Financial_Curve</th>
											<th>PnL<BR><%=eod_procLastDt %></th>
											<th>PnL<BR><%=report_dt %></th>
											<th>Invoice#</th>
											<th>Invoice<BR>ON</th>
										</tr>
									</thead>
									<tbody>
										<%int k=0;
										if(VCOUNTERPARTY_CD.size()>0){%>
											<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ 
											k++;
											%>
												<tr>
													<td align="center" <%if(VINVGEN_FLAG.elementAt(i).equals("G")){ %>style="background-color: var(--temp_data_highlight)" title="Invoice Generated"<%} %>>
														<%if(write_access.equals("Y")){ %>
															<input type="checkbox" class="form-check-input" name="chk" id="chk<%=i%>" <%if(VINVGEN_FLAG.elementAt(i).equals("G")){ %>title="Invoice Generated" disabled <%} %>>
														<%}else{%>
															<%=k %>
														<%} %>
													</td>
													<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i) %>
														<input type="hidden" class="form-check-input" name="countPty_cd" id="countPty_cd<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>">
													</td>
													<td align="center"><%=VDEAL_MAPPING.elementAt(i) %><br>[<%=VCONT_REF.elementAt(i)%>]
														<input type="hidden" class="form-check-input" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>">
														<input type="hidden" class="form-check-input" name="agmt_rev" id="agmt_rev<%=i%>" value="<%=VAGMT_REV.elementAt(i)%>">
														<input type="hidden" class="form-check-input" name="agmt_type" id="agmt_type<%=i%>" value="<%=VAGMT_TYPE.elementAt(i)%>">
														<input type="hidden" class="form-check-input" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>">
														<input type="hidden" class="form-check-input" name="cont_rev" id="cont_rev<%=i%>" value="<%=VCONT_REV.elementAt(i)%>">
														<input type="hidden" class="form-check-input" name="cont_type" id="cont_type<%=i%>" value="<%=VCONT_TYPE.elementAt(i)%>">
														<input type="hidden" class="form-check-input" name="instrument_no" id="instrument_no<%=i%>" value="<%=VINSTRUMENT_NO.elementAt(i)%>">
													</td>
													<td align="center"><%=VCONT_START_DT.elementAt(i) %>-<%=VCONT_END_DT.elementAt(i) %></td>
													<td align="center"><%=VPLANT_ABBR.elementAt(i) %>
														<input type="hidden" class="form-check-input" name="countPty_plant" id="countPty_plant<%=i%>" value="<%=VPLANT_SEQ.elementAt(i)%>">
													</td>
													<td align="center"><%=VBU_PLANT_ABBR.elementAt(i) %>
														<input type="hidden" class="form-check-input" name="bu_plant" id="bu_plant<%=i%>" value="<%=VBU_PLANT_SEQ.elementAt(i)%>">
														<input type="hidden" class="form-check-input" name="bu_state_tin" id="bu_state_tin<%=i%>" value="<%=VBU_STATE_TIN.elementAt(i)%>">
													</td>
													<td align="right"><%=VBOOKED_MMBTU.elementAt(i) %></td>
													<td align="center"><%=VQTY_UNIT.elementAt(i) %></td>
													<td align="center"><%=VTRADE_DT.elementAt(i) %></td>
													<td align="center"><%=VBUY_SELL.elementAt(i) %>
														<input type="hidden" class="form-check-input" name="buySell" id="buySell<%=i%>" value="<%=VBUY_SELL.elementAt(i)%>">
													</td>
													<td align="right"><%=VRATE.elementAt(i) %></td>
													<td align="center"><%=VPRICE_TYPE.elementAt(i) %></td>
													<td align="right"><%=VFLOAT_RATE.elementAt(i) %></td>
													<td align="center"><%=VDEAL_PRICE_CURVE.elementAt(i) %>
														<input type="hidden" class="form-check-input" name="price_curve" id="price_curve<%=i%>" value="<%=VDEAL_PRICE_CURVE.elementAt(i)%>">
													</td>
													<%if(VEXPOFREEZEETOTALPNL.size()>0) { %>
								 						<%if(!VEXPOFREEZEETOTALPNL.elementAt(i).equals("") && Double.parseDouble(""+VEXPOFREEZEETOTALPNL.elementAt(i))>=0) { %>	
													       <td align="right"><font color="green"><%=VEXPOFREEZEETOTALPNL.elementAt(i)%></font></td>	
													    <%} else if(!VEXPOFREEZEETOTALPNL.elementAt(i).equals("") && Double.parseDouble(""+VEXPOFREEZEETOTALPNL.elementAt(i))<0){ %>
													       <td align="right"><font color="red"><%=VEXPOFREEZEETOTALPNL.elementAt(i)%></font></td>	
													    <%} else {%>	
													    	<td align="right"><%=VEXPOFREEZEETOTALPNL.elementAt(i)%></td>	
													    <%} %>
													<% } else {%>	
													    <td align="center"></td>
													<%} %>
													<%if(!VEXPOTOTALPNL.elementAt(i).equals("") && Double.parseDouble(""+VEXPOTOTALPNL.elementAt(i))>=0) { %>	
												     	<td align="right"><font color="green"><%=VEXPOTOTALPNL.elementAt(i)%></font></td>	
												    <%} else if(!VEXPOTOTALPNL.elementAt(i).equals("") && Double.parseDouble(""+VEXPOTOTALPNL.elementAt(i))<0){ %>
												     	<td align="right"><font color="red"><%=VEXPOTOTALPNL.elementAt(i)%></font></td>		
												    <%} else {%>	
												    	<td align="right"><%=VEXPOTOTALPNL.elementAt(i)%></td>
												    <%} %>
													<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
													<td align="center">Settle Date</td>
												</tr>
											<%} %>
										<%}else{ %>
											<tr>
												<td align="center" colspan="18">
													<%=utilmsg.infoMessage("<b>No deals Available!</b>") %>
												</td>
											</tr>
										<%} %>
									</tbody>
								</table>&nbsp;
								<div align="center">
									<%=utilmsg.infoMessage("<b>Merging Invoice enabled for Same Counterparty, Same Plant, Same Business Unit and Same Financial Curve Only!</b>") %>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="sysdate" value="<%=sysdate%>">

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