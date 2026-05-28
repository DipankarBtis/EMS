
<%@page import="java.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var action = document.forms[0].action.value;
	var rpt_dt = document.forms[0].rpt_dt.value;
	var chk_update_flag = document.forms[0].chk_update_flag.value;

	var flag=true;
	
	var u = document.forms[0].u.value;
	
	if (!chk_update_flag.includes("Y") && action !== "list") {
		flag = false;
	}
	
	if (flag) {
	//	if(confirm("Action: " + action + " selected. \n\nClick OK to continue."))
		{
			var url = "rpt_deal_active_price.jsp?counterparty_cd="+counterparty_cd+"&u="+u+
					"&rpt_dt="+rpt_dt+"&action="+action+"&chk_update_flag="+chk_update_flag;
		
			document.getElementById("loading").style.visibility = "visible";
			location.replace(url);
		}
	}
	//else {
	if (!chk_update_flag.includes("Y") && action !== "list") {
		alert("No Deal is Selected.\nKindly Select atleast one checkbox.");
		document.forms[0].action.value = "list";
	}
}

var newWindow;
function openExposure(report_dt, counterparty_cd, owner_cd, account, cont_type, agmt_no, agmt_rev, cont_no, cont_rev, cargo_no) {
	var u = document.forms[0].u.value;
	
	if (confirm("Do you want to view Exposure Report?")) {
		var url = "../market_risk/rpt_mr_deal_dtl.jsp?report_dt="+report_dt+"&counterparty_cd="+counterparty_cd+"&legalEntity="+owner_cd+
				"&account="+account+"&contract_type="+cont_type+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&cont_no="+cont_no+"&cont_rev="+cont_rev+
				"&cargo_no="+cargo_no+"&u="+u;
		
		if(!newWindow || newWindow.closed)
		{
			newWindow= window.open(url,"Exposure Report","top=10,left=70,width=800,height=700,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow= window.open(url,"Exposure Report","top=10,left=70,width=800,height=700,scrollbars=1");
		}
	}
}
function openVariablePrice(counterparty_cd, cont_no, cont_rev, cont_ref, agmt_no, agmt_rev, cont_type, cargo_no, start_dt, end_dt, cont_status) {
	var u = document.forms[0].u.value;
	
	if (confirm("Do you want to view Variable Pricing?")) {
		var url = "../mgmt_reports/frm_config_variable_price.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+cont_type+
				"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev+"&cont_ref="+cont_ref+
				"&cargo_no="+cargo_no+"&cont_status="+cont_status+"&start_dt="+start_dt+"&end_dt="+end_dt+"&u="+u;
		
		if(!newWindow || newWindow.closed)
		{
			newWindow= window.open(url,"Variable Price","top=10,left=70,width=1000,height=700,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow= window.open(url,"Variable Price","top=10,left=70,width=1000,height=700,scrollbars=1");
		}
	}
}
function selectCheckBox(index) {
	
	total_count = document.forms[0].chkBox.length;
	chkBox = document.forms[0].chkBox;
	document.forms[0].chk_update_flag.value = "";
	
	for (var i = 0; i < total_count; i++) {
		if (chkBox[i].checked) {
			document.forms[0].chk_update_flag.value += "Y:";
		}
		else {
			document.forms[0].chk_update_flag.value += "N:";
		}
	}
	/// alert(document.forms[0].chk_update_flag.value);
}
function exportToXls()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var url = "xls_deal_active_price.jsp?fileName=NCF Report.xls&counterparty_cd="+counterparty_cd+"&from_dt="+from_dt+"&to_dt="+to_dt;
	location.replace(url);
}
function exportToXls()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var action = document.forms[0].action.value;
	var rpt_dt = document.forms[0].rpt_dt.value;
	var chk_update_flag = document.forms[0].chk_update_flag.value;
	var report_date = rpt_dt.split("/")[2] + rpt_dt.split("/")[1] + rpt_dt.split("/")[0];
	
	var fileName = "Deal_Active_Price_"+report_date+".xls";
	
	var url="xls_deal_active_price.jsp?counterparty_cd="+counterparty_cd+"&rpt_dt="+rpt_dt+"&action="+action+"&chk_update_flag="+chk_update_flag+"&fileName="+fileName;
	location.replace(url);
}
function SetPriceforReqAprvRevoke()
{ 
   // alert(document.forms[0].set.value);
    if(document.forms[0].set.value=="Set")
    {
		var flag = true;
		var msg = "First Check The Following Fields :\n\n";
		//var index_count = document.forms[0].chk.length;//parseInt(document.forms[0].chk_flag.value);
		var index_count = document.forms[0].chkBox.length;
		//chk = document.forms[0].chkBox;
		var i = 0;
		//document.forms[0].chk_flag.value="";
		document.forms[0].chk_update_flag.value="";
		//alert(index_count);
		if(index_count==1)
		{
			var price_new_value = document.forms[0].new_value.value;
			if(document.forms[0].chkBox.checked)
			{
				document.forms[0].chk_update_flag.value="Y";
			}
			else
			{
				if(price_new_value=='')
					document.forms[0].chkBox.checked = false;
				else
					document.forms[0].chkBox.checked = true;
				if(price_new_value=='')
					document.forms[0].chk_update_flag.value="N";
				else
					document.forms[0].chk_update_flag.value="Y";
				flag = false;
			}
		}
		else if(index_count>1)
		{
			var cnt = 0;
			var index = 0;
			for(i=0;i<index_count;i++)
			{//alert(i+") "+document.forms[0].chk_update_flag.value);
				var price_new_value = document.forms[0].new_value[i].value;
				if(document.forms[0].chkBox[i].checked)
				{
				//	document.forms[0].chk[i].checked =false;
					index = i+1;
					//document.forms[0].chk_flag[i].value = "Y";//alert(document.forms[0].chk_flag[i].value);
					if(index>1)
						document.forms[0].chk_update_flag.value += ":";
					if(price_new_value=='')
						document.forms[0].chk_update_flag.value += "N";
					else
						document.forms[0].chk_update_flag.value += "Y";
					cnt++;
				}
				else
				{
					if(price_new_value=='')
						document.forms[0].chkBox[i].checked = false;
					else
						document.forms[0].chkBox[i].checked = true;
					index = i+1;
					//document.forms[0].chk_flag[i].value = "N";
					if(index>1)
						document.forms[0].chk_update_flag.value += ":";
					if(price_new_value=='')
						document.forms[0].chk_update_flag.value += "N";
					else
						document.forms[0].chk_update_flag.value += "Y";
				}
			}
		}
		else
		{
			alert("Index Count NOT Defined Properly !!!");
			flag = false;
		}
		document.forms[0].set.value='Reset';
    }
    else
    {
    	ResetPriceforReqAprvRevoke();
		document.forms[0].set.value='Set';
    }
}
function ResetPriceforReqAprvRevoke()
{ //alert(RefCd); alert(Type); 
	//alert("RESET");
	var flag = true;
	var msg = "First Check The Following Fields :\n\n";
	var index_count = document.forms[0].chkBox.length;//parseInt(document.forms[0].chk_flag.value);
	var i = 0;
	
	//document.forms[0].chk_flag.value="";
	document.forms[0].chk_update_flag.value="";
	//alert(index_count);
	if(index_count==1)
	{
		if(document.forms[0].chkBox.checked)
		{
			document.forms[0].chk_update_flag.value = "N";
		}
		/* else
		{
			document.forms[0].chk_update_flag.value = "N";
			msg += "Please Select The CheckBox Before Submitting The Data !!!\n";
			flag = false;
		} */
	}
	else if(index_count>1)
	{
		var cnt = 0;
		var index = 0;
		for(i=0;i<index_count;i++)
		{//alert(i);
			
				index = i+1;
				if(index>1)
					document.forms[0].chk_update_flag.value += ":";
				document.forms[0].chk_update_flag.value += "N";
				cnt++;
				document.forms[0].chkBox[i].checked=false;
			
		}
		//alert(cnt);
		if(cnt==0)
		{
			msg += "Please Select Atleast One CheckBox Before Submitting The Data !!!\n";
			flag = false;
		}
	}
	else
	{
		alert("Index Count NOT Defined Properly !!!");
		flag = false;
	}
	document.forms[0].set.value='Reset';
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_DealActivePrice" id="db_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
//String sysdate=utildate.getPreviousDate();
String sysdate=utildate.getSysdate();
String sysDateTime=utildate.getSysdateWithTime24hr();

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String action=request.getParameter("action")==null?"list":request.getParameter("action");
String rlng_dlng=request.getParameter("rlng_dlng")==null?"0":request.getParameter("rlng_dlng");
String buy_sell=request.getParameter("but_sell")==null?"0":request.getParameter("buy_sell");
String rpt_dt=request.getParameter("rpt_dt")==null?sysdate:request.getParameter("rpt_dt");
String chk_update_flag = request.getParameter("chk_update_flag")==null ? "" : request.getParameter("chk_update_flag");
String curMthYr=utildate.getMonthName(rpt_dt)+"-"+utildate.getCurrentYear(rpt_dt);

db_mgmt.setCallFlag("ACTIVE_DEAL_PRICE");
db_mgmt.setComp_cd(owner_cd);
db_mgmt.setCounterparty_cd(counterparty_cd);
db_mgmt.setReport_dt(rpt_dt);
db_mgmt.setAction(action);
db_mgmt.setChkUpdateFlag(chk_update_flag);
db_mgmt.setEmpCD(session.getAttribute("emp_cd").toString());
db_mgmt.init();

Vector VMST_COUNTERPARTY_CD = db_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = db_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = db_mgmt.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_NM = db_mgmt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_CD = db_mgmt.getVCOUNTERPARTY_CD();
Vector VCONTRACT_TYPE = db_mgmt.getVCONTRACT_TYPE();
Vector VCOUNTERPARTY_ABBR = db_mgmt.getVCOUNTERPARTY_ABBR();
Vector VDEAL_TYPE = db_mgmt.getVDEAL_TYPE();
Vector VCONT_PERIOD = db_mgmt.getVCONT_PERIOD();
Vector VCONT_STATUS = db_mgmt.getVCONT_STATUS();
Vector VPRICE_TYPE = db_mgmt.getVPRICE_TYPE();
Vector VAPPROVED_RATE = db_mgmt.getVAPPROVED_RATE();
Vector VAPPROVED_DATE = db_mgmt.getVAPPROVED_DATE();
Vector VAPPLICABLE_RATE = db_mgmt.getVAPPLICABLE_RATE();
Vector VRATE_DT_RANGE = db_mgmt.getVRATE_DT_RANGE();
Vector VRATE_EFF_DT = db_mgmt.getVRATE_EFF_DT();
Vector VBILL_FREQ = db_mgmt.getVBILL_FREQ();
Vector VBILL_FREQ_DT = db_mgmt.getVBILL_FREQ_DT();
Vector VPriceAppl = db_mgmt.getVPriceAppl();

Vector VTRADE_DT = db_mgmt.getVTRADE_DT();
Vector VBUY_SELL = db_mgmt.getVBUY_SALE();
Vector VAWAITING_RATE = db_mgmt.getVAWAITING_RATE();
Vector VDEAL_NUMBER = db_mgmt.getVDEAL_NUMBER();
Vector VDEAL_REF = db_mgmt.getVDEAL_REF();
Vector VFIN_CURV = db_mgmt.getVFIN_CURV();
Vector VPHY_CURV = db_mgmt.getVPHY_CURV();
Vector VAGMT_NO = db_mgmt.getVAGMT_NO();
Vector VAGMT_REV = db_mgmt.getVAGMT_REV();
Vector VCONT_NO = db_mgmt.getVCONT_NO();
Vector VCONT_REV = db_mgmt.getVCONT_REV();
Vector VCARGO_NO = db_mgmt.getVCARGO_NO();

// Header
Vector VApplicableCurves = db_mgmt.getVApplicableCurves();
Vector VApplicableSpotPrice = db_mgmt.getVApplicableSpotPrice();
Vector VApplicableSpotUnit = db_mgmt.getVApplicableSpotUnit();
Vector VApplicableCommodityType = db_mgmt.getVApplicableCommodityType();
Vector VApplicableSpotFwdRegdDt = db_mgmt.getVApplicableSpotFwdRegdDt();
Vector gVCurveContMthSettlePeriod = db_mgmt.getVCurveContMthSettlePeriod();
Vector VCurveContMthSettlePrice = db_mgmt.getVCurveContMthSettlePrice();
String lastCurveEntDt = db_mgmt.getLastCurveEntDt();
String LastPriceEntryDateTime = db_mgmt.getLastPriceEntryDateTime(); //SB20251215
String ContMthYr = db_mgmt.getContMthYr();
String ToEmail= db_mgmt.getToEmail();//SB20250117


NumberFormat nf = new DecimalFormat("###########0.00");

action = "list";
msg = db_mgmt.getMsg();
msg_type = db_mgmt.getMsgType();
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
				    		Active Deal Pricing (<%=sysDateTime %>)
	   	 				</div>
						<div class="col-auto">
					 		<span class="input-group-text"><a id="rptDownloadBtn"><i title="Export To Excel" onclick="exportToXls();" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></a></span>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Counter Party: </b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh();">
										<option value="0">--All--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_ABBR.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Report Dt:</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="rpt_dt" value="<%=rpt_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						
		      						</div>
				    			</div>
							</div>
						</div>
					
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Action: </b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="action" onchange="refresh();">
										<option value="list">List</option>
										<%if(write_access.equals("Y")) { %><option value="request">Request</option><%} %>
										<%if(approve_access.equals("Y")) { %><option value="approve">Approve</option><%} %>
										<%if(approve_access.equals("Y")) { %><option value="revoke">Revoke</option><%} %>
										<%if(delete_access.equals("Y")) { %><option value="delete">Delete</option><%} %>
									</select>
									<script>document.forms[0].action.value="<%=action%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Refresh" onclick="refresh();">
								</div>
							</div>
						</div>
					</div>					
					
					
						<div class="table-responsive">
						<table class="table table-bordered" id="currentPrice">						
								<tr align="center">
								<th align="center" >Regd. Date<%//=LastPriceEntryDateTime %></th>
								<th align="center" >Spot Pricing Day <%=lastCurveEntDt%> <%//=LastPriceEntryDateTime %></th>
								<th align="center" colspan="2">Day Price<%//=LastPriceEntryDateTime %></th>
								<th align="center" ><%=curMthYr%><%//=LastPriceEntryDateTime %></th>
								<th align="center" colspan="2">Avg. Settle Price<%//=LastPriceEntryDateTime %></th>
								</tr>
						
							<% for (int i = 0; i < VApplicableCurves.size(); i++) { %>
								<tr align="center">
									<td align="center"><%//=LastPriceEntryDateTime %><%=VApplicableSpotFwdRegdDt.elementAt(i) %></td>
									<td align="right"><%=VApplicableCurves.elementAt(i) %> (<%=VApplicableCommodityType.elementAt(i)%>)</td>
									<td align="right"><%= VApplicableSpotPrice.elementAt(i)%></td>
									<td align="left"><%=VApplicableSpotUnit.elementAt(i) %></td>
									<td align="right"><%=VApplicableCurves.elementAt(i) %></td>
									<td align="right"><%=VCurveContMthSettlePrice.elementAt(i) %></td>
									<td align="left"><%=VApplicableSpotUnit.elementAt(i) %></td>
								</tr>
								<%} %> 
							
						</table>
					</div>
					
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered" id="example">
							<thead id="tbsearch">
								<tr>
									<th align="center"><input type="button" class="btn btn-warning com-btn" name="set" value="Set" onclick="SetPriceforReqAprvRevoke();">Sr#</th>
									<!-- <th align="center">Counterparty ABBR</th> -->
									<th align="center">Counterparty</th>
									<th align="center">Deal Id#/Ref</th>
									<th align="center">Buy-Sell/Deal Type</th>
									<th align="center">Contract Period</th>
									<th align="center">Price Type / Billing</th>
									<th align="center">Approved Price (MMBTU)</th>
									<th align="center">Awaiting for Approval</th>
									<th align="center">Applicable Price</th>
									<th align="center">Financial Curve</th>
									<!-- <th align="center">Physical Curve</th> -->
								</tr>
							</thead>
							<tbody>
								<%if(VCOUNTERPARTY_CD.size()>0){
									for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>									
										<% String rowcolor=""; if(i%2==0) {	rowcolor="D9D9F3";}else{ rowcolor="";}	%>
									<tr style="background-color: <%=rowcolor%>">
										<td align="left" TITLE="<%=VPriceAppl.elementAt(i) %>"><%=i+1 %>&nbsp;
										<%if(VAWAITING_RATE.elementAt(i).equals("") && VAPPLICABLE_RATE.elementAt(i).equals("")) { %>
										<input type="checkbox" name="chkBox" id="<%=VDEAL_NUMBER.elementAt(i)%>" onclick="selectCheckBox('<%=i%>');" DISABLED> 
										<%} else { %>
										<input type="checkbox" name="chkBox" id="<%=VDEAL_NUMBER.elementAt(i)%>" onclick="selectCheckBox('<%=i%>');"> 
										<%} %>
										</td>
										<td align="left" title="Click to View Exposure Data." onclick="openExposure('<%=rpt_dt%>', '<%=VCOUNTERPARTY_CD.elementAt(i)%>', '<%=owner_cd%>', '<%=VBUY_SELL.elementAt(i)%>', '<%=VCONTRACT_TYPE.elementAt(i)%>', '<%=VAGMT_NO.elementAt(i)%>', '<%=VAGMT_REV.elementAt(i)%>', '<%=VCONT_NO.elementAt(i)%>', '<%=VCONT_REV.elementAt(i)%>', '<%=VCARGO_NO.elementAt(i)%>')">
										<%=VCOUNTERPARTY_ABBR.elementAt(i)%>-<%=VCOUNTERPARTY_NM.elementAt(i)%><BR>
										<span
											<%if(VCONT_STATUS.elementAt(i).equals("Approved")){ %>
	    										class="alert" style="color: green;"
	    									<%}else if(VCONT_STATUS.elementAt(i).equals("Pending Approval")){ %>
	    										class="alert" style="color: black;"
	    									<%}else { %>
	    										class="alert" style="color: red;"
	    									<%}%>><b><%=VCONT_STATUS.elementAt(i)%></b>
	    								</span>
										<td align="center" TITLE="(<%=VCOUNTERPARTY_CD.elementAt(i)%>)REV#:<%=VCONT_REV.elementAt(i)%>"><%=VDEAL_NUMBER.elementAt(i)%><BR><%=VDEAL_REF.elementAt(i)%></td>
										<td align="left"><%//=VDEAL_TYPE.elementAt(i)%><!-- </td> -->
										<!-- <td align="center"> -->
											<span
											<%if(VBUY_SELL.elementAt(i).equals("Buy")){ %>
	    										class="alert" style="background: #ffccff; color: #cc00cc;"
	    									<%}else { %>
	    										class="alert alert-primary"
	    									<%}%>><b><%=VBUY_SELL.elementAt(i)%></b></span>
	    									<%=VDEAL_TYPE.elementAt(i)%>
		    							</td>
									
										<td align="center" TITLE="Deat Dt:<%=VTRADE_DT.elementAt(i)%>"><%=VCONT_PERIOD.elementAt(i)%></td>
										
										<td align="center" onclick="openVariablePrice('<%=VCOUNTERPARTY_CD.elementAt(i)%>', '<%=VCONT_NO.elementAt(i)%>', '<%=VCONT_REV.elementAt(i)%>', '<%=VDEAL_REF.elementAt(i)%>', '<%=VAGMT_NO.elementAt(i)%>', '<%=VAGMT_REV.elementAt(i)%>', '<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VCARGO_NO.elementAt(i)%>', '<%=VCONT_PERIOD.elementAt(i).toString().split(" - ")[0]%>', '<%=VCONT_PERIOD.elementAt(i).toString().split(" - ")[1]%>', '<%=VCONT_STATUS.elementAt(i)%>')">
										<span <%if(VPRICE_TYPE.elementAt(i).equals("Fixed")){ %>
	    										class="alert" style="background: lightblue; color: #cc00cc;"
	    									<%}else { %>
	    										class="alert alert-primary"
	    									<%}%>>
										<B><%=VPRICE_TYPE.elementAt(i)%></B></span><BR><%=VBILL_FREQ.elementAt(i) %></td>
										<td align="center">
										<span <%if(VPriceAppl.elementAt(i).equals("Y")){ %>
	    										class="alert" style="background: lightgreen; color: black;"	    									
	    									<%}%>>
										<B><%=VAPPROVED_RATE.elementAt(i)%></B></span>
										<BR><%=VAPPROVED_DATE.elementAt(i)%>
										</td>
										<td align="right"><%=VAWAITING_RATE.elementAt(i)%></td>
										<td align="right" TITLE="Avg. Date Range <%=VRATE_DT_RANGE.elementAt(i) %> Eff Date:<%=VBILL_FREQ_DT.elementAt(i) %>">
										<span <%if(VPriceAppl.elementAt(i).equals("Y")){ %>
	    										class="alert" style="background: lightgreen; color: blue;"	    									
	    									<%}%>>
										<%=VAPPLICABLE_RATE.elementAt(i)%></span>
										<BR><%=VRATE_EFF_DT.elementAt(i) %>
										 <input type="hidden" name="new_value" value="<%=VAPPLICABLE_RATE.elementAt(i) %>"></td>
										<td align="right" TITLE="Physical Curve: <%=VPHY_CURV.elementAt(i)%>"><%=VFIN_CURV.elementAt(i)%></td>
										<%-- <td align="right"><%=VPHY_CURV.elementAt(i)%></td> --%>
									</tr>
									<%} %>
									
									 <tr class="title2">			
						 				<td colspan="7" >
											<div align="left">
											<input type='radio' name="email" value="Y" CHECKED>Send Email&nbsp;To:&nbsp;
											<%if(ToEmail.equals("")){ %>
											<input type="hidden" name="email_to" value="<%=ToEmail %>" TITLE="<%=ToEmail %>" style="background: white;color:black;font-weight:bolder;" size="50">N.A.
											<%} else { %>
											<input type="text" name="email_to" value="<%=ToEmail %>" TITLE="<%=ToEmail %>" style="background: white;color:black;font-weight:bolder;" size="60" readonly>
											<%} %>
								     		<!-- <input type='radio' name="email" value="N" onChange="SetEmail('N');">No Email -->
											</div>
										</td>		
										<td colspan="3" align="left">&nbsp;<FONT SIZE=1 COLOR=black>&nbsp;
										<input type="hidden" name="chk_update_flag" value = "">	<!-- This will be used to submit data. -->
										</FONT>
										</td>					
									</tr>
								<%}else{ %>
									<tr>
										<td colspan=10 align="center"><%=utilmsg.infoMessage("<b>No Data Available for Active Deal Pricing!</b>") %></td>
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
$(document).ready(function() {
	
	$('#tbsearch th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "Sr#")
		{
			//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
		}
		else
		{
			$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:100px"/></div>');
		}
	});
	
});

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example");
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	//tbody=tr[i].getElementsByTagName("tbody");alert(tbody)
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