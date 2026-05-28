<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
</script>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_VariablePricing" id="market_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String cont_end_dt=request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String cont_ref=request.getParameter("cont_ref")==null?"":request.getParameter("cont_ref");
String cont_status=request.getParameter("cont_status")==null?"":request.getParameter("cont_status");
String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
String cn_contract_status=request.getParameter("cn_contract_status")==null?"":request.getParameter("cn_contract_status");

String contract_no ="";
if(contract_type.equalsIgnoreCase("S"))
{
	contract_no = contract_type+""+agmt_no+"-"+cont_no+"-"+cargo_no;
}
else if(contract_type.equalsIgnoreCase("S"))
{
	contract_no = contract_type+""+agmt_no+"-"+cont_no;
}
else
{
	contract_no = contract_type+""+cont_no;
}


String mapping_id=counterparty_cd+"-"+agmt_no+"-"+cont_no;
if(contract_type.equals("N"))
{
	mapping_id+="-"+cargo_no;
}

market_risk.setCallFlag("VARIABLE_PRICE_INFO");
market_risk.setCounterparty_cd(counterparty_cd);
market_risk.setComp_cd(owner_cd);
market_risk.setAgmt_no(agmt_no);
market_risk.setAgmt_rev_no(agmt_rev_no);
market_risk.setCont_no(cont_no);
market_risk.setCont_rev_no(cont_rev_no);
market_risk.setContract_type(contract_type);
market_risk.setCargo_no(cargo_no);
market_risk.setMapping_id(mapping_id);
market_risk.setStart_dt(cont_start_dt);
market_risk.setEnd_dt(cont_end_dt);
market_risk.init();

String counterparty_abbr=market_risk.getCounterparty_abbr();
String display_map_id = market_risk.getDisplay_map_id();

Vector VDELVH_MONTH = market_risk.getVDELVH_MONTH();
Vector VDURATION = market_risk.getVDURATION();
Vector VCURVE_NM = market_risk.getVCURVE_NM();
Vector VSETTLE_PRICE = market_risk.getVSETTLE_PRICE();
Vector VPRICE_TYPE = market_risk.getVPRICE_TYPE();
Vector VEFF_PRICE = market_risk.getVEFF_PRICE();
Vector VPREM_DISC_RATE = market_risk.getVPREM_DISC_RATE();
Vector VFINAL_PRICE = market_risk.getVFINAL_PRICE();
Vector VPRICE_DECI = market_risk.getVPRICE_DECI();
Vector VFINAL_PRICE_DECI = market_risk.getVFINAL_PRICE_DECI();
%>
<body>
<%@ include file="../home/loading.jsp"%>
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
					    	Price Detail <%=counterparty_abbr%> (<%=display_map_id%>) [Contract Period : <%=cont_start_dt %> - <%=cont_end_dt %>] as on <%=sysdate %>
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Delivery Month</th>										
										<th>Price Type</th>										
										<th>Duration</th>
										<th>Curve Name</th>
										<!-- <th>Settle Price</th>
										<th>Eff. Price</th>	 -->									
										<!-- <th>Premium(+)/Discount(-)</th>	 -->									
										<th>Final Eff. Price</th>										
										<!-- <th>Settle Price/Mth Logic</th>
										<th>Slope</th>
										<th>Constant</th>
										<th>Physical Curve</th>
										<th>Price Formula</th>
										<th>Remark</th> -->
									</tr>
								</thead>
								<tbody>
									<%if(VDELVH_MONTH.size()>0){%>
										<%for(int i=0;i<VDELVH_MONTH.size();i++){%>
											<tr>
												<td align="center"><%=VDELVH_MONTH.elementAt(i) %></td>
												<td align="center"><%=VPRICE_TYPE.elementAt(i) %></td>
												<td align="center"><%=VDURATION.elementAt(i) %></td>
												<td align="center"><%=VCURVE_NM.elementAt(i) %></td>
												<%-- <td align="right"><%=VSETTLE_PRICE.elementAt(i) %></td>
												<td align="right"><%=VEFF_PRICE.elementAt(i) %></td> --%>
		 										<%-- <td align="right"><%=VPREM_DISC_RATE.elementAt(i) %></td> --%>
												<td align="right" title="<%=VFINAL_PRICE_DECI.elementAt(i)%> decimal places"><%=VFINAL_PRICE.elementAt(i) %></td>
												<!--<td></td>
												<td></td>
												<td></td>
												<td></td> -->
											</tr>
										<%}%>
									<%}else{%>
										<tr>
											<td colspan="8">
												<div align="center"><%=utilmsg.infoMessage("<b>No Price Line Configured!</b>")%></div>
											</td>
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
</body>
</html>