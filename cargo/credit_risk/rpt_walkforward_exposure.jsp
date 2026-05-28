<%@page import="java.io.PrintWriter"%>
<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script src="../js/chart_js/chart.js"></script>
<script>

function refresh(clearance)
{
	var prev_clearance = document.forms[0].prev_clearance.value;
	var report_days = document.forms[0].report_days.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	if(prev_clearance != clearance)
	{
		counterparty_cd = "0";
		report_days = "21";
	}

	var u = document.forms[0].u.value;
	
	if(report_days >= 21 && report_days <=365)
	{
	
			var url = "rpt_walkforward_exposure.jsp?u="+u+"&report_days="+report_days+"&clearance="+clearance+"&counterparty_cd="+counterparty_cd;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert("Please Enter #Days between 21 and 365 !!");
		document.forms[0].report_days.value = "21";
		return false;
	}
}

function showTableRow()
{
	var expand_val = document.getElementById("expand").className;
	if(expand_val == "fa fa-expand")
	{
		var ele = document.getElementsByClassName("body");
		for (var i = 0; i < ele.length; i++)
		{
			ele[i].style.display = "table-row-group";
		}
		document.getElementById("expand").className="fa fa-compress";
	}
	else
	{
		var ele = document.getElementsByClassName("body");
		for (var i = 0; i < ele.length; i++)
		{
        	ele[i].style.display = "none";
		}
		document.getElementById("expand").className='fa fa-expand';
	}	
}

function showTableRow1()
{
	var expand_val = document.getElementById("expand1").className;
	if(expand_val == "fa fa-expand")
	{
		var ele = document.getElementsByClassName("body1");
		for (var i = 0; i < ele.length; i++)
		{
			ele[i].style.display = "table-row-group";
		}
		document.getElementById("expand1").className="fa fa-compress";
	}
	else
	{
		var ele = document.getElementsByClassName("body1");
		for (var i = 0; i < ele.length; i++)
		{
        	ele[i].style.display = "none";
		}
		document.getElementById("expand1").className='fa fa-expand';
	}	
}

function showTableRow2()
{
	var expand_val = document.getElementById("expand2").className;
	if(expand_val == "fa fa-expand")
	{
		var ele = document.getElementsByClassName("body2");
		for (var i = 0; i < ele.length; i++)
		{
			ele[i].style.display = "table-row-group";
		}
		document.getElementById("expand2").className="fa fa-compress";
	}
	else
	{
		var ele = document.getElementsByClassName("body2");
		for (var i = 0; i < ele.length; i++)
		{
        	ele[i].style.display = "none";
		}
		document.getElementById("expand2").className='fa fa-expand';
	}	
}

