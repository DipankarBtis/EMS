<%@page import="java.util.Vector"%>
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
	
	var url = "rpt_mr_exposure.jsp?report_dt="+report_dt+"&expo_type="+expo_type+
		"&u="+u;

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

var newWindow;
function showDealExposure(legalEntity,counterparty_cd, account, contract_type, agmt_no, agmt_rev_no, cont_no, cont_rev_no,cargo_no)
{
	var report_dt = document.forms[0].report_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_mr_deal_dtl.jsp?report_dt="+report_dt+"&counterparty_cd="+counterparty_cd+"&legalEntity="+legalEntity+
		"&account="+account+"&contract_type="+contract_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+	
		"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&cargo_no="+cargo_no+
		"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"MR Exposure Detail","top=10,left=10,width=1200,height=1200,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"MR Exposure Detail","top=10,left=10,width=1200,height=1200,scrollbars=1");
	}
}


function showFreezedDealExposure(legalEntity,counterparty_cd, account, contract_type, agmt_no, agmt_rev_no, cont_no, cont_rev_no,cargo_no)
{
	var report_dt = document.forms[0].report_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_mr_deal_dtl_freezed.jsp?report_dt="+report_dt+"&counterparty_cd="+counterparty_cd+"&legalEntity="+legalEntity+
		"&account="+account+"&contract_type="+contract_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+	
		"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&cargo_no="+cargo_no+
		"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"MR Exposure Freez Detail","top=10,left=10,width=1200,height=1200,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"MR Exposure Freez Detail","top=10,left=10,width=1200,height=1200,scrollbars=1");
	}
}


function exposureEoDAutomation()
{
	var report_dt = document.forms[0].report_dt.value;
	
	var temp_report_dt = document.forms[0].temp_report_dt.value;
	var value = compareDate(temp_report_dt,report_dt);
	
	var u = document.forms[0].u.value;
	var write_access = document.forms[0].write_access.value;

	if(write_access=="N")
	{
		alert("You don't have write access to Freeze EoD Exposure!")
	}
	else if(value == "2")
	{
		alert("Zema pricing not available for "+report_dt+" to Freeze EoD Exposure!")
	}
	else
	{
		var a = confirm("You are about overwrite Freezed EoD Exposure dated "+report_dt+".\n Do you want to Proceed? ")
		if(a)
		{
			var url = "rpt_mr_exposure_eod.jsp?report_dt="+report_dt+"&u="+u;
		
			document.getElementById("loading").style.visibility = "visible";
			location.replace(url);
		}
	}
}


