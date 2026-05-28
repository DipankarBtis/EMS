<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var month_to = document.forms[0].month_to.value;
	var year_to = document.forms[0].year_to.value;
	var segment = document.forms[0].segment.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_sales_register.jsp?u="+u+"&segment="+segment+"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function exportToXls()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var month_to = document.forms[0].month_to.value;
	var year_to = document.forms[0].year_to.value;
	var segment = document.forms[0].segment.value;
	
	var url = "xls_sales_register.jsp?fileName=Sales Register Report.xls&segment="+segment+"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.accounting.DataBean_Accounting" id="accounting" scope="request"></jsp:useBean>
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

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String month_to=request.getParameter("month_to")==null?""+currentMonth:request.getParameter("month_to");
String year_to=request.getParameter("year_to")==null?""+currentYear:request.getParameter("year_to");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");

if(month.length() == 1)
{
	month="0"+month; 
}
if(month_to.length() == 1)
{
	month_to="0"+month_to; 
}

accounting.setCallFlag("SALES_REGISTER");
accounting.setComp_cd(owner_cd);
accounting.setMonth(month);
accounting.setYear(year);
accounting.setMonth_to(month_to);
accounting.setYear_to(year_to);
accounting.setSegment(segment);
accounting.init();

Vector VSEGMENT = accounting.getVSEGMENT();
Vector VSEGMENT_TYPE = accounting.getVSEGMENT_TYPE();
Vector VCOUNTERPARTY_CD = accounting.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = accounting.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = accounting.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = accounting.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = accounting.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = accounting.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = accounting.getVCONT_NO();
Vector VCONT_REV_NO = accounting.getVCONT_REV_NO();
Vector VAGMT_NO = accounting.getVAGMT_NO();
Vector VAGMT_REV_NO = accounting.getVAGMT_REV_NO();
Vector VDIS_CONT_MAPPING = accounting.getVDIS_CONT_MAPPING();
Vector VCONTRACT_TYPE = accounting.getVCONTRACT_TYPE();
Vector VPERIOD_START_DT = accounting.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = accounting.getVPERIOD_END_DT();
Vector VSALES_PRICE = accounting.getVSALES_PRICE();
Vector VSALES_PRICE_CD = accounting.getVSALES_PRICE_CD();
Vector VSALES_PRICE_NM = accounting.getVSALES_PRICE_NM();
Vector VFINANCIAL_YEAR = accounting.getVFINANCIAL_YEAR();
Vector VINVOICE_RAISED_IN = accounting.getVINVOICE_RAISED_IN();
Vector VINVOICE_TYPE = accounting.getVINVOICE_TYPE();
Vector VINVOICE_NO = accounting.getVINVOICE_NO();
Vector VINVOICE_SEQ = accounting.getVINVOICE_SEQ();
Vector VINVOICE_DT = accounting.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = accounting.getVINVOICE_DUE_DT();



Vector VGROSS_AMT = accounting.getVGROSS_AMT();
Vector VTAX_AMT = accounting.getVTAX_AMT();
Vector VINVOICE_AMT = accounting.getVINVOICE_AMT();
Vector VTCS_AMT = accounting.getVTCS_AMT();
Vector VTCS_FACTOR = accounting.getVTCS_FACTOR();
Vector VTDS_GROSS_PERCENT = accounting.getVTDS_GROSS_PERCENT();
Vector VTDS_GROSS_AMT = accounting.getVTDS_GROSS_AMT();
Vector VTDS_TAX_PERCENT = accounting.getVTDS_TAX_PERCENT();
Vector VTDS_TAX_AMT = accounting.getVTDS_TAX_AMT();
Vector VTAX_STRUCT_DTL = accounting.getVTAX_STRUCT_DTL();
Vector VTDS_TCS_FLAG = accounting.getVTDS_TCS_FLAG();
Vector VNET_PAYABLE_AMT = accounting.getVNET_PAYABLE_AMT();


Vector VCONTRACT_TYPE_NM = accounting.getVCONTRACT_TYPE_NM();
Vector VMONTH_NM = accounting.getVMONTH_NM();
Vector VTCQ = accounting.getVTCQ();
Vector VSUPPLIED_QTY_MMBTU = accounting.getVSUPPLIED_QTY_MMBTU();
Vector VBALANCE_QTY_MMBTU = accounting.getVBALANCE_QTY_MMBTU();
Vector VSTART_DT = accounting.getVSTART_DT();
Vector VEND_DT = accounting.getVEND_DT();
Vector VALLOC_QTY = accounting.getVALLOC_QTY();
Vector VEXCHNG_RATE = accounting.getVEXCHNG_RATE();

