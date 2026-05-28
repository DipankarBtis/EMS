<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function setValue(financial,inv_typ,inv_seq,inv_no,mapping_id,add_type,bu,freq,p_start_dt,p_end_dt)
{
	window.opener.setInvoiceDetail(financial,inv_typ,inv_seq,inv_no,mapping_id,add_type,bu,freq,p_start_dt,p_end_dt);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.remittance.DataBean_Remittance" id="remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String month=request.getParameter("month")==null?"":request.getParameter("month");
String year=request.getParameter("year")==null?"":request.getParameter("year");

remittance.setCallFlag("OTHER_INVOICE_LIST");
remittance.setComp_cd(owner_cd);
remittance.setCounterparty_cd(counterparty_cd);
remittance.setMonth(month);
remittance.setYear(year);
remittance.init();

Vector VFINANCIAL_YEAR = remittance.getVFINANCIAL_YEAR();
Vector VINVOICE_SEQ = remittance.getVINVOICE_SEQ();
Vector VINVOICE_NO = remittance.getVINVOICE_NO();
Vector VINVOICE_CATEGORY = remittance.getVINVOICE_CATEGORY();
Vector VINVOICE_TYPE = remittance.getVINVOICE_TYPE();
Vector VINVOICE_TYPE_NM = remittance.getVINVOICE_TYPE_NM();
Vector VPERIOD_START_DT = remittance.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = remittance.getVPERIOD_END_DT();
Vector VADDRESS_TYPE = remittance.getVADDRESS_TYPE();
Vector VADDRESS_NAME = remittance.getVADDRESS_NAME();
Vector VSTATUS = remittance.getVSTATUS();
Vector VINVOICE_REF = remittance.getVINVOICE_REF();
Vector VSEL_BU_PLANT_ABBR = remittance.getVSEL_BU_PLANT_ABBR();
Vector VSEL_BU_PLANT_SEQ_NO = remittance.getVSEL_BU_PLANT_SEQ_NO();
Vector VMAPPING_ID = remittance.getVMAPPING_ID();
Vector VFREQ = remittance.getVFREQ();
%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	Other Invoice List
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr>
											<th>Select</th>
											<th>Invoice#</th>
											<th>Invoice Category</th>
											<th>Invoice Type</th>
											<th>Billing Period</th>
											<th>Status</th>
											<th>Business Unit</th>
											<th>Trader Plant/Address</th>
										</tr>
									</thead>
									<tbody>
									<%if(VINVOICE_SEQ.size() > 0){ %>
										<%for(int i=0; i<VINVOICE_SEQ.size(); i++){ %>
										<tr>
											<td align="center"><input type="radio" 
												onclick="setValue('<%=VFINANCIAL_YEAR.elementAt(i)%>',
																'<%=VINVOICE_TYPE.elementAt(i)%>',
																'<%=VINVOICE_SEQ.elementAt(i)%>',
																'<%=VINVOICE_NO.elementAt(i)%>',
																'<%=VMAPPING_ID.elementAt(i)%>',
																'<%=VADDRESS_TYPE.elementAt(i)%>',
																'<%=VSEL_BU_PLANT_SEQ_NO.elementAt(i)%>',
																'<%=VFREQ.elementAt(i)%>',
																'<%=VPERIOD_START_DT.elementAt(i)%>',
																'<%=VPERIOD_END_DT.elementAt(i)%>');"></td>
											<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_CATEGORY.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_TYPE_NM.elementAt(i)%></td>
											<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
											<td align="center"><%=VSTATUS.elementAt(i)%></td>
											<td align="center"><%=VSEL_BU_PLANT_ABBR.elementAt(i)%></td>
											<td align="center"><%=VADDRESS_NAME.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
									<tr>
										<td colspan="8" align="center"><%=utilmsg.infoMessage("<b>No Invoice Generated!</b>") %></td>
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
</form>
</body>
</html>