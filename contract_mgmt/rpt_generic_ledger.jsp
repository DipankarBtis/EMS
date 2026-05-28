<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
var newWindow;
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var url="rpt_ledger_contract_list.jsp?counterparty_cd="+counterparty_cd;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Supply Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Supply Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_type)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_generic_ledger.jsp?counterparty_cd="+countpty_cd+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&st_dt="+st_dt+"&end_dt="+end_dt+
			"&u="+u+"&contract_type="+cont_type;//+"&cargo_no="+cargo_no;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String yestdate = utildate.getPreviousDate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");

String st_dt = request.getParameter("st_dt")==null?"":request.getParameter("st_dt");
String en_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");


cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setCounterparty_cd(counterparty_cd);
cont_mgmt.setAgmt_no(agmt_no);
cont_mgmt.setAgmt_rev_no(agmt_rev_no);
cont_mgmt.setCont_no(cont_no);
cont_mgmt.setCont_rev_no(cont_rev_no);
cont_mgmt.setContract_type(contract_type);
cont_mgmt.setCallFlag("GENERIC_LEDGER");
cont_mgmt.init();

Vector VMST_COUNTERPARTY_CD = cont_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = cont_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = cont_mgmt.getVMST_COUNTERPARTY_ABBR();

Vector VGAS_DT = cont_mgmt.getVGAS_DT();
Vector VDCQ = cont_mgmt.getVDCQ();
Vector VCOLOR_ALLOC = cont_mgmt.getVCOLOR_ALLOC();
Vector VPRICE = cont_mgmt.getVPRICE();
Vector VPRICE_UNIT = cont_mgmt.getVPRICE_UNIT();
Vector VEXCHG_RATE = cont_mgmt.getVEXCHG_RATE();
Vector VLC = cont_mgmt.getVLC();
Vector VBG = cont_mgmt.getVBG();
Vector VADVANCE_AMT = cont_mgmt.getVADVANCE_AMT();
Vector VCASH_DEPOSIT = cont_mgmt.getVCASH_DEPOSIT();

Vector VLIMIT_VALUE = cont_mgmt.getVLIMIT_VALUE();
Vector VAPPROVED_EXCEED_VAL = cont_mgmt.getVAPPROVED_EXCEED_VAL();
Vector VTOTAL_ALLOWABLE_CREDIT = cont_mgmt.getVTOTAL_ALLOWABLE_CREDIT();
Vector VPCG_VALUE = cont_mgmt.getVPCG_VALUE();
Vector VGROSS_AMT = cont_mgmt.getVGROSS_AMT();
Vector VOTHER_CHRG_AMT = cont_mgmt.getVOTHER_CHRG_AMT();
Vector VTAX_AMT = cont_mgmt.getVTAX_AMT();
Vector VTOTAL_GROSS_AMT = cont_mgmt.getVTOTAL_GROSS_AMT();
Vector VNET_AMT = cont_mgmt.getVNET_AMT();
Vector VCUMULATIVE_NET_AMT = cont_mgmt.getVCUMULATIVE_NET_AMT();
Vector VTDS_AMT  = cont_mgmt.getVTDS_AMT();
Vector VPAY_RECEIVED_AMT = cont_mgmt.getVPAY_RECEIVED_AMT();
Vector VCUMULATIVE_PAY_RECEIVED_AMT = cont_mgmt.getVCUMULATIVE_PAY_RECEIVED_AMT();
Vector VBALANCE_AMT = cont_mgmt.getVBALANCE_AMT();

Vector VQTY_INFO = cont_mgmt.getVQTY_INFO();
Vector VADV_INFO  = cont_mgmt.getVADV_INFO();
Vector VADV_ADJUST = cont_mgmt.getVADV_ADJUST();
Vector VHOLD_AMT = cont_mgmt.getVHOLD_AMT();
Vector VPAY_TOTAL = cont_mgmt.getVPAY_TOTAL();
Vector VADV_ADJ_TOTAL = cont_mgmt.getVADV_ADJ_TOTAL();
Vector VCUMULATIVE_ADV_ADJ_AMT = cont_mgmt.getVCUMULATIVE_ADV_ADJ_AMT();
Vector VOTHER_INV_AMT = cont_mgmt.getVOTHER_INV_AMT();


String cont_ref_no=cont_mgmt.getCont_ref_no();
String dealDisplayMap=cont_mgmt.getDealDisplayMap();
String counterparty_nm=cont_mgmt.getCounterparty_nm();
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
					    	Generic Ledger
					    </div>
					    <a href="../contract_mgmt/xls_generic_ledger.jsp?fileName=Generic_Ledger.xls&counterparty_cd=<%=counterparty_cd%>
					    &cont_no=<%=cont_no %>&cont_rev_no=<%=cont_rev_no%>&agmt_no=<%=agmt_no%>&agmt_rev_no=<%=agmt_rev_no %>
					    &contract_type=<%=contract_type%>&st_dt=<%=st_dt%>&end_dt=<%=en_dt%>" download="Generic Ledger">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
							</div>
						</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh();">
										<option value="">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i)%></option>
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
				    				<input type="button" class="btn btn-info btn-sm select_btn" value="Select Contract" onclick="openContList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<textarea class="form-control" rows="4" readonly style="font-weight:bold;">Counterparty : <%=counterparty_nm %>&#13;&#10;Deal Map : <%=dealDisplayMap%>&#13;&#10;Cont/Trade Ref : <%=cont_ref_no%>&#13;&#10;Contract Duration : <%=st_dt%> - <%=en_dt%></textarea>				    				
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr>
											<th rowspan="2">Gas Date</th>
											<th rowspan="2">Exchange Rate<br>(INR/USD)</th>
											<th colspan="8">Allowable Credit (INR)</th>
											<th colspan="9">Consumed Credit | Exposure (INR)</th>											
											<th colspan="5">Payment Received Details (INR)</th>
											<th colspan="4">Advance Adjustment Details (INR)</th>
											<th rowspan="2">Balance (INR)</th>																						
											<th rowspan="2">Rule</th>
										</tr>					
										<tr>
											<th>Credit Exceed Days</th>
											<th>LC</th>
											<th>BG</th>
											<th>Cash Deposit</th>
											<th>Advance</th>
											<th title="Deal Sepecific PCG only">PCG</th> <!-- Uncapped PCG logic not integrated -->
											<th title="Limit logic not integrated" >Limit</th>
											<th>Total</th>
											<th>Qty</th>
											<th>Gas Price /MMBTU</th>
											<th>Gross Amount</th>
											<th title="Weighted Avg. of applicable Transportation Charges, Marketing Margin, Other Charges">Eff. Other Charges</th>
											<th>Total Gross Amount</th>
											<th>Tax</th>
											<th>Net Amount</th>
											<th>Other Invoiced Net Amount</th>
											<th>Cumulative<br>Net Amount</th>
											<th title="Nutralizing on first payment received">TDS</th>
											<th>Payment Received</th>
											<th>Advance Adj. Payment</th>
											<th title="=TDS + Payment Received + Advance Adj. Payment">Total Recv. Payment</th>
											<th>Cumulative<br>Recv. Payment</th>
											<th>Hold Amount</th>												
											<th>Advance Adj. Payment</th>
											<th title="= Advance Adj. Payment + Hold Amount">Total Adj. Advance</th>
											<th>Cumulative<br>Adj. Advance</th>										
										</tr>
									</thead>
									<tbody>s
									<%if(VGAS_DT.size() > 0){ %>
										<%for(int i=0; i<VGAS_DT.size(); i++){ %>
										<tr <%if(VGAS_DT.elementAt(i).equals(yestdate)){%> style="background: #a6ff4d;" <%}%>>										
											<td align="center"><%=VGAS_DT.elementAt(i)%></td>
											<td align="right"><%=VEXCHG_RATE.elementAt(i)%></td>
											<td align="right"><%=VAPPROVED_EXCEED_VAL.elementAt(i)%></td>
											<td align="right"><%=VLC.elementAt(i)%></td>
											<td align="right"><%=VBG.elementAt(i)%></td>
											<td align="right"><%=VCASH_DEPOSIT.elementAt(i) %></td>
											<td align="right" title="<%=VADV_INFO.elementAt(i)%>"><%=VADVANCE_AMT.elementAt(i) %></td>
											<td align="right"><%=VPCG_VALUE.elementAt(i) %></td>
											<td align="right"><%=VLIMIT_VALUE.elementAt(i) %></td>
											<td align="right" style="font-weight: bold;"><%=VTOTAL_ALLOWABLE_CREDIT.elementAt(i)%></td>
											<td align="right" title="<%=VQTY_INFO.elementAt(i)%>" style="color: <%=VCOLOR_ALLOC.elementAt(i)%>;"><%=VDCQ.elementAt(i)%></td>
											<td align="right"><%=VPRICE.elementAt(i)%> <%=VPRICE_UNIT.elementAt(i)%></td>
											<td align="right"><%=VGROSS_AMT.elementAt(i)%></td>
											<td align="right"><%=VOTHER_CHRG_AMT.elementAt(i)%></td>
											<td align="right"><%=VTOTAL_GROSS_AMT.elementAt(i)%></td>
											<td align="right"><%=VTAX_AMT.elementAt(i)%></td>
											<td align="right"><%=VNET_AMT.elementAt(i)%></td>
											<td align="right"><%=VOTHER_INV_AMT.elementAt(i)%></td>
											<td align="right" style="font-weight: bold;"><%=VCUMULATIVE_NET_AMT.elementAt(i)%></td>
											<td align="right"><%=VTDS_AMT.elementAt(i)%></td>
											<td align="right"><%=VPAY_RECEIVED_AMT.elementAt(i) %></td>
											<td align="right"><%=VADV_ADJUST.elementAt(i)%></td>
											<td align="right"><%=VPAY_TOTAL.elementAt(i) %></td>
											<td align="right" style="font-weight: bold;"><%=VCUMULATIVE_PAY_RECEIVED_AMT.elementAt(i)%></td>
											<td align="right"><%=VHOLD_AMT.elementAt(i) %></td>
											<td align="right"><%=VADV_ADJUST.elementAt(i)%></td>																						
											<td align="right"><%=VADV_ADJ_TOTAL.elementAt(i)%></td>											
											<td align="right" style="font-weight: bold;"><%=VCUMULATIVE_ADV_ADJ_AMT.elementAt(i) %></td>
											<td align="right" style="font-weight: bold; <%if(VBALANCE_AMT.elementAt(i).toString().contains("-") ){%> color: red; <%}else{%>color: green;<%}%>"><%=VBALANCE_AMT.elementAt(i)%></td>
											<td></td>
										</tr>
										<%} %>
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

<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<input type="hidden" name="u" value="<%=u%>">

</form>
</body>
</html>