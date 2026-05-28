<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.Vector"%>
<%@page import="java.time.*"%>
<%@page import="java.time.format.*"%>
<%@page import="java.time.temporal.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function getDurationEndDt() {

	var storage_days = parseInt(document.forms[0].storage_days.value, 10);
    var sd_start_dt = document.forms[0].sd_start_dt.value;

    if (isNaN(storage_days) || storage_days < 0) {
        alert('Invalid Storage days!!');
        document.forms[0].storage_days.value="";
        return;
    }

    var startDate = parseDate(sd_start_dt);
    if (!startDate) {
        alert('Please configure Actual Receipt Date for the Cargo!!');
        return;
    }

    var endDate = new Date(startDate);
    endDate.setDate(startDate.getDate() + storage_days);

    var year = endDate.getFullYear();
    var month = String(endDate.getMonth() + 1).padStart(2, '0');
    var day = String(endDate.getDate()).padStart(2, '0');
    
    var formattedDate = day+"/"+month+"/"+year;

    document.forms[0].sd_end_dt.value=formattedDate;
}

function getExpendedDt() {

	var expn_days = parseInt(document.forms[0].expn_days.value, 10);
    var sd_end_dt=document.forms[0].sd_end_dt.value

    if (isNaN(expn_days) || expn_days < 0) {
        alert('Invalid Storage days!!');
        document.forms[0].expn_days.value="";
        return;
    }

    var startDate = parseDate(sd_end_dt);
    if (!startDate) {
        alert('Please configure Actual Receipt Date for the Cargo!!');
        return;
    }

    var endDate = new Date(startDate);
    endDate.setDate(startDate.getDate() + expn_days);

    var year = endDate.getFullYear();
    var month = String(endDate.getMonth() + 1).padStart(2, '0');
    var day = String(endDate.getDate()).padStart(2, '0');
    
    var formattedDate = day+"/"+month+"/"+year;

    document.forms[0].expn_end_dt.value=formattedDate;
}

function parseDate(dateStr) 
{
    var parts = dateStr.split('/');
    if (parts.length === 3) 
    {
        var day = parseInt(parts[0], 10);
        var month = parseInt(parts[1], 10) - 1;
        var year = parseInt(parts[2], 10);
        return new Date(year, month, day);
    }
    return null;
}

