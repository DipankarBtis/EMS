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
	var automationFlag = document.forms[0].automationFlag.value;
	var isGenerateXML = document.forms[0].isGenerateXML.value;
	
	var execute_access = document.forms[0].execute_access.value;
		
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
		var url = "rpt_derv_accrual_accounting.jsp?u="+u+"&report_dt="+report_dt.value;
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
	
	var url = "xls_derv_accrual_accounting.jsp?fileName= Derivatives Accrual Report.xls&report_dt="+report_dt;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Invoice" id="derv_accounting" scope="request"></jsp:useBean>
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
derv_accounting.setCallFlag("DERV_ACCRUAL_ACCOUNTING");
derv_accounting.setComp_cd(owner_cd);
derv_accounting.setReport_dt(report_dt);
derv_accounting.setMonth(month);
derv_accounting.setYear(year);
derv_accounting.setCounterparty_cd(counterparty_cd);
derv_accounting.setCont_mapp(contract_map);
derv_accounting.setAutomation_flag(automationFlag);
derv_accounting.setIsGenerateXML(isGenerateXML);
derv_accounting.setRequest(request);
derv_accounting.init();

String isFreezed=derv_accounting.getIsFreezed();
String eodProcessDoneOn=derv_accounting.getEodProcessDoneOn();

String tot_qty=derv_accounting.getTot_qty();
String tot_gross_amt =derv_accounting.getTot_gross_amt();
String tot_buy_amt=derv_accounting.getTot_buy_amt();
String tot_sell_amt=derv_accounting.getTot_sell_amt();

Vector VCOUNTERPTY_CD = derv_accounting.getVCOUNTERPARTY_CD();
Vector VCOUNTERPTY_ABBR = derv_accounting.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPTY_NM = derv_accounting.getVCOUNTERPARTY_NM();
Vector VPRODUCTION_MONTH = derv_accounting.getVPRODUCTION_MONTH();
Vector VSTART_DT = derv_accounting.getVCONT_START_DT();
Vector VEND_DT = derv_accounting.getVCONT_END_DT();
Vector VPLANT_SEQ = derv_accounting.getVPLANT_SEQ();
Vector VPLANT_ABBR = derv_accounting.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = derv_accounting.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = derv_accounting.getVBU_PLANT_ABBR();
Vector VPERIOD_START_DT = derv_accounting.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = derv_accounting.getVPERIOD_END_DT();
Vector VDEAL_MAPPING = derv_accounting.getVDEAL_MAPPING();
Vector VCONT_REF = derv_accounting.getVCONT_REF();
Vector VBILLING_FREQ = derv_accounting.getVBILLING_FREQ();
Vector VINVOICE_DUE_DT = derv_accounting.getVINVOICE_DUE_DT();
Vector VACCRUAL_QTY = derv_accounting.getVACCRUAL_QTY();
Vector VACCRUAL_AMT = derv_accounting.getVACCRUAL_AMT();
Vector VMST_COUNTERPARTY_CD = derv_accounting.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = derv_accounting.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = derv_accounting.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_MAP_LIST = derv_accounting.getVCONT_MAP_LIST();
Vector VDIS_CONT_MAP_LIST = derv_accounting.getVDIS_CONT_MAP_LIST();
Vector VSELL_PRICE_NM = derv_accounting.getVSELL_PRICE_NM();
Vector VGROSS_AMT = derv_accounting.getVGROSS_AMT();
Vector VEXCHNG_RATE = derv_accounting.getVEXCHNG_RATE();
Vector VCASH_FLOW = derv_accounting.getVCASH_FLOW();
Vector VQTY_UNIT = derv_accounting.getVQTY_UNIT();
Vector VBUY_AMT = derv_accounting.getVBUY_AMT();
Vector VSELL_AMT = derv_accounting.getVSELL_AMT();
Vector VDEAL_PROD_NM = derv_accounting.getVDEAL_PROD_NM();
Vector VBUY_SELL = derv_accounting.getVBUY_SELL();
Vector VFLOAT_PRICE = derv_accounting.getVFLOAT_PRICE();
Vector VFIXED_PRICE = derv_accounting.getVFIXED_PRICE();
Vector VMONTH = derv_accounting.getVMONTH();
Vector VYEAR = derv_accounting.getVYEAR();

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
					    	Derivatives Accrual Report
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
									<div class="col">
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
										<th>Trader
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Customer" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Contract No
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Contract/Trade Ref#
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract_ref" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>BUY/SELL
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="buy_sell" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Product
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="product" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Month
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="month" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Year
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="year" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Plant
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
										<th>Fixed Price</th>
										<th>Float Price</th>
										<th>Accrual Qty</th>
										<th>Qty Unit</th>
										<th>Buy Amount</th>
										<th>Sell Amount</th>
										<th>Accrual Amount <br> (USD)</th>
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
											<%=VDEAL_MAPPING.elementAt(i) %>
										</td>
										<td align="center">
											<%=VCONT_REF.elementAt(i) %>
										</td>
										<td align="center"><%=VBUY_SELL.elementAt(i) %></td>
										<td align="center"><%=VDEAL_PROD_NM.elementAt(i) %></td>
										<td align="center"><%=VMONTH.elementAt(i) %></td>
										<td align="center"><%=VYEAR.elementAt(i) %></td>
										<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
										<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
										<td align="center"><%=VCASH_FLOW.elementAt(i)%></td>
										<td align="center"><%=VPRODUCTION_MONTH.elementAt(i) %></td>
										<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
										<td align="right"><%=VFIXED_PRICE.elementAt(i) %></td>
										<td align="right"><%=VFLOAT_PRICE.elementAt(i) %></td>
										<td align="right"><%=VACCRUAL_QTY.elementAt(i) %></td>
										<td align="right"><%=VQTY_UNIT.elementAt(i) %></td>
										<td align="right"><%=VBUY_AMT.elementAt(i) %></td>
										<td align="right"><%=VSELL_AMT.elementAt(i) %></td>
										<td align="right" title="<%if(Double.parseDouble(""+VGROSS_AMT.elementAt(i))<0){%>Payable<%}else{%>Receivable<%}%>">
											<span style="color:<%if(Double.parseDouble(""+VGROSS_AMT.elementAt(i))<0){%>red<%}else{%>green<%}%>">
												<%=VGROSS_AMT.elementAt(i)%>
											</span>
										</td>
									</tr>
									<%} %>
									<tr>
										<td colspan="15" align="right"><b>Total:</b></td>
										<td align="right"><b><%=tot_qty %></b></td>
										<td></td>
										<td align="right"><b><%=tot_buy_amt %></b></td>
										<td align="right"><b><%=tot_sell_amt %></b></td>
										<td align="right"><b>
											<span style="color:<%if(Double.parseDouble(tot_gross_amt)<0){%>red<%}else{%>green<%}%>">
												<%=tot_gross_amt%>
											</span></b>
										</td>
									</tr>
								<%}else{ %>
									<tr>
										<td align="center" colspan="20"><%=utilmsg.infoMessage("<b>No Accrual Data Available!</b>") %></td>
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