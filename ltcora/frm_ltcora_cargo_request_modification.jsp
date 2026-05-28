<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function reload()
{
	window.opener.location.reload()();
	window.close();	
}
function doSubmit()
{
	var ltcora_tariff = document.forms[0].ltcora_tariff.value;
	var ltcora_tariff_unit = document.forms[0].ltcora_tariff_unit.value;
	var sug_per = document.forms[0].sug_per.value;
	var u = document.forms[0].u.value;
	var opration = document.forms[0].opration.value;
	
	var msg="";
	var flag=true;
	
	if(trim(ltcora_tariff) == "")
	{
		msg+="Enter LTCORA Teriff Amount!\n";
		flag=false;
	}
	if(trim(ltcora_tariff_unit) == "")
	{		
		msg+="Select LTCORA Teriff Unit!\n";
		flag=false;
	}
	if(trim(sug_per) == "")
	{
		msg+="Enter SUG(%)!\n";
		flag=false;
	}
	
	
	if(flag)
	{
		var a = confirm("Do you want to Submit?"); 
		
		
		if(a)
		{
			//document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
			reload();
		}
	}
	else
	{
		alert(msg)
	}
}

function doApprove()
{
	var ltcora_tariff = document.forms[0].ltcora_tariff.value;
	var ltcora_tariff_unit = document.forms[0].ltcora_tariff_unit.value;
	var sug_per = document.forms[0].sug_per.value;
	var u = document.forms[0].u.value;
	
	var opration = document.forms[0].opration.value;
	
	var msg="";
	var flag=true;
	
	if(trim(ltcora_tariff) == "")
	{
		msg+="Enter LTCORA Teriff Amount!\n";
		flag=false;
	}
	if(trim(ltcora_tariff_unit) == "")
	{
		msg+="Select LTCORA Teriff Unit!\n";
		flag=false;
	}
	if(trim(sug_per) == "")
	{
		msg+="Enter SUG(%)!\n";
		flag=false;
	}
	
	
	if(flag)
	{
		var a = confirm("Do you want to Approve?");
		
		if(a)
		{
			document.forms[0].opration.value="APPROVE";
			//document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
			reload();
		}
	}
	else
	{
		alert(msg)
	}
}

function enable_disable()
{
	var chk = document.getElementById("chk");
	
	var ltcora_tariff = document.forms[0].ltcora_tariff;
	var ltcora_tariff_unit = document.forms[0].ltcora_tariff_unit;
	var sug_per = document.forms[0].sug_per;
	var submit_btn = document.forms[0].submit_btn;
	var ltcora_tariff_unit = document.forms[0].ltcora_tariff_unit;
	var contract_type = document.forms[0].contract_type.value;
	
	if(chk.checked)
	{
		ltcora_tariff.disabled=false;
		//ltcora_tariff_unit.disabled=false;
		sug_per.disabled=false;
		submit_btn.disabled=false;
		
		if(contract_type == 'O' || contract_type == 'Q')
		{
			ltcora_tariff_unit.style.pointerEvents = "auto";
		}
	}
	else
	{
		ltcora_tariff.disabled=true;
		//ltcora_tariff_unit.disabled=true;
		sug_per.disabled=true;
		submit_btn.disabled=true;
		ltcora_tariff_unit.style.pointerEvents = "none";
	}
}

