
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
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "rpt_dlng_contract_summary.jsp?counterparty_cd="+counterparty_cd+
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
	
	var url = "xls_dlng_contract_summary.jsp?fileName=DlngContractSummary "+sysdate+".xls&counterparty_cd="+counterparty_cd+"&segmentType="+segmentType+"&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String firstDate="01/"+sysdate.substring(3, sysdate.length());

String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?firstDate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

dlng.setCallFlag("DLNG_CONTRACT_SUMMARY");
dlng.setComp_cd(owner_cd);
dlng.setSegmentType(segmentType);
dlng.setCounterparty_cd(counterparty_cd);
dlng.setFrom_dt(from_dt);
dlng.setTo_dt(to_dt);
dlng.init();

Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = dlng.getVCONT_NO();
Vector VCONT_REV_NO = dlng.getVCONT_REV_NO();
Vector VAGMT_NO = dlng.getVAGMT_NO();
Vector VAGMT_REV_NO = dlng.getVAGMT_REV_NO();
Vector VTCQ = dlng.getVTCQ();
Vector VTCQ_MMSCM = dlng.getVTCQ_MMSCM();
Vector VSIGNING_DT = dlng.getVSIGNING_DT();
Vector VSTART_DT = dlng.getVSTART_DT();
Vector VEND_DT = dlng.getVEND_DT();
Vector VRATE = dlng.getVRATE();
Vector VRATE_UNIT = dlng.getVRATE_UNIT();
Vector VDIS_CONT_MAPPING = dlng.getVDIS_CONT_MAPPING();
Vector VCONT_STATUS = dlng.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = dlng.getVCONT_STATUS_FLG();
Vector VCONT_REF = dlng.getVCONT_REF();
Vector VENT_DT = dlng.getVENT_DT();
Vector VENT_BY = dlng.getVENT_BY();
Vector VAPRV_DT = dlng.getVAPRV_DT();
Vector VAPRV_BY = dlng.getVAPRV_BY();
Vector VPRICE_TYPE = dlng.getVPRICE_TYPE();
Vector VDELV_POINT = dlng.getVDELV_POINT();
Vector VPLANT_UNIT = dlng.getVPLANT_UNIT();
Vector VIS_ALLOCATED = dlng.getVIS_ALLOCATED();
Vector VSUPPLIED_QTY_MMBTU = dlng.getVSUPPLIED_QTY_MMBTU();
Vector VSUPPLIED_QTY_MMSCM = dlng.getVSUPPLIED_QTY_MMSCM();
Vector VBALANCE_QTY_MMBTU = dlng.getVBALANCE_QTY_MMBTU();
Vector VBALANCE_QTY_MMSCM = dlng.getVBALANCE_QTY_MMSCM();
Vector VALLOC_MIN_DT = dlng.getVALLOC_MIN_DT();
Vector VALLOC_MAX_DT = dlng.getVALLOC_MAX_DT();
Vector VBU_POINT = dlng.getVBU_POINT();

Vector VSEGMENT = dlng.getVSEGMENT();
Vector VSEGMENT_TYPE = dlng.getVSEGMENT_TYPE();
Vector VTEMP_SEGMENT = dlng.getVTEMP_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = dlng.getVTEMP_SEGMENT_TYPE();
Vector VINDEX = dlng.getVINDEX();
Vector VDUE_DATE = dlng.getVDUE_DATE();
Vector VEXCHANGE_RATE = dlng.getVEXCHANGE_RATE();
Vector VEXCHNG_RATE_NM = dlng.getVEXCHNG_RATE_NM();

Vector VTOTAL_MMBTU = dlng.getVTOTAL_MMBTU();
Vector VTOTAL_SCM = dlng.getVTOTAL_SCM();
Vector VTOTALSUPPLIED_MMBTU = dlng.getVTOTALSUPPLIED_MMBTU();
Vector VTOTALSUPPLIED_SCM = dlng.getVTOTALSUPPLIED_SCM();
Vector VTOTALBALANCE_MMBTU = dlng.getVTOTALBALANCE_MMBTU();
Vector VTOTALBALANCE_SCM = dlng.getVTOTALBALANCE_SCM();
Vector VCONTRACT_TYPE = dlng.getVCONTRACT_TYPE();
Vector VHEADER_SEGMENT = dlng.getVHEADER_SEGMENT();
Vector VSEGMENT_INDEX = dlng.getVSEGMENT_INDEX();

