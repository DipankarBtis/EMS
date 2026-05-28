
<%@page import="java.util.*"%>
<%@page import= "java.text.NumberFormat"%>
<%@page import= "java.text.DecimalFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script src="../js/chart_js/chart.js"></script>
<script>

function refresh()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var filter_security_type = document.forms[0].filter_security_type.value;
	
	var flag=true;
	var msg="";
	
	var count = compareDate(from_dt,to_dt);
	if(parseInt(count) == 1)
	{
		msg+="From Date should be less or equal To Date!";
		flag=false;
	}
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "rpt_coll_sec_kpi.jsp?u="+u+"&filter_security_type="+filter_security_type+
			"&from_dt="+from_dt+"&to_dt="+to_dt;

		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
		document.forms[0].from_dt.value="";
	}
}
function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var filter_security_type = document.forms[0].filter_security_type.value;
	
	var url = "xls_coll_sec_kpi.jsp?fileName=Collateral/Security KPI Report.xls&from_dt="+from_dt+"&to_dt="+to_dt+"&filter_security_type="+filter_security_type;

	location.replace(url);
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CR_ReceivableReport" id="dbcredit" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
NumberFormat nf = new DecimalFormat("###########0.00");
NumberFormat nf2 = new DecimalFormat("###,###,###,##0.00");

String sysdate=utildate.getSysdate();
int curr_year=utildate.getCurrentYear();
String from_dt = request.getParameter("from_dt")==null?"01/01/"+curr_year:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?"31/12/"+curr_year:request.getParameter("to_dt");
String filter_security_type = request.getParameter("filter_security_type")==null?"0":request.getParameter("filter_security_type");

dbcredit.setCallFlag("KPI_REPORT");
dbcredit.setComp_cd(owner_cd);
dbcredit.setFilter_security_type(filter_security_type);
dbcredit.setFrom_dt(from_dt);
dbcredit.setTo_dt(to_dt);
dbcredit.init();

Vector VCOUNTERPARTY_NM = dbcredit.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcredit.getVCOUNTERPARTY_ABBR();
Vector VSEC_CATEGORY = dbcredit.getVSEC_CATEGORY();
Vector VSEC_TYPE = dbcredit.getVSEC_TYPE();
Vector VSEC_REF_NO = dbcredit.getVSEC_REF_NO();
Vector VVALUE = dbcredit.getVVALUE();
Vector VRECEIVED_DATE = dbcredit.getVRECEIVED_DATE();
Vector VMST_BANK_NM = dbcredit.getVMST_BANK_NM();
Vector VMST_BANK_CD = dbcredit.getVMST_BANK_CD();
Vector VMST_BANK_ABBR = dbcredit.getVMST_BANK_ABBR();
Vector VMST_BRANCH_NAME = dbcredit.getVMST_BRANCH_NAME();
Vector VDEAL_NO = dbcredit.getVDEAL_NO();
Vector VISS_BANK_NM = dbcredit.getVISS_BANK_NM();
Vector VDEAL_TYPE = dbcredit.getVDEAL_TYPE();
Vector VISS_BANK_REF = dbcredit.getVISS_BANK_REF();
Vector VADV_BANK_NM = dbcredit.getVADV_BANK_NM();
Vector VADV_BANK_REF = dbcredit.getVADV_BANK_REF();
Vector VCONFIRM_BANK_NM = dbcredit.getVCONFIRM_BANK_NM();
Vector VCONFIRM_BANK_REF = dbcredit.getVCONFIRM_BANK_REF();
Vector VADV_BANK_CD = dbcredit.getVADV_BANK_CD();
Vector VISS_BANK_CD = dbcredit.getVISS_BANK_CD();
Vector VCONFIRM_BANK_CD = dbcredit.getVCONFIRM_BANK_CD();
Vector VCANCEL_DT = dbcredit.getVCANCEL_DT();
Vector VEXP_VAL = dbcredit.getVEXP_VAL();
Vector VFROM_DT = dbcredit.getVFROM_DT();
Vector VTO_DT = dbcredit.getVTO_DT();
Vector VISSUE_DT = dbcredit.getVISSUE_DT();
Vector VEXPIRE_DT = dbcredit.getVEXPIRE_DT();
Vector VREMARK = dbcredit.getVREMARK();
Vector VSTATUS = dbcredit.getVSTATUS();
Vector VVALUE_USD = dbcredit.getVVALUE_USD();
Vector VEXCHANGE_RATE = dbcredit.getVEXCHANGE_RATE();
Vector VLEGAL_ENTITY = dbcredit.getVLEGAL_ENTITY();
Vector VDIS_CONTRACT_TYPE = dbcredit.getVDIS_CONTRACT_TYPE();

