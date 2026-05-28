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
	//var inv_type = document.forms[0].inv_type.value;
	
	var u = document.forms[0].u.value;
	
	var flag = checkDateRangeOnApply(document.forms[0].from_dt,document.forms[0].to_dt);
	if(flag)
	{
		var url = "frm_derivatives_invoice_approval.jsp?u="+u+
				"&from_dt="+from_dt+"&to_dt="+to_dt;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}

var newWindow;

function doGenXML(couterpty_cd,invoice_no,financial_year,invoice_seq,contract_type,invoice_type,bu_state_tin,sap_approval_flag,agmt_no,cont_no)
{
	/*var a=confirm("Do you want to Generate XML?")
	if(a)
	{
		document.forms[0].counterparty_cd.value=couterpty_cd;
		document.forms[0].invoice_no.value=invoice_no;
		document.forms[0].financial_year.value=financial_year;
		document.forms[0].invoice_seq.value=invoice_seq;
		document.forms[0].contract_type.value=contract_type;
		document.forms[0].type_flag.value=type_flag;
		document.forms[0].invoice_type.value=invoice_type;
		
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].option.value="GENERATE_INVOICE_XML"
		document.forms[0].submit();
	}*/
	var u = document.forms[0].u.value;
	
	var url = "rpt_view_derivatives_invoice_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
			"&contract_type="+contract_type+"&invoice_type="+invoice_type+
			"&counterparty_cd="+couterpty_cd+"&invoice_no="+invoice_no+"&bu_state_tin="+bu_state_tin+
			"&sap_approval_flag="+sap_approval_flag+"&agmt_no="+agmt_no+"&cont_no="+cont_no;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Derivatives SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Derivatives SAP Approval","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	//var inv_type = document.forms[0].inv_type.value;
	
	var url = "xls_derivatives_invoice_approval.jsp?from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}
</script>

</head>
<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Invoice" id="derv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
//String inv_type=request.getParameter("inv_type")==null?"":request.getParameter("inv_type");

derv.setCallFlag("DERV_INV_APPROVAL");
derv.setComp_cd(owner_cd);
derv.setFrom_dt(from_dt);
derv.setTo_dt(to_dt);
//derv.setInv_type(inv_type);
derv.init();

Vector VMST_INV_TYPE = derv.getVMST_INV_TYPE();
Vector VMST_INV_TYPE_FLG = derv.getVMST_INV_TYPE_FLG();
Vector VBU_NM = derv.getVBU_PLANT_ABBR();
Vector VCOUNTERPARTY_CD = derv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = derv.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = derv.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = derv.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = derv.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = derv.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = derv.getVCONT_NO();
Vector VAGMT_NO = derv.getVAGMT_NO();
Vector VDIS_CONT_MAPPING = derv.getVDEAL_MAPPING();
Vector VCONTRACT_TYPE = derv.getVCONT_TYPE();
Vector VPERIOD_START_DT = derv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = derv.getVPERIOD_END_DT();
Vector VFINANCIAL_YEAR = derv.getVFIN_YEAR();
Vector VINVOICE_NO = derv.getVINVOICE_NO();
Vector VINVOICE_SEQ = derv.getVINVOICE_SEQ();
Vector VBU_STATE_TIN = derv.getVBU_STATE_TIN();
Vector VINVOICE_DT = derv.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = derv.getVINVOICE_DUE_DT();
Vector VSALES_PRICE = derv.getVSELL_RATE();
Vector VSALES_PRICE_CD = derv.getVSELL_PRICE_CD();
Vector VSALES_PRICE_NM = derv.getVSELL_PRICE_NM();
Vector VGROSS_AMT = derv.getVGROSS_AMT();
Vector VINVOICE_AMT = derv.getVINVOICE_AMT();
Vector VNET_PAYABLE_AMT = derv.getVNET_PAYABLE_AMT();
Vector VPAY_RECV_AMT = derv.getVPAY_RECV_AMT();
Vector VPAY_RECV_DT = derv.getVPAY_RECV_DT();
Vector VSHORT_RECEIVED = derv.getVSHORT_RECEIVED();
Vector VSAP_APPROVAL_FLAG = derv.getVSAP_APPROVAL_FLG();
Vector VINVOICE_TYPE = derv.getVINVOICE_TYPE();
Vector VINDEX = derv.getVINDEX();

Vector VALLOC_QTY = derv.getVALLOC_QTY();
Vector VALLOC_QTY_UNIT = derv.getVALLOC_QTY_UNIT();
Vector VINVOICE_RAISED_IN = derv.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = derv.getVPAYMENT_DONE_IN();
Vector VSALES_AMT = derv.getVSELL_AMT();
Vector VCONT_REF_NO= derv.getVCONT_REF();
Vector VCASH_FLOW= derv.getVCASH_FLOW();
Vector VINV_REF= derv.getVINV_REF();
Vector VFIN_SYS= derv.getVFIN_SYS();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Accounting">

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
					    	Derivatives Actual Report
					    </div>
					    <div align="right" onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
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
									<div class="col">
										<div class="input-group input-group-sm" >
											<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
											onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
											<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
										</div>
									</div>
									<div class="col-auto">
										<label class="form-label"><b>To</b></label>
									</div>
									<div class="col">
										<div class="input-group input-group-sm" >
											<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
											onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
											<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
										</div>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
											<div class="col-auto">
												<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
											</div>
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
				<%int l=0;
					int m=0;
					int j=0;
					int i=0;
					int k=0;
					for(l=0; l<VMST_INV_TYPE_FLG.size();l++){
					 int index=Integer.parseInt(""+VINDEX.elementAt(l));%>
					 <div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading1">
										<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l %>" aria-expanded="false" aria-controls="collapse<%=l%>"><%=VMST_INV_TYPE.elementAt(l) %>&nbsp;<font color="blue">(<%=index%> Items)</font></button>	
							    	</h2>
							    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading1">
							    		<div class="accordion-body accor-body">
											<div class="row">
												<div class="table-responsive">
													<table class="table table-bordered table-hover" id="example<%=l%>">
														<thead>
															<tr>
																<th rowspan="2">SR#</th>
																<th rowspan="2">Trader
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Customer<%=l%>" onkeyup="Search(this,'1','<%=l%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<th rowspan="2"><%=owner_abbr%> BU
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_bu<%=l%>" onkeyup="Search(this,'2','<%=l%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<th rowspan="2">Contract#
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Contract<%=l%>" onkeyup="Search(this,'3','<%=l%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<th rowspan="2">Contract/Trade Ref#
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_ContractRef<%=l%>" onkeyup="Search(this,'4','<%=l%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<th rowspan="2">Billing Period
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_billinng period<%=l%>" onkeyup="Search(this,'5','<%=l%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<th rowspan="2">Cash Flow
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_billinng period<%=l%>" onkeyup="Search(this,'6','<%=l%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<th rowspan="2">Invoice#
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_InvoiceNo<%=l%>" onkeyup="Search(this,'7','<%=l%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<%if(VMST_INV_TYPE_FLG.elementAt(l).equals("R")){ %>
																<th rowspan="2">Remittance#
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_RemNo<%=l%>" onkeyup="Search(this,'8','<%=l%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<%} %>
																<th rowspan="2">Invoice Date</th>
																<th rowspan="2">Invoice Due Date</th>
																<th rowspan="2">Invoiced Qty</th>
																<th rowspan="2">Qty Unit</th>
																<!-- <th rowspan="2">Rate</th>
																<th rowspan="2" style="background: #000066; color: white;">Rate Unit / MMBTU</th> -->
																<th rowspan="2">Sales Amount</th>
																<th rowspan="2" style="background: #000066; color: white;">Invoice Raised In</th>
																<th rowspan="2" style="background: #000066; color: white;">Invoice Paid In</th>
																<th rowspan="2">Gross Amount</th>
																<th rowspan="2">Invoice Amount</th>
																<%if(VMST_INV_TYPE_FLG.elementAt(l).equals("R")){ %>
																<th rowspan="2">Net Payable</th>
																<%}else{ %>
																<th rowspan="2">Net Receivable</th>
																<%} %>
																<th rowspan="2">SAP View</th>
															</tr>
														</thead>
														<tbody>
														<%k=0;
														if(index > 0){ %>
															<%for(i=i; i<VCOUNTERPARTY_CD.size(); i++){ 
															k+=1;%>
															<tr>
																<td align="center"><%=k%></td>
																<td title="<%=VCOUNTERPARTY_NM.elementAt(i)%>">
																	<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
																	<input type="hidden" name="counterparty_cd" id="counterparty_cd<%=i%>" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>" disabled>
																	<input type="hidden" name="agmt_no" id="agmt_no<%=i%>" value="<%=VAGMT_NO.elementAt(i)%>" disabled>
																	<input type="hidden" name="cont_no" id="cont_no<%=i%>" value="<%=VCONT_NO.elementAt(i)%>" disabled>
																	<input type="hidden" name="contract_type" id="contract_type<%=i%>" value="<%=VCONTRACT_TYPE.elementAt(i)%>" disabled>
																	<input type="hidden" name="financial_year" id="financial_year<%=i%>" value="<%=VFINANCIAL_YEAR.elementAt(i)%>" disabled>
																	<input type="hidden" name="invoice_seq" id="invoice_seq<%=i%>" value="<%=VINVOICE_SEQ.elementAt(i)%>" disabled>
																	<input type="hidden" name="bu_state_tin" id="bu_state_tin<%=i%>" value="<%=VBU_STATE_TIN.elementAt(i)%>" disabled>
																</td>
																<td align="center"><%=VBU_NM.elementAt(i) %></td>
																<td align="center"><div style="width:300px; word-wrap: break-word; white-space: normal;"><%=VDIS_CONT_MAPPING.elementAt(i) %></div></td>
																<td align="center"><div style="width:300px; word-wrap: break-word; white-space: normal;"><%=VCONT_REF_NO.elementAt(i) %></div></td>
																<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
																<td align="center"><%=VCASH_FLOW.elementAt(i)%></td>
																<%if(VMST_INV_TYPE_FLG.elementAt(l).equals("R")){ %>
																	<td align="center"><%=VINV_REF.elementAt(i)%></td>
																	<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
																<%}else{ %>
																	<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
																<%} %>
																<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
																<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
																<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
																<td align="right"><%=VALLOC_QTY_UNIT.elementAt(i) %></td>
																<%-- <td align="right"><%=VSALES_PRICE.elementAt(i)%></td>
																<td align="center" style="background: #b3f0ff;"><%=VSALES_PRICE_NM.elementAt(i) %></td> --%>
																<td align="right"><%=VSALES_AMT.elementAt(i)%></td>
																<td style="background: #b3f0ff;"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>
																<td style="background: #b3f0ff;"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
																<td> <%=VGROSS_AMT.elementAt(i)%>
																	<input type="hidden" name="gross_amt" id="gross_amt<%=i%>" value="<%=VGROSS_AMT.elementAt(i)%>">
																</td>
																<td align="right">
																	<%=VINVOICE_AMT.elementAt(i) %>
																	<input type="hidden" name="invoice_amt" id="invoice_amt<%=i%>" value="<%=VINVOICE_AMT.elementAt(i)%>">
																</td>
																<td align="right">
																	<%=VNET_PAYABLE_AMT.elementAt(i)%>
																</td>	
																<td align="center">
																<%if(!owner_cd.equals("1") && utildate.getDays(""+VINVOICE_DT.elementAt(i),"01/04/2026")<=0){ %>
																	<%if(VFIN_SYS.elementAt(i).equals("S")){%>
																		<span>
																			 <i class="fa fa-sun-o fa-2x" aria-hidden="true" style="color:orange;"></i>
																		</span>
																	<%}else{%>
																		<span class="fa-stack fa-lg">
																		  <i class="fa fa-eye fa-stack-1x"></i>
																		  <i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
																		</span>
																	<%} %>
																<%}else{ %>
																	<i class="fa <%if(VSAP_APPROVAL_FLAG.elementAt(i).equals("Y")) {%>fa-eye<%} %> fa-2x"
																	onclick="doGenXML('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
																	 '<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
																	 '<%=VINVOICE_TYPE.elementAt(i)%>','<%=VBU_STATE_TIN.elementAt(i)%>',
																	 '<%=VSAP_APPROVAL_FLAG.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>');"
																	 style="color: brown;"
																	></i>
																<%} %>
																</td>			 											
															</tr>
															<%if(k==index){
																i=i+1;
																break;
															} %>
															<%} %>
														<%}else{ %>
															<tr>
																<td colspan="19" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
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
function Search(obj, indx, j) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+j);
  	
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