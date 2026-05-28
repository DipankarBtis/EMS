<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_sap_interface_status.jsp?u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var url = "xls_sap_interface_status.jsp?fileName=SAP Interface Status Report.xls&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_MGMT_Reports" id="dbmgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdt = utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdt:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdt:request.getParameter("to_dt");

dbmgmt.setCallFlag("SAP_INTERFACE_STATUS");
dbmgmt.setComp_cd(owner_cd);
dbmgmt.setFrom_dt(from_dt);
dbmgmt.setTo_dt(to_dt);
dbmgmt.init();

//Pratham Bhatt
String bu_region = dbmgmt.getBu_Region();
Vector V_BU_ABBR = dbmgmt.getV_BU_ABBR();
Vector V_PLANT_ABBR = dbmgmt.getV_PLANT_ABBR();
Vector V_FMS_REF = dbmgmt.getV_FMS_REF();
Vector V_POST_STATUS = dbmgmt.getV_POST_STATUS();
Vector V_POST_DT=dbmgmt.getV_POST_DT();
Vector V_POST_TIME=dbmgmt.getV_POST_TIME();
Vector V_IDOC_NO=dbmgmt.getV_IDOC_NO();
Vector V_IDOC_STATUS=dbmgmt.getV_IDOC_STATUS();
Vector V_STATUS_MSG=dbmgmt.getV_STATUS_MSG();
Vector V_DOC_NO=dbmgmt.getV_DOC_NO();
Vector V_COMPANY_CODE=dbmgmt.getV_COMPANY_CODE();
Vector V_FISCAL_YR=dbmgmt.getV_FISCAL_YR();
Vector V_MSG_STATUS=dbmgmt.getV_MSG_STATUS();
Vector V_CONT_NO = dbmgmt.getV_CONT_NO();
Vector V_DUE_DT=dbmgmt.getV_DUE_DT();
Vector V_APPROVE_DT = dbmgmt.getV_APPROVE_DT();
Vector V_APPROVED_BY = dbmgmt.getV_APPROVED_BY();
Vector V_APPROVED_FLAG = dbmgmt.getV_APPROVED_FLAG();
Vector V_NET_PAYABLE_AMT = dbmgmt.getV_NET_PAYABLE_AMT();
Vector V_SAP_POSTED = dbmgmt.getV_SAP_POSTED();
Vector VINDEX = dbmgmt.getVINDEX();
Vector VSAP_INDEX = dbmgmt.getVSAP_INDEX();

Vector VACC_BU_ABBR = dbmgmt.getVACC_BU_ABBR();
Vector VACC_PLANT_ABBR = dbmgmt.getVACC_PLANT_ABBR();
Vector VACC_FMS_REF = dbmgmt.getVACC_FMS_REF();
Vector VACC_POST_STATUS = dbmgmt.getVACC_POST_STATUS();
Vector VACC_POST_DT=dbmgmt.getVACC_POST_DT();
Vector VACC_POST_TIME=dbmgmt.getVACC_POST_TIME();
Vector VACC_IDOC_NO=dbmgmt.getVACC_IDOC_NO();
Vector VACC_IDOC_STATUS=dbmgmt.getVACC_IDOC_STATUS();
Vector VACC_STATUS_MSG=dbmgmt.getVACC_STATUS_MSG();
Vector VACC_DOC_NO=dbmgmt.getVACC_DOC_NO();
Vector VACC_COMPANY_CODE=dbmgmt.getVACC_COMPANY_CODE();
Vector VACC_FISCAL_YR=dbmgmt.getVACC_FISCAL_YR();
Vector VACC_MSG_STATUS=dbmgmt.getVACC_MSG_STATUS();
Vector VACC_CONT_NO = dbmgmt.getVACC_CONT_NO();
Vector VACC_DUE_DT=dbmgmt.getVACC_DUE_DT();
Vector VACC_APPROVE_DT = dbmgmt.getVACC_APPROVE_DT();
Vector VACC_APPROVED_BY = dbmgmt.getVACC_APPROVED_BY();
Vector VACC_APPROVED_FLAG = dbmgmt.getVACC_APPROVED_FLAG();
Vector VACC_NET_PAYABLE_AMT = dbmgmt.getVACC_NET_PAYABLE_AMT();
Vector VACC_SAP_POSTED = dbmgmt.getVACC_SAP_POSTED();
Vector VACC_INDEX = dbmgmt.getVACC_INDEX();
Vector VACC_SAP_INDEX = dbmgmt.getVACC_SAP_INDEX();

