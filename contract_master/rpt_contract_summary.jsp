
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
	var segmentType = document.forms[0].segmentType.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var flag=true;
	var msg="";
	
	var count = compareDate(from_dt,to_dt);
	if(parseInt(count) == 1)
	{
		msg+="From Date should be less or equal To Date!";
		flag=false;
	}
	
	if(trim(from_dt)=="" || trim(to_dt)=="")
	{
		msg+="Please Select Start Date and End Date!";
		flag=false;
	}
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "rpt_contract_summary.jsp?counterparty_cd="+counterparty_cd+
				"&u="+u+"&segmentType="+segmentType+"&from_dt="+from_dt+"&to_dt="+to_dt;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}

function exportToXls()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var segmentType = document.forms[0].segmentType.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var sysdate = document.forms[0].sysdate.value;
	
	var url = "xls_contract_summary.jsp?fileName=ContractSummary "+sysdate+".xls&counterparty_cd="+counterparty_cd+"&segmentType="+segmentType+"&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_master.DB_ContractMaster_Report" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String firstDate="01/"+sysdate.substring(3, sysdate.length());

String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?firstDate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

contract.setCallFlag("CONTRACT_SUMMARY");
contract.setComp_cd(owner_cd);
contract.setSegmentType(segmentType);
contract.setCounterparty_cd(counterparty_cd);
contract.setFrom_dt(from_dt);
contract.setTo_dt(to_dt);
contract.init();

Vector VCOUNTERPARTY_CD = contract.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = contract.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = contract.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = contract.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = contract.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = contract.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = contract.getVCONT_NO();
Vector VCONT_REV_NO = contract.getVCONT_REV_NO();
Vector VAGMT_NO = contract.getVAGMT_NO();
Vector VAGMT_REV_NO = contract.getVAGMT_REV_NO();
Vector VTCQ = contract.getVTCQ();
Vector VTCQ_MMSCM = contract.getVTCQ_MMSCM();
Vector VSIGNING_DT = contract.getVSIGNING_DT();
Vector VSTART_DT = contract.getVSTART_DT();
Vector VEND_DT = contract.getVEND_DT();
Vector VRATE = contract.getVRATE();
Vector VRATE_UNIT = contract.getVRATE_UNIT();
Vector VDIS_CONT_MAPPING = contract.getVDIS_CONT_MAPPING();
Vector VCONT_STATUS = contract.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = contract.getVCONT_STATUS_FLG();
Vector VCONT_REF = contract.getVCONT_REF();
Vector VENT_DT = contract.getVENT_DT();
Vector VENT_BY = contract.getVENT_BY();
Vector VAPRV_DT = contract.getVAPRV_DT();
Vector VAPRV_BY = contract.getVAPRV_BY();
Vector VPRICE_TYPE = contract.getVPRICE_TYPE();
Vector VDELV_POINT = contract.getVDELV_POINT();
Vector VPLANT_UNIT = contract.getVPLANT_UNIT();
Vector VIS_ALLOCATED = contract.getVIS_ALLOCATED();
Vector VSUPPLIED_QTY_MMBTU = contract.getVSUPPLIED_QTY_MMBTU();
Vector VSUPPLIED_QTY_MMSCM = contract.getVSUPPLIED_QTY_MMSCM();
Vector VBALANCE_QTY_MMBTU = contract.getVBALANCE_QTY_MMBTU();
Vector VBALANCE_QTY_MMSCM = contract.getVBALANCE_QTY_MMSCM();
Vector VALLOC_MIN_DT = contract.getVALLOC_MIN_DT();
Vector VALLOC_MAX_DT = contract.getVALLOC_MAX_DT();
Vector VBU_POINT = contract.getVBU_POINT();

Vector VSEGMENT = contract.getVSEGMENT();
Vector VSEGMENT_TYPE = contract.getVSEGMENT_TYPE();
Vector VTEMP_SEGMENT = contract.getVTEMP_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = contract.getVTEMP_SEGMENT_TYPE();
Vector VINDEX = contract.getVINDEX();
Vector VDUE_DATE = contract.getVDUE_DATE();
Vector VEXCHANGE_RATE = contract.getVEXCHANGE_RATE();
Vector VEXCHNG_RATE_NM = contract.getVEXCHNG_RATE_NM();

