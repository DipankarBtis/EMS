<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DB_MR_ExposureReport" id="market_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

String report_dt=request.getParameter("report_dt")==null?"":request.getParameter("report_dt");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String account = request.getParameter("account")==null?"0":request.getParameter("account");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");

market_risk.setCallFlag("EXPOSURE_CONTRACT_DTL");
market_risk.setReport_dt(report_dt);
market_risk.setCounterparty_cd(counterparty_cd);
market_risk.setAccount(account);
market_risk.setContract_type(contract_type);
market_risk.setAgmt_no(agmt_no);
market_risk.setAgmt_rev_no(agmt_rev_no);
market_risk.setCont_no(cont_no);
market_risk.setCont_rev_no(cont_rev_no);
market_risk.setComp_cd(owner_cd);
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

// Totalization
String total=market_risk.getTotal();
String fin_realized_leg=market_risk.getFin_realized_leg();
String fin_unrealized_leg=market_risk.getFin_unrealized_leg();
String phy_unrealized_leg=market_risk.getPhy_unrealized_leg();
String phy_expo_ori=market_risk.getPhy_expo_ori();
String fin_expo_ori=market_risk.getFin_expo_ori();
String phy_expo_unrealized=market_risk.getPhy_expo_unrealized();
String fin_expo_unrealized=market_risk.getFin_expo_unrealized();
String phy_expo_realized=market_risk.getPhy_expo_realized();
String fin_expo_realized=market_risk.getFin_expo_realized();
String total_dcq=market_risk.getTotal_dcq();
String total_actual_qty=market_risk.getTotal_actual_qty();

Vector VGAS_DT = market_risk.getVGAS_DT();
Vector VDCQ = market_risk.getVDCQ();
Vector VDCQ_COLOR = market_risk.getVDCQ_COLOR();
Vector VACTUAL_QTY = market_risk.getVACTUAL_QTY();
Vector VPRICE_TYPE = market_risk.getVPRICE_TYPE();
Vector VEFF_RATE = market_risk.getVEFF_RATE();

Vector VPHY_REALIZED_UNREALIZED = market_risk.getVPHY_REALIZED_UNREALIZED();
Vector VFIN_REALIZED_UNREALIZED = market_risk.getVFIN_REALIZED_UNREALIZED();
Vector VPHY_EXPO_REALIZED = market_risk.getVPHY_EXPO_REALIZED();
Vector VFIN_EXPO_REALIZED = market_risk.getVFIN_EXPO_REALIZED();
Vector VPHY_EXPO_UNREALIZED = market_risk.getVPHY_EXPO_UNREALIZED();
Vector VFIN_EXPO_UNREALIZED = market_risk.getVFIN_EXPO_UNREALIZED();
Vector VPHY_EXPO_ORIGINAL = market_risk.getVPHY_EXPO_ORIGINAL();
Vector VFIN_EXPO_ORIGINAL = market_risk.getVFIN_EXPO_ORIGINAL();

Vector VSETTLE_PRICE = market_risk.getVSETTLE_PRICE();
Vector VSLOPE = market_risk.getVSLOPE();
Vector VCONST = market_risk.getVCONST();
Vector VPHY_FORWARD_PRICE = market_risk.getVPHY_FORWARD_PRICE();
Vector VFIN_FORWARD_PRICE = market_risk.getVFIN_FORWARD_PRICE();

Vector VPHY_UNREALIZED_LEG = market_risk.getVPHY_UNREALIZED_LEG();
Vector VFIN_UNREALIZED_LEG = market_risk.getVFIN_UNREALIZED_LEG();
Vector VFIN_REALIZED_LEG = market_risk.getVFIN_REALIZED_LEG();
Vector VTOTAL = market_risk.getVTOTAL();
Vector VCONT_MMYYYY = market_risk.getVCONT_MMYYYY();

Vector VFIN_MMYYYY = market_risk.getVFIN_MMYYYY();
Vector VSETTLE_START_DT = market_risk.getVSETTLE_START_DT();
Vector VSETTLE_END_DT = market_risk.getVSETTLE_END_DT();
Vector VCONTRACT_PRICE = market_risk.getVCONTRACT_PRICE();

Vector VSR = market_risk.getVSR();
Vector VROW_COLOR = market_risk.getVROW_COLOR();
Vector VPHYS_CURVE_NM_DTL = market_risk.getVPHYS_CURVE_NM_DTL();

String file_nm="MR_Exposure_Deal_Dtl.xls";
%>
<body>
<%
	response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "inline; filename="+file_nm);
    
