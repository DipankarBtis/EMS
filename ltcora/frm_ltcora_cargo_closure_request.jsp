<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function closeChild(msg,msg_type)
{
	var cargo_cont_status=document.forms[0].cargo_cont_status.value;
	if(cargo_cont_status=='X' || cargo_cont_status=='A')
	{
// 		var msg = document.forms[0].msg.value;
// 		var msg_type = document.forms[0].msg_type.value;
		//window.opener.location.reload();  
		window.opener.refreshParent(msg,msg_type);
		window.close();  
	}
}

function reload()
{
	window.opener.location.reload()();
	window.close();	
}

function enable_disable()
{
	var chk = document.getElementById("chk");
	var clsr_dt = document.forms[0].clsr_dt;
	var req_btn = document.getElementById("req_btn");
	//var aprv_btn = document.getElementById("aprv_btn");
	//var rej_btn = document.getElementById("rej_btn");
	
	if(chk.checked)
	{
		clsr_dt.disabled=false;
		req_btn.disabled=false;
		//aprv_btn.disabled=false;
		//rej_btn.disabled=false;
	}
	else
	{
		clsr_dt.disabled=true;
		req_btn.disabled=true;
		//aprv_btn.disabled=true;
		//rej_btn.disabled=true;
	}
}

function approve_or_reject_closure(request_status)
{
	var clsr_dt=document.forms[0].clsr_dt;
	var counterparty_cd = document.forms[0].counterparty_cd;
	var agmt_no = document.forms[0].agmt_no;
	var agmt_rev_no = document.forms[0].agmt_rev_no;
	var buy_sale = document.forms[0].buy_sale;
	var agreement_type = document.forms[0].agreement_type;
	var cargo_ref = document.forms[0].cargo_ref;
	var cargo_no = document.forms[0].cargo_no;
	var contract_type = document.forms[0].contract_type;
	var cont_no = document.forms[0].cont_no;
	var cont_rev_no = document.forms[0].cont_rev_no;

	var flag = true;
	var msg="";
	
	if(clsr_dt.value == '')
	{
		msg="Closure Date can't be empty!\n";	
		flag=false;
	}
	
	if(flag==true)
	{
		//alert("company_cd:"+counterparty_cd.value+"\nagmt_no:"+agmt_no.value+"\nagmt_rev:"+agmt_rev_no.value+"\nagmt_type:"+agreement_type.value+"\ncont_no:"+cont_no.value+"\ncont_rev:"+cont_rev_no.value+"\ncont_type:"+contract_type.value+"\ncargo_no:"+cargo_no.value+"\nrequest_status:"+request_status);
		var a = confirm("Do you want to "+request_status.toLowerCase()+" closure request for cargo# ("+cargo_ref.value+")");
		if(a)
		{
			document.forms[0].option.value="APPROVE_REJECT_CLOSURE_REQUEST";
			document.forms[0].clsr_dt=clsr_dt.value;
			document.forms[0].opration.value=request_status;
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function requestClosure()
{
	var clsr_dt=document.forms[0].clsr_dt;
	var cargo_ref = document.forms[0].cargo_ref;
	var alloc_qty = document.forms[0].alloc_qty;
	var flag = true;
	var msg="";
	
	if(clsr_dt.value == '')
	{
		msg="Closure Date can't be empty!\n";	
		flag=false;
	}
	if(parseFloat(alloc_qty.value)!=parseFloat(0))		//As per Vijay's Feedback 'if cargo balance qty not zero system should not allow to initiate Request Closure'.
	{
		msg="Quantity allocated till closure should be 0!\n";
		flag=false;
	}
	if(flag==true)
	{
		var a = confirm("Do you want to submit closure request for cargo# ("+cargo_ref.value+")");
		if(a)
		{
			document.forms[0].submit();
			reload();			
		}
	}
	else
	{
		alert(msg);
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.ltcora.DataBean_LtcoraMaster" id="ltcora" scope="request"></jsp:useBean>
<%
String buy_sale=request.getParameter("buy_sale")==null?"":request.getParameter("buy_sale");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String agreement_type=request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
String cargo_ref = request.getParameter("cargo_number")==null?"":request.getParameter("cargo_number");
String closure_eff_dt = request.getParameter("closure_eff_dt")==null?"":request.getParameter("closure_eff_dt");
String cargo_cont_status = request.getParameter("cargo_cont_status")==null?"":request.getParameter("cargo_cont_status");
String closure_flag = request.getParameter("closure_flag")==null?"":request.getParameter("closure_flag");
String clsr_qty = request.getParameter("closure_qty")==null?"0.00":request.getParameter("closure_qty");

ltcora.setCallFlag("LTCORA_CARGO_CLOSURE_REQUEST");
ltcora.setComp_cd(owner_cd);
ltcora.setCounterparty_cd(counterparty_cd);
ltcora.setContract_type(contract_type);
ltcora.setCont_no(cont_no);
ltcora.setCont_rev_no(cont_rev_no);
ltcora.setAgmt_no(agmt_no);
ltcora.setAgmt_rev_no(agmt_rev_no);
ltcora.setAgmt_type(agreement_type);
ltcora.setCargo_no(cargo_no);
ltcora.init();

cont_rev_no = ltcora.getCont_rev_no();
String counterparty_name = ltcora.getCounterparty_name();
String closure_qty = ltcora.getClosure_qty();
String clsr_eff_dt = ltcora.getClosure_eff_dt();
String clsr_flag = ltcora.getClosure_flag();
clsr_flag = clsr_flag.equals("")?closure_flag:clsr_flag;
closure_qty = Double.parseDouble(closure_qty)==0?clsr_qty:closure_qty;
closure_eff_dt = closure_eff_dt.equals("")?clsr_eff_dt:closure_eff_dt;

%>

<body <%if(!msg.equals("")){%> onload="closeChild('<%=msg%>','<%=msg_type%>');"<%} %>>
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
						LTCORA (<%=counterparty_name%> - <%=cargo_ref %>) Closure Request
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th></th>
										<th>Cargo Ref#</th>
										<th>Closure Date<span class="s-red">*</span></th>
										<th>Quantity Allocated till Closure<span class="s-red">*</span></th>
										<th>Request</th>
										<th>Approve</th>
										<th>Reject</th>
									</tr>
								</thead>
								<tbody id="itemTab">
									<tr>
										<td align="center">
											<%if(write_access.equals("Y") && !clsr_flag.equals("R")){%>
											<input type="checkbox" class="form-check-input" name="chk" id="chk" onclick="enable_disable();">
											<input type="hidden" name="chk_flag" id="chk_flag" value="N" >
											<%}else {%><%=1 %><%}%>
										</td>
										<td align="center"><%=cargo_ref %></td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
													<input type="text" class="form-control form-control-sm date fmsdtpick" name="clsr_dt" id="clsr_dt" 
						      						value="" maxLength="10" onchange="validateDate(this);" autocomplete="off"
						      						<%if(clsr_flag.equals("R")){%> disabled <%} %>
						      						>
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      					</div>
						      					<script>
						      						document.forms[0].clsr_dt.value='<%=closure_eff_dt%>';
						      					</script>
						      				</div>
						      			</td>
										<td align="center">
											<div style="width:150px;">
												<div class="input-group input-group-sm">
													<input type="text" class="form-control form-control-sm" name="alloc_qty" id="alloc_qty" value=<%=closure_qty %> readOnly>
												</div>
											</div>
										</td>
										<td align="center">
											<input type="button" class="btn btn-warning com-btn" value="Request Closure" style="border-radius: 50px;" id="req_btn" 
											onclick="requestClosure();"
											disabled>
										</td>
										<td align="center">
												<i class="fa fa-thumbs-o-up fa-2x" onclick="approve_or_reject_closure('APPROVE');"
											    style="<%if(!approve_access.equals("Y") && !clsr_flag.equals("R") || !clsr_flag.equals("R")){ %>
													pointer-events: none; opacity: .65; color: gray;
												<%} else{%>
												    color:blue;
												<%}%>">
												</i>
											<!-- <input type="button" class="btn btn-warning com-btn" value="Approve Closure" style="border-radius: 50px;" id="aprv_btn" disabled> -->
										</td>
										<td align="center">
											<i class="fa fa-thumbs-o-down fa-2x" onclick="approve_or_reject_closure('REJECT');"
											    style="<%if(!approve_access.equals("Y") || !clsr_flag.equals("R")){ %>
													pointer-events: none; opacity: .65; color: gray;
												<%} else{%>
												    color:blue;
												<%}%>">
												</i>
											<!-- <input type="button" class="btn btn-warning com-btn" value="Reject Closure" style="border-radius: 50px;" id="rej_btn" disabled> -->
										</td>
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

<input type="hidden" name="option" value="LTCORA_CARGO_CLOSURE_REQUEST">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="buy_sale" value="<%=buy_sale%>">
<input type="hidden" name="agreement_type" value="<%=agreement_type%>">
<input type="hidden" name=cargo_ref value="<%=cargo_ref%>">
<input type="hidden" name=cargo_no value="<%=cargo_no%>">
<input type="hidden" name=contract_type value="<%=contract_type%>">
<input type="hidden" name=cont_no value="<%=cont_no%>">
<input type="hidden" name=cont_rev_no value="<%=cont_rev_no%>">
<input type="hidden" name="opration" value="">
<input type="hidden" name="cargo_cont_status" value="<%=cargo_cont_status%>">
<input type="hidden" name="msg" value="<%=msg%>">
<input type="hidden" name="msg_type" value="<%=msg_type%>">

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