function checkRateFormate(obj) //WHEN CHANGE RATE UNIT
{ 
	var a="0"
	var b="0"
	
	var rate = document.forms[0].ltcora_tariff;
	
	if(obj.value == "1")
	{
		a="6";
		b="2";
	}
	else
	{
		a="6";
		b="4";
	}
	
	rate.setAttribute("onblur","checkNumber1(this,"+a+","+b+");");
	
	var c = parseInt(a)-parseInt(b);
	var flag=true;
	
	var fieldValue=rate.value;
    
    var len = 0;
    
    var str = fieldValue.substring(0,fieldValue.indexOf('.')).length;
	
	if(str == 0)
	{
		len = fieldValue.length;
	}
	else
	{
		len = str;
	}
    
    if(rate.value!="" && rate.value!=null && rate.value!=' ')
    {
		if((parseInt(len) > parseInt(c)))
		{
    		alert("Please, Enter In the Required  Format.."+'('+c+' ,'+b+' )');
    		rate.value= "";
    		rate.select();
			flag = false;
		}
		else
		{
			var decallowed = b;  // how many decimals are allowed?
        
        	if(isNaN(fieldValue) || fieldValue == "")
        	{
        		alert("Please, Enter In the Required  Format.."+'('+c+' ,'+b+' )');
        		rate.value="";
        		rate.select();	 
				flag=false;
        	}
      		else
      		{
         		if(fieldValue.indexOf('.') == -1) 
		    	{
		    		fieldValue += ".";
         		}
         	
         		dectext = fieldValue.substring(fieldValue.indexOf('.')+1, fieldValue.length);
         	
         		if(parseInt(dectext.length) > parseInt(decallowed))
            	{
		    		alert("Please, Enter In the Required  Format.."+'('+c+' ,'+b+') !!!');
		    		rate.value="";
		    		rate.select();		
			 		flag=false;
            	}
         		else
         		{
              		flag=true;
            	}
        	}
		}
   	}
    
    return flag;
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.ltcora.DataBean_LtcoraMaster" id="ltcora" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
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

ltcora.setCallFlag("LTCORA_CARGO_MODREQ");
ltcora.setComp_cd(owner_cd);
ltcora.setCounterparty_cd(counterparty_cd);
ltcora.setAgmt_no(agmt_no);
ltcora.setAgmt_rev_no(agmt_rev_no);
ltcora.setAgreement_type(agreement_type);
ltcora.setBuy_sale(buy_sale);
ltcora.setCont_no(cont_no);
ltcora.setCont_rev_no(cont_rev_no);
ltcora.setContract_type(contract_type);
ltcora.setCargo_no(cargo_no);
ltcora.init();

String ltcora_tariff = ltcora.getLtcora_tariff();
String ltcora_tariff_unit = ltcora.getLtcora_tariff_unit();
String approval_flag = ltcora.getApproval_flag();

String counterparty_name = ltcora.getCounterparty_name();

Double sug_per = ltcora.getSug_per();
int mod_count = ltcora.getMod_count();
%>
<body onload="">

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
						LTCORA (<%=counterparty_name%> - <%=cargo_ref %>) Modification Request
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th rowspan="1"></th>
										<th rowspan="1">Cargo Ref#</th>
										<th rowspan="1">LTCORA Tariff<span class="s-red">*</span></th>
										
										<th rowspan="1">SUG(%)<span class="s-red">*</span></th>
										<th rowspan="1"></th>
										<th rowspan="1">Approve</th>
									</tr>									
								</thead>
								<tbody id="itemTab">
									<tr>
										<td align="center">
											<%if(write_access.equals("Y")){%>
											<input type="checkbox" class="form-check-input" name="chk" id="chk" onclick="enable_disable();">
											<input type="hidden" name="chk_flag" id="chk_flag" value="N" >
											<%}else {%><%=1 %><%} %>
										</td>
										<td align="center"><%=cargo_ref %></td>
										<td>
											<div class="row m-b-5">
												<div class="col">
													<input type="text" class="form-control form-control-sm" name="ltcora_tariff" id="ltcora_tariff" value="<%=ltcora_tariff %>" disabled
													<%if(ltcora_tariff_unit.equals("1")){ %>onblur="checkNumber1(this,6,2);negNumber(this);"<%}else{ %>onblur="checkNumber1(this,6,4);negNumber(this);"<%}%>>
												</div>
												<div class="col">
													<select class="form-select form-select-sm" name="ltcora_tariff_unit" onchange="checkRateFormate(this);" style="pointer-events: none;">
								    					<option value="" selected="selected">--Select--</option>
								    					<option value="2">USD/MMBTU</option>
								    					<option value="1">INR/MMBTU</option>
								    				</select>
								    				<script>document.forms[0].ltcora_tariff_unit.value="<%=ltcora_tariff_unit%>"</script>
												</div>
											</div>	
										</td>
										<td>
											<input type="text" class="form-control form-control-sm" name="sug_per" id="sug_per" value="<%=sug_per %>" disabled>
										</td>
										<td align="center">
											<%if(write_access.equals("Y") && opration.equals("INSERT")){ %>
											<input type="button" class="btn btn-warning com-btn" name="submit_btn" value="Submit" onclick="doSubmit();" disabled>
											<%}else{ %>
											<input type="button" class="btn btn-warning com-btn" name="" value="Submit" disabled>
											<%} %>
										</td>
										<td align="center">
											<i class="fa fa-thumbs-o-up fa-2x" onclick="doApprove()"
											    style="<%if(!approve_access.equals("Y") || mod_count==0 || approval_flag.equals("Y")){ %>
													pointer-events: none; opacity: .65; color: gray;
												<%} else{%>
												    color:blue;
												<%}%>">
											</i>
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

<input type="hidden" name="option" value="LTCORA_CARGO_MODREQ">
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
<input type="hidden" name="opration" value="<%=opration%>">

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