Vector VTOTAL_MMBTU = contract.getVTOTAL_MMBTU();
Vector VTOTAL_SCM = contract.getVTOTAL_SCM();
Vector VTOTALSUPPLIED_MMBTU = contract.getVTOTALSUPPLIED_MMBTU();
Vector VTOTALSUPPLIED_SCM = contract.getVTOTALSUPPLIED_SCM();
Vector VTOTALBALANCE_MMBTU = contract.getVTOTALBALANCE_MMBTU();
Vector VTOTALBALANCE_SCM = contract.getVTOTALBALANCE_SCM();
Vector VCONTRACT_TYPE = contract.getVCONTRACT_TYPE();
Vector VHEADER_SEGMENT = contract.getVHEADER_SEGMENT();
Vector VSEGMENT_INDEX = contract.getVSEGMENT_INDEX();

Vector VDCQ = contract.getVDCQ();
Vector VTOTAL_DCQ = contract.getVTOTAL_DCQ();

Vector VDISP_SEGMENT = contract.getVDISP_SEGMENT();
Vector VDISP_SEGMENT_TYPE = contract.getVDISP_SEGMENT_TYPE();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form action="">
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
				    		Contract Summary Report 
	   	 				</div>
	   	 				<!--Harsh Maheta 20230310 : XLS file for Excel Export functionality-->
	   	 				<a>
							<span class="input-group-text">
							 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
							</span>
						</a>
				    </div>
				</div>      	
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Contract</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="segmentType" onchange="refresh()">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VDISP_SEGMENT.size();i++){ %>
										<option value="<%=VDISP_SEGMENT_TYPE.elementAt(i)%>"><%=VDISP_SEGMENT.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].segmentType.value="<%=segmentType%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%
					int ctn=0;
					int l=0;
					int m=0;
					int j=0;
					int i=0;
					int k=0;
					for(l=0; l<VHEADER_SEGMENT.size();l++){
					 int segment_index=Integer.parseInt(""+VSEGMENT_INDEX.elementAt(l));%>
					 <div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading1">
										<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l %>" aria-expanded="false" aria-controls="collapse<%=l%>"><%=VHEADER_SEGMENT.elementAt(l) %>&nbsp;</button>	
							    	</h2>
							    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading1">
							    		<div class="accordion-body accor-body">
											<div class="row">
												<%m=0;
												for(j=j; j<VTEMP_SEGMENT.size(); j++){ 
													int index = Integer.parseInt(""+VINDEX.elementAt(j));
													m+=1;
												if(j!=0)
												{
												%>
												<div class="row">
													<div class="col-sm-12 col-xs-12 col-md-12">
													&nbsp;
													</div>
												</div> 
												<%} %>
												<div class="row m-b-5">
													<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VTEMP_SEGMENT.elementAt(j) %></label>
												</div>
												<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr>
																	<th rowspan="2">Sr#</th>
																	<th rowspan="2">Customer</th>
																	<%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("A")){%>
																		<th rowspan="2">Cargo#</th>
																		<th rowspan="2">Contract Ref#</th>
																		<th rowspan="2">Signing Date</th>
																		<th rowspan="2">Cargo Arrival - Storage Window</th>
																	<%}else{ %>
																		<th rowspan="2">Contract#</th>
																		<%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("X")){ %>
																		<th rowspan="2">Trade Ref#</th>
																		<%}else{ %>
																		<th rowspan="2">Contract Ref#</th>
																		<%} %>
																		<th rowspan="2">Signing Date</th>
																		<th rowspan="2">Contract Period</th>
																	<%} %>
																	<th rowspan="2">Status</th>	
																	<th rowspan="2">Price Type</th>																			
																	<th rowspan="2">Contract Price</th>
																	<th rowspan="2">Currency/MMBTU</th>
																	<th rowspan="2">Exchange Rate</th>
																	<th rowspan="2">Payment Due Period(Days)</th>
																	<th rowspan="2">Business Unit</th>
																	<th rowspan="2">Delivery Point</th>
																	<th rowspan="2">Customer Plants</th>
																	<th rowspan="2">Contract Enter By/Dt </th>
																	<th rowspan="2">Contract Approved By/Dt </th>
																	<th rowspan="2">Allocation Start Date</th>
																	<th rowspan="2">Last Allocation Date</th>
																	<th rowspan="2">DCQ</th> 
																	<th colspan="2">Booked</th>
																	<th colspan="2"><%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("A")){%>Regasified<%}else{ %>Supplied<%} %></th>
																	<th colspan="2">Balance</th>
																</tr>
																<tr>
																	<th>MMBTU</th>
																	<th>MMSCM</th>
																	<th>MMBTU</th>
																	<th>MMSCM</th>
																	<th>MMBTU</th>
																	<th>MMSCM</th>
																</tr>
															</thead>
															<tbody>
															<%k=0;
															if(index > 0){ %>
																<%for(i=i;i<VCOUNTERPARTY_CD.size(); i++){ 
																k+=1;
																%>
																	<tr>
																		<td align="center"><%=k%></td>
																		<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
																		<%-- <%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("A")){%> --%>
																			<td><%=VCONTRACT_TYPE.elementAt(i)%></td>
																		<%-- <%}else{ %>
																			<td><%=VDIS_CONT_MAPPING.elementAt(i)%></td>
																		<%} %> --%>
																		<td><%=VCONT_REF.elementAt(i)%></td>
																		<td><%=VSIGNING_DT.elementAt(i)%></td>
																		<td><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
																		<%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("A")){%>
																			<td>
																				<%=VCONT_STATUS.elementAt(i)%>
																			</td>
																		<%}else{ %>
																			<td <%if(VIS_ALLOCATED.elementAt(i).equals("Y")){ %>style="background:#99ffcc;"<%}else{ %>style="background:#ffffcc;"<%} %>>
																				<%=VCONT_STATUS.elementAt(i)%>
																			</td>
																		<%} %>
																		<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
																		<td align="right"><%=VRATE.elementAt(i)%></td>
																		<td align="center"><%=VRATE_UNIT.elementAt(i)%></td>
																		<td><%=VEXCHNG_RATE_NM.elementAt(i) %></td>
																		<td><%=VDUE_DATE.elementAt(i) %></td>
																		<td><%=VBU_POINT.elementAt(i) %></td>
																		<td><%=VDELV_POINT.elementAt(i) %></td>
																		<td><%=VPLANT_UNIT.elementAt(i) %></td>
																		<td align="center"><%=VENT_BY.elementAt(i)%><br><%=VENT_DT.elementAt(i)%></td>
																		<td align="center"><%=VAPRV_BY.elementAt(i)%><br><%=VAPRV_DT.elementAt(i)%></td>
																		<td align="center"><%=VALLOC_MIN_DT.elementAt(i)%></td>
																		<td align="center"><%=VALLOC_MAX_DT.elementAt(i)%></td>
																		<td align="right"><%=VDCQ.elementAt(i) %></td>
																		<td align="right"><%=VTCQ.elementAt(i)%></td>
																		<td align="right"><%=VTCQ_MMSCM.elementAt(i)%></td>
																		<td align="right"><%=VSUPPLIED_QTY_MMBTU.elementAt(i)%></td>
																		<td align="right"><%=VSUPPLIED_QTY_MMSCM.elementAt(i)%></td>
																		<td align="right"><%=VBALANCE_QTY_MMBTU.elementAt(i)%></td>
																		<td align="right"><%=VBALANCE_QTY_MMSCM.elementAt(i)%></td>
																	</tr>
																	<%if(k==index)
																	{%>
																		<tr>
																		<td colspan="19" align="right"><b>Total :</b></td>
																		<td align="right"><b><%=VTOTAL_DCQ.elementAt(j) %></b></td>
																		<td align="right"><b><%=VTOTAL_MMBTU.elementAt(j) %></b></td>
																		<td align="right"><b><%=VTOTAL_SCM.elementAt(j) %></b></td>
																		<td align="right"><b><%=VTOTALSUPPLIED_MMBTU.elementAt(j) %></b></td>
																		<td align="right"><b><%=VTOTALSUPPLIED_SCM.elementAt(j) %></b></td>
																		<td align="right"><b><%=VTOTALBALANCE_MMBTU.elementAt(j) %></b></td>
																		<td align="right"><b><%=VTOTALBALANCE_SCM.elementAt(j) %></b></td>
																	</tr>
																		<% i=i+1;
																		break;
																	}%>
																<%} %>
															<%}else{ %>
																<tr>
																	<td colspan="26" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
																</tr>
															<%} %>
															</tbody>
														</table>
													</div>
												</div>
												<%if(m==segment_index)
												{
													j=j+1;
													break;
												}%>
												<%} %>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

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