<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var invoice_type = document.forms[0].invoice_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "../extn_interface/frm_generate_oth_irn.jsp?&u="+u+"&month="+month+"&year="+year+"&invoice_type="+invoice_type;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function setEnableDisabled(obj,index)
{
	var invoice_no = document.getElementById("invoice_no_"+index);
	var counterparty_cd = document.getElementById("counterparty_cd_"+index);
	var contract_type = document.getElementById("contract_type_"+index);
	var inv_type = document.getElementById("inv_type_"+index);
	var sys_or_ff_inv = document.getElementById("sys_or_ff_inv_"+index);
	
	if(obj.checked)
	{
		invoice_no.disabled=false;
		contract_type.disabled=false;
		counterparty_cd.disabled=false;
		inv_type.disabled=false;
		sys_or_ff_inv.disabled=false;
	}
	else
	{
		invoice_no.disabled=true;
		contract_type.disabled=true;
		counterparty_cd.disabled=true;
		inv_type.disabled=true;
		sys_or_ff_inv.disabled=true;
	}
}

function checkAll(obj)
{
	var chk = document.forms[0].chk;
	var is_irn_generated = document.forms[0].is_irn_generated;
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0; i<chk.length; i++)
			{
				if(is_irn_generated[i].value=="Y")
				{
					chk[i].checked=false;
				}
				else
				{
					if(obj.checked)
					{
						chk[i].checked=true;
					}
					else
					{
						chk[i].checked=false;
					}
				}
				setEnableDisabled(chk[i],i);
			}
		}
		else
		{
			if(is_irn_generated.value=="Y")
			{
				chk.checked=false;
			}
			else
			{
				if(obj.checked)
				{
					chk.checked=true;
				}
				else
				{
					chk.checked=false;
				}
			}
			setEnableDisabled(chk,"0");	
		}
	}
}

