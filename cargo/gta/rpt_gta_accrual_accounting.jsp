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
	var automationFlag = document.forms[0].automationFlag.value;
	var isGenerateXML = document.forms[0].isGenerateXML.value;
	var execute_access = document.forms[0].execute_access.value;
	var u = document.forms[0].u.value;
	
	var url = "rpt_gta_accrual_accounting.jsp?u="+u+"&report_dt="+report_dt;
	if(execute_access=="Y")
	{
		url+="&automationFlag="+automationFlag+"&isGenerateXML="+isGenerateXML;
	}	
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
	var url = "xls_gta_accrual_accounting.jsp?fileName= Accounting GTA Accrual Report.xls&report_dt="+report_dt;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GTA_Remittance" id="gta" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String report_dt=request.getParameter("report_dt")==null?sysdate:request.getParameter("report_dt");
String automationFlag=request.getParameter("automationFlag")==null?"":request.getParameter("automationFlag");
String isGenerateXML=request.getParameter("isGenerateXML")==null?"":request.getParameter("isGenerateXML");


gta.setCallFlag("GTA_ACCRUAL_ACCOUNTING");
gta.setComp_cd(owner_cd);
gta.setReport_dt(report_dt);
gta.setAutomation_flag(automationFlag);
gta.setIsGenerateXML(isGenerateXML);
gta.setRequest(request);
gta.init();

String isFreezed=gta.getIsFreezed();
String eodProcessDoneOn=gta.getEodProcessDoneOn();

Vector VCOUNTERPTY_CD = gta.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_ABBR = gta.getVCOUNTERPTY_ABBR();
Vector VCOUNTERPTY_NM = gta.getVCOUNTERPTY_NM();
Vector VPRODUCTION_MONTH = gta.getVPRODUCTION_MONTH();
Vector VSTART_DT = gta.getVSTART_DT();
Vector VEND_DT = gta.getVEND_DT();
Vector VTRANS_BU_SEQ = gta.getVTRANS_BU_SEQ();
Vector VTRANS_BU_ABBR = gta.getVTRANS_BU_ABBR();
Vector VBU_PLANT_SEQ = gta.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = gta.getVBU_PLANT_ABBR();
Vector VPERIOD_START_DT = gta.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = gta.getVPERIOD_END_DT();
Vector VDIS_CONT_MAPPING = gta.getVDIS_CONT_MAPPING();
Vector VCONT_REF_NO = gta.getVCONT_REF_NO();
Vector VBILLING_FREQ_NM = gta.getVBILLING_FREQ_NM();
Vector VINVOICE_DUE_DT = gta.getVINVOICE_DUE_DT();
Vector VACCRUAL_QTY = gta.getVACCRUAL_QTY();
Vector VACCRUAL_AMT = gta.getVACCRUAL_AMT();
Vector VCASH_FLOW_NM = gta.getVCASH_FLOW_NM();
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
					    	Accounting GTA Accrual Report
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
										<i class="fa fa-play-circle-o fa-2x" style="color: red;" onclick="accrualAutomation();" title="Kick-off Automation"></i>
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
										<th>Transporter
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
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_month" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div>										
										</th>
										<th>Billing Cycle</th>
										<th>Billing Period</th>
										<th>Invoice Due Date</th>
										<th>Accrual MMBTU</th>										
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
										<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i) %>										
																					
										</td>
										<td align="center">
											<%=VCONT_REF_NO.elementAt(i) %>
										</td>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										<td align="center"><%=VTRANS_BU_ABBR.elementAt(i)%></td>
										<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
										<td align="center"><%=VCASH_FLOW_NM.elementAt(i)%></td>
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
										<td align="right"><%=VACCRUAL_AMT.elementAt(i)%></td>									
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="14"><%=utilmsg.infoMessage("<b>No Accrual Data Available!</b>") %></td>
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