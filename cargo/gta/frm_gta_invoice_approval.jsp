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
	
	var url = "frm_gta_invoice_approval.jsp?u="+u+"&segment="+segment+"&from_dt="+from_dt+"&to_dt="+to_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var newWindow;
function openSapView(financial_year,invoice_seq,contract_type)
{
	var url = "rpt_view_gx_txn_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+"&contract_type="+contract_type;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"GTA Invoice SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"GTA Invoice SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function doExchngSubmit(index,financial_year,invoice_seq,contract_type,exchng_flag,type_flag,invoice_type)
{
	var sap_exchng_dt=document.getElementById("sap_exchang_dt"+index).value
	var sap_exchng_rate=document.getElementById("sap_exchang_rate"+index).value
	
	/* var tds_factor=document.getElementById("tds_factor"+index).value
	var tds_amt=document.getElementById("tds_amt"+index).value
	var tds_struct_cd=document.getElementById("tds_struct_cd"+index).value
	var tds_eff_dt=document.getElementById("tds_eff_dt"+index).value */
	
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
			
			/* document.forms[0].tdsFactor.value=tds_factor;
			document.forms[0].tdsAmount.value=tds_amt;
			document.forms[0].tdsStructCd.value=tds_struct_cd;
			document.forms[0].tdsEffDt.value=tds_eff_dt */
			
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert("Enter Exchange Rate for ROW - "+(parseInt(index)+1))
	}
}

