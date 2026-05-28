<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%><script>
function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var prev_counterparty_cd = document.forms[0].prev_counterparty_cd.value;
	
	var plant_seq = document.forms[0].plant_seq.value;
	var prev_plant_seq = document.forms[0].prev_plant_seq.value;
	
	var meter_seq = document.forms[0].meter_seq.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var meterNm = document.forms[0].meter_seq;
	var meter_nm = meterNm.options[meterNm.selectedIndex].text;
	
	if(prev_counterparty_cd != counterparty_cd)
	{
		plant_seq="0";
		meter_seq="0";
		meter_nm="";
	}
	else if(prev_plant_seq != plant_seq)
	{
		meter_seq="0";
		meter_nm="";
	}
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_meter_ticket_reading.jsp?counterparty_cd="+counterparty_cd+"&u="+u+
			"&from_dt="+from_dt+"&to_dt="+to_dt+"&plant_seq="+plant_seq+
			"&meter_seq="+meter_seq+"&meter_nm="+meter_nm;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String plant_seq=request.getParameter("plant_seq")==null?"0":request.getParameter("plant_seq");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String meter_seq=request.getParameter("meter_seq")==null?"0":request.getParameter("meter_seq");
String meter_nm=request.getParameter("meter_nm")==null?"":request.getParameter("meter_nm");

cont_mgmt.setCallFlag("REPORT_METER_TICKET_READING");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setFrom_dt(from_dt);
cont_mgmt.setTo_dt(to_dt);
cont_mgmt.setCounterparty_cd(counterparty_cd);
cont_mgmt.setPlant_seq(plant_seq);
cont_mgmt.setMeter_seq(meter_seq);
cont_mgmt.init();

Vector VMST_COUNTERPARTY_CD = cont_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = cont_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = cont_mgmt.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_PLANT_SEQ = cont_mgmt.getVMST_COUNTERPARTY_PLANT_SEQ();
Vector VMST_COUNTERPARTY_PLANT_ABBR = cont_mgmt.getVMST_COUNTERPARTY_PLANT_ABBR();
Vector VSUB_METER_SEQ = cont_mgmt.getVSUB_METER_SEQ();
Vector VSUB_METER_NM = cont_mgmt.getVSUB_METER_NM();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = cont_mgmt.getVCOUNTERPARTY_PLANT_ABBR();

Vector VINDEX = cont_mgmt.getVINDEX();
Vector VSUB_INDEX = cont_mgmt.getVSUB_INDEX();

Vector VGAS_DT = cont_mgmt.getVGAS_DT();
Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();
Vector VQTY_SCM = cont_mgmt.getVQTY_SCM();
Vector VQTY_BTU = cont_mgmt.getVQTY_BTU();
Vector VRECONCIL_QTY_MMBTU = cont_mgmt.getVRECONCIL_QTY_MMBTU();
Vector VRECONCIL_QTY_SCM = cont_mgmt.getVRECONCIL_QTY_SCM();
Vector VRECONCIL_QTY_BTU = cont_mgmt.getVRECONCIL_QTY_BTU();
Vector VTOTAL_QTY_MMBTU = cont_mgmt.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_SCM = cont_mgmt.getVTOTAL_QTY_SCM();
Vector VCALC_GCV = cont_mgmt.getVCALC_GCV();
Vector VCALC_NCV = cont_mgmt.getVCALC_NCV();
Vector VDEFINE_GCV = cont_mgmt.getVDEFINE_GCV();
Vector VDEFINE_NCV = cont_mgmt.getVDEFINE_NCV();

Vector VTOT_QTY_MMBTU = cont_mgmt.getVTOT_QTY_MMBTU();
Vector VTOT_QTY_SCM = cont_mgmt.getVTOT_QTY_SCM();
Vector VTOT_QTY_BTU = cont_mgmt.getVTOT_QTY_BTU();
Vector VTOT_RECONCIL_QTY_MMBTU = cont_mgmt.getVTOT_RECONCIL_QTY_MMBTU();
Vector VTOT_RECONCIL_QTY_SCM = cont_mgmt.getVTOT_RECONCIL_QTY_SCM();
Vector VTOT_RECONCIL_QTY_BTU = cont_mgmt.getVTOT_RECONCIL_QTY_BTU();
Vector VTOT_TOTAL_QTY_MMBTU = cont_mgmt.getVTOT_TOTAL_QTY_MMBTU();
Vector VTOT_TOTAL_QTY_SCM = cont_mgmt.getVTOT_TOTAL_QTY_SCM();
Vector VTOT_CALC_GCV = cont_mgmt.getVTOT_CALC_GCV();
Vector VTOT_CALC_NCV = cont_mgmt.getVTOT_CALC_NCV();
Vector VTOT_DEFINE_GCV = cont_mgmt.getVTOT_DEFINE_GCV();
Vector VTOT_DEFINE_NCV = cont_mgmt.getVTOT_DEFINE_NCV();

