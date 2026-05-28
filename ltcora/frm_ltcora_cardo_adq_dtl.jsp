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

function calTotalQty() 
{
    var total = 0;
    var inputs = document.getElementsByName('adq_mmbtu');
    var isValid = true;

    for (var i = 0; i < inputs.length; i++)
    {
        var value = inputs[i].value.trim();
       
        if (value !== '')
        {
            if (!isNumeric(value))
            {
                isValid = false;
                alert("Please enter a valid number in all input fields!!");
                inputs[i].value="";
                break;
            }
           
            total += parseFloat(value);
        }
    }

    if (isValid) 
    {
    	document.getElementById('total_qty_value').value = total.toFixed(2);
    }
    else 
    {
        document.getElementById('total_qty_value').value = '';
    }
}

function isNumeric(value) 
{
    return !isNaN(parseFloat(value)) && isFinite(value);
}

function doSubmit(id_no)
{
	var total_qty_value = document.forms[0].total_qty_value.value;
	
	var msg = "";
    var flag = true;
    
    if (trim(total_qty_value) == "") //|| trim(total_qty_value) == "0.00"
    {
        msg += "Please Enter Daily ADQ MMBTU!\n";
        flag = false;
    }
    
    if (flag)
    {
        var a = confirm("Do you want to Submit?");
        if (a)
        {
            document.forms[0].submit();
        }
        window.opener.setTotalADQDetail(total_qty_value,id_no);
    } 
    else 
    {
        alert(msg);
    }
}

function checkRateFormat(id, index, a, b) {
    let rateInput = document.getElementById(id + index).value.trim();

    let hasDecimal = rateInput.includes('.');

    let parts = rateInput.split('.');
    let integerPart = parts[0];
    let decimalPart = parts[1];

    if (integerPart.length > a) {
    	alert("Please, Enter In the Required  Format..("+a+" ,"+b+")");
        document.getElementById(id + index).value = "";
        document.forms[0].total_qty_value.value="";
        return;
    }

    if (hasDecimal && decimalPart.length > b) {
        alert("Please, Enter In the Required  Format..("+a+" ,"+b+")");
        document.getElementById(id + index).value = "";
        document.forms[0].total_qty_value.value="";
        return;
    }

    if (!hasDecimal && rateInput.length > a) {
    	 alert("Please, Enter In the Required  Format..("+a+" ,"+b+")");
        document.getElementById(id + index).value = "";
        document.forms[0].total_qty_value.value="";
        return;
    }

    if (isNaN(rateInput) || parseFloat(rateInput) < 0) {
        alert("Please enter a valid Number!!");
        document.getElementById(id + index).value = "";
        document.forms[0].total_qty_value.value="";
        return;
    }
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateUtil" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.ltcora.DataBean_LtcoraMaster" id="dbltcora" scope="request"></jsp:useBean>

<%
String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
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

String disp_cont_no = request.getParameter("cargo_number_disp")==null?"":request.getParameter("cargo_number_disp");
String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");

DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

LocalDate startDate = LocalDate.parse(window_start_dt, formatter);
LocalDate endDate = LocalDate.parse(window_end_dt, formatter);

Vector<String> VDATE_LIST = new Vector<>();
Vector<String> VMOD_ADQ_QTY = new Vector<>();

dbltcora.setCallFlag("LTCORA_UNLOADED_QUANT_DTL");
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

Vector VADQ_DT = dbltcora.getVADQ_DT();
Vector VADQ_QTY = dbltcora.getVADQ_QTY();

int noOfDates=0;

LocalDate currentDate = startDate;
while (!currentDate.isAfter(endDate)) 
{
	noOfDates++;
	VDATE_LIST.add(currentDate.format(formatter));
    currentDate = currentDate.plusDays(1);
    
    if(VADQ_DT.isEmpty())
    {
    	VADQ_QTY.add("");
    }
}

//HM20250303 : below part is added if Arrival window is updated after adding the Unloaded quantity for previous Data(Arrival window)
if(!VADQ_DT.isEmpty())
{
	if (VADQ_DT.size() < VDATE_LIST.size())
	{
        // Add null values to VDATE_LIST to make the sizes equal
        int difference = VDATE_LIST.size() - VADQ_DT.size();
        for (int i = 0; i < difference; i++)
        {
        	VADQ_QTY.add("");  // Add null if partially Unloaded quantity is added
        	VADQ_DT.add("");  // Add null if partially Unloaded quantity is added
        }
    }
}
%>
<body onload="calTotalQty();">
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
					    	Daily ADQ Unloaded Quantity Details (<%=cargo_ref_no %>)
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>ADQ Date<span class="s-red">*</span></th>
										<th>Daily ADQ MMBTU</th>
									</tr>
								</thead>
								<tbody>
								<%if(VDATE_LIST.size()>0) {%>
									<%for(int i=0;i<VDATE_LIST.size(); i++){
									%>
								<tr>
 										<td align="center"><%=i+1%></td>
										<td align="center">
											<div style="width:150px;">
												<div class="input-group input-group-sm" >
						      						<input class="form-control form-control" type="text" name="window_dt" id="window_dt<%=i%>" value="<%=VDATE_LIST.elementAt(i) %>" readonly>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div> 
					      					</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input class="form-control form-control-sm" type="text" name="adq_mmbtu" id="adq_mmbtu<%=i%>" value="<%=VADQ_QTY.elementAt(i) %>" onchange="calTotalQty();" onblur="checkRateFormat('adq_mmbtu','<%=i%>','8','2')">
											</div>
										</td>
									</tr>
									<%} %>
									<tr>
										<td colspan="2" align="right"><b>Total ADQ MMBTU:<span class="s-red">*</span></b></td>
										<td align="center">
											<div style="width:100px;">
												<input class="form-control form-control-sm" name="total_qty_value" id="total_qty_value" value="" readonly>
											</div>		
										</td>
									</tr>
								<%}else{ %>
									<tr>
										<td colspan="11" align="center"><%=utilmsg.infoMessage("<b>No Cargo Arrival Window Avaulable!!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit('<%=Integer.parseInt(cargo_no)-1%>');">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Close" onclick="window.close();">
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="LTCORA_CN_CARGO_ADQ_DTL">
<input type="hidden" name="opration" value="INSERT">

<input type="hidden" name="counterparty_cd" id="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="buy_sell" id="buy_sell" value="<%=buy_sell%>">
<input type="hidden" name="agmt_no" id="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" id="agmt_rev" value="<%=agmt_rev%>">
<input type="hidden" name="agmt_type" id="agmt_type" value="<%=agmt_type%>">
<input type="hidden" name="contract_type" id="contract_type" value="<%=contract_type%>">
<input type="hidden" name="cont_rev" id="cont_rev" value="<%=cont_rev%>">
<input type="hidden" name="cont_no" id="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cargo_no" id="cargo_no" value="<%=cargo_no%>">

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