Vector VSAP_INTERFACE_DISPLAY = dbmgmt.getVSAP_INTERFACE_DISPLAY();
Vector VTITLE = dbmgmt.getVTITLE();
Vector VACC_SAP_INTERFACE_DISPLAY = dbmgmt.getVACC_SAP_INTERFACE_DISPLAY();
Vector VACC_TITLE = dbmgmt.getVACC_TITLE();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="d-flex justify-content-between">
						<div class="topheader">
				    		SAP Interface Status Report 
	   	 				</div>
	   	 				<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-5 col-xs-5 col-md-5"></div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>From</b></label>
							</div>
						</div>
						<div class="col-auto">
		      				<div class="input-group input-group-sm">
	      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
	      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
	      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
      						</div>
		    			</div>
						<div class="col-auto">
							<label class="form-label"><b>To</b></label>
						</div>
						<div class="col-auto">
		      				<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
	      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
	      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
      						</div>
						</div>
					</div>
				</div>
				<div class="card-header cdheader">
					 <div class="d-flex justify-content-between">
						<div class="topheader">
				    		Actual
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
				<%
				int ctn=0;
				int l=0;
				int k=0;
				for(int i=0; i<VSAP_INTERFACE_DISPLAY.size();i++){%>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i><%=VSAP_INTERFACE_DISPLAY.elementAt(i) %></label>
					</div>
					<%
					for(int j=0; j<Integer.parseInt(""+VSAP_INDEX.elementAt(i));j++) {
					%>
						<div class="accordion-item accor_item">
							<h2 class="accordion-header" id="heading1">
 								<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=ctn+1 %>" aria-expanded="false" aria-controls="collapse<%=ctn+1%>"><%=VTITLE.elementAt(ctn) %>&nbsp;<font color="blue">(<%=VINDEX.elementAt(ctn++) %>)</font></button>	
					    	</h2>
					    	<div id="collapse<%=ctn%>" class="accordion-collapse collapse" aria-labelledby="heading1">
					    		<div class="accordion-body accor-body">
									<div class="row">
										<div class="table-responsive">
											<table class="table table-bordered" id="filter<%=ctn%>">
												<thead id="tbsearch<%=ctn%>">
													<tr>
														<th align="center">Sr#</th>
														<th align="center">Doc Num</th><!-- FMSngRefNum -->
														<th align="center">Deal Num</th><!-- FMS Contract# -->
														<th align="center">Internal Legal Entity</th><!-- Owner-BU ABBR (SETI-HZ) -->
														<th align="center">External Legal Entity</th><!-- Counterparty-PLANT ABBR -->
														<th align="center">Doc Status</th><!-- Report data will come only for Approved SAP XML (Approved) -->
														<th align="center">Payment Due Date</th>
														<th align="center">Settle Date</th><!-- Invoice/Remittance Approval Date -->
														<th align="center">Settled By</th><!-- Invoice/Remittance Approval By -->
														<th align="center">Invoice Type</th><!-- For Accrual it will be Accrual else invoice -->
														<th align="center">Amount</th><!-- Netpayable as per Invoice -->
														<th align="center">Posted to AM</th><!-- Approved Invoice it is YES -->
														<th align="center">Region</th>
														<th align="center">Document Type</th><!-- X1 : actual payables, X2 : actual receivables, X3 : accrual payables, X4 : accrual receivables -->
														<th align="center">Posting Date</th>
														<th align="center">Message ID</th><!-- iDocNo -->
														<th align="center">Message Status</th>
														<th align="center">Posted to SAP</th><!-- No - means some posting Error -->
														<th align="center">SAP Doc Num</th><!-- SAPDocNumber -->
														<th align="center">SAP Acknowledgement</th><!-- SAPStatusMsg -->
														<th align="center">Sap Company Code</th>
														<th align="center">Rep Center</th><!-- SEI -->
													</tr>
												</thead>
												<tbody>
													<%
													 int index = Integer.parseInt(""+VINDEX.elementAt((ctn-1)));
													if(Integer.parseInt(""+VINDEX.elementAt(ctn-1))>0){
														k=0; for(l=l; l<V_FMS_REF.size(); l++)
														{
															k+=1;
														%>
														<tr>
															<td align="center"><%=k%>.</td>
															<td align="center"><%=V_FMS_REF.elementAt(l)%></td>
															<td align="center"><%=V_CONT_NO.elementAt(l)%></td>
															<td align="center"><%=V_BU_ABBR.elementAt(l)%></td> 
															<td align="center"><%=V_PLANT_ABBR.elementAt(l)%></td> 
															<td align="center"><span class="alert alert-success"><b>Approve</b></span></td>
															<td align="center"><%=V_DUE_DT.elementAt(l)%></td>
															<td align="center"><%=V_APPROVE_DT.elementAt(l)%></td>
															<td align="center"><%=V_APPROVED_BY.elementAt(l)%></td>
															<td align="center">Invoiced</td><!-- * For Accrual it will be Accrual else invoice -->
															<td align="right"><%=V_NET_PAYABLE_AMT.elementAt(l)%></td>
															<%if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Receivable")){%>
															<td align="center"<%if(!V_APPROVED_FLAG.elementAt(l).equals("Y")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(V_APPROVED_FLAG.elementAt(l).equals("Y")){%><b>Yes</b><%} %></td>
															<%}else if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Payable")){%>
															<td align="center"<%if(!V_APPROVED_FLAG.elementAt(l).equals("A")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(V_APPROVED_FLAG.elementAt(l).equals("A")){%><b>Yes</b><%} %></td>
															<%}else if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Advance")){%>
															<td align="center"<%if(!V_APPROVED_FLAG.elementAt(l).equals("O")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(V_APPROVED_FLAG.elementAt(l).equals("O")){%><b>Yes</b><%} %></td>
															<%} %>
															<td align="center"><%=bu_region%></td>
															<%if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Receivable")){%>
															<td align="center">X2</td>
															<%}else{%>
															<td align="center">X1</td>
															<%} %>
															<td align="center"><%=V_POST_DT.elementAt(l)%></td>
															<td align="center"><%=V_IDOC_NO.elementAt(l)%></td>
															<td align="center" valign="middle">
																<%if(V_MSG_STATUS.elementAt(l).equals("S")){ %>
																	<span class="alert alert-success"><b>Success</b></span>
																<%}else if(V_MSG_STATUS.elementAt(l).equals("W")){ %>
																	<span class="alert alert-secondary"><b>Warning</b></span>
																<%}else if(V_MSG_STATUS.elementAt(l).equals("E")){ %>
																	<span class="alert alert-danger"><b>Error</b></span>
																<%}else if(V_MSG_STATUS.elementAt(l).equals("I")){ %>
																	<span class="alert alert-info"><b>Info.</b></span>
																<%}%>
															</td>
															<td align="center"><%=V_SAP_POSTED.elementAt(l)%></td>
															<td align="center"><%=V_DOC_NO.elementAt(l) %></td>
															<td align="center"><%=V_STATUS_MSG.elementAt(l)%></td>
															<td align="center"><%=V_COMPANY_CODE.elementAt(l)%></td>
															<td align="center">SEI</td>
														</tr>
															<%if(k==index){%>
																<%l=l+1;
																break;}
															%>
														<%} %>
													<%}else{ %>
														<tr>
															<td colspan="22" align="center"><%=utilmsg.infoMessage("<b>No SAP Interface Status Data is Available For Receivable!</b>") %></td>
														</tr>     
													<%} %>
												</tbody>
											</table>
										</div>
									</div>
								</div>	
					    	</div>
					    </div>
					<%} %>
				<%} %>
				</div>
				<div class="card-header cdheader">
					 <div class="d-flex justify-content-between">
						<div class="topheader">
				    		Accrual
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
				<%
				ctn=0;
				l=0;
				k=0;
				for(int i=0; i<VACC_SAP_INTERFACE_DISPLAY.size();i++){%>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i><%=VACC_SAP_INTERFACE_DISPLAY.elementAt(i) %></label>
					</div>
					<%
					for(int j=0; j<Integer.parseInt(""+VACC_SAP_INDEX.elementAt(i));j++) {
					%>
						<div class="accordion-item accor_item">
							<h2 class="accordion-header" id="heading_acc1">
 								<button name="acc_sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse_acc<%=ctn+1 %>" aria-expanded="false" aria-controls="collapse_acc<%=ctn+1%>"><%=VACC_TITLE.elementAt(ctn) %>&nbsp;<font color="blue">(<%=VACC_INDEX.elementAt(ctn++) %>)</font></button>	
					    	</h2>
					    	<div id="collapse_acc<%=ctn%>" class="accordion-collapse collapse" aria-labelledby="heading_acc1">
					    		<div class="accordion-body accor-body">
									<div class="row">
										<div class="table-responsive">
											<table class="table table-bordered" id="acc_filter<%=ctn%>">
												<thead id="acc_tbsearch<%=ctn%>">
													<tr>
														<th align="center">Sr#</th>
														<th align="center">Doc Num</th><!-- FMSngRefNum -->
														<th align="center">Deal Num</th><!-- FMS Contract# -->
														<th align="center">Internal Legal Entity</th><!-- Owner-BU ABBR (SETI-HZ) -->
														<th align="center">External Legal Entity</th><!-- Counterparty-PLANT ABBR -->
														<th align="center">Doc Status</th><!-- Report data will come only for Approved SAP XML (Approved) -->
														<th align="center">Payment Due Date</th>
														<th align="center">Settle Date</th><!-- Report Date of XML Generation Date -->
														<th align="center">Settled By</th><!-- By Default : System -->
														<th align="center">Invoice Type</th><!-- For Accrual it will be Accrual else invoice -->
														<th align="center">Amount</th><!-- Netpayable as per Invoice -->
														<th align="center">Posted to AM</th><!-- Approved Invoice it is YES -->
														<th align="center">Region</th>
														<th align="center">Document Type</th><!-- X1 : actual payables, X2 : actual receivables, X3 : accrual payables, X4 : accrual receivables -->
														<th align="center">Posting Date</th>
														<th align="center">Message ID</th><!-- iDocNo -->
														<th align="center">Message Status</th>
														<th align="center">Posted to SAP</th><!-- No - means some posting Error -->
														<th align="center">SAP Doc Num</th><!-- SAPDocNumber -->
														<th align="center">SAP Acknowledgement</th><!-- SAPStatusMsg -->
														<th align="center">Sap Company Code</th>
														<th align="center">Rep Center</th><!-- SEI -->
													</tr>
												</thead>
												<tbody>
													<%
													 int index = Integer.parseInt(""+VACC_INDEX.elementAt((ctn-1)));
														if(Integer.parseInt(""+VACC_INDEX.elementAt(ctn-1))>0){
														k=0; for(l=l; l<VACC_FMS_REF.size(); l++)
														{
															k+=1;
														%>
														<tr>
															<td align="center"><%=k%>.</td>
															<td align="center"><%=VACC_FMS_REF.elementAt(l)%></td>
															<td align="center"><%=VACC_CONT_NO.elementAt(l)%></td>
															<td align="center"><%=VACC_BU_ABBR.elementAt(l)%></td> 
															<td align="center"><%=VACC_PLANT_ABBR.elementAt(l)%></td> 
															<td align="center"><span class="alert alert-success"><b>Approve</b></span></td>
															<td align="center"><%=VACC_DUE_DT.elementAt(l)%></td>
															<td align="center"><%=VACC_APPROVE_DT.elementAt(l)%></td>
															<td align="center"><%=VACC_APPROVED_BY.elementAt(l)%></td>
															<td align="center">Accrual</td><!-- * For Accrual it will be Accrual else invoice -->
															<td align="right"><%=VACC_NET_PAYABLE_AMT.elementAt(l)%></td>
															<%if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Receivable")){%>
															<td align="center"<%if(!VACC_APPROVED_FLAG.elementAt(l).equals("Y")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(VACC_APPROVED_FLAG.elementAt(l).equals("Y")){%><b>Yes</b><%} %></td>
															<%}else if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Payable")){%>
															<td align="center"<%if(!VACC_APPROVED_FLAG.elementAt(l).equals("A")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(VACC_APPROVED_FLAG.elementAt(l).equals("A")){%><b>Yes</b><%} %></td>
															<%}else if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Advance")){%>
															<td align="center"<%if(!VACC_APPROVED_FLAG.elementAt(l).equals("O")){ %>style="color:red"<%}else{ %>style="color:green"<%} %>><%if(VACC_APPROVED_FLAG.elementAt(l).equals("O")){%><b>Yes</b><%} %></td>
															<%} %>
															<td align="center"><%=bu_region%></td>
															<%if(VSAP_INTERFACE_DISPLAY.elementAt(i).equals("Receivable")){%>
															<td align="center">X4</td>
															<%}else{%>
															<td align="center">X3</td>
															<%} %>
															<td align="center"><%=VACC_POST_DT.elementAt(l)%></td>
															<td align="center"><%=VACC_IDOC_NO.elementAt(l)%></td>
															<td align="center" valign="middle">
																<%if(VACC_MSG_STATUS.elementAt(l).equals("S")){ %>
																	<span class="alert alert-success"><b>Success</b></span>
																<%}else if(VACC_MSG_STATUS.elementAt(l).equals("W")){ %>
																	<span class="alert alert-secondary"><b>Warning</b></span>
																<%}else if(VACC_MSG_STATUS.elementAt(l).equals("E")){ %>
																	<span class="alert alert-danger"><b>Error</b></span>
																<%}else if(VACC_MSG_STATUS.elementAt(l).equals("I")){ %>
																	<span class="alert alert-info"><b>Info.</b></span>
																<%}%>
															</td>
															<td align="center"><%=VACC_SAP_POSTED.elementAt(l)%></td>
															<td align="center"><%=VACC_DOC_NO.elementAt(l) %></td>
															<td align="center"><%=VACC_STATUS_MSG.elementAt(l)%></td>
															<td align="center"><%=VACC_COMPANY_CODE.elementAt(l)%></td>
															<td align="center">SEI</td>
														</tr>
															<%if(k==index){%>
																<%l=l+1;
																break;}
															%>
														<%} %>
													<%}else{ %>
														<tr>
															<td colspan="22" align="center"><%=utilmsg.infoMessage("<b>No SAP Interface Status Data is Available For Receivable!</b>") %></td>
														</tr>     
													<%} %>
												</tbody>
											</table>
										</div>
									</div>
								</div>	
					    	</div>
					    </div>
					<%} %>
				<%} %>
				</div>
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
<input type="hidden" name="sap_title" value="<%=VINDEX.size()%>">

