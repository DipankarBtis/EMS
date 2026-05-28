<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function doSubmit()
{
	var msg = "";
	var flag = true;
	
	var i = 0;
	var index = 0;
	
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var tcq = document.forms[0].tcq.value;
	var dcq = document.forms[0].dcq.value;
	var remark = document.forms[0].remark.value;
	var seq_no = document.forms[0].seq_no.value;
	
	var line_start_date = document.forms[0].line_start_date;
	var line_end_date = document.forms[0].line_end_date;
	var line_seq_no = document.forms[0].line_seq_no;
	
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
	if(trim(tcq)=="")
	{
		msg += "Please Enter Assessed TCQ!\n";
		flag = false;
	}
	/*if(trim(dcq)=="")
	{
		msg += "Assessed DCQ Calculation Failed!\n";
		flag = false;
	}*/
	if(trim(remark)=='')
	{
		msg += "Please Enter Remark!\n";
		flag = false;
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
		msg += "TAQ timeline Duration already covered, Please check date range!\n";
		flag = false;
	}
	
	if(!allowTAQLineEdit())
	{
		// Incorrect Input. Hang Tight.
	}
	else if(flag)
	{
		var a = confirm("Do You Want To Submit TAQ Specification Details For Selected Deal?");
		
		if(a)
		{
			//document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function doModify(seq_no,from_dt,to_dt,tcq,dcq,remark)
{
	document.forms[0].from_dt.value=from_dt;
	document.forms[0].to_dt.value=to_dt;
	document.forms[0].temp_from_dt.value=from_dt;
	document.forms[0].temp_to_dt.value=to_dt;
	
	document.forms[0].seq_no.value=seq_no;
	document.forms[0].tcq.value=tcq;
	document.forms[0].dcq.value=dcq;
	document.forms[0].remark.value=remark;
	
	var yesdate=document.forms[0].yesdate.value; // SYSDATE -1
	var sysdate=document.forms[0].sysdate.value;
	
	var value_3 = compareDate(sysdate,from_dt);
	var value_33 = compareDate(yesdate,to_dt);
	
	if(value_3 == "1" &&  value_33 != "1") //comparing to dt with system date, iff from_dt < system date
	{
		document.forms[0].from_dt.readOnly=true;
		document.forms[0].from_dt.style.pointerEvents = "none";
	}
	else
	{
		document.forms[0].from_dt.readOnly=false;
		document.forms[0].from_dt.style.pointerEvents = "auto";
	}
	
	document.forms[0].opration.value="MODIFY";
}

function allowTAQLineEdit()
{	
	var yesdate=document.forms[0].yesdate.value; // SYSDATE -1
	var sysdate=document.forms[0].sysdate.value;
	var opration=document.forms[0].opration.value;
	
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var temp_from_dt = document.forms[0].temp_from_dt.value;
	var temp_to_dt = document.forms[0].temp_to_dt.value;
	
	var cont_start_dt = document.forms[0].cont_start_dt.value;
	var cont_end_dt = document.forms[0].cont_end_dt.value;
	
	//alert(sysdate+"-"+from_dt+"-"+to_dt+"-"+temp_from_dt+"-"+temp_to_dt);
	
	var flag=true;
	if(from_dt != "" && to_dt != "")
	{

		//let date1 = new Date("01/16/2024");
		//let date2 = new Date("01/26/2024");
		
		//let date1 = new Date(from_dt);
		//let date2 = new Date(to_dt);

		// Calculating the time difference
		// of two dates
		//let Difference_In_Time = date2.getTime() - date1.getTime();

		// Calculating the no. of days between
		// two dates
		//let Difference_In_Days =Math.round(Difference_In_Time / (1000 * 3600 * 24));

	    //alert(Difference_In_Days)
		
	    var val_dt = compareDate(sysdate,cont_end_dt);
		var val_dt_1 = compareDate(sysdate,from_dt);
		
		var value_0 = compareDate(from_dt,to_dt);
		var value_1 = compareDate(from_dt,cont_start_dt);
		var value_11 = compareDate(from_dt,cont_end_dt);
		var value_2 = compareDate(to_dt,cont_start_dt);
		var value_22 = compareDate(to_dt,cont_end_dt);
		var value_3 = compareDate(sysdate,temp_from_dt);
		var value_33 = compareDate(yesdate,to_dt); 
		
		if(val_dt == "1")
		{
			alert("Contract is expired! - TAQ Detail Can't be Configured!");
			flag=false;
		}
		else if(val_dt_1 == "1" && opration=="INSERT")
		{
			alert("TAQ timeline - From Date ("+from_dt+") < System date ("+sysdate+") not Allowed!"); 
			flag=false;
		}		
		else if(value_0 == "1") //comparing from and to date
		{
			alert("TAQ timeline From date ("+from_dt+") > To date ("+to_dt+")! \nThis is not Allowed!");
			flag=false;
		}
		else if(value_1 == "2" ||  value_11 == "1") //comparing from dt with contract date
		{
			alert("TAQ timeline ("+from_dt+" - "+to_dt+") should be with in range of Contract period ("+cont_start_dt+" - "+cont_end_dt+")!");
			flag=false;
		}
		else if(value_2 == "2" ||  value_22 == "1") //comparing to dt with contract date
		{
			alert("TAQ timeline ("+from_dt+" - "+to_dt+") should be with in range of Contract period ("+cont_start_dt+" - "+cont_end_dt+")!");
			flag=false;
		}
		else if(value_3 == "1" &&  value_33 == "1" && opration=="MODIFY") //comparing to dt with system date, iff from_dt < system date
		{
			alert("TAQ timeline To date ("+to_dt+") < System date - 1 ("+yesdate+")! \nThis is not Allowed!");
			flag=false;
		}
		else if(value_3 != "1" &&  val_dt_1 == "1" && opration=="MODIFY") //comparing from dt with system date
		{
			alert("TAQ timeline From date ("+from_dt+") < System date ("+sysdate+")! \nThis is not Allowed!");
			flag=false;
		}
	}
	return flag;
}

</script>
</head>

<jsp:useBean class="com.etrm.fms.market_risk.DataBean_VariablePricing" id="market_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String yesdate = utildate.getPreviousDate();
String sysdate = utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String cont_end_dt=request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String account=request.getParameter("account")==null?"":request.getParameter("account");

market_risk.setCallFlag("VARIABLE_TAQ_CONFIG");
market_risk.setCounterparty_cd(counterparty_cd);
market_risk.setComp_cd(owner_cd);
market_risk.setAgmt_no(agmt_no);
market_risk.setCont_no(cont_no);
market_risk.setContract_type(contract_type);
market_risk.init();

String counterparty_abbr=market_risk.getCounterparty_abbr();
String display_map_id = market_risk.getDisplay_map_id();

Vector VSTART_DT = market_risk.getVSTART_DT();
Vector VEND_DT = market_risk.getVEND_DT();
Vector VSEQ_NO = market_risk.getVSEQ_NO();

Vector VIS_RADIO_ENABLE = market_risk.getVIS_RADIO_ENABLE(); //JD

Vector VTCQ = market_risk.getVTCQ();
Vector VDCQ = market_risk.getVDCQ();
Vector VREMARK = market_risk.getVREMARK();

Vector VENTERED_BY = market_risk.getVENTERED_BY();
Vector VMODIFIED_BY = market_risk.getVMODIFIED_BY();
%>
<body>
<form method="post" action="../servlet/Frm_VariablePricing">

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
					    	TAQ Configuration <%=counterparty_abbr%> (<%=display_map_id%>) [Contract Period : <%=cont_start_dt %> - <%=cont_end_dt %>]
					    	<span
							<%if(account.equals("Buy")){ %>
								class="alert" style="background: #ffccff; color: #cc00cc;"
							<%}else { %>
								class="alert alert-primary"
							<%}%>><b><%=account%></b></span>
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th></th>										
										<th>From</th>
										<th>To</th>
										<th>Assessed TCQ</th>
										<th>Assessed DCQ</th>										
										<th>Remark</th>
										<th>Entered By</th>
										<th>Modified By</th>
									</tr>
								</thead>
								<tbody>
								<%if(VSEQ_NO.size()>0){ %>
									<%for(int i=0; i<VSEQ_NO.size(); i++){ %>
									<tr>
										<td align="center">
											<input type="radio" name="rd" 
											<%if (VIS_RADIO_ENABLE.elementAt(i).equals("N")){ %>
												disabled
											<%} %>
											onclick="doModify('<%=VSEQ_NO.elementAt(i)%>','<%=VSTART_DT.elementAt(i)%>','<%=VEND_DT.elementAt(i)%>',
													'<%=VTCQ.elementAt(i)%>','<%=VDCQ.elementAt(i)%>','<%=VREMARK.elementAt(i)%>');">&nbsp;&nbsp;
											<%=i+1%>
										</td>
										<td align="center">
											<input type="hidden" name="line_start_date" id="line_start_date<%=i%>" value="<%=VSTART_DT.elementAt(i)%>" >
											<input type="hidden" name="line_seq_no" id="line_seq_no<%=i%>" value="<%=VSEQ_NO.elementAt(i)%>" >
											<%=VSTART_DT.elementAt(i)%>
										</td>
										<td align="center">
											<input type="hidden" name="line_end_date" id="line_end_date<%=i%>" value="<%=VEND_DT.elementAt(i)%>" >
											<%=VEND_DT.elementAt(i)%>
										</td>																	
										<td align="right"><%=VTCQ.elementAt(i)%></td>
										<td align="right"><%=VDCQ.elementAt(i)%></td>								
										<td><%=VREMARK.elementAt(i)%></td>	
										<td align="center"><%=VENTERED_BY.elementAt(i)%></td>
										<td align="center"><%=VMODIFIED_BY.elementAt(i)%></td>																		
									</tr>
									<%}
								}else{%>
								<tr>
									<td colspan="8" align="center"><%=utilmsg.infoMessage("<b>No TAQ Detail Configured!</b>") %></td>
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
					    	TAQ Details
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
			      						onchange="validateDate(this);allowTAQLineEdit();" autocomplete="off">
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
			      						onchange="validateDate(this);allowTAQLineEdit();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<input type="hidden" name="temp_to_dt" value="">
	      						</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Assessed TCQ<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">				    			
			    					<input type="text" class="form-control form-control-sm" name="tcq" value="" onBlur="negNumber(this);checkNumber1(this,10,2);">				    			
				  				</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Assessed DCQ</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">				    			
			    					<input type="text" class="form-control form-control-sm" name="dcq" value="" readOnly onBlur="negNumber(this);checkNumber1(this,10,2);">				    			
				  				</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">	
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Remark<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<textarea class="form-control form-control-sm"  name="remark" rows="2" maxlength="150"></textarea>
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

<input type="hidden" name="option" value="VARIABLE_TAQ_CONFIG">
<input type="hidden" name="opration" value="INSERT">

<input type="hidden" name="yesdate" value="<%=yesdate%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="cont_start_dt" value="<%=cont_start_dt%>">
<input type="hidden" name="cont_end_dt" value="<%=cont_end_dt%>">
<input type="hidden" name="account" value="<%=account%>">
<input type="hidden" name="display_map_id" value="<%=display_map_id%>">

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