Vector VDCQ = dlng.getVDCQ();
Vector VTOTAL_DCQ = dlng.getVTOTAL_DCQ();
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
				    		DLNG Contract Summary Report 
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
										<%for(int i=0;i<VSEGMENT.size();i++){ %>
										<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT.elementAt(i)%></option>
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
							<%-- <div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading1">
										<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l %>" aria-expanded="false" aria-controls="collapse<%=l%>"><%=VHEADER_SEGMENT.elementAt(l) %>&nbsp;</button>	
							    	</h2>
							    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading1">
							    		<div class="accordion-body accor-body"> --%>
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
														<table class="table table-bordered table-hover" id="filterbysearch_<%=j%>">
															<thead>
																<tr>
																	<th rowspan="2">Sr#</th>
																	<th rowspan="2">Customer<div align="center"><input class="form-control form-control-sm" type="text" id="contCp_<%=j %>" onkeyup="Search(this,'1','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
																	<th rowspan="2">Contract#<div align="center"><input class="form-control form-control-sm" type="text" id="contNum_<%=j %>" onkeyup="Search(this,'2','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
																	<%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("X")){ %>
																	<th rowspan="2">Trade Ref#</th>
																	<%}else{ %>
																	<th rowspan="2">Contract Ref#</th>
																	<%} %>
																	<th rowspan="2">Signing Date</th>
																	<th rowspan="2">Contract Period</th>
																	<th rowspan="2">Status</th>	
																	<th rowspan="2">Price Type</th>																			
																	<th rowspan="2">Contract Price</th>
																	<th rowspan="2">Currency/MMBTU</th>
																	<th rowspan="2">Exchange Rate</th>
																	<th rowspan="2">Payment Due Period(Days)</th>
																	<th rowspan="2">Business Unit</th>
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
																	<th>MT</th>
																	<th>MMBTU</th>
																	<th>MT</th>
																	<th>MMBTU</th>
																	<th>MT</th>
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
																		<td <%if(VIS_ALLOCATED.elementAt(i).equals("Y")){ %>style="background:#99ffcc;"<%}else{ %>style="background:#ffffcc;"<%} %>>
																			<%=VCONT_STATUS.elementAt(i)%>
																		</td>
																		<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
																		<td align="right"><%=VRATE.elementAt(i)%></td>
																		<td align="center"><%=VRATE_UNIT.elementAt(i)%></td>
																		<td><%=VEXCHNG_RATE_NM.elementAt(i) %></td>
																		<td><%=VDUE_DATE.elementAt(i) %></td>
																		<td><%=VBU_POINT.elementAt(i) %></td>
																		<td><%=VPLANT_UNIT.elementAt(i) %></td>
																		<td align="center"><%=VENT_BY.elementAt(i)%><br><%=VENT_DT.elementAt(i)%></td>
																		<td align="center"><%=VAPRV_BY.elementAt(i)%><br><%=VAPRV_DT.elementAt(i)%></td>
																		<td align="center"><%=VALLOC_MIN_DT.elementAt(i)%></td>
																		<td align="center"><%=VALLOC_MAX_DT.elementAt(i)%></td>
																		<td align="right"><%=VDCQ.elementAt(i)%></td>
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
																		<td colspan="18" align="right"><b>Total :</b></td>
																		<td align="right"><b><%=VTOTAL_DCQ.elementAt(j)%></b></td>
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
																	<td colspan="25" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
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
										<!-- </div>
									</div>
								</div>
							</div> -->
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
<script>
function Search(obj, indx, j) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch_"+j);
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	if (td) 
    	{
      		txtValue = td.textContent || td.innerText;
      		if (txtValue.toLocaleLowerCase().indexOf(filter) > -1) {
        		tr[i].style.display = "";
        		count++;
      		} else {
      			tr[i].style.display = "none";
      		}
    	}
  	}
}
</script>
</body>
</html>