String inLCConfCount = dbcredit.getInLCConfCount();
String inLCConfAmt = dbcredit.getInLCConfAmt();
String inLCConfAmtUsd = dbcredit.getInLCConfAmtUsd();
String inLCCanCount = dbcredit.getInLCCanCount();
String inLCCanAmt = dbcredit.getInLCCanAmt();
String inLCCanAmtUsd = dbcredit.getInLCCanAmtUsd();
String inLCAdvCount = dbcredit.getInLCAdvCount();
String inLCAdvAmt = dbcredit.getInLCAdvAmt();
String inLCAdvAmtUsd = dbcredit.getInLCAdvAmtUsd();
String inBGConfCount = dbcredit.getInBGConfCount();
String inBGConfAmt = dbcredit.getInBGConfAmt();
String inBGConfAmtUsd = dbcredit.getInBGConfAmtUsd();
String inBGCanCount = dbcredit.getInBGCanCount();
String inBGCanAmt = dbcredit.getInBGCanAmt();
String inBGCanAmtUsd = dbcredit.getInBGCanAmtUsd();
String inBGAdvCount = dbcredit.getInBGAdvCount();
String inBGAdvAmt = dbcredit.getInBGAdvAmt();
String inBGAdvAmtUsd = dbcredit.getInBGAdvAmtUsd();
String inPCGConfCount = dbcredit.getInPCGConfCount();
String inPCGConfAmt = dbcredit.getInPCGConfAmt();
String inPCGConfAmtUsd = dbcredit.getInPCGConfAmtUsd();
String inPCGCanCount = dbcredit.getInPCGCanCount();
String inPCGCanAmt = dbcredit.getInPCGCanAmt();
String inPCGCanAmtUsd = dbcredit.getInPCGCanAmtUsd();
String inPCGAdvCount = dbcredit.getInPCGAdvCount();
String inPCGAdvAmt = dbcredit.getInPCGAdvAmt();
String inPCGAdvAmtUsd = dbcredit.getInPCGAdvAmtUsd();

