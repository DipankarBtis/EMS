<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function goBack()
{
	var u = document.forms[0].u.value;
	
	var a = confirm("Are You sure You want to go Back?")
	if(a)
	{
		var url = "rpt_mr_exposure.jsp?u="+u;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}

function exportToXls()
{
	var report_dt = document.forms[0].report_dt.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var account = document.forms[0].account.value;
	var contract_type = document.forms[0].contract_type.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev_no = document.forms[0].agmt_rev_no.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev_no = document.forms[0].cont_rev_no.value;
	var cargo_no = document.forms[0].cargo_no.value;
	var legalEntity = document.forms[0].legalEntity.value;
	
	var url = "xls_mr_deal_dtl.jsp?report_dt="+report_dt+"&counterparty_cd="+counterparty_cd+
		"&account="+account+"&contract_type="+contract_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+	
		"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&cargo_no="+cargo_no+"&legalEntity="+legalEntity;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DB_MR_ExposureReport" id="market_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String report_dt=request.getParameter("report_dt")==null?"":request.getParameter("report_dt");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String account = request.getParameter("account")==null?"0":request.getParameter("account");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
String legalEntity=request.getParameter("legalEntity")==null?"":request.getParameter("legalEntity");

market_risk.setCallFlag("EXPOSURE_CONTRACT_DTL");
market_risk.setReport_dt(report_dt);
market_risk.setCounterparty_cd(counterparty_cd);
market_risk.setAccount(account);
market_risk.setContract_type(contract_type);
market_risk.setAgmt_no(agmt_no);
market_risk.setAgmt_rev_no(agmt_rev_no);
market_risk.setCont_no(cont_no);
market_risk.setCont_rev_no(cont_rev_no);
market_risk.setComp_cd(legalEntity);
market_risk.setCargo_no(cargo_no);
market_risk.init();

String counterparty_nm = market_risk.getCounterparty_nm();
String display_deal_mapp = market_risk.getDisplay_deal_mapp();
String deal_ref_no = market_risk.getDeal_ref_no();
String deal_dt = market_risk.getDeal_dt();
String start_dt = market_risk.getStart_dt();
String end_dt = market_risk.getEnd_dt();
String entered_by = market_risk.getEntered_by();
String entered_dt = market_risk.getEntered_dt();
String approved_by = market_risk.getApproved_by();
String approved_dt = market_risk.getApproved_dt();
String qty_unit = market_risk.getQty_unit();
String qty_unit_nm = market_risk.getQty_unit_nm();

// Totalization
String total=market_risk.getTotal();
String fin_realized_leg=market_risk.getFin_realized_leg();
String fin_unrealized_leg=market_risk.getFin_unrealized_leg();
String phy_unrealized_leg=market_risk.getPhy_unrealized_leg();
String phy_expo_unrealized=market_risk.getPhy_expo_unrealized();
String fin_expo_unrealized=market_risk.getFin_expo_unrealized();
String phy_expo_realized=market_risk.getPhy_expo_realized();
String fin_expo_realized=market_risk.getFin_expo_realized();
String phy_expo_ori=market_risk.getPhy_expo_ori();
String fin_expo_ori=market_risk.getFin_expo_ori();
String total_dcq=market_risk.getTotal_dcq();
String total_actual_qty=market_risk.getTotal_actual_qty();

Vector VGAS_DT = market_risk.getVGAS_DT();
Vector VDCQ = market_risk.getVDCQ();
Vector VDCQ_COLOR = market_risk.getVDCQ_COLOR();
Vector VACTUAL_QTY = market_risk.getVACTUAL_QTY();
Vector VPRICE_TYPE = market_risk.getVPRICE_TYPE();
Vector VEFF_RATE = market_risk.getVEFF_RATE();
Vector VEFF_RATE_INFO = market_risk.getVEFF_RATE_INFO();

Vector VPHY_EXPO_ORIGINAL = market_risk.getVPHY_EXPO_ORIGINAL();
Vector VFIN_EXPO_ORIGINAL = market_risk.getVFIN_EXPO_ORIGINAL();
Vector VPHY_EXPO_ORIGINAL_INFO = market_risk.getVPHY_EXPO_ORIGINAL_INFO();
Vector VFIN_EXPO_ORIGINAL_INFO = market_risk.getVFIN_EXPO_ORIGINAL_INFO();

Vector VPHY_REALIZED_UNREALIZED = market_risk.getVPHY_REALIZED_UNREALIZED();
Vector VFIN_REALIZED_UNREALIZED = market_risk.getVFIN_REALIZED_UNREALIZED();

Vector VPHY_EXPO_REALIZED = market_risk.getVPHY_EXPO_REALIZED();
Vector VFIN_EXPO_REALIZED = market_risk.getVFIN_EXPO_REALIZED();
Vector VPHY_EXPO_REALIZED_INFO = market_risk.getVPHY_EXPO_REALIZED_INFO();
Vector VFIN_EXPO_REALIZED_INFO = market_risk.getVFIN_EXPO_REALIZED_INFO();

Vector VPHY_EXPO_UNREALIZED = market_risk.getVPHY_EXPO_UNREALIZED();
Vector VFIN_EXPO_UNREALIZED = market_risk.getVFIN_EXPO_UNREALIZED();
Vector VPHY_EXPO_UNREALIZED_INFO = market_risk.getVPHY_EXPO_UNREALIZED_INFO();
Vector VFIN_EXPO_UNREALIZED_INFO = market_risk.getVFIN_EXPO_UNREALIZED_INFO();

Vector VSETTLE_PRICE = market_risk.getVSETTLE_PRICE();
Vector VSLOPE = market_risk.getVSLOPE();
Vector VCONST = market_risk.getVCONST();
Vector VPHY_FORWARD_PRICE = market_risk.getVPHY_FORWARD_PRICE();
Vector VFIN_FORWARD_PRICE = market_risk.getVFIN_FORWARD_PRICE();

Vector VPHY_UNREALIZED_LEG = market_risk.getVPHY_UNREALIZED_LEG();
Vector VFIN_UNREALIZED_LEG = market_risk.getVFIN_UNREALIZED_LEG();
Vector VFIN_REALIZED_LEG = market_risk.getVFIN_REALIZED_LEG();

Vector VPHY_UNREALIZED_LEG_INFO = market_risk.getVPHY_UNREALIZED_LEG_INFO();
Vector VFIN_UNREALIZED_LEG_INFO = market_risk.getVFIN_UNREALIZED_LEG_INFO();
Vector VFIN_REALIZED_LEG_INFO = market_risk.getVFIN_REALIZED_LEG_INFO();

Vector VTOTAL = market_risk.getVTOTAL();
Vector VCONT_MMYYYY = market_risk.getVCONT_MMYYYY();

Vector VFIN_MMYYYY = market_risk.getVFIN_MMYYYY();
Vector VSETTLE_START_DT = market_risk.getVSETTLE_START_DT();
Vector VSETTLE_END_DT = market_risk.getVSETTLE_END_DT();

Vector VCONTRACT_PRICE = market_risk.getVCONTRACT_PRICE();
Vector VSR = market_risk.getVSR();
Vector VROW_COLOR = market_risk.getVROW_COLOR();

Vector VTOTAL_CHARGE = market_risk.getVTOTAL_CHARGE();

Vector VPHYS_CURVE_NM_DTL = market_risk.getVPHYS_CURVE_NM_DTL();
%>
<body>
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
					    	<%=counterparty_nm %> (<%=display_deal_mapp %>) Exposure Report [Date: <%=report_dt %>]
					    </div>
					    <div class="row justify-content-end">
							<!-- <div class="col-auto">
				    			<span class="input-group-text" title="Back" onclick="goBack();"><i class="fa fa-step-backward fa-2x"></i></span>
				    		</div> -->
							<div class="col-auto">
							    <div align="right" onclick="exportToXls();" style="color:green;">
									<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-10 col-xs-10 col-md-10">
							&nbsp;<span
									<%if(account.equals("Buy")){ %>
 										class="alert" style="background: #ffccff; color: #cc00cc;"
 									<%}else { %>
 										class="alert alert-primary"
 									<%}%>><b><%=account %></b></span> <b>Contract#:</b>	<%=display_deal_mapp %> <br>
							&nbsp;<b>Contract/Trade Ref#:</b> <%=deal_ref_no %> <br>
							&nbsp;<b>Contract Period:</b> <%=start_dt %>-<%=end_dt %><br>
							&nbsp;<b>Contract Signing Date:</b> <%=deal_dt %> <br>
							&nbsp;<b>Contract Entered By:</b> <%=entered_by %> On <%=entered_dt %> <br>
							&nbsp;<b>Contract Approved By:</b> <%=approved_by %> On <%=approved_dt %>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="d-flex justify-content-end">
								<div class="form-group row">
									<div class="col-auto">
										<!-- <div align="right" onclick="exportToXls();" style="color:green;">
											<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
										</div> -->
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="filterbysearch">
								<thead id="tbsearch">
									<tr>										
										<th rowspan="2">Sr#</th>
										<th rowspan="2">Gas Day</th>
										<th rowspan="2" >Contract Month <br>MM/YYYY</th>										
										<th rowspan="2">DCQ <br>(<%=qty_unit_nm%>)</th>
										<th rowspan="2"  style="background: #000066; color: white;">Allocated Quantity<br>(<%=qty_unit_nm%>)</th>
										<th rowspan="2">Physical Curve</th>										
										<th rowspan="2">Financial Price Type</th>
										<th rowspan="2" title="Gas Price + WA Charges">Contract Price ($/<%=qty_unit_nm%>)</th>
										<th colspan="4" style="color: blue;">Settle Price Details</th>
										<th colspan="2"> Realized/Unrealized</th>										
										<th colspan="2" style="color: #ff0080;">Forward Price</th>	
										<th rowspan="2">Slope</th>
										<th rowspan="2" title="Eff. Constant = Constant + (Premium/Discount * Slope)">Eff. Constant</th>
										<!-- <th rowspan="2">Total Charge ($/<%=qty_unit_nm%>)</th>		 -->								
										<th rowspan="2"  style="background: #000066; color: white;" title="{(Settle Price * Slope) + Constant} + WA Charges"> Effective<br>Contract Price<br>($/<%=qty_unit_nm%>)</th>										
										<th colspan="2"> Original Exposure (<%=qty_unit_nm%>)</th>																		
										<th colspan="2"> Unrealized Exposure (<%=qty_unit_nm%>)</th>
										<th colspan="2"> Realized Exposure (<%=qty_unit_nm%>)</th>
										<th rowspan="2" style="background: #f9ecf2;" title="Unrealized (Phy.) Exposure * {Forward Price (Phy.) + WA Charges}"> Unrealized Physical Leg($)</th>
										<th rowspan="2" style="background: #ffe6ff;" title="Unrealized (Fin.) Exposure * {(Forward Price (Fin.) + Constant/Slope) + (WA Charges/Slope)}"> Unrealized Financial Leg($)</th>
										<th rowspan="2" style="background: #000066; color: white;" title="Allocated <%=qty_unit_nm%> * Effective Contract Price"> Realized Financial Leg($)</th>
										<th rowspan="2" title="Unrealized (Phy. +Fin.) + Realized Fin.">Total($)</th>
									</tr>
									<tr>	
										<th>Fin. Month <br>MM/YYYY</th>										
										<th title="Settle Period Start Date for the Financial Month">Start Date</th>
										<th title="Settle Period End Date for the Financial Month">End Date</th>
										<th style="background: #e6e6ff;" title="Avg of SPOT prices for Settle Period (NB. Slope decides conversion to $/<%=qty_unit_nm%>)">Settled Price <br> ($/UNIT)</th>
										<th>Physical Leg</th>		
										<th>Financial Leg</th>
										<th style="background: #f9ecf2;">Physical Curve ($/<%=qty_unit_nm%>)</th>																			
										<th style="background: #ffe6ff;">Financial Curve ($/UNIT)</th>	
										<th title="DCQ">Physical Leg</th>		
										<th title="DCQ * Slope">Financial Leg</th>																				
										<th>Physical Leg</th>		
										<th title="(DCQ/Working days of contract Month) * remaining working days">Financial Leg</th>	
										<th>Physical Leg</th>		
										<th title="(DCQ/Working days of contract Month) * working days till report date">Financial Leg</th>																																																												
									</tr>
								</thead>
								<tbody>
								<%if(VGAS_DT.size()>0){ %>
									<%for(int i=0; i<VGAS_DT.size(); i++){ %>
									<tr <%if(VGAS_DT.elementAt(i).equals(report_dt)){%> style="background: #a6ff4d;" 
									<%} else {%>style="background:<%=VROW_COLOR.elementAt(i)%>" <%} %>> 					
										<td align="center"><%=VSR.elementAt(i)%></td>
										<td align="center"><%=VGAS_DT.elementAt(i) %></td>
										<td align="center"><%=VCONT_MMYYYY.elementAt(i) %></td>
										<td align="right"><font color="<%=VDCQ_COLOR.elementAt(i)%>"><%=VDCQ.elementAt(i) %></font></td>
										<td align="right" style="background: #b3f0ff;"><%=VACTUAL_QTY.elementAt(i) %></td>										
										<td align="center"><%=VPHYS_CURVE_NM_DTL.elementAt(i)%></td>
										<td><%=VPRICE_TYPE.elementAt(i) %></td>					
										<td align="right" <%if(!VCONTRACT_PRICE.elementAt(i).equals(VEFF_RATE.elementAt(i))){%> style= "color: red;"<%} %>><%=VCONTRACT_PRICE.elementAt(i)%></td>
										<td align="center"><%=VFIN_MMYYYY.elementAt(i) %></td>
										<td align="center"><%=VSETTLE_START_DT.elementAt(i) %></td>									
										<td align="center"><%=VSETTLE_END_DT.elementAt(i) %></td>
										<td align="right" style="background: #e6e6ff;"><%=VSETTLE_PRICE.elementAt(i)%></td>
										<td align="center"
											<%if(VPHY_REALIZED_UNREALIZED.elementAt(i).equals("R")){ %> 
												style="color: blue;"
											<%}else{ %>	
												style="color: #ff0080;"
											<%} %>	>
											<b><%=VPHY_REALIZED_UNREALIZED.elementAt(i)%></b>
										</td>
										<td align="center"
										<%if(VFIN_REALIZED_UNREALIZED.elementAt(i).equals("R")){ %> 
												style="color: blue;"
											<%}else{ %>	
												style="color: #ff0080;"
											<%} %>	>
											<b><%=VFIN_REALIZED_UNREALIZED.elementAt(i)%></b>
										</td>																			
										<td align="right" style="background: #f9ecf2;"><%=VPHY_FORWARD_PRICE.elementAt(i) %></td>
										<td align="right" style="background: #ffe6ff;"><%=VFIN_FORWARD_PRICE.elementAt(i) %></td>
										<td align="right"><%=VSLOPE.elementAt(i) %></td>
										<td align="right"><%=VCONST.elementAt(i) %></td>
										<%-- <td align="right"><%=VTOTAL_CHARGE.elementAt(i) %></td> --%>
										<td align="right" style="background: #b3f0ff;" title="<%=VEFF_RATE_INFO.elementAt(i)%>"><%=VEFF_RATE.elementAt(i)%></td>						
										<td align="right" title="<%=VPHY_EXPO_ORIGINAL_INFO.elementAt(i)%>"><%=VPHY_EXPO_ORIGINAL.elementAt(i)%></td>
										<td align="right" title="<%=VFIN_EXPO_ORIGINAL_INFO.elementAt(i)%>"><%=VFIN_EXPO_ORIGINAL.elementAt(i)%></td>										
										<td align="right" style="color: #ff0080;" title="<%=VPHY_EXPO_UNREALIZED_INFO.elementAt(i)%>"><%=VPHY_EXPO_UNREALIZED.elementAt(i)%></td>
										<td align="right" style="color: #ff0080;" title="<%=VFIN_EXPO_UNREALIZED_INFO.elementAt(i)%>"><%=VFIN_EXPO_UNREALIZED.elementAt(i)%></td>
										<td align="right" style="color: blue;" title="<%=VPHY_EXPO_REALIZED_INFO.elementAt(i)%>"><%=VPHY_EXPO_REALIZED.elementAt(i)%></td>
										<td align="right" style="color: blue;" title="<%=VFIN_EXPO_REALIZED_INFO.elementAt(i)%>"><%=VFIN_EXPO_REALIZED.elementAt(i)%></td>
										<td align="right" style="background: #f9ecf2;" title="<%=VPHY_UNREALIZED_LEG_INFO.elementAt(i)%>">
											<%if (!VPHY_EXPO_UNREALIZED.elementAt(i).equals("")) {%>
												<%=VPHY_UNREALIZED_LEG.elementAt(i) %>
											<%} %>
										</td>
										<td align="right" style="background: #ffe6ff;" title="<%=VFIN_UNREALIZED_LEG_INFO.elementAt(i)%>">
											<%if (!VFIN_EXPO_UNREALIZED.elementAt(i).equals("")) {%>
												<%=VFIN_UNREALIZED_LEG.elementAt(i) %>
											<%} %>
										</td>
										<td align="right" title="<%=VFIN_REALIZED_LEG_INFO.elementAt(i)%>" 
											<%if((VPHY_REALIZED_UNREALIZED.elementAt(i).equals("U")) || (VFIN_REALIZED_UNREALIZED.elementAt(i).equals("U"))){ %> 												
												style="color: blue; background: #b3f0ff;"
											<%} else { %>
												style="background: #b3f0ff;" 
											<%}%>><%=VFIN_REALIZED_LEG.elementAt(i) %></td>
										<td align="right" 
											<%if((VPHY_REALIZED_UNREALIZED.elementAt(i).equals("U")) || (VFIN_REALIZED_UNREALIZED.elementAt(i).equals("U"))){ %> 												
												style="color: #ff0080;"
											<%} %>><%=VTOTAL.elementAt(i) %></td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="29">
											<div align="center"><%=utilmsg.infoMessage("<b>No Exposure for the Report Date!</b>")%></div>
										</td>
									</tr>
								<%} %>	
									<tr style="background: #000066; color: white; font-weight: bold;">
										<td colspan="3" align="center"><b>Total</b></td>
										<td align="right"><%=total_dcq%></td>
										<td align="right"><%=total_actual_qty%></td>
										<td colspan="14" align="center"></td>
										<td align="right"><%=phy_expo_ori%></td>
										<td align="right"><%=fin_expo_ori%></td>
										<td align="right"><%=phy_expo_unrealized%></td>
										<td align="right"><%=fin_expo_unrealized%></td>
										<td align="right"><%=phy_expo_realized%></td>
										<td align="right"><%=fin_expo_realized %></td>
										<td align="right"><%=phy_unrealized_leg %></td>
										<td align="right"><%=fin_unrealized_leg %></td>
										<td align="right"><%=fin_realized_leg%></td>
										<td align="right"><%=total%></td>
									</tr>
								</tbody>								
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>							

<input type="hidden" name="report_dt" value="<%=report_dt%>">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="account" value="<%=account%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">  	
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="cargo_no" value="<%=cargo_no%>">
<input type="hidden" name="legalEntity" value="<%=legalEntity%>">

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