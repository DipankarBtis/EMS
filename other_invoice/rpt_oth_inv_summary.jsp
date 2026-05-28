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
	var u = document.forms[0].u.value;
	
	var url="rpt_oth_inv_summary.jsp?month="+month+"&year="+year+"&u="+u;
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function exportToXls()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var fileName="Other_Invoice_Summary.xls"
	
	var url="xls_oth_inv_summary.jsp?fileName="+fileName+"&month="+month+"&year="+year;
	
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.other_invoice.DB_Other_Invoice_Report" id="oth_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();
int filter_start_year = CommonVariable.filter_start_year;

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
if(month.length() == 1)
{
	month="0"+month; 
}
oth_rpt.setCallFlag("OTH_INV_SUMMARY");
oth_rpt.setComp_cd(owner_cd);
//oth_rpt.setInvoice_type(invoice_type);
oth_rpt.setMonth(month);
oth_rpt.setYear(year);
oth_rpt.init();

Vector VSEGMENT_NM = oth_rpt.getVSEGMENT_NM();
Vector VSEGMENT_TYPE = oth_rpt.getVSEGMENT_TYPE();
Vector VDISP_SEGMENT_NM = oth_rpt.getVDISP_SEGMENT_NM();
Vector VDISP_SEGMENT_TYPE = oth_rpt.getVDISP_SEGMENT_TYPE();

Vector VINVOICE_TYPE = oth_rpt.getVINVOICE_TYPE();
Vector VVENDOR_CD = oth_rpt.getVVENDOR_CD();
Vector VVENDOR_NM = oth_rpt.getVVENDOR_NM();
Vector VINVOICE_NO = oth_rpt.getVINVOICE_NO();
Vector VINVOICE_DT = oth_rpt.getVINVOICE_DT();
Vector VGROSS_AMT_USD = oth_rpt.getVGROSS_AMT_USD();
Vector VGROSS_AMT_INR = oth_rpt.getVGROSS_AMT_INR();
Vector VTAX_AMT_USD = oth_rpt.getVTAX_AMT_USD();
Vector VTAX_AMT_INR = oth_rpt.getVTAX_AMT_INR();
Vector VNET_PAY_USD = oth_rpt.getVNET_PAY_USD();
Vector VNET_PAY_INR = oth_rpt.getVNET_PAY_INR();
Vector VCHECKED_FLAG = oth_rpt.getVCHECKED_FLAG();
Vector VCHECKED_BY = oth_rpt.getVCHECKED_BY();
Vector VCHECKED_DT = oth_rpt.getVCHECKED_DT();
Vector VAPPROVED_FLAG = oth_rpt.getVAPPROVED_FLAG();
Vector VAPPROVED_BY = oth_rpt.getVAPPROVED_BY();
Vector VAPPROVED_DT = oth_rpt.getVAPPROVED_DT();
Vector VINV_FLAG = oth_rpt.getVINV_FLAG();
Vector VCRITERIA = oth_rpt.getVCRITERIA();

Vector VINDEX = oth_rpt.getVINDEX();
%>
<body>
<%@ include file="../home/header.jsp"%>
<%if(!owner_cd.equals("2")) {%>
<div class="box-body">
	<div class="row">
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
		<div class="col-md-8 col-sm-8 col-xs-8">
			<div class="card cardmain">
				<div class="card-header cdheader ">
				</div>
				<div class="card-body cdbody">
					<div class="alert alert-info">
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="form-group row" align="center">
									<label class="form-label"  style="font-size:40px;font-weight: 700;"><i class='fa fa-exclamation-circle fa-lg'></i> Feature Not Supported</label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
				</div>
			</div>   
		</div>
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
	</div>
</div>
<%}else{ %>
<form method="post" action="../servlet/Frm_sap_interface" enctype="multipart/form-data">
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
					    	Other Invoice Summary 
					    </div>
					    <div class="d-flex justify-content-between">
					   		 <div onclick="exportToXls();" style="color:green;">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
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
					  					<%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">
							<div class="col">
								<input type="button" class="btn btn-warning com-btn" value="Apply Filters" onclick="refresh();">
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%
						int i=0,j=0,ctn=0,l=0,k=0;
						for(l=0;l<VDISP_SEGMENT_TYPE.size();l++){
							int index = Integer.parseInt(""+VINDEX.elementAt(l));
					%>
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading1">
											<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l %>" aria-expanded="false" aria-controls="collapse<%=l%>">
											<%=VDISP_SEGMENT_NM.elementAt(l) %>&nbsp;&nbsp;<font color="blue">(<%=index%> Items)</font>
											</button>	
								    	</h2>
								    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading1">
								    		<div class="accordion-body accor-body">
												<div class="row">
								    				<div class="table-responsive">
														<table class="table table-bordered table-hover" id="example<%=l%>">
															<thead id="tbsearch<%=l%>">
																<tr>
																	<th rowspan="2">Sr#</th>
																	<th rowspan="2">Invoice Type</th>
																	<th rowspan="2">Customer</th>
																	<th rowspan="2">Invoice No</th>
																	<th rowspan="2">Invoice Date</th>
																	<th colspan="3">INR Details</th>
																	<th colspan="3">USD Details</th>
																	<th rowspan="2">Checked</th>
																	<th rowspan="2">Approved</th>
																</tr>
																<tr>
																	<th>Gross Amount</th>
																	<th>Tax Amount</th>
																	<th>Net Amount</th>
																	<th>Gross Amount</th>
																	<th>Tax Amount</th>
																	<th>Net Amount</th>
																</tr>
															</thead>
															<tbody>
																<%k=0;
																	if(index > 0){ %>
																		<%for(i=i;i<VVENDOR_CD.size();i++){
																			k+=1;
																		%>
																			<tr>
																				<td align="center"><%=k%></td>
																				<td align="center"><%=VINVOICE_TYPE.elementAt(i)%> <%if(VINV_FLAG.elementAt(i).equals("CR")||VINV_FLAG.elementAt(i).equals("DR")){%>[<%=VINV_FLAG.elementAt(i)%>]<%} %></td>
																				<td align="center"><%=VVENDOR_NM.elementAt(i)%></td>
																				<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
																				<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
																				<td align="right"><%=VGROSS_AMT_INR.elementAt(i)%></td>
																				<td align="right"><%=VTAX_AMT_INR.elementAt(i)%></td>
																				<td align="right"><%=VNET_PAY_INR.elementAt(i)%></td>
																				<td align="right"><%=VGROSS_AMT_USD.elementAt(i)%></td>
																				<td align="right"><%=VTAX_AMT_USD.elementAt(i)%></td>
																				<td align="right"><%=VNET_PAY_USD.elementAt(i)%></td>
																				<td align="center"><%=VCHECKED_BY.elementAt(i)%><br><%=VCHECKED_DT.elementAt(i) %></td>
																				<td align="center"><%=VAPPROVED_BY.elementAt(i)%><br><%=VAPPROVED_DT.elementAt(i) %></td>
																			</tr>
																			<%if(k==index)
																			{
																				i+=1;
																				break;
																			}
																			%>
																		<%} %>
																<%}else{%>
																	<tr>
																		<td colspan="13" align="center"><%=utilmsg.infoMessage("<b>No "+ VDISP_SEGMENT_NM.elementAt(l)+" Invoice is Generated for Selected Period!</b>") %></td>
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
						</div>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="tmp_from_month" value="<%=month%>">
<input type="hidden" name="tmp_from_year" value="<%=year%>">

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
<input type="hidden" name="acc_size" value="<%=VINDEX.size()%>">
</form>
<%} %>
</body>
</html>