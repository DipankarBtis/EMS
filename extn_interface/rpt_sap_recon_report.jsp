<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
.inputWrapper {
    height: 32px;
    width: 32px;
    overflow: hidden;
    position: relative;
    cursor: pointer;
    /*Using a background color, but you can use a background image to represent a button*/
    /* background-color: #DDF; */
}
.fileInput {
    cursor: pointer;
    height: 100%;
    width: 75px;
    position:absolute;
    top: 0;
    right: 0;
    z-index: 99;
    /*This makes the button huge. If you want a bigger button, increase the font size*/
    font-size:50px;
    /*Opacity settings for all browsers*/
    opacity: 0;
    -moz-opacity: 0;
    filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0)
}
</style>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var counterparty_cd=document.forms[0].counterparty_cd;
	var u=document.forms[0].u.value;
	var from_dt=document.forms[0].from_dt.value;
	var to_dt=document.forms[0].to_dt.value;
	var entity_role=document.forms[0].entity_role.value;
	var perv_entity_role=document.forms[0].perv_entity_role.value;
	//var gl_cd=document.forms[0].gl_cd.value;
	var from_dt_split = from_dt.split("/");
	var to_dt_split = to_dt.split("/");
	var from_dt1 = from_dt_split[2]+from_dt_split[1]+from_dt_split[0];
	var to_dt1 = to_dt_split[2]+to_dt_split[1]+to_dt_split[0];
	var flag=true;
	var count = compareDate(from_dt,to_dt);
	var tds = document.forms[0].tds;
	var tcs = document.forms[0].tcs;
	var tax = document.forms[0].tax;
	
	if(tds.checked)
	{
		tds.value="Y";
	}
	else
	{
		tds.value="N";
	}
	if(tcs.checked)
	{
		tcs.value="Y";
	}
	else
	{
		tcs.value="N";
	}
	if(tax.checked)
	{
		tax.value="Y";
	}
	else
	{
		tax.value="N";
	}
	
	var msg="";
	
	if(entity_role!=perv_entity_role)
	{
		counterparty_cd.value=0;
	}
	
	if(parseInt(count) == 1)
	{
		msg+="To date ("+to_dt+") must be greater than from date ("+from_dt+")!\n";	
		flag=false;
	}
	var tmp = from_dt.split("/")
	var tmp1 = to_dt.split("/")
	var date1 = new Date(tmp[1]+"/"+tmp[0]+"/"+tmp[2]);
 	var date2 = new Date(tmp1[1]+"/"+tmp1[0]+"/"+tmp1[2]);
 	
 	var time_difference = date2.getTime() - date1.getTime();
 	var days_difference = time_difference / (1000 * 60 * 60 * 24);
 	
 	if(parseInt(days_difference)+1 > 92)
	{
		msg+="Date range can't exceed 3 months period!";
		flag=false;
	}
	
	var url="rpt_sap_recon_report.jsp?u="+u+"&entity_role="+entity_role+"&counterparty_cd="+counterparty_cd.value+"&from_dt="+from_dt+"&to_dt="+to_dt
			+"&tds="+tds.value+"&tcs="+tcs.value+"&tax="+tax.value;//+"&gl_cd="+gl_cd
	
	if(flag==true)
	{
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}

function exportToXls()
{
	var sysdate = document.forms[0].sysdate.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var entity_role=document.forms[0].entity_role.value;
	var u=document.forms[0].u.value;
	
	sysdate = sysdate.toString();
	sysdate = sysdate.split('/').join('');
	var tds = document.forms[0].tds;
	var tcs = document.forms[0].tcs;
	var tax = document.forms[0].tax;
	
	if(tds.checked)
	{
		tds.value="Y";
	}
	else
	{
		tds.value="N";
	}
	if(tcs.checked)
	{
		tcs.value="Y";
	}
	else
	{
		tcs.value="N";
	}
	if(tax.checked)
	{
		tax.value="Y";
	}
	else
	{
		tax.value="N";
	}
	//var url = "xls_sap_recon_report.jsp?fileName=SAP Recon Report "+sysdate+".xls&counterparty_cd="+counterparty_cd+"&from_dt="+from_dt+"&to_dt="+to_dt;
	//var url = "xls_sap_recon_report.jsp?fileName=SAP Recon Report "+sysdate+".xls&from_dt="+from_dt+"&to_dt="+to_dt+"&entity_role="+entity_role+"&counterparty_cd="+counterparty_cd;
	var url = "xls_sap_all_recon_report.jsp?fileName=SAP Recon Report "+sysdate+".xls&from_dt="+from_dt+"&to_dt="+to_dt+"&entity_role="+entity_role+"&counterparty_cd="+counterparty_cd
			+"&tds="+tds.value+"&tcs="+tcs.value+"&tax="+tax.value+"&u="+u;
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function importData()
{
	  let input = document.createElement('input');
	  input.type = 'file';
	  input.onchange = _ => {
	    // you can use this method to get file and perform respective operations
	            let files =   Array.from(input.files);
	           // console.log(files);
	       };
	  //input.click();	  
}

function upload()
{
	document.forms[0].opration.value="UPLOAD";
	
	var opration = document.forms[0].opration.value;
	
	if(file.files.length == 0 )
	{
		//console.log("Nooooooo File selected!!!!");
	}
	else
	{
		if(opration)
		{
			var file_nm = document.getElementById('file');
			if(file_nm.files.item(0).name.includes(".xls") || file_nm.files.item(0).name.includes(".xlsx"))
			{
				var upload = confirm("Do you want to upload "+file_nm.files.item(0).name+" ?")
				if(upload==true)
				{
					document.forms[0].submit();
				}
			}
			else
			{
				var rejact = alert("Uploading file should be in .xls or .xlsx format!!!!")
			}
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sap_interface" id="db_intrface" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<% 
String sysdate=utildate.getSysdate();
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt = request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
String gl_cd = request.getParameter("gl_cd")==null?"0":request.getParameter("gl_cd");
String tds = request.getParameter("tds")==null?"Y":request.getParameter("tds");
String tcs = request.getParameter("tcs")==null?"Y":request.getParameter("tcs");
String tax = request.getParameter("tax")==null?"Y":request.getParameter("tax");

db_intrface.setCallFlag("SAP_RECON_RPT");
db_intrface.setComp_cd(owner_cd);
db_intrface.setCounterparty_cd(counterparty_cd);
db_intrface.setFrom_dt(from_dt);
db_intrface.setTo_dt(to_dt);
db_intrface.setEntity_role(entity_role);
db_intrface.setGl_cd(gl_cd);
db_intrface.setTds_flag(tds);
db_intrface.setTcs_flag(tcs);
db_intrface.setTax_flag(tax);
db_intrface.init();

String amt_sum = db_intrface.getAmt_sum();
String qty_sum = db_intrface.getQty_sum();
String amt_usd_sum = db_intrface.getAmt_usd_sum();
String sap_sum = db_intrface.getSap_sum();
String delta_sap_sum=db_intrface.getDelta_Sap_Sum();
String sap_sum_usd=db_intrface.getSap_Sum_Usd();
String delta_sap_sum_usd = db_intrface.getDelta_Sap_Sum_Usd();

Vector VMST_COUNTERPARTY_CD = db_intrface.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = db_intrface.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = db_intrface.getVMST_COUNTERPARTY_NM();

Vector VMST_GL_CD = db_intrface.getVMST_GL_CD();
Vector VMST_GL_DESC = db_intrface.getVMST_GL_DESC();

Vector VCOUNTERPARTY_CD = db_intrface.getVCOUNTERPARTY_CD();
Vector VCO_CD = db_intrface.getVCO_CD();
Vector VDEAL_MAP = db_intrface.getVDEAL_MAP();
Vector VINVOICE_NO = db_intrface.getVINVOICE_NO();
Vector VACCOUNT_PERIOD_YR = db_intrface.getVACCOUNT_PERIOD_YR();
Vector VACCOUNT_PERIOD_MONTH = db_intrface.getVACCOUNT_PERIOD_MONTH();
Vector VPERIOD_START_DT = db_intrface.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = db_intrface.getVPERIOD_END_DT();
Vector VGL_CD = db_intrface.getVGL_CD();
Vector VGL_DESC = db_intrface.getVGL_DESC();
Vector VVENDOR_CD = db_intrface.getVVENDOR_CD();
Vector VCURRENCY = db_intrface.getVCURRENCY();
Vector VDOC_TYPE = db_intrface.getVDOC_TYPE();
Vector VPK = db_intrface.getVPK();
Vector VDOC_NO=db_intrface.getVDOC_NO();
Vector VITEMTEXT=db_intrface.getVITEMTEXT();
Vector VTRANS_TYPE=db_intrface.getVTRANS_TYPE();
Vector VSAP_APPROVED_BY=db_intrface.getVSAP_APPROVED_BY();
Vector VPOST_DT = db_intrface.getVPOST_DT();
Vector VDOC_DT = db_intrface.getVDOC_DT();
Vector VALLOC_QTY = db_intrface.getVALLOC_QTY();
Vector VAMT = db_intrface.getVAMT();
Vector VUSD_AMT = db_intrface.getVUSD_AMT();
Vector VSAP_VALUE_INR = db_intrface.getVSAP_VALUE_INR();
Vector VSAP_VALUE_USD = db_intrface.getVSAP_VALUE_USD();
Vector VDELTA_SAP_VALUE_INR = db_intrface.getVDELTA_SAP_VALUE_INR();
Vector VDELTA_SAP_VALUE_USD = db_intrface.getVDELTA_SAP_VALUE_USD();
Vector VVENDOR_NM = db_intrface.getVVENDOR_NM();
Vector VIS_SAP_DOC_DATA = db_intrface.getVIS_SAP_DOC_DATA();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_sap_interface" enctype="multipart/form-data">
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
					    	SAP Recon Report
					    </div>
					    <div class="row justify-content-end">
						    <div class="col-auto">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x" onclick="exportToXls();" ></i></span>
							</div>
							<div class="col-auto">
							 	<div class="btn-group">
									<select class="btn btn-outline-secondary btngrp <%if(!entity_role.equals("0")){%>btnactive<%}%>" name="entity_role" onchange="refresh();">
										<option value="0">---All---</option>
										<option value="C">Customer</option>
						    			<option value="T">Trader</option>
						    			<option value="V">Vessel Agent</option>
						    			<option value="H">Custom House Agent</option>
						    			<option value="S">Surveyor</option>
						    			<option value="R">Transporter</option>
						    			<option value="G">Gas Exchange</option> 
						    			<!-- <option value="B">Business Owner</option>-->
									</select>
								</div>
								<script>
									document.forms[0].entity_role.value="<%=entity_role%>"
								</script>	
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0">--All--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_ABBR.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<%-- <div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
										<label class="form-label"><b>GL Data</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="gl_cd" onchange="refresh()">
										<option value="0">--All--</option>
										<%for(int i=0;i<VMST_GL_CD.size();i++){ %>
										<option value="<%=VMST_GL_CD.elementAt(i)%>"><%=VMST_GL_CD.elementAt(i)%> - <%=VMST_GL_DESC.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].gl_cd.value="<%=gl_cd%>"</script>
								</div>
							</div>
						</div> --%>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From </b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm">
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off" >
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
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<input type="checkbox" class="form-check-input" name="tds" value="Y" onclick="refresh()" <%if(tds.equals("Y")){%>checked<%}%>>
									<label class="form-label"><b>TDS</b></label>
								</div>
								<div class="col-auto">
									<input type="checkbox" class="form-check-input" name="tcs" value="Y" onclick="refresh()" <%if(tcs.equals("Y")){%>checked<%}%>>
									<label class="form-label"><b>TCS</b></label>
								</div>
								<div class="col-auto">
									<input type="checkbox" class="form-check-input" name="tax" value="Y" onclick="refresh()" <%if(tax.equals("Y")){%>checked<%}%>>
									<label class="form-label"><b>TAX</b></label>
								</div>
							</div>
						</div>
						<!-- for uploading the xls file -->
						<div class="col-sm-2 col-xs-2 col-md-2" align="right">
							<div class="d-flex justify-content-end">
								<div class="form-group row">
									<%if(write_access.equals("Y")){%>
										<div class="col-auto">
											<div class="btn-group">
												<div class="btn btn-outline-secondary subbtngrp" style="color:green; position:relative;" title="Sample Download" >
												 	<a href="../sample/Sample_SAP.xlsx"><i style="color: blue;" class="fa fa-download"></i>&nbsp;Sample</a>
												</div>
												<div class="btn btn-outline-secondary subbtngrp" onclick="importData();" onchange="upload();" position:relative;" class="inputWrapper" title="Import SAP Excel File" >
													<input class="fileInput" type="file" name="File" id="file" ><i class="fa fa-upload" aria-hidden="true"></i>&nbsp;Upload
												</div>
											</div>
										</div>
									<%}%>
								</div>
							</div>
						</div>
					</div> 
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered" id="example">
							<thead id="tbsearch">
								<tr>
									<th>CoCd</th>
									<th>Contract Ref#</th>
									<th>EMS Reference#</th>
									<th>Year</th>
									<th>Billing Peroid</th>
									<th>Month</th>
									<!-- <th>Vendor Cd</th> -->
									<th>GL</th>
									<th>GL Description</th>
									<th>Currency</th>
									<th>Doc.Type</th>
									<th>P/Ky</th>
									<th>SAP Document No</th>
									<th>GL Line Item- Text</th>
									<th>Transaction Type</th>
									<th>User Name</th>
									<th title="EMS Value">Amt in Loc -USD</th>
									<th title="EMS Value">Amt in Doc Currency-INR</th>
									<th>Document Date</th>
									<th>Posting Date</th>
									<th>Quantity</th>
									<th>SAP Code</th>
									<th>Vendor Name</th>
									<th title="SAP Value"  style="background: #000066; color: white;">Amt in Loc -USD</th>
									<th title="SAP Value"  style="background: #000066; color: white;">Amt in Loc-INR</th>
									<!-- <th>SAP Value</th> -->
									<th title="EMS-SAP">Difference in Loc -USD</th>
									<th title="EMS-SAP">Difference in Loc -INR</th>
								</tr>
							</thead>
							<tbody>
								<%if(VCOUNTERPARTY_CD.size()>0){ %>
								<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
								<tr <%if(VIS_SAP_DOC_DATA.elementAt(i).equals("Y")){%>style="background-color: #ccffcc;"<%}%>>
									<td align="center"><%=VCO_CD.elementAt(i)%></td>
									<td align="center"><%=VDEAL_MAP.elementAt(i)%></td>
									<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
									<td align="center"><%=VACCOUNT_PERIOD_YR.elementAt(i)%></td>
									<td align="center"><%=VPERIOD_START_DT.elementAt(i)%>-<%=VPERIOD_END_DT.elementAt(i)%></td>
									<td align="center"><%=VACCOUNT_PERIOD_MONTH.elementAt(i) %></td>
									<%-- <td align="center"><%=VVENDOR_CD.elementAt(i) %></td> --%>
									<td align="center"><%=VGL_CD.elementAt(i)%></td>
									<td align="center"><%=VGL_DESC.elementAt(i) %></td>
									<td align="center"><%=VCURRENCY.elementAt(i) %></td>
									<td align="center"><%=VDOC_TYPE.elementAt(i) %></td>
									<td align="center"><%=VPK.elementAt(i) %></td>
									<td align="center"><%=VDOC_NO.elementAt(i) %></td>
									<td align="center"><%=VITEMTEXT.elementAt(i) %></td>
									<td align="center"><%=VTRANS_TYPE.elementAt(i) %></td>
									<td align="center"><%=VSAP_APPROVED_BY.elementAt(i) %></td>
									<td align="right"><%=VUSD_AMT.elementAt(i) %></td>
									<td align="right"><%=VAMT.elementAt(i) %></td>
									<td align="center"><%=VDOC_DT.elementAt(i) %></td>
									<td align="center"><%=VPOST_DT.elementAt(i) %></td>
									<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
									<td align="center"><%=VVENDOR_CD.elementAt(i) %></td>
									<td align="center"><%=VVENDOR_NM.elementAt(i) %></td>
									<td align="right"><%=VSAP_VALUE_USD.elementAt(i) %></td>
									<td align="right"><%=VSAP_VALUE_INR.elementAt(i) %></td>
									<td align="right" <%if(!VDELTA_SAP_VALUE_USD.elementAt(i).equals("") && (Double.parseDouble(VDELTA_SAP_VALUE_USD.elementAt(i).toString())!=0)){%>style="color:red;"<%}%>><%=VDELTA_SAP_VALUE_USD.elementAt(i) %></td>
									<td align="right" <%if(!VDELTA_SAP_VALUE_INR.elementAt(i).equals("") && (Double.parseDouble(VDELTA_SAP_VALUE_INR.elementAt(i).toString())!=0)){%>style="color:red;"<%}%>><%=VDELTA_SAP_VALUE_INR.elementAt(i) %></td>
								</tr>
								<%} %>
								<tr>
									<td colspan="15" align="right">Total:</td>
									<td align="right"><%=amt_usd_sum %></td>
									<td align="right"><%=amt_sum %></td>
									<td colspan="2"></td>
									<td align="right"><%=qty_sum %></td>
									<td colspan="2"></td>
									<td align="right"><%=sap_sum_usd%></td>
									<td align="right"><%=sap_sum %></td>
									<td align="right"><%=delta_sap_sum_usd %></td>
									<td align="right"><%=delta_sap_sum %></td>
								</tr>
								<%}else{%>
									<tr>
										<td colspan="26" align="center"><%=utilmsg.infoMessage("<b>No Invoice Data Found!</b>") %></td>
									</tr>
								<%}%>
							</tbody>
						</table>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div align="center"><%=utilmsg.infoMessage("<b>Note: The date filter will show result with respect to Posting Date!</b>") %></div>
   					</div>
   				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="perv_entity_role" value="<%=entity_role%>">
<input type="hidden" name="option" id="option" value="UPLOAD_SAP_XLS">
<input type="hidden" name="opration" value="">


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

$(document).ready(function() {
	
	$('#tbsearch th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "Sr#")
		{
			//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
		}
		else
		{
			$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:100px"/></div>');
		}
	});
	
});

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
    	//tbody=tr[i].getElementsByTagName("tbody");alert(tbody)
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