Vector VSALES_AMT = accounting.getVSALES_AMT();
Vector VTRANSPORT_CHARGES_AMT = accounting.getVTRANSPORT_CHARGES_AMT();
Vector VMARKETING_MARGIN_AMT = accounting.getVMARKETING_MARGIN_AMT();
Vector VOTHER_CHARGES_AMT = accounting.getVOTHER_CHARGES_AMT();

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
					    	Sales Register
					    </div>
					   	<div class="d-flex justify-content-between">
					   		 <div onclick="exportToXls();" style="color:green;">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
							</div>&nbsp;
							<div class="btn-group">
								<select class="btn btn-outline-secondary btngrp btnactive" name="segment" onchange="refresh();">
									<option value="">All</option>
									<%for(int i=0;i<VSEGMENT.size();i++){ %>
									<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT.elementAt(i)%></option>
									<%} %>
								</select>
							</div>
							<script>document.forms[0].segment.value="<%=segment%>"</script>
					   	</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>Month/Year</b></label>
					  		</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="month" onchange="refresh();">
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
					  				<select class="form-select form-select-sm" name="year" onchange="refresh();">
					  					<%for(int i=(currentYear+1); i > (currentYear-10);i--){ %>
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
					  				<select class="form-select form-select-sm" name="month_to" onchange="refresh();">
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
					  				<select class="form-select form-select-sm" name="year_to" onchange="refresh();">
					  					<%for(int i=(currentYear+1); i > (currentYear-10);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year_to.value="<%=year_to%>"</script>
								</div>
							</div>
				  		</div>
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th rowspan="2">SR#</th>
										<th rowspan="2">Month
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_month" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Segment
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_segment" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th rowspan="2">Counterparty
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_counterparty" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th colspan="5" style="background: #000066; color: white;">Contract Details</th>
										<th colspan="11" style="background: #000066; color: white;">Invoice Details</th>
										<th colspan="11" style="background: #000066; color: white;">INR</th>
										<th colspan="11" style="background: #000066; color: white;">USD</th>
									</tr>
									<tr>	
										<th>Contract#<br> [Contract/Trade Ref]
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_contract" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div>
										</th>										
										<th>Contract Period</th>
										<th>Booked MMBTU</th>
										<th>Supplied MMBTU</th>
										<th>Balance MMBTU</th>
										<th>Invoice Type
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_invoice_type" onkeyup="Search(this,'9');" placeholder="Search.." style="width:100px"/></div>										
										</th>										
										<th>Invoice#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_invoice_no" onkeyup="Search(this,'10');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Invoice Date</th>
										<th>Invoice Due Date</th>
										<th>Billing Period</th>
										<th>Invoiced MMBTU</th>
										<th>Sales Rate</th>
										<th>Rate Unit/MMBTU
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_rate" onkeyup="Search(this,'16');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Sales Amount</th>
										<th>Exchange Rate (INR/USD)</th>
										<th>Invoice Raised In
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_invoiced_in" onkeyup="Search(this,'19');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Transportation Tariff</th>
										<th>Marketing Margin</th>
										<th>Other Charges </th>
										<th>Total Gross Amount</th>
										<th>Tax Structure
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_tax_str" onkeyup="Search(this,'24');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Tax </th>
										<th>Invoice Amount</th>
										<th>TCS/TDS
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_tcstds" onkeyup="Search(this,'27');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>TCS/TDS%</th>
										<th>+TCS/-TDS Amount</th>
										<th>Net Receivable</th>
										<th>Transportation Tariff</th>
										<th>Marketing Margin</th>
										<th>Other Charges </th>
										<th>Gross Amt</th>
										<th>Tax Structure</th>
										<th>Tax </th>
										<th>Invoice Amt</th>
										<th>TCS/TDS</th>
										<th>TCS/TDS%</th>
										<th>+TCS/-TDS Amt</th>
										<th>Net Receivable</th>												
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPARTY_CD.size() > 0){ %>
									<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
									<tr>
										<td><%=i+1%></td>
										<td align="center"><%=VMONTH_NM.elementAt(i)%></td>
										<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i)%></td>
										<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%>:<%=VCOUNTERPARTY_NM.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
										<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i)%></td>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										<td align="right"><%=VTCQ.elementAt(i)%></td>
										<td align="right"><%=VSUPPLIED_QTY_MMBTU.elementAt(i) %></td>
										<td align="right"><%=VBALANCE_QTY_MMBTU.elementAt(i) %></td>
										<td align="center"><%=VINVOICE_TYPE.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
										<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i) %></td>
										<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
										<td align="right"><%=VSALES_PRICE.elementAt(i)%></td>
										<td align="center" style="background: #b3f0ff;"><%=VSALES_PRICE_NM.elementAt(i) %></td>
										<td align="right"><%=VSALES_AMT.elementAt(i)%></td>
										<td align="right"><%=VEXCHNG_RATE.elementAt(i) %></td>
										<td align="center" style="background: #b3f0ff;"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>
										<td align="right"><%=VTRANSPORT_CHARGES_AMT.elementAt(i)%></td>
										<td align="right"><%=VMARKETING_MARGIN_AMT.elementAt(i)%></td>
										<td align="right"><%=VOTHER_CHARGES_AMT.elementAt(i)%></td>
										<td align="right"><%=VGROSS_AMT.elementAt(i)%></td>
										<td align="center"><%=VTAX_STRUCT_DTL.elementAt(i)%></td>
										<td align="right"><%=VTAX_AMT.elementAt(i)%></td>
										<td align="right"><%=VINVOICE_AMT.elementAt(i) %></td>
										<td align="center"><%=VTDS_TCS_FLAG.elementAt(i)%></td>
										<td align="right"><% if (VTDS_TCS_FLAG.elementAt(i).equals("TCS")) {%>
											<%=VTCS_FACTOR.elementAt(i) %> 
											<%}else{%> <%=VTDS_GROSS_PERCENT.elementAt(i)%> 
											<%}%> </td>
										<td align="right"><% if (VTDS_TCS_FLAG.elementAt(i).equals("TCS")) {%>
											<%=VTCS_AMT.elementAt(i) %>
											<%}else{%> <%=VTDS_GROSS_AMT.elementAt(i)%>
											<%}%> </td>	
										<td align="right"><%=VNET_PAYABLE_AMT.elementAt(i) %> </td>	
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
									<%}%>
								<%}else{%>
									<tr>
										<td colspan="47" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
									</tr>
								<%}%>
								</tbody>
							</table>
						</div>
					</div>
				</div>			
			</div>
		</div>
	</div>
</div>

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
<script>
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example");
  	
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
</html>