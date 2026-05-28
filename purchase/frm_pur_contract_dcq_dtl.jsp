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
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var dcq = document.forms[0].dcq.value;
	var status = document.forms[0].status_flag.value;
	var seq_no = document.forms[0].seq_no.value;
	var u = document.forms[0].u.value;
	var line_start_date = document.forms[0].line_start_date;
	var line_end_date = document.forms[0].line_end_date;
	var line_seq_no = document.forms[0].line_seq_no;
	
	var msg = "Please Check the Following Field(s):\n\n";
	var flag=true;
	
	if(trim(from_dt)=='')
	{
		msg += "Please Enter From Date!\n";
		flag = false;
	}
	if(trim(to_dt)=='')
	{
		msg += "Please Enter To Date!\n";
		flag = false;
	}
	if(trim(dcq) == '')
	{
		msg+="Please Enter DCQ !\n";
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
		msg += "Variable DCQ Timeline already covered, Please check date range!\n";
		flag = false;
	}
	
	if(flag)
	{
		var a= confirm("Do you want to Submit Variable DCQ?");
		if(a)
		{
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function doModify(from_dt,to_dt,dcq,status_flag,remark,seq_no)
{
	document.forms[0].from_dt.value=from_dt;
	document.forms[0].to_dt.value=to_dt;
	document.forms[0].dcq.value=dcq;
	document.forms[0].status_flag.value=status_flag;
	document.forms[0].remark.value=remark;
	document.forms[0].seq_no.value=seq_no;
	
	document.forms[0].opration.value='MODIFY';
	
	if(status_flag=='Y')
	{
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="Y";
		document.getElementById("flexSwitchCheckChecked").checked = true;
	}
	else
	{
		document.getElementById("lb").innerHTML="In-active";
		document.getElementById("status_flag").value="N";
		document.getElementById("flexSwitchCheckChecked").checked = false;
	}
}

function allowDCQLineEdit()
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
		var val_dt = compareDate(sysdate,cont_end_dt);
		var val_dt_1 = compareDate(sysdate,from_dt);
		
		var value_0 = compareDate(from_dt,to_dt);
		var value_1 = compareDate(from_dt,cont_start_dt);
		var value_11 = compareDate(from_dt,cont_end_dt);
		var value_2 = compareDate(to_dt,cont_start_dt);
		var value_22 = compareDate(to_dt,cont_end_dt);
		var value_3 = compareDate(sysdate,temp_from_dt);
		var value_33 = compareDate(sysdate,to_dt);
		
		if(val_dt == "1")
		{
			alert("Contract is expired! - Variable DCQ Detail Can't be Configured!");
			document.forms[0].from_dt.value="";
			document.forms[0].to_dt.value="";
			flag=false;
		}
		/*else if(val_dt_1 == "1" && opration=="INSERT")
		{
			alert("Variable DCQ timeline - From Date ("+from_dt+") < System date ("+sysdate+") not Allowed!"); 
			document.forms[0].from_dt.value="";
			document.forms[0].to_dt.value="";
			flag=false;
		}*/		
		else if(value_0 == "1") //comparing from and to date
		{
			alert("Variable DCQ timeline From date ("+from_dt+") > To date ("+to_dt+")! \nThis is not Allowed!");
			document.forms[0].from_dt.value="";
			document.forms[0].to_dt.value="";
			flag=false;
		}
		else if(value_1 == "2" ||  value_11 == "1") //comparing from dt with contract date
		{
			alert("Variable DCQ timeline ("+from_dt+" - "+to_dt+") should be with in range of Contract period ("+cont_start_dt+" - "+cont_end_dt+")!");
			document.forms[0].from_dt.value="";
			document.forms[0].to_dt.value="";
			flag=false;
		}
		else if(value_2 == "2" ||  value_22 == "1") //comparing to dt with contract date
		{
			alert("Variable DCQ timeline ("+from_dt+" - "+to_dt+") should be with in range of Contract period ("+cont_start_dt+" - "+cont_end_dt+")!");
			document.forms[0].from_dt.value="";
			document.forms[0].to_dt.value="";
			flag=false;
		}
		else if(value_3 == "1" &&  value_33 == "1" && opration=="MODIFY") //comparing to dt with system date, iff from_dt < system date
		{
			alert("Variable DCQ timeline To date ("+to_dt+") < System date - 1 ("+yesdate+")! \nThis is not Allowed!");
			document.forms[0].from_dt.value="";
			document.forms[0].to_dt.value="";
			flag=false;
		}
		/* else if(value_3 != "1" &&  val_dt_1 == "1" && opration=="MODIFY") //comparing from dt with system date
		{
			alert("Variable DCQ timeline From date ("+from_dt+") < System date ("+sysdate+")! \nThis is not Allowed!");
			document.forms[0].from_dt.value="";
			document.forms[0].to_dt.value="";
			flag=false;
		} */
	}
}

