<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CreditRisk_Report" id="db_walkforward" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String detail = request.getParameter("detail")==null?"":request.getParameter("detail");
String cust_nm = request.getParameter("cust_nm")==null?"":request.getParameter("cust_nm");
String deal_id = request.getParameter("deal_id")==null?"":request.getParameter("deal_id");
String total = request.getParameter("total")==null?"":request.getParameter("total");
String prev_total = request.getParameter("prev_total")==null?"":request.getParameter("prev_total");
String prev_date=utildate.getPreviousDate();
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt = request.getParameter("from_dt")==null?prev_date:request.getParameter("from_dt");
String clearance = request.getParameter("clearance")==null?"K":request.getParameter("clearance");
String report_days  = request.getParameter("report_days")==null?"21":request.getParameter("report_days");
String sell_buy  = request.getParameter("sell_buy")==null?"Sell":request.getParameter("sell_buy");
String type = request.getParameter("type")==null?"Sell":request.getParameter("type");

db_walkforward.setCallFlag("WALKFORWARD_EXPOSURE_REPORT");
db_walkforward.setClearance(clearance);
db_walkforward.setComp_cd(owner_cd);
db_walkforward.setFrom_dt(from_dt);
db_walkforward.setReport_days(report_days);
db_walkforward.setCounterparty_cd(counterparty_cd);
db_walkforward.init();

Vector VOC_DELV_DT = db_walkforward.getVOC_DELV_DT();
Vector VOC_COMP_PROFILE = db_walkforward.getVOC_COMP_PROFILE();
Vector VOC_PRICE_TYPE = db_walkforward.getVOC_PRICE_TYPE();
Vector VOC_DCQ = db_walkforward.getVOC_DCQ();
Vector VOC_CONT_PRICE = db_walkforward.getVOC_CONT_PRICE();
Vector VOC_TOTAL = db_walkforward.getVOC_TOTAL();
Vector VOC_EXCH_RATE = db_walkforward.getVOC_EXCH_RATE();
Vector VOC_APPLY_TAX = db_walkforward.getVOC_APPLY_TAX();
Vector VOC_TRANS_CHARGE = db_walkforward.getVOC_TRANS_CHARGE();
Vector VOC_GRAND_TOTAL = db_walkforward.getVOC_GRAND_TOTAL();
Vector VOC_CUMULATIVE_TOTAL = db_walkforward.getVOC_CUMULATIVE_TOTAL();
Vector VEXPO_DELV_DT = db_walkforward.getVEXPO_DELV_DT();
Vector VEXPO_COMP_PROFILE = db_walkforward.getVEXPO_COMP_PROFILE();
Vector VEXPO_PRICE_TYPE = db_walkforward.getVEXPO_PRICE_TYPE();
Vector VEXPO_DCQ = db_walkforward.getVEXPO_DCQ();
Vector VEXPO_CONT_PRICE = db_walkforward.getVEXPO_CONT_PRICE();
Vector VEXPO_TOTAL = db_walkforward.getVEXPO_TOTAL();
Vector VEXPO_EXCH_RATE = db_walkforward.getVEXPO_EXCH_RATE();
Vector VEXPO_APPLY_TAX = db_walkforward.getVEXPO_APPLY_TAX();
Vector VEXPO_TRANS_CHARGE = db_walkforward.getVEXPO_TRANS_CHARGE();
Vector VEXPO_GRAND_TOTAL = db_walkforward.getVEXPO_GRAND_TOTAL();
Vector VEXPO_CUMULATIVE_TOTAL = db_walkforward.getVEXPO_CUMULATIVE_TOTAL();
%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	<%=cust_nm %> (<%=deal_id%>)
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr>
											<th>Date</th>
											<th>Legal Entity</th>
											<th>Price Type</th>
											<th>DCQ</th>
											<th>Contract Price</th>
											<th>Total</th>
											<th>Exchange Rate</th>
											<th>Applied Tax</th>
											<th>Trans Value</th>
											<th>Grand Total</th>
											<th>Cumulative Total</th>
										</tr>
									</thead>
									<tbody>
									<%if(type.equals("OPEN_CREDIT"))
									{
										for(int i=0; i< VOC_DELV_DT.size(); i++)
										{%>
											<tr>
												<td align="center" style="background:#99ffcc;"><%=VOC_DELV_DT.elementAt(i) %></td>
												<td align="center"><%=VOC_COMP_PROFILE.elementAt(i) %></td>
												<td align="center"><%=VOC_PRICE_TYPE.elementAt(i) %></td>
												<td align="center"><%=VOC_DCQ.elementAt(i) %></td>
												<td align="right"><%=VOC_CONT_PRICE.elementAt(i) %></td>
												<td align="right"><%=VOC_TOTAL.elementAt(i) %></td>
												<td align="right"><%=VOC_EXCH_RATE.elementAt(i) %></td>
												<td align="right"><%=VOC_APPLY_TAX.elementAt(i) %></td>
												<td align="right"><%=VOC_TRANS_CHARGE.elementAt(i) %></td>
												<td align="right"><%=VOC_GRAND_TOTAL.elementAt(i) %></td>
												<td align="right"><%=VOC_CUMULATIVE_TOTAL.elementAt(i) %></td>
											</tr>
										<%}
									}
									else if(type.equals("EXPOSURE"))
									{
										for(int i=0; i< VEXPO_DELV_DT.size(); i++)
										{%>
											<tr>
												<td align="center" style="background:#99ffcc;"><%=VEXPO_DELV_DT.elementAt(i) %></td>
												<td align="center"><%=VEXPO_COMP_PROFILE.elementAt(i) %></td>
												<td align="center"><%=VEXPO_PRICE_TYPE.elementAt(i) %></td>
												<td align="center"><%=VEXPO_DCQ.elementAt(i) %></td>
												<td align="right"><%=VEXPO_CONT_PRICE.elementAt(i) %></td>
												<td align="right"><%=VEXPO_TOTAL.elementAt(i) %></td>
												<td align="right"><%=VEXPO_EXCH_RATE.elementAt(i) %></td>
												<td align="right"><%=VEXPO_APPLY_TAX.elementAt(i) %></td>
												<td align="right"><%=VEXPO_TRANS_CHARGE.elementAt(i) %></td>
												<td align="right"><%=VEXPO_GRAND_TOTAL.elementAt(i) %></td>
												<td align="right"><%=VEXPO_CUMULATIVE_TOTAL.elementAt(i) %></td>
											</tr>
										<%}
									}%>
										<tr>
											<td colspan="10" style="background-color: #99ffcc;"><strong>Prev Total = <%=prev_total%></strong></td>
											<td style="background-color: #99ffcc;"><strong>Total = <%=total%></strong></td>
										</tr>
									
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="" align="right">
						<input type="button" class="btn btn-warning com-btn" value="Close" onclick="window.close();">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</form>
</body>
</html>