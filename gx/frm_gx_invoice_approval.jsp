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
	var segment = ""//document.forms[0].segment.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_gx_invoice_approval.jsp?u="+u+"&segment="+segment+"&from_dt="+from_dt+"&to_dt="+to_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var newWindow;
function openSapView(financial_year,invoice_seq,contract_type)
{
	var url = "rpt_view_gx_txn_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+"&contract_type="+contract_type;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"GX Transaction Charge SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"GX Transaction Charge SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function doExchngSubmit(index,financial_year,invoice_seq,contract_type,exchng_flag,type_flag,invoice_type)
{
	var sap_exchng_dt=document.getElementById("sap_exchang_dt"+index).value
	var sap_exchng_rate=document.getElementById("sap_exchang_rate"+index).value
	
	if(trim(sap_exchng_rate)!="")
	{
		var a=confirm("Do you want to apply this Exchange rate?")
		if(a)
		{
			document.forms[0].financial_year.value=financial_year;
			document.forms[0].invoice_seq.value=invoice_seq;
			document.forms[0].contract_type.value=contract_type;
			document.forms[0].exchng_dt.value=sap_exchng_dt;
			document.forms[0].exchng_rate.value=sap_exchng_rate;
			document.forms[0].sap_exchng_flag.value=exchng_flag;
			document.forms[0].type_flag.value=type_flag;
			document.forms[0].invoice_type.value=invoice_type;
						
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert("Enter Exchange Rate for ROW - "+(parseInt(index)+1))
	}
}

function doGenXML(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,type_flag,invoice_type, 
					sap_approval_flag, gx_counterparty_cd,bu_plant_seq, agmt_no, cont_no)
{
	var url = "rpt_view_gx_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
			"&contract_type="+contract_type+"&type_flag="+type_flag+"&invoice_type="+invoice_type+"&gx_counterparty_cd="+gx_counterparty_cd+
			"&counterparty_cd="+couterpty_cd+"&invoice_no="+invoice_no+"&sap_approval_flag="+sap_approval_flag+
			"&bu_plant_seq="+bu_plant_seq+"&agmt_no="+agmt_no+"&cont_no="+cont_no;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Gas Exchange SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Gas Exchange SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var segment = ""//document.forms[0].segment.value;
	
	var url = "xls_gx_invoice_approval.jsp?segment="+segment+"&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.gx.DataBean_Gx_Invoice" id="gx_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");

gx_inv.setCallFlag("GX_REMITTANCE_FO_APPROVAL");
gx_inv.setComp_cd(owner_cd);
//gx_inv.setSegment(segment);
gx_inv.setFrom_dt(from_dt);
gx_inv.setTo_dt(to_dt);
gx_inv.init();

Vector VSEGMENT = gx_inv.getVSEGMENT();
Vector VSEGMENT_TYPE = gx_inv.getVSEGMENT_TYPE();

Vector VINVOICE_DT = gx_inv.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = gx_inv.getVINVOICE_DUE_DT();
Vector VINVOICE_NO = gx_inv.getVINVOICE_NO();
Vector VINVOICE_SEQ = gx_inv.getVINVOICE_SEQ();
//Vector VPERIOD_START_DT = gx_inv.getVPERIOD_START_DT();
//Vector VPERIOD_END_DT = gx_inv.getVPERIOD_END_DT();
Vector VALLOC_QTY = gx_inv.getVALLOC_QTY();
Vector VTXN_RATE = gx_inv.getVTXN_RATE();
Vector VRATE_UNIT = gx_inv.getVRATE_UNIT();
Vector VGROSS_AMT = gx_inv.getVGROSS_AMT();
Vector VTAX_AMT = gx_inv.getVTAX_AMT();
Vector VINVOICE_AMT = gx_inv.getVINVOICE_AMT();
//Vector VEXCHNAGE_RATE = gx_inv.getVEXCHNAGE_RATE();
//Vector VEXCHNAGE_RATE_DATE = gx_inv.getVEXCHNAGE_RATE_DATE();
Vector VBU_PLANT_SEQ = gx_inv.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = gx_inv.getVBU_PLANT_ABBR();
Vector VDEAL_NO = gx_inv.getVDEAL_NO();
//Vector VSALE_AMT = gx_inv.getVSALE_AMT();
Vector VADJ_SIGN = gx_inv.getVADJ_SIGN();
Vector VADJ_AMT = gx_inv.getVADJ_AMT();
Vector VNET_PAYABLE = gx_inv.getVNET_PAYABLE();
Vector VTCS_TDS = gx_inv.getVTCS_TDS();
Vector VTCS_TDS_AMT = gx_inv.getVTCS_TDS_AMT();
Vector VTCS_TDS_FACTOR = gx_inv.getVTCS_TDS_FACTOR();
Vector VINVOICE_RAISED_IN = gx_inv.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = gx_inv.getVPAYMENT_DONE_IN();
Vector VFINANCIAL_YEAR = gx_inv.getVFINANCIAL_YEAR();
Vector VCONTRACT_TYPE = gx_inv.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = gx_inv.getVCONTRACT_TYPE_NM();
//Vector VSAP_EXCHANG_FLAG = gx_inv.getVSAP_EXCHANG_FLAG();
Vector VSAP_APPROVAL_FLAG = gx_inv.getVSAP_APPROVAL_FLAG();

Vector VINVOICE_TYPE =gx_inv.getVINVOICE_TYPE();
Vector VTYPE_FLAG = gx_inv.getVTYPE_FLAG();
Vector VTYPE_NM= gx_inv.getVTYPE_NM();
Vector VCASH_FLOW = gx_inv.getVCASH_FLOW();

/* Vector VGROSS_AMT_USD = gx_inv.getVGROSS_AMT_USD();
Vector VTAX_AMT_USD = gx_inv.getVTAX_AMT_USD();
Vector VINVOICE_AMT_USD = gx_inv.getVINVOICE_AMT_USD();
Vector VADJ_AMT_USD = gx_inv.getVADJ_AMT_USD();
Vector VNET_PAYABLE_USD = gx_inv.getVNET_PAYABLE_USD();
Vector VTCS_TDS_AMT_USD = gx_inv.getVTCS_TDS_AMT_USD(); */
Vector VTCS_TDS_STRUCT_CD = gx_inv.getVTCS_TDS_STRUCT_CD();
Vector VTCS_TDS_EFF_DT = gx_inv.getVTCS_TDS_EFF_DT();
//Vector VTCS_TDS_DONE = gx_inv.getVTCS_TDS_DONE();

Vector VCOUNTERPARTY_CD = gx_inv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = gx_inv.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = gx_inv.getVCOUNTERPARTY_NM();
Vector VINDEX = gx_inv.getVINDEX();
Vector VGX_COUNTERPTY_CD = gx_inv.getVGX_COUNTERPTY_CD();
Vector VGX_COUNTERPTY_ABBR = gx_inv.getVGX_COUNTERPTY_ABBR();

Vector VCONT_NO = gx_inv.getVCONT_NO();
Vector VAGMT_NO = gx_inv.getVAGMT_NO();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Gx_Invoice">

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
					    	Transaction Charge Actuals Report
					    </div>
					    <%-- <div class="btn-group">
							<select class="btn btn-outline-secondary btngrp btnactive" name="segment" onchange="refresh();">
								<option value="">All</option>
								<%for(int i=0;i<VSEGMENT.size();i++){ %>
								<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT.elementAt(i)%></option>
								<%} %>
							</select>
						</div>
						<script>document.forms[0].segment.value="<%=segment%>"</script> --%>
						<div align="right" onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>From</b></label>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
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
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%int i=0;int k=0;
					for(int j=0; j<VSEGMENT_TYPE.size(); j++){ 
						int index = Integer.parseInt(""+VINDEX.elementAt(j));
					if(j!=0)
					{%>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div> 
					<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o">
							</i> Transaction Charge Remittance [<%=VSEGMENT.elementAt(j) %>]
							<span style="color:blue;">(<%=VINDEX.elementAt(j)%> Items)</span></label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th >Sr#</th>
										<th >Type</th>
										<th >On behalf of Counterparty</th>
										<th ><%=owner_abbr%> BU</th>
										<th >Contract Type</th>
										<th >Contract#</th>
										<th >Cash Flow</th>
										<th >Invoice#</th>
										<th >Invoice Date</th>
										<th >Invoice Due Date</th>
										<th >Invoiced MMBTU</th>
										<th >Rate</th>
										<th  style="background: #000066; color: white;">Rate Unit</th>
										<th  style="background: #000066; color: white;">Invoice Raised In</th>
										<th  style="background: #000066; color: white;">Invoice Paid In</th>
										<th >Gross Amount</th>
										<th >Tax</th>
										<th >Invoice Amount</th>
										<th >TCS/TDS</th>
										<th >TCS/TDS %</th>
										<th >+TCS/-TDS Amount</th>
										<th >Adjust Amount</th>
										<th >Net Payable</th>
										<!-- <th >SAP Approval</th> -->
									</tr>									
								</thead>
								<tbody>
								<%k=0;
								if(index > 0){ %>
									<%for(i=i; i<VINVOICE_SEQ.size(); i++){ 
										k+=1;
									%>
									<tr>
										<td align="center"><%=k %></td>
										<td align="center"><%=VTYPE_NM.elementAt(i) %></td>
										<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(i) %>"><%=VCOUNTERPARTY_ABBR.elementAt(i) %></td>
										<td align="center"><%=VBU_PLANT_ABBR.elementAt(i) %></td>
										<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i) %></td>
										<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
										<td align="center"><%=VCASH_FLOW.elementAt(i) %></td>
										<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
										<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
										<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
										<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
										<td align="right"><%=VTXN_RATE.elementAt(i) %></td>
										<td align="center" style="background: #b3f0ff;"><%=VRATE_UNIT.elementAt(i) %></td>
										<td align="center" style="background: #b3f0ff;"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>																									
										<td align="center" style="background: #b3f0ff;"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
										<td align="right"><%=VGROSS_AMT.elementAt(i) %></td>
										<td align="right"><%=VTAX_AMT.elementAt(i) %></td>
										<td align="right"><%=VINVOICE_AMT.elementAt(i) %></td>
										<td align="center"><%=VTCS_TDS.elementAt(i) %></td>
										<td align="center"><%=VTCS_TDS_FACTOR.elementAt(i)%></td>
										<td align="right"><%=VTCS_TDS_AMT.elementAt(i) %></td>
										<td align="right"><%=VADJ_SIGN.elementAt(i)%><%=VADJ_AMT.elementAt(i)%></td>
										<td align="right"><%=VNET_PAYABLE.elementAt(i)%></td>
										<%-- <td align="center">											
											<%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
												<input type="button" class="btn btn-warning com-btn" value="Approve"
											 	onclick="doGenXML('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
												 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
												 '<%=VTYPE_FLAG.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','<%=VSAP_APPROVAL_FLAG.elementAt(i)%>',
												 '<%=VGX_COUNTERPTY_CD.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>');"
												 >
											<%}else if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")){ %>
												<i class="fa <%if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")) {%>fa-eye<%} %> fa-2x"
												onclick="doGenXML('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
												 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
												 '<%=VTYPE_FLAG.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','<%=VSAP_APPROVAL_FLAG.elementAt(i)%>',
												 '<%=VGX_COUNTERPTY_CD.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>');"
												 ></i>
											<%} %>	 
										</td>		 --%>								
									</tr>
									<%
										if(k==index)
										{
											i=i+1;
											break;
										}
									} %>
								<%}else{ %>
									<tr>
										<td colspan="23" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="financial_year" value="">
<input type="hidden" name="invoice_seq" value="">
<input type="hidden" name="contract_type" value="">
<input type="hidden" name="exchng_dt" value="">
<input type="hidden" name="exchng_rate" value="">
<input type="hidden" name="sap_exchng_flag" value="">
<input type="hidden" name="tdsFactor" value="">
<input type="hidden" name="tdsAmount" value="">
<input type="hidden" name="tdsStructCd" value="">
<input type="hidden" name="tdsEffDt" value="">
<input type="hidden" name="type_flag" value="">
<input type="hidden" name="invoice_type" value="">

<input type="hidden" name="option" value="SAP_EXCHANGE_RATE_UPDATE">

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