Vector VCOLOR = cont_mgmt.getVCOLOR();
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
				    		Meter Ticket Reading 
	   	 				</div>
	   	 				<a href="../contract_mgmt/xls_meter_ticket_reading.jsp?fileName=Meter Ticket Reading <%=sysdate%>.xls&plant_seq=<%=plant_seq%>&counterparty_cd=<%=counterparty_cd %>&from_dt=<%=from_dt%>&to_dt=<%=to_dt %>&sysdate=<%=sysdate%>&meter_seq=<%=meter_seq%>&meter_nm=<%=meter_nm%>" download="Incident Tracker">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Transporter</b></label>
								</div>
								<div class="col-auto">
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
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Transporter Plant</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="plant_seq" onchange="refresh()">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_PLANT_SEQ.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_PLANT_SEQ.elementAt(i)%>"><%=VMST_COUNTERPARTY_PLANT_ABBR.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].plant_seq.value="<%=plant_seq%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Meter</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="meter_seq" onchange="refresh()">
										<option value="0">All</option>
										<%for(int i=0;i<VSUB_METER_SEQ.size();i++){ %>
										<option value="<%=VSUB_METER_SEQ.elementAt(i)%>"><%=VSUB_METER_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].meter_seq.value="<%=meter_seq%>"</script>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%int j=0,k=0,l=0,m=0;
					for(int i=0; i<VCOUNTERPARTY_CD.size(); i++)
					{ 
						String countpty_cd=""+VCOUNTERPARTY_CD.elementAt(i);
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
					%>
					<%if(i!=0){ %>
					&nbsp;
					<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VCOUNTERPARTY_ABBR.elementAt(i)%></label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
						<%k=0;
						if(index > 0){ %>
							<%for(j=j;j<VCOUNTERPARTY_PLANT_SEQ.size(); j++) 
							{
								String countpty_plant_seq=""+VCOUNTERPARTY_PLANT_SEQ.elementAt(j);
								int sub_index = Integer.parseInt(""+VSUB_INDEX.elementAt(j));
								k+=1;
							%>
								<input type="hidden" name="sub_index" value="<%=sub_index%>">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading<%=l%>">
    										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
								    			<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(j)%>&nbsp;&nbsp;<%if(!meter_nm.equals("")){%><font color="blue">[Meter&nbsp;:&nbsp;<%=meter_nm%>]</font><%} %>
								      		</button>
								    	</h2>
								    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
								      		<div class="accordion-body accor-body">
								        		<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr>
																	<th rowspan="2">Gas Day</th>
																	<th colspan="2">Meter Qty</th>
																	<th colspan="2">Reconciliation Qty</th>
																	<th colspan="2">Daily Total Qty</th>
																	<th colspan="2">Calculated Heat Value (KCal/SCM)</th>
																	<th colspan="2">Define Heat Value (KCal/SCM)</th>
																</tr>
																<tr>
																	<th>MMBTU</th>
																	<th>SCM</th>
																	<th>MMBTU</th>
																	<th>SCM</th>
																	<th>MMBTU</th>
																	<th>SCM</th>
																	<th>GCV</th>
																	<th>NCV</th>
																	<th>GCV</th>
																	<th>NCV</th>
																</tr>
															</thead>
															<tbody>
																<%m=0;
																if(sub_index>0){ %>
																	<%for(l=l; l<VQTY_MMBTU.size(); l++)
																	{ 
																		m+=1;
																	%>
																		<tr>
																			<td align="center"><%=VGAS_DT.elementAt(l)%></td>
																			<td align="right"><%=VQTY_MMBTU.elementAt(l)%></td>
																			<td align="right"><%=VQTY_SCM.elementAt(l)%></td>
																			<td align="right"><%=VRECONCIL_QTY_MMBTU.elementAt(l)%></td>
																			<td align="right"><%=VRECONCIL_QTY_SCM.elementAt(l)%></td>
																			<td align="right" style="background:<%=VCOLOR.elementAt(l)%>"><%=VTOTAL_QTY_MMBTU.elementAt(l)%></td>
																			<td align="right" style="background:<%=VCOLOR.elementAt(l)%>"><%=VTOTAL_QTY_SCM.elementAt(l)%></td>
																			<td align="right"><%=VCALC_GCV.elementAt(l)%></td>
																			<td align="right"><%=VCALC_NCV.elementAt(l)%></td>
																			<td align="right"><%=VDEFINE_GCV.elementAt(l)%></td>
																			<td align="right"><%=VDEFINE_NCV.elementAt(l)%></td>
																		</tr>
																		<%if(m==sub_index)
																		{%>
																			<tr>
																				<td align="right"><b>Total (<%=VCOUNTERPARTY_PLANT_ABBR.elementAt(j)%>)</b></td>
																				<td align="right"><b><%=VTOT_QTY_MMBTU.elementAt(j)%></b></td>
																				<td align="right"><b><%=VTOT_QTY_SCM.elementAt(j)%></b></td>
																				<td align="right"><b><%=VTOT_RECONCIL_QTY_MMBTU.elementAt(j)%></b></td>
																				<td align="right"><b><%=VTOT_RECONCIL_QTY_SCM.elementAt(j)%></b></td>
																				<td align="right"><b><%=VTOT_TOTAL_QTY_MMBTU.elementAt(j)%></b></td>
																				<td align="right"><b><%=VTOT_TOTAL_QTY_SCM.elementAt(j)%></b></td>
																				<td align="right"><b><%=VTOT_CALC_GCV.elementAt(j)%></b></td>
																				<td align="right"><b><%=VTOT_CALC_NCV.elementAt(j)%></b></td>
																				<td align="right"><b><%=VTOT_DEFINE_GCV.elementAt(j)%></b></td>
																				<td align="right"><b><%=VTOT_DEFINE_NCV.elementAt(j)%></b></td>
																			</tr>
																			<%l=l+1;
																			break;
																		}%>
																	<%} %> 
																<%} %>
															</tbody>
														</table>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<%if(k==index)
								{
									j=j+1;
									break;
								}%>
							<%} %>
						<%} %>
						</div>
					</div>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>
				

<input type="hidden" name="prev_counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="prev_plant_seq" value="<%=plant_seq%>">

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