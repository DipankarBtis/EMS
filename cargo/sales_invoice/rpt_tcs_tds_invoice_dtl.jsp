<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.sales_invoice.DataBean_Sales_Invoice" id="sales_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");

sales_inv.setCallFlag("TCS_TDS_INV_DTL");
sales_inv.setComp_cd(owner_cd);
sales_inv.setCounterparty_cd(counterparty_cd);
sales_inv.setFinancial_year(financial_year);
sales_inv.init();

Vector VDEAL_NO = sales_inv.getVDEAL_NO();
Vector VCONT_REF_NO = sales_inv.getVCONT_REF_NO();
Vector VINVOICE_AMT = sales_inv.getVINVOICE_AMT();
Vector VINVOICE_NO = sales_inv.getVINVOICE_NO();
Vector VINVOICE_DT = sales_inv.getVINVOICE_DT();

String total_InvoiceAmt = sales_inv.getTotal_InvoiceAmt();

%>
<body>

<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Invoice Detail
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th>Sr#</th>
											<th>Contract#</th>
											<th>Contract Ref#</th>
											<th>Invoice#</th>
											<th>Invoice Date</th>
											<th>Invoice Amount</th>
										</tr>
									</thead>
									<tbody>
									<%if(VDEAL_NO.size()>0){ %>
										<%for(int i=0;i<VDEAL_NO.size(); i++){ %>
										<tr>
											<td align="center"><%=i+1%></td>
											<td align="center"><%=VDEAL_NO.elementAt(i)%></td>
											<td align="center"><%=VCONT_REF_NO.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
											<td align="right"><%=VINVOICE_AMT.elementAt(i)%></td>
										</tr>
										<%} %>
										<tr>
											<td align="right" colspan="5"><b>Total :</b></td>
											<td align="right"><b><%=total_InvoiceAmt %></b></td>
										</tr>
									<%}else{ %>
										<tr>
											<td align="center" colspan="6"><%=utilmsg.infoMessage("<b>No Invoice!</b>")%></td>
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
</body>
</html>