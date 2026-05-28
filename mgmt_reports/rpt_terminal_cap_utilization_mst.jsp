<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ page import="java.util.*" %>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var u = document.forms[0].u.value;
	var fin_year = document.forms[0].fin_year.value;
	
	var url="rpt_terminal_cap_utilization_mst.jsp?fin_year="+fin_year+"&u="+u;
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function exportToXls()
{
	var fin_year = document.forms[0].fin_year.value;
	var comp_abbr=document.forms[0].comp_abbr.value;
	
	var fileName=comp_abbr+"_Terminal Capacity Utilization Master Report"+".xls";
	
	var url="xls_terminal_cap_utilization_mst.jsp?&fin_year="+fin_year+"&fileName="+fileName;
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_Govt_Reports" id="govt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String current_fin_year=utildate.getFinancialYear(sysdate);

String fin_year = request.getParameter("fin_year")==null?""+current_fin_year:request.getParameter("fin_year");

govt_rpt.setCallFlag("TERMINAL_CAPACITY_UTILIZATION_MST");
govt_rpt.setComp_cd(owner_cd);
govt_rpt.setFinancial_year(fin_year);
govt_rpt.init();

Vector VMONTH_YEAR = govt_rpt.getVMONTH_YEAR();
Vector VRLNG_SEND_OUT_MMBTU = govt_rpt.getVRLNG_SEND_OUT_MMBTU();
Vector VDLNG_SEND_OUT_MMBTU = govt_rpt.getVDLNG_SEND_OUT_MMBTU();
Vector VTOTAL_SEND_OUT_MMBTU = govt_rpt.getVTOTAL_SEND_OUT_MMBTU();
Vector VMONTH_DAYS = govt_rpt.getVMONTH_DAYS();
Vector VTOTAL_SEND_OUT_MMSCM = govt_rpt.getVTOTAL_SEND_OUT_MMSCM();
Vector VTOTAL_SEND_OUT_MMSCMD = govt_rpt.getVTOTAL_SEND_OUT_MMSCMD();
Vector VNAMEPLATE_CAP = govt_rpt.getVNAMEPLATE_CAP();
Vector VMMSCMD_BY_NP = govt_rpt.getVMMSCMD_BY_NP();
Vector VMMTPA_CAP = govt_rpt.getVMMTPA_CAP();
Vector VNAMEPLATE_CAP_PER_MTH = govt_rpt.getVNAMEPLATE_CAP_PER_MTH();
Vector VMMTPA_CAP_PER_MTH = govt_rpt.getVMMTPA_CAP_PER_MTH();
Vector VCAP_UTILIZE_PER = govt_rpt.getVCAP_UTILIZE_PER();
Vector VCUM_CAP_UTILIZE_PER = govt_rpt.getVCUM_CAP_UTILIZE_PER();
Vector VMST_FINANCIAL_YEAR = govt_rpt.getVMST_FINANCIAL_YEAR();

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
					    	Terminal Capacity Utilization Master Report
					    </div>
					    <div class="col-auto">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x" onclick="exportToXls();" ></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Financial Year</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="fin_year">
										<option value="0" selected="selected" disabled>--Select--</option>
										<%for(int i=0;i<VMST_FINANCIAL_YEAR.size();i++){ %>
											<option value="<%=VMST_FINANCIAL_YEAR.elementAt(i)%>"><%=VMST_FINANCIAL_YEAR.elementAt(i)%></option>
										<%} %>	  		  
									</select>
									<script>document.forms[0].fin_year.value="<%=fin_year%>"</script>
								</div> 
							</div>
					  	</div>
					  	<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered" id="example">
							<thead id="tbsearch">
								<tr>
									<th>Sr#</th>
									<th>Month-Year</th>
									<th>RLNG Send Out in <br>GAIL-GSPL-PIL HAZ<br>(MMBTU)</th>
									<th>DLNG Volume (Own+TP) <br>(MMBTU)</th>
									<th>Total Send out from<br>Terminal in MMBTU</th>
									<th>No. of Days in month</th>
									<th>Terminal Send Out<br>(MMSCM)</th>
									<th>Terminal Send Out<br>(MMSCMD)</th>
									<th>Nameplate Capacity<br>(MMTPA)</th>
									<th>MMSCMD/Nameplate<br>Capacity MMTPA</th>
									<th title="=(Terminal Send Out (MMSCM) * Nameplate Capacity) / (MMSCMD/Nameplate Capacity)">MMTPA Capacity</th>
									<th title="=Nameplate Capacity/12">Nameplate Capacity<br>per month<br>(MMTPA)</th>
									<th title="=MMTPA Capacity/12">MMTPA Capacity<br>per month</th>
									<th title="=MMTPA Capacity(per Month)/Nameplate Capacity(per month)*100">Capacity Utilization in % for the month</th>
									<th>Cumulative Capacity utilization in %<br>Year to Month</th>
								</tr>
							</thead>
							<tbody>
								<%if(VMONTH_YEAR.size()>0){%>
									<%for(int i=0;i<VMONTH_YEAR.size();i++){%>
										<tr>
											<td align="center"><%=i+1 %></td>
											<td align="center"><%=VMONTH_YEAR.elementAt(i) %></td>
											<td align="right"><%=VRLNG_SEND_OUT_MMBTU.elementAt(i) %></td>
											<td align="right"><%=VDLNG_SEND_OUT_MMBTU.elementAt(i) %></td>
											<td align="right"><%=VTOTAL_SEND_OUT_MMBTU.elementAt(i) %></td>
											<td align="center"><%=VMONTH_DAYS.elementAt(i)%></td>
											<td align="right"><%=VTOTAL_SEND_OUT_MMSCM.elementAt(i) %></td>
											<td align="right"><%=VTOTAL_SEND_OUT_MMSCMD.elementAt(i) %></td>
											<td align="right"><%=VNAMEPLATE_CAP.elementAt(i) %></td>
											<td align="right"><%=VMMSCMD_BY_NP.elementAt(i) %></td>
											<td align="right"><%=VMMTPA_CAP.elementAt(i) %></td>
											<td align="right"><%=VNAMEPLATE_CAP_PER_MTH.elementAt(i) %></td>
											<td align="right"><%=VMMTPA_CAP_PER_MTH.elementAt(i) %></td>
											<td align="right"><%=VCAP_UTILIZE_PER.elementAt(i) %></td>
											<td align="right"><%=VCUM_CAP_UTILIZE_PER.elementAt(i) %></td>
										</tr>
									<%} %>
								<%} else{%>
									<tr>
										<td colspan="15" align="center"><%=utilmsg.infoMessage("<b>No Terminal Utilization Data Found!</b>") %></td>
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
<input type="hidden" name="comp_abbr" value="<%=owner_abbr %>">
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