function doGenerateExcel()
{
	var chk = document.forms[0].chk;
	var chk_count=parseInt("0");
	
	var msg="";
	var flag=true;
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0; i<chk.length; i++)
			{
				if(chk[i].checked)
				{
					chk_count++;
				}
			}
		}
		else
		{
			if(chk.checked)
			{
				chk_count++;
			}
		}
	}
	
	if(parseInt(chk_count) == 0)
	{
		msg="Please Select atleast ONE(1) Invoice!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a=confirm("Do you want to Generate Excel for IRN?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function doUploadExcel()
{
	var upload_file = document.forms[0].upload_file.value;
	
	if(upload_file.includes(".xlsx"))
	{
		var a=confirm("Do you want to Import IRN Response Excel?");
		if(a)
		{
			document.forms[0].option.value="OTH_UPLOAD_RESPONSE_IRN_EXCEL";
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}	
	}
	else
	{
		alert("Uploading file should be in .xlsx format only!")	
	}
}

function autoDounloadExcel()
{
	document.getElementById("multisheetIcon").click();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_IRN_Generation" id="irn_gen" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
int currentYear = 0;
int currentMonth = 0;
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	currentMonth=Integer.parseInt(temp[1]);
	currentYear=Integer.parseInt(temp[2]);
}
int filter_start_year = CommonVariable.filter_start_year;

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
String file_nm=request.getParameter("file_nm")==null?"":request.getParameter("file_nm");

if(month.length() == 1)
{
	month="0"+month; 
}

irn_gen.setCallFlag("OTH_INVOICE_LIST_FOR_IRN_GEN");
irn_gen.setComp_cd(owner_cd);
irn_gen.setMonth(month);
irn_gen.setYear(year);
irn_gen.setInvoice_type(invoice_type);
irn_gen.init();

Vector VCOUNTERPARTY_CD = irn_gen.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = irn_gen.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = irn_gen.getVCOUNTERPARTY_ABBR();
Vector VCONTRACT_TYPE = irn_gen.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = irn_gen.getVCONTRACT_TYPE_NM();
Vector VINVOICE_NO = irn_gen.getVINVOICE_NO();
Vector VINVOICE_DT = irn_gen.getVINVOICE_DT();
Vector VAPPROVE_DT = irn_gen.getVAPPROVE_DT();
Vector VINVOICE_TYPE = irn_gen.getVINVOICE_TYPE();
Vector VINVOICE_TYPE_NM = irn_gen.getVINVOICE_TYPE_NM();
Vector VIS_IRN_GENERATED = irn_gen.getVIS_IRN_GENERATED();
Vector VSYS_OR_FF_INV = irn_gen.getVSYS_OR_FF_INV();
%>
<body onload="<%if(!file_nm.equals("")) {%>autoDounloadExcel();<%} %>">
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
<form method="post" action="../servlet/Frm_IRN_Generation" enctype="multipart/form-data">
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
					    	e-Invoice (IG-Finance) IRN Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="form-group row">
								<div class="col-md-2 col-sm-0 col-xs-0"></div>
								<div class="col-md-4 col-sm-6 col-xs-6">
									<!-- <div class="d-flex justify-content-center"> -->
										<div class="form-group row">
											<div class="col-auto">
												<label class="form-label"><b>Month/Year</b></label>
								  			</div>
								  			<div class="col">
								  				<select class="form-select form-select-sm" name="month" onchange="">
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
								  				<select class="form-select form-select-sm" name="year" onchange="">
								  					<%for(int i=(currentYear+1); i > filter_start_year;i--){ %>
														<option value="<%=i%>"><%=i%></option>
													<%} %>
												</select>
												<script>document.forms[0].year.value="<%=year%>"</script>
											</div>
										</div>
									<!-- </div> -->
								</div>
								<div class="col-md-4 col-sm-4 col-xs-4">
									<!-- <div class="d-flex justify-content-center"> -->
										<div class="form-group row">
											<div class="col-auto">
												<label class="form-label"><b>Invoice Type</b></label>
								  			</div>
								  			<div class="col">
								  				<select class="form-select form-select-sm" name="invoice_type" onchange="">
													<option value="">--All--</option>
													<!-- <option value="COSTR">Cost Recharge (COSTR)</option> -->
													<option value="COSTRH">Cost Recharge HPPL (COSTRH)</option>
													<option value="HSA">Berthing Invoice (HPPL Shipping Agent) (HSA)</option>
													<option value="HS">PFA Fees Invoice (HPPL-SEIPL) (HS)</option>
													<option value="SFA">Scrap Fixed Asset (SFA)</option>
													<option value="NPR">NPR (NPR)</option>
													<option value="AHPL">AHPL Invoice (AHPL Revenue Share) (AHPL)</option>
													<option value="GA">GNA Invoice (GA)</option>
												</select>
												<script>document.forms[0].invoice_type.value="<%=invoice_type%>"</script>
											</div>
										</div>
									<!-- </div> -->
						  		</div>
						  		<div class="col-md-2 col-sm-2 col-xs-2">
						  			<div class="col">
										<input type="button" class="btn btn-warning com-btn" value="Apply Filters" onclick="refresh();">
				  					</div>
						  		</div>
						  	</div>
						</div>
					</div>
				</div>
				<%if(!file_nm.equals("")){ %>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="form-group row">
								<div class="col-md-6 col-sm-6 col-xs-12">
									<div class="form-group row">
										<div class="col-auto">
											<label class="form-label"><b>Generated IRN Excel</b></label>
							  			</div>
							  			<div class="col">
							  				<a id="multisheetIcon" href = "../work_data<%=owner_cd%>/IRN_EXCEL/Oth_Generated/<%=file_nm%>"><%=file_nm%>&nbsp;<i class="fa fa-arrow-circle-down fa-lg"></i></a>
							  			</div>
							  		</div>
							  	</div>
							</div>
						</div>
					</div>
				</div>
				<%} %>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th><input type="checkbox" class="form-check-input" onclick="checkAll(this);"></th>
											<th>Vendor/Supplier</th>
											<!-- <th>Contract Type</th> -->
											<th>Invoice Type</th>
											<th>Invoice No</th>
											<th>Invoice Date</th>
											<th>Approval Date</th>
										</tr>
									</thead>
									<tbody>
									<%if(VCOUNTERPARTY_CD.size()>0) {%>
										<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++) {%>
										<tr>
											<td align="center" <%if(VIS_IRN_GENERATED.elementAt(i).equals("Y")) {%>style="background: #99ffcc"<%} %>>
												<input type="checkbox" class="form-check-input" name="chk" id="chk_<%=i%>" 
												onclick="setEnableDisabled(this,'<%=i%>');" 
												<%if(VIS_IRN_GENERATED.elementAt(i).equals("Y")) {%>disabled<%} %>>
												<input type="hidden" name="sys_or_ff_inv" id="sys_or_ff_inv_<%=i%>" value="<%=VSYS_OR_FF_INV.elementAt(i)%>" disabled>
												<input type="hidden" name="invoice_no" id="invoice_no_<%=i%>" value="<%=VINVOICE_NO.elementAt(i)%>" disabled>
												<input type="hidden" name="counterparty_cd" id="counterparty_cd_<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>" disabled>
												<input type="hidden" name="contract_type" id="contract_type_<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled>
												<input type="hidden" name="inv_type" id="inv_type_<%=i%>" value="<%=VINVOICE_TYPE.elementAt(i)%>" disabled>
												<input type="hidden" name="is_irn_generated" id="is_irn_generated_<%=i%>" value="<%=VIS_IRN_GENERATED.elementAt(i)%>" disabled>
											</td>
											<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i) %></td>
											<%-- <td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i) %></td> --%>
											<td align="center"><%=VINVOICE_TYPE_NM.elementAt(i) %></td>
											<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
											<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
											<td align="center"><%=VAPPROVE_DT.elementAt(i) %></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td align="center" colspan="7"><%=utilmsg.infoMessage("<b>Invoice not Available for IRN Generation!</b>") %></td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Generate Excel" onclick="doGenerateExcel();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Generate Excel" disabled>
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>
	&nbsp;
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	e-Invoice (IG-Finance) IRN Response
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="form-group row">
								<div class="col-md-6 col-sm-12 col-xs-12">
									<div class="form-group row">
										<div class="col-auto">
											<label class="form-label"><b>Import IRN Response Excel</b></label>
							  			</div>
							  			<div class="col">
						      				<div class="input-group input-group-sm" >
						      					<input type="file" class="form-control form-control-sm" name="upload_file" id="upload_file">
							      				<span class="input-group-text"><i class="fa fa-upload fa-lg"></i></span>
							      			</div>
							  			</div>
							  		</div>
							  	</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Import Excel" onclick="doUploadExcel();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Import Excel" disabled>
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="OTH_GENERATE_EXCEL_FOR_IRN">

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
<%} %>
</body>
</html>