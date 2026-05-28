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
	//var month = document.forms[0].month.value;
	//var year  = document.forms[0].year.value;
	/* var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_map = document.forms[0].contract_map.value;*/
	var automationFlag = document.forms[0].automationFlag.value;
	var isGenerateXML = document.forms[0].isGenerateXML.value;
	
	/*var prev_counterparty_cd = document.forms[0].prev_counterparty_cd.value;
	if(prev_counterparty_cd!=counterparty_cd)
	{
		contract_map="";
	} */
	var execute_access = document.forms[0].execute_access.value;
		
	var u = document.forms[0].u.value;
	
	var url = "rpt_accrual_accounting.jsp?u="+u+"&report_dt="+report_dt;
	if(execute_access=="Y")
	{
		url+="&automationFlag="+automationFlag+"&isGenerateXML="+isGenerateXML;
	}			
	//"&month="+month+"&year="+year+
	//"&counterparty_cd="+counterparty_cd+"&contract_map="+contract_map
			
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

function accrualAutomation()
{
	var execute_access = document.forms[0].execute_access.value;
	
	if(execute_access=="Y")
	{
		document.forms[0].automationFlag.value="Y";
		var flag="N";
		if(document.forms[0].chk.checked)
		{
			var a = confirm("Confirm Generation of SAP XML ?");
			
			if(a)
			{
				flag= "Y";
			}
		}	
		document.forms[0].isGenerateXML.value=flag;
		refresh();
	}
	else
	{
		alert("You don't have Execute Access!");
	}
}
function exportToXls()
{
	var report_dt = document.forms[0].report_dt.value;
	/* var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_map = document.forms[0].contract_map.value;
	var automationFlag = document.forms[0].automationFlag.value;
	var isGenerateXML = document.forms[0].isGenerateXML.value; */
	
	var url = "xls_accrual_accounting.jsp?fileName= Accounting Sales Accrual Report.xls&report_dt="+report_dt;
		//"&counterparty_cd="+counterparty_cd+"&contract_map="+contract_map+"&automationFlag="+automationFlag+"&isGenerateXML="+isGenerateXML;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.accounting.DataBean_Accounting" id="accounting" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String date_num = "0"; 
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	date_num=temp[0];
}
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();
String report_dt=request.getParameter("report_dt")==null?sysdate:request.getParameter("report_dt");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_map=request.getParameter("contract_map")==null?"":request.getParameter("contract_map");
String automationFlag=request.getParameter("automationFlag")==null?"":request.getParameter("automationFlag");
String isGenerateXML=request.getParameter("isGenerateXML")==null?"":request.getParameter("isGenerateXML");

if(month.length() == 1)
{
	month="0"+month; 
}
accounting.setCallFlag("ACCRUAL_ACCOUNTING");
accounting.setComp_cd(owner_cd);
accounting.setReport_dt(report_dt);
accounting.setMonth(month);
accounting.setYear(year);
accounting.setCounterparty_cd(counterparty_cd);
accounting.setCont_mapp(contract_map);
accounting.setAutomation_flag(automationFlag);
accounting.setIsGenerateXML(isGenerateXML);
accounting.setRequest(request);
accounting.init();

String isFreezed=accounting.getIsFreezed();
String eodProcessDoneOn=accounting.getEodProcessDoneOn();

