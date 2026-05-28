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
	
	var url = "rpt_purchase_register.jsp?u="+u+"&segment="+segment+
			"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to;

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
	
	var url = "xls_purchase_register.jsp?fileName=Purchse Register From "+month+"/"+year+" To "+month_to+"/"+year_to+".xls&segment="+segment+
	"&month="+month+"&year="+year+"&month_to="+month_to+"&year_to="+year_to;

	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="purchase" scope="request"></jsp:useBean>
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

purchase.setCallFlag("PURCHASE_REGISTER");
purchase.setComp_cd(owner_cd);
purchase.setMonth(month);
purchase.setYear(year);
purchase.setMonth_to(month_to);
purchase.setYear_to(year_to);
purchase.setSegment(segment);
purchase.init();

Vector VINVOICE_DT = purchase.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = purchase.getVINVOICE_DUE_DT();
Vector VINVOICE_NO = purchase.getVINVOICE_NO();
Vector VINVOICE_SEQ = purchase.getVINVOICE_SEQ();
Vector VPERIOD_START_DT = purchase.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = purchase.getVPERIOD_END_DT();
Vector VFREQ = purchase.getVFREQ();
Vector VFREQ_NM = purchase.getVFREQ_NM();
Vector VALLOC_QTY = purchase.getVALLOC_QTY();
Vector VSALES_PRICE = purchase.getVSALES_PRICE();
Vector VSALES_PRICE_UNIT = purchase.getVSALES_PRICE_UNIT();
Vector VRATE_NM = purchase.getVRATE_NM();
Vector VGROSS_AMT = purchase.getVGROSS_AMT();
Vector VTAX_AMT = purchase.getVTAX_AMT();
Vector VINVOICE_AMT = purchase.getVINVOICE_AMT();
Vector VADJ_SIGN = purchase.getVADJ_SIGN();
Vector VADJ_AMT = purchase.getVADJ_AMT();
Vector VNET_PAYABLE = purchase.getVNET_PAYABLE();
Vector VEXCHNAGE_RATE = purchase.getVEXCHNAGE_RATE();
Vector VSALES_PRICE_USD = purchase.getVSALES_PRICE_USD();
Vector VSALES_PRICE_UNIT_USD = purchase.getVSALES_PRICE_UNIT_USD();
Vector VRATE_NM_USD = purchase.getVRATE_NM_USD();
Vector VGROSS_AMT_USD = purchase.getVGROSS_AMT_USD();
Vector VTAX_AMT_USD = purchase.getVTAX_AMT_USD();
Vector VINVOICE_AMT_USD = purchase.getVINVOICE_AMT_USD();
Vector VADJ_SIGN_USD = purchase.getVADJ_SIGN_USD();
Vector VADJ_AMT_USD = purchase.getVADJ_AMT_USD();
Vector VNET_PAYABLE_USD = purchase.getVNET_PAYABLE_USD();
Vector VMONTH = purchase.getVMONTH();
Vector VCOUNTERPTY_CD = purchase.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = purchase.getVCOUNTERPTY_ABBR();
Vector VDEAL_NO = purchase.getVDEAL_NO();
Vector VBU_NM = purchase.getVBU_NM();
Vector VSEGMENT = purchase.getVSEGMENT();

Vector VDISPLAY_SEGMENT = purchase.getVDISPLAY_SEGMENT();
Vector VDISPLAY_SEGMENT_TYP = purchase.getVDISPLAY_SEGMENT_TYP();
Vector VINDEX = purchase.getVINDEX();
Vector VCOLOR = purchase.getVCOLOR();
Vector VLINKED_INV_REF = purchase.getVLINKED_INV_REF();
Vector VPAY_RECV_AMT =  purchase.getVPAY_RECV_AMT();
Vector VPAY_STATUS = purchase.getVPAY_STATUS();
Vector VINV_STATUS = purchase.getVINV_STATUS();
Vector VTCS_AMT=purchase.getVTCS_AMT();
Vector VTCS_AMT_USD = purchase.getVTCS_AMT_USD();
Vector VTDS_AMT=purchase.getVTDS_AMT();
Vector VTDS_AMT_USD = purchase.getVTDS_AMT_USD();