function enable_disable()
{
	var chk = document.getElementById("chk");
	
	var expn_days = document.getElementById("expn_days");

	if(chk.checked)
	{
		expn_days.disabled=false;
	}
	else
	{
		expn_days.disabled=true;
	}
}
function doProceed()
{
	var id_no = document.forms[0].id_no.value;
	
	var storage_days = document.forms[0].storage_days.value;
	var expn_days = document.forms[0].expn_days.value;
	var actual_recpt_dt = document.forms[0].actual_recpt_dt.value;
	
	if(storage_days != "")
	{
		var a = confirm("Do you want to proceed?");
		
		if(a)
		{
			alert("Submit Cargo Details after proceeding.");
			window.opener.setStorageDetail(id_no,storage_days,expn_days,actual_recpt_dt);
			window.close();
		}
	}
	else
	{
		alert("Please Provide Storage Days!");
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateUtil" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.ltcora.DataBean_LtcoraMaster" id="dbltcora" scope="request"></jsp:useBean>

<%
String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String actual_recpt_dt=request.getParameter("actual_recpt_dt")==null?"":request.getParameter("actual_recpt_dt");
String window_start_dt=request.getParameter("window_start_dt")==null?"":request.getParameter("window_start_dt");
String window_end_dt = request.getParameter("window_end_dt")==null?"":request.getParameter("window_end_dt");
String cargo_ref_no = request.getParameter("cargoRef")==null?"":request.getParameter("cargoRef");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_name = request.getParameter("cont_name")==null?"":request.getParameter("cont_name");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String buy_sell = request.getParameter("buy_sell")==null?"":request.getParameter("buy_sell");
String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
String id_no = request.getParameter("id_no")==null?"":request.getParameter("id_no");
String supp_qty = request.getParameter("supp_qty")==null?"":request.getParameter("supp_qty");

String disp_cont_no = request.getParameter("cargo_number_disp")==null?"":request.getParameter("cargo_number_disp");
String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");

dbltcora.setCallFlag("LTCORA_CN_CARGO_STORAGE_DTL");
dbltcora.setComp_cd(owner_cd);
dbltcora.setCounterparty_cd(counterparty_cd);
dbltcora.setContract_type(contract_type);
dbltcora.setAgmt_no(agmt_no);
dbltcora.setAgmt_type(agmt_type);
dbltcora.setCont_no(cont_no);
dbltcora.setCont_rev_no(cont_rev);
dbltcora.setAgmt_rev_no(agmt_rev);
dbltcora.setBuy_sale(buy_sell);
dbltcora.setCargo_no(cargo_no);
dbltcora.init();

String storage_days=dbltcora.getStorage_days();
String storage_ext_days=dbltcora.getStorage_ext_days();
String allocated_qty_mmbtu=dbltcora.getAllocated_qty_mmbtu();

if(storage_days.equals("0"))
{
	storage_days = "";
}
if(storage_ext_days.equals("0"))
{
	storage_ext_days = "";
}

String storage_end_dt = dateUtil.getDate(actual_recpt_dt, storage_days);
String storage_ext_dt = dateUtil.getDate(storage_end_dt, storage_ext_days);
String extend_storage = dbltcora.getExtend_storage();

double balance_qty_mmbtu = Double.parseDouble(supp_qty) - Double.parseDouble(allocated_qty_mmbtu);
%>
<body onload="enable_disable()">
<form method="post" action="../servlet/Frm_LtcoraMaster">
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
					    	Storage Duration Details (<%=cargo_ref_no %>)
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th rowspan="2">Storage Days<span class="s-red">*</span></th>
										<th colspan="2">Storage Duration</th>
										<th colspan="2">Storage Duration Extension</th>
									</tr>
									<tr>
										<th>Start Date</th>
										<th>End Date</th>
										<th>Extension of Days</th>
										<th>Extended End Date</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td align="center">
											<div style="width:100px;">
												<input class="form-control form-control-sm" type="text" name="storage_days" id="storage_days" value="<%=storage_days %>" onblur="getDurationEndDt()" maxlength="2">
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input  class="form-control form-control-sm" type="text" name="sd_start_dt" id="sd_start_dt" value="<%=actual_recpt_dt %>" readonly>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div> 
					      					</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input  class="form-control form-control-sm" type="text" name="sd_end_dt" id="sd_end_dt" value="<%=storage_end_dt %>" readonly>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div> 
					      					</div>
										</td>
										<td align="center">
											<div style="width:100px;" class="form-group d-flex justify-content-between">
												<input type="checkbox" class="form-check-input" name="chk" id="chk" onclick="enable_disable();" <%if(!extend_storage.equals("Y")){ %>style="pointer-events:none;"<%} %>>&nbsp;
												<input class="form-control form-control-sm" type="text" name="expn_days" id="expn_days" value="<%=storage_ext_days %>" onblur="getExpendedDt()" maxlength="2">
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input  class="form-control form-control-sm" type="text" name="expn_end_dt" id="expn_end_dt" value="<%=storage_ext_dt %>" readonly>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div> 
					      					</div>
										</td>
									</tr>
									<tr>
										<td colspan="4" align="right"><b>Balance Quantity as on Date :</b></td>
										<td align="center">
											<div>
												<input class="form-control form-control-sm" type="text" name="blnc_qty" id="blnc_qty" value="<%=balance_qty_mmbtu %>" 
												title="Qty To Be Supplied - Total Allocated Quantity =<%=supp_qty%> - <%=allocated_qty_mmbtu%> =<%=balance_qty_mmbtu%>" readonly>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
							<div align="center"><%=utilmsg.infoMessage("<b>Nomination/Allocation not Done Beyond Storage Duration!</b>") %></div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Proceed" onclick="doProceed();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Close" onclick="window.close();">
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="LTCORA_CN_CARGO_STORAGE_DTL">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="id_no" value="<%=id_no%>">

<input type="hidden" name="counterparty_cd" id="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="buy_sell" id="buy_sell" value="<%=buy_sell%>">
<input type="hidden" name="agmt_no" id="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" id="agmt_rev" value="<%=agmt_rev%>">
<input type="hidden" name="agmt_type" id="agmt_type" value="<%=agmt_type%>">
<input type="hidden" name="contract_type" id="contract_type" value="<%=contract_type%>">
<input type="hidden" name="cont_rev" id="cont_rev" value="<%=cont_rev%>">
<input type="hidden" name="cont_no" id="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cargo_no" id="cargo_no" value="<%=cargo_no%>">

<input type="hidden" name="actual_recpt_dt" id="actual_recpt_dt" value="<%=actual_recpt_dt%>">
<input type="hidden" name="window_start_dt" id="window_start_dt" value="<%=window_start_dt%>">
<input type="hidden" name="window_end_dt" id="window_end_dt" value="<%=window_end_dt%>">


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