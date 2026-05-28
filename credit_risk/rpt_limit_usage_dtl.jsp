<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DataBean_CreditRisk" id="db_limit" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%

String clearance = request.getParameter("clearance")==null?"K":request.getParameter("clearance");
String entity = request.getParameter("entity") == null?"C":request.getParameter("entity");
String entity_cd = request.getParameter("entity_cd") == null?"0":request.getParameter("entity_cd");
String limit_status = request.getParameter("limit_status") == null?"0":request.getParameter("limit_status");
String detail = request.getParameter("detail")==null?"":request.getParameter("detail");
String cust_nm = request.getParameter("cust_nm")==null?"":request.getParameter("cust_nm");
String deal_id = request.getParameter("deal_id")==null?"":request.getParameter("deal_id");
String total = request.getParameter("total")==null?"":request.getParameter("total");
String usage_type = request.getParameter("usage_type")==null?"":request.getParameter("usage_type");

db_limit.setCallFlag("CREDIT_LIMIT_LIST");
db_limit.setClearance(clearance);
db_limit.setComp_cd(owner_cd);
db_limit.setEntity(entity);
db_limit.setEntity_cd(entity_cd);
db_limit.setLimit_status(limit_status);
db_limit.init();

Vector VU_DEAL_NO = db_limit.getVU_DEAL_NO();
Vector VU_COMP_PROFILE = db_limit.getVU_COMP_PROFILE();
Vector VU_DELV_DT = db_limit.getVU_DELV_DT();
Vector VU_PRICE_TYPE = db_limit.getVU_PRICE_TYPE();
Vector VU_DCQ = db_limit.getVU_DCQ();
Vector VU_PRICE = db_limit.getVU_PRICE();
Vector VU_TOTAL = db_limit.getVU_TOTAL();
Vector VU_EXCH_RATE = db_limit.getVU_EXCH_RATE();
Vector VU_TAX = db_limit.getVU_TAX();
Vector VU_GRAND_TOTAL = db_limit.getVU_GRAND_TOTAL();

Vector VPCG_SEC_REF = db_limit.getVPCG_SEC_REF();
Vector VPCG_SEC_TYPE = db_limit.getVPCG_SEC_TYPE();
Vector VPCG_SEC_VALUE = db_limit.getVPCG_SEC_VALUE();
Vector VPCG_DEAL_NO = db_limit.getVPCG_DEAL_NO();
Vector VPCG_COMP_ABBR = db_limit.getVPCG_COMP_ABBR();
Vector VPCG_STATUS = db_limit.getVPCG_STATUS();

String pcg_total_value = db_limit.getPcg_total_value();

%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	<%=cust_nm %>
				    </div>
				</div>
				<%if(usage_type.equals("NET_USAGE")){ %>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr>
											<th>Deal No</th>
											<th>Legal Entity</th>
											<th>Security Ref.</th>
											<th>PCG Status</th>
											<th>PCG Value</th>
										</tr>
									</thead>
									<tbody>
									<%if(VPCG_DEAL_NO.size()>0) {%>
										<%
										for(int i=0; i< VPCG_DEAL_NO.size(); i++)
										{%>
											<tr>
												<td align="center" style="background:#99ffcc;"><%=VPCG_DEAL_NO.elementAt(i) %></td>
												<td align="center"><%=VPCG_COMP_ABBR.elementAt(i) %></td>
												<td align="center"><%=VPCG_SEC_REF.elementAt(i) %></td>
												<td align="center"><%=VPCG_STATUS.elementAt(i) %></td>
												<td align="right"><%=VPCG_SEC_VALUE.elementAt(i) %></td>
											</tr>
										<%}%>
											<tr>
												<td colspan="4" style="background:#99ffcc;" align="right"><strong>Total</strong></td>
												<td style="background:#99ffcc" align="right"><strong><%=pcg_total_value%></strong></td>
											</tr>
									<%}else{ %>
										<tr><td colspan="5" align="center"><%=utilmsg.infoMessage("<b>PCG Is Not Available!</b>") %></td></tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<%}else if(usage_type.equals("USAGE")){ %>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr>
											<th>Deal No</th>
											<th>Legal Entity</th>
											<th>Date</th>
											<th>Price Type</th>
											<th>DCQ</th>
											<th>Contract Price</th>
											<th>Total</th>
											<th>Exchange Rate</th>
											<th>Applied Tax</th>
											<th>Grand Total</th>
										</tr>
									</thead>
									<tbody >
										<%
										for(int i=0; i< VU_DEAL_NO.size(); i++)
										{%>
											<tr>
												<td align="center" style="background:#99ffcc;"><%=VU_DEAL_NO.elementAt(i) %></td>
												<td align="center"><%=VU_COMP_PROFILE.elementAt(i) %></td>
												<td align="center"><%=VU_DELV_DT.elementAt(i) %></td>
												<td align="center"><%=VU_PRICE_TYPE.elementAt(i) %></td>
												<td align="right"><%=VU_DCQ.elementAt(i) %></td>
												<td align="right"><%=VU_PRICE.elementAt(i) %></td>
												<td align="right"><%=VU_TOTAL.elementAt(i) %></td>
												<td align="right"><%=VU_EXCH_RATE.elementAt(i) %></td>
												<td align="right"><%=VU_TAX.elementAt(i) %></td>
												<td align="right"><%=VU_GRAND_TOTAL.elementAt(i) %></td>
											</tr>
										<%}%>
										<tr>
											<td colspan="9" style="background:#99ffcc;" align="right"><strong>Total</strong></td>
											<td style="background:#99ffcc" align="right"><strong><%=total%></strong></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<%} %>
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