Vector VDIS_REMITTANCE_NO = purchase.getVDIS_REMITTANCE_NO();
Vector VINVOICE_TYPE = purchase.getVINVOICE_TYPE();
Vector VQTY_UNIT = purchase.getVQTY_UNIT();
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
					    	Purchase Register
					    </div>
					    <div class="row justify-content-end">
							<div class="col-auto" onclick="exportToXls();" style="color:green;">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
							</div>					
							<div class="col-auto">
							 	<div class="btn-group">
									<select class="btn btn-outline-secondary btngrp btnactive" name="segment" onchange="refresh();">
										<option value="">All</option>
										<option value="D">Domestic NG</option>
										<option value="I">IGX</option>
										<option value="N">LNG Cargo</option>
										<option value="CD">Custom Duty Cargo</option>
										<option value="L">LTCORA</option>
										<option value="Y">Surveyor Agent</option>
										<option value="A">Vessel Agent</option>
										<option value="H">Custom House Agent</option>
									</select>
								</div>
								<script>document.forms[0].segment.value="<%=segment%>"</script>
							</div>
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
					<%int i=0;int k=0;
					for(int j=0; j<VDISPLAY_SEGMENT_TYP.size(); j++){ 
						int index = Integer.parseInt(""+VINDEX.elementAt(j));
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
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VDISPLAY_SEGMENT.elementAt(j) %></label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover" id="register<%=j%>">
									<thead>
										<tr>
											<th rowspan="2">Sr#</th>
											<th rowspan="2">Segment
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Segment<%=j%>" onkeyup="Search(this,'1','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
											</th>
											<th rowspan="2">Month
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_month<%=j%>" onkeyup="Search(this,'2','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
											</th>
											<th rowspan="2">Trader
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_trader<%=j%>" onkeyup="Search(this,'3','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
											</th>
											<th rowspan="2"><%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("N") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("CD") || VDISPLAY_SEGMENT_TYP.elementAt(j).equals("L")){ %>Cargo#<%}else{ %>Contract#<%} %>
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract<%=j%>" onkeyup="Search(this,'4','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
											</th>
											<th rowspan="2">BU
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_bu<%=j%>" onkeyup="Search(this,'5','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
											</th>
											<th rowspan="2">Invoice Type</th>
											<th colspan="10">Invoice Details</th>
											<th colspan="7">USD Details</th>
											<th colspan="7">INR Details</th>
											<th rowspan="2">Payment Status</th>
											<th rowspan="2">Payment Received (INR)</th>
										</tr>
										<tr>
											<th>Invoice#
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_invoice<%=j%>" onkeyup="Search(this,'7','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
											</th>
											<th>Remittance#
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_remittance<%=j%>" onkeyup="Search(this,'8','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
											</th>
											<th>Invoice Status
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_status<%=j%>" onkeyup="Search(this,'9','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
											</th>
											<th>Billing Cycle
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_billing_cycle<%=j%>" onkeyup="Search(this,'10','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
											</th>
											<th>Invoice Period</th>
											<th>Invoice Date
												<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_InvDt<%=j%>" onkeyup="Search(this,'12','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
											</th>
											<th>Invoice Due Date</th>
											<th>Invoiced Qty</th>
											<th>Exchange Rate</th>
											<th>Price/Qty</th>
											<th>Gross Amount</th>
											<th>Tax</th>
											<th>Invoice Amount</th>
											<th>TCS Value</th>
											<th>TDS Value</th>
											<th>Adjustment Amount</th>
											<th>Net Payable</th>
											<!-- <th>Price/Qty</th> -->
											<th>Gross Amount</th>
											<th>Tax</th>
											<th>Invoice Amount</th>
											<th>TCS Value</th>
											<th>TDS Value</th>
											<th>Adjustment Amount</th>
											<th>Net Payable</th>
										</tr>
									</thead>
									<tbody>
									<%k=0;
									if(index > 0){ %>
										<%for(i=i; i<VINVOICE_SEQ.size(); i++){ 
											k+=1;
										%>
										<tr>
											<td align="center">
												<%=k%>
											</td>
											<td align="center"><%=VSEGMENT.elementAt(i)%></td>
											<td align="center"><%=VMONTH.elementAt(i)%></td>
											<td align="center"><%=VCOUNTERPTY_ABBR.elementAt(i)%></td>
											<td align="center"><%=VDEAL_NO.elementAt(i)%></td>
											<td align="center"><%=VBU_NM.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_TYPE.elementAt(i)%></td>
											<td align="left"><%=VINVOICE_NO.elementAt(i)%> <%if(!VLINKED_INV_REF.elementAt(i).equals("")){%>(Ref:<%=VLINKED_INV_REF.elementAt(i)%>)<%} %></td>
											<td align="center"><%=VDIS_REMITTANCE_NO.elementAt(i) %>
											<td align="center" style="background: <%=VCOLOR.elementAt(i)%>"><%=VINV_STATUS.elementAt(i) %></td>
											<td align="center"><%=VFREQ_NM.elementAt(i)%></td>
											<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
											<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
											<td align="right"><%=VALLOC_QTY.elementAt(i)%> <%=VQTY_UNIT.elementAt(i) %></td>
											<td align="right"><%=VEXCHNAGE_RATE.elementAt(i)%></td>
											<td align="right"><%=VSALES_PRICE.elementAt(i)%>&nbsp;<%=VRATE_NM.elementAt(i)%></td>
											<td align="right"><%=VGROSS_AMT_USD.elementAt(i)%></td>
											<td align="right"><%=VTAX_AMT_USD.elementAt(i) %></td>
											<td align="right"><%=VINVOICE_AMT_USD.elementAt(i)%></td>
											<td align="right"><%=VTCS_AMT_USD.elementAt(i) %></td>
											<td align="right"><%=VTDS_AMT_USD.elementAt(i) %></td>
											<td align="right"><%=VADJ_SIGN_USD.elementAt(i)%><%=VADJ_AMT_USD.elementAt(i)%></td>
											<td align="right"><%=VNET_PAYABLE_USD.elementAt(i)%></td>
											<%-- <td align="right"><%=VSALES_PRICE.elementAt(i)%> --%><!-- &nbsp; --><%//=VRATE_NM.elementAt(i)%></td>
											<td align="right"><%=VGROSS_AMT.elementAt(i)%></td>
											<td align="right"><%=VTAX_AMT.elementAt(i) %></td>
											<td align="right"><%=VINVOICE_AMT.elementAt(i)%></td>
											<td align="right"><%=VTCS_AMT.elementAt(i) %></td>
											<td align="right"><%=VTDS_AMT.elementAt(i) %></td>
											<td align="right"><%=VADJ_SIGN.elementAt(i)%><%=VADJ_AMT.elementAt(i)%></td>
											<td align="right"><%=VNET_PAYABLE.elementAt(i)%></td>
											<td align="center"><%=VPAY_STATUS.elementAt(i) %></td>
											<td align="right"><%=VPAY_RECV_AMT.elementAt(i) %></td>
										</tr>
										<%
											if(k==index)
											{
												i=i+1;
												break;
											}
										} %>
									<%}else{ %>
										<tr>
											<td colspan="33" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Month!</b>") %></td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<%} %>
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
function Search(obj, indx, j) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("register"+j);
  	
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