function checkDateRange(index,dt_flag)
{
	var start_dt=document.forms[0].start_dt.value;
	var end_dt=document.forms[0].end_dt.value;
	
	var from_dt=document.getElementById("from_dt"+index).value;
	var to_dt=document.getElementById("to_dt"+index).value;
	
	var flag=true;
	if((from_dt!="" && trim(from_dt) != "" && from_dt != null) && (to_dt!="" && trim(to_dt) != "" && to_dt != null))
	{
		if(dt_flag=="F")
		{
			var count = compareDate(from_dt,to_dt);
			if(parseInt(count) == 1)
			{
				alert("From Date should be less or equal To Date!")
				document.getElementById("from_dt"+index).value="";
				flag=false;
			}
		}
		else if(dt_flag=="T")
		{
			var count = compareDate(from_dt,to_dt);
			if(parseInt(count) == 1)
			{
				alert("To Date should be grater or equal From Date!")
				document.getElementById("to_dt"+index).value="";
				flag=false;
			}
		}
	}
	
	if(flag)
	{
		var value = compareDate(start_dt,from_dt);
		var value1 = compareDate(end_dt,to_dt);
		//alert(value+"=="+value1)
		
		if(parseInt(value) == 1 && parseInt(value1) == 2)
	  	{
			flag=false;
	  	}
		else if(parseInt(value) == 1)
		{
			flag=false;
		}
		else if(parseInt(value1) == 2)
		{
			flag=false;
		}
		
		if(!flag)
		{
			alert("From Date("+from_dt+") & To Date("+to_dt+") not in Range of Contract Period("+start_dt+" - "+end_dt+")!")
			document.getElementById("from_dt"+index).value="";
			document.getElementById("to_dt"+index).value="";
		}
	}
}
function setActiveInactive(obj)
{
	if(obj.checked)
	{
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="Y";
	}
	else
	{
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="N";
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DataBean_Trader_Contract_Mst" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");

String cont_start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String cont_end_dt=request.getParameter("end_dt")==null?"":request.getParameter("end_dt");

purchase.setCallFlag("TRADER_CONTRACT_DCQ_DTL");
purchase.setCounterparty_cd(counterparty_cd);
purchase.setComp_cd(owner_cd);
purchase.setAgmt_no(agmt_no);
purchase.setAgmt_rev_no(agmt_rev_no);
purchase.setCont_no(cont_no);
purchase.setCont_rev_no(cont_rev_no);
purchase.setContract_type(contract_type);
purchase.init();

Vector VSEQ_NO = purchase.getVSEQ_NO();
Vector VFROM_DT = purchase.getVFROM_DT();
Vector VTO_DT = purchase.getVTO_DT();
Vector VDCQ = purchase.getVDCQ();
Vector VREMARK = purchase.getVREMARK();
Vector VSTATUS = purchase.getVSTATUS();
Vector VIS_RADIO_ENABLE = purchase.getVIS_RADIO_ENABLE();

String counterparty_abbr= purchase.getCounterparty_abbr();
String display_map_id=purchase.getDisplay_map_id();

%>
<body>
<form method="post" action="../servlet/Frm_Trader_Contract_Mst">
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
					    	Variable DCQ Details <%=counterparty_abbr%> (<%=display_map_id%>) [Contract Period : <%=cont_start_dt %> - <%=cont_end_dt %>]
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
										<th>From Date</th>
										<th>To Date</th>
										<th>DCQ (MMBTU)</th>
										<th>Status</th>
										<th>Remark</th>
									</tr>
								</thead>
								<tbody id="itemTab">
								<%if(VFROM_DT.size() > 0){%>
									<%for(int i=0;i<VFROM_DT.size(); i++){%>
									<tr id="row<%=i+1%>">
										<td align="center">
											<input type="radio" name="rd" 
											<%if (VIS_RADIO_ENABLE.elementAt(i).equals("N")){ %>
												disabled
											<%} %>
												onclick="doModify('<%=VFROM_DT.elementAt(i)%>','<%=VTO_DT.elementAt(i)%>','<%=VDCQ.elementAt(i)%>',
													'<%=VSTATUS.elementAt(i)%>','<%=VREMARK.elementAt(i)%>','<%=VSEQ_NO.elementAt(i)%>')">&nbsp;
											<%=i+1%>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
													<input type="hidden" name="line_start_date" id="line_start_date<%=i%>" value="<%=VFROM_DT.elementAt(i)%>" >
													<input type="hidden" name="line_seq_no" id="line_seq_no<%=i%>" value="<%=VSEQ_NO.elementAt(i)%>" >
						      						<%=VFROM_DT.elementAt(i)%>
					      						</div>
				      						</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
													<input type="hidden" name="line_end_date" id="line_end_date<%=i%>" value="<%=VTO_DT.elementAt(i)%>" >
						      						<%=VTO_DT.elementAt(i)%>
					      						</div>
				      						</div>
										</td>
										<td align="center">
											<%=VDCQ.elementAt(i)%>
										</td>
										<td align="center">
											<div style="width:50px;">
												<font style="color:<%if(VSTATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font>
												<%if(VSTATUS.elementAt(i).equals("Y")){%>Active<%}else{%>In-Active<%} %>
											</div>
										</td>
										<td align="center">
											<%=VREMARK.elementAt(i)%>
										</td>
										<%-- <td align="center">
											<a onclick="removeRow('row<%=i+1%>','<%=i+1%>');" id="minus<%=i+1%>" <%if((i+1) == 1){ %>style="display:none"<%} %><%else if((VSEQ_NO.size() - 1) == i){ %><%}else{%>style="display:none"<%} %>>
												<i class="fa fa-minus-circle fa-2x"></i>
											</a>&nbsp;
											<a onclick="addrow();" id="plus<%=i+1%>" <%if((VSEQ_NO.size() - 1) == i){ %><%}else{%>style="display:none"<%} %>>
												<i class="fa fa-plus-circle fa-2x"></i>
											</a>
										</td> --%>
									</tr>
									<%} %>
								<%}else{ %>	
									<tr>
										<td colspan="6" align="center"><%=utilmsg.infoMessage("<b>Variable DCQ Not Configured!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>&nbsp;
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Variable DCQ Configuration
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
			      						onchange="validateDate(this);allowDCQLineEdit();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
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
			      						onchange="validateDate(this);allowDCQLineEdit();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<input type="hidden" name="temp_to_dt" value="">
	      						</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>DCQ (MMBTU)<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<input type="text" class="form-control form-control-sm" name="dcq" id="dcq" style="text-align:left;" value="" >
	      						</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Status</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<div class="input-group input-group-sm" >
			      						<div class="form-check form-switch">
											<input class="form-check-input" name="active" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked="checked" onclick="setActiveInactive(this);">
										  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb"></label>
										</div>
										<input type="hidden" name="status_flag" id="status_flag" value="Y">
		      						</div>
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

<input type="hidden" name="option" value="VARIABLE_DCQ">
<input type="hidden" name="seq_no" value="">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="cont_start_dt" value="<%=cont_start_dt%>">
<input type="hidden" name="cont_end_dt" value="<%=cont_end_dt%>">

<input type="hidden" name="item_size" id="item_size" value="<%=VSEQ_NO.size()%>">
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