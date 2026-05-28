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
	
	var flag=true;
	var msg="";
	var flag=checkDateRangeOnApply(document.forms[0].from_dt,document.forms[0].to_dt);
	if(flag)
	{
		if(trim(from_dt)!="" && trim(to_dt)!="")
		{
			if(flag==true)
			{
				var url = "../extn_interface/frm_oth_invoice_sun_acc_approval.jsp?u="+u+"&segment="+segment+
							"&from_dt="+from_dt+"&to_dt="+to_dt;
				document.getElementById("loading").style.visibility = "visible";
				location.replace(url);
			}
		}
	}
}


function doSubmit()
{
	var msg="";
	var flag=true;
	
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

var newWindow;

function doGenXML(supp_cd,inv_type,fin_yr,inv_seq,bu_seq,bu_state_tin,invoice_no,inv_flag)
{
	var a=confirm("Do you want to approve invoice for SUN XML generation?")
	if(a)
	{
		document.forms[0].supp_cd.value=supp_cd;
		document.forms[0].invoice_type.value=inv_type;
		document.forms[0].financial_year.value=fin_yr;
		document.forms[0].invoice_seq.value=inv_seq;
		document.forms[0].bu_seq.value=bu_seq;
		document.forms[0].bu_state_tin.value=bu_state_tin;
		document.forms[0].invoice_no.value=invoice_no;
		document.forms[0].inv_flag.value=inv_flag;
		
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].option.value="APPROVE_OTH_INVOICE_SUN_XML"
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

function doReApproveXML(supp_cd,inv_type,fin_yr,inv_seq,bu_seq,bu_state_tin,invoice_no,inv_flag)
{
	var a=confirm("Do you want to re-approve invoice for SUN XML generation?");
	if(a)
	{
		document.forms[0].supp_cd.value=supp_cd;
		document.forms[0].invoice_type.value=inv_type;
		document.forms[0].financial_year.value=fin_yr;
		document.forms[0].invoice_seq.value=inv_seq;
		document.forms[0].bu_seq.value=bu_seq;
		document.forms[0].bu_state_tin.value=bu_state_tin;
		document.forms[0].invoice_no.value=invoice_no;
		document.forms[0].inv_flag.value=inv_flag;
		
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].option.value="RE-APPROVE_OTH_INVOICE_SUN_XML"
		document.forms[0].submit();
	}
}

</script>

</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_oth_inv_sun_interface" id="accounting" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");

accounting.setCallFlag("OTH_INV_SUN_APPROVAL");
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
Vector VVENDOR_CD = accounting.getVVENDOR_CD();
Vector VVENDOR_NM = accounting.getVVENDOR_NM();
Vector VSUPPLIER_CD = accounting.getVSUPPLIER_CD();
Vector VINVOICE_TYPE = accounting.getVINVOICE_TYPE();
Vector VPERIOD_START_DT = accounting.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = accounting.getVPERIOD_END_DT();
Vector VFINANCIAL_YEAR = accounting.getVFINANCIAL_YEAR();
Vector VINVOICE_NO = accounting.getVINVOICE_NO();
Vector VINVOICE_SEQ = accounting.getVINVOICE_SEQ();
Vector VBU_SEQ = accounting.getVBU_SEQ();
Vector VBU_STATE_TIN = accounting.getVBU_STATE_TIN();
Vector VINVOICE_DT = accounting.getVINVOICE_DT();
Vector VINVOICE_DUE_DT = accounting.getVINVOICE_DUE_DT();
Vector VGROSS_AMT = accounting.getVGROSS_AMT();
Vector VTAX_AMT = accounting.getVTAX_AMT();
Vector VINVOICE_AMT = accounting.getVINVOICE_AMT();
Vector VNET_PAYABLE_AMT = accounting.getVNET_PAYABLE_AMT();
Vector VTDS_GROSS_AMT = accounting.getVTDS_GROSS_AMT();
Vector VSAP_APPROVAL_FLAG = accounting.getVSAP_APPROVAL_FLAG();
Vector VTYPE_FLAG = accounting.getVTYPE_FLAG();
Vector VINVOICE_RAISED_IN = accounting.getVINVOICE_RAISED_IN();
Vector VPAYMENT_DONE_IN = accounting.getVPAYMENT_DONE_IN();
Vector VCASH_FLOW= accounting.getVCASH_FLOW();
Vector VINDEX=accounting.getVINDEX();
Vector VAPPROVE_HIST=accounting.getVAPPROVE_HIST();
Vector VINV_FLAG=accounting.getVINV_FLAG();