%>
<font style="font-size: 16px;"><b><%=counterparty_nm %> (<%=display_deal_mapp %>) Exposure Report [Date: <%=report_dt %>]</b></font>
<br>
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
<br>
<table border='1'>
	<thead>
		<tr>
			<th rowspan="2">Sr#</th>
			<th rowspan="2">Gas Day</th>
			<th rowspan="2" >Contract Month <br>MM/YYYY</th>										
			<th rowspan="2">DCQ <br>(MMBTU)</th>
			<th rowspan="2"  style="background: #000066; color: white;">Allocated <br>MMBTU</th>										
			<th rowspan="2">Physical Curve</th>
			<th rowspan="2">Financial Price Type</th>
			<th rowspan="2">Contract Price ($/MMBTU)</th>
			<th colspan="4" style="color: blue;">Settle Price Details</th>
			<th colspan="2"> Realized/Unrealized</th>										
			<th colspan="2" style="color: #ff0080;">Forward Price</th>	
			<th rowspan="2">Slope</th>
			<th rowspan="2">Constant</th>										
			<th rowspan="2"  style="background: #000066; color: white;"> Effective<br>Contract Price<br>($/MMBTU)</th>			
			<th colspan="2"> Original Exposure (MMBTU)</th>																		
			<th colspan="2"> Unrealized Exposure (MMBTU)</th>
			<th colspan="2"> Realized Exposure (MMBTU)</th>
			<th rowspan="2" style="background: #f9ecf2;"> Unrealized Physical Leg($)</th>
			<th rowspan="2" style="background: #ffe6ff;"> Unrealized Financial Leg($)</th>
			<th rowspan="2" style="background: #000066; color: white;"> Realized Financial Leg($)</th>
			<th rowspan="2">Total($)</th>
		</tr>
		<tr>	
			<th>Fin. Month <br>MM/YYYY</th>										
			<th>Start Date</th>
			<th>End Date</th>
			<th style="background: #e6e6ff;">Settled Price <br> ($/UNIT)</th>
			<th>Physical Leg</th>		
			<th>Financial Leg</th>
			<th style="background: #f9ecf2;">Physical Curve ($/MMBTU)</th>																			
			<th style="background: #ffe6ff;">Financial Curve ($/UNIT)</th>	
			<th>Physical Leg</th>		
			<th>Financial Leg</th>																				
			<th>Physical Leg</th>		
			<th>Financial Leg</th>	
			<th>Physical Leg</th>		
			<th>Financial Leg</th>																																																																																																			
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
			<td align="right" style="background: #b3f0ff;" title="<%//=VEFF_RATE_INFO.elementAt(i)%>"><%=VEFF_RATE.elementAt(i)%></td>				
			<td align="right" title="<%//=VPHY_EXPO_ORIGINAL_INFO.elementAt(i)%>"><%=VPHY_EXPO_ORIGINAL.elementAt(i)%></td>
			<td align="right" title="<%//=VFIN_EXPO_ORIGINAL_INFO.elementAt(i)%>"><%=VFIN_EXPO_ORIGINAL.elementAt(i)%></td>										
			<td align="right" style="color: #ff0080;" title="<%//=VPHY_EXPO_UNREALIZED_INFO.elementAt(i)%>"><%=VPHY_EXPO_UNREALIZED.elementAt(i)%></td>
			<td align="right" style="color: #ff0080;" title="<%//=VFIN_EXPO_UNREALIZED_INFO.elementAt(i)%>"><%=VFIN_EXPO_UNREALIZED.elementAt(i)%></td>
			<td align="right" style="color: blue;" title="<%//=VPHY_EXPO_REALIZED_INFO.elementAt(i)%>"><%=VPHY_EXPO_REALIZED.elementAt(i)%></td>
			<td align="right" style="color: blue;" title="<%//=VFIN_EXPO_REALIZED_INFO.elementAt(i)%>"><%=VFIN_EXPO_REALIZED.elementAt(i)%></td>
			<td align="right" style="background: #f9ecf2;" title="<%//=VPHY_UNREALIZED_LEG_INFO.elementAt(i)%>">
				<%if (!VPHY_EXPO_UNREALIZED.elementAt(i).equals("")) {%>
					<%=VPHY_UNREALIZED_LEG.elementAt(i) %>
				<%} %>
			</td>
			<td align="right" style="background: #ffe6ff;" title="<%//=VFIN_UNREALIZED_LEG_INFO.elementAt(i)%>">
				<%if (!VFIN_EXPO_UNREALIZED.elementAt(i).equals("")) {%>
					<%=VFIN_UNREALIZED_LEG.elementAt(i) %>
				<%} %>
			</td>
			<td align="right" title="<%//=VFIN_REALIZED_LEG_INFO.elementAt(i)%>" 
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
			<td colspan="26">
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
											

</body>
</html>