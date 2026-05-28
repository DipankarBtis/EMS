
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function Do_close(msg,msg_type,accroid)
{
	window.opener.refershPar(msg,msg_type,accroid);
	window.close();
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Invoice" id="derv_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String plant_seq=request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
String period_start_dt=request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
String temp_period_start_dt=request.getParameter("temp_period_start_dt")==null?"":request.getParameter("temp_period_start_dt");
String temp_period_end_dt=request.getParameter("temp_period_end_dt")==null?"":request.getParameter("temp_period_end_dt");
String bu_unit=request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String exist_financial_year=request.getParameter("exist_financial_year")==null?"":request.getParameter("exist_financial_year");
String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
String operation=request.getParameter("operation")==null?"":request.getParameter("operation");
String instrument_no=request.getParameter("instrument_no")==null?"":request.getParameter("instrument_no");
String inv_type=request.getParameter("inv_type")==null?"":request.getParameter("inv_type");
String inv_dt=request.getParameter("inv_dt")==null?"":request.getParameter("inv_dt");

/* String[] all_agmt_no = agmt_no.split(", ");
String[] all_agmt_rev = agmt_rev.split(", ");
String[] all_cont_type = contract_type.split(", ");
String[] all_cont_no = cont_no.split(", ");
String[] all_cont_rev = cont_rev.split(", ");
String[] all_instrument_no = instrument_no.split(", ");

for(int i=0;i<all_cont_no.length;i++)
{ */
	derv_inv.setCallFlag("DERV_INVOICE_MODIFICATION");
	derv_inv.setComp_cd(owner_cd);
	derv_inv.setCounterparty_cd(counterparty_cd);
	derv_inv.setAgmt_no(agmt_no);
	derv_inv.setAgmt_rev(agmt_rev);
	derv_inv.setCont_no(cont_no);
	derv_inv.setCont_rev(cont_rev);
	derv_inv.setCont_type(contract_type);
	derv_inv.setInstrument_no(instrument_no);
	derv_inv.setPlant_seq(plant_seq);
	derv_inv.setBilling_cycle(billing_cycle);
	derv_inv.setPeriod_start_dt(period_start_dt);
	derv_inv.setPeriod_end_dt(period_end_dt);
	derv_inv.setTemp_period_start_dt(temp_period_start_dt);
	derv_inv.setTemp_period_end_dt(temp_period_end_dt);
	derv_inv.setBu_plant_seq(bu_unit);
	derv_inv.setFy_year(exist_financial_year);
	derv_inv.setExist_financial_year(exist_financial_year);
	derv_inv.setBu_state_tin(bu_state_tin);
	derv_inv.setOperation(operation);
	derv_inv.setInv_dt(inv_dt);
	derv_inv.setInv_type(inv_type);
	derv_inv.init();
/* } */
String comp_name=derv_inv.getComp_name();
String plant_abbr=derv_inv.getPlant_abbr();
String bu_plant_abbr=derv_inv.getBu_plant_abbr();
String counterparty_abbr=derv_inv.getCounterparty_abbr();
String financial_year=derv_inv.getFy_year();
String invoice_seq=derv_inv.getInvoice_seq();

Vector VCOUNTERPARTY_CD = derv_inv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = derv_inv.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = derv_inv.getVCOUNTERPARTY_ABBR();
Vector VAGMT_TYPE = derv_inv.getVAGMT_TYPE();
Vector VAGMT_NO = derv_inv.getVAGMT_NO();
Vector VAGMT_REV = derv_inv.getVAGMT_REV();
Vector VCONT_TYPE = derv_inv.getVCONT_TYPE();
Vector VCONT_NO = derv_inv.getVCONT_NO();
Vector VCONT_REV = derv_inv.getVCONT_REV();
Vector VCONT_NAME = derv_inv.getVCONT_NAME();
Vector VCONT_REF = derv_inv.getVCONT_REF();
Vector VDEAL_MAPPING = derv_inv.getVDEAL_MAPPING();
Vector VINSTRUMENT_NO = derv_inv.getVINSTRUMENT_NO();
Vector VBUY_SELL = derv_inv.getVBUY_SELL();
Vector VCARGO_ARRIVAL_DT = derv_inv.getVCARGO_ARRIVAL_DT();
Vector VBOOKED_MMBTU = derv_inv.getVBOOKED_MMBTU();
Vector VBOOKED_SCM = derv_inv.getVBOOKED_SCM();
Vector VQTY_UNIT = derv_inv.getVQTY_UNIT();
Vector VTRADE_DT = derv_inv.getVTRADE_DT();
Vector VRATE = derv_inv.getVRATE();
Vector VCONT_START_DT = derv_inv.getVCONT_START_DT();
Vector VCONT_END_DT = derv_inv.getVCONT_END_DT();
Vector VDEAL_PRICE_CURVE = derv_inv.getVDEAL_PRICE_CURVE();
Vector VDEAL_PROD_NM = derv_inv.getVDEAL_PROD_NM();
Vector VFLOAT_RATE = derv_inv.getVFLOAT_RATE();
Vector VINVGEN_FLAG = derv_inv.getVINVGEN_FLAG();
Vector VPLANT_SEQ = derv_inv.getVPLANT_SEQ();
Vector VPLANT_ABBR = derv_inv.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = derv_inv.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = derv_inv.getVBU_PLANT_ABBR();
Vector VPRICE_TYPE = derv_inv.getVPRICE_TYPE();
Vector VMST_PLANT_SEQ = derv_inv.getVMST_PLANT_SEQ();
Vector VMST_PLANT_ABBR = derv_inv.getVMST_PLANT_ABBR();
Vector VMST_BU_PLANT_SEQ = derv_inv.getVMST_BU_PLANT_SEQ();
Vector VMST_BU_PLANT_ABBR = derv_inv.getVMST_BU_PLANT_ABBR();
Vector VMST_CURVE_NM = derv_inv.getVMST_CURVE_NM();
Vector VSELL_RATE = derv_inv.getVSELL_RATE();
Vector VSELL_AMT = derv_inv.getVSELL_AMT();
Vector VBUY_RATE = derv_inv.getVBUY_RATE();
Vector VBUY_AMT = derv_inv.getVBUY_AMT();
Vector VINSTRUMENT_TYPE = derv_inv.getVINSTRUMENT_TYPE();
Vector VCURVE_NM = derv_inv.getVCURVE_NM();
Vector VINSTRUMENT_DURATION = derv_inv.getVINSTRUMENT_DURATION();
Vector VPERIOD_START_DT = derv_inv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = derv_inv.getVPERIOD_END_DT();

String remark1=derv_inv.getRemark1();
String remark2=derv_inv.getRemark2();
inv_dt=derv_inv.getInv_dt();
String inv_due_dt=derv_inv.getInv_due_dt();
period_start_dt=derv_inv.getPeriod_start_dt();
period_end_dt=derv_inv.getPeriod_end_dt();
String inv_ref=derv_inv.getInv_ref();
String chk_flg=derv_inv.getChk_flg();
String bank_formula=derv_inv.getBank_formula();

if(inv_type.equals("I") && (remark1.equals("") || remark1.equals("Please pay the invoiced amount by wire transfer at Bank Name: ")))
{
	remark1="Please pay the invoiced amount by wire transfer at Bank Name: "+bank_formula;
}

if(period_start_dt.equals(""))
{
	period_start_dt=""+VTRADE_DT.elementAt(0);
}
if(period_end_dt.equals(""))
{
	period_end_dt=""+VTRADE_DT.elementAt(VTRADE_DT.size()-1);
}

String inv_info_msg="";
/* if(!inv_entered_on.equals(""))
{
	inv_info_msg="Invoice Entered On : <b>"+inv_entered_on+"</b>";
}
if(!inv_approved_on.equals(""))
{
	inv_info_msg+=" Invoice Approved On : <b>"+inv_approved_on+"</b>";
} */

String[] temp = inv_dt.split("/");
String date_num=temp[0];
String month=temp[1];
String year=temp[2];
%>
<body onload="<%if(!msg.equals("")){ %>Do_close('<%=msg%>','<%=msg_type%>','<%=accroid%>');<%} %>calculate_amt();">

<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_Derivatives_Invoice">
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
					    	<%=comp_name %> [<%=exist_financial_year %>:  <%=month%>/<%=year%>]
					    </div>
					   	<!-- <span class="btn rounded-circle" style="background:var(--header_color);color:var(--header_font_color);" title="Back" onclick="goBack();">
						  &nbsp;<i class="fa fa-step-backward fa-2x"></i>&nbsp;
						</span> -->
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th colspan="1"><%=owner_abbr%> Detail</th>
											<!-- <th rowspan="2">Billing Cycle</th> -->
											<th colspan="2">Customer Detail</th>
										</tr>
										<tr>
											<th>Business Unit<span class="s-red">*</span></th>
											<th>Customer</th>
											<th>Plant</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td align="center">
												<%=bu_plant_abbr %>
											</td>
											<!-- <td align="center">
												Trade(Date Range)
												<input type="hidden" name="billing_cycle" id="billing_cycle" value="12">
											</td> -->
											<td align="center">
												<%=counterparty_abbr %>
											</td>
											<td align="center"><%=plant_abbr%></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Invoice Type<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="inv_type" value="" readonly>
					      				<input type="hidden" class="form-control form-control-sm" name="inv_type_flg" value="" readonly>
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label" id="invref"><b>Invoice Ref</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<input type="text" class="form-control form-control-sm" name="inv_ref" value="<%=inv_ref %>" maxlength="25">
					      				<input type="hidden" class="form-control form-control-sm" name="invoice_seq" value="<%=invoice_seq %>" maxlength="25">
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Invoice Date<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="inv_dt" value="<%=inv_dt %>" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Due Date<span class="s-red">*</span></b></label>
					  			</div>
							</div> 
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="due_dt" value="<%=inv_due_dt %>" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div>
						<%-- <div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>From Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="frm_dt" value="<%=period_start_dt %>" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>To Date</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=period_end_dt %>" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
						</div> --%>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
							</div>
						</div>
						<div class="row m-b-5">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th>Trade Date</th>
											<th>Deal No</th>
											<th>Item</th>
											<th>Index</th>
											<th>Trade Term</th>
											<th>Quantity Unit</th>
											<th>Quantity</th>
											<th>Rate Unit</th>
											<th>Buy Rate</th>
											<th>Buy Amount</th>
											<th>Sell Rate</th>
											<th>Sell Amount</th>
											<th>Total Amount</th>
										</tr>
									</thead>
									<tbody>
										<%for(int i=0; i<VDEAL_MAPPING.size(); i++){ %>
										<tr>
											<td align="center"><%=VTRADE_DT.elementAt(i) %></td>
											<td align="center"><%=VDEAL_MAPPING.elementAt(i) %> <br>(<%=VCONT_REF.elementAt(i) %>)
												<input type="hidden" class="form-check-input" name="agmtNo" id="agmtNo<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>">
												<input type="hidden" class="form-check-input" name="agmtRev" id="agmtRev<%=i%>" value="<%=VAGMT_REV.elementAt(i)%>">
												<input type="hidden" class="form-check-input" name="agmtType" id="agmtType<%=i%>" value="<%=VAGMT_TYPE.elementAt(i)%>">
												<input type="hidden" class="form-check-input" name="contNo" id="contNo<%=i%>" value="<%=VCONT_NO.elementAt(i)%>">
												<input type="hidden" class="form-check-input" name="contRev" id="contRev<%=i%>" value="<%=VCONT_REV.elementAt(i)%>">
												<input type="hidden" class="form-check-input" name="contType" id="contType<%=i%>" value="<%=VCONT_TYPE.elementAt(i)%>">
												<input type="hidden" class="form-check-input" name="instrumentNo" id="instrumentNo<%=i%>" value="<%=VINSTRUMENT_NO.elementAt(i)%>">
												<input type="hidden" class="form-check-input" name="buySell" id="buySell<%=i%>" value="<%=VBUY_SELL.elementAt(i)%>">
												<input type="hidden" class="form-check-input" name="cont_ref" id="cont_ref<%=i%>" value="<%=VCONT_REF.elementAt(i)%>">
												<input type="hidden" class="form-check-input" name="frm_dt" id="frm_dt<%=i%>" value="<%=VPERIOD_START_DT.elementAt(i)%>">
												<input type="hidden" class="form-check-input" name="to_dt" id="to_dt<%=i%>" value="<%=VPERIOD_END_DT.elementAt(i)%>">
											</td>
											<td align="center"><%=VINSTRUMENT_TYPE.elementAt(i) %></td>
											<td align="center"><%=VCURVE_NM.elementAt(i) %></td>
											<td align="center"><%=VINSTRUMENT_DURATION.elementAt(i) %></td>
											<td align="center"><%=VQTY_UNIT.elementAt(i) %></td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="qty" id="qty<%=i %>" value="<%=VBOOKED_MMBTU.elementAt(i)%>" style="text-align: right;" onchange="calculate_amt();" readonly>
												</div>
											</td>
											<td align="center">USD</td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="buy_rate" id="buy_rate<%=i %>" value="<%=VBUY_RATE.elementAt(i)%>" style="text-align: right;" onchange="calculate_amt();" <%if(VBUY_SELL.elementAt(i).equals("BUY")){ %>readonly<%} %>>
												</div>
											</td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="buy_amt" id="buy_amt<%=i %>" value="<%=VBUY_AMT.elementAt(i) %>" style="text-align: right;" readonly>
												</div>
											</td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="sell_rate" id="sell_rate<%=i %>" value="<%=VSELL_RATE.elementAt(i)%>" style="text-align: right;" onchange="calculate_amt();" <%if(VBUY_SELL.elementAt(i).equals("SELL")){ %>readonly<%} %>>
												</div>
											</td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="sell_amt" id="sell_amt<%=i %>" value="<%=VSELL_AMT.elementAt(i) %>" style="text-align: right;" readonly>
												</div>
											</td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="total_amt" id="total_amt<%=i %>" value="" style="text-align: right;" readonly>
												</div>
											</td>
										</tr>
										<%} %>
										<tr>
											<td colspan="12" align="right"><b>Total:</b></td>
											<td align="center">
												<div style="width:100px;">
													<input type="text" class="form-control form-control-sm" name="grand_total_amt" id="grand_total_amt" value="" style="text-align: right;" readonly>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Remark 1</b></label>
					  			</div>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<textarea class="form-control form-control-sm" name="remark1" rows="3"><%=remark1%></textarea>
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>Remark 2</b></label>
					  			</div>
							</div>
							<div class="col-sm-10 col-xs-10 col-md-10">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					      				<textarea class="form-control form-control-sm" name="remark2" rows="3"><%=remark2%></textarea>
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-12 col-xs-12 col-md-12">
							</div>
						</div>
					    <div class="row m-b-5">
					    	<div class="col-sm-6 col-xs-6 col-md-6" align="right"> 
					    	<%if(!chk_flg.equals("Y")){ %>
					    		<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<%if(write_access.equals("Y")){ %>
											<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
										<%}else{ %>
											<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
										<%} %>
					    			</div>
					    		</div>
					    	<%} %>
					    	</div>
					    </div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="GEN_DERV_INV">
<input type="hidden" name="opration" value="<%=operation%>">
<input type="hidden" name="operation" value="<%=operation%>">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" value="<%=agmt_rev%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev" value="<%=cont_rev%>">
<input type="hidden" name="instrument_no" value="<%=instrument_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="plant_seq" value="<%=plant_seq%>">
<input type="hidden" name="period_start_dt" value="<%=period_start_dt%>">
<input type="hidden" name="period_end_dt" value="<%=period_end_dt%>">
<input type="hidden" name="temp_period_start_dt" value="<%=temp_period_start_dt%>">
<input type="hidden" name="temp_period_end_dt" value="<%=temp_period_end_dt%>">
<input type="hidden" name="billing_cycle" value="<%=billing_cycle%>">
<input type="hidden" name="bu_plant_seq" value="<%=bu_unit%>">
<input type="hidden" name="bu_state_tin" value="<%=bu_state_tin%>">
<input type="hidden" name="old_inv_type_flg" value="<%=inv_type%>">

<input type="hidden" name="financial_year" value="<%=financial_year%>">
<input type="hidden" name="exist_financial_year" value="<%=exist_financial_year%>">
<%-- <input type="hidden" name="contract_ref" value="<%=contract_ref%>"> --%>
<input type="hidden" name="accroid" value="<%=accroid%>">
<%-- <input type="hidden" name="plant_gstin_no" value="<%=plant_gstin_no%>"> --%>
<%-- <input type="hidden" name="bu_gstin_no" value="<%=bu_gstin_no%>"> --%>

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

<script type="text/javascript">
enableButton=true;
function doSubmit()
{
	var rowCount = <%=VDEAL_MAPPING.size()%>;
	var invoice_dt = document.forms[0].inv_dt.value;
	var invoice_due_dt = document.forms[0].due_dt.value;
	var inv_ref = document.forms[0].inv_ref.value;
	var inv_type_flg = document.forms[0].inv_type_flg.value;
	var inv_type = document.forms[0].inv_type.value;
	var qty = document.forms[0].qty;
	var buy_rate = document.forms[0].buy_rate;
	var buy_amt = document.forms[0].buy_amt;
	var sell_rate = document.forms[0].sell_rate;
	var sell_amt = document.forms[0].sell_amt;
	var total_amt = document.forms[0].total_amt;
	
	var msg="";
	var flag=true;
	
	if(inv_type_flg=="R")
	{
		if(trim(inv_ref)=="")
		{
			msg+="Enter Vendor Invoice No!\n";
			flag=false;
		}
	}
	if(trim(inv_type_flg)=="")
	{
		msg+="Enter Invoice Type!\n";
		flag=false;
	}
	if(trim(invoice_dt)=="")
	{
		msg+="Enter Invoice Date!\n";
		flag=false;
	}
	if(trim(invoice_due_dt)=="")
	{
		msg+="Enter Invoice Due Date!\n";
		flag=false;
	}
	
	if(trim(invoice_dt)!="" && trim(invoice_due_dt)!="")
	{
		var count = compareDate(invoice_dt,invoice_due_dt);
		if(parseInt(count) == 1)
		{
			msg+="Invoice Due Date should be grater or equal Invoice Date!";
			flag=false;
		}
	}
	if(rowCount>1)
	{
		for (var i = 0; i < rowCount; i++) 
	    {
			if(trim(qty[i].value)!="")
			{
				if(parseFloat(qty[i].value) <= 0)
				{
					msg+="Quantity should be > ZERO(0) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;	
				}
			}
			else
			{
				msg+="Enter quantity for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			
			if(trim(buy_amt[i].value)!="")
			{
				if(parseFloat(buy_amt[i].value) <= 0)
				{
					msg+="Buy amount should be > ZERO(0) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;	
				}
			}
			else
			{
				msg+="Enter Buy amount for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
			
			if(trim(sell_amt[i].value)!="")
			{
				if(parseFloat(sell_amt[i].value) <= 0)
				{
					msg+="Sell amount should be > ZERO(0) for ROW - "+parseInt(i+1)+"!\n";
					flag=false;	
				}
			}
			else
			{
				msg+="Enter Sell amount for ROW - "+parseInt(i+1)+"!\n";
				flag=false;
			}
	    }
	}
	else
	{
		if(trim(qty.value)!="")
		{
			if(parseFloat(qty.value) <= 0)
			{
				msg+="Quantity should be > ZERO(0) for ROW - 1!\n";
				flag=false;	
			}
		}
		else
		{
			msg+="Enter quantity for ROW - 1!\n";
			flag=false;
		}
		
		if(trim(buy_amt.value)!="")
		{
			if(parseFloat(buy_amt.value) <= 0)
			{
				msg+="Buy amount should be > ZERO(0) for ROW - 1!\n";
				flag=false;	
			}
		}
		else
		{
			msg+="Enter Buy amount for ROW - 1!\n";
			flag=false;
		}
		
		if(trim(sell_amt.value)!="")
		{
			if(parseFloat(sell_amt.value) <= 0)
			{
				msg+="Sell amount should be > ZERO(0) for ROW - 1!\n";
				flag=false;	
			}
		}
		else
		{
			msg+="Enter Sell amount for ROW - 1!\n";
			flag=false;
		}
	}
	
	
	if(flag)
	{
		var a=confirm("Do you want to Submit "+inv_type+"?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			editAllowedOnCpStatus = true;
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function calculate_amt() 
{
    var rowCount = <%=VDEAL_MAPPING.size()%>;
    var remark1="<%=remark1%>";
    var bank_formula="<%=bank_formula%>";
	var grandTotal=0;
    for (var i = 0; i < rowCount; i++) 
    {
        var qty = parseFloat(document.getElementById("qty" + i).value);
        var buyRate = parseFloat(document.getElementById("buy_rate" + i).value);
        var sellRate = parseFloat(document.getElementById("sell_rate" + i).value);

        var buyAmt = qty * buyRate;
        var sellAmt = qty * sellRate;
        var totalAmt = sellAmt - buyAmt;
        
        grandTotal += totalAmt;

        document.getElementById("buy_amt" + i).value = buyAmt.toFixed(2);
        document.getElementById("sell_amt" + i).value = sellAmt.toFixed(2);
        document.getElementById("total_amt" + i).value = totalAmt.toFixed(2);
    }
    
    if(grandTotal<0)
   	{
    	document.forms[0].inv_type.value="Remittance";
    	document.forms[0].inv_type_flg.value="R";
    	if(remark1.includes("Please pay the invoiced amount by wire transfer at Bank Name: "))
   		{
    		document.forms[0].remark1.value="";
   		}
    	document.getElementById('invref').innerHTML='<b>Vendor Invoice No<span class="s-red">*</span></b>';
    	grandTotal=grandTotal*(-1);
   	}
    else
   	{
    	document.forms[0].inv_type.value="Invoice";
    	document.forms[0].inv_type_flg.value="I";
    	document.getElementById('invref').innerHTML='<b>Invoice Ref</b>';
    	if(remark1=="" || remark1=="Please pay the invoiced amount by wire transfer at Bank Name: ")
   		{
    		document.forms[0].remark1.value="Please pay the invoiced amount by wire transfer at Bank Name: ";
   		}
    	else
   		{
    		document.forms[0].remark1.value=remark1;
   		}
   	}
    
    document.getElementById("grand_total_amt").value = grandTotal.toFixed(2);
}
</script>
</html>