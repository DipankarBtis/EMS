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
	var expo_type = document.forms[0].expo_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_exposure_tracking.jsp?report_dt="+report_dt+"&expo_type="+expo_type+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function nextDate(day_no)
{
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
		
		refresh();
	}
}
function exportToXls()
{
	var report_dt = document.forms[0].report_dt.value;
	var expo_type = document.forms[0].expo_type.value;
	
	var url = "xls_exposure_tracking.jsp?fileName=Exposure Tracking Report "+report_dt+".xls&report_dt="+report_dt+"&expo_type="+expo_type;

	location.replace(url);
}

function displayInfo(id,id1)
{
	document.getElementById("calcTable").innerHTML="";
	document.getElementById("calcTable").innerHTML=document.getElementById(id).value;
	document.getElementById("modalHedingInfo").innerHTML=document.getElementById(id1).value;
	$("#calcInfoModel").modal("show");

}
</script>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DB_CR_ExposureTracking" id="credit_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String yesdate = utildate.getPreviousDate();

String report_dt=request.getParameter("report_dt")==null?yesdate:request.getParameter("report_dt");
String expo_type=request.getParameter("expo_type")==null?"R":request.getParameter("expo_type");

credit_risk.setCallFlag("EXPOSURE_TRACKING");
credit_risk.setReport_dt(report_dt);
credit_risk.setExpo_type(expo_type);
credit_risk.init();

Vector VACCOUNT = credit_risk.getVACCOUNT();
Vector VCOUNTERPARTY_CD = credit_risk.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = credit_risk.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = credit_risk.getVCOUNTERPARTY_ABBR();
Vector VAGMT_NO = credit_risk.getVAGMT_NO();
Vector VAGMT_REV_NO = credit_risk.getVAGMT_REV_NO();
Vector VCONT_NO = credit_risk.getVCONT_NO();
Vector VCONT_REV_NO = credit_risk.getVCONT_REV_NO();
Vector VDISPLAY_DEAL_MAP = credit_risk.getVDISPLAY_DEAL_MAP();
Vector VSIGNING_DT = credit_risk.getVSIGNING_DT();
Vector VSTART_DT = credit_risk.getVSTART_DT();
Vector VEND_DT = credit_risk.getVEND_DT();
Vector VPRICE_TYPE = credit_risk.getVPRICE_TYPE();
Vector VRATE = credit_risk.getVRATE();
Vector VRATE_UNIT = credit_risk.getVRATE_UNIT();
Vector VRATE_UNIT_NM = credit_risk.getVRATE_UNIT_NM();
Vector VTCQ = credit_risk.getVTCQ();
Vector VDCQ = credit_risk.getVDCQ();

Vector VBILLED_AMT = credit_risk.getVBILLED_AMT();
Vector VBILLED_AMT_INFO = credit_risk.getVBILLED_AMT_INFO();

Vector VUNBILLED_AMT = credit_risk.getVUNBILLED_AMT();
Vector VUNBILLED_AMT_INFO = credit_risk.getVUNBILLED_AMT_INFO();

Vector VUNBILLED_CURRENT_MONTH = credit_risk.getVUNBILLED_CURRENT_MONTH(); 	
Vector VUNBILLED_CURRENT_MONTH_INFO = credit_risk.getVUNBILLED_CURRENT_MONTH_INFO(); 	

Vector VFORWARD_NOTIONAL = credit_risk.getVFORWARD_NOTIONAL(); 
Vector VFORWARD_NOTIONAL_INFO = credit_risk.getVFORWARD_NOTIONAL_INFO(); 	

Vector VGROSS_EXPOSURE = credit_risk.getVGROSS_EXPOSURE();
Vector VGROSS_EXPOSURE_INFO = credit_risk.getVGROSS_EXPOSURE_INFO(); 	

Vector VGROSS_EXPOSURE_TAX = credit_risk.getVGROSS_EXPOSURE_TAX();
Vector VGROSS_EXPOSURE_TAX_INFO = credit_risk.getVGROSS_EXPOSURE_TAX_INFO();

Vector VCOLLATERAL_VALUE = credit_risk.getVCOLLATERAL_VALUE();
Vector VCOLLATERAL_INFO = credit_risk.getVCOLLATERAL_INFO();

