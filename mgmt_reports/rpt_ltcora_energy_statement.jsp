
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_ltcora_energy_statement.jsp?counterparty_cd="+counterparty_cd+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var newWindow;
function openContractList()
{
	var u = document.forms[0].u.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var buy_sell = document.forms[0].buy_sell.value;
	
	var msg="";
	var flag=true;
	
	if(trim(counterparty_cd) == "" || counterparty_cd == "0")
	{
		msg+="Select Customer!\n";
		flag=false;
	}
	
	var url = "../ltcora/frm_ltcora_cargo_cont_list.jsp?counterparty_cd="+counterparty_cd+"&buy_sell="+buy_sell+"&u="+u;
	
	if(flag)
	{
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
		}
	}
	else
	{
		alert(msg);
	}
}

function setContDetail(countpty_cd,cont_no,cont_rev_no,cont_type,disp_cont_no,cont_ref_no,cont_name,no_cargo,agmt_no,contract_type,agmt_rev,agmt_type,buy_sell,start_dt,end_dt,sug_per)
{
	var u = document.forms[0].u.value;

	var url = "../mgmt_reports/rpt_ltcora_energy_statement.jsp?counterparty_cd="+countpty_cd+"&cont_no="+cont_no+
			"&disp_cont_no="+disp_cont_no+"&cont_ref_no="+cont_ref_no+"&cont_rev="+cont_rev_no+
			"&cont_name="+cont_name+"&no_cargo="+no_cargo+"&agmt_no="+agmt_no+"&contract_type="+contract_type+
			"&agmt_rev="+agmt_rev+"&agmt_type="+agmt_type+"&buy_sell="+buy_sell+"&start_dt="+start_dt+"&end_dt="+end_dt+"&sug_per="+sug_per+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function generatePDFORXLS(dealMap,qqNo,qqDt,vesselNm,startDt,endDt,boeNo,boeDt,unloadedQty,sugQty,regassQty,counterparty_cd,gen_type)
{
	var signing_dt = document.forms[0].signing_dt.value;
	var contact_person = document.forms[0].contact_person.value;
	
	var url="rpt_energy_stmt_pdf.jsp?qqNo="+qqNo+"&qqDt="+qqDt+"&vesselNm="+vesselNm+"&startDt="+startDt
			+"&endDt="+endDt+"&boeNo="+boeNo+"&boeDt="+boeDt+"&unloadedQty="+unloadedQty+"&sugQty="+sugQty
			+"&regassQty="+regassQty+"&counterparty_cd="+counterparty_cd+"&dealMap="+dealMap+"&signing_dt="+signing_dt+"&contact_person="+contact_person
			+"&gen_type="+gen_type;
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Energy Statement","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Energy Statement","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
		
}
function exportToXls()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agmt_type = document.forms[0].agmt_type.value;
	var agmt_no = document.forms[0].agmt_no.value;
	var agmt_rev = document.forms[0].agmt_rev.value;
	var cont_no = document.forms[0].cont_no.value;
	var cont_rev = document.forms[0].cont_rev.value;
	var contract_type = document.forms[0].contract_type.value;
	var sug_per = document.forms[0].sug_per.value;
	var u = document.forms[0].u.value;
	
	var url = "xls_ltcora_energy_statement.jsp?fileName=LTCORA Cargo Energy Statement.xls&counterparty_cd="+counterparty_cd+
			"&agmt_type="+agmt_type+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+
			"&cont_no="+cont_no+"&cont_rev="+cont_rev+"&contract_type="+contract_type+"&sug_per="+sug_per+"&u="+u;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_MGMT_Reports" id="mgmt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String buy_sell = request.getParameter("buy_sell")==null?"C":request.getParameter("buy_sell");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String disp_cont_no = request.getParameter("disp_cont_no")==null?"":request.getParameter("disp_cont_no");
String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String cont_name = request.getParameter("cont_name")==null?"":request.getParameter("cont_name");
String no_cargo = request.getParameter("no_cargo")==null?"":request.getParameter("no_cargo");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String sug_per = request.getParameter("sug_per")==null?"":request.getParameter("sug_per");
String appPath = request.getServletContext().getRealPath("");

mgmt_rpt.setCallFlag("ENERGY_STATEMENT");
mgmt_rpt.setComp_cd(owner_cd);
mgmt_rpt.setBuySell(buy_sell);
mgmt_rpt.setCounterparty_cd(counterparty_cd);
mgmt_rpt.setAgmt_type(agmt_type);
mgmt_rpt.setAgmt_no(agmt_no);
mgmt_rpt.setAgmt_rev(agmt_rev);
mgmt_rpt.setCont_no(cont_no);
mgmt_rpt.setCont_rev(cont_rev);
mgmt_rpt.setCont_type(contract_type);
mgmt_rpt.setSug(sug_per);
mgmt_rpt.setAppPath(appPath);//HM : Passing appPath through JSP as 'mgmt_reports' package does not contains any servelet file so can not Access servelet.request in DB
mgmt_rpt.init();

String signing_dt = mgmt_rpt.getSigning_dt();

Vector VMST_COUNTERPARTY_CD = mgmt_rpt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = mgmt_rpt.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = mgmt_rpt.getVMST_COUNTERPARTY_NM();

Vector VCONTACT_PERSON = mgmt_rpt.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_SEQ_NO = mgmt_rpt.getVCONTACT_PERSON_SEQ_NO();

Vector VDEAL_MAP = mgmt_rpt.getVDEAL_MAP();
Vector VQQ_NO = mgmt_rpt.getVQQ_NO();
Vector VQQ_DT = mgmt_rpt.getVQQ_DT();
Vector VSHIP_NM = mgmt_rpt.getVSHIP_NM();
Vector VSTORAGE_START_DT = mgmt_rpt.getVSTORAGE_START_DT();
Vector VSTORAGE_END_DT = mgmt_rpt.getVSTORAGE_END_DT();
Vector VBOE_NO = mgmt_rpt.getVBOE_NO();
Vector VBOE_DT = mgmt_rpt.getVBOE_DT();
Vector VUNLOADED_QTY = mgmt_rpt.getVUNLOADED_QTY();
Vector VSUG_QTY = mgmt_rpt.getVSUG_QTY();
Vector VREGASSIFY_QTY = mgmt_rpt.getVREGASSIFY_QTY();
Vector VISFILE_EXIST = mgmt_rpt.getVISFILE_EXIST();

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
			}%>
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
				    		LTCORA Cargo Energy Statement
	   	 				</div>
	   	 				<a>
							<span class="input-group-text">
							 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
							</span>
						</a>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Customer<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh();">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="button" class="btn btn-info btn-sm select_btn" value="Select CN/Period" onclick="openContractList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="cont_name" value="<%=cont_name%>" readOnly>
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label title="Confirmation No#" class="form-label"><b>LTCORA (Sales) CN/Period</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-6 col-xs-6 col-md-6">
				    				<input type="text" class="form-control form-control-sm" name="cargo_number_disp" value="<%=disp_cont_no%>" maxLength="50" readOnly>
				    				<input type="hidden"name="cont_cargo_number" value="<%=disp_cont_no%>">
				      			</div>
				      			<div class="col-sm-6 col-xs-6 col-md-6">
				    				<input type="text" class="form-control form-control-sm" name="cont_ref_no" value="<%=cont_ref_no%>" maxLength="50" readOnly>
				      				<input type="hidden" name="agmt_no" id="agmt_no" value="<%=agmt_no%>">
				      				<input type="hidden" name="agmt_rev" id="agmt_rev" value="<%=agmt_rev%>">
				      				<input type="hidden" name="agmt_type" id="agmt_type" value="<%=agmt_type%>">
				      				<input type="hidden" name="contract_type" id="contract_type" value="<%=contract_type%>">
				      				<input type="hidden" name="cont_rev" id="cont_rev" value="<%=cont_rev%>">
				      				<input type="hidden" name="cont_no" id="cont_no" value="<%=cont_no%>">
				      				<input type="hidden" name="no_cargo" id="no_cargo" value="<%=no_cargo%>">
				      				<input type="hidden" name="sug_per" id="sug_per" value="<%=sug_per%>">
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Contract Period</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="disp_start_dt" value="<%=start_dt%> - <%=end_dt%>" maxLength="10" disabled="disabled">
			      						<input type="hidden" name="cont_start_dt" id="cont_start_dt" value="<%=start_dt%>">
				      					<input type="hidden" name="cont_end_dt" id="cont_end_dt" value="<%=end_dt%>">
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>No. of Cargo</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="no_cargo_number" value="<%=no_cargo%>" maxLength="50" disabled>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<!-- For Contact Person -->
					<div class="row m-b-5" style="display: none">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>From Contact Person<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="contact_person" onchange="">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCONTACT_PERSON_SEQ_NO.size();i++){ %>
										<option value="<%=VCONTACT_PERSON_SEQ_NO.elementAt(i)%>"><%=VCONTACT_PERSON.elementAt(i)%></option>
										<%} %>
									</select>
									<%-- <script>document.forms[0].contact_person.value="<%=contact_person%>"</script> --%>
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
					 	<div class="col-md-12 col-sm-12 col-xs-12">
					 		<div class="table-responsive">
	    						<table class="table table-bordered table-hover">
	    							<thead>
	    								<tr>
	    									<th rowspan="2">Sr#</th>
	    									<th rowspan="2">Cargo#</th>
	    									<th rowspan="2">Q & Q Certificate#</th>
	    									<th rowspan="2">Q & Q Certificate Date</th>
	    									<th rowspan="2">Vessel Name</th>
	    									<th rowspan="2">Vessel Arrival Date</th>
	    									<th colspan="2">Storage Period</th>
	    									<th rowspan="2">BOE Number</th>
	    									<th rowspan="2">BOE Date</th>
	    									<th rowspan="2">Unloaded MMBTU</th>
	    									<th rowspan="2">SUG</th>
	    									<th rowspan="2">ReGasification MMBTU</th>
	    									<th rowspan="2">PDF</th>
	    									<th rowspan="2">Excel</th>
	    								</tr>
	    								<tr>
	    									<th>Start Date</th>
	    									<th>End Date</th>
	    								</tr>
	    							</thead>
	    							<tbody>
	    								<%if(VDEAL_MAP.size()>0){%>
	    									<%for(int i=0;i<VDEAL_MAP.size();i++){%>
	    									<tr>
	    										<td align="left"><%=i+1%></td>
	    										<td align="center"><%=VDEAL_MAP.elementAt(i)%></td>
	    										<td align="center"><%=VQQ_NO.elementAt(i)%></td>
	    										<td align="center"><%=VQQ_DT.elementAt(i)%></td>
	    										<td align="center"><%=VSHIP_NM.elementAt(i)%></td>
	    										<td align="center"><%=VSTORAGE_START_DT.elementAt(i)%></td>
	    										<td align="center"><%=VSTORAGE_START_DT.elementAt(i)%></td>
	    										<td align="center"><%=VSTORAGE_END_DT.elementAt(i)%></td>
	    										<td align="center"><%=VBOE_NO.elementAt(i)%></td>
	    										<td align="center"><%=VBOE_DT.elementAt(i)%></td>
	    										<td align="right"><%=VUNLOADED_QTY.elementAt(i)%></td>
	    										<td align="right"><%=VSUG_QTY.elementAt(i)%></td>
	    										<td align="right"><%=VREGASSIFY_QTY.elementAt(i)%></td>
	    										<td align="center">
	    											<%if(print_access.equals("Y")){ %>
														<i 
														<%if(VISFILE_EXIST.elementAt(i).equals("Y")){ %>
														class="fa fa-file-pdf-o fa-2x pdf_icon" 
														<%}else{ %>
														class="fa fa-print fa-2x" 
														<%} %>
														style="color:#800000;"
														onclick="generatePDFORXLS('<%=VDEAL_MAP.elementAt(i)%>','<%=VQQ_NO.elementAt(i)%>',
														'<%=VQQ_DT.elementAt(i)%>','<%=VSHIP_NM.elementAt(i)%>','<%=VSTORAGE_START_DT.elementAt(i)%>',
														'<%=VSTORAGE_END_DT.elementAt(i)%>','<%=VBOE_NO.elementAt(i)%>','<%=VBOE_DT.elementAt(i)%>',
														'<%=VUNLOADED_QTY.elementAt(i)%>','<%=VSUG_QTY.elementAt(i)%>','<%=VREGASSIFY_QTY.elementAt(i)%>','<%=counterparty_cd%>'
														,'PDF')"></i>
													<%}else{ %>
														<i class="fa fa-file-pdf-o fa-2x pdf_icon" style="color:grey;"></i>
													<%} %>
	    										</td>
	    										<td align="center">
	    											<%if(print_access.equals("Y")){ %>
														<i class="fa fa-file-excel-o fa-2x excel_icon" onclick="generatePDFORXLS('<%=VDEAL_MAP.elementAt(i)%>','<%=VQQ_NO.elementAt(i)%>',
														'<%=VQQ_DT.elementAt(i)%>','<%=VSHIP_NM.elementAt(i)%>','<%=VSTORAGE_START_DT.elementAt(i)%>',
														'<%=VSTORAGE_END_DT.elementAt(i)%>','<%=VBOE_NO.elementAt(i)%>','<%=VBOE_DT.elementAt(i)%>',
														'<%=VUNLOADED_QTY.elementAt(i)%>','<%=VSUG_QTY.elementAt(i)%>','<%=VREGASSIFY_QTY.elementAt(i)%>','<%=counterparty_cd%>'
														,'XLS')"></i>
													<%}else{ %>
														<i class="fa fa-file-excel-o fa-2x excel_icon" style="color:grey;"></i>
													<%} %>
	    										</td>
	    									</tr>
	    									<%}%>
	    								<%}else{%>
	    									<tr>
	    										<td colspan="15" align="center"><%=utilmsg.infoMessage("<b>No Cargo is Available!</b>") %></td>
	    									</tr>
	    								<%}%>
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
<input type="hidden" name="buy_sell" value="<%=buy_sell%>">
<input type="hidden" name="comp_abbr" value="<%=owner_abbr%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="signing_dt" value="<%=signing_dt%>">

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