<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function doSubmit()
{
	var msg = "Please Check the Following Field(s):\n\n";
	var flag = true;
	
	var i = 0;
	var index = 0;
	
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var seq_no = document.forms[0].seq_no.value;
	var u = document.forms[0].u.value;
	var value = document.forms[0].credit_exceed_value.value;
	
	var line_start_date = document.forms[0].line_start_date;
	var line_end_date = document.forms[0].line_end_date;
	var line_seq_no = document.forms[0].line_seq_no;
	
	if(from_dt==null || from_dt=='' || from_dt==' ' || from_dt=='0')
	{
		msg += "Please Enter The From Date Field Properly !!!\n";
		flag = false;
	}
	if(to_dt==null || to_dt=='' || to_dt==' ' || to_dt=='0')
	{
		msg += "Please Enter The To Date Field Properly !!!\n";
		flag = false;
	}
	if(trim(value)=="")
	{
		msg+="Please Enter Value!\n";
		flag=false;
	}
		
	var dateOverWrite_count=parseInt("0");
	if(line_start_date!=null && line_start_date!=undefined)
	{
		if(line_start_date.length!=undefined)
		{
			for(var i=0;i<line_start_date.length;i++)
			{
				if(line_seq_no[i].value!=seq_no)
				{
					var value_1 = compareDate(line_start_date[i].value,to_dt);
					var value_11 = compareDate(line_end_date[i].value,from_dt);
					
					if(value_1 != '1' && value_11 != '2')
					{
						dateOverWrite_count++;
					}
				}
			}
		}
		else
		{
			if(line_seq_no.value!=seq_no)
			{
				var value_1 = compareDate(line_start_date.value,to_dt);
				var value_11 = compareDate(line_end_date.value,from_dt);
				
				if(value_1 != '1' && value_11 != '2')
				{
					dateOverWrite_count++;
				}
			}
		}
	}
	
	if(parseInt(dateOverWrite_count) > 0)
	{
		msg += "Credit Exceed Line Duration already covered, Please check date range!\n";
		flag = false;
	}
	
	if(flag)
	{
		var a = confirm("Do You Want To Submit Credit Exceed Specification Details For Selected Deal?");
		
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

function allowCreditExceedEdit()
{
	var sysdate=document.forms[0].sysdate.value;
	var opration=document.forms[0].opration.value;
	
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var temp_from_dt = document.forms[0].temp_from_dt.value;
	var temp_to_dt = document.forms[0].temp_to_dt.value;
	
	var cont_start_dt = document.forms[0].cont_start_dt.value;
	var cont_end_dt = document.forms[0].cont_end_dt.value;
	
	if(from_dt != "" && to_dt != "")
	{
		var value_0 = compareDate(from_dt,to_dt);
		var value_1 = compareDate(from_dt,cont_start_dt);
		var value_11 = compareDate(from_dt,cont_end_dt);
		var value_2 = compareDate(to_dt,cont_start_dt);
		var value_22 = compareDate(to_dt,cont_end_dt);
		var value_3 = compareDate(sysdate,temp_from_dt);
		var value_33 = compareDate(sysdate,to_dt);
		
		if(value_0 == "1") //comparing from and to date
		{
			alert("Credit Exceed line From date "+from_dt+" > To date "+to_dt+" not Allowed!");
		}
		else if(value_1 == "2" ||  value_11 == "1") //comparing from dt with contract date
		{
			alert("Credit Exceed line ("+from_dt+" - "+to_dt+") should be with in range of Contract period ("+cont_start_dt+" - "+cont_end_dt+")!");
		}
		else if(value_2 == "2" ||  value_22 == "1") //comparing to dt with contract date
		{
			alert("Credit Exceed line ("+from_dt+" - "+to_dt+") should be with in range of Contract period ("+cont_start_dt+" - "+cont_end_dt+")!");
		}
		/*else if(value_3 == "1" &&  value_33 == "1" && opration=="MODIFY") //comparing to dt with system date, iff from_dt < system date
		{
			alert("Credit Exceed line To date "+to_dt+" should not be less than System date "+sysdate+"!");
		}*/
	}
}

function doModify(seq_no, from_dt, to_dt, remark,status,value)
{ 	
	document.forms[0].from_dt.value=from_dt;
	document.forms[0].to_dt.value=to_dt;
	document.forms[0].temp_from_dt.value=from_dt;
	document.forms[0].temp_to_dt.value=to_dt;
	
	document.forms[0].seq_no.value=seq_no;
	document.forms[0].remark.value=remark;
	document.forms[0].status.value=status;
	document.forms[0].credit_exceed_value.value=value;
	
	/* var sysdate=document.forms[0].sysdate.value;
	var value_1 = compareDate(sysdate,from_dt);
	
	if(value_1=="1")
	{
		document.forms[0].from_dt.style.pointerEvents = "none";
		document.forms[0].from_dt.readOnly=true;
		document.forms[0].price_type.style.pointerEvents = "none";
		document.forms[0].physical_curve.style.pointerEvents = "none";
		document.forms[0].currency.style.pointerEvents = "none";
		document.forms[0].rate.readOnly=true;
	}
	else
	{
		document.forms[0].from_dt.style.pointerEvents = "auto";
		document.forms[0].from_dt.readOnly=false;
		document.forms[0].price_type.style.pointerEvents = "auto";
		document.forms[0].physical_curve.style.pointerEvents = "auto";
		document.forms[0].currency.style.pointerEvents = "auto";
		document.forms[0].rate.readOnly=false;
	} */
	
	document.forms[0].opration.value="MODIFY";
}

function isNumeric()
{
	var credit_exceed_value = document.forms[0].credit_exceed_value.value;
	var number = /^-?\d*\.?\d+$/;
	
	if(credit_exceed_value.match(number))
	{
		checkNumber1(document.forms[0].credit_exceed_value,'14','2')
	}
	else
	{
		alert("Enter Only Numeric Value !!");
		document.forms[0].credit_exceed_value.value="";
		return false;
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DataBean_CreditRisk" id="credit_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String cont_end_dt=request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String isDealFm=request.getParameter("isDealFm")==null?"":request.getParameter("isDealFm");

String display_map_id=utilBean.NewDealMappingId(owner_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");

credit_risk.setCallFlag("EXCEED_CREDIT_DAYS_CONFIG");
credit_risk.setCounterparty_cd(counterparty_cd);
credit_risk.setComp_cd(owner_cd);
credit_risk.setAgmt_no(agmt_no);
credit_risk.setAgmt_rev_no(agmt_rev_no);
credit_risk.setCont_no(cont_no);
credit_risk.setCont_rev_no(cont_rev_no);
credit_risk.setContract_type(contract_type);
credit_risk.init();

String counterparty_abbr=credit_risk.getCounterparty_abbr();

Vector VFROM_DT = credit_risk.getVFROM_DT();
Vector VTO_DT = credit_risk.getVTO_DT();
Vector VSTATUS = credit_risk.getVSTATUS();
Vector VSTATUS_NM = credit_risk.getVSTATUS_NM();
Vector VSEQ_NO = credit_risk.getVSEQ_NO();
Vector VREMARK = credit_risk.getVREMARK();
Vector VVALUE = credit_risk.getVVALUE();

%>
<body>
<%@ include file="../home/loading.jsp"%>
<form action="../servlet/Frm_CreditRisk" method="post">

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
					    	Exceed Credit Days <%=counterparty_abbr%> (<%=display_map_id%>) [Contract Period : <%=cont_start_dt %> - <%=cont_end_dt %>]
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
										<th>From</th>
										<th>To</th>
										<th>Credit Exceed Value<br>(INR)</th>
										<th>Status</th>
										<th>Remark</th>
									</tr>
								</thead>
								<tbody>
								<%if(VFROM_DT.size()>0) {%>
									<%for(int i=0;i<VFROM_DT.size(); i++){%>	
									<tr>
										<td align="center">
											<input type="radio" name="rd" 
												<%if (!VSTATUS.elementAt(i).equals("P")){ %>
													disabled
												<%} %>
												onclick="doModify('<%=VSEQ_NO.elementAt(i)%>','<%=VFROM_DT.elementAt(i)%>','<%=VTO_DT.elementAt(i)%>',
													'<%=VREMARK.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>','<%=VVALUE.elementAt(i)%>');">&nbsp;&nbsp;
											<%=i+1%>
										</td>
										<td align="center">
											<input type="hidden" name="line_start_date" id="line_start_date<%=i%>" value="<%=VFROM_DT.elementAt(i)%>" >
											<input type="hidden" name="line_seq_no" id="line_seq_no<%=i%>" value="<%=VSEQ_NO.elementAt(i)%>" >
											<%=VFROM_DT.elementAt(i)%>
										</td>
										<td align="center">
											<input type="hidden" name="line_end_date" id="line_end_date<%=i%>" value="<%=VTO_DT.elementAt(i)%>" >
											<%=VTO_DT.elementAt(i)%>
										</td>
										<td align="right"><%=VVALUE.elementAt(i)%></td>																	
										<td align="center">
												<span class="
												<%if(VSTATUS.elementAt(i).equals("P")){ %>
													alert alert-primary
												<%}else if(VSTATUS.elementAt(i).equals("O")){  %>
													alert alert-success
												<%}else if(VSTATUS.elementAt(i).equals("C")){  %>
													alert alert-danger
												<%} %>
												"><b><%=VSTATUS_NM.elementAt(i)%></b></span>
										</td>
										<td align="left"><%=VREMARK.elementAt(i)%></td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="6" align="center"><%=utilmsg.infoMessage("<b>No Credit Exceed Days Configured for the Contract!</b>")%></td>
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
	&nbsp;
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Exceed Credit Days Details
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>From Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="" maxLength="10" 
			      						onchange="validateDate(this);allowCreditExceedEdit();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<input type="hidden" name="seq_no" value="">
		      						<input type="hidden" name="temp_from_dt" value="">
	      						</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>To Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="" maxLength="10" 
			      						onchange="validateDate(this);allowCreditExceedEdit();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<input type="hidden" name="temp_to_dt" value="">
	      						</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">	
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Credit Exceed Value (INR)<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="credit_exceed_value" onchange="isNumeric();">
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Status<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="status" id="status" 
									<%if(isDealFm.equals("Y")){%>style="pointer-events: none;"<%}%>>
										<option value="P">Pending</option>
										<option value="O">In Order</option>
										<option value="C">Cancel</option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">	
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Remark</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<textarea class="form-control form-control-sm"  name="remark" rows="1" maxlength="150"></textarea>
				      			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="CREDIT_EXCEED_CONFIG">
<input type="hidden" name="opration" value="INSERT">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="cont_start_dt" value="<%=cont_start_dt%>">
<input type="hidden" name="cont_end_dt" value="<%=cont_end_dt%>">

<input type="hidden" name="sysdate" value="<%=sysdate%>">

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