Vector VCASH_COLLATERAL_VALUE = credit_risk.getVCASH_COLLATERAL_VALUE();
Vector VCASH_COLLATERAL_INFO = credit_risk.getVCASH_COLLATERAL_INFO();

Vector VNET_EXPOSURE = credit_risk.getVNET_EXPOSURE();
Vector VNET_EXPOSURE_INFO = credit_risk.getVNET_EXPOSURE_INFO();

Vector VPCG_VALUE = credit_risk.getVPCG_VALUE();
Vector VPCG_INFO = credit_risk.getVPCG_INFO();

Vector VLIMIT = credit_risk.getVLIMIT();
Vector VCREDIT_EXCEED = credit_risk.getVCREDIT_EXCEED();
Vector VNET_EXPOSURE_USD = credit_risk.getVNET_EXPOSURE_USD();
Vector VCREDIT_EXCEED_USD = credit_risk.getVCREDIT_EXCEED_USD();

Vector VCONSUMED_LIMIT = credit_risk.getVCONSUMED_LIMIT();
Vector VCREDIT_EXCEED_INFO = credit_risk.getVCREDIT_EXCEED_INFO();

Vector VLIMIT_VALUE_LINKED_COMP = credit_risk.getVLIMIT_VALUE_LINKED_COMP();
Vector VPARENT_LIMIT_VALUE = credit_risk.getVPARENT_LIMIT_VALUE();

Vector VINDEX = credit_risk.getVINDEX();
Vector VEXPOSURE_HEADING = credit_risk.getVEXPOSURE_HEADING();
Vector VEXPOSURE_HEADING_FLAG = credit_risk.getVEXPOSURE_HEADING_FLAG();

Vector VHEADING_INFO = credit_risk.getVHEADING_INFO();
Vector VBILLED_QTY = credit_risk.getVBILLED_QTY();

Vector VLEGAL_ENTITY = credit_risk.getVLEGAL_ENTITY();
Vector VLEGAL_ENTITY_ABBR = credit_risk.getVLEGAL_ENTITY_ABBR();
Vector VDIS_CONTRACT_TYPE = credit_risk.getVDIS_CONTRACT_TYPE();

String total_igx_ac_rece = credit_risk.getTotal_igx_ac_rece();
String total_igx_unbilled_rece = credit_risk.getTotal_igx_unbilled_rece();
String total_igx_delv_curr_mth = credit_risk.getTotal_igx_delv_curr_mth();
String total_igx_fwd_not = credit_risk.getTotal_igx_fwd_not();
String total_igx_gross_expo = credit_risk.getTotal_igx_gross_expo();
String total_igx_gross_expo_incl_tax = credit_risk.getTotal_igx_gross_expo_incl_tax();
String total_igx_net_expo = credit_risk.getTotal_igx_net_expo();
String total_igx_limit = credit_risk.getTotal_igx_limit();
String total_igx_cr_exceed = credit_risk.getTotal_igx_cr_exceed();
String total_igx_cr_exceed_usd = credit_risk.getTotal_igx_cr_exceed_usd();
String total_igx_net_expo_usd = credit_risk.getTotal_igx_net_expo_usd();

String isEodProcessDone=credit_risk.getIsEodProcessDone();
String eodProcessDoneOn=credit_risk.getEodProcessDoneOn();

