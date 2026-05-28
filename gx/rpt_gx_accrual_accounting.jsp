<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var report_dt = document.forms[0].report_dt;
	//var month = document.forms[0].month.value;
	//var year  = document.forms[0].year.value;
	//var counterparty_cd = document.forms[0].counterparty_cd.value;
	//var contract_map = document.forms[0].contract_map.value;
	var automationFlag = document.forms[0].automationFlag.value;
	var isGenerateXML = document.forms[0].isGenerateXML.value;
	var execute_access = document.forms[0].execute_access.value;
	/*
	var prev_counterparty_cd = document.forms[0].prev_counterparty_cd.value;
	if(prev_counterparty_cd!=counterparty_cd)
	{
		contract_map="";
	}*/
	
	var u = document.forms[0].u.value;
	var sysdate = document.forms[0].sysdate.value;
	var prev_report_dt = document.forms[0].prev_report_dt.value;
	
	var tmp=report_dt.value.split("/");
	var tmp1=sysdate.split("/");
	var temp_report_dt = new Date(tmp[1]+"/"+tmp[0]+"/"+tmp[2]);
 	var temp_sysdate = new Date(tmp1[1]+"/"+tmp1[0]+"/"+tmp1[2]);
	
	var flag=true;
	if(temp_report_dt>temp_sysdate)
	{
		flag=false;
	}
	
	if(flag==true)
	{
		var url = "rpt_gx_accrual_accounting.jsp?u="+u+"&report_dt="+report_dt.value;
		if(execute_access=="Y")
		{
			url+="&automationFlag="+automationFlag+"&isGenerateXML="+isGenerateXML;
		}
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert("Report Date can't exceed System Date ("+sysdate+")!");
		setTimeout(function () {
		        report_dt.value = prev_report_dt;
		}, 0);
		return false;
	}
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
	
	var url = "xls_gx_accrual_accounting.jsp?report_dt="+report_dt;
	
	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.gx.DataBean_Gx_Invoice" id="gx" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String temp_sysdate = utildate.getSysdate();
String sysdate = utildate.getPreviousDate();

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
gx.setCallFlag("GX_ACCRUAL_ACCOUNTING");
gx.setComp_cd(owner_cd);
gx.setReport_dt(report_dt);
//gx.setMonth(month);
//gx.setYear(year);
gx.setCounterparty_cd(counterparty_cd);
gx.setCont_mapp(contract_map);
gx.setAutomation_flag(automationFlag);
gx.setIsGenerateXML(isGenerateXML);
gx.setRequest(request);
gx.init();

String isFreezed=gx.getIsFreezed();
String eodProcessDoneOn=gx.getEodProcessDoneOn();

Vector VCOUNTERPTY_CD = gx.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = gx.getVCOUNTERPTY_ABBR();
Vector VCOUNTERPTY_NM = gx.getVCOUNTERPTY_NM();
Vector VPRODUCTION_MONTH = gx.getVPRODUCTION_MONTH();
Vector VSTART_DT = gx.getVSTART_DT();
Vector VEND_DT = gx.getVEND_DT();
Vector VGX_BU_PLANT_SEQ = gx.getVGX_BU_PLANT_SEQ();
Vector VGX_BU_PLANT_ABBR = gx.getVGX_BU_PLANT_ABBR();
Vector VBU_PLANT_SEQ = gx.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = gx.getVBU_PLANT_ABBR();
Vector VDIS_CONT_MAPPING = gx.getVDIS_CONT_MAPPING();
Vector VCONT_REF_NO = gx.getVCONT_REF_NO();
Vector VINVOICE_DUE_DT = gx.getVINVOICE_DUE_DT();
Vector VACCRUAL_QTY = gx.getVACCRUAL_QTY();
Vector VACCRUAL_AMT = gx.getVACCRUAL_AMT();
Vector VMST_COUNTERPARTY_CD = gx.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = gx.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = gx.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_MAP_LIST = gx.getVCONT_MAP_LIST();
Vector VDIS_CONT_MAP_LIST = gx.getVDIS_CONT_MAP_LIST();
Vector VGROSS_AMT = gx.getVGROSS_AMT();
Vector VGX_COUNTERPTY_ABBR = gx.getVGX_COUNTERPTY_ABBR();
Vector VGX_COUNTERPTY_CD = gx.getVGX_COUNTERPTY_CD();
Vector VBUY_SELL = gx.getVBUY_SELL();
Vector VCONTRACT_TYPE_NM = gx.getVCONTRACT_TYPE_NM();

