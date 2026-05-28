<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var u = document.forms[0].u.value;
	
	
		var url = "rpt_contract_wise.jsp?counterparty_cd="+counterparty_cd+"&month="+month+"&year="+year+
				"&u="+u;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	
}

function doSubmit()
{
	var counterpartyName = document.forms[0].counterpartyNm.value;
	var counterpartyCd = document.forms[0].counterparty_cd.value;
	var msg = "";
	
	if(counterpartyCd!="0")
	{
		msg = "This is submit expected sales data for "+counterpartyName+" !\n Do You Want to Procced ?";
	}
	else
	{
		msg = "This is submit expected sales data for All Counterparty !\n Do You Want to Procced ?";
	}
	var a = confirm(msg);
	if(a)
	{
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].submit();
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_master.DB_ContractMaster_Report" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String month=request.getParameter("month")==null?"0"+currentMonth:request.getParameter("month");
	if(month.length()==1){
		month="0"+month;
	}else{
		month=""+month;
	}
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");

String user_cd=(String)session.getAttribute("user_cd");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

contract.setCallFlag("CONTRACT_WISE");
contract.setComp_cd(owner_cd);
contract.setMonth(month);
contract.setYear(year);
contract.setCounterparty_cd(counterparty_cd);
contract.init();

Vector VCOUNTERPARTY_CD = contract.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = contract.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = contract.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = contract.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = contract.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = contract.getVMST_COUNTERPARTY_ABBR();
Vector VSEGMENT = contract.getVSEGMENT();
Vector VSEGMENT_TYPE = contract.getVSEGMENT_TYPE();
Vector VDIS_CONT_MAPPING = contract.getVDIS_CONT_MAPPING();
Vector VCONT_REF = contract.getVCONT_REF();
Vector VSIGNING_DT = contract.getVSIGNING_DT();
Vector VRATE = contract.getVRATE();
Vector VRATE_UNIT = contract.getVRATE_UNIT();
Vector VSTART_DT = contract.getVSTART_DT();
Vector VEND_DT = contract.getVEND_DT();
Vector VCONT_STATUS_FLG = contract.getVCONT_STATUS_FLG();
Vector VCONT_STATUS = contract.getVCONT_STATUS();
Vector VINDEX = contract.getVINDEX();
Vector VSUB_INDEX = contract.getVSUB_INDEX();
Vector VTCQ = contract.getVTCQ();
Vector VTOTAL_TCQ = contract.getVTOTAL_TCQ();
Vector VALLOC_MIN_DT = contract.getVALLOC_MIN_DT();
Vector VALLOC_MAX_DT = contract.getVALLOC_MAX_DT();
Vector<Double> VSUPPLIED_QTY_MMBTU = contract.getVSUPPLIED_QTY_MMBTU();
Vector VSALES_AMT_INR = contract.getVSALES_AMT_INR();
Vector VSALES_AMT_USD = contract.getVSALES_AMT_USD();
Vector VBALANCEQTY = contract.getVBALANCEQTY();
Vector VTOTAL_SUPPLIED_QTY_MMBTU = contract.getVTOTAL_SUPPLIED_QTY_MMBTU();
Vector VTOTAL_BALANCE_QTY_MMBTU = contract.getVTOTAL_BALANCE_QTY_MMBTU();
Vector VTOTAL_SALES_AMT_INR = contract.getVTOTAL_SALES_AMT_INR();
Vector VTOTAL_SALES_AMT_USD = contract.getVTOTAL_SALES_AMT_USD();
Vector VAGMT_NO = contract.getVAGMT_NO();
Vector VAGMT_REV_NO = contract.getVAGMT_REV_NO();
Vector VCONT_NO = contract.getVCONT_NO();
Vector VCONT_REV_NO = contract.getVCONT_REV_NO();
Vector VCONTRACT_TYPE = contract.getVCONTRACT_TYPE();
Vector VAPPROVED_FLAG = contract.getVAPPROVED_FLAG();
Vector VAPPROVED_DATE = contract.getVAPPROVED_DATE();
Vector VAPPROVED_COUNTERPARTY_CD = contract.getVAPPROVED_COUNTERPARTY_CD();
Vector VAPPROVED_COUNTERPARTY_NM = contract.getVAPPROVED_COUNTERPARTY_NM();
Vector VAPPROVED_COLOR = contract.getVAPPROVED_COLOR();
Vector VTEMP_RATE_UNIT = contract.getVTEMP_RATE_UNIT();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_ContracMaster">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
		<%if(!msg.equals("")){ %>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
			<% } %>
			<div class="card cardmain">
				<div class="card-header cdheader topheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
						    Contract Wise Report(Expected Sales)
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-12">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Month</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="month" id="month" onchange="refresh();">
										<option value="0" lable="Select" selected="selected">--Select--</option>
										<option value="01" label="January">January</option>
										<option value="02" label="February">February</option>
										<option value="03" label="March">March</option>
										<option value="04" label="April">April</option>
										<option value="05" label="May">May</option>
										<option value="06" label="June">June</option>
										<option value="07" label="July">July</option>
										<option value="08" label="August">August</option>
										<option value="09" label="September">September</option>
										<option value="10" label="October">October</option>
										<option value="11" label="November">November</option>
										<option value="12" label="December">December</option>
									</select>
									<script>document.forms[0].month.value="<%=month%>"</script>
								</div>
								<div class="col-auto">
									<label class="form-label"><b>Year</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="year" id="year" onchange="refresh();">
										<option value="0" lable="Select" selected="selected">--Select--</option>
					  					<%for(int i=(currentYear); i > (currentYear-10);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
								<div class="col-auto">
									<label class="form-label"><b>Customer</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0" lable="All" selected="selected">--All--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
											<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %> 
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div align="center">
					<%if(!VAPPROVED_FLAG.contains("Y") && VCOUNTERPARTY_CD.size()!=0 && approve_access.equals("Y"))
					{ %>
						<input type="button" class="btn btn-warning com-btn" value="Approve Expected Sales Figure" onclick="doSubmit();">
					<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Approve Expected Sales Figure" disabled>
					<%} %>
					</div>&nbsp;
					<div align="center">
						<div class="col-sm-6 col-xs-6 col-md-6">
							<div class="form-group row">
			    				<div class="col-sm-12 col-xs-12 col-md-12">
			    					<div align="left">
			    					<% for (int a=0; a<VAPPROVED_COUNTERPARTY_CD.size(); a++){
										 if(VAPPROVED_FLAG.contains("Y")){ %>
										<%=utilmsg.infoMessage("<b>Expected Sales Figure approved for "+VAPPROVED_COUNTERPARTY_NM.elementAt(a)+" since "+VAPPROVED_DATE.elementAt(a)+"</b>")%>
										<%} %>
									<%} %>
									</div>
			    				</div>
			    			</div>
						</div>
					</div>&nbsp;
					<%int i=0;int k=0; int l=0; int j=0;
					for(j=0; j<VSEGMENT_TYPE.size(); j++){
					int index = Integer.parseInt(""+VINDEX.elementAt(j));%> 
					<% if(j!=0)
						{%>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">&nbsp;
							</div>
						</div> 
						<%} %>
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;<%=VSEGMENT.elementAt(j)%></label>
						</div>
						<%if(index>0){ %>
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<%int m=0;
								for(i=i; i<VCOUNTERPARTY_CD.size(); i++){
									int sub_index = Integer.parseInt(""+VSUB_INDEX.elementAt(i));
									m++;
								%>
								<input type="hidden" name="sub_index" value="">
								<div class="accordion">
									<div class="accordion-item accor_item">
									
										<h2 class="accordion-header" id="heading<%=1%>">
	   										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
		   										 <%=VCOUNTERPARTY_NM.elementAt(i) %>
								      		</button>
								    	</h2>
								    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
								      		<div class="accordion-body accor-body">
												<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr>
																	<th>Contract#</th>
																	<th>
																		<%if(!VSEGMENT_TYPE.elementAt(j).equals("X")){ %>
																		Contract Ref#
																		<%}else{%>
																		Trade Ref#
																		<%} %>
																	</th> 
																	<th>Contract Period</th>
																	<th>Booked MMBTU</th>  
																	<th>Supplied MMBTU</th> 
																	<th>Balance MMBTU</th>
																	<th>Signing Date</th> 
																	<th>Contract Price</th> 
																	<th>Currency/MMBTU</th>
																	<th>Allocation Start Date</th> 
																	<th>Allocation End Date</th>           
																	<th>Expected Sales(INR)</th>
																	<th>Expected Sales(USD)</th> 
																</tr>
															</thead>
															<tbody>
																<%k=0;
																	if(sub_index > 0){ %>
																		<% 
																		for(l=l;l<VCOUNTERPARTY_CD.size(); l++){
																		k+=1;
																		%>
																		<tr>
																			<td><font style="background: <%=VAPPROVED_COLOR.elementAt(l)%>"><%=VDIS_CONT_MAPPING.elementAt(l)%></font>
<%-- 																				<input type="text" class="form-control form-control-sm" name="dis_cont_mapping" id="dis_cont_mapping<%=l%>" value="<%=VDIS_CONT_MAPPING.elementAt(l)%>" style="background:<%=VAPPROVED_COLOR.elementAt(l)%>" disabled> --%>
																				<input type="hidden" name="counterpartyCd" id="counterpartyCd<%=l%>" value="<%=VCOUNTERPARTY_CD.elementAt(l)%>">
																				<input type="hidden" name="agmt_no" id="agmt_no<%=l%>" value="<%=VAGMT_NO.elementAt(l)%>">
																				<input type="hidden" name="agmt_rev" id="agmt_rev<%=l%>" value="<%=VAGMT_REV_NO.elementAt(l)%>">
																				<input type="hidden" name="cont_no" id="cont_no<%=l%>" value="<%=VCONT_NO.elementAt(l)%>">
																				<input type="hidden" name="cont_rev" id="cont_rev<%=l%>" value="<%=VCONT_REV_NO.elementAt(l)%>">
																				<input type="hidden" name="contract_type" id="contract_type<%=l%>" value="<%=VCONTRACT_TYPE.elementAt(l)%>">
																				<input type="hidden" name="Month" id="Month<%=l%>" value="<%=month%>">
																				<input type="hidden" name="Year" id="Year<%=l%>" value="<%=year%>">
																				<input type="hidden" name="approve_flag" id="approve_flag<%=l%>" value="Y">
																			</td>
																			<td><%=VCONT_REF.elementAt(l)%></td>
																			<td align="center"><%=VSTART_DT.elementAt(l)%>-<%=VEND_DT.elementAt(l)%></td>
																			<td align="right"><input type="text" class="form-control form-control-sm" name="tcq" id="tcq<%=l%>" value="<%=VTCQ.elementAt(l)%>" style="text-align:right;background:<%=VAPPROVED_COLOR.elementAt(l)%>" disabled>
																				<input type="hidden" name="temp_tcq" id="temp_tcq<%=l%>" value="<%=VTCQ.elementAt(l)%>">
																			</td>
																			<td align="right"><input type="text" class="form-control form-control-sm" name="supplied_qty" id="supplied_qty<%=l%>" value="<%=VSUPPLIED_QTY_MMBTU.elementAt(l)%>" style="text-align:right;background:<%=VAPPROVED_COLOR.elementAt(l)%>" disabled>
																				<input type="hidden" name="temp_supplied_qty" id="temp_supplied_qty<%=l%>" value="<%=VSUPPLIED_QTY_MMBTU.elementAt(l)%>">
																			</td>
																			<td align="right"><input type="text" class="form-control form-control-sm" name="balance_qty" id="balance_qty<%=l%>" value="<%=VBALANCEQTY.elementAt(l)%>" style="text-align:right;background:<%=VAPPROVED_COLOR.elementAt(l)%>" disabled>
																				<input type="hidden" name="temp_balance_qty" id="temp_balance_qty<%=l%>" value="<%=VBALANCEQTY.elementAt(l)%>">
																			</td>
																			<td align="center"><%=VSIGNING_DT.elementAt(l) %></td>
																			<td align="right"><input type="text" class="form-control form-control-sm" name="sales_rate" id="sales_rate<%=l%>" value="<%=VRATE.elementAt(l)%>" style="text-align:right;background:<%=VAPPROVED_COLOR.elementAt(l)%>" disabled>
																				<input type="hidden" name="temp_sales_rate" id="temp_sales_rate<%=l%>" value="<%=VRATE.elementAt(l)%>">
																			</td>
																			<td align="center"><input type="text" class="form-control form-control-sm" name="rate_unit" id="rate_unit<%=l%>" value="<%=VRATE_UNIT.elementAt(l)%>" style="text-align:center;background:<%=VAPPROVED_COLOR.elementAt(l)%>" disabled>
																				<input type="hidden" name="temp_rate_unit" id="temp_rate_unit<%=l%>" value="<%=VTEMP_RATE_UNIT.elementAt(l)%>">
																			</td>
																			<td align="center"><%=VALLOC_MIN_DT.elementAt(l) %></td>
																			<td align="center"><%=VALLOC_MAX_DT.elementAt(l) %></td>
																			<td align="right"><input type="text" class="form-control form-control-sm" name="sales_amt_inr" id="sales_amt_inr<%=l%>" value="<%=VSALES_AMT_INR.elementAt(l)%>" style="text-align:right;background:<%=VAPPROVED_COLOR.elementAt(l)%>" disabled>
																				<input type="hidden" name="temp_sales_amt_inr" id="temp_sales_amt_inr<%=l%>" value="<%=VSALES_AMT_INR.elementAt(l)%>">
																			</td>
																			<td align="right"><input type="text" class="form-control form-control-sm" name="sales_amt_usd" id="sales_amt_usd<%=l%>" value="<%=VSALES_AMT_USD.elementAt(l)%>" style="text-align:right;background:<%=VAPPROVED_COLOR.elementAt(l)%>" disabled>
																				<input type="hidden" name="temp_sales_amt_usd" id="temp_sales_amt_usd<%=l%>" value="<%=VSALES_AMT_USD.elementAt(l)%>">
																			</td>
																		</tr>
																		   
																		<%if(sub_index==k)
																		{
																		%>
																			<tr>
																				<td align="right"><b>Total :</b></td>
																				<td colspan="2"></td>
																				<td align="right"><b><%=VTOTAL_TCQ.elementAt(i) %></b></td>
																				<td align="right"><b><%=VTOTAL_SUPPLIED_QTY_MMBTU.elementAt(i) %></b></td>
																				<td align="right"><b><%=VTOTAL_BALANCE_QTY_MMBTU.elementAt(i) %></b></td>
																				<td colspan="3"></td>
																				<td colspan="2"></td>
																				<td align="right"><b><%=VTOTAL_SALES_AMT_INR.elementAt(i) %></b></td>
																				<td align="right"><b><%=VTOTAL_SALES_AMT_USD.elementAt(i) %></b></td>
																			</tr>
																			<% l=l+1;
																			break;
																		   }
																	    }%>
																	<%} else{%>
																	<tr>
																		<td colspan="22" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
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
								<%if(index==m)
								{
									i=i+1;
									break;
								}
							 }%>
							</div>
						</div>
						<%}else{%>
							<div colspan="22" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></div>
						<%} %>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>
<%if(VCOUNTERPARTY_NM.size()>0){ %>
	<input type="hidden" name="counterpartyNm" id="counterpartyNm<%=i%>" value="<%=VCOUNTERPARTY_NM.elementAt(0)%>">
<%} %>
<input type="hidden" name="option" value="Exp_sales">
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