var newWindow;
function showDetails(clearance,from_dt,report_days,counterparty_cd,type,cust_nm,deal_id,total,prev_total)
{
	url="rpt_walkforward_expo_details.jsp?clearance="+clearance+"&from_dt="+from_dt+"&report_days="+report_days+"&counterparty_cd="+counterparty_cd+"&type="+type+"&cust_nm="+cust_nm+"&deal_id="+deal_id+"&total="+total+"&prev_total="+prev_total;
	if(!newWindow || newWindow.closed)
	{
		newWindow= window.open(url,"rpt_walkforward_expo_detail_list","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
	    newWindow= window.open(url,"rpt_walkforward_expo_detail_list","top=10,left=10,width=1200,height=600,scrollbars=1");
	}
}

</script>

</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CreditRisk_Report" id="db_walkforward" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String prev_date=utildate.getPreviousDate();
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt = request.getParameter("from_dt")==null?prev_date:request.getParameter("from_dt");
String clearance = request.getParameter("clearance")==null?"K":request.getParameter("clearance");
String report_days  = request.getParameter("report_days")==null?"21":request.getParameter("report_days");
String sell_buy  = request.getParameter("sell_buy")==null?"Sell":request.getParameter("sell_buy");

db_walkforward.setCallFlag("WALKFORWARD_EXPOSURE_REPORT");
db_walkforward.setClearance(clearance);
db_walkforward.setComp_cd(owner_cd);
db_walkforward.setFrom_dt(from_dt);
db_walkforward.setReport_days(report_days);
db_walkforward.setCounterparty_cd(counterparty_cd);
db_walkforward.init();

Vector VMST_COUNTERPARTY_CD = db_walkforward.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = db_walkforward.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = db_walkforward.getVMST_COUNTERPARTY_ABBR();

Vector VEXPOSURE_DT = db_walkforward.getVEXPOSURE_DT();
Vector VOPENEXPOSURE = db_walkforward.getVOPENEXPOSURE();
Vector VPARENT_CD = db_walkforward.getVPARENT_CD();
Vector VLIMIT_CALCULATION = db_walkforward.getVLIMIT_CALCULATION();
Vector VCOLOR = db_walkforward.getVCOLOR();

Vector VDEAL_ID = db_walkforward.getVDEAL_ID();
Vector VBUYSELL = db_walkforward.getVBUYSELL();
Vector VSETTLEMENTSELLCREDIT = db_walkforward.getVSETTLEMENTSELLCREDIT();
Vector VAVAILABLECREDIT = db_walkforward.getVAVAILABLECREDIT();
Vector VCUSTWISESETTLEMENTSELLCREDITFORPREVDT = db_walkforward.getVCUSTWISESETTLEMENTSELLCREDITFORPREVDT();
Vector VCUSTWISESETTLEMENTSELLCREDIT = db_walkforward.getVCUSTWISESETTLEMENTSELLCREDIT();
Vector VINFO = db_walkforward.getVINFO();
Vector VPARENT_NM = db_walkforward.getVPARENT_NM();
Vector VPARENT_LIMIT = db_walkforward.getVPARENT_LIMIT();
Vector VSEC_DEAL_ID = db_walkforward.getVSEC_DEAL_ID();
Vector VSEC_COUNTERPARTY_CD = db_walkforward.getVSEC_COUNTERPARTY_CD();
Vector VSEC_EXCHGRATE = db_walkforward.getVSEC_EXCHGRATE();
Vector VSEC_TAXRATE = db_walkforward.getVSEC_TAXRATE();
Vector VSEC_BUYSELL = db_walkforward.getVSEC_BUYSELL();
Vector VSEC_TRANSVALUE = db_walkforward.getVSEC_TRANSVALUE();
Vector VSEC_RATE = db_walkforward.getVSEC_RATE();
Vector VSEC_RATE_UNIT = db_walkforward.getVSEC_RATE_UNIT();
Vector VSETTLEMENTSELLEXPOSUR = db_walkforward.getVSETTLEMENTSELLEXPOSUR();
Vector VSEC_COUNTERPARTY_NM = db_walkforward.getVSEC_COUNTERPARTY_NM();
Vector VCUSTWISESETTLEMENTSELLEXPOSUREFORPREVDT = db_walkforward.getVCUSTWISESETTLEMENTSELLEXPOSUREFORPREVDT();
Vector VCUSTWISESETTLEMENTSELLEXPOSURE = db_walkforward.getVCUSTWISESETTLEMENTSELLEXPOSURE();
Vector VINFO1 = db_walkforward.getVINFO1();
Vector VCOUNTERPARTY_NM = db_walkforward.getVCOUNTERPARTY_NM();
Vector VSEC_DISP_DEAL_ID = db_walkforward.getVSEC_DISP_DEAL_ID();
Vector VDISP_DEAL_ID = db_walkforward.getVDISP_DEAL_ID();

%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="">
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
				    		WalkForward Exposure Report 
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<div class="btn-group" >
											<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("K")){%>btnactive<%}%>" onclick="refresh('K')">KYC</label>
											<label class="btn btn-outline-secondary subbtngrp1 <%if(clearance.equals("I")){%>btnactive<%}%>" onclick="refresh('I')">IGX</label>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>	
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Counterparty</b></label>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
				    			<div class="col">
				      				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=clearance%>')">
										<option value="0" lable="0" selected="selected">--Select--</option>
											<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
												<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
											<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">
							<label class="form-label"><b>#Days</b></label>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">
							<div class="form-group row">
				    			<div class="col">
				      				<input type="text" class="form-control form-control-sm" name="report_days" value="<%=report_days %>" onblur="refresh('<%=clearance%>')">
									<script>document.forms[0].report_days.value="<%=report_days%>"</script>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<div class="col">
				      				<span style="color:red;">*<I>(Min 21 Days - Max 365 Days)</I></span>
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%if(!counterparty_cd.equals("0"))
				{%>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>Report Date</b></label>
									</div>
									<div class="col-auto">
					      				<div class="input-group input-group-sm" >
				      				   		<input type="text" class="form-control form-control-sm" name="from_dt" value="<%=from_dt %>" readonly>
			      						</div>
					    			</div>
								</div>
							</div>
						</div>
					</div>
				
					&nbsp;
					<div class="table-responsive">
						<table class="table table-bordered">
							<tr>
								<td>
									<div align="center">
										<canvas id="myChart" style="width:100%;max-width:1000px"></canvas>
									</div>
								</td>
							</tr>
						</table>
					</div>&nbsp;
					<div class="table-responsive">
						<table class="table table-bordered">
							<thead>
								<tr>
									<th></th>
									<th colspan="3" align="center">Exposure Type</th>
									<% for(int i=0; i<VEXPOSURE_DT.size(); i++){%>
										<th><div align="center"><%=VEXPOSURE_DT.elementAt(i) %></div></th>
									<%}%>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td align="right" valign="middle"><i id="expand" class="fa fa-expand" onclick="showTableRow()"></i></td>
									<td colspan="3"><b>Limit (INR)</b></td>
									<%for(int i=0; i < VLIMIT_CALCULATION.size(); i++){ %>
									<td><div align="right"><%=VLIMIT_CALCULATION.elementAt(i)%></div></td>
									<%} %>
								</tr>
								<tbody class="body" id="body" style="display:none;">
									<tr style="text-align:center;font-weight:bold;background:#bce6ff;color:#0c63e4;">
										<th style="background:white;"></th>
										<th>Exposure Type</th>
										<th colspan="2">Parent Name(LE)</th>
										<%for(int i=0; i < VEXPOSURE_DT.size(); i++){ %>
										<th><div align="center"><%=VEXPOSURE_DT.elementAt(i) %></div></th>
										<%}%>
									</tr>		
									<%int m=-1; int n =0; int size1 = (int)VEXPOSURE_DT.size();
									for(int i=0; i < VPARENT_NM.size(); i++)
									{ %>
									<tr>
										<td></td>
										<td><div align="center">Limit</div></td>
										<td colspan="2"><div align="center"><%=VPARENT_NM.elementAt(i)%></div></td>
										<%for(n=n; n < VPARENT_LIMIT.size(); n++){ 
											m=m+1;
											if(m==size1)
											{
												break;
											}
											else
											{%>
											<td><div align="right"><%=VPARENT_LIMIT.elementAt(n)%></div></td>
											<%}
										}%>
									</tr>
									<%}%>
									<tr><td colspan="<%=VEXPOSURE_DT.size()+4%>"></td></tr>
								</tbody>
								<tr>
									<td align="right" valign="middle"><i id="expand1" class="fa fa-expand" onclick="showTableRow1()"></i></td>
									<td colspan="3"><b>Settlement Sell Open Credit (INR) <font color='blue'>(Cumulative)</font></b></td>
									<%for(int i=0; i < VSETTLEMENTSELLCREDIT.size(); i++){ %>
									<td><div align="right"><%=VSETTLEMENTSELLCREDIT.elementAt(i)%></div></td>
									<%}%>
								</tr>
								<tbody class="body1" id="body1" style="display:none;">
									<tr style="text-align:center;font-weight:bold;background:#bce6ff;color:#0c63e4;">
										<td style="background:white;"></td>
										<th>Customer Name</th>
										<th>Deal No#</th>
										<th>Buy/Sell</th>
										<%for(int i=0; i < VEXPOSURE_DT.size(); i++){ %>
										<th><div align="center"><%=VEXPOSURE_DT.elementAt(i) %></div></th>
										<%}%>
									</tr>
												
									<%int k=-1; int j =0; int size = (int)VEXPOSURE_DT.size();
									for(int i=0; i < VDEAL_ID.size(); i++){ %>
									<tr>
										<td></td>
										<td><div align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></div></td>
										<% k=-1;
											String originalDealId = "";
											if(!VDEAL_ID.elementAt(i).equals(""))
											{
												originalDealId=""+VDISP_DEAL_ID.elementAt(i); 
											}
										%>
										<td><div align="center"><%=originalDealId%></div></td>
										<td><div align="center"><%=VBUYSELL.elementAt(i)%></div></td>
										<%for(j=j; j < VCUSTWISESETTLEMENTSELLCREDIT.size(); j++){ 
											k=k+1;
											if(k==size)
											{
												break;
											}
											else
											{%>
												<td title="Click to view detail" onclick="showDetails('<%=clearance%>','<%=from_dt%>','<%=report_days%>','<%=counterparty_cd%>','OPEN_CREDIT',&quot;<%=VCOUNTERPARTY_NM.elementAt(i)%>&quot;,'<%=originalDealId%>','<%=VCUSTWISESETTLEMENTSELLCREDIT.elementAt(j)%>','<%=VCUSTWISESETTLEMENTSELLCREDITFORPREVDT.elementAt(j)%>')">
												<div align="right"><%=VCUSTWISESETTLEMENTSELLCREDIT.elementAt(j)%></div></td>
											<%}
										}%>
									</tr>
									<%}%>
									<tr><td colspan="<%=VEXPOSURE_DT.size()+4%>"></td></tr>		
								</tbody>
								<tr>
									<td align="right" valign="middle"><i id="expand2" class="fa fa-expand" onclick="showTableRow2()"></i></td>
									<td colspan="3"><b>Settlement Sell Exposures (INR)</b></td>
									<%for(int i=0; i < VSETTLEMENTSELLEXPOSUR.size(); i++){ %>
									<td><div align="right"><%=VSETTLEMENTSELLEXPOSUR.elementAt(i)%></div></td>
									<%}%>
								</tr>
								<tbody class="body2" id="body2" style="display:none;">
									<tr style="text-align:center;font-weight:bold;background:#bce6ff;color:#0c63e4;">
										<th style="background:white;"></th>
										<th>Customer Name</th>
										<th>Deal No#</th>
										<th>Buy/Sell</th>
										<%for(int i=0; i < VEXPOSURE_DT.size(); i++){ %>
										<th><div align="center"><%=VEXPOSURE_DT.elementAt(i) %></div></th>
										<%}%>
									</tr>
											
									<%int f=-1; int z =0; int size_1 = (int)VEXPOSURE_DT.size();
									for(int i=0; i < VSEC_DEAL_ID.size(); i++){ %>
									<tr>
										<td></td>
										<td><div align="left"><%=VSEC_COUNTERPARTY_NM.elementAt(i)%></div></td>
										<% f=-1;
											String originalDealId = "";
											if(!VSEC_DEAL_ID.elementAt(i).equals(""))
											{
												originalDealId = ""+VSEC_DISP_DEAL_ID.elementAt(i);
											}
										%>
										<td><div align="center"><%=originalDealId%></div></td>
										<td><div align="center"><%=VSEC_BUYSELL.elementAt(i)%></div></td>
										<%for(z=z; z < VCUSTWISESETTLEMENTSELLEXPOSURE.size(); z++){ 
											f=f+1;
											if(f==size_1)
											{
												break;
											}
											else
											{
											%>
												<td title="Click to view detail" onclick="showDetails('<%=clearance%>','<%=from_dt%>','<%=report_days%>','<%=counterparty_cd%>','EXPOSURE',&quot;<%=VSEC_COUNTERPARTY_NM.elementAt(i)%>&quot;,'<%=originalDealId%>','<%=VCUSTWISESETTLEMENTSELLEXPOSURE.elementAt(z)%>','<%=VCUSTWISESETTLEMENTSELLEXPOSUREFORPREVDT.elementAt(z)%>')">
												<div align="right"><%=VCUSTWISESETTLEMENTSELLEXPOSURE.elementAt(z)%></div></td>
											<%}
										}%>
									</tr>
									<%}%> 
									<tr><td colspan="<%=VEXPOSURE_DT.size()+4%>"></td></tr>
								</tbody>
								<tr>
									<td></td>
									<td colspan="3"><b>Available Credit (INR)</b></td>
									<%for(int i=0; i < VAVAILABLECREDIT.size(); i++){ %>
									<td><div align="right"><%=VAVAILABLECREDIT.elementAt(i)%></div></td>
									<%} %>
								<tr>
							</tbody>
						</table>
					</div>
					<%}else{ %>
						<div align="center"><%=utilmsg.infoMessage("<b>Please Select any Counterparty!</b>") %></div>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="prev_clearance" value="<%=clearance%>">
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

<script type="text/javascript">

var xValues = [100,200,300,400,500,600,700,800,900,1000];
var xValues = [];
var settlement_expo = [];
var limit = [];


<%for (int i = 0; i < VEXPOSURE_DT.size(); i++) {
	String temp = VEXPOSURE_DT.elementAt(i).toString().replaceAll("/", "");
	%>

	xValues.push(<%=temp%>);
	settlement_expo.push(<%=VSETTLEMENTSELLCREDIT.elementAt(i)%>);
	limit.push(<%=VLIMIT_CALCULATION.elementAt(i)%>);
 
<%}%>

const dataLine = 
{
type: 'line',
  data: {
    labels: xValues,
    datasets: [{
        label: "limit",
        data: limit,
        backgroundColor: [
          //'rgba(105, 0, 132, 0.2)',
        	'rgba(0, 137, 132, 0.2)',
        ],
        fill: true,
        borderColor: [
          //'rgba(255, 99, 132, 0.8)',
        	'rgba(50, 150, 255, 1)',
        ],
        borderWidth: 2,
        tension: 0.4
      },
      {
        label: "Settlement Exposure",
        data: settlement_expo,
        backgroundColor: [
        	'rgba(105, 0, 132, 0.2)',
        ],
        fill: true,
        borderColor: [
        	'rgba(255, 99, 132, 0.8)',
        ],
        borderWidth: 2,
        tension: 0.4
      }
    ]
  },
 	 options: {
     responsive: true
  }
};

new Chart(document.getElementById('myChart'), dataLine);

</script>
</html>