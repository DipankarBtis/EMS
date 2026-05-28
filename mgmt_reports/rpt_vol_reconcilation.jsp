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
	var bu_select = document.forms[0].bu_select.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var month_to = document.forms[0].month_to.value;
	var year_to = document.forms[0].year_to.value;
	
	var u = document.forms[0].u.value;
	
	var flag=checkMonthYearRange(document.forms[0].month,document.forms[0].year,document.forms[0].month_to,document.forms[0].year_to);
	if(flag==true)
	{
		var url="rpt_vol_reconcilation.jsp?month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to+"&bu_select="+bu_select+"&u="+u;
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}

function exportToXls()
{
	var bu_select = document.forms[0].bu_select.value;
	var sysdate = document.forms[0].sysdate.value;
	var month=document.forms[0].month.value;
	var year=document.forms[0].year.value;
	var month_to = document.forms[0].month_to.value;
	var year_to = document.forms[0].year_to.value;
	var sysdate1 = sysdate.replace("/","_");
	
	var fileName = "<%=owner_abbr%>_VolumeReconciliationReport_"+sysdate1+".xls";
	
	var url="xls_vol_reconcilation.jsp?month="+month+"&year="+year+"&fileName="+fileName+"&month_to="+month_to+"&year_to="+year_to+"&bu_select="+bu_select;
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_MGMT_Reports" id="mgmt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String accroid=request.getParameter("accroid")==null?"":request.getParameter("accroid");
String bu_select=request.getParameter("bu_select")==null?"0":request.getParameter("bu_select");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String month_to=request.getParameter("month_to")==null?""+currentMonth:request.getParameter("month_to");
String year_to=request.getParameter("year_to")==null?""+currentYear:request.getParameter("year_to");

if(month.length() == 1)
{
	month="0"+month; 
}
if(month_to.length() == 1)
{
	month_to="0"+month_to; 
}

String from_dt = "01/"+month+"/"+year;
String to_dt=utildate.getLastDateOfMonth(month_to,year_to);

int filter_start_year = CommonVariable.filter_start_year;
mgmt_rpt.setCallFlag("VOLUME_REPORT");
mgmt_rpt.setComp_cd(owner_cd);
mgmt_rpt.setFrom_dt(from_dt);
mgmt_rpt.setTo_dt(to_dt);
mgmt_rpt.setBu_select(bu_select);
mgmt_rpt.init();


Vector VBU_SEQ = mgmt_rpt.getVBU_SEQ();
Vector VBU_ABBR = mgmt_rpt.getVBU_ABBR();

Vector VINVOICE_LIST_ABBR = mgmt_rpt.getVINVOICE_LIST_ABBR();
Vector VINVOICE_LIST_NAME = mgmt_rpt.getVINVOICE_LIST_NAME();
Vector VINDEX = mgmt_rpt.getVINDEX();

Vector VCOUNTERPARTY_CD = mgmt_rpt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = mgmt_rpt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = mgmt_rpt.getVCOUNTERPARTY_NM();

Vector VPUR_COUNTERPARTY_CD = mgmt_rpt.getVPUR_COUNTERPARTY_CD();
Vector VPUR_COUNTERPARTY_ABBR = mgmt_rpt.getVPUR_COUNTERPARTY_ABBR();
Vector VPUR_COUNTERPARTY_NM = mgmt_rpt.getVPUR_COUNTERPARTY_NM();

Vector VDEAL_NO = mgmt_rpt.getVDEAL_NO();
Vector VCONT_REF_NO = mgmt_rpt.getVCONT_REF_NO();
Vector VAGMT_BASE = mgmt_rpt.getVAGMT_BASE();
Vector VINVOICE_NO = mgmt_rpt.getVINVOICE_NO();
Vector VALLOC_QTY = mgmt_rpt.getVALLOC_QTY();

Vector VPUR_INDEX = mgmt_rpt.getVPUR_INDEX();
Vector VPUR_DEAL_NO = mgmt_rpt.getVPUR_DEAL_NO();
Vector VPUR_CONT_REF_NO = mgmt_rpt.getVPUR_CONT_REF_NO();
Vector VPUR_INVOICE_NO = mgmt_rpt.getVPUR_INVOICE_NO();
Vector VPUR_ALLOC_QTY = mgmt_rpt.getVPUR_ALLOC_QTY();

Vector VTOTAL_QTY = mgmt_rpt.getVTOTAL_QTY();
Vector VPUR_TOTAL_QTY = mgmt_rpt.getVPUR_TOTAL_QTY();
Vector VOPEN_QTY = mgmt_rpt.getVOPEN_QTY();
Vector VCLOSE_QTY = mgmt_rpt.getVCLOSE_QTY();

Vector VMONTH = mgmt_rpt.getVMONTH();
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
					    	Volume Reconciliation Report
					    </div>
				    	<div class="d-flex justify-content-between">
						    <div class="col-auto">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x" onclick="exportToXls();" ></i></span>
							</div>&nbsp;&nbsp;
							<div class="btn-group">
								<select class="btn btn-outline-secondary btngrp btnactive" name="bu_select" onchange="refresh();">
									<option value="0">All</option>
									<%for(int i=0;i<VBU_SEQ.size();i++){ %>
											<option value="<%=VBU_SEQ.elementAt(i)%>"><%=VBU_ABBR.elementAt(i)%></option>
									<%} %>
								</select>
							</div>
							<script>document.forms[0].bu_select.value="<%=bu_select%>"</script>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
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
							</div>
						</div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>to</b></label>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col">
					  				<select class="form-select form-select-sm" name="month_to">
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
									<script>document.forms[0].month_to.value="<%=month_to%>"</script>
								</div>
								<div class="col">
					  				<select class="form-select form-select-sm" name="year_to">
					  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year_to.value="<%=year_to%>"</script>
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
				<%int i=0,k=0,l=0,m=0;
				for(int j=0; j<VINVOICE_LIST_NAME.size(); j++)
				{ 
					int index=Integer.parseInt(""+VINDEX.elementAt(j));
					String heading = ""+VINVOICE_LIST_ABBR.elementAt(j);
					String sal_qty = ""+VTOTAL_QTY.elementAt(j);
					String pur_qty = ""+VPUR_TOTAL_QTY.elementAt(j);
					String open_qty = ""+VOPEN_QTY.elementAt(j);
					String close_qty = ""+VCLOSE_QTY.elementAt(j);
					%>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="<%=heading%>">
   										<button name="sub_module_cd" class="accordion-button <%if(!accroid.equals(heading)){%>collapsed<%}%> accor-btn" type="button" 
   											data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="<%if(!accroid.equals(heading)){%>true<%}else{%>false<%} %>" aria-controls="collapse<%=j%>">
							    			<%=VINVOICE_LIST_NAME.elementAt(j)%>&nbsp;&nbsp;
							    			<font color="black" style="background: white;padding: 2px 5px 4px 5px;border-radius: 30px;">
								    			<span style="color:black;">[Opening Inventory : <%=open_qty%> MMBTU]</span>
								    			<span style="color:blue;">[Sales : <%=sal_qty%> MMBTU]</span>  
								    			<span style="color:green;">[Purchase : <%=pur_qty%> MMBTU]</span>
								    			<span style="color:red;">[Closing Inventory : <%=close_qty%> MMBTU]</span>
							    			</font>
							    			&nbsp;
							      		</button>	
							    	</h2>
									<div id="collapse<%=j%>" class="accordion-collapse collapse <%if(accroid.equals(heading)){%>show<%}%>" aria-labelledby="<%=heading%>">
							      		<div class="accordion-body accor-body">
											<div class="row">
												<div class="col-md-12 col-sm-12 col-xs-12">
													<div class="table-responsive">
														<table class="table table-bordered table-hover serchtbl" id="example<%=j%>">
															<thead>
																<tr>
																<th rowspan="2" align="center" valign="middle">Month/Year</th>
																<th colspan="5">SALES</th>
																<th colspan="5">PURCHASE</th>
																</tr>
																<tr>
																	<th>Counterparty
																		<br><div align="center"><input class="form-control form-control-sm" type="text" id="sal_counterparty<%=j%>" onkeyup="Search(this,'1','<%=j%>');" placeholder="Search Counterparty" style="width:100px"/></div>
																	</th>
																	<th>Contract No
																		<br><div align="center"><input class="form-control form-control-sm" type="text" id="sal_cont<%=j%>" onkeyup="Search(this,'2','<%=j%>');" placeholder="Search Contract No" style="width:100px"/></div>
																	</th>
																	<th>Contract/Trade Ref#
																		<br><div align="center"><input class="form-control form-control-sm" type="text" id="sal_contref<%=j%>" onkeyup="Search(this,'3','<%=j%>');" placeholder="Search Contract/Trade Ref#" style="width:100px"/></div>
																	</th>
																	<th>Invoice No
																		<br><div align="center"><input class="form-control form-control-sm" type="text" id="sal_inv<%=j%>" onkeyup="Search(this,'4','<%=j%>');" placeholder="Search Invoice No" style="width:100px"/></div>
																	</th>
																	<th>Quantity(MMBTU)
																		<br><div align="center"><input class="form-control form-control-sm" type="text" id="sal_qty<%=j%>" onkeyup="Search(this,'5','<%=j%>');" placeholder="Search Quantity(MMBTU)" style="width:100px"/></div>
																	</th>
																	<th>Counterparty
																		<br><div align="center"><input class="form-control form-control-sm" type="text" id="pur_counterparty<%=j%>" onkeyup="Search(this,'6','<%=j%>');" placeholder="Search Counterparty" style="width:100px"/></div>
																	</th>
																	<th>Contract No
																		<br><div align="center"><input class="form-control form-control-sm" type="text" id="pur_cont<%=j%>" onkeyup="Search(this,'7','<%=j%>');" placeholder="Search Contract No" style="width:100px"/></div>
																	</th>
																	<th>Contract/Trade Ref#
																		<br><div align="center"><input class="form-control form-control-sm" type="text" id="pur_contref<%=j%>" onkeyup="Search(this,'8','<%=j%>');" placeholder="Search Contract/Trade Ref#" style="width:100px"/></div>
																	</th>
																	<th>Invoice No
																		<br><div align="center"><input class="form-control form-control-sm" type="text" id="pur_inv<%=j%>" onkeyup="Search(this,'9','<%=j%>');" placeholder="Search Invoice No" style="width:100px"/></div>
																	</th>
																	<th>Quantity(MMBTU)
																		<br><div align="center"><input class="form-control form-control-sm" type="text" id="pur_qty<%=j%>" onkeyup="Search(this,'10','<%=j%>');" placeholder="Search Quantity(MMBTU)" style="width:100px"/></div>
																	</th>
																</tr>
															</thead>
															<tbody>
																<tr>
																	<td align="center"><b>Opening Inventory</b></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td align="right"><b><%=open_qty %></b></td>
																</tr>
																<%k=0;
																
																if(index>0)
																{
																	for(i=i; i<VCOUNTERPARTY_CD.size(); i++)
																	{
																	k+=1;
																	%>
																	<tr>
																		<td align="center"><%=VMONTH.elementAt(i) %></td>
																		<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(i)%>">
																			<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
																		</td>
																		<td align="center">
																			<font color="blue"><%=VDEAL_NO.elementAt(i)%></font>
																			<%if(VAGMT_BASE.elementAt(i).equals("D")){ %>
																			<font style="background:#a6ff4d;">[DLV]</font>
																			<%} %>
																			</td>
																		<td align="center"><%=VCONT_REF_NO.elementAt(i)%>
																		</td>
																		<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
																		<td align="right"><%=VALLOC_QTY.elementAt(i)%></td>
																		
																		<td align="center" title="<%=VPUR_COUNTERPARTY_NM.elementAt(i)%>">
																			<%=VPUR_COUNTERPARTY_ABBR.elementAt(i)%>
																		</td>
																		<td align="center">
																			<font color="blue"><%=VPUR_DEAL_NO.elementAt(i)%></font>
																			</td>
																		<td align="center"><%=VPUR_CONT_REF_NO.elementAt(i)%>
																		</td>
																		<td align="center"><%=VPUR_INVOICE_NO.elementAt(i)%></td>
																		<td align="right"><%=VPUR_ALLOC_QTY.elementAt(i)%></td>
																	</tr>
																	<%if(k==index){
																			i=i+1;
																			break;
																		} %>
																	<%} %>	
																	<tr>
																		<td colspan="5"  align="right"><b>Total(MMBTU) :</b></td>
																		<td align="right"><b><%=sal_qty %></b></td>
																		<td colspan="4"></td>
																		<td align="right"><b><%=pur_qty %></b></td>
																	</tr>
																	
																	
																<%}else{ %>
																	<tr>
																		<td align="center" colspan="11"><%=utilmsg.infoMessage("<b>No Invoice is Generated!</b>") %></td>
																	</tr>
																<%} %>
																<tr>
																	<td align="center"><b>Closing Inventory</b></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td align="right"><b><%=close_qty %></b></td>
																</tr>
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
						</div>
					<%} %>	
				</div>
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="comp_abbr" value="<%=owner_abbr%>">

<input type="hidden" name="tmp_from_month" value="<%=month%>">
<input type="hidden" name="tmp_from_year" value="<%=year%>">
<input type="hidden" name="tmp_to_month" value="<%=month_to%>">
<input type="hidden" name="tmp_to_year" value="<%=year_to%>">

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

<input type="hidden" name="invoice_list_name" value="<%=VINVOICE_LIST_NAME.size()%>">

<script>
function Search(obj, indx, j) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+j);
  	
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
</form>
</html>