</form>
<script>

function Search(obj,indx,j)
{
	var input,filter,table,tr, td, txtValue, count=0;
	input = document.getElementById(obj.id);
	
	filter = input.value.toLocaleLowerCase()
	var id = "filter"+j;
	table = document.getElementById(id);
	tr = table.getElementsByTagName("tr");
	
	for(i=1;i<tr.length;i++)
	{
		td = tr[i].getElementsByTagName("td")[indx];
		if(td)
		{	
			txtValue = td.textContent || td.innerText;
			if(txtValue.toLocaleLowerCase().indexOf(filter) > -1)
			{
				tr[i].style.display = "";
				count++;
			}
			else
			{
				tr[i].style.display = "none";
			}
		}
	}
}

$(document).ready(function() {
	var size = document.forms[0].sap_title.value;
	var j;
	for(j=1;j<=size-1;j++)
	{
	$('#filter'+j+' #tbsearch'+j+' th').each(function(i){
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
			$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+j+'" onkeyup="Search(this,'+i+','+j+');" placeholder="Search '+title+'" style="width:100px"/></div>');
		}
	});
		
	}
	
});

function Search(obj,indx,j)
{
	var input,filter,table,tr, td, txtValue, count=0;
	input = document.getElementById(obj.id);
	
	filter = input.value.toLocaleLowerCase()
	var id = "acc_filter"+j;
	table = document.getElementById(id);
	tr = table.getElementsByTagName("tr");
	
	for(i=1;i<tr.length;i++)
	{
		td = tr[i].getElementsByTagName("td")[indx];
		if(td)
		{	
			txtValue = td.textContent || td.innerText;
			if(txtValue.toLocaleLowerCase().indexOf(filter) > -1)
			{
				tr[i].style.display = "";
				count++;
			}
			else
			{
				tr[i].style.display = "none";
			}
		}
	}
}

$(document).ready(function() {
	var size = document.forms[0].sap_title.value;
	var j;
	for(j=1;j<=size-1;j++)
	{
	$('#acc_filter'+j+' #acc_tbsearch'+j+' th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "")
		{
		}
		else if(title == "Sr#")
		{
			//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="acc_table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
		}
		else
		{
			$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="acc_table_'+title+j+'" onkeyup="Search(this,'+i+','+j+');" placeholder="Search '+title+'" style="width:100px"/></div>');
		}
	});
		
	}
	
});
</script>
</body>
</html>