Vector VTAX_STRUCT_DTL = gx.getVTAX_STRUCT_DTL();
Vector VTAX_AMT = gx.getVTAX_AMT();
Vector VTAX_INFO = gx.getVTAX_INFO();
Vector VTOTAL_ACCRUAL_AMT = gx.getVTOTAL_ACCRUAL_AMT();

String str_tot_accrual_mmbtu = gx.getStr_tot_accrual_mmbtu();
String str_tot_accrual_amt = gx.getStr_tot_accrual_amt();
String str_accrual_tax_amt = gx.getStr_accrual_tax_amt();
String str_total_accrual_amt = gx.getStr_total_accrual_amt();

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
					    	Transaction Charge Accrual Report
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
										<th>On behalf of Counterparty
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Customer" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Gas Exchange</th>
										<th>Buy/Sell
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_buy_sell" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>										
										</th>
										<th>Contract Type
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract_type" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Contract No
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Contract/Trade Ref#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Contract Period</th>
										<th>Gx Business Unit
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_plant" onkeyup="Search(this,'8');" placeholder="Search.." style="width:100px"/></div>										
										</th>
										<th>Business Unit
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_bu" onkeyup="Search(this,'9');" placeholder="Search.." style="width:100px"/></div>										
										</th>
										<th>Cash Flow
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_cash_flow" onkeyup="Search(this,'10');" placeholder="Search.." style="width:100px"/></div>										
										</th>
										<th>Production Month 
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_month" onkeyup="Search(this,'11');" placeholder="Search.." style="width:100px"/></div>										
										</th>
										<th>Invoice Due Date</th>
										<th>Accrual MMBTU</th>
										<th>Gross Amount <br> (INR)</th>
										<th>Tax Structure</th>
										<th>Tax Amount <br> (INR)</th>
										<th>Accrual Amount <br> (INR)</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPTY_CD.size() > 0){ %>
									<%for(int i=0; i<VCOUNTERPTY_CD.size(); i++){ %>
									<tr>
										<td align="center"  <%if(isFreezed.equals("Y")){ %>style="background: #99ffcc;"<%} %>><%=i+1 %></td>
										<td align="center" title="<%=VCOUNTERPTY_NM.elementAt(i)%>">
											<%=VCOUNTERPTY_ABBR.elementAt(i)%>
										</td>
										<td align="center"><%=VGX_COUNTERPTY_ABBR.elementAt(i)%></td>
										<td align="center"><%=VBUY_SELL.elementAt(i)%></td>
										<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i)%></td>
										<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i) %>	</td>
										<td align="center"><%=VCONT_REF_NO.elementAt(i)%></td>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										<td align="center"><%=VGX_BU_PLANT_ABBR.elementAt(i)%></td>
										<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
										<td align="center">Service Charge</td>
										<td align="center"><%=VPRODUCTION_MONTH.elementAt(i) %></td>										
										<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
										<td align="right"><%=VACCRUAL_QTY.elementAt(i) %></td>
										<td align="right"><%=VACCRUAL_AMT.elementAt(i)%></td>
										<td align="left"><%=VTAX_STRUCT_DTL.elementAt(i) %></td>
										<td align="right" title="<%=VTAX_INFO.elementAt(i)%>"><%=VTAX_AMT.elementAt(i) %></td>
										<td align="right"><%=VTOTAL_ACCRUAL_AMT.elementAt(i) %></td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="18"><%=utilmsg.infoMessage("<b>No Accrual Data Available!</b>") %></td>
									</tr>
								<%} %>
									<tr style="font-weight: bold;">
										<td colspan="13" align="right">Total : </td>
										<td align="right"><%=str_tot_accrual_mmbtu %></td>
										<td align="right"><%=str_tot_accrual_amt %></td>
										<td align="right"><%//=str_tot_accrual_mmbtu %></td>
										<td align="right"><%=str_accrual_tax_amt %></td>
										<td align="right"><%=str_total_accrual_amt%></td>
									</tr>
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
<input type="hidden" name="sysdate" value="<%=temp_sysdate%>">
<input type="hidden" name="prev_report_dt" value="<%=report_dt%>">

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