String expo_type_nm="";
if(expo_type.equals("R"))
{
	expo_type_nm="Receivable";
}
else
{
	expo_type_nm="Payable";
}
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
					    	Exposure Tracking Report
					    </div>
					    <div class="col-sm-8 col-xs-8 col-md-8"></div>
					    <div class="btn-group">
							<select class="btn btn-outline-secondary btngrp btnactive" name="expo_type" onchange="refresh()">
								<!-- <option value="P">Payable</option> -->
								<option value="R">Receivable</option>
							</select>
						</div>
						<script>
							document.forms[0].expo_type.value="<%=expo_type%>"
						</script>
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
										<label class="form-label"><b>Report Date</b></label>
									</div>
									<div class="col-auto">
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
				</div>
				<%int i=0;
				for(int j=0; j < VEXPOSURE_HEADING.size(); j++) { 
					String expo_head_flag=""+VEXPOSURE_HEADING_FLAG.elementAt(j);
					int index=Integer.parseInt(""+VINDEX.elementAt(j));
				%>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;<%=VEXPOSURE_HEADING.elementAt(j) %></label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr <%if(isEodProcessDone.equals("Y")){%> style="background: skyblue;color:black;" <%} %>>
										<th rowspan="2">Sr#</th>
										<th rowspan="2">Legal Entity</th>
										<th rowspan="2">Account</th>										
										<th rowspan="2">Counterparty
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Counterparty" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Contract Type
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract_type" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Contract#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Signing Date</th>
										<th rowspan="2">Contract Period</th>
										<th rowspan="2">Price Type</th>
										<th rowspan="2">Price Rate</th>
										<th rowspan="2">Rate Unit</th>
										<th colspan="12">INR</th>
										<th colspan="2">USD</th>
									</tr>
									<tr <%if(isEodProcessDone.equals("Y")){%> style="background: skyblue;color:black;" <%} %>>
										<th title="Billed Gross Amount <%=expo_type_nm %>">Account <%=expo_type_nm %></th>
										<th title="Unbilled Supplied MMBTU * (Price + Transportation Charge + Marketing Margin + Other Charges)">Unbilled <%=expo_type_nm %></th>
										<th title="IF ((DCQ * Days) > Remaining TCQ) THEN Remaining TCQ * Price ELSE (DCQ * Days) * Price">Undelivered Current Month</th>
										<th>Forward Notional Next Month</th>
										<th title= "Account <%=expo_type_nm %> + Unbilled <%=expo_type_nm %>">Gross Exposure</th>
										<th>Gross Exposure<br>(Incl. Tax)</th>
										<th>Non-Cash Collateral</th>
										<th>Cash Collateral</th>
										<th>Net Exposure</th>
										<th>PCG</th>
										<th>Limit</th>
										<th>Credit Exceed</th>
										<th>Net Exposure</th>
										<th>Credit Exceed</th>
									</tr>
								</thead>
								<tbody>
								<%if(index>0){ 
									int k=0;
								%>
									<%for(i=i; i<VCOUNTERPARTY_CD.size(); i++){ 
										k=k+1;
									%>
									<%if(isEodProcessDone.equals("Y")){ %>
									<tr>
										<td align="center" style="background: #99ffcc;"><%=k%></td>
										<td align="center"><%=VLEGAL_ENTITY_ABBR.elementAt(i)%></td>
										<td align="center"><%=VACCOUNT.elementAt(i)%></td>
										<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
										<td align="left"><%=VDIS_CONTRACT_TYPE.elementAt(i)%></td>
										<td align="center"><%=VDISPLAY_DEAL_MAP.elementAt(i)%></td>
										<td align="center"><%=VSIGNING_DT.elementAt(i)%></td>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										<td align="center"><%=VPRICE_TYPE.elementAt(i) %></td>
										<td align="right"><%=VRATE.elementAt(i)%></td>
										<td align="center"><%=VRATE_UNIT_NM.elementAt(i)%></td>
										<td align="right"><%=VBILLED_AMT.elementAt(i)%></td>
										<td align="right"><%=VUNBILLED_AMT.elementAt(i)%></td>
										<td align="right"><%=VUNBILLED_CURRENT_MONTH.elementAt(i)%></td>
										<td align="right"><%=VFORWARD_NOTIONAL.elementAt(i)%></td>
										<td align="right"><%=VGROSS_EXPOSURE.elementAt(i)%></td>
										<td align="right"><%=VGROSS_EXPOSURE_TAX.elementAt(i)%></td>
										<td align="right"><%=VCOLLATERAL_VALUE.elementAt(i)%></td>
										<td align="right"><%=VCASH_COLLATERAL_VALUE.elementAt(i)%></td>
										<td align="right"><%=VNET_EXPOSURE.elementAt(i)%></td>
										<td align="right">
											<%if(!VPARENT_LIMIT_VALUE.elementAt(i).equals(""))
											{ %>
												<%=VPCG_VALUE.elementAt(i) %><br>
												<font color=green><%=VPARENT_LIMIT_VALUE.elementAt(i) %></font> 
											<%}
											else
											{%>
												<%=VPCG_VALUE.elementAt(i) %>
											<%} %>
										</td>
										<td align="right">
											<%if(!VPARENT_LIMIT_VALUE.elementAt(i).equals(""))
											{ %>
												<%=VLIMIT.elementAt(i) %><br>
												<%-- <font color=green><%=VPARENT_LIMIT_VALUE.elementAt(i) %></font> --%>
											<%}
											else
											{%>
												<%=VLIMIT.elementAt(i) %>
											<%} %>
										</td>
										<td align="right"><%=VCREDIT_EXCEED.elementAt(i)%></td>
										<td align="right"><%=VNET_EXPOSURE_USD.elementAt(i) %></td>
										<td align="right"><%=VCREDIT_EXCEED_USD.elementAt(i)%></td>
									</tr>
									<%} else{ %>
									<tr>
										<td align="center"><%=k%></td>
										<td align="center"><%=VLEGAL_ENTITY_ABBR.elementAt(i)%></td>
										<td align="center"><%=VACCOUNT.elementAt(i)%></td>
										<td>
											<%=VCOUNTERPARTY_NM.elementAt(i)%>
											<input type="hidden" id="headInfo<%=i%>" value="<%=VHEADING_INFO.elementAt(i)%>">
										</td>
										<td align="left"><%=VDIS_CONTRACT_TYPE.elementAt(i)%></td>
										<td align="center"><%=VDISPLAY_DEAL_MAP.elementAt(i)%></td>
										<td align="center"><%=VSIGNING_DT.elementAt(i)%></td>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										<td align="center"><%=VPRICE_TYPE.elementAt(i) %></td>
										<td align="right"><%=VRATE.elementAt(i)%></td>
										<td align="center"><%=VRATE_UNIT_NM.elementAt(i)%></td>
										<td align="right" onclick="displayInfo('billedInfo<%=i%>','headInfo<%=i%>');">
											<%=VBILLED_AMT.elementAt(i)%></td>
											<input type="hidden" id="billedInfo<%=i%>" value="<%=VBILLED_AMT_INFO.elementAt(i)%>">
										<td align="right" onclick="displayInfo('unbilledInfo<%=i%>','headInfo<%=i%>');">
											<%=VUNBILLED_AMT.elementAt(i)%>
											<input type="hidden" id="unbilledInfo<%=i%>" value="<%=VUNBILLED_AMT_INFO.elementAt(i)%>">
										</td>
										<td align="right" title="<%=VUNBILLED_CURRENT_MONTH_INFO.elementAt(i)%>"><%=VUNBILLED_CURRENT_MONTH.elementAt(i)%></td>
										<td align="right" title="<%=VFORWARD_NOTIONAL_INFO.elementAt(i)%>"><%=VFORWARD_NOTIONAL.elementAt(i)%></td>
										<td align="right" title="<%=VGROSS_EXPOSURE_INFO.elementAt(i)%>"><%=VGROSS_EXPOSURE.elementAt(i)%></td>
										<td align="right" title="<%=VGROSS_EXPOSURE_TAX_INFO.elementAt(i)%>"><%=VGROSS_EXPOSURE_TAX.elementAt(i)%></td>
										<td align="right" title="<%=VCOLLATERAL_INFO.elementAt(i)%>"><%=VCOLLATERAL_VALUE.elementAt(i)%></td>
										<td align="right" title="<%=VCASH_COLLATERAL_INFO.elementAt(i)%>"><%=VCASH_COLLATERAL_VALUE.elementAt(i)%></td>
										<td align="right" title="<%=VNET_EXPOSURE_INFO.elementAt(i)%>"><%=VNET_EXPOSURE.elementAt(i)%></td>
										<td align="right" title="<%=VPCG_INFO.elementAt(i)%>"><%-- <%=VPCG_VALUE.elementAt(i)%> --%>
											<%if(!VPARENT_LIMIT_VALUE.elementAt(i).equals(""))
											{ %>
												<%if(!VLIMIT_VALUE_LINKED_COMP.elementAt(i).equals("")) 
												{ %>
													<font color=blue><%=VPCG_VALUE.elementAt(i) %></font><br>
													<font color=green><%=VPARENT_LIMIT_VALUE.elementAt(i) %></font>
												<%} 
												else 
												{ %> 
													<%=VPCG_VALUE.elementAt(i) %><br>
													<font color=green><%=VPARENT_LIMIT_VALUE.elementAt(i) %></font>
												<%} %> 
											<%}
											else
											{%>
												<%if(!VLIMIT_VALUE_LINKED_COMP.elementAt(i).equals("")) 
												{ %>
													<font color=blue><%=VPCG_VALUE.elementAt(i) %></font>
												<%} 
												else 
												{ %> 
													<%=VPCG_VALUE.elementAt(i) %>
												<%} %>
											<%} %>
										</td>
										<td align="right" title="<%=VCONSUMED_LIMIT.elementAt(i)%>"><%-- <%=VLIMIT.elementAt(i)%> --%>
											<%if(!VPARENT_LIMIT_VALUE.elementAt(i).equals(""))
											{ %>
												<%if(!VLIMIT_VALUE_LINKED_COMP.elementAt(i).equals("")) 
												{ %>
													<font color=blue><%=VLIMIT.elementAt(i) %></font><br>
													<%-- <font color=green><%=VPARENT_LIMIT_VALUE.elementAt(i) %></font> --%>
												<%} 
												else 
												{ %> 
													<%=VLIMIT.elementAt(i) %><br>
													<%-- <font color=green><%=VPARENT_LIMIT_VALUE.elementAt(i) %></font> --%>
												<%} %> 
											<%}
											else
											{%>
												<%if(!VLIMIT_VALUE_LINKED_COMP.elementAt(i).equals("")) 
												{ %>
													<font color=blue><%=VLIMIT.elementAt(i) %></font>
												<%} 
												else 
												{ %> 
													<%=VLIMIT.elementAt(i) %>
												<%} %>
											<%} %>
										</td>
										<td align="right" title= "<%=VCREDIT_EXCEED_INFO.elementAt(i)%>"><%=VCREDIT_EXCEED.elementAt(i)%></td>
										<td align="right"><%=VNET_EXPOSURE_USD.elementAt(i) %></td>
										<td align="right"><%=VCREDIT_EXCEED_USD.elementAt(i)%></td>
									</tr>
									<%} %>
										<%if(k==index){%>
											<%if(expo_head_flag.equals("I")){%>
											<tr style="font-weight: bold;">
												<td colspan="11" align="right"><b>IGX Summary</b></td>
												<td align="right"><%=total_igx_ac_rece%></td>
												<td align="right"><%=total_igx_unbilled_rece%></td>
												<td align="right"><%=total_igx_delv_curr_mth%></td>
												<td align="right"><%=total_igx_fwd_not%></td>
												<td align="right"><%=total_igx_gross_expo%></td>
												<td align="right"><%=total_igx_gross_expo_incl_tax%></td>
												<td align="right"></td>
												<td align="right"></td>
												<td align="right"><%=total_igx_net_expo%></td>
												<td></td>
												<td align="right"><%=total_igx_limit %></td>
												<td align="right"><%=total_igx_cr_exceed %></td>
												<td align="right"><%=total_igx_net_expo_usd %></td>
												<td align="right"><%=total_igx_cr_exceed_usd%></td>
											</tr>
											<%} %>
											<%i=i+1;
											break;
										 }%>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="25">
											<div align="center"><%=utilmsg.infoMessage("<b>No Exposure for the Report Date!</b>")%></div>
										</td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<%} %>
				<%if(isEodProcessDone.equals("Y")){ %>
				<div class="modal-footer cdfooter">
					<div align="center"><%=utilmsg.infoMessage("<b>Exposure Data Freezed on "+eodProcessDoneOn+" for the Report Date "+report_dt+"</b>")%></div>					
				</div>
				<%} %>
			</div>
		</div>
	</div>
</div>

<!-- MODAL -->
<div class="modal fade" id="calcInfoModel" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader" id="modalHedingInfo">
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="calcTable">
							</table>
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
</body>
<script>
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
</html>