%>
<body>
<%@ include file="../home/header.jsp"%>
<%if(!owner_cd.equals("2")) {%>
<div class="box-body">
	<div class="row">
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
		<div class="col-md-8 col-sm-8 col-xs-8">
			<div class="card cardmain">
				<div class="card-header cdheader ">
				</div>
				<div class="card-body cdbody">
					<div class="alert alert-info">
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="form-group row" align="center">
									<label class="form-label"  style="font-size:40px;font-weight: 700;"><i class='fa fa-exclamation-circle fa-lg'></i> Feature Not Supported</label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
				</div>
			</div>   
		</div>
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
	</div>
</div>
<%}else{ %>
<form method="post" action="../servlet/Frm_oth_inv_sun_interface">

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
					    	Other Invoice SUN Approval
					    </div>
					    <div class="btn-group">
							<select class="btn btn-outline-secondary btngrp btnactive" name="segment" onchange="">
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
						<div class="col-sm-12 col-xs-12 col-md-12">
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
									<div class="col">
										<input type="button" class="btn btn-warning com-btn" value="Apply Filters" onclick="refresh();">
				  					</div>
								</div>
							</div>
						</div>
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
													<table class="table table-bordered table-hover ems_sorttbl" id="example<%=l%>">
														<thead id="tbsearch<%=l%>">
															<tr>
																<th rowspan="2">SR#</th>
																<th rowspan="2">Vendor</th>
																<th rowspan="2">Business Unit</th>
																<th rowspan="2">Cash Flow</th>
																<th rowspan="2">Invoice#</th>
																<th rowspan="2">Invoice Date</th>
																<th rowspan="2">Invoice Due Date</th>
																<th rowspan="2" style="background: #000066; color: white;">Invoice Raised In</th>
																<th rowspan="2" style="background: #000066; color: white;">Invoice Paid In</th>
																<th rowspan="2">Gross Amount</th>
																<th rowspan="2">Tax Amount</th>
																<th rowspan="2">Invoice Amount</th>
																<th rowspan="2">Net Receivable</th>
																<th rowspan="2">SUN Approval</th>
																<th rowspan="2">SUN Re-Approval</th>
																<th rowspan="2">SUN Approved By/On</th>
															</tr>
														</thead>
														<tbody>
														<% k=0;
															if(index > 0){ %>
															<%for(i=i; i<VVENDOR_CD.size(); i++){
																k+=1;%>
															<tr>
																<td align="center"><%=k%></td>
																<td title="<%=VVENDOR_CD.elementAt(i)%>">
																	<%=VVENDOR_NM.elementAt(i)%>
																</td>
																<td align="center"><%=VBU_NM.elementAt(i) %></td>
																<td align="center"><%=VCASH_FLOW.elementAt(i)%></td>
																<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
																<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
																<td align="center"><%=VINVOICE_DUE_DT.elementAt(i)%></td>
																<td align="center" style="background: #b3f0ff;"><%=VINVOICE_RAISED_IN.elementAt(i) %></td>
																<td align="center" style="background: #b3f0ff;"><%=VPAYMENT_DONE_IN.elementAt(i) %></td>
																<td align="right"> <%=VGROSS_AMT.elementAt(i)%>
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
																	 	onclick="doGenXML('<%=VSUPPLIER_CD.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>',
																		 	'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VBU_SEQ.elementAt(i)%>',
																		 	'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>','<%=VINV_FLAG.elementAt(i)%>');"
																		 style="color:#00cc00;"> </i>
																	<%}else{ %> 
																		<i class="fa fa-flag fa-2x" style="pointer-events: none; opacity: .65; color: gray;"></i>
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
																	 		onclick="doReApproveXML('<%=VSUPPLIER_CD.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>',
																		 		'<%=VFINANCIAL_YEAR.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VBU_SEQ.elementAt(i)%>',
																		 		'<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>','<%=VINV_FLAG.elementAt(i)%>');"
																		 	style="color:#00cc00;"> </i>
																		<%}else{%> 
																			<i class="fa fa-thumbs-o-up fa-2x" style="pointer-events: none; opacity: .65; color: gray;"></i>
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
																<td colspan="26" align="center"><%=utilmsg.infoMessage("<b>No "+VTEMP_SEGMENT.elementAt(l)+" Invoice is Generated for Selected Period!</b>") %></td>
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

<input type="hidden" name="option" value="APPROVE_OTH_INVOICE_SUN_XML">
<input type="hidden" name="supp_cd" value="">
<input type="hidden" name="invoice_type" value="">
<input type="hidden" name="financial_year" value="">
<input type="hidden" name="invoice_seq" value=""> 
<input type="hidden" name="bu_seq" value=""> 
<input type="hidden" name="bu_state_tin" value="">
<input type="hidden" name="invoice_no" value="">
<input type="hidden" name="inv_flag" value="">
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
<%} %>
</body>
<script>

$(document).ready(function() {
	var acc_size=document.forms[0].acc_size.value; 
	for(k=0;k<acc_size;k++)
	{
		$('#tbsearch'+k+' th').each(function(i){
			//alert(i)
			var title = $(this).text();
			if(title == "SR#")
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