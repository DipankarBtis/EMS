<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
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

//String display_map_id=contract_type+""+agmt_no+"-"+cont_no;

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


market_risk.setCallFlag("VARIABLE_PRICE_CONFIG");
market_risk.setCounterparty_cd(counterparty_cd);
market_risk.setComp_cd(owner_cd);
market_risk.setAgmt_no(agmt_no);
market_risk.setAgmt_rev_no(agmt_rev_no);
market_risk.setCont_no(cont_no);
market_risk.setCont_rev_no(cont_rev_no);
market_risk.setContract_type(contract_type);
market_risk.setCargo_no(cargo_no);
market_risk.setMapping_id(mapping_id);
market_risk.init();

String counterparty_abbr=market_risk.getCounterparty_abbr();
String display_map_id = market_risk.getDisplay_map_id();

Vector VSTART_DT = market_risk.getVSTART_DT();
Vector VEND_DT = market_risk.getVEND_DT();
Vector VPRICE_TYPE = market_risk.getVPRICE_TYPE();
Vector VPRICE_RANGE_NM = market_risk.getVPRICE_RANGE_NM();
Vector VPRICE_RATE = market_risk.getVPRICE_RATE();
Vector VPRICE_RATE_UNIT = market_risk.getVPRICE_RATE_UNIT();
Vector VPRICE_RATE_UNIT_NM = market_risk.getVPRICE_RATE_UNIT_NM();
Vector VPHYSICAL_CURVE = market_risk.getVPHYSICAL_CURVE();
Vector VSLOPE = market_risk.getVSLOPE();
Vector VCONSTANT = market_risk.getVCONSTANT();
Vector VREMARK = market_risk.getVREMARK();
Vector VSEQ_NO = market_risk.getVSEQ_NO();

Vector VIS_RADIO_ENABLE = market_risk.getVIS_RADIO_ENABLE();
Vector VPHYSICAL_CURVE_MST = market_risk.getVPHYSICAL_CURVE_MST();
Vector VFINANCIAL_CURVE_MST = market_risk.getVFINANCIAL_CURVE_MST();
Vector VCURVE_NM = market_risk.getVCURVE_NM();
Vector VPRICE_RANGE = market_risk.getVPRICE_RANGE();
Vector VPRICE_START_DT = market_risk.getVPRICE_START_DT();
Vector VPRICE_END_DT = market_risk.getVPRICE_END_DT();

Vector VCURVE_LOGIC = market_risk.getVCURVE_LOGIC();
Vector VFORMULA = market_risk.getVFORMULA();
Vector VMIN_PRICE_ST_END_DT = market_risk.getVMIN_PRICE_ST_END_DT();

Vector VPRICE_DECI = market_risk.getVPRICE_DECI();
Vector VFINAL_PRICE_DECI = market_risk.getVFINAL_PRICE_DECI();
Vector VPREM_DISC_RATE = market_risk.getVPREM_DISC_RATE();

String num_price_line = "";
String price_line_start_dt = "";
String price_line_end_dt = "";
String isDefaulter="";
if (VSTART_DT.size() <= 0)
{
	price_line_start_dt = cont_start_dt;
	price_line_end_dt = cont_end_dt;
	//	isDefaulter="Y";	
}	
%>
<body>
<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_VariablePricing">

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
					    	Price Configuration <%=counterparty_abbr%> (<%=display_map_id%>) [Contract Period : <%=cont_start_dt %> - <%=cont_end_dt %>]
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th></th>										
										<th>From</th>
										<th>To</th>
										<th>Pricing Type</th>
										<th>Curve Logic</th>										
										<th>Price Detail</th>										
										<th>Settle Price/Mth Logic</th>
										<th>Slope</th>
										<th>Constant</th>
										<th>Premium(+)/ Discount(-)</th>
										<th>Price Upto (Decimal)</th>
										<th>Final Price Upto (Decimal)</th>
										<th>Physical Curve</th>
										<th>Price Formula</th>
										<th>Remark</th>
									</tr>
								</thead>
								<tbody>
								<%if(VPRICE_TYPE.size()>0) {%>
									<%for(int i=0;i<VPRICE_TYPE.size(); i++){%>	
									<tr>
										<td align="center">
											<%=i+1%>
										</td>
										<td align="center">
											<input type="hidden" name="line_start_date" id="line_start_date<%=i%>" value="<%=VSTART_DT.elementAt(i)%>" >
											<input type="hidden" name="line_seq_no" id="line_seq_no<%=i%>" value="<%=VSEQ_NO.elementAt(i)%>" >
											<%=VSTART_DT.elementAt(i)%>
										</td>
										<td align="center">
											<input type="hidden" name="line_end_date" id="line_end_date<%=i%>" value="<%=VEND_DT.elementAt(i)%>" >
											<%=VEND_DT.elementAt(i)%>
										</td>																	
										<td align="center" >
											<%if(VPRICE_TYPE.elementAt(i).equals("F")) {%>
												FIXED PRICE
											<%}else{%>
												CURVE PRICE
											<%}%>
										</td>
										<td align="center"><%=VCURVE_LOGIC.elementAt(i)%></td>
										<td align="center">
											<%if(VPRICE_TYPE.elementAt(i).equals("F")) {%>
												<%=VPRICE_RATE.elementAt(i)%> <%=VPRICE_RATE_UNIT_NM.elementAt(i)%> /MMBTU
											<%}else{%>
												<%=VCURVE_NM.elementAt(i) %>
											<%}%></td>											
										<td align="center"><%=VPRICE_RANGE_NM.elementAt(i)%></td>										
										<td align="right"><%=VSLOPE.elementAt(i)%></td>
										<td align="right"><%=VCONSTANT.elementAt(i)%></td>
										<td align="right"><%=VPREM_DISC_RATE.elementAt(i) %></td>
										<td align="center"><%=VPRICE_DECI.elementAt(i) %></td>
										<td align="center"><%=VFINAL_PRICE_DECI.elementAt(i) %></td>
										<td align="center"><%=VPHYSICAL_CURVE.elementAt(i)%></td>	
										<td align="center"><%=VFORMULA.elementAt(i)%></td>									
										<td align="center"><%=VREMARK.elementAt(i)%></td>																		
									</tr>
									<%} %>
								<%}else{ %>
								<tr>
									<td colspan="15">
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


<input type="hidden" name="option" value="VARIABLE_PRICE_CONFIG">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="old_value" value="">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="cont_start_dt" value="<%=cont_start_dt%>">
<input type="hidden" name="cont_end_dt" value="<%=cont_end_dt%>">
<input type="hidden" name="cont_ref" value="<%=cont_ref%>">
<input type="hidden" name="cont_status" value="<%=cont_status%>">

<input type="hidden" name="sysdate" value="<%=sysdate%>">

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