String outLCConfCount = dbcredit.getOutLCConfCount();
String outLCConfAmt = dbcredit.getOutLCConfAmt();
String outLCConfAmtUsd = dbcredit.getOutLCConfAmtUsd();
String outLCCanCount = dbcredit.getOutLCCanCount();
String outLCCanAmt = dbcredit.getOutLCCanAmt();
String outLCCanAmtUsd = dbcredit.getOutLCCanAmtUsd();
String outLCAdvCount = dbcredit.getOutLCAdvCount();
String outLCAdvAmt = dbcredit.getOutLCAdvAmt();
String outLCAdvAmtUsd = dbcredit.getOutLCAdvAmtUsd();
String outBGConfCount = dbcredit.getOutBGConfCount();
String outBGConfAmt = dbcredit.getOutBGConfAmt();
String outBGConfAmtUsd = dbcredit.getOutBGConfAmtUsd();
String outBGCanCount = dbcredit.getOutBGCanCount();
String outBGCanAmt = dbcredit.getOutBGCanAmt();
String outBGCanAmtUsd = dbcredit.getOutBGCanAmtUsd();
String outBGAdvCount = dbcredit.getOutBGAdvCount();
String outBGAdvAmt = dbcredit.getOutBGAdvAmt();
String outBGAdvAmtUsd = dbcredit.getOutBGAdvAmtUsd();
String outPCGConfCount = dbcredit.getOutPCGConfCount();
String outPCGConfAmt = dbcredit.getOutPCGConfAmt();
String outPCGConfAmtUsd = dbcredit.getOutPCGConfAmtUsd();
String outPCGCanCount = dbcredit.getOutPCGCanCount();
String outPCGCanAmt = dbcredit.getOutPCGCanAmt();
String outPCGCanAmtUsd = dbcredit.getOutPCGCanAmtUsd();
String outPCGAdvCount = dbcredit.getOutPCGAdvCount();
String outPCGAdvAmt = dbcredit.getOutPCGAdvAmt();
String outPCGAdvAmtUsd = dbcredit.getOutPCGAdvAmtUsd();
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
				    		Collateral/Security KPI Report
	   	 				</div>
	   	 				<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>From</b></label>
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
						</div>
					</div>	
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-6 col-xs-6 col-md-6">
							<div class="card cardmain">
								<div class="card-header cdheader">
									 <div class="d-flex justify-content-between">
										<div class="topheader">
											Incoming Security Summary
										</div>
									</div>
								</div>
								<div class="card-body cdbody">
									<div class="table-responsive">
										<table class="table table-bordered table-hover">
											<thead>
												<tr>
													<th ></th>
													<th  colspan="3">Advised</th>
													<th  colspan="3">Confirmed</th>
													<th  colspan="3">Cancelled</th>
												</tr>
												<tr bgcolor="">
													<th></th>
													<th style="background-color: #000066; color: white">Count</th>
													<th>Value(INR)</th>
													<th>Value(USD)</th>
													<th style="background-color: #000066; color: white">Count</th>
													<th>Value(INR)</th>
													<th>Value(USD)</th>
													<th style="background-color: #000066; color: white">Count</th>
													<th>Value(INR)</th>
													<th>Value(USD)</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td align="center" style="background-color: "><b>LC</b></td>
													<td style="background-color: #b3f0ff" align="center"><%=inLCAdvCount%></td>
													<td align="right"><%=inLCAdvAmt %></td>
													<td align="right"><%=inLCAdvAmtUsd %></td>
													<td style="background-color: #b3f0ff" align="right"><%=inLCConfCount %></td>
													<td align="right"><%=inLCConfAmt %></td>
													<td align="right"><%=inLCConfAmtUsd %></td>
													<td style="background-color: #b3f0ff" align="right"><%=inLCCanCount %></td>
													<td align="right"><%=inLCCanAmt %></td>
													<td align="right"><%=inLCCanAmtUsd %></td>
												</tr>
												<tr>
													<td align="center" style="background-color: "><b>BG</b></td>
													<td style="background-color: #b3f0ff" align="center"><%=inBGAdvCount%></td>
													<td align="right"><%=inBGAdvAmt %></td>
													<td align="right"><%=inBGAdvAmtUsd %></td>
													<td style="background-color: #b3f0ff" align="right"><%=inBGConfCount %></td>
													<td align="right"><%=inBGConfAmt %></td>
													<td align="right"><%=inBGConfAmtUsd %></td>
													<td style="background-color: #b3f0ff" align="right"><%=inBGCanCount %></td>
													<td align="right"><%=inBGCanAmt %></td>
													<td align="right"><%=inBGCanAmtUsd %></td>
												</tr>
												<tr>
													<td align="center" style="background-color: "><b>PCG</b></td>
													<td style="background-color: #b3f0ff" align="center"><%=inPCGAdvCount%></td>
													<td align="right"><%=inPCGAdvAmt %></td>
													<td align="right"><%=inPCGAdvAmtUsd %></td>
													<td style="background-color: #b3f0ff" align="right"><%=inPCGConfCount %></td>
													<td align="right"><%=inPCGConfAmt %></td>
													<td align="right"><%=inPCGConfAmtUsd %></td>
													<td style="background-color: #b3f0ff" align="right"><%=inPCGCanCount %></td>
													<td align="right"><%=inPCGCanAmt %></td>
													<td align="right"><%=inPCGCanAmtUsd %></td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-xs-6 col-md-6">
							<div class="card cardmain">
								<div class="card-header cdheader">
									 <div class="d-flex justify-content-between">
										<div class="topheader">
											Outgoing Security Summary
										</div>
									</div>
								</div>
								<div class="card-body cdbody">
									<div class="table-responsive">
										<table class="table table-bordered table-hover">
											<thead>
												<tr>
													<th ></th>
													<th  colspan="3">Advised</th>
													<th  colspan="3">Confirmed</th>
													<th  colspan="3">Cancelled</th>
												</tr>
												<tr bgcolor="">
													<th></th>
													<th style="background-color: #000066; color: white" >Count</th>
													<th>Value(INR)</th>
													<th>Value(USD)</th>
													<th style="background-color: #000066; color: white">Count</th>
													<th>Value(INR)</th>
													<th>Value(USD)</th>
													<th style="background-color: #000066; color: white">Count</th>
													<th>Value(INR)</th>
													<th>Value(USD)</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td align="center" style="background-color: "><b>LC</b></td>
													<td style="background-color: #b3f0ff" align="center"><%=outLCAdvCount%></td>
													<td align="right"><%=outLCAdvAmt %></td>
													<td align="right"><%=outLCAdvAmtUsd %></td>
													<td style="background-color: #b3f0ff"align="right"><%=outLCConfCount %></td>
													<td align="right"><%=outLCConfAmt %></td>
													<td align="right"><%=outLCConfAmtUsd %></td>
													<td style="background-color: #b3f0ff" align="right"><%=outLCCanCount %></td>
													<td align="right"><%=outLCCanAmt %></td>
													<td align="right"><%=outLCCanAmtUsd %></td>
												</tr>
												<tr>
													<td align="center" style="background-color: "><b>BG</b></td>
													<td style="background-color: #b3f0ff" style="background-color: #b3f0ff" align="center"><%=outBGAdvCount%></td>
													<td align="right"><%=outBGAdvAmt %></td>
													<td align="right"><%=outBGAdvAmtUsd %></td>
													<td style="background-color: #b3f0ff" align="right"><%=outBGConfCount %></td>
													<td align="right"><%=outBGConfAmt %></td>
													<td align="right"><%=outBGConfAmtUsd %></td>
													<td style="background-color: #b3f0ff" align="right"><%=outBGCanCount %></td>
													<td align="right"><%=outBGCanAmt %></td>
													<td align="right"><%=outBGCanAmtUsd %></td>
												</tr>
												<tr>
													<td align="center" style="background-color: "><b>PCG</b></td>
													<td style="background-color: #b3f0ff" style="background-color: #b3f0ff" align="center"><%=outPCGAdvCount%></td>
													<td align="right"><%=outPCGAdvAmt %></td>
													<td align="right"><%=outPCGAdvAmtUsd %></td>
													<td style="background-color: #b3f0ff" align="right"><%=outPCGConfCount %></td>
													<td align="right"><%=outPCGConfAmt %></td>
													<td align="right"><%=outPCGConfAmtUsd %></td>
													<td style="background-color: #b3f0ff" align="right"><%=outPCGCanCount %></td>
													<td align="right"><%=outPCGCanAmt %></td>
													<td align="right"><%=outPCGCanAmtUsd %></td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="card cardmain">
						<div class="card-header cdheader">
							 <div class="d-flex justify-content-between">
								<div class="topheader">
									Incoming/Outgoing
								</div>
							</div>
						</div>
						<div class="card-body cdbody">
							<div class="table-responsive">
								<table id="search_by_filter" class="table table-bordered table-hover">
									<thead>
										<tr>
											<th align="center">Sr#</th>
											<th align="center">Legal Entity<div align="center"><input class="form-control form-control-sm" type="text" id="legal_entity" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
											<th align="center">Counterparty<div align="center"><input class="form-control form-control-sm" type="text" id="counterparty_nm" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
											<th align="center">Security Type<div align="center"><input class="form-control form-control-sm" type="text" id="security_type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
											<th align="center">Incoming/ Outgoing
											<br>
									    	<div align="center">
										    	<select class="form-select form-select-sm" name="filter_security_type" onchange="refresh();" style="width:75px;" >
										    		<option value="0">All</option>
										    		<option value="INC">Incoming</option>
										    		<option value="OUT">Outgoing</option>
										    	</select>
									    	</div>
									    	<script>document.forms[0].filter_security_type.value="<%=filter_security_type%>"</script>
											</th>
											<th align="center">Contract Type<div align="center"><input class="form-control form-control-sm" type="text" id="cont_type" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
											<th align="center">Deal Ref#<div align="center"><input class="form-control form-control-sm" type="text" id="dealNo" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
											<th align="center">Security Ref#<div align="center"><input class="form-control form-control-sm" type="text" id="security_reference_no" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div></th>
											<th align="center">Status<div align="center"><input class="form-control form-control-sm" type="text" id="stats" onkeyup="Search(this,'8');" placeholder="Search.." style="width:100px"/></div></th>
											<th align="center">Security Value(INR)</th>
											<th align="center">Security Value(USD)</th>
											<th align="center">Received Date</th>
											<th align="center">Issued Date</th>
											<th align="center">Expire Date</th>
											<th align="center">Cancellation Date</th>
											<th align="center">Issuing Bank name</th>
											<th align="center">Issuing Bank's Reference</th>
											<th align="center">Advising Bank Name</th>
											<th align="center">Advising Bank's Reference</th>
											<th align="center">Confirming Bank Name</th>
											<th align="center">Confirming Bank's Reference</th>
											<th align="center">Remarks</th>
										</tr>
									</thead>
									<tbody>
									<%if(VCOUNTERPARTY_NM.size()>0){ %>
										<%for(int i=0;i<VCOUNTERPARTY_NM.size();i++) {%>
										<tr>
											<td align="center"><%=i+1%></td>
											<td align="center"><%=VLEGAL_ENTITY.elementAt(i)%></td>
											<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
											<td align="center">
											<span 
											<%if(VSEC_TYPE.elementAt(i).equals("LC")){ %>
	    										class="alert alert-info"
	    									<%}else if(VSEC_TYPE.elementAt(i).equals("BG")){ %>
	    										class="alert alert-warning"
	    									<%}else if(VSEC_TYPE.elementAt(i).equals("PCG")){ %>
	    										class="alert alert-primary"
	    									<%}%>><b><%=VSEC_TYPE.elementAt(i) %></b></span></td>
											<td align="center"><span
											<%if(VSEC_CATEGORY.elementAt(i).equals("R")){ %>
	    										class="alert alert-info"
	    									<%}else if(VSEC_CATEGORY.elementAt(i).equals("I")){ %>
	    										class="alert alert-warning"
	    									<%}%>><b><%if(VSEC_CATEGORY.elementAt(i).equals("R")){%>Incoming<%}else if(VSEC_CATEGORY.elementAt(i).equals("I")) {%>Outgoing<%} %></b></span></td>
											<td align="left"><%=VDIS_CONTRACT_TYPE.elementAt(i)%></td>
											<td align="center"><%=VDEAL_NO.elementAt(i)%></td>
											<td align="center"><%=VSEC_REF_NO.elementAt(i)%></td>
											<td align="center">
												<span 
											<%if(VSTATUS.elementAt(i).equals("Pending")){ %>
	    										class="alert alert-primary"
	    									<%}else if(VSTATUS.elementAt(i).equals("In Order")){ %>
	    										class="alert alert-success"
	    									<%}else if(VSTATUS.elementAt(i).equals("Cancelled")){ %>
	    										class="alert alert-danger"
	    									<%}else if(VSTATUS.elementAt(i).equals("Pending For Amendment")){ %>
	    										class="alert alert-secondary"
	    									<%}else if(VSTATUS.elementAt(i).equals("Restated")){ %>
	    										class="alert alert-warning"
	    									<%}else if(VSTATUS.elementAt(i).equals("Dummy")){ %>
	    										class="alert alert-info"
	    									<%}%>><b><%=VSTATUS.elementAt(i) %></b></span>
											</td>
											<td align="right"><%if(!VVALUE.elementAt(i).equals("")){%><%=nf2.format(Double.parseDouble(""+VVALUE.elementAt(i)))%><%}else{ %><%=nf2.format(Double.parseDouble(""+VVALUE_USD.elementAt(i))*Double.parseDouble(""+VEXCHANGE_RATE.elementAt(i)))%><%}%></td>
											<td align="right"><%if(!VVALUE_USD.elementAt(i).equals("")){%><%=nf2.format(Double.parseDouble(""+VVALUE_USD.elementAt(i)))%><%}else{ %><%=nf2.format(Double.parseDouble(""+VVALUE.elementAt(i))/Double.parseDouble(""+VEXCHANGE_RATE.elementAt(i)))%><%}%></td>
											<td align="center"><%=VRECEIVED_DATE.elementAt(i)%></td>
											<td align="center"><%=VISSUE_DT.elementAt(i)%></td>
											<td align="center"><%=VEXPIRE_DT.elementAt(i)%></td>
											<td align="center"><%=VCANCEL_DT.elementAt(i)%></td>
											<td align="center"><%=VISS_BANK_NM.elementAt(i)%></td>
											<td align="center"><%=VISS_BANK_REF.elementAt(i)%></td>
											<td align="center"><%=VADV_BANK_NM.elementAt(i)%></td>
											<td align="center"><%=VADV_BANK_REF.elementAt(i)%></td>
											<td align="center"><%=VCONFIRM_BANK_NM.elementAt(i)%></td>
											<td align="center"><%=VCONFIRM_BANK_REF.elementAt(i)%></td>
											<td align="center"><%=VREMARK.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="22" align="center"><%=utilmsg.infoMessage("<b>No Security Available in Selected Time Period!</b>") %></td>
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
<script>
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("search_by_filter");
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	if (td) 
    	{
      		txtValue = td.textContent || td.innerText;
      		if (txtValue.toLocaleLowerCase().indexOf(filter) > -1) {
        		tr[i].style.display = "";
        		count++;
      		} else {
      			tr[i].style.display = "none";
      		}
    	}       
  	}
}
</script>
</body>

</html>
