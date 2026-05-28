<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var report_dt = document.forms[0].report_dt.value;
	var expo_type = document.forms[0].expo_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_exeed_credit.jsp?report_dt="+report_dt+"&expo_type="+expo_type+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function nextDate(day_no)
{
	var dt = document.forms[0].report_dt.value;
	if(dt!="")
	{
	   	var split = dt.split("/");
		var d_dt = split[0];
		var m_dt = split[1];
		var y_dt = split[2];
		
		var dt1 = new Date(y_dt+"-"+m_dt+"-"+d_dt);
		if(day_no == "-1")
		{
			dt1.setDate(dt1.getDate()-1);
		}
		else
		{
			dt1.setDate(dt1.getDate()+1);
		}
		var day = dt1.getDate();
		if(parseInt(day) < 10)
		{
			day="0"+day;
		}
		var month = dt1.getMonth()+1;
		var year = dt1.getFullYear();
		if(parseInt(month) < 10)
		{
			month="0"+month;
		}
		var to_dt= day+"/"+month+"/"+year;
		
		document.forms[0].report_dt.value=to_dt;
		
		refresh();
	}
}
function exportToXls()
{
	var report_dt = document.forms[0].report_dt.value;
	var expo_type = document.forms[0].expo_type.value;
	
	var url = "xls_exeed_credit.jsp?fileName=Credit Exceed Report "+report_dt+".xls&report_dt="+report_dt+"&expo_type="+expo_type;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DB_CR_ExposureTracking" id="credit_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String yesdate = utildate.getPreviousDate();

String report_dt=request.getParameter("report_dt")==null?yesdate:request.getParameter("report_dt");
String expo_type=request.getParameter("expo_type")==null?"R":request.getParameter("expo_type");

credit_risk.setCallFlag("CREDIT_EXCEED");
credit_risk.setReport_dt(report_dt);
credit_risk.setComp_cd(owner_cd);
credit_risk.setExpo_type(expo_type);
credit_risk.init();

Vector VACCOUNT = credit_risk.getVACCOUNT();
Vector VCOUNTERPARTY_CD = credit_risk.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = credit_risk.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = credit_risk.getVCOUNTERPARTY_ABBR();
Vector VAGMT_NO = credit_risk.getVAGMT_NO();
Vector VAGMT_REV_NO = credit_risk.getVAGMT_REV_NO();
Vector VCONT_NO = credit_risk.getVCONT_NO();
Vector VCONT_REV_NO = credit_risk.getVCONT_REV_NO();
Vector VDISPLAY_DEAL_MAP = credit_risk.getVDISPLAY_DEAL_MAP();
Vector VSIGNING_DT = credit_risk.getVSIGNING_DT();
Vector VSTART_DT = credit_risk.getVSTART_DT();
Vector VEND_DT = credit_risk.getVEND_DT();
Vector VPRICE_TYPE = credit_risk.getVPRICE_TYPE();
Vector VRATE = credit_risk.getVRATE();
Vector VRATE_UNIT = credit_risk.getVRATE_UNIT();
Vector VRATE_UNIT_NM = credit_risk.getVRATE_UNIT_NM();
Vector VTCQ = credit_risk.getVTCQ();
Vector VDCQ = credit_risk.getVDCQ();
Vector VCREDIT_EXCEED = credit_risk.getVCREDIT_EXCEED();
Vector VCREDIT_EXCEED_INFO = credit_risk.getVCREDIT_EXCEED_INFO();
Vector VCREDIT_EXCEED_USD = credit_risk.getVCREDIT_EXCEED_USD();
Vector VCREDIT_EXCEED_REASON = credit_risk.getVCREDIT_EXCEED_REASON();
Vector VCREDIT_EXCEED_REASON_FLAG = credit_risk.getVCREDIT_EXCEED_REASON_FLAG();


Vector VINDEX = credit_risk.getVINDEX();
Vector VEXPOSURE_HEADING = credit_risk.getVEXPOSURE_HEADING();
Vector VEXPOSURE_HEADING_FLAG = credit_risk.getVEXPOSURE_HEADING_FLAG();

String total_igx_cr_exceed = credit_risk.getTotal_igx_cr_exceed();
String total_igx_cr_exceed_usd = credit_risk.getTotal_igx_cr_exceed_usd();

String expo_type_nm="";
if(expo_type.equals("R"))
{
	expo_type_nm="Receivable";
}
else
{
	expo_type_nm="Payable";
}
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
					    	Credit Exceed Report
					    </div>
					    <div>
					    	<div class="d-flex justify-content-end">
							    <div class="btn-group">
									<select class="btn btn-outline-secondary btngrp btnactive" name="expo_type" onchange="refresh()">
										<!-- <option value="P">Payable</option> -->
										<option value="R">Receivable</option>
									</select>
								</div>&nbsp;
								<div onclick="exportToXls();" style="color:green;">
									<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
								</div>
							</div>
						</div>
						<script>
							document.forms[0].expo_type.value="<%=expo_type%>"
						</script>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>Report Date</b></label>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
					      					<span class="input-group-text" onclick="nextDate('-1');" title="click for Back Date"><i class="fa fa-backward fa-lg"></i></span>
						      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="report_dt" id="report_dt" value="<%=report_dt%>" maxLength="10" 
						      				onchange="validateDate(this);refresh();">
						      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      					<span class="input-group-text" onclick="nextDate('1');" title="click for Next Date"><i class="fa fa-forward fa-lg"></i></span>
					      				</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<%int i=0;
				for(int j=0; j < VEXPOSURE_HEADING.size(); j++) { 
					String expo_head_flag=""+VEXPOSURE_HEADING_FLAG.elementAt(j);
					int index=Integer.parseInt(""+VINDEX.elementAt(j));
				%>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;<%=VEXPOSURE_HEADING.elementAt(j) %></label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Account</th>										
										<th>Counterparty
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Counterparty" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Contract#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Signing Date</th>
										<th>Contract Period</th>
										<th>Price Type</th>
										<th>Price Rate</th>
										<th>Rate Unit</th>
										<th>Credit Exceed (INR)</th>
										<th>Credit Exceed (USD)</th>
										<th>Exceed Reason</th>
									</tr>
								</thead>
								<tbody>
								<%if(index>0){ 
									int k=0;
									int sr=0;
								%>
									<%for(i=i; i<VCOUNTERPARTY_CD.size(); i++){ 
										k=k+1;
										if (VCREDIT_EXCEED_REASON_FLAG.elementAt(i).equals("Y"))
										{sr+=1;
										%>
											<tr>
												<td align="center"><%=sr%></td>
												<td align="center"><%=VACCOUNT.elementAt(i)%></td>
												<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
												<td align="center"><%=VDISPLAY_DEAL_MAP.elementAt(i)%></td>
												<td align="center"><%=VSIGNING_DT.elementAt(i)%></td>
												<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
												<td align="center"><%=VPRICE_TYPE.elementAt(i) %></td>
												<td align="right"><%=VRATE.elementAt(i)%></td>
												<td align="center"><%=VRATE_UNIT_NM.elementAt(i)%></td>
												<td align="right" <%-- title= "<%=VCREDIT_EXCEED_INFO.elementAt(i)%>" --%>><%=VCREDIT_EXCEED.elementAt(i)%></td>
												<td align="right"><%=VCREDIT_EXCEED_USD.elementAt(i)%></td>
												<td><%=VCREDIT_EXCEED_REASON.elementAt(i) %></td>
											</tr>
										<%}
										if(k==index){%>
											<%if(expo_head_flag.equals("I")){%>
											<tr style="font-weight: bold;">
												<td colspan="9" align="right"><b>IGX Summary</b></td>
												<td align="right"><%=total_igx_cr_exceed %></td>
												<td align="right"><%=total_igx_cr_exceed_usd%></td>
												<td align="right"></td>
											</tr>
											<%} %>
											<%i=i+1;
											break;
									 	}%>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="12">
											<div align="center"><%=utilmsg.infoMessage("<b>No Exposure for the Report Date!</b>")%></div>
										</td>
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
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example");
  	
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