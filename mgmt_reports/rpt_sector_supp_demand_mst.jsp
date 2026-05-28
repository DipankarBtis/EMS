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
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var url="rpt_sector_supp_demand_mst.jsp?month="+month+"&year="+year+"&u="+u;
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function exportToXls()
{
	var sysdate = document.forms[0].sysdate.value;
	var comp_abbr = document.forms[0].comp_abbr.value;
	var month=document.forms[0].month.value;
	var year=document.forms[0].year.value;
	
	var sysdate1 = sysdate.replace("/","_");
	
	var fileName = comp_abbr+"_"+"Master_Report_Demand_and_Supply"+".xls";
	
	var url="xls_sector_supp_demand_mst.jsp?month="+month+"&year="+year+"&fileName="+fileName;
	location.replace(url);
}
</script>
</head>

<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_Govt_Reports" id="govt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");

if(month.length() == 1)
{
	month="0"+month; 
}

int filter_start_year = CommonVariable.filter_start_year;

govt_rpt.setCallFlag("SUPPLY_DEMAND_SECTOR_MASTER_RPT");
govt_rpt.setComp_cd(owner_cd);
govt_rpt.setMonth(month);
govt_rpt.setYear(year);
govt_rpt.init();

Vector VCOUNTERPARTY_CD = govt_rpt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = govt_rpt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = govt_rpt.getVCOUNTERPARTY_NM();
Vector VPLANT_NM = govt_rpt.getVPLANT_NM();
Vector VPLANT_SECTOR_NM = govt_rpt.getVPLANT_SECTOR_NM();
Vector VDISPLAY_DEAL_MAP = govt_rpt.getVDISPLAY_DEAL_MAP();
Vector VDEMAND_SECTOR_CD = govt_rpt.getVDEMAND_SECTOR_CD();
Vector VSUPPLY_SECTOR_CD = govt_rpt.getVSUPPLY_SECTOR_CD();
Vector VSTATE_CD = govt_rpt.getVSTATE_CD();
Vector VQTY_MMBTU = govt_rpt.getVQTY_MMBTU();
Vector VQTY_MMSCM = govt_rpt.getVQTY_MMSCM();
Vector VQTY_MT = govt_rpt.getVQTY_MT();
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
					    	Master Report (Demand and Supply) 
					    </div>
					    <div class="col-auto">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x" onclick="exportToXls();" ></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
							</div>
						</div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>Month/Year</b></label>
					  		</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="month">
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
								<div class="col">
					  				<select class="form-select form-select-sm" name="year">
					  					<%for(int i=(currentYear); i > filter_start_year;i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
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
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered" id="example">
							<thead id="tbsearch">
								<tr>
									<th>Sr#</th>
									<th>Customer Name</th>
									<th>Plant Name</th>
									<th>Sector from <br> Plant Mapping</th>
									<th>Contract</th>
									<th>SectorID-DEMAND</th>
									<th>BUSectorId-SUPPLY</th>
									<th>State Code</th>
									<th>Qty MMBTU</th>
									<th>Qty MSCM</th>
									<th>Qty MT</th>
								</tr>
							</thead>
							<tbody>
								<%if(VCOUNTERPARTY_CD.size()>0){%>
									<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){%>
										<tr>
											<td align="center"><%=i+1%></td>
											<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i) %></td>
											<td align="center"><%=VPLANT_NM.elementAt(i) %></td>
											<td align="center"><%=VPLANT_SECTOR_NM.elementAt(i) %></td>
											<td>
												<div style="width:400px; word-wrap: break-word; white-space: normal;">
													<%=VDISPLAY_DEAL_MAP.elementAt(i) %>
												</div>
											</td>
											<td align="center"><%=VDEMAND_SECTOR_CD.elementAt(i) %></td>
											<td align="center"><%=VSUPPLY_SECTOR_CD.elementAt(i) %></td>
											<td align="center"><%=VSTATE_CD.elementAt(i) %></td>
											<td align="right"><%=VQTY_MMBTU.elementAt(i) %></td>
											<td align="right"><%=VQTY_MMSCM.elementAt(i) %></td>
											<td align="right"><%=VQTY_MT.elementAt(i) %></td>
										</tr>
									<%}%>
								<%}else{%>
									<tr>
										<td colspan="11" align="center"><%=utilmsg.infoMessage("<b>No Data Found!</b>") %></td>
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
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="comp_abbr" value="<%=owner_abbr%>">

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