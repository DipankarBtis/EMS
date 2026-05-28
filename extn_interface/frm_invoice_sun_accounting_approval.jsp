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
	var segment = document.forms[0].segment.value;
	
	var u = document.forms[0].u.value;
	
	var url = "../extn_interface/frm_invoice_sun_accounting_approval.jsp?u="+u+"&segment="+segment+
			"&from_dt="+from_dt+"&to_dt="+to_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function enable_disable(index)
{
	var chk = document.getElementById("chk"+index);
	
	var counterparty_cd = document.getElementById("counterparty_cd"+index);
	var agmt_no = document.getElementById("agmt_no"+index);
	var agmt_rev_no = document.getElementById("agmt_rev_no"+index);
	var cont_no = document.getElementById("cont_no"+index);
	var cont_rev_no = document.getElementById("cont_rev_no"+index);
	var contract_type = document.getElementById("contract_type"+index);
	var financial_year = document.getElementById("financial_year"+index);
	var invoice_seq = document.getElementById("invoice_seq"+index);
	var bu_state_tin = document.getElementById("bu_state_tin"+index);
	var tds_on_gross = document.getElementById("tds_on_gross"+index);
	var tds_on_gross_amt = document.getElementById("tds_on_gross_amt"+index);
	var tds_on_tax = document.getElementById("tds_on_tax"+index);
	var tds_on_tax_amt = document.getElementById("tds_on_tax_amt"+index);
	var net_payable_amt = document.getElementById("net_payable_amt"+index);
	var pay_received_amt = document.getElementById("pay_received_amt"+index);
	var short_received = document.getElementById("short_received"+index);
	var pay_received_dt = document.getElementById("pay_received_dt"+index);
	var remark = document.getElementById("remark"+index);
	
	if(chk.checked)
	{
		counterparty_cd.disabled=false;
		agmt_no.disabled=false;
		agmt_rev_no.disabled=false;
		cont_no.disabled=false;
		cont_rev_no.disabled=false;
		contract_type.disabled=false;
		financial_year.disabled=false;
		invoice_seq.disabled=false;
		bu_state_tin.disabled=false;
		tds_on_gross.disabled=false;
		tds_on_gross_amt.disabled=false;
		tds_on_tax.disabled=false;
		tds_on_tax_amt.disabled=false;
		net_payable_amt.disabled=false;
		pay_received_amt.disabled=false;
		short_received.disabled=false;
		pay_received_dt.disabled=false;
		remark.disabled=false;
	}
	else
	{
		counterparty_cd.disabled=true;
		agmt_no.disabled=true;
		agmt_rev_no.disabled=true;
		cont_no.disabled=true;
		cont_rev_no.disabled=true;
		contract_type.disabled=true;
		financial_year.disabled=true;
		invoice_seq.disabled=true;
		bu_state_tin.disabled=true;
		tds_on_gross.disabled=true;
		tds_on_gross_amt.disabled=true;
		tds_on_tax.disabled=true;
		tds_on_tax_amt.disabled=true;
		net_payable_amt.disabled=true;
		pay_received_amt.disabled=true;
		short_received.disabled=true;
		pay_received_dt.disabled=true;
		remark.disabled=true;
	}
}

function calcPayableAmt(index)
{
	var gross_amt = document.getElementById("gross_amt"+index);
	var tds_on_gross = document.getElementById("tds_on_gross"+index);
	var tds_on_gross_amt = document.getElementById("tds_on_gross_amt"+index);
	
	var tax_amt = document.getElementById("tax_amt"+index);
	var tds_on_tax = document.getElementById("tds_on_tax"+index);
	var tds_on_tax_amt = document.getElementById("tds_on_tax_amt"+index);
	
	var invoice_amt = document.getElementById("invoice_amt"+index);
	var net_payable_amt = document.getElementById("net_payable_amt"+index);
	var pay_received_amt = document.getElementById("pay_received_amt"+index);
	var short_received = document.getElementById("short_received"+index);
	
	//CALC tds on gross

	if(trim(gross_amt.value)!="" && trim(tds_on_gross.value)!="")
	{
		var tds_amt = parseFloat("0");
		
		tds_amt=parseFloat(gross_amt.value) * parseFloat(tds_on_gross.value) / 100;
		
		if(!isNaN(tds_amt))
		{
			tds_on_gross_amt.value=round(parseFloat(tds_amt),2)
		}
		else
		{
			tds_on_gross_amt.value="";
		}
	}
	else
	{
		tds_on_gross_amt.value="";
	}
	
	//calc tds on tax
	if(trim(tax_amt.value)!="" && trim(tds_on_tax.value)!="")
	{
		var tds_amt = parseFloat("0");
		
		tds_amt=parseFloat(tax_amt.value) * parseFloat(tds_on_tax.value) / 100;
		
		if(!isNaN(tds_amt))
		{
			tds_on_tax_amt.value=round(parseFloat(tds_amt),2)
		}
		else
		{
			tds_on_tax_amt.value="";
		}
	}
	else
	{
		tds_on_tax_amt.value="";
	}
	
	//calc net receivable
	if(trim(invoice_amt.value)!="")
	{
		var net_recv_amt = parseFloat(invoice_amt.value);
		
		if(trim(tds_on_gross.value)!="")
		{
			net_recv_amt=parseFloat(net_recv_amt) - parseFloat(tds_on_gross_amt.value);
		}
		
		if(trim(tds_on_tax.value)!="")
		{
			net_recv_amt=parseFloat(net_recv_amt) - parseFloat(tds_on_tax_amt.value);
		}
		
		if(!isNaN(net_recv_amt))
		{
			net_payable_amt.value=round(parseFloat(net_recv_amt),2)
		}
		else
		{
			net_payable_amt.value="";
		}
	}
	
	//calc short receive
	if(trim(pay_received_amt.value)!="")
	{
		var short_amt = round(parseFloat(net_payable_amt.value)-parseFloat(pay_received_amt.value),2);
		
		if(!isNaN(short_amt))
		{
			short_received.value=round(parseFloat(short_amt),2)
		}
		else
		{
			short_received.value="";
		}
	}
	else
	{
		short_received.value=net_payable_amt.value;
	}
}

function doSubmit()
{
	var chk = document.forms[0].chk;
	
	var pay_received_amt = document.forms[0].pay_received_amt;
	var pay_received_dt = document.forms[0].pay_received_dt;
	
	var msg="";
	var flag=true;
	
	var countChk=parseInt("0");
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					countChk=parseInt(countChk) + 1;
					
					if(trim(pay_received_amt[i].value) == "")
					{
						msg+="Enter Actual Received for ROW - "+(praseInt(i)+1)+"!\n";
						flag=false;
					}
					if(trim(pay_received_dt[i].value) == "")
					{
						msg+="Enter Received Date for ROW - "+(praseInt(i)+1)+"!\n";
						flag=false;
					}
				}
			}
		}
		else
		{
			if(chk.checked)
			{
				countChk=parseInt(countChk) + 1;
				
				if(trim(pay_received_amt.value) == "")
				{
					msg+="Enter Actual Received for ROW - 1!\n";
					flag=false;
				}
				if(trim(pay_received_dt.value) == "")
				{
					msg+="Enter Received Date for ROW - 1!\n";
					flag=false;
				}
			}
		}
	}
	
	if(parseInt(countChk)<=0)
	{
		alert("Please Select atleast one(1) ROW for Submit!");
	}
	else
	{
		if(flag)
		{
			var a = confirm("Do you want to Submit?")
			if(a)
			{
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].submit();
			}
		}
		else
		{
			alert(msg);
		}
	}
}

var newWindow;

function doGenXML(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,type_flag,invoice_type,bu_state_tin,sap_approval_flag,agmt_no,cont_no)
{
	var a=confirm("Do you want to approve invoice for SUN XML generation?")
	if(a)
	{
		document.forms[0].counterparty_cd.value=couterpty_cd;
		document.forms[0].invoice_no.value=invoice_no;
		document.forms[0].financial_year.value=financial_year;
		document.forms[0].invoice_seq.value=invoice_seq;
		document.forms[0].contract_type.value=contract_type;
		document.forms[0].type_flag.value=type_flag;
		document.forms[0].invoice_type.value=invoice_type;
		document.forms[0].bu_state_tin.value=bu_state_tin;
		
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].option.value="APPROVE_SALE_INVOICE_SUN_XML"
		document.forms[0].submit();
		
	}
	//var u = document.forms[0].u.value;
	
	/* var url = "../accounting/rpt_view_sales_invoice_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
			"&contract_type="+contract_type+"&type_flag="+type_flag+"&invoice_type="+invoice_type+
			"&counterparty_cd="+couterpty_cd+"&invoice_no="+invoice_no+"&bu_state_tin="+bu_state_tin+
			"&sap_approval_flag="+sap_approval_flag+"&agmt_no="+agmt_no+"&cont_no="+cont_no+"&gen_type=S";

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Sales SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Sales SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	} */
}

function doReApproveXML(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,type_flag,invoice_type,bu_state_tin,sap_approval_flag,agmt_no,cont_no)
{
	var a=confirm("Do you want to re-approve invoice for SUN XML generation?");
	if(a)
	{
		document.forms[0].counterparty_cd.value=couterpty_cd;
		document.forms[0].invoice_no.value=invoice_no;
		document.forms[0].financial_year.value=financial_year;
		document.forms[0].invoice_seq.value=invoice_seq;
		document.forms[0].contract_type.value=contract_type;
		document.forms[0].type_flag.value=type_flag;
		document.forms[0].invoice_type.value=invoice_type;
		document.forms[0].bu_state_tin.value=bu_state_tin;
		
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].option.value="RE-APPROVE_SALE_INVOICE_SUN_XML"
		document.forms[0].submit();
	}
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var segment = document.forms[0].segment.value;
	
	var url = "xls_invoice_accounting_approval.jsp?segment="+segment+"&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}
</script>

</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sun_interface" id="accounting" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");

accounting.setCallFlag("INVOICE_SUN_APPROVAL");
accounting.setComp_cd(owner_cd);
accounting.setFrom_dt(from_dt);
accounting.setTo_dt(to_dt);
accounting.setSegment(segment);
accounting.init();

Vector VSEGMENT = accounting.getVSEGMENT();
Vector VSEGMENT_TYPE = accounting.getVSEGMENT_TYPE();
Vector VTEMP_SEGMENT = accounting.getVTEMP_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = accounting.getVTEMP_SEGMENT_TYPE();
Vector VBU_NM = accounting.getVBU_NM();
Vector VCOUNTERPARTY_CD = accounting.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = accounting.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = accounting.getVCOUNTERPARTY_ABBR();
//Vector VMST_COUNTERPARTY_CD = accounting.getVMST_COUNTERPARTY_CD();
//Vector VMST_COUNTERPARTY_NM = accounting.getVMST_COUNTERPARTY_NM();
//Vector VMST_COUNTERPARTY_ABBR = accounting.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = accounting.getVCONT_NO();
Vector VCONT_REV_NO = accounting.getVCONT_REV_NO();
Vector VAGMT_NO = accounting.getVAGMT_NO();
Vector VAGMT_REV_NO = accounting.getVAGMT_REV_NO();
Vector VDIS_CONT_MAPPING = accounting.getVDIS_CONT_MAPPING();
Vector VCONTRACT_TYPE = accounting.getVCONTRACT_TYPE();
Vector VPERIOD_START_DT = accounting.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = accounting.getVPERIOD_END_DT();
Vector VFINANCIAL_YEAR = accounting.getVFINANCIAL_YEAR();
Vector VINVOICE_NO = accounting.getVINVOICE_NO();
Vector VINVOICE_SEQ = accounting.getVINVOICE_SEQ();
Vector VBU_STATE_TIN = accounting.getVBU_STATE_TIN();
Vector VINVOICE_DT = accounting.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = accounting.getVINVOICE_DUE_DT();
Vector VSALES_PRICE = accounting.getVSALES_PRICE();
Vector VSALES_PRICE_CD = accounting.getVSALES_PRICE_CD();
Vector VSALES_PRICE_NM = accounting.getVSALES_PRICE_NM();
Vector VGROSS_AMT = accounting.getVGROSS_AMT();
Vector VTAX_AMT = accounting.getVTAX_AMT();
Vector VINVOICE_AMT = accounting.getVINVOICE_AMT();
Vector VNET_PAYABLE_AMT = accounting.getVNET_PAYABLE_AMT();
Vector VTCS_AMT = accounting.getVTCS_AMT();
Vector VTDS_GROSS_PERCENT = accounting.getVTDS_GROSS_PERCENT();
Vector VTDS_GROSS_AMT = accounting.getVTDS_GROSS_AMT();
Vector VTDS_TAX_PERCENT = accounting.getVTDS_TAX_PERCENT();
Vector VTDS_TAX_AMT = accounting.getVTDS_TAX_AMT();
Vector VPAY_RECV_AMT = accounting.getVPAY_RECV_AMT();
Vector VPAY_RECV_DT = accounting.getVPAY_RECV_DT();
Vector VTAX_STRUCT_DTL = accounting.getVTAX_STRUCT_DTL();
Vector VTDS_TCS_FLAG = accounting.getVTDS_TCS_FLAG();
Vector VSHORT_RECEIVED = accounting.getVSHORT_RECEIVED();
Vector VSAP_APPROVAL_FLAG = accounting.getVSAP_APPROVAL_FLAG();
Vector VTYPE_FLAG = accounting.getVTYPE_FLAG();

Vector VTCS_TDS = accounting.getVTCS_TDS();
Vector VALLOC_QTY = accounting.getVALLOC_QTY();
Vector VINVOICE_RAISED_IN = accounting.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = accounting.getVPAYMENT_DONE_IN();
Vector VSALES_AMT = accounting.getVSALES_AMT();
Vector VINVOICE_TYPE=accounting.getVINVOICE_TYPE();
Vector VCONT_REF_NO= accounting.getVCONT_REF_NO();
Vector VCASH_FLOW= accounting.getVCASH_FLOW();

Vector VINDEX=accounting.getVINDEX();
Vector VAPPROVE_HIST=accounting.getVAPPROVE_HIST();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_sun_interface">

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
					    	Sales Actual SUN Approval
					    </div>
					    <div class="btn-group">
							<select class="btn btn-outline-secondary btngrp btnactive" name="segment" onchange="refresh();">
								<option value="">All</option>
								<%for(int i=0;i<VSEGMENT.size();i++){ %>
								<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT.elementAt(i)%></option>
								<%} %>
							</select>
						</div>
						<script>document.forms[0].segment.value="<%=segment%>"</script>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-10 col-xs-10 col-md-10">
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
						<!-- <div class="col-sm-2 col-xs-2 col-md-2">
							<div class="d-flex justify-content-end">
								<div class="form-group row">
									<div class="col-auto">
										<div align="right" onclick="exportToXls();" style="color:green;">
											<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
										</div>
									</div>
								</div>
							</div>
						</div> -->
					</div>
				</div>
				<div class="card-body cdbody">
				<%
					int k=0;
					int i=0;
					int l=0;			
					for(l=0;l<VTEMP_SEGMENT_TYPE.size();l++){
					int index = Integer.parseInt(""+VINDEX.elementAt(l));%>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading1">
										<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l %>" aria-expanded="false" aria-controls="collapse<%=l%>"><%=VTEMP_SEGMENT.elementAt(l)%>&nbsp;<font color="blue">(<%=index%> Items)</font></button>	
							    	</h2>
							    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading1">
							    		<div class="accordion-body accor-body">
											<div class="row">
												<div class="table-responsive">
													<table class="table table-bordered table-hover" id="example<%=l%>">
														<thead id="tbsearch<%=l%>">
															<tr>
																<th rowspan="2">SR#</th>
																<th rowspan="2">Customer</th>
																<th rowspan="2"><%=owner_abbr%> BU</th>
																<th rowspan="2">Contract#</th>
																<th rowspan="2">Contract/Trade Ref#</th>
																<th rowspan="2">Billing Period</th>
																<th rowspan="2">Cash Flow</th>
																<th rowspan="2">Invoice#</th>
																<th rowspan="2">Invoice Date</th>
																<th rowspan="2">Invoice Due Date</th>
																<th rowspan="2">Invoiced MMBTU</th>
																<th rowspan="2">Rate</th>
																<th rowspan="2" style="background: #000066; color: white;">Rate Unit / MMBTU</th>
																<th rowspan="2">Sales Amount</th>
																<th rowspan="2" style="background: #000066; color: white;">Invoice Raised In</th>
																<th rowspan="2" style="background: #000066; color: white;">Invoice Paid In</th>
																<th rowspan="2">Gross Amount</th>
																<th rowspan="2">Tax </th>
																<th rowspan="2">Invoice Amount</th>
																<!-- 20250811: AS PER VAISHNAVI FEEDBACK: NO NEED TO SHOW TDS IN APPROVAL PAGE -->
																<!-- <th rowspan="2">TCS/TDS</th>
																<th rowspan="2">TCS/TDS%</th>
																<th rowspan="2">+TCS/-TDS Amount</th> -->
																<th rowspan="2">Net Receivable</th>
																<th rowspan="2">SUN Approval</th>
																<th rowspan="2">SUN Re-Approval</th>
																<th rowspan="2">SUN Approved By/On</th>
															</tr>
														</thead>
														<tbody>
														<% k=0;
															if(index > 0){ %>
															<%for(i=i; i<VCOUNTERPARTY_CD.size(); i++){
																k+=1;%>
															<tr>
																<td align="center"><%=k%></td>
																<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%>">
																	<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
																	<%-- <input type="hidden" name="counterparty_cd" id="counterparty_cd<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>" disabled> --%>
																	<input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>" disabled>
																	<input type="hidden" name="agmt_rev_no" id="agmt_rev_no<%=i%>" value="<%=VAGMT_REV_NO.elementAt(i)%>" disabled>
																	<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>" disabled>
																	<input type="hidden" name="cont_rev_no" id="cont_rev_no<%=i%>" value="<%=VCONT_REV_NO.elementAt(i)%>" disabled>
																	<input type="hidden" name="contract_type" id="contract_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled>
																	<%-- <input type="hidden" name="financial_year" id="financial_year<%=i%>" value="<%=VFINANCIAL_YEAR.elementAt(i)%>" disabled>
																	<input type="hidden" name="invoice_seq" id="invoice_seq<%=i%>" value="<%=VINVOICE_SEQ.elementAt(i)%>" disabled> --%>
																	<%-- <input type="hidden" name="bu_state_tin" id="bu_state_tin<%=i%>" value="<%=VBU_STATE_TIN.elementAt(i)%>" disabled> --%>
																</td>
																<td align="center"><%=VBU_NM.elementAt(i) %></td>
																<td align="center"><%if(VTEMP_SEGMENT_TYPE.elementAt(l).equals("V")){%><div style="width:400px; word-wrap: break-word; white-space: normal;"><%}%><%=VDIS_CONT_MAPPING.elementAt(i) %><%if(VTEMP_SEGMENT_TYPE.elementAt(l).equals("V")){%></div><%}%></td>
																<td align="center"><%if(VTEMP_SEGMENT_TYPE.elementAt(l).equals("V")){%><div style="width:400px; word-wrap: break-word; white-space: normal;"><%}%><%=VCONT_REF_NO.elementAt(i) %><%if(VTEMP_SEGMENT_TYPE.elementAt(l).equals("V")){%></div><%} %></td>
																<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
																<td align="center"><%=VCASH_FLOW.elementAt(i)%></td>
																<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
																<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
																<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
																<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
																<td align="right"><%=VSALES_PRICE.elementAt(i)%></td>
																<td align="center" style="background: #b3f0ff;"><%=VSALES_PRICE_NM.elementAt(i) %></td>
																<td align="right"><%=VSALES_AMT.elementAt(i)%></td>
																<td style="background: #b3f0ff;"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>
																<td style="background: #b3f0ff;"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
																<td> <%=VGROSS_AMT.elementAt(i)%>
																	<input type="hidden" name="gross_amt" id="gross_amt<%=i%>" value="<%=VGROSS_AMT.elementAt(i)%>">
																</td>
																<td align="right">
																	<%=VTAX_AMT.elementAt(i)%>
																	<input type="hidden" name="tax_amt" id="tax_amt<%=i%>" value="<%=VTAX_AMT.elementAt(i)%>">
																</td>
																<td align="right">
																	<%=VINVOICE_AMT.elementAt(i) %>
																	<input type="hidden" name="invoice_amt" id="invoice_amt<%=i%>" value="<%=VINVOICE_AMT.elementAt(i)%>">
																</td>
																<!-- 20250811 AS PER VAISHNAVI FEEDBACK: NO NEED TO SHOW THE TDS COLUMN -->
																<%-- <td><%=VTCS_TDS.elementAt(i) %></td>
																<td><%=VTDS_GROSS_PERCENT.elementAt(i) %></td>
																<td><%=VTDS_GROSS_AMT.elementAt(i) %></td>	 --%>									
																<td align="right">
																	<%=VNET_PAYABLE_AMT.elementAt(i)%>
																</td>																	
																<td align="center">		
																<%if(owner_cd.equals("1")){ %>
																			<span class="fa-stack fa-lg">
																			  <i class="fa fa-flag fa-stack-1x"></i>
																			  <i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
																			</span>
																<%}else{%>										 
																	<%if(!VSAP_APPROVAL_FLAG.elementAt(i).equals("Y") && approve_access.equals("Y")){ %>
																		<!-- <i class="fa fa-sun-o fa-2x " aria-hidden="true" value="Approve" -->
																		<i class="fa fa-flag fa-2x" aria-hidden="true" value="Approve"
																	 	onclick="doGenXML('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
																		 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																		 '<%=VTYPE_FLAG.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','<%=VBU_STATE_TIN.elementAt(i)%>',
																		 '<%=VSAP_APPROVAL_FLAG.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>');" 
																		 style="color:#00cc00;"> </i>
																	<%}else{ %> 
																		<i class="fa fa-flag fa-2x" style="pointer-events: none; opacity: .65; color: gray;"></i>
																		<!--  style="color:#ff6600;"></i> -->
																	<%} %>
																<%} %>	
																</td>	
																<td align="center">
																	<%if(owner_cd.equals("1")){ %>
																			<span class="fa-stack fa-lg">
																			  <i class="fa fa-flag fa-stack-1x"></i>
																			  <i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
																			</span>
																	<%}else{%>
																		<%if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y") && approve_access.equals("Y")){ %>
																			<i class="fa fa-thumbs-o-up fa-2x" aria-hidden="true" value="Approve"
																	 		onclick="doReApproveXML('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
																		 	'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																		 	'<%=VTYPE_FLAG.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','<%=VBU_STATE_TIN.elementAt(i)%>',
																		 	'<%=VSAP_APPROVAL_FLAG.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>');" 
																		 	style="color:#00cc00;"> </i>
																		<%}else{%> 
																			<i class="fa fa-thumbs-o-up fa-2x" style="pointer-events: none; opacity: .65; color: gray;"></i>
																			<!--  style="color:#ff6600;"></i> -->
																		<%} %>
																	<%} %>
																</td>
																<td align="center">
																<%if(owner_cd.equals("1")){ %>
																	Not Applicable		
																<%}else{%>
																	<%=VAPPROVE_HIST.elementAt(i) %>
																<%} %>
																</td>		 											
															</tr>
																<%if(k==index)
																{i=i+1;
																	break;
																} %>
															<%} %>
														<%}else{ %>
															<tr>
																<td colspan="26" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
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
				<%}%>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="counterparty_cd" value="">
<input type="hidden" name="bu_state_tin" value="">
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
<input type="hidden" name="invoice_no" value="">
<input type="hidden" name="option" value="">
<input type="hidden" name="acc_size" value="<%=VINDEX.size()%>">

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
	var acc_size=document.forms[0].acc_size.value; 
	for(k=0;k<acc_size;k++)
	{
		$('#tbsearch'+k+' th').each(function(i){
			//alert(i)
			var title = $(this).text();
			if(title == "Sr#")
			{
				//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
			}
			else
			{
				$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'_'+k+'" onkeyup="Search(this,'+i+','+k+');" placeholder="Search '+title+'" style="width:100px"/></div>');
			}
		});
	}
	
});

function Search(obj, indx,k) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+k);
  	
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