Vector VCOUNTERPTY_CD = accounting.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = accounting.getVCOUNTERPTY_ABBR();
Vector VCOUNTERPTY_NM = accounting.getVCOUNTERPTY_NM();
Vector VPRODUCTION_MONTH = accounting.getVPRODUCTION_MONTH();
Vector VSTART_DT = accounting.getVSTART_DT();
Vector VEND_DT = accounting.getVEND_DT();
Vector VPLANT_SEQ = accounting.getVPLANT_SEQ();
Vector VPLANT_ABBR = accounting.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = accounting.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = accounting.getVBU_PLANT_ABBR();
Vector VPERIOD_START_DT = accounting.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = accounting.getVPERIOD_END_DT();
Vector VDIS_CONT_MAPPING = accounting.getVDIS_CONT_MAPPING();
Vector VCONT_REF_NO = accounting.getVCONT_REF_NO();
Vector VBILLING_FREQ_NM = accounting.getVBILLING_FREQ_NM();
Vector VINVOICE_DUE_DT = accounting.getVINVOICE_DUE_DT();
Vector VACCRUAL_QTY = accounting.getVACCRUAL_QTY();
Vector VACCRUAL_AMT = accounting.getVACCRUAL_AMT();
Vector VMST_COUNTERPARTY_CD = accounting.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = accounting.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = accounting.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_MAP_LIST = accounting.getVCONT_MAP_LIST();
Vector VDIS_CONT_MAP_LIST = accounting.getVDIS_CONT_MAP_LIST();
Vector VSALES_PRICE_NM = accounting.getVSALES_PRICE_NM();
Vector VGROSS_AMT = accounting.getVGROSS_AMT();
Vector VEXCHNG_RATE = accounting.getVEXCHNG_RATE();

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
					    	Accounting Sales Accrual Report
					    </div>
					    <div>
					    	<div class="form-group row">
								
								<div class="col-auto">
				   	 				<span class="input-group-text">
									 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
									</span>	
								</div>
							</div> 					    
						</div>
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
						<%-- <div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>Production Period</b></label>
					  		</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
					  			<div class="col">
					  				<select class="form-select form-select-sm" name="month" onchange="refresh();">
										<option value="00" label="--All--">All</option>
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
					  				<select class="form-select form-select-sm" name="year" onchange="refresh();">
					  					<%for(int i=(currentYear+1); i > (currentYear-10);i--){ %>
											<option value="<%=i%>"><%=i%></option>
										<%} %>
									</select>
									<script>document.forms[0].year.value="<%=year%>"</script>
								</div>
							</div>
						</div> --%>
						<%-- <div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="">--All--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>				
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Contract</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="contract_map" onchange="refresh()">
										<option value="">--All--</option>
										<%for(int i=0;i<VDIS_CONT_MAP_LIST.size();i++){ %>
										<option value="<%=VCONT_MAP_LIST.elementAt(i)%>"><%=VDIS_CONT_MAP_LIST.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].contract_map.value="<%=contract_map%>"</script>
								</div>
							</div>
						</div>--%>
						<%if(execute_access.equals("Y")){ %>
						<div class="col-sm-6 col-xs-6 col-md-6"></div>
						<div class="col-sm-6 col-xs-6 col-md-6">  
							<div class="d-flex justify-content-end">
								<div class="form-group row">
									<div class="col-auto">
										<input type="checkbox" class="form-check-input" name="chk">
										<b>Generate SAP XML</b>		
									</div>								
									<div class="col-auto">						
										<i class="fa fa-play-circle-o fa-2x" style="color: red;" onclick="accrualAutomation();" title="Kick-off EoD Process"></i>
									</div>
								</div>	
							</div>	
						</div> 	
						<%} %>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr <%if(isFreezed.equals("Y")){ %>style="background: skyblue;color:black;"<%} %>>										
										<th>Sr#</th>
										<th>Customer
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Customer" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Contract No
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Contract/Trade Ref#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract_ref" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Contract Period</th>
										<th>Plant
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_plant" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div>										
										</th>
										<th>Business Unit
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_bu" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div>										
										</th>
										<th>Cash Flow
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_cash_flow" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div>										
										</th>
										<th>Production Month 
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_month" onkeyup="Search(this,'8');" placeholder="Search.." style="width:100px"/></div>										
										</th>
										<th>Billing Cycle</th>
										<th>Billing Period</th>
										<th>Invoice Due Date</th>
										<th>Accrual MMBTU</th>
										<th>Accrual Amount <br> (USD)</th>
										<th>Exchage Rate </th>
										<th>Accrual Amount <br> (INR)</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPTY_CD.size() > 0){ %>
									<%for(int i=0; i<VCOUNTERPTY_CD.size(); i++){ %>
									<tr>
										<td align="center" <%if(isFreezed.equals("Y")){ %>style="background: #99ffcc;"<%} %>><%=i+1 %></td>
										<td align="center" title="<%=VCOUNTERPTY_NM.elementAt(i)%>">
											<%=VCOUNTERPTY_ABBR.elementAt(i)%>
										</td>
										<td align="center">
											<%=VDIS_CONT_MAPPING.elementAt(i) %>
										</td>
										<td align="center">
											<%=VCONT_REF_NO.elementAt(i) %>
										</td>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
										<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
										<td align="center">Commodity</td>
										<td align="center"><%=VPRODUCTION_MONTH.elementAt(i) %></td>
										<td align="center">
											<span 
	    									<%if(VBILLING_FREQ_NM.elementAt(i).equals("1st-Fortnight")){ %>
	    										class="alert alert-info"
	    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("2nd-Fortnight")){ %>
	    										class="alert alert-warning"
	    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("1st-Weekly")){ %>
	    										class="alert" style="background:#eeccff;color: #660099;"
	    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("2nd-Weekly")){ %>
	    										class="alert alert-dark"
	    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("3rd-Weekly")){ %>
	    										class="alert alert-success"
	    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("4th-Weekly")){ %>
	    										class="alert alert-danger"
	    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("5th-Weekly")){ %>
	    										class="alert" style="background:#e6ccff;color:#330066;"
	    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("Monthly")){ %>
	    										class="alert alert-primary"
	    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("Other")){ %>
	    										class="alert" style="background:#b3ffb3;color: #008000;"
	    									<%} %>
	    									><b><%=VBILLING_FREQ_NM.elementAt(i)%></b></span>																								
										</td>
										<td align="center">
											<%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%>
										</td>
										<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
										<td align="right"><%=VACCRUAL_QTY.elementAt(i) %></td>
										<td align="right"><%if (VSALES_PRICE_NM.elementAt(i).equals("USD")){ %><%=VACCRUAL_AMT.elementAt(i)%><%}%></td>
										<td align="right"><%if (VSALES_PRICE_NM.elementAt(i).equals("USD") && VEXCHNG_RATE.elementAt(i).equals("0.00")){ %>
												<span style="color:red"><i class="fa fa-info-circle" aria-hidden="true"></i> Missing Exchange Rate</span>						
											<%} else {%><%=VEXCHNG_RATE.elementAt(i)%><%}%></td>
										<td align="right"><%if (!VACCRUAL_AMT.elementAt(i).equals("0.00") && VGROSS_AMT.elementAt(i).equals("0.00")){ %>
												<span style="color:red"><i class="fa fa-info-circle" aria-hidden="true"></i> Error</span>						
											<%} else {%><%=VGROSS_AMT.elementAt(i) %><%}%></td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="15"><%=utilmsg.infoMessage("<b>No Accrual Data Available!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<%if(isFreezed.equals("Y")){ %>
				<div class="modal-footer cdfooter">
					<div align="center"><%=utilmsg.infoMessage("<b>Accrual Data Freezed on "+eodProcessDoneOn+" for the Report Date "+report_dt+"</b>")%></div>					
				</div>
				<%} %>
			</div>
		</div>
	</div>
</div>			    

<input type="hidden" name="prev_counterparty_cd" value="<%=counterparty_cd%>">

<input type="hidden" name="automationFlag" value="N">
<input type="hidden" name="isGenerateXML" value="N">

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