function exportToXls()
{
	var report_dt = document.forms[0].report_dt.value;
	var isEodDone = document.forms[0].isEodDone.value;
	
	var u = document.forms[0].u.value;
	
	if(isEodDone=="true")
	{	
		var url = "rpt_mr_exposure_eod_csv.jsp?report_dt="+report_dt+"&u="+u;
	
		//document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert("EoD Exposure not Freezed for "+report_dt+"!")
	}	
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DB_MR_ExposureReport" id="market_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String yesdate = utildate.getPreviousDate();
String sysdate = utildate.getSysdate();

String report_dt=request.getParameter("report_dt")==null?yesdate:request.getParameter("report_dt");
String expo_type=request.getParameter("expo_type")==null?"":request.getParameter("expo_type");

market_risk.setCallFlag("EXPOSURE_CONTRACT_LIST");
market_risk.setReport_dt(report_dt);
market_risk.setComp_cd(owner_cd);
market_risk.setExpo_type(expo_type);
market_risk.init();

Vector VLEGAL_ENTITY_CD = market_risk.getVLEGAL_ENTITY_CD();
Vector VLEGAL_ENTITY = market_risk.getVLEGAL_ENTITY();
Vector VCOUNTERPARTY_CD = market_risk.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = market_risk.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = market_risk.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = market_risk.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = market_risk.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = market_risk.getVMST_COUNTERPARTY_ABBR();
Vector VCONTRACT_TYPE = market_risk.getVCONTRACT_TYPE();
Vector VDIS_CONTRACT_TYPE = market_risk.getVDIS_CONTRACT_TYPE();
Vector VCONT_NO = market_risk.getVCONT_NO();
Vector VCONT_REV_NO = market_risk.getVCONT_REV_NO();
Vector VAGMT_NO = market_risk.getVAGMT_NO();
Vector VAGMT_REV_NO = market_risk.getVAGMT_REV_NO();
Vector VCARGO_NO = market_risk.getVCARGO_NO();
Vector VDISPLAY_DEAL_MAP = market_risk.getVDISPLAY_DEAL_MAP();
Vector VCONT_REF_NO = market_risk.getVCONT_REF_NO();
Vector VSIGNING_DT = market_risk.getVSIGNING_DT();
Vector VSTART_DT = market_risk.getVSTART_DT();
Vector VEND_DT = market_risk.getVEND_DT();
Vector VACCOUNT = market_risk.getVACCOUNT();
Vector VRATE = market_risk.getVRATE();
Vector VPRICE_TYPE = market_risk.getVPRICE_TYPE();
Vector VFIN_CURVE_NM = market_risk.getVFIN_CURVE_NM();
Vector VPHYS_CURVE_NM = market_risk.getVPHYS_CURVE_NM();
Vector VCONT_STATUS = market_risk.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = market_risk.getVCONT_STATUS_FLG();
Vector VPRICE_LINE_RATE = market_risk.getVPRICE_LINE_RATE();
Vector VIS_EOD_PROCESS_DONE = market_risk.getVIS_EOD_PROCESS_DONE();

Vector VSTORAGE_ROW_HEADING = market_risk.getVSTORAGE_ROW_HEADING();
Vector VSTORAGE_MMSCM = market_risk.getVSTORAGE_MMSCM();
Vector VSTORAGE_MMBTU = market_risk.getVSTORAGE_MMBTU();
Vector VSTORAGE_LAST_DT = market_risk.getVSTORAGE_LAST_DT();

Vector VCOMPANY_CD = market_risk.getVCOMPANY_CD();
Vector VCOMPANY_ABBR = market_risk.getVCOMPANY_ABBR();
Vector VCOMPANY_NAME = market_risk.getVCOMPANY_NAME();

String storage_collapse_info=market_risk.getStorage_collapse_info();
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
					    	Deals Overview
					    </div>
					    <div class="btn-group">
							<select class="btn btn-outline-secondary btngrp btnactive" name="expo_type" onchange="refresh()">
								<option value="">All</option>
								<option value="P">Payable</option>
								<option value="R">Receivable</option>
							</select>
						</div>
						<script>
							document.forms[0].expo_type.value="<%=expo_type%>"
						</script>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-10 col-xs-10 col-md-10">
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
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="d-flex justify-content-end">
								<div class="form-group row">															
									<div class="col-auto">	
										<i class="fa fa-play-circle-o fa-2x" style="color: red;" onclick="exposureEoDAutomation();" title="Freeze EoD Exposure"></i>
									</div>
									<div class="col-auto">
										<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();" title="Export Freezed EoD Exposure"></i>
									</div>
								</div>	
							</div>	
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="col-md-12 col-sm-12 col-xs-12">
						<div class="accordion">
							<div class="accordion-item accor_item">
								<h2 class="accordion-header" id="tank_collapse">
  									<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse" aria-expanded="false" aria-controls="collapse">
						    			Storage Tank <%=storage_collapse_info%> 
						      		</button>	
						    	</h2>
								<div id="collapse" class="accordion-collapse collapse" aria-labelledby="tank_collapse">
						      		<div class="accordion-body accor-body">
										<div class="row">
											<div class="table-responsive">
												<table class="table table-bordered table-hover">
													<thead>
														<tr>
															<th rowspan="2"></th>
															<%for(int i=0;i<VCOMPANY_ABBR.size();i++){ %>
															<th colspan="3"><%=VCOMPANY_ABBR.elementAt(i)%></th>
															<%} %>
														</tr>
														<tr>
															<%for(int i=0;i<VCOMPANY_ABBR.size();i++){ %>										
															<th>MMSCM</th>
															<th>MMBTU</th>
															<th>Remark</th>
															<%} %>
														</tr>
													</thead>	
													<tbody>
													<%for(int i=0;i<VSTORAGE_ROW_HEADING.size();i++){ %>
														<tr <%if((i+1) == VSTORAGE_ROW_HEADING.size()){%>style="font-weight: bold;"<%}%>>	
															<td><%=VSTORAGE_ROW_HEADING.elementAt(i)%></td>
															<%for(int j=0;j<VCOMPANY_ABBR.size();j++){ 
															int k=0;
															if(j>0)
															{
																int size = VSTORAGE_ROW_HEADING.size();
																k= (size*j)+i;	
															}
															else
															{
																k=i;
															}
															%>
															<td align="right"><%=VSTORAGE_MMSCM.elementAt(k)%></td>
															<td align="right"><%=VSTORAGE_MMBTU.elementAt(k)%></td>
															<td><%=VSTORAGE_LAST_DT.elementAt(k)%></td>
															<%} %>
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
					</div>
				</div>	
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="filterbysearch">
								<thead id="tbsearch">
									<tr>
										<th>Sr#</th>
										<th title="Freezed Deal Detail"></th>
										<th title="System Deal Detail"></th>
										<th>Legal Entity</th>
										<th>Account</th>										
										<th>Counterparty</th>
										<th>Contract Type</th>
										<th>Contract#</th>
										<th>Contract/Trade Ref#</th>
										<th>Contract Status</th>
										<th>Contract Period</th>
										<th>Price Type</th>
										<th>Price($/MMBTU)</th>										
										<th>Financial Curve</th>
										<th>Physical Curve</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPARTY_CD.size()>0){ %>
									<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
									<tr>
										<td align="center">
											<%=i+1%>
										</td>	
										<td align="center" <%if(VIS_EOD_PROCESS_DONE.elementAt(i).equals("Y")){ %> style="background: #99ffcc;" <%} %>>
											<i class="fa fa-floppy-o fa-lg"  
											onclick="showFreezedDealExposure('<%=VLEGAL_ENTITY_CD.elementAt(i)%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>',
													'<%=VACCOUNT.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
													'<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>',
													'<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VCARGO_NO.elementAt(i)%>');"
											style="<%if(!VIS_EOD_PROCESS_DONE.elementAt(i).equals("Y")){ %>
													pointer-events: none; opacity: .65; color: gray;																								<%}%>"		
											></i>
										</td>
										<td align="center" style="color: #008080;">
											<i class="fa fa-cogs fa-lg"  
											onclick="showDealExposure('<%=VLEGAL_ENTITY_CD.elementAt(i)%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>',
													'<%=VACCOUNT.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
													'<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>',
													'<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VCARGO_NO.elementAt(i)%>');"
											></i>
										</td>
										<td align="center">
											<%=VLEGAL_ENTITY.elementAt(i)%>
										</td>
										<td align="center">
											<span
											<%if(VACCOUNT.elementAt(i).equals("Buy")){ %>
	    										class="alert" style="background: #ffccff; color: #cc00cc;"
	    									<%}else { %>
	    										class="alert alert-primary"
	    									<%}%>><b><%=VACCOUNT.elementAt(i)%></b></span>
		    							</td>
										<td title="<%=VCOUNTERPARTY_NM.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
										<td><%=VDIS_CONTRACT_TYPE.elementAt(i)%> </td>
										<td align="center"><%=VDISPLAY_DEAL_MAP.elementAt(i)%></td>
										<td align="center"><%=VCONT_REF_NO.elementAt(i) %></td>
										<td align="center"><%=VCONT_STATUS.elementAt(i) %></td>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
										<td align="right"><%=VRATE.elementAt(i)%>
											<%-- <%if (!VPRICE_LINE_RATE.elementAt(i).equals("")) {%>
												<font color="red">(<%=VPRICE_LINE_RATE.elementAt(i)%>)</font>
											<%}%> --%>	
										</td>
										<td align="center"><%=VFIN_CURVE_NM.elementAt(i)%></td>
										<td align="center"><%=VPHYS_CURVE_NM.elementAt(i) %></td>										
									</tr>
									<%} %>
									<%}else{ %>
										<tr>
											<td colspan="15">
												<div align="center"><%=utilmsg.infoMessage("<b>No Exposure for the Report Date!</b>")%></div>
											</td>
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
</div>						

<input type="hidden" name="temp_report_dt" value="<%=yesdate%>">
<input type="hidden" name="isEodDone" value="<%=VIS_EOD_PROCESS_DONE.contains("Y")%>">

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

<script>

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch");
  	
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

$(document).ready(function() {
	
	$('#tbsearch th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "")
		{
		}
		else if(title == "Sr#")
		{
			//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
		}
		else
		{
			$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:100px"/></div>');
		}
	});
	
});
</script>
</body>
</html>