<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_EnergyBank" id="energyBank" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
energyBank.setCallFlag("OWN_PROJECTED_DTL");
energyBank.setComp_cd(owner_cd);
energyBank.init();

Vector VSEL_TRADER_CD = energyBank.getVSEL_TRADER_CD();
Vector VSEL_TRADER_ABBR = energyBank.getVSEL_TRADER_ABBR();
Vector VSEL_DISPLAY_CONT_DTL = energyBank.getVSEL_DISPLAY_CONT_DTL();
Vector VSEL_START_DT = energyBank.getVSEL_START_DT();
Vector VSEL_BOOKED_QTY = energyBank.getVSEL_BOOKED_QTY();
Vector VSEL_SUG = energyBank.getVSEL_SUG();
Vector VSEL_UNLOADED_QTY = energyBank.getVSEL_UNLOADED_QTY();
Vector VSEL_TOTAL_EDQ = energyBank.getVSEL_TOTAL_EDQ();
Vector VSEL_TOTAL_PROJECTED = energyBank.getVSEL_TOTAL_PROJECTED();
%>
<body>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	Projected LTCORA Cargoes after <%=sysdate %>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered">
									<thead>
										<!-- <tr valign="top"> -->
										<tr>
											<th>Sr#</th>
											<th>Customer</th>
											<th>Contract#</th>
											<th>Actual Receipt Date</th>
											<th>Expected MMBTU</th>
											<th>SUG (%)</th>
											<th>SUG MMBTU</th>
										</tr>
									</thead>
									<tbody>
										<%if(VSEL_TRADER_CD.size()>0){
											for(int i=0;i<VSEL_TRADER_CD.size();i++){
										%>
											<tr>
												<td align="center"><%=i+1 %></td>
												<td align="center"><%=VSEL_TRADER_ABBR.elementAt(i)%></td>
												<td align="center"><%=VSEL_DISPLAY_CONT_DTL.elementAt(i) %></td>
												<td align="center"><%=VSEL_START_DT.elementAt(i) %></td>
												<td align="center"><%=VSEL_BOOKED_QTY.elementAt(i) %></td>
												<td align="center"><%=VSEL_SUG.elementAt(i) %></td>
												<td align="center"><%=VSEL_UNLOADED_QTY.elementAt(i) %></td>
											</tr>
										<%}%>
										<tr>
											<td colspan="4" align="right">Total:</td>
											<td align="center"><%=VSEL_TOTAL_EDQ.elementAt(0)%></td>
											<td></td>
											<td align="center"><%=VSEL_TOTAL_PROJECTED.elementAt(0)%></td>
										</tr>
										<%}else{%>
											<tr>
												<td colspan="7" align="center"><%=utilmsg.infoMessage("<b>No projected LTCORA is Available!</b>") %></td>
											</tr>
										<%}%>
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
</body>
</html>