function doGenXML(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,type_flag,invoice_type,sap_approval_flag, bu_seq, agmt_no, cont_no)
{
	var url = "rpt_view_gta_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
			"&contract_type="+contract_type+"&type_flag="+type_flag+"&invoice_type="+invoice_type+
			"&counterparty_cd="+couterpty_cd+"&invoice_no="+invoice_no+"&sap_approval_flag="+sap_approval_flag+
			"&bu_seq="+bu_seq+"&agmt_no="+agmt_no+"&cont_no="+cont_no;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"GTA SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"GTA SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var segment = ""//document.forms[0].segment.value;
	
	var url = "xls_gta_invoice_approval.jsp?segment="+segment+"&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GTA_Remittance" id="remittance" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");

remittance.setCallFlag("GTA_REMITTANCE_FO_APPROVAL");
remittance.setComp_cd(owner_cd);
//remittance.setSegment(segment);
remittance.setFrom_dt(from_dt);
remittance.setTo_dt(to_dt);
remittance.init();

Vector VSEGMENT = remittance.getVSEGMENT();
Vector VSEGMENT_TYPE = remittance.getVSEGMENT_TYPE();

Vector VINVOICE_DT = remittance.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = remittance.getVINVOICE_DUE_DT();
Vector VINVOICE_NO = remittance.getVINVOICE_NO();
Vector VINVOICE_SEQ = remittance.getVINVOICE_SEQ();
//Vector VPERIOD_START_DT = remittance.getVPERIOD_START_DT();
//Vector VPERIOD_END_DT = remittance.getVPERIOD_END_DT();
Vector VALLOC_QTY = remittance.getVALLOC_QTY();
Vector VTXN_RATE = remittance.getVTXN_RATE();
Vector VRATE_UNIT = remittance.getVRATE_UNIT();
Vector VGROSS_AMT = remittance.getVGROSS_AMT();
Vector VTAX_AMT = remittance.getVTAX_AMT();
Vector VINVOICE_AMT = remittance.getVINVOICE_AMT();
//Vector VEXCHNAGE_RATE = remittance.getVEXCHNAGE_RATE();
//Vector VEXCHNAGE_RATE_DATE = remittance.getVEXCHNAGE_RATE_DATE();
Vector VBU_PLANT_ABBR = remittance.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ = remittance.getVBU_PLANT_SEQ();
Vector VDEAL_NO = remittance.getVDEAL_NO();
//Vector VSALE_AMT = remittance.getVSALE_AMT();
Vector VADJ_SIGN = remittance.getVADJ_SIGN();
Vector VADJ_AMT = remittance.getVADJ_AMT();
Vector VNET_PAYABLE = remittance.getVNET_PAYABLE();
Vector VTCS_TDS = remittance.getVTCS_TDS();
Vector VTCS_TDS_AMT = remittance.getVTCS_TDS_AMT();
Vector VTCS_TDS_FACTOR = remittance.getVTCS_TDS_FACTOR();
Vector VINVOICE_RAISED_IN = remittance.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = remittance.getVPAYMENT_DONE_IN();
Vector VFINANCIAL_YEAR = remittance.getVFINANCIAL_YEAR();
Vector VCONTRACT_TYPE = remittance.getVCONTRACT_TYPE();
Vector VSAP_APPROVAL_FLAG = remittance.getVSAP_APPROVAL_FLAG();

Vector VINVOICE_TYPE =remittance.getVINVOICE_TYPE();
Vector VTYPE_FLAG = remittance.getVTYPE_FLAG();
Vector VTYPE_NM= remittance.getVTYPE_NM();

Vector VTCS_TDS_STRUCT_CD = remittance.getVTCS_TDS_STRUCT_CD();
Vector VTCS_TDS_EFF_DT = remittance.getVTCS_TDS_EFF_DT();

Vector VCOUNTERPARTY_CD = remittance.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = remittance.getVCOUNTERPARTY_ABBR();
Vector VINDEX = remittance.getVINDEX();
Vector V_TYPE_NM = remittance.getV_TYPE_NM();

Vector VCONT_NO = remittance.getVCONT_NO();
Vector VAGMT_NO = remittance.getVAGMT_NO();
Vector VCASH_FLOW= remittance.getVCASH_FLOW();
Vector VREMITTANCE_NO = remittance.getVREMITTANCE_NO();


%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_GTA_Remittance">

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
					    	GTA Remittance Actuals Report
					    </div>
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
				<div class="row m-b-5">
					<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;GTA Remittance</label>
				</div>
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
					
					 <h2 class="accordion-header" id="heading<%=j%>">
						<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="false" aria-controls="collapse<%=j%>">
			    			GTA Remittance [<%=VSEGMENT.elementAt(j) %>] <span style="color:blue;">(<%=VINDEX.elementAt(j)%> Items)</span>
			      		</button>
			    	</h2>
					<div id="collapse<%=j%>" class="accordion-collapse collapse" aria-labelledby="heading<%=j%>">
			      		<div class="accordion-body accor-body">	
							<div class="row">
								<div class="table-responsive">
									<table class="table table-bordered table-hover" id="example">
										<thead>
											<tr>
												<th>Sr#</th>
												<th>Type</th>
												<th>Transporter</th>
												<th><%=owner_abbr%> BU</th>
												<th>Contract#</th>
												<th>Cash Flow</th>
												<th>Invoice#</th>
												<th>Remittance#</th>
												<th>Invoice Date</th>
												<th>Invoice Due Date</th>
												<th style="background: #000066; color: white;">Invoice Raised In</th>
												<th style="background: #000066; color: white;">Invoice Paid In</th>
												<th>Gross Amount</th>
												<th>Tax</th>
												<th>Invoice Amount</th>
												<th>TCS/TDS</th>
												<th>TCS/TDS %</th>
												<th>+TCS/-TDS Amount</th>
												<th>Adjust Amount</th>
												<th>Net Payable</th>
											</tr>									
										</thead>
										<tbody>
										<%k=0;
										if(index > 0){ %>
											<%for(i=i; i<VINVOICE_SEQ.size(); i++){ 
												k+=1;
											%>
											<tr>
												<td align="right"><%=k %></td>	
												<td align="center"><%=VTYPE_NM.elementAt(i) %></td>
												<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i) %></td>
												<td align="center"><%=VBU_PLANT_ABBR.elementAt(i) %></td>
												<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
												<td align="center"><%=VCASH_FLOW.elementAt(i) %></td>
												<%-- <td align="center"><%=VPERIOD_START_DT.elementAt(i) %> - <%=VPERIOD_END_DT.elementAt(i) %></td> --%>
												<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
												<td align="center"><%=VREMITTANCE_NO.elementAt(i) %></td>
												<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
												<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
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
												<td colspan="30" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
											</tr>
										<%} %>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					<%} %>&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>&nbsp;GTA Free Flow Remittance</label>
					</div>
					
					<h2 class="accordion-header" id="heading11">
						<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse11" aria-expanded="false" aria-controls="collapse11">
			    			 GTA Remittance [Free Flow] <span style="color:blue;">(<%=V_TYPE_NM.size()%> Items)</span>
			      		</button>
			    	</h2>
			    	<div id="collapse11" class="accordion-collapse collapse" aria-labelledby="heading11">
			      		<div class="accordion-body accor-body">	
							<div class="row">
								<div class="table-responsive">
									<table class="table table-bordered table-hover" id="example">
										<thead>
											<tr>
												<th>Sr#</th>
												<th>Type</th>
												<th>Transporter</th>
												<th><%=owner_abbr%> BU</th>
												<th>Contract#</th>
												<th>Cash Flow</th>
												<th>Invoice#</th>
												<th>Remittance#</th>
												<th>Invoice Date</th>
												<th>Invoice Due Date</th>
												<th style="background: #000066; color: white;">Invoice Raised In</th>
												<th style="background: #000066; color: white;">Invoice Paid In</th>
												<th>Gross Amount</th>
												<th>Tax</th>
												<th>Invoice Amount</th>
												<th>TCS/TDS</th>
												<th>TCS/TDS %</th>
												<th>+TCS/-TDS Amount</th>
												<th>Adjust Amount</th>
												<th>Net Payable</th>
											</tr>									
										</thead>
										<tbody>
										<%k=0;i=0;
										if(V_TYPE_NM.size() > 0){ %>
											<%for(i=i; i<VINVOICE_SEQ.size(); i++){
												if(VTYPE_NM.elementAt(i).equals("FFLOW"))
												{
												k+=1;
											%>
											<tr>
												<td align="right"><%=k %></td>	
												<td align="center"><%=VTYPE_NM.elementAt(i) %></td>
												<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i) %></td>
												<td align="center"><%=VBU_PLANT_ABBR.elementAt(i) %></td>
												<td align="center"><%=VDEAL_NO.elementAt(i) %></td>
												<td align="center"><%=VCASH_FLOW.elementAt(i) %></td>
												<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
												<td align="center"><%=VREMITTANCE_NO.elementAt(i) %></td>
												<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
												<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
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
											</tr>
											<%
											} 
										}%>
										<%}else{ %>
											<tr>
												<td colspan="30" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
											</tr>